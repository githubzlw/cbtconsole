package com.importExpress.service;

import com.cbt.warehouse.pojo.HotCategory;
import com.cbt.warehouse.pojo.HotDiscount;
import com.cbt.warehouse.pojo.HotEvaluation;

import java.util.List;

public interface HotManageService {

    /**
     * 查询热卖类别管理分页数据
     * @param param
     * @return
     */
    List<HotCategory> queryForList(HotCategory param);


    /**
     * 查询热卖类别管理总数
     * @param param
     * @return
     */
    int queryForListCount(HotCategory param);

    /**
     * 检查是否存在热卖类别数据
     * @param param
     * @return
     */
    int checkHotCategoryIsExists(HotCategory param);

    /**
     * 插入类别数据
     * @param param
     * @return
     */
    int insertIntoHotCategory(HotCategory param);

    /**
     * 根据ID获取类别信息
     * @param id
     * @return
     */
    HotCategory getCategoryById(int id);

    /**
     * 更新热卖类别数据
     * @param param
     * @return
     */
    int updateHotCategory(HotCategory param);


    /**
     * 根据热卖ID和商品PID获取折扣信息
     * @param hotId
     * @param pid
     * @return
     */
    HotDiscount queryDiscountByHotIdAndPid(int hotId, String pid);

    /**
     *确认折扣信息是否存在
     * @param hotId
     * @param pid
     * @return
     */
    int checkDiscountIsExists(int hotId, String pid);

    /**
     * 插入折扣信息
     * @param hotDiscount
     * @return
     */
    int insertIntoDiscount(HotDiscount hotDiscount);

    /**
     * 更新折扣信息
     * @param hotDiscount
     * @return
     */
    int updateDiscountInfo(HotDiscount hotDiscount);


    /**
     * 检查商品评价是否存在
     * @param goodsPid
     * @param skuId
     * @return
     */
    int checkEvaluationIsExists(String goodsPid, String skuId);

    /**
     * 根据ID查询商品评价信息
     * @param id
     * @return
     */
    HotEvaluation queryHotEvaluationById(int id);

    /**
     * 插入商品评价信息
     * @param hotEvaluation
     * @return
     */
    int insertIntoHotEvaluation(HotEvaluation hotEvaluation);

    /**
     * 更新商品评价信息
     * @param hotEvaluation
     * @return
     */
    int updateHotEvaluation(HotEvaluation hotEvaluation);

    /**
     * 删除类别
     * @param id
     * @return
     */
    int deleteCategory(int id);

}