package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.Mabangshipment;

import java.util.List;

/**   
 * @Title: MabangshipmentService.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年7月22日
 * @version V1.0   
 */
public interface MabangshipmentService {
	
	/**
	 * 添加马帮运单列表
	 * @param list
	 * @return
	 */
	int insertMabangShipment(List<Mabangshipment> list);
	
	int insertMabangShipment(Mabangshipment mabangshipment);
	
	//查询到货情况
    List<Mabangshipment> selectInstorage(Mabangshipment mb);
    //查询到货情况数量
    int selectInstorageCount(Mabangshipment mb);
  //查询采购成本
    List<Mabangshipment> selectPurchaseCost(Mabangshipment mb);

}
