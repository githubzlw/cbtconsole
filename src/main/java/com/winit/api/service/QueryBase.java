package com.winit.api.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.winit.api.constants.RequestParam;
import com.winit.api.model.RequestMsg;
import com.winit.api.utils.ApiClient;
import com.winit.api.utils.DateUtil;
import com.winit.api.utils.SignUtil;

public abstract class QueryBase {

    /**
     * 构建业务参数，由子类实现
     */
    protected abstract void setdBusinessData(RequestMsg requestMsg);

    /**
     * 构建调用的接口名称
     */
    protected abstract void setRequestAction(RequestMsg requestMsg);

    /**
     * 解析request调用结果
     * 
     * @param result
     */
    protected abstract void parseRequestResult(String result);

    /**
     * 测试调用步骤
     */
    protected void doAction() {
        RequestMsg requestMsg = new RequestMsg();
        // 1.构建RequestMsg系统级别参数
        setBaseRequestInfo(requestMsg);
        // 2.构建RequestMsg调用的接口名称
        setRequestAction(requestMsg);
        // 3.构建RequestMsg调用的业务参数，需要排序
        setdBusinessData(requestMsg);
        // 4.构建RequestMsg调用的用户签名
        setRequestUserSign(requestMsg, TokenUtil.getToken());
        // 5.构建RequestMsg调用的应用签名
        // 此处clientSecret即创建应用时系统分配的client_secret
        setRequestClientSign(requestMsg, RequestParam.CLIENT_SECRET);
        // 6.发送request请求，解析返回结果
        String result = sendRequest(requestMsg);

        parseRequestResult(result);

    }

    /**
     * 构建请求的系统级别参数
     * 
     * @param requestMsg
     */
    protected void setBaseRequestInfo(RequestMsg requestMsg) {
        // appKey:卖家账号
        requestMsg.setApp_key(RequestParam.USERNAME);

        // format:格式，默认json格式
        requestMsg.setFormat("json");

        // version:版本，默认1.0
        requestMsg.setVersion("1.0");

        // sign_method:加密方式，默认md5
        requestMsg.setSign_method("md5");

        // timestamp:调用时间戳，系统当前时间，格式：yyyy-MM-dd HH:mm:ss
        requestMsg.setTimestamp(DateUtil.parse2String(new Date(), DateUtil.FULL_DATE_STRING));

        // language:语言，默认：zh_CN
        requestMsg.setLanguage("zh_CN");

        // platform:平台。与Client_id一一对应，即应用创建时填写的CODE，推荐使用公司英文简称或中文汉语首字母组合，大写
        requestMsg.setPlatform(RequestParam.CLIENT_CODE);

        // client_id:即应用的key，由注册应用时系统自动生成，不可修改
        requestMsg.setClient_id(RequestParam.CLIENT_ID);
    }

    /**
     * 生成用户签名sign
     * 
     * @param requestMsg
     * @param token
     */
    protected void setRequestUserSign(RequestMsg requestMsg, String token) {
        String sign = SignUtil.getSign(requestMsg, token);
        requestMsg.setSign(sign);

    }

    /**
     * 生成应用签名client_sign
     * 
     * @param requestMsg
     * @param clientSecret
     */
    protected void setRequestClientSign(RequestMsg requestMsg, String clientSecret) {
        String clientSign = SignUtil.getSign(requestMsg, clientSecret);
        requestMsg.setClient_sign(clientSign);

    }

    protected String sendRequest(RequestMsg requestMsg) {
        ApiClient apiClient = new ApiClient();
        String result = apiClient.post(requestMsg, RequestParam.URL);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject json = JSONObject.parseObject(result);
            String code = json.getString("code");
            // 错误码"0"表示业务请求成功，非"0"标识业务操作异常
            if ("0".equals(code)) {
                System.out.println("成功");
                System.out.println(result);
            } else {
                System.err.println(result);
            }
        }

        return result;

    }
}
