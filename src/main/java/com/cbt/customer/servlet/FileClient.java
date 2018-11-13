package com.cbt.customer.servlet;

import ceRong.tools.servlet.FileDataService;

import java.io.*;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileClient {

    public FileClient() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        try {
            FileDataService fileDataService = (FileDataService) Naming.lookup("rmi://192.168.1.120:6600/FileDataService");
            SimpleDateFormat dfw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println("time start="+dfw.format(new Date()));// new Date()为获取当前系统时间
/*            List<syncTableInfo> synList = fileDataService.upload("D:/home/hadoop/pic/" + System.currentTimeMillis() + ".jpg", new FileClient().fileToByte("E:/cache/g01_a_alicdn_kf_HTB1.ahQJVXXXXXcXXXXq6xXFXXXO_2015-New-Women-Winter-Warm-Long-Down-Parkas-Female-Slim-Down-Cotton-Jacket-Hooded-Faux-Fur.jpg_220x220.jpg"));
            System.out.println("time end="+dfw.format(new Date()));// new Date()为获取当前系统时间
            for(int i=0;i<synList.size();i++){
            	float score =synList.get(i).getScore();
            } */
            /*while(true) {
            	int uploadStatus = fileDataService.getUploadStatus();
            	if (uploadStatus == 2) {
            		List<syncTableInfo> synList = fileDataService.uploadCallBack();
            		for(int i=0;i<synList.size();i++){
                    	float score =synList.get(i).getScore();
                    }
            		break;
            	}
            }*/
                        
            //            for (int z = 0; z < hits.length(); z++) {
//            	//分数
//            	float fraction=hits.score(z);
//            	int Similarity = (int)(100-fraction+0.5);
//                String fileIdentifier = hits.doc(z).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
//                int pos = fileIdentifier.lastIndexOf("\\");
//                String strFilePath = fileIdentifier.substring(10, fileIdentifier.length());
//                fileIdentifier = fileIdentifier.substring(pos+1,fileIdentifier.length()-4);
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //这个方法比较重要，通过这个方法把一个名为filename的文件转化为一个byte数组
    public List<byte[]> fileToByte(String filename){
        List<byte[]> list = new ArrayList<byte[]>();
        try {
            File file = new File(filename);
            byte [] buffer = new byte [(int)file.length()];
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            while (is.read(buffer) > 0 ) {
            	list.add(buffer);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
