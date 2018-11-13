package com.cbt.pay.servlet;

import com.cbt.bean.*;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.log.dao.SaveLogToMysql;
import com.cbt.pay.dao.IOrderDao;
import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.OrderDao;
import com.cbt.pay.dao.PaymentDao;
import com.cbt.pay.service.IOrderServer;
import com.cbt.pay.service.ISpidersServer;
import com.cbt.pay.service.OrderServer;
import com.cbt.pay.service.SpidersServer;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;
import com.cbt.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

public class OrderInfoNew extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(OrderInfoNew.class);

	// 添加新的订单
	public void addOrder(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String paySIDs = request.getParameter("paySID");
		int userid = 0;
		try {
			String[] userinfo = WebCookie.getUser(request);
			String username = "";// 用户名
			if (userinfo != null) {
				userid = Integer.parseInt(userinfo[0]);
				username = userinfo[1];
			} else {
				LOG.warn("没有userid");
			}
			PrintWriter out = response.getWriter();
			HttpSession session = request.getSession();
			String paras = null;// 得到request中的JSON对象的字符流
			int state = Integer.parseInt(request.getParameter("state"));
			String freeString = request.getParameter("free");
			String[] payString = paySIDs.split("@");
			String currency = request.getParameter("currency");
			String info = request.getParameter("info");
			Object ordersession = session.getAttribute(info);
			double sf = 0;
			String type = request.getParameter("type");
			String sum_w = null;	
			String sum_v = null;
			String express = null;
			String express_e = null;
			String express_s = null;
			double sf_e = 0;
			double sf_s = 0;
			String sum_v_ff_e = null;
			String sum_w_ff_e = null;
			String sum_v_ff_s = null;
			String sum_w_ff_s = null;
			String[] itemid_ = null;
			String[] itemid_e = null;
			String[] itemid_s = null;
			Address addressInfo = null;
			String itemid = "";
			if(ordersession != null){
				Map<String, String> mapx = (Map<String, String>) session.getAttribute(info);
				express_e = mapx.get("express_e");
				express_s = mapx.get("express_s");
				sf_e = Utility.getIsDouble(mapx.get("sf_e"))?Double.parseDouble(mapx.get("sf_e")):0;
				sf_s = Utility.getIsDouble(mapx.get("sf_s"))?Double.parseDouble(mapx.get("sf_s")):0;
				sum_v_ff_e = mapx.get("sum_v_ff_e");
				sum_w_ff_e = mapx.get("sum_w_ff_e");
				sum_v_ff_s = mapx.get("sum_v_ff_s");
				sum_w_ff_s = mapx.get("sum_w_ff_s");
				express = mapx.get("express");
				sf = Utility.getIsDouble(mapx.get("sf"))?Double.parseDouble(mapx.get("sf")):0;
				sum_w = mapx.get("sum_w");
				sum_v = mapx.get("sum_v");
				itemid = mapx.get("itemId").replace("@", ",");
				itemid_ = Utility.getStringIsNull(mapx.get("itemid"))?mapx.get("itemid").split("@"):null;
				itemid_e = Utility.getStringIsNull(mapx.get("itemid_e"))?mapx.get("itemid_e").split("@"):null;
				itemid_s = Utility.getStringIsNull(mapx.get("itemid_s"))?mapx.get("itemid_s").split("@"):null;
				if(Utility.getStringIsNull(mapx.get("addressInfo"))){
					try {
						 JSONObject jo = JSONObject.fromObject(mapx.get("addressInfo"));
						 addressInfo = (Address) JSONObject.toBean(jo, Address.class);
					} catch (Exception e) {
						LOG.warn(mapx.get("addressInfo"));
						LOG.debug("OrderInfoNew-addOrder-address Exception:userId:" + userid +paySIDs,e);
					}
				}
			}else{
				out.print(5);
				out.flush();
				out.close();
				return;
			}
			
			
			//获取用户中的货币
			String currency1 = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency1)){
				currency1 = "USD";
			}
			if(!currency1.equals(currency)){
				out.print(3);
				out.close();
				return;
			}
			String paySID = payString[1];// 得到request中的JSON对象的字符流
			String orderNo = payString[0];
			int free = 0;
			if (Utility.getStringIsNull(freeString)) {
				free = Integer.parseInt(freeString);
			}
			String ip = Utility.getIpAddress(request);
			
			float product_cost = 0;
			float product_cost_ = 0;
			float product_cost_e = 0;
			float product_cost_s = 0;
			float service_fee = 0;
			int maxtime_ = 0;
			int maxtime_e = 0;
			int maxtime_s = 0;
			IOrderServer os = new OrderServer();
			List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
			if (paras == null) {
				ISpidersServer is = new SpidersServer();
				spiderlist = is.getSelectedItem(userid,
						itemid, 1);
				paras = JSONArray.fromObject(spiderlist).toString();
			}
