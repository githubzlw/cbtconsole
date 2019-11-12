package com.cbt.controller;

import com.cbt.bean.Category1688Bean;
import com.cbt.bean.CategoryBean;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.CategoryService;
import com.cbt.util.AppConfig;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.EasyUiTreeUtils;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/category")
public class CategoryRefreshController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CategoryRefreshController.class);
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView getDiscountList(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new ModelAndView("categorylist");
		String categoryName = req.getParameter("cname");
		try {
			req.setCharacterEncoding("utf-8");
			categoryName = StringUtils.isBlank(categoryName)?null:categoryName;
			if(categoryName != null){
				categoryName  =new String(categoryName.getBytes("ISO8859-1"),"utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String categoryId = req.getParameter("catid");
		categoryId = StringUtils.isBlank(categoryId)?null:categoryId;
		String strPage = req.getParameter("page");
		int page = StrUtils.isNum(strPage) ? Integer.valueOf(strPage) : 1;
		DataSourceSelector.set("dataSource127hop");
		List<Category1688Bean> list = categoryService.getList(categoryName,categoryId,(page-1)*50);
		int listTotal = categoryService.getListTotal(categoryName, categoryId);
		DataSourceSelector.restore();
		listTotal = listTotal % 50 == 0 ? listTotal / 50 : listTotal / 50 + 1;
		
		mv.addObject("list", list);
		mv.addObject("page", page);
		mv.addObject("cname", categoryName);
		mv.addObject("catid", categoryId);
		mv.addObject("total", listTotal);
		
		return mv;
	}


	@RequestMapping(value = "/queryCategoryTree")
	@ResponseBody
	public List<Map<String, Object>> queryCategoryTree(HttpServletRequest request, String categoryId, String categoryName) {

		CategoryBean categoryBean = new CategoryBean();
		if (StringUtils.isNotBlank(categoryId) && !"0".equals(categoryId)) {
			categoryBean.setCid(categoryId);
		}
		if (StringUtils.isNotBlank(categoryName)) {
			categoryBean.setCategoryName(categoryName);
		}


		List<Map<String, Object>> treeMap = new ArrayList<>();
		try {
			List<CategoryBean> list = categoryService.queryCategoryList(categoryBean);
			int count = categoryService.queryCategoryListCount(categoryBean);
			treeMap = EasyUiTreeUtils.genEasyUiTree(list, count);
			list.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return treeMap;
	}


	/**
	 * @date 2016年11月5日
	 * @author abc
	 * @param type  1-添加/修改  2-删除
	 * @return  
	 */
	@RequestMapping(value="/deal",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public int dealDiscount(HttpServletRequest req, HttpServletResponse resp){
		try{

			String strid = req.getParameter("id");
			String ename = req.getParameter("ename");
			int id = StrUtils.isNum(strid) ? Integer.valueOf(strid) : 0;
			if(StringUtils.isBlank(ename) || id==0){
				return -1;
			}
			ename = ename.trim();
			SendMQ.sendMsg(new RunSqlModel("update 1688_category set en_name='"+ename+"' where id='"+id+"'"));

		}catch (Exception e){
			e.printStackTrace();
		}
		return 1;
	}
	
	
	/**前台application刷新
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/refresh",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String refresh(){
		String url  = AppConfig.ip.indexOf("192.168.1.29:8899") > -1 ?
				"http://192.168.1.29:8081/app/rcategory" : "https://www.import-express.com/app/rcategory";
		DownloadMain.getContentClient(url, null);
		return "refresh success";
	}


	@RequestMapping(value = "/changeCategoryData", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResult changeCategoryData(HttpServletRequest request, String oidCid, String curCid, String newCid) {
		Assert.notNull(oidCid, "获取原始父类别ID失败");
		Assert.notNull(curCid, "获取当前别ID失败");
		Assert.notNull(newCid, "获取新的父类别ID失败");
		JsonResult json = new JsonResult();
		if (StringUtils.isBlank(oidCid)) {
			json.setOk(false);
			json.setMessage("获取原始父类别ID失败");
			return json;
		}
		if (StringUtils.isBlank(curCid)) {
			json.setOk(false);
			json.setMessage("获取当前别ID失败");
			return json;
		}
		if (StringUtils.isBlank(newCid)) {
			json.setOk(false);
			json.setMessage("获取新的父类别ID失败");
			return json;
		}
		try {
			CategoryBean oldParentBean = categoryService.queryCategoryById(oidCid);
			CategoryBean newParentBean = categoryService.queryCategoryById(newCid);
			CategoryBean curBean = categoryService.queryCategoryById(curCid);
			Assert.notNull(oldParentBean, "获取原始父类别bean失败");
			Assert.notNull(newParentBean, "获取原始父类别bean失败");
			Assert.notNull(curBean, "获取原始父类别bean失败");
			if(oldParentBean == null || newParentBean == null || curBean == null){
				json.setOk(false);
				json.setMessage("获取类别bean失败");
				return json;
			}

			// 1.原父类 宠物 116 1   移除 childids中当前类
			if (StringUtils.isNotBlank(oldParentBean.getChildids())) {
				String rs = ("," + oldParentBean.getChildids() + ",").replace(("," + curCid + ","), "");
				if (rs.length() > 2) {
					oldParentBean.setChildids(rs.substring(1, rs.length() - 1));
				} else {
					oldParentBean.setChildids("");
				}
			}
			// 2.现父类 宠物保健品 125278008 2    新增 childids中当前类，获取path数据，lv数据
			if (StringUtils.isNotBlank(newParentBean.getChildids())) {
				if (!("," + newParentBean.getChildids() + ",").contains("," + curCid + ",")) {
					newParentBean.setChildids(newParentBean.getChildids() + "," + curCid);
				}
			} else {
				newParentBean.setChildids(curCid);
			}
			// 3.当前类 宠物医药 125270017 2  更新parent_id，更新path 为 path+现父类id,更新 lv 为 现父类lv数据+1
			// curBean.setParentId(newCid);
			// curBean.setPath(newParentBean.getPath() + "," + newCid);
			// curBean.setLv(newParentBean.getLv() + 1);

			// 4.当前子类 ... 更新path 为更新path 当前类path+当前类id,更新 lv 为 当前类lv数据+1
			// 递归更新所有子类

			List<CategoryBean> resultList = new ArrayList<>();

			resultList.add(oldParentBean);
			resultList.add(newParentBean);

			changeChildData(curBean,newParentBean,resultList);
			categoryService.batchUpdateCategory(resultList);
			resultList.clear();
			json.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("changeCategoryData error:" + e.getMessage());
		}
		return json;
	}

	/**
	 * 递归调用更新类别数据
	 * @param curBean
	 * @param parentBean
	 * @param resultList
	 */
	private void changeChildData(CategoryBean curBean,CategoryBean parentBean,List<CategoryBean> resultList){

		if(parentBean != null){
			curBean.setParentId(parentBean.getCid());
			curBean.setPath(parentBean.getPath() + "," + curBean.getCid());
			curBean.setLv(parentBean.getLv() + 1);

			if(StringUtils.isNotBlank(curBean.getChildids())){
				List<String> cidList = Arrays.asList(curBean.getChildids().split(","));
				List<CategoryBean> childCatList = categoryService.queryChildCategory(cidList);
				for(CategoryBean childBean : childCatList){
					changeChildData(childBean,curBean,resultList);
				}
			}
			resultList.add(curBean);
		}
	}
	
	
}