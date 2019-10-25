package com.cbt.warehouse.ctrl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.TypeBean;
import com.cbt.common.StringUtils;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.TaoBaoInfoList;
import com.cbt.report.service.GeneralReportService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.StrUtils;
import com.cbt.util.Utility;
import com.cbt.warehouse.service.InventoryService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.InventoryCheck;
import com.cbt.website.bean.InventoryCheckRecord;
import com.cbt.website.bean.InventoryCheckWrap;
import com.cbt.website.bean.InventoryData;
import com.cbt.website.bean.InventoryDetailsWrap;
import com.cbt.website.bean.InventoryWrap;
import com.cbt.website.bean.LossInventoryWrap;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.JsonUtils;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;

/**
 * 有关后台库存逻辑相关控制器  王宏杰  2018-12-07
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController {
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private GeneralReportService generalReportService;
	IExpressTrackDao dao = new ExpressTrackDaoImpl();
	
	/**
	 * 仓库操作库存移库位
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/barcode")
	protected ModelAndView barcodeList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		ModelAndView mv = new ModelAndView("inventorygoods");
		
		Map<String, Object> map = new HashMap<>();
		String state = request.getParameter("state");
		state = StrUtils.isNum(state) ? state : "-1";
		String orderid = request.getParameter("orderid");
		orderid = StringUtil.isBlank(orderid) ? null : orderid;
		String inid = request.getParameter("inid");
		inid = StrUtils.isNum(inid) ? inid : "0";
		String page = request.getParameter("page");
		page = StrUtils.isNum(page) ? page : "1";
		map.put("currentPage", Integer.parseInt(page));
		map.put("page", (Integer.parseInt(page) - 1)*20);
		map.put("state", state);
		map.put("orderid", orderid);
		map.put("inid", inid);
		
		int count = inventoryService.inventoryBarcodeListCount(map);
		if(count > 0) {
			List<InventoryWrap> barcodeList = inventoryService.inventoryBarcodeList(map);
			mv.addObject("barcodeList", barcodeList);
		}
		mv.addObject("barcodeCount", count);
		
		int barcodeListPage = count % 20 == 0 ? count / 20 : count / 20 + 1;
		mv.addObject("barcodeListPage", barcodeListPage);
		mv.addObject("queryParam",map);
		
		return mv;
	}
	/**
	 * 仓库操作库存移库位
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/barcode/update")
	@ResponseBody
	protected Map<String,Object> barcodeUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		Map<String,Object> result = new HashMap<>();
		result.put("status", 200);
		String stribid = request.getParameter("ibid");
		stribid = StrUtils.isNum(stribid) ? stribid : "0";
		
		String strliid = request.getParameter("liid");
		strliid = StrUtils.isNum(strliid) ? strliid : "0";
		
		String strinorout= request.getParameter("inorout");
		strinorout = StrUtils.isNum(strinorout) ? strinorout : "0";
		
		String orderbarcode = request.getParameter("orderbarcode");
		orderbarcode = StringUtil.isBlank(orderbarcode) ? orderbarcode : orderbarcode.trim();
		String inbarcode = request.getParameter("inbarcode");
		inbarcode = StringUtil.isBlank(inbarcode) ? inbarcode : inbarcode.trim();
		Map<String,Object> map = new HashMap<>();
		map.put("ibid", Integer.parseInt(stribid));
		map.put("liid", Integer.parseInt(strliid));
		map.put("inorout", Integer.parseInt(strinorout));
		map.put("inbarcode", inbarcode);
		map.put("orderbarcode", orderbarcode);
		map.put("admid",adm!=null? adm.getId() : 0);
		int updateBarcode = inventoryService.updateBarcode(map);
		if(updateBarcode < 1) {
			result.put("status", 500);
			result.put("reason", "数据错误:"+stribid+"/"+strliid+"/"+strinorout+"/"+inbarcode+"/"+orderbarcode);
		}
		return result;
	}
	
	/**
	 * 仓库取消了移库请求
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/barcode/remark")
	@ResponseBody
	protected Map<String,Object> barcodeRemark(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		
		Map<String,Object> result = new HashMap<>();
		result.put("status", 200);
		String stribid = request.getParameter("ibid");
		stribid = StrUtils.isNum(stribid) ? stribid : "0";
		
		String strliid = request.getParameter("liid");
		strliid = StrUtils.isNum(strliid) ? strliid : "0";
		
		String stribState= request.getParameter("ibState");
		stribState = StrUtils.isNum(stribState) ? stribState : "0";
		
		String orderbarcode = request.getParameter("orderbarcode");
		orderbarcode = StringUtil.isBlank(orderbarcode) ? orderbarcode : orderbarcode.trim();
		String inbarcode = request.getParameter("inbarcode");
		inbarcode = StringUtil.isBlank(inbarcode) ? inbarcode : inbarcode.trim();
		String remark = request.getParameter("remark");
		remark = StringUtil.isBlank(remark) ? remark : remark.trim();
		
		int ibState = Integer.parseInt(stribState);
		Map<String,Object> map = new HashMap<>();
		map.put("ibid", Integer.parseInt(stribid));
		map.put("liid", Integer.parseInt(strliid));
		map.put("ibState", ibState);
		map.put("inbarcode", inbarcode);
		map.put("orderbarcode", orderbarcode);
		map.put("admid",adm!=null? adm.getId() : 0);
		map.put("remark", remark);
		if(ibState == 0) {
			map.put("state", 4);
		}else if(ibState == 1){
			map.put("state", 5);
		}
		int updateBarcode = inventoryService.updateRemark(map);
		if(updateBarcode < 1) {
			result.put("status", 500);
			result.put("reason", "数据错误:"+stribid+"/"+strliid+"/"+stribState+"/"+inbarcode+"/"+orderbarcode);
		}
		return result;
	}
	
	/**
	 * 查询库存明细
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/inventorydetails")
	@ResponseBody
	protected EasyUiJsonResult inventoryDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<>();
		String inventory_sku_id = request.getParameter("inid");
		map.put("inventory_sku_id", inventory_sku_id);
		
		String page = request.getParameter("page");
		page  = StrUtils.isNum(page) ? page : "1";
		map.put("page", (Integer.valueOf(page) -1 ) * 20);
		
		List<InventoryDetailsWrap> toryList = inventoryService.inventoryDetails(map);
		int toryListCount = inventoryService.inventoryDetailsCount(map);
		json.setRows(toryList);
		json.setTotal(toryListCount);
		return json;
	}

	/**
	 * 库存报损列表
	 */
	@RequestMapping("/loss/list")
	public ModelAndView inventoryLossList(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		ModelAndView result = new ModelAndView("inventoryloss");
		String strPage = request.getParameter("page");
		int page = 0;
		page = StrUtils.isMatch(strPage, "(\\d+)") ? Integer.valueOf(strPage) : 1;
		String strtype = request.getParameter("type");
		int type = 0;
		type = StrUtils.isMatch(strtype, "(\\d+)") ? Integer.valueOf(strtype) : -1;
		String enddate = request.getParameter("enddate");
		enddate = StringUtil.isBlank(enddate) ? "" : enddate;
		String startdate = request.getParameter("startdate");
		startdate = StringUtil.isBlank(startdate) ? "" : startdate;
		
		Map<String,Object> map = new HashMap<>();
		map.put("startdate",startdate);
		map.put("enddate",enddate);
		map.put("type",type );
		map.put("currentPage",page );
		map.put("page",(page - 1) * 20 );
		int lossListCount = inventoryService.inventoryLossListCount(map);
		if(lossListCount > 0) {
			List<LossInventoryWrap> inventoryLossList = inventoryService.inventoryLossList(map);
			result.addObject("lossList", inventoryLossList);
		}
		int lossListPage = lossListCount % 20 == 0 ? lossListCount / 20 : lossListCount / 20 + 1;
		result.addObject("queryParam", map);
		result.addObject("lossListPage", lossListPage);
		result.addObject("lossCount", lossListCount);
		return result;
	}
	/**
	 * 库存报损
	 */
	@RequestMapping("/addLoss")
	@ResponseBody
	public Map<String,Object> addInventoryLoss(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		Map<String,Object> result = new HashMap<>();
		String igoodsId = request.getParameter("igoodsId");
		String iskuid = request.getParameter("iskuid");
		String ispecid = request.getParameter("ispecid");
		iskuid = StringUtil.isBlank(iskuid) ? igoodsId : iskuid;
		ispecid = StringUtil.isBlank(ispecid) ? igoodsId : ispecid;
		String change_type = request.getParameter("change_type");
		String remark = request.getParameter("remark");
		String strchangeNumber = request.getParameter("changeNumber");
		String in_id = request.getParameter("in_id");
		
		int changeNumber = 0;
		changeNumber = StrUtils.isMatch(strchangeNumber, "(\\d+)") ? Integer.valueOf(strchangeNumber) : 0;
		
		Map<String,Object> map = new HashMap<>();
		map.put("goods_pid", igoodsId);
		map.put("skuid", iskuid);
		map.put("specid", ispecid);
		map.put("change_type", change_type);
		map.put("remark", remark);
		map.put("changeNumber",changeNumber );
		map.put("inventory_sku_id",in_id );
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null) {
			result.put("status", 500);
			return result;
		}
		map.put("change_adm",String.valueOf(adm.getId()) );
		Map<String, Object> reportLossInventory = inventoryService.reportLossInventory(map);
		result.putAll(reportLossInventory);
		return result;
	}
	/**
	 * 当验货数量大于商品销售数量时，将多余的商品存入库存表中invenntory
	*
	* @param request
	* @param response
	* @throws ServletException
	* @throws IOException
	*/
	@RequestMapping("/add")
	public void addInventory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
	  Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
	  String barcode = request.getParameter("barcode");//库存位置
	  String inventory_count = request.getParameter("inventory_count");//库存数量
	  String orderid = request.getParameter("orderid");//库存商品的订单号
	  String odid = request.getParameter("odid");//库存商品编号
	  String goodid = request.getParameter("goodid");//库存商品编号
	  String storage_count = request.getParameter("storage_count");//验货数量
	  String when_count = request.getParameter("when_count");//当次验货数量
	  String unit = request.getParameter("unit");
	  String specid = request.getParameter("specid");
	  String skuid = request.getParameter("skuid");
	  String tbspecid = request.getParameter("tbspecid");
	  String tbskuid = request.getParameter("tbskuid");
	  String shipno = request.getParameter("shipno");
	  
	  Map<String,String> inventory = new HashMap<>();
	  inventory.put("admName",adm.getAdmName());
	  inventory.put("admId",String.valueOf(adm.getId()));
	  inventory.put("barcode",barcode );
	  inventory.put("inventory_count", inventory_count);
	  inventory.put("odid", odid);
	  inventory.put("goodid", goodid);
	  inventory.put("orderid",orderid );
	  inventory.put("skuid",skuid );
	  inventory.put("specid",specid );
	  inventory.put("tbskuid",tbskuid );
	  inventory.put("tbspecid",tbspecid );
	  inventory.put("storage_count",storage_count );
	  inventory.put("when_count",when_count );
	  inventory.put("unit", unit);
	  inventory.put("shipno", shipno);
	  inventory.put("storage_type", "1");
	  int res = inventoryService.addInventory(inventory);
