package com.cbt.website.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.CodeMaster;
import com.cbt.bean.GoodsCheckBean;
import com.cbt.bean.OrderDatailsNew;
import com.cbt.bean.OrderProductSource;
import com.cbt.common.StringUtils;
import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;
import com.cbt.jcys.bean.DataInfo;
import com.cbt.jcys.bean.PriceData;
import com.cbt.jcys.bean.PriceReturnJsonNew;
import com.cbt.jcys.util.JcgjSoapHttpPost;
import com.cbt.jdbc.DBHelper;
import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.processes.servlet.Currency;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.PreferentialPrice;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.*;
import com.cbt.website.dao2.*;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("all")
public class Purchase extends HttpServlet {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(Purchase.class);
	PurchaseServer purchaseServer = new PurchaseServerImpl();
	private final static org.slf4j.Logger SLOG = LoggerFactory.getLogger("source");
	@Autowired
	private IWarehouseService iWarehouseService;
	
	// ????????????????????????
		public void useInventory(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			Map<String, String> map = new HashMap<String, String>();
			int row=0;
			String od_id=request.getParameter("od_id");
			String isUse=request.getParameter("isUse");
			map.put("od_id", od_id);
			map.put("isUse", isUse);
			row = purchaseServer.useInventory(map);
			PrintWriter out = response.getWriter();
			out.print(row + "");
			out.close();
		}
		/**
		 * ????????????????????????
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
		public void OfflinePaymentApplication(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			Map<String, String> map = new HashMap<String, String>();
			String sessionId = request.getSession().getId();
			String admuserw = Redis.hget(sessionId, "admuser");
			SerializeUtil su = new SerializeUtil();
			Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
			int row=0;
			String list=request.getParameter("list");
			String off_remark=request.getParameter("off_remark");
			map.put("list", list);
			map.put("admName", admuser.getAdmName());
			map.put("off_remark", off_remark);
			row = purchaseServer.OfflinePaymentApplication(map);
			PrintWriter out = response.getWriter();
			out.print(row + "");
			out.close();
		}

	// ???????????????????????????
	public void insertSources(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		int adminid = admuser.getId();
		String shipno = request.getParameter("shipno");
		String taobaoPrice = request.getParameter("taobaoPrice");
		if (taobaoPrice == null || taobaoPrice.equals("")) {
			taobaoPrice = "0";
		}
		String taobaoFeight = request.getParameter("taobaoFeight");
		if (taobaoFeight == null || taobaoFeight.equals("")) {
			taobaoFeight = "0";
		}
		String delivary_date = request.getParameter("delivary_date");
		String goodsQty = request.getParameter("goodsQty");
		if (goodsQty == null || goodsQty.equals("")) {
			goodsQty = "0";
		}
		String taobao_url = request.getParameter("taobao_url");
		String goods_sku = request.getParameter("goods_sku");
		String taobao_name = request.getParameter("taobao_name");
		String preferential = request.getParameter("preferential");
		String paydate = request.getParameter("paydate");
		String goods_imgs = request.getParameter("goods_imgs");
		String admName =request.getParameter("admName");
		String TbOrderid = request.getParameter("TbOrderid");
		String TbGoodsid = request.getParameter("TbGoodsid");
//		admName = purchaseServer.getUserName(admName);
		map.put("username", admName);
		map.put("shipno", shipno);
		map.put("taobaoPrice", taobaoPrice);
		map.put("taobaoFeight", taobaoFeight);
		map.put("goodsQty", goodsQty);
		map.put("taobao_url", taobao_url);
		map.put("goods_sku", goods_sku);
		map.put("taobao_name", taobao_name);
		map.put("preferential", preferential);
		map.put("paydate", paydate);
		map.put("goods_imgs", goods_imgs);
		map.put("delivary_date", delivary_date);
		map.put("TbOrderid", TbOrderid);
		map.put("TbGoodsid", TbGoodsid);
		map.put("adminid", String.valueOf(adminid));
		double prices = Integer.parseInt(goodsQty) * Double.parseDouble(taobaoPrice) + Double.parseDouble(taobaoFeight);
		map.put("totalprice", String.valueOf(prices));
		int row = purchaseServer.insertSources(map);
		PrintWriter out = response.getWriter();
		out.print(row + "");
		out.close();
	}
	
	/**
	 * ??????????????????????????????
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void saveRepalyContent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		InsertMessageNotification msgNtDao = new InsertMessageNotification();
		AdmUserDao admDao = new AdmUserDaoImpl();
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserw, Admuser.class);
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time=sdf.format(date);
		String orderid=request.getParameter("orderid");
		String odid=request.getParameter("odid");
		String goodsid=request.getParameter("goodsid");
		String text=request.getParameter("text");
		String type=request.getParameter("type");
		if(!StringUtils.isEmpty(text)){
			text="????????????:"+admuser.getAdmName()+",??????:"+time+",??????:"+text;
		}else{
			text="??????????????????";
		}
		map.put("orderid", orderid);
		map.put("odid", odid);
		map.put("goodsid", goodsid);
		map.put("text", text);
		map.put("type", type);
		String context = purchaseServer.saveRepalyContent(map);
		PrintWriter out = response.getWriter();
			try {
				if(!StringUtils.isEmpty(text) && "1".equals(type)){
					List<Admuser> admusers = admDao.queryByOrderNo(orderid);
					if(admusers.size()>0){
						//??????????????????????????????
						 msgNtDao.insertMessageInsertByType(orderid,text,admusers.get(0).getId(),1,admuser.getId(),0);
					}
				}else if(!StringUtils.isEmpty(text) && "2".equals(type)){
					//?????????????????????
					int admuserid = admDao.queryByBuyerOrderNo(orderid,odid);
					msgNtDao.insertMessageInsertByType(orderid,text,admuserid,1,admuser.getId(),0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		out.print(context);
		out.close();
	}

	/**
	 * ????????????:??????ali????????????
	 * author:zlw
	 */
	public void winSearch(HttpServletRequest request, HttpServletResponse response) {
		
		//goodsPid
		String goodsPid = request.getParameter("goodsPid");
		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		
//		//?????????????????????
		List<GoodsCheckBean> goodsCheckBeans = ips.findWinPic(goodsPid);
//		
		request.setAttribute("gbbs", goodsCheckBeans);
		try {
			request.getRequestDispatcher("/website/winImg.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// ????????????
	public void getExchangeRate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String currency = request.getParameter("currency");
		Map<String, Double> maphl = Currency.getMaphl(request);
		double excrate = maphl.get(currency) / maphl.get("RMB");
		PrintWriter out = response.getWriter();
		out.print(excrate);
		out.close();
	}

	// ?????????rmb
	public void getUsatoRmb(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		double exchange_rate = 1;
		DecimalFormat df = new DecimalFormat("#0.##");

		Map<String, Double> maphl = Currency.getMaphl(request);

		// eur CAD:0.00 GBP:0.00 AUD:0.00
		exchange_rate = maphl.get("RMB");
		exchange_rate = exchange_rate / maphl.get("USD");
		double eur = exchange_rate;

		PrintWriter out = response.getWriter();
		out.print(eur);
		out.close();
	}

	// ??????????????????
	public void getStockOrderInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sessionId = request.getSession().getId();
		String sql = "select distinct od.goodsid,od.orderid from order_details od inner join lock_inventory i on od.id=i.od_id where i.od_id<>0 and i.is_delete=0 and od.state=0 and i.is_use=1";
		String admuserw = "";
		String orderid_no_array = request.getParameter("orderid_no_array");
		String goodsid = request.getParameter("goodsid");
		admuserw = Redis.hget(sessionId, "admuser");
		if (admuserw == null || "".equals(admuserw)) {
			request.getRequestDispatcher("website/main_login.jsp").forward(request, response);
			return;
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		String pagenum = request.getParameter("pagenum");
		if (pagenum == null)
			pagenum = "1";
		String orderid = request.getParameter("orderid");
		if (orderid == null)
			orderid = "";
		String admid = request.getParameter("admsuerid");
		if (admid == null)
			admid = admuser.getId().toString();
		if (admid == null)
			admid = "";
		String userid = request.getParameter("userid");
		if (userid == null)
			userid = "";
		String orderno = request.getParameter("orderno");
		if (orderno == null || "".equals(orderno)) {
			orderno = "";
		} else {
			sql += " and od.orderid='" + orderno + "'";
		}
		String goodid = request.getParameter("goodid");
		if (goodid == null || "".equals(goodid)) {
			goodid = "";
		} else {
			sql += " and od.goodsid='" + goodid + "' ";
		}
		String state = request.getParameter("state");
		if (state != null && !"-1".equals(state)) {
			sql += " and i.flag=" + state + "";
		}
		request.setAttribute("state", state);
		String goodname = request.getParameter("goodname");
		if (goodname == null)
			goodname = "";
		String date = request.getParameter("date");// ????????????
		if (date == null)
			date = "";
		String days = request.getParameter("days");
		if (days == null)
			days = "";
		String pagesize = request.getParameter("pagesize");
		if (pagesize == null)
			pagesize = 50 + "";
		String unpaid = request.getParameter("unpaid");
		int unpay = 0;
		if (unpaid != null)
			unpay = Integer.parseInt(unpaid);
		List<PurchasesBean> pblist = new ArrayList<PurchasesBean>();
		sql+=" order by i.id desc";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				List<PurchasesBean> list = purchaseServer.getStockOrderInfo(pagenum, orderid, null, userid,
						rs.getString("orderid"), rs.getString("goodsid"), date, days, null, unpay,
						Integer.parseInt(pagesize), orderid_no_array, goodsid, goodname);
				LOG.info("list.size=" + list.size());
				for (int i = 0; i < list.size(); i++) {
					PurchasesBean p = list.get(i);
					sql = "SELECT ifnull(i.remark,'??????') as remark,li.lock_remaining,i.remaining,i.new_remaining,i.barcode,li.flag FROM lock_inventory li INNER JOIN inventory i ON li.in_id=i.id INNER JOIN order_details od ON li.od_id=od.id  where od.goodsid='"
							+ p.getGoodsid() + "' AND od.orderid='" + p.getOrderNo() + "'";
					stmt = conn.prepareStatement(sql);
					rs1 = stmt.executeQuery();
					if (rs1.next()) {
						System.out.println("??????ID=" + p.getConfirm_userid());
						int admuserid = purchaseServer.getBuyId(p.getOrderNo(), String.valueOf(p.getGoodsid()));
						String name = purchaseServer.getUserbyID(String.valueOf(admuserid));
						p.setAdmin(name);
						p.setAllRemaining(rs1.getInt("new_remaining") > 0 ? rs1.getInt("new_remaining")
								: rs1.getInt("remaining"));
						p.setRemaining(rs1.getString("lock_remaining"));// ???????????????
						p.setFlag(rs1.getInt("flag"));
						p.setBarcode(rs1.getString("barcode"));// ?????????????????????
						p.setRemark(rs1.getString("remark"));
						pblist.add(p);
					}
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn);
		}

		LOG.info("??????????????????????????????:" + pblist.size());
		request.setAttribute("pblist", pblist);
		request.setAttribute("allCount", pblist.size());
		request.setAttribute("pagenum", pagenum);
		int pageSize = pblist.size() / Integer.valueOf(pagesize);
		if (pblist.size() % Integer.valueOf(pagesize) > 0) {
			pageSize = pageSize + 1;
		}
		request.setAttribute("pageSize", pageSize);
		request.getRequestDispatcher("website/stock_order_info.jsp").forward(request, response);
	}

	// ???????????? ????????????
	public void getPurchaseByXXX(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		long start = System.currentTimeMillis();
		System.out.println("===========================getPurchaseByXXX:" + sdf1.format(new Date()) + "=====????????? ============");
		String sessionId = request.getSession().getId();
		String admuserw = "";
		String orderid_no_array = request.getParameter("orderid_no_array");
		String idtypes_=request.getParameter("idtypes_");
		String goodsid = request.getParameter("goodsid");
		String pagenum = request.getParameter("pagenum");
		String orderid = request.getParameter("orderid");
		String userid = request.getParameter("userid");
		String admid = request.getParameter("admid");
		String orderno = request.getParameter("orderno");
		String goodid = request.getParameter("goodid");
		String goodname = request.getParameter("goodname");
		admuserw = Redis.hget(sessionId, "admuser");
		String date = request.getParameter("date");// ????????????
		String days = request.getParameter("days");
		String state = request.getParameter("state");
		String pagesize = request.getParameter("pagesize");
		String unpaid = request.getParameter("unpaid");
		String orderarrs=request.getParameter("orderarrs");
		String search_state=request.getParameter("search_state");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		if (admuserw == null || "".equals(admuserw)) {
			request.getRequestDispatcher("website/main_login.jsp").forward(request, response);
			return;
		}
		Integer adminid = admuser.getId();
		Integer cgid = admuser.getId();
		orderid=StringUtils.isStrNull(orderid)?null:orderid;
		admid=StringUtils.isStrNull(admid)?"":admid;
		userid=StringUtils.isStrNull(userid)?"":userid;
		orderno=StringUtils.isStrNull(orderno)?"":orderno;
		goodid=StringUtils.isStrNull(goodid)?"":goodid;
		goodname=StringUtils.isStrNull(goodname)?"":goodname;
		date=StringUtils.isStrNull(date)?"":date;
		days=StringUtils.isStrNull(days)?"":days;
		state=StringUtils.isStrNull(state)?"":state;
		int unpay = 0;
		unpay=StringUtils.isStrNull(unpaid)?0:Integer.parseInt(unpaid);
		System.out.println("=======================????????????====purchaseServer.findPageByCondition:"+ sdf1.format(new Date()) + "=================");
		Page page = purchaseServer.findPageByCondition(pagenum, orderid, admid, userid, orderno, goodid, date, days,
				state, unpay,50, orderid_no_array, goodsid, goodname,orderarrs,search_state);
		System.out.println("=======================????????????====purchaseServer.findPageByCondition:"+ sdf1.format(new Date()) + "=================");
		request.setAttribute("pagenum", page.getPagenum());
		request.setAttribute("totalnum", page.getTotalrecords());
		request.setAttribute("totalpage", page.getTotalpage());
		request.setAttribute("pblist", page.getRecords());
		request.setAttribute("admid", admid==null || "".equals(admid) || "1".equals(admid)?"999":admid);
		request.setAttribute("cgid", cgid);
		request.setAttribute("userid", userid);
		request.setAttribute("idtypes_", idtypes_);
		request.setAttribute("goodname", goodname);
		request.setAttribute("search_state", search_state);
		request.setAttribute("orderno", orderno);
		request.setAttribute("goodid", "5201314".equals(goodid) || "5201315".equals(goodid)?"":goodid);
		request.setAttribute("date", date);
		request.setAttribute("days", days);
		System.out.println("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" + days);
		request.setAttribute("state", state);
		request.setAttribute("admuser", admuser);
		request.setAttribute("unpay", unpay);
		request.setAttribute("page_size", Integer.parseInt(pagesize));

		// ????????????camry??????????????????????????????????????????????????????
		if (admuser.getRoletype().equals("0") || "camry".toLowerCase().equals(admuser.getAdmName().toLowerCase())) {
			List<AdminUserBean> aublist = purchaseServer.getAllAdmUser();
			// ????????????camry??????????????????????????????????????????????????????
			if ("camry".toLowerCase().equals(admuser.getAdmName().toLowerCase())) {
				for (int i = 0; i < aublist.size(); i++) {
					if (aublist.get(i).getRoleType() != 2) {
						aublist.remove(i);
					}
				}
			}
			request.setAttribute("aublist", aublist);
		} else {
			List<AdminUserBean> aublist = new ArrayList<AdminUserBean>();
			AdminUserBean aub = new AdminUserBean();
			aub.setId(adminid);
			aub.setAdmName(admuser.getAdmName());
			aublist.add(aub);
			request.setAttribute("aublist", aublist);
		}
		System.out.println("=======================????????????====:" + sdf1.format(new Date()) + "=================");
		List<OrderProductSource> goodsids = purchaseServer.getAllGoodsids(adminid);
		request.setAttribute("goodsids", JSONArray.toJSONString(goodsids));
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		request.setAttribute("hideTr", "<script>hideTr()</script>");
		request.setAttribute("keepValue", "<script>keepValue()</script>");
		request.getRequestDispatcher("website/PurchaseShow.jsp").forward(request, response);
		// DBHelper2.closeConnection();
	}

	// ?????????????????????
	public void getOutBefore(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userid = 0;

		// ???????????? ???????????? ??????????????????
		// List<outIdBean> uibList = purchaseServer.findOutId(userid, "");
		// //???????????? x
		// request.setAttribute("uiblist", uibList);

		List<outIdBean> idList = new ArrayList<outIdBean>(); // ??????id
		idList = purchaseServer.getOutNowId();
		request.setAttribute("idlist", idList);

		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		request.setAttribute("admuser", admuser);
		// request.getRequestDispatcher("website/PurchaseExportBefore.jsp").forward(request,
		// response);
		request.getRequestDispatcher("website/PurchaseExportBeforeNew.jsp").forward(request, response);
	}

	// ?????????????????????
	public void getOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<outIdBean> uibList = purchaseServer.findOutIdTwo(0);
		request.setAttribute("uiblist", uibList);
		List<outIdBean> idList = new ArrayList<outIdBean>();
		idList = purchaseServer.getOutNowIdTwo();
		request.setAttribute("idlist", idList);
		// ??????CodeMaster?????????
		List<CodeMaster> logisticsList = purchaseServer.getCodeMaster();
		request.setAttribute("logisticsList", logisticsList);
		request.setAttribute("hideorder", "<script>FnHideOrder()</script>");
		request.getRequestDispatcher("website/PurchaseExport.jsp").forward(request, response);
	}



