package com.importExpress.mapper;

import com.importExpress.pojo.AmazonOrderBean;
import com.importExpress.pojo.AmazonOrderParam;
import com.importExpress.pojo.AmazonProductBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AmazonProductMapper {

    /**
     * 分页查询速卖通对标信息
     *
     * @param amazonProduct
     * @return
     */
    List<AmazonProductBean> queryForList(AmazonProductBean amazonProduct);

    /**
     * 速卖通对标信息总数
     *
     * @param amazonProduct
     * @return
     */
    int queryForListCount(AmazonProductBean amazonProduct);

    /**
     * 设置匹配PID和标识
     *
     * @param amazonPid
     * @param dealState
     * @param adminId
     * @param matchPid
     * @return
     */
    int setAmazonDealFlag(@Param("amazonPid") String amazonPid, @Param("dealState") int dealState, @Param("adminId") int adminId, @Param("matchPid") String matchPid);


    /**
     * 检查亚马逊产品是否存在
     *
     * @param amazonPid
     * @return
     */
    int checkAmazonProductIsExists(@Param("amazonPid") String amazonPid);

    List<AmazonOrderBean> queryAmazonOrderList(AmazonOrderParam param);

    int queryAmazonOrderListCount(AmazonOrderParam param);

    int insertAmazonOrder(AmazonOrderBean orderBean);

    int insertAmazonOrderList(@Param("list") List<AmazonOrderBean> list);
}
