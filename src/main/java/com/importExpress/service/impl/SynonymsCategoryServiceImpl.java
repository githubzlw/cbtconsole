package com.importExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.jdbc.DBHelper;
import com.google.common.collect.Lists;
import com.importExpress.mapper.SynonymsCategoryMapper;
import com.importExpress.pojo.SynonymsCategoryWrap;
import com.importExpress.service.SynonymsCategoryService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
@Service
public class SynonymsCategoryServiceImpl implements SynonymsCategoryService {
	@Autowired
	private SynonymsCategoryMapper synonymsCategoryMapper;

	@Override
	public List<SynonymsCategoryWrap> getCategoryList(String catid, int page) {
		// TODO Auto-generated method stub
		return synonymsCategoryMapper.getCategoryList(catid, page);
	}

	@Override
	public int categoryListCount(String catid) {
		// TODO Auto-generated method stub
		return synonymsCategoryMapper.categoryListCount(catid);
	}

	@Override
	public int updateCategory(String catid, String content) {
		String sql = "update synonyms_category set valid=1,synonyms_category=? where catid=?";
		List<String> lstValue = Lists.newArrayList();
		lstValue.add(content);
		lstValue.add(catid);
		
		String runSql = DBHelper.covertToSQL(sql, lstValue);
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		
		return sendMsgByRPC == null ? 0 : Integer.parseInt(sendMsgByRPC);
	}

	@Override
	public int delete(String catid) {
		String sql = "update synonyms_category set valid=0 where catid=?";
		List<String> lstValue = Lists.newArrayList();
		lstValue.add(catid);
		String runSql = DBHelper.covertToSQL(sql, lstValue);
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		
		return sendMsgByRPC == null ? 0 : Integer.parseInt(sendMsgByRPC);
	}

	@Override
	public int addCategory(SynonymsCategoryWrap wrap) {
		String sql = "insert into  synonyms_category (category,catid,synonyms_category,valid) values(?,?,?,?)";
		List<String> lstValue = Lists.newArrayList();
		lstValue.add(wrap.getCategory());
		lstValue.add(wrap.getCatid());
		lstValue.add(wrap.getSynonymsCategory());
		lstValue.add("1");
		String runSql = DBHelper.covertToSQL(sql, lstValue);
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		return sendMsgByRPC == null ? 0 : Integer.parseInt(sendMsgByRPC);
	}

}
