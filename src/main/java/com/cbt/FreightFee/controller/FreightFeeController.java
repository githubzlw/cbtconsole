package com.cbt.FreightFee.controller;

import com.cbt.FreightFee.service.FreightFeeSerive;
import com.cbt.util.Util;
import com.cbt.warehouse.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 后台运费计算
 * @author admin
 *
 */
@Controller
@RequestMapping("freightFeeController")
public class FreightFeeController {
	DecimalFormat df = new DecimalFormat("0.00");
	@Autowired
	private FreightFeeSerive freightFeeSerive;

	//	@RequestMapping("getOrderFreight")
	@RequestMapping(value = "/getOrderFreight", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getFreightFee(HttpServletRequest request, HttpServletResponse response) {
		double freightFee=0.00;
		try{
			String weight = request.getParameter("weight");          //重量
			String volumn = request.getParameter("volume");           //体积
			String countId = request.getParameter("countryid");         //国家
			String id=request.getParameter("sp_id");
			if ("43".equals(countId)) {
				countId = "36";
			}
			String shippingmethod = request.getParameter("transport"); //运输方式
			String subShippingmethod = request.getParameter("subShippingmethod"); //子运输方式
			subShippingmethod=StringUtil.isBlank(subShippingmethod)?"":subShippingmethod;
			String volumes = request.getParameter("volumes");//体积  20*20*50
			if ("SF".equals(shippingmethod)) {
				shippingmethod = "JCEX";
			}
			//原飞航=
			double weights_ = Double.parseDouble(weight == null || "".equals(weight) ? "0.00" : weight);
			double volume_ = volumn == null ? 0 : Double.parseDouble(volumn == null || "undefined".equals(volumn) ? "0.00" : volumn);
			Map freightFeeMap = freightFeeSerive.getFreightFee(weights_, volume_, countId, shippingmethod, subShippingmethod, volumes);
			freightFee=Double.valueOf(freightFeeMap.get("freightFee").toString());
			if (freightFee > 0) {
				//获取订单实时汇率
				double rate=Double.parseDouble(String.valueOf(Util.EXCHANGE_RATE));
				double fweight = weights_ > volume_ ? weights_ : volume_;
				if (fweight % Double.valueOf(String.valueOf(fweight).split("\\.")[0]) >= 0.5) {
					fweight = Double.valueOf(String.valueOf(fweight).split("\\.")[0]) + 1;
				} else if (fweight % Double.valueOf(String.valueOf(fweight).split("\\.")[0]) > 0 && fweight % Double.valueOf(String.valueOf(fweight).split("\\.")[0]) < 0.5) {
					fweight = Double.valueOf(String.valueOf(fweight).split("\\.")[0]) + 0.5;
				}
				if (fweight > 150) {
					freightFee = freightFee - fweight * 5;
				}
				freightFee = freightFee / rate;
				DecimalFormat df = new DecimalFormat("######0.00");
				freightFee = Double.valueOf(df.format(freightFee));
			}else{
				freightFee=0.00;
			}
			if(shippingmethod.toLowerCase().equals("epacket") || shippingmethod.toLowerCase().equals("emsinten")){
				shippingmethod="emsinten";
			}
			freightFeeSerive.updateFreightByexpressno(freightFee, "",id,subShippingmethod,shippingmethod);
		}catch (Exception e){
			freightFee=0.00;
		}
		return df.format(freightFee);
	}

	@RequestMapping("updateFee")
	public String updateFee(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = freightFeeSerive.getShippingInfo();
//		List<Map<String,Object>>   list = new ArrayList<Map<String,Object>>();
		String weight = "";          //重量
		String volumn = "";           //体积
		String svolumn = "";           //体积
		String countId = "";         //国家
		String shippingmethod = ""; //运输方式
		String subShippingmethod = "";
		String expressno = "";
//		double freightFee =  freightFeeSerive.getFreightFee(2.00,0.002,"美国","Epacket","CNE全球特惠","20*20*20");
		for (Map<String, Object> map : list) {
			weight = map.get("sweight") == null ?"":map.get("sweight").toString();
			volumn = map.get("volumeweight") == null || map.get("volumeweight").toString() == "null" ? "0" : map.get("volumeweight").toString();
			svolumn = map.get("svolume") == null?"":map.get("svolume").toString();//体积
			countId = map.get("country")==null ?"": map.get("country").toString();
			shippingmethod = map.get("transportcompany").toString();
			if ("SF".equals(shippingmethod)) {
				shippingmethod = "JCEX";
			} else if ("大誉".equals(shippingmethod)) {
				shippingmethod = "原飞航";
			}
			subShippingmethod = map.get("shippingtype") == null ? "" : map.get("shippingtype").toString();
			expressno = map.get("expressno").toString();
			if (weight == "" || volumn == "") {
				continue;
			}
			String id=map.get("id").toString();
			double rate=Double.parseDouble(String.valueOf(Util.EXCHANGE_RATE));
			double weights_ = Double.parseDouble(weight);//实重
			double volume_ = Double.parseDouble(volumn);//抛重
			//获取新的运费
			Map freightFeeMap = freightFeeSerive.getFreightFee(weights_, volume_, countId, shippingmethod, subShippingmethod, svolumn);
			double freightFee=Double.valueOf(freightFeeMap.get("freightFee").toString());
			//将计算的运费更新到出库表
			if (freightFee > 0) {
				freightFee = freightFee /rate;
				DecimalFormat df = new DecimalFormat("######0.00");
				freightFee = Double.valueOf(df.format(freightFee));
				System.out.println(map.get("id") + "=================" + freightFee);
				freightFeeSerive.updateFreightByexpressno(freightFee, expressno,map.get("id").toString(),subShippingmethod,shippingmethod);
			}
		}
		System.out.println("更新完成=============");
		return null;
	}

	public double getOrderRate(String sp_id){
		return  freightFeeSerive.getOrderRate(sp_id);
	}

	public static void main(String[] args) {
		sss(null,null);
	}

	@RequestMapping("updateFeeCost")
	public String updateFeeCost(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = freightFeeSerive.getShippingCostInfo();
		//获取所有没有最优运费的订单
		try{
			for (Map<String, Object> map : list) {
				if( map.get("sweight") ==null ||  map.get("volumeweight") ==null ||  map.get("svolume") ==null ||  map.get("country") ==null){
					continue;
				}
				String weight = map.get("sweight").toString();
				String volumn = map.get("volumeweight") == null || map.get("volumeweight").toString() == "null" ? "0" : map.get("volumeweight").toString();
				String svolumn = map.get("svolume").toString();//体积
				Object countIds = map.get("country");
				String countId = countIds.toString();
				String shippingmethod =map.get("transportcompany")==null?"":map.get("transportcompany").toString();
				String subShippingmethod = map.get("shippingtype") == null ? "" : map.get("shippingtype").toString();
				String expressno = map.get("expressno").toString();
				if (StringUtil.isBlank(weight) || StringUtil.isBlank(volumn)) {
					continue;
				}
				String id=map.get("id").toString();
				double rate=Double.parseDouble(String.valueOf(Util.EXCHANGE_RATE));
				double weights_ = Double.parseDouble(weight);//实重
				double volume_ = Double.parseDouble(volumn);//抛重
				System.out.println("id======"+map.get("id").toString());
				//邮政
				Map freightEpacketMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "epacket", "", svolumn);
				double freightEpacket=99999.99;
				if(freightEpacketMap.get("lowestPrice")!=null && Double.valueOf(freightEpacketMap.get("lowestPrice").toString())>0 && StringUtil.isNotBlank(freightEpacketMap.get("lowestPriceCom").toString())){
					freightEpacket=Double.valueOf(freightEpacketMap.get("lowestPrice").toString());
				}
				//jcex
				Map freightJcexMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "jcex", subShippingmethod, svolumn);
				double freightJcex=Double.valueOf(freightJcexMap.get("freightFee").toString());
				if(freightJcex<=0){
					freightJcex=99999.99;
				}
				//原飞航
				Map freightYfhMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "原飞航", subShippingmethod, svolumn);
				double freightYfh=Double.valueOf(freightYfhMap.get("freightFee").toString());
				if(freightYfh<=0){
					freightYfh=99999.99;
				}
				double optimal_cost=0.00;
				String optimal_company="";
				if(freightEpacket<freightJcex && freightEpacket<freightYfh){
					optimal_cost=freightEpacket;
					optimal_company="邮政"+freightEpacketMap.get("lowestPriceCom");
				}else if(freightJcex<freightEpacket && freightJcex<freightYfh){
					optimal_cost=freightJcex;
					optimal_company="JCEX";
				}else if(freightYfh<freightEpacket && freightYfh<freightJcex){
					optimal_cost=freightYfh;
					optimal_company="原飞航";
				}else{
					continue;
				}
				//update shipping_package set optimal_cost="+optimal_cost+",optimal_company='"+optimal_company+"' where id="+rs.getString("id")+"
				System.out.println("最优价格：optimal_cost="+optimal_cost+"========= 最优物流公司="+optimal_company);
				if(optimal_cost<=0 || StringUtil.isBlank(optimal_company)){
					continue;
				}
				freightFeeSerive.updatePackCost(map.get("id").toString(),optimal_cost,optimal_company);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}

//	@RequestMapping("sss")
	public static String sss(HttpServletRequest request, HttpServletResponse response) {
		String expressno = "";
		try {
//            String data="{\"Token\":\"39E74D0D-3ACF-6C38-A9DD-AE5A65DFE544\",\n" + "\"UAccount\":\"33150\",\n"
//                    + "\"Password\":\"31B68D81E2A8824C65D552EAAF116D2A\",\n" + "\"OrderList\":[{\"Address1\":\""+map.get("Address1")+"\",\n"
//                    + "\"Address2\":\"\",\n" + "\"ApiOrderId\":\""+map.get("ApiOrderId")+"\",\n" + "\"City\":\""+map.get("City")+"\",\n" + "\"CiId\":\""+map.get("CiId")+"\",\n"
//                    + "\"County\":\""+map.get("County")+"\",\n" + "\"CCode\":\"USD\",\n" + "\"Email\":\"\",\n" + "\"PackType\":\"3\",\n" + "\"Phone\":\""+map.get("Phone")+"\",\n"
//                    + "\"PtId\":\"THAMR\",\n" + "\"ReceiverName\":\""+map.get("ReceiverName")+"\",\n" + "\"SalesPlatformFlag\":\"0\",\n"
//                    + "\"SyncPlatformFlag\":\"flyt.logistics.import-express\",\n" + "\"Zip\":\""+map.get("Zip")+"\",\n"
//                    + "\"OrderDetailList\":[\n" + "{\"ItemName\":\"\",\n" + "\"Price\":\""+map.get("Price")+"\",\n"
//                    + "\"Quantities\":\""+map.get("Quantities_i")+"\",\n" + "\"Sku\":\""+map.get("Sku")+"\"}],\n" + "\"HaikwanDetialList\":[\n"
//                    + "{\"HwCode\":\"\",\n" + "\"ItemCnName\":\"商品\",\n" + "\"ItemEnName\":\""+map.get("ItemEnName")+"\",\n"
//                    + "\"ItemId\":\"\",\n" + "\"ProducingArea\":\"CN\",\n" + "\"Quantities\":\""+map.get("Quantities")+"\",\n" + "\"UnitPrice\":\""+map.get("UnitPrice")+"\",\n"
//                    + "\"Weight\":\""+map.get("")+"\",\n" + "\"BtId\":\"\",\n" + "\"CCode\":\"USD\",\n"
//                    + "\"Purpose\":\"\",\n" + "\"Material\":\"\"}\n" + "]}]}";
			String data = "{\"Token\":\"39E74D0D-3ACF-6C38-A9DD-AE5A65DFE544\",\n" + "\"UAccount\":\"33150\",\n" + "\"Password\":\"31B68D81E2A8824C65D552EAAF116D2A\",\n" + "\"OrderList\":[{\"Address1\":\"0,104 Bridge Street,\",\n" + "\"Address2\":\"\",\n" + "\"ApiOrderId\":\"Q62613158536050811\",\n" + "\"City\":\"KORUMBURRA,\",\n" + "\"CiId\":\"AU\",\n" + "\"County\":\"AU\",\n" + "\"CCode\":\"USD\",\n" + "\"Email\":\"\",\n" + "\"PackType\":\"3\",\n" + "\"Phone\":\"0499198783\",\n" + "\"PtId\":\"THAMR\",\n" + "\"ReceiverName\":\"luschani99@gmail.com\",\n" + "\"SalesPlatformFlag\":\"0\",\n" + "\"SyncPlatformFlag\":\"flyt.logistics.import-express\",\n" + "\"Zip\":\"3950\",\n" + "\"OrderDetailList\":[\n" + "{\"ItemName\":\"goods\",\n" + "\"Price\":\"20\",\n" + "\"Quantities\":\"5\",\n" + "\"Sku\":\"\"}],\n" + "\"HaikwanDetialList\":[\n" + "{\"HwCode\":\"\",\n" + "\"ItemCnName\":\"商品\",\n" + "\"ItemEnName\":\"made in china\",\n" + "\"ItemId\":\"\",\n" + "\"ProducingArea\":\"CN\",\n" + "\"Quantities\":\"5\",\n" + "\"UnitPrice\":\"20\",\n" + "\"Weight\":\"\",\n" + "\"BtId\":\"\",\n" + "\"CCode\":\"USD\",\n" + "\"Purpose\":\"\",\n" + "\"Material\":\"\"}\n" + "]}]}";
//			String data="{\"Token\":\"39E74D0D-3ACF-6C38-A9DD-AE5A65DFE544\",\"UAccount\":\"33150\",\"Password\":\"31B68D81E2A8824C65D552EAAF116D2A\",\"OrderList\":{\"Address1\":\"0,104 Bridge Street,\",\"Address2\":\"\",\"ApiOrderId\":\"Q626131585360508\",\"City\":\"KORUMBURRA\",\"CiId\":\"AU\",\"County\":\"AU\",\"CCode\":\"USD\",\"Email\":\"\",\"PackType\":\"3\",\"Phone\":\"0499198783\",\"PtId\":\"THAMR\",\"ReceiverName\":\"luschani99@gmail.com\",\"SalesPlatformFlag\":\"0\",\"SyncPlatformFlag\":\"flyt.logistics.import-express\",\"Zip\":\"3950\"},\"OrderDetailList\":{\"ItemName\":\"goods\",\"Price\":\"20\",\"Quantities\":\"5\",\"Sku\":\"\"},\"HaikwanDetialList\":{\"HwCode\":\"\",\"ItemCnName\":\"%E5%95%86%E5%93%81\",\"ItemEnName\":\"made in china\",\"ItemId\":\"\",\"ProducingArea\":\"CN\",\"Quantities\":\"5\",\"UnitPrice\":\"20\",\"Weight\":\"\",\"BtId\":\"\",\"CCode\":\"USD\",\"Purpose\":\"CN\",\"Material\":\"5\"}}";
			String path = "http://exorderwebapi.flytcloud.com/api/OrderSyn/ErpUploadOrder";
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String str = br.readLine();
			System.out.println("data=" + data);
			System.out.println("响应内容为:  " + str);
			JSONObject json = JSONObject.fromObject(str);
			String Success = json.getString("Success");
			if (StringUtil.isNotBlank(Success) && "true".equals(Success)) {
				System.out.println("上传飞特订单接口成功");
				String ErpSuccessOrders = json.getString("ErpSuccessOrders").replace("[", "").replace("]", "");
				json = JSONObject.fromObject(ErpSuccessOrders);
				expressno = json.get("TraceId").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("expressno===" + expressno);
		return expressno;
	}
}
