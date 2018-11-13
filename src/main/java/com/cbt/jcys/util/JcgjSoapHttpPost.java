package com.cbt.jcys.util;

import com.cbt.jcys.bean.PriceData;
import com.cbt.jcys.bean.PriceReturnJson;
import com.cbt.jcys.bean.PriceReturnJsonNew;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JcgjSoapHttpPost {
	// GetNumber 运单
	public static final String GETTNUMBER_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><GetNumber xmlns=\"http://www.jcex.com/\"><country>p_country</country><city>p_city</city></GetNumber></soap12:Body></soap12:Envelope>";
	public static final String GETTNUMBER_PARAMETER_COUNTRY = "p_country";
	public static final String GETTNUMBER_PARAMETER_CITY = "p_city";
	public static final String GETTNUMBER_URL = "http://115.238.63.133:8012/Jcex.asmx?op=GetNumber";
	public static final String GETTNUMBER_RETURN_PLACEHOLDER = "GetNumberResult";

	// REQUEST
	public static final String REQUEST_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><Request xmlns=\"http://www.jcex.com/\"><jsonstr>p_jsonstr</jsonstr></Request></soap12:Body></soap12:Envelope>";
	public static final String REQUEST_PARAMETER_JSONSTR = "p_jsonstr";
	public static final String REQUEST_URL = "http://115.238.63.133:8012/Jcex.asmx?op=Request";
	public static final String REQUEST_RETURN_PLACEHOLDER = "RequestResult";

	// BILLNO
	public static final String BILLNO_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><billno xmlns=\"http://www.jcex.com/\"><billno1>p_billno1</billno1></billno></soap12:Body></soap12:Envelope>";
	public static final String BILLNO_PARAMETER_BILLNO1 = "P_billno1";
	public static final String BILLNO_URL = "http://115.238.63.133:8012/Jcex.asmx?op=billno";
	public static final String BILLNO_RETURN_PLACEHOLDER = "billnoResult";

	// Price 报价 baojia
	public static final String PRICE_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><Price xmlns=\"http://www.jcex.com\"><baojia>p_baojia</baojia></Price></soap12:Body></soap12:Envelope>";
	public static final String PRICE_PARAMETER_BAOJIA = "p_baojia";
	public static final String PRICE_URL = "http://115.238.63.133:8011/Price.ashx?Pricejson=";
	public static final String PRICE_URL2 = "http://115.238.63.133:8088/Service1.asmx?op=Price";
	public static final String PRICE_RETURN_PLACEHOLDER = "PriceResult";

	// track_cn 运单跟踪
	public static final String TRACK_CN_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><track_cn xmlns=\"http://www.jcex.com\"><trackid>p_trackid</trackid></track_cn></soap12:Body></soap12:Envelope>";
	public static final String TRACK_CN_PARAMETER_TRACKID = "p_trackid";
	public static final String TRACK_CN_URL = "http://115.238.63.133:8088/service1.asmx?op=track_cn";
	public static final String TRACK_CN_RETURN_PLACEHOLDER = "track_cnResult";

	/**
	 * 将parram字符串参数里面的占位符，替换成实际的值
	 * 
	 * @param param
	 *            要替换的字符串
	 * @param params
	 *            Map<"替换的字符","实际值">
	 * @return 返回替换之后的结果
	 * @throws Exception
	 */
	public String replace(String param, Map<String, String> params) throws Exception {
		String result = param; // xml

		if (params != null && !params.isEmpty()) {
			// 拼凑占位符
			for (Map.Entry<String, String> entry : params.entrySet()) {

				String name = entry.getKey();
				Pattern p = Pattern.compile(name);

				Matcher m = p.matcher(result);
				if (m.find()) {
					result = m.replaceAll(entry.getValue());
				}
			}
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * soap访问service
	 * 
	 * @param input
	 *            请求的xml格式
	 * @param map
	 *            替换xml里的占位符的值
	 * @param strurl
	 *            请求的url地址
	 * @return 返回的结果也是xml格式
	 * @throws Exception
	 */
	public synchronized String soapService(String input, Map<String, String> map, String strurl) throws Exception {
		String str = "";
		String soap = replace(input, map);
		HttpURLConnection conn = null;
		byte[] b = null;
		OutputStream outPut = null;
		try {
			// 封装数据，数据以byte方式传输
			b = soap.getBytes();
			// 需要请求的连接
			URL url = new URL(strurl);
			// 打开连接
			conn = (HttpURLConnection) url.openConnection();
			// 设置请求方式和消息头以及超时时间
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(8000);
			// 设置是否允许对外输出数据
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("Content-Length", String.valueOf(b.length));
			// 取得输出流
			outPut = conn.getOutputStream();
			outPut.write(b);
			outPut.flush();
			// 判断请求是否成功
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				byte[] b1 = new byte[1024];
				int len = 0;
				int temp = 0;
				while ((temp = in.read()) != -1) {
					b1[len] = (byte) temp;
					len++;
				}
				str = new String(b1, "UTF-8");
			} else {
				System.out.println("失败-----" + conn.getResponseCode());
			}
		} catch (Exception e) {
			System.out.println("失败-----" + conn.getResponseCode());
		} finally {
			try {
				if (outPut != null) {
					outPut.close();
				}
				if (conn != null) {
					conn.getContentLength();
					InputStream is = conn.getInputStream();
//					OutputStream out=conn.getOutputStream();
				    is.close();
//				    out.close();
				    conn.disconnect();
				}
				if (b != null) {
					b = null;
				}
			} catch (IOException e) {
				System.out.println("关闭连接失败");
			}
		}
		return str;
	}

	/**
	 * 根据指定标签，取出标签里面的值
	 * 
	 * @param str
	 *            数据源
	 * @param ref
	 *            要取的标签
	 * @return
	 */
	public String getReturnStr(String str, String ref) {

		if (str != null && !"".equals(str) && ref != null && "".equals(ref)) {
			return "";
		}
		int startIndex = str.indexOf("<" + ref + ">", 0);
		int endIndex = str.indexOf("</" + ref + ">", startIndex);
		return str.substring(startIndex + ref.length() + 2, endIndex);
	}

	/**
	 * 将java对象转json字符串
	 * 
	 * @param o
	 * @return
	 */
	public String objToGson(Object o) {
		if (o == null) {
			return "";
		}
		return (new Gson().toJson(o));
	}

	public String objToJson(Object o) {

		if (o == null) {
			return "";
		}
		return (new GsonBuilder().serializeNulls().create().toJson(o));
	}

	// REQUEST
	/**
	 * 新增运单资料
	 * 
	 * @param json
	 *            新增运单数据 格式为json
	 * @return
	 * @throws Exception
	 */
	public String callRequest(String json) throws Exception {
		String str = "";

		if (json == null || "".equals(json)) {
			return "参数不能为空";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(JcgjSoapHttpPost.REQUEST_PARAMETER_JSONSTR, json);

		String ret = this.soapService(JcgjSoapHttpPost.REQUEST_XML, map, JcgjSoapHttpPost.REQUEST_URL);

		// System.out.println(ret);

		str = this.getReturnStr(ret, JcgjSoapHttpPost.REQUEST_RETURN_PLACEHOLDER);
		return str;
	}

	// GETTNUMBER
	/**
	 * 获取单号
	 * 
	 * @param country
	 *            国家
	 * @param city
	 *            城市
	 * @return
	 * @throws Exception
	 */
	public String callGetNumber(String country, String city){
		String str = "";
		try{
			if (country == null || "".equals(country) || city == null || "".equals(city)) {
				return "参数不能为空";
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put(JcgjSoapHttpPost.GETTNUMBER_PARAMETER_COUNTRY, country);
			map.put(JcgjSoapHttpPost.GETTNUMBER_PARAMETER_CITY, city);

			String ret = this.soapService(JcgjSoapHttpPost.GETTNUMBER_XML, map, JcgjSoapHttpPost.GETTNUMBER_URL);
			str = this.getReturnStr(ret, JcgjSoapHttpPost.GETTNUMBER_RETURN_PLACEHOLDER);
		}catch (Exception e){
			str="";
		}
		return str;
	}

	// BILLNO
	/**
	 * 查询转单号
	 * 
	 * @param billno1
	 *            转单号
	 * @return
	 * @throws Exception
	 */
	public String callBillno(String billno1) throws Exception {
		String str = "";
		if (billno1 == null || "".equals(billno1)) {
			return "参数不能为空";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(JcgjSoapHttpPost.BILLNO_PARAMETER_BILLNO1, billno1);

		String ret = this.soapService(JcgjSoapHttpPost.BILLNO_XML, map, JcgjSoapHttpPost.BILLNO_URL);

		// System.out.println(ret);

		str = this.getReturnStr(ret, JcgjSoapHttpPost.BILLNO_RETURN_PLACEHOLDER);
		return str;
	}

	/**
	 * 计算运费
	 * 
	 * @param baojia
	 * @return
	 * @throws Exception
	 */
	public String callPrice(String baojia) throws Exception {
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(JcgjSoapHttpPost.PRICE_URL + baojia);
			URLConnection uc = urlObject.openConnection();
			// uc.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public String preInputSet(String data){
		String ret = "";
		URL url = null;
		HttpURLConnection httpurlconnection = null;
		InputStream in = null;
		BufferedReader breader = null;
		try {
			url = new URL("http://api.cnexps.com/cgi-bin/EmsData.dll?DoApi");
			// 以post方式请求
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.getOutputStream().write(data.getBytes());
			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();
			in = httpurlconnection.getInputStream();
			breader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String str = breader.readLine();
			while (str != null) {
				ret += str;
				str = breader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpurlconnection != null) {
				httpurlconnection.getContentLength();
			    httpurlconnection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (breader != null) {
				try {
					breader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	public String callPrice2(String baojia) throws Exception {
		String str = "";
		if (baojia == null || "".equals(baojia)) {
			return "参数不能为空";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(JcgjSoapHttpPost.PRICE_PARAMETER_BAOJIA, baojia);
		// map.put()

		String ret = this.soapService(JcgjSoapHttpPost.PRICE_XML, map, JcgjSoapHttpPost.PRICE_URL2);

		// System.out.println(ret);

		str = this.getReturnStr(ret, JcgjSoapHttpPost.PRICE_RETURN_PLACEHOLDER);
		return str;
	}

	/**
	 * json转list
	 * 
	 * @param str
	 * @return list
	 * @throws Exception
	 */
	public List<PriceReturnJsonNew> getJsonToPriceReturnJson(String str) throws Exception {
		List<PriceReturnJson> list = null;
		List<PriceReturnJsonNew> prjlist = null;
		if (str != null && !"{\"page\":false,\"result\":false,\"msg\":\"未查询到相应的报价,请再另换一组参数试试查询其它的报价\"}".equals(str)) {

			if (str.indexOf("JSJG") != -1) {

				JSONArray jArray = JSONArray.fromObject("[" + str + "]");
				// list=JSONArray.toList(jArray,PriceReturnJson.class);

				prjlist = JSONArray.toList(jArray, PriceReturnJsonNew.class);

			}
		}

		// Gson gson = new Gson();
		// List<PriceReturnJson> list = gson.fromJson(str, new
		// TypeToken<List<PriceReturnJson>>(){}.getType());
		return prjlist;
	}

	/**
	 * 佳成运费计算
	 * 
	 * @param pd
	 * @return
	 */
	public List<PriceReturnJsonNew> getJcFreight(PriceData pd) {
		System.out.println(this.objToJson(pd));
		List<PriceReturnJsonNew> list = null;
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Map pacs[] = new HashMap[1];
		Map<String, Object> pacdata = new HashMap<String, Object>();
		// 组装
		pacdata.put("DataInfo", pd); // di 运单 pd 报价
		pacs[0] = pacdata;
		mapdata.put("Package", pacs);

		String strpac = this.objToJson(mapdata);
		System.out.println(strpac);

		String ret = "";
		try {
			ret = this.callPrice(strpac);
			System.out.println("callPrice__返回结果：" + ret);
			if (ret.indexOf("查询服务异常") != -1) {
				list = this.getJsonToPriceReturnJson(ret);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// BILLNO
	/**
	 * 查询转单号 TRACK_CN
	 * 
	 * @param trackid
	 *            转单号
	 * @return
	 * @throws Exception
	 */
	public String callTrackCn(String trackid) throws Exception {
		String str = "";
		if (trackid == null || "".equals(trackid)) {
			return "参数不能为空";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(JcgjSoapHttpPost.TRACK_CN_PARAMETER_TRACKID, trackid);

		String ret = this.soapService(JcgjSoapHttpPost.TRACK_CN_XML, map, JcgjSoapHttpPost.TRACK_CN_URL);

		System.out.println("结果——————" + ret);

		// str =
		// this.getReturnStr(ret,JcgjSoapHttpPost.TRACK_CN_RETURN_PLACEHOLDER);
		return str;
	}

	public String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		String result = "";
		char[] charArray = null;
		byte[] byteArray = null;
		byte[] md5Bytes = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			charArray = inStr.toCharArray();
			byteArray = new byte[charArray.length];

			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];
			md5Bytes = md5.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			result = hexValue.toString();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		} finally {
			charArray = null;
			byteArray = null;
			md5Bytes = null;
		}

		return result;

	}

}
