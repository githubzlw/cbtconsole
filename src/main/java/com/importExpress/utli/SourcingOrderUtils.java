package com.importExpress.utli;

import com.alibaba.fastjson.JSONObject;
import com.cbt.website.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.utli
 * @date:2021-07-29
 */
@Slf4j
public class SourcingOrderUtils {


    private static PropertiesUtils reader = new PropertiesUtils("cbt.properties");

    private static String sourcingOrderUpdate = reader.getProperty("sourcing.order.update");
    private static String sourcingStockUpdate = reader.getProperty("sourcing.stock.update");

    private OKHttpUtils okHttpUtils = new OKHttpUtils();

    /**
     * 更新订单的状态
     *
     * @param orderNo
     * @param state 订单状态：订单状态：0->待付款；1->采购；2->入库；3->已发货；4->已完结；5->已经付款； -1/6->取消订单
     * @return
     */
    public JsonResult updateSourcingOrder(String orderNo, Integer state) {


        Assert.isTrue(StringUtils.isNotBlank(orderNo), "orderNo null");
        Assert.isTrue(null != state && state > -1, "state null");
        try {

            Map<String, String> param = new HashMap<>();
            param.put("orderNo", orderNo);
            param.put("state", String.valueOf(state));

            JSONObject jsonObject = okHttpUtils.postURL(sourcingOrderUpdate, param);
            if (jsonObject.containsKey("code") && 200 == jsonObject.getIntValue("code")) {
                return JsonResult.success(jsonObject.toJSONString());
            } else {
                return JsonResult.error(jsonObject.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateSourcingOrder,orderNo[{}],state[{}],error:", orderNo, state, e);
            return JsonResult.error(e.getMessage());
        }
    }


    /**
     * 更新订单库存的状态
     *
     * @param orderNo
     * @param state
     * @return
     */
    public JsonResult updateOrderStock(String orderNo, Integer state) {
        Assert.isTrue(StringUtils.isNotBlank(orderNo), "orderNo null");
        Assert.isTrue(null != state && state > -1, "state null");
        try {

            Map<String, String> param = new HashMap<>();
            param.put("orderNo", orderNo);
            param.put("state", String.valueOf(state));

            JSONObject jsonObject = okHttpUtils.postURL(sourcingStockUpdate, param);
            if (jsonObject.containsKey("code") && 200 == jsonObject.getIntValue("code")) {
                return JsonResult.success(jsonObject.getJSONObject("data"));
            } else {
                return JsonResult.error(jsonObject.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateOrderStock,orderNo[{}],state[{}],error:", orderNo, state, e);
            return JsonResult.error(e.getMessage());
        }
    }
}
