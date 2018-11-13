package com.importExpress.mapper;

import com.importExpress.pojo.DShippUser;
import com.importExpress.pojo.DropShippApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DropShippApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DropShippApply record);

    int insertSelective(DropShippApply record);

    DropShippApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DropShippApply record);

    int updateByPrimaryKey(DropShippApply record);
   
    List<DShippUser> findAllDropShip(@Param("userCategory") String userCategory, int start, int end);

    int dropShiptotal(@Param("userCategory") String userCategory, int start, int end);
}