//			String[] itemids = itemid.split(",");
			LOG.warn("goods list count:" + spiderlist.size());
			LOG.warn("addorder userid:" + userid);
			boolean isBalance = false;//是否整体余额支付
			boolean isAll = true;
			if(Utility.getStringIsNull(type)){
				if(type.equals("0")){
					isAll = false;
				}
			}
			//增加行为日志
			Object  view_url_count_ = session.getAttribute("view_url_count");//页面计数
			int view_url_count = 1;
			if(view_url_count_ != null){
				view_url_count = Integer.parseInt(view_url_count_.toString())+1;
				session.setAttribute("view_url_count", view_url_count);
			}else{
				session.setAttribute("view_url_count", view_url_count);
			}
			LOG.warn("addorderInfo:" + orderNo+"@"+itemid);
			SaveLogToMysql.insert(userid, session.getId(), orderNo+"@"+itemid, "", ip, "", view_url_count,(type+"，pay for product|Pay for all"), request.getRequestURI(),"","");
			
			if (spiderlist.size() != 0) {
				List<OrderDetailsBean> odb = new ArrayList<OrderDetailsBean>();
//				List<Address> addresslist = new ArrayList<Address>();
				int addressid = 0;
				
				LOG.warn(userid);
				/*if (userid != 0) {
					addresslist = os.getUserAddr(userid);
					LOG.warn("address list count:" + addresslist.size());
				}*/
				JSONArray ja = JSONArray.fromObject(paras.replace("\r\n",
						"\\r\\n"));

				session.setAttribute("shopcar", null);
				
				//获取用户余额
				IUserServer userServer = new UserServer();
				double[] balance_ac = userServer.getBalance(userid);
				double order_ac = balance_ac[1];//用户剩余运费抵扣金额
				double balance_ = balance_ac[0];
				int product_fee = 1;
				
				//获取混批折扣率
				List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
				list_cd = Application.getClassDiscount(request);
				int details_number1 = 0, details_number2 = 0, details_number3 = 0;
				double extra_freight = 0;//额外运费金额
				for (int i = 0; i < ja.size(); i++) {
					JSONObject json = (JSONObject) ja.get(i);
					product_fee = json.getInt("freight_free");
					int deliverytime = 0;
					OrderDetailsBean temp = new OrderDetailsBean();
					temp.setGoodsid(json.getInt("id"));
					String delivery_time = json.getString("delivery_time").replaceAll(" ", "");
					if (json.getString("url").indexOf("taobao") != -1
							|| json.getString("url").indexOf("tmall") != -1) {
						deliverytime = 5;
						temp.setDelivery_time("5");
					} else if (delivery_time.equals("")) {
						temp.setDelivery_time("unkown lead time");
					} else {
						deliverytime = Integer.parseInt(delivery_time.indexOf("-") > -1 ? delivery_time.split("-")[1] : delivery_time);
						temp.setDelivery_time(delivery_time);
					}
					temp.setFreight_free(product_fee);
					temp.setCheckprice_fee(50);// 询价费用写死
					temp.setCheckproduct_fee(100);
					temp.setState(0);// 订单详细信息状态
					temp.setFileupload("/temp/pic");
					temp.setYourorder(json.getInt("number"));
					temp.setUserid(userid);
					temp.setGoodsname(json.getString("name"));
					temp.setGoodsprice(json.getString("price"));
					temp.setGoodsdata_id(Integer.parseInt(Utility
							.getString(json.getString("goodsdata_id"))));
					temp.setFreight("0");
					temp.setRemark(json.getString("remark"));
					temp.setGoods_class(json.getInt("goods_class"));
					temp.setGoods_url(json.getString("url"));
					temp.setGoods_img(json.getString("img_url"));
					temp.setGoods_type(json.getString("types"));
					temp.setGbulk_volume(json.getString("bulk_volume"));
					temp.setGtotal_weight(json.getString("total_weight"));
					double sum_price = json.getDouble("price") * json.getInt("number");
					if(itemid_ != null){
						for (int j = 0; j < itemid_.length; j++) {
							if(Integer.parseInt(itemid_[j]) == json.getInt("id")){
								temp.setOrderid(orderNo+"1");
								extra_freight += json.getDouble("extra_freight");
								temp.setExtra_freight(json.getDouble("extra_freight"));
								details_number1 ++;
								product_cost_ += sum_price;
								if (maxtime_ < deliverytime) {
									maxtime_ = deliverytime;
								}
								break;
							}
						}
					}
					if(itemid_e != null){
						for (int j = 0; j < itemid_e.length; j++) {
							if(Integer.parseInt(itemid_e[j]) == json.getInt("id")){
								temp.setOrderid(orderNo+"2");
								details_number2 ++;
								product_cost_e += sum_price;
								if (maxtime_e < deliverytime) {
									maxtime_e = deliverytime;
								}
								break;
							}
						}
					}
					if(itemid_s != null){
						for (int j = 0; j < itemid_s.length; j++) {
							if(Integer.parseInt(itemid_s[j]) == json.getInt("id")){
								temp.setOrderid(orderNo+"3");
								details_number3 ++;
								product_cost_s += sum_price;
								if (maxtime_s < deliverytime) {
									maxtime_s = deliverytime;
								}
								break;
							}
						}
					}
					product_cost += sum_price;
					odb.add(temp);

					Cookie cookie = WebCookie.getCookieByName(request,
							"expressType");
					// Object cookie = session.getAttribute("expressType");
					if (cookie != null) {
						String id = "";
						String val = cookie.getValue();
						id = "+" + json.getString("guId") + "+";
						if (val.indexOf(id) > -1) {
							val = val.replace(id, "");
						}
						// session.setAttribute("expressType",val);
						cookie.setValue(val);
						cookie.setMaxAge(3600 * 24);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
					for (int j = 0; j < list_cd.size(); j++) {
						ClassDiscount cd = list_cd.get(j);
						if(cd.getId() == json.getInt("goods_class") && json.getDouble("price") <= 150){
							cd.setSum_price( cd.getSum_price()+json.getDouble("price")*json.getInt("number"));
							break;
						}
					}
				}
				//当前生成页面为免邮支付，购物车改为非免邮。此时价格发生变化
				if(product_fee == 0 && free == 1){
					out.print(4);
					out.close();
					return;
				}
				os.add(odb);
				
				//计算总折扣金额
				double g_price = 0;
				for (int k = 0; k < list_cd.size(); k++) {
					ClassDiscount cd = list_cd.get(k);
					if(cd.getPrice() < cd.getSum_price()){
						g_price += cd.getSum_price()-cd.getSum_price()*cd.getDeposit_rate();
					}
				}
				
				List<OrderBean> oblist = new ArrayList<OrderBean>();
				double service_ = 0;
				//2016-1-8 remove
				/*if(isAll){
					if(sf != 0){
						service_fee = product_cost_;
					}
					if(sf_e != 0){
						service_fee += product_cost_e;
					}
					if(sf_s != 0){
						service_fee += product_cost_s;
					}
					if (service_fee < 1000) {
						service_fee = (float) (service_fee * 0.08);
						service_ = 0.08;
					} else if (service_fee < 3000) {
						service_fee = (float) (service_fee * 0.06);
						service_ = 0.06;
					} else if (service_fee < 5000) {
						service_fee = (float) (service_fee * 0.04);
						service_ = 0.04;
					} else if (service_fee < 10000) {
						service_fee = (float) (service_fee * 0.03);
						service_ = 0.03;
					} else {
						service_fee = (float) (service_fee * 0.025);
						service_ = 0.025;
					}
				}*/
				
				double service_fee_ = service_fee;//剩余未分配服务费
				double discount_amount_ = g_price;//剩余未分配优惠折扣
				double order_ac_ = 0;
				List<Payment> pays = new ArrayList<Payment>();
    			String orderNos = ""; 
    			String country =  addressInfo == null ? "USA" : addressInfo.getCountryname();
				if(product_cost_ != 0){
					orderNos = orderNo + "1,";
					OrderBean ob = new OrderBean();
					ob.setPay_price_tow("0");
					ob.setIp(ip);
					ob.setUserid(userid);
					ob.setCurrency(currency);
					ob.setOrderNo(orderNo+"1");
					ob.setDetails_number(details_number1);
					double bl = product_cost_ / product_cost;
					ob.setDiscount_amount(g_price * bl);
					discount_amount_ = discount_amount_ - ob.getDiscount_amount();
					ob.setState(state);
					ob.setForeign_freight(isAll?(sf+""):"0");
					ob.setActual_ffreight(sf+"");
					ob.setProduct_cost(new BigDecimal(product_cost_).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					ob.setDeliveryTime(maxtime_);
					String mode_transport = express+"@"+sf+"@"+(isAll?"all":"product");
					ob.setService_fee("0");
					ob.setExtra_freight(extra_freight);
					if(isAll){
						if(sf != 0 ){
							if(sf_e != 0 || sf_s != 0 ){
								double s = new BigDecimal(product_cost_*service_).setScale(2,
										BigDecimal.ROUND_HALF_UP).doubleValue();
								service_fee_ -=  s;
								ob.setService_fee(s+"");
							}else{
								ob.setService_fee(new BigDecimal(service_fee).setScale(2,
										BigDecimal.ROUND_HALF_UP).toString());
							}
						}
						 
						if(order_ac > sf){
							order_ac = order_ac - sf;
							order_ac_ = sf;
							ob.setOrder_ac(sf);
						}else{
							order_ac_ = order_ac;
							ob.setOrder_ac(order_ac);
							order_ac = 0;
						}
					}else{
						order_ac = 0;
						ob.setOrder_ac(0);
					}
					ob.setMode_transport(mode_transport);
					
					ob.setActual_weight_estimate(Utility.getIsDouble(sum_w) ? Double.parseDouble(sum_w) : 0);
					ob.setActual_lwh(sum_v);
					oblist.add(ob);
				}
				if(product_cost_e != 0){
    				orderNos += orderNo + "2,";
					OrderBean ob = new OrderBean();
					ob.setPay_price_tow("0");
					ob.setIp(ip);
					ob.setUserid(userid);
					ob.setCurrency(currency);
					ob.setOrderNo(orderNo+"2");
					ob.setDetails_number(details_number2);
					if(product_cost_s != 0){
						double bl = product_cost_e / product_cost;
						ob.setDiscount_amount(g_price * bl);
						discount_amount_ = discount_amount_ - ob.getDiscount_amount();
					}else{
						ob.setDiscount_amount(discount_amount_);
					}
					ob.setState(state);
					ob.setForeign_freight(isAll?(sf_e+""):"0");
					ob.setActual_ffreight(sf_e+"");
					ob.setProduct_cost(new BigDecimal(product_cost_e).setScale(2,
							BigDecimal.ROUND_HALF_UP).toString());
					ob.setDeliveryTime(maxtime_e);
					
					double service_e = 0;
					ob.setService_fee("0");
					if(isAll){
						if(sf_e != 0 ){
							if(sf_s != 0 ){
								double s = new BigDecimal(product_cost_*service_).setScale(2,
										BigDecimal.ROUND_HALF_UP).doubleValue();
								service_fee_ -=  s;
								service_e = s;
								ob.setService_fee(s+"");
							}else{
								service_e = service_fee_;
								ob.setService_fee(new BigDecimal(service_fee_).setScale(2,
										BigDecimal.ROUND_HALF_UP).toString());
							}
						}
						if(order_ac > sf_e){
							order_ac = order_ac - sf_e;
							order_ac_ += sf_e;
							ob.setOrder_ac(sf_e);
						}else{
							order_ac_ += order_ac;
							ob.setOrder_ac(order_ac);
							order_ac = 0;
						}
					}
					String mode_transport = express_e+"@"+sf_e+"@"+((service_e==0)?"all":"product");
					if(mode_transport.indexOf("@@") > -1){
						if(Utility.getStringIsNull(express) && express.lastIndexOf("@") == 0){
							mode_transport = mode_transport.replace("@@", express + "@");
						}else{
							mode_transport = mode_transport.replace("@@", "@" + country + "@");
						}
					}
					ob.setMode_transport(mode_transport);
					
					ob.setActual_weight_estimate(Utility.getIsDouble(sum_w_ff_e) ? Double.parseDouble(sum_w_ff_e) : 0); 
					ob.setActual_lwh(sum_v_ff_e);
					oblist.add(ob);
				}
				if(product_cost_s != 0){
    				orderNos += orderNo + "3,";
					OrderBean ob = new OrderBean();
					ob.setPay_price_tow("0");
					ob.setIp(ip);
					ob.setUserid(userid);
					ob.setCurrency(currency);
					ob.setOrderNo(orderNo+"3");
					ob.setDetails_number(details_number3);
					ob.setDiscount_amount(discount_amount_);
					ob.setState(state);
					ob.setForeign_freight(isAll?(sf_s+""):"0");
					ob.setActual_ffreight(sf_s+"");
					ob.setProduct_cost(new BigDecimal(product_cost_s).setScale(2,
							BigDecimal.ROUND_HALF_UP).toString());
					ob.setDeliveryTime(maxtime_s);
					ob.setService_fee(!isAll ? new BigDecimal(service_fee_).setScale(2,
							BigDecimal.ROUND_HALF_UP).toString():"0");
					String mode_transport = express_s+"@"+sf_s+"@"+((service_fee_==0 )?"all":"product");
					if(mode_transport.indexOf("@@") > -1){
						if(Utility.getStringIsNull(express) && express.lastIndexOf("@") == 0){
							mode_transport = mode_transport.replace("@@", express + "@");
						}else{
							mode_transport = mode_transport.replace("@@", "@" + country + "@");
						}
					}
					if(isAll){
						if(order_ac > sf_s){
							order_ac = sf_s;
							order_ac_ += sf_s;
							ob.setOrder_ac(sf_s);
						}else{
							ob.setOrder_ac(order_ac);
							order_ac_ += order_ac;
							order_ac = 0;
						}
					}
					ob.setMode_transport(mode_transport);
					
					ob.setActual_weight_estimate(Utility.getIsDouble(sum_w_ff_s) ? Double.parseDouble(sum_w_ff_s) : 0);
					ob.setActual_lwh(sum_v_ff_s);
					oblist.add(ob);
				}
				if(!isAll){
					service_fee = 0;
					sf = 0;
					sf_e = 0;
					sf_s = 0;
				}
				BigDecimal b1 = new BigDecimal(product_cost + sf + sf_e +sf_s - g_price - order_ac_ + service_fee + extra_freight);
				double product_cost1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				IPaymentDao dao = new PaymentDao();

				if (addressInfo != null) {
					List<Map<String, Object>> orderAddressList = new ArrayList<Map<String,Object>>();
	    			for (int i = 0; i < oblist.size(); i++) {
							addressid = addressInfo.getId();
							Map<String, Object> orderAddressMap = new HashMap<String, Object>();
							orderAddressMap.put("addressid", addressInfo
									.getId());
							orderAddressMap.put("orderno", oblist.get(i).getOrderNo());
							orderAddressMap.put("country", addressInfo
									.getCountry());
							orderAddressMap.put("statename", addressInfo
									.getStatename());
							orderAddressMap.put("address", addressInfo
									.getAddress());
							orderAddressMap.put("address2", addressInfo
									.getAddress2());
							orderAddressMap.put("phoneNumber", addressInfo
									.getPhone_number());
							orderAddressMap.put("zipcode", addressInfo
									.getZip_code());
							orderAddressMap.put("street", addressInfo
									.getStreet());
							orderAddressMap.put("recipients", addressInfo
									.getRecipients());
							orderAddressList.add(orderAddressMap);
					}
					os.addOrderAddress(orderAddressList);
				}
				int res = 0;
				if(balance_ > product_cost1){
					double order_ac1 = 0;
					for (int i = 0; i < oblist.size(); i++) {
						oblist.get(i).setPay_price_tow(isAll ? oblist.get(i).getActual_ffreight():"0");
						BigDecimal pay_price = new BigDecimal(Double.parseDouble(oblist.get(i).getProduct_cost()) + Double.parseDouble(oblist.get(i).getForeign_freight()) + Double.parseDouble(oblist.get(i).getService_fee()) + oblist.get(i).getExtra_freight() - oblist.get(i).getDiscount_amount() - oblist.get(i).getOrder_ac()).setScale(2, BigDecimal.ROUND_HALF_UP);
						oblist.get(i).setPay_price_three(pay_price.toString());
						oblist.get(i).setPay_price(pay_price.doubleValue());
						oblist.get(i).setState(5);
						order_ac1 += oblist.get(i).getOrder_ac();
						//添加payment表
						Payment pay = new Payment();
						pay.setUserid(userid);// 添加用户id
						pay.setOrderid(oblist.get(i).getOrderNo());// 添加订单id
						pay.setOrderdesc("余额支付");// 添加订单描述
						pay.setPaystatus(1);// 添加付款状态
						pay.setPaymentid("");// 添加付款流水号（paypal返回的）
						pay.setPayment_amount(pay_price.floatValue());// 添加付款金额（paypal返回的）
						pay.setPayment_cc(currency);// 添加付款币种（paypal返回的）
						pay.setPaySID(paySID);
						pay.setPayflag("O");
						pay.setPaytype("2");
						pays.add(pay);
					}
					IPaymentDao paydao = new PaymentDao();
					paydao.addPayments(pays);
					//修改购物车状态
					int upgres = os.updateGoodscarState(itemid);
					Cookie cartNumber = WebCookie.getCookieByName(request,
							"cartNumber");
					if (cartNumber != null) {
						cartNumber.setValue((Integer.parseInt(cartNumber.getValue()) - upgres)+"");
						cartNumber.setMaxAge(3600 * 24 * 2);
						cartNumber.setPath("/");
						response.addCookie(cartNumber);
					}
					//添加余额变更记录
	    			RechargeRecord rr = new RechargeRecord();
	    			rr.setUserid(userid);
	    			rr.setPrice(product_cost1);
	    			
	    			rr.setRemark_id(orderNos);
	    			rr.setType(1);
	    			//余额抵扣
	    			rr.setRemark("Balance deduction order："+orderNos);
	    			rr.setUsesign(1);
	    			rr.setCurrency(currency);
	    			//ljj   修改日期  4.7
	    			dao.addRechargeRecord(rr);
	    			//修改用户余额表
//					userServer.upUserPrice(userid, -product_cost1);
					userServer.upUserPrice(userid, -product_cost1,-order_ac1);
					isBalance = true;
					res = 6;
				}
				os.addOrderInfo(oblist, addressid, odb.size());
				String paylogorderdesc = "付款时记录";
				dao.insertintoPaylog(userid, orderNos, paySID, (float)product_cost1,
						username, paylogorderdesc,balance_ > product_cost1 ? "2" : "1");// 付款时往paylog表中插入一条记录
				
				BigDecimal b = new BigDecimal(product_cost1);
				String amount = b.setScale(2, BigDecimal.ROUND_HALF_UP)
						.toString();
				String md1 = AppConfig.paypal_business + userid + orderNo
						+ amount;
				LOG.debug("-------md1:" + md1);
				String sign = Md5Util.encoder(md1);
				String custom = userid + "@" + paySID;
//				String formstr = Utility.getForm(username, orderNo, amount,
//						sign, custom, "O", 1,currency);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("payamount", product_cost1);
				map.put("orderid", orderNo);
				map.put("amount", amount);
				map.put("sign", sign);
				map.put("custom", custom);
				session.setAttribute("paymap", map);
				session.setAttribute("payamount", product_cost1);
				session.setAttribute("orderid", orderNo);
				os.initCheckData(orderNos);
				if(isBalance){
					ISpidersServer spidersServer = new SpidersServer();
					spidersServer.sendEmail(orderNos.substring(0,orderNos.length()-1),userid);
				}
				out.print( res);// 返回订单+订单重复支付的标志
				out.flush();
				out.close();
			} else {
				Map<String, Object> map = (Map<String, Object>) session.getAttribute("paymap");
				if(map != null){
					List<OrderDetailsBean> odblist = os.getOrder(userid, itemid);
					LOG.warn("order list count:" + odblist.size());
					String amount = (String) map.get("amount");
					String sign = map.get("sign").toString();
					String custom = map.get("custom").toString();
					String paylogorderdesc = "付款时记录";
					orderNo = odblist.get(0).getOrderid();
					String formstr = Utility.getForm(username, orderNo, amount,sign, custom, "O","1@0@"+orderNo,currency);
					product_cost = Float.parseFloat(os.getOrderInfo(userid, orderNo).get(0).getProduct_cost());
					IPaymentDao dao = new PaymentDao();
					dao.insertintoPaylog(userid, orderNo, paySID, product_cost,username, paylogorderdesc,"1");// 付款时往paylog表中插入一条记录
					List<OrderBean> oblist = os.getOrderInfo(userid, orderNo);
					if (oblist != null && oblist.size() > 0) {
						int status = oblist.get(0).getState();
						if (status == 5 || status == 3) {
							out.print("1");
							return;
						}
					}
					out.print(formstr);// 返回订单+订单重复支付的标志
					out.flush();
					out.close();
					LOG.warn(odblist.get(0).getOrderid());
				}else{
					
				}			
				LOG.warn("重复订单");
			}
		} catch (Exception e) {
			LOG.error("OrderInfoNew-addOrder Exception:userId:" + userid +paySIDs,e);
		}
	}
	
	//跳转到paypal支付
	public void toPaypal(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.warn("toPaypal start");
		String[] userinfo = WebCookie.getUser(request);
		String username = "";// 用户名
		int userid = 0;
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
			username = userinfo[1];
		} else {
			LOG.warn("没有userid");
		}
		PrintWriter out = response.getWriter();
		String currency = request.getParameter("currency");//货币单位
		String info = request.getParameter("info");//商品信息
		//获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		if(!currency.equals(currency1)){
			out.print(3);
			out.close();
			return;
		}
		String freeString = request.getParameter("free");
		String type = request.getParameter("type");
		int free = 0;
		if (Utility.getStringIsNull(freeString)) {
			free = Integer.parseInt(freeString);
		}
		HttpSession session = request.getSession();
		Object ordersession = session.getAttribute(info);
		String paras = null;// 得到request中的JSON对象的字符流
		String[] payString = request.getParameter("paySID").split("@");
		String paySID = payString[1];// 得到request中的JSON对象的字符流
		String orderNo = payString[0];
		String itemid = null;
		double sf = 0;
		double sf_e = 0;
		double sf_s = 0;
		String[] itemid_ = null;
		String[] itemid_e = null;
		String[] itemid_s = null;
		if(ordersession != null){
			Map<String, String> mapx = (Map<String, String>) session.getAttribute(info);
			sf_e = Utility.getIsDouble(mapx.get("sf_e"))?Double.parseDouble(mapx.get("sf_e")):0;
			sf_s = Utility.getIsDouble(mapx.get("sf_s"))?Double.parseDouble(mapx.get("sf_s")):0;
			sf = Utility.getIsDouble(mapx.get("sf"))?Double.parseDouble(mapx.get("sf")):0;
			itemid_ = Utility.getStringIsNull(mapx.get("itemid"))?mapx.get("itemid").split("@"):null;
			itemid_e = Utility.getStringIsNull(mapx.get("itemid_e"))?mapx.get("itemid_e").split("@"):null;
			itemid_s = Utility.getStringIsNull(mapx.get("itemid_s"))?mapx.get("itemid_s").split("@"):null;
			itemid = mapx.get("itemId").replace("@", ",");
		}else{
			out.print(5);
			out.close();
			return;
		}
		//获取session订单号
		LOG.warn("toPaypal orderNo:" + orderNo);
		List<Object[]> spiderlist = new ArrayList<Object[]>();
		if (paras == null) {
			ISpidersServer is = new SpidersServer();
			spiderlist = is.getSelectedItemPrice(userid, itemid, 1);
		}
		LOG.warn("toPaypal cartInfo:" + JSONArray.fromObject(spiderlist));
		float product_cost = 0;
		int product_fee = 1;
		//获取混批折扣率
		List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
		list_cd = Application.getClassDiscount(request);
