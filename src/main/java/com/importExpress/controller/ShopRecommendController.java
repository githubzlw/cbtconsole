package com.importExpress.controller;

import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.ShopRecommendInfo;
import com.importExpress.service.ShopRecommendService;
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


    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("shopRecommendList");
        try {
            List<ShopRecommendInfo> list = shopRecommendService.queryShopRecommendInfoList();
            mv.addObject("list", list);
            mv.addObject("isShow", 1);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isShow", 0);
            mv.addObject("message", e.getMessage());
            logger.error("queryForList error:", e);
        }
        return mv;
    }


    @RequestMapping("/insertShopRecommendInfo")
    @ResponseBody
    public JsonResult insertShopRecommendInfo(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        ShopRecommendInfo shopRecommendInfo = new ShopRecommendInfo();
        try {
            json = getBeanByParam(request, shopRecommendInfo);
            if (json.isOk()) {
                json.setOk(false);
                if (shopRecommendService.checkRecommendInfoByShopId(shopRecommendInfo.getShopId()) > 0) {
                    json.setOk(false);
                    json.setMessage("店铺:" + shopRecommendInfo.getShopId() + " 已经录入");
                } else {
                    shopRecommendService.insertShopRecommendInfo(shopRecommendInfo);
                    json.setOk(true);
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
            json = getBeanByParam(request, shopRecommendInfo);
            if (json.isOk()) {
                json.setOk(false);
                shopRecommendService.updateShopRecommendInfo(shopRecommendInfo);
                json.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("insertShopRecommendInfo shopId:" + shopRecommendInfo.getShopId() + " error", e);
        }
        return json;
    }

    private JsonResult getBeanByParam(HttpServletRequest request, ShopRecommendInfo shopRecommendInfo) {
        JsonResult json = new JsonResult();
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


    @RequestMapping("/")
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
