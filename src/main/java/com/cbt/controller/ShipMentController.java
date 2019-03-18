package com.cbt.controller;

import com.alibaba.fastjson.JSON;
import com.cbt.parse.service.StrUtils;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SysParamUtil;
import com.cbt.util.WebTool;
import com.cbt.warehouse.pojo.Shipment;
import com.cbt.warehouse.service.ShipmentService;
import com.cbt.warehouse.util.ExcelUtil;
import com.cbt.warehouse.util.ReadExcel;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.DataGridResult;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.FileTool;
import net.sf.json.JSONArray;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title : ShipMentController.java
 * @Description : TODO
 * @Company : www.importExpress.com
 * @author : 柒月
 * @date : 2016年9月8日
 * @version : V1.0
 */

@Controller
@RequestMapping("/shipment")
public class ShipMentController {

	@Autowired
	private ShipmentService shipmentService;


	/**
	 * 运营部运单导入
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/shipMentUpload")
	public @ResponseBody
    String toShipment(MultipartHttpServletRequest request, HttpServletResponse response) {
		String result = "";
		String uploadPath = SysParamUtil.getParam("uploadPath");
		Iterator<String> itr = request.getFileNames();
		String company = request.getParameter("company");
		MultipartFile mpf = null;
		// 每次上传的数据标识
		String uuid = UUID.randomUUID().toString();
		while (itr.hasNext()) {
			// 取出文件
			mpf = request.getFile(itr.next());
			Long time = System.currentTimeMillis();
			String path = uploadPath + "/" + time + "/";
			isExist(path);
			// 输出(保存)文件
			try {
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(path + mpf.getOriginalFilename()));
				List<Shipment> list = new ArrayList<Shipment>();
				if ("JCEX".equals(company)) {
					list = ExcelUtil.readJCExcel(path + mpf.getOriginalFilename(), uuid);
				} else if ("emsinten".equals(company)) {
					list = ExcelUtil.readYZExcel(path + mpf.getOriginalFilename(), uuid);
				}else if ("原飞航".equals(company)) {
					list = ExcelUtil.readYFHExcel(path + mpf.getOriginalFilename(), uuid);
				}else if("SF".equals(company)){
					list = ExcelUtil.readSFExcel(path + mpf.getOriginalFilename(), uuid);
				}else if("HY".equals(company)){
					list = ExcelUtil.readHYExcel(path + mpf.getOriginalFilename(), uuid,company);
				}else if("TL".equals(company)){
					list = ExcelUtil.readTLExcel(path + mpf.getOriginalFilename(), uuid);
				}else if ("灿鑫".equals(company) || "深圳诚泰".equals(company)) {
					list = ExcelUtil.readHYExcel(path + mpf.getOriginalFilename(), uuid,company);
				}else if("迅邮".equals(company)){
					list = ExcelUtil.readXYExcel(path + mpf.getOriginalFilename(), uuid,company);
				}else if("大誉".equals(company)){
					list = ReadExcel.readDYExcel(path + mpf.getOriginalFilename(), uuid,company);
				}else if("衡欣".equals(company)){
					list = ReadExcel.readHXExcel(path + mpf.getOriginalFilename(), uuid,company);
				}
				// 校验forwarder表中的运单信息
				// 已经上传的运单信息不能重复上传
				List<Shipment>  payedList = shipmentService.insertShipment(list);
				int  resCount = payedList.get(0).getCount();
				if(payedList.size()==1&&payedList.get(0).getOrderno()==null){
					result = "{\"success\":true,\"successNum\":" + resCount + ",\"list\":"+JSON.toJSONString(null)+"}";
				}else{
					result = "{\"success\":true,\"successNum\":" + resCount + ",\"list\":"+JSON.toJSONString(payedList)+"}";
				}
//				 List<Shipment> unValidateList = shipmentService.validateShipmentList(list, uuid);
//				 unValidateList.addAll(payedList);
				// 导入完成,删除文件夹及其文件夹下的文件
				FileTool.deleteDirectory(path);
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
				result = "{\"success\":false,\"message\":\"系统错误,请联系开发人员!\"}";
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				result = "{\"success\":false,\"message\":\"系统错误,请联系开发人员!\"}";
			} catch (InvalidFormatException e) {
				e.printStackTrace();
				result = "{\"success\":false,\"message\":\"系统错误,请联系开发人员!\"}";
			} catch (IOException e) {
				e.printStackTrace();
				result = "{\"success\":false,\"message\":\"系统错误,请联系开发人员!\"}";
			} catch (NestedServletException e) {
				e.printStackTrace();
				result = "{\"success\":false,\"message\":\"系统错误,请联系开发人员!\"}";
			} catch (IllegalArgumentException e) {
				result = "{\"success\":false,\"message\":\"文件头损坏,请用office另存一份再上传!\"}";
				// e.printStackTrace();
			}
		}
		return result;
	}

	@RequestMapping("/exportExcel")
	public void exportExcel(@RequestParam(value = "ids") String ids, HttpServletRequest request,
                            HttpServletResponse response) {
		String exportPath = SysParamUtil.getParam("exportPath");
		String[] arr = ids.split(",");
		List<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; i < arr.length; i++) {
			idList.add(Integer.parseInt(arr[i]));
		}
		List<Shipment> list = shipmentService.selectShipmentList(idList);
		long time = System.currentTimeMillis();
		File file = new File(exportPath + time + ".xlsx");
		ExcelUtil.createExcel(file.getAbsolutePath(), list);
		response.setContentType("application/msexcel;charset=utf-8");
		// 文件名
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + new String(file.getName().getBytes(), "utf-8") + "\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentLength((int) file.length());
		byte[] buffer = new byte[4096];// 缓冲区
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			output = new BufferedOutputStream(response.getOutputStream());
			input = new BufferedInputStream(new FileInputStream(file));
			int n = -1;
			// 遍历，开始下载
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush(); // 不可少
			response.flushBuffer();// 不可少
		} catch (Exception e) {
			// 异常捕捉
			e.printStackTrace();
		} finally {
			// 关闭流，不可少
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if (file.exists()) {
			file.delete();// 删除文件
		}
	}


	@RequestMapping("/exportshipment")
	public void exportExcel(@RequestParam(value = "company", required = false) String company,
                            @RequestParam(value = "senttimeBegin", required = false) String senttimeBegin,
                            @RequestParam(value = "senttimeEnd", required = false) String senttimeEnd,
                            @RequestParam(value = "choiseType", required = false) String choiseType,
                            @RequestParam(value = "type", required = false) String type,
                            HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		//可重构抽取成一个方法
//		company = new String(company.getBytes("ISO-8859-1"), "UTF-8");
		List<Shipment> list = shipmentService.loadExportShipment(company, senttimeBegin, senttimeEnd, choiseType);
		String exportPath = SysParamUtil.getParam("exportPath");
		long time = System.currentTimeMillis();
		File file = new File(exportPath + time + ".xlsx");
		ExcelUtil.createExcel1(file.getAbsolutePath(), list,Integer.parseInt(choiseType),type);
		response.setContentType("application/msexcel;charset=utf-8");
		// 文件名
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + new String(file.getName().getBytes(), "utf-8") + "\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentLength((int) file.length());
		byte[] buffer = new byte[4096];// 缓冲区
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			output = new BufferedOutputStream(response.getOutputStream());
			input = new BufferedInputStream(new FileInputStream(file));
			int n = -1;
			// 遍历，开始下载
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush(); // 不可少
			response.flushBuffer();// 不可少
		} catch (Exception e) {
			// 异常捕捉
		} finally {
			// 关闭流，不可少
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if (file.exists()) {
			file.delete();// 删除文件
		}
	}



	// 判断文件是否存在,不存在则创建
	public Boolean isExist(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
		return true;
	}

	/**
	 * 加载运单信息
	 *
	 * @param company
	 *            运输公司
	 *            日期
	 * @param choiseType
	 *            选择类型
	 */
	@RequestMapping("/loadShipment")
	public void loadShipment(@RequestParam(value = "company", required = false) String company,
                             @RequestParam(value = "senttimeBegin", required = false) String senttimeBegin,
                             @RequestParam(value = "senttimeEnd", required = false) String senttimeEnd,
                             @RequestParam(value = "choiseType", required = false) String choiseType,
                             @RequestParam(value = "page", required = false) Integer page, HttpServletRequest request, HttpServletResponse response) {
		senttimeBegin=StringUtil.isNotBlank(senttimeBegin)?(senttimeBegin+" 00:00:00"):"";
		senttimeEnd=StringUtil.isNotBlank(senttimeEnd)?(senttimeEnd+" 23:59:59"):"";
		Map<String,String> map=new HashMap<String,String>();
        page=page>0?(page = (page - 1) * 40):page;
		map.put("company",company);
        map.put("senttimeBegin",senttimeBegin);
        map.put("senttimeEnd",senttimeEnd);
        map.put("choiseType",choiseType);
        map.put("page",String.valueOf(page));
        map.put("pageSize","40");
		DataGridResult res = shipmentService.loadShipment(map);
		WebTool.writeJson(SerializeUtil.ObjToJson(res), response);
	}

