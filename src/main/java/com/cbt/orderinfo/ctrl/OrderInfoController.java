package com.cbt.orderinfo.ctrl;

import ceRong.tools.bean.SearchLog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.trade.param.AlibabaTradeFastCreateOrderResult;
import com.cbt.Specification.util.DateFormatUtil;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.Tb1688OrderHistory;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.orderinfo.service.OrderinfoService;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.RechangeRecord;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.util.Utility;
import com.cbt.util.*;
import com.cbt.warehouse.pojo.returnbill;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.PurchaseGoodsBean;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.dao.*;
import com.cbt.website.dao2.IWebsiteOrderDetailDao;
import com.cbt.website.dao2.WebsiteOrderDetailDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.UploadByOkHttp;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.pojo.CommonResult;
import com.importExpress.pojo.InputData;
import com.importExpress.pojo.OrderStatic;
import com.importExpress.service.IPurchaseService;
import com.importExpress.utli.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderInfoController {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(OrderInfoController.class);
    @Autowired
    private IOrderinfoService iOrderinfoService;
    @Autowired
    private IPurchaseService purchaseService;
    @Autowired
    private SendMailFactory sendMailFactory;
    @Autowired
    private ISpiderServer spiderService;
    private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
    private static final String SERVICE_LOCAL_PATH = "/data";
    private static final String SERVICE_SHOW_URL_1 = "http://img.import-express.com";
    private static final String SERVICE_SHOW_URL_2 = "http://img1.import-express.com";
    private static final String SERVICE_SHOW_URL_3 = "https://img.import-express.com";
    private static final String SERVICE_SHOW_URL_4 = "https://img1.import-express.com";
    //private static String imgSavePath = "E:\\site\\images";//??????????????????????????????E:\site\images

    private PaymentDaoImp paymentDao = new PaymentDao();
    private IWebsiteOrderDetailDao websiteOrderDetailDao = new WebsiteOrderDetailDaoImpl();

    @RequestMapping(value = "/changeBuyer")
    public void changeBuyer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String odid = request.getParameter("odid");
        String admuserid = request.getParameter("admuserid");
        PrintWriter out = response.getWriter();
        int row = 0;
        map.put("admuserid", admuserid);
        map.put("odid", odid);
        try {
            row = iOrderinfoService.changeBuyer(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.print(row);
        out.close();
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/flushOrderFreight")
    public void flushOrderFreight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            logger.info("????????????????????????????????????===");
            List<OrderBean> list = iOrderinfoService.getFlushOrderFreightOrder();
            flushFreight(list);
            list.clear();
            logger.info("????????????????????????????????????===");
        } catch (Exception e) {
            logger.info("??????????????????????????????????????????????????????????????????");
        }
        //????????????????????????????????????????????????????????????????????????
        List<OrderBean> listFlag = iOrderinfoService.getAllOrderInfo();
        for (int i = 0; i < listFlag.size(); i++) {
            OrderBean o = listFlag.get(i);
            String orderNo = o.getOrderNo();
            String yourorder = o.getYourorder();
            String buyAmount = o.getBuyAmount();
            String buycount = o.getBuycount();
            String esprice = o.getEsBuyPrice();
            System.out.println("orderNo===================" + orderNo);
            if (StringUtil.isBlank(buyAmount) || StringUtil.isBlank(buycount)) {
                continue;
            }
            double fit = Math.abs((Double.parseDouble(esprice) - Double.parseDouble(buyAmount)) / Double.parseDouble(buyAmount) * 100);
            if (Integer.valueOf(buycount) > Integer.valueOf(yourorder) || fit > 10) {
                iOrderinfoService.deleteFlagByOrder(orderNo);
                iOrderinfoService.insertFlagByOrderid(orderNo);
            }
        }
    }

    public void flushFreight(List<OrderBean> list) {
        DecimalFormat df = new DecimalFormat("######0.00");
        for (OrderBean o : list) {
            logger.info("?????????????????????" + o.getOrderNo() + "???????????????????????????");
            double freightFee = iOrderinfoService.getFreightFee(o.getVolumeweight(), o);
            String freight = df.format(freightFee);
            //?????????????????????????????????
            List<OrderDetailsBean> odList = iOrderinfoService.getOrdersDetails(o.getOrderNo());
            double BuyPrice = 0.00;
            for (OrderDetailsBean odb : odList) {
                String price1688 = Utility.getStringIsNull(odb.getCbrPrice()) ? odb.getCbrPrice() : "0";
                if ("0".equals(price1688) || StringUtil.isBlank(odb.getCbrPrice())) {
                    price1688 = odb.getCbrdPrice();
                }
                price1688 = StringUtil.isBlank(price1688) ? "0" : price1688;
                String es_price = price1688;
                if (odb.getState() == 1 || odb.getState() == 0) {
                    es_price = StringUtil.getEsPrice(es_price);
                } else {
                    es_price = "0.00";
                }
                String ali_sellunit = odb.getAli_sellunit();
                int unit = Util.getNumberForStr(ali_sellunit);
                BuyPrice += Double.valueOf(es_price) * odb.getYourorder() * unit;
            }
            double pid_amount = 0.00;
            if (odList.size() > 0) {
                pid_amount = odList.get(0).getPid_amount();
            }
            //????????????????????????,??????????????????
            String esBuyPrice = df.format(BuyPrice + pid_amount);
            logger.info("?????????" + o.getOrderNo() + "????????????" + freight + "???????????????????????????" + esBuyPrice + "???");
            iOrderinfoService.updateFreightForOrder(o.getOrderNo(), freight, esBuyPrice);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/flushOrderWaring")
    public void flushOrderWaring(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<OrderBean> listFlag = iOrderinfoService.getAllOrderInfo();
        for (int i = 0; i < listFlag.size(); i++) {
            OrderBean o = listFlag.get(i);
            String orderNo = o.getOrderNo();
            String yourorder = o.getYourorder();
            String buyAmount = o.getBuyAmount();
            String buycount = o.getBuycount();
            String esprice = o.getEsBuyPrice();
            double fit = Math.abs((Double.parseDouble(esprice) - Double.parseDouble(buyAmount)) / Double.parseDouble(buyAmount) * 100);
            if (Integer.valueOf(buycount) > Integer.valueOf(yourorder) || fit > 10) {
                iOrderinfoService.insertFlagByOrderid(orderNo);
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getGoodsData")
    public void getGoodsData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        PrintWriter out = response.getWriter();
        List<Tb1688OrderHistory> order = new ArrayList<Tb1688OrderHistory>();
        try {
            //????????????????????????????????????scan_parcel_log_info
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
            iOrderinfoService.insertScanLog(expresstrackid, adm.getAdmName());
            order = iOrderinfoService.getGoodsData(expresstrackid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String json = gson.toJson(order);
        out.print(json);
        out.flush();
        out.close();
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getResultInfo")
    public void getResultInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        List<SearchResultInfo> list = new ArrayList<SearchResultInfo>();
        PrintWriter out = response.getWriter();
        try {
            String expresstrackid = request.getParameter("expresstrackid");
            String checked = request.getParameter("checked");
            String selectType = request.getParameter("selectType");
            String offlineType = request.getParameter("offlineType");
            list = iOrderinfoService.getOrder(expresstrackid, checked, selectType, offlineType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (SearchResultInfo searchResultInfo : list) {
            String orderNo = searchResultInfo.getOrderid();
            int isUpdate = 0;
            if (searchResultInfo.getOrderid().contains("_SN")) {
                // ??????????????????????????????
                isUpdate = 1;
            } else if (orderNo.contains("_")) {
                String[] splitList = orderNo.split("_");
                if (splitList != null && splitList.length > 1 && splitList[1].length() > 3 && splitList[0].contains(splitList[1])) {
                    // ??????????????????
                    isUpdate = 2;
                }
            }
            if (isUpdate > 0) {
                try {
                    // ????????????
                    iOrderinfoService.updateOrderSplitNumGoodsDistribution(orderNo);
                    // ??????????????????
                    iOrderinfoService.updateOrderSplitNumOrderDetailsData(orderNo.substring(0, orderNo.indexOf("_")), orderNo);
                    // ??????????????????????????????????????????
                    iOrderinfoService.updateOrderSplitNumGoodsCommunicationInfoData(orderNo);
                    if (isUpdate == 1) {
                        // ??????????????????????????????
                        iOrderinfoService.updateOrderSplitNumPurchaseData(orderNo);
                        // ??????????????????????????????
                        iOrderinfoService.updateOrderSplitNumIdRelationtableData(orderNo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("updateOrderSplitNumOrderDetailsData error:", e);
                }
            }
        }
        out.print(JSONArray.toJSONString(list));
        out.flush();
        out.close();
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/userUpdateAdmuser")
    public void userUpdateAdmuser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String adminid = request.getParameter("adminid");
        String userid = request.getParameter("userid");
        String email = request.getParameter("email");
        String admJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
        String users = user.getAdmName();
        String admName = request.getParameter("admName");
        String orderNo = request.getParameter("orderNo");
        PrintWriter out = response.getWriter();
        int row = 0;
        map.put("userid", userid);
        map.put("adminid", adminid);
        map.put("users", users);
        map.put("email", email);
        map.put("admName", admName);
        map.put("orderNo", orderNo);
        try {
            row = iOrderinfoService.addUser(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.print(row);
        out.close();
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/updatecanceltatus")
    public void updatecanceltatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Map<String, String> map = new HashMap<String, String>();
        int res = 0;
        try {
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
            map.put("orderid", request.getParameter("orderid"));
            map.put("goodid", request.getParameter("goodid"));
            map.put("odid", request.getParameter("odid"));
            map.put("repState", request.getParameter("repState"));
            map.put("warehouseRemark", request.getParameter("warehouseRemark"));
            map.put("count", request.getParameter("count"));
            map.put("adminId", String.valueOf(adm.getId()));
            res = iOrderinfoService.updatecanceltatus(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/updateCheckStatus")
    @ResponseBody
    public Map<String, Object> updateCheckStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> result = new HashMap<String, Object>();
        int res = 0, num = 0;
        List<Tb1688OrderHistory> checkOrder = null;
        try {
            map.put("th", request.getParameter("th"));
            map.put("tbPid", request.getParameter("tbPid"));
            map.put("tbskuid", request.getParameter("tbskuid"));
            map.put("goodid", request.getParameter("goodid"));
            map.put("status", request.getParameter("status"));
            map.put("orderid", request.getParameter("orderid"));
            map.put("goodurl", request.getParameter("goodurl"));
            map.put("barcode", request.getParameter("barcode"));
            map.put("userid", request.getParameter("userid"));
            map.put("userName", request.getParameter("userName"));
            map.put("tbOrderId", request.getParameter("tbOrderId"));
            map.put("shipno", request.getParameter("shipno"));
            map.put("itemid", request.getParameter("itemid"));
            map.put("repState", request.getParameter("repState"));
            map.put("odid", request.getParameter("odid"));
            map.put("specid", request.getParameter("specid"));
            map.put("skuid", request.getParameter("skuid"));
            map.put("warehouseRemark", request.getParameter("warehouseRemark"));
            int count = Integer.valueOf(request.getParameter("count"));
            map.put("count", String.valueOf(count));
            String weight = "0.00";
            weight = StringUtil.isBlank(weight) ? "0.00" : weight;
            map.put("weight", String.valueOf(weight));
            res = iOrderinfoService.updateTbstatus(map);
            if (res > 0) {
                //????????????????????????????????????????????????????????????????????????
                num = iOrderinfoService.checkOrderState(map.get("orderid"));

                //??????????????????
                String tbsourceCount = request.getParameter("tbsourceCount");
                String sourceCount = request.getParameter("sourceCount");
                if ("1".equals(sourceCount)) {
                    checkOrder = iOrderinfoService.checkOrder(map.get("shipno"), StrUtils.isNum(tbsourceCount) ? Integer.valueOf(tbsourceCount) : 0);
                }
            }
            String goods_pid = request.getParameter("goods_pid");//????????????????????????
            if (!("1".equals(goods_pid) || "".equals(goods_pid) || goods_pid == null)) {
                Boolean b = this.iOrderinfoService.UpdateGoodsState(goods_pid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("res", res);
        result.put("num", num);
        result.put("checkOrder", checkOrder);
        result.put("barcode", request.getParameter("barcode"));
        result.put("shipno", request.getParameter("shipno"));
        return result;
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/updatecancelChecktatus")
    public void updatecancelChecktatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Map<String, String> map = new HashMap<String, String>();
        int res = 0;
        try {
            map.put("orderid", request.getParameter("orderid"));
            map.put("goodid", request.getParameter("goodid"));
            map.put("warehouseRemark", request.getParameter("warehouseRemark"));
            map.put("repState", request.getParameter("repState"));
            map.put("count", request.getParameter("count"));
            map.put("odid", request.getParameter("odid"));
            //??????????????????
            map.put("barcode", request.getParameter("barcode"));
            map.put("specid", request.getParameter("specid"));
            map.put("skuid", request.getParameter("skuid"));
            map.put("tbskuid", request.getParameter("tbskuid"));
            map.put("tbspecid", request.getParameter("tbspecid"));
            map.put("seiUnit", request.getParameter("seiUnit"));
            String cance_inventory_count = request.getParameter("cance_inventory_count");
            if (cance_inventory_count == null || "".equals(cance_inventory_count) || !com.cbt.util.StrUtils.isMatch(cance_inventory_count, "(\\d+)")) {
                cance_inventory_count = "0";
            }
            map.put("cance_inventory_count", cance_inventory_count);
            String weight = request.getParameter("weight");
            weight = StringUtil.isBlank(weight) ? "0.00" : weight;
            map.put("weight", request.getParameter("weight"));
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
            map.put("adminId", String.valueOf(adm.getId()));
            res = iOrderinfoService.updatecancelChecktatus(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/updateGoodStatus")
    public void updateGoodStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        Map<String, String> map = new HashMap<String, String>();
        int res = 0;
        response.setContentType("text/html;charset=utf-8");
        String userName = request.getParameter("userName");
        String warehouseRemark = request.getParameter("warehouseRemark");
        if (warehouseRemark == null || "".equals(warehouseRemark)) {
            warehouseRemark = "";
        } else {
            warehouseRemark = userName + ":" + warehouseRemark + "(" + DateFormatUtil.getWithMinutes(new Date()) + ");";
        }
        try {
            map.put("orderid", request.getParameter("orderid"));
            map.put("goodid", request.getParameter("goodid"));
            map.put("status", request.getParameter("status"));
            map.put("goodurl", request.getParameter("goodurl"));
            map.put("odid", request.getParameter("odid"));
            map.put("barcode", request.getParameter("barcode"));
            map.put("userid", request.getParameter("userid"));
            map.put("userName", userName);
            map.put("tbOrderId", request.getParameter("tbOrderId"));
            map.put("shipno", request.getParameter("shipno"));
            map.put("itemid", request.getParameter("itemid"));
            map.put("repState", request.getParameter("repState"));
            map.put("warehouseRemark", warehouseRemark);
            map.put("taobaoId", request.getParameter("taobaoId"));
            // ??????????????????????????????
            int count = Integer.valueOf(request.getParameter("count"));
            map.put("count", String.valueOf(count));
            res = iOrderinfoService.updateGoodStatus(map);
            String goods_pid = request.getParameter("goods_pid");//????????????????????????
            if (!("1".equals(goods_pid) || "".equals(goods_pid) || goods_pid == null)) {
                Boolean b = this.iOrderinfoService.UpdateGoodsState(goods_pid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * ???????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/allTrack")
    public void allTrack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        Map<String, String> map = new HashMap<String, String>();

        String tbInfo = request.getParameter("tbInfo");
        if (StringUtils.isBlank(tbInfo)) {
            PrintWriter out = response.getWriter();
            out.print(0);
            out.flush();
            out.close();
            return;
        }

        try {
            String shipno = request.getParameter("shipno");
            String type = request.getParameter("type");
            String barcode = request.getParameter("barcode");
            String userid = request.getParameter("userid");
            String userName = request.getParameter("userName");
            String tbOrderId = request.getParameter("tbOrderId");
            map.put("shipno", shipno);
            map.put("barcode", barcode);
            map.put("userid", userid);
            map.put("userName", userName);
            map.put("tbOrderId", tbOrderId);
            map.put("warehouseRemark", "");
            map.put("status", "1");
            map.put("repState", "1");

            Map<String, String> resultMap = new HashMap<>();
            if (StringUtils.isBlank(tbOrderId) || StringUtils.isBlank(shipno)) {
                PrintWriter out = response.getWriter();
                resultMap.put("code","0");
                resultMap.put("message","????????????????????????????????????");
                out.print(resultMap);
                out.flush();
                out.close();
                return;
            }


            // ????????????????????????????????????????????????????????????????????????????????????
            List<SearchResultInfo> list = JSONArray.parseArray(tbInfo, SearchResultInfo.class);
            if (CollectionUtils.isEmpty(list)) {
                PrintWriter out = response.getWriter();
                resultMap.put("code","0");
                resultMap.put("message","??????????????????");
                out.print(resultMap);
                out.flush();
                out.close();
                return;
            }

            if ("1".equals(type)) {
                list.forEach(e-> {
                    map.put("odid", e.getOdid());
                    map.put("goodurl", e.getGoods_url());
                    map.put("goodid", String.valueOf(e.getGoodsid()));
                    map.put("orderid", e.getOrderid());
                    map.put("count", "0");
                    map.put("itemid", e.getTaobao_itemid());
                    map.put("tbOrderId", e.getTaobao_orderid());

                    iOrderinfoService.updateGoodStatus(map);
                });

                /*//??????????????????
                Map<String, Integer> shipMap = iOrderinfoService.getTbShip(shipno);
                if (shipMap != null && !shipMap.isEmpty()) {
                    List<Map<String, Object>> allList = iOrderinfoService.allTrack(map);
                    for (Map<String, Object> allMap : allList) {
                        String orderid = String.valueOf(allMap.get("orderid"));
                        String goodid = String.valueOf(allMap.get("goodsid"));
                        String goodurl = String.valueOf(allMap.get("goods_url"));
                        String odid = String.valueOf(allMap.get("odid"));
                        String skuid = String.valueOf(allMap.get("skuid"));
                        String itemid = String.valueOf(allMap.get("tb_1688_itemid"));
                        String usecount = String.valueOf(allMap.get("usecount"));
                        map.put("odid", odid);
                        map.put("goodurl", goodurl);
                        map.put("goodid", goodid);
                        map.put("orderid", orderid);
                        map.put("count", "0");
                        map.put("itemid", itemid);

                        Integer count = shipMap.get(itemid + "_" + skuid);
                        if (count == null) {
                            count = shipMap.get(itemid + "_");
                        }
                        if (count != null && count + 1 > Integer.parseInt(usecount)) {
                            iOrderinfoService.updateGoodStatus(map);
                        }
                    }
                }*/

            } else if ("0".equals(type)) {
                List<OrderDetailsBean> oList = iOrderinfoService.getAllCancelDetails(map);
                for (OrderDetailsBean o : oList) {
                    String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
                    Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
                    map.put("orderid", o.getOrderid());
                    map.put("goodid", String.valueOf(o.getGoodsid()));
                    map.put("odid", String.valueOf(o.getId()));
                    map.put("repState", request.getParameter("repState"));
                    map.put("warehouseRemark", request.getParameter("warehouseRemark"));
                    map.put("count", request.getParameter("count"));
                    map.put("adminId", String.valueOf(adm.getId()));
                    iOrderinfoService.updatecanceltatus(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(1);
        out.flush();
        out.close();
    }

    /**
     * 1688????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/orderReturn")
    public void orderReturn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        int row = 0;
        String id = request.getParameter("id");
        String orderid = request.getParameter("orderid");
        String admJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
        if (user == null) {
            out.print(row);
            out.close();
        }
        String users = user.getAdmName();
        try {
            map.put("id", id);
            map.put("orderid", orderid);
            map.put("admName", users);
            row = iOrderinfoService.orderReturn(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.print(row);
        out.close();
    }

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/addUser")
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String adminid = request.getParameter("adminid");
        String userid = request.getParameter("userid");
        String email = request.getParameter("email");
        String admJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
        String users = user.getAdmName();
        String admName = request.getParameter("admName");
        String orderNo = request.getParameter("orderNo");
        PrintWriter out = response.getWriter();
        int row = 0;
        map.put("userid", userid);
        map.put("adminid", adminid);
        map.put("users", users);
        map.put("email", email);
        map.put("admName", admName);
        map.put("orderNo", orderNo);
        try {
            row = iOrderinfoService.addUser(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.print(row);
        out.close();
    }

    /**
     * ????????????-??????????????????????????????????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/sendCutomers")
    public void sendCutomers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String orderNo = null;
        String orderNo1 = request.getParameter("orderNo");
        int whichOne = Integer.parseInt(request.getParameter("whichOne"));
        // ??????????????????isDropship??????
        int isDropship = Integer.parseInt(request.getParameter("isDropship"));
        // 0?????????1???
        if (isDropship == 1) {
            orderNo = orderNo1.substring(0, orderNo1.indexOf("_"));
        } else {
            orderNo = orderNo1;
        }
        int row = 0;
        //???????????????????????????????????????
        String email = iOrderinfoService.getUserEmailByOrderNo(orderNo);
        if (StringUtil.isNotBlank(email)) {
            Map<String, Object> modelM = new HashedMap();
            modelM.put("orderNo", orderNo);
            modelM.put("name", email);
            modelM.put("accountLink", "https://www.import-express.com/orderInfo/emailLink?orderNo=" + orderNo + "");
            MultiSiteUtil.getSiteTypeNum(orderNo);
            if (MultiSiteUtil.getSiteTypeNum(orderNo) == 1) {
                modelM.put("websiteType", 1);
                sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order change notice", modelM, TemplateType.GOODS_CHANGE);
            }
            if (MultiSiteUtil.getSiteTypeNum(orderNo) == 2) {
                modelM.put("websiteType", 2);
                modelM.put("accountLink", "https://www.kidscharming.com/orderInfo/emailLink?orderNo=" + orderNo + "");
                sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order change notice", modelM, TemplateType.GOODS_CHANGE_KIDS);
            }
            if (MultiSiteUtil.getSiteTypeNum(orderNo) == 3) {
                modelM.put("websiteType", 3);
                modelM.put("accountLink", "https://www.petstoreinc.com/orderInfo/emailLink?orderNo=" + orderNo + "");
                sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order change notice", modelM, TemplateType.GOODS_CHANGE_PET);
            }
//			sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order change notice", modelM, TemplateType.GOODS_CHANGE);

            iOrderinfoService.updateOrderinfoUpdateState(orderNo);
            SendMQ.sendMsg(new RunSqlModel("update orderinfo set server_update=1 where order_no='" + orderNo + "'"));

            row = 1;
        }
        out.print(row);
        out.close();
    }

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/sendCheckEmailForUser")
    public void sendCheckEmailForUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String orderNo = request.getParameter("orderNo");
        String email = request.getParameter("email");
        String remark = request.getParameter("remark");
        int row = iOrderinfoService.checkRecord(orderNo);
        int index = 0;
        if (row == 1) {
            index = 2;
        } else if (StringUtil.isNotBlank(email)) {
            Map<String, Object> modelM = new HashedMap();
            modelM.put("orderNo", orderNo);
            modelM.put("name", email);
            modelM.put("remark", remark);
            modelM.put("toHref", "https://www.import-express.com/apa/tracking.html?loginflag=false&orderNo=" + orderNo + "");
            modelM.put("accountLink", "https://www.import-express.com/orderInfo/emailLink?orderNo=" + orderNo + "");
            sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order Inspection notice", modelM, TemplateType.CHECK);
            iOrderinfoService.insertEmailRecord(orderNo);
            index = 1;
        }
        out.print(index);
        out.close();
    }

    public void getQueryParam(HttpServletRequest request, Map<String, String> paramMap, Admuser user, HttpServletResponse response) {
        String userID_req = request.getParameter("userid");
        String state_req = request.getParameter("state");
        String trackState_req = request.getParameter("trackState");
        String startdate_req = request.getParameter("startdate");
        String enddate_req = request.getParameter("enddate");
        int showUnpaid = Integer.parseInt(request.getParameter("showUnpaid"));
        String orderno = request.getParameter("orderno");
        orderno = StringUtil.isBlank(orderno) ? "" : orderno;
        String email = request.getParameter("email");
        email = StringUtil.isBlank(email) ? "" : email;
        String paymentid = request.getParameter("paymentid");
        paymentid = StringUtil.isBlank(paymentid) ? "" : paymentid;
        int page = Utility.getStringIsNull(request.getParameter("page")) ? Integer.parseInt(request.getParameter("page")) : 1;
        String admuserid_str = request.getParameter("admuserid");
        admuserid_str = StringUtil.isBlank(admuserid_str) ? "0" : admuserid_str;
        request.setAttribute("admuserid_str", admuserid_str);
        String type = request.getParameter("type");
        type = StringUtil.isNotBlank(type) ? type : "";
        String status = request.getParameter("status");
        int status_ = Utility.getStringIsNull(status) ? Integer.parseInt(status) : 0;
        request.setAttribute("page", page);
        page = page > 0 ? (page - 1) * 40 : 0;
        userID_req = userID_req != null && !userID_req.equals("") ? userID_req.replaceAll("\\D+", "") : "0";
        int userID = userID_req != null && !userID_req.equals("") ? Integer.parseInt(userID_req) : 0;
        int state = Utility.getStringIsNull(state_req) ? Integer.parseInt(state_req) : -2;
        int trackState = Utility.getStringIsNull(trackState_req) ? Integer.parseInt(trackState_req) : 0;
        int admuserid = user.getId();
        if ("0".equals(user.getRoletype())) {
            admuserid = Utility.getStringIsNull(admuserid_str) ? Integer.parseInt(admuserid_str) : 0;
        }
        startdate_req = StringUtil.isNotBlank(startdate_req) ? startdate_req + " 00:00:00" : "0";
        enddate_req = StringUtil.isNotBlank(enddate_req) ? enddate_req + " 23:59:59" : "0";
        //Added <V1.0.1> Start??? cjc 2019/6/4 10:57:52 Description : ??????????????????????????????
        if (9 == state) {
            type = "order_pending";
        }
        //End???

        paramMap.put("userID", String.valueOf(userID));
        paramMap.put("state", String.valueOf(state));
        paramMap.put("trackState", String.valueOf(trackState));
        paramMap.put("startdate_req", startdate_req);
        paramMap.put("enddate_req", enddate_req);
        paramMap.put("orderno", orderno);
        paramMap.put("page", String.valueOf(page));
        paramMap.put("admuserid", String.valueOf(admuserid));
        paramMap.put("showUnpaid", String.valueOf(showUnpaid));
        paramMap.put("type", type);
        paramMap.put("status_", String.valueOf(status_));
        paramMap.put("paymentid", paymentid);
        paramMap.put("email", email);
        request.setAttribute("admuserid", admuserid);
        request.setAttribute("strname", admuserid);
        request.setAttribute("admName", user.getAdmName());
        request.setAttribute("roletype", user.getRoletype());
        request.setAttribute("strm", user.getRoletype());
        request.setAttribute("showUnpaid", showUnpaid);
        request.setAttribute("email", email);
        request.setAttribute("type", type);
    }


    @RequestMapping(value = "/getOrderInfo.do", method = RequestMethod.GET)
    public String getOrderInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        long startTime = System.currentTimeMillis();
        Map<String, String> paramMap = new HashMap<String, String>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            String admJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
            if (user == null) {
                return "main_login";
            }
            getQueryParam(request, paramMap, user, response);
            if (StringUtil.isNotBlank(paramMap.get("type").toString()) && "order_pending".equals(paramMap.get("type").toString())) {
                list = iOrderinfoService.getorderPending(Integer.valueOf(paramMap.get("admuserid")));
            } else {
                list = iOrderinfoService.getOrderManagementQuery(paramMap);
//				for(int i=0;i<list.size();i++){
//					String orderid=list.get(i).get("order_no");
//					String problem=iOrderinfoService.getProblem(orderid);
//					list.get(i).put("problem", problem);
//				}
            }
            int count = getCount(paramMap, list);
            //???????????????
            StringBuilder sb = new StringBuilder();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    sb.append("'" + list.get(i).get("order_no") + "',");
                }
                request.setAttribute("ordernolist", sb.toString().substring(0, sb.length() - 1));
            }
            request.setAttribute("orderws", JSONArray.toJSON(list));
            UserDao dao = new UserDaoImpl();
            //??????????????????????????????????????????
            List<ConfirmUserInfo> sellAdm = iOrderinfoService.getAllSalesAndBuyer();
            List<ConfirmUserInfo> purchaseAdm = new ArrayList<ConfirmUserInfo>();
            purchaseAdm = dao.getAllByRoleType(2);
            request.setAttribute("sellAdm", JSONArray.toJSONString(sellAdm));
            request.setAttribute("purchaseAdm", JSONArray.toJSONString(purchaseAdm));
            request.setAttribute("count", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("????????????????????????:"+(System.currentTimeMillis()-startTime));
        return "nordermgr";
    }

    private int getCount(Map<String, String> paramMap, List<Map<String, String>> list) {
        int count = 0;
        if (list != null && !list.isEmpty() && !"order_pending".equals(paramMap.get("type"))) {
            count = iOrderinfoService.getOrdersCount(paramMap);
        } else {
            count = list.size();
        }
        return count;
    }

    private List<ConfirmUserInfo> getConfirmUserInfos(HttpServletRequest request, UserDao dao) {
        List<ConfirmUserInfo> listAdm;
        String action = request.getParameter("action");
        if ("currentUser".equals(action)) {
            String sessionId = request.getSession().getId();
            String admuserJson = Redis.hget(sessionId, "admuser");
            Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
            listAdm = dao.getCurSub(admuser);
        } else {
            listAdm = dao.getAllByOperations();
        }
        return listAdm;
    }

    @RequestMapping(value = "/getOrderStates")
    @ResponseBody
    public List<OrderStatic> getOrderStates(HttpServletRequest request) {
        //?????????????????????
        List<OrderStatic> rsList = new ArrayList<>();
        try {
            String admuserid_str = request.getParameter("admuserid");
            String admJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
            String strm = user.getRoletype();
            int admuserid = user.getId();
            if ("0".equals(strm)) {
                admuserid = Utility.getStringIsNull(admuserid_str) ? Integer.parseInt(admuserid_str) : 0;
            }
            List<Map<String, Object>> maps = iOrderinfoService.getOrdersState(admuserid);

            if (CollectionUtils.isNotEmpty(maps)) {
                maps.forEach(e -> {
                    try {
                        rsList.add(MapAndBeanUtil.map2bean(e, OrderStatic.class));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // System.err.println(e);
					/*tempMap.clear();
					try {
						for (Map.Entry<String, Integer> entry : e.entrySet()) {
							tempMap.put(entry.getKey(), entry.getValue());
						}
						if(tempMap.size() == 2){
							rsList.add(MapAndBeanUtil.map2bean(tempMap, OrderStatic.class));
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}*/
                    //rsList.add(new OrderStatic(e.get("counts"), e.get("state")));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsList;
    }

    /**
     * @param request
     * @param model
     * @return Map<String, Object>
     * @Title getComfirmedSourceGoods
     * @Description ??????????????????1688?????????????????????
     */
    @RequestMapping(value = "/getComfirmedSourceGoods")
    public @ResponseBody
    Map<String, Object> getComfirmedSourceGoods(HttpServletRequest request, Model model) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> listMap = purchaseService.getComfirmedSourceGoods();
        List<String> shopidList = new ArrayList<String>(5);
        List<PurchaseGoodsBean> beanList = new ArrayList<PurchaseGoodsBean>();
        if (listMap != null) {
            for (Map<String, Object> goods_map : listMap) {
                PurchaseGoodsBean bean = new PurchaseGoodsBean();
                bean.setPid(String.valueOf(goods_map.get("goods_pid")));
                bean.setNumber(Integer.parseInt(String.valueOf(goods_map.get("yourorder"))));
                bean.setShopid(String.valueOf(goods_map.get("shop_id")));
                bean.setId(Integer.parseInt(String.valueOf(goods_map.get("id"))));
                bean.setCar_img(String.valueOf(goods_map.get("car_img")));
                bean.setCar_type(String.valueOf(goods_map.get("car_type")));
                bean.setGoodsname(String.valueOf(goods_map.get("goodsname")));
                bean.setGoodsprice(String.valueOf(goods_map.get("goodsprice")));
                bean.setAdminid(String.valueOf(goods_map.get("adminid")));
                bean.setOrderid(String.valueOf(goods_map.get("orderid")));
                bean.setSampling_flag((Integer) goods_map.get("sampling_flag"));
                beanList.add(bean);
                if (!shopidList.contains(bean.getShopid())) {
                    shopidList.add(bean.getShopid());
                }
            }
        }
        map.put("beanList", beanList);
        map.put("shopidList", shopidList);
        return map;
    }

    @RequestMapping(value = "/generate1688Orders")
    public @ResponseBody
    List<Map<String, Object>> generate1688Orders(HttpServletRequest request, Model model) {
        List<Map<String, Object>> list_map = new ArrayList<Map<String, Object>>();
        String app_key = "7031967";//1688?????????APP_KEY
        String sec_key = "z2tB0cavGIL";//1688?????????APP_KEY??????
        String access_taken = (String) request.getSession().getAttribute("1688_access_taken");
        if (access_taken == null) {
            List<String> result_list = purchaseService.sendPost("https://gw.open.1688.com/openapi/param2/1/system.oauth2/getToken/" + app_key,
                    "grant_type=refresh_token&client_id=" + app_key + "&client_secret=" + sec_key + "&refresh_token=419e8236-9c1c-4328-b0c2-3d5fc66814d5");
            if (result_list != null) {
                for (int i = 0; i < result_list.size(); i++) {
                    String str = result_list.get(i);
                    if (str.indexOf("access_token") > -1) {
                        String[] strArr = str.replace("{", "").replace("}", "").split(",");
                        for (String sss : strArr) {
                            if (sss.indexOf("access_token") > -1) {
                                access_taken = sss.replace("\"", "").split(":")[1];
                                break;
                            }
                        }
                    }
                }
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(access_taken)) {
                request.getSession().setAttribute("1688_access_taken", access_taken);
            }
        }
        String shop_id = request.getParameter("shopid");
        //??????????????? ????????????
        List<Map<String, Object>> listMap = purchaseService.getComfirmedSourceGoods();
        List<String> shopidList = new ArrayList<String>(5);
        List<PurchaseGoodsBean> beanList = new ArrayList<PurchaseGoodsBean>();
        if (listMap != null) {
            for (Map<String, Object> goods_map : listMap) {
                PurchaseGoodsBean bean = new PurchaseGoodsBean();
                bean.setPid(String.valueOf(goods_map.get("goods_pid")));
                bean.setNumber(Integer.parseInt(String.valueOf(goods_map.get("yourorder"))));
                bean.setShopid(String.valueOf(goods_map.get("shop_id")));
                bean.setId(Integer.parseInt(String.valueOf(goods_map.get("id"))));
                bean.setCar_img(String.valueOf(goods_map.get("car_img")));
                bean.setCar_type(String.valueOf(goods_map.get("car_type")));
                bean.setGoodsname(String.valueOf(goods_map.get("goodsname")));
                bean.setGoodsprice(String.valueOf(goods_map.get("goodsprice")));
                bean.setSpecId(String.valueOf(goods_map.get("actual_volume")));
                bean.setSampling_flag((Integer) goods_map.get("sampling_flag"));
                beanList.add(bean);
                if (!shopidList.contains(bean.getShopid())) {
                    shopidList.add(bean.getShopid());
                }
            }
        }
        for (String shopid : shopidList) {
            if (shop_id != null && !shopid.equals(shop_id)) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shopid", shopid);
            List<PurchaseGoodsBean> thisShopGoods = new ArrayList<PurchaseGoodsBean>();
            List<Integer> idsList = new ArrayList<Integer>();
            for (PurchaseGoodsBean pgbBean : beanList) {
                //?????????????????????
                if (pgbBean.getSampling_flag() != 2 && shopid.equals(pgbBean.getShopid())) {
                    thisShopGoods.add(pgbBean);
                    idsList.add(pgbBean.getId());
                }
            }
            if (thisShopGoods.size() > 0) {
                try {
                    //??????1688API,????????????
                    AlibabaTradeFastCreateOrderResult result = purchaseService.generateOrdersByShopId(app_key, sec_key, access_taken, thisShopGoods);
                    if (result.getSuccess()) {
                        String orderId = result.getResult().getOrderId();
                        Long orderAmount = result.getResult().getTotalSuccessAmount();
                        System.out.println("??????" + shopid + "???????????????????????????" + orderId);
                        map.put("success", true);
                        map.put("orderId", orderId);
                        map.put("orderAmount", orderAmount);
                        map.put("message", "??????" + shopid + "???????????????????????????" + orderId + "??????????????????" + orderAmount);

                        //?????????????????????????????????
                        purchaseService.updateAutoOrderFlag(idsList);
                    } else {
                        String message = result.getMessage();//????????????
                        map.put("success", false);
                        map.put("message", "??????" + shopid + "????????????????????????" + message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("success", false);
                    map.put("message", "??????" + shopid + "????????????????????????" + e.getMessage());
                }
            } else {
                map.put("success", false);
                map.put("message", "??????" + shopid + "????????????????????????????????? ?????????????????????????????????????????????");
            }
            list_map.add(map);
        }
        return list_map;
    }

    @RequestMapping(value = "/getSizeChart")
    public @ResponseBody
    List<Map<String, Object>> getSizeChart(HttpServletRequest request) {
        String catid = request.getParameter("catid");
        String rowidListstr = request.getParameter("rowidListstr");
        List<Integer> rowidList = new ArrayList<Integer>();
        if (StringUtils.isNotBlank(rowidListstr)) {
            String[] rowidArrays = rowidListstr.split(",");
            for (String idstr : rowidArrays) {
                if (StringUtils.isBlank(idstr)) {
                    continue;
                } else {
                    idstr = idstr.replaceAll("\\s*", "");
                }
                rowidList.add(Integer.parseInt(idstr));
            }
        }
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isNotBlank(admuserJson)) {
            Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
            //??????????????????
            if (rowidList.size() > 0 && adm != null) {
                purchaseService.updateSizeChartById(rowidList, adm.getId());
            }

        }
        return purchaseService.getSizeChart(catid);
    }

    //??????????????????
    @RequestMapping(value = "/loadCategoryName")
    public @ResponseBody
    List<Map<String, Object>> loadCategoryName(HttpServletRequest request) {
        String catid = request.getParameter("catid");
        return purchaseService.loadCategoryName(catid);
    }

    //??????????????????
    @RequestMapping(value = "/loadCategoryName_add")
    public @ResponseBody
    List<Map<String, Object>> loadCategoryName_add(HttpServletRequest request) {
        String catid = request.getParameter("catid");
        return purchaseService.loadCategoryName_add(catid);
    }

    //??????????????????
    @RequestMapping(value = "/getSizeChart_add")
    public @ResponseBody
    List<Map<String, Object>> getSizeChart_add(HttpServletRequest request) {
        String catid = request.getParameter("catid");
        String rowidListstr = request.getParameter("rowidListstr");
        List<Integer> rowidList = new ArrayList<Integer>();
        if (StringUtils.isNotBlank(rowidListstr)) {
            String[] rowidArrays = rowidListstr.split(",");
            for (String idstr : rowidArrays) {
                if (StringUtils.isBlank(idstr)) {
                    continue;
                } else {
                    idstr = idstr.replaceAll("\\s*", "");
                }
                rowidList.add(Integer.parseInt(idstr));
            }
        }
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isNotBlank(admuserJson)) {
            Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
            //??????????????????
            if (rowidList.size() > 0 && adm != null) {
                purchaseService.updateSizeChartById_add(rowidList, adm.getId());
            }

        }
        return purchaseService.getSizeChart_add(catid);
    }

    @RequestMapping(value = "/uploadImg")
    public @ResponseBody
    Map<String, Object> uploadImg(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        response.setContentType("text/html;charset=utf-8");
        String imgObj = request.getParameter("imgFile");
        String imgName = request.getParameter("imgName");
        String rowid = request.getParameter("rowid");
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank("pid")) {
            pid = "1111111";
        }
        int row = 0;
        try {
            imgName = URLDecoder.decode(imgName, "utf-8");//????????????????????????????????????????????????????????????????????

            //String path = imgSavePath+File.separator+imgName;//Windows??????????????????
            // ????????????????????????
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            String localDiskPath = ftpConfig.getLocalDiskPath();
            String imgSavePath = localDiskPath + pid;
            String path = imgSavePath + File.separator + imgName;
            //??????????????????????????????????
            File file = new File(imgSavePath);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();//????????????????????
            }

            //???? ?????????imgObj ??????????????????????
            FileOutputStream os = new FileOutputStream(path);
            imgObj = imgObj.replaceAll("#wb#", "+");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(imgObj);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//??????????????????
                    b[i] += 256;
                }
            }
            InputStream is = new ByteArrayInputStream(b);
            int len = 0;
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            os.close();
            is.close();

            Integer row_id = Integer.parseInt(rowid);
            //?????????????????????????????????
            if ("1".equals(request.getParameter("add"))) {
                row = purchaseService.updateSizeChart_add(imgName, path, row_id);
            } else {
                row = purchaseService.updateSizeChart(imgName, path, row_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", 0);
        }
        if (row > 0) {
            map.put("state", 1);
        } else {
            map.put("state", 0);
        }
        return map;
    }

    /**
     * @param request
     * @param response
     * @return Map<String, Object>
     * @Title updateOnlineDetailImgs
     * @Description ????????????????????????????????????????????????????????????????????????
     */
    @RequestMapping(value = "/updateOnlineDetailImgs")
    public @ResponseBody
    List<String> updateOnlineDetailImgs(HttpServletRequest request, HttpServletResponse response) {
        List<String> result_list = new ArrayList<String>();
        List<Integer> up_ids = new ArrayList<Integer>();
        //??????????????????????????????????????????
        List<Map<String, Object>> listMap = purchaseService.getSizeChartPidInfo();
        if (listMap != null) {
            Map<String, String> uploadMap = new HashMap<String, String>();
            int count = 0;
            for (Map<String, Object> map : listMap) {
                count++;
                String id = String.valueOf(map.get("id"));
                String pid = String.valueOf(map.get("pid"));//40454495059
                String en_info = String.valueOf(map.get("en_info"));
                String imgPath = String.valueOf(map.get("remotpath"));//https://img.import-express.com/importcsvimg/coreimg1/40454495059/desc/3019180200_1237798930.jpg
                String imgName = imgPath.split("/desc/")[1];//3019180200_1237798930.jpg
                String replace_img = String.valueOf(map.get("replace_img"));//20181227161224.png
                String replace_localpath = String.valueOf(map.get("replace_localpath"));// /data/cbtconsole/cbtimg/editimg/520962734304/20181227161224.png
                //replace_localpath = "E:\\site\\images\\39310691178\\4.4.png";
                if (StringUtils.isNotBlank(en_info) && StringUtils.isNotBlank(pid) && StringUtils.isNotBlank(imgPath) && StringUtils.isNotBlank(replace_img) && StringUtils.isNotBlank(replace_localpath)) {
                    //??????en_info??????????????????????????????????????????????????????
                    Document nwDoc = Jsoup.parseBodyFragment(en_info);
                    Elements imgEls = nwDoc.getElementsByTag("img");
                    if (imgEls != null && imgEls.size() > 0) {
                        for (Element imel : imgEls) {
                            String imgUrl = imel.attr("src");
                            //??????????????????
                            if (StringUtils.isNotBlank(imgUrl) && imgUrl.indexOf(imgName) > -1) {
                                /**********?????? en_info?????? start***/
                                //???????????????????????????????????????
                                String uploadFileName = Calendar.getInstance().getTimeInMillis() + ".jpg";
                                imgUrl = imgUrl.replace(imgName, uploadFileName);
                                imel.attr("src", imgUrl);
                                /**********?????? en_info?????? end***/

                                /**********????????????????????????????????? start*****/
                                //String remoteSavePath = remotePath+File.separator+"desc"+File.separator+replace_img;
                                String remoteSavePath = GoodsInfoUtils.changeRemotePathToLocal(imgPath, 1);
                                remoteSavePath = remoteSavePath.replace(imgName, "");
                                uploadMap.put(replace_localpath, remoteSavePath + "@@@@" + uploadFileName);
                                /**********????????????????????????????????? end*****/
                                break;
                            }
                        }
                        /**********????????????MQ?????????mongodb eninfo?????? start*****/
                        InputData inputData = new InputData('u'); //u???????????????c???????????????d????????????
                        inputData.setPid(pid);
                        inputData.setEninfo(nwDoc.html());
                        GoodsInfoUpdateOnlineUtil.updateOnlineAndSolr(inputData, 0, 0);
                        result_list.add(id + "@" + pid);
                        up_ids.add(Integer.parseInt(id));
                        /**********????????????MQ?????????mongodb eninfo?????? end*****/

                        //???100???????????????
                        if (count == 100) {
                            boolean dsds = UploadByOkHttp.doUpload(uploadMap, 1);
                            //?????????????????????
                            if (up_ids.size() > 0 && dsds) {
                                purchaseService.updateSizeChartUpload(up_ids);
                            }

                            uploadMap = new HashMap<String, String>();
                            up_ids = new ArrayList<Integer>();
                            count = 0;
                        }
                    }
                } else {
                    logger.error("updateOnlineDetailImgs data error,row id :" + id);
                }
            }
        }
        return result_list;
    }


    /**
     * @param request
     * @return Map<String, Object>
     * @Title searchProductLog
     * @Description ??????????????????????????????????????????????????????
     */
    @SuppressWarnings({"unchecked", "static-access"})
    @RequestMapping("/searchProductLog")
    public @ResponseBody
    Map<String, Object> searchProductLog(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String saveFlag = request.getParameter("saveFlag");
        Map<String, Object> map = new HashMap<String, Object>();
        String userinfo = request.getParameter("userid");
        int userid = userinfo == null ? 0 : Integer.parseInt(userinfo);
        if ("0".equals(saveFlag) || "1".equals(saveFlag)) {
            String keyWords = request.getParameter("keyWords") == null ? "" : request.getParameter("keyWords");
            if (StringUtil.isNotBlank(keyWords) && keyWords.length() > 200) {
                keyWords = keyWords.substring(0, 198);
            }
            String catid = request.getParameter("catid");
            String sortType = request.getParameter("sortType");
            String pageNumber = request.getParameter("pageNumber") == null ? "" : request.getParameter("pageNumber");
            if (StringUtil.isNotBlank(pageNumber) && pageNumber.length() > 10) {
                pageNumber = pageNumber.substring(0, 8);
            }
            String productShowIdList = request.getParameter("productShowIdList");
            String allProductList = request.getParameter("allProductList");
            int listSize = Integer.parseInt(request.getParameter("listSize") == null ? "0" : request.getParameter("listSize"));
            String rowid = request.getParameter("rowid");
            SearchLog seaLog = new SearchLog();
            seaLog.setKeyWords(keyWords);
            seaLog.setCatid(catid);
            seaLog.setPageNumber(pageNumber);
            seaLog.setSortType(sortType);
            seaLog.setProductShowIdList(productShowIdList);
            seaLog.setSaveFlag(saveFlag);
            seaLog.setId(Integer.parseInt(rowid == null ? "0" : rowid));
            seaLog.setUserid(userid);
            seaLog.setAllProductList(allProductList);
            seaLog.setListSize(listSize);
            //??????????????????
            if ("1".equals(request.getParameter("device"))) {
                seaLog.setDevice(1);
            } else {
                seaLog.setDevice(0);
            }
            String sessionid = request.getSession(true).getId();
            seaLog.setSessionid(sessionid);
            String ip = "";
            Calendar cal = Calendar.getInstance();
            int year = Calendar.YEAR;
            int month = Calendar.MONTH;
            int date = Calendar.DATE;
            seaLog.setYear(cal.get(year));
            seaLog.setMonth(cal.get(year) + "-" + (cal.get(month) + 1));
            seaLog.setDay(cal.get(year) + "-" + (cal.get(month) + 1) + "-" + cal.get(date));
            seaLog.setSearchMD5(Md5Util.md5Operation(seaLog.getKeyWords() + seaLog.getCatid() + seaLog.getSortType() + seaLog.getPageNumber()));
            if (userid != 0) {
                seaLog.setSearchUserMD5(Md5Util.md5Operation(seaLog.getKeyWords() + seaLog.getCatid() + seaLog.getSortType() + seaLog.getPageNumber() + userid));
            } else {
                seaLog.setSearchUserMD5(Md5Util.md5Operation(seaLog.getKeyWords() + seaLog.getCatid() + seaLog.getSortType() + seaLog.getPageNumber() + sessionid));
            }
            int rowId = spiderService.saveTheSearchLogOnSearchPage(seaLog);
            map.put("rowid", rowId);
            map.put("searchMD5", seaLog.getSearchMD5());
            map.put("searchUserMD5", seaLog.getSearchUserMD5());
            return map;
        } else {
            return map;
        }
    }

    @RequestMapping("/searchClickProductLog")
    @ResponseBody
    public void markTheProduct(HttpServletRequest request) {
        String searchMD5 = request.getParameter("searchMD5");
        String searchUserMD5 = request.getParameter("searchUserMD5");
        String goodsPid = request.getParameter("goodsPid");
        spiderService.saveTheClickCountOnSearchPage(goodsPid, searchMD5, searchUserMD5);
    }


    @RequestMapping("/recoverCancelOrder")
    @ResponseBody
    public JsonResult recoverCancelOrder(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("??????????????????!");
            return json;
        }
        com.cbt.pojo.Admuser admuser = (com.cbt.pojo.Admuser) SerializeUtil.JsonToObj(admuserJson, com.cbt.pojo.Admuser.class);

        String orderNo = request.getParameter("orderNo");
        if (StringUtils.isBlank(orderNo)) {
            json.setMessage("?????????????????????");
            json.setOk(false);
            return json;
        }
        String stateStr = request.getParameter("state");
        if (StringUtils.isBlank(stateStr)) {
            json.setMessage("????????????????????????");
            json.setOk(false);
            return json;
        }

        String oldStateStr = request.getParameter("oldState");
        if (StringUtils.isBlank(oldStateStr)) {
            oldStateStr = "0";
        }

        try {
            RechangeRecord record = paymentDao.querySystemCancelOrder(orderNo);
            if (record == null || record.getPrice() <= 0) {
                json.setMessage("???????????????????????????????????????????????????????????????");
                json.setOk(false);
            } else {
                int res = websiteOrderDetailDao.websiteUpdateOrderState(orderNo, Integer.valueOf(stateStr));
                // ?????????????????????????????????
                websiteOrderDetailDao.updateOrderStateLog(orderNo, Integer.valueOf(stateStr), Integer.valueOf(oldStateStr), "??????????????????", admuser.getId());
                if (res > 0) {
                    ChangUserBalanceDao balanceDao = new ChangUserBalanceDaoImpl();
                    BigDecimal bd = new BigDecimal(String.valueOf(record.getPrice()));
                    json = balanceDao.changeBalance(record.getUserId(), bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), -1,
                            1, orderNo, "??????????????????", admuser.getId());
                    if (json.isOk()) {
                        // ????????????????????????
                        boolean isSuccess = websiteOrderDetailDao.deleteRechangeRecord(record.getUserId(), orderNo);
                        if (isSuccess) {
                            json.setOk(true);
                        } else {
                            json.setMessage("??????????????????????????????");
                            json.setOk(false);
                            // ?????????????????????????????????
                            websiteOrderDetailDao.websiteUpdateOrderState(orderNo, -1);
                        }
                    } else {
                        // ?????????????????????????????????
                        websiteOrderDetailDao.websiteUpdateOrderState(orderNo, -1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.error(e.getMessage());
            json.setMessage("error:" + e.getMessage());
            json.setOk(false);
        }
        return json;
    }

    @RequestMapping("/getOdid")
    @ResponseBody
    public List<String> getOdid(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        String shipno = request.getParameter("shipno");
        list = this.spiderService.FindOdidByShipno(shipno);
        return list;
    }

    @RequestMapping("/updataChecked")//????????????
    @ResponseBody
    public int updataChecked(HttpServletRequest request) {
        int ret = 0;
        String id = request.getParameter("id");
        ret = this.spiderService.updataCheckedById(id);
        return ret;
    }

    @RequestMapping("/delOrderinfo")
    @ResponseBody
    public int delOrderinfo(String orderno) {
        int ret = 0;
        try {
            ret = spiderService.delOrderinfo(orderno);
        } catch (Exception e) {
            log.error("????????????", e);
        }
        return ret;
    }
    /**
     *????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getReplace")
    @ResponseBody
    public CommonResult getReplace(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String shipno = request.getParameter("shipno");
        String odid = request.getParameter("odid");
        try {
        	List<Map<String,Object>> replace = iOrderinfoService.getReplace(odid,shipno);
        	if(replace == null || replace.isEmpty()) {
        		return CommonResult.failed(); 
        	}
        	List<Map<String,Object>> result = Lists.newArrayList();
        	replace.stream().forEach(r->{
        		String goods_pid = (String)r.get("goods_pid");
        		String itemid = (String)r.get("itemid");
        		if(!goods_pid.equals(itemid)) {
        			//??????
        			String imgurl = (String)r.get("imgurl");
        			r.put("imgurl", imgurl.replace("60x60", "220x220"));
        			r.put("replace", true);
        			result.add(r);
        		}
        	});
        	if(CollectionUtils.isEmpty(result)) {
        		return CommonResult.failed();
        	}
        	return CommonResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }
}
