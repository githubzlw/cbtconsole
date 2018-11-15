package com.importExpress.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.importExpress.pojo.CustomerDisputeBean;
import com.importExpress.service.CustomerDisputeService;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;
@Service
public class CustomerDisputeServiceImpl implements CustomerDisputeService {
	@Override
	public Map<String, Object> list(String disputeID,int startNum, int limitNum,
			String startTime, String endTime, String status) {
		Map<String, Object> result = new HashMap<String,Object>();
		
		MongoDBHelp instance = MongoDBHelp.INSTANCE;
		BasicDBObject q = new BasicDBObject();
		q.put("resource_type", "dispute");
		if(StringUtils.isNotBlank(disputeID)) {
			q.put("resource.dispute_id", disputeID);
		}
		if(StringUtils.isNotBlank(startTime)) {
			q.append("resource.create_time",new BasicDBObject("$gt",startTime));
		}
		if(StringUtils.isNotBlank(endTime)) {
			q.append("resource.create_time",new BasicDBObject("$lt",endTime));
		}
		if(StringUtils.isNotBlank(status)) {
			q.put("resource.status", status);
		}
//		disputeID,startTime,endTime,status
		
		List<String> documents = 
				instance.findAny("data",q,null, startNum, limitNum);
		List<CustomerDisputeBean> list = new ArrayList<CustomerDisputeBean>();
		CustomerDisputeBean bean ;
    	for(String content : documents) {
    		JSONObject document = JSONObject.parseObject(content);
    		
    		bean = new CustomerDisputeBean();
    		JSONObject  resource = (JSONObject)document.get("resource");
			bean.setId("");
			bean.setUpdateTime(document.getString("create_time"));
			bean.setDisputeID(resource.getString("dispute_id"));
			JSONObject disputeAmount =  (JSONObject)resource.get("dispute_amount");
			
			bean.setValue(disputeAmount.getString("value") + disputeAmount.getString("currency_code"));
			bean.setReason(resource.getString("reason"));
			bean.setStatus(resource.getString("status"));
			list.add(bean);
    	}
		
		
		long total = instance.count("data",q);
		result.put("total", total);
		result.put("data", list);
		
		return result;
	}

}
