package com.cbt.website.service;

import com.cbt.bean.*;
import com.cbt.pay.dao.IOrderDao;
import com.cbt.pay.dao.OrderDao;
import com.cbt.util.OrderInfoUtil;
import com.cbt.util.Utility;
import com.cbt.warehouse.pojo.Dropshiporder;
import com.cbt.website.dao.*;
import com.importExpress.pojo.OrderCancelApproval;
import com.importExpress.utli.NotifyToCustomerUtil;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

public class OrderSplitServer implements IOrderSplitServer{

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderSplitServer.class);
	IOrderSplitDao dao = new OrderSplitDaoImpl();
    IOrderwsDao orderwsDao = new OrderwsDao();
	
	@SuppressWarnings("finally")
	@Override
//	public String splitOrder(String orderNo, String odids, int state,String adminUserName,Map<String, String> oddSMap) {
	public String splitOrder(String orderNo, String odids, int state,String adminUserName) {
		/*1.所有 有 采购链接的 商品 就 直接 转入一个 新订单，并且转状态到 采购中
		2.已支付的产品金额按产品直接拆分
		3.客户是以前付过 运费的，我们就按照 体积重量，直接拆分 这 两个订单
		 体积重量 = 长* 宽*高 (厘米)/5000  和 实际重量对比 取 大值
		4.客户如果取消商品，而该订单 是 -split 的，就不再计算批量折扣
		5.原订单的批量折扣金额， 按照价格比例分开
		6.50美元的 运费credit的拆分*/
		int initState = 5;//订单最初始状态
		String result = "";
		Map<String, String> map = new HashMap<String, String>();
		//标记是否有填写拆分数量订单
		boolean flag = false;
		//前台选中的拆分数量《原始数量的orderdetailId
		String selSplitArr = "";
		//更新orderdetails 集合
		List <OrderDetailsBean>updateOrderDetaildList = new ArrayList<OrderDetailsBean>();
		//添加orderdetails 集合
		List <OrderDetailsBean>insertOrderDetaildList = new ArrayList<OrderDetailsBean>();
		try {
		//List<Map<String,String>> splitList = new ArrayList<Map<String,String>>();
		Map <String,String>	 splitMap = new HashMap<String, String>();
	    //遍历前台orderdetails和输入拆单数量map
//		Iterator<Map.Entry<String, String>> entries = oddSMap.entrySet().iterator(); 
//		while (entries.hasNext()) {  
//		    Map.Entry<String, String> entry = entries.next(); 
//		    //根据orderdetailID查出原有数量
//		    int youOrder = dao.getOrderdetailYouorder(entry.getKey());
//		    int odsId = Integer.parseInt(entry.getValue());
//		    if(youOrder < odsId){
//		    	//拆分数量大于原有数量
//		    	result = "拆分数量大于原有数量";
//				map.put("res", "");
//				map.put("msg","拆分数量大于原有数量");
//				return JSONArray.fromObject(map).toString();
//		    }else if(youOrder > odsId && odsId !=0){
//		    	//有选择数量
//		    	flag = true;
//		    	//所有填写数量且不等于原有数量的商品
//		    	splitMap.put(entry.getKey(), entry.getValue());
//		    	selSplitArr = selSplitArr + entry.getKey() + ",";  
//		    }
//		}  
		//查询订单和订单详情
		//当前拆分之后所剩的订单状态如果一致就更新orderinfo的状态
		OrderBean orderBean = dao.getOrders(orderNo);
		initState = orderBean.getState(); // 记录初始状态
		String[] odid = odids.split("@");//保留的 <拆单完事剩余的商品>
		List<OrderProductSource>  list= dao.getOrderProductSource(orderNo, odid); 
		int OrderProductSourceBuycount = 0;//购买的数量
		int OrderProductSourceUsecount = 0;//用户的数量
		int purchase_state_1 = 0;//状态为-购买中1
		int purchase_state_2 = 0;//状态为-已到仓库2
		int purchase_state_3 = 0;//状态为-出运中3
		//这个重复了 ，移到后面
		for (int i = 0; i < list.size(); i++) {
			OrderProductSource orderVo =  list.get(i);
			OrderProductSourceBuycount += orderVo.getBuycount();
			OrderProductSourceUsecount += orderVo.getUsecount();
			if(OrderProductSourceBuycount >= OrderProductSourceUsecount){
				//订单已经采购了
				if(orderVo.getPurchaseState()== 3 ){
					purchase_state_1 +=1;
				}
				//订单已经入库了
				if(orderVo.getPurchaseState() == 4 || orderVo.getPurchaseState() == 6){
					purchase_state_2 +=1;
				}
				//订单已经出运了
				if(orderVo.getPurchaseState() == 8){
					purchase_state_3 +=1;
				}
			}
		}
		if(purchase_state_1 != 0 || (purchase_state_1 == list.size() && list.size() > 0) || (purchase_state_2 != 0 && purchase_state_2 <list.size()) ){
			orderBean.setState(1);
		}else if(purchase_state_2 != 0 && purchase_state_2 == list.size()){
			orderBean.setState(2);
		}else if(purchase_state_3 != 0 && purchase_state_3 == list.size()){
			orderBean.setState(3);
		}else {
			orderBean.setState(5);
		}
		//记录老订单拆分日志
		String info_log = ", product_cost="+orderBean.getProduct_cost()+",pay_price="+orderBean.getPay_price()
							+",pay_price_tow="+orderBean.getPay_price_tow()+",pay_price_three="+orderBean.getPay_price_three()
								+",actual_ffreight="+orderBean.getActual_ffreight()+",service_fee="+orderBean.getService_fee()
									+",order_ac="+orderBean.getOrder_ac()+",discount_amount="+orderBean.getDiscount_amount()
										+",cashback="+orderBean.getCashback()+",extra_freight="+orderBean.getExtra_freight()
											+",share_discount="+orderBean.getShare_discount()+",Coupon_discount="+orderBean.getCoupon_discount()+",extra_discount="+orderBean.getExtra_discount()+"state="+orderBean.getState();//--cjc 2016.11.07 
		List<OrderDetailsBean> orderDetails = dao.getOrdersDetails_split(orderNo);
		double price_split = 0;//拆分产品价格
		double price = 0;//未拆分产品价格
		double width = 0;
		double width_split = 0;
		//混批折扣
		double discount_amount = 0;
		double remaining_price_split = orderBean.getRemaining_price();//订单剩余未支付金额
		String splitId = "";
		String splitGid = "";
		String selSplitGid = "";
		int details_number = 0;//需采购数量
		String[] odIds_ = odids.split("@");//未勾选的商品，先出货
		//int purchase_state_o = orderBean.getPurchase_number();
		//遍历订单详情，获取产品金额拆分支付费用，运费拆分，50 credit拆分，混批折扣拆分，服务费拆分<服务费现在不进行拆分了，留在原单>
		int isSplit = 0;
		int purchase_state = 0;//原订单已采购的order_detail数量
		int pstate3 = 0;//记录原订单(未勾选)详情中采购状态为3的数量
		int pstate4 = 0;//记录原订单(未勾选)详情中采购状态为4的数量
		int odstate = 0;//记录原订单(未勾选)详情中入库状态为1的数量
		for (int i = 0; i < orderDetails.size(); i++) {
			OrderDetailsBean odb = orderDetails.get(i);
			//有采购地址，需拆分为采购中的订单
			double width_ = Double.parseDouble(odb.getActual_volume())/50;
			double weight = Double.parseDouble(odb.getActual_weight());
			for (int j = 0; j < odIds_.length; j++) {
				if(odIds_[j].equals(odb.getId()+"")){
					isSplit = 1;
					break;
				}
			}
			if(isSplit == 1){
				//未拆分出去的，老订单，先出货商品
				double pricesum = odb.getYourorder() * Double.parseDouble(odb.getGoodsprice());
				price += pricesum;
				width += width_ > weight ? width_ : weight;
//				purchase_state += odb.getPurchase_state();
				// Purchase_state  3采购,  state 1到库(到库后采购状态可能 Purchase_state==1，也可能还保留 purchase= 3)<到库以后采购状态已经改成Purchase_state==1了>
				//计算订单采购的数量
				if(odb.getPurchase_state()==3 || odb.getState()==1 ){
					purchase_state ++;
				}
				if(odb.getPurchase_state()==3 && odb.getState()==0){ //只有当采购状态为3 并且状态为0 才表示为采购，商品到仓库后，采购状态可能改为1，可能还是3，但state是为0的 <现在只改为3了>
					pstate3++;
				}
				if(odb.getState()==1){
					pstate4++;
				}
				if(odb.getState()==1){//记录原订单(未勾选)详情中入库状态为1的数量
					odstate++;
				}
//				if(odb.getDiscount_ratio() != 1 && odb.getDiscount_ratio() != 0){
//					discount_amount += pricesum * (1-odb.getDiscount_ratio());//把批量优惠的钱也算到了混批优惠里面了，应该有问题。就是算在这个里面
//				}
			}else{
				//splitMap若包含该orderdetailId,则在拆分后的订单和原始订单中都有商品,部分拆单
				if(splitMap.containsKey(String.valueOf(odb.getId()))){
					int splitNum = Integer.parseInt(splitMap.get(String.valueOf(odb.getId())));   
					//未拆分出去老订单，先出货商品
					int oldNum = odb.getYourorder() - splitNum;
					double pricesum = oldNum * Double.parseDouble(odb.getGoodsprice());
					price += pricesum;
					width += width_ > weight ? width_ : weight;   ////这个字段拆分怎么处理？？
					//计算订单采购的数量    采购数量怎么处理，是不是直接算原始orderdetail 
					if(odb.getPurchase_state()==3 || odb.getState()==1 ){
						purchase_state ++;
					}
					if(odb.getPurchase_state()==3 && odb.getState()==0){ //只有当采购状态为3 并且状态为0 才表示为采购，商品到仓库后，采购状态可能改为1，可能还是3，但state是为0的 <现在只改为3了>
						pstate3++;
					}
					if(odb.getState()==1){
						pstate4++;
					}
					if(odb.getState()==1){//记录原订单(未勾选)详情中入库状态为1的数量
						odstate++;
					}
//					if(odb.getDiscount_ratio() != 1 && odb.getDiscount_ratio() != 0){
//						discount_amount += pricesum * (1-odb.getDiscount_ratio());//把批量优惠的钱也算到了混批优惠里面了，应该有问题。
//					}
					//更新老订单对应的orderDetails
					//数量，还有什么？？？暂时只有数量
					odb.setYourorder(oldNum);
					updateOrderDetaildList.add(odb);
					//部分拆分出去的订单
					double pricesumSplit = splitNum * Double.parseDouble(odb.getGoodsprice());
					price_split += pricesumSplit;
					details_number ++;////这个字段拆分怎么处理？？怎么重新算重量
					//width_split += width_ > weight ? width_ : weight; //这个字段拆分怎么处理？？怎么重新算重量
					selSplitGid += odb.getGoodsid()+",";
					//新增订单orderDetails,有哪些参数？？？
					OrderDetailsBean newOrderDetail = (OrderDetailsBean)odb.clone();
					newOrderDetail.setUserid(odb.getUserid());
					newOrderDetail.setYourorder(splitNum);
					insertOrderDetaildList.add(newOrderDetail);
				}else{
					//拆分出去的，后出货商品,全部拆单
					splitId += odb.getId()+",";
					splitGid += odb.getGoodsid()+",";
					double pricesum = odb.getYourorder() * Double.parseDouble(odb.getGoodsprice());
					price_split += pricesum;
					width_split += width_ > weight ? width_ : weight;
					details_number ++;//拆分出去商品总类数
				}
			}
			isSplit = 0;
		}
		if(splitId.equals("") && splitMap.isEmpty()){
			result = "请选择拆分商品";
			map.put("res", "");
			map.put("msg","请选择拆分商品");
			return JSONArray.fromObject(map).toString();
		}
		
//		double split = price_split / Double.parseDouble(orderBean.getProduct_cost()) ;
		//订单运费
		double freight = (width + width_split) != 0 ? (width / (width + width_split) * Double.parseDouble(orderBean.getForeign_freight())) : 0;
		//credit
		double credit = 0;
		if(orderBean.getOrder_ac() != 0){
			credit = (width + width_split) != 0 ? (width / (width + width_split) * orderBean.getOrder_ac()) : 0;
		}
		//服务费
		double service = 0;
		if(Utility.getIsDouble(orderBean.getService_fee())){
			service = Double.parseDouble(orderBean.getService_fee());
		
		}
		//当前返单优惠coupon_discount
				double coupon_discount=0;
				double coupon_discount1=0;
					coupon_discount1 = orderBean.getCoupon_discount();
					coupon_discount = price/(price+price_split)*coupon_discount1;
		//当前加急运费
				double extra_freight=0;
				double extra_freight1=0;
					extra_freight1 = orderBean.getExtra_freight();
					extra_freight = price/(price+price_split)*extra_freight1; 
		//当前分享折扣 share_discount +=pricesum * (1-odb.getDiscount_ratio()); 
					double share_discount=0;
					double share_discount1=0;
						share_discount1 = orderBean.getShare_discount();
						share_discount = price/(price+price_split)*share_discount1; 
		//手动优惠金额;  extra_discount
					double extra_discount=0;
					double extra_discount1=0;
							extra_discount1 = orderBean.getExtra_discount();
							extra_discount = price/(price+price_split)*extra_discount1; 				
		//10美元减免，只有超过200美元才有
				double cashback = 0;
				cashback = orderBean.getCashback();
	    //混批折扣按照比例来算
				discount_amount = price/(price+price_split)*orderBean.getDiscount_amount();
			    double split_diacount_amout =  orderBean.getDiscount_amount() - discount_amount;
		//pay_price_tow已支付运费，
		double pay_price_tow = 0;
		if(Utility.getStringIsNull(orderBean.getPay_price_tow()) && !"0".equals(orderBean.getPay_price_tow())){
			pay_price_tow = freight;
		}
		
			
		//现订单支付费用
		double pay_price = price + pay_price_tow - discount_amount - credit+service-cashback+extra_freight-share_discount - extra_discount-coupon_discount;//减去10美元优惠的201611.3 减去分享折扣17.1.6 减去手动优惠的1.19
		double remaining_price = 0;
		if(pay_price > orderBean.getPay_price()){
			remaining_price = pay_price - orderBean.getPay_price();
			pay_price = orderBean.getPay_price();
		}
		//pay_price_three余额抵扣
		double pay_price_three = Double.parseDouble((Utility.getStringIsNull(orderBean.getPay_price_three())?orderBean.getPay_price_three():"0"));
		double pay_price_three_split = 0;
		if(pay_price_three != 0){
			if(pay_price < pay_price_three){
				pay_price_three_split = pay_price_three - pay_price;
				pay_price_three = pay_price;
			}
			 
		}
		
		//生成另一个采购中订单
		//修改已有货源订单详情的订单号
		String orderNew = null ;
		orderNew = OrderInfoUtil.getNewOrderNo(orderNo, orderBean, 0, 0);

		int isPurchase = 0;
		//拆完后原订单数量和入库数量相等，则将原订单信息改为入库，
		if (odIds_.length + splitMap.size() == odstate && orderBean.getState() == 2) {
			isPurchase = 2;
		} else {
		//拆完后原订单数量和采购数量相等并且原订单为确认价格中，则将原订单信息改为采购中 
		//只要有一个商品处于采购中，则当前订单的状态为采购中
			if ( purchase_state>0 && orderBean.getState() == 5) {
				isPurchase = 1;
			} else if(purchase_state <= 0 ){
				isPurchase = 5;
			}
		}
		OrderBean orderBean2 = new OrderBean();
		int new_purchase_num = 0; // 拆出的新单中已采购的数量
		String tempTotalSplit = splitId + selSplitArr;
		String totalSplit = (tempTotalSplit).substring(0,tempTotalSplit.length()-1);
		List<Integer> list_new = dao.getOrderDetailPurchaseByOdids(totalSplit);
		for (int i = 0; i < list_new.size(); i++) {
			//如果商品的状态已经开始采购，计数累计
			if(list_new.get(i)>=3){
				new_purchase_num++;
			}
		}
		
		//当前拆分出来的订单状态判断
		String[] odids1 = tempTotalSplit.split(",");   
		List<OrderProductSource>  list1= dao.getOrderProductSource(orderNo, odids1);
		int OrderProductSourceBuycountOne = 0;
		int OrderProductSourceUsecountOne = 0;
		int purchase_state_0One = 0;//状态为5 审核中
		int purchase_state_1One = 0;//状态为-购买中1
		int purchase_state_2One = 0;//状态为-已到仓库2
		int purchase_state_3One = 0;//状态为-出运中3
		for (int j = 0; j < list1.size(); j++) {
			OrderProductSource orderVoOne =  list1.get(j);
			OrderProductSourceBuycountOne += orderVoOne.getBuycount();
			OrderProductSourceUsecountOne += orderVoOne.getUsecount();
			if(OrderProductSourceBuycountOne >= OrderProductSourceUsecountOne){
				//部分商品采购后又取消采购，此时purchaseState状态为确认货源状态(1), 所以新订单的状态为 审核中(5)
				if(orderVoOne.getPurchaseState() == 1 || orderVoOne.getPurchaseState()== 2 || orderVoOne.getPurchaseState()== 12 || orderVoOne.getPurchaseState() == 0){
					purchase_state_0One +=1;
				}
				if( orderVoOne.getPurchaseState() == 3){//购买中
					purchase_state_1One +=1;
				}
				if(orderVoOne.getPurchaseState() ==4 || orderVoOne.getPurchaseState() == 6){//入库
					purchase_state_2One +=1;
				}
				if(orderVoOne.getPurchaseState() ==8){//出运
					purchase_state_3One +=1;
				}
			}
		}
		//确认货源的数量不为0且确认货源的数量和录入货源的数量一致或者货源链接的数量为0
		if(purchase_state_0One != 0 && purchase_state_0One == list1.size() || list1.size() == 0){
			orderBean2.setState(5);
		// 确认价格的货源数量不为0且确认货源的数量>=1或者确认价格的数量和拆分出来的数量一致
		}else if(purchase_state_1One != 0 && purchase_state_1One >= 1 || purchase_state_1One == odids1.length){
			orderBean2.setState(1);
		//入库数量不为0且入库数量和货源数量一直且和拆分出来的数量一致
		}else if(purchase_state_2One != 0 && purchase_state_2One == list1.size() && purchase_state_2One == odids1.length){
			orderBean2.setState(2);
		//出运中数量不为0且出运数量和货源数量一直且和拆分出来的数量一直
		}else if(purchase_state_3One != 0 && purchase_state_3One == list1.size() && purchase_state_3One == odids1.length){
			orderBean2.setState(3);
		}else{
			//如果不符合以上情况，则使用主单的初始状态
//			orderBean2.setState(orderBean.getState());
			orderBean2.setState(initState); 
		}
		
		orderBean2.setMode_transport(orderBean.getMode_transport());
		orderBean2.setIp(orderBean.getIp());
		orderBean2.setUserid(orderBean.getUserid());
		orderBean2.setDeliveryTime(orderBean.getDeliveryTime());
		orderBean2.setCurrency(orderBean.getCurrency());
		orderBean2.setOrderNo(orderNew);
		orderBean2.setProduct_cost(new BigDecimal(price_split).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		double new_split_pay_price = new BigDecimal(orderBean.getPay_price() - pay_price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		orderBean2.setPay_price( state == 1 ? new_split_pay_price : 0.0);
		String foreign =  (state == 1 ? new BigDecimal(Double.parseDouble(orderBean.getForeign_freight()) - freight).setScale(2, BigDecimal.ROUND_HALF_UP) : 0.0).toString();
		orderBean2.setForeign_freight(foreign);
		orderBean2.setActual_ffreight(foreign);
		orderBean2.setOrder_ac(new BigDecimal(orderBean.getOrder_ac() - credit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean2.setDiscount_amount(new BigDecimal(split_diacount_amout).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean2.setShare_discount(new BigDecimal(orderBean.getShare_discount() - share_discount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean2.setExtra_discount(new BigDecimal(orderBean.getExtra_discount() - extra_discount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean2.setCoupon_discount(new BigDecimal(orderBean.getCoupon_discount() - coupon_discount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean2.setDetails_number(details_number);
		orderBean2.setServer_update(orderBean.getServer_update());
		orderBean2.setClient_update(orderBean.getClient_update());
		if(Utility.getIsDouble(orderBean.getPay_price_tow())){
			orderBean2.setPay_price_tow(new BigDecimal(Double.parseDouble(orderBean.getPay_price_tow()) - pay_price_tow).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		}
		orderBean2.setState(state == 1 ? orderBean2.getState() : -1);
		orderBean2.setPay_price_three( state == 1 ? new BigDecimal(pay_price_three_split).setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.0");
//		orderBean2.setPurchase_number(purchase_state_o - purchase_state);
		orderBean2.setPurchase_number(new_purchase_num);
		orderBean2.setCreatetime(orderBean.getCreatetime());
		orderBean2.setRemaining_price(remaining_price_split-remaining_price);
		orderBean2.setPackage_style(orderBean.getPackage_style());
		orderBean2.setExtra_freight(new BigDecimal(orderBean.getExtra_freight() - extra_freight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//获取拆分后的加急运费--cjc20161105
		
		//修改当前订单表
		orderBean.setProduct_cost(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//当前订单剩余的产品费用
		orderBean.setPay_price(new BigDecimal(pay_price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//带服务费
		orderBean.setForeign_freight(new BigDecimal(freight).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		orderBean.setOrder_ac(new BigDecimal(credit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean.setDiscount_amount(new BigDecimal(discount_amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());  
		orderBean.setShare_discount(new BigDecimal(share_discount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean.setExtra_discount(new BigDecimal(extra_discount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		orderBean.setCoupon_discount(new BigDecimal(coupon_discount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		/*if(Utility.getIsDouble(orderBean.getService_fee())){
			orderBean.setService_fee(new BigDecimal(Double.parseDouble(orderBean.getService_fee()) - service).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		}*/
		orderBean.setDetails_number(orderBean.getDetails_number() - details_number);
		orderBean.setPurchase_number(0);
		orderBean.setActual_ffreight(new BigDecimal(freight).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		orderBean.setPay_price_three(new BigDecimal(pay_price_three).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//余额抵扣
		orderBean.setExtra_freight(new BigDecimal(extra_freight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//获取拆分后的加急运费--cjc20161105
		if(Utility.getIsDouble(orderBean.getPay_price_tow())){
			orderBean.setPay_price_tow(new BigDecimal(pay_price_tow).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		}
		orderBean.setRemaining_price(remaining_price);
		orderBean.setPurchase_number(purchase_state);
		//如果未勾选商品采购状态为3的数量和未勾选商品的odid数量一样，表示所有未勾选商品都是采购中  更改 只要1个商品为采购中，当前订单的状态就为采购中。
		if( pstate3 > 0 || (pstate4 > 0 && pstate4 != odIds_.length)){
			orderBean.setState(1);
		}else if(pstate4==odIds_.length){//如果未勾选商品采购状态为4的数量和未勾选商品的odid数量一样，表示所有未勾选商品都是到库
			orderBean.setState(2);
		}else{
			orderBean.setState(isPurchase);//如果上面的条件都不满足，就是5
		}
		Payment pay = new Payment();
		pay.setUserid(orderBean.getUserid());// 添加用户id
		pay.setOrderid(orderNew);// 添加订单id
		pay.setOrderdesc("order split");// 添加订单描述
		pay.setPaystatus(1);// 添加付款状态
		pay.setPaymentid("");// 添加付款流水号（paypal返回的）
		pay.setPayment_amount((float)new_split_pay_price);// 添加付款金额（paypal返回的）
		pay.setPayment_cc(orderBean.getCurrency());// 添加付款币种（paypal返回的）
		pay.setPaySID("");
		pay.setPayflag("O");
		pay.setPaytype("3");
		double order_ac = 0;
		double balance_pay = 0;
		if(state == 0){
			//查询用户余额和货币值 错的注释
			order_ac = new BigDecimal((orderBean.getOrder_ac() - credit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			balance_pay = new_split_pay_price;
		}
		/*************************记录新订单的拆分日志 start ********************************/
		String new_info_log = ", product_cost="+orderBean2.getProduct_cost()+",pay_price="+orderBean2.getPay_price()
									+",pay_price_tow="+orderBean2.getPay_price_tow()+",pay_price_three="+orderBean2.getPay_price_three()
										+",actual_ffreight="+orderBean2.getActual_ffreight()+",service_fee="+orderBean2.getService_fee()
											+",order_ac="+orderBean2.getOrder_ac()+",discount_amount="+orderBean2.getDiscount_amount()
												+",cashback="+orderBean2.getCashback()+",extra_freight="+orderBean2.getExtra_freight()
													+",share_discount="+orderBean2.getShare_discount()+",Coupon_discount="+orderBean2.getCoupon_discount()+",extra_discount="+orderBean2.getExtra_discount()+"state="+orderBean2.getState();//--cjc 2017.5.18
		//记录老拆分日志
		info_log = "order_New:" + orderNew + info_log;
		dao.addOrderInfoLog(orderBean.getOrderNo(), info_log);
		new_info_log = "order_New:" + orderNew + new_info_log;
		dao.addOrderInfoLog(orderNew, new_info_log);
		/*************************记录新订单的拆分日志 end ********************************/
		//新增拆分订单 orderBean2--修改拆分后原订单orderinfo orderBean
		int res = dao.splitOrder(orderBean, orderBean2, splitId, selSplitArr, pay, state , order_ac, balance_pay, splitGid,adminUserName,orderBean.getDetails_pay(),updateOrderDetaildList,insertOrderDetaildList,selSplitGid);
		result = res > 0 ? orderNew : "";
		map.put("res", res > 0 ? orderNew : "");
		map.put("msg", res > 0 ? "拆分成功" : "拆分失败，拆分返回0");  
		} catch (Exception e) {
			map.put("res",   "");
			map.put("msg", "异常抛出");
			result = e.getMessage();
			e.printStackTrace();
			LOG.debug("OrderSplitServer-Exception:",e);
		}finally{
			return JSONArray.fromObject(map).toString();
		}
	}
	
	/**
     * Drop Ship 订单拆分功能
     */
	@SuppressWarnings({ "unused", "finally" })
	@Override
	@Transactional
	public Map<String, String> splitOrderShip(String orderNo, String odids, int userId,String state) {
		String result = "";
		int res=0;
		Map<String, String> map = new HashMap<String, String>();
		try {
		String[] goodsids = odids.split("@");//分隔选中的
		if(goodsids.length==0 || goodsids.equals("")){//判断是否选择拆分商品
			result = "请选择拆分商品";
			map.put("res", "");
			map.put("msg","请选择拆分商品");
			return map;
		}
		
		//判断该订单是否只有一个子订单，是不允许拆分，否允许拆分
		List<OrderDetailsBean> detailsBeanList=new ArrayList<OrderDetailsBean>();
		detailsBeanList=dao.getOrdersDetailsList(orderNo,0);
	    Set<String> strList=new HashSet<String>();
	    //遍历订单详情,将DropShipId添加到集合中
        for (int i = 0; i < detailsBeanList.size(); i++) {
        	strList.add(detailsBeanList.get(i).getDropshipid());
		}
        /*if(strList.size()==1){
        	result = "单个子订单不允许拆分";
			map.put("res", "");
			map.put("msg","单个子订单不允许拆分");
			return JSONArray.fromObject(map).toString();
        }*/
        
		//查询拆分订单详情,用来计算产品价格和数量(拆分)
		double split_product_cost = 0;//拆分产品价格
	    int split_product_num=0;//拆分产品的数量
	    double split_weight=0;//拆分产品的重量
	    int GoodsStatus=0; //拆分商品的状态 (采购状态),新订单的状态
	    OrderDetailsBean orderDetails=null;
		List<OrderDetailsBean> orderDetailsBeanList=new ArrayList<OrderDetailsBean>();
		for (int i = 0; i < goodsids.length; i++) {
			orderDetails=dao.getDropShipOrdersDetails(orderNo, goodsids[i]);
			if(orderDetails!=null){
				//计算拆分产品的价格
				double pricesum = orderDetails.getYourorder() * Double.parseDouble(orderDetails.getGoodsprice());
				split_product_cost += pricesum; 
				split_product_num ++; //拆分产品的数量
				//计算拆分产品的重量
				double weight = orderDetails.getOd_total_weight();
				split_weight+=weight;
				//选中商品的状态判断设置
				orderDetailsBeanList.add(orderDetails);
			}
		}
		
		//判断拆分的产品是否是同一个子订单类型类型，如果是则可以拆分，如果不是就不能拆分(重写)
		for (int i = 0; i < orderDetailsBeanList.size(); i++) {
			for (int j = 0; j < orderDetailsBeanList.size(); j++) {	
				//是否是同一个子订单类型类型
				if(!orderDetailsBeanList.get(i).getDropshipid().equals(orderDetailsBeanList.get(j).getDropshipid())){
					result = "请选择同一个子订单类型商品进行拆分";
					map.put("res", "0");
					map.put("msg","请选择同一个子订单内商品进行拆分");
					return map;
				}	
			}
		 }
		
		//查询dropshoporder记录信息,用来插入新的记录根据 dropshiporder.parent_order_no =orderinfo.order_no
		 List<Dropshiporder> dropshiporderList=dao.getDropShipOrderList(orderNo);
		String orderState = "-1";
		 //勾选的商品对应的子订单
		 String dropshipid=orderDetailsBeanList.get(0).getDropshipid();
		 //所有子订单取第一个
		 Dropshiporder dropshiporder=dropshiporderList.get(0);
		 //子订单数量加一
		 int dropshipNum=dropshiporderList.size()+1;
		 //新的子订单号
		 String newChildOrderNo=dropshiporder.getParentOrderNo()+"_"+dropshipNum;
		 //查询地址信息根据order_details.dropshipid查询地址信息并赋值到新的地址信息
		 //OrderAddress orderAdress=dao.getOrderAddressByOrderNo(dropshiporder.getChildOrderNo());
		 //根据勾选的商品所在子订单地址，添加新的订单地址
		 OrderAddress orderAdress=dao.getOrderAddressByOrderNo(dropshipid);
		 OrderAddress newOrderAddress=new OrderAddress();//新增的地址信息
		 newOrderAddress.setAddressId(orderAdress.getAddressId());
		 newOrderAddress.setOrderNo(newChildOrderNo);
		 newOrderAddress.setCountry(orderAdress.getCountry());
		 newOrderAddress.setStatename(orderAdress.getStatename());
		 newOrderAddress.setAddress(orderAdress.getAddress());
		 newOrderAddress.setAddress2(orderAdress.getAddress2());
		 newOrderAddress.setZipcode(orderAdress.getZipcode());
		 newOrderAddress.setPhoneNumber(orderAdress.getPhoneNumber());
		 newOrderAddress.setAdstatus(orderAdress.getAdstatus());
		 newOrderAddress.setStreet(orderAdress.getStreet());
		 newOrderAddress.setRecipients(orderAdress.getRecipients());
		 newOrderAddress.setFlag(orderAdress.getFlag());
		 dao.addOrderAddress(newOrderAddress);//添加成功地址返回id,插入到DropShip订单信息
		 //将插入成功的地址信息查询出来
		 OrderAddress orderAddr=dao.getOrderAddressByOrderNo(newChildOrderNo);
         //需要插入的参数变量声明
		 String newForeignFreight=""; //新的运费
		 String newPayPrice="";  //新订单支付价钱
		 String newPayPriceTow=""; //已支付的运费
		 double newActualWeightEstimate=0; //重量

         double totalWeight = 0;//整个订单的商品重量
         double weightRate =0;//计算取消订单的商总重量占整个订单商品的总重量占比


		 boolean flag=true;
		 Dropshiporder updateDropshiporder;
		 for (int i = 0; i < dropshiporderList.size(); i++) {
			 updateDropshiporder=new Dropshiporder();
			 Dropshiporder shiporder=dropshiporderList.get(i);
			 if(orderNo.equals(shiporder.getChildOrderNo())){
			 	orderState = shiporder.getState();
			 }
			 //更新dropshiporder的product_cost和details_Number
			 double no_split_product_cost=0;//拆分成功，剩余的产品价格
			 int no_split_product_num=0; //拆分成功,剩余的产品数量
			 double no_split_weight=0;//拆分成功，剩余的重量


			 for(int j=0;j<orderDetailsBeanList.size();j++){
				 OrderDetailsBean orderDetailsBean= orderDetailsBeanList.get(j);
				 //如果order_details.dropshipid=dropshiporder.child_order_no处理产品价格和数量
				 if(shiporder.getChildOrderNo().equals(orderDetailsBean.getDropshipid())){
					 double pricesum = orderDetailsBean.getYourorder() * Double.parseDouble(orderDetailsBean.getGoodsprice());
					 no_split_product_cost += pricesum; //拆分成功，剩余的产品价格
					 no_split_product_num ++; //拆分成功,剩余的产品数量
					 //计算拆分产品的重量
					 double weight = orderDetailsBean.getOd_total_weight();
					 no_split_weight+=weight;
					 flag=false;
				 }else{
					 flag=true; 
				 }
			 }
			 //重新获取未取消的商品
             detailsBeanList.clear();
             detailsBeanList=dao.getOrdersDetailsList(orderNo,1);
             for(OrderDetailsBean odBean : detailsBeanList){
                 totalWeight += odBean.getOd_total_weight();
             }

             //计算取消订单的商总重量占整个订单商品的总重量占比，方便计算运费
             weightRate = split_weight / totalWeight;
			 //FLAG 为true,外层循环子订单中不包含前台选中商品    对新订单参数的获取是否要拉到外层for循环之外？？？
			 /*if(!flag){
				 //新订单数据
				 //此处抛出数据java.lang.NumberFormatException: Infinite or NaN 异常  新运费
				 newForeignFreight=new BigDecimal(((new Double(shiporder.getForeignFreight())).doubleValue()*(no_split_weight/shiporder.getActualWeightEstimate()))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				 //新的应付额  产品总价 + 产品运费 
				 newPayPrice=new BigDecimal(no_split_product_cost+((new Double(shiporder.getForeignFreight())).doubleValue()*(no_split_weight/shiporder.getActualWeightEstimate()))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();  
				 newPayPriceTow=new BigDecimal(((new Double(shiporder.getForeignFreight())).doubleValue()*(no_split_weight/shiporder.getActualWeightEstimate()))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				 newActualWeightEstimate=new BigDecimal(shiporder.getActualWeightEstimate()-no_split_weight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			 }*/
			 if(!flag){
				 //原油订单数据
				 //计算拆单之后价格和数量并更新(原订单),产品总价为原来子订单总价减去拆单商品总价    shiporder.getProductCost()-no_split_product_cost;
				 updateDropshiporder.setProductCost(new BigDecimal((new Double(shiporder.getProductCost())).doubleValue()-no_split_product_cost).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				 updateDropshiporder.setDetailsNumber(shiporder.getDetailsNumber()-no_split_product_num);
				 //计算拆单后的重量运费并更新(原订单)(总的运费-(总重量 /拆分的重量 *总的运费))
				 //updateDropshiporder.setForeignFreight(new BigDecimal((new Double(shiporder.getForeignFreight())).doubleValue()-((new Double(shiporder.getForeignFreight())).doubleValue()*(no_split_weight/shiporder.getActualWeightEstimate()))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				 //计算支付的费用(运费+product_cost)
				 //updateDropshiporder.setPayPrice(new BigDecimal(new Double(shiporder.getProductCost())-no_split_product_cost+(new Double(shiporder.getForeignFreight())).doubleValue()-((new Double(shiporder.getForeignFreight())).doubleValue()*(no_split_weight/shiporder.getActualWeightEstimate()))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				 //计算 pay_price_tow=foreign_fright;
				 //updateDropshiporder.setPayPriceTow(new BigDecimal((new Double(shiporder.getForeignFreight())).doubleValue()-((new Double(shiporder.getForeignFreight())).doubleValue()*(no_split_weight/shiporder.getActualWeightEstimate()))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				 //计算原订单的剩余总重量(原订单)(总重量-拆分的重量)
				 //updateDropshiporder.setActualWeightEstimate(shiporder.getActualWeightEstimate()-no_split_weight);
				 updateDropshiporder.setChildOrderNo(shiporder.getChildOrderNo()); 
				 //更新原订单的状态
				 //updateDropshiporder.setState(String.valueOf(no_splitState));
			 }else{
				 //子订单没有包含前台选中商品，是不是可以不用更新？？？
				 updateDropshiporder.setProductCost(shiporder.getProductCost());
				 updateDropshiporder.setDetailsNumber(shiporder.getDetailsNumber());
				 updateDropshiporder.setForeignFreight(shiporder.getForeignFreight());
				 updateDropshiporder.setPayPrice(shiporder.getPayPrice());
				 updateDropshiporder.setPayPriceTow(shiporder.getPayPriceTow());
				 updateDropshiporder.setActualWeightEstimate(shiporder.getActualWeightEstimate());
				 updateDropshiporder.setChildOrderNo(shiporder.getChildOrderNo());
			 }
			 dao.updateDropShiporder(updateDropshiporder);
		 }
		 //添加DropShip订单信息(新增订单记录)
		 Dropshiporder newDropshiporder=new Dropshiporder();
		 newDropshiporder.setParentOrderNo(dropshiporder.getParentOrderNo());
		 newDropshiporder.setChildOrderNo(newChildOrderNo);
		 newDropshiporder.setUserId(userId);
		 newDropshiporder.setDeliveryTime(dropshiporder.getDeliveryTime());
		 newDropshiporder.setModeTransport(dropshiporder.getModeTransport());
		 newDropshiporder.setServiceFee(dropshiporder.getServiceFee());
		 //地址
		 //拆分的产品价格.doubleValue();
		 newDropshiporder.setAddressId(orderAddr.getId());
		 newDropshiporder.setProductCost(new BigDecimal((split_product_cost)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		 //拆分的运费
		 newDropshiporder.setForeignFreight(newForeignFreight);
		 //支付费用
		 newDropshiporder.setPayPrice(newPayPrice);
		 //已支付运费
		 newDropshiporder.setPayPriceTow(newPayPriceTow);
		 //拆分的重量
		 newDropshiporder.setActualWeightEstimate(split_weight);
		 
		 newDropshiporder.setDetailsNumber(split_product_num);//拆分产品数量
		 newDropshiporder.setPackagStyle(dropshiporder.getPackagStyle());
		 newDropshiporder.setCreateTime(dropshiporder.getCreateTime());
		 newDropshiporder.setServerUpdate(dropshiporder.getServerUpdate());
		 newDropshiporder.setClientUpdate(dropshiporder.getClientUpdate());
		 newDropshiporder.setActualAllincost(dropshiporder.getActualAllincost());
		 newDropshiporder.setIp(dropshiporder.getIp());
		 newDropshiporder.setRemainingPrice(dropshiporder.getRemainingPrice());
		 newDropshiporder.setActualVolume(dropshiporder.getActualVolume());
		 newDropshiporder.setActualWeight(dropshiporder.getActualWeight());
		 newDropshiporder.setActualWeightEstimate(dropshiporder.getActualWeightEstimate());
		 newDropshiporder.setCustomDiscussOther(dropshiporder.getCustomDiscussOther());
		 newDropshiporder.setCustomDiscussFright(dropshiporder.getCustomDiscussFright());
		 newDropshiporder.setTransportTime(dropshiporder.getTransportTime());
		 newDropshiporder.setExpectArriveTime(dropshiporder.getExpectArriveTime());
		 newDropshiporder.setArriveTime(dropshiporder.getArriveTime());
		 newDropshiporder.setState(dropshiporder.getState());
		 //newDropshiporder.setState(String.valueOf(splitState));//新生成订单的状态
		 newDropshiporder.setCancelObj(dropshiporder.getCancelObj());
		 newDropshiporder.setOrderAc(dropshiporder.getOrderAc());
		 newDropshiporder.setPurchaseNumber(dropshiporder.getPurchaseNumber());
		 newDropshiporder.setCurrency(dropshiporder.getCurrency());
		 newDropshiporder.setDiscountAmount(dropshiporder.getDiscountAmount());
		 newDropshiporder.setActualWeightEstimate(dropshiporder.getActualWeightEstimate());
		 newDropshiporder.setActualFreightC(dropshiporder.getActualFreightC());
		 newDropshiporder.setExtraFreight(dropshiporder.getExtraFreight());
		 newDropshiporder.setOrderShow(dropshiporder.getOrderShow());
		 newDropshiporder.setPurchaseDays(dropshiporder.getPurchaseDays());
		 newDropshiporder.setActualLwh(dropshiporder.getActualLwh());
		 newDropshiporder.setPackagNumber(dropshiporder.getPackagNumber());
		 newDropshiporder.setOrderpaytime(dropshiporder.getOrderpaytime());
		 newDropshiporder.setOrderremark(dropshiporder.getOrderremark());
		 newDropshiporder.setCashback(dropshiporder.getCashback());
		 dao.insertDropShiporder(newDropshiporder);//添加DropShipOrder记录信息
			 
		 //更新order_details dropshipid字段
		 OrderDetailsBean orderDetailsBean=new OrderDetailsBean();
		 for (int i = 0; i < goodsids.length; i++) {
			 orderDetailsBean.setDropshipid(newChildOrderNo);
			 orderDetailsBean.setOrderid(orderNo);
			 orderDetailsBean.setGoodsid(Integer.parseInt(goodsids[i]));
			 dao.updateOrderDetails(orderDetailsBean);
	     }
		 //调用存储过程来更新状态
		 //1.更新新订单的状态
		 dao.updateDropShipOrderStates(newChildOrderNo);
		 String oldDropId = orderDetailsBeanList.get(0).getDropshipid();
		 //2.更新原有订单的状态
		 res=dao.updateDropShipOrderStates(oldDropId);
		 //3.更新新老订单的重量，运费、商品总价和支付金额,母订单的状态
         IOrderDao odDao = new OrderDao();
         List<OrderBean>  orderBeans = odDao.getOrderInfo(userId,orderNo);
         float orderAc = Float.valueOf(String.valueOf(weightRate * orderBeans.get(0).getOrder_ac()));
         float extraFreight = Float.valueOf(String.valueOf(weightRate * orderBeans.get(0).getExtra_freight()));
		 dao.updateDropShipDate(oldDropId,newChildOrderNo,orderNo,orderAc,extraFreight);

		 map.put("orderNoNew", res > 0 ? oldDropId + "@" + newChildOrderNo : "");
		 map.put("res", "1");
		 map.put("msg", res > 0 ? "拆分成功" : "拆分失败，拆分返回0");

			// 判断是否是取消状态,是取消,则更新新订单的状态
			if ("0".equals(state)) {
				IOrderwsServer orderwsServer = new OrderwsServer();
				float totalPrice = 0;
				float weight = 0;
				//获取dropship订单相关的数据
				List<Dropshiporder> dropList = dao.getDropShipOrderList(orderNo);
				for (Dropshiporder child : dropList) {
					if (newChildOrderNo.equals(child.getChildOrderNo())) {
						totalPrice = Float.valueOf(child.getPayPrice());
						break;
					}
				}
				if (totalPrice > 0) {
                    weight = Float.valueOf(String.valueOf(split_weight));
					res = orderwsServer.closeDropshipOrder(userId, orderNo, newChildOrderNo, totalPrice,extraFreight, orderAc, weight);
					// 取消成功后,返回现金给客户,
					if (res > 0) {
						float actualPay = totalPrice;
						/*UserDao dao = new UserDaoImpl();
						float order_ac = new BigDecimal(extraFreight + orderAc).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
						dao.updateUserAvailable(userId,
								new BigDecimal(actualPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
								" system closeOrder:" + orderNo + "@" + newChildOrderNo, orderNo, null, 0, order_ac, 1);
						*/
						// 走审批流程
						OrderCancelApproval cancelApproval = new OrderCancelApproval();
						cancelApproval.setUserId(userId);
						cancelApproval.setOrderNo(orderNo);
						cancelApproval.setPayPrice(actualPay);
						cancelApproval.setType(2);
						cancelApproval.setDealState(0);
						cancelApproval.setOrderState(Integer.valueOf(orderState));
						NotifyToCustomerUtil.insertIntoOrderCancelApproval(cancelApproval);

						map.put("orderNoNew", res > 0 ? oldDropId + "@" + newChildOrderNo : "");
						map.put("res", "1");
						map.put("msg", "拆分并取消成功");

					} else {
						map.put("orderNoNew", res > 0 ? oldDropId + "@" + newChildOrderNo : "");
						map.put("res", "0");
						map.put("msg", "拆分成功，取消失败，请联系研发部");

					}
				} else {
					map.put("orderNoNew", res > 0 ? oldDropId + "@" + newChildOrderNo : "");
					map.put("res", "0");
					map.put("msg", "拆分成功，取消失败，请联系研发部");
				}

			}
//		//拆单取消后取消的商品如果有使用库存则还原库存
//		dao.cancelInventory(goodsids);
		} catch (Exception e) {
			map.put("msg", res > 0 ? "拆分成功" : "拆分失败，拆分返回0");
			map.put("res",   "");
			map.put("orderNoNew", "");
			result = e.getMessage();
			e.printStackTrace();
			LOG.debug("OrderSplitServer-Exception:",e);
		}finally{
			return map;
		}
	}
	
	@Override
	public List<OrderBean> getSplitOrder(String[] orderNos) {
		return dao.getOrders(orderNos);
	}
	@Override
	public List<Object[]> getSplitOrderDetails(String[] orderNo) {
		List<Object[]> list = dao.getOrdersDetails(orderNo);
		return list;
	}

	@Override
	public String getUserEmailByUserName(int userId) {
		UserDao userDao = new UserDaoImpl();
		return userDao.getUserEmailByUserName("", userId);
	}

	@Override
	public List<Object[]> getMessage_error(String time, String endtime,
			int page) {
		return dao.getMessage_error(time, endtime, (page-1)*30, 30);
	}

	@Override
	public int getMessage_error(String time, String endtime) {
		return dao.getMessage_error(time, endtime);
	}

	@Override
	public List<Object[]> getOrderDetails(String[] orderNos) {
		return dao.getOrderDetails(orderNos);
	}
}
