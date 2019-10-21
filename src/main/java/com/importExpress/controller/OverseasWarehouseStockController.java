package com.importExpress.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cbt.parse.service.StrUtils;
import com.google.common.collect.Maps;
import com.importExpress.pojo.OverseasWarehouseStock;
import com.importExpress.pojo.OverseasWarehouseStockParamter;
import com.importExpress.pojo.OverseasWarehouseStockWrap;
import com.importExpress.service.OverseasWarehouseStockService;

/**
 * 海外仓库存相关
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/owstock")
public class OverseasWarehouseStockController {
	@Autowired
	private OverseasWarehouseStockService stockService;
	
	/**
	 * 库存列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView stockList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("ows_stock");
		try {
			
			String strPage = request.getParameter("page");
			int page = StrUtils.isNum(strPage) ? Integer.parseInt(strPage) : 1;
			
			String goodsPid = request.getParameter("pid");
			goodsPid = StringUtils.isBlank(goodsPid) ? null : goodsPid;
			
			String skuid = request.getParameter("skuid");
			skuid = StringUtils.isBlank(skuid) ? null : skuid;
			
			OverseasWarehouseStockParamter param = OverseasWarehouseStockParamter.builder()
					.page((page - 1) * 20)
					.goodsPid(goodsPid)
					.skuid(skuid)
					.build();
			
			int stockListCount = stockService.getStockListCount(param);
			if(stockListCount > 0) {
				List<OverseasWarehouseStock> stockList = stockService.getStockList(param);
				mv.addObject("stockList", stockList);
			}
			String lastSyncStock = stockService.getLastSyncStock();
			mv.addObject("stockParamter", param);
			mv.addObject("lastSyncStock", lastSyncStock);
			mv.addObject("stockListCount", stockListCount);
			int stockListPage = stockListCount % 20 == 0 ? stockListCount / 20 : stockListCount / 20 + 1;
			mv.addObject("stockListPage", stockListPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
		
	}
	/**
	 * 记录表
	 * @return
	 */
	@RequestMapping(value = "/log")
	public ModelAndView stockLog(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("ows_stock_log");
		try {
			
			String strPage = request.getParameter("page");
			int page = StrUtils.isNum(strPage) ? Integer.parseInt(strPage) : 1;
			
			String goodsPid = request.getParameter("pid");
			goodsPid = StringUtils.isBlank(goodsPid) ? null : goodsPid;
			
			String skuid = request.getParameter("skuid");
			skuid = StringUtils.isBlank(skuid) ? null : skuid;
			
			String code = request.getParameter("code");
			code = StringUtils.isBlank(code) ? null : code;
			
			String owsid = request.getParameter("owsid");
			owsid = StrUtils.isNum(owsid) ? owsid : "0";
			
			OverseasWarehouseStockParamter param = OverseasWarehouseStockParamter.builder()
					.page((page - 1) * 20)
					.goodsPid(goodsPid)
					.skuid(skuid)
					.code(code)
					.owsid(Integer.parseInt(owsid))
					.build();
			
			int stockLogListCount = stockService.getStockLogListCount(param);
			if(stockLogListCount > 0) {
				List<OverseasWarehouseStockWrap> stockLogList = stockService.getStockLogList(param);
				mv.addObject("stockLogList", stockLogList);
			}
			mv.addObject("stockParamter", param);
			mv.addObject("stockLogListCount", stockLogListCount);
			int stockLogListPage = stockLogListCount % 20 == 0 ? stockLogListCount / 20 : stockLogListCount / 20 + 1;
			mv.addObject("stockLogListPage", stockLogListPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
		
	}
	
	/**
	 * 同步库存
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sync")
	@ResponseBody
	public Map<String,Object> syncStock(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result = Maps.newHashMap();
		
		
		return result;
		
	} 

}
