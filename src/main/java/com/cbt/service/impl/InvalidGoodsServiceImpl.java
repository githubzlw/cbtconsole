package com.cbt.service.impl;

import com.cbt.bean.IntensveBean;
import com.cbt.bean.InvalidUrlBean;
import com.cbt.dao.InvalidGoodsDao;
import com.cbt.dao.impl.InvalidGoodsDaoImpl;
import com.cbt.service.InvalidGoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvalidGoodsServiceImpl implements InvalidGoodsService {
	private InvalidGoodsDao invalidGoodsDao = new InvalidGoodsDaoImpl();

	@Override
	public int addFilterGoodsUrl(String goodsUuid,String goodsPid) {
		
		return invalidGoodsDao.addFilterGoodsUrl(goodsUuid,goodsPid);
	}
	@Override
	public int addFilterStoreUrl(String storeUrl,String storeId) {
		
		return invalidGoodsDao.addFilterStoreUrl(storeUrl,storeId);
	}
	

	@Override
	public int updateGoodsUrl(String goodsUuid,String goodsPid) {
		return invalidGoodsDao.updateGoodsUrl(goodsUuid,goodsPid);
	}


	@Override
	public List<InvalidUrlBean> getInvalidUrls(int page,int type) {
		if(page<1){
			return null;
		}
		page = (page-1)*40;
		return invalidGoodsDao.getInvalidUrls(page, type);
	}
	
	@Override
	public List<IntensveBean> getIntensve() {
		return invalidGoodsDao.getIntensve();
	}


}
