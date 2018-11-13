package com.importExpress.controller;

import com.cbt.userinfo.service.IUserInfoService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.GoodsCarActiveBeanUpdate;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.GoodsCarShowBean;
import com.importExpress.pojo.GoodsCarconfigWithBLOBs;
import com.importExpress.pojo.ShopCarMarketing;
import com.importExpress.pojo.ShopCarMarketingExample;
import com.importExpress.service.GoodsCarconfigService;
import com.importExpress.service.ShopCarMarketingService;
import com.importExpress.utli.RedisModel;
import com.importExpress.utli.SendEmailNew;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopCarMarketingCtr")
public class ShopCarMarketingController {
    private static final Log logger = LogFactory.getLog(ShopCarMarketingController.class);


    @Autowired
    private GoodsCarconfigService goodsCarconfigService;

    @Autowired
    private ShopCarMarketingService shopCarMarketingService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private SendEmailNew sendEmailNew;


    @RequestMapping("/queryCarInfoByUserId")
    public ModelAndView queryCarInfoByUserId(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsCarInfo");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("message", "用户未登录");
            mv.addObject("success", 0);
            return mv;
        }
        String userIdStr = request.getParameter("userId");
        if (StringUtils.isBlank(userIdStr)) {
            mv.addObject("message", "获取用户ID失败");
            mv.addObject("success", 0);
            return mv;
        } else {
            mv.addObject("userId", userIdStr);
        }
        try {
            //查询当前客户存在的购物车数据
            ShopCarMarketingExample marketingExample = new ShopCarMarketingExample();
            ShopCarMarketingExample.Criteria marketingCriteria = marketingExample.createCriteria();
            marketingCriteria.andUseridEqualTo(Integer.valueOf(userIdStr));
            List<ShopCarMarketing> shopCarMarketingList = shopCarMarketingService.selectByExample(marketingExample);
            //格式化处理规格数据
            for (ShopCarMarketing shopCar : shopCarMarketingList) {
                String tempType = "";
                if (StringUtils.isNotBlank(shopCar.getGoodsType())) {
                    String[] splitFirst = shopCar.getGoodsType().split(",");
                    for (String spCh : splitFirst) {
                        String[] spScList = spCh.split("@");
                        tempType += spScList[0] + ";";
                        spScList = null;
                    }
                    splitFirst = null;
                }
                if (tempType.length() > 0) {
                    shopCar.setGoodsType(tempType);
                }
            }
            mv.addObject("success", 1);
            mv.addObject("list", shopCarMarketingList);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("goodsCarInfo error:" + e.getMessage());
            logger.error("goodsCarInfo error:" + e.getMessage());
            mv.addObject("message", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("success", 0);
        }
        return mv;
    }


    @RequestMapping("/updateCarGoodsPriceByUserId")
    @ResponseBody
    public JsonResult updateCarGoodsPriceByUserId(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("用户未登录");
            return json;
        }
        String userIdStr = request.getParameter("userId");
        if (StringUtils.isBlank(userIdStr)) {
            json.setOk(false);
            json.setMessage("获取用户ID失败");
            return json;
        }

