package com.cbt.warehouse.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Inventory;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.*;
import com.cbt.warehouse.pojo.SampleOrderBean;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.warehouse.service.InventoryService;
import com.cbt.website.dao.IOrderSplitDao;
import com.cbt.website.dao.OrderInfoDao;
import com.cbt.website.dao.OrderInfoImpl;
import com.cbt.website.dao.OrderSplitDaoImpl;
import com.cbt.website.service.IOrderSplitServer;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderSplitServer;
import com.cbt.website.service.OrderwsServer;
import com.cbt.website.util.JsonResult;
import com.google.gson.Gson;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.pojo.OrderCancelApproval;
import com.importExpress.pojo.SplitGoodsNumBean;
import com.importExpress.service.OrderSplitRecordService;
import com.importExpress.service.OverseasWarehouseStockService;
import com.importExpress.utli.MultiSiteUtil;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.SwitchDomainNameUtil;
import com.importExpress.utli.UserInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/orderSplit")
public class NewOrderSplitCtr {
    @Autowired
    private Send send;
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(NewOrderSplitCtr.class);
    @Autowired
    private SendMailFactory sendMailFactory;
    @Autowired
    private IOrderinfoService iOrderinfoService;
    @Autowired
    private OrderSplitRecordService orderSplitRecordService;
    @Autowired
    private IWarehouseService iWarehouseService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OverseasWarehouseStockService owsService;
    @Autowired
    private CustomGoodsService customGoodsService;

