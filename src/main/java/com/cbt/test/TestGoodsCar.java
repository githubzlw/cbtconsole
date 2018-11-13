package com.cbt.test;

import com.cbt.bean.GoodsCarBean;
import net.sf.json.JSONArray;

import java.util.List;

public class TestGoodsCar {

    public static void main(String[] args) {
        String carStr = "[{\"aliPosttime\":\"9-15\",\"comparePrices\":\"12.43\",\"delivery_time\":\"6\",\"free_sc_days\":\"9-15\",\"goodsUnit\":\"pair\",\"goods_catid\":\"1034340\",\"guId\":\"03170ea27006d77e34c360643eb297b4\",\"id\":0,\"img_type\":\"https://img.import-express.com/importcsvimg/coreimg/565330047563/8540949672_1516891798.60x60.jpg@\",\"img_url\":\"https://img.import-express.com/importcsvimg/coreimg/565330047563/8540949672_1516891798.60x60.jpg\",\"name\":\"Orange Red Spring And Summer Large Size Flat Sole Zip Women Beach Sandal\",\"price4\":10.48,\"remark\":\"\",\"seller\":\"dd\",\"sessionId\":\"\",\"shopId\":\"shop2133364623300\",\"types\":\"Color:yellow@152532413532232161,Size:36@15253241353224502,\",\"url\":\"https://www.import-express.com/goodsinfo/orange-red-spring-and-summer-large-size-flat-sole-zip-1565330047563.html\",\"userid\":22821,\"width\":\"\"},{\"aliPosttime\":\"9-15\",\"comparePrices\":\"12.43\",\"delivery_time\":\"6\",\"free_sc_days\":\"9-15\",\"goodsUnit\":\"pair\",\"goods_catid\":\"1034340\",\"guId\":\"3823d4ecd5f283583046b28b87fe5fa9\",\"id\":0,\"img_type\":\"https://img.import-express.com/importcsvimg/coreimg/565330047563/8540949672_1516891798.60x60.jpg@\",\"img_url\":\"https://img.import-express.com/importcsvimg/coreimg/565330047563/8540949672_1516891798.60x60.jpg\",\"name\":\"Orange Red Spring And Summer Large Size Flat Sole Zip Women Beach Sandal\",\"price4\":10.48,\"remark\":\"\",\"seller\":\"dd\",\"sessionId\":\"\",\"shopId\":\"shop2133364623300\",\"types\":\"Color:yellow@152532413532232161,Size:40@15253241353224506,\",\"url\":\"https://www.import-express.com/goodsinfo/orange-red-spring-and-summer-large-size-flat-sole-zip-1565330047563.html\",\"userid\":22821,\"width\":\"\"}]";

        List<GoodsCarBean> list_active = (List<GoodsCarBean>) JSONArray.toCollection(JSONArray.fromObject(carStr),GoodsCarBean.class);//字符串转换成List
        System.err.println("list_active size:" + list_active.size());
        System.err.println("list_active:" + list_active.toString());
    }
}
