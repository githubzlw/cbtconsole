package com.cbt.export.controller;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cbt.export.pojo.ExportInfo;
import com.cbt.export.service.ExportService;
import com.cbt.parse.service.StrUtils;
import com.cbt.website.util.EasyUiJsonResult;

@Controller
@RequestMapping("/cexport")
public class CustomsExportController {
	@Autowired
	private ExportService exportService;
	@RequestMapping("/ship")
	@ResponseBody
	public EasyUiJsonResult exportShippingPackages(HttpServletRequest request, HttpServletResponse response){
		EasyUiJsonResult json = new EasyUiJsonResult();
		String order_no = request.getParameter("order_no");
		String sStart = request.getParameter("page");
		sStart = StrUtils.isNum(sStart) ? sStart : "1";
		String sLimit = request.getParameter("rows");
		sLimit = StrUtils.isNum(sLimit) ? sLimit : "10";
		int limitNum = Integer.valueOf(sLimit);
		int startNum = (Integer.valueOf(sStart) - 1) * limitNum;
		json.setSuccess(true);
		try {
			List<ExportInfo> export = exportService.getExport(startNum, limitNum,order_no);
			json.setRows(export);
			int exportCount = StringUtils.isBlank(order_no) ? exportService.getExportCount() : 1;
			json.setTotal(exportCount);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setTotal(0);
		}
		return json;
	}

	/**
	 * 导出 销售统计报表
	 */
	@RequestMapping(value = "/export")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String order_no = request.getParameter("order_no");
		String sStart = request.getParameter("page");
		sStart = StrUtils.isNum(sStart) ? sStart : "1";
		String sLimit = request.getParameter("rows");
		sLimit = StrUtils.isNum(sLimit) ? sLimit : "10";
		int limitNum = Integer.valueOf(sLimit);
		int startNum = (Integer.valueOf(sStart) - 1) * limitNum;
		
		try {
			List<ExportInfo> export = exportService.getExport(startNum, limitNum,order_no);
			if(export != null && !export.isEmpty()) {
				
				Calendar c = Calendar.getInstance();
				int yy = c.get(Calendar.YEAR);
				int mm = c.get(Calendar.MONTH) + 1;
				int dd = c.get(Calendar.DAY_OF_MONTH);
				int hh = c.get(Calendar.HOUR_OF_DAY);
				int mi = c.get(Calendar.MINUTE);
				int ss = c.get(Calendar.SECOND);
				
				String sheetName = "出口审批";
				
				String filename = "" + yy;
				filename += mm < 10 ? ("0" + mm) : mm;
				filename += dd < 10 ? ("0" + dd) : dd;
				filename += hh < 10 ? ("0" + hh) : hh;
				filename += mi < 10 ? ("0" + mi) : mi;
				filename += ss < 10 ? ("0" + ss) : ss;
				filename += ".xls";
				
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet(sheetName);
				String[] heji = new String[]{"","电商企业代码","物流企业代码","电商订单号","总运单号","分运单号","订单总重量","订单总净重",
						"币种","商品总价值","运杂费","保费","集包号","小包件数","发货人名称","发货人地址","发货人国家","发货人电话","收件人名称 ","收件人地址 ","收件人国家",
						"收件人电话","支付交易流水号","支付总金额","支付企业代码","支付企业名称","到帐时间","电商商品编号","商品名称","单件重量",
						"商品数量","商品单价","商品总价","海关备案编号","HS编码 "};
				int rows =0;  //记录行数
				HSSFRow row = sheet.createRow(rows++);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				//写入报表汇总
//				HSSFCell hcell = row.createCell(0); //添加标题
//				hcell.setCellValue("报表合计：");
				row = sheet.createRow(rows++);  //到下一行添加数据
				for (int i = 0; i < heji.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellValue(heji[i]);
					cell.setCellStyle(style);
				}
				int size = export.size();
				for (int i = 0; i < size; i++) {
					row = sheet.createRow(rows++);
					ExportInfo exportInfo = export.get(i);
					row.createCell(0).setCellValue(i+1);
//					{"ID","时间","用户","邮箱","订单号","金额","手续费"，"利润","类型","支付方式","pp账号"};
					row.createCell(1).setCellValue(exportInfo.getCompanyCode());
					row.createCell(2).setCellValue(exportInfo.getLogisticscode());
					row.createCell(3).setCellValue(exportInfo.getNumber());
					row.createCell(4).setCellValue(exportInfo.getShipmentNumber());
					row.createCell(5).setCellValue(exportInfo.getSubShipmentNumber());
					row.createCell(6).setCellValue(exportInfo.getWeight());
					row.createCell(7).setCellValue(exportInfo.getNetWeight());
					row.createCell(8).setCellValue(exportInfo.getCurrency());
					row.createCell(9).setCellValue(exportInfo.getCost());
					row.createCell(10).setCellValue(exportInfo.getOtherCost());
					row.createCell(11).setCellValue(exportInfo.getPremium());
					row.createCell(12).setCellValue(exportInfo.getPackageCode());
					row.createCell(13).setCellValue(exportInfo.getPackageNumber());
					row.createCell(14).setCellValue(exportInfo.getPostName());
					row.createCell(15).setCellValue(exportInfo.getPostAddress());
					row.createCell(16).setCellValue(exportInfo.getPostContry());
					row.createCell(17).setCellValue(exportInfo.getPostNumber());
					row.createCell(18).setCellValue(exportInfo.getReceivingName());
					row.createCell(19).setCellValue(exportInfo.getReceivingAddress());
					row.createCell(20).setCellValue(exportInfo.getReceivingContry());
					row.createCell(21).setCellValue(exportInfo.getReceivingNumber());
					row.createCell(22).setCellValue(exportInfo.getTransactionNumber());
					row.createCell(23).setCellValue(exportInfo.getTotalAmount());
					row.createCell(24).setCellValue(exportInfo.getEnterpriseCode());
					row.createCell(25).setCellValue(exportInfo.getEnterpriseName());
					row.createCell(26).setCellValue(exportInfo.getReceivingTime());
					row.createCell(27).setCellValue(exportInfo.getPid());
					row.createCell(28).setCellValue(exportInfo.getGoodsName());
					row.createCell(29).setCellValue(exportInfo.getGoodsWeight());
					row.createCell(30).setCellValue(exportInfo.getQuality());
					row.createCell(31).setCellValue(exportInfo.getGoodsPrice());
					row.createCell(32).setCellValue(exportInfo.getGoodsCost());
					row.createCell(33).setCellValue(exportInfo.getCustomsRecordNumber());
					row.createCell(34).setCellValue(exportInfo.getHsNumber());
				}
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment;filename=" + filename);
				OutputStream ouputStream = response.getOutputStream();
				wb.write(ouputStream);
				ouputStream.flush();
				ouputStream.close();
				
			}else {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.write("<script>alert('没有数据，无法导出。'); window.history.back();</script>");
				out.close();
			}
        } catch (Exception e) {
        	e.printStackTrace();
        	response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.write("<script>alert('程序出错，无法导出。'); window.history.back();</script>");
			out.close();
        	
        }

	}
}
