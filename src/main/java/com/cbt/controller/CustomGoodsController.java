package com.cbt.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbt.bean.*;
import com.cbt.customer.service.IShopUrlService;
import com.cbt.util.GoodsInfoUtils;
import com.importExpress.pojo.GoodsMd5Bean;
import com.importExpress.pojo.OnlineGoodsStatistic;
import com.importExpress.pojo.ShopMd5Bean;
import com.importExpress.utli.EasyUiTreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cbt.parse.service.StrUtils;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/cutom")
public class CustomGoodsController {
	private static final Log LOG = LogFactory.getLog(CustomGoodsController.class);

	private String imgFilePath = "F:\\console\\tomcatImportCsv\\webapps\\importsvimg\\img\\";

	// private String localIP = "http://27.115.38.42:8083/";
	// private String wanlIP = "http://192.168.1.27:8083/";


	@Autowired
	private CustomGoodsService customGoodsService;

	@Autowired
    private IShopUrlService shopUrlService;

	/**
	 * 列出指定产品的操作记录
	 *
	 * @date 2017年3月14日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/rlist", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getRecordList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("recordlist");
		String pid = request.getParameter("pid");
		if (pid == null || pid.isEmpty() || !StrUtils.isMatch(pid, "(\\d+)")) {
			return mv;
		}
		String page = request.getParameter("page");
		if (page == null || page.isEmpty() || !StrUtils.isMatch(page, "(\\d+)")) {
			page = "1";
		}
		List<CustomRecord> recordList = customGoodsService.getRecordList(pid, Integer.valueOf(page));
		mv.addObject("recordList", recordList);
		int count = recordList == null || recordList.isEmpty() ? 0 : recordList.get(0).getCount();

		count = count % 50 == 0 ? count / 50 : count / 50 + 1;

		mv.addObject("totalpage", count);
		mv.addObject("currentpage", page);

		return mv;
	}

	/**
	 * 显示产品列表页面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/showProductPage", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView showProductPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("custommr");
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
		List<ConfirmUserInfo> AllAdm = dao.getAllUserHasOffUser();
		List<ConfirmUserInfo> newAdms = new ArrayList<ConfirmUserInfo>();
		for (ConfirmUserInfo userInfo : AllAdm) {
			newAdms.add(userInfo);
		}
		mv.addObject("sellAdm", newAdms);
		return mv;
	}

	@RequestMapping(value = "/queryStaticizeList")
	@ResponseBody
	public List<String> queryStaticizeList(String catid) {
		try {
			return customGoodsService.queryStaticizeList(catid);
		} catch (Exception e) {
		}
		return null;
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
		ModelAndView mv = new ModelAndView("customgoods_list");

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
		queryBean.setPage((Integer.valueOf(strPage) -1) * 50);

		String catid = request.getParameter("catid");
		if (StringUtils.isNotBlank(catid) && !"0".equals(catid)) {
			queryBean.setCatid(catid);
		}

		String sttime = request.getParameter("sttime");
		if (StringUtils.isNotBlank(sttime)) {
			queryBean.setSttime(sttime);
		}

		String unsellableReason=request.getParameter("unsellableReason");
		if(StringUtils.isBlank(unsellableReason) || "-1".equals(unsellableReason)){
			unsellableReason=null;
		}
		queryBean.setUnsellableReason(unsellableReason);
		String edtime = request.getParameter("edtime");
		if (StringUtils.isNotBlank(edtime)) {
			queryBean.setEdtime(edtime);
		}

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
		if (StringUtils.isNotBlank(isEdited)) {
			queryBean.setIsEdited(Integer.valueOf(isEdited));
		}


		String isAbnormal = request.getParameter("isAbnormal");
		if (StringUtils.isNotBlank(isAbnormal)) {
			queryBean.setIsAbnormal(Integer.valueOf(isAbnormal));
		}

		String isBenchmark = request.getParameter("isBenchmark");// 对标参数0全部，1对标，2非对标
		if (isBenchmark == null || "".equals(isBenchmark)) {
			isBenchmark = "-1";
		}
		queryBean.setIsBenchmark(Integer.valueOf(isBenchmark));

		/**
		 * 重量检查组合方式( 0 2 3 4 5 2*5 3*5); 0不是异常;2对于重量 比 类别平均重量 高30% 而且 运费占 总价格 占比超 35%的 ;
		 * 3如果重量 比 类别平均重量低40%，请人为检查; 4重量数据为空的; 5对于所有的 运费占总免邮价格 60%以上的
		 */
		String weightCheck = request.getParameter("weightCheck");
		if (weightCheck == null || "".equals(weightCheck)) {
			weightCheck = "-1";
		}
		queryBean.setWeightCheck(Integer.valueOf(weightCheck));

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
			sourceUsedFlag = "-1";
		}
		queryBean.setSourceUsedFlag(Integer.valueOf(sourceUsedFlag));

		String ocrMatchFlag = request.getParameter("ocrMatchFlag");
		if (ocrMatchFlag == null || "".equals(ocrMatchFlag)) {
			ocrMatchFlag = "0";
		}
		queryBean.setOcrMatchFlag(Integer.valueOf(ocrMatchFlag));

		String infringingFlag = request.getParameter("infringingFlag");
		if (org.apache.commons.lang3.StringUtils.isBlank(infringingFlag)) {
			infringingFlag = "-1";
		}
		queryBean.setInfringingFlag(Integer.valueOf(infringingFlag));


		String aliWeightBegin = request.getParameter("aliWeightBegin");
		if(StringUtils.isNotBlank(aliWeightBegin) && !"0".equals(aliWeightBegin)){
			queryBean.setAliWeightBegin(Double.valueOf(aliWeightBegin));
		}

		String aliWeightEnd = request.getParameter("aliWeightEnd");
		if(StringUtils.isNotBlank(aliWeightEnd) && !"0".equals(aliWeightEnd)){
			queryBean.setAliWeightEnd(Double.valueOf(aliWeightEnd));
		}

		String onlineTime = request.getParameter("onlineTime");
		if(StringUtils.isNotBlank(onlineTime)){
			queryBean.setOnlineTime(onlineTime);
		}

		String offlineTime = request.getParameter("offlineTime");
		if(StringUtils.isNotBlank(offlineTime)){
			queryBean.setOfflineTime(offlineTime);
		}

		String editBeginTime = request.getParameter("editBeginTime");
		if(StringUtils.isNotBlank(editBeginTime)){
			queryBean.setEditBeginTime(editBeginTime);
		}

		String editEndTime = request.getParameter("editEndTime");
		if(StringUtils.isNotBlank(editEndTime)){
			queryBean.setEditEndTime(editEndTime);
		}

		String weight1688Begin = request.getParameter("weight1688Begin");
		if(StringUtils.isNotBlank(weight1688Begin) && !"0".equals(weight1688Begin)){
			queryBean.setWeight1688Begin(Double.valueOf(weight1688Begin));
		}

		String weight1688End = request.getParameter("weight1688End");
		if(StringUtils.isNotBlank(weight1688End) && !"0".equals(weight1688End)){
			queryBean.setWeight1688End(Double.valueOf(weight1688End));
		}

		String price1688Begin = request.getParameter("price1688Begin");
		if(StringUtils.isNotBlank(price1688Begin) && !"0".equals(price1688Begin)){
			queryBean.setPrice1688Begin(Double.valueOf(price1688Begin));
		}

		String price1688End = request.getParameter("price1688End");
		if(StringUtils.isNotBlank(price1688End) && !"0".equals(price1688End)){
			queryBean.setPrice1688End(Double.valueOf(price1688End));
		}

		String isSort = request.getParameter("isSort");
		if(StringUtils.isNotBlank(isSort)){
			queryBean.setIsSort(Integer.valueOf(isSort));
		}
		
		String isComplain = request.getParameter("isComplain");
		if(StringUtils.isNotBlank(isComplain) && StringUtils.equals(isComplain, "1")){
			queryBean.setIsComplain(Integer.valueOf(isComplain));
		}

		String fromFlag = request.getParameter("fromFlag");
		if(StringUtils.isNotBlank(fromFlag)){
			queryBean.setFromFlag(Integer.valueOf(fromFlag));
		}


		String finalWeightBegin = request.getParameter("finalWeightBegin");
		if(StringUtils.isNotBlank(finalWeightBegin) && !"0".equals(finalWeightBegin)){
			queryBean.setFinalWeightBegin(Double.valueOf(finalWeightBegin));
		}
		String finalWeightEnd = request.getParameter("finalWeightEnd");
		if(StringUtils.isNotBlank(finalWeightEnd) && !"0".equals(finalWeightEnd)){
			queryBean.setFinalWeightEnd(Double.valueOf(finalWeightEnd));
		}
		String minPrice = request.getParameter("minPrice");
		if(StringUtils.isNotBlank(minPrice) && !"0".equals(minPrice)){
			queryBean.setMinPrice(Double.valueOf(minPrice));
		}
		String maxPrice = request.getParameter("maxPrice");
		if(StringUtils.isNotBlank(maxPrice) && !"0".equals(maxPrice)){
			queryBean.setMaxPrice(Double.valueOf(maxPrice));
		}

		String isSoldFlag = request.getParameter("isSoldFlag");
		if(StringUtils.isNotBlank(isSoldFlag)){
			queryBean.setIsSoldFlag(Integer.valueOf(isSoldFlag));
		}

		String isWeigthZero = request.getParameter("isWeigthZero");
		if(StringUtils.isNotBlank(isWeigthZero)){
			queryBean.setIsWeigthZero(Integer.valueOf(isWeigthZero));
		}



		List<CustomGoodsPublish> goodsList = customGoodsService.queryGoodsInfos(queryBean);
		goodsList.stream().forEach(c -> {
			String complainId = c.getComplainId();
			complainId = StringUtils.endsWith(complainId, ",")?complainId.substring(0, complainId.length()-1) : complainId;
			if(StringUtils.isNotBlank(complainId)) {
				c.setComplain(Arrays.asList(complainId.split(",")));
			}
		});

		int count = customGoodsService.queryGoodsInfosCount(queryBean);
		int amount = (count % 50 == 0 ? count / 50 : count / 50 + 1);
		mv.addObject("catid", catid);
		mv.addObject("goodsList", goodsList);
		mv.addObject("totalpage", amount);
		mv.addObject("totalNum", count);
		mv.addObject("currentpage", strPage);
		mv.addObject("pagingNum", 50);
		return mv;
	}

	@RequestMapping(value = "/queryCategoryTree")
	@ResponseBody
	public List<Map<String, Object>> queryCategoryTree(HttpServletRequest request, HttpServletResponse response) {

		CustomGoodsQuery queryBean = new CustomGoodsQuery();
		//queryBean.setCatid("0");

		String strPage = request.getParameter("page");
		if (strPage == null || strPage.isEmpty() || !StrUtils.isMatch(strPage, "(\\d+)")) {
			strPage = "1";
		}
		queryBean.setPage((Integer.valueOf(strPage) -1) * 50);

		String sttime = request.getParameter("sttime");
		if (StringUtils.isNotBlank(sttime)) {
			queryBean.setSttime(sttime);
		}

		String edtime = request.getParameter("edtime");
		if (StringUtils.isNotBlank(edtime)) {
			queryBean.setEdtime(edtime);
		}

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
		if (StringUtils.isNotBlank(isEdited)) {
			queryBean.setIsEdited(Integer.valueOf(isEdited));
		}

		String isAbnormal = request.getParameter("isAbnormal");
		if (StringUtils.isNotBlank(isAbnormal)) {
			queryBean.setIsAbnormal(Integer.valueOf(isAbnormal));
		}

		String isBenchmark = request.getParameter("isBenchmark");// 对标参数0全部，1对标，2非对标
		if (isBenchmark == null || "".equals(isBenchmark)) {
			isBenchmark = "-1";
		}
		queryBean.setIsBenchmark(Integer.valueOf(isBenchmark));

		/**
		 * 重量检查组合方式( 0 2 3 4 5 2*5 3*5); 0不是异常;2对于重量 比 类别平均重量 高30% 而且 运费占 总价格 占比超 35%的 ;
		 * 3如果重量 比 类别平均重量低40%，请人为检查; 4重量数据为空的; 5对于所有的 运费占总免邮价格 60%以上的
		 */
		String weightCheck = request.getParameter("weightCheck");
		if (weightCheck == null || "".equals(weightCheck)) {
			weightCheck = "-1";
		}
		queryBean.setWeightCheck(Integer.valueOf(weightCheck));

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
			sourceUsedFlag = "-1";
		}
		queryBean.setSourceUsedFlag(Integer.valueOf(sourceUsedFlag));

		String ocrMatchFlag = request.getParameter("ocrMatchFlag");
		if (ocrMatchFlag == null || "".equals(ocrMatchFlag)) {
			ocrMatchFlag = "0";
		}
		queryBean.setOcrMatchFlag(Integer.valueOf(ocrMatchFlag));

		String infringingFlag = request.getParameter("infringingFlag");
		if (org.apache.commons.lang3.StringUtils.isBlank(infringingFlag)) {
			infringingFlag = "-1";
		}
		queryBean.setInfringingFlag(Integer.valueOf(infringingFlag));

		String aliWeightBegin = request.getParameter("aliWeightBegin");
		if(StringUtils.isNotBlank(aliWeightBegin) && !"0".equals(aliWeightBegin)){
			queryBean.setAliWeightBegin(Double.valueOf(aliWeightBegin));
		}

		String aliWeightEnd = request.getParameter("aliWeightEnd");
		if(StringUtils.isNotBlank(aliWeightEnd) && !"0".equals(aliWeightEnd)){
			queryBean.setAliWeightEnd(Double.valueOf(aliWeightEnd));
		}

		String onlineTime = request.getParameter("onlineTime");
		if(StringUtils.isNotBlank(onlineTime)){
			queryBean.setOnlineTime(onlineTime);
		}

		String offlineTime = request.getParameter("offlineTime");
		if(StringUtils.isNotBlank(offlineTime)){
			queryBean.setOnlineTime(offlineTime);
		}

		String editBeginTime = request.getParameter("editBeginTime");
		if(StringUtils.isNotBlank(editBeginTime)){
			queryBean.setEditBeginTime(editBeginTime);
		}

		String editEndTime = request.getParameter("editEndTime");
		if(StringUtils.isNotBlank(editEndTime)){
			queryBean.setEditEndTime(editEndTime);
		}

		String weight1688Begin = request.getParameter("weight1688Begin");
		if(StringUtils.isNotBlank(weight1688Begin) && !"0".equals(weight1688Begin)){
			queryBean.setWeight1688Begin(Double.valueOf(weight1688Begin));
		}

		String weight1688End = request.getParameter("weight1688End");
		if(StringUtils.isNotBlank(weight1688End) && !"0".equals(weight1688End)){
			queryBean.setWeight1688End(Double.valueOf(weight1688End));
		}

		String price1688Begin = request.getParameter("price1688Begin");
		if(StringUtils.isNotBlank(price1688Begin) && !"0".equals(price1688Begin)){
			queryBean.setPrice1688Begin(Double.valueOf(price1688Begin));
		}

		String price1688End = request.getParameter("price1688End");
		if(StringUtils.isNotBlank(price1688End) && !"0".equals(price1688End)){
			queryBean.setPrice1688End(Double.valueOf(price1688End));
		}

		String isSort = request.getParameter("isSort");
		if(StringUtils.isNotBlank(isSort)){
			queryBean.setIsSort(Integer.valueOf(isSort));
		}

		String isComplain = request.getParameter("isComplain");
		if(StringUtils.isNotBlank(isComplain) && StringUtils.equals(isComplain, "1")){
			queryBean.setIsComplain(Integer.valueOf(isComplain));
		}

		String fromFlag = request.getParameter("fromFlag");
		if(StringUtils.isNotBlank(fromFlag)){
			queryBean.setFromFlag(Integer.valueOf(fromFlag));
		}

		String finalWeightBegin = request.getParameter("finalWeightBegin");
		if(StringUtils.isNotBlank(finalWeightBegin) && !"0".equals(finalWeightBegin)){
			queryBean.setFinalWeightBegin(Double.valueOf(finalWeightBegin));
		}
		String finalWeightEnd = request.getParameter("finalWeightEnd");
		if(StringUtils.isNotBlank(finalWeightEnd) && !"0".equals(finalWeightEnd)){
			queryBean.setFinalWeightEnd(Double.valueOf(finalWeightEnd));
		}
		String minPrice = request.getParameter("minPrice");
		if(StringUtils.isNotBlank(minPrice) && !"0".equals(minPrice)){
			queryBean.setMinPrice(Double.valueOf(minPrice));
		}
		String maxPrice = request.getParameter("maxPrice");
		if(StringUtils.isNotBlank(maxPrice) && !"0".equals(maxPrice)){
			queryBean.setMaxPrice(Double.valueOf(maxPrice));
		}

		String isSoldFlag = request.getParameter("isSoldFlag");
		if(StringUtils.isNotBlank(isSoldFlag)){
			queryBean.setIsSoldFlag(Integer.valueOf(isSoldFlag));
		}

		String isWeigthZero = request.getParameter("isWeigthZero");
		if(StringUtils.isNotBlank(isWeigthZero)){
			queryBean.setIsWeigthZero(Integer.valueOf(isWeigthZero));
		}

		List<CategoryBean> categorys = customGoodsService.queryCateroryByParam(queryBean);
		int count = customGoodsService.queryGoodsInfosCount(queryBean);

		List<Map<String, Object>> treeMap = EasyUiTreeUtils.genEasyUiTree(categorys,count);
		categorys.clear();
		return treeMap;
	}
	
	@RequestMapping(value = "/queryStaticizeCategoryTree")
	@ResponseBody
	public List<Map<String, Object>> queryStaticizeCategoryTree() {

		List<CategoryBean> categorys = customGoodsService.queryStaticizeCateroryByParam();
		int count = customGoodsService.queryStaticizeGoodsInfosCount();

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
				customGoodsService.batchSaveEnName(user, cgLst);
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
			customGoodsService.batchSaveEnName(user, cgLst);
			CustomGoodsPublish goods = customGoodsService.getGoods(pid, 0);
			customGoodsService.publish(goods);
			customGoodsService.updateState(4, pid, user.getId());
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
		CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);
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
		customGoodsService.publish(goods);

		String result = "";
		// 添加记录
		String adminid = request.getParameter("adminid");
		int publish = customGoodsService.updateState(4, pid, Integer.valueOf(adminid));
		if (publish > 0) {
			result = "success";
		} else {
			result = "fail";
		}
		// customGoodsService.updateValid(1, pid);

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
				customGoodsService.batchSaveEnName(user, cgLst);
			} else {
				json.setOk(false);
				json.setMessage("获取保存列表失败，请重试");
				return json;
			}

			for (CustomGoodsBean oldGd : cgLst) {
				CustomGoodsPublish goods = customGoodsService.getGoods(oldGd.getPid(), 0);
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

				customGoodsService.publish(goods);
				int publish = customGoodsService.updateState(4, goods.getPid(), user.getId());
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
			boolean success = customGoodsService.batchDeletePids(pidLst);
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
			boolean success = customGoodsService.updateBmFlagByPids(pidLst,user.getId());
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


	@RequestMapping(value = "/onlineGoodsStatistic", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public JsonResult onlineGoodsStatistic(HttpServletRequest request, HttpServletResponse response) {
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
			OnlineGoodsStatistic statistic = new OnlineGoodsStatistic();
			int isEditTotal = customGoodsService.queryIsEditOnlineGoodsTotal(user.getId(),1);
			int onlineTotal = customGoodsService.queryOnlineGoodsTotal(1);
			int typeinTotal = customGoodsService.queryTypeinGoodsTotal(user.getId(),1);

			statistic.setIsEditTotal(isEditTotal);
			statistic.setOnlineTotal(onlineTotal);
			statistic.setTypeinTotal(typeinTotal);
			json.setOk(true);
			json.setData(statistic);

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
	 * @return
	 */
	@RequestMapping(value = "/offshelf", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String offShelf(HttpServletRequest request, HttpServletResponse response) {

		String rs = "";
		String pid = request.getParameter("pid");
		// System.err.println("pid^^^^"+pid);
		if (pid == null || pid.isEmpty()) {
			return rs;
		}
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		boolean is = false;
		try{

			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				return rs;
			}
			pid = pid.endsWith(",") ? pid.substring(0, pid.length() - 1) : pid;

			// 本地产品下架 2-产品下架 3-发布失败 4-发布成功
			is = customGoodsService.updateStateList(2, pid, user.getId());
			rs = "1";
		}catch (Exception e){
			e.printStackTrace();
			rs = "0";
		}
		return rs;
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
	 * 跟进店铺分组查询md5数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryForMd5List")
    @ResponseBody
    public EasyUiJsonResult queryForMd5List(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }
        ShopMd5Bean md5Bean = new ShopMd5Bean();

        int startNum = 0;
        int limitNum = 30;
        String rowStr = request.getParameter("rows");
        if (!(StringUtils.isBlank(rowStr) || "0".equals(rowStr))) {
            limitNum = Integer.valueOf(rowStr);
            md5Bean.setLimitNum(limitNum);
        }

        String pageStr = request.getParameter("page");
        if (!(StringUtils.isBlank(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        md5Bean.setStartNum(startNum);

        String shopId = request.getParameter("shopId");
        if (StringUtils.isNotBlank(shopId)) {
            md5Bean.setShopId(shopId);
        }


        try {
            List<ShopMd5Bean> res = customGoodsService.queryForMd5List(md5Bean);
            int count = customGoodsService.queryForMd5ListCount(md5Bean);
            for(ShopMd5Bean shopMd5Bean : res){
                md5Bean.setMd5Img(shopMd5Bean.getMd5Img().substring(shopMd5Bean.getMd5Img().lastIndexOf("/") + 1));
                shopMd5Bean.setIsMark(customGoodsService.checkShopGoodsImgIsMarkByParam(md5Bean));
            }
            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    /**
     * 标记已删除
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/updateImgDeleteByMd5")
    @ResponseBody
    public JsonResult updateImgDeleteByMd5(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isBlank(admuserJson)) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }

        String md5 = request.getParameter("md5");
        if (StringUtils.isBlank(md5)) {
            json.setOk(false);
            json.setMessage("获取MD5失败");
            return json;
        }


        try {
            customGoodsService.updateImgDeleteByMd5(md5, shopId);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }



    @RequestMapping("/queryMd5NoSameList")
    @ResponseBody
    public EasyUiJsonResult queryMd5NoSameList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }
        ShopMd5Bean md5Bean = new ShopMd5Bean();

        int startNum = 0;
        int limitNum = 30;
        String rowStr = request.getParameter("rows");
        if (!(StringUtils.isBlank(rowStr) || "0".equals(rowStr))) {
            limitNum = Integer.valueOf(rowStr);
            md5Bean.setLimitNum(limitNum);
        }

        String pageStr = request.getParameter("page");
        if (!(StringUtils.isBlank(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        md5Bean.setStartNum(startNum);

        String md5Val = request.getParameter("md5Val");
        if (StringUtils.isNotBlank(md5Val)) {
            md5Bean.setMd5Val(md5Val);
        }


        try {
            List<ShopMd5Bean> res = customGoodsService.queryMd5NoSameList(md5Bean);
            int count = customGoodsService.queryMd5NoSameListCount(md5Bean);
            for(ShopMd5Bean shopMd5Bean : res){
                md5Bean.setMd5Img(shopMd5Bean.getMd5Img().substring(shopMd5Bean.getMd5Img().lastIndexOf("/") + 1));
                shopMd5Bean.setIsMark(customGoodsService.checkShopGoodsImgIsMarkByParam(md5Bean));
            }
            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    /**
     * 根据MD5获取相同MD5的商品
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryShopGoodsByMd5", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView queryShopGoodsByMd5(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("sameMd5ImgShow");
        String md5Val = request.getParameter("md5Val");
        if (StringUtils.isBlank(md5Val)) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取MD5值失败");
            return mv;
        }else{
            mv.addObject("md5Val", md5Val);
        }
        try {
            List<GoodsMd5Bean> list = customGoodsService.queryShopGoodsByMd5(md5Val);
            ShopMd5Bean shopMd5Bean = new ShopMd5Bean();
            for(GoodsMd5Bean md5Bean : list){
            	shopMd5Bean.setMd5Img(md5Bean.getImgShow().substring(md5Bean.getImgShow().lastIndexOf("/") + 1));
            	shopMd5Bean.setShopId(md5Bean.getShopId());
                md5Bean.setIsMark(customGoodsService.checkShopGoodsImgIsMarkByParam(shopMd5Bean));
			}
            mv.addObject("isShow", 1);
            mv.addObject("list", list);
            mv.addObject("showTotal", list.size());
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isShow", 0);
            mv.addObject("message", "查询失败，原因 :" + e.getMessage());
            LOG.error("查询失败，原因 :" + e.getMessage());
        }
        return mv;
    }


    /**
     * 标记公共图片
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/setShopImgFlag")
    @ResponseBody
    public JsonResult setShopImgFlag(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isBlank(admuserJson)) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        }
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        String pids = request.getParameter("pids");
        if (StringUtils.isBlank(pids)) {
            json.setOk(false);
            json.setMessage("获取PID失败");
            return json;
        }

        String md5Val = request.getParameter("md5Val");
        if (StringUtils.isBlank(md5Val)) {
            json.setOk(false);
            json.setMessage("获取md5失败");
            return json;
        }
        try {
            String[] pidArr = pids.split("-");
            List<String> pidList = new ArrayList<>(pidArr.length);
            for(String pid : pidArr){
                if(StringUtils.isNotBlank(pid)){
                    pidList.add(pid);
                }
            }

            List<GoodsMd5Bean> list = customGoodsService.queryShopGoodsByMd5(md5Val);
            List<ShopGoodsInfo> deleteGoodsInfos = new ArrayList<>();
            for (GoodsMd5Bean md5Bean : list) {
                if(pidList.contains(md5Bean.getPid())){}
                ShopGoodsInfo delGd = new ShopGoodsInfo();
                delGd.setShopId(md5Bean.getShopId());
                delGd.setPid(md5Bean.getPid());
                delGd.setImgUrl(md5Bean.getRemotePath().substring(md5Bean.getRemotePath().indexOf(md5Bean.getPid()) + 1));
                delGd.setLocalPath(md5Bean.getLocalPath());
                delGd.setRemotePath(GoodsInfoUtils.changeLocalPathToRemotePath(md5Bean.getRemotePath()));
                deleteGoodsInfos.add(delGd);
            }
            // 插入公共图片表中
            shopUrlService.insertShopGoodsDeleteImgs(deleteGoodsInfos, user.getId());
            list.clear();
            deleteGoodsInfos.clear();

            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("更新失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败，原因:" + e.getMessage());
        }
        return json;
    }


    /**
     * 批量下架商品
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/batchOffGoods")
    @ResponseBody
    public JsonResult batchOffGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isBlank(admuserJson)) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        }
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        String pids = request.getParameter("pids");
        if (StringUtils.isBlank(pids)) {
            json.setOk(false);
            json.setMessage("获取PID失败");
            return json;
        }
        try {
            String[] pidArr = pids.split(",");
            for(String pid : pidArr){
                if(StringUtils.isNotBlank(pid)){
                    // customGoodsService.updateStateList(2, pid, user.getId());
                    customGoodsService.setGoodsValid(pid, user.getAdmName(), user.getId(), 0, "同款下架");
                }
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("更新失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败，原因:" + e.getMessage());
        }
        return json;
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
