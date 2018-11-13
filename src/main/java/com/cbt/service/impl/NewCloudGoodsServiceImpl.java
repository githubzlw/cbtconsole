package com.cbt.service.impl;

import com.cbt.bean.*;
import com.cbt.dao.NewCloudGoodsDao;
import com.cbt.dao.impl.NewCloudGoodsDaoImpl;
import com.cbt.service.NewCloudGoodsService;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewCloudGoodsServiceImpl implements NewCloudGoodsService {
	private NewCloudGoodsDao newCloudGoodsDao = new NewCloudGoodsDaoImpl();

	@Override
	public List<CategoryBean> getCaterory() {

		return newCloudGoodsDao.getCaterory();
	}

	@Override
	public List<CategoryBean> queryCateroryByParam(CustomGoodsQuery queryBean) {
		return newCloudGoodsDao.queryCateroryByParam(queryBean);
	}

	@Override
	public List<CustomGoodsBean> getGoodsList(String catid, int page, String sttime, String edtime, int state) {

		return newCloudGoodsDao.getGoodsList(catid, page, sttime, edtime, state);
	}

	@Override
	public CustomGoodsPublish getGoods(String pid, int type) {

		return newCloudGoodsDao.getGoods(pid, type);
	}

	@Override
	public int updateInfo(CustomGoodsBean bean) {

		return newCloudGoodsDao.updateInfo(bean);
	}

	@Override
	public int publish(CustomGoodsPublish bean) {

		return newCloudGoodsDao.publish(bean);
	}

	@Override
	public String getGoodsInfo(String pid) {

		return newCloudGoodsDao.getGoodsInfo(pid);
	}

	@Override
	public int updateState(int state, String pid, int adminid) {

		return newCloudGoodsDao.updateState(state, pid, adminid);
	}

	@Override
	public int updateValid(int valid, String pid) {

		return newCloudGoodsDao.updateValid(valid, pid);
	}

	@Override
	public int insertRecord(String pid, String admin, int state, String record) {

		return newCloudGoodsDao.insertRecord(pid, admin, state, record);
	}

	@Override
	public List<CustomRecord> getRecordList(String pid, int page) {

		return newCloudGoodsDao.getRecordList(pid, page);
	}

	@Override
	public List<CustomGoodsBean> getGoodsList(String pidList) {

		return newCloudGoodsDao.getGoodsList(pidList);
	}


	@Override
	public boolean updateStateList(int state, String pids, int adminid) {

		return newCloudGoodsDao.updateStateList(state, pids, adminid);
	}

	@Override
	public int updateValidList(int valid, String pids) {

		return newCloudGoodsDao.updateValidList(valid, pids);
	}

	@Override
	public int insertRecordList(List<String> pids, String admin, int state, String record) {

		return newCloudGoodsDao.insertRecordList(pids, admin, state, record);
	}

	@Override
	public List<CustomGoodsBean> getGoodsListByCatid(String catid) {

		return newCloudGoodsDao.getGoodsListByCatid(catid);
	}

	@Override
	public int updateInfoList(List<CustomGoodsBean> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}

		return newCloudGoodsDao.updateInfoList(list);
	}

	@Override
	public List<CustomGoodsPublish> queryGoodsInfos(CustomGoodsQuery queryBean) {
		return newCloudGoodsDao.queryGoodsInfos(queryBean);
	}

	@Override
	public int queryGoodsInfosCount(CustomGoodsQuery queryBean) {
		return newCloudGoodsDao.queryGoodsInfosCount(queryBean);
	}

	@Override
	public void batchSaveEnName(Admuser user, List<CustomGoodsBean> cgLst) {
		newCloudGoodsDao.batchSaveEnName(user, cgLst);
	}

	@Override
	public CustomGoodsPublish queryGoodsDetails(String pid, int type) {
		return newCloudGoodsDao.queryGoodsDetails(pid, type);
	}

	@Override
	public int saveEditDetalis(CustomGoodsPublish cgp, String adminName, int adminId, int type) {
		return newCloudGoodsDao.saveEditDetalis(cgp, adminName, adminId, type);
	}

	@Override
	public GoodsPictureQuantity queryPictureQuantityByPid(String pid) {
		return newCloudGoodsDao.queryPictureQuantityByPid(pid);
	}

	@Override
	public int setGoodsValid(String pid, String adminName, int adminId, int type) {
		return newCloudGoodsDao.setGoodsValid(pid, adminName, adminId, type);
	}

	@Override
	public boolean batchDeletePids(String[] pidLst) {
		return newCloudGoodsDao.batchDeletePids(pidLst);
	}

	@Override
	public int updateGoodsState(String pid,int goodsState) {
		return newCloudGoodsDao.updateGoodsState(pid,goodsState);
	}

	@Override
	public boolean updateBmFlagByPids(String[] pidLst, int adminid) {
		return newCloudGoodsDao.updateBmFlagByPids(pidLst, adminid) == pidLst.length;
	}

	@Override
	public ShopManagerPojo queryByShopId(String shopId) {
		return newCloudGoodsDao.queryByShopId(shopId);
	}

	@Override
	public boolean batchInsertSimilarGoods(String mainPid, String similarPids, int adminId,List<String> existPids) {
		return newCloudGoodsDao.batchInsertSimilarGoods(mainPid, similarPids, adminId,existPids);
	}

	@Override
	public List<SimilarGoods> querySimilarGoodsByMainPid(String mainPid) {
		return newCloudGoodsDao.querySimilarGoodsByMainPid(mainPid);
	}

}