	@RequestMapping("/updateShipMentFlag")
	public void updateShipMentFlag(@RequestParam(value = "sm_id", required = false) String sm_id, @RequestParam(value = "flag_remark", required = false) String flag_remark, @RequestParam(value = "totalprice", required = false) String totalprice, HttpServletRequest request, HttpServletResponse response){
		String jsonResult = "";
		List<Integer> idList = new ArrayList<Integer>();
		if(sm_id==null || "".equals(sm_id)){
			jsonResult = "{\"success\":false,\"message\":\"人工判断可以支付运费失败!\"}";
		}else{
			idList.add(Integer.valueOf(sm_id));
			int res = shipmentService.updateShipMentFlag(idList,flag_remark,totalprice);
			if (res!=0) {
				jsonResult = "{\"success\":true,\"message\":\"人工判断可以支付运费成功!\"}";
			}else {
				jsonResult = "{\"success\":false,\"message\":\"人工判断可以支付运费失败!\"}";
			}
		}
		WebTool.writeJson(jsonResult, response);
	}

//	@RequestMapping("/batchUpdateShipMentFlag")
//	public void batchUpdateShipMentFlag(@RequestParam(value = "idStr", required = false) String idStr,HttpServletRequest request,HttpServletResponse response){
//		String[] idSplit = idStr.split(",");
//		List<Integer> idList = new ArrayList<Integer>();
//		for (String string : idSplit) {
//			idList.add(Integer.parseInt(string));
//		}
//		int res = shipmentService.updateShipMentFlag(idList);
//		String jsonResult = "";
//		if (res!=0) {
//			jsonResult = "{\"success\":true,\"message\":\"人工判断批量可以支付运费成功!\"}";
//		}else {
//			jsonResult = "{\"success\":false,\"message\":\"人工判断批量可以支付运费失败!\"}";
//		}
//		WebTool.writeJson(jsonResult, response);
//	}

