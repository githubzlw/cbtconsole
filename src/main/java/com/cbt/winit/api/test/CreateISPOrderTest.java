package com.cbt.winit.api.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.cbt.winit.api.model.RequestMsg;

public class CreateISPOrderTest extends BaseTest {

    /**
     * 创建ISP(小包)订单
     */
    @Test
    public void createISPOrder() {

        doTest();
    }

    @Override
    protected void setdBusinessData(RequestMsg requestMsg) {

        // 使用TreeMap保持字段是排序好的
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("buyerAddress1", "160 the esplanade apt 708");
        data.put("buyerCity", "toronto");
        data.put("buyerContactNo", "13800001111");
        data.put("buyerCountry", "CA");
        data.put("buyerEmail", "abc@winit.com.cn");
        data.put("buyerHouseNo", "12-8");
        data.put("buyerName", "mingbao");
        data.put("buyerState", "ON");
        data.put("buyerZipCode", "m5a 3t2");
        data.put("dispatchType", "P");
        data.put("ebaySellerId", "443668888");

        data.put("refNo", "201802281124681");
        data.put("shipperAddrCode", "dr001");
        data.put("warehouseCode", "YW10000023");
        data.put("winitProductCode", "WP-HKP101");

        // 包裹节点：数组
        List<Map<String, Object>> packageList = new ArrayList<Map<String, Object>>();
        Map<String, Object> packageMap = new TreeMap<String, Object>();

        packageMap.put("height", "10");
        packageMap.put("length", "10");
        packageMap.put("weight", "10");
        packageMap.put("width", "10");

        // 包裹中的商品节点：数组
        List<Map<String, Object>> merchandiseList = new ArrayList<Map<String, Object>>();
        Map<String, Object> merchandise = new TreeMap<String, Object>();
        merchandise.put("declaredNameCn", "墨镜");
        merchandise.put("declaredNameEn", "sunglasses");
        merchandise.put("declaredValue", "0.5");
        merchandise.put("itemID", "34BayItemI34245");
        merchandise.put("transactionID", "PowerSellerABC123");
        merchandise.put("merchandiseQuantity", "1");
        merchandise.put("merchandiseCode", "US-MOSS");

        merchandiseList.add(merchandise);

        packageMap.put("merchandiseList", merchandiseList);

        packageList.add(packageMap);

        data.put("packageList", packageList);

        requestMsg.setData(data);

    }

    @Override
    protected void setRequestAction(RequestMsg requestMsg) {

        requestMsg.setAction("isp.order.createOrder");

    }

    @Override
    protected void parseRequestResult(String result) {
        // TODO Auto-generated method stub
        
    }

}
