package com.importExpress.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.importExpress.pojo.CustomerDisputeBean;
import com.importExpress.service.CustomerDisputeService;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;
@Service
public class CustomerDisputeServiceImpl implements CustomerDisputeService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
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
		
		BasicDBObject s = new BasicDBObject();
		s.put("create_time", -1);
		List<String> documents = 
				instance.findAny("data",q,s , startNum, limitNum);
		List<CustomerDisputeBean> list = new ArrayList<CustomerDisputeBean>();
		CustomerDisputeBean bean ;
		try {
	    	for(String content : documents) {
	    		JSONObject document = JSONObject.parseObject(content);
	    		
	    		bean = new CustomerDisputeBean();
	    		JSONObject  resource = (JSONObject)document.get("resource");
				bean.setId("");
				String createTime = document.getString("create_time").replace("Z", " UTC");
				bean.setUpdateTime(sdf.format(utc.parse(createTime)));
				
				bean.setDisputeID(resource.getString("dispute_id"));
				JSONObject disputeAmount =  (JSONObject)resource.get("dispute_amount");
				
				bean.setValue(disputeAmount.getString("value") + disputeAmount.getString("currency_code"));
				bean.setReason(resource.getString("reason"));
				bean.setStatus(resource.getString("status"));
				list.add(bean);
	    	}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long total = instance.count("data",q);
		result.put("total", total);
		result.put("data", list);
		
		return result;
	}

}
