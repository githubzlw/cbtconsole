package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.MabangshipmentMapper;
import com.cbt.warehouse.pojo.Mabangshipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**   
 * @Title: MabangshipmentServiceImpl.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年7月22日
 * @version V1.0   
 */

@Service
public class MabangshipmentServiceImpl implements MabangshipmentService {
	
	@Autowired
	private MabangshipmentMapper mabangshipmentMapper;

	@Override
	public int insertMabangShipment(List<Mabangshipment> list) {
		return mabangshipmentMapper.insertMabangShipment(list);
	}

	//查询到货情况
    public List<Mabangshipment> selectInstorage(Mabangshipment mb){
    	return mabangshipmentMapper.selectInstorage(mb);
    }
    //查询到货情况数量
    public int selectInstorageCount(Mabangshipment mb){
    	return mabangshipmentMapper.selectInstorageCount(mb);
    }
    
  //查询采购成本
    public List<Mabangshipment> selectPurchaseCost(Mabangshipment mb){
    	return mabangshipmentMapper.selectPurchaseCost(mb);
    }

	@Override
	public int insertMabangShipment(Mabangshipment mabangshipment) {
		return mabangshipmentMapper.insertSelective(mabangshipment);
	}
    
}
