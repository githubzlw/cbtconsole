package com.importExpress.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cbt.parse.service.StrUtils;
import com.cbt.service.CategoryService;
import com.cbt.util.Md5Util;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.CatalogProduct;
import com.importExpress.pojo.CatalogProductWrap;
import com.importExpress.pojo.RecommendCatalog;
import com.importExpress.service.RecommendCatalogService;

@Controller
@RequestMapping("/catalog")
public class RecommendCatalogController {
	@Autowired
	private RecommendCatalogService recommendCatalogService;
	@Autowired
	private CategoryService categoryService;
	
	/**挑选产品临时保存
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
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
		}
		Subject currentUser = SecurityUtils.getSubject();
		Admuser admuser = (Admuser)currentUser.getPrincipal();
		String redisKey  = "catalog"+admuser.getId();
		String catalog = Redis.hget(redisKey, saveKey);
		List<Object> redisWrap = new ArrayList<>();
		if(StringUtils.isNotBlank(catalog)) {
			Map<Object,Object> clazzMap = new HashMap<>();
			clazzMap.put("products",CatalogProduct.class);
			redisWrap = SerializeUtil.JsonToList(catalog, CatalogProductWrap.class,clazzMap );
		}
		List<CatalogProductWrap> wraps = null;
		if("1".equals(del)) {
			//取消
			wraps = deleteProduct(redisWrap, pid, result);
		}else {
			CatalogProduct product = recommendCatalogService.product(pid,site);
			if(product == null) {
				result.put("status", 101);
				result.put("message", "产品数据库无该产品"+pid+",请联系开发人员！！");
				return result;
			}
			wraps = mergeProduct(product, redisWrap);
		}
		
		result.put("status", 200);
		catalog =  wraps==null||wraps.isEmpty() ? "" : SerializeUtil.ListToJson(wraps);
		result.put("product", catalog);
		result.put("productSize", wraps==null ? 0:wraps.size());
		result.put("saveKey", saveKey);
		Redis.hset(redisKey, saveKey, catalog,2*60*60);
		return result;
		
	}
	
	/**目录预览与生成
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public Map<String,Object> catalogCreate(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		String preview = request.getParameter("preview");
		//仅预览
		String saveKey = request.getParameter("saveKey");
		if(StringUtils.isBlank(saveKey)) {
			result.put("status", 100);
			result.put("message", "请先挑选产品后再预览！！！");
			return result;
		}
		
		Subject currentUser = SecurityUtils.getSubject();
		Admuser admuser = (Admuser)currentUser.getPrincipal();
		String redisKey  = "catalog"+admuser.getId();
		
		String redisCatalog = Redis.hget(redisKey, saveKey);
		List<CatalogProductWrap> redisWrap = new ArrayList<>();
		if(StringUtils.isNotBlank(redisCatalog)) {
			redisWrap = SerializeUtil.JsonToListT(redisCatalog, CatalogProductWrap.class);
		}else {
			result.put("status", 101);
			result.put("message", "请先挑选产品后再预览！！！");
			return result;
		}
		if("true".equals(preview)) {
			result.put("status", 200);
			result.put("productSize", redisWrap.size());
			result.put("product", redisCatalog);
			return result;
		}
		String tem = request.getParameter("tem");
		int template = Integer.parseInt(tem);
		String catalogName = request.getParameter("catalogname");
		//生成目录
		RecommendCatalog catalog = new RecommendCatalog();
//			catalog.setCatalogFile(catalogFile);
		catalog.setCatalogName(catalogName);
		catalog.setCreateAdmin(admuser.getAdmName());
		int productCount = redisWrap.stream().mapToInt(CatalogProductWrap::getProductCount).sum();
		catalog.setProductCount(productCount);
		catalog.setProductList(redisCatalog);
		catalog.setStatus(1);
		catalog.setTemplate(template);
		int addCatelog = 0;
		try {
			addCatelog = recommendCatalogService.addCatelog(catalog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(addCatelog < 1) {
			result.put("status", 102);
			result.put("message", "生成目录失败！！！");
			return result;
		}
		result.put("status", 200);
		result.put("addCatelog", addCatelog);
		Redis.hdel(redisKey);
		return result;
	}
	/**预览时清空产品
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/clear")
	@ResponseBody
	public Map<String,Object> catalogClear(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		//仅预览
		String saveKey = request.getParameter("saveKey");
		if(StringUtils.isBlank(saveKey)) {
			result.put("status", 100);
			result.put("message", "无产品可清空,请先挑选产品！！！");
			return result;
		}
		Subject currentUser = SecurityUtils.getSubject();
		Admuser admuser = (Admuser)currentUser.getPrincipal();
		String redisKey  = "catalog"+admuser.getId();
		Redis.hdel(redisKey);
		result.put("status", 200);
		return result;
	}
	/**预览时清空产品
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> catalogDelete(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		//仅预览
		String id = request.getParameter("id");
		if(!StrUtils.isNum(id)) {
			result.put("status", 100);
			result.put("message", "数据不存在！！！");
			return result;
		}
		int deleteCatalog = recommendCatalogService.deleteCatalog(Integer.parseInt(id));
		if(deleteCatalog > 0) {
			result.put("status", 200);
		}else {
			result.put("status", 101);
			result.put("message", "删除发生错误！！！");
		}
		return result;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView catalogList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("catalog_list");
		try {
			String intTemplate = request.getParameter("template");
			int template = StrUtils.isNum(intTemplate)? Integer.parseInt(intTemplate) : 0;
			String catalogName = request.getParameter("catalogName");
			catalogName = StringUtils.isBlank(catalogName) ? null : catalogName;
			
			String intPage = request.getParameter("page");
			int page = StrUtils.isNum(intPage) ? Integer.parseInt(intPage) : 1;
			
			mv.addObject("currentPage", page);
			
			page = (page - 1) * 20;
			
			int catalogCount = recommendCatalogService.catalogCount(template, catalogName);
			mv.addObject("catalogCount", catalogCount);
			if(catalogCount > 0) {
				List<RecommendCatalog> catalogList = recommendCatalogService.catalogList(page, template, catalogName);
				mv.addObject("catalogList", catalogList);
			}
			
			int toltalPage = catalogCount% 20 == 0 ? catalogCount / 20 : catalogCount / 20 + 1;
			
			mv.addObject("toltalPage", toltalPage);
			mv.addObject("template", template);
			mv.addObject("catalogName", catalogName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**添加
	 * @param product
	 * @param wraps
	 */
	private List<CatalogProductWrap> addProduct(CatalogProduct product,List<CatalogProductWrap> wraps) {
		String category= categoryService.getCategoryByCatid(product.getCatid());
		CatalogProductWrap productWrap = new CatalogProductWrap();
		productWrap.setCategory(category);
		productWrap.setCatid(product.getCatid());
		List<CatalogProduct> products = new ArrayList<>();
		products.add(product);
		productWrap.setProducts(products );
		productWrap.setProductCount(products.size());
		wraps.add(productWrap);
		return wraps;
	}
	
