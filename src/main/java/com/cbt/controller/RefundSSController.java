package com.cbt.controller;

import com.cbt.bean.BalanceBean;
import com.cbt.bean.OrderBean;
import com.cbt.bean.Payment;
import com.cbt.messages.service.MessageNotificationService;
import com.cbt.parse.service.StrUtils;
import com.cbt.paypal.service.PayPalService;
import com.cbt.pojo.MessageNotification;
import com.cbt.processes.service.UserServer;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.refund.bean.RefundBean;
import com.cbt.service.AdditionalBalanceService;
import com.cbt.service.IPaymentService;
import com.cbt.service.RefundSSService;
import com.cbt.service.impl.AdditionalBalanceServiceImpl;
import com.cbt.util.DateFormatUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * refund.sql: status 状态 0-申请退款 1-同意退款 2-退款完结 -1-驳回退款 -2 -用户取消退款; type 来源 0-提现
 * 1-paypal 2-用户网上投诉 3-邮件投诉
 *
 * @author abc
 * @date 2016年11月7日
 */
@Controller
@RequestMapping("/refundss")
public class RefundSSController {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(RefundSSController.class);

    @Autowired
    private UserServer userServer;
    @Autowired
    private RefundSSService refundService;

    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private AdditionalBalanceService additionalBalanceService;
    @Autowired
    private MessageNotificationService messageNotificationService;

    private PaymentDaoImp paymentDao = new PaymentDao();

    @Autowired
    private PayPalService ppApiService;

