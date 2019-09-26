package com.importExpress.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cbt.util.Md5Util;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.importExpress.pojo.CatalogProduct;
import com.importExpress.pojo.CatalogProductWrap;
import com.importExpress.service.RecommendCatalogService;

@Controller
@RequestMapping("/catalog")
public class RecommendCatalogController {
	@Autowired
	private RecommendCatalogService recommendCatalogService;
	@RequestMapping("/save")
	public Map<String,Object> catalogSave(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		String pid = request.getParameter("pid");
		String del = request.getParameter("del");
		String tem = request.getParameter("tem");
		int site = Integer.parseInt(tem);
		String saveKey = request.getParameter("saveKey");
		if(StringUtils.isBlank(saveKey) ) {
			if("1".equals(del)) {
				result.put("status", 100);
				result.put("message", "无数据可删除,请先挑选产品！！");
				return result;
			}
			saveKey = Md5Util.md5Operation("catalog"+System.currentTimeMillis());
			result.put("saveKey", saveKey);
		}
//		Redis.hset("", field, value);
		String catalog = Redis.hget("catalog", saveKey);
		List<CatalogProductWrap> redisWrap = new ArrayList<>();
		List<CatalogProductWrap> wraps = new ArrayList<>();
		if(StringUtils.isNotBlank(catalog)) {
			redisWrap = SerializeUtil.JsonToListT(catalog, CatalogProductWrap.class);
		}
		if("1".equals(del)) {
			//取消
			for(CatalogProductWrap w : redisWrap) {
				List<CatalogProduct> products = w.getProducts();
				List<CatalogProduct> newProducts = new ArrayList<>();
				for(CatalogProduct p : products) {
					if(p.getPid().equals(pid)) {
						continue;
					}
					newProducts.add(p);
				}
				if(newProducts.size()!=0) {
					wraps.add(w);
				}
			}
		}else {
			CatalogProduct product = recommendCatalogService.product(pid,site);
			if(redisWrap.isEmpty()) {
				CatalogProductWrap productWrap = new CatalogProductWrap();
				wraps.add(productWrap);
			}else {
				for(CatalogProductWrap w : redisWrap) {
					List<CatalogProduct> products = w.getProducts();
					if(w.getCatid().equals(product.getCatid())) {
						products.add(product);
					}
					wraps.add(w);
				}
			}
		}
		result.put("status", 200);
		result.put("product", wraps);
		result.put("productSize", wraps.size());
		Redis.hset("catalog", saveKey, wraps.isEmpty() ? "" : SerializeUtil.ListToJson(wraps),2*60*60);
		return result;
		
	}
	
	@RequestMapping("/create")
	public Map<String,Object> catalogCreate(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		String preview = request.getParameter("preview");
		//仅预览
		String saveKey = request.getParameter("saveKey");
		if("true".equals(preview)) {
			
			
			
		}else {
			//生成目录
			
		}
		return result;
	}

	public ModelAndView catalogList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		
		
		
		return mv;
		
	}
	
	
	
	
}
