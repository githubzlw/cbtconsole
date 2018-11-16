package com.importExpress.mapper;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.importExpress.pojo.GoodsEditBean;
import com.importExpress.pojo.GoodsParseBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomGoodsMapper {


    /**
     * 插入已经编辑商品信息
     *
     * @param shopId
     * @param pid
     * @param adminId
     * @return
     */
    int insertPidIsEdited(@Param("shopId") String shopId, @Param("pid") String pid, @Param("adminId") int adminId);

    /**
     * 根据PID检查是否存在已经编辑信息
     *
     * @param pid
     * @return
     */
    int checkIsEditedByPid(@Param("pid") String pid);

    /**
     * 更新PID操作人信息
     *
     * @param editBean
     * @return
     */
    int updatePidIsEdited(GoodsEditBean editBean);


    /**
     * 根据用户ID查询已经录入的商品数量
     *
     * @param adminId
     * @param valid
     * @return
     */
    int queryTypeinGoodsTotal(@Param("adminId") int adminId, @Param("valid") int valid);

    /**
     * 查询在线商品数量
     *
     * @param valid
     * @return
     */
    int queryOnlineGoodsTotal(@Param("valid") int valid);

    /**
     * 查询已经编辑的商品数量
     *
     * @param adminId
     * @return
     */
    int queryIsEditOnlineGoodsTotal(@Param("adminId") int adminId, @Param("valid") int valid);

    List<String> queryStaticizeList(@Param("catid") String catid);


    /**
     * 设置对标信息
     *
     * @param pid
     * @param aliPid
     * @param aliPrice
     * @return
     */
    int saveBenchmarking(@Param("pid") String pid, @Param("aliPid") String aliPid, @Param("aliPrice") String aliPrice);


    /**
     * 插入编辑商品标识
     *
     * @param editBean
     * @return
     */
    int insertIntoGoodsEditBean(GoodsEditBean editBean);

    /**
     * 查询商品标识
     *
     * @param editBean
     * @return
     */
    List<GoodsEditBean> queryGoodsEditBean(GoodsEditBean editBean);


    /**
     * 查询商品编辑日志总数
     *
     * @param editBean
     * @return
     */
    int queryGoodsEditBeanCount(GoodsEditBean editBean);

    /**
     * 产品库数据分页查询
     *
     * @param goodsQuery
     * @return
     */
    List<CustomGoodsPublish> queryForListByParam(CustomGoodsQuery goodsQuery);

    /**
     * 产品库数据总数统计
     *
     * @param goodsQuery
     * @return
     */
    int queryForListByParamCount(CustomGoodsQuery goodsQuery);

    /**
     * 产品库类别数据查询
     *
     * @param goodsQuery
     * @return
     */
    List<CategoryBean> queryCategoryByParam(CustomGoodsQuery goodsQuery);


    /**
     * 根据PID查询商品详情
     *
     * @param pid
     * @return
     */
    CustomGoodsPublish queryGoodsDetailsByPid(@Param("pid") String pid);


    /**
     * 获取产品表最大ID
     *
     * @return
     */
    int queryMaxIdFromCustomGoods();

    /**
     * 分页查询商品信息
     *
     * @param minId
     * @param maxId
     * @return
     */
    List<GoodsParseBean> queryCustomGoodsByLimit(@Param("minId") int minId, @Param("maxId") int maxId);

    /**
     * 更新价格和统计信息
     *
     * @param goodsParseBean
     * @return
     */
    int updateCustomGoodsStatistic(GoodsParseBean goodsParseBean);


    /**
     * 确认是否存在标识的商品
     *
     * @param pid
     * @return
     */
    int checkIsExistsGoods(@Param("pid") String pid);

    /**
     * 插入价格和统计信息
     *
     * @param goodsParseBean
     * @return
     */
    int insertCustomGoodsStatistic(GoodsParseBean goodsParseBean);

    /**
     * 更新商品搜索次数
     *
     * @return
     */
    int updateGoodsSearchNum(@Param("pid") String pid);

    /**
     * 更新商品的点击次数
     *
     * @return
     */
    int updateGoodsClickNum();


    /**
     * 修改商品重量
     *
     * @param pid
     * @param newWeight
     * @param oldWeight
     * @param weightIsEdit
     * @return
     */
    int updateGoodsWeightByPid(@Param("pid") String pid, @Param("newWeight") double newWeight, @Param("oldWeight") double oldWeight, @Param("weightIsEdit") int weightIsEdit);

    /**
     * 更新商品详情信息
     *
     * @param goodsPublish
     * @return
     */
    int updateGoodsDetailsByInfo(CustomGoodsPublish goodsPublish);


    /**
     * 更新和锁定利润率
     *
     * @param pid
     * @param type
     * @param editProfit
     * @return
     */
    int editAndLockProfit(@Param("pid") String pid, @Param("type") int type, @Param("editProfit") double editProfit);

}
