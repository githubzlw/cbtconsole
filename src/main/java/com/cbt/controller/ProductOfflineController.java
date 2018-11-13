package com.cbt.controller;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.ProductOfflineService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/productOff")
public class ProductOfflineController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ProductOfflineController.class);
	@Autowired
	private ProductOfflineService ptOlService;

	/**
	 * 显示产品列表页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/showProductPage", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView showProductPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("product_manage");
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
				if (userInfo.getConfirmusername().equalsIgnoreCase("Ling")) {
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
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clist", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getCustomsList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("product_manage_list");

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

		String strState = request.getParameter("state");
		if (strState == null || "".equals(strState)) {
			strState = "0";
		}
		queryBean.setState(Integer.valueOf(strState));

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

		String isAbnormal = request.getParameter("isAbnormal");
		if (isAbnormal == null || "".equals(isAbnormal)) {
			isAbnormal = "-1";
		}
		queryBean.setIsAbnormal(Integer.valueOf(isAbnormal));

		String isBenchmark = request.getParameter("isBenchmark");// 对标参数0全部，1对标，2非对标
		if (isBenchmark == null || "".equals(isBenchmark)) {
			isBenchmark = "-1";
		}
		queryBean.setIsBenchmark(Integer.valueOf(isBenchmark));

		String bmFlag = request.getParameter("bmFlag");
		if (bmFlag == null || "".equals(bmFlag)) {
			bmFlag = "0";
		}
		queryBean.setBmFlag(Integer.valueOf(bmFlag));

		String sourceProFlag = request.getParameter("sourceProFlag");
		if (sourceProFlag == null || "".equals(sourceProFlag)) {
			sourceProFlag = "0";
		}
		queryBean.setSourceProFlag(Integer.valueOf(sourceProFlag));

		String soldFlag = request.getParameter("soldFlag");
		if (soldFlag == null || "".equals(soldFlag)) {
			soldFlag = "0";
		}
		queryBean.setSoldFlag(Integer.valueOf(soldFlag));

		String priorityFlag = request.getParameter("priorityFlag");
		if (priorityFlag == null || "".equals(priorityFlag)) {
			priorityFlag = "0";
		}
		queryBean.setPriorityFlag(Integer.valueOf(priorityFlag));

		String addCarFlag = request.getParameter("addCarFlag");
		if (addCarFlag == null || "".equals(addCarFlag)) {
			addCarFlag = "0";
		}
		queryBean.setAddCarFlag(Integer.valueOf(addCarFlag));

		String sourceUsedFlag = request.getParameter("sourceUsedFlag");
		if (sourceUsedFlag == null || "".equals(sourceUsedFlag)) {
			sourceUsedFlag = "0";
		}
		queryBean.setSourceUsedFlag(Integer.valueOf(sourceUsedFlag));

		String ocrMatchFlag = request.getParameter("ocrMatchFlag");
		if (ocrMatchFlag == null || "".equals(ocrMatchFlag)) {
			ocrMatchFlag = "0";
		}
		queryBean.setOcrMatchFlag(Integer.valueOf(ocrMatchFlag));
		
		
		
		List<CustomGoodsPublish> goodsList = ptOlService.queryGoodsInfos(queryBean);

		int count = ptOlService.queryGoodsInfosCount(queryBean);
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

		String strState = request.getParameter("state");
		if (strState == null || "".equals(strState)) {
			strState = "0";
		}
		queryBean.setState(Integer.valueOf(strState));

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

		String isAbnormal = request.getParameter("isAbnormal");
		if (isAbnormal == null || "".equals(isAbnormal)) {
			isAbnormal = "-1";
		}
		queryBean.setIsAbnormal(Integer.valueOf(isAbnormal));

		String isBenchmark = request.getParameter("isBenchmark");// 对标参数0全部，1对标，2非对标
		if (isBenchmark == null || "".equals(isBenchmark)) {
			isBenchmark = "-1";
		}
		queryBean.setIsBenchmark(Integer.valueOf(isBenchmark));

		String bmFlag = request.getParameter("bmFlag");
		if (bmFlag == null || "".equals(bmFlag)) {
			bmFlag = "0";
		}
		queryBean.setBmFlag(Integer.valueOf(bmFlag));

		String sourceProFlag = request.getParameter("sourceProFlag");
		if (sourceProFlag == null || "".equals(sourceProFlag)) {
			sourceProFlag = "0";
		}
		queryBean.setSourceProFlag(Integer.valueOf(sourceProFlag));

		String soldFlag = request.getParameter("soldFlag");
		if (soldFlag == null || "".equals(soldFlag)) {
			soldFlag = "0";
		}
		queryBean.setSoldFlag(Integer.valueOf(soldFlag));

		String priorityFlag = request.getParameter("priorityFlag");
		if (priorityFlag == null || "".equals(priorityFlag)) {
			priorityFlag = "0";
		}
		queryBean.setPriorityFlag(Integer.valueOf(priorityFlag));

		String addCarFlag = request.getParameter("addCarFlag");
		if (addCarFlag == null || "".equals(addCarFlag)) {
			addCarFlag = "0";
		}
		queryBean.setAddCarFlag(Integer.valueOf(addCarFlag));

		String sourceUsedFlag = request.getParameter("sourceUsedFlag");
		if (sourceUsedFlag == null || "".equals(sourceUsedFlag)) {
			sourceUsedFlag = "0";
		}
		queryBean.setSourceUsedFlag(Integer.valueOf(sourceUsedFlag));

		String ocrMatchFlag = request.getParameter("ocrMatchFlag");
		if (ocrMatchFlag == null || "".equals(ocrMatchFlag)) {
			ocrMatchFlag = "0";
		}
		queryBean.setOcrMatchFlag(Integer.valueOf(ocrMatchFlag));
		
		List<CategoryBean> categorys = ptOlService.queryCateroryByParam(queryBean);
		int count = ptOlService.queryGoodsInfosCount(queryBean);

		Map<String, Object> treeRoot = new HashMap<String, Object>();// 根节点
		List<Map<String, Object>> treeMap = new ArrayList<Map<String, Object>>();// 根节点的所有子节点

		List<Map<String, Object>> parentMaps = new ArrayList<Map<String, Object>>();
		// 循环获取一级目录数据并赋值子菜单
		for (CategoryBean ct : categorys) {
			if (ct.getLv() == 1) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put("id", ct.getCid());
				childMap.put("text", ct.getCategoryName());
				childMap.put("state", "closed");
				childMap.put("total", ct.getTotal());
				childMap.put("children", getChildMap(ct.getCid(), categorys, ct.getLv()));
				parentMaps.add(childMap);
			}
		}
		
		List<Map<String, Object>> nwParentMaps = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> ptMap : parentMaps) {
			int ptCount = doTreeCount(ptMap);
			if (ptCount == 0) {
				ptMap.put("children", null);
			}else{
				ptCount += Integer.valueOf(ptMap.get("total").toString());
				ptMap.put("text", ptMap.get("text") + "(<b>" + ptCount + "</b>)");
				nwParentMaps.add(ptMap);
			}	
		}
		parentMaps.clear();
		
		treeRoot.put("children", nwParentMaps);
		treeRoot.put("id", "0");
		treeRoot.put("text", "All Category" + "(<b>" + count + "</b>)");
		treeMap.add(treeRoot);
		categorys.clear();
		return treeMap;
	}

	// 根据cid和lv寻找次级目录
	private List<Map<String, Object>> getChildMap(String cid, List<CategoryBean> categorys, int lv) {
		List<Map<String, Object>> childMaps = new ArrayList<Map<String, Object>>();
		for (CategoryBean ct : categorys) {
			// 判断path数据不为空lv是传递参数的+1值，并且在path中含有父类的cid
			if ((ct.getLv() == lv + 1) && !(ct.getPath() == null || "".equals(ct.getPath()))) {
				// 寻找当前数据的
				String[] catids = ct.getPath().split(",");
				if (catids.length > 0) {
					for (String catid : catids) {
						if (cid.equals(catid)) {
							Map<String, Object> child = new HashMap<String, Object>();
							child.put("id", ct.getCid());
							child.put("text", ct.getCategoryName());
							child.put("total", ct.getTotal());

							List<Map<String, Object>> childList = getChildMap(ct.getCid(), categorys, ct.getLv());
							// 递归创建
							child.put("children", childList);
							if (ct.getLv() == 2 && childList.size() > 0) {
								child.put("state", "closed");
							}
							childMaps.add(child);
							break;
						}
					}
				}
				catids = null;
			}
		}
		return childMaps;
	}

	// 递归统计总数
	@SuppressWarnings("unchecked")
	private int doTreeCount(Map<String, Object> parentMap) {
		int count = 0;
		List<Map<String, Object>> list = (List<Map<String, Object>>) parentMap.get("children");
		if (list == null || list.size() == 0) {
			return count;
		}
		List<Map<String, Object>> nwList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> childMap : list) {
			// 统计子节点的孩子总数+本身的总数
			int cur_cnt = doTreeCount(childMap);
			if (cur_cnt == 0) {
				childMap.put("children", null);
			}
			cur_cnt += Integer.valueOf(childMap.get("total").toString());
			childMap.put("text", childMap.get("text") + "(<b>" + cur_cnt + "</b>)");
			// 覆盖赋值
			childMap.put("total", cur_cnt);
			if(cur_cnt > 0){
				nwList.add(childMap);
			}
			count += cur_cnt;
		}
		parentMap.put("children", nwList);
		list.clear();
		// 返回前记录当前节点的统计个数
		return count;
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
				ptOlService.batchSaveEnName(user, cgLst);
			} else {
				json.setOk(false);
				json.setMessage("获取保存列表失败，请重试");
			}

		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("保存错误，原因：" + e.getMessage());
			LOG.error("保存错误，原因：" + e.getMessage());
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
			ptOlService.batchSaveEnName(user, cgLst);
			CustomGoodsPublish goods = ptOlService.getGoods(pid, 0);
			ptOlService.publish(goods);
			ptOlService.updateState(4, pid, user.getId());
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
		if (pid == null || pid.isEmpty() || !StrUtils.isMatch(pid, "(\\d+)")) {
			return "fail";
		}
		CustomGoodsPublish goods = ptOlService.getGoods(pid, 0);
		if (StringUtils.equals(goods.getEntype(), "null")) {
			goods.setEntype("");
		}
		if (StringUtils.equals(goods.getType(), "null")) {
			goods.setType("");
		}
		ptOlService.publish(goods);

		String result = "";
		// 添加记录
		String adminid = request.getParameter("adminid");
		int publish = ptOlService.updateState(4, pid, Integer.valueOf(adminid));
		if (publish > 0) {
			result = "success";
		} else {
			result = "fail";
		}
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
	@SuppressWarnings("unchecked")
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

			List<CustomGoodsBean> cgLst = (List<CustomGoodsBean>) JSONArray.toCollection(jsonArray,
					CustomGoodsBean.class);// 这里的t是Class<T>
			if (cgLst.size() > 0) {
				ptOlService.batchSaveEnName(user, cgLst);
			} else {
				json.setOk(false);
				json.setMessage("获取保存列表失败，请重试");
				return json;
			}

			for (CustomGoodsBean oldGd : cgLst) {
				CustomGoodsPublish goods = ptOlService.getGoods(oldGd.getPid(), 0);
				if (StringUtils.isBlank(goods.getEntype())) {
					goods.setEntype("");
				}
				if (StringUtils.isBlank(goods.getType())) {
					goods.setType("");
				}

				ptOlService.publish(goods);
				int publish = ptOlService.updateState(4, goods.getPid(), user.getId());
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
			LOG.error("执行失败，原因：" + e.getMessage());
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
			boolean success = ptOlService.batchDeletePids(pidLst);
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
			LOG.error("执行失败，原因：" + e.getMessage());
		}

		return json;
	}


	
}
