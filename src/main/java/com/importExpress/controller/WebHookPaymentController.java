package com.importExpress.controller;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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

import com.cbt.pojo.ReportSalesInfo;
import com.cbt.pojo.SalesReport;
import com.cbt.website.bean.PaymentFooterBean;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.WebhookPaymentBean;
import com.importExpress.service.WebhoolPaymentService;

@Controller
@RequestMapping("/webhook/payment")
public class WebHookPaymentController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private WebhoolPaymentService webhoolPaymentService;

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUiJsonResult list(HttpServletRequest request, HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		request.getParameter("row");
		
		int startNum = 0;
        int limitNum = 50;
        String limitStr = request.getParameter("rows");
        if (!(limitStr == null || "".equals(limitStr) || "0".equals(limitStr))) {
        	limitNum = Integer.valueOf(limitStr);
        }
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String type = request.getParameter("type");
        
        String sttime = request.getParameter("sttime");
        if (sttime == null || "".equals(sttime)) {
            sttime = "";
        } else {
            sttime += " 00:00:00";
        }
        
        String edtime = request.getParameter("edtime");
        if (edtime == null || "".equals(edtime)) {
            edtime = "";
        } else {
            edtime += " 23:59:59";
        }
		try {
			Map<String, Object> result = webhoolPaymentService.list(startNum, limitNum,sttime, edtime,type);
//            long total = (long)result.get("total");
            json.setSuccess(true);
            json.setRows(result.get("data"));
            json.setTotal((int)result.get("total"));
            WebhookPaymentBean footer = new WebhookPaymentBean();
            footer.setEmail("Amount:"+result.get("mcGrossTatal")+" USD");
            footer.setOrderNO("");
            footer.setAmount("Refunded:"+result.get("mcRefundTatal")+" USD");
            footer.setProfit("Amount - Refunded:");
            footer.setType(result.get("profit")+" USD");
            List<WebhookPaymentBean> footerLst = new ArrayList<WebhookPaymentBean>();
			footerLst.add(footer);
            
            json.setFooter(footerLst);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
		return json;
	}
	
	
	
	/**
	 * 导出 销售统计报表
	 */
	@RequestMapping(value = "/export")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		int startNum = 0;
//        int limitNum = 20;
//        String limitStr = request.getParameter("rows");
//        if (!(limitStr == null || "".equals(limitStr) || "0".equals(limitStr))) {
//        	limitNum = Integer.valueOf(limitStr);
//        }
//        String pageStr = request.getParameter("page");
//        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
//            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
//        }
        String type = request.getParameter("type");

        String importFlag = request.getParameter("importFlag");

        String sttime = request.getParameter("sttime");
        if (sttime == null || "".equals(sttime)) {
            sttime = sdf.format(new Date()).split("(\\s+)")[0]+" 00:00:00";
        } else {
            sttime += " 00:00:00";
        }
        String edtime = request.getParameter("edtime");
        if (edtime == null || "".equals(edtime)) {
            edtime = sdf.format(new Date());
        } else {
            edtime += " 23:59:59";
        }
		try {
			Map<String, Object> result = webhoolPaymentService.list(0, 0,sttime, edtime,type);
			int tatal = (int)result.get("total");
			if(tatal > 0) {
				List<WebhookPaymentBean> data = (List<WebhookPaymentBean>)result.get("data");



				if(StringUtils.isNotBlank(importFlag) && "1".equals(importFlag)) {

					Map<String, List<WebhookPaymentBean>> rsMap = data.stream()
						.filter(e-> Double.parseDouble(e.getAmount().replace("USD","").trim()) > 0
								&& "ImportExpress".equalsIgnoreCase(e.getType()))
						.collect(Collectors.groupingBy(WebhookPaymentBean::getOrderNO));

					List<WebhookPaymentBean> resultData = new ArrayList<>();
					Map<String, WebhookPaymentBean> tempPaymentBean;
					List<WebhookPaymentBean> tempList;
					for (String orderNo : rsMap.keySet()) {
						tempList = rsMap.get(orderNo);
						if (CollectionUtils.isNotEmpty(tempList) && tempList.size() > 1) {
							tempPaymentBean = new HashMap<>();
							for (WebhookPaymentBean paymentBean : tempList) {
								tempPaymentBean.put(paymentBean.getAmount(), paymentBean);
							}
							if (tempPaymentBean.size() > 0) {
								resultData.addAll(tempPaymentBean.values());
							}
						} else {
							resultData.addAll(rsMap.get(orderNo));
						}
					}
					data.clear();
					data = resultData;
					if(CollectionUtils.isNotEmpty(resultData)){
						resultData.sort(Comparator.comparing(WebhookPaymentBean::getCreateTime).reversed());
					}
				}

				
				Calendar c = Calendar.getInstance();
				int yy = c.get(Calendar.YEAR);
				int mm = c.get(Calendar.MONTH) + 1;
				int dd = c.get(Calendar.DAY_OF_MONTH);
				int hh = c.get(Calendar.HOUR_OF_DAY);
				int mi = c.get(Calendar.MINUTE);
				int ss = c.get(Calendar.SECOND);
				
				String sheetName = StringUtils.isEmpty(edtime)? "" : edtime.split("(\\s+)")[0];
				
				String filename = "" + yy;
				filename += mm < 10 ? ("0" + mm) : mm;
				filename += dd < 10 ? ("0" + dd) : dd;
				filename += hh < 10 ? ("0" + hh) : hh;
				filename += mi < 10 ? ("0" + mi) : mi;
				filename += ss < 10 ? ("0" + ss) : ss;
				filename += ".xls";
				
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet(sheetName);
				String[] heji = {"序号","ID","时间","用户","邮箱","订单号","金额","手续费","利润","类型","支付方式","pp账号"};
				int rows =0;  //记录行数
				HSSFRow row = sheet.createRow(rows++);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				//写入报表汇总
				HSSFCell hcell = row.createCell(0); //添加标题
				hcell.setCellValue("报表合计：");
				row = sheet.createRow(rows++);  //到下一行添加数据
				for (int i = 0; i < heji.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellValue(heji[i]);
					cell.setCellStyle(style);
				}
				int size = data.size();
				for (int i = 0; i < size; i++) {
					row = sheet.createRow(rows++);
					WebhookPaymentBean webhook = data.get(i);
					row.createCell(0).setCellValue(i+1);
//					{"ID","时间","用户","邮箱","订单号","金额","手续费"，"利润","类型","支付方式","pp账号"};
					row.createCell(1).setCellValue(webhook.getId());
					row.createCell(2).setCellValue(webhook.getCreateTime());
					row.createCell(3).setCellValue(webhook.getUserid());
					row.createCell(4).setCellValue(webhook.getEmail());
					row.createCell(5).setCellValue(webhook.getOrderNO());
					row.createCell(6).setCellValue(webhook.getAmount());
					row.createCell(7).setCellValue(webhook.getTransactionFee());
					row.createCell(8).setCellValue(webhook.getProfit());
					row.createCell(9).setCellValue(webhook.getType());
					row.createCell(10).setCellValue(webhook.getPayType());
					row.createCell(11).setCellValue(webhook.getReceiverID());
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
				out.write("<script>alert('没有数据，无法导出。或重新选择日期后再导出。'); window.history.back();</script>");
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
