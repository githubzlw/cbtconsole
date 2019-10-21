package com.importExpress.utli;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cbt.parse.service.DownloadMain;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


public class OKHttpUtils {
    private static final Log logger = LogFactory.getLog(OKHttpUtils.class);
    private static OkHttpClient client;
    private static int total = 0;

    /**
     * 产品上下架图片处理接口
     * 接口地址（get或者post都可以）
     * 	后台下架
     * 		http://192.168.1.48:18079/syncsku/cbt/updateOffShellGoods.do?pid=12312313&valid=0&unsellableReason=6&method=2
     * 	后台上架
     * 		http://192.168.1.48:18079/syncsku/cbt/updateOffShellGoods.do?pid=12312313&valid=1&unsellableReason=45&method=2
     *
     * 参数
     * 	method 更新方式 1-添加到上下架列表 后续定时走; 2- 实时更新;
     * 	valid	更新产品表上下架值 按照上面地址提供
     * 	unsellableReason  更新产品表原因值 按照上面地址提供
     *
     * 返回值
     * 	state
     * 		true-更新成功；
     * 		false-更新失败
     * 			失败时候 message 是失败原因；
     */
    private static final String IMG_ONLINE_AND_DELETE_URL = "http://192.168.1.48:18079/syncsku/cbt/updateOffShellGoods.do";

    static {
		/*client = new OkHttpClient().Builder().connectTimeout(600, TimeUnit.SECONDS).readTimeout(300, TimeUnit.SECONDS)
		.writeTimeout(300, TimeUnit.SECONDS);*/

        client = getClientInstence();
    }

