package com.importExpress.service;

import com.importExpress.pojo.BigUserBean;

import java.util.List;

public interface BigUserService {

    List<BigUserBean> queryForList(BigUserBean userBean);

    int queryForListCount(BigUserBean userBean);

}