	// ????????????
	public void AddResource(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String,String> map=new HashMap<String, String>();
		String type = request.getParameter("type");
		String admidd = request.getParameter("admid");
		int admid = Integer.parseInt(admidd);
		String useridd = request.getParameter("userid");
		int userid = Integer.parseInt(useridd);
		String goodsdata_idd = request.getParameter("goodsdata_id");
		int goodsdataid = Integer.parseInt(goodsdata_idd);
		String goods_url = request.getParameter("goods_url");
		String googs_img = request.getParameter("googs_img");
		String goods_pricee = request.getParameter("goods_price");
		double goodsprice = Double.parseDouble(goods_pricee);
		String goods_title = request.getParameter("goods_title");
		String googs_numberr = request.getParameter("googs_number");
		int googsnumber = Integer.parseInt(googs_numberr);
		String orderNo = request.getParameter("orderNo");
		String shop_id = request.getParameter("shop_id");
		String goodidd = request.getParameter("goodid");
		String cGoodstypee = request.getParameter("cGoodstype");
		int goodid = Integer.parseInt(goodidd);
		String strtemp = request.getParameter("od_id");
		int od_id = Integer.parseInt(request.getParameter("od_id"));// order_details
		String reason = request.getParameter("reason");
		String state_flag=request.getParameter("state_flag");
		String straight_address=request.getParameter("straight_address");
		double price;
		String resource;
		if (reason.contains("?????????")) {
			price = 0;
			resource = "";
		} else {
			String pricee = request.getParameter("price");
			price = Double.parseDouble(pricee);
			resource = request.getParameter("resource");
			if (resource.contains("1688.com")) {
				resource = resource.substring(0, resource.indexOf(".html") + 5);
			}else if(resource.contains("taobao")){
				String x=resource.split("\\?")[0];
				String y[]=resource.split("\\?")[1].split("&");
				for(int i=0;i<y.length;i++){
					if(y[i].contains("id")){
						resource=x+"?"+y[i];
					}
				}
			}
		}
		String issuree = request.getParameter("issuree");// ??????????????????????????????
		String buycountt = request.getParameter("buycount");
		String currency = request.getParameter("currency");
		int buycount = Integer.parseInt(buycountt);
		String aduser = purchaseServer.getUserbyID(admidd);
		request.setAttribute("adusername", aduser);
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		request.setAttribute("admuser", admuser);
		request.setAttribute("admid", admid);
		int st = this.checkOrder(orderNo, goodid);
//		try{
//			if(!StringUtils.isStrNull(shop_id) && !"null".equals(shop_id)){
//				shop_id=shop_id.split("\\//")[1].split("\\.")[0];
//			}
//		}catch(Exception e){
//			st=222;
//		}
		if (st == 111) { // ???????????????

		} else if (st == 2) { // ???????????????

		} else {
			purchaseServer.AddRecource(type, admid, userid, goodsdataid, goods_url, googs_img, goodsprice, goods_title,
					googsnumber, orderNo, od_id, goodid, price, resource, buycount, reason, currency, "", cGoodstypee,
					issuree,shop_id,state_flag,straight_address);
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String time=sdf.format(date);
			SLOG.info(time+"?????????"+admid+"?????????????????????"+orderNo+"????????????"+goodid+"?????????????????????:"+resource);

			// ????????????????????????????????????
			if (!(reason == null || "".equals(reason)) && !reason.contains("???????????????")) {
				String orderRemark = "";
				String sendContent = "?????????," + admuser.getAdmName() + "???????????????[" + goodidd + "]????????????????????????";
				orderRemark = reason.replaceAll("//", "");
				// ?????????????????????
				orderRemark = orderRemark.substring(0, orderRemark.length() - 1);
				InsertMessageNotification msgNtDao = new InsertMessageNotification();
				msgNtDao.insertByOrderComment(sendContent, orderRemark, orderNo, admuser, "?????????,");
			}
		}

		PrintWriter out = response.getWriter();
		out.print(st);
		out.flush();
		out.close();
	}

