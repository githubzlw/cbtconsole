package com.cbt.redNet.service.impl;

import com.cbt.redNet.dao.RedNetMapper;
import com.cbt.redNet.pojo.redNet;
import com.cbt.redNet.pojo.redNetStatistics;
import com.cbt.redNet.service.RedNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedNetServiceImpl implements RedNetService {

	@Autowired
	private RedNetMapper redNetMapper;
	
	@Override
	public List<redNetStatistics> showRedNetStatistics(String redNetId,
                                                       String pushTime, String startTime, String endTime, int page) {
		// TODO Auto-generated method stub
		page = (page-1)*30;
		List<redNetStatistics>  list = redNetMapper.showRedNetStatistics(redNetId,pushTime,startTime,endTime,page);
		int  count = 0 ;
		if(list.size()>0){
			count = redNetMapper.CountRedNetStatistics(redNetId,pushTime,startTime,endTime);
			list.get(0).setCount(count);
		}
		return  list;
	}

	@Override
	public List<redNet> showRedNetInfo(String redNetId, String redNetName,
                                       String site , int page) {
		// TODO Auto-generated method stub
		page = (page-1)*30;
	    List<redNet>  list = redNetMapper.showRedNetInfo(redNetId,redNetName,site,page);
	    int  count = 0 ;
	    if(list.size()>0){
	    	count = redNetMapper.CountRedNetInfo(redNetId,redNetName,site);
	    	list.get(0).setCount(count);
	    }
		return  list ;
	}

	@Override
	public List<redNet> showAllRedNet() {
		// TODO Auto-generated method stub
		return redNetMapper.showAllRedNet();
	}

	@Override
	public int addRedNet(redNet bean) {
		// TODO Auto-generated method stub
		return redNetMapper.addRedNet(bean);
	}

	@Override
	public redNet showRedNetById(int id) {
		// TODO Auto-generated method stub
		return redNetMapper.showRedNetById(id);
	}

	@Override
	public int addRedNetStatistics(redNetStatistics bean) {
		// TODO Auto-generated method stub
		return redNetMapper.addRedNetStatistics(bean);
	}

	@Override
	public int selectRedNetByName(String redNetName) {
		// TODO Auto-generated method stub
		return redNetMapper.selectRedNetByName(redNetName);
	}

	@Override
	public int selectRedNetStatistics(String shareId) {
		// TODO Auto-generated method stub
		return redNetMapper.selectRedNetStatistics(shareId);
	}

	@Override
	public int updateRedNet(redNet bean) {
		// TODO Auto-generated method stub
		return redNetMapper.updateRedNet(bean);
	}

	@Override
	public redNetStatistics showRedNetStatisticsById(int id) {
		// TODO Auto-generated method stub
		return   redNetMapper.showRedNetStatisticsById(id);
	}

	@Override
	public int updateRedNetStatistics(redNetStatistics bean) {
		// TODO Auto-generated method stub
		return  redNetMapper.updateRedNetStatistics(bean);
	}

	
	
}
