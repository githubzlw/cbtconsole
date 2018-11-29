package com.cbt.warehouse.ctrl;

import com.cbt.common.StringUtils;
import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.ProblemFeedBackBean;
import com.cbt.warehouse.service.ProblemFeedBackService;
import com.cbt.website.util.JsonResult;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取注册页面、支付页面、购物车页面的反馈信息;
 * 
 * @author tb
 * @time 2017.3.9
 */
@Controller
@RequestMapping("/problemFeedBack")
public class ProblemFeedBackController {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ProblemFeedBackController.class);

	@Autowired
	private ProblemFeedBackService problemFeedBackService;
	
	/**
	 * 客户返回页面回复客户反馈内容并发送邮件给客户
	 * @Title saveRepalyContent 
	 * @Description TODO
	 * @param request
	 * @return
	 * @return JsonResult
	 */
	@RequestMapping("/saveRepalyContent")
	@ResponseBody
	public JsonResult saveRepalyContent(HttpServletRequest request) {
		JsonResult json = new JsonResult();
		IGuestBookService ibs = new GuestBookServiceImpl();
		try{
			String report_id=request.getParameter("report_id");
			String text=request.getParameter("text");
			if(StringUtils.isStrNull(report_id)){
				return json;
			}
			//发送邮件给客户
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			Date now = new Date();
			dateFormat.setLenient(false);
			String date = dateFormat.format(now);
			List<ProblemFeedBackBean> list=problemFeedBackService.getReportProblem(report_id);
			if(list.size()>0){
				ProblemFeedBackBean p=list.get(0);
				int count = ibs.replyReport(p.getId(),text, date, p.getUserName(), p.getQustion(), p.getUserEmail(), p.getUserid(), p.getSale_email());
				json.setOk(count>0);
			}else{
				json.setOk(false);
			}
		}catch(Exception e){
			e.printStackTrace();
			json.setOk(false);
		}
		return json;
	}

	@RequestMapping("/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request) {

		int type = Integer.parseInt(request.getParameter("type"));
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String pageStr = request.getParameter("page");
		String adminIdStr = request.getParameter("adminId");
		String is_report = request.getParameter("is_report");
		int adminId = 0;
		JsonResult json = new JsonResult();
		if(adminIdStr == null || "".equals(adminIdStr)){
			json.setOk(false);
			json.setMessage("获取负责人id失败");
			return json;
		} else{
			adminId = Integer.valueOf(adminIdStr);
		}
		int page = 1;
		if (pageStr != null && !"".equals(pageStr)) {
			page = Integer.valueOf(pageStr);
		}
		if (page <= 0) {
			page = 1;
		}
		
		try {
			List<ProblemFeedBackBean> list = problemFeedBackService.queryForList(type, beginDate, endDate,adminId,
					(page - 1) * 20,is_report);
			Long count = problemFeedBackService.queryCount(type, beginDate, endDate,adminId,is_report);
			json.setOk(true);
			json.setTotal(count);
			json.setData(list);
			return json;
		} catch (Exception e) {
			LOG.error("The query fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The query fails, reason is :" + e.getMessage());
			return json;
		}

	}


    /**
     * customerInfo_collection.html 页头提醒条数
     *  2018/11/19 9:57 ly
     *  http://192.168.1.57:8086/cbtconsole/problemFeedBack/queryWarningNum
     *
     * @return
     */
    @RequestMapping("/queryWarningNum")
    @ResponseBody
    public Map<String, String> queryWarningNum(HttpServletRequest request) {
        try {
            String admJson = Redis.hget(request.getSession().getId(), "admuser");
            if (admJson == null) {
                return null;
            }
            Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
            int admuserid = user.getId();
            int strm = user.getRoletype();
            //临时添加Sales1账号查看投诉管理统计数据
            if (strm == 0 || user.getAdmName().equalsIgnoreCase("Ling") || user.getAdmName().equalsIgnoreCase("Sales1")
                    || user.getAdmName().equalsIgnoreCase("Sales5") || user.getAdmName().equalsIgnoreCase("emmaxie")
		            || user.getAdmName().equalsIgnoreCase("admin1")) {
                admuserid = 0;
            }
            Map<String, String> result = problemFeedBackService.queryWarningNum(admuserid);
            return result;
        } catch (Exception e) {
            LOG.error("The queryWarningNum query fails, the reason is :" + e.getMessage());
        }
        return null;
    }

	@RequestMapping("/exportExcel")
	@ResponseBody
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {

		List<ProblemFeedBackBean> list = new ArrayList<ProblemFeedBackBean>();
		try {
			list = problemFeedBackService.queryForAllList();
			HSSFWorkbook wb = genExcle(list);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=problemfeedback.xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The exportExcel fails, the reason is :" + e.getMessage());
		}
	}

	private HSSFWorkbook genExcle(List<ProblemFeedBackBean> list) {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("用户使用问题反馈");
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
		cell.setCellValue("反馈问题");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("类型");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			ProblemFeedBackBean pFeedBackBean = (ProblemFeedBackBean) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(pFeedBackBean.getId());
			row.createCell(1).setCellValue(pFeedBackBean.getUid() == 0 ? "" : pFeedBackBean.getUid() + "");
			row.createCell(2).setCellValue(pFeedBackBean.getEmail());
			row.createCell(3).setCellValue(pFeedBackBean.getProblem());
			if (pFeedBackBean.getType() == 1) {
				row.createCell(4).setCellValue("注册页面反馈");
			} else if (pFeedBackBean.getType() == 2) {
				row.createCell(4).setCellValue("购物车页面反馈");
			} else if (pFeedBackBean.getType() == 3) {
				row.createCell(4).setCellValue("支付页面反馈");
			} else {
				row.createCell(4).setCellValue("");
			}
			cell = row.createCell(5);
			cell.setCellValue(pFeedBackBean.getCreatetime() == null ? "" : sdf.format(pFeedBackBean.getCreatetime()));
		}
		return wb;
	}

}
