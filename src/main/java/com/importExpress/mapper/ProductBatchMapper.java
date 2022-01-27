package com.importExpress.mapper;


import com.importExpress.pojo.ProductBatchDiscount;
import com.importExpress.pojo.ProductBatchDiscountParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductBatchMapper {


    List<ProductBatchDiscount> list(ProductBatchDiscountParam param);

    List<ProductBatchDiscount> listByPid(@Param("list") List<String> pidList);

    int listCount(ProductBatchDiscountParam param);

    int insert(ProductBatchDiscount discount);

    int insertBatch(@Param("list") List<ProductBatchDiscount> discountList);

    int update(ProductBatchDiscount discount);

    int delete(@Param("pidList") List<String> pidList, @Param("skuIdList") List<String> skuIdList);

}
