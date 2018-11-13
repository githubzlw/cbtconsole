package com.cbt.util;

import java.math.BigDecimal;

/**
 * 数值精度截取工具类
 */
public class BigDecimalUtil {


    /**
     * 截取float类型的数据
     *
     * @param floatVal    : 原值
     * @param truncateNum : 截取位数
     * @return
     */
    public static float truncateFloat(float floatVal, int truncateNum) {
        float resultVal = floatVal;
        if (truncateNum > 0) {
            BigDecimal bigDecimal = new BigDecimal(floatVal);
            resultVal = bigDecimal.setScale(truncateNum, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return resultVal;
    }


    /**
     * @param doubleVal   : 原值
     * @param truncateNum : 截取位数
     * @return
     */
    public static double truncateDouble(double doubleVal, int truncateNum) {
        double resultVal = doubleVal;
        if (truncateNum > 0) {
            BigDecimal bigDecimal = new BigDecimal(doubleVal);
            resultVal = bigDecimal.setScale(truncateNum, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return resultVal;
    }


}
