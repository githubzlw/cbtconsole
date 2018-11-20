package com.importExpress.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.importExpress.mapper.PaymentMapper;
import com.importExpress.pojo.WebhookPaymentBean;
import com.importExpress.service.WebhoolPaymentService;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.net.APIResource;
@Service
public class WebhoolPaymentServiceImpl implements WebhoolPaymentService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
	private SimpleDateFormat sdfe = new SimpleDateFormat("HH:mm:ss MMM d, yyyy",Locale.ENGLISH);
	private DecimalFormat df = new DecimalFormat("#0.00");
//	02:08:46 Nov 05, 2018 PST
	@Autowired
	private PaymentMapper paymentMapper;
	@Override
	public Map<String,Object> list(int startNum, int limitNum,String startTime,String endTime, String type) {
		Map<String,Object> result = new HashMap<String, Object>();
		BasicDBObject q = new BasicDBObject();
//		q.put("payment_status", Pattern.compile("[(Complet)(Refund)]{1}ed"));//
//		q.put("payment_status", new BasicDBObject("$regex", Pattern.compile("[(Complet)(Refund)]{1}ed",Pattern.DOTALL)));//
		
//		DBObject[] orDbj = new BasicDBObject[2];  
//        orDbj[0] = new BasicDBObject("payment_status", "Completed");  
//        orDbj[1] = new BasicDBObject("payment_status", "Refunded");  
        BasicDBList values = new BasicDBList();      
       
//		int qFlag = 0;
//        if(StringUtils.equals(type, "1")) {
//			q.put("custom", Pattern.compile("(.*@){1}.*"));
//		}else if(StringUtils.equals(type, "2")) {
//			q.put("custom", Pattern.compile("EBAY"));
//		}else if(StringUtils.equals(type, "3")) {
//			q.put("custom", "");
//		}else {
//			qFlag = 1;
//		}
//		if(qFlag == 0) {
//			 values.add(new BasicDBObject("$or", orDbj));  
//			 q.put("$and", values);
//		}else {
//		}
		values.add(new BasicDBObject("payment_status", "Completed"));
		values.add(new BasicDBObject("payment_status", "Refunded"));
		q.put("$or", values);
		
		long total = MongoDBHelp.INSTANCE.count("data", q);
		int wTotal = (int)total;
		Double mcGrossTatal = 0.00;
		Double mcRefundTatal = 0.00;
		WebhookPaymentBean bean;
//			BasicDBObject s = new BasicDBObject();
//			s.put("ISODate(payment_date)", -1);
		List<String> findAny = MongoDBHelp.INSTANCE.findAny("data",q,null);//, s, startNum, limitNum
		List<WebhookPaymentBean> list = new ArrayList<WebhookPaymentBean>();
		try {
			for (String find : findAny) {
				bean = new WebhookPaymentBean();
				if(StringUtils.indexOf(find, "JSON:") > -1) {
					Event event = APIResource.GSON.fromJson(find.split("JSON:")[1], Event.class);
					Long created = event.getCreated() * 1000L;
					String creatTime = sdf.format(created);
					bean.setTime(created);
				    bean.setCreateTime(creatTime);
					Charge charge = (Charge) event.getData().getObject();
					bean.setId(charge.getId());
					String amount = String.valueOf(charge.getAmount());
					amount = amount.substring(0,amount.length()-2)+"."+amount.substring(amount.length()-2);
					bean.setAmount(amount+" "+charge.getCurrency());
					bean.setMcGross(Double.valueOf(amount));
					
					bean.setUserid("");		
					bean.setOrderNO(charge.getMetadata().get("order_no"));
					bean.setEmail(charge.getReceiptEmail());
					
					bean.setType( "ImportExpress");
					bean.setPayType("Stripe");
					bean.setFindType("Webhook");
					bean.setReceiverID("");
					bean.setTransactionFee("");
					bean.setProfit("");
				}else {
					JSONObject parseObject = JSONObject.parseObject(find);
					String id = parseObject.getString("payer_id");
					bean.setId(id);
					bean.setPayType("paypal");
					bean.setFindType("Webhook");
					String receiver_id = parseObject.getString("receiver_id");
					bean.setReceiverID(StringUtils.equals(receiver_id, "UDSXBNQ5ARA76") ? "新("+receiver_id+")" : "旧("+receiver_id+")");
					String mcFee = parseObject.getString("mc_fee");
					bean.setTransactionFee(mcFee + " " + parseObject.getString("mc_currency"));
					String mcGross = parseObject.getString("mc_gross");
					bean.setTrackID(parseObject.getString("ipn_track_id"));
					bean.setRefundAmount(0);
					if(!StringUtils.isEmpty(mcGross) && StringUtils.startsWith(mcGross, "-")) {
						bean.setRefundAmount(Double.valueOf(mcGross));
						bean.setMcGross(0);
					}else if(!StringUtils.isEmpty(mcGross)){
						bean.setRefundAmount(0);
						bean.setMcGross(Double.valueOf(mcGross));
					}
					
					bean.setAmount(mcGross + " " + parseObject.getString("mc_currency"));
					if(!StringUtils.isEmpty(mcGross) && !StringUtils.isEmpty(mcFee)) {
						bean.setProfit(df.format(Double.valueOf(mcGross) - Double.valueOf(mcFee)));
					}
					String create_time = parseObject.getString("payment_date").replace("PST", "").trim();//注意是空格+UTC
					if(create_time.indexOf(",") == -1) {
						create_time = create_time +", 2018";
					}
					Date parse = sdfe.parse(create_time );
					bean.setCreateTime(sdf.format(parse));
					bean.setTime(parse.getTime());
					bean.setEmail(parseObject.getString("payer_email"));
					String custom = parseObject.getString("custom");
					bean.setOrderNO("");
					if(StringUtils.indexOf(custom, "@") > -1) {
						bean.setType( "ImportExpress");
						
						String[] split = custom.indexOf("{@}") > -1 ? 
								custom.split("\\{@\\}") : custom.split("@");
						
						bean.setUserid(split.length > 3 ? split[0] : "");	
						String orderno = split.length > 5 ? split[6] : split.length > 3 ? split[2] : "";
						bean.setOrderNO(StringUtils.equals(orderno.trim(), "null") ? "" : orderno);
						
					}else if(StringUtils.startsWith(custom, "EBAY")) {
						bean.setType( "EBAY");
					}else {
						bean.setType( "贸易" );
						String name = parseObject.getString("payer_business_name");
						bean.setUserid(StringUtils.isEmpty(name) ?  parseObject.getString("last_name") : name);
					}
				}
				list.add(bean);
			}
			long stTime = StringUtils.isEmpty(startTime) ? 0 : sdf.parse(startTime).getTime();
	        long edTimeTemp = 0L;
	        if(StringUtils.isEmpty(endTime)) {
	        	Calendar rightNow = Calendar.getInstance();
	        	rightNow.setTime(new Date());
	        	rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加10天
	        	edTimeTemp = rightNow.getTime().getTime();
	        }else {
	        	edTimeTemp = sdf.parse(endTime).getTime();
	        	
	        }
	        long edTime = edTimeTemp;
			List<String> filter = new ArrayList<String>();
			Stream<WebhookPaymentBean> stream = list.stream().filter(w -> {
				if(filter.contains(w.getTrackID())) {
					return false;
				}
				filter.add(w.getTrackID());
				boolean fl = true;
				//过滤类型
				if(StringUtils.equals(type, "1")) {
					fl = StringUtils.equals(w.getType(), "ImportExpress");
				}else if(StringUtils.equals(type, "2")) {
					fl = StringUtils.equals(w.getType(), "EBAY");
				}else if(StringUtils.equals(type, "3")) {
					fl = StringUtils.equals(w.getType(), "贸易");
				}
				//时间选择区间
				return fl && w.getTime() > stTime &&  w.getTime() < edTime;
			});
			
			list = stream.collect(Collectors.toList());
			//统计总数
			wTotal = (int)list.stream().count();
			//金额统计
			mcGrossTatal = list.stream().collect(Collectors.summingDouble(WebhookPaymentBean::getMcGross));
			mcRefundTatal = list.stream().collect(Collectors.summingDouble(WebhookPaymentBean::getRefundAmount));
			
			if(!StringUtils.equals(type, "2") && !StringUtils.equals(type, "3")) {
				//加上sql的stripe数据
				Double countAmount = paymentMapper.countAmount(startTime, endTime);
				countAmount = countAmount == null ? 0.00: countAmount;
				mcGrossTatal += countAmount;
				//数据总数
				int sTotal = paymentMapper.countStripePayment(startTime,endTime);
				wTotal = wTotal + sTotal;
				List<Map<String,Object>> listStripePayment = paymentMapper.listStripePayment(0, startNum+limitNum,startTime,endTime);
				for (Map<String, Object> map : listStripePayment) {
					bean = new WebhookPaymentBean();
					bean.setId((String)map.get("paymentid"));
					bean.setCreateTime(((Timestamp)map.get("createtime")).toString());
					bean.setTime(sdf.parse(bean.getCreateTime()).getTime());
					bean.setUserid(String.valueOf((int)map.get("userid")));
					bean.setOrderNO((String)map.get("orderid"));
					String orderdesc = ((String)map.get("orderdesc")).split("通过")[0].replace("付款人:", "");
					bean.setEmail(orderdesc);
					Float paymentAmount = (Float)map.get("payment_amount");
					bean.setAmount(paymentAmount+" "+((String)map.get("payment_cc")).toUpperCase());
					bean.setType( "ImportExpress");
					bean.setPayType("Stripe");
					bean.setFindType("Mysql");
					bean.setReceiverID("");
					bean.setTransactionFee("");
					bean.setProfit("");
					list.add(bean);
				}
			}
			//排序
			list = list.stream().sorted((w1,w2)->{
				return Long.compare(w2.getTime(), w1.getTime());
			}).collect(Collectors.toList());
			
			//分页
			if(limitNum > 0) {
				list = list.stream().skip(startNum).limit(limitNum)
						.collect(Collectors.toList());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*if(!StringUtils.equals(type, "2") && !StringUtils.equals(type, "3")) {
			int count = resultList.size();
			int sstartNum = 0,slimitNum = 0;
			if(limitNum == 0) {
				slimitNum = 0;
			}else {
				if(startNum > wTotal || startNum + limitNum > wTotal) {
					if(startNum > wTotal) {
						sstartNum = startNum - wTotal;
						slimitNum = limitNum;
						
					}else if(startNum + limitNum > wTotal) {
						sstartNum = 0;
						slimitNum = limitNum - count; 
					}
				}
			}
			int sTotal = paymentMapper.countStripePayment(startTime,endTime);
			wTotal = wTotal + sTotal;
			if((limitNum!=0 && slimitNum != 0) || (limitNum ==0)) {
				List<Map<String,Object>> listStripePayment = paymentMapper.listStripePayment(sstartNum, slimitNum,startTime,endTime);
				for (Map<String, Object> map : listStripePayment) {
					bean = new WebhookPaymentBean();
					bean.setId((String)map.get("paymentid"));
					bean.setCreateTime(((Timestamp)map.get("createtime")).toString());
					bean.setUserid(String.valueOf((int)map.get("userid")));
					bean.setOrderNO((String)map.get("orderid"));
					String orderdesc = ((String)map.get("orderdesc")).split("通过")[0].replace("付款人:", "");
					bean.setEmail(orderdesc);
					Float paymentAmount = (Float)map.get("payment_amount");
					bean.setAmount(paymentAmount+" "+((String)map.get("payment_cc")).toUpperCase());
					bean.setType( "ImportExpress");
					bean.setPayType("Stripe");
					bean.setFindType("Mysql");
					bean.setReceiverID("");
					bean.setTransactionFee("");
					bean.setProfit("");
					resultList.add(bean);
				}
				
			}
		}*/
		
		
		result.put("total", wTotal);
		result.put("data", list);
		result.put("mcGrossTatal", df.format(mcGrossTatal));
		result.put("mcRefundTatal", df.format(mcRefundTatal));
		result.put("profit", df.format(mcGrossTatal + mcRefundTatal));
		
		return result;
	}

}
