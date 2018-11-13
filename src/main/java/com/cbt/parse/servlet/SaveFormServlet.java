package com.cbt.parse.servlet;

import com.cbt.parse.service.CategoryUtils;
import com.cbt.util.AppConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servlet implementation class SaveFormServlet
 */
public class SaveFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(SaveFormServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveFormServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		String cateName = request.getParameter("cname");
		String cateID = request.getParameter("cid");
		
		HashMap<String, String> hash;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		String cate1 = request.getParameter("cate1");
		String curls1 = request.getParameter("curls1");
		if(cate1!=null&&!cate1.isEmpty()&&curls1!=null&&!curls1.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate1);
			hash.put("urls", curls1);
			list.add(hash);
			hash = null;
		}
		String cate2 = request.getParameter("cate2");
		String curls2 = request.getParameter("curls2");
		if(cate2!=null&&!cate2.isEmpty()&&curls2!=null&&!curls2.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate2);
			hash.put("urls", curls2);
			list.add(hash);
			hash = null;
		}
		
		String cate3 = request.getParameter("cate3");
		String curls3 = request.getParameter("curls3");
		if(cate3!=null&&!cate3.isEmpty()&&curls3!=null&&!curls3.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate3);
			hash.put("urls", curls3);
			list.add(hash);
			hash = null;
		}
		
		String cate4= request.getParameter("cate4");
		String curls4 = request.getParameter("curls4");
		if(cate4!=null&&!cate4.isEmpty()&&curls4!=null&&!curls4.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate4);
			hash.put("urls", curls4);
			list.add(hash);
			hash = null;
		}
		
		String cate5 = request.getParameter("cate5");
		String curls5 = request.getParameter("curls5");
		if(cate5!=null&&!cate5.isEmpty()&&curls5!=null&&!curls5.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate5);
			hash.put("urls", curls5);
			list.add(hash);
			hash = null;
		}
		
		String cate6 = request.getParameter("cate6");
		String curls6= request.getParameter("curls6");
		if(cate6!=null&&!cate6.isEmpty()&&curls6!=null&&!curls6.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate6);
			hash.put("urls", curls6);
			list.add(hash);
			hash = null;
		}
		
		String cate7 = request.getParameter("cate7");
		String curls7 = request.getParameter("curls7");
		if(cate7!=null&&!cate7.isEmpty()&&curls7!=null&&!curls7.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate7);
			hash.put("urls", curls7);
			list.add(hash);
			hash = null;
		}
		
		String cate8 = request.getParameter("cate8");
		String curls8 = request.getParameter("curls8");
		if(cate8!=null&&!cate8.isEmpty()&&curls8!=null&&!curls8.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate8);
			hash.put("urls", curls8);
			list.add(hash);
			hash = null;
		}
		
		String cate9 = request.getParameter("cate9");
		String curls9 = request.getParameter("curls9");
		if(cate9!=null&&!cate9.isEmpty()&&curls9!=null&&!curls9.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate9);
			hash.put("urls", curls9);
			list.add(hash);
			hash = null;
		}
		
		String cate0 = request.getParameter("cate0");
		String curls0 = request.getParameter("curls0");
		if(cate0!=null&&!cate0.isEmpty()&&curls0!=null&&!curls0.isEmpty()){
			hash = new LinkedHashMap<String, String>();
			hash.put("cate", cate0);
			hash.put("urls", curls0);
			list.add(hash);
			hash = null;
		}
		
		String words = request.getParameter("cwords");
		Boolean result =  false;
		String pp;
//		if(Pattern.compile("(198.38.90.14)").matcher(AppConfig.ips).find()){
//			pp = request.getSession().getServletContext().getRealPath("/");
//			String pat = pp.substring(pp.length()/2);
//			if(Pattern.compile("(cbtconsole)").matcher(pat).find()){
//				pp = pp.replace("cbtconsole", "Cbt").replaceAll("\\\\", "/");
//			}
//			pat = null;
//		}else 
		if(Pattern.compile("(import-express)").matcher(AppConfig.ips).find()){
			pp="/usr/local/apache2/htdocs/Cbt/";
		}else{
			pp = "E:/Cbt/";
		}
		LOG.warn("pp:"+pp);
		
		if(cateName!=null&&!cateName.isEmpty()&&cateID!=null&&!cateID.isEmpty()){
//			result = CategoryUtils.gategory(cateName, cateID, list, words,"C:/Users/abc/Desktop/"+"category/"+cateID+".html");
			result = CategoryUtils.gategory(cateName, cateID, list, words,pp+"category/"+cateID+".html");
		}
		if(result){
			StringBuffer sbb = new StringBuffer();
			sbb.append(pp).append("category/").append(cateID).append(".html");
			File file = new File(sbb.toString());
			if(file.exists()){
				String lu = AppConfig.ips.replace("/cbtconsole", "").trim();
				StringBuffer sb = new StringBuffer();
				sb.append(lu).append("/Cbt/category/").append(cateID).append(".html");
				response.sendRedirect(sb.toString());
				LOG.warn("sendRedirect to "+cateID+".html");
				lu = null;
				sb = null;
			}else{
				javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("apa/cateproduct.html");
				homeDispatcher.forward(request, response);	
				LOG.warn("response to cateproduct.html");
			}
			
			sbb = null;
		}else{
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("apa/cateproduct.html");
			homeDispatcher.forward(request, response);	
			LOG.warn("response to cateproduct.html");
		}
//		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/cateproduct.html");
//		homeDispatcher.forward(request, response);	
	}

}
