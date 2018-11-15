package com.cbt.method.servlet;

import com.cbt.controller.OrderInfoNewController;
import com.cbt.method.dao.OrderDetailsMethodBean;
import com.cbt.method.service.OrderDetailsService;
import com.cbt.method.service.OrderDetailsServiceImpl;
import com.cbt.util.GetConfigureInfo;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 根据采购ID获取F盘文本中存储的需要采购的商品信息
 * @author 王宏杰
 * 最后修改时间:2016-10-08
 *
 */
public class GetListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderInfoNewController.class);
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
    
	/**
	 * 从存储的配置文件中获取某个采购需要购买的商品封装成list返回到浏览器
	 * @author 王宏杰
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OrderDetailsService ods=new OrderDetailsServiceImpl();
		try {
			String admname=request.getParameter("nickname");
			Properties props = new Properties();
			String ipurl=admname;
			String path= GetConfigureInfo.getAdgoodsPath();
			 InputStream in = new BufferedInputStream (new FileInputStream(path));
			 props.load(in);
			 StringBuffer goodsid=new StringBuffer();
	         String json = props.getProperty ("result");
	         JSONArray listJson=JSONArray.fromObject(json);
	         for(int j=0;j<listJson.size();j++){
	        	 Map map=(Map)listJson.get(j);
	        	 if(map.get(String.valueOf(ipurl))!=null){
	        		 List odsList=(List)map.get(String.valueOf(ipurl));
	        		 int i=0;
	        		 for (Object object : odsList) {
	        		 	if(odsList.size()>=15){
	        		 		if(i>(odsList.size()*0.6)){
								goodsid.append(object+",");
							}
						}else{
							goodsid.append(object+",");
						}
	        		 	i++;
					}
	        	 }
	        	 map.clear();
	         }
	         List<OrderDetailsMethodBean> list=new ArrayList<OrderDetailsMethodBean>();
	         if(goodsid.indexOf(",")>-1){
	        	 list=ods.getOrderDetailsByOrderNo(00,goodsid.substring(0,goodsid.length()-1).toString());
	         }
			PrintWriter out;
			out = response.getWriter();
			out.print(JSONArray.fromObject(list).toString());
			out.flush();
			list.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
