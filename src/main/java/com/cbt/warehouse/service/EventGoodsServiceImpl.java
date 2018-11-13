package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.EventGoodsDao;
import com.cbt.warehouse.pojo.AliCategoryPojo;
import com.cbt.warehouse.pojo.EventGoodsDetailsPojo;
import com.cbt.warehouse.pojo.EventGoodsPojo;
import com.cbt.website.bean.EventGoodsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EventGoodsServiceImpl implements EventGoodsService {

	@Autowired
	private EventGoodsDao dao;
	
	/**
	 * getTbGoodsSample  获得所有样品
	 */
	@Override
	public List<EventGoodsPojo> getTbGoodsSample(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.getTbGoodsSample(map);
	}

	//获得单件样品
	@Override
	public EventGoodsPojo getTbGoodsSampleById(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.getTbGoodsSampleById(map);
	}

	@Override
	public int insertTbGoodsSample(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("category", map.get("category").toString().replaceAll("'", "&apos;"));
		map.put("title", map.get("title").toString().replaceAll("'", "&apos;"));
		return dao.insertTbGoodsSample(map);
	}

	@Override
	public List<EventGoodsDetailsPojo> getTbGoodsSampleDetails(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.getTbGoodsSampleDetails(map);
	}

	@Override
	public int delteCommodityByid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.delteCommodityByid(map);
	}

	@Override
	public int updateTbGoodsSampleByid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.updateTbGoodsSampleByid(map);
	}

	@Override
	public EventGoodsDetailsPojo getTbGoodsSampleDetailsById(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.getTbGoodsSampleDetailsById(map);
	}

	@Override
	public int updateTbGoodsSampleDetailsByid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.updateTbGoodsSampleDetailsByid(map);
	}

	@Override
	public int insertTbGoodsSampleDetails(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("goodsname", map.get("goodsname").toString().replaceAll("'", "&apos;"));
		return dao.insertTbGoodsSampleDetails(map);
	}

	@Override
	public EventGoodsBean getGoodsDataById(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.getGoodsDataById(map);
	}

	@Override
	public List<AliCategoryPojo> getAliCategory(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.getAliCategory(map);
	}

	@Override
	public int deleteTbGoodsSample(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.deleteTbGoodsSample(map);
	}

//	public int batchImportTbGSD(Map<String, Object> map) {
//		// TODO Auto-generated method stub
//		return dao.batchImportTbGSD(map);
//	}

//	@Override
//	public List<ExpressRecord> getExpressRecord(Map<String, Object> map) {
//		// TODO Auto-generated method stub
//		return dao.getExpressRecord(map);
//	}

//	@Override
//	public int insertExpressRecord(Map<String, Object> map) {
//		// TODO Auto-generated method stub
//		return dao.insertExpressRecord(map);
//	}
//
//	@Override
//	public int getCountExpressRecord(Map<String, Object> map) {
//		// TODO Auto-generated method stub
//		return dao.getCountExpressRecord(map);
//	}
//	
//	@Override
//	public List getidRelationtable(String orderid) {
//		
//		return dao.getidRelationtable(orderid);
//	}

	@Override
	public int delteGoodsSampleDetails(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.delteGoodsSampleDetails(map);
	}
}
