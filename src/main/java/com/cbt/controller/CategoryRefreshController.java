package com.cbt.controller;

import com.cbt.bean.Category1688Bean;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.CategoryService;
import com.cbt.util.AppConfig;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
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
import java.io.UnsupportedEncodingException;
import java.util.List;

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
	
	
}