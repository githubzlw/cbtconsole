package com.importExpress.utli;

import com.cbt.bean.CustomGoodsPublish;
import com.importExpress.pojo.CustomBenchmarkSkuNew;
import com.importExpress.pojo.SkuValPO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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
        mqSql.append(",keyword='" + bean.getKeyword() + "')" +
                ".append(,eninfo='" + checkAndReplaceQuotes(bean.getEninfo()) + "')" +
                ".append(,enname='" + checkAndReplaceQuotes(bean.getEnname()) + "')" +
                ".append(,weight='" + bean.getWeight() + "')" +
                ".append(,img='" + bean.getImg() + "')" +
                ".append(,endetail='" + checkAndReplaceQuotes(bean.getEndetail()) + "')" +
                ".append(,revise_weight='" + bean.getReviseWeight() + "')" +
                ".append(,final_weight='" + bean.getFinalWeight() + "')" +
                ".append(,price='" + bean.getPrice() + "')" +
                ".append(,wprice='" + bean.getWprice() + "')" +
                ".append(,range_price='" + bean.getRangePrice() + "')" +
                ".append(,sku='" + bean.getSku() + "')" +
                ".append(),cur_time=now(),bm_flag=1,goodsstate=4");

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
     * 批量下架AWS商品
     *
     * @param state
     * @param pids
     * @param adminid
     */
    public static void batchUpdateGoodsState(int state, String pids, int adminid) {
        String[] pidList = pids.split(",");
        for (String pid : pidList) {
            if (StringUtils.isNotBlank(pid)) {
                String sql = "update custom_benchmark_ready set valid=" + (state == 4 ? 1 : 0) + ",goodsstate="
                        + state + ",cur_time = NOW() where pid = '" + pid + "'";
                NotifyToCustomerUtil.sendSqlByMq(sql);
            }
        }
    }


    public void setGoodsValid(String pid, int type) {
        String sql = "update custom_benchmark_ready set valid=" + (type == 1 ? 1 : 0) + ",goodsstate=" + (type == 1 ? 4 : 2);
        if (type != 1) {
            sql += ",unsellableReason = 6";
        }
        sql += ",cur_time=sysdate() where pid = '" + pid + "'";
        NotifyToCustomerUtil.sendSqlByMq(sql);
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

}
