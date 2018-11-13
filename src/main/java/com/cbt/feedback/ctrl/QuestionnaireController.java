package com.cbt.feedback.ctrl;

import com.cbt.feedback.bean.Questionnaire;
import com.cbt.feedback.service.QuestionnaireService;
import com.cbt.website.util.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {
	private static final Log LOG = LogFactory.getLog(QuestionnaireController.class);

	@Resource
	private QuestionnaireService questionnaireService;

	@RequestMapping("/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		List<Questionnaire> qtns = new ArrayList<Questionnaire>();
		String typeStr = request.getParameter("type");
		String userIdStr = request.getParameter("userId");
		String userEmail = request.getParameter("userEmail");
		String pageStr = request.getParameter("page");
		int page = 1;
		if (pageStr != null && !"".equals(pageStr)) {
			page = Integer.valueOf(pageStr);
		}
		if (page <= 0) {
			page = 1;
		}
		int userId = 0;
		if (!(userIdStr == null || "".equals(userIdStr))) {
			userId = Integer.valueOf(userIdStr);
		}
		int type = 0;
		if (!(typeStr == null || "".equals(typeStr))) {
			type = Integer.valueOf(typeStr);
		}
		try {
			qtns = questionnaireService.queryForList(type, userId, userEmail, (page - 1) * 20);
			for (Questionnaire qtn : qtns) {
				if (qtn.getContent() == null || "".equals(qtn.getContent())) {
					qtn.setContent("");
				} else {
					qtn.setContent(URLDecoder.decode(qtn.getContent()));
				}
				if (qtn.getOtherComment() == null || "".equals(qtn.getOtherComment())) {
					qtn.setOtherComment("");
				} else {
					qtn.setOtherComment(URLDecoder.decode(qtn.getOtherComment()));
				}
				if (qtn.getUserEmail() == null || "".equals(qtn.getUserEmail())) {
					qtn.setUserEmail("");
				}
				if (qtn.getSearchKeywords() == null || "".equals(qtn.getSearchKeywords())) {
					qtn.setSearchKeywords("");
				} else {
					qtn.setSearchKeywords(URLDecoder.decode(qtn.getSearchKeywords()));
				}
			}
			Long total = questionnaireService.queryForCount(type, userId, userEmail);
			json.setOk(true);
			json.setTotal(total);
			json.setData(qtns);
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

		List<Questionnaire> list = new ArrayList<Questionnaire>();
		try {
			list = questionnaireService.queryForAllList();
			HSSFWorkbook wb = genExcle(list);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=questionnaire.xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The exportExcel fails, the reason is :" + e.getMessage());
		}
	}

	private HSSFWorkbook genExcle(List<Questionnaire> list) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("搜索页问卷调查");
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
		cell.setCellValue("用户id");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("用户邮箱");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("类型");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("内容");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("留言");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("搜索关键词");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("搜索Url");
		cell.setCellStyle(style);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			Questionnaire qtn = (Questionnaire) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(qtn.getId());
			row.createCell(1).setCellValue(qtn.getUserId() == 0 ? "" : qtn.getUserId() + "");
			row.createCell(2).setCellValue(qtn.getUserEmail() == null ? "" : qtn.getUserEmail());

			if (qtn.getType() == 1) {
				row.createCell(3).setCellValue("价格太高");
			} else if (qtn.getType() == 2) {
				row.createCell(3).setCellValue("没有找到想要的");
			} else {
				row.createCell(3).setCellValue("");
			}
			row.createCell(4).setCellValue(qtn.getContent() == null ? "" : URLDecoder.decode(qtn.getContent()));
			row.createCell(5)
					.setCellValue(qtn.getOtherComment() == null ? "" : URLDecoder.decode(qtn.getOtherComment()));
			cell = row.createCell(6);
			cell.setCellValue(qtn.getCreateTime() == null ? "" : sdf.format(qtn.getCreateTime()));

			row.createCell(7)
					.setCellValue(qtn.getSearchKeywords() == null ? "" : URLDecoder.decode(qtn.getSearchKeywords()));
			row.createCell(8).setCellValue(qtn.getSearchUrl() == null ? "" : qtn.getSearchUrl());

		}
		return wb;

	}

}
