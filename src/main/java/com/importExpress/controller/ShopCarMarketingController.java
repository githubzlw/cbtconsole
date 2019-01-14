package com.importExpress.controller;

import com.cbt.orderinfo.service.OrderinfoService;
import com.cbt.userinfo.service.IUserInfoService;
import com.cbt.util.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.GoodsCarActiveBeanUpdate;
import com.cbt.website.dao.shoppingCartDao;
import com.cbt.website.dao.shoppingCartDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.pojo.*;
import com.importExpress.service.GoodsCarconfigService;
import com.importExpress.service.ShopCarMarketingService;
import com.importExpress.utli.GoodsPriceUpdateUtil;
import com.importExpress.utli.RedisModel;
import com.importExpress.utli.SendEmailNew;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/shopCarMarketingCtr")
public class ShopCarMarketingController {
    private static final Log logger = LogFactory.getLog(ShopCarMarketingController.class);

    private static final String GET_MIN_FREIGHT_URL = GetConfigureInfo.getValueByCbt("getMinFreightUrl");
    @Autowired
    private GoodsCarconfigService goodsCarconfigService;

    @Autowired
    private ShopCarMarketingService shopCarMarketingService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private SendEmailNew sendEmailNew;

    @Autowired
    private SendMailFactory sendMailFactory;
    @Autowired
    private OrderinfoService orderinfoService;

    private shoppingCartDao shopCarDao = new shoppingCartDaoImpl();


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

        String adminNameFirst = request.getParameter("adminNameFirst");
        if (StringUtils.isBlank(adminNameFirst)) {
            json.setOk(false);
            json.setMessage("获取销售名称失败");
            return json;
        }

        String adminName = request.getParameter("adminName");
        if (StringUtils.isBlank(adminName)) {
            json.setOk(false);
            json.setMessage("获取销售名称失败");
            return json;
        }

        String adminEmail = request.getParameter("adminEmail");
        if (StringUtils.isBlank(adminEmail)) {
            json.setOk(false);
            json.setMessage("获取销售邮箱失败");
            return json;
        }

        String whatsApp = request.getParameter("whatsApp");
        if (StringUtils.isBlank(whatsApp)) {
            json.setOk(false);
            json.setMessage("获取whatsApp失败");
            return json;
        }
        /*String emailContent = request.getParameter("emailContent");
        if (StringUtils.isBlank(emailContent)) {
            json.setOk(false);
            json.setMessage("获取邮件内容失败");
            return json;
        }*/


