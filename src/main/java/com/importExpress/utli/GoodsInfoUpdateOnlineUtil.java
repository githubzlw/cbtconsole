package com.importExpress.utli;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.util.DateFormatUtil;
import com.importExpress.pojo.CustomBenchmarkSkuNew;
import com.importExpress.pojo.InputData;
import com.importExpress.pojo.SkuValPO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodsInfoUpdateOnlineUtil {

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
    public static void publishToOnlineByMongoDB(CustomGoodsPublish bean) {
        InputData inputData = new InputData('u'); //u表示更新；c表示创建，d表示删除

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
        //最终更新的json数据,json数据现在按照jack要求是写入文件，一条json数据对应一条语句 写在文件的一行，然后文件提供到jack
        String json = JsonUtils.objectToJsonNotNull(inputData);
        System.err.println(json);
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
    public static void batchUpdateGoodsStateMongoDB(int state, String pids, int adminid) {
        InputData inputData;// u表示更新；c表示创建，d表示删除
        String[] pidList = pids.split(",");
        for (String pid : pidList) {
            if (StringUtils.isNotBlank(pid)) {
                inputData = new InputData('u');
                inputData.setValid((state == 4 ? "1" : "0"));
                inputData.setGoodsstate(String.valueOf(4));
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
                inputData.setPid(pid);
                String json = JsonUtils.objectToJsonNotNull(inputData);
                System.err.println(json);
            }
        }
    }


    public static void setGoodsValidByMq(String pid, int type) {
        String sql = "update custom_benchmark_ready set valid=" + (type == 1 ? 1 : 0) + ",goodsstate=" + (type == 1 ? 4 : 2);
        if (type != 1) {
            sql += ",unsellableReason = 6";
        }
        sql += ",cur_time=sysdate() where pid = '" + pid + "'";
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }


    public static void setGoodsValidByMongoDb(String pid, int type) {
        InputData inputData = new InputData('u'); // u表示更新；c表示创建，d表示删除
                inputData.setValid((type == 1 ? "1" : "0"));
                inputData.setGoodsstate(type == 1 ? "4" : "2");
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
                inputData.setUnsellableReason("6");
                inputData.setPid(pid);
                String json = JsonUtils.objectToJsonNotNull(inputData);
                System.err.println(json);
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



    public static boolean updateOnlineAndSolr(InputData inputData,int isSolr){

        try{
            String json = JsonUtils.objectToJsonNotNull(inputData);
            System.err.println(json);

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

}