	/**删除产品
	 * @param redisWrap
	 * @param pid
	 * @param result
	 * @return
	 */
	private List<CatalogProductWrap> deleteProduct(List<Object> redisWrap,String pid,Map<String,Object> result) {
		List<CatalogProductWrap> wraps = new ArrayList<>();
		try {
			for(Object wrap : redisWrap) {
				CatalogProductWrap w = (CatalogProductWrap)wrap;
				List<CatalogProduct> products = w.getProducts();
				List<CatalogProduct> newProducts = new ArrayList<>();
				for(CatalogProduct p : products) {
					if(p.getPid().equals(pid)) {
						continue;
					}
					newProducts.add(p);
				}
				if(newProducts.size()!=0) {
					w.setProducts(newProducts);
					w.setProductCount(newProducts.size());
					wraps.add(w);
				} else {
					result.put("catid", w.getCatid());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wraps;
	}
	
	/**与redis历史产品合并
	 * @param product
	 * @param redisWrap
	 * @return
	 */
	private List<CatalogProductWrap> mergeProduct(CatalogProduct product,List<Object> redisWrap) {
		List<CatalogProductWrap> wraps = new ArrayList<>();
		if(redisWrap.isEmpty()) {
			addProduct(product, wraps);
			return wraps;
		}
		boolean isadd = true;
		for(Object wrap : redisWrap) {
			CatalogProductWrap w = (CatalogProductWrap)wrap;
			if(w.getCatid().equals(product.getCatid())) {
				List<CatalogProduct> products = w.getProducts();
				products.add(product);
				w.setProductCount(products.size());
				isadd = false;
			}
			wraps.add(w);
		}
		if(isadd) {
			addProduct(product, wraps);
		}
		return wraps;
	}
}
