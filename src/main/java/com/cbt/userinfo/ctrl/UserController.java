package com.cbt.userinfo.ctrl;

import com.cbt.bean.Address;
import com.cbt.bean.BalanceBean;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderPaymentBean;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.dao.RefundDaoPlus;
import com.cbt.dao.impl.RefundDaoImpl;
import com.cbt.pay.service.IOrderServer;
import com.cbt.pojo.AddBalanceInfo;
import com.cbt.pojo.RechangeRecord;
import com.cbt.pojo.UserEx;
import com.cbt.refund.bean.RefundBean;
import com.cbt.service.AdditionalBalanceService;
import com.cbt.service.IPaymentService;
import com.cbt.service.IRechargeRecordService;
import com.cbt.service.RefundSSService;
import com.cbt.userinfo.service.IUserInfoService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.CustomerFinancialBean;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userinfo")
public class UserController {
    @Autowired
    private RefundSSService refundSSService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IOrderServer iOrderServer;

    @Autowired
    private IRechargeRecordService rechargeRecordService;
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private AdditionalBalanceService additionalBalanceService;
    private DecimalFormat format = new DecimalFormat("#0.00");
    private PaymentDaoImp paymentDao = new PaymentDao();

    private RefundDaoPlus refundDao = new RefundDaoImpl();



    @RequestMapping(value = "/checkUserBalance.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> checkUserBalance(HttpServletRequest request, Model model) throws ParseException {
        Map<String,Object> rsMap = new HashMap<String,Object>();
        String strUserId = request.getParameter("userId");
        if (StringUtils.isBlank(strUserId)) {
            return rsMap;
        }
        int userId = Integer.parseInt(strUserId);

        try{

            Map<String, Object> listu = userInfoService.getUserCount(userId);
            // I总的实际到账金额 payment 中 paypal+wiretransfer
            Map<String, Double> paysUserid = paymentDao.getPaysUserid(userId);

            CustomerFinancialBean financial = new CustomerFinancialBean();
            financial.setUserId(userId);
            double totalPaypal = paysUserid.get("paypal");//总的PayPal支付
            financial.setPayPalPay(BigDecimalUtil.truncateDouble(totalPaypal, 2));
            double wireTransferPay = paysUserid.get("wiretransfer");//总的wireTransfer支付
            financial.setWireTransferPay(BigDecimalUtil.truncateDouble(wireTransferPay, 2));
            double actualPayAmount = paysUserid.get("all");//总的实际到账金额
            financial.setActualPayAmount(BigDecimalUtil.truncateDouble(actualPayAmount, 2));
            double balancePay = paysUserid.get("balancePay");//总的余额支付
            financial.setBalancePay(BigDecimalUtil.truncateDouble(balancePay, 2));
            double payForBalance = paysUserid.get("payForBalance");//充值
            financial.setPayForBalance(BigDecimalUtil.truncateDouble(payForBalance, 2));


            IOrderwsDao order = new OrderwsDao();
            double actualOrderAmount = order.getOrdersPayUserid(userId);// 总的实际完成订单金额
            financial.setActualOrderAmount(BigDecimalUtil.truncateDouble(actualOrderAmount, 2));
            double cancelOrderAmount = order.getOrdersCancelUserid(userId);//被取消的订单金额
            financial.setCancelOrderAmount(BigDecimalUtil.truncateDouble(cancelOrderAmount, 2));

            //double totalOrderAmount = totalPaypal + wireTransferPay + balancePay + stripePay;//总的下订单总金额
            double totalOrderAmount = actualOrderAmount + cancelOrderAmount;//总的下订单总金额
            financial.setTotalOrderAmount(BigDecimalUtil.truncateDouble(totalOrderAmount, 2));



            double totalApplyRefund = refundSSService.getApplyRefund(userId);// 总的申请退款或提现金额
            //double totalApplyPaypal = refundSSService.getApplyPaypal(userId);// 总的已经退款或提现金额
            double hasRefundAmount = refundSSService.getRefund(userId);// 已退款金额
            financial.setHasRefundAmount(BigDecimalUtil.truncateDouble(hasRefundAmount, 2));
            double totalDealRefund = totalApplyRefund - hasRefundAmount;//退款或提现处理中
            financial.setDealRefundAmount(BigDecimalUtil.truncateDouble(totalDealRefund, 2));
            double apiRefundAmount = 0;//系统API退款
            financial.setApiRefundAmount(BigDecimalUtil.truncateDouble(apiRefundAmount, 2));
            double manualRefundAmount = hasRefundAmount - apiRefundAmount;//手动退款金额
            financial.setManualRefundAmount(BigDecimalUtil.truncateDouble(manualRefundAmount, 2));


            double compensateAmount = additionalBalanceService.getMoneyAmount(userId);// 总的额外奖励或者补偿
            financial.setCompensateAmount(BigDecimalUtil.truncateDouble(compensateAmount, 2));

            //算法1：账号应有余额 = 总的实际到账金额 - 总的实际完成订单金额 + 额外奖励或补偿-实际发放的  退款或提现

            double dueBalance1 = actualPayAmount - actualOrderAmount + compensateAmount - totalApplyRefund;
            financial.setDueBalance(BigDecimalUtil.truncateDouble(dueBalance1, 2));

            //算法2：账号应有余额 = 被取消的订单金额 - 余额支付的金额 + 额外奖励或补偿-实际发放的  退款或提现 + 充值
            double dueBalance2 = cancelOrderAmount - balancePay + compensateAmount - totalApplyRefund + payForBalance;

            double currentBalance = 0;////当前余额
            if (listu != null) {
                // 用户emailemail
                currentBalance = (Double) listu.get("available_m");
            }
            financial.setCurrentBalance(BigDecimalUtil.truncateDouble(currentBalance, 2));
            financial.setAvailableBalance(BigDecimalUtil.truncateDouble(currentBalance, 2));
            if (Math.abs(dueBalance1 - currentBalance) >= 0.01) {
                rsMap.put("error",1);
            }else{
                rsMap.put("error",0);
            }
            double blockedBalance = totalDealRefund;
            financial.setBlockedBalance(BigDecimalUtil.truncateDouble(blockedBalance, 2));
            rsMap.put("error",0);
            rsMap.put("dueBalance1",BigDecimalUtil.truncateDouble(dueBalance1, 2));
            rsMap.put("dueBalance2",BigDecimalUtil.truncateDouble(dueBalance2, 2));
            rsMap.put("userId",userId);
            rsMap.put("currentBalance",currentBalance);
        }catch (Exception e){
            rsMap.clear();
        }
        return rsMap;
    }

