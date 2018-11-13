package com.cbt.shopcar.controller;

import com.cbt.shopcar.pojo.GoodCar;
import com.cbt.shopcar.service.GoodCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/goodCar")
public class GoodCarController {
	
	
	@Autowired
	private GoodCarService goodCarService;
	
	
	@RequestMapping("/getgoodcar")
	@ResponseBody
	public List<GoodCar> getGoodCar(@RequestParam("session")String sessionid, @RequestParam("userid")Integer userid, @RequestParam("flag")Integer preshopping){
		List<GoodCar> goodCarList = goodCarService.getGoodCar(sessionid, userid, preshopping);
		for (GoodCar goodCar : goodCarList) {
			System.out.println(goodCar.toString());
		}
		return goodCarList;
	}

}
