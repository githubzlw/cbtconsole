package com.importExpress.service.impl;

import com.importExpress.mapper.CustomBenchmarkSkuMapper;
import com.importExpress.pojo.CustomBenchmarkSku;
import com.importExpress.pojo.CustomBenchmarkSkuExample;
import com.importExpress.service.CustomBenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * *****************************************************************************************
 *
 * @ClassName CustomBenchmarkServiceImpl
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2019/3/27 17:53:42
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       17:53:422019/3/27     cjc                       初版
 * ******************************************************************************************
 */
@Service
public class CustomBenchmarkServiceImpl implements CustomBenchmarkService {
    @Autowired
    private CustomBenchmarkSkuMapper custombenchmarkskumapper;
    @Override
    public int countByExample(CustomBenchmarkSkuExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(CustomBenchmarkSkuExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(CustomBenchmarkSku record) {
        return 0;
    }

    @Override
    public int insertSelective(CustomBenchmarkSku record) {
        return 0;
    }

    @Override
    public List<CustomBenchmarkSku> selectByExample(CustomBenchmarkSkuExample example) {
        return custombenchmarkskumapper.selectByExample(example);
    }

    @Override
    public CustomBenchmarkSku selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(CustomBenchmarkSku record, CustomBenchmarkSkuExample example) {
        return 0;
    }

    @Override
    public int updateByExample(CustomBenchmarkSku record, CustomBenchmarkSkuExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(CustomBenchmarkSku record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(CustomBenchmarkSku record) {
        return 0;
    }
    @Override
    public String selectTypeNameBySkuId(String skuId) {
        return custombenchmarkskumapper.selectTypeNameBySkuId(skuId);
    }
}
