package com.cbt.feedback.service;

import com.cbt.feedback.bean.SubscribeEmail;
import com.cbt.feedback.dao.SubscribeEmailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class SubscribeEmailServiceImpl implements SubscribeEmailService {
	
	@Resource
	private SubscribeEmailMapper subscribeEmailMapper;

	@Override
	public List<SubscribeEmail> queryForList() {
		return subscribeEmailMapper.queryForList();
	}

	@Override
	public void insertSubscribeEmail(SubscribeEmail subscribeEmail) {
		subscribeEmailMapper.insertSubscribeEmail(subscribeEmail);
	}

	@Override
	public void updateSubscribeEmail(SubscribeEmail subscribeEmail) {
		subscribeEmailMapper.updateSubscribeEmail(subscribeEmail);
	}

	@Override
	public void deleteSubscribeEmail(int id) {
		subscribeEmailMapper.deleteSubscribeEmail(id);
	}

}
