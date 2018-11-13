package com.cbt.warehouse.ctrl;

import com.cbt.bean.OrderBean;
import com.cbt.bean.ShippingBean;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.cbt.website.util.JsonResult;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orderInfo")
public class NewOrderInfoCtr {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(NewOrderInfoCtr.class);
	@Autowired
	private IOrderinfoService iOrderinfoService;
	/**
	 * 根据订单号获取国家名称
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/queryCountryNameByOrderNo.do")
	@ResponseBody
	public JsonResult queryCountryNameByOrderNo(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			json.setOk(false);
			json.setMessage("获取订单号失败");
			return json;
		}
		String countryname = "";
		try {
			IOrderwsServer server = new OrderwsServer();
			countryname = server.queryCountryNameByOrderNo(orderNo);
			if (countryname.equals("USA EAST") || countryname.equals("USA WEST")) {
				countryname = "USA";
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("查询国家名称失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询国家名称失败,原因：" + e.getMessage());
		}
		json.setOk(true);
		json.setData(countryname);
		return json;

	}

	/**
	 * 采购页面查询订单支付金额等信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryOrderAmount")
	@ResponseBody
	public Map<String,String> queryOrderAmount(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> map=new HashMap<String,String>();
		DecimalFormat df = new DecimalFormat("#0.###");
		IOrderwsServer server = new OrderwsServer();
		try{
			String orderNo=request.getParameter("orderNo");
			if(StringUtil.isBlank(orderNo)){
				return map;
			}
			map=server.queryOrderAmount(orderNo);
			String allFreight = String.valueOf(server.getAllFreightByOrderid(orderNo));
			OrderBean orderInfo = iOrderinfoService.getOrders(orderNo);
			double freightFee =orderInfo.getFreightFee();
			//iOrderinfoService.getFreightFee(allFreight, orderInfo);
			map.put("allFreight",df.format(freightFee));
			List<ShippingBean> spb=iOrderinfoService.getShipPackmentInfo(orderNo);
			String actual_freight="654321";
			String awes_freight="0.00";
			Double actualFreight=0.00;
			Double awesFreight=0.00;
			double rate =Double.valueOf(orderInfo.getExchange_rate());
			if(spb.size()>0){
				for(ShippingBean s:spb){
					if(s != null && StringUtil.isNotBlank(s.getTransportcompany())){
						actualFreight += StringUtil.isBlank(s.getActual_freight())?0.00:Double.parseDouble(s.getActual_freight());
						awesFreight=awesFreight+Double.parseDouble(s.getEstimatefreight())*rate;
					}
				}
				actual_freight=df.format(actualFreight);
				awes_freight=df.format(awesFreight);
			}
			map.put("actual_freight",actual_freight);
			map.put("awes_freight",awes_freight);
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}


}
