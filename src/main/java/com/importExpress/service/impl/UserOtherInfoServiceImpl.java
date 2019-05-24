package com.importExpress.service.impl;

import com.importExpress.mapper.UserOtherInfoMapper;
import com.importExpress.pojo.UserOtherInfoBean;
import com.importExpress.service.UserOtherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOtherInfoServiceImpl implements UserOtherInfoService {

    @Autowired
    private UserOtherInfoMapper userOtherInfoMapper;


    @Override
    public List<UserOtherInfoBean> queryForList(UserOtherInfoBean userOtherInfo) {
        return userOtherInfoMapper.queryForList(userOtherInfo);
    }

    @Override
    public int queryForListCount(UserOtherInfoBean userOtherInfo) {
        return userOtherInfoMapper.queryForListCount(userOtherInfo);
    }

    @Override
    public int updateFollowInfo(UserOtherInfoBean userOtherInfo) {
        userOtherInfoMapper.insertFollowInfoByAdminId(userOtherInfo);
        return userOtherInfoMapper.updateFollowInfo(userOtherInfo);
    }

    @Override
    public int insertFollowInfoByAdminId(UserOtherInfoBean userOtherInfo) {
        return userOtherInfoMapper.insertFollowInfoByAdminId(userOtherInfo);
    }
}
