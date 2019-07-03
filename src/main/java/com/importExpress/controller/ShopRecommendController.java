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
import com.importExpress.utli.UserInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/shopRecommend")
public class ShopRecommendController {
    private final static Logger logger = LoggerFactory.getLogger(ShopRecommendController.class);

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
                    dealGoodsInfo(goodsList);
                    shopRecommendInfo.setGoodsList(goodsList);
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


    private void dealGoodsInfo(List<ShopRecommendGoods> goodsList) {
        if (goodsList != null && goodsList.size() > 0) {
            for (ShopRecommendGoods gd : goodsList) {
                if (gd.getMainImg().contains("http://") || gd.getMainImg().contains("https://")) {
                    gd.setGoodsImg(gd.getMainImg());
                } else {
                    gd.setGoodsImg(gd.getRemotePath() + gd.getMainImg());
                }
            }
        }
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
            json = getBeanByParam(request, shopRecommendInfo);
            if (shopRecommendInfo.getId() == null || shopRecommendInfo.getId() == 0) {
                shopRecommendInfo.setCreateAdminId(admuser.getId());
            } else {
                shopRecommendInfo.setUpdateAdminId(admuser.getId());
                shopRecommendInfo.setCoverPid("");
                shopRecommendInfo.setCoverImg("");
            }
            if (json.isOk()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("insertShopRecommendInfo error", e);
        }
        return json;
    }


    @RequestMapping("/updateShopRecommendInfo")
    @ResponseBody
    public JsonResult updateShopRecommendInfo(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        ShopRecommendInfo shopRecommendInfo = new ShopRecommendInfo();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }
            shopRecommendInfo.setUpdateAdminId(admuser.getId());
            json = getBeanByParam(request, shopRecommendInfo);
            if (json.isOk()) {
                if (StringUtils.isBlank(shopRecommendInfo.getShopId()) || shopRecommendInfo.getIsOn() == null ||
                        shopRecommendInfo.getSort() == null || StringUtils.isBlank(shopRecommendInfo.getCoverPid())
                        || StringUtils.isBlank(shopRecommendInfo.getCoverImg())) {
                    json.setOk(false);
                    json.setMessage("获取参数失败");
                } else {
                    json.setOk(false);
                    shopRecommendService.updateShopRecommendInfo(shopRecommendInfo);
                    json.setOk(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("updateShopRecommendInfo shopId:" + shopRecommendInfo.getShopId() + " error", e);
        }
        return json;
    }

    private JsonResult getBeanByParam(HttpServletRequest request, ShopRecommendInfo shopRecommendInfo) {
        JsonResult json = new JsonResult();

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
                    List<CustomGoodsPublish> goodsList = customGoodsService.queryGoodsByShopId(shopId);
                    for (CustomGoodsPublish gd : goodsList) {
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

    @RequestMapping("/11")
    @ResponseBody
    public JsonResult insertShopRecommendInfo1() {
        JsonResult json = new JsonResult();
        try {

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
        }
        return json;
    }
}
