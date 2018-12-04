package com.importExpress.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.importExpress.mapper.AdminRUserMapper;
import com.importExpress.mapper.CustomerDisputeMapper;
import com.importExpress.pojo.AdminRUser;
import com.importExpress.pojo.CustomerDisputeBean;
import com.importExpress.pojo.CustomerDisputeVO;
import com.importExpress.service.CustomerDisputeService;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Dispute;
import com.stripe.net.APIResource;
@Service
public class CustomerDisputeServiceImpl implements CustomerDisputeService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
	@Autowired
	private CustomerDisputeMapper customerDisputeMapper;
	@Autowired
	private AdminRUserMapper adminRUserMapper;
	@Override
	public Map<String, Object> list(String disputeID,int startNum, int limitNum,
			String startTime, String endTime, String status,int admID) {
		Map<String, Object> result = new HashMap<String,Object>();
		
		MongoDBHelp instance = MongoDBHelp.INSTANCE;
		BasicDBObject q = new BasicDBObject();
		q.put("resource_type", "dispute");
//		if(StringUtils.isNotBlank(disputeID)) {
//			q.put("resource.dispute_id", disputeID);
//		}
//		if(StringUtils.isNotBlank(startTime)) {
//			q.append("resource.create_time",new BasicDBObject("$gt",startTime));
//		}
//		if(StringUtils.isNotBlank(endTime)) {
//			q.append("resource.create_time",new BasicDBObject("$lt",endTime));
//		}
//		if(StringUtils.isNotBlank(status)) {
//			q.put("resource.status", status);
//		}
//		BasicDBObject s = new BasicDBObject("create_time",-1);
		List<String> documents = 
				instance.findAny("data",q,null);
		List<CustomerDisputeVO> list = new ArrayList<CustomerDisputeVO>();
		CustomerDisputeVO bean ;
		long total = 0L;
		List<String> filter = new ArrayList<String>();
		try {
	    	for(String content : documents) {
	    		bean = new CustomerDisputeVO();
	    		if(StringUtils.indexOf(content,"dispute_id") > -1) {
	    			JSONObject document = JSONObject.parseObject(content);
	    			JSONObject  resource = (JSONObject)document.get("resource");
	    			bean.setDisputeID(resource.getString("dispute_id"));
	    			Date parse = utc.parse(document.getString("create_time").replace("Z", " UTC"));
	    			bean.setUpdateTime(sdf.format(parse));
	    			bean.setTime(parse.getTime());
	    			JSONObject disputeAmount =  (JSONObject)resource.get("dispute_amount");
	    			
	    			bean.setValue(disputeAmount.getString("value") + disputeAmount.getString("currency_code"));
	    			bean.setReason(resource.getString("reason"));
	    			bean.setStatus(resource.getString("status"));
	    			bean.setApiType("Paypal");
	    			JSONArray disputedTransactions = (JSONArray)resource.get("disputed_transactions");
	    			JSONObject disputedTransaction = (JSONObject)disputedTransactions.get(0);
	    			JSONObject seller = (JSONObject)disputedTransaction.get("seller");
	    			String merchant_id = seller.getString("merchant_id");
	    			bean.setMerchantID(merchant_id);
	    			String custom = disputedTransaction.getString("custom");
	    			bean.setOrderNo("");
	    			if(StringUtils.indexOf(custom, "@") > -1) {
	    				
	    				String[] split = custom.indexOf("{@}") > -1 ? custom.split("\\{@\\}") : custom.split("@");
	    				
	    				bean.setUserid(split.length > 3 ? split[0] : "");
	    				bean.setOrderNo(split.length == 10 ? split[6] : split.length > 3 ?split[2] : "");
	    				
	    			}
	    			list.add(bean);
	    		}else if(StringUtils.indexOf(content, "issuing.dispute") > -1){
	    			Dispute dispute = APIResource.GSON.fromJson(content, Dispute.class);
                    List<BalanceTransaction> balanceTransactions = dispute.getBalanceTransactions();
                    BalanceTransaction balanceTransaction = balanceTransactions.get(0);
                    bean.setDisputeID(balanceTransaction.getId());
                    String net = String.valueOf(balanceTransaction.getNet());
                    net = net.substring(0,net.length() - 2) + "." + net.substring(net.length() - 2);
                    
                    bean.setValue(net + " " + dispute.getCurrency());
                    bean.setTime(dispute.getCreated() * 1000L);
                    bean.setUpdateTime(sdf.format(bean.getTime()));
                    bean.setUserid("");
                    bean.setEmail("");
                    bean.setOrderNo("");
                    bean.setUpdateTime(sdf.format(dispute.getCreated() * 1000L));
                    bean.setReason(dispute.getReason());
	    			bean.setStatus(dispute.getStatus());
	    			bean.setApiType("stripe");
	    			list.add(bean);
	    		}
				
	    	}
	    	
	    	list = list.stream().sorted((b1,b2)->{
	    		return Long.compare(b2.getTime(), b1.getTime());
	    	}).collect(Collectors.toList());
	    	
	    	List<String> useridList = new ArrayList<String>();
	    	list.stream().forEach(c->{
	    		if(!StringUtils.isBlank(c.getUserid()) && !useridList.contains(c.getUserid())) {
	    			useridList.add(c.getUserid());
	    		}
	    	});
	    	
	    	if(!useridList.isEmpty()) {
	    		Map<String,AdminRUser> useridMap = new HashMap<String,AdminRUser>();
	    		int admid = admID == 1 || admID == 8 || admID == 18? 0 : admID;
	    		List<AdminRUser> selectByUserID = adminRUserMapper.selectByUserID(useridList,admid);
	    		selectByUserID.stream().forEach(m -> {
	    			useridMap.put(String.valueOf(m.getUserid()), m);
	    		});
	    		
	    		list.stream().forEach(c->{
	    			AdminRUser adminRUser = useridMap.get(c.getUserid());
	    			if(adminRUser != null) {
	    				c.setEmail(adminRUser.getUseremail());
	    				c.setOprateAdm(adminRUser.getAdmname());
	    			}
	    		});
	    	}
	    	long sTime = StringUtils.isNotBlank(startTime) ? sdf.parse(startTime).getTime() : 0;
	    	
	    	long etimeTemp = 0L;
	    	if(StringUtils.isNotBlank(endTime)) {
	    		etimeTemp  = sdf.parse(endTime).getTime();
	    	}else {
	    		Calendar rightNow = Calendar.getInstance();
	    		rightNow.setTime(new Date());
	    		rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
	    		etimeTemp = rightNow.getTime().getTime();
	    	}
	    	long etime = etimeTemp;
	    	
	    	list = list.stream().filter(b->{
	    		if(filter.contains(b.getDisputeID())) {
	    			return false;
	    		}else {
	    			filter.add(b.getDisputeID());
	    			boolean fl = true;
	    			if(StringUtils.isNotBlank(disputeID)) {
	    				fl = StringUtils.equals(b.getDisputeID(), disputeID); 
	    			}
	    			if(StringUtils.isNotBlank(status)) {
	    				fl = fl && StringUtils.equals(b.getStatus(), status);
	    			}
	    			return fl && b.getTime() > sTime && b.getTime() < etime && StringUtils.isNotBlank(b.getOprateAdm());
	    		}
	    	}).collect(Collectors.toList());
	    	
	    	total = list.stream().count();
	    	
	    	list = list.stream().skip(startNum).limit(limitNum).collect(Collectors.toList());
	    	
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
		q.put("resource.dispute_id", disputeID);
		BasicDBObject s = new BasicDBObject("create_time",-1);
		List<String> documents = 
				instance.findAny("data",q,s);
		if(documents != null && !documents.isEmpty()) {
			JSONObject  resource= (JSONObject)JSONObject.parseObject(documents.get(0)).get("resource");
			return JSONObject.toJSONString(resource);
		}
		return null;
	}
	
	@Override
	public int confirm(CustomerDisputeBean customer) {
		
		return customerDisputeMapper.insert(customer);
	}
	@Override
	public int count(String disputeID,String status) {
		Integer count = customerDisputeMapper.count(disputeID,status) ;
		return count == null ? 0 : count;
	}
	@Override
	public List<CustomerDisputeBean> confirmList(String disputeid,String status,int startNum,int limitNum) {
		// TODO Auto-generated method stub
		return customerDisputeMapper.confirmList(disputeid,status,startNum,limitNum);
	}
	@Override
	public int updateStatus(String disputeId, String status) {
		// TODO Auto-generated method stub
		return customerDisputeMapper.updateStatus(disputeId, status);
	}
	@Override
	public CustomerDisputeBean getComfirmByDisputeID(String disputeid) {
		// TODO Auto-generated method stub
		return customerDisputeMapper.getComfirmByDisputeID(disputeid);
	}
	@Override
	public Integer updateRefuseReason(String disputeid, String refuseReason) {
		// TODO Auto-generated method stub
		return customerDisputeMapper.updateRefuseReason(disputeid, refuseReason);
	}
	@Override
	public Integer updateRefund(String disputeid, String refundedAmount) {
		// TODO Auto-generated method stub
		return customerDisputeMapper.updateRefund(disputeid, refundedAmount);
	}
	@Override
	public long updateMessage(String disputeId) {
		MongoDBHelp instance = MongoDBHelp.INSTANCE;
		BasicDBObject filter = new BasicDBObject();
		filter.put("resource_type", "dispute");
		filter.put("resource.dispute_id", disputeId);
		BasicDBObject update = new BasicDBObject("$set",new BasicDBObject("isRead",true));
		long result = instance.update("data", filter, update ); 
		
		return result;
	}
	
	@Override
	public Map<String, Object> list(List<String> orderIdList) {
		Map<String, Object> result = new HashMap<String,Object>();
		
		MongoDBHelp instance = MongoDBHelp.INSTANCE;
		BasicDBObject q = new BasicDBObject();
		q.put("resource_type", "dispute");
		BasicDBObject s = new BasicDBObject("_id",-1);
		List<String> documents = 
				instance.findAny("data",q,s);
		List<CustomerDisputeVO> list = new ArrayList<CustomerDisputeVO>();
		CustomerDisputeVO bean ;
		long total = 0L;
		List<String> filter = new ArrayList<String>();
		try {
	    	for(String content : documents) {
	    		bean = new CustomerDisputeVO();
	    		if(StringUtils.indexOf(content,"dispute_id") > -1) {
	    			JSONObject document = JSONObject.parseObject(content);
	    			JSONObject  resource = (JSONObject)document.get("resource");
	    			bean.setDisputeID(resource.getString("dispute_id"));
	    			Date parse = utc.parse(document.getString("create_time").replace("Z", " UTC"));
	    			bean.setUpdateTime(sdf.format(parse));
	    			bean.setTime(parse.getTime());
	    			JSONArray disputedTransactions = (JSONArray)resource.get("disputed_transactions");
	    			JSONObject disputedTransaction = (JSONObject)disputedTransactions.get(0);
	    			JSONObject seller = (JSONObject)disputedTransaction.get("seller");
	    			String merchant_id = seller.getString("merchant_id");
	    			bean.setMerchantID(merchant_id);
	    			String custom = disputedTransaction.getString("custom");
	    			bean.setOrderNo("");
	    			if(StringUtils.indexOf(custom, "@") > -1) {
	    				String[] split = custom.indexOf("{@}") > -1 ? custom.split("\\{@\\}") : custom.split("@");
	    				bean.setUserid(split.length > 3 ? split[0] : "");
	    				bean.setOrderNo(split.length > 6 ? split[6] : split.length > 3 ?split[2] : "");
	    				
	    			}
	    			list.add(bean);
	    		}else if(StringUtils.indexOf(content, "issuing.dispute") > -1){
	    			Dispute dispute = APIResource.GSON.fromJson(content, Dispute.class);
                    List<BalanceTransaction> balanceTransactions = dispute.getBalanceTransactions();
                    BalanceTransaction balanceTransaction = balanceTransactions.get(0);
                    bean.setDisputeID(balanceTransaction.getId());
                    String net = String.valueOf(balanceTransaction.getNet());
                    net = net.substring(0,net.length() - 2) + "." + net.substring(net.length() - 2);
                    
                    bean.setValue(net + " " + dispute.getCurrency());
                    bean.setTime(dispute.getCreated() * 1000L);
                    bean.setUpdateTime(sdf.format(bean.getTime()));
                    bean.setUserid("");
                    bean.setEmail("");
                    bean.setOrderNo("");
                    bean.setUpdateTime(sdf.format(dispute.getCreated() * 1000L));
                    bean.setReason(dispute.getReason());
	    			bean.setStatus(dispute.getStatus());
	    			bean.setApiType("stripe");
	    			list.add(bean);
	    		}
	    	}
	    	
	    	list = list.stream().filter(b->{
	    		if(filter.contains(b.getDisputeID())) {
	    			return false;
	    		}else {
	    			filter.add(b.getDisputeID());
	    			if(orderIdList.contains(b.getOrderNo())) {
	    				return true;
	    			}
	    			return false;
	    		}
	    	}).collect(Collectors.toList());
	    	
	    	list.stream().forEach(l -> {
	    		result.put(l.getUserid()+"_"+l.getOrderNo(), l);
	    	});
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
