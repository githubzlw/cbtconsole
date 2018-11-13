package com.cbt.website.service;

import com.cbt.bean.GoodsCheckBean;
import com.cbt.website.bean.AliExpressTop240Bean;
import com.cbt.website.bean.GoodsSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IAliExpress240Sercive {

	/*public int saveAliExpress240(List<AliExpressTop240Bean> aliExpressTop240Beans, String keyword, String typeid, int results_typeid);*/
	
	public List<AliExpressTop240Bean> getAliExpress240s(String typeId, String keyword);
	
	/**
	 * 保存处理的搜索链接， 关键词，类别ID
	 * ylm
	 * 	@param keyword,typeid,pagenum,search_number	
	 */
	public int saveSearch240_type(String keyword, String typeid, int pagenum);

	/**
	 * 查询需要处理的关键词和类别是否已处理过
	 * ylm
	 * @param keyword,typeid
	 */
	public List<String[]> getSearch240_type(List<String[]> search240_type);


	/**查询关键词和类别是否存在数据集合
	 * @param keyword
	 * @param typeid
	 * @return
	 */
	public HashMap<String,String> getSearch240_typeCount(String keyword, String typeid);


	/**查询数据集合
	 * @param typeId
	 * @param keyword
	 * @param sort
	 * @return
	 */
	public ArrayList<AliExpressTop240Bean> getAliExpress240(int results_typeid, String sort, int page);

	/**
	 * 获取所有处理过的关键词和类别
	 * ylm
	 * @param keyword,typeid
	 */
	public List<String[]> getSearch240_type();
	
	/**
	 * 获取淘宝图片搜索结果
	 * ylm
	 * @param id tab_top240results_type中的主键
	 */
	public List<GoodsCheckBean> getGoodsCheckBeans(int id);
	
	/**
	 * 保存选择的对应货源地址
	 * ylm
	 * @param id tab_top240results_type中的主键
	 */
	public String saveGoodsSourch(int search_number, int typeId, String id);
	
	/**
	 * 增加货源表
	 * ylm
	 * @param state-原搜索商品有无货源
	 */
	public int addGoodsSource(GoodsSource bean, int typeId, int state, int gid);
}
