package com.cbt.Specification.dao;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.Specification.bean.SpecificationMapping;
import com.cbt.Specification.bean.SpecificationTranslation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecificationMapper {

	/**
	 * 根据cid查询符合的规格翻译数据
	 * 
	 * @param cid
	 * @return
	 */
	List<SpecificationTranslation> queryTranslationByCid(String cid);

	/**
	 * 根据cid查询cid的path
	 * 
	 * @param cid
	 * @return
	 */
	String queryPathByCid(String cid);

	/**
	 * 根据id查询规格翻译数据
	 * 
	 * @param id
	 * @return
	 */
	SpecificationTranslation queryTranslationById(Integer id);

	/**
	 * 插入sTranslation数据
	 * 
	 * @param sTranslation
	 */
	void insertTranslation(SpecificationTranslation sTranslation);

	/**
	 * 修改sTranslation数据
	 * 
	 * @param sTranslation
	 */
	void updateTranslation(SpecificationTranslation sTranslation);

	/**
	 * 根据id删除翻译数据
	 * 
	 * @param id
	 */
	void deleteTranslation(int id);

	/**
	 * 根据translationId查询符合的规格映射数据
	 * 
	 * @param translationId
	 * @return
	 */
	List<SpecificationMapping> queryMappingByTranslationId(int translationId);

	/**
	 * 根据id查询规格映射数据
	 * 
	 * @param id
	 * @return
	 */
	SpecificationMapping queryMappingById(Integer id);

	/**
	 * 插入sMapping数据
	 * 
	 * @param sMapping
	 */
	void insertMapping(SpecificationMapping sMapping);

	/**
	 * 修改sMapping数据
	 * 
	 * @param sMapping
	 */
	void updateMapping(SpecificationMapping sMapping);

	/**
	 * 查询第一等级的商品类别
	 * 
	 * @return
	 */
	List<AliCategory> queryAliCategoryByLvOne();

	/**
	 * 根据cid和lv查询商品类别
	 * 
	 * @param cid
	 * @param lv
	 * @return
	 */
	List<AliCategory> queryAliCategoryByCidAndLv(@Param("cid") String cid, @Param("lv") int lv);

	/**
	 * 根据id删除映射表的数据
	 * 
	 * @param id
	 */
	void deleteMappingById(int id);

	/**
	 * 根据translationId翻译的id删除数据
	 * 
	 * @param translationId
	 */
	void deleteMappingByTranslationId(int translationId);

	/**
	 * 查询全部规格英文名
	 * 
	 * @return
	 */
	List<String> queryTranslationEnName();

	/**
	 * 根据规格英文名修改翻译的中文
	 * 
	 * @param chName
	 * @param enName
	 */
	void updateSpecificationByEnName(@Param("chName") String chName, @Param("enName") String enName);

	/**
	 * 查询全部规格属性英文名
	 * 
	 * @return
	 */
	List<String> queryMappingEnName();

	/**
	 * 根据规格属性英文名修改翻译的中文
	 * 
	 * @param chName
	 * @param enName
	 */
	void updateMappingByEnName(@Param("chName") String chName, @Param("enName") String enName);

}
