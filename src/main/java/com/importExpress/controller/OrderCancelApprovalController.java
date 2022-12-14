package com.importExpress.controller;

import com.cbt.bean.BalanceBean;
import com.cbt.paypal.service.PayPalService;
import com.cbt.service.AdditionalBalanceService;
import com.cbt.systemcode.service.SecondaryValidationService;
import com.cbt.util.BigDecimalUtil;
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
import com.importExpress.pojo.OrderCancelApprovalAmount;
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
    @Autowired
    private AdditionalBalanceService additionalBalanceService;


    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("orderCancelApproval");

        EasyUiJsonResult json = new EasyUiJsonResult();

        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            mv.addObject("success", 0);
            mv.addObject("message", "??????????????????");
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
            userId = Integer.parseInt(userIdStr);
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
            type = Integer.parseInt(typeStr);
        }
        mv.addObject("type", type);


        int dealState = -1;
        String dealStateStr = request.getParameter("dealState");

        if (StringUtils.isNotBlank(dealStateStr)) {
            dealState = Integer.parseInt(dealStateStr);
        }
        mv.addObject("dealState", dealState);


        int startNum = 0;
        int limitNum = 30;
        int page = 1;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            page = Integer.parseInt(pageStr);
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
            if (userId > 0) {
                cancelApproval.setUserId(userId);
            }
            if (StringUtils.isNotBlank(orderNo)) {
                cancelApproval.setOrderNo(orderNo);
            }
            cancelApproval.setStartNum(startNum);
            cancelApproval.setLimitNum(limitNum);

            List<OrderCancelApproval> list = approvalService.queryForList(cancelApproval);

            int total = approvalService.queryForListCount(cancelApproval);
            for (OrderCancelApproval approvalBean : list) {
                List<OrderCancelApprovalDetails> detailsList = approvalService.queryForDetailsList(approvalBean.getId());
                for (OrderCancelApprovalDetails approvalDetail : detailsList) {
                    //??????????????????
                    if (approvalDetail.getDealState() == 1) {
                        //?????????1?????????????????????????????????????????????
                        approvalBean.setApproval1(approvalDetail);
                    } else if (approvalDetail.getDealState() == 2) {
                        //?????????2?????????????????????????????????????????????
                        approvalBean.setApproval2(approvalDetail);
                    } else if (approvalDetail.getDealState() == 3) {
                        //?????????3?????????????????????????????????
                        approvalBean.setApproval3(approvalDetail);
                    } else if (approvalDetail.getDealState() == 4) {
                        //?????????9??????????????????
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
            mv.addObject("message", "?????????????????????:" + e.getMessage());
            logger.error("????????????????????? :" + e.getMessage());
        }
        return mv;
    }


    @RequestMapping(value = "/setStateAndRemark", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult setStateAndRemark(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        try {
            //????????????????????????
            String sessionId = request.getSession().getId();
            String userJson = Redis.hget(sessionId, "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null) {
                json.setOk(false);
                json.setMessage("????????????,???????????????");
                return json;
            }
            //????????????id
            String approvalIdStr = request.getParameter("approvalId");
            int approvalId = 0;
            if (StringUtils.isBlank(approvalIdStr)) {
                json.setOk(false);
                json.setMessage("????????????id??????,?????????");
                return json;
            } else {
                approvalId = Integer.parseInt(approvalIdStr);
            }

            String dealStateStr = request.getParameter("dealState");
            int dealState = 0;
            if (StringUtils.isBlank(dealStateStr)) {
                json.setOk(false);
                json.setMessage("????????????????????????,?????????");
                return json;
            } else {
                dealState = Integer.parseInt(dealStateStr);
            }

            String userIdStr = request.getParameter("userId");
            int userId = 0;
            if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
                json.setOk(false);
                json.setMessage("????????????id??????,?????????");
                return json;
            } else {
                userId = Integer.parseInt(userIdStr);
            }

            String orderNo = request.getParameter("orderNo");
            if (StringUtils.isBlank(orderNo)) {
                json.setOk(false);
                json.setMessage("???????????????????????????,?????????");
                return json;
            } else if (orderNo.contains("_")) {
                // jxw 19-05-09 ???????????????dp????????????????????????,????????????????????????
                String[] orderSplit = orderNo.split("_");
                if(orderSplit[1].length() > 2){
                    json.setOk(false);
                    json.setMessage("??????????????????????????????????????????");
                    return json;
                }
            }
            String operatorIdStr = request.getParameter("operatorId");
            int operatorId = 0;
            if (StringUtils.isBlank(operatorIdStr) || "0".equals(operatorIdStr)) {
                json.setOk(false);
                json.setMessage("???????????????ID??????,?????????");
                return json;
            } else {
                operatorId = Integer.parseInt(operatorIdStr);
                if (operatorId != user.getId()) {
                    json.setOk(false);
                    json.setMessage("??????????????????????????????");
                    return json;
                }
            }

            String refundMethodStr = request.getParameter("refundMethod");
            if (StringUtils.isBlank(refundMethodStr) || Integer.parseInt(refundMethodStr) == 0) {
                json.setOk(false);
                json.setMessage("????????????????????????,?????????");
                return json;
            }

            String refundAmountStr = request.getParameter("refundAmount");
            double refundAmount = 0;
            if (dealState == 4) {
                refundAmount = 0;
            } else {
                if (StringUtils.isBlank(refundAmountStr) || "0".equals(refundAmountStr)) {
                    json.setOk(false);
                    json.setMessage("????????????????????????,?????????");
                    return json;
                } else if ("1".equals(refundMethodStr) && Double.parseDouble(refundAmountStr) >= 300 && dealState > 1) {
                    json.setOk(false);
                    json.setMessage("?????????????????????300????????????");
                    return json;
                } else {
                    refundAmount = Double.parseDouble(refundAmountStr);
                }
            }




            String remark = request.getParameter("remark");
            if (StringUtils.isBlank(remark)) {
                json.setOk(false);
                json.setMessage("????????????????????????,?????????");
                return json;
            }

            String secvlidPwd = request.getParameter("secvlidPwd");
            if (dealState == 3 && StringUtils.isBlank(secvlidPwd)) {
                json.setOk(false);
                json.setMessage("??????????????????,?????????");
                return json;
            }

            boolean isCheck;
            if (dealState == 3) {
                isCheck = secondaryValidationService.checkExistsPassword(operatorId, secvlidPwd);
            } else {
                //???????????????1??????????????????????????????????????????
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
                approvalBean.setRefundMethod(Integer.parseInt(refundMethodStr));

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
                approvalDetails.setRefundMethod(Integer.parseInt(refundMethodStr));

                if (dealState < 2) {
                    approvalService.updateOrderCancelApprovalState(approvalBean);
                    approvalService.insertIntoApprovalDetails(approvalDetails);
                    //??????MQ??????????????????
                    updateOnlineDealState(approvalBean);

                    // ????????????
                    approvalBean.setDealState(2);
                    approvalService.updateOrderCancelApprovalState(approvalBean);
                    approvalDetails.setDealState(2);
                    approvalDetails.setRemark(remark + "@????????????????????????");
                    approvalService.insertIntoApprovalDetails(approvalDetails);

                }else if(dealState == 4){
                    approvalService.updateOrderCancelApprovalState(approvalBean);
                    approvalDetails.setDealState(3);
                    approvalService.insertIntoApprovalDetails(approvalDetails);
                    //??????MQ??????????????????
                    updateOnlineDealState(approvalBean);
                }
                if (dealState == 2 || dealState == 3) {
                    json = refundOrderCancelByApi(approvalBean, approvalDetails);
                }
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????,?????????");
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("setAndRemark error,reason: " + e.getMessage());
            json.setOk(false);
            json.setMessage("????????????,?????????" + e.getMessage());
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
            mv.addObject("message", "??????????????????");
            return mv;
        }

        //????????????id
        String approvalIdStr = request.getParameter("approvalId");
        int approvalId = 0;
        if (StringUtils.isBlank(approvalIdStr)) {
            mv.addObject("success", 0);
            mv.addObject("message", "????????????ID??????,?????????");
            return mv;
        } else {
            approvalId = Integer.parseInt(approvalIdStr);
        }
        try {
            OrderCancelApproval approvalBean = approvalService.queryForSingle(approvalId);
            List<OrderCancelApprovalDetails> approvalDetailsList = approvalService.queryForDetailsList(approvalId);
            mv.addObject("success", 1);
            mv.addObject("approvalBean", approvalBean);
            mv.addObject("approvalDetails", approvalDetailsList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("approvalId???" + approvalId + ",queryApprovalDetails????????? :" + e.getMessage());
            mv.addObject("success", 0);
            mv.addObject("message", "approvalId???" + approvalId + ",queryApprovalDetails?????????:" + e.getMessage());
        }
        return mv;
    }


    private JsonResult refundOrderCancelByApi(OrderCancelApproval approvalBean, OrderCancelApprovalDetails approvalDetails) {

        JsonResult json = new JsonResult();
        try {
            String refundOrderNo = approvalBean.getOrderNo();
            if (refundOrderNo.contains("_")) {
                String[] orderSplit = refundOrderNo.split("_");
                if (orderSplit[1].trim().length() > 2) {
                    json.setOk(false);
                    json.setMessage("??????????????????????????????????????????");
                    return json;
                } else {
                    refundOrderNo = orderSplit[0].trim();
                }
            }

            // ?????????????????????PayPal???stripe??????????????????PayPal???stripe?????????????????????????????????????????????????????????????????????????????????????????????????????????
            List<PaymentDetails> list = paymentServiceNew.queryPaymentDetails(refundOrderNo);

            double totalAmount = 0;
            double orderBalance = 0;
            double orderPay = 0;

            for (PaymentDetails pd : list) {
                // ??????????????????
                totalAmount += pd.getPaymentAmount();
                if (pd.getPayType() == 2) {
                    // ?????????????????????
                    orderBalance += pd.getPaymentAmount();
                } else if (pd.getPayType() == 0 || pd.getPayType() == 1 || pd.getPayType() == 5) {
                    // ??????PayPal TT stripe?????????
                    orderPay += pd.getPaymentAmount();
                }
            }
            list.clear();

            /*if(orderPay > 300){
                json.setOk(false);
                json.setMessage("????????????????????????:" + orderPay + "??????300????????????");
                return json;
            }*/

            OrderCancelApproval approvalOld = approvalService.queryForSingle(approvalBean.getId());
            if (approvalBean.getRefundMethod() == 1) {
                // PayPal??????Stripe??????
                // ???????????????????????? ???????????? ????????????
                if (totalAmount - approvalBean.getAgreeAmount() < -0.01) {
                    // ??????????????????
                    json.setOk(false);
                    json.setMessage("????????????,[????????????" + refundOrderNo + ",???????????????"
                            + BigDecimalUtil.truncateDouble(totalAmount, 2)
                            + "????????????????????????" + BigDecimalUtil.truncateDouble(approvalBean.getAgreeAmount(), 2) + "]");
                } else {
                    // ??????PayPal TT stripe???????????????
                    if (orderPay > 0) {
                        if (orderPay - approvalBean.getAgreeAmount() >= -0.01) {
                            json = ppApiService.refundByMq(refundOrderNo, decimalFormat.format(approvalBean.getAgreeAmount()));
                        } else {
                            json = ppApiService.refundByMq(refundOrderNo, decimalFormat.format(orderPay));
                            approvalBean.setRemainAmount(approvalBean.getAgreeAmount() - orderPay);
                        }
                    } else {
                        // ??????????????????????????????????????????
                        json.setOk(true);
                        json.setTotal(0L);
                        approvalBean.setRemainAmount(approvalBean.getAgreeAmount());
                    }

                    if (json.isOk()) {
                        OrderCancelApprovalAmount approvalAmount = new OrderCancelApprovalAmount();
                        approvalAmount.setApprovalId(approvalBean.getId());
                        approvalAmount.setOrderNo(approvalBean.getOrderNo());
                        if (orderPay > 0) {
                            if (orderPay - approvalBean.getAgreeAmount() >= -0.01) {
                                approvalAmount.setPayAmount(approvalBean.getAgreeAmount());
                            } else {
                                approvalAmount.setPayAmount(orderPay);
                            }
                        } else {
                            approvalAmount.setPayAmount(approvalBean.getAgreeAmount());
                        }
                        approvalAmount.setPayType(json.getTotal().intValue());
                        // ?????????PayPal?????????????????????????????????
                        if (orderPay > 0) {
                            approvalService.insertIntoOrderCancelApprovalAmount(approvalAmount);
                        }
                        if (approvalOld.getDealState() == 1 || approvalOld.getDealState() == 2) {
                            approvalBean.setDealState(2);
                            approvalService.updateOrderCancelApprovalState(approvalBean);
                            approvalDetails.setDealState(2);
                            approvalService.insertIntoApprovalDetails(approvalDetails);
                        }

                        approvalBean.setDealState(3);
                        approvalDetails.setDealState(3);
                        if (orderPay > 0) {
                            if (orderPay - approvalBean.getAgreeAmount() >= -0.01) {
                                approvalDetails.setRemark(approvalDetails.getRemark() + ",?????????API??????????????????(API?????????" +
                                        BigDecimalUtil.truncateDouble(approvalBean.getAgreeAmount(), 2)
                                        + ")<br>" + json.getMessage());
                            } else {
                                approvalDetails.setRemark(approvalDetails.getRemark() + ",?????????API??????????????????(API?????????" +
                                        BigDecimalUtil.truncateDouble(orderPay, 2) + "???????????????:"
                                        + BigDecimalUtil.truncateDouble(approvalBean.getAgreeAmount() - orderPay, 2)
                                        + ")<br>" + json.getMessage());
                            }
                        } else {
                            approvalDetails.setRemark(approvalDetails.getRemark() + ",?????????????????????????????????(???????????????" +
                                    BigDecimalUtil.truncateDouble(approvalBean.getAgreeAmount(), 2) + ")");
                        }
                        approvalService.updateOrderCancelApprovalState(approvalBean);
                        approvalService.insertIntoApprovalDetails(approvalDetails);
                        //??????MQ??????????????????
                        updateOnlineDealState(approvalBean);
                        // ????????????????????????
                        approvalService.insertIntoPaymentByApproval(approvalBean.getUserId(), approvalBean.getOrderNo());
                        // ??????????????????????????????
                        if (approvalBean.getRemainAmount() > 0) {
                            UserDao dao = new UserDaoImpl();
                            dao.updateUserAvailable(approvalBean.getUserId(),
                                    new BigDecimal(approvalBean.getRemainAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
                                    " system closeOrder:" + approvalBean.getOrderNo(), approvalBean.getOrderNo(),
                                    String.valueOf(approvalBean.getAdminId()), 0, 0, 1);
                            // ??????????????????
                            approvalAmount.setPayAmount(approvalBean.getRemainAmount());
                            approvalAmount.setPayType(2);
                            approvalService.insertIntoOrderCancelApprovalAmount(approvalAmount);
                        }
                    } else {
                        if (approvalOld.getDealState() == 1 || approvalOld.getDealState() == 2) {
                            approvalBean.setDealState(2);
                            approvalService.updateOrderCancelApprovalState(approvalBean);
                            approvalDetails.setDealState(2);
                            approvalDetails.setRemark(approvalDetails.getRemark() + "[????????????????????????]");
                            approvalService.insertIntoApprovalDetails(approvalDetails);
                        }
                        logger.error("userId:" + approvalBean.getUserId() + ",approvalId:" + approvalBean.getId() + ",refundByPayPalApi error :" + json.getMessage());
                        System.err.println("userId:" + approvalBean.getUserId() + ",approvalId:" + approvalBean.getId() + ",refundByPayPalApi error :" + json.getMessage());
                        json.setOk(false);
                        json.setMessage("??????:[" + json.getMessage() + "],??????IT????????????");
                    }
                }
            } else if (approvalBean.getRefundMethod() == 2) {
                // ????????????
                if(totalAmount - approvalBean.getAgreeAmount() < -0.01){
                    // ??????????????????
                    json.setOk(false);
                    json.setMessage("????????????,[????????????" + refundOrderNo + ",???????????????"
                            + BigDecimalUtil.truncateDouble(totalAmount, 2)
                            + "????????????????????????" + BigDecimalUtil.truncateDouble(approvalBean.getAgreeAmount(), 2) + "]");
                } else {
                    OrderCancelApprovalAmount approvalAmount = new OrderCancelApprovalAmount();
                    approvalAmount.setApprovalId(approvalBean.getId());
                    approvalAmount.setOrderNo(approvalBean.getOrderNo());
                    approvalAmount.setPayAmount(approvalBean.getAgreeAmount());
                    approvalAmount.setPayType(2);
                    if (approvalOld.getDealState() == 1 || approvalOld.getDealState() == 2) {
                        approvalBean.setDealState(2);
                        approvalService.updateOrderCancelApprovalState(approvalBean);
                        approvalDetails.setDealState(2);
                        approvalService.insertIntoApprovalDetails(approvalDetails);
                    }
                    approvalBean.setDealState(3);
                    approvalDetails.setDealState(3);
                    approvalDetails.setRemark(approvalDetails.getRemark() + ",?????????????????????????????????(???????????????" +
                            BigDecimalUtil.truncateDouble(approvalBean.getAgreeAmount(), 2) + ")");
                    approvalService.updateOrderCancelApprovalState(approvalBean);
                    approvalService.insertIntoApprovalDetails(approvalDetails);
                    //??????MQ??????????????????
                    updateOnlineDealState(approvalBean);
                    // ????????????????????????
                    approvalService.insertIntoPaymentByApproval(approvalBean.getUserId(), approvalBean.getOrderNo());
                    // ??????????????????????????????
                    UserDao dao = new UserDaoImpl();
                    dao.updateUserAvailable(approvalBean.getUserId(),
                            new BigDecimal(approvalBean.getAgreeAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
                            " system closeOrder:" + approvalBean.getOrderNo(), approvalBean.getOrderNo(),
                            String.valueOf(approvalBean.getAdminId()), 0, 0, 1);
                    // ??????????????????
                    approvalAmount.setPayAmount(approvalBean.getAgreeAmount());
                    approvalAmount.setPayType(2);
                    approvalService.insertIntoOrderCancelApprovalAmount(approvalAmount);
                }
            } else {
                json.setOk(false);
                json.setMessage("??????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("userId:" + approvalBean.getUserId() + ",rdId:" + approvalBean.getId() + ",refundByPayPalApi error :" + e.getMessage());
            System.err.println("userId:" + approvalBean.getUserId() + ",rdId:" + approvalBean.getId() + ",refundByPayPalApi error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("?????????IT????????????");
        }
        return json;
    }


    private void updateOnlineDealState(OrderCancelApproval approvalBean) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        String sql = "update order_cancel_approval set deal_state =" + approvalBean.getDealState()
                + " ,agree_amount = " + approvalBean.getAgreeAmount() + ",refund_method = " + approvalBean.getRefundMethod()
                + ",update_time = '" + DATE_FORMAT.format(calendar.getTime()) + "' where user_id = " + approvalBean.getUserId()
                + " and order_no = '" + approvalBean.getOrderNo() + "'";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/offLineRefund", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult offLineRefund(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        try {
            //????????????????????????
            String sessionId = request.getSession().getId();
            String userJson = Redis.hget(sessionId, "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null) {
                json.setOk(false);
                json.setMessage("????????????,???????????????");
                return json;
            }
            //????????????id
            String approvalIdStr = request.getParameter("approvalId");
            int approvalId = 0;
            if (StringUtils.isBlank(approvalIdStr)) {
                json.setOk(false);
                json.setMessage("????????????id??????,?????????");
                return json;
            } else {
                approvalId = Integer.parseInt(approvalIdStr);
            }

            String dealStateStr = request.getParameter("dealState");
            int dealState = 0;
            if (StringUtils.isBlank(dealStateStr)) {
                json.setOk(false);
                json.setMessage("????????????????????????,?????????");
                return json;
            } else {
                dealState = Integer.parseInt(dealStateStr);
            }

            String userIdStr = request.getParameter("userId");
            int userId = 0;
            if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
                json.setOk(false);
                json.setMessage("????????????id??????,?????????");
                return json;
            } else {
                userId = Integer.parseInt(userIdStr);
            }

            String orderNo = request.getParameter("orderNo");
            String operatorIdStr = request.getParameter("operatorId");
            int operatorId = 0;
            if (StringUtils.isBlank(operatorIdStr) || "0".equals(operatorIdStr)) {
                json.setOk(false);
                json.setMessage("???????????????ID??????,?????????");
                return json;
            }

            String txnId = request.getParameter("txnId");
            if (StringUtils.isBlank(txnId)) {
                json.setOk(false);
                json.setMessage("???????????????????????????,?????????");
                return json;
            }
            String refundMethodStr = request.getParameter("refundMethod");
            if (StringUtils.isBlank(refundMethodStr) || Integer.parseInt(refundMethodStr) == 0) {
                json.setOk(false);
                json.setMessage("????????????????????????,?????????");
                return json;
            }

            String refundAmountStr = request.getParameter("refundAmount");
            double refundAmount = Double.parseDouble(refundAmountStr);

            String remark = request.getParameter("remark");
            if (StringUtils.isBlank(remark)) {
                json.setOk(false);
                json.setMessage("????????????????????????,?????????");
                return json;
            }

            OrderCancelApproval approvalBean = new OrderCancelApproval();
            approvalBean.setId(approvalId);
            approvalBean.setOrderNo(orderNo);
            approvalBean.setDealState(dealState);
            approvalBean.setAgreeAmount(refundAmount);
            approvalBean.setUserId(userId);
            approvalBean.setAdminId(user.getId());
            approvalBean.setRefundMethod(Integer.parseInt(refundMethodStr));

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
            approvalDetails.setRefundMethod(Integer.parseInt(refundMethodStr));


            OrderCancelApproval approvalOld = approvalService.queryForSingle(approvalBean.getId());
            OrderCancelApprovalAmount approvalAmount = new OrderCancelApprovalAmount();
            approvalAmount.setApprovalId(approvalBean.getId());
            approvalAmount.setOrderNo(approvalBean.getOrderNo());
            approvalAmount.setPayAmount(approvalBean.getAgreeAmount());
            approvalAmount.setPayType(2);
            if (approvalOld.getDealState() == 1 || approvalOld.getDealState() == 2) {
                approvalBean.setDealState(2);
                approvalService.updateOrderCancelApprovalState(approvalBean);
                approvalDetails.setDealState(2);
                approvalService.insertIntoApprovalDetails(approvalDetails);
            }
            approvalBean.setDealState(3);
            approvalDetails.setDealState(3);
            approvalDetails.setRemark(approvalDetails.getRemark() + ",?????????????????????????????????(?????????:" +txnId + "," +
                    BigDecimalUtil.truncateDouble(approvalBean.getAgreeAmount(), 2) + ")");
            approvalService.updateOrderCancelApprovalState(approvalBean);
            approvalService.insertIntoApprovalDetails(approvalDetails);
            //??????MQ??????????????????
            updateOnlineDealState(approvalBean);
            // ????????????????????????
            approvalService.insertIntoPaymentByApproval(approvalBean.getUserId(), approvalBean.getOrderNo());

            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("offLineRefund error,reason: " + e.getMessage());
            json.setOk(false);
            json.setMessage("????????????,?????????" + e.getMessage());
        }
        return json;
    }

}
