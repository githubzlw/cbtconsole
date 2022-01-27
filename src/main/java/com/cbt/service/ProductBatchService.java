package com.cbt.service;

import com.importExpress.pojo.ProductBatchDiscount;
import com.importExpress.pojo.ProductBatchDiscountParam;

import java.util.List;

public interface ProductBatchService {

    List<ProductBatchDiscount> list(ProductBatchDiscountParam param);

    List<ProductBatchDiscount> listByPid(List<String> pidList);

    int listCount(ProductBatchDiscountParam param);

    int insert(ProductBatchDiscount discount);

    int insertBatch(List<ProductBatchDiscount> discountList);

    int update(ProductBatchDiscount discount);

    int delete(List<String> pidList, List<String> skuIdList);

    public void asyncGet(String url);


}
