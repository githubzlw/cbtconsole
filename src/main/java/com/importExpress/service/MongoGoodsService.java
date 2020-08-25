package com.importExpress.service;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.importExpress.pojo.MongoGoodsBean;

import java.util.List;
import java.util.Map;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.cbt.service
 * @date:2019/11/22
 */
public interface MongoGoodsService {

    /**
     * 获取MongoDB中查询数据
     *
     * @param queryBean
     * @return
     * @throws Exception
     */
    List<CustomGoodsPublish> queryListByParam(CustomGoodsQuery queryBean) throws Exception;

    /**
     * 获取MongoDB中查询数据总数
     *
     * @param queryBean
     * @return
     * @throws Exception
     */
    long queryListByParamCount(CustomGoodsQuery queryBean) throws Exception;

    /**
     * 查询catid的数据
     *
     * @param queryBean
     * @return
     * @throws Exception
     */
    List<String> findCatidFromMongo3(CustomGoodsQuery queryBean) throws Exception;

    /**
     * 根据ID分页查询商品信息
     *
     * @param minId
     * @param maxId
     * @return
     */
    List<MongoGoodsBean> queryBeanByLimit(int minId, int maxId);

    /**
     * 查询单个bean
     * @param pid
     * @return
     */
    MongoGoodsBean queryBeanByPid(String pid);


    /**
     * 插入是到MongoDB
     *
     * @param list
     * @return
     */
    int insertGoodsToMongoBatch(List<MongoGoodsBean> list) throws Exception;

    /**
     * 读取数据并且出入
     * @return
     * @throws Exception
     */
    int insertGoodsToMongoMapBatch(List<Map<String, Object>> list) throws Exception;

    /**
     * 单个插入
     * @param goodsBean
     * @return
     * @throws Exception
     */
    int insertGoodsToMongoSingle(MongoGoodsBean goodsBean)  throws Exception;

    /**
     * 插入分类数据
     *
     * @param cidMap
     * @return
     */
    int insertIntoPidCatidNum(Map<String, Integer> cidMap);

    /**
     * 获取所有Categorybean
     *
     * @return
     */
    List<CategoryBean> queryAllCategoryBean();

    /**
     * 查询MongoDB中是否存在
     * @param pidList
     * @return
     */
    List<String> checkIsMongoByList(List<String> pidList);

    /**
     * 根据PID查询MongoDB是否存在
     * @param pid
     * @return
     */
    boolean checkIsMongoByPid(String pid);

    /**
     * 更新MongoDB数据
     *
     * @param mongoGoodsBean
     * @return
     */
    long updateGoodsInfoToMongoDb(MongoGoodsBean mongoGoodsBean) throws Exception;

    /**
     * 批量更新MongoDB数据
     *
     * @param list
     * @return
     */
    long batchUpdateGoodsInfoToMongoDb(List<MongoGoodsBean> list) throws Exception;

    /**
     * 插入或者更新MongoDB数据
     * @param pid
     * @return
     */
    int insertOrUpdateMongodb(String pid) throws Exception;


    /**
     * 清空库
     */
    void clearDatabase();

    /**
     * 创建索引
     */
    void createDatabaseIndex();

}
