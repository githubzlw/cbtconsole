package com.cbt.website.servlet;


import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**   
 * @Title: dataPushServlet.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年8月2日
 * @version V1.0   
 */
public class dataPushServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(dataPushServlet.class);
	
	
	protected void dataPush(String sqlStr, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		DataPushDao dataPushDao = new DataPushDaoImpl();
//		dataPushDao.PushData(tableName, colsName, pak, colsValue, pakValue);
	}
}
