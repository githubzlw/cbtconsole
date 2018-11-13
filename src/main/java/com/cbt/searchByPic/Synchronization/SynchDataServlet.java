package com.cbt.searchByPic.Synchronization;

import com.cbt.bean.SearchResults;
import com.cbt.parse.service.StrUtils;
import com.cbt.searchByPic.dao.SynchDataDao;
import com.cbt.searchByPic.dao.SynchDataDaoImpl;
import com.cbt.util.WebTool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class JspToHtmlServlet
 */
public class SynchDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SynchDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String  indexId = request.getParameter("indexId");
		if(StrUtils.isNotNullEmpty(indexId)){
			SynchDataDao  synchdao = new SynchDataDaoImpl();
			//从国内服务器数据库取数据  
			List<SearchResults>  list  = synchdao.selectInfoByIndexId(Integer.parseInt(indexId));
			 //远程插入数据
			 int res = synchdao.insertInfo(list,Integer.parseInt(indexId));
			 String result = "";
			 if(res==1){
				 result= "true";
			 }else{
				 result = "false";
			 }
			 WebTool.writeJson(result, response);
			}
			
		}
		
		
	}

