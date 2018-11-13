package com.importExpress.controller;

import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Redis;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.AliBenchmarkingBean;
import com.importExpress.pojo.AliBenchmarkingStatistic;
import com.importExpress.pojo.KeyWordBean;
import com.importExpress.service.AliBeanchmarkingService;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 对标商品查询和统计
 */
@Controller
@RequestMapping("/aliBeanchmarking")
public class AliBeanchmarkingController {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(AliBeanchmarkingController.class);

    @Autowired
    private AliBeanchmarkingService beanchmarkingService;

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title queryForList
     * @Description 根据条件查询列表数据
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

        AliBenchmarkingBean benchmarkingBean = new AliBenchmarkingBean();
        int startNum = 0;
        int limitNum = 30;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String pid = request.getParameter("pid");
        if (StringUtils.isNotBlank(pid)) {
            benchmarkingBean.setPid(pid);
        }
        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isNotBlank(aliPid)) {
            benchmarkingBean.setAliPid(aliPid);
        }
        String beginTime = request.getParameter("beginTime");
        if (StringUtils.isNotBlank(beginTime)) {
            benchmarkingBean.setBeginDate(beginTime);
        }

        String endTime = request.getParameter("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            benchmarkingBean.setEndDate(endTime);
        }
        String admidStr = request.getParameter("admId");
        if (!(StringUtils.isBlank(admidStr) || "0".equals(admidStr))) {
            benchmarkingBean.setAdminId(Integer.valueOf(admidStr));
        }
        String isEdited = request.getParameter("isEdited");
        if (!(StringUtils.isBlank(isEdited) || "0".equals(isEdited))) {
            benchmarkingBean.setIsEdited(Integer.valueOf(isEdited));
        }
        String isOnline = request.getParameter("isOnline");
        if (!(StringUtils.isBlank(isOnline) || "0".equals(isOnline))) {
            benchmarkingBean.setIsOnline(Integer.valueOf(isOnline));
        }
        String category = request.getParameter("category");
        if (StringUtils.isNotBlank(category) && !"0".equals(category)) {
            benchmarkingBean.setCategory(category);
        }
        try {
            benchmarkingBean.setLimitNum(limitNum);
            benchmarkingBean.setStartNum(startNum);
            List<AliBenchmarkingBean> list = beanchmarkingService.queryAliBenchmarkingForList(benchmarkingBean);

            int total = beanchmarkingService.queryAliBenchmarkingForListCount(benchmarkingBean);

            json.setSuccess(true);
            json.setRows(list);
            json.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    /**
     * 执行统计查询
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryStatistic")
    @ResponseBody
    public EasyUiJsonResult queryStatistic(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }
        String admidStr = request.getParameter("admId");
        int adminId = 0;
        if (!(StringUtils.isBlank(admidStr) || "0".equals(admidStr))) {
            adminId = Integer.valueOf(admidStr);
        }
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String admName = request.getParameter("admName");
        admName = StringUtil.isBlank(admName) ? null : admName;
        try {
            List<AliBenchmarkingStatistic> list = beanchmarkingService.queryAliBenchmarkingStatistic(adminId, beginDate, endDate, admName);
            for (AliBenchmarkingStatistic statistic : list) {
                if (statistic.getBenchmarkingTotalNum() > 0) {
                    float isEditedRate = (100.0F * statistic.getEditedNum()) / (1.0F * statistic.getBenchmarkingTotalNum());
                    statistic.setIsEditedRate(BigDecimalUtil.truncateFloat(isEditedRate, 2));
                    //上线的百分比=上线产品数量/编辑过的数量
                    float onlineRate = (100.0F * statistic.getOnlineNum()) / (1.0F * statistic.getBenchmarkingTotalNum());
                    statistic.setOnlineRate(BigDecimalUtil.truncateFloat(onlineRate, 2));
                }
            }
            json.setSuccess(true);
            json.setRows(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    /**
     * 查询对应adminId的关键词列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryKeyWordListByAdminId")
    @ResponseBody
    public EasyUiJsonResult queryKeyWordListByAdminId(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admidStr = request.getParameter("adminId");
        int adminId = 0;
        if (!(StringUtils.isBlank(admidStr) || "0".equals(admidStr))) {
            adminId = Integer.valueOf(admidStr);
        } else {
            json.setSuccess(false);
            json.setMessage("获取用户ID失败");
        }
        int startNum = 0;
        int limitNum = 25;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        try {
            List<KeyWordBean> list = beanchmarkingService.queryKeyWordListByAdminId(adminId, startNum, limitNum);
            int total = beanchmarkingService.queryKeyWordListByAdminIdCount(adminId);
            json.setSuccess(true);
            json.setRows(list);
            json.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/queryKeyWordDetails")
    @ResponseBody
    public EasyUiJsonResult queryKeyWordDetails(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();

        AliBenchmarkingBean benchmarkingBean = new AliBenchmarkingBean();
        int startNum = 0;
        int limitNum = 30;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String adminIdStr = request.getParameter("adminId");
        if (!(StringUtils.isBlank(adminIdStr) || "0".equals(adminIdStr))) {
            benchmarkingBean.setAdminId(Integer.valueOf(adminIdStr));
        }
        String keyword = request.getParameter("keyword");
        if (StringUtils.isBlank(keyword)) {
            json.setSuccess(false);
            json.setMessage("获取关键词失败");
            return json;
        }
        try {
            benchmarkingBean.setLimitNum(limitNum);
            benchmarkingBean.setStartNum(startNum);
            benchmarkingBean.setKeyword(keyword);
            List<AliBenchmarkingBean> list = beanchmarkingService.queryAliBenchmarkingForList(benchmarkingBean);
            int total = beanchmarkingService.queryAliBenchmarkingForListCount(benchmarkingBean);

            json.setSuccess(true);
            json.setRows(list);
            json.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/saveGoodsFlag")
    @ResponseBody
    public JsonResult saveGoodsFlag(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();


        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取PID失败");
            return json;
        }
        String priceContent = request.getParameter("priceContent");
        if (StringUtils.isBlank(priceContent)) {
            json.setOk(false);
            json.setMessage("获取价格失败");
            return json;
        }

        String freeFlagStr = request.getParameter("freeFlag");
        int freeFlag = 0;
        if (StringUtils.isNotBlank(freeFlagStr)) {
            freeFlag = Integer.valueOf(freeFlagStr);
        } else {
            json.setOk(false);
            json.setMessage("获取免邮标识失败");
            return json;
        }

        String benchmarkingFlagStr = request.getParameter("benchmarkingFlag");
        int benchmarkingFlag = 0;
        if (StringUtils.isNotBlank(benchmarkingFlagStr)) {
            benchmarkingFlag = Integer.valueOf(benchmarkingFlagStr);
        } else {
            json.setOk(false);
            json.setMessage("获取对标标识失败");
            return json;
        }
        try {
            //解析moq和最大最小价格
            String[] priceList = priceContent.split(",");
            int moq = 0;
            double minPrice = 0;
            double maxPrice = 0;
            int tempMoq = 0;
            double tempPrice = 0;
            for (String priceChild : priceList) {
                if (StringUtils.isNotBlank(priceChild)) {
                    String[] tempStr = priceChild.split("@");
                    String moqStr = tempStr[0].trim();
                    if (moqStr.contains("-")) {
                        moqStr = moqStr.split("-")[0].trim();
                    } else if (moqStr.contains("≥")) {
                        moqStr = moqStr.split("≥")[1].trim();
                    } else if (moqStr.contains(">")) {
                        moqStr = moqStr.split(">")[1].trim();
                    }
                    tempMoq = Integer.valueOf(moqStr);
                    tempPrice = Double.valueOf(tempStr[1].trim());
                    if (moq == 0) {
                        moq = tempMoq;
                    } else {
                        if (moq > tempMoq) {
                            moq = tempMoq;
                        }
                    }
                    if (minPrice == 0) {
                        minPrice = tempPrice;
                    } else {
                        if (minPrice > tempPrice) {
                            minPrice = tempPrice;
                        }
                    }
                    if (maxPrice == 0) {
                        maxPrice = tempPrice;
                    } else {
                        if (maxPrice < tempPrice) {
                            maxPrice = tempPrice;
                        }
                    }
                }
            }
            String rangePrice = "";
            if (moq == 0 || minPrice == 0 || maxPrice == 0) {
                json.setOk(false);
                json.setMessage("解析价格失败");
            } else {
                rangePrice = BigDecimalUtil.truncateDouble(minPrice, 2) + "-" + BigDecimalUtil.truncateDouble(maxPrice, 2);
            }

            boolean isSuccess = beanchmarkingService.updateGoodsFlag(pid, moq, rangePrice, "[" + priceContent.replace("@", "$") + "]",
                    (freeFlag == 1 ? 1 : 0), (benchmarkingFlag == 1 ? 1 : 0));
            if (isSuccess) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("更新异常，请重试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("pid:" + pid + "saveGoodsFlag，更新失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败，原因:" + e.getMessage());
        }
        return json;
    }


}
