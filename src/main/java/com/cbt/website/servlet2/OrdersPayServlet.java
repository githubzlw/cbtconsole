package com.cbt.website.servlet2;

import com.cbt.website.server.GoodsServer;
import com.cbt.website.server.IGoodsServer;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Servlet implementation class OrdersPayServlet
 */
public class OrdersPayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrdersPayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IGoodsServer goosd = new GoodsServer();
		List<Object[]> list = goosd.getOrdersPay();
		for (int i = 0; i < list.size(); i++) {
			String paymentid = (String) list.get(i)[0];
			int paystatus = getPayState(paymentid);
			list.get(i)[0] = paystatus;
		}
		PrintWriter out = response.getWriter();
		out.print(JSONArray.fromObject(list));
		out.flush();
		out.close();
	}

	
	public int getPayState(String paymentid) throws IOException{
		paymentid = paymentid.toUpperCase();
		String str="cmd=_notify-synch&tx="+paymentid+"&at=Mi-Qu2mmOY841halXqpYeQABC7ZelRe7Ts5hL9a4__qabCxAE4bGsQ3IWqy";
		URL u = new URL("https://www.paypal.com/cgi-bin/webscr");  
	    //正式环境  
	    URLConnection uc = u.openConnection();  
	    uc.setDoOutput(true);  
	    uc.setRequestProperty("Content-Type",  
	            "application/x-www-form-urlencoded");  
	    PrintWriter pw = new PrintWriter(uc.getOutputStream());  
	    pw.println(str);  
	    pw.close();  
	    //接受 PayPal 对 IPN 回发的回复信息  
	    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	    String header=in.readLine();
	    String res="";
	    int paystatus = 0;
	    while((res=in.readLine())!=null){	
	    	String[] temp=res.split("=");
	    	if(temp.length>1){
	    		String paystatuString = temp[1];
	    		  if(header.equals("SUCCESS")&&temp[0].equals("payment_status")&&(paystatuString.equals("Completed")||paystatuString.equals("Pending"))){
	    			paystatus = paystatuString.equals("Pending") ? 2 : 1;
	    		} 
	    	} 
	    }
		return paystatus;
	}
}
