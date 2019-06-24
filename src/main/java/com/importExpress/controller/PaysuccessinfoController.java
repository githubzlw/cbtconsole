package com.importExpress.controller;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.Paysuccessinfo;
import com.importExpress.pojo.PaysuccessinfoExample;
import com.importExpress.pojo.UserOtherInfoBean;
import com.importExpress.service.PaysuccessinfoService;
import com.importExpress.utli.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * *****************************************************************************************
 *
 * @ClassName PaysuccessinfoController
 * @Author: cjc
 * @Descripeion 付款成功页面
 * @Date： 2019/6/20 20:26:42
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       20:26:422019/6/20     cjc                       初版
 * ******************************************************************************************
 */
@Slf4j
@Controller
@RequestMapping("/paySuccessInfo")
public class PaysuccessinfoController {
    @Autowired
    PaysuccessinfoService paysuccessinfoService;

    @RequestMapping(value = "/getListInfo")
    @ResponseBody
    public EasyUiJsonResult getListInfo(HttpServletRequest request, HttpServletResponse response){
        EasyUiJsonResult json = new EasyUiJsonResult();
        try {
            String userJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setMessage("请登录后操作");
                json.setSuccess(false);
                return json;
            }
            String pageStr = request.getParameter("page");
            String limitNumStr = request.getParameter("rows");
            String sttime = request.getParameter("sttime");
            String edtime = request.getParameter("edtime");
            String userIdStr = request.getParameter("userId");
            String orderNo = request.getParameter("orderNo");
            List<Paysuccessinfo> paysuccessinfoList = paysuccessinfoService.queryPaySuccessInfoList(pageStr, limitNumStr, sttime, edtime, userIdStr, orderNo, user.getId());
            long total = paysuccessinfoList.stream().count();
            json.setTotal((int) total);
            json.setRows(paysuccessinfoList);
            json.setSuccess(true);
        } catch (Exception e) {
            log.error("queryForList error:", e);
            json.setMessage("queryForList error:" + e.getMessage());
            json.setSuccess(false);
        }
        return json;
    }
}
