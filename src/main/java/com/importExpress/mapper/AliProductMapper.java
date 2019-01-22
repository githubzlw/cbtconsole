package com.importExpress.mapper;

import com.importExpress.pojo.AliProductBean;
import com.importExpress.pojo.Goods1688OfferBean;
import com.importExpress.pojo.ImportProductBean;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AliProductMapper {


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
    List<ImportProductBean> query1688ByLire(@Param("aliPid") String aliPid);

    /**
     * 查询对标的lire数据
     *
     * @param aliPid
     * @return
     */
    List<ImportProductBean> query1688ByPython(@Param("aliPid") String aliPid);

    /**
     * 设置阿里商品处理状态
     *
     * @param aliPid
     * @param dealState
     * @return
     */
    int setAliFlag(@Param("aliPid") String aliPid, @Param("dealState") int dealState, @Param("adminId") int adminId);

    /**
     * 设置1688商品的处理状态
     *
     * @param aliPid
     * @param pid
     * @param dealState
     * @return
     */
    int set1688PidFlag(@Param("aliPid") String aliPid, @Param("pid") String pid, @Param("dealState") int dealState, @Param("adminId") int adminId);


    /**
     * 开发产品的数据标识
     *
     * @param aliPid
     * @param pid
     * @return
     */
    int setDevelop1688PidFlag(@Param("aliPid") String aliPid, @Param("pid") String pid, @Param("adminId") int adminId);

    /**
     * 爆款开发的商品信息
     * @param pid
     * @return
     */
    Goods1688OfferBean queryGoodsOffersByPid(@Param("pid") String pid);
    
}
