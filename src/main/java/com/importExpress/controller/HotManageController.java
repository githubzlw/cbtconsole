package com.importExpress.controller;

import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.pojo.HotCategory;
import com.cbt.warehouse.pojo.HotDiscount;
import com.cbt.warehouse.pojo.HotEvaluation;
import com.cbt.warehouse.pojo.HotSellingGoods;
import com.cbt.warehouse.service.HotGoodsService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.service.HotManageService;
import com.importExpress.utli.NotifyToCustomerUtil;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/hotManage")
public class HotManageController {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(HotManageController.class);
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private HotManageService hotManageService;

    @Autowired
    private HotGoodsService hotGoodsService;

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
        int limitNum = 25;
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
        HotCategory param = new HotCategory();
        try {

            param.setHotType(hotType);
            param.setIsOn(isOn);
            param.setStartNum(startNum);
            param.setLimitNum(limitNum);

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
            json.setOk(false);
            json.setMessage("获取显示图片链接失败");
            return json;
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
            json.setOk(false);
            json.setMessage("获取显示图片链接失败");
            return json;
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
                goods.setGoodsUrl("https://www.import-express.com/goodsinfo/" + goods.getShowName() + (goods.getIsNewCloud() > 0 ? "-3" : "-1") + goods.getGoodsPid() + ".html");
            }
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


    private void insertCategoryOnline(HotCategory param) {
        String show_name = param.getShowName();
        if (show_name.contains("'")) {
            show_name = show_name.replace("'", "\\'");
        }
        if (show_name.contains("\"")) {
            show_name = show_name.replace("\"", "\\\"");
        }
        String category_name = param.getCategoryName();
        if (category_name.contains("'")) {
            category_name = category_name.replace("'", "\\'");
        }
        if (category_name.contains("\"")) {
            category_name = category_name.replace("\"", "\\\"");
        }
        String sql = "insert into hot_category(category_name,show_name,show_img,is_on,sorting,hot_type,admin_id)" +
                " values('" + category_name + "','" + show_name + "','" + param.getShowImg() + "'," + param.getIsOn()
                + "," + param.getSorting() + "," + param.getHotType() + "," + param.getAdminId() + ")";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }

    private void updateCategoryOnline(HotCategory param) {
        String show_name = param.getShowName();
        if (show_name.contains("'")) {
            show_name = show_name.replace("'", "\\'");
        }
        if (show_name.contains("\"")) {
            show_name = show_name.replace("\"", "\\\"");
        }
        String category_name = param.getCategoryName();
        if (category_name.contains("'")) {
            category_name = category_name.replace("'", "\\'");
        }
        if (category_name.contains("\"")) {
            category_name = category_name.replace("\"", "\\\"");
        }
        String sql = "update hot_category set" +
                " category_name='" + category_name + "',show_name='" + show_name + "',show_img='" + param.getShowImg()
                + "',is_on=" + param.getIsOn()
                + ",sorting=" + param.getSorting() + ",hot_type=" + param.getHotType() + ",update_admin_id=" + param.getAdminId()
                + " where id = " + param.getId();
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void insertDiscountOnline(HotDiscount hotDiscount) {

        try {

            Calendar calendar = Calendar.getInstance();

            if (StringUtils.isNotBlank(hotDiscount.getBeginTime())) {
                calendar.setTime(DATE_FORMAT.parse(hotDiscount.getBeginTime()));
                calendar.add(Calendar.HOUR_OF_DAY, -8);
                hotDiscount.setBeginTime(DATE_FORMAT.format(calendar.getTime()));
            }
            if (StringUtils.isNotBlank(hotDiscount.getEndTime())) {
                calendar.setTime(DATE_FORMAT.parse(hotDiscount.getEndTime()));
                calendar.add(Calendar.HOUR_OF_DAY, -8);
                hotDiscount.setEndTime(DATE_FORMAT.format(calendar.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "insert into hot_goods_discount(hot_id,goods_pid,percentage,begin_time,end_time,admin_id,sort) values(";
        sql += hotDiscount.getHotId() + ",'" + hotDiscount.getGoodsPid() + "'," + hotDiscount.getPercentage()
                + ",'" + hotDiscount.getBeginTime() + "','" + hotDiscount.getEndTime() + "'," + hotDiscount.getAdminId()
                + "," + hotDiscount.getSort() + ")";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void updateDiscountOnline(HotDiscount hotDiscount) {

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

        String sql = "update hot_goods_discount set percentage= " + hotDiscount.getPercentage() + ",begin_time= '" + hotDiscount.getBeginTime() +
                "',end_time= '" + hotDiscount.getEndTime() + "',admin_id= " + hotDiscount.getAdminId() + ",sort= " + hotDiscount.getSort() +
                "        where hot_id = " + hotDiscount.getHotId() + " and goods_pid='" + hotDiscount.getGoodsPid() + "'";
        System.err.println(sql);
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    private void insertEvaluationOnline(HotEvaluation hotEvaluation) {
        String content = hotEvaluation.getContent();
        if (StringUtils.isNotBlank(content)) {
            if (content.contains("'")) {
                content = content.replace("'", "\\'");
            }
            if (content.contains("\"")) {
                content = content.replace("\"", "\\\"");
            }
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
            if (content.contains("'")) {
                content = content.replace("'", "\\'");
            }
            if (content.contains("\"")) {
                content = content.replace("\"", "\\\"");
            }
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


}
