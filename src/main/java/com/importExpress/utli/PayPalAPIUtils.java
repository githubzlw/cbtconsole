package com.importExpress.utli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.paypal.base.codec.binary.Base64;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayPalAPIUtils {
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
			CLIENT_ID = resource.getString("LIVE_CLIENT_ID");
			SECRET = resource.getString("LIVE_CLIENT_SECRET");
			URL = resource.getString("LIVE_ENDPOINT");
		}
		TOKEN_URL = URL +"/v1/oauth2/token";
		DISPUTE_URL = URL +"/v1/customer/disputes/";
		DISPUTE_LIST = URL +"/v1/customer/disputes?disputed_transaction_id=";
	}
	
	/**
	 * 获取token
	 * @return
	 * @throws Exception 
	 */
	public String GetAccessToken() throws Exception {
		String authorization = CLIENT_ID + ":" + SECRET;
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
			System.out.println(parseObject);
			return parseObject.getString("access_token");
		}
		return "";
	}
	
	/**
	  * 指定交易申诉列表
	 * @param accessToken
	 * @param disputedTransactionID
	 * @return
	 * @throws Exception
	 */
	public String listDispute(String accessToken, String disputedTransactionID) throws Exception {
		
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
	
	/**申诉详情
	 * @param accessToken access_token
	 * @param disputeID 事件号
	 * @throws Exception 
	 */
	public String showDisputeDetails(String accessToken,String disputeID) throws Exception {
		//设置请求头
		Headers header = new Headers.Builder()
		.set("content-type", "application/json")
		.set("Authorization", "Bearer "+accessToken)
		.build();
			
		String url = DISPUTE_URL + disputeID;
		//请求
		String response = okHttpUtils.get(url, header);
		System.out.println("showDisputeDetails:\r\n"+response);
		return 	response;
	}
	
	
	/**Partially updates a dispute
	 * @param accessToken access_token
	 * @param disputeID 事件号
	 * @throws Exception 
	 */
	public String partiallyUpdates(String accessToken,String disputeID,JSONObject param) throws Exception {
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/";
		//请求
		String mediaType = "application/json;UTF-8";
		String response = okHttpUtils.patch(url, header, mediaType, JSONObject.toJSONString(param));
		System.out.println("showDisputeDetails:\r\n"+response);
		return 	response;
	}
	
	
	
	
	/**接受索赔
	 * @param accessToken
	 * @param disputeID
	 * @return
	 * @throws Exception
	 */
	public String acceptClaim(String accessToken, String disputeID,JSONObject param) throws Exception {
		
			//设置请求头
			Headers header = new Headers.Builder()
			.set("content-type", "application/json")
			.set("Authorization", "Bearer "+accessToken)
			.build();
		
			String url = DISPUTE_URL + disputeID+"/accept-claim";
			//请求
			String mediaType = "application/json";
//			String param = "{\"note\":\"Refund to the you\"}";
			String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
			System.out.println(response);
			return 	response;
	}
	
	
	/**
	 * note:sandbox only
	 * @param accessToken
	 * @param disputeID
	 * @return
	 * @throws Exception 
	 */
	public String updateDisputeStatus(String accessToken, String disputeID) throws Exception {
		//设置请求头
		Headers header = new Headers.Builder()
		.set("content-type", "application/json")
		.set("Authorization", "Bearer "+accessToken)
		.build();
	
		String url = DISPUTE_URL + disputeID+"/require-evidence";
		//请求
		String mediaType = "application/json;UTF-8";
		String param = "{\"action\":\"SELLER_EVIDENCE\"}";
		String response = okHttpUtils.post(url, header, mediaType , param );
		System.out.println(response);
		return 	response;
	}
	/**提议
	 * @param accessToken
	 * @param disputeID
	 * @return
	 * @throws Exception 
	 */
	public String makeOffer(String accessToken, String disputeID,JSONObject param) throws Exception {
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/make-offer";
		//请求
		String mediaType = "application/json;UTF-8";//application/x-www-form-urlencoded
		String param1 = JSONObject.toJSONString(param);
		System.out.println("aaaaaaaaa="+param1);
		String response = okHttpUtils.post(url, header, mediaType , param1);
		return 	response;
	}

	public String makeOffer2(String accessToken, String disputeID,JSONObject param) throws Exception {
		//设置请求头
		okhttp3.Headers header = new okhttp3.Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();

		String url = DISPUTE_URL + disputeID+"/make-offer";

		OkHttpClient okHttpClient = new OkHttpClient();
		String param1 = JSONObject.toJSONString(param);
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, param1);
		Request request = new Request.Builder()
				.url(url)
				.headers(header)
				.post(body)
				.build();

		try {
			Response response = okHttpClient.newCall(request).execute();
			System.out.println(response.body().string());
			return response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";

	}
	/**Acknowledge returned item
	 * @param accessToken
	 * @param disputeID
	 * @return
	 * @throws Exception 
	 */
	public String acknowledgeReturnedItem(String accessToken, String disputeID,JSONObject param) throws Exception {
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/acknowledge-return-item";
		//请求
		String mediaType = "application/json;UTF-8";
		String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
		return 	response;
	}
	/**发送消息
	 * @param accessToken
	 * @param disputeID
	 * @return
	 * @throws Exception 
	 */
	public String sendMessage(String accessToken, String disputeID,JSONObject param) throws Exception {
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "application/json")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/send-message";
		//请求
		String mediaType = "application/json;UTF-8";
		String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
		return 	response;
	}
	/**提供证据
	 * @param accessToken
	 * @param disputeID
	 * @return
	 * @throws Exception 
	 */
	public String provideEvidence(String accessToken, String disputeID,JSONObject param) throws Exception {
		//设置请求头
		Headers header = new Headers.Builder()
				.set("content-type", "Content-Type: multipart/related; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
				.set("Authorization", "Bearer "+accessToken)
				.build();
		
		String url = DISPUTE_URL + disputeID+"/provide-evidence";
		//请求
		String mediaType = "application/json;UTF-8";
		String response = okHttpUtils.post(url, header, mediaType , JSONObject.toJSONString(param) );
		return 	response;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		//数据
    	JSONObject data = new JSONObject();
    	data.put("note", "testconten");
    	data.put("evidence_type", "OTHER");
    	
    	
    	JSONObject returnAddress =  new JSONObject();
    	returnAddress.put("address_line_1", "14,Kimberly st");
    	returnAddress.put("address_line_2", "Open Road North");
    	returnAddress.put("country_code", "US");
    	returnAddress.put("admin_area_1", "Gotham City");
    	returnAddress.put("admin_area_2", "Gotham");
    	returnAddress.put("postal_code", "124566");
    	
    	JSONObject evidence_info =  new JSONObject();
    	
    	JSONObject tracking =  new JSONObject();
    	tracking.put("carrier_name", "FEDEX");
    	tracking.put("tracking_number", "122533485");
    	
    	List<JSONObject> tracking_info = new ArrayList<JSONObject>();
    	tracking_info.add( tracking);
    	
//    	evidence_info.put("tracking_info", tracking_info);
    	
    	
//    	data.put("evidence_info", evidence_info);
    	
//    	data.put("return_shipping_address", returnAddress);
    	
    	
		PayPalAPIUtils utils = new PayPalAPIUtils();
		String accessToken = utils.GetAccessToken();
		String disputeID = "PP-000-043-575-833";
		
		//设置请求头
		Headers mHeaders = new Headers.Builder()
				.set("Content-Type", "multipart/related; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
				.set("Authorization", "Bearer "+accessToken)
				.build();

		File file = new File("C:\\Users\\Administrator\\Desktop\\evidence\\QQ06.png");
		//设置请求参数
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        MediaType MutilPart_Form_Data = MediaType.parse("multipart/form-data; charset=utf-8");
        RequestBody bodyParams = RequestBody.create(MutilPart_Form_Data, JSONObject.toJSONString(data));
        RequestBody bodyParam = RequestBody.create(MutilPart_Form_Data, JSONObject.toJSONString(returnAddress));
        System.out.println(JSONObject.toJSONString(data));
        System.out.println(JSONObject.toJSONString(returnAddress));
        System.out.println(file.getName());
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) 
                .addFormDataPart("file1", "@"+file.getName(), fileBody)
//                .addFormDataPart("input", "", bodyParams)
                .addFormDataPart("return_shipping_address", "", bodyParam);
//                 .addFormDataPart("evidence_type", "PROOF_OF_REFUND")
//                 .addFormDataPart("carrier_name", "FEDEX")
//                .addFormDataPart("tracking_number", "122533485")
//                .addFormDataPart("notes", "testnotes")
//                .addFormDataPart("address_line_1", "14,Kimberly st")
//                .addFormDataPart("address_line_2", "Open Road North")
//                .addFormDataPart("country_code", "US")
//                .addFormDataPart("admin_area_1", "Gotham City")
//                .addFormDataPart("admin_area_2", "Gotham")
//                .addFormDataPart("postal_code", "124566");       
        String url = DISPUTE_URL + disputeID + "/provide-evidence";
    
    //发送post请求
        Request request = new Request.Builder().url(url)
        		.headers(mHeaders).post(requestBody.build()).build();
        OkHttpClient client = new OkHttpClient();
		Response response = client.newBuilder()
				/*.connectTimeout(50, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)*/
				.build().newCall(request).execute();
		System.out.println(response.body().string());
		
//	utils.updateDisputeStatus(accessToken, disputeID);
		
	}

}
