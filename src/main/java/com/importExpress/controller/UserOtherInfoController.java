package com.importExpress.controller;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.UserOtherInfoBean;
import com.importExpress.service.UserOtherInfoService;
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
@RequestMapping("/userOtherInfo")
public class UserOtherInfoController {
    private static final Log logger = LogFactory.getLog(UserOtherInfoController.class);

    @Autowired
    private UserOtherInfoService userOtherInfoService;


    /**
     * 分页查询数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryForList")
    @ResponseBody
    public EasyUiJsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        UserOtherInfoBean userOtherInfoQuery = new UserOtherInfoBean();
        try {
            String userJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setMessage("请登录后操作");
                json.setSuccess(false);
                return json;
            } else {
                // 非管理员的，只看分配的客户数据
                if (!"0".equals(user.getRoletype())) {
                    userOtherInfoQuery.setAdminId(user.getId());
                }
            }
            String userIdStr = request.getParameter("userId");
            if (StringUtils.isNotBlank(userIdStr)) {
                userOtherInfoQuery.setUserId(Integer.valueOf(userIdStr));
            }
            String userEmail = request.getParameter("userEmail");
            if (StringUtils.isNotBlank(userEmail)) {
                userOtherInfoQuery.setUserEmail(userEmail);
            }
            String beginTime = request.getParameter("beginTime");
            if (StringUtils.isNotBlank(beginTime)) {
                userOtherInfoQuery.setBeginTime(beginTime);
            }
            String endTime = request.getParameter("endTime");
            if (StringUtils.isNotBlank(endTime)) {
                userOtherInfoQuery.setEndTime(endTime);
            }
            int row = 25;
            String rowStr = request.getParameter("rows");
            if (StringUtils.isNotBlank(rowStr)) {
                row = Integer.valueOf(rowStr);
            }
            userOtherInfoQuery.setLimitNum(row);
            int page = 1;
            String pageStr = request.getParameter("page");
            if (StringUtils.isNotBlank(pageStr)) {
                page = Integer.valueOf(pageStr);
            }
            userOtherInfoQuery.setStartNum((page - 1) * row);

            List<UserOtherInfoBean> list = userOtherInfoService.queryForList(userOtherInfoQuery);
            for (UserOtherInfoBean infoBean : list) {
                if ("1".equals(infoBean.getUserType())) {
                    infoBean.setUserTypeDesc("Combine Shipping (ocean freight, cheapest shipping rate)");
                } else if ("2".equals(infoBean.getUserType())) {
                    infoBean.setUserTypeDesc("Combine Shipping (air freight, better price than shipping individually)");
                } else if ("3".equals(infoBean.getUserType())) {
                    infoBean.setUserTypeDesc("Quality Control");
                } else if ("4".equals(infoBean.getUserType())) {
                    infoBean.setUserTypeDesc("Custom Packaging");
                }
            }
            int total = userOtherInfoService.queryForListCount(userOtherInfoQuery);
            json.setSuccess(true);
            json.setRows(list);
            json.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("queryForList error:", e);
            json.setMessage("queryForList error:" + e.getMessage());
            json.setSuccess(false);
        }
        return json;
    }


    @RequestMapping("/saveFollowInfo")
    @ResponseBody
    public JsonResult saveFollowInfo(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        try {

            String userJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }

            String id = request.getParameter("id");
            String userId = request.getParameter("userId");
            String remark = request.getParameter("remark");
            if (StringUtils.isBlank(id)) {
                json.setOk(false);
                json.setMessage("获取ID失败");
                return json;
            }
            if (StringUtils.isBlank(userId)) {
                json.setOk(false);
                json.setMessage("获取客户ID失败");
                return json;
            }
            if (StringUtils.isBlank(remark)) {
                json.setOk(false);
                json.setMessage("获取备注失败");
                return json;
            }
            UserOtherInfoBean userOtherInfo = new UserOtherInfoBean();
            userOtherInfo.setId(Integer.valueOf(id));
            userOtherInfo.setUserId(Integer.valueOf(userId));
            userOtherInfo.setRemarks(remark);
            userOtherInfo.setAdminId(user.getId());
            userOtherInfoService.updateFollowInfo(userOtherInfo);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("saveFollowInfo error:", e);
        }
        return json;
    }
}
