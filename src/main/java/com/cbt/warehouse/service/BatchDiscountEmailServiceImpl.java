package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.BatchDiscountEmailMapper;
import com.cbt.warehouse.pojo.BatchDiscountEmail;
import com.cbt.warehouse.pojo.BatchDiscountEmailDetails;
import com.cbt.warehouse.pojo.BatchDiscountPurchasPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchDiscountEmailServiceImpl implements BatchDiscountEmailService {

	@Autowired
	private BatchDiscountEmailMapper batchDiscountEmailDao;

	@Override
	public List<BatchDiscountEmail> queryEmailList(String orderNo, int adminId, int userId, String flag,
			String beginDate, String endDate, int stateNum, int showNum) {
		return batchDiscountEmailDao.queryEmailList(orderNo, adminId, userId, flag, beginDate, endDate, stateNum,
				showNum);
	}

	@Override
	public int queryEmailListCount(String orderNo, int adminId, int userId, String flag, String beginDate,
			String endDate) {
		return batchDiscountEmailDao.queryEmailListCount(orderNo, adminId, userId, flag, beginDate, endDate);
	}

	@Override
	public BatchDiscountEmail queryEmailByOrderNo(String orderNo) {
		return batchDiscountEmailDao.queryEmailByOrderNo(orderNo);
	}

	@Override
	public List<BatchDiscountEmailDetails> queryDetailsList(String orderNo) {
		return batchDiscountEmailDao.queryDetailsList(orderNo);
	}

	@Override
	public List<BatchDiscountPurchasPrice> queryPurchasPriceList(int goodsId) {
		return batchDiscountEmailDao.queryPurchasPriceList(goodsId);
	}

	@Override
	public int updatePurchasPrice(BatchDiscountPurchasPrice purchasPrice) {
		return batchDiscountEmailDao.updatePurchasPrice(purchasPrice);
	}

	@Override
	public int insertEmail(String orderNo) {
		return batchDiscountEmailDao.insertEmail(orderNo);
	}

	@Override
	public int insertEmailDetails(String orderNo) {
		return batchDiscountEmailDao.insertEmailDetails(orderNo);
	}

	@Override
	public int updateDefaultPurchasPrice(String orderNo) {
		return batchDiscountEmailDao.updateDefaultPurchasPrice(orderNo);
	}

	@Override
	public int buildRelationshipsByOrderNo(String orderNo) {
		return batchDiscountEmailDao.buildRelationshipsByOrderNo(orderNo);
	}

	@Override
	public int insertPurchasPrice(String orderNo) {
		return batchDiscountEmailDao.insertPurchasPrice(orderNo);
	}

	@Override
	public boolean checkIsPurchasPrice(String orderNo) {
		return batchDiscountEmailDao.checkIsPurchasPrice(orderNo) > 0;
	}

	@Override
	public int updateEmailDetailsFreeShippingPrice(String orderNo, int goodsId, float freeShippingPrice) {
		if (freeShippingPrice > 0) {
			return batchDiscountEmailDao.updateEmailDetailsFreeShippingPrice(orderNo, goodsId, freeShippingPrice);
		} else {
			return 0;
		}

	}

	@Override
	public void updateValidByGoodsId(String orderNo, int goodsId, int minQuantify, int maxQuantify) {
		batchDiscountEmailDao.updateEmailDetailsValid(orderNo, goodsId);
		batchDiscountEmailDao.updatePurchasPriceValid(orderNo, goodsId, minQuantify, maxQuantify);

	}

	@Override
	public int batchUpdateEmailDetailsValid(List<Integer> uedLst,String orderNo) {
		batchDiscountEmailDao.UpdateEmailDetailsDefaultValid(orderNo);
		return batchDiscountEmailDao.batchUpdateEmailDetailsValid(uedLst);
	}

	@Override
	public int batchUpdatePurchasPrice(List<BatchDiscountPurchasPrice> uppLst) {
		return batchDiscountEmailDao.batchUpdatePurchasPrice(uppLst);
	}

	@Override
	public int updateEmailFlagByOrderNo(String orderNo, String flag) {
		return batchDiscountEmailDao.updateEmailFlagByOrderNo(orderNo, flag);
	}

}
