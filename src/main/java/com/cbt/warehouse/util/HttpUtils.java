package com.cbt.warehouse.util;


import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class HttpUtils {
    public static String post(){
        try {
            File f = new File("D:/3543534.mp4");
            PostMethod filePost = new PostMethod("http://140.82.48.255:3000/upload");
            Part[] parts = {new FilePart("video", f),new StringPart("token","cerong2018jack")};
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            org.apache.commons.httpclient.HttpClient clients = new org.apache.commons.httpclient.HttpClient();
            int status = clients.executeMethod(filePost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(filePost.getResponseBodyAsStream(), "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println("==line："+line);
                stringBuffer.append(line);
            }
            rd.close();
            System.out.println("接受到的流是：" + stringBuffer + "—-" + status);
            if(stringBuffer.toString().contains("success")){
                //成功
                String [] msg=stringBuffer.toString().replace("}","").replace("{","")
                        .replace("\"","").replace("\\","").split("/");
                System.out.println(msg);
                String result=msg[msg.length-1];
                System.out.println("result:"+result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
}

    public static void main(String[] args) {
        post();
    }
}