package com.cbt.parse.servlet;

import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.driver.DriverInterface;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.TypeUtils;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * sj
 */
public class GoodsTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(GoodsTest.class);
       
    public GoodsTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long stt = new Date().getTime();
		JSONArray jsonArray = JSONArray.fromObject("[]");
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		try {
			String keyword = req.getParameter("keyword");
			LOG.warn("Search1: keyword="+keyword);
			String keyurl = req.getParameter("keyurl");
			String k0 = req.getParameter("k0");
			String k1 = req.getParameter("k1");
			String k2 = req.getParameter("k2");
			String k3 = req.getParameter("k3");
			String price1 = req.getParameter("price1");
			String price2 = req.getParameter("price2");
			String pages = req.getParameter("page");
			String cid = req.getParameter("cid");
			String pid = req.getParameter("pid");
			String simiar = req.getParameter("simiar");
			String index = req.getParameter("ind");
			String catid = req.getParameter("catid");
			
/***************************参数设置 start**************************************/	
			//类别id
			if(catid==null||"undefined".equals(catid)||catid.isEmpty()||!Pattern.compile("(\\d+)").matcher(catid).matches()){
				catid = "0";
			}
			//首页二级页
			if(index!=null&&!"1".equals(index)){
				index = null;
			}
			
			//关键词
			if(keyword==null||"undefined".equals(keyword)){
				keyword = "";
			}else{
				keyword = keyword.replaceAll("\\s+", " ").trim();
				if(keyword.length()>1&&keyword.substring(0,1).equals("<")){
					keyword = keyword.substring(1);
				}
			}
			//“类似商品”参数
			if(simiar==null||"undefined".equals(simiar)){
				simiar = "";
			}else{
				simiar = simiar.trim();
			}
			//类型id
			if(pid==null||"undefined".equals(pid)||pid.isEmpty()){
				pid = "0";
			}
			//类别id
			if(cid==null||"undefined".equals(cid)||cid.isEmpty()){
				cid = "0";
			}
			//页数
			if(pages==null||"undefined".equals(pages)||pages.isEmpty()){
				pages = "1";
			}else{
				pages = DownloadMain.getSpiderContext(pages, "(\\d+)");
				pages = pages.isEmpty()?"1":pages;
			}
			//最小价格
			if(price1==null||"undefined".equals(price1)){
				price1="";
			}else{
				price1 = DownloadMain.getSpiderContext(price1, "(\\d+\\.*\\d*)");
			}
			//最大价格
			if(price2==null||"undefined".equals(price2)){
				price2="";
			}else{
				price2 = DownloadMain.getSpiderContext(price2, "(\\d+\\.*\\d*)");
			}
			//最小订量
			String minq = req.getParameter("minq");
			if(minq==null||"undefined".equals(minq)){
				minq  = "";
			}else{
				minq = minq.replaceAll("\\D+", "").trim();
			}
			//最大大量
			String maxq = req.getParameter("maxq");
			if(maxq==null||"undefined".equals(maxq)){
				maxq  ="";
			}else{
				maxq = maxq.replaceAll("\\D+", "").trim();
			}
			//搜索排序
			String sort = req.getParameter("srt");
			if(sort==null||"undefind".equals(sort)||sort.isEmpty()){
				sort = "default";
			}
			//是否为商店商品搜索
			String store = req.getParameter("store");
			if(store==null||"undefind".equals(store)||store.isEmpty()){
				store = "0";
			}
			//搜索链接
			String encodeUrl = null;
			if(k0!=null&&k1!=null&&k2!=null&&k3!=null
			   &&!k0.isEmpty()&&!k1.isEmpty()&&!k2.isEmpty()&&!k3.isEmpty()
			   &&!"undefined".equals(k0)&&!"undefined".equals(k1)
			   &&!"undefined".equals(k2)&&!"undefined".equals(k3)){
					StringBuilder sbtem = new StringBuilder();
					encodeUrl = sbtem.append(k2).append(k0)
									    .append(k3).append(k1).toString();
					encodeUrl = TypeUtils.decodeUrl(encodeUrl);
					sbtem.delete(0, sbtem.length());
					encodeUrl = sbtem.append("http://").append(encodeUrl).toString();
					sbtem = null;
			 }
			if(keyurl==null||keyurl.isEmpty()||"undefined".equals(keyurl)){
				keyurl = encodeUrl;
			}
			//来源网站
			String website = req.getParameter("website");
			if(website==null||"undefined".equals(website)||website.isEmpty()){
				website = "a";
			}
			//商店id
			String sid = req.getParameter("sid");
			if(sid!=null&&("undefined".equals(sid)||sid.isEmpty())){
				sid = null;
			}
			//商店来源网站
			String cm = req.getParameter("cm");
			if(cm!=null&&("undefined".equals(cm)||cm.isEmpty())){
				cm = null;
			}
			//类别
			String cat = req.getParameter("cat");
			if(cat==null||cat.isEmpty()||"undefined".equals(cat)){
				cat = null;
			}
			
/**********************************参数设置 end***************************************/
		
			DriverInterface driver = new DriverInterface();
			ArrayList<SearchGoods> goods = null;
			if("a".equals(website)){
				 goods = driver.search(6, keyword, price1, price2, minq, maxq, sort, catid, pages, pid);
			}else if("y".equals(website)){
				goods = driver.search(2, keyword, price1, price2, minq, maxq, sort, catid, pages, pid);
			}else if("o".equals(website)){
				goods = driver.search(7, keyword, price1, price2, minq, maxq, sort, catid, pages, pid);
			}
			if(goods!=null&&!goods.isEmpty()){
				jsonArray = JSONArray.fromObject(goods);
			}
			goods = null;
		} catch (Exception e1) {
			jsonArray = JSONArray.fromObject("[]");
			LOG.error("e1",e1);
			LOG.warn("error-----what error");
		}finally{
			req.setAttribute("responsetext", jsonArray);
			req.setAttribute("goodsnum", "4");
			RequestDispatcher homeDispatcher = req.getRequestDispatcher("cbt/testproduct.jsp");
			homeDispatcher.forward(req, resp);
		}
		jsonArray = null;
		LOG.warn("GoodsTypeServerlet time "+(new Date().getTime()-stt));
	}
}

