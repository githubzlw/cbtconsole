package com.cbt.bigpro.servlet;

import com.alibaba.fastjson.JSON;
import com.cbt.bigpro.bean.AliCategoryPojo;
import com.cbt.bigpro.bean.BigGoodsArea;
import com.cbt.bigpro.dao.BigGoodsAreaDao;
import com.cbt.bigpro.dao.BigGoodsAreaDaoImpl;
import com.cbt.bigpro.dao.GoodInfoDao;
import com.cbt.bigpro.dao.GoodsInfoDaoImpl;
import com.cbt.util.WebTool;
import net.minidev.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class BigGoodsAreaServlet
 */
public class BigGoodsAreaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BigGoodsAreaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void AllGoodInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String  gid = request.getParameter("id");
		int page  = Integer.parseInt(request.getParameter("page"));
		List<BigGoodsArea>  goodsList = new ArrayList<BigGoodsArea>();
		BigGoodsAreaDao  bg =  new BigGoodsAreaDaoImpl();
		int pagesize = 20;//每页显示记录数
		if(gid==null||gid==""){
			if(page!=-2){
				goodsList= bg.getBigGoodsIfo(gid,page, pagesize);
			}
		}else{
		   if(page==-2){
			   goodsList= bg.getBigGoodsIfo(gid, page, pagesize);
		   }
		}
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(goodsList));
	}
	
	
	protected void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id =request.getParameter("id");
		String category =request.getParameter("category");
		double num =Double.parseDouble(request.getParameter("num"));
		double price =Double.parseDouble(request.getParameter("price"));
		double discount =Double.parseDouble(request.getParameter("discount"));
		String url =request.getParameter("url");
		String title = request.getParameter("title");
		String img = request.getParameter("img");
		String  weight = request.getParameter("weight");
		String   catid = request.getParameter("catid");
		String keyword = request.getParameter("keyword");
		BigGoodsArea  bg = new BigGoodsArea();
		bg.setGoodsId(id);
		bg.setCategory(category);
		bg.setTitle(title);
		bg.setNum(num);
		bg.setPrice(price);
		bg.setDiscount(discount);
		bg.setGoodsurl(url);
		bg.setImg(img);
		bg.setWeight(weight);
		bg.setCatid1(catid);
		bg.setKeyWord(keyword);
		BigGoodsAreaDao  bgdao = new BigGoodsAreaDaoImpl();
		int i = bgdao.save(bg);
		String  result  = "";
		if(i==1){
			result  = "true";
		}else{
			result ="false";
		}
		WebTool.writeJson(result, response);
	}
	
	/*//下架
	protected void offshelf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String  pids = request.getParameter("offshelfId");
		String[] offshelfIds = pids.split(",");
        BigGoodsAreaDao  bg = new BigGoodsAreaDaoImpl();
        int i =  bg.updateBigGoodsArea(offshelfIds);
        String  result  = "";
		if(i==1){
			result ="{\"status\":false,\"message\":\"\"}";
			
		}else{
			result  = "{\"status\":true,\"message\":\"\"}";
		}
		WebTool.writeJson(result, response);
		
	}*/
	
	//检查商品是否存在于大货区
	protected void checkIsExistence(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     String  pid = request.getParameter("pid");
	     BigGoodsAreaDao  bg = new BigGoodsAreaDaoImpl();
	     int  i=  bg.isExistence(pid);
	     String result = "";
	     if(i>=1){
	    	 result  = "false";
	     }else{
	    	 result  = "true";
	     }
	     WebTool.writeJson(result, response);
	}
	
	
	//获取类别
		protected  void  getAliCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		     BigGoodsAreaDao  bg = new BigGoodsAreaDaoImpl();
		     List<AliCategoryPojo> list =  bg.getAliCategory();
		     PrintWriter out = response.getWriter();
			out.print(JSONArray.toJSONString(list));
		}
		
 //获取下级类别		
		protected  void  getSubType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    String id = request.getParameter("id");
			BigGoodsAreaDao  bg = new BigGoodsAreaDaoImpl();
		     List<AliCategoryPojo> list =  bg.getSubType(id);
		     PrintWriter out = response.getWriter();
			 out.print(JSONArray.toJSONString(list));
		}
		

		//检查商品
		protected void findGoodsByPid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String pid = request.getParameter("id");
	        GoodInfoDao  gd = new GoodsInfoDaoImpl();
	        BigGoodsArea  goodsDao  = gd.getGoodsInfo(pid);
	        String img = "";
	        String weight = "";
	        if(goodsDao.getImg().contains("[")){
	        	//从goodsdata表 获取数据
	        	 if(goodsDao.getImg().contains("http://")){
	        		 img = goodsDao.getImg().substring(1, goodsDao.getImg().lastIndexOf("]"));
	        	 }else{
	        	//从custom_goods表 获取数据
	        		 String[] imgs = (goodsDao.getImg().substring(1, goodsDao.getImg().lastIndexOf("]"))).split(",");
		        	 img = imgs[0];
		        	 img = goodsDao.getLocalpath()+img;
	        	 }
	        }else{
	        	img = goodsDao.getImg();
	        }
	        if(goodsDao.getWeight().contains("g")){
	        	double weightcut =Double.parseDouble(goodsDao.getWeight().substring(0, goodsDao.getWeight().lastIndexOf("g")));
	        	DecimalFormat df=new DecimalFormat("0.00");  
	        	double avgWeight = weightcut/1000;
	        	weight = df.format(avgWeight); 
	        	goodsDao.setWeight(weight);
	        }
	        goodsDao.setImg(img);
	        String  json  =JSON.toJSONString(goodsDao);
	        PrintWriter out = response.getWriter();
			out.print(json);
		}

		//获取类别对应的商品
		protected void findGoodsByCategoryId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String catid = request.getParameter("catid");
			String  catid1 = request.getParameter("catid1");
			 BigGoodsAreaDao  bg = new BigGoodsAreaDaoImpl();
			 List<BigGoodsArea> list  = bg.findGoodsByCategoryId(catid,catid1);
			  PrintWriter out = response.getWriter();
			out.print(JSONArray.toJSONString(list));
		}
		//下架产品
		protected void delteCommodityByid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	         String id = request.getParameter("id");
	         BigGoodsAreaDao  bg = new BigGoodsAreaDaoImpl();
	         int i =  bg.delteCommodityByid(id);
	         String result = "";
	         if(i==1){
	        	 result = "true";
	         }else{
	        	 result = "false";
	         }
	         WebTool.writeJson(result, response);
		}
		
		
}