	// ??????????????????
	public void AddNoGS(HttpServletRequest request, HttpServletResponse response) {
		String goods_url = request.getParameter("goods_url");
		String goods_type = request.getParameter("goods_type");
		String userid = request.getParameter("userid");
		String orderNo = request.getParameter("orderNo");
		String goodid1 = request.getParameter("goodid");
		int goodid = Integer.parseInt(goodid1);

		String aduser = purchaseServer.getUserbyID(userid);
		request.setAttribute("adusername", aduser);
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		request.setAttribute("admuser", admuser);
		request.setAttribute("admid", userid);
		int st = this.checkOrder(orderNo, goodid);
		int res = 0;
		if (st == 111) { // ???????????????

		} else if (st == 2) { // ???????????????

		} else {
			purchaseServer.AddNoGS(goods_url, goods_type, userid);
			res = 1;
		}

		try {
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ????????????127(?????????????????????????????????)
	/*
	 * public void AddResource127(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException {
	 * String type = request.getParameter("type"); String admidd =
	 * request.getParameter("admid"); int admid = Integer.parseInt(admidd);
	 * String useridd = request.getParameter("userid"); int userid =
	 * Integer.parseInt(useridd); String goodsdata_idd =
	 * request.getParameter("goodsdata_id"); int goodsdataid =
	 * Integer.parseInt(goodsdata_idd); String goods_url =
	 * request.getParameter("goods_url");
	 * 
	 * String googs_img = request.getParameter("googs_img"); String goods_pricee
	 * = request.getParameter("goods_price"); double goodsprice =
	 * Double.parseDouble(goods_pricee); String goods_title =
	 * request.getParameter("goods_title"); String googs_numberr =
	 * request.getParameter("googs_number"); int googsnumber =
	 * Integer.parseInt(googs_numberr); String orderNo =
	 * request.getParameter("orderNo"); String goodidd =
	 * request.getParameter("goodid"); int goodid = Integer.parseInt(goodidd);
	 * int od_id =
	 * Integer.parseInt(request.getParameter("od_id"));//order_details ??????ID
	 * 
	 * String reason = request.getParameter("reason"); double price; String
	 * resource;
	 * 
	 * if(reason.contains("?????????")){ price = 0; resource = ""; } else { String
	 * pricee = request.getParameter("price"); price =
	 * Double.parseDouble(pricee); //resource =
	 * TypeUtils.modefindUrl(request.getParameter("resource"),1); ?????????????????? url??????
	 * resource = request.getParameter("resource"); }
	 * 
	 * String buycountt = request.getParameter("buycount"); String currency =
	 * request.getParameter("currency"); int buycount =
	 * Integer.parseInt(buycountt); String aduser =
	 * purchaseServer.getUserbyID(admidd); request.setAttribute("adusername",
	 * aduser); String sessionId = request.getSession().getId(); String admuserw
	 * = Redis.hget(sessionId, "admuser"); SerializeUtil su = new
	 * SerializeUtil(); Admuser admuser = (Admuser) su.JsonToObj(admuserw,
	 * Admuser.class); request.setAttribute("admuser", admuser);
	 * request.setAttribute("admid", admid); int st =
	 * this.checkOrder(orderNo,goodid); if(st == 111){ //???????????????
	 * 
	 * } else if(st == 2){ //???????????????
	 * 
	 * } else { purchaseServer.AddRecource127(type,admid, userid, goodsdataid,
	 * goods_url, googs_img, goodsprice, goods_title, googsnumber, orderNo,
	 * od_id,goodid, price, resource, buycount, reason, currency, ""); }
	 * PrintWriter out = response.getWriter(); out.print(st); out.flush();
	 * out.close(); }
	 */
	// ????????????
	public void ShowRmark(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		String goodsdataid = request.getParameter("goodsdataid");
		String goodid = request.getParameter("goodid");
		response.setCharacterEncoding("UTF-8");
		OrderProductSource orderProductSource = purchaseServer.ShowRmark(orderNo, Integer.parseInt(goodsdataid), Integer.parseInt(goodid));
		PrintWriter out = response.getWriter();
		out.write(JSONObject.toJSONString(orderProductSource));
		out.flush();
		out.close();
	}

	// ?????????
	public void YiRuKu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getParameter("url");
		String orderNo = request.getParameter("orderno");
		String goodidd = request.getParameter("goodid");
		int goodid = Integer.parseInt(goodidd);
		int rk = purchaseServer.YiRuKu(url, orderNo, goodid);
		String admid = request.getParameter("admid");
		String aduser = purchaseServer.getUserbyID(admid);
		request.setAttribute("adusername", aduser);
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		request.setAttribute("admuser", admuser);
		PrintWriter out = response.getWriter();
		out.print(rk);
		out.flush();
		out.close();
	}

	// ?????????
	public void YiRuKu_crossshop(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getParameter("url");
		String orderNo = request.getParameter("orderno");
		String goodidd = request.getParameter("goodid");
		int goodid = Integer.parseInt(goodidd);
		int rk = purchaseServer.YiRuKu127(url, orderNo, goodid);
		String admid = request.getParameter("admid");
		String aduser = purchaseServer.getUserbyID(admid);
		request.setAttribute("adusername", aduser);
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		request.setAttribute("admuser", admuser);
		PrintWriter out = response.getWriter();
		out.print(rk);
		out.flush();
		out.close();
	}

	// ??????????????????
	public void getOtherSources(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		String goodidd = request.getParameter("goodid");
		String goods_url = request.getParameter("goods_url");
		int goodid = Integer.parseInt(goodidd);
		String otherUrl = "";
		int st = this.checkOrder(orderNo, goodid);
		if (st == 111) { // ???????????????
			otherUrl = "cancel";
		} else if (st == 2) { // ???????????????
			otherUrl = "cancel";
		} else {
			otherUrl = purchaseServer.getOtherSources(orderNo, goodid, goods_url);
		}
		PrintWriter out = response.getWriter();
		out.print(otherUrl);
		out.flush();
		out.close();
	}
	

	// ????????????one(????????????????????????WarehouseCtrl.purchaseConfirm ?????????)
	public void PurchaseComfirmOne(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		String goodidd = request.getParameter("goodid");
		int goodid = Integer.parseInt(goodidd);
		String adminid = request.getParameter("adminid");
		int admid = Integer.parseInt(adminid);
		int i = purchaseServer.PurchaseComfirmOne(orderNo, goodid, admid);

		if (i > 0) {
			Date nowTime = new Date();
			SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String writeStr = "======??????????????????" + goodid + "<<<<<<     ??????:" + time.format(nowTime) + "      ";
			writeStr += "orderNo:" + orderNo + "      ";
			writeStr += "goodsid:" + goodid + "      ";
			writeStr += "\r\n\r\n";
			UtilAll.printBufInfo(writeStr);
		}
		PrintWriter out = response.getWriter();
		out.print(i);
		out.flush();
		out.close();
	}

	// ????????????one(????????????????????????WarehouseCtrl.purchaseConfirm ?????????)
	public void PurchaseComfirmOne127(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		String goodidd = request.getParameter("goodid");
		int goodid = Integer.parseInt(goodidd);
		String adminid = request.getParameter("adminid");
		int admid = Integer.parseInt(adminid);
		int i = purchaseServer.PurchaseComfirmOne127(orderNo, goodid, admid);
		PrintWriter out = response.getWriter();
		out.print(i);
		out.flush();
		out.close();
	}

	// ????????????
	public void PurchaseComfirmOneQxhy(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		String odids = request.getParameter("odid");
		int odid = Integer.parseInt(odids);
		String adminid = request.getParameter("adminid");
		int admid = Integer.parseInt(adminid);
		// ??????
		int i = purchaseServer.PurchaseComfirmOneQxhy(orderNo, odid, admid);
		// ????????????
		if (i > 0) {
			Date nowTime = new Date();
			SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String writeStr = "******??????????????????" + odid + "<<<<<<     ??????:" + time.format(nowTime) + "      ";
			writeStr += "orderNo:" + orderNo + "      ";
			writeStr += "goodsid:" + odid + "      ";
			writeStr += "\r\n\r\n";
			UtilAll.printBufInfo(writeStr);
		}

		PrintWriter out = response.getWriter();
		out.print(i);
		out.flush();
		out.close();
	}

	class AllQxcgQr implements Callable<String> {
		private Map<String, String> map;

		public AllQxcgQr(Map<String, String> map) {
			this.map = map;
		}

		@Override
		public String call() throws Exception {
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			String adminid = (String) map.get("adminid");
			int admid = Integer.parseInt(adminid);

			int st = purchaseServer.PurchaseComfirmOne(orderNo, goodid, admid);
			if (st > 0) {
				// ????????????
				Date nowTime = new Date();
				SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String writeStr = "======??????????????????" + goodid + "<<<<<<     ??????:" + time.format(nowTime) + "      ";
				writeStr += "orderNo:" + orderNo + "      ";
				writeStr += "goodsid:" + goodid + "      ";
				writeStr += "\r\n\r\n";
				UtilAll.printBufInfo(writeStr);
				return orderNo + goodid;
			} else {
				return "";
			}
		}
	}

	// ?????????????????? ?????????????????????
	public void allQxcgQr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, InterruptedException, ExecutionException {/*
		String listmap = request.getParameter("listmap");
		JSONObject json = JSONObject.fromObject(listmap);
		List<Map<String, String>> edit = (List<Map<String, String>>) json.getJSONArray("listmap");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
																				// ????????????????????????Executor??????????????????????????????
		for (int i = 0; i < edit.size(); i++) {
			try {
				results.add(exec.submit(new AllQxcgQr(edit.get(i))));
			} catch (Exception e) {
				results.add(exec.submit(new AllQxcgQr(edit.get(i))));
			}
		}

		for (int i = 0; i < results.size(); i++) {
			Future<String> fs = results.get(i);
			Map<String, String> map = edit.get(i);
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			Map<String, String> m = new HashMap<String, String>();
			m.put("orderno", orderNo);
			m.put("goodsid", goodid + "");
			list.add(m);
			if (!"".equals(fs.get())) {

				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
			} else {
				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
				exec.submit(new AllQxcgQr(edit.get(i)));
			}
		}
		exec.shutdown();
		PrintWriter out = response.getWriter();
		//
		out.print(JSONArray.fromObject(list).toString());
		out.flush();
		out.close();
	*/}

	public int PurchaseComfirmOne(String sql, String sqltwo, String sqlthree) {
		String orderNo = null;
		int goodsid = 0;
		int adminid;
		int i = 0;
		// sql = "update order_product_source set
		// confirm_userid=null,confirm_time=null,purchase_state=1 where
		// orderid=? and goodsid=? and del=0";
		// String sqltwo = "update order_details set
		// purchase_state=0,purchase_time=null,purchase_confirmation=null where
		// orderid=? and goodsid=?";
		// String sqlthree = "update orderinfo set
		// purchase_number=purchase_number-1 where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null, stmttwo = null, stmtinfo = null;
		try {
			stmt = conn.prepareStatement(sql);
			// stmt.setString(1, orderNo);
			// stmt.setInt(2, goodsid);

			stmt.executeUpdate();
			i++;
			stmttwo = conn.prepareStatement(sqltwo);
			// stmttwo.setString(1, orderNo);
			// stmttwo.setInt(2, goodsid);
			stmttwo.executeUpdate();

			stmtinfo = conn.prepareStatement(sqlthree);
			// stmtinfo.setString(1, orderNo);
			stmtinfo.executeUpdate();
			i++;
			// this.order_state(conn, orderNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttwo != null) {
				try {
					stmttwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtinfo != null) {
				try {
					stmtinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	// ???????????????????????? ?????? ?????????????????????
	class AllcgqrQr_crossshop implements Callable<String> {
		private Map<String, String> map;

		public AllcgqrQr_crossshop(Map<String, String> map) {
			this.map = map;
		}

		@Override
		public String call() throws Exception {
			String goodsurl = (String) map.get("goodsurl");
			String googsimg = (String) map.get("googsimg");
			String goodsprice = (String) map.get("goodsprice");// ????????????
			String goodstitle = (String) map.get("goodstitle");
			String googsnumberr = (String) map.get("googsnumber");// ????????????
			int googsnumber = Integer.parseInt(googsnumberr);
			String oldValue = (String) map.get("oldValue");// ????????????
			String newValue = (String) map.get("newValue");// ??????
			String purchaseCountt = (String) map.get("purchaseCount");// ????????????
			int purchaseCount = Integer.parseInt(purchaseCountt);
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			String adminid = (String) map.get("adminid");
			int admid = Integer.parseInt(adminid);
			String useridd = (String) map.get("userid");
			int userid = Integer.parseInt(useridd);
			String goodsdata_idd = (String) map.get("goodsdataid");
			int goodsdataid = Integer.parseInt(goodsdata_idd);
			int st = checkOrder(orderNo, goodid);
			int od_id = Integer.parseInt((String) map.get("od_id"));// order_details
																	// ??????ID
			if (st == 111) { // ???????????????

			} else if (st == 2) { // ???????????????

			} else {
				st = purchaseServer.PurchaseComfirmTwo_crossshop(userid, orderNo, od_id, goodid, goodsdataid, admid,
						goodsurl, googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);

				if (st > 0) {

					return orderNo + goodid;
				} else {
					return "";
				}
			}
			return "";
		}
	}

	// ???????????????????????? ?????????????????????
	class AllcgqrQr implements Callable<String> {
		private Map<String, String> map;

		public AllcgqrQr(Map<String, String> map) {
			this.map = map;
		}

		@Override
		public String call() throws Exception {
			String goodsurl = (String) map.get("goodsurl");
			String googsimg = (String) map.get("googsimg");
			String goodsprice = (String) map.get("goodsprice");// ????????????
			String goodstitle = (String) map.get("goodstitle");
			String googsnumberr = (String) map.get("googsnumber");// ????????????
			int googsnumber = Integer.parseInt(googsnumberr);
			String oldValue = (String) map.get("oldValue");// ????????????
			String newValue = (String) map.get("newValue");// ??????
			String purchaseCountt = (String) map.get("purchaseCount");// ????????????
			int purchaseCount = Integer.parseInt(purchaseCountt);
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			String adminid = (String) map.get("adminid");
			int admid = Integer.parseInt(adminid);
			String useridd = (String) map.get("userid");
			int userid = Integer.parseInt(useridd);
			String goodsdata_idd = (String) map.get("goodsdataid");
			int goodsdataid = Integer.parseInt(goodsdata_idd);
			int st = checkOrder(orderNo, goodid);
			int od_id = Integer.parseInt((String) map.get("od_id"));// order_details
																	// ??????ID
			if (st == 111) { // ???????????????

			} else if (st == 2) { // ???????????????

			} else {
				st = purchaseServer.PurchaseComfirmTwo(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl,
						googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);
				if (st > 0) {
					Date nowTime = new Date();
					SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String writeStr = "======??????????????????" + goodid + ">>>>>>     ??????:" + time.format(nowTime) + "      ";
					writeStr += "userid:" + userid + "      ";
					writeStr += "orderNo:" + orderNo + "      ";
					writeStr += "goodsid:" + goodid + "      ";
					writeStr += "????????????:" + newValue + "      ";
					writeStr += "\r\n\r\n";
					UtilAll.printBufInfo(writeStr);
					return orderNo + goodid;
				} else {
					return "";
				}
			}
			return "";
		}
	}

	// ?????????????????? ?????? ?????????????????????
	public void allcgqrQr_crossshop(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, InterruptedException, ExecutionException {/*
		String listmap = request.getParameter("listmap");
		JSONObject json = JSONObject.fromObject(listmap);
		List<Map<String, String>> edit = (List<Map<String, String>>) json.getJSONArray("listmap");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
																				// ????????????????????????Executor??????????????????????????????

		for (int i = 0; i < edit.size(); i++) {
			try {
				results.add(exec.submit(new AllcgqrQr_crossshop(edit.get(i))));
			} catch (Exception e) {
				System.out.println("?????????" + edit.get(i).get("goodsid"));
				results.add(exec.submit(new AllcgqrQr_crossshop(edit.get(i))));
			}

		}

		for (int i = 0; i < results.size(); i++) {
			Future<String> fs = results.get(i);
			Map<String, String> map = edit.get(i);
			// (Future<String> fs : results) {
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			Map<String, String> m = new HashMap<String, String>();
			m.put("orderno", orderNo);
			m.put("goodsid", goodid + "");
			list.add(m);
			if (!"".equals(fs.get())) {

				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
			} else {
				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
				exec.submit(new AllcgqrQr(edit.get(i)));
			}
		}
		exec.shutdown();
		PrintWriter out = response.getWriter();
		//
		out.print(JSONArray.fromObject(list).toString());
		out.flush();
		out.close();
	*/}

	// ?????????????????? ?????? ?????????????????????
	public void allcgqrQr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, InterruptedException, ExecutionException {/*
		String listmap = request.getParameter("listmap");
		JSONObject json = JSONObject.fromObject(listmap);
		List<Map<String, String>> edit = (List<Map<String, String>>) json.getJSONArray("listmap");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
																				// ????????????????????????Executor??????????????????????????????

		for (int i = 0; i < edit.size(); i++) {
			try {
				results.add(exec.submit(new AllcgqrQr(edit.get(i))));
			} catch (Exception e) {
				System.out.println("?????????" + edit.get(i).get("goodsid"));
				results.add(exec.submit(new AllcgqrQr(edit.get(i))));
			}

		}

		for (int i = 0; i < results.size(); i++) {
			Future<String> fs = results.get(i);
			Map<String, String> map = edit.get(i);
			// (Future<String> fs : results) {
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			Map<String, String> m = new HashMap<String, String>();
			m.put("orderno", orderNo);
			m.put("goodsid", goodid + "");
			list.add(m);
			if (!"".equals(fs.get())) {

				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
			} else {
				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
				exec.submit(new AllcgqrQr(edit.get(i)));
			}
		}
		exec.shutdown();
		PrintWriter out = response.getWriter();
		//
		out.print(JSONArray.fromObject(list).toString());
		out.flush();
		out.close();
	*/}

	// ???????????? //?????????????????????
	public void PurchaseComfirmTwo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String goodsurl = request.getParameter("goodsurl");
		String googsimg = request.getParameter("googsimg");
		String goodsprice = request.getParameter("goodsprice");// ????????????
		String goodstitle = request.getParameter("goodstitle");
		String googsnumberr = request.getParameter("googsnumber");// ????????????
		int googsnumber = Integer.parseInt(googsnumberr);
		String oldValue = request.getParameter("oldValue");// ????????????
		String newValue = request.getParameter("newValue");// ??????
		String purchaseCountt = request.getParameter("purchaseCount");// ????????????
		int purchaseCount = Integer.parseInt(purchaseCountt);
		String orderNo = request.getParameter("orderno");
		String goodidd = request.getParameter("goodsid");
		int goodid = Integer.parseInt(goodidd);
		String adminid = request.getParameter("adminid");
		int admid = Integer.parseInt(adminid);
		String useridd = request.getParameter("userid");
		int userid = Integer.parseInt(useridd);
		String goodsdata_idd = request.getParameter("goodsdataid");
		int goodsdataid = Integer.parseInt(goodsdata_idd);
		int st = this.checkOrder(orderNo, goodid);
		int od_id = Integer.parseInt(request.getParameter("od_id"));// order_details
																	// ??????ID
		if (st == 111) { // ???????????????

		} else if (st == 2) { // ???????????????

		} else {
			st = purchaseServer.PurchaseComfirmTwo(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl,
					googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);

			// st =
			// purchaseServer.purchaseConfirmation("1",admid,orderNo,goodid);
			if (st != 0) {
				Date nowTime = new Date();
				SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String writeStr = "======??????????????????" + goodid + ">>>>>>     ??????:" + time.format(nowTime) + "      ";
				writeStr += "userid:" + userid + "      ";
				writeStr += "orderNo:" + orderNo + "      ";
				writeStr += "goodsid:" + goodid + "      ";
				writeStr += "????????????:" + newValue + "      ";
				writeStr += "\r\n\r\n";
				UtilAll.printBufInfo(writeStr);
				st = 100;
			}
		}
		PrintWriter out = response.getWriter();
		out.print(st);
		out.flush();
		out.close();
	}

	// ???????????? //crossshop //?????????
	public void PurchaseComfirmTwo_crossshop(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String goodsurl = request.getParameter("goodsurl");
		String googsimg = request.getParameter("googsimg");
		String goodsprice = request.getParameter("goodsprice");// ????????????
		String goodstitle = request.getParameter("goodstitle");
		String googsnumberr = request.getParameter("googsnumber");// ????????????
		int googsnumber = Integer.parseInt(googsnumberr);
		String oldValue = request.getParameter("oldValue");// ????????????
		String newValue = request.getParameter("newValue");// ??????
		String purchaseCountt = request.getParameter("purchaseCount");// ????????????
		int purchaseCount = Integer.parseInt(purchaseCountt);
		String orderNo = request.getParameter("orderno");
		String goodidd = request.getParameter("goodsid");
		int goodid = Integer.parseInt(goodidd);
		String adminid = request.getParameter("adminid");
		int admid = Integer.parseInt(adminid);
		String useridd = request.getParameter("userid");
		int userid = Integer.parseInt(useridd);
		String goodsdata_idd = request.getParameter("goodsdataid");
		int goodsdataid = Integer.parseInt(goodsdata_idd);
		int st = this.checkOrder(orderNo, goodid);
		int od_id = Integer.parseInt(request.getParameter("od_id"));// order_details
																	// ??????ID
		if (st == 111) { // ???????????????

		} else if (st == 2) { // ???????????????

		} else {
			st = purchaseServer.PurchaseComfirmTwo127(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl,
					googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);
			if (st != 0) {
				st = 100;
			}
		}
		PrintWriter out = response.getWriter();
		out.print(st);
		out.flush();
		out.close();
	}

	// ????????????
	private static HashMap<String, String> toHashMap(Object object) {
		HashMap<String, String> data = new HashMap<String, String>();
		// ???json??????????????????jsonObject
		JSONObject jsonObject = JSONObject.parseObject(object.toString());
		Iterator it = jsonObject.keySet().iterator();
		// ??????jsonObject??????????????????Map??????
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = (String) jsonObject.get(key);
			data.put(key, value);
		}
		return data;
	}

	class AllQxQr implements Callable<String> {
		private Map<String, String> map;

		public AllQxQr(Map<String, String> map) {
			this.map = map;
		}

		@Override
		public String call() throws Exception {

			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			String adminid = (String) map.get("adminid");
			int admid = Integer.parseInt(adminid);

			// ??????
			int r = purchaseServer.PurchaseComfirmOneQxhy(orderNo, goodid, admid);

			if (r > 0) {
				// ????????????
				Date nowTime = new Date();
				SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String writeStr = "******??????????????????" + goodid + "<<<<<<     ??????:" + time.format(nowTime) + "      ";
				writeStr += "orderNo:" + orderNo + "      ";
				writeStr += "goodsid:" + goodid + "      ";
				writeStr += "\r\n\r\n";
				UtilAll.printBufInfo(writeStr);

				return orderNo + goodid;
			} else {
				return "";
			}
		}
	}
	
     /**
      * ???????????????????????????????????????	
      * @param request
      * @param response
      * @throws ServletException
      * @throws IOException
      * @throws NumberFormatException
      * @throws InterruptedException
      * @throws ExecutionException
      */
	public void queryPreferentialPrice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, InterruptedException, ExecutionException {
		String orderid = request.getParameter("orderid");
		int goodsid = Integer.valueOf(request.getParameter("goodsid"));
		String goods_p_url=request.getParameter("goods_p_url");
		List<PreferentialPrice> list =purchaseServer.queryPreferentialPrice(orderid,goodsid,goods_p_url);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(list));
		out.flush();
		out.close();
	}
	

	
	/**
     * ???????????????????????????????????????	
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws NumberFormatException
     * @throws InterruptedException
     * @throws ExecutionException
     */
	public void addPreferentialPrice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, InterruptedException, ExecutionException {
		Map<Object,Object> map=new HashMap<Object, Object>();
		int add_begin = Integer.valueOf(request.getParameter("add_begin"));
		int add_end = Integer.valueOf(request.getParameter("add_end"));
		double add_price = Double.valueOf(request.getParameter("add_price"));
		String orderid=request.getParameter("orderid");
		String goodsid=request.getParameter("goodsid");
		String goods_p_url=request.getParameter("goods_p_url").trim();
		String goods_p_price=request.getParameter("goods_p_price");
		map.put("add_begin", add_begin);
		map.put("add_end", add_end);
		map.put("add_price", add_price);
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("goods_p_url", goods_p_url);
		map.put("goods_p_price", goods_p_price);
		int row =purchaseServer.addPreferentialPrice(map);
		PrintWriter out = response.getWriter();
		out.print(row);
		out.flush();
		out.close();
	}
	
	/**
     * ???????????????????????????????????????	
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws NumberFormatException
     * @throws InterruptedException
     * @throws ExecutionException
     */
	public void updatePreferentialPrice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, InterruptedException, ExecutionException {
		String id = request.getParameter("id");
		Map<Object,Object> map=new HashMap<Object, Object>();
		int new_begin = Integer.valueOf(request.getParameter("new_begin"));
		int new_end = Integer.valueOf(request.getParameter("new_end"));
		double new_price = Double.valueOf(request.getParameter("new_price"));
		String type=request.getParameter("type");
		String old_begin=request.getParameter("old_begin");
		String old_end=request.getParameter("old_end");
		String goods_p_itemid=request.getParameter("goods_p_itemid");
		map.put("id", id);
		map.put("new_begin", new_begin);
		map.put("new_end", new_end);
		map.put("new_price", new_price);
		map.put("type", type);
		map.put("goods_p_itemid", goods_p_itemid);
		map.put("old_begin", old_begin);
		map.put("old_end", old_end);
		int row =purchaseServer.updatePreferentialPrice(map);
		PrintWriter out = response.getWriter();
		out.print(row);
		out.flush();
		out.close();
	}

	public void allQxQr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, InterruptedException, ExecutionException {
		String listmap = request.getParameter("listmap");
		JSONObject json = JSONObject.parseObject(listmap);
		List<Map> edit = JSONArray.parseArray(json.getString("listmap"), Map.class);

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
																				// ????????????????????????Executor??????????????????????????????

		for (int i = 0; i < edit.size(); i++) {
			try {
				results.add(exec.submit(new AllQxQr(edit.get(i))));
			} catch (Exception e) {
				results.add(exec.submit(new AllQxQr(edit.get(i))));
			}
		}

		for (int i = 0; i < results.size(); i++) {
			Future<String> fs = results.get(i);
			Map<String, String> map = edit.get(i);
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			Map<String, String> m = new HashMap<String, String>();
			m.put("orderno", orderNo);
			m.put("goodsid", goodid + "");
			list.add(m);
			if (!"".equals(fs.get())) {

				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
			} else {
				System.out.println("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
				exec.submit(new AllQxQr(edit.get(i)));
			}
		}
		exec.shutdown();
		PrintWriter out = response.getWriter();
		//
		out.print(JSONArray.toJSONString(list));
		out.flush();
		out.close();
	}

	// Callable
	class TaskWithResult implements Callable<String> {
		private Map<String, String> map;

		public TaskWithResult(Map<String, String> map) {
			this.map = map;
		}

		@Override
		public String call() throws Exception {
			String goodsurl = (String) map.get("goodsurl");
			String googsimg = (String) map.get("googsimg");
			String goodsprice = (String) map.get("goodsprice");// ????????????
			String goodstitle = (String) map.get("goodstitle");
			String googsnumberr = (String) map.get("googsnumber");// ????????????
			int googsnumber = Integer.parseInt(googsnumberr);
			String oldValue = (String) map.get("oldValue");// ????????????
			String newValue = (String) map.get("newValue");// ??????
			String purchaseCountt = (String) map.get("purchaseCount");// ????????????
			int purchaseCount = Integer.parseInt(purchaseCountt);
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			String adminid = (String) map.get("adminid");
			int admid = Integer.parseInt(adminid);
			String useridd = (String) map.get("userid");
			int userid = Integer.parseInt(useridd);
			String goodsdata_idd = (String) map.get("goodsdataid");
			int goodsdataid = Integer.parseInt(goodsdata_idd);
			int st = checkOrder(orderNo, goodid);
			int od_id = Integer.parseInt((String) map.get("od_id"));// order_details
																	// ??????ID
			String child_order_no = (String) map.get("child_order_no");// ?????????dropship???????????????dropship??????????????????
			String isDropshipOrder = (String) map.get("isDropshipOrder");
			if (st == 111) { // ???????????????

			} else if (st == 2) { // ??????????????? PurchaseComfirmTwoHyqr

			} else {
				// Map map = new HashMap();
				// map.put("orderid", orderNo);
				// map.put("goodsid", goodid);
				// updateGoodsDistribution(map);

				st = purchaseServer.PurchaseComfirmTwoHyqr(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl,
						googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount,
						child_order_no, isDropshipOrder);
				// ????????????
				Date nowTime = new Date();
				SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String writeStr = "******??????????????????" + goodid + ">>>>>>     ??????:" + time.format(nowTime) + "      ";
				writeStr += "userid:" + userid + "      ";
				writeStr += "orderNo:" + orderNo + "      ";
				writeStr += "goodsid:" + goodid + "      ";
				writeStr += "????????????:" + newValue + "      ";
				writeStr += "\r\n\r\n";
				// UtilAll.printBufInfo(writeStr);

				if (st > 0) {
					return orderNo + goodid;
				} else {
					return "";
				}
			}
			return "";
		}
	}
	
	public void allQxcgQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InterruptedException, ExecutionException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=purchaseServer.allQxcgQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}
	
	public void allQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InterruptedException, ExecutionException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		if(admid==null || "".equals(admid)){
			String sessionId = request.getSession().getId();
			String admuserw = Redis.hget(sessionId, "admuser");
			SerializeUtil su = new SerializeUtil();
			Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
			admid=String.valueOf(admuser.getId());
		}
		if(admid=="999"){
			admid="9";
		}
		String datas=purchaseServer.allQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}
	
	public void allQxQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InterruptedException, ExecutionException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=purchaseServer.allQxQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}
	
	public void allcgqrQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InterruptedException, ExecutionException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=purchaseServer.allcgqrQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	public void allQr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InterruptedException, ExecutionException {
		String listmap = request.getParameter("listmap");
		JSONObject json = JSONObject.parseObject(listmap);
		final List<Map> edit = JSONArray.parseArray(json.getString("listmap"),Map.class) ;

		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
																				// ????????????????????????Executor??????????????????????????????

		for (int i = 0; i < edit.size(); i++) {
			try {
				results.add(exec.submit(new TaskWithResult(edit.get(i))));
			} catch (Exception e) {
				// System.out.println("?????????" + edit.get(i).get("goodsid"));
				// ??????log??????
				LOG.error("?????????" + edit.get(i).get("goodsid") + "??????:" + e.getMessage());
				results.add(exec.submit(new TaskWithResult(edit.get(i))));
			}

		}

		for (int i = 0; i < results.size(); i++) {
			Future<String> fs = results.get(i);
			Map<String, String> map = edit.get(i);
			// (Future<String> fs : results) {
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			Map<String, String> m = new HashMap<String, String>();
			m.put("orderno", orderNo);
			m.put("goodsid", goodid + "");
			list.add(m);
			if (!"".equals(fs.get())) {
				// System.out.println("?????????" + fs.get() + "----------" + (String)
				// map.get("goodsid"));
				// ??????log??????
				LOG.info("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
			} else {
				// System.out.println("?????????" + fs.get() + "----------" + (String)
				// map.get("goodsid"));
				// ??????log??????
				LOG.error("?????????" + fs.get() + "----------" + (String) map.get("goodsid"));
				exec.submit(new TaskWithResult(edit.get(i)));
			}
		}
		exec.shutdown();

		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSON(list));
		out.flush();
		out.close();
	}

	// ????????????
	public void PurchaseComfirmTwoHyqr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// DBHelper2.setConnection("url127hop","userName127hop","userPass127hop");
		// // DBHelper.setConnection("url128","userName128","userPass128");
		// DBHelper2.init();

		String goodsurl = request.getParameter("goodsurl");
		String googsimg = request.getParameter("googsimg");
		String goodsprice = request.getParameter("goodsprice");// ????????????
		String goodstitle = request.getParameter("goodstitle");
		String googsnumberr = request.getParameter("googsnumber");// ????????????
		int googsnumber = Integer.parseInt(googsnumberr);
		String oldValue = request.getParameter("oldValue");// ????????????
		String newValue = request.getParameter("newValue");// ??????
		String purchaseCountt = request.getParameter("purchaseCount");// ????????????
		int purchaseCount = Integer.parseInt(purchaseCountt);
		String orderNo = request.getParameter("orderno");
		String goodidd = request.getParameter("goodsid");
		int goodid = Integer.parseInt(goodidd);
		String adminid = request.getParameter("adminid");
		int admid=0;
		if(adminid==null || "".equals(adminid)){
			String sessionId = request.getSession().getId();
			String admuserw = Redis.hget(sessionId, "admuser");
			SerializeUtil su = new SerializeUtil();
			Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
			admid=admuser.getId();
		}else{
			admid = Integer.parseInt(adminid);
		}
		if(admid==999){
			admid=9;
		}
		String useridd = request.getParameter("userid");
		int userid = Integer.parseInt(useridd);
		String goodsdata_idd = request.getParameter("goodsdataid");
		int goodsdataid = Integer.parseInt(goodsdata_idd);
		int st = this.checkOrder(orderNo, goodid);
		int od_id = Integer.parseInt(request.getParameter("od_id"));// order_details
																	// ??????ID
		String child_order_no = request.getParameter("child_order_no");// ?????????dropship???????????????dropship??????????????????
		String isDropshipOrder = request.getParameter("isDropshipOrder");

		if (st == 111) { // ???????????????

		} else if (st == 2) { // ??????????????? PurchaseComfirmTwoHyqr

		} else {
			st = purchaseServer.PurchaseComfirmTwoHyqr(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl,
					googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount, child_order_no,
					isDropshipOrder);//
			// Map map = new HashMap();
			// map.put("orderid", orderNo);
			// map.put("goodsid", goodid);
			// updateGoodsDistribution(map);
			if (st != 0) {
				// ????????????
				Date nowTime = new Date();
				SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String writeStr = "******??????????????????" + goodid + ">>>>>>     ??????:" + time.format(nowTime) + "      ";
				writeStr += "userid:" + userid + "      ";
				writeStr += "orderNo:" + orderNo + "      ";
				writeStr += "goodsid:" + goodid + "      ";
				writeStr += "????????????:" + newValue + "      ";
				writeStr += "\r\n\r\n";
				UtilAll.printBufInfo(writeStr);
				st = 100;
				LOG.info("??????????????????:" + time.format(nowTime));
			}
		}

		PrintWriter out = response.getWriter();
		out.print(st);
		out.flush();
		out.close();
	}

