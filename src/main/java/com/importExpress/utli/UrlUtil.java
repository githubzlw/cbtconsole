package com.importExpress.utli;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class UrlUtil {



	    public final static String ZUUL_SEARCH="http://52.34.56.133:18005/api/search-service/";

	    /**
	     * singleton
	     */
	    private static UrlUtil singleton = null;

	    /**
	     * The singleton HTTP client.
	     */
	    private final OkHttpClient client = new OkHttpClient.Builder()
	            .connectTimeout(10, TimeUnit.SECONDS)
	            .readTimeout(20, TimeUnit.SECONDS)
	            .build();

	    /**
	     * 构造函数
	     */
	    private UrlUtil() {

	    }

	    /**
	     * getInstance
	     *
	     * @return
	     */
	    public static UrlUtil getInstance() {

	        if (singleton == null) {
	            synchronized (UrlUtil.class) {
	                if (singleton == null) {
	                    singleton = new UrlUtil();
	                }
	            }
	        }
	        return singleton;
	    }

	    /**
	     * 调用URL（Get）
	     *
	     * @param url
	     * @return
	     * @throws IOException
	     */
	    public JSONObject callUrlByGet(String url) throws IOException {

	        Request request = new Request.Builder().url(url).build();

	        Response response = client.newCall(request).execute();
	        if (!response.isSuccessful()) {
	            throw new IOException("response is not successful");
	        }
	        return response.body() != null ?
	                JSON.parseObject(response.body().string()) : null;
	    }
	    public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
	    /**
	     * 调用URL（Post）
	     *
	     * @param url
	     * @return
	     * @throws IOException
	     */
	    public JSONObject callUrlByPost(String url,Object param) throws IOException {
	        String param_ = JSONObject.toJSONString(param);
	        RequestBody requestBody = RequestBody.create(mediaType, param_);
	        Request request = new Request.Builder()
	                                     .url(url)
	                                     .post(requestBody)
	                                     .build();

	        Response response = client.newCall(request).execute();
	        if (!response.isSuccessful()) {
	            throw new IOException("response is not successful,url:"+url+";param:"+param_);
	        }
	        return response.body() != null ?
	                JSON.parseObject(response.body().string()) : null;
	    }

	    public JSONObject postURL(String url, Map<String,String> params) throws IOException {

	        // Create okhttp3 form body builder.
	        FormBody.Builder formBodyBuilder = new FormBody.Builder();

	        // Add form parameters
	        params.forEach( (k,v) -> {
	            if(v !=null) formBodyBuilder.add(k, v);
	        });

	        // Build form body.
	        FormBody formBody = formBodyBuilder.build();

	        // Create a http request object.
	        Request.Builder builder = new Request.Builder();
	        builder = builder.url(url);
	        builder = builder.post(formBody);
	        Request request = builder.build();

	        // Create a new Call object with post method.
	        Response response = client.newCall(request).execute();
	        if (!response.isSuccessful()) {
	            throw new IOException("response is not successful,url:"+url+",param:"+params.toString());
	        }
	        return response.body() != null ?
	                JSON.parseObject(response.body().string()) : null;
	    }

	    public JSONObject doPut(String url, Map<String,Object> params) throws IOException {
	        FormBody.Builder builder = addParamToBuilder(params);
	        FormBody body = builder.build();
	        Request request = new Request.Builder()
	                .url(url)
	                .put(body)
	                .build();
	        // Create a new Call object with put method.
	        Response response = client.newCall(request).execute();
	        if (!response.isSuccessful()) {
	            throw new IOException("response is not successful");
	        }
	        return response.body() != null ?
	                JSON.parseObject(response.body().string()) : null;
	    }
	    public static FormBody.Builder addParamToBuilder( Map<String,Object> map){
	        FormBody.Builder builder=new FormBody.Builder();
	        if(map!=null){
	            Iterator<Map.Entry<String,Object>> ite= map.entrySet().iterator();
	            for(;ite.hasNext();){
	                Map.Entry<String,Object> kv=ite.next();
	                builder.add(kv.getKey(), kv.getValue().toString());
	            }
	        }
	        return builder;
	    }
	    public JSONObject doDelete(String url, Map<String,Object> params) throws IOException {
	        Request request = new Request.Builder()
	                .url(url)
	                .delete()
	                .build();
	        // Create a new Call object with delete method.
	        Response response = client.newCall(request).execute();
	        if (!response.isSuccessful()) {
	            throw new IOException("response is not successful");
	        }
	        return response.body() != null ?
	                JSON.parseObject(response.body().string()) : null;
	    }
	    public JSONObject doPatch(String url, Map<String,Object> params) throws IOException {
	        FormBody.Builder builder = addParamToBuilder(params);
	        FormBody body = builder.build();
	        Request request = new Request.Builder()
	                .url(url)
	                .patch(body)
	                .build();
	        // Create a new Call object with delete method.
	        Response response = client.newCall(request).execute();
	        if (!response.isSuccessful()) {
	            log.error("url:[{}]",url);
	            throw new IOException("response is not successful");
	        }
	        return response.body() != null ?
	                JSON.parseObject(response.body().string()) : null;
	    }
	    public JSONObject doPost(String url, Map<String,Object> params) throws IOException {
	        FormBody.Builder builder = addParamToBuilder(params);
	        FormBody body = builder.build();
	        Request request = new Request.Builder().addHeader("Accept","*/*")
					.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
	                .url(url)
	                .post(body)
	                .build();
	        return executeCall(url, request);
	    }
	    public JSONObject doGet(String url) throws IOException {
			Request request = new Request.Builder().addHeader("Accept","*/*")
					.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
					.url(url)
					.get()
					.build();

			return executeCall(url, request);
	    }
	/**
	 * call url by retry times
	 * @param url
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private JSONObject executeCall(String url, Request request) throws IOException {
		Response response =null;
		try{
			response = client.newCall(request).execute();
		}catch(IOException ioe){
			//重试15次（每次1秒）
			try {
				int count=0;
				while(true){
					Thread.sleep(1000);
					try {
						response = client.newCall(request).execute();
					} catch (IOException e) {
						log.warn("do retry ,times=[{}]",count);
					}
					if(count>15){
						break;
					}
					++count;
				}
			} catch (InterruptedException e) {
			}
		}

		if (response==null || !response.isSuccessful()) {
			log.error("url:[{}]", url);
			throw new IOException("call url is not successful");
		}

		return response.body() != null ?
				JSON.parseObject(response.body().string()) : null;
	}

}
