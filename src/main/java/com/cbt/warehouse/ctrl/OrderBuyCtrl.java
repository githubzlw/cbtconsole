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

}
