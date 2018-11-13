package com.cbt.admuser.dao;

import com.cbt.pojo.Admuser;

import java.util.List;

public interface AdmuserMapper {


    Admuser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admuser record);
    
    List<Admuser> selectAdmuser();
}