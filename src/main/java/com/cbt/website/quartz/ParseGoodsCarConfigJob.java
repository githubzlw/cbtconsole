package com.cbt.website.quartz;

import com.cbt.util.BigDecimalUtil;
import com.cbt.website.bean.GoodsCarActiveBean;
import com.importExpress.pojo.*;
import com.importExpress.service.GoodsCarconfigService;
import com.importExpress.service.ShopCarMarketingService;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.List;

public class ParseGoodsCarConfigJob extends QuartzJobBean {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ParseGoodsCarConfigJob.class);


    @Autowired
    private GoodsCarconfigService goodsCarconfigService;


    @Autowired
    private ShopCarMarketingService shopCarMarketingService;

    public void testFun() throws Exception {
        System.out.println("parseGoodsCarConfigJob testFun begin...");
        logger.info("parseGoodsCarConfigJob testFun begin...");
    }

    public void updateGoodsCarInfo() throws Exception {
        System.out.println("parseGoodsCarConfigJob begin...");
        logger.info("parseGoodsCarConfigJob begin...");

        try {

            GoodsCarconfigExample example = new GoodsCarconfigExample();
            GoodsCarconfigExample.Criteria configCriteria = example.createCriteria();
            configCriteria.andUseridGreaterThan(0);
            //获取所有有标识的购物车数据
            List<GoodsCarconfigWithBLOBs> list = goodsCarconfigService.queryByIsNew(0);

            //解析所有的购物车数据

            for (GoodsCarconfigWithBLOBs carconfigWithBLOBs : list) {
                if(carconfigWithBLOBs.getUserid() > 0 ){
                    if (StringUtils.isBlank(carconfigWithBLOBs.getShopcarinfo()) || StringUtils.isBlank(carconfigWithBLOBs.getShopcarshowinfo())) {
                        System.err.println("id:" + carconfigWithBLOBs.getId() + ",userId:" + carconfigWithBLOBs.getUserid() + " shopInfo empty <:<:");
                        //更新标识为已经处理
                        goodsCarconfigService.updateByIdAndUserId(carconfigWithBLOBs.getId(), carconfigWithBLOBs.getUserid());
                        continue;
                    } else {
                        System.err.println("id:" + carconfigWithBLOBs.getId() + ",userId:" + carconfigWithBLOBs.getUserid() + " shopInfo begin parse--");
                    }
                    //用于计算部分的数据
                    List<GoodsCarActiveBean> listActive = (List<GoodsCarActiveBean>) JSONArray.toCollection(JSONArray.fromObject(carconfigWithBLOBs.getShopcarinfo()), GoodsCarActiveBean.class);
                    //用于显示部分的数据
                    List<GoodsCarShowBean> listShow = (List<GoodsCarShowBean>) JSONArray.toCollection(JSONArray.fromObject(carconfigWithBLOBs.getShopcarshowinfo()), GoodsCarShowBean.class);

                    List<ShopCarMarketing> genMarketingList = new ArrayList<ShopCarMarketing>();

                    //组合成购物车数据
                    for (GoodsCarActiveBean gacb : listActive) {
                        for (GoodsCarShowBean gcsb : listShow) {
                            if (gacb.getGuId().equals(gcsb.getGuId())) {
                                genMarketingList.add(combinemarketing(gacb, gcsb));
                                break;
                            }
                        }
                    }




                    /*shopCarMarketingService.deleteMarketingByUserId(carconfigWithBLOBs.getUserid());
                    System.err.println("userId:" + carconfigWithBLOBs.getUserid() + ",insert size:" + genMarketingList.size());
                    for(ShopCarMarketing shopInsertInCar : genMarketingList){
                        shopCarMarketingService.insertSelective(shopInsertInCar);
                    }
                    genMarketingList.clear();*/


                    try {

                        //查询当前客户存在的购物车数据
                        ShopCarMarketingExample marketingExample = new ShopCarMarketingExample();
                        ShopCarMarketingExample.Criteria marketingCriteria = marketingExample.createCriteria();
                        marketingCriteria.andUseridEqualTo(carconfigWithBLOBs.getUserid());
                        List<ShopCarMarketing> shopCarMarketingList = shopCarMarketingService.selectByExample(marketingExample);

                        //删除以前数据

                        //循环判断是否要更新的数据,不更新，进行插入操作
                        List<ShopCarMarketing> upMarketingList = new ArrayList<ShopCarMarketing>();
                        List<ShopCarMarketing> insertMarketingList = new ArrayList<ShopCarMarketing>();
                        for (ShopCarMarketing shopCarMarketing : genMarketingList) {
                            int isExists = 0;
                            for (ShopCarMarketing shopCar : shopCarMarketingList) {
                                if (shopCarMarketing.getItemid().equals(shopCar.getItemid()) && shopCarMarketing.getGoodsType().equals(shopCar.getGoodsType())) {
                                    upMarketingList.add(shopCarMarketing);
                                    isExists = 1;
                                    break;
                                }
                            }
                            if (isExists == 0) {
                                insertMarketingList.add(shopCarMarketing);
                            }
                        }

                        //找到需要删除商品
                        List<ShopCarMarketing> deleteMarketingList = new ArrayList<ShopCarMarketing>();
                        for (ShopCarMarketing shopDeleteCar : shopCarMarketingList) {
                            int isDelete = 1;
                            for (ShopCarMarketing shopCarMarketing : genMarketingList) {
                                if (shopCarMarketing.getItemid().equals(shopDeleteCar.getItemid()) && shopCarMarketing.getGoodsType().equals(shopDeleteCar.getGoodsType())) {
                                    //upMarketingList.add(shopCarMarketing);
                                    isDelete = 0;
                                    break;
                                }
                            }
                            if (isDelete > 0) {
                                deleteMarketingList.add(shopDeleteCar);
                            }
                        }


                        //更新购物车数据
                        System.err.println("userId:" + carconfigWithBLOBs.getUserid() + ",update size:" + upMarketingList.size());
                        if (upMarketingList.size() > 0) {
                            /*for (ShopCarMarketing shopUpCar : upMarketingList) {
                                shopCarMarketingService.updateByPrimaryKey(shopUpCar);
                            }*/
                            shopCarMarketingService.updateShopCarBatch(upMarketingList);
                        }
                        upMarketingList.clear();
                        //插入购物车数据
                        System.err.println("userId:" + carconfigWithBLOBs.getUserid() + ",insert size:" + insertMarketingList.size());

                        if (insertMarketingList.size() > 0) {
                            shopCarMarketingService.insertShopCarBatch(insertMarketingList);
                            /*List<ShopCarMarketing> tempInsertMarketingList = new ArrayList<ShopCarMarketing>();
                            int totalCount = 0;
                            int notInsert = 0;
                            for (ShopCarMarketing shopInsertInCar : insertMarketingList) {
                                if (totalCount == 0) {
                                    tempInsertMarketingList.add(shopInsertInCar);
                                    shopCarMarketingService.insertSelective(shopInsertInCar);
                                    totalCount++;
                                } else {
                                    for (ShopCarMarketing tempShopInCar : tempInsertMarketingList) {
                                        if (tempShopInCar.getItemid().equals(shopInsertInCar.getItemid()) && tempShopInCar.getGoodsType().equals(shopInsertInCar.getGoodsType())) {
                                            notInsert = 1;
                                            break;
                                        }
                                    }
                                    if (notInsert < 1) {
                                        tempInsertMarketingList.add(shopInsertInCar);
                                        shopCarMarketingService.insertSelective(shopInsertInCar);
                                        notInsert = 0;
                                    }else{
                                        deleteMarketingList.add(shopInsertInCar);
                                    }
                                    totalCount++;
                                }
                            }
                            tempInsertMarketingList.clear();*/
                        }
                        insertMarketingList.clear();

                        //删除购物车数据
                        System.err.println("userId:" + carconfigWithBLOBs.getUserid() + ",delete size:" + deleteMarketingList.size());
                        if (deleteMarketingList.size() > 0) {
                            /*for (ShopCarMarketing deleteCar : deleteMarketingList) {
                                shopCarMarketingService.deleteByPrimaryKey(deleteCar.getId());
                            }*/
                            shopCarMarketingService.deleteShopCarBatch(deleteMarketingList);
                        }
                        deleteMarketingList.clear();
                        //更新标识为已经处理
                        goodsCarconfigService.updateByIdAndUserId(carconfigWithBLOBs.getId(), carconfigWithBLOBs.getUserid());

                        shopCarMarketingList.clear();
                        genMarketingList.clear();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    listActive.clear();
                    listShow.clear();

                }else{
                    System.err.println("无效数据,即将删除");
                    goodsCarconfigService.deleteByPrimaryKey(carconfigWithBLOBs.getId());
                }
            }

            list.clear();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("parseGoodsCarConfigJob error:" + e.getMessage());
            logger.error("parseGoodsCarConfigJob error:" + e.getMessage());
        }

        System.out.println("parseGoodsCarConfigJob end!!");
        logger.info("parseGoodsCarConfigJob end!!");
    }


    private ShopCarMarketing combinemarketing(GoodsCarActiveBean activeBean, GoodsCarShowBean showBean) {
        ShopCarMarketing marketing = new ShopCarMarketing();
        if (activeBean != null) {
            //marketing.set(activeBean.getCategoryDiscountRate());
            //marketing.setEs1(activeBean.getEs1());
            marketing.setFirstnumber(activeBean.getFirstnumber());
            marketing.setFirstprice(activeBean.getFirstprice());
            marketing.setFreeprice(activeBean.getFreeprice());
            marketing.setFreight(activeBean.getFreight());
            //marketing.setFreight_es1(activeBean.getFreight_es1());
            marketing.setGuid(activeBean.getGuId());
            marketing.setMethodFeight(activeBean.getMethod_feight());
            marketing.setNormLeast(activeBean.getNorm_least());
            marketing.setNotfreeprice(activeBean.getNotfreeprice());
            marketing.setGoogsNumber(activeBean.getNumber());
            //marketing.setoNum(activeBean.getoNum());
            marketing.setPerWeight(activeBean.getPerWeight());
            if(StringUtils.isNotBlank(activeBean.getPrice())){
                marketing.setGoogsPrice(String.valueOf(BigDecimalUtil.truncateDouble(Double.valueOf(activeBean.getPrice()), 2)));
            }else{
                marketing.setGoogsPrice("0");
            }
            marketing.setPrice1(Double.parseDouble(activeBean.getPrice1()));
            marketing.setPrice2(activeBean.getPrice2());
            marketing.setPrice3(activeBean.getPrice3());
            //marketing.setStartBizFactoryPrice(activeBean.getStartBizFactoryPrice());
            marketing.setState(activeBean.getState());
            //marketing.setTotal_price(activeBean.getTotal_price());
            marketing.setTotalWeight(activeBean.getTotal_weight());
            marketing.setBulkVolume(activeBean.getBulk_volume());
            marketing.setSeilunit(activeBean.getSeilUnit());
            marketing.setItemid(activeBean.getItemId());
            marketing.setIsbattery(activeBean.getIsBattery());
            marketing.setGoodsClass(activeBean.getGoods_class());
            marketing.setGoodsurlmd5(activeBean.getGoodsUrlMD5());
            marketing.setBizpricediscount(activeBean.getBizPriceDiscount());
            marketing.setSpiderprice(Double.valueOf(StringUtils.isBlank(activeBean.getSpider_Price()) ? "0" : activeBean.getSpider_Price()));
            //marketing.setPricelistsize(activeBean.getPriceListSize());
            marketing.setPricelistsize(activeBean.getPriceListSize() + "@@@@@" + activeBean.getPriceList());
            //marketing.setPrice_List(activeBean.getPriceListSize() + "@@@@@" + activeBean.getPriceList());
            if (StringUtils.isBlank(activeBean.getProcessingTimeMin())) {
                marketing.setDeliveryTime("3-" + (StringUtils.isBlank(activeBean.getProcessingTime()) ? "6" : activeBean.getProcessingTime()));
            } else if (Integer.parseInt(activeBean.getProcessingTimeMin()) >= Integer.parseInt(activeBean.getProcessingTime())) {
                marketing.setDeliveryTime(activeBean.getProcessingTime());
            } else {
                marketing.setDeliveryTime(activeBean.getProcessingTimeMin() + "-" + activeBean.getProcessingTime());
            }
            marketing.setGroupBuyId(activeBean.getGroupBuyId());
            //marketing.setGbPrice(activeBean.getGbPrice());
        }
        if (showBean != null) {
            marketing.setGoodsunit(showBean.getGoodsUnit());
            marketing.setGuid(showBean.getGuId());
            marketing.setId(showBean.getId());
            //marketing.setImg_type(showBean.getImg_type());
            marketing.setGoogsImg(showBean.getImg_url());
            marketing.setGoodsTitle(showBean.getName());
            marketing.setPrice4(showBean.getPrice4());
            marketing.setRemark(showBean.getRemark());
            marketing.setSessionid(showBean.getSessionId());
            marketing.setShopid(showBean.getShopId());
            marketing.setGoodsType(showBean.getTypes());
            marketing.setGoodsUrl(showBean.getUrl());
            marketing.setWidth(showBean.getWidth());
            marketing.setGoogsSeller(showBean.getSeller());
            marketing.setAliposttime(showBean.getAliPosttime());
            marketing.setFreeScDays(showBean.getFree_sc_days());
            marketing.setDeliveryTime(showBean.getDelivery_time());
            marketing.setUserid(showBean.getUserid());
            marketing.setCatid(showBean.getGoods_catid());
            marketing.setSkuid1688(showBean.getSkuid_1688());
        }
        return marketing;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    }

}
