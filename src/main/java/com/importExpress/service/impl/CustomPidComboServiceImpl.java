package com.importExpress.service.impl;

import com.importExpress.mapper.CustomPidComboMapper;
import com.importExpress.pojo.CustomPidComboBean;
import com.importExpress.pojo.CustomPidComboQuery;
import com.importExpress.service.CustomPidComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.service.impl
 * @date:2020-10-19
 */
@Service
public class CustomPidComboServiceImpl implements CustomPidComboService {

    @Autowired
    private CustomPidComboMapper customPidComboMapper;

    @Override
    public List<CustomPidComboBean> queryForList(CustomPidComboQuery comboQuery) {
        return customPidComboMapper.queryForList(comboQuery);
    }

    @Override
    public int queryForListCount(CustomPidComboQuery comboQuery) {
        return customPidComboMapper.queryForListCount(comboQuery);
    }

    @Override
    public int insertCustomPidCombo(CustomPidComboBean comboBean) {
        return customPidComboMapper.insertCustomPidCombo(comboBean);
    }

    @Override
    public int updateCustomPidCombo(CustomPidComboBean comboBean) {
        return customPidComboMapper.updateCustomPidCombo(comboBean);
    }

    @Override
    public int deleteCustomPidCombo(CustomPidComboBean comboBean) {
        return customPidComboMapper.deleteCustomPidCombo(comboBean);
    }
}
