package com.importExpress.controller;

import com.alibaba.fastjson.JSONArray;
import com.cbt.pojo.Admuser;
import com.cbt.util.BigDecimalUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.AmazonOrderBean;
import com.importExpress.pojo.AmazonOrderParam;
import com.importExpress.pojo.CommonResult;
import com.importExpress.pojo.ProductBatchBean;
import com.importExpress.service.AmazonProductService;
import com.importExpress.utli.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/amazonOrderCtr")
public class AmazonOrderController {
    private static final Log logger = LogFactory.getLog(AmazonOrderController.class);

    private final static String excel2003L = ".xls";    //2003- 版本的excel
    private final static String excel2007U = ".xlsx";   //2007+ 版本的excel

    @Autowired
    private AmazonProductService amazonProductService;


    @RequestMapping("/list")
    @ResponseBody
    public EasyUiJsonResult list(HttpServletRequest request, HttpServletResponse response) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");

        try {

            AmazonOrderParam param = new AmazonOrderParam();
            if (StringUtils.isBlank(page)) {
                param.setPageNum(1);
            } else {
                param.setPageNum(Integer.parseInt(page));
            }
            if (StringUtils.isBlank(rows)) {
                param.setPageSize(50);
            } else {
                param.setPageSize(Integer.parseInt(rows));
            }
            param.setPageSize(Integer.parseInt(rows));
            param.setPageNum((param.getPageNum() - 1) * param.getPageSize());

            int count = amazonProductService.queryAmazonOrderListCount(param);
            List<AmazonOrderBean> orderBeanList = amazonProductService.queryAmazonOrderList(param);
            if (CollectionUtils.isEmpty(orderBeanList)) {
                orderBeanList = new ArrayList<>();
            }

            return EasyUiJsonResult.success(orderBeanList, count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insert,error:", e);
            return EasyUiJsonResult.error("insert,error:" + e.getMessage());
        }
    }

    /**
     * 上传excel并将内容导入数据库中
     *
     * @return
     */
    @RequestMapping(value = "/import")
    @ResponseBody
    public CommonResult importExcel(@RequestParam(value = "uploadfile", required = true) MultipartFile file, HttpServletRequest request) throws Exception {


        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");
        if (!file.getOriginalFilename().endsWith(excel2003L) && !file.getOriginalFilename().endsWith(excel2007U)) {
            return CommonResult.failed("非" + excel2003L + "或者" + excel2007U + "结尾的文件");
        }
        InputStream in = null;
        try {
            in = file.getInputStream();

            int size = 0;
            List<AmazonOrderBean> orderBeanList = genWbData(in, file.getOriginalFilename());// 获取excel数据
            if (CollectionUtils.isNotEmpty(orderBeanList)) {
                amazonProductService.insertAmazonOrderList(orderBeanList);
            }
            return CommonResult.success("执行成功，更新数量:" + size);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("importExcel,error:", e);
            return CommonResult.failed(e.getMessage());
        } finally {
            if (null != in) {
                in.close();
            }
        }
    }


    private static List<AmazonOrderBean> genWbData(InputStream in, String fileName) throws Exception {
        //创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        List<AmazonOrderBean> rsList = new ArrayList<>();
        sheet = work.getSheetAt(0);
        if (sheet == null) {
            return rsList;
        }
        //遍历当前sheet中的所有行
        //包涵头部，所以要小于等于最后一列数,这里也可以在初始值加上头部行数，以便跳过头部
        for (int j = sheet.getFirstRowNum() + 1; j <= sheet.getLastRowNum(); j++) {
            //读取一行
            row = sheet.getRow(j);
            //去掉空行和表头
            if (row == null || row.getFirstCellNum() == j) {
                continue;
            }
            //遍历所有的列
            AmazonOrderBean orderBean = new AmazonOrderBean();
            int y = row.getFirstCellNum();
            orderBean.setDate_time(row.getCell(y++).getStringCellValue());
            orderBean.setFn_sku(row.getCell(y++).getStringCellValue());

            orderBean.setAsin(row.getCell(y++).getStringCellValue());
            orderBean.setMsku(row.getCell(y++).getStringCellValue());
            orderBean.setTitle(row.getCell(y++).getStringCellValue());
            orderBean.setDisposition(row.getCell(y++).getStringCellValue());

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setStarting_warehouse_balance(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setStarting_warehouse_balance(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setIn_transit_between_warehouses(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setIn_transit_between_warehouses(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setReceipts(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setReceipts(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setCustomer_shipments(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setCustomer_shipments(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setCustomer_returns(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setCustomer_returns(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setVendor_returns(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setVendor_returns(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setWarehouse_transfer(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setWarehouse_transfer(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setFound(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setFound(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setLost(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setLost(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setDamaged(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setDamaged(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setDisposed(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setDisposed(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setOther_events(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setOther_events(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setEnding_warehouse_balance(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setEnding_warehouse_balance(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                orderBean.setUnknown_events(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setUnknown_events(row.getCell(y).getStringCellValue());
            }
            y++;

            orderBean.setLocation(row.getCell(y++).getStringCellValue());

            /*if (row.getCell(y).getCellType() == 0) {
                orderBean.setCatid(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                orderBean.setCatid(row.getCell(y).getStringCellValue());
            }
            y++;*/


            rsList.add(orderBean);
        }
        return rsList;
    }


    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     */
    private static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            wb = new HSSFWorkbook(inStr);  //2003-
        } else if (excel2007U.equals(fileType)) {
            wb = new XSSFWorkbook(inStr);  //2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }


}
