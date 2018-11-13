package com.cbt.controller;

import com.cbt.bean.OrderBean;
import com.cbt.bean.Payment;
import com.cbt.bean.UserBean;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.PaymentDao;
import com.cbt.processes.dao.ProcessPaymentDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.Utility;
import com.cbt.website.bean.PaymentConfirm;
import com.cbt.website.dao.OrderInfoDao;
import com.cbt.website.dao.OrderInfoImpl;
import com.cbt.website.dao.PaymentConfirmDao;
import com.cbt.website.dao.PaymentConfirmDaoImpl;
import com.cbt.website.service.PaymentConfirmServer;
import com.cbt.website.userAuth.bean.Admuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/cbt/orderws")
public class OrderwsController {

	@Autowired
	private PaymentConfirmServer paymentConfirmServer;
	

	/**
	 * 确认订单
	 * @param pass
	 * @param orderNo
	 * @param paytypeid
	 * @param userId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/paymentConfirm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> paymentConfirm(String pass, String orderNo, String paytypeid,int userId, HttpServletRequest request) {

		Map<String, Object> map = new HashMap<String, Object>();
//		Admuser adm = (Admuser) request.getSession().getAttribute("admuser");
		
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
	/*	
	 * 改成，只有ling能确认，不需要密码
		if (!pass.equals(adm.getPassword())) {
			map.put("code", "1");
			map.put("msg", "密码错误");
			return map;
		}
		*/
		String confirmtime = Utility.format(new Date(), Utility.datePattern1);
		String paytype = "";
		if ("pay_0".equals(paytypeid)) {
			paytype = "0";
		} else if ("pay_1".equals(paytypeid)) {
			paytype = "1";
		} else if ("pay_2".equals(paytypeid)) {
			paytype = "2";
		}
		int result = 0;
		try {
			result = paymentConfirmServer.confirmOrder(orderNo,adm.getAdmName(), confirmtime, paytype, null, null,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == 1) {
			map.put("code", "0");
			map.put("msg", "Successful operation");
			map.put("confirmname", adm.getAdmName());
			map.put("confirmtime", confirmtime);
		} else {
			map.put("code", "1");
			map.put("msg", "Operation failure");
		}
		return map;
	}
	
	
	
	
	/**
	 * 补录订单
	 * @param userpass
	 * @param orderNo
	 * @param paytypeid
	 * @param userId
	 * @param confirmprice
	 * @param tradingencoding
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addPaymentConfirm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> paymentConfirm(String userpass, String orderNo, String paytypeid,int userId,String confirmprice,String tradingencoding, boolean isPayFreight,HttpServletRequest request) {

		Map<String, Object> map = new HashMap<String, Object>();
//		Admuser adm = (Admuser) request.getSession().getAttribute("admuser");
		
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		
		if (!userpass.equals(adm.getPassword())) {
			map.put("code", "1");
			map.put("msg", "密码错误");
			return map;
		}
		String confirmtime = Utility.format(new Date(), Utility.datePattern1);
		String paytype = "";
		if ("pay_0".equals(paytypeid)) {
			paytype = "0";
		} else if ("pay_1".equals(paytypeid)) {
			paytype = "1";
		} else if ("pay_2".equals(paytypeid)) {
			paytype = "2";
		}
		int result = 0;
		try {
			//添加或修改支付确认信息
			PaymentConfirmDao paymentconfirmDao = new PaymentConfirmDaoImpl();
			PaymentConfirm paymentConfirm = paymentconfirmDao.getPaymentConfirmBean(orderNo);
			if (paymentConfirm != null) {
				paymentConfirm.setConfirmtime(Utility.format(new Date(), Utility.datePattern1));
				paymentconfirmDao.updatePaymentConfirm(paymentConfirm);
			}else {
				paymentConfirm = new PaymentConfirm();
				paymentConfirm.setOrderno(orderNo);
				paymentConfirm.setConfirmname(adm.getAdmName());
				paymentConfirm.setConfirmtime(Utility.format(new Date(), Utility.datePattern1));
				paymentConfirm.setPaymentid(tradingencoding);
				paymentConfirm.setPaytype(paytype);
				paymentconfirmDao.addPaymentConfirm(paymentConfirm);
			}
			
			/********************************/
			IUserDao userDao = new UserDao();
			ProcessPaymentDao paymentDao = new PaymentDao();
			OrderInfoDao orderinfoDao = new OrderInfoImpl();
			
			//获取订单信息与用户信息
			OrderBean order = orderinfoDao.getOrderInfo(orderNo, null); 
			UserBean user = userDao.getUserEmailId(userId);
			Payment payment = new Payment();
			payment.setUserid(userId);
			payment.setOrderid(orderNo);
			payment.setPaymentid(tradingencoding);
			payment.setPayment_amount(Float.parseFloat(confirmprice));
			payment.setPayment_cc(order.getCurrency());
			payment.setOrderdesc(user.getEmail());
			payment.setUsername(user.getName());
			payment.setPaystatus(1);
			payment.setCreatetime(Utility.format(new Date(), Utility.datePattern1));
			payment.setPayflag("O");
			payment.setPaytype(paytype);
			
			try {				
				// 添加支付信息
				paymentDao.addPayment(payment);
				//修改订单信息
				result = orderinfoDao.updateOrder(order, confirmprice, isPayFreight);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result == 1) {
			map.put("code", "0");
			map.put("msg", "Successful operation");
			map.put("confirmname", adm.getAdmName());
			map.put("confirmtime", confirmtime);
		} else {
			map.put("code", "1");
			map.put("msg", "Operation failure");
		}
		return map;
	}
	
	
	

}