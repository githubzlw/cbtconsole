package com.cbt.website.servlet;

import com.cbt.service.RefundSSService;
import com.cbt.service.impl.RefundServiceImpl;
import com.cbt.website.bean.PayCheckBean;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取用户信息
 */
@Controller
public class PaypalCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PaypalCheckServlet.class);
	private RefundSSService  refundDao = new RefundServiceImpl(); 
	
	
    public PaypalCheckServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PrintWriter out = response.getWriter();
		try {
			String userid = request.getParameter("userid");
			String datestart = request.getParameter("datestart");
			String dateend = request.getParameter("dateend");
			String email = request.getParameter("email");
			String paymentemail=request.getParameter("paymentemail");
			String paytype=request.getParameter("paytype");
			String page=request.getParameter("page");
			userid = userid.replaceAll("\\D+", "").trim();
			userid = userid.isEmpty()?"-1":userid;
			if(datestart!=null&&datestart.isEmpty()){
				datestart = null;
			}
			if(dateend!=null&&dateend.isEmpty()){
				dateend = null;
			}
			if(page==null||page.isEmpty()||"0".equals(page)){
				page = "1";
			}else{
				page = page.replaceAll("\\D+", "").trim();
				page = page.isEmpty()?"1":page;		
			}
			if(paymentemail!=null&&paymentemail.isEmpty()){
				paymentemail = null;
			}
			if(paytype!=null&&paytype.isEmpty()){
				paytype = null;
			}
			if(datestart!=null&&dateend!=null&&dateend.equals(datestart)){
				datestart = datestart + " 00:00:00";
				dateend = dateend + " 23:59:59";
			}
			PayCheckBean bean = new PayCheckBean();
			bean.setPage(Integer.parseInt(page));
			bean.setUserid(Integer.parseInt(userid));
			bean.setEmail(email);
			bean.setDataStart(datestart);
			bean.setDataEnd(dateend);
			bean.setPayEmail(paymentemail);
			bean.setPaytype(paytype);
			PaymentDaoImp  dao  =new PaymentDao();
			list = dao.query(bean);
			double allTotalMoney=dao.getAllTotalMoney(bean);
			PaymentBean	pb = new PaymentBean();
			pb.setUserid(-2);
			pb.setAllTotalMoney(allTotalMoney);
			list.add(pb);
			//request.setAttribute("allTotalMoney", allTotalMoney);
		} catch (Exception e) {
			LOG.warn("",e);
		}finally{
			out.print(JSONArray.fromObject(list));
			out.flush();
			out.close();
		}
		
	}
	
}
