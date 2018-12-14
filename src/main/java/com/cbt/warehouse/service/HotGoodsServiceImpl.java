package com.cbt.warehouse.service;

import com.cbt.bean.CustomGoodsBean;
import com.cbt.warehouse.dao.HotGoodsMapper;
import com.cbt.warehouse.pojo.HotSellingCategory;
import com.cbt.warehouse.pojo.HotSellingGoods;
import com.importExpress.mapper.HotManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HotGoodsServiceImpl implements HotGoodsService {
	@Autowired
	private HotGoodsMapper hotGoodsMapper;

	@Autowired
	private HotManageMapper hotManageMapper;

	@Override
	public List<HotSellingCategory> queryForList() {
		return hotGoodsMapper.queryForList();
	}

	@Override
	public int updateHotSellingCategory(HotSellingCategory category) {
		return hotGoodsMapper.updateHotSellingCategory(category);
	}

	@Override
	public List<HotSellingGoods> queryByHotSellingCategory(int hotSellingCategoryId, int hotType) {
		return hotGoodsMapper.queryByHotSellingCategory(hotSellingCategoryId,hotType);
	}

	@Override
	public int insertHotSellingGoods(HotSellingGoods hsGoods) {
		return hotGoodsMapper.insertHotSellingGoods(hsGoods);
	}

	@Override
	public int deleteGoodsByPid(int categoryId, String goodsPid) {
		return hotGoodsMapper.deleteGoodsByPid(categoryId, goodsPid);
	}

	@Override
	public int updateHotSellingGoods(HotSellingGoods hsGoods) {
		hotManageMapper.insertHotSellingUpdateLog(hsGoods.getHotSellingId(),hsGoods.getGoodsPid(),hsGoods.getUpdateAdmid(),Integer.valueOf(hsGoods.getIsOn()));
		return hotGoodsMapper.updateHotSellingGoods(hsGoods);
	}

	@Override
	public int deleteHotSellingGoodsTmp(int hsGoodsId) {
		return hotGoodsMapper.deleteHotSellingGoodsTmp(hsGoodsId);
	}

	@Override
	public void genHotCategoryAndGoods() {
		hotGoodsMapper.genHotCategoryAndGoods();
	}

	@Override
	public List<HotSellingGoods> queryForByTmp() {
		return hotGoodsMapper.queryForByTmp();
	}

	@Override
	public int updateHotSellingGoodsValid() {
		return hotGoodsMapper.updateHotSellingGoodsValid();
	}

	@Override
	public int updateHotSellingGoodsPid(int hsGoodsId, String goodsPid) {
		return hotGoodsMapper.updateHotSellingGoodsPid(hsGoodsId, goodsPid);
	}

	@Override
	public int updateHotSellingGoodsImgUrl(int hsGoodsId, String goodsImgUrl) {
		return hotGoodsMapper.updateHotSellingGoodsImgUrl(hsGoodsId, goodsImgUrl);
	}

	@Override
	public int deleteHotSellingGoodsByImgUrl() {
		return hotGoodsMapper.deleteHotSellingGoodsByImgUrl();
	}

	@Override
	public List<HotSellingCategory> queryInsertCategory() {
		return hotGoodsMapper.queryInsertCategory();
	}

	@Override
	public List<HotSellingGoods> queryInsertGoods() {
		return hotGoodsMapper.queryInsertGoods();
	}

	@Override
	public int insertCategoryToOnLine(List<HotSellingCategory> ctLst) {
		return hotGoodsMapper.insertCategoryToOnLine(ctLst);
	}

	@Override
	public int insertGoodsToOnLine(List<HotSellingGoods> gdLst) {
		return hotGoodsMapper.insertGoodsToOnLine(gdLst);
	}

	@Override
	public int updateInsertCategory() {
		return hotGoodsMapper.updateInsertCategory();
	}

	@Override
	public int updateInsertGoods() {
		return hotGoodsMapper.updateInsertGoods();
	}

	@Override
	public int updateRelationship() {
		return hotGoodsMapper.updateRelationship();
	}

	@Override
	public CustomGoodsBean queryFor1688Goods(String pid) {
		return hotGoodsMapper.queryFor1688Goods(pid);
	}

	@Override
	public boolean checkExistsGoods(int categoryId, String goodsPid) {
		return hotGoodsMapper.queryExistsGoodsCount(categoryId, goodsPid) > 0;
	}

	@Override
	public boolean insertHotGoodsUse(String goodsPid) {
		return hotGoodsMapper.insertHotGoodsUse(goodsPid);
	}

	@Override
	public int useHotGoodsByState(Map<String, String> pidsMap,int hotId,int adminId) {
		for (String key : pidsMap.keySet()) {
			hotManageMapper.insertHotSellingUpdateLog(hotId, key, adminId, Integer.valueOf(pidsMap.get(key)));
		}
		return hotGoodsMapper.useHotGoodsByState(pidsMap);
	}

	@Override
	public int deleteHotUseGoodsByPid(String goodsPid) {
		return hotGoodsMapper.deleteHotUseGoodsByPid(goodsPid);
	}

}
