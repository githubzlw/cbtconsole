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

import com.cbt.util.StrUtils;
import com.google.common.collect.Maps;
import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.service.BuyForMeService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/bf")
public class BuyForMeController {

    @Autowired
    private BuyForMeService buyForMeService;


    @RequestMapping("/orders")
    @ResponseBody
    public ModelAndView lstOrders(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView mv = new ModelAndView("buyforme_mg");
        Map<String,Object> map = Maps.newHashMap();
        String strPage = request.getParameter("page");
        strPage = StrUtils.isNum(strPage) ? strPage : "1";
        
        String strState = request.getParameter("state");
        strState = StringUtils.isBlank(strState) ? "-2" : strState;
        
        String userId = request.getParameter("userid");
        String orderno = request.getParameter("orderno");
        
        int current_page = Integer.parseInt(strPage);
        map.put("current_page", current_page);
        map.put("page", (current_page - 1)*30);
        map.put("state", Integer.valueOf(strState));
        map.put("orderNo", orderno);
        map.put("userId", userId);
        
        
        int ordersCount = buyForMeService.getOrdersCount(map);
        if(ordersCount > 0) {
        	List<BFOrderInfo> orders = buyForMeService.getOrders(map);
        	mv.addObject("orders", orders);
        }
    	
       
        int totalPage = ordersCount % 30 == 0 ? ordersCount / 30 : ordersCount / 30 + 1;
        mv.addObject("listCount", ordersCount);
        mv.addObject("totalPage", totalPage);
        mv.addObject("queryParam", map);
        return mv;
    }
    @RequestMapping("/detail")
    @ResponseBody
    public ModelAndView orderDetail(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView mv = new ModelAndView("buyforme_detail");
    	String bfId = request.getParameter("bfid");
    	List<BFOrderDetail> orderDetails = buyForMeService.getOrderDetails(bfId);
    	mv.addObject("orderDetails", orderDetails);
    	return mv;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map<String,Object> addDetailSku(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	String bfId = request.getParameter("bfid");
    	String bfDetailsId = request.getParameter("bfdid");
    	String id = request.getParameter("id");
    	String num = request.getParameter("num");
    	
    	BFOrderDetailSku detailSku = new BFOrderDetailSku();
    	
    	detailSku.setBfDetailsId(Integer.parseInt(bfDetailsId));
    	detailSku.setBfId(Integer.parseInt(bfId));
    	detailSku.setId(StringUtils.isNotBlank(id)?Integer.parseInt(id) : 0);
    	detailSku.setNum(Integer.parseInt(num));
    	detailSku.setNumIid(request.getParameter("numiid"));
    	detailSku.setPrice(request.getParameter("price"));
    	detailSku.setProductUrl(request.getParameter("url"));
    	detailSku.setSku(request.getParameter("sku"));
    	detailSku.setSkuid(request.getParameter("num"));
    	detailSku.setState(0);
		int addOrderDetailsSku = buyForMeService.addOrderDetailsSku(detailSku );
    	
    	mv.put("orderDetails", addOrderDetailsSku);
    	return mv;
    }
    
   


}