	@RequestMapping("/delShipment")
	public void delShipment(@RequestParam(value = "idStr", required = false) String idStr, HttpServletRequest request, HttpServletResponse response){
		String[] idSplit = idStr.split(",");
		List<Integer> idList = new ArrayList<Integer>();
		for (String string : idSplit) {
			idList.add(Integer.parseInt(string));
		}
		int res = shipmentService.deleteShipment(idList);
		String jsonResult = "";
		if (res!=0) {
			jsonResult = "{\"success\":true,\"message\":\"运单信息删除成功!\"}";
		}else {
			jsonResult = "{\"success\":false,\"message\":\"运单信息删除失败!\"}";
		}
		WebTool.writeJson(jsonResult, response);
	}

	@RequestMapping("/insertSources")
	public void insertSources(@RequestParam(value = "amounts", required = false) String amounts, @RequestParam(value = "datas", required = false) String datas, @RequestParam(value = "comps", required = false) String comps, HttpServletRequest request, HttpServletResponse response){
		int res = shipmentService.insertSources(amounts,datas,comps);
		String jsonResult = "";
		if (res!=0) {
			jsonResult = "{\"success\":true,\"message\":\"录入物流赔偿款成功!\"}";
		}else {
			jsonResult = "{\"success\":false,\"message\":\"录入物流赔偿款失败!\"}";
		}
		WebTool.writeJson(jsonResult, response);
	}

	@RequestMapping("/PurchaseStatisticsInquiry")
	public void PurchaseStatisticsInquiry(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> maps = new HashMap<String, Object>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		String tj_days=request.getParameter("tj_days");
		map.put("tj_days", tj_days);
		List<String[]> list=shipmentService.PurchaseStatisticsInquiry(map);
		maps.put("list", list);
		WebTool.writeJson(JSONArray.fromObject(maps).toString(), response);
	}

