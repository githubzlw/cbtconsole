package com.cbt.admuser.service;

import com.cbt.pojo.Admuser;

import java.util.List;


public interface AdmuserService {

    Admuser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admuser record);
    
    List<Admuser> selectAdmuser();
}