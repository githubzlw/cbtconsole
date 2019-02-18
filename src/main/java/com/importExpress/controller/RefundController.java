package com.importExpress.controller;

import com.cbt.bean.BalanceBean;
import com.cbt.bean.OrderBean;
import com.cbt.paypal.service.PayPalService;
import com.cbt.service.AdditionalBalanceService;
import com.cbt.service.impl.AdditionalBalanceServiceImpl;
import com.cbt.systemcode.service.SecondaryValidationService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.bean.PaymentDetails;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.RefundDetailsBean;
import com.importExpress.pojo.RefundNewBean;
import com.importExpress.pojo.RefundResultInfo;
import com.importExpress.service.RefundNewService;
import com.importExpress.utli.NotifyToCustomerUtil;
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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退款相关处理
 */
@Controller
@RequestMapping("/refundCtr")
public class RefundController {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(RefundController.class);

    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static AdditionalBalanceService additionalBalanceService = new AdditionalBalanceServiceImpl();

    @Autowired
    private RefundNewService refundNewService;

    @Autowired
    private SecondaryValidationService secondaryValidationService;

    @Autowired
    private PayPalService ppApiService;


    /**
     * @param request
     * @param response
     * @return ModelAndView
     * @Title queryForList
     * @Description 根据条件查询列表数据
     */
    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("refundManage");

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

        String payPalEmail = request.getParameter("payPalEmail");
        if (StringUtils.isNotBlank(payPalEmail)) {
            mv.addObject("payPalEmail", payPalEmail);
        } else {
            mv.addObject("payPalEmail", "");
        }

        String beginTime = request.getParameter("beginTime");
        if (StringUtils.isNotBlank(beginTime)) {
            mv.addObject("beginTime", beginTime);
        } else {
            mv.addObject("beginTime", "");
        }

        String endTime = request.getParameter("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            mv.addObject("endTime", endTime);
        } else {
            mv.addObject("endTime", "");
        }

        String typeStr = request.getParameter("type");
        int type = -1;
        if (StringUtils.isNotBlank(typeStr)) {
            type = Integer.valueOf(typeStr);
        }
        mv.addObject("type", type);


        int state = -1;
        /*String stateStr = request.getParameter("state");

        if (StringUtils.isNotBlank(stateStr)) {
            state = Integer.valueOf(stateStr);
        }
        mv.addObject("state", state);*/

        String appMoneyStr = request.getParameter("appMoney");
        int appMoney = 0;
        if (StringUtils.isNotBlank(appMoneyStr)) {
            appMoney = Integer.valueOf(appMoneyStr);
        }
        mv.addObject("appMoney", appMoney);

        //判断选中的状态进行参数设置
        int chooseState = -1;
        String chooseStateStr = request.getParameter("chooseState");
        if (StringUtils.isNotBlank(chooseStateStr)) {
            chooseState = Integer.valueOf(chooseStateStr);
        }
        mv.addObject("chooseState", chooseState);
        if (chooseState == 0) {
            //chooseState=0表示待审批，如果是管理员，则待审批是销售同意状态
            if ("0".equals(adm.getRoletype())) {
                if(1 == adm.getId()){
                    //操作人是Ling，则是销售审批状态
                    state = 1;
                }else{
                    //如果是Emma，则是主管审批状态
                    state = 2;
                }
            } else {
                //如果是销售，则待审批是退款申请状态
                state = 0;
            }
        } else if (chooseState == 9) {
            //chooseState=9表示驳回
            state = 9;
        } else if (chooseState == 3) {
            //chooseState=3表示财务确认
            state = 3;
        } else if (chooseState == 4) {
            //chooseState=4表示已经完结
            state = 4;
        }

