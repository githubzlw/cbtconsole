package com.importExpress.service.impl;

import com.importExpress.mapper.AmazonProductMapper;
import com.importExpress.pojo.AmazonProductBean;
import com.importExpress.service.AmazonProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmazonProductServiceImpl implements AmazonProductService {

    @Autowired
    private AmazonProductMapper amazonProductMapper;


    @Override
    public List<AmazonProductBean> queryForList(AmazonProductBean amazonProduct) {
        return amazonProductMapper.queryForList(amazonProduct);
    }

    @Override
    public int queryForListCount(AmazonProductBean amazonProduct) {
        return amazonProductMapper.queryForListCount(amazonProduct);
    }

    @Override
    public int setAmazonDealFlag(String amazonPid, int dealState, int adminId, String matchPid) {
        return amazonProductMapper.setAmazonDealFlag(amazonPid, dealState, adminId, matchPid);
    }

    @Override
    public int checkAmazonProductIsExists(String amazonPid) {
        return amazonProductMapper.checkAmazonProductIsExists(amazonPid);
    }
}
