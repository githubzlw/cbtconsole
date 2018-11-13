package com.cbt.exchangeRate.service.impl;

import com.cbt.bean.ExchangeRateDaily;
import com.cbt.exchangeRate.dao.ExchangeRateDao;
import com.cbt.exchangeRate.dao.ExchangeRateDaoImpl;
import com.cbt.exchangeRate.service.ExchangeRateService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();


    @Override
    public Map<String, Double> updateRate(List<Map<String, String>> list) {
        // TODO Auto-generated method stub

        int ret = exchangeRateDao.updateRate(list);
        //更新成功后再次刷新session存储的汇率数据
        Map<String, Double> map = new HashMap<String, Double>();
        if (ret > 1) {
            List<Map<String, Object>> rateList = exchangeRateDao.getExchangeRate();
            for (Map<String, Object> m : rateList) {
                map.put((String) m.get("country"), Double.valueOf(String.valueOf(m.get("exchange_rate"))));
            }
            return map;
        }
        return null;
    }

    @Override
    public List<ExchangeRateDaily> queryExchangeRateByDate(int year, int month, int start, int rows) {
        return exchangeRateDao.queryExchangeRateByDate(year, month, start, rows);
    }

    @Override
    public int queryExchangeRateByDateCount(int year, int month) {
        return exchangeRateDao.queryExchangeRateByDateCount(year, month);
    }

}
