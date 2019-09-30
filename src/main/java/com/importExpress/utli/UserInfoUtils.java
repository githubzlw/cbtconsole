package com.importExpress.utli;

import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class UserInfoUtils {
    private static List<ConfirmUserInfo> allAdminList = new ArrayList<ConfirmUserInfo>();


    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    public static Admuser getUserInfo(HttpServletRequest request) {
        String sessionId = "1";//request.getSession().getId();
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


    public static List<ConfirmUserInfo> queryAllAdminList() {
        if (allAdminList == null || allAdminList.isEmpty()) {
            UserDao dao = new UserDaoImpl();
            List<ConfirmUserInfo> admList = dao.getAllUserHasOffUser();
            for (ConfirmUserInfo userInfo : admList) {
                String userName = userInfo.getConfirmusername();
                if (userInfo.getRole() == 0) {
                    allAdminList.add(userInfo);
                } else {
                    allAdminList.add(userInfo);
                }
            }
        }
        return allAdminList;
    }
}
