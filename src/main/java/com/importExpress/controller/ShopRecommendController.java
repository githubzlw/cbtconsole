package com.importExpress.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.pojo.Admuser;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.util.StrUtils;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.ShopRecommendGoods;
import com.importExpress.pojo.ShopRecommendInfo;
import com.importExpress.service.ShopRecommendService;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.OKHttpUtils;
import com.importExpress.utli.UserInfoUtils;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shopRecommend")
public class ShopRecommendController {
    private final static Logger logger = LoggerFactory.getLogger(ShopRecommendController.class);
    private static OKHttpUtils okHttpUtils = new OKHttpUtils();

    @Autowired
    private ShopRecommendService shopRecommendService;

    @Autowired
    private CustomGoodsService customGoodsService;


    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("shopRecommendList");
        try {
            if (UserInfoUtils.checkIsLogin(request)) {
                List<ShopRecommendInfo> list = shopRecommendService.queryShopRecommendInfoList();
                for (ShopRecommendInfo shopRecommendInfo : list) {
                    List<ShopRecommendGoods> goodsList = shopRecommendService.queryShopRecommendGoodsByShopId(shopRecommendInfo.getShopId());
                    List<ShopRecommendGoods> showList = dealGoodsInfo(goodsList);
                    shopRecommendInfo.setGoodsList(showList);
                    goodsList.clear();
                }
                mv.addObject("list", list);
                mv.addObject("isShow", 1);
            } else {
                mv.addObject("isShow", 0);
                mv.addObject("message", "请登录后操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isShow", 0);
            mv.addObject("message", e.getMessage());
            logger.error("queryForList error:", e);
        }
        return mv;
    }


    private List<ShopRecommendGoods> dealGoodsInfo(List<ShopRecommendGoods> goodsList) {
        List<ShopRecommendGoods> showList = new ArrayList<>();
        if (goodsList != null && goodsList.size() > 0) {
            int total = 0;
            for (ShopRecommendGoods gd : goodsList) {
                if (gd.getMainImg().contains("http://") || gd.getMainImg().contains("https://")) {
                    gd.setGoodsImg(gd.getMainImg());
                } else {
                    gd.setGoodsImg(gd.getRemotePath() + gd.getMainImg());
                }
                total++;
                if (total <= 8) {
                    showList.add(gd);
                } else {
                    break;
                }
            }
        }
        return showList;
    }