    /**
     * ????????????(???????????????Drop Ship??????)
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/doSplit.do")
    @ResponseBody
    public JsonResult doSplit(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("ordersplit start");
        JsonResult json = new JsonResult();
        try {
            String orderno = request.getParameter("orderno");
            String odids = request.getParameter("odids");
            String state = request.getParameter("state");// 0??????1????????????
            if (state == null || "".equals(state)) {
                json.setOk(false);
                json.setMessage("????????????????????????");
            }

            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            if (admuserJson == null) {
                json.setOk(false);
                json.setMessage("???????????????");
                return json;
            } else {
                Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
                // ???????????????Drop Ship??????????????????????????????????????????
                OrderInfoDao orderInfoDao = new OrderInfoImpl();
                OrderBean orderBean = orderInfoDao.getOrderInfo(orderno, null);
                IOrderSplitServer splitServer = new OrderSplitServer();
                if (orderBean.getIsDropshipOrder() == 1) {
                    // Drop Ship ????????????
                    Map<String, String> map = splitServer.splitOrderShip(orderno, odids, orderBean.getUserid(), state);
                    if ("1".equals(map.get("res"))) {
                        json.setOk(true);
                        json.setData(map.get("orderNoNew"));
                    } else {
                        json.setOk(false);
                        json.setMessage(map.get("msg"));
                    }
                } else {
                    // ??????????????????
                    // json = splitCommonOrder(orderno, odids, state);
                    json = newSplitCommonOrder(admuser, orderno, odids, state, 0);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("orderSplit error:", e);
            json.setOk(false);
            json.setMessage("orderSplit error:" + e.getMessage());
        }
        // LOG.info("ordersplit end");
        return json;
    }
    @RequestMapping(value = "/DownSample")
    @ResponseBody
    public JsonResult DownSample(HttpServletRequest request, HttpServletResponse response,@RequestBody List<OrderDetailsBean> odls) {
        JsonResult json = new JsonResult();
        for (OrderDetailsBean old:odls){
            System.out.println(old);
        }
        return json;
    }
    @RequestMapping(value = "/deliverOrder")
    public String deliverOrder(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "page",defaultValue = "0")int page,@RequestParam(value = "pagesize",defaultValue = "30")int pagesize, Model model) {
        String pid=request.getParameter("pid");
        String orderno=request.getParameter("orderno");
        String userid=request.getParameter("userid");
        if (StringUtils.isBlank(pid)){
            pid=null;
        } if (StringUtils.isBlank(orderno)){
            return "samplelibrary";
        }
       Gson gson=new Gson();
        List<Inventory> odls=new ArrayList<>();
        odls=this.iWarehouseService.FindAllGoods(page,pagesize,pid);
        String s2 = gson.toJson(odls);
        model.addAttribute("orderDetail",odls);
        request.setAttribute("odls",s2);
        request.setAttribute("orderno",orderno);
        request.setAttribute("userid",userid);
        return "samplelibrary";
    }
    @RequestMapping(value = "/saveNewOrder")
    @ResponseBody
    public int SaveNewOrder(HttpServletRequest request, HttpServletResponse response,@RequestBody List<SampleOrderBean> list ) {
        if (list.size()>0) {
            try {
                boolean bo=this.iOrderinfoService.setSampleGoodsIsOrder(list.get(0).getOrderNo(), list.get(0).getUserId(), list);
//                send.deliverMqSend(list);
                if (!bo){
                    return 0;
                }
                 bo=this.iWarehouseService.setInventoryCountBySkuAndPid(list);
                 bo=this.iWarehouseService.addprocurement(list);
                return 1;
            }catch (Exception e){
                return 0;
            }

        }
        return 0;
    }

    @SuppressWarnings("finally")
    private JsonResult splitCommonOrder(String orderNo, String odids, String state) {
        JsonResult json = new JsonResult();
        IOrderSplitDao dao = new OrderSplitDaoImpl();
        String nwOrderNo = "";
        
        /*
         * 1.?????? ??? ??????????????? ?????? ??? ?????? ???????????? ?????????????????????????????? ????????? 2.????????????????????????????????????????????? 3.?????????????????????
         * ??????????????????????????? ??????????????????????????? ??? ???????????? ???????????? = ???* ???*??? (??????)/5000 ??? ?????????????????? ??? ??????
         * 4.??????????????????????????????????????? ??? -split ????????????????????????????????? 5.????????????????????????????????? ???????????????????????? 6.50?????????
         * ??????credit?????????
         */
        try {
            // ???????????????????????????
            OrderBean orderBean = dao.getOrders(orderNo);
            String[] odidLst = odids.split("@");// ?????????????????????order_details???id
            // ???????????????????????????
            String info_log = ", product_cost=" + orderBean.getProduct_cost() + ",pay_price=" + orderBean.getPay_price()
                    + ",pay_price_tow=" + orderBean.getPay_price_tow() + ",pay_price_three="
                    + orderBean.getPay_price_three() + ",actual_ffreight=" + orderBean.getActual_ffreight()
                    + ",service_fee=" + orderBean.getService_fee() + ",order_ac=" + orderBean.getOrder_ac()
                    + ",discount_amount=" + orderBean.getDiscount_amount() + ",cashback=" + orderBean.getCashback()
                    + ",extra_freight=" + orderBean.getExtra_freight() + ",share_discount="
                    + orderBean.getShare_discount() + ",Coupon_discount=" + orderBean.getCoupon_discount()
                    + ",extra_discount=" + orderBean.getExtra_discount() + "state=" + orderBean.getState();// --cjc

            LOG.info("ordersplit info_log:" + info_log);

            dao.addOrderInfoLog(orderBean.getOrderNo(), info_log);
            List<OrderDetailsBean> orderDetails = dao.getOrdersDetails_split(orderNo);

            List<OrderDetailsBean> nwOrderDetails = new ArrayList<OrderDetailsBean>();
            List<Integer> goodsIds = new ArrayList<Integer>();
            // ???????????????????????????????????????ids
            if (odidLst.length > 0) {
                for (OrderDetailsBean odds : orderDetails) {
                    for (String odid : odidLst) {
                        if (odds.getId() == Integer.valueOf(odid)) {
                            nwOrderDetails.add(odds);
                            goodsIds.add(odds.getGoodsid());
                            break;
                        }
                    }
                }

                if (nwOrderDetails.size() > 0) {
                    // ??????????????????????????????
                    // ??????????????????????????????????????????
                    nwOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 0, 0);

                    // ??????????????????
                    boolean isOk = true;
                    StringBuffer ocBf = new StringBuffer();
                    for (OrderDetailsBean nwOdDt : nwOrderDetails) {
                        if (dao.orderSplitByOrderNo(orderBean.getUserid(), orderNo, nwOrderNo, nwOdDt.getGoodsid(),
                                nwOdDt.getYourorder())) {
                            ocBf.append("," + nwOdDt.getGoodsid());
                            continue;
                        } else {
                            isOk = false;
                            break;
                        }
                    }

                    if (!isOk) {
                        json.setOk(false);
                        json.setMessage("???????????????????????????????????????" + (ocBf.length() > 0 ? ocBf.toString().substring(1) : ""));
                    } else {
                        // ???????????????????????????,?????????,???????????????????????????
                        if ("0".equals(state)) {
                            // ??????????????????:??????????????????=?????????payprice;??????????????????????????????;
                            // ?????????????????????????????????recharge_record;??????????????????payment
                            dao.cancelNewOrder(orderBean.getUserid(), nwOrderNo);
                        } else {
                            // ????????????????????????
                            dao.insertIntoPayment(orderBean.getUserid(), nwOrderNo, orderNo);
                        }
                        // ?????????????????????
                        dao.checkAndUpdateOrderState(orderNo, nwOrderNo);
                        // ????????????????????????????????????,??????
                        if (goodsIds.size() > 0) {
                            dao.updateWarehouseInfo(orderNo, nwOrderNo, goodsIds);
                        }

                        json.setOk(true);
                        json.setMessage("????????????");
                        json.setData(nwOrderNo);
                        if ("0".equals(state)) {
                            // ??????????????????????????????????????????????????????????????????
                        	inventoryService.cancelToInventory(odidLst, 0, "");
//                            dao.cancelInventory(odidLst);
                        }
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("????????????????????????????????????");
                }

            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("OrderSplitServer-Exception:", e);
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        } finally {
            return json;
        }
    }

