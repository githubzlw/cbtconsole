package com.cbt.controller;

import com.cbt.bean.SameTypeGoodsBean;
import com.cbt.pojo.Admuser;
import com.cbt.service.SameTypeGoodsService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.util.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * @ClassName SameTypeGoodsController
 * @Description 同款商品controller
 * @author Jxw
 * @date 2018年1月22日
 */
@Controller
@RequestMapping(value = "/sameTypeGoods")
public class SameTypeGoodsController {

	private static final Log LOG = LogFactory.getLog(SameTypeGoodsController.class);

	@Autowired
	private SameTypeGoodsService stGsService;

	private List<Admuser> adminList = null;

	/**
	 * 
	 * @Title queryForList
	 * @Description 根据条件查询列表数据
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult
	 */
	@RequestMapping("/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser admuser = new Admuser();
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		} else {
			admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

		}

		String typeStr = request.getParameter("type");
		if (typeStr == null || "".equals(typeStr)) {
			typeStr = "0";
		}
		int start = 0;
		int limitNum = 20;
		String pageStr = request.getParameter("page");
		if (pageStr == null || "".equals(pageStr) || "0".equals(pageStr)) {
			start = 0;
		} else {
			start = (Integer.valueOf(pageStr) - 1) * limitNum;
		}
		try {
			int adminId = admuser.getRoletype() == 0 ? 0 : admuser.getId();
			List<SameTypeGoodsBean> res = stGsService.queryForList(Integer.valueOf(typeStr), adminId, start, limitNum);
			int count = stGsService.queryForListCount(Integer.valueOf(typeStr), adminId);
			if (res.size() > 0) {
				dealRalationAdmin(res);
			}
			json.setOk(true);
			json.setData(res);
			json.setTotal(Long.valueOf(count));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因:" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/queryListByMainPid")
	@ResponseBody
	public JsonResult queryListByMainPid(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		}

		String mainPid = request.getParameter("mainPid");
		if (mainPid == null || "".equals(mainPid)) {
			json.setOk(false);
			json.setMessage("获取主pid失败");
			return json;
		}

