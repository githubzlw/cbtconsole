package com.cbt.feedback.service;

import com.cbt.feedback.bean.Questionnaire;
import com.cbt.feedback.dao.QuestionnaireMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

	@Resource
	private QuestionnaireMapper questionnaireMapper;

	@Override
	public List<Questionnaire> queryForList(int type, int userId, String userEmail, int queryStart) {
		return questionnaireMapper.queryForList(type, userId, userEmail, queryStart);
	}

	@Override
	public Long queryForCount(int type, int userId, String userEmail) {
		return questionnaireMapper.queryForCount(type, userId, userEmail);
	}

	@Override
	public List<Questionnaire> queryForAllList() {
		return questionnaireMapper.queryForAllList();
	}

}
