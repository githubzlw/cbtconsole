package com.cbt.controller;

import com.cbt.bean.*;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.pay.service.IOrderServer;
import com.cbt.pay.service.ISpidersServer;
import com.cbt.pay.service.SpidersServer;
import com.cbt.util.Md5Util;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(value = "/cbt/orderinfonew")
public class OrderInfoNewController {

	private static final Log LOG = LogFactory.getLog(OrderInfoNewController.class);
	@Autowired
	private IOrderServer iOrderServer;
	
	@RequestMapping(value = "/loginCart", method = RequestMethod.POST)
	public String login1(HttpServletRequest request, HttpServletResponse response, String itemId,
                         String flag, String sf, String express, String sum_w, String sum_v, String max_w, String express_e, String express_s, String sf_s, String sf_e, String country_id, String currency, String isCombine, String info, RedirectAttributesModelMap modelMap, Map<String, Object> map) {
		return this.login(request, response, itemId, flag, sf, express, sum_w, sum_v,max_w, express_e, express_s, sf_s,  sf_e,country_id, currency,isCombine, info, modelMap, map);
	}
	@RequestMapping(value = "/loginCart", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, String itemId,
                        String flag, String sf, String express, String sum_w, String sum_v, String max_w, String express_e, String express_s, String sf_s, String sf_e, String country_id, String currency, String isCombine, String info, RedirectAttributesModelMap modelMap, Map<String, Object> map) {
		String[] userinfo = WebCookie.getUser(request);
		 //获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		HttpSession session = request.getSession();
		if(userinfo == null){
			String exist = WebCookie.cookie(request, "userName");
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("itemId", itemId);
			maps.put("flag", flag);
			maps.put("sf", sf);
			maps.put("express", express);
			maps.put("sum_w", sum_w);
			maps.put("sum_v", sum_v);
			maps.put("max_w", max_w);
			maps.put("state", "1");
			maps.put("currency", currency1);
			if(Utility.getStringIsNull(country_id)){
				maps.put("countryId", country_id);
			}
			String reqs = itemId + sf + express + sum_w + sum_v + max_w;
			String md5 = Md5Util.encoder(reqs);
			Object ordersession = session.getAttribute(md5);
			if(ordersession != null){
				session.removeAttribute(md5);
			}
			session.setAttribute(md5,maps);
			session.setMaxInactiveInterval(3600);
			map.put("info", md5);
			if(exist == null){
				return "register";
			}
			return "geton";
		}else{
			if(!Utility.getStringIsNull(flag)){
				return "redirect:/processesServlet?action=getShopCar&className=Goods";
			}
			int flagInt = Integer.parseInt(flag);
			  if(flagInt==1){
				  Map<String, String> maps = new HashMap<String, String>();
				  if(!Utility.getStringIsNull(info)){
						maps.put("itemId", itemId);
						maps.put("flag", flag);
						maps.put("sf", sf);
						maps.put("express", express);
						maps.put("sum_w", sum_w);
						maps.put("sum_v", sum_v);
						maps.put("max_w", max_w);
						maps.put("state", "1");
						maps.put("currency", currency1);
						if(Utility.getStringIsNull(country_id)){
							maps.put("countryId", country_id);
						}
						String reqs = itemId + sf + express + sum_w + sum_v + max_w;
						String md5 = Md5Util.encoder(reqs);
						Object ordersession = session.getAttribute(md5);
						if(ordersession != null){
							session.removeAttribute(md5);
						}
						session.setAttribute(md5,maps);
						session.setMaxInactiveInterval(3600);
						info  = md5;
				  }else{
					  Object ordersession = session.getAttribute(info);
					  if(ordersession != null){
						  maps = (Map<String, String>) ordersession;
						  currency = maps.get("currency");
						  sf = maps.get("sf");
							 if(Utility.getStringIsNull(currency)){
								  if(Utility.getStringIsNull(sf)){
										//获取汇率
										int sf_ = (int)Math.rint((Double.parseDouble(sf)));
										sf = sf_ + "";
									}
							  }
							maps.put("sf", sf);
							maps.put("currency", currency1);
							session.setAttribute(info,maps);
							session.setMaxInactiveInterval(3600);
					  }
				  }
				  modelMap.addAttribute("action", "getSelectedItem");
				  modelMap.addAttribute("className", "RequireInfoServlet");
				  modelMap.addAttribute("info",info);
                  return "redirect:/paysServlet";
			  }else if(flagInt==0 || flagInt==3){
				   String express_o = "";
					Object ordersession = session.getAttribute(info);
					if(ordersession != null){
						Map<String, String> mapx = (Map<String, String>) session.getAttribute(info);
						itemId = mapx.get("itemId");
						currency = mapx.get("currency");
						if(flagInt == 0){
							int isCombine_ = Utility.getStringIsNull(isCombine)? Integer.parseInt(isCombine):0;
							int s = 0;
							if(Utility.getStringIsNull(express)){
								if(!express.equals(mapx.get("express"))){
									mapx.put("express", express);
									s = 1;
								}
								express_o = express;
							}else{
								express_o = mapx.get("express");
								express = mapx.get("express");
							}
							if(Utility.getStringIsNull(mapx.get("express_e"))){
								express_o = mapx.get("express_e");
							}
							if(Utility.getStringIsNull(mapx.get("express_s"))){
								express_o = mapx.get("express_s");
							}
							if(Utility.getStringIsNull(currency)){
								if(Utility.getStringIsNull(sf_e)){
									int sf_ = (int)Math.rint((Double.parseDouble(sf_e)));
									sf_e = sf_ + "";
								}
								if(Utility.getStringIsNull(sf_s)){
									int sf_ = (int)Math.rint((Double.parseDouble(sf_s)));
									sf_s = sf_ + "";
								}
								mapx.put("currency", currency1);
							}
							if(isCombine_ != 0){
								String string = mapx.get("itemid") ;
								boolean isnull = !Utility.getStringIsNull(string)|| string.equals("null");
								string = isnull ? "" : string;
								System.out.println((mapx.get("itemid") == null ? "" : mapx.get("itemid")));
								if(isCombine_ == 1 ){
									mapx.put("express_e", "");
									mapx.put("sf_e", "0");
									mapx.put("itemid", string+mapx.get("itemid_e"));
									mapx.put("itemid_e", null);
									if(isnull){
										mapx.put("express", express_e);
										mapx.put("sf", sf_e);
									}
								}else if(isCombine_ == 2 ){
									mapx.put("express_s", "");
									mapx.put("sf_s", "0");
									mapx.put("itemid", string+mapx.get("itemid_s"));
									mapx.put("itemid_s", null);
									if(isnull){
										mapx.put("express", express_s);
										mapx.put("sf", sf_s);
									}
								}else{
									mapx.put("express", express);
									mapx.put("express_e", "");
									mapx.put("sf_e", "0");
									mapx.put("express_s", "");
									mapx.put("sf_s", "0");
									mapx.put("itemid", string+mapx.get("itemid_e")+mapx.get("itemid_s"));
									mapx.put("itemid_e", null);
									mapx.put("itemid_s", null);
									mapx.put("sf", sf);
								}
								if(Utility.getIsDouble(sum_v)){
									mapx.put("sum_v", sum_v);
								}
								if(Utility.getIsDouble(sum_w)){
									mapx.put("sum_w", sum_v);
								}
								if(Utility.getIsDouble(max_w)){
									mapx.put("max_w", max_w);
								}
								s = 1;
							}else if(Utility.getStringIsNull(express_e)){
								mapx.put("express_e", express_e);
							}
							if(Utility.getStringIsNull(express_s)){
								mapx.put("express_s", express_s);
							}
							if(Utility.getIsDouble(sf)){
								double string = Double.parseDouble(sf);
								if(string != 0 && Double.parseDouble(sf) != Double.parseDouble(mapx.get("sf"))){
									mapx.put("sf", sf);
									s = 1;
								}
							}
							if(Utility.getIsDouble(sf_s) && isCombine_ == 0){
								if(Double.parseDouble(sf_s) != 0){
								mapx.put("sf_s", sf_s);
								s = 1;
								}
							}
							
							if(s == 1){
								String reqs = mapx.get("itemId") + sf + express + mapx.get("sum_w") + mapx.get("sum_v") + mapx.get("max_w");
								String md5 = Md5Util.encoder(reqs);
								Object ordersession1 = session.getAttribute(md5);
								if(ordersession1 == null){
									session.setAttribute(md5,mapx);
									session.removeAttribute(info);
									info = md5;
								}
								session.setMaxInactiveInterval(3600);
							}
						}else{
							express = mapx.get("express");
							sf = mapx.get("sf");
							sum_v = mapx.get("sum_v");
							sum_w = mapx.get("sum_w");
						}
					}else{
						return "redirect:/processesServlet?action=getShopCar&className=Goods";
					}
					//获取用户中的货币
					map.put("currency", Utility.getStringIsNull(currency1) ? currency1 : currency);
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
//					IUserServer userServer = new UserServer();
//					double balance = userServer.getBalance(userid)[0];
			        map.put("code", "0");
			        map.put("zonelist", zonelist);
			        map.put("addresslist", addresslist);
			        if(addresslist.size() > 0){
			        	map.put("defaultaddress", JSONObject.fromObject(addresslist.get(0)).toString().replaceAll("'", "&apos;"));
			        }
			        map.put("total", total);
//			        map.put("service_fee", service_fee);
			        map.put("addresssize", addresslist.size());
			        map.put("total", total);
			        map.put("size", addresslist.size());
//				    map.put("balance", balance);
//				    map.put("g_price", g_price);//折扣金额
				    map.put("itemId", itemId);
				    map.put("sf", sf);
				    map.put("express", express);
				    String[] express_ = express_o.split("@");
				    String country = null;
				    if(express_.length >= 3){
				    	country = express_o.split("@")[2];
				    	map.put("country", country);
				    }
				    else if(express != null && express.indexOf("@") > -1){
				    	country = express.substring(express.lastIndexOf("@")+1, express.length());
				    	map.put("country", country);
				    }
				    int isShowSelect = 0;
					List<StateName> statelist =zs.getStateName();
				    if(country != null){
			    		isShowSelect = 1;
			    		List<String> list = new ArrayList<String>();
				    	if(country.equals("USA EAST") || country.equals("USA WEST") || country.equals("USA")){
							for (int i = 0; i < statelist.size(); i++) {
								list.add(statelist.get(i).getStatename());
							}
				    	}else if(country.equals("AUSTRALIA")){
				    		list=Utility.getState("2");
				    	}else if(country.equals("S. AFRICA")){
				    		list=Utility.getState("29");
				    	}else if(country.equals("UK")){
				    		list=Utility.getState("35");
				    	}
				        map.put("statelist", list);
				    }else{
			    		isShowSelect = 1;
			    		List<String> list = new ArrayList<String>();
						for (int i = 0; i < statelist.size(); i++) {
							list.add(statelist.get(i).getStatename());
						}
				        map.put("statelist", list);
				    }
				    map.put("isShowSelect", isShowSelect);
				    map.put("statelistUSA", statelist);
				    map.put("info", info);
				    map.put("sum_w", Utility.getIsDouble(sum_w) ? Double.parseDouble(sum_w) : 0);
				    map.put("sum_v", sum_v);
			        return "address";
			  }else if(flagInt==2){
				  Object infos = session.getAttribute(info);
				  if(infos == null){
					  return "redirect:/processesServlet?action=getShopCar&className=Goods";
					}
					Map<String, String> mapx = (Map<String, String>) session.getAttribute(info);
					itemId = mapx.get("itemId");
					String reqs = mapx.get("itemId") + sf + express + mapx.get("sum_w") + mapx.get("sum_v") + mapx.get("max_w");
					String md5 = Md5Util.encoder(reqs);
					Object ordersession1 = session.getAttribute(md5);
					if(ordersession1 == null){
						session.setAttribute(md5,mapx);
						session.removeAttribute(info);
						info = md5;
					}
					session.setMaxInactiveInterval(3600);
					String itemid = mapx.get("itemId");
					String pay_express = mapx.get("express");
					sf = mapx.get("sf");
					//获取汇率
					currency = mapx.get("currency");
					if(Utility.getStringIsNull(currency)){
						
						if(Utility.getStringIsNull(sf_e)){
							int sf_ = (int)Math.rint((Double.parseDouble(sf_e)));
							sf_e = sf_ + "";
						}
						if(Utility.getStringIsNull(sf_s)){
							int sf_ = (int)Math.rint((Double.parseDouble(sf_s)));
							sf_s = sf_ + "";
						}
					}
					mapx.put("sf", sf);
					mapx.put("express", express);
					if(Utility.getStringIsNull(express_e)){
						mapx.put("express_e", express_e);
					}
					if(Utility.getStringIsNull(express_s)){
						mapx.put("express_s", express_s);
					}
					mapx.put("sf_e", sf_e);
					mapx.put("sf_s", sf_s);
					int state = Integer.parseInt(mapx.get("state"));
					int userid=Integer.parseInt(userinfo[0]);
					ISpidersServer is = new SpidersServer();
					List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
					if(userid!=0){
						spiderlist = is.getSelectedItem(userid, itemid,state);
					}else{
						LOG.warn("没有userid");
					}
					request.setAttribute("currency", currency1);
					request.setAttribute("itemid", itemid);
					request.setAttribute("spiderlist", spiderlist);
					request.setAttribute("pay_express", pay_express);
					request.setAttribute("sf", sf);
				  modelMap.addAttribute("action", "getSelectedItemrequiredinfo");
				  modelMap.addAttribute("className", "RequireInfoServlet");
				  modelMap.addAttribute("state", "1");
				  modelMap.addAttribute("info", md5);
				  modelMap.addAttribute("pay_express", express);
                  return "redirect:/paysServlet?info="+md5;
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
	public Map<String, Object> checkAddress(HttpServletRequest request) {
		Map<String, Object> map=new HashMap<String, Object>();
		String[] userinfo = WebCookie.getUser(request);
		int addresse=iOrderServer.existUserAddr(Integer.parseInt(userinfo[0]));
		if(addresse > 0){
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