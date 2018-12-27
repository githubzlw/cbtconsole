package com.importExpress.controller;

import com.importExpress.pojo.AliProductBean;
import com.importExpress.service.AliProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/produceCtr")
public class AliProductMacthController {
    private static final Log logger = LogFactory.getLog(AliProductMacthController.class);

    @Autowired
    private AliProductService aliProductService;

    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("aliMatch1688");

        AliProductBean productBean = new AliProductBean();
        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isNotBlank(aliPid)) {
            productBean.setAliPid(aliPid);
        }
        String keyword = request.getParameter("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            productBean.setKeyword(keyword);
        }

        String adminIdStr = request.getParameter("adminId");
        if (StringUtils.isNotBlank(adminIdStr)) {
            productBean.setAdminId(Integer.valueOf(adminIdStr));
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
            List<AliProductBean> aliBeans = aliProductService.queryForList(productBean);
            for (AliProductBean aliProduct : aliBeans) {
                aliProduct.setProductListLire(aliProductService.query1688ByLire(productBean.getAliPid()));
                aliProduct.setProductListPython(aliProductService.query1688ByPython(productBean.getAliPid()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.error(e.getMessage());
        }
        return mv;
    }

}