		try {
			List<SameTypeGoodsBean> res = stGsService.queryListByMainPid(mainPid);
			if (res.size() > 0) {
				dealRalationAdmin(res);
			}
			json.setOk(true);
			json.setData(res);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因:" + e.getMessage());
		}
		return json;
	}

	/**
	 * 
	 * @Title batchAdd
	 * @Description 批量新增商品
	 * @param request
	 * @param response
	 * @return 是否执行成功
	 * @return JsonResult
	 */
	@RequestMapping("/batchAddAllGoods")
	@ResponseBody
	public JsonResult batchAddAllGoods(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser admuser = new Admuser();
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		} else {
			admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

		}

		String mainUrl = request.getParameter("mainUrl");
		if (mainUrl == null || "".equals(mainUrl)) {
			json.setOk(false);
			json.setMessage("获取主图商品url失败");
			return json;
		}
		String urls = request.getParameter("urlIds");
		if (urls == null || "".equals(urls)) {
			json.setOk(false);
			json.setMessage("获取同款商品urls失败，请确认输入是否正确");
			return json;
		} else {
			urls = urls.replace("\n", ";");
			System.err.println("urls:" + urls);
		}
		String typeStr = request.getParameter("type");
		if (typeStr == null || "".equals(typeStr)) {
			json.setOk(false);
			json.setMessage("获取类别失败");
			return json;
		}
		String aveWeightStr = request.getParameter("aveWeight");
		if (aveWeightStr == null || "".equals(aveWeightStr) || "0".equals(aveWeightStr)) {
			json.setOk(false);
			json.setMessage("获取平均重量失败");
			return json;
		}
		try {
			json = stGsService.batchAddUrl(mainUrl, urls, admuser.getId(), Integer.valueOf(typeStr),
					Double.valueOf(aveWeightStr));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("批量插入失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("批量插入失败，原因:" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/batchAddTypeGoods")
	@ResponseBody
	public JsonResult batchAddTypeGoods(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser admuser = new Admuser();
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		} else {
			admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

		}

		String mainUrl = request.getParameter("mainUrl");
		if (mainUrl == null || "".equals(mainUrl)) {
			json.setOk(false);
			json.setMessage("获取主图商品url失败，请确认输入是否正确");
			return json;
		}
		String urls = request.getParameter("urlIds");
		if (urls == null || "".equals(urls)) {
			json.setOk(false);
			json.setMessage("获取同款商品urls失败，请确认输入是否正确");
			return json;
		} else {
			urls = urls.replace("\n", ";");
			System.err.println("urls:" + urls);
		}
		String typeStr = request.getParameter("type");
		if (typeStr == null || "".equals(typeStr)) {
			json.setOk(false);
			json.setMessage("获取类别失败");
			return json;
		}
		String aveWeightStr = request.getParameter("aveWeight");
		if (aveWeightStr == null || "".equals(aveWeightStr) || "0".equals(aveWeightStr)) {
			json.setOk(false);
			json.setMessage("获取平均重量失败");
			return json;
		}
		try {
			json = stGsService.batchAddTypeUrl(mainUrl, urls, admuser.getId(), Integer.valueOf(typeStr),
					Double.valueOf(aveWeightStr));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("批量插入失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("批量插入失败，原因:" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/deleteGoodsByMainPid")
	@ResponseBody
	public JsonResult deleteGoodsByMainPid(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		}

		String mainPid = request.getParameter("mainPid");
		if (mainPid == null || "".equals(mainPid)) {
			json.setOk(false);
			json.setMessage("获取主图PID失败");
			return json;
		}

		try {
			boolean isSuccess = stGsService.deleteGoodsByMainPid(mainPid);
			if (isSuccess) {
				json.setOk(true);
			} else {
				json.setOk(false);
				json.setMessage("执行失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("mainPid:" + mainPid + ",删除失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("mainPid:" + mainPid + ",删除失败，原因:" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/deleteGoodsByPid")
	@ResponseBody
	public JsonResult deleteGoodsByPid(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		}

		String pid = request.getParameter("pid");
		if (pid == null || "".equals(pid)) {
			json.setOk(false);
			json.setMessage("获取当前PID失败");
			return json;
		}
		String mainPid = request.getParameter("mainPid");
		if (mainPid == null || "".equals(mainPid)) {
			json.setOk(false);
			json.setMessage("获取主图PID失败");
			return json;
		}

		try {
			boolean isSuccess = stGsService.deleteGoodsByPid(mainPid, pid);
			if (isSuccess) {
				json.setOk(true);
			} else {
				json.setOk(false);
				json.setMessage("执行失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("mainPid:" + mainPid + ",pid:" + pid + ",删除失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("mainPid:" + mainPid + ",pid:" + pid + ",删除失败，原因:" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/replaceGoodsMainPid")
	@ResponseBody
	public JsonResult replaceGoodsMainPid(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		}

		String newPid = request.getParameter("newPid");
		if (newPid == null || "".equals(newPid)) {
			json.setOk(false);
			json.setMessage("获取newPid失败");
			return json;
		}

		String oldPid = request.getParameter("oldPid");
		if (oldPid == null || "".equals(oldPid)) {
			json.setOk(false);
			json.setMessage("获取oldPid失败");
			return json;
		}

		try {
			boolean isSuccess = stGsService.replaceGoodsMainPid(newPid, oldPid);
			if (isSuccess) {
				json.setOk(true);
			} else {
				json.setOk(false);
				json.setMessage("执行失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("newPid:" + newPid + ",oldPid:" + oldPid + ",替换失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("newPid:" + newPid + ",oldPid:" + oldPid + ",替换失败，原因:" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/useGoodsByState")
	@ResponseBody
	public JsonResult useGoodsByState(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admuserJson == null) {
			json.setOk(false);
			json.setMessage("用户未登陆");
			return json;
		}

		String pids = request.getParameter("pids");
		if (pids == null || "".equals(pids)) {
			json.setOk(false);
			json.setMessage("获取商品PID失败");
			return json;
		}

		String stateStr = request.getParameter("state");
		int state = 0;
		if (stateStr == null || "".equals(stateStr)) {
			json.setOk(false);
			json.setMessage("获取状态参数失败");
			return json;
		} else {
			state = Integer.valueOf(stateStr);
		}

		try {
			boolean isSuccess = stGsService.useGoodsByState(state, pids);
			if (isSuccess) {
				json.setOk(true);
			} else {
				json.setOk(false);
				json.setMessage("执行失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("state:" + state + ",执行失败，原因 :" + e.getMessage());
			json.setOk(false);
			json.setMessage("state:" + state + ",执行失败，原因:" + e.getMessage());
		}
		return json;
	}

	private void dealRalationAdmin(List<SameTypeGoodsBean> goodList) {
		if (adminList == null || adminList.size() == 0) {
			adminList = stGsService.queryAllAdmin();
		}
		for (SameTypeGoodsBean good : goodList) {
			if (good.getAdminId() > 0) {
				for (Admuser admin : adminList) {
					if (good.getAdminId() == admin.getId()) {
						good.setAdminName(admin.getAdmname());
						break;
					}
				}
			} else {
				continue;
			}
		}
	}

}
