package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.Skuinfo;

import java.util.List;

/**   
 * @Title: SkuinfoService.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年7月25日
 * @version V1.0   
 */
public interface SkuinfoService {

	/**
	 * 添加马帮sku列表
	 * @param list
	 * @return
	 */
	int insertSkuinfo(List<Skuinfo> list);
	
	public List<Skuinfo>  selectShipImgLinkBySKU(Skuinfo sku);
	
}