	// ??????????????????????????????????????????
	public void PurchaseComfirmTwoHyqr127(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// DBHelper2.setConnection("url127hop","userName127hop","userPass127hop");
		// // DBHelper.setConnection("url128","userName128","userPass128");
		// DBHelper2.init();

		String goodsurl = request.getParameter("goodsurl");
		String googsimg = request.getParameter("googsimg");
		String goodsprice = request.getParameter("goodsprice");// ????????????
		String goodstitle = request.getParameter("goodstitle");
		String googsnumberr = request.getParameter("googsnumber");// ????????????
		int googsnumber = Integer.parseInt(googsnumberr);
		String oldValue = request.getParameter("oldValue");// ????????????
		String newValue = request.getParameter("newValue");// ??????
		String purchaseCountt = request.getParameter("purchaseCount");// ????????????
		int purchaseCount = Integer.parseInt(purchaseCountt);
		String orderNo = request.getParameter("orderno");
		String goodidd = request.getParameter("goodsid");
		int goodid = Integer.parseInt(goodidd);
		String adminid = request.getParameter("adminid");
		int admid = Integer.parseInt(adminid);
		String useridd = request.getParameter("userid");
		int userid = Integer.parseInt(useridd);
		String goodsdata_idd = request.getParameter("goodsdataid");
		int goodsdataid = Integer.parseInt(goodsdata_idd);
		int st = this.checkOrder(orderNo, goodid);
		int od_id = Integer.parseInt(request.getParameter("od_id"));// order_details
																	// ??????ID
		if (st == 111) { // ???????????????

		} else if (st == 2) { // ??????????????? PurchaseComfirmTwoHyqr

		} else {
			st = purchaseServer.PurchaseComfirmTwoHyqr127(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl,
					googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);

			// ????????????????????????
			if (st != 0) {
				st = 100;
			}
		}
		PrintWriter out = response.getWriter();
		out.print(st);
		out.flush();
		out.close();
	}

