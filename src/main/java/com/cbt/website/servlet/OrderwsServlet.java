package com.cbt.website.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.*;
import com.cbt.change.util.ChangeRecordsDao;
import com.cbt.change.util.CheckCanUpdateUtil;
import com.cbt.change.util.ErrorLogDao;
import com.cbt.change.util.OnlineOrderInfoDao;
import com.cbt.email.entity.EmailReceive1;
import com.cbt.email.service.EmailReceiveServiceImpl;
import com.cbt.email.service.IEmailReceiveService;
import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.StrUtils;
import com.cbt.pay.dao.IOrderDao;
import com.cbt.pay.dao.OrderDao;
import com.cbt.pay.service.IPayServer;
import com.cbt.pay.service.ISpidersServer;
import com.cbt.pay.service.PayServer;
import com.cbt.pay.service.SpidersServer;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.service.*;
import com.cbt.util.*;
import com.cbt.warehouse.service.InventoryService;
import com.cbt.warehouse.service.InventoryServiceImpl;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.OrderBuy;
import com.cbt.website.bean.PaymentConfirm;
import com.cbt.website.dao.*;
import com.cbt.website.dao2.IWebsiteOrderDetailDao;
import com.cbt.website.dao2.WebsiteOrderDetailDaoImpl;
import com.cbt.website.service.*;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.service.OverseasWarehouseStockService;
import com.importExpress.service.impl.OverseasWarehouseStockServiceImpl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ylm ??????????????????
 */
