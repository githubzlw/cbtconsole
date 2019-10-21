package com.importExpress.utli;

import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.winit.api.constants.RequestParam;
import com.winit.api.model.RequestMsg;
import com.winit.api.utils.ApiClient;

public class WinitAPIUtils {
	private static String QUERY_WAREHOUSE = "queryWarehouse";
	private static String QUERY_PRODUCT_INVENTORY= "queryProductInventoryList4Page";
	
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
	
	/**
	 * 开始同步库存
	 */
	public static void syncStock() {
		//生成签名
		createSign();
		//请求仓库，返回仓库ID 和仓库Code
		getStockCode();
		
		//查询库存明细
		
		
	}
	
	/**
	 * 生成签名
	 * @return
	 */
	private static String createSign() {
		
		return "";
	}
	
	/**请求仓库，返回仓库ID 和仓库Code
	 * 
	 * @return
	 */
	private static Map<String,String> getStockCode(){
		Map<String,String> stockCodeMap = Maps.newHashMap();
		/*"action":"queryWarehouse",
	    "app_key":"rebecca",
	    "client_id":"ODJKMDU1YZCTYJQ5YY00ZWZLLTK5N2QTOWY4MZI5OGMWNDG2",
	    "client_sign":"20B0F915570E829C09E1EC74A074F625",
	    "data":"",
	    "format":"json",
	    "language":"zh_CN",
	    "platform":"OWNERERP",
	    "sign":"183CE29588FD471A7534BE1CF94913E9",
	    "sign_method":"md5",
	    "timestamp":"2015-06-16 00:19:26",
	    "version":"1.0" */
		
		Map<String,Object> requestParam = Maps.newHashMap();
		requestParam.put("action", "queryWarehouse");
		requestParam.put("app_key", "rebecca");
		requestParam.put("client_id", "ODJKMDU1YZCTYJQ5YY00ZWZLLTK5N2QTOWY4MZI5OGMWNDG2");
		requestParam.put("client_sign", "20B0F915570E829C09E1EC74A074F625");
		requestParam.put("format", "json");
		requestParam.put("language", "zh_CN");
		requestParam.put("platform", "OWNERERP");
		requestParam.put("sign", "183CE29588FD471A7534BE1CF94913E9");
		requestParam.put("sign_method", "md5");
		requestParam.put("timestamp", "2015-06-16 00:19:26");
		requestParam.put("version", "1.0");
		requestParam.put("data", "");
		
		
		return stockCodeMap;
		
	}
	
	/**
	 * queryProductInventoryList4Page
	 */
	private static void queryInvntoryDetail() {
		/*{
		    "action": "queryProductInventoryList4Page",
		    "app_key": "rebecca",
		    "client_id":"ODJKMDU1YZCTYJQ5YY00ZWZLLTK5N2QTOWY4MZI5OGMWNDG2",
		    "client_sign":"CC3F32A4D985B8176E22525F6ABD7FA1",
		    "data": {
		        "categoryID": "",
				"DOITier": "",
				"inventoryType": "Country",
		        "isActive":"Y",
		        "pageNum": "1",
		        "pageSize": "100",
		        "productCode": "",
		        "name": "",
				"specification": "",
				"warehouseId": "",
		        "warehouseCode": "DE0001"
		    },
		    "format": "json",
		    "language": "zh_CN",
		    "platform": "OWNERERP",
		    "sign": "8AB9BE71E5C0C9DBC5471327250B8A2C",
		    "sign_method": "md5",
		    "timestamp": "2015-06-16 00:19:26",
		    "version": "1.0"
		}*/
		
		
		
		
		
	}
	
	

}
