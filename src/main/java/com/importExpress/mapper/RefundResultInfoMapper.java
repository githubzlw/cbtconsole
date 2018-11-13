package com.importExpress.mapper;

import com.importExpress.pojo.RefundResultInfo;
import com.importExpress.pojo.RefundResultInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RefundResultInfoMapper {
    int countByExample(RefundResultInfoExample example);

    int deleteByExample(RefundResultInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RefundResultInfo record);

    int insertSelective(RefundResultInfo record);

    List<RefundResultInfo> selectByExampleWithBLOBs(RefundResultInfoExample example);

    List<RefundResultInfo> selectByExample(RefundResultInfoExample example);

    RefundResultInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RefundResultInfo record, @Param("example") RefundResultInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") RefundResultInfo record, @Param("example") RefundResultInfoExample example);

    int updateByExample(@Param("record") RefundResultInfo record, @Param("example") RefundResultInfoExample example);

    int updateByPrimaryKeySelective(RefundResultInfo record);

    int updateByPrimaryKeyWithBLOBs(RefundResultInfo record);

    int updateByPrimaryKey(RefundResultInfo record);
}