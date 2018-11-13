package com.cbt.service.impl;

import com.cbt.bean.FeedbackBean;
import com.cbt.dao.FeedbackDao;
import com.cbt.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;

	@Override
	public FeedbackBean queryById(Integer id) {
		return feedbackDao.queryById(id);
	}

	@Override
	public List<FeedbackBean> queryByType(Integer type) {
		return feedbackDao.queryByType(type);
	}

}
