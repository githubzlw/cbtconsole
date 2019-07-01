package com.importExpress.utli;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.util.DateFormatUtil;
import com.cbt.util.FirstLetterUtitl;
import com.cbt.util.StrUtils;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.CustomBenchmarkSkuNew;
import com.importExpress.pojo.InputData;
import com.importExpress.pojo.SkuValPO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodsInfoUpdateOnlineUtil {
    private static final Log logger = LogFactory.getLog(GoodsInfoUpdateOnlineUtil.class);
    private static OKHttpUtils okHttpUtils = new OKHttpUtils();

    /**
     * 刷新产品表数据
     */
    private static final String MONGODB_UPDATE_GOODS_URL_LOCAL = "http://192.168.1.153:8001/invokejob/b004";

    /**
     * 刷新solr的
     */
    private static final String MONGODB_UPDATE_SOLR_URL_LOCAL = "http://192.168.1.153:8001/invokejob/b006";

    /**
     * online
     */
    private static final String LOCAL_JSON_PATH = "/data/cbtconsole/product/";
    /**
     * 刷新产品表数据
     */
    private static final String MONGODB_UPDATE_GOODS_URL_ONLINE = "http://35.166.131.70:18001/invokejob/b004";
    /**
     * 刷新solr的
     */
    private static final String MONGODB_UPDATE_SOLR_URL_ONLINE = "http://35.166.131.70:18001/invokejob/b006";

    /**
     * 生成events文件路径
     */
    private static final String LOCAL_EVENTS_PATH = "/data/cbtconsole/events";

    /**
     * 前台上传文件路径
     */
    public static final String ONLINE_EVENTS_URL = "https://www.import-express.com/popProducts/postEventsFile";
    // public static final String ONLINE_EVENTS_URL = "http://127.0.0.1:8087/popProducts/postEventsFile";

    // test
//    private static final String LOCAL_JSON_PATH = "E:/data/cbtconsole/product/";
//    private static final String MONGODB_UPDATE_GOODS_URL_ONLINE = "http://192.168.1.153:8001/invokejob/b004";// 刷新产品表数据
//    private static final String MONGODB_UPDATE_SOLR_URL_ONLINE = "http://192.168.1.153:8001/invokejob/b006";// 刷新solr的

    /**
     * sku使用MQ更新AWS服务器数据
     *
     * @param pid
     * @param insertList
     */
    public static void updateCustomBenchmarkSkuNewByMq(String pid, List<CustomBenchmarkSkuNew> insertList) {
        List<String> mqSqlList = new ArrayList<>();
        StringBuffer mqSql;
        SkuValPO skuValPO;
        for (CustomBenchmarkSkuNew skuNew : insertList) {
            mqSql = new StringBuffer();
            skuValPO = skuNew.getSkuVal();
            mqSql.append(",wprice = '" + skuNew.getWprice() + "'")
                    .append(", act_sku_cal_price = " + skuValPO.getActSkuCalPrice())
                    .append(",act_sku_multi_currency_cal_price = " + skuValPO.getActSkuMultiCurrencyCalPrice())
                    .append(", act_sku_multi_currency_display_price = " + skuValPO.getActSkuMultiCurrencyDisplayPrice())
                    .append(",sku_cal_price = " + skuValPO.getSkuCalPrice())
                    .append(", sku_multi_currency_cal_price = " + skuValPO.getSkuMultiCurrencyCalPrice())
                    .append(",final_weight  = '" + skuNew.getFinalWeight() + "'")
                    .append(",sku_multi_currency_display_price = " + skuValPO.getActSkuMultiCurrencyDisplayPrice());
            mqSqlList.add("update custom_benchmark_sku set " + mqSql.toString().substring(1)
                    + " where pid = '" + pid + "' and sku_prop_ids='" + pid + "'");
        }
        for (String sql : mqSqlList) {
            NotifyToCustomerUtil.sendSqlByMq(sql);
        }
    }


    /**
     * MQ更新商品信息到AWS服务器
     *
     * @param bean
     */
    public static void publishToOnlineByMq(CustomGoodsPublish bean) {
        StringBuffer mqSql = new StringBuffer();
        mqSql.append(",keyword='" + bean.getKeyword() + "'")
                .append(",eninfo='" + checkAndReplaceQuotes(bean.getEninfo()) + "'")
                .append(",enname='" + checkAndReplaceQuotes(bean.getEnname()) + "'")
                .append(",weight='" + bean.getWeight() + "'")
                .append(",img='" + bean.getImg() + "'")
                .append(",endetail='" + checkAndReplaceQuotes(bean.getEndetail()) + "'")
                .append(",revise_weight='" + bean.getReviseWeight() + "'")
                .append(",final_weight='" + bean.getFinalWeight() + "'")
                .append(",price='" + bean.getPrice() + "'")
                .append(",wprice='" + bean.getWprice() + "'")
                .append(",range_price='" + bean.getRangePrice() + "'")
                .append(",sku='" + bean.getSku() + "'")
                .append(",cur_time=now(),bm_flag=1,goodsstate=4");

        if (bean.getIsEdited() == 1) {
            mqSql.append(",finalName='" + checkAndReplaceQuotes(bean.getEnname()) + "'");
        } else if (bean.getIsEdited() == 2) {
            mqSql.append(",finalName='" + checkAndReplaceQuotes(bean.getEnname()) + "',infoReviseFlag=1,priceReviseFlag=1");
        }
        if (StringUtils.isNotBlank(bean.getFeeprice())) {
            mqSql.append(",feeprice='" + bean.getFeeprice() + "'");
        }
        if (bean.getEninfo() == null || "".equals(bean.getEninfo()) || bean.getEninfo().length() < 10) {
            mqSql.append(",is_show_det_img_flag=0");
        } else {
            mqSql.append(",is_show_det_img_flag=1");
        }
        mqSql.append(",entype='" + bean.getEntype() + "',sellunit='" + bean.getSellUnit() + "'");
        mqSql.append(",ali_pid='" + bean.getAliGoodsPid() + "',ali_price='" + bean.getAliGoodsPrice()
                + "',matchSource='" + bean.getMatchSource() + "'");
        mqSql.append(" where pid='" + bean.getPid() + "'");
        NotifyToCustomerUtil.sendSqlByMq("update custom_benchmark_ready set valid = 1" + mqSql.toString());
    }


    /**
     * MongoDB更新商品信息到AWS服务器
     *
     * @param bean
     */
    public static boolean publishToOnlineByMongoDB(CustomGoodsPublish bean) {
        InputData inputData = new InputData('u'); //u表示更新；c表示创建，d表示删除

        bean.setEnname(FirstLetterUtitl.getNameNew(bean.getEnname(), bean.getCategoryName())); //对标题名字过短的进行拼接
        inputData.setEntype_new(bean.getEntypeNew());

        inputData.setKeyword(bean.getKeyword());
        inputData.setEninfo(checkAndReplaceQuotes(bean.getEninfo()));
        inputData.setEnname(checkAndReplaceQuotes(bean.getEnname()));
        inputData.setWeight(bean.getWeight());
        inputData.setImg(bean.getImg());
        inputData.setEndetail(checkAndReplaceQuotes(bean.getEndetail()));
        inputData.setRevise_weight(bean.getReviseWeight());
        inputData.setFinal_weight(bean.getFinalWeight());
        inputData.setPrice(bean.getPrice());
        inputData.setWprice(bean.getWprice());
        inputData.setRange_price(bean.getRangePrice());
        // inputData.setSku(bean.getSku());
        inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
        inputData.setBm_flag("1");
        inputData.setGoodsstate("4");
        if (bean.getIsEdited() == 1) {
            inputData.setFinalName(checkAndReplaceQuotes(bean.getEnname()));
        } else if (bean.getIsEdited() == 2) {
            inputData.setFinalName(checkAndReplaceQuotes(bean.getEnname()));
            inputData.setInfoReviseFlag("1");
            inputData.setPriceReviseFlag("1");
        }
        if (StringUtils.isNotBlank(bean.getFeeprice())) {
            inputData.setFeeprice(bean.getFeeprice());
        }
        if (bean.getEninfo() == null || "".equals(bean.getEninfo()) || bean.getEninfo().length() < 10) {
            inputData.setIs_show_det_img_flag("0");
        } else {
            inputData.setIs_show_det_img_flag("1");
        }
        inputData.setEntype(bean.getEntype());
        inputData.setSellunit(bean.getSellUnit());
        inputData.setAli_pid(bean.getAliGoodsPid());
        inputData.setAli_price(bean.getAliGoodsPrice());
        inputData.setMatchSource(String.valueOf(bean.getMatchSource()));
        inputData.setPid(bean.getPid());
        inputData.setValid("1");
        inputData.setGoodsstate("4");
        inputData.setFprice_str(bean.getFpriceStr());
        inputData.setSize_info_en(bean.getSizeInfoEn() == null ? "" : bean.getSizeInfoEn());
        inputData.setCustom_main_image(bean.getShowMainImage().replace(bean.getRemotpath(), ""));
        inputData.setRemotpath(bean.getRemotpath());
        inputData.setDescribe_good_flag(String.valueOf(bean.getDescribeGoodFlag()));
        //最终更新的json数据,json数据现在按照jack要求是写入文件，一条json数据对应一条语句 写在文件的一行，然后文件提供到jack
        return updateLocalAndSolr(inputData, 1);
    }

    /**
     * 更新库存标识到mongodb和solr
     *
     * @param pid
     */
    public static boolean stockToOnlineByMongoDB(String pid, String stock) {
        InputData inputData = new InputData('u'); //u表示更新；c表示创建，d表示删除
        inputData.setIs_stock_flag(stock);
        inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
        inputData.setPid(pid);
        //最终更新的json数据,json数据现在按照jack要求是写入文件，一条json数据对应一条语句 写在文件的一行，然后文件提供到jack
        return updateLocalAndSolr(inputData, 1);
    }

    /**
     * 更新库存标识到mongodb和solr
     *
     * @param pid
     */
    public static boolean videoUrlToOnlineByMongoDB(String pid, String path) {
        InputData inputData = new InputData('u'); //u表示更新；c表示创建，d表示删除
        inputData.setVideo_url(path);
        inputData.setCustom_video_flag(StringUtils.isNotBlank(path) ? "1" : "0");
        inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
        inputData.setPid(pid);
        //最终更新的json数据,json数据现在按照jack要求是写入文件，一条json数据对应一条语句 写在文件的一行，然后文件提供到jack
        return updateLocalAndSolr(inputData, 1);
    }


    /**
     * MQ批量下架AWS商品
     *
     * @param state
     * @param pids
     * @param adminid
     */
    public static void batchUpdateGoodsStateByMQ(int state, String pids, int adminid) {
        String[] pidList = pids.split(",");
        for (String pid : pidList) {
            if (StringUtils.isNotBlank(pid)) {
                String sql = "update custom_benchmark_ready set valid=" + (state == 4 ? 1 : 0) + ",goodsstate="
                        + state + ",cur_time = NOW() where pid = '" + pid + "'";
                NotifyToCustomerUtil.sendSqlByMq(sql);
            }
        }
    }


    /**
     * MongoDB批量下架AWS商品
     *
     * @param state
     * @param pids
     * @param adminid
     */
    public static boolean batchUpdateGoodsStateMongoDB(int state, String pids, int adminid) {
        int total = 0;
        int count = 0;
        InputData inputData;// u表示更新；c表示创建，d表示删除
        String[] pidList = pids.split(",");
        for (String pid : pidList) {
            if (StringUtils.isNotBlank(pid)) {
                total++;
                inputData = new InputData('u');
                inputData.setValid((state == 4 ? "1" : "0"));
                inputData.setGoodsstate(String.valueOf(4));
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
                inputData.setPid(pid);
                if (updateLocalAndSolr(inputData, 1)) {
                    count++;
                }
            }
        }
        return total == count;
    }


    public static void setGoodsValidByMq(String pid, int type) {
        String sql = "update custom_benchmark_ready set valid=" + (type == 1 ? 1 : 0) + ",goodsstate=" + (type == 1 ? 4 : 2);
        if (type != 1) {
            sql += ",unsellableReason = 6";
        }
        sql += ",cur_time=sysdate() where pid = '" + pid + "'";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    public static boolean setGoodsValidByMongoDb(String pid, int type) {
        InputData inputData = new InputData('u'); // u表示更新；c表示创建，d表示删除
        inputData.setValid((type == 1 ? "1" : "0"));
        inputData.setGoodsstate(type == 1 ? "4" : "2");
        inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
        inputData.setUnsellableReason("6");
        inputData.setPid(pid);
        return updateLocalAndSolr(inputData, 1);
    }

    public static boolean setGoodsValidByMongoDb2(String pid, int type) {
        InputData inputData = new InputData('u'); // u表示更新；c表示创建，d表示删除
        inputData.setValid((type == 1 ? "1" : "2"));
        inputData.setGoodsstate(type == 1 ? "4" : "2");
        inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
        inputData.setUnsellableReason("24");
        inputData.setPid(pid);
        return updateLocalAndSolr(inputData, 1);
    }

    public static boolean setNoBenchmarkingMongoDb(String pid) {
        // String updateSqlAws = "update custom_benchmark_ready set ali_weight='',bm_flag=2,isBenchmark=3 where pid = ?";
        InputData inputData = new InputData('u'); // u表示更新；c表示创建，d表示删除
        inputData.setAli_weight("0");
        inputData.setBm_flag("2");
        inputData.setIsBenchmark("3");
        inputData.setPid(pid);
        return updateLocalAndSolr(inputData, 0);
    }

    public static boolean setCustomerReadyMongoDb(String pid, String aliPid, String aliPrice, int bmFlag, int isBenchmark, String edName, String rwKeyword) {
        // String updateSqlAws = "update custom_benchmark_ready set ali_weight='',bm_flag=2,isBenchmark=3 where pid = ?";
        InputData inputData = new InputData('u'); // u表示更新；c表示创建，d表示删除
        inputData.setAli_pid(aliPid);
        inputData.setAli_price(aliPrice);
        inputData.setBm_flag(String.valueOf(bmFlag));
        inputData.setIsBenchmark(String.valueOf(isBenchmark));
        inputData.setFinalName(edName);
        inputData.setRw_keyword(rwKeyword);
        inputData.setPid(pid);
        return updateLocalAndSolr(inputData, 0);
    }


    /**
     * 替换字符串中的逗号
     *
     * @param str
     * @return
     */
    public static String checkAndReplaceQuotes(String str) {
        String tempStr = str;
        if (StringUtils.isNotBlank(str)) {
            if (str.contains("'")) {
                tempStr = str.replace("'", "\\'");
            }
            if (str.contains("\"")) {
                tempStr = str.replace("\"", "\\\"");
            }
        }
        return tempStr;
    }

    /**
     * 更新线上
     *
     * @param inputData
     * @param isSolr    0不更新solr  1更新solr
     * @return
     */
    public static boolean updateOnlineAndSolr(InputData inputData, int isSolr) {
        JsonResult json = new JsonResult();
        File file = null;
        try {
            file = writeToLocal(LOCAL_JSON_PATH + "/" + inputData.getPid() + "on004.json", JsonUtils.objectToJsonNotNull(inputData));
            if (file != null) {
                String result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_GOODS_URL_ONLINE, file);
                System.err.println("pid:" + inputData.getPid() + ",valid:" + inputData.getValid() + ",product online:["
                        + result.replace("\n", "") + "]");
                if (StringUtils.isBlank(result) || result.contains("NG") || result.contains("FAILED")) {
                    json.setOk(false);
                    json.setMessage("online执行调用mongodb更新产品接口失败");
                    System.err.println(inputData.getPid() + ",online执行调用mongodb更新产品接口失败");
                    logger.error(inputData.getPid() + ",online执行调用mongodb更新产品接口失败");
                } else {
                    file.delete();
                    json.setOk(true);
                    json.setData(result);
                    if (isSolr > 0 && result.contains("true")) {
                        file = writeToLocal(LOCAL_JSON_PATH + "/" + inputData.getPid() + "on006.json", result);
                        if (file != null) {
                            result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_SOLR_URL_ONLINE, file);
                            System.err.println("pid:" + inputData.getPid() + ",valid:" + inputData.getValid() + ",solr online:["
                                    + result.replace("\n", "") + "]");
                            if (StringUtils.isBlank(result) || result.contains("NG") || result.contains("FAILED")) {
                                json.setOk(false);
                                json.setMessage("online执行调用mongodb更新solr接口失败");
                                System.err.println(inputData.getPid() + ",online执行调用mongodb更新solr接口失败");
                                logger.error(inputData.getPid() + ",online执行调用mongodb更新solr接口失败");
                            } else {
                                json.setOk(true);
                                json.setData(result);
                            }
                        } else {
                            json.setOk(false);
                            json.setMessage("online solr生成json文件失败");
                            System.err.println(inputData.getPid() + ",online solr生成json文件失败");
                            logger.error(inputData.getPid() + ",online solr生成json文件失败");
                        }
                    }
                }
            } else {
                json.setOk(false);
                json.setMessage("online产品生成json文件失败");
                System.err.println(inputData.getPid() + ",online产品生成json文件失败");
                logger.error(inputData.getPid() + ",online产品生成json文件失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("pid:" + inputData.getPid() + ",updateOnlineAndSolr error:", e);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        return json.isOk();
    }


    /**
     * 本地更新
     *
     * @param inputData
     * @param isSolr    0不更新solr  1更新solr
     * @return
     */
    public static boolean updateLocalAndSolr(InputData inputData, int isSolr) {
        JsonResult json = new JsonResult();
        File file = null;
        try {
            if (inputData != null) {
                inputData.setFinalName(StrUtils.removeChineseCode(checkAndReplaceQuotes(inputData.getFinalName())));
                inputData.setEnname(StrUtils.removeChineseCode(checkAndReplaceQuotes(inputData.getEnname())));
                inputData.setWprice(StrUtils.removeSpecialCodeForWprice(inputData.getWprice()));
            }
            file = writeToLocal(LOCAL_JSON_PATH + "/" + inputData.getPid() + "lc004.json", JsonUtils.objectToJsonNotNull(inputData));
            if (file != null) {
                String result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_GOODS_URL_LOCAL, file);
                System.err.println("pid:" + inputData.getPid() + ",valid:" + inputData.getValid() + ",product local:["
                        + result.replace("\n", "") + "]");
                if (StringUtils.isBlank(result) || result.contains("NG") || result.contains("FAILED")) {
                    json.setOk(false);
                    json.setMessage("local执行调用mongodb更新产品接口失败");
                    System.err.println(inputData.getPid() + ",local执行调用mongodb更新产品接口失败");
                    logger.error(inputData.getPid() + ",local执行调用mongodb更新产品接口失败");
                } else {
                    file.delete();
                    json.setOk(true);
                    json.setData(result);
                    if (isSolr > 0 && result.contains("true")) {
                        file = writeToLocal(LOCAL_JSON_PATH + "/" + inputData.getPid() + "lc006.json", result);
                        if (file != null) {
                            result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_SOLR_URL_LOCAL, file);
                            System.err.println("pid:" + inputData.getPid() + ",valid:" + inputData.getValid() + ",solr local:["
                                    + result.replace("\n", "") + "]");
                            if (StringUtils.isBlank(result) || result.contains("NG") || result.contains("FAILED")) {
                                json.setOk(false);
                                json.setMessage("local执行调用mongodb更新solr接口失败");
                                System.err.println(inputData.getPid() + ",local执行调用mongodb更新solr接口失败");
                                logger.error(inputData.getPid() + ",local执行调用mongodb更新solr接口失败");
                            } else {
                                json.setOk(true);
                                json.setData(result);
                            }
                        } else {
                            json.setOk(false);
                            json.setMessage("local solr生成json文件失败");
                            System.err.println(inputData.getPid() + ",local solr生成json文件失败");
                            logger.error(inputData.getPid() + ",local solr生成json文件失败");
                        }
                    }
                }
            } else {
                json.setOk(false);
                json.setMessage("local产品生成json文件失败");
                System.err.println(inputData.getPid() + ",local产品生成json文件失败");
                logger.error(inputData.getPid() + ",local产品生成json文件失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("pid:" + inputData.getPid() + ",updateOnlineAndSolr error:", e);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        if (json.isOk()) {
            return updateOnlineAndSolr(inputData, isSolr);
        }
        return json.isOk();
    }


    private static List<String> genBatchUpdatePidList(List<String> pidList, int flag, Object objVal) {
        List<String> upList = new ArrayList<>();
        // 1是供应商打分,2是精品店铺
        String fieldName = "";
        if (flag == 1) {
            fieldName = "quality_avg";
        } else if (flag == 2) {
            fieldName = "shop_type";
        } else {
            return upList;
        }
        // 生成更新数据
        for (String pid : pidList) {
            StringBuffer pidBf = new StringBuffer();
            pidBf.append("{\"");

            pidBf.append("pid");
            pidBf.append("\":\"");
            pidBf.append(pid);
            pidBf.append("\",\"");

            pidBf.append(fieldName);
            pidBf.append("\":\"");
            pidBf.append(objVal);
            pidBf.append("\",\"");

            pidBf.append("cur_time");
            pidBf.append("\":\"");
            pidBf.append(DateFormatUtil.getWithSeconds(new Date()));
            pidBf.append("\",\"crud\":\"u\"}\\n");
            upList.add(pidBf.toString());
        }
        return upList;
    }

    /**
     * 批量设置mongodb中店铺的打分数据
     *
     * @param shopName                   : 店铺名称
     * @param pidList                    : 店铺下商品PID集合
     * @param "+String.valueOf(objVal)+" : 店铺分数
     * @param isSolr                     : 是否更新solr
     * @return
     */
    public static boolean batchSetGoodsShopScoreLocal(String shopName, List<String> pidList, int flag, Object objVal, int isSolr) {
        boolean isSuccess = false;
        File file = null;
        List<String> upList = new ArrayList<>();
        try {
            upList = genBatchUpdatePidList(pidList, flag, objVal);
            file = writeLinesToLocal(LOCAL_JSON_PATH + "/" +shopName + "lc004.json", upList);

            if (file != null) {
                String result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_GOODS_URL_LOCAL, file);
                System.err.println("shopName:" + shopName + ",pid size:" + pidList.size() + ",product local:[" + String.valueOf(objVal) + ":"
                        + objVal + "]");
                if (StringUtils.isBlank(result) || result.contains("FAILED")) {
                    System.err.println("shopName:" + shopName + ",local执行调用mongodb更新产品接口失败");
                    logger.error("shopName:" + shopName + ",local执行调用mongodb更新产品接口失败");
                    return isSuccess;
                } else {
                    file.delete();
                    isSuccess = true;
                    if (isSolr > 0 && result.contains("true")) {
                        file = writeToLocal(LOCAL_JSON_PATH + "/" + shopName + "lc006.json", result);
                        if (file != null) {
                            result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_SOLR_URL_LOCAL, file);
                            System.err.println("shopName:" + shopName + ",pid size:" + pidList.size() + ",solr local:[" + String.valueOf(objVal) + ":"
                                    + objVal + "]");
                            if (StringUtils.isBlank(result) || result.contains("FAILED")) {
                                isSuccess = false;
                                logger.error("shopName:" + shopName + ",local执行调用mongodb更新solr接口失败");
                            } else {
                                isSuccess = true;
                            }
                        } else {
                            isSuccess = false;
                            System.err.println("shopName:" + shopName + ",local solr生成json文件失败");
                            logger.error("shopName:" + shopName + ",local solr生成json文件失败");
                        }
                    }
                }
            } else {
                isSuccess = false;
                System.err.println("shopName:" + shopName + ",local产品生成json文件失败");
                logger.error("shopName:" + shopName + ",local产品生成json文件失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        if (isSuccess) {
            isSuccess = batchSetGoodsShopScoreOnline(shopName, upList, objVal, isSolr);
            upList.clear();
        }
        pidList.clear();
        return isSuccess;
    }


    /**
     * 批量设置mongodb中店铺的打分数据
     *
     * @param shopName : 店铺名称
     * @param upList   : 组合更新PID集合
     * @param objVal   : 更新值
     * @param isSolr   : 是否更新solr
     * @return
     */
    public static boolean batchSetGoodsShopScoreOnline(String shopName, List<String> upList, Object objVal, int isSolr) {
        boolean isSuccess = false;
        File file = null;
        try {
            file = writeLinesToLocal(LOCAL_JSON_PATH + "/" +shopName + "on004.json", upList);
            if (file != null) {
                String result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_GOODS_URL_ONLINE, file);
                System.err.println("shopName:" + shopName + ",pid size:" + upList.size() + ",product online:[ " + String.valueOf(objVal) + ":"
                        + "+String.valueOf(objVal)+" + "]");
                if (StringUtils.isBlank(result) || result.contains("FAILED")) {
                    System.err.println("shopName:" + shopName + ",online执行调用mongodb更新产品接口失败");
                    logger.error("shopName:" + shopName + ",online执行调用mongodb更新产品接口失败");
                    return isSuccess;
                } else {
                    file.delete();
                    isSuccess = true;
                    if (isSolr > 0 && result.contains("true")) {
                        file = writeToLocal(LOCAL_JSON_PATH + "/" + shopName + "on006.json", result);
                        if (file != null) {
                            result = okHttpUtils.postFileNoParam("file", MONGODB_UPDATE_SOLR_URL_ONLINE, file);
                            System.err.println("shopName:" + shopName + ",pid size:" + upList.size() + ",solr online:[ " + String.valueOf(objVal) + ":"
                                    + "+String.valueOf(objVal)+" + "]");
                            if (StringUtils.isBlank(result) || result.contains("FAILED")) {
                                isSuccess = false;
                                logger.error("shopName:" + shopName + ",online执行调用mongodb更新solr接口失败");
                            } else {
                                isSuccess = true;
                            }
                        } else {
                            isSuccess = false;
                            System.err.println("shopName:" + shopName + ",online solr生成json文件失败");
                            logger.error("shopName:" + shopName + ",online solr生成json文件失败");
                        }
                    }
                }
            } else {
                isSuccess = false;
                System.err.println("shopName:" + shopName + ",online产品生成json文件失败");
                logger.error("shopName:" + shopName + ",online产品生成json文件失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        return isSuccess;
    }

    /**
     * 更新详情数据
     *
     * @param gd
     */
    public static boolean updatePidEnInfo(CustomGoodsPublish gd) {
        InputData inputData = new InputData('u');
        inputData.setEninfo(gd.getEninfo());
        inputData.setPid(gd.getPid());
        return updateLocalAndSolr(inputData, 0);
    }

    /**
     * 更新mongodb的体积重量数据
     * @param pid
     * @param newWeight
     * @return
     */
    public static boolean updateVolumeWeight(String pid, String newWeight){
        InputData inputData = new InputData('u');
        inputData.setVolume_weight(newWeight);
        inputData.setPid(pid);
        return updateLocalAndSolr(inputData, 0);
    }


    /**
     * 写入mongodb数据大本地
     * @param fileName
     * @param json
     * @return
     * @throws Exception
     */
    private static File writeToLocal(String fileName, String json) throws Exception {
        String tempJson = json;
        if (tempJson.contains("\\\\\\")) {
            tempJson = tempJson.replace("\\\\\\", "\\");
        }
        if (tempJson.contains("\\\\'")) {
            tempJson = tempJson.replace("\\\\'", "'");
        }
        File file = new File(fileName);
        // FileHelper.writeFile(file, tempJson);
        FileUtils.write(file, tempJson, "utf-8");
        file = new File(fileName);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }


    /**
     * 写入数据到本地
     * @param fileName
     * @param json
     * @return
     * @throws Exception
     */
    public static File writeDataToLocal(String fileName, String json) throws Exception {
        String timeStr = String.valueOf(System.currentTimeMillis()) + "_" ;
        File parentfile = new File(LOCAL_EVENTS_PATH);
        if(!(parentfile.exists() && parentfile.isDirectory())){
            parentfile.mkdirs();
        }
        FileUtils.write(new File(LOCAL_EVENTS_PATH + "/" + timeStr + fileName), json, "utf-8");
        File file = new File(LOCAL_EVENTS_PATH + "/" + timeStr + fileName);
        if (file.exists()) {
            return file;
        } else {
            throw new Exception("生成文件异常");
        }
    }

    /**
     * 写入List集合数据到本地
     * @param fileName
     * @param list
     * @return
     * @throws Exception
     */
    private static File writeLinesToLocal(String fileName, List<String> list) throws Exception {
        File file = new File(fileName);
        FileUtils.writeLines(file, list, true);
        file = new File(fileName);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        String[] local = new String[]{
                "45329511937", "1118853261", "1003032520", "544553893961", "575982118149", "569067241849", "567821235887", "565773384302",
                "560079033036", "558393074423", "557837286871", "555062906660", "555049880615", "551356710597", "548976278672",
                "546299010610", "545407012794", "544582407985", "542283271867", "542139063056", "541995292522", "539032382464",
                "531917117383", "528633204617", "528310780177", "528041842236", "527671329725", "526324114141", "524390322021",
                "523804329708", "523157042235", "45106745554", "45089230356", "42191464650", "38651968713", "38638625749", "38274133867",
                "36747868463", "525408485750", "39189639792", "579985489094", "557647910424", "554601959921", "544367479217", "527595694146",
                "550454355219", "546873071729", "543733277485", "44068557604", "530595587751", "579122855406", "577946040930", "577886397814",
                "575262056264", "570628741684", "569351510249", "569206647311", "567614011474", "566788517698", "566530357138", "565031351794",
                "563302730217", "563255483205", "562594355170", "562452082382", "560607261066", "558679010041", "558244925899", "557212151840",
                "555596778303", "554886125828", "553171648185", "551220864539", "549479071492", "548216415688", "546194794811", "545337845778",
                "544527940498", "544265698348", "539646506491", "537512034983", "534028975506", "532751074703", "530639570514", "528615880917",
                "527092392233", "523953225132", "523933858609", "522960273003", "520266025443", "43221899326", "38547001175", "1281146336",};
        for (String l : local) {
            InputData inputData = new InputData('u');
            inputData.setPid(l);
            inputData.setCustom_video_flag("1");
            inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
            GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1);

        }
    }
}
