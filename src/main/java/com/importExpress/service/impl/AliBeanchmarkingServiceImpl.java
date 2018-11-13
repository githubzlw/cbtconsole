package com.importExpress.service.impl;

import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.importExpress.mapper.AliBeanchmarkingMapper;
import com.importExpress.pojo.AliBenchmarkingBean;
import com.importExpress.pojo.AliBenchmarkingStatistic;
import com.importExpress.pojo.KeyWordBean;
import com.importExpress.service.AliBeanchmarkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliBeanchmarkingServiceImpl implements AliBeanchmarkingService {
    @Autowired
    private AliBeanchmarkingMapper bmMapper;

    private CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();


    @Override
    public List<AliBenchmarkingBean> queryAliBenchmarkingForList(AliBenchmarkingBean benchmarkingBean) {
        return bmMapper.queryAliBenchmarkingForList(benchmarkingBean);
    }

    @Override
    public int queryAliBenchmarkingForListCount(AliBenchmarkingBean benchmarkingBean) {
        return bmMapper.queryAliBenchmarkingForListCount(benchmarkingBean);
    }

    @Override
    public List<AliBenchmarkingStatistic> queryAliBenchmarkingStatistic(int adminId, String beginDate, String endDate, String admName) {
        return bmMapper.queryAliBenchmarkingStatistic(adminId, beginDate, endDate, admName);
    }

    @Override
    public List<KeyWordBean> queryKeyWordListByAdminId(int adminId, int startNum, int limitNum) {
        return bmMapper.queryKeyWordListByAdminId(adminId, startNum, limitNum);
    }

    @Override
    public int queryKeyWordListByAdminIdCount(int adminId) {
        return bmMapper.queryKeyWordListByAdminIdCount(adminId);
    }

    @Override
    public boolean updateGoodsFlag(String pid, int moq, String rangePrice, String priceContent, int isSoldFlag, int benchmarkingFlag) {
        return customGoodsDao.updateGoodsFlag(pid, moq, rangePrice, priceContent, isSoldFlag, benchmarkingFlag);
    }
}
