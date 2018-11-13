package com.cbt.warehouse.ctrl;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AppAddTest {
    public static final String ADD_URL = "http://exorderwebapi.flytcloud.com/api/OrderSyn/ErpUploadOrder";
    public static void appadd() throws  Exception{
        JSONObject obj = new JSONObject();
        String message = java.net.URLEncoder.encode("商品","utf-8");
        obj.element("Token", "39E74D0D-3ACF-6C38-A9DD-AE5A65DFE544");
        obj.element("UAccount", "33150");
        obj.element("Password", "31B68D81E2A8824C65D552EAAF116D2A");
        obj.element("UAccount", "33150");
        JSONObject orderList = new JSONObject();
        orderList.element("Address1", "0,104 Bridge Street,");
        orderList.element("Address2", "");
        orderList.element("ApiOrderId", "Q626131585360508");
        orderList.element("City", "KORUMBURRA");
        orderList.element("CiId", "AU");
        orderList.element("County", "AU");
        orderList.element("CCode", "USD");
        orderList.element("Email", "");
        orderList.element("PackType", "3");
        orderList.element("Phone", "0499198783");
        orderList.element("PtId", "THAMR");
        orderList.element("ReceiverName", "luschani99@gmail.com");
        orderList.element("SalesPlatformFlag", "0");
        orderList.element("SyncPlatformFlag", "flyt.logistics.import-express");
        orderList.element("Zip", "3950");
        obj.element("OrderList", orderList);
        JSONObject orderDetailList = new JSONObject();
        orderDetailList.element("ItemName", "goods");
        orderDetailList.element("Price", "20");
        orderDetailList.element("Quantities", "5");
        orderDetailList.element("Sku", "");
        obj.element("OrderDetailList", orderDetailList);
        JSONObject haikwanDetialList = new JSONObject();
        haikwanDetialList.element("HwCode", "");
        haikwanDetialList.element("ItemCnName", message);
        haikwanDetialList.element("ItemEnName", "made in china");
        haikwanDetialList.element("ItemId", "");
        haikwanDetialList.element("ProducingArea", "CN");
        haikwanDetialList.element("Quantities", "5");
        haikwanDetialList.element("UnitPrice", "20");
        haikwanDetialList.element("Weight", "");
        haikwanDetialList.element("BtId", "");
        haikwanDetialList.element("CCode", "USD");
        haikwanDetialList.element("Purpose", "CN");
        haikwanDetialList.element("Material", "5");
        obj.element("HaikwanDetialList", haikwanDetialList);
        try {
            URL url = new URL(ADD_URL);
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 设置维持长连接
            connection.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            connection.setRequestProperty("Charset", "UTF-8");

            connection.setRequestProperty("contentType", "application/json");
            // 转换为字节数组
            byte[] data = (obj.toString()).getBytes();
            // 设置文件长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:

            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.writeBytes("data="+obj.toString());
            System.out.println("data="+obj.toString());
            out.flush();
            out.close();
            //读取响应
            System.out.println(connection.getResponseCode());

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);
            reader.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try{
            appadd();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    }
