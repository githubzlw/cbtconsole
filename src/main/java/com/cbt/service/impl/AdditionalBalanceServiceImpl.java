package com.cbt.service.impl;

import com.cbt.bean.BalanceBean;
import com.cbt.dao.AdditionalBalanceDao;
import com.cbt.dao.impl.AdditionalBalanceDaoImpl;
import com.cbt.service.AdditionalBalanceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdditionalBalanceServiceImpl implements AdditionalBalanceService {
	private AdditionalBalanceDao additionalBalanceDao = new AdditionalBalanceDaoImpl();

	@Override
	public int insert(BalanceBean bean) {
		
		return additionalBalanceDao.insert(bean);
	}
	@Override
	public int insertForRefund(BalanceBean bean) {
		
		return additionalBalanceDao.insertForRefund(bean);
	}

	@Override
	public List<BalanceBean> getBalanceByUserId(Integer userId, int page) {

		return additionalBalanceDao.getBalanceByUserId(userId,page);
	}

	@Override
	public double getMoneyAmount(int userId) {

		return additionalBalanceDao.getMoneyAmount(userId);
	}

	@Override
	public Map<String, String> getBalanceByUserIds(
			List<Integer> userIds) {
		
		return additionalBalanceDao.getBalanceByUserIds(userIds);
	}
	@Override
	public int updateStateById(int id, int state) {
		
		return additionalBalanceDao.updateStateById(id, state);
	}
	@Override
	public double getMoneyAmountByCid(String cid) {
		
		return additionalBalanceDao.getMoneyAmountByCid(cid);
	}
	@Override
	public Map<String, Double> getMoneyAmountByCids(String cidList) {
		return additionalBalanceDao.getMoneyAmountByCids(cidList);
	}
	
	
	

}
