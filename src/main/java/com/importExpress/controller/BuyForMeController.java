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

import com.cbt.jdbc.DBHelper;
import com.cbt.util.StrUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.service.BuyForMeService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

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
    	try {
			
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
    		map.put("userId", StringUtils.isBlank(userId) ? null : userId);
    		
    		
    		int ordersCount = buyForMeService.getOrdersCount(map);
    		if(ordersCount > 0) {
    			List<BFOrderInfo> orders = buyForMeService.getOrders(map);
    			mv.addObject("orders", orders);
    		}
    		
    		
    		int totalPage = ordersCount % 30 == 0 ? ordersCount / 30 : ordersCount / 30 + 1;
    		mv.addObject("listCount", ordersCount);
    		mv.addObject("totalPage", totalPage);
    		mv.addObject("queryParam", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return mv;
    }
    @RequestMapping("/detail")
    @ResponseBody
    public ModelAndView orderDetail(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView mv = new ModelAndView("buyforme_detail");
    	try {
			
    		String orderNo = request.getParameter("no");
    		String bfid = request.getParameter("bfid");
    		List<BFOrderDetail> orderDetails = buyForMeService.getOrderDetails(orderNo,bfid);
    		mv.addObject("orderDetails", orderDetails);
    		//订单状态：-1 取消，0申请，1处理中 2销售处理完成 3已支付
    		
    		Map<String, Object> order = buyForMeService.getOrder(orderNo);
    		if(order != null) {
    			int state = Integer.parseInt(StrUtils.object2NumStr(order.get("state")));
    			String strState = state == -1?"申请已取消":state==0?"申请待处理":state==1?"申请处理中":state==2?"销售处理完成":"已支付";
    			order.put("stateContent", strState);
    			
    		}
    		mv.addObject("order", order);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map<String,Object> addDetailSku(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	String bfId = request.getParameter("bfid");
    	String id = request.getParameter("id");
    	String delete = request.getParameter("delete");
    	int addOrderDetailsSku = 0;
    	if(StringUtils.isNotBlank(delete) && "1".equals(delete)) {
    		addOrderDetailsSku = buyForMeService.deleteOrderDetailsSku(StringUtils.isNotBlank(id)?Integer.parseInt(id) : 0);
    	}else {
    		String bfDetailsId = request.getParameter("bfdid");
    		String num = request.getParameter("num");
    		BFOrderDetailSku detailSku = new BFOrderDetailSku();
    		detailSku.setBfDetailsId(Integer.parseInt(bfDetailsId));
    		detailSku.setBfId(Integer.parseInt(bfId));
    		detailSku.setId(StringUtils.isNotBlank(id)?Integer.parseInt(id) : 0);
    		detailSku.setNum(Integer.parseInt(num));
    		detailSku.setNumIid(request.getParameter("numiid"));
    		detailSku.setPrice(request.getParameter("price"));
    		detailSku.setPriceBuy(request.getParameter("priceBuy"));
    		detailSku.setPriceBuyc(request.getParameter("priceBuyc"));
    		detailSku.setShipFeight(request.getParameter("shipFeight"));
    		detailSku.setProductUrl(request.getParameter("url"));
    		detailSku.setSku(request.getParameter("sku"));
    		String skuid = "";
    		detailSku.setSkuid(skuid);
    		detailSku.setState(0);
    		addOrderDetailsSku = buyForMeService.addOrderDetailsSku(detailSku );
    	}
		mv.put("state", addOrderDetailsSku > 0 ? 200 : 500);
    	mv.put("orderDetails", addOrderDetailsSku);
    	return mv;
    }
    @RequestMapping("/finsh")
    @ResponseBody
    public Map<String,Object> finsh(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	String bfId = request.getParameter("bfid");
    	bfId = StrUtils.isNum(bfId) ? bfId : "0";
    	int addOrderDetailsSku = buyForMeService.finshOrder(Integer.parseInt(bfId));
    	mv.put("state", addOrderDetailsSku > 0 ? 200 : 500);
    	
    	List<BFOrderDetailSku> orderDetailsSku = buyForMeService.getOrderDetailsSku(bfId);
    	String sql1 = " update buyforme_details_sku set sku=?,product_url=?,num=?,price=?  where id=?";
    	String sql2 = "insert into buyforme_details_sku(sku,product_url,num,price,id,bf_id,bf_details_id,num_iid,skuid,remark,state)" + 
    			"  values(?,?,?,?,?,?,?,?,?,?)";
    	List<String> lstValues = Lists.newArrayList();
    	for(BFOrderDetailSku o : orderDetailsSku) {
    		lstValues.clear();
    		lstValues.add(o.getSku());
    		lstValues.add(o.getProductUrl());
    		lstValues.add(String.valueOf(o.getNum()));
    		lstValues.add(o.getPrice());
    		lstValues.add(String.valueOf(o.getId()));
    		String covertToSQL = DBHelper.covertToSQL(sql1,lstValues);
    		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(covertToSQL));
    		if(Integer.parseInt(sendMsgByRPC) < 1) {
    			lstValues.add(String.valueOf(o.getBfId()));
    			lstValues.add(String.valueOf(o.getBfDetailsId()));
    			lstValues.add(o.getNumIid());
    			lstValues.add(o.getSkuid());
    			lstValues.add(o.getRemark());
    			lstValues.add(String.valueOf(o.getState()));
    			covertToSQL = DBHelper.covertToSQL(sql2,lstValues);
    			sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(covertToSQL));
    		}
    	}
    	return mv;
    }
    
   


}
