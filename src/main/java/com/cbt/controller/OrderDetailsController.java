package com.cbt.controller;

import com.cbt.bean.Preferential;
import com.cbt.bean.ShippingBean;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.processes.dao.IPreferentialDao;
import com.cbt.processes.dao.PreferentialDao;
import com.cbt.processes.service.IPreferentialServer;
import com.cbt.processes.service.PreferentialServer;
import com.cbt.processes.servlet.Goods;
import com.cbt.util.WebCookie;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/cbt/orderdetails")
public class OrderDetailsController {
	
	private static final Log LOG = LogFactory.getLog(OrderDetailsController.class);

	@RequestMapping(value = "/discountsView", method = RequestMethod.GET)
	public String discountsView(HttpServletRequest request, String totalPrice, String country,
                                String quantity, String email, String moq, String freeShipPrice,
                                String freeShipFlag, String shipDiscounts, HttpServletResponse response,
                                String singleweightmax, String size, String gurl, Map<String, Object> map, String types, String currency) {
		try {
			Preferential preferential = new Preferential();
			// 批量Discount
			String regEx = "[^0-9/.]";
			String goodsunit=moq.replaceAll("[^(A-Za-z)]", "");
			Pattern p = Pattern.compile(regEx);
			Matcher tpm = p.matcher(totalPrice);
			Matcher moqm = p.matcher(moq);
			String tpstr = tpm.replaceAll("").trim();
			double tpd = Double.parseDouble(tpstr);//总金额
			String moqstr = moqm.replaceAll("").trim();
			int moqInt = Integer.parseInt(moqstr);//最小订量
			int quantityInt = Integer.parseInt(quantity);//当前订量
			//shipDiscounts非免邮价格
			// 只有总金额>$200 而且 订量大于等于MOQ 3倍时， 才有 批量discount
			if (tpd > 200 && (quantityInt - moqInt * 3) >= 0) {
				String finalDiscount = "";
				int bulkDiscounts = 0;
				int flag = quantityInt / moqInt;
				if (flag > 3 && flag < 11) {
					bulkDiscounts = 5;
				} else if (flag > 10 && flag < 21) {
					bulkDiscounts = 10;
				} else if (flag > 20 && flag < 31) {
					bulkDiscounts = 15;
				} else if (flag > 30) {
					bulkDiscounts = 20;
				}

				if ("1".equals(freeShipFlag)) {
					//免邮
					float freeShipPriceFloat = Float.parseFloat(freeShipPrice);//免邮价格
					float sold = Float
							.parseFloat((Float.parseFloat(shipDiscounts) * (1 - (float) bulkDiscounts / 100))
									+ "");
					finalDiscount = (int)Math.floor(100 - (sold / freeShipPriceFloat* 100))+"";
				} else {
					//非免邮
					finalDiscount = bulkDiscounts + "";
				}
				String discountedUnitPrice=new BigDecimal(Float.parseFloat(freeShipPrice)*(100-Float.parseFloat(finalDiscount))/100).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
				String totalProductPrice=new BigDecimal(Float.parseFloat(discountedUnitPrice)*Float.parseFloat(quantity)).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
				preferential.setDiscountedUnitPrice(discountedUnitPrice);
				map.put("discountedUnitPrice", discountedUnitPrice);
				map.put("totalProductPrice", totalProductPrice);
				preferential.setTotalprice(totalProductPrice);
				map.put("finalDiscount", finalDiscount);
				preferential.setDiscount(finalDiscount+"%");
				map.put("discountflag", "p");
			} else {
				map.put("discountflag", "d");
			}
			IZoneServer izoneServer=new ZoneServer();
			float volume=1;
			String[] packagesizes=size.split("\\|");
			for(int i=0; i<packagesizes.length; i++){
				if(packagesizes[i] != null && !"undefined".equals(packagesizes[i]) && !"".equals(packagesizes[i]) ){
					volume=volume*Integer.parseInt(packagesizes[i]);
				}			
			}
			
			String ship="0";
			String name="";
			String day="";
			if( singleweightmax != null && !"undefined".equals(singleweightmax) && !"".equals(singleweightmax)){
				float singleweightmaxF=Float.parseFloat(singleweightmax);
				String weight=new BigDecimal(singleweightmaxF*quantityInt).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				List<ShippingBean> list=izoneServer.getShippingList(Integer.parseInt(country), Float.parseFloat(weight), new BigDecimal(volume*Float.parseFloat(quantity)/1000000).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue(), Float.parseFloat(singleweightmax), 16);
				
				if(list != null && list.size()>0){
					for(ShippingBean shippingBean : list){
						int id=shippingBean.getId();
						LOG.warn("---------------id:"+id);
						name=shippingBean.getName();
						LOG.warn("---------------name:"+name);
						double result=shippingBean.getResult();
						LOG.warn("---------------result:"+result);
						double result1=shippingBean.getResult1();
						LOG.warn("---------------result1:"+result1);
						day=shippingBean.getDays();
						LOG.warn("---------------day:"+day);
					}
					name=list.get(0).getName();
					day=list.get(0).getDays();
					ship=new BigDecimal(list.get(0).getResult()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				}	
			}				
			map.put("ship",ship);	
			map.put("name",name);	
			map.put("day",day);	
			map.put("email", email);
			map.put("country", country);
			map.put("freeShipPrice", freeShipPrice);
			map.put("goodsunit", goodsunit);
			int userid=0;
			String username="";
			String[] userinfos = WebCookie.getUser(request);
			String sessionId = null;
			if(userinfos != null){
				userid=Integer.parseInt(userinfos[0]);
				username=userinfos[1];
			}else{
			    sessionId = Goods.getSessionId(request, response);
			}
			preferential.setNumber(Integer.parseInt(quantity));
			preferential.setUserid(userid);
			preferential.setCountry(country);
			preferential.setEmail(email);
			preferential.setUsername(username);
			preferential.setNote("");
			preferential.setSessionid(sessionId);
			preferential.setpGoodsUnit(goodsunit);
			preferential.setShipping(ship);
			preferential.setSprice(Double.parseDouble(freeShipPrice));
			preferential.setGoods_types(types);
			preferential.setCurrency(currency);
//			int pid = preferentialServer.savePreferential2(preferential,gurl);
			map.put("preferential", JSONObject.fromObject(preferential));
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("discounts error",e);
			LOG.debug("discounts error url:"+gurl);
		}
		return "discountsView";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateDiscounts(int id, String remark) {
		Map<String, Object> map = new HashMap<String, Object>();
		IPreferentialDao dao = new PreferentialDao();
		int result=dao.updateRemark(id, remark);
		if(result == 1){
			map.put("code", "0");
			map.put("msg", "SUCCESS");
		}else{
			map.put("code", "1");
			map.put("msg", "FAIL");
		}		
		return map;
	}

	@RequestMapping(value = "/addDiscounts", method = RequestMethod.POST)
	@ResponseBody
	public int addDiscounts(String pre,String gurl) {
		 JSONObject jo = JSONObject.fromObject(pre);
		Preferential preferential = (Preferential) JSONObject.toBean(jo, Preferential.class);
		IPreferentialServer preferentialServer = new PreferentialServer();
		int pid = preferentialServer.savePreferential2(preferential,gurl);
		return pid;
	}
}