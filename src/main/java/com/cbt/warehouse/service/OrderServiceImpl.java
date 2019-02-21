package com.cbt.warehouse.service;

import com.cbt.bean.*;
import com.cbt.pojo.page.Page;
import com.cbt.userinfo.dao.UserMapper;
import com.cbt.warehouse.dao.WarehouseMapper;
import com.cbt.warehouse.dao.InquiryMapper;
import com.cbt.warehouse.dao.OrderMapper;
import com.cbt.warehouse.dao.SpiderMapper;
import com.cbt.warehouse.pojo.ClassDiscount;
import com.cbt.warehouse.pojo.Dropshiporder;
import com.cbt.warehouse.pojo.IdRelationTable;
import com.cbt.warehouse.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
	
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private InquiryMapper inquiryMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SpiderMapper spiderMapper;
	
	@Autowired
	private WarehouseMapper dao;
	@Override
	public List<OrderBean> getOrders(int userID, int state, int startpage,String orderNo,String timeFrom,String timeTo) {
		return orderMapper.getOrders(userID, state, (startpage - 1) * 8, 8,orderNo,timeFrom,timeTo);
	}

	@Override
	public List<OrderDetailsBean> getProductDetail(int userID, int state,
                                                   int startpage, String orderNo) {
		return orderMapper.getProductDetail(userID, state, (startpage - 1) * 8, 8, orderNo);
	}

	@Override
	public int getOrderdNumber(int userID, int state,String orderNo,String timeFrom,String timeTo) {
		return orderMapper.getCntByUseridAndState(userID, state,orderNo,timeFrom,timeTo);
	}

	@Override
	public List<Map<String,Object>> getNeedComfirmSourceGoods() {
		return orderMapper.getNeedComfirmSourceGoods();
	}
	
	@Override
	public int[] getOrdersIndividual(int userid) {
		List<Integer[]> ls = new ArrayList<Integer[]>();
		List<Map<String, Object>> listMap = orderMapper.getOrdersIndividual(userid);
		for (Map<String, Object> map : listMap) {
			Integer[] inte = {Integer.valueOf(String.valueOf(map.get("state"))), Integer.valueOf(String.valueOf(map.get("counts")))};
			ls.add(inte);
		}
		int waitPay = 0;//-1等待付款状态
		int ing = 0;//2确认价格
		int onck =0; //1已到仓库
		int cy =0; //3出运中
		int history =  0;//历史订单	
		int purchase =  0;//购买中	
		int xj = 0;//询价结果数量
		for (int i = 0; i < ls.size(); i++) {
			 if(ls.get(i)[0] == 0){
				 waitPay = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 5){
				 ing = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 3){
				 cy = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 4){
				 history = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 1){
				 purchase = ls.get(i)[1];
			 }
		}
		onck =  orderMapper.warehouse(userid);//3已到仓库
		xj = inquiryMapper.getInquiryNumber(userid);//询价结果数量
		//等待付款状态,确认价格,购买中,已到仓库,出运中,历史订单,询价结果数量
		int[] oi = {waitPay,ing,purchase,onck,cy,history,xj};
		return oi;
	}

	@Override
	public Map<String, List<String>> getConfirmThePriceOf(int userId) {
		Map<String, List<String>> confirmThePriceOf = new HashMap<String, List<String>>();
		List<String> cList=new ArrayList<String>();
		List<String> aList=new ArrayList<String>();
		
		List<Map<String, Object>> list = orderMapper.getConfirmThePriceOf(userId);
		for (Map<String, Object> map : list) {
			int state = Integer.valueOf(String.valueOf(map.get("state")));
			String string = (String)map.get("order_no");
			if(state == 5){
				cList.add(string);
			}else{
				aList.add(string);
			}
		}
		confirmThePriceOf.put("advance", aList);
		confirmThePriceOf.put("ver", cList);
		
		return confirmThePriceOf;
	}

	//此处修改请同步修改DropshiporderServiceImpl类中的getDropShipOrderInfo方法.
	@Override
	public Map<String, Object> getCtpoOrderInfo(String orderNo, int userId) {
		Map<String, Object> ctpoOrderInfo=new HashMap<String,Object>();
		List<CtpoOrderBean> ctpoList=new ArrayList<CtpoOrderBean>();
		List<Map<String, Object>> listMap = orderMapper.getCtpoOrderInfo(orderNo,userId);
		double productCost=0;//需要支付的钱
		String jiaohuoriqi="";
		String foreign_freight = "0";
		double discount_amount = 0;
		double share_discount = 0;
		String currency = "USD";
		double order_ac = 0;
		String service_fee = "0";
		double cashback = 0,extra_freight=0;
		for (Map<String, Object> map : listMap) {
			discount_amount = (Double)map.get("discount_amount");
			share_discount = (Double)map.get("share_discount");
			order_ac = (Double)map.get("order_ac");
			int goodId = (Integer)map.get("goodsid");
			CtpoOrderBean ctpoOrderBean=new CtpoOrderBean();
			ctpoOrderBean.setState(String.valueOf(map.get("state")));
			ctpoOrderBean.setId((Integer)map.get("orderid"));
			ctpoOrderBean.setOrderNo(String.valueOf(map.get("order_no")));
			ctpoOrderBean.setGoodId(goodId);
			ctpoOrderBean.setGoodsName((String)map.get("goodsname"));
			ctpoOrderBean.setGoodsUrl((String)map.get("car_url"));
			ctpoOrderBean.setGoodsImg((String)map.get("car_img"));
			ctpoOrderBean.setGoodsRemark((String)map.get("remark"));
			ctpoOrderBean.setPurchase_state((Integer)map.get("purchase_state"));
			currency = (String)map.get("currency");
			ctpoOrderBean.setCurrency(currency);
			ctpoOrderBean.setImg_type((String)map.get("goods_typeimg"));
			ctpoOrderBean.setDiscount_amount((Double)map.get("discount_amount"));
			ctpoOrderBean.setShare_discount((Double)map.get("share_discount"));
			ctpoOrderBean.setTypes((String)map.get("car_type"));
			String freight = (String)map.get("goodsfreight");
			float goodsfreight=0;
			if(Utility.getStringIsNull(freight)){
				goodsfreight=Float.parseFloat(freight);
			}
			String ff = (String)map.get("foreign_freight");
			if(Utility.getStringIsNull(ff)){
				foreign_freight = ff;
			}
			cashback = (Double)map.get("cashback");
			extra_freight = (Double)map.get("extra_freight");
			String service_f = (String)map.get("service_fee");
			if(Utility.getStringIsNull(service_f)){
				service_fee = service_f;
			}
			ctpoOrderBean.setGoodsfreight(freight);
			float goodsPrice=0;
			String goodsPriceStr=(String)map.get("goodsprice");
			if(Utility.getStringIsNull(goodsPriceStr)){
				goodsPrice=Float.parseFloat(goodsPriceStr);
			}
			ctpoOrderBean.setGoodsPrice(goodsPriceStr);
			/*String productCostStr = (String)map.get("oi.product_cost");
			if(Utility.getStringIsNull(productCostStr)){
				totalProductCost=Float.parseFloat(productCostStr);
			}*/
			ctpoOrderBean.setDeliveryTime((String)map.get("delivery_time"));
			int yourorder = (Integer)map.get("yourorder");
			ctpoOrderBean.setYourOrder(yourorder);
			List<String> type1List=new ArrayList<String>();
			List<String> type2List=new ArrayList<String>();
			List<String> type3List=new ArrayList<String>();
			List<String> type5List=new ArrayList<String>();
			List<String> type7List=new ArrayList<String>();
			boolean isCancel=false;
//			float danj=goodsPrice;
			List<Map<String, Object>> changes = orderMapper.getOrderChanges(orderNo, goodId);
			for (Map<String, Object> change : changes) {
				int ropType = (Integer)change.get("ropType");
				//begin jxw 2017-3-24 添加问题类型为4的标识
				if(ropType == 4){
					ctpoOrderBean.setRopType(ropType);
				}
				//end
				String oldValue=(String)change.get("oldValue");
				String newValue=(String)change.get("newValue");
				switch (ropType) {
				case 1://价格变动
					type1List.add(oldValue);
					type1List.add(newValue);
					/*if(Utility.getStringIsNull(newValue)){
						Float danj=Float.parseFloat(newValue);
						goodsPrice=danj;
//						ctpoOrderBean.setGoodsPrice(danj+"");
					}*/
					ctpoOrderBean.setChange1(type1List);
					break;
				case 2://交期变动
//					if(!Utility.getStringIsNull(oldValue)){
//						oldValue=Utility.getDateFormatYMD().format(new Date());
//					}else if(oldValue.trim().equals("0")){
//						oldValue=Utility.getDateFormatYMD().format(new Date());
//					}
					type2List.add(oldValue);
					type2List.add(newValue);
					if (Utility.getStringIsNull(newValue)) {
						jiaohuoriqi = newValue;
					}
					ctpoOrderBean.setChange2(type2List);
					break;
				case 3://最小定量
					type3List.add(oldValue);
					type3List.add(newValue);
					yourorder=Integer.parseInt(newValue);
					ctpoOrderBean.setChange3(type3List);
					break;
				case 4://取消商品
					ctpoOrderBean.setChange4(newValue);
					isCancel=true;
					break;
				case 5://交流信息
					SimpleDateFormat format = new SimpleDateFormat("hhaa,MM-dd ",Locale.ENGLISH);  
					String dateline = format.format((Date)change.get("dateline"));
					if(oldValue.equals("1")){
						type5List.add(dateline+"  Supplier:"+newValue);
					}else{
						type5List.add(dateline+"  Customer:"+newValue);
					}
					break;
				case 7://国内运费
					type7List.add(oldValue);
					type7List.add(newValue);
					ctpoOrderBean.setChange7(type7List);
					break;
				default:
					break;
				}
			}
			ctpoOrderBean.setChange5(type5List);
			if(!isCancel){
//				ctpoOrderBean.setYourOrder(yourorder);
//				ctpoOrderBean.setGoodsPrice(goodsPrice+"");
				productCost=productCost+(goodsPrice*yourorder+goodsfreight);
			}
			ctpoOrderBean.setCancel(isCancel);
			ctpoList.add(ctpoOrderBean);
		}
		ctpoOrderInfo.put("currency", currency);
		ctpoOrderInfo.put("ctpoList", ctpoList);
		ctpoOrderInfo.put("productCost", productCost);
		ctpoOrderInfo.put("discount_amount", discount_amount);
		ctpoOrderInfo.put("share_discount", share_discount);
		ctpoOrderInfo.put("order_ac", order_ac);
		ctpoOrderInfo.put("foreign_freight", foreign_freight);
		ctpoOrderInfo.put("service_fee", service_fee);
		ctpoOrderInfo.put("cashback", cashback);
		ctpoOrderInfo.put("extra_freight", extra_freight);
		if(jiaohuoriqi!=null){
			ctpoOrderInfo.put("jiaoqiTime", jiaohuoriqi);
		}else{
			ctpoOrderInfo.put("jiaoqiTime", "");
		}
		Map<String, Object> orderinfoMap = dao.getOrderinfoByOrderNo(orderNo);//获取当前订单的信息
		//查询该订单支付总金额
		ctpoOrderInfo.put("totalProductCost", orderinfoMap.get("pay_price"));
		//查询该订单Cash Back金额
		ctpoOrderInfo.put("cashback",orderinfoMap.get("cashback"));
		//查询该订单extra_discount金额
		ctpoOrderInfo.put("extra_discount", orderinfoMap.get("extra_discount"));
		//查询该订单优惠券折扣金额
		ctpoOrderInfo.put("coupon_discount",orderinfoMap.get("coupon_discount"));
		return ctpoOrderInfo;
	}

	@Override
	public String savectpoOrderZiXun(int goodId, String orderNo, String zixun) {
		orderMapper.saveOrderZiXun(orderNo, goodId, 5, "0", zixun, 1);
		
		int affectedRowCount = orderMapper.updateClientUpdateByOrderNo(orderNo);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			affectedRowCount = 0;
		}
		return "{\"result\":"+affectedRowCount+"}";
	}

	@Override
	public Integer[] deleteCtpoOrderGoods(String orderNo, int goodId, int userid,
                                          int purchase_state, List<ClassDiscount> cdList, HttpServletResponse response) {
		int res = orderMapper.getDelOrder(orderNo);//根据订单号获取当前订单有多少个商品未取消
		
		orderMapper.updateOrderChange2State(orderNo, goodId, null);//更新order_change
		
		Map<String, Object> orderinfoMap = orderMapper.getOrderinfoByOrderNo(orderNo);//获取当前订单的信息
		String remaining = String.valueOf(orderinfoMap.get("remaining_price"));
		orderinfoMap.put("remaining_price", Utility.getStringIsNull(remaining) ? remaining : "0");
		
		List<OrderDetailsBean> orderDetails = orderMapper.getDelOrderByOrderNoAndStatus(orderNo, goodId);//获取订单改商品的订单详情
		int deleteCtpoOrderGoods = orderMapper.updateOrderDetails2StateByGoodId(orderNo, goodId);//修改订单详情状态=2
		if (deleteCtpoOrderGoods < 0) {
			deleteCtpoOrderGoods = 0;
		}
		//查询该订单中的订单详情是否还存在数据
		//Map<String, Object> odblist=orderMapper.getDelOrderByOrderNoAndStatus(orderNo,goodId, 2);
		//List<OrderDetailsBean> orderDetailsBeans = (List<OrderDetailsBean>) orderinfoMap.get("odb");
		double discount_amount = Double.parseDouble(orderinfoMap.get("discount_amount").toString());//订单类别折扣金额
		double share_discount = Double.parseDouble(orderinfoMap.get("share_discount").toString());//社交分享折扣
		double coupon_discount = Double.parseDouble(orderinfoMap.get("coupon_discount").toString());//返单优惠
		String currency = orderinfoMap.get("currency").toString();
		double order_ac = Double.parseDouble(orderinfoMap.get("order_ac").toString());
		double product_cost = Double.parseDouble(orderinfoMap.get("product_cost").toString());
		double cashback = Double.parseDouble(orderinfoMap.get("cashback").toString());
		double payprice = Double.parseDouble(orderinfoMap.get("pay_price").toString());
		//获取用户货币单位
//		String currency_u = userMapper.getCurrencyById(userid);
		 double userAccount =Double.parseDouble(String.valueOf(dao.getBalance_currency(userid).get("available_m")));
		double exchange_rate = 1;
		String pay_price = "";
		if(orderinfoMap != null && deleteCtpoOrderGoods == 1){
			for (int i = 0; i < orderDetails.size(); i++) {
				OrderDetailsBean odb = orderDetails.get(i);
				if(odb.getGoodsid() == goodId){
					int yourorder=odb.getYourorder();
					String goodsprice=odb.getGoodsprice();
					BigDecimal b=new BigDecimal(goodsprice);
		    		double mc_gross=b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					double available=mc_gross*yourorder;//该商品的总金额=(数量*单价)-该商品的混批折扣金额
					double discount_pro = 0;
					double  newShareDiscount =0;
					double  newCouponDiscount =0;
					//混批折扣按比例减少
					if(discount_amount != 0 && odb.getDiscount_ratio() != 0){
						discount_pro = new BigDecimal((1-odb.getDiscount_ratio()) * available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					//返单优惠
					if(coupon_discount>0){
						newCouponDiscount = new BigDecimal(available/product_cost*coupon_discount).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					//社交分享折扣相应减少
					if(share_discount>0){
						newShareDiscount = new BigDecimal(available * 0.03 ).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
						if(newShareDiscount>share_discount){
							newShareDiscount = share_discount;
						}
					}
					available = new BigDecimal(available - discount_pro-newShareDiscount-newCouponDiscount).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					discount_amount = discount_amount - discount_pro;
					share_discount = share_discount - newShareDiscount;
					share_discount = share_discount>0?share_discount:0;
					product_cost = new BigDecimal(product_cost - available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					/*if(odb.getGoods_class() != 0){
						up_discount = 1;
					}*/
					
					
					//订单所欠费用小于商品费用->删除的商品放入余额（商品费用-订单所欠费用），订单所欠费用大于商品费用->修改订单表的订单所欠费用(订单所欠费用-商品费用),订单所欠费用为0->删除的商品放入余额(商品费用)
					String remaining_price =  (String) orderinfoMap.get("remaining_price");
					double r_price=0;//订单所欠费用
					if(Utility.getIsDouble(remaining_price)){
						BigDecimal r=new BigDecimal(remaining_price);
			    		r_price=r.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					if(product_cost < 200 && cashback != 0){
						cashback = 0;
						r_price = r_price + 10;
					}
					double available_r = 0;
		    		if(r_price > available ){
		    			//订单所欠费用大于商品费用->修改订单表的订单所欠费用(订单所欠费用-商品费用)
		    			available_r = new BigDecimal(r_price - available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
			    		pay_price = new BigDecimal(payprice).setScale(2,   BigDecimal.ROUND_HALF_UP).toString();
			    		dao.updateOrderPayPrice1(userid, orderNo, pay_price, null,discount_amount,share_discount,product_cost,available_r,cashback);
		    		}else{
		    			//订单所欠费用小于商品费用->删除的商品放入余额（商品费用-订单所欠费用）
		    			available_r = new BigDecimal(available - r_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
		    			if(res == 1){
		    				available_r = payprice;
		    				order_ac = new BigDecimal(order_ac*exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		    			}else{
		    				order_ac = 0;
		    			}
		    			double available_ru = new BigDecimal(available_r*exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			    		pay_price = new BigDecimal(payprice - available_r).setScale(2,   BigDecimal.ROUND_HALF_UP).toString();
		    			//将删除的商品钱放入余额
			    		if(Double.parseDouble(pay_price) >= 0){ 
			    			//添加充值记录
			    			RechargeRecord rr = new RechargeRecord();
			    			rr.setUserid(userid);
			    			rr.setPrice(available_ru);
			    			rr.setRemark_id(orderNo);
			    			rr.setType(1);
			    			rr.setBalanceAfter(userAccount+available_ru);
			    			rr.setRemark("cancel:"+orderNo+",goodsid:"+goodId);
			    			rr.setCurrency(currency);
			    			dao.addRechargeRecord(rr);
			    			dao.upUserPrice(userid, available_ru, order_ac);
				    		dao.updateOrderPayPrice1(userid, orderNo, pay_price, null, discount_amount,share_discount,product_cost, 0.0,cashback);
				    		//社交分享--同步更新分享者优惠折扣
				    		try {
				    			dao.updateAcceptPriceByOrderNo(orderNo,pay_price);
							} catch (Exception e) {
								// TODO: handle exception
							}
				    		
			    		}
		    		}
				}
			}
			
		}
		if(res == 1 && deleteCtpoOrderGoods == 1){
			//将订单状态改为已删除状态
			dao.upOrderPurchase(purchase_state, orderNo, 6);
			//删除对应消息
			dao.delMessageByOrderid(orderNo, "Order #");	
			//更新社交分享 accept_share 状态
			try {
				int ret = dao.updateAcceptShareByOrderNo(orderNo);
				if(ret>0){
					 //清除cookie 中的 shareID
					 Cookie cookie = new Cookie("shareID",null);
					 cookie.setMaxAge(0);
					 cookie.setPath("/");
					 response.addCookie(cookie);
				}
			} catch (Exception e) {
			}
			return new Integer[]{deleteCtpoOrderGoods,6};
		}else{
			dao.upOrderPurchase(purchase_state, orderNo, 5);
			Integer orderstate =  dao.checkUpOrderState(orderNo);
			return new Integer[]{deleteCtpoOrderGoods,orderstate};
//			return new Integer[]{deleteCtpoOrderGoods,5};
		}
	}

	@Override
	public int updatePriceReductionOffer(int userId, int goodsDataId,
			int goodsCarId) {
		orderMapper.updatePreshoppingcarInfo(userId, goodsDataId, goodsCarId);
		
		int affectedRowCount = orderMapper.updateGoodsCar(userId, goodsDataId, goodsCarId);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			return 0;
		}
		return affectedRowCount;
	}

	@Override
	public int updateGoosCar(int userId) {
		int affectedRowCount = orderMapper.updateGoodsCarByUserId(userId);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			return 0;
		}
		return affectedRowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateOrderInfo(int userId, String value, int type,
			int goodsid, String orderNo) {
		BigDecimal updatedtotalprice = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
		int updateOrderInfo = 0;
		boolean isUpdateUserAvailable = false;
		Double amount_u = null;
		String currency_u = null;
		String newProductCost = null;
		Double discount_amount = null;
		Double amount = null;
		Double service_fee = null;
		Double cashback = null;
		Double extra_freight = null;
		if(goodsid == 0){
			Map<String, Object> ctpoOrderInfo = getCtpoOrderInfo(orderNo,userId);
			List<CtpoOrderBean> ctpoList=(List<CtpoOrderBean>)ctpoOrderInfo.get("ctpoList");
			String foreign_freight = (String) ctpoOrderInfo.get("foreign_freight");
			discount_amount = Double.valueOf(ctpoOrderInfo.get("discount_amount").toString());//混批优惠
			cashback = Double.valueOf(ctpoOrderInfo.get("cashback").toString());//满减
			service_fee = Double.valueOf(ctpoOrderInfo.get("service_fee").toString());//服务费
			extra_freight = Double.valueOf(ctpoOrderInfo.get("extra_freight").toString());//升级运费
			double order_ac = Double.valueOf(ctpoOrderInfo.get("order_ac").toString());//运费抵扣金额
			
			int up_discount = 0;
			if(ctpoList !=null && ctpoList.size()>0){
				for(CtpoOrderBean ctpoOrderBean : ctpoList){
					int yourOrder = ctpoOrderBean.getYourOrder();
					double price = Double.parseDouble(ctpoOrderBean.getGoodsPrice());
					List<String> changetotalpricelist=ctpoOrderBean.getChange1();
					if(changetotalpricelist != null && changetotalpricelist.size()>0){
						 up_discount = 1;
						 price = Double.parseDouble(changetotalpricelist.get(1));
					}
					List<String> changetotalpricelist1=ctpoOrderBean.getChange3();
					if(changetotalpricelist1 != null && changetotalpricelist1.size()>0){
						 up_discount = 1;
						 yourOrder = Integer.parseInt(changetotalpricelist1.get(1));
					}
					BigDecimal b = new BigDecimal(yourOrder*price).setScale(2, BigDecimal.ROUND_HALF_UP);
					updatedtotalprice=updatedtotalprice.add(b);
				}
			}
			/*BigDecimal bproductCost=new BigDecimal(ctpoOrderInfo.get("productCost").toString()).setScale(2,   BigDecimal.ROUND_HALF_UP);
			BigDecimal updatedtotalprice=bproductCost.add(b1);	*/
			newProductCost=updatedtotalprice.toString();
			if(up_discount == 1){
				Map<String, Object> orderDiscountMap = orderMapper.getOrderDiscount(orderNo);
				if(orderDiscountMap != null){
					Double price = (Double)orderDiscountMap.get("price");
					if (price != null) {
						discount_amount = price;
						orderMapper.upOrderDiscount((Integer)orderDiscountMap.get("id"));
					}
				}
			}
			//支付金额=商品总金额+运费-折扣金额-运费抵扣+服务费service_fee+额外运费extra_freight-减免cashback
			updatedtotalprice = updatedtotalprice.add(new BigDecimal(Double.valueOf(foreign_freight)+extra_freight+service_fee).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(new BigDecimal(discount_amount+order_ac+cashback).setScale(2, BigDecimal.ROUND_HALF_UP));
			logger.debug("--------------newProductCost:"+newProductCost);
			Map<String, Object> orderinfoMap = dao.getOrderinfoByOrderNo(orderNo);//获取当前订单的信息
			//获取用户总付款金额
			double oldProductCost= Utility.getStringIsNull(orderinfoMap.get("pay_price").toString())?Double.parseDouble(orderinfoMap.get("pay_price").toString()):0;
			logger.debug("--------------oldProductCost:"+oldProductCost);
			BigDecimal boldProductCost=new BigDecimal(oldProductCost);
			
	        amount=updatedtotalprice.subtract(boldProductCost).setScale(2, BigDecimal.ROUND_HALF_UP).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
	        logger.debug("--------------remaining_price:"+amount);
	        if(amount<0){
				amount_u = amount;
				
				isUpdateUserAvailable = true;
				logger.debug("--------------退款到余额---------");
	        }else{
	        	logger.debug("--------------更新订单所剩余额---------");
	        }
	        	        
	        if (isUpdateUserAvailable) {
	        	orderMapper.updateUserAvailableM(amount_u, userId);
	        	orderMapper.insertRechargeRecord(userId, (-amount_u), 2, "确认价格中商品金额支付多出，金额变更", orderNo, 0, currency_u);
	        	orderMapper.updateOrderinfo2PayPrice(updatedtotalprice, newProductCost, discount_amount, userId, orderNo);
	        } else {
	        	orderMapper.updateOrderinfo2RemainingPrice(amount, newProductCost, discount_amount, userId, orderNo);
	        }
	        orderMapper.updateOrderDetailsChange(userId, orderNo);
	        
	        List<Integer> ropTypes = new ArrayList<Integer>();
	        ropTypes.add(1);
	        ropTypes.add(3);
	        updateOrderInfo = orderMapper.updateOrderChange2StateByRopTypeIn(orderNo, ropTypes);
			if (updateOrderInfo < 0) {
				//throw new DataAccessException();
				updateOrderInfo = 0;
			}
		} else {
			// 1价格 2交期3定量4取消5咨询6货源(内部不给客户看)
			if(type == 0){
				logger.debug("--------------value:"+value);
				//价格
				Map<String, Object> ctpoOrderInfo = getCtpoOrderInfo(orderNo,userId);
				List<CtpoOrderBean> ctpoList=(List<CtpoOrderBean>)ctpoOrderInfo.get("ctpoList");
				String foreign_freight = (String) ctpoOrderInfo.get("foreign_freight");
				discount_amount = (Double) ctpoOrderInfo.get("discount_amount");
				cashback = (Double) ctpoOrderInfo.get("cashback");//满减
				service_fee = (Double) ctpoOrderInfo.get("service_fee");//服务费
				extra_freight = (Double) ctpoOrderInfo.get("extra_freight");//升级运费
								
				int up_discount = 0;
				if(ctpoList !=null && ctpoList.size()>0){
					for(CtpoOrderBean ctpoOrderBean : ctpoList){
						int yourOrder=ctpoOrderBean.getYourOrder();
						List<String> changetotalpricelist=ctpoOrderBean.getChange1();
						if(!Utility.getStringIsNull(ctpoOrderBean.getChange3())){
							up_discount = 1;
						}
						if(changetotalpricelist != null && changetotalpricelist.size()>0){
							 BigDecimal b2 = new BigDecimal(Double.parseDouble(changetotalpricelist.get(1))*yourOrder).setScale(2, BigDecimal.ROUND_HALF_UP);
							 //BigDecimal b3 = new BigDecimal(Double.parseDouble(changetotalpricelist.get(0))*yourOrder).setScale(2, BigDecimal.ROUND_HALF_UP);
							 updatedtotalprice=updatedtotalprice.add(b2);
						}else{
							 BigDecimal b = new BigDecimal(Double.parseDouble(ctpoOrderBean.getGoodsPrice())*ctpoOrderBean.getYourOrder()).setScale(2, BigDecimal.ROUND_HALF_UP);
							 updatedtotalprice=updatedtotalprice.add(b);
						}
					}
				}
				/*BigDecimal bproductCost=new BigDecimal(ctpoOrderInfo.get("productCost").toString()).setScale(2,   BigDecimal.ROUND_HALF_UP);
				BigDecimal updatedtotalprice=bproductCost.add(b1);	*/
				newProductCost=updatedtotalprice.toString();
				if(up_discount == 1){
					Map<String, Object> orderDiscountMap = orderMapper.getOrderDiscount(orderNo);
					Double price = (Double)orderDiscountMap.get("price");
					if (price != null) {
						discount_amount = price;
						orderMapper.upOrderDiscount((Integer)orderDiscountMap.get("id"));
					}
				}
				updatedtotalprice = updatedtotalprice.add(new BigDecimal(foreign_freight+extra_freight+service_fee).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(new BigDecimal(discount_amount+cashback).setScale(2, BigDecimal.ROUND_HALF_UP));
//				updatedtotalprice = updatedtotalprice.add(new BigDecimal(foreign_freight).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(new BigDecimal(discount_amount).setScale(2, BigDecimal.ROUND_HALF_UP));
				logger.debug("--------------newProductCost:"+newProductCost);
				Map<String, Object> orderinfoMap = dao.getOrderinfoByOrderNo(orderNo);//获取当前订单的信息
				//获取用户总付款金额
				double oldProductCost= Utility.getStringIsNull(orderinfoMap.get("pay_price").toString())?Double.parseDouble(orderinfoMap.get("pay_price").toString()):0;
				logger.debug("--------------oldProductCost:"+oldProductCost);
				BigDecimal boldProductCost=new BigDecimal(oldProductCost);
				
		        amount=updatedtotalprice.subtract(boldProductCost).setScale(2, BigDecimal.ROUND_HALF_UP).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
		        logger.debug("--------------remaining_price:"+amount);
		        if(amount<0){
					amount_u = amount;
					isUpdateUserAvailable = true;
					logger.debug("--------------退款到余额---------");
		        }else{
		        	logger.debug("--------------更新订单所剩余额---------");
		        }
			} else if (type == 2) {
				//最小订量
				Map<String, Object> ctpoOrderInfo = getCtpoOrderInfo(orderNo,userId);
				cashback = (Double) ctpoOrderInfo.get("cashback");//满减
				service_fee = (Double) ctpoOrderInfo.get("service_fee");//服务费
				extra_freight = (Double) ctpoOrderInfo.get("extra_freight");//升级运费
				List<CtpoOrderBean> ctpoList=(List<CtpoOrderBean>)ctpoOrderInfo.get("ctpoList");
				String foreign_freight = (String) ctpoOrderInfo.get("foreign_freight");
				discount_amount = (Double) ctpoOrderInfo.get("discount_amount");
				int up_discount = 0;
				if(ctpoList !=null && ctpoList.size()>0){
					for(CtpoOrderBean ctpoOrderBean : ctpoList){
						String price=ctpoOrderBean.getGoodsPrice();
						List<String> changetotalpricelist=ctpoOrderBean.getChange3();
						if(!Utility.getStringIsNull(ctpoOrderBean.getChange1())){
							up_discount = 1;
						}
						if(changetotalpricelist != null && changetotalpricelist.size()>0){
							 BigDecimal b2 = new BigDecimal(Integer.parseInt(changetotalpricelist.get(1))*Double.parseDouble(price)).setScale(2, BigDecimal.ROUND_HALF_UP);
							 //BigDecimal b3 = new BigDecimal(Double.parseDouble(changetotalpricelist.get(0))*yourOrder).setScale(2, BigDecimal.ROUND_HALF_UP);
							 updatedtotalprice=updatedtotalprice.add(b2);
						}else{
							 BigDecimal b = new BigDecimal(ctpoOrderBean.getYourOrder()*Double.parseDouble(price)).setScale(2, BigDecimal.ROUND_HALF_UP);
							 updatedtotalprice=updatedtotalprice.add(b);
						}					         
					}
				}
				/*BigDecimal bproductCost=new BigDecimal(ctpoOrderInfo.get("productCost").toString()).setScale(2,   BigDecimal.ROUND_HALF_UP);
				BigDecimal updatedtotalprice=bproductCost.add(b1);	*/
				newProductCost=updatedtotalprice.toString();
				updatedtotalprice = updatedtotalprice.add(new BigDecimal(foreign_freight+extra_freight+service_fee).setScale(2, BigDecimal.ROUND_HALF_UP));
				logger.debug("--------------newProductCost:"+newProductCost);
//				Object[] oldProductCost1 =orderwsService.getOrdersPay(orderNo);//获取用户总付款金额
				Map<String, Object> orderinfoMap = dao.getOrderinfoByOrderNo(orderNo);//获取当前订单的信息
				double oldProductCost= Utility.getStringIsNull(orderinfoMap.get("pay_price").toString())?Double.parseDouble(orderinfoMap.get("pay_price").toString()):0;
				logger.debug("--------------oldProductCost:"+oldProductCost);
				BigDecimal boldProductCost=new BigDecimal(oldProductCost);
				if(up_discount == 1){
					Map<String, Object> orderDiscountMap = orderMapper.getOrderDiscount(orderNo);
					Double price = (Double)orderDiscountMap.get("price");
					if (price != null) {
						discount_amount = price;
						orderMapper.upOrderDiscount((Integer)orderDiscountMap.get("id"));
					}
				}
		        amount=updatedtotalprice.subtract(boldProductCost).subtract(new BigDecimal(discount_amount+cashback)).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
		        logger.debug("--------------remaining_price:"+amount);
		        if(amount<0){
					//获取用户货币单位
					amount_u = amount;
					isUpdateUserAvailable = true;
					logger.debug("--------------退款到余额---------");
		        }else{
		        	logger.debug("--------------更新订单所剩余额---------");
		        }
			}
			
			if (type == 0) {
				if (isUpdateUserAvailable) {
		        	orderMapper.updateUserAvailableM(amount_u, userId);
		        	orderMapper.insertRechargeRecord(userId, (-amount_u), 2, "确认价格中商品金额支付多出，金额变更", orderNo, 0, currency_u);
		        	orderMapper.updateOrderinfo2PayPrice(updatedtotalprice, newProductCost, discount_amount, userId, orderNo);
		        } else {
		        	orderMapper.updateOrderinfo2RemainingPrice(amount, newProductCost, discount_amount, userId, orderNo);
		        }
								
				updateOrderInfo = orderMapper.updateOrderChange2State(orderNo, goodsid, 1);
				if (updateOrderInfo < 0) {
					//throw new DataAccessException();
					updateOrderInfo = 0;
				}
			} else if (type == 1) {
				
				orderMapper.updateOrderDetails2DeliveryTime(value,userId,goodsid,orderNo);
				
				//orderMapper.updateOrderinfo2DeliveryTime(value, orderNo);
				
				updateOrderInfo = orderMapper.updateOrderChange2State(orderNo, goodsid, 2);
				if (updateOrderInfo < 0) {
					//throw new DataAccessException();
					updateOrderInfo = 0;
				}
			} else if (type == 2) {
				if (isUpdateUserAvailable) {
		        	orderMapper.updateUserAvailableM(amount_u, userId);
		        	orderMapper.insertRechargeRecord(userId, (-amount_u), 2, "确认价格中商品金额支付多出,订量变更", orderNo, 0, currency_u);
		        	orderMapper.updateOrderinfo2PayPrice(updatedtotalprice, newProductCost, discount_amount, userId, orderNo);
		        } else {
		        	orderMapper.updateOrderinfo2RemainingPrice(amount, newProductCost, discount_amount, userId, orderNo);
		        }
				
				updateOrderInfo = orderMapper.updateOrderChange2State(orderNo, goodsid, 3);
				if (updateOrderInfo < 0) {
					//throw new DataAccessException();
					updateOrderInfo = 0;
				}
			} else if (type == 3) {
				updateOrderInfo = orderMapper.updateOrderChange2State(orderNo, goodsid, 7);
				if (updateOrderInfo < 0) {
					//throw new DataAccessException();
					updateOrderInfo = 0;
				}
			}
//			orderMapper.updateOrderDetails2Price(userId, goodsid, orderNo, value);
			
		}
		
		//客户端重新提交订单，提示后台前端有更新
//		orderwsMapper.updateClinetAndServerUpdateState(orderNo, 1, 0);
		return ""+updateOrderInfo;
	}

	@Override
	public int saveEvaluate(Evaluate evaluate) {
		int affectedRowCount = orderMapper.saveEvaluate(evaluate);
		if (affectedRowCount != 1) {
			//throw new DataAccessException();
			affectedRowCount = 0;
		}
		return affectedRowCount;
	}

	@Override
	public int upOrderState(String orderNo, int state) {
		int affectedRowCount = orderMapper.upOrderState(orderNo, state);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			affectedRowCount = 0;
		}
		return affectedRowCount;
	}

	@Override
	public int upCouponPrice(String orderNo) {
		int affectedRowCount = orderMapper.upCouponPrice(orderNo);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			affectedRowCount = 0;
		}
		return affectedRowCount;
	}
	
	@Override
	public int cancelOrder(String orderNo, int cancel_obj) {
		int affectedRowCount = orderMapper.cancelOrder(orderNo, cancel_obj);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			affectedRowCount = 0;
		}
		return affectedRowCount;
	}

	@Override
	public Map<String, String> getOrderChangeState(String[] orderNo) {
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, Object>> cnts = orderMapper.getOrderChangeState(orderNo);
		for (Map<String, Object> cnt : cnts) {
			int counts = Integer.parseInt(cnt.get("counts").toString());
			if(counts!=0){
				map.put((String)cnt.get("orderno"), counts+"");
			}
		}
		
		String[] orderNo1 = new String[orderNo.length-map.size()];
		int j = 0;
		for (int i = 0; i < orderNo.length; i++) {
			if(map.get(orderNo[i])==null){
				orderNo1[j] = orderNo[i];
				j++;
			}
		}
		/*Map<String, String> map1 = dao.getOrderChangeState1(orderNo1);
		for (String key : map1.keySet()) {
			
			map.put(key, map1.get(key));
		}*/
		return map;
	}

	@Override
	public float getTotalPrice(String goodsids) {
		return orderMapper.getTotalPrice(goodsids);
	}

	@Override
	public void updateOrderState(int userid, String orderid) {
		orderMapper.updateOrderinfo2State(userid, orderid);
		orderMapper.updateOrderDetails2StateByUserid(userid, orderid);
	}

	@Override
	public AdvanceOrderBean getOrders(String orderNo) {
		List<SpiderBean> spiders = orderMapper.getSpidersByOrderid(orderNo);
		AdvanceOrderBean aOrderBean = orderMapper.getAdvanceOrderByOrderNo(orderNo);
		aOrderBean.setSpiderBean(spiders);
		return aOrderBean;
	}
	
	public OrderBean getOrderByorderNo(String orderNo){
		return orderMapper.getOrderByorderNo(orderNo);
	}

	@Override
	public List<SpiderBean> getSpiders(String orderNo) {
		return orderMapper.getSpiders(orderNo);
	}

	@Override
	public List<ProductChangeBean> getProductChangeInfo(String orderNo, String flag) {
		List<ProductChangeBean> alList = new ArrayList<ProductChangeBean>();
		List<ProductChangeBean> chaList = new ArrayList<ProductChangeBean>();
		
		List<Map<String, Object>> orderDetails = orderMapper.getProductChangeInfoByOrderDetails(orderNo, flag);
		for (Map<String, Object> map : orderDetails) {
			ProductChangeBean pcb = new ProductChangeBean();
			pcb.setGoodId(map.get("goodid").toString());
			pcb.setAliImg((String)map.get("aliimg"));
			pcb.setAliUrl((String)map.get("aliurl"));
			pcb.setAliGoodsCarUrl((String)map.get("car_url"));
			String carImg = (String)map.get("car_img");
			if(carImg.indexOf(".jpg")>1){
				pcb.setAliGoodsCarImg(carImg.substring(0,carImg.indexOf(".jpg")+4));
			}else{
				pcb.setAliGoodsCarImg(carImg.substring(0,carImg.indexOf(".png")+4));
			}
			pcb.setAliPrice(map.get("aliprice").toString());
			pcb.setAliName((String)map.get("aliname"));
			pcb.setGoodsPrice((String)map.get("goodsprice"));
			pcb.setGoodsCarId(map.get("goodscarid").toString());
			pcb.setPurchaseState(map.get("purchase_state").toString());
			pcb.setGoodsName((String)map.get("goodsname"));
			pcb.setChangeFlag((Integer)map.get("changeflag"));
			String[] type = map.get("car_type").toString().split(",");
			
//			String[] type1 = type[0].split("@");
//			pcb.setGoodsType(type1[0]+",");
//			if(type.length>1){
//				String[] type2 = type[1].split("@");
//				pcb.setGoodsType(type1[0]+","+type2[0]);
//			}
			
			if(type.length!=0){
				String[] type1 = new String[type.length];
				if(type[0].indexOf('@')>0){
					type1 = type[0].split("@");
					pcb.setGoodsType(type1[0]+",");
				}else{
					pcb.setGoodsType(type[0]+",");
				}
				if(type.length>1){
					if(type[0].indexOf('@')>0){
						String[] type2 = type[1].split("@");
						pcb.setGoodsType(type1[0]+","+type2[0]);
					}else{
						pcb.setGoodsType(type[0]+","+type[1]);
					}
					
				}
			}
			
			pcb.setCurrency((String)map.get("currency"));
			
			List<Map<String, Object>> goodsdatas = orderMapper.getProductChangeInfoByGoodsdata((String)map.get("aliurl"), map.get("goodscarid").toString());
			for (Map<String, Object> goodsdata : goodsdatas) {
				ProductChangeBean pcb1 = new ProductChangeBean();
				String chagoodimg = (String) goodsdata.get("chagoodimg");
				if(chagoodimg!=null&&!chagoodimg.isEmpty()){
					pcb1.setChangeImg(chagoodimg);
				}else{
					String gdimg = (String) goodsdata.get("gdimg");
					if(gdimg!=null && !gdimg.isEmpty()){
						gdimg = gdimg.replace("[", "").replace("]", "").trim();
						String[] gdimgs = gdimg.split(",\\s+");
						if(gdimgs[0].indexOf(".jpg")>1 || gdimgs[0].indexOf(".png")>1 || gdimgs[0].indexOf(".gif")>1){
							pcb1.setChangeImg(gdimgs[0]);
						}else{
							pcb1.setChangeImg(gdimgs[0]+"jpg");
						}
						
					}
				}
				
				pcb1.setChangePrice(goodsdata.get("chagoodprice").toString());
				//goodsdata:pid
				pcb1.setChangePid(goodsdata.get("pID").toString());
				pcb1.setChangeUrl((String) goodsdata.get("chagoodurl"));
				//goodsdata:name
				pcb1.setChangeName((String) goodsdata.get("chagoodname"));
				pcb1.setAliUrl((String) goodsdata.get("aliurl"));
				pcb1.setChangeFlag((Integer) goodsdata.get("changeflag"));
				pcb1.setGoodsType((String) goodsdata.get("goodstype"));
				pcb1.setGdId(goodsdata.get("gdid").toString());
				pcb1.setGoodsCarId(goodsdata.get("goodscarid").toString());
				
				chaList.add(pcb1);
			}
			alList.add(pcb);
		}
				
		for(int i=0;i<alList.size();i++){
			alList.get(i).setChangeList(new ArrayList<ProductChangeBean>());
			for(int j=0;j<chaList.size();j++){
				if(alList.get(i).getAliUrl().equals(chaList.get(j).getAliUrl()) && alList.get(i).getGoodsCarId().equals(chaList.get(j).getGoodsCarId())){
					alList.get(i).getChangeList().add(chaList.get(j));
				}
			}
		}
		
		return alList;
	}

	@Override
	public List<ProductChangeBean> getPriceReductionOffer(String userId) {
		return orderMapper.getPriceReductionOffer(Integer.parseInt(userId));
	}

	@Override
	public int upQuestions(String orderid, String questions) {
		String orderidStr = orderMapper.getAdvance(orderid);
		int affectedRowCount = 0;
		if(orderidStr != null){
			affectedRowCount = orderMapper.upQuestions(orderid, questions);
			if (affectedRowCount < 0) {
				//throw new DataAccessException();
				affectedRowCount = 0;
			}
		}else{
			affectedRowCount = orderMapper.addAdvance(orderid, questions);
			if (affectedRowCount != 1) {
				//throw new DataAccessException();
				affectedRowCount = 0;
			}
		}
		return affectedRowCount;
	}

	@Override
	public int getOrderState(String orderNo) {
		return orderMapper.getOrderState(orderNo);
	}
	
	/**
	*
	*
	*
	*******************************/

	@Override
	public void add(List<OrderDetailsBean> orderdetails) {
		//添加订单详细信息
		for(int i=0;i<orderdetails.size();i++){
			OrderDetailsBean orderdetail=orderdetails.get(i);
			orderMapper.addOrderDetail(orderdetail);
		}
		
	}

	@Override
	public List<OrderDetailsBean> getOrder(int userid, String orderid) {
		List<OrderDetailsBean> odb = orderMapper.getOrderDetail(userid, orderid);
		return odb;
	}

	@Override
	public int addAddress(Address add) {
		return orderMapper.addAddress(add);
	}

	@Override
	public List<Address> getUserAddr(int userid) {
		//获取用户地址
		return orderMapper.getUserAddr(userid);
	}

	@Override
	public int existUserAddr(int userid) {
		//查询用户地址是否存在
		return orderMapper.existUserAddr(userid);
	}

	@Override
	public int updateIndvidualAddress(int userid, int id, String address,
			String country, String phonenumber, String zipcode, String address2) {

		int res = 0;
		if(id == 0){
			Address add = new Address();
			add.setUserid(userid);
			add.setAddress(address);
			add.setCountry(country);
			add.setZip_code(zipcode);
			add.setPhone_number(phonenumber);
			//添加新地址
			orderMapper.addAddress(add);
		}else{
			//更新用户地址
			res = orderMapper.updateUserAddress(id, address, country, phonenumber, zipcode,address2,"California", null, null);
		}
		return res;
	}

	@Override
	public void addOrderInfo(List<OrderBean> orderinfo, int addressid,
			int odcount) {
		String exchange_rate=orderMapper.getExchangeRate("RMB");
		for(int i=0;i<orderinfo.size();i++){
			OrderBean info =orderinfo.get(i);
			info.setAddressid(addressid);
			info.setOdcount(odcount);
			info.setExchange_rate(exchange_rate);
			orderMapper.addOrderInfo(info);
		}
		//保存混批折扣记录,addressid,odcount
		OrderBean ob = orderinfo.get(0);
		if(ob.getDiscount_amount() > 0){
			//保存订单折扣信息
			orderMapper.saveOrder_discount(ob.getOrderNo(), 1, ob.getDiscount_amount(), "");
		}
	}

	@Override
	public List<OrderBean> getOrderInfo(int userid, String order_no) {
		//获取用户的订单记录
		return orderMapper.getOrderInfoByOrder_noAndUserid(userid, order_no.split(","));
	}

	@Override
	public void updateOrderState(int userid, String orderid,String pay_price_three) {
		//确认付款后修改订单和订单详情状态
		orderMapper.updateOrderPayPriceThree(userid, orderid, pay_price_three);
		orderMapper.updateOrderState(userid, orderid);
	}

	@Override
	public void updateOrderStatePayPrice(int userid, String orderid,String pay_price_three, String pryprice, String ipnAddressJson,
			double order_ac,double owe) {
		//更新用户支付金额,状态
		orderMapper.updateOrderStatePayPrice(userid, orderid, pay_price_three, pryprice, ipnAddressJson,order_ac,owe);
		orderMapper.updateOrderState(userid, orderid);
		}

	@Override
	public void updateOrderStatePayPrice(int userid, List<String[]> orderInfo,
			String ipnAddressJson) {
		//更新用户支付金额,状态
		orderMapper.updateOrderStatePayPrice_1(userid, orderInfo, ipnAddressJson);
		}

	@Override
	public int updateGoodscarState(int userid, String itemid) {
			//itemid = itemid.replaceAll(",", "','") + "'";
		//itemid = "'" + itemid.replaceAll(",", "','") + "'";
		itemid = itemid.replaceAll(",", "','");
		return orderMapper.updateGoodscarState(userid, itemid);
	}

	@Override
	public int updateGoodscarStateByItemid(String itemid) {
		//itemid = itemid.replaceAll("@", ",");
		String[] itemIdArray = itemid.split("@");
		return orderMapper.updateGoodscarStateByItemid(itemIdArray);
	}

	@Override
	public void updateGoodscarStateAgain(int userid, String itemid) {
		//付款时再次更新购物车的商品状态
		orderMapper.updateGoodscarStateAgain(userid, itemid);
	}

	@Override
	public void updateUserAddress(int id, String address, String country,
			String phonenumber, String zipcode, String address2,
			String statename, String recipients, String street) {
		// 更新用户地址
		orderMapper.updateUserAddress(id, address, country, phonenumber, zipcode, address2,statename, recipients, street);
	}

	@Override
	public List<Eightcatergory> getHomefurnitureProduct(String catergory) {
		// 获取homefurniture页面内容
		catergory = catergory.replaceAll("@", ",");
		return orderMapper.getHomefurnitureProduct(catergory);
	}

	@Override
	public void delUserAddressByid(int id) {
		//根据id删除收件地址
		orderMapper.delUserAddressByid(id);
	}

	@Override
	public void setDefault(int id, int userid) {
		//设置默认地址
		orderMapper.setDefault(id, userid);
		
	}

	@Override
	public Address getUserAddrById(int id) {
		//通过id查询地址
		return orderMapper.getUserAddrById(id);
	}

	@Override
	public int getAddressCountByUserId(int userid) {
		//通过userid查询地址count
		return orderMapper.getAddressCountByUserId(userid);}

	@Override
	public void updateOrderPayPrice(int userid, String order_no,
			String pay_price, String ipnAddressJson) {
		// 更新用户支付金额
		orderMapper.updateOrderPayPrice(userid, order_no, pay_price, ipnAddressJson, -1, -1, 0,-1);
	}

	@Override
	public int addOrderAddress(Map<String, Object> map) {
		//新增地址
		return orderMapper.addOrderAddress(map);
	}

	@Override
	public int addOrderAddress(List<Map<String, Object>> maps) {
		int row=0;
		//新增订单地址
		for(int i=0;i<maps.size();i++){
			Map<String, Object> map=maps.get(i);
			row+=orderMapper.addOrderAddress(map);
		}
		return row;
	}

	@Override
	public int saveOrder_discount(String orderno, int discounttype,
			double price, String discountinfo) {
		//保存订单折扣信息
		return orderMapper.saveOrder_discount(orderno, discounttype, price, discountinfo);
		}

	@Override
	public int upOrderExpress(String orderno, String mode_transport,
			String actual_ffreight) {
		//修改订单需支付运费金额，运输方式
		return orderMapper.upOrderExpress(orderno, mode_transport, actual_ffreight);
		}

	@Override
	public int upOrderExpress(String orderno, String mode_transport,
			String actual_ffreight, String remaining_price,
			String pay_price_tow, double service_fee, double pay_price) {
		// 修改订单需支付运费金额，运输方式,运费，剩余支付金额，已支付运费
		return orderMapper.upOrderExpress_1(orderno, mode_transport, actual_ffreight,remaining_price,pay_price_tow,service_fee, pay_price);
	}

	@Override
	public int upOrderService_fee(String orderno, String service_fee) {
		//修改订单服务费
		return orderMapper.upOrderService_fee(orderno, service_fee);
	}

	@Override
	public String initCheckData(String orderNos) {
		return orderMapper.initCheckData(orderNos);
	}

	@Override
	public List<OrderBean> getOrderByUid(Integer uid) {

		return orderMapper.getOrderByUid(uid);
	}

	@Override
	public int updateOrderShowFlag(int userid) {
		String paymentTime = orderMapper.selectOrderShowFlag(userid);
		int rows = 0;
		if (StringUtils.isNotBlank(paymentTime)) {
			rows = orderMapper.updateOrderShowFlag(userid, paymentTime);
		}
		return rows;
	}

	@Override
	public int updateUnpaidOrderShowFlag(int userid) {
		String paymentTime = orderMapper.selectUnpaidOrderShowFlag(userid);
		int rows = 0;
		if (StringUtils.isNotBlank(paymentTime)) {
			rows = orderMapper.updateUnpaidOrderShowFlag(userid, paymentTime);
		}
		return rows;
	}

	@Override
	public List<OrderBean> getOrdersByUserid(int userid) {
		List<OrderBean> oblist = new ArrayList<OrderBean>();
		List<Map<String, Object>> listMap = orderMapper.getOrdersByUserid(userid);
		//循环
		for (Map<String, Object> map : listMap) {
			OrderBean temp = new OrderBean();
			temp.setOrderNo((String)map.get("order_no"));
			//dropship 标识
			int dropshipOrderFlag = Integer.parseInt(map.get("isDropshipOrder").toString());
			if(dropshipOrderFlag==1){
				List<Dropshiporder> dropshipList =  dao.selectByUserIdAndParentOrderNo(userid,(String)map.get("order_no"));
				int size = dropshipList.size();
				int i = 0 ,i_1 = 0, j = 0,k = 0;
				if(size>0){
					 for(Dropshiporder row:dropshipList){
						 int state = Integer.parseInt(row.getState());
						 if(state!=1){ //满足所有子单状态不为1.且存在状态为5
							  i++;
							  if(state==5){
								  i_1++;
							  }
						 }if(state==1){ //如果dropship 子单状态有采购状态，则主单状态为采购状态
							  temp.setState(1);
							  break;
						  }if(state>=2&&state<5){ //到库状态判断
							  j++;
						  }if(state>=3&&state<5){ //出库状态判断
							  k++;
						  }
					 }
					 if(i==size){
						 if(i_1>0){
							 temp.setState(5);
						 }
					 }if(j==size){
						 temp.setState(2);
					 }if(k==size){
						 temp.setState(3);
					 } 
				}
			}else{
				temp.setState(Integer.parseInt((String)map.get("state")));
			}
			temp.setForeign_freight((String)map.get("foreign_freight"));
			String product_cost_ = (String)map.get("product_cost");
			if (StringUtils.isNotBlank(product_cost_)) {
				double fp = Double.parseDouble(product_cost_);
				temp.setProduct_cost(new BigDecimal(fp).setScale(2,   BigDecimal.ROUND_HALF_UP).toString());
			} else {
				temp.setProduct_cost(null);
			}
			String createTime = String.valueOf(map.get("create_time")); 
			temp.setCreatetime(createTime == null ? null : createTime.substring(0, 16));
			temp.setRemaining_price(map.get("remaining_price") == null ? 0.0 : (Double)map.get("remaining_price"));
			String pay_price=(String)map.get("pay_price");
			if(pay_price != null && !"".equals(pay_price)){
				//temp.setPay_price(new BigDecimal(pay_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				temp.setPay_price(Double.valueOf(pay_price));
			}	
			temp.setMode_transport((String)map.get("mode_transport"));
			temp.setServer_update((Integer)map.get("server_update"));
			temp.setCurrency((String)map.get("currency"));
			temp.setActual_ffreight((String)map.get("actual_ffreight"));
			temp.setPaystatus((String)map.get("paystatus"));
			temp.setExpressNo((String)map.get("express_no"));
			temp.setDeliveryTime(map.get("delivery_time") == null ? 0 : Integer.valueOf((String)map.get("delivery_time")));
			String service_fee1=String.valueOf(map.get("service_fee") == null ? 0.0 :Double.parseDouble(map.get("service_fee").toString()));
			temp.setService_fee(service_fee1);
			temp.setChaOrderNo(String.valueOf(map.get("chaOrderNo")));
			//Subsitution Confirmed标识
			temp.setDomestic_freight((String)map.get("domestic_freight"));
			//dropship标识
			temp.setIsDropshipOrder(Integer.parseInt(map.get("isDropshipOrder").toString()));
			
			//免邮费免邮判断
			String modeTransport = (String)map.get("mode_transport");
			if(modeTransport != null && modeTransport.indexOf("@0@all") != -1) {
				temp.setFree_shipping(1);
			}else{
				temp.setFree_shipping(0);
			}
			//未支付订单先放到已支付订单里，1分钟内如果没支付在 放回未支付订单
			temp.setPayFlag(Integer.parseInt(map.get("payflag").toString()));
			//未支付订单4小時内不可以取消
			temp.setCancelFlag(Integer.parseInt(map.get("cancelFlag").toString()));
			//已支付订单8小時内消息切换标识
			temp.setComformFlag(Integer.parseInt(map.get("comformFlag").toString()));
			
			//PayPal 审核中
			if(temp.getState()==0 && "0".equals(temp.getPaystatus())){
				temp.setPayFlag(1);
			}
			//额外运费
			temp.setExtra_freight(Double.valueOf(map.get("extra_freight").toString()));
			//额外優惠
			temp.setExtra_discount(Double.valueOf(map.get("extra_discount").toString()));
			
			oblist.add(temp);
		}
		return oblist;
	}

	@Override
	public int getPriceReduction(int userid) {
		return orderMapper.getPriceReduction(userid);
	}

	@Override
	public int getHistoryCount(int userid) {
		return orderMapper.getHistoryCount(userid);
	}
	
	
	@Override
	public void updateServerUpdate(String order_no) {
		orderMapper.updateServerUpdate(order_no);
		
	}

	@Override
	public List<OrderDetailsBean> getIndividualOrdersDetails(String orderNo, int state) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		 
		listMap = orderMapper.getIndividualOrdersDetails(orderNo,state);
		List<OrderDetailsBean> spiderlist = new ArrayList<OrderDetailsBean>();

		String gdsUrl=null;
		
		for (Map<String, Object> rs : listMap) {
			OrderDetailsBean odb = new OrderDetailsBean();
			odb.setUserid(Integer.valueOf((String) rs.get("userid")));
			odb.setOrderid(orderNo);
			odb.setId(Integer.valueOf(rs.get("oid").toString()));
			//yqy start 修改
			odb.setDropshipid((String)rs.get("dropshipid"));
			//yqy end
			odb.setCheckproduct_fee(Integer.valueOf((String)rs.get("checkprice_fee")));
			odb.setState(Integer.valueOf((String) rs.get("state")));
			odb.setPaytime((String)rs.get("paytime"));
			odb.setYourorder(Integer.valueOf((String) rs.get("yourorder")));
			odb.setGoodsprice((String)rs.get("goodsprice"));
			odb.setGoodsid(Integer.valueOf((String) rs.get("goodsid")));
			odb.setFreight((String)rs.get("goodsfreight"));
			odb.setGoodsname((String)rs.get("goodsname"));
			odb.setGoods_img((String)rs.get("car_img").toString().replace("_50x50.jpg", ""));
			odb.setGoods_url((String)rs.get("car_url"));
//			odb.setGoodsprice((String)rs.get("od.goodsprice"));
			odb.setDelivery_time((String)rs.get("delivery_time"));
			odb.setFreight_free(Integer.valueOf((String) rs.get("freight_free")));
			odb.setGoods_type((String)rs.get("car_type"));
			int carId = Integer.valueOf((String) rs.get("goodsid"));
			gdsUrl = (String)rs.get("car_url");
			if(rs.get("Actual_price")!=null){
				odb.setActual_price(Double.valueOf((String) rs.get("Actual_price")));
			}
			if(rs.get("Actual_freight")!=null){
				odb.setActual_price(Double.valueOf((String) rs.get("Actual_freight")));
			}
			odb.setActual_weight((String)rs.get("Actual_weight"));
			odb.setActual_volume((String)rs.get("Actual_volume"));
			//lzj start  备注 od.fileupload在sql中没有查询这个字段  我自己加上的这个字段
			odb.setFileupload((String)rs.get("fileupload"));
			//lzj  end
			odb.setChange_price("");
			odb.setChange_delivery("");
			odb.setNewsourceurl("");
			odb.setIscancel(0);
			odb.setRemark((String) rs.get("remark"));
			odb.setPurchase_confirmation((String)rs.get("purchase_confirmation"));
			odb.setPurchase_state(Integer.valueOf((String) rs.get("purchase_state")));
			odb.setPurchase_time(String.valueOf(rs.get("purchase_time")));
			odb.setCreatetime((String) rs.get("create_time"));
			//yqy start 修改
			Object object = rs.get("goods_typeimg");
			if (object!=null) {
				odb.setImg_type((String) object.toString().replace("_50x50.jpg", ""));
			}
			//yqy end
			Map<String, Object> rs2 = orderMapper.getIndividualOrdersChange(orderNo,carId);
			if(rs2!=null){
				for (int j = 0; j < rs2.size(); j++) {
					int ropType = Integer.parseInt(String.valueOf(rs2.get("ropType")));
					String values=String.valueOf(rs2.get("newValue"));
					if(ropType==1){
						odb.setChange_price(values);
					}
					if(ropType==2){
						odb.setChange_delivery(values);
					}
					if(ropType==6){
						odb.setNewsourceurl("price:"+rs2.get("oldValue")+"<br/><a  onclick='fnRend(\""+values+"\")' >"+values+"</a>");
					}
					if(ropType==4){
						odb.setIscancel(1);
					}
					if(ropType==7){
						odb.setChange_freight(values);
					}
					if(ropType != 6){
						odb.setPurchase_state(2);
					}
				}
			}
				
			spiderlist.add(odb);
			
		}
		return spiderlist;
	}

	@Override
	public List<IdRelationTable> getIdRelationtable(String orderNo) {
		List<IdRelationTable> list = orderMapper.getIdRelationtable(orderNo);
		return list;
	}

	@Override
	public void updateChangeDelFlag(String order_no, int goodsCarid) {
		orderMapper.updateChangeDelFlag(order_no, goodsCarid);
		
	}

	@Override
	public String getOrderNo() {
		return orderMapper.getOrderNo();
	}

	@Override
	public String getMaxOrderno(String userid) {
		return orderMapper.getMaxOrderno(userid);
	}
	
	@Override
	public void mergeOrder(String orderid) {
		String newOrderId = System.currentTimeMillis() + "";
		orderMapper.insertOrderInfo(orderid, newOrderId);
		orderMapper.updateOrderId(orderid, newOrderId);
	}

	@Override
	public Integer existOrderNo(String orderNo) {
		return orderMapper.existOrderNo(orderNo);
	}
	
	@Override
	public void updateOrderInfoState(String orderNo) {
		orderMapper.updateOrderInfoState(orderNo);
		
	}
	
	@Override
	public void updateOrderInfopr(String orderNo,int userId,String productCost,String remainingPrice) {
		orderMapper.updateOrderInfopr(orderNo,userId,productCost,remainingPrice);
		
	}
	
	@Override
	public void updateChangeGood(String orderNo,String goodId) {
		orderMapper.updateChangeGood(orderNo,goodId);
		
	}

	@Override
	public void updateOrderinfo2Freight(String orderNo) {
		orderMapper.updateOrderinfo2Freight(orderNo);
		
	}

	
	
	@Override
	public int saveGoodsCar(SpiderBean spider) {
		return orderMapper.saveGoodsCar(spider);
	}
	
	@Override
	public void updateOrderDetail(String orderNo,int userId,String goodsId,String goodSprice,String name,int goodsCarId,int id,String remark,String goodsUrl,String goodsImg,String goodsType) {
		orderMapper.updateOrderDetail(orderNo,userId,goodsId,goodSprice,name,goodsCarId,id,remark,goodsUrl,goodsImg,goodsType);
	}
	
	@Override
	public void updateProductSource(String goodsid,String goodsdataid,String goodsUrl,String goodsImgUrl,String goodsPrice,String name,String orderNo,int goodCarKey) {
		orderMapper.updateProductSource(goodsid,goodsdataid,goodsUrl,goodsImgUrl,goodsPrice,name,orderNo,goodCarKey);
	}
	
	@Override
	public Page<OrderBean> getOrderByUid(int uid, Page<OrderBean> page) {
		int start = (page.getStartIndex()-1) * 10;
		int pageSize = page.getOnePageCount();
		//限定只有出运中的订单可以投诉
		List<OrderBean> orderList = orderMapper.getOrderByUidPage(uid, start, pageSize);
		int countRow = orderMapper.getOrderByUidPageCount(uid);
		page.setCountRecord(countRow);
		page.setList(orderList);
		page.calculate();
		return page;
	}
	
	@Override
	public int updateOrderinfoByBalancePayOnSuccess(int userId, String orderNo, String payPrice, String payPriceThree, String payPriceTow) {
		int affectedRowCount = orderMapper.updateOrderinfoByBalancePayOnSuccess(userId, orderNo, payPrice, payPriceThree, payPriceTow);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			return 0;
		}
		return affectedRowCount;
	}
	
	@Override
	public int updateOrderAc(int userId, String orderNo, double orderAc) {
		int affectedRowCount = orderMapper.updateOrderAc(userId, orderNo, orderAc);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			return 0;
		}
		return affectedRowCount;
	}
	
	
	@Override
	public int updateRemainingPrice(int userId, String orderNo, double remainingPrice) {
		int affectedRowCount = orderMapper.updateRemainingPrice(userId, orderNo, remainingPrice);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			return 0;
		}
		return affectedRowCount;
	}

	@Override
	public void addOrderInfo(OrderBean orderBean) {
		orderMapper.addOrderSelective(orderBean);
	}

	@Override
	public Map<String, Object> getTotalPriceFormOrderNo(String orderNo) {
		return orderMapper.getTotalPriceFormOrderNo(orderNo);
	}
	
	
	@Override
	public List<OrderDetailsBean> getDelOrderByDropshipIdAndStatus(String orderNo, int goodId) {
		return orderMapper.getDelOrderByDropshipIdAndStatus(orderNo, goodId);
	}
	
	public String getCountryIdFromOrderNo(String orderNo){
		return orderMapper.getCountryIdFromOrderNo(orderNo);
	}

	@Override
	public int updateOrderDetails2StateByGoodId(String orderNo, int goodid) {
		return orderMapper.updateOrderDetails2StateByGoodId(orderNo, goodid);
	}
	
	@Override
	public int updateOrderDetailsStateByDropshipNoAndGoodId(String dropshipNo,int goodid) {
		return orderMapper.updateOrderDetailsStateByDropshipNoAndGoodId(dropshipNo, goodid);
	}

	@Override
	public String getZoneBy(int id) {
		return orderMapper.getZoneBy(id);
	}

	@Override
	public int updateOrderInfoFormCancelProduct(Map<String, Object> map) {
		return orderMapper.updateOrderInfoFormCancelProduct(map);
	}

	@Override
	public int addOrderNoLog(int userId, String orderNo, String days,
			int addressId, String itemInfo,double pay_product) {
		return orderMapper.addOrderNoLog(userId, orderNo, days, addressId, itemInfo,pay_product);
	}

	@Override
	public int getOrderCgWithCancle(String orderNo) {
		return orderMapper.getOrderCgWithCancle(orderNo);
	}

	@Override
	public int updateOrderChange2State(String orderNo, int goodId, Integer ropType) {
		return orderMapper.updateOrderChange2State(orderNo, goodId, ropType);
	}
	
	@Override
	public int queryPreferential(int userid) {
		// TODO Auto-generated method stub
		return orderMapper.queryPreferential(userid);
	}
	
	@Override
	public int updatePreferential(int userid, String orderNo) {
		// TODO Auto-generated method stub
		return orderMapper.updatePreferential(userid,orderNo);
	}
	
	@Override
	public int upUserPrice(int userId, double price) {
		int affectedRowCount = orderMapper.upPrice(userId, price);
		if (affectedRowCount < 0) {
			//throw new DataAccessException();
			return 0;
		}
		return affectedRowCount;
	}
}
