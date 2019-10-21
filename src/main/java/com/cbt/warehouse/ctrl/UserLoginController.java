package com.cbt.warehouse.ctrl;

import com.cbt.util.Md5Util;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.WebCookie;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.Dao.UserAuthDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;
import com.cbt.website.userAuth.impl.UserAuthDaoImpl;
import com.cbt.website.util.JsonResult;
import com.importExpress.service.QueryUserService;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/userLogin")
public class UserLoginController {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(HotGoodsCtrl.class);
    @Autowired
    private QueryUserService userService;

    private static AdmUserDao admuserDao = new AdmUserDaoImpl();
    private static UserAuthDao userauthDao = new UserAuthDaoImpl();

    private static List<Admuser> admuserList = null;

    static {
        try {
            admuserList = admuserDao.queryForList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/checkUserInfo.do")
	public String login(HttpServletRequest request, HttpServletResponse response){
    	String username = request.getParameter("userName");
        String password = request.getParameter("passWord");
		Subject currentUser = SecurityUtils.getSubject();
		String sessionId = request.getSession().getId();
		JsonResult json = new JsonResult();
		json.setOk(true);
		System.out.println("currentUser.isAuthenticated():"+currentUser.isAuthenticated());
		if (!currentUser.isAuthenticated()) {
        	// 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(username.toLowerCase(),password);
            // rememberme
            token.setRememberMe(true);
            try {
            	System.out.println("1. " + token.hashCode());
            	// 执行登录. 
                currentUser.login(token);
                json.setOk(true);
                Admuser admuser = (Admuser)currentUser.getPrincipal();
                Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
            } 
            // ... catch more exceptions here (maybe custom ones specific to your application?
            // 所有认证时异常的父类. 
            catch (AuthenticationException e) {
            	e.printStackTrace();
                //unexpected condition?  error?
            	System.out.println("登录失败: " + e.getMessage());
            	json.setOk(false);
                json.setMessage("校检用户信息失败，原因：" + e.getMessage());
                LOG.error("校检用户信息失败，原因：", e);
            }catch (Exception e) {
            	System.out.println("登录失败: " + e.getMessage());
            	json.setOk(false);
                json.setMessage("登录失败，原因：" + e.getMessage());
                LOG.error("登录失败，原因：", e);
			}
        }
		
		 return "redirect:/website/main_menu.jsp";
	}

    /**
           * 判断用户权限信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/authlist.do")
    @ResponseBody
    public JsonResult authlist(HttpServletRequest request, HttpServletResponse response) {
    	
    	LOG.info("step into the checkUserInfo()");
    	String sessionId = request.getSession().getId();
    	JsonResult json = new JsonResult();
    	json.setOk(false);
    	Subject currentUser = SecurityUtils.getSubject();
    	if(currentUser.isAuthenticated()) {
    		json.setOk(true);
    	}
    	Admuser admuser = (Admuser)currentUser.getPrincipal();
    	try {
    		
    		List<AuthInfo> authlist;
    		if (admuser != null) {
    			authlist = userauthDao.getUserAuth(admuser.getAdmName());
    			// 数据放入redis
    	        Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
    			Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
    			LOG.info("authlist:{}", authlist);
    			LOG.info("save sessionId:[]", sessionId);
    			LOG.info("login is ok!");
    			json.setData(authlist);
    			json.setOk(true);
    			//清除页面保存的用户名密码
    			/*Cookie usName = new Cookie("usName", username);
    			usName.setMaxAge(3600 * 24 * 6);
    			usName.setPath("/");
    			response.addCookie(usName);
    			Cookie usPass = new Cookie("usPass", admuser.getPassword());
    			usPass.setMaxAge(3600 * 24 * 6);
    			usPass.setPath("/");
    			response.addCookie(usPass);*/
    		} else {
    			json.setOk(false);
    			LOG.info("login is NG!");
    			json.setMessage("登录信息错误");
    		}
    		
    		
    	} catch (Exception e) {
    		e.getStackTrace();
    		json.setOk(false);
    		json.setMessage("校检用户信息失败，原因：" + e.getMessage());
    		LOG.error("校检用户信息失败，原因：", e);
    	}
    	return json;
    }
    /**
     * 判断用户信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/checkUserInfoss.do")
    @ResponseBody
    public JsonResult checkUserInfo(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("step into the checkUserInfo()");
        JsonResult json = new JsonResult();

        String username = request.getParameter("userName");
        String password = request.getParameter("passWord");
        String sessionId = request.getSession().getId();
        boolean isTrue = false;
        try {

            Admuser admuser = checkUserPassword(username, password);
            /*admuser = admuserDao.getAdmUser(username, password);
            System.err.println("getAdmUser:" + admuser);*/
            List<AuthInfo> authlist;
            if (admuser != null) {
                authlist = userauthDao.getUserAuth(username);
                // 数据放入redis
                Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
                Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
                LOG.info("authlist:{}", authlist);
                LOG.info("save sessionId:[]", sessionId);
                LOG.info("login is ok!");
                json.setOk(true);
                isTrue = true;
                if (isTrue) {
                    //清除页面保存的用户名密码
                    Cookie usName = new Cookie("usName", username);
                    usName.setMaxAge(3600 * 24 * 6);
                    usName.setPath("/");
                    response.addCookie(usName);
                    Cookie usPass = new Cookie("usPass", admuser.getPassword());
                    usPass.setMaxAge(3600 * 24 * 6);
                    usPass.setPath("/");
                    response.addCookie(usPass);
                }
            } else {
                json.setOk(false);
                LOG.info("login is NG!");
                json.setMessage("登录信息错误");
            }


        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("校检用户信息失败，原因：" + e.getMessage());
            LOG.error("校检用户信息失败，原因：", e);
        }
        return json;
    }

    private Admuser checkUserPassword(String userName, String password) {
        
        Admuser adm = admuserDao.queryForListByName(userName);
        if(adm != null) {
        	if (adm.getPassword().equals(password) || adm.getPassword().equals(Md5Util.md5Operation(password))) {
                return adm;
            }
        }
        /*Admuser adm = null;
        if (admuserList == null || admuserList.size() == 0) {
            try {
                admuserList = admuserDao.queryForList();
            } catch (Exception e) {
                LOG.error("admuserDao.queryForList()：", e);
            }
        }
        for (Admuser admuser : admuserList) {
            if (admuser.getAdmName().equalsIgnoreCase(userName)) {
                if (admuser.getPassword().equals(password) || admuser.getPassword().equals(Md5Util.md5Operation(password))) {
                    adm = admuser;
                }
                break;
            }
        }*/
        return null;
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/loginOut.do")
    public String loginOut(HttpServletRequest request, HttpServletResponse response) {

        try {
            request.getSession().removeAttribute("admuser");
            request.getSession().removeAttribute("userauth");
            Redis.hdel(request.getSession().getId());
            Subject currentUser = SecurityUtils.getSubject();
            Admuser admuser = (Admuser)currentUser.getPrincipal();
            if(admuser != null) {
            	Redis.hdel("authPremession"+admuser.getId());
            }
            //清除页面保存的用户名密码
            Cookie usName = new Cookie("usName", null);
            usName.setMaxAge(0);
            usName.setPath("/");
            response.addCookie(usName);
            Cookie usPass = new Cookie("usPass", null);
            usPass.setMaxAge(0);
            usPass.setPath("/");
            response.addCookie(usPass);
            currentUser.logout();
        } catch (Exception e) {
            e.getStackTrace();
            LOG.error("退出失败，原因：" + e.getMessage());
        }

        return "main_login";
//		return "login";
    }

    /**
     * 打开主菜单链接
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/openMainMenuUrl.do")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public JsonResult openMainMenuUrl(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String url = request.getParameter("url");
        HttpSession session = request.getSession();
        try {

            // 从redis中获取用户信息
            String userJson = Redis.hget(session.getId(), "admuser");
            if (userJson == null || "".equals(userJson)) {
                json.setOk(false);
                json.setMessage("登录超时,请重新登录");
                return json;
            }

            // 从session中获取主菜单列表信息

            String userauthJson = Redis.hget(session.getId(), "userauth");
            JSONArray jsonArray = JSONArray.fromObject(userauthJson);// 把String转换为json
            List<AuthInfo> authlist = (List<AuthInfo>) JSONArray.toCollection(jsonArray, AuthInfo.class);

            if (authlist != null && authlist.size() > 0) {
                for (int i = 0; i < authlist.size(); i++) {
                    if (url.equals(authlist.get(i).getUrl())) {
                        json.setOk(true);
                        LOG.info("{}:匹配成功", url);
                        break;
                    }
                }
            } else {
                json.setOk(false);
                json.setMessage("登录超时,请重新登录");
                return json;
            }
        } catch (Exception e) {
            LOG.error("打开链接失败，原因：" + e.getMessage());
            json.setOk(false);
            json.setMessage("打开失败,请重新登录");
        }

        return json;
    }

    /**
     * ly  2018/12/05 13:07
     * 用户密码修改
     */
    @RequestMapping(value = "/resetPwd.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult resetPwd(HttpServletRequest request, String admName, String oldPwd, String newPwd) {
        JsonResult json = new JsonResult();
        try {
            //数据校验
            if (StringUtils.isBlank(admName) || StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)) {
                json.setOk(false);
                json.setMessage("旧密码或者新密码或者待修改的用户为空!");
                return json;
            }
            //登录的用户校验
            HttpSession session = request.getSession();
            String userJson = Redis.hget(session.getId(), "admuser");
            if (StringUtils.isBlank(userJson)) {
                json.setOk(false);
                json.setMessage("登录超时,请重新登录!");
                return json;
            }
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (StringUtils.isBlank(admName) || null == user || !admName.equalsIgnoreCase(user.getAdmName())) {
                json.setOk(false);
                json.setMessage("修改用户非本人!");
                return json;
            }
            json = userService.resetPwd(admName, oldPwd, newPwd);
            //修改完密码从新获取用户list
            admuserList = admuserDao.queryForList();
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("密码修改失败，原因:" + e.getMessage());
            LOG.error("UserLoginController.resetPwd error", e);
        }
        return json;
    }

    @RequestMapping(value = "/autoLoginByCookiei")
    @ResponseBody
    public JsonResult autoLoginByCookie(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String username = WebCookie.cookieValue(request, "usName");
        String password = WebCookie.cookieValue(request, "usPass");
        String sessionId = request.getSession().getId();
        boolean isTrue = false;
        if (org.apache.commons.lang3.StringUtils.isBlank(username) || org.apache.commons.lang3.StringUtils.isBlank(password)) {
            json.setOk(false);
            json.setMessage("cookie用户信息为空,重新登陆！");
            return json;
        }
        try {
            AdmUserDao admuserDao = new AdmUserDaoImpl();
            UserAuthDao userauthDao = new UserAuthDaoImpl();
            Admuser admuser = null;
            admuser = admuserDao.getAdmUser(username, password);
            List<AuthInfo> authlist = new ArrayList<AuthInfo>();
            if (admuser != null) {
                authlist = userauthDao.getUserAuth(username);
                // 数据放入redis
                Long userauth1 = Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
                Long userauth = Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
                LOG.info("authlist:{}", authlist);
                LOG.info("save sessionId:[]", sessionId);
                LOG.info("login is ok!");
                json.setOk(true);
                isTrue = true;
            } else {
                //Added <V1.0.1> Start： cjc 2018/10/9 11:37 TODO 如果用户密码不正确则直接验证是否是MD5 密码直接登陆
                admuser = admuserDao.getAdmUserMd5(username, password);
                if (admuser != null) {
                    authlist = userauthDao.getUserAuth(username);
                    // 数据放入redis
                    Long userauth1 = Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
                    Long userauth = Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
                    LOG.info("login is ok!");
                    json.setOk(true);
                    isTrue = true;
                } else {
                    json.setOk(false);
                    LOG.info("login is NG!");
                    json.setMessage("登录信息错误");
                }
                //End：

            }

        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("校检用户信息失败，原因：" + e.getMessage());
            LOG.error("校检用户信息失败，原因：", e);
        }
        return json;
    }
}
