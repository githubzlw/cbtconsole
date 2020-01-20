package com.importExpress.service.impl;

import com.importExpress.mapper.UserFreeNotFreeMapper;
import com.importExpress.pojo.UserFreeNotFree;
import com.importExpress.pojo.UserFreeNotFreeExample;
import com.importExpress.service.UserFreeNotFreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * *****************************************************************************************
 *
 * @ClassName UserFreeNotFreeServiceImpl
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2020/1/19 1:58 下午
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       1:58 下午2020/1/19     cjc                       初版
 * ******************************************************************************************
 */
@Service
public class UserFreeNotFreeServiceImpl implements UserFreeNotFreeService {
    @Autowired
    UserFreeNotFreeMapper userFreeNotFreeMapper;
    @Override
    public int countByExample(UserFreeNotFreeExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(UserFreeNotFreeExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Async
    @Override
    public int insert(int userId,int state,int admId) {
        UserFreeNotFree userFreeNotFree= new UserFreeNotFree();
        userFreeNotFree.setAdminid(admId);
        userFreeNotFree.setUserid(userId);
        userFreeNotFree.setFree(state);
        return userFreeNotFreeMapper.insert(userFreeNotFree);
    }
    @Async
    @Override
    public int insertSelective(int userId,int state,int admId) {
        UserFreeNotFree userFreeNotFree= new UserFreeNotFree();
        userFreeNotFree.setAdminid(admId);
        userFreeNotFree.setUserid(userId);
        userFreeNotFree.setFree(state);
        return userFreeNotFreeMapper.insertSelective(userFreeNotFree);
    }

    @Override
    public List<UserFreeNotFree> selectByExample(UserFreeNotFreeExample example) {
        return null;
    }

    @Override
    public UserFreeNotFree selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(UserFreeNotFree record, UserFreeNotFreeExample example) {
        return 0;
    }

    @Override
    public int updateByExample(UserFreeNotFree record, UserFreeNotFreeExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(UserFreeNotFree record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(UserFreeNotFree record) {
        return 0;
    }

    @Override
    public List<UserFreeNotFree> selectAll() {
        return userFreeNotFreeMapper.selectAll();
    }
}
