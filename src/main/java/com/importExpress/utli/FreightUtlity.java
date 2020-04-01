package com.importExpress.utli;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.cbt.common.UrlUtil;
import com.google.gson.Gson;
import com.importExpress.pojo.CommonResult;
import com.importExpress.pojo.ResultBean;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.util.GetConfigureInfo;
import com.cbt.util.StrUtils;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;

/**
 * *****************************************************************************************
 *
 * @ClassName FreightUtlity
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2018/12/18 21:21:25
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       21:21:252018/12/18     cjc                       初版
 * ******************************************************************************************
 */
@Service
@Slf4j
public class FreightUtlity {
    private static final Logger logger = LoggerFactory.getLogger(FreightUtlity.class);
    private static final String getFreightCostUrl = GetConfigureInfo.getValueByCbt("getMinFreightUrl");
    @Autowired
    private SendMailFactory sendMailFactory;
    private static UrlUtil instance = UrlUtil.getInstance();
    public static double getFreightByOrderno(String orderNo) {
        double freight = 0;
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder().add("orderNo", String.valueOf(orderNo)).build();
        /*Request request = new Request.Builder().url(getFreightCostUrl).post(formBody).build();*/
        String url = getFreightCostUrl.replace("getMinFreightByUserId","getFreightByOrderNo");
        Request request = new Request.Builder().addHeader("Accept","*/*")
				.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .url(url).post(formBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String resultStr = response.body().string();
            JSONObject json = JSONObject.fromObject(resultStr);
            if (json.getBoolean("ok")) {
                Map<String, Double> data = (Map<String, Double>) json.getJSONObject("data");
                freight = (Double) data.get("freightCost");
                freight = new BigDecimal(freight*6.6).setScale(2,BigDecimal.ROUND_UP).doubleValue();
            } else {
                logger.warn("getFreightByOrderno error :<:<:<");
            }
        } catch (Exception e) {
            logger.warn("getFreightByOrderno error,orderNo:[{}],e:[{}]" + orderNo + e.getMessage());
        }
        return freight;
    }
    public static double getFreightByWeight(int countryId,String weight,String modeTransport) {
    	double freight = 0;
    	OkHttpClient okHttpClient = new OkHttpClient();
    	
    	RequestBody formBody = new FormBody.Builder().add("countryId", String.valueOf(countryId)).add("weight", weight).build();
    	/*Request request = new Request.Builder().url(getFreightCostUrl).post(formBody).build();*/
    	String url = getFreightCostUrl.replace("getMinFreightByUserId","getFreightByWeight");
    	Request request = new Request.Builder().addHeader("Accept","*/*")
    			.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    			.url(url).post(formBody).build();
    	try {
    		Response response = okHttpClient.newCall(request).execute();
    		String resultStr = response.body().string();
    		JSONObject json = JSONObject.fromObject(resultStr);
    		if (json.getBoolean("ok")) {
    			JSONArray jsonObject = json.getJSONArray("data");
    			for(int i=0;i<jsonObject.size();i++) {
    				JSONObject fromObject = JSONObject.fromObject(jsonObject.get(i));
    				if(modeTransport.equals(fromObject.get("shippingmethod"))) {
    					freight = (Double) fromObject.get("freightCost");
    				}
    			}
    		} else {
    			logger.warn("getFreightByWeight error :<:<:<");
    		}
    	} catch (Exception e) {
    		logger.warn("getFreightByOrderno error,countryId:[{}],e:[{}]" + countryId + e.getMessage());
    	}
    	return freight;
    }
    public void sendMailNew(String email, String copyEmail, String title, Map<String, Object> map,Enum<TemplateType> templateType) {
        try {
            sendMailFactory.sendMail(email, copyEmail, title, map,templateType);
        } catch (Exception e) {
            e.printStackTrace();
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(map);
            String modeStr = jsonObject.toString();
            logger.error("genOrderSplitEmail: email:"+map.get("email")+" model_json:"+ modeStr +" e.message:"+ e.getMessage());
        }
    }
    /**
     * @Title getPriceListStr
	 * @Description TODO 这个是拼接  价格区间
	 * @param priceListStr 价格区间的长长的Str
	 * @return  按照规则拼接好的
	 * @return String
	 */
    public static String getPriceListStrTwo(int priceListSize,String[] priceListStr) {
        int Batch_Quantity =0;
        //初始值等于moq 就行
        double Batch_Price = 0;
        //初始值等于产品单页的价格就行
        int Batch_Quantity1 = 0;
        //初始值等于moq 就行
        double Batch_Price1 = 0;
        //初始值等于产品单页的价格就行
        int Batch_Quantity0 = 0;
        //初始值等于moq 就行
        double Batch_Price0 = 0;
        String priceListString = null;
        JSONArray jsonArray = JSONArray.fromObject(priceListStr);
        int listSize = jsonArray.size();
        try {
            switch (listSize) {
                case 1:
                    String Batch_Quantity_Price_List0 = priceListStr[0];
                    String[] wholesalePriceTems = Batch_Quantity_Price_List0.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity0 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0]  : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                   /* System.out.println("===============");
                    Arrays.stream(wholesalePriceTems).forEach(e->{
                        System.out.println("wholesalePriceTems-="+e);
                    });*/
                    if (wholesalePriceTems.length>=2 && StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price0 = Double.parseDouble(wholesalePriceTems[1].toString().indexOf("-")>-1?wholesalePriceTems[1].toString().split("-")[0]:wholesalePriceTems[1].toString());
                    }
                    break;
                case 2:
                    Batch_Quantity_Price_List0 = priceListStr[0];
                    wholesalePriceTems = Batch_Quantity_Price_List0.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity0 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0]  : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price0 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }

