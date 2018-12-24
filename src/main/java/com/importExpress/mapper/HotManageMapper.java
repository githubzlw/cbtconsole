package com.importExpress.mapper;

import com.cbt.warehouse.pojo.HotCategory;
import com.cbt.warehouse.pojo.HotDiscount;
import com.cbt.warehouse.pojo.HotEvaluation;
import com.importExpress.pojo.HotSellGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HotManageMapper {

    List<HotCategory> queryForList(HotCategory param);

    int queryForListCount(HotCategory param);

    int checkHotCategoryIsExists(HotCategory param);

    int insertIntoHotCategory(HotCategory param);

    HotCategory getCategoryById(@Param("id") int id);

    int updateHotCategory(HotCategory param);


    HotDiscount queryDiscountByHotIdAndPid(@Param("hotId") int hotId, @Param("pid") String pid);

    int checkDiscountIsExists(@Param("hotId") int hotId, @Param("pid") String pid);

    int insertIntoDiscount(HotDiscount hotDiscount);

    int updateDiscountInfo(HotDiscount hotDiscount);


    int checkEvaluationIsExists(@Param("goodsPid") String goodsPid, @Param("skuId") String skuId);

    HotEvaluation queryHotEvaluationById(@Param("id") int id);

    int insertIntoHotEvaluation(HotEvaluation hotEvaluation);

    int updateHotEvaluation(HotEvaluation hotEvaluation);

    int deleteCategory(@Param("id") int id);

    int deleteGoodsByCategoryId(@Param("id") int id);


    int insertHotSellingUpdateLog(@Param("hotId") int hotId, @Param("pid") String pid, @Param("adminId") int adminId, @Param("isOn") int isOn);

    List<HotSellGoods> queryGoodsByHotType(@Param("hotType") int hotType);

	List<HotCategory> queryCategoryList(HotCategory hotCategory);
}
