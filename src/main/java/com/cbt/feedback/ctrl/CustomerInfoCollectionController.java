package com.cbt.feedback.ctrl;

import com.cbt.feedback.bean.CustomerFeedback;
import com.cbt.feedback.bean.CustomerInfoCollection;
import com.cbt.feedback.bean.Questionnaire;
import com.cbt.feedback.service.CustomerInfoCollectionService;
import com.cbt.website.util.JsonResult;

import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customerInfoCollection")
public class CustomerInfoCollectionController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CustomerInfoCollectionController.class);

	@Resource
	private CustomerInfoCollectionService customerInfonService;

	@RequestMapping("/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		List<Questionnaire> customerFds = new ArrayList<Questionnaire>();
		String typeStr = request.getParameter("type");
		String salesStr = request.getParameter("sales");
		String beginDateStr = request.getParameter("beginDate");
		String endDateStr = request.getParameter("endDate");
		String comment = request.getParameter("comment");
		String pageStr = request.getParameter("page");
		int page = 1;
		if (pageStr != null && !"".equals(pageStr)) {
			page = Integer.valueOf(pageStr);
		}
		if (page <= 0) {
			page = 1;
		}
		try {
			customerFds = customerInfonService.queryForList(Integer.valueOf(typeStr), salesStr, beginDateStr,
					endDateStr, comment, (page - 1) * 20);
			Long total = customerInfonService.queryCount(Integer.valueOf(typeStr), salesStr, beginDateStr, endDateStr,
					comment);
			json.setOk(true);
			json.setTotal(total);
			json.setData(customerFds);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The query fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The query fails, reason is :" + e.getMessage());
			return json;
		}
	}

	@RequestMapping("/queryByType")
	@ResponseBody
	public JsonResult queryByType(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String type = request.getParameter("type");
		if (type == null || type == "") {
			json.setOk(false);
			json.setMessage("failed to get the type");
			return json;
		}
		List<CustomerInfoCollection> customerInfos = new ArrayList<CustomerInfoCollection>();
		try {
			customerInfos = customerInfonService.queryByType(Integer.valueOf(type));
			json.setOk(true);
			json.setData(customerInfos);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The query fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The query fails, reason is :" + e.getMessage());
			return json;
		}
	}

	@RequestMapping("/insertCustomerInfo")
	@ResponseBody
	public JsonResult insertCustomerInfo(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		CustomerInfoCollection customerInfo = getCustomerInfo(request);
		try {
			customerInfonService.insertCustomerInfo(customerInfo);
			json.setOk(true);
			json.setMessage("insert successfully");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The insert customerInfo failure, reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("Submit failure, please try later");
			return json;
		}

	}

	@RequestMapping("/updateCustomerInfo")
	@ResponseBody
	public JsonResult updateCustomerInfo(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		CustomerInfoCollection customerInfo = getCustomerInfo(request);
		if (customerInfo.getId() == 0) {
			json.setOk(false);
			json.setMessage("failed to get customerInfoId");
			return json;
		}
		try {
			customerInfonService.updateCustomerInfo(customerInfo);
			json.setOk(true);
			json.setMessage("update successfully");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The update customerInfo failure, reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("update failure, please try later");
			return json;
		}

	}

	@RequestMapping("/deleteCustomerInfo")
	@ResponseBody
	public JsonResult deleteCustomerInfo(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String id = request.getParameter("id");
		if (id == null || id == "") {
			json.setOk(false);
			json.setMessage("failed to get customerInfoId");
			return json;
		}
		try {
			customerInfonService.deleteCustomerInfo(Integer.valueOf(id));
			json.setOk(true);
			json.setMessage("delete successfully");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The delete customerInfo failure, reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("delete failure, please try later");
			return json;
		}

	}

	@RequestMapping("/exportExcel")
	@ResponseBody
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {

		List<CustomerFeedback> list = new ArrayList<CustomerFeedback>();
		try {
			list = customerInfonService.queryForAllList();
			HSSFWorkbook wb = genExcle(list);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=customerFeebdack.html.xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The exportExcel fails, the reason is :" + e.getMessage());
		}
	}

	private CustomerInfoCollection getCustomerInfo(HttpServletRequest request) {
		CustomerInfoCollection customerInfo = new CustomerInfoCollection();
		String id = request.getParameter("id");
		if (!(id == null || id == "")) {
			customerInfo.setId(Integer.valueOf(id));
		}
		String type = request.getParameter("type");
		if (!(type == null || type == "")) {
			customerInfo.setType(Integer.valueOf(type));
		}
		String needsProducts = request.getParameter("needsProducts");
		if (!(needsProducts == null || needsProducts == "")) {
			customerInfo.setNeedsProducts(needsProducts);
		}
		String sales = request.getParameter("sales");
		if (!(sales == null || sales == "")) {
			customerInfo.setSales(sales);
		}
		String email = request.getParameter("email");
		if (!(email == null || email == "")) {
			customerInfo.setEmail(email);
		}

		return customerInfo;
	}

	private HSSFWorkbook genExcle(List<CustomerFeedback> list) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("注册&订阅用户信息收集");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		// HSSFCell cell = row.createCell((short) 0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("ID");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("来源");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("用户id");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("用户邮箱");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("needsProducts");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("sales");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			CustomerFeedback cFeedback = (CustomerFeedback) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(cFeedback.getId());

			if (cFeedback.getType() == 1) {
				row.createCell(1).setCellValue("订阅");
			} else if (cFeedback.getType() == 2) {
				row.createCell(1).setCellValue("注册");
			} else {
				row.createCell(1).setCellValue("");
			}

			row.createCell(2).setCellValue(cFeedback.getUserId() == 0 ? "" : cFeedback.getUserId() + "");
			row.createCell(3).setCellValue(cFeedback.getEmail() == null ? "" : cFeedback.getEmail());
			row.createCell(4).setCellValue(cFeedback.getNeedsProducts() == null ? "" : cFeedback.getNeedsProducts());
			row.createCell(5).setCellValue(cFeedback.getSales() == null ? "" : cFeedback.getSales());
			cell = row.createCell(6);
			cell.setCellValue(cFeedback.getCreateTime() == null ? "" : sdf.format(cFeedback.getCreateTime()));
		}
		return wb;

	}

}
