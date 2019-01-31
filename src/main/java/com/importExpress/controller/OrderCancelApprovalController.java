package com.importExpress.controller;

import com.cbt.paypal.service.PayPalService;
import com.cbt.systemcode.service.SecondaryValidationService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.PaymentDetails;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.OrderCancelApproval;
import com.importExpress.pojo.OrderCancelApprovalDetails;
import com.importExpress.service.OrderCancelApprovalService;
import com.importExpress.service.PaymentServiceNew;
import com.importExpress.utli.NotifyToCustomerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/orderCancelApprovalCtr")
public class OrderCancelApprovalController {
    private final static Logger logger = LoggerFactory.getLogger(OrderCancelApprovalController.class);

    @Autowired
    private OrderCancelApprovalService approvalService;
    @Autowired
    private SecondaryValidationService secondaryValidationService;
    @Autowired
    private PaymentServiceNew paymentServiceNew;
    @Autowired
    private PayPalService ppApiService;
    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("orderCancelApproval");

        EasyUiJsonResult json = new EasyUiJsonResult();

        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            mv.addObject("success", 0);
            mv.addObject("message", "请登录后操作");
            return mv;
        }

        int adminId = 0;
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        mv.addObject("operatorId", adm.getId());
        mv.addObject("roleType", adm.getRoletype());
        mv.addObject("operatorName", adm.getAdmName());
        if ("0".equals(adm.getRoletype())) {
            adminId = 0;
        } else {
            adminId = adm.getId();
        }

        String userIdStr = request.getParameter("userId");
        int userId = 0;
        if (StringUtils.isNotBlank(userIdStr)) {
            userId = Integer.valueOf(userIdStr);
        }
        if (userId > 0) {
            mv.addObject("userId", userId);
        } else {
            mv.addObject("userId", "");
        }

        String orderNo = request.getParameter("orderNo");
        if (StringUtils.isNotBlank(orderNo)) {
            mv.addObject("orderNo", orderNo);
        } else {
            mv.addObject("orderNo", "");
        }

        String typeStr = request.getParameter("type");
        int type = 0;
        if (StringUtils.isNotBlank(typeStr)) {
            type = Integer.valueOf(typeStr);
        }
        mv.addObject("type", type);


        int dealState = -1;
        String dealStateStr = request.getParameter("dealState");

        if (StringUtils.isNotBlank(dealStateStr)) {
            dealState = Integer.valueOf(dealStateStr);
        }
        mv.addObject("dealState", dealState);


        int startNum = 0;
        int limitNum = 30;
        int page = 1;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            page = Integer.valueOf(pageStr);
            startNum = (page - 1) * limitNum;
        }
        mv.addObject("page", page);
        try {

            OrderCancelApproval cancelApproval = new OrderCancelApproval();
            if (type > 0) {
                cancelApproval.setType(type);
            }
            if (dealState > -1) {
                cancelApproval.setDealState(dealState);
            }
            if (adminId > 0) {
                cancelApproval.setAdminId(adminId);
            }
            if(userId > 0){
                cancelApproval.setUserId(userId);
            }
            if(StringUtils.isNotBlank(orderNo)){
                cancelApproval.setOrderNo(orderNo);
            }
            cancelApproval.setStartNum(startNum);
            cancelApproval.setLimitNum(limitNum);

            List<OrderCancelApproval> list = approvalService.queryForList(cancelApproval);

            int total = approvalService.queryForListCount(cancelApproval);
            for (OrderCancelApproval approvalBean : list) {
                List<OrderCancelApprovalDetails> detailsList = approvalService.queryForDetailsList(approvalBean.getId());
                for (OrderCancelApprovalDetails approvalDetail : detailsList) {
                    //判断各个状态
                    if (approvalDetail.getDealState() == 1) {
                        //状态是1的为销售审批，放入销售审批详情
                        approvalBean.setApproval1(approvalDetail);
                    } else if (approvalDetail.getDealState() == 2) {
                        //状态是2的为主管审批，放入主管审批详情
                        approvalBean.setApproval2(approvalDetail);
                    } else if (approvalDetail.getDealState() == 3) {
                        //状态是3的为审批完成，已经退款
                        approvalBean.setApproval3(approvalDetail);
                    } else if (approvalDetail.getDealState() == 4) {
                        //状态是9的为驳回信息
                    }
                }
                detailsList.clear();
            }
            mv.addObject("success", 1);
            mv.addObject("list", list);
            mv.addObject("total", total);
            int totalPage = total / 10;
            if (total % 10 > 0) {
                totalPage++;
            }
            mv.addObject("totalPage", totalPage);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            mv.addObject("success", 0);
            mv.addObject("message", "查询失败，原因:" + e.getMessage());
            logger.error("查询失败，原因 :" + e.getMessage());
        }
        return mv;
    }


    @RequestMapping(value = "/setStateAndRemark", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult setStateAndRemark(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        try {
            //获取登录用户信息
            String sessionId = request.getSession().getId();
            String userJson = Redis.hget(sessionId, "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null) {
                json.setOk(false);
                json.setMessage("登录过期,请重新登录");
                return json;
            }
            //获取申诉id
            String approvalIdStr = request.getParameter("approvalId");
            int approvalId = 0;
            if (StringUtils.isBlank(approvalIdStr)) {
                json.setOk(false);
                json.setMessage("获取申请id失败,请重试");
                return json;
            } else {
                approvalId = Integer.valueOf(approvalIdStr);
            }

            String dealStateStr = request.getParameter("dealState");
            int dealState = 0;
            if (StringUtils.isBlank(dealStateStr)) {
                json.setOk(false);
                json.setMessage("获取处理状态失败,请重试");
                return json;
            } else {
                dealState = Integer.valueOf(dealStateStr);
            }

            String userIdStr = request.getParameter("userId");
            int userId = 0;
            if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
                json.setOk(false);
                json.setMessage("获取客户id失败,请重试");
                return json;
            } else {
                userId = Integer.valueOf(userIdStr);
            }

            String orderNo = request.getParameter("orderNo");
            if (StringUtils.isBlank(orderNo)) {
                json.setOk(false);
                json.setMessage("获取申请订单号失败,请重试");
                return json;
            } else if (orderNo.contains("_")) {
                json.setOk(false);
                json.setMessage("拆单或者补货订单号，不可退款");
                return json;
            }

            String operatorIdStr = request.getParameter("operatorId");
            int operatorId = 0;
            if (StringUtils.isBlank(operatorIdStr) || "0".equals(operatorIdStr)) {
                json.setOk(false);
                json.setMessage("获取操作人ID失败,请重试");
                return json;
            } else {
                operatorId = Integer.valueOf(operatorIdStr);
                if (operatorId != user.getId()) {
                    json.setOk(false);
                    json.setMessage("获取非当前操作人操作");
                    return json;
                }
            }

            String refundAmountStr = request.getParameter("refundAmount");
            double refundAmount = 0;
            if (dealState == 4) {
                refundAmount = 0;
            } else {
                if (StringUtils.isBlank(refundAmountStr) || "0".equals(refundAmountStr)) {
                    json.setOk(false);
                    json.setMessage("获取退款金额失败,请重试");
                    return json;
                } else {
                    refundAmount = Double.valueOf(refundAmountStr);
                }
            }


            String remark = request.getParameter("remark");
            if (StringUtils.isBlank(remark)) {
                json.setOk(false);
                json.setMessage("获取备注信息失败,请重试");
                return json;
            }

            String secvlidPwd = request.getParameter("secvlidPwd");
            if (dealState == 3 && StringUtils.isBlank(secvlidPwd)) {
                json.setOk(false);
                json.setMessage("获取密码失败,请重试");
                return json;
            }

            boolean isCheck;
            if (dealState == 3) {
                isCheck = secondaryValidationService.checkExistsPassword(operatorId, secvlidPwd);
            } else {
                //操作状态是1的说明是销售，不需要校检密码
                isCheck = true;
            }
            if (isCheck) {
                OrderCancelApproval approvalBean = new OrderCancelApproval();
                approvalBean.setId(approvalId);
                approvalBean.setOrderNo(orderNo);
                approvalBean.setDealState(dealState);
                approvalBean.setAgreeAmount(refundAmount);
                approvalBean.setUserId(userId);
                approvalBean.setAdminId(user.getId());

                OrderCancelApprovalDetails approvalDetails = new OrderCancelApprovalDetails();
                approvalDetails.setApprovalId(approvalId);
                if (dealState < 4) {
                    approvalDetails.setDealState(dealState);
                } else {
                    approvalDetails.setDealState(4);
                }
                approvalDetails.setOrderNo(orderNo);
                approvalDetails.setPayPrice(refundAmount);
                approvalDetails.setAdminId(user.getId());
                approvalDetails.setRemark(remark);

                if(dealState < 2){
                    approvalService.updateOrderCancelApprovalState(approvalBean);
                    approvalService.insertIntoApprovalDetails(approvalDetails);
                    //使用MQ更新线上状态
                    updateOnlineDealState(approvalBean);
                }
                if (dealState == 2 || dealState == 3) {
                    json = refundOrderCancelByApi(approvalBean, approvalDetails);
                }
            } else {
                json.setOk(false);
                json.setMessage("二次密码验证失败,请重试");
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("setAndRemark error,reason: " + e.getMessage());
            json.setOk(false);
            json.setMessage("执行失败,原因：" + e.getMessage());
        }
        json.setData(null);
        return json;
    }


    @RequestMapping("/queryApprovalDetails")
    @ResponseBody
    public ModelAndView queryApprovalDetails(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("approvalDetails");
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            mv.addObject("success", 0);
            mv.addObject("message", "请登录后操作");
            return mv;
        }

        //获取申诉id
        String approvalIdStr = request.getParameter("approvalId");
        int approvalId = 0;
        if (StringUtils.isBlank(approvalIdStr)) {
            mv.addObject("success", 0);
            mv.addObject("message", "获取申请ID失败,请重试");
            return mv;
        } else {
            approvalId = Integer.valueOf(approvalIdStr);
        }
        try {
            OrderCancelApproval approvalBean = approvalService.queryForSingle(approvalId);
            List<OrderCancelApprovalDetails> approvalDetailsList = approvalService.queryForDetailsList(approvalId);
            mv.addObject("success", 1);
            mv.addObject("approvalBean", approvalBean);
            mv.addObject("approvalDetails", approvalDetailsList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("approvalId：" + approvalId + ",queryApprovalDetails，原因 :" + e.getMessage());
            mv.addObject("success", 0);
            mv.addObject("message", "approvalId：" + approvalId + ",queryApprovalDetails，原因:" + e.getMessage());
        }
        return mv;
    }


    private JsonResult refundOrderCancelByApi(OrderCancelApproval approvalBean, OrderCancelApprovalDetails approvalDetails) {

        JsonResult json = new JsonResult();
        try {
            json = ppApiService.reFundNew(approvalBean.getOrderNo(), decimalFormat.format(approvalBean.getAgreeAmount()));
            OrderCancelApproval approvalOld =   approvalService.queryForSingle(approvalBean.getId());
            if (json.isOk()) {
                if(approvalOld.getDealState() == 1 || approvalOld.getDealState() == 2){
                    approvalBean.setDealState(2);
                    approvalService.updateOrderCancelApprovalState(approvalBean);
                    approvalDetails.setDealState(2);
                    approvalService.insertIntoApprovalDetails(approvalDetails);
                }

                approvalBean.setDealState(3);
                approvalDetails.setDealState(3);
                approvalDetails.setRemark(approvalDetails.getRemark() + ",执行API退款成功！<br>" + json.getMessage());
                approvalService.updateOrderCancelApprovalState(approvalBean);
                approvalService.insertIntoApprovalDetails(approvalDetails);
                //使用MQ更新线上状态
                updateOnlineDealState(approvalBean);
                // 添加一笔负的到账
                approvalService.insertIntoPaymentByApproval(approvalBean.getUserId(), approvalBean.getOrderNo());
                // 退给客户余额，如果有
                List<PaymentDetails> paymentDetails = paymentServiceNew.queryPaymentDetails(approvalBean.getOrderNo());
                if (paymentDetails != null && paymentDetails.size() > 0) {
                    float balance = 0;
                    for (PaymentDetails paymentDt : paymentDetails) {
                        if (paymentDt.getPayType() == 2) {
                            balance += paymentDt.getPaymentAmount();
                        }
                    }
                    if (balance > 0) {
                        UserDao dao = new UserDaoImpl();
                        dao.updateUserAvailable(approvalBean.getUserId(),
                                new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
                                " system closeOrder:" + approvalBean.getOrderNo(), approvalBean.getOrderNo(),
                                String.valueOf(approvalBean.getAdminId()), 0, 0, 1);
                    }
                }
            } else {
                if(approvalOld.getDealState() == 1 || approvalOld.getDealState() == 2){
                    approvalBean.setDealState(2);
                    approvalService.updateOrderCancelApprovalState(approvalBean);
                    approvalDetails.setDealState(2);
                    approvalDetails.setRemark(approvalDetails.getRemark() + "[退款失败，请重试]");
                    approvalService.insertIntoApprovalDetails(approvalDetails);
                }
                logger.error("userId:" + approvalBean.getUserId() + ",approvalId:" + approvalBean.getId() + ",refundByPayPalApi error :" + json.getMessage());
                System.err.println("userId:" + approvalBean.getUserId() + ",approvalId:" + approvalBean.getId() + ",refundByPayPalApi error :" + json.getMessage());
                json.setOk(false);
                json.setMessage("退款失败,原因:[" + json.getMessage() + "],联系IT人员查看");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("userId:" + approvalBean.getUserId() + ",rdId:" + approvalBean.getId() + ",refundByPayPalApi error :" + e.getMessage());
            System.err.println("userId:" + approvalBean.getUserId() + ",rdId:" + approvalBean.getId() + ",refundByPayPalApi error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("退款失败,请联系IT人员查看");
        }
        return json;
    }

    private void updateOnlineDealState(OrderCancelApproval approvalBean) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        String sql = "update order_cancel_approval set deal_state =" + approvalBean.getDealState()
                + " ,agree_amount = " + approvalBean.getAgreeAmount() + ",update_time = '"
                + DATE_FORMAT.format(calendar.getTime()) + "' where user_id = " + approvalBean.getUserId()
                + " and order_no = '" + approvalBean.getOrderNo() + "'";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


}
