package com.cbt.parse.servlet;

import com.cbt.parse.bean.DataReturn;
import com.cbt.parse.service.ConnectKeyWords;
import net.sf.json.JSONArray;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class DataReturnServlet
 */
public class DataReturnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataReturnServlet() {
        super();
        // TODO Auto-generated constructor stub
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		// TODO Auto-generated method stub
		JSONArray jsonArray = JSONArray.fromObject("[]");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			String reqWord = request.getParameter("reqWord");
			String website = request.getParameter("val");
			
			//取得Application对象   
	        ServletContext application=this.getServletContext();   
	        //取得Application属性   
	        Object attribute_words = application.getAttribute("words");
	        
//	        IntensveDao id = new IntensveDao();
//			ArrayList<HashMap<String, String>> list = id.querryIntensive();
//	        System.out.println("--------------");
//	        System.out.println(attribute_words);
			String inwords = attribute_words!=null?attribute_words.toString():null;
//			System.out.println("------*******--------");
//			System.out.println(attribute_words);
//			System.out.println("***************");
			
			/*获取的数据返回页面*/
			if(reqWord!=null&&!reqWord.isEmpty()&&website!=null&&!website.isEmpty()){
				List<DataReturn> connectData = ConnectKeyWords.getConnectAli(reqWord,inwords);
				if(connectData!=null){
					jsonArray = JSONArray.fromObject(connectData);
				}
			}
			
		} catch (Exception e) {
			
		}finally{
			if(out!=null){
				out.print(jsonArray);
				out.flush();
				out.close();
			}
			jsonArray = null;
		}
	}
}
