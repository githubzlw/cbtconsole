package com.cbt.searchByPic.service.impl;

import com.cbt.bean.SearchIndex;
import com.cbt.bean.SearchResults;
import com.cbt.searchByPic.bean.CustomerRequireBean;
import com.cbt.searchByPic.dao.SearchByPicMapper;
import com.cbt.searchByPic.service.SearchByPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchByPicServiceImpl implements SearchByPicService {

	@Autowired
	private SearchByPicMapper SearchByPicmapper;
	
	private  DecimalFormat format = new DecimalFormat("#0.00");
	
	@Override
	public List<CustomerRequireBean> selectAll(int parseInt) {
		// TODO Auto-generated method stub
		parseInt =(parseInt-1)*40;
	    List<CustomerRequireBean> selectAll = SearchByPicmapper.selectAll(parseInt);
		int count = SearchByPicmapper.count();
		List<CustomerRequireBean> results = new ArrayList<CustomerRequireBean>();
		for(CustomerRequireBean index:selectAll){
			index.setCount(count);
			results.add(index);
		}
		return results;
	}

	@Override
	public CustomerRequireBean selectByPrimaryKey(int parseInt) {
		// TODO Auto-generated method stub
		return SearchByPicmapper.selectByPrimaryKey(parseInt);
	}

	@Override
	public int insertSelective(SearchIndex searchIndex) {
		// TODO Auto-generated method stub
		if(SearchByPicmapper.countByEnName(searchIndex.getEnName())==0){
			SearchByPicmapper.insertSelective(searchIndex);
			return searchIndex.getId();
		}
		return -1;
	}

	@Override
	public int updateByPrimaryKey(SearchIndex searchIndex) {
		// TODO Auto-generated method stub
		return SearchByPicmapper.updateByEnName(searchIndex);
	}

	@Override
	public List<SearchIndex> selectList(int parseInt, int indexId) {
		// TODO Auto-generated method stub
		parseInt = (parseInt-1)*40;
		List<SearchIndex> selectAll = SearchByPicmapper.selectList(parseInt,indexId);
		int count = SearchByPicmapper.countIndex(indexId);
		List<SearchIndex> results = new ArrayList<SearchIndex>();
		for(SearchIndex index:selectAll){
			index.setCount(count);
			results.add(index);
		}
		return results;
	}

	@Override
	public List<SearchResults> selectByIndexIdAndPrice(int indexId, int page, Double minprice, Double maxprice) {
		// TODO Auto-generated method stub
		page = (page-1)*80;
		List<SearchResults> selectByIndexId = SearchByPicmapper.selectByIndexIdAndPrice(indexId,page,minprice,maxprice);
		int countByIndexId = SearchByPicmapper.countByIndexIdAndPrce(indexId, minprice, maxprice);
		List<SearchResults> results = new ArrayList<SearchResults>();
		for(SearchResults index:selectByIndexId){
			index.setCount(countByIndexId);
			if(index.getGoodsPriceRe()<0.001){
				index.setGoodsPriceRe(Double.valueOf(format.format(index.getGoodsPrice()/6.5*1.2)));
			}
			results.add(index);
		}
		
		return results;
	}

	@Override
	public SearchIndex selectKeyWords(int id) {
		// TODO Auto-generated method stub
		return SearchByPicmapper.selectKeyWords(id);
	}

	@Override
	public List<SearchResults> selectByIndexId(int id, int paged) {
		// TODO Auto-generated method stub
		paged = (paged-1)*80;
		List<SearchResults> selectByIndexId = SearchByPicmapper.selectByIndexId(id,paged);
		int countByIndexId = SearchByPicmapper.countByIndexId(id);
		List<SearchResults> results = new ArrayList<SearchResults>();
		for(SearchResults index:selectByIndexId){
			index.setCount(countByIndexId);
			if(index.getGoodsPriceRe()<0.001){
				index.setGoodsPriceRe(Double.valueOf(format.format(index.getGoodsPrice()/6.5*1.2)));
			}
			results.add(index);
		}
		
		return results;
	}

	@Override
	public int updateValidByPids(int id, String pids, int i) {
		// TODO Auto-generated method stub
		if(pids==null||pids.isEmpty()){
			return 0;
		}
		List<String> list = new ArrayList<String>();
		String[] split = pids.split(",");
		for(String s:split){
			if(!s.isEmpty()){
				list.add(s);
			}
		}
		if(list.isEmpty()){
			return 0;
		}
		return SearchByPicmapper.updateValidByPids(id,list,i);
	}

	@Override
	public int saveListByTempTable(List<SearchResults> list) {
		// TODO Auto-generated method stub
		if(list==null||list.isEmpty()){
			return 0;
		}
		SearchByPicmapper.dropTempTable();
		SearchByPicmapper.createTempTable();
		SearchByPicmapper.insertTempTable(list);
		int updateTemTable = SearchByPicmapper.updateTempTable();
		int addByTempTable = SearchByPicmapper.addByTempTable();
		SearchByPicmapper.dropTempTable();
		return updateTemTable+addByTempTable;
	}

	@Override
	public List<SearchResults> selectResultByIndexId(int indexId, int start) {
		// TODO Auto-generated method stub
		start = start>0?(start-1)*80:0;
		List<SearchResults> results = new ArrayList<SearchResults>();
		List<SearchResults> selectByIndexId = SearchByPicmapper.selectResultByIndexId(indexId, start);
		int countByIndexId = SearchByPicmapper.countByIndexId(indexId);
		for(SearchResults result:selectByIndexId){
			result.setCount(countByIndexId);
			results.add(result);
		}
		
		return results;
	}

	@Override
	public int updateTranslationTime(int id) {
		// TODO Auto-generated method stub
		return SearchByPicmapper.updateTranslationTime(id);
	}

	@Override
	public int updateValidByPids1(int id, String pids, int i) {
		// TODO Auto-generated method stub
		if(pids==null||pids.isEmpty()){
			return 0;
		}
		List<String> list = new ArrayList<String>();
		String[] split = pids.split(",");
		for(String s:split){
			if(!s.isEmpty()){
				list.add(s);
			}
		}
		if(list.isEmpty()){
			return 0;
		}
		return SearchByPicmapper.updateValidByPids1(id,list,i);
	}

	@Override
	public List<SearchResults> selectByIndexIdAll(Integer indexId) {
		// TODO Auto-generated method stub
		List<SearchResults> selectByIndexId = SearchByPicmapper.selectByIndexIdAll(indexId);
		return selectByIndexId;
	}

	@Override
	public void deleteByIndexId(Integer indexId) {
		// TODO Auto-generated method stub
		SearchByPicmapper.deleteByIndexId(indexId);
	}

	@Override
	public int updateSyncFlag(Integer indexId, int i) {
		// TODO Auto-generated method stub
		return SearchByPicmapper.updateSyncFlag(indexId,i);
	}

	@Override
	public void updateCustomByIndexId(int parseInt, int index_id) {
		// TODO Auto-generated method stub
		SearchByPicmapper.updateCustomByIndexId(parseInt,index_id);
	}


}
