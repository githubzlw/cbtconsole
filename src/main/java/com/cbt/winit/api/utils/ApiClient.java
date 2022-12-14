package com.cbt.winit.api.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;
import com.cbt.winit.api.model.RequestMsg;

/**
 * 
 * 客户端发送POST请求
 * @version 
 * <pre>
 *
 * </pre>
 * @since 1.
 */
public class ApiClient {

    public static final String CONTENTTYPE = "application/json";
    public static final String CHARSET     = "UTF-8";

    /**
     * @param requestMsg :请求的参数实体
     * @param url：调用URL
     * @return 调用结果
     */
    public String post(RequestMsg requestMsg, String url) {
        String result = null;
        PostMethod post = null;
        HttpClient client = null;
        try {
            String postData = JSONObject.toJSONString(requestMsg).toString();
            StringRequestEntity entity = new StringRequestEntity(postData, CONTENTTYPE, CHARSET);

            post = new PostMethod(url);
            post.setRequestEntity(entity);

            client = new HttpClient();
            int statusCode = client.executeMethod(post);

            if (statusCode == HttpStatus.SC_OK) {
                result = IOUtils.toString(post.getResponseBodyAsStream(), CHARSET);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return result;
    }
}
