package com.cbt.pay.servlet;

import com.cbt.bean.OrderBean;
import com.cbt.bean.Payment;
import com.cbt.bean.RechargeRecord;
import com.cbt.log.dao.SaveLogToMysql;
import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.PaymentDao;
import com.cbt.pay.service.*;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;
import com.cbt.processes.servlet.Currency;
import com.cbt.util.AppConfig;
import com.cbt.util.Md5Util;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

public class PaymentServerlet extends HttpServlet {
	private static final Log LOG = LogFactory.getLog(PaymentServerlet.class);
	/**
	 * wanyang 付款记录处理
	 */
	private static final long serialVersionUID = 1L;

	// 添加付款记录
	public void addPayment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		int userid = Integer.parseInt(request.getParameter("userid"));
		String orderid = request.getParameter("orderid");
		String orderdesc = request.getParameter("orderdesc");
		String username = request.getParameter("username");
		int paystatus = Integer.parseInt(request.getParameter("paystatus"));
		String paymentid = request.getParameter("paymentid");
		float payment_amount = Float.parseFloat(request
				.getParameter("payment_amount"));
		String payment_cc = request.getParameter("payment_cc");
		IPayServer ps = new PayServer();
		if (ps.getPayment(userid, orderid).getPaymentid() != null) {
			LOG.warn("重复付款记录");
		} else {
			Payment pay = new Payment();
			pay.setUserid(userid);// 添加用户id
			pay.setOrderid(orderid);// 添加订单id
			pay.setOrderdesc(orderdesc);// 添加订单描述
			pay.setUsername(username);// 添加用户名
			pay.setPaystatus(paystatus);// 添加付款状态
			pay.setPaymentid(paymentid);// 添加付款流水号（paypal返回的）
			pay.setPayment_amount(payment_amount);// 添加付款金额（paypal返回的）
			pay.setPayment_cc(payment_cc);// 添加付款币种（paypal返回的）
			ps.addPayment(pay);
			LOG.warn("新加付款记录");
		}
	}

	public void getPaypalPaymentDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		IPayServer ps = new PayServer();
		String orderid = request.getParameter("orderid");
		String[] userinfo = WebCookie.getUser(request);
		int userid = 0;
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
		} else {
			LOG.warn("没有userid");
		}
		PrintWriter out = response.getWriter();
		out.print("[" + ps.getPayment(userid, orderid) + "]");
		out.flush();
		out.close();
	}

	public void insertPaylog(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		IPaymentDao dao = new PaymentDao();
		String paySID = UUID.randomUUID().toString();
		String[] userinfo = WebCookie.getUser(request);
		String orderNo = request.getParameter("orderid");
		String status = request.getParameter("status");
		int userid = 0;
		String username = "";
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
			username = userinfo[1];
		} else {
			LOG.warn("没有userid");
		}
		IOrderServer os = new OrderServer();
		Map<String, String> result = new HashMap<String, String>();
		List<OrderBean> list = os.getOrderInfo(userid, orderNo);
		String amount = "99999999999.00";
		double credit = 0;
		if (list != null && list.size() > 0) {
			int obstatus = list.get(0).getState();
			if (obstatus == 5 || obstatus == 3) {
				out.print("1");
				return;
			}
			String paylogorderdesc = "付款时记录";
			String payflag = "O";
			if ("0".equals(status)) {
				//获取运费抵扣金额
				String foreign_freight_ = list.get(0).getForeign_freight();
				double foreign_freight = Utility.getIsDouble(foreign_freight_) ? Double.parseDouble(foreign_freight_) : 0;
				if(foreign_freight != 0){
					IUserServer userServer = new UserServer();
					double[] balance_credit = userServer.getBalance(userid);
					if(balance_credit[1] != 0){
						 //获取用户中的货币
						String currency1 = WebCookie.cookie(request, "currency");
						if(!Utility.getStringIsNull(currency1)){
							currency1 = "USD";
						}
						//获取汇率
						Map<String, Double> maphl = Currency.getMaphl(request);
						double exchange_rate = maphl.get(list.get(0).getCurrency())/maphl.get(currency1);
						credit = new BigDecimal(balance_credit[1]*exchange_rate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					}
				}
					if(foreign_freight < credit){
						credit = foreign_freight;
					}
				BigDecimal b = new BigDecimal(list.get(0).getProduct_cost());
				String service_fee_ = list.get(0).getService_fee();
				double service_fee = Utility.getIsDouble(service_fee_) ? Double.parseDouble(service_fee_) :0 ;
				amount = b.subtract(new BigDecimal(list.get(0).getDiscount_amount())).subtract(new BigDecimal(credit)).add(new BigDecimal(list.get(0).getForeign_freight())).add(new BigDecimal(service_fee)).setScale(2,   BigDecimal.ROUND_HALF_UP).toString();
			} else {
				payflag = "N";
				amount = String.valueOf(list.get(0).getRemaining_price());
			}
			if("Y".equals(status)){
				payflag = "Y";
			}
			BigDecimal b = new BigDecimal(amount);
			amount = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			dao.insertintoPaylog(userid, orderNo, paySID,
					b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
					username, paylogorderdesc,"1");
			String md = AppConfig.paypal_business + userid + orderNo + amount;
			LOG.debug("-------md:" + md);
			String sign = Md5Util.encoder(md);
			String custom = userid + "@" + paySID;
			String formstr = Utility.getForm(username, orderNo, amount, sign,
					custom, payflag,"0@" + credit + "@" + orderNo,list.get(0).getCurrency());
			result.put("code", "0");
			result.put("formstr", formstr);
			HttpSession session = request.getSession();
			String ip = Utility.getIpAddress(request);
			Object  view_url_count_ = session.getAttribute("view_url_count");//页面计数
			int view_url_count = 1;
			if(view_url_count_ != null){
				view_url_count = Integer.parseInt(view_url_count_.toString())+1;
			}
			session.setAttribute("view_url_count", view_url_count);
			SaveLogToMysql.insert(userid, session.getId(), orderNo+"@"+amount, "", ip, "", view_url_count,"pay", request.getRequestURI(),"","");
			out.print(JSONArray.fromObject(result).toString());
		} else {
			result.put("code", "1");
			result.put("msg", "illegal operation");
			out.print(JSONArray.fromObject(result).toString());
		}
		out.flush();
		out.close();
		
	}

	public void validatePayment1(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// 从 PayPal 出读取 POST 信息同时添加变量„cmd
		try {
			Enumeration en = request.getParameterNames();
			String str = "cmd=_notify-validate";
			IOrderServer os = new OrderServer();
			String str2 = "cmd=_notify-validate";
			String payflag = "";
			int state_balance = 0;//是否余额支付
			double order_ac = 0;//运费抵扣费用
			String orderNos = "";
			while (en.hasMoreElements()) {
				try {	
					String paramName = (String) en.nextElement();
					LOG.warn("--------------paramName:" + paramName);
					String paramValue = request.getParameter(paramName);
					LOG.warn("--------------paramValue:" + paramValue);
					str2 = str2 + "&" + paramName + "="
							+ URLEncoder.encode(paramValue, "utf-8");
					if (paramName.equals("custom") && paramValue.indexOf("@") > 0) {
						String[] custom = paramValue.split("@");
						payflag = custom[3];
							if(Utility.getStringIsNull(custom[5])){
								order_ac = Double.parseDouble(custom[5].toString());
								if(custom[4].equals("1")){
									state_balance = 1;
								}
							}
							if(custom.length == 7){
								orderNos = custom[6].toString();
							}
					}
					
			} catch (Exception e) {
					throw e;
				}
			}
			// 检查是否重复支付
			LOG.warn("--------------payflag:" + payflag);
			LOG.warn("--------------str:" + str);
			LOG.warn("--------------str2:" + str2);
			// System.out.println("paypal validate info:" + str);
			// 建议在此将接受到的信息 str 记录到日志文件中以确认是否收到 IPN 信息
			// 将信息 POST 回给 PayPal 进行验证
			// 设置 HTTP 的头信息
			// 在 Sandbox 情况下，设置：
			URL u = new URL(AppConfig.paypal_action);
			// 正式环境
			// URL u = new URL("https://www.paypal.com/cgi-bin/webscr");
			
			URLConnection uc = u.openConnection();
			uc.setDoOutput(true);
			uc.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.println(str2);
			pw.flush();
			pw.close();
			// 接受 PayPal 对 IPN 回发的回复信息
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			String res = in.readLine();
			LOG.warn("--------------res:" + res);
			in.close();
			// 将 POST 信息分配给本地变量，可以根据您的需要添加
			// 该付款明细所有变量可参考：

			// https://www.paypal.com/IntegrationCenter/ic_ipn-pdt-variable-reference.html
			String itemName = request.getParameter("item_name");// 商品名
																// --这里存入的是用户名
			String itemNumber = request.getParameter("item_number");// 购买数量
																	// --这里存入的是订单号
			String paymentStatus = request.getParameter("payment_status");// 交易状态
			String paymentDate = request.getParameter("payment_date");// 交易时间
			String paymentAmount = request.getParameter("mc_gross");// 交易钱数
			String paymentCurrency = request.getParameter("mc_currency");// 货币种类
			String txnId = request.getParameter("txn_id");// 交易id
			String receiverEmail = request.getParameter("receiver_email");// 收款人email
			String payerEmail = request.getParameter("payer_email");// 付款人email

			String address_status = request.getParameter("address_status");
			String residence_country = request
					.getParameter("residence_country");
			String address_country = request.getParameter("address_country");
			String address_city = request.getParameter("address_city");
			String address_country_code = request
					.getParameter("address_country_code");
			String address_state = request.getParameter("address_state");
			String address_name = request.getParameter("address_name");
			String address_street = request.getParameter("address_street");
			Map<String, String> ipnAddressMap = new HashMap<String, String>();
			ipnAddressMap.put("address_status", address_status);
			ipnAddressMap.put("residence_country", residence_country);
			ipnAddressMap.put("address_country", address_country);
			ipnAddressMap.put("address_city", address_city);
			ipnAddressMap.put("address_country_code", address_country_code);
			ipnAddressMap.put("address_state", address_state);
			ipnAddressMap.put("address_name", address_name);
			ipnAddressMap.put("address_street", address_street);
			ipnAddressMap.put("receiverEmail", receiverEmail);
			String ipnAddressJson = JSONArray.fromObject(ipnAddressMap).toString();
			int userid = 0;
			String paySID = "";
			if (request.getParameter("custom") != null
					&& request.getParameter("custom").indexOf("@") > 0) {
				String[] custom = request.getParameter("custom").split("@");
				userid = Integer.parseInt(custom[0]);// 付款人id
				paySID = custom[1];
			}
			int paybtype = 1;// 1:paypal

			if (res == null || res == "")
				res = "0";
			IPaymentDao payDao = new PaymentDao();
			Payment pay = new Payment();
			pay.setUserid(userid);// 添加用户id
			if (itemNumber != null) {
				pay.setOrderid(itemNumber);// 添加订单id
			}
			pay.setOrderdesc(payerEmail);// 添加订单描述
			pay.setUsername(itemName);// 添加用户名
			pay.setPaystatus(paymentStatus.equals("Completed") ? 3 : 2);// 添加付款状态
			pay.setPaymentid(txnId);// 添加付款流水号（paypal返回的）
			pay.setPayment_amount(Float.parseFloat(paymentAmount));// 添加付款金额（paypal返回的）
			pay.setPayment_cc(paymentCurrency);// 添加付款币种（paypal返回的）
			pay.setPaySID(paySID);
			if (payflag != null && payflag != "") {
				pay.setPayflag(payflag);
			} else {
				payflag = "U";
				pay.setPayflag(payflag);
			}
			int upgres = os.updateGoodscarState(userid, Utility.getStringIsNull(orderNos) ? orderNos : itemNumber);
			
			
			// 不管付款成功与否都往paylog插入数据
			payDao.insertintoPaylog(pay, paymentDate, paybtype);
			RechargeRecord rr = new RechargeRecord();
			rr.setUserid(userid);
			rr.setPrice(Double.parseDouble(paymentAmount));
			rr.setRemark_id(Utility.getStringIsNull(orderNos) ? orderNos : itemNumber);
			IPayServer ps = new PayServer();
			IUserServer userServer = new UserServer();
			int ordercount = ps.validateOrder(userid, Utility.getStringIsNull(orderNos) ? orderNos.split(",")[0] : itemNumber);
			LOG.warn("--------------itemNumber:" + itemNumber);
			LOG.warn("--------------paymentStatus:" + paymentStatus);
			LOG.warn("--------------ordercount:" + ordercount);
			if (paymentStatus.equals("Completed")) {
				pay.setPaystatus(1);
				BigDecimal b = new BigDecimal(paymentAmount);
				double mc_gross = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				if (ordercount == 0) {
					//获取用户余额
					double balance_ = 0;
					if(state_balance == 1){
						double[] balance_ac = userServer.getBalance(userid);
						balance_ = balance_ac[0];
						if(order_ac != 0){
							userServer.upUserPrice(userid, -balance_, -order_ac);
						}else{
							userServer.upUserPrice(userid, -balance_);
						}
						rr.setPrice(balance_);
						rr.setUsesign(1);
						rr.setRemark("余额抵扣订单");
						payDao.addRechargeRecord(rr);
						if(balance_ > 0){
							Payment pay1 = new Payment();
							pay1.setUserid(userid);// 添加用户id
							pay1.setOrderid(itemNumber);// 添加订单id
							pay1.setOrderdesc("余额支付");// 添加订单描述
							pay1.setPaystatus(1);// 添加付款状态
							pay1.setPaymentid("");// 添加付款流水号（paypal返回的）
							pay1.setPayment_amount((float) balance_);// 添加付款金额（paypal返回的）
							pay1.setPayment_cc(paymentCurrency);// 添加付款币种（paypal返回的）
							pay1.setPaySID(paySID);
							pay1.setPayflag("O");
							pay1.setPaytype("2");
							payDao.addPayment(pay1);
						}
					}
					
					BigDecimal b1 = new BigDecimal(mc_gross+balance_);
					double pryprice = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					os.updateOrderStatePayPrice(userid, itemNumber,balance_+"",
							String.valueOf(pryprice), ipnAddressJson,0);
					Cookie cartNumber = WebCookie.getCookieByName(request,
							"cartNumber");
					if (cartNumber != null) {
						cartNumber.setValue((Integer.parseInt(cartNumber.getValue()) - upgres)+"");
						cartNumber.setMaxAge(3600 * 24 * 2);
						cartNumber.setPath("/");
						response.addCookie(cartNumber);
					}
					HttpSession session = request.getSession();
					session.setAttribute("ordering", null);

				} else if (ordercount == 1) {
					List<OrderBean> list = os.getOrderInfo(userid, itemNumber);
					double payprice = 0.00;
					if (list != null && list.size() > 0) {
						payprice = list.get(0).getPay_price();
					}
					int paycount = ps.validateOrder2(userid, itemNumber,
							payflag);
					LOG.warn("--------------paycount:" + paycount);
					if (paycount == 1) {
						LOG.warn("--------------paypal重复支付成功,金额存入余额------------");
						userServer.upUserPrice(userid, mc_gross);
						rr.setRemark("paypal重复支付成功,金额存入余额");
						rr.setPrice(mc_gross);
						payDao.addRechargeRecord(rr);
						os.updateOrderPayPrice(userid, itemNumber,
								String.valueOf(payprice), ipnAddressJson);
					} else {
						LOG.warn("--------------paypal支付成功-----------");
						rr.setRemark("paypal支付成功");
						BigDecimal b1 = new BigDecimal(mc_gross+payprice);
						double pryprice = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						os.updateOrderPayPrice(userid, itemNumber,
								String.valueOf(pryprice),
								ipnAddressJson);
					}
				}
			} else {
				pay.setPaystatus(0);
				rr.setRemark("paypal支付失败");
			}
			pay.setPaytype("0");
			payDao.addPayment(pay);
			if (ordercount == 0) {
				ISpidersServer spiders = new SpidersServer();
				spiders.sendEmail(itemNumber, userid);
			}
			if (res.equals("VERIFIED")) {
				LOG.warn("---------------该订单号" + itemNumber + "支付成功");
				// 检查付款状态
				// 检查 txn_id 是否已经处理过
				// 检查 receiver_email 是否是您的 PayPal 账户中的 EMAIL 地址
				// 检查付款金额和货币单位是否正确
				// 处理其他数据，包括写数据库

			} else {
				// 非法信息，可以将此记录到您的日志文件中以备调查
				LOG.warn("---------------该订单号" + itemNumber + "支付成功，但支付信息存在问题");
			}
		} catch (Exception e) {
			LOG.warn("------------------paypal error-------------------");
			LOG.error("PAYPAL ERROR:",e);
		}
	}
	
	
	public void validatePayment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// 从 PayPal 出读取 POST 信息同时添加变量„cmd
		try {
			Enumeration en = request.getParameterNames();
			String str = "cmd=_notify-validate";
			String str2 = "cmd=_notify-validate";
			String payflag = "";
			int state_balance = 0;//是否余额支付
			double order_ac = 0;//运费抵扣费用
			String orderNos = null;
			while (en.hasMoreElements()) {
				try {	
					String paramName = (String) en.nextElement();
					LOG.warn("--------------paramName:" + paramName);
					String paramValue = request.getParameter(paramName);
					LOG.warn("--------------paramValue:" + paramValue);
					str2 = str2 + "&" + paramName + "="
							+ URLEncoder.encode(paramValue, "utf-8");
					if (paramName.equals("custom") && paramValue.indexOf("@") > 0) {
						String[] custom = paramValue.split("@");
						payflag = custom[3];
							if(Utility.getStringIsNull(custom[5])){
								order_ac = Double.parseDouble(custom[5].toString());
								if(custom[4].equals("1")){
									state_balance = 1;
								}
							}
							if(custom.length == 7){
								orderNos = custom[6].toString();
							}
					}
			} catch (Exception e) {
					throw e;
				}
			}
			if(Utility.getStringIsNull(orderNos) && orderNos.indexOf(",") > -1){
				this.pay(request, response, payflag, str, str2, orderNos, state_balance, order_ac);
			}else{
				this.pay1(request, response, payflag, str, str2, orderNos, state_balance, order_ac);
			}
		} catch (Exception e) {
			LOG.warn("------------------paypal error-------------------");
			LOG.error("PAYPAL ERROR:",e);
		}
	}
	
	//生成多个订单
	public void pay(HttpServletRequest request,
                    HttpServletResponse response, String payflag, String str, String str2, String orderNos, int state_balance, double order_ac) throws ServletException, IOException {
		IOrderServer os = new OrderServer();
		// 检查是否重复支付
		LOG.warn("--------------payflag:" + payflag);
		LOG.warn("--------------str:" + str);
		LOG.warn("--------------str2:" + str2);
		// System.out.println("paypal validate info:" + str);
		// 建议在此将接受到的信息 str 记录到日志文件中以确认是否收到 IPN 信息
		// 将信息 POST 回给 PayPal 进行验证
		// 设置 HTTP 的头信息
		// 在 Sandbox 情况下，设置：
		URL u = new URL(AppConfig.paypal_action);
		// 正式环境
		// URL u = new URL("https://www.paypal.com/cgi-bin/webscr");
		
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(str2);
		pw.close();
		// 接受 PayPal 对 IPN 回发的回复信息
		BufferedReader in = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));
		String res = in.readLine();
		LOG.warn("--------------res:" + res);
		in.close();
		// 将 POST 信息分配给本地变量，可以根据您的需要添加
		// 该付款明细所有变量可参考：

		// https://www.paypal.com/IntegrationCenter/ic_ipn-pdt-variable-reference.html
		String itemName = request.getParameter("item_name");// 商品名
															// --这里存入的是用户名
		String itemNumber = request.getParameter("item_number");// 购买数量
																// --这里存入的是订单号
		String paymentStatus = request.getParameter("payment_status");// 交易状态
		String paymentDate = request.getParameter("payment_date");// 交易时间
		String paymentAmount = request.getParameter("mc_gross");// 交易钱数
		String paymentCurrency = request.getParameter("mc_currency");// 货币种类
		String txnId = request.getParameter("txn_id");// 交易id