        int startNum = 0;
        int limitNum = 10;
        int page = 1;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            page = Integer.valueOf(pageStr);
            startNum = (page - 1) * limitNum;
        }
        mv.addObject("page", page);
        try {

            List<RefundNewBean> list = refundNewService.queryForList(userId, payPalEmail, beginTime, endTime, type, state,
                    appMoney, adminId, startNum, limitNum);
            int total = refundNewService.queryForListCount(userId, payPalEmail, beginTime, endTime, type, state, appMoney, adminId);
            for (RefundNewBean refundBean : list) {
                List<RefundDetailsBean> refundDetailsList = refundNewService.queryRefundDetailsByRefundId(refundBean.getId());
                for (RefundDetailsBean refundDetailsBean : refundDetailsList) {
                    //判断各个状态

                    if (refundDetailsBean.getRefundState() == 1) {
                        //状态是1的为销售审批，放入销售审批详情
                        refundBean.setSalesApproval(refundDetailsBean);
                    } else if (refundDetailsBean.getRefundState() == 2) {
                        //状态是2的为主管审批，放入主管审批详情
                        refundBean.setAdminApproval(refundDetailsBean);
                    } else if (refundDetailsBean.getRefundState() == 3) {
                        //状态是3的为财务确认，放入财务确认详情
                        refundBean.setFinancialApproval(refundDetailsBean);
                    } else if (refundDetailsBean.getRefundState() == 4) {
                        //状态是4的为已经执行退款
                        refundBean.setFinishApproval(refundDetailsBean);
                    } else if (refundDetailsBean.getRefundState() == 9) {
                        //状态是9的为驳回信息
                        //判断是销售还是管理员驳回的
                        if (refundBean.getSalesId() == refundDetailsBean.getAdminId()) {
                            refundBean.setSalesApproval(refundDetailsBean);
                        } else {
                            //Ling驳回放入主管审批
                            //EMMA驳回放入财务审批
                            if(refundDetailsBean.getAdminId() == 8){
                                refundBean.setFinancialApproval(refundDetailsBean);
                            }else{
                                refundBean.setAdminApproval(refundDetailsBean);
                            }

                        }
                    }
                }
                refundDetailsList.clear();
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
            logger.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            mv.addObject("success", 0);
            mv.addObject("message", "查询失败，原因:" + e.getMessage());
        }
        return mv;
    }


    /**
     * 设置并备注退款信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/setAndRemark", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult setAndRemark(HttpServletRequest request, HttpServletResponse response) {

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
            String refundIdStr = request.getParameter("refundId");
            int refundId = 0;
            if (StringUtils.isBlank(refundIdStr)) {
                json.setOk(false);
                json.setMessage("获取id失败,请重试");
                return json;
            } else {
                refundId = Integer.valueOf(refundIdStr);
            }

            String typeStr = request.getParameter("type");
            int type = 0;
            if (StringUtils.isBlank(typeStr)) {
                json.setOk(false);
                json.setMessage("获取申请类型失败,请重试");
                return json;
            } else {
                type = Integer.valueOf(typeStr);
            }
//
//            String stateStr = request.getParameter("state");
//            int state = 0;
//            if (StringUtils.isNotBlank(stateStr)) {
//                json.setOk(false);
//                json.setMessage("获取状态失败,请重试");
//                return json;
//            } else {
//                state = Integer.valueOf(stateStr);
//            }

            String actionFlagStr = request.getParameter("actionFlag");
            int actionFlag = 0;
            if (StringUtils.isBlank(actionFlagStr)) {
                json.setOk(false);
                json.setMessage("获取操作状态失败,请重试");
                return json;
            } else {
                actionFlag = Integer.valueOf(actionFlagStr);
            }

            String userIdStr = request.getParameter("userId");
            int userId = 0;
            if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
                json.setOk(false);
                json.setMessage("获取客户ID失败,请重试");
                return json;
            } else {
                userId = Integer.valueOf(userIdStr);
            }

            String orderNo = request.getParameter("orderNo");
            if (StringUtils.isBlank(orderNo)) {
                json.setOk(false);
                json.setMessage("获取订单号失败,请重试");
                return json;
            }else if(orderNo.contains("_")){
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
            if (StringUtils.isBlank(refundAmountStr) || "0".equals(refundAmountStr)) {
                json.setOk(false);
                json.setMessage("获取退款金额失败,请重试");
                return json;
            } else {
                refundAmount = Double.valueOf(refundAmountStr);
            }

            String remark = request.getParameter("remark");
            if (StringUtils.isBlank(remark)) {
                json.setOk(false);
                json.setMessage("获取备注信息失败,请重试");
                return json;
            }

            String secvlidPwd = request.getParameter("secvlidPwd");
            if (actionFlag > 1 && StringUtils.isBlank(secvlidPwd) && user.getId() > 1) {
                json.setOk(false);
                json.setMessage("获取密码失败,请重试");
                return json;
            }

            boolean isCheck = false;
            if (actionFlag == 1 || operatorId == 1) {
                //操作状态是1的说明是销售，不需要校检密码
                isCheck = true;
            } else {
                isCheck = secondaryValidationService.checkExistsPassword(operatorId, secvlidPwd);
            }
            if (isCheck) {
                RefundDetailsBean detailsBean = new RefundDetailsBean();
                detailsBean.setRefundId(refundId);
                detailsBean.setAdminId(user.getId());
                detailsBean.setOrderNo(orderNo);
                detailsBean.setRefundState(actionFlag);
                detailsBean.setRefundAmount(refundAmount);
                detailsBean.setRemark(remark);
                detailsBean.setUserId(userId);
                if (actionFlag != 4) {
                    refundNewService.setAndRemark(detailsBean);
                    //使用MQ更新线上状态
                    updateOnlineRefundState(detailsBean);
                }

                //判断是否是主管同意状态，如果是，并且金额<=50则直接退款给客户，并且完结订单
                if (user.getId() == 1 && "0".equals(user.getRoletype())) {
                    //判断是Ling账号
                    /*if (actionFlag == 2 && refundAmount <= 50) {
                        //主管同意状态,并且金额小于等于50美元,自动设置到财务确认状态
                        detailsBean.setRefundState(3);
                        detailsBean.setRemark("退款金额小于等于50美金,系统自动确认为财务确认状态");
                        refundNewService.setAndRemark(detailsBean);
                        //使用MQ更新线上状态
                        updateOnlineRefundState(detailsBean);
                        //退款操作
                        json = refundByUserId(detailsBean, type, user.getAdmName());
                    }*/
                } else if ((user.getId() == 8 || user.getId() == 83) && "0".equals(user.getRoletype())) {
                    //判断是EMMA或者Mandy
                    if (actionFlag == 2 || actionFlag == 3) {
                        if (actionFlag == 2) {
                            //主管同意状态
                            detailsBean.setRefundState(3);
                            detailsBean.setRemark("系统自动确认为财务确认状态");
                            refundNewService.setAndRemark(detailsBean);
                            //使用MQ更新线上状态
                            updateOnlineRefundState(detailsBean);
                        }
                        //退款操作
                        if (user.getId() == 8) {
                            if (refundAmount <= 500) {
                                json = refundByUserId(detailsBean, type, user.getAdmName());
                            } else {
                                json.setOk(true);
                                json.setMessage("金额大于500，请Emma继续操作");
                            }
                        } else if (user.getId() == 83) {
                            json = refundByUserId(detailsBean, type, user.getAdmName());
                        }
                    } else if (actionFlag == 4) {
                        json = refundByUserId(detailsBean, type, user.getAdmName());
                    }
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


    private JsonResult refundByUserId(RefundDetailsBean detailsBean, int type, String adminName) {

        JsonResult json = new JsonResult();
        try {
            json = ppApiService.reFundNew(detailsBean.getOrderNo(), decimalFormat.format(detailsBean.getRefundAmount()));
            if (json.isOk()) {
                //如果是PayPal申请退款，则自动添加一条余额补偿记录(非余额补偿)
                if (type == 1) {
                    additionalBalanceService.insert(new BalanceBean(detailsBean.getUserId(), detailsBean.getRefundAmount(),
                            "System Add:客户PayPal申请退款完结后,自动生成的数据", adminName,
                            "refundId_" + detailsBean.getRefundId(), detailsBean.getOrderNo()));
                }
            } else {
                logger.error("userId:" + detailsBean.getUserId() + ",rdId:" + detailsBean.getRefundId() + ",refundByPayPalApi error :" + json.getMessage());
                System.err.println("userId:" + detailsBean.getUserId() + ",rdId:" + detailsBean.getRefundId() + ",refundByPayPalApi error :" + json.getMessage());
                json.setOk(false);
                json.setMessage("退款失败,原因:[" + json.getMessage() + "],状态已经更新到“财务确认”,请EMMA继续操作或者联系IT人员查看");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("userId:" + detailsBean.getUserId() + ",rdId:" + detailsBean.getRefundId() + ",refundByPayPalApi error :" + e.getMessage());
            System.err.println("userId:" + detailsBean.getUserId() + ",rdId:" + detailsBean.getRefundId() + ",refundByPayPalApi error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("退款失败,状态已经更新到待执行,请EMMA继续操作或者联系IT人员查看");
        }
        //成功后修改退款到完结状态
        if (json.isOk()) {
            detailsBean.setRefundState(4);
            detailsBean.setRemark(detailsBean.getRemark() + ",执行API退款成功！<br>退款交易号[" + json.getMessage() + "]");
            refundNewService.setAndRemark(detailsBean);
            //使用MQ更新线上状态
            updateOnlineRefundState(detailsBean);
        }
        return json;
    }

    /**
     * 拒绝退款
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/rejectRefund", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult rejectRefund(HttpServletRequest request, HttpServletResponse response) {

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
            String refundIdStr = request.getParameter("refundId");
            int refundId = 0;
            if (StringUtils.isBlank(refundIdStr)) {
                json.setOk(false);
                json.setMessage("获取id失败,请重试");
                return json;
            } else {
                refundId = Integer.valueOf(refundIdStr);
            }
            String actionFlagStr = request.getParameter("actionFlag");
            int actionFlag = 0;
            if (StringUtils.isBlank(actionFlagStr)) {
                json.setOk(false);
                json.setMessage("获取操作状态失败,请重试");
                return json;
            } else {
                actionFlag = Integer.valueOf(actionFlagStr);
            }

            String userIdStr = request.getParameter("userId");
            int userId = 0;
            if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
                json.setOk(false);
                json.setMessage("获取客户ID失败,请重试");
                return json;
            } else {
                userId = Integer.valueOf(userIdStr);
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

            String remark = request.getParameter("remark");
            if (StringUtils.isBlank(remark)) {
                json.setOk(false);
                json.setMessage("获取备注信息失败,请重试");
                return json;
            }

            String orderNo = request.getParameter("orderNo");

            RefundDetailsBean detailsBean = new RefundDetailsBean();
            detailsBean.setRefundId(refundId);
            detailsBean.setAdminId(user.getId());
            detailsBean.setOrderNo(orderNo == null ? "" : orderNo);
            detailsBean.setRefundState(actionFlag);
            detailsBean.setRefundAmount(0);
            detailsBean.setRemark(remark);
            detailsBean.setUserId(userId);
            refundNewService.setAndRemark(detailsBean);
            //如果驳回客户申请的是余额提现，则退给客户余额
            RefundNewBean refundNewBean = refundNewService.queryRefundById(refundId);
            if(refundNewBean.getType() == 0){
                UserDao userDao = new UserDaoImpl();
                userDao.updateUserAvailable(refundNewBean.getUserId(), (float) refundNewBean.getAppliedAmount(),
                        "拒绝提现请求，回款,refundId:" + refundNewBean.getId() + ",amount:" + refundNewBean.getAppliedAmount(),
                        "refundId:" + refundNewBean.getId(), user.getAdmName(), 0, 0, 4);
            }
            //使用MQ更新线上状态
            updateOnlineRefundState(detailsBean);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("rejectRefund error,reason: " + e.getMessage());
            json.setOk(false);
            json.setMessage("执行失败,原因：" + e.getMessage());
        }
        return json;
    }


    /**
     * 已经线下退款
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
            String refundIdStr = request.getParameter("refundId");
            int refundId = 0;
            if (StringUtils.isBlank(refundIdStr)) {
                json.setOk(false);
                json.setMessage("获取id失败,请重试");
                return json;
            } else {
                refundId = Integer.valueOf(refundIdStr);
            }


            String typeStr = request.getParameter("type");
            int type = 0;
            if (StringUtils.isBlank(typeStr)) {
                json.setOk(false);
                json.setMessage("获取申请类型失败,请重试");
                return json;
            } else {
                type = Integer.valueOf(typeStr);
            }


            String actionFlagStr = request.getParameter("actionFlag");
            int actionFlag = 0;
            if (StringUtils.isBlank(actionFlagStr)) {
                json.setOk(false);
                json.setMessage("获取操作状态失败,请重试");
                return json;
            } else {
                actionFlag = Integer.valueOf(actionFlagStr);
            }

            String userIdStr = request.getParameter("userId");
            int userId = 0;
            if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
                json.setOk(false);
                json.setMessage("获取客户ID失败,请重试");
                return json;
            } else {
                userId = Integer.valueOf(userIdStr);
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

            String remark = request.getParameter("remark");
            if (StringUtils.isBlank(remark)) {
                json.setOk(false);
                json.setMessage("获取备注信息失败,请重试");
                return json;
            }
            String orderNo = request.getParameter("orderNo");

            RefundDetailsBean detailsBean = new RefundDetailsBean();
            detailsBean.setRefundId(refundId);
            detailsBean.setAdminId(user.getId());
            detailsBean.setOrderNo(orderNo == null ? "" : orderNo);
            detailsBean.setRefundState(actionFlag);
            detailsBean.setRefundAmount(0);
            detailsBean.setRemark(remark);
            detailsBean.setUserId(userId);
            refundNewService.setAndRemark(detailsBean);
            //使用MQ更新线上状态
            updateOnlineRefundState(detailsBean);

            detailsBean.setRefundState(4);
            detailsBean.setRemark("已经确认线下退款，退款自动完结");
            refundNewService.setAndRemark(detailsBean);
            //使用MQ更新线上状态
            updateOnlineRefundState(detailsBean);

            //如果是PayPal申请退款，则自动添加一条余额补偿记录(非余额补偿)
            if (type == 1) {
                additionalBalanceService.insert(new BalanceBean(detailsBean.getUserId(), detailsBean.getRefundAmount(),
                        "System Add:客户PayPal申请退款完结后,自动生成的数据", user.getAdmName(),
                        "refundId_" + detailsBean.getRefundId(), detailsBean.getOrderNo()));
            }

            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("offLineRefund error,reason: " + e.getMessage());
            json.setOk(false);
            json.setMessage("执行失败,原因：" + e.getMessage());
        }
        return json;
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
            Map<String, String> orderNosMap = new HashMap<String, String>();
            //获取PayPal支付订单金额
            List<PaymentBean> paymentList = refundNewService.queryPaymentInfoByUserId(userId, 3);
            if (!(paymentList == null || paymentList.isEmpty())) {
                //获取已经在退款流程中的订单金额
                List<RefundNewBean> refundList = refundNewService.queryRefundByUserId(userId);
                //循环获取可用订单号
                for (PaymentBean pmb : paymentList) {
                    if (StringUtils.isNotBlank(pmb.getOrderid())) {
                        double tempRefundAmount = 0;
                        if (!(refundList == null || refundList.isEmpty())) {
                            for (RefundNewBean rfb : refundList) {
                                if (pmb.getOrderid().equals(rfb.getOrderNo()) && rfb.getState() != 9) {
                                    tempRefundAmount += rfb.getAgreeAmount();
                                }
                            }
                        }
                        if (Double.valueOf(pmb.getPayment_amount()) - refundAmount - tempRefundAmount >= 0) {
                            if (!orderNosMap.containsKey(pmb.getOrderid()) && orderNosMap.size() < 6) {
                                orderNosMap.put(pmb.getOrderid(), pmb.getOrderid() + "(remain:"
                                        + BigDecimalUtil.truncateDouble((Double.valueOf(pmb.getPayment_amount()) - tempRefundAmount), 2) + ")");
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
            logger.error("userId:" + userIdStr + ",findCanRefundOrderNo error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("userId:" + userIdStr + ",findCanRefundOrderNo error :" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/queryRefundDetails")
    @ResponseBody
    public ModelAndView queryRefundDetails(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("refundDetails");
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            mv.addObject("success", 0);
            mv.addObject("message", "请登录后操作");
            return mv;
        }

        //获取申诉id
        String refundIdStr = request.getParameter("refundId");
        int refundId = 0;
        if (StringUtils.isBlank(refundIdStr)) {
            mv.addObject("success", 0);
            mv.addObject("message", "获取退款ID失败,请重试");
            return mv;
        } else {
            refundId = Integer.valueOf(refundIdStr);
        }
        try {
            RefundNewBean refundNewBean = refundNewService.queryRefundById(refundId);
            List<RefundDetailsBean> refundDetailsList = refundNewService.queryRefundDetailsByRefundId(refundId);
            mv.addObject("success", 1);
            mv.addObject("refundBean", refundNewBean);
            mv.addObject("refundDetails", refundDetailsList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("refundId：" + refundId + ",queryRefundDetails，原因 :" + e.getMessage());
            mv.addObject("success", 0);
            mv.addObject("message", "refundId：" + refundId + ",queryRefundDetails，原因:" + e.getMessage());
        }
        return mv;
    }


    @RequestMapping("/queryOrderDeal")
    @ResponseBody
    public ModelAndView queryOrderDeal(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("refundOrderDeal");
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            mv.addObject("success", 0);
            mv.addObject("message", "请登录后操作");
            return mv;
        }

        String userIdStr = request.getParameter("userId");
        int userId = 0;
        if (StringUtils.isBlank(userIdStr)) {
            mv.addObject("success", 0);
            mv.addObject("message", "获取用户ID失败");
            return mv;
        } else {
            userId = Integer.valueOf(userIdStr);
        }
        String orderNo = request.getParameter("orderNo");
        if (StringUtils.isBlank(orderNo)) {
            mv.addObject("success", 0);
            mv.addObject("message", "获取订单号失败");
            return mv;
        }
        try {
            List<OrderBean> orderList = refundNewService.queryOrderInfoByOrderNoOrUserId(userId, orderNo);
            mv.addObject("orderList", orderList);
            List<PaymentDetails> paymentList = refundNewService.queryPaymentInfoByOrderNoOrUserId(userId, orderNo);
            mv.addObject("paymentList", paymentList);
            mv.addObject("success", 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("userId：" + userId + ",orderNo：" + orderNo + ",queryOrderDeal，原因 :" + e.getMessage());
            mv.addObject("success", 0);
            mv.addObject("message", "userId：" + userId + ",orderNo：" + orderNo + ",queryOrderDeal，原因 :" + e.getMessage());
        }
        return mv;
    }


    /**
     * 使用MQ更新线上状态
     *
     * @param detailsBean
     */
    private void updateOnlineRefundState(RefundDetailsBean detailsBean) {
        //更新线上申请状态
        StringBuffer sbf = new StringBuffer("update refund set status =" + detailsBean.getRefundState());
        sbf.append(",agreetime =sysdate()");
        if (detailsBean.getRefundState() == 4 || detailsBean.getRefundState() == 9) {
            sbf.append(",endtime = sysdate()");
        } else {
            sbf.append(",account = " + detailsBean.getRefundAmount() + ",orderid = '" + detailsBean.getOrderNo() + "'");
        }
        sbf.append(" where id = " + detailsBean.getRefundId() + " and userid = " + detailsBean.getUserId());
        NotifyToCustomerUtil.sendSqlByMq(sbf.toString());
    }

    @RequestMapping("/refundByPayNo")
    @ResponseBody
    private JsonResult refundByPayNo(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String adminIdStr = request.getParameter("adminId");
        int adminId = 0;
        if (StringUtils.isBlank(adminIdStr)) {
            json.setOk(false);
            json.setMessage("获取用户ID失败");
            return json;
        } else {
            adminId = Integer.valueOf(adminIdStr);
            if (adminId == 0 || adminId != user.getId()) {
                json.setOk(false);
                json.setMessage("选择用户和登录用户不一致");
                return json;
            }
        }

        String payNo = request.getParameter("payNo");
        if (StringUtils.isBlank(payNo)) {
            json.setOk(false);
            json.setMessage("获取交易号失败");
            return json;
        }
        String payType = request.getParameter("payType");
        if (StringUtils.isBlank(payType)) {
            json.setOk(false);
            json.setMessage("获取支付类别失败");
            return json;
        }
        String validPassWord = request.getParameter("validPassWord");
        if (StringUtils.isBlank(validPassWord)) {
            json.setOk(false);
            json.setMessage("获取验证密码失败");
            return json;
        }
        String remark = request.getParameter("remark");
        if (StringUtils.isBlank(remark)) {
            json.setOk(false);
            json.setMessage("获取备注失败");
            return json;
        }
        String refundAmount = request.getParameter("refundAmount");
        if (StringUtils.isBlank(refundAmount)) {
            json.setOk(false);
            json.setMessage("获取退款金额失败");
            return json;
        }
        try {
            json = ppApiService.refundByPayNo(payNo, payType, refundAmount, remark, adminId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("payNo:" + payNo + ",payType:" + payType + ",refundByPayNo error :" + e.getMessage());
            System.err.println("payNo:" + payNo + ",payType:" + payType + ",refundByPayNo error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("退款失败,原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/queryForRefundResultList")
    @ResponseBody
    public EasyUiJsonResult queryForRefundResultList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }

        RefundResultInfo queryPm = new RefundResultInfo();
        int startNum = 0;
        int limitNum = 50;
        String rowsStr = request.getParameter("rows");
        if (StringUtil.isNotBlank(rowsStr)) {
            limitNum = Integer.valueOf(rowsStr);
        }

        String pageStr = request.getParameter("page");
        if (!(StringUtil.isBlank(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String orderNo = request.getParameter("orderNo");
        if (StringUtil.isNotBlank(orderNo)) {
            queryPm.setOrderno(orderNo);
        }
        String userId = request.getParameter("userId");
        if (StringUtil.isNotBlank(userId)) {
            queryPm.setUserid(userId);
        }
        String refundId = request.getParameter("refundId");
        if (StringUtil.isNotBlank(refundId)) {
            queryPm.setRefundid(refundId);
        }
        String saleId = request.getParameter("saleId");
        if (StringUtil.isNotBlank(saleId)) {
            queryPm.setSaleId(saleId);
        }


        try {
            queryPm.setLimitNum(limitNum);
            queryPm.setStartNum(startNum);
            List<RefundResultInfo> res = refundNewService.queryForRefundResultList(queryPm);


            int count = refundNewService.queryForRefundResultListCount(queryPm);
            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }

}
