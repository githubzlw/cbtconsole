package com.cbt.website.servlet;

import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.service.DownloadMain;
import com.cbt.website.bean.GoodsSource;
import com.cbt.website.dao.GoodsSourceDaoImp;
import com.cbt.website.dao.IGoodsSourceDao;
import com.cbt.website.service.WebsiteParse;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * sj
 */
public class GoodsWebsiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(GoodsWebsiteServlet.class);
       
    public GoodsWebsiteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONArray jsonArray = JSONArray.fromObject("[]");
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		try {
			
			String goodId = req.getParameter("goodId");
			String aliUrl = req.getParameter("aliUrl");
			String orderNo = req.getParameter("orderNo");
			
			
			String keyword = req.getParameter("keyword");
//			System.out.println("keyword1:"+keyword);
//			keyword = URLDecoder.decode(URLDecoder.decode(keyword, "UTF-8"), "UTF-8");
//			System.out.println("keyword2:"+keyword);
			keyword = new String(keyword.getBytes("ISO8859-1"),"UTF-8");
//			System.out.println("keyword3:"+keyword);
			String price1 = req.getParameter("price1");
			String price2 = req.getParameter("price2");
			String pages = req.getParameter("page");
			String catid = req.getParameter("catid");
/***************************参数设置 start**************************************/	
			//类别id
			if(catid==null||"undefined".equals(catid)||catid.isEmpty()||!Pattern.compile("(\\d+)").matcher(catid).matches()){
				catid = "0";
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
				sort = "order-desc";
			}
			//来源网站
			String website = req.getParameter("website");
			if(website==null||"undefined".equals(website)||website.isEmpty()){
				website = "o";
			}
/**********************************参数设置 end***************************************/
		  String url = "http://s.1688.com/selloffer/offer_search.htm?keywords="+URLEncoder.encode(keyword,"GBK")+"&n=y";
//		  String url = "http://s.1688.com/selloffer/offer_search.htm?keywords="+keyword+"&n=y";
		  if("bbPrice-asc".equals(sort)){
			   url = url+"&descendOrder=false&sortType=price";
		   }else if("bbPrice-desc".equals(sort)){
			   url = url+"&descendOrder=true&sortType=price";
		   }else if("order-desc".equals(sort)){
			   url = url+"&descendOrder=true&sortType=quantity_sum_month";
		   }
		  if(!price1.isEmpty()){
			  url = url+"&priceStart="+price1;
		  }
		  if(!price2.isEmpty()){
			  url = url+"&priceEnd="+price2;
		  }
		  if(!maxq.isEmpty()){
			  url = url+"&quantityBegin="+maxq;
		  }
		    ArrayList<SearchGoods> goods = null;
			WebsiteParse  wp = new WebsiteParse();
			System.out.println("url:"+url);
			goods = wp.ParseSearch(url,keyword, pages);
			if(goods!=null&&!goods.isEmpty()){
				jsonArray = JSONArray.fromObject(goods);
			}
			goods = null;
			
			req.setAttribute("aliUrl", aliUrl);
			req.setAttribute("goodId", goodId);
			req.setAttribute("orderNo", orderNo);
			
		} catch (Exception e1) {
			jsonArray = JSONArray.fromObject("[]");
			LOG.error("e1",e1);
			LOG.warn("error-----what error");
		}finally{
			req.setAttribute("responsetext", jsonArray);
			req.setAttribute("goodsnum", "4");
			
			RequestDispatcher homeDispatcher = req.getRequestDispatcher("website/sroduct.jsp");
			homeDispatcher.forward(req, resp);
		}
		jsonArray = null;
	}
	
	protected void addSource(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		IGoodsSourceDao dao  =new GoodsSourceDaoImp();
		String url = req.getParameter("url");
		String id = req.getParameter("goodsid");
		String purl = req.getParameter("purl");
		String pName = req.getParameter("name");
		String pPrice = req.getParameter("price");
		String pImg = req.getParameter("img");
		//String orderNo = req.getParameter("orderNo");
		String checkFlag = req.getParameter("checkFlag");
		if("false".equals(checkFlag)){
			dao.deleteGoodsSource(url,purl);
		}else{
			if(url!=null&&!url.isEmpty()&&purl!=null&&!purl.isEmpty()){
				
				int queryExsis = dao.queryExsis(purl,url);
				if(queryExsis==0){
					GoodsSource bean = new GoodsSource();
					bean.setGoodsId(Integer.valueOf(id));
					bean.setGoodsImg(pImg);
					bean.setGoodsName(pName);
					bean.setGoodsPrice(pPrice);
					bean.setGoodsPurl(purl);
					bean.setGoodsUrl(url);
					bean.setOrderDesc(50);
					bean.setSourceType("1688");
					dao.add(bean);
				}
			}
		}

		
//		try {
//			req.getRequestDispatcher("customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo="+orderNo).forward(req, resp);
//		} catch (ServletException e) {
//			e.printStackTrace();	
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}