//		int state = 0;//是否存在已支付商品
		double product_cost_ = 0,product_cost_e = 0,product_cost_s = 0;
		double extra_freight = 0;
		for (int i = 0; i < spiderlist.size(); i++) {
			Object[] obj = spiderlist.get(i);
			double sum_price = Integer.parseInt(obj[1].toString())
					* Double.parseDouble(obj[2].toString());
			product_fee = (Integer) obj[4];
			for (int j = 0; j < list_cd.size(); j++) {
				ClassDiscount cd = list_cd.get(j);
				if(cd.getId() == (Integer)obj[5] && Double.parseDouble(obj[2].toString()) <= 150){
					cd.setSum_price( cd.getSum_price()+sum_price);
					break;
				}
			}
			if(itemid_ != null){
				for (int j = 0; j < itemid_.length; j++) {
					if(Utility.getStringIsNull(itemid_[j])){
						if(Integer.parseInt(itemid_[j]) == Integer.parseInt(obj[0].toString())){
							product_cost_ += sum_price;
							extra_freight += Double.parseDouble(obj[6].toString());
							break;
						}
					}
				}
			}
			if(itemid_e != null){
				for (int j = 0; j < itemid_e.length; j++) {
					if(Utility.getStringIsNull(itemid_e[j])){
						if(Integer.parseInt(itemid_e[j]) == Integer.parseInt(obj[0].toString())){
							product_cost_e += sum_price;
							break;
						}
					}
				}
			}
			if(itemid_s != null){
				for (int j = 0; j < itemid_s.length; j++) {
					if(Utility.getStringIsNull(itemid_s[j])){
						if(Integer.parseInt(itemid_s[j]) == Integer.parseInt(obj[0].toString())){
							product_cost_s += sum_price;
							break;
						}
					}
				}
			}
			product_cost += sum_price;
		}
		
		double g_price = 0;
		for (int k = 0; k < list_cd.size(); k++) {
			ClassDiscount cd = list_cd.get(k);
			if(cd.getPrice() < cd.getSum_price()){
				g_price += cd.getSum_price()-cd.getSum_price()*cd.getDeposit_rate();
			}
		}
		
		//当前生成页面为免邮支付，购物车改为非免邮。此时价格发生变化
		if(product_fee == 0 && free == 1){
			out.print(4);
			out.close();
			return;
		}
		boolean isAll = true;
		if(Utility.getStringIsNull(type)){
			if(type.equals("0")){
				isAll = false;//只支付产品费用
			}
		}
		double service_fee = 0;
		//2016-1-8 remove
		/*if(sf != 0){
			service_fee = product_cost_;
		}
		if(sf_e != 0){
			service_fee += product_cost_e;
		}
		if(sf_s != 0){
			service_fee += product_cost_s;
		}
		if (service_fee < 1000) {
			service_fee = (float) (service_fee * 0.08);
		} else if (service_fee < 3000) {
			service_fee = (float) (service_fee * 0.06);
		} else if (service_fee < 5000) {
			service_fee = (float) (service_fee * 0.04);
		} else if (service_fee < 10000) {
			service_fee = (float) (service_fee * 0.03);
		} else {
			service_fee = (float) (service_fee * 0.025);
		}
		*/
		if(!isAll){
			service_fee = 0;
			sf = 0;
			sf_e = 0;
			sf_s = 0;
		}
		
		//获取用户余额
		IUserServer userServer = new UserServer();
		double[] balance_ac = userServer.getBalance(userid);
		double order_ac = balance_ac[1];
		double sf_ = sf + sf_e +sf_s;
		if(isAll){
			if(order_ac > sf_){
				order_ac = sf_;
			}
		}else{
			order_ac = 0;
		}
		double balance_ = balance_ac[0];
		String orderNos = "";
		if(product_cost_ != 0){
			orderNos = orderNo + "1,";
		}
		if(product_cost_e != 0){
			orderNos += orderNo + "2,";
		}if(product_cost_s != 0){
			orderNos += orderNo + "3,";
		}
		orderNos = orderNos.substring(0,orderNos.length()-1);
		BigDecimal b1 = new BigDecimal(product_cost + sf_ - g_price - order_ac + service_fee + extra_freight);
		double product_cost1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		String amount1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
				.toString();
		if(product_cost == 0){
			LOG.debug("Failed to pay:" + JSONArray.fromObject(spiderlist));
			out.print(0);
			out.flush();
			out.close();
			return;
		}else if(balance_ >= product_cost1){
			out.print("amount="+amount1+"&orderid="+orderNos+"&currency="+currency);
			out.flush();
			out.close();
			return;
		}
		
		BigDecimal b = new BigDecimal(product_cost1 - balance_);
		String amount = b.setScale(2, BigDecimal.ROUND_HALF_UP)
				.toString();
		String md1 = AppConfig.paypal_business + userid + orderNo
				+ amount;
		LOG.debug("-------md1:" + md1);
		LOG.debug("-------sf:" + sf);
		String sign = Md5Util.encoder(md1);
		String custom = userid + "@" + paySID;
		//查询购物车商品价格
		String formstr = Utility.getForm(username, orderNo, amount, sign, custom, "O", "1@"+order_ac  +"@" + orderNos , currency);
		out.print(formstr);
		out.flush();
		out.close();
	}
	
	//获取订单号
	public void getOrderNo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		IOrderDao dao = new OrderDao();
		String orderid = dao.getOrderNo();
		String orderNo = Utility.generateOrderNumber(Long.parseLong(orderid+ ""));
		PrintWriter out = response.getWriter();
		String paySID = UUID.randomUUID().toString();
		out.print(orderNo + "@" + paySID);
		out.flush();
		out.close();
	}
	/*
	 * 根据用户id和产品id得到商品信息 public void getOrder(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException {
	 * request.setCharacterEncoding("utf-8");
	 * response.setCharacterEncoding("utf-8");
	 * response.setContentType("text/html"); PrintWriter out =
	 * response.getWriter(); IOrderServer os = new OrderServer(); IOrderDao od =
	 * new OrderDao(); int userid =
	 * Integer.parseInt(request.getParameter("userid")); String
	 * itemid=request.getParameter("itemid"); //String orderid =
	 * od.getMaxOrderno(request.getParameter("userid"))+"";
	 * 
	 * List<OrderDetailsBean> orderlist = new ArrayList<OrderDetailsBean>();
	 * if(userid!=0){ orderlist = os.getOrder(userid, itemid); }else{
	 * LOG.warn("没有userid"); } out.print(orderlist);
	 * 
	 * LOG.warn("order.json:"+orderlist.toString()); }
	 */
	// 提交订单前判断用户是否登录
	public void loginCart(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userId");
		String parameter = request.getParameter("itemId");
		String flag_s = request.getParameter("flag");
		String freeShipping = request.getParameter("FreeShipping");
		int flag = Integer.parseInt(flag_s);
		String[] userinfo = WebCookie.getUser(request);
		if (userinfo == null) {
			flag = freeShipping != null ? 3 : flag;
			Cookie cookie = new Cookie("pageState", (flag + 4) + ":"
					+ parameter);
			cookie.setMaxAge(3600);
			cookie.setPath("/");
			response.addCookie(cookie);
			String exist = WebCookie.cookie(request, "userName");
			if (exist == null) {
				response.sendRedirect("cbt/register.jsp");
				return;
			}
			response.sendRedirect("cbt/geton.jsp");
		} else {
			if (flag == 1) {
				response.sendRedirect("paysServlet?action=getSelectedItem&className=RequireInfoServlet&userId="
						+ userid + "&itemId=" + parameter + "&state=1");
			} else if (flag == 0) {
				response.sendRedirect("cbt/address.jsp?userId=" + userid
						+ "&itemId=" + parameter
						+ (freeShipping != null ? "&FreeShipping=1" : ""));
			} else if (flag == 2) {
				response.sendRedirect("paysServlet?action=getSelectedItemrequiredinfo&className=RequireInfoServlet&userId="
						+ userid + "&itemId=" + parameter + "&state=1");
			}

		}
	}

	// 如果用户没有地址的话，添加新地址
	public void addAddress(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		// int userid=Integer.parseInt(request.getParameter("userid"));
		String[] userinfo = WebCookie.getUser(request);
		int userid = 0;
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
		} else {
			LOG.warn("没有userid");
		}
		IOrderServer os = new OrderServer();
		String recipients = request.getParameter("recipients");
		String address = request.getParameter("address");
		String country = request.getParameter("country");
		String zipcode = request.getParameter("zipcode");
		String phonenumber = request.getParameter("phonenumber");
		String address3 = request.getParameter("address3");
		String statename = request.getParameter("statename");
		Address add = new Address();
		add.setUserid(userid);
		add.setRecipients(recipients);
		add.setAddress(address);
		add.setCountry(country);
		add.setZip_code(zipcode);
		add.setPhone_number(phonenumber);
		add.setAddress2(address3);
		add.setStatename(statename);
		os.addAddress(add);
	}

	// 如果用户有地址，取相应的地址
	public void getUserAddress(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String free = request.getParameter("free");
		String goodsids = request.getParameter("itemid");
		PrintWriter out = response.getWriter();
		List<Address> addresslist = new ArrayList<Address>();
		IOrderServer os = new OrderServer();
		String total = "";
		String[] userinfo = WebCookie.getUser(request);
		int userid = 0;
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
		} else {
			out.print("1");
			return;
		}
		if (userid != 0) {
			addresslist = os.getUserAddr(userid);
		}
		// 获取对应商品的总价格
		if (free != null) {
			float totalPrice = os.getTotalPrice(goodsids);
			total = totalPrice + "";
		}

		int size = addresslist.size();
		IZoneServer zs = new ZoneServer();
		List<ZoneBean> zonelist = zs.getAllZone();
		List<StateName> statelist = zs.getStateName();
		StringBuffer sb = new StringBuffer();
		String countryoption = "";
		String stateoption = "";

		if (size > 0) {//
			sb.append("<span id=\"have\">Please confirm your address：<span style=\"color:red\" id=\"updatesuc\"></span></span>");
			sb.append("<table id=\"address_tab\"><tr>");
			for (int i = 0; i < zonelist.size(); i++) {
				if (zonelist.get(i).getId() == Integer.parseInt(addresslist
						.get(0).getCountry())) {
					countryoption += "<option value=\""
							+ zonelist.get(i).getId()
							+ "\" selected=\"selected\">"
							+ zonelist.get(i).getCountry() + "</option>";
				} else {
					countryoption += "<option value=\""
							+ zonelist.get(i).getId() + "\">"
							+ zonelist.get(i).getCountry() + "</option>";
				}

			}
			for (int i = 0; i < statelist.size(); i++) {
				if (statelist.get(i).getStatename()
						.equals(addresslist.get(0).getStatename())) {
					stateoption += "<option value=\""
							+ statelist.get(i).getStatename()
							+ "\" selected=\"selected\">"
							+ statelist.get(i).getStatename() + "</option>";
				} else {
					stateoption += "<option value=\""
							+ statelist.get(i).getStatename() + "\">"
							+ statelist.get(i).getStatename() + "</option>";
				}
			}
			sb.append("<tr><td>Recipients:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"address\" maxlength=\"50\" id=\"recipients\" value=\""
					+ addresslist.get(0).getRecipients() + "\"></td>");
			sb.append("<td><button id=\"updatebtn\" class=\"btn_greena\" onclick=\"updateaddress("
					+ addresslist.get(0).getId()
					+ ")\">update</button></td></tr>");
			
			sb.append("<td>Address:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"address\" maxlength=\"50\" id=\"address\" value=\""
					+ addresslist.get(0).getAddress() + "\"></td>");
			

			sb.append("<tr><td>City:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"address\" maxlength=\"50\" id=\"city\" value=\""
					+ addresslist.get(0).getAddress2() + "\"></td></tr>");

			sb.append("<tr>");
			sb.append("<td>Country:</td><td><select id=\"country\" onchange=\"change("
					+ addresslist.get(0).getCountry()
					+ ")\">"
					+ countryoption
					+ "</select></td>");
			sb.append("</tr>");

			sb.append("<tr id=\"statetr\">");
			sb.append("<td>State:</td>");
			sb.append("<td><select id=\"state\" style=\"width:212px\">"
					+ stateoption + "</select></td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>Zip Code:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"zipcode\" maxlength=\"50\" id=\"zipcode\" value=\""
					+ addresslist.get(0).getZip_code() + "\"></td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>Phone Number:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"phonenumber\" maxlength=\"50\" id=\"phonenumber\" value=\""
					+ addresslist.get(0).getPhone_number() + "\"></td>");
			sb.append("</tr>");
		} else {
			sb.append("<span id=\"nothave\">You don't have an address yet, please enter an valid shipping address:<br></span>");
			sb.append("<table id=\"address_tab\" ><tr>");
			for (int i = 0; i < zonelist.size(); i++) {
				if (zonelist.get(i).getId() == 36) {
					countryoption += "<option value=\""
							+ zonelist.get(i).getId()
							+ "\" selected=\"selected\">"
							+ zonelist.get(i).getCountry() + "</option>";
				} else {
					countryoption += "<option value=\""
							+ zonelist.get(i).getId() + "\">"
							+ zonelist.get(i).getCountry() + "</option>";
				}
			}
			for (int i = 0; i < statelist.size(); i++) {
				if (statelist.get(i).getStatename().equals("California")) {
					stateoption += "<option value=\""
							+ statelist.get(i).getStatename()
							+ "\" selected=\"selected\">"
							+ statelist.get(i).getStatename() + "</option>";
				} else {
					stateoption += "<option value=\""
							+ statelist.get(i).getStatename() + "\">"
							+ statelist.get(i).getStatename() + "</option>";
				}
			}

			sb.append("<tr><td>Recipients:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"address\" id=\"recipients\" maxlength=\"50\" value=\"\"></td></tr>");
			
			sb.append("<td>Address:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"address\" maxlength=\"50\" id=\"address\" value=\"\"></td>");
			sb.append("<td><button style=\"display:none\" id=\"cofirmbtn\" class=\"btn_greena\" onclick=\"toAddress()\">confirm</button></td>");
			sb.append("</tr>");

			sb.append("<tr><td>City:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"address\" maxlength=\"50\" id=\"city\" value=\"\"></td></tr>");

			sb.append("<tr>");
			sb.append("<td>Country:</td><td><select id=\"country\" onchange=\"change()\">"
					+ countryoption + "</select></td>");
			sb.append("</tr>");

			sb.append("<tr id=\"statetr\">");
			sb.append("<td>State:</td>");
			sb.append("<td><select id=\"state\" style=\"width:212px\">"
					+ stateoption + "</select></td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>Zip Code:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"zipcode\" maxlength=\"50\" id=\"zipcode\" value=\"\"></td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td>Phone Number:</td>");
			sb.append("<td><input type=\"text\" style=\"width:207px\" name=\"phonenumber\" maxlength=\"50\" id=\"phonenumber\" value=\"\"></td>");
			sb.append("</tr>");
		}
		if (size > 0) {
			out.print(sb + "@" + total + "@" + addresslist.get(0).getId() + "@"
					+ size);
		} else {
			out.print(sb + "@" + total + "@" + "" + "@" + size);
		}
		out.flush();
		out.close();
		// LOG.warn("address.json:"+addresslist.toString());
		// LOG.warn("zone.json:"+zonelist.toString());
		// LOG.warn("state.json:"+statelist.toString());
	}

	// 如果用户有地址，取相应的地址
	public void getUserAddressInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String free = request.getParameter("free");
		String goodsids = request.getParameter("itemid");
		PrintWriter out = response.getWriter();
		List<Address> addresslist = new ArrayList<Address>();
		IOrderServer os = new OrderServer();
		String total = "";
		String[] userinfo = WebCookie.getUser(request);
		int userid = 0;
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
		} else {
			out.print("1");
			return;
		}
		if (userid != 0) {
			addresslist = os.getUserAddr(userid);
		}
		// 获取对应商品的总价格
		if (free != null) {
			float totalPrice = os.getTotalPrice(goodsids);
			total = totalPrice + "";
		}

		IZoneServer zs = new ZoneServer();
		List<ZoneBean> zonelist = zs.getAllZone();
		List<StateName> statelist = zs.getStateName();
		request.setAttribute("addresslist", addresslist);
		request.setAttribute("code", "0");
		request.setAttribute("zonelist", zonelist);
		request.setAttribute("statelist", statelist);
		request.setAttribute("addresslist", addresslist);
		request.setAttribute("total", total);
		request.setAttribute("addresssize", addresslist.size());
		request.setAttribute("total", total);
		request.setAttribute("size", addresslist.size());
		
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("cbt/addressInfo.jsp");
		homeDispatcher.forward(request, response);
	}
	// 更新用户地址
	public void updateUserAddress(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		int id = 0;
		if (request.getParameter("id") != null) {
			id = Integer.parseInt(request.getParameter("id"));
		}
		String recipients = request.getParameter("recipients");
		String address = request.getParameter("address");
		String country = request.getParameter("country");
		String zipcode = request.getParameter("zipcode");
		String phonenumber = request.getParameter("phonenumber");
		String address2 = request.getParameter("address2");
		String statename = request.getParameter("statename");
		String street = request.getParameter("street");
		IOrderServer os = new OrderServer();
		System.out.println(id);
		os.updateUserAddress(id, address, country, phonenumber, zipcode,
				address2, statename, recipients, street);
	}

	/**
	 * ylm 修改个人中心的收货地址
	 */
	public void updateIndividualAddress(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		int userid = Integer.parseInt(request.getParameter("userid"));
		int id = Integer.parseInt(request.getParameter("id"));
		String address = request.getParameter("address");
		String country = request.getParameter("country");
		String zipcode = request.getParameter("zipcode");
		String phonenumber = request.getParameter("phonenumber");
		IOrderServer os = new OrderServer();
		int res = os.updateIndvidualAddress(userid, id, address, country,
				phonenumber, zipcode, "");
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	/*
	 * //添加一条订单记录 public void addOrderInfo(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException{
	 * request.setCharacterEncoding("utf-8");
	 * response.setCharacterEncoding("utf-8");
	 * response.setContentType("text/html"); int
	 * userid=Integer.parseInt(request.getParameter("userid")); String
	 * orderNo=request.getParameter("orderid"); String
	 * product_cost=request.getParameter("productcost"); int
	 * state=Integer.parseInt(request.getParameter("state")); int
	 * addressid=Integer.parseInt(request.getParameter("addressid")); int
	 * maxtime=Integer.parseInt(request.getParameter("maxtime"));
	 * List<OrderBean> oblist=new ArrayList<OrderBean>(); IOrderServer os = new
	 * OrderServer(); OrderBean ob = new OrderBean(); ob.setOrderNo(orderNo);
	 * ob.setUserid(userid); ob.setProduct_cost(product_cost);
	 * ob.setState(state); ob.setDeliveryTime(maxtime); oblist.add(ob);
	 * os.addOrderInfo(oblist,addressid); }
	 */

	// 获取用户订单详情记录
	public void getOrderInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		IOrderServer os = new OrderServer();
		IOrderDao od = new OrderDao();
		int userid = Integer.parseInt(request.getParameter("userid"));
		String orderid = od.getMaxOrderno(request.getParameter("userid")) + "";

		List<OrderBean> orderinfolist = new ArrayList<OrderBean>();
		if (userid != 0) {
			orderinfolist = os.getOrderInfo(userid, orderid);
		} else {
			LOG.warn("没有userid");
		}
		out.print(orderinfolist);
		out.flush();
		out.close();
		LOG.warn("orderinfo.json:" + orderinfolist.toString());
	}

	/*
	 * //付款后更新购物车状态 public void updateGoodscarStateAgain(HttpServletRequest
	 * request, HttpServletResponse response) throws ServletException,
	 * IOException{ request.setCharacterEncoding("utf-8");
	 * response.setCharacterEncoding("utf-8");
	 * response.setContentType("text/html"); IOrderServer os = new
	 * OrderServer(); int
	 * userid=Integer.parseInt(request.getParameter("userid")); String
	 * itemid=request.getParameter("itemid");
	 * os.updateGoodscarStateAgain(userid, itemid); }
	 */

	// homefurniture页面
	public void getHomefurnitureProduct(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		IOrderServer os = new OrderServer();
		PrintWriter out = response.getWriter();
		String catergory = request.getParameter("catergory");
		out.print(os.getHomefurnitureProduct(catergory));
		out.flush();
		out.close();
		// LOG.warn(os.getHomefurnitureProduct(catergory));
	}

	/*
	 * //更新购物车状态 public void updateGoodscarState(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException{
	 * request.setCharacterEncoding("utf-8");
	 * response.setCharacterEncoding("utf-8");
	 * response.setContentType("text/html"); IOrderServer os = new
	 * OrderServer(); ISpidersServer is = new SpidersServer(); int
	 * userid=Integer.parseInt(request.getParameter("userid")); String
	 * itemid=request.getParameter("itemid"); os.updateGoodscarState(userid,
	 * itemid); String sessionId = null; String[] userinfo =
	 * WebCookie.getUser(request); if(userinfo==null){ sessionId =
	 * this.getSessionId(request, response); }
	 * 
	 * Cookie cookie = WebCookie.getCookieByName(request, "cartNumber") ;
	 * if(cookie != null){
	 * cookie.setValue(is.getGoogs_carNum(userinfo==null?0:Integer
	 * .parseInt(userinfo[0]), sessionId)+""); cookie.setMaxAge(3600*24*2);
	 * cookie.setPath("/"); response.addCookie(cookie); } }
	 */

	// 更新订单状态
	public void updateOrderState(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String[] userinfo = WebCookie.getUser(request);
		int userid = 0;
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
		} else {
			LOG.warn("没有userid");
		}
		String orderid = request.getParameter("orderid");
		LOG.warn("地址加入Orderinfo:" + userid + " " + orderid);
		IOrderServer os = new OrderServer();
		os.updateOrderState(userid, orderid,"0");
	}

	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] c = request.getCookies();
		for (Cookie cookie2 : c) {
			if (cookie2.getName().equals("sessionId")) {
				return cookie2.getValue();
			}
		}
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		Cookie cookie = new Cookie("sessionId", sessionId);
		cookie.setMaxAge(3600 * 24 * 2);
		cookie.setPath("/");
		response.addCookie(cookie);
		return sessionId;
	}
	 
}
