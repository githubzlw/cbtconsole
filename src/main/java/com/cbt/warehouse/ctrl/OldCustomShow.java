package com.cbt.warehouse.ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cbt.bean.Orderinfo;
import com.cbt.pojo.Admuser;
import com.cbt.util.SplitPage;
import com.cbt.warehouse.pojo.OldCustom;
import com.cbt.warehouse.service.OldCustomShowService;


@Controller
@RequestMapping("/OldCustomShow")
public class OldCustomShow {
	@Autowired
	private OldCustomShowService oldCustomShowService;
	@RequestMapping(value = "/getOldCustomShow", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void getNotShipped(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="admName",defaultValue="-1")String admName) {
		String email=request.getParameter("email");
		String staTime=request.getParameter("staTime");
		String enTime=request.getParameter("enTime");
		String id=request.getParameter("id");
		String cuName=request.getParameter("cuName");
		int pagesize=35;
		int start = (page-1) * pagesize;
		List<OldCustom> list=this.oldCustomShowService.FindOldCustoms(admName,staTime,enTime,email,id,cuName,start,pagesize);
		request.setAttribute("OldCustomShows", list);
		int goodsCheckCount = this.oldCustomShowService.getOldCustomCount(admName,staTime,enTime,email,id,cuName);
		SplitPage.buildPager(request, goodsCheckCount, pagesize, page);
		request.setAttribute("admName", admName);
		request.setAttribute("email", email);
		request.setAttribute("timeFrom", staTime);
		request.setAttribute("timeTo", enTime);
		request.setAttribute("id", id);
		request.setAttribute("cuName", cuName);
		try {
			request.getRequestDispatcher("/website/oldCustomersShow.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getAlladm")
	@ResponseBody
	public List<Admuser> getAlladm(HttpServletRequest request, HttpServletResponse response) {
	
		List<Admuser> list=this.oldCustomShowService.FindAllAdm();
		
		return list;
	}
	@RequestMapping(value = "/getCusOrder")
	@ResponseBody
	public void getCusOrder(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="page",defaultValue="1")int page) {
		int pagesize=35;
		int start = (page-1) * pagesize;
		String usid=request.getParameter("usid");
		String order=request.getParameter("order");
		if ("".equals(order)) {
			order=null;
		}
		List<Orderinfo> list=this.oldCustomShowService.FindOrderByUsid(usid,start,pagesize,order);
		request.setAttribute("orderAll", list);
		int goodsCheckCount = this.oldCustomShowService.getOrderCount(usid,order);
		SplitPage.buildPager(request, goodsCheckCount, pagesize, page);
		request.setAttribute("order", order);
		request.setAttribute("usid", usid);
		try {
			request.getRequestDispatcher("/website/OrserAll.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
