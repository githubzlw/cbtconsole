package com.cbt.service.impl;

import com.cbt.bean.BusiessBean;
import com.cbt.dao.BusiessDao;
import com.cbt.service.BusiessService;
import com.importExpress.mapper.CustomizedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BusiessServiceImpl implements BusiessService {
	
	@Autowired
	private BusiessDao busiessDao;
	@Autowired
	private CustomizedMapper customizedMapper;
	
	@Override
	public int saveBusiess(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return busiessDao.saveBusiess(params);
	}

	@Override
	public List<BusiessBean> getBusiessPage(BusiessBean busiessBean, int pagenum) {
		// TODO Auto-generated method stub
		return busiessDao.getBusiessPage(busiessBean,pagenum);
	}

	@Override
	public int getBusiessCountPage(BusiessBean busiessBean) {
		// TODO Auto-generated method stub
		return busiessDao.getBusiessCountPage(busiessBean);
	}

	@Override
	public int deleteBusiess(int id) {
		int res = busiessDao.deleteBusiess(id);
		
		//start add by yang_tao for 更改服装定制信息状态
		BusiessBean bean = busiessDao.getById(id);
		if(null != bean && null != bean.getCustomizedId() && 0 != bean.getCustomizedId().intValue()){
			customizedMapper.updateStatus(bean.getCustomizedId(), "1");
		}
		//end add by yang_tao for 更改服装定制信息状态
		return res;
		
	}

	@Override
	public int queryByChoiceCountPage(BusiessBean busiessBean, int type) {
		return busiessDao.queryByChoiceCountPage(busiessBean, type);
	}

	@Override
	public List<BusiessBean> queryByChoice(BusiessBean busiessBean, int pagenum, int type) {
		return busiessDao.queryByChoice(busiessBean, pagenum, type);
	}
	
}
