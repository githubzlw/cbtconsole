package com.cbt.controller;

import com.cbt.auto.ctrl.OrderAutoServlet;
import com.cbt.bean.*;
import com.cbt.bean.OrderBean;
import com.cbt.common.StringUtils;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.util.*;
import com.cbt.warehouse.pojo.*;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.PrePurchasePojo;
import com.cbt.website.bean.PurchasesBean;
import com.cbt.website.dao2.Page;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.service.IPurchaseService;
import com.importExpress.utli.MultiSiteUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/purchase")
public class PurchaseController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PurchaseController.class);
	IOrderwsServer server1 = new OrderwsServer();
	private PurchaseServer purchaseServer = new PurchaseServerImpl();
	@Autowired
	private IPurchaseService iPurchaseService;
	@Autowired
	private IOrderinfoService iOrderinfoService;
	@Autowired
	private IWarehouseService iWarehouseService;
	@RequestMapping(value = "/determineStraighthair")
	@ResponseBody
	protected void determineStraighthair(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Map<Object,Object> map = new HashMap<Object,Object>();
		response.setCharacterEncoding("utf-8");
		String orderid = request.getParameter("orderid");
		String goodsid=request.getParameter("goodsid");
		String odid=request.getParameter("odid");
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("odid", odid);
		int row=purchaseServer.determineStraighthair(map);
		PrintWriter out = response.getWriter();
		out.print(row);
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/getDetailsChangeInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDetailsChangeInfo(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");

		Map<String, String> map = new HashMap<String, String>();
		map.put("goodsid", goodsid);
		map.put("orderid", orderid);
		List<ChangeGoodsLogPojo> list = iPurchaseService.getDetailsChangeInfo(map);
		return JSONArray.fromObject(list).toString();
	}

	// 采购补货
	@RequestMapping(value = "/insertOrderReplenishment.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertOrderReplenishment(HttpServletRequest request,
	                                       Model model) {
		String admJson = Redis.hget(request.getSession().getId(), "admuser");// 获取登录用户
		com.cbt.pojo.Admuser user = (com.cbt.pojo.Admuser) SerializeUtil
				.JsonToObj(admJson, com.cbt.pojo.Admuser.class);
		String userid = request.getParameter("userid");
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		String goods_url = request.getParameter("goods_url");
		String goods_p_url = request.getParameter("goods_p_url");
		String goods_price = request.getParameter("goods_price");
		String buycount = request.getParameter("buycount");
		String remark = request.getParameter("remark");
		String rep_type = request.getParameter("rep_type");
		String goods_title = request.getParameter("goods_title");
		String shop_id=request.getParameter("shop_id");
		String odid=request.getParameter("odid");
		if (goods_p_url == null || "".equals(goods_p_url)) {
			return "0";
		}
		String tb_1688_itemid = Util.getItemid(goods_p_url);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("goods_url", goods_url);
		map.put("goods_p_url", goods_p_url);
		map.put("goods_price", goods_price);
		map.put("buycount", buycount);
		map.put("remark", remark);
		map.put("tb_1688_itemid", tb_1688_itemid);
		map.put("rep_type", rep_type);
		map.put("goods_title", goods_title);
		map.put("goods_type", "0");
		map.put("shop_id", shop_id);
		map.put("odid",odid);
		int ret = iPurchaseService.insertOrderReplenishment(map);
		if (ret > 0) {
			iPurchaseService.updateReplenishmentState(map);
			// 添加补货记录
			map.put("admuserid", user.getId());
			iPurchaseService.addReplenishmentRecord(map);
		}
		return "" + ret;
	}

	// 采购补货
	@RequestMapping(value = "/getIsReplenishment.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getIsReplenishment(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("goods_type", "0");
		List<Replenishment_RecordPojo> list = iPurchaseService.getIsReplenishments(map);
		return JSONArray.fromObject(list).toString();
	}

	// 线下采购记录
	@RequestMapping(value = "/getIsOfflinepurchase.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getIsOfflinepurchase(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		List<OfflinePurchaseRecordsPojo> list = iPurchaseService.getIsOfflinepurchase(map);
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 全部取消货源
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allQxQrNew")
	@ResponseBody
	protected void allQxQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=iPurchaseService.allQxQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	/**
	 * 采购是否使用库存
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/useInventory")
	@ResponseBody
	protected void useInventory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Map<String, String> map = new HashMap<String, String>();
		int row=0;
		String od_id=request.getParameter("od_id");
		String isUse=request.getParameter("isUse");
		map.put("od_id", od_id);
		map.put("isUse", isUse);
		row = iPurchaseService.useInventory(map);
		PrintWriter out = response.getWriter();
		out.print(row + "");
		out.close();
	}

	/**
	 * 获取其他货源
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getOtherSources")
	@ResponseBody
	protected void getOtherSources(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		response.setCharacterEncoding("UTF-8");
		String orderNo = request.getParameter("orderNo");
		String odid = request.getParameter("odid");
		String goods_url = request.getParameter("goods_url");
		String otherUrl = "";
		int st = iPurchaseService.checkOrder(orderNo, odid);
		if (st == 111) { // 订单被取消
			otherUrl = "cancel";
		} else if (st == 2) { // 商品被取消
			otherUrl = "cancel";
		} else {
			otherUrl = iPurchaseService.getOtherSources(orderNo, odid, goods_url);
		}
		PrintWriter out = response.getWriter();
		out.print(otherUrl);
		out.flush();
		out.close();
	}

	/**
	 * 原链接订单信息录入
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/insertSources")
	@ResponseBody
	protected void insertSources(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Map<String, String> map = new HashMap<String, String>();
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserw, Admuser.class);
		int adminid = admuser.getId();
		String shipno = request.getParameter("shipno");
		String odid = request.getParameter("odid");
		String taobaoPrice = request.getParameter("taobaoPrice");
		taobaoPrice=StringUtil.isBlank(taobaoPrice)?"0":taobaoPrice;
		String taobaoFeight = request.getParameter("taobaoFeight");
		taobaoFeight=StringUtil.isBlank(taobaoFeight)?"0":taobaoFeight;
		String delivary_date = request.getParameter("delivary_date");
		String goodsQty = request.getParameter("goodsQty");
		goodsQty=StringUtil.isBlank(goodsQty)?"0":goodsQty;
		String username = getInsertSourceString(request, map, adminid, shipno, taobaoPrice, taobaoFeight, delivary_date, goodsQty,odid);
		double prices = Integer.parseInt(goodsQty) * Double.parseDouble(taobaoPrice) + Double.parseDouble(taobaoFeight);
		map.put("totalprice", String.valueOf(prices));
		int row=0;
		if(StringUtil.isNotBlank(username)){
			row = iPurchaseService.insertSources(map);
		}
		PrintWriter out = response.getWriter();
		out.print(row + "");
		out.flush();
		out.close();
	}

	private String getInsertSourceString(HttpServletRequest request, Map<String, String> map, int adminid, String shipno, String taobaoPrice, String taobaoFeight, String delivary_date, String goodsQty, String odid) {
		String taobao_url = request.getParameter("taobao_url");
		String goods_sku = request.getParameter("goods_sku");
		String taobao_name = request.getParameter("taobao_name");
		String preferential = request.getParameter("preferential");
		String paydate = request.getParameter("paydate");
		String goods_imgs = request.getParameter("goods_imgs");
		String admName =request.getParameter("admName");
		String TbOrderid = request.getParameter("TbOrderid");
		String TbGoodsid = request.getParameter("TbGoodsid");
		String username = iWarehouseService.getBuyerNames(String.valueOf(adminid));
		map.put("username", username);
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
		map.put("odid",odid);
		return username;
	}

	/**
	 * 一键取消采购
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allQxcgQrNew")
	@ResponseBody
	protected void allQxcgQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=iPurchaseService.allQxcgQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	/**
	 *
	 * @Title getPrePurchase
	 * @Description 获取采购管理页面订单信息
	 * @param request 客户端请求
	 * @param model
	 * @return easy ui结果集
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/getPrePurchase", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getPrePurchase(HttpServletRequest request, Model model) throws ParseException {
		DataSourceSelector.restore();
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		int page = Integer.parseInt(request.getParameter("page"));
		String orderid=request.getParameter("orderid");
		String userid=request.getParameter("userid");
		String days=request.getParameter("days");
		String goodsid=request.getParameter("goodsid");
		String state=request.getParameter("state");
		String admuserid=request.getParameter("admuserid");
		String goods_name=request.getParameter("goods_name");
		String goods_pid=request.getParameter("goods_pid");
		String type=request.getParameter("type");
		if (page > 0) {
			page = (page - 1) * 40;
		}
		List<PrePurchasePojo> list=new ArrayList<PrePurchasePojo>();
		List<String> counts=new ArrayList<String>();
		map.put("admuserid",admuserid);
		map.put("page", page);
		if("000000000".equals(orderid)){
			orderid="'000000000'";
		}
		map.put("orderid", orderid==null || "".equals(orderid) || "''".equals(orderid)?null:orderid);
		map.put("userid", userid==null || "".equals(userid)?null:userid);
		map.put("days", days==null || "".equals(days)?null:days);
		map.put("goodsid", goodsid==null || "".equals(goodsid)?null:goodsid);
		map.put("state", "-1".equals(state)?null:state);
		map.put("goods_name", goods_name==null || "".equals(goods_name)?null:goods_name);
		map.put("goods_pid",StringUtil.isNotBlank(goods_pid)?goods_pid:null);
		if("6".equals(type) ){
			if(!StringUtils.isStrNull(orderid)){
				list=iPurchaseService.getPrePurchaseForTB(map);
				counts=iPurchaseService.getPrePurchaseCount(map);
			}else{
				list=new ArrayList<PrePurchasePojo>();
				counts=new ArrayList<String>();
			}
		}else{
			list = iPurchaseService.getPrePurchase(map);
			counts=iPurchaseService.getPrePurchaseCount(map);
		}
		json.setRows(list);
		json.setTotal(counts.size());
		return json;
	}

	/**
	 *
	 * @Title getMCgCount
	 * @Description 获得每月采购数量
	 * @param request
	 * @param model
	 * @return 返回采购人员每月采购数量
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getMCgCount.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMCgCount(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buyid", admuserid);
		return iPurchaseService.getMCgCount(map) + "";
	}

	/**
	 *
	 * @Title getDistributionCount
	 * @Description 获取当日采购分配种类
	 * @param request
	 * @param model
	 * @return 返回当日采购分配种类
	 * @return String 返回结果类型
	 */
	@RequestMapping(value = "/getDistributionCount.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDistributionCount(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buyid", admuserid);
		return iPurchaseService.getDistributionCount(map) + "";
	}
	/**
	 *
	 * @Title getCgCount
	 * @Description 获得采购数量
	 * @param request
	 * @param model
	 * @return 返回某个采购的采购数量
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getCgCount.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getCgCount(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("buyid", admuserid);
		return iPurchaseService.getCgCount(map) + "";
	}
	/**
	 * 人工采购分配
	 * @Title purchasing_allocation
	 * @Description TODO
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @return void
	 */
	@RequestMapping(value = "/purchasing_allocation")
	public void purchasing_allocation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int row=0;
		try{
			OrderAutoServlet o=new OrderAutoServlet();
			o.PreOrderAutoDistribution();
			row=1;
		}catch(Exception e){
			e.printStackTrace();
			row=0;
		}
		out.print(row);
		out.close();
	}
	/**
	 *
	 * @Title getSjCgCount
	 * @Description 获得实际采购数量
	 * @param request
	 * @param model
	 * @return 返回采购实际采购数量
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getSjCgCount.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSjCgCount(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buyid", admuserid);
		return iPurchaseService.getSjCgCount(map) + "";
	}

	/**
	 * 发货3天未入库
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getShippedNoStorage", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getShippedNoStorage(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		Map<String, Object> map = new HashMap<String, Object>();
		String username=null;
		if(!"1".equals(admuserid)){
			username = iWarehouseService.getBuyerNames(admuserid);
		}
		map.put("username", username);
		return iPurchaseService.getShippedNoStorage(map) + "";
	}

	/**
	 *
	 * @Title getfpCount
	 * @Description 获得分配数量
	 * @param request
	 * @param model
	 * @return 返回某个采购的分配数量
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getfpCount.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getfpCount(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buyid", admuserid);
		return iPurchaseService.getfpCount(map) + "";
	}

	/**
	 * 超过1天未发货
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getNotShipped", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getNotShipped(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		Map<String, Object> map = new HashMap<String, Object>();
		String username=null;
		if(!"1".equals(admuserid)){
			username = iWarehouseService.getBuyerNames(admuserid);
		}
		map.put("username", username);
		return iPurchaseService.getNotShipped(map) + "";
	}

	/**
	 *
	 * @Title getOrderInfoCountByState
	 * @Description 获取采购管理页面超过交期项目等结果
	 * @param request 客户端请求
	 * @param model
	 * @return 超过交期项目的订单数量
	 * @throws ParseException
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getOrderInfoCountByState.do", method = RequestMethod.POST)
	@ResponseBody
	public String getOrderInfoCountByState(HttpServletRequest request,Model model) throws ParseException {
		String state = request.getParameter("state");
		String admuserid=request.getParameter("admuserid");
		String sql = "";
		if(!"5".equals(state) && !"6".equals(state) && !"7".equals(state)){
			if ("1".equals(state)) {
				// 超过交期项目
				sql = " and o.order_no in(SELECT distinct order_no FROM orderinfo where orderpaytime<=DATE_SUB(CURDATE(), INTERVAL 5 DAY) and state in(1,2,5))";
			} else if ("2".equals(state)) {
				// 建议替换
				sql = " and o.order_no in(select distinct ops.orderid from order_product_source ops inner join order_details od on ops.od_id=od.id ";
				if (!"1".equals(admuserid)) {
					sql+="inner join goods_distribution g on od.id=g.odid ";
				}
				sql+="where od.state<2 and (od.od_state = 12 or ops.purchase_state=12)";
				if (!"1".equals(admuserid)) {
					sql += " and g.admuserid='"+admuserid+"'";
				}
				sql += ") ";
			} else if ("3".equals(state)) {
				// 同意替换
				sql = " and o.order_no in(select distinct ops.orderid from order_product_source ops inner join order_details od on ops.od_id=od.id ";
				if (!"1".equals(admuserid)) {
					sql+="inner join goods_distribution g on od.id=g.odid ";
				}
				sql+="where od.state<2 AND (ops.purchase_state = 13 or od.od_state=13) and ops.purchase_state not in (3,4,6,8)";
				if (!"1".equals(admuserid)) {
					sql += " and g.admuserid='"+admuserid+"'";
				}
				sql += ")";
			} else if ("4".equals(state)) {
				// 验货有疑问
				sql = " and o.order_no in(select distinct id.orderid from goods_distribution g inner join id_relationtable id on g.orderid=id.orderid and g.goodsid=id.goodid inner join order_details od on id.goodid=od.goodsid where id.is_delete=0 and id.is_replenishment=1 and id.goodstatus<>1 and od.state=1 and od.checked=0 ";
				if (!"1".equals(admuserid)) {
					sql += "and g.admuserid='"+admuserid+"'";
				}
				sql += ") and o.state not in(6,-1) ";
			}
			String sql1 = "select count(order_no) ordernonum, GROUP_CONCAT(\"'\",CONCAT(order_no,\"'\")) ordernoarray from (select DISTINCT o.order_no from orderinfo o inner join admin_r_user a on o.user_id=a.userid where a.admName <> 'testAdm' and o.state>0 and state<6 and 1=1 ";
			// 0 代表管理员
			if (!"1".equals(admuserid)) {
				sql1 += "and o.order_no in(select DISTINCT orderid from goods_distribution where admuserid='"+ admuserid + "') ";
			}
			sql1 += sql + " )a";
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("sql", sql1);
			OrderInfoCountPojo oicp = iWarehouseService.getOrderInfoCountByState(map1);
			if (oicp.getOrdernoarray() == null || "".equals(oicp.getOrdernoarray())) {
				oicp.setOrdernoarray("000000000");
			}
			return JSONObject.fromObject(oicp).toString();
		}else if("5".equals(state)){
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("admin",admuserid);
			String username = iWarehouseService.getBuyerNames(admuserid);
			map1.put("username", username==null?"":username);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
			String time=yesterday + "16:00:00";
			map1.put("times", time);
			OrderInfoCountPojo oicp = iPurchaseService.getOrderInfoCountNoitemid(map1);
			List<PurchasesBean> op_list=iPurchaseService.getOrderInfoCountItemid(map1);
			if(op_list.size()>0){
				String admName = iPurchaseService.queryBuyCount(Integer.valueOf(op_list.get(0).getConfirm_userid()));
				for (PurchasesBean p : op_list) {
					TaoBaoOrderInfo t = server1.getShipStatusInfo(p.getTb_1688_itemid(), p.getLast_tb_1688_itemid(),
							p.getConfirm_time().substring(0, 10), admName,"",p.getOffline_purchase(),p.getOrderNo(),p.getGoodsid());
					if(t!=null && !StringUtils.isStrNull(t.getOrderstatus()) && (t.getOrderstatus().indexOf("等待卖家发货")>-1 || t.getOrderstatus().indexOf("等待买家付款")>-1)){
						if(!oicp.getOrdernoarray().contains(p.getOrderNo())){
							oicp.setOrdernonum(String.valueOf(Integer.valueOf(oicp.getOrdernonum())+1));
							String order_str="";
							if(StringUtils.isStrNull(oicp.getOrdernoarray())){
								order_str="'"+p.getOrderNo()+"'";
							}else{
								order_str=oicp.getOrdernoarray()+",'"+p.getOrderNo()+"'";
							}
							oicp.setOrdernoarray(order_str);
						}
					}else if(t!=null && !StringUtils.isStrNull(t.getShipno()) && (StringUtils.isStrNull(t.getShipstatus()) || t.getShipstatus().indexOf("了解发货状态")>-1 || StringUtils.isStrNull(t.getShipstatus()))){
						if(!oicp.getOrdernoarray().contains(p.getOrderNo())){
							oicp.setOrdernonum(String.valueOf(Integer.valueOf(oicp.getOrdernonum())+1));
							String order_str="";
							if(StringUtils.isStrNull(oicp.getOrdernoarray())){
								order_str="'"+p.getOrderNo()+"'";
							}else{
								order_str=oicp.getOrdernoarray()+",'"+p.getOrderNo()+"'";
							}
							oicp.setOrdernoarray(order_str);
						}
					}
				}
			}
			if (oicp.getOrdernoarray() == null || "".equals(oicp.getOrdernoarray())) {
				oicp.setOrdernoarray("000000000");
			}
			return JSONObject.fromObject(oicp).toString();
		}else if("6".equals(state)){
			//查询采购入库有问题包裹
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("name", !"1".equals(admuserid)?admuserid:null);
			OrderInfoCountPojo oicp = iPurchaseService.getNoMatchOrderByTbShipno(map1);
			return JSONObject.fromObject(oicp).toString();
		}else if("7".equals(state)){
			//采购超24H无物流信息(订单)
			OrderInfoCountPojo oicp=new OrderInfoCountPojo();
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("name", !"1".equals(admuserid)?admuserid:null);
			List<String> orderids= iPurchaseService.getNoShipInfoOrder(map1);
			int index_num=0;
			StringBuilder o=new StringBuilder();
			for(String orderid:orderids){
				List<PurchasesBean> list_p=iPurchaseService.getFpOrderDetails(orderid,admuserid);
				for (PurchasesBean purchasesBean : list_p) {
					String admName = iPurchaseService.queryBuyCount(purchasesBean.getConfirm_userid());
					TaoBaoOrderInfo t = server1.getShipStatusInfo(purchasesBean.getTb_1688_itemid(), purchasesBean.getLast_tb_1688_itemid(),
							purchasesBean.getConfirm_time().substring(0, 10), admName,"",purchasesBean.getOffline_purchase(),orderid,purchasesBean.getGoodsid());
					if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {

					}else if(!o.toString().contains(orderid)){
						o.append("'").append(orderid).append("',");
						index_num++;
					}
				}
			}
			oicp.setOrdernonum(String.valueOf(index_num));
			oicp.setOrdernoarray(o.toString().length()>0?o.toString().substring(0,o.toString().length()-1):"");
			return JSONObject.fromObject(oicp).toString();
		}
		return null;
	}

	/**
	 * 一键确认采购
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allcgqrQrNew")
	@ResponseBody
	protected void allcgqrQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
        Integer websiteType = org.apache.commons.lang3.StringUtils.isBlank(request.getParameter("websiteType"))?1:Integer.parseInt(request.getParameter("websiteType"));
		String datas=iPurchaseService.allcgqrQrNew(orderNo,Integer.valueOf(admid),websiteType);
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	/**
	 *  货源确认
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/PurchaseComfirmTwoHyqr")
	@ResponseBody
	protected void PurchaseComfirmTwoHyqr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String goodsurl = request.getParameter("goodsurl");
		String googsimg = request.getParameter("googsimg");
		// 用户价格
		String goodsprice = request.getParameter("goodsprice");
		String goodstitle = request.getParameter("goodstitle");
		// 用户定量
		String googsnumberr = request.getParameter("googsnumber");
		int googsnumber = Integer.parseInt(googsnumberr);
		String oldValue = request.getParameter("oldValue");
		String newValue = request.getParameter("newValue");
		String purchaseCountt = request.getParameter("purchaseCount");
		int purchaseCount = Integer.parseInt(purchaseCountt);
		String orderNo = request.getParameter("orderno");
		String odid = request.getParameter("od_id");
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
		//判断订单是否被取消
		int st = iPurchaseService.checkOrder(orderNo,String.valueOf(odid));
		int od_id = Integer.parseInt(request.getParameter("od_id"));// order_details
		String child_order_no = request.getParameter("child_order_no");
		String isDropshipOrder = request.getParameter("isDropshipOrder");
		if (st == 111) {
			// 订单被取消
		} else if (st == 2) {
			// 商品被取消 PurchaseComfirmTwoHyqr
		} else {
			st = iPurchaseService.PurchaseComfirmTwoHyqr(userid, orderNo, od_id, 0, goodsdataid, admid, goodsurl,
					googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount, child_order_no,
					isDropshipOrder);
			if (st != 0) {
				// 记录操作
				Date nowTime = new Date();
				SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String writeStr = "******货源确认操作" + odid + ">>>>>>     时间:" + time.format(nowTime) + "      ";
				writeStr += "userid:" + userid + "      ";
				writeStr += "orderNo:" + orderNo + "      ";
				writeStr += "goodsid:" + odid + "      ";
				writeStr += "录入货源:" + newValue + "      ";
				writeStr += "\r\n\r\n";
				UtilAll.printBufInfo(writeStr);
				st = 100;
				LOG.info("确认货源时间:" + time.format(nowTime));
			}
		}

		PrintWriter out = response.getWriter();
		out.print(st);
		out.flush();
		out.close();
	}

	/**
	 * 全部确认货源
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allQrNew")
	@ResponseBody
	protected void allQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas="";
		if(admid==null || "".equals(admid)){
			String sessionId = request.getSession().getId();
			String admuserw = Redis.hget(sessionId, "admuser");
			SerializeUtil su = new SerializeUtil();
			Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
			admid=String.valueOf(admuser.getId());
		}
		if("999".equals(admid) || "1".equals(admid) || "83".equals(admid) || "84".equals(admid)){
			datas="";
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer bf = new StringBuffer();
			Date date = new Date();
			List<Map<String,String>> mapList=iPurchaseService.allQrNew(orderNo,Integer.valueOf(admid));
			for(Map<String,String> map:mapList){
				String oldValue =String.valueOf(map.get("goods_p_price"));
				String newValue =String.valueOf(map.get("goods_p_url"));
				int purchaseCount = Integer.valueOf(String.valueOf(map.get("buycount")));
				if (purchaseCount == 0) {
					purchaseCount =Integer.valueOf(String.valueOf(map.get("yourorder")));
				}
				String url="";
				String car_urlMD5=String.valueOf(map.get("car_urlMD5"));
				String goods_pid=String.valueOf(map.get("goods_pid"));
				String goodsName=String.valueOf(map.get("goodsname"));
				if(StringUtil.isNotBlank(car_urlMD5) && car_urlMD5.substring(0, 1).equals("D")){
					url="https://detail.1688.com/offer/"+goods_pid+".html";
				}else if(StringUtil.isNotBlank(car_urlMD5) && car_urlMD5.substring(0, 1).equals("M")){
					url="https://www.amazon.com/"+(StringUtil.isBlank(goodsName)?"a":goodsName)+"/dp/"+goods_pid;
				}else{
					url=String.valueOf(map.get("car_url"));
				}
				iPurchaseService.PurchaseComfirmTwoHyqr(Integer.valueOf(String.valueOf(map.get("userid"))),String.valueOf(map.get("orderid")),
						Integer.valueOf(String.valueOf(map.get("id"))),Integer.valueOf(String.valueOf(map.get("goodsid"))),
						Integer.valueOf(String.valueOf(map.get("goodsdata_id"))),Integer.valueOf(admid),url,String.valueOf(map.get("car_img")),String.valueOf(map.get("goodsprice"))
						,goodsName,Integer.valueOf(String.valueOf(map.get("yourorder"))),oldValue,newValue,purchaseCount,"","");
				bf.append(String.valueOf(map.get("orderid"))).append(";")
						.append(String.valueOf(map.get("id"))).append(";")
						.append(sdf.format(date)).append("&");
				datas=bf.toString();
			}
		}
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/ShowRmark")
	@ResponseBody
	protected void ShowRmark(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo = request.getParameter("orderNo");
		String goodsdataid = request.getParameter("goodsdataid");
		String goodid = request.getParameter("goodid");
		String odid = request.getParameter("odid");
		response.setCharacterEncoding("UTF-8");
		OrderProductSource orderProductSource = iPurchaseService.ShowRmark(orderNo, Integer.parseInt(goodsdataid), Integer.parseInt(goodid),odid);
		JSONObject json  =JSONObject.fromObject(orderProductSource);
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.flush();
		out.close();
	}


	@RequestMapping(value = "/addGoodNoForRedis")
	@ResponseBody
	protected void addGoodNoForRedis(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String parameters=request.getParameter("parameters");
		System.out.println("parameters为"+parameters);
		String admIds=request.getParameter("admId");
		int admid=Integer.valueOf(StringUtil.isBlank(admIds)?"0":admIds);
		System.out.println("当前用户id为:"+admIds);
		String str[]=new String[parameters.indexOf(",")>-1?parameters.split(",").length:1];
		if(parameters.indexOf(",")>-1){
			str=parameters.split(",");
		}else{
			str[0]=parameters;

		}
		String goodNo="";
		String orderNo="";
		if("0".equals(admIds)){
			//获取登录用户
			String admJson = Redis.hget(request.getSession().getId(), "admuser");
			com.cbt.pojo.Admuser user = (com.cbt.pojo.Admuser)SerializeUtil.JsonToObj(admJson, com.cbt.pojo.Admuser.class);
			admid=user.getId();
		}
		String admname=iPurchaseService.getUserName(admid);
		boolean flag=true;
		Properties properties = new Properties();
		try
		{
			String filePath= GetConfigureInfo.getAdgoodsPath();//获取项目路径
			InputStream inputStream = new FileInputStream(filePath);
			properties.load(inputStream);
			inputStream.close(); //关闭流
			String result = properties.getProperty("result");
			if(result!=null && !result.equals("")){
				JSONArray json=JSONArray.fromObject(result);
				for(int i=0;i<json.size();i++){
					Map map=(Map) json.get(i);
					if(map.get(String.valueOf(admname))!=null){//如果该用户已经存在
						List list=(List) map.get(String.valueOf(admname));
						for(int j=0;j<str.length;j++){
							goodNo=str[j].split(":")[0];
							orderNo=str[j].split(":")[1];
							if(!list.contains(goodNo+":"+orderNo)){
								list.add(goodNo+":"+orderNo);
								json.remove(i);
								json.add(map);
								this.writeData("result", json.toString(), filePath);
							}
						}
						flag=false;
						break;
					}
				}
				if(flag){
					Map mapn = new HashMap();
					List list=new ArrayList();
					for(int j=0;j<str.length;j++){
						list.add(str[j].split(":")[0]+":"+str[j].split(":")[1]);
					}
					mapn.put(String.valueOf(admname), list);
					json.add(mapn);
					this.writeData("result", json.toString(), filePath);
				}
			}else{
				Map map = new HashMap();
				List list=new ArrayList();
				for(int j=0;j<str.length;j++){
					list.add(str[j].split(":")[0]+":"+str[j].split(":")[1]);
				}
				map.put(String.valueOf(admname), list);
				JSONArray json = JSONArray.fromObject(map);
				this.writeData("result", json.toString(), filePath);
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void writeData(String key, String value,String fileURL) {
		Properties prop = new Properties();
		InputStream fis = null;
		OutputStream fos = null;
		try {
			File file = new File(fileURL);
			if (!file.exists())
				file.deleteOnExit();
			file.createNewFile();
			fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();//一定要在修改值之前关闭fis
			fos = new FileOutputStream(file);
			prop.setProperty(key, value);
			prop.store(fos, "Update '" + key + "' value");
			fos.close();

		} catch (IOException e) {
			System.out.println("Visit " + fileURL + " for updating "
					+ value + " value error");
		}finally{
			try {
				fos.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 货源录入采购页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/AddResource")
	public void AddResource(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Map<String,String> map=new HashMap<String, String>();
		map.put("type",request.getParameter("type"));
		map.put("admid",request.getParameter("admid"));
		map.put("userid",request.getParameter("userid"));
		map.put("goodsdata_id",request.getParameter("goodsdata_id"));
		map.put("goods_url",request.getParameter("goods_url"));
		map.put("googs_img",request.getParameter("googs_img"));
		map.put("goods_price",request.getParameter("goods_price"));
		map.put("goods_title",request.getParameter("goods_title"));
		map.put("googs_number",request.getParameter("googs_number"));
		map.put("orderNo",request.getParameter("orderNo"));
		String shop_id=request.getParameter("shop_id");
//		map.put("shop_id",StringUtil.isBlank(shop_id)?"":shop_id);
		map.put("goodid",request.getParameter("goodid"));
		map.put("cGoodstype",request.getParameter("cGoodstype"));
		map.put("od_id",request.getParameter("od_id"));
		map.put("reason",request.getParameter("reason"));
		map.put("state_flag",request.getParameter("state_flag"));
//		map.put("straight_address",request.getParameter("straight_address"));

		map.put("issuree",request.getParameter("issuree"));
		map.put("buycount",request.getParameter("buycount"));
		map.put("currency",request.getParameter("currency"));
//		String straight_address=request.getParameter("straight_address");
//		map.put("straight_address",StringUtil.isBlank(straight_address)?"":straight_address);
		double price=0;
		int state=0;
		String resource="";
		PrintWriter out = response.getWriter();
		try{
			if (!map.get("reason").contains("无货源")) {
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
			map.put("resource",resource);
			map.put("price",String.valueOf(price));
			// 判断是否替换成功标识
			String aduser = iPurchaseService.getUserbyID(map.get("admid"));
			request.setAttribute("adusername", aduser);
			String sessionId = request.getSession().getId();
			request.setAttribute("admid", map.get("admid"));
			state=iPurchaseService.checkOrder(map.get("orderNo"), map.get("od_id"));
			if (state != 111 && state != 2) {
				iPurchaseService.AddRecource(map);
				Date date=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String time=sdf.format(date);
				System.out.println(time+"采购【"+map.get("admid")+"】修改了订单【"+map.get("orderNo")+"】商品【"+map.get("od_id")+"】的货源链接为:"+resource);
			}
		}catch (Exception e){
			e.printStackTrace();
			out.print(0);
		}
		out.print(state);
		out.flush();
		out.close();
	}


	@RequestMapping(value = "/queryPurchaseInfo")
	public String queryPurchaseInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		long start = System.currentTimeMillis();
		System.out.println("queryPurchaseInfo:" + sdf1.format(new Date()) + " 按条件查询");
		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				return "main_login";
			}
			String orderid_no_array = request.getParameter("orderid_no_array");
			String idtypes_ = request.getParameter("idtypes_");
			String goodsid = request.getParameter("goodsid");
			String pagenum = request.getParameter("pagenum");
			String orderid = request.getParameter("orderid");
			String userid = request.getParameter("userid");
			String admid = request.getParameter("admid");
			String orderno = request.getParameter("orderno");
			String goodid = request.getParameter("goodid");
			String goodname = request.getParameter("goodname");
			String date = request.getParameter("date");
			String days = request.getParameter("days");
			String state = request.getParameter("state");
			String pagesize = request.getParameter("pagesize");
			String unpaid = request.getParameter("unpaid");
			String orderarrs = request.getParameter("orderarrs");
			String search_state = request.getParameter("search_state");
			Integer adminid = user.getId();
			Integer cgid = user.getId();
			orderid = StringUtils.isStrNull(orderid) ? null : orderid;
			admid = StringUtils.isStrNull(admid) ? "" : admid;
			userid = StringUtils.isStrNull(userid) ? "" : userid;
			orderno = StringUtils.isStrNull(orderno) ? "" : orderno;
			goodid = StringUtils.isStrNull(goodid) ? "" : goodid.trim();
			goodname = StringUtils.isStrNull(goodname) ? "" : goodname;
			date = StringUtils.isStrNull(date) ? "" : date;
			days = StringUtils.isStrNull(days) ? "" : days;
			state = StringUtils.isStrNull(state) ? "" : state;
			int unpay = 0;
			unpay = StringUtils.isStrNull(unpaid) ? 0 : Integer.parseInt(unpaid);
			System.out.println("========开始执行,purchaseServer.findPageByCondition:" + sdf1.format(new Date()) + "========");
			Page page=iPurchaseService.findPageByCondition(pagenum, orderid, admid, userid, orderno, goodid, date, days,
					state, unpay, Integer.valueOf(pagesize), orderid_no_array, goodsid, goodname, orderarrs,search_state);
			System.out.println("========结束执行,purchaseServer.findPageByCondition:" + sdf1.format(new Date()) + "========");
			if(page.getRecords().size()>0){
				PurchasesBean p=(PurchasesBean)page.getRecords().get(page.getRecords().size()-1);
				String tbInfo=p.getTborderInfo();
				request.setAttribute("tbInfo", StringUtil.isBlank(tbInfo)?"":tbInfo);
			}
			//获取订单出运信息
			String actual_freight = getActualFreight(orderno);
			request.setAttribute("actual_freight",actual_freight);
			//获取预估运费 start
			// 订单信息
			OrderBean orderInfo =iPurchaseService.getOrders(orderno);
			String allFreight = String.valueOf(iPurchaseService.getAllFreightByOrderid(orderno));
			double freightFee = orderInfo.getFreightFee();
			//iOrderinfoService.getFreightFee(allFreight, orderInfo);
			// 获取预估运费 end
			saveValueForRequest(request, user, idtypes_, userid, admid, orderno, goodid, goodname, date, days, state, pagesize, search_state, cgid, unpay, page, freightFee);
			// 获取所有采购人员信息
			List<com.cbt.pojo.Admuser> aublist = iOrderinfoService.getAllBuyer();
			request.setAttribute("aublist", net.sf.json.JSONArray.fromObject(aublist));
			System.out.println("========全部结束:" + sdf1.format(new Date()) + "========");
			List<OrderProductSource> odids = purchaseServer.getAllGoodsids(adminid);
			request.setAttribute("odids", JSONArray.fromObject(odids).toString());
			// 根据订单号判断的客户下单的网站
            request.setAttribute("websiteType", MultiSiteUtil.getSiteTypeNum(orderno));
			long end = System.currentTimeMillis();
			System.out.println("耗时:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("执行错误，原因：" + e.getMessage());
			LOG.error("执行错误，原因：" + e.getMessage());
		}
		return "PurchaseShow";
	}

	private void saveValueForRequest(HttpServletRequest request, Admuser user, String idtypes_, String userid, String admid, String orderno, String goodid, String goodname, String date, String days, String state, String pagesize, String search_state, Integer cgid, int unpay, Page page, double freightFee) {
		request.setAttribute("allFreight", freightFee);
		request.setAttribute("pagenum", page.getPagenum());
		request.setAttribute("totalnum", page.getTotalrecords());
		request.setAttribute("totalpage", page.getTotalpage());
		request.setAttribute("pblist", page.getRecords());
		request.setAttribute("pid_amount",page.getPid_amount());
		request.setAttribute("admid", admid == null || "".equals(admid) || "1".equals(admid) || "83".equals(admid) || "84".equals(admid) ? "999" : admid);
		request.setAttribute("cgid", cgid);
		request.setAttribute("userid", userid);
		request.setAttribute("idtypes_", idtypes_);
		request.setAttribute("goodname", goodname);
		request.setAttribute("search_state", search_state);
		request.setAttribute("orderno", orderno);
		request.setAttribute("goodid", "5201314".equals(goodid) || "5201315".equals(goodid) ? "" : goodid);
		request.setAttribute("date", date);
		request.setAttribute("days", days);
		request.setAttribute("state", state);
		request.setAttribute("admuser", user);
		request.setAttribute("unpay", unpay);
		request.setAttribute("page_size", Integer.parseInt(pagesize));
		request.setAttribute("hideTr", "<script>hideTr()</script>");
		request.setAttribute("keepValue", "<script>keepValue()</script>");
	}

	private String getActualFreight(String orderno) {
		List<ShippingBean> spb=iOrderinfoService.getShipPackmentInfo(orderno);
		String actual_freight="0";
		for(ShippingBean s:spb){
			if(s != null && StringUtil.isNotBlank(s.getActual_freight())){
				actual_freight+=Double.parseDouble(s.getActual_freight());
			}
		}
		return actual_freight;
	}
	@RequestMapping(value = "/getOrderCount.do")
	@ResponseBody
	public orderJson getOrderCount(HttpServletRequest request,  Model model) {//查看差货数量
		orderJson orderJson=new orderJson();
		String admuserid=request.getParameter("admuserid");
			int count = iWarehouseService.FindOrderCount(admuserid);
       orderJson.setTotal(count);
		return orderJson;
	}
	@RequestMapping(value = "/getBadgoods" ,produces = "text/html;charset=UTF-8")
	public void getBadgoods(HttpServletRequest request,HttpServletResponse response, Model model,@RequestParam(value="page",defaultValue="1")int page) {//展示差货详情
        String pid=request.getParameter("pid");
        int pagesize=35;
        int start = (page - 1) * pagesize;
        List<CustomGoodsBean> goodsBeans=this.iWarehouseService.getBadgoods(start,pagesize,pid);
        int goodsCheckCount=this.iWarehouseService.getBadgoodsCount();
		SplitPage.buildPager(request, goodsCheckCount, pagesize, page);
		request.setAttribute("goodsBeans",goodsBeans);
        request.setAttribute("pid", pid);
		try {
			request.getRequestDispatcher("/website/Badgoods.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/AddBadOrder")
	@ResponseBody
	public orderJson AddBadOrder(HttpServletRequest request,HttpServletResponse response) {//查看差货数量
        orderJson orderJson=new orderJson();
        String pid=request.getParameter("pid");
		Double price= null;
		try {
			price = Double.parseDouble(request.getParameter("pid1"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			orderJson.setTotal(0);
			return orderJson;
		}

		int count = iWarehouseService.AddBadOrder(pid,price);
		if (count==1){
			orderJson.setTotal(1);
		}else {
			orderJson.setTotal(0);
		}
		return orderJson;

	}
	@RequestMapping(value = "/UpdateState")
	@ResponseBody
	public orderJson UpdateState(HttpServletRequest request,HttpServletResponse response  ) {
		String pid=request.getParameter("pid");
		orderJson orderJson=new orderJson();
		int count = iWarehouseService.UpdateState(pid);
		if (count==1){
			orderJson.setMessage("操作成功");
		}else {
			orderJson.setMessage("保存失败");
		}
		return orderJson;
	}
	@RequestMapping(value = "/FindReviewShelves",produces = "text/html;charset=UTF-8")
	public void FindReviewShelves(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="page",defaultValue="1")int page) {
	    String cupid=request.getParameter("cupid");
		int pagesize = 35;
		int start = (page - 1) * pagesize;
		String pid = request.getParameter("pid");
		Double price = null;
		try {
			price = Double.parseDouble(request.getParameter("price"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		List<badGoods> list = this.iWarehouseService.findAllCustomBypid(pid, price, pagesize, start,cupid);
		int goodsCheckCount = this.iWarehouseService.findAllCustomBypidCount(pid, price,cupid);
		SplitPage.buildPager(request, goodsCheckCount, pagesize, page);
		request.setAttribute("goodsBeans", list);
        request.setAttribute("cupid", cupid);
		request.setAttribute("pid", pid);
		request.setAttribute("price", price);
		try {
			request.getRequestDispatcher("/website/Review_shelves.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/AddReviewGoods")
	@ResponseBody
	public orderJson AddReviewGoods(HttpServletRequest request,HttpServletResponse response) {
        String pid=request.getParameter("pid");
        String catid1=request.getParameter("catid1");
        String name=request.getParameter("name");
        String maxPrice=request.getParameter("maxPrice");
		orderJson orderJson=new orderJson();
		int count = iWarehouseService.AddReviewGoods(pid,catid1,name,maxPrice);
		if (count==1){
			orderJson.setTotal(1);
		}else {
			orderJson.setTotal(0);
		}
		return orderJson;
	}
}
