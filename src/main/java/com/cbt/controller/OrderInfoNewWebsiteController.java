package com.cbt.controller;

import com.cbt.bean.*;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.pay.service.IOrderServer;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/website/websitenew")
public class OrderInfoNewWebsiteController {

	@Autowired
	private IOrderServer iOrderServer;
	
	@RequestMapping(value = "/loginCart", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, String userId, String itemId,
                        String flag, String FreeShipping, RedirectAttributesModelMap modelMap, Map<String, Object> map) {
		int flagInt = Integer.parseInt(flag);
		String[] userinfo = WebCookie.getUser(request);
		IUserDao udao=new UserDao();
		String username=udao.getUsernameByid(Integer.parseInt(userId));
		if(userinfo == null){
			flagInt = FreeShipping!=null?3:flagInt;
			Cookie cookie=new Cookie("pageState", (flagInt+4)+":"+itemId);
			cookie.setMaxAge(3600);
			cookie.setPath("/");
			response.addCookie(cookie);
			String exist = WebCookie.cookie(request, "userName");
			if(exist == null){
				return "register";
			}
			return "geton";
		}else{
			  if(flagInt==1){
				  modelMap.addAttribute("action", "getSelectedItem");
				  modelMap.addAttribute("className", "RequireInfoServlet");
				  modelMap.addAttribute("userId", userId);
				  modelMap.addAttribute("itemId", itemId);
				  modelMap.addAttribute("state", "1");
                  return "redirect:/paysServlet";
			  }else if(flagInt==0){
					List<Address> addresslist = new ArrayList<Address>();
					String total="";
					String[] userinfos = WebCookie.getUser(request);
					int userid=0;
					if(userinfos != null){
						userid=Integer.parseInt(userinfos[0]);
					}else{
						map.put("code", "1");
						map.put("msg", "User authentication failed");
						return "/geton";
					}
					if(userid!=0){
						addresslist=iOrderServer.getUserAddr(userid);
					}
					//获取对应商品的总价格
					float totalPrice = iOrderServer.getTotalPrice(itemId);
					
					double service_fee = 0;
					if(totalPrice<1000){
						service_fee=(float) (totalPrice*0.08);
					}else if(totalPrice<3000){
						service_fee=(float) (totalPrice*0.06);
					}else if(totalPrice<5000){
						service_fee=(float) (totalPrice*0.04);
					}else if(totalPrice<10000){
						service_fee=(float) (totalPrice*0.03);
					}else{
						service_fee=(float) (totalPrice*0.025);
					}
					
					total=totalPrice+"";
					IZoneServer zs = new ZoneServer();
					List<ZoneBean> zonelist = zs.getAllZone();
					List<StateName> statelist =zs.getStateName();
			        map.put("code", "0");
			        map.put("zonelist", zonelist);
			        map.put("statelist", statelist);
			        map.put("addresslist", addresslist);
			        map.put("total", total);
			        map.put("service_fee", service_fee);
			        map.put("addresssize", addresslist.size());
			        map.put("total", total);
			        //map.put("addressid", addresslist.get(0).getId());
			        map.put("size", addresslist.size());
			        return "address";
			  }else if(flagInt==2){
				  modelMap.addAttribute("action", "getSelectedItemrequiredinfo");
				  modelMap.addAttribute("className", "RequireInfoServlet");
				  modelMap.addAttribute("userId", userId);
				  modelMap.addAttribute("itemId", itemId);
				  modelMap.addAttribute("state", "1");
                  return "redirect:/paysServlet";
			  }
			  else if(flagInt==5){//后台website管理跳转到地址修改页面
				  	UserBean userbean= new UserBean();
					userbean.setId(Integer.parseInt(userId));
					userbean.setName(username);
					request.getSession().setAttribute("userInfo", userbean);
					List<Address> addresslist = new ArrayList<Address>();
					String total="";
					String[] userinfos = WebCookie.getUser(request);
					int userid=0;
					if(userinfos != null){
						userid=Integer.parseInt(userinfos[0]);
					}else{
						map.put("code", "1");
						map.put("msg", "User authentication failed");
						return "/geton";
					}
					if(userid!=0){
						addresslist=iOrderServer.getUserAddr(userid);
					}
					IZoneServer zs = new ZoneServer();
					List<ZoneBean> zonelist = zs.getAllZone();
					List<StateName> statelist =zs.getStateName();
			        map.put("code", "0");
			        map.put("zonelist", zonelist);
			        map.put("statelist", statelist);
			        map.put("addresslist", addresslist);
			        map.put("total", total);
			        map.put("addresssize", addresslist.size());
			        map.put("total", total);
			        //map.put("addressid", addresslist.get(0).getId());
			        map.put("size", addresslist.size());
			        return "websiteaddress";
			  }
			
		}
		return "geton";
	}
	
