package com.importExpress.controller;

import com.alibaba.fastjson.JSONArray;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.GoodsCarActiveBean;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.GoodsCarconfigExample;
import com.importExpress.pojo.GoodsCarconfigWithBLOBs;
import com.importExpress.service.GoodsCarconfigService;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/goodsCarConfig")
public class GoodsCarConfigController {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(GoodsCarConfigController.class);


    @Autowired
    private GoodsCarconfigService goodsCarconfigService;


    @RequestMapping("/goodsCarInfo")
    public ModelAndView goodsCarInfo(HttpServletRequest request, HttpServletResponse response) {
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
            GoodsCarconfigExample example = new GoodsCarconfigExample();
            GoodsCarconfigExample.Criteria criteria = example.createCriteria();
            criteria.andUseridEqualTo(Integer.valueOf(userIdStr));
            List<GoodsCarconfigWithBLOBs> list = goodsCarconfigService.selectByExampleWithBLOBs(example);
            List<GoodsCarActiveBean> resultList = new ArrayList<>();
            List<GoodsCarActiveBean> listActive = new ArrayList<>();
            for (GoodsCarconfigWithBLOBs carconfigWithBLOBs : list) {
                listActive = JSONArray.parseArray(carconfigWithBLOBs.getShopcarinfo(), GoodsCarActiveBean.class);
                if (listActive != null && listActive.size() > 0) {
                    resultList.addAll(listActive);
                }
            }
            list.clear();
            mv.addObject("success", 1);
            mv.addObject("list", resultList);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("goodsCarInfo error:" + e.getMessage());
            logger.error("goodsCarInfo error:", e);
            mv.addObject("message", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("success", 0);
        }
        return mv;
    }

}
