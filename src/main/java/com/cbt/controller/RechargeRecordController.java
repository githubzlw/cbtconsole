package com.cbt.controller;

import com.cbt.bean.RechargeRecord;
import com.cbt.refund.bean.RefundBeanExtend;
import com.cbt.service.IRechargeRecordService;
import com.cbt.service.RefundSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/rechargeRecord")
public class RechargeRecordController {
	
	@Autowired
	private IRechargeRecordService rechargeRecordService;
	@Autowired
	private RefundSSService refundService;
	
	
	@RequestMapping(value = "/findRecordByUid", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView findRecordByUid(Integer uid){
		ModelAndView mv = new ModelAndView("consumingRecordsConsole");
		if(uid!=null){
			List<RechargeRecord> recordsList =rechargeRecordService.findRecordByUid(uid);
			List<RefundBeanExtend> refundList =refundService.searchAllRefundByUid(uid);
			mv.addObject("uid", uid);  
			mv.addObject("recordsList", recordsList);   
			mv.addObject("refundList", refundList);   
			return mv;
		}else{
			return mv.addObject("message","请求参数出错，请检查。");
		}
	}
	
	
	class SortByTimeDesc implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			RefundBeanExtend s1 = (RefundBeanExtend) o1;
			RefundBeanExtend s2 = (RefundBeanExtend) o2;
			return s2.compareTo(s1);
		}
	}
	
}