	@RequestMapping(value = "/delAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delAddress(String id) {
		Map<String, Object> map=new HashMap<String, Object>();
		iOrderServer.delUserAddressByid(Integer.parseInt(id));
        map.put("code", "0");
        map.put("msg", "Successful operation");
		return map;
	}
	
	@RequestMapping(value = "/addAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addAddress(HttpServletRequest request, Address address) {
		Map<String, Object> map=new HashMap<String, Object>();
		String[] userinfo = WebCookie.getUser(request);
		int total=iOrderServer.getAddressCountByUserId(Integer.parseInt(userinfo[0]));
		if(total>=5){
			map.put("code", "1");
	        map.put("msg", "Only allows the addition of five recipient address");
	        return map;
		}
		address.setUserid(Integer.parseInt(userinfo[0]));
		address.setCreatetime(Utility.format(new Date(), Utility.datePattern1));
		int keys=iOrderServer.addAddress(address);
		iOrderServer.setDefault(keys, Integer.parseInt(userinfo[0]));
		if(keys == -1){
			map.put("code", "1");
	        map.put("msg", "Operation failed");
		}else{
			address.setId(keys);
			map.put("code", "0");
	        map.put("msg", "Successful operation");
	        map.put("bean", address);
	        map.put("total", total);
		}  
		return map;
	}
	
	@RequestMapping(value = "/checkAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkAddress(HttpServletRequest request, Address address) {
		Map<String, Object> map=new HashMap<String, Object>();
		String[] userinfo = WebCookie.getUser(request);
		List<Address> addresslist=iOrderServer.getUserAddr(Integer.parseInt(userinfo[0]));
		if(addresslist != null && addresslist.size()>0){
			map.put("code", "0");
			map.put("msg", "true");
		}else{
			map.put("code", "1");
			map.put("msg", "Please add the address");
		}
		return map;
	}
	
	@RequestMapping(value = "/setDefault", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setDefault(HttpServletRequest request, String id) {
		Map<String, Object> map=new HashMap<String, Object>();
		String[] userinfo = WebCookie.getUser(request);
		iOrderServer.setDefault(Integer.parseInt(id), Integer.parseInt(userinfo[0]));
		Address address=iOrderServer.getUserAddrById(Integer.parseInt(id));
        map.put("code", "0");
        map.put("msg", "Successful operation");
        map.put("bean", address);
		return map;
	}
	
	@RequestMapping(value = "/getAddressById", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAddressById(String id) {
		Map<String, Object> map=new HashMap<String, Object>();
		Address address=iOrderServer.getUserAddrById(Integer.parseInt(id));
		if(!"2".equals(address.getCountry()) && !"29".equals(address.getCountry()) && !"35".equals(address.getCountry())){
			map.put("flag", "0");
			IZoneServer zs = new ZoneServer();
			List<StateName> statelist =zs.getStateName();
			map.put("state", statelist);
		}else{
			map.put("flag", "1");
			List<String> list=Utility.getState(address.getCountry());
			map.put("state", list);
		}
		map.put("code", "0");
		map.put("msg", "Successful operation");
        map.put("bean", address);
		return map;
	}
	
	@RequestMapping(value = "/checkOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkOrder(HttpServletRequest request, String order_no, String payflag) {
		Map<String, Object> map=new HashMap<String, Object>();
		String[] userinfo = WebCookie.getUser(request);
		List<OrderBean> list=iOrderServer.getOrderInfo(Integer.parseInt(userinfo[0]), order_no);
		if(list != null && list.size()>0){
			map.put("code", "0");
			int state=list.get(0).getState();
			if(state == 5 || state == 3){
				if("Y".equals(payflag)){
					map.put("msg", "Payment completion");
					map.put("url", "/processesServlet?action=getCenter&className=IndividualServlet");	
				}else{
					map.put("msg", "Payment completion");
					map.put("url", "/processesServlet?action=getCtpoOrders&className=OrderInfo&orderNo="+order_no);	
				}					
			}else{
				map.put("msg", "Failed to pay");
				map.put("url", "/processesServlet?action=getOrders&className=OrderInfo&state=0&page=1");
			}
		}else{
			map.put("code", "1");
			map.put("msg", "Do not illegal operation");
		}
		return map;
	}
}