package com.importExpress.utli;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
* @author 作者 E-mail: saycjc@outlook.com
* @version 创建时间：2018年4月25日 下午3:34:13 
* 类说明 
*/
public class SaleProfitRateUtil {
    public static int getUserId(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        int uid = user.getId();
        return uid;
    }
}
 