//	  dao.addInventory(barcode, inventory_count, orderid, odid, storage_count, when_count, adm.getAdmName(), unit);
	  PrintWriter out = response.getWriter();
	  out.print(res);
	  out.flush();
	  out.close();
	}
	/**
	 * 当验货数量大于商品销售数量时，将多余的商品存入库存表中invenntory
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/addtbinventory")
	@ResponseBody
	public Map<String,Object> addtbinventory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String,Object> result = new HashMap<>();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String checkOrderhid = request.getParameter("checkOrderhid");//
		checkOrderhid = org.apache.commons.lang.StringUtils.endsWith(checkOrderhid, ",") ? checkOrderhid.substring(0, checkOrderhid.length()-1) : checkOrderhid;
		
		String shipno = request.getParameter("shipno");
		String barcode = request.getParameter("barcode");
		barcode = org.apache.commons.lang.StringUtils.endsWith(barcode, ",") ? barcode.substring(0, checkOrderhid.length()-1) : barcode;
		Map<String,String> inventory = new HashMap<>();
		inventory.put("admName",adm.getAdmName());
		inventory.put("admId",String.valueOf(adm.getId()));
	  	inventory.put("checkOrderhid",checkOrderhid );
	  	inventory.put("shipno", shipno);
	  	inventory.put("barcode", barcode);
		int res = inventoryService.addInventory(inventory);
		result.put("res", res);
		return result;
	}

	/**
	 * 查询库存统计报表
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/searchGoodsInventoryInfo")
	@ResponseBody
	protected EasyUiJsonResult searchGoodsInventoryInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<Object, Object> map = getObjectByInventory(request,false);
		List<InventoryData> toryList = inventoryService.getIinOutInventory(map);
		int toryListCount = inventoryService.getIinOutInventoryCount(map);
		json.setRows(toryList);
		json.setTotal(toryListCount);
		return json;
	}
	
	/**
	 * 查询库存统计报表
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/list")
	@RequiresPermissions(value = {"admin","user:1026"},logical= Logical.OR)
	protected ModelAndView inventoryInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		ModelAndView mv = new ModelAndView("inventoryReport");
		Map<Object, Object> map = getObjectByInventory(request,false);
		int toryListCount = inventoryService.getIinOutInventoryCount(map);
		if(toryListCount > 0) {
			InventoryCheck lastInventoryCheck = inventoryService.getLastInventoryCheck();
			if(lastInventoryCheck != null) {
				mv.addObject("lastCheckTime", lastInventoryCheck.getCheckTime());
			}
			List<InventoryData> toryList = inventoryService.getIinOutInventory(map);
			mv.addObject("toryList", toryList);
		}
		mv.addObject("toryListCount", toryListCount);
		
		int toryListPage = toryListCount % 20 == 0 ? toryListCount / 20 : toryListCount / 20 + 1;
		mv.addObject("toryListPage", toryListPage);
		mv.addObject("page", map.get("current_page"));
		mv.addObject("queryParam",map);
//		//是否有待操作的移库请求，只有处理完才可以盘点
//		int unDoneInventoryBarcode = inventoryService.getUnDoneInventoryBarcode();
//		mv.addObject("isBarcodeDone",unDoneInventoryBarcode);
		return mv;
	}
	

	private Map<Object, Object> getObjectByInventory(HttpServletRequest request,boolean checkFlag) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String inid = request.getParameter("inid");
		inid = StrUtils.isNum(inid) ? inid : "0";
		map.put("inid", inid);
		int page = Integer.valueOf(StrUtils.isNum(request.getParameter("page")) ? request.getParameter("page") : "1");
		map.put("current_page", page);
		
		int pageCount = page > 0 ? (page - 1) * 20 : 0;
		map.put("page", pageCount);
		map.put("export", "0");
		
	    String goods_pid = request.getParameter("goods_pid");
	    goods_pid= StringUtil.isBlank(goods_pid) ? null : goods_pid;
	    map.put("goods_pid",goods_pid);
	    
		String strmaxintentory = request.getParameter("maxintentory");
		map.put("qmaxintentory",strmaxintentory);
		strmaxintentory = StrUtils.isMatch(strmaxintentory, "\\d+") ? strmaxintentory : "65535";
		map.put("maxintentory",Integer.valueOf(strmaxintentory));
		
		String strminintentory = request.getParameter("minintentory");
		map.put("qminintentory",strminintentory);
		strminintentory = StrUtils.isMatch(strminintentory, "\\d+") ? strminintentory : "0";
		map.put("minintentory",Integer.valueOf(strminintentory));
		
		String strisline = request.getParameter("isline");
		strisline = StrUtils.isMatch(strisline, "\\d+") ? strisline : "0";
		map.put("isline",Integer.valueOf(strisline));
		
		String goodscatid = request.getParameter("goodscatid");
		goodscatid = goodscatid == null ? "0" : goodscatid;
		map.put("goodscatid", goodscatid);
		if(checkFlag) {
			String check_id = request.getParameter("check_id");
			check_id = StrUtils.isNum(check_id) && !"0".equals(check_id)? check_id : "0";
			map.put("check_id", Integer.valueOf(check_id));
		}
		
		return map;
	}

	/**
	 * 库存盘点
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 *             whj 2017-04-25
	 */
	@RequestMapping(value = "/updateSources")
	@ResponseBody
	protected JsonResult updateSources(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResult json = new JsonResult();
		TaoBaoInfoList list = new TaoBaoInfoList();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String new_barcode = request.getParameter("new_barcode");
		String old_barcode = request.getParameter("old_barcode");
		String new_remaining = request.getParameter("new_remaining");
		String old_remaining = request.getParameter("old_remaining");
		String flag = request.getParameter("flag");
		String remark = request.getParameter("remark");
		String pd_id = request.getParameter("pd_id");
		if (new_barcode == null || new_barcode.equals("")) {
			new_barcode = old_barcode;
		}
		// 判断新的库位是否存在
		int isExit = inventoryService.isExitBarcode(new_barcode);
		Inventory i = inventoryService.queryInById(pd_id);
		double goods_p_price=0.00;
		if (isExit > 0 && new_remaining != null && !new_remaining.equals("") && i!=null) {
			String price = i.getGoods_p_price();
			double new_inventory_amount = 0.00;
			if (price.indexOf(",") > -1) {
				String prices[] = price.split(",");
				goods_p_price=Utility.getMaxPrice(prices);
				new_inventory_amount = goods_p_price * Integer.valueOf(new_remaining);
			} else {
				goods_p_price=Double.valueOf(price);
				new_inventory_amount = goods_p_price * Integer.valueOf(new_remaining);
			}
			remark=StringUtil.isBlank(remark)?"":remark;
			isExit = inventoryService.updateSources(flag, StringUtil.isBlank(i.getSku())?"":i.getSku(), i.getGoods_pid(), i.getCar_urlMD5(), new_barcode, old_barcode,
					Integer.valueOf(new_remaining), Integer.valueOf(old_remaining), remark, new_inventory_amount);
			if (isExit > 0) {
				if(Integer.valueOf(new_remaining)<=0){
					//如果库存为0则更改库存标识
					inventoryService.updateIsStockFlag(i.getGoods_pid());
//
//					SendMQ.sendMsg(new RunSqlModel("update custom_benchmark_ready set is_stock_flag=0 where pid='"+i.getGoods_pid()+"'"));
//
					GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(i.getGoods_pid(),"0");
				}
				// 记录更改货源记录日志
				inventoryService.updateSourcesLog(i.getId(), adm.getAdmName(),StringUtil.isBlank(i.getSku())?"":i.getSku(), i.getGoods_pid(), new_barcode,
						old_barcode, Integer.valueOf(new_remaining), Integer.valueOf(old_remaining), remark);
				inventoryService.insertChangeBarcode(i.getId(), old_barcode, new_barcode);
				if(Integer.valueOf(new_remaining)<Integer.valueOf(old_remaining)){
					//记录损耗的库存记录
					map.put("new_remaining", new_remaining);
					map.put("old_remaining", old_remaining);
					map.put("new_barcode", new_barcode);
					map.put("old_barcode", old_barcode);
					map.put("in_id", i.getId());
					double loss_amount=goods_p_price*(Integer.valueOf(new_remaining)-Integer.valueOf(old_remaining));
					BigDecimal bg = new BigDecimal(loss_amount);
					double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("loss_amount",f1);//损耗金额
					map.put("loss_price", goods_p_price);
					map.put("admName", adm.getAdmName());
					inventoryService.recordLossInventory(map);
				}
			}
		}
		list.setAllCount(isExit);
		json.setData(list);
		return json;
	}

	@RequestMapping(value = "/problem_inventory")
	@ResponseBody
	protected void problem_inventory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Map<Object,Object> map = new HashMap<Object,Object>();
		response.setCharacterEncoding("utf-8");
		String id = request.getParameter("in_id");
		map.put("in_id", id);
		int row=inventoryService.problem_inventory(map);
		PrintWriter out = response.getWriter();
		out.print(row+"");
		out.flush();
		out.close();
	}

	/**
	 * 手动录入亚马逊库存
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/inventoryYmxEntry")
	@ResponseBody
	protected JsonResult inventoryYmxEntry(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		JsonResult json = new JsonResult();
		TaoBaoInfoList list = new TaoBaoInfoList();
		Map<String, String> map = new HashMap<String, String>();
		String itmeid = request.getParameter("itmeid");
		String count = request.getParameter("ymx_count");
		String img = request.getParameter("ymx_img");
		String good_name = request.getParameter("good_name");
		String remark = request.getParameter("remark_ymx");
		String goods_p_price = request.getParameter("goods_p_price");
		String barcode = request.getParameter("ymx_barcode2");
		double new_inventory_amount=0.00;
		if(StringUtil.isNotBlank(goods_p_price)){
			new_inventory_amount=Double.parseDouble(goods_p_price)*Integer.valueOf(count);
		}
		map.put("goods_pid", itmeid);
		map.put("count", count);
		map.put("img", StringUtil.isBlank(img)?"/cbtconsole/img/yuanfeihang/loaderTwo.gif":img);
		map.put("good_name", StringUtil.isBlank(good_name)?"亚马逊商品库存录入":good_name);
		map.put("remark", remark);
		map.put("barcode", barcode);
		map.put("itmeid",itmeid);
		map.put("goods_p_price",goods_p_price);
		map.put("new_inventory_amount",String.valueOf(new_inventory_amount));
		map.put("goods_url","https://www.import-express.com/goodsinfo/cbtconsole-1"+itmeid+".html");
		map.put("goods_p_url","https://detail.1688.com/offer/"+itmeid+".html");
		//判断该pid商品是否已在库存中
		Inventory i=inventoryService.getInventoryByPid(map);
		int row=0;
		if(i == null){
			//此次录入的库存是新的亚马逊库存,做插入操作
			row=inventoryService.insertInventoryYmx(map);
		}else{
			//之前有录入过该1688链接的亚马逊库存,让采购更新这里不做操作
//			map.put("id",String.valueOf(i.getId()));
//			row=taoBaoOrderService.updateInventoryYmx();
		}
		list.setAllCount(row);
		json.setData(list);
		return json;
	}

	/**
	 * 库存管理页面统计最近30天新产生的库存
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getNewInventory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getNewInventory(HttpServletRequest request, Model model) {
		return inventoryService.getNewInventory() + "";
	}


	/**
	 * 查询库存统计报表
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/searchAliCategory")
	@ResponseBody
	protected JsonResult searchAliCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		JsonResult json = new JsonResult();
		TaoBaoInfoList list = new TaoBaoInfoList();
		String type = request.getParameter("type");
		String cid = request.getParameter("cid");
		List<AliCategory> aliCategoryList = inventoryService.searchAliCategory(type, cid);
		for (AliCategory a : aliCategoryList) {
			if(a.getCategory().contains("'")){
				a.setCategory(a.getCategory().replace("'", " "));
			}
		}
		list.setAliCategoryList(aliCategoryList);
		json.setData(list);
		return json;
	}

	/**
	 * 导出库存Excel
	 *
	 * @throws ParseException
	 *
	 * @throws Exception
	 */

	@RequestMapping(value = "/exportGoodsInventory")
	public void exportGoodsInventory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String type = request.getParameter("type");
		String type11 = request.getParameter("type11");
		String goodinfo = request.getParameter("goodinfo");
		String year = request.getParameter("year");
		String flag = request.getParameter("flag");
		String mouth = request.getParameter("mouth");
		String scope = request.getParameter("scope");
		String scope11 = request.getParameter("scope11");
		String goodscatid = request.getParameter("goodscatid");
		String have_barcode=request.getParameter("have_barcode");
		String barcode = request.getParameter("barcode");
		String startdate=request.getParameter("startdate");
		String enddate=request.getParameter("enddate");
		int count = Integer.valueOf(request.getParameter("count") != null && !"".equals(request.getParameter("count"))
				? request.getParameter("count").toString() : "0");
		int count11 = Integer
				.valueOf(request.getParameter("count11") != null && !"".equals(request.getParameter("count11"))
						? request.getParameter("count11").toString() : "0");
		String goodsurl = request.getParameter("goodsurl");
		String sku = request.getParameter("sku");
		String buyTime = "";
		if (year != null && year.equals("0")) {
			buyTime = null;
		} else if (year != null && mouth.equals("0")) {
			buyTime = year;
		} else if (year != null) {
			buyTime = year + "-" + mouth;
		}
		goodscatid="0".equals(goodscatid)?null:goodscatid;
		if("全部".equals(goodscatid)){
			goodscatid="abc";
		}else if("其他".equals(goodscatid)){
			goodscatid="bcd";
		}
		if ("-1".equals(flag)) {
			flag = null;
		}
		if (goodinfo == null || goodinfo.equals("")) {
			goodinfo = null;
		}
		if (goodsurl == null || goodsurl.equals("")) {
			goodsurl = null;
		}
		if (sku == null || sku.equals("")) {
			sku = null;
		}
		if(StringUtils.isStrNull(startdate)){
			startdate=null;
		}else{
			startdate=startdate+" 00:00:00";
		}
		if(StringUtils.isStrNull(enddate)){
			enddate=null;
		}else{
			enddate=enddate+" 23:59:59";
		}
		if(!"全部".equals(have_barcode)){
			barcode=have_barcode;
		}else if ("全部".equals(have_barcode) && barcode != null && !"".equals(barcode) && barcode.indexOf("STK") <= -1) {
			barcode = Utility.getBarcode(barcode);
		} else if (barcode == null || "".equals(barcode)) {
			barcode = null;
		}
		if (scope != null && scope.equals("0")) {
			scope = null;
		}
		map.put("buyTime", buyTime);
		map.put("goodinfo", goodinfo);
		map.put("sku", sku);
		map.put("goodsurl", goodsurl);
		map.put("type", type);
		map.put("type11", type11);
		map.put("scope11", scope11);
		map.put("count11", count11);
		map.put("scope", scope);
		map.put("count", count);
		map.put("goodscatid", goodscatid);
		map.put("barcode", barcode);
		map.put("flag", flag);
		map.put("enddate", enddate);
		map.put("startdate", startdate);
		map.put("page", -1);
		map.put("export", "1");
		List<InventoryData> toryList = inventoryService.getIinOutInventory(map);
		HSSFWorkbook wb = generalReportService.exportGoodsInventory(toryList);
		response.setContentType("application/vnd.ms-excel");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String filename = "库存盘点数据导出" + sdf.format(new Date());
		filename=StringUtils.getFileName(filename);
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 手动录入库存
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 *             whj 2017-04-25
	 */
	@RequestMapping(value = "/inventoryEntry")
	@ResponseBody
	protected JsonResult inventoryEntry(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		JsonResult json = new JsonResult();
		TaoBaoInfoList list = new TaoBaoInfoList();
		Map<Object, Object> map = new HashMap<Object, Object>();
		int row = 0;
		String orderNo=request.getParameter("orderNo");
		String goodsid = request.getParameter("goodsid");
		String count = request.getParameter("count");
		String remark = request.getParameter("remark");
		String in_barcode = request.getParameter("in_barcode");
		map.put("orderid", orderNo);
		map.put("goodsid", goodsid);
		map.put("count", count);
		map.put("remark", remark);
		map.put("in_barcode", in_barcode);
		OrderDetailsBean od = inventoryService.findOrderDetails(map);
		if (od != null && Integer.valueOf(count) >= 0) {
			map.put("goods_name", od.getGoodsname());
			map.put("sku", od.getCar_type());
			map.put("goodscatid", od.getGoodscatid());
			map.put("car_img", od.getCar_img());
			if (od.getGoods_pid() == null || "".equals(od.getGoods_pid())) {
				String pid = Utility.getItemid(od.getCar_url());
				map.put("goods_pid", pid);
				map.put("goods_url", od.getCar_url());
			} else {
				map.put("goods_pid", od.getGoods_pid());
				map.put("goods_url", "https://www.import-express.com/spider/detail?&source=" + od.getCar_urlMD5()
						+ "&item=" + od.getGoods_pid() + "");
			}
			map.put("car_urlMD5", od.getCar_urlMD5());
			map.put("goods_p_price", od.getGoods_p_price());
			double new_amount = Integer.valueOf(count)* Double.valueOf("".equals(od.getGoods_p_price()) ? "0" : od.getGoods_p_price());
			map.put("inventory_amount", new_amount);
			Inventory in = inventoryService.queryInId(od.getCar_type(), od.getGoods_pid(), in_barcode,od.getCar_urlMD5(), "");
			if (in != null) {
				map.put("flag", in.getFlag());
				row = inventoryService.updateSources(String.valueOf(in.getFlag()), od.getCar_type(),
						od.getGoods_pid(), od.getCar_urlMD5(), in_barcode, in_barcode, Integer.valueOf(count),
						Integer.valueOf(count), remark, new_amount);
			} else {
				row = inventoryService.inventoryEntry(map);
			}
		}
		list.setAllCount(row);
		json.setData(list);
		return json;
	}

	/**
	 * 库存管理页面统计最近30天新产生的库存
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getSaleInventory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSaleInventory(HttpServletRequest request, Model model) {
		return inventoryService.getSaleInventory() + "";
	}

	/**
	 * 最近30天 产生的 库存损耗
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getLossInventory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getLossInventory(HttpServletRequest request, Model model) {
		return inventoryService.getLossInventory() + "";
	}

	/**
	 *最近30天 产生的 库存删除
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getDeleteInventory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDeleteInventory(HttpServletRequest request, Model model) {
		return inventoryService.getDeleteInventory() + "";
	}

	/**
	 * 删除库存
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 *             whj 2017-04-25
	 */
	@RequestMapping(value = "/deleteInventory")
	@ResponseBody
	protected JsonResult deleteInventory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		JsonResult json = new JsonResult();
		TaoBaoInfoList list = new TaoBaoInfoList();
		int id=Integer.valueOf(request.getParameter("id"));
		String goods_pid=request.getParameter("goods_pid");
