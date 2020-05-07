package com.importExpress.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Buy4MeCusotme;
import com.cbt.pojo.BuyForMeStatistic;
import com.cbt.userinfo.dao.UserMapper;
import com.cbt.util.GetConfigureInfo;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.importExpress.mapper.BuyForMeMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.BuyForMeService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.importExpress.utli.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BuyForMeServiceImpl implements BuyForMeService {

    private static final String getFreightCostUrl = GetConfigureInfo.getValueByCbt("getMinFreightUrl");

    private static UrlUtil instance = UrlUtil.getInstance();

    @Autowired
    private BuyForMeMapper buyForMemapper;
    @Autowired
    UserMapper userMapper;

    private Map<String, String> ipMap = new HashMap<>();

    @Override
    public List<BFOrderInfo> getOrders(Map<String, Object> map) {
        List<BFOrderInfo> orders = buyForMemapper.getOrders(map);
        if (orders == null) {
            return orders;
        }
//		订单状态：-1 取消，0申请，1处理中 2销售处理完成 3已支付;
        orders.stream().forEach(o -> {
            String content = o.getState() == -1 ? "取消" : o.getState() == 0 ?
                    "申请中" : o.getState() == 1 ? "处理中" : o.getState() == 2 ?
                    "销售处理完成" : o.getState() == 3 ? "待支付" : o.getState() == 4 ? "已支付" : "";
            o.setStateContent(content);
        });
        return orders;
    }

    @Override
    public int getOrdersCount(Map<String, Object> map) {
        return buyForMemapper.getOrdersCount(map);
    }

    @Override
    public List<BFOrderDetail> getOrderDetails(String orderNo, String bfId) {
        List<BFOrderDetail> orderDetails = buyForMemapper.getOrderDetails(orderNo);
        if (orderDetails == null || orderDetails.isEmpty()) {
            return Lists.newArrayList();
        }
        List<BFOrderDetailSku> orderDetailsSku = getOrderDetailsSku(bfId);
        if (orderDetailsSku == null || orderDetailsSku.isEmpty()) {
            return orderDetails;
        }
        Map<Integer, List<DetailsSku>> detailsIdSku = Maps.newHashMap();
        orderDetailsSku.stream().forEach(o -> {
            int bfDetailsId = o.getBfDetailsId();
            List<DetailsSku> list = detailsIdSku.get(bfDetailsId);
            list = list == null ? Lists.newArrayList() : list;
            DetailsSku detailsSku = DetailsSku.builder().num(o.getNum()).skuid(o.getSkuid())
                    .price(o.getPrice()).url(o.getProductUrl()).sku(o.getSku()).id(o.getId())
                    .priceBuy(o.getPriceBuy()).priceBuyc(o.getPriceBuyc()).shipFeight(o.getShipFeight())
                    .weight(o.getWeight())
                    .state(o.getState())
                    .unit(o.getUnit())
                    .build();
            list.add(detailsSku);
            detailsIdSku.put(bfDetailsId, list);
        });
        orderDetails.stream().forEach(o -> {
            List<DetailsSku> list = detailsIdSku.get(o.getId());
            o.setSkus(list);
            if (list != null && !list.isEmpty()) {
                o.setSkuCount(list.size());
                o.setWeight(list.get(0).getWeight());
                o.setCount(list.stream().filter(s -> s.getState() > 0).mapToInt(DetailsSku::getNum).sum());
            }
        });

        return orderDetails;
    }

    @Override
    public List<BFOrderDetailSku> getOrderDetailsSku(String bfId) {
        return buyForMemapper.getOrderDetailsSku(bfId);
    }

    @Override
    public int addOrderDetailsSku(BFOrderDetailSku detailSku) {
        int result = 0;
        if (detailSku.getId() == 0) {
            result = buyForMemapper.addOrderDetailsSku(detailSku);
        } else {
            result = buyForMemapper.updateOrderDetailsSku(detailSku);
        }
        buyForMemapper.updateOrdersDetailsState(detailSku.getBfDetailsId(), 1);
        buyForMemapper.updateOrdersState(detailSku.getBfId(), 1);

        return result;
    }

    @Override
    public int updateOrderDetailsSkuState(int id, int state, int bfId) {
        buyForMemapper.updateOrdersState(bfId, 1);
        return buyForMemapper.updateOrderDetailsSkuState(id, state);
    }

    @Override
    public int finshOrder(int id) {
        return buyForMemapper.updateOrdersState(id, 2);
    }

    @Override
    public Map<String, Object> getOrder(String orderNo) {
        return buyForMemapper.getOrder(orderNo);
    }

    @Override
    public int updateOrderDetailsSkuWeight(String weight, int bfdid) {
        return buyForMemapper.updateOrderDetailsSkuWeight(weight, bfdid);
    }

    @Override
    public int updateDeliveryTime(String orderNo, String time, String feight, String method, int bfid) {
        int updateDeliveryTime = buyForMemapper.updateDeliveryTime(orderNo, time, feight, method);
        // buyForMemapper.updateOrdersState(bfid, 1);
        if (updateDeliveryTime > 0) {
            String sql = "update buyforme_orderinfo set delivery_time =?,ship_feight=?,delivery_method=? where order_no=?";
            List<String> lstValue = Lists.newArrayList();
            lstValue.add(time);
            lstValue.add(feight);
            lstValue.add(method);
            lstValue.add(orderNo);
            String covertToSQL = DBHelper.covertToSQL(sql, lstValue);
            SendMQ.sendMsg(new RunSqlModel(covertToSQL));
        }
        return updateDeliveryTime;
    }

    @Override
    public int insertRemark(String orderNo, String remark) {
        return buyForMemapper.insertRemark(orderNo, remark);
    }

    @Override
    public List<Map<String, String>> getRemark(String orderNo) {
        return buyForMemapper.getRemark(orderNo);
    }

    @Override
    public List<TransportMethod> getTransport() {
        Map<String, List<String>> result = Maps.newHashMap();
        List<Map<String, String>> transport = buyForMemapper.getTransport();
        transport.stream().forEach(t -> {
            String key = t.get("shipping_time");
            List<String> value = result.get(key);
            value = value == null ? Lists.newArrayList() : value;
            value.add(t.get("transport_mode"));
            result.put(key, value);
        });
        List<TransportMethod> list = Lists.newArrayList();
        result.entrySet().forEach(r -> {
            list.add(TransportMethod.builder().time(r.getKey()).method(r.getValue()).build());
        });
        return list;
    }

    @Override
    public int updateOrdersDetailsRemark(int id, String remark) {
        int update = buyForMemapper.updateOrdersDetailsRemark(id, remark);
        if (update > 0) {
            String sql = "update buyforme_details set remark_replay=? where id=?";
            List<String> lstValue = Lists.newArrayList();
            lstValue.add(remark);
            lstValue.add(String.valueOf(id));
            String covertToSQL = DBHelper.covertToSQL(sql, lstValue);
            SendMQ.sendMsg(new RunSqlModel(covertToSQL));
        }
        return update;
    }

    @Override
    public int updateOrdersAddress(Map<String, String> map) {
        int update = buyForMemapper.updateOrdersAddress(map);
        if (update > 0) {
            String sql = "update buyforme_address set address=?,address2=?," +
                    "country=?,phone_number=?,zip_code=?,statename=?,street=?,recipients=?  where id=?";
            List<String> lstValue = Lists.newArrayList();
            lstValue.add(map.get("address"));
            lstValue.add(map.get("address2"));
            lstValue.add(map.get("country"));
            lstValue.add(map.get("phone"));
            lstValue.add(map.get("code"));
            lstValue.add(map.get("statename"));
            lstValue.add(map.get("street"));
            lstValue.add(map.get("recipients"));
            lstValue.add(map.get("id"));
            String covertToSQL = DBHelper.covertToSQL(sql, lstValue);
            SendMQ.sendMsg(new RunSqlModel(covertToSQL));
        }
        return update;
    }

    @Override
    public List<Admuser> lstAdms() {
        return buyForMemapper.lstAdms();
    }

    @Override
    public int deleteProduct(int bfdid) {
        int deleteProduct = buyForMemapper.deleteProduct(bfdid);
        if (deleteProduct > 0) {
            String sql = "update buyforme_details a left join  buyforme_details_sku b on a.id=b.bf_details_id " +
                    "set a.state=-1,b.state=-1 where a.id=" + bfdid;
            SendMQ.sendMsg(new RunSqlModel(sql));
        }
        return deleteProduct;
    }

    @Override
    public List<ZoneBean> lstCountry() {
        return buyForMemapper.lstCountry();
    }

    @Override
    public int insertBFChat(BFChat bfChat) {
        return buyForMemapper.insertBFChat(bfChat);
    }

    @Override
    public List<BFChat> queryBFChatList(BFChat bfChat) {
        return buyForMemapper.queryBFChatList(bfChat);
    }

    @Override
    public List<BuyForMeSearchLog> querySearchList(BuyForMeSearchLog searchLog) {
        List<BuyForMeSearchLog> buyForMeSearchLogs = buyForMemapper.querySearchList(searchLog);

        if (CollectionUtils.isNotEmpty(buyForMeSearchLogs)) {
            setCountryName(buyForMeSearchLogs);
        }

        return buyForMeSearchLogs;
    }

    @Override
    public int querySearchListCount(BuyForMeSearchLog searchLog) {
        return buyForMemapper.querySearchListCount(searchLog);
    }

    @Override
    public int updateSearchLogList(List<BuyForMeSearchLog> searchLogList) {

        if (CollectionUtils.isNotEmpty(searchLogList)) {
            return setCountryName(searchLogList);
        }
        return 0;
    }

    @Override
    public int cancelOrders(int id) {
        int update = buyForMemapper.updateOrderAllState(id);
        if (update > 0) {
            String sql = "update  buyforme_orderinfo a left join " +
                    " buyforme_details_sku b on a.id=b.bf_id " +
                    " left join  buyforme_details c on c.order_no=a.order_no " +
                    "set a.state=-1,b.state=-1,c.state=-1  where a.id=" + id;
            SendMQ.sendMsg(new RunSqlModel(sql));
        }
        return update;
    }


    @Override
    public JsonResult getCustomerCartDetails(String userId) {

        JsonResult jsonResult = new JsonResult();
        CommonResult commonResult = new CommonResult();
        String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId", "buy4me/" + userId);
        jsonResult.setErrorInfo("error!");
        com.alibaba.fastjson.JSONObject jsonObject;
        try {
            String requestUrl = url;
            jsonObject = instance.doGet(requestUrl);
            commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
        } catch (IOException e) {
            log.error("CartController refresh ", e);
        }
        if (commonResult.getCode() == 200) {
            String data1 = (String) commonResult.getData();
            BuyForMeStatistic data = new Gson().fromJson(data1, BuyForMeStatistic.class);
            data.getItemList().stream().forEach(e -> {
                List<CustomerQuestionsAndReplayBean> remarkReplay = e.getRemarkReplay();
                e.setRemark_replay(new Gson().toJson(remarkReplay));
            });
            jsonResult = JsonResult.success(data);
        }
        return jsonResult;
    }

    @Override
    public CommonResult putMsg(String userId, String itemid, String msg) {

        JsonResult jsonResult = new JsonResult();
        CommonResult commonResult = new CommonResult();
        String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId", "buy4me/" + userId + "/" + itemid);
        jsonResult.setErrorInfo("error!");
        com.alibaba.fastjson.JSONObject jsonObject;
        try {
            String requestUrl = url;
            Map<String, Object> map = new HashMap<>();
            map.put("msg", msg);
            jsonObject = instance.doPost(requestUrl, map);
            commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
        } catch (IOException e) {
            log.error("CartController refresh ", e);
        }
        return commonResult;
    }

    @Override
    public EasyUiJsonResult queryCustomers(ShopCarUserStatistic statistic) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        CommonResult commonResult = new CommonResult();
        String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId", "buy4me/queryAll");

        com.alibaba.fastjson.JSONObject jsonObject;
        try {
            String requestUrl = url;
            jsonObject = instance.doGet(requestUrl);
            commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
        } catch (IOException e) {
            log.error("CartController refresh ", e);
        }
        if (commonResult.getCode() == 200) {
            int userId = statistic.getUserId();
            int limitNum = statistic.getLimitNum();
            int num = statistic.getStartNum();
            String admname = statistic.getAdmname();
            List<String> list = (List<String>) commonResult.getData();
            int length = 0;
            if (list == null) {
                list = new ArrayList<>();
            }

            String s = "car:";
            Map<String, String> hasNewMsgMap = new HashMap<>();
            List<String> list1 = new ArrayList<>();
            list.stream().forEach(e -> {
                if (e.indexOf(s) > -1) {
                    e = e.split(s)[1];
                }
                if (e.indexOf(":") > -1) {
                    e = e.split(":")[0];
                    hasNewMsgMap.put(e, e);
                }
                list1.add(e);
            });
            list = this.filterHaveOrderUsers(list1);
            length = list.size();
            if (length > 1) {
                //Collections.reverse(list);
                // 分页
                if (userId != 0) {
                    list = list.stream().filter(e -> e.equals(String.valueOf(userId))).collect(Collectors.toList());
                } else if (StringUtils.isBlank(admname)) {
                    list = list.stream().skip(num).limit(limitNum).collect(Collectors.toList());
                }
            }
            List<Buy4MeCusotme> list2 = new ArrayList<>();
            list.stream().forEach(e -> {
                boolean hasMsg = false;
                if (hasNewMsgMap.containsKey(e)) {
                    hasMsg = true;
                }
                String adm = "admin(未分配)";
                Buy4MeCusotme buy4MeCusotme = new Buy4MeCusotme();
                if (StringUtils.isNumeric(e)) {
                    Map<String, String> userInfoMap = userMapper.queryAdmByUser(e);
                    if (null != userInfoMap) {
                        if (StringUtils.isNotBlank(userInfoMap.get("admName"))) {
                            adm = userInfoMap.get("admName");
                        }
                        if (StringUtils.isNotBlank(userInfoMap.get("chinapostbig"))) {
                            buy4MeCusotme.setCountry(userInfoMap.get("chinapostbig"));
                        }


                    }
                }
                buy4MeCusotme.setUserId(e);
                buy4MeCusotme.setJumpLink(e);
                buy4MeCusotme.setHasMsg(hasMsg);
                buy4MeCusotme.setAdm(adm);
                list2.add(buy4MeCusotme);
            });

            if (userId != 0) {
                //List<Buy4MeCusotme> collect = list2.stream().filter(e -> e.getUserId().equals(String.valueOf(userId))).collect(Collectors.toList());
                list2.stream().forEach(e -> {
                    JsonResult customerCartDetails = getCustomerCartDetails(e.getUserId());
                    pase(e, customerCartDetails);
                });
                json.setRows(list2);
                json.setTotal(list2.size());
            } else if (StringUtils.isNotBlank(admname)) {
                List<Buy4MeCusotme> collect = list2.stream().filter(e -> e.getAdm().equals(admname)).collect(Collectors.toList());
                collect = collect.stream().skip(num).limit(limitNum).collect(Collectors.toList());
                collect.stream().forEach(e -> {
                    JsonResult customerCartDetails = getCustomerCartDetails(e.getUserId());
                    pase(e, customerCartDetails);
                });
                json.setRows(collect);
                json.setTotal(collect.size());
            } else {
                //Collections.reverse(list2);
                list2.stream().forEach(e -> {
                    JsonResult customerCartDetails = getCustomerCartDetails(e.getUserId());
                    pase(e, customerCartDetails);
                });
                json.setRows(list2);
                json.setTotal(length);
            }

        } else {
            json.setSuccess(false);
        }
        return json;
    }

    private void pase(Buy4MeCusotme e, JsonResult customerCartDetails) {
        BuyForMeStatistic data = (BuyForMeStatistic) customerCartDetails.getData();
        e.setTotalNum(data.getTotalNum());
        e.setTotalPrice(data.getTotalPrice());
        e.setItemNum(data.getItemNum());
    }

    public List<String> filterHaveOrderUsers(List<String> allList) {
        List<String> userIdList = new ArrayList<>();
        //@date：2020/4/26 5:38 下午 Description : 获取有订单的列表
        List<String> userLists = buyForMemapper.queryAllOrderUnPay();
        allList.removeAll(userLists);
        userLists.clear();
        return allList;
    }

    private int setCountryName(List<BuyForMeSearchLog> buyForMeSearchLogs) {

        int count = 0;
        // 获取已经读取到的IP信息，放入内存中
        List<BuyForMeSearchLog> readList = buyForMeSearchLogs.stream().filter(e -> StringUtils.isNotBlank(e.getCountryName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(readList)) {
            readList.forEach(e -> {
                if (!ipMap.containsKey(e.getIp())) {
                    ipMap.put(e.getIp(), e.getCountryName());
                }
            });
            readList.clear();
        }

        Map<String, List<BuyForMeSearchLog>> listMap = buyForMeSearchLogs.stream().filter(e -> StringUtils.isBlank(e.getCountryName())).collect(Collectors.groupingBy(BuyForMeSearchLog::getIp));
        if (null != listMap && listMap.size() > 0) {
            Map<String, BuyForMeSearchLog> searchLogMap = new HashMap<>();
            listMap.forEach((k, v) -> {
                if (StringUtils.isNotBlank(k)) {
                    String ip = k;
                    if (ip.contains("192.168") || ip.contains("127.0") || ip.contains("0:0:0:0:0:0:0:1")) {
                        ip = "2.24.0.0";
                    }
                    if (ipMap.containsKey(ip)) {
                        String data = ipMap.get(ip);
                        v.forEach(cl -> cl.setCountryName(data));
                        if (!searchLogMap.containsKey(ip)) {
                            BuyForMeSearchLog tempLog = new BuyForMeSearchLog();
                            tempLog.setIp(ip);
                            tempLog.setCountryName(data);
                            searchLogMap.put(ip, tempLog);
                        }
                    } else {
                        String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId", "queryNameByIp?ip=" + ip);
                        CommonResult commonResult = null;
                        try {
                            String requestUrl = url;
                            JSONObject jsonObject = instance.doGet(requestUrl);
                            commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                            log.error("CartController refresh ", e);
                        }
                        if (null != commonResult && commonResult.getCode() == 200) {
                            String data = (String) commonResult.getData();
                            System.err.println(ip + "->" + data);
                            ipMap.put(ip, data);
                            v.forEach(cl -> cl.setCountryName(data));
                            if (!searchLogMap.containsKey(ip)) {
                                BuyForMeSearchLog tempLog = new BuyForMeSearchLog();
                                tempLog.setIp(ip);
                                tempLog.setCountryName(data);
                                searchLogMap.put(ip, tempLog);
                            }
                        }
                    }

                }
            });
            listMap.clear();
            count = searchLogMap.size();
            if (count > 0) {
                buyForMemapper.updateSearchLogCountry(searchLogMap.values());
                searchLogMap.clear();
            }
        }
        return count;
    }

}
