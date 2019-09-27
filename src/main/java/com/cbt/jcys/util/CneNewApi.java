package com.cbt.jcys.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.Md5Util;
import com.cbt.website.util.UploadByOkHttp;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新的CNE的 API
 */
public class CneNewApi {
    private final static Logger logger = LoggerFactory.getLogger(CneNewApi.class);

    /**
     * icID 客户ID
     */
    private static final String ICID = "14972";
    /**
     * secret为客户账号和密钥
     */
    private static final String SECRET = "Yb70MrMh9Q4Odl5";

    private static final String CNE_URL_ONAME_LIST = "http://api.cne.com/cgi-bin/EmsData.dll?DoApi";
    private static final String CNE_URL_SHIP_NO = "https://api.cne.com/cgi-bin/EmsData.dll?DoApi";

    private static CneNewApi cneNewApi = new CneNewApi();


    private CneNewApi() {

    }

    public static CneNewApi getInstance() {
        if (cneNewApi == null) {
            synchronized (CneNewApi.class) {
                cneNewApi = new CneNewApi();
            }
        }
        return cneNewApi;
    }


    public List<String> getCneList(){
        List<String> list = new ArrayList<>();
        String result = null;
        JSONObject paramJson = new JSONObject();
        paramJson.put("RequestName", "EmsKindList");
        OkHttpClient client = UploadByOkHttp.initClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, paramJson.toJSONString());


