package com.importExpress.controller;

import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.OKHttpUtils;
import com.importExpress.utli.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.controller
 * @date:2021-07-29
 */
@Slf4j
@Controller
@RequestMapping("/sourcingOrder")
public class SourcingOrderController {


    private OKHttpUtils okHttpUtils = new OKHttpUtils();

    private static PropertiesUtils reader = new PropertiesUtils("cbt.properties");
    private static String sourcingStockList = reader.getProperty("sourcing.stock.list");
    private static String sourcingUpdateStockMatch = reader.getProperty("sourcing.stock.updateStockMatch");

    @Autowired
    private IOrderinfoService iOrderinfoService;

    @GetMapping("/getOrderStockList")
    @ResponseBody
    public JsonResult getOrderStockList(String orderNo) {
        try {

            String s = okHttpUtils.get(sourcingStockList + "/" + orderNo);
            JSONObject jsonObject = JSONObject.parseObject(s);
            if (null != jsonObject && jsonObject.containsKey("code") && 200 == jsonObject.getIntValue("code")) {

                return JsonResult.success(jsonObject.getJSONArray("data"));
            } else {
                return JsonResult.error(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getOrderStock,orderNo[{}],error:", orderNo, e);
            return JsonResult.error(e.getMessage());
        }
    }

    @GetMapping("/getLocalOrder")
    @ResponseBody
    public JsonResult getLocalOrder(String orderNo) {
        try {
            List<OrderDetailsBean> odb = iOrderinfoService.getOrdersDetails(orderNo);
            return JsonResult.success(odb);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getLocalOrder,orderNo[{}],error:", orderNo, e);
            return JsonResult.error(e.getMessage());
        }
    }


    @PostMapping("/updateStockMatch")
    @ResponseBody
    public JsonResult updateStockMatch(String orderNo, String matchInfo) {
        try {
            Map<String, String> param = new HashMap<>();
            param.put("orderNo", orderNo);
            param.put("matchInfo", matchInfo);
            JSONObject jsonObject = okHttpUtils.postURL(sourcingUpdateStockMatch, param);
            if (null != jsonObject && jsonObject.containsKey("code") && 200 == jsonObject.getIntValue("code")) {

                return JsonResult.success(jsonObject);
            } else {
                return JsonResult.error(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateStockMatch,orderNo[{}],matchInfo[{}],error:", orderNo, matchInfo, e);
            return JsonResult.error(e.getMessage());
        }
    }


}
