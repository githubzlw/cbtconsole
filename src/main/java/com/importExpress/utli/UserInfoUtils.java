package com.importExpress.utli;

import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;

import javax.servlet.http.HttpServletRequest;

public class UserInfoUtils {


    public static Admuser getUserInfo(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        return admuser;
    }
}
