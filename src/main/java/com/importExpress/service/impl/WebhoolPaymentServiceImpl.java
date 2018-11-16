package com.importExpress.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.importExpress.mapper.PaymentMapper;
import com.importExpress.pojo.WebhookPaymentBean;
import com.importExpress.service.WebhoolPaymentService;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WebhoolPaymentServiceImpl implements WebhoolPaymentService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
	private SimpleDateFormat sdfe = new SimpleDateFormat("HH:mm:ss MMM d, yyyy",Locale.ENGLISH);
//	02:08:46 Nov 05, 2018 PST
	@Autowired
	private PaymentMapper paymentMapper;
	@Override
	public Map<String,Object> list(int startNum, int limitNum, String type) {
		Map<String,Object> result = new HashMap<String, Object>();
		BasicDBObject q = new BasicDBObject();
		q.put("payment_status", "Completed");
		
		if(StringUtils.equals(type, "1")) {
			q.put("custom", java.util.regex.Pattern.compile("(.*@){1}.*"));
		}else if(StringUtils.equals(type, "2")) {
			q.put("custom", java.util.regex.Pattern.compile("EBAY"));
		}else if(StringUtils.equals(type, "3")) {
			q.put("custom", "");
		}
		
		long total = MongoDBHelp.INSTANCE.count("data", q);
		int wTotal = (int)total;
		
		List<WebhookPaymentBean> resultList = new ArrayList<WebhookPaymentBean>();
		WebhookPaymentBean bean;
		if(startNum < wTotal) {
//			BasicDBObject s = new BasicDBObject();
//			s.put("ISODate(payment_date)", -1);
			List<String> findAny = MongoDBHelp.INSTANCE.findAny("data", q,null);//, s, startNum, limitNum
			List<WebhookPaymentBean> list = new ArrayList<WebhookPaymentBean>();
			try {
				for (String find : findAny) {
					bean = new WebhookPaymentBean();
					JSONObject parseObject = JSONObject.parseObject(find);
					String id = parseObject.getString("payer_id");
					System.out.println(id);
					bean.setId(id);
					bean.setPayType("paypal");
					bean.setFindType("Webhook");
					String receiver_id = parseObject.getString("receiver_id");
					bean.setReceiverID(StringUtils.equals(receiver_id, "UDSXBNQ5ARA76") ? "新("+receiver_id+")" : "旧("+receiver_id+")");
					bean.setTransactionFee(parseObject.getString("mc_fee")+" "+parseObject.getString("mc_currency"));
					String create_time = parseObject.getString("payment_date").replace("PST", "").trim();//注意是空格+UTC
					if(create_time.indexOf(",") == -1) {
						create_time = create_time +", 2018";
					}
					bean.setCreateTime(sdf.format(sdfe.parse(create_time )));
					bean.setTime(sdfe.parse(create_time ).getTime());
					bean.setAmount(parseObject.getString("mc_gross")+" "+parseObject.getString("mc_currency"));
					bean.setEmail(parseObject.getString("payer_email"));
					String custom = parseObject.getString("custom");
					bean.setOrderNO("");
					if(StringUtils.indexOf(custom, "@") > -1) {
						bean.setType( "ImportExpress");
						if(custom.indexOf("{@}") > -1) {
							String[] split = custom.split("\\{@\\}");
							if(split.length > 3) {
								bean.setUserid(split[0]);
								bean.setOrderNO(split[2]);
							}
						}else {
							String[] split = custom.split("@");
							if(split.length == 10) {
								bean.setUserid(split[0]);
								bean.setOrderNO(split[6]);
							}
						}
					}else if(StringUtils.startsWith(custom, "EBAY")) {
						bean.setType( "EBAY");
					}else {
						bean.setType( "贸易" );
					}
					list.add(bean);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultList = list.stream().sorted((w1,w2)->{
				return Long.compare(w2.getTime(), w1.getTime());
				
			}).skip(startNum).limit(limitNum)
					.collect(Collectors.toList());
		}
		
		if(!StringUtils.equals(type, "2") && !StringUtils.equals(type, "3")) {
			int count = resultList.size();
			int sstartNum = 0,slimitNum = 0;
			if(startNum > wTotal || startNum + limitNum > wTotal) {
				if(startNum > wTotal) {
					sstartNum = startNum - wTotal;
					slimitNum = limitNum;
					
				}else if(startNum + limitNum > wTotal) {
					sstartNum = 0;
					slimitNum = limitNum - count; 
				}
			}
			int sTotal = paymentMapper.countStripePayment();
			wTotal = wTotal + sTotal;
			if(slimitNum != 0) {
				List<Map<String,Object>> listStripePayment = paymentMapper.listStripePayment(sstartNum, slimitNum);
				for (Map<String, Object> map : listStripePayment) {
					bean = new WebhookPaymentBean();
					bean.setId((String)map.get("paymentid"));
					bean.setCreateTime(((Timestamp)map.get("createtime")).toString());
					bean.setUserid(String.valueOf((int)map.get("userid")));
					bean.setOrderNO((String)map.get("orderid"));
					String orderdesc = ((String)map.get("orderdesc")).split("通过")[0].replace("付款人:", "");
					bean.setEmail(orderdesc);
					bean.setAmount((Float)map.get("payment_amount")+" "+((String)map.get("payment_cc")).toUpperCase());
					bean.setType( "ImportExpress");
					bean.setPayType("Stripe");
					bean.setFindType("Mysql");
					bean.setReceiverID("");
					resultList.add(bean);
				}
			}
		}
		result.put("total", wTotal);
		result.put("data", resultList);
		
		return result;
	}

}