//		String receiverEmail = request.getParameter("receiver_email");// 收款人email
		String payerEmail = request.getParameter("payer_email");// 付款人email

		String address_status = request.getParameter("address_status");
		String residence_country = request
				.getParameter("residence_country");
		String address_country = request.getParameter("address_country");
		String address_city = request.getParameter("address_city");
		String address_country_code = request
				.getParameter("address_country_code");
		String address_state = request.getParameter("address_state");
		String address_name = request.getParameter("address_name");
		String address_street = request.getParameter("address_street");
		Map<String, String> ipnAddressMap = new HashMap<String, String>();
		ipnAddressMap.put("address_status", address_status);
		ipnAddressMap.put("residence_country", residence_country);
		ipnAddressMap.put("address_country", address_country);
		ipnAddressMap.put("address_city", address_city);
		ipnAddressMap.put("address_country_code", address_country_code);
		ipnAddressMap.put("address_state", address_state);
		ipnAddressMap.put("address_name", address_name);
		ipnAddressMap.put("address_street", address_street);
		ipnAddressMap.put("receiverEmail", payerEmail);
		String ipnAddressJson = JSONArray.fromObject(ipnAddressMap).toString();
		int userid = 0;
		String paySID = "";
		if (request.getParameter("custom") != null
				&& request.getParameter("custom").indexOf("@") > 0) {
			String[] custom = request.getParameter("custom").split("@");
			userid = Integer.parseInt(custom[0]);// 付款人id
			paySID = custom[1];
		}
		int paybtype = 1;// 1:paypal

		if (res == null || res == "")
			res = "0";
		IPaymentDao payDao = new PaymentDao();
		List<Payment> paysList = new ArrayList<Payment>();
		Payment pay = new Payment();
		pay.setUserid(userid);// 添加用户id
		if (itemNumber != null) {
			pay.setOrderid(itemNumber);// 添加订单id
		}
		pay.setOrderdesc(payerEmail);// 添加订单描述
		pay.setUsername(itemName);// 添加用户名
		pay.setPaystatus(paymentStatus.equals("Completed") ? 3 : 2);// 添加付款状态
		pay.setPaymentid(txnId);// 添加付款流水号（paypal返回的）
		pay.setPayment_cc(paymentCurrency);// 添加付款币种（paypal返回的）
		pay.setPaySID(paySID);
		if (payflag != null && payflag != "") {
			pay.setPayflag(payflag);
		} else {
			payflag = "U";
			pay.setPayflag(payflag);
		}
		int upgres = os.updateGoodscarState(userid, orderNos );
		
		
		// 不管付款成功与否都往paylog插入数据
		payDao.insertintoPaylog(pay, paymentDate, paybtype);
		RechargeRecord rr = new RechargeRecord();
		rr.setUserid(userid);
		double paymentAmount_ = Double.parseDouble(paymentAmount);
		rr.setPrice(paymentAmount_);
		rr.setRemark_id(orderNos);
		IPayServer ps = new PayServer();
		IUserServer userServer = new UserServer();
		int ordercount = ps.validateOrder(userid, orderNos.split(",")[0]);
		LOG.warn("--------------itemNumber:" + itemNumber);
		LOG.warn("--------------paymentStatus:" + paymentStatus);
		LOG.warn("--------------ordercount:" + ordercount);
		if (paymentStatus.equals("Completed")) {
			pay.setPaystatus(1);
			BigDecimal b = new BigDecimal(paymentAmount);
			double mc_gross = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			List<OrderBean> list = os.getOrderInfo(userid, orderNos);
			if (ordercount == 0) {
				//获取用户余额
				double balance_ = 0;
				double balance_sy = balance_;//扣掉剩余的用户余额
				if(state_balance == 1){
					double[] balance_ac = userServer.getBalance(userid);
					balance_ = balance_ac[0];
					if(order_ac != 0){
						userServer.upUserPrice(userid, -balance_, -order_ac);
					}else{
						userServer.upUserPrice(userid, -balance_);
					}
					rr.setPrice(balance_);
					rr.setUsesign(1);
					rr.setRemark("余额抵扣订单");
				}
				List<String[]> orderinfos = new ArrayList<String[]>();
				for (int i = 0; i < list.size(); i++) {
					OrderBean ob = list.get(i);
					double foreign_freight = Utility.getIsDouble(ob.getForeign_freight())? Double.parseDouble(ob.getForeign_freight()):0;
					BigDecimal pay_price = new BigDecimal(Double.parseDouble(ob.getProduct_cost()) + foreign_freight + Double.parseDouble(ob.getService_fee()) - ob.getDiscount_amount() - ob.getOrder_ac()).setScale(2, BigDecimal.ROUND_HALF_UP);
					
					if(i == 0){
						double payb= paymentAmount_+balance_;
						BigDecimal   bs   =   new   BigDecimal(payb);
						pay.setPayment_amount(bs.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue());// 添加付款金额（paypal返回的）
						pay.setPayment_other(new BigDecimal(paymentAmount_ + balance_ - pay_price.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
					}
						Payment pay1 = new Payment();
						pay1.setUserid(userid);// 添加用户id
						pay1.setOrderid(ob.getOrderNo());// 添加订单id
						pay1.setOrderdesc("合并支付");// 添加订单描述
						pay1.setPaystatus(1);// 添加付款状态
						pay1.setPaymentid("");// 添加付款流水号（paypal返回的）
						pay1.setPayment_amount(pay_price.floatValue());// 添加付款金额（paypal返回的）
						pay1.setPayment_cc(paymentCurrency);// 添加付款币种（paypal返回的）
						pay1.setPaySID(paySID);
						pay1.setPayflag("O");
						pay1.setPaytype("4");
						paysList.add(pay1);
					
					double pay_price_three = 0;
					if(balance_sy > 0){
						if(pay_price.doubleValue() >= balance_sy){
							pay_price_three = balance_sy;
							balance_sy = 0;
						}else{
							balance_sy = new BigDecimal(balance_sy - pay_price.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP)
									.doubleValue();;
							pay_price_three = pay_price.doubleValue();
						}
					}
					String[] orderinfo = {ob.getOrderNo(),pay_price.toString(),pay_price_three+""};
					orderinfos.add(orderinfo);
				}
				os.updateOrderStatePayPrice(userid, orderinfos, ipnAddressJson);
				Cookie cartNumber = WebCookie.getCookieByName(request,
						"cartNumber");
				if (cartNumber != null) {
					cartNumber.setValue((Integer.parseInt(cartNumber.getValue()) - upgres)+"");
					cartNumber.setMaxAge(3600 * 24 * 2);
					cartNumber.setPath("/");
					response.addCookie(cartNumber);
				}
				HttpSession session = request.getSession();
				session.setAttribute("ordering", null);

			} else if (ordercount == 1) {
				double payprice = 0.00;
				for (int i = 0; i < list.size(); i++) {
					payprice += list.get(0).getPay_price();
				}
				int paycount = ps.validateOrder2(userid, itemNumber,
						payflag);
				LOG.warn("--------------paycount:" + paycount);
				if (paycount == 1) {
					LOG.warn("--------------paypal重复支付成功,金额存入余额------------");
					userServer.upUserPrice(userid, mc_gross);
					rr.setRemark("paypal重复支付成功,金额存入余额");
					rr.setPrice(mc_gross);
					payDao.addRechargeRecord(rr);
					os.updateOrderPayPrice(userid, itemNumber,
							String.valueOf(payprice), ipnAddressJson);
				} else {
					LOG.warn("--------------paypal支付成功-----------");
					rr.setRemark("paypal支付成功");
					BigDecimal b1 = new BigDecimal(mc_gross+payprice);
					double pryprice = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					os.updateOrderPayPrice(userid, itemNumber,
							String.valueOf(pryprice),
							ipnAddressJson);
				}
			}
		} else {
			pay.setPaystatus(0);
			rr.setRemark("paypal支付失败");
		}
		pay.setPaytype("0");
		paysList.add(pay);
		payDao.addPayments(paysList);
		if (ordercount == 0) {
			ISpidersServer spiders = new SpidersServer();
			spiders.sendEmail(itemNumber, userid);
		}
		if (res.equals("VERIFIED")) {
			LOG.warn("---------------该订单号" + itemNumber + "支付成功");
			// 检查付款状态
			// 检查 txn_id 是否已经处理过
			// 检查 receiver_email 是否是您的 PayPal 账户中的 EMAIL 地址
			// 检查付款金额和货币单位是否正确
			// 处理其他数据，包括写数据库

		} else {
			// 非法信息，可以将此记录到您的日志文件中以备调查
			LOG.warn("---------------该订单号" + itemNumber + "支付成功，但支付信息存在问题");
		}
	}

	//生成一个订单
	public void pay1(HttpServletRequest request,
                     HttpServletResponse response, String payflag, String str, String str2, String itemNumber, int state_balance, double order_ac) throws ServletException, IOException {
		IOrderServer os = new OrderServer();
		// 检查是否重复支付
		LOG.warn("--------------payflag:" + payflag);
		LOG.warn("--------------str:" + str);
		LOG.warn("--------------str2:" + str2);
		// System.out.println("paypal validate info:" + str);
		// 建议在此将接受到的信息 str 记录到日志文件中以确认是否收到 IPN 信息
		// 将信息 POST 回给 PayPal 进行验证
		// 设置 HTTP 的头信息
		// 在 Sandbox 情况下，设置：
		URL u = new URL(AppConfig.paypal_action);
		// 正式环境
		// URL u = new URL("https://www.paypal.com/cgi-bin/webscr");
		
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(str2);
		pw.close();
		// 接受 PayPal 对 IPN 回发的回复信息
		BufferedReader in = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));
		String res = in.readLine();
		LOG.warn("--------------res:" + res);
		in.close();
		// 将 POST 信息分配给本地变量，可以根据您的需要添加
		// 该付款明细所有变量可参考：

		// https://www.paypal.com/IntegrationCenter/ic_ipn-pdt-variable-reference.html
		String itemName = request.getParameter("item_name");// 商品名
															// --这里存入的是用户名
		if(!Utility.getStringIsNull(itemNumber)){
			itemNumber = request.getParameter("item_number");// 购买数量
			// --这里存入的是订单号
		}
