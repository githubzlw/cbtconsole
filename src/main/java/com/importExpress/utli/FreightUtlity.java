package com.importExpress.utli;

import com.cbt.util.GetConfigureInfo;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

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
public class FreightUtlity {
    private static final Logger logger = LoggerFactory.getLogger(FreightUtlity.class);
    private static final String getFreightCostUrl = GetConfigureInfo.getValueByCbt("getMinFreightUrl");

    public static double getFreightByOrderno(String orderNo) {
        double freight = 0;
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder().add("orderNo", String.valueOf(orderNo)).build();
        /*Request request = new Request.Builder().url(getFreightCostUrl).post(formBody).build();*/
        String url = getFreightCostUrl.replace("getMinFreightByUserId","getFreightByOrderNo");
        Request request = new Request.Builder().url(url).post(formBody).build();
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
}
