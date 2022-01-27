package com.importExpress.service.impl;

import com.importExpress.mapper.AmazonProductMapper;
import com.importExpress.pojo.AmazonOrderBean;
import com.importExpress.pojo.AmazonOrderParam;
import com.importExpress.pojo.AmazonProductBean;
import com.importExpress.service.AmazonProductService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<AmazonOrderBean> queryAmazonOrderList(AmazonOrderParam param) {
        return amazonProductMapper.queryAmazonOrderList(param);
    }

    @Override
    public int queryAmazonOrderListCount(AmazonOrderParam param) {
        return amazonProductMapper.queryAmazonOrderListCount(param);
    }

    @Override
    public int insertAmazonOrder(AmazonOrderBean orderBean) {
        return amazonProductMapper.insertAmazonOrder(orderBean);
    }

    @Override
    public int insertAmazonOrderList(List<AmazonOrderBean> orderBeanList) {
        int insertNum = 0;
        if (CollectionUtils.isNotEmpty(orderBeanList)) {
            int num = 800;
            if (orderBeanList.size() > num) {
                int total = orderBeanList.size() / num;
                if (orderBeanList.size() % num > 0) {
                    total++;
                }
                for (int j = 1; j <= total; j++) {
                    List<AmazonOrderBean> list = orderBeanList.stream().skip((j-1) * num).limit(num).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(list)) {
                        insertNum += amazonProductMapper.insertAmazonOrderList(list);
                    }
                }
            }
        }
        return insertNum;
    }
}
