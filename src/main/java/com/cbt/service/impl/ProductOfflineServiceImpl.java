package com.cbt.service.impl;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.cbt.dao.ProductOfflineDao;
import com.cbt.dao.impl.ProductOfflineDaoImpl;
import com.cbt.service.ProductOfflineService;
import com.cbt.website.userAuth.bean.Admuser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOfflineServiceImpl implements ProductOfflineService {
	private ProductOfflineDao ptOlDao = new ProductOfflineDaoImpl();


	@Override
	public List<CategoryBean> queryCateroryByParam(CustomGoodsQuery queryBean) {
		return ptOlDao.queryCateroryByParam(queryBean);
	}

	@Override
	public CustomGoodsPublish getGoods(String pid, int type) {

		return ptOlDao.getGoods(pid, type);
	}

	@Override
	public int updateInfo(CustomGoodsBean bean) {

		return ptOlDao.updateInfo(bean);
	}

	@Override
	public int publish(CustomGoodsPublish bean) {

		return ptOlDao.publish(bean);
	}


	@Override
	public int updateState(int state, String pid, int adminid) {

		return ptOlDao.updateState(state, pid, adminid);
	}

	@Override
	public int updateValid(int valid, String pid) {

		return ptOlDao.updateValid(valid, pid);
	}
	

	@Override
	public List<CustomGoodsBean> getGoodsListByCatid(String catid) {

		return ptOlDao.getGoodsListByCatid(catid);
	}

	@Override
	public int updateInfoList(List<CustomGoodsBean> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}

		return ptOlDao.updateInfoList(list);
	}

	@Override
	public List<CustomGoodsPublish> queryGoodsInfos(CustomGoodsQuery queryBean) {
		return ptOlDao.queryGoodsInfos(queryBean);
	}

	@Override
	public int queryGoodsInfosCount(CustomGoodsQuery queryBean) {
		return ptOlDao.queryGoodsInfosCount(queryBean);
	}

	@Override
	public void batchSaveEnName(Admuser user, List<CustomGoodsBean> cgLst) {
		ptOlDao.batchSaveEnName(user, cgLst);
	}

	@Override
	public CustomGoodsPublish queryGoodsDetails(String pid, int type) {
		return ptOlDao.queryGoodsDetails(pid, type);
	}

	@Override
	public int saveEditDetalis(CustomGoodsPublish cgp, int adminId, int type) {
		return ptOlDao.saveEditDetalis(cgp, adminId, type);
	}
	
	@Override
	public int replaceDetalisImgToLocal(CustomGoodsPublish cgp, int adminId) {
		return ptOlDao.replaceDetalisImgToLocal(cgp,adminId);
	}
	
	@Override
	public boolean batchDeletePids(String[] pidLst) {
		return ptOlDao.batchDeletePids(pidLst);
	}

	@Override
	public boolean publishGoods(String pid,int adminId) {
		return ptOlDao.publishGoods(pid,adminId);
	}
}
