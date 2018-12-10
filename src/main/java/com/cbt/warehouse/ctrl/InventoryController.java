package com.cbt.warehouse.ctrl;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.common.StringUtils;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.TaoBaoInfoList;
import com.cbt.report.service.GeneralReportService;
import com.cbt.util.Utility;
import com.cbt.warehouse.service.InventoryService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有关后台库存逻辑相关控制器  王宏杰  2018-12-07
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/inventory")
public class InventoryController {
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private GeneralReportService generalReportService;

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
		Map<Object, Object> map = new HashMap<Object, Object>();
		int page = Integer.valueOf(request.getParameter("page"));
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
		String goods_pid=request.getParameter("goods_pid");
		String enddate=request.getParameter("enddate");
		String valid=request.getParameter("valid");
		goods_pid= StringUtil.isBlank(goods_pid)?null:goods_pid;
		int count = Integer.valueOf(request.getParameter("count") != null && !"".equals(request.getParameter("count"))? request.getParameter("count").toString() : "0");
		int count11 = Integer.valueOf(request.getParameter("count11") != null && !"".equals(request.getParameter("count11"))? request.getParameter("count11").toString() : "0");
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
		flag="-1".equals(flag)?null:flag;
		page=page>0?(page - 1) * 20:page;
		goodinfo= StringUtils.isStrNull(goodinfo)?null:goodinfo;
		goodsurl=StringUtils.isStrNull(goodsurl)?null:goodsurl;
		sku=StringUtils.isStrNull(sku)?null:sku;
		startdate=StringUtils.isStrNull(startdate)?null:(startdate+" 00:00:00");
		enddate=StringUtils.isStrNull(enddate)?null:(enddate+" 23:59:59");
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
		map.put("page", page);
		map.put("flag", flag);
		map.put("enddate", enddate);
		map.put("startdate", startdate);
		map.put("export", "0");
		map.put("goods_pid",goods_pid);
		map.put("valid","-1".equals(valid)?null:valid);
		List<Inventory> toryList = inventoryService.getIinOutInventory(map);
		List<Inventory> toryListCount = inventoryService.getIinOutInventoryCount(map);
		json.setRows(toryList);
		json.setTotal(toryListCount.size());
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
		List<Inventory> toryList = inventoryService.getIinOutInventory(map);
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
		// String orderid=request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		String count = request.getParameter("count");
		String remark = request.getParameter("remark");
		String in_barcode = request.getParameter("in_barcode");
		// map.put("orderid", orderid);
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
}