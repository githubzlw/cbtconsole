package com.cbt.warehouse.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        	// ?????????????????????????????? UsernamePasswordToken ??????
            UsernamePasswordToken token = new UsernamePasswordToken(username.toLowerCase(),password);
            // rememberme
            token.setRememberMe(true);
            try {
            	System.out.println("1. " + token.hashCode());
            	// ????????????. 
                currentUser.login(token);
                json.setOk(true);
                Admuser admuser = (Admuser)currentUser.getPrincipal();
                Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
            } 
            // ... catch more exceptions here (maybe custom ones specific to your application?
            // ??????????????????????????????. 
            catch (AuthenticationException e) {
            	e.printStackTrace();
                //unexpected condition?  error?
            	System.out.println("????????????: " + e.getMessage());
            	json.setOk(false);
                json.setMessage("????????????????????????????????????" + e.getMessage());
                LOG.error("????????????????????????????????????", e);
            }catch (Exception e) {
            	System.out.println("????????????: " + e.getMessage());
            	json.setOk(false);
                json.setMessage("????????????????????????" + e.getMessage());
                LOG.error("????????????????????????", e);
			}
        }
		
		 return "redirect:/website/main_menu.jsp";
	}

    /**
           * ????????????????????????
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
    			// ????????????redis
    	        Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
    			Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
    			LOG.info("authlist:{}", authlist);
    			LOG.info("save sessionId:[]", sessionId);
    			LOG.info("login is ok!");
    			json.setData(authlist);
    			json.setOk(true);
    			//????????????????????????????????????
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
    			json.setMessage("??????????????????");
    		}
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		json.setOk(false);
    		json.setMessage("????????????????????????????????????" + e.getMessage());
    		LOG.error("????????????????????????????????????", e);
    	}
    	return json;
    }
    /**
     * ??????????????????
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
                // ????????????redis
                Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
                Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
                LOG.info("authlist:{}", authlist);
                LOG.info("save sessionId:[]", sessionId);
                LOG.info("login is ok!");
                json.setOk(true);
                isTrue = true;
                if (isTrue) {
                    //????????????????????????????????????
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
                json.setMessage("??????????????????");
            }


        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????????????????" + e.getMessage());
            LOG.error("????????????????????????????????????", e);
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
                LOG.error("admuserDao.queryForList()???", e);
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
     * ????????????
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
            //????????????????????????????????????
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
            e.printStackTrace();
            LOG.error("????????????????????????" + e.getMessage());
        }

        return "main_login";
//		return "login";
    }

    /**
     * ?????????????????????
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

            // ???redis?????????????????????
            String userJson = Redis.hget(session.getId(), "admuser");
            if (userJson == null || "".equals(userJson)) {
                json.setOk(false);
                json.setMessage("????????????,???????????????");
                return json;
            }

            // ???session??????????????????????????????

            String userauthJson = Redis.hget(session.getId(), "userauth");
            JSONArray jsonArray = JSONArray.fromObject(userauthJson);// ???String?????????json
            List<AuthInfo> authlist = (List<AuthInfo>) JSONArray.toCollection(jsonArray, AuthInfo.class);

            if (authlist != null && authlist.size() > 0) {
                for (int i = 0; i < authlist.size(); i++) {
                    if (url.equals(authlist.get(i).getUrl())) {
                        json.setOk(true);
                        LOG.info("{}:????????????", url);
                        break;
                    }
                }
            } else {
                json.setOk(false);
                json.setMessage("????????????,???????????????");
                return json;
            }
        } catch (Exception e) {
            LOG.error("??????????????????????????????" + e.getMessage());
            json.setOk(false);
            json.setMessage("????????????,???????????????");
        }

        return json;
    }

    /**
     * ly  2018/12/05 13:07
     * ??????????????????
     */
    @RequestMapping(value = "/resetPwd.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult resetPwd(HttpServletRequest request, String admName, String oldPwd, String newPwd) {
        JsonResult json = new JsonResult();
        try {
            //????????????
            if (StringUtils.isBlank(admName) || StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)) {
                json.setOk(false);
                json.setMessage("??????????????????????????????????????????????????????!");
                return json;
            }
            //?????????????????????
            HttpSession session = request.getSession();
            String userJson = Redis.hget(session.getId(), "admuser");
            if (StringUtils.isBlank(userJson)) {
                json.setOk(false);
                json.setMessage("????????????,???????????????!");
                return json;
            }
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (StringUtils.isBlank(admName) || null == user || !admName.equalsIgnoreCase(user.getAdmName())) {
                json.setOk(false);
                json.setMessage("?????????????????????!");
                return json;
            }
            json = userService.resetPwd(admName, oldPwd, newPwd);
            //?????????????????????????????????list
            admuserList = admuserDao.queryForList();
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("???????????????????????????:" + e.getMessage());
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
            json.setMessage("cookie??????????????????,???????????????");
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
                // ????????????redis
                Long userauth1 = Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
                Long userauth = Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
                LOG.info("authlist:{}", authlist);
                LOG.info("save sessionId:[]", sessionId);
                LOG.info("login is ok!");
                json.setOk(true);
                isTrue = true;
            } else {
                //Added <V1.0.1> Start??? cjc 2018/10/9 11:37 TODO ???????????????????????????????????????????????????MD5 ??????????????????
                admuser = admuserDao.getAdmUserMd5(username, password);
                if (admuser != null) {
                    authlist = userauthDao.getUserAuth(username);
                    // ????????????redis
                    Long userauth1 = Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
                    Long userauth = Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
                    LOG.info("login is ok!");
                    json.setOk(true);
                    isTrue = true;
                } else {
                    json.setOk(false);
                    LOG.info("login is NG!");
                    json.setMessage("??????????????????");
                }
                //End???

            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????????????????" + e.getMessage());
            LOG.error("????????????????????????????????????", e);
        }
        return json;
    }
}
