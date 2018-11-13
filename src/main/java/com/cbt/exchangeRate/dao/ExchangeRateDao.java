package com.cbt.exchangeRate.dao;

import com.cbt.bean.ExchangeRateDaily;

import java.util.List;
import java.util.Map;

public interface ExchangeRateDao {

    int updateRate(List<Map<String, String>> list);

    List<Map<String, Object>> getExchangeRate();


    /**
     * 根据时间查询汇率信息
     *
     * @param year
     * @param month
     * @param start
     * @param rows
     * @return
     */
    List<ExchangeRateDaily> queryExchangeRateByDate(int year, int month, int start, int rows);

    /**
     * 根据时间查询汇率信息个数
     * @param year
     * @param month
     * @return
     */
    int queryExchangeRateByDateCount(int year, int month);

}
