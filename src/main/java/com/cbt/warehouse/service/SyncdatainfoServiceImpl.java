package com.cbt.warehouse.service;

import com.cbt.pojo.Syncdatainfo;
import com.cbt.pojo.SyncdatainfoExample;
import com.cbt.warehouse.dao.SyncdatainfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**   
 * @Title : SyncdatainfoServiceImpl.java 
 * @Description : TODO
 * @Company : www.importExpress.com
 * @author : 柒月
 * @date : 2016年11月18日
 * @version : V1.0   
 */

@Service("SyncdatainfoService")
public class SyncdatainfoServiceImpl implements SyncdatainfoService {
	
	@Autowired
	private SyncdatainfoMapper syncdatainfoMapper;

	@Override
	public int addSyncdatainfo(Syncdatainfo syncdatainfo) {
		return syncdatainfoMapper.insertSelective(syncdatainfo);
	}

	@Override
	public List<Syncdatainfo> getSyncdatainfoList() {
		SyncdatainfoExample example = new SyncdatainfoExample();
		return syncdatainfoMapper.selectByExample(example);
	}

}
