package com.importExpress.mapper;

import com.cbt.pojo.SearchStatic;
import com.cbt.warehouse.pojo.HotCategory;
import com.cbt.warehouse.pojo.HotClassInfo;
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

    int insertIntoHotEvaluation(HotEvaluation hotEvaluation);

    int updateHotEvaluation(HotEvaluation hotEvaluation);

    int deleteCategory(@Param("id") int id);

    int deleteGoodsByCategoryId(@Param("id") int id);


    int insertHotSellingUpdateLog(@Param("hotId") int hotId, @Param("pid") String pid, @Param("adminId") int adminId, @Param("isOn") int isOn);

    List<HotSellGoods> queryGoodsByHotType(@Param("hotType") int hotType);

	List<HotCategory> queryCategoryList(HotCategory hotCategory);

	/**
     * 获取所有的热卖分组
     * @return
     */
	List<HotClassInfo> getClassInfoList(HotClassInfo classInfo);

    /**
     * 插入热卖分组
     * @param hotClassInfo
     * @return
     */
	int insertIntoHotClassInfo(HotClassInfo hotClassInfo);

	/**
     * 更新热卖分组
     * @param hotClassInfo
     * @return
     */
	int updateIntoHotClassInfo(HotClassInfo hotClassInfo);

	/**
     * 删除热卖分组
     * @param id
     * @return
     */
	int deleteHotClassInfo(int id);

	List<SearchStatic> querySearchStaticList(SearchStatic searchStatic);

	int querySearchStaticListCount(SearchStatic searchStatic);

	int checkSearchStatic(SearchStatic searchStatic);

	int addSearchStatic(SearchStatic searchStatic);

	int updateSearchStatic(SearchStatic searchStatic);

	int updateValid(SearchStatic searchStatic);

	int setJsonState(SearchStatic searchStatic);
}
