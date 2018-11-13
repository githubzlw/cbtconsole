package com.cbt.website.quartz;

import com.cbt.method.servlet.OrderDetailsServlet;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DelPropertiesJob implements Job {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CheckUnpaidOrderJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		synchronized (this) {
			try {
				System.out.println("开始清空存放商品编号文件.........................");
				Properties properties = new Properties();
				//String filePath="E:/myproject/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/cbtconsole/WEB-INF/classes/addGoodsNo.properties";//获取项目路径
				String filePath="F:/cbtconsole/adgoods/addGoodsNo.properties";//获取项目路径
	            InputStream inputStream;
				inputStream = new FileInputStream(filePath);
				properties.load(inputStream);
	            inputStream.close(); //关闭流
	            String result = properties.getProperty("result");
	            if(result!=null && !result.equals("")){
	            	result="";
	            	OrderDetailsServlet o=new OrderDetailsServlet();
	            	o.writeData("result", "[]", filePath);
	            }
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("结束清空存放商品编号文件.........................");
	}
}
