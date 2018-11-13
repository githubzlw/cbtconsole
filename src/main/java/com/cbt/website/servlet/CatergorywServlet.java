package com.cbt.website.servlet;

import com.cbt.bean.Eightcatergory;
import com.cbt.parse.service.TypeUtils;
import com.cbt.website.service.CatergorywService;
import com.cbt.website.service.ICatergorywService;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 八大类的后台操作
 */
public class CatergorywServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CatergorywServlet() {
        super();
    }
    
	protected void getCatergory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		int type = Integer.parseInt(request.getParameter("type"));
		String cate = request.getParameter("cate");
		PrintWriter out = response.getWriter();
		ICatergorywService service = new CatergorywService();
		List<Eightcatergory> list = new ArrayList<Eightcatergory>();
		list = service.getCatergory(id,cate,type);
		out.print(JSONArray.fromObject(list));
		out.flush();
		out.close();
	}

	
	/**
	 * 修改
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void upCatergory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int row = Integer.parseInt(request.getParameter("row"));
		String productname = request.getParameter("name");
		String url = request.getParameter("url");
		String imgurl = request.getParameter("imgurl");
		String price = request.getParameter("price");
		String unit = request.getParameter("unit");
		String mind = request.getParameter("mind");
		String id = request.getParameter("id");
		String cate = request.getParameter("catergory");
		ICatergorywService service = new CatergorywService();
		Eightcatergory catergory = new Eightcatergory();
		catergory.setRow(row);
		catergory.setCatergory(cate);
		catergory.setImgurl(imgurl);
		catergory.setUnit(unit);
		catergory.setUrl(url);
		catergory.setMinorder(Integer.parseInt(mind));
		catergory.setProductname(productname);
		catergory.setPrice(Float.parseFloat(price));
		catergory.setId(Integer.parseInt(id));
		catergory.setValid(1);//只有有效的商品才能修改.
		int res = service.upCatergory(catergory);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	/**
	 * 添加
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void addCatergory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productname = request.getParameter("name");
		String url = request.getParameter("url");
		String imgurl = request.getParameter("imgurl");
		String price = request.getParameter("price");
		String unit = request.getParameter("unit");
		String mind = request.getParameter("mind");
		String id = request.getParameter("id");
		String cate = request.getParameter("catergory");
		ICatergorywService service = new CatergorywService();
		Eightcatergory catergory = new Eightcatergory();
		catergory.setImgurl(imgurl);
		catergory.setUnit(unit);
		if(url.indexOf("http:") == -1){
			url = TypeUtils.decodeGoods(url);
		}
		catergory.setUrl(url);
		catergory.setMinorder(Integer.parseInt(mind));
		catergory.setProductname(productname);
		catergory.setPrice(Float.parseFloat(price));
		catergory.setId(Integer.parseInt(id));
		catergory.setCatergory(cate);
		int res = service.addCatergory(catergory);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	protected void deleteCatergory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer row = Integer.parseInt(request.getParameter("row"));
		ICatergorywService service = new CatergorywService();
		int result = service.deleteCatergory(row);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}
	
	
	protected void getCatergoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Object> catergoryList = new ArrayList<Object>();
		ICatergorywService service = new CatergorywService();
		catergoryList = service.getCategoryList();
		PrintWriter out = response.getWriter();
		out.print(JSONArray.fromObject(catergoryList));
//		out.print(catergoryList);
		out.flush();
		out.close();
	}
	
	
}
