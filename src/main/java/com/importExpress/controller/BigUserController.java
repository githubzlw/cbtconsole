package com.importExpress.controller;

import com.cbt.util.Redis;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.BigUserBean;
import com.importExpress.service.BigUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/bigUserCtr")
public class BigUserController {
    private static final Log LOG = LogFactory.getLog(BigUserController.class);

    @Autowired
    private BigUserService bigUserService;

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

        String userIdStr = request.getParameter("userId");
        int userId = 0;
        if (StringUtils.isNotBlank(userIdStr)) {
            userId = Integer.valueOf(userIdStr);
        }

        String adminIdStr = request.getParameter("adminId");
        int adminId = 0;
        if (StringUtils.isNotBlank(userIdStr)) {
            adminId = Integer.valueOf(adminIdStr);
        }

        int startNum = 0;
        int limitNum = 30;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        BigUserBean param = new BigUserBean();
        try {

            param.setSalesId(adminId);
            param.setUserId(userId);
            param.setStartNum(startNum);
            param.setLimitNum(limitNum);

            List<BigUserBean> res = bigUserService.queryForList(param);
            int count = bigUserService.queryForListCount(param);

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

}
