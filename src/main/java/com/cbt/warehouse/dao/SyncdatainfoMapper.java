package com.cbt.warehouse.dao;

import com.cbt.pojo.Syncdatainfo;
import com.cbt.pojo.SyncdatainfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SyncdatainfoMapper {
    int countByExample(SyncdatainfoExample example);

    int deleteByExample(SyncdatainfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Syncdatainfo record);

    int insertSelective(Syncdatainfo record);

    List<Syncdatainfo> selectByExample(SyncdatainfoExample example);

    Syncdatainfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Syncdatainfo record, @Param("example") SyncdatainfoExample example);

    int updateByExample(@Param("record") Syncdatainfo record, @Param("example") SyncdatainfoExample example);

    int updateByPrimaryKeySelective(Syncdatainfo record);

    int updateByPrimaryKey(Syncdatainfo record);
}