    public static OkHttpClient getClientInstence() {
        OkHttpClient clientIns = new OkHttpClient.Builder().connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS).writeTimeout(600, TimeUnit.SECONDS).build();
        return clientIns;
    }


    /**
     * 说明
     * 后台对产品上下架的更新
     * 后台下架
     * http://192.168.1.48:18079/syncsku/cbt/updateNeedOffShell.do?pid=12312313&valid=0&unsellableReason=6&method=1
     * 后台上架
     * http://192.168.1.48:18079/syncsku/cbt/updateNeedOffShell.do?pid=12312313&valid=1&unsellableReason=45&method=1
     * @param method 更新方式 1-添加到上下架列表 后续定时走; 2- 实时更新;
     */
    public static boolean optionGoodsInterface(String pid, int valid, int unsellableReason, int method) {
        /*boolean isSu = false;
        try {
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//					.addFormDataPart("image", originFile.getName(),
                    .addFormDataPart("pid", pid)
                    .addFormDataPart("valid", String.valueOf(valid))
                    .addFormDataPart("unsellableReason", String.valueOf(unsellableReason))
                    .addFormDataPart("method", String.valueOf(method))
                    .build();*/
            // Request request = new Request.Builder().addHeader("Accept", "*/*").addHeader("Connection", "close").addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").url(IMG_ONLINE_AND_DELETE_URL).post(formBody).build();
            // client = new OkHttpClient();
            /*OkHttpClient client = getClientInstence();
            Response response = client.newCall(request).execute();
            String rs = response.body().string();
            String url = IMG_ONLINE_AND_DELETE_URL + "?pid=" + pid + "&valid=" + valid
                    + "&unsellableReason=" + unsellableReason + "&method=" + method;
            String rs = DownloadMain.getContentClient(url, null);
            JSONObject json = JSON.parseObject(rs);
            System.out.println(rs);
            if (json.containsKey("state") && json.getBooleanValue("state")) {
                isSu = true;
            } else {
                isSu = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("optionGoodsInterface error:", e);
        }
        return isSu;*/
        return true;
    }

    /**
     * Get请求
     *
     * @param url      请求链接
     * @param mHeaders 请求头
     * @return
     * @throws IOException
     */
    public String get(String url, Headers mHeaders) throws Exception {
        checkAndInitOkHttp();

        Request request = new Request.Builder().url(url).headers(mHeaders).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new Exception("Error:" + response.body().string() + "Exception:" + response);
        }
    }

    /**
     * Post请求
     *
     * @param url       请求链接
     * @param mHeaders  请求头
     * @param mediaType
     * @param param     请求参数
     * @return
     * @throws IOException
     */
    public String post(String url, Headers mHeaders, String mediaType, String param) throws Exception {
        checkAndInitOkHttp();

        //设置请求头header
//		System.out.println("----heads:"+mHeaders);
        //设置请求参数
        RequestBody body = RequestBody.create(MediaType.parse(mediaType), param);
        //发送post请求
//		System.out.println("-------body:"+body);
        Request request = new Request.Builder().url(url).headers(mHeaders).post(body).build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new Exception("result:" + response.body().string() + "Exception:" + response);
        }
    }

    /**
     * Post请求
     *
     * @param url      请求链接
     * @param mHeaders 请求头
     * @param param    请求参数
     * @param file     请求文件
     * @return
     * @throws IOException
     */
    public String postFile(String url, Headers mHeaders, Map<String, Object> param, Map<String, File> fileMap) throws Exception {
        //设置请求参数
        MediaType MutilPart_Form_File = MediaType.parse("multipart/form-data; charset=utf-8");
        RequestBody fileBody = null;
        MediaType MutilPart_Form_Data = MediaType.parse("application/json");
        RequestBody bodyParams = null;//RequestBody.create(MutilPart_Form_Data,param);


        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        Iterator<Entry<String, File>> iteratorFile = fileMap.entrySet().iterator();

        while (iteratorFile.hasNext()) {
            Entry<String, File> fileNext = iteratorFile.next();
            fileBody = RequestBody.create(MutilPart_Form_File, fileNext.getValue());
            requestBody.addFormDataPart(fileNext.getKey(), "@" + fileNext.getValue().getName(), fileBody);
        }

        Iterator<Entry<String, Object>> iteratorParam = param.entrySet().iterator();
        while (iteratorParam.hasNext()) {
            Entry<String, Object> paramNext = iteratorParam.next();
            bodyParams = RequestBody.create(MutilPart_Form_Data, JSON.toJSONString(paramNext.getValue()));
//	      	System.out.println(paramNext.getKey()+"===="+JSON.toJSONString(paramNext.getValue()));
            requestBody.addFormDataPart(paramNext.getKey(), "", bodyParams);
        }


        //发送post请求o
        Request request = new Request.Builder().addHeader("Connection", "close").url(url)
                .headers(mHeaders).post(requestBody.build()).build();
        Response response = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new Exception("result:" + response.body().string() + "Exception:" + response);
        }
    }

    /**
     * Patch请求
     *
     * @param url       请求链接
     * @param mHeaders  请求头
     * @param mediaType
     * @param param     请求参数
     * @return
     * @throws IOException
     */
    public String patch(String url, Headers mHeaders, String mediaType, String param) throws Exception {
        checkAndInitOkHttp();

        //设置请求参数
        RequestBody formBody = RequestBody.create(MediaType.parse(mediaType), param);

        //发送patch请求
        Request request = new Request.Builder().addHeader("Connection", "close")
                .url(url)
                .headers(mHeaders)
                .patch(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new Exception("result:" + response.body().string() + "Exception:" + response);
        }

    }


    public boolean postFileNoParam(String url, File file) throws Exception {
        checkAndInitOkHttp();

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uploadFile", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().addHeader("Accept", "*/*").addHeader("Connection", "close")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .post(body)
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        System.err.println(response);
        if (response.isSuccessful()) {
            String result = response.body().string();
            System.err.println("upload result:" + result);
            return "1".equals(result);
        } else {
            return false;
        }
    }

    public String postFileNoParam(String uploadFileName, String url, File file) throws Exception {

        checkAndInitOkHttp();

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(uploadFileName, file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().addHeader("Accept", "*/*").addHeader("Connection", "close")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .post(body)
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }
    }


    private void checkAndInitOkHttp() {
        total++;
        if (total % 100 == 0) {
            total = 0;
            client = getClientInstence();
        }
    }

    public static void main(String[] args) throws Exception {
        OKHttpUtils okHttpUtils = new OKHttpUtils();
        File file = new File("E:/hotJson/1000190129.json");
        okHttpUtils.postFileNoParam("file", "http://192.168.1.153:8001/invokejob/b004", file);
    }
}
