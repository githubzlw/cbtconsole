package com.importExpress.utli;

import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
		return resultString;
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

    /**
     * @param httpUrl 访问的url
     * @param contains 返回结果中包含内容
     * @param num 如果返回结果不包含此内容的重试次数
     * @return
     */
    public static String doGet(String httpUrl, Map<String, String> param, String contains, Integer num) {
        if (num == null || num == 0){
            return "";
        }
        String refRes = doGet(httpUrl, param);
        if (refRes.contains(contains)) {
            //更新成功
            return refRes;
        } else {
            return doGet(httpUrl, param, contains, --num);
        }
    }

    /**
     * @param httpUrl 访问的url
     * @param contains 返回结果中包含内容
     * @param num 如果返回结果不包含此内容的重试次数
     * @return
     */
    public static String doPost(String httpUrl, Map<String, String> param, String contains, Integer num) {
        if (num == null || num == 0){
            return "";
        }
        String refRes = doPost(httpUrl, param);
        if (refRes.contains(contains)) {
            //更新成功
            return refRes;
        } else {
            return doGet(httpUrl, param, contains, --num);
        }
    }

	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}
	
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

//    private static OkHttpClient client = new OkHttpClient();
//    设置请求超时时间
    private static OkHttpClient client = new OkHttpClient().newBuilder()
                        .connectTimeout(24, TimeUnit.HOURS)
                        .readTimeout(24, TimeUnit.HOURS).build();

    public static boolean postFileNoParam(String url, File file, String suffix)  throws Exception {
        String message = "进行mongodb数据更新(" +System.currentTimeMillis() + "), url:" + url + ", file:" + file;
        System.out.println(message);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().addHeader("Accept","*/*")
                .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .post(body)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if(response != null && response.isSuccessful()){
            String result = response.body().string();
            if (StringUtils.isNotBlank(result) && !result.toLowerCase().contains("failure") && !result.toLowerCase().contains("status:failed")) {
                //保存返回结果
                File resultFile = new File(file.getAbsolutePath() + suffix);
                PrintStream printStream = new PrintStream(new FileOutputStream(resultFile, false));
                printStream.print(result);
                printStream.close();
                return true;
            }
            System.out.println(result);  // 更新错误时候的报错
            return false;
        }else{
            return false;
        }
    }
    
    public static void updateMongodbDate(String url004, String url006, File file, String suffix, String suffixSolr){
        try {
            boolean boo = HttpClientUtil.postFileNoParam(url004, file, suffix);
            if(boo){
                File resultFile = new File(file + suffix);
                if (resultFile.exists()){
                    //刷新solr
                    HttpClientUtil.postFileNoParam(url006, resultFile, suffixSolr);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("updateMongodbDate 调用mongodb刷新接口刷新错误");
        }
    }

    private static final int THREAD_NUMBER = 2; // 2个线程同时刷新本地线上数据

    private static class UpdateMongodbDateRunnable implements Runnable {
        private String url004;
        private String url006;
        private File file;
        private String suffix;
        private String suffixSolr;
        private CountDownLatch countDownLatch;

        public UpdateMongodbDateRunnable(String url004, String url006, File file, String suffix, String suffixSolr, CountDownLatch countDownLatch) {
            this.url004 = url004;
            this.url006 = url006;
            this.file = file;
            this.suffix = suffix;
            this.suffixSolr = suffixSolr;
            this.countDownLatch = countDownLatch;
        }
        public void run() {
            try {
                updateMongodbDate(url004, url006, file, suffix, suffixSolr);//执行更新
            } catch (Exception e) {
                throw new RuntimeException("updateMongodbDate 调用mongodb刷新接口刷新错误");
            } finally {
                countDownLatch.countDown(); //刷新mongodb和solr后 count计数值减一
            }
        }
    }

    public static void main(String[] args)  throws Exception{

    }
}
