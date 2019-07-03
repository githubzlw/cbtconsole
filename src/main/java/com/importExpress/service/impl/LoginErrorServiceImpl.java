package com.importExpress.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.importExpress.pojo.LoginErrorInfo;
import com.importExpress.service.LoginErrorService;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;
@Service
public class LoginErrorServiceImpl implements LoginErrorService {
	SimpleDateFormat sdfen = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy", Locale.US);
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public Map<String,Object> getList(int startNum, int limitNum) {
		BasicDBObject q = new BasicDBObject();
//		new BasicDBObject("payment_status", "Completed")
		q.put("flag", new BasicDBObject("$gt",-1));
		BasicDBObject s = new BasicDBObject("_id",-1);
		Map<String,Object> result = new HashMap<>();
		long total = MongoDBHelp.INSTANCE.countFromMongo2("pv_data", q);
		List<String> findAny = MongoDBHelp.INSTANCE.findAnyFromMongo2("pv_data",q,s,startNum,limitNum);
		List<LoginErrorInfo> errors = new ArrayList<>();
		LoginErrorInfo error = null;
		try {
			for(String f : findAny) {
				JSONObject parseObject = JSONObject.parseObject(f);
				JSONObject data = JSONObject.parseObject(parseObject.getString("data"));
				error = new LoginErrorInfo();
				error.setEmail(data.getString("email"));
				error.setPass(data.getString("pass"));
				Integer errorType = data.getInteger("errorType");
				if(errorType != null) {
					error.setErrorType(errorType==1? "用户不存在" : "密码错误");
				}else {
					error.setErrorType("");
				}
				error.setSessionId(data.getString("sessionId"));
				error.setGuid(data.getString("guid"));
				Integer userId = data.getInteger("userId");
				error.setUserId(userId != null ? String.valueOf(userId) : "");
				
				int flag = parseObject.getIntValue("flag");
				if(flag == 0) {
					error.setLoginType("Login fail");
				}else if(flag == 1) {
					error.setLoginType("Add to car fail");
				}else if(flag == 2) {
					error.setLoginType("Goods car change");
				}
				JSONObject localDate1 = JSONObject.parseObject(parseObject.getString("localDate1"));
				
				int year = localDate1.getIntValue("year");
				int monthValue = localDate1.getIntValue("monthValue");
				int dayOfMonth = localDate1.getIntValue("dayOfMonth");
				
				
				error.setLoginTime(year+"-"+monthValue+"-"+dayOfMonth);
//				Date parse = sdf.parse(error.getLoginTime());
//				error.setTime(parse.getTime());
				errors.add(error);
			}
			/*errors = errors.stream().sorted((b1,b2)->{
	    		return Long.compare(b2.getTime(), b1.getTime());
	    	}).collect(Collectors.toList());
			*/
			/*long sTime = StringUtils.isNotBlank(startTime) ? sdf.parse(startTime).getTime() : 0;
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
			errors = errors.stream().filter(b->{
	    		return b.getTime() > sTime && b.getTime() < etime;
	    	}).collect(Collectors.toList());
	    	*/
//			total = errors.stream().count();
	    	
//			errors = errors.stream().skip(startNum).limit(limitNum).collect(Collectors.toList());
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
		result.put("count",(int)total );
		result.put("data", errors);
		return result;
	}

}
