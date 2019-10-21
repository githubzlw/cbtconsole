package com.winit.api.test;

import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;
import com.winit.api.constants.RequestParam;
import com.winit.api.model.RequestMsg;
import com.winit.api.utils.ApiClient;

public class TokenTest {

    /**
     * 获取调用令牌
     * 
     * @return
     */
    public static String getToken() {
        String token = null;
        RequestMsg requestMsg = new RequestMsg();
        requestMsg.setAction("user.getToken");
        Map<String, String> data = new TreeMap<String, String>();
        data.put("userName", RequestParam.USERNAME);
        data.put("passWord", RequestParam.PASSWORD);
        requestMsg.setData(data);
        ApiClient apiClient = new ApiClient();
        String result = apiClient.post(requestMsg, RequestParam.URL);
        JSONObject json = JSONObject.parseObject(result);
        if (json.containsKey("code") && "0".equals(json.getString("code"))) {
            JSONObject dataResult = json.getJSONObject("data");
            token = dataResult.getString("token");
        }
        return token;
    }
}
