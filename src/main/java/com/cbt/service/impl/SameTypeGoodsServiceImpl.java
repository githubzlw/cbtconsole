package com.cbt.service.impl;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.bean.SameTypeGoodsBean;
import com.cbt.dao.SameTypeGoodsDao;
import com.cbt.dao.impl.SameTypeGoodsDaoImpl;
import com.cbt.pojo.Admuser;
import com.cbt.service.SameTypeGoodsService;
import com.cbt.website.util.JsonResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SameTypeGoodsServiceImpl implements SameTypeGoodsService {

	private SameTypeGoodsDao stGdDao = new SameTypeGoodsDaoImpl();

	@Override
	public JsonResult batchAddUrl(String mainUrl, String urls, int adminId, int typeFlag, double aveWeight) {
		return stGdDao.batchAddUrl(mainUrl, urls, adminId, typeFlag, aveWeight);
	}

	@Override
	public JsonResult batchAddTypeUrl(String mainUrl, String urls, int adminId, int typeFlag, double aveWeight) {
		return stGdDao.batchAddTypeUrl(mainUrl, urls, adminId, typeFlag, aveWeight);
	}

	@Override
	public List<CustomOnlineGoodsBean> queryDealGoods() {
		return stGdDao.queryDealGoods();
	}

	@Override
	public List<SameTypeGoodsBean> queryForList(int type, int adminId, int start, int limitNum) {
		return stGdDao.queryForList(type, adminId, start, limitNum);
	}

	@Override
	public int queryForListCount(int type, int adminId) {
		return stGdDao.queryForListCount(type, adminId);
	}

	@Override
	public List<SameTypeGoodsBean> queryListByMainPid(String mainPid) {
		return stGdDao.queryListByMainPid(mainPid);
	}

	@Override
	public List<Admuser> queryAllAdmin() {
		return stGdDao.queryAllAdmin();
	}

	@Override
	public boolean deleteGoodsByMainPid(String mainPid) {
		return stGdDao.deleteGoodsByMainPid(mainPid);
	}

	@Override
	public boolean deleteGoodsByPid(String mainPid, String pid) {
		return stGdDao.deleteGoodsByPid(mainPid, pid);
	}

	@Override
	public boolean replaceGoodsMainPid(String newPid, String oldPid) {
		return stGdDao.replaceGoodsMainPid(newPid, oldPid);
	}

	@Override
	public boolean useGoodsByState(int state, String pids) {
		return stGdDao.useGoodsByState(state, pids);
	}

}
