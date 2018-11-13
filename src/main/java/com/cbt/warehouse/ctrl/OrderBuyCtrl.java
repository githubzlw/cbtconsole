package com.cbt.warehouse.ctrl;

import com.cbt.bean.OrderBuyBean;
import com.cbt.warehouse.service.IOrderBuyService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/orderbuy")
public class OrderBuyCtrl {
	@Autowired
	private IOrderBuyService buyService;
	
	
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ResponseBody 
	public String getById(Integer id){
		OrderBuyBean bean = buyService.getById(id);
		
		return JSONObject.fromObject(bean).toString();
	}
	
//	
//	@RequestMapping(value = "/getById.do", method = RequestMethod.GET)
//	public String getById(HttpServletRequest request, Model model) {
//		Address address = addressService.getById(Integer.valueOf(request.getParameter("id")));
//		
//		if(address != null) {
//			System.out.println("查询成功");
//		}
//		return "indexuser";
//	}
}
