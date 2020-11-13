package com.cbt.userinfo.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.cbt.bean.*;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.dao.RefundDaoPlus;
import com.cbt.dao.impl.RefundDaoImpl;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
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
import com.cbt.website.bean.UserInfo;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.pojo.UserRecommendEmail;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.importExpress.utli.UserInfoUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
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
    private SendMailFactory sendMailFactory;

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

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(RefundDaoImpl.class);

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
        int backList=userInfoService.checkUserName(String.valueOf(listu.get("email")));
        request.setAttribute("backList",backList);
        //获取用户地址
        List<Address> addresslist = iOrderServer.getUserAddr(userId);
        request.setAttribute("addresslist", addresslist);
        //获取paypal账号
        List<String> paypayList = userInfoService.getPaypal(userId);
        //判断paypal账号是否有黑名单
        int payBackList=0;
        for(String username:paypayList){
            payBackList+=userInfoService.checkUserName(username);
        }
        request.setAttribute("paypays", paypayList);
        request.setAttribute("payBackList", payBackList);

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



        double totalComplaintAmount =  refundSSService.queryComplaintTotalAmount(userId);// 申诉退款总金额
        double totalApplyRefund = refundSSService.getApplyRefund(userId) + totalComplaintAmount;// 总的申请退款或提现金额
        //double totalApplyPaypal = refundSSService.getApplyPaypal(userId);// 总的已经退款或提现金额
        double hasRefundAmount = refundSSService.getRefund(userId) + totalComplaintAmount;// 已退款金额
        financial.setHasRefundAmount(BigDecimalUtil.truncateDouble(hasRefundAmount, 2));
        double totalDealRefund = totalApplyRefund - hasRefundAmount;//退款或提现处理中
        financial.setDealRefundAmount(BigDecimalUtil.truncateDouble(totalDealRefund, 2));

        double apiRefundAmount = 0;//系统API退款
        financial.setApiRefundAmount(BigDecimalUtil.truncateDouble(apiRefundAmount, 2));
        double manualRefundAmount = hasRefundAmount - apiRefundAmount;//手动退款金额
        financial.setManualRefundAmount(BigDecimalUtil.truncateDouble(manualRefundAmount, 2));


        double compensateAmount = additionalBalanceService.getMoneyAmount(userId);// 总的额外奖励或者补偿
        financial.setCompensateAmount(BigDecimalUtil.truncateDouble(compensateAmount, 2));

        //算法2：账号应有余额 = 被取消的订单金额 - 余额支付的金额 + 额外奖励或补偿-实际发放的  退款或提现 + 充值
        double dueBalance = cancelOrderAmount - balancePay + compensateAmount - totalApplyRefund + payForBalance;
        financial.setDueBalance2(BigDecimalUtil.truncateDouble(dueBalance, 2));


        //算法1：账号应有余额 = 总的实际到账金额 - 总的实际完成订单金额 + 额外奖励或补偿-实际发放的  退款或提现
        dueBalance = actualPayAmount - actualOrderAmount + compensateAmount - (totalApplyRefund -totalComplaintAmount);
        financial.setDueBalance(BigDecimalUtil.truncateDouble(dueBalance, 2));


        double currentBalance = 0;////当前余额
        double balanceCorrection = 0;

        if (listu != null) {
            // 用户emailemail
            currentBalance = Double.valueOf(listu.get("available_m").toString()) ;
            balanceCorrection = Double.valueOf(listu.get("balance_correction").toString());

        }
        financial.setCurrentBalance(BigDecimalUtil.truncateDouble(currentBalance, 2));
        financial.setAvailableBalance(BigDecimalUtil.truncateDouble(currentBalance, 2));
        financial.setBalanceCorrection(BigDecimalUtil.truncateDouble(balanceCorrection, 2));

        if (Math.abs(currentBalance - dueBalance - balanceCorrection) >= 0.01) {
            financial.setWarnFlag(1);
            String warnMsg = "客户实际余额(" + BigDecimalUtil.truncateDouble(currentBalance, 2)
                    + ")不等于" + "应有余额+强平余额(" + BigDecimalUtil.truncateDouble(dueBalance + balanceCorrection, 2) + ")";
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
        int backAddressCount=0;
        for(OrderBean odIf : orderList){
            odIf.setActualPay(0);
            backAddressCount+=odIf.getBackAddressCount();
        }
        request.setAttribute("orderList", orderList);
        request.setAttribute("backAddressCount",backAddressCount);
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

    @RequestMapping(value = "/queryUserRemark")
    @ResponseBody
    public List<String> queryUserRemark(int userid) {
        return userInfoService.queryUserRemark(userid);
    }
    @RequestMapping(value = "/updateUserRemark")
    @ResponseBody
    public Map<String, String> updateUserRemark(String id) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            userInfoService.updateUserRemark(id);
            result.put("state", "true");
            result.put("message", "删除成功");
        } catch (Exception e){
            result.put("state", "false");
            result.put("message", "删除异常");
            LOG.error("updateUserRemark error, id " + id, e);
        }
        return result;
    }

    @RequestMapping(value = "/addUserRemark")
    @ResponseBody
    public Map<String, String> addUserRemark(int userid, String remark) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            userInfoService.insertUserRemark(userid, remark);
            result.put("state", "true");
            result.put("message", "添加成功");
        } catch (Exception e){
            result.put("state", "false");
            result.put("message", "添加异常");
            LOG.error("addUserRemark error, userid " + userid + "remark" + remark, e);
        }
        return result;
    }

    @RequestMapping(value = "/upPhone")
    @ResponseBody
    public String upPhone(HttpServletRequest request, String oldPhone, String newPhone, int userid) throws ParseException {
        int res = 0;
        try{

            DataSourceSelector.set("dataSource127hop");
            String admJson = Redis.hget(request.getSession().getId(), "admuser");
            if (admJson == null) {
                return "3";
            }
            Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
            int admuserid = user.getId();
            String exitEmail = userInfoService.exitPhone(oldPhone, userid);
            if (exitEmail == null) {
                SendMQ.sendMsg(new RunSqlModel("insert into user_ex (userid,otherphone) values('"+userid+"','"+newPhone+"')"));
            } else {
                SendMQ.sendMsg(new RunSqlModel("update user_ex set otherphone='"+newPhone+"' where userid='"+userid+"'"));
            }
            res=1;

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
        result = JSONArray.toJSONString(additionalList);
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
        result = JSONArray.toJSONString(orderPayList);
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
        result = JSONArray.toJSONString(payDetailList);
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


    @RequestMapping(value = "/getUserAllInfoById", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getUserAllInfoById(HttpServletRequest request, Integer userId) {
        JsonResult json = new JsonResult();
        if(userId == null || userId < 0){
            json.setOk(false);
            json.setMessage("获取用户ID失败");
            return json;
        }
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        try {
            UserInfo useInfo = userInfoService.queryAllInfoById(userId);
            useInfo.setAdmuser(admuser.getEmail());

            List<UserRecommendEmail>  list =  userInfoService.queryRecommendEmailInfo(userId);

            json.setOk(true);
            json.setData(useInfo);
            json.setAllData(list);
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("获取失败");
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping(value = "/sendRecommendEmail", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult sendRecommendEmail(HttpServletRequest request, Integer userId, String userEmail,String createTime
    ,String buniessInfo,String goodsNeed,String sendUrl,String sellEmail,String goodsRequire ,Integer webSite) {
        JsonResult json = new JsonResult();

        if (userId == null || userId < 0) {
            json.setOk(false);
            json.setMessage("获取客户ID失败");
            return json;
        }
        if (webSite == null || webSite < 0) {
            json.setOk(false);
            json.setMessage("获取网站类别失败");
            return json;
        }
        if (StringUtils.isBlank(userEmail)) {
            json.setOk(false);
            json.setMessage("获取客户邮箱失败");
            return json;
        }
        if (StringUtils.isBlank(createTime)) {
            json.setOk(false);
            json.setMessage("获取客户创建时间失败");
            return json;
        }
        if (StringUtils.isBlank(buniessInfo)) {
            buniessInfo = "";
        }
        if (StringUtils.isBlank(goodsNeed)) {
            goodsNeed = "";
        }
        if (StringUtils.isBlank(goodsRequire)) {
            goodsRequire = "";
        }
        if (StringUtils.isBlank(sendUrl)) {
            json.setOk(false);
            json.setMessage("获取目录地址失败");
            return json;
        }
        if (StringUtils.isBlank(sellEmail)) {
            json.setOk(false);
            json.setMessage("获取销售邮箱失败");
            return json;
        }
        int catalogIdInt = 0;

        // https://www.kidsproductwholesale.com/apa/recatalog.html?id=2
        String urlStr = sendUrl.substring(sendUrl.indexOf("?") + 1);
        String[] andList = urlStr.split("&");
        if(andList.length > 0){
            int count = 0;
            String[] tempStrs ;
            for(String andCl : andList){
                if(!andCl.contains("=")){
                    continue;
                }
                tempStrs = andCl.split("=");
                if(tempStrs.length == 2){
                    if("id".equals(tempStrs[0])){
                        catalogIdInt = Integer.parseInt(tempStrs[1]);
                    }
                    break;
                }
            }
            if(catalogIdInt == 0){
                json.setOk(false);
                json.setMessage("获取链接数据异常");
                return json;
            }
        } else {
            json.setOk(false);
            json.setMessage("推送链接数据异常");
            return json;
        }

        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        try {
            Map<String,Object> sendMap = new HashMap<>();
            sendMap.put("userId",String.valueOf(userId));
            sendMap.put("createTime",createTime);
            sendMap.put("buniessInfo",buniessInfo);
            sendMap.put("goodsNeed",goodsNeed);
            sendMap.put("goodsRequire",goodsRequire);
            sendMap.put("sendUrl",sendUrl);
            if("0".equals(webSite) || "1".equals(webSite)){
                sendMap.put("webSite","1");
            }else if("2".equals(webSite) || "3".equals(webSite)){
                sendMap.put("webSite","2");
            }else if("4".equals(webSite)){
                sendMap.put("webSite","3");
            }else{
                sendMap.put("webSite","1");
            }

            String title = "Our Recommendation";
            sendMap.put("title",title);

            TemplateType emailHtml = TemplateType.OUR_RECOMMENDATION;
            sendMailFactory.sendMail(userEmail, sellEmail, title, sendMap, emailHtml);


            UserRecommendEmail userRecommendEmail = new UserRecommendEmail();
            userRecommendEmail.setAdminId(admuser.getId());
            userRecommendEmail.setCreateTime(createTime);
            userRecommendEmail.setSendUrl(sendUrl);
            userRecommendEmail.setEmailContent(sendMap.toString());
            userRecommendEmail.setUserId(userId);
            userRecommendEmail.setCatalogId(catalogIdInt);

            userInfoService.insertIntoUserRecommendEmail(userRecommendEmail);

            json.setOk(true);
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("获取失败");
            e.printStackTrace();
        }
        return json;
    }


    @RequestMapping(value = "/queryMemAuthList", method = RequestMethod.POST)
    @ResponseBody
    public EasyUiJsonResult queryBusinessMembershipAuthorization(HttpServletRequest request,
                                            Integer page , Integer rows, Integer userId, String email,
                                            Integer countryId, Integer authFlag, Integer site) {
        EasyUiJsonResult json = new EasyUiJsonResult();

        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if(admuser == null || admuser.getId() == 0){
            json.setSuccess(false);
            json.setMessage("请登录后操作");
            return json;
        }

        UserInfo userInfo = new UserInfo();
        if(page == null || page == 0){
            page = 1;
        }
        if(rows != null && rows > 0){
            userInfo.setLimitNum(rows);
            userInfo.setStartNum((page -1) * rows);
        }
        if(userId != null &&  userId > 0){
            userInfo.setUserid(userId);
        }

        if(StringUtils.isNotBlank(email)){
            userInfo.setEmail(email);
        }
        userInfo.setCountryId(countryId);
        userInfo.setAuthFlag(authFlag);

        if(site == null || site < -1){
            site = -1;
        }
        userInfo.setSite(-1);

        try {

            if(admuser.getRoletype() > 0){
                userInfo.setAdminId(admuser.getId());
            }

            List<UserInfo> list= userInfoService.queryBusinessMembershipAuthorization(userInfo);

            int count = userInfoService.queryBusinessMembershipAuthorizationCount(userInfo);
            json.setRows(list);
            json.setTotal(count);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMessage("获取失败");
            e.printStackTrace();
            LOG.error("queryMemAuthList error:",e);

        }
        return json;
    }

    @RequestMapping(value = "/getZoneList")
    @ResponseBody
    public List<ZoneBean> getZoneList() {
        List<ZoneBean> list = new ArrayList<>();
        try {
            IZoneServer os = new ZoneServer();
            list = os.getAllZone();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("getZoneList error:", e);
        }
        return list;
    }
}
