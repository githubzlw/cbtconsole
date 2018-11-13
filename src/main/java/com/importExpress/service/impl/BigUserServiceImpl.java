package com.importExpress.service.impl;

import com.importExpress.mapper.BigUserMapper;
import com.importExpress.pojo.BigUserBean;
import com.importExpress.service.BigUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BigUserServiceImpl implements BigUserService {

    @Autowired
    private BigUserMapper bigUserMapper;

    @Override
    public List<BigUserBean> queryForList(BigUserBean userBean) {
        return bigUserMapper.queryForList(userBean);
    }

    @Override
    public int queryForListCount(BigUserBean userBean) {
        return bigUserMapper.queryForListCount(userBean);
    }
}