//		String itemNumber = request.getParameter("item_number");// 购买数量
																// --这里存入的是订单号
		String paymentStatus = request.getParameter("payment_status");// 交易状态
		String paymentDate = request.getParameter("payment_date");// 交易时间
		String paymentAmount = request.getParameter("mc_gross");// 交易钱数
		String paymentCurrency = request.getParameter("mc_currency");// 货币种类
		String txnId = request.getParameter("txn_id");// 交易id
		String receiverEmail = request.getParameter("receiver_email");// 收款人email
		String payerEmail = request.getParameter("payer_email");// 付款人email

		String address_status = request.getParameter("address_status");
		String residence_country = request
				.getParameter("residence_country");
		String address_country = request.getParameter("address_country");
		String address_city = request.getParameter("address_city");
		String address_country_code = request
				.getParameter("address_country_code");
		String address_state = request.getParameter("address_state");
		String address_name = request.getParameter("address_name");
		String address_street = request.getParameter("address_street");
		Map<String, String> ipnAddressMap = new HashMap<String, String>();
		ipnAddressMap.put("address_status", address_status);
		ipnAddressMap.put("residence_country", residence_country);
		ipnAddressMap.put("address_country", address_country);
		ipnAddressMap.put("address_city", address_city);
		ipnAddressMap.put("address_country_code", address_country_code);
		ipnAddressMap.put("address_state", address_state);
		ipnAddressMap.put("address_name", address_name);
		ipnAddressMap.put("address_street", address_street);
		ipnAddressMap.put("receiverEmail", receiverEmail);
		String ipnAddressJson = JSONArray.fromObject(ipnAddressMap).toString();
		int userid = 0;
		String paySID = "";
		if (request.getParameter("custom") != null
				&& request.getParameter("custom").indexOf("@") > 0) {
			String[] custom = request.getParameter("custom").split("@");
			userid = Integer.parseInt(custom[0]);// 付款人id
			paySID = custom[1];
		}
		int paybtype = 1;// 1:paypal

		if (res == null || res == "")
			res = "0";
		IPaymentDao payDao = new PaymentDao();
		Payment pay = new Payment();
		pay.setUserid(userid);// 添加用户id
		if (itemNumber != null) {
			pay.setOrderid(itemNumber);// 添加订单id
		}
		pay.setOrderdesc(payerEmail);// 添加订单描述
		pay.setUsername(itemName);// 添加用户名
		pay.setPaystatus(paymentStatus.equals("Completed") ? 3 : 2);// 添加付款状态
		pay.setPaymentid(txnId);// 添加付款流水号（paypal返回的）
		pay.setPayment_amount(Float.parseFloat(paymentAmount));// 添加付款金额（paypal返回的）
		pay.setPayment_cc(paymentCurrency);// 添加付款币种（paypal返回的）
		pay.setPaySID(paySID);
		if (payflag != null && payflag != "") {
			pay.setPayflag(payflag);
		} else {
			payflag = "U";
			pay.setPayflag(payflag);
		}
		int upgres = os.updateGoodscarState(userid, itemNumber);
		
		
		// 不管付款成功与否都往paylog插入数据
		payDao.insertintoPaylog(pay, paymentDate, paybtype);
		RechargeRecord rr = new RechargeRecord();
		rr.setUserid(userid);
		rr.setPrice(Double.parseDouble(paymentAmount));
		rr.setRemark_id(itemNumber);
		IPayServer ps = new PayServer();
		IUserServer userServer = new UserServer();
		int ordercount = ps.validateOrder(userid, itemNumber);
		LOG.warn("--------------itemNumber:" + itemNumber);
		LOG.warn("--------------paymentStatus:" + paymentStatus);
		LOG.warn("--------------ordercount:" + ordercount);
		if (paymentStatus.equals("Completed")) {
			pay.setPaystatus(1);
			BigDecimal b = new BigDecimal(paymentAmount);
			double mc_gross = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (ordercount == 0) {
				//获取用户余额
				double balance_ = 0;
				if(state_balance == 1){
					double[] balance_ac = userServer.getBalance(userid);
					balance_ = balance_ac[0];
					if(order_ac != 0){
						userServer.upUserPrice(userid, -balance_, -order_ac);
					}else{
						userServer.upUserPrice(userid, -balance_);
					}
					rr.setPrice(balance_);
					rr.setUsesign(1);
					rr.setRemark("余额抵扣订单");
					payDao.addRechargeRecord(rr);
					if(balance_ > 0){
						Payment pay1 = new Payment();
						pay1.setUserid(userid);// 添加用户id
						pay1.setOrderid(itemNumber);// 添加订单id
						pay1.setOrderdesc("余额支付");// 添加订单描述
						pay1.setPaystatus(1);// 添加付款状态
						pay1.setPaymentid("");// 添加付款流水号（paypal返回的）
						pay1.setPayment_amount((float) balance_);// 添加付款金额（paypal返回的）
						pay1.setPayment_cc(paymentCurrency);// 添加付款币种（paypal返回的）
						pay1.setPaySID(paySID);
						pay1.setPayflag("O");
						pay1.setPaytype("2");
						payDao.addPayment(pay1);
					}
				}else if(order_ac != 0){
					userServer.upUserPrice(userid, 0, -order_ac);
				}
				
				BigDecimal b1 = new BigDecimal(mc_gross+balance_);
				double pryprice = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				os.updateOrderStatePayPrice(userid, itemNumber,balance_+"",
						String.valueOf(pryprice), ipnAddressJson,order_ac);
				Cookie cartNumber = WebCookie.getCookieByName(request,
						"cartNumber");
				if (cartNumber != null) {
					cartNumber.setValue((Integer.parseInt(cartNumber.getValue()) - upgres)+"");
					cartNumber.setMaxAge(3600 * 24 * 2);
					cartNumber.setPath("/");
					response.addCookie(cartNumber);
				}
				HttpSession session = request.getSession();
				session.setAttribute("ordering", null);

			} else if (ordercount == 1) {
				List<OrderBean> list = os.getOrderInfo(userid, itemNumber);
				double payprice = 0.00;
				if (list != null && list.size() > 0) {
					payprice = list.get(0).getPay_price();
				}
				int paycount = ps.validateOrder2(userid, itemNumber,
						payflag);
				LOG.warn("--------------paycount:" + paycount);
				if (paycount == 1) {
					LOG.warn("--------------paypal重复支付成功,金额存入余额------------");
					userServer.upUserPrice(userid, mc_gross);
					rr.setRemark("paypal重复支付成功,金额存入余额");
					rr.setPrice(mc_gross);
					payDao.addRechargeRecord(rr);
					os.updateOrderPayPrice(userid, itemNumber,
							String.valueOf(payprice), ipnAddressJson);
				} else {
					LOG.warn("--------------paypal支付成功-----------");
					rr.setRemark("paypal支付成功");
					BigDecimal b1 = new BigDecimal(mc_gross+payprice);
					double pryprice = b1.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					os.updateOrderPayPrice(userid, itemNumber,
							String.valueOf(pryprice),
							ipnAddressJson);
				}
			}
		} else {
			pay.setPaystatus(0);
			rr.setRemark("paypal支付失败");
		}
		pay.setPaytype("0");
		payDao.addPayment(pay);
		if (ordercount == 0) {
			ISpidersServer spiders = new SpidersServer();
			spiders.sendEmail(itemNumber, userid);
		}
		if (res.equals("VERIFIED")) {
			LOG.warn("---------------该订单号" + itemNumber + "支付成功");
			// 检查付款状态
			// 检查 txn_id 是否已经处理过
			// 检查 receiver_email 是否是您的 PayPal 账户中的 EMAIL 地址
			// 检查付款金额和货币单位是否正确
			// 处理其他数据，包括写数据库

		} else {
			// 非法信息，可以将此记录到您的日志文件中以备调查
			LOG.warn("---------------该订单号" + itemNumber + "支付成功，但支付信息存在问题");
		}
	}
	
	//支付完成后发送邮件
	public void sendEmail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String orderid = request.getParameter("orderid");
		String[] userinfo = WebCookie.getUser(request);
		if (userinfo != null) {
			ISpidersServer spiders = new SpidersServer();
			spiders.sendEmail(orderid, Integer.parseInt(userinfo[0]));
		} else {
			LOG.warn("没有userid");
		}
	}
	public static void main(String[] args) {
		double payb= 6.91+0.21;
		BigDecimal   b   =   new   BigDecimal(payb);  
		double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		
	}
}