        Request request = new Request.Builder().url(CNE_URL_ONAME_LIST).post(body).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result =  response.body().string();
            } else {
                System.err.println("result:" + response.body().string() + "Exception:" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("getCneList", e);
        }


        System.out.println(result);

        JSONObject json =JSONObject.parseObject(result);
        JSONArray jsonArray = json.getJSONArray("List");
        if(jsonArray != null && jsonArray.size() > 0){
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(jsonArray.getJSONObject(i).getString("oName"));
            }
        }
        return list;
    }

    /**
     * @param countryCode : 国家简码，两位
     * @param fWeight     : 件数，默认1,此为单个订单包裹数量，非SKU 数量
     * @param itemNum     : 详情商品总数
     * @param cReceiver   : 收件人
     * @param cRAddr      : 收件地址
     * @param cRCity      : 收件城市
     * @param cRPostcode  : 收件邮编
     * @param cRCountry   : 收件国家【必须为英文】
     * @param odbList     : 订单详情
     * @return
     */
    public List<Map<String, String>> getResultByParam(String countryCode, String fWeight, int itemNum,
                                                      String cReceiver, String cRAddr, String cRCity, String cRPostcode, String cRCountry, List<OrderDetailsBean> odbList) {

        List<Map<String, String>> mapList = new ArrayList<>();
        // 1.拿到准备数据
        JSONObject paramJson = readyJsonData(countryCode, fWeight, itemNum, cReceiver, cRAddr, cRCity, cRPostcode, cRCountry, odbList);
        // 使用okhttp调用API
        String resultApi = getResultByUrl(paramJson);
        if (StringUtils.isNotBlank(resultApi) && resultApi.length() > 10) {
            JSONObject resultJson = JSONObject.parseObject(resultApi);

            // 正数，处理记录数。负数，失败（-2：icID 错误；-3:没有RecList；-9:系统错误）
            int returnValue = resultJson.getIntValue("ReturnValue");
            if (returnValue == 1) {
                JSONArray resList = resultJson.getJSONArray("ErrList");
                if (resList == null || resList.size() == 0) {
                    logger.error("getResultByParam error--resList: " + resList);
                }
                JSONObject cneJson;
                for (int i = 0; i < resList.size(); i++) {
                    cneJson = resList.getJSONObject(0);
                    if (cneJson == null) {
                        logger.error("getResultByParam error--cneJson: " + cneJson);
                    } else {
                        if (StringUtils.isBlank(cneJson.getString("cMess")) && StringUtils.isNotBlank(cneJson.getString("cNo"))) {
                            Map<String, String> cneMap = new HashMap<>();
                            cneMap.put("iIndex", cneJson.getString("iIndex"));
                            cneMap.put("iID", cneJson.getString("iID"));
                            cneMap.put("cNo", cneJson.getString("cNo"));
                            cneMap.put("cNum", cneJson.getString("cNum"));
                            cneMap.put("cRNo", cneJson.getString("cRNo"));
                            mapList.add(cneMap);
                        } else {
                            logger.error("getResultByParam error--cMess: " + cneJson);
                        }
                    }
                }
            } else {
                logger.error("getResultByParam error--resultJson: " + resultJson);
            }
        }
        return mapList;
    }


    /**
     * 使用okhttp获取URL的数据
     *
     * @param paramJson
     * @return
     */
    private String getResultByUrl(JSONObject paramJson) {
        String result = null;
        OkHttpClient client = UploadByOkHttp.initClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, paramJson.toJSONString());


        Request request = new Request.Builder().url(CNE_URL_SHIP_NO).post(body).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                System.err.println("result:" + response.body().string() + "Exception:" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("getResultByUrl", e);
        }
        return result;
    }

    /**
     * 准备json数据
     *
     * @param countryCode : 国家简码，两位
     * @param fWeight     : 件数，默认1,此为单个订单包裹数量，非SKU 数量
     * @param itemNum     : 详情商品总数
     * @param cReceiver   : 收件人
     * @param cRAddr      : 收件地址
     * @param cRCity      : 收件城市
     * @param cRPostcode  : 收件邮编
     * @param cRCountry   : 收件国家【必须为英文】
     * @param odbList     : 订单详情
     * @return
     */
    private JSONObject readyJsonData(String countryCode, String fWeight, int itemNum,
                                     String cReceiver, String cRAddr, String cRCity, String cRPostcode, String cRCountry, List<OrderDetailsBean> odbList) {
        // 参数格式 paramJson
        JSONObject paramJson = new JSONObject();
        paramJson.put("RequestName", "PreInputSet");
        Long currTime = System.currentTimeMillis();
        paramJson.put("TimeStamp", currTime);
        String md5 = Md5Util.md5Operation(ICID + currTime + SECRET);
        paramJson.put("MD5", md5);

        // 多个recDataObject 组合recList
        JSONArray recList = new JSONArray();


        // recDataObject 数据，存放运费和商品数据
        JSONObject recDataObject = new JSONObject();
        //快件类型，默认为1。取值为：0(文件),1(包裹),2(防水袋)
        recDataObject.put("nItemType", "1");
        // 快递类别
        recDataObject.put("cEmsKind", "CNE全球特惠");
        // ISO 二字代码
        recDataObject.put("cDes", countryCode);
        // 重量，公斤，3 位小数
        recDataObject.put("fWeight", BigDecimalUtil.truncateDouble(Double.valueOf(fWeight), 3));
        if (itemNum < 1) {
            recDataObject.put("itemNum", 1);
        } else {
            recDataObject.put("itemNum", itemNum);
        }

        recDataObject.put("cReceiver", cReceiver);
        recDataObject.put("cRAddr", cRAddr);
        recDataObject.put("cRCity", cRCity);
        recDataObject.put("cRPostcode", cRPostcode);
        recDataObject.put("cRCountry", cRCountry);


        JSONArray goodsList = new JSONArray();
        for (OrderDetailsBean odb : odbList) {
            JSONObject goodData = new JSONObject();
            if (StringUtils.isBlank(odb.getGoodsname())) {
                // 海关申报物品描述
                goodData.put("cxGoods", "import express goods");
                // 物品英文描述,0-63 字符
                goodData.put("cxGoodsA", "import express goods");
            } else {
                if (odb.getGoodsname().length() > 60) {
                    // 海关申报物品描述
                    goodData.put("cxGoods", odb.getGoodsname().substring(0, 60));
                    // 物品英文描述,0-63 字符
                    goodData.put("cxGoodsA", odb.getGoodsname().substring(0, 60));
                } else {
                    // 海关申报物品描述
                    goodData.put("cxGoods", odb.getGoodsname());
                    // 物品英文描述,0-63 字符
                    goodData.put("cxGoodsA", odb.getGoodsname());
                }
            }

            // 海关申报物品数量
            goodData.put("ixQuantity", odb.getYourorder());
            // 海关申报单价
            goodData.put("fxPrice", odb.getGoodsprice());
            goodsList.add(goodData);
        }
        // 商品信息
        recDataObject.put("GoodsList", goodsList);
        // 加入记录列表
        recList.add(recDataObject);

        //全部参数整合
        paramJson.put("RecList", recList);
        return paramJson;
    }

    public static void main(String[] args) {
        System.err.println(CneNewApi.getInstance().getCneList());
    }

}
