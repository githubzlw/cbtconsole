package com.cbt.website.dao;

import com.cbt.bean.GoodsCheckBean;
import com.cbt.website.bean.AliExpressTop240Bean;
import com.cbt.website.bean.GoodsSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IAliExpress240Dao {

	/*public int saveAliExpress240(List<AliExpressTop240Bean> aliExpressTop240Beans, String keyword, String typeid, int results_typeid);*/
	
	public List<AliExpressTop240Bean> getAliExpress240s(String typeId, String keyword);
	
	public ArrayList<AliExpressTop240Bean> getAliExpress240(int results_typeid, String sort, int page);

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

	/**
	 * 获取所有处理过的关键词和类别
	 * ylm
	 * @param keyword,typeid
	 */
	public List<String[]> getSearch240_type();
	
	/**
	 * 修改搜索过滤表的有无货源状态
	 * ylm
	 * @param keyword,typeid
	 */
	public int upSearch240(List<Integer> id, int type);
	
	/**
	 * 修改搜索过滤表的图片下载
	 * ylm
	 * @param keyword,typeid
	 */
	public int upSearch240_img(String url, String img);
	
	/**
	 * 获取淘宝图片搜索结果
	 * ylm
	 * @param id tab_top240results_type中的主键
	 */
	public List<GoodsCheckBean> getGoodsCheckBeans(int id);
	
	/**
	 * 修改tab_top240results_type表的search_number有货商品数量examinetime审查时间
	 * ylm
	 * @param keyword,typeid
	 */
	public int upSearch240_type(int number, int gid);
	
	/**
	 * 增加货源表
	 * ylm
	 * @param keyword,typeid
	 */
	public int addGoodsSource(List<GoodsSource> bean);
	
	/**
	 * 增加货源表
	 * ylm
	 * @param keyword,typeid
	 */
	public int addGoodsSource(GoodsSource bean);
	
	/**
	 * 修改tab_top240results_type表的search_number有货商品数量
	 * ylm
	 * @param keyword,typeid
	 */
	public int upSearch240_type_number(int gid);
}
