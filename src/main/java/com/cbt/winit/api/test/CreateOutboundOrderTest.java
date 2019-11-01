package com.cbt.winit.api.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.cbt.winit.api.model.RequestMsg;

public class CreateOutboundOrderTest extends BaseTest {

    /**
     * 创建出库单（确认中）
     */
    @Test
    public void createOutboundOrder() {

        doTest();
    }

    @Override
    public void setdBusinessData(RequestMsg requestMsg) {
        
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("warehouseID", "1000001");
        data.put("eBayOrderID", "3298472983749823480");
        data.put("repeatable", "N");
        data.put("deliveryWayID", "1000020");
        data.put("insuranceTypeID", "1000000");
        data.put("sellerOrderNo", "PowerSellerABC123");
        data.put("recipientName", "mingbao");
        data.put("phoneNum", "15989431906");
        data.put("zipCode", "3153");
        data.put("emailAddress", "443668888@qq.com");
        data.put("state", "AU");
        data.put("region", "Victoria");
        data.put("city", "Bayswater North");
        data.put("address1", "1 Stafford Crt.");
        data.put("address2", "abc");

        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        Map<String, Object> product = new TreeMap<String, Object>();
        product.put("productCode", "W12424539");
        product.put("specification", "");
        product.put("eBayTransactionID", "2433ctionI234");
        product.put("eBayItemID", "34BayItemI34245");
        product.put("eBaySellerID", "PowerSellerABC123");
        product.put("eBayBuyerID", "PowerBuyerDEF456");
        product.put("productNum", "1");

        productList.add(product);

        data.put("productList", productList);

        requestMsg.setData(data);

    }

    @Override
    public void setRequestAction(RequestMsg requestMsg) {
        requestMsg.setAction("createOutboundOrder");

    }

    @Override
    protected void parseRequestResult(String result) {
        // TODO Auto-generated method stub
        
    }

}
