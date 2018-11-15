package com.importExpress.utli;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

import okhttp3.*;
import okhttp3.Request.Builder;



public class OKHttpUtils {
	private OkHttpClient client = new OkHttpClient();
	
	/**
	 * Get请求
	 * @param url 请求链接
	 * @param header 请求头
	 * @return
	 * @throws IOException
	 */
	public String get(String url,Headers mHeaders) throws Exception {
		Request request = new Request.Builder().url(url).headers(mHeaders).build();
	    Response response = client.newCall(request).execute();
	    if(response.isSuccessful()) {
	    	return response.body().string();
	    }else {
	    	throw new Exception("result:"+response.body().string()+"Exception:"+response);
	    }
	}
	
	/**Post请求
	 * @param url 请求链接
	 * @param header 请求头
	 * @param mediaType 
	 * @param param 请求参数
	 * @return
	 * @throws IOException
	 */
	public String post(String url,Headers mHeaders,String mediaType,String param) throws Exception{
		
		//设置请求头header
		System.out.println("----heads:"+mHeaders);
		//设置请求参数
		RequestBody body = RequestBody.create(MediaType.parse(mediaType), param);
		//发送post请求
		System.out.println("-------body:"+body);
		Request request = new Request.Builder().url(url).headers(mHeaders).post(body).build();
	    
	    Response response = client.newCall(request).execute();
	    if(response.isSuccessful()) {
	    	return response.body().string();
	    }else {
	    	throw new Exception("result:"+response.body().string()+"Exception:"+response);
	    }
	}
	/**Post请求
	 * @param url 请求链接
	 * @param header 请求头
	 * @param mediaType 
	 * @param param 请求参数
	 * @return
	 * @throws IOException
	 */
	public String postFile(String url,Headers mHeaders,String param,File file) throws Exception{
		
		//设置请求头header
		System.out.println("----heads:"+mHeaders);
		
		//设置请求参数
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        
        RequestBody paramBody = RequestBody.create(MediaType.parse("application/json"), param);
        
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) 
                .addFormDataPart("evidence", file.getName(), fileBody)
                .addFormDataPart("evidence_type", "PROOF_OF_FULFILLMENT")
                .addFormDataPart("carrier_name", "FEDEX")
                .addFormDataPart("tracking_number", "122533485")
                .addFormDataPart("notes", "testnotes")
                .addFormDataPart("address_line_1", "14,Kimberly st")
                .addFormDataPart("address_line_2", "Open Road North")
                .addFormDataPart("country_code", "US")
                .addFormDataPart("admin_area_1", "Gotham City")
                .addFormDataPart("admin_area_2", "Gotham")
                .addFormDataPart("postal_code", "124566");
//       if (param != null) {
//	        // map 里面是请求中所需要的 key 和 value
//    	   Iterator<Entry<String, String>> iterator = param.entrySet().iterator();
//    	   while (iterator.hasNext()) {
//    		   Entry<String, String> next = iterator.next();
//    		   requestBody.addFormDataPart(next.getKey(), next.getValue());
//		}
//    	   System.out.println(requestBody.toString());
//    }
       System.out.println(param.toString());
    
    //发送post请求
        Request request = new Request.Builder().url(url)
        		.headers(mHeaders).post(requestBody.build()).build();
		
		Response response = client.newBuilder()
				.connectTimeout(50, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.build().newCall(request).execute();
		if(response.isSuccessful()) {
			return response.body().string();
		}else {
			throw new Exception("result:"+response.body().string()+"Exception:"+response);
		}
		
		
		
	}
	/**Patch请求
	 * @param url 请求链接
	 * @param header 请求头
	 * @param mediaType 
	 * @param param 请求参数
	 * @return
	 * @throws IOException
	 */
	public String patch(String url,Headers mHeaders,String mediaType,String param) throws Exception{
		
		//设置请求参数
		RequestBody formBody = RequestBody.create(MediaType.parse(mediaType), param);
		
		//发送patch请求
		Request request = new Request.Builder()
									.url(url)
									.headers(mHeaders)
									.patch(formBody)
									.build();
		
		Response response = client.newCall(request).execute();
		if(response.isSuccessful()) {
	    	return response.body().string();
	    }else {
	    	throw new Exception("result:"+response.body().string()+"Exception:"+response);
	    }
		
	}
	
}
