package com.importExpress.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.importExpress.mapper.PaymentMapper;
import com.importExpress.mapper.UserNewMapper;
import com.importExpress.pojo.CustomerDisputeBean;
import com.importExpress.service.CustomerDisputeService;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;
@Service
public class CustomerDisputeServiceImpl implements CustomerDisputeService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
	@Autowired
	private UserNewMapper userNewMapper;
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
		BasicDBObject s = new BasicDBObject("create_time",-1);
		List<String> documents = 
				instance.findAny("data",q,s);
		List<CustomerDisputeBean> list = new ArrayList<CustomerDisputeBean>();
		CustomerDisputeBean bean ;
		long total = 0L;
		List<String> filter = new ArrayList<String>();
		try {
	    	for(String content : documents) {
	    		JSONObject document = JSONObject.parseObject(content);
	    		JSONObject  resource = (JSONObject)document.get("resource");
	    		if(filter.contains(resource.getString("dispute_id"))) {
	    			continue;
	    		}
	    		filter.add(resource.getString("dispute_id"));
	    		bean = new CustomerDisputeBean();
	    		bean.setDisputeID(resource.getString("dispute_id"));
				bean.setId("");
				Date parse = utc.parse(document.getString("create_time").replace("Z", " UTC"));
				bean.setUpdateTime(sdf.format(parse));
				bean.setTime(parse.getTime());
				JSONObject disputeAmount =  (JSONObject)resource.get("dispute_amount");
				
				bean.setValue(disputeAmount.getString("value") + disputeAmount.getString("currency_code"));
				bean.setReason(resource.getString("reason"));
				bean.setStatus(resource.getString("status"));
				
				JSONArray disputedTransactions = (JSONArray)resource.get("disputed_transactions");
				
				String custom = ((JSONObject)disputedTransactions.get(0)).getString("custom");
				bean.setOrderNo("");
				if(StringUtils.indexOf(custom, "@") > -1) {
					if(custom.indexOf("{@}") > -1) {
						String[] split = custom.split("\\{@\\}");
						if(split.length > 3) {
							bean.setUserid(split[0]);
							bean.setOrderNo(split[2]);
						}
					}else {
						String[] split = custom.split("@");
						if(split.length == 10) {
							bean.setUserid(split[0]);
							bean.setOrderNo(split[6]);
						}
					}
				}
				list.add(bean);
	    	}
	    	total = list.stream().count();
	    			
	    	list = list.stream().skip(startNum).limit(limitNum).collect(Collectors.toList());
	    	
	    	List<String> useridList = new ArrayList<String>();
	    	list.stream().forEach(c->{
	    		if(!StringUtils.isBlank(c.getUserid()) && !useridList.contains(c.getUserid())) {
	    			useridList.add(c.getUserid());
	    		}
	    	});
	    	if(!useridList.isEmpty()) {
	    		Map<String,String> useridMap = new HashMap<String,String>();
	    		List<Map<String,Object>> emailList = userNewMapper.getEmailByIdList(useridList);
	    		emailList.stream().forEach(m -> {
	    			useridMap.put(String.valueOf(m.get("id")), (String)m.get("email"));
	    		});
	    		
	    		list.stream().forEach(c->{
	    			c.setEmail(useridMap.get(c.getUserid()));
	    		});
	    	}
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		result.put("total", total);
		result.put("data", list);
		
		return result;
	}
	@Override
	public String info(String disputeID) {
		MongoDBHelp instance = MongoDBHelp.INSTANCE;
		BasicDBObject q = new BasicDBObject();
		q.put("resource_type", "dispute");
		if(StringUtils.isNotBlank(disputeID)) {
			q.put("resource.dispute_id", disputeID);
		}
		BasicDBObject s = new BasicDBObject("create_time",-1);
		List<String> documents = 
				instance.findAny("data",q,s);
		if(documents != null && !documents.isEmpty()) {
			JSONObject  resource= (JSONObject)JSONObject.parseObject(documents.get(0)).get("resource");
			
			return JSONObject.toJSONString(resource);
		}
		return null;
	}

}
