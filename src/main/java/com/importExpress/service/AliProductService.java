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

    /**
     * 设置阿里商品处理状态
     *
     * @param aliPid
     * @param dealState
     * @return
     */
    int setAliFlag(String aliPid, int dealState,int adminId);


    /**
     * 设置1688商品的处理状态
     *
     * @param aliPid
     * @param pid
     * @param dealState
     * @return
     */
    int set1688PidFlag(String aliPid, String pid, int dealState,int adminId);

    /**
     * 开发产品和数据标识
     * @param aliPid
     * @param aliPrice
     * @param pid
     * @param adminId
     * @return
     */
    int develop1688Pid(String aliPid,String aliPrice, String pid,int adminId);
    
}
