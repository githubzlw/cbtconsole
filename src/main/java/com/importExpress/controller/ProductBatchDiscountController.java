package com.importExpress.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.pojo.Admuser;
import com.cbt.service.ProductBatchService;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.CommonResult;
import com.importExpress.pojo.ProductBatchDiscount;
import com.importExpress.pojo.ProductBatchDiscountParam;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.PropertiesUtils;
import com.importExpress.utli.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequestMapping("/productDiscount")
@Controller
public class ProductBatchDiscountController {

    private static final Log logger = LogFactory.getLog(ProductBatchDiscountController.class);
    @Autowired
    private ProductBatchService productBatchService;

    private static PropertiesUtils reader = new PropertiesUtils("cbt.properties");
    private static String productBatchDiscountUrl = reader.getProperty("product.batch.discount.url");


    @RequestMapping("/list")
    @ResponseBody
    public EasyUiJsonResult list(HttpServletRequest request, HttpServletResponse response) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        String pid = request.getParameter("pid");
        String sku_id = request.getParameter("sku_id");

        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");

        ProductBatchDiscountParam param = new ProductBatchDiscountParam();
        if (StringUtils.isBlank(page)) {
            param.setPage(1);
        } else {
            param.setPage(Integer.parseInt(page));
        }
        if (StringUtils.isBlank(rows)) {
            param.setRows(10);
        } else {
            param.setRows(Integer.parseInt(rows));
        }
        param.setLimitNum(param.getRows());
        param.setStartNum((param.getPage() - 1) * param.getRows());

        try {

            if (StringUtils.isBlank(pid)) {
                param.setPid(null);
            } else {
                param.setPid(pid);
            }
            if (StringUtils.isBlank(sku_id)) {
                param.setSku_id(null);
            } else {
                param.setSku_id(sku_id);
            }

            if (userInfo.getAdmName().equalsIgnoreCase("ling") || userInfo.getAdmName().equalsIgnoreCase("testAdm")) {
                param.setAdmin_id(0);
            } else {
                param.setAdmin_id(userInfo.getId());
            }

            int listCount = productBatchService.listCount(param);
            if (listCount > 0) {
                List<ProductBatchDiscount> list = productBatchService.list(param);
                return EasyUiJsonResult.success(list, listCount);
            }
            return EasyUiJsonResult.success(new ArrayList<ProductBatchDiscount>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("list,param[" + JSONObject.toJSONString(param) + "],error:", e);
            return EasyUiJsonResult.error(e.getMessage());
        }

    }

    @RequestMapping("/insert")
    @ResponseBody
    public CommonResult insert(HttpServletRequest request, ProductBatchDiscount discount) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");
        Assert.isTrue(discount != null, "discount null");
        Assert.isTrue(StringUtils.isNotBlank(discount.getPid()), "pid null");
        Assert.isTrue(discount.getP1_num() > 0, "p1_num null");
        Assert.isTrue(discount.getP1_discount() > 0, "p1_discount null");

