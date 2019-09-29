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
	private String saveKey = "catalogkey";
	
	/**挑选产品临时保存
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> catalogSave(HttpServletRequest request, HttpServletResponse response) {
		Subject currentUser = SecurityUtils.getSubject();
		Admuser admuser = (Admuser)currentUser.getPrincipal();
		String redisKey  = "catalog"+admuser.getId();
		
		Map<String,Object> result = new HashMap<>();
		String pid = request.getParameter("pid");
		String del = request.getParameter("del");
		String editid = request.getParameter("editid");
		String tem = request.getParameter("tem");
		int site = Integer.parseInt(tem);
		String redisCatalog = Redis.hget(redisKey, saveKey);
		if(StringUtils.isBlank(redisCatalog) ) {
			//若是从管理页面过来的，先获取产品数据
			redisCatalog = catalogProduct(editid);
			if(StringUtils.isBlank(redisCatalog) && "1".equals(del)) {
				result.put("status", 100);
				result.put("message", "无数据可删除,请先挑选产品！！");
				return result;
			}else {
				if(StringUtils.isBlank(redisCatalog)) {
					Redis.hset(redisKey, saveKey, redisCatalog,2*60*60);
				}
			}
		}
		
		List<Object> redisWrap = new ArrayList<>();
		if(StringUtils.isNotBlank(redisCatalog)) {
			Map<Object,Object> clazzMap = new HashMap<>();
			clazzMap.put("products",CatalogProduct.class);
			redisWrap = SerializeUtil.JsonToList(redisCatalog, CatalogProductWrap.class,clazzMap );
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
		redisCatalog =  wraps==null||wraps.isEmpty() ? "" : SerializeUtil.ListToJson(wraps);
		result.put("product", redisCatalog);
		result.put("productSize", wraps==null ? 0:wraps.size());
//		result.put("saveKey", saveKey);
		Redis.hset(redisKey, saveKey, redisCatalog,2*60*60);
		return result;
		
	}
	
	/**目录预览
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/product")
	@ResponseBody
	public Map<String,Object> catalogProduct(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		Subject currentUser = SecurityUtils.getSubject();
		Admuser admuser = (Admuser)currentUser.getPrincipal();
		String redisKey  = "catalog"+admuser.getId();
		String isManag = request.getParameter("isManag");
		List<CatalogProductWrap> redisWrap = new ArrayList<>();
		String redisCatalog = "";
		if("true".equals(isManag)) {
			String id = request.getParameter("id");
			//仅预览
			if(!StrUtils.isNum(id)) {
				result.put("status", 100);
				result.put("message", "数据不存在！！！");
				return result;
			}
			RecommendCatalog catalogById = recommendCatalogService.catalogById(Integer.parseInt(id));
			if(catalogById == null) {
				result.put("status", 101);
				result.put("message", "数据不存在！！！");
				return result;
			}
			result.put("productSize", 0);
			redisCatalog = catalogById.getProductList();
			if(StringUtils.isBlank(redisCatalog)) {
				result.put("status", 101);
				result.put("message", "无产品可预览！！！");
				return result;
			}
			redisWrap = SerializeUtil.JsonToListT(redisCatalog, CatalogProductWrap.class);
			result.put("productSize", redisWrap.size());
			Redis.hset(redisKey, saveKey, redisCatalog,2*60*60);
		}else {
			redisCatalog = Redis.hget(redisKey, saveKey);
			if(StringUtils.isNotBlank(redisCatalog)) {
				redisWrap = SerializeUtil.JsonToListT(redisCatalog, CatalogProductWrap.class);
			}
		}
		result.put("status", 200);
		result.put("productSize", redisWrap.size());
		result.put("product", redisCatalog);
		return result;
		
	}
	/**目录生成
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public Map<String,Object> catalogCreate(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		//仅预览
		Subject currentUser = SecurityUtils.getSubject();
		Admuser admuser = (Admuser)currentUser.getPrincipal();
		String redisKey  = "catalog"+admuser.getId();
		
		String redisCatalog = Redis.hget(redisKey, saveKey);
		List<CatalogProductWrap> redisWrap = new ArrayList<>();
		if(StringUtils.isNotBlank(redisCatalog)) {
			redisWrap = SerializeUtil.JsonToListT(redisCatalog, CatalogProductWrap.class);
		}else {
			result.put("status", 101);
			result.put("message", "请先挑选产品！！！");
			return result;
		}
		String tem = request.getParameter("tem");
		int template = Integer.parseInt(tem);
		String id = request.getParameter("id");
		String catalogName = request.getParameter("catalogname");
		//生成目录
		int addCatelog = 0;
		RecommendCatalog catalog = new RecommendCatalog();
		catalog.setCatalogName(catalogName);
		catalog.setCreateAdmin(admuser.getAdmName());
		int productCount = redisWrap.stream().mapToInt(CatalogProductWrap::getProductCount).sum();
		catalog.setProductCount(productCount);
		catalog.setProductList(redisCatalog);
		catalog.setStatus(1);
		catalog.setTemplate(template);
		if(StrUtils.isNum(id)) {
			catalog.setId(Integer.parseInt(id));
			addCatelog = recommendCatalogService.updateCatalog(catalog);
		}else {
			addCatelog = recommendCatalogService.addCatelog(catalog);
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
		Subject currentUser = SecurityUtils.getSubject();
		Admuser admuser = (Admuser)currentUser.getPrincipal();
		String redisKey  = "catalog"+admuser.getId();
		Redis.hdel(redisKey);
		result.put("status", 200);
		return result;
	}
	/**删除目录
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
						result.put("pid", pid);
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
				boolean exsis = false;
				for(int i=0,size=products.size();i<size &&!exsis;i++) {
					exsis = products.get(i).getPid().equals(product.getPid());
				}
				if(!exsis) {
					products.add(product);
					w.setProductCount(products.size());
				}
				isadd = false;
			}
			wraps.add(w);
		}
		if(isadd) {
			addProduct(product, wraps);
		}
		return wraps;
	}
	/**
	 * 获取持久化的数据
	 */
	private String  catalogProduct(String id) {
		if(!StrUtils.isNum(id)) {
			return "";
		}
		
		RecommendCatalog catalogById = recommendCatalogService.catalogById(Integer.parseInt(id));
		if(catalogById == null) {
			return "";
		}
		String productList = catalogById.getProductList();
		if(StringUtils.isBlank(productList)) {
			return productList;
		}
		return "";
	}
}
