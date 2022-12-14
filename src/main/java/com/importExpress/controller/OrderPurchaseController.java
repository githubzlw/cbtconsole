package com.importExpress.controller;

import com.cbt.util.BigDecimalUtil;
import com.cbt.util.DateFormatUtil;
import com.cbt.util.Redis;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.OrderPurchase;
import com.importExpress.service.OrderPurchaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
import java.util.stream.Collectors;

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

    private static final double EXCHANGE_RATE = 6.88;

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
            json.setMessage("???????????????");
            return json;
        }
        try {
            int startNum = (page - 1) * rows;
            OrderPurchase orderPurchase = new OrderPurchase();
            orderPurchase.setStartNum(startNum);
            orderPurchase.setLimitNum(rows);

            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));

            orderPurchaseService.getCurrentOrder(orderPurchase);

            List<OrderPurchase> orderPurchaseList = orderPurchaseService.queryForList(orderPurchase);
            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                    if (e.getExchange_rate() < 6) {
                        e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * EXCHANGE_RATE, 2));
                    } else {
                        e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getExchange_rate(), 2));
                    }
                });
            }

            int count = orderPurchaseService.queryForListCount(orderPurchase);
            json.setSuccess(orderPurchaseList, count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("queryForList error:", e);
            json.setSuccess(false);
            json.setMessage("?????????????????????:" + e.getMessage());
        }

        return json;
    }


    @RequestMapping("/exportKopExcelDetail")
    @ResponseBody
    public void exportKopExcelDetail(HttpServletResponse response, @RequestParam(name = "beginTime", required = true) String beginTime) {
        OutputStream ouputStream = null;
        try {
            OrderPurchase orderPurchase = new OrderPurchase();
            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));

            orderPurchaseService.getCurrentOrder(orderPurchase);
            List<OrderPurchase> orderPurchaseList = orderPurchaseService.queryForList(orderPurchase);
            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                    if (e.getExchange_rate() < 6) {
                        e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * EXCHANGE_RATE, 2));
                    } else {
                        e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getExchange_rate(), 2));
                    }
                });
            }
            HSSFWorkbook wb = genOrderPurchaseListExcelDetail(orderPurchaseList, timeBegin.getYear() + "???" + timeBegin.getMonthValue() + "???1688????????????");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + timeBegin.getYear() + "-" + timeBegin.getMonthValue() + "-ourOrderWith1688PurchaseDetails.xls");
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

    @RequestMapping("/exportKopExcelTotal")
    @ResponseBody
    public void exportKopExcelTotal(HttpServletResponse response, @RequestParam(name = "beginTime", required = true) String beginTime) {
        OutputStream ouputStream = null;
        try {
            OrderPurchase orderPurchase = new OrderPurchase();
            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));

            orderPurchaseService.getCurrentOrder(orderPurchase);

            List<OrderPurchase> orderPurchaseList = orderPurchaseService.queryForList(orderPurchase);
            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                    if (e.getExchange_rate() < 6) {
                        e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getYourorder() * EXCHANGE_RATE, 2));
                    } else {
                        e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getYourorder() * e.getExchange_rate(), 2));
                    }
                });
            }

            // ???????????????????????????
            List<OrderPurchase> noPurchaseList = orderPurchaseList.stream().filter(e -> StringUtils.isBlank(e.getShipno()) || StringUtils.isBlank(e.getOd_shipno()) || "0".equals(e.getOd_shipno())).collect(Collectors.toList());

            // ??????????????????????????????

            // ????????????????????????
            List<OrderPurchase> otherList = orderPurchaseList.stream().filter(e -> StringUtils.isNotBlank(e.getShipno()) && StringUtils.isNotBlank(e.getOd_shipno()) && !"0".equals(e.getOd_shipno())).collect(Collectors.toList());

            orderPurchaseList.clear();

            List<OrderPurchase> resultList = new ArrayList<>(orderPurchaseList.size());


            // ??????
            Map<String, List<OrderPurchase>> orderNoMap = otherList.stream().collect(Collectors.groupingBy(OrderPurchase::getOrderid));

            orderNoMap.forEach((k, v) -> {
                // ?????????
                Map<String, List<OrderPurchase>> tempMap = v.stream().collect(Collectors.groupingBy(OrderPurchase::getShipno));
                if (tempMap.size() > 0) {
                    tempMap.forEach((ck, cv) -> {
                        OrderPurchase tempPurchase = new OrderPurchase();
                        tempPurchase.setOrderid(cv.get(0).getOrderid());
                        tempPurchase.setOrderpaytime(cv.get(0).getOrderpaytime());
                        cv.forEach(sel -> {
                            tempPurchase.setYourorder(sel.getYourorder() + tempPurchase.getYourorder());
                            tempPurchase.setGoodsprice(sel.getGoodsprice() + tempPurchase.getGoodsprice());
                        });
                        tempPurchase.setShipno(ck);
                        tempPurchase.setTb_orderid(cv.get(0).getTb_orderid());
                        tempPurchase.setItemqty(cv.get(0).getItemqty());
                        tempPurchase.setTotalprice(cv.get(0).getTotalprice());
                        tempPurchase.setOrderdate(cv.get(0).getOrderdate());
                        tempPurchase.setYear(cv.get(0).getYear());
                        resultList.add(tempPurchase);
                    });
                    tempMap.clear();
                }
            });
            orderNoMap.clear();
            otherList.clear();

            resultList.addAll(noPurchaseList);
            noPurchaseList.clear();

            HSSFWorkbook wb = genOrderPurchaseListExcelTotal(resultList, timeBegin.getYear() + "???" + timeBegin.getMonthValue() + "???1688????????????");
            resultList.clear();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + timeBegin.getYear() + "-" + timeBegin.getMonthValue() + "-ourOrderWith1688PurchaseTotal.xls");
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


    @RequestMapping("/taobaoList")
    @ResponseBody
    public EasyUiJsonResult taobaoList(HttpServletRequest request,
                                       @RequestParam(name = "rows", defaultValue = "40") Integer rows,
                                       @RequestParam(name = "page", defaultValue = "1") Integer page,
                                       @RequestParam(name = "beginTime", required = true) String beginTime) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("???????????????");
            return json;
        }
        try {
            int startNum = (page - 1) * rows;
            OrderPurchase orderPurchase = new OrderPurchase();
            orderPurchase.setStartNum(startNum);
            orderPurchase.setLimitNum(rows);

            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));

            orderPurchaseService.getCurrentOrder(orderPurchase);

            List<OrderPurchase> orderPurchaseList = orderPurchaseService.taobaoList(orderPurchase);
            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    if (StringUtils.isNotBlank(e.getOd_shipno())) {
                        e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                        if (e.getExchange_rate() < 6) {
                            //  * EXCHANGE_RATE
                            e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice(), 2));
                        } else {
                            //  * e.getExchange_rate()
                            e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice(), 2));
                        }
                    }
                });
            }

            int count = orderPurchaseService.taobaoListCount(orderPurchase);
            json.setSuccess(orderPurchaseList, count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("taobaoList error:", e);
            json.setSuccess(false);
            json.setMessage("?????????????????????:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/taobaoListExcel")
    @ResponseBody
    public void taobaoListExcel(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(name = "beginTime", required = true) String beginTime) {
        OutputStream ouputStream = null;
        try {
            OrderPurchase orderPurchase = new OrderPurchase();
            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));

            orderPurchaseService.getCurrentOrder(orderPurchase);

            List<OrderPurchase> orderPurchaseList = orderPurchaseService.taobaoList(orderPurchase);
            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                    if (StringUtils.isNotBlank(e.getOrderid())) {
                        if (e.getExchange_rate() < 6) {
                            //  * EXCHANGE_RATE
                            e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getYourorder(), 2));
                        } else {
                            // * e.getExchange_rate()
                            e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getYourorder() , 2));
                        }
                    }
                });
            }

            HSSFWorkbook wb = genOrderPurchaseListExcelDetail(orderPurchaseList, timeBegin.getYear() + "???" + timeBegin.getMonthValue() + "???1688????????????????????????");
            orderPurchaseList.clear();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + timeBegin.getYear() + "-" + timeBegin.getMonthValue() + "-1688PurchaseForOurOrder.xls");
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


    @RequestMapping("/taobaoListGroupExcel")
    @ResponseBody
    public void taobaoListGroupExcel(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(name = "beginTime", required = true) String beginTime) {
        OutputStream ouputStream = null;
        try {
            OrderPurchase orderPurchase = new OrderPurchase();
            LocalDateTime timeBegin = DateFormatUtil.getTimeWithStr(beginTime + " 00:00:00");

            LocalDateTime timeEnd = timeBegin.plusMonths(1);
            orderPurchase.setBeginTime(beginTime);
            orderPurchase.setEndTime(DateFormatUtil.formatDateToYearAndMonthAndDayString(timeEnd));

            orderPurchaseService.getCurrentOrder(orderPurchase);

            List<OrderPurchase> orderPurchaseList = orderPurchaseService.taobaoListGroup(orderPurchase);
            /*List<OrderPurchase> orderPurchaseList = orderPurchaseService.taobaoList(orderPurchase);

            Map<String, List<OrderPurchase>> hasMap= orderPurchaseList.stream().filter(e-> StringUtils.isNotBlank(e.getOrderid())).collect(Collectors.groupingBy(OrderPurchase::getTb_orderid));

            Map<String, List<OrderPurchase>> noMap = orderPurchaseList.stream().filter(e -> StringUtils.isBlank(e.getOrderid())).collect(Collectors.groupingBy(OrderPurchase::getTb_orderid));

            orderPurchaseList.clear();

            Set<String> orderSet = new HashSet<>();

            hasMap.forEach((k, v) -> {
                orderSet.add(v.get(0).getOrderid());
                orderPurchaseList.add(v.get(0));
            });


            noMap.forEach((k, v) -> {
                if (!orderSet.contains(v.get(0).getOrderid())) {
                    orderSet.add(v.get(0).getOrderid());
                    orderPurchaseList.add(v.get(0));
                }
            });

            hasMap.clear();
            noMap.clear();*/


            if (CollectionUtils.isNotEmpty(orderPurchaseList)) {
                orderPurchaseList.forEach(e -> {
                    e.setYear(timeBegin.getYear() + "-" + timeBegin.getMonthValue());
                    if (StringUtils.isNotBlank(e.getOrderid())) {
                        if (e.getExchange_rate() < 6) {
                            //  * EXCHANGE_RATE
                            e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getYourorder(), 2));
                        } else {
                            //  * e.getExchange_rate()
                            e.setGoodsprice(BigDecimalUtil.truncateDouble(e.getGoodsprice() * e.getYourorder(), 2));
                        }
                    }
                });
            }
            double totalRefund = 0;
            double totalPrice = 0;
            for(OrderPurchase cd : orderPurchaseList){
                if("????????????".equalsIgnoreCase(cd.getOrderstatus()) || "??????".equalsIgnoreCase(cd.getOrderstatus())){
                    totalRefund += Double.parseDouble(cd.getTotalprice());
                } else {
                    totalPrice += Double.parseDouble(cd.getTotalprice());
                }
            }

            System.err.println(totalRefund);
            System.err.println(totalPrice);
            HSSFWorkbook wb = genOrderPurchaseListExcelGroup(orderPurchaseList, timeBegin.getYear() + "???" + timeBegin.getMonthValue() + "???1688??????????????????????????????");
            orderPurchaseList.clear();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + timeBegin.getYear() + "-" + timeBegin.getMonthValue() + "-1688PurchaseForOurOrderGroup.xls");
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


    private HSSFWorkbook genOrderPurchaseListExcelDetail(List<OrderPurchase> orderPurchaseList, String title) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(title);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ????????????????????????

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("?????????");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("?????????");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("????????????PID");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("????????????(USD)");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("1688?????????");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("1688?????????");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("????????????(RMB)");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("??????????????????");
        cell.setCellStyle(style);

        for (int i = 0; i < orderPurchaseList.size(); i++) {
            row = sheet.createRow((int) i + 1);
            OrderPurchase bc = orderPurchaseList.get(i);
            // ??????????????????????????????????????????
            row.createCell(0).setCellValue(bc.getYear());
            row.createCell(1).setCellValue(bc.getOrderid());
            row.createCell(2).setCellValue(bc.getOrderpaytime());
            row.createCell(3).setCellValue(bc.getOd_pid());
            row.createCell(4).setCellValue(bc.getYourorder());
            row.createCell(5).setCellValue(bc.getGoodsprice());
            if (bc.getOd_state() == 0) {
                row.createCell(6).setCellValue("?????????");
            } else if (bc.getOd_state() == 1) {
                row.createCell(6).setCellValue("??????");
            } else if (bc.getOd_state() == -1 || bc.getOd_state() == 2) {
                row.createCell(6).setCellValue("??????");
            } else {
                row.createCell(6).setCellValue("");
            }
            row.createCell(7).setCellValue(bc.getTb_orderid());
            row.createCell(8).setCellValue(bc.getShipno());
            row.createCell(9).setCellValue(bc.getItemurl());
            row.createCell(10).setCellValue(bc.getOrderstatus());
            row.createCell(11).setCellValue(bc.getItemqty());
            row.createCell(12).setCellValue(bc.getItemprice());
            row.createCell(13).setCellValue(bc.getOrderdate());
        }
        return wb;
    }


    private HSSFWorkbook genOrderPurchaseListExcelGroup(List<OrderPurchase> orderPurchaseList, String title) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(title);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ????????????????????????

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("?????????");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("?????????");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("????????????PID");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("????????????(USD)");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("1688?????????");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("1688?????????");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("??????????????????(RMB)");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("??????????????????");
        cell.setCellStyle(style);

        for (int i = 0; i < orderPurchaseList.size(); i++) {
            row = sheet.createRow((int) i + 1);
            OrderPurchase bc = orderPurchaseList.get(i);
            // ??????????????????????????????????????????
            row.createCell(0).setCellValue(bc.getYear());
            row.createCell(1).setCellValue(bc.getOrderid());
            row.createCell(2).setCellValue(bc.getOrderpaytime());
            row.createCell(3).setCellValue(bc.getOd_pid());
            row.createCell(4).setCellValue(bc.getYourorder());
            row.createCell(5).setCellValue(bc.getGoodsprice());
            if (bc.getOd_state() == 0) {
                row.createCell(6).setCellValue("?????????");
            } else if (bc.getOd_state() == 1) {
                row.createCell(6).setCellValue("??????");
            } else if (bc.getOd_state() == -1 || bc.getOd_state() == 2) {
                row.createCell(6).setCellValue("??????");
            } else {
                row.createCell(6).setCellValue("");
            }
            row.createCell(7).setCellValue(bc.getTb_orderid());
            row.createCell(8).setCellValue(bc.getShipno());
            row.createCell(9).setCellValue(bc.getItemurl());
            row.createCell(10).setCellValue(bc.getOrderstatus());
            row.createCell(11).setCellValue(bc.getItemqty());
            row.createCell(12).setCellValue(bc.getTotalprice());
            row.createCell(13).setCellValue(bc.getOrderdate());
        }
        return wb;
    }

    private HSSFWorkbook genOrderPurchaseListExcelTotal(List<OrderPurchase> orderPurchaseList, String title) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(title);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ????????????????????????

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("?????????");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("?????????");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("????????????(RMB)");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("1688?????????");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("1688?????????");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("????????????");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("????????????(RMB)");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("??????????????????");
        cell.setCellStyle(style);

        for (int i = 0; i < orderPurchaseList.size(); i++) {
            row = sheet.createRow((int) i + 1);
            OrderPurchase bc = orderPurchaseList.get(i);
            // ??????????????????????????????????????????
            row.createCell(0).setCellValue(bc.getYear());
            row.createCell(1).setCellValue(bc.getOrderid());
            row.createCell(2).setCellValue(bc.getOrderpaytime());
            row.createCell(3).setCellValue(bc.getYourorder());
            row.createCell(4).setCellValue(bc.getGoodsprice());
            row.createCell(5).setCellValue(bc.getTb_orderid());
            row.createCell(6).setCellValue(bc.getShipno());
            row.createCell(7).setCellValue(bc.getItemqty());
            row.createCell(8).setCellValue(bc.getTotalprice());
            row.createCell(9).setCellValue(bc.getOrderdate());
        }
        return wb;
    }
}
