package com.importExpress.controller;

import com.cbt.bean.CategoryBean;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.Redis;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.OnlineGoodsCheck;
import com.importExpress.utli.EasyUiTreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/onlineGoodsCtr")
public class OnlineGoodsController {
    private final static Logger logger = LoggerFactory.getLogger(OnlineGoodsController.class);

    @Autowired
    private CustomGoodsService customGoodsService;


    @RequestMapping("/queryForList")
    @ResponseBody
    public EasyUiJsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        OnlineGoodsCheck queryPm = new OnlineGoodsCheck();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }

        int startNum = 0;
        int limitNum = 50;
        String rowsStr = request.getParameter("rows");
        if (StringUtils.isNotBlank(rowsStr)) {
            limitNum = Integer.valueOf(rowsStr);
        }

        String pageStr = request.getParameter("page");
        if (!(StringUtils.isBlank(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isNotBlank(pid)) {
            queryPm.setPid(pid);
        }
        String catid = request.getParameter("catid");
        if (StringUtils.isNotBlank(catid) && !"0".equals(catid)) {
            queryPm.setCatid(catid);
        }

        try {
            queryPm.setLimitNum(limitNum);
            queryPm.setStartNum(startNum);
            List<OnlineGoodsCheck> res = customGoodsService.queryOnlineGoodsForList(queryPm);
            for (OnlineGoodsCheck goodsCheck : res) {
                if (StringUtils.isNotBlank(goodsCheck.getImgShow())) {
                    goodsCheck.setImgShow(goodsCheck.getRemotePath() + goodsCheck.getImgShow().replace("60x60","400x400"));
                }
                if (StringUtils.isNotBlank(goodsCheck.getEninfoShow1())) {
                    if (goodsCheck.getEninfoShow1().contains("http") || goodsCheck.getEninfoShow1().contains("https")) {

                    } else {
                        goodsCheck.setEninfoShow1(goodsCheck.getRemotePath() + goodsCheck.getEninfoShow1());
                    }
                }
                if (StringUtils.isNotBlank(goodsCheck.getEninfoShow2())) {
                    if (goodsCheck.getEninfoShow2().contains("http") || goodsCheck.getEninfoShow2().contains("https")) {

                    } else {
                        goodsCheck.setEninfoShow2(goodsCheck.getRemotePath() + goodsCheck.getEninfoShow2());
                    }
                }
                if (StringUtils.isNotBlank(goodsCheck.getEninfoShow3())) {
                    if (goodsCheck.getEninfoShow3().contains("http") || goodsCheck.getEninfoShow3().contains("https")) {

                    } else {
                        goodsCheck.setEninfoShow3(goodsCheck.getRemotePath() + goodsCheck.getEninfoShow3());
                    }
                }
                if (StringUtils.isNotBlank(goodsCheck.getEninfoShow4())) {
                    if (goodsCheck.getEninfoShow4().contains("http") || goodsCheck.getEninfoShow4().contains("https")) {

                    } else {
                        goodsCheck.setEninfoShow4(goodsCheck.getRemotePath() + goodsCheck.getEninfoShow4());
                    }
                }
            }
            int count = customGoodsService.queryOnlineGoodsForListCount(queryPm);
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


    @RequestMapping(value = "/genCategoryTree")
    @ResponseBody
    public List<Map<String, Object>> genCategoryTree(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> treeMap = new ArrayList<Map<String, Object>>();// 根节点的所有子节点
        OnlineGoodsCheck queryPm = new OnlineGoodsCheck();
        String pid = request.getParameter("pid");
        if (StringUtils.isNotBlank(pid)) {
            queryPm.setPid(pid);
        }
        String catid = request.getParameter("catid");
        if (StringUtils.isNotBlank(catid) && !"0".equals(catid)) {
            queryPm.setCatid(catid);
        }
        try {


            List<CategoryBean> categorys = customGoodsService.queryCategoryList(queryPm);
            int count = customGoodsService.queryOnlineGoodsForListCount(queryPm);
            treeMap = EasyUiTreeUtils.genEasyUiTree(categorys, count);
            categorys.clear();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return treeMap;
    }


}
