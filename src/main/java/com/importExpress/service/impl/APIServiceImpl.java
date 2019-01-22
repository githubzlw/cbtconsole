package com.importExpress.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.importExpress.service.APIService;
import com.importExpress.utli.OKHttpUtils;
import com.paypal.base.codec.binary.Base64;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
		String param = "{\"action\":\"SELLER_EVIDENCE\"}";
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
	public String provideEvidence(String disputeID, String merchantID,Map<String,Object> param,Map<String,File> fileMap) throws Exception {
		String accessToken = getAccessToken(merchantID);
		//设置请求头
		Headers header = new Headers.Builder()
				.set("Content-Type", "multipart/related;  boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID + "/provide-evidence";
		//请求
		String response = okHttpUtils.postFile(url, header,param,fileMap );
		return 	response;
	}
//public static void main(String[] args) {
//	APIServiceImpl d = new APIServiceImpl();
//	try {
//		d.updateDisputeStatus("PP-D-7183", "");
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//}
public static void main(String[] args) {
	APIServiceImpl d = new APIServiceImpl();
	try {
		d.updateDisputeStatus("PP-D-7183", "");
		JSONObject in = new JSONObject();
        in.put("evidence_type", "PROOF_OF_FULFILLMENT");
//        
//        
        JSONArray tracking_info = new JSONArray();
        JSONObject info = new JSONObject();
        info.put("carrier_name", "FEDEX");
        info.put("tracking_number", "122533485");
        tracking_info.add(info);
//        
        JSONObject evidence_info = new JSONObject();
        evidence_info.put("tracking_info", tracking_info);
//        
        in.put("evidence_info", evidence_info);
        in.put("notes", "Test");
        Map<String,Object> param = new HashMap<>();
        param.put("input", in);
        
        Map<String,File> fileMap = new HashMap<>();
      fileMap.put("file1", new File("C:\\Users\\Administrator\\Desktop\\sample.png"));
//        d.provideEvidence("PP-D-7183", "", param, fileMap);
//		
//        
//        
//        JSONObject returnAddress =  new JSONObject();
//    	returnAddress.put("address_line_1", "14,Kimberly st");
//    	returnAddress.put("address_line_2", "Open Road North");
//    	returnAddress.put("country_code", "US");
//    	returnAddress.put("admin_area_1", "Gotham City");
//    	returnAddress.put("admin_area_2", "Gotham");
//    	returnAddress.put("postal_code", "124566");
//        
//        
//    	param.put("return_shipping_address", returnAddress);
//        
//        
//    	Map<String,File> fileMap = new HashMap<>();
//        fileMap.put("file1", new File("C:\\Users\\Administrator\\Desktop\\sample.png"));
//        
//		String accessToken = d.getAccessToken("");
//		//设置请求头
//		Headers header = new Headers.Builder()
//				.set("Content-Type", "multipart/related;  boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
//				.set("Authorization", "Bearer "+accessToken)
//				.build();
//		
//		String url = DISPUTE_URL + "PP-D-7183" + "/provide-evidence";
//
//		//设置请求参数
//        MediaType MutilPart_Form_File = MediaType.parse("multipart/form-data; charset=utf-8");
//        RequestBody fileBody = null;
//        MediaType MutilPart_Form_Data = MediaType.parse("application/json");
//        RequestBody bodyParams = null;//RequestBody.create(MutilPart_Form_Data,param);
//                  
//        
//        MultipartBody.Builder requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM) ;
//        Iterator<Entry<String, File>> iteratorFile = fileMap.entrySet().iterator();
//        
//        while (iteratorFile.hasNext()){
//        	Entry<String, File> fileNext = iteratorFile.next();
//        	fileBody = RequestBody.create(MutilPart_Form_File, fileNext.getValue());
//        	requestBody.addFormDataPart(fileNext.getKey(), "@"+fileNext.getValue().getName(), fileBody);
//		}
//        
//        Iterator<Entry<String, Object>> iteratorParam = param.entrySet().iterator();
//        while (iteratorParam.hasNext()) {
//        	Entry<String, Object> paramNext = iteratorParam.next();
//        	bodyParams = RequestBody.create(MutilPart_Form_Data,JSON.toJSONString(paramNext.getValue()));
//        	System.out.println(paramNext.getKey()+"===="+JSON.toJSONString(paramNext.getValue()));
//        	requestBody.addFormDataPart(paramNext.getKey(), "", bodyParams);
//		}
//        
//    
//    //发送post请求o
//        Request request = new Request.Builder().url(url)
//        		.headers(header).post(requestBody.build()).build();
//		Response response = new OkHttpClient().newBuilder()
//				.connectTimeout(50, TimeUnit.SECONDS)
//				.readTimeout(10, TimeUnit.SECONDS)
//				.build().newCall(request).execute();
//		if(response.isSuccessful()) {
//			System.out.println(response.body().string());
//		}else {
//			System.out.println("-----------"+response.body().string());
//		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
