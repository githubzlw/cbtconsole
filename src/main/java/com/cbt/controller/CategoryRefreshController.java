package com.cbt.controller;

import com.cbt.bean.Category1688Bean;
import com.cbt.bean.CategoryAllBean;
import com.cbt.bean.CategoryBean;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.service.CategoryService;
import com.cbt.util.AppConfig;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.*;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	 * @date 2016???11???5???
	 * @author abcU
	 * @param type  1-??????/??????  2-??????
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
	
	
	/**??????application??????
	 * @date 2016???11???14???
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

	@RequestMapping(value = "/queryAllCategoryByParam", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public EasyUiJsonResult queryAllCategoryByParam(HttpServletRequest request, String cid, Integer page, Integer rows) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		if (StringUtils.isBlank(cid) || "0".equals(cid)) {
			cid = null;
		}
		if (page == null || page < 1) {
			page = 1;
		}
		if (rows == null || rows < 1) {
			rows = 40;
		}
		int limitNum = rows;
		int startNum = (page - 1) * limitNum;
		try {
			List<CategoryAllBean> list = categoryService.queryAllCategoryByParam(cid, startNum, limitNum);
			int count = categoryService.queryAllCategoryByParamCount(cid, startNum, limitNum);
			json.setSuccess(true);
			json.setTotal(count);
			json.setRows(list);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("queryAllCategoryByParam", e);
			json.setSuccess(false);
		}
		return json;
	}

	@RequestMapping(value = "/getCidInfo", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResult getCidInfo(String cid) {
		Assert.notNull(cid,"??????????????????");
		JsonResult json = new JsonResult();
		if (StringUtils.isBlank(cid)) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		try {
			CategoryBean oldParentBean = categoryService.queryCategoryById(cid);
			if (oldParentBean != null) {
				json.setOk(true);
				json.setData(oldParentBean);
			} else {
				Assert.notNull(oldParentBean,"??????????????????");
				json.setOk(false);
				json.setMessage("??????????????????");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("getCidInfo", e);
		}
		return json;
	}


    @RequestMapping(value = "/updateCategoryInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult updateCategoryInfo(HttpServletRequest request, String cid, String oldCid, String parentCid,
                                         String chName, String enName) {
        JsonResult json = new JsonResult();
        Assert.notNull(cid,"??????????????????");
        if (StringUtils.isBlank(cid)) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        Admuser admuser = UserInfoUtils.getUserInfo(request);

        Assert.notNull(admuser, "?????????");
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        if ((StringUtils.isBlank(parentCid) && StringUtils.isBlank(oldCid))
                && StringUtils.isBlank(chName) && StringUtils.isBlank(enName)) {
        	Assert.isTrue(false,"????????????????????????");
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        try {

			if (StringUtils.isNotBlank(parentCid) && StringUtils.isNotBlank(oldCid)) {
				return updateCategoryDataByCid(oldCid, cid, parentCid, admuser.getId());
			} else if (StringUtils.isNotBlank(chName) || StringUtils.isNotBlank(enName)) {
				CategoryAllBean categoryBean = new CategoryAllBean();
				categoryBean.setCategoryId(cid);
				categoryBean.setChangeName(chName);
				categoryBean.setChangeEnName(enName);
				categoryBean.setUpdateAdminId(admuser.getId());
				// MQ????????????
				String sql = "update 1688_category set update_admin_id =" + categoryBean.getUpdateAdminId();
				if (StringUtils.isNotBlank(categoryBean.getChangeName())) {
					sql += ",change_name ='" + GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(categoryBean.getChangeName()) + "'";
				}
				if (StringUtils.isNotBlank(categoryBean.getChangeEnName())) {
					sql += ",change_en_name ='" + GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(categoryBean.getChangeEnName()) + "'";
				}
				sql += " where category_id ='" + categoryBean.getCategoryId() + "'";
				/*String rsStr = SendMQ.sendMsgByRPC(new RunSqlModel(sql));
				if (StringUtils.isNotBlank(rsStr) && Integer.parseInt(rsStr) > 0) {
					categoryService.updateChangeAllBeanInfo(categoryBean);
					json.setOk(true);
				} else {
					json.setOk(true);
					json.setMessage("??????????????????????????????");
				}*/

				SendMQ.sendMsg(new RunSqlModel(sql));
				categoryService.updateChangeAllBeanInfo(categoryBean);
			}

		} catch (Exception e) {
            e.printStackTrace();
            LOG.error("updateCategoryInfo", e);
            Assert.isTrue(false,e.getMessage());
            json.setOk(false);
            json.setMessage("updateCategoryInfo error:" + e.getMessage());
        }
        return json;
    }


	@RequestMapping(value = "/changeCategoryData", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResult changeCategoryData(HttpServletRequest request, String oidCid, String curCid, String newCid) {
		Assert.notNull(oidCid, "?????????????????????ID??????");
		Assert.notNull(curCid, "???????????????ID??????");
		Assert.notNull(newCid, "?????????????????????ID??????");

		JsonResult json = new JsonResult();
        Admuser admuser =  UserInfoUtils.getUserInfo(request);

		Assert.notNull(admuser, "?????????");
		if (admuser == null || admuser.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}


		if (StringUtils.isBlank(oidCid)) {
			json.setOk(false);
			json.setMessage("?????????????????????ID??????");
			return json;
		}
		if (StringUtils.isBlank(curCid)) {
			json.setOk(false);
			json.setMessage("???????????????ID??????");
			return json;
		}
		if (StringUtils.isBlank(newCid)) {
			json.setOk(false);
			json.setMessage("?????????????????????ID??????");
			return json;
		}
		try {
			updateCategoryDataByCid(oidCid,curCid,newCid,admuser.getId());
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("changeCategoryData error:" + e.getMessage());
			LOG.error("changeCategoryData error:", e);
		}
		return json;
	}


	@RequestMapping(value = "/addCategoryInfo", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResult addCategoryInfo(HttpServletRequest request, @RequestParam(name = "parentCid", required = true) String parentCid,
									  @RequestParam(name = "chName", required = true) String chName,
									  @RequestParam(name = "enName", required = true) String enName) {
		JsonResult json = new JsonResult();
		Admuser admuser = UserInfoUtils.getUserInfo(request);

		Assert.notNull(admuser, "?????????");
		if (admuser == null || admuser.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}

		if (StringUtils.isBlank(parentCid) || StringUtils.isBlank(chName) || StringUtils.isBlank(enName)) {
			Assert.isTrue(false, "????????????????????????");
			json.setOk(false);
			json.setMessage("????????????????????????");
			return json;
		}
		try {

			CategoryAllBean categoryBean = new CategoryAllBean();
			categoryBean.setName(chName);
			categoryBean.setEnName(enName);
			categoryBean.setParentId(parentCid);

			categoryBean.setDescription("import catid");
			categoryBean.setUpdateAdminId(admuser.getId());
			categoryService.insertIntoCatidInfo(categoryBean);

			json.setSuccess("???????????????ID???"  + categoryBean.getId());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("updateCategoryInfo", e);
			Assert.isTrue(false, e.getMessage());
			json.setOk(false);
			json.setMessage("updateCategoryInfo error:" + e.getMessage());
		}
		return json;
	}


    private JsonResult updateCategoryDataByCid(String oidCid, String curCid, String newCid, int adminId) {
		JsonResult json = new JsonResult();
		CategoryBean oldParentBean = categoryService.queryCategoryById(oidCid);
		CategoryBean newParentBean = categoryService.queryCategoryById(newCid);
		CategoryBean curBean = categoryService.queryCategoryById(curCid);
		Assert.notNull(oldParentBean, "?????????????????????bean??????");
		Assert.notNull(newParentBean, "?????????????????????bean??????");
		Assert.notNull(curBean, "?????????????????????bean??????");
		if (oldParentBean == null || newParentBean == null || curBean == null) {
			json.setOk(false);
			json.setMessage("????????????bean??????");
			return json;
		}

		// 1.????????? ?????? 116 1   ?????? childids????????????
		if (StringUtils.isNotBlank(oldParentBean.getChildids())) {
			String rs = ("," + oldParentBean.getChildids() + ",").replace(("," + curCid + ","), "");
			if (rs.length() > 2) {
				oldParentBean.setChildids(rs.substring(1, rs.length() - 1));
			} else {
				oldParentBean.setChildids("");
			}
		}
		// 2.????????? ??????????????? 125278008 2    ?????? childids?????????????????????path?????????lv??????
		if (StringUtils.isNotBlank(newParentBean.getChildids())) {
			if (!("," + newParentBean.getChildids() + ",").contains("," + curCid + ",")) {
				newParentBean.setChildids(newParentBean.getChildids() + "," + curCid);
			}
		} else {
			newParentBean.setChildids(curCid);
		}
		// 3.????????? ???????????? 125270017 2  ??????parent_id?????????path ??? path+?????????id,?????? lv ??? ?????????lv??????+1
		// curBean.setParentId(newCid);
		// curBean.setPath(newParentBean.getPath() + "," + newCid);
		// curBean.setLv(newParentBean.getLv() + 1);

		// 4.???????????? ... ??????path ?????????path ?????????path+?????????id,?????? lv ??? ?????????lv??????+1
		// ????????????????????????

		List<CategoryBean> resultList = new ArrayList<>();

		resultList.add(oldParentBean);
		resultList.add(newParentBean);

		changeChildData(curBean, newParentBean, resultList);

		resultList.stream().forEach(e -> {
			StringBuffer mqSql = new StringBuffer();
			e.setAdminId(adminId);
			mqSql.append("update 1688_category set change_parent_id ='" + e.getParentId() + "'");
			mqSql.append(",change_path ='" + e.getPath() + "'");
			mqSql.append(",change_childids ='" + e.getChildids() + "'");
			mqSql.append(",change_lv =" + e.getLv() + "");
			mqSql.append(",update_admin_id =" + adminId + "");
			mqSql.append(" where category_id ='" + e.getCid() + "'");
			SendMQ.sendMsg(new RunSqlModel(mqSql.toString()));
		});

		/*String rsStr = SendMQ.sendMsgByRPC(new RunSqlModel(mqSql.toString()));
		if (StringUtils.isNotBlank(rsStr) && Integer.parseInt(rsStr) > 0) {
			categoryService.batchUpdateCategory(resultList);
		}*/


		categoryService.batchUpdateCategory(resultList);

		resultList.clear();
		json.setOk(true);
		return json;
	}

	/**
	 * ??????????????????????????????
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