                    String Batch_Quantity_Price_List1 = priceListStr[1];
                    wholesalePriceTems = Batch_Quantity_Price_List1.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity1 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0]  : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price1 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }
                    break;


                case 3:
                    Batch_Quantity_Price_List0 = priceListStr[0];
                    wholesalePriceTems = Batch_Quantity_Price_List0.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity0 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0]  : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price0 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }

                    Batch_Quantity_Price_List1 = priceListStr[1];
                    wholesalePriceTems = Batch_Quantity_Price_List1.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity1 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0]  : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price1 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }
                    String Batch_Quantity_Price_List2 = priceListStr[2];
                    wholesalePriceTems = Batch_Quantity_Price_List2.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0]  : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price = Double.parseDouble(wholesalePriceTems[1].toString());
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getPriceListStrTwo error, priceListStr: "+ Arrays.toString(priceListStr));
        }

//	        }
        if (Batch_Quantity0>0 && Batch_Price0>0) {
            priceListString = String.valueOf(Batch_Quantity0)+"$"+String.valueOf(Batch_Price0);
            if (Batch_Quantity1 > 0 && Batch_Price1 > 0) {
                priceListString = priceListString+String.valueOf(","+Batch_Quantity1)+"$"+String.valueOf(Batch_Price1);
                if (Batch_Quantity > 0 && Batch_Price > 0) {
                    priceListString = priceListString+String.valueOf(","+Batch_Quantity)+"$"+String.valueOf(Batch_Price);
                }
            }
        }
        return priceListString;
    }
    /**
     * @Title: getNextIntervalPrice
     * @Author: cjc
     * @Despricetion:TODO 获取当前商品的下一区间价格
     * @Date: 2018/5/14 20:58
     * @Param: [listSize, urlMd5, num, normLeast, freePrice, freightEs1, priceList, isNextPrice]
     * @Return: double
     */
    public static double getNextIntervalPrice(String listSize, int num, String normLeast, String priceList, boolean isNextPrice) {
        Map<String, Double> spiderPrcieMoq = new HashMap<String, Double>();
        double price1 = 1;
        int bathMoq1 = 0;
        //购物车页面选择的价格
        double spiderPirce = 0;
        int batchMoq = num;
        //初始值等于产品单页的价格就行
        if (Integer.valueOf(StringUtils.isNotBlank(listSize) ? listSize : "0") > 0 && priceList != null && !"".equals(priceList)) {
            String[] wholesalePriceList = priceList.replaceAll("[\\[\\]]", "").split(",");
            if (!isNextPrice) {
                Double valueOf = Double.valueOf(wholesalePriceList[0].split("(\\$)")[1]);
                spiderPirce = valueOf;
            }
            for (String wholesalePriceTem : wholesalePriceList) {
                String[] wholesalePriceTems = wholesalePriceTem.split("(\\$)");
                if (isMatch(wholesalePriceTems[0].trim(), "(\\d+-\\d+)|(≥\\d+)|(\\d+)")) {
                    if ("≥0".equals(wholesalePriceTems[0].trim())) {
                        bathMoq1 = 1;
                    } else {
                        bathMoq1 = Integer.parseInt(wholesalePriceTems[0].trim());
                    }
                    if (num >= bathMoq1 && !isNextPrice) {
                        //获取到价格,进而判断价格是否是区间
                        if (isRangePrice(wholesalePriceTems[1].trim())) {
                            if (wholesalePriceTems[1].trim().indexOf("-") > -1) {
                                price1 = Double.valueOf(wholesalePriceTems[1].trim().split("-")[0]);
                            } else {
                                price1 = Double.valueOf(wholesalePriceTems[1].trim());
                            }
                        }
                        batchMoq = bathMoq1;
                        spiderPirce = price1;
                    } else if (num < bathMoq1 && isNextPrice) {
                        //获取到价格,进而判断价格是否是区间
                        if (isRangePrice(wholesalePriceTems[1].trim())) {
                            if (wholesalePriceTems[1].trim().indexOf("-") > -1) {
                                price1 = Double.valueOf(wholesalePriceTems[1].trim().split("-")[0]);
                            } else {
                                price1 = Double.valueOf(wholesalePriceTems[1].trim());
                            }
                        }
                        batchMoq = bathMoq1;
                        spiderPirce = price1;
                        break;
                    }
                }
            }
        }
        spiderPrcieMoq.put("spiderPirce", spiderPirce);
        spiderPrcieMoq.put("batchMoq", Double.valueOf(batchMoq));
        return spiderPirce;

    }
    /**
     * 字符串是否为产品区间价格
     *
     * @date 2016年4月28日
     * @author abc
     * @param str
     * @return
     */
    public static boolean isRangePrice(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str = str.replaceAll("(\\s*-\\s*)", "-");
        return Pattern.compile("(\\d+(\\.\\d+){0,1}(-\\d+(\\.\\d+){0,1}){0,1})")
                .matcher(str).matches();
    }
    /**
     * 输入的字符串是否匹配正则表达式
     *
     * @date 2016年4月28日
     * @author abc
     * @param str
     * @param reg
     * @return
     */
    public static boolean isMatch(String str, String reg) {
        if (str == null || reg == null || reg.isEmpty()) {
            return false;
        }
        return Pattern.compile(reg).matcher(str).matches();
    }
    
	//运费 =   克重 * 0.08元/克 ->0.08
	public static  double getFeightWs(double weight){
		
		double newFeight = 0.08 * weight * 1000;
			
		return new BigDecimal(newFeight).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static void main(String[] args) {
		double freightByWeight = getFreightByWeight(36, "0.23", "EPACKET (USPS)");
		System.out.println(freightByWeight);
		
	}


    public static CommonResult changeUserNotFree(int userId,int state) {
        CommonResult commonResult = new CommonResult();
        int result = 0;
        OkHttpClient okHttpClient = new OkHttpClient();
        /*Request request = new Request.Builder().url(getFreightCostUrl).post(formBody).build();*/
        String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId","goodsCar/"+userId+"/"+state);

        com.alibaba.fastjson.JSONObject jsonObject;
        try {
            String requestUrl = url;
            jsonObject = instance.doGet(requestUrl);
            commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
        } catch (IOException e) {
            log.error("CartController refresh ",e);
        }
        return commonResult;
    }
}