    @RequestMapping("/insertShopRecommendInfo")
    @ResponseBody
    public JsonResult insertShopRecommendInfo(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        ShopRecommendInfo shopRecommendInfo = new ShopRecommendInfo();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }
            shopRecommendInfo = getBeanByParam(request);
            if (shopRecommendInfo.getId() == null || shopRecommendInfo.getId() == 0) {
                shopRecommendInfo.setCreateAdminId(admuser.getId());
            } else {
                shopRecommendInfo.setUpdateAdminId(admuser.getId());
                shopRecommendInfo.setCoverPid("");
                shopRecommendInfo.setCoverImg("");
            }
            if (StringUtils.isBlank(shopRecommendInfo.getShopId()) || shopRecommendInfo.getIsOn() == null ||
                    shopRecommendInfo.getSort() == null) {
                json.setOk(false);
                json.setMessage("获取参数失败");
            } else {
                json.setOk(false);
                if (shopRecommendInfo.getId() > 0) {
                    shopRecommendService.updateShopRecommendInfo(shopRecommendInfo);
                    json.setOk(true);
                } else {
                    if (shopRecommendService.checkRecommendInfoByShopId(shopRecommendInfo.getShopId()) > 0) {
                        json.setOk(false);
                        json.setMessage("店铺:" + shopRecommendInfo.getShopId() + " 已经录入");
                    } else {
                        shopRecommendService.insertShopRecommendInfo(shopRecommendInfo);
                        json.setOk(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("insertShopRecommendInfo error", e);
        }
        return json;
    }


    @RequestMapping("/deleteShopRecommendInfoByShopId")
    @ResponseBody
    public JsonResult deleteShopRecommendInfoByShopId(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }

        try {
            shopRecommendService.deleteShopRecommendInfoByShopId(shopId);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("deleteShopRecommendInfoByShopId shopId:" + shopId + " error", e);
        }
        return json;
    }

    @RequestMapping("/queryGoodsListByShopId")
    public ModelAndView queryGoodsListByShopId(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("shopRecommendGoods");
        try {
            if (UserInfoUtils.checkIsLogin(request)) {
                String shopId = request.getParameter("shopId");
                if (StringUtils.isBlank(shopId)) {
                    mv.addObject("isShow", 0);
                    mv.addObject("message", "获取店铺ID失败");
                } else {
                    ShopRecommendInfo info = shopRecommendService.queryRecommendInfoByShopId(shopId);
                    List<ShopRecommendGoods> recommendGoodsList = shopRecommendService.queryShopRecommendGoodsByShopId(shopId);
                    Set<String> pidSet = new HashSet<>(recommendGoodsList.size() * 2);
                    if (recommendGoodsList != null && recommendGoodsList.size() > 0) {
                        pidSet = recommendGoodsList.stream().map(ShopRecommendGoods::getPid).collect(Collectors.toSet());
                    }
                    List<CustomGoodsPublish> goodsList = customGoodsService.queryGoodsByShopId(shopId);
                    for (CustomGoodsPublish gd : goodsList) {
                        gd.setBmFlag(0);
                        gd.setIsEdited(0);
                        if (gd.getPid().equals(info.getCoverPid())) {
                            gd.setBmFlag(1);
                        }
                        if (pidSet.contains(gd.getPid())) {
                            gd.setIsEdited(1);
                        }
                        if (gd.getShowMainImage().contains("http://") || gd.getShowMainImage().contains("https://")) {
                            gd.setCustomMainImage(gd.getShowMainImage());
                        } else {
                            gd.setCustomMainImage(gd.getRemotpath() + gd.getShowMainImage());
                        }
                        String rangePrice = gd.getRangePrice();
                        String maxPrice = "";
                        if (StringUtils.isBlank(rangePrice) || rangePrice.trim().length() == 0) {
                            if (Integer.valueOf(gd.getIsSoldFlag()) > 0) {
                                if (StringUtils.isNotBlank(gd.getFeeprice())) {
                                    List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",
                                            StrUtils.object2Str(gd.getFeeprice()));
                                    if (matchStrList != null && !matchStrList.isEmpty()) {
                                        rangePrice = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                                        if (matchStrList.size() > 1) {
                                            maxPrice = StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                                            rangePrice = rangePrice + "-" + maxPrice;
                                        }
                                    } else {
                                        rangePrice = StrUtils.object2Str(gd.getPrice());
                                    }
                                }
                            } else {
                                List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)", StrUtils.object2Str(gd.getWprice()));
                                if (matchStrList != null && !matchStrList.isEmpty()) {
                                    rangePrice = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                                    if (matchStrList.size() > 1) {
                                        maxPrice = StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                                        rangePrice = rangePrice + "-" + maxPrice;
                                    }
                                } else {
                                    rangePrice = StrUtils.object2Str(gd.getPrice());
                                }
                            }
                        }
                        gd.setRangePrice(rangePrice);
                        gd.setOnlineUrl(GoodsInfoUtils.genOnlineUrl(gd));
                    }
                    // 排序
                    goodsList.sort((o1, o2) -> {
                        if (o1.getBmFlag() == o2.getBmFlag()) {
                            return o2.getIsEdited() - o1.getIsEdited();
                        } else {
                            return o2.getBmFlag() - o1.getBmFlag();
                        }
                    });
                    mv.addObject("list", goodsList);
                    mv.addObject("isShow", 1);
                }

            } else {
                mv.addObject("isShow", 0);
                mv.addObject("message", "请登录后操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isShow", 0);
            mv.addObject("message", e.getMessage());
            logger.error("queryGoodsListByShopId error:", e);
        }
        return mv;
    }


