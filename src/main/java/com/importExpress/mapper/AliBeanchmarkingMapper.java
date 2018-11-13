package com.importExpress.mapper;

import com.importExpress.pojo.AliBenchmarkingBean;
import com.importExpress.pojo.AliBenchmarkingStatistic;
import com.importExpress.pojo.KeyWordBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AliBeanchmarkingMapper {
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
     * @return
     */
    List<AliBenchmarkingStatistic> queryAliBenchmarkingStatistic(@Param("adminId") int adminId, @Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("admName") String admName);


    /**
     * 查询对应adminId的关键词列表
     *
     * @param adminId
     * @param startNum
     * @param limitNum
     * @return
     */
    List<KeyWordBean> queryKeyWordListByAdminId(@Param("adminId") int adminId, @Param("startNum") int startNum, @Param("limitNum") int limitNum);


    int queryKeyWordListByAdminIdCount(@Param("adminId") int adminId);

    /**
     * 设置对标商品标识
     * @param pid
     * @param moq
     * @param freeFlag
     * @param benchmarkingFlag
     * @return
     */
    int updateGoodsFlag(@Param("pid") String pid, @Param("moq") int moq, @Param("freeFlag") int freeFlag, @Param("benchmarkingFlag") int benchmarkingFlag);
}
