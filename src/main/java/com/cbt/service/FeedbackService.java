package com.cbt.service;

import com.cbt.bean.FeedbackBean;

import java.util.List;

public interface FeedbackService {

	public FeedbackBean queryById(Integer id);

	public List<FeedbackBean> queryByType(Integer type);

}
