package com.cbt.controller;

import com.alibaba.fastjson.JSONArray;
import com.cbt.bean.Payment;
import com.cbt.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private IPaymentService paymentService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray add(Payment payment) {
		Integer rows =paymentService.add(payment);
		if(rows>0){
			return JSONArray.parseArray("添加成功");
		}else{
			return JSONArray.parseArray("添加失败");
		}
	}
	
	@RequestMapping(value = "/delById", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray delById(Integer id) {
		Integer rows =paymentService.delById(id);
		if(rows>0){
			return JSONArray.parseArray("删除单条成功");
		}else{
			return JSONArray.parseArray("删除单条失败");
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray update(Payment payment) {
		Integer rows =paymentService.update(payment);
		if(rows>0){
			return JSONArray.parseArray("修改单条成功");
		}else{
			return JSONArray.parseArray("修改单条失败");
		}
	}
	
	
	
	//根据用户ID查询可退款的到账        可退款条件 1：已支付的单子    2：距今未超过3个月
	@RequestMapping(value = "/getRefundAblePaymentByUid", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getRefundAblePaymentByUid(Integer uid, Double balance, String currency, double appcount, String appcurrency) {
		ModelAndView mv = new ModelAndView("refundAction");
		List<Payment> paymentList =null;
		mv.addObject("uid",uid);
		mv.addObject("balance",balance);
		mv.addObject("appcount",appcount);
		mv.addObject("appcurrency",appcurrency);
		mv.addObject("currency",currency);
		if(uid!=null){
		  paymentList =paymentService.getRefundAblePaymentByUid(uid);
		  if(paymentList!=null && paymentList.size()!=0){
			  	mv.addObject("status","1");    //status   返回状态   0 请求成功但没有数据    1请求成功且有数据   2请求失败，参数错误
			  	mv.addObject("paymentList",paymentList);
			}else{
				mv.addObject("status","0");
				mv.addObject("paymentList","");
			}
		}else{
			mv.addObject("status","2");
			mv.addObject("message","请求出错，请联系管理员");
		}
		return mv;
	}
	
}
