package com.importExpress.utli;

import com.cbt.util.BigDecimalUtil;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.utli
 * @date:2020-11-11
 */
public class B2CPriceUtil {

    public static final float CURRENT_RATE = 6.5f;


    public static double changeToB2BPrice(double factoryPrice, double addPriceLv, double initialFreight) {
        return BigDecimalUtil.truncateDouble(factoryPrice / CURRENT_RATE * addPriceLv + initialFreight, 2);
    }

    public static String changeToB2BPriceString(double factoryPrice, double addPriceLv, double initialFreight) {
        return BigDecimalUtil.truncateDoubleString(factoryPrice / CURRENT_RATE * addPriceLv + initialFreight, 2);
    }


    /**
     * (1688产品单价【wholesale_price】 *1.4+5+0.042*max(重量，体积重量))/6.6
     *
     * @param costPrice
     * @param finalWeigth
     * @param volumeWeight
     * @return
     */
    public static String getFreePriceStr(float costPrice, String finalWeigth, String volumeWeight) {
        double tempNUm = ((costPrice * 1.6 + 5 + 0.042 * 1000 * Math.max(Float.parseFloat(finalWeigth), Float.parseFloat(volumeWeight))) / CURRENT_RATE);
        return BigDecimalUtil.truncateDoubleString(tempNUm, 2);
    }

    public static double getFreePrice(float costPrice, String finalWeigth, String volumeWeight) {

        double tempNUm = ((costPrice * 1.6 + 5 + 0.042 * 1000 * Math.max(Float.parseFloat(finalWeigth), Float.parseFloat(volumeWeight))) / CURRENT_RATE);
        return BigDecimalUtil.truncateDouble(tempNUm, 2);
    }
}