	// ??????????????????????????????????????? ////////////////////
	public void getOutById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userid = request.getParameter("userid");
		List<outIdBean> uibList = purchaseServer.findOutIdTwo(Integer.parseInt(userid));
		request.setAttribute("uiblist", uibList);
		if (Integer.parseInt(userid) == 0) {
			request.setAttribute("userid", "??????");
		} else {
			request.setAttribute("userid", userid);
		}
		List<outIdBean> idList = new ArrayList<outIdBean>();
		idList = purchaseServer.getOutNowId();
		request.setAttribute("idlist", idList);
		String Email = purchaseServer.getEmail(Integer.parseInt(userid));
		request.setAttribute("email", Email);
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		request.setAttribute("admuser", admuser);
		request.setAttribute("hideorder", "<script>FnHideOrder()</script>");
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("website/PurchaseExportDetails.jsp");
		homeDispatcher.forward(request, response);
	}

	// ???????????????????????????
	public void getOrdersById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userid = request.getParameter("userid");
		// List<outIdBean> uibList =
		// purchaseServer.findOutId(Integer.valueOf(userid), null);
		// //????????????????????????????????? ?????????????????????????????????

		PurchaseDaoImpl pddao = new PurchaseDaoImpl();
		// ???????????? ????????????????????? ??????
		List<PurchaseBean> list = pddao.getOrdersbyidNew(userid);

		PrintWriter out = response.getWriter();
		List odlist = new ArrayList();
		for (PurchaseBean pb : list) {

			odlist.add(pb.getOrderNo() + pb.getOfState()); // ???????????????+zhaungt
		}

		// for(outIdBean ul:uibList){
		// for(PurchaseBean pb:ul.getPurchaseBean()){
		//
		//// odlist.add(pb.getOrderNo());
		// odlist.add(pb.getOrderNo()+pb.getOfState()); // ???????????????+zhaungt
		// }
		// }
		out.print(odlist);
		out.close();
	}

	// ???????????????????????????????????????/\/\/\/\/\/\/\/
	public void getDetailsByOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String admid = request.getParameter("admid");
		String userid = request.getParameter("userid");
		String order = request.getParameter("orderno");
		OrderPayDetails orderPayDetail = purchaseServer.getDetailsByOrder("", userid, order);
		feeCount fc = new feeCount();
		String countrycode = fc.fpxFee(orderPayDetail.getMode_transport());// return
																			// country_code
		if (countrycode.equals("")) {
			request.setAttribute("ccb", "");
		} else {
			request.setAttribute("ccb", countrycode);
			request.setAttribute("ccb1", orderPayDetail.getCountry());
		}
		List<CountryCodeBean> ccbList = purchaseServer.getCountryCode();
		request.setAttribute("ccblist", ccbList);

		List<ProductCodeBean> pcbList = purchaseServer.getProductCode();
		request.setAttribute("pcblist", pcbList);
		String yfharea = fc.yfhFee(countrycode);
		if (yfharea.equals("")) {
			List<YFHareaBean> arealist = new ArrayList<YFHareaBean>();
			YFHareaBean yab1 = new YFHareaBean();
			YFHareaBean yab2 = new YFHareaBean();
			YFHareaBean yab3 = new YFHareaBean();
			YFHareaBean yab4 = new YFHareaBean();
			YFHareaBean yab5 = new YFHareaBean();
			yab1.setYfharea_name("???/???/???");
			yab2.setYfharea_name("??????");
			yab3.setYfharea_name("??????");
			yab4.setYfharea_name("??????");
			yab5.setYfharea_name("??????");
			arealist.add(yab1);
			arealist.add(yab2);
			arealist.add(yab3);
			arealist.add(yab4);
			arealist.add(yab5);
			request.setAttribute("yfharea", yfharea);
			request.setAttribute("arealist", arealist);
		} else {
			request.setAttribute("yfharea", yfharea);
		}
		// ??????????????????
		List<OrderDatailsNew> odnList = purchaseServer.getOrderdataelsNew(order);
		request.setAttribute("odnList", odnList);

		// request.setAttribute("datelsScript",
		// "<script>test1('"+odnList+"')</script>");
		request.setAttribute("orderPayDetail", orderPayDetail);
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("website/PurchaseExportBeforeDetails.jsp");
		// ???????????????????????? ?????? ????????????
		List<CodeMaster> logisticsList = purchaseServer.getCodeMaster();
		request.setAttribute("logisticsList", logisticsList);
		homeDispatcher.forward(request, response);
	}

	public void saveOrderFeeDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userid = Integer.parseInt(request.getParameter("uid"));
		String orderno = request.getParameter("order");
		double pkfee = Double.parseDouble(request.getParameter("packagefee"));
		String t = request.getParameter("actfee");
		double actfee = 0.0;
		if (t != null && !"".equals(t)) {
			actfee = Double.parseDouble(t);
		}
		double actcgetfee = 0.0;
		t = request.getParameter("actcgetfee");
		if (t != null && !"".equals(t)) {
			actcgetfee = Double.parseDouble(t);
		}
		String volume = request.getParameter("volume");

		double weight = Double.parseDouble(request.getParameter("weight"));
		String country = request.getParameter("country");
		String[] cty = country.split("@"); // request.getParameter("feetrans")

		double feetrans = 0.0;
		t = request.getParameter("feetrans");
		if (t != null && !"".equals(t)) {
			feetrans = Double.parseDouble(t);
		}
		// System.out.println("==="+request.getParameter("feetrans")+"----");

		// feetrans = Double.parseDouble(request.getParameter("feetrans"));
		double feecount = 0.0;
		t = request.getParameter("feecount");
		if (t != null && !"".equals(t)) {
			feecount = Double.parseDouble(t);
		}
		// feecount = Double.parseDouble(request.getParameter("feecount"));
		int transport = Integer.parseInt(request.getParameter("transport"));// 1:4PX;2:YuanFeiHang
		String zone = request.getParameter("zone");
		String trans = request.getParameter("trans");
		String area = request.getParameter("area");
		String days = request.getParameter("days");
		String admin = request.getParameter("admin");
		String currency = request.getParameter("currency");
		String jcCargoType = request.getParameter("jcCargoType");// jcCargoType
																	// ????????????
		int idts = Integer.parseInt(request.getParameter("id_ts"));
		double app_credit = Double.parseDouble(request.getParameter("app_credit"));
		double deduction = Double.parseDouble(request.getParameter("deduction"));

		String yhfNum = request.getParameter("yhfNum");
		OrderFeeDetails ofd = new OrderFeeDetails();
		ofd.setYfhNum(yhfNum);
		ofd.setCargoType(jcCargoType); // ????????????
		ofd.setUid(userid);
		ofd.setOrder(orderno);
		ofd.setDelivery_time(days);
		ofd.setCurrency(currency);
		ofd.setPackagefee(pkfee);
		ofd.setActfee(actfee);
		ofd.setActcgetfee(actcgetfee);
		ofd.setVolume(volume);
		ofd.setWeight(weight);
		ofd.setCountry(cty[cty.length - 3]);
		ofd.setFeetrans(feetrans);
		ofd.setFeecount(feecount);
		ofd.setIdts(idts);
		ofd.setApp_credit(app_credit);
		ofd.setDeduction(deduction);
		if (transport == 1) {
			ofd.setTransport("4PX");
			ofd.setTrans(trans);
		} else if (transport == 2) {
			ofd.setTransport("YFH");
			ofd.setTrans("YFH");
		} else if (transport == 5) {
			ofd.setTransport("other");
			ofd.setTrans(trans);
		} else if (transport == 3) {
			ofd.setTransport("JCEX");
			ofd.setTrans(trans);
		}
		ofd.setZone(zone);
		ofd.setOrder_area(area);
		ofd.setAdmin(admin);
		int i = purchaseServer.saveOrderFee(ofd); // ??????????????????
		PrintWriter out = response.getWriter();
		out.print(i);
		out.close();
	}

	// ??????????????????
	public void showWuLiu(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderlist = request.getParameter("cks");
		String outmethod = request.getParameter("outmethod");// ????????????,0:4PX;1:?????????
		UserOrderDetails uod = purchaseServer.getUserDetails(orderlist);
		request.setAttribute("logistics", uod);
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("website/PurchaseExportWuliu.jsp");
		homeDispatcher.forward(request, response);
	}

	// ????????????
	public String outJc(UserOrderDetails uod, String billNo) throws Exception {
		JcgjSoapHttpPost jc = new JcgjSoapHttpPost();
		DataInfo di = new DataInfo();
		di.setHYBM("SH0809"); // ????????????
		di.setPWD("SH61504007"); // ????????????
		di.setBillNo(billNo);
		// di.setBillNo("55555555566"); //?????????
		di.setKJLX(uod.getGoodstype()); // ????????????
		di.setFJRGS(uod.getAdmincompany()); // ???????????????
		di.setFJRDH(uod.getAdminphone()); // ???????????????
		di.setFJRXM(uod.getAdminname()); // ???????????????
		di.setHGZCBM(""); // ??????????????????

		di.setSJRXM(uod.getUserName()); // ???????????????
		di.setSJRDH(uod.getUserphone()); // ???????????????
		di.setSJRGJ(uod.getUserzone()); // ???????????????
		di.setSJRYB(uod.getUsercode()); // ???????????????
		di.setSJRCS_YWMC(""); // ??????????????? ???
		di.setSTATENAME(uod.getUserstate()); // ???????????????
		di.setSJRCS(uod.getUsercity()); // ???????????????
		di.setSJRGS(uod.getUsercompany()); // ???????????????
		di.setPPCC(""); // ???????????? 1.PP 2.CC 3.TT

		di.setWPLX(""); // ????????????

		List<ProductBean> proBeanList = new ArrayList<ProductBean>();
		proBeanList = uod.getProductBean();

		// ????????????
		int ppp = proBeanList.size();
		for (int p = 0; p < ppp; p++) {
			// ????????????????????????(Length <= 200)
			di.setPM(proBeanList.get(p).getProducenglishtname());
			// ????????????????????????(Length <= 200)
			di.setYWPM(proBeanList.get(p).getProductname());/////// #######
			// ??????(??????: 1)(0 < Amount <= 999)
			di.setJS(proBeanList.get(p).getProductnum());

			// ????????????????????????(??????: PCE)????????????????????????????????????(Length = 3)
			di.setWPLX("PCE");// ???
			// ??????(0 < Amount <= [10,2])
			di.setSBJE(proBeanList.get(p).getProductprice());
		}
		di.setHSBM(""); // HS??????
		di.setSBBZ(""); // ????????????
		di.setZSBGBZ(""); // ?????????????????? 1-??????0-???
		di.setTYBM(""); // ????????????
		di.setSJZL(uod.getWeight()); // ??????
		di.setTJZL(uod.getWeight()); // ??????
		di.setCKMC(""); // ????????????
		di.setSUBFLAG(""); // ??????????????? ??????

		// pageck
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Map pacs[] = new HashMap[1];
		Map<String, Object> pacdata = new HashMap<String, Object>();

		pacdata.put("DataInfo", di); // di ??????

		pacs[0] = pacdata;
		mapdata.put("Package", pacs);

		String strpac = jc.objToGson(mapdata);
		System.out.println(strpac);
		//
		//
		// REQUEST ????????????

		String retmsg = jc.callRequest(strpac);
		System.out.println("REQUEST__?????????" + retmsg);
		if (retmsg.indexOf("????????????") != -1) {
			purchaseServer.OutPortNow(uod.getOrderno(), di.getBillNo(), "JCEX", uod.getUserid());
			return "10000";// ??????
		} else {
			return "1";// ??????
		}

	}

	public String outJc127(UserOrderDetails uod, String billNo) throws Exception {
		JcgjSoapHttpPost jc = new JcgjSoapHttpPost();
		DataInfo di = new DataInfo();
		di.setHYBM("SH0809"); // ????????????
		di.setPWD("SH61504007"); // ????????????
		di.setBillNo(billNo);
		// di.setBillNo("55555555566"); //?????????
		di.setKJLX(uod.getGoodstype()); // ????????????
		di.setFJRGS(uod.getAdmincompany()); // ???????????????
		di.setFJRDH(uod.getAdminphone()); // ???????????????
		di.setFJRXM(uod.getAdminname()); // ???????????????
		di.setHGZCBM(""); // ??????????????????

		di.setSJRXM(uod.getUserName()); // ???????????????
		di.setSJRDH(uod.getUserphone()); // ???????????????
		di.setSJRGJ(uod.getUserzone()); // ???????????????
		di.setSJRYB(uod.getUsercode()); // ???????????????
		di.setSJRCS_YWMC(""); // ??????????????? ???
		di.setSTATENAME(uod.getUserstate()); // ???????????????
		di.setSJRCS(uod.getUsercity()); // ???????????????
		di.setSJRGS(uod.getUsercompany()); // ???????????????
		di.setPPCC(""); // ???????????? 1.PP 2.CC 3.TT

		di.setWPLX(""); // ????????????

		List<ProductBean> proBeanList = new ArrayList<ProductBean>();
		proBeanList = uod.getProductBean();

		// ????????????
		int ppp = proBeanList.size();
		for (int p = 0; p < ppp; p++) {
			// ????????????????????????(Length <= 200)
			di.setPM(proBeanList.get(p).getProducenglishtname());
			// ????????????????????????(Length <= 200)
			di.setYWPM(proBeanList.get(p).getProductname());/////// #######
			// ??????(??????: 1)(0 < Amount <= 999)
			di.setJS(proBeanList.get(p).getProductnum());

			// ????????????????????????(??????: PCE)????????????????????????????????????(Length = 3)
			di.setWPLX("PCE");// ???
			// ??????(0 < Amount <= [10,2])
			di.setSBJE(proBeanList.get(p).getProductprice());
		}
		di.setHSBM(""); // HS??????
		di.setSBBZ(""); // ????????????
		di.setZSBGBZ(""); // ?????????????????? 1-??????0-???
		di.setTYBM(""); // ????????????
		di.setSJZL(uod.getWeight()); // ??????
		di.setTJZL(uod.getWeight()); // ??????
		di.setCKMC(""); // ????????????
		di.setSUBFLAG(""); // ??????????????? ??????

		// pageck
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Map pacs[] = new HashMap[1];
		Map<String, Object> pacdata = new HashMap<String, Object>();

		pacdata.put("DataInfo", di); // di ??????

		pacs[0] = pacdata;
		mapdata.put("Package", pacs);

		String strpac = jc.objToGson(mapdata);
		System.out.println(strpac);
		//
		//
		// REQUEST ????????????

		String retmsg = jc.callRequest(strpac);
		System.out.println("REQUEST__?????????" + retmsg);
		if (retmsg.indexOf("????????????") != -1) {
			purchaseServer.OutPortNow127(uod.getOrderno(), di.getBillNo(), "JCEX", uod.getUserid());
			return "10000";// ??????
		} else {
			return "1";// ??????
		}

	}

	// ??????
	public void OutPortNow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserOrderDetails uod = new UserOrderDetails();// ####
		String outMethod = request.getParameter("outmethod");
		uod.setOutmethod(outMethod);// ????????????,0:4PX;1:?????????;2:????????????
		uod.setOrderno(request.getParameter("orderno"));
		String vv = request.getParameter("orderno");
		String trans = request.getParameter("transport");
		if (trans != null) {
			String[] tspot = trans.split(",");
			uod.setTransport(tspot[0]);
			// String transportname = tspot[1]; //?????????????????? ????????????????????????
		}
		uod.setWeight(request.getParameter("weight"));
		uod.setGoodstype(request.getParameter("goodstype"));
		uod.setUserName(request.getParameter("username"));
		uod.setEmail(request.getParameter("useremail"));
		uod.setAdminname(request.getParameter("adminname"));
		uod.setUsercompany(request.getParameter("usercompany"));
		uod.setAdmincompany(request.getParameter("admincompany"));
		uod.setUserzone(request.getParameter("userzone"));
		uod.setAdminzone(request.getParameter("adminzone"));
		uod.setUsercode(request.getParameter("usercode"));
		uod.setAdmincode(request.getParameter("admincode"));
		uod.setUserstate(request.getParameter("userstate"));
		uod.setUsercity(request.getParameter("usercity"));
		uod.setUserstreet(request.getParameter("userstreet"));
		uod.setUseraddress(request.getParameter("useraddress"));
		uod.setAdminaddress(request.getParameter("adminaddress"));
		uod.setUserphone(request.getParameter("userphone"));
		uod.setAdminphone(request.getParameter("adminphone"));
		uod.setUserid(request.getParameter("userid"));
		uod.setAdminprovince(request.getParameter("idadminsprovince"));
		uod.setAdmincity(request.getParameter("idadmincity"));
		uod.setGoodsnum(request.getParameter("idgoodssum"));

		if (outMethod.equals("0") || outMethod.equals("3")) {
			String productBean = request.getParameter("prdktbn");
			if (productBean != null) {
				String[] pbaaa = productBean.split("//////");
				List<ProductBean> productList = new ArrayList<ProductBean>();
				for (int a = 0; a < pbaaa.length; a++) {
					String[] pbbbb = pbaaa[a].split("//");
					ProductBean pbean = new ProductBean();
					pbean.setProductname(pbbbb[0]);
					pbean.setProducenglishtname(pbbbb[1]);
					pbean.setProductremark(pbbbb[2]);
					pbean.setProductnum(pbbbb[3]);
					pbean.setProductprice(pbbbb[4]);
					pbean.setProductcurreny(pbbbb[5]);
					productList.add(pbean);
				}
				uod.setProductBean(productList);
			}
		}

		String orderlist = request.getParameter("remark");// ????????????
		String[] ck = orderlist.split("<br/>");
		uod.setMark(ck[0]);

		// return ;
		int outState = 0;
		if (outMethod.equals("0")) { // 4PX????????????
			outState = this.fpxApiApplication(uod, orderlist, "4PX");
		} else if (outMethod.equals("1")) { // ?????????????????????
			String yuanfeihangno = request.getParameter("idyuanfeihangno");
			outState = this.yfhApiApplication(uod, orderlist, yuanfeihangno);
		} else if (outMethod.equals("5")) { // ??????????????????
			String no = request.getParameter("express_no");
			String logistics_name = request.getParameter("logistics_name");
			outState = this.outOtherApplication(uod, orderlist, no, logistics_name);
		} else if ((outMethod.equals("3"))) {
			try {
				JcgjSoapHttpPost jc = new JcgjSoapHttpPost();
				outState = Integer.parseInt(outJc(uod, jc.callGetNumber(uod.getUserzone(), "887799")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("??????????????????");
		}
		PrintWriter out = response.getWriter();
		out.print(outState);
		out.close();
	}

	// 4PX????????????
	public int fpxApiApplication(UserOrderDetails uod, String orderlist, String transportname) {
		int state = 0;
		FindOrderSample fos = new FindOrderSample();
		// CreateOrderSample cos = new CreateOrderSample();// ?????????4?????????API
		CreateAndPreAlertOrderSample cos = new CreateAndPreAlertOrderSample(); // ???????????????????????????
		RemoveOrderSample ros = new RemoveOrderSample();
		int tt = fos.findOrderService(orderlist);// ????????????????????????????????????
		int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
		if (tt == 0) {
			// System.out.println("??????????????????!");
			state = 1;
		} else if (tt > 1 || ttt != 0) { // ????????????????????????????????????????????????????????????
			// ros.removeOrderService(orderlist);//??????API
			// System.out.println("????????????????????????!");
			state = 2;
		} else {
			// ?????????????????????TrackingNumber
			// String TrackingNumber = cos.createOrderService(uod);
			String TrackingNumber = cos.createAndPreAlertOrderService(uod);// ?????????????????????
			if (TrackingNumber.equals("Failure")) {
				// System.out.println("???????????????");
				state = 3;
			} else {
				String[] ck = orderlist.split("<br/>");
				for (int i = 0; i < ck.length; i++) {
					// ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
					purchaseServer.OutPortNow(ck[i], TrackingNumber, transportname, uod.getUserid());
				}
				System.out.println("??????????????????" + TrackingNumber);
				state = 4;
			}
		}
		return state;
	}

	public int fpxApiApplication127(UserOrderDetails uod, String orderlist, String transportname) {
		int state = 0;
		FindOrderSample fos = new FindOrderSample();
		// CreateOrderSample cos = new CreateOrderSample();// ?????????4?????????API
		CreateAndPreAlertOrderSample cos = new CreateAndPreAlertOrderSample(); // ???????????????????????????
		RemoveOrderSample ros = new RemoveOrderSample();
		int tt = fos.findOrderService(orderlist);// ????????????????????????????????????
		int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
		if (tt == 0) {
			// System.out.println("??????????????????!");
			state = 1;
		} else if (tt > 1 || ttt != 0) { // ????????????????????????????????????????????????????????????
			// ros.removeOrderService(orderlist);//??????API
			// System.out.println("????????????????????????!");
			state = 2;
		} else {
			// ?????????????????????TrackingNumber
			// String TrackingNumber = cos.createOrderService(uod);
			String TrackingNumber = cos.createAndPreAlertOrderService(uod);// ?????????????????????
			if (TrackingNumber.equals("Failure")) {
				// System.out.println("???????????????");
				state = 3;
			} else {
				String[] ck = orderlist.split("<br/>");
				for (int i = 0; i < ck.length; i++) {
					// ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
					purchaseServer.OutPortNow127(ck[i], TrackingNumber, transportname, uod.getUserid());
				}
				System.out.println("??????????????????" + TrackingNumber);
				state = 4;
			}
		}
		return state;
	}

	// ?????????-????????????
	public int yfhApiApplication(UserOrderDetails uod, String orderlist, String yuanfeihangno) {
		int state = 0;
		if (yuanfeihangno == null || yuanfeihangno.equals("")) { // ??????????????????//
			state = 5;
			return state;
		} else {
			int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
			if (ttt != 0) {
				// System.out.println("???????????????????????????");
				state = 6;
			} else {
				String[] ck = orderlist.split("<br/>");
				for (int i = 0; i < ck.length; i++) {
					// ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
					purchaseServer.OutPortNow(ck[i], yuanfeihangno, "yyffhh", uod.getUserid());
				}
				state = 4;
			}
		}
		return state;
	}

	public int yfhApiApplication127(UserOrderDetails uod, String orderlist, String yuanfeihangno) {
		int state = 0;
		if (yuanfeihangno == null || yuanfeihangno.equals("")) { // ??????????????????//
			state = 5;
			return state;
		} else {
			int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
			if (ttt != 0) {
				// System.out.println("???????????????????????????");
				state = 6;
			} else {
				String[] ck = orderlist.split("<br/>");
				for (int i = 0; i < ck.length; i++) {
					// ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
					purchaseServer.OutPortNow127(ck[i], yuanfeihangno, "yyffhh", uod.getUserid());
				}
				state = 4;
			}
		}
		return state;
	}

	// ????????????????????????
	public int outOtherApplication(UserOrderDetails uod, String orderlist, String outOtherno, String logistics_name) {
		int state = 0;
		// if (outOtherno == null || outOtherno.equals("")) {
		// // state = 5; //??????????????????????????????????????? ???????????????
		// // return state;
		// } else {
		// int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
		// if (ttt != 0) {
		// // System.out.println("???????????????????????????");
		// state = 6;
		// } else {
		// String[] ck = orderlist.split("<br/>");
		// for (int i = 0; i < ck.length; i++) {
		// // ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
		// purchaseServer.OutPortNow(ck[i], outOtherno,
		// logistics_name,uod.getUserid());
		// }
		// state = 4;
		// }
		// }

		// ????????????????????????????????????????????????
		int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
		if (ttt != 0) {
			// System.out.println("???????????????????????????");
			state = 6;
		} else {
			String[] ck = orderlist.split("<br/>");
			for (int i = 0; i < ck.length; i++) {
				// ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
				purchaseServer.OutPortNow(ck[i], outOtherno, logistics_name, uod.getUserid());
			}
			state = 4;
		}
		uod = null;
		orderlist = null;
		outOtherno = null;
		logistics_name = null;
		return state;
	}

	// ????????????????????????
	public int outOtherApplication127(UserOrderDetails uod, String orderlist, String outOtherno,
                                      String logistics_name) {
		int state = 0;
		// if (outOtherno == null || outOtherno.equals("")) {
		// // state = 5; //??????????????????????????????????????? ???????????????
		// // return state;
		// } else {
		// int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
		// if (ttt != 0) {
		// // System.out.println("???????????????????????????");
		// state = 6;
		// } else {
		// String[] ck = orderlist.split("<br/>");
		// for (int i = 0; i < ck.length; i++) {
		// // ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
		// purchaseServer.OutPortNow(ck[i], outOtherno,
		// logistics_name,uod.getUserid());
		// }
		// state = 4;
		// }
		// }

		// ????????????????????????????????????????????????
		int ttt = purchaseServer.findOrderService(orderlist);// ???????????????????????????????????????
		if (ttt != 0) {
			// System.out.println("???????????????????????????");
			state = 6;
		} else {
			String[] ck = orderlist.split("<br/>");
			for (int i = 0; i < ck.length; i++) {
				// ??????orderinfo??????????????????????????????????????????????????????;?????????forwarder?????????????????????;
				purchaseServer.OutPortNow127(ck[i], outOtherno, logistics_name, uod.getUserid());
			}
			state = 4;
		}
		return state;
	}

	// ????????????
	public void getFeeByStyle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String currency = request.getParameter("currency");
		Map<String, Double> maphl = Currency.getMaphl(request);
		String fee = "";
		feeCount fc = new feeCount();
		/*
		 * orderPayDetail.setFpxFee(fc.fpxFee(addres,rs.getString(
		 * "actual_weight_estimate"),
		 * rs.getString("actual_volume"),"P","A1"));//4PX??????????????????????????????????????????????????????????????????
		 * orderPayDetail.setYfhFee(fc.yfhFee(addres,rs.getString(
		 * "actual_weight_estimate"),"P")); ???????????????????????????????????????????????????
		 */
		String id = request.getParameter("id"); // ???????????????1:4PX;2:?????????
		String country = request.getParameter("country"); // ??????
		String countrycode = request.getParameter("countrycode"); // ????????????
		String transcode = request.getParameter("trans"); // ??????????????????
		String volume = request.getParameter("volume"); // ??????
		String weight = request.getParameter("weight"); // ??????
		String jcCargoType = request.getParameter("jcCargoType");// ????????????

		// ??????
		PriceData pd = new PriceData();
		pd.setHYBM("SH0809"); // ?????? ZYW0037
		pd.setPWD("SH61504007"); // ?????? 1
		pd.setWorldCountry(countrycode); // ??????
		pd.setFasterWay(""); // ????????????
		pd.setFasterPort("0"); // ???????????? KNSHA
		pd.setPackageType(jcCargoType); // ????????????
		pd.setCweight(weight); // ????????????

		if (Integer.valueOf(id) == 1) { // 4PX??????
			ChargeCalculateSample sample = new ChargeCalculateSample();
			try {
				fee = sample.chargeCalculateService(countrycode, weight, volume, "P", transcode, currency, maphl);
			} catch (Exception e) {
				// TODO: handle exception
				fee = "00001";
				System.out.println("sample.chargeCalculateService 4px ??????:");
				// e.printStackTrace();
			} finally {

			}

		} else if (Integer.valueOf(id) == 2) { // ???????????????
			String area = request.getParameter("area");
			fee = fc.getYFHFee(area, weight, currency, maphl);
		} else if (Integer.valueOf(id) == 4) {

			fee = "#";
			String[] s = null;
			if (volume != null && !"".equals(volume)) {
				s = volume.split("\\*");
			}
			if (s != null && s.length == 3) {
				pd.setClong(s[0]); // ???
				pd.setCwidth(s[1]); // ???
				pd.setCheight(s[2]); // ???
			} else {
				pd.setClong(""); // ???
				pd.setCwidth(""); // ???
				pd.setCheight(""); // ???
			}
			// List<PriceReturnJsonNew> prjlist
			JcgjSoapHttpPost jc = new JcgjSoapHttpPost(); // gson
			List<PriceReturnJsonNew> list = jc.getJcFreight(pd);
			if (list != null) {

				fee = JSONArray.toJSONString(list.get(0).getMsg());
			} else {
				fee = "count_failure";
				// System.out.println("????????????????????????");
			}

		} else { // ????????????

			fee = "#";
			String[] s = null;
			if (volume != null && !"".equals(volume)) {
				s = volume.split("\\*");
			}
			if (s != null && s.length == 3) {
				pd.setClong(s[0]); // ???
				pd.setCwidth(s[1]); // ???
				pd.setCheight(s[2]); // ???
			} else {
				pd.setClong(""); // ???
				pd.setCwidth(""); // ???
				pd.setCheight(""); // ???
			}

			JcgjSoapHttpPost jc = new JcgjSoapHttpPost(); // gson
			List<PriceReturnJsonNew> list = jc.getJcFreight(pd);

			for (int i = 0; list != null && i < list.size(); i++) {
				for (int j = 0; j < list.get(i).getMsg().length; j++) {

					System.out.println(list.get(i).getMsg()[j].getWLMX() + "-----" + transcode);
					if (list.get(i).getMsg()[j].getWLMX().equals(transcode)) {

						float pric = Float.parseFloat(list.get(i).getMsg()[j].getJSJG());

						double exchange_rate = 1;
						DecimalFormat df = new DecimalFormat("#0.##");

						Map<String, Double> map = Currency.getMaphl(request);

						// eur CAD:0.00 GBP:0.00 AUD:0.00
						exchange_rate = maphl.get("USD");
						exchange_rate = exchange_rate / map.get("RMB");
						BigDecimal b = new BigDecimal(pric * exchange_rate);
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						fee = f1 + "#";
						break;
					}
				}
			}
			System.out.println("??????" + list.get(0).getMsg()[0].getJSJG());

		}
		PrintWriter out = response.getWriter();
		out.print(fee);
		out.close();
	}

	// ???????????????????????????
	// public int checkOrder(String order,int goodsid){ 10.31???
	public int checkOrder(String orderNo, int goodsid) {
		int rt = 0;
		String sql = "select state from orderinfo where order_no=?";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			// stmt.setString(1, order);10.31???
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				rt = rs.getInt("state");
			}
			if (rt != -1 && rt != 6) {
				// rt = this.checkGoods(order, goodsid);10.31???
				rt = this.checkGoods(orderNo, goodsid);
			} else {
				rt = 111;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rt;
	}

	// ???????????????????????????
	public int checkGoods(String order, int goodsid) {
		int rt = 0;
		String sql = "select state from order_details where orderid=? and goodsid=?";
		String sqlTwo = "select ropType from order_change where orderNo=? and goodId=? and ropType=4";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rss = null;
		PreparedStatement stmtt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, order);
			stmt.setInt(2, goodsid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				rt = rs.getInt("state");
				if (rt != 2) {
					stmtt = conn.prepareStatement(sqlTwo);
					stmtt.setString(1, order);
					stmtt.setInt(2, goodsid);
					rss = stmtt.executeQuery();
					if (rss.next()) {
						rt = rss.getInt("ropType");
						if (rt == 4)
							rt = 2;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rss != null) {
				try {
					rss.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rt;
	}

	// ????????????????????????
	public void getOriginalUrl(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderNo = request.getParameter("order_no");
		int goodid = Integer.parseInt(request.getParameter("goods_id"));
		String originalUrl = "";
		originalUrl = purchaseServer.getOriginalGoodsUrl(orderNo, goodid);
		PrintWriter out = response.getWriter();
		out.print(originalUrl);
		out.flush();
		out.close();
	}

	public void updateGoodsDistribution(Map map) {
		String sql = "update goods_distribution set iscomplete=1 where orderid=? and goodsid=?	";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, map.get("orderid") + "");
			stmt.setString(2, map.get("goodsid") + "");
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	public void getInventory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String remaining ="";
		String orderid = request.getParameter("orderNo");
		String goodsid = request.getParameter("goodsid");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT i.lock_remaining,i.is_use FROM lock_inventory i INNER JOIN order_details od ON i.od_id=od.id WHERE od.goodsid='"
				+ goodsid + "' AND od.orderid='" + orderid + "' and i.flag=0 and i.is_delete=0";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				remaining = rs.getString("lock_remaining")+"&"+rs.getString("is_use");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		PrintWriter out = response.getWriter();
		out.print(remaining);
		out.flush();
		out.close();
	}

	public void notePurchaseAgain(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String messageStr = "";
		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			messageStr = "????????????????????????????????????";
		}
		String od_idStr = request.getParameter("od_id");
		if (od_idStr == null || "".equals(od_idStr)) {
			messageStr = "?????????????????????????????????id??????";
		}
		String goodsidStr = request.getParameter("goodsid");
		if (goodsidStr == null || "".equals(goodsidStr)) {
			messageStr = "???????????????????????????id??????";
		}
		String remarkContent = request.getParameter("remarkContent");
		if (remarkContent == null || "".equals(remarkContent)) {
			messageStr = "???????????????????????????????????????";
		}
		String isPush = request.getParameter("isPush");
		if (isPush == null || "".equals(isPush)) {
			messageStr = "???????????????????????????????????????";
			isPush = "";
		}

		if ("".equals(messageStr)) {
			purchaseServer.notePurchaseAgain(orderNo, Integer.valueOf(od_idStr), Integer.valueOf(goodsidStr),
					remarkContent);

			if ("Y".equals(isPush)) {
				// ????????????????????????
				String sessionId = request.getSession().getId();
				String admuserw = Redis.hget(sessionId, "admuser");
				SerializeUtil su = new SerializeUtil();
				Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
				String orderRemark = "";
				String sendContent = "?????????," + admuser.getAdmName() + "???????????????[" + goodsidStr + "]??????????????????";
				InsertMessageNotification msgNtDao = new InsertMessageNotification();
				msgNtDao.insertByOrderComment(sendContent, remarkContent, orderNo, admuser, "?????????,");
			}
			messageStr = "ok";

		}
		PrintWriter out = response.getWriter();
		out.print(messageStr);
		out.flush();
		out.close();

	}
	/**
	 * ????????????????????? ?????????28???ali_data??????ali_info_data
	 * @Title insertDateToAliInfoData 
	 * @Description TODO
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @return void
	 */
	public void insertDateToAliInfoData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		String od_id = request.getParameter("od_id");
		int row = purchaseServer.insertDateToAliInfoData(od_id,admuser==null?"camry":admuser.getAdmName());
		PrintWriter out = response.getWriter();
		out.print(row);
		out.flush();
		out.close();
	}

}