    @SuppressWarnings("finally")
    private JsonResult newSplitCommonOrder(Admuser admuser, String orderNo, String odids, String state, int isOverSea) {

        // 1.???????????????????????????????????????
        // 2.????????????????????????????????????
        // 3.???????????????????????????????????????????????????????????????????????????????????????????????????
        // 4.??????????????????
        // 5.?????????????????????????????????????????????????????????
        // 6.??????????????????????????????????????????????????????????????????????????????

        JsonResult json = new JsonResult();
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        String nwOrderNo = "";
        try {

            // ???????????????????????????
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if(orderBean == null || StringUtils.isBlank(orderBean.getOrderNo())){
                json.setOk(false);
                json.setMessage("??????????????????????????????");
                return json;
            }
            // ?????????????????????
            nwOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 0, isOverSea);
            // ?????????????????????order_details???id
            String[] odidLst = odids.split("@");
            // 1.???????????????????????????????????? log
            boolean isSuccess = saveOrderInfoBeforeSplitOrder(orderBean, orderNo, odids, admuser);
            if (isSuccess) {
                // 2.????????????????????????????????????
                OrderBean orderBeanTemp = (OrderBean) orderBean.clone();
                // ?????????????????????????????????
                List<OrderDetailsBean> orderDetails = splitDao.getOrdersDetails_split(orderNo);
                List<OrderDetailsBean> nwOrderDetails = new ArrayList<OrderDetailsBean>();
                List<Integer> goodsIds = new ArrayList<Integer>();
                List<Integer> odIds = new ArrayList<>();
                // ???????????????????????????????????????ids
                if (odidLst.length > 0) {
                    // ?????????????????????
                    double totalPayPriceOld = orderBean.getPay_price();
                    // ?????????????????????
                    double totalGoodsCostOld = 0;
                    double totalGoodsWeightOld = 0;//
                    for (OrderDetailsBean odds : orderDetails) {
                        for (String odid : odidLst) {
                            if (odds.getId() == Integer.valueOf(odid)) {
                                nwOrderDetails.add(odds);
                                goodsIds.add(odds.getGoodsid());
                                odIds.add(Integer.valueOf(odid));
                                break;
                            }
                        }
                        totalGoodsCostOld += Double.valueOf(odds.getGoodsprice()) * odds.getYourorder();
                        totalGoodsWeightOld += Double.valueOf(odds.getActual_weight());
                    }
                    //??????????????????
                    /*OrderSplitMain orderMain = new OrderSplitMain();
                    orderMain.setCost(totalGoodsCostOld);
                    orderMain.setFeight(orderBean.getActual_weight_estimate());
                    orderMain.setOrderid(orderNo);
                    orderMain.setWeight(totalGoodsWeightOld);
                    String mode_transport = orderBean.getMode_transport();
                    String modeTransport = mode_transport.split("@")[0];
                    orderMain.setModeTransport(modeTransport);
                    if(orderNo.indexOf("_") == -1) {
                    	orderMain.setCountryName(mode_transport.split("@")[2]);
                		orderSplitRecordService.insertMainOrder(orderMain);
                	}*/

                    // ???????????????odids??????
                    if (nwOrderDetails.size() == odidLst.length && !(totalPayPriceOld <= 0 || totalGoodsCostOld <= 0)) {
                        // 3.??????????????????????????????????????????
                        calculateExpectedResult(json, nwOrderDetails, orderNo, nwOrderNo, orderBean, totalGoodsCostOld,
                                totalPayPriceOld, orderBeanTemp, admuser, state, odidLst, goodsIds, odIds);
                        //????????????????????????????????????
                        //????????????????????????????????????????????????????????????
//						boolean flag=splitDao.checkTestOrder(odidLst[0]);
//						if(flag){}
                        /* if ("0".equals(state)) {
                        	inventoryService.cancelToInventory(odidLst, admuser.getId(), admuser.getAdmName());
                           for (String odid : odidLst) {
                                splitDao.addInventory(odid, "??????????????????");
                            }
                        }*/
                    } else {
                        json.setOk(false);
                        if (nwOrderDetails.size() == 0 || nwOrderDetails.size() != odidLst.length) {
                            json.setMessage("????????????????????????????????????");
                        } else if (totalPayPriceOld <= 0) {
                            json.setMessage("????????????????????????????????????????????????0");
                        } else if (totalGoodsCostOld <= 0) {
                            json.setMessage("??????????????????????????????????????????0");
                        }
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("?????????????????????????????????");
                }
            } else {
                json.setOk(false);
                json.setMessage("??????????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("OrderSplitServer-Exception:", e);
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        } finally {
            return json;
        }
    }

    /**
     * ????????????????????????????????????
     */
    private boolean saveOrderInfoBeforeSplitOrder(OrderBean orderBean, String orderNo, String odids, Admuser admuser) {
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        // ???????????????????????????
        String info_log = ", product_cost=" + orderBean.getProduct_cost() + ",pay_price=" + orderBean.getPay_price()
                + ",pay_price_tow=" + orderBean.getPay_price_tow() + ",pay_price_three="
                + orderBean.getPay_price_three() + ",actual_ffreight=" + orderBean.getActual_ffreight()
                + ",service_fee=" + orderBean.getService_fee() + ",order_ac=" + orderBean.getOrder_ac()
                + ",discount_amount=" + orderBean.getDiscount_amount() + ",cashback=" + orderBean.getCashback()
                + ",extra_freight=" + orderBean.getExtra_freight() + ",share_discount=" + orderBean.getShare_discount()
                + ",Coupon_discount=" + orderBean.getCoupon_discount() + ",extra_discount="
                + orderBean.getExtra_discount() + "state=" + orderBean.getState()
                + "vatBalance = " + orderBean.getVatBalance(); // --cjc
        // 1.???????????????????????????????????????
        LOG.info("ordersplit orderNo:" + orderNo + "; info_log:[" + info_log + "];");
        LOG.info("ordersplit odids:[" + odids + "];");
        info_log = "";
        return splitDao.addOrderInfoAndPaymentLog(orderBean.getOrderNo(), admuser, 0);
    }

    /**
     * ???????????????????????????
     */
    private void calculateExpectedResult(JsonResult json, List<OrderDetailsBean> nwOrderDetails, String orderNo,
                                         String nwOrderNo, OrderBean orderBean, double totalGoodsCostOld, double totalPayPriceOld,
                                         OrderBean orderBeanTemp, Admuser admuser, String state, String[] odidLst,
                                         List<Integer> goodsIds, List<Integer> odIds) {
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        // 3.???????????????????????????????????????????????????????????????????????????????????????????????????
        // ????????????????????????
        double totalGoodsCostNew = 0;
        for (OrderDetailsBean nwOdDt : nwOrderDetails) {
            totalGoodsCostNew += Double.parseDouble(nwOdDt.getGoodsprice()) * nwOdDt.getYourorder();
        }
        if (totalGoodsCostNew > 0) {

            // ????????????
            double splitRatio = totalGoodsCostNew / totalGoodsCostOld;
            OrderBean odbeanNew = OrderInfoUtil.genNewOrderInfo(orderBean, orderBeanTemp, splitRatio, nwOrderNo,
                    totalGoodsCostOld, nwOrderDetails);

            // ????????????????????????
            double totalPayPriceNew = totalPayPriceOld * splitRatio;
            // 3.???????????????????????????????????????????????????????????????????????????????????????????????????(??????????????????)
            List<OrderBean> orderBeans = new ArrayList<OrderBean>();
            orderBeans.add(odbeanNew);
            orderBeans.add(orderBeanTemp);
            boolean success = splitDao.saveOrderInfoLogByList(orderBeans, admuser);
            if (success) {
                // ??????????????????
                doSplitOrderAction(json, nwOrderDetails, orderNo, nwOrderNo, orderBeanTemp, odbeanNew, admuser, state,
                        odidLst, goodsIds, (float) totalPayPriceNew,  odIds);
            } else {
                json.setOk(false);
                json.setMessage("?????????????????????????????????????????????");
            }
        } else {
            json.setOk(false);
            json.setMessage("??????????????????????????????????????????0");
        }
    }

    /**
     * ?????????????????????
     */
    private void doSplitOrderAction(JsonResult json, List<OrderDetailsBean> nwOrderDetails, String orderNo,
                                    String nwOrderNo, OrderBean orderBeanTemp, OrderBean odbeanNew, Admuser admuser, String state,
                                    String[] odidLst, List<Integer> goodsIds, float totalPayPriceNew, List<Integer> odIds) {

        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        // 4.??????????????????
        // ??????????????????
        boolean isOk = splitDao.newOrderSplitFun(orderBeanTemp, odbeanNew, nwOrderDetails, state, 0);
        if (!isOk) {
            json.setOk(false);
            json.setMessage("?????????????????????");
        } else {

            // ?????????????????????
            splitDao.checkAndUpdateOrderState(orderNo, nwOrderNo);
            // ????????????????????????????????????,??????
            if (goodsIds.size() > 0) {
                splitDao.updateWarehouseInfo(orderNo, nwOrderNo, goodsIds);
                splitDao.updateGoodsCommunicationInfo(orderNo, nwOrderNo, odIds);
                odIds.clear();
            }

            // ???????????????????????????,?????????,???????????????????????????
            if ("0".equals(state)) {
                // ??????????????????:??????????????????=?????????payprice;??????????????????????????????;
                // ?????????????????????????????????recharge_record;??????????????????payment
                splitDao.cancelNewOrder(orderBeanTemp.getUserid(), nwOrderNo);
                // ??????????????????????????????????????????????????????????????????
                inventoryService.cancelToInventory(odidLst, admuser.getId(), admuser.getAdmName());
//                splitDao.cancelInventory(odidLst);
                // 5.??????????????????????????????????????????????????????????????????????????????
                /*ChangUserBalanceDao balanceDao = new ChangUserBalanceDaoImpl();
                balanceDao.changeBalance(orderBeanTemp.getUserid(), genFloatWidthTwoDecimalPlaces(totalPayPriceNew), 1,
                        1, nwOrderNo, "", admuser.getId());*/

                IOrderwsServer orderwsServer = new OrderwsServer();
                int oiState = orderwsServer.checkOrderState(orderNo, "0");
                OrderCancelApproval cancelApproval = new OrderCancelApproval();
                cancelApproval.setUserId(orderBeanTemp.getUserid());
                cancelApproval.setOrderNo(nwOrderNo);
                cancelApproval.setPayPrice(totalPayPriceNew);
                cancelApproval.setType(2);
                cancelApproval.setDealState(0);
                cancelApproval.setOrderState(oiState);
                NotifyToCustomerUtil.insertIntoOrderCancelApproval(cancelApproval);
            }else {
            	// orderSplitRecordService.insertChildOrder(orderMain,nwOrderNo);
            }
            // 6.?????????????????????????????????????????????????????????
            splitDao.addOrderInfoAndPaymentLog(nwOrderNo, admuser, 1);

            goodsIds.clear();
            nwOrderDetails.clear();
            json.setOk(true);
            json.setMessage("????????????");
            json.setData(nwOrderNo);
        }
    }


    @RequestMapping(value = "/splitGoodsByNum")
    @ResponseBody
    public JsonResult splitGoodsByNum(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        String orderNo = request.getParameter("orderNo");
        String odIds = request.getParameter("odIds");
        try {

            if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(odIds)) {
                json.setOk(false);
                json.setMessage("????????????????????????");
                return json;
            } else if (orderNo.contains("SN")) {
                json.setOk(false);
                json.setMessage("??????????????????????????????????????????");
                return json;
            }

            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("??????????????????");
                return json;
            }

            // ???????????????Drop Ship??????????????????????????????????????????
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if(orderBean == null || StringUtils.isBlank(orderBean.getOrderNo())){
                json.setOk(false);
                json.setMessage("????????????????????????!");
                return json;
            }
            if (orderBean.getIsDropshipOrder() == 1) {
                // Drop Ship ???????????????????????????
                json.setOk(false);
                json.setMessage("DropShip???????????????????????????");
                return json;
            }

            List<SplitGoodsNumBean> splitIdList = (List<SplitGoodsNumBean>) JSONArray.parseArray(odIds, SplitGoodsNumBean.class);
            if (splitIdList == null || splitIdList.isEmpty()) {
                json.setOk(false);
                json.setMessage("????????????????????????");
                return json;
            }
            OrderBean orderInfo = iOrderinfoService.getOrders(orderNo);
            if (orderInfo.getState() != 5 && orderInfo.getState() != 1 && orderInfo.getState() != 2) {
                json.setOk(false);
                json.setMessage("???????????????????????????????????????????????????????????????");
                return json;
            }

            // 1.??????????????????,?????????????????????
            List<OrderDetailsBean> odbList = iOrderinfoService.getOrdersDetails(orderNo);
            List<OrderDetailsBean> nwOrderDetails = new ArrayList<>(splitIdList.size());
            // ????????????OrderDetailsBean??????
            Map<Integer, OrderDetailsBean> oldOrderDeatisMap = new HashMap<>(odbList.size());

            double oldTotalGoodsCost = 0;
            // ??????????????????
            for (OrderDetailsBean orderDetail : odbList) {
                oldTotalGoodsCost += Double.valueOf(orderDetail.getGoodsprice()) * orderDetail.getYourorder();
            }

            double newTotalGoodsCost = 0;
            for (SplitGoodsNumBean goodsNumBean : splitIdList) {
                goodsNumBean.setAdminId(admuser.getId());
                goodsNumBean.setOrderNo(orderNo);
                for (OrderDetailsBean orderDetail : odbList) {
                    if (orderDetail.getId() == goodsNumBean.getOdId()) {
                        // ????????????????????????????????????????????????
                        goodsNumBean.setGoodsPrice(Double.valueOf(orderDetail.getGoodsprice()));
                        goodsNumBean.setOldNum(orderDetail.getYourorder());

                        newTotalGoodsCost += goodsNumBean.getGoodsPrice() * goodsNumBean.getNum();
                        // ??????????????????????????????
                        orderDetail.setYourorder(goodsNumBean.getOldNum() - goodsNumBean.getNum());
                        oldOrderDeatisMap.put(orderDetail.getGoodsid(), orderDetail);
                        //
                        OrderDetailsBean orderDetailTemp = (OrderDetailsBean) orderDetail.clone();
                        orderDetailTemp.setYourorder(goodsNumBean.getNum());
                        orderDetailTemp.setOldGoodsNum(goodsNumBean.getOldNum() - goodsNumBean.getNum());
                        nwOrderDetails.add(orderDetailTemp);
                        break;
                    }
                }
            }

            // ????????????
            iOrderinfoService.insertIntoOrderSplitNumLog(splitIdList);
            // ????????????????????????????????????????????????
            if (oldTotalGoodsCost == 0 || newTotalGoodsCost == 0) {
                json.setOk(false);
                json.setMessage("??????????????????????????????");
                return json;
            }
            double splitRatio = newTotalGoodsCost / oldTotalGoodsCost;
            // 2.??????????????????
            // ????????????????????????????????????
            OrderBean orderBeanTemp = (OrderBean) orderBean.clone();
            String newOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 1, 0);
            OrderBean newOrderBean = OrderInfoUtil.genNewOrderInfo(orderBean, orderBeanTemp, splitRatio, newOrderNo,
                    oldTotalGoodsCost, nwOrderDetails);

            // ????????????
            List<OrderBean> orderBeans = new ArrayList<>(2);
            orderBeans.add(orderBean);
            orderBeans.add(newOrderBean);
            orderBeans.add(orderBeanTemp);
            splitDao.saveOrderInfoLogByList(orderBeans, admuser);
            // 3.??????????????????
            boolean isOk = splitDao.newOrderSplitFun(orderBeanTemp, newOrderBean, nwOrderDetails,
                    OrderInfoConstantUtil.REVIEW,  1);
            if (isOk) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("?????????????????????");
            }
            odbList.clear();
            nwOrderDetails.clear();
            oldOrderDeatisMap.clear();

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("splitGoodsByNum error:", e);
            json.setOk(false);
            json.setMessage("orderNo:" + orderNo + ",splitGoodsByNum error:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/splitGoodsByOverSea")
    @ResponseBody
    public JsonResult splitGoodsByOverSea(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        String orderNo = request.getParameter("orderNo");
        String odIds = request.getParameter("odIds");
        try {

            if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(odIds)) {
                json.setOk(false);
                json.setMessage("????????????????????????");
                return json;
            }

            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("??????????????????");
                return json;
            }

