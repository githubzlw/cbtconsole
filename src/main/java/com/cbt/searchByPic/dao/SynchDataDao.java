package com.cbt.searchByPic.dao;

import com.cbt.bean.SearchResults;

import java.util.List;

public interface SynchDataDao {

	List<SearchResults> selectInfoByIndexId(int parseInt);

	int insertInfo(List<SearchResults> list, int indexId);

}
