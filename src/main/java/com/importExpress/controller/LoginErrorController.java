package com.importExpress.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.LoginErrorInfo;
import com.importExpress.service.LoginErrorService;

@Controller
@RequestMapping("/login/error")
public class LoginErrorController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private LoginErrorService loginErrorService;

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUiJsonResult list(HttpServletRequest request, HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
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
			List<LoginErrorInfo> result = loginErrorService.getList(startNum, limitNum);
            json.setSuccess(true);
            json.setRows(result);
            json.setTotal(result.size());
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
	/*@RequestMapping(value = "/export")
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
	}*/
	
}
