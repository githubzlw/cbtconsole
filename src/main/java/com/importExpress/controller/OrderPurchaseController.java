package com.importExpress.controller;

import com.cbt.util.DateFormatUtil;
import com.cbt.util.Redis;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.OrderPurchase;
import com.importExpress.service.OrderPurchaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.controller
 * @date:2020/1/9
 */
@Controller
@RequestMapping("/orderPurchase")
public class OrderPurchaseController {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(OrderPurchaseController.class);

    private final OrderPurchaseService orderPurchaseService;

    public OrderPurchaseController(OrderPurchaseService orderPurchaseService) {
        this.orderPurchaseService = orderPurchaseService;
    }

    @RequestMapping("/list")
    @ResponseBody
    public EasyUiJsonResult queryForList(HttpServletRequest request,
                                         @RequestParam(name = "rows", defaultValue = "40") Integer rows,
                                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                                         @RequestParam(name = "beginTime", required = true) String beginTime) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }
        try {
            int startNum = page * rows;
            OrderPurchase orderPurchase = new OrderPurchase();
            orderPurchase.setStartNum(startNum);
            orderPurchase.setLimitNum(rows);

            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));
            List<OrderPurchase> orderPurchaseList = orderPurchaseService.queryForList(orderPurchase);
            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                });
            }

            int count = orderPurchaseService.queryForListCount(orderPurchase);
            json.setSuccess(orderPurchaseList, count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("queryForList error:", e);
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }

        return json;
    }


    @RequestMapping("/exportListExcel")
    @ResponseBody
    public void exportListExcel(HttpServletResponse response, @RequestParam(name = "beginTime", required = true) String beginTime) {
        OutputStream ouputStream = null;
        try {
            OrderPurchase orderPurchase = new OrderPurchase();
            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));
            List<OrderPurchase> orderPurchaseList = orderPurchaseService.queryForList(orderPurchase);
            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                });
            }
            HSSFWorkbook wb = genOrderPurchaseListExcel(orderPurchaseList, timeBegin.getYear() + "年" + timeBegin.getMonthValue() + "月1688采购详情");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + timeBegin.getYear() + "-" + timeBegin.getMonthValue() + "-ourOrderWith1688Purchase.xls");
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

    private HSSFWorkbook genOrderPurchaseListExcel(List<OrderPurchase> orderPurchaseList, String title) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(title);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("年月份");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("订单号");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("下单时间");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("订单商品PID");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("下单数量");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("下单价格(USD)");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("商品状态");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("1688运单号");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("采购商品");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("采购数量");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("采购数量");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("采购总价(RMB)");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("采购支付时间");
        cell.setCellStyle(style);

        for (int i = 0; i < orderPurchaseList.size(); i++) {
            row = sheet.createRow((int) i + 1);
            OrderPurchase bc = orderPurchaseList.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell(0).setCellValue(bc.getYear());
            row.createCell(1).setCellValue(bc.getOrderid());
            row.createCell(2).setCellValue(bc.getOrderpaytime());
            row.createCell(3).setCellValue(bc.getOd_pid());
            row.createCell(4).setCellValue(bc.getYourorder());
            row.createCell(5).setCellValue(bc.getGoodsprice());
            if (bc.getOd_state() == 0) {
                row.createCell(6).setCellValue("待采购");
            } else if (bc.getOd_state() == 1) {
                row.createCell(6).setCellValue("入库");
            } else if (bc.getOd_state() == -1 || bc.getOd_state() == 2) {
                row.createCell(6).setCellValue("取消");
            } else {
                row.createCell(6).setCellValue("");
            }
            row.createCell(7).setCellValue(bc.getShipno());
            row.createCell(8).setCellValue(bc.getItemurl());
            row.createCell(9).setCellValue(bc.getItemqty());
            row.createCell(10).setCellValue(bc.getTotalprice());
            row.createCell(11).setCellValue(bc.getOrderdate());
        }
        return wb;
    }
}
