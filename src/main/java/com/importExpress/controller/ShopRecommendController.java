package com.importExpress.controller;

import com.cbt.pojo.Admuser;
import com.cbt.website.util.JsonResult;
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


    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("shopRecommendList");
        try {
            if (UserInfoUtils.checkIsLogin(request)) {
                List<ShopRecommendInfo> list = shopRecommendService.queryShopRecommendInfoList();
                for(ShopRecommendInfo shopRecommendInfo : list){
                    shopRecommendInfo.setGoodsList(shopRecommendService.queryShopRecommendGoodsByShopId(shopRecommendInfo.getShopId()));
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
            shopRecommendInfo.setCreateAdminId(admuser.getId());
            json = getBeanByParam(request, shopRecommendInfo);
            if (json.isOk()) {
                if (StringUtils.isBlank(shopRecommendInfo.getShopId()) || shopRecommendInfo.getIsOn() == null ||
                        shopRecommendInfo.getSort() == null) {
                    json.setOk(false);
                    json.setMessage("获取参数失败");
                } else {
                    json.setOk(false);
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