        String userName;
        String userEmail = request.getParameter("userEmail");
        if (StringUtils.isBlank(userEmail)) {
            //查询客户信息
            Map<String, Object> listu = userInfoService.getUserCount(Integer.valueOf(userIdStr));
            userEmail = listu.get("email").toString();
            listu.clear();
            if(listu.containsValue("name") && StringUtils.isNotBlank(listu.get("name").toString())){
                userName = listu.get("name").toString();
            }else{
                userName = userEmail;
            }
        }
        try {

            int userId = Integer.valueOf(userIdStr);
            String emailTitle = "You have made some wonderful selections";

            //1.重新生成goods_carconfig数据，并进行保存
            List<GoodsCarShowBean> showList = new ArrayList<GoodsCarShowBean>();
            List<GoodsCarActiveBeanUpdate> activeList = new ArrayList<GoodsCarActiveBeanUpdate>();
            ShopCarMarketingExample marketingExample = new ShopCarMarketingExample();
            ShopCarMarketingExample.Criteria marketingCriteria = marketingExample.createCriteria();
            marketingCriteria.andUseridEqualTo(userId);
            List<ShopCarMarketing> shopCarMarketingList = shopCarMarketingService.selectByExample(marketingExample);
            int isUpdatePrice = 0;
            for (ShopCarMarketing shopCar : shopCarMarketingList) {
                //解析数据
                if (shopCar.getPrice1() > 0) {
                    isUpdatePrice++;
                }
                showList.add(genShowBeanBeanByShopCar(shopCar));
                activeList.add(genActiveBeanByShopCar(shopCar));
            }

            //判断是否有改价的情况，有改价更新并清空购物车
            if (isUpdatePrice > 0) {
                boolean isSuccess = updateGoodsCarConfig(showList, activeList, Integer.valueOf(userIdStr));
                //更新成功后，发布邮件的时候全部更新线上
                if (isSuccess) {
                    //2.清空redis数据
                    //使用MQ清空购物车数据
                    //redis示例
                    SendMQ sendMQ = new SendMQ();
                    RedisModel redisModel = new RedisModel(new String[]{userIdStr});
                    redisModel.setType("3");
                    sendMQ.sendMsg(redisModel);
                    sendMQ.closeConn();
                } else {
                    json.setOk(false);
                    json.setMessage("更新失败,请重试");
                    return json;
                }
            }
            Map<String,String> paramMap = new HashMap<>();
            //3.发送邮件给客户
            //Added <V1.0.1> Start： cjc 2018/11/6 20:28 TODO 给客户发送邮件
            boolean modelB = StringUtils.isNotBlank(request.getParameter("model"));
            if (modelB) {
                String modelStr = request.getParameter("model");
                Map<String, Object> model = SerializeUtil.JsonToMapStr(modelStr);
                sendMailFactory.sendMail(String.valueOf(model.get("userEmail")), null, emailTitle, model, TemplateType.SHOPPING_CART_MARKETING);
            } else {

                paramMap.put("userEmail",userEmail);
                paramMap.put("emailTitle",emailTitle);
                paramMap.put("adminNameFirst",adminNameFirst);
                paramMap.put("adminName",adminName);
                paramMap.put("adminEmail",adminEmail);
                paramMap.put("whatsApp",whatsApp);
                genHtmlEamil(userId,paramMap);
                //sendEmailNew.send(user.getEmail(), "", userEmail, emailContent, emailTitle, "", 1);
            }
            //4.更新跟进信息
            shopCarMarketingService.updateAndInsertUserFollowInfo(userId, user.getId(), paramMap.toString());
            json.setOk(true);
            json.setMessage("发送邮件成功！");
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


    private void genHtmlEamil(int userId,Map<String,String> paramMap){
        try{
            //查询当前客户存在的购物车数据
            ShopCarMarketingExample marketingExample = new ShopCarMarketingExample();
            ShopCarMarketingExample.Criteria marketingCriteria = marketingExample.createCriteria();
            marketingCriteria.andUseridEqualTo(userId);
            List<ShopCarMarketing> shopCarMarketingList = shopCarMarketingService.selectByExample(marketingExample);
            //格式化处理规格数据
            double productCost = 0;
            double actualCost = 0;
            double totalProductCost = 0;
            double totalActualCost = 0;
            List<ShopCarMarketing> resultList = new ArrayList<ShopCarMarketing>();
            List<ShopCarMarketing> sourceList = new ArrayList<ShopCarMarketing>();
            int sourceCount = 0;
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
                shopCar.setGoogsImg(shopCar.getGoogsImg().replace(".60x60",".400x400"));
                List<String> typeList= new ArrayList<>();
                if(StringUtils.isNotBlank(shopCar.getGoodsType())){
                    String[] tempList = shopCar.getGoodsType().split(";");
                    for(String str : tempList){
                        if(StringUtils.isNotBlank(str)){
                            typeList.add(str);
                        }
                    }
                }
                shopCar.setTypeList(typeList);
                if (shopCar.getPrice1() > 0) {
                    double totalPrice = Double.valueOf(shopCar.getGoogsPrice()) * shopCar.getGoogsNumber();
                    productCost += shopCar.getPrice1() * shopCar.getGoogsNumber();
                    actualCost += totalPrice;
                    totalProductCost += shopCar.getPrice1() * shopCar.getGoogsNumber();
                    totalActualCost += totalPrice;
                    shopCar.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
                    resultList.add(shopCar);
                } else {
                    double totalPrice = Double.valueOf(shopCar.getGoogsPrice()) * shopCar.getGoogsNumber();
                    totalProductCost += totalPrice;
                    totalActualCost += totalPrice;
                    shopCar.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
                    if(sourceCount < 5){
                        sourceList.add(shopCar);
                        sourceCount ++;
                    }
                }
            }
            shopCarMarketingList.clear();

            Map<String,Object> modelM = new HashMap<String,Object>();
            double offCost = productCost - actualCost;

                modelM.put("productCost", BigDecimalUtil.truncateDouble(productCost, 2));
                modelM.put("actualCost", BigDecimalUtil.truncateDouble(actualCost, 2));
                modelM.put("totalProductCost", BigDecimalUtil.truncateDouble(totalProductCost, 2));
                modelM.put("totalActualCost", BigDecimalUtil.truncateDouble(totalActualCost, 2));
                if(productCost > 0){
                    modelM.put("offRate", BigDecimalUtil.truncateDouble((offCost) / productCost * 100, 2));
                }
                modelM.put("offCost", BigDecimalUtil.truncateDouble(offCost, 2));
                modelM.put("updateList", resultList);
                modelM.put("sourceList", sourceList);

                modelM.put("userEmail", paramMap.get("userEmail"));
                modelM.put("adminNameFirst", paramMap.get("adminNameFirst"));
                modelM.put("adminName", paramMap.get("adminName"));
                modelM.put("adminEmail", paramMap.get("adminEmail"));
                modelM.put("whatsApp", paramMap.get("whatsApp"));
			    sendMailFactory.sendMail(paramMap.get("userEmail"), paramMap.get("adminEmail"), paramMap.get("emailTitle"), modelM, TemplateType.SHOPPING_CART);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if (shopCar.getPrice1() > 0) {
            activeBean.setPrice1(shopCar.getPrice1() + "@1");
        } else {
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

    @RequestMapping("/queryShopCarList")
    @ResponseBody
    public List<Object[]> queryShopCarList(HttpServletRequest request, HttpServletResponse response) {
        String userid = request.getParameter("userid") == "" ? "0" : request.getParameter("userid");
        Integer currenPage = Integer.parseInt(request.getParameter("currenPage"));
        String isorder = request.getParameter("isorder");
        Integer pageSize = 40;
        Integer begin = (currenPage - 1) * pageSize;
        shoppingCartDao shopDao = new shoppingCartDaoImpl();

        String sessionId = request.getSession().getId();
        String admuserJson = Redis.hget(sessionId, "admuser");
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        String statu = request.getParameter("status");
        int status = -1;
        if (statu != null && !"".equals(statu)) {
            status = Integer.parseInt(statu);
        }

        List<Object[]> list = shopDao.getAllUserShopCarList(Integer.parseInt(userid), admuser, begin, pageSize, Integer.parseInt(isorder), status);
        return list;
    }


    @RequestMapping("/queryForList")
    @ResponseBody
    public EasyUiJsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        ShopCarUserStatistic statistic = new ShopCarUserStatistic();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            if (!"0".equals(admuser.getRoletype())) {
                statistic.setFollowAdminId(admuser.getId());
            }
        }

        int startNum = 0;
        int limitNum = 30;
        String rowsStr = request.getParameter("rows");
        if (StringUtils.isNotBlank(rowsStr)) {
            limitNum = Integer.valueOf(rowsStr);
        }

        String pageStr = request.getParameter("page");
        if (StringUtils.isNotBlank(pageStr)) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }


        String followIdStr = request.getParameter("followId");
        int followId = 0;
        if (StringUtils.isNotBlank(followIdStr)) {
            followId = Integer.parseInt(followIdStr);
        }

        String adminIdStr = request.getParameter("adminId");
        int adminId = 0;
        if (StringUtils.isNotBlank(adminIdStr)) {
            adminId = Integer.parseInt(adminIdStr);
        }

        String userIdStr = request.getParameter("userId");
        int userId = 0;
        if (StringUtils.isNotBlank(userIdStr)) {
            userId = Integer.parseInt(userIdStr);
        }

        String userEmail = request.getParameter("userEmail");
        if (StringUtils.isNotBlank(userEmail)) {
            statistic.setUserEmail(userEmail);
        }

        String isOrderStr = request.getParameter("isOrder");
        int isOrder = -1;
        if (StringUtils.isNotBlank(isOrderStr)) {
            isOrder = Integer.parseInt(isOrderStr);
        }

        double beginMoney = 0;
        String beginMoneyStr = request.getParameter("beginMoney");
        if (StringUtils.isNotBlank(beginMoneyStr)) {
            beginMoney = Double.parseDouble(beginMoneyStr);
        }

        double endMoney = 0;
        String endMoneyStr = request.getParameter("endMoney");
        if (StringUtils.isNotBlank(endMoneyStr)) {
            endMoney = Double.parseDouble(endMoneyStr);
        }

        int countryId = 0;
        String countryIdStr = request.getParameter("countryId");
        if (StringUtils.isNotBlank(countryIdStr)) {
            countryId = Integer.parseInt(countryIdStr);
        }

        try {

            statistic.setFollowAdminId(followId);
            statistic.setSaleId(adminId);
            statistic.setIsOrder(isOrder);
            statistic.setUserId(userId);
            statistic.setBeginMoney(beginMoney);
            statistic.setEndMoney(endMoney);
            statistic.setCountryId(countryId);
            statistic.setStartNum(startNum);
            statistic.setLimitNum(limitNum);
            List<ShopCarUserStatistic> res = shopCarMarketingService.queryForList(statistic);
            int count = shopCarMarketingService.queryForListCount(statistic);

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


    @RequestMapping("/queryShoppingCarByUserId")
    public ModelAndView queryShoppingCarByUserId(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("shoppingCartWithSingleUser");
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
            int userId = Integer.valueOf(userIdStr);
            ShopCarUserStatistic carUserStatistic = shopCarMarketingService.queryUserInfo(userId);
            if ("ePacket".equals(carUserStatistic.getShippingName())) {
                carUserStatistic.setShippingName("EPACKET (USPS)");
            }
            List<ShopCarInfo> shopCarInfoList = shopCarMarketingService.queryShopCarInfoByUserId(userId);
            double totalFreight = 0;
            double totalPrice = 0;
            double totalWhosePrice = 0;
            double estimateProfit = 0;
            
            for (ShopCarInfo carInfo : shopCarInfoList) {
                if(StringUtils.isBlank(carInfo.getWholesalePrice())){
                    continue;
                }
                //设置显示图片
                carInfo.setShowImg(carInfo.getRemotePath() + carInfo.getMainImg());
                carInfo.setCartGoodsImg(carInfo.getCartGoodsImg().replace("60x60.", "400x400."));
                carInfo.setOnlineUrl("https://www.import-express.com/goodsinfo/cbtconsole-1" + carInfo.getPid() + ".html");
                //格式化规格
                String tempType = "";
                if (StringUtils.isNotBlank(carInfo.getGoodsType())) {
                    String[] splitFirst = carInfo.getGoodsType().split(",");
                    for (String spCh : splitFirst) {
                        String[] spScList = spCh.split("@");
                        tempType += spScList[0] + ";";
                        spScList = null;
                    }
                    splitFirst = null;
                }
                if (tempType.length() > 0) {
                    carInfo.setGoodsType(tempType);
                }

                totalPrice += carInfo.getCartGoodsNum() * carInfo.getCartGoodsPrice();
                // 运费计算公式
                double freight = genFreightByPid(carUserStatistic.getShippingName(), carInfo.getCatid1(), carInfo.getCartWeight(), carUserStatistic.getCountryId());
                totalFreight += freight;
                double oldProfit = 0;
                //获取1688价格(1piece)
                double wholePrice = 0;
                String wholePriceStr = carInfo.getWholesalePrice();
                String firstPrice = wholePriceStr.split(",")[0].split("\\$")[1].trim();
                firstPrice = firstPrice.replace("]", "");

                if (firstPrice.contains("-")) {
                    wholePrice = Double.valueOf(firstPrice.split("-")[1].trim());
                } else {
                    wholePrice = Double.valueOf(firstPrice.trim());
                }

                //计算加价率
                if ((carInfo.getIsBenchmark() == 1 && carInfo.getBmFlag() == 1) || carInfo.getIsBenchmark() == 2) {
                    //对标时
                    double priceXs = (Double.valueOf(carInfo.getAliPrice()) * GoodsPriceUpdateUtil.EXCHANGE_RATE - freight) / wholePrice;
                    //加价率
                    oldProfit = GoodsPriceUpdateUtil.getAddPriceJz(priceXs);
                    carInfo.setPriceRate(BigDecimalUtil.truncateDouble(oldProfit, 2));
                } else {
                    //非对标
                    double catXs = GoodsPriceUpdateUtil.getCatxs(carInfo.getCatid1(), String.valueOf(wholePrice));
                    //加价率= 0.55+类别调整值
                    oldProfit = 0.55 + catXs;
                    carInfo.setPriceRate(BigDecimalUtil.truncateDouble(oldProfit, 2));
                }

                //计算利润率
                totalWhosePrice += wholePrice * carInfo.getCartGoodsNum();
            }
            // 调用线上接口，获取客户支付运费,实际我司运费
            getMinFreightByUserId(userId,carUserStatistic);
            // 利润率计算
            estimateProfit = (totalPrice + carUserStatistic.getTotalFreight() - carUserStatistic.getOffFreight() - totalWhosePrice / GoodsPriceUpdateUtil.EXCHANGE_RATE) / totalWhosePrice * 100D;

            carUserStatistic.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
            carUserStatistic.setEstimateProfit(BigDecimalUtil.truncateDouble(estimateProfit, 2));
            carUserStatistic.setTotalWhosePrice(BigDecimalUtil.truncateDouble(totalWhosePrice / GoodsPriceUpdateUtil.EXCHANGE_RATE, 2));


            mv.addObject("success", 1);
            mv.addObject("userInfo", carUserStatistic);
            mv.addObject("goodsList", shopCarInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryShoppingCarByUserId error:" + e.getMessage());
            logger.error("queryShoppingCarByUserId error:" + e.getMessage());
            mv.addObject("message", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("success", 0);
        }
        return mv;
    }

    private double getMinFreightByUserId(int userId,ShopCarUserStatistic carUserStatistic) {

        double freight = 0;
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder().add("userId", String.valueOf(userId)).build();
        Request request = new Request.Builder().addHeader("Accept","*/*")
				.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .url(GET_MIN_FREIGHT_URL).post(formBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String resultStr = response.body().string();
            JSONObject json = JSONObject.fromObject(resultStr);
            if (json.getBoolean("ok")) {
                System.out.println("getMinFreightByUserId success !!!");
                freight = json.getJSONObject("data").getDouble("totalFreight");
                if(freight > 0 ){
                    carUserStatistic.setShippingName(json.getJSONObject("data").getString("transportation"));
                    carUserStatistic.setTotalFreight(BigDecimalUtil.truncateDouble(freight, 2));
                }
                carUserStatistic.setOffFreight(BigDecimalUtil.truncateDouble(json.getJSONObject("data").getDouble("freightCost"), 2));
            } else {
                System.err.println("getMinFreightByUserId error :<:<:<");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.error("getMinFreightByUserId error:" + e.getMessage());
        }
        return freight;
    }


    @RequestMapping("/genShopCarInfoByUserId")
    @ResponseBody
    public Map<String, Object> genShopCarInfoByUserId(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            resultMap.put("success", "0");
            resultMap.put("message", "用户未登录");
            return resultMap;
        }
        String userIdStr = request.getParameter("userId");
        if (StringUtils.isBlank(userIdStr)) {
            resultMap.put("success", "0");
            resultMap.put("message", "获取用户ID失败");
            return resultMap;
        }
        try {
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
                    }
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
                } else {
                    double totalPrice = Double.valueOf(shopCar.getGoogsPrice()) * shopCar.getGoogsNumber();
                    productCost += totalPrice;
                    actualCost += totalPrice;
                    shopCar.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
                    sourceList.add(shopCar);
                }
            }
            shopCarMarketingList.clear();
            if (resultList.size() > 0) {
                double offCost = productCost - actualCost;
                resultMap.put("success", "1");
                resultMap.put("productCost", BigDecimalUtil.truncateDouble(productCost, 2));
                resultMap.put("actualCost", BigDecimalUtil.truncateDouble(actualCost, 2));
                resultMap.put("offRate", BigDecimalUtil.truncateDouble((offCost) / productCost * 100, 2));
                resultMap.put("offCost", BigDecimalUtil.truncateDouble(offCost, 2));
                resultList.clear();
                sourceList.clear();
            } else {
                resultMap.put("success", "0");
                resultMap.put("message", "未进行商品改价");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("genShopCarInfoByUserId error:" + e.getMessage());
            logger.error("genShopCarInfoByUserId error:" + e.getMessage());
            resultMap.put("success", "0");
            resultMap.put("message", "执行过程失败，原因：" + e.getMessage());
        }
        return resultMap;
    }


    @RequestMapping("/comparedEmailWithAliExpress")
    public ModelAndView comparedEmailWithAliExpress(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("comparedWithAliExpressEmail");
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
            int userId = Integer.valueOf(userIdStr);
            //查询客户信息
            Map<String, Object> listu = userInfoService.getUserCount(userId);
            mv.addObject("userEmail", listu.get("email"));
            listu.clear();
            List<ShopCarInfo> shopCarInfoList = shopCarMarketingService.queryShopCarInfoByUserId(userId);
            List<ShopCarInfo> resultList = new ArrayList<>();
            for (ShopCarInfo shopCar : shopCarInfoList) {
                shopCar.setShowImg(shopCar.getRemotePath() + shopCar.getMainImg());
                shopCar.setOnlineUrl("https://www.import-express.com/goodsinfo/" + shopCar.getGoodsTitle() + "-1" + shopCar.getPid() + ".html");
                String tempType = "";
                if (StringUtils.isNotBlank(shopCar.getGoodsType())) {
                    String[] splitFirst = shopCar.getGoodsType().split(",");
                    for (String spCh : splitFirst) {
                        String[] spScList = spCh.split("@");
                        tempType += spScList[0] + ";";
                    }
                }
                if (tempType.length() > 0) {
                    shopCar.setGoodsType(tempType);
                }
                if (StringUtils.isNotBlank(shopCar.getAliPid())) {
                    resultList.add(shopCar);
                }
            }
            shopCarInfoList.clear();
            if (resultList.size() > 0) {
                mv.addObject("resultList", resultList);
                mv.addObject("success", 1);
            } else {
                mv.addObject("message", "无对标商品信息，请确认");
                mv.addObject("success", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("comparedEmailWithAliExpress error:" + e.getMessage());
            logger.error("comparedEmailWithAliExpress error:" + e.getMessage());
            mv.addObject("message", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("success", 0);
        }
        return mv;
    }


    //计算商品运费

    public double genFreightByPid(String modeTransport, String catid, double odWeight, int countryId) {
        double esFreight = 0.00;
        if (StringUtil.isBlank(modeTransport)) {
            return 0.00;
        }
        String[] strs = modeTransport.split("@");
        String type = strs[0];
        if (StringUtil.isBlank(type) || "null".equals(type)) {
            type = "Epacket";
        }
        type = type.toLowerCase();
        //特殊商品
        double weight1 = 0.00;
        //普通商品
        double weight2 = 0.00;

        if (Util.getThisCatIdIsSpecialStr(catid)) {
            //特殊商品
            weight1 += odWeight;
        } else {
            //普通商品
            weight2 += odWeight;
        }

        double freight1 = 0.00, freight2 = 0.00;
        if (weight1 > 0) {
            freight1 = orderinfoService.getOrderShippingCost(weight1, countryId, type, true);
        }
        if (weight2 > 0) {
            freight2 = orderinfoService.getOrderShippingCost(weight2, countryId, type, false);
        }
        esFreight += (freight1 + freight2);
        return esFreight / GoodsPriceUpdateUtil.EXCHANGE_RATE;
    }


    @RequestMapping("/queryTrackingList")
    @ResponseBody
    public EasyUiJsonResult queryTrackingList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        ShopTrackingBean param = new ShopTrackingBean();
        String adminUserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(adminUserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }

        int startNum = 0;
        int limitNum = 30;
        String rowsStr = request.getParameter("rows");
        if (StringUtils.isNotBlank(rowsStr)) {
            limitNum = Integer.valueOf(rowsStr);
        }

        String pageStr = request.getParameter("page");
        if (StringUtils.isNotBlank(pageStr)) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }

        String userIdStr = request.getParameter("userId");
        if (StringUtils.isNotBlank(userIdStr)) {
            param.setUserId(Integer.parseInt(userIdStr));
        }

        String orderNo = request.getParameter("orderNo");
        if (StringUtils.isNotBlank(orderNo)) {
            param.setOrderNo(orderNo);
        }

        String adminIdStr = request.getParameter("adminId");
        if (StringUtils.isNotBlank(adminIdStr)) {
            param.setAdminId(Integer.parseInt(adminIdStr));
        }

        String orderPayBeginTime = request.getParameter("orderPayBeginTime");
        if (StringUtils.isNotBlank(orderPayBeginTime)) {
            param.setOrderPayTime(orderPayBeginTime);
        }

        String orderPayEndTime = request.getParameter("orderPayEndTime");
        if (StringUtils.isNotBlank(orderPayEndTime)) {
            param.setOrderPayEndTime(orderPayEndTime);
        }

        try {

            param.setStartNum(startNum);
            param.setLimitNum(limitNum);
            List<ShopTrackingBean> res = shopCarMarketingService.queryTrackingList(param);
            int count = shopCarMarketingService.queryTrackingListCount(param);
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


    @RequestMapping("/exportTrackingExcel")
    @ResponseBody
    public void exportTrackingExcel(HttpServletRequest request, HttpServletResponse response) {

        ShopTrackingBean param = new ShopTrackingBean();

        String userIdStr = request.getParameter("userId");
        if (StringUtils.isNotBlank(userIdStr)) {
            param.setUserId(Integer.parseInt(userIdStr));
        }

        String orderNo = request.getParameter("orderNo");
        if (StringUtils.isNotBlank(orderNo)) {
            param.setOrderNo(orderNo);
        }

        String adminIdStr = request.getParameter("adminId");
        if (StringUtils.isNotBlank(adminIdStr)) {
            param.setAdminId(Integer.parseInt(adminIdStr));
        }

        String orderPayBeginTime = request.getParameter("orderPayBeginTime");
        if (StringUtils.isNotBlank(orderPayBeginTime)) {
            param.setOrderPayTime(orderPayBeginTime);
        }

        String orderPayEndTime = request.getParameter("orderPayEndTime");
        if (StringUtils.isNotBlank(orderPayEndTime)) {
            param.setOrderPayEndTime(orderPayEndTime);
        }
        OutputStream ouputStream = null;
        try {
            List<ShopTrackingBean> res = shopCarMarketingService.queryTrackingList(param);

            HSSFWorkbook wb = genTrackingExcel(res, "购物车营销追踪详情");
            response.setContentType("application/vnd.ms-excel");
            String headerTitle = "attachment;filename=" + (StringUtils.isNotBlank(adminIdStr) ? res.get(0).getAdminName() : "")
                    + "-shopCartMarketingTracking.xls";
            response.setHeader("Content-disposition", headerTitle);
            response.setCharacterEncoding("utf-8");
            ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ouputStream != null) {
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HSSFWorkbook genTrackingExcel(List<ShopTrackingBean> res, String title) {

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(title);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("订单号");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("下单时间");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("下单金额(USD)");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("销售");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("跟进时间");
        cell.setCellStyle(style);

        for (int i = 0; i < res.size(); i++) {
            row = sheet.createRow((int) i + 1);
            // 第四步，创建单元格，并设置值
            row.createCell(0).setCellValue(res.get(i).getUserId());
            row.createCell(1).setCellValue(res.get(i).getOrderNo());
            row.createCell(2).setCellValue(res.get(i).getOrderPayTime());
            row.createCell(3).setCellValue(res.get(i).getOrderPayAmount());
            row.createCell(4).setCellValue(res.get(i).getAdminName());
            row.createCell(5).setCellValue(res.get(i).getFollowTime());
        }
        return wb;
    }


    @RequestMapping("/queryAllCountry")
    @ResponseBody
    public List<ZoneBean> queryAllCountry(HttpServletRequest request, HttpServletResponse response) {

        List<ZoneBean> list = new ArrayList<>();
        try {
            list = shopCarMarketingService.queryAllCountry();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败，原因 :" + e.getMessage());
        }
        return list;
    }


    @RequestMapping("/genShoppingCarMarketingEmail")
    public ModelAndView genShoppingCarMarketingEmail(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("shopCartMarketingEmail");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("message", "用户未登录");
            mv.addObject("success", 0);
            return mv;
        } else {
            mv.addObject("adminName", user.getAdmName());
            mv.addObject("adminEmail", user.getEmail());
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
            double totalProductCost = 0;
            double totalActualCost = 0;
            List<ShopCarMarketing> resultList = new ArrayList<ShopCarMarketing>();
            List<ShopCarMarketing> sourceList = new ArrayList<ShopCarMarketing>();
            int sourceCount = 0;
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
                    totalProductCost += shopCar.getPrice1() * shopCar.getGoogsNumber();
                    totalActualCost += totalPrice;
                    shopCar.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
                    resultList.add(shopCar);
                } else {
                    double totalPrice = Double.valueOf(shopCar.getGoogsPrice()) * shopCar.getGoogsNumber();
                    totalProductCost += totalPrice;
                    totalActualCost += totalPrice;
                    shopCar.setTotalPrice(BigDecimalUtil.truncateDouble(totalPrice, 2));
                    if(sourceCount < 5){
                        sourceList.add(shopCar);
                        sourceCount ++;
                    }
                }
            }
            shopCarMarketingList.clear();
            double offCost = productCost - actualCost;
            mv.addObject("productCost", BigDecimalUtil.truncateDouble(productCost, 2));
            mv.addObject("actualCost", BigDecimalUtil.truncateDouble(actualCost, 2));
            mv.addObject("totalProductCost", BigDecimalUtil.truncateDouble(totalProductCost, 2));
            mv.addObject("totalActualCost", BigDecimalUtil.truncateDouble(totalActualCost, 2));
            if(productCost > 0){
                mv.addObject("offRate", BigDecimalUtil.truncateDouble((offCost) / productCost * 100, 2));
            }
            mv.addObject("success", 1);
            mv.addObject("offCost", BigDecimalUtil.truncateDouble(offCost, 2));
            mv.addObject("updateList", resultList);
            mv.addObject("sourceList", sourceList);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("genShoppingCarMarketingEmail error:" + e.getMessage());
            logger.error("genShoppingCarMarketingEmail error:" + e.getMessage());
            mv.addObject("message", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("success", 0);
        }
        return mv;
    }
}
