package com.cbt.controller;

import com.cbt.bean.RechargeRecord;
import com.cbt.bean.UserBean;
import com.cbt.parse.service.StrUtils;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.servlet.Currency;
import com.cbt.service.AdditionalBalanceService;
import com.cbt.service.RefundSSService;
import com.cbt.service.impl.AdditionalBalanceServiceImpl;
import com.cbt.service.impl.RefundServiceImpl;
import com.cbt.website.bean.*;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.service.PaymentServiceNew;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取用户信息
 */
@Controller
@RequestMapping(value = "/paycheckc")
public class PaypalCheckController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PaypalCheckController.class);
	@Autowired
	private RefundSSService refundSSService;
	@Autowired
	private AdditionalBalanceService additionalBalanceService;

	private DecimalFormat format = new DecimalFormat("#0.00");

	@Autowired
	private PaymentServiceNew paymentServiceNew;

	/**
	 * 用户订单到账管理页面
	 * 
	 * @date 2016年9月30日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/doall", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ModelAndView doAll(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("paycheck");

		try {
			String userid = request.getParameter("userid");
			String datestart = request.getParameter("datestart");
			String dateend = request.getParameter("dateend");
			String email = request.getParameter("email");
			String paymentemail = request.getParameter("paymentemail");
			String paytype = request.getParameter("paytype");
			String page = request.getParameter("page");
			String ordersum = request.getParameter("ordersum");
			ordersum = ordersum == null || ordersum.isEmpty() ? "0" : ordersum;
			ordersum = StrUtils.isMatch(ordersum, "(\\d)") ? ordersum : "0";
			if (userid == null) {
				userid = "-1";
			} else {
				userid = userid.replaceAll("\\D+", "").trim();
				userid = userid.isEmpty() ? "-1" : userid;
			}
			if (datestart != null && datestart.isEmpty()) {
				datestart = null;
			}
			if (dateend != null && dateend.isEmpty()) {
				dateend = null;
			}
			if (page == null || page.isEmpty() || "0".equals(page)) {
				page = "1";
			} else {
				page = page.replaceAll("\\D+", "").trim();
				page = page.isEmpty() ? "1" : page;
			}
			if (paymentemail != null && paymentemail.isEmpty()) {
				paymentemail = null;
			}
			if (paytype != null && paytype.isEmpty()) {
				paytype = null;
			}
			if (datestart != null && dateend != null && dateend.equals(datestart)) {
				datestart = datestart + " 00:00:00";
				dateend = dateend + " 23:59:59";
			}
			PayCheckBean bean = new PayCheckBean();
			bean.setPage(Integer.parseInt(page));
			bean.setUserid(Integer.parseInt(userid));
			bean.setEmail(email);
			bean.setDataStart(datestart);
			bean.setDataEnd(dateend);
			bean.setPayEmail(paymentemail);
			bean.setPaytype(paytype);
			bean.setOrdersum(Integer.valueOf(ordersum));
			PaymentDaoImp dao = new PaymentDao();
			List<PaymentBean> list = dao.query(bean);

			double exchange_rate = 1;

			Map<String, Double> maphl = Currency.getMaphl(request);

			// eur CAD:0.00 GBP:0.00 AUD:0.00
			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("EUR");
			double eur = exchange_rate;

			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("CAD");
			double cad = exchange_rate;

			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("GBP");
			double gbp = exchange_rate;

			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("AUD");
			double aud = exchange_rate;

			double usd_total_money = 0;
			double eur_total_money = 0;
			double cad_total_money = 0;
			double gbp_total_money = 0;
			double aud_total_money = 0;

			for (PaymentBean payment : list) {
				String currency = payment.getPayment_cc().toUpperCase();
				if ("USD".equals(currency)) {
					usd_total_money = usd_total_money + Double.valueOf(payment.getPayment_amount());
				} else if ("CAD".equals(currency)) {
					cad_total_money = cad_total_money + Double.valueOf(payment.getPayment_amount());
				} else if ("GBP".equals(currency)) {
					gbp_total_money = gbp_total_money + Double.valueOf(payment.getPayment_amount());
				} else if ("AUD".equals(currency)) {
					aud_total_money = aud_total_money + Double.valueOf(payment.getPayment_amount());
				} else if ("EUR".equals(currency)) {
					eur_total_money = eur_total_money + Double.valueOf(payment.getPayment_amount());
				}
			}

			double allTotalMoney = usd_total_money + eur_total_money * eur + cad_total_money * cad
					+ gbp_total_money * gbp + aud_total_money * aud;

			mv.addObject("allTotalMoney", format.format(allTotalMoney));
			mv.addObject("usdTotalMoney", format.format(usd_total_money));
			mv.addObject("eurTotalMoney", format.format(eur_total_money));
			mv.addObject("cadTotalMoney", format.format(cad_total_money));
			mv.addObject("gbpTotalMoney", format.format(gbp_total_money));
			mv.addObject("audTotalMoney", format.format(aud_total_money));

			mv.addObject("list", list);
			// double allTotalMoney=dao.getAllTotalMoney(bean);
			// mv.addObject("allTotalMoney", allTotalMoney);
			int total = 0;
			if (list != null && !list.isEmpty()) {
				int page_total = Integer.valueOf(list.get(0).getTotal());
				total = page_total % 40 == 0 ? page_total / 40 : page_total / 40 + 1;
				mv.addObject("count", page_total);
			}

			mv.addObject("total", total);
			mv.addObject("currenpage", page);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("query error:" + e.getMessage());
		}

		return mv;

	}

	/**
	 * 用户订单到账管理页面
	 * 
	 */
	@RequestMapping(value = "/queryForList.do")
	@ResponseBody
	public EasyUiJsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String userid = request.getParameter("userid");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String email = request.getParameter("email");
		String paymentEmail = request.getParameter("paymentEmail");
		String payType = request.getParameter("payType");
		String ordersumStr = request.getParameter("ordersum");
		String payId = request.getParameter("payId");
		int ordersum = 0;
		if(!(ordersumStr == null || "".equals(ordersumStr))){
			ordersum = Integer.valueOf(ordersumStr);
		}

		String pageNumStr = request.getParameter("rows");
		int pageNum = 20;
		if (pageNumStr == null || "".equals(pageNumStr) || "0".equals(pageNumStr)) {
			json.setSuccess(false);
			json.setMessage("获取分页数失败");
			return json;
		} else {
			pageNum = Integer.valueOf(pageNumStr);
		}

		String stateNumStr = request.getParameter("page");
		int stateNum = 1;
		if (!(stateNumStr == null || "".equals(stateNumStr) || "0".equals(stateNumStr))) {
			stateNum = Integer.valueOf(stateNumStr);
		}

		PayCheckBean bean = new PayCheckBean();
		bean.setPage(stateNum);
		if (!(userid == null || "".equals(userid))) {
			bean.setUserid(Integer.parseInt(userid));
		} else {
			bean.setUserid(0);
		}
		if (!(email == null || "".equals(email))) {
			bean.setEmail(email);
		}
		if (!(beginDate == null || "".equals(beginDate))) {
			bean.setDataStart(beginDate + " 00:00:00");
		}
		if (!(endDate == null || "".equals(endDate))) {
			bean.setDataEnd(endDate + " 23:59:59");
		}
		if (!(paymentEmail == null || "".equals(paymentEmail))) {
			bean.setPayEmail(paymentEmail);
		}
		if (!(payType == null || "".equals(payType))) {
			bean.setPaytype(payType);
		}
		if(StringUtils.isNotBlank(payId)){
			bean.setPayId(payId);
		}

		bean.setOrdersum(Integer.valueOf(ordersum));
		bean.setRows(pageNum);
		try {

			PaymentDaoImp dao = new PaymentDao();
			// List<PaymentBean> list = dao.query(bean);
			bean.setPage((bean.getPage() -1) * bean.getRows());

//			List<PaymentStatistics> list = dao.queryPaymentStatistics(bean);
//			int total = dao.queryPaymentStatisticsCount(bean);

			List<PaymentStatistics> list = paymentServiceNew.queryPaymentStatistics(bean);
			int total = paymentServiceNew.queryPaymentStatisticsCount(bean);

			// 计算底部统计
			double exchange_rate = 1;

			Map<String, Double> maphl = Currency.getMaphl(request);

			// eur CAD:0.00 GBP:0.00 AUD:0.00
			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("EUR");
			double eur = exchange_rate;

			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("CAD");
			double cad = exchange_rate;

			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("GBP");
			double gbp = exchange_rate;

			exchange_rate = maphl.get("USD");
			exchange_rate = exchange_rate / maphl.get("AUD");
			double aud = exchange_rate;

			double usd_total_money = 0;
			double eur_total_money = 0;
			double cad_total_money = 0;
			double gbp_total_money = 0;
			double aud_total_money = 0;

			for (PaymentStatistics pms : list) {
				String currency = pms.getCurrency().toUpperCase();
				if ("USD".equals(currency)) {
					usd_total_money = usd_total_money + pms.getPaymentAmount();
				} else if ("CAD".equals(currency)) {
					cad_total_money = cad_total_money + pms.getPaymentAmount();
				} else if ("GBP".equals(currency)) {
					gbp_total_money = gbp_total_money + pms.getPaymentAmount();
				} else if ("AUD".equals(currency)) {
					aud_total_money = aud_total_money + pms.getPaymentAmount();
				} else if ("EUR".equals(currency)) {
					eur_total_money = eur_total_money + pms.getPaymentAmount();
				}
			}

			double allTotalMoney = usd_total_money + eur_total_money * eur + cad_total_money * cad
					+ gbp_total_money * gbp + aud_total_money * aud;

			PaymentFooterBean footer = new PaymentFooterBean();
			footer.setOrderNo("金额总数:" + format.format(allTotalMoney) + ";");
			footer.setPaymentTime("USD:" + format.format(usd_total_money) + ";EUR:" + format.format(eur_total_money)
					+ ";CAD:" + format.format(cad_total_money) + ";");
			footer.setCurrency("GBP:" + format.format(gbp_total_money) + ";AUD:" + format.format(aud_total_money));

			List<PaymentFooterBean> footerLst = new ArrayList<PaymentFooterBean>();
			footerLst.add(footer);

			json.setSuccess(true);
			json.setRows(list);
			json.setFooter(footerLst);
			json.setTotal(total);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("query error:" + e.getMessage());
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 用户订单到账管理页面
	 * 
	 */
	@RequestMapping(value = "/queryPaymentDetails.do")
	@ResponseBody
	public EasyUiJsonResult queryPaymentDetails(HttpServletRequest request, HttpServletResponse response) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			json.setSuccess(false);
			json.setMessage("获取订单号失败");
			return json;
		}
		try {

			PaymentDaoImp dao = new PaymentDao();
			//List<PaymentDetails> list = dao.queryPaymentDetails(orderNo);
			List<PaymentDetails> list = paymentServiceNew.queryPaymentDetails(orderNo);
			json.setSuccess(true);
			json.setRows(list);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("query error:" + e.getMessage());
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 单个用户账目校对页面 更新:2016-10-12 申请金额(已完成的、未完成的)按照实际退款金额来计算
	 * 
	 * @date 2016年9月30日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/payInfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ModelAndView payInfo(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("userpayment");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			PayInfoBean pay = new PayInfoBean();
			String userid = request.getParameter("userid");
			if (userid == null || userid.isEmpty()) {
				return mv;
			}

			userid = userid.replaceAll("\\D+", "").trim();
			userid = userid.isEmpty() ? "0" : userid;
			int int_userid = Integer.parseInt(userid);

			// 订单付款金额
			PaymentDaoImp dao = new PaymentDao();
			double orderPayPrice = dao.getPayUserid(int_userid);
			// 额外奖励
			double moneyAmount = additionalBalanceService.getMoneyAmount(int_userid);

			// 订单支出总金额
			IOrderwsDao order = new OrderwsDao();
			double ordersPayAll = order.getOrdersPayUserid(int_userid);

			// 申请退款金额
			double applyRefund = refundSSService.getApplyRefund(int_userid);

			// paypal申请退款金额
			double applyPaypal = refundSSService.getApplyPaypal(int_userid);

			applyRefund = applyRefund - applyPaypal;

			// 已退款金额
			double refund2 = refundSSService.getRefund(int_userid);

			// 用户信息
			IUserDao user_dao = new UserDao();
			UserBean userBean = user_dao.getUserFromIdForCheck(int_userid);
			if (userBean != null) {
				// 用户email
				pay.setUserEmail(userBean.getEmail());
			}
			pay.setUserId(userid);

			// 用户余额
			String userBanlance = userBean.getAvailable_m();
			userBanlance = StrUtils.isNullOrEmpty(userBanlance) ? "0" : userBanlance;
			userBanlance = StrUtils.isMatch(userBanlance, "(\\d+\\.*\\d*)") ? userBanlance : "0";

			// 应有余额
			double balance = moneyAmount + orderPayPrice - ordersPayAll - applyRefund - refund2;
			String strBalance = format.format(balance);
			pay.setAddBalance(format.format(moneyAmount));

			// 汇总余额差
			double subtract = balance - Double.valueOf(userBanlance);
			String strSubtract = format.format(subtract);

			// paypal申诉
			// double paypalByUserid = dao.getPaypalByUserid(int_userid);
			// String strPaypalByUserid = format.format(paypalByUserid);

			// pay.setPaypalAmount(strPaypalByUserid);
			pay.setOrderPriceAll(format.format(ordersPayAll));
			pay.setOrderPayPrice(format.format(orderPayPrice));
			pay.setCurrencyBalance(userBanlance);
			pay.setAppRefund(format.format(applyRefund));
			pay.setAppPaypal(format.format(applyPaypal));

			pay.setRefund(format.format(refund2));
			pay.setBalance("-0.00".equals(strBalance) ? "0.00" : strBalance);
			pay.setTotal("-0.00".equals(strSubtract) ? "0.00" : strSubtract);
			mv.addObject("payment", pay);

			List<RechargeRecord> recordList = refundSSService.getRecordList(int_userid, 1);
			mv.addObject("recordList", recordList);
			int count = recordList != null && !recordList.isEmpty() ? recordList.get(0).getCount() : 0;
			mv.addObject("pagenow", count == 0 ? 0 : 1);
			count = count % 40 == 0 ? count / 40 : count / 40 + 1;
			mv.addObject("pagecount", count);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOG.error("",e);
		}

		return mv;
	}

	/**
	 * rechargRecord
	 * 
	 * @date 2016年9月30日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/record", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String rechargRecord(HttpServletRequest request, HttpServletResponse response) {
		String result = "[]";
		try {
			String strUserid = request.getParameter("userid");
			String strPage = request.getParameter("page");
			if (strUserid == null || strUserid.isEmpty()) {
				return result;
			}

			strUserid = strUserid.replaceAll("\\D+", "").trim();
			strUserid = strUserid.isEmpty() ? "0" : strUserid;

			if (strPage == null || strPage.isEmpty()) {
				strPage = "1";
			}

			strPage = strPage.replaceAll("\\D+", "").trim();
			strPage = strPage.isEmpty() ? "1" : strPage;

			int userid = Integer.valueOf(strUserid);
			int page = Integer.valueOf(strPage);
			List<RechargeRecord> recordList = refundSSService.getRecordList(userid, page);
			// request.setAttribute("allTotalMoney", allTotalMoney);
			result = JSONArray.fromObject(recordList).toString();

		} catch (Exception e) {
			LOG.warn("",e);
		}

		return result;

	}

	/**
	 * 用户账目校对页面 更新:2016-10-12 申请金额(已完成的、未完成的)按照实际退款金额来计算
	 * 
	 * @date 2016年9月30日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/paycheck", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ModelAndView payCheck(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mv = new ModelAndView("userpaycheck");

		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			List<PayInfoBean> infos = new ArrayList<PayInfoBean>();
			String page = request.getParameter("page");
			if (page == null || page.isEmpty()) {
				page = "1";
			} else {
				page = page.replaceAll("\\D+", "");
				page = page.isEmpty() ? "1" : page;
			}
			PaymentDaoImp payMentDao = new PaymentDao();
			IOrderwsDao orderwsDao = new OrderwsDao();

			// 用户订单
			List<PayInfoBean> list_payMentBean = payMentDao.getPaymentForCheck(Integer.parseInt(page));
			// 用户id列表
			List<Integer> useridList = new ArrayList<Integer>();
			for (PayInfoBean list : list_payMentBean) {
				String userId = list.getUserId();
				if (StrUtils.isNullOrEmpty(userId) || !StrUtils.isMatch(userId, "(\\d+)")) {
					continue;
				}
				useridList.add(Integer.valueOf(userId));
			}
			// 用户额外奖励余额
			Map<String, String> balanceByUserId = additionalBalanceService.getBalanceByUserIds(useridList);

			// 用户申请退款金额
			Map<String, String> applyRefundByUserids = refundSSService.getApplyRefundByUserids(useridList);
			// 用户paypal申请退款金额
			Map<String, String> applyPaypalByUserids = refundSSService.getApplyPaypalByUserids(useridList);

			// 已退款金额
			Map<String, String> refundByUserids = refundSSService.getRefundByUserids(useridList);

			// 订单付款金额
			Map<String, String> payByUserids = payMentDao.getPayByUserids(useridList);

			// 订单支出总金额
			Map<String, String> ordersPayByUserids = orderwsDao.getOrdersPayByUserids(useridList);

			// 用户paypal申诉
			// Map<String, String> paypalByUserids =
			// payMentDao.getPaypalByUserids(useridList);

			int list_payMentBean_num = list_payMentBean.size();
			int pageTotal = 0;
			for (int i = 0; i < list_payMentBean_num; i++) {
				PayInfoBean pay = list_payMentBean.get(i);
				String userEmail = pay.getUserEmail();

				if (userEmail.indexOf("@qq.") > -1 || userEmail.indexOf("@163.") > -1) {
					continue;
				}
				pageTotal = pay.getPageTotal();
				String strUserid = pay.getUserId();
				// 订单付款金额
				String orderPayPrice = payByUserids.get(strUserid);
				orderPayPrice = StrUtils.isNullOrEmpty(orderPayPrice) ? "0" : orderPayPrice;
				orderPayPrice = StrUtils.isMatch(orderPayPrice, "(\\d+\\.*\\d*)") ? orderPayPrice : "0";

				// 订单支出总金额
				String ordersPayAll = ordersPayByUserids.get(strUserid);
				ordersPayAll = StrUtils.isNullOrEmpty(ordersPayAll) ? "0" : ordersPayAll;
				ordersPayAll = StrUtils.isMatch(ordersPayAll, "(\\d+\\.*\\d*)") ? ordersPayAll : "0";

				// 申请退款金额
				String applyRefund = applyRefundByUserids.get(strUserid);
				applyRefund = StrUtils.isNullOrEmpty(applyRefund) ? "0" : applyRefund;
				applyRefund = StrUtils.isMatch(applyRefund, "(\\d+\\.*\\d*)") ? applyRefund : "0";
				// paypal申诉
				String applyPaypal = applyPaypalByUserids.get(strUserid);
				applyPaypal = StrUtils.isNullOrEmpty(applyPaypal) ? "0" : applyPaypal;
				applyPaypal = StrUtils.isMatch(applyPaypal, "(\\d+\\.*\\d*)") ? applyPaypal : "0";

				// 已退款金额
				String refund = refundByUserids.get(strUserid);
				refund = StrUtils.isNullOrEmpty(refund) ? "0" : refund;
				refund = StrUtils.isMatch(refund, "(\\d+\\.*\\d*)") ? refund : "0";

				// 额外奖励
				String addBalance = balanceByUserId.get(strUserid);
				addBalance = StrUtils.isNullOrEmpty(addBalance) ? "0" : addBalance;
				addBalance = StrUtils.isMatch(addBalance, "(\\d+\\.*\\d*)") ? addBalance : "0";

				double applyRefundD = Double.valueOf(applyRefund);
				double applyPaypalD = Double.valueOf(applyPaypal);
				applyRefundD = applyRefundD - applyPaypalD;

				// 应有余额 (订单付款金额+额外奖励-订单支出总金额-申请退款金额-已退款金额)
				Double balance = Double.valueOf(orderPayPrice) + Double.valueOf(addBalance)
						- Double.valueOf(ordersPayAll) - applyRefundD - Double.valueOf(refund);

				// paypal申诉
				// String paypal = paypalByUserids.get(strUserid);
				// paypal = StrUtils.isNullOrEmpty(paypal) ? "0":paypal;
				// pay.setPaypalAmount(paypal);

				pay.setAddBalance(addBalance);
				pay.setOrderPriceAll(format.format(Double.valueOf(ordersPayAll)));
				pay.setOrderPayPrice(format.format(Double.valueOf(orderPayPrice)));
				pay.setAppRefund(format.format(applyRefundD));
				pay.setAppPaypal(applyPaypal);

				pay.setRefund(format.format(Double.valueOf(refund)));
				String ybalance = format.format(balance);
				pay.setBalance(ybalance.equals("-0.00") ? "0.00" : ybalance);
				// 汇总余额差(应有余额 -用户当前余额)
				String rebalance = format.format(balance - Double.valueOf(pay.getCurrencyBalance()));
				// payment.total=='-0.01'||payment.total=='0.01'
				if ("-0.01".equals(rebalance) || "0.01".equals(rebalance) || "-0.00".equals(rebalance)) {
					pay.setTotal("0.00");
				} else {
					pay.setTotal(rebalance);
				}

				infos.add(pay);
			}

			mv.addObject("list", infos);
			mv.addObject("pageCurrent", Integer.parseInt(page));
			mv.addObject("pageTotal", pageTotal);
			balanceByUserId = null;
			applyRefundByUserids = null;
			refundByUserids = null;
			ordersPayByUserids = null;
			// paypalByUserids = null;
			payByUserids = null;
			list_payMentBean = null;
		} catch (Exception e) {
			// TODO: handle exception
			LOG.warn("",e);
		}
		return mv;
	}

	public static void main(String[] args) {
		DecimalFormat format = new DecimalFormat("#0.00");
		ArrayList<PayInfoBean> infos = new ArrayList<PayInfoBean>();
		RefundSSService refundSSService = new RefundServiceImpl();
		AdditionalBalanceService additionalBalanceService = new AdditionalBalanceServiceImpl();

		PaymentDaoImp payMentDao = new PaymentDao();
		IOrderwsDao orderwsDao = new OrderwsDao();

		// 用户订单
		List<PayInfoBean> list_payMentBean = payMentDao.getPaymentForCheck(1);
		// 用户id列表
		List<Integer> useridList = new ArrayList<Integer>();
		for (PayInfoBean list : list_payMentBean) {
			String userId = list.getUserId();
			if (StrUtils.isNullOrEmpty(userId) || !StrUtils.isMatch(userId, "(\\d+)")) {
				continue;
			}
			System.err.println("userId:" + userId);
			useridList.add(Integer.valueOf(userId));
		}
		// 用户额外奖励余额
		Map<String, String> balanceByUserId = additionalBalanceService.getBalanceByUserIds(useridList);

		// 用户申请退款金额
		Map<String, String> applyRefundByUserids = refundSSService.getApplyRefundByUserids(useridList);

		// 用户paypal申请退款金额
		Map<String, String> applyPaypalByUserids = refundSSService.getApplyPaypalByUserids(useridList);

		// 已退款金额
		Map<String, String> refundByUserids = refundSSService.getRefundByUserids(useridList);
		// 订单付款金额
		Map<String, String> payByUserids = payMentDao.getPayByUserids(useridList);

		// 订单支出总金额
		Map<String, String> ordersPayByUserids = orderwsDao.getOrdersPayByUserids(useridList);

		// 用户paypal申诉
		// Map<String, String> paypalByUserids =
		// payMentDao.getPaypalByUserids(useridList);

		int list_payMentBean_num = list_payMentBean.size();
		int countAdd = 0;
		int counde = 0;
		int count = 0;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list_payMentBean_num; i++) {
			PayInfoBean pay = list_payMentBean.get(i);

			String userEmail = pay.getUserEmail();
			if (userEmail != null && (userEmail.indexOf("@qq.") > -1 || userEmail.indexOf("@163.") > -1)) {
				continue;
			}

			String strUserid = pay.getUserId();

			// 订单付款金额
			String orderPayPrice = payByUserids.get(strUserid);
			orderPayPrice = StrUtils.isNullOrEmpty(orderPayPrice) ? "0" : orderPayPrice;
			orderPayPrice = StrUtils.isMatch(orderPayPrice, "(\\d+\\.*\\d*)") ? orderPayPrice : "0";

			// 订单支出总金额
			String ordersPayAll = ordersPayByUserids.get(strUserid);
			ordersPayAll = StrUtils.isNullOrEmpty(ordersPayAll) ? "0" : ordersPayAll;
			ordersPayAll = StrUtils.isMatch(ordersPayAll, "(\\d+\\.*\\d*)") ? ordersPayAll : "0";

			// 申请退款金额
			String applyRefund = applyRefundByUserids.get(strUserid);
			applyRefund = StrUtils.isNullOrEmpty(applyRefund) ? "0" : applyRefund;
			applyRefund = StrUtils.isMatch(applyRefund, "(\\d+\\.*\\d*)") ? applyRefund : "0";

			// paypal申诉
			String applyPaypal = applyPaypalByUserids.get(strUserid);
			applyPaypal = StrUtils.isNullOrEmpty(applyPaypal) ? "0" : applyPaypal;
			applyPaypal = StrUtils.isMatch(applyPaypal, "(\\d+\\.*\\d*)") ? applyPaypal : "0";

			// 已退款金额
			String refund = refundByUserids.get(strUserid);
			refund = StrUtils.isNullOrEmpty(refund) ? "0" : refund;
			refund = StrUtils.isMatch(refund, "(\\d+\\.*\\d*)") ? refund : "0";

			// 额外奖励
			String addBalance = balanceByUserId.get(strUserid);
			addBalance = StrUtils.isNullOrEmpty(addBalance) ? "0" : addBalance;
			addBalance = StrUtils.isMatch(addBalance, "(\\d+\\.*\\d*)") ? addBalance : "0";

			double applyRefundD = Double.valueOf(applyRefund);
			double applyPaypalD = Double.valueOf(applyPaypal);
			applyRefundD = applyRefundD - applyPaypalD;
			// 应有余额 (订单付款金额+额外奖励-订单支出总金额-申请退款金额-已退款金额)
			Double balance = Double.valueOf(orderPayPrice) + Double.valueOf(addBalance) - Double.valueOf(ordersPayAll)
					- applyRefundD - Double.valueOf(refund);

			pay.setAddBalance(addBalance);
			pay.setOrderPriceAll(format.format(Double.valueOf(ordersPayAll)));
			pay.setOrderPayPrice(format.format(Double.valueOf(orderPayPrice)));
			pay.setAppRefund(format.format(Double.valueOf(applyRefund)));
			pay.setRefund(format.format(Double.valueOf(refund)));
			String ybalance = format.format(balance);
			pay.setBalance(ybalance.equals("-0.00") ? "0.00" : ybalance);

			// 汇总余额差(应有余额 -用户当前余额)
			String rebalance = format.format(balance - Double.valueOf(pay.getCurrencyBalance()));
			pay.setTotal(rebalance.equals("-0.00") ? "0.00" : rebalance);
			// pay.setPageCurrent(Integer.parseInt(page));

			if (!pay.getTotal().equals("0.00")) {
				System.err.println("strUserid:" + strUserid + ">>>>>>>>>>rebalance:" + rebalance);
				sb.append("\nstrUserid:" + strUserid + ">>>>>>>>>>rebalance:" + rebalance);
				count++;
				if (pay.getTotal().indexOf("-") > -1) {
					counde++;
				} else {
					countAdd++;
				}
				infos.add(pay);
			}

		}
		balanceByUserId = null;
		applyRefundByUserids = null;
		refundByUserids = null;
		ordersPayByUserids = null;
		// paypalByUserids = null;
		payByUserids = null;
		list_payMentBean = null;
		System.err.println(infos);
		System.err.println("counde:" + counde);
		System.err.println("countAdd:" + countAdd);
		System.err.println("count:" + count);
		System.err.println("sb:" + sb);

	}

}
