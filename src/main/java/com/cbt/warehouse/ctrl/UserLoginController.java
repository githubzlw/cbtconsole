package com.cbt.warehouse.ctrl;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.Dao.UserAuthDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;
import com.cbt.website.userAuth.impl.UserAuthDaoImpl;
import com.cbt.website.util.JsonResult;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/userLogin")
public class UserLoginController {
	private static final Log LOG = LogFactory.getLog(HotGoodsCtrl.class);

	/**
	 * 判断用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkUserInfo.do")
	@ResponseBody
	public JsonResult checkUserInfo(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();

		String username = request.getParameter("userName");
		String password = request.getParameter("passWord");
		String sessionId = request.getSession().getId();
		try {

			AdmUserDao admuserDao = new AdmUserDaoImpl();
			UserAuthDao userauthDao = new UserAuthDaoImpl();
			Admuser admuser = null;
			admuser = admuserDao.getAdmUser(username, password);
			List<AuthInfo> authlist = new ArrayList<AuthInfo>();
			if (admuser != null) {
				authlist = userauthDao.getUserAuth(username);
				// 数据放入redis
				Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
				Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
				json.setOk(true);
			} else {
				//Added <V1.0.1> Start： cjc 2018/10/9 11:37 TODO 如果用户密码不正确则直接验证是否是MD5 密码直接登陆
				admuser = admuserDao.getAdmUserMd5(username, password);
				if (admuser != null) {
					authlist = userauthDao.getUserAuth(username);
					// 数据放入redis
					Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
					Redis.hset(sessionId, "userauth", JSONArray.fromObject(authlist).toString());
					json.setOk(true);
				}else {
					json.setOk(false);
					json.setMessage("登录信息错误");
				}
				//End：

			}

		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("校检用户信息失败，原因：" + e.getMessage());
			LOG.error("校检用户信息失败，原因：" + e.getMessage());
		}
		return json;
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
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("退出失败，原因：" + e.getMessage());
		}

//		return "main_login";
		return "login";
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
						System.out.println(url + ":匹配成功");
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

}
