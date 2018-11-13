package com.cbt.feedback.service;

import com.cbt.feedback.bean.ErrorFeedback;
import com.cbt.feedback.dao.ErrorFeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ErrorFeedbackServiceImpl implements ErrorFeedbackService {

	@Autowired
	private ErrorFeedbackMapper  errorFeedbackmapper ; 
	

	@Override
	public List<ErrorFeedback> showErrorFb(int type, String belongto,
			String startTime, String endTime, int deFlag, int page) {
		// TODO Auto-generated method stub
		int  start = (page-1)*40 ;
		startTime = startTime==""||startTime==null?null:startTime+" 00:00:00";
	    endTime = endTime==""||endTime==null?null:endTime+" 23:59:59";
		List<ErrorFeedback>  list =	errorFeedbackmapper.showErrorFb(type,belongto,startTime,endTime,deFlag,start);
		int count = errorFeedbackmapper.count(type,belongto,startTime,endTime,deFlag);
		if(list.size()>0){
			list.get(0).setCount(count);
		}
		return  list ;
	}

	@Override
	public int InsertErrorInfo(String content, String createtime,
			String belongTo, int type, String logLocation, int positionFlag) {
		return  errorFeedbackmapper.InsertErrorInfo(content,createtime,belongTo,type,logLocation,positionFlag);
	}

	@Override
	public int updateErrorFlag(int id, String remark) {
		// TODO Auto-generated method stub
		return  errorFeedbackmapper.updateErrorFlag(id,remark);
	}

	@Override
	public int updateSomeErrorFlag(List<Map<String, String>> bgList) {
		// TODO Auto-generated method stub
		return errorFeedbackmapper.updateSomeErrorFlag(bgList);
	}

}
