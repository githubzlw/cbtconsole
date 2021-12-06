package com.importExpress.controller;

import com.alibaba.fastjson.JSONArray;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.ImportExSku;
import com.cbt.pojo.Admuser;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Redis;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.CommonResult;
import com.importExpress.pojo.GoodsEditBean;
import com.importExpress.pojo.ProductBatchBean;
import com.importExpress.utli.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/productBatch")
@Controller
public class ProductBatchController {

    private static final Log logger = LogFactory.getLog(ProductBatchController.class);
    @Autowired
    private CustomGoodsService customGoodsService;

    private static String PID_lIST = "pidList";
    private final static String excel2003L = ".xls";    //2003- 版本的excel
    private final static String excel2007U = ".xlsx";   //2007+ 版本的excel

    @RequestMapping("/insert")
    @ResponseBody
    public CommonResult insert(HttpServletRequest request, HttpServletResponse response) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");

        String pidList = request.getParameter("pidList");
        Assert.isTrue(StringUtils.isNotBlank(pidList), "pidList null");
        try {
            List<String> list = getListByRedis(userInfo);
            if (list.size() >= 20) {
                return CommonResult.failed("已经存在超过20个商品了，请删除");
            }
            int beginSize = list.size();
            Arrays.stream(pidList.split(",")).forEach(e -> {
                if (!list.contains(e)) {
                    list.add(e);
                }
            });
            int endSize = list.size() - beginSize;
            if (list.size() > 20) {
                return CommonResult.failed("本次添加后，存在超过20个商品了，原始数据:" + beginSize + ",新添加数据:" + endSize + ",请删除!");
            }
            Redis.hset(PID_lIST, String.valueOf(userInfo.getId()), JSONArray.toJSONString(list));
            return CommonResult.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insert,error:", e);
            return CommonResult.failed("insert,error:" + e.getMessage());
        }
    }


    @RequestMapping("/list")
    @ResponseBody
    public EasyUiJsonResult list(HttpServletRequest request, HttpServletResponse response) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");
        try {

            List<ProductBatchBean> rsList = genList(userInfo);

            List<String> list = getListByRedis(userInfo);
            if (CollectionUtils.isNotEmpty(list)) {
                List<CustomGoodsPublish> customGoodsPublishes = customGoodsService.queryGoodsByPidList(list);
                customGoodsPublishes.forEach(e -> {
                    if (StringUtils.isNotBlank(e.getSku_new())) {
                        List<ImportExSku> importExSkuList = JSONArray.parseArray(e.getSku_new(), ImportExSku.class);

                        importExSkuList.forEach(el -> {
                            ProductBatchBean pb = new ProductBatchBean();
                            pb.setPid(e.getPid());
                            pb.setCatid(e.getCatid1());
                            pb.setName_en(e.getEnname());
                            pb.setName_ch(e.getName());
                            pb.setSku_id(el.getSkuId());
                            pb.setShop_id(e.getShopId());
                            pb.setUnit(e.getSellUnit());
                            pb.setWeight(BigDecimalUtil.truncateDoubleString(el.getFianlWeight() * 1000, 2));
                            pb.setVolume_weight(BigDecimalUtil.truncateDoubleString(el.getVolumeWeight() * 1000, 2));
                            pb.setPacking_size(e.getVolum());
                            if (e.getShowMainImage().contains("http")) {
                                pb.setMain_img(e.getShowMainImage());
                            } else {
                                pb.setMain_img(e.getRemotpath() + e.getShowMainImage());
                            }

                            pb.setP1_moq(e.getMorder());
                            pb.setP1_free_price(el.getSkuVal().getFreeSkuPrice());
                            pb.setP1_wprice(String.valueOf(el.getSkuVal().getSkuCalPrice()));
                            pb.setP1_1688(String.valueOf(el.getSkuVal().getCostPrice()));

                            pb.setOld_1688(e.getWholesalePrice());

                            rsList.add(pb);

                        });

                    } else {
                        ProductBatchBean pb = new ProductBatchBean();
                        pb.setPid(e.getPid());
                        pb.setCatid(e.getCatid1());
                        pb.setName_en(e.getEnname());
                        pb.setName_ch(e.getName());
                        pb.setSku_id("");
                        pb.setShop_id(e.getShopId());
                        pb.setUnit(e.getSellUnit());
                        pb.setWeight(BigDecimalUtil.truncateDoubleString(Double.parseDouble(StringUtils.isNotBlank(e.getFinalWeight()) ? e.getFinalWeight() : e.getWeight()) * 1000, 2));
                        if (StringUtils.isNotBlank(e.getVolumeWeight())) {
                            pb.setVolume_weight(BigDecimalUtil.truncateDoubleString(Double.parseDouble(e.getVolumeWeight()) * 1000, 2));
                        }
                        pb.setPacking_size(e.getVolum());
                        if (e.getShowMainImage().contains("http")) {
                            pb.setMain_img(e.getShowMainImage());
                        } else {
                            pb.setMain_img(e.getRemotpath() + e.getShowMainImage());
                        }

                        dealWpriceOrFreePrice(e, pb);

                        rsList.add(pb);
                    }
                });
            }

            return EasyUiJsonResult.success(rsList, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insert,error:", e);
            return EasyUiJsonResult.error("insert,error:" + e.getMessage());
        }
    }

    private List<ProductBatchBean> genList(Admuser userInfo) {
        List<ProductBatchBean> rsList = new ArrayList<>();

        List<String> list = getListByRedis(userInfo);
        if (CollectionUtils.isNotEmpty(list)) {
            List<CustomGoodsPublish> customGoodsPublishes = customGoodsService.queryGoodsByPidList(list);
            customGoodsPublishes.forEach(e -> {
                if (StringUtils.isNotBlank(e.getSku_new())) {
                    List<ImportExSku> importExSkuList = JSONArray.parseArray(e.getSku_new(), ImportExSku.class);

                    importExSkuList.forEach(el -> {
                        ProductBatchBean pb = new ProductBatchBean();
                        pb.setPid(e.getPid());
                        pb.setCatid(e.getCatid1());
                        pb.setName_en(e.getEnname());
                        pb.setName_ch(e.getName());
                        pb.setSku_id(el.getSkuId());
                        pb.setShop_id(e.getShopId());
                        pb.setUnit(e.getSellUnit());
                        pb.setWeight(BigDecimalUtil.truncateDoubleString(el.getFianlWeight() * 1000, 2));
                        pb.setVolume_weight(BigDecimalUtil.truncateDoubleString(el.getVolumeWeight() * 1000, 2));
                        pb.setPacking_size(e.getVolum());
                        if (e.getShowMainImage().contains("http")) {
                            pb.setMain_img(e.getShowMainImage());
                        } else {
                            pb.setMain_img(e.getRemotpath() + e.getShowMainImage());
                        }

                        pb.setP1_moq(e.getMorder());
                        pb.setP1_free_price(el.getSkuVal().getFreeSkuPrice());
                        pb.setP1_wprice(String.valueOf(el.getSkuVal().getSkuCalPrice()));
                        pb.setP1_1688(String.valueOf(el.getSkuVal().getCostPrice()));

                        pb.setOld_1688(e.getWholesalePrice());

                        rsList.add(pb);

                    });

                } else {
                    ProductBatchBean pb = new ProductBatchBean();
                    pb.setPid(e.getPid());
                    pb.setCatid(e.getCatid1());
                    pb.setName_en(e.getEnname());
                    pb.setName_ch(e.getName());
                    pb.setSku_id("");
                    pb.setShop_id(e.getShopId());
                    pb.setUnit(e.getSellUnit());
                    pb.setWeight(BigDecimalUtil.truncateDoubleString(Double.parseDouble(StringUtils.isNotBlank(e.getFinalWeight()) ? e.getFinalWeight() : e.getWeight()) * 1000, 2));
                    if (StringUtils.isNotBlank(e.getVolumeWeight())) {
                        pb.setVolume_weight(BigDecimalUtil.truncateDoubleString(Double.parseDouble(e.getVolumeWeight()) * 1000, 2));
                    }
                    pb.setPacking_size(e.getVolum());
                    if (e.getShowMainImage().contains("http")) {
                        pb.setMain_img(e.getShowMainImage());
                    } else {
                        pb.setMain_img(e.getRemotpath() + e.getShowMainImage());
                    }

                    dealWpriceOrFreePrice(e, pb);

                    rsList.add(pb);
                }
            });
        }
        return rsList;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public CommonResult delete(HttpServletRequest request, HttpServletResponse response) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");

        String pidList = request.getParameter("pidList");
        Assert.isTrue(StringUtils.isNotBlank(pidList), "pidList null");
        try {
            List<String> list = getListByRedis(userInfo);


            List<String> listNew = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(list)) {
                List<String> strings = Arrays.asList(pidList.split(","));
                list.forEach(e -> {
                    if (!strings.contains(e)) {
                        listNew.add(e);
                    }
                });
            }
            list.clear();
            Redis.hset(PID_lIST, String.valueOf(userInfo.getId()), JSONArray.toJSONString(listNew));

            return CommonResult.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("delete,error:", e);
            return CommonResult.failed("delete,error:" + e.getMessage());
        }
    }

    @RequestMapping("/deleteAll")
    @ResponseBody
    public CommonResult deleteAll(HttpServletRequest request, HttpServletResponse response) {
        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");
        try {
            List<String> listNew = new ArrayList<>();
            Redis.hset(PID_lIST, String.valueOf(userInfo.getId()), JSONArray.toJSONString(listNew));
            return CommonResult.success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteAll,error:", e);
            return CommonResult.failed("deleteAll,error:" + e.getMessage());
        }
    }


    /**
     * 导出 销售统计报表
     */
    @RequestMapping(value = "/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        Assert.isTrue(userInfo != null, "请登录后执行");

        try {
            List<ProductBatchBean> rsList = genList(userInfo);
            if (CollectionUtils.isNotEmpty(rsList)) {

                Calendar c = Calendar.getInstance();
                int yy = c.get(Calendar.YEAR);
                int mm = c.get(Calendar.MONTH) + 1;
                int dd = c.get(Calendar.DAY_OF_MONTH);
                int hh = c.get(Calendar.HOUR_OF_DAY);
                int mi = c.get(Calendar.MINUTE);
                int ss = c.get(Calendar.SECOND);

                String sheetName = "产品批量编辑导出";

                String filename = "" + yy;
                filename += mm < 10 ? ("0" + mm) : mm;
                filename += dd < 10 ? ("0" + dd) : dd;
                filename += "-";
                filename += hh < 10 ? ("0" + hh) : hh;
                filename += mi < 10 ? ("0" + mi) : mi;
                filename += ss < 10 ? ("0" + ss) : ss;
                filename += "-";
                filename += userInfo.getAdmName() + ".xls";

                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet(sheetName);
                String[] heji = new String[]{"PID", "产品名称", "所属类别", "店铺ID", "skuId", "重量(g)", "体积重量(g)",
                        "单位", "P1-MOQ", "P1免邮价($)", "P1非免邮价($)", "P1-1688价格(￥)", "P2-MOQ", "P2免邮价($)", "P2非免邮价($)", "P2-1688价格(￥)",
                        "P3-MOQ", "P3免邮价($)", "P3非免邮价($)", "P3-1688价格(￥)"};
                int rows = 0;  //记录行数
                HSSFRow row = sheet.createRow(rows++);
                HSSFCellStyle style = wb.createCellStyle();
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                //写入报表汇总
                HSSFCell hcell = row.createCell(rows++); //添加标题
                hcell.setCellValue("SKU的商品价格部分请填写P1的相关内容，非SKU商品按照P1-P2-P3填写，P2-P3没有的不填写");
                row = sheet.createRow(rows++);  //到下一行添加数据
                for (int i = 0; i < heji.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(heji[i]);
                    cell.setCellStyle(style);
                }
                int size = rsList.size();
                for (int i = 0; i < size; i++) {
                    row = sheet.createRow(rows++);
                    ProductBatchBean batchBean = rsList.get(i);
                    int tempNum = 0;
                    row.createCell(tempNum++).setCellValue(batchBean.getPid());
                    row.createCell(tempNum++).setCellValue(batchBean.getName_en());
                    row.createCell(tempNum++).setCellValue(batchBean.getCatid());
                    row.createCell(tempNum++).setCellValue(batchBean.getShop_id());
                    row.createCell(tempNum++).setCellValue(batchBean.getSku_id());
                    row.createCell(tempNum++).setCellValue(batchBean.getWeight());
                    row.createCell(tempNum++).setCellValue(batchBean.getVolume_weight());
                    row.createCell(tempNum++).setCellValue(batchBean.getUnit());
                    row.createCell(tempNum++).setCellValue(batchBean.getP1_moq());
                    row.createCell(tempNum++).setCellValue(batchBean.getP1_free_price());
                    row.createCell(tempNum++).setCellValue(batchBean.getP1_wprice());
                    row.createCell(tempNum++).setCellValue(batchBean.getP1_1688());
                    if (batchBean.getP2_moq() > 0) {
                        row.createCell(tempNum++).setCellValue(batchBean.getP2_moq());
                        row.createCell(tempNum++).setCellValue(batchBean.getP2_free_price());
                        row.createCell(tempNum++).setCellValue(batchBean.getP2_wprice());
                        row.createCell(tempNum++).setCellValue(batchBean.getP2_1688());
                    } else {
                        row.createCell(tempNum++).setCellValue("");
                        row.createCell(tempNum++).setCellValue("");
                        row.createCell(tempNum++).setCellValue("");
                        row.createCell(tempNum++).setCellValue("");
                    }
                    if (batchBean.getP3_moq() > 0) {
                        row.createCell(tempNum++).setCellValue(batchBean.getP3_moq());
                        row.createCell(tempNum++).setCellValue(batchBean.getP3_free_price());
                        row.createCell(tempNum++).setCellValue(batchBean.getP3_wprice());
                        row.createCell(tempNum).setCellValue(batchBean.getP3_1688());
                    } else {
                        row.createCell(tempNum++).setCellValue("");
                        row.createCell(tempNum++).setCellValue("");
                        row.createCell(tempNum++).setCellValue("");
                        row.createCell(tempNum).setCellValue("");
                    }
                }
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment;filename=" + filename);
                OutputStream ouputStream = response.getOutputStream();
                wb.write(ouputStream);
                ouputStream.flush();
                ouputStream.close();

            } else {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write("<script>alert('没有数据，无法导出。'); window.history.back();</script>");
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("<script>alert('程序出错，无法导出。'); window.history.back();</script>");
            out.close();

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
            List<ProductBatchBean> productBatchBeans = genWbData(in, file.getOriginalFilename());// 获取excel数据
            if (CollectionUtils.isNotEmpty(productBatchBeans)) {
                // 分组，使用PID做key
                Map<String, List<ProductBatchBean>> collect = productBatchBeans.stream().collect(Collectors.groupingBy(ProductBatchBean::getPid));
                size = collect.size();

                //List<String> list = getListByRedis(userInfo);

                List<String> stringList = productBatchBeans.stream().map(ProductBatchBean::getPid).collect(Collectors.toList());
                List<CustomGoodsPublish> customGoodsPublishes = customGoodsService.queryGoodsByPidList(stringList);
                Map<String, CustomGoodsPublish> gdMap = new HashMap<>();
                customGoodsPublishes.forEach(e -> gdMap.put(e.getPid(), e));

                collect.forEach((k, v) -> {

                    // --------------------类别更新暂时不支持

                    CustomGoodsPublish goodsPublish = gdMap.get(k);//取商品数据
                    // 插入日志
                    GoodsEditBean editBean = new GoodsEditBean();
                    editBean.setPublish_flag(1);
                    editBean.setAdmin_id(userInfo.getId());

                    editBean.setOld_title(goodsPublish.getEnname());
                    editBean.setPid(goodsPublish.getPid());
                    editBean.setWprice_old(goodsPublish.getWprice());
                    editBean.setRange_price_old(goodsPublish.getRangePrice());
                    editBean.setFeeprice_old(goodsPublish.getFeeprice());

                    // SKU的商品
                    Map<String, ImportExSku> exSkuMap = new HashMap<>();
                    List<ImportExSku> importExSkuList = new ArrayList<>();
                    if (StringUtils.isNotBlank(goodsPublish.getSku_new())) {
                        // 如果存在SKU，则生成SKU的map数据
                        importExSkuList = JSONArray.parseArray(goodsPublish.getSku_new(), ImportExSku.class);
                        importExSkuList.forEach(e -> exSkuMap.put(e.getSkuId(), e));
                    }


                    if (v.size() == 1) {
                        v.forEach(cl -> {
                            goodsPublish.setEnname(cl.getName_en());
                            // 更新MOQ
                            if (cl.getP1_moq() > 0) {
                                goodsPublish.setMorder(cl.getP1_moq());
                            }
                            if (StringUtils.isNotBlank(cl.getUnit())) {
                                goodsPublish.setSellUnit(cl.getUnit());
                            }
                            // 如果是skuId有值，则更新sku的数据
                            if (StringUtils.isNotBlank(cl.getSku_id())) {
                                if (exSkuMap.containsKey(cl.getSku_id())) {
                                    ImportExSku iSu = exSkuMap.get(cl.getSku_id());
                                    if (StringUtils.isNotBlank(cl.getWeight())) {
                                        iSu.setFianlWeight(BigDecimalUtil.truncateDouble(Double.parseDouble(cl.getWeight()) / 1000D, 2));
                                    }
                                    if (StringUtils.isNotBlank(cl.getVolume_weight())) {
                                        iSu.setVolumeWeight(BigDecimalUtil.truncateDouble(Double.parseDouble(cl.getVolume_weight()) / 1000D, 2));
                                    }
                                    iSu.getSkuVal().setFreeSkuPrice(cl.getP1_free_price());
                                    iSu.getSkuVal().setSkuCalPrice(BigDecimalUtil.truncateFloat(Float.parseFloat(cl.getP1_wprice()), 2));
                                }
                            } else {
                                if (StringUtils.isNotBlank(cl.getWeight())) {
                                    goodsPublish.setFinalWeight(BigDecimalUtil.truncateDoubleString(Double.parseDouble(cl.getWeight()) / 1000D, 2));
                                }
                                if (StringUtils.isNotBlank(cl.getVolume_weight())) {
                                    goodsPublish.setVolumeWeight(BigDecimalUtil.truncateDoubleString(Double.parseDouble(cl.getVolume_weight()) / 1000D, 2));
                                }

                                // 否则组合wprice和freePrice [1-4 $ 848.25, 5-9 $ 805.84, ≥10 $ 781.66]
                                StringBuilder wpSb = new StringBuilder();
                                wpSb.append("[");
                                StringBuilder frSb = new StringBuilder();
                                frSb.append("[");

                                if (cl.getP2_moq() > 0) {
                                    wpSb.append(cl.getP1_moq()).append("-").append(cl.getP2_moq() - 1).append(" $ ").append(cl.getP1_wprice());
                                    if (cl.getP3_moq() > 0) {
                                        wpSb.append(", ").append(cl.getP2_moq()).append("-").append(cl.getP3_moq() - 1).append(" $ ").append(cl.getP2_wprice());
                                        wpSb.append(", ≥").append(cl.getP3_moq()).append(" $ ").append(cl.getP3_wprice()).append("]");
                                    }

                                    frSb.append(cl.getP1_moq()).append("-").append(cl.getP2_moq() - 1).append(" $ ").append(cl.getP1_free_price());
                                    if (cl.getP3_moq() > 0) {
                                        frSb.append(", ").append(cl.getP2_moq()).append("-").append(cl.getP3_moq() - 1).append(" $ ").append(cl.getP2_free_price());
                                        frSb.append(", ≥").append(cl.getP3_moq()).append(" $ ").append(cl.getP3_free_price()).append("]");
                                    }
                                } else {
                                    wpSb.append("≥").append(cl.getP1_moq()).append(" $ ").append(cl.getP1_wprice()).append("]");

                                    frSb.append("≥").append(cl.getP1_moq()).append(" $ ").append(cl.getP1_wprice()).append("]");
                                }
                                goodsPublish.setWprice(wpSb.toString());
                                goodsPublish.setFree_price_new(frSb.toString());
                            }
                        });
                    } else {
                        // 多个值的，必定是sku的数据
                        v.forEach(cl -> {
                            goodsPublish.setEnname(cl.getName_en());
                            if (cl.getP1_moq() > 0) {
                                goodsPublish.setMorder(cl.getP1_moq());
                            }
                            if (StringUtils.isNotBlank(cl.getUnit())) {
                                goodsPublish.setSellUnit(cl.getUnit());
                            }

                            if (StringUtils.isNotBlank(cl.getSku_id())) {
                                if (exSkuMap.containsKey(cl.getSku_id())) {
                                    ImportExSku iSu = exSkuMap.get(cl.getSku_id());
                                    if (StringUtils.isNotBlank(cl.getWeight())) {
                                        iSu.setFianlWeight(BigDecimalUtil.truncateDouble(Double.parseDouble(cl.getWeight()) / 1000D, 2));
                                    }
                                    if (StringUtils.isNotBlank(cl.getVolume_weight())) {
                                        iSu.setVolumeWeight(BigDecimalUtil.truncateDouble(Double.parseDouble(cl.getVolume_weight()) / 1000D, 2));
                                    }
                                    iSu.getSkuVal().setFreeSkuPrice(cl.getP1_free_price());
                                    iSu.getSkuVal().setSkuCalPrice(BigDecimalUtil.truncateFloat(Float.parseFloat(cl.getP1_wprice()), 2));
                                }
                            }
                        });
                    }

                    exSkuMap.clear();
                    if (StringUtils.isNotBlank(goodsPublish.getSku_new())) {
                        goodsPublish.setSku_new(JSONArray.toJSONString(importExSkuList));
                        //生成rangePrice
                        float maxFrPr = 0;
                        float minFrPr = 0;

                        float maxWpPr = 0;
                        float minWpPr = 0;

                        for (ImportExSku e : importExSkuList) {
                            //wprice
                            if (maxWpPr == 0 || maxWpPr < e.getSkuVal().getSkuCalPrice()) {
                                maxWpPr = e.getSkuVal().getSkuCalPrice();
                            }
                            if (minWpPr == 0 || minWpPr > e.getSkuVal().getSkuCalPrice()) {
                                minWpPr = e.getSkuVal().getSkuCalPrice();
                            }
                            if (maxWpPr == minWpPr) {
                                goodsPublish.setRangePrice(BigDecimalUtil.truncateDoubleString(minWpPr, 2));
                            } else {
                                goodsPublish.setRangePrice(BigDecimalUtil.truncateDoubleString(minWpPr, 2) + "-" + BigDecimalUtil.truncateDoubleString(maxWpPr, 2));
                            }

                            //freePrice
                            if (maxFrPr == 0 || maxFrPr < Float.parseFloat(e.getSkuVal().getFreeSkuPrice())) {
                                maxFrPr = Float.parseFloat(e.getSkuVal().getFreeSkuPrice());
                            }
                            if (minFrPr == 0 || minFrPr > Float.parseFloat(e.getSkuVal().getFreeSkuPrice())) {
                                minFrPr = Float.parseFloat(e.getSkuVal().getFreeSkuPrice());
                            }

                            if (maxFrPr == minFrPr) {
                                goodsPublish.setRange_price_free_new(BigDecimalUtil.truncateDoubleString(minFrPr, 2));
                            } else {
                                goodsPublish.setRange_price_free_new(BigDecimalUtil.truncateDoubleString(minFrPr, 2) + "-" + BigDecimalUtil.truncateDoubleString(maxFrPr, 2));
                            }
                        }

                        importExSkuList.clear();
                    }

                    editBean.setNew_title(goodsPublish.getEnname());
                    editBean.setWprice_new(goodsPublish.getWprice());
                    editBean.setFeeprice_new(goodsPublish.getFeeprice());
                    editBean.setRange_price_new(goodsPublish.getRange_price_free_new());
                    // 插入编辑日志
                    customGoodsService.insertIntoGoodsPriceOrWeight(editBean);

                    customGoodsService.insertPidIsEdited(goodsPublish.getShopId(), goodsPublish.getPid(), userInfo.getId());
                });

                //批量更新 customGoodsPublishes
                customGoodsService.batchUpdatePriceAndWeight(customGoodsPublishes);

                // 更新Mongo
                customGoodsPublishes.parallelStream().forEach(e -> {
                    try {
                        CustomGoodsPublish goodsPublish = customGoodsService.queryGoodsDetails(e.getPid(), 0);
                        customGoodsService.publish(goodsPublish);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        logger.error("pid:[" + e.getPid() + "],publish error:", e1);
                    }
                });
                collect.clear();
                customGoodsPublishes.clear();

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

    private static List<ProductBatchBean> genWbData(InputStream in, String fileName) throws Exception {
        //创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        List<ProductBatchBean> rsList = new ArrayList<>();
        sheet = work.getSheetAt(0);
        if (sheet == null) {
            return rsList;
        }
        //遍历当前sheet中的所有行
        //包涵头部，所以要小于等于最后一列数,这里也可以在初始值加上头部行数，以便跳过头部
        for (int j = sheet.getFirstRowNum() + 3; j <= sheet.getLastRowNum(); j++) {
            //读取一行
            row = sheet.getRow(j);
            //去掉空行和表头
            if (row == null || row.getFirstCellNum() == j) {
                continue;
            }
            //遍历所有的列
            ProductBatchBean batchBean = new ProductBatchBean();
            int y = row.getFirstCellNum();
            batchBean.setPid(row.getCell(y++).getStringCellValue());
            batchBean.setName_en(row.getCell(y++).getStringCellValue());
            if (row.getCell(y).getCellType() == 0) {
                batchBean.setCatid(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setCatid(row.getCell(y).getStringCellValue());
            }
            y++;

            batchBean.setShop_id(row.getCell(y++).getStringCellValue());
            batchBean.setSku_id(row.getCell(y++).getStringCellValue());
            if (row.getCell(y).getCellType() == 0) {
                batchBean.setWeight(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setWeight(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                batchBean.setVolume_weight(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setVolume_weight(row.getCell(y).getStringCellValue());
            }
            y++;

            batchBean.setUnit(row.getCell(y++).getStringCellValue());
            if (row.getCell(y).getNumericCellValue() > 0) {
                batchBean.setP1_moq((int) row.getCell(y).getNumericCellValue());
            }
            y++;
            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP1_free_price(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP1_free_price(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP1_wprice(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP1_wprice(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP1_1688(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP1_1688(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                if (row.getCell(y).getNumericCellValue() > 0) {
                    batchBean.setP2_moq((int) row.getCell(y).getNumericCellValue());
                }
            } else {
                if (StringUtils.isNotBlank(row.getCell(y).getStringCellValue())) {
                    batchBean.setP2_moq(Integer.parseInt(row.getCell(y).getStringCellValue()));
                }
            }
            y++;


            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP2_free_price(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP2_free_price(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP2_wprice(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP2_wprice(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP2_1688(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP2_1688(row.getCell(y).getStringCellValue());
            }
            y++;


            if (row.getCell(y).getCellType() == 0) {
                if (row.getCell(y).getNumericCellValue() > 0) {
                    batchBean.setP3_moq((int) row.getCell(y).getNumericCellValue());
                }
            } else {
                if (StringUtils.isNotBlank(row.getCell(y).getStringCellValue())) {
                    batchBean.setP3_moq(Integer.parseInt(row.getCell(y).getStringCellValue()));
                }
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP3_free_price(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP3_free_price(row.getCell(y).getStringCellValue());
            }
            y++;


            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP3_wprice(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP3_wprice(row.getCell(y).getStringCellValue());
            }
            y++;

            if (row.getCell(y).getCellType() == 0) {
                batchBean.setP3_1688(String.valueOf(row.getCell(y).getNumericCellValue()));
            } else {
                batchBean.setP3_1688(row.getCell(y).getStringCellValue());
            }

            rsList.add(batchBean);
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

    private void dealWpriceOrFreePrice(CustomGoodsPublish gd, ProductBatchBean pb) {
        if (StringUtils.isNotBlank(gd.getWprice()) && gd.getWprice().length() > 5) {
            String tempWprice = gd.getWprice().replace("$", "@").replace("[", "").replace("]", "").trim();
            String[] splitWp = tempWprice.split(",");

            for (int i = 0; i < splitWp.length; i++) {
                String[] elWPrice = splitWp[i].split("@");
                switch (i) {
                    case 0:
                        if (elWPrice[0].contains("-")) {
                            pb.setP1_moq(Integer.parseInt(elWPrice[0].split("-")[0].trim()));
                        } else if (elWPrice[0].contains("≥")) {
                            pb.setP1_moq(Integer.parseInt(elWPrice[0].split("≥")[1].trim()));
                        } else {
                            pb.setP1_moq(Integer.parseInt(elWPrice[0].trim()));
                        }
                        pb.setP1_wprice(elWPrice[1].trim());
                        break;
                    case 1:
                        if (elWPrice[0].contains("-")) {
                            pb.setP2_moq(Integer.parseInt(elWPrice[0].split("-")[0].trim()));
                        } else if (elWPrice[0].contains("≥")) {
                            pb.setP2_moq(Integer.parseInt(elWPrice[0].split("≥")[1].trim()));
                        } else {
                            pb.setP2_moq(Integer.parseInt(elWPrice[0].trim()));
                        }
                        pb.setP2_wprice(elWPrice[1].trim());
                        break;
                    case 2:
                        if (elWPrice[0].contains("-")) {
                            pb.setP3_moq(Integer.parseInt(elWPrice[0].split("-")[0].trim()));
                        } else if (elWPrice[0].contains("≥")) {
                            pb.setP3_moq(Integer.parseInt(elWPrice[0].split("≥")[1].trim()));
                        } else {
                            pb.setP3_moq(Integer.parseInt(elWPrice[0].trim()));
                        }
                        pb.setP3_wprice(elWPrice[1].trim());
                        break;
                }
                //
            }
        }
        if (StringUtils.isNotBlank(gd.getFree_price_new()) && gd.getFree_price_new().length() > 5) {
            String tempFreePrice = gd.getFree_price_new().replace("$", "@").replace("[", "").replace("]", "").trim();
            String[] splitFp = tempFreePrice.split(",");

            for (int i = 0; i < splitFp.length; i++) {
                String[] elFPrice = splitFp[i].split("@");
                switch (i) {
                    case 0:
                        pb.setP1_free_price(elFPrice[1].trim());
                        break;
                    case 1:
                        pb.setP2_free_price(elFPrice[1].trim());
                        break;
                    case 2:
                        pb.setP3_free_price(elFPrice[1].trim());
                        break;
                }
            }
        }

        if (StringUtils.isNotBlank(gd.getWholesalePrice()) && gd.getWholesalePrice().length() > 5) {
            String tempWsPrice = gd.getWholesalePrice().replace("$", "@").replace("[", "").replace("]", "").trim();
            String[] splitWsp = tempWsPrice.split(",");
            for (int i = 0; i < splitWsp.length; i++) {
                String[] elWhPrice = splitWsp[i].split("@");
                switch (i) {
                    case 0:
                        pb.setP1_1688(elWhPrice[1].trim());
                        break;
                    case 1:
                        pb.setP2_1688(elWhPrice[1].trim());
                        break;
                    case 2:
                        pb.setP3_1688(elWhPrice[1].trim());
                        break;
                }
            }
        }
    }


    private List<String> getListByRedis(Admuser userInfo) {
        List<String> list = new ArrayList<>();
        String oldList = Redis.hget(PID_lIST, String.valueOf(userInfo.getId()));
        if (StringUtils.isNotBlank(oldList)) {
            JSONArray jsonArray = JSONArray.parseArray(oldList);
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(jsonArray.getString(i));
            }
        }
        return list;
    }

}
