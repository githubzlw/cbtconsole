package com.cbt.website.servlet;

import com.cbt.website.bean.UserBehavior;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import net.minidev.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class UserStatisitcsServlet
 */
public class UserStatisitcsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserStatisitcsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //客户数
	protected void getStatistics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		Date date = new Date();
//	    DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
//	    System.out.println(df1.format(date));
	    String date=request.getParameter("date");
	    int[] summary= new int[12];
	    IOrderwsDao dao= new OrderwsDao();
	    int result_1=dao.getCountofRegisterUser(date);//注册客户数量
	    int result_2=dao.getCountofUserEnterinsite(date);//进入的用户总数量
	    int result_3=dao.getCountofSearchNotAdd(date);//当日有搜索但没添加购物车的客户
	    int result_4=dao.getCountofAddGoodscar(date);//有添加购物车的客户数量 （以前就注册过的客户）
	    int result_5=dao.getCountofAddNotRegister_1(date);//当日添加购物车，但未完成注册的客户（ 只有一件商品的 ）
	    int result_6=dao.getCountofAddNotRegister_2(date);//当日添加购物车，但未完成注册的客户（有多件商品在购物车中的）
	    int result_7=dao.getCountofRegisterNotAddr(date);//当日添加购物车，完成注册但没输入地址的客户
	    int result_8=0;//当日添加购物车，输入了地址，但在支付界面没点支付按钮的客户    
	    int result_9=dao.getCountofPaypal(date);//当日添加购物车，在支付界面点了支付按钮，但在PayPal没完成支付的客户
	    int result_10=dao.getCountofNeworder(date);//线上新下单的客户数量
	    int result_11=dao.getCountofAddGoodscar_1(date);//有添加购物车的客户数量 （当日完成注册的客户）
	    int result_12=dao.getCountofAddGoodscar_2(date);//有添加购物车的客户数量 （以前就注册过的客户）
	    summary[0]=result_1;
	    summary[1]=result_2;
	    summary[2]=result_3;
	    summary[3]=result_4;
	    summary[4]=result_5;
	    summary[5]=result_6;
	    summary[6]=result_7;
	    summary[7]=result_8;
	    summary[8]=result_9;
	    summary[9]=result_10;
	    summary[10]=result_11;
	    summary[11]=result_12;
	    PrintWriter out = response.getWriter();
		out.print(net.sf.json.JSONArray.fromObject(summary));
	}
	//客户信息以及购物车物品数
	protected void getUserinfoOfStatistics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int type=Integer.parseInt(request.getParameter("type"));
		String date=request.getParameter("date");
		IOrderwsDao dao= new OrderwsDao();
		List<Object> list=new ArrayList<Object>();
		if(type==1){
			list=dao.getUserofRegister(date);
		}else if(type==2){
			list=dao.getUserofEnterinsite(date);
		}else if(type==3){
			list=dao.getUserofSearchNotAdd(date);
		}else if(type==4){
			list=dao.getUserofAddGoodscar(date);
		}else if(type==5){
			list=dao.getUserofAddNotRegister_1(date);
		}else if(type==6){
			list=dao.getUserofAddNotRegister_2(date);
		}else if(type==7){
			list=dao.getUserofRegisterNotAddr(date);
		}else if(type==9){
			list=dao.getUserofPaypal(date);
		}else if(type==10){
			list=dao.getUserofNeworder(date);
		}else if(type==11){
			list=dao.getUserofAddGoodscar_1(date);
		}else if(type==12){
			list=dao.getUserofAddGoodscar_2(date);
		}
		PrintWriter out = response.getWriter();
		out.print(net.sf.json.JSONArray.fromObject(list));
	}
	//客户对应的行为轨迹
	protected void getUserBehavior(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userid=Integer.parseInt(request.getParameter("userid"));
		String view_dtime= request.getParameter("dateTime");
		String view_date_time = null;
		if(view_dtime!=""){
			view_date_time= view_dtime+" 00:00:00";
		}
		int page=Integer.parseInt(request.getParameter("page"));
		int pagesize=40;//每页显示记录数
		List<UserBehavior> list= new ArrayList<UserBehavior>(); 
		IOrderwsDao dao= new OrderwsDao();
		list=dao.recordUserBehavior(userid,page,pagesize,view_date_time);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(list));
	}
}
