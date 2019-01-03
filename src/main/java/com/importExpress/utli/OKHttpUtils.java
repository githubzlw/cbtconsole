package com.importExpress.utli;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class OKHttpUtils {
	private static OkHttpClient client;
	private static int total = 0;

	static {
		client = new OkHttpClient();
		client.newBuilder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS);
	}
	
	/**
	 * Get请求
	 * @param url 请求链接
	 * @param mHeaders 请求头
	 * @return
	 * @throws IOException
	 */
	public String get(String url,Headers mHeaders) throws Exception {
		checkAndInitOkHttp();

		Request request = new Request.Builder().url(url).headers(mHeaders).build();
	    Response response = client.newCall(request).execute();
	    if(response.isSuccessful()) {
	    	return response.body().string();
	    }else {
	    	throw new Exception("Error:"+response.body().string()+"Exception:"+response);
	    }
	}
	
	/**Post请求
	 * @param url 请求链接
	 * @param mHeaders 请求头
	 * @param mediaType 
	 * @param param 请求参数
	 * @return
	 * @throws IOException
	 */
	public String post(String url,Headers mHeaders,String mediaType,String param) throws Exception{
		checkAndInitOkHttp();

		//设置请求头header
//		System.out.println("----heads:"+mHeaders);
		//设置请求参数
		RequestBody body = RequestBody.create(MediaType.parse(mediaType), param);
		//发送post请求
//		System.out.println("-------body:"+body);
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
	 * @param mHeaders 请求头
	 * @param param 请求参数
	 * @param file 请求文件
	 * @return
	 * @throws IOException
	 */
	public String postFile(String url,Headers mHeaders,String param,File file) throws Exception{

		//设置请求头header
//		System.out.println("----heads:"+mHeaders);
		
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
	 * @param mHeaders 请求头
	 * @param mediaType 
	 * @param param 请求参数
	 * @return
	 * @throws IOException
	 */
	public String patch(String url,Headers mHeaders,String mediaType,String param) throws Exception{
		checkAndInitOkHttp();

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


	public boolean postFileNoParam(String url,File file)  throws Exception {
		checkAndInitOkHttp();

		RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
		MultipartBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("uploadFile", file.getName(), fileBody)
				.build();
		Request request = new Request.Builder().addHeader("Accept","*/*")
				.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
				.post(body)
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		System.err.println(response);
		if(response.isSuccessful()){
			String result = response.body().string();
			System.err.println("upload result:" + result);
			return "1".equals(result);
		}else{
			return false;
		}
	}

	public String postFileNoParam(String uploadFileName,String url,File file)  throws Exception {
		
		checkAndInitOkHttp();

		RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
		MultipartBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart(uploadFileName, file.getName(), fileBody)
				.build();
		Request request = new Request.Builder().addHeader("Accept","*/*")
				.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
				.post(body)
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		if(response.isSuccessful()){
			return response.body().string();
		}else{
			return null;
		}
	}
	
	
	private void checkAndInitOkHttp(){
		total++;
		if (total % 100 == 0) {
			total = 0;
			client = new OkHttpClient();
			client.newBuilder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS);
		}
	}

	public static void main(String[] args) throws Exception{
		OKHttpUtils okHttpUtils = new OKHttpUtils();
		File file = new File("E:/hotJson/1000190129.json");
        okHttpUtils.postFileNoParam("file","http://192.168.1.153:8001/invokejob/b004", file);
	}
}