    @RequestMapping(value = "/findAllUser", method = RequestMethod.POST)
    @ResponseBody
    public List<String> findAllUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
        List<String> admuserList = new ArrayList<String>();
        try {
            String admName= StringUtil.isBlank(user.getAdmName())?null:user.getAdmName();
            admuserList = userInfoService.getAllAdmuser(admName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admuserList;
    }



    @RequestMapping(value = "/getUserInfo.do", method = RequestMethod.GET)
    public String getOrderInfo(HttpServletRequest request, Model model) throws ParseException {
        String strUserId = request.getParameter("userId");
        if (StringUtils.isBlank(strUserId)) {
            return "userInfo";
        }
        request.setAttribute("userId", strUserId);
        int userId = Integer.parseInt(strUserId);

        Map<String, Object> listu = userInfoService.getUserCount(userId);
//		System.err.println("listu:"+listu.toString());
//		DataSourceSelector.restore();
        request.setAttribute("user", listu);
        //@author  zhulg 获取扩展用户信息
        UserEx userex = userInfoService.getUserEx(userId);
        request.setAttribute("userex", userex);
        //获取用户地址
        List<Address> addresslist = iOrderServer.getUserAddr(userId);
        request.setAttribute("addresslist", addresslist);
        //获取paypal账号
        List<String> paypayList = userInfoService.getPaypal(userId);
        request.setAttribute("paypays", paypayList);


        // I总的实际到账金额 payment 中 paypal+wiretransfer
        Map<String, Double> paysUserid = paymentDao.getPaysUserid(userId);


        CustomerFinancialBean financial = new CustomerFinancialBean();
        financial.setUserId(userId);
        double totalPaypal = paysUserid.get("paypal");//总的PayPal支付
        financial.setPayPalPay(BigDecimalUtil.truncateDouble(totalPaypal, 2));
        double wireTransferPay = paysUserid.get("wiretransfer");//总的wireTransfer支付
        financial.setWireTransferPay(BigDecimalUtil.truncateDouble(wireTransferPay, 2));
        double actualPayAmount = paysUserid.get("all");//总的实际到账金额
        financial.setActualPayAmount(BigDecimalUtil.truncateDouble(actualPayAmount, 2));
        double balancePay = paysUserid.get("balancePay");//总的余额支付
        financial.setBalancePay(BigDecimalUtil.truncateDouble(balancePay, 2));
        double stripePay = paysUserid.get("stripePay");//总的stripePay支付
        financial.setStripePay(BigDecimalUtil.truncateDouble(stripePay, 2));
        double payForBalance = paysUserid.get("payForBalance");//充值
        financial.setPayForBalance(BigDecimalUtil.truncateDouble(payForBalance, 2));



        IOrderwsDao order = new OrderwsDao();
        double actualOrderAmount = order.getOrdersPayUserid(userId);// 总的实际完成订单金额
        financial.setActualOrderAmount(BigDecimalUtil.truncateDouble(actualOrderAmount, 2));
        double cancelOrderAmount = order.getOrdersCancelUserid(userId);//被取消的订单金额
        financial.setCancelOrderAmount(BigDecimalUtil.truncateDouble(cancelOrderAmount, 2));

        //double totalOrderAmount = totalPaypal + wireTransferPay + balancePay + stripePay;//总的下订单总金额
        double totalOrderAmount = actualOrderAmount + cancelOrderAmount;//总的下订单总金额
        financial.setTotalOrderAmount(BigDecimalUtil.truncateDouble(totalOrderAmount, 2));



        double totalApplyRefund = refundSSService.getApplyRefund(userId);// 总的申请退款或提现金额
        //double totalApplyPaypal = refundSSService.getApplyPaypal(userId);// 总的已经退款或提现金额
        double hasRefundAmount = refundSSService.getRefund(userId);// 已退款金额
        financial.setHasRefundAmount(BigDecimalUtil.truncateDouble(hasRefundAmount, 2));
        double totalDealRefund = totalApplyRefund - hasRefundAmount;//退款或提现处理中
        financial.setDealRefundAmount(BigDecimalUtil.truncateDouble(totalDealRefund, 2));

        double apiRefundAmount = 0;//系统API退款
        financial.setApiRefundAmount(BigDecimalUtil.truncateDouble(apiRefundAmount, 2));
        double manualRefundAmount = hasRefundAmount - apiRefundAmount;//手动退款金额
        financial.setManualRefundAmount(BigDecimalUtil.truncateDouble(manualRefundAmount, 2));


        double compensateAmount = additionalBalanceService.getMoneyAmount(userId);// 总的额外奖励或者补偿
        financial.setCompensateAmount(BigDecimalUtil.truncateDouble(compensateAmount, 2));

        //算法1：账号应有余额 = 总的实际到账金额 - 总的实际完成订单金额 + 额外奖励或补偿-实际发放的  退款或提现

        double dueBalance = actualPayAmount - actualOrderAmount + compensateAmount - totalApplyRefund;
        financial.setDueBalance(BigDecimalUtil.truncateDouble(dueBalance, 2));

        //算法2：账号应有余额 = 被取消的订单金额 - 余额支付的金额 + 额外奖励或补偿-实际发放的  退款或提现 + 充值
        dueBalance = cancelOrderAmount - balancePay + compensateAmount - totalApplyRefund + payForBalance;

        financial.setDueBalance2(BigDecimalUtil.truncateDouble(dueBalance, 2));

        double currentBalance = 0;////当前余额
        if (listu != null) {
            // 用户emailemail
            currentBalance = (Double) listu.get("available_m");
        }
        financial.setCurrentBalance(BigDecimalUtil.truncateDouble(currentBalance, 2));
        financial.setAvailableBalance(BigDecimalUtil.truncateDouble(currentBalance, 2));
        if (Math.abs(dueBalance - currentBalance) >= 0.01) {
            financial.setWarnFlag(1);
            String warnMsg = "客户实际余额(" + BigDecimalUtil.truncateDouble(currentBalance, 2)
                    + ")不等于" + "应有余额(" + BigDecimalUtil.truncateDouble(dueBalance, 2) + ")";
            financial.setWarnMsg(warnMsg);
        }
        double blockedBalance = totalDealRefund;
        financial.setBlockedBalance(BigDecimalUtil.truncateDouble(blockedBalance, 2));
        request.setAttribute("financial", financial);

        //收支
        List<Map<String, Object>> payDetailByUserid = paymentDao.payDetailByUserid(userId, 1);
        if (payDetailByUserid != null) {
            Map<String, Object> map = payDetailByUserid.get(payDetailByUserid.size() - 1);
            payDetailByUserid.remove(payDetailByUserid.size() - 1);
            request.setAttribute("sumMap", map);
        }

        request.setAttribute("payDetailList", payDetailByUserid);

        int count = payDetailByUserid != null && !payDetailByUserid.isEmpty() ?
                Integer.valueOf(payDetailByUserid.get(0).get("count").toString()) : 0;

        request.setAttribute("payDetailpagenow", count == 0 ? 0 : 1);
        count = count % 20 == 0 ? count / 20 : count / 20 + 1;
        request.setAttribute("payDetailpagecount", count);

        //获取客户的全部订单数据
        List<OrderBean> orderList = paymentDao.queryOrderInfoByUserId(userId);
        //计算实际应付PayPal金额
        for(OrderBean odIf : orderList){
            odIf.setActualPay(0);
        }
        request.setAttribute("orderList", orderList);

        List<RechangeRecord> rcRds = paymentDao.queryRechangeRecordByUserId(userId);
        //统计余额总收入，补偿总收入和余额总支出
        double balanceTotalRevenue = 0;//余额总收入
        double compensationTotalRevenue = 0;//补偿总收入
        double balanceTotalSpending = 0;//余额总支出
        for(RechangeRecord rerd : rcRds){
            if(rerd.getUseSign() == 0){
                balanceTotalRevenue += rerd.getPrice();
            }else{
                balanceTotalSpending += rerd.getPrice();
            }
        }


        List<AddBalanceInfo> adBcs = paymentDao.quertAddBalanceInfoByUserId(userId);
        for(AddBalanceInfo balanceInfo:adBcs){
            compensationTotalRevenue += balanceInfo.getMoney();
        }

        balanceTotalRevenue = balanceTotalRevenue - compensationTotalRevenue;

        List<RefundBean> refundList = paymentDao.quertRefundByUserId(userId);
        request.setAttribute("rechangeList", rcRds);
        request.setAttribute("addBlList", adBcs);
        request.setAttribute("refundList", refundList);

        request.setAttribute("balanceTotalRevenue", BigDecimalUtil.truncateDouble(balanceTotalRevenue,2));
        request.setAttribute("compensationTotalRevenue", BigDecimalUtil.truncateDouble(compensationTotalRevenue,2));
        request.setAttribute("balanceTotalSpending", BigDecimalUtil.truncateDouble(balanceTotalSpending,2));

        return "userInfo";
    }


    @RequestMapping(value = "/upEmail")
    @ResponseBody
    public String upEmail(HttpServletRequest request, String email, String oldemail, int userid) throws ParseException {
        DataSourceSelector.set("dataSource127hop");
        String exitEmail = userInfoService.exitEmail(email);
        if (Utility.getStringIsNull(exitEmail)) {
            return "2";
        }

        String admJson = Redis.hget(request.getSession().getId(), "admuser");
        if (admJson == null) {
            return "3";
        }
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
        int admuserid = user.getId();

        int res = userInfoService.upEmail(email, userid, oldemail, admuserid);
        DataSourceSelector.restore();
        return res + "";
    }

    @RequestMapping(value = "/upPhone")
    @ResponseBody
    public String upPhone(HttpServletRequest request, String oldPhone, String newPhone, int userid) throws ParseException {
        int res = 0;
        try{
            SendMQ sendMQ = new SendMQ();
            DataSourceSelector.set("dataSource127hop");
            String admJson = Redis.hget(request.getSession().getId(), "admuser");
            if (admJson == null) {
                return "3";
            }
            Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
            int admuserid = user.getId();
            String exitEmail = userInfoService.exitPhone(oldPhone, userid);
            if (exitEmail == null) {
                sendMQ.sendMsg(new RunSqlModel("insert into user_ex (userid,otherphone) values('"+userid+"','"+newPhone+"')"));
            } else {
                sendMQ.sendMsg(new RunSqlModel("update user_ex set otherphone='"+newPhone+"' where userid='"+userid+"'"));
            }
            res=1;
            sendMQ.closeConn();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DataSourceSelector.restore();
        }
        return res + "";
    }

    @RequestMapping(value = "/additional")
    @ResponseBody
    public String additionalList(HttpServletRequest request, HttpServletResponse resp) throws ParseException {
        String result = "[]";
        String strUserid = request.getParameter("userid");
        String strPage = request.getParameter("page");
        if (strUserid == null || strUserid.isEmpty()) {
            return result;
        }
        strUserid = strUserid.replaceAll("\\D+", "").trim();
        strUserid = strUserid.isEmpty() ? "0" : strUserid;
        if (strPage == null || strPage.isEmpty()) {
            strPage = "1";
        }
        strPage = strPage.replaceAll("\\D+", "").trim();
        strPage = strPage.isEmpty() ? "1" : strPage;

        int userid = Integer.valueOf(strUserid);
        int page = Integer.valueOf(strPage);
        List<BalanceBean> additionalList = additionalBalanceService.getBalanceByUserId(userid, page);
        int count = additionalList != null && !additionalList.isEmpty() ? additionalList.get(0).getCountTotal() : 0;
        count = count % 20 == 0 ? count / 20 : count / 20 + 1;
        result = JSONArray.fromObject(additionalList).toString();
        return result;
    }

    @RequestMapping(value = "/order")
    @ResponseBody
    public String orderList(HttpServletRequest request, HttpServletResponse resp) throws ParseException {
        String result = "[]";
        String strUserid = request.getParameter("userid");
        String strPage = request.getParameter("page");
        if (strUserid == null || strUserid.isEmpty()) {
            return result;
        }
        strUserid = strUserid.replaceAll("\\D+", "").trim();
        strUserid = strUserid.isEmpty() ? "0" : strUserid;
        if (strPage == null || strPage.isEmpty()) {
            strPage = "1";
        }
        strPage = strPage.replaceAll("\\D+", "").trim();
        strPage = strPage.isEmpty() ? "1" : strPage;

        int userid = Integer.valueOf(strUserid);
        int page = Integer.valueOf(strPage);
        List<OrderPaymentBean> orderPayList = paymentDao.getOrderPayList(userid, page);
        result = JSONArray.fromObject(orderPayList).toString();
        return result;
    }

    @RequestMapping(value = "/payDetail", produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String payDetailList(HttpServletRequest request, HttpServletResponse resp) throws ParseException {
        try {
            request.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        String result = "[]";
        String strUserid = request.getParameter("userid");
        String strPage = request.getParameter("page");
        if (strUserid == null || strUserid.isEmpty()) {
            return result;
        }
        strUserid = strUserid.replaceAll("\\D+", "").trim();
        strUserid = strUserid.isEmpty() ? "0" : strUserid;
        if (strPage == null || strPage.isEmpty()) {
            strPage = "1";
        }
        strPage = strPage.replaceAll("\\D+", "").trim();
        strPage = strPage.isEmpty() ? "1" : strPage;

        int userid = Integer.valueOf(strUserid);
        int page = Integer.valueOf(strPage);
        List<Map<String, Object>> payDetailList = paymentDao.payDetailByUserid(userid, page);
        result = JSONArray.fromObject(payDetailList).toString();
        return result;
    }



    @RequestMapping(value = "/queryOrderInfos", method = RequestMethod.POST)
    @ResponseBody
    public List<String> queryOrderInfos(HttpServletRequest request, HttpServletResponse resp){
        List<String> orderList = new ArrayList<String>();
        String strUserId = request.getParameter("userId");
        if(StringUtils.isNotBlank(strUserId) && !"0".equals(strUserId)){
            List<OrderBean> orders = paymentDao.queryOrderInfoByUserId(Integer.valueOf(strUserId));
            for(OrderBean odb : orders){
                // && odb.getState() != 6 && odb.getState() != -1
                if(odb.getState() != 0){
                    if(orderList.size() > 10){
                        break;
                    }else{
                        orderList.add(odb.getOrderNo());
                    }
                }
            }
        }
        return orderList;
    }

}
