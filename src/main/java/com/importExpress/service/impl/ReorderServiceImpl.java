package com.importExpress.service.impl;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.TypeUtils;
import com.cbt.util.SerializeUtil;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.dao.SpiderMapper;
import com.cbt.website.bean.GoodsCarActiveBean;
import com.importExpress.mapper.GoodsCarconfigMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.GoodsCarconfigService;
import com.importExpress.service.OrderSplitService;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.RedisModel;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * *****************************************************************************************
 *
 * @ClassName ReorderServiceImpl
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2018/11/10 16:20:09
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       16:20:092018/11/10     cjc                       初版
 * ******************************************************************************************
 */
@Service
public class ReorderServiceImpl implements com.importExpress.service.ReorderService {
    private static final Logger logger = LogManager.getLogger(ReorderServiceImpl.class);
    @Autowired
    private SpiderMapper spiderMapper;
    /**
     * @Title: getGoodsInfoByOrderNo
     * @Author: cjc
     * @Despricetion:TODO 获取订单购物车表数据
     * @Date: 2018/11/10 16:21:27
     * @Param: [orderNo, goodsId]
     * @Return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @Override
    public List<Map<String, Object>> getGoodsInfoByOrderNo(String orderNo, String goodsId) {
        List<Map<String, Object>> listGoods =new ArrayList<Map<String, Object>>();
        if(goodsId!=null){
            String[] goodsIdArr = goodsId.split("#");
            listGoods = spiderMapper.getGoodsInfoByOrderNo(orderNo,goodsIdArr);
            return listGoods;
        }else{
            listGoods = spiderMapper.getGoodsInfoByOrderNo(orderNo,null);
            return listGoods;
        }
    }
    @Override
    public List<Map<String, Object>> getGoodsInfoByUserId(String userid) {
        List<Map<String, Object>> listGoods =new ArrayList<Map<String, Object>>();
        listGoods = spiderMapper.getGoodsInfoByUserid(userid);
        return listGoods;
    }
    @Autowired
    private OrderSplitService orderSplitService;
    @Override
    public String reorder(String orderNo, String userId) {
        String message = "Reorder failure!";
        boolean b = StringUtils.isNotBlank(orderNo) && StringUtils.isNotBlank(userId);
        if(b){
            //第一步先查询用户是否存在
            //过滤数字
            userId = userId.replaceAll("[^(0-9)]", "");
//            DataSourceSelector.set("dataSource127hop");
            UserBean userBean = orderSplitService.getUserFromId(Integer.parseInt(userId));
//            DataSourceSelector.restore();
            //查询订单是否存在
            //过滤订单数字和字母
            orderNo = orderNo.replaceAll("[^(a-zA-Z0-9\\_)]", "");
            //查询订单是否存在
//            DataSourceSelector.set("dataSource127hop");
            List<Map<String, Object>> ListGoods = this.getGoodsInfoByOrderNo(orderNo, null);
//            DataSourceSelector.restore();
            if(null == userBean){
                message = "user is null!";
            }else if(null == ListGoods){
                message = "get goods error!";
            }
            if(userBean != null && ListGoods != null){
                //新购物车中需要插入goods_car表的产品
                List<GoodsCarActiveBean> listActive_new_1 = new ArrayList<GoodsCarActiveBean>();
                List<GoodsCarShowBean> listShow_new_1 = new ArrayList<GoodsCarShowBean>();
                List<GoodsCarActiveBean> list_active = new ArrayList<>();
                List<GoodsCarShowBean> list_show = new ArrayList<>();
                int cartnumber = 0;//购物车数量
                int addAmount=0;
                Map<String, Object> mapInfo = null;
                Long start = 0l;
                Long end = 0l;
                if(ListGoods!=null){
                    start = System.currentTimeMillis();

                    List<String> goodsList=new ArrayList<>(20);
                    for(Map item:ListGoods){
                        goodsList.add((String)item.get("itemId"));
                    }

                    // List<String> offShelfGoodsList = spiderService.getOffShelfGoods(goodsList);
                    for(int k=0;k<ListGoods.size();k++){
                        Map<String,Object> goodMap = ListGoods.get(k);
                        if(goodMap==null){
                            logger.error("Reorder-Exception:userId:||goods_car_all视图无数据");
                            continue;
                        }else{
                            if(goodMap.get("googs_number") == null || (int)goodMap.get("googs_number") == 0) {
                                logger.error("Reorder-Exception:userId:"+userId+"||googs_number为0，reorder数据异常！");
                                continue;
                            }else if(goodMap.get("goodsUrlMD5") != null&&String.valueOf(goodMap.get("goodsUrlMD5")).substring(0,1).equalsIgnoreCase("A")) {
                                //去掉ali商品
                                continue;
                            }
                        }
                        boolean upDateFlag = false;
                        String priceListTem = "";
                        String numTem = "";
                        String priceTem = "";
                        int originalPriceListSize = 0;
                        String originalPrice = "";
                        String pid = (String)goodMap.get("itemId");
                        //获取产品所有id
                        String catids = (String)goodMap.get("bizPriceDiscount");
                        Integer isFreeShipProduct = 0;
                        if(goodMap.get("isFreeShipProduct")!=null){
                            isFreeShipProduct = (Integer)goodMap.get("isFreeShipProduct");
                        }

                        GoodsCarActiveBean goodsActiveInfo = new GoodsCarActiveBean();//经常更新且参与价格计算的购物车数据
                        GoodsCarShowBean goodsCarShowInfo = new GoodsCarShowBean();//页面显示的购物车数据
                        goodsCarShowInfo.setUserid(Integer.parseInt(userId));
                        String goodsUuid = TypeUtils.itemIDToUUID((String)goodMap.get("itemId"), TypeUtils.urlToSourceType((String) goodMap.get("goods_url")));
                        String startBizFactoryPrice = null;
                        Double classDiscountDepositRate = 1d;
                        String goodsUrl = (String) goodMap.get("goods_url");
                        String shopId = (String) goodMap.get("shopId");
                        double firstprice = 0;
                        if(goodMap.get("firstprice") != null){
                            firstprice = (double)goodMap.get("firstprice");
                        }
                        if(firstprice==0){
                            firstprice = Double.parseDouble((String)goodMap.get("googs_price"));
                        }
                        double notFreePrice1 = goodMap.get("notfreeprice")==null ? 0 : (double)goodMap.get("notfreeprice") ;
                        int goods_number = (int) goodMap.get("googs_number");
                        String goods_img = (String) goodMap.get("googs_img");
                        goods_img = goods_img==null?goods_img:goods_img.replaceAll("(//1(\\.\\d+)*/bmi/)", "//");
                        String goods_title = ((String) goodMap.get("goods_title")).replace("\"", "");
                        String remark = (String) goodMap.get("remark");
                        String norm_least = (String) goodMap.get("norm_least");
                        String delivery_time = StringUtils.isNotBlank((String)goodMap.get("delivery_time")) ? (String)goodMap.get("delivery_time"):"9-15";
                        double price4 = goodMap.get("price4")!=null ? (double)goodMap.get("price4"):0;//添加购物车时，产品显示的免邮价
                        String comparealiPrice = goodMap.get("comparealiPrice")!=null ? (String)goodMap.get("comparealiPrice"):"0";
                        double freight = goodMap.get("freight")!=null ? (double)goodMap.get("freight"):0;//添加购物车时，产品的邮费
                        String sellUnit = (String) goodMap.get("seilUnit");
                        String goodsUnit = (String) goodMap.get("goodsUnit");
                        String width = (String) goodMap.get("width");//体积
                        String perWeight = (String) goodMap.get("perWeight");//单个产品重量,没有单位
                        String guid = (String) goodMap.get("guid");
                        //如果用户添加购物车所选的数量低于本数量 需要额外掏钱
                        int sampleMoq = (Integer) goodMap.get("sampleMoq") == null ? 0 : (Integer) goodMap.get("sampleMoq");
                        // 样品费用
                        double sampleFee = (Double) goodMap.get("sampleFee") == null ? 0.00 : (Double) goodMap.get("sampleFee");
                        //库存标识
                        int isStockFlag = (Integer) goodMap.get("is_stock_flag") == null ? 0 : (Integer) goodMap.get("is_stock_flag");
                        //GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(pid,String.valueOf(isStockFlag));
                        int shopCount = (Integer) goodMap.get("shopCount") == null ? 0 : (Integer) goodMap.get("shopCount");

                        if(perWeight != null && !"".equals(perWeight) && perWeight.toLowerCase().contains("kg")){
                            perWeight = perWeight.toLowerCase().replace("kg", "");
                        }
                        int goods_class = goodMap.get("goods_class")!=null?(int) goodMap.get("goods_class"):0;//商品类别
                        double spiderPrice = goodMap.get("spiderPrice")!=null?(double) goodMap.get("spiderPrice"):0;
                        String priceListSizeStr = StrUtils.object2Str(goodMap.get("priceListSize"));
                        String price_list = "";
                        if(priceListSizeStr.split("@@@@@").length==2){
                            price_list = priceListSizeStr.split("@@@@@")[1];
                            priceListSizeStr = priceListSizeStr.split("@@@@@")[0];
                        }
                        if(priceListSizeStr.split("@@@@@").length==1){
                            priceListSizeStr = priceListSizeStr.split("@@@@@")[0];
                        }
                        if(upDateFlag){
                            price_list = priceListTem;
                            priceListSizeStr = String.valueOf(originalPriceListSize);
                        }
                        priceListSizeStr = priceListSizeStr.replaceAll("(\\.\\d+)", "");
                        int priceListSize = org.apache.commons.lang.StringUtils.isBlank(priceListSizeStr) ? 0 : Integer.valueOf(priceListSizeStr);
                        startBizFactoryPrice = String.valueOf(notFreePrice1);
                        delivery_time = StringUtils.isNotBlank(delivery_time) ? delivery_time : "5";
                        String minTime = "3";
                        String maxTime = "6";
                        if(delivery_time.split("-").length==1){
                            maxTime = delivery_time.split("-")[0];
                        }else if(delivery_time.split("-").length==2){
                            minTime = delivery_time.split("-")[0];
                            maxTime = delivery_time.split("-")[1];
                        }
                        goodsActiveInfo.setGoodsUrlMD5(goodsUuid);
                        goodsActiveInfo.setItemId((String)goodMap.get("itemId"));//商品id
                        if(upDateFlag){
                            goodsActiveInfo.setPrice(originalPrice);
                        }else {
                            goodsActiveInfo.setPrice((String)goodMap.get("googs_price"));

                        }
                        goodsActiveInfo.setFreight(0);
                        goodsActiveInfo.setNumber(goods_number);
                        goodsActiveInfo.setNorm_least(norm_least);
                        goodsActiveInfo.setStartBizFactoryPrice(startBizFactoryPrice);
                        goodsActiveInfo.setCategoryDiscountRate(classDiscountDepositRate!=null?Double.valueOf(classDiscountDepositRate):1.0);
                        goodsActiveInfo.setPerWeight(perWeight);
                        goodsActiveInfo.setMethod_feight((double)goodMap.get("method_feight"));
                        goodsActiveInfo.setIsBattery((int) goodMap.get("isBattery"));
                        goodsActiveInfo.setGoods_class(goods_class);
                        goodsActiveInfo.setSeilUnit(sellUnit);
                        goodsActiveInfo.setSpider_Price("0.00");
                        goodsActiveInfo.setPriceListSize(String.valueOf(priceListSize));
                        if(upDateFlag){
                            goodsActiveInfo.setFirstprice(Double.valueOf(originalPrice));
                        }else {
                            goodsActiveInfo.setFirstprice(firstprice);
                        }
                        goodsActiveInfo.setFreight(freight);
                        goodsActiveInfo.setPriceList(price_list);
                        goodsActiveInfo.setProcessingTime(maxTime);
                        goodsActiveInfo.setProcessingTimeMin(minTime);
                        goodsActiveInfo.setBizPriceDiscount(catids);
                        goodsActiveInfo.setIsFreeShipProduct(isFreeShipProduct);
                        goodsActiveInfo.setComparePrices(comparealiPrice);
                        goodsActiveInfo.setSampleFee(sampleFee);
                        goodsActiveInfo.setSampleMoq(sampleMoq);
                        goodsActiveInfo.setShopId(shopId);
                        goodsActiveInfo.setIsStockFlag(isStockFlag);
                        goodsActiveInfo.setShopCount(shopCount);

                        goodsCarShowInfo.setSkuid_1688((String)goodMap.get("skuid_1688"));
                        goodsCarShowInfo.setUrl(goodsUrl);
                        goodsCarShowInfo.setId(0);
                        goodsCarShowInfo.setShopId(shopId);//店铺id
                        goodsCarShowInfo.setSeller("dd");//店铺名称
                        goodsCarShowInfo.setName(goods_title);
                        goodsCarShowInfo.setImg_url(goods_img);
                        goodsCarShowInfo.setRemark(remark);
                        goodsCarShowInfo.setWidth(width);
                        goodsCarShowInfo.setGoodsUnit(goodsUnit);
                        goodsCarShowInfo.setTypes((String)goodMap.get("goods_type"));
                        goodsCarShowInfo.setFree_sc_days((String)goodMap.get("aliPosttime"));
                        goodsCarShowInfo.setDelivery_time(delivery_time);
                        goodsCarShowInfo.setUserid(Integer.parseInt(userId));
                        goodsCarShowInfo.setPrice4(price4);
                        goodsCarShowInfo.setComparePrices(comparealiPrice);
                        /**
                         * 与购物车原有商品合并
                         * */
                        int up = 0;//默认0,表示当前商品不存在于购物车中，如果存在为1
                        if(up==0){
                            int number1 = goodsActiveInfo.getNumber();
                            goodsActiveInfo.setFreight(freight);
                            //添加购物车
                            goodsActiveInfo.setFirstnumber(number1);
                            goodsActiveInfo.setFreeprice((double)goodMap.get("freeprice"));
                            goodsActiveInfo.setNotfreeprice(notFreePrice1);
                            goodsActiveInfo.setPrice3(notFreePrice1);
                            goodsCarShowInfo.setPrice4(price4);//添加购物车时，产品单页显示的start price
                            goodsActiveInfo.setPerWeight(perWeight);
                            String volume = ParseGoodsUrl.calculateVolume(number1, width, sellUnit, goodsUnit);
                            String weight1 = ParseGoodsUrl.calculateWeight(number1,goodsActiveInfo.getPerWeight(), sellUnit, goodsUnit);
                            goodsActiveInfo.setBulk_volume(volume);
                            goodsActiveInfo.setTotal_weight(weight1);
                            goodsActiveInfo.setTotal_weight((String)goodMap.get("total_weight"));
                            goodsActiveInfo.setGuId(guid);
                            goodsActiveInfo.setState(0);
                            goodsActiveInfo.setBizPriceDiscount(catids);
                            goodsActiveInfo.setShopId(shopId);
                            goodsCarShowInfo.setGuId(guid);
                            list_show.add(goodsCarShowInfo);
                            list_active.add(goodsActiveInfo);
                            listActive_new_1.add(goodsActiveInfo);
                            listShow_new_1.add(goodsCarShowInfo);
                        }
                        addAmount++;
                    }
                    // 向 goods_carconfig 表插入 activeList 和 showinfList 信息 在前台网站从新登陆则直接从数据库查询
                    // 要使用mq
                   /* DataSourceSelector.set("dataSource127hop");
                    int count = saveShpCarInfoToDateBase(Integer.parseInt(userId),list_active,list_show);
                    DataSourceSelector.restore();*/
                   // String sql = "update goods_carconfig set shopCarShowinfo = '"+SerializeUtil.ListToJson(list_show).replaceAll("'","") +"', shopCarinfo='"+SerializeUtil.ListToJson(list_active)+"' where userid="+userId;
                    //Added <V1.0.1> Start： cjc 2019/3/22 17:41:01 Description :使用新的数据结构
                    //step v1. @author: cjc @date：2019/3/22 17:44:19   Description : 合并
                    List<GoodsCarActiveSimplBean> list_goods = activeShowBeanToSimpleBeanAdapter(list_active,list_show);
                    String GoodsCarActiveSimplBeanJson = SerializeUtil.ListToJson(list_goods);
                    String sql = "update goods_carconfig set buyForMeCarConfig = '"+GoodsCarActiveSimplBeanJson.replaceAll("'","") +"' where userid="+userId;

                    //End：

                    try {
                        SendMQ sendMQ = new SendMQ();
                        sendMQ.sendMsg(new RunSqlModel(sql));
                        sendMQ.closeConn();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(listActive_new_1.size()>0){
                        /**
                         * 向goods_car表异步插入购物车数据
                         * */
                        List<SpiderNewBean> spidersDB = new ArrayList<SpiderNewBean>();
                        for (int i = 0; i < listActive_new_1.size(); i++) {
                            SpiderNewBean spiderBean = new SpiderNewBean();
                            GoodsCarActiveBean activeBean = listActive_new_1.get(i);
                            GoodsCarShowBean showBean = new GoodsCarShowBean();
                            for(GoodsCarShowBean gcsBean : listShow_new_1){
                                if(gcsBean.getGuId().equals(activeBean.getGuId())){
                                    showBean = gcsBean;
                                    spiderBean = combineSpdBean(activeBean,null,showBean);
                                    spidersDB.add(spiderBean);
                                    break;
                                }
                            }
                        }
                        // 要使用mq
                        int i = addGoogs_cars(Integer.valueOf(userId), spidersDB);
                        if(i>0 ){
                            message = "success!";
                        }
                    }
                }
            }
        }
        return message;
    }
    /**
     * 将,GoodsCarActiveBean，GoodsCarSaveTableBean，GoodsCarShowBean，合并为SpiderBean,注意url不参与合并，合并后的SpiderBean的url为空
     * */
    public SpiderNewBean combineSpdBean(GoodsCarActiveBean activeBean, GoodsCarSaveTableBean saveBean, GoodsCarShowBean showBean){
        SpiderNewBean spdBean = new SpiderNewBean();
        if(activeBean!=null){
            spdBean.setCategoryDiscountRate(activeBean.getCategoryDiscountRate());
            spdBean.setEs1(activeBean.getEs1());
            spdBean.setFirstnumber(activeBean.getFirstnumber());
            spdBean.setFirstprice(activeBean.getFirstprice());
            spdBean.setFreeprice(activeBean.getFreeprice());
            spdBean.setFreight(activeBean.getFreight());
            spdBean.setFreight_es1(activeBean.getFreight_es1());
            spdBean.setGuId(activeBean.getGuId());
            spdBean.setMethod_feight(activeBean.getMethod_feight());
            spdBean.setNorm_least(activeBean.getNorm_least());
            spdBean.setNotfreeprice(activeBean.getNotfreeprice());
            spdBean.setNumber(activeBean.getNumber());
            spdBean.setoNum(activeBean.getONum());
            spdBean.setPerWeight(activeBean.getPerWeight());
            spdBean.setPrice(activeBean.getPrice());
            spdBean.setPrice1(Double.parseDouble(activeBean.getPrice1()));
            spdBean.setPrice2(activeBean.getPrice2());
            spdBean.setPrice3(activeBean.getPrice3());
            spdBean.setStartBizFactoryPrice(activeBean.getStartBizFactoryPrice());
            spdBean.setState(activeBean.getState());
            spdBean.setTotal_price(activeBean.getTotal_price());
            spdBean.setTotal_weight(activeBean.getTotal_weight());
            spdBean.setBulk_volume(activeBean.getBulk_volume());
            spdBean.setSeilUnit(activeBean.getSeilUnit());
            spdBean.setItemId(activeBean.getItemId());
            spdBean.setIsBattery(activeBean.getIsBattery());
            spdBean.setGoods_class(activeBean.getGoods_class());
            spdBean.setUrlMD5(activeBean.getGoodsUrlMD5());
            spdBean.setBizPriceDiscount(activeBean.getBizPriceDiscount());
            spdBean.setSpider_Price("0.00");
            spdBean.setPriceListSize(activeBean.getPriceListSize());
            spdBean.setPrice_List(activeBean.getPriceListSize()+"@@@@@"+activeBean.getPriceList());
            spdBean.setSampleFee(activeBean.getSampleFee());
            spdBean.setSampleMoq(activeBean.getSampleMoq());
            spdBean.setIsStockFlag(activeBean.getIsStockFlag());
            spdBean.setShopCount(activeBean.getShopCount());
            if(activeBean.getIsStockFlag()==1){
                spdBean.setDelivery_time("0");
            }else{
                if(activeBean.getProcessingTimeMin()==null||"".equals(activeBean.getProcessingTimeMin())){
                    if("".equals(activeBean.getProcessingTime())||activeBean.getProcessingTime()==null){
                        spdBean.setDelivery_time("3-6");
                    }else if(activeBean.getProcessingTime().indexOf("-")>-1){
                        spdBean.setDelivery_time(activeBean.getProcessingTime());
                    }
                }else if(Integer.parseInt(activeBean.getProcessingTimeMin())>=Integer.parseInt(activeBean.getProcessingTime())){
                    spdBean.setDelivery_time(activeBean.getProcessingTime());
                }else{
                    spdBean.setDelivery_time(activeBean.getProcessingTimeMin()+"-"+activeBean.getProcessingTime());
                }
            }

            spdBean.setGroupBuyId(activeBean.getGroupBuyId());
            spdBean.setGbPrice(activeBean.getGbPrice());
            spdBean.setIsFreeShipProduct(activeBean.getIsFreeShipProduct());
        }
        if(showBean!=null){
            spdBean.setGoodsUnit(showBean.getGoodsUnit());
            spdBean.setGuId(showBean.getGuId());
            spdBean.setId(showBean.getId());
            spdBean.setImg_type(showBean.getImg_type());
            spdBean.setImg_url(showBean.getImg_url());
            spdBean.setName(showBean.getName());
            spdBean.setPrice4(showBean.getPrice4());
            spdBean.setRemark(showBean.getRemark());
            spdBean.setSessionId(showBean.getSessionId());
            spdBean.setShopId(showBean.getShopId());
            spdBean.setTypes(showBean.getTypes());
            spdBean.setUrl(showBean.getUrl());
            spdBean.setWidth(showBean.getWidth());
            spdBean.setSeller(showBean.getSeller());
            spdBean.setAliPosttime(showBean.getAliPosttime());
            spdBean.setFree_sc_days(showBean.getFree_sc_days());
            spdBean.setUserId(showBean.getUserid());
            spdBean.setGoods_catid(showBean.getGoods_catid());
            spdBean.setComparealiPrice(showBean.getComparePrices());
            spdBean.setSkuid_1688(showBean.getSkuid_1688());
            spdBean.setValid(showBean.getValid());
        }
        if(saveBean!=null){
            spdBean.setCartNumber(saveBean.getCartNumber());
            spdBean.setDeposit_rate(saveBean.getDeposit_rate());
            spdBean.setExtra_freight(saveBean.getExtra_freight());
            spdBean.setFeeprice(saveBean.getFeeprice());
            spdBean.setFree_shopping_company(saveBean.getFree_shopping_company());
            spdBean.setFreight_free(saveBean.getFreight_free());
            spdBean.setFreight_upgrade(saveBean.getFreight_upgrade());
            spdBean.setGoodsdata_id(saveBean.getGoodsdata_id());
            spdBean.setIsFeight(saveBean.getIsFeight());
            spdBean.setIsshipping_promote(saveBean.getIsshipping_promote());
            spdBean.setIsValid(saveBean.getIsValid());
            spdBean.setIsvolume(saveBean.getIsvolume());
            spdBean.setNorm_least1(saveBean.getNorm_least1());
            spdBean.setNorm_most(saveBean.getNorm_most());
            spdBean.setOffPrice(saveBean.getOffPrice());
            spdBean.setPay_price(saveBean.getPay_price());
            spdBean.setPreferential(saveBean.getPreferential());
            spdBean.setPreshopping(saveBean.getPreshopping());
            spdBean.setpWprice(saveBean.getpWprice());
            spdBean.setShipping_cost(saveBean.getShipping_cost());
            spdBean.setShipping_express(saveBean.getShipping_express());
            spdBean.setSource_url(saveBean.getSource_url());
            spdBean.setTheproductfrieght(saveBean.getTheproductfrieght());
            spdBean.setTrue_shipping(saveBean.getTrue_shipping());
            spdBean.setUrl_number(saveBean.getUrl_number());
            spdBean.setUrl_sumprice(saveBean.getUrl_sumprice());
            spdBean.setWeight(saveBean.getWeight());
        }
        return spdBean;
    }
    public int addGoogs_cars(int userid,List<SpiderNewBean> spider) {
		/*Map<String, List<SpiderBean>> map = new HashMap<String, List<SpiderBean>>();
		map.put("list", spider);*/

        /**
         * 增加url的MD5指纹,goods_type多余字符过滤 qiqing 17/06/16
         * */
        if(spider!=null){
            for(int i=0;i<spider.size();i++){
                if(!(spider.get(i).getTypes() == null || spider.get(i).getTypes()=="")){
                    String	types =	spider.get(i).getTypes();
                    String types1="";
                    if (types.indexOf("<") > -1 && types.indexOf(">") > -1) {//如果没多余字符就别进for循环。
                        for(int j=1;j<5;j++){//有多余字符就过滤4次
                            if (types.indexOf("<") > -1 && types.indexOf(">") > -1) {
                                types1 = types.substring(types.indexOf("<"),types.indexOf(">")+1);
                                types = types.replace(types1, "");
                            }
                        }
                        spider.get(i).setTypes(types);
                    }
                }
            }
        }

        int res=0;
        try {
//            res = spiderMapper.batchAddGoogs_car(userid, spider);
            String sql = "insert goods_car(userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,freight,remark,datatime,itemId,shopId,norm_least,\n" + "        pWprice,sessionid,true_shipping,delivery_time,freight_free,width,perWeight,seilUnit,goodsUnit,bulk_volume,total_weight,per_weight,free_shopping_company,free_sc_days,goodsdata_id,preferential,deposit_rate,guid,goods_type,currency,feeprice,goods_class,extra_freight,source_url,catid,method_feight,isshipping_promote,price1,price2,price3,price4,theproductfrieght,notfreeprice,isvolume,freeprice,firstprice,firstnumber,state,addPrice,isFeight,isBattery,aliPosttime,goodsUrlMD5,priceListSize,skuid_1688,isFreeShipProduct,is_stock_flag,shopCount) values ";
            for (int i = 0; i < spider.size(); i++) {
                SpiderNewBean spiderNewBean = spider.get(i);
                sql += "('" + userid + "','" + spiderNewBean.getUrl() + "','" + spiderNewBean.getName().replaceAll("'","") + "','" + spiderNewBean.getSeller() + "','" + spiderNewBean.getImg_url() + "','" + spiderNewBean.getPrice() + "','" + spiderNewBean.getNumber() + "','" + spiderNewBean.getFreight() + "','" + spiderNewBean.getRemark() + "',now(),'" + spiderNewBean.getItemId() + "','" + spiderNewBean.getShopId() + "','" + spiderNewBean.getNorm_least() + "','" + spiderNewBean.getpWprice() + "','" + spiderNewBean.getSessionId() + "','" + spiderNewBean.getTrue_shipping() + "','" + spiderNewBean.getDelivery_time() + "','" + spiderNewBean.getFreight_free() + "','" + spiderNewBean.getWidth() + "','" + spiderNewBean.getPerWeight() + "','" + spiderNewBean.getSeilUnit() + "','"+ spiderNewBean.getGoodsUnit() + "','" + spiderNewBean.getBulk_volume() + "','" + spiderNewBean.getTotal_weight() + "','" + spiderNewBean.getWeight() + "','" + spiderNewBean.getFree_shopping_company() + "','" + spiderNewBean.getFree_sc_days() + "','" + spiderNewBean.getGoodsdata_id() + "','" + spiderNewBean.getPreferential() + "','" + spiderNewBean.getDeposit_rate() + "','" + spiderNewBean.getGuId() + "','" + spiderNewBean.getTypes() + "','" + spiderNewBean.getCurrency() + "','" + spiderNewBean.getFeeprice() + "','" + spiderNewBean.getGoods_class() + "','" + spiderNewBean.getExtra_freight() + "','" + spiderNewBean.getSource_url() + "','" + spiderNewBean.getGoods_catid() + "','" + spiderNewBean.getMethod_feight() + "','" + spiderNewBean.getIsshipping_promote() + "','" + spiderNewBean.getPrice1() + "','" + spiderNewBean.getPrice2() + "','" + spiderNewBean.getPrice3() + "','" + spiderNewBean.getPrice4() + "','" + spiderNewBean.getTheproductfrieght() + "','" + spiderNewBean.getNotfreeprice() + "','" + spiderNewBean.getIsvolume() + "','" + spiderNewBean.getFreeprice() + "','" + spiderNewBean.getFirstprice() + "','" + spiderNewBean.getFirstnumber() + "','" + spiderNewBean.getState() + "','" + spiderNewBean.getFreight_upgrade() + "','" + spiderNewBean.getIsFeight() + "','" + spiderNewBean.getIsBattery() + "','" + spiderNewBean.getAliPosttime() + "','" + spiderNewBean.getUrlMD5() + "','" + spiderNewBean.getPrice_List() + "','" + spiderNewBean.getSkuid_1688() + "','" + spiderNewBean.getIsFreeShipProduct() + "','" + spiderNewBean.getIsStockFlag() + "','" + spiderNewBean.getShopCount() + "')";
                if (i < (spider.size() - 1)) {
                    sql += ",";
                }
            }
            SendMQ sendMQ = new SendMQ();
            sendMQ.sendMsg(new RunSqlModel(sql));
            res = 1;
            sendMQ.closeConn();
        } catch (Exception e) {
            logger.error("GoodsAddThread:批量插入goods_car表出错!", e);
            logger.error("------商品信息： " + spider.toString());
            e.printStackTrace();
        }
        for (int i = 0; i < spider.size(); i++) {
            if(StringUtils.isNotBlank(spider.get(i).getImg_type()) && res > 0){
                spiderMapper.addGoogs_carTypeimg(res, spider.get(i).getImg_type());
            }
        }
        return res;
    }
    @Autowired
    private GoodsCarconfigMapper goodsCarconfigMapper;
    @Autowired
    private GoodsCarconfigService goodsCarconfigService;
    public  int saveShpCarInfoToDateBase(int userid,List<GoodsCarActiveBean> list_active,List<GoodsCarShowBean> list_show){
        int count = 0;
        String shopcar_active = SerializeUtil.ListToJson(list_active);
        String shopcar_show = SerializeUtil.ListToJson(list_show);
        //插入数据库
        GoodsCarconfigWithBLOBs record = new GoodsCarconfigWithBLOBs();
        GoodsCarconfigExample example = new GoodsCarconfigExample();
        GoodsCarconfigExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userid);
        record.setShopcarinfo(shopcar_active);
        record.setShopcarshowinfo(shopcar_show);
        count = goodsCarconfigMapper.updateByExampleSelective(record, example);
        return count;
    }
    public static List<GoodsCarActiveSimplBean> activeShowBeanToSimpleBeanAdapter(List<GoodsCarActiveBean> list_active,List<GoodsCarShowBean> list_show){
        List<GoodsCarActiveSimplBean> redisGoodsList = new ArrayList<GoodsCarActiveSimplBean>();
        if(list_active==null||list_show==null) {
            return redisGoodsList;
        }
        if(list_active.size()==0||list_show.size()==0) {
            return redisGoodsList;
        }
        Iterator<GoodsCarActiveBean> iterator_active = list_active.iterator();
        Iterator<GoodsCarShowBean> iterator_show = list_show.iterator();
        while(iterator_active.hasNext()) {
            GoodsCarActiveBean activeBean = iterator_active.next();
            GoodsCarShowBean showBean = null;
            boolean isTrue = false;
            while(iterator_show.hasNext()) {
                showBean = iterator_show.next();
                if(activeBean!=null&&showBean!=null&&showBean.getGuId().equals(activeBean.getGuId())) {
                    isTrue = true;
                    break;
                }
            }
            if(!isTrue) {
                continue;
            }
            GoodsCarActiveSimplBean redisGoods = new GoodsCarActiveSimplBean();
            redisGoods.setBizPriceDiscount(activeBean.getBizPriceDiscount());
            redisGoods.setCategoryDiscountRate(activeBean.getCategoryDiscountRate());
            redisGoods.setComparePrices(activeBean.getComparePrices());
            redisGoods.setGbPrice(activeBean.getGbPrice());
            redisGoods.setGroupBuyId(activeBean.getGroupBuyId());
            redisGoods.setItemId(activeBean.getItemId());
            redisGoods.setNumber(activeBean.getNumber());
            redisGoods.setPrice(activeBean.getPrice());
            redisGoods.setPrice1(String.valueOf(activeBean.getPrice1()));
            redisGoods.setPrice2(activeBean.getPrice2());
            redisGoods.setPrice3(activeBean.getPrice3());
            redisGoods.setPrice4(showBean.getPrice4());
            redisGoods.setPriceList(activeBean.getPriceList());
            redisGoods.setPriceListSize(activeBean.getPriceListSize());
            redisGoods.setRemark(showBean.getRemark());
            redisGoods.setSessionId(showBean.getSessionId());
            redisGoods.setShopCount(activeBean.getShopCount());
            redisGoods.setSkuid_1688(showBean.getSkuid_1688());
            redisGoods.setSpec_id(showBean.getSkuid_1688());
            redisGoods.setTypes(showBean.getTypes());
            redisGoods.setUrlMD5(activeBean.getGoodsUrlMD5());
            //redisGoods.setImg_url(showBean.getImg_url());
            if(StringUtils.isNotBlank(redisGoods.getComparePrices())) {
                if(Double.parseDouble(redisGoods.getComparePrices()) <= Double.parseDouble(redisGoods.getPrice())) {
                    redisGoods.setComparePrices("0");
                }
            }else {
                redisGoods.setComparePrices("0");
            }
            redisGoodsList.add(redisGoods);
        }
        return redisGoodsList;
    }
    @Override
    public String returnGoodsCarByUserId(String userId){
            String message = "Reorder failure!";
            boolean b = StringUtils.isNotBlank(userId);
            if(b){
                //第一步先查询用户是否存在
                //过滤数字
                userId = userId.replaceAll("[^(0-9)]", "");
//            DataSourceSelector.set("dataSource127hop");
                UserBean userBean = orderSplitService.getUserFromId(Integer.parseInt(userId));
//            DataSourceSelector.restore();
                //查询订单是否存在
                //过滤订单数字和字母
                //查询订单是否存在
//            DataSourceSelector.set("dataSource127hop");
                List<Map<String, Object>> ListGoods = this.getGoodsInfoByUserId(userId);
//            DataSourceSelector.restore();
                if(null == userBean){
                    message = "user is null!";
                }else if(null == ListGoods){
                    message = "get goods error!";
                }
                if(userBean != null && ListGoods != null){
                    //新购物车中需要插入goods_car表的产品
                    List<GoodsCarActiveBean> listActive_new_1 = new ArrayList<GoodsCarActiveBean>();
                    List<GoodsCarShowBean> listShow_new_1 = new ArrayList<GoodsCarShowBean>();
                    List<GoodsCarActiveBean> list_active = new ArrayList<>();
                    List<GoodsCarShowBean> list_show = new ArrayList<>();
                    int cartnumber = 0;//购物车数量
                    int addAmount=0;
                    Map<String, Object> mapInfo = null;
                    Long start = 0l;
                    Long end = 0l;
                    if(ListGoods!=null){
                        start = System.currentTimeMillis();

                        List<String> goodsList=new ArrayList<>(20);
                        for(Map item:ListGoods){
                            goodsList.add((String)item.get("itemId"));
                        }

                        // List<String> offShelfGoodsList = spiderService.getOffShelfGoods(goodsList);
                        for(int k=0;k<ListGoods.size();k++){
                            Map<String,Object> goodMap = ListGoods.get(k);
                            if(goodMap==null){
                                logger.error("Reorder-Exception:userId:||goods_car_all视图无数据");
                                continue;
                            }else{
                                if(goodMap.get("googs_number") == null || (int)goodMap.get("googs_number") == 0) {
                                    logger.error("Reorder-Exception:userId:"+userId+"||googs_number为0，reorder数据异常！");
                                    continue;
                                }else if(goodMap.get("goodsUrlMD5") != null&&String.valueOf(goodMap.get("goodsUrlMD5")).substring(0,1).equalsIgnoreCase("A")) {
                                    //去掉ali商品
                                    continue;
                                }
                            }
                            boolean upDateFlag = false;
                            String priceListTem = "";
                            String numTem = "";
                            String priceTem = "";
                            int originalPriceListSize = 0;
                            String originalPrice = "";
                            String pid = (String)goodMap.get("itemId");
                            //获取产品所有id
                            String catids = (String)goodMap.get("bizPriceDiscount");
                            Integer isFreeShipProduct = 0;
                            if(goodMap.get("isFreeShipProduct")!=null){
                                isFreeShipProduct = (Integer)goodMap.get("isFreeShipProduct");
                            }

                            GoodsCarActiveBean goodsActiveInfo = new GoodsCarActiveBean();//经常更新且参与价格计算的购物车数据
                            GoodsCarShowBean goodsCarShowInfo = new GoodsCarShowBean();//页面显示的购物车数据
                            goodsCarShowInfo.setUserid(Integer.parseInt(userId));
                            String goodsUuid = TypeUtils.itemIDToUUID((String)goodMap.get("itemId"), TypeUtils.urlToSourceType((String) goodMap.get("goods_url")));
                            String startBizFactoryPrice = null;
                            Double classDiscountDepositRate = 1d;
                            String goodsUrl = (String) goodMap.get("goods_url");
                            String shopId = (String) goodMap.get("shopId");
                            double firstprice = 0;
                            if(goodMap.get("firstprice") != null){
                                firstprice = (double)goodMap.get("firstprice");
                            }
                            if(firstprice==0){
                                firstprice = Double.parseDouble((String)goodMap.get("googs_price"));
                            }
                            double notFreePrice1 = goodMap.get("notfreeprice")==null ? 0 : (double)goodMap.get("notfreeprice") ;
                            int goods_number = (int) goodMap.get("googs_number");
                            String goods_img = (String) goodMap.get("googs_img");
                            goods_img = goods_img==null?goods_img:goods_img.replaceAll("(//1(\\.\\d+)*/bmi/)", "//");
                            String goods_title = ((String) goodMap.get("goods_title")).replace("\"", "");
                            String remark = (String) goodMap.get("remark");
                            String norm_least = (String) goodMap.get("norm_least");
                            String delivery_time = StringUtils.isNotBlank((String)goodMap.get("delivery_time")) ? (String)goodMap.get("delivery_time"):"9-15";
                            double price4 = goodMap.get("price4")!=null ? (double)goodMap.get("price4"):0;//添加购物车时，产品显示的免邮价
                            String comparealiPrice = goodMap.get("comparealiPrice")!=null ? (String)goodMap.get("comparealiPrice"):"0";
                            double freight = goodMap.get("freight")!=null ? (double)goodMap.get("freight"):0;//添加购物车时，产品的邮费
                            String sellUnit = (String) goodMap.get("seilUnit");
                            String goodsUnit = (String) goodMap.get("goodsUnit");
                            String width = (String) goodMap.get("width");//体积
                            String perWeight = (String) goodMap.get("perWeight");//单个产品重量,没有单位
                            String guid = (String) goodMap.get("guid");
                            //如果用户添加购物车所选的数量低于本数量 需要额外掏钱
                            int sampleMoq = (Integer) goodMap.get("sampleMoq") == null ? 0 : (Integer) goodMap.get("sampleMoq");
                            // 样品费用
                            double sampleFee = (Double) goodMap.get("sampleFee") == null ? 0.00 : (Double) goodMap.get("sampleFee");
                            //库存标识
                            int isStockFlag = (Integer) goodMap.get("is_stock_flag") == null ? 0 : (Integer) goodMap.get("is_stock_flag");
                            //GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(pid,String.valueOf(isStockFlag));
                            int shopCount = (Integer) goodMap.get("shopCount") == null ? 0 : (Integer) goodMap.get("shopCount");

                            if(perWeight != null && !"".equals(perWeight) && perWeight.toLowerCase().contains("kg")){
                                perWeight = perWeight.toLowerCase().replace("kg", "");
                            }
                            int goods_class = goodMap.get("goods_class")!=null?(int) goodMap.get("goods_class"):0;//商品类别
                            double spiderPrice = goodMap.get("spiderPrice")!=null?(double) goodMap.get("spiderPrice"):0;
                            String priceListSizeStr = StrUtils.object2Str(goodMap.get("priceListSize"));
                            String price_list = "";
                            if(priceListSizeStr.split("@@@@@").length==2){
                                price_list = priceListSizeStr.split("@@@@@")[1];
                                priceListSizeStr = priceListSizeStr.split("@@@@@")[0];
                            }
                            if(priceListSizeStr.split("@@@@@").length==1){
                                priceListSizeStr = priceListSizeStr.split("@@@@@")[0];
                            }
                            if(upDateFlag){
                                price_list = priceListTem;
                                priceListSizeStr = String.valueOf(originalPriceListSize);
                            }
                            priceListSizeStr = priceListSizeStr.replaceAll("(\\.\\d+)", "");
                            int priceListSize = org.apache.commons.lang.StringUtils.isBlank(priceListSizeStr) ? 0 : Integer.valueOf(priceListSizeStr);
                            startBizFactoryPrice = String.valueOf(notFreePrice1);
                            delivery_time = StringUtils.isNotBlank(delivery_time) ? delivery_time : "5";
                            String minTime = "3";
                            String maxTime = "6";
                            if(delivery_time.split("-").length==1){
                                maxTime = delivery_time.split("-")[0];
                            }else if(delivery_time.split("-").length==2){
                                minTime = delivery_time.split("-")[0];
                                maxTime = delivery_time.split("-")[1];
                            }
                            goodsActiveInfo.setGoodsUrlMD5(goodsUuid);
                            goodsActiveInfo.setItemId((String)goodMap.get("itemId"));//商品id
                            if(upDateFlag){
                                goodsActiveInfo.setPrice(originalPrice);
                            }else {
                                goodsActiveInfo.setPrice((String)goodMap.get("googs_price"));

                            }
                            goodsActiveInfo.setFreight(0);
                            goodsActiveInfo.setNumber(goods_number);
                            goodsActiveInfo.setNorm_least(norm_least);
                            goodsActiveInfo.setStartBizFactoryPrice(startBizFactoryPrice);
                            goodsActiveInfo.setCategoryDiscountRate(classDiscountDepositRate!=null?Double.valueOf(classDiscountDepositRate):1.0);
                            goodsActiveInfo.setPerWeight(perWeight);
                            goodsActiveInfo.setMethod_feight((double)goodMap.get("method_feight"));
                            goodsActiveInfo.setIsBattery((int) goodMap.get("isBattery"));
                            goodsActiveInfo.setGoods_class(goods_class);
                            goodsActiveInfo.setSeilUnit(sellUnit);
                            goodsActiveInfo.setSpider_Price("0.00");
                            goodsActiveInfo.setPriceListSize(String.valueOf(priceListSize));
                            if(upDateFlag){
                                goodsActiveInfo.setFirstprice(Double.valueOf(originalPrice));
                            }else {
                                goodsActiveInfo.setFirstprice(firstprice);
                            }
                            goodsActiveInfo.setFreight(freight);
                            goodsActiveInfo.setPriceList(price_list);
                            goodsActiveInfo.setProcessingTime(maxTime);
                            goodsActiveInfo.setProcessingTimeMin(minTime);
                            goodsActiveInfo.setBizPriceDiscount(catids);
                            goodsActiveInfo.setIsFreeShipProduct(isFreeShipProduct);
                            goodsActiveInfo.setComparePrices(comparealiPrice);
                            goodsActiveInfo.setSampleFee(sampleFee);
                            goodsActiveInfo.setSampleMoq(sampleMoq);
                            goodsActiveInfo.setShopId(shopId);
                            goodsActiveInfo.setIsStockFlag(isStockFlag);
                            goodsActiveInfo.setShopCount(shopCount);

                            goodsCarShowInfo.setSkuid_1688((String)goodMap.get("skuid_1688"));
                            goodsCarShowInfo.setUrl(goodsUrl);
                            goodsCarShowInfo.setId(0);
                            goodsCarShowInfo.setShopId(shopId);//店铺id
                            goodsCarShowInfo.setSeller("dd");//店铺名称
                            goodsCarShowInfo.setName(goods_title);
                            goodsCarShowInfo.setImg_url(goods_img);
                            goodsCarShowInfo.setRemark(remark);
                            goodsCarShowInfo.setWidth(width);
                            goodsCarShowInfo.setGoodsUnit(goodsUnit);
                            goodsCarShowInfo.setTypes((String)goodMap.get("goods_type"));
                            goodsCarShowInfo.setFree_sc_days((String)goodMap.get("aliPosttime"));
                            goodsCarShowInfo.setDelivery_time(delivery_time);
                            goodsCarShowInfo.setUserid(Integer.parseInt(userId));
                            goodsCarShowInfo.setPrice4(price4);
                            goodsCarShowInfo.setComparePrices(comparealiPrice);
                            /**
                             * 与购物车原有商品合并
                             * */
                            int up = 0;//默认0,表示当前商品不存在于购物车中，如果存在为1
                            if(up==0){
                                int number1 = goodsActiveInfo.getNumber();
                                goodsActiveInfo.setFreight(freight);
                                //添加购物车
                                goodsActiveInfo.setFirstnumber(number1);
                                goodsActiveInfo.setFreeprice((double)goodMap.get("freeprice"));
                                goodsActiveInfo.setNotfreeprice(notFreePrice1);
                                goodsActiveInfo.setPrice3(notFreePrice1);
                                goodsCarShowInfo.setPrice4(price4);//添加购物车时，产品单页显示的start price
                                goodsActiveInfo.setPerWeight(perWeight);
                                String volume = ParseGoodsUrl.calculateVolume(number1, width, sellUnit, goodsUnit);
                                String weight1 = ParseGoodsUrl.calculateWeight(number1,goodsActiveInfo.getPerWeight(), sellUnit, goodsUnit);
                                goodsActiveInfo.setBulk_volume(volume);
                                goodsActiveInfo.setTotal_weight(weight1);
                                goodsActiveInfo.setTotal_weight((String)goodMap.get("total_weight"));
                                goodsActiveInfo.setGuId(guid);
                                goodsActiveInfo.setState(0);
                                goodsActiveInfo.setBizPriceDiscount(catids);
                                goodsActiveInfo.setShopId(shopId);
                                goodsCarShowInfo.setGuId(guid);
                                list_show.add(goodsCarShowInfo);
                                list_active.add(goodsActiveInfo);
                                listActive_new_1.add(goodsActiveInfo);
                                listShow_new_1.add(goodsCarShowInfo);
                            }
                            addAmount++;
                        }
                        // 向 goods_carconfig 表插入 activeList 和 showinfList 信息 在前台网站从新登陆则直接从数据库查询
                        // 要使用mq
                   /* DataSourceSelector.set("dataSource127hop");
                    int count = saveShpCarInfoToDateBase(Integer.parseInt(userId),list_active,list_show);
                    DataSourceSelector.restore();*/
                        // String sql = "update goods_carconfig set shopCarShowinfo = '"+SerializeUtil.ListToJson(list_show).replaceAll("'","") +"', shopCarinfo='"+SerializeUtil.ListToJson(list_active)+"' where userid="+userId;
                        //Added <V1.0.1> Start： cjc 2019/3/22 17:41:01 Description :使用新的数据结构
                        //step v1. @author: cjc @date：2019/3/22 17:44:19   Description : 合并
                        List<GoodsCarActiveSimplBean> list_goods = activeShowBeanToSimpleBeanAdapter(list_active,list_show);
                        String GoodsCarActiveSimplBeanJson = SerializeUtil.ListToJson(list_goods);
                        String sql = "update goods_carconfig set buyForMeCarConfig = '"+GoodsCarActiveSimplBeanJson.replaceAll("'","") +"' where userid="+userId;

                        //End：

                        try {
                            SendMQ sendMQ = new SendMQ();
                            //sendMQ.sendMsg(new RunSqlModel(sql));
                            sendMQ.closeConn();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(listActive_new_1.size()>0){
                            /**
                             * 向goods_car表异步插入购物车数据
                             * */
                            List<SpiderNewBean> spidersDB = new ArrayList<SpiderNewBean>();
                            for (int i = 0; i < listActive_new_1.size(); i++) {
                                SpiderNewBean spiderBean = new SpiderNewBean();
                                GoodsCarActiveBean activeBean = listActive_new_1.get(i);
                                GoodsCarShowBean showBean = new GoodsCarShowBean();
                                for(GoodsCarShowBean gcsBean : listShow_new_1){
                                    if(gcsBean.getGuId().equals(activeBean.getGuId())){
                                        showBean = gcsBean;
                                        spiderBean = combineSpdBean(activeBean,null,showBean);
                                        spidersDB.add(spiderBean);
                                        break;
                                    }
                                }
                            }
                            // 要使用mq
                            /*int i = addGoogs_cars(Integer.valueOf(userId), spidersDB);
                            if(i>0 ){
                                message = "success!";
                            }*/
                        }
                    }
                }
            }
            return message;
    }
}
