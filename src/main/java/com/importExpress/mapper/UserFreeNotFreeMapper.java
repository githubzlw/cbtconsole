package com.importExpress.mapper;

import com.importExpress.pojo.UserFreeNotFree;
import com.importExpress.pojo.UserFreeNotFreeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFreeNotFreeMapper {
    int countByExample(UserFreeNotFreeExample example);

    int deleteByExample(UserFreeNotFreeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserFreeNotFree record);

    int insertSelective(UserFreeNotFree record);

    List<UserFreeNotFree> selectByExample(UserFreeNotFreeExample example);

    UserFreeNotFree selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserFreeNotFree record, @Param("example") UserFreeNotFreeExample example);

    int updateByExample(@Param("record") UserFreeNotFree record, @Param("example") UserFreeNotFreeExample example);

    int updateByPrimaryKeySelective(UserFreeNotFree record);

    int updateByPrimaryKey(UserFreeNotFree record);

    List<UserFreeNotFree> selectAll();
}