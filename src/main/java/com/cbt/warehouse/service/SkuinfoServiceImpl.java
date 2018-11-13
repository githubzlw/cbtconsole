package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.SkuinfoMapper;
import com.cbt.warehouse.pojo.Skuinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**   
 * @Title: SkuinfoServiceImpl.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年7月25日
 * @version V1.0   
 */

@Service
public class SkuinfoServiceImpl implements SkuinfoService {
	
	
	@Autowired
	private SkuinfoMapper skuinfoMapper;

	@Override
	public int insertSkuinfo(List<Skuinfo> list) {
		return skuinfoMapper.insertSkuinfo(list);
	}
	
	public List<Skuinfo> selectShipImgLinkBySKU(Skuinfo sku){
		return skuinfoMapper.selectShipImgLinkBySKU(sku);
	}

}
