package com.cbt.systemcode.ctrl;

import com.cbt.bean.SecondaryValidation;
import com.cbt.systemcode.service.SecondaryValidationService;
import com.cbt.util.CheckPasswordUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/secondaryValidation")
public class SecondaryValidationController {
	private static final Log LOG = LogFactory.getLog(SecondaryValidationController.class);

	@Autowired
	private SecondaryValidationService secValiService;

	@RequestMapping(value = "/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonResult json = new JsonResult();
		List<SecondaryValidation> valiLst = new ArrayList<SecondaryValidation>();
		try {
			valiLst = secValiService.queryForList();
			json.setOk(true);
			json.setData(valiLst);
		} catch (Exception e) {
			json.setOk(false);
			json.setMessage("获取失败,请联系管理员");
			LOG.error("获取失败,原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/queryByUserId")
	@ResponseBody
	public JsonResult queryByUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonResult json = new JsonResult();
		SecondaryValidation sValidation = new SecondaryValidation();

		String userid = request.getParameter("userid");
		if (userid == null || "".equals(userid)) {
			json.setOk(false);
			json.setMessage("获取userid失败");
			return json;
		}
		try {
			sValidation = secValiService.queryByUserId(Integer.valueOf(userid));
			json.setOk(true);
			json.setData(sValidation);
		} catch (Exception e) {
			json.setOk(false);
			json.setMessage("获取失败,请联系管理员");
			LOG.error("获取失败,原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/insertSecondaryValidation")
	@ResponseBody
	public JsonResult insertSecondaryValidation(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JsonResult json = new JsonResult();
		SecondaryValidation secVali = new SecondaryValidation();

		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admJson == null) {
			json.setOk(false);
			json.setMessage("获取用户失败,请重新登录后操作");
			return json;
		}
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);

		if (user == null) {
			json.setOk(false);
			json.setMessage("获取用户失败,请重新登录后操作");
			return json;
		}

		String userid = request.getParameter("userid");
		if (userid == null || "".equals(userid)) {
			json.setOk(false);
			json.setMessage("获取userid失败");
			return json;
		} else if (!user.getId().toString().equals(userid)) {
			json.setOk(false);
			json.setMessage("不是当前用户,拒绝操作");
			return json;
		}

		String password = request.getParameter("password");
		if (password == null || "".equals(password)) {
			json.setOk(false);
			json.setMessage("获取password失败");
			return json;
		}
		if (!CheckPasswordUtil.doCheck(password)) {
			json.setOk(false);
			json.setMessage("password不符合条件:不少于8位,字母、数字、特殊字符组合,终止执行");
			return json;
		}
		String remark = request.getParameter("remark");

		secVali.setUserid(Integer.valueOf(userid));
		secVali.setPassword(password);
		secVali.setRemark(remark == null ? "" : remark);

		try {
			int count = secValiService.existsSecondaryValidation(Integer.valueOf(userid));
			if (count > 0) {
				json.setOk(false);
				json.setMessage("该用户已存在验证密码,如需要,请修改密码");
			} else {
				secValiService.insertSecondaryValidation(secVali);
				json.setOk(true);
			}

		} catch (Exception e) {
			json.setOk(false);
			json.setMessage("插入失败,请联系管理员");
			LOG.error("插入失败,原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/updateByUserId")
	@ResponseBody
	public JsonResult updateByUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonResult json = new JsonResult();
		SecondaryValidation secVali = new SecondaryValidation();

		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admJson == null) {
			json.setOk(false);
			json.setMessage("获取用户失败,请重新登录后操作");
			return json;
		}
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);

		if (user == null) {
			json.setOk(false);
			json.setMessage("获取用户失败,请重新登录后操作");
			return json;
		}

		String userid = request.getParameter("userid");
		if (userid == null || "".equals(userid)) {
			json.setOk(false);
			json.setMessage("获取userid失败");
			return json;
		} else if (!user.getId().toString().equals(userid)) {
			json.setOk(false);
			json.setMessage("不是当前用户,拒绝操作");
			return json;
		}

		String oldPassword = request.getParameter("oldPassword");
		if (oldPassword == null || "".equals(oldPassword)) {
			json.setOk(false);
			json.setMessage("获取原始密码失败");
			return json;
		}
		String password = request.getParameter("password");
		if (password == null || "".equals(password)) {
			json.setOk(false);
			json.setMessage("获取password失败");
			return json;
		}
		if (!CheckPasswordUtil.doCheck(password)) {
			json.setOk(false);
			json.setMessage("password不符合条件:不少于8位,字母、数字、特殊字符组合终止执行");
			return json;
		}
		String remark = request.getParameter("remark");

		secVali.setUserid(Integer.valueOf(userid));
		secVali.setPassword(password);
		secVali.setRemark(remark == null ? "" : remark);
		secVali.setUpdateName(user.getAdmName());
		try {

			int count = secValiService.existsSecondaryValidation(Integer.valueOf(userid));
			if (count > 0) {

				boolean is = secValiService.checkExistsPassword(Integer.valueOf(userid), oldPassword);
				if (is) {
					secValiService.updateByUserId(secVali);
					json.setOk(true);
				} else {
					json.setOk(false);
					json.setMessage("原始密码输入错误");
				}
			} else {
				json.setOk(false);
				json.setMessage("该用户不已存在验证密码,如需要,请新增密码");
			}

		} catch (Exception e) {
			json.setOk(false);
			json.setMessage("更新失败,请联系管理员");
			LOG.error("更新失败,原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/deleteByUserId")
	@ResponseBody
	public JsonResult deleteByUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonResult json = new JsonResult();
		String userid = request.getParameter("userid");
		if (userid == null || "".equals(userid)) {
			json.setOk(false);
			json.setMessage("获取userid失败");
			return json;
		}
		try {
			secValiService.deleteByUserId(Integer.valueOf(userid));
			json.setOk(true);
		} catch (Exception e) {
			json.setOk(false);
			json.setMessage("删除失败,请联系管理员");
			LOG.error("删除失败,原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/checkExistsPassword")
	@ResponseBody
	public JsonResult checkExistsPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonResult json = new JsonResult();
		String userid = request.getParameter("userid");
		if (userid == null || "".equals(userid)) {
			json.setOk(false);
			json.setMessage("获取userid失败");
			return json;
		}
		String password = request.getParameter("password");
		if (password == null || "".equals(password)) {
			json.setOk(false);
			json.setMessage("获取password失败");
			return json;
		}
		try {
			boolean is = secValiService.checkExistsPassword(Integer.valueOf(userid), password);
			json.setOk(is);
			if (!is) {
				json.setMessage("当前用户验证密码输入错误或不存在");
			}

		} catch (Exception e) {
			json.setOk(false);
			json.setMessage("验证失败,请联系管理员");
			LOG.error("验证失败,原因：" + e.getMessage());
		}
		return json;
	}

}