        try {
            discount.setAdmin_id(userInfo.getId());
            productBatchService.insert(discount);
            return CommonResult.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insert,error:", e);
            return CommonResult.failed("insert,error:" + e.getMessage());
        }
    }

    @RequestMapping("/insertBatch")
    @ResponseBody
    public CommonResult insertBatch(HttpServletRequest request) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");

        String discountListStr = request.getParameter("discountList");
        Assert.isTrue(StringUtils.isNotBlank(discountListStr), "discountList null");

        try {
            List<ProductBatchDiscount> parseArray = JSONArray.parseArray(discountListStr, ProductBatchDiscount.class);
            List<String> pidList = new ArrayList<>();

            parseArray.forEach(e -> {
                if (!pidList.contains(e.getPid())) {
                    pidList.add(e.getPid());
                }
                e.setAdmin_id(userInfo.getId());
            });

            List<ProductBatchDiscount> oldList = productBatchService.listByPid(pidList);
            pidList.clear();

            Set<String> pidSkuSet = new HashSet<>();
            oldList.forEach(e -> pidSkuSet.add(e.getPid() + "_" + (StringUtils.isNotBlank(e.getSku_id()) ? e.getSku_id() : "")));
            oldList.clear();

            StringBuffer sb = new StringBuffer();
            parseArray.forEach(e -> {
                if (pidSkuSet.contains(e.getPid() + "_" + (StringUtils.isNotBlank(e.getSku_id()) ? e.getSku_id() : ""))) {
                    sb.append(e.getPid()).append("_").append(StringUtils.isNotBlank(e.getSku_id()) ? e.getSku_id() : "");
                }
            });
            if (sb.length() > 1) {
                return CommonResult.failed(sb.toString() + ",已经存在此数据");
            }
            productBatchService.insertBatch(parseArray);

            insertProductBatchDiscount(parseArray);
            return CommonResult.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertBatch,error:", e);
            return CommonResult.failed("insertBatch,error:" + e.getMessage());
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public CommonResult update(HttpServletRequest request, ProductBatchDiscount discount) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");

        Assert.isTrue(discount != null, "discount null");
        Assert.isTrue(discount.getId() > 0, "id null");

        Assert.isTrue(StringUtils.isNotBlank(discount.getPid()), "pid null");
        Assert.isTrue(discount.getP1_num() > 0, "p1_num null");
        Assert.isTrue(discount.getP1_discount() > 0, "p1_discount null");
        try {
            productBatchService.update(discount);
            return CommonResult.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("update,error:", e);
            return CommonResult.failed("update,error:" + e.getMessage());
        }
    }


    @RequestMapping("/delete")
    @ResponseBody
    public CommonResult delete(HttpServletRequest request) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");

        String pidListStr = request.getParameter("pidList");
        Assert.isTrue(StringUtils.isNotBlank(pidListStr), "pidList null");

        String skuIdListStr = request.getParameter("skuIdList");
        try {
            List<String> pidList = Arrays.asList(pidListStr.split(","));
            List<String> skuIdList = null;
            if (StringUtils.isNotBlank(skuIdListStr)) {
                skuIdList = Arrays.asList(pidListStr.split(","));
            }
            productBatchService.delete(pidList, skuIdList);

            deleteProductBatchDiscount(pidList, skuIdList);
            return CommonResult.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("delete,error:", e);
            return CommonResult.failed("delete,error:" + e.getMessage());
        }
    }

    private void insertProductBatchDiscount(List<ProductBatchDiscount> list) {
        String sql = "insert into product_batch_discount(pid,sku_id,p1_num,p1_discount,p2_num,p2_discount,admin_id) values";
        StringBuffer sb = new StringBuffer();
        list.forEach(e -> sb.append(",('").append(e.getPid()).append("','").append(StringUtils.isNotBlank(e.getSku_id()) ? e.getSku_id() : "").append("',")
                .append(e.getP1_num()).append(",").append(e.getP1_discount()).append(",").append(e.getP2_num()).append(",")
                .append(e.getP2_discount()).append(",").append(e.getAdmin_id()).append(")") );
        NotifyToCustomerUtil.sendSqlByMq(sql + sb.toString().substring(1) + ";");
        productBatchService.asyncGet(productBatchDiscountUrl);
    }

    private void deleteProductBatchDiscount(List<String> pidList, List<String> skuIdList) {
        String sql = "delete from product_batch_discount where pid in(";
        StringBuffer sb = new StringBuffer();
        pidList.forEach(e -> sb.append(",'").append(e).append("'"));
        sql += sb.toString().substring(1) + ")";
        if (CollectionUtils.isNotEmpty(skuIdList)) {
            StringBuffer skuTemp = new StringBuffer(" and sku_id in (");
            skuIdList.forEach(e -> skuTemp.append(",'").append(e).append("'"));
            sql += skuTemp.toString().substring(1) + ")";
        }
        NotifyToCustomerUtil.sendSqlByMq(sql);

        productBatchService.asyncGet(productBatchDiscountUrl);
    }

}
