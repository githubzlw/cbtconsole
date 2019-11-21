package com.importExpress.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cbt.util.StrUtils;
import com.cbt.warehouse.util.StringUtil;
import com.google.common.collect.Maps;
import com.importExpress.pojo.SynonymsCategoryWrap;
import com.importExpress.service.SynonymsCategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/synonyms/category")
public class SynonymsCategoryController {
	@Autowired
	private SynonymsCategoryService syCategoryService;
	
	/**列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("synonyms_category");
		String catid = request.getParameter("catid");
		catid = StringUtil.isBlank(catid) ? null : catid;
		
		String page = request.getParameter("page");
		page = StrUtils.isNum(page) ? page : "1";
		int categoryListCount = 0;
		categoryListCount = syCategoryService.categoryListCount(catid);
		try {
			if(categoryListCount > 0) {
				List<SynonymsCategoryWrap> categoryList = syCategoryService.getCategoryList(catid, (Integer.parseInt(page)-1)*20);
				mv.addObject("categoryList", categoryList);
			}
			
		} catch (Exception e) {
			log.error("SynonymsCategoryController",e);
		}
		
		int totalPage = categoryListCount% 20 == 0? categoryListCount/20:categoryListCount/20+1;
		mv.addObject("currentPage", page);
		mv.addObject("catid",catid);
		mv.addObject("totalPage",totalPage);
		mv.addObject("totalCount",categoryListCount);
		return mv;
	}
	
	/**更新类别同义词
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> addCategory(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = Maps.newHashMap();
		String catid = request.getParameter("catid");
		String content = request.getParameter("content");
		String category = request.getParameter("category");
		
		SynonymsCategoryWrap wrap = SynonymsCategoryWrap.builder()
									.category(category)
									.catid(catid)
									.synonymsCategory(content).build();
		
		int updateCategory = syCategoryService.addCategory(wrap);
		result.put("status", 200);
		if(updateCategory < 1) {
			result.put("status", 100);
			result.put("message", "更新失败");
		}
		return result;
	}
	private static String reg = "[\\[\\]\\{\\}<>《》。？/：；“”‘’、+=|）（*&%￥#@！~·~`\\!@#\\$%\\^&\\*\\(\\)\\|\\-_\\+\\=\\\\\\.\\:;'\"\\?/]";
	
	public static void main(String[] args) {
		String str = "Hamster Toys,Toys Hamster*";
		str = str.replace("，", ",")
				.replaceAll(reg, " ");
		System.out.println("***"+str+"****");
		
	}
	/**更新类别同义词
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Map<String, Object> updateCategory(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = Maps.newHashMap();
		String catid = request.getParameter("catid");
		String content = request.getParameter("content");
		if(StringUtil.isNotBlank(content)) {
			content = content.replace("，", ",")
					.replaceAll(reg, " ").trim().replaceAll("(\\s+)", " ");
		}
		int updateCategory = syCategoryService.updateCategory(catid, content);
		result.put("status", 200);
		if(updateCategory < 1) {
			result.put("status", 100);
			result.put("message", "更新失败");
		}
		return result;
	}
	/**删除类别同义词
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> deleteCategory(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = Maps.newHashMap();
		String catid = request.getParameter("catid");
		int delete = syCategoryService.delete(catid);
		result.put("status", 200);
		if(delete < 1) {
			result.put("status", 100);
			result.put("message", "删除失败");
		}
		return result;
	}
	
	
	

}
