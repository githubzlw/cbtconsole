package com.cbt.jcys.util;

import com.importExpress.utli.SearchFileUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
	
	/**
	 * 发送POST 请求
	 * @param url 请求地址
	 * @param charset 编码格式
	 * @param params 请求参数
	 * @return 响应
	 * @throws IOException
	 */
	public static String post(String url, String charset, Map<String, Object> params) throws IOException {
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
		try {
			conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			
			out = new OutputStreamWriter(conn.getOutputStream(), charset);
			out.write(buildQuery(params, charset));
			out.flush();
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
	result.append(tempLine);
            }

		} catch (IOException e) {
//			e.printStackTrace();
            System.out.println("post连接异常, url:" + url);
		} finally {
			if (out != null) {
				out.close();
			}
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
		}
		return result.toString();
	}

    public static String doGet(String httpUrl) {
        StringBuffer htmlCode = new StringBuffer("");
        InputStream in = null;
        BufferedReader breader = null;
        try {
            URL url = new java.net.URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.connect();
//            connection.wait(10800000); // 3小时
            in = connection.getInputStream();
            breader = new BufferedReader(
                    new InputStreamReader(in, "UTF-8"));
            String currentLine;
            while ((currentLine = breader.readLine()) != null) {
                htmlCode.append(currentLine).append("\r\n");
            }
        } catch (Exception e) {
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if(breader != null) {
                try {
                    breader.close();
                } catch (IOException e) {
                }
            }
        }
        return htmlCode.toString();
    }

    /**
     * @param httpUrl 访问的url
     * @param contains 返回结果中包含内容
     * @param num 如果返回结果不包含此内容的重试次数
     * @return
     */
    public static String doGet(String httpUrl, String contains, Integer num) {
        if (num == null || num == 0){
            return "";
        }
        String refRes = doGet(httpUrl);
        if (refRes.contains(contains)) {
            //更新成功
            return refRes;
        } else {
            return doGet(httpUrl, contains, --num);
        }
    }
	
	/**
	 * 将map转换为请求字符串
	 * <p>data=xxx&msg_type=xxx</p>
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String buildQuery(Map<String, Object> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuffer data = new StringBuffer();
		boolean flag = false;

		for (Entry<String, Object> entry : params.entrySet()) {
			if (flag) {
				data.append("&");
			} else {
				flag = true;
			}
			data.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), charset));
		}
		
		return data.toString();
	
	}

    /**
     * 调用线上接口返回对应数据
     * @param uri 线上接口地址（不用域名） 比如 /coupon/deleteCoupon
     * @param params 可变参数，传入的依次为 变量名 变量值；变量名 变量值；...
     * @return
     */
    public static String postCoupon(String uri, String... params){
        if (StringUtils.isBlank(uri)){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (int i = 0; i < params.length; i++) {
                map.put(params[i++], params[i]);
            }
            return post(SearchFileUtils.importexpressPath + uri, "utf-8", map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String key="F3452BD0AE2944C071";
		String data = "[{\"logisticsId\":\"\",\"tradeOrderId\":\"L0011\",\"platformSource\":10663,\"orderSource\":2,\"tradeOrderValue\":\"10\",\"tradeOrderValueUnit\":\"\",\"payableWeight\":12,\"buyerName\":\"11\",\"buyerPhone\":\"11\",\"buyerMobile\":\"11\",\"buyerEmail\":\"11\",\"buyerCountry\":\"UnitedKingdom\",\"buyerProvince\":\"xx\",\"buyerCity\":\"xx\",\"buyerDistrict\":\"xx\",\"buyerStreetaddress\":\"11\",\"buyerZipcode\":\"11\",\"buyerWangwangId\":\"11\",\"buyerQq\":0,\"buyerIdCard\":\"\",\"senderName\":\"11\",\"senderPhone\":\"11\",\"senderMobile\":\"11\",\"senderEmail\":\"11\",\"senderCountry\":\"China\",\"senderProvince\":\"上海市\",\"senderCity\":\"shanghaishi\",\"senderDistrict\":\"11\",\"senderStreetAddress\":\"11\",\"senderZipCode\":\"11\",\"senderWangwangId\":\"11\",\"senderQq\":0,\"warehouseCode\":\"1420016\",\"productId\":129,\"userId\":\"1628007\",\"shipType\":2,\"buyerIdtype\":1,\"netWeight\":1.5,\"senderCompany\":\"XXX\",\"volumWeight1\":\"12\",\"buyerOther\":\"\",\"intlOrderItemList\":[{\"itemId\":\"4901301259769\",	\"itemName\":\"花王纸尿布B型号50枚2包装\",	\"itemUnitPrice\":182,	\"itemQuantity\":1,	\"itemRemark\":\"\",	\"dutyMoney\":0,	\"blInsure\":0,	\"length\":0,\"width\":0,\"high\":0,	\"itemMaterial\":\"\",	\"itemWeight\":0.023,\"currencyType\":\"CNY\",\"itemRule\":\"\",	\"makeCountry\":\"美国\",\"itemUnit\":\"件\"}]}]";
		String str=String.format("%s%s", data,key);
		map.put("data", data);
		map.put("msg_type", "eur.order.insert");
		map.put("data_digest",Md5Helper.md5(str, "utf-8", true));
		map.put("company_id", "SHKR1229007E1081");
		
		try {
			 String json=post("http://intltest.zto.cn/api/Order/init", "UTF-8", map);
			 JSONObject jb = JSONObject.fromObject(json);
			 String orderNo=JSONObject.fromObject(jb.getString("data")).getString("logisticsId");
			 System.out.println(orderNo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
