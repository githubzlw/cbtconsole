package com.importExpress.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.AmazonProductBean;
import com.importExpress.service.AmazonProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/amazonProductCtr")
public class AmazonProductController {
    private static final Log logger = LogFactory.getLog(AmazonProductController.class);

    @Autowired
    private AmazonProductService amazonProductService;

    @Autowired
    private CustomGoodsService customGoodsService;


    /**
     * 查询亚马逊信息列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("amazonMatch1688");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            return mv;
        }

        AmazonProductBean productBean = new AmazonProductBean();
        String amazonPid = request.getParameter("amazonPid");
        if (StringUtils.isNotBlank(amazonPid)) {
            productBean.setAmazonPid(amazonPid.trim());
        }

        String keyword = request.getParameter("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            productBean.setKeyword(keyword.trim());
        }

        String adminIdStr = request.getParameter("adminId");
        if (StringUtils.isNotBlank(adminIdStr)) {
            productBean.setAdminId(Integer.valueOf(adminIdStr));
        }

        String dealStateStr = request.getParameter("dealState");
        if (StringUtils.isNotBlank(dealStateStr)) {
            if (!"-1".equals(dealStateStr)) {
                productBean.setDealState(Integer.valueOf(dealStateStr));
            }
        }

        int page = 1;
        String pageStr = request.getParameter("page");
        if (StringUtils.isNotBlank(pageStr)) {
            page = Integer.valueOf(pageStr);
        }
        int limitNum = 20;
        productBean.setLimitNum(limitNum);
        productBean.setStartNum((page - 1) * limitNum);

        try {
            mv.addObject("amazonPid", productBean.getAmazonPid() == null ? "" : productBean.getAmazonPid());
            mv.addObject("keyword", productBean.getKeyword() == null ? "" : productBean.getKeyword());
            mv.addObject("page", page);
            mv.addObject("adminId", productBean.getAdminId());
            mv.addObject("dealState", dealStateStr);

            List<AmazonProductBean> amazonGoodsList = amazonProductService.queryForList(productBean);
            int total = amazonProductService.queryForListCount(productBean);
            mv.addObject("goodsList", amazonGoodsList);
            mv.addObject("total", total);
            if (total % limitNum == 0) {
                mv.addObject("totalPage", total / limitNum);
            } else {
                mv.addObject("totalPage", total / limitNum + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.error(e.getMessage());
        }
        return mv;
    }


    @RequestMapping("/checkIsExists")
    @ResponseBody
    public JsonResult checkIsExists(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String amazonPid = request.getParameter("amazonPid");//亚马逊商品的ID
        String url1688 = request.getParameter("url1688");//1688商品的URL
        if (StringUtils.isBlank(amazonPid)) {
            System.err.println("获取参数失败");
            json.setOk(false);
            json.setMessage("获取参数失败，请重试");
        } else {
            try {

                String pid = null;
                if (StringUtils.isNotBlank(url1688)) {
                    //解析url1688获取1688PID
                    pid = url1688.substring(url1688.lastIndexOf("/") + 1, url1688.indexOf(".html"));
                }
                int count = amazonProductService.checkAmazonProductIsExists(amazonPid);
                if (count > 0) {
                    //标识1，表示存在对标
                    json.setOk(true);
                    json.setData(1);
                } else {
                    json.setOk(true);
                    // 取出1688商品的全部信息
                    CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);
                    if (goods == null || StringUtils.isBlank(goods.getPid())) {
                        //标识0，标识可以执行
                        json.setData(0);
                    } else {
                        //标识2，表示已经在线上
                        json.setData(2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                json.setOk(false);
                json.setMessage("校检失败，原因：" + e.getMessage());
                logger.error("checkIsExists error:" + e.getMessage());
            }
        }

        return json;
    }


    @RequestMapping("/addToOnline")
    public String addToOnline(HttpServletRequest request, HttpServletResponse response) {

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            request.setAttribute("message", "请登录后操作");
            request.setAttribute("result", "0");
            return "addGoodsResult";
        }

        String amazonPid = request.getParameter("amazonPid");//ali商品的URL
        String url1688 = request.getParameter("url1688");//1688商品的URL
        if (StringUtils.isBlank(amazonPid) || StringUtils.isBlank(url1688)) {
            System.err.println("获取参数失败");
        } else {
            try {
                String pid = "";
                if (StringUtils.isNotBlank(url1688)) {
                    pid = url1688.substring(url1688.lastIndexOf("/") + 1, url1688.indexOf(".html"));
                }
                amazonProductService.setAmazonDealFlag(amazonPid, 1, user.getId(), pid);
                request.setAttribute("message", "执行成功，请关闭当前页面");
                request.setAttribute("result", "1");
            } catch (Exception e) {
                request.setAttribute("message", "执行失败，原因:" + e.getMessage());
                request.setAttribute("result", "0");
                e.printStackTrace();
                logger.error("addToOnline error:" + e.getMessage());
            }
        }
        return "addGoodsResult";
    }


}