public class OrderwsServlet extends HttpServlet {
	private InventoryService inventoryService = new InventoryServiceImpl();
	private OverseasWarehouseStockService owsService = new OverseasWarehouseStockServiceImpl();

	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderwsServlet.class);
	IExpressTrackDao dao= new ExpressTrackDaoImpl();
	public OrderwsServlet() {
		super();
	}

	String urlll = null;

	// ????????????????????????
	protected void getOrderChangeState(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderNo = request.getParameter("orderNo");
		if (orderNo.indexOf("'") == -1) {
			orderNo = "'" + orderNo + "'";
		}
		IOrderwsServer server = new OrderwsServer();

	}

	// ??????????????????
	protected void getInit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderno = request.getParameter("orderno");
		PrintWriter out = response.getWriter();

		BuyUserDao buy = new BuyUserDaoImp();
		ArrayList<OrderBuy> orderBuy = buy.query(orderno);
		if (orderBuy != null && !orderBuy.isEmpty()) {
			out.print(orderBuy.get(0).getBuyId());
		} else {
			out.print(0);
		}
		out.flush();
		out.close();
	}

	// ??????????????????
	protected void info(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String admName = adm.getAdmName();

		String orderNo = request.getParameter("orderNo");
		String userId = request.getParameter("userId");

		IOrderwsServer server = new OrderwsServer();
		IPayServer payServer = new PayServer();
		// ?????????????????????(????????????????????????)
		List<Payment> payList = payServer.getOrdersPayList(orderNo, 1);

		OrderBean order = new OrderBean();
		List<OrderBean> orders = new ArrayList<OrderBean>();
		// ????????????
		List<OrderBean> list = server.getListOrders(orderNo);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getOrderNo().equals(orderNo)) {
				order = list.get(i);
			} else {
				orders.add(list.get(i));
			}
		}
		if (StrUtils.isMatch(userId, "(\\d+)")) {
			UserServer userServer = new UserServer();
			double[] balance = userServer.getBalance(Integer.valueOf(userId));
			request.setAttribute("balance", balance[0]);
		}

		// ??????????????????
		// List<OrderDetailsBean> odb = server.getOrdersDetails(orderNo);
		// //orderDetail
		// request.setAttribute("orderDetail", odb); // ??????????????????
		// ??????????????????????????????
		PaymentConfirm paymentConfirm = server.queryForPaymentConfirm(orderNo);
		//??????????????????
		int row=server.isTblack(payList.size()>0?payList.get(0).getUsername():"");
		if(row>0){
			request.setAttribute("isTblack", "???????????????????????????");
		}
		// ??????????????????
		request.setAttribute("order", order);
		// ?????????????????????????????????
		request.setAttribute("orders", orders);
		request.setAttribute("payList", payList);
		request.setAttribute("paymentConfirm", paymentConfirm);
		// ????????????
		request.setAttribute("actual_ffreight_",
				Utility.getIsDouble(order.getActual_ffreight()) ? Double.parseDouble(order.getActual_ffreight()) : 0);
		request.setAttribute("userId", userId);
		if ("0".equals(adm.getRoletype())) {
			request.getRequestDispatcher("website/paymentConfirm.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("website/paymentConfirm1.jsp").forward(request, response);
		}

	}

	// ??????????????????
	protected void getChangeBuy(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String buyid = request.getParameter("buyid");
		String orderno = request.getParameter("orderno");
		String buyuser = request.getParameter("buyuser");
		BuyUserDao buy = new BuyUserDaoImp();
		// ArrayList<OrderBuy> orderBuy = buy.query(orderno);
		PrintWriter out = response.getWriter();
		// if(orderBuy==null||orderBuy.isEmpty()){
		int update = buy.update(orderno, buyuser, Integer.parseInt(buyid)); // ??????
		int add = buy.add(orderno, buyuser, Integer.parseInt(buyid));
		out.print("add=" + add);
		// }else{
		// int update = buy.update(orderno, buyuser, Integer.parseInt(buyid));
		// out.print("update="+update);
		// }
		out.flush();
		out.close();
	}

	// ??????????????????
	protected void getOrderws(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String userID_req = request.getParameter("userid");
		String state_req = request.getParameter("state");
		String startdate_req = request.getParameter("startdate");
		String enddate_req = request.getParameter("enddate");
		int showUnpaid = Integer.parseInt(request.getParameter("showUnpaid"));
		String username = request.getParameter("username");
		String orderno = request.getParameter("orderno");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		int page = Utility.getStringIsNull(request.getParameter("page"))
				? Integer.parseInt(request.getParameter("page")) : 1;
		int currentPage = Utility.getStringIsNull(request.getParameter("currentPage"))
				? Integer.parseInt(request.getParameter("currentPage")) : 1;
		String buyid = request.getParameter("buyuser");
		String admuserid_str = request.getParameter("admuserid");
		String status_str = request.getParameter("status");
		int buyuser = 0;
		if (buyid != null && !buyid.isEmpty()) {
			buyuser = Integer.parseInt(buyid);
		}
		userID_req = userID_req != null && !userID_req.equals("") ? userID_req.replaceAll("\\D+", "") : "0";
		int userID = userID_req != null && !userID_req.equals("") ? Integer.parseInt(userID_req) : 0;

		int state = Utility.getStringIsNull(state_req) ? Integer.parseInt(state_req) : -2;
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admJson == null) {
			response.sendRedirect("website/main_login.jsp");
			return;
		}
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
		String strm = user.getRoletype();
		int strname = user.getId();
		if ("0".equals(strm)) {
			strname = Utility.getStringIsNull(admuserid_str) ? Integer.parseInt(admuserid_str) : 0;
		}
		Date startdate = null;
		Date enddate = null;
		if (startdate_req != null && !startdate_req.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startdate = sdf.parse(startdate_req);
		}
		if (enddate_req != null && !enddate_req.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enddate = sdf.parse(enddate_req);
		}
		int status = 0;
		if (status_str != null && !status_str.equals("")) {
			request.setAttribute("status", status_str);
			status = Integer.parseInt(status_str);
		}
		IOrderwsServer server = new OrderwsServer();
		List<OrderBean> list = server.getOrders(userID, state, startdate, enddate, username, email, orderno, phone,
				page, strname, buyuser, showUnpaid, status);
		// int count = server.getOrdersPage(userID, state, date, username,
		// email, orderno, phone);
		int count = 0;
		if (list != null && !list.isEmpty()) {
			count = list.get(0).getTotal();
		}
		request.setAttribute("orderws", JSONArray.toJSON(list));
		UserDao dao = new UserDaoImpl();
		List<ConfirmUserInfo> listAdm = null;
		String action = request.getParameter("action");

		if ("currentUser".equals(action)) {
			String sessionId = request.getSession().getId();
			String admuserJson = Redis.hget(sessionId, "admuser");
			Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

			listAdm = dao.getCurSub(admuser);
		} else {
			listAdm = dao.getAll();
		}
		request.setAttribute("listAdm", JSONArray.toJSON(listAdm));
		request.setAttribute("count", count);
		request.setAttribute("page", page);
		request.setAttribute("admuserid", strname);
		request.setAttribute("buyuser", buyuser);
		request.setAttribute("strname", strname);
		request.setAttribute("strm", strm);
		request.setAttribute("showUnpaid", showUnpaid);

		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/ordermgr.jsp");
		homeDispatcher.forward(request, response);
		/*
		 * map.put("order", list); map.put("count", count); map.put("page",
		 * page); map.put("admuserid", admuserid); map.put("buyuser", buyuser);
		 * PrintWriter out = response.getWriter();
		 * out.print(net.sf.json.JSONArray.fromObject(map)); out.flush();
		 * out.close();
		 */
	}

	// ??????????????????
	protected void getGoodsCar(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String userID_req = request.getParameter("userid");
		String email = request.getParameter("email");
		System.out.println(userID_req);
		System.out.println(email);
		IGoodsServer server = new GoodsServerImpl();
		List<SpiderBean> list = server.getSpiderBeans(Integer.parseInt(userID_req), email);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSON(list));
		out.flush();
		out.close();
	}

	/* ?????????????????? */
	public void getOrderAddress(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderid = request.getParameter("orderid");
		IOrderDao orderDao = new OrderDao();
		Address orderAddr = orderDao.getOrderAddress(orderid);
		PrintWriter out = response.getWriter();
		// System.out.println(orderAddr.toString());
		out.print("[" + orderAddr.toString() + "]");
		out.flush();
		out.close();
	}

	/* ???????????????paypal?????? */
	public void getIpnaddress(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderid = request.getParameter("orderid");
		UserDao dao = new UserDaoImpl();
		Map<String, String> ipnaddress = dao.getIpnaddress(orderid);
		PrintWriter out = response.getWriter();
		out.print(ipnaddress);
		out.flush();
		out.close();
	}

	/* ?????????????????? */
	public void updateOrderAddress(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderid = request.getParameter("orderid");
		String address = request.getParameter("address");
		String address2 = request.getParameter("address2");
		String statename = request.getParameter("statename");
		String countryid = request.getParameter("countryid");
		String zipcode = request.getParameter("zipcode");
		String phonenumber = request.getParameter("phonenumber");
		String recipients = request.getParameter("recipients");
		String street = request.getParameter("street");
		String isDropFlag = request.getParameter("isDropFlag");

		Address addr = new Address();
		IOrderDao orderDao = new OrderDao();
		addr.setAddress(address);
		addr.setAddress2(address2);
		addr.setCountry(countryid);
		addr.setStatename(statename);
		addr.setZip_code(zipcode);
		addr.setPhone_number(phonenumber);
		addr.setRecipients(recipients);
		if("1".equals(isDropFlag)){
			addr.setStreet(address);
		}else{
			addr.setStreet(street);
		}
		orderDao.updateOnlineOrderAddress(addr, orderid); //????????????????????????  2018/08/27 10:38 ly
		int res = orderDao.updateOrderAddress(addr, orderid);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	public void delGoods(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		IOrderDao io = new OrderDao();
		int result = io.delGoods(goodsid, orderid);
		float product_cost = Float.parseFloat(io.getOrderProductcost(orderid));
		float service_fee = 0;
		// 2016-1-7 remove(sj)
		/*
		 * if(product_cost<1000){ service_fee=(float) (product_cost*0.08); }else
		 * if(product_cost<3000){ service_fee=(float) (product_cost*0.06); }else
		 * if(product_cost<5000){ service_fee=(float) (product_cost*0.04); }else
		 * if(product_cost<10000){ service_fee=(float) (product_cost*0.03);
		 * }else{ service_fee=(float) (product_cost*0.025); }
		 */

		io.updateOrderinfo(product_cost + "", service_fee + "", orderid);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}

	// ??????????????????
	public void getPrintDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		List<OrderDetailsBean> odb = server.getOrdersDetails(orderNo);
		List<OrderDetailsBean> temp = new ArrayList<OrderDetailsBean>();
		for (int i = 0; i < odb.size(); i++) {
			if (odb.get(i).getState() != 2) {
				temp.add(odb.get(i));
			}

		}
		// OrderBean order = server.getOrders(orderNo);
		PrintWriter out = response.getWriter();
		System.out.println(JSONArray.toJSONString(temp));
		out.print(JSONArray.toJSONString(temp));
		out.flush();
		out.close();
	}

	/* ???????????????????????? */
	public void getNewTotalPrice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		IOrderwsDao orderwsDao = new OrderwsDao();
		String orderNo = request.getParameter("orderNo");
		List<String> res = orderwsDao.getNewTotalPrice(orderNo);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(res));
		out.flush();
		out.close();
	}

	/* ?????????????????????????????????????????????????????? */
	public void addUserInCharge(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		IOrderwsDao orderwsDao = new OrderwsDao();
		UserDao dao = new UserDaoImpl();
		String amid = request.getParameter("adminid");
		amid = amid == null ? "0" : amid;
		amid = amid.isEmpty() ? "0" : amid;
		int adminid = Integer.parseInt(amid);
		int userid = Integer.parseInt(request.getParameter("userid"));
		String username = request.getParameter("username");
		String useremail = request.getParameter("useremail");
		String admName = request.getParameter("admName");
		int res = 0;
		if (orderwsDao.queryUserInCharge(userid) > 0) {
			res = orderwsDao.updateUserInCharge(userid, adminid, admName);
		} else {
			String users = "";
			// res=dao.addUserInCharge(userid,username,useremail,adminid,admName);
			res = dao.updateAdminuser(userid, adminid, users, useremail, username, admName);// ????????????
			// ??????????????????
		}
		// ?????????????????????????????????????????????????????????????????????
		List<String> orderNosList = orderwsDao.getOrdersNos(userid);
		for (int i = 0; i < orderNosList.size(); i++) {
			ISpidersServer spiders = new SpidersServer();
			// spiders.sendEmail(orderNosList.get(i), username, useremail,
			// userid);
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	public void afterReplenishment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String[] data = request.getParameter("parm").split(",");
		IOrderwsServer server = new OrderwsServer();
		String newOrderid = "";
		String oldOrderid="";
		int row = 0;
		if (data.length > 0) {
			oldOrderid = data[0].split(":")[2].toString();
			newOrderid = oldOrderid + "_" + oldOrderid.substring(oldOrderid.length() - 4, oldOrderid.length());
			LOG.info("????????????????????????:" + newOrderid);
			row = +server.addOrderInfo(data[0].split(":")[2].toString(), newOrderid, data.length);
			if (row > 0) {
				// ??????????????????????????????
				int state = server.judgeOrderState(newOrderid);
				if (state == -1 || state == 6) {
					Random random = new Random();
					int randomInt = random.nextInt(90) + 10;
					newOrderid = oldOrderid + "_" + oldOrderid.substring(oldOrderid.length() - 4, oldOrderid.length())
							+ randomInt;
					LOG.info("????????????????????????:" + newOrderid);
					row = +server.addOrderInfo(data[0].split(":")[2].toString(), newOrderid, data.length);
				}
				//??????????????????????????????
				int admuserid=server.addAutoAdmuser(oldOrderid);
				for (int i = 0; i < data.length; i++) {
					String[] obj = data[i].split(":");
					String goodsid = obj[0].toString();
					String count = obj[1].toString();
					String orderid = obj[2].toString();
					LOG.info("????????????:" + goodsid + "\t ????????????:" + count + "\t ?????????:" + newOrderid);
					row += server.addOrderDetails(goodsid, count, newOrderid, orderid,admuserid);
				}
			}
		}
		PrintWriter out = response.getWriter();
		if (row - 1 != data.length) {
			LOG.info("???????????????????????????????????????");
			boolean flag = server.deleteOrderInfo(newOrderid);
			if (flag) {
				LOG.info("????????????");
			} else {
				LOG.info("????????????");
			}
			out.print("0");
		} else {
			out.print("1");
		}
		out.flush();
		out.close();
	}

	// ?????????????????????????????????
	public void getOrderDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String orderNo = request.getParameter("orderNo");
		String payTime = request.getParameter("paytime");
		request.setAttribute("payToTime", payTime);
		IOrderwsServer server = new OrderwsServer();
		//??????????????????????????????
		List<com.cbt.pojo.Admuser>  aublist = server.getAllBuyer();
		request.setAttribute("aublist", aublist);

		// ????????????
		OrderBean order = server.getOrders(orderNo);

		PaymentDaoImp paymentDao = new PaymentDao();
		String fileByOrderid = paymentDao.getFileByOrderid(orderNo);
		if (fileByOrderid == null || fileByOrderid.length() < 10) {
			request.setAttribute("invoice", "0");
		} else {
			if (fileByOrderid.indexOf(".pdf") > -1) {
				request.setAttribute("invoice", "1");
			} else {
				request.setAttribute("invoice", "2");
			}
		}
		//??????????????????????????????????????????
		server.updateGoodsCarMessage(orderNo);
		// ??????????????????
		List<OrderDetailsBean> odb = server.getOrdersDetails(orderNo); // orderDetail
		for(int i=0;i<odb.size();i++){
			OrderDetailsBean o=odb.get(i);
			if(o.getConfirm_userid()!=null && !"".equals(o.getConfirm_userid())){
				String admName=dao.queryBuyCount(Integer.valueOf(o.getConfirm_userid()));
				TaoBaoOrderInfo t=server.getShipStatusInfo(o.getTb_1688_itemid(),o.getLast_tb_1688_itemid(),o.getConfirm_time().substring(0,10),admName,o.getShipno(),o.getOffline_purchase(),o.getOrderid(),o.getGoodsid());
				if(t!=null && t.getShipstatus()!=null && t.getShipstatus().length()>0){
					String shipstatus = t.getShipstatus().split("\n")["2".equals(t.getTbOr1688())?0:t.getShipstatus().split("\n").length - 1];
					if("2".equals(t.getTbOr1688())){
						String msg=t.getShipstatus().split("\n")[1];
						o.setShipstatus(shipstatus+"\n "+msg);
					}else{
						o.setShipstatus(shipstatus);
					}
				}
				o.setShipno(t.getShipno());
			}
		}
		Forwarder forw = null;
		int state = order.getState();

		// if(state == 7){
		// //??????
		// AdvanceOrderBean advance = server.getAdvanceBean(orderNo);
		// request.setAttribute("advance", advance);
		// }

		// ????????????????????????
		// PaymentConfirmServer paymentConfirmServer=(PaymentConfirmServer)
		// WebsiteServlet.context.getBean("paymentConfirmServerImpl");
		// Map<String, Object>
		// map=paymentConfirmServer.getPaymentConfirm(orderNo);

		// zp ?????? ????????????????????????????????????
		Map<String, Object> map = new HashMap<String, Object>();
		// ???????????????map order
		// for(int i=0; i<odb.size(); i++){
		// OrderDetailsBean orderdeta = odb.get(i);
		// map.put("id", orderdeta.getDzId());
		// map.put("orderno", orderdeta.getDzOrderno());
		// map.put("confirmname", orderdeta.getDzConfirmname());
		// map.put("confirmtime", orderdeta.getDzConfirmtime());
		// }

		map.put("id", order.getDzId());
		map.put("orderno", order.getDzOrderno());
		map.put("confirmname", order.getDzConfirmname());
		map.put("confirmtime", order.getDzConfirmtime());

		String str_oid = ""; // ??????order_detail??? id
		// ?????????????????????
		float sumPrice = 0;

		String purchasetime_order = "";
		double preferential_price = 0;
		for (int i = 0; i < odb.size(); i++) {
			if (state == 1) {
				preferential_price += odb.get(i).getPreferential_price();
				String purchase_time = odb.get(i).getPurchase_time();
				if (Utility.getStringIsNull(purchase_time)) {
					if (Utility.getStringIsNull(purchasetime_order)) {
						long purchase_time_long = Long
								.valueOf(purchase_time.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
						long purchasetime_order_long = Long
								.valueOf(purchasetime_order.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
						purchasetime_order = purchase_time_long > purchasetime_order_long ? purchase_time
								: purchasetime_order;
					} else {
						purchasetime_order = purchase_time;
					}
				}
			}
			if (odb.get(i).getOrsstate() > 0) {
				sumPrice += odb.get(i).getSumGoods_p_price();
			}

			str_oid = str_oid + odb.get(i).getId() + ",";
			String goods_img = odb.get(i).getGoods_img();
			if (goods_img != null && goods_img.equals("/img/1.png")) {
				odb.get(i).setGoods_img("/cbtconsole/img/1.png");
			}
		}

		String arrive_time1 = "";
		String arrive_time = order.getMode_transport();
		int arrive = 0;
		String shipMethod = "0";
		if (!Utility.getStringIsNull(order.getExpect_arrive_time())) {
			arrive_time1 = order.getExpect_arrive_time();
			arrive = -1;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		// String countryName = "";
		if (Utility.getStringIsNull(arrive_time)) {
			if (arrive_time.indexOf("@") > -1) {
				String[] strings = arrive_time.split("@");
				arrive_time = strings[1];
				shipMethod = strings[0];
				// countryName = strings.length > 1 ? strings[2] : "";
				if (arrive_time.indexOf("-") > -1) {
					String[] arrive_time_ = arrive_time.split("-");
					// int min = Utility.getIsInt(arrive_time_[0]) ?
					// Integer.parseInt(arrive_time_[0]):0;
					int max = Utility.getIsInt(arrive_time_[1]) ? Integer.parseInt(arrive_time_[1]) : 0;
					arrive = max;
				} else {
					arrive = Utility.getIsInt(arrive_time) ? Integer.parseInt(arrive_time) : 0;
				}

				if (arrive != 0 && arrive != -1) {
					Calendar c = Calendar.getInstance();
					if (Utility.getStringIsNull(order.getTransport_time())) {
						c.setTime(sf.parse(order.getTransport_time()));
					}
					c.add(Calendar.DAY_OF_MONTH, arrive);
					arrive_time = sf.format(c.getTime());
				}
			} else {
				arrive_time = "";
			}
		}
		if (state == 3) {
			forw = server.getForwarder(orderNo);
			// zlw add start
			// ??????????????????
			List<CodeMaster> logisticsList = server.getLogisticsInfo();
			request.setAttribute("logisticsList", logisticsList);
			// zlw add end
		} else if (state == 4) {
			Evaluate evaluate = server.getEvaluate(orderNo);
			request.setAttribute("evaluate", evaluate);
		} else if (state != 0 || state != -1 || state != 7) {
			// ??????????????????
			List<Map<String, String>> pays = server.getOrdersPays(orderNo);
			request.setAttribute("pays", pays);
			// ?????????????????????
			String pay_time = "";
			for (int i = 0; i < pays.size(); i++) {
				String order_pay = pays.get(i).get("orderid");
				if (orderNo.equals(order_pay)) {
					pay_time = pays.get(i).get("createtime");
				}
			}
			if (Utility.getStringIsNull(pay_time)) {
				// ????????????+??????????????????+??????????????????<????????????
				Calendar c = Calendar.getInstance();
				c.setTime(sf.parse(pay_time));
				if (state == 5) {// 48????????????????????????
					c.add(Calendar.DAY_OF_MONTH, 2);
					if ((new Date().getTime()) > c.getTime().getTime()) {
						request.setAttribute("delivery_warning", "(????????????" + sf.format(c.getTime()) + ")");
					}
				} else if (state == 1) {// ???????????????-2???<???????????? ?????????
					c.add(Calendar.DAY_OF_MONTH, order.getDeliveryTime() - 2);
					if ((new Date().getTime()) > c.getTime().getTime()) {
						request.setAttribute("delivery_warning", "(????????????" + sf.format(c.getTime()) + ")");
					}
				} else if (state == 2) {// ???????????????+???????????????<???????????? ?????????
					c.add(Calendar.DAY_OF_MONTH, order.getDeliveryTime());
					if ((new Date().getTime()) > c.getTime().getTime()) {
						request.setAttribute("delivery_warning", "(????????????" + sf.format(c.getTime()) + ")");
					}
				}
			}
		}
		// DataSourceSelector.restore();

		if (arrive != 0) {
			request.setAttribute("expect_arrive_time", arrive == -1 ? arrive_time1 : arrive_time);// ??????????????????
		}
		if (str_oid.length() > 0) {
			str_oid = str_oid.substring(0, str_oid.length() - 1);
		}
		request.setAttribute("str_oid", str_oid);
		request.setAttribute("shipMethod", shipMethod);
		request.setAttribute("orderNo", orderNo);
		request.setAttribute("orderDetail", odb); // ??????????????????
		request.setAttribute("order", order);
		// ????????????
		Double actual_ffreight_ = Utility.getIsDouble(order.getActual_ffreight())
				? Double.parseDouble(order.getActual_ffreight()) : 0;
		request.setAttribute("actual_ffreight_", actual_ffreight_);
		// service_fee????????????????????????
		request.setAttribute("service_fee",
				Utility.getIsDouble(order.getService_fee()) ? Double.parseDouble(order.getService_fee()) : 0);
		request.setAttribute("cashback", order.getCashback());
		String mode_transport = "";
		int isFree = 0;
		// if(order.getState() == 5){
		int isSplitOrder = 0;
		if (order.getActual_freight_c() != 0) {
			isSplitOrder = 1;
		}

		if (Utility.getStringIsNull(order.getMode_transport())) {
			String[] mode_transport_ = order.getMode_transport().split("@");
			for (int j = 0; j < mode_transport_.length; j++) {
				if (j == mode_transport_.length - 2
						&& Utility.getIsDouble(mode_transport_[mode_transport_.length - 2])) {
					if (Double.parseDouble(mode_transport_[mode_transport_.length - 2]) == 0) {
						if (mode_transport_[mode_transport_.length - 1].equals("all")) {
							mode_transport_[mode_transport_.length - 1] = "??????";
							isFree = 1;
						} else {
							mode_transport_[mode_transport_.length - 1] = "????????????";
							isFree = 1;
						}
					}
				}
				if (j == mode_transport_.length - 1) {
					if (mode_transport_[j].equals("product")) {
						mode_transport_[j] = "????????????";
					} else if (mode_transport_[j].equals("all")) {
						if (isFree != 1) {
							mode_transport_[j] = "??????";
						}
					}
				}
				mode_transport += mode_transport_[j];
				if (mode_transport.indexOf(".") > -1) {
					mode_transport = mode_transport.split("\\.")[0];
				}
				if (j != mode_transport_.length - 1)
					mode_transport += "@";
			}
		}

		/*
		 * if(orderNo.indexOf("_") > -1){ isSplitOrder = 1; }
		 * request.setAttribute("isSplitOrder", isSplitOrder); //zp??????
		 */
		// ???????????????Drop Ship??????????????????????????????????????????
		OrderInfoDao orderInfoDao = new OrderInfoImpl();
		OrderBean orderBean = orderInfoDao.getOrderInfo(orderNo, null);
		request.setAttribute("isSplitOrder", orderBean.getIsDropshipOrder());

		// ???????????????????????? order???????????????-----ylm??????????????????????????????????????????
		IUserDao userDao = new com.cbt.processes.dao.UserDao();

		order.setApplicable_credit(
				new BigDecimal(order.getApplicable_credit()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		// }

		// ?????????????????????
		request.setAttribute("preferential_price", preferential_price);
		request.setAttribute("sumPrice", sumPrice);
		// ??????????????????
		request.setAttribute("purchasetime_order", purchasetime_order);
		// ????????????????????????->?????????????????????????????? ??????
		request.setAttribute("orderNos", server.getOrderNos(order.getUserid(),orderNo));
		// request.setAttribute("countryName", countryName);
		// ?????????????????????????????????
		// int count = server.getChangegooddata(orderNo);
		int count = order.getPackage_style(); // zp??????
		request.setAttribute("count", count);

		/* ????????????????????????---2016.7.25.abc---start */
		// ???????????????????????????????????????????????????????????????????????????????????????
		if (order.getState() == 2) {
			double unpaid_freight = 0.0;
			if (mode_transport.indexOf("????????????") > -1 || mode_transport.indexOf("??????") > -1) {
				unpaid_freight = order.getRemaining_price();
			}
			// ??????????????????
			if (order.getRemaining_price() > 0) {
				request.setAttribute("unpaid_freight", unpaid_freight);
				request.setAttribute("freight_deduction", "1");
			}
		}

		/* ????????????????????????---2016.7.25.abc----end */
		request.setAttribute("mode_transport", mode_transport);
		request.setAttribute("isFree", isFree);
		request.setAttribute("forwarder", forw);
		request.setAttribute("paymentconfirm", map);

		// 7.21 ?????????????????????????????????
		String[] strs = mode_transport.split("@");
		int jq = 0;
		// for(int i=0;i<strs.length;i++){
		if (strs.length > 1 && strs[1].indexOf("-") > 0) {
			String[] jqs = null;
			if (strs[1].indexOf("Days") > 0) { // ??????????????? Days?????????
				jqs = strs[1].substring(0, strs[1].indexOf(" Days")).split("-");
			} else {
				jqs = strs[1].split("-");
			}
			for (int j = 0; j < jqs.length; j++) {
				jq += Integer.parseInt(jqs[j]);
			}
		}
		// }
		// ????????????
		ICurrencyService currencyDao = new CurrencyService();
		double rate = currencyDao.currencyConverter("RMB");

		double sp = Double.parseDouble(order.getProduct_cost() == null ? "0.0" : order.getProduct_cost());
		double hp = order.getDiscount_amount();
		double sale = (sp - hp) * rate; // ?????????
		double buy = 0.0; // ?????????
		double volume = 0.0; // ?????????
		double weight = 0.0; // ?????????
		for (int i = 0; i < odb.size(); i++) {
			if (odb.get(i).getState() != 2) { // order_detail???state???2??????????????????????????????????????????????????????
				buy += odb.get(i).getSumGoods_p_price();
				//volume += odb.get(i).getOd_bulk_volume();
				weight += odb.get(i).getOd_total_weight();
			}
		}
		request.setAttribute("avg_jq", jq / 2); // ???????????????
		request.setAttribute("sale", Math.round(sale * 100) / 100.0);
		request.setAttribute("buy", Math.round(buy * 100) / 100.0);
		request.setAttribute("volume", Math.round(volume * 1000) / 1000.0);
		request.setAttribute("weight", Math.round(weight * 1000) / 1000.0);
		String countryid = "0";
		if (odb.size() != 0 && odb != null) {
			countryid = odb.get(0).getCountry();
			if ("42".equals(countryid) || "43".equals(countryid)) {
				countryid = "36";
			}
		}

		request.setAttribute("country", countryid);
		request.setAttribute("countryName",
				(order.getMode_transport() == null || "".equals(order.getMode_transport())) ? "USA" : strs[2]);
		request.setAttribute("orderNo", order.getOrderNo());
		request.setAttribute("rate", rate);
		request.setAttribute("userid", order.getUserid());
		String lirun1 = null;
		if (sale == 0.0 || buy == 0.0) {
			lirun1 = "--";
		} else {
			lirun1 = String.format("%.2f", ((sale - buy) / sale * 100)) + "%";
		}
		request.setAttribute("lirun1", lirun1);
		request.setAttribute("isDropshipOrder", order.getIsDropshipOrder());
		//????????????????????????
		IEmailReceiveService service = new EmailReceiveServiceImpl();
		//????????????????????????
		List<EmailReceive1> emaillist=service.getall(orderNo);
		request.setAttribute("emaillist", emaillist);
		// ?????????????????????????????????---HomTU
		// List urlBeforeList = server.getOldUrl();
		// request.setAttribute("urlbeforelist", urlBeforeList);
		String stateString = request.getParameter("state1");
		if (!Utility.getStringIsNull(stateString)) {
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/order_detail1.jsp");
			homeDispatcher.forward(request, response);
			return;
		}
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/order_detail.jsp");
		homeDispatcher.forward(request, response);
	}

	// cjc19.09
	// ????????????????????????????????????
	public void getChildrenOrderDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo = request.getParameter("orderNo");
		String payTime = request.getParameter("paytime");
		request.setAttribute("payToTime", payTime);
		String isDropshipOrder = request.getParameter("isDropshipOrder");
		IOrderwsServer server = new OrderwsServer();
		// ????????????
		OrderBean order = server.getChildrenOrders(orderNo);

		PaymentDaoImp paymentDao = new PaymentDao();
		String fileByOrderid = paymentDao.getFileByOrderid(orderNo);
		if (fileByOrderid == null || fileByOrderid.length() < 10) {
			request.setAttribute("invoice", "0");
		} else {
			if (fileByOrderid.indexOf(".pdf") > -1) {
				request.setAttribute("invoice", "1");
			} else {
				request.setAttribute("invoice", "2");
			}
		}
		// ?????????????????? --getChildrenOrdersDetails
		List<OrderDetailsBean> odb = server.getChildrenOrdersDetails(orderNo); // orderDetail
		Forwarder forw = null;
		int state = order.getState();

		// if(state == 7){
		// //??????
		// AdvanceOrderBean advance = server.getAdvanceBean(orderNo);
		// request.setAttribute("advance", advance);
		// }

		// ????????????????????????
		// PaymentConfirmServer paymentConfirmServer=(PaymentConfirmServer)
		// WebsiteServlet.context.getBean("paymentConfirmServerImpl");
		// Map<String, Object>
		// map=paymentConfirmServer.getPaymentConfirm(orderNo);

		// zp ?????? ????????????????????????????????????
		Map<String, Object> map = new HashMap<String, Object>();
		// ???????????????map order
		// for(int i=0; i<odb.size(); i++){
		// OrderDetailsBean orderdeta = odb.get(i);
		// map.put("id", orderdeta.getDzId());
		// map.put("orderno", orderdeta.getDzOrderno());
		// map.put("confirmname", orderdeta.getDzConfirmname());
		// map.put("confirmtime", orderdeta.getDzConfirmtime());
		// }

		map.put("id", order.getDzId());
		map.put("orderno", order.getDzOrderno());
		map.put("confirmname", order.getDzConfirmname());
		map.put("confirmtime", order.getDzConfirmtime());

		String str_oid = ""; // ??????order_detail??? id
		// ?????????????????????
		float sumPrice = 0;

		String purchasetime_order = "";
		double preferential_price = 0;
		for (int i = 0; i < odb.size(); i++) {
			if (state == 1) {
				preferential_price += odb.get(i).getPreferential_price();
				String purchase_time = odb.get(i).getPurchase_time();
				if (Utility.getStringIsNull(purchase_time)) {
					if (Utility.getStringIsNull(purchasetime_order)) {
						long purchase_time_long = Long
								.valueOf(purchase_time.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
						long purchasetime_order_long = Long
								.valueOf(purchasetime_order.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
						purchasetime_order = purchase_time_long > purchasetime_order_long ? purchase_time
								: purchasetime_order;
					} else {
						purchasetime_order = purchase_time;
					}
				}
			}
			if (odb.get(i).getOrsstate() > 0) {
				sumPrice += odb.get(i).getSumGoods_p_price();
			}

			str_oid = str_oid + odb.get(i).getId() + ",";
			String goods_img = odb.get(i).getGoods_img();
			if (goods_img != null && goods_img.equals("/img/1.png")) {
				odb.get(i).setGoods_img("/cbtconsole/img/1.png");
			}
		}

		String arrive_time1 = "";
		String arrive_time = order.getMode_transport();
		int arrive = 0;
		String shipMethod = "0";
		if (!Utility.getStringIsNull(order.getExpect_arrive_time())) {
			arrive_time1 = order.getExpect_arrive_time();
			arrive = -1;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		// String countryName = "";
		if (Utility.getStringIsNull(arrive_time)) {
			if (arrive_time.indexOf("@") > -1) {
				String[] strings = arrive_time.split("@");
				arrive_time = strings[1];
				shipMethod = strings[0];
				// countryName = strings.length > 1 ? strings[2] : "";
				if (arrive_time.indexOf("-") > -1) {
					String[] arrive_time_ = arrive_time.split("-");
					// int min = Utility.getIsInt(arrive_time_[0]) ?
					// Integer.parseInt(arrive_time_[0]):0;
					int max = Utility.getIsInt(arrive_time_[1]) ? Integer.parseInt(arrive_time_[1]) : 0;
					arrive = max;
				} else {
					arrive = Utility.getIsInt(arrive_time) ? Integer.parseInt(arrive_time) : 0;
				}

				if (arrive != 0 && arrive != -1) {
					Calendar c = Calendar.getInstance();
					if (Utility.getStringIsNull(order.getTransport_time())) {
						c.setTime(sf.parse(order.getTransport_time()));
					}
					c.add(Calendar.DAY_OF_MONTH, arrive);
					arrive_time = sf.format(c.getTime());
				}
			} else {
				arrive_time = "";
			}
		}
		if (state == 3) {
			forw = server.getForwarder(orderNo);
			// zlw add start
			// ??????????????????
			List<CodeMaster> logisticsList = server.getLogisticsInfo();
			request.setAttribute("logisticsList", logisticsList);
			// zlw add end
		} else if (state == 4) {
			Evaluate evaluate = server.getEvaluate(orderNo);
			request.setAttribute("evaluate", evaluate);
		} else if (state != 0 || state != -1 || state != 7) {
			// ??????????????????
			List<Map<String, String>> pays = server.getOrdersPays(orderNo);
			request.setAttribute("pays", pays);
			// ?????????????????????
			String pay_time = "";
			for (int i = 0; i < pays.size(); i++) {
				String order_pay = pays.get(i).get("orderid");
				if (orderNo.equals(order_pay)) {
					pay_time = pays.get(i).get("createtime");
				}
			}
			if (Utility.getStringIsNull(pay_time)) {
				// ????????????+??????????????????+??????????????????<????????????
				Calendar c = Calendar.getInstance();
				c.setTime(sf.parse(pay_time));
				if (state == 5) {// 48????????????????????????
					c.add(Calendar.DAY_OF_MONTH, 2);
					if ((new Date().getTime()) > c.getTime().getTime()) {
						request.setAttribute("delivery_warning", "(????????????" + sf.format(c.getTime()) + ")");
					}
				} else if (state == 1) {// ???????????????-2???<???????????? ?????????
					c.add(Calendar.DAY_OF_MONTH, order.getDeliveryTime() - 2);
					if ((new Date().getTime()) > c.getTime().getTime()) {
						request.setAttribute("delivery_warning", "(????????????" + sf.format(c.getTime()) + ")");
					}
				} else if (state == 2) {// ???????????????+???????????????<???????????? ?????????
					c.add(Calendar.DAY_OF_MONTH, order.getDeliveryTime());
					if ((new Date().getTime()) > c.getTime().getTime()) {
						request.setAttribute("delivery_warning", "(????????????" + sf.format(c.getTime()) + ")");
					}
				}
			}
		}
		// DataSourceSelector.restore();

		if (arrive != 0) {
			request.setAttribute("expect_arrive_time", arrive == -1 ? arrive_time1 : arrive_time);// ??????????????????
		}
		if (str_oid.length() > 0) {
			str_oid = str_oid.substring(0, str_oid.length() - 1);
		}
		request.setAttribute("str_oid", str_oid);
		request.setAttribute("shipMethod", shipMethod);
		request.setAttribute("orderNo", orderNo);
		request.setAttribute("orderDetail", odb); // ??????????????????
		request.setAttribute("order", order);
		// ????????????
		Double actual_ffreight_ = Utility.getIsDouble(order.getActual_ffreight())
				? Double.parseDouble(order.getActual_ffreight()) : 0;
		request.setAttribute("actual_ffreight_", actual_ffreight_);
		// service_fee????????????????????????
		request.setAttribute("service_fee",
				Utility.getIsDouble(order.getService_fee()) ? Double.parseDouble(order.getService_fee()) : 0);
		request.setAttribute("cashback", order.getCashback());
		String mode_transport = "";
		int isFree = 0;
		// if(order.getState() == 5){
		int isSplitOrder = 0;
		if (order.getActual_freight_c() != 0) {
			isSplitOrder = 1;
		}

		if (Utility.getStringIsNull(order.getMode_transport())) {
			String[] mode_transport_ = order.getMode_transport().split("@");
			for (int j = 0; j < mode_transport_.length; j++) {
				if (j == mode_transport_.length - 2
						&& Utility.getIsDouble(mode_transport_[mode_transport_.length - 2])) {
					if (Double.parseDouble(mode_transport_[mode_transport_.length - 2]) == 0) {
						if (mode_transport_[mode_transport_.length - 1].equals("all")) {
							mode_transport_[mode_transport_.length - 1] = "??????";
							isFree = 1;
						} else {
							mode_transport_[mode_transport_.length - 1] = "????????????";
							isFree = 1;
						}
					}
				}
				if (j == mode_transport_.length - 1) {
					if (mode_transport_[j].equals("product")) {
						mode_transport_[j] = "????????????";
					} else if (mode_transport_[j].equals("all")) {
						if (isFree != 1) {
							mode_transport_[j] = "??????";
						}
					}
				}
				mode_transport += mode_transport_[j];
				if (mode_transport.indexOf(".") > -1) {
					mode_transport = mode_transport.split("\\.")[0];
				}
				if (j != mode_transport_.length - 1)
					mode_transport += "@";
			}
		}

		if (orderNo.indexOf("_") > -1) {
			isSplitOrder = 1;
		}
		request.setAttribute("isSplitOrder", isSplitOrder); // zp??????

		// ???????????????????????? order???????????????-----ylm??????????????????????????????????????????
		IUserDao userDao = new com.cbt.processes.dao.UserDao();

		// String currency_u = userDao.getUserCurrency(order.getUserid());

		// double exchange_rate = 1;
		// if(Utility.getStringIsNull(currency_u) && order.getCurrency() != null
		// && !currency_u.equals(order.getCurrency())){
		////// ??????????????????
		// ISpiderDao spiderDao = new SpiderDao();
		// Map<String, Double> exchangeRate =
		// spiderDao.getExchangeRate();//???????????????
		// exchange_rate = (float)
		// (exchangeRate.get(order.getCurrency())/exchangeRate.get(currency_u));//??????
		//
		// //?????????????????? ??????????????? zp??????
		//// DecimalFormat df=new DecimalFormat("#0.##");
		//// Map<String, Double> maphl = Currency.getMaphl(request);
		//// //eur CAD:0.00 GBP:0.00 AUD:0.00
		//// exchange_rate = maphl.get("USD");
		//// exchange_rate = exchange_rate/maphl.get(currency_u);
		//
		// }
		order.setApplicable_credit(
				new BigDecimal(order.getApplicable_credit()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		// }

		// ?????????????????????
		request.setAttribute("preferential_price", preferential_price);
		request.setAttribute("sumPrice", sumPrice);
		// ??????????????????
		request.setAttribute("purchasetime_order", purchasetime_order);
		// ????????????????????????->?????????????????????????????? ??????
		request.setAttribute("orderNos", server.getOrderNos(order.getUserid(),orderNo));
		// request.setAttribute("countryName", countryName);
		// ?????????????????????????????????
		// int count = server.getChangegooddata(orderNo);
		int count = order.getPackage_style(); // zp??????
		request.setAttribute("count", count);

		/* ????????????????????????---2016.7.25.abc---start */
		// ???????????????????????????????????????????????????????????????????????????????????????
		if (order.getState() == 2) {
			double unpaid_freight = 0.0;
			if (mode_transport.indexOf("????????????") > -1 || mode_transport.indexOf("??????") > -1) {
				unpaid_freight = order.getRemaining_price();
			}
			// ??????????????????
			if (order.getRemaining_price() > 0) {
				request.setAttribute("unpaid_freight", unpaid_freight);
				request.setAttribute("freight_deduction", "1");
			}
		}

		/* ????????????????????????---2016.7.25.abc----end */
		request.setAttribute("mode_transport", mode_transport);
		request.setAttribute("isFree", isFree);
		request.setAttribute("forwarder", forw);
		request.setAttribute("paymentconfirm", map);

		// 7.21 ?????????????????????????????????
		String[] strs = mode_transport.split("@");
		int jq = 0;
		// for(int i=0;i<strs.length;i++){
		if (strs.length > 1 && strs[1].indexOf("-") > 0) {
			String[] jqs = null;
			if (strs[1].indexOf("Days") > 0) { // ??????????????? Days?????????
				jqs = strs[1].substring(0, strs[1].indexOf(" Days")).split("-");
			} else {
				jqs = strs[1].split("-");
			}
			for (int j = 0; j < jqs.length; j++) {
				jq += Integer.parseInt(jqs[j]);
			}
		}
		// }
		// ????????????
		ICurrencyService currencyDao = new CurrencyService();
		double rate = currencyDao.currencyConverter("RMB");

		double sp = Double.parseDouble(order.getProduct_cost() == null ? "0.0" : order.getProduct_cost());
		double hp = order.getDiscount_amount();
		double sale = (sp - hp) * rate; // ?????????
		double buy = 0.0; // ?????????
		double volume = 0.0; // ?????????
		double weight = 0.0; // ?????????
		for (int i = 0; i < odb.size(); i++) {
			if (odb.get(i).getState() != 2) { // order_detail???state???2??????????????????????????????????????????????????????
				buy += odb.get(i).getSumGoods_p_price();
				//volume += odb.get(i).getOd_bulk_volume();
				weight += odb.get(i).getOd_total_weight();
			}
		}
		request.setAttribute("avg_jq", jq / 2); // ???????????????
		request.setAttribute("sale", Math.round(sale * 100) / 100.0);
		request.setAttribute("buy", Math.round(buy * 100) / 100.0);
		request.setAttribute("volume", Math.round(volume * 1000) / 1000.0);
		request.setAttribute("weight", Math.round(weight * 1000) / 1000.0);
		String countryid = "0";
		if (odb.size() != 0 && odb != null) {
			countryid = odb.get(0).getCountry();
			if ("42".equals(countryid) || "43".equals(countryid)) {
				countryid = "36";
			}
		}
		request.setAttribute("country", countryid);
		request.setAttribute("countryName",
				(order.getMode_transport() == null || "".equals(order.getMode_transport())) ? "USA" : strs[2]);
		request.setAttribute("orderNo", order.getOrderNo());
		request.setAttribute("rate", rate);
		request.setAttribute("userid", order.getUserid());
		request.setAttribute("isDropshipOrder1", isDropshipOrder);
		String lirun1 = null;
		if (sale == 0.0 || buy == 0.0) {
			lirun1 = "--";
		} else {
			lirun1 = String.format("%.2f", ((sale - buy) / sale * 100)) + "%";
		}
		request.setAttribute("lirun1", lirun1);
		request.setAttribute("isDropshipOrder", order.getIsDropshipOrder());
		// ?????????????????????????????????---HomTU
		// List urlBeforeList = server.getOldUrl();
		// request.setAttribute("urlbeforelist", urlBeforeList);
		String stateString = request.getParameter("state1");
		if (!Utility.getStringIsNull(stateString)) {
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/order_detail1.jsp");
			homeDispatcher.forward(request, response);
			return;
		}
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/order_detail.jsp");
		homeDispatcher.forward(request, response);
	}

	public void queryCountryNameByOrderNo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		String countryname = server.queryCountryNameByOrderNo(orderNo);
		if (countryname.equals("USA EAST") || countryname.equals("USA WEST")) {
			countryname = "USA";
		}
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(countryname);
		out.close();
	}

	public void getConfigrm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		// ????????????confirm?????????
		List<Object[]> confirmList = server.getOrderChanges(orderNo);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(confirmList));
		out.flush();
		out.close();
	}

	// ??????????????????????????????
	public void upOrderDetailState(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderDetailID = request.getParameter("orderDetailID");
		String state = request.getParameter("state");
		String userId = request.getParameter("userId");
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		int res = server.upOrderDeatail(Integer.parseInt(orderDetailID), Integer.parseInt(state), orderNo,
				Integer.parseInt(userId));
		IMessageServer messageServer = new MessageServer();
		int result = messageServer.addMessage(Integer.parseInt(userId),
				"You have the goods have arrived at the warehouse", orderNo);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	// ????????????????????????????????????
	public void upOrderDetailDepot(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		// ?????????????????????????????????????????????????????????????????????????????????????????? &???????????????????????????????????????????????????
		// ????????????????????????????????????????????????????????????
		Map<String, String> map = new HashMap<String, String>();
		// D:\frequency\portrait
		String s = request.getSession().getServletContext().getRealPath("/" + "//img//ShoppingImg");
		map = inio(request, response, s);
		String actual_volume = map.get("longc") + "*" + map.get("wight") + "*" + map.get("hight");// ????????????
		// List<OrderDetailsBean> odb =
		// server.getOrdersDetails(orderNo,actual_ffreight,custom_discuss_fright,custom_discuss_other,actual_price,fileupload,actual_freight);
		// request.setAttribute("orderDetail", odb);
		Iterator<String> iter = map.keySet().iterator();
		IOrderwsServer server = new OrderwsServer();
		/*
		 * while (iter.hasNext()) { String key = iter.next();
		 *
		 * System.out.println("map:"+key+","+map.get(key));
		 *
		 * }
		 */
		int res = server.upOrderDeatail(Integer.parseInt(map.get("orderDetailID")), iso, map.get("actual_weight"),
				actual_volume, map.get("actual_price"), map.get("actual_freight"), map.get("fileupload"));
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher(
				"/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo=" + map.get("orderNo"));
		homeDispatcher.forward(request, response);
	}

	// ??????????????????order_detail
	public void getOrderCommunicationJS(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		List<Object[]> orderChange = server.getOrderChanges(orderNo, 0);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(orderChange));
		out.flush();
		out.close();
	}

	// ??????????????????
	public void getOrderCommunication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		int goodId = Integer.parseInt(request.getParameter("goodId"));
		int changeType = Integer.parseInt(request.getParameter("changeType"));
		int lastNum = 0;
		IOrderwsServer server = new OrderwsServer();
		String lastNumStr = request.getParameter("lastNum");
		if (Utility.getStringIsNull(lastNumStr)) {
			lastNum = Integer.parseInt(lastNumStr);
		}
		Map<String, Object> orderChange = server.getOrderChange(orderNo, goodId, changeType, lastNum);
		Integer maxId = (Integer) orderChange.get("maxId");
		request.setAttribute("maxId", maxId);
		request.setAttribute("cont", (List<String>) orderChange.get("cont"));
		request.setAttribute("goodid", goodId);
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("website/orderchange_communicate.jsp");
		homeDispatcher.forward(request, response);
	}

	// ?????????????????? ??????????????? ????????????
	public void sendCutomers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = null;
		String orderNo1 = request.getParameter("orderNo");
		int whichOne = Integer.parseInt(request.getParameter("whichOne"));
		int isDropship = Integer.parseInt(request.getParameter("isDropship"));// ??????????????????isDropship??????
		// 0?????????1???
		if (isDropship == 1) {
			orderNo = orderNo1.substring(0, orderNo1.indexOf("_"));
		} else {
			orderNo = orderNo1;
		}
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		IOrderwsServer server = new OrderwsServer();
		// "/cbtconsole/AbstractServlet?action=getOrders&className=OrderInfo&state="+state+"&page=1";
		String res = server.sendCutomers(serverName, serverPort, orderNo, whichOne, isDropship, orderNo1); // result:1
		PrintWriter out = response.getWriter();
		out.println(res);
		out.flush();
		out.close();
	}

	// ??????????????????????????????
	public void upOrderDepot(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo = request.getParameter("orderNo");
		String actual_ffreight = request.getParameter("actual_ffreight");// ??????????????????
		// String custom_discuss_fright =
		// request.getParameter("custom_discuss_fright");//?????????????????????????????????
		String custom_discuss_other = request.getParameter("custom_discuss_other");// ????????????
		String transport_time = request.getParameter("transport_time");// ????????????
		String expect_arrive_time = request.getParameter("expect_arrive_time");// ??????????????????
		String actual_weight = request.getParameter("actual_weight");// ????????????
		String remaining_price = request.getParameter("remaining_price");// ??????????????????,????????????-?????????????????????
		String actual_allincost = request.getParameter("actual_allincost");// ???????????????
		String applicable_credit = request.getParameter("applicable_credit_n");// ????????????????????????
		String applicable_credit_q = request.getParameter("applicable_credit_q");// ?????????????????????????????????
		String order_ac = request.getParameter("order_ac");// ??????????????????????????????
		String service_fee = request.getParameter("service_fees");// ?????????
		String userId = request.getParameter("userId");
		String orderState = request.getParameter("orderState");
		String longc = request.getParameter("longc");
		String wight = request.getParameter("wight");
		String height = request.getParameter("height");
		String domestic_freight = request.getParameter("domestic_freight");
		String email = request.getParameter("email");
		String mode_transport = request.getParameter("mode_transport");
		String yx_freight = request.getParameter("yx_freight");// ?????????????????????
		String actual_freight_c = request.getParameter("actual_freight_c");// ????????????????????????
		String currency = request.getParameter("currency");
		String actual_lwh = "0";
		/*
		 * if(Utility.getIsDouble(longc) && Utility.getIsDouble(wight) &&
		 * Utility.getIsDouble(height)){ actual_lwh = new
		 * BigDecimal((Double.parseDouble(longc) * Double.parseDouble(wight) *
		 * Double.parseDouble(height))/1000000).setScale(2).toString(); }
		 */
		String actual_volume = longc + "*" + wight + "*" + height;
		Date date = null, expect_arrive_date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (transport_time != null && !transport_time.equals("")) {
			date = sdf.parse(transport_time);
		}
		if (expect_arrive_time != null && !expect_arrive_time.equals("")) {
			expect_arrive_date = sdf.parse(expect_arrive_time);
		}
		int order_state = -2;
		IOrderwsServer server = new OrderwsServer();
		if (orderState != null) {
			order_state = Integer.parseInt(orderState);
			String oldstate = request.getParameter("oldstate");
			// ????????????????????????????????????????????????????????????????????????????????????
			if (oldstate.equals("2") && orderState.equals("3")) {
				IMessageServer messageServer = new MessageServer();
				messageServer.addMessage(Integer.parseInt(userId), "Your order is shipped.", orderNo);
				server.sendShipment(orderNo, Integer.parseInt(userId), email, expect_arrive_time);
			}
		}
		/*
		 * if(order_state == 3){ String express_no =
		 * request.getParameter("express_no"); String logistics_name =
		 * request.getParameter("logistics_name"); String transport_details =
		 * request.getParameter("transport_details"); String new_state =
		 * request.getParameter("new_state"); if(express_no != null){ //
		 * Forwarder forwarder = new Forwarder();
		 * forwarder.setOrder_no(orderNo); forwarder.setExpress_no(express_no);
		 * forwarder.setLogistics_name(logistics_name);
		 * forwarder.setTransport_details(transport_details);
		 * forwarder.setNew_state(new_state); server.saveForwarder(forwarder); }
		 * }
		 */
		// ????????????????????????
		IUserDao userDao = new com.cbt.processes.dao.UserDao();
		double actual_freight_c_ = 0;
		if (Utility.getIsDouble(actual_freight_c)) {
			actual_freight_c_ = Double.parseDouble(actual_freight_c);
		}
		// ????????????????????????????????????????????????
		// ??????????????????????????????????????????
		float applicable_credit1 = Float.parseFloat(applicable_credit) - Float.parseFloat(applicable_credit_q);
		int res = server.upOrder(Integer.parseInt(userId), orderNo, actual_ffreight, custom_discuss_other, date,
				actual_weight, order_state, actual_volume, expect_arrive_date, actual_allincost, remaining_price,
				Double.parseDouble(order_ac), service_fee, domestic_freight, mode_transport, actual_freight_c_, 1,
				applicable_credit1);
		if (Utility.getIsDouble(yx_freight)) {
			server.addOrder_reductionfreight(orderNo, Double.parseDouble(yx_freight));
		}
		if (!applicable_credit.equals(applicable_credit_q)) {
			IUserServer userServer = new UserServer();
			userServer.upUserApplicableCredit(Integer.parseInt(userId),
					(float) (Double.parseDouble(applicable_credit)));
		}
		if (res < 0) {
			orderNo = orderNo + "&upstate=1";
		}
		response.sendRedirect("WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo=" + orderNo);
	}

	// ??????????????????????????????????????????????????? ???????????????????????????
	public void upOrderChange(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderNo = request.getParameter("orderNo");
		int goodId = Integer.parseInt(request.getParameter("goodId"));
		String oldInfo = request.getParameter("oldInfo");
		String newInfo = request.getParameter("newInfo");
		int changeType = Integer.parseInt(request.getParameter("changeType"));
		IOrderwsServer server = new OrderwsServer();
		String res = server.updateOrderChange(orderNo, goodId, oldInfo, newInfo, changeType);
		PrintWriter out = response.getWriter();
		out.println(res);
		out.flush();
		out.close();
	}

	// ???????????????
	public void upOrderChangeResolve(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		int goodId = Integer.parseInt(request.getParameter("goodId"));
		int changeType = Integer.parseInt(request.getParameter("changeType"));
		IOrderwsServer server = new OrderwsServer();
		int res = server.upOrderChangeResolve(orderNo, goodId, changeType);
		PrintWriter out = response.getWriter();
		out.println(res);
		out.flush();
		out.close();
	}

	// ????????????????????????
	public void saveForwarder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String express_no = request.getParameter("express_no");
		String logistics_name = request.getParameter("logistics_name");
		String transport_details = request.getParameter("transport_details");
		String new_state = request.getParameter("new_state");
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		PrintWriter out = response.getWriter();
		if (express_no != null) {
			Forwarder forwarder = new Forwarder();
			forwarder.setOrder_no(orderNo);
			forwarder.setExpress_no(express_no);
			forwarder.setLogistics_name(logistics_name);
			forwarder.setTransport_details(transport_details);
			forwarder.setNew_state(new_state);
			int forw = server.saveForwarder(forwarder);
			out.print(forw);
			out.flush();
			out.close();
		} else {
			out.print(0);
			out.flush();
			out.close();
		}
	}

	/**
	 * ????????????:??????????????????id?????? ?????????????????? author:lizhanjun date:2015???4???15???
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getOrderDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String str = request.getParameter("id");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		IOrderwsServer server = new OrderwsServer();
		PrintWriter out = response.getWriter();
		if (str != null && !"".equals(str)) {
			int id = Integer.parseInt(str);
			OrderDetailsBean odb = server.getById(id);
			// String json = "{\"actual_freight\":"+odb.getActual_freight()+",
			// \"actual_price\":"+odb.getActual_price()+",
			// \"actual_weight\":"+odb.getActual_weight()+"}";
			if (odb != null) {
				String[] s = odb.getActual_volume().split("\\*");
				Object[] arr = new Object[] { odb.getActual_freight(), odb.getActual_weight(), odb.getActual_price(),
						s[0], s[1], s[2] };
				out.print(JSONArray.toJSONString(arr));
				out.flush();
				out.close();
			} else {
				out.print(0);
				out.flush();
				out.close();
			}
		}
	}

	// ylm?????????????????????
	public void saveAdvanceorder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String freight = request.getParameter("freight");
		String tariffs = request.getParameter("tariffs");
		String answer = request.getParameter("answer");
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer orderwsServer = new OrderwsServer();
		int res = orderwsServer.upQuestions(orderNo, answer, freight, tariffs);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	/**
	 * ????????????:??????????????????????????????????????????????????? author:ylm date:2015???8???1???
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void closeOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		// jxw 2017-4-25 ???????????????
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		// ????????????????????????????????????????????????
		if (adm == null) {
			PrintWriter out = response.getWriter();
			out.print(0);
			out.flush();
			out.close();
			return;
		} else {

			String orderNo = request.getParameter("orderNo");
			String currency = request.getParameter("currency");
			String toEmail = request.getParameter("email");
			String confirmEmail = request.getParameter("confirmEmail");
			int isDropshipOrder = Integer.valueOf(request.getParameter("isDropshipOrder"));
			try {

				IOrderwsServer orderwsServer = new OrderwsServer();
				// ???????????????????????????drop??????
				boolean isDrop = orderwsServer.isDropshipOrder(orderNo) > 0;
				if (isDrop) {
					String mainOrderNo = orderwsServer.queryMainOrderByDropship(orderNo);
					if (mainOrderNo == null || "".equals(mainOrderNo)) {
						String remark = "?????????:" + orderNo + ",????????????dropship??????????????????,????????????";
						LOG.error(remark);
						// ??????????????????????????????
						InsertMessageNotification messageDao = new InsertMessageNotification();
						messageDao.orderChangeError(orderNo, adm.getId(), remark);
					} else {
						closeDropShipOrder(request, response, orderwsServer, adm.getId(), mainOrderNo, orderNo, toEmail,confirmEmail);
					}
				} else {
					closeGeneralOrder(request, response, orderwsServer, adm.getId(), orderNo, toEmail, confirmEmail);

				}

			} catch (Exception e) {
				LOG.error("closeOrder error, orderNo : " + orderNo + ",reason : " + e.getMessage());
				throw new Exception(e);
			}

		}
	}

	/**
	 * dropship????????????
	 *
	 * @param request
	 * @param response
	 * @param orderwsServer
	 * @param adminId
	 * @param orderNo
	 * @param toEmail
	 * @param confirmEmail
	 * @throws Exception
	 */
	private void closeDropShipOrder(HttpServletRequest request, HttpServletResponse response,
                                    IOrderwsServer orderwsServer, int adminId, String mainOrderNo, String orderNo, String toEmail,
                                    String confirmEmail) throws Exception {

		LOG.info("closeDropShipOrder start");

		// ???????????????????????????????????????????????????????????????????????????,????????????????????????????????????????????????
		if (mainOrderNo.equals(orderNo)) {
			int res = orderwsServer.iscloseOrder(orderNo);
			if (res > 0) {
				PrintWriter out = response.getWriter();
				out.print(2);
				out.flush();
				out.close();
				return;
			}
			LOG.info("?????????:" + orderNo + ",????????????orderinfo????????????:" + -1);

			// jxw 2017-4-25????????????????????????,?????????????????????-1(????????????),??????????????????adminId
			boolean isCheck = CheckCanUpdateUtil.updateOnlineOrderInfoByLocal(mainOrderNo, -1, adminId);
			if (isCheck) {

				// ??????orderinfo?????????
				res = orderwsServer.closeOrder(orderNo);
				if(res>0){
					//??????????????????????????????
					if(mainOrderNo.indexOf("_H")>-1) {
						owsService.reduceOrderStock(mainOrderNo, 0,  "??????orderno/odid?????????,????????????????????????orderStock");
					}else {
						inventoryService.cancelOrderToInventory(mainOrderNo, adminId, "");
					}
//					orderwsServer.cancelInventory(mainOrderNo);
				}
				// ??????dropshiporder?????????
				orderwsServer.closeDropshipOrderByMainOrderNo(mainOrderNo);
				int userId = Integer.parseInt(request.getParameter("userId"));

				// ????????????????????????
				// zlw add start
				float actualPay = Float.parseFloat(request.getParameter("actualPay"));
				float order_ac = Float.parseFloat(request.getParameter("order_ac"));
				UserDao dao = new UserDaoImpl();
				int res1 = 0;
				order_ac = new BigDecimal(order_ac).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
				res1 = dao.updateUserAvailable(userId,
						new BigDecimal(actualPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
						" system closeOrder:" + orderNo, orderNo, String.valueOf(adminId), 0, order_ac, 1);
				// zlw add end

				// ssd add start
				// ?????????????????????????????????
				StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
				sbBuffer.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='"
						+ AppConfig.ip_email + "/img/logo.png' ></img></a>");
				sbBuffer.append(
						"<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear " + toEmail + "</div>");
				sbBuffer.append("<br><br>Order#: " + orderNo);
				sbBuffer.append(
						"<br><br>We apologize, but despite our efforts, we weren???t able to fulfill some or all of the items in your order.");
				sbBuffer.append(
						"<br>We apologize for any inconvenience this has caused and look forward to your next visit to ");
				sbBuffer.append("<a href='" + AppConfig.server_path + "'>www.importx.com</a>.");
				sbBuffer.append("<br>Thank you for shopping with us.");
				sbBuffer.append("<br>To review your order status, click ");
				sbBuffer.append("<a href='" + AppConfig.center_path + "'>" + AppConfig.center_path + "</a>.");
				sbBuffer.append("<br><br>Sincerely,");
				sbBuffer.append("<br>Import-Express Team");

				SendEmail.send(confirmEmail, null, toEmail, sbBuffer.toString(),
						"Your ImportExpress Order " + orderNo + " transaction is closed!", "", orderNo, 2);
				// ssd and end
				PrintWriter out = response.getWriter();
				out.print(res);
				out.print(res1);
				out.flush();
				out.close();

				// jxw 2017-4-25 ???????????????????????????????????????????????????
				insertChangeRecords(orderNo, -1, adminId);
			}
			LOG.info("closeGeneralOrder end");
		} else {
			// ???????????????,
			// ????????????????????????????????????
			int isMainCancel = orderwsServer.isCloseByDropshipMainOrder(mainOrderNo);
			if (isMainCancel > 0) {
				InsertMessageNotification messageDao = new InsertMessageNotification();
				String remark = "dropship????????????:" + mainOrderNo + ",????????????????????????????????????,???????????????,????????????";
				LOG.error(remark);
				// ??????????????????????????????
				messageDao.orderChangeError(orderNo, adminId, remark);
				// ????????????????????????
				String sqlStr = "dropship MainOrderNo:" + mainOrderNo + ",operationType:"
						+ OrderInfoConstantUtil.OFFLINECANCEL;
				ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, adminId, 2, remark);
			} else {

				// ???????????????????????????????????????
				int res = orderwsServer.isCloseDropshipOrder(orderNo);
				if (res > 0) {
					LOG.info("????????????:" + orderNo + ",??????dropship??????????????????");
					PrintWriter out = response.getWriter();
					out.print(2);
					out.flush();
					out.close();
					return;
				} else {
					// ???????????????????????????,?????????????????????,
					LOG.info("?????????:" + orderNo + ",????????????dropship???????????????: -1");

					float totalPrice = Float.parseFloat(request.getParameter("totalPrice"));
					float freight = Float.parseFloat(request.getParameter("freight"));
					float weight = Float.parseFloat(request.getParameter("weight"));
					int userId = Integer.parseInt(request.getParameter("userId"));
					res = orderwsServer.closeDropshipOrder(userId,mainOrderNo, orderNo, totalPrice, 0,freight, weight);
					// ???????????????,?????????????????????,
					if (res > 0) {

						// ????????????????????????
						// zlw add start
						float actualPay = Float.parseFloat(request.getParameter("actualPay"));
						float order_ac = Float.parseFloat(request.getParameter("order_ac"));
						UserDao dao = new UserDaoImpl();
						int res1 = 0;
						order_ac = new BigDecimal(order_ac).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
						res1 = dao.updateUserAvailable(userId,
								new BigDecimal(actualPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
								" system closeOrder:" + orderNo,orderNo, String.valueOf(adminId), 0, order_ac, 1);
						// zlw add end

						// ??????SQL??????????????????dropship????????????????????????,????????????,?????????orderInfo??????????????????
						int mainOrderUpd = orderwsServer.updateMainOrderByDropship(userId,mainOrderNo, orderNo);
						if (mainOrderUpd <= 0 && mainOrderUpd != -2) {// -2??????????????????
							String remark = "dropship?????????:" + orderNo + ",????????????????????????["
									+ OrderInfoConstantUtil.OFFLINECANCEL + "]??????";
							LOG.error(remark);
							// ??????????????????????????????
							InsertMessageNotification messageDao = new InsertMessageNotification();
							messageDao.orderChangeError(orderNo, adminId, remark);
							// ????????????????????????
							String sqlStr = "dropshipOrderNo:" + orderNo + ",operationType:"
									+ OrderInfoConstantUtil.OFFLINECANCEL;
							ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, adminId, 2, remark);
						}

						// ssd add start
						// ?????????????????????????????????
						StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
						sbBuffer.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='"
								+ AppConfig.ip_email + "/img/logo.png' ></img></a>");
						sbBuffer.append(
								"<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear " + toEmail + "</div>");
						sbBuffer.append("<br><br>Order#: " + orderNo);
						sbBuffer.append(
								"<br><br>We apologize, but despite our efforts, we weren???t able to fulfill some or all of the items in your order.");
						sbBuffer.append(
								"<br>We apologize for any inconvenience this has caused and look forward to your next visit to ");
						sbBuffer.append("<a href='" + AppConfig.server_path + "'>www.importx.com</a>.");
						sbBuffer.append("<br>Thank you for shopping with us.");
						sbBuffer.append("<br>To review your order status, click ");
						sbBuffer.append("<a href='" + AppConfig.center_path + "'>" + AppConfig.center_path + "</a>.");
						sbBuffer.append("<br><br>Sincerely,");
						sbBuffer.append("<br>Import-Express Team");

						SendEmail.send(confirmEmail, null, toEmail, sbBuffer.toString(),
								"Your ImportExpress Order " + orderNo + " transaction is closed!", "", orderNo, 2);
						// ssd and end
						PrintWriter out = response.getWriter();
						out.print(res);
						out.print(res1);
						out.flush();
						out.close();
					} else {
						String remark = "?????????:" + orderNo + ",??????dropship??????????????????,????????????:"
								+ OrderInfoConstantUtil.OFFLINECANCEL;
						LOG.error(remark);
						InsertMessageNotification messageDao = new InsertMessageNotification();
						// ??????????????????????????????
						messageDao.orderChangeError(orderNo, adminId, remark);
						// ????????????????????????
						String sqlStr = "dropshipOrderNo:" + orderNo + ",operationType:"
								+ OrderInfoConstantUtil.OFFLINECANCEL;
						ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, adminId, 2, remark);
					}
				}

			}
			LOG.info("closeDropShipOrder end");
		}

	}

	/**
	 * ??????????????????
	 *
	 * @param request
	 * @param response
	 * @param orderwsServer
	 * @param adminId
	 * @param orderNo
	 * @param toEmail
	 * @param confirmEmail
	 * @throws Exception
	 */
	private void closeGeneralOrder(HttpServletRequest request, HttpServletResponse response,
                                   IOrderwsServer orderwsServer, int adminId, String orderNo, String toEmail, String confirmEmail)
			throws Exception {

		LOG.info("closeGeneralOrder start,orderNo : " + orderNo);

		// jxw 2017-4-25????????????????????????,?????????????????????-1(????????????),??????????????????adminId
		boolean isCheck = CheckCanUpdateUtil.updateOnlineOrderInfoByLocal(orderNo, -1, adminId);
		if (isCheck) {

			int res = orderwsServer.iscloseOrder(orderNo);
			if (res > 0) {
				PrintWriter out = response.getWriter();
				out.print(2);
				out.flush();
				out.close();
				return;
			}
			LOG.info("?????????:" + orderNo + ",????????????orderinfo????????????:" + -1);
			res = orderwsServer.closeOrder(orderNo);
			if(res>0){
				//??????????????????????????????
				if(orderNo.indexOf("_H")>-1) {
					owsService.reduceOrderStock(orderNo, 0,  "??????orderno/odid?????????,????????????????????????orderStock");
				}else {
					inventoryService.cancelOrderToInventory(orderNo, adminId, "");
				}
//				orderwsServer.cancelInventory(orderNo);
			}
			int userId = Integer.parseInt(request.getParameter("userId"));

			// ????????????????????????
			// zlw add start
			float actualPay = Float.parseFloat(request.getParameter("actualPay"));
			float order_ac = Float.parseFloat(request.getParameter("order_ac"));
			int isDropshipOrder = Integer.valueOf(request.getParameter("isDropshipOrder"));
			if (isDropshipOrder == 2) {
				actualPay = 0;
				order_ac = 0;
			}
			UserDao dao = new UserDaoImpl();
			int res1 = 0;
			order_ac = new BigDecimal(order_ac).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			res1 = dao.updateUserAvailable(userId,
					new BigDecimal(actualPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
					" system closeOrder:" + orderNo, orderNo,String.valueOf(adminId), 0, order_ac, 1);
			// zlw add end

			// ssd add start
			// ?????????????????????????????????
			StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
			sbBuffer.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='"
					+ AppConfig.ip_email + "/img/logo.png' ></img></a>");
			sbBuffer.append(
					"<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear " + toEmail + "</div>");
			sbBuffer.append("<br><br>Order#: " + orderNo);
			sbBuffer.append(
					"<br><br>We apologize, but despite our efforts, we weren???t able to fulfill some or all of the items in your order.");
			sbBuffer.append(
					"<br>We apologize for any inconvenience this has caused and look forward to your next visit to ");
			sbBuffer.append("<a href='" + AppConfig.server_path + "'>www.importx.com</a>.");
			sbBuffer.append("<br>Thank you for shopping with us.");
			sbBuffer.append("<br>To review your order status, click ");
			sbBuffer.append("<a href='" + AppConfig.center_path + "'>" + AppConfig.center_path + "</a>.");
			sbBuffer.append("<br><br>Sincerely,");
			sbBuffer.append("<br>Import-Express Team");

			SendEmail.send(confirmEmail, null, toEmail, sbBuffer.toString(),
					"Your ImportExpress Order " + orderNo + " transaction is closed!", "", orderNo, 2);
			// ssd and end
			PrintWriter out = response.getWriter();
			out.print(res);
			out.print(res1);
			out.flush();
			out.close();
			// jxw 2017-4-25 ???????????????????????????????????????????????????
			try {
				insertChangeRecords(orderNo, -1, adminId);
			} catch (Exception e) {
				LOG.error("closeGeneralOrder ?????????");
			}
			LOG.info("closeGeneralOrder end");
		}

	}

	public void updateGoods(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.info("updateGoods start");
		response.setHeader("Access-Control-Allow-Origin", "*");
		IOrderwsServer orderwsServer = new OrderwsServer();
		String type = request.getParameter("type");
		String gid = request.getParameter("gid");
		String value = request.getParameter("value").trim();
		int res = orderwsServer.updateGoods(Integer.parseInt(type), Integer.parseInt(gid), value);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
		LOG.info("updateGoods end");
	}

	/**
	 * ????????????:???????????????????????? author:ylm date:2015???8???11???
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getGoodpostage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userid = Integer.parseInt(request.getParameter("userid"));
		int page = Integer.parseInt(request.getParameter("page"));
		IOrderwsServer orderwsServer = new OrderwsServer();
		List<Object[]> res = orderwsServer.getGoodpostage(userid, page);
		int count = orderwsServer.getGoodpostageNumber(userid);
		Object[] objects = { count, page };
		res.add(objects);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(res));
		out.flush();
		out.close();
	}

	// ????????????????????????
	public void upOrderPurchase(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int orderdid = Integer.parseInt(request.getParameter("orderdid"));
		String orderid = request.getParameter("orderid");
		String purchase_confirmation = request.getParameter("purchase_confirmation");
		IOrderwsServer orderwsServer = new OrderwsServer();
		int res = orderwsServer.upOrderPurchase(orderdid, orderid, purchase_confirmation);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	// ????????????????????????
	public void cancelOrderPurchase(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int orderdid = Integer.parseInt(request.getParameter("orderdid"));
		String orderid = request.getParameter("orderid");
		IOrderwsServer orderwsServer = new OrderwsServer();
		int res = orderwsServer.cancelOrderPurchase(orderdid, orderid);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	public void purchasepriceOfOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IWebsiteOrderDetailDao dao = new WebsiteOrderDetailDaoImpl();
		String orderid = request.getParameter("orderid");
		float res = dao.PurchaseAmountOfOrder(orderid);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	/**
	 * ????????????
	 *
	 * @date 2016???7???25???
	 * @author abc
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void freightDeduction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int result = 1;
		// ???????????????
		String refreight = request.getParameter("refreight");
		if (StrUtils.isNullOrEmpty(refreight) || !StrUtils.isMatch(refreight, "(\\d+\\.*\\d*)")) {
			result = 0;
		}
		// ?????????
		String orderNo = request.getParameter("orderNo");
		if (StrUtils.isNullOrEmpty(orderNo)) {
			result = 0;
		}
		// ??????id
		String userId = request.getParameter("userId");
		if (StrUtils.isNullOrEmpty(userId) || !StrUtils.isMatch(userId, "(\\d+\\.*\\d*)")) {
			result = 0;
		}
		if (result == 1) {
			int i_userId = Integer.parseInt(userId);
			// ?????????????????????
			UserServer user = new UserServer();
			double userApplicableCredit = user.getUserApplicableCredit(i_userId);

			userApplicableCredit = userApplicableCredit - Double.valueOf(refreight);

			userApplicableCredit = userApplicableCredit < 0.001 ? 0 : userApplicableCredit;

			OrderwsServer order = new OrderwsServer();
			OrderBean orderBean = order.getOrder_remainingPrice(orderNo);
			if (orderBean == null) {
				result = 0;
			} else {
				double order_ac = orderBean.getOrder_ac();
				double remaining_price = orderBean.getRemaining_price();
				double drefreight = Double.valueOf(refreight);
				order_ac = order_ac + drefreight;
				remaining_price = remaining_price - drefreight;
				remaining_price = remaining_price < 0.001 ? 0 : remaining_price;

				int credit = user.upUserApplicableCredit(i_userId, userApplicableCredit);
				if (credit > 0) {
					// ???????????????????????????????????? order_ac(+) remaining_price(-)
					int upOrder_remainingPrice = order.upOrder_remainingPrice(orderNo, remaining_price, order_ac);
					if (upOrder_remainingPrice == 0) {
						// ??????????????????
						result = -1;
					} else {
						result = 1;
					}
				} else {
					result = -2;
				}
			}
		}
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}

	public synchronized Map<String, String> inio(HttpServletRequest request, HttpServletResponse response,
                                                 String filePath) {
		isMultipart = ServletFileUpload.isMultipartContent(request);
		Map<String, String> map = new HashMap<String, String>();
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("GBK");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			factory.setRepository(new File(filePath));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);
			List<FileItem> fileItems = upload.parseRequest(request);
			// ???????????????????????????????????? ?????????,??????
			String fileName = "";
			StringBuffer sb = new StringBuffer();
			for (FileItem fi : fileItems) {
				// ??????????????? ???????????????????????? ?????? ??????
				if (fi.isFormField()) {
					// ???????????????????????????
					title = fi.getFieldName();
					if (title.equals("orderNo")) {
						map.put("orderNo", fi.getString());
					} else if (title.equals("orderDetailID")) {
						map.put("orderDetailID", fi.getString());
					} else if (title.equals("orderstate")) {
						map.put("orderstate", fi.getString());
					} else if (title.equals("actual_price")) {
						map.put("actual_price", fi.getString());
					} else if (title.equals("actual_freight")) {
						map.put("actual_freight", fi.getString());
					} else if (title.equals("actual_weight")) {
						map.put("actual_weight", fi.getString());
					} else if (title.equals("pay_price")) {
						map.put("pay_price", fi.getString());
					} else if (title.equals("service_fee")) {
						map.put("service_fee", fi.getString());
					} else if (title.equals("longc")) {
						map.put("longc", fi.getString());
					} else if (title.equals("wight")) {
						map.put("wight", fi.getString());
					} else if (title.equals("hight")) {
						map.put("hight", fi.getString());
					}
				}

				// ??????????????? ?????????????????????????????? ???????????????????????? ?????????????????????
				else {
					/**
					 * ??????????????????????????? ?????????????????????
					 */
					// ???????????????
					String value = fi.getName();
					// ??????????????????????????????
					int start = value.lastIndexOf("\\");
					String fn = value.substring(start + 1);
					// ?????? ??????????????? ?????????????????????1??? ??????????????????
					sb.append(fn).append(",");
					fileName = sb.toString();
					// ??????????????????
					OutputStream out = new FileOutputStream(new File(filePath, fn));

					InputStream in = fi.getInputStream();

					int length = 0;
					byte[] buf = new byte[1024];

					// in.read(buf) ?????????????????????????????? buf ?????????
					while ((length = in.read(buf)) != -1) {
						// ??? buf ????????? ???????????? ?????? ????????????????????????
						out.write(buf, 0, length);

					}

					in.close();
					out.close();
				}
			}
			map.put("fileupload", fileName);

		} catch (Exception ex) {

			LOG.warn("doPost():" + ex);
		}
		return map;

	}

	private boolean isMultipart;

	private String title;

	private int maxFileSize = 25000 * 10240;

	private int maxMemSize = 25000 * 10240;

	private File file;

	private String iso;

	// public Map<String, Object> paymentConfirm(HttpServletRequest
	// request,HttpServletResponse response) throws ServletException,
	// IOException {
	// String userId = request.getParameter("userId");
	// String orderNo = request.getParameter("orderNo");
	// String pass = request.getParameter("pass");
	// String paytypeid = request.getParameter("paytypeid");
	//
	// PaymentConfirmServer paymentConfirmServer = new
	// PaymentConfirmServerImpl();
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	//// Admuser adm = (Admuser) request.getSession().getAttribute("admuser");
	//
	// String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
	// Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
	// Admuser.class);
	//
	// if (!pass.equals(adm.getPassword())) {
	// map.put("code", "1");
	// map.put("msg", "????????????");
	// return map;
	// }
	// String confirmtime = Utility.format(new Date(), Utility.datePattern1);
	// String paytype = "";
	// if ("pay_0".equals(paytypeid)) {
	// paytype = "0";
	// } else if ("pay_1".equals(paytypeid)) {
	// paytype = "1";
	// } else if ("pay_2".equals(paytypeid)) {
	// paytype = "2";
	// }
	// int result = 0;
	// try {
	// result = paymentConfirmServer.confirmOrder(orderNo,adm.getAdmName(),
	// confirmtime, paytype, null, null,userId);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if (result == 1) {
	// map.put("code", "0");
	// map.put("msg", "Successful operation");
	// map.put("confirmname", adm.getAdmName());
	// map.put("confirmtime", confirmtime);
	// } else {
	// map.put("code", "1");
	// map.put("msg", "Operation failure");
	// }
	// return map;
	// }

	// 8.1 ?????? ?????????????????????
	public void getRepeatUserid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int uid = Integer.parseInt(request.getParameter("userid"));
		IOrderwsServer server = new OrderwsServer();
		List<Integer> list_uid = server.getRepeatUserid(uid);
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(JSONArray.toJSONString(list_uid));
		out.close();
	}

	public void changeBuyer(HttpServletRequest request, HttpServletResponse response) {
		try {
			String odid = request.getParameter("odid").toString();
			String admuserid = request.getParameter("admuserid").toString();
			IOrderwsServer server = new OrderwsServer();
			int row = server.changeBuyer(Integer.valueOf(odid), Integer.valueOf(admuserid));
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			if (row > 0) {
				out.write("success");
			} else {
				out.write("fail");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changeOrderBuyer(HttpServletRequest request, HttpServletResponse response) {
		try {
			String orderid = request.getParameter("orderid").toString();
			String admuserid = request.getParameter("admuserid");
			IOrderwsServer server = new OrderwsServer();
			int row = server.changeOrderBuyer(orderid, Integer.valueOf(admuserid),"0");
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			if (row > 0) {
				out.write("success");
			} else {
				out.write("fail");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ???????????????????????????????????? 2016.8.30 lyb
	 */
	public void updateVolumeOrWeight(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String flag = request.getParameter("flag"); // ???????????? 0?????????????????????1??????????????????
		int id = Integer.parseInt(request.getParameter("id")); // ?????????id
		String vw = request.getParameter("vw"); // ???????????????
		String seilUnit = request.getParameter("seilUnit"); // ??????????????????
		String goodsUnit = request.getParameter("goodsUnit"); // ??????????????????
		int number = Integer.parseInt(request.getParameter("number")); // ?????????????????????
		String total = "";
		IGoodsServer server = new GoodsServerImpl();
		if (flag.equals("0")) {
			total = ParseGoodsUrl.calculateVolume(number, vw, seilUnit, goodsUnit); // ?????????
		} else {
			total = ParseGoodsUrl.calculateWeight(number, vw, seilUnit, goodsUnit); // ?????????
		}
		int i = server.updateVolumeOrWeight(flag, id, vw, total);
		PrintWriter out = response.getWriter();
		out.write(i + "");
		out.close();
	}

	// ???????????????????????? lyb
	public void getCustCountry(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		List<Map<String, String>> list = server.getCustCountry(orderNo);
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(JSONArray.toJSONString(list));
		out.close();
	}

	/**
	 * ??????oid????????????????????? lyb
	 */
	public void getBuyerByOrderNo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderno = request.getParameter("str_oid"); // order_detail???id
		// ??????????????????
		IOrderwsServer server = new OrderwsServer();
		List<Map<String, String>> list = server.getBuyerByOrderNo(orderno);
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(JSONArray.toJSONString(list));
		out.close();
	}

	public void queryprocurement(HttpServletRequest request, HttpServletResponse response) throws IOException {
		IOrderwsServer server = new OrderwsServer();
		List<com.cbt.pojo.Admuser> list = server.getAllBuyer();
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(JSONArray.toJSONString(list));
		LOG.info("====================" + list.size());
		out.close();
	}

	// ?????????????????????????????????????????????
	public void queryPyaprice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer server = new OrderwsServer();
		// ????????????
		OrderBean order = server.getOrders(orderNo);

		request.setAttribute("orderNo", orderNo);
		request.setAttribute("order", order);
		request.setAttribute("service_fee",
				Utility.getIsDouble(order.getService_fee()) ? Double.parseDouble(order.getService_fee()) : 0);
		request.setAttribute("cashback", order.getCashback());
		request.setAttribute("product_cost", order.getProduct_cost());// ???????????????
		request.setAttribute("discount_amount", order.getDiscount_amount());// ???????????????
		request.setAttribute("extra_freight", order.getExtra_freight());// ????????????
		request.setAttribute("extra_discount", order.getExtra_discount());// ??????????????????
		request.setAttribute("share_discount", order.getShare_discount());// ????????????
		double price1 = Double.valueOf(order.getProduct_cost()) - order.getCashback() - order.getDiscount_amount()
				- order.getExtra_discount() - order.getShare_discount() + Double.valueOf(order.getService_fee())
				+ order.getExtra_freight();
		BigDecimal p = new BigDecimal(price1);
		double price = p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		request.setAttribute("price", price);// ???????????????
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/updateorderstate.jsp");
		homeDispatcher.forward(request, response);
		return;
		//
	}

	public void seehistoryPrice(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String url = request.getParameter("url");
		System.out.println(url);
		GoodsPriceHistoryserviceImpl historyservice = new GoodsPriceHistoryserviceImpl();
		List<Object[]> list = historyservice.seehistoryPrice(url);
		System.out.println(list);
		if (!list.isEmpty() && list.size() > 0) {
			resMap.put("statu", true);
			resMap.put("data", list);
		} else {
			resMap.put("statu", false);
			resMap.put("message", "??????????????????!");
		}
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(JSONObject.toJSONString(resMap));
		out.flush();
		out.close();

	}

	private void insertChangeRecords(String orderNo, int operationType, int adminId) {

		Orderinfo orderinfo = new Orderinfo();
		try {

			// ???????????????????????????????????????????????????
			OnlineOrderInfoDao infoDao = new OnlineOrderInfoDao();
			orderinfo = infoDao.queryOrderInfoByOrderNo(orderNo);
			if (orderinfo != null) {
				ChangeRecordsDao cRecordsDao = new ChangeRecordsDao();
				cRecordsDao.insertOrderChange(orderinfo, adminId, operationType);
			} else {
				LOG.error("??????[" + orderNo + "]?????????????????????????????????????????????");
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (orderinfo != null) {
				LOG.error("??????[" + orderinfo.getOrderNo() + "]?????????????????????????????????" + operationType);
			} else {
				LOG.error("??????[" + orderNo + "]?????????????????????????????????????????????");
			}
		}
	}

}
