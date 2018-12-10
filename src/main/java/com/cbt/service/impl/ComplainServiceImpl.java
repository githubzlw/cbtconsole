package com.cbt.service.impl;

import com.cbt.bean.Complain;
import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;
import com.cbt.bean.ComplainVO;
import com.cbt.dao.IComplainDao;
import com.cbt.pojo.page.Page;
import com.cbt.service.IComplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplainServiceImpl implements IComplainService{

	@Autowired
	private IComplainDao complainDao;


	@Override
	public ComplainVO getComplainByCid(Integer cid) {
		// TODO Auto-generated method stub
		return complainDao.getComplainByCid(cid);
	}


	@Override
	public Integer afterResponse(Integer complainid, Integer chatid, Integer cfid) {
		// TODO Auto-generated method stub
		return complainDao.afterResponse(complainid, chatid, cfid);
	}

	@Override
	public Integer responseCustomer(ComplainChat t, String dealAdmin, Integer dealAdminId, Integer complainid) {
		return complainDao.responseCustomer(t, dealAdmin, dealAdminId,complainid);
	}

	@Override
	public List<ComplainVO> getCommunicatingByCidBG(Integer complainId) {
		// TODO Auto-generated method stub
		return complainDao.getCommunicatingByCidBG(complainId);
	}

	@Override
	public Integer closeComplain(Integer complainid) {
		// TODO Auto-generated method stub
		return complainDao.closeComplain(complainid);
	}

	@Override
	public List<ComplainFile> getImgsByCid(Integer complainId) {
		// TODO Auto-generated method stub
		return complainDao.getImgsByCid(complainId);
	}

	@Override
	public Page<ComplainVO> searchComplainByParam(Complain t, String username,Page page,String admName,int roleType,int check) {
		// TODO Auto-generated method stub
		return complainDao.searchComplainByParam(t, username,page,admName,roleType,check);
	}
	
	@Override
	public int updateGoodsid(int id, String orderid, String goodsid) {
		// TODO Auto-generated method stub
		return complainDao.updateGoodsid(id, orderid, goodsid);
	}
	@Override
	public int updateDisputeid(int id,String disputeid,String merchantid) {
		// TODO Auto-generated method stub
		return complainDao.updateDisputeid(id, disputeid,merchantid);
	}


	@Override
	public List<ComplainVO> getComplainByDisputeId(List<String> disputeIdList) {
		if(disputeIdList == null || disputeIdList.isEmpty()) {
			return null;
		}
		// TODO Auto-generated method stub
		return complainDao.getComplainByDisputeId(disputeIdList);
	}


	@Override
	public List<ComplainVO> getComplainByUserId(String userId) {
		// TODO Auto-generated method stub
		return complainDao.getComplainByUserId(userId);
	}
	
	

}
