package com.cbt.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cbt.paypal.util.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author luohao
 * @date 2019/12/5
 */
@Slf4j
public class UrlUtil {


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
    public JSONObject callUrlByPost(String url, Object param) throws IOException {
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
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
    public JSONObject doGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
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
}