//		String sku=request.getParameter("sku");
		String barcode=request.getParameter("barcode");
//		String amount=request.getParameter("amount");
		String dRemark=request.getParameter("dRemark");
		int count = inventoryService.deleteInventory(id,dRemark);
		if(count>0){
			list.setAllCount(count);
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			//如果库存为0则更改库存标识

			InventoryController.UpdateIsStockFlagThread u=new InventoryController.UpdateIsStockFlagThread(String.valueOf(adm.getId()), goods_pid, id, barcode,adm.getAdmName());
			u.start();
		}else{
			list.setAllCount(0);
		}
		json.setData(list);
		return json;
	}

	class UpdateIsStockFlagThread extends Thread{
		private String goods_pid;
		private String adm_id;
		private int id;
		private String barcode;
		private String admName;

		public UpdateIsStockFlagThread(String adm_id,String goods_pid,int id,String barcode,String admName){
			this.adm_id = adm_id;
			this.goods_pid = goods_pid;
			this.id = id;
			this.barcode=barcode;
			this.admName=admName;
		}

		@Override
		public void run() {
			try{
//
				inventoryService.updateIsStockFlag2(goods_pid);
//				SendMQ.sendMsg(new RunSqlModel("update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'"));
				GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"0");
				//如果库存为0则更改库存标识
				inventoryService.updateIsStockFlag(goods_pid);
				//记录删除该库存的人
//				String admuserJson = Redis.hget(adm_id, "admuser");
//				Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
				inventoryService.updateSourcesLog(id, admName, "", goods_pid, "",barcode, 0, 0, "库存删除");
//
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**根据产品ID获取数据（产品库存录入）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/get/product")
	@ResponseBody
	public Map<String,Object> getProduct(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result = new HashMap<>();
		String goods_pid = request.getParameter("goods_pid");
		BasicDBObject find = new BasicDBObject("pid",Long.parseLong(goods_pid));
		//产品名 图片 sku 类别 url  type
		List<String> findAnyFromMongo2 = MongoDBHelp.INSTANCE.findAnyFromMongo2("product", find , null, 0, 0);
		if(findAnyFromMongo2 == null  || findAnyFromMongo2.isEmpty()) {
			result.put("status", 500);
			result.put("reason", "未找到产品");
			return result;
		}
		List<Map<String,String>> typelist = new ArrayList<Map<String,String>>();
		String string = findAnyFromMongo2.get(0);
		JSONObject json = JSONObject.parseObject(string);
		result.put("goodsCatid", json.getString("catid1"));
		result.put("goodsPice", json.getString("price"));
		result.put("goods_pid", goods_pid);
		result.put("goodsName", json.getString("enname"));
		String sku = json.getString("sku");
		String entype = json.getString("entype_new");
		String remotPath = json.getString("remotpath");
		String img = remotPath+json.getString("custom_main_image");
		result.put("goodsImg", img);
		if(StringUtil.isNotBlank(entype) && StringUtil.isNotBlank(sku)) {
			Map<String,TypeBean> typeMap = new HashMap<>();
			List<TypeBean> entypeNew = JsonUtils.jsonToList(entype, TypeBean.class);
			if (entypeNew != null && entypeNew.size() > 0) {
				for (TypeBean typeBean : entypeNew) {
					if (StringUtil.isNotBlank(typeBean.getImg())) {
						typeBean.setImg(remotPath + typeBean.getImg());
					}
					typeMap.put(typeBean.getId(), typeBean);
				}
			}
			
			JSONArray parseArray = JSONArray.parseArray(sku);
			
			Map<String,String> skuM = null;
			boolean isSku = true;
			for(int i=0,size=parseArray.size();i<size;i++) {
				JSONObject skubject = JSONObject.parseObject(String.valueOf(parseArray.get(i)));
				String skuPropIds = skubject.getString("skuPropIds");
				String specId = skubject.getString("specId");
				String skuId = skubject.getString("skuId");
				
				isSku = true;
				skuM = new HashMap<>();
				skuM.put("specId", specId);
				skuM.put("skuId", skuId);
				
				String[] skuPropIdsArray = skuPropIds.split(",");
				for(String s : skuPropIdsArray) {
					TypeBean typeBean = typeMap.get(s);
					if(typeBean == null) {
						isSku = false;
						break;
					}
					String context= typeBean.getLableType()+":"+typeBean.getValue()+"@"+typeBean.getId();
					String skuContext = skuM.get("sku");
					skuContext = StringUtil.isBlank(skuContext) ? context : skuContext + "," + context;
					
					skuM.put("sku", skuContext);
					String skuimg = StringUtil.isBlank(typeBean.getImg()) ? img : typeBean.getImg();
					skuM.put("skuimg", skuimg);
				}
				if(isSku) {
					typelist.add(skuM);
				}
			}
		}
		result.put("skuList", typelist);
		result.put("skuListSize", typelist.size());
		result.put("status", 200);
		return result;
	}
	
	/**根据淘宝订单号或运单号获取产品列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("get/tborder")
	@ResponseBody
	public Map<String,Object> getTBorder(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result = new HashMap<>();
		String order_shipno = request.getParameter("order_shipno");
		result.put("tbGoodsSize", 0);
		result.put("status", 500);
		try {
			List<Map<String,Object>> tbGoods = inventoryService.getTbGoods(order_shipno);
			if(tbGoods != null && !tbGoods.isEmpty()) {
				result.put("tbGoodsList", tbGoods);
				result.put("tbGoodsSize", tbGoods.size());
				result.put("status", 200);
			}else{
				result.put("tbGoodsSize", 0);
				result.put("reason", "未查询到数据");
			}
			
		} catch (Exception e) {
			result.put("reason", "请求出错");
		}
		return result;
	}
	
	
	
	
	/**获取库位
	 * @return
	 */
	@RequestMapping("/get/barcode")
	@ResponseBody
	public Map<String,Object> getBarcode(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		result.put("status", 200);
		result.put("barcode", "<input type='text' placeholder='请输入库位条形码' class='i_barcode_n'> ");
		return result;
	}
	
	/**录入库存
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/input")
	@ResponseBody
	public Map<String,Object> inputInventory(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result = new HashMap<>();
		result.put("status", 200);
		String isTbOrder = StrUtils.object2NumStr(request.getParameter("isTbOrder")) ;
		String varray = request.getParameter("varray");
		String reasonType = request.getParameter("reasonType");
		String remark = request.getParameter("remark");
		Map<String,String>  map = new HashMap<>();
		map.put("isTBOrder",isTbOrder);
		map.put("remark",remark );
		map.put("reasonType", reasonType);
		varray = StringUtil.isNotBlank(varray) ? varray : "XXX";
		varray = varray.startsWith(";") ? varray.substring(1) : varray;
		String[] varrays = varray.split(";");
		
		if("0".equals(isTbOrder)) {
			String lu_pid = request.getParameter("lu_pid");
			String lu_price = request.getParameter("lu_price");
			String lu_name = request.getParameter("lu_name");
			String lu_img = request.getParameter("lu_img");
			String lu_catid = request.getParameter("lu_catid");
			map.put("goods_pid",lu_pid );
			map.put("goods_name", lu_name);
			map.put("img", lu_img);
			map.put("goodsCatid", lu_catid);
			map.put("goods_price",lu_price );
			
			for(String v : varrays) {
				String[] vs = v.split("(\\|d\\|)");
				if(vs.length>5) {
					if(!StrUtils.isNum(vs[3].trim()) || Integer.parseInt(vs[3].trim()) < 1) {
						continue;
					}
					map.put("sku",vs[0].trim());
					map.put("specid",vs[1].trim());
					map.put("skuid",vs[2].trim());
					map.put("count",vs[3].trim());
					map.put("barcode",vs[4].trim());
					map.put("img",vs[5].trim());
					inventoryService.inputInventory(map);
				}
			}
		}else {
			map.put("tbOrderid",request.getParameter("tb_order"));
			map.put("tbShipno",request.getParameter("tb_shipno"));

			for(String v : varrays) {
				String[] vs = v.split("(\\|d\\|)");
				if(vs.length>8) {
					if(!StrUtils.isNum(vs[3].trim()) || Integer.parseInt(vs[3].trim()) < 1) {
						continue;
					}
//					lu_sku+"|"+lu_specid+"|"+lu_skuid+"|"+lu_count+"|"+lu_barcode+"|"+lu_name+"|"+lu_pid+"|"+lu_img+"|"+lu_url+"|"+lu_price;
					map.put("sku",vs[0].trim());
					map.put("specid",vs[1].trim());
					map.put("skuid",vs[2].trim());
					map.put("count",vs[3].trim());
					map.put("barcode",vs[4].trim());
					map.put("goods_name", vs[5].trim());
					map.put("goods_pid",vs[6].trim() );
					map.put("img", vs[7].trim());
					map.put("goods_purl",vs[8].trim() );
					map.put("goods_price",vs[9].trim() );
					inventoryService.inputInventory(map);
				}
			}
		}
		return result;
	}
	/**库存盘点列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/check/list")
	public ModelAndView inventoryCheck(HttpServletRequest request, HttpServletResponse response) {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null) {
			ModelAndView mv = new ModelAndView("main_menu");
			return mv;
		}
		ModelAndView mv = new ModelAndView("inventorycheck");
		Map<Object, Object> map = getObjectByInventory(request,true);
		int checkListCount = inventoryService.getIinOutInventoryCount(map);
		if(checkListCount == 0) {
			return mv;
		}
		List<InventoryCheckWrap> invetoryCheckList = inventoryService.invetoryCheckList(map);
		
		mv.addObject("checkList", invetoryCheckList);
		mv.addObject("checkListCount", checkListCount);
		int toryListPage = checkListCount % 20 == 0 ? checkListCount / 20 : checkListCount / 20 + 1;
		mv.addObject("toryListPage", toryListPage);
		
		if(Integer.parseInt(StrUtils.object2NumStr(map.get("check_id"))) == 0) {
			List<InventoryCheck> unDoneInventoryCheck = inventoryService.getUnDoneInventoryCheck();
			map.put("check_id", 
					unDoneInventoryCheck==null || unDoneInventoryCheck.isEmpty() ? 0 : unDoneInventoryCheck.get(0).getId());
			
		}
		mv.addObject("queryParam",map);
		
		//是否有待操作的移库请求，只有处理完才可以盘点
		int unDoneInventoryBarcode = inventoryService.getUnDoneInventoryBarcode();
		mv.addObject("isBarcodeDone",unDoneInventoryBarcode);
		
		return mv;
		
	}
	/**开始盘点
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/check/start")
	@ResponseBody
	public Map<String,Object> checkStart(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result= new HashMap<>();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null) {
			result.put("status", 500);
			result.put("reason", "未登录，请先登录");
			return result;
		}
		
		result.put("status", 200);
		InventoryCheck check = new InventoryCheck();
		String strCat = request.getParameter("checkCategory");
		strCat = StrUtils.isNum(strCat) ? strCat : "0";
		check.setCheckCat(Integer.valueOf(strCat));
		check.setCheckAdm(adm.getId());
		int insertInventoryCheck = inventoryService.insertInventoryCheck(check);
		result.put("check_id", insertInventoryCheck);
		return result;
	}
	
	/**盘点开始,修改产品库存
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/check/update")
	@ResponseBody
	public Map<String,Object> checkUpdate(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result= new HashMap<>();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null) {
			result.put("status", 500);
			result.put("reason", "未登录，请先登录");
			return result;
		}
		
		result.put("status", 200);
		
		String strCheckId = request.getParameter("check_id");
		strCheckId = StrUtils.isNum(strCheckId) ? strCheckId : "0";
		String strInventoryId = request.getParameter("in_id");
		strInventoryId = StrUtils.isNum(strInventoryId) ? strInventoryId : "0";
		if("0".equals(strInventoryId) || "0".equals(strCheckId)) {
			result.put("status", 501);
			result.put("reason", "数据错误,未匹配库存或者未启动盘点");
			return result;
		}
		try {
			String strRecordId = request.getParameter("record_id");
			strRecordId = StrUtils.isNum(strRecordId) ? strRecordId : "0";
			
			int recordId = Integer.parseInt(strRecordId);
			
			String strInventoryRemaining = request.getParameter("inventory_remaining");
			strInventoryRemaining = StringUtil.isNotBlank(strInventoryRemaining) ? strInventoryRemaining.trim() : "0";
			strInventoryRemaining = StrUtils.isNum(strInventoryRemaining) ? strInventoryRemaining.trim() : "0";
			
			String strCheckRemaining = request.getParameter("check_remaining");
			strCheckRemaining = StringUtil.isNotBlank(strCheckRemaining) ? strCheckRemaining.trim() : "0";
			strCheckRemaining = StrUtils.isNum(strCheckRemaining) ? strCheckRemaining : "0";
			InventoryCheckRecord record = new InventoryCheckRecord();
			record.setId(recordId);
			String goodsPid = request.getParameter("goods_pid");
			String goodsSku = request.getParameter("goods_sku");
			String goodsSpecid = request.getParameter("goods_specid");
			String goodsSkuid = request.getParameter("goods_skuid");
			String goodsPrice = request.getParameter("goods_price");
			String beforeBarcode = request.getParameter("before_barcode");
			String afterBarcode = request.getParameter("after_barcode");
			String goodsName = request.getParameter("goods_name");
			record.setAfterBarcode(afterBarcode);
			record.setBeforeBarcode(beforeBarcode);
			record.setCheckRemaining(Integer.parseInt(strCheckRemaining));
			record.setGoodsPid(goodsPid);
			record.setGoodsPrice(goodsPrice);
			record.setGoodsSku(goodsSku);
			record.setGoodsSkuid(goodsSkuid);
			record.setGoodsSpecid(goodsSpecid);
			record.setInventorySkuId(Integer.parseInt(strInventoryId));
			record.setInventoryCheckId(Integer.parseInt(strCheckId));
			record.setInventoryRemaining(Integer.parseInt(strInventoryRemaining));
			record.setGoodsName(goodsName);
			if(recordId > 0) {
				inventoryService.updateInventoryCheckRecord(record );
			}else {
				recordId = inventoryService.insertInventoryCheckRecord(record);
			}
			result.put("recordId", recordId);
			result.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reson", "发生错误");
			result.put("status", 103);
		}
		
		return result;
	}
	
	
	/**开始盘点
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/check/cancel")
	@ResponseBody
	public Map<String,Object> checkCancel(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result= new HashMap<>();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null) {
			result.put("status", 500);
			result.put("reason", "未登录，请先登录");
			return result;
		}
		
		result.put("status", 200);
		InventoryCheck check = new InventoryCheck();
		String check_id = request.getParameter("check_id");
		check_id = StrUtils.isNum(check_id) ? check_id : "0";
		if("0".equals(check_id)) {
			result.put("status", 501);
			result.put("reason", "未匹配到盘点记录");
			return result;
			
		}
		check.setId(Integer.valueOf(check_id));
		check.setCancelAdm(adm.getId());
		check.setCancelRemark("撤销本次盘点");
		int checkCancel = inventoryService.updateInventoryCheckCancel(check);
		result.put("check_cancel", checkCancel);
		if(checkCancel == 0) {
			result.put("status", 502);
			result.put("reason", "盘点取消失败,未开始实际盘点");
		}else {
			result.put("status", 200);
		}
		return result;
	}
	/**完成盘点
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/check/done")
	public void checkDone(HttpServletRequest request, HttpServletResponse response) {
		String check_id = request.getParameter("check_id");
		check_id = StrUtils.isNum(check_id) ? check_id : "0";
		int checkId = Integer.parseInt(check_id);
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null) {
			return ;
		}
		try {
			List<InventoryCheckRecord> dicRecord = inventoryService.doneInventoryCheckRecord(checkId,adm.getId());
			HSSFWorkbook wb = generalReportService.exportInventoryCheckExcel(dicRecord);
			response.setContentType("application/vnd.ms-excel");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String filename = "库存盘点数据导出" + sdf.format(new Date());
			filename = StringUtils.getFileName(filename);
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**完成盘点
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/check/print")
	public void checkPrint(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = getObjectByInventory(request,true);
		try {
			
			List<InventoryCheckWrap> checkList = inventoryService.invetoryCheckList(map);
			
			HSSFWorkbook wb = generalReportService.exportInventoryExcel(checkList);
			response.setContentType("application/vnd.ms-excel");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String filename = "库存盘点数据导出" + sdf.format(new Date());
			filename = StringUtils.getFileName(filename);
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**获取列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/catlist")
	@ResponseBody
	public Map<String,Object> catlist(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result= new HashMap<>();
		result.put("status", 200);
		List<Map<String,Object>> ncatList = new ArrayList<>();
		List<Map<String,Object>> catList = inventoryService.getInventoryCatList();
		if(catList == null || catList.isEmpty()) {
			result.put("status", 500);
			result.put("reason", "未获取到类别列表");
		}else {
			for(Map<String,Object> m : catList) {
				if(m == null || m.isEmpty()) {
					continue;
				}
				if(org.apache.commons.lang.StringUtils.equals(StrUtils.object2Str(m.get("catid")), "0")) {
					continue;
				}
				if(StringUtil.isBlank(StrUtils.object2Str(m.get("catname")))) {
					continue;
				}
				ncatList.add(m);
			}
			result.put("catSize", ncatList.size());
			result.put("catList", ncatList);
		}
		return result;
	}
	
	/**盘点历史记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/check/info")
	public ModelAndView checkInfo(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("inventorycheckinfo");
		String strInid = request.getParameter("inid");
		strInid = StrUtils.isNum(strInid) ? strInid : "0";
		
		String strPage = request.getParameter("page");
		strPage = StrUtils.isNum(strPage) ? strPage : "1";
		
		int page = (Integer.parseInt(strPage) - 1) * 50;
		String goods_pid = request.getParameter("goods_pid");
		goods_pid = StringUtil.isBlank(goods_pid) ? null : goods_pid;
		
		int inid = Integer.valueOf(strInid);
		int icrHistoryCount = 0,totalPage = 0;
		if(inid > 0) {
			icrHistoryCount = inventoryService.getICRHistoryCount(inid,goods_pid);
			if(icrHistoryCount > 0) {
				List<InventoryCheckRecord> icrHistory = inventoryService.getICRHistory(inid,page,goods_pid);
				mv.addObject("icrHistory", icrHistory);
				totalPage = icrHistoryCount % 50 == 0 ? icrHistoryCount / 50 : icrHistoryCount / 50 + 1;
			}
		}
		mv.addObject("currentPage", Integer.parseInt(strPage));
		
		mv.addObject("inid", inid);
		mv.addObject("totalPage", totalPage);
		mv.addObject("totalCount", icrHistoryCount);
		return mv;
	}
	
	
}