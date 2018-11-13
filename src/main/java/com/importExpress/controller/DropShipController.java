package com.importExpress.controller;

import com.importExpress.pojo.DShippUser;
import com.importExpress.service.DropShippApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/dropship")
public class DropShipController {
	private final static int PAGESIZE = 10;
	private static final long serialVersionUID = 1L;
	
		@Autowired
		private DropShippApplyService dropShippApplyService;
	/**
	 * 方法描述:查询DropShippApply
	 */
	@RequestMapping("/dropshiplist.do")
	public String findAllDropShip(Model model, HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		String userCategory = request.getParameter("userCategory");
		if(userCategory != null && !"".equals(userCategory)) {
			model.addAttribute("userCategory", userCategory);
		}else {
			userCategory="2";
		}
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<DShippUser> findAll = dropShippApplyService.findAllDropShip(userCategory,start, PAGESIZE);
	     
//		int total = dropShippApplyService.dropShiptotal(userCategory,start, PAGESIZE);
//		SplitPage.buildPager(request, total, PAGESIZE, page);
		model.addAttribute("ds", findAll);
		return "/dropshiplist";
	}
}