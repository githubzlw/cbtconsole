package com.cbt.website.thread;

import com.cbt.website.dao.AliExpress240Dao;
import com.cbt.website.dao.IAliExpress240Dao;
import com.cbt.website.servlet.Aliexpress_top120;
import com.cbt.website.util.FileTool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DownloadImgThred  implements Runnable {

	private static final Log LOG = LogFactory.getLog(Aliexpress_top120.class);
   public static final List<String[]> listimg = new ArrayList<String[]>();
   public static boolean Flag = true;
   public IAliExpress240Dao dao = new AliExpress240Dao();
   @Override
   public void run() {
       String[] tmp = null;
       try {
    	   while (Flag) {
//             synchronized(listimg) {
                 if (listimg.size() == 0) {
                     break ;
                 }
                 String img = listimg.get(0)[1];
                 String url = listimg.get(0)[0];
                 String filename = img.substring(img.lastIndexOf("/"),img.length());
                 boolean flag= FileTool.upLoadFromProduction(filename, img);
                 if(flag){
                     tmp = listimg.remove(0);
                     //修改图片下载地址
                     dao.upSearch240_img(url, "http://www.china-clothing-wholesale.com/hotproduct/" + filename);
                 }
//             }
    	   }
	} catch (Exception e) {
		LOG.debug(e.getMessage());
	}
       }
   
}
