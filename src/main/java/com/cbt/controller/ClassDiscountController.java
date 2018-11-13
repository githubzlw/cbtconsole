package com.cbt.controller;

import com.cbt.bean.DiscountBean;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.ClassDiscountSerice;
import com.cbt.service.impl.ClassDiscountSericeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/discount")
public class ClassDiscountController {
	private static final Log LOG = LogFactory.getLog(ClassDiscountController.class);
	private ClassDiscountSerice classDiscountSerice = new ClassDiscountSericeImpl();
	
	@RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView getDiscountList(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new ModelAndView("classdiscount");
		String catid=req.getParameter("catid");
		String price = req.getParameter("price");
		String desopite = req.getParameter("desopite");
		catid = catid!=null&&!StrUtils.isMatch(catid, "(\\d+)")?null:catid;
		price = price!=null&&!StrUtils.isMatch(price, "(\\d+)")?null:price;
		desopite = desopite!=null&&!StrUtils.isMatch(desopite, "(0\\.\\d+)")?null:desopite;
		List<DiscountBean> discount = classDiscountSerice.getDiscount(catid,price,desopite);
		mv.addObject("list", discount);
		return mv;
	}
	
	/**
	 * @date 2016年11月5日
	 * @author abc
	 * @param type  1-添加/修改  2-删除
	 * @param index
	 * @param showName
	 * @param className
	 * @param price
	 * @param depositRate
	 * @param catid
	 * @param classType
	 * @return  
	 */
	@RequestMapping(value="/deal",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public int dealDiscount(String type,String index,String showName,String className,
			String price,String depositRate,String catid,String classType){
		LOG.warn("type:"+type+"-index:"+index+"-showName:"+showName+"-className:"+className+
				"-price:"+price+"-depositRate:"+depositRate+"-catid:"+catid+"-classType:"+classType);
		if(StrUtils.isNullOrEmpty(type)||!StrUtils.isMatch(type, "(\\d)")){
			return 0;
		}
		int _type = Integer.valueOf(type);
		if(StrUtils.isNullOrEmpty(index)||!StrUtils.isMatch(index, "(\\d+)")){
			index = "0";
		}
		if(_type==2){
			return classDiscountSerice.delete(Integer.valueOf(index));
		}
		
		if(StrUtils.isNullOrEmpty(classType)||!StrUtils.isMatch(classType, "(\\d+)")){
			return -3;
		}
		if(StrUtils.isNullOrEmpty(depositRate)||!StrUtils.isMatch(depositRate, "(0\\.\\d+)")){
			return -2;
		}
		if(StrUtils.isNullOrEmpty(price)||!StrUtils.isMatch(price, "(\\d+)")){
			return -1;
		}
		
		DiscountBean bean = new DiscountBean();
		bean.setId(Integer.valueOf(index));
		bean.setCatid(catid);
		bean.setClassName(className);
		bean.setClassType(Integer.valueOf(classType));
		bean.setShowName(showName);
		bean.setPrice(Integer.valueOf(price));
		bean.setDepositRate(Double.valueOf(depositRate));
		if(classDiscountSerice.isExsis(catid)>0){
			return classDiscountSerice.update(bean);
		}else{
			return classDiscountSerice.add(bean);
		}
	}
	
	/**前台application刷新
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/refresh",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String refresh(){
		String url  = "https://www.import-express.com/app/rclassd";
		DownloadMain.getContentClient(url, null);
		return "refresh success";
	}
	
	
}