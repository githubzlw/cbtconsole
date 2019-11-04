package com.cbt.winit.api.test;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.cbt.winit.api.model.RequestMsg;

public class QueryDeliveryWayTest extends BaseTest {

    /**
     * 根据仓库ID查询该仓库支持的派送渠道(派送方式)
     */
    @Test
    public void queryDeliveryWay() {
        doTest();
    }

    @Override
    public void setdBusinessData(RequestMsg requestMsg) {
        JSONObject data = new JSONObject();
        data.put("warehouseID", 1000089);
        requestMsg.setData(data);

    }

    @Override
    public void setRequestAction(RequestMsg requestMsg) {
        requestMsg.setAction("queryDeliveryWay");

    }

    @Override
    protected void parseRequestResult(String result) {
        // TODO Auto-generated method stub
        
    }

}
