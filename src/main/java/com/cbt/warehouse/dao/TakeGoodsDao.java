package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.AliCategoryPojo;
import com.cbt.warehouse.pojo.ExpressRecord;
import com.cbt.warehouse.pojo.TbGoodsSampleDetailsPojo;
import com.cbt.warehouse.pojo.TbGoodsSamplePojo;
import com.cbt.website.bean.SampleGoodsBean;

import java.util.List;
import java.util.Map;

public interface TakeGoodsDao{
	//获得所有样品
	public List<TbGoodsSamplePojo> getTbGoodsSample(Map<String, Object> map);

	//批量导入
	int batchImportTbGSD(Map<String, Object> map);

	//记录扫描快递
	int insertExpressRecord(Map<String, Object> map);

	//记录扫描快递数量
	int getCountExpressRecord(Map<String, Object> map);

	public List getidRelationtable(String orderid);

	//删除样品下所有商品
	int delteGoodsSampleDetails(Map<String, Object> map);

	//读取样品
	public SampleGoodsBean getGoodsDataById(Map<String, Object> map);

	//获得扫描快递记录
	public List<ExpressRecord> getExpressRecord(Map<String, Object> map);

	//获得单件样品
	public TbGoodsSamplePojo getTbGoodsSampleById(Map<String, Object> map);

	//插入新样品
	public int insertTbGoodsSample(Map<String, Object> map);

	//获得样品所有商品
	public List<TbGoodsSampleDetailsPojo> getTbGoodsSampleDetails(Map<String, Object> map);

	//删除单件商品
	public int delteCommodityByid(Map<String, Object> map);

	//删除单件样品
	public int deleteTbGoodsSample(Map<String, Object> map);


	//修改单件样品
	public int updateTbGoodsSampleByid(Map<String, Object> map);

	//修改单件商品
	public int updateTbGoodsSampleDetailsByid(Map<String, Object> map);

	//获得单件商品
	public TbGoodsSampleDetailsPojo getTbGoodsSampleDetailsById(Map<String, Object> map);

	//插入新商品
	public int insertTbGoodsSampleDetails(Map<String, Object> map);

	//读取样品类型
	public List<AliCategoryPojo> getAliCategory(Map<String, Object> map);
	
	
} 