    // 获取所有退款申请信息 和 后台管理员列表
    @RequestMapping(value = "/getAllRefundApply", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAllRefundApply(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("refunddeal");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        String roletype = user.getRoletype();
        int uid = user.getId();
        String admName = user.getAdmName();
        mv.addObject("admName", admName);
        mv.addObject("roletype", roletype);
        mv.addObject("uid", uid);
        if (roletype != null && !roletype.equals("3") && !roletype.equals("4") && !roletype.equals("0")) {
            return mv;
        }

        List<RefundBean> list = refundService.searchRefund(0, "", "", "", 0, 1, -1, 0, admName);

        mv.addObject("stupresent", 2);
        mv.addObject("typeresent", -1);

        // 分页
        int pagecount = 0, datacount = 0;
        if (list != null && list.size() != 0) {
            datacount = list.get(0).getCount();
            pagecount = datacount % 10 == 0 ? datacount / 10 : datacount / 10 + 1;
        }
        int countUnReaded = messageNotificationService.countUnReaded(8);
        mv.addObject("messagecount", countUnReaded);

        mv.addObject("list", list);
        mv.addObject("pagecount", pagecount);
        mv.addObject("pagenow", 1);
        mv.addObject("count", datacount);
        return mv;
    }

    /**
     * 无匹配用户的退款申请
     *
     * @param request
     * @return
     * @date 2017年3月25日
     * @author abc
     */
    @RequestMapping(value = "/rlist", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ModelAndView getRefundList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("refundlist");
//		String sessionId = request.getSession().getId();
//		String userJson = Redis.hget(sessionId, "admuser");
//		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
//		String roletype = user.getRoletype();
//		int uid = user.getId();
//		String admName = user.getAdmName();
//		mv.addObject("admName", admName);
//		mv.addObject("roletype", roletype);
//		mv.addObject("uid", uid);
//		if (roletype != null && !roletype.equals("1") && !roletype.equals("0")) {
//			return mv;
//		}
        String strPage = request.getParameter("page");
        strPage = !StrUtils.isMatch(strPage, "(\\d+)") ? "1" : strPage;
        int page = Integer.valueOf(strPage);

        List<RefundBean> list = refundService.getRefundList(page);
        // 分页
        int pagecount = 0, datacount = 0;
        if (list != null && list.size() != 0) {
            datacount = list.get(0).getCount();
            pagecount = datacount % 40 == 0 ? datacount / 40 : datacount / 40 + 1;
        }

        mv.addObject("refundList", list);
        mv.addObject("pagecount", pagecount);
        mv.addObject("pagenow", pagecount == 0 ? 0 : page);
        mv.addObject("count", datacount);
        return mv;
    }


    /**
     * 更新退款信息
     *
     * @param request
     * @return
     * @date 2017年3月25日
     * @author abc
     */
    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public int updateRefund(HttpServletRequest request, HttpServletResponse response) {
        String strId = request.getParameter("id");
        strId = !StrUtils.isMatch(strId, "(\\d+)") ? "0" : strId;

        String strUserId = request.getParameter("userid");
        strUserId = strUserId != null ? strUserId.trim() : strUserId;
        strUserId = !StrUtils.isMatch(strUserId, "(\\d+)") ? "0" : strUserId;
        if ("0".equals(strId)) {
            return 0;
        }
        String orderid = request.getParameter("orderid");
        orderid = orderid != null ? orderid.trim() : orderid;

        String paypalName = request.getParameter("paypalname");
        paypalName = paypalName != null ? paypalName.trim() : paypalName;

        int userid = Integer.valueOf(strUserId);
        int id = Integer.valueOf(strId);
        return refundService.updateRefund(id, userid, orderid, paypalName);
    }

    /**
     * 更新退款信息
     *
     * @param request
     * @return
     * @date 2017年3月25日
     * @author abc
     */
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView queryPayment(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("paymentlist");
        String strAppAcount = request.getParameter("appcount");
        strAppAcount = !StrUtils.isMatch(strAppAcount, "(\\d+\\.*\\d*)") ? "0" : strAppAcount;
        if ("0".equals(strAppAcount)) {
            return mv;
        }
        double appCount = Double.valueOf(strAppAcount);

        String curreny = request.getParameter("curreny");
        String strPage = request.getParameter("page");
        strPage = !StrUtils.isMatch(strPage, "(\\d+)") ? "1" : strPage;
        int page = Integer.valueOf(strPage);
        List<PaymentBean> list = refundService.getPayment(appCount, curreny, page);
        // 分页
        int pagecount = 0, datacount = 0;
        if (list != null && list.size() != 0) {
            datacount = list.get(0).getCount();
            pagecount = datacount % 40 == 0 ? datacount / 40 : datacount / 40 + 1;
        }
        mv.addObject("paymentList", list);
        mv.addObject("pagecount", pagecount);
        mv.addObject("pagenow", pagecount == 0 ? 0 : page);
        mv.addObject("count", datacount);
        return mv;
    }

    /**
     * 获取所有退款申请信息 和 后台管理员列表
     *
     * @param userid    用户id
     * @param username  用户名
     * @param appdate   申请时间
     * @param agreeTime 处理时间
     * @param status    状态(-3-所有退款 0-新申请退款 -2-用户取消 1-银行处理中 2-完结 -1-拒绝)
     * @param deal      处理人
     * @param startrow  页码
     * @return
     * @date 2016年10月11日
     * @author abc
     */
    @RequestMapping(value = "/searchByParam", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView searchByParam(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("refunddeal");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            return mv;
        }
        String roletype = user.getRoletype();        int uid = user.getId();
        String admName = user.getAdmName();
        mv.addObject("admName", admName);
        mv.addObject("roletype", roletype);
        mv.addObject("uid", uid);
        if (roletype != null && !roletype.equals("3") && !roletype.equals("4") && !roletype.equals("0")) {
            return mv;
        }
        String strUserid = request.getParameter("userid");
        int userid = strUserid == null || strUserid.isEmpty() ? 0 : Integer.valueOf(strUserid);
        String username = request.getParameter("username");
        String appdate = request.getParameter("appdate");
        String agreeTime = request.getParameter("agreeTime");
        String strStatus = request.getParameter("status");
        int status = strStatus == null || strStatus.isEmpty() ? 0 : Integer.valueOf(strStatus);
        String strStartrow = request.getParameter("startrow");
        int startrow = strStartrow == null || strStartrow.isEmpty() ? 0 : Integer.valueOf(strStartrow);
        String strType = request.getParameter("type");
        int type = strType == null || strType.isEmpty() ? 0 : Integer.valueOf(strType);

        String rid = request.getParameter("rid");
        String mid = request.getParameter("mid");

        if (rid == null || rid.isEmpty() || !StrUtils.isMatch(rid, "(\\d+)")) {
            rid = "0";
        } else if (rid != null && StrUtils.isMatch(rid, "(\\d+)")) {
            if (!rid.equals("0") && mid != null && StrUtils.isMatch(mid, "(\\d+)")) {
                messageNotificationService.updateIsReadById(Integer.valueOf(mid));
            }
        }
        List<RefundBean> list = refundService.searchRefund(userid, username, appdate, agreeTime, status, startrow, type,
                Integer.valueOf(rid), admName);
        // 投诉申诉退款余额补偿
        String cidList = "";
        for (RefundBean l : list) {
            int complainId = l.getComplainId();
            cidList = complainId > 0 ? cidList + complainId + "," : cidList;
            //根据当前申请退款账号获得可退款订单号
            List<Payment> refundOrderNo = refundService.getRefundOrderNo(l.getPaypalname());
            //l.setOrderNoList(refundOrderNo);
        }
        cidList = cidList.endsWith(",") ? cidList.substring(0, cidList.length() - 1) : cidList;
        if (!cidList.isEmpty()) {
            Map<String, Double> moneyAmountByCids = additionalBalanceService.getMoneyAmountByCids(cidList);
            for (RefundBean l : list) {
                int complainId = l.getComplainId();
                if (complainId > 0) {
                    Double money = moneyAmountByCids.get(l.getComplainId() + "");
                    l.setAdditionBanlance(money == null ? 0 : money);
                }
            }
        }
        List<AdminUserBean> AdmuserList = refundService.getAllAdmUser();
        if (list != null && list.size() > 0) {
            mv.addObject("status", "1");
            mv.addObject("admuserList", AdmuserList);
        } else {
            mv.addObject("status", "-1");
            mv.addObject("message", "未查询到数据");
        }
        if (userid == 0) {
            mv.addObject("userid", null);
        } else {
            mv.addObject("userid", userid);
        }
        // 针对 Emma 新申请退款和 Ling的不同需求 在查询完毕后处理 状态
        // if(status==6){
        // status=2;
        // }
        // //针对 Emma 银行处理中和 Ling的不同需求 在查询完毕后处理 状态
        // if(status==7){
        // status=4;
        // }

        // 分页
        int pagecount = 0, datacount = 0;
        if (list != null && list.size() != 0) {
            datacount = list.get(0).getCount();
            pagecount = datacount % 10 == 0 ? datacount / 10 : datacount / 10 + 1;
        }

        mv.addObject("list", list);
        mv.addObject("username", username);
        mv.addObject("appdate", appdate);
        mv.addObject("agreeTime", agreeTime);
        mv.addObject("stupresent", status);
        mv.addObject("typeresent", type);
        mv.addObject("pagecount", pagecount);
        mv.addObject("pagenow", startrow);
        mv.addObject("count", datacount);
        int countUnReaded = messageNotificationService.countUnReaded(8);
        mv.addObject("messagecount", countUnReaded);
        return mv;
    }

    /**
     * 根据状态获取所有退款申请信息
     *
     * @param status
     * @return
     * @date 2016年10月11日
     * @author abc
     */
    @RequestMapping(value = "/searchByStatus", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView searchByStatus(Integer status) {
        ModelAndView mv = new ModelAndView("refunddeal");
        if (status != null) {
            List<RefundBean> list = refundService.searchRefundByState(status);
            if (list != null && list.size() > 0) {
                mv.addObject("status", "1");
                mv.addObject("refundList", list);
                mv.addObject("stupresent", status);
            } else {
                mv.addObject("status", "-1");
                mv.addObject("message", "未查询到数据");
                mv.addObject("stupresent", status);
            }
        } else {
            return mv.addObject("message", "请求参数出错，请检查。");
        }
        return mv;
    }

    /**
     * 执行退款或者拒绝退款
     *
     * @param uid
     * @param appcount
     * @param rid
     * @param admuser
     * @param agreeOrNot  1-同意 2-拒绝
     * @param appcurrency
     * @param refuse
     * @return
     * @date 2016年10月11日
     * @author abc
     */
    @RequestMapping(value = "/dealRefund", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult dealRefund(int uid, double appcount, int rid, String admuser, int agreeOrNot, String appcurrency,
                                 String refuse, int type) {
        String res = "";
        JsonResult jr = new JsonResult();
        int row = 0;
        if (uid != 0 && agreeOrNot != 0) {

            row = refundService.agreeRefund(uid, appcount, rid, admuser, agreeOrNot, appcurrency, refuse.trim(), type);
            res = row > 0 ? "操作成功" : "操作失败";
        } else {
            res = "数据传输有误，请联系管理员。";
        }
        jr.setMessage(res);
        return jr;
    }

    /**
     * 完结退款
     *
     * @param refundId
     * @param userEmail
     * @param userName
     * @param appcount
     * @param currency
     * @param additionid
     * @return
     * @date 2016年10月11日
     * @author abc
     */
    @RequestMapping(value = "/finishRefund", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult finishRefund(int refundId, String userEmail, String userName, double appcount, String currency,
                                   int additionid) {
        String res = "";
        JsonResult jr = new JsonResult();
        int row = 0;
        if (refundId != 0) {
            row = refundService.finishRefund(refundId);
            if (row > 0) {
                // 针对邮件投诉、网页投诉 退款完结后更改余额补偿表状态
                if (additionid > 0) {
                    additionalBalanceService.updateStateById(additionid, 1);
                }
                res = "完结退款成功，并以邮件形式通知了客户";
                StringBuffer sb = new StringBuffer("Your refund application has been approved and you "
                        + "are able to trace your refund within the next 48hours.");
                sb.append("<br/>");
                sb.append("Refund amount:" + currency + "&nbsp;" + appcount);
                // String contents = SendEmail.SetContent(userName,
                // sb).toString();
                // SendEmail.send(SendEmail.FROM, SendEmail.PWD, userEmail,
                // contents, "Response for your withdraw apply", "An withdraw
                // request", 1);
            } else {
                res = "完结退款成功失败";
            }
        } else {
            res = "数据传输有误，请联系管理员。";
        }
        jr.setMessage(res);
        return jr;
    }

    @RequestMapping(value = "/report", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelAndView reportGet(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("reportForm");

        String sdate = request.getParameter("sdate");
        String edate = request.getParameter("edate");
        String page = request.getParameter("page");
        String reason = request.getParameter("reason");

        if (page == null || page.isEmpty() || !StrUtils.isMatch(page, "(\\d+)")) {
            page = "1";
        }
        List<RefundBean> list = refundService.reportGet(sdate, edate, reason, Integer.valueOf(page));
        int count = list != null && !list.isEmpty() ? list.get(0).getCount() : 0;

        mv.addObject("list", list);
        mv.addObject("pagenow", page);
        mv.addObject("pagecount", count);

        return mv;
    }

    /**
     * emma对存在疑惑的退款添加反馈
     *
     * @param rid
     * @param feedback
     * @return
     * @date 2016年9月6日
     * @author abc
     */
    @RequestMapping(value = "/addFeedback", method = RequestMethod.POST)
    @ResponseBody
    public int addFeedback(int rid, String feedback, String orderno) {
        int res = 0;
        if (rid != 0 && feedback != "") {
            res = refundService.addFeedback(rid, feedback);
        }

        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setRefundId(rid);
        messageNotification.setSendContent(feedback);
        messageNotification.setIsRead("N");
        messageNotification.setSendType(8);
        messageNotification.setSenderId(1);
        messageNotification.setOrderNo(orderno);
        Timestamp time = new Timestamp(new Date().getTime());
        messageNotification.setCreateTime(time);
        // 添加消息
        messageNotificationService.insertMessageNotification(messageNotification);
        return res;
    }

    /**
     * Ling添加退款备注，方便emma校正 (添加备注即初步同意退款，状态更改为银行处理中)
     *
     * @param id          id
     * @param remark      备注
     * @param agreepeople 操作人
     * @param typeid      退款来源 0-提现 1-paypal申请 2-用户网上投诉 3-用户邮件投诉
     * @param appcount    申请退款金额
     * @param account     实际退款金额
     * @return
     * @date 2016年10月8日
     * @author abc
     */
    @RequestMapping(value = "/remark", method = RequestMethod.POST)
    @ResponseBody
    public String addRemark(int id, String remark, String agreepeople,
                            int typeid, String appcount, String account,
                            String orderid, String payid, String userid, String additionid,
                            String complainid, int resontype) {
        String res = "";
        try {
            if (StrUtils.isNullOrEmpty(account)) {
                return "-1";
            }
            account = account.trim();
            if (!StrUtils.isMatch(account, "(\\d+\\.*\\d*)")) {
                return "-1";
            }
            complainid = StrUtils.isNullOrEmpty(complainid) ? "0" : complainid;
            complainid = StrUtils.isMatch(complainid, "(\\d+)") ? complainid : "0";

            additionid = StrUtils.isNullOrEmpty(additionid) ? "0" : additionid;
            additionid = StrUtils.isMatch(additionid, "(\\d+)") ? "0" : additionid;

            double double_account = Double.valueOf(account);

            // 针对用户投诉和邮件投诉 ,金额要记录到余额奖励与补偿表中
            int additionId = 0;
            if (typeid > 1 && "0".equals(additionid)) {
                additionId = additionalBalanceService.insertForRefund(new BalanceBean(Integer.valueOf(userid),
                        double_account, remark, agreepeople, typeid, complainid, orderid, 0));
            }
            if (id != 0 && remark != "") {
                int status = 1;
                // 如果是余额提现的情况下，若实际退款金额为0的话就直接完结掉，不需要emma处理
                if (typeid == 0 && double_account < 0.01) {
                    status = 2;
                }
                // 其他销售备注不更改状态,ling备注更改状态
                if (agreepeople != null && !agreepeople.toLowerCase().equals("ling") && !agreepeople.toLowerCase().equals("emmaxie")) {
                    status = 0;
                }
                int resInt = refundService.addRemark(double_account, id, remark, agreepeople, additionId, status, resontype);
                res = resInt + "";
                // paypal申诉 自动取消订单，余额自动校正
                if (typeid == 1) {
                    // res = closeOrder(orderid, Integer.valueOf(userid),
                    // Double.valueOf(account));
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            res = e.toString();
            e.printStackTrace();
        }
        return res;
    }


    @RequestMapping(value = "/doRemark")
    @ResponseBody
    public String doRemark(HttpServletRequest request, HttpServletResponse response) {
        String idStr = request.getParameter("id");
        int id = 0;
        if (idStr != null && !"".equals(idStr)) {
            id = Integer.valueOf(idStr);
        }
        String remark = request.getParameter("remark");

        String agreepeople = request.getParameter("agreepeople");

        //格式化备注样式和信息
        remark = DateFormatUtil.getWithMinutes(new Date()) + "," + agreepeople + ":" + remark + ";";

        String typeidStr = request.getParameter("typeid");
        int typeid = 0;
        if (typeidStr != null && !"".equals(typeidStr)) {
            typeid = Integer.valueOf(typeidStr);
        }
        String appcount = request.getParameter("appcount");
        String account = request.getParameter("account");
        String orderid = request.getParameter("orderid");
        String payid = request.getParameter("payid");
        String userid = request.getParameter("userid");
        String additionid = request.getParameter("additionid");
        String complainid = request.getParameter("complainid");
        String resontypeStr = request.getParameter("resontype");
        int resontype = 0;
        if (resontypeStr != null && !"".equals(resontypeStr)) {
            resontype = Integer.valueOf(resontypeStr);
        }


        String res = "";
        try {
            if (StrUtils.isNullOrEmpty(account)) {
                return "-1";
            }
            account = account.trim();
            if (!StrUtils.isMatch(account, "(\\d+\\.*\\d*)")) {
                return "-1";
            }
            complainid = StrUtils.isNullOrEmpty(complainid) ? "0" : complainid;
            complainid = StrUtils.isMatch(complainid, "(\\d+)") ? complainid : "0";

            additionid = StrUtils.isNullOrEmpty(additionid) ? "0" : additionid;
            additionid = StrUtils.isMatch(additionid, "(\\d+)") ? "0" : additionid;

            double double_account = Double.valueOf(account);

            // 针对用户投诉和邮件投诉 ,金额要记录到余额奖励与补偿表中
            int additionId = 0;
            if (typeid > 1 && "0".equals(additionid)) {
                additionId = additionalBalanceService.insertForRefund(new BalanceBean(Integer.valueOf(userid),
                        double_account, remark, agreepeople, typeid, complainid, orderid, 0));
            }
            if (id != 0 && remark != "") {
                int status = 1;
                // 如果是余额提现的情况下，若实际退款金额为0的话就直接完结掉，不需要emma处理
                if (typeid == 0 && double_account < 0.01) {
                    status = 2;
                }

                // 如果是PayPal的,double_account超过500,ling只能确定退款,不能直接操作,需要emma处理
                if (typeid == 1 && double_account > 500) {
                    status = 3;
                }

                // 其他销售备注不更改状态,ling备注更改状态
                if (agreepeople != null && !agreepeople.toLowerCase().equals("ling")  && !agreepeople.toLowerCase().equals("emmaxie")) {
                    status = 0;
                }
                int resInt = refundService.addRemark(double_account, id, remark, agreepeople, additionId, status,
                        resontype);
                res = resInt + "";
                // paypal申诉 自动取消订单，余额自动校正
                if (typeid == 1) {
                    // res = closeOrder(orderid, Integer.valueOf(userid),
                    // Double.valueOf(account));
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            res = e.toString();
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 重构方法,退款中判断状态和备注
     */
    @RequestMapping(value = "/confirmAndRemark", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult confirmAndRemark(HttpServletRequest request, HttpServletResponse response) {

        JsonResult result = new JsonResult();
        try {
            //获取登录用户信息
            String sessionId = request.getSession().getId();
            String userJson = Redis.hget(sessionId, "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null) {
                result.setOk(false);
                result.setMessage("登录过期,请重新登录");
                return result;
            }
            //获取申诉id
            String id = request.getParameter("id");
            if (id == null || "".equals(id)) {
                result.setOk(false);
                result.setMessage("获取id失败,请重试");
                return result;
            }
            //获取客户id
            String userid = request.getParameter("userid");
            if (userid == null || "".equals(userid)) {
                result.setOk(false);
                result.setMessage("获取userid失败,请重试");
                return result;
            }
            //获取备注信息
            String remark = request.getParameter("remark");
            if (remark == null || "".equals(remark)) {
                result.setOk(false);
                result.setMessage("获取备注失败,请重试");
                return result;
            }
            //格式化备注样式和信息
            remark = "[" + DateFormatUtil.getWithMinutes(new Date()) + "]" + user.getAdmName() + ":" + remark + ";";
            //获取退款金额
            String account = request.getParameter("account");
            if (account == null || "".equals(account)) {
                result.setOk(false);
                result.setMessage("获取退款金额失败,请重试");
                return result;
            }
            //获取处理状态
            String status = request.getParameter("status");
            if (status == null || "".equals(status)) {
                result.setOk(false);
                result.setMessage("获取处理状态失败,请重试");
                return result;
            }
            String refundOrderNo = request.getParameter("refundOrderNo");
            if ("1".equals(status) && StringUtils.isBlank(refundOrderNo)) {
                result.setOk(false);
                result.setMessage("获取订单号失败,请重试");
                return result;
            } else if (StringUtils.isBlank(refundOrderNo)) {
                refundOrderNo = "";
            }
            RefundBean rfBean = paymentDao.queryRefundBeanById(Integer.valueOf(id), Integer.valueOf(userid));
            //判断当前退款状态，禁止重复操作
            if (rfBean != null) {
                if (rfBean.getStatus() == Integer.valueOf(status) && (rfBean.getStatus() == 1 || rfBean.getStatus() == 2)) {
                    result.setOk(false);
                    result.setMessage("此状态已经更新，终止操作");
                    return result;
                } else {
                    refundService.confirmAndRemark(Integer.valueOf(id), Integer.valueOf(userid), refundOrderNo, remark, Double.valueOf(account), Integer.valueOf(status), user.getAdmName());
                    //如果拒绝客户申请的余额提现，则退给客户余额
                    if ((Integer.valueOf(status) == -1 || Integer.valueOf(status) == -3) && rfBean.getType() == 0) {
                        UserDao userDao = new UserDaoImpl();
                        float appCount = (float) rfBean.getAppcount();
                        userDao.updateUserAvailable(Integer.valueOf(userid), appCount,
                                "拒绝提现请求，回款,refund_id:" + id + ",amount:" + rfBean.getAppcount(), "refund_id:" + id, user.getAdmName(), 0, 0, 4);
                    }
                    result.setOk(true);
                    result.setMessage("执行成功");
                }
            } else {
                result.setOk(false);
                result.setMessage("获取退款信息失败，请确认信息是否存在");
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("confirmAndRemark error,reason: " + e.getMessage());
            result.setOk(false);
            result.setMessage("保存失败,请重试");
        }
        return result;
    }

    /**
     * 手动添加一笔退款 仅限于用户邮件投诉，以及用户网上投诉
     *
     * @param ruserid   用户id
     * @param rappcount 申请金额
     * @param rpaypal   paypal账号
     * @param rorderid  订单id
     * @param rpayid    交易号
     * @param rtype     类型 1- paypal申请 3-邮件投诉
     * @date 2016年10月8日
     * @author abc
     */
    @RequestMapping(value = "/addRefund", method = RequestMethod.POST)
    @ResponseBody
    public int addRefund(String ruserid, String rappcount, String rpaypal, String rorderid, String rpayid,
                         String rtype) {

        int uid = StrUtils.isNull(ruserid) || !StrUtils.isMatch(ruserid, "(\\d+)") ? 0 : Integer.valueOf(ruserid);
        Double appcount = StrUtils.isNull(rappcount) || !StrUtils.isMatch(rappcount, "(\\d+\\.*\\d*)") ? 0
                : Double.valueOf(rappcount);
        ;
        int type = StrUtils.isNull(rtype) || !StrUtils.isMatch(rtype, "(\\d+)") ? 3 : Integer.valueOf(rtype);
        // Map<String, Object> map = new HashMap<String, Object>();
        if (uid == 0 || Math.abs(appcount) < 0.01) {
            // map.put("status", false);
            return 0;
        }
        int addRefundFromAppeal = refundService.addRefundFromAppeal(uid, appcount, rpaypal, rpayid, rorderid, type);
        // map.put("status", addRefundFromAppeal==0?false:true);
        return addRefundFromAppeal;

    }

    /**
     * 手动添加一笔退款 仅限于用户邮件投诉，以及用户网上投诉
     *
     * @date 2016年10月8日
     * @author abc
     * 用户id
     * 申请金额
     * paypal账号
     * 订单id
     * 交易号
     * 类型 1- paypal申请 3-邮件投诉
     */
    @RequestMapping(value = "/getavailable", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getAvailableOld(String userid) {
        Map<String, Object> map = new HashMap<String, Object>();
        int uid = StrUtils.isNull(userid) || !StrUtils.isMatch(userid, "(\\d+)") ? 0 : Integer.valueOf(userid);
        double[] balance = userServer.getBalance(uid);
        map.put("available", balance[0]);
        map.put("currency", balance[1]);
        return map;
    }

    /**
     * 方法描述:系统后台的确认价格中的取消订单按钮 author:sj date:2015年8月1日
     *
     * @param orderNo
     * @param userid
     * @param account
     */
    public String closeOrder(String orderNo, int userid, double account) {
        if (orderNo == null || orderNo.isEmpty() || userid < 1 || account < 0.01) {
            return "";
        }
        IOrderwsServer orderwsServer = new OrderwsServer();

        OrderBean stateByOrderNo = orderwsServer.getStateByOrderNo(userid, orderNo);
        // 订单已经在采购中、到库、出运中的话就不能取消订单，需要人为操作
        if (stateByOrderNo == null) {
            return "订单取消失败:该订单可能已经 采购/到库/出运/取消,请手动操作该订单";
        }
        // 订单
        String orderNo2 = stateByOrderNo.getOrderNo();
        double pay_price = stateByOrderNo.getPay_price();

        if (orderNo2 != null && orderNo2.equals(orderNo) && Math.abs(pay_price - account) < 0.01) {
            refundService.closeOrderForPaypal(userid, account, orderNo);
            return "成功";
        } else {
            return "订单取消失败:申诉金额与订单实际金额不符,无法取消该订单,请手动操作该订单";
        }

        // 获取用户货币单位
        // IUserDao userDao = new com.cbt.processes.dao.UserDao();
        // String currency_u = userDao.getUserCurrency(userid);
        // double exchange_rate = 1;
        // if(!currency_u.equals(currency)){
        // //获取汇率单位
        // ISpiderDao spiderDao = new SpiderDao();
        // Map<String, Double> exchangeRate =
        // spiderDao.getExchangeRate();//汇率值获取
        // exchange_rate =
        // exchangeRate.get(currency_u)/exchangeRate.get(currency);//汇率
        // }
        // //zlw add start
        // float actualPay =
        // Float.parseFloat(request.getParameter("actualPay"));
        // float order_ac = Float.parseFloat(request.getParameter("order_ac"));
        // UserDao dao= new UserDaoImpl();
        // int res1 = 0;
        // order_ac = new BigDecimal(order_ac*exchange_rate).setScale(2,
        // BigDecimal.ROUND_HALF_UP).floatValue();
        // res1=dao.updateUserAvailable(userId, new
        // BigDecimal(actualPay*exchange_rate).setScale(2,
        // BigDecimal.ROUND_HALF_UP).floatValue(), " system
        // closeOrder:"+orderNo, null,0,order_ac,1);
        // //zlw add end

        // //ssd add start
        // // 发送取消订单的提醒邮件
        // StringBuffer sbBuffer = new StringBuffer("<div style='font-size:
        // 14px;'>");
        // sbBuffer.append("<a href='" + AppConfig.ip_email
        // + "'><img style='cursor: pointer' src='" + AppConfig.ip_email
        // + "/img/logo.png' ></img></a>");
        // sbBuffer.append("<div style='font-size: 14px;'><div
        // style='font-weight: bolder;'>Dear "+toEmail+"</div>");
        // sbBuffer.append("<br><br>Order#: "+orderNo);
        // sbBuffer.append("<br><br>Your purchase order is closed due to supply
        // reason, refund has been returned to your ");
        // sbBuffer.append("<a href='" + AppConfig.ip_email
        // + "'>Import-express account</a>.");
        // sbBuffer.append("<br>You may use the balance to continue shopping or
        // withdrawl to PayPal.");
        // sbBuffer.append("<br><br>Regards. ");
        // sbBuffer.append("<br>Import-Express Team");
        //
        // SendEmail.send(confirmEmail, null, toEmail, sbBuffer.toString(),
        // "Your ImportExpress Order "+ orderNo +" transaction is closed!","",
        // orderNo, 2);
        // //ssd and end
    }

    /**
     * 读取消息列表
     *
     * @return
     * @date 2017年1月13日
     * @author abc
     */
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getMessageList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("refundmsg");
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        String roletype = user.getRoletype();
        int uid = user.getId();
        String admName = user.getAdmName();
        mv.addObject("admName", admName);
        mv.addObject("roletype", roletype);
        mv.addObject("uid", uid);

        String isread = request.getParameter("isread");
        if (isread == null || isread.isEmpty()) {
            isread = "N";
        }
        String orderno = request.getParameter("orderno");
        if (orderno != null && orderno.isEmpty()) {
            orderno = null;
        }
        List<MessageNotification> msgList = messageNotificationService.getMsgList(8, isread, orderno);
        if (msgList.size() == 1) {
            MessageNotification msg = msgList.get(0);
            mv.addObject("refundid", msg.getRefundId());
            mv.addObject("msgid", msg.getId());
            mv.addObject("msgflag", 1);
        }
        mv.addObject("msgList", msgList);
        return mv;

    }


    /**
     * 退款统计
     */
    @RequestMapping(value = "/refundStatistical", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult refundStatistical(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        List<RefundBean> refundlst = new ArrayList<RefundBean>();
        String typeStr = request.getParameter("type");
        String beginDateStr = request.getParameter("beginDate");
        String endDateStr = request.getParameter("endDate");
        String pageStr = request.getParameter("page");
        int page = 1;
        if (pageStr != null && !"".equals(pageStr)) {
            page = Integer.valueOf(pageStr);
        }
        if (page <= 0) {
            page = 1;
        }
        try {

            Long total = 0L;
            json.setOk(true);
            json.setTotal(total);
            json.setData(refundlst);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("The query fails, the reason is :" + e.getMessage());
            json.setOk(false);
            json.setMessage("The query fails, reason is :" + e.getMessage());
            return json;
        }
    }


    /**
     * 获取可退款的订单号
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/findCanRefundOrderNo", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult findCanRefundOrderNo(HttpServletRequest request, HttpServletResponse response) {

        //计算规则：需要退款的金额 <=
        //该订单有效的到账金额=该订单的支付金额-已经退款的金额总和(包括已经在申请中的金额)
        JsonResult json = new JsonResult();
        String userIdStr = request.getParameter("userId");
        String refundAmountStr = request.getParameter("refundAmount");

        int userId = 0;
        if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
            json.setOk(false);
            json.setMessage("获取用户ID失败");
        } else {
            userId = Integer.valueOf(userIdStr);
        }

        double refundAmount = 0;
        if (StringUtils.isBlank(refundAmountStr) || "0".equals(refundAmountStr)) {
            json.setOk(false);
            json.setMessage("获取退款金额失败");
        } else {
            refundAmount = Double.valueOf(refundAmountStr);
        }

        try {

            Map<String,String> orderNosMap = new HashMap<String,String>();
            //获取PayPal支付订单金额
            List<PaymentBean> paymentList = refundService.queryPayMentInfoByUserId(userId,3);
            if (!(paymentList == null || paymentList.isEmpty())) {
                //获取已经在退款流程中的订单金额
                List<RefundBean> refundList = paymentDao.quertRefundByUserId(userId);
                //循环获取可用订单号
                for (PaymentBean pmb : paymentList) {
                    if (StringUtils.isNotBlank(pmb.getOrderid())) {
                        int tempRefundAmout = 0;
                        if (!(refundList == null || refundList.isEmpty())) {
                            for (RefundBean rfb : refundList) {
                                if (pmb.getOrderid().equals(rfb.getOrderid())) {
                                    tempRefundAmout += rfb.getAccount();
                                }
                            }
                        }
                        if (Double.valueOf(pmb.getPayment_amount()) - refundAmount - tempRefundAmout >= 0) {
                            if(!orderNosMap.containsKey(pmb.getOrderid())){
                                orderNosMap.put(pmb.getOrderid(),pmb.getOrderid() + "(remain:" + (Double.valueOf(pmb.getPayment_amount()) - tempRefundAmout) + ")");
                            }
                        }
                    }
                }
            }
            json.setOk(true);
            json.setData(orderNosMap);
            json.setTotal((long) orderNosMap.size());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("userId:" + userIdStr + ",findCanRefundOrderNo error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("userId:" + userIdStr + ",findCanRefundOrderNo error :" + e.getMessage());
        }
        return json;
    }


    /**
     * 根据PayPal的API进行退款
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/refundByPayPalApi", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult refundByPayPalApi(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        //获取登录用户信息
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("登录过期,请重新登录");
            return json;
        }

        String userIdStr = request.getParameter("userId");
        String rdIdStr = request.getParameter("rdId");

        int userId = 0;
        if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
            json.setOk(false);
            json.setMessage("获取用户ID失败");
        } else {
            userId = Integer.valueOf(userIdStr);
        }
        int rdId = 0;
        if (StringUtils.isBlank(rdIdStr) || "0".equals(rdIdStr)) {
            json.setOk(false);
            json.setMessage("获取退款ID失败");
        } else {
            rdId = Integer.valueOf(rdIdStr);
        }
        try {
            RefundBean rfBean = paymentDao.queryRefundBeanById(rdId, userId);
            if (rfBean != null) {
                DecimalFormat dcft = new DecimalFormat("0.00");
                json = ppApiService.reFundNew(rfBean.getOrderid(), dcft.format(rfBean.getAccount()));
                if (json.isOk()) {
                    //如果是PayPal申请退款，则自动添加一条余额补偿记录(非余额补偿)
                    if(rfBean.getType() == 1){
                        AdditionalBalanceService server = new AdditionalBalanceServiceImpl();
                        server.insert(new BalanceBean(userId, rfBean.getAccount(), "System Add:客户PayPal申请退款完结后,自动生成的数据", user.getAdmName(), "refundId_" + rfBean.getId(), rfBean.getOrderid()));
                    }
                    //更新完结状态
                    refundService.updateRefundState(userId, rdId, 2);
                } else {
                    LOG.error("userId:" + userIdStr + ",rdId:" + rdId + ",refundByPayPalApi error :" + json.getMessage());
                    System.err.println("userId:" + userIdStr + ",rdId:" + rdId + ",refundByPayPalApi error :" + json.getMessage());
                    json.setOk(false);
                    json.setMessage("退款失败,请联系IT人员查看");
                }
            } else {
                LOG.error("userId:" + userIdStr + ",rdId:" + rdId + ",refundByPayPalApi error :获取退款信息失败,终止退款");
                System.err.println("userId:" + userIdStr + ",rdId:" + rdId + ",refundByPayPalApi error :获取退款信息失败,终止退款");
                json.setOk(false);
                json.setMessage("获取退款信息失败,终止退款");

                //更新完结状态
                //refundService.updateRefundState(userId, rdId, -4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("userId:" + userIdStr + ",findCanRefundOrderNo error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("userId:" + userIdStr + ",findCanRefundOrderNo error :" + e.getMessage());
        }
        return json;
    }


}