	/**
	 *   过去30天完成出货的订单的 出货时间 和 客户下单时间的 平均时间间隔
	 */
	@RequestMapping("/shipmentCount")
	public void shipmentCount(HttpServletRequest request, HttpServletResponse response){
		DecimalFormat    df   = new DecimalFormat("######0.00");
		String paytimestart = request.getParameter("paytimestart");
		String paytimeend = request.getParameter("paytimeend");
		String days = request.getParameter("days");
		if(StrUtils.isNullOrEmpty(days)){
			days="0";
		}
		List<String[]> orderinfo = shipmentService.shipmentTimeCount(paytimestart, paytimeend,Integer.parseInt(days));

		double  avgTime =0; //平均时效（实际时效/总数）
		int     orderCount = 0 ; //总订单数（结果无重复订单总数）
		double  delaysRate = 0 ; //延迟占比（延迟数/总数）
		if(orderinfo.size()>0){
			orderCount =Integer.parseInt(orderinfo.get(orderinfo.size()-1)[0]); //总订单数
			avgTime = (double)Integer.parseInt(orderinfo.get(orderinfo.size()-1)[1])/orderCount ;
			delaysRate = (double)Integer.parseInt(orderinfo.get(orderinfo.size()-1)[2])/orderCount;
			orderinfo.remove(orderCount);
		}
		int avg = 0;
		if(orderinfo.size() > 0){
			for (int i = 0; i < orderinfo.size(); i++) {
				String de_time=orderinfo.get(i)[3];
				if(de_time.contains("-")){
					de_time=de_time.split("-")[1];
				}
				avg += Integer.parseInt(de_time);
			}
			avg = avg/orderinfo.size();
		}
		int res = 0;
		int order_count=shipmentService.getOrderCount();
		int details_count=shipmentService.getDetailsCount();
		int order_buy_time=shipmentService.getOrderBuyTime();
		int goods_buy_time=shipmentService.getGoodsbuytime();
		int ch_time=shipmentService.getChTime();
		Map<String, Object> map = new HashMap<String, Object>();
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		if(admJson == null){
			res = -1;
		}else{
			Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
			String strm=user.getRoletype();
			/*if("0".equals(strm)){
				res = 1;
			}*/
			res = 1;
		}
		request.setAttribute("res", 0);
		map.put("avgTime", df.format(avgTime));
		map.put("delaysRate",df.format(delaysRate));
		map.put("orderCount", orderCount);
		map.put("res", res);
		map.put("avg", avg);
		map.put("orderinfo", orderinfo);
		map.put("details_count", details_count);
		map.put("order_buy_time", order_buy_time/order_count);
		map.put("goods_buy_time",goods_buy_time/details_count);
		map.put("ch_time", ch_time/order_count);
		map.put("paytimestart", paytimestart=paytimestart==null||paytimestart==""?null:paytimestart);
		map.put("paytimeend", paytimeend);
		WebTool.writeJson(JSONArray.fromObject(map).toString(), response);
	}


