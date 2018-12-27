package com.importExpress.service;


import com.importExpress.pojo.AliProductBean;
import com.importExpress.pojo.ImportProductBean;

import java.util.List;

public interface AliProductService {


    /**
     * 分页查询速卖通对标信息
     *
     * @param aliProduct
     * @return
     */
    List<AliProductBean> queryForList(AliProductBean aliProduct);

    /**
     * 速卖通对标信息总数
     *
     * @param aliProduct
     * @return
     */
    int queryForListCount(AliProductBean aliProduct);

    /**
     * 查询对标的lire数据
     *
     * @param aliPid
     * @return
     */
    List<ImportProductBean> query1688ByLire(String aliPid);

    /**
     * 查询对标的lire数据
     *
     * @param aliPid
     * @return
     */
    List<ImportProductBean> query1688ByPython(String aliPid);
}
