package com.cbt.controller;

import com.cbt.bean.ExchangeRateDaily;
import com.cbt.exchangeRate.service.ExchangeRateService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.website.util.EasyUiJsonResult;

import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/exchangeRate")
@Controller
public class ExchangeRateController {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ExchangeRateController.class);

    @Autowired
    private ExchangeRateService exchangeRateService;


    @RequestMapping(value = "/queryMonthly")
    @ResponseBody
    public EasyUiJsonResult queryMonthly(HttpServletRequest request) {
        EasyUiJsonResult json = new EasyUiJsonResult();
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
                monthStr = "0";
                // queryTotal = 12;
            }

            List<ExchangeRateDaily> list = exchangeRateService.queryExchangeRateByDate(Integer.valueOf(yearStr), Integer.valueOf(monthStr), 0, 0);

            Map<String, List<ExchangeRateDaily>> rsMap = new HashMap<String, List<ExchangeRateDaily>>();
            //按照每月进行分组
            for (ExchangeRateDaily rateDaily : list) {
                if (rsMap.containsKey(rateDaily.getGetTime())) {
                    rsMap.get(rateDaily.getGetTime()).add(rateDaily);
                } else {
                    List<ExchangeRateDaily> cldList = new ArrayList<ExchangeRateDaily>();
                    cldList.add(rateDaily);
                    rsMap.put(rateDaily.getGetTime(), cldList);
                }
            }
            list.clear();
            //计算每月平均汇率
            List<ExchangeRateDaily> newList = new ArrayList<ExchangeRateDaily>();
            if (rsMap.size() > 0) {
                for (String rsKey : rsMap.keySet()) {
                    ExchangeRateDaily avgRateDaily = new ExchangeRateDaily();
                    avgRateDaily.setGetTime(rsKey);
                    double newAudRate = 0;
                    double newCadRate = 0;
                    double newEurRate = 0;
                    double newGbpRate = 0;
                    double newRmbRate = 0;
                    for (ExchangeRateDaily tmRateDaily : rsMap.get(rsKey)) {
                        newAudRate += avgRateDaily.getAudRate() + tmRateDaily.getAudRate();
                        newCadRate += avgRateDaily.getCadRate() + tmRateDaily.getCadRate();
                        newEurRate += avgRateDaily.getEurRate() + tmRateDaily.getEurRate();
                        newGbpRate += avgRateDaily.getGbpRate() + tmRateDaily.getGbpRate();
                        newRmbRate += avgRateDaily.getRmbRate() + tmRateDaily.getRmbRate();
                    }
                    avgRateDaily.setAudRate(BigDecimalUtil.truncateDouble(newAudRate / rsMap.get(rsKey).size(),2));
                    avgRateDaily.setCadRate(BigDecimalUtil.truncateDouble(newCadRate / rsMap.get(rsKey).size(),2));
                    avgRateDaily.setEurRate(BigDecimalUtil.truncateDouble(newEurRate / rsMap.get(rsKey).size(),2));
                    avgRateDaily.setGbpRate(BigDecimalUtil.truncateDouble(newGbpRate / rsMap.get(rsKey).size(),2));
                    avgRateDaily.setRmbRate(BigDecimalUtil.truncateDouble(newRmbRate / rsMap.get(rsKey).size(),2));
                    newList.add(avgRateDaily);
                }
            }
            rsMap.clear();

            json.setRows(newList);
            json.setTotal(newList.size());
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


    @RequestMapping(value = "/queryExchangeRateDetails")
    @ResponseBody
    public EasyUiJsonResult queryExchangeRateDetails(HttpServletRequest request) {

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
        try {
            List<ExchangeRateDaily> list = exchangeRateService.queryExchangeRateByDate(Integer.valueOf(yearStr), Integer.valueOf(monthStr), stateNum, pageNum);
            int total = exchangeRateService.queryExchangeRateByDateCount(Integer.valueOf(yearStr), Integer.valueOf(monthStr));

            json.setRows(list);
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

    @RequestMapping("/exportExchangeRateDetailsExcel")
    @ResponseBody
    public void exportExchangeRateDetailsExcel(HttpServletRequest request, HttpServletResponse response) {

        // 获取年份参数
        String yearStr = request.getParameter("year");
        // 获取月份参数
        String monthStr = request.getParameter("month");
        if (!(yearStr == null || "".equals(yearStr.trim()) || "0".equals(yearStr.trim()) || "-1".equals(yearStr.trim())
                || monthStr == null || "".equals(monthStr.trim()) || "0".equals(monthStr.trim())
                || "-1".equals(monthStr.trim()))) {
            OutputStream ouputStream = null;
            try {
                List<ExchangeRateDaily> list = exchangeRateService.queryExchangeRateByDate(Integer.valueOf(yearStr), Integer.valueOf(monthStr), 0, 0);
                HSSFWorkbook wb = genExchangeRateExcel(list, yearStr + "年" + monthStr + "实时汇率详情");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition",
                        "attachment;filename=" + yearStr + "-" + monthStr + "-exchangeRate.xls");
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


    private HSSFWorkbook genExchangeRateExcel(List<ExchangeRateDaily> list, String title) {

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
        cell.setCellValue("时间");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("EUR汇率");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("CAD汇率");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("GBP汇率");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("AUD汇率");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("RMB汇率");
        cell.setCellStyle(style);

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow((int) i + 1);
            ExchangeRateDaily exchangeRateDaily = list.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell(0).setCellValue(exchangeRateDaily.getCreateTime());
            row.createCell(1).setCellValue(exchangeRateDaily.getEurRate());
            row.createCell(2).setCellValue(exchangeRateDaily.getCadRate());
            row.createCell(3).setCellValue(exchangeRateDaily.getGbpRate());
            row.createCell(4).setCellValue(exchangeRateDaily.getAudRate());
            row.createCell(5).setCellValue(exchangeRateDaily.getRmbRate());
        }
        return wb;
    }


}
