package com.cbt.orderinfo.ctrl;

import com.alibaba.trade.param.AlibabaTradeFastCreateOrderResult;
import com.cbt.Specification.util.DateFormatUtil;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.Tb1688OrderHistory;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.util.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.PurchaseGoodsBean;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.quartz.ParseOrderFreightJob;
import com.cbt.website.userAuth.bean.Admuser;
import com.google.gson.Gson;
import com.importExpress.service.IPurchaseService;

import ceRong.tools.bean.SearchLog;
import net.minidev.json.JSONArray;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderInfoController{
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(OrderInfoController.class);
	@Autowired
	private IOrderinfoService iOrderinfoService;
	
	@Autowired
	private IPurchaseService purchaseService;

	@Autowired
	private ISpiderServer spiderService;

	@RequestMapping(value = "/changeBuyer")
	public void changeBuyer(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		String odid = request.getParameter("odid");
		String admuserid=request.getParameter("admuserid");
		PrintWriter out = response.getWriter();
		int row=0;
		map.put("admuserid",admuserid);
		map.put("odid",odid);
		try{
			row=iOrderinfoService.changeBuyer(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(row);
		out.close();
	}

	/**
	 * 订单预估国际运费接口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/flushOrderFreight")
	public void flushOrderFreight(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try{
			logger.info("开始刷新订单预估运费数据===");
			List<OrderBean> list=iOrderinfoService.getFlushOrderFreightOrder();
			flushFreight(list);
			list.clear();
			logger.info("结束刷新订单预估运费数据===");
		}catch (Exception e){
			logger.info("刷新订单运费运费错误。、。。。。。。。。。。");
		}
		//采购输入的价格数量和订单销售数量有问题的订单预警
		List<OrderBean> listFlag=iOrderinfoService.getAllOrderInfo();
		for(int i=0;i<listFlag.size();i++){
			OrderBean o=listFlag.get(i);
			String orderNo=o.getOrderNo();
			String yourorder=o.getYourorder();
			String buyAmount=o.getBuyAmount();
			String buycount=o.getBuycount();
			String esprice=o.getEsBuyPrice();
			System.out.println("orderNo==================="+orderNo);
			if(StringUtil.isBlank(buyAmount) || StringUtil.isBlank(buycount)){
				continue;
			}
			double fit=Math.abs((Double.parseDouble(esprice)-Double.parseDouble(buyAmount))/Double.parseDouble(buyAmount)*100);
			if(Integer.valueOf(buycount)>Integer.valueOf(yourorder) || fit>10){
				iOrderinfoService.deleteFlagByOrder(orderNo);
				iOrderinfoService.insertFlagByOrderid(orderNo);
			}
		}
	}

	public void flushFreight(List<OrderBean> list){
		DecimalFormat df = new DecimalFormat("######0.00");
		for(OrderBean o:list){
			logger.info("开始刷新订单【"+o.getOrderNo()+"】运费和预估采购额");
			double freightFee = iOrderinfoService.getFreightFee(o.getVolumeweight(), o);
			String freight=df.format(freightFee);
			//刷新订单的预计采购金额
			List<OrderDetailsBean> odList=iOrderinfoService.getOrdersDetails(o.getOrderNo());
			double BuyPrice=0.00;
			for(OrderDetailsBean odb:odList){
				String price1688 = Utility.getStringIsNull(odb.getCbrPrice()) ? odb.getCbrPrice() : "0";
				if("0".equals(price1688) || StringUtil.isBlank(odb.getCbrPrice())){
					price1688=odb.getCbrdPrice();
				}
				price1688=StringUtil.isBlank(price1688)?"0":price1688;
				String es_price=price1688;
				if(odb.getState() ==1 || odb.getState() == 0){
					es_price=StringUtil.getEsPrice(es_price);
				}else{
					es_price="0.00";
				}
				String ali_sellunit=odb.getAli_sellunit();
				int unit= Util.getNumberForStr(ali_sellunit);
				BuyPrice+=Double.valueOf(es_price)*odb.getYourorder()*unit;
			}
			double pid_amount=0.00;
			if(odList.size()>0){
				pid_amount=odList.get(0).getPid_amount();
			}
			//更新预估国际运费,预估采购金额
			String esBuyPrice=df.format(BuyPrice+pid_amount);
			logger.info("订单【"+o.getOrderNo()+"】运费【"+freight+"】和预估采购额：【"+esBuyPrice+"】");
			iOrderinfoService.updateFreightForOrder(o.getOrderNo(),freight,esBuyPrice);
		}
	}

	/**
	 * 月用户利润订单预警
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/flushOrderWaring")
	public void flushOrderWaring(HttpServletRequest request, HttpServletResponse response)throws Exception {
		List<OrderBean> listFlag=iOrderinfoService.getAllOrderInfo();
		for(int i=0;i<listFlag.size();i++){
			OrderBean o=listFlag.get(i);
			String orderNo=o.getOrderNo();
			String yourorder=o.getYourorder();
			String buyAmount=o.getBuyAmount();
			String buycount=o.getBuycount();
			String esprice=o.getEsBuyPrice();
			double fit=Math.abs((Double.parseDouble(esprice)-Double.parseDouble(buyAmount))/Double.parseDouble(buyAmount)*100);
			if(Integer.valueOf(buycount)>Integer.valueOf(yourorder) || fit>10){
				iOrderinfoService.insertFlagByOrderid(orderNo);
			}
		}
	}

	/**
	 * 入库时获取淘宝信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getGoodsData")
	public void getGoodsData(HttpServletRequest request, HttpServletResponse response)throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String expresstrackid = request.getParameter("expresstrackid");
		PrintWriter out = response.getWriter();
		List<Tb1688OrderHistory> order=new ArrayList<Tb1688OrderHistory>();
		try {
			//记录扫描快递包裹日志记录scan_parcel_log_info
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			iOrderinfoService.insertScanLog(expresstrackid, adm.getAdmName());
			order = iOrderinfoService.getGoodsData(expresstrackid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		String json = gson.toJson(order);
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * 获取需要入库验货的商品信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getResultInfo")
	public void getResultInfo(HttpServletRequest request, HttpServletResponse response)throws Exception {
		request.setCharacterEncoding("utf-8");

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		List<SearchResultInfo> list=new ArrayList<SearchResultInfo>();
		PrintWriter out = response.getWriter();
		try{
			String expresstrackid = request.getParameter("expresstrackid");
			String checked = request.getParameter("checked");
			list = iOrderinfoService.getOrder(expresstrackid, checked);
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(net.minidev.json.JSONArray.toJSONString(list));
		out.flush();
		out.close();
	}

	/**
	 * 用户管理页面更新销售
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/userUpdateAdmuser")
	public void userUpdateAdmuser(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		String adminid = request.getParameter("adminid");
		String userid=request.getParameter("userid");
		String  email = request.getParameter("email");
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
		String users = user.getAdmName();
		String username=request.getParameter("userName");
		String admName=request.getParameter("admName");
		String orderNo=request.getParameter("orderNo");
		PrintWriter out = response.getWriter();
		int row=0;
		//用户id
		map.put("userid",userid);
		//销售人id
		map.put("adminid",adminid);
		map.put("users",users);
		//客户邮箱
		map.put("email",email);
		//销售人名称
		map.put("admName",admName);
		//订单号
		map.put("orderNo",orderNo);
		try{
			row=iOrderinfoService.addUser(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(row);
		out.close();
	}

	/**
	 * 取消入库
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/updatecanceltatus")
	public void updatecanceltatus(HttpServletRequest request, HttpServletResponse response)throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Map<String,String> map=new HashMap<String,String>();
		int res=0;
		try{
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			map.put("orderid", request.getParameter("orderid"));
			map.put("goodid", request.getParameter("goodid"));
			map.put("odid", request.getParameter("odid"));
			map.put("repState", request.getParameter("repState"));
			map.put("warehouseRemark", request.getParameter("warehouseRemark"));
			map.put("count", request.getParameter("count"));
			map.put("adminId", String.valueOf(adm.getId()));
			res = iOrderinfoService.updatecanceltatus(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	/**
	 * 验货无误
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateCheckStatus")
	public void updateCheckStatus(HttpServletRequest request, HttpServletResponse response)throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Map<String,String> map=new HashMap<String,String>();
		int res=0,num=0;
		try{
			map.put("orderid", request.getParameter("orderid"));
			map.put("goodid", request.getParameter("goodid"));
			map.put("status", request.getParameter("status"));
			map.put("goodurl", request.getParameter("goodurl"));
			map.put("barcode", request.getParameter("barcode"));
			map.put("userid", request.getParameter("userid"));
			map.put("userName", request.getParameter("userName"));
			map.put("tbOrderId", request.getParameter("tbOrderId"));
			map.put("shipno", request.getParameter("shipno"));
			map.put("itemid", request.getParameter("itemid"));
			map.put("repState", request.getParameter("repState"));
			map.put("odid",request.getParameter("odid"));
			map.put("warehouseRemark", request.getParameter("warehouseRemark"));
			int count = Integer.valueOf(request.getParameter("count"));
			map.put("count", String.valueOf(count));
			String weight = "0.00";
			weight = StringUtil.isBlank(weight) ? "0.00" : weight;
			map.put("weight", String.valueOf(weight));
			res = iOrderinfoService.updateTbstatus(map);
			if (res > 0) {
				//验货无误成功，判断该订单是否全部到库并且验货无误
				num = iOrderinfoService.checkOrderState(map.get("orderid"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(res + "," + num);
		out.flush();
		out.close();
	}

	/**
	 * 取消验货
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/updatecancelChecktatus")
	public void updatecancelChecktatus(HttpServletRequest request, HttpServletResponse response)throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Map<String,String> map=new HashMap<String,String>();
		int res=0;
		try{
			map.put("orderid",request.getParameter("orderid"));
			map.put("goodid",request.getParameter("goodid"));
			map.put("warehouseRemark",request.getParameter("warehouseRemark"));
			map.put("repState",request.getParameter("repState"));
			map.put("count",request.getParameter("count"));
			map.put("odid",request.getParameter("odid"));
			//取消验货数量
			map.put("barcode",request.getParameter("barcode"));
			map.put("seiUnit",request.getParameter("seiUnit"));
			String cance_inventory_count = request.getParameter("cance_inventory_count");
			if (cance_inventory_count == null || "".equals(cance_inventory_count)) {
				cance_inventory_count = "0";
			}
			map.put("cance_inventory_count",cance_inventory_count);
			String weight = request.getParameter("weight");
			weight = StringUtil.isBlank(weight) ? "0.00" : weight;
			map.put("weight",request.getParameter("weight"));
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			map.put("adminId",String.valueOf(adm.getId()));
			res = iOrderinfoService.updatecancelChecktatus(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	/**
	 * 商品到库操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateGoodStatus")
	public void updateGoodStatus(HttpServletRequest request, HttpServletResponse response)throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String,String> map=new HashMap<String,String>();
		int res=0;
		response.setContentType("text/html;charset=utf-8");
		String userName = request.getParameter("userName");
		String warehouseRemark = request.getParameter("warehouseRemark");
		if (warehouseRemark == null || "".equals(warehouseRemark)) {
			warehouseRemark = "";
		} else {
			warehouseRemark = userName + ":" + warehouseRemark + "(" + DateFormatUtil.getWithMinutes(new Date()) + ");";
		}
		try{
			map.put("orderid", request.getParameter("orderid"));
			map.put("goodid", request.getParameter("goodid"));
			map.put("status", request.getParameter("status"));
			map.put("goodurl", request.getParameter("goodurl"));
			map.put("odid",request.getParameter("odid"));
			map.put("barcode", request.getParameter("barcode"));
			map.put("userid", request.getParameter("userid"));
			map.put("userName", userName);
			map.put("tbOrderId", request.getParameter("tbOrderId"));
			map.put("shipno", request.getParameter("shipno"));
			map.put("itemid", request.getParameter("itemid"));
			map.put("repState", request.getParameter("repState"));
			map.put("warehouseRemark", warehouseRemark);
			// 验货有误时的验货数量
			int count = Integer.valueOf(request.getParameter("count"));
			map.put("count",String.valueOf(count));
			res = iOrderinfoService.updateGoodStatus(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

	/**
	 * 一键确认或取消入库
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/allTrack")
	public void allTrack(HttpServletRequest request, HttpServletResponse response)throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String,String> map=new HashMap<String,String>();
		try{
			String shipno = request.getParameter("shipno");
			String type = request.getParameter("type");
			String barcode = request.getParameter("barcode");
			String userid = request.getParameter("userid");
			String userName = request.getParameter("userName");
			String tbOrderId = request.getParameter("tbOrderId");
			map.put("shipno",shipno);
			map.put("barcode",barcode);
			map.put("userid",userid);
			map.put("userName",userName);
			map.put("tbOrderId",tbOrderId);
			map.put("warehouseRemark", "");
			map.put("status", "1");
			map.put("repState", "1");
			if("1".equals(type)){
				List<Map<String,String>> allList=iOrderinfoService.allTrack(map);
				for(Map<String,String> allMap:allList){
					String orderid = String.valueOf(allMap.get("orderid"));
					String goodid =String.valueOf(allMap.get("goodsid"));
					String goodurl = String.valueOf(allMap.get("goods_url"));
					String odid = String.valueOf(allMap.get("odid"));
					map.put("odid",odid);
					map.put("goodurl",goodurl);
					map.put("goodid", goodid);
					map.put("orderid", orderid);
					map.put("count","0");
					map.put("itemid", String.valueOf(allMap.get("tb_1688_itemid")));
					iOrderinfoService.updateGoodStatus(map);
				}
			}else if("0".equals(type)){
				List<OrderDetailsBean> oList=iOrderinfoService.getAllCancelDetails(map);
				for(OrderDetailsBean o:oList){
					String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
					Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
					map.put("orderid", o.getOrderid());
					map.put("goodid", String.valueOf(o.getGoodsid()));
					map.put("odid", String.valueOf(o.getId()));
					map.put("repState", request.getParameter("repState"));
					map.put("warehouseRemark", request.getParameter("warehouseRemark"));
					map.put("count", request.getParameter("count"));
					map.put("adminId", String.valueOf(adm.getId()));
					iOrderinfoService.updatecanceltatus(map);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(1);
		out.flush();
		out.close();
	}

	/**
	 * 1688采购订单建议退货管理记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderReturn")
	public void orderReturn(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		PrintWriter out = response.getWriter();
		int row=0;
		String id = request.getParameter("id");
		String orderid = request.getParameter("orderid");
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
		if(user == null){
			out.print(row);
			out.close();
		}
		String users = user.getAdmName();
		try{
			map.put("id",id);
			map.put("orderid",orderid);
			map.put("admName",users);
			row=iOrderinfoService.orderReturn(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(row);
		out.close();
	}

	/**
	 * 更新订单的销售采购信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/addUser")
	public void addUser(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		String adminid = request.getParameter("adminid");
		String userid=request.getParameter("userid");
		String  email = request.getParameter("email");
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
		String users = user.getAdmName();
		String username=request.getParameter("userName");
		String admName=request.getParameter("admName");
		String orderNo=request.getParameter("orderNo");
		PrintWriter out = response.getWriter();
		int row=0;
		//用户id
		map.put("userid",userid);
		//销售人id
		map.put("adminid",adminid);
		map.put("users",users);
		//客户邮箱
		map.put("email",email);
		//销售人名称
		map.put("admName",admName);
		//订单号
		map.put("orderNo",orderNo);
		try{
			row=iOrderinfoService.addUser(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(row);
		out.close();
	}


	
	@RequestMapping(value = "/getOrderInfo.do", method = RequestMethod.GET)
	public String getOrderInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException, IOException {
		Map<String,String> paramMap=new HashMap<String,String>();
		String ropType = request.getParameter("ropType");
		String userID_req = request.getParameter("userid");
		String state_req = request.getParameter("state");
		String startdate_req = request.getParameter("startdate");
		String enddate_req = request.getParameter("enddate");
		int showUnpaid = Integer.parseInt(request.getParameter("showUnpaid"));
		String orderno = request.getParameter("orderno");
		String email = request.getParameter("email");
		String paymentid=request.getParameter("paymentid");
		paymentid=StringUtil.isBlank(paymentid)?"":paymentid;
		int page = Utility.getStringIsNull(request.getParameter("page"))?Integer.parseInt(request.getParameter("page")):1;
		int currentPage = Utility.getStringIsNull(request.getParameter("currentPage"))?Integer.parseInt(request.getParameter("currentPage")):1;
		String buyid = request.getParameter("buyuser");
		String admuserid_str = request.getParameter("admuserid");
		admuserid_str=StringUtil.isBlank(admuserid_str)?"0":admuserid_str;
		request.setAttribute("admuserid_str",admuserid_str);
		String type = request.getParameter("type");
		request.setAttribute("type",StringUtil.isNotBlank(type)?type:"");
		String status = request.getParameter("status");
		int status_ = Utility.getStringIsNull(status) ? Integer.parseInt(status) : 0;
		int buyuser=StringUtil.isNotBlank(buyid)?Integer.parseInt(buyid):0;
		page=page>0?(page - 1) * 40:0;
		userID_req = userID_req!=null&& !userID_req.equals("") ?userID_req.replaceAll("\\D+", ""):"0";
		int userID = userID_req !=null && !userID_req.equals("") ? Integer.parseInt(userID_req) : 0;
		int state = Utility.getStringIsNull(state_req) ? Integer.parseInt(state_req) : -2;
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		if(admJson == null){
			return "main_login";
		}
		Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
		String strm=user.getRoletype(); int admuserid=user.getId();
		if("0".equals(strm)){
			admuserid = Utility.getStringIsNull(admuserid_str) ? Integer.parseInt(admuserid_str) : 0;
		}
		Date startdate =null;
		Date enddate = null;
		if(startdate_req!=null&&startdate_req.isEmpty()){
			startdate = null;
		}
		if(enddate_req!=null&&enddate_req.isEmpty()){
			enddate = null;
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(startdate_req!=null && !startdate_req.equals("")){
			startdate_req=startdate_req + " 00:00:00";
		}else{
			startdate_req="0";
		}
		if(enddate_req!=null && !enddate_req.equals("") )
		{
			enddate_req=enddate_req + " 23:59:59";
		}else{
			enddate_req="0";
		}
		try {
			if(StringUtil.isNotBlank(type) && "order_pending".equals(type)){
				list=iOrderinfoService.getorderPending();
			}else{
				list=iOrderinfoService.getOrderManagementQuery(userID, state, startdate_req, enddate_req, email, orderno, page, currentPage, admuserid, buyuser, showUnpaid, type,status_,paymentid);
				for(int i=0;i<list.size();i++){
					String orderid=list.get(i).get("order_no");
					String problem=iOrderinfoService.getProblem(orderid);
					list.get(i).put("problem", problem);
				}
			}
		int count = getCount(showUnpaid, orderno, email, type, status_, buyuser, userID, state, admuserid, startdate_req, enddate_req, list);
		//获取订单号
		StringBuilder sb = new StringBuilder();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				sb.append("'"+list.get(i).get("order_no")+"',");
			}
			request.setAttribute("ordernolist", sb.toString().substring(0,sb.length()-1));
		}
		request.setAttribute("orderws", net.sf.json.JSONArray.fromObject(list));
		UserDao dao=new UserDaoImpl();
		List<ConfirmUserInfo> listAdm = getConfirmUserInfos(request, dao);
		page = Utility.getStringIsNull(request.getParameter("page"))?Integer.parseInt(request.getParameter("page")):1;
		//获取纯销售和采销一体账户信息
		List<ConfirmUserInfo> sellAdm =iOrderinfoService.getAllSalesAndBuyer();
		List<ConfirmUserInfo> purchaseAdm =  new ArrayList<ConfirmUserInfo>();
		purchaseAdm = dao.getAllByRoleType(2);
		request.setAttribute("listAdm", JSONArray.toJSONString(listAdm));
		request.setAttribute("sellAdm", JSONArray.toJSONString(sellAdm));
		request.setAttribute("purchaseAdm", JSONArray.toJSONString(purchaseAdm));
		request.setAttribute("count", count);
		request.setAttribute("page", page);
		request.setAttribute("admuserid", admuserid);
		request.setAttribute("buyuser", buyuser);
		request.setAttribute("strname", admuserid);
		request.setAttribute("admName", user.getAdmName());
		request.setAttribute("roletype", user.getRoletype());
		request.setAttribute("strm", strm);
		request.setAttribute("showUnpaid", showUnpaid);
		request.setAttribute("email", email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "nordermgr";
	}

	private int getCount(int showUnpaid, String orderno, String email, String type, int status_, int buyuser, int userID, int state, int admuserid, String startdate, String enddate, List<Map<String, String>> list) {
		int count = 0;
		if(list!=null&&!list.isEmpty() && !"order_pending".equals(type)){
			startdate="0".equals(startdate)?null:startdate;
			enddate="0".equals(enddate)?null:enddate;
			count = iOrderinfoService.getOrdersCount(userID, state, startdate, enddate, email, orderno, admuserid, buyuser, showUnpaid, type,status_ );
		}else{
			count=list.size();
		}
		return count;
	}

	private List<ConfirmUserInfo> getConfirmUserInfos(HttpServletRequest request, UserDao dao) {
		List<ConfirmUserInfo> listAdm;
		String action=request.getParameter("action");
		if ("currentUser".equals(action)) {
			String sessionId = request.getSession().getId();
			String admuserJson = Redis.hget(sessionId, "admuser");
			Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			listAdm = dao.getCurSub(admuser);
		}else{
			listAdm = dao.getAllByOperations();
		}
		return listAdm;
	}

	@RequestMapping(value = "/getOrderStates")
	@ResponseBody
	public net.sf.json.JSONArray getOrderStates(HttpServletRequest request, Model model) throws ParseException {
		//订单列表的统计
		try {
			String admuserid_str = request.getParameter("admuserid");
			String admJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
			String strm=user.getRoletype(); int admuserid=user.getId();
			if("0".equals(strm)){
				//临时添加Sales1查看所有订单列表的统计
				admuserid = Utility.getStringIsNull(admuserid_str) ? Integer.parseInt(admuserid_str) : 0;
			}
			List<Map<String, Integer>> maps =  iOrderinfoService.getOrdersState(admuserid);
			return net.sf.json.JSONArray.fromObject(maps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title getComfirmedSourceGoods 
	 * @Description 获取可以自动1688下单的商品列表
	 * @param request
	 * @param model
	 * @return
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/getComfirmedSourceGoods")
	public @ResponseBody
    Map<String,Object> getComfirmedSourceGoods(HttpServletRequest request, Model model){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> listMap = purchaseService.getComfirmedSourceGoods();
		List<String> shopidList = new ArrayList<String>(5);
		List<PurchaseGoodsBean> beanList = new ArrayList<PurchaseGoodsBean>();
		if(listMap!=null) {
			for(Map<String,Object> goods_map :listMap) {
				PurchaseGoodsBean bean = new PurchaseGoodsBean();
				bean.setPid(String.valueOf(goods_map.get("goods_pid")));
				bean.setNumber(Integer.parseInt(String.valueOf(goods_map.get("yourorder"))));
				bean.setShopid(String.valueOf(goods_map.get("shop_id")));
				bean.setId(Integer.parseInt(String.valueOf(goods_map.get("id"))));
				bean.setCar_img(String.valueOf(goods_map.get("car_img")));
				bean.setCar_type(String.valueOf(goods_map.get("car_type")));
				bean.setGoodsname(String.valueOf(goods_map.get("goodsname")));
				bean.setGoodsprice(String.valueOf(goods_map.get("goodsprice")));
				bean.setAdminid(String.valueOf(goods_map.get("adminid")));
				bean.setOrderid(String.valueOf(goods_map.get("orderid")));
				bean.setSampling_flag((Integer)goods_map.get("sampling_flag"));
				beanList.add(bean);
				if(!shopidList.contains(bean.getShopid())) {
					shopidList.add(bean.getShopid());
				}
			}
		}
		
		map.put("beanList", beanList);
		map.put("shopidList", shopidList);
		return map;
	}
	
	@RequestMapping(value = "/generate1688Orders")
	public @ResponseBody
    List<Map<String,Object>> generate1688Orders(HttpServletRequest request, Model model){
		List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>();
		String app_key = "7031967";//1688账号的APP_KEY
		String sec_key = "z2tB0cavGIL";//1688账号的APP_KEY密码
		String access_taken = (String) request.getSession().getAttribute("1688_access_taken");
		if(access_taken == null) {
			List<String> result_list = purchaseService.sendPost("https://gw.open.1688.com/openapi/param2/1/system.oauth2/getToken/"+app_key, 
					"grant_type=refresh_token&client_id="+app_key+"&client_secret="+sec_key+"&refresh_token=419e8236-9c1c-4328-b0c2-3d5fc66814d5");
			if(result_list!=null) {
				for(int i=0;i<result_list.size();i++) {
					String str = result_list.get(i);
					if(str.indexOf("access_token")>-1) {
						String[] strArr = str.replace("{", "").replace("}", "").split(",");
						for(String sss:strArr) {
							if(sss.indexOf("access_token")>-1) {
								access_taken = sss.replace("\"", "").split(":")[1];
								break;
							}
						}
					}
				}
			}
			if(org.apache.commons.lang.StringUtils.isNotBlank(access_taken)) {
				request.getSession().setAttribute("1688_access_taken",access_taken);
			}
		}
		String shop_id = request.getParameter("shopid");
		
		//需要下单的 商品信息
		List<Map<String,Object>> listMap  = purchaseService.getComfirmedSourceGoods();
		
		List<String> shopidList = new ArrayList<String>(5);
		List<PurchaseGoodsBean> beanList = new ArrayList<PurchaseGoodsBean>();
		if(listMap!=null) {
			for(Map<String,Object> goods_map :listMap) {
				PurchaseGoodsBean bean = new PurchaseGoodsBean();
				bean.setPid(String.valueOf(goods_map.get("goods_pid")));
				bean.setNumber(Integer.parseInt(String.valueOf(goods_map.get("yourorder"))));
				bean.setShopid(String.valueOf(goods_map.get("shop_id")));
				bean.setId(Integer.parseInt(String.valueOf(goods_map.get("id"))));
				bean.setCar_img(String.valueOf(goods_map.get("car_img")));
				bean.setCar_type(String.valueOf(goods_map.get("car_type")));
				bean.setGoodsname(String.valueOf(goods_map.get("goodsname")));
				bean.setGoodsprice(String.valueOf(goods_map.get("goodsprice")));
				bean.setSpecId(String.valueOf(goods_map.get("actual_volume")));
				bean.setSampling_flag((Integer)goods_map.get("sampling_flag"));
				beanList.add(bean);
				if(!shopidList.contains(bean.getShopid())) {
					shopidList.add(bean.getShopid());
				}
			}
		}
		for(String shopid:shopidList) {
			if(shop_id!=null&&!shopid.equals(shop_id)) {
				continue;
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("shopid", shopid);
			
			List<PurchaseGoodsBean> thisShopGoods = new ArrayList<PurchaseGoodsBean>();
			List<Integer> idsList = new ArrayList<Integer>();
			for(PurchaseGoodsBean pgbBean:beanList) {
				//排除已下过单的
				if(pgbBean.getSampling_flag()!=2&&shopid.equals(pgbBean.getShopid())) {
					thisShopGoods.add(pgbBean);
					idsList.add(pgbBean.getId());
				}
			}
			if(thisShopGoods.size()>0) {
				try {
					//调取1688API,自动下单
					AlibabaTradeFastCreateOrderResult result = purchaseService.generateOrdersByShopId(app_key,sec_key,access_taken,thisShopGoods);
					if (result.getSuccess()) {
					    String orderId = result.getResult().getOrderId();
					    Long orderAmount = result.getResult().getTotalSuccessAmount();
					    System.out.println("店铺"+shopid+"下单成功，订单号："+orderId);
					    map.put("success",true);
					    map.put("orderId", orderId);
					    map.put("orderAmount", orderAmount);
					    map.put("message", "店铺"+shopid+"下单成功，订单号："+orderId+"，订单金额："+orderAmount);
					    
					    //自动下单成功，更新标识
					    purchaseService.updateAutoOrderFlag(idsList);
					}else {
						String message = result.getMessage();//错误信息
						map.put("success",false);
					    map.put("message", "店铺"+shopid+"下单失败，原因："+message);
					}
				} catch (Exception e) {
					e.printStackTrace();
					map.put("success",false);
				    map.put("message", "店铺"+shopid+"下单失败，原因："+e.getMessage());
				}
			}else {
				map.put("success",false);
			    map.put("message", "店铺"+shopid+"下单失败，原因：没有还 未下单的，且已确认货源的商品！");
			}
			list_map.add(map);
		}
		return list_map;
	}
	
	/**
     * 
     * @Title searchProductLog 
     * @Description 搜索页面记录用户的搜索结果和点击商品
     * @param request
     * @return
     * @return Map<String,Object>
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    @RequestMapping("/searchProductLog")
    public @ResponseBody Map<String,Object> searchProductLog(HttpServletRequest request,HttpServletResponse response) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        String saveFlag = request.getParameter("saveFlag");
        Map<String,Object> map = new HashMap<String,Object>();
        String userinfo = request.getParameter("userid");
        int userid = userinfo==null?0:Integer.parseInt(userinfo);
        if("0".equals(saveFlag)||"1".equals(saveFlag)){//搜索日志记录和页面产品展示数量更新
            String keyWords = request.getParameter("keyWords")==null?"":request.getParameter("keyWords");
            if(StringUtil.isNotBlank(keyWords) && keyWords.length()>200){
                keyWords = keyWords.substring(0, 198);
            }
            String catid = request.getParameter("catid");
            String sortType = request.getParameter("sortType");
            String pageNumber = request.getParameter("pageNumber")==null?"":request.getParameter("pageNumber");
            if(StringUtil.isNotBlank(pageNumber) && pageNumber.length()>10){
                pageNumber = pageNumber.substring(0, 8);
            }
            String productShowIdList = request.getParameter("productShowIdList");
            String allProductList = request.getParameter("allProductList");
            int listSize = Integer.parseInt(request.getParameter("listSize")==null?"0":request.getParameter("listSize"));
            String rowid = request.getParameter("rowid");
            SearchLog seaLog = new SearchLog();
            seaLog.setKeyWords(keyWords);
            seaLog.setCatid(catid);
            seaLog.setPageNumber(pageNumber);
            seaLog.setSortType(sortType);
            seaLog.setProductShowIdList(productShowIdList);
            seaLog.setSaveFlag(saveFlag);
            seaLog.setId(Integer.parseInt(rowid==null?"0":rowid));
            seaLog.setUserid(userid);
            seaLog.setAllProductList(allProductList);
            seaLog.setListSize(listSize);
            //如果是移动端
            if("1".equals(request.getParameter("device"))){
                seaLog.setDevice(1);
            }else{
                seaLog.setDevice(0);
            }
            String sessionid = request.getSession(true).getId();
            seaLog.setSessionid(sessionid);
            String ip = "";
            Calendar cal = Calendar.getInstance();
            int year = Calendar.YEAR;
            int month = Calendar.MONTH;
            int date = Calendar.DATE;
            seaLog.setYear(cal.get(year));
            seaLog.setMonth(cal.get(year)+"-"+(cal.get(month)+1));
            seaLog.setDay(cal.get(year)+"-"+(cal.get(month)+1)+"-"+cal.get(date));
            seaLog.setSearchMD5(Md5Util.md5Operation(seaLog.getKeyWords()+seaLog.getCatid()+seaLog.getSortType()+seaLog.getPageNumber()));
            if(userid!=0){
                seaLog.setSearchUserMD5(Md5Util.md5Operation(seaLog.getKeyWords()+seaLog.getCatid()+seaLog.getSortType()+seaLog.getPageNumber()+userid));
            }else{
                seaLog.setSearchUserMD5(Md5Util.md5Operation(seaLog.getKeyWords()+seaLog.getCatid()+seaLog.getSortType()+seaLog.getPageNumber()+sessionid));
            }
            int rowId = spiderService.saveTheSearchLogOnSearchPage(seaLog);
            map.put("rowid", rowId);
            map.put("searchMD5", seaLog.getSearchMD5());
            map.put("searchUserMD5", seaLog.getSearchUserMD5());
            return map;
        }else{
            return map;
        }
    }
	
    @RequestMapping("/searchClickProductLog")
    @ResponseBody
    public void markTheProduct(HttpServletRequest request) {
        String searchMD5 = request.getParameter("searchMD5");
        String searchUserMD5 = request.getParameter("searchUserMD5");
        String goodsPid = request.getParameter("goodsPid");
        spiderService.saveTheClickCountOnSearchPage(goodsPid,searchMD5,searchUserMD5);
    }
}
