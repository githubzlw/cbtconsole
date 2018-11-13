package com.cbt.controller;

import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.PaymentDao;
import com.cbt.processes.dao.IOrderuserDao;
import com.cbt.processes.dao.OrderuserDao;
import com.cbt.processes.servlet.Currency;
import com.cbt.util.AppConfig;
import com.cbt.util.Md5Util;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/cbt/pay")
public class PayController {
	
	private static final Log LOG = LogFactory.getLog(PayController.class);
	
	@RequestMapping(value = "/topay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> topay(HttpServletRequest request, String order_no) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String[] userinfo = WebCookie.getUser(request);
			IOrderuserDao dao = new OrderuserDao();
			Map<String, Object> ctpoOrderInfo = dao.getCtpoOrderInfo(order_no,Integer.parseInt(userinfo[0]));
			double discount_amount = (Double) ctpoOrderInfo.get("discount_amount");
			double order_ac = (Double) ctpoOrderInfo.get("order_ac");
			String currency =  (String) ctpoOrderInfo.get("currency");//订单货币单位
			String service_fee_ =  (String) ctpoOrderInfo.get("service_fee");//订单货币单位
			float service_fee = Utility.getIsDouble(service_fee_) ? Float.parseFloat(service_fee_) : 0;
			float newProductCost = (Float) ctpoOrderInfo.get("productCost") + Float.parseFloat(ctpoOrderInfo.get("foreign_freight").toString())+service_fee;
			IOrderwsDao orderwsDao = new OrderwsDao();
			Object[] oldProductCost=orderwsDao.getOrdersPay(order_no);
			double newProductCost2 = Double.parseDouble(String.valueOf(newProductCost));
			BigDecimal b1 = new BigDecimal(newProductCost2);
	        BigDecimal b2 = new BigDecimal(Double.parseDouble(oldProductCost[0].toString()));
	        BigDecimal amount=b1.subtract(b2).subtract(new BigDecimal(discount_amount)).subtract(new BigDecimal(order_ac)).setScale(2,   BigDecimal.ROUND_HALF_UP);
	        
	        //获取用户余额
	        /*int userid = 0;
			if (userinfo != null) {
				userid = Integer.parseInt(userinfo[0]);
			}
			IUserServer userServer = new UserServer();
			String[] balance_ac = userServer.getBalance_currency(userid);
			double balance_ = 0;
			String currency_us = balance_ac[1];
			if(Utility.getStringIsNull(balance_ac[0])){
				balance_ = Double.parseDouble(balance_ac[0]);
				if(!currency_us.equals(currency)){
					
				}
			}*/
			IPaymentDao dao1 = new PaymentDao();
			/* if(balance_ >= amount.doubleValue()){
	        	//余额支付
				//添加payment表
				Payment pay1 = new Payment();
				pay1.setUserid(userid);// 添加用户id
				pay1.setOrderid(order_no);// 添加订单id
				pay1.setOrderdesc("余额支付");// 添加订单描述
				pay1.setPaystatus(1);// 添加付款状态
				pay1.setPaymentid("");// 添加付款流水号（paypal返回的）
				pay1.setPayment_amount(amount.floatValue());// 添加付款金额（paypal返回的）
				pay1.setPayment_cc(currency);// 添加付款币种（paypal返回的）
				pay1.setPaySID("");
				pay1.setPayflag("O");
				pay1.setPaytype("2");
				dao1.addPayment(pay1);
				//添加余额变更记录
    			RechargeRecord rr = new RechargeRecord();
    			rr.setUserid(userid);
    			rr.setPrice(amount.doubleValue());
    			rr.setRemark_id(order_no);
    			rr.setType(1);
    			rr.setRemark("余额抵扣订单："+order_no);
    			rr.setUsesign(1);
    			dao1.addRechargeRecord(rr);
    			//修改用户余额表
				userServer.upUserPrice(userid, -amount.doubleValue());
				map.put("code", "2");
				map.put("msg", "amount="+amount+"&orderid="+order_no);
				return map;
	        }*/
			
			String md1=AppConfig.paypal_business+userinfo[0]+order_no+amount;
	    	LOG.debug("-------md1:"+md1);
			String sign=Md5Util.encoder(md1);
			String paySID = UUID.randomUUID().toString();
			String custom=userinfo[0]+"@"+paySID;	
			dao1.insertintoPaylog(Integer.parseInt(userinfo[0]), order_no, paySID, amount.floatValue(),
					userinfo[1], "","1");// 付款时往paylog表中插入一条记录
			
			String formstr=Utility.getForm(userinfo[1], order_no, amount.toString(), sign, custom, "N","0@0@"+order_no, oldProductCost[1].toString());
			map.put("code", "0");
			map.put("msg", formstr);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			map.put("code", "1");
			map.put("msg", "Operation failure");
		}
		return map;
	}
	
	@RequestMapping(value = "/paysubmit", method = RequestMethod.POST)
	public String paysubmit(HttpServletRequest request, HttpServletResponse response,
                            String form, Map<String, Object> map) {
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo == null){
			String exist = WebCookie.cookie(request, "userName");
			if(exist == null){
				return "register";
			}
			return "geton";
		}
		map.put("form", form);
		return "paysubmit";
	}
	
	@RequestMapping(value = "/topayChange", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> topayChange(HttpServletRequest request, String order_no, String chaPriceSum, String remainingprice,
                                           String goodIdStr, String chaPriceStr, String changeUrlStr, String changeNameStr,
                                           String changeImgStr, String goodsCarIdStr, String goodsTypeStr, String gdIdStr, String aliCurrencyStr, String remarkStr) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String[] userinfo = WebCookie.getUser(request);
			IOrderuserDao dao = new OrderuserDao();
			
			//更新orderinfo表
			dao.updateOrderInfopr(order_no,Integer.parseInt(userinfo[0]),chaPriceSum,remainingprice);
			String[] goodIdAry = goodIdStr.split(",");
			String[] chaPriceAry = chaPriceStr.split(",");
			String[] chaUrlAry = changeUrlStr.split(",");
			String[] chaNameAry = changeNameStr.split(",");
			String[] chaImgAry = changeImgStr.split(",");
			String[] chaGoodsCarIdAry = goodsCarIdStr.split(",");
			String[] chaGoodsTypeAry = goodsTypeStr.split(";");
			String[] chaGdIdAry = gdIdStr.split(",");
			String[] aliCurAry = aliCurrencyStr.split(",");
			String[] remarkAry = remarkStr.split(",");
			
			
			
			for(int i=1;i<goodIdAry.length;i++){
				
				//更新changegooddata商品替换标识个人中心
				dao.updateChangeGood(order_no,goodIdAry[i]);
				//追加goods_car
				int goodsCarKey = dao.saveGoodsCar(chaUrlAry[i],chaNameAry[i],chaImgAry[i],chaGoodsTypeAry[i],Integer.parseInt(goodIdAry[i]),remarkAry[i]);
				//更新orderDetail
				dao.updateOrderDetail(order_no,Integer.parseInt(userinfo[0]), goodIdAry[i], chaPriceAry[i],chaNameAry[i],Integer.parseInt(chaGoodsCarIdAry[i]),goodsCarKey,remarkAry[i]
						,chaUrlAry[i],chaImgAry[i],chaGoodsTypeAry[i]);
				//获取汇率
				 Map<String, Double> maphl = Currency.getMaphl(request);
				 double exchange_rate = 1;
				 DecimalFormat df=new DecimalFormat("#0.##");
				 exchange_rate = maphl.get("RMB");
				 exchange_rate = exchange_rate/maphl.get(aliCurAry[i]);
				 
				//更新order_product_source商品替换标识
				dao.updateProductSource(chaGoodsCarIdAry[i],chaGdIdAry[i],chaUrlAry[i],chaImgAry[i],df.format(Double.parseDouble(chaPriceAry[i])*exchange_rate),chaNameAry[i],order_no,goodsCarKey);
			}
		
			if(!remainingprice.equals("0")){
				Map<String, Object> ctpoOrderInfo = dao.getCtpoOrderInfo(order_no,Integer.parseInt(userinfo[0]));
				double discount_amount = (Double) ctpoOrderInfo.get("discount_amount");
				double order_ac = (Double) ctpoOrderInfo.get("order_ac");
				String currency =  (String) ctpoOrderInfo.get("currency");//订单货币单位
				String service_fee_ =  (String) ctpoOrderInfo.get("service_fee");//订单货币单位
				float service_fee = Utility.getIsDouble(service_fee_) ? Float.parseFloat(service_fee_) : 0;
				float newProductCost = (Float) ctpoOrderInfo.get("productCost") + Float.parseFloat(ctpoOrderInfo.get("foreign_freight").toString())+service_fee;
				IOrderwsDao orderwsDao = new OrderwsDao();
				Object[] oldProductCost=orderwsDao.getOrdersPay(order_no);
				double newProductCost2 = Double.parseDouble(String.valueOf(newProductCost));
				BigDecimal b1 = new BigDecimal(newProductCost2);
		        BigDecimal b2 = new BigDecimal(Double.parseDouble(oldProductCost[0].toString()));
		        BigDecimal amount=b1.subtract(b2).subtract(new BigDecimal(discount_amount)).subtract(new BigDecimal(order_ac)).setScale(2,   BigDecimal.ROUND_HALF_UP);
		        
				IPaymentDao dao1 = new PaymentDao();
				
				String md1=AppConfig.paypal_business+userinfo[0]+order_no+amount;
		    	LOG.debug("-------md1:"+md1);
				String sign=Md5Util.encoder(md1);
				String paySID = UUID.randomUUID().toString();
				String custom=userinfo[0]+"@"+paySID;	
				dao1.insertintoPaylog(Integer.parseInt(userinfo[0]), order_no, paySID, amount.floatValue(),
						userinfo[1], "","1");// 付款时往paylog表中插入一条记录
				
				String formstr=Utility.getForm(userinfo[1], order_no, amount.toString(), sign, custom, "N","0@0@"+order_no, oldProductCost[1].toString());
				map.put("code", "0");
				map.put("msg", formstr);
			}else{
				map.put("code", "3");
				map.put("msg", "Success");
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			map.put("code", "1");
			map.put("msg", "Operation failure");
		}
		return map;
	}
}