package com.cbt.FreightFee.service.impl;

import com.cbt.FreightFee.dao.FreightFeeMapper;
import com.cbt.FreightFee.service.FreightFeeSerive;
import com.cbt.warehouse.pojo.TransitPricecost;
import com.cbt.warehouse.pojo.ZoneBean;
import com.cbt.warehouse.util.StringUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class FreightFeeServiceImpl implements FreightFeeSerive {

	@Autowired
	private FreightFeeMapper freightFeeMapper;
	

	@Override
	public Map getFreightFee(double weights_, double volume_,
			String countId, String shippingmethod, String subShippingmethod,
			String volume) {
			Map map=new HashMap();
		double fweight = weights_ > volume_ ? weights_ : volume_;
		double freightFee = 0;
		// 根据不同的运输方式计算不同的运费
		if (shippingmethod.equals("原飞航")) {
			if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>=0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+1;
			}else if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>0 && fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])<0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+0.5;
			}
			freightFee = getYFHFreightFee(fweight, countId, freightFee,subShippingmethod);
			if(freightFee<=0){
				//如果预估《=0，则国家使用南非来预估
				freightFee = getYFHFreightFee(fweight, "29", freightFee,subShippingmethod);
			}
		}else if (shippingmethod.equals("DHL")) {
			if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>=0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+1;
			}else if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>0 && fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])<0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+0.5;
			}
			if(freightFee<=0){
				//如果预估《=0，则国家使用南非来预估
				freightFee = getDHLFreightFee(fweight, "29", freightFee,subShippingmethod);
			}
		}else if(shippingmethod.equals("大誉") || shippingmethod.equals("迅邮")){
			if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>=0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+1;
			}else if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>0 && fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])<0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+0.5;
			}
			freightFee = getDYFreightFee(fweight, countId, freightFee,subShippingmethod);
		}else if (shippingmethod.toLowerCase().equals("jcex")) {
			if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>=0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+1;
			}else if(fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])>0 && fweight%Double.valueOf(String.valueOf(fweight).split("\\.")[0])<0.5){
				fweight=Double.valueOf(String.valueOf(fweight).split("\\.")[0])+0.5;
			}
			if(countId.contains("AFRICA")){
				countId="S.AFRICA";
			}
			ZoneBean zone = freightFeeMapper.getZone(countId.trim());
			if(zone == null){
				map.put("freightFee",0.00);
				return map;
//				return 0.00;
			}
			freightFee=FreightData.getFreight(zone.getChinapostbig(),String.valueOf(fweight));
			if(freightFee<=0){
				freightFee = getJcexFreightFee(fweight, countId, freightFee,shippingmethod);
			}
		}
		map.put("freightFee",freightFee);
		// CEN
		if (shippingmethod.toLowerCase().equals("epacket") || shippingmethod.toLowerCase().equals("emsinten")) {
			Map mapCne = getEpacketFreightFee(fweight, countId, freightFee,
					shippingmethod, subShippingmethod, volume);
			map.put("freightFee",mapCne.get("freightFee"));
			map.put("lowestPrice",mapCne.get("lowestPrice"));
			map.put("lowestPriceCom",mapCne.get("lowestPriceCom"));
		}
		return map;
//		return freightFee;
	}
	
