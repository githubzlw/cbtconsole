package com.importExpress.controller;

import com.cbt.service.CustomGoodsService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.pojo.*;
import com.cbt.warehouse.service.HotGoodsService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.Utility;
import com.importExpress.pojo.GoodsEditBean;
import com.importExpress.pojo.HotCategoryShow;
import com.importExpress.pojo.HotSellGoods;
import com.importExpress.pojo.HotSellGoodsShow;
import com.importExpress.service.HotManageService;
import com.importExpress.utli.*;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hotManage")
public class HotManageController {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(HotManageController.class);
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String HOT_FILE_LOCAL_PATH = "/data/cbtconsole/hotJson";
    private static final String HOT_UPLOAD_TO_PATH = "https://www.import-express.com/popProducts/hotFileUpload";

    // private static final String HOT_FILE_LOCAL_PATH = "E:/hotJson";
    // private static final String HOT_UPLOAD_TO_PATH = "http://127.0.0.1:8087/popProducts/hotFileUpload";



    private OKHttpUtils okHttpUtils = new OKHttpUtils();

    @Autowired
    private HotManageService hotManageService;

    @Autowired
    private HotGoodsService hotGoodsService;

    @Autowired
    private CustomGoodsService customGoodsService;

    /**
     * 分页查询类别信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryForList")
    @ResponseBody
    public EasyUiJsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }

        int startNum = 0;
        int limitNum = 40;

        String rowsStr = request.getParameter("rows");
        if (!(StringUtils.isBlank(rowsStr) || "0".equals(rowsStr))) {
            limitNum = Integer.valueOf(rowsStr);
        }

        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String isOnStr = request.getParameter("isOn");
        int isOn = -1;
        if (StringUtils.isNotBlank(isOnStr)) {
            isOn = Integer.valueOf(isOnStr);
        }

        int hotType = -1;
        String hotTypeStr = request.getParameter("hotType");
        if (StringUtils.isNotBlank(hotTypeStr)) {
            hotType = Integer.valueOf(hotTypeStr);
        }
        int webSite = 1;
        String webSiteStr = request.getParameter("webSite");
        if(StringUtils.isNotBlank(webSiteStr) && Integer.valueOf(webSiteStr) > 0){
            webSite = Integer.valueOf(webSiteStr);
        }
        HotCategory param = new HotCategory();
        try {

            param.setHotType(hotType);
            param.setIsOn(isOn);
            param.setStartNum(startNum);
            param.setLimitNum(limitNum);
            param.setWebSite(webSite);

            List<HotCategory> res = hotManageService.queryForList(param);
            SwitchDomainNameUtil.changeHotGoodsCatidList(res, webSite);
            int count = hotManageService.queryForListCount(param);

            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    /**
     * 根据ID获取类别信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getCategoryById")
    @ResponseBody
    public JsonResult getCategoryById(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String idStr = request.getParameter("id");
        int id = 0;
        if (StringUtils.isNotBlank(idStr)) {
            id = Integer.valueOf(idStr);
        } else {
            json.setOk(false);
            json.setMessage("获取ID失败");
        }

        try {
            HotCategory hotCategory = hotManageService.getCategoryById(id);
            json.setOk(true);
            json.setData(hotCategory);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("获取类别信息失败，原因：" + e.getMessage());
            LOG.error("获取类别信息失败，原因：" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/saveCategory")
    @ResponseBody
    public JsonResult saveCategory(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String category_name = request.getParameter("category_name");

        if (StringUtils.isBlank(category_name)) {
            json.setOk(false);
            json.setMessage("获取类别名称失败");
            return json;
        }

        String show_name = request.getParameter("show_name");
        if (StringUtils.isBlank(show_name)) {
            json.setOk(false);
            json.setMessage("获取显示名称失败");
            return json;
        }

        String show_img = request.getParameter("show_img");
        if (StringUtils.isBlank(show_img)) {
            show_img = "";
            /*json.setOk(false);
            json.setMessage("获取显示图片链接失败");
            return json;*/
        }

        String view_more_url = request.getParameter("view_more_url");
        if (StringUtils.isBlank(view_more_url)) {
            view_more_url = "";
        }

        String hot_typeStr = request.getParameter("hot_type");
        int hot_type = -1;
        if (StringUtils.isBlank(hot_typeStr)) {
            json.setOk(false);
            json.setMessage("获取所属类别失败");
            return json;
        } else {
            hot_type = Integer.valueOf(hot_typeStr);
        }


        String is_onStr = request.getParameter("is_on");
        int is_on = 0;
        if (StringUtils.isNotBlank(is_onStr)) {
            is_on = Integer.valueOf(is_onStr);
        }

        String enter_sortStr = request.getParameter("enter_sort");
        int enter_sort = 0;
        if (StringUtils.isNotBlank(enter_sortStr)) {
            enter_sort = Integer.valueOf(enter_sortStr);
        }

        int webSite =0;
        String webSiteStr = request.getParameter("webSite");
        if(StringUtils.isNotBlank(webSiteStr) && Integer.valueOf(webSiteStr) > 0){
            webSite = Integer.valueOf(webSiteStr);
        }else {
            json.setOk(false);
            json.setMessage("获取网站类别失败");
            return json;
        }

        HotCategory param = new HotCategory();
        try {
            param.setHotType(hot_type);
            param.setIsOn(is_on);
            param.setAdminId(user.getId());
            param.setCategoryName(category_name);
            param.setShowName(show_name);
            param.setShowImg(show_img);
            param.setSorting(enter_sort);
            param.setUpdateAdminId(user.getId());
            param.setWebSite(webSite);
            param.setViewMoreUrl(view_more_url);
            int isCheck = hotManageService.checkHotCategoryIsExists(param);
            if (isCheck > 0) {
                json.setOk(false);
                json.setMessage("当前类别或图片链接已经存在");
            } else {
                //hotManageService.insertIntoHotCategory(param);
                insertCategoryOnline(param);
                json.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存类别商品失败，原因：" + e.getMessage());
            LOG.error("保存类别商品失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/updateCategory")
    @ResponseBody
    public JsonResult updateCategory(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String category_id = request.getParameter("category_id");

        if (StringUtils.isBlank(category_id)) {
            json.setOk(false);
            json.setMessage("获取ID失败");
            return json;
        }

        String category_name = request.getParameter("category_name");

        if (StringUtils.isBlank(category_name)) {
            json.setOk(false);
            json.setMessage("获取类别名称失败");
            return json;
        }

        String show_name = request.getParameter("show_name");
        if (StringUtils.isBlank(show_name)) {
            json.setOk(false);
            json.setMessage("获取显示名称失败");
            return json;
        }

        String show_img = request.getParameter("show_img");
        if (StringUtils.isBlank(show_img)) {
            show_img = "";
            /*json.setOk(false);
            json.setMessage("获取显示图片链接失败");
            return json;*/
        }


        String view_more_url = request.getParameter("view_more_url");
        if (StringUtils.isBlank(view_more_url)) {
            view_more_url = "";
        }

        String hot_typeStr = request.getParameter("hot_type");
        int hot_type = -1;
        if (StringUtils.isBlank(hot_typeStr)) {
            json.setOk(false);
            json.setMessage("获取所属类别失败");
            return json;
        } else {
            hot_type = Integer.valueOf(hot_typeStr);
        }


        String is_onStr = request.getParameter("is_on");
        int is_on = 0;
        if (StringUtils.isNotBlank(is_onStr)) {
            is_on = Integer.valueOf(is_onStr);
        }

        String enter_sortStr = request.getParameter("enter_sort");
        int enter_sort = 0;
        if (StringUtils.isNotBlank(enter_sortStr)) {
            enter_sort = Integer.valueOf(enter_sortStr);
        }

        int webSite =0;
        String webSiteStr = request.getParameter("webSite");
        if(StringUtils.isNotBlank(webSiteStr) && Integer.valueOf(webSiteStr) > 0){
            webSite = Integer.valueOf(webSiteStr);
        }else {
            json.setOk(false);
            json.setMessage("获取网站类别失败");
            return json;
        }

        HotCategory param = new HotCategory();
        try {
            param.setId(Integer.valueOf(category_id));
            param.setHotType(hot_type);
            param.setIsOn(is_on);
            param.setCategoryName(category_name);
            param.setShowName(show_name);
            param.setShowImg(show_img);
            param.setSorting(enter_sort);
            param.setUpdateAdminId(user.getId());
            param.setWebSite(webSite);
            param.setViewMoreUrl(view_more_url);
            int isCheck = hotManageService.checkHotCategoryIsExists(param);
            if (isCheck > 0) {
                json.setOk(false);
                json.setMessage("当前类别或图片链接已经存在");
            } else {
                hotManageService.updateHotCategory(param);
                updateCategoryOnline(param);
                json.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("更新类别商品失败，原因：" + e.getMessage());
            LOG.error("更新类别商品失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/deleteCategory")
    @ResponseBody
    public JsonResult deleteCategory(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String categoryId = request.getParameter("categoryId");

        if (StringUtils.isBlank(categoryId) || "0".equals(categoryId)) {
            json.setOk(false);
            json.setMessage("获取ID失败");
            return json;
        }

        int id = Integer.valueOf(categoryId);
        try {

            hotManageService.deleteCategory(id);
            deleteCategoryOnline(id);
            json.setOk(true);
            json.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("删除类别商品失败，原因：" + e.getMessage());
            LOG.error("删除类别商品失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/queryGoodsByCategoryId")
    @ResponseBody
    public ModelAndView queryGoodsByCategoryId(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("hotGoodsList");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "请登录后操作");
            return mv;
        } else {
            mv.addObject("uid", user.getId());
        }

        String categoryIdStr = request.getParameter("categoryId");
        String webType = request.getParameter("webType");
        if(StringUtils.isBlank(webType)) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取网站类别失败");
            return mv;
        }else{
            mv.addObject("webType", webType);
        }


        if (StringUtils.isBlank(categoryIdStr)) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取类别id失败");
            return mv;
        } else {
            mv.addObject("categoryId", categoryIdStr);
        }

        try {
            HotCategory hotCategory = hotManageService.getCategoryById(Integer.valueOf(categoryIdStr));
            List<HotSellingGoods> goodsList = hotGoodsService.queryByHotSellingCategory(Integer.valueOf(categoryIdStr), hotCategory.getHotType());

            int isOnTotal = 0;
            for (HotSellingGoods goods : goodsList) {
                String goodsImgUrl = goods.getGoodsImg();
                if(goodsImgUrl.indexOf("http")>-1){
                    if(goodsImgUrl.split("http").length > 2){
                        goods.setGoodsImg("http"+ goodsImgUrl.split("http")[2]);
                    }
                }
                if("1".equals(goods.getIsOn())){
                    isOnTotal ++;
                }
                String range_price = StrUtils.object2Str(goods.getRangePrice());
                String maxPrice = "";
                if (StringUtils.isBlank(range_price) || range_price.trim().length() == 0) {
                    if (goods.getIsSoldFlag() > 0) {
                        if (StringUtils.isNotBlank(goods.getFeeprice())) {
                            List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",
                                    StrUtils.object2Str(goods.getFeeprice()));
                            if (matchStrList != null && !matchStrList.isEmpty()) {
                                range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                                if (matchStrList.size() > 1) {
                                    maxPrice = StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                                    range_price = range_price + "-" + maxPrice;
                                } else {
                                    maxPrice = range_price;
                                }
                            } else {
                                range_price = StrUtils.object2Str(goods.getGoodsPrice());
                                maxPrice = range_price;
                            }
                        }
                    } else {
                        List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)", StrUtils.object2Str(goods.getWprice()));
                        if (matchStrList != null && !matchStrList.isEmpty()) {
                            range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                            maxPrice = range_price;
                            if (matchStrList.size() > 1) {
                                maxPrice = StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                                range_price = range_price + "-" + maxPrice;
                            } else {
                                maxPrice = range_price;
                            }
                        } else {
                            range_price = StrUtils.object2Str(goods.getGoodsPrice());
                            maxPrice = range_price;
                        }
                    }
                } else {
                    if (range_price.contains("-")) {
                        maxPrice = range_price.split("-")[1].trim();
                    } else {
                        maxPrice = range_price;
                    }
                }
                if ("".equals(maxPrice)) {
                    System.err.println(maxPrice);
                }
                goods.setMaxPrice(maxPrice);
                goods.setShowPrice(range_price);
                if (hotCategory.getHotType() == 2) {
                    goods.setVirtualOldPrice(BigDecimalUtil.truncateDouble(Double.valueOf(maxPrice) * (1 + goods.getDiscountPercentage() / 100D), 2));
                }
                goods.setGoodsUrl("https://www.import-express.com/goodsinfo/" + Utility.StringFilter(goods.getShowName()) + (goods.getIsNewCloud() > 0 ? "-3" : "-1") + goods.getGoodsPid() + ".html");
            }

            SwitchDomainNameUtil.changeHotGoodsList(goodsList, Integer.valueOf(webType));
            mv.addObject("isOnTotal", isOnTotal);
            mv.addObject("allTotal", goodsList.size());
            mv.addObject("isShow", 1);
            mv.addObject("hotType", hotCategory.getHotType());
            mv.addObject("goodsList", goodsList);
            mv.addObject("categoryName", hotCategory.getShowName());
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取类别商品失败，原因：" + e.getMessage());
            LOG.error("获取类别商品失败，原因：" + e.getMessage());

        }
        return mv;
    }


    @RequestMapping(value = "/queryDiscountByHotIdAndPid")
    @ResponseBody
    public JsonResult queryDiscountByHotIdAndPid(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String categoryId = request.getParameter("categoryId");

        if (StringUtils.isBlank(categoryId)) {
            json.setOk(false);
            json.setMessage("获取ID失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");

        if (StringUtils.isBlank(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }
        try {
            HotDiscount hotDiscount = hotManageService.queryDiscountByHotIdAndPid(Integer.valueOf(categoryId), goodsPid);
            if (hotDiscount != null && hotDiscount.getHotId() > 0) {
                json.setData(hotDiscount);
                json.setTotal(1L);
            } else {
                json.setTotal(0L);
            }
            json.setOk(true);

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存类别商品失败，原因：" + e.getMessage());
            LOG.error("保存类别商品失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/insertIntoDiscount")
    @ResponseBody
    public JsonResult insertIntoDiscount(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String hotId = request.getParameter("hotId");

        if (StringUtils.isBlank(hotId)) {
            json.setOk(false);
            json.setMessage("获取类别ID失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");

        if (StringUtils.isBlank(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        String percentageStr = request.getParameter("percentage");
        double percentage = 0;
        if (StringUtils.isBlank(percentageStr) || "0".equals(percentageStr)) {
            json.setOk(false);
            json.setMessage("获取折扣百分比失败");
            return json;
        } else {
            percentage = Double.valueOf(percentageStr);
        }


        String beginTime = request.getParameter("beginTime");

        if (StringUtils.isBlank(beginTime)) {
            json.setOk(false);
            json.setMessage("获取开始时间失败");
            return json;
        }

        String endTime = request.getParameter("endTime");

        if (StringUtils.isBlank(endTime)) {
            json.setOk(false);
            json.setMessage("获取结束时间失败");
            return json;
        }


        String sortStr = request.getParameter("sort");
        int sort = 0;
        if (StringUtils.isBlank(sortStr)) {
            json.setOk(false);
            json.setMessage("获取排序值失败");
            return json;
        } else {
            sort = Integer.valueOf(sortStr);
        }

        try {

            HotDiscount hotDiscount = new HotDiscount();
            hotDiscount.setBeginTime(beginTime);
            hotDiscount.setEndTime(endTime);
            hotDiscount.setAdminId(user.getId());
            hotDiscount.setGoodsPid(goodsPid);
            hotDiscount.setHotId(Integer.valueOf(hotId));
            hotDiscount.setPercentage(percentage);
            hotDiscount.setSort(sort);
            hotManageService.insertIntoDiscount(hotDiscount);
            //更新线上
            insertDiscountOnline(hotDiscount);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存商品折扣信息失败，原因：" + e.getMessage());
            LOG.error("保存商品折扣信息失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/updateDiscountInfo")
    @ResponseBody
    public JsonResult updateDiscountInfo(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String idStr = request.getParameter("id");

        if (StringUtils.isBlank(idStr)) {
            json.setOk(false);
            json.setMessage("获取数据ID失败");
            return json;
        }

        String hotId = request.getParameter("hotId");

        if (StringUtils.isBlank(hotId)) {
            json.setOk(false);
            json.setMessage("获取类别ID失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");

        if (StringUtils.isBlank(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        String percentageStr = request.getParameter("percentage");
        double percentage = 0;
        if (StringUtils.isBlank(percentageStr) || "0".equals(percentageStr)) {
            json.setOk(false);
            json.setMessage("获取折扣百分比失败");
            return json;
        } else {
            percentage = Double.valueOf(percentageStr);
        }


        String beginTime = request.getParameter("beginTime");

        if (StringUtils.isBlank(beginTime)) {
            json.setOk(false);
            json.setMessage("获取开始时间失败");
            return json;
        }

        String endTime = request.getParameter("endTime");

        if (StringUtils.isBlank(endTime)) {
            json.setOk(false);
            json.setMessage("获取结束时间失败");
            return json;
        }


        String sortStr = request.getParameter("sort");
        int sort = 0;
        if (StringUtils.isBlank(sortStr)) {
            json.setOk(false);
            json.setMessage("获取排序值失败");
            return json;
        } else {
            sort = Integer.valueOf(sortStr);
        }

        try {

            HotDiscount hotDiscount = new HotDiscount();
            hotDiscount.setId(Integer.valueOf(idStr));
            hotDiscount.setBeginTime(beginTime);
            hotDiscount.setEndTime(endTime);
            hotDiscount.setAdminId(user.getId());
            hotDiscount.setGoodsPid(goodsPid);
            hotDiscount.setHotId(Integer.valueOf(hotId));
            hotDiscount.setPercentage(percentage);
            hotDiscount.setSort(sort);
            hotManageService.updateDiscountInfo(hotDiscount);
            //更新线上
            updateDiscountOnline(hotDiscount);
            json.setOk(true);


        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存商品折扣信息失败，原因：" + e.getMessage());
            LOG.error("保存商品折扣信息失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/insertIntoEvaluationInfo")
    @ResponseBody
    public JsonResult insertIntoEvaluationInfo(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String skuId = request.getParameter("skuId");

        if (StringUtils.isBlank(skuId)) {
            json.setOk(false);
            json.setMessage("获取SkuId失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");

        if (StringUtils.isBlank(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        String userIdStr = request.getParameter("userId");
        int userId = 0;
        if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
            json.setOk(false);
            json.setMessage("获取客户ID失败");
            return json;
        } else {
            userId = Integer.valueOf(userIdStr);
        }


        String content = request.getParameter("content");

        if (StringUtils.isBlank(content)) {
            json.setOk(false);
            json.setMessage("获取评价内容失败");
            return json;
        }

        String evaluationTime = request.getParameter("evaluationTime");

        if (StringUtils.isBlank(evaluationTime)) {
            json.setOk(false);
            json.setMessage("获取评价时间失败");
            return json;
        }


        String evaluationLevelStr = request.getParameter("evaluationLevel");
        int evaluationLevel = 0;
        if (StringUtils.isBlank(evaluationLevelStr)) {
            json.setOk(false);
            json.setMessage("获取排序值失败");
            return json;
        } else {
            evaluationLevel = Integer.valueOf(evaluationLevelStr);
        }

        String serviceLevelStr = request.getParameter("service_level");
        int serviceLevel = 0;
        if (StringUtils.isBlank(serviceLevelStr)) {
            json.setOk(false);
            json.setMessage("获取排序值失败");
            return json;
        } else {
            serviceLevel = Integer.valueOf(serviceLevelStr);
        }

        try {

            HotEvaluation hotEvaluation = new HotEvaluation();
            hotEvaluation.setAdminId(user.getId());
            hotEvaluation.setContent(content);
            hotEvaluation.setEvaluationLevel(evaluationLevel);
            hotEvaluation.setEvaluationTime(evaluationTime);
            hotEvaluation.setGoodsPid(goodsPid);
            hotEvaluation.setServiceLevel(serviceLevel);
            hotEvaluation.setSkuId(skuId);
            hotEvaluation.setUserId(userId);
            hotManageService.insertIntoHotEvaluation(hotEvaluation);
            //更新线上
            insertEvaluationOnline(hotEvaluation);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存商品评价信息失败，原因：" + e.getMessage());
            LOG.error("保存商品评价信息失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/updateEvaluationInfo")
    @ResponseBody
    public JsonResult updateEvaluationInfo(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String idStr = request.getParameter("id");

        if (StringUtils.isBlank(idStr)) {
            json.setOk(false);
            json.setMessage("获取数据ID失败");
            return json;
        }

        String skuId = request.getParameter("skuId");

        if (StringUtils.isBlank(skuId)) {
            json.setOk(false);
            json.setMessage("获取SkuId失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");

        if (StringUtils.isBlank(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        String userIdStr = request.getParameter("userId");
        int userId = 0;
        if (StringUtils.isBlank(userIdStr) || "0".equals(userIdStr)) {
            json.setOk(false);
            json.setMessage("获取客户ID失败");
            return json;
        } else {
            userId = Integer.valueOf(userIdStr);
        }


        String content = request.getParameter("content");

        if (StringUtils.isBlank(content)) {
            json.setOk(false);
            json.setMessage("获取评价内容失败");
            return json;
        }

        String evaluationTime = request.getParameter("evaluationTime");

        if (StringUtils.isBlank(evaluationTime)) {
            json.setOk(false);
            json.setMessage("获取评价时间失败");
            return json;
        }


        String evaluationLevelStr = request.getParameter("evaluationLevel");
        int evaluationLevel = 0;
        if (StringUtils.isBlank(evaluationLevelStr)) {
            json.setOk(false);
            json.setMessage("获取排序值失败");
            return json;
        } else {
            evaluationLevel = Integer.valueOf(evaluationLevelStr);
        }

        String serviceLevelStr = request.getParameter("service_level");
        int serviceLevel = 0;
        if (StringUtils.isBlank(serviceLevelStr)) {
            json.setOk(false);
            json.setMessage("获取排序值失败");
            return json;
        } else {
            serviceLevel = Integer.valueOf(serviceLevelStr);
        }

        try {

            HotEvaluation hotEvaluation = new HotEvaluation();
            hotEvaluation.setId(Integer.valueOf(idStr));
            hotEvaluation.setAdminId(user.getId());
            hotEvaluation.setContent(content);
            hotEvaluation.setEvaluationLevel(evaluationLevel);
            hotEvaluation.setEvaluationTime(evaluationTime);
            hotEvaluation.setGoodsPid(goodsPid);
            hotEvaluation.setServiceLevel(serviceLevel);
            hotEvaluation.setSkuId(skuId);
            hotEvaluation.setUserId(userId);
            hotManageService.updateHotEvaluation(hotEvaluation);
            //更新线上
            updateEvaluationOnline(hotEvaluation);
            json.setOk(true);


        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存商品折扣信息失败，原因：" + e.getMessage());
            LOG.error("保存商品折扣信息失败，原因：" + e.getMessage());
        }
        return json;
    }


    /**
	 * 刷新热卖商品json文件
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refreshHotJson")
	@ResponseBody
	public int refreshHotJson(HttpServletRequest request,HttpServletResponse response){

		try {
		    HotCategory param = new HotCategory();
		    param.setIsOn(1);
			for (int hotType = 1; hotType < 8; hotType++) {
				List<HotCategoryShow> resultList = new ArrayList<HotCategoryShow>();
				param.setHotType(hotType);
				List<HotCategory> categoryList = hotManageService.queryCategoryList(param);
				List<HotSellGoods> hotGoodsList = hotManageService.queryGoodsByHotType(hotType);
				genHotGoodsData(categoryList,hotGoodsList,resultList);
				//匹配文件信息
				String fileName = "";
				if (hotType == 1) {
					fileName = "queryAllHotGoodsOld.json";
				} else if (hotType == 2) {
					fileName = "index-new-C2.json";
				} else if (hotType == 3) {
					fileName = "index-new-C3.json";
				} else if (hotType == 4) {
					fileName = "queryGoodsByHotTypePanda.json";
				} else if (hotType == 5) {
					fileName = "queryGoodsByHotTypeAlisa.json";
				} else if (hotType == 6) {
					fileName = "queryAllHotGoodsMobile.json";
				} else if (hotType == 7) {
					fileName = "index-new-C4.json";
				}
				writeHotGoodsJson(resultList, fileName);
				categoryList.clear();
				hotGoodsList.clear();
				resultList.clear();
			}
			// 刷新到线上
            boolean isSuccess = uploadFileToOnline();
			if(isSuccess){
			    return 1;
            }else{
			    return 0;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}



	@RequestMapping(value = "/useGoodsPromotionFlag")
    @ResponseBody
    public JsonResult useGoodsPromotionFlag(HttpServletRequest request, HttpServletResponse response) {

        GoodsEditBean editBean = new GoodsEditBean();
        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        } else {
            editBean.setAdmin_id(user.getId());
        }

        String pids = request.getParameter("pids");
        if (StringUtils.isBlank(pids)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        try {
            editBean.setPromotion_flag(1);
            String[] pidList = pids.split(",");
            for (String pid : pidList) {
                if (StringUtils.isNotBlank(pid)) {
                    editBean.setPid(pid);
                    customGoodsService.updatePidIsEdited(editBean);
                    customGoodsService.insertIntoGoodsEditBean(editBean);
                    customGoodsService.updatePromotionFlag(pid);
                }
            }
            json.setOk(true);
            json.setMessage("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("更新热卖商品热销标识失败，原因：" + e.getMessage());
            LOG.error("更新热卖商品热销标识失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/getHotTypeList")
    @ResponseBody
    public EasyUiJsonResult getHotTypeList(Integer hotType) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        if(hotType == null || hotType< 0){
            json.setSuccess(false);
            json.setMessage("获取类别失败");
            return json;
        }

        HotCategory param = new HotCategory();
        try {

            param.setHotType(hotType);
            param.setIsOn(-1);

            List<HotCategory> res = hotManageService.queryForList(param);
            int count = hotManageService.queryForListCount(param);

            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/getClassInfoList")
    @ResponseBody
    public JsonResult getClassInfoList(@RequestParam(value = "jsonName", required = false) String jsonName,
                                       @RequestParam(value = "className", required = false) String className, int rows, int page) {
        JsonResult json = new JsonResult();
        HotClassInfo classInfo = new HotClassInfo();

        if(StringUtils.isNotBlank(jsonName)){
            classInfo.setJsonName(jsonName);
        }
        if(StringUtils.isNotBlank(className)){
            classInfo.setClassName(className);
        }
        if (rows > 0 && page > 0) {
            classInfo.setStartNum((page - 1) * rows);
            classInfo.setLimitNum(rows);
        }
        try {
            List<HotClassInfo> list = hotManageService.getClassInfoList(classInfo);
            int length = 0;
            if (list == null) {
                list = new ArrayList<>();
            } else {
                length = list.size();
                if (rows > 1) {
                    // 分页
                    list = list.stream().skip(classInfo.getStartNum()).limit(classInfo.getLimitNum()).collect(Collectors.toList());
                }
            }
            json.setOk(true);
            json.setRows(list);
            json.setTotal((long) length);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/queryClassInfoList")
    @ResponseBody
    public JsonResult queryClassInfoList(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        HotClassInfo classInfo = new HotClassInfo();
        try {
            List<HotClassInfo> list = hotManageService.getClassInfoList(classInfo);
            int length = list.size();
            json.setOk(true);
            json.setData(list);
            json.setTotal((long) length);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/getClassInfoById")
    @ResponseBody
    public JsonResult getClassInfoById(@RequestParam(value = "id", required = false) Integer id) {

        JsonResult json = new JsonResult();

        HotClassInfo classInfo = new HotClassInfo();
        if (id == null || id < 0) {
            json.setOk(false);
            json.setMessage("获取参数失败");
            return json;
        }
        try {
            List<HotClassInfo> list = hotManageService.getClassInfoList(classInfo);
            HotClassInfo classInfoRs = null;
            if (list != null && list.size() > 0) {
                for(HotClassInfo info : list){
                    if(info.getId() == id){
                        classInfoRs = info;
                        break;
                    }
                }
                list.clear();
                if(classInfoRs == null){
                    json.setOk(false);
                    json.setMessage("获取信息失败");
                    return json;
                }
            } else {
                json.setOk(false);
                json.setMessage("获取结果失败");
                return json;
            }
            json.setOk(true);
            json.setRows(classInfoRs);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("getClassInfoById查询失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("getClassInfoById查询失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/setClassInfo")
    @ResponseBody
    public JsonResult setClassInfo(HttpServletRequest request,
                                   @RequestParam(value = "id", required = false) Integer id,
                                   @RequestParam(value = "className", required = true) String className,
                                   @RequestParam(value = "jsonName", required = true) String jsonName) {

        JsonResult json = new JsonResult();

        HotClassInfo classInfo = new HotClassInfo();
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后重试");
            return json;
        }
        if (StringUtils.isBlank(className)) {
            json.setOk(false);
            json.setMessage("获取分组名称失败");
            return json;
        }
        if (StringUtils.isBlank(jsonName)) {
            json.setOk(false);
            json.setMessage("获取json名称失败");
            return json;
        }else if(!jsonName.endsWith(".json")){
            jsonName += ".json";
        }
        try {
            classInfo.setClassName(className);
            classInfo.setJsonName(jsonName);
            if (id == null || id < 1) {
                classInfo.setAdminId(admuser.getId());
                insertHotClassInfoOnline(classInfo);
                hotManageService.insertIntoHotClassInfo(classInfo);
            } else {
                classInfo.setId(id);
                classInfo.setUpdateAdminId(admuser.getId());
                updateHotClassInfoOnline(classInfo);
                hotManageService.updateIntoHotClassInfo(classInfo);
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("setClassInfo失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("setClassInfo失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/deleteClassIfo")
    @ResponseBody
    public JsonResult deleteClassIfo(HttpServletRequest request,
                                   @RequestParam(value = "id", required = true) Integer id) {

        JsonResult json = new JsonResult();
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后重试");
            return json;
        }
        if (id == null || id < 1) {
            json.setOk(false);
            json.setMessage("获取分组名称失败");
            return json;
        }
        try {
            deleteHotClassInfoOnline(id);
            hotManageService.deleteHotClassInfo(id);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("deleteClassIfo失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("deleteClassIfo失败，原因:" + e.getMessage());
        }
        return json;
    }

	/**
	 * 热卖json信息写入
	 * @param resultList
	 * @param fileName
	 */
	private void writeHotGoodsJson(List<HotCategoryShow> resultList,String fileName) {
		FileWriter fw = null;
		PrintWriter out = null;
		try {
			File patentFile = new File(HOT_FILE_LOCAL_PATH);
			if(!(patentFile.exists() && patentFile.isDirectory())){
				patentFile.mkdirs();
			}
			fw = new FileWriter(HOT_FILE_LOCAL_PATH + "/" + fileName, false);
			out = new PrintWriter(fw);
			//JSONObject dataJson = JSONObject.fromObject(resultList);
			out.write(JSONArray.fromObject(resultList).toString());
			out.println();

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(fileName + " writeHotGoodsJson,error:" + e.getMessage());
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
			}
		}
	}

	private boolean uploadFileToOnline() {
        boolean isSuccess = false;
        File file = new File(HOT_FILE_LOCAL_PATH);
        if (file.exists() && file.isDirectory()) {
            File[] childList = file.listFiles();
            int total = childList.length;
            int count = 0;
            boolean childSuccess;
            for (File child : childList) {
                try {
                    childSuccess = okHttpUtils.postFileNoParam(HOT_UPLOAD_TO_PATH, child);
                    if(childSuccess){
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("uploadFileToOnline,error:" + e.getMessage());
                    LOG.error("uploadFileToOnline,error:" + e.getMessage());
                }
            }
            isSuccess = (total == count);
        } else {
            System.err.println("本次获取文件失败，无法更新到线上");
            LOG.error("uploadFileToOnline,无法更新到线上");
        }
        return isSuccess;
    }


	private void genHotGoodsData(List<HotCategory> categoryList,List<HotSellGoods> hotGoodsList,List<HotCategoryShow> resultList) {
		for (HotCategory hotCategory : categoryList) {
			HotCategoryShow categoryShow = new HotCategoryShow();
			categoryShow.setHotType(hotCategory.getHotType());
			categoryShow.setId(hotCategory.getId());
			categoryShow.setShowImg(hotCategory.getShowImg());
			categoryShow.setShowName(hotCategory.getShowName());
			categoryShow.setSorting(hotCategory.getSorting());
			resultList.add(categoryShow);
		}
		//使用商品匹配类别
		for (HotSellGoods hotGd : hotGoodsList) {

			HotSellGoodsShow sellGoodsShow = new HotSellGoodsShow();
			sellGoodsShow.setAmazon_price(hotGd.getAmazon_price());
			sellGoodsShow.setAsin_code(hotGd.getAsin_code());
			sellGoodsShow.setDiscountBeginTime(hotGd.getDiscountBeginTime());
			sellGoodsShow.setDiscountEndTime(hotGd.getDiscountEndTime());
			sellGoodsShow.setDiscountId(hotGd.getDiscountId());
			sellGoodsShow.setPrice1688(hotGd.getPrice1688());
			sellGoodsShow.setDiscountPercentage(hotGd.getDiscountPercentage());
			if (hotGd.getDiscountPercentage() > 0) {
				double discountPrice = Double.valueOf(hotGd.getMaxPrice()) * ( 1 + hotGd.getDiscountPercentage() / 100D) ;
				sellGoodsShow.setDiscountPrice(BigDecimalUtil.truncateDouble(discountPrice, 2));
				sellGoodsShow.setPrice1688(hotGd.getMaxPrice());
			}
			sellGoodsShow.setDiscountSort(hotGd.getDiscountSort());
			sellGoodsShow.setGoods_import_url(hotGd.getGoods_import_url());
			sellGoodsShow.setGoods_img(hotGd.getGoods_img());
			sellGoodsShow.setGoods_pid(hotGd.getGoods_pid());
			sellGoodsShow.setGoods_price(hotGd.getGoods_price());
			sellGoodsShow.setGoods_unit(hotGd.getGoods_unit());
			sellGoodsShow.setHot_id(Integer.valueOf(hotGd.getHot_selling_id()));
			sellGoodsShow.setMoq(hotGd.getMoq());
			sellGoodsShow.setPrice_show(hotGd.getPrice_show());
			sellGoodsShow.setProfit_margin(hotGd.getProfit_margin());
			sellGoodsShow.setRangePrice(hotGd.getRangePrice());
			sellGoodsShow.setShow_name(hotGd.getShow_name());

			for (HotCategoryShow hotCategoryShow : resultList) {
				if (hotCategoryShow.getId() == sellGoodsShow.getHot_id()) {
					if (hotCategoryShow.getGoodsList() == null || hotCategoryShow.getGoodsList().size() == 0) {
						hotCategoryShow.setGoodsList(new ArrayList<HotSellGoodsShow>());
					}
					hotCategoryShow.getGoodsList().add(sellGoodsShow);
				}
			}
		}
	}


    private void insertCategoryOnline(HotCategory param) {
        String show_name = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(param.getShowName());
        String category_name = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(param.getCategoryName());
        String sql = "insert into hot_category(category_name,show_name,show_img,is_on,sorting,hot_type,admin_id,web_site,view_more_url)" +
                " values('" + category_name + "','" + show_name + "','" + param.getShowImg() + "'," + param.getIsOn()
                + "," + param.getSorting() + "," + param.getHotType() + "," + param.getAdminId() + ","
                + param.getWebSite() + ",'" + param.getViewMoreUrl() + "')";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }

    private void updateCategoryOnline(HotCategory param) {
        String show_name = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(param.getShowName());
        String category_name = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(param.getCategoryName());
        String sql = "update hot_category set" +
                " category_name='" + category_name + "',show_name='" + show_name + "',show_img='" + param.getShowImg()
                + "',is_on=" + param.getIsOn()
                + ",sorting=" + param.getSorting() + ",hot_type=" + param.getHotType() + ",update_admin_id=" + param.getAdminId()
                + ",web_site=" + param.getWebSite()
                + ",view_more_url='" + param.getViewMoreUrl() + "'"
                + " where id = " + param.getId();
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void insertDiscountOnline(HotDiscount hotDiscount) {

        AutoSub8Hour(hotDiscount);
        String sql = "insert into hot_goods_discount(hot_id,goods_pid,percentage,begin_time,end_time,admin_id,sort) values(";
        sql += hotDiscount.getHotId() + ",'" + hotDiscount.getGoodsPid() + "'," + hotDiscount.getPercentage()
                + ",'" + hotDiscount.getBeginTime() + "','" + hotDiscount.getEndTime() + "'," + hotDiscount.getAdminId()
                + "," + hotDiscount.getSort() + ")";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void updateDiscountOnline(HotDiscount hotDiscount) {

        AutoSub8Hour(hotDiscount);
        String sql = "update hot_goods_discount set percentage= " + hotDiscount.getPercentage() + ",begin_time= '" + hotDiscount.getBeginTime() +
                "',end_time= '" + hotDiscount.getEndTime() + "',admin_id= " + hotDiscount.getAdminId() + ",sort= " + hotDiscount.getSort() +
                "        where hot_id = " + hotDiscount.getHotId() + " and goods_pid='" + hotDiscount.getGoodsPid() + "'";
        System.err.println(sql);
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }

    private void AutoSub8Hour(HotDiscount hotDiscount){
        try{

            Calendar calendar = Calendar.getInstance();

            if(StringUtils.isNotBlank(hotDiscount.getBeginTime())){
                calendar.setTime(DATE_FORMAT.parse(hotDiscount.getBeginTime()));
                calendar.add(Calendar.HOUR_OF_DAY,-8);
                hotDiscount.setBeginTime(DATE_FORMAT.format(calendar.getTime()));
            }
            if(StringUtils.isNotBlank(hotDiscount.getEndTime())){
                calendar.setTime(DATE_FORMAT.parse(hotDiscount.getEndTime()));
                calendar.add(Calendar.HOUR_OF_DAY,-8);
                hotDiscount.setEndTime(DATE_FORMAT.format(calendar.getTime()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void insertEvaluationOnline(HotEvaluation hotEvaluation) {
        String content = hotEvaluation.getContent();
        if (StringUtils.isNotBlank(content)) {
            content = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(content);
        } else {
            content = "";
        }
        String sql = "insert into hot_goods_evaluation(goods_pid,sku_id,user_id,content,evaluation_level," +
                "service_level,admin_id,evaluation_time) " +
                "values('" + hotEvaluation.getGoodsPid() + "','" + hotEvaluation.getSkuId() + "'," + hotEvaluation.getUserId() + ",'"
                + content + "'," + hotEvaluation.getEvaluationLevel() + "," + hotEvaluation.getServiceLevel() + ","
                + hotEvaluation.getAdminId() + ",'" + hotEvaluation.getEvaluationTime() + "')";
        NotifyToCustomerUtil.sendSqlByMq(sql);

    }


    private void updateEvaluationOnline(HotEvaluation hotEvaluation) {
        String content = hotEvaluation.getContent();
        if (StringUtils.isNotBlank(content)) {
            content = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(content);
        } else {
            content = "";
        }
        String sql = "update hot_goods_evaluation set goods_pid='" + hotEvaluation.getGoodsPid() + "'," +
                "sku_id='" + hotEvaluation.getSkuId() + "',user_id=" + hotEvaluation.getUserId() + ",content='" + content + "'," +
                "evaluation_level=" + hotEvaluation.getEvaluationLevel() + "," +
                "service_level=" + hotEvaluation.getServiceLevel() + ",admin_id=" + hotEvaluation.getAdminId() + "," +
                "evaluation_time='+" + hotEvaluation.getEvaluationTime() + "' where goods_pid='" + hotEvaluation.getGoodsPid() + "'";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void deleteCategoryOnline(int id) {
        String deleteGoods = "delete from hot_selling_goods where hot_selling_id = " + id;
        NotifyToCustomerUtil.sendSqlByMq(deleteGoods);
        String sql = "delete from hot_category where id = " + id;
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void insertHotClassInfoOnline(HotClassInfo hotClassInfo) {

        String sql = "insert into hot_class_info(class_name,json_name,admin_id) values(";
        sql += "'" + hotClassInfo.getClassName() + "','" + hotClassInfo.getJsonName()
                + "'," + hotClassInfo.getAdminId() + ")";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void updateHotClassInfoOnline(HotClassInfo hotClassInfo) {

        String sql = "update hot_class_info set class_name = '" + hotClassInfo.getClassName()
                + "', json_name = '" + hotClassInfo.getJsonName() + "',update_admin_id = " + hotClassInfo.getUpdateTime()
                + " where id = " + hotClassInfo.getId();
        System.err.println(sql);
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }

    private void deleteHotClassInfoOnline(int id){
	    String sql = "delete from hot_class_info where id = " + id;
	    NotifyToCustomerUtil.sendSqlByMq(sql);
    }

}
