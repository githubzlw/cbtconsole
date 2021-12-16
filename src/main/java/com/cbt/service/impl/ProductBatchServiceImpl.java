package com.cbt.service.impl;

import com.cbt.service.ProductBatchService;
import com.importExpress.mapper.ProductBatchMapper;
import com.importExpress.pojo.ProductBatchDiscount;
import com.importExpress.pojo.ProductBatchDiscountParam;
import com.importExpress.utli.OKHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductBatchServiceImpl implements ProductBatchService {

    private OKHttpUtils okHttpUtils = new OKHttpUtils();

    @Autowired
    private ProductBatchMapper productBatchMapper;

    @Override
    public List<ProductBatchDiscount> list(ProductBatchDiscountParam param) {
        return productBatchMapper.list(param);
    }

    @Override
    public List<ProductBatchDiscount> listByPid(List<String> pidList) {
        return productBatchMapper.listByPid(pidList);
    }

    @Override
    public int listCount(ProductBatchDiscountParam param) {
        return productBatchMapper.listCount(param);
    }

    @Override
    public int insert(ProductBatchDiscount discount) {
        return productBatchMapper.insert(discount);
    }

    @Override
    public int insertBatch(List<ProductBatchDiscount> discountList) {
        return productBatchMapper.insertBatch(discountList);
    }

    @Override
    public int update(ProductBatchDiscount discount) {
        return productBatchMapper.update(discount);
    }

    @Override
    public int delete(List<String> pidList, List<String> skuIdList) {
        return productBatchMapper.delete(pidList, skuIdList);
    }

    @Override
    @Async
    public void asyncGet(String url) {
        try {
            TimeUnit.SECONDS.sleep(10);
            System.err.println("--------" + url);
            okHttpUtils.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