            // ???????????????Drop Ship??????????????????????????????????????????
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if (orderBean.getIsDropshipOrder() == 1) {
                // Drop Ship ???????????????????????????
                json.setOk(false);
                json.setMessage("DropShip???????????????????????????");
                return json;
            }

            json = newSplitCommonOrder(admuser, orderNo,  odIds, "1", 1);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("splitGoodsByNum error:", e);
            json.setOk(false);
            json.setMessage("orderNo:" + orderNo + ",splitGoodsByNum error:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setOverSeaOrder")
    @ResponseBody
    public JsonResult setOverSeaOrder(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        String orderNo = request.getParameter("orderNo");
        try {

            if (StringUtils.isBlank(orderNo)) {
                json.setOk(false);
                json.setMessage("?????????????????????");
                return json;
            }

            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("??????????????????");
                return json;
            }

            List<OrderDetailsBean> odb=iOrderinfoService.getOrdersDetails(orderNo);
            boolean noOverSea = false;
            for(OrderDetailsBean o : odb){
                if(o.getIsOverseasWarehouseProduct() == 0){
                    noOverSea = true;
                    break;
                }
            }
            odb.clear();
            if(noOverSea){
                json.setOk(false);
                json.setMessage("???????????????????????????????????????????????????????????????");
                return json;
            }

            // ???????????????????????????
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if(orderBean == null || StringUtils.isBlank(orderBean.getOrderNo())){
                json.setOk(false);
                json.setMessage("??????????????????????????????");
                return json;
            }
            // ?????????????????????
            String nwOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 0, 1);