//	public static void main(String[] args) {
//		double fweight_=211.00;
//		if(fweight_%Double.valueOf(String.valueOf(fweight_).split("\\.")[0])>=0.5){
//			fweight_=Double.valueOf(String.valueOf(fweight_).split("\\.")[0])+1;
//		}else if(fweight_%Double.valueOf(String.valueOf(fweight_).split("\\.")[0])>0 && fweight_%Double.valueOf(String.valueOf(fweight_).split("\\.")[0])<0.5){
//			fweight_=Double.valueOf(String.valueOf(fweight_).split("\\.")[0])+0.5;
//		}
//		System.out.println("=="+fweight_);
//	}


	/**
	 * 计算原飞航运费
	 *
	 * @param fweight
	 * @param countId
	 * @param freightFee
	 * @return
	 */
	public double getDYFreightFee(double fweight, String countId,
	                               double freightFee,String subShippingmethod) {
		if(countId.contains("AFRICA")){
			countId="S.AFRICA";
		}
		ZoneBean zone = freightFeeMapper.getZone(countId);
		if (zone != null && zone.getFedexie() != null) {
			String fedexie = zone.getFedexie();
			String country = zone.getCountry();
			// 运费 = 首重价 + （（总重量-整除重量） 整除 整除重量 +1） * 续重价
			// 墨西哥 150+40
			if ("MEXICO".equals(country)) {
				if (fweight <= 0.5) {
					freightFee = 150;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 150 + Math.ceil((fweight - 0.5) / 0.5) * 40;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}else if (fedexie.contains("加拿大")) {
				if (fweight <= 0.5) {
					freightFee = 120;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 120 + Math.ceil((fweight - 0.5) / 0.5) * 25;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}else{
				if (fweight <= 0.5) {
					freightFee = 100;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 100 + Math.ceil((fweight - 0.5) / 0.5) * 23;
				}
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 28;
				}
			}
		}
		return freightFee;
	}


	/**
	 * 计算DHL运费
	 *
	 * @param fweight
	 * @param countId
	 * @param freightFee
	 * @return
	 */
	public double getDHLFreightFee(double fweight, String countId,
	                               double freightFee,String subShippingmethod) {
		if(countId.contains("AFRICA")){
			countId="S.AFRICA";
		}
		ZoneBean zone = freightFeeMapper.getZone(countId);
		if (zone != null && zone.getFedexie() != null) {
			String fedexie = zone.getFedexie();//subShippingmethod;//
			String country = zone.getCountry();
			// 运费 = 首重价 + （（总重量-整除重量） 整除 整除重量 +1） * 续重价
			// 墨西哥 150+40
			if ("MEXICO".equals(country)) {
				if (fweight <= 0.5) {
					freightFee = 125;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 125 + Math.ceil((fweight - 0.5) / 0.5) * 35;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}else if("APAC".equals(country)){//亚太地区
				if (fweight <= 0.5) {
					freightFee = 125;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 125 + Math.ceil((fweight - 0.5) / 0.5) * 25;
				}
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
			// 美国,加拿大,西欧等国家运费计算公式 120+25
			if (fedexie.contains("西欧") || fedexie.contains("美国")
					|| fedexie.contains("加拿大")) {
				if (fweight <= 0.5) {
					freightFee = 130;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 130 + Math.ceil((fweight - 0.5) / 0.5) * 30;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 42;
				}
			}
			// 东欧国家 160+50
			if (fedexie.contains("东欧")) {
				if (fweight <= 0.5) {
					freightFee = 160;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 160 + Math.ceil((fweight - 0.5) / 0.5) * 50;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
			// 澳洲 125+30
			if (fedexie.contains("澳大利亚")) {
				if (fweight <= 0.5) {
					freightFee = 130;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 130 + Math.ceil((fweight - 0.5) / 0.5) * 30;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
			// 非洲国家 170+55
			if (fedexie.contains("非洲")) {
				if (fweight <= 0.5) {
					freightFee = 180;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 180 + Math.ceil((fweight - 0.5) / 0.5) * 55;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 65;
				}
			}
			// 南美国家 180+60
			if ("Honduras".equals(country)  || fedexie.contains("南美")) {
				if (fweight <= 0.5) {
					freightFee = 180;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 180 + Math.ceil((fweight - 0.5) / 0.5) * 55;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 65;
				}
			}
		}
		return freightFee;
	}
	

	/**
	 * 计算原飞航运费
	 * 
	 * @param fweight
	 * @param countId
	 * @param freightFee
	 * @return
	 */
	public double getYFHFreightFee(double fweight, String countId,
			double freightFee,String subShippingmethod) {
		if(countId.contains("AFRICA")){
			countId="S.AFRICA";
		}
		ZoneBean zone = freightFeeMapper.getZone(countId);
		if (zone != null && zone.getFedexie() != null) {
			String fedexie = zone.getFedexie();//subShippingmethod;//
			String country = zone.getCountry();
			// 运费 = 首重价 + （（总重量-整除重量） 整除 整除重量 +1） * 续重价
			// 墨西哥 150+40
			if ("MEXICO".equals(country)) {
				if (fweight <= 0.5) {
					freightFee = 125;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 125 + Math.ceil((fweight - 0.5) / 0.5) * 35;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}else if("APAC".equals(country)){//亚太地区
				if (fweight <= 0.5) {
					freightFee = 125;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 125 + Math.ceil((fweight - 0.5) / 0.5) * 25;
				}
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
			// 美国,加拿大,西欧等国家运费计算公式 120+25
			if (fedexie.contains("西欧") || fedexie.contains("美国")
					|| fedexie.contains("加拿大")) {
				if (fweight <= 0.5) {
					freightFee = 120;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 120 + Math.ceil((fweight - 0.5) / 0.5) * 25;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 38;
				}
			}
			// 东欧国家 160+50
			if (fedexie.contains("东欧")) {
				if (fweight <= 0.5) {
					freightFee = 160;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 160 + Math.ceil((fweight - 0.5) / 0.5) * 50;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
			// 澳洲 125+30
			if (fedexie.contains("澳大利亚")) {
				if (fweight <= 0.5) {
					freightFee = 105;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 105 + Math.ceil((fweight - 0.5) / 0.5) * 25;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
			// 非洲国家 170+55
			if (fedexie.contains("非洲")) {
				if (fweight <= 0.5) {
					freightFee = 155;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 155 + Math.ceil((fweight - 0.5) / 0.5) * 35;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
			// 南美国家 180+60
			if ("Honduras".equals(country) || country.contains("AFRICA") || fedexie.contains("南美")) {
				if (fweight <= 0.5) {
					freightFee = 150;
				}
				if (fweight > 0.5 && fweight < 21) {
					freightFee = 150 + Math.ceil((fweight - 0.5) / 0.5) * 40;
				}
				// 当重量超过21KG后的计算公式需求不明确
				if (fweight > 21) {
					freightFee = Math.ceil(fweight) * 45;
				}
			}
		}
		return freightFee;
	}

	public Map getEpacketFreightFee(double fweight, String countId,
			double freightFee, String shippingmethod, String subShippingmethod,
			String volume) {
		Map map=new HashMap();
		if(countId.contains("AFRICA")){
			countId="S.AFRICA";
		}
		ZoneBean zone = freightFeeMapper.getZone(countId);
		try {
			if (zone.getChinapostbig() != null && !"".equals(zone.getChinapostbig())) {
				final WebClient webClient = new WebClient(BrowserVersion.CHROME);
				webClient.getOptions().setCssEnabled(true);// 设置css是否生效
				webClient.getOptions().setJavaScriptEnabled(true);// 设置js是否生效
				webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 设置ajax请求
				webClient.getOptions().setTimeout(10000);
				webClient.waitForBackgroundJavaScript(3000);
				final HtmlPage page = webClient.getPage("http://www.cne.com/cnexx/13/d.htm");
				final HtmlForm form = page.getFormByName("PriceForm");
				final HtmlTextInput code = form.getInputByName("cdes");
				code.setValueAttribute(zone.getChinapostbig());
				final HtmlTextInput fw = form.getInputByName("fweight");
				fw.setValueAttribute(String.valueOf(fweight));
				if (volume.indexOf("*") > -1) {
					String[] vos = volume.split("\\*");
					for (int i = 0; i < vos.length; i++) {
						if (i == 0) {
							final HtmlTextInput il = form.getInputByName("il");
							il.setValueAttribute(String.valueOf(vos[0]));
						} else if (i == 1) {
							final HtmlTextInput iw = form.getInputByName("iw");
							iw.setValueAttribute(String.valueOf(vos[1]));
						} else if (i == 2) {
							final HtmlTextInput ih = form.getInputByName("ih");
							ih.setValueAttribute(String.valueOf(vos[2]));
						}
					}
				}
				HtmlButton btnSubmit = null;
				HtmlPage new_page = null;
				DomNodeList<DomElement> domElements = page
						.getElementsByTagName("button");
				for (DomElement temp : domElements) {
					if (temp.getAttribute("class").equals("btn1_mouseout")) {
						btnSubmit = (HtmlButton) temp;
						new_page = btnSubmit.click();
						break;
					}
				}
				if (new_page != null) {
					try{
						HtmlTable table = new_page.getHtmlElementById("opTable");
						int i = 1;
						int j = 1;
						double lowestPrice=9999.99;
						for (HtmlTableRow row : table.getRows()) { // 遍历所有行
							String name = "";
							double value = 0.00;
							String bg = row.getAttribute("bgcolor");
							if ("#FFFFFF".equals(bg)) {
								for (HtmlTableCell cell : row.getCells()) { // 遍历所有列
									if (j == 1) {
										name = cell.asText();
									} else if (j == 11) {
										value = Double.valueOf(cell.asText()
												.replaceAll("￥", ""));
									}
									j = j + 1;
								}
								if(StringUtil.isNotBlank(subShippingmethod) && name.equals(subShippingmethod)){
									freightFee=value;
									map.put("lowestPrice",0.00);
									map.put("lowestPriceCom","");
									break;
								}else{
									if(!name.contains("海运") && lowestPrice>value){
										lowestPrice=value;
										map.put("lowestPrice",lowestPrice);
										map.put("lowestPriceCom",name);
									}
								}
								i = i + 1;
								j = 1;
							} else {
								continue;
							}
						}
						i = 1;
					}catch(Exception e){
						freightFee=0.00;
						map.put("lowestPrice",0.00);
						map.put("lowestPriceCom","");
					}
				}
				///webClient.closeAllWindows();
			}
		} catch (Exception e) {
			/*e.printStackTrace();*/
			log.error("获取预估运费错误,shippingMehtod:{},e.message:{[]}",shippingmethod,e.getMessage());
			freightFee=0.00;
		}
		map.put("freightFee",freightFee);
		return map;
//		return freightFee;
	}

	/**
	 * 获取JCEX 运费
	 * 
	 * @param fweight
	 * @param countId
	 * @param freightFee
	 * @param shippingmethod
	 * @return
	 */
	public double getJcexFreightFee(double fweight, String countId,
			double freightFee, String shippingmethod) {
		try{
			if("37".equals(countId)){
				countId="29";
			}else if("43".equals(countId)){
				countId="36";
			}else if(countId.contains("AFRICA")){
				countId="29";
			}
			List<TransitPricecost> transitPricecosts = freightFeeMapper.selectTransitInfo(countId,shippingmethod);
			TransitPricecost transitBean = new TransitPricecost();
			for (TransitPricecost bean : transitPricecosts) {
				if (fweight > bean.getMinweight()) {
					transitBean = bean;
				}
			}
			if (transitPricecosts.size() > 0) {
				// 运费 = 首重价 + （（总重量-整除重量） 整除 整除重量 +1） * 续重价  92+9*34
				if (transitBean.getMinweight() == 0 && transitBean.getMaxweight() == 21) {
					freightFee = transitBean.getUnder()
							+ Math.ceil((fweight - transitBean.getDivisionweight())
							/ transitBean.getDivisionweight())
							* transitBean.getOver();
				} else if(transitBean.getMinweight() == 21){
//				freightFee = Math
//						.ceil((fweight - transitBean.getDivisionweight())
//								/ transitBean.getDivisionweight())
//						* transitBean.getOver();
					freightFee = Math.ceil(fweight)* transitBean.getOver();
				}
			}
		}catch (Exception e){
			freightFee=0.00;
		}
		return freightFee;
	}

	@Override
	public List<Map<String, Object>> getShippingInfo() {
		// TODO Auto-generated method stub
		return freightFeeMapper.getShippingInfo();
	}

	@Override
	public List<Map<String, Object>> getShippingCostInfo() {
		return freightFeeMapper.getShippingCostInfo();
	}

	@Override
	public int updatePackCost(String id, double cost, String company) {
		return freightFeeMapper.updatePackCost(id,cost,company);
	}

	@Override
	public void updateFreightByexpressno(double freightFee, String expressno,String id,String subShippingmethod,String shippingmethod) {

		freightFeeMapper.updateFreightByexpressno(freightFee, expressno,id,subShippingmethod,shippingmethod);
	}

	@Override
	public double getOrderRate(String sp_id) {
		return Double.valueOf(freightFeeMapper.getOrderRate(sp_id));
	}
}
