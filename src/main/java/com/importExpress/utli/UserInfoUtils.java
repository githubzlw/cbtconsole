package com.importExpress.utli;

import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;

import javax.servlet.http.HttpServletRequest;

public class UserInfoUtils {


    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    public static Admuser getUserInfo(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        return admuser;
    }


    /**
     * 判断是否登录
     * @param request
     * @return
     */
    public static boolean checkIsLogin(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (admuser == null || admuser.getId() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
