package com.importExpress.mapper;

import com.importExpress.pojo.CustomPidComboBean;
import com.importExpress.pojo.CustomPidComboQuery;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.mapper
 * @date:2020-10-19
 */
public interface CustomPidComboMapper {


    List<CustomPidComboBean> queryForList(CustomPidComboQuery comboQuery);

    int queryForListCount(CustomPidComboQuery comboQuery);

    int insertCustomPidCombo(CustomPidComboBean comboBean);

    int updateCustomPidCombo(CustomPidComboBean comboBean);

    int deleteCustomPidCombo(CustomPidComboBean comboBean);


}
