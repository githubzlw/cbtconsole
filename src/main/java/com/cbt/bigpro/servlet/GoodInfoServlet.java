package com.cbt.bigpro.servlet;

import com.alibaba.fastjson.JSON;
import com.cbt.bigpro.bean.BigGoodsArea;
import com.cbt.bigpro.dao.GoodInfoDao;
import com.cbt.bigpro.dao.GoodsInfoDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * Servlet implementation class GoodInfoServlet
 */
public class GoodInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodInfoServlet() {
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
		String pid = request.getParameter("id");
        GoodInfoDao gd = new GoodsInfoDaoImpl();
        BigGoodsArea  goodsDao  = gd.getGoodsInfo(pid);
        String img = "";
        String weight = "";
        if(goodsDao.getImg().contains("[")){
        	 String[] imgs = (goodsDao.getImg().substring(1, goodsDao.getImg().lastIndexOf("]"))).split(",");
        	 img = imgs[0];
        	 img = goodsDao.getLocalpath()+img;
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

}
