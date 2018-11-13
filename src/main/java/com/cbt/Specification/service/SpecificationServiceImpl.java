package com.cbt.Specification.service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.Specification.bean.SpecificationMapping;
import com.cbt.Specification.bean.SpecificationTranslation;
import com.cbt.Specification.dao.SpecificationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("SpecificationService")
public class SpecificationServiceImpl implements SpecificationService {

	@Resource
    SpecificationMapper specificationMapper;

	@Override
	public List<SpecificationTranslation> queryTranslationByCid(String cid) {
		return specificationMapper.queryTranslationByCid(cid);
	}

	@Override
	public String queryPathByCid(String cid) {
		return specificationMapper.queryPathByCid(cid);
	}

	@Override
	public SpecificationTranslation queryTranslationById(Integer id) {
		return specificationMapper.queryTranslationById(id);
	}

	@Override
	public void insertTranslation(SpecificationTranslation sTranslation) {
		specificationMapper.insertTranslation(sTranslation);
	}

	@Override
	public void updateTranslation(SpecificationTranslation sTranslation) {
		specificationMapper.updateTranslation(sTranslation);
	}

	@Override
	public void deleteTranslation(int id) {
		specificationMapper.deleteTranslation(id);
	}

	@Override
	public List<SpecificationMapping> queryMappingByTranslationId(int translationId) {
		return specificationMapper.queryMappingByTranslationId(translationId);
	}

	@Override
	public SpecificationMapping queryMappingById(Integer id) {
		return specificationMapper.queryMappingById(id);
	}

	@Override
	public void insertMapping(SpecificationMapping sMapping) {
		specificationMapper.insertMapping(sMapping);
	}

	@Override
	public void updateMapping(SpecificationMapping sMapping) {
		specificationMapper.updateMapping(sMapping);
	}

	@Override
	public List<AliCategory> queryAliCategoryByLvOne() {
		return specificationMapper.queryAliCategoryByLvOne();
	}

	@Override
	public List<AliCategory> queryAliCategoryByCidAndLv(String cid, int lv) {
		return specificationMapper.queryAliCategoryByCidAndLv(cid, lv);
	}

	@Override
	public void deleteMappingById(int id) {
		specificationMapper.deleteMappingById(id);

	}

	@Override
	public void deleteMappingByTranslationId(int translationId) {
		specificationMapper.deleteMappingByTranslationId(translationId);
	}

	@Override
	public List<String> queryTranslationEnName() {
		return specificationMapper.queryTranslationEnName();
	}

	@Override
	public void updateSpecificationByEnName(String chName, String enName) {
		specificationMapper.updateSpecificationByEnName(chName, enName);

	}

	@Override
	public List<String> queryMappingEnName() {
		return specificationMapper.queryMappingEnName();
	}

	@Override
	public void updateMappingByEnName(String chName, String enName) {
		specificationMapper.updateMappingByEnName(chName, enName);

	}

}
