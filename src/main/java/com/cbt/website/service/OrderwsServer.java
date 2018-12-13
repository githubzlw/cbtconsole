package com.cbt.website.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cbt.FreightFee.controller.FreightFeeController;
import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.PaymentConfirm;
import com.cbt.website.bean.QualityResult;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;
import com.cbt.website.dao.*;

public class OrderwsServer implements IOrderwsServer {

	private IOrderwsDao dao = new OrderwsDao();
	private IMessageServer messageServer = new MessageServer();
	private IOrderSplitDao splitDao = new OrderSplitDaoImpl();

	@Override
	public List<OrderBean> getOrders(int userID, int state, Date startdate, Date enddate, String username, String email,
	                                 String orderno, String phone, int page, int admuserid, int buyid, int showUnpaid, int status) {
		List<OrderBean> order = dao.getOrders(userID, state, startdate, enddate, username, email, orderno, phone,
				(page - 1) * 40, 40, admuserid, buyid, showUnpaid, status);
		return order;
	}

	@Override
	public int upOrderDeatail(int orderDatailId, int state, String orderNo, int userId) {
		String uuid = UUIDUtil.getEffectiveUUID(userId, "");
		String path = "";
		try {
			path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int res = dao.upOrderDeatail(orderDatailId, state);
		if (res > 0) {
			int res1 = dao.updateOrderStatus(orderNo);
			if (res1 > 0) {
				res = 2;
				// 货物全部到达仓库，发送邮件
				StringBuffer info = new StringBuffer();
				info.append("Order#：" + orderNo
						+ "<br> Your ImportExpress Order products now all arrived at our warehouse! It will now process to the delivery. If you have not paid the shipping fee yet, please go to <a style='color: #0070C0' href='"
						+ AppConfig.ip_email + path + "'>[Your account]</a> and pay for it.<br>");
				IOrderSplitServer splitServer = new OrderSplitServer();
				String email = splitServer.getUserEmailByUserName(userId);
				StringBuffer sbBuffer = SendEmail.SetContent(email, info);
				if (Utility.getStringIsNull(email)) {
					SendEmail.send(null, null, email, sbBuffer.toString(),
							"Your ImportExpress Order products now all arrived at our warehouse!", null, 1);
				}
			}
		}
		return res;
	}

	@Override
	public List<OrderDetailsBean> getOrdersDetails(String orderNo) {
		List<OrderDetailsBean> list=dao.getOrdersDetails(orderNo);
		return list;
	}

	@Override
	public double getAllFreightByOrderid(String orderid) {

		return dao.getAllFreightByOrderid(orderid);
	}

	@Override
	public void updateGoodsCarMessage(String orderNo) {
		dao.updateGoodsCarMessage(orderNo);
	}

	@Override
	public TaoBaoOrderInfo getShipStatusInfo(String tb_1688_itemid,
	                                         String last_tb_1688_itemid, String time, String admName,String shipno,int offline_purchase,String orderid,int goodsid) {

		return dao.getShipStatusInfo(tb_1688_itemid,last_tb_1688_itemid,time,admName,shipno,offline_purchase,orderid,goodsid);
	}

	// getChildrenOrdersDetails 得到dropship的子订单详情 OrderwsServer
	@Override
	public List<OrderDetailsBean> getChildrenOrdersDetails(String orderNo) {

		return dao.getChildrenOrdersDetails(orderNo);
	}

	@Override
	public int upOrder(int userId, String orderNo, String actual_ffreight, String custom_discuss_other,
	                   Date transport_time, String actual_weight, int state, String actual_volume, Date expect_arrive_date,
	                   String actual_allincost, String remaining_price, double order_ac, String service_fee,
	                   String domestic_freight, String mode_transport, double actual_freight_c_, float exchange_rate,
	                   float applicable_credit) {

		float remaining_price_d = Float.parseFloat(remaining_price);
		int res = dao.upOrder(orderNo, actual_ffreight, custom_discuss_other, state, transport_time, actual_weight,
				actual_volume, expect_arrive_date, actual_allincost, remaining_price_d < 0 ? 0 : remaining_price_d,
				order_ac, service_fee, domestic_freight, mode_transport, actual_freight_c_,
				remaining_price_d < 0 ? remaining_price_d : 0);
		if (res > 0) {
			if (remaining_price_d < 0) {
				com.cbt.website.dao.UserDao uDao = new UserDaoImpl();
				// 将订单所剩余额放入用户表

				// 汇率换算
				// 插入余额变更表记录
				uDao.updateUserAvailable(userId, -remaining_price_d * exchange_rate, "Update orderinfo:" + orderNo,
						orderNo,null, 0, (float) (applicable_credit * exchange_rate), 0);
			}
		}
		if (res > 0 && state == 3) {
			// 如果是修改成出运中的状态则修改订单详情中的状态，由1改为0，否则个人中心的到仓商品数量会错误
			dao.upOrderDeatailstate(orderNo, 0);
		}
		return res;
	}

	@Override
	public int upOrderDeatail(int orderDatailId, String file, String weight, String volume, String actual_price,
	                          String actual_freight, String file_upload) {
		// TODO Auto-generated method stub
		return dao.upOrderDeatail(orderDatailId, file, weight, volume, actual_price, actual_freight, file_upload);
	}

	@Override
	public OrderBean getOrders(String orderNo) {
		// TODO Auto-generated method stub
		OrderBean orderBean = dao.getOrders(orderNo);
		/*
		 * double actual_allincost = orderBean.getActual_allincost();
		 * //获取客户总付款金额 double s = dao.getOrdersPay(orderNo);
		 * orderBean.setPay_price(s);
		 */
		// orderBean.setRemaining_price(actual_allincost-s);
		return orderBean;
	}

	@Override
	public double getEstimatefreight(String orderNo) {
		return  dao.getEstimatefreight(orderNo);
	}

	@Override
	public double getAllWeight(String orderNo) {
		return  dao.getAllWeight(orderNo);
	}

	@Override
	public ShippingBean getShipPackmentInfo(String orderid) {
		return dao.getShipPackmentInfo(orderid);
	}

	@Override
	public OrderBean getChildrenOrders(String orderNo) {
		// TODO Auto-generated method stub
		OrderBean orderBean = dao.getChildrenOrders(orderNo);
		/*
		 * double actual_allincost = orderBean.getActual_allincost();
		 * //获取客户总付款金额 double s = dao.getOrdersPay(orderNo);
		 * orderBean.setPay_price(s);
		 */
		// orderBean.setRemaining_price(actual_allincost-s);
		return orderBean;
	}

	@Override
	public List<OrderBean> getListOrders(String orderNo) {
		List<OrderBean> listOrderBean = dao.getListOrders(orderNo);
		return listOrderBean;
	}

	@Override
	public PaymentConfirm queryForPaymentConfirm(String orderNo) {
		return dao.queryForPaymentConfirm(orderNo);
	}


	@Override
	public int isTblack(String userName) {
		return dao.isTblack(userName);
	}

	public List<String> getOrderNos(int userid,String orderid) {
		return dao.getOrderNos(userid,orderid);
	}

	@Override
	public int saveForwarder(Forwarder forwarder) {
		if (dao.getForwarder(forwarder.getOrder_no()) != null) {
			return dao.upForwarder(forwarder);
		}
		return dao.saveForwarder(forwarder);
	}

	@Override
	public Forwarder getForwarder(String orderNo) {

		return dao.getForwarder(orderNo);
	}

	@Override
	public int getChangegooddata(String orderNo) {

		return dao.getChangegooddata(orderNo);
	}

	@Override
	public Evaluate getEvaluate(String orderNo) {
		return dao.getEvaluate(orderNo);
	}

	@Override
	public String updateOrderChange(String orderNo, int goodId, String oldInfo, String newInfo, int changeType) {
		if (changeType == 2) {
			if (!Utility.getStringIsNull(oldInfo)) {
				oldInfo = Utility.getDateFormatYMD().format(new Date());
			} else if (oldInfo.trim().equals("0")) {
				oldInfo = Utility.getDateFormatYMD().format(new Date());
			}
		}
		int updateOrderChange = dao.updateOrderChange(orderNo, goodId, oldInfo, newInfo, changeType);
		String result = "{\"result\":" + updateOrderChange + "}";
		return result;
	}

	@Override
	public Map<String, Object> getOrderChange(String orderNo, int goodId, int changeType, int lastNum) {
		Map<String, Object> orderChange = dao.getOrderChange(orderNo, goodId, changeType, lastNum);
		return orderChange;
	}

	@Override
	public List<Object[]> getOrderChanges(String orderNo, int lastNum) {
		List<Object[]> orderChange = dao.getOrderChanges(orderNo, lastNum);
		return orderChange;
	}

	@Override
	public String sendCutomers(String serverName, int port, String orderNo, int whichOne, int isDropship,
	                           String orderNo1) {
		int result = 0;
		UserBean user = dao.getUserBeanByOrderNo(orderNo);
		StringBuffer sb = new StringBuffer("<div style='font-size: 14px;'>");

		String uuid = UUIDUtil.getEffectiveUUID(0, user.getEmail());
		String path = "";

		sb.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='" + AppConfig.ip_email
				+ "/img/logo.png' ></img></a>");
		sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear Valued " + user.getName() + ",</div>");
		// "/cbtconsole/AbstractServlet?action=getOrders&className=OrderInfo&state="+state+"&page=1";
		if (whichOne == 1) {
			List<Integer> res = null;
			dao.updateClinetAndServerUpdateState(orderNo, 0, 1);// 更改主订单号的状态
			if (isDropship != 1) {
				result = dao.updateOrderChangeStatus(orderNo);
				res = dao.getOrderChangeStatus(orderNo);
			} else {
				result = dao.updateOrderChangeStatus(orderNo1);
				res = dao.getOrderChangeStatus(orderNo1);

			}
			String mess = "";
			int up_discount = 0;
			for (int i = 0; i < res.size(); i++) {
				// 问题类型 1价格 2交期3定量4取消5咨询6货源(内部不给客户看)7国内运费
				if (res.get(i) == 1) {
					up_discount = 1;
				} else if (res.get(i) == 2) {
					mess = " Item lead time has been updated;";
					up_discount = 1;
				} else if (res.get(i) == 3) {
					mess = "Your order quantity is too low;";
					up_discount = 1;
				} else if (res.get(i) == 4) {
					mess = "item picked are no longer valid;";
					up_discount = 1;
				} else if (res.get(i) == 5) {
					up_discount = 2;
				} else if (res.get(i) == 7) {
					mess = "Collection cost in China has been updated;";
				}
			}
			/*
			 * if(up_discount == 1){ //修改订单优惠金额 List<Object[]> list =
			 * dao.getOrderClass(orderNo); IPreferentialDao pre = new
			 * PreferentialDao(); List<ClassDiscount> list_cd =
			 * pre.getClass_discount(); double discount_price = 0; for (int i =
			 * 0; i < list.size(); i++) { Object[] obj = list.get(i); double
			 * price =Double.parseDouble((String)obj[0]); int number =
			 * Integer.parseInt(obj[1].toString()); double price_ = price *
			 * number;
			 *
			 * String class_g = obj[2].toString();
			 * if(Utility.getStringIsNull(class_g) && price <= 150){ for (int j
			 * = 0; j < list_cd.size(); j++) { ClassDiscount cd =
			 * list_cd.get(j); if(cd.getId()==Integer.parseInt(class_g)){
			 * cd.setSum_price(cd.getSum_price()+price_); } } } } for (int i =
			 * 0; i < list_cd.size(); i++) { ClassDiscount cd = list_cd.get(i);
			 * if(cd.getPrice() < cd.getSum_price()){ discount_price +=
			 * (1-cd.getDeposit_rate())*cd.getSum_price(); } } String discount_
			 * = list.get(0)[3].toString(); if(!discount_.equals("") &&
			 * !discount_.equals("0")){ dao.addDiscountChenge(orderNo,
			 * list.get(0)[3].toString(), new
			 * BigDecimal(discount_price).setScale(2,
			 * BigDecimal.ROUND_HALF_UP).toString(), "后台修改订单金额|订量|删除:"+orderNo);
			 * } }
			 */
			// 通知客户订单有更新
			String sendemail = null;
			String pwd = null;
			IUserDao userDao = new UserDao();
			String[] adminEmail = userDao.getAdminUser(0, null, user.getId());
			if (adminEmail != null) {
				sendemail = adminEmail[0];
				pwd = adminEmail[1];
			}
			if (up_discount > 0) {
				sb.append("<div>Order #:" + orderNo + "</div><br>");

				try {
					path = UUIDUtil.getAutoLoginPath(
							"/orderInfo/emailLink?orderNo=" + orderNo, uuid);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				sb.append("<div>Due to the supply reasons, please go to your order in <a href='" + AppConfig.ip_email
						+ path
						+ "'>your account</a> to check your item status by click 【Need Your Action】 and we will continue purchase for you after your confirmation, or go to your order to 【Cancel Item】and refund will be done automatically. </div>");

				sb.append(
						"<br><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>");
				sb.append(
						"<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
				sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");

				try {
					path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sb.append(
						"This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"
								+ AppConfig.ip_email + path + "'>here</a>.");
				sb.append("</div></div></div>");
				result = SendEmail.send(sendemail, pwd, user.getEmail(), sb.toString(),
						"Your ImportExpress order " + orderNo + " need your action now!", orderNo, 1);
			}
			if (up_discount != 2) {
				result = messageServer.addMessage(user.getId(), "Order #:" + orderNo + "，" + mess, orderNo);
			}
		}
		if (whichOne == 2) {
			dao.updateClinetAndServerUpdateState(orderNo, 0, 0);
			// 确认订单 更改订单状态
			result = dao.updateOrderStatus(orderNo, 1);
			result = messageServer.addMessage(user.getId(), "Your order has been verified and starts purchasing.",
					orderNo);
			// 删除该订单消息栏中含有确认价格信息的数据
			messageServer.delMessage(orderNo, "Order #");
		}
		return "{\"result\":" + result + "}";
	}

	@Override
	public int sendShipment(String orderNo, int userId, String email, String expect_arrive_time) {
		String uuid = UUIDUtil.getEffectiveUUID(userId, "");
		String path = "/individual/getCenter";
		try {
			path = UUIDUtil.getAutoLoginPath("", uuid);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int res = 0;
		IOrderSplitServer splitServer = new OrderSplitServer();
		email = splitServer.getUserEmailByUserName(userId);
		StringBuffer sb = new StringBuffer("<div style='font-size: 14px;'>");
		sb.append("<a href='http://www.import-express.com'><img style='cursor: pointer' src='" + AppConfig.ip_email
				+ "/img/logo.png' ></img></a>");
		sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear Valued " + email + ",</div>");

		sb.append("<br><div>Order#: <a style='color: #0070C0' href='" + AppConfig.ip_email
				+ "/processesServlet?action=individualOrderdetail&className=OrderInfo&orderNo=" + orderNo + "'>"
				+ orderNo + "</a></div>");
		sb.append("<br><div>Your order is now on the way to you, estimated time of arrival is " + expect_arrive_time
				+ ". You can<a style='color: #0070C0' href='" + AppConfig.ip_email + path
				+ "'> Track it here</a>.</div>");

		sb.append(
				"<br><div><label style='font-weight: bolder;'>Kindly reminder</label>: Please claim the item as <label style='font-weight: bolder;'>PERSONAL USE</label> to avoid business tax during custom clearance.</div>");

		sb.append(
				"<br><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>");
		sb.append(
				"<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
		sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
		sb.append(
				"This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"
						+ AppConfig.ip_email + path + "'>here</a>.");
		sb.append("</div></div></div>");
		String sendemail = null;
		String pwd = null;
		IUserDao userDao = new UserDao();
		String[] adminEmail = userDao.getAdminUser(0, null, userId);
		if (adminEmail != null) {
			sendemail = adminEmail[0];
			pwd = adminEmail[1];
		}
		res = SendEmail.send(sendemail, pwd, email, sb.toString(),
				"ImportExpress Order " + orderNo + " is now on the way to you!", orderNo, 1);
		return res;
	}

	@Override
	public int upOrderChangeResolve(String orderNo, int goodId, int changeType) {
		return dao.upOrderChangeResolve(orderNo, goodId, changeType);
	}

	@Override
	public OrderDetailsBean getById(int id) {
		OrderDetailsBean odb = dao.getById(id);
		return odb;
	}

	@Override
	public List<Object[]> getOrderChanges(String orderNo) {
		return dao.getOrderChanges(orderNo);
	}

	@Override
	public int upQuestions(String orderid, String answer, String freight, String tariffs) {
		String orderidStr = dao.getAdvance(orderid);
		if (orderidStr != null) {
			return dao.upQuestions(orderid, answer, freight, tariffs);
		} else {
			return dao.addAdvance(orderid, freight, tariffs);
		}

	}

	@Override
	public AdvanceOrderBean getAdvanceBean(String orderid) {
		return dao.getAdvanceBean(orderid);
	}

	@Override
	public List<Map<String, String>> getOrdersPays(String orderNo) {
		return dao.getOrdersPays(orderNo);
	}

	@Override
	public int updateGoods(int type, int goodsid, String value) {
		return dao.updateGoods(type, goodsid, value);
	}

	@Override
	public int getOrdersPage(int userID, int state, Date date, String username, String email, String orderno,
	                         String phone) {

		return dao.getOrdersPage(userID, state, date, username, email, orderno, phone);
	}

	@Override
	public int closeOrder(String orderNo) {
		return dao.closeOrder(orderNo);
	}

	@Override
	public boolean checkTestOrder(String orderid) {
		return dao.checkTestOrder(orderid);
	}

	@Override
	public void cancelInventory1(String orderNo) {
		dao.cancelInventory1(orderNo);
	}

	@Override
	public void cancelInventory(String orderNo) {
		dao.cancelInventory(orderNo);
	}

	@Override
	public List<Object[]> getGoodpostage(int userid, int page) {
		return dao.getGoodpostage(userid, (page - 1) * 40, 40);
	}

	@Override
	public int getGoodpostageNumber(int userid) {
		return dao.getGoodpostageNumber(userid);
	}

	@Override
	public int upOrderPurchase(int orderdetailid, String orderNo, String purchase_confirmation) {
		return dao.upOrderPurchase(orderdetailid, orderNo, purchase_confirmation);
	}

	@Override
	public int cancelOrderPurchase(int orderdetailid, String orderNo) {
		// TODO Auto-generated method stub
		return dao.cancelOrderPurchase(orderdetailid, orderNo);
	}
	@Override
	public List<CodeMaster> getLogisticsInfo() {
		return dao.getLogisticsInfo();
	}

	@Override
	public List<String> getOrdersNos(int userID) {
		// TODO Auto-generated method stub
		return dao.getOrdersNos(userID);
	}

	@Override
	public int existOrders(String orderNo) {
		// TODO Auto-generated method stub
		return dao.existOrders(orderNo);
	}

	@Override
	public int addOrder_reductionfreight(String orderNo, double price) {
		return dao.addOrder_reductionfreight(orderNo, price);
	}

	@Override
	public String getOrdersEvaluateByOrderNo(String orderNo) {
		return dao.getOrdersEvaluateByOrderNo(orderNo);
	}

	@Override
	public OrderBean getOrder_remainingPrice(String orderNo) {

		return dao.getOrder_remainingPrice(orderNo);
	}

	@Override
	public int upOrder_remainingPrice(String orderNo, double remainingPrice, double order_ac) {

		return dao.upOrder_remainingPrice(orderNo, remainingPrice, order_ac);
	}

	public List<OrderBean> getOrderChangeState(String orderNo) {
		return dao.getOrderChangeState(orderNo);
	}

	public String queryCountryNameByOrderNo(String orderNo) {
		return dao.queryCountryNameByOrderNo(orderNo);
	}

	public List<Integer> getRepeatUserid(int id) {
		return dao.getRepeatUserid(id);
	}

	@Override
	public int iscloseOrder(String orderNo) {
		return dao.iscloseOrder(orderNo);
	}

	@Override
	public List<Map<String, String>> getCustCountry(String orderno) {
		return dao.getCustCountry(orderno);
	}

	@Override
	public List<Map<String, String>> getBuyerByOrderNo(String orderno) {
		return dao.getBuyerByOrderNo(orderno);
	}

	@Override
	public OrderBean getStateByOrderNo(int userid, String orderno) {

		return dao.getStateByOrderNo(userid, orderno);
	}

	@Override
	public int checkOrderState(String orderNo,String isDropshipOrder1) {
		return dao.checkOrderState(orderNo, isDropshipOrder1);
	}

	@Override
	public Map<String, String> queryOrderAmount(String orderNo) {
		return dao.queryOrderAmount(orderNo);
	}

	@Override
	public int getIpnPaymentStatus(String orderNo) {
		return dao.getIpnPaymentStatus(orderNo);
	}

	@Override
	public List<QualityResult> openCheckResult(Map<String, String> map) {

		return dao.openCheckResult(map);
	}

	@Override
	public List<Admuser> getAllBuyer() {

		return dao.getAllBuyer();
	}

	@Override
	public int changeBuyer(int odid, int admuserid) {

		return dao.changeBuyer(odid, admuserid);
	}

	@Override
	public int changeOrderBuyer(String orderid, int admuserid,String odids) {

		return dao.changeOrderBuyer(orderid, admuserid,odids);
	}

	@Override
	public int addOrderDetails(String goodsid, String count, String newOrderid, String orderid,int admuserid) {
		return dao.addOrderDetails(goodsid, count, newOrderid, orderid,admuserid);
	}

	@Override
	public int addOrderInfo(String orderid, String newOrderid, int length) {

		return dao.addOrderInfo(orderid, newOrderid, length);
	}

	@Override
	public int addAutoAdmuser(String orderid) {

		return dao.addAutoAdmuser(orderid);
	}

	@Override
	public boolean deleteOrderInfo(String newOrderid) {

		return dao.deleteOrderInfo(newOrderid);
	}

	@Override
	public int isDropshipOrder(String orderNo) throws Exception {
		return dao.isDropshipOrder(orderNo);
	}

	@Override
	public String queryMainOrderByDropship(String orderNo) throws Exception {
		return dao.queryMainOrderByDropship(orderNo);
	}

	@Override
	public int isCloseDropshipOrder(String orderNo) throws Exception {
		return dao.isCloseDropshipOrder(orderNo);
	}

	@Override
	public int isCloseByDropshipMainOrder(String mainOrderNo) throws Exception {
		return dao.isCloseByDropshipMainOrder(mainOrderNo);
	}

	@Override
	public int closeDropshipOrder(int userId,String mainOrderNo, String childOrderNo, float totalPrice, float orderAc, float extraFreight, float weight)
			throws Exception {
		// 子订单设置为取消状态
		dao.closeDropshipOrder(userId,childOrderNo);
		// 修改整单的商品总价和运费金额
		dao.updateMainOrderNoTotalPriceAndFreight(userId,mainOrderNo, totalPrice, orderAc,extraFreight,weight);
		// 更改dropship子订单关联商品为取消
		return dao.closeOrderGoodsByDropshipOrder(userId,childOrderNo);
	}

	@Override
	public int closeDropshipOrderByMainOrderNo(String mainOrderNo) throws Exception {
		// 更改主订单状态为取消
		dao.closeDropshipOrderByMainOrderNo(mainOrderNo);
		// 关联商品全部处于取消状态
		return dao.closeDropshipOrderGoodsByMainOrderNo(mainOrderNo);
	}

	@Override
	public int updateMainOrderByDropship(int userId,String mainOrderNo, String orderNo) throws Exception {

		// 判断是否取消了
		boolean isAllCancel = dao.checkDropshipIsCancel(mainOrderNo);
		// 已经设置为全部取消了
		if (isAllCancel) {
			// 先更改dropship主订单状态为取消
			return dao.closeOrderByDropshipOrder(userId,orderNo);
		} else {
			// 返回-2
			return -2;
		}
	}

	@Override
	public int judgeOrderState(String orderid) {

		return dao.judgeOrderState(orderid);
	}

	@Override
	public float queryGoodsPriceFromDetails(String orderNo) {
		return dao.queryGoodsPriceFromDetails(orderNo) ;
	}
	@Override
	public TabTransitFreightinfoUniteOur getFreightInfo(String countryNameCn,int isEub){
		return dao.getFreightInfo(countryNameCn, isEub);
	}

	@Override
	public double getFreightByWeight(TabTransitFreightinfoUniteOur fo,
	                                 double weight) {
		//weithg 是kg 转g
		double weihtG = weight * 1000;
		//是否支持eub
		int eubType = fo.getEubType();
		//jcex首重
		double baseWeiht = 0.0;
		//jcex首重价格
		double basePrice =0.0;
		//jcex 续重
		double ratioWeight = 0.0;
		//jcex续重价格
		double ratioPrice = 0.0;
		//运费
		double FreightFeeController = 0.0;
		switch (eubType) {
			case 1:
				//Eub首重
				baseWeiht = fo.getEubBaseWeight();
				//Eub首重价格
				basePrice = fo.getEubBasePrice().doubleValue();
				//Eub 续重
				ratioWeight = fo.getEubRatioWeight();
				//Eub续重价格
				ratioPrice = fo.getEubRatioPrice().doubleValue();
				break;
			default:
				//jcex首重
				baseWeiht = fo.getJcexBaseWeight();
				//jcex首重价格
				basePrice = fo.getJcexBasePrice().doubleValue();
				//jcex 续重
				ratioPrice = fo.getJcexRatioWeight();
				//jcex续重价格
				ratioWeight  = fo.getJcexRatioPrice().doubleValue();
				break;
		}
		System.out.println("basePrice "+basePrice+"+ "+"("+"weihtG "+weihtG+"+"+ "-"+"baseWeiht "+baseWeiht+") /"+  "ratioWeight "+ratioWeight +"*"+"ratioPrice:"+ratioPrice);
		if(ratioWeight > 0.0) {
			FreightFeeController = new BigDecimal((weihtG - baseWeiht) / ratioWeight * ratioPrice + basePrice).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return FreightFeeController;
	}

	@Override
	public int isExistsMessageByOrderNo(String orderNo) {
		return splitDao.isExistsMessageByOrderNo(orderNo) ;
	}
}
