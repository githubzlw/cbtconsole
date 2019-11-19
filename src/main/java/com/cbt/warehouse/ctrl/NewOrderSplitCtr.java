package com.cbt.warehouse.ctrl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbt.service.CustomGoodsService;
import com.importExpress.pojo.GoodsOverSea;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Inventory;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.OrderInfoConstantUtil;
import com.cbt.util.OrderInfoUtil;
import com.cbt.util.Redis;
import com.cbt.util.Send;
import com.cbt.util.SerializeUtil;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;
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
import com.importExpress.utli.MultiSiteUtil;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.SwitchDomainNameUtil;
import com.importExpress.utli.UserInfoUtils;

import net.sf.json.JSONArray;

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
     * 订单拆分(正常订单和Drop Ship订单)
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
            String state = request.getParameter("state");// 0取消1二次出货
            if (state == null || "".equals(state)) {
                json.setOk(false);
                json.setMessage("获取拆单状态失败");
            }

            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            if (admuserJson == null) {
                json.setOk(false);
                json.setMessage("用户未登陆");
                return json;
            } else {
                Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
                // 判断是否是Drop Ship订单，根据订单号获取订单信息
                OrderInfoDao orderInfoDao = new OrderInfoImpl();
                OrderBean orderBean = orderInfoDao.getOrderInfo(orderno, null);
                IOrderSplitServer splitServer = new OrderSplitServer();
                if (orderBean.getIsDropshipOrder() == 1) {
                    // Drop Ship 拆单流程
                    Map<String, String> map = splitServer.splitOrderShip(orderno, odids, orderBean.getUserid(), state);
                    if ("1".equals(map.get("res"))) {
                        json.setOk(true);
                        json.setData(map.get("orderNoNew"));
                    } else {
                        json.setOk(false);
                        json.setMessage(map.get("msg"));
                    }
                } else {
                    // 正常拆单流程
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
         * 1.所有 有 采购链接的 商品 就 直接 转入一个 新订单，并且转状态到 采购中 2.已支付的产品金额按产品直接拆分 3.客户是以前付过
         * 运费的，我们就按照 体积重量，直接拆分 这 两个订单 体积重量 = 长* 宽*高 (厘米)/5000 和 实际重量对比 取 大值
         * 4.客户如果取消商品，而该订单 是 -split 的，就不再计算批量折扣 5.原订单的批量折扣金额， 按照价格比例分开 6.50美元的
         * 运费credit的拆分
         */
        try {
            // 查询订单和订单详情
            OrderBean orderBean = dao.getOrders(orderNo);
            String[] odidLst = odids.split("@");// 需要取消的商品order_details的id
            // 记录老订单拆分日志
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
            // 判断是否获取到要拆分的商品ids
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
                    // 生成另一个采购中订单
                    // 修改已有货源订单详情的订单号
                    nwOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 0, 0);

                    // 开始执行拆单
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
                        json.setMessage("拆分失败，已成功拆分商品：" + (ocBf.length() > 0 ? ocBf.toString().substring(1) : ""));
                    } else {
                        // 判断是否是取消状态,是取消,则更新新订单的状态
                        if ("0".equals(state)) {
                            // 执行退款操作:更新客户余额=新订单payprice;更新新订单状态为取消;
                            // 新增客户余额变更记录表recharge_record;新增支付记录payment
                            dao.cancelNewOrder(orderBean.getUserid(), nwOrderNo);
                        } else {
                            // 补充新的支付信息
                            dao.insertIntoPayment(orderBean.getUserid(), nwOrderNo, orderNo);
                        }
                        // 更新订单的状态
                        dao.checkAndUpdateOrderState(orderNo, nwOrderNo);
                        // 更新新订单商品的入库信息,本地
                        if (goodsIds.size() > 0) {
                            dao.updateWarehouseInfo(orderNo, nwOrderNo, goodsIds);
                        }

                        json.setOk(true);
                        json.setMessage("拆分成功");
                        json.setData(nwOrderNo);
                        if ("0".equals(state)) {
                            // 拆单取消后取消的商品如果有使用库存则还原库存
                        	inventoryService.cancelToInventory(odidLst, 0, "");
//                            dao.cancelInventory(odidLst);
                        }
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("拆分失败，匹配商品不成功");
                }

            } else {
                json.setOk(false);
                json.setMessage("获取拆分商品失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("OrderSplitServer-Exception:", e);
            json.setOk(false);
            json.setMessage("拆分失败，原因：" + e.getMessage());
        } finally {
            return json;
        }
    }

    @SuppressWarnings("finally")
    private JsonResult newSplitCommonOrder(Admuser admuser, String orderNo, String odids, String state, int isOverSea) {

        // 1.订单拆单前，做数据保存信息
        // 2.复制订单信息充当临时订单
        // 3.统计拆单商品所有的原始价格，支付价格之和，给出预期结果，保存数据库
        // 4.执行拆单操作
        // 5.执行完成后，给出执行的结果并保存数据库
        // 6.如果是取消商品进余额，则调用统一接口进行客户余额变更

        JsonResult json = new JsonResult();
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        String nwOrderNo = "";
        try {

            // 查询订单和订单详情
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if(orderBean == null || StringUtils.isBlank(orderBean.getOrderNo())){
                json.setOk(false);
                json.setMessage("获取订单失败，请重试");
                return json;
            }
            // 获取新的订单号
            nwOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 0, isOverSea);
            // 需要取消的商品order_details的id
            String[] odidLst = odids.split("@");
            // 1.拆单之前保存订单原始信息 log
            boolean isSuccess = saveOrderInfoBeforeSplitOrder(orderBean, orderNo, odids, admuser);
            if (isSuccess) {
                // 2.复制订单信息充当临时订单
                OrderBean orderBeanTemp = (OrderBean) orderBean.clone();
                // 获取所有订单详情的信息
                List<OrderDetailsBean> orderDetails = splitDao.getOrdersDetails_split(orderNo);
                List<OrderDetailsBean> nwOrderDetails = new ArrayList<OrderDetailsBean>();
                List<Integer> goodsIds = new ArrayList<Integer>();
                List<Integer> odIds = new ArrayList<>();
                // 判断是否获取到要拆分的商品ids
                if (odidLst.length > 0) {
                    // 原订单支付金额
                    double totalPayPriceOld = orderBean.getPay_price();
                    // 原订单商品总价
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
                    //插入主单信息
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

                    // 判断传递的odids有效
                    if (nwOrderDetails.size() == odidLst.length && !(totalPayPriceOld <= 0 || totalGoodsCostOld <= 0)) {
                        // 3.计算预期结果并保存和拆单操作
                        calculateExpectedResult(json, nwOrderDetails, orderNo, nwOrderNo, orderBean, totalGoodsCostOld,
                                totalPayPriceOld, orderBeanTemp, admuser, state, odidLst, goodsIds, odIds);
                        //取消订单后商品进入库存中
                        //判断该订单是否为测试订单如果是则不入库存
//						boolean flag=splitDao.checkTestOrder(odidLst[0]);
//						if(flag){}
                        /* if ("0".equals(state)) {
                        	inventoryService.cancelToInventory(odidLst, admuser.getId(), admuser.getAdmName());
                           for (String odid : odidLst) {
                                splitDao.addInventory(odid, "拆单取消库存");
                            }
                        }*/
                    } else {
                        json.setOk(false);
                        if (nwOrderDetails.size() == 0 || nwOrderDetails.size() != odidLst.length) {
                            json.setMessage("拆分失败，匹配商品不成功");
                        } else if (totalPayPriceOld <= 0) {
                            json.setMessage("拆分失败，此订单支付金额小于等于0");
                        } else if (totalGoodsCostOld <= 0) {
                            json.setMessage("拆分失败，此订单总价小于等于0");
                        }
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("获取需要拆分的商品失败");
                }
            } else {
                json.setOk(false);
                json.setMessage("保存日志失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("OrderSplitServer-Exception:", e);
            json.setOk(false);
            json.setMessage("拆分失败，原因：" + e.getMessage());
        } finally {
            return json;
        }
    }

    /**
     * 拆单之前保存订单原始信息
     */
    private boolean saveOrderInfoBeforeSplitOrder(OrderBean orderBean, String orderNo, String odids, Admuser admuser) {
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        // 记录老订单拆分日志
        String info_log = ", product_cost=" + orderBean.getProduct_cost() + ",pay_price=" + orderBean.getPay_price()
                + ",pay_price_tow=" + orderBean.getPay_price_tow() + ",pay_price_three="
                + orderBean.getPay_price_three() + ",actual_ffreight=" + orderBean.getActual_ffreight()
                + ",service_fee=" + orderBean.getService_fee() + ",order_ac=" + orderBean.getOrder_ac()
                + ",discount_amount=" + orderBean.getDiscount_amount() + ",cashback=" + orderBean.getCashback()
                + ",extra_freight=" + orderBean.getExtra_freight() + ",share_discount=" + orderBean.getShare_discount()
                + ",Coupon_discount=" + orderBean.getCoupon_discount() + ",extra_discount="
                + orderBean.getExtra_discount() + "state=" + orderBean.getState()
                + "vatBalance = " + orderBean.getVatBalance(); // --cjc
        // 1.订单拆单前，做数据保存信息
        LOG.info("ordersplit orderNo:" + orderNo + "; info_log:[" + info_log + "];");
        LOG.info("ordersplit odids:[" + odids + "];");
        info_log = "";
        return splitDao.addOrderInfoAndPaymentLog(orderBean.getOrderNo(), admuser, 0);
    }

    /**
     * 计算预期结果并保存
     */
    private void calculateExpectedResult(JsonResult json, List<OrderDetailsBean> nwOrderDetails, String orderNo,
                                         String nwOrderNo, OrderBean orderBean, double totalGoodsCostOld, double totalPayPriceOld,
                                         OrderBean orderBeanTemp, Admuser admuser, String state, String[] odidLst,
                                         List<Integer> goodsIds, List<Integer> odIds) {
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        // 3.统计拆单商品所有的原始价格，支付价格之和，给出预期结果，保存数据库
        // 新的订单商品总价
        double totalGoodsCostNew = 0;
        for (OrderDetailsBean nwOdDt : nwOrderDetails) {
            totalGoodsCostNew += Double.valueOf(nwOdDt.getGoodsprice()) * nwOdDt.getYourorder();
        }
        if (totalGoodsCostNew > 0) {

            // 拆分比例
            double splitRatio = totalGoodsCostNew / totalGoodsCostOld;
            OrderBean odbeanNew = OrderInfoUtil.genNewOrderInfo(orderBean, orderBeanTemp, splitRatio, nwOrderNo,
                    totalGoodsCostOld, nwOrderDetails);

            // 新的订单支付金额
            double totalPayPriceNew = totalPayPriceOld * splitRatio;
            // 3.统计拆单商品所有的原始价格，支付价格之和，给出预期结果，保存数据库(保存预期结果)
            List<OrderBean> orderBeans = new ArrayList<OrderBean>();
            orderBeans.add(odbeanNew);
            orderBeans.add(orderBeanTemp);
            boolean success = splitDao.saveOrderInfoLogByList(orderBeans, admuser);
            if (success) {
                // 开始拆单操作
                doSplitOrderAction(json, nwOrderDetails, orderNo, nwOrderNo, orderBeanTemp, odbeanNew, admuser, state,
                        odidLst, goodsIds, (float) totalPayPriceNew,  odIds);
            } else {
                json.setOk(false);
                json.setMessage("保存拆单信息失败，程序终止执行");
            }
        } else {
            json.setOk(false);
            json.setMessage("拆分失败，拆分商品商品总价为0");
        }
    }

    /**
     * 真正的拆单操作
     */
    private void doSplitOrderAction(JsonResult json, List<OrderDetailsBean> nwOrderDetails, String orderNo,
                                    String nwOrderNo, OrderBean orderBeanTemp, OrderBean odbeanNew, Admuser admuser, String state,
                                    String[] odidLst, List<Integer> goodsIds, float totalPayPriceNew, List<Integer> odIds) {

        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        // 4.执行拆单操作
        // 开始执行拆单
        boolean isOk = splitDao.newOrderSplitFun(orderBeanTemp, odbeanNew, nwOrderDetails, state, 0);
        if (!isOk) {
            json.setOk(false);
            json.setMessage("拆分失败请重试");
        } else {

            // 更新订单的状态
            splitDao.checkAndUpdateOrderState(orderNo, nwOrderNo);
            // 更新新订单商品的入库信息,本地
            if (goodsIds.size() > 0) {
                splitDao.updateWarehouseInfo(orderNo, nwOrderNo, goodsIds);
                splitDao.updateGoodsCommunicationInfo(orderNo, nwOrderNo, odIds);
                odIds.clear();
            }

            // 判断是否是取消状态,是取消,则更新新订单的状态
            if ("0".equals(state)) {
                // 执行退款操作:更新客户余额=新订单payprice;更新新订单状态为取消;
                // 新增客户余额变更记录表recharge_record;新增支付记录payment
                splitDao.cancelNewOrder(orderBeanTemp.getUserid(), nwOrderNo);
                // 拆单取消后取消的商品如果有使用库存则还原库存
                inventoryService.cancelToInventory(odidLst, admuser.getId(), admuser.getAdmName());
//                splitDao.cancelInventory(odidLst);
                // 5.如果是取消商品进余额，则调用统一接口进行客户余额变更
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
            // 6.执行完成后，给出执行的结果并保存数据库
            splitDao.addOrderInfoAndPaymentLog(nwOrderNo, admuser, 1);

            goodsIds.clear();
            nwOrderDetails.clear();
            json.setOk(true);
            json.setMessage("拆分成功");
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
                json.setMessage("获取拆单数据失败");
                return json;
            } else if (orderNo.contains("SN")) {
                json.setOk(false);
                json.setMessage("数量拆单数据，不允许再次拆单");
                return json;
            }

            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }

            // 判断是否是Drop Ship订单，根据订单号获取订单信息
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if(orderBean == null || StringUtils.isBlank(orderBean.getOrderNo())){
                json.setOk(false);
                json.setMessage("获取订单信息失败!");
                return json;
            }
            if (orderBean.getIsDropshipOrder() == 1) {
                // Drop Ship 拆单，不可数量拆单
                json.setOk(false);
                json.setMessage("DropShip订单，不能数量拆单");
                return json;
            }

            JSONArray jsonArray = JSONArray.fromObject(odIds);
            List<SplitGoodsNumBean> splitIdList = (List<SplitGoodsNumBean>) JSONArray.toCollection(jsonArray, SplitGoodsNumBean.class);
            if (splitIdList == null || splitIdList.isEmpty()) {
                json.setOk(false);
                json.setMessage("转换拆单数据失败");
                return json;
            }
            OrderBean orderInfo = iOrderinfoService.getOrders(orderNo);
            if (orderInfo.getState() != 5 && orderInfo.getState() != 1 && orderInfo.getState() != 2) {
                json.setOk(false);
                json.setMessage("订单状态非审核、采购或者入库状态，不能拆单");
                return json;
            }

            // 1.获取原来数据,并进行数据处理
            List<OrderDetailsBean> odbList = iOrderinfoService.getOrdersDetails(orderNo);
            List<OrderDetailsBean> nwOrderDetails = new ArrayList<>(splitIdList.size());
            // 生成新的OrderDetailsBean数据
            Map<Integer, OrderDetailsBean> oldOrderDeatisMap = new HashMap<>(odbList.size());

            double oldTotalGoodsCost = 0;
            // 计算商品总价
            for (OrderDetailsBean orderDetail : odbList) {
                oldTotalGoodsCost += Double.valueOf(orderDetail.getGoodsprice()) * orderDetail.getYourorder();
            }

            double newTotalGoodsCost = 0;
            for (SplitGoodsNumBean goodsNumBean : splitIdList) {
                goodsNumBean.setAdminId(admuser.getId());
                goodsNumBean.setOrderNo(orderNo);
                for (OrderDetailsBean orderDetail : odbList) {
                    if (orderDetail.getId() == goodsNumBean.getOdId()) {
                        // 保存商品价格和数量信息，放入日志
                        goodsNumBean.setGoodsPrice(Double.valueOf(orderDetail.getGoodsprice()));
                        goodsNumBean.setOldNum(orderDetail.getYourorder());

                        newTotalGoodsCost += goodsNumBean.getGoodsPrice() * goodsNumBean.getNum();
                        // 执行修改产品数量操作
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

            // 记录日志
            iOrderinfoService.insertIntoOrderSplitNumLog(splitIdList);
            // 计算拆分产品总价占原总价的百分比
            if (oldTotalGoodsCost == 0 || newTotalGoodsCost == 0) {
                json.setOk(false);
                json.setMessage("订单商品计算总价失败");
                return json;
            }
            double splitRatio = newTotalGoodsCost / oldTotalGoodsCost;
            // 2.新的订单数据
            // 复制订单信息充当临时订单
            OrderBean orderBeanTemp = (OrderBean) orderBean.clone();
            String newOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 1, 0);
            OrderBean newOrderBean = OrderInfoUtil.genNewOrderInfo(orderBean, orderBeanTemp, splitRatio, newOrderNo,
                    oldTotalGoodsCost, nwOrderDetails);

            // 拆单日志
            List<OrderBean> orderBeans = new ArrayList<>(2);
            orderBeans.add(orderBean);
            orderBeans.add(newOrderBean);
            orderBeans.add(orderBeanTemp);
            splitDao.saveOrderInfoLogByList(orderBeans, admuser);
            // 3.开始执行拆单
            boolean isOk = splitDao.newOrderSplitFun(orderBeanTemp, newOrderBean, nwOrderDetails,
                    OrderInfoConstantUtil.REVIEW,  1);
            if (isOk) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("拆分失败请重试");
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
                json.setMessage("获取拆单数据失败");
                return json;
            }

            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }

            // 判断是否是Drop Ship订单，根据订单号获取订单信息
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if (orderBean.getIsDropshipOrder() == 1) {
                // Drop Ship 拆单，不可数量拆单
                json.setOk(false);
                json.setMessage("DropShip订单，不能数量拆单");
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
                json.setMessage("获取订单号失败");
                return json;
            }

            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }

            List<OrderDetailsBean> odb=iOrderinfoService.getOrdersDetails(orderNo);
            boolean noOverSea = false;
            for(OrderDetailsBean o : odb){
                List<GoodsOverSea> goodsOverSeaList = customGoodsService.queryGoodsOverSeaInfoByPid(o.getGoods_pid());
                if(CollectionUtils.isNotEmpty(goodsOverSeaList)){
                    Long count = goodsOverSeaList.stream().filter(e-> e.getIsSupport() > 0).count();
                    if(count > 0){
                        o.setOverSeaFlag(1);
                    }else{
                        noOverSea = true;
                        break;
                    }
                    goodsOverSeaList.clear();
                } else{
                    noOverSea = true;
                    break;
                }
            }
            odb.clear();
            if(noOverSea){
                json.setOk(false);
                json.setMessage("含有非海外仓商品，不能设置整单为海外仓订单");
                return json;
            }

            // 查询订单和订单详情
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            OrderBean orderBean = splitDao.getOrders(orderNo);
            if(orderBean == null || StringUtils.isBlank(orderBean.getOrderNo())){
                json.setOk(false);
                json.setMessage("获取订单失败，请重试");
                return json;
            }
            // 获取新的订单号
            String nwOrderNo = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 0, 1);

            int rs = iOrderinfoService.updateOrderNoToNewNo(orderNo, nwOrderNo);
            if(rs > 0){
                 json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("更新订单失败，请重试");
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
     * 获取订单拆分后的邮件信息
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
                    webSiteTitle = "Kids Product Wholesale";
                    webType = TemplateType.DISMANTLING_KID;
                    break;
                case 3:
                    webSiteTitle = "Lovely Pet Supply";
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
            // 判断是否开启线下同步线上配置
            if (GetConfigureInfo.openSync()) {
                String odids = request.getParameter("odids");

                String ordernoNew = request.getParameter("ordernoNew");

                String time = request.getParameter("time");
                String time_ = request.getParameter("time_");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                int state = Integer.parseInt(request.getParameter("state"));

                IOrderSplitDao orderSplitDao = new OrderSplitDaoImpl();
                // 老订单的订单信息
                OrderBean oldOrderBean = orderSplitDao.getOrderInfo(orderNo);

                OrderBean nwOrderBean = new OrderBean();
                nwOrderBean.setOrderNo(ordernoNew);

                Calendar calendar = Calendar.getInstance();
                String expect_arrive_time_ = "";
                String expect_arrive_time = "";
                if (state == 1) {
                    // 获取运输方式和对应的运输时间
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

                // 获取用户uuid和个人中心自动登录路径
                String uuid = UUIDUtil.getEffectiveUUID(oldOrderBean.getUserid(), "");
                String url = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
                request.setAttribute("autoUrl", url);
                String autoUrl = "http://www.import-express.com" + url;
                model.put("autoUrl", SwitchDomainNameUtil.checkNullAndReplace(autoUrl,MultiSiteUtil.getSiteTypeNum(orderNo)));
                // 获取用户email
                IOrderSplitServer splitServer = new OrderSplitServer();
                request.setAttribute("email", splitServer.getUserEmailByUserName(oldOrderBean.getUserid()));
                model.put("email", splitServer.getUserEmailByUserName(oldOrderBean.getUserid()));
                List<Object[]> orderDetails = orderSplitDao.queryLocalOrderDetails(orderNo);
                List<Object[]> nwOrderDetails = new ArrayList<Object[]>();// 拆分的订单
                List<Object[]> oldOrderDetails = new ArrayList<Object[]>();// 剩余的订单
                // 发送邮件开始
                String[] odidLst = odids.split("@");
                // 判断是否获取到要拆分的商品ids
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
                // 发送邮件开始
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
                        // 获取运输方式和对应的运输时间
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

                // 获取用户uuid和个人中心自动登录路径
                String uuid = UUIDUtil.getEffectiveUUID(orderBeans.get(0).getUserid(), "");
                String url = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
                request.setAttribute("autoUrl", url);
                // 获取用户email
                request.setAttribute("email", splitServer.getUserEmailByUserName(orderBeans.get(0).getUserid()));
                model.put("autoUrl", SwitchDomainNameUtil.checkNullAndReplace(url, MultiSiteUtil.getSiteTypeNum(orderNo)));
                model.put("email", splitServer.getUserEmailByUserName(orderBeans.get(0).getUserid()));
                List<Object[]> orderDetails = new ArrayList<Object[]>();
                if (state == 1) {
                    orderDetails = splitServer.getSplitOrderDetails(orderNos);
                } else {
                    orderDetails = splitServer.getOrderDetails(orderNos);
                }
                List<Object[]> details_ = new ArrayList<Object[]>();// 拆分的订单
                List<Object[]> details = new ArrayList<Object[]>();// 剩余的订单
                // 发送邮件开始
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
                    //针对取消的订单计算折扣金额

                    double coupon_discount = obBean_.getCoupon_discount();// coupon优惠
                    double extra_discount = obBean_.getExtra_discount();// 其他优惠
                    double grade_discount = obBean_.getGradeDiscount();// 等级优惠
                    double share_discount = obBean_.getShare_discount();// 分享优惠
                    double discount_amount = obBean_.getDiscount_amount();// 优惠金额（之前BIz等）
                    double cash_back = obBean_.getCashback();// cash_back折扣

                    double extra_freight = obBean_.getExtra_freight();// 额外运费
                    double vatBalance = obBean_.getVatBalance();//双清包税价格

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
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(model);
            String modeStr = jsonObject.toString();
            try {
                // 邮件替换头部
                model.put("websiteType", MultiSiteUtil.getSiteTypeNum(orderNo));
                sendMailFactory.sendMail(String.valueOf(model.get("email")), email,
                        "Due to supply reasons, we can only send your order partially at first.", model, webType);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("genOrderSplitEmail: email:" + model.get("email") + " model_json:" + modeStr + " e.message:", e);
                message = "Failed to send mail, please contact the developer by screen, thank you！" + e.getMessage();
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
