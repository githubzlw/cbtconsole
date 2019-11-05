package com.cbt.controller;

import com.cbt.bean.*;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.parse.service.StrUtils;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.service.AutoOrderService;
import com.cbt.service.ZoneService;
import com.cbt.service.impl.ZoneServiceImpl;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/autoorder")
public class AutoOrderController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(AutoOrderController.class);
	@Autowired
	private AutoOrderService autoOrderService ;
	@Autowired
	private IOrderinfoService iOrderinfoService;
	private DecimalFormat format = new DecimalFormat("#0.00");
	private PaymentDaoImp paymentDao = new PaymentDao();
	private ZoneService zoneService = new ZoneServiceImpl();
	//获取订单号
	@RequestMapping("/getOrderNo")
	@ResponseBody
	public String getOrderNo(HttpServletRequest request,
			HttpServletResponse response) {
		DataSourceSelector.set("dataSource127hop");
		String orderid =iOrderinfoService.getOrderNo();
		DataSourceSelector.restore();
		//autoOrderService.getOrderNo();
		String orderNo = Utility.generateOrderNumber(Long.parseLong(orderid+ ""));
		String paySID = UUID.randomUUID().toString();
		return orderNo + "@" + paySID;
	}
	/**生成进账记录
	 * @date 2016年11月18日
	 * @author abc
	 * @param userid 用户id
	 * @param paymentAmount 总额
	 * @param ptype 支付类型
	 * @param orderno 订单号
	 * @param paymentid 支付id
	 * @param orderdesc paypal账号
	 * @param dealMan 处理人
	 * @param otime 付款时间
	 * @return  
	 */
	@RequestMapping(value="/payment",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String addPayment(String userid,String paymentAmount,String ptype,String orderno,
			String paymentid,String orderdesc,String dealMan,String otime,String upfile){
		if(dealMan==null||dealMan.isEmpty()){
			return "0";
		}
		if(userid==null||userid.isEmpty()||!StrUtils.isMatch(userid, "(\\d+)")){
			return "";
		}
		if(paymentAmount==null||paymentAmount.isEmpty()||!StrUtils.isMatch(paymentAmount, "(\\d+\\.*\\d*)")){
			return "";
		}
		if(orderdesc==null||orderdesc.length()<3){
			return "-1";
		}
		orderno = orderno!=null?orderno.trim():orderno;
		String paySID = UUID.randomUUID().toString();
		int int_userid = Integer.valueOf(userid);
		PaymentBean payment = new PaymentBean();
		payment.setUserid(int_userid);
		payment.setOrderid(orderno);
		payment.setPaymentid(paymentid);
		payment.setPayment_amount(paymentAmount);
		payment.setPayment_cc("USD");
		payment.setOrderdesc(orderdesc);
		payment.setPaystatus("1");
		payment.setPaySID(paySID);
		payment.setPayflag("O");
		payment.setPaytype(ptype);
		payment.setCreatetime(otime);
		payment.setPayment_other("0");
		//用户信息
		IUserDao user_dao  =new UserDao();
		UserBean userBean = iOrderinfoService.getUserFromIdForCheck(int_userid);
		//user_dao.getUserFromIdForCheck(int_userid);
		if(userBean!=null){
			//用户email
			payment.setUsername(userBean.getName());
		}
		//记录
//		paymentDao.countPaymentInvoiceByorderuser(userid, orderno);
		writeOrderNote(userid,orderno,dealMan,upfile);
		DataSourceSelector.set("dataSource127hop");
		int addPayment =iOrderinfoService.addPayment(payment);
		DataSourceSelector.restore();
		//paymentDao.addPayment(payment );
		return addPayment>0?paySID:"";
	}
	
	
	/**生成订单
	 * @date 2016年11月18日
	 * @author abc
	 * @param userid 用户id
	 * @param price 金额
	 * @param remark 备注
	 * @param ostate状态 0-未付款  5-确认价格中
	 * @param dealMan 处理人员
	 * @param upfile 上传文件
	 * @return  
	 */
	@RequestMapping(value="/add",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public  String addOrder(String userid,String price,String remark,String ostate,String dealMan,String upfile){
//		Map<String, Object>  map = new HashMap<String, Object>();
		try{
			if(dealMan==null||dealMan.isEmpty()){
				return "0";
			}

			if(userid==null||userid.isEmpty()||!StrUtils.isMatch(userid, "(\\d+)")){
				return "";
			}

			if(price==null||price.isEmpty()||!StrUtils.isMatch(price, "(\\d+\\.*\\d*)")){
				return "";
			}

			int userId = Integer.valueOf(userid);
			String exchange_rate =iOrderinfoService.getExchangeRate();
			//获取用户地址
			DataSourceSelector.set("dataSource127hop");
			List<Address> userAddr =iOrderinfoService.getUserAddr(userId);
//			autoOrderService.getUserAddr(userId);
			if(userAddr.size()==0){
				return "-5";
			}
			String orderid = iOrderinfoService.getOrderNo();
//			autoOrderService.getOrderNo();
			//autoOrderService.getExchangeRate();
			String orderNo = Utility.generateOrderNumber(Long.parseLong(orderid+ ""));

			Address address = userAddr.get(0);
			int address_id = address.getId();
			Integer yourOrder = 1;

			//添加订单表
			OrderBean orderBean = new OrderBean();
			orderBean.setOrderNo(orderNo);
			orderBean.setUserid(userId);
			double productCost =  yourOrder*Double.valueOf(price);
			orderBean.setProduct_cost(format.format(productCost));
			orderBean.setState(Integer.valueOf(ostate));
			orderBean.setDeliveryTime(15);
			orderBean.setService_fee("0");
			orderBean.setIp("");
			orderBean.setDetails_number(yourOrder);
			orderBean.setPay_price_three("");
			orderBean.setForeign_freight("0");
			orderBean.setPay_price(productCost);
			orderBean.setPay_price_tow("");
			orderBean.setCurrency("USD");
			orderBean.setActual_ffreight("0");
			orderBean.setDiscount_amount(0);
			orderBean.setOrder_ac(0);
			orderBean.setActual_lwh("0");
			orderBean.setActual_weight("1");
			orderBean.setActual_weight_estimate(1);
			orderBean.setExtra_freight(0);
			orderBean.setPackag_number(1);
			orderBean.setOrderRemark(remark);
			orderBean.setExchange_rate(exchange_rate);

			String country = address.getCountry();
			String county_ = "USA";
//		if(StrUtils.isNullOrEmpty(country)&&StrUtils.isMatch(country, "(\\d+)")){
			county_ =iOrderinfoService.getCountryById(country,userid);
//			zoneService.getCountryById(country,userid);
			if(StringUtil.isBlank(county_)){
				return "-5";
			}
//		}
			orderBean.setMode_transport("China Post Express@5-9@"+county_+"@0.0@all");
			List<OrderBean> OrderBeanList = new ArrayList<OrderBean>();
			OrderBeanList.add(orderBean);
			int addOrderInfo = iOrderinfoService.addOrderInfo(OrderBeanList, address_id);
//			autoOrderService.addOrderInfo(OrderBeanList, address_id, 0);
			if(addOrderInfo<1){
				return "-4";
			}

			//添加order-detail
			List<OrderDetailsBean> orderdetails = new ArrayList<OrderDetailsBean>();
			OrderDetailsBean detailBean = new OrderDetailsBean();
			detailBean.setOrderid(orderNo);
			detailBean.setDelivery_time("15");
			detailBean.setUserid(userId);
			detailBean.setGoodsname("Import Express Product");
			detailBean.setGoodsprice(format.format(productCost));
			detailBean.setRemark(remark);
			detailBean.setYourorder(yourOrder);
			long time = new Date().getTime();
			detailBean.setGoods_url("http://import-express.com/"+time+".html");
			detailBean.setGoods_img("/img/1.png");
			detailBean.setOd_bulk_volume("");
			detailBean.setOd_total_weight(0.001);
			detailBean.setGoodsid(1400);
			orderdetails.add(detailBean);
//			int addOrderDetail =iOrderinfoService.addOrderDetail(orderdetails );
			int addOrderDetail =autoOrderService.addOrderDetail(orderdetails );
			if(addOrderDetail<1){
				return "-3";
			}
			//添加order_address
			Map<String, Object> addressMap = new HashMap<String, Object>();
			addressMap.put("orderno", orderNo);
			addressMap.put("addressid", String.valueOf(address_id));
			addressMap.put("country", address.getCountryname());
			addressMap.put("statename", address.getStatename());
			addressMap.put("address", address.getAddress());
			addressMap.put("address2", address.getAddress2());
			addressMap.put("phoneNumber", address.getPhone_number());
			addressMap.put("zipcode", address.getZip_code());
			addressMap.put("street", address.getStreet());
			addressMap.put("recipients", address.getRecipients());
//			int addOrderAddress =iOrderinfoService.addOrderAddress(addressMap);
			int addOrderAddress =autoOrderService.addOrderAddress(addressMap);
			writeOrderNote(userid,orderNo,dealMan,upfile);
			return addOrderAddress>0?orderNo:"";
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return "";
	}


	public void writeOrderNote(String userid,String orderno,String dealMan,String upfile){
		DataSourceSelector.restore();
		if(iOrderinfoService.countPaymentInvoiceByorderuser(userid, orderno)==0){
			iOrderinfoService.addPaymentNote(userid, orderno, dealMan,upfile);
//			paymentDao.addPaymentNote(userid, orderno, dealMan);
		}else{
			iOrderinfoService.updatePaymentNote(userid, orderno, dealMan,upfile);
//			paymentDao.updatePaymentNote(userid, orderno, dealMan, 1);
		}
	}
	
	/**读取invoice文件
	 * @date 2016年11月8日
	 * @author abc
	 * @param request
	 * @param response
	 * @throws IOException  
	 */
	@RequestMapping(value="/show")
   public void showFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
	 	String orderid = request.getParameter("orderid"); //文件名
	    String fileByOrderid =iOrderinfoService.getFileByOrderid(orderid);
	    //paymentDao.getFileByOrderid(orderid);
	    String index = request.getParameter("index");
	    index = StrUtils.isNullOrEmpty(index)?"0":index;
	    index = StrUtils.isMatch(index, "(\\d+)")?index:"0";
	    OutputStream outStream=response.getOutputStream(); //得到向客户端输出二进制数据的对象   
	    FileInputStream fileIs=null;
	   //测试用
//	    fileByOrderid = "C:/Users/abc/Desktop/Invoice23.pdf,";
	    
	    fileByOrderid = fileByOrderid==null?"":fileByOrderid;
	    fileByOrderid = fileByOrderid.endsWith(",")?
	    		fileByOrderid.substring(0,fileByOrderid.length()-1):fileByOrderid;
		fileByOrderid = fileByOrderid.replaceAll("F:","/root/F:");
	    String[] files = fileByOrderid.split(",");
	    if(Integer.valueOf(index)<files.length){
	    	fileByOrderid = files[Integer.valueOf(index)];
	    }
	    fileByOrderid = fileByOrderid.replace("//", "/");
	    String type = "";
	    if(fileByOrderid.length()>1){
	    	if(fileByOrderid.indexOf(".pdf")>-1){
    			type = "application/pdf";
    		}else{
    			type = "image/.*";
    		}
    		try {//saveFileUrl+imgFile
    			fileIs = new FileInputStream(fileByOrderid);
    			int i=fileIs.available(); //得到文件大小   
    			byte data[]=new byte[i];   
    			fileIs.read(data);  //读数据   
    			outStream.write(data);  //输出数据      
    			if(fileIs!=null){
    				fileIs.close();   
    			}
    		} catch (Exception e) {
    			LOG.error("系统找不到图像文件："+fileByOrderid);  
    		}
	    }
	    response.setContentType(type); //设置返回的文件类型   
	    outStream.flush();  
	    outStream.close();  
   }
	/**
	 * @date 2016年11月8日
	 * @author abc
	 * @param request
	 * @param response
	 * @return  
	 * @throws IOException 
	 * @throws ServletException
	 */
	@RequestMapping(value="/shows")
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		ModelAndView mv = new ModelAndView("invoiceshow");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String orderid = request.getParameter("orderid");
		String fileByOrderid = iOrderinfoService.getFileByOrderid(orderid);
		fileByOrderid = fileByOrderid==null?"":fileByOrderid;
	    fileByOrderid = fileByOrderid.endsWith(",")?
	    		fileByOrderid.substring(0,fileByOrderid.length()-1):fileByOrderid;
	
		mv.addObject("orderid", orderid);
		mv.addObject("list", fileByOrderid.split(","));
		return mv;
		
	}
	
	/**销售自生成订单
	 * @date 2016年11月18日
	 * @author abc
	 * @param userid
	 * @param orderid
	 * @return  
	 */
	@RequestMapping(value="/alist")
	public ModelAndView getAutoList(String userid, String orderid, String page){
		ModelAndView mv = new ModelAndView("autoorderlist");
		if(page==null||page.isEmpty()||!StrUtils.isMatch(page, "(\\d+)")){
			page = "1";
		}
		mv.addObject("userid", userid);
		mv.addObject("orderid", orderid);
		mv.addObject("currentpage", page);
		List<AutoOrderBean> orderList =iOrderinfoService.getOrderList(orderid, userid, page);
//		autoOrderService.getOrderList(orderid, userid, page);
		int count  = 0;
		if(orderList!=null&&!orderList.isEmpty()){
			count = orderList.get(0).getCount();
			count = count%40==0?count/40:count/40+1;
		}
		
		mv.addObject("orderList", orderList);
		mv.addObject("totalpage", count);
		return mv;
	}
	
	/**取消自生成订单
	 * @date 2016年11月18日
	 * @author abc
	 * @param userid
	 * @param orderid
	 * @return  
	 */
	@RequestMapping(value="/cancelorder",method={RequestMethod.GET,RequestMethod.POST})
	public  String cancelOrder(String orderid){
		if(orderid==null||orderid.isEmpty()){
			return "";
		}
		System.err.println(">>>>>>>>>>>"+orderid);
//		autoOrderService.cancelOrder(orderid);
		return iOrderinfoService.cancelOrder(orderid)+"";
	}
	
	/**取消自生成订进账记录
	 * @date 2016年11月18日
	 * @author abc
	 * @param userid
	 * @param orderid
	 * @return  
	 */
	@RequestMapping(value="/cancelpay",method={RequestMethod.GET,RequestMethod.POST})
	public  String cancelPayment(String pid){
		if(pid==null||pid.isEmpty()||!StrUtils.isMatch(pid, "(\\d+)")){
			return "";
		}
		System.err.println(">>>>>>>>>>>"+pid);
//		autoOrderService.cancelPayment(pid);
		return iOrderinfoService.cancelPayment(pid)+"";
	}
	
	
}