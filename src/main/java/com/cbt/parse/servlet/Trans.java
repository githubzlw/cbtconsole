package com.cbt.parse.servlet;

import com.cbt.parse.bean.GoodsDaoBean;
import com.cbt.parse.dao.GoodsDao;
import com.cbt.parse.daoimp.IGoodsDao;
import com.cbt.parse.service.TypeUtils;
import com.cbt.util.AppConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Servlet implementation class Trans
 */
public class Trans extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Trans() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getParameter("url");
		request.setAttribute("url", url);
		if(url!=null&&!"undefind".equals(url)){
			url = url.replaceAll("\\s+", "").trim();
			if(url.indexOf("processesServlet")>0){
				url = url.split("SpiderServlet").length>0?url.split("SpiderServlet")[1]:"";
				if(url.indexOf("weight=")>0){
					url = url.replaceAll("&weight.*", "").trim();
				}
				if(url.indexOf("freeshipping=")>0){
					url = url.replaceAll("&freeshipping.*", "").trim();
				}
				if(url.indexOf("s=y")>0){
					url = url.replaceAll("&s=y", "").trim();
				}
				if(url.indexOf("u1=")>0){
					url = TypeUtils.decodeGoods(url);
					url = TypeUtils.modefindUrl(url, 1);
				}
				else if(url.indexOf("url=")>0){
					url = URLDecoder.decode(url, "utf-8");
					url = TypeUtils.modefindUrl(url, 1);
					url = url.replaceAll("&url=", "").trim();
				}
			}
			
			if(url!=null&&!url.isEmpty()){
				url = url.replaceAll("\\s+", "").trim();
				url = TypeUtils.modefindUrl(url, 1);
				String result = null;
				StringBuilder sb = new StringBuilder();
				
				/*if(Pattern.compile("(eelly)").matcher(url).find()){
					GoodsBean goods = DriverInterface.goods(url, null, 2);
					if(goods!=null&&!goods.isEmpty()){
						String productId = goods.getpID();
						if(productId!=null&&!productId.isEmpty()){
							result = sb.append(AppConfig.ips).append("/GoodsSearch?site=e&pid=")
									.append(productId).toString();
							sb.delete(0, sb.length());
						}
					}
				}
				/*else if(Pattern.compile("(alibaba)").matcher(url).find()){
				if(Pattern.compile("(yiwugou)").matcher(url).find()){
					IYiWuDao  gd = new YiWuDao();
					ArrayList<YiWuBean>  data= gd.querry(url, null);
					if(data!=null&&!data.isEmpty()){
						String productId = data.get(0).getProductId();
						if(productId!=null&&!productId.isEmpty()){
							result = sb.append(pref).append("GoodsSearch?site=y&pid=")
									.append(productId).toString();
							sb.delete(0, sb.length());
						}
						
					}
				}else if(Pattern.compile("(alibaba)").matcher(url).find()){
					IWholesaleGoodsDao  gd = new WholesaleGoodsDao();
					ArrayList<WholesaleGoodsBean> data = gd.queryData(url, null);
					if(data!=null&&!data.isEmpty()){
						String productId = data.get(0).getPid();
						if(productId!=null&&!productId.isEmpty()){
							result = sb.append(pref).append("GoodsSearch?site=w&pid=")
									.append(productId).toString();
							sb.delete(0, sb.length());
							
						}
					}
				}*/
				if(result==null||result.isEmpty()){
					IGoodsDao  gd = new GoodsDao();
					GoodsDaoBean data = gd.queryData("goodsdata_write",url);
					if(data!=null){
						String productId = null;
						productId = data.getpID();
						if(productId!=null&&!productId.isEmpty()){
							sb.append(AppConfig.path).append("/GoodsSearch?site=g&pid=")
							.append(productId).append("&cid=").append(data.getcID());
						}
						result = sb.toString();
						sb.delete(0, sb.length());
					}
				}
				if(result!=null&&!result.isEmpty()){
					request.setAttribute("data", result);
				}
			}
		}
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/trans.jsp");
		homeDispatcher.forward(request, response);
	}

}
