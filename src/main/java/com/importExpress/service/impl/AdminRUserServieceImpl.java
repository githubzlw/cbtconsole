package com.importExpress.service.impl;

import com.importExpress.mapper.AdminRUserMapper;
import com.importExpress.pojo.AdminRUser;
import com.importExpress.pojo.AdminRUserExample;
import com.importExpress.service.AdminRUserServiece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * *****************************************************************************************
 *
 * @ClassName AdminRUserServieceImpl
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2018/10/10 10:09
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       10:092018/10/10     cjc                       初版
 * ******************************************************************************************
 */
@Service("AdminRUserServieceImpl")
public class AdminRUserServieceImpl implements AdminRUserServiece {
    @Autowired
    private AdminRUserMapper adminRUserMapper;
    @Override
    public int countByExample(AdminRUserExample example) {
        return adminRUserMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(AdminRUserExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(AdminRUser record) {
        return 0;
    }

    @Override
    public int insertSelective(AdminRUser record) {
        return 0;
    }

    @Override
    public List<AdminRUser> selectByExample(AdminRUserExample example) {
        return adminRUserMapper.selectByExample(example);
    }

    @Override
    public AdminRUser selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(AdminRUser record, AdminRUserExample example) {
        return adminRUserMapper.updateByExampleSelective(record,example);
    }

    @Override
    public int updateByExample(AdminRUser record, AdminRUserExample example) {
        return adminRUserMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(AdminRUser record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(AdminRUser record) {
        return 0;
    }
}
