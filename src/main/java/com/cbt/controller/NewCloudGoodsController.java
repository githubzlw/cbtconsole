package com.cbt.controller;

import com.cbt.bean.*;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.NewCloudGoodsService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.EasyUiTreeUtils;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName NewCloudGoodsController 
 * @Description 新品云商品controller
 * @author Administrator
 * @date 2018年2月9日
 */

@Controller
@RequestMapping(value = "/newcloud")
public class NewCloudGoodsController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(NewCloudGoodsController.class);
	
	private String imgFilePath = "F:\\console\\tomcatImportCsv\\webapps\\importsvimg\\img\\";


	@Autowired
	private NewCloudGoodsService newCloudService;


	/**
	 * 显示产品列表页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/showProductPage", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView showProductPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("newcloudManage");
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("uid", 0);
			return mv;
		} else {
			mv.addObject("admName", user.getAdmName());
			mv.addObject("roletype", user.getRoletype());
			mv.addObject("uid", user.getId());
		}
		UserDao dao = new UserDaoImpl();
		List<ConfirmUserInfo> AllAdm = dao.getAllByOperations();
		List<ConfirmUserInfo> newAdms = new ArrayList<ConfirmUserInfo>();
		for (ConfirmUserInfo userInfo : AllAdm) {
			if (userInfo.getRole() == 0) {
				if (userInfo.getConfirmusername().equalsIgnoreCase("Ling") || userInfo.getConfirmusername().equalsIgnoreCase("emmaxie")
						|| userInfo.getConfirmusername().equalsIgnoreCase("admin1")) {
					newAdms.add(userInfo);
				} else if (userInfo.getConfirmusername().equalsIgnoreCase("testAdm")) {
					newAdms.add(userInfo);
				}
			} else if (userInfo.getRole() == 1 || userInfo.getRole() == 2) {
				newAdms.add(userInfo);
			}
		}
		mv.addObject("sellAdm", newAdms);
		return mv;
	}

	/**
	 * 列出产品
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clist", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getCustomsList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("newcloudGoodsList");

		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			return mv;
		} else {
			String roletype = user.getRoletype();
			int uid = user.getId();
			String admName = user.getAdmName();
			mv.addObject("admName", admName);
			mv.addObject("roletype", roletype);
			mv.addObject("uid", uid);
		}

		CustomGoodsQuery queryBean = new CustomGoodsQuery();

		String strPage = request.getParameter("page");
		if (strPage == null || strPage.isEmpty() || !StrUtils.isMatch(strPage, "(\\d+)")) {
			strPage = "1";
		}
		queryBean.setPage(Integer.valueOf(strPage));

		String catid = request.getParameter("catid");
		if (catid == null || "".equals(catid)) {
			catid = "";
		}
		queryBean.setCatid(catid);
		
		String valid = request.getParameter("valid");
		if(valid == null || "".equals(valid)) {
			valid = "-1";
		}
		queryBean.setValid(Integer.parseInt(valid));

		String sttime = request.getParameter("sttime");
		if (sttime == null || "".equals(sttime)) {
			sttime = "";
		}
		queryBean.setSttime(sttime);

		String edtime = request.getParameter("edtime");
		if (edtime == null || "".equals(edtime)) {
			edtime = "";
		}
		queryBean.setEdtime(edtime);

		String adminid = request.getParameter("adminid");
		if (adminid == null || adminid.isEmpty()) {
			adminid = "0";
		}
		queryBean.setAdminId(Integer.valueOf(adminid));

		String isEdited = request.getParameter("isEdited");
		if (isEdited == null || isEdited.isEmpty()) {
			isEdited = "-1";
		}
		queryBean.setIsEdited(Integer.valueOf(isEdited));

		List<CustomGoodsPublish> goodsList = newCloudService.queryGoodsInfos(queryBean);

		int count = newCloudService.queryGoodsInfosCount(queryBean);
		int amount = (count % 40 == 0 ? count / 40 : count / 40 + 1);
		mv.addObject("catid", catid);
		mv.addObject("goodsList", goodsList);
		mv.addObject("totalpage", amount);
		mv.addObject("totalNum", count);
		mv.addObject("currentpage", strPage);
		mv.addObject("pagingNum", 40);
		return mv;
	}

	@RequestMapping(value = "/queryCategoryTree")
	@ResponseBody
	public List<Map<String, Object>> queryCategoryTree(HttpServletRequest request, HttpServletResponse response) {

		CustomGoodsQuery queryBean = new CustomGoodsQuery();
		queryBean.setCatid("0");

		String strPage = request.getParameter("page");
		if (strPage == null || strPage.isEmpty() || !StrUtils.isMatch(strPage, "(\\d+)")) {
			strPage = "1";
		}
		queryBean.setPage(Integer.valueOf(strPage));
		
		String valid = request.getParameter("valid");
		if(valid == null || "".equals(valid)) {
			valid = "-1";
		}
		queryBean.setValid(Integer.parseInt(valid));

		String sttime = request.getParameter("sttime");
		if (sttime == null || "".equals(sttime)) {
			sttime = "";
		}
		queryBean.setSttime(sttime);

		String edtime = request.getParameter("edtime");
		if (edtime == null || "".equals(edtime)) {
			edtime = "";
		}
		queryBean.setEdtime(edtime);

		String adminid = request.getParameter("adminid");
		if (adminid == null || adminid.isEmpty()) {
			adminid = "0";
		}
		queryBean.setAdminId(Integer.valueOf(adminid));

		String isEdited = request.getParameter("isEdited");
		if (isEdited == null || isEdited.isEmpty()) {
			isEdited = "-1";
		}
		queryBean.setIsEdited(Integer.valueOf(isEdited));

		List<CategoryBean> categorys = newCloudService.queryCateroryByParam(queryBean);
		int count = newCloudService.queryGoodsInfosCount(queryBean);

		List<Map<String, Object>> treeMap = EasyUiTreeUtils.genEasyUiTree(categorys,count);
		categorys.clear();
		return treeMap;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/batchSaveEnName")
	@ResponseBody
	public JsonResult batchSaveEnName(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");

		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取登录信息失败，请登录");
				return json;
			}

			String ennamesJsonStr = request.getParameter("ennames");

			JSONArray jsonArray = JSONArray.fromObject(ennamesJsonStr);// 把String转换为json

			List<CustomGoodsBean> cgLst = (List<CustomGoodsBean>) JSONArray.toCollection(jsonArray,
					CustomGoodsBean.class);// 这里的t是Class<T>
			if (cgLst.size() > 0) {
				newCloudService.batchSaveEnName(user, cgLst);
			} else {
				json.setOk(false);
				json.setMessage("获取保存列表失败，请重试");
			}

		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("保存错误，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/saveAndRepublishGoods")
	@ResponseBody
	public JsonResult saveAndRepublishGoods(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");

		String pid = request.getParameter("pid");
		String enname = request.getParameter("enname");

		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取登录信息失败，请登录");
				return json;
			}
			if (pid == null || "".equals(pid)) {
				json.setOk(false);
				json.setMessage("获取pid失败");
				return json;
			}
			if (enname == null || "".equals(enname)) {
				json.setOk(false);
				json.setMessage("获取编辑详情失败");
				return json;
			}

			List<CustomGoodsBean> cgLst = new ArrayList<CustomGoodsBean>();
			CustomGoodsBean cg = new CustomGoodsBean();
			cg.setPid(pid);
			cg.setEnname(enname);
			cgLst.add(cg);
			newCloudService.batchSaveEnName(user, cgLst);
			CustomGoodsPublish goods = newCloudService.getGoods(pid, 0);
			newCloudService.publish(goods);
			newCloudService.updateState(4, pid, user.getId());
			json.setOk(true);
			json.setMessage("执行成功");

		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("保存错误，原因：" + e.getMessage());
			LOG.error("保存错误，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 更新数据到线上
	 * 
	 * @date 2017年3月21日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/publish", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String publish(HttpServletRequest request, HttpServletResponse response) {
		String pid = request.getParameter("pid");
		// System.err.println("pid^^^^"+pid);
		if (pid == null || pid.isEmpty() || !StrUtils.isMatch(pid, "(\\d+)")) {
			return "fail";
		}
		CustomGoodsPublish goods = newCloudService.getGoods(pid, 0);
		/*
		 * String remotpath = goods.getRemotpath(); if
		 * (remotpath.indexOf("/importsvimg/") > -1) { remotpath =
		 * ContentConfig.IMG_URL + remotpath.split("/importsvimg/")[1]; } else
		 * if (remotpath.indexOf("/importcsvimg/") > -1) { remotpath =
		 * ContentConfig.IMG_URL + remotpath.split("/importcsvimg/")[1]; }
		 * goods.setRemotpath(remotpath);
		 */
		// System.err.println("goods^^"+goods);
		if (StringUtils.equals(goods.getEntype(), "null")) {
			goods.setEntype("");
		}
		if (StringUtils.equals(goods.getType(), "null")) {
			goods.setType("");
		}
		newCloudService.publish(goods);

		String result = "";
		// 添加记录
		String adminid = request.getParameter("adminid");
		int publish = newCloudService.updateState(4, pid, Integer.valueOf(adminid));
		if (publish > 0) {
			result = "success";
		} else {
			result = "fail";
		}
		// newCloudService.updateValid(1, pid);

		/*
		 * if (publish > 0) { //
		 * System.err.println("reotUrl+pid^^"+ContentConfig.DOWNLOAD_URL+pid);
		 * String content =
		 * DownloadMain.getContentClient(ContentConfig.DOWNLOAD_URL + pid +
		 * "&admin=" + admin, null); if (content.isEmpty()) { result = "fail"; }
		 * // System.err.println("contentClient^^"+content); }
		 */
		return result;
	}


	/**
	 * 批量保存并更新数据到线上
	 * 
	 * @date 2017年3月21日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/batchSavePublish", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public JsonResult batchSavePublish(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");

		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取登录信息失败，请登录");
				return json;
			}

			String ennamesJsonStr = request.getParameter("ennames");

			JSONArray jsonArray = JSONArray.fromObject(ennamesJsonStr);// 把String转换为json

			@SuppressWarnings("unchecked")
			List<CustomGoodsBean> cgLst = (List<CustomGoodsBean>) JSONArray.toCollection(jsonArray,
					CustomGoodsBean.class);// 这里的t是Class<T>
			if (cgLst.size() > 0) {
				newCloudService.batchSaveEnName(user, cgLst);
			} else {
				json.setOk(false);
				json.setMessage("获取保存列表失败，请重试");
				return json;
			}

			for (CustomGoodsBean oldGd : cgLst) {
				CustomGoodsPublish goods = newCloudService.getGoods(oldGd.getPid(), 0);
				/*
				 * String remotpath = goods.getRemotpath(); if
				 * (remotpath.indexOf("/importsvimg/") > -1) { remotpath =
				 * ContentConfig.IMG_URL + remotpath.split("/importsvimg/")[1];
				 * } else if (remotpath.indexOf("/importcsvimg/") > -1) {
				 * remotpath = ContentConfig.IMG_URL +
				 * remotpath.split("/importcsvimg/")[1]; }
				 * goods.setRemotpath(remotpath);
				 */
				if (StringUtils.isBlank(goods.getEntype())) {
					goods.setEntype("");
				}
				if (StringUtils.isBlank(goods.getType())) {
					goods.setType("");
				}

				newCloudService.publish(goods);
				int publish = newCloudService.updateState(4, goods.getPid(), user.getId());
				if (publish > 0) {
					json.setOk(true);
					json.setMessage("执行成功");
				} else {
					json.setOk(false);
					json.setMessage("执行失败，本地发布状态未更新");
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("执行失败,原因：" + e.getMessage());
			LOG.error("执行失败,原因：" + e.getMessage());
		}

		return json;
	}

	/**
	 * 批量保存并更新数据到线上
	 * 
	 * @date 2017年3月21日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/batchDelete", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public JsonResult batchDelete(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");

		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取登录信息失败，请登录");
				return json;
			} else {
				if (!("0".equals(user.getRoletype()) || "1".equals(user.getRoletype()))) {
					json.setOk(false);
					json.setMessage("当前操作人无权限删除");
					return json;
				}
			}

			String[] pidLst = null;
			String pids = request.getParameter("pids");
			if (pids == null || "".equals(pids)) {
				json.setOk(false);
				json.setMessage("获取pids失败");
				return json;
			} else {
				if (pids.endsWith(",")) {
					pidLst = pids.substring(0, pids.length() - 1).split(",");
				} else {
					pidLst = pids.split(",");
				}
			}
			boolean success = newCloudService.batchDeletePids(pidLst);
			if (success) {
				json.setOk(true);
			} else {
				json.setOk(false);
				json.setMessage("执行失败,请重试");
			}
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("执行失败,原因：" + e.getMessage());
			LOG.error("执行失败,原因：" + e.getMessage());
		}

		return json;
	}

	/**
	 * 
	 * @Title markerByAdm 
	 * @Description 人为对标flag更新
	 * @param request
	 * @param response
	 * @return JsonResult
	 */
	@RequestMapping(value = "/markerByAdm", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public JsonResult markerByAdm(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");

		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取登录信息失败，请登录");
				return json;
			}

			String[] pidLst = null;
			String pids = request.getParameter("pids");
			if (pids == null || "".equals(pids)) {
				json.setOk(false);
				json.setMessage("获取pids失败");
				return json;
			} else {
				if (pids.endsWith(",")) {
					pidLst = pids.substring(0, pids.length() - 1).split(",");
				} else {
					pidLst = pids.split(",");
				}
			}
			boolean success = newCloudService.updateBmFlagByPids(pidLst,user.getId());
			if (success) {
				json.setOk(true);
			} else {
				json.setOk(false);
				json.setMessage("执行失败,请重试");
			}
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("执行失败,原因：" + e.getMessage());
			LOG.error("执行失败,原因：" + e.getMessage());
		}

		return json;
	}


	/**
	 * 产品下架处理
	 * 
	 * @date 2016年12月23日
	 * @author abc
	 * @param pid
	 * @return
	 */
	@RequestMapping(value = "/offshelf", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String offShelf(HttpServletRequest request, HttpServletResponse response) {

		String pid = request.getParameter("pid");
		// System.err.println("pid^^^^"+pid);
		if (pid == null || pid.isEmpty()) {
			return "";
		}
		pid = pid.endsWith(",") ? pid.substring(0, pid.length() - 1) : pid;
		//List<String> pidlist = Arrays.asList(pid.split(","));

		//pid = "'" + pid.replace(",", "','") + "'";

		String adminid = request.getParameter("adminid");
		if (adminid == null || adminid.isEmpty() || "0".equals(adminid)) {
			return "";
		}
		
		// 本地产品下架 2-产品下架 3-发布失败 4-发布成功
		boolean is = newCloudService.updateStateList(2, pid, Integer.valueOf(adminid));
		// 线上产品状态只有两种:下架和上架
		//newCloudService.updateValidList(0, pid);
		
		//int updateValid = newCloudService.setGoodsValid(pid, "", Integer.valueOf(adminid), -1);

		// 添加记录--本地产品状态记录
		//newCloudService.insertRecordList(pidlist, adminid, 2, "产品下架");

		return is ? "1" :  "0";
	}

	/**
	 * 列出所有图片
	 * 
	 * @date 2017年1月17日
	 * @author abc
	 * @return
	 */
	@RequestMapping(value = "/file", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getImgList(String path) {
		ModelAndView mv = new ModelAndView("customfile");
		if (path == null || path.isEmpty()) {
			path = imgFilePath;
		}
		List<FileBean> list = fileList(new File(path), imgFilePath);
		mv.addObject("files", list);
		return mv;
	}

	/**
	 * 删除文件
	 * 
	 * @date 2017年1月17日
	 * @author abc
	 * @return
	 */
	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String deleteFile(String path) {
		if (path == null || path.isEmpty()) {
			return "";
		}
		String[] paths = path.split(",");
		for (String p : paths) {
			if (p.isEmpty()) {
				continue;
			}
			deleteFile(new File(imgFilePath + p));
		}

		return "";
	}

	/**
	 * 删除文件及文件夹
	 * 
	 * @date 2017年1月18日
	 * @author abc
	 * @param file
	 */
	private void deleteFile(File file) {
		if (file.isDirectory())// 判断file是否是目录
		{
			File[] lists = file.listFiles();
			for (File list : lists) {
				deleteFile(list);
			}
		}
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 列出文件
	 * 
	 * @date 2017年1月18日
	 * @author abc
	 * @param file
	 * @param replace
	 * @return
	 */
	private List<FileBean> fileList(File file, String replace) {
		List<FileBean> list = new ArrayList<FileBean>();
		if (file.isDirectory())// 判断file是否是目录
		{
			File[] lists = file.listFiles();
			if (lists != null) {
				for (int i = 0; i < lists.length; i++) {
					FileBean bean = new FileBean();
					String path = lists[i].getPath();
					String filePath = path.replace(file.getPath() + "\\", "");
					bean.setFileLevel(path.split("(\\\\)").length - 7);
					bean.setFileParent((file.getPath() + "\\").replace(replace, ""));
					bean.setFilePath(filePath);
					bean.setIsFile(filePath.indexOf(".") > -1 ? 0 : 1);
					bean.setIndex(i);
					if (StrUtils.isFind(filePath, "((\\.jpg)|(\\.png))")) {
						bean.setIsImg(1);
					} else {
						bean.setIsImg(0);
					}
					bean.setList(fileList(lists[i], replace));
					list.add(bean);
				}
			}
		}
		return list;
	}
}
