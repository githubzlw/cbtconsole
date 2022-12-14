package com.cbt.feedback.ctrl;

import com.cbt.feedback.bean.SubscribeEmail;
import com.cbt.feedback.service.SubscribeEmailService;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/subscribeEmail")
public class SubscribeEmailController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SubscribeEmailController.class);

	@Resource
	private SubscribeEmailService subscribeEmailService;

	@RequestMapping("/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		List<SubscribeEmail> subscribeEmails = new ArrayList<SubscribeEmail>();
		try {
			subscribeEmails = subscribeEmailService.queryForList();
			json.setOk(true);
			json.setData(subscribeEmails);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The query fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The query fails, reason is :" + e.getMessage());
			return json;
		}
	}

	@RequestMapping("/exportExcel")
	@ResponseBody
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {

		List<SubscribeEmail> subscribeEmails = new ArrayList<SubscribeEmail>();
		try {
			subscribeEmails = subscribeEmailService.queryForList();
			HSSFWorkbook wb = genExcle(subscribeEmails);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=subscribeEmail.xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The exportExcel fails, the reason is :" + e.getMessage());
		}
	}

	@RequestMapping("/insertSubscribeEmail")
	@ResponseBody
	public JsonResult insertSubscribeEmail(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		SubscribeEmail subscribeEmail = getSubscribeEmail(request);
		subscribeEmail.setCreateTime(new Timestamp(System.currentTimeMillis()));
		try {
			subscribeEmailService.insertSubscribeEmail(subscribeEmail);
			json.setOk(true);
			json.setMessage("insert successfully");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The insert SubscribeEmail failure, reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("Submit failure, please try later");
			return json;
		}

	}

	@RequestMapping("/updateSubscribeEmail")
	@ResponseBody
	public JsonResult updateSubscribeEmail(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		SubscribeEmail subscribeEmail = getSubscribeEmail(request);
		if (subscribeEmail.getId() == 0) {
			json.setOk(false);
			json.setMessage("failed to get SubscribeEmailId");
			return json;
		}
		try {
			subscribeEmailService.updateSubscribeEmail(subscribeEmail);
			json.setOk(true);
			json.setMessage("update successfully");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The update SubscribeEmail failure, reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("update failure, please try later");
			return json;
		}

	}

	@RequestMapping("/deleteSubscribeEmail")
	@ResponseBody
	public JsonResult deleteSubscribeEmail(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String id = request.getParameter("id");
		if (id == null || id == "") {
			json.setOk(false);
			json.setMessage("failed to get SubscribeEmailId");
			return json;
		}
		try {
			subscribeEmailService.deleteSubscribeEmail(Integer.valueOf(id));
			json.setOk(true);
			json.setMessage("delete successfully");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The delete SubscribeEmail failure, reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("delete failure, please try later");
			return json;
		}

	}

	private SubscribeEmail getSubscribeEmail(HttpServletRequest request) {
		SubscribeEmail subscribeEmail = new SubscribeEmail();
		String id = request.getParameter("id");
		if (!(id == null || id == "")) {
			subscribeEmail.setId(Integer.valueOf(id));
		}
		String email = request.getParameter("email");
		if (!(email == null || email == "")) {
			subscribeEmail.setEmail(email);
		}
		return subscribeEmail;
	}

	private HSSFWorkbook genExcle(List<SubscribeEmail> subscribeEmails) {

		// ????????????????????????webbook???????????????Excel??????
		HSSFWorkbook wb = new HSSFWorkbook();
		// ???????????????webbook???????????????sheet,??????Excel????????????sheet
		HSSFSheet sheet = wb.createSheet("????????????");
		// ???????????????sheet??????????????????0???,???????????????poi???Excel????????????????????????short
		HSSFRow row = sheet.createRow((int) 0);
		// ???????????????????????????????????????????????? ??????????????????
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ????????????????????????

		// HSSFCell cell = row.createCell((short) 0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("ID");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("??????");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("??????");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("????????????");
		cell.setCellStyle(style);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < subscribeEmails.size(); i++) {
			row = sheet.createRow((int) i + 1);
			SubscribeEmail sEmail = (SubscribeEmail) subscribeEmails.get(i);
			// ??????????????????????????????????????????
			row.createCell(0).setCellValue((double) sEmail.getId());
			row.createCell(1).setCellValue(sEmail.getEmail());
			row.createCell(2).setCellValue(sEmail.getLabel());
			cell = row.createCell(3);
			cell.setCellValue(sdf.format(sEmail.getCreateTime()));
		}
		return wb;

	}

}
