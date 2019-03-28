package com.importExpress.service;

import com.importExpress.pojo.CustomBenchmarkSku;
import com.importExpress.pojo.CustomBenchmarkSkuExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomBenchmarkService {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int countByExample(CustomBenchmarkSkuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int deleteByExample(CustomBenchmarkSkuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int insert(CustomBenchmarkSku record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int insertSelective(CustomBenchmarkSku record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    List<CustomBenchmarkSku> selectByExample(CustomBenchmarkSkuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    CustomBenchmarkSku selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int updateByExampleSelective(@Param("record") CustomBenchmarkSku record, @Param("example") CustomBenchmarkSkuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int updateByExample(@Param("record") CustomBenchmarkSku record, @Param("example") CustomBenchmarkSkuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int updateByPrimaryKeySelective(CustomBenchmarkSku record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table custom_benchmark_sku
     *
     * @mbggenerated Wed Mar 27 17:50:42 CST 2019
     */
    int updateByPrimaryKey(CustomBenchmarkSku record);

    String selectTypeNameBySkuId(String skuId);
}
