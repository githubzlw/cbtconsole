package com.cbt.service.impl;

import com.cbt.bean.*;
import com.cbt.dao.SingleGoodsDao;
import com.cbt.dao.impl.SingleGoodsDaoImpl;
import com.cbt.pojo.Admuser;
import com.cbt.service.SingleGoodsService;
import com.cbt.website.util.JsonResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingleGoodsServiceImpl implements SingleGoodsService {


	private SingleGoodsDao sgGdDao = new SingleGoodsDaoImpl();

	@Override
	public JsonResult saveGoods(String goodsUrl, int adminId, double goodsWeight,int drainageFlag,int goodsType,String aliPid,String aliPrice) {
		return sgGdDao.saveGoods(goodsUrl, adminId, goodsWeight,drainageFlag,goodsType,aliPid,aliPrice);
	}

	@Override
	public List<CustomOnlineGoodsBean> queryDealGoods() {
		return sgGdDao.queryDealGoods();
	}

	@Override
	public List<SameTypeGoodsBean> queryForList(SingleQueryGoodsParam queryPm) {
		return sgGdDao.queryForList(queryPm);
	}

	@Override
	public int queryForListCount(SingleQueryGoodsParam queryPm) {
		return sgGdDao.queryForListCount(queryPm);
	}

	@Override
	public List<Admuser> queryAllAdmin() {
		return sgGdDao.queryAllAdmin();
	}

	@Override
	public boolean deleteGoodsByPid(String pid) {
		return sgGdDao.deleteGoodsByPid(pid);
	}

	@Override
	public List<SingleGoodsCheck> queryCrossBorderGoodsForList(SingleGoodsCheck goodsCheck) {
		return sgGdDao.queryCrossBorderGoodsForList(goodsCheck);
	}

	@Override
	public int queryCrossBorderGoodsForListCount(SingleGoodsCheck goodsCheck) {
		return sgGdDao.queryCrossBorderGoodsForListCount(goodsCheck);
	}

	@Override
	public int updateSingleGoodsCheck(SingleGoodsCheck goodsCheck) {
		return sgGdDao.updateSingleGoodsCheck(goodsCheck);
	}

	@Override
	public int insertIntoSingleGoodsByIsCheck(String pid) {
		return sgGdDao.insertIntoSingleGoodsByIsCheck(pid);
	}

    @Override
    public int batchUpdateSingleGoodsCheck(List<SingleGoodsCheck> goodsList) {
        return sgGdDao.batchUpdateSingleGoodsCheck(goodsList);
    }

	@Override
	public List<CategoryBean> queryCategoryList(SingleGoodsCheck queryPm) {
		return sgGdDao.queryCategoryList(queryPm);
	}

    @Override
    public int setMainImgByPid(String pid, String imgUrl) {
        return sgGdDao.setMainImgByPid(pid,imgUrl);
    }

	@Override
	public int setMainImgByShopId(String shopId, String imgUrl) {
		return sgGdDao.setMainImgByShopId(shopId, imgUrl);
	}

}
