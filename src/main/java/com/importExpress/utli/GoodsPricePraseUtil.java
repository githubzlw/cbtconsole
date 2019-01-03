package com.importExpress.utli;

import com.cbt.util.StrUtils;
import com.importExpress.pojo.ImportProductBean;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class GoodsPricePraseUtil {

    public static void prasePrice(ImportProductBean goods) {
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
}