            int rs = iOrderinfoService.updateOrderNoToNewNo(orderNo, nwOrderNo);
            if(rs > 0){
                 json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("??????????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("setOverSeaOrder error:", e);
            json.setOk(false);
            json.setMessage("orderNo:" + orderNo + ",setOverSeaOrder error:" + e.getMessage());
        }
        return json;
    }


    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/genOrderSplitEmail.do")
    @ResponseBody
    public String genOrderSplitEmail(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("genOrderSplitEmail start sendEmailInfo");
        Map<String, Object> model = new HashMap<>();
        String message = "success!";
        String email = request.getParameter("email");
        if (StringUtils.isNotBlank(email)) {
            String regex = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(email);
            while (m.find()) {
                email = m.group();
            }
        }
        String orderNo = request.getParameter("orderno");
        String websiteType = request.getParameter("websiteType");
        // boolean isKidFlag = "2".equals(websiteType);
        int isWebSiteFlag = MultiSiteUtil.getSiteTypeNum(orderNo);
        try {
            String webSiteTitle = "Import Express";
            TemplateType  webType= TemplateType.DISMANTLING_IMPORT;
            switch (isWebSiteFlag) {
                case 2:
                    webSiteTitle = "Kids Charming";
                    webType = TemplateType.DISMANTLING_KID;
                    break;
                case 3:
                    webSiteTitle = "Pet Store Inc";
                    webType = TemplateType.DISMANTLING_PET;
                    break;
                case 4:
                    webSiteTitle = "Restaurant Kitchen Equipments";
                    webType = TemplateType.DISMANTLING_RESTAURANT;
                    break;
                default:
                    webSiteTitle = "Import Express";
            }

            String remark = StringUtils.isNotBlank(request.getParameter("remark ")) ? request.getParameter("remark") : "";
            model.put("remark", remark);
            // ??????????????????????????????????????????
            if (GetConfigureInfo.openSync()) {
                String odids = request.getParameter("odids");

                String ordernoNew = request.getParameter("ordernoNew");

                String time = request.getParameter("time");
                String time_ = request.getParameter("time_");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                int state = Integer.parseInt(request.getParameter("state"));

                IOrderSplitDao orderSplitDao = new OrderSplitDaoImpl();
                // ????????????????????????
                OrderBean oldOrderBean = orderSplitDao.getOrderInfo(orderNo);

                OrderBean nwOrderBean = new OrderBean();
                nwOrderBean.setOrderNo(ordernoNew);

                Calendar calendar = Calendar.getInstance();
                String expect_arrive_time_ = "";
                String expect_arrive_time = "";
                if (state == 1) {
                    // ??????????????????????????????????????????
                    String mode_transport = oldOrderBean.getMode_transport();
                    int mode_transport_day = 0;
                    if (mode_transport != null && mode_transport.contains("@")) {
                        String[] mode_transports = mode_transport.split("@");
                        if (mode_transports.length > 3) {
                            mode_transport = mode_transport.split("@")[1];
                            if (Utility.getStringIsNull(mode_transport)) {
                                if (mode_transport.contains("-")) {
                                    mode_transport = mode_transport.split("-")[1];
                                }
                                mode_transport_day = Integer.parseInt(mode_transport.replace("Days", "").trim());
                            }
                        }
                    }
                    if (oldOrderBean.getOrderNo().contains("_")) {
                        if (mode_transport_day != 0 && Utility.getStringIsNull(time_)) {
                            calendar.setTime(sdf.parse(time_));
                            calendar.set(Calendar.DAY_OF_MONTH,
                                    calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
                            expect_arrive_time_ = sdf.format(calendar.getTime());
                        }
                    } else {
                        if (mode_transport_day != 0 && Utility.getStringIsNull(time)) {
                            calendar.setTime(sdf.parse(time));
                            calendar.set(Calendar.DAY_OF_MONTH,
                                    calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
                            expect_arrive_time = sdf.format(calendar.getTime());
                        }
                    }
                }

                // ????????????uuid?????????????????????????????????
                String uuid = UUIDUtil.getEffectiveUUID(oldOrderBean.getUserid(), "");
                String url = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
                request.setAttribute("autoUrl", url);
                String autoUrl = "http://www.import-express.com" + url;
                model.put("autoUrl", SwitchDomainNameUtil.checkNullAndReplace(autoUrl,MultiSiteUtil.getSiteTypeNum(orderNo)));
                // ????????????email
                IOrderSplitServer splitServer = new OrderSplitServer();
                request.setAttribute("email", splitServer.getUserEmailByUserName(oldOrderBean.getUserid()));
                model.put("email", splitServer.getUserEmailByUserName(oldOrderBean.getUserid()));
                List<Object[]> orderDetails = orderSplitDao.queryLocalOrderDetails(orderNo);
                List<Object[]> nwOrderDetails = new ArrayList<Object[]>();// ???????????????
                List<Object[]> oldOrderDetails = new ArrayList<Object[]>();// ???????????????
                // ??????????????????
                String[] odidLst = odids.split("@");
                // ???????????????????????????????????????ids
                if (odidLst.length > 0) {
                    for (int i = 0; i < orderDetails.size(); i++) {
                        boolean isNew = false;
                        for (String odid : odidLst) {
                            if (orderDetails.get(i)[1].toString().equals(odid)) {
                                nwOrderDetails.add(orderDetails.get(i));
                                isNew = true;
                                break;
                            }
                        }
                        if (!isNew) {
                            oldOrderDetails.add(orderDetails.get(i));
                        }
                    }
                }

                float product_cost = 0;
                if (nwOrderDetails.size() > 0) {
                    for (int k = 0; k < nwOrderDetails.size(); k++) {
                        product_cost += Float.valueOf(nwOrderDetails.get(k)[3].toString());

                    }
                }

                nwOrderBean.setProduct_cost(String.valueOf(product_cost));
                // ??????????????????
                request.setAttribute("details", oldOrderDetails);
                request.setAttribute("details_", nwOrderDetails);
                SwitchDomainNameUtil.changeObjectList(oldOrderDetails, MultiSiteUtil.getSiteTypeNum(orderNo));
                    SwitchDomainNameUtil.changeObjectList(nwOrderDetails, MultiSiteUtil.getSiteTypeNum(orderNo));
                model.put("details", oldOrderDetails);
                model.put("details_", nwOrderDetails);
                if (state == 1) {
                    request.setAttribute("expect_arrive_time_", expect_arrive_time_);
                    request.setAttribute("expect_arrive_time", expect_arrive_time);
                    request.setAttribute("orderbean", oldOrderBean);
                    request.setAttribute("orderbean_", nwOrderBean);
                    model.put("expect_arrive_time_", expect_arrive_time_);
                    model.put("expect_arrive_time", expect_arrive_time);
                    model.put("orderbean", oldOrderBean);
                    model.put("orderbean_", nwOrderBean);
                } else {
                    request.setAttribute("orderbean_", nwOrderBean);
                    model.put("orderbean_", nwOrderBean);
                }
                request.setAttribute("currency", oldOrderBean.getCurrency());
                model.put("currency", oldOrderBean.getCurrency());
            } else {

                String ordernoNew = request.getParameter("ordernoNew");
                String time = request.getParameter("time");
                String time_ = request.getParameter("time_");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                int state = Integer.parseInt(request.getParameter("state"));
                model.put("orderno", orderNo);
                model.put("ordernoNew", ordernoNew);
                model.put("time", time);
                model.put("time_", time_);
                model.put("state", state);
                IOrderSplitServer splitServer = new OrderSplitServer();
                String[] orderNos;
                if (state == 1) {
                    orderNos = new String[]{orderNo, ordernoNew};
                } else {
                    orderNos = new String[]{ordernoNew};
                }
                List<OrderBean> orderBeans = splitServer.getSplitOrder(orderNos);
                Calendar calendar = Calendar.getInstance();
                String expect_arrive_time_ = "";
                String expect_arrive_time = "";
                OrderBean obBean = new OrderBean();
                OrderBean obBean_ = new OrderBean();
                if (state == 1) {
                    for (int i = 0; i < orderBeans.size(); i++) {
                        // ??????????????????????????????????????????
                        String mode_transport = orderBeans.get(i).getMode_transport();
                        int mode_transport_day = 0;
                        if (mode_transport != null && mode_transport.contains("@")) {
                            String[] mode_transports = mode_transport.split("@");
                            if (mode_transports.length > 3) {
                                mode_transport = mode_transport.split("@")[1];
                                if (Utility.getStringIsNull(mode_transport)) {
                                    if (mode_transport.contains("-")) {
                                        mode_transport = mode_transport.split("-")[1];
                                    }
                                    mode_transport_day = Integer.parseInt(mode_transport.replace("Days", "").trim());
                                }
                            }
                        }
                        if (orderBeans.get(i).getOrderNo().contains("_")) {
                            if (mode_transport_day != 0 && Utility.getStringIsNull(time_)) {
                                calendar.setTime(sdf.parse(time_));
                                calendar.set(Calendar.DAY_OF_MONTH,
                                        calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
                                expect_arrive_time_ = sdf.format(calendar.getTime());
                            }
                            if (orderNo.equals(orderBeans.get(i).getOrderNo())) {
                                obBean = orderBeans.get(i);
                            } else {
                                obBean_ = orderBeans.get(i);
                            }
                        } else {
                            if (mode_transport_day != 0 && Utility.getStringIsNull(time)) {
                                calendar.setTime(sdf.parse(time));
                                calendar.set(Calendar.DAY_OF_MONTH,
                                        calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
                                expect_arrive_time = sdf.format(calendar.getTime());
                            }
                            obBean = orderBeans.get(i);
                        }
                    }
                } else {
                    obBean_ = orderBeans.get(0);
                }

                // ????????????uuid?????????????????????????????????
                String uuid = UUIDUtil.getEffectiveUUID(orderBeans.get(0).getUserid(), "");
                String url = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
                request.setAttribute("autoUrl", url);
                // ????????????email
                request.setAttribute("email", splitServer.getUserEmailByUserName(orderBeans.get(0).getUserid()));
                model.put("autoUrl", SwitchDomainNameUtil.checkNullAndReplace(url, MultiSiteUtil.getSiteTypeNum(orderNo)));
                model.put("email", splitServer.getUserEmailByUserName(orderBeans.get(0).getUserid()));
                List<Object[]> orderDetails = new ArrayList<Object[]>();
                if (state == 1) {
                    orderDetails = splitServer.getSplitOrderDetails(orderNos);
                } else {
                    orderDetails = splitServer.getOrderDetails(orderNos);
                }
                List<Object[]> details_ = new ArrayList<Object[]>();// ???????????????
                List<Object[]> details = new ArrayList<Object[]>();// ???????????????
                // ??????????????????
                for (int i = 0; i < orderDetails.size(); i++) {
                    if ((orderDetails.get(i)[0].toString().contains("_"))) {
                        if (orderNo.length() > 17) {
                            if (!orderDetails.get(i)[0].equals(ordernoNew)) {
                                details.add(orderDetails.get(i));
                            } else {
                                details_.add(orderDetails.get(i));
                            }
                        } else {
                            details_.add(orderDetails.get(i));
                        }
                    } else {
                        details.add(orderDetails.get(i));
                    }
                }

                request.setAttribute("details", details);
                request.setAttribute("details_", details_);
                SwitchDomainNameUtil.changeObjectList(details, MultiSiteUtil.getSiteTypeNum(orderNo));
                SwitchDomainNameUtil.changeObjectList(details_, MultiSiteUtil.getSiteTypeNum(orderNo));
                model.put("details", details);
                model.put("details_", details_);

                if (state == 1) {
                    request.setAttribute("expect_arrive_time_", expect_arrive_time_);
                    request.setAttribute("expect_arrive_time", expect_arrive_time);
                    request.setAttribute("orderbean", obBean);
                    request.setAttribute("orderbean_", obBean_);
                    model.put("expect_arrive_time_", expect_arrive_time_);
                    model.put("expect_arrive_time", expect_arrive_time);
                    model.put("orderbean", obBean);
                    model.put("orderbean_", obBean_);

                    model.put("title", webSiteTitle + " Split Order Reminder");
                    model.put("message", "");
                } else {
                    //???????????????????????????????????????

                    double coupon_discount = obBean_.getCoupon_discount();// coupon??????
                    double extra_discount = obBean_.getExtra_discount();// ????????????
                    double grade_discount = obBean_.getGradeDiscount();// ????????????
                    double share_discount = obBean_.getShare_discount();// ????????????
                    double discount_amount = obBean_.getDiscount_amount();// ?????????????????????BIz??????
                    double cash_back = obBean_.getCashback();// cash_back??????

                    double extra_freight = obBean_.getExtra_freight();// ????????????
                    double vatBalance = obBean_.getVatBalance();//??????????????????

                    double qualityFee = Double.parseDouble(obBean_.getActual_lwh() == null ? "0.00" : obBean_.getActual_lwh());
                    double processFee = obBean_.getProcessingfee();

                    double totalDisCount = coupon_discount + extra_discount + grade_discount
                            + share_discount + discount_amount + cash_back;
                    if (totalDisCount > 0.01) {
                        request.setAttribute("totalDisCount", orderBeans.get(0).getCurrency() + " -"
                                + (BigDecimalUtil.truncateDouble(totalDisCount, 2)));
                        model.put("totalDisCount", orderBeans.get(0).getCurrency() + " -"
                                + (BigDecimalUtil.truncateDouble(totalDisCount, 2)));
                    } else {
                        request.setAttribute("totalDisCount", "--");
                        model.put("totalDisCount", "--");
                    }

                    double totalExtraFree = extra_freight + vatBalance + qualityFee + processFee;
                    if (totalExtraFree > 0.01) {
                        request.setAttribute("totalExtraFree", orderBeans.get(0).getCurrency() + " "
                                + BigDecimalUtil.truncateDouble(totalExtraFree, 2));
                        model.put("totalExtraFree", orderBeans.get(0).getCurrency() + " "
                                + BigDecimalUtil.truncateDouble(totalExtraFree, 2));
                    } else {
                        request.setAttribute("totalExtraFree", "--");
                        model.put("totalExtraFree", "--");
                    }
                    System.err.println(orderBeans.get(0).getPay_price() + "(payPrice)="
                            +  orderBeans.get(0).getProduct_cost() + "(productCost)+ "
                            + totalExtraFree + "(totalExtraFree)" + "-" + totalDisCount + "(totalDisCount)");
                    request.setAttribute("orderbean_", obBean_);
                    model.put("orderbean_", obBean_);
                    model.put("title", "Your "+webSiteTitle+" order was partially cancelled");
                    model.put("message", obBean_.getOrderNo());
                }
                request.setAttribute("currency", orderBeans.get(0).getCurrency());
                model.put("currency", orderBeans.get(0).getCurrency());
                String liveChatLink = "http://chat32.live800.com/live800/chatClient/chatbox.jsp?companyID=496777&configID=70901&lan=en&jid=4818862369&enterurl=http%3A%2F%2Fwww.import-express.com%2Fcbtconsole%2Fapa%2Fcontact.html&amp;timestamp=1441622560799&amp;pagereferrer=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2F&amp;firstEnterUrl=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2Fcbtconsole%2Fapa%2Fcontact%2Ehtml&amp;pagetitle=Customer+Service";
                model.put("here", SwitchDomainNameUtil.checkNullAndReplace(liveChatLink, MultiSiteUtil.getSiteTypeNum(orderNo)));
            }

            try {
                // ??????????????????
                model.put("websiteType", MultiSiteUtil.getSiteTypeNum(orderNo));
                sendMailFactory.sendMail(String.valueOf(model.get("email")), email,
                        "Due to supply reasons, we can only send your order partially at first.", model, webType);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("genOrderSplitEmail: email:" + model.get("email") + " model_json:" + model + " e.message:", e);
                message = "Failed to send mail, please contact the developer by screen, thank you???" + e.getMessage();
            }
        } catch (Exception e) {
            LOG.error("genOrderSplitEmail", e);
            LOG.error("genOrderSplitEmail:", e);
        }
        LOG.info("getOrderSplit sendEmailInfo end");
        return message;
    }
   @RequestMapping("/orderdtail")
   public void getorderdtail(HttpServletResponse response,HttpServletRequest request ) throws IOException {
       try {
           int i=this.iWarehouseService.orderdtailDetail();
           if (i==0){
               i=1/0;
           }
       } catch (Exception e) {
           response.getWriter().write("{\"code\":500,\"message\":\"fail newPreOrderAutoDistribution " + e.getMessage() + "\",\"data\":null}");
           LOG.error("fail newPreOrderAutoDistribution error ", e);
           return;
       }
       response.setContentType("text/html;charset=UTF-8");
       response.getWriter().write("{\"code\":200,\"message\":\"success\",\"data\":null}");
   }

}
