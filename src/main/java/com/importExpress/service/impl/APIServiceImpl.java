package com.importExpress.service.impl;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.importExpress.service.APIService;
import com.importExpress.utli.OKHttpUtils;
import com.paypal.base.codec.binary.Base64;

import okhttp3.Headers;
@Service
public class APIServiceImpl implements APIService {
	//CLIENT_ID
	//SECRET
	private static final String SANDBOX_FLAG ;
	private static final String CLIENT_ID; 
    private static final String SECRET; 
    private static final String URL; 
//	//CLIENT_ID
//	private final String CLIENT_ID = "AbcNSczSOI2hs3jDSyNYWv8frXFMZgWqLGGvseyFs_Ga6zzMtKLwkK4GZbOOcrs_xPPzeDr309UfDoas";
//	//SECRET
//	private final String SECRET = "EGIZQZs2ayFpE9v87fBRqV1Yz0gY6maYCKu1jG5rPWTdkbOGfaxAfcLPm2PP5Kbw7zrBOigJvDcQPudx";
	//TOKEN_URL
	private static final String TOKEN_URL;// = "https://api.sandbox.paypal.com/v1/oauth2/token";
	//DISPUTE_DETAIL
	private static final String DISPUTE_URL;// = "https://api.sandbox.paypal.com/v1/customer/disputes/";
	//DISPUTE_LIST
	private static final String DISPUTE_LIST;// = "https://api.sandbox.paypal.com/v1/customer/disputes?disputed_transaction_id=";
	
	private OKHttpUtils okHttpUtils = new OKHttpUtils();
	
	static{
		ResourceBundle resource = ResourceBundle.getBundle("paypal",Locale.getDefault());
		SANDBOX_FLAG = resource.getString("SANDBOX_FLAG");
		if(StringUtils.equals(SANDBOX_FLAG, "true")) {
			CLIENT_ID = resource.getString("SANDBOX_CLIENT_ID");
			SECRET = resource.getString("SANDBOX_CLIENT_SECRET");
			URL = resource.getString("SANDBOX_ENDPOINT");
		}else {
			CLIENT_ID = resource.getString("LIVE_CLIENT_NEW_ID");
			SECRET = resource.getString("LIVE_CLIENT_NEW_SECRET");
			URL = resource.getString("LIVE_ENDPOINT");
		}
		TOKEN_URL = URL +"/v1/oauth2/token";
		DISPUTE_URL = URL +"/v1/customer/disputes/";
		DISPUTE_LIST = URL +"/v1/customer/disputes?disputed_transaction_id=";
	}
	@Override
	public String getAccessToken(String merchantID) throws Exception {
		String authorization = CLIENT_ID + ":" + SECRET;
		if(StringUtils.equals(merchantID, "584JZVFU6PPVU")) {
			ResourceBundle resource = ResourceBundle.getBundle("paypal",Locale.getDefault());
			authorization = resource.getString("LIVE_CLIENT_OLD_ID")+":"
					+ resource.getString("LIVE_CLIENT_OLD_SECRET");
		}
    	authorization = Base64.encodeBase64String(authorization.getBytes());
    	
		Headers header = new Headers.Builder()
				.set("Accept", "application/json")
				.set("Accept-Language", "en_US")
				.set("content-type", "application/x-www-form-urlencoded")
				.set("Authorization", "Basic "+authorization)
				.build();
		String  mediaType = "application/x-www-form-urlencoded";
		String param = "grant_type=client_credentials";
		
		String response = okHttpUtils.post(TOKEN_URL, header, mediaType, param);
		if(StringUtils.isNotEmpty(response)) {
			JSONObject parseObject = JSONObject.parseObject(response);
			return parseObject.getString("access_token");
		}
		return "";
	}

	@Override
	public String listDispute(String disputedTransactionID,String merchantID) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
		.set("content-type", "application/json")
		.set("Authorization", "Bearer "+accessToken)
		.build();
			
		String url = DISPUTE_LIST + disputedTransactionID;
		//请求
		String response = okHttpUtils.get(url, header);
		System.out.println(response);
		return 	response;
	}

	@Override
	public String showDisputeDetails(String disputeID,String merchantID) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
		.set("content-type", "application/json")
		.set("Authorization", "Bearer "+accessToken)
		.build();
			
		String url = DISPUTE_URL + disputeID;
		//请求
		String response = "";
		try {
			response = okHttpUtils.get(url, header);
			
		} catch (Exception e) {
			if(e.getMessage().indexOf("RESOURCE_NOT_FOUND_ERROR") > -1) {
				response = showDisputeDetails(disputeID, "584JZVFU6PPVU");
			}else {
				throw e;
			}
		}
		return 	response;
	}

	@Override
	public String partiallyUpdates(String disputeID, String merchantID, JSONObject param) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
		.set("content-type", "application/json")
		.set("Authorization", "Bearer "+accessToken)
		.build();
	
		String url = DISPUTE_URL + disputeID+"/accept-claim";
		//请求
		String mediaType = "application/json; charset=utf-8";
//		String param = "{\"note\":\"Refund to the you\"}";
		String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
		System.out.println(response);
		return 	response;
	}

	@Override
	public String acceptClaim(String disputeID , String merchantID, JSONObject param) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
		.set("content-type", "application/json")
		.set("Authorization", "Bearer "+accessToken)
		.build();
	
		String url = DISPUTE_URL + disputeID+"/accept-claim";
		//请求
		String mediaType = "application/json; charset=utf-8";
//		String param = "{\"note\":\"Refund to the you\"}";
		String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
		System.out.println(response);
		return 	response;
	}

	@Override
	public String updateDisputeStatus(String disputeID, String merchantID) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
		.set("content-type", "application/json")
		.set("Authorization", "Bearer "+accessToken)
		.build();
	
		String url = DISPUTE_URL + disputeID+"/require-evidence";
		//请求
		String mediaType = "application/json; charset=utf-8";
		String param = "{\"action\":\"BUYER_EVIDENCE\"}";
		String response = okHttpUtils.post(url, header, mediaType , param );
		System.out.println(response);
		return 	response;	
	}

	@Override
	public String makeOffer(String disputeID, String merchantID, JSONObject param) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/make-offer";
		//请求
		String mediaType = "application/json; charset=utf-8";//application/json; charset=utf-8 application/x-www-form-urlencoded
		String param1 = JSONObject.toJSONString(param);
//		System.out.println("aaaaaaaaa="+param1);
		String response = okHttpUtils.post(url, header, mediaType , param1);
		return 	response;
	}

	@Override
	public String acknowledgeReturnedItem(String disputeID, String merchantID, JSONObject param) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/acknowledge-return-item";
		//请求
		String mediaType = "application/json; charset=utf-8";
		String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
		return 	response;
	}

	@Override
	public String sendMessage(String disputeID, String merchantID, JSONObject param) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/send-message";
		//请求
		String mediaType = "application/json; charset=utf-8";
		String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
		return 	response;
	}

	@Override
	public String provideEvidence(String disputeID, String merchantID,String param,File fileName) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
				.set("Content-Type", "multipart/related; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID + "/provide-evidence";
		//请求
		String response = okHttpUtils.postFile(url, header,param,fileName );
		return 	response;
	}

}
