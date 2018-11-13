package com.importExpress.service;

import com.importExpress.pojo.AliBenchmarkingBean;
import com.importExpress.pojo.AliBenchmarkingStatistic;
import com.importExpress.pojo.KeyWordBean;

import java.util.List;

public interface AliBeanchmarkingService {

    /**
     * 查询对标商品列表信息
     *
     * @param benchmarkingBean
     * @return
     */
    List<AliBenchmarkingBean> queryAliBenchmarkingForList(AliBenchmarkingBean benchmarkingBean);

    /**
     * 对标商品列表统计总数
     *
     * @param benchmarkingBean
     * @return
     */
    int queryAliBenchmarkingForListCount(AliBenchmarkingBean benchmarkingBean);


    /**
     * 对标商品相关统计
     *
     * @param adminId
     * @return
     */
    List<AliBenchmarkingStatistic> queryAliBenchmarkingStatistic(int adminId, String beginDate, String endDate, String admName);


    /**
     * 查询对应adminId的关键词列表
     *
     * @param adminId
     * @param startNum
     * @param limitNum
     * @return
     */
    List<KeyWordBean> queryKeyWordListByAdminId(int adminId, int startNum, int limitNum);

    int queryKeyWordListByAdminIdCount(int adminId);

    /**
     * 设置对标商品标识
     *
     * @param pid
     * @param moq
     * @param rangePrice
     * @param priceContent
     * @param isSoldFlag
     * @param benchmarkingFlag
     * @return
     */
    boolean updateGoodsFlag(String pid, int moq, String rangePrice, String priceContent, int isSoldFlag, int benchmarkingFlag);

}
