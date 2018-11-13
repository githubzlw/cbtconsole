package com.cbt.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.cbt.dao.OffShelfDao;
import com.cbt.dao.impl.OffShelfDaoImpl;
import com.cbt.service.OffShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffShelfServiceImpl implements OffShelfService {

	@Autowired
	private OffShelfDao offShelfDao;
	
	@Override
	public Integer updateByPid(String pid, Integer unsellableReason) {
		//1688商品货源变更 直接下架
		if (null != unsellableReason && unsellableReason == 14) {
			return offShelfDao.updateValidAndUnsellableReasonByPidJDBC(pid, 0, unsellableReason);
		}
		//重复验证合格 直接上架
		if (null != unsellableReason && unsellableReason == 5) {
			return offShelfDao.updateValidAndUnsellableReasonByPidJDBC(pid, 1, unsellableReason);
		}
		Integer valid = null;
		//查询是否有库存
		Integer remaining = offShelfDao.queryRemainingByPid(pid);
		if (null != remaining && remaining > 0) {
			// 有库存 只更新 unsellableReason字段 不更新valid字段
			return offShelfDao.updateValidAndUnsellableReasonByPid(pid, valid, unsellableReason) + 10;
		}
		//不需要同款
		switch (unsellableReason) {
			case 3:
			case 5:
			case 12:
			case 13:
				valid = null;
				break;
			case 6:
				valid = 0;
				break;
			case 2:
			case 7:
			case 10:
			case 11:
				valid = 2; 
				break;
			default:
				valid = -1;
				break;
		}
		if (valid == null || valid != -1) {
			//不需要同款的直接更新
			return offShelfDao.updateValidAndUnsellableReasonByPid(pid, valid, unsellableReason);
		}
		
		//需要同款的		//查询同款数据
		Integer sameProduct = offShelfDao.querySameProductIds(pid);
		if (null == sameProduct) {
			sameProduct = 0;
		}
		//对应处理
		switch (unsellableReason) {
			case 1:
			case 4:
				valid = sameProduct>0?2:0;
				break;
			case 8:
			case 9:
				valid = sameProduct>2?2:0;
				break;
			default:
				valid = -1;
				break;
		}
		if (valid == null || valid != -1) {
			return offShelfDao.updateValidAndUnsellableReasonByPid(pid, valid, unsellableReason);
		}
		return 2;
	}
	
	public Integer updateByPidJDBC(String pid, Integer unsellableReason) {
		OffShelfDao offShelfDao = new OffShelfDaoImpl();
		//1688商品货源变更 直接下架
		if (null != unsellableReason && unsellableReason == 14) {
			return offShelfDao.updateValidAndUnsellableReasonByPidJDBC(pid, 0, unsellableReason);
		}
		//重复验证合格 直接上架
		if (null != unsellableReason && unsellableReason == 5) {
			return offShelfDao.updateValidAndUnsellableReasonByPidJDBC(pid, 1, unsellableReason);
		}
		Integer valid = null;
		//查询是否有库存
		Integer remaining = offShelfDao.queryRemainingByPid(pid);
		if (null != remaining && remaining > 0) {
			// 有库存 只更新 unsellableReason字段 不更新valid字段
			return offShelfDao.updateValidAndUnsellableReasonByPidJDBC(pid, valid, unsellableReason) + 10;
		}
		//不需要同款
		switch (unsellableReason) {
			case 3:
			case 12:
			case 13:
				valid = null;
				break;
			case 6:
				valid = 0;
				break;
			case 2:
			case 7:
			case 10:
			case 11:
				valid = 2; 
				break;
			default:
				valid = -1;
				break;
		}
		if (valid == null || valid != -1) {
			//不需要同款的直接更新
			return offShelfDao.updateValidAndUnsellableReasonByPidJDBC(pid, valid, unsellableReason);
		}
		
		//需要同款的		//查询同款数据
		Integer sameProduct = offShelfDao.querySameProductIds(pid);
		if (null == sameProduct) {
			sameProduct = 0;
		}
		//对应处理
		switch (unsellableReason) {
			case 1:
			case 4:
				valid = sameProduct>0?2:0;
				break;
			case 8:
			case 9:
				valid = sameProduct>2?2:0;
				break;
			default:
				valid = -1;
				break;
		}
		if (valid == null || valid != -1) {
			return offShelfDao.updateValidAndUnsellableReasonByPidJDBC(pid, valid, unsellableReason);
		}
		return 2;
	}
	
	@Override
	public void goodsSoftOffShelf() {
		//查询软下架商品
		List<String> list = offShelfDao.queryGoodsSoftOffShelf();
		if (null == list || list.size() < 1) {
			return;
		}
		Integer reason;
		//更新对应数据
		for (String str : list) {
			String[] pidArr = str.split("@");
			if(pidArr == null || pidArr.length != 2){
				continue;
			}
			reason = Integer.valueOf(pidArr[1]);
			if(StringUtils.isEmpty(pidArr[0]) || StringUtils.isEmpty(reason + "")){
				continue;
			}
			//重新更新数据
			updateByPid(pidArr[0],reason);
		}
		
	}


}