    @RequestMapping("/setShopMainImg")
    @ResponseBody
    public JsonResult setShopMainImg(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        ShopRecommendInfo shopRecommendInfo = new ShopRecommendInfo();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }
            shopRecommendInfo = getBeanByParam(request);
            shopRecommendInfo.setUpdateAdminId(admuser.getId());
            if (StringUtils.isBlank(shopRecommendInfo.getShopId()) || StringUtils.isBlank(shopRecommendInfo.getCoverPid())
                    || StringUtils.isBlank(shopRecommendInfo.getCoverImg())) {
                json.setOk(false);
                json.setMessage("获取参数失败");
            } else {
                List<ShopRecommendGoods> recommendGoodsList = shopRecommendService.queryShopRecommendGoodsByShopId(shopRecommendInfo.getShopId());
                int isGs = 0;
                for (ShopRecommendGoods gd : recommendGoodsList) {
                    if (gd.getPid().equals(shopRecommendInfo.getCoverPid())) {
                        isGs = 1;
                        break;
                    }
                }
                if (isGs == 0) {
                    List<ShopRecommendGoods> list = new ArrayList<>();
                    ShopRecommendGoods inGd = new ShopRecommendGoods();
                    inGd.setShopId(shopRecommendInfo.getShopId());
                    inGd.setPid(shopRecommendInfo.getCoverPid());
                    inGd.setGoodsImg(shopRecommendInfo.getCoverImg());
                    inGd.setIsOn(1);
                    list.add(inGd);
                    shopRecommendService.insertShopRecommendGoods(list);
                    list.clear();
                }
                shopRecommendService.updateShopMainImg(shopRecommendInfo);
                json.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("updateShopRecommendInfo shopId:" + shopRecommendInfo.getShopId() + " error", e);
        }
        return json;
    }


    @RequestMapping("/saveShopGoods")
    @ResponseBody
    public JsonResult saveShopGoods(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        ShopRecommendInfo queryInfo = new ShopRecommendInfo();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }
            queryInfo = getBeanByParam(request);
            queryInfo.setUpdateAdminId(admuser.getId());
            if (json.isOk()) {
                if (StringUtils.isBlank(queryInfo.getShopId()) || StringUtils.isBlank(queryInfo.getPidList())) {
                    json.setOk(false);
                    json.setMessage("获取参数失败");
                } else {
                    JSONArray jsonArray = JSONArray.fromObject(queryInfo.getPidList());
                    List<ShopRecommendGoods> insertGoodsList = (List<ShopRecommendGoods>) JSONArray.toCollection(jsonArray, ShopRecommendGoods.class);
                    if (insertGoodsList != null && insertGoodsList.size() > 0) {
                        ShopRecommendInfo recommendInfo = shopRecommendService.queryRecommendInfoByShopId(queryInfo.getShopId());
                        boolean isCheckMain = true;
                        for (ShopRecommendGoods srGd : insertGoodsList) {
                            srGd.setShopId(queryInfo.getShopId());
                            srGd.setIsOn(1);
                            srGd.setCreateAdminId(admuser.getId());
                            if (srGd.getPid().equals(recommendInfo.getCoverPid())) {
                                isCheckMain = false;
                            }
                        }
                        if (isCheckMain) {
                            ShopRecommendGoods inGd = new ShopRecommendGoods();
                            inGd.setShopId(queryInfo.getShopId());
                            inGd.setPid(queryInfo.getCoverPid());
                            inGd.setGoodsImg(queryInfo.getCoverImg());
                            inGd.setIsOn(1);
                            inGd.setCreateAdminId(admuser.getId());
                            insertGoodsList.add(inGd);
                        }
                        shopRecommendService.deleteShopRecommendGoods(queryInfo.getShopId());
                        shopRecommendService.insertShopRecommendGoods(insertGoodsList);
                        insertGoodsList.clear();
                        json.setOk(true);
                    } else {
                        json.setOk(false);
                        json.setMessage("获取参数失败");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("updateShopRecommendInfo shopId:" + queryInfo.getShopId() + " error", e);
        }
        return json;
    }


    private ShopRecommendInfo getBeanByParam(HttpServletRequest request) {

        ShopRecommendInfo shopRecommendInfo = new ShopRecommendInfo();
        String spId = request.getParameter("spId");
        if (StringUtils.isNotBlank(spId)) {
            shopRecommendInfo.setId(Integer.valueOf(spId));
        }
        String shopId = request.getParameter("shopId");
        if (StringUtils.isNotBlank(shopId)) {
            shopRecommendInfo.setShopId(shopId);
        }
        String sort = request.getParameter("sort");
        if (StringUtils.isNotBlank(sort)) {
            shopRecommendInfo.setSort(Integer.valueOf(sort));
        }
        String isOn = request.getParameter("isOn");
        if (StringUtils.isNotBlank(isOn)) {
            shopRecommendInfo.setIsOn(Integer.valueOf(isOn));
        }
        String coverImg = request.getParameter("coverImg");
        if (StringUtils.isNotBlank(coverImg)) {
            shopRecommendInfo.setCoverImg(coverImg);
        }
        String coverPid = request.getParameter("coverPid");
        if (StringUtils.isNotBlank(coverPid)) {
            shopRecommendInfo.setCoverPid(coverPid);
        }
        String pidList = request.getParameter("pidList");
        if (StringUtils.isNotBlank(pidList)) {
            shopRecommendInfo.setPidList(pidList);
        }
        return shopRecommendInfo;
    }

    /**
     * 生成电商网站显示数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/genOnlineData")
    @ResponseBody
    public JsonResult genOnlineData(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            if (UserInfoUtils.checkIsLogin(request)) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }

            List<ShopRecommendInfo> list = shopRecommendService.queryShopRecommendInfoList();
            List<ShopRecommendInfo> upList = new ArrayList<>();
            for (ShopRecommendInfo shopRecommendInfo : list) {
                if (shopRecommendInfo.getIsOn() > 0) {
                    List<ShopRecommendGoods> goodsList = shopRecommendService.queryShopRecommendGoodsByShopId(shopRecommendInfo.getShopId());
                    List<ShopRecommendGoods> showList = dealGoodsInfo(goodsList);
                    shopRecommendInfo.setGoodsList(showList);
                    goodsList.clear();
                    upList.add(shopRecommendInfo);
                }
            }
            // 生成需要数据
            genShopRecommendInfoData(upList);
            // 排序
            upList.sort(Comparator.comparingInt(ShopRecommendInfo::getSort));

            File file = GoodsInfoUpdateOnlineUtil.writeDataToLocal("shopRecommend.json", upList.toString());
            boolean isSuccess = okHttpUtils.postFileNoParam(GoodsInfoUpdateOnlineUtil.ONLINE_SHOP_RECOMMEND_URL, file);
            upList.clear();
            if (isSuccess) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("上传文件失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("genOnlineData error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }

    private void genShopRecommendInfoData(List<ShopRecommendInfo> upList) {
        // 遍历生成数据
        CustomGoodsPublish goods;
        for (ShopRecommendInfo Info : upList) {
            for (ShopRecommendGoods childGoods : Info.getGoodsList()) {
                goods = customGoodsService.queryGoodsDetails(childGoods.getPid(), 0);
                childGoods.setEnName(goods.getEnname());
                if (goods.getShowMainImage().contains("http:") || goods.getShowMainImage().contains("https:")) {
                    childGoods.setMainImg(goods.getShowMainImage().replace("http:", "https:"));
                } else {
                    childGoods.setMainImg(goods.getRemotpath().replace("http:", "https:") + goods.getShowMainImage());
                }
                childGoods.setOnlineUrl(GoodsInfoUtils.genOnlineUrl(goods));
                if (goods.getRangePrice() == null || "".equals(goods.getRangePrice()) || "[]".equals(goods.getRangePrice().trim())) {
                    if (Integer.valueOf(goods.getIsSoldFlag()) > 0) {
                        if (StringUtils.isBlank(goods.getFeeprice()) || "[]".equals(goods.getFeeprice().trim())) {
                            childGoods.setPriceShow(goods.getPrice().trim());
                        } else {
                            if (goods.getFeeprice().contains(",")) {
                                String[] prices = goods.getFeeprice().split(",");
                                String[] price2 = prices[0].replace(" ", "").split("\\$");
                                childGoods.setPriceShow(price2[1].replace("[", "").replace("]", "").trim());
                            } else {
                                String[] price2 = goods.getFeeprice().replace(" ", "").split("\\$");
                                childGoods.setPriceShow(price2[1].replace("[", "").replace("]", "").trim());
                            }
                        }
                    } else {
                        if (StringUtils.isBlank(goods.getWprice()) || "[]".equals(goods.getWprice().trim())) {
                            childGoods.setPriceShow(goods.getPrice().trim());
                        } else {
                            if (goods.getWprice().contains(",")) {
                                String[] prices = goods.getWprice().split(",");
                                String[] price2 = prices[0].replace(" ", "").split("\\$");
                                childGoods.setPriceShow(price2[1].replace("[", "").replace("]", "").trim());
                            } else {
                                String[] price2 = goods.getWprice().replace(" ", "").split("\\$");
                                childGoods.setPriceShow(price2[1].replace("[", "").replace("]", "").trim());
                            }
                        }
                    }
                } else {
                    if (goods.getRangePrice().contains("-")) {
                        childGoods.setPriceShow(goods.getRangePrice().split("-")[1].trim());
                    } else {
                        childGoods.setPriceShow(goods.getRangePrice().trim());
                    }
                }
                childGoods.setGoodsUnit(goods.getSellUnit());
            }
        }
    }
}
