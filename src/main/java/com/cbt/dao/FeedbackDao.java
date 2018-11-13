package com.cbt.dao;

import com.cbt.bean.FeedbackBean;

import java.util.List;

public interface FeedbackDao {

	public FeedbackBean queryById(Integer id);

	public List<FeedbackBean> queryByType(Integer type);

}