        String goodsIdStr = request.getParameter("goodsId");
        if (StringUtils.isBlank(goodsIdStr)) {
            json.setOk(false);
            json.setMessage("获取goodsId失败");
            return json;
        }
        String goodsPriceStr = request.getParameter("goodsPrice");
        if (StringUtils.isBlank(goodsPriceStr) || "0".equals(goodsPriceStr)) {
            json.setOk(false);
            json.setMessage("获取原始价格失败");
            return json;
        }
        String newPriceStr = request.getParameter("newPrice");
        if (StringUtils.isBlank(newPriceStr) || "0".equals(newPriceStr)) {
            json.setOk(false);
            json.setMessage("获取修改价格失败");
            return json;
        }
        try {
            int count = 0;
            count = shopCarMarketingService.updateGoodsCarPrice(Integer.valueOf(goodsIdStr), Integer.valueOf(userIdStr), Double.valueOf(goodsPriceStr), Double.valueOf(newPriceStr));
            if (count > 0) {
                //更新成功后，发布邮件的时候全部更新线上
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("更新失败,请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("userId:" + userIdStr + ",goodsId:" + goodsIdStr + ",updateCarGoodsPriceByUserId error:" + e.getMessage());
            logger.error("userId:" + userIdStr + ",goodsId:" + goodsIdStr + ",updateCarGoodsPriceByUserId error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("执行失败，原因：" + e.getMessage());
            return json;
        }
        return json;
    }


    @RequestMapping("/sendEmailCarInfoByUserId")
    public ModelAndView sendEmailCarInfoByUserId(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsCarInfoEmail");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("message", "用户未登录");
            mv.addObject("success", 0);
            return mv;
        }
        String userIdStr = request.getParameter("userId");
        if (StringUtils.isBlank(userIdStr)) {
            mv.addObject("message", "获取用户ID失败");
            mv.addObject("success", 0);
            return mv;
        } else {
            mv.addObject("userId", userIdStr);
        }
        try {
            //查询客户信息
            Map<String, Object> listu = userInfoService.getUserCount(Integer.valueOf(userIdStr));
            mv.addObject("userEmail", listu.get("email"));
            listu.clear();
            //查询当前客户存在的购物车数据
            ShopCarMarketingExample marketingExample = new ShopCarMarketingExample();
            ShopCarMarketingExample.Criteria marketingCriteria = marketingExample.createCriteria();
            marketingCriteria.andUseridEqualTo(Integer.valueOf(userIdStr));
            List<ShopCarMarketing> shopCarMarketingList = shopCarMarketingService.selectByExample(marketingExample);
            //格式化处理规格数据
            double productCost = 0;
            double actualCost = 0;
            List<ShopCarMarketing> resultList = new ArrayList<ShopCarMarketing>();
            List<ShopCarMarketing> sourceList = new ArrayList<ShopCarMarketing>();
            for (ShopCarMarketing shopCar : shopCarMarketingList) {
                String tempType = "";
                if (StringUtils.isNotBlank(shopCar.getGoodsType())) {
                    String[] splitFirst = shopCar.getGoodsType().split(",");
                    for (String spCh : splitFirst) {
                        String[] spScList = spCh.split("@");
                        tempType += spScList[0] + ";";
                        spScList = null;
                    }
                    splitFirst = null;
                }
                if (tempType.length() > 0) {
                    shopCar.setGoodsType(tempType);
                }
                if (shopCar.getPrice1() > 0) {
                    double totalPrice = Double.valueOf(shopCar.getGoogsPrice()) * shopCar.getGoogsNumber();
                    productCost += shopCar.getPrice1() * shopCar.getGoogsNumber();
                    actualCost += totalPrice;
                    shopCar.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
                    resultList.add(shopCar);
                }else{
                    double totalPrice = Double.valueOf(shopCar.getGoogsPrice()) * shopCar.getGoogsNumber();
                    shopCar.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
                    sourceList.add(shopCar);
                }
            }
            shopCarMarketingList.clear();
            if (resultList.size() > 0) {
                double offCost = productCost - actualCost;
                mv.addObject("productCost", BigDecimalUtil.truncateDouble(productCost, 2));
                mv.addObject("actualCost", BigDecimalUtil.truncateDouble(actualCost, 2));
                mv.addObject("offRate", BigDecimalUtil.truncateDouble((offCost) / productCost * 100, 2));
                mv.addObject("success", 1);
                mv.addObject("offCost", BigDecimalUtil.truncateDouble(offCost, 2));
                mv.addObject("updateList", resultList);
                mv.addObject("sourceList", sourceList);
            } else {
                mv.addObject("message", "未设置商品价格，请先设置后打开此页面");
                mv.addObject("success", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendEmailCarInfoByUserId error:" + e.getMessage());
            logger.error("sendEmailCarInfoByUserId error:" + e.getMessage());
            mv.addObject("message", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("success", 0);
        }
        return mv;
    }


    @RequestMapping("/confirmAndSendEmail")
    @ResponseBody
    public JsonResult confirmAndSendEmail(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("用户未登录");
            return json;
        }
        String userIdStr = request.getParameter("userId");
        if (StringUtils.isBlank(userIdStr)) {
            json.setOk(false);
            json.setMessage("获取用户ID失败");
            return json;
        }

        String emailContent = request.getParameter("emailContent");
        if (StringUtils.isBlank(emailContent)) {
            json.setOk(false);
            json.setMessage("获取邮件内容失败");
            return json;
        }

        String userEmail = request.getParameter("userEmail");
        if (StringUtils.isBlank(userEmail)) {
            //查询客户信息
            Map<String, Object> listu = userInfoService.getUserCount(Integer.valueOf(userIdStr));
            userEmail = listu.get("email").toString();
            listu.clear();
        }
        try {


            //1.重新生成goods_carconfig数据，并进行保存
            List<GoodsCarShowBean> showList = new ArrayList<GoodsCarShowBean>();
            List<GoodsCarActiveBeanUpdate> activeList = new ArrayList<GoodsCarActiveBeanUpdate>();
            ShopCarMarketingExample marketingExample = new ShopCarMarketingExample();
            ShopCarMarketingExample.Criteria marketingCriteria = marketingExample.createCriteria();
            marketingCriteria.andUseridEqualTo(Integer.valueOf(userIdStr));
            List<ShopCarMarketing> shopCarMarketingList = shopCarMarketingService.selectByExample(marketingExample);
            for (ShopCarMarketing shopCar : shopCarMarketingList) {
                //解析数据
                showList.add(genShowBeanBeanByShopCar(shopCar));
                activeList.add(genActiveBeanByShopCar(shopCar));
            }
            boolean isSuccess = updateGoodsCarConfig(showList, activeList, Integer.valueOf(userIdStr));
            //更新成功后，发布邮件的时候全部更新线上
            if (isSuccess) {
                //2.清空redis数据
                //使用MQ清空购物车数据
                //redis示例
                SendMQ sendMQ = new SendMQ();
                sendMQ.sendMsg(new RedisModel(new String[]{userIdStr}));
                sendMQ.closeConn();
                //3.发送邮件给客户
                String title = "We have reduced price for your ImportExpress shopping cart selection!";
                sendEmailNew.send(user.getEmail(), "", userEmail, emailContent, title, "", 1);
                json.setOk(true);
                json.setMessage("发送邮件成功！");
            } else {
                json.setOk(false);
                json.setMessage("更新失败,请重试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("userId:" + userIdStr + ",confirmAndSendEmail error:" + e.getMessage());
            logger.error("userId:" + userIdStr + ",confirmAndSendEmail error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("执行失败，原因：" + e.getMessage());
            return json;
        }
        return json;
    }


    private boolean updateGoodsCarConfig(List<GoodsCarShowBean> showList, List<GoodsCarActiveBeanUpdate> activeList, int userId) {

        GoodsCarconfigWithBLOBs record = new GoodsCarconfigWithBLOBs();
        String shopcar_active = activeList.toString();
        record.setShopcarinfo(shopcar_active);

        String shopcar_show = showList.toString();
        record.setShopcarshowinfo(shopcar_show);
        record.setUserid(userId);
        return goodsCarconfigService.updateGoodsCarConfig(record);
    }


    /**
     * ShopCarMarketing反解析GoodsCarShowBean
     *
     * @param shopCar
     * @return
     */
    private GoodsCarShowBean genShowBeanBeanByShopCar(ShopCarMarketing shopCar) {

        GoodsCarShowBean showBean = new GoodsCarShowBean();

        //showBean设置
        showBean.setGoodsUnit(shopCar.getGoodsunit());
        showBean.setGuId(shopCar.getGuid());
        showBean.setId(shopCar.getId());
        //showBean.setImg_type(shopCar.getImg_type() == null ? "" : shopCar.getImg_type());
        showBean.setImg_url(shopCar.getGoogsImg());
        showBean.setName(shopCar.getGoodsTitle());
        showBean.setPrice4(shopCar.getPrice4() == null ? 0 : shopCar.getPrice4());
        showBean.setRemark(shopCar.getRemark());
        showBean.setSessionId(shopCar.getSessionid());
        showBean.setShopId(shopCar.getShopid());
        showBean.setTypes(shopCar.getGoodsType());
        showBean.setUrl(shopCar.getGoodsUrl() == null ? "" : shopCar.getGoodsUrl());
        showBean.setWidth(shopCar.getWidth());
        showBean.setSeller(shopCar.getGoogsSeller());
        showBean.setAliPosttime(shopCar.getAliposttime() == null ? "" : shopCar.getAliposttime());
        showBean.setFree_sc_days(shopCar.getFreeScDays());
        showBean.setDelivery_time(shopCar.getDeliveryTime());
        showBean.setUserid(shopCar.getUserid());
        showBean.setGoods_catid(shopCar.getCatid() == null ? "" : shopCar.getCatid());
        showBean.setSkuid_1688(shopCar.getSkuid1688());
        showBean.setComparePrices(shopCar.getComparealiprice() == null ? "" : shopCar.getComparealiprice());

        return showBean;
    }


    /**
     * ShopCarMarketing反解析GoodsCarActiveBean
     *
     * @param shopCar
     * @return
     */
    private GoodsCarActiveBeanUpdate genActiveBeanByShopCar(ShopCarMarketing shopCar) {

        GoodsCarActiveBeanUpdate activeBean = new GoodsCarActiveBeanUpdate();
        //activeBean设置
        //activeBean.setCategoryDiscountRate(shopCar.getCategoryDiscountRate());
        //activeBean.setEs1(shopCar.getEs1());
        activeBean.setFirstnumber(shopCar.getFirstnumber());
        activeBean.setFirstprice(shopCar.getFirstprice());
        activeBean.setFreeprice(shopCar.getFreeprice());
        activeBean.setFreight(shopCar.getFreight());
        //activeBean.setFreight_es1(shopCar.getFreight_es1());
        activeBean.setGuId(shopCar.getGuid());
        activeBean.setMethod_feight(shopCar.getMethodFeight());
        activeBean.setMethod_day(shopCar.getDeliveryTime());
        activeBean.setNorm_least(shopCar.getNormLeast());
        activeBean.setNotfreeprice(shopCar.getNotfreeprice());
        activeBean.setNumber(shopCar.getGoogsNumber());
        //activeBean.setoNum(shopCar.getoNum());
        activeBean.setPerWeight(shopCar.getPerWeight());
        activeBean.setPrice(shopCar.getGoogsPrice());
        if(shopCar.getPrice1() > 0){
            activeBean.setPrice1(shopCar.getPrice1() + "@1");
        }else{
            activeBean.setPrice1("0");
        }
        activeBean.setPrice2(shopCar.getPrice2());
        activeBean.setPrice3(shopCar.getPrice3());
        //activeBean.setStartBizFactoryPrice(shopCar.getStartBizFactoryPrice() == null ? "" : shopCar.getStartBizFactoryPrice());
        activeBean.setState(shopCar.getState());
        activeBean.setTotal_price(shopCar.getTotalPrice());
        activeBean.setTotal_weight(shopCar.getTotalWeight());
        activeBean.setBulk_volume(shopCar.getBulkVolume());
        activeBean.setSeilUnit(shopCar.getSeilunit());
        activeBean.setItemId(shopCar.getItemid());
        activeBean.setIsBattery(shopCar.getIsbattery());
        activeBean.setGoods_class(shopCar.getGoodsClass());
        activeBean.setUrlMD5(shopCar.getGoodsurlmd5() == null ? "" : shopCar.getGoodsurlmd5());
        activeBean.setBizPriceDiscount(shopCar.getBizpricediscount());
        activeBean.setSpider_Price(shopCar.getSpiderprice() == null ? "0" : shopCar.getSpiderprice().toString());
        activeBean.setPriceListSize(shopCar.getPricelistsize());
        if (StringUtils.isNotBlank(shopCar.getPricelistsize())) {
            String[] priceSplit = shopCar.getPricelistsize().split("@@@@@");
            if (priceSplit != null && priceSplit.length == 2) {
                activeBean.setPriceListSize(priceSplit[0].trim());
                activeBean.setPriceList(priceSplit[1].trim());
            } else {
                activeBean.setPriceListSize("");
                activeBean.setPriceList("");
            }
            priceSplit = null;
        }


        activeBean.setGroupBuyId(shopCar.getGroupBuyId());
        //activeBean.setGbPrice(shopCar.getGbPrice());


        if (StringUtils.isNotBlank(shopCar.getDeliveryTime())) {
            String[] timeSplit = shopCar.getDeliveryTime().split("-");
            if (timeSplit.length == 2) {
                activeBean.setProcessingTimeMin(timeSplit[0].trim());
                activeBean.setProcessingTime(timeSplit[1].trim());
            } else if (timeSplit.length == 1) {
                activeBean.setProcessingTime(timeSplit[0].trim());
            }
            timeSplit = null;
        }

        return activeBean;
    }

}
