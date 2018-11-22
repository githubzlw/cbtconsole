package com.importExpress.service.impl;

import com.cbt.warehouse.util.StringUtil;
import com.importExpress.mapper.QueAnsMapper;
import com.importExpress.pojo.QueAns;
import com.importExpress.service.QuestionAndAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionAndAnswerServiceImpl implements QuestionAndAnswerService {

	@Autowired
	private QueAnsMapper queAnsMapper;

	@Override
	public List<QueAns> findByQuery(String goodsPid, String goodsName,
                                    int adminId, int replyFlag, int replyStatus,String startdate, String enddate, int page) {
		List<QueAns> list=queAnsMapper.findByQuery(goodsPid, goodsName, adminId,replyFlag,replyStatus, startdate,enddate, page);
		for(QueAns q:list){
			if(StringUtil.isNotBlank(q.getReply_content())){
				q.setReply_content(q.getReply_content().replace("\n",""));
			}
		}
		return list;
	}

	@Override
	public int getCountByQuery(String goodsPid, String goodsName, int adminId,
			int replyFlag, int replyStatus, String startdate,String enddate) {
		
		return queAnsMapper.getCountByQuery(goodsPid, goodsName, adminId, replyFlag, replyStatus, startdate,enddate);
	}
	@Override
	public int changeIsShow(String pid, String type) {
		
		return queAnsMapper.changeIsShow(pid, type);
	}
	@Override
	public QueAns getQueAnsinfo(int qid) {
		
		return queAnsMapper.getQueAnsinfo(qid);
	}

	@Override
	public int deleteQuestion(int pid) {
		return queAnsMapper.deleteQuestion(pid);
	}

	@Override
	public int influenceShop(int qid, String shop_id,String state) {
		
		return queAnsMapper.influenceShop(qid,shop_id,state);
	}
	@Override
	public int updateReplyContent(int qid, int adminId, String rContent,String isShow,String shop_id) {
		
		return queAnsMapper.updateReplyContent(qid, adminId, rContent,isShow,shop_id);
	}

	@Override
	public int updateRemark(int qid, int adminId, int isReview,
			String reviewRemark) {
		
		return queAnsMapper.updateRemark(qid, adminId, isReview, reviewRemark);
	}


	

}