	/**
	 * 显示前三个月的运费综合
	 *
	 * 从出库表查询数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showCalculFreight")
	public String  showCalculFreight(HttpServletRequest request, HttpServletResponse response){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		Date  now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);//把当前时间赋给日历
		calendar.add(calendar.MONTH, -3);  //设置为前3月
		Date dBefore = calendar.getTime();   //得到前3月的时间
		String  dBeforeTime = sdf.format(dBefore)+"  00:00:00";
		List<Shipment> list = shipmentService.showCalculFreight(dBeforeTime);
		List<String>  createtimeList = new ArrayList<String>();
		for(Shipment sp:list){
			String createtime = sdf.format(sp.getCreatetime()) ;
			if(!createtimeList.contains(createtime)){
				createtimeList.add(createtime);
			}
		}
		List<Shipment> finalList = new ArrayList<Shipment>();
		for(String str:createtimeList){
			Shipment sm = new Shipment();
			double   otherFreight =0.00;// new BigDecimal(0);
			double   totalPrice =0.00;//  new BigDecimal(0);
			for(Shipment shipment:list){
				if(sdf.format(shipment.getCreatetime()).equals(str)){
					sm.setPassFlag(shipment.getPassFlag());
					if(shipment.getTransportcompany()!=null){
						if(shipment.getTransportcompany().equalsIgnoreCase("emsinten")){
							sm.setEmsintenFreight(shipment.getTotalprice()==null?"0":shipment.getTotalprice());
						}else if(shipment.getTransportcompany().equalsIgnoreCase("jcex")){
							sm.setJecxFreight(shipment.getTotalprice()==null?"0":shipment.getTotalprice());
						}else if(shipment.getTransportcompany().equals("原飞航")){
							sm.setYfhFreight(shipment.getTotalprice()==null?"0":shipment.getTotalprice());
						}else{
							otherFreight += (StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.valueOf(shipment.getTotalprice()));
						}
						totalPrice +=StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.valueOf(shipment.getTotalprice());
					}else{
						otherFreight=(StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.valueOf(shipment.getTotalprice()));
						totalPrice=(StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.valueOf(shipment.getTotalprice()));
					}
				}
			}
			sm.setOtherFreight(String.valueOf(otherFreight));
			sm.setTotalprice(String.valueOf(totalPrice));
			sm.setCreateTime(str);
			finalList.add(sm);
		}
		request.setAttribute("finalList", finalList);
		return "finalFreight";
	}


	/**
	 * 每个月运费验证完成后,更新状态
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("check")
	@ResponseBody
	public  String  check(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String  createTime = request.getParameter("createTime");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		createTime = createTime+"-01 00:00:00";
		Calendar calendar = Calendar.getInstance();
		Date  date = sdf.parse(createTime);
		calendar.setTime(date);//把当前时间赋给日历
		calendar.add(calendar.MONTH, +1);
		Date dAfter = calendar.getTime();
		String  end = sdf.format(dAfter);
		int ret = shipmentService.check(createTime,end);
		return  String.valueOf(ret);
	}


	/**
	 * 导出前三个月的运费总额
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("exportAllFreight")
	public  void  exportAllFreight(HttpServletRequest request , HttpServletResponse response)  {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		Date  now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);//把当前时间赋给日历
		calendar.add(calendar.MONTH, -3);  //设置为前3月
		Date dBefore = calendar.getTime();   //得到前3月的时间
		String  dBeforeTime = sdf.format(dBefore)+"  00:00:00";
		List<Shipment> list = shipmentService.showCalculFreight(dBeforeTime);
		List<String>  createtimeList = new ArrayList<String>();
		for(Shipment sp:list){
			String createtime = sdf.format(sp.getCreatetime()) ;
			if(!createtimeList.contains(createtime)){
				createtimeList.add(createtime);
			}
		}
		List<Shipment> finalList = new ArrayList<Shipment>();
		for(String str:createtimeList){
			Shipment sm = new Shipment();
			double   otherFreight =0.00;
			double   totalPrice = 0.00;
			for(Shipment shipment:list){
				if(sdf.format(shipment.getCreatetime()).equals(str)){
					sm.setPassFlag(shipment.getPassFlag());
					if(shipment.getTransportcompany()!=null){
						if(shipment.getTransportcompany().equalsIgnoreCase("emsinten")){
							sm.setEmsintenFreight(shipment.getTotalprice()==null?"0":shipment.getTotalprice());
						}else if(shipment.getTransportcompany().equalsIgnoreCase("jcex")){
							sm.setJecxFreight(shipment.getTotalprice()==null?"0":shipment.getTotalprice());
						}else if(shipment.getTransportcompany().equals("原飞航")){
							sm.setYfhFreight(shipment.getTotalprice()==null?"0":shipment.getTotalprice());
						}else{
							otherFreight +=(StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.parseDouble(shipment.getTotalprice()));
						}
						totalPrice +=(StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.parseDouble(shipment.getTotalprice()));
					}else{
						otherFreight=(StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.parseDouble(shipment.getTotalprice()));
						totalPrice=(StringUtil.isBlank(shipment.getTotalprice())?0.00:Double.parseDouble(shipment.getTotalprice()));
					}
				}
			}
			sm.setOtherFreight(String.valueOf(otherFreight));
			sm.setTotalprice(String.valueOf(totalPrice));
			sm.setCreateTime(str);
			finalList.add(sm);
		}

		String exportPath = SysParamUtil.getParam("exportPath");
		long time = System.currentTimeMillis();
		File file = new File(exportPath + time + ".xlsx");
		ExcelUtil.createExcel2(file.getAbsolutePath(), finalList);
		response.setContentType("application/msexcel;charset=utf-8");
		// 文件名
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + new String(file.getName().getBytes(), "utf-8") + "\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentLength((int) file.length());
		byte[] buffer = new byte[4096];// 缓冲区
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			output = new BufferedOutputStream(response.getOutputStream());
			input = new BufferedInputStream(new FileInputStream(file));
			int n = -1;
			// 遍历，开始下载
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush(); // 不可少
			response.flushBuffer();// 不可少
		} catch (Exception e) {
			// 异常捕捉
		} finally {
			// 关闭流，不可少
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if (file.exists()) {
			file.delete();// 删除文件
		}
	}
}
