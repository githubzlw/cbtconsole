package com.importExpress.service;

import com.importExpress.pojo.UserFreeNotFree;
import com.importExpress.pojo.UserFreeNotFreeExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface UserFreeNotFreeService {
    int countByExample(UserFreeNotFreeExample example);

    int deleteByExample(UserFreeNotFreeExample example);

    int deleteByPrimaryKey(Integer id);

    @Async
    int insert(int userId,int state,int admId);
    @Async
    void insertSelective(int userId,int state,int admId);

    List<UserFreeNotFree> selectByExample(UserFreeNotFreeExample example);

    UserFreeNotFree selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserFreeNotFree record, @Param("example") UserFreeNotFreeExample example);

    int updateByExample(@Param("record") UserFreeNotFree record, @Param("example") UserFreeNotFreeExample example);

    int updateByPrimaryKeySelective(UserFreeNotFree record);

    int updateByPrimaryKey(UserFreeNotFree record);

    List<UserFreeNotFree> selectAll();
}
