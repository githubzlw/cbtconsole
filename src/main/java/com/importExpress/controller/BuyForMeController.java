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
import com.google.gson.Gson;
import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.pojo.TransportMethod;
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
    /**申请单详情
     * @param request
     * @param response
     * @return
     */
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
    			String delivery_method = StrUtils.object2Str(order.get("delivery_method"));
    			order.put("delivery_method", delivery_method);
    		}
    		List<Map<String,String>> remark = buyForMeService.getRemark(orderNo);
    		mv.addObject("remark", remark);
    		mv.addObject("order", order);
    		
//    		Map<String, List<String>> transport = buyForMeService.getTransport();
//    		mv.addObject("transport", transport);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }
    
    /**获取交期方式
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/transport")
    @ResponseBody
    public Map<String,Object> transport(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
			
    		List<TransportMethod> transport = buyForMeService.getTransport();
    		mv.put("methodList", transport);
    		mv.put("state", transport.size() > 0 ? 200 : 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }

    /**录入、修改规格
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Map<String,Object> addDetailSku(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
        	String id = request.getParameter("id");
        	String delete = request.getParameter("delete");
        	int addOrderDetailsSku = 0;
        	if(StringUtils.isNotBlank(delete) && "1".equals(delete)) {
        		addOrderDetailsSku = buyForMeService.updateOrderDetailsSkuState(StringUtils.isNotBlank(id)?Integer.parseInt(id) : 0,-1);
        	}else {
        		String bfId = request.getParameter("bfid");
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
        		detailSku.setWeight(request.getParameter("weight"));
        		detailSku.setUnit(request.getParameter("unit"));
        		String skuid = "";
        		detailSku.setSkuid(skuid);
        		detailSku.setState(1);
        		addOrderDetailsSku = buyForMeService.addOrderDetailsSku(detailSku );
        	}
    		mv.put("state", addOrderDetailsSku > 0 ? 200 : 500);
        	mv.put("orderDetails", addOrderDetailsSku);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }
    /**无效规格
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/invalid")
    @ResponseBody
    public Map<String,Object> invalidDetailSku(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
			
    		String id = request.getParameter("id");
    		int updateOrderDetailsSkuState = 
    				buyForMeService.updateOrderDetailsSkuState(StringUtils.isNotBlank(id)?Integer.parseInt(id) : 0,0);
    		
    		mv.put("state", updateOrderDetailsSkuState > 0 ? 200 : 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }
    /**确认交期时间
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/time")
    @ResponseBody
    public Map<String,Object> deliveryTime(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
    		String orderNo = request.getParameter("orderNo");
    		String time = request.getParameter("time");
    		String feight = request.getParameter("feight");
    		String method = request.getParameter("method");
    		int update = buyForMeService.updateDeliveryTime(orderNo,time,feight,method);
    		mv.put("state", update > 0 ? 200 : 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }
    /**内部回复
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/remark")
    @ResponseBody
    public Map<String,Object> remark(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
    		String orderNo = request.getParameter("orderNo");
    		String remark = request.getParameter("remark");
    		int update = buyForMeService.insertRemark(orderNo,remark);
    		mv.put("state", update > 0 ? 200 : 500);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return mv;
    }
    
    /**修改重量
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/weight")
    @ResponseBody
    public Map<String,Object> weightDetailSku(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
    		String weight = request.getParameter("weight");
        	String sbfdid = request.getParameter("bfdid");
        	int bfdid = StrUtils.isNum(sbfdid) ? Integer.valueOf(sbfdid) : 0;
        	int updateOrderDetailsSkuState = 
        			buyForMeService.updateOrderDetailsSkuWeight(weight, bfdid);
        	
    		mv.put("state", updateOrderDetailsSkuState > 0 ? 200 : 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }
    /**回复客户备注
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/deremark")
    @ResponseBody
    public Map<String,Object> replayRemark(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
    		String remark = request.getParameter("remark");
    		String sbfdid = request.getParameter("bfdid");
    		int bfdid = StrUtils.isNum(sbfdid) ? Integer.valueOf(sbfdid) : 0;
    		int updateOrderDetailsSkuState = 
    				buyForMeService.updateOrdersDetailsRemark(bfdid, remark);
    		
    		mv.put("state", updateOrderDetailsSkuState > 0 ? 200 : 500);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return mv;
    }
    
    /**确认
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/finsh")
    @ResponseBody
    public Map<String,Object> finsh(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> mv = Maps.newHashMap();
    	try {
    		String bfId = request.getParameter("bfid");
        	bfId = StrUtils.isNum(bfId) ? bfId : "0";
        	int addOrderDetailsSku = buyForMeService.finshOrder(Integer.parseInt(bfId));
        	mv.put("state", addOrderDetailsSku > 0 ? 200 : 500);
        	List<String> lstValues = Lists.newArrayList();
        	if(addOrderDetailsSku > 0) {
        		String sql1 = "update buyforme_orderinfo set state=? where id=?";
        		lstValues.add("2");
        		lstValues.add(bfId);
        		SendMQ.sendMsgByRPC(new RunSqlModel(DBHelper.covertToSQL(sql1,lstValues)));
        	}
        	
        	
        	List<BFOrderDetailSku> orderDetailsSku = buyForMeService.getOrderDetailsSku(bfId);
        	String sql1 = " update buyforme_details_sku set sku=?,product_url=?,num=?,price=?,price_buy=?,price_buy_c=?,ship_feight=?,weight=?,unit=?  where id=?";
        	String sql2 = "insert into buyforme_details_sku(sku,product_url,num,price,price_buy,price_buy_c,ship_feight,weight,unit,id,bf_id,bf_details_id,num_iid,skuid,remark,state)" + 
        			"  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	lstValues.clear();
        	for(BFOrderDetailSku o : orderDetailsSku) {
        		lstValues.clear();
        		lstValues.add(o.getSku());
        		lstValues.add(o.getProductUrl());
        		lstValues.add(String.valueOf(o.getNum()));
        		lstValues.add(o.getPrice());
        		lstValues.add(o.getPriceBuy());
        		lstValues.add(o.getPriceBuyc());
        		lstValues.add(o.getShipFeight());
        		lstValues.add(o.getWeight());
        		lstValues.add(o.getUnit());
        		lstValues.add(String.valueOf(o.getId()));
        		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(DBHelper.covertToSQL(sql1,lstValues)));
        		if(Integer.parseInt(sendMsgByRPC) < 1) {
        			lstValues.add(String.valueOf(o.getBfId()));
        			lstValues.add(String.valueOf(o.getBfDetailsId()));
        			lstValues.add(o.getNumIid());
        			lstValues.add(o.getSkuid());
        			lstValues.add(o.getRemark());
        			lstValues.add(String.valueOf(o.getState()));
        			SendMQ.sendMsg(new RunSqlModel(DBHelper.covertToSQL(sql2,lstValues)));
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mv;
    }
    
   


}
