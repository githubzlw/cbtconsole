package com.cbt.bigpro.dao;

import com.cbt.bigpro.bean.AliCategoryPojo;
import com.cbt.bigpro.bean.BigGoodsArea;

import java.util.List;


public interface BigGoodsAreaDao {

	//查询大货区商品
	public  List<BigGoodsArea>  getBigGoodsIfo(String id, int page, int pagesize);

	//保存单个大货区商品
	public int save(BigGoodsArea bean);

	//商品下架
    public int  updateBigGoodsArea(String[] pids);

	public int isExistence(String pid);

	//获取类别
	public List<AliCategoryPojo> getAliCategory();

	//获取下级类别
	public List<AliCategoryPojo> getSubType(String id);

	//根据catid 查询商品
	public List<BigGoodsArea> findGoodsByCategoryId(String catid, String catrid1);

	public int delteCommodityByid(String id);
}
