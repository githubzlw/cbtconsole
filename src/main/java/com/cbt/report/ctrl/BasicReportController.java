package com.cbt.report.ctrl;

import com.cbt.bean.OrderBean;
import com.cbt.refund.bean.PayPalImportInfo;
import com.cbt.report.service.BasicReportService;
import com.cbt.report.vo.*;
import com.cbt.util.*;
import com.cbt.warehouse.ctrl.NewOrderDetailsCtr;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;

import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping(value = "/basicStatistical")
public class BasicReportController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(BasicReportController.class);

	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_XLSX = "xlsx";

	@Autowired
	private BasicReportService basicReportService;

	/**
	 * 订单财务
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderFinancial")
	@ResponseBody
	public EasyUiJsonResult orderFinancial(HttpServletRequest request) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		List<OrderFinancialBean> financialLst = new ArrayList<OrderFinancialBean>();
		try {

			String yearStr = request.getParameter("year");
			// 获取年份参数
			if (yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim())
					|| "-1".equals(yearStr.trim())) {
				json.setSuccess(false);
				json.setMessage("获取年份败");
				return json;
			}

			// 获取月份参数
			String monthStr = request.getParameter("month");
			// int queryTotal = 1;
			if (monthStr == null || "".equals(monthStr.trim()) || "-1".equals(monthStr.trim())) {
				monthStr = "";
				// queryTotal = 12;
			}

			financialLst = basicReportService.queryOrderFinancial(yearStr, monthStr);
			/*
			 * if (financialLst.size() < queryTotal) { List<Map<String, String>>
			 * monthLst = Util.genQueryDate(yearStr, monthStr); for (Map<String,
			 * String> monthMap : monthLst) {
			 * basicReportService.insertOrderFinancial(monthMap.get("beginDate")
			 * , monthMap.get("endDate")); } financialLst.clear(); financialLst
			 * = basicReportService.queryOrderFinancial(yearStr, monthStr); }
			 */
			for (OrderFinancialBean ofb : financialLst) {
				ofb.setYear(ofb.getYear() + "-" + ofb.getMonth());
			}
			json.setRows(financialLst);
			json.setTotal(financialLst.size());
			json.setSuccess(true);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
			return json;
		}

	}

	@RequestMapping("/exportOrderFinancialExcel")
	@ResponseBody
	public void exportOrderFinancialExcel(HttpServletRequest request, HttpServletResponse response) {

		List<OrderFinancialBean> financialLst = new ArrayList<OrderFinancialBean>();
		OutputStream ouputStream = null;
		try {
			String yearStr = request.getParameter("year");
			// 获取年份参数
			if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()))) {
				// 获取月份参数
				String monthStr = request.getParameter("month");
				financialLst = basicReportService.queryOrderFinancial(yearStr, monthStr);
				HSSFWorkbook wb = genOrderFinancialExcle(financialLst);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment;filename=orderFinancial.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ouputStream != null) {
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private HSSFWorkbook genOrderFinancialExcle(List<OrderFinancialBean> financialLst) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单财务");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		// HSSFCell cell = row.createCell((short) 0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("年份");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("月份");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("本月实际销售额(R1)");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("财务销售额(R2)");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("系统PayPal总收入(V)");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("余额支付(Q)");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("本月新增加的余额之和(U)");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("余额补偿");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("客户充值");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("客户多付");
		cell.setCellStyle(style);
		cell = row.createCell(10);
		cell.setCellValue("订单取消(全部或部分)");
		cell.setCellStyle(style);
		cell = row.createCell(11);
		cell.setCellValue("余额变更(期末-期初)");
		cell.setCellStyle(style);
		cell = row.createCell(12);
		cell.setCellValue("余额提现(Z)");
		cell.setCellStyle(style);

		for (int i = 0; i < financialLst.size(); i++) {
			row = sheet.createRow((int) i + 1);
			OrderFinancialBean financial = (OrderFinancialBean) financialLst.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(financial.getYear());
			row.createCell(1).setCellValue(financial.getMonth());
			row.createCell(2).setCellValue(financial.getOrderSales());
			row.createCell(3).setCellValue(financial.getFinancialSales());
			row.createCell(4).setCellValue(financial.getPayPalRevenue());
			row.createCell(5).setCellValue(financial.getBalancePayment());
			row.createCell(6).setCellValue(financial.getNewAddBalance());
			row.createCell(7).setCellValue(financial.getBalanceCompensation());
			row.createCell(8).setCellValue(financial.getPayForBalance());
			row.createCell(9).setCellValue(financial.getOverPaymentBalance());
			row.createCell(10).setCellValue(financial.getOrderCancel());
			row.createCell(11).setCellValue(financial.getBalanceChanges());
			row.createCell(12).setCellValue(financial.getBalanceWithdrawal());
		}
		return wb;
	}

	/**
	 * 本月实际销售订单详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderSalesOrderInfo")
	@ResponseBody
	public EasyUiJsonResult orderSalesOrderInfo(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		// 获取年份参数
		String yearStr = request.getParameter("year");
		if (yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取年份败");
			return json;
		}
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取月份败");
			return json;
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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<OrderInfoBean> orderInfos = basicReportService.queryOrderSales(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum,0);
			int total = basicReportService.queryOrderSalesCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(orderInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 重新生成订单及销售额对账报表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/onloadOrderFinancialDate")
	public void onloadOrderFinancialDate(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int row=1;
		PrintWriter out = response.getWriter();
		try{
			StringBuilder sb=new StringBuilder();
			Map<String,String> map=new HashMap<String,String>(5);
			String year=request.getParameter("year");
			String month=request.getParameter("month");
			map.put("month",month);
			map.put("year",year);
			basicReportService.onloadOrderFinancialDate(map);
		}catch (Exception e){
			row=1;
		}
		out.print(row);
		out.close();
	}

	@RequestMapping("/exportOrderSalesExcel")
	@ResponseBody
	public void exportOrderSalesExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<OrderInfoBean> orderInfos = basicReportService.queryOrderSales(monthLst.get(0).get("beginDate"),
						monthLst.get(0).get("endDate"), 0, 0,1);
				HSSFWorkbook wb = genOrderSales(yearStr, monthStr, orderInfos);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-orderSales.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private HSSFWorkbook genOrderSales(String yearStr, String monthStr, List<OrderInfoBean> orderInfos) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(yearStr + "年" + monthStr + "月份实际销售额详情");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("客户id");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("客户邮箱地址");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("实际支付金额(USD)");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("实际采购金额(USD)");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("实际运费金额(USD)");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("出运号");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("订单利润(USD)");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("订单创建时间");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("订单状态");
		cell.setCellStyle(style);
		cell = row.createCell(10);
		cell.setCellValue("订单支付时间");
		cell.setCellStyle(style);

		for (int i = 0; i < orderInfos.size(); i++) {
			row = sheet.createRow(i + 1);
			OrderInfoBean oi = (OrderInfoBean) orderInfos.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(oi.getOrderNo());
			row.createCell(1).setCellValue(oi.getUserId());
			row.createCell(2).setCellValue(oi.getUserEmail());
			row.createCell(3).setCellValue(oi.getPayPrice());
			row.createCell(4).setCellValue(oi.getBuyAmount());
			row.createCell(5).setCellValue(oi.getFrightAmount());
			row.createCell(6).setCellValue(oi.getExpressNo());
			row.createCell(7).setCellValue(oi.getProfit());
			row.createCell(8).setCellValue(DateFormatUtil.getWithSeconds(oi.getCreateTime()));
			if ("0".equals(oi.getState())) {
				row.createCell(9).setCellValue("未支付订单");
			} else if ("1".equals(oi.getState())) {
				row.createCell(9).setCellValue("采购中");
			} else if ("2".equals(oi.getState())) {
				row.createCell(9).setCellValue("入库");
			} else if ("3".equals(oi.getState())) {
				row.createCell(9).setCellValue("出运");
			} else if ("4".equals(oi.getState())) {
				row.createCell(9).setCellValue("完结");
			} else if ("5".equals(oi.getState())) {
				row.createCell(9).setCellValue("订单审核");
			} else if ("6".equals(oi.getState())) {
				row.createCell(9).setCellValue("客户取消");
			} else if ("-1".equals(oi.getState())) {
				row.createCell(9).setCellValue("系统取消");
			} else {
				row.createCell(9).setCellValue("");
			}
			row.createCell(10).setCellValue(DateFormatUtil.getWithSeconds(oi.getOrderPayTime()));
		}
		return wb;
	}

	/**
	 * 电汇总收入详情查看
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryWireRevenuePayPalInfo")
	@ResponseBody
	public EasyUiJsonResult queryWireRevenuePayPalInfo(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		// 获取年份参数
		String yearStr = request.getParameter("year");
		if (yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取年份败");
			return json;
		}
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取月份败");
			return json;
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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<PayPalInfoBean> paypalInfos = basicReportService.queryWireRevenue(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum);
			int total = basicReportService.queryWireRevenueCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(paypalInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 每月的PayPal总收入详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryPayPalRevenuePayPalInfo")
	@ResponseBody
	public EasyUiJsonResult queryPayPalRevenuePayPalInfo(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		// 获取年份参数
		String yearStr = request.getParameter("year");
		if (yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取年份败");
			return json;
		}
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取月份败");
			return json;
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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<PayPalInfoBean> paypalInfos = basicReportService.queryPayPalRevenue(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum);
			int total = basicReportService.queryPayPalRevenueCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(paypalInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportPayPalRevenueExcel")
	@ResponseBody
	public void exportPayPalRevenueExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<PayPalInfoBean> paypalInfos = basicReportService
						.queryPayPalRevenue(monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), 0, 0);
				HSSFWorkbook wb = genPaymentInfoExcel(paypalInfos, yearStr + "年" + monthStr + "系统PayPal收入详情");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-paypalRevenue.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@RequestMapping(value = "/queryStripPayInfo")
	@ResponseBody
	public EasyUiJsonResult queryStripPayInfo(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		// 获取年份参数
		String yearStr = request.getParameter("year");
		if (yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取年份败");
			return json;
		}
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取月份败");
			return json;
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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<PayPalInfoBean> paypalInfos = basicReportService.queryStripPayInfo(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum);
			int total = basicReportService.queryStripPayInfoCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(paypalInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportStripPayExcel")
	@ResponseBody
	public void exportStripPayExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<PayPalInfoBean> paypalInfos = basicReportService
						.queryStripPayInfo(monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), 0, 0);
				HSSFWorkbook wb = genPaymentInfoExcel(paypalInfos, yearStr + "年" + monthStr + "stripe收入详情");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-stripePay.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private HSSFWorkbook genPaymentInfoExcel(List<PayPalInfoBean> paypalInfos, String title) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(title);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("客户id");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("付款金额");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("付款币种");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("支付标识");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("支付类别");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("交易号");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);

		for (int i = 0; i < paypalInfos.size(); i++) {
			row = sheet.createRow((int) i + 1);
			PayPalInfoBean pp = (PayPalInfoBean) paypalInfos.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(pp.getOrderid());
			row.createCell(1).setCellValue(pp.getUserId());
			row.createCell(2).setCellValue(pp.getPaymentAmount());
			row.createCell(3).setCellValue(pp.getPaymentCc());
			if ("A".equals(pp.getPayFlag())) {
				row.createCell(4).setCellValue("客户充值");
			} else if ("O".equals(pp.getPayFlag())) {
				row.createCell(4).setCellValue("订单支付");
			} else if ("Y".equals(pp.getPayFlag())) {
				row.createCell(4).setCellValue("运费支付");
			} else if ("N".equals(pp.getPayFlag())) {
				row.createCell(4).setCellValue("修改商品价格后的订单支付金额");
			} else {
				row.createCell(4).setCellValue("");
			}
			if ("0".equals(pp.getPayType())) {
				row.createCell(5).setCellValue("paypal支付");
			} else if ("1".equals(pp.getPayType())) {
				row.createCell(5).setCellValue("WireTransfer支付");
			} else if ("2".equals(pp.getPayType())) {
				row.createCell(5).setCellValue("余额支付");
			} else if ("3".equals(pp.getPayType())) {
				row.createCell(5).setCellValue("订单拆分");
			} else if ("4".equals(pp.getPayType())) {
				row.createCell(5).setCellValue("合并支付");
			} else if ("5".equals(pp.getPayType())) {
				row.createCell(5).setCellValue("Stripe支付");
			} else {
				row.createCell(5).setCellValue("");
			}
			row.createCell(6).setCellValue(pp.getPayNo());
			row.createCell(7).setCellValue(DateFormatUtil.getWithSeconds(pp.getCreateTime()));
		}
		return wb;
	}

	/**
	 * 每月的总余额支付详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryBalancePaymentInfo")
	@ResponseBody
	public EasyUiJsonResult queryBalancePaymentInfo(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<PayPalInfoBean> paypalInfos = basicReportService.queryBalancePayment(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum);
			int total = basicReportService.queryBalancePaymentCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(paypalInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportBalancePaymentExcel")
	@ResponseBody
	public void exportBalancePaymentExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<PayPalInfoBean> paypalInfos = basicReportService
						.queryBalancePayment(monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), 0, 0);
				HSSFWorkbook wb = genPaymentInfoExcel(paypalInfos, yearStr + "年" + monthStr + "余额支付详情");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-balancePayment.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 客户余额变更查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/customerBalanceChange")
	@ResponseBody
	public EasyUiJsonResult customerBalanceChange(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<CustomerBalanceChangeBean> balanceChanges = basicReportService.queryCustomerBalanceChange(
					monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum,
					pageNum);
			int total = basicReportService.queryCustomerBalancesChangeCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			for (CustomerBalanceChangeBean cmbl : balanceChanges) {
				cmbl.setYear(monthLst.get(0).get("year"));
				cmbl.setMonth(monthLst.get(0).get("month"));
			}
			json.setRows(balanceChanges);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportBalanceChangeExcel")
	@ResponseBody
	public void exportBalanceChangeExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<CustomerBalanceChangeBean> balanceChanges = basicReportService.queryCustomerBalanceChange(
						monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), 0, 0);
				for (CustomerBalanceChangeBean cmbl : balanceChanges) {
					cmbl.setYear(monthLst.get(0).get("year"));
					cmbl.setMonth(monthLst.get(0).get("month"));
				}
				HSSFWorkbook wb = genBalanceChangeExcel(balanceChanges, yearStr + "年" + monthStr + "客户余额变更详情");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-balanceChange.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private HSSFWorkbook genBalanceChangeExcel(List<CustomerBalanceChangeBean> balanceChanges, String title) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(title);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("年份");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("月份");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("客户id");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("变更余额(USD)=月末余额-月初余额");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("当前余额(USD)");
		cell.setCellStyle(style);

		for (int i = 0; i < balanceChanges.size(); i++) {
			row = sheet.createRow((int) i + 1);
			CustomerBalanceChangeBean bc = (CustomerBalanceChangeBean) balanceChanges.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(bc.getYear());
			row.createCell(1).setCellValue(bc.getMonth());
			row.createCell(2).setCellValue(bc.getUserId());
			row.createCell(3).setCellValue(bc.getChangeBalance());
			row.createCell(4).setCellValue(bc.getCurrentBalance());
		}
		return wb;
	}

	/**
	 * 客户余额补偿查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/balanceCompensation")
	@ResponseBody
	public EasyUiJsonResult balanceCompensation(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<BalanceCompensation> balanceCss = basicReportService.queryBalanceCompensation(
					monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum,
					pageNum);
			int total = basicReportService.queryBalanceCompensationCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			for (BalanceCompensation bcs : balanceCss) {
				bcs.setYear(monthLst.get(0).get("year"));
				bcs.setMonth(monthLst.get(0).get("month"));
			}
			json.setRows(balanceCss);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportBalanceCompensationExcel")
	@ResponseBody
	public void exportBalanceCompensationExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<BalanceCompensation> balanceCss = basicReportService.queryBalanceCompensation(
						monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), 0, 0);
				for (BalanceCompensation bcs : balanceCss) {
					bcs.setYear(monthLst.get(0).get("year"));
					bcs.setMonth(monthLst.get(0).get("month"));
				}
				HSSFWorkbook wb = genBalanceCompensationExcel(balanceCss, yearStr + "年" + monthStr + "客户余额补偿详情");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-balanceCompensation.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private HSSFWorkbook genBalanceCompensationExcel(List<BalanceCompensation> balanceCss, String title) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(title);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("年份");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("月份");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("客户id");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("补偿金额(USD)");
		cell.setCellStyle(style);

		for (int i = 0; i < balanceCss.size(); i++) {
			row = sheet.createRow((int) i + 1);
			BalanceCompensation bcs = (BalanceCompensation) balanceCss.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(bcs.getYear());
			row.createCell(1).setCellValue(bcs.getMonth());
			row.createCell(2).setCellValue(bcs.getUserId());
			row.createCell(3).setCellValue(bcs.getAmount());
		}
		return wb;
	}

	/**
	 * 客户余额补偿查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderCancel")
	@ResponseBody
	public EasyUiJsonResult orderCancel(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<OrderCancelBean> orderCancels = basicReportService.queryOrderCancel(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum);
			int total = basicReportService.queryOrderCancelCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			for (OrderCancelBean ocb : orderCancels) {
				ocb.setYear(monthLst.get(0).get("year"));
				ocb.setMonth(monthLst.get(0).get("month"));
			}
			json.setRows(orderCancels);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportOrderCancelExcel")
	@ResponseBody
	public void exportOrderCancelExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<OrderCancelBean> orderCancels = basicReportService
						.queryOrderCancel(monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), 0, 0);
				for (OrderCancelBean ocb : orderCancels) {
					ocb.setYear(monthLst.get(0).get("year"));
					ocb.setMonth(monthLst.get(0).get("month"));
				}
				HSSFWorkbook wb = genOrderCancelExcel(orderCancels, yearStr + "年" + monthStr + "订单取消(全部或部分)详情");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-orderCancel.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private HSSFWorkbook genOrderCancelExcel(List<OrderCancelBean> orderCancels, String title) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(title);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("年份");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("月份");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("客户id");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("订单取消金额(USD)");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("备注信息");
		cell.setCellStyle(style);

		for (int i = 0; i < orderCancels.size(); i++) {
			row = sheet.createRow((int) i + 1);
			OrderCancelBean ocb = (OrderCancelBean) orderCancels.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(ocb.getYear());
			row.createCell(1).setCellValue(ocb.getMonth());
			row.createCell(2).setCellValue(ocb.getUserId());
			row.createCell(3).setCellValue(ocb.getAmount());
			row.createCell(4).setCellValue(ocb.getRemarkId());
			row.createCell(5).setCellValue(ocb.getRemark());
		}
		return wb;
	}

	/**
	 * 每月的总余额提现详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryBalanceWithdrawalInfo")
	@ResponseBody
	public EasyUiJsonResult queryBalanceWithdrawalInfo(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<RefundInfoBean> refundlInfos = basicReportService.queryBalanceWithdrawal(
					monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum,
					pageNum);
			for (RefundInfoBean rfi : refundlInfos) {
				rfi.setYear(monthLst.get(0).get("year"));
				rfi.setMonth(monthLst.get(0).get("month"));
			}
			int total = basicReportService.queryBalanceWithdrawalCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(refundlInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportBalanceWithdrawalExcel")
	@ResponseBody
	public void exportBalanceWithdrawalExcel(HttpServletRequest request, HttpServletResponse response) {

		// 获取年份参数
		String yearStr = request.getParameter("year");
		// 获取月份参数
		String monthStr = request.getParameter("month");
		if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
				|| monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
				|| "-1".equals(monthStr.trim()))) {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			OutputStream ouputStream = null;
			try {
				List<RefundInfoBean> refundlInfos = basicReportService
						.queryBalanceWithdrawal(monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), 0, 0);
				for (RefundInfoBean rfi : refundlInfos) {
					rfi.setYear(monthLst.get(0).get("year"));
					rfi.setMonth(monthLst.get(0).get("month"));
				}
				HSSFWorkbook wb = genBalanceWithdrawalExcel(refundlInfos, yearStr + "年" + monthStr + "余额提现详情");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=" + yearStr + "-" + monthStr + "-balanceWithdrawal.xls");
				response.setCharacterEncoding("utf-8");
				ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ouputStream != null) {
						ouputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private HSSFWorkbook genBalanceWithdrawalExcel(List<RefundInfoBean> refundlInfos, String title) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(title);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("年份");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("月份");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("客户id");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("提现金额(USD)");

		for (int i = 0; i < refundlInfos.size(); i++) {
			row = sheet.createRow((int) i + 1);
			RefundInfoBean rfi = (RefundInfoBean) refundlInfos.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(rfi.getYear());
			row.createCell(1).setCellValue(rfi.getMonth());
			row.createCell(2).setCellValue(rfi.getUserId());
			row.createCell(3).setCellValue(rfi.getRefundAmount());
		}
		return wb;
	}

	/**
	 * PayPal申述订单退款
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderRefund")
	@ResponseBody
	public EasyUiJsonResult orderRefund(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		// 获取年份参数
		if (yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())) {
			json.setSuccess(false);
			json.setMessage("获取年份败");
			return json;
		}
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<RefundInfoBean> paypalRefunds = basicReportService.queryPaypalRefunds(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum);
			int total = basicReportService.queryPaypalRefundsCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			for (RefundInfoBean pprf : paypalRefunds) {
				pprf.setYear(monthLst.get(0).get("year"));
				pprf.setMonth(monthLst.get(0).get("month"));
			}
			json.setRows(paypalRefunds);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 导出退款财务信息
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping("/exportPayPalRefundExcel")
	@ResponseBody
	public void exportPayPalRefundExcel(HttpServletRequest request, HttpServletResponse response) {

		OutputStream ouputStream = null;
		try {
			String yearStr = request.getParameter("year");
			String monthStr = request.getParameter("month");

			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			List<RefundInfoBean> paypalRefunds = basicReportService.queryPaypalRefunds(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), 0, 0);
			for (RefundInfoBean pprf : paypalRefunds) {
				pprf.setYear(monthLst.get(0).get("year"));
				pprf.setMonth(monthLst.get(0).get("month"));
			}
			HSSFWorkbook wb = genPayPalRefundExcle(paypalRefunds);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=paypalRefund.xls");
			response.setCharacterEncoding("utf-8");
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ouputStream != null) {
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private HSSFWorkbook genPayPalRefundExcle(List<RefundInfoBean> paypalRefunds) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("PayPal申述退款");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		// HSSFCell cell = row.createCell((short) 0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("年份");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("月份");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("订单支付时间");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("订单实际支付金额(USD)");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("退款时间");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("退款金额(USD)");
		cell.setCellStyle(style);

		for (int i = 0; i < paypalRefunds.size(); i++) {
			row = sheet.createRow((int) i + 1);
			RefundInfoBean ppRefund = (RefundInfoBean) paypalRefunds.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(ppRefund.getYear());
			row.createCell(1).setCellValue(ppRefund.getMonth());
			row.createCell(2).setCellValue(ppRefund.getOrderNo());
			row.createCell(3).setCellValue(ppRefund.getPayTime().toString());
			row.createCell(4).setCellValue(ppRefund.getPayAmount());
			row.createCell(5).setCellValue(ppRefund.getRefundTime().toString());
			row.createCell(6).setCellValue(ppRefund.getRefundAmount());
		}
		row = sheet.createRow((int) paypalRefunds.size() + 1);
		row.createCell(0).setCellValue("备注:本报表中金额单位全部是USD");
		return wb;
	}

	/**
	 * 导入退款信息到refund表记录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/importPayPalRefundExcel")
	@ResponseBody
	public void importPayPalRefundExcel(HttpServletRequest request, HttpServletResponse response) {

		OutputStream ouputStream = null;
		try {
			String yearStr = request.getParameter("year");
			String monthStr = request.getParameter("month");

			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			List<RefundInfoBean> paypalRefunds = basicReportService.queryPaypalRefunds(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), 0, 0);
			for (RefundInfoBean pprf : paypalRefunds) {
				pprf.setYear(monthLst.get(0).get("year"));
				pprf.setMonth(monthLst.get(0).get("month"));
			}
			HSSFWorkbook wb = genPayPalRefundExcle(paypalRefunds);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=paypalRefund.xls");
			response.setCharacterEncoding("utf-8");
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ouputStream != null) {
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 每月的未启动订单详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryNotStartedOrderAmount")
	@ResponseBody
	public EasyUiJsonResult queryNotStartedOrderAmount(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<PayPalInfoBean> paypalInfos = basicReportService.queryNotStartedOrderAmount(
					monthLst.get(0).get("beginDate"), monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum,
					pageNum);
			int total = basicReportService.queryNotStartedOrderAmountCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(paypalInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 每月的PayPal总退款详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryTotalRefundInfo")
	@ResponseBody
	public EasyUiJsonResult queryTotalRefundInfo(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
		try {
			List<RefundInfoBean> refundInfos = basicReportService.queryTotalRefund(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"), (stateNum - 1) * pageNum, pageNum);
			int total = basicReportService.queryTotalRefundCount(monthLst.get(0).get("beginDate"),
					monthLst.get(0).get("endDate"));

			json.setRows(refundInfos);
			json.setTotal(total);
			json.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}



	@RequestMapping("/exportTotalRefundExcel")
	@ResponseBody
	public void exportTotalRefundExcel(HttpServletRequest request, HttpServletResponse response) {

		List<RefundInfoBean> totalRfLst = new ArrayList<RefundInfoBean>();
		List<Map<String, String>> monthLst = genQueryDaylyDate();
		OutputStream ouputStream = null;
		try {
			for (Map<String, String> ym : monthLst) {
				RefundInfoBean rf = new RefundInfoBean();
				float total = 0;
				List<RefundInfoBean> rfCld = basicReportService.queryTotalRefund(ym.get("beginDate"), ym.get("endDate"),
						0, 5000);
				if (rfCld.size() > 0) {
					for (RefundInfoBean ppf : rfCld) {
						total += Float.valueOf(ppf.getRefundAmount());
					}
				}
				rf.setYear(ym.get("endDate").substring(5, 10));
				rf.setRefundAmount(total);
				totalRfLst.add(rf);
			}
			HSSFWorkbook wb = genTotalRefund(totalRfLst);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=month7payment.xls");
			response.setCharacterEncoding("utf-8");
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ouputStream != null) {
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private HSSFWorkbook genTotalRefund(List<RefundInfoBean> totalRfLst) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("7月份退款统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		// HSSFCell cell = row.createCell((short) 0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("总金额");
		cell.setCellStyle(style);

		for (int i = 0; i < totalRfLst.size(); i++) {
			row = sheet.createRow((int) i + 1);
			RefundInfoBean pp = (RefundInfoBean) totalRfLst.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(pp.getYear());
			row.createCell(1).setCellValue(pp.getRefundAmount());
		}
		return wb;
	}

	/**
	 * 
	 * @Title queryEditedProductProfits
	 * @Description 编辑商品利润查询
	 * @param request
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/queryEditedProductProfits")
	@ResponseBody
	public EasyUiJsonResult queryEditedProductProfits(HttpServletRequest request) {

		EasyUiJsonResult json = new EasyUiJsonResult();
		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");

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

		String sortingStr = request.getParameter("sorting");
		int sorting = 0;
		if (!(sortingStr == null || "".equals(sortingStr))) {
			sorting = Integer.valueOf(sortingStr);
		}
		try {
			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			List<EditedProductProfits> productProfits = basicReportService.queryEditedProductProfits(
					monthLst.get(0).get("beginDate"), monthLst.get(monthLst.size() - 1).get("endDate"),
					(stateNum - 1) * pageNum, pageNum, sorting,Util.EXCHANGE_RATE);
			int total = basicReportService.queryEditedProductProfitsCount(monthLst.get(0).get("beginDate"),
					monthLst.get(monthLst.size() - 1).get("endDate"));
			NewOrderDetailsCtr newOrder =new NewOrderDetailsCtr();
			List<EditedProductProfitsFooter> footerLst = new ArrayList<EditedProductProfitsFooter>();
			EditedProductProfitsFooter footer = new EditedProductProfitsFooter();
			float totalProfit = 0;
			for (EditedProductProfits ppt : productProfits) {
				IOrderwsServer server = new OrderwsServer();
				//String allFreightTem = String.valueOf(server.getAllFreightByOrderid(ppt.getOrderNo()));
				OrderBean orderInfo = server.getOrders(ppt.getOrderNo());
				double freightFee = orderInfo.getFreightFee();
				//newOrder.getFreightFee(String.valueOf(ppt.getIntenetFreight()), orderInfo);
				//ppt.setIntenetFreight(Float.parseFloat(String.valueOf(freightFee)));
				totalProfit += ppt.getGrossProfit();
			}
			footer.setOrderPrice(totalProfit);
			footerLst.add(footer);

			json.setRows(productProfits);
			json.setTotal(total);
			json.setSuccess(true);
			json.setFooter(footerLst);

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			LOG.error("查询失败，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/exportEditedProductProfitsExcel")
	@ResponseBody
	public void exportEditedProductProfitsExcel(HttpServletRequest request, HttpServletResponse response) {

		String yearStr = request.getParameter("year");
		String monthStr = request.getParameter("month");
		String sortingStr = request.getParameter("sorting");
		int sorting = 0;
		if (!(sortingStr == null || "".equals(sortingStr))) {
			sorting = Integer.valueOf(sortingStr);
		}

		OutputStream ouputStream = null;
		try {

			List<Map<String, String>> monthLst = Util.genQueryDate(yearStr, monthStr);
			List<EditedProductProfits> productProfits = basicReportService.queryEditedProductProfits(
					monthLst.get(0).get("beginDate"), monthLst.get(monthLst.size() - 1).get("endDate"), 0, 0, sorting,Util.EXCHANGE_RATE);
			float totalProfit = 0;
			for (EditedProductProfits ppt : productProfits) {
				totalProfit += ppt.getGrossProfit();
			}
			HSSFWorkbook wb = genProductProfitsExcel(productProfits, totalProfit);
			response.setContentType("application/vnd.ms-excel");
			String showStr = "attachment;filename=";
			if (!(monthStr == null || "".equals(monthStr))) {
				showStr +=  yearStr + "-" + monthStr + "-editedProductProfits.xls";
			} else {
				showStr +=  yearStr + "-" + "productProfits.xls";
			}
			response.setHeader("Content-disposition", showStr);
			response.setCharacterEncoding("utf-8");
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ouputStream != null) {
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private HSSFWorkbook genProductProfitsExcel(List<EditedProductProfits> productProfits, float totalProfit) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		// HSSFCell cell = row.createCell((short) 0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("商品PID");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("客户ID");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("下单时间");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("商品编辑人");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("商品下单金额");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("商品采购金额");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("商品国际运费");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("商品毛利润");
		cell.setCellStyle(style);

		for (int i = 0; i < productProfits.size(); i++) {
			row = sheet.createRow((int) i + 1);
			EditedProductProfits ppt = (EditedProductProfits) productProfits.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(ppt.getPid());
			row.createCell(1).setCellValue(ppt.getUserId());
			row.createCell(2).setCellValue(ppt.getOrderNo());
			row.createCell(3).setCellValue(DateFormatUtil.getWithSeconds(ppt.getCreateTime())
					.substring(0, 10));
			row.createCell(4).setCellValue(ppt.getAdminName());
			row.createCell(5).setCellValue(ppt.getOrderPrice());
			row.createCell(6).setCellValue(ppt.getPurchasePrice());
			row.createCell(7).setCellValue(ppt.getIntenetFreight());
			row.createCell(8).setCellValue(ppt.getGrossProfit());
		}
		row = sheet.createRow(productProfits.size() + 2);
		row.createCell(0).setCellValue("单位(RMB),总毛利润：" + totalProfit);
		return wb;
	}

	private List<Map<String, String>> genQueryDaylyDate() {
		List<Map<String, String>> monthLst = new ArrayList<Map<String, String>>();

		for (int i = 1; i <= 31; i++) {
			Map<String, String> dataMap = new HashMap<String, String>();
			if (i < 10) {
				dataMap.put("beginDate", "2017-07-0" + i + " 00:00:00");
				dataMap.put("endDate", "2017-07-0" + i + " 23:59:59");
			} else {
				dataMap.put("beginDate", "2017-07-" + i + " 00:00:00");
				dataMap.put("endDate", "2017-07-" + i + " 23:59:59");
			}
			monthLst.add(dataMap);
		}

		return monthLst;
	}

	@RequestMapping("/uploadExcel")
	@ResponseBody
	public JsonResult upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null) {
			json.setOk(false);
			json.setMessage("获取登录用户信息失败");
			return json;
		}

		List<String> remoteFlies = genRemoteFile(request);
		List<PayPalImportInfo> infos = new ArrayList<PayPalImportInfo>();
		if (remoteFlies == null || remoteFlies.size() == 0) {
			json.setOk(false);
			json.setMessage("上传文件失败，终止执行，请确认文件是否是xlsx或者xls的后缀");
		} else {
			for (String filePath : remoteFlies) {
				List<PayPalImportInfo> currenInfos = genPayPalInfo(filePath, user.getId());
				if (!(currenInfos == null || currenInfos.size() == 0)) {
					infos.addAll(currenInfos);
				}
			}
			if (infos.size() > 0) {
				try {
					basicReportService.batchSavePayPalInfoByExcel(infos);
					json.setOk(true);
					json.setMessage("上传文件成功，本次导入数量：" + infos.size());
				} catch (Exception e) {
					e.printStackTrace();
					json.setOk(true);
					json.setMessage("保存失败，原因：" + e.getMessage());
					System.out.println("保存失败，原因：" + e.getMessage());
				}

			} else {
				json.setOk(false);
				json.setMessage("生成数据失败，终止执行，请重试");
			}
		}

		return json;

	}

	private List<String> genRemoteFile(MultipartHttpServletRequest request) {

		// 获取当前项目的根目录
		String savePath = request.getSession().getServletContext().getRealPath("/");
		// 获取当前Tomcat的根目录
		// String savePath = System.getProperty("user.dir");
		savePath = savePath.replaceAll("\\\\", "/");
		List<String> remoteFlies = new ArrayList<String>();

		// 获取上传的所有文件名
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {
			// 取出文件
			mpf = request.getFile(itr.next());
			LOG.info(mpf.getOriginalFilename() + " 开始上传!");
			if (!mpf.isEmpty()) {
				FileOutputStream fops = null;
				try {
					String orFileName = mpf.getOriginalFilename();
					String suffix = orFileName.substring(orFileName.lastIndexOf("."));
					if (".xls".equalsIgnoreCase(suffix) || ".xlsx".equalsIgnoreCase(suffix)) {
						// 得到文件保存的名称(防止文件名称冲突)
						String saveFilename = makeFileName(orFileName);
						// 得到文件的保存目录
						String realSavePath = savePath + "/financeExcel/" + saveFilename;
						// 判断路径是否存在，不存在创建目录
						File savePathDir = new File(savePath + "/financeExcel");
						if (!savePathDir.exists()) {
							savePathDir.mkdirs();
						}
						// 开始文件流的输入
						File uploadFile = new File(realSavePath);

						fops = new FileOutputStream(uploadFile);
						fops.write(mpf.getBytes());
						fops.flush();

						// 放入列表集合
						remoteFlies.add(realSavePath);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fops != null) {
						try {
							fops.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return remoteFlies;
	}

	private String makeFileName(String filename) { //
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString() + "_" + filename;
	}

	private List<PayPalImportInfo> genPayPalInfo(String filePath, int userId) {
		List<PayPalImportInfo> infos = new ArrayList<PayPalImportInfo>();

		Workbook workbook = null;
		InputStream is = null;
		try {

			is = new FileInputStream(filePath);
			if (filePath.endsWith(EXTENSION_XLS)) {
				workbook = (Workbook) new HSSFWorkbook(is);
			} else if (filePath.endsWith(EXTENSION_XLSX)) {
				workbook = new XSSFWorkbook(is);
			}

			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				// 读取数据行
				int currCount = 0;
				for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					if (currentRow == null) {
						continue;
					}
					if (currentRow.getCell(0) == null || currentRow.getCell(1) == null) {
						continue;
					}
					currCount++;
					try {

						PayPalImportInfo ppInfo = new PayPalImportInfo();
						ppInfo.setCreateAdminid(userId);

						System.out.print("	发生日期:" + currentRow.getCell(0).getDateCellValue());
						ppInfo.setOccurrenceDate(DateFormatUtil.getWithSeconds(currentRow.getCell(0).getDateCellValue())
								.substring(0, 10));

						System.out.print("	发生时间:" + (new Date((currentRow.getCell(0).getDateCellValue().getTime()
								+ currentRow.getCell(1).getDateCellValue().getTime()))));
						ppInfo.setOccurrenceTime(DateFormatUtil
								.getWithSeconds(new Date((currentRow.getCell(0).getDateCellValue().getTime()
										+ currentRow.getCell(1).getDateCellValue().getTime())))
								.substring(11));

						System.out.print("	时区:" + getCellValue(currentRow.getCell(2), true, false).trim());
						ppInfo.setTimeZone(getCellValue(currentRow.getCell(2), true, false).trim());

						System.out.print("	客户名称:" + getCellValue(currentRow.getCell(3), true, false).trim());
						ppInfo.setCustomerName(getCellValue(currentRow.getCell(3), true, false).trim());

						System.out.print("	PayPal类型:" + getCellValue(currentRow.getCell(4), true, false).trim());
						ppInfo.setType(getCellValue(currentRow.getCell(4), true, false).trim());

						System.out.print("	PayPal状态:" + getCellValue(currentRow.getCell(5), true, false).trim());
						ppInfo.setState(getCellValue(currentRow.getCell(5), true, false).trim());

						System.out.print("	币种:" + getCellValue(currentRow.getCell(6), true, false).trim());
						ppInfo.setMonetaryUnit(getCellValue(currentRow.getCell(6), true, false).trim());

						System.out.print("	总额:" + getCellValue(currentRow.getCell(7), true, true).trim());
						ppInfo.setTotalAmount(Float.valueOf(getCellValue(currentRow.getCell(7), true, true).trim()));

						System.out.print("	手续费:" + getCellValue(currentRow.getCell(8), true, true).trim());
						ppInfo.setServiceCharge(Float.valueOf(getCellValue(currentRow.getCell(8), true, true).trim()));

						System.out.print("	净额:" + getCellValue(currentRow.getCell(9), true, true).trim());
						ppInfo.setNetAmount(Float.valueOf(getCellValue(currentRow.getCell(9), true, true).trim()));

						System.out.println("	发件人邮箱地址:" + getCellValue(currentRow.getCell(10), true, false).trim());
						ppInfo.setSenderEmail(getCellValue(currentRow.getCell(10), true, false).trim());

						System.out.print("	收件人邮箱 :" + getCellValue(currentRow.getCell(11), true, false).trim());
						ppInfo.setRecipientEmail(getCellValue(currentRow.getCell(11), true, false).trim());

						System.out.print("	交易号 :" + getCellValue(currentRow.getCell(12), true, false).trim());
						ppInfo.setTransactionNumber(getCellValue(currentRow.getCell(12), true, false).trim());

						System.out.print("	送货地址:" + getCellValue(currentRow.getCell(13), true, false).trim());
						ppInfo.setDeliveryAddress(getCellValue(currentRow.getCell(13), true, false).trim());

						System.out.print("	地址状态:" + getCellValue(currentRow.getCell(14), true, false).trim());
						ppInfo.setAddressState(getCellValue(currentRow.getCell(14), true, false).trim());

						System.out.print("	物品名称:" + getCellValue(currentRow.getCell(15), true, false).trim());
						ppInfo.setItemName(getCellValue(currentRow.getCell(15), true, false).trim());

						System.out.print("	订单号(物品号):" + getCellValue(currentRow.getCell(16), true, false).trim());
						ppInfo.setOrderNo(getCellValue(currentRow.getCell(16), true, false).trim());

						System.out.print("	运费和手续费金额:" + getCellValue(currentRow.getCell(17), true, true).trim());
						String spHd = getCellValue(currentRow.getCell(17), true, true).trim();
						ppInfo.setShippingHandling(Float.valueOf("".equals(spHd) ? "0" : spHd));

						System.out.print("	保险金额:" + getCellValue(currentRow.getCell(18), true, true).trim());
						String isAt = getCellValue(currentRow.getCell(18), true, true).trim();
						ppInfo.setInsuredAmount(Float.valueOf("".equals(isAt) ? "0" : isAt));

						System.out.print("	营业税:" + getCellValue(currentRow.getCell(19), true, true).trim());
						String bnTax = getCellValue(currentRow.getCell(19), true, true).trim();
						ppInfo.setBusinessTax(Float.valueOf("".equals(bnTax) ? "0" : bnTax));

						System.out.println("	选项 1 名称:" + getCellValue(currentRow.getCell(20), true, false).trim());
						ppInfo.setOption1Name(getCellValue(currentRow.getCell(20), true, false).trim());

						System.out.print("	选项 1 值:" + getCellValue(currentRow.getCell(21), true, false).trim());
						ppInfo.setOption1Value(getCellValue(currentRow.getCell(21), true, false).trim());

						System.out.print("	选项 2 名称:" + getCellValue(currentRow.getCell(22), true, false).trim());
						ppInfo.setOption2Name(getCellValue(currentRow.getCell(22), true, false).trim());

						System.out.print("	选项 2 值:" + getCellValue(currentRow.getCell(23), true, false).trim());
						ppInfo.setOption2Value(getCellValue(currentRow.getCell(23), true, false).trim());

						System.out.print("	参考交易号:" + getCellValue(currentRow.getCell(24), true, false).trim());
						ppInfo.setReferenceTradeNumber(getCellValue(currentRow.getCell(24), true, false).trim());

						System.out.print("	账单号:" + getCellValue(currentRow.getCell(25), true, false).trim());
						ppInfo.setBillNumber(getCellValue(currentRow.getCell(25), true, false).trim());

						System.out.print("	自定义号码:" + getCellValue(currentRow.getCell(26), true, false).trim());
						ppInfo.setCustomNumber(getCellValue(currentRow.getCell(26), true, false).trim());

						System.out.print("	数量:" + getCellValue(currentRow.getCell(27), true, true).trim());
						String qtt = getCellValue(currentRow.getCell(27), true, true).trim();
						ppInfo.setQuantity(Integer.valueOf("".equals(qtt) ? "0" : qtt));

						System.out.print("	收据号:" + getCellValue(currentRow.getCell(28), true, false).trim());
						ppInfo.setReceiptNumber(getCellValue(currentRow.getCell(28), true, false).trim());

						System.out.print("	余额:" + getCellValue(currentRow.getCell(29), true, true).trim());
						ppInfo.setBalance(Float.valueOf(getCellValue(currentRow.getCell(29), true, true).trim()));

						System.out.println("	地址第1行:" + getCellValue(currentRow.getCell(30), true, false).trim());
						ppInfo.setAddressLine1(getCellValue(currentRow.getCell(30), true, false).trim());

						System.out.print("	地址第2行:" + getCellValue(currentRow.getCell(31), true, false).trim());
						ppInfo.setAddressLine2(getCellValue(currentRow.getCell(31), true, false).trim());

						System.out.print("	城镇/城市:" + getCellValue(currentRow.getCell(32), true, false).trim());
						ppInfo.setTownCity(getCellValue(currentRow.getCell(32), true, false).trim());

						System.out.print(
								"	省/市/自治区/直辖市/特别行政区:" + getCellValue(currentRow.getCell(33), true, false).trim());
						ppInfo.setProvince(getCellValue(currentRow.getCell(33), true, false).trim());

						System.out.print("	邮政编码:" + getCellValue(currentRow.getCell(34), true, false).trim());
						ppInfo.setPostalCode(getCellValue(currentRow.getCell(34), true, false).trim());

						System.out.print("	国家/地区:" + getCellValue(currentRow.getCell(35), true, false).trim());
						ppInfo.setCountry(getCellValue(currentRow.getCell(35), true, false).trim());

						System.out.print("	联系电话号码:" + getCellValue(currentRow.getCell(36), true, false).trim());
						ppInfo.setContactPhoneNo(getCellValue(currentRow.getCell(36), true, false).trim());

						System.out.print("	主题:" + getCellValue(currentRow.getCell(37), true, false).trim());
						ppInfo.setTheme(getCellValue(currentRow.getCell(37), true, false).trim());

						System.out.print("	备注:" + getCellValue(currentRow.getCell(38), true, false).trim());
						ppInfo.setRemark(getCellValue(currentRow.getCell(38), true, false).trim());

						System.out.print("	国家/地区代码:" + getCellValue(currentRow.getCell(39), true, false).trim());
						ppInfo.setCountryCode(getCellValue(currentRow.getCell(39), true, false).trim());

						System.out.println("	余额影响:" + getCellValue(currentRow.getCell(40), true, false).trim());
						ppInfo.setInfluenceBalance(getCellValue(currentRow.getCell(40), true, false).trim());

						System.out.println("--------------------------------------------------------------");
						System.out.println();
						infos.add(ppInfo);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("数据解析失败:[" + getCellValue(currentRow.getCell(0), true, false) + "]");
					} finally {
						currentRow = null;
					}

				}
				System.out.println("获取全部行数:" + (lastRowIndex - firstRowIndex));
				System.out.println("获取实际总数:" + currCount + ",存入数据总数:" + infos.size());
				System.out.println("=================================================================================");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return infos;

	}

	/**
	 * 取单元格的值
	 * 
	 * @param cell
	 *            单元格对象
	 * @param treatAsStr
	 *            为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
	 * @param checkIsEmpty
	 *            为true时，把为数值类型的数据做为空的判断，为空的设置为'0'
	 * @return
	 */
	private static String getCellValue(Cell cell, boolean treatAsStr, boolean checkIsEmpty) {
		if (cell == null) {
			return "";
		}
		if (treatAsStr) {
			// 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
			// 加上下面这句，临时把它当做文本来读取
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}

		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue()).trim();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (checkIsEmpty) {
				String cellVal = String.valueOf(cell.getNumericCellValue()).trim();
				return (cellVal == null || "".equals(cellVal)) ? "0" : cellVal;
			} else {
				return String.valueOf(cell.getNumericCellValue()).trim();
			}
		} else {
			return String.valueOf(cell.getStringCellValue()).trim() == null ? ""
					: String.valueOf(cell.getStringCellValue()).trim();
		}
	}

}
