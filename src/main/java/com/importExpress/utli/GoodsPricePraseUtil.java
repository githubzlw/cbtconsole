package com.importExpress.utli;

import com.cbt.util.BigDecimalUtil;
import com.cbt.util.StrUtils;
import com.importExpress.pojo.ImportProductBean;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsPricePraseUtil {

    public static void praseExpressPrice(ImportProductBean goods) {
        String range_price = StrUtils.object2Str(goods.getRangePrice());
        String maxPrice = "";
        if (StringUtils.isBlank(range_price) || range_price.trim().length() == 0) {
            if (goods.getIsSoldFlag() > 0) {
                if (StringUtils.isNotBlank(goods.getFeeprice())) {
                    List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",
                            StrUtils.object2Str(goods.getFeeprice()));
                    if (matchStrList != null && !matchStrList.isEmpty()) {
                        range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                        if (matchStrList.size() > 1) {
                            maxPrice = StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                            range_price = range_price + "-" + maxPrice;
                        } else {
                            maxPrice = range_price;
                        }
                    } else {
                        range_price = StrUtils.object2Str(goods.getPrice());
                        maxPrice = range_price;
                    }
                }
            } else {
                List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)", StrUtils.object2Str(goods.getWprice()));
                if (matchStrList != null && !matchStrList.isEmpty()) {
                    range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                    maxPrice = range_price;
                    if (matchStrList.size() > 1) {
                        maxPrice = StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                        range_price = range_price + "-" + maxPrice;
                    } else {
                        maxPrice = range_price;
                    }
                } else {
                    range_price = StrUtils.object2Str(goods.getPrice());
                    maxPrice = range_price;
                }
            }
        } else {
            if (range_price.contains("-")) {
                maxPrice = range_price.split("-")[1].trim();
            } else {
                maxPrice = range_price;
            }
        }
        if ("".equals(maxPrice)) {
            System.err.println(maxPrice);
        }
        goods.setShowPrice(range_price);
    }


    public static void prase1688Goods(List<ImportProductBean> goodsList) {
        for (ImportProductBean gd : goodsList) {
            // 读取价格数据
            if (StringUtils.isNotBlank(gd.getPrice())) {
                // 计算价格
                List<String> priceStrList = new ArrayList<String>();
                String price = gd.getPrice().replaceAll("\\n", "");
                String[] priceStrs = price.split("起批量");
                priceStrList = StrUtils.matchStrList("(\\d+\\.\\d{1,2})", priceStrs[0]);
                String minPrice = "0";
                String maxPrice = "0";
                if (!priceStrList.isEmpty()) {
//                    String price_1688 = priceStrList.get(0);
//                    double factoryPrice = Double.valueOf(price_1688.split("-")[0]) / StrUtils.EXCHANGE_RATE;
//                    if (factoryPrice < 0.01) {
//                        factoryPrice = Double.valueOf(price_1688.split("-")[0]);
//                    }
//                    gd.setShowPrice(String.valueOf(factoryPrice));
                    for (String tempStr : priceStrList) {
                        if ("0".equals(minPrice) || Double.valueOf(tempStr) < Double.valueOf(minPrice)) {
                            minPrice = tempStr;
                        }
                        if (Double.valueOf(tempStr) > Double.valueOf(maxPrice)) {
                            maxPrice = tempStr;
                        }
                    }

                    String changeMinPrice = String.valueOf(BigDecimalUtil.truncateDouble(Double.valueOf(minPrice) / StrUtils.EXCHANGE_RATE, 2));
                    String changeMaxPrice = String.valueOf(BigDecimalUtil.truncateDouble(Double.valueOf(maxPrice) / StrUtils.EXCHANGE_RATE, 2));
                    if ("0".equals(minPrice) && "0".equals(maxPrice)) {
                        gd.setShowPrice("--");
                    } else if ("0".equals(minPrice)) {
                        gd.setShowPrice(changeMaxPrice);
                    } else if ("0".equals(maxPrice)) {
                        gd.setShowPrice(changeMinPrice);
                    } else if (minPrice.equals(maxPrice)) {
                        gd.setShowPrice(changeMinPrice);
                    } else {
                        gd.setShowPrice(changeMinPrice + "-" + changeMaxPrice);
                    }
                }
                priceStrList.clear();
            }
        }
    }
}
