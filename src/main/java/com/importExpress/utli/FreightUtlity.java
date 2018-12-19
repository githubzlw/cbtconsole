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
    private static final String getFreightCostUrl = GetConfigureInfo.getValueByCbt("getFreightCostUrl");

    public static double getFreightByOrderno(String orderNo) {
        double freight = 0;
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder().add("orderNo", String.valueOf(orderNo)).build();
        Request request = new Request.Builder().url(getFreightCostUrl).post(formBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String resultStr = response.body().string();
            JSONObject json = JSONObject.fromObject(resultStr);
            if (json.getBoolean("ok")) {
                System.out.println("getFreightByOrderno success !!!");
                Map<String, Double> data = (Map<String, Double>) json.getJSONObject("data");
                freight = (Double) data.get("freightCost");
            } else {
                System.err.println("getFreightByOrderno error :<:<:<");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.error("getFreightByOrderno error,orderNo:[{}],e:[{}]" + orderNo + e.getMessage());
        }
        return freight;
    }
}
