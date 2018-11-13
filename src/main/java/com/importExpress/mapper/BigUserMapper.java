package com.importExpress.mapper;

import com.importExpress.pojo.BigUserBean;

import java.util.List;

public interface BigUserMapper {

    List<BigUserBean> queryForList(BigUserBean userBean);

    int queryForListCount(BigUserBean userBean);

}
