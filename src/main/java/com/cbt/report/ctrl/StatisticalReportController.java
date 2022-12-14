package com.cbt.report.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.cbt.Specification.bean.AliCategory;
import com.cbt.Specification.util.DateFormatUtil;
import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.*;
import com.cbt.pojo.GeneralReportExample.Criteria;
import com.cbt.processes.servlet.Currency;
import com.cbt.report.service.*;
import com.cbt.report.vo.NoAvailableRate;
import com.cbt.report.vo.StatisticalReportVo;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SysParamUtil;
import com.cbt.util.Util;
import com.cbt.warehouse.pojo.Shipments;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.cbt.warehouse.pojo.Tb1688Account;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.warehouse.util.ExcelUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.InventoryData;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.AliBillingDetails;
import com.importExpress.pojo.AliPayInfo;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/StatisticalReport")
public class StatisticalReportController {

    @Autowired
    private BasicReportService basicReportService;

    @Autowired
    private ReportInfoService reportInfoService;

    @Autowired
    private GeneralReportService generalReportService;

    @Autowired
    private ReportDetailService reportDetailService;

    @Autowired
    private PurchaseReportService purchaseReportService;

    @Autowired
    private TaobaoOrderService taoBaoOrderService;

    @Autowired
    private IWarehouseService iWarehouseService;

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @return JSONArray
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title getHavebarcode
     * @Description TODO
     */
    @RequestMapping(value = "/getHavebarcode", method = RequestMethod.GET)
    @ResponseBody
    protected JSONArray getHavebarcode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        List<Inventory> list = new ArrayList<Inventory>();
        Inventory i = new Inventory();
        i.setBarcode("??????");
        i.setId(0);
        list.add(i);
        List<Inventory> list1 = iWarehouseService.getHavebarcode();
        for (Inventory inventory : list1) {
            list.add(inventory);
        }
        JSONArray jsonArr = JSONArray.parseArray(JSONArray.toJSONString(list));
        return jsonArr;
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    @ResponseBody
    protected JSONArray getAllUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        List<Inventory> list = new ArrayList<Inventory>();
        Inventory i = new Inventory();
        i.setBarcode("??????");
        list.add(i);
        List<Inventory> list1 = iWarehouseService.getAllUser();
        for (Inventory inventory : list1) {
            list.add(inventory);
        }
        JSONArray jsonArr = JSONArray.parseArray(JSONArray.toJSONString(list));
        return jsonArr;
    }

    /**
     * ????????????(??????????????????)
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @param timeFrom
     * @param timeTo
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/addReport")
    @ResponseBody
    public JsonResult addReport(HttpServletRequest request, @Param("reportYear") String reportYear,
                                @Param("reportMonth") String reportMonth, @Param("timeFrom") String timeFrom,
                                @Param("timeTo") String timeTo, @Param("type") String type) throws ParseException {
        JsonResult json = new JsonResult();
        // ??????????????????
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        // ???????????????
        GeneralReport generalReport = new GeneralReport();
        generalReport.setCreateOpertor(adm.getAdmName());
        generalReport.setReportYear(reportYear);
        generalReport.setReportMonth(reportMonth);
        if (type.equalsIgnoreCase("order")) {
            generalReport.setBreportId(2);
            generalReport.setBreportName("????????????");
        } else {
            generalReport.setBreportId(1);
            generalReport.setBreportName("????????????");
        }
        // ????????????
        Map<String, Double> maphl = Currency.getMaphl(request);
        GeneralReport generalReport1 = null;
        // ????????????????????????????????????
        generalReport1 = generalReportService.selectBySelective(generalReport);
        if (generalReport1 != null) {
            // ??????Generreport
            GeneralReportExample example = new GeneralReportExample();
            Criteria criteria = example.createCriteria();
            criteria.andIdEqualTo(generalReport1.getId());
            generalReportService.deleteByExample(example);
            // ??????report_info
            ReportInfoExample example1 = new ReportInfoExample();
            com.cbt.pojo.ReportInfoExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andGenralReportIdEqualTo(generalReport1.getId());
            reportInfoService.deleteByExample(example1);
            // ??????Report_detail
            ReportDetailExample example2 = new ReportDetailExample();
            com.cbt.pojo.ReportDetailExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andGenralReportIdEqualTo(generalReport1.getId());
            reportDetailService.deleteByExample(example2);

        }
        int generalReportId = generalReportService.insertSelective(generalReport);

        // ???????????????????????? ????????????????????????
        StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        statisticalReportVo.setTimeFrom(sdf.parse(timeFrom));
        statisticalReportVo.setTimeTo(sdf.parse(timeTo));
        // ???????????????????????? ????????????????????????
        List<ReportDetail> reportDetailList = new ArrayList<ReportDetail>();
        List<ReportDetail> reportDetailListN = new ArrayList<ReportDetail>();
        List<ReportDetail> reportDetailListO = new ArrayList<ReportDetail>(); // ????????????????????????
        double rmb = (Double) maphl.get("RMB");
        statisticalReportVo.setCurrency(rmb);
        reportDetailList = type.equalsIgnoreCase("order") ? reportDetailService.selectOrderDetailByTime(timeFrom, timeTo) : reportDetailService.selectReportDetailByTime(timeFrom, timeTo);
        Double salesPrice = 0.0, freightSum = 0.0, purchasePrice = 0.0, usePayPrice = 0.0, averagePrice = 0.0, buyAveragePrice = 0.0, profitLoss = 0.0;
        int salesVolumes = 0, buycount = 0;
        int row = 0;
        if (reportDetailList.size() > 0) {
            for (ReportDetail reportDetail : reportDetailList) {
                if (type.equalsIgnoreCase("order")) {
                    // ?????????????????????????????????????????????
                    Double freight = reportDetail.getFreight();
                    // ??????genralReportId
                    reportDetail.setGenralReportId(generalReportId);
                    // ?????????????????????
                    Double ave = reportDetail.getBuyAveragePrice();
                    freightSum += freight;
                    salesPrice += reportDetail.getSalesPrice();
                    salesVolumes += reportDetail.getSalesVolumes();
                    buyAveragePrice += reportDetail.getBuyAveragePrice();
                    purchasePrice += reportDetail.getPurchasePrice();
                    buycount += reportDetail.getBuycount();
                    usePayPrice += reportDetail.getUsePayPrice();
                    reportDetailListO.add(reportDetail);

                } else {// ?????????????????????????????????
                    salesPrice += reportDetail.getSalesPrice();
                    salesVolumes += reportDetail.getSalesVolumes();
                    purchasePrice += reportDetail.getPurchasePrice();
                    buycount += reportDetail.getBuycount();
                    reportDetail.setGenralReportId(generalReportId);
                    reportDetailListN.add(reportDetail);
                }
            }
            if (type.equalsIgnoreCase("order") && reportDetailListO != null) {
                reportDetailService.insertReportDetailListO(reportDetailListO);
            }
            if (type != null && !type.equalsIgnoreCase("order") && reportDetailListN != null) {
                reportDetailService.insertReportDetailList(reportDetailListN);
            }
            // ??????????????????
            ReportInfo reportInfo = new ReportInfo();
            // ?????????????????????
            reportInfo.setBuyCount(buycount);
            reportInfo.setGoodsCount(salesVolumes);
            reportInfo.setInvalidPurchaseNum(buycount - salesVolumes);
            reportInfo.setTotalExpenditure(purchasePrice);
            reportInfo.setTotalRevenue(salesPrice);
            if (salesVolumes == 0) {
                reportInfo.setAveragePrice(0.0);
            } else {
                reportInfo.setAveragePrice(salesPrice / salesVolumes);
            }
            if (type != null && type.equalsIgnoreCase("order")) {
                reportInfo.setOrderNum(reportDetailList.size());
                reportInfo.setFreight(freightSum);
                if (usePayPrice == 0.0) {
                    reportInfo.setProfitLoss(0.0D);
                } else {
                    reportInfo.setProfitLoss((usePayPrice - purchasePrice - freightSum) / usePayPrice * 100);
                }
                reportInfo.setBreportId(2);
            } else {
                reportInfo.setBreportId(1);
                if (salesPrice == 0.0) {
                    reportInfo.setProfitLoss(0.0D);
                } else {
                    reportInfo.setProfitLoss((salesPrice - purchasePrice) / salesPrice * 100);
                }
                reportInfo.setCategroyNum(reportDetailList.size());
            }
            if (generalReportId != 0) {
                reportInfo.setGenralReportId(generalReportId);
                row = reportInfoService.insertSelective(reportInfo);
            }
        }
        // ?????????????????????????????????
        if (row > 0) {
            json.setOk(true);
        } else {
            json.setMessage("???????????????");
            json.setOk(false);
        }
        return json;
    }

    /**
     * ?????????????????????????????? ????????? 2018-06-21
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @param timeFrom
     * @param timeTo
     * @param type
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/addReport2")
    @ResponseBody
    public JsonResult addReport2(HttpServletRequest request, @Param("reportYear") String reportYear,
                                 @Param("reportMonth") String reportMonth, @Param("timeFrom") String timeFrom,
                                 @Param("timeTo") String timeTo, @Param("type") String type) throws ParseException {
        JsonResult json = new JsonResult();
        // ??????????????????
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        Map<String, String> map = new HashMap<String, String>(1);
        try {
            String time = reportYear + "-" + (Integer.valueOf(reportMonth) < 10 ? "0" + reportMonth : reportMonth);
            map.put("time", time);
            generalReportService.insertOrderReport(map);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
        }
        return json;
    }

    @RequestMapping(value = "/addReport2_bak")
    @ResponseBody
    public JsonResult addReport2_bak(HttpServletRequest request, @Param("reportYear") String reportYear,
                                     @Param("reportMonth") String reportMonth, @Param("timeFrom") String timeFrom,
                                     @Param("timeTo") String timeTo, @Param("type") String type) throws ParseException {
        JsonResult json = new JsonResult();
        // ??????????????????
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        // ???????????????
        GeneralReport generalReport = new GeneralReport();
        generalReport.setCreateOpertor(adm.getAdmName());
        generalReport.setReportYear(reportYear);
        generalReport.setReportMonth(reportMonth);
        generalReport.setBreportId(2);
        generalReport.setBreportName("????????????");

        timeFrom = request.getParameter("timeFrom");
        timeTo = request.getParameter("timeTo");
        // ????????????
        Map<String, Double> maphl = Currency.getMaphl(request);
        GeneralReport generalReport1 = null;
        // ????????????????????????????????????
        generalReport1 = generalReportService.selectBySelective(generalReport);
        if (generalReport1 != null) {
            // ??????Generreport
            GeneralReportExample example = new GeneralReportExample();
            Criteria criteria = example.createCriteria();
            criteria.andIdEqualTo(generalReport1.getId());
            generalReportService.deleteByExample(example);
            // ??????report_info
            ReportInfoExample example1 = new ReportInfoExample();
            com.cbt.pojo.ReportInfoExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andGenralReportIdEqualTo(generalReport1.getId());
            reportInfoService.deleteByExample(example1);
            // ??????Report_detail_order
            ReportDetailExample example2 = new ReportDetailExample();
            com.cbt.pojo.ReportDetailExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andGenralReportIdEqualTo(generalReport1.getId());
            reportDetailService.deleteReportDetailOrder(example2);
            // generalReportService.deleteReportDetailOrder(generalReport);
        }
        int generalReportId = generalReportService.insertSelective(generalReport);

        // ???????????????????????? ????????????????????????
        StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        statisticalReportVo.setTimeFrom(sdf.parse(timeFrom));
        statisticalReportVo.setTimeTo(sdf.parse(timeTo));

        // ???????????????????????? ????????????????????????
        List<ReportDetailOrder> reportDetailList = new ArrayList<ReportDetailOrder>();
        // List<ReportDetail> reportDetailListO = new ArrayList<ReportDetail>();
        // //????????????????????????
        double rmb = (Double) maphl.get("RMB");
        statisticalReportVo.setCurrency(rmb);

        reportDetailList = reportDetailService.selectOrderReportDetailss(timeFrom, timeTo);

        Double salesPrice = 0.0, freightSum = 0.0, purchasePrice = 0.0, salesPriceAll = 0.0, purchasePriceAll = 0.0,
                usePayPrice = 0.0, averagePrice = 0.0, buyAveragePrice = 0.0, profitLoss = 0.0;
        int categroyNum = 0, salesVolumes = 0, buycount = 0;
        int row = 0;
        String pid = "";
        if (reportDetailList.size() > 0) {
            for (ReportDetailOrder reportDetail : reportDetailList) {
                // ?????????????????????????????????????????????
                Double freight = reportDetail.getFreight();
                // ??????genralReportId
                reportDetail.setGenralReportId(generalReportId);
                // //?????????????????????
                freightSum += freight;
                salesPrice = salesPrice + reportDetail.getSalesPrice();
                salesVolumes = salesVolumes + reportDetail.getSalesVolumes();
                purchasePrice += reportDetail.getPurchasePrice();
                buycount += reportDetail.getBuycount();
                usePayPrice += reportDetail.getSalesPrice();
                // ?????????????????????
                int salec = reportDetail.getSalesVolumes() == 0 ? 1 : reportDetail.getSalesVolumes();
                int buyc = reportDetail.getBuycount() == 0 ? 1 : reportDetail.getBuycount();
                buyAveragePrice += reportDetail.getPurchasePrice() / buyc;
                reportDetail.setAveragePrice(reportDetail.getSalesPrice() / salec);
                reportDetail.setBuyAveragePrice(reportDetail.getPurchasePrice() / buyc);
                double salep = reportDetail.getSalesPrice() == 0 ? 1 : reportDetail.getSalesPrice();
                reportDetail.setProfitLoss((salep - reportDetail.getPurchasePrice() - reportDetail.getFreight()) / salep * 100);
            }
            if (reportDetailList != null && reportDetailList.size() > 0) {
                reportDetailService.insertReportDetailOrder(reportDetailList);
            }
            // ??????????????????
            ReportInfo reportInfo = new ReportInfo();
            // ?????????????????????
            reportInfo.setBuyCount(buycount);
            reportInfo.setGoodsCount(salesVolumes);
            reportInfo.setInvalidPurchaseNum(buycount - salesVolumes);
            reportInfo.setTotalExpenditure(purchasePrice);
            reportInfo.setTotalRevenue(salesPrice);
            if (salesVolumes == 0) {
                reportInfo.setAveragePrice(0.0);
            } else {
                reportInfo.setAveragePrice(salesPrice / salesVolumes);
            }
            reportInfo.setOrderNum(reportDetailList.size());
            reportInfo.setFreight(freightSum);
            if (usePayPrice == 0.0) {
                reportInfo.setProfitLoss(0.0D);
            } else {
                reportInfo.setProfitLoss((salesPrice - purchasePrice - freightSum) / salesPrice * 100);
            }
            reportInfo.setBreportId(2);
            if (generalReportId != 0) {
                reportInfo.setGenralReportId(generalReportId);
                row = reportInfoService.insertSelective(reportInfo);
            }
        }
        // ?????????????????????????????????
        if (row > 0) {
            json.setOk(true);
        } else {
            json.setMessage("???????????????");
            json.setOk(false);
        }
        return json;
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/selectReport")
    @ResponseBody
    public JsonResult selectReport(HttpServletRequest request, @Param("reportYear") String reportYear,
                                   @Param("reportMonth") String reportMonth, @Param("orderName") String orderName,
                                   @Param("type") String type) {
        JsonResult json = new JsonResult();
        Map<String, String> map = new HashMap<String, String>(3);
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        map.put("page", String.valueOf(page));
        map.put("orderName", StringUtil.isBlank(orderName) ? null : orderName);
        map.put("time", reportYear + "-" + (Integer.valueOf(reportMonth) < 10 ? "0" + reportMonth : reportMonth));
        List<StatisticalReportPojo> list = reportInfoService.getCategoryReport(map);
        List<StatisticalReportPojo> listCount = reportInfoService.getCategoryReportCount(map);
        List<StatisticalReportPojo> allList = reportInfoService.getAllCategoryReport(map);
        json.setData(list);
        json.setAllData(allList);
        json.setTotal((long) listCount.size());
        return json;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/selectReport1")
    @ResponseBody
    public JsonResult selectReport1(HttpServletRequest request, @Param("reportYear") String reportYear,
                                    @Param("reportMonth") String reportMonth, @Param("orderName") String orderName,
                                    @Param("type") String type) {
        JsonResult json = new JsonResult();
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        // ??????????????????????????????
        int noFreeFreight = 3;
        if (request.getParameter("noFreeFreight") != null) {
            noFreeFreight = Integer.parseInt(request.getParameter("noFreeFreight"));
        }
        GeneralReport generalReport = new GeneralReport();
        generalReport.setReportYear(reportYear);
        generalReport.setReportMonth(reportMonth);
        generalReport.setNoFreeFreight(noFreeFreight);
        double expenditure = 0.00;
        double allSalesPrice = 0.00;
        double count = 0.00;
        double sumFreight = 0.00;
        int procurementCount = 0;
        int salesCount = 0;
        if (type != null && type.equalsIgnoreCase("order")) {
            generalReport.setBreportId(2);
        } else {
            generalReport.setBreportId(1);
        }
        List<ReportInfo> reportInfoList = reportInfoService.selectByGeneral(generalReport);
        // ????????????????????????
        StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
        if (reportInfoList != null && reportInfoList.size() > 0) {
            List<ReportDetail> reportDetailList = reportDetailService.selectByGeneral(generalReport, orderName,
                    (page - 1) * 20);

            int datacount = reportDetailService.selectByGeneralCount(generalReport);
            if (reportDetailList.size() > 0 && reportDetailList != null) {
                reportDetailList.get(0).setDatacount(datacount);
            }
            statisticalReportVo.setReportDetailList(reportDetailList);
            if (noFreeFreight == 1 && reportDetailList.size() > 0) {// whj
                ReportInfo ri = new ReportInfo();
                ri.setOrderNum(reportDetailList.size());
                for (int i = 0; i < reportDetailList.size(); i++) {
                    ReportDetail rd = reportDetailList.get(i);
                    expenditure += rd.getPurchasePrice();// ?????????
                    allSalesPrice += rd.getSalesPrice();// ?????????
                    count += rd.getBuycount();// ?????????
                    sumFreight += rd.getFreight();// ?????????
                    procurementCount += rd.getBuycount();
                    salesCount += rd.getSalesVolumes();
                }
                double averagePrice = count > 0 ? allSalesPrice / count : 0.00;// ?????????
                ri.setTotalExpenditure(expenditure);
                ri.setTotalRevenue(allSalesPrice);
                ri.setGoodsCount((int) count);
                ri.setAveragePrice(averagePrice);
                ri.setFreight(sumFreight);
                double av = (allSalesPrice - expenditure) / allSalesPrice * 100;
                ri.setProfitLoss(av);
                ri.setRedundantCount(procurementCount - salesCount);
                reportInfoList.clear();
                reportInfoList.add(ri);
                statisticalReportVo.setReportInfoList(reportInfoList);
            } else if (noFreeFreight == 1 && reportDetailList.size() == 0) {
                reportInfoList.clear();
                statisticalReportVo.setReportInfoList(reportInfoList);
            } else {
                for (int i = 0; i < reportDetailList.size(); i++) {
                    ReportDetail rd = reportDetailList.get(i);
                    procurementCount += rd.getBuycount();
                    salesCount += rd.getSalesVolumes();
                }
                ReportInfo rt = reportInfoList.get(0);
                rt.setRedundantCount(procurementCount - salesCount);
                reportInfoList.clear();
                reportInfoList.add(rt);
                statisticalReportVo.setReportInfoList(reportInfoList);
            }
        }

        json.setData(statisticalReportVo);
        if (reportInfoList != null && reportInfoList.size() > 0) {
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    /**
     * ?????????????????????????????? ?????????2018-06-21
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @param orderName
     * @param type
     * @return
     */
    @RequestMapping(value = "/selectReport2")
    @ResponseBody
    public JsonResult selectReport2(HttpServletRequest request, @Param("reportYear") String reportYear,
                                    @Param("reportMonth") String reportMonth, @Param("orderName") String orderName,
                                    @Param("type") String type) {
        JsonResult json = new JsonResult();
        Map<String, String> map = new HashMap<String, String>(1);
        try {
            int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
            page = (page - 1) * 20;
            map.put("time", reportYear + "-" + (Integer.valueOf(reportMonth) < 10 ? "0" + reportMonth : reportMonth));
            map.put("page", String.valueOf(page));
            map.put("orderName", orderName);
            List<StatisticalReportPojo> list = reportDetailService.getOrderReportList(map);
            List<StatisticalReportPojo> listCount = reportDetailService.getOrderReportListCount(map);
            List<StatisticalReportPojo> AllList = reportDetailService.getAllOrderReportList(map);
            json.setData(list);
            json.setAllData(AllList);
            json.setTotal((long) listCount.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping(value = "/selectReport2_bak")
    @ResponseBody
    public JsonResult selectReport2_bak(HttpServletRequest request, @Param("reportYear") String reportYear,
                                        @Param("reportMonth") String reportMonth, @Param("orderName") String orderName,
                                        @Param("type") String type) {
        JsonResult json = new JsonResult();
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        // ??????????????????????????????
        int noFreeFreight = 3;
        if (request.getParameter("noFreeFreight") != null) {
            noFreeFreight = Integer.parseInt(request.getParameter("noFreeFreight"));
        }
        GeneralReport generalReport = new GeneralReport();
        generalReport.setReportYear(reportYear);
        generalReport.setReportMonth(reportMonth);
        generalReport.setNoFreeFreight(noFreeFreight);
        generalReport.setBreportId(2);
        List<ReportInfo> reportInfoList = reportInfoService.selectByGeneral(generalReport);
        // ????????????????????????
        StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
        if (reportInfoList != null && reportInfoList.size() > 0) {
            List<ReportDetailOrder> reportDetailList = reportDetailService.selectReportOrder(generalReport, orderName,
                    (page - 1) * 20);
            int datacount = reportDetailService.selectReportOrderCount(generalReport);
            if (reportDetailList.size() > 0 && reportDetailList != null) {
                reportDetailList.get(0).setDatacount(datacount);
            }
            statisticalReportVo.setReportDetailOrder(reportDetailList);

            List<ReportDetailOrder> rdo_list = reportDetailService.selectReportOrderTotal(generalReport); // ??????????????????
            ReportInfo rp = new ReportInfo();

            ReportDetailOrder rdo = rdo_list.get(0);
            rp.setTotalExpenditure(rdo.getPurchasePrice()); // ??????????????????
            rp.setTotalRevenue(rdo.getSalesPrice()); // ??????????????????
            rp.setGoodsCount(rdo.getSalesVolumes());
            rp.setRedundantCount(rdo.getBuycount() - rdo.getSalesVolumes());
            rp.setOrderNum(datacount);
            rp.setFreight(rdo.getFreight());

            reportInfoList.clear();
            reportInfoList.add(rp);
            statisticalReportVo.setReportInfoList(reportInfoList);
        }

        json.setData(statisticalReportVo);
        if (reportInfoList != null && reportInfoList.size() > 0) {
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    /**
     * ??????Excel
     *
     * @throws ParseException
     * @throws Exception
     */

    @RequestMapping(value = "/export")
    public void exportExcel(HttpServletRequest request, @Param("reportYear") String reportYear,
                            @Param("reportMonth") String reportMonth, @Param("orderName") String orderName, @Param("type") String type,
                            HttpServletResponse response) throws Exception {
        int noFreeFreight = 3;
        double expenditure = 0.00;
        double allSalesPrice = 0.00;
        double count = 0.00;
        double sumFreight = 0.00;
        int procurementCount = 0;
        int salesCount = 0;
        orderName = "sales_volumes";
        if (request.getParameter("noFreeFreight") != null && !request.getParameter("noFreeFreight").equals("")) {
            noFreeFreight = Integer.valueOf(request.getParameter("noFreeFreight"));
        }
        // ??????????????????????????????
        GeneralReport generalReport = new GeneralReport();
        generalReport.setReportYear(reportYear);
        generalReport.setReportMonth(reportMonth);
        generalReport.setNoFreeFreight(noFreeFreight);
        if (type.equalsIgnoreCase("order")) {
            generalReport.setBreportId(2);
        } else {
            generalReport.setBreportId(1);
        }
        List<ReportInfo> reportInfoList = reportInfoService.selectByGeneral(generalReport);
        // ????????????????????????
        StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
        statisticalReportVo.setYear(reportYear);
        statisticalReportVo.setMonth(reportMonth);
        if (reportInfoList != null && reportInfoList.size() > 0) {
            List<ReportDetail> reportDetailList = reportDetailService.selectByGeneral(generalReport, orderName, -1);
            statisticalReportVo.setReportDetailList(reportDetailList);
            // whj
            if (noFreeFreight == 1 && reportDetailList.size() > 0) {// whj
                ReportInfo ri = new ReportInfo();
                ri.setOrderNum(reportDetailList.size());
                for (int i = 0; i < reportDetailList.size(); i++) {
                    ReportDetail rd = reportDetailList.get(i);
                    expenditure += rd.getPurchasePrice();// ?????????
                    allSalesPrice += rd.getSalesPrice();// ?????????
                    count += rd.getBuycount();// ?????????
                    sumFreight += rd.getFreight();// ?????????
                    procurementCount += rd.getBuycount();
                    salesCount += rd.getSalesVolumes();
                }
                double averagePrice = count > 0 ? allSalesPrice / count : 0.00;// ?????????
                ri.setTotalExpenditure(expenditure);
                ri.setTotalRevenue(allSalesPrice);
                ri.setGoodsCount((int) count);
                ri.setAveragePrice(new BigDecimal(averagePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                ri.setFreight(sumFreight);
                double av = (allSalesPrice - expenditure) / allSalesPrice * 100;// ???????????????
                ri.setProfitLoss(new BigDecimal(av).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                ri.setRedundantCount(procurementCount - salesCount);
                reportInfoList.clear();
                reportInfoList.add(ri);
                statisticalReportVo.setReportInfoList(reportInfoList);
            } else if (noFreeFreight == 1 && reportDetailList.size() == 0) {
                reportInfoList.clear();
                statisticalReportVo.setReportInfoList(reportInfoList);
            } else {
                statisticalReportVo.setReportInfoList(reportInfoList);
            }
            HSSFWorkbook wb = generalReportService.export(statisticalReportVo, type);
            response.setContentType("application/vnd.ms-excel");
            Date date = new Date(System.currentTimeMillis());
            int year = date.getYear() + 1900;
            String filename = "";
            if (type.equalsIgnoreCase("order")) {
                filename = "OrderCategroy" + year;
            } else if (type.equalsIgnoreCase("categroy")) {
                filename = "GoodsCategroy" + year;
            }
            filename = StringUtils.getFileName(filename);
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        }
    }

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/exportOrder")
    public void exportOrderExcel(HttpServletRequest request, @Param("reportYear") String reportYear,
                                 @Param("reportMonth") String reportMonth, @Param("orderName") String orderName, @Param("type") String type,
                                 HttpServletResponse response) throws Exception {
        int noFreeFreight = 3;
        orderName = "sales_volumes";
        if (request.getParameter("noFreeFreight") != null && !request.getParameter("noFreeFreight").equals("")) {
            noFreeFreight = Integer.valueOf(request.getParameter("noFreeFreight"));
        }
        try {
            // ??????????????????????????????
            GeneralReport generalReport = new GeneralReport();
            generalReport.setReportYear(reportYear);
            generalReport.setReportMonth(reportMonth);
            generalReport.setNoFreeFreight(noFreeFreight);
            generalReport.setBreportId(2);
            List<ReportInfo> reportInfoList = reportInfoService.selectByGeneral(generalReport);
            // ????????????????????????
            StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
            statisticalReportVo.setYear(reportYear);
            statisticalReportVo.setMonth(reportMonth);
            if (reportInfoList != null && reportInfoList.size() > 0) {
                List<ReportDetailOrder> reportDetailList = reportDetailService.selectReportOrder(generalReport, orderName, -1);
                statisticalReportVo.setReportDetailOrder(reportDetailList);
                List<ReportDetailOrder> rdo_list = reportDetailService.selectReportOrderTotal(generalReport); // ??????????????????
                ReportInfo rp = new ReportInfo();
                ReportDetailOrder rdo = rdo_list.get(0);
                rp.setTotalExpenditure(rdo.getPurchasePrice()); // ??????????????????
                rp.setTotalRevenue(rdo.getSalesPrice()); // ??????????????????
                rp.setGoodsCount(rdo.getSalesVolumes());
                rp.setRedundantCount(rdo.getBuycount() - rdo.getSalesVolumes());
                rp.setOrderNum(reportDetailList.size());
                rp.setFreight(rdo.getFreight());
                rp.setAveragePrice(rdo.getSalesPrice() / rdo.getSalesVolumes());
                rp.setProfitLoss((rdo.getSalesPrice() - rdo.getPurchasePrice() - rdo.getFreight()) / rdo.getSalesPrice() * 100);

                reportInfoList.clear();
                reportInfoList.add(rp);
                statisticalReportVo.setReportInfoList(reportInfoList);
                HSSFWorkbook wb = generalReportService.export(statisticalReportVo, type);
                // String uuid = "export"+UUID.randomUUID().toString();
                response.setContentType("application/vnd.ms-excel");
                // response.setHeader("Content-disposition",
                // "attachment;filename="+uuid);
                Date date = new Date(System.currentTimeMillis());
                int year = date.getYear() + 1900;
                String filename = "";
                filename = "OrderCategroy" + year;
                filename = StringUtils.getFileName(filename);
                response.setHeader("Content-disposition", "attachment;filename=" + filename);
                OutputStream ouputStream = response.getOutputStream();
                wb.write(ouputStream);
                ouputStream.flush();
                ouputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/showGoods")
    @ResponseBody
    public JsonResult showGoods(HttpServletRequest request, @Param("timeFrom") String timeFrom,
                                @Param("timeTo") String timeTo, @Param("categroy") String categroy) throws ParseException {
        JsonResult json = new JsonResult();
        if (categroy.equals("?????????") || categroy == null || "".equals(categroy)) {
            json.setOk(false);
            return json;
        }
        // ????????????????????????
        StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
        if (categroy.lastIndexOf("^^") > 0) {
            categroy = categroy.substring(categroy.lastIndexOf("^^") + 2, categroy.length());
        }
        statisticalReportVo.setCategory(categroy);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        statisticalReportVo.setTimeFrom(sdf.parse(timeFrom));
        statisticalReportVo.setTimeTo(sdf.parse(timeTo));
        List<OrderProductSource> orderProductSourceList = reportDetailService.selectByTimeAndCategroy(statisticalReportVo);
        if (orderProductSourceList != null && orderProductSourceList.size() > 0) {
            json.setData(orderProductSourceList);
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/showGoodsByOrder")
    @ResponseBody
    public JsonResult showGoodsByOrder(HttpServletRequest request, @Param("timeFrom") String timeFrom,
                                       @Param("timeTo") String timeTo, @Param("categroy") String categroy) throws ParseException {
        JsonResult json = new JsonResult();
        // ????????????????????????
        StatisticalReportVo statisticalReportVo = new StatisticalReportVo();
        statisticalReportVo.setCategory(categroy);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        statisticalReportVo.setTimeFrom(sdf.parse(timeFrom));
        statisticalReportVo.setTimeTo(sdf.parse(timeTo));
        List<OrderProductSource> orderProductSourceList = reportDetailService.selectByTimeAndOrder(statisticalReportVo);

        if (orderProductSourceList != null && orderProductSourceList.size() > 0) {
            json.setData(orderProductSourceList);
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    /**
     * @Title: orderReport @Description: ???????????????????????? @param @param
     * mav @param @param request @param @return ???????????? @return
     * ModelAndView ???????????? @throws
     */
    @RequestMapping(value = "/orderReport")
    public ModelAndView orderReport(ModelAndView mav, HttpServletRequest request) {
        mav.addObject("type", "order");
        mav.setViewName("/orderReport");
        return mav;
    }

    /**
     * @Title: orderReport @Description: ???????????????????????? @param @param
     * mav @param @param request @param @return ???????????? @return
     * ModelAndView ???????????? @throws
     */
    @RequestMapping(value = "/orderReport1")
    public ModelAndView orderReport1(ModelAndView mav, HttpServletRequest request) {
        mav.setViewName("/orderReport1");
        return mav;
    }

    /**
     * @Title: categroyReport @Description: ???????????????????????? @param @param
     * mav @param @param request @param @return ???????????? @return
     * ModelAndView ???????????? @throws
     */
    @RequestMapping(value = "/categroyReport")
    public ModelAndView categroyReport(ModelAndView mav, HttpServletRequest request) {
        mav.addObject("type", "categroy");
        mav.setViewName("/statisticalReport");
        return mav;
    }

    /**
     * @Description: ????????????????????????
     */
    @RequestMapping(value = "/salesReport")
    public ModelAndView salesReport(ModelAndView mav, HttpServletRequest request) {
        mav.addObject("type", "sales");
        mav.setViewName("/salesReport");
        return mav;
    }

    /**
     * @Description: ????????????????????????
     */
    @RequestMapping(value = "/purchaseReport")
    public ModelAndView purchaseReport(ModelAndView mav, HttpServletRequest request) {
        mav.addObject("type", "purchase");
        mav.setViewName("/purchaseReport");
        return mav;
    }

    /**
     * ?????????????????????????????????
     */
    @RequestMapping(value = "/UnpackPurchaseReport")
    public ModelAndView UnpackPurchaseReport(ModelAndView mav, HttpServletRequest request) {
        mav.setViewName("/UnpackPurchaseReport");
        return mav;
    }

    /**
     * ??????????????????
     */
    @RequestMapping("/outPackageReport")
    public ModelAndView outPackageReport(ModelAndView mav) {
        mav.setViewName("/outPackageReport");
        return mav;
    }

    /**
     * ??????????????????????????????
     */
    @RequestMapping("/taoBaoOrderReport")
    public ModelAndView taoBaoOrderReport(ModelAndView mav) {
        mav.setViewName("/taobaoOrderReport");
        return mav;
    }

    /**
     * ???????????????????????????
     */
    @RequestMapping("/inOutDetailsReport")
    public ModelAndView inOutDetailsReport(ModelAndView mav) {
        mav.setViewName("/inOutDetailsReport");
        return mav;
    }

    /**
     * ????????????????????????
     */
    @RequestMapping("/goodsInventoryReport")
    public ModelAndView goodsInventoryReport(ModelAndView mav) {
        mav.setViewName("goodsInventoryReport");
        return mav;
    }

    /**
     * ????????????????????????
     */
    @RequestMapping("/noAvailableRateStatistics")
    public ModelAndView noAvailableRateStatistics(ModelAndView mav) {
        mav.setViewName("/noAvailableRateStatistics");
        return mav;
    }

    /**
     * ????????????????????????
     */
    @RequestMapping("/purchasingSpendingReport")
    public ModelAndView purchasingSpendingReport(ModelAndView mav) {
        mav.setViewName("/purchasingSpendingReport");
        return mav;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj
     */
    @RequestMapping(value = "/getErrorFileInfo")
    @ResponseBody
    public JsonResult getErrorFileInfo(HttpServletRequest request, HttpServletResponse response, Model model)
            throws ParseException, IOException {
        response.setCharacterEncoding("utf-8");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        System.out.println(yesterday);
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -2);
        String beforeYesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
        System.out.println(beforeYesterday);
        File dir = new File("F:/cbtconsole/log");
        JsonResult json = new JsonResult();
        List<DownFileInfo> list = new ArrayList<DownFileInfo>();
        TaoBaoInfoList Alllist = new TaoBaoInfoList();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.println("files[i].getName()==" + files[i].getName());
                if ("cbt.log".equals(files[i].getName()) || "pay.log".equals(files[i].getName())
                        || files[i].getName().indexOf(yesterday) > -1
                        || files[i].getName().indexOf(beforeYesterday) > -1) {
                    System.out.println("??????==" + files[i].getName());
                    DownFileInfo info = new DownFileInfo();
                    info.setFileName(files[i].getName());
                    list.add(info);
                }
            }
        }
        Alllist.setFileList(list);
        json.setData(Alllist);
        return json;
    }

    @RequestMapping(value = "/selectStatus")
    @ResponseBody
    public JsonResult selectStatus(HttpServletRequest request, HttpServletResponse response, Model model)
            throws ParseException, IOException {
        JsonResult json = new JsonResult();
        List<String> list = taoBaoOrderService.selectStatus();
        response.setCharacterEncoding("utf-8");
        request.setAttribute("list", list);
        json.setData(list);
        return json;
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj
     */
    @RequestMapping(value = "/getTaoBaoOrderDetailsInfo.do", method = RequestMethod.GET)
    public String getOrderInfo(HttpServletRequest request, HttpServletResponse response, Model model)
            throws ParseException, IOException {
        response.setCharacterEncoding("utf-8");
        String orderid = request.getParameter("orderid");
        System.out.println("?????????????????????:" + orderid);
        List<TaoBaoOrderInfo> orderids = taoBaoOrderService.associatedOrder(orderid);
        StringBuffer bforderid = new StringBuffer();
        StringBuffer bf = new StringBuffer();
        for (int j = 0; j < orderids.size(); j++) {
            TaoBaoOrderInfo info = orderids.get(j);
            bforderid.append("'" + info.getOpsorderid() + "',");
            bf.append(info.getOpsorderid() + ":???????????????" + info.getBuycount() + "\t");
        }
        String orders = "";
        orders = bforderid.toString().length() > 1
                ? bforderid.toString().substring(0, bforderid.toString().length() - 1) : "''";
        List<TaoBaoOrderInfo> list = taoBaoOrderService.getTaoBaoOrderDetails(orderid, orders);
        System.out.println("??????" + orderid + "??????[" + list.size() + "]?????????");
        if (list.size() > 0) {
            request.setAttribute("orderid", list.get(0).getOrderid());
            request.setAttribute("orderdate", list.get(0).getOrderdate());
            request.setAttribute("tbOt1688", list.get(0).getTbOr1688().equals("0") ? "??????" : "1688");
            request.setAttribute("orderstatus", list.get(0).getOrderstatus());
            request.setAttribute("totalprice", list.get(0).getTotalprice());
            request.setAttribute("paytreasureid", list.get(0).getPaytreasureid());
            request.setAttribute("merchantorderid", list.get(0).getMerchantorderid());
            request.setAttribute("seller", list.get(0).getSeller());
            request.setAttribute("username", list.get(0).getUsername());
            int totalqty = 0;
            for (int i = 0; i < list.size(); i++) {
                String regex = "\\d*";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(list.get(i).getItemqty().trim());
                if (m.find()) {
                    if (!"".equals(m.group()))
                        System.out.println("come here:" + m.group());
                    totalqty += Integer.valueOf(m.group());
                }
                System.out.println("===opsid=" + list.get(i).getOpsid());
            }
            List idList = taoBaoOrderService.isStorage(orderid);
            if (idList.size() > 0) {
                request.setAttribute("isStorage", "?????????");
            } else {
                request.setAttribute("isStorage", "?????????");
            }
            if (bf.length() > 0) {
                request.setAttribute("orderids", bf.toString());
            }
            List<TaoBaoOrderInfo> buyCount = taoBaoOrderService.queryBuyCount(orderid, orders);
            request.setAttribute("buyCount", JSONArray.toJSON(buyCount));
            request.setAttribute("totalqty", totalqty);
            request.setAttribute("list", JSONArray.toJSON(list));
        }

        return "taobaoOrderDetailsReport";
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportCategoryReport")
    public void exportCategoryReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>(3);
        String reportYear = request.getParameter("reportYear");
        String orderName = request.getParameter("orderName");
        String reportMonth = request.getParameter("reportMonth");
        try {
            map.put("orderName", StringUtil.isBlank(orderName) ? null : orderName);
            map.put("time", reportYear + "-" + (Integer.valueOf(reportMonth) < 10 ? "0" + reportMonth : reportMonth));
            List<StatisticalReportPojo> listCount = reportInfoService.getCategoryReportCount(map);
            System.out.println("infoList================" + listCount.size());
            HSSFWorkbook wb = generalReportService.exportCategoryList(listCount);
            response.setContentType("application/vnd.ms-excel");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String filename = "????????????????????????" + sdf.format(new Date());
            filename = StringUtils.getFileName(filename);
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/createUserProfitByMonth")
    public void createUserProfitByMonth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>(3);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        PrintWriter out = response.getWriter();
        int row = 0;
        try {
            String startTime = "";
            String endTime = "";
            List<OrderSalesAmountPojo> list = new ArrayList<OrderSalesAmountPojo>();
            List<Map<String, String>> monthLst = new ArrayList<Map<String, String>>();
            if ("0".equals(month)) {
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("beginDate", year + "-01-01 00:00:00");
                dataMap.put("endDate", year + "-12-31 23:59:59");
            } else {
                monthLst = Util.genQueryDate(year, month);
            }
            startTime = monthLst.get(0).get("beginDate");
            endTime = monthLst.get(monthLst.size() - 1).get("endDate");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("type", "1");
            //??????????????????????????????
            taoBaoOrderService.deleteUserProfitByMonth(year + "-" + endTime);
            list = taoBaoOrderService.getUserProfitByMonthCount(map);
            row = taoBaoOrderService.insertUserProfitBatch(list);
        } catch (Exception e) {
            System.out.println(" ?????????????????????????????????");
            e.printStackTrace();
        }
        out.print(row);
        out.close();
    }

    @RequestMapping(value = "/exportUserProfitByMonth")
    public void exportUserProfitByMonth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>(3);
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        try {
            String startTime = "";
            String endTime = "";
            List<OrderSalesAmountPojo> list = new ArrayList<OrderSalesAmountPojo>();
            if (!"0".equals(year) && !"0".equals(month)) {
                List<Map<String, String>> monthLst = Util.genQueryDate(year, month);
                startTime = monthLst.get(0).get("beginDate");
                endTime = monthLst.get(monthLst.size() - 1).get("endDate");
                map.put("startTime", startTime);
                map.put("endTime", endTime);
                map.put("type", "1");
                list = taoBaoOrderService.getUserProfitByMonthCount(map);
            }
            HSSFWorkbook wb = generalReportService.exportUserProfitByMonth(list);
            response.setContentType("application/vnd.ms-excel");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String filename = "???????????????????????????" + sdf.format(new Date());
            filename = StringUtils.getFileName(filename);
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            System.out.println("???????????????????????????");
            //e.printStackTrace();
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportLossInventoryExcel")
    public void exportLossInventoryExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            Map<Object, Object> map = new HashMap<Object, Object>();
            String barcode = request.getParameter("barcode");
            String startdate = request.getParameter("startdate");
            String enddate = request.getParameter("enddate");
            String admName = request.getParameter("admName");
            startdate = StringUtils.isStrNull(startdate) ? null : (startdate + " 00:00:00");
            enddate = StringUtils.isStrNull(enddate) ? null : (enddate + " 23:59:59");
            barcode = "0".equals(barcode) ? null : barcode;
            admName = "0".equals(admName) ? null : admName;
            map.put("barcode", barcode);
            map.put("startdate", startdate);
            map.put("enddate", enddate);
            map.put("admName", admName);
            List<LossInventoryPojo> toryListCount = taoBaoOrderService.searchLossInventoryCount(map);
            HSSFWorkbook wb = generalReportService.exportLossInventoryExcel(toryListCount);
            response.setContentType("application/vnd.ms-excel");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String filename = "????????????????????????" + sdf.format(new Date());
            filename = StringUtils.getFileName(filename);
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportDeleteInventoryExcel")
    public void exportDeleteInventoryExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            Map<Object, Object> map = new HashMap<Object, Object>();
            String barcode = request.getParameter("barcode");
            String startdate = request.getParameter("startdate");
            String enddate = request.getParameter("enddate");
            String admName = request.getParameter("admName");
            startdate = StringUtils.isStrNull(startdate) ? null : (startdate + " 00:00:00");
            enddate = StringUtils.isStrNull(enddate) ? null : (enddate + " 23:59:59");
            barcode = "0".equals(barcode) ? null : barcode;
            admName = "0".equals(admName) ? null : admName;
            map.put("barcode", barcode);
            map.put("startdate", startdate);
            map.put("enddate", enddate);
            map.put("admName", admName);
            List<Inventory> toryListCount = taoBaoOrderService.searchGoodsInventoryDeleteInfoCount(map);
            HSSFWorkbook wb = generalReportService.exportDeleteInventoryExcel(toryListCount);
            response.setContentType("application/vnd.ms-excel");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String filename = "????????????????????????" + sdf.format(new Date());
            filename = StringUtils.getFileName(filename);
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportUpdateInventoryExcel")
    public void exportUpdateInventoryExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            Map<Object, Object> map = new HashMap<Object, Object>();
            String barcode = request.getParameter("barcode");
            String startdate = request.getParameter("startdate");
            String enddate = request.getParameter("enddate");
            String admName = request.getParameter("admName");
            startdate = StringUtils.isStrNull(startdate) ? null : (startdate + " 00:00:00");
            enddate = StringUtils.isStrNull(enddate) ? null : (enddate + " 23:59:59");
            barcode = "0".equals(barcode) ? null : barcode;
            admName = "0".equals(admName) ? null : admName;
            map.put("barcode", barcode);
            map.put("startdate", startdate);
            map.put("enddate", enddate);
            map.put("admName", admName);
            List<Inventory> toryListCount = taoBaoOrderService.searchGoodsInventoryUpdateInfoCount(map);
            HSSFWorkbook wb = generalReportService.exportUpdateInventoryExcel(toryListCount);
            response.setContentType("application/vnd.ms-excel");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String filename = "????????????????????????" + sdf.format(new Date());
            filename = StringUtils.getFileName(filename);
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ??????Excel
     *
     * @throws ParseException
     * @throws Exception
     */

    @RequestMapping(value = "/exportTaoBaoOrder")
    public void exportExcelTaoBaoOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        String orderstatus = request.getParameter("orderstatus");// ???????????????
        String orderdate = "0";// ????????????
        String orderSource = request.getParameter("orderSource");// ????????????
        String amount = request.getParameter("amount");// ??????????????????
        String noCycle = request.getParameter("noCycle");// ???????????????
        String orderid = request.getParameter("orderid");// ???????????????
        String procurementAccount = request.getParameter("procurementAccount");// ????????????
        String isCompany = request.getParameter("isCompany");//
        String myorderid = request.getParameter("myorderid");//
        String enddate = request.getParameter("enddate");
        String startdate = request.getParameter("startdate");
        String payenddate = request.getParameter("payenddate");
        String paystartdate = request.getParameter("paystartdate");
        int start = 0;
        int end = 0;
        int DeliveryDate = 0;
        startdate = StringUtils.isStrNull(startdate) ? null : startdate;
        paystartdate = StringUtils.isStrNull(paystartdate) ? null : paystartdate;
        payenddate = !StringUtils.isStrNull(payenddate) ? payenddate + " 23:59:59" : null;
        enddate = !StringUtils.isStrNull(enddate) ? enddate + " 23:59:59" : null;
        orderid = StringUtils.isStrNull(orderid) ? null : orderid;
        myorderid = StringUtils.isStrNull(myorderid) ? null : myorderid;
        orderstatus = StringUtils.isStrNull(orderstatus) ? null : orderstatus;
        orderdate = orderdate.equals("0") ? null : orderdate;
        orderSource = orderSource.equals("-1") ? null : orderSource;
        procurementAccount = procurementAccount.equals("0") ? null : procurementAccount;
        if (noCycle.equals("0")) {
            noCycle = null;
        } else {
            DeliveryDate = Integer.valueOf(noCycle);
        }
        if (amount.equals("-1")) {
            amount = null;
        } else {
            start = Integer.valueOf(amount.split("~")[0]);
            end = Integer.valueOf(amount.split("~")[1]);
        }
        DecimalFormat df = new DecimalFormat("#0.###");
        double pagePrice = 0.00;
        List<TaoBaoOrderInfo> infoList = taoBaoOrderService.getTaoBaoOrderList(orderstatus, orderSource, orderdate,
                procurementAccount, start, end, DeliveryDate, orderid, isCompany, myorderid, startdate, enddate,
                paystartdate, payenddate, -1);
        for (int i = 0; i < infoList.size(); i++) {
            String totalPrice = infoList.get(i).getTotalprice();
            totalPrice = totalPrice.indexOf(",") > -1 ? totalPrice.replaceAll(",", "") : totalPrice;
            pagePrice += Double.valueOf(totalPrice);
        }
        System.out.println("infoList================" + infoList.size());
        HSSFWorkbook wb = generalReportService.exportTaoBaoOrder(infoList, Double.valueOf(df.format(pagePrice)));
        response.setContentType("application/vnd.ms-excel");
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        String filename = "taoBaoOrder" + year;
        filename = StringUtils.getFileName(filename);
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/searchTaoBaoOrder")
    @ResponseBody
    protected JsonResult searchTaoBaoOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setCharacterEncoding("utf-8");
        DecimalFormat df = new DecimalFormat("#0.###");
        JsonResult json = new JsonResult();
        int page = Integer.valueOf(request.getParameter("page"));// ?????????
        if (page > 0) {
            page = (page - 1) * 20;
        }
        String orderstatus = request.getParameter("orderstatus");// ???????????????
        // String orderdate=request.getParameter("orderdate");//????????????
        String orderSource = request.getParameter("orderSource");// ????????????
        String amount = request.getParameter("amount");// ??????????????????
        String noCycle = request.getParameter("noCycle");// ???????????????
        String orderid = request.getParameter("orderid");// ???????????????
        String procurementAccount = request.getParameter("procurementAccount");// ????????????
        String isCompany = request.getParameter("isCompany");//
        String myorderid = request.getParameter("myorderid");//
        String enddate = request.getParameter("enddate");
        String startdate = request.getParameter("startdate");
        String payenddate = request.getParameter("payenddate");
        String paystartdate = request.getParameter("paystartdate");
        int start = 0;
        int end = 0;
        int DeliveryDate = 0;
        startdate = StringUtils.isStrNull(startdate) || "0".equals(startdate) ? null : startdate;
        paystartdate = StringUtils.isStrNull(paystartdate) ? null : paystartdate;
        enddate = !StringUtils.isStrNull(enddate) ? enddate + " 23:59:59" : null;
        payenddate = !StringUtils.isStrNull(payenddate) ? payenddate + " 23:59:59" : null;
        orderid = StringUtils.isStrNull(orderid) ? null : orderid;
        myorderid = StringUtils.isStrNull(myorderid) ? null : myorderid;
        orderstatus = StringUtils.isStrNull(orderstatus) || "0".equals(orderstatus) ? null : orderstatus;
        orderSource = orderSource.equals("-1") ? null : orderSource;
        isCompany = "0".equals(isCompany) ? null : isCompany;
        procurementAccount = procurementAccount.equals("0") ? null : procurementAccount;
        if (noCycle.equals("0")) {
            noCycle = null;
        } else {
            DeliveryDate = Integer.valueOf(noCycle);
        }
        if (amount.equals("-1")) {
            amount = null;
        } else {
            start = Integer.valueOf(amount.split("~")[0]);
            end = Integer.valueOf(amount.split("~")[1]);
        }
        TaoBaoInfoList list = new TaoBaoInfoList();
        List<TaoBaoOrderInfo> infoList = taoBaoOrderService.getTaoBaoOrderList(orderstatus, orderSource, null,
                procurementAccount, start, end, DeliveryDate, orderid, isCompany, myorderid, startdate, enddate,
                paystartdate, payenddate, page);
        System.out.println("infoList================" + infoList.size());
        double pagePrice = 0.00;
        List<TaoBaoOrderInfo> count = taoBaoOrderService.getAllCount(orderstatus, orderSource, null, procurementAccount,
                start, end, DeliveryDate, orderid, isCompany, myorderid, startdate, enddate, paystartdate, payenddate);
        for (int i = 0; i < count.size(); i++) {
            String totalPrice = count.get(i).getTotalprice();
            totalPrice = totalPrice.indexOf(",") > -1 ? totalPrice.replaceAll(",", "") : totalPrice;
            pagePrice += Double.valueOf(totalPrice);
        }
        list.setPagePrice(Double.valueOf(df.format(pagePrice)));
        list.setAllCount(count.size() > 0 ? count.size() : 0);
        list.setInfoList(infoList);
        json.setData(list);
        return json;
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/getAllBuyer", method = RequestMethod.GET)
    @ResponseBody
    protected JSONArray getAllBuyer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        List<Tb1688Account> adm_list = iWarehouseService.getAllBuy();
        JSONArray jsonArr = JSONArray.parseArray(JSONArray.toJSONString(adm_list));
        return jsonArr;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/getAllAdm", method = RequestMethod.GET)
    @ResponseBody
    protected JSONArray getAllAdm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        List<com.cbt.pojo.Admuser> adm_list = taoBaoOrderService.getAllBuyer();
        JSONArray jsonArr = JSONArray.parseArray(JSONArray.toJSONString(adm_list));
        return jsonArr;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/getNewBarcode", method = RequestMethod.GET)
    @ResponseBody
    protected JSONArray getNewBarcode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        List<String> list = taoBaoOrderService.getNewBarcode();
        List<AliCategory> barcode_list = new ArrayList<AliCategory>();
        for (String string : list) {
            AliCategory a = new AliCategory();
            a.setId(string);
            a.setPath(string);
            barcode_list.add(a);
        }
        JSONArray jsonArr = JSONArray.parseArray(JSONArray.toJSONString(barcode_list));
        return jsonArr;
    }

    @RequestMapping(value = "/addTbOrderRemark")
    @ResponseBody
    protected JsonResult addTbOrderRemark(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list = new TaoBaoInfoList();
        String id = request.getParameter("id");
        String remark = request.getParameter("context");
        String old_remark = taoBaoOrderService.getTbOrderRemark(id);
        if (old_remark != null && !"".equals(old_remark)) {
            remark = old_remark + "<br>" + remark;
        }
        int row = taoBaoOrderService.addTbOrderRemark(id, remark);
        if (row > 0) {
            list.setAmount(remark);
        }
        json.setData(list);
        return json;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/getSourceValidation")
    @ResponseBody
    protected JsonResult getSourceValidation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list = new TaoBaoInfoList();
        Map<String, String> map = new HashMap<String, String>();
        // String admuserJson = Redis.hget(request.getSession().getId(),
        // "admuser");
        // Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
        // Admuser.class);
        String buyer = request.getParameter("buyer");
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        String isTrack = request.getParameter("isTrack");
        int page = Integer.valueOf(request.getParameter("page"));// ?????????
        String type = request.getParameter("type");
        type = StringUtil.isBlank(type) ? "1" : type;
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (startdate == null || "".equals(startdate)) {
            startdate = "1990-01-01 00:00:00";
        }
        if (enddate == null || "".equals(enddate)) {
            enddate = "2999-01-01 00:00:00";
        }
        StringBuffer accounts = new StringBuffer("(");
        // ??????????????????
        List<String> account = iWarehouseService.getBuyerName(buyer);
        for (int i = 0; i < account.size(); i++) {
            accounts.append("'" + account.get(i) + "',");
        }
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        map.put("page", String.valueOf(page));

        String acco = accounts.toString().substring(0, accounts.length() - 1) + ")";
        acco = "1".equals(buyer) ? null : acco;
        buyer = "1".equals(buyer) ? null : buyer;
        map.put("account", acco);
        map.put("buyer", buyer);
        if ("1".equals(type)) {
            // ????????????????????????????????????????????????  type=1
            List<OrderProductSource> ops_list = taoBaoOrderService.getSourceValidation(buyer, acco, page, startdate, enddate);
            // ????????????????????????????????????(??????????????????????????????1688????????????)
//			int count = taoBaoOrderService.getCount(buyer, startdate, enddate);
            // ?????????????????????????????????
//			int counts = taoBaoOrderService.getCounts(acco, startdate, enddate);
            //??????????????????
            int ops_list_count = taoBaoOrderService.getSourceValidationCount(buyer, acco, page, startdate, enddate);
//			for (OrderProductSource orderProductSource : ops_list) {
//				if (orderProductSource.getBuyerOrderid() == null || orderProductSource.getBuyerOrderid().equals("")) {
//					orderProductSource.setBuyerOrderid("???");
//				}
//				// orderProductSource.setCar_img(account);//?????????
//				orderProductSource.setAdminid(count);//????????????????????????
//				orderProductSource.setGoodsdataid(counts);//??????1688?????????????????????
//			}
            list.setOps_list(ops_list);//???????????????????????????????????????????????????????????????????????????1688???????????????
            list.setAllCount(ops_list_count);//??????????????????????????????
            list.setTb_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTbAllCount(0);
            list.setTaobao_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTaobao_list_size(0);
            list.setFpNoBuyInfoList(new ArrayList<OrderProductSource>());
            list.setFpNoBuyInfoListCount(0);
            list.setOneMathchMoreOrderInfo(new ArrayList<OrderProductSource>());
            list.setOneMathchMoreOrderInfoCount(0);
            list.setAllBuyerOrderInfo(new ArrayList<TaoBaoOrderInfo>());
            list.setAllBuyerOrderInfoCount(0);
        } else if ("2".equals(type)) {
            // ??????????????????????????????  type=2
            List<TaoBaoOrderInfo> tb_list = taoBaoOrderService.getSourceValidationForTb(buyer, acco, page, startdate,
                    enddate, isTrack);
            for (TaoBaoOrderInfo taoBaoOrderInfo : tb_list) {
                if (taoBaoOrderInfo.getOpsorderid() == null || "".equals(taoBaoOrderInfo.getOpsorderid())) {
                    taoBaoOrderInfo.setOpsorderid("???");
                }
            }
            //1688????????????????????????
            int tb_list_count = taoBaoOrderService.getSourceValidationForTbCount(buyer, acco, page, startdate, enddate,
                    isTrack);
            // 1688????????????
            String amount = taoBaoOrderService.getBuyerAmountByMouth(startdate, enddate, acco);
            list.setAmount(amount == null || "".equals(amount) ? "0" : amount);
            list.setTb_list(tb_list);
            list.setTbAllCount(tb_list_count);
            list.setOps_list(new ArrayList<OrderProductSource>());
            list.setAllCount(0);
            list.setTaobao_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTaobao_list_size(0);
            list.setFpNoBuyInfoList(new ArrayList<OrderProductSource>());
            list.setFpNoBuyInfoListCount(0);
            list.setOneMathchMoreOrderInfo(new ArrayList<OrderProductSource>());
            list.setOneMathchMoreOrderInfoCount(0);
            list.setAllBuyerOrderInfo(new ArrayList<TaoBaoOrderInfo>());
            list.setAllBuyerOrderInfoCount(0);
        } else if ("3".equals(type)) {
            //		 ??????????????????????????????????????????1688????????????  type=3
            //???????????????????????????????????????????????????
            List<TaoBaoOrderInfo> taobao_list = taoBaoOrderService.getTaoBaoNoInspection(map);
            List<TaoBaoOrderInfo> taobao_list_count = taoBaoOrderService.getTaoBaoNoInspectionCount(map);
            list.setTaobao_list(taobao_list);
            list.setTaobao_list_size(taobao_list_count.size());
            list.setTb_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTbAllCount(0);
            list.setOps_list(new ArrayList<OrderProductSource>());
            list.setAllCount(0);
            list.setFpNoBuyInfoList(new ArrayList<OrderProductSource>());
            list.setFpNoBuyInfoListCount(0);
            list.setOneMathchMoreOrderInfo(new ArrayList<OrderProductSource>());
            list.setOneMathchMoreOrderInfoCount(0);
            list.setAllBuyerOrderInfo(new ArrayList<TaoBaoOrderInfo>());
            list.setAllBuyerOrderInfoCount(0);
        } else if ("4".equals(type)) {
            //??????????????????????????????????????????  type=4
            List<OrderProductSource> fpNoBuyInfoList = taoBaoOrderService.getNopurchaseDistribution(map);
            List<OrderProductSource> fpNoBuyInfoListCount = taoBaoOrderService.getNopurchaseDistributionCount(map);
            list.setFpNoBuyInfoList(fpNoBuyInfoList);
            list.setFpNoBuyInfoListCount(fpNoBuyInfoListCount.size());
            list.setTaobao_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTaobao_list_size(0);
            list.setTb_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTbAllCount(0);
            list.setOps_list(new ArrayList<OrderProductSource>());
            list.setAllCount(0);
            list.setOneMathchMoreOrderInfo(new ArrayList<OrderProductSource>());
            list.setOneMathchMoreOrderInfoCount(0);
            list.setAllBuyerOrderInfo(new ArrayList<TaoBaoOrderInfo>());
            list.setAllBuyerOrderInfoCount(0);
        } else if ("5".equals(type)) {
            //????????????????????????????????????  type=5
            List<OrderProductSource> oneMathchMoreOrderInfo = taoBaoOrderService.getOneMathchMoreOrderInfo(map);
            List<OrderProductSource> oneMathchMoreOrderInfoCount = taoBaoOrderService.getOneMathchMoreOrderInfoCount(map);
            list.setOneMathchMoreOrderInfo(oneMathchMoreOrderInfo);
            list.setOneMathchMoreOrderInfoCount(oneMathchMoreOrderInfoCount.size());
            list.setFpNoBuyInfoList(new ArrayList<OrderProductSource>());
            list.setFpNoBuyInfoListCount(0);
            list.setTaobao_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTaobao_list_size(0);
            list.setTb_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTbAllCount(0);
            list.setOps_list(new ArrayList<OrderProductSource>());
            list.setAllCount(0);
            list.setAllBuyerOrderInfo(new ArrayList<TaoBaoOrderInfo>());
            list.setAllBuyerOrderInfoCount(0);
        } else if ("6".equals(type)) {
            //??????????????????
            List<TaoBaoOrderInfo> allBuyerOrderInfo = taoBaoOrderService.getAllBuyerOrderInfo(map);
            List<TaoBaoOrderInfo> allBuyerOrderInfoCount = taoBaoOrderService.getAllBuyerOrderInfoCount(map);
            list.setAllBuyerOrderInfo(allBuyerOrderInfo);
            list.setAllBuyerOrderInfoCount(allBuyerOrderInfoCount.size());
            list.setOneMathchMoreOrderInfo(new ArrayList<OrderProductSource>());
            list.setOneMathchMoreOrderInfoCount(0);
            list.setFpNoBuyInfoList(new ArrayList<OrderProductSource>());
            list.setFpNoBuyInfoListCount(0);
            list.setTaobao_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTaobao_list_size(0);
            list.setTb_list(new ArrayList<TaoBaoOrderInfo>());
            list.setTbAllCount(0);
            list.setOps_list(new ArrayList<OrderProductSource>());
            list.setAllCount(0);
        }


        json.setData(list);
        return json;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param barcode ????????????
     * @return
     */
    public String getBarcode(String barcode) {
        StringBuffer bar = new StringBuffer("sh");
        if (barcode.length() == 5) {
            for (int i = 0; i < barcode.length(); i++) {
                char item = barcode.charAt(i);
                if (i < 2) {
                    bar.append(item);
                } else {
                    bar.append("00").append(item);
                }
            }
        } else if (barcode.length() == 6) {
            for (int i = 0; i < barcode.length(); i++) {
                char item = barcode.charAt(i);
                if (i < 2) {
                    bar.append(item);
                } else if (i == 2 || i == 3) {
                    bar.append(item);
                } else {
                    bar.append("00").append(item);
                }
                if (i == 1) {
                    bar.append("0");
                }
            }
        }
        return bar.toString();
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-06-22
     */
    @RequestMapping(value = "/createBatch")
    @ResponseBody
    protected JsonResult createBatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        Calendar cal = Calendar.getInstance();
        String y = String.valueOf(cal.get(Calendar.YEAR));
        String m = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String d = String.valueOf(cal.get(Calendar.DATE));
        if (Integer.valueOf(m) < 10) {
            m = "0" + m;
        }
        if (Integer.valueOf(d) < 10) {
            d = "0" + d;
        }
        String time = y + m + d;
        String new_batch = "";
        String old_batch = taoBaoOrderService.queryOldBatch(time);
        if (old_batch != null && !"".equals(old_batch)) {
            new_batch = time + "_" + (Integer.valueOf(old_batch.split("_")[1]) + 1);
        } else {
            new_batch = time + "_1";
        }
        json.setBatch(new_batch);
        return json;
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-06-22
     */
    @RequestMapping(value = "/delCoupons")
    @ResponseBody
    protected JsonResult delCoupons(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list1 = new TaoBaoInfoList();
        int id = Integer.valueOf(StringUtils.isStrNull(request.getParameter("id")) ? "0" : request.getParameter("id"));
        int row = taoBaoOrderService.delCoupons(id);
        list1.setAllCount(row);
        json.setData(list1);
        return json;
    }

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-07-17
     */
    @RequestMapping(value = "/queryUsingRange")
    @ResponseBody
    protected JsonResult queryUsingRange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list1 = new TaoBaoInfoList();
        List<AliCategory> list = taoBaoOrderService.queryUsingRange();
        list1.setAliCategoryList(list);
        json.setData(list1);
        return json;
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-06-22
     */
    @RequestMapping(value = "/createCoupons")
    @ResponseBody
    protected JsonResult createCoupons(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list1 = new TaoBaoInfoList();
        Map<Object, Object> map = new HashMap<Object, Object>();
        // ??????????????????
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        String denomination = request.getParameter("denomination");// ????????????
        String discount = request.getParameter("discount");
        String total_circulation = request.getParameter("total_circulation");// ????????????
        double total_amount = 0.00;
        if (denomination == null || "".equals(denomination)) {
            denomination = String.valueOf(Double.valueOf(discount) / 100);
        } else {
            total_amount = Double.valueOf(total_circulation) * Double.valueOf(denomination);
        }
        String minimum_cons = request.getParameter("minimum_cons");// ????????????
        String for_most = request.getParameter("for_most");// ????????????
        String validity_day = request.getParameter("validity_day");// ?????????
        String validity_type = request.getParameter("validity_type");// ?????????
        String most_favorable = request.getParameter("most_favorable");
        String using_range = request.getParameter("using_range");
        if (most_favorable == null || "".equals(most_favorable)) {
            most_favorable = "-";
        }
        String startdata = "-";
        String enddata = "-";
        if ("2".equals(validity_type)) {
            startdata = validity_day.split("&")[0];
            enddata = validity_day.split("&")[1];
        } else if ("3".equals(validity_type)) {
            startdata = validity_day;
        }
        String batch = request.getParameter("batch");// ??????
        String coupons_name = request.getParameter("coupons_name");// ???????????????
        String coupons_type = request.getParameter("coupons_type");// ???????????????
        String disbursement = request.getParameter("disbursement");// ????????????
        String create_adm = adm.getAdmName();
        map.put("denomination", denomination);
        map.put("total_circulation", total_circulation);
        map.put("most_favorable", most_favorable);
        map.put("minimum_cons", minimum_cons);
        map.put("for_most", for_most);
        // map.put("validity_day", validity_day);
        map.put("validity_type", validity_type);
        map.put("startdata", startdata);
        map.put("enddata", enddata);
        map.put("batch", batch);
        map.put("coupons_name", coupons_name);
        map.put("coupons_type", coupons_type);
        map.put("disbursement", disbursement);
        map.put("create_adm", create_adm);
        map.put("total_amount", total_amount);
        map.put("using_range", using_range);
        map.put("id", 0);
        try {
            taoBaoOrderService.createCoupons(map);
            int coupons_id = taoBaoOrderService.getCouponsId(batch);
            if (coupons_id > 0) {
                list1.setTbAllCount(coupons_id);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < Integer.valueOf(total_circulation); i++) {
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("batch", batch);
                    map1.put("promo_code", this.createPromoCode());// ?????????
                    map1.put("disbursement", disbursement);
                    map1.put("validity_day", validity_day);
                    map1.put("adm_name", create_adm);
                    map1.put("coupons_id", coupons_id);
                    map1.put("validity_type", validity_type);
                    map1.put("startdata", startdata);
                    map1.put("enddata", enddata);
                    list.add(map1);
                }
                taoBaoOrderService.createCouponSubsidiary(list);
            } else {
                list1.setTbAllCount(0);
            }
        } catch (Exception e) {
            list1.setTbAllCount(0);
        }
        json.setBatch(batch.split("_")[0] + (Integer.valueOf(batch.split("_")[1]) + 1));
        json.setData(list1);
        return json;
    }

    public String createPromoCode() {
        String a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer code = new StringBuffer();
        char[] rands = new char[10];
        for (int i = 0; i < rands.length; i++) {
            int rand = (int) (Math.random() * a.length());
            rands[i] = a.charAt(rand);
        }
        for (int i = 0; i < rands.length; i++) {
            code.append(rands[i]);
        }
        return code.toString();
    }

    /**
     * ????????????/????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/getSaleBuyInfo")
    @ResponseBody
    protected EasyUiJsonResult getSaleBuyInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<String, String> map = new HashMap<String, String>();
        String orderid = request.getParameter("orderid");
        String tborderid = request.getParameter("tborderid");
        String odid = request.getParameter("odid");
        orderid = StringUtil.isBlank(orderid) ? null : orderid;
        odid = StringUtil.isBlank(odid) ? null : odid;
        tborderid = StringUtil.isNotBlank(tborderid) ? tborderid : null;
        int page = Integer.valueOf(request.getParameter("page"));
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (StringUtil.isBlank(orderid) && StringUtil.isBlank(odid) && StringUtil.isBlank(tborderid)) {
            json.setTotal(0);
            json.setRows(new ArrayList<StraightHairPojo>());
            return json;
        }
        map.put("orderid", orderid);
        map.put("odid", odid);
        map.put("page", String.valueOf(page));
        map.put("tborderid", tborderid);
        List<StraightHairPojo> list = taoBaoOrderService.getSaleBuyInfo(map);
        List<StraightHairPojo> listCount = taoBaoOrderService.getSaleBuyInfoCount(map);
        json.setTotal(listCount.size());
        json.setRows(list);
        return json;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param request
     * @param response
     * @return EasyUiJsonResult
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title StraightHairList
     * @Description TODO
     */
    @RequestMapping(value = "/StraightHairList")
    @ResponseBody
    protected EasyUiJsonResult StraightHairList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        if (page > 0) {
            page = (page - 1) * 20;
        }
        String admuserid = request.getParameter("admuserid");
        String goods_pid = request.getParameter("goods_pid");
        goods_pid = StringUtils.isStrNull(goods_pid) ? null : goods_pid;
        List<StraightHairPojo> list = new ArrayList<StraightHairPojo>();
        List<StraightHairPojo> list_count = new ArrayList<StraightHairPojo>();
        map.put("page", page);
        map.put("admuserid", admuserid);
        map.put("goods_pid", goods_pid);
        list = taoBaoOrderService.StraightHairList(map);
        list_count = taoBaoOrderService.StraightHairListCount(map);
        json.setRows(list);
        json.setTotal(list_count.size());
        return json;
    }

    @RequestMapping(value = "/reviewManagerment")
    @ResponseBody
    protected EasyUiJsonResult reviewManagerment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<String, String> map = new HashMap<String, String>();
        int page = Integer.valueOf(request.getParameter("page"));
        if (page > 0) {
            page = (page - 1) * 20;
        }
        String orderid = request.getParameter("orderno");
        String goodsPid = request.getParameter("goods_pid");
        String userid = request.getParameter("userid");
        goodsPid = StringUtils.isStrNull(goodsPid) ? null : goodsPid;
        map.put("page", String.valueOf(page));
        map.put("orderid", StringUtil.isNotBlank(orderid) ? orderid : null);
        map.put("goodsPid", StringUtil.isNotBlank(goodsPid) ? goodsPid : null);
        map.put("userid", StringUtil.isNotBlank(userid) ? userid : null);
        List<ReviewManage> list = taoBaoOrderService.reviewManagerment(map);
        List<ReviewManage> list_count = taoBaoOrderService.reviewManagermentCount(map);
        json.setRows(list);
        json.setTotal(list_count.size());
        return json;
    }

    /**
     * ?????????????????? ??????
     *
     * @param request
     * @param response
     * @return EasyUiJsonResult
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title getPayFailureOrder
     * @Description TODO
     */
    @RequestMapping(value = "/getPayFailureOrder")
    @ResponseBody
    protected EasyUiJsonResult getPayFailureOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        int page = Integer.valueOf(request.getParameter("page"));
        if (page > 0) {
            page = (page - 1) * 20;
        }
        startdate = StringUtils.isStrNull(startdate) ? null : (startdate + " 00:00:00");
        enddate = StringUtils.isStrNull(enddate) ? null : (enddate + " 23:59:59");
        map.put("page", page);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        List<PayFailureOrderPojo> list = new ArrayList<PayFailureOrderPojo>();
        List<PayFailureOrderPojo> list_count = new ArrayList<PayFailureOrderPojo>();
        list = taoBaoOrderService.getPayFailureOrder(map);
        list_count = taoBaoOrderService.getPayFailureOrderCount(map);
        json.setRows(list);
        json.setTotal(list_count.size());
        return json;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @return EasyUiJsonResult
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title searchLossInventoryInfo
     * @Description TODO
     */
    @RequestMapping(value = "/searchLossInventoryInfo")
    @ResponseBody
    protected EasyUiJsonResult searchLossInventoryInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String times = request.getParameter("times");
        if (page > 0) {
            page = (page - 1) * 20;
        }
        times = StringUtil.isNotBlank(times) ? times : null;
        String admuserid = request.getParameter("admuserid");
        List<LossInventoryPojo> list = new ArrayList<LossInventoryPojo>();
        List<LossInventoryPojo> list_count = new ArrayList<LossInventoryPojo>();
        map.put("page", page);
        map.put("admuserid", admuserid);
        map.put("times", times);
        list = taoBaoOrderService.searchLossInventory(map);
        list_count = taoBaoOrderService.searchLossInventoryCount(map);
        json.setRows(list);
        json.setTotal(list_count.size());
        return json;
    }


    /**
     * Importexpress-GA????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-28
     */
    @RequestMapping(value = "/getOrderQuery")
    @ResponseBody
    protected EasyUiJsonResult getOrderQuery(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ????????????
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String startdate = StringUtils.isStrNull(request.getParameter("startdate")) ? dateFormat.format(System.currentTimeMillis()) : request.getParameter("startdate") + " 00:00:00";
        String enddate = StringUtils.isStrNull(request.getParameter("enddate")) ? dateFormat.format(System.currentTimeMillis()) : request.getParameter("enddate") + " 00:00:00";
        Date date = dateFormat.parse(enddate); // ????????????
        Date newDate = addDate(date, 1); // ??????????????????20???
        enddate = dateFormat.format(newDate);
        if (page > 0) {
            page = (page - 1) * 20;
        }
        map.put("startTime", startdate);
        map.put("endTime", enddate);
        request.setAttribute("startdate", startdate);
        request.setAttribute("enddate", enddate);
        map.put("page", page);
        List<DataQueryBean> list = taoBaoOrderService.getOrderQuery(map);
        for (DataQueryBean dataQueryBean : list) {
            dataQueryBean.setNew_user_goods_car(String.valueOf(Integer.valueOf(dataQueryBean.getNew_user_goods_car()) / Integer.valueOf("0".equals(dataQueryBean.getNew_user_count()) ? "1" : dataQueryBean.getNew_user_count())));

        }
        json.setRows(list);
        json.setTotal(1);
        return json;
    }

    public static Date addDate(Date date, long day) throws ParseException {
        long time = date.getTime(); // ??????????????????????????????
        day = day * 24 * 60 * 60 * 1000; // ????????????????????????????????????
        time += day; // ???????????????????????????
        return new Date(time); // ???????????????????????????
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-28
     */
    @RequestMapping(value = "/getInventoryAmount")
    @ResponseBody
    protected EasyUiJsonResult getInventoryAmount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String times = request.getParameter("times");
        String startTime = "";
        String endTime = "";
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", page);
        List<InventoryDetailsPojo> list = taoBaoOrderService.getInventoryAmount(map);
        for (InventoryDetailsPojo i : list) {
            i.setCar_img("<img src='" + i.getCar_img() + "' height='100' width='100'>");
        }
        int count = taoBaoOrderService.getInventoryAmountCount(map).size();
        json.setRows(list);
        json.setTotal(count);
        return json;
    }


    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-28
     */
    @RequestMapping(value = "/getSaleInventory")
    @ResponseBody
    protected EasyUiJsonResult getSaleInventory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String times = request.getParameter("times");
        String startTime = "";
        String endTime = "";
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if ("2000".equals(times)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -30);
            Date monday = c.getTime();
            startTime = sdf.format(monday) + " 00:00:00";
            endTime = "2020-01-01 23:59:59";
        } else if (times.indexOf("-") > -1) {
            startTime = times + "-01 00:00:00";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", page);
        List<InventoryDetailsPojo> list = taoBaoOrderService.getSaleInventory(map);
        for (InventoryDetailsPojo i : list) {
            i.setCar_img("<img src='" + i.getCar_img() + "' height='100' width='100'>");
            i.setIs_use("1".equals(i.getIs_use()) ? "??????????????????" : "??????????????????");
            i.setIs_delete("1".equals(i.getIs_delete()) ? "??????" : "??????");
            i.setFlag("1".equals(i.getFlag()) ? "?????????" : "??????");
        }
        int count = taoBaoOrderService.getSaleInventoryCount(map).size();
        json.setRows(list);
        json.setTotal(count);
        return json;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/setDisplay")
    public void setDisplay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        int row = 0;
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", request.getParameter("id"));
        map.put("showFlag", request.getParameter("showFlag"));
        row = taoBaoOrderService.setDisplay(map);

        SendMQ.sendMsg(new RunSqlModel("update goods_comments_real set show_flag='" + map.get("showFlag") + "' where id='" + map.get("id") + "'"));

        out.print(row);
        out.close();
    }

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/getNoStorageDetails")
    @ResponseBody
    protected EasyUiJsonResult getNoStorageDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        String orderNo = request.getParameter("orderNo");
        Map<String, String> map = new HashMap<String, String>();
        orderNo = StringUtil.isBlank(orderNo) ? null : orderNo;
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if (adm == null) {
            return json;
        }
        map.put("orderNo", orderNo);
        List<TaoBaoOrderInfo> list = null;
        int count = 0;
        String username = null;
        if (!"1".equals(String.valueOf(adm.getId()))) {
            username = iWarehouseService.getBuyerNames(String.valueOf(adm.getId()));
        }
        map.put("username", username);
        list = taoBaoOrderService.getNoStorageDetails(map);
        count = taoBaoOrderService.getNoStorageDetailsCount(map);
        json.setTotal(count);
        json.setRows(list);
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/getTbOrderDetails")
    @ResponseBody
    protected EasyUiJsonResult getTbOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        List<TaoBaoOrderInfo> list = new ArrayList<TaoBaoOrderInfo>();
        List<TaoBaoOrderInfo> listCount = new ArrayList<TaoBaoOrderInfo>();
        Map<String, String> map = new HashMap<String, String>();
        int page = Integer.valueOf(request.getParameter("page"));
        if (page > 0) {
            page = (page - 1) * 20;
        }
        String orderid = request.getParameter("orderid");
        map.put("orderid", orderid);
        map.put("page", String.valueOf(page));
        list = taoBaoOrderService.getTbOrderDetails(map);
        listCount = taoBaoOrderService.getTbOrderDetailsCount(map);
        json.setRows(list);
        json.setTotal(listCount.size());
        return json;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-28
     */
    @RequestMapping(value = "/BuyOrderDetails")
    @ResponseBody
    protected EasyUiJsonResult BuyOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String times = request.getParameter("times");
        String userName = request.getParameter("userName");
        String type = request.getParameter("type");
        String startTime = "";
        String endTime = "";
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(userName) || "?????????".equals(userName)) {
            userName = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", page);
        map.put("userName", userName);
        List<TaoBaoOrderInfo> list = null;
        int count = 0;
        if ("1".equals(type)) {
            list = taoBaoOrderService.getNoStorage(map);
            count = taoBaoOrderService.getNoStorageCount(map).size();
        } else if ("2".equals(type)) {
            list = taoBaoOrderService.getCancelAmount(map);
            count = taoBaoOrderService.getCancelAmountCount(map).size();
        } else if ("3".equals(type)) {
            list = taoBaoOrderService.getNoMatchingOrder(map);
            count = taoBaoOrderService.getNoMatchingOrderCount(map).size();
        } else if ("4".equals(type)) {
            list = taoBaoOrderService.getLastAmount(map);
            count = taoBaoOrderService.getLastAmountCount(map).size();
        } else if ("5".equals(type)) {
            list = taoBaoOrderService.getGrabNormalAmount(map);
            count = taoBaoOrderService.getGrabNormalAmountCount(map).size();
        }
        for (TaoBaoOrderInfo c : list) {
            if ("0".equals(c.getTbOr1688())) {
                c.setTbOr1688("??????");
            } else if ("1".equals(c.getTbOr1688())) {
                c.setTbOr1688("1688");
            } else if ("3".equals(c.getTbOr1688())) {
                c.setTbOr1688("??????");
            } else {
                c.setTbOr1688("??????");
            }
            if ("1".equals(type) || "3".equals(type)) {
                String o_remark = c.getOperation_remark() == null || "".equals(c.getOperation_remark()) ? "" : c.getOperation_remark();
                c.setOperation_remark("<div style='overflow-y:scroll;height:150px;'><font id='" + c.getId() + "'>" + o_remark + "</font></div><div><button class='repalyBtn' onclick=\"doReplay(" + c.getId() + ")\">??????</button>&nbsp;&nbsp;<button class='repalyBtn' style='left:3px;' onclick=\"openStorage('" + c.getOrderid() + "','" + c.getShipno() + "','" + c.getSku() + "','" + c.getItemqty() + "','" + c.getItemurl() + "','" + c.getItemid() + "')\">??????</button></div>");
            }
            c.setItemname("<a target='_blank' href='" + c.getItemurl() + "'>" + c.getItemname().substring(0, c.getItemname().length() / 3) + "</a>");
            c.setImgurl("<img src='" + c.getImgurl() + "' height='100' width='100'>");
        }
        json.setRows(list);
        json.setTotal(count);
        return json;
    }


    @RequestMapping(value = "/getRefundOrderDetails")
    @ResponseBody
    protected EasyUiJsonResult getRefundOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.parseInt(request.getParameter("page"));
        String times = request.getParameter("times");
        String userName = request.getParameter("userName");
        String startTime = "";
        String endTime = "";
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (times.indexOf("-") > -1) {
            startTime = times + "-01 00:00:00";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(userName) || "?????????".equals(userName)) {
            userName = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", page);
        map.put("userName", userName);
        map.put("limitNum", 20);
        List<TaoBaoOrderInfo> list = null;
        int count = 0;
        try {
            list = taoBaoOrderService.getRefundAmount(map);
            count = taoBaoOrderService.getRefundAmountCount(map).size();
            for (TaoBaoOrderInfo c : list) {
                if ("0".equals(c.getTbOr1688())) {
                    c.setTbOr1688("??????");
                } else if ("1".equals(c.getTbOr1688())) {
                    c.setTbOr1688("1688");
                } else if ("3".equals(c.getTbOr1688())) {
                    c.setTbOr1688("??????");
                } else {
                    c.setTbOr1688("??????");
                }
                c.setItemname("<a target='_blank' href='" + c.getItemurl() + "'>" + c.getItemname().substring(0, c.getItemname().length() / 3) + "</a>");
                c.setImgurl("<img src='" + c.getImgurl() + "' height='100' width='100'>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        json.setRows(list);
        json.setTotal(count);
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-28
     */
    @RequestMapping(value = "/getforecastAmount")
    @ResponseBody
    protected EasyUiJsonResult getforecastAmount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String times = request.getParameter("times");
        String orderid = request.getParameter("orderid");
        // String type=request.getParameter("type");
        String startTime = "";
        String endTime = "";
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (orderid == null || "".equals(orderid)) {
            orderid = null;
        }
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", page);
        map.put("orderid", orderid);
        List<ShippingPackage> list = null;
        int count = 0;
        // if("1".equals(type)){
        // ????????????
        list = taoBaoOrderService.getforecastAmount(map);
        count = taoBaoOrderService.getforecastAmountCount(map).size();
        // }
        json.setRows(list);
        json.setTotal(count);
        return json;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-28
     */
    @RequestMapping(value = "/getPayFreight")
    @ResponseBody
    protected EasyUiJsonResult getPayFreight(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String times = request.getParameter("times");
        String orderNo = request.getParameter("orderNo");// ????????????
        String startTime = "";
        String endTime = "";
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (orderNo == null || "".equals(orderNo)) {
            orderNo = null;
        }
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", page);
        map.put("orderid", orderNo);
        List<Shipments> list = null;
        int count = 0;
        // ????????????
        list = taoBaoOrderService.getPayFreight(map);
        count = taoBaoOrderService.getPayFreightCount(map).size();
        json.setRows(list);
        json.setTotal(count);
        return json;
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-22
     */
    @RequestMapping(value = "/searchCancelGoodsInventory")
    @ResponseBody
    protected EasyUiJsonResult searchCancelGoodsInventory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String state = request.getParameter("state") == null ? "2" : request.getParameter("state");// ????????????
        if (page > 0) {
            page = (page - 1) * 20;
        }
        map.put("page", page);
        map.put("state", state);
        List<CancelGoodsInventory> list = taoBaoOrderService.searchCancelGoodsInventory(map);
        for (CancelGoodsInventory c : list) {
            if (c.getFlag() == 1) {
                c.setOperation("<button disabled='disabled' id='" + c.getId()
                        + "' style='color:darkgray;' onclick=\"reductionInventory(" + c.getId() + "," + c.getIn_id() + ","
                        + c.getI_flag() + "," + c.getInventory_qty() + ",'" + c.getInventory_barcode() + "','" + c.getGoods_p_price() + "')\">????????????</button>");
            } else {
                c.setOperation("<button id='" + c.getId() + "' onclick=\"reductionInventory(" + c.getId() + ","
                        + c.getIn_id() + "," + c.getI_flag() + "," + c.getInventory_qty() + ",'" + c.getInventory_barcode() + "','" + c.getGoods_p_price() + "')\">????????????</button>");
            }
            c.setOrder_barcode("<span style='color:red;'>" + c.getOrder_barcode() + "</span>");
            c.setInventory_barcode("<span style='color:red;'>" + c.getInventory_barcode() + "</span>");
            c.setCar_img("<img src='" + c.getCar_img() + "' height='100' width='100'>");
        }
        int count = taoBaoOrderService.searchCancelGoodsInventoryCount(map);
        json.setRows(list);
        json.setTotal(count);
        return json;
    }


    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @author whj 2017-08-22
     */
    @RequestMapping(value = "/getOfflinePaymentApplication")
    @ResponseBody
    protected EasyUiJsonResult getOfflinePaymentApplication(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String userName = request.getParameter("userName");
        String state = request.getParameter("state") == null ? "2" : request.getParameter("state");// ????????????
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        if (StringUtils.isStrNull(startTime)) {
            startTime = null;
        }
        if (StringUtils.isStrNull(endTime)) {
            endTime = null;
        }
        page = page > 0 ? (page - 1) * 20 : 0;
        userName = "??????".equals(userName) ? null : userName;
        map.put("page", page);
        map.put("state", state);
        map.put("userName", userName);
        map.put("endTime", endTime);
        map.put("startTime", startTime);
        List<OfflinePaymentApplicationPojo> list = taoBaoOrderService.getOfflinePaymentApplication(map);
        List<OfflinePaymentApplicationPojo> count = taoBaoOrderService.getOfflinePaymentApplicationCount(map);
        json.setRows(list);
        json.setTotal(count.size());
        return json;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @return void
     * @throws ServletException
     * @throws IOException
     * @Title straightShipnoEntry
     * @Description TODO
     */
    @RequestMapping(value = "/straightShipnoEntry")
    public void straightShipnoEntry(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int row = 0;
        try {
            String orderid = request.getParameter("orderid");
            String goodsid = request.getParameter("goodsid");
            String remark = request.getParameter("remark");
            String shipno = request.getParameter("shipno");
            map.put("orderid", orderid);
            map.put("goodsid", goodsid);
            map.put("remark", remark);
            map.put("shipno", shipno);
            row = taoBaoOrderService.straightShipnoEntry(map);
        } catch (Exception e) {
            e.printStackTrace();
            row = 0;
        }
        out.print(row);
        out.close();
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-23
     */
    @RequestMapping(value = "/orderDetetailsSaleDetails")
    @ResponseBody
    protected EasyUiJsonResult orderDetetailsSaleDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String date_time = request.getParameter("date_time");
        String orderid = request.getParameter("orderid");
        if (orderid == null || "".equals(orderid)) {
            orderid = null;
        }
        if (page > 0) {
            page = (page - 1) * 30;
        }
        String startTime = date_time + " 00:00:00";
        String endTime = date_time + " 23:59:59";

        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", 0);
        map.put("page", page);
        map.put("orderid", orderid);
        List<OrderDetetailsSalePojo> list = taoBaoOrderService.orderDetetailsSaleDetails(map);
        List<OrderDetetailsSalePojo> count = taoBaoOrderService.orderDetetailsSaleDetailsCount(map);
        json.setRows(list);
        json.setTotal(count.size() + 1);
        return json;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-23
     */
    @RequestMapping(value = "/orderDetetailsSale")
    @ResponseBody
    protected EasyUiJsonResult orderDetetailsSale(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String times = request.getParameter("dd");
        String endDate = null;
        String startTime = null;
        int page = Integer.valueOf(request.getParameter("page"));
        if (page > 0) {
            page = (page - 1) * 30;
        }
        if (times == null || "".equals(times)) {
            startTime = "2017-04-01 00:00:00";
        } else {
            startTime = times + " 00:00:00";
            endDate = times + " 23:59:59";
        }
        map.put("startTime", startTime);
        map.put("endTime", endDate);
        map.put("page", page);
        List<OrderDetetailsSalePojo> list = taoBaoOrderService.orderDetetailsSale(map);
        List<OrderDetetailsSalePojo> count = taoBaoOrderService.orderDetetailsSaleCount(map);
        json.setRows(list);
        json.setTotal(count.size());
        return json;
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-23
     */
    @RequestMapping(value = "/orderSalesAmount")
    @ResponseBody
    protected EasyUiJsonResult orderSalesAmount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        DecimalFormat df = new DecimalFormat("#0.###");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String startTime = "";
        String endTime = "";
        if (!"0".equals(year) && !"0".equals(month)) {
            startTime = year + "-" + month + "-01 00:00:00";
            endTime = year + "-" + month + "-31 23:59:59";
        } else {
            startTime = "2017-04-01 00:00:00";
            endTime = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        List<OrderSalesAmountPojo> list = taoBaoOrderService.orderSalesAmount(map);
        json.setRows(list);
        json.setTotal(list.size());
        return json;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param request
     * @param model
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/getExchange", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> getExchange(HttpServletRequest request, Model model) throws ParseException {
        Map<String, String> p_map = new HashMap<String, String>();
        Map<String, String> r_map = new HashMap<String, String>();
        try {
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            if (StringUtil.isBlank(month) || StringUtil.isBlank(year)) {
                return r_map;
            }
            p_map.put("time", year + "-" + month);
            Map<String, String> resultMap = iWarehouseService.getExchange(p_map);
            r_map.put("eur_rate", String.valueOf(resultMap.get("eur_rate")));
            r_map.put("cad_rate", String.valueOf(resultMap.get("cad_rate")));
            r_map.put("gbp_rate", String.valueOf(resultMap.get("gbp_rate")));
            r_map.put("aud_rate", String.valueOf(resultMap.get("aud_rate")));
            r_map.put("rmb_rate", String.valueOf(resultMap.get("rmb_rate")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r_map;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/profitSummaryData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> profitSummaryData(HttpServletRequest request, Model model) throws ParseException {
        Map<String, String> map = new HashMap<String, String>();
        TaoBaoInfoList list = new TaoBaoInfoList();
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String userId = request.getParameter("userId");
        userId = StringUtil.isBlank(userId) ? null : userId.trim();
        try {
            String startTime = "";
            String endTime = "";
            if ("0".equals(month)) {
                startTime = year + "-01-01 00:00:00";
                endTime = year + "-12-31 23:59:59";
            } else if (!"0".equals(year) && !"0".equals(month)) {
                List<Map<String, String>> monthLst = Util.genQueryDate(year, month);
                startTime = monthLst.get(0).get("beginDate");
                endTime = monthLst.get(monthLst.size() - 1).get("endDate");
            } else {
                return map;
            }
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("userId", userId);
            List<OrderSalesAmountPojo> oList = taoBaoOrderService.getProfitSummaryData(map);
            if (oList.size() > 0) {
                map.put("estimateProfit", oList.get(0).getEstimateProfit());
                map.put("forecastProfits", oList.get(0).getForecastProfits());
                map.put("endProfit", oList.get(0).getEndProfits());
            } else {
                map.put("estimateProfit", "0.00");
                map.put("forecastProfits", "0.00");
                map.put("endProfit", "0.00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * ?????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/getUserProfitByMonth")
    @ResponseBody
    protected EasyUiJsonResult getUserProfitByMonth(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<String, String> map = new HashMap<String, String>();
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        int page = Integer.valueOf(request.getParameter("page"));
        String userId = request.getParameter("userId");
        userId = StringUtil.isBlank(userId) ? null : userId.trim();
        try {
            if (page > 0) {
                page = (page - 1) * 100;
            }
            String startTime = "";
            String endTime = "";
            if ("0".equals(month)) {
                startTime = year + "-01-01 00:00:00";
                endTime = year + "-12-31 23:59:59";
            } else if (!"0".equals(year) && !"0".equals(month)) {
                List<Map<String, String>> monthLst = Util.genQueryDate(year, month);
                startTime = monthLst.get(0).get("beginDate");
                endTime = monthLst.get(monthLst.size() - 1).get("endDate");
            } else {
                return json;
            }
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("page", String.valueOf(page));
            map.put("userId", userId);
            map.put("type", "0");
            map.put("times", (year + "-" + month));
            List<OrderSalesAmountPojo> list = taoBaoOrderService.getUserProfitByMonth(map);
            List<OrderSalesAmountPojo> counts = taoBaoOrderService.getUserProfitByMonthCount(map);
//			List<OrderSalesAmountPojo> list = taoBaoOrderService.getUserProfitByMonthData(map);
//			List<OrderSalesAmountPojo> counts = taoBaoOrderService.getUserProfitByMonthCountData(map);
            json.setRows(list);
            json.setTotal(counts.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-16
     */
    @RequestMapping(value = "/buyReconciliationReport")
    @ResponseBody
    protected EasyUiJsonResult buyReconciliationReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String startTime = "";
        String endTime = "";
        if (!"0".equals(year) && !"0".equals(month)) {
            startTime = year + "-" + month + "-01 00:00:00";
            endTime = year + "-" + month + "-31 23:59:59";
        } else if (!"0".equals(year)) {
            startTime = year + "-01-01 00:00:00";
            endTime = year + "-12-31 23:59:59";
        } else {
            startTime = "2017-01-01 00:00:00";
            endTime = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("year", !"0".equals(year) ? year : null);
        map.put("month", !"0".equals(month) ? month : null);
        List<BuyReconciliationPojo> list = taoBaoOrderService.buyReconciliationReport(map, "0");
        json.setRows(list);
        json.setTotal(list.size());
        return json;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-16
     */
    @RequestMapping(value = "/exportForecastAmount")
    @ResponseBody
    protected void exportForecastAmount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        request.setCharacterEncoding("utf-8");
        Map<Object, Object> map = new HashMap<Object, Object>();
        String times = request.getParameter("times");
        String orderid = request.getParameter("orderid");
        String type = request.getParameter("type");
        String startTime = "";
        String endTime = "";
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        if (orderid == null || "".equals(orderid)) {
            orderid = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("orderid", orderid);
        HSSFWorkbook wb = null;
        List<ShippingPackage> list = null;
        List<Shipments> list2 = null;
        response.setContentType("application/vnd.ms-excel");
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        String filename = "";
        if ("1".equals(type)) {
            list = taoBaoOrderService.getforecastAmountCount(map);
            filename = "????????????????????????" + year;
        } else if ("2".equals(type)) {
            list2 = taoBaoOrderService.getPayFreightCount(map);
            filename = "??????????????????????????????" + year;
        }
        wb = generalReportService.exportForecastAmount(list, list2, type);
        filename = StringUtils.getFileName(filename);
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }


    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-16
     */
    @RequestMapping(value = "/exportInventoryAmount")
    @ResponseBody
    protected void exportInventoryAmount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        request.setCharacterEncoding("utf-8");
        Map<Object, Object> map = new HashMap<Object, Object>();
        String times = request.getParameter("times");
        String type = request.getParameter("type");
        String startTime = "";
        String endTime = "";
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        HSSFWorkbook wb = null;
        List<InventoryDetailsPojo> list = taoBaoOrderService.getInventoryAmountCount(map);
        response.setContentType("application/vnd.ms-excel");
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        String filename = "??????????????????????????????" + year;
        wb = generalReportService.exportInventoryAmount(list);
        filename = StringUtils.getFileName(filename);
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * @param request
     * @param response
     * @return void
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title exportOfflinePayment
     * @Description ????????????????????????????????????
     */
    @RequestMapping(value = "/exportOfflinePayment")
    @ResponseBody
    protected void exportOfflinePayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        HSSFWorkbook wb = null;
        request.setCharacterEncoding("utf-8");
        Map<Object, Object> map = new HashMap<Object, Object>();
//		String userName=new String(request.getParameter("userName").getBytes("ISO8859-1"),"utf-8");
        String userName = request.getParameter("userName");
        if ("whj".equals(userName)) {
            userName = "?????????";
        }
        String state = request.getParameter("state") == null ? "2" : request.getParameter("state");// ????????????
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        if (StringUtils.isStrNull(startTime)) {
            startTime = null;
        }
        if (StringUtils.isStrNull(endTime)) {
            endTime = null;
        }
        userName = "?????????".equals(userName) ? null : userName;
        map.put("state", state);
        map.put("userName", userName);
        map.put("endTime", endTime);
        map.put("startTime", startTime);
        List<OfflinePaymentApplicationPojo> list = taoBaoOrderService.getOfflinePaymentApplicationCount(map);
        response.setContentType("application/vnd.ms-excel");
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        String filename = "????????????????????????????????????" + year;
        wb = generalReportService.exportOfflinePayment(list);
        filename = StringUtils.getFileName(filename);
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-16
     */
    @RequestMapping(value = "/exportSaleInventory")
    @ResponseBody
    protected void exportSaleInventory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        request.setCharacterEncoding("utf-8");
        Map<Object, Object> map = new HashMap<Object, Object>();
        String times = request.getParameter("times");
        String startTime = "";
        String endTime = "";
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        HSSFWorkbook wb = null;
        List<InventoryDetailsPojo> list = taoBaoOrderService.getSaleInventoryCount(map);
        for (InventoryDetailsPojo i : list) {
            i.setCar_img("<img src='" + i.getCar_img() + "' height='100' width='100'>");
            i.setIs_use("1".equals(i.getIs_use()) ? "??????????????????" : "??????????????????");
            i.setIs_delete("1".equals(i.getIs_delete()) ? "??????" : "??????");
            i.setFlag("1".equals(i.getFlag()) ? "??????" : "??????");
        }
        response.setContentType("application/vnd.ms-excel");
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        String filename = "??????????????????????????????" + year;
        wb = generalReportService.exportSaleInventory(list);
        filename = StringUtils.getFileName(filename);
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-16
     */
    @RequestMapping(value = "/exportBuyOrderDetails")
    @ResponseBody
    protected void exportBuyOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        request.setCharacterEncoding("utf-8");
        Map<Object, Object> map = new HashMap<Object, Object>();
        String times = request.getParameter("times");
        // String userName = new String(request.getParameter("userName").toString().getBytes("ISO8859-1"), "utf-8");
        String userName = request.getParameter("userName");
        String type = request.getParameter("type");
        String startTime = "";
        String endTime = "";
        if (times.indexOf("-") > -1) {
//			startTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-01 00:00:00";
            startTime = times + "-01 00:00:00";
//			endTime = times.split("-")[0] + "-"
//					+ (Integer.valueOf(times.split("-")[1]) > 9 ? times.split("-")[1] : "0" + times.split("-")[1])
//					+ "-31 23:59:59";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        System.err.println("-----------userName:" + userName);
        if (org.apache.commons.lang3.StringUtils.isBlank(userName) || "?????????".equals(userName)) {
            userName = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("userName", userName);
        HSSFWorkbook wb = null;
        List<TaoBaoOrderInfo> list = null;
        response.setContentType("application/vnd.ms-excel");
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        String filename = "";
        System.err.println("------------type :" + type);
        System.err.println("------------map :" + map.toString());
        if ("1".equals(type)) {
            //????????????????????????????????????
            list = taoBaoOrderService.getNoStorageCount(map);
            // filename = "??????????????????????????????????????????" + times;
            filename = "purchaseOrderWithMatchingSourceWithoutWarehousing" + times;
        } else if ("2".equals(type)) {
            //??????????????????????????????????????????
            list = taoBaoOrderService.getCancelAmountCount(map);
            // filename = "??????????????????????????????????????????" + times;
            filename = "purchaseOrdersCorrespondToCancellationOfSalesOrders" + times;
        } else if ("3".equals(type)) {
            // ?????????????????????????????????????????????
            list = taoBaoOrderService.getNoMatchingOrderCount(map);
            // filename = "?????????????????????????????????" + times;
            filename = "NoOrderMatchesPurchaseOrder" + times;
        } else if ("4".equals(type)) {
            // ????????????????????????????????????????????????
            list = taoBaoOrderService.getLastAmountCount(map);
            // filename = "??????????????????????????????????????????" + times;
            filename = "purchaseOrdersCorrespondToSalesOrdersOfLastMonth" + times;
        } else if ("5".equals(type)) {
            // ????????????????????????????????????????????????
            list = taoBaoOrderService.getGrabNormalAmountCount(map);
            // filename = "????????????????????????????????????" + times;
            filename = "captureTheNormalPurchaseOrderDetails" + times;
        }
        for (TaoBaoOrderInfo c : list) {
            if ("0".equals(c.getTbOr1688())) {
                c.setTbOr1688("??????");
            } else if ("1".equals(c.getTbOr1688())) {
                c.setTbOr1688("1688");
            } else if ("3".equals(c.getTbOr1688())) {
                c.setTbOr1688("??????");
            } else {
                c.setTbOr1688("??????");
            }
        }

        map.clear();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("year", !"0".equals(year) ? year : null);
        map.put("month", month < 10? "0" + month : month);
        List<BuyReconciliationPojo> buyReconciliationPojoList = taoBaoOrderService.buyReconciliationReport(map, "0");
        System.err.println("------------list :" + list.toString());
        wb = generalReportService.exportBuyOrderDetails(list, type, buyReconciliationPojoList.get(0));
        // filename = StringUtils.getFileName(filename);
        // filename = new String(filename.getBytes("ISO8859-1"), "utf-8");
        buyReconciliationPojoList.clear();
        response.setHeader("Content-disposition", "attachment;filename=" +  filename + ".xls");
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }


    /**
     * ????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-16
     */
    @RequestMapping(value = "/exportRefundOrderDetails")
    @ResponseBody
    protected void exportRefundOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        request.setCharacterEncoding("utf-8");
        Map<Object, Object> map = new HashMap<Object, Object>();
        String times = request.getParameter("times");
        String userName = request.getParameter("userName");
        String startTime = "";
        String endTime = "";
        if (times.indexOf("-") > -1) {
            startTime = times + "-01 00:00:00";
            endTime = times + "-31 23:59:59";
        } else {
            startTime = "2999-01-01 00:00:00";
            endTime = "2999-03-01 00:00:00";
        }
        System.err.println("-----------userName:" + userName);
        if (org.apache.commons.lang3.StringUtils.isBlank(userName) || "?????????".equals(userName)) {
            userName = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("userName", userName);
        map.put("limitNum", 0);
        HSSFWorkbook wb = null;
        List<TaoBaoOrderInfo> list = null;
        response.setContentType("application/vnd.ms-excel");
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        String filename = "";
        System.err.println("------------map :" + map.toString());
        taoBaoOrderService.update_taobao_refund_amount(startTime);
        list = taoBaoOrderService.getRefundAmount(map);
        // filename = "????????????????????????????????????" + times;
        filename = "captureTheNormalPurchaseOrderRefundDetails" + times;
        for (TaoBaoOrderInfo c : list) {
            if ("0".equals(c.getTbOr1688())) {
                c.setTbOr1688("??????");
            } else if ("1".equals(c.getTbOr1688())) {
                c.setTbOr1688("1688");
            } else if ("3".equals(c.getTbOr1688())) {
                c.setTbOr1688("??????");
            } else {
                c.setTbOr1688("??????");
            }
        }

        map.clear();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("year", !"0".equals(year) ? year : null);
        map.put("month", month < 10 ? "0" + month : month);
        List<BuyReconciliationPojo> buyReconciliationPojoList = taoBaoOrderService.buyReconciliationReport(map, "0");
        System.err.println("------------list :" + list.toString());
        wb = generalReportService.exportBuyOrderDetails(list, "5", buyReconciliationPojoList.get(0));
        // filename = StringUtils.getFileName(filename);
        // filename = new String(filename.getBytes("ISO8859-1"), "utf-8");
        buyReconciliationPojoList.clear();
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }


    /**
     * ?????????????????????????????????excel
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-16
     */
    @RequestMapping(value = "/exportBuyReconciliation")
    @ResponseBody
    protected void exportBuyReconciliation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        Map<Object, Object> map = new HashMap<Object, Object>();
        DecimalFormat df = new DecimalFormat("#0.###");
        String years = request.getParameter("year");
        String month = request.getParameter("month");
        String startTime = "";
        String endTime = "";
        if (!"0".equals(years) && !"0".equals(month)) {
            startTime = years + "-" + month + "-01 00:00:00";
            endTime = years + "-" + month + "-31 23:59:59";
        } else {
            startTime = "2017-04-01 00:00:00";
            endTime = null;
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        List<BuyReconciliationPojo> list = taoBaoOrderService.buyReconciliationReport(map, "1");
        HSSFWorkbook wb = generalReportService.exportBuyReconciliation(list);
        response.setContentType("application/vnd.ms-excel");

        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear() + 1900;
        String filename = "???????????????????????????????????????" + year;
        filename = StringUtils.getFileName(filename);
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-17
     */
    @RequestMapping(value = "/getZfuDate")
    @ResponseBody
    protected JsonResult getZfuDate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list = new TaoBaoInfoList();
        String date = request.getParameter("date");
        List<ZfuDate> list1 = taoBaoOrderService.getZfuDate(date);
        list.setList(list1);
        json.setData(list);
        return json;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-22
     */
    @RequestMapping(value = "/reductionInventory")
    @ResponseBody
    protected JsonResult reductionInventory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list = new TaoBaoInfoList();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String id = request.getParameter("id");// cancel_goods_inventory??????ID
        String i_id = request.getParameter("in_id");
        String i_flag = request.getParameter("i_flag");
        String inventory_qty = request.getParameter("inventory_qty");
        String goods_p_price = request.getParameter("goods_p_price");
        // ??????????????????
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        int row = 0;
        if (adm != null) {
            map.put("id", id);
            map.put("username", adm.getAdmName());
            map.put("i_id", i_id);
            map.put("i_flag", i_flag);
            map.put("inventory_qty", inventory_qty);
            map.put("amount", Double.valueOf(goods_p_price) * Integer.valueOf(inventory_qty));
            row = taoBaoOrderService.reductionInventory(map);
        }
        list.setAllCount(row);
        json.setData(list);
        return json;
    }

    /**
     * ??????/???????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-08-17
     */
    @RequestMapping(value = "/addZfbData")
    @ResponseBody
    protected JsonResult addZfbData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list = new TaoBaoInfoList();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String date = request.getParameter("date");
        String beginBlance = request.getParameter("beginBlance");
        String endBlance = request.getParameter("endBlance");
        String transfer = request.getParameter("transfer");
        String payFreight = request.getParameter("payFreight");
        String ebayAmount = request.getParameter("ebayAmount");
        String materialsAmount = request.getParameter("materialsAmount");
        String zfbFright = request.getParameter("zfbFright");
        String cancelAmount = request.getParameter("cancelAmount");
        map.put("date", date);
        map.put("beginBlance", beginBlance);
        map.put("endBlance", endBlance);
        map.put("transfer", transfer);
        map.put("payFreight", payFreight);
        map.put("ebayAmount", ebayAmount);
        map.put("materialsAmount", materialsAmount);
        map.put("zfbFright", zfbFright);
        map.put("cancelAmount", cancelAmount);
        int row = 0;
        if (StringUtil.isNotBlank(date)) {
            row = taoBaoOrderService.addZfbData(map);
        }
        list.setAllCount(row);
        json.setData(list);
        return json;
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-06-22
     */
    @RequestMapping(value = "/searchCoupusDetailsView")
    @ResponseBody
    protected JsonResult searchCoupusDetailsView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        TaoBaoInfoList list = new TaoBaoInfoList();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String id = request.getParameter("id");
        map.put("id", id);
        List<CouponSubsidiary> list1 = taoBaoOrderService.searchCoupusDetailsView(map);
        list.setList2(list1);
        json.setData(list);
        return json;
    }

    /**
     * ????????????:?????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/findAll")
    @ResponseBody
    protected EasyUiJsonResult findAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        String str = request.getParameter("page");
        String date = request.getParameter("createTime");
        String type = request.getParameter("questionType");
        IGuestBookService ibs = new GuestBookServiceImpl();
        String parse = null;
        String timeFrom = null;
        String timeTo = null;
        String time1 = request.getParameter("timeFrom");
        String time2 = request.getParameter("timeTo");
        if (time1 != null && time1 != "") {
            timeFrom = time1;
        }
        if (time2 != null && time2 != "") {
            timeTo = time2;
        }
        // ??????????????????
        int qtype = StrUtils.isNotNullEmpty(type) == true ? Integer.parseInt(type) : 0;
        // ??????????????????
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        int adminid = adm.getId();
        try {
            if (date != null && !"".equals(date)) {
                parse = date;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String s = request.getParameter("status");
        int state = -1;
        if (s != null && !"".equals(s)) {
            state = Integer.parseInt(s);
        }
        String strAdminId = request.getParameter("adminId");
        adminid = StrUtils.isNum(strAdminId) ? Integer.valueOf(strAdminId) : 0;
        String su = request.getParameter("userId");
        int userId = 0;
        if (su != null && !"".equals(su)) {
            userId = Integer.parseInt(su);
        }
        String userName = request.getParameter("userName");
        if (userName != null && !"".equals(userName)) {
        }
        String pname = request.getParameter("pname");
        if (pname != null && !"".equals(pname)) {
        }
        String useremail = request.getParameter("useremail");
        if (useremail != null && !useremail.equals("")) {
        }
        int page = 1;
        if (str == null) {
            str = "1";
        }
        if (str != null) {
            page = Integer.parseInt(str);
        }
        int start = (page - 1) * 30;
        List<GuestBookBean> findAll = ibs.findAll(userId, parse, state, userName, pname, start, 30, useremail, timeFrom,
                timeTo, adminid, qtype);
        for (GuestBookBean gBean : findAll) {
            String ppName = gBean.getPname();
            if (ppName != null && !"".equals(ppName.trim())) {
                ppName = ppName.trim().replaceAll("\'", "\\\'").replaceAll("\"", "\\\"");
                gBean.setPname(ppName);
            }
        }
        int total = ibs.total(userId, parse, state, userName, pname, start, 30, useremail, timeFrom, timeTo, adminid, qtype);
        json.setRows(findAll);
        json.setTotal(total);
        return json;
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-06-22
     */
    @RequestMapping(value = "/searchCoupusDetails")
    @ResponseBody
    protected EasyUiJsonResult searchCoupusDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String coupons_type = request.getParameter("coupons_type");
        String use_time = request.getParameter("use_time");
        String get_time = request.getParameter("get_time");
        String promo_code = request.getParameter("promo_code");
        String disbursement = request.getParameter("disbursement");
        int coupons_id = Integer
                .valueOf(request.getParameter("coupons_id") == null ? "0" : request.getParameter("coupons_id"));
        int page = Integer.valueOf(request.getParameter("page"));
        if (page > 0) {
            page = (page - 1) * 20;
        }
        if (StringUtils.isStrNull(coupons_type)) {
            coupons_type = null;
        }
        if (StringUtils.isStrNull(use_time)) {
            use_time = null;
        }
        if (StringUtils.isStrNull(get_time)) {
            get_time = null;
        }
        if (StringUtils.isStrNull(promo_code)) {
            promo_code = null;
        }
        if (StringUtils.isStrNull(disbursement)) {
            disbursement = null;
        }
        map.put("coupons_id", coupons_id);
        map.put("coupons_type", coupons_type);
        map.put("use_time", use_time);
        map.put("get_time", get_time);
        map.put("promo_code", promo_code);
        map.put("disbursement", disbursement);
        map.put("page", page);
        List<CouponSubsidiary> list1 = taoBaoOrderService.searchCoupusDetails(map);
        int acount = taoBaoOrderService.searchCoupusDetailsCount(map);
        json.setRows(list1);
        json.setTotal(acount);
        return json;
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     * @author whj 2017-06-22
     */
    @RequestMapping(value = "/searchCoupusManagement")
    @ResponseBody
    protected EasyUiJsonResult searchCoupusManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));
        String coupons_name = request.getParameter("coupons_name");
        String coupons_type = request.getParameter("coupons_type");
        String batch = request.getParameter("batch");
        String disbursement = request.getParameter("disbursement");
        String promo_code = request.getParameter("promo_code");
        String userid = request.getParameter("userid");
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        if (coupons_name == null || "".equals(coupons_name)) {
            coupons_name = null;
        }
        if (batch == null || "".equals(batch)) {
            batch = null;
        }
        if (startdate == null || "".equals(startdate)) {
            startdate = null;
        }
        if (enddate == null || "".equals(enddate)) {
            enddate = null;
        }
        if (promo_code == null || "".equals(promo_code)) {
            promo_code = null;
        }
        if (userid == null || "".equals(userid)) {
            userid = null;
        }
        if (coupons_type == null || "".equals(coupons_type)) {
            coupons_type = null;
        }
        if (disbursement == null || "".equals(disbursement)) {
            disbursement = null;
        }
        if (page > 0) {
            page = (page - 1) * 20;
        }
        map.put("page", page);
        map.put("coupons_name", coupons_name);
        map.put("coupons_type", coupons_type);
        map.put("batch", batch);
        map.put("userid", userid);
        map.put("promo_code", promo_code);
        map.put("disbursement", disbursement);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        List<Coupons> list1 = taoBaoOrderService.searchCoupusManagement(map);
        int aounts = taoBaoOrderService.searchCoupusManagementCount(map);
        json.setRows(list1);
        json.setTotal(aounts);
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return EasyUiJsonResult
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title searchGoodsInventoryUpdateInfo
     * @Description TODO
     */
    @RequestMapping(value = "/searchGoodsInventoryUpdateInfo")
    @ResponseBody
    protected EasyUiJsonResult searchGoodsInventoryUpdateInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));// ?????????
        String barcode = request.getParameter("barcode");
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        String admName = request.getParameter("admName");
        page = page > 0 ? (page - 1) * 20 : page;
        startdate = StringUtils.isStrNull(startdate) ? null : (startdate + " 00:00:00");
        enddate = StringUtils.isStrNull(enddate) ? null : (enddate + " 23:59:59");
        barcode = "??????".equals(barcode) ? null : barcode;
        admName = "??????".equals(admName) ? null : admName;
        map.put("page", page);
        map.put("barcode", barcode);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        map.put("admName", admName);
        List<Inventory> toryList = taoBaoOrderService.searchGoodsInventoryUpdateInfo(map);
        List<Inventory> toryListCount = taoBaoOrderService.searchGoodsInventoryUpdateInfoCount(map);
        json.setRows(toryList);
        json.setTotal(toryListCount.size());
        return json;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @param response
     * @return EasyUiJsonResult
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title searchSampleProduct
     * @Description TODO
     */
    @RequestMapping(value = "/searchSampleProduct")
    @ResponseBody
    protected EasyUiJsonResult searchSampleProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));// ?????????
        String pids = request.getParameter("pids");
        if (!StringUtils.isStrNull(pids) && pids.length() > 5) {
            pids = pids.trim().replace("[", "").replace("]", "");
            page = page > 0 ? (page - 1) * 20 : page;
            map.put("page", page);
            StringBuilder sb = new StringBuilder();
            if (pids.indexOf(",") > -1) {
                String[] pid = pids.split(",");
                for (String s : pid) {
                    sb.append("'").append(s.trim()).append("',");
                }
            } else {
                sb.append("'").append(pids.trim()).append("',");
            }
            map.put("pids", sb.substring(0, sb.length() - 1));
            List<Orderinfo> toryList = taoBaoOrderService.searchSampleProduct(map);
            List<Orderinfo> toryListCount = taoBaoOrderService.searchSampleProductCount(map);
            json.setRows(toryList);
            json.setTotal(toryListCount.size());
        } else {
            json.setRows(new ArrayList<Orderinfo>());
            json.setTotal(0);
        }
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/searchGoodsInventoryDeleteInfo")
    @ResponseBody
    protected EasyUiJsonResult searchGoodsInventoryDeleteInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        int page = Integer.valueOf(request.getParameter("page"));// ?????????
        String barcode = request.getParameter("barcode");
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        String admName = request.getParameter("admName");
        String times = request.getParameter("times");
        times = StringUtil.isNotBlank(times) ? times : null;
        page = page > 0 ? (page - 1) * 20 : page;
        startdate = StringUtils.isStrNull(startdate) ? null : (startdate + " 00:00:00");
        enddate = StringUtils.isStrNull(enddate) ? null : (enddate + " 23:59:59");
        barcode = "??????".equals(barcode) ? null : barcode;
        admName = "??????".equals(admName) ? null : admName;
        map.put("page", page);
        map.put("barcode", barcode);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        map.put("admName", admName);
        map.put("export", "0");
        map.put("times", times);
        List<Inventory> toryList = taoBaoOrderService.searchGoodsInventoryDeleteInfo(map);
        List<Inventory> toryListCount = taoBaoOrderService.searchGoodsInventoryDeleteInfoCount(map);
        json.setRows(toryList);
        json.setTotal(toryListCount.size());
        return json;
    }

    @RequestMapping(value = "/getAllInventory", method = RequestMethod.GET)
    @ResponseBody
    protected JSONArray getAllInventory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        List<InventoryData> list = taoBaoOrderService.getAllInventory();
        InventoryData i = new InventoryData();
        i.setGoodsCatid("0");
        i.setCategoryName("All");
        list.add(0, i);
        InventoryData ii = new InventoryData();
        ii.setGoodsCatid("-1");
        ii.setCategoryName("Other");
        list.add(list.size(), ii);
        JSONArray jsonArr = JSONArray.parseArray(JSONArray.toJSONString(list));
        return jsonArr;
    }


    /**
     * ???????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/searchinOutDetails")
    @ResponseBody
    protected JsonResult searchinOutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        JsonResult json = new JsonResult();
        Map<String, String> map = new HashMap<String, String>(5);
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid").trim();
        String type = request.getParameter("type").trim();
        String startTime = request.getParameter("startTime").trim();
        String endTime = request.getParameter("endTime").trim();
        int page = Integer.valueOf(request.getParameter("page"));// ?????????
        if (page > 0) {
            page = (page - 1) * 20;
        }
        orderid = StringUtil.isBlank(orderid) ? null : orderid;
        goodsid = StringUtil.isBlank(goodsid) ? null : goodsid;
        type = StringUtil.isBlank(type) ? null : type;
        startTime = StringUtil.isBlank(startTime) ? null : startTime;
        endTime = StringUtil.isBlank(endTime) ? null : endTime;
        map.put("orderid", orderid);
        map.put("goodsid", goodsid);
        map.put("type", type);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", String.valueOf(page));
        TaoBaoInfoList list = new TaoBaoInfoList();
        List<InOutDetailsInfo> inOutList = taoBaoOrderService.getIinOutDetails(map);
        System.out.println("inOutList===" + inOutList.size());
        List<InOutDetailsInfo> allCount = taoBaoOrderService.getIinOutDetailsCount(map);
        list.setAllCount(allCount.size());
        list.setInOutList(inOutList);
        json.setData(list);
        return json;
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @return EasyUiJsonResult
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @Title searchInOutDetails
     * @Description TODO
     */
    @RequestMapping(value = "/searchInOutDetails")
    @ResponseBody
    protected EasyUiJsonResult searchInventoryInOutDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String in_id = request.getParameter("in_id");
        int page = Integer.valueOf(request.getParameter("page"));// ?????????
        if (page > 0) {
            page = (page - 1) * 20;
        }
        in_id = StringUtils.isStrNull(in_id) ? null : in_id;
        map.put("page", page);
        map.put("in_id", in_id);
        List<StorageOutboundDetailsPojo> toryList = taoBaoOrderService.searchInventoryInOutDetails(map);
        List<StorageOutboundDetailsPojo> toryListCount = taoBaoOrderService.searchInventoryInOutDetailsCount(map);
        json.setRows(toryList);
        json.setTotal(toryListCount.size());
        return json;
    }

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/updateReply")
    @ResponseBody
    protected void updateReply(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        Map<Object, Object> map = new HashMap<Object, Object>();
        response.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");
        String replyContent = request.getParameter("replyContent");
        map.put("id", id);
        map.put("replyContent", replyContent);
        int row = taoBaoOrderService.updateReply(map);
        String remark = "";
        if (row > 0) {
            remark = taoBaoOrderService.getOperationRemark(map);
        }
        PrintWriter out = response.getWriter();
        out.print(remark);
        out.flush();
        out.close();
    }


    /**
     * ????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/inStorage")
    @ResponseBody
    protected void inStorage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        Map<Object, Object> map = new HashMap<Object, Object>();
        response.setCharacterEncoding("utf-8");
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        String import_goodsid = request.getParameter("import_goodsid");
        String import_orderid = request.getParameter("import_orderid");
        String tb_orderid = request.getParameter("tb_orderid");
        String tb_shipno = request.getParameter("tb_shipno");
        String tb_sku = request.getParameter("tb_sku");
        String tb_qty = request.getParameter("tb_qty");
        String tb_itemurl = request.getParameter("tb_itemurl");
        String tb_itemid = request.getParameter("tb_itemid");
        String type = request.getParameter("type");
        map.put("import_orderid", import_orderid);
        map.put("import_goodsid", import_goodsid);
        map.put("tb_orderid", tb_orderid);
        map.put("tb_shipno", tb_shipno);
        map.put("tb_sku", tb_sku);
        map.put("tb_qty", tb_qty);
        map.put("username", adm.getAdmName());
        map.put("userid", adm.getId());
        map.put("tb_itemurl", tb_itemurl);
        map.put("type", type);
        map.put("tb_itemid", tb_itemid);
        int row = taoBaoOrderService.inStorage(map);
        PrintWriter out = response.getWriter();
        out.print(row);
        out.flush();
        out.close();
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param reportYear
     * @param reportMonth
     * @return
     */
    @RequestMapping(value = "/selectBuyGoods")
    @ResponseBody
    protected void selectBuyGoods(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        String orderName = request.getParameter("orderName");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        int count = 0;
        int page = Integer.parseInt(request.getParameter("page"));
        List<buyGoods> list = new ArrayList<buyGoods>();
        if (orderName.equals("salesm")) {
            list = reportDetailService.selectBuyGoods(page, start, end);
        } else if (orderName.equals("saless")) {
            list = reportDetailService.selectBuyGoods1(page, start, end);
        } else if (orderName.equals("salesp")) {
            list = reportDetailService.selectBuyGoods2(page, start, end);
        }
        count = reportDetailService.selectBuyGoodsCount(start, end);
        for (buyGoods buyGoods : list) {
            buyGoods.setName(buyGoods.getName().substring(0, buyGoods.getName().length() / 3) + "...");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pre", list);
        map.put("count", count);
        map.put("page", page);
        PrintWriter out = response.getWriter();
        out.print(JSONArray.toJSON(map));
        out.flush();
        out.close();
    }

    /**
     * ??????Excel
     *
     * @throws ParseException
     * @throws Exception
     */

    @RequestMapping(value = "/export1")
    public void exportExcel1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ??????????????????????????????
        String orderName = request.getParameter("orderName");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        List<buyGoods> list = new ArrayList<buyGoods>();
        if (orderName.equals("salesm")) {
            list = reportDetailService.selectBuyGoods3(start, end);
        } else if (orderName.equals("saless")) {
            list = reportDetailService.selectBuyGoods4(start, end);
        } else if (orderName.equals("salesp")) {
            list = reportDetailService.selectBuyGoods5(start, end);
        }
        if (list != null && list.size() > 0) {
            List<ExpbuyGoods> list1 = new ArrayList<ExpbuyGoods>();
            for (int i = 0; i < list.size(); i++) {
                ExpbuyGoods eg = new ExpbuyGoods();
                eg.setId(i + 1);
                eg.setCategory(list.get(i).getCategory() != null ? list.get(i).getCategory() : " ");
                eg.setName(list.get(i).getName() == null ? " " : list.get(i).getName());
                eg.setSaleNums(list.get(i).getSaleNums() == null ? " " : list.get(i).getSaleNums());
                eg.setPrice(list.get(i).getPrice() != null ? list.get(i).getPrice() : "0.0");
                eg.setCount(list.get(i).getCount() == null ? 0 : list.get(i).getCount());
                eg.setBuycount(list.get(i).getBuycount() != null ? list.get(i).getBuycount() : "0");
                eg.setBuyprices(list.get(i).getBuyprices() != null ? list.get(i).getBuyprices() : "0");
                eg.setBuysum(list.get(i).getBuySums() == null ? "" : list.get(i).getBuySums());
                list1.add(eg);
            }
            HSSFWorkbook wb = generalReportService.export1(list1);
            // String uuid = "export1"+UUID.randomUUID().toString();
            response.setContentType("application/vnd.ms-excel");

            Date date = new Date(System.currentTimeMillis());
            int year = date.getYear() + 1900;
            int mon = date.getMonth() + 1;
            int day = date.getDate();
            int hour = date.getHours();
            int min = date.getMinutes();
            int sec = date.getSeconds();
            String filename = "BuyGoods" + year;
            if (mon < 10) {
                filename += "0" + mon;
            } else {
                filename += mon;
            }
            if (day < 10) {
                filename += "0" + day;
            } else {
                filename += day;
            }
            if (hour < 10) {
                filename += "0" + hour;
            } else {
                filename += hour;
            }
            if (min < 10) {
                filename += "0" + min;
            } else {
                filename += min;
            }
            if (sec < 10) {
                filename += "0" + sec;
            } else {
                filename += sec;
            }
            filename += ".xls";
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        }
    }

    @RequestMapping(value = "/selectExistExport")
    public void selectExistExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String year = request.getParameter("reportYear");
        String mon = request.getParameter("reportMonth");
        GeneralReport cp = new GeneralReport();
        cp.setReportYear(year);
        cp.setReportMonth(mon);
        int i = generalReportService.selectExistExport(cp);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("" + i);
        out.close();
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/saveRemark")
    public void saveRemark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String tb_id = request.getParameter("tb_id");
        String remarkContent = request.getParameter("remarkContent");
        Map<String, String> map = new HashMap<String, String>();
        try {
            map.put("tb_id", tb_id);
            map.put("remarkContent", remarkContent);
            int row = taoBaoOrderService.saveRemark(map);
            out.write("" + row);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.close();
    }

    @RequestMapping(value = "/enable")
    public void enable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.valueOf(request.getParameter("id"));
        int i = taoBaoOrderService.enable(id);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("" + i);
        out.close();
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/addCoupusDetailsView")
    public void addCoupusDetailsView(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        int id = Integer.valueOf(request.getParameter("subsidiary_id"));
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        String userid = request.getParameter("userid");
        String remark = request.getParameter("remark");
        String user_name = taoBaoOrderService.getUserName(Integer.valueOf(userid));
        int i = 0;
        if ((user_name == null || "".equals(user_name))
                && (userid != null && !"".equals(userid) && !"0".equals(userid))) {
            i = 0;
        } else {
            Map<Object, Object> map = new HashMap<Object, Object>();
            map.put("startdate", startdate);
            map.put("enddate", enddate);
            map.put("userid", userid);
            map.put("remark", remark);
            map.put("user_name", user_name);
            map.put("id", id);
            i = taoBaoOrderService.addCoupusDetailsView(map);
        }
        PrintWriter out = response.getWriter();
        out.write("" + i);
        out.close();
    }

    @RequestMapping(value = "/enableForSubsidiary")
    public void enableForSubsidiary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int i = 0;
        int id = Integer.valueOf(request.getParameter("id"));
        int is_enable = taoBaoOrderService.queryCouponsState(id);
        if (is_enable == 0) {
            i = taoBaoOrderService.enableForSubsidiary(id);
        }
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("" + i);
        out.close();
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/AllenableDetails")
    public void AllenableDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        int i = 0;
        Map<Object, Object> map = new HashMap<Object, Object>();
        String type = request.getParameter("type");
        String ordersArr = request.getParameter("ordersArr").substring(0,
                request.getParameter("ordersArr").length() - 1);
        String ids[] = ordersArr.split(",");
        StringBuffer bf = new StringBuffer("(");
        for (int j = 0; j < ids.length; j++) {
            if (j + 1 == ids.length) {
                bf.append(ids[j] + ")");
            } else {
                bf.append(ids[j]).append(",");
            }
        }
        map.put("type", Integer.valueOf(type));
        map.put("ids", bf.toString());
        int is_enable = taoBaoOrderService.queryCouponsState(Integer.valueOf(ids[0]));
        if (is_enable == 0) {
            i = taoBaoOrderService.AllenableDetails(map);
        }
        PrintWriter out = response.getWriter();
        out.write("" + i);
        out.close();
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/throughReview")
    public void ThroughReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        int i = 0;
        Map<Object, Object> map = new HashMap<Object, Object>();
        // ??????????????????
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if (adm != null) {
            String ids = "";
            String id = request.getParameter("id");
            if (id.indexOf(",") > -1) {
                ids = id.substring(0, id.length() - 1);
            } else {
                ids = id;
            }
            map.put("id", ids);
            map.put("admName", adm.getAdmName());
            i = taoBaoOrderService.ThroughReview(map);
        }
        PrintWriter out = response.getWriter();
        out.write("" + i);
        out.close();
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/Allenable")
    public void Allenable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        int i = 0;
        Map<Object, Object> map = new HashMap<Object, Object>();
        String type = request.getParameter("type");
        String ordersArr = request.getParameter("ordersArr").substring(0,
                request.getParameter("ordersArr").length() - 1);
        String ids[] = ordersArr.split(",");
        StringBuffer bf = new StringBuffer("(");
        for (int j = 0; j < ids.length; j++) {
            if (j + 1 == ids.length) {
                bf.append(ids[j] + ")");
            } else {
                bf.append(ids[j]).append(",");
            }
        }
        map.put("type", Integer.valueOf(type));
        map.put("ids", bf.toString());
        i = taoBaoOrderService.Allenable(map);
        PrintWriter out = response.getWriter();
        out.write("" + i);
        out.close();
    }

    /**
     * ??????????????????????????????
     */
    @RequestMapping(value = "/insertSalesReport")
    @ResponseBody
    public JsonResult insertSalesReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String orderName = request.getParameter("orderName");
        // String type = request.getParameter("type");
        // int page = Integer.parseInt(request.getParameter("page"));
        // ??????????????????????????????
        String[] time = startTime.split("-");
        int years = Integer.parseInt(time[0]);
        int months = Integer.parseInt(time[1]);
        int count = reportDetailService.selectSalesReportCount(startTime, endTime);
        if (count > 0) {
            reportDetailService.deleteSalesReportByDate(startTime, endTime);
            reportDetailService.deleteSalesReportInfoByMonth(years, months);
        }
        List<SalesReport> list = reportDetailService.selectSalesReport(startTime, endTime, orderName);
        // ???????????????
        int ins = reportDetailService.insertSalesReport(list);
        if (ins < 1) {
            return null;
        }
        // ?????????20???
        List<SalesReport> list2 = reportDetailService.selectSalesReportByPage(startTime, endTime, orderName, 0);
        // ????????????
        double yue_sale = 0.0, all_sale = 0.0;
        int yueOrder = 0, allOrder = 0;
        int newUser = 0, oldUser = 0;
        for (int i = 0; i < list.size(); i++) {
            SalesReport sp = list.get(i);
            yue_sale += sp.getPayprice();
            yueOrder += sp.getSl();
            all_sale += sp.getZje();
            allOrder += sp.getZsl();
            if (sp.getNewsign().equals("NEW")) {
                newUser++;
            } else {
                oldUser++;
            }
        }
        ReportSalesInfo info = new ReportSalesInfo();
        info.setYears(years);
        info.setMonths(months);
        info.setUserCount(list.size());
        info.setCost(yue_sale);
        info.setOrderCount(yueOrder);
        info.setAllCost(all_sale);
        info.setAllOrder(allOrder);
        info.setNewUser(newUser);
        info.setOldUser(oldUser);

        reportDetailService.insertSalesReportInfo(info);

        StatisticalReportVo sv = new StatisticalReportVo();
        sv.setSaleReport(list2);
        sv.setReportSalesInfo(info);
        JsonResult json = new JsonResult();
        if (list != null && list.size() > 0) {
            json.setData(sv);
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/selectSalesReport")
    @ResponseBody
    public JsonResult selectSalesReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String orderName = request.getParameter("orderName");
        // String type = request.getParameter("type");
        int page = Integer.parseInt(request.getParameter("page"));

        String[] time = startTime.split("-");
        int years = Integer.parseInt(time[0]);
        int months = Integer.parseInt(time[1]);
        List<SalesReport> list2 = reportDetailService.selectSalesReportByPage(startTime, endTime, orderName,
                (page - 1) * 20);
        int count = reportDetailService.selectSalesReportCount(startTime, endTime);
        if (list2 != null && list2.size() > 0) {
            list2.get(0).setDatacount(count);
        }
        ReportSalesInfo info = reportDetailService.selectSaleReportInfo(years, months);

        StatisticalReportVo sv = new StatisticalReportVo();
        sv.setSaleReport(list2);
        sv.setReportSalesInfo(info);
        JsonResult json = new JsonResult();
        if (list2 != null && list2.size() > 0) {
            json.setData(sv);
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    /**
     * ?????? ??????????????????
     */
    @RequestMapping(value = "/exportSales")
    public void exportSalesExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String orderName = request.getParameter("orderName");
        // String type = request.getParameter("type");
        // String ny = endTime.substring(0,endTime.lastIndexOf("-"));
        String[] str = endTime.split("-");
        String ny = str[0] + "???" + str[1] + "???";

        ReportSalesInfo info = reportDetailService.selectSaleReportInfo(Integer.parseInt(str[0]),
                Integer.parseInt(str[1]));
        List<SalesReport> list = reportDetailService.selectSalesReportByPage(startTime, endTime, orderName, -1);
        if (list.size() > 0 && list != null) {
            HSSFWorkbook wb = generalReportService.exportSalesReport(list, info, ny);
            response.setContentType("application/vnd.ms-excel");
            Calendar c = Calendar.getInstance();
            int yy = c.get(Calendar.YEAR);
            int mm = c.get(Calendar.MONTH) + 1;
            int dd = c.get(Calendar.DAY_OF_MONTH);
            int hh = c.get(Calendar.HOUR_OF_DAY);
            int mi = c.get(Calendar.MINUTE);
            int ss = c.get(Calendar.SECOND);

            String filename = "" + yy;
            filename += mm < 10 ? ("0" + mm) : mm;
            filename += dd < 10 ? ("0" + dd) : dd;
            filename += hh < 10 ? ("0" + hh) : hh;
            filename += mi < 10 ? ("0" + mi) : mi;
            filename += ss < 10 ? ("0" + ss) : ss;
            filename += ".xls";
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("<script>alert('??????????????????????????????????????????????????????????????????'); window.history.back();</script>");
            out.close();
        }
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "selectPurchaseByDate")
    @ResponseBody
    public JsonResult selectPurchaseByDate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        List<PurchaseReport> list = purchaseReportService.selectPurchaseByDate(startdate, enddate);

        JsonResult json = new JsonResult();
        StatisticalReportVo vo = new StatisticalReportVo();
        vo.setPurchaseReport(list);
        Object[] obj = new Object[2];
        double je = 0.0;
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            je += list.get(i).getJe();
            num += list.get(i).getNum();
        }
        obj[0] = num;
        obj[1] = je;
        vo.setObj(obj);
        json.setData(vo);
        if (list != null && list.size() > 0) {
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    @RequestMapping(value = "/selectUnpackPurchase")
    @ResponseBody
    public JsonResult selectUnpackPurchase(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");

        List<ReportUnpackPurchase> unpack = purchaseReportService.selectUnpacking(startdate, enddate);
        List<ReportUnpackPurchase> purchase = purchaseReportService.selectPurchasePackage(startdate, enddate);
        int unpackNum = 0;
        int purchaseNum = 0;
        // ??????????????????
        for (ReportUnpackPurchase upBean : unpack) {
            unpackNum += upBean.getNum();
        }
        // ????????????????????????
        for (ReportUnpackPurchase upBean : purchase) {
            purchaseNum += upBean.getNum();
        }
        StatisticalReportVo vo = new StatisticalReportVo();
        vo.setUnpack(unpack);
        vo.setPurchase(purchase);
        vo.setUnpackNum(unpackNum);
        vo.setPurchaseNum(purchaseNum);

        json.setData(vo);
        if ((unpack != null && unpack.size() > 0) || (purchase != null && purchase.size() > 0)) {
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    @RequestMapping(value = "/selectOutPackCount")
    @ResponseBody
    public JsonResult selectOutPackCount(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        List<ReportUnpackPurchase> list = purchaseReportService.selectOutCount(startdate, enddate);
        json.setData(list);
        if (list.size() > 0 && list != null) {
            json.setOk(true);
        } else {
            json.setOk(false);
        }
        return json;
    }

    /**
     * ???????????????
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/purchasingSpending")
    @ResponseBody
    public JsonResult purchasingSpending(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        String page = request.getParameter("page");

        return json;
    }

    /**
     * ???????????????
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/noAvailableRate")
    @ResponseBody
    public JsonResult noAvailableRateStatistics(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        try {

            List<NoAvailableRate> rsList = new ArrayList<NoAvailableRate>();
            String type = request.getParameter("type");
            // ??????????????????,?????????1:???????????? 2:???????????? 3:????????????
            if (type == null || "".equals(type)) {
                json.setOk(false);
                json.setMessage("??????????????????,?????????????????????!");
                return json;
            }
            // ?????????????????????????????????
            String beginTimeStr = request.getParameter("beginTime");
            String endTimeStr = request.getParameter("endTime");
            if (beginTimeStr == null || "".equals(beginTimeStr) || endTimeStr == null || "".equals(endTimeStr)) {
                json.setOk(false);
                json.setMessage("??????????????????????????????????????????????????????????????????!");
                return json;
            }

            // ????????????????????????yyyy-MM-dd?????????????????????yyyy-MM-dd HH:mm:ss???????????? ' 00:00:00'
            // beginTimeStr += " 00:00:00";
            // endTimeStr += " 23:59:59";
            Date beginDate = DateFormatUtil.formatStringToDate(beginTimeStr + " 00:00:00");
            Date endDate = DateFormatUtil.formatStringToDate(endTimeStr + " 23:59:59");
            if (endDate.getTime() < beginDate.getTime()) {
                json.setOk(false);
                json.setMessage("??????????????????????????????!");
                return json;
            }
            // ????????????
            if ("1".equals(type)) {

                genResult(rsList, beginDate, endDate, Calendar.MONTH, 1);

            } else if ("2".equals(type)) {// ????????????
                genResult(rsList, beginDate, endDate, Calendar.WEEK_OF_YEAR, 2);
            } else if ("3".equals(type)) {// ????????????
                genResult(rsList, beginDate, endDate, Calendar.DATE, 3);
            }

            json.setData(rsList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????,??????:" + e.getMessage());
            return json;
        }

    }


    @RequestMapping(value = "/uploadExcelFile")
    @ResponseBody
    protected JsonResult uploadExcelFile(@RequestParam(value = "file", required = false) MultipartFile file,
                                         HttpServletRequest request) {
        JsonResult json = new JsonResult();
        try {

            String type = request.getParameter("type");
            if (StringUtil.isBlank(type)) {
                json.setOk(false);
                json.setMessage("??????????????????");
                return json;
            }

            String uploadPath = SysParamUtil.getParam("uploadPath");
            LocalDate today = LocalDate.now();
            int year = today.getYear();
            int month = today.getMonthValue();
            String fileSplit;
            if (month < 10) {
                fileSplit = String.valueOf(year) + "0" + String.valueOf(month);
            } else {
                fileSplit = String.valueOf(year) + String.valueOf(month);
            }
            Long time = System.currentTimeMillis();
            String path = uploadPath + "aliPay/" + fileSplit + "/";
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            String orFileName = file.getOriginalFilename();
            String fileAllPath = path + time + orFileName.substring(orFileName.lastIndexOf("."));
            FileCopyUtils.copy(file.getBytes(), new FileOutputStream(fileAllPath));
            if ("4".equals(type)) {
                List<AliPayInfo> infoList = ExcelUtil.getAliPayInfoByExcel(fileAllPath);
                if (infoList == null || infoList.isEmpty()) {

                    json.setOk(false);
                    json.setMessage("??????????????????");
                } else {
                    reportInfoService.insertAliPayInfo(infoList);
                    infoList.clear();
                    json.setOk(true);
                }
            } else if ("0".equals(type) || "1".equals(type) || "2".equals(type) ) {
                List<AliBillingDetails> detailList = ExcelUtil.getAliBillingDetailsByExcel(fileAllPath);
                if (detailList == null || detailList.isEmpty()) {
                    json.setOk(false);
                    json.setMessage("??????????????????");
                } else {
                    detailList.stream().forEach(e -> e.setAccountType(Integer.valueOf(type)));
                    reportInfoService.insertAliBillingDetails(detailList);
                    detailList.clear();
                    json.setOk(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("???????????????" + e.getMessage());
        }
        return json;
    }


    private void genResult(List<NoAvailableRate> rsList, Date beginDate, Date endDate, int calendarType,
                           int queryType) {

        Calendar beginCld = Calendar.getInstance();
        beginCld.setTime(beginDate);
        Calendar endCld = Calendar.getInstance();
        endCld.setTime(beginDate);
        // ??????????????????????????????
        endCld.add(calendarType, 1);
        endCld.add(Calendar.DATE, -1);// ??????????????????
        int num = (int) Math.ceil((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24));
        for (int k = 0; k < num; k++) {
            // ????????????calendarType???????????????????????????????????????????????????calendarType????????????????????????????????????
            // ??????calendarType????????????????????????????????????????????????calendarType???????????????????????????????????????????????????beginCld???endCld??????
            // ??????calendarType????????????????????????????????????????????????calendarType?????????????????????????????????????????????beginCld???endDate??????
            float currentRt = 0;
            String bgStr = DateFormatUtil.getWithDay(beginCld.getTime());
            String edString = DateFormatUtil.getWithDay(endCld.getTime());
            if (beginCld.getTimeInMillis() < endDate.getTime() && endCld.getTimeInMillis() <= endDate.getTime()) {
                currentRt = purchaseReportService.noAvailableRateStatistics(bgStr + " 00:00:00",
                        edString + " 23:59:59");
            } else if (beginCld.getTimeInMillis() < endDate.getTime() && endCld.getTimeInMillis() > endDate.getTime()) {
                currentRt = purchaseReportService.noAvailableRateStatistics(bgStr + " 00:00:00",
                        DateFormatUtil.getWithDay(endDate) + " 23:59:59");
            } else {
                break;
            }
            NoAvailableRate noAlRt = new NoAvailableRate();
            noAlRt.setOrder(k + 1);
            noAlRt.setRate(currentRt * 100.0f);// ???????????????
            if (queryType == 1) {
                noAlRt.setDateStr(bgStr.substring(0, 7));
            } else if (queryType == 2) {
                noAlRt.setDateStr(bgStr.substring(0, 10) + " ~ " + edString.substring(0, 10));
            } else if (queryType == 3) {
                noAlRt.setDateStr(bgStr.substring(0, 10));
            }

            // ?????????????????????calendarType
            beginCld.add(calendarType, 1);
            endCld.add(calendarType, 1);
            rsList.add(noAlRt);
        }
    }


}