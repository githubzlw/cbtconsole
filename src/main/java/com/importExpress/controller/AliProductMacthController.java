package com.importExpress.controller;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.AliProductBean;
import com.importExpress.service.AliProductService;
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
@RequestMapping("/productCtr")
public class AliProductMacthController {
    private static final Log logger = LogFactory.getLog(AliProductMacthController.class);

    @Autowired
    private AliProductService aliProductService;

    /**
     * 查询阿里信息列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("aliMatch1688");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            return mv;
        }

        AliProductBean productBean = new AliProductBean();
        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isNotBlank(aliPid)) {
            productBean.setAliPid(aliPid.trim());
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
            mv.addObject("aliPid", productBean.getAliPid() == null ? "" : productBean.getAliPid());
            mv.addObject("keyword", productBean.getKeyword() == null ? "" : productBean.getKeyword());
            mv.addObject("page", page);
            mv.addObject("adminId", productBean.getAdminId());
            mv.addObject("dealState", dealStateStr);

            List<AliProductBean> aliBeans = aliProductService.queryForList(productBean);
            for (AliProductBean aliProduct : aliBeans) {
                aliProduct.setProductListLire(aliProductService.query1688ByLire(aliProduct.getAliPid()));
                aliProduct.setProductListPython(aliProductService.query1688ByPython(aliProduct.getAliPid()));
            }
            int total = aliProductService.queryForListCount(productBean);
            mv.addObject("infos", aliBeans);
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


    /**
     * 更新ali商品的处理标识
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/setAliFlag")
    @ResponseBody
    public JsonResult setAliFlag(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isBlank(aliPid)) {
            json.setOk(false);
            json.setMessage("获取AliPid失败");
            return json;
        }

        String dealStateStr = request.getParameter("dealState");
        if (StringUtils.isBlank(dealStateStr)) {
            json.setOk(false);
            json.setMessage("获取处理状态失败");
            return json;
        }
        try {
            aliProductService.setAliFlag(aliPid, Integer.valueOf(dealStateStr), user.getId());
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("setAliFlag error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败:" + e.getMessage());
            logger.error("setAliFlag error:" + e.getMessage());
        }
        return json;
    }


    /**
     * 更新lire商品的处理标识
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/set1688PidFlag")
    @ResponseBody
    public JsonResult set1688PidFlag(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isBlank(aliPid)) {
            json.setOk(false);
            json.setMessage("获取AliPid失败");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取1688Pid失败");
            return json;
        }

        String dealStateStr = request.getParameter("dealState");
        if (StringUtils.isBlank(dealStateStr)) {
            json.setOk(false);
            json.setMessage("获取处理状态失败");
            return json;
        }
        try {
            aliProductService.set1688PidFlag(aliPid, pid, Integer.valueOf(dealStateStr), user.getId());
            // 如果是对标，更新对标信息
            if ("2".equals(dealStateStr)) {
                aliProductService.setAliFlag(aliPid, 2, user.getId());
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("set1688PidFlag error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败:" + e.getMessage());
            logger.error("set1688PidFlag error:" + e.getMessage());
        }
        return json;
    }

}
