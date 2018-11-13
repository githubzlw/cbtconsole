package com.cbt.systemcode.dao;

import com.cbt.pojo.SystemCode;
import com.cbt.pojo.SystemCodeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemCodeMapper {
    int countByExample(SystemCodeExample example);

    int deleteByExample(SystemCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemCode record);

    int insertSelective(SystemCode record);

    List<SystemCode> selectByExample(SystemCodeExample example);

    SystemCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SystemCode record, @Param("example") SystemCodeExample example);

    int updateByExample(@Param("record") SystemCode record, @Param("example") SystemCodeExample example);

    int updateByPrimaryKeySelective(SystemCode record);

    int updateByPrimaryKey(SystemCode record);
}