package com.cbt.Specification.service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.Specification.bean.SpecificationMapping;
import com.cbt.Specification.bean.SpecificationTranslation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecificationService {

	/**
	 * 根据Cid查询所有的规格翻译数据
	 * 
	 * @return
	 */
	public List<SpecificationTranslation> queryTranslationByCid(String cid);

	/**
	 * 根据cid查询cid的path
	 * 
	 * @param cid
	 * @return
	 */
	public String queryPathByCid(String cid);

	/**
	 * 根据id查询规格翻译数据
	 * 
	 * @param id
	 * @return
	 */
	public SpecificationTranslation queryTranslationById(Integer id);

	/**
	 * 插入sTranslation数据
	 * 
	 * @param sTranslation
	 */
	public void insertTranslation(SpecificationTranslation sTranslation);

	/**
	 * 修改sTranslation数据
	 * 
	 * @param sTranslation
	 */
	public void updateTranslation(SpecificationTranslation sTranslation);

	/**
	 * 根据TranslationId查询所有的规格映射数据
	 * 
	 * @return
	 */
	public List<SpecificationMapping> queryMappingByTranslationId(int translationId);

	/**
	 * 根据id查询规格映射数据
	 * 
	 * @param id
	 * @return
	 */
	public SpecificationMapping queryMappingById(Integer id);

	/**
	 * 插入sMapping数据
	 * 
	 * @param sMapping
	 */
	public void insertMapping(SpecificationMapping sMapping);

	/**
	 * 修改sMapping数据
	 * 
	 * @param sMapping
	 */
	public void updateMapping(SpecificationMapping sMapping);

	/**
	 * 根据id删除翻译数据
	 * 
	 * @param id
	 */
	void deleteTranslation(int id);

	/**
	 * 查询第一等级的商品类别
	 * 
	 * @return
	 */
	public List<AliCategory> queryAliCategoryByLvOne();

	/**
	 * 根据cid和lv查询商品类别
	 * 
	 * @param cid
	 * @param lv
	 * @return
	 */
	public List<AliCategory> queryAliCategoryByCidAndLv(@Param("cid") String cid, @Param("lv") int lv);

	/**
	 * 根据id删除映射表的数据
	 * 
	 * @param id
	 */
	public void deleteMappingById(int id);

	/**
	 * 根据translationId翻译的id删除数据
	 * 
	 * @param translationId
	 */
	public void deleteMappingByTranslationId(int translationId);

	/**
	 * 查询全部规格英文名
	 * 
	 * @return
	 */
	public List<String> queryTranslationEnName();

	/**
	 * 根据规格英文名修改翻译的中文
	 * 
	 * @param chName
	 * @param enName
	 */
	public void updateSpecificationByEnName(@Param("chName") String chName, @Param("enName") String enName);

	/**
	 * 查询全部规格属性英文名
	 * 
	 * @return
	 */
	public List<String> queryMappingEnName();

	/**
	 * 根据规格属性英文名修改翻译的中文
	 * 
	 * @param chName
	 * @param enName
	 */
	public void updateMappingByEnName(@Param("chName") String chName, @Param("enName") String enName);

}
