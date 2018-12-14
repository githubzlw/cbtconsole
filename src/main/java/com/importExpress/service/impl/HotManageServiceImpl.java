package com.importExpress.service.impl;

import com.cbt.warehouse.pojo.HotCategory;
import com.cbt.warehouse.pojo.HotDiscount;
import com.cbt.warehouse.pojo.HotEvaluation;
import com.importExpress.mapper.HotManageMapper;
import com.importExpress.service.HotManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotManageServiceImpl implements HotManageService {
    @Autowired
    private HotManageMapper hotManageMapper;


    @Override
    public List<HotCategory> queryForList(HotCategory param) {
        return hotManageMapper.queryForList(param);
    }

    @Override
    public int queryForListCount(HotCategory param) {
        return hotManageMapper.queryForListCount(param);
    }

    @Override
    public int checkHotCategoryIsExists(HotCategory param) {
        return hotManageMapper.checkHotCategoryIsExists(param);
    }

    @Override
    public int insertIntoHotCategory(HotCategory param) {
        return hotManageMapper.insertIntoHotCategory(param);
    }

    @Override
    public HotCategory getCategoryById(int id) {
        return hotManageMapper.getCategoryById(id);
    }

    @Override
    public int updateHotCategory(HotCategory param) {
        hotManageMapper.insertHotSellingUpdateLog(param.getId(), "", param.getUpdateAdminId(), param.getIsOn());
        return hotManageMapper.updateHotCategory(param);
    }

    @Override
    public HotDiscount queryDiscountByHotIdAndPid(int hotId, String pid) {
        return hotManageMapper.queryDiscountByHotIdAndPid(hotId, pid);
    }

    @Override
    public int checkDiscountIsExists(int hotId, String pid) {
        return hotManageMapper.checkDiscountIsExists(hotId, pid);
    }

    @Override
    public int insertIntoDiscount(HotDiscount hotDiscount) {
        return hotManageMapper.insertIntoDiscount(hotDiscount);
    }

    @Override
    public int updateDiscountInfo(HotDiscount hotDiscount) {
        return hotManageMapper.updateDiscountInfo(hotDiscount);
    }

    @Override
    public int checkEvaluationIsExists(String goodsPid, String skuId) {
        return hotManageMapper.checkEvaluationIsExists(goodsPid, skuId);
    }

    @Override
    public HotEvaluation queryHotEvaluationById(int id) {
        return hotManageMapper.queryHotEvaluationById(id);
    }

    @Override
    public int insertIntoHotEvaluation(HotEvaluation hotEvaluation) {
        return hotManageMapper.insertIntoHotEvaluation(hotEvaluation);
    }

    @Override
    public int updateHotEvaluation(HotEvaluation hotEvaluation) {
        return hotManageMapper.updateHotEvaluation(hotEvaluation);
    }

    @Override
    public int deleteCategory(int id) {
        hotManageMapper.deleteGoodsByCategoryId(id);
        return hotManageMapper.deleteCategory(id);
    }
}
