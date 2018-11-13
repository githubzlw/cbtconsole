package com.cbt.warehouse.service;

import com.cbt.bean.SearchGoods;
import com.cbt.bean.ShippingBean;
import com.cbt.util.ShipComparator;
import com.cbt.util.Util;
import com.cbt.warehouse.dao.ZoneMapper;
import com.cbt.warehouse.pojo.*;
import com.cbt.warehouse.util.Utility;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Component
@Service
public class ZoneShippingServiceImpl implements ZoneShippingService {
	
	@Autowired
	private ZoneMapper zoneMapper;

	@Autowired
	private IZoneInfoService zoneInfoService;
//	private final double usa_et = 6.75; //美元汇率 
	

	@Autowired
	private HttpServletRequest request;
	
	@Override
	public float getCost(int countryid, float weight, int type, float volume,
			float singleweightmax) {
		String shiptype="";//zone表的列名,对应不同运费方式的表名
		String colname="";
		String name="";//对应国家在不同运输方式下的值
		float result=0;//运费计算结果
		float volumeweight=0;
		float tempweight=0;//临时存储变量
		
		if(type==9){//EMS运费方式的体积重计算方式:volume/8000,其他的:volume/5000
			volumeweight=volume*125;
		}else{
			volumeweight=volume*200;
		}
		
		boolean flag=true;//如果从zone表中取值为0,则将其值置为false;
		if(volumeweight>weight){
			weight=volumeweight;
		}
		
		List<String> shipTypes = null;
		List<Float> fedexies = null;
		switch(type){
		case 1://运费方式为fedexie
			shiptype="fedexie";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(name.equals("西欧")){
				colname="westeurope";
			}else if(name.equals("东欧")){
				colname="easteurope";
			}else if(name.equals("南美")){
				colname="southamerica";
			}else if(name.equals("澳大利亚")){
				colname="australia";
			}else if(name.equals("美国东岸")){
				colname="eastusa";
			}else if(name.equals("日本")){
				colname="japan";
			}else if(name.equals("加拿大墨西哥")){
				colname="canadamexico";
			}else if (name.equals("非洲")){
				colname="africa";
			}else if(name.equals("美国西岸")){
				colname="westusa";
			}
			else if(name.equals("0")){
				flag=false;
			}
			//重量在0-0.5或者在0.5-1之间
			if(flag){
				if(weight<=20.5){
					double temp=Math.floor(weight);
					if((weight-temp)<0.5&&(weight-temp)>0){
						weight=(float) (Math.floor(weight)+0.5);
					}else if((weight-temp)>0.5){
						weight=(float)(Math.ceil(weight));
					}
					
					fedexies = zoneMapper.getFedexiePrice(weight, colname, shiptype);
					result=(float) (fedexies.get(fedexies.size()-1)*0.16*0.9);
				}else if(weight>20.5&&weight<=44){
					fedexies = zoneMapper.getFedexiePrice(44, colname, shiptype);
					result=(float) (fedexies.get(fedexies.size()-1)*weight*0.16*0.9);
				}else if(weight>44&&weight<=70){
					fedexies = zoneMapper.getFedexiePrice(70, colname, shiptype);
					result=(float) (fedexies.get(fedexies.size()-1)*weight*0.16*0.9);
				}else if(weight>70&&weight<=99){
					fedexies = zoneMapper.getFedexiePrice(99, colname, shiptype);
					result=(float) (fedexies.get(fedexies.size()-1)*weight*0.16*0.9);
				}else if(weight>99&&weight<=299){
					fedexies = zoneMapper.getFedexiePrice(299, colname, shiptype);
					result=(float) (fedexies.get(fedexies.size()-1)*weight*0.16*0.9);
				}else if(weight>299){
					fedexies = zoneMapper.getFedexiePrice(300, colname, shiptype);
					result=(float) (fedexies.get(fedexies.size()-1)*weight*0.16*0.9);
				}
			}

			break;
		case 2://运费方式为epacket
			shiptype="epacket";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> epacketPriceMap = zoneMapper.getEpacketPrice(name);
				if(weight<0.06){
					weight=0.06f;
				}
			   result=(epacketPriceMap.get(epacketPriceMap.size()-1).get("fee1")*weight*1000+epacketPriceMap.get(epacketPriceMap.size()-1).get("fee2"))*0.16f;
			}
			
			break;
		case 3://运费方式为dhlfba
			shiptype="dhlfba";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
//			System.out.println(name);
			if(!name.equals("0")){
				List<Map<String, Float>> dhlfbaList = zoneMapper.getDhlfbaPrice();
				
				float weight1=dhlfbaList.get(dhlfbaList.size()-1).get("weight1");
				float weight2=dhlfbaList.get(dhlfbaList.size()-1).get("weight2");
				float fee1=dhlfbaList.get(dhlfbaList.size()-1).get("fee1");
				float fee2=dhlfbaList.get(dhlfbaList.size()-1).get("fee2");
				
				float dhlfba = 0;
				if((weight>weight1&&weight<=weight2)&&weight2<=500&&weight<20){
					dhlfba=(float) (fee1+fee2*((weight-0.5)/0.5));
				}else if(weight>=20){
					dhlfba=weight*fee2;
				}
				
				result=dhlfba*0.16f;	
			}
			break;
		case 4://运费方式为china post sal
			shiptype="chinapostbig";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> chinapostbigList = zoneMapper.getChinapostsalPrice(name);
				float under=chinapostbigList.get(chinapostbigList.size()-1).get("under");
				float over=chinapostbigList.get(chinapostbigList.size()-1).get("over");
				float r=under+((weight-1)>0?(weight-1):0)*over;
				result=r*0.16f;
			}
			break;
		case 5://运费方式为china post air
			shiptype="chinapostbig";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> chinapostbigList = zoneMapper.getChinapostsurfacePrice(name);
				float under=chinapostbigList.get(chinapostbigList.size()-1).get("under");
				float over=chinapostbigList.get(chinapostbigList.size()-1).get("over");
				float r=under+((weight-1)>0?(weight-1):0)*over;
				result=r*0.16f;
			}
			break;
		case 6://运费方式为Sweden瑞典邮政小包
			shiptype="sweden";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Integer>> swedenList = zoneMapper.getSweden(name);
				int price=swedenList.get(swedenList.size()-1).get("price");
				int guahao=swedenList.get(swedenList.size()-1).get("guahao");
				float r=price*weight+guahao;
				result=r*0.16f;
			}
			break;
		case 7://运费方式为TNT荷兰小包价格,限重： 2KG
			shiptype="tnt";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> tntList = zoneMapper.getTNT(name);
				float price=tntList.get(tntList.size()-1).get("price");
				float guahao=tntList.get(tntList.size()-1).get("guahao");
				float r=price*weight+guahao;
				result=r*0.16f;
			}
			break;
		case 8://运费方式为KYD 国际小包
			shiptype="kyd";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> kydList = zoneMapper.getKyd(name);
				float fee1=kydList.get(kydList.size()-1).get("fee1");
				float fee2=kydList.get(kydList.size()-1).get("fee2");
				/*
				 * 标准价计算公式=（重量-0.05）*续重+首重 （最低收费50g，1g为一个单位）                                                                                            
				 *  例如：200g巴西，                                                                                                                               
				 *  1：标准价=（0.2-0.05）*120+5  （最低收费5元）     
				 *  2：运费=（标准价+8）*折扣
				*/
				float r=(float) ((fee2*1000*(weight-0.05)+fee1+8)*0.85);
				result=r*0.16f;
			}
			break;
		case 9://运费方式为EMS
			shiptype="ems";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> emsList = zoneMapper.getEMS(name);
				float fee1=emsList.get(emsList.size()-1).get("fee1");//首重0.5kg费用，不足0.5kg按0.5kg计算
				float fee2=emsList.get(emsList.size()-1).get("fee2");//续重0.5kg费用
				float r;
				if(weight<=0.5){
					r=fee1;
				}else{
					r=(float) (fee1+Math.ceil((weight-0.5)/0.5)*fee2);
				}
				result=r*0.16f;
			}
			break;
		case 10://运费方式为E特快
			shiptype="efast";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> emsList = zoneMapper.getEfast(name);
				float fee1=emsList.get(emsList.size()-1).get("fee1");//首重0.05kg费用，不足0.05kg按0.05kg计算
				float fee2=emsList.get(emsList.size()-1).get("fee2");//续重0.05kg费用
				float r;
				if(weight<=0.05){
					r=fee1;
				}else{
					r=(float) (fee1+Math.ceil((weight-0.05)/0.05)*fee2);
				}
				result=r*0.16f;
			}
			break;
		case 11://运费方式为FedexGround
			shiptype="fedexground";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			tempweight=weight/0.45f;
			if(!name.equals("0")){
				if(tempweight<=150){
					List<Float> fedexgroundList = zoneMapper.getFedexground(name, (float)Math.ceil(tempweight));
					result=fedexgroundList.get(fedexgroundList.size()-1)+60+weight/1000*50;
				}else if(tempweight>150&&(singleweightmax/0.45)<150){
					//处理当总重量>150Lb, 而单件 最大重量<150Lb 时,重量单位是lb
					int i=(int) Math.floor((weight/0.45)/150);
					List<Float> fedexgroundList1 = zoneMapper.getFedexground(name, 150);
					List<Float> fedexgroundList2 = zoneMapper.getFedexground(name, (float)Math.ceil(tempweight)-i*150);
					
					result=(fedexgroundList1.get(fedexgroundList1.size()-1)+60+weight/1000*50)*i + (fedexgroundList2.get(fedexgroundList2.size()-1)+60+weight/1000*50);
				}
			}
			break;
		case 12://运费方式为ltlshipping
			shiptype="ltlshipping";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			tempweight=weight/0.45f;
			if(name.equals("eastofusa")){
				result=(float) (0.8*tempweight+286+200);
			}else if(name.equals("westofusa")){
				result=(float) (0.8*tempweight+150+200);
			}else if(name.equals("centralofusa")){
				result=(float) (0.8*tempweight+216+200);
			}
			break;
		case 13://运费方式为Port Pickup
			shiptype="portpickup";
			/*if(weight <= 5000){
				weight = 5000;
			}*/
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);	
			if(!name.equals("0")){
				List<Map<String, Object>> portpickupList = zoneMapper.getPortpickup(name);
				float fee1=Float.valueOf(String.valueOf(portpickupList.get(portpickupList.size()-1).get("fee1")));
				float fee2=Float.valueOf(String.valueOf(portpickupList.get(portpickupList.size()-1).get("fee2")));
				result=fee1+fee2/1000*weight;
			}
			break;
		case 14://运费方式为Airport Pickup
			shiptype="airportpickup";
			
			Map<String, Float> airportpickupMap = zoneMapper.getAirportpickup(countryid);
			if(airportpickupMap != null){
				float fee1=airportpickupMap.get("fee1");
				float fee2=airportpickupMap.get("fee2");
				float fee3=airportpickupMap.get("fee3");
				if(weight < 100){
					result=fee1+fee2*weight;
				}else{
					result=fee1+fee3*weight;
				}
			}
			break;
		case 15://运费方式为Aramex Mideast
			shiptype="aramexmideast";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> aramexmideastList = zoneMapper.getAramex(name);
				float fee1=aramexmideastList.get(aramexmideastList.size()-1).get("fee1");
				float fee2=aramexmideastList.get(aramexmideastList.size()-1).get("fee2");
				float fee3=aramexmideastList.get(aramexmideastList.size()-1).get("fee3");
				float r;
				if(weight<=20){
					r=(float) (fee1+fee2*(weight-0.5)/0.5);
				}else{
					r=fee3*weight;
				}
				result=r*0.16f;
			}
			break;
		case 16://运费方式为DHL
			shiptype="dhl";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> dhlList = zoneMapper.getDHL(name);
				Map<String, Float> dhlMap = dhlList.get(dhlList.size()-1);
				
				float fee3=dhlMap.get("fee3");
				float fee4=dhlMap.get("fee4");
				float fee5=dhlMap.get("fee5");
				float fee6=dhlMap.get("fee6");
				float fee7=dhlMap.get("fee7");
				float fee8=dhlMap.get("fee8");
				float fee9=dhlMap.get("fee9");
				float fee10=dhlMap.get("fee10");
				float fee11=dhlMap.get("fee11");
				float fee12=dhlMap.get("fee12");
				float fee13=dhlMap.get("fee13");
				float fee14=dhlMap.get("fee14");
				float fee15=dhlMap.get("fee15");
				float fee16=dhlMap.get("fee16");
				
				float r = 0;
				if(weight<=0.5){
					r=fee3;
				}else if(weight>0.5&&weight<=5){
					r=(float) (fee3+fee4*(weight-0.5)/0.5);
				}else if(weight>5&&weight<=5.5){
					r=fee5;
				}else if(weight>5.5&&weight<=10){
					r=(float) (fee5+fee6*(weight-0.5)/0.5);
				}else if(weight>10&&weight<=10.5){
					r=fee7;
				}else if(weight>10.5&&weight<=21){
					r=(float) (fee7+fee8*(weight-0.5)/0.5);
				}else if(weight>21&&weight<=31){
					r=weight*fee9;
				}else if(weight>31&&weight<=51){
					r=weight*fee10;
				}else if(weight>51&&weight<=71){
					r=weight*fee11;
				}else if(weight>71&&weight<=101){
					r=weight*fee12;
				}else if(weight>101&&weight<=201){
					r=weight*fee13;
				}else if(weight>201&&weight<=300){
					r=weight*fee14;
				}else if(weight>300&&weight<=500){
					r=weight*fee15;
				}else if(weight>500){
					r=weight*fee16;
				}
				result=r*0.16f;
			}
			break;
		case 17://运费方式为dhlfba其他几个国家
			shiptype="dhlfbaother";
			shipTypes = zoneMapper.getShippingType(countryid, shiptype);
			name=shipTypes.get(shipTypes.size()-1);
			if(!name.equals("0")){
				List<Map<String, Float>> dhlList = zoneMapper.getDhlfbaother(name);
				Map<String, Float> dhlMap = dhlList.get(dhlList.size()-1);
				
				float fee1=dhlMap.get("fee1");
				float fee2=dhlMap.get("fee2");
				float fee3=dhlMap.get("fee3");
				float fee4=dhlMap.get("fee4");
				
				float r = 0;
				if(weight<=0.5){
					result=fee1;
				}else if(weight>0.5&&weight<=11){
					result=(float) (fee1+fee2*(weight-0.5)/0.5);
				}else if(weight>11&&weight<=20){
					result=fee3*weight;
				}else if(weight>20){
					result=fee4*weight;
				}
				result=r*0.16f;
			}
			break;
		default:
			break;
		}
		
		if(!flag){
			return 0;
		}
		
		return result;
	}

	@Override
	public List<ShippingBean> getShippingList(int countryid, float weight,
			float volume, float singleweightmax, int count) {
		List<ShippingBean> list=new ArrayList<ShippingBean>();
		ShippingBean sb[] = {
				new ShippingBean(1,"FedEx Economy","5-7",0,0),new ShippingBean(2,"China Post ePacket","10",0,0),new ShippingBean(3,"DHL-FBA","7-10",0,0),new ShippingBean(4,"China Post SAL","30",0,0),
				new ShippingBean(5,"China Post AIR","10-15",0,0),new ShippingBean(6,"Sweden Post","10-40",0,0),new ShippingBean(7,"Dutch post","10-20",0,0),new ShippingBean(8,"China Post Small Pak","15-20",0,0),
				new ShippingBean(9,"EMS","5",0,0),new ShippingBean(10,"China Post Express","10",0,0),new ShippingBean(11,"FedEx Ground","20-35",0,0),new ShippingBean(12,"LTL trucking","20-35",0,0),
				new ShippingBean(13,"","",0,0),new ShippingBean(14,"Airport Pickup","7",0,0),new ShippingBean(15,"Aramex MidEast","7",0,0),new ShippingBean(16,"DHL","3-5",0,0)
		};
		for(int i=0;i<count;i++){
			float temp=getCost(countryid, weight, i+1,volume,singleweightmax)*1.05f;
			//float temp1=getCost(countryid, 2*weight, i+1,volume,singleweightmax)*1.05f;
			if(temp!=0){
				BigDecimal   b   =   new   BigDecimal(temp);
				float   f   =   b.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();
				//BigDecimal   b1   =   new   BigDecimal(temp1);
				//float   f1   =   b1.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();
				if(f == 0){
					continue;
				}
				sb[i].setResult(f);
				if(i==12){
					switch(countryid){
					case 36:
						sb[i].setDays("35");
						sb[i].setName("Sea Port Pickup");
						break;
					case 42:
						sb[i].setDays("15");
						sb[i].setName("Sea Port Pickup(Los Angeles)");
						break;
					case 43:
						sb[i].setDays("15");
						sb[i].setName("Sea Port Pickup(Los Angeles)");
						break;
					case 35:
						sb[i].setDays("32");
						sb[i].setName("Sea Port Pickup(Felixstowe)");
						break;
					case 2:
						sb[i].setDays("23");
						sb[i].setName("Sea Port Pickup(Melbourne)");
						break;
					case 6:
						sb[i].setDays("17");
						sb[i].setName("Sea Port Pickup(Vancouver)");
						break;
					case 13:
						sb[i].setDays("32");
						sb[i].setName("Sea Port Pickup(Hamburg)");
						break;
					case 29:
						sb[i].setDays("35");
						sb[i].setName("Sea Port Pickup(Johannesburg)");
						break;
					default:
						break;
					}
				}
			}
		}

		if(weight<2){
			list.add(sb[1]);
			list.add(sb[5]);
			list.add(sb[6]);
			list.add(sb[9]);
			list.add(sb[7]);
			list.add(sb[8]);
			Collections.sort(list, new ShipComparator());	
		}else if(weight<15&&weight>=2){
			list.add(sb[4]);
			list.add(sb[3]);
			list.add(sb[9]);
			list.add(sb[7]);
			//list.add(sb[2]);
			list.add(sb[0]);
			Collections.sort(list, new ShipComparator());
		}else if(weight<30&&weight>=15){
			list.add(sb[10]);
			list.add(sb[4]);
			list.add(sb[3]);
			list.add(sb[9]);
			list.add(sb[7]);
			//list.add(sb[2]);
			list.add(sb[0]);
			list.add(sb[15]);
			Collections.sort(list, new ShipComparator());
		}else if(weight>=30){
			//list.add(sb[2]);
			list.add(sb[0]);
			list.add(sb[11]);
			list.add(sb[12]);
			list.add(sb[13]);
			list.add(sb[14]);
			Collections.sort(list, new ShipComparator());
		}
		int k = list.size();
		int t = 0;
		for (int j = 0; j < k; j++) {
			j = t == 1 ? j-1:j;
			if(list.get(j).getResult() == 0){
				list.remove(j);
				t = 1;
				k--;
			}else {
				t = 0;
			}
		}
		return list;
	}
	
	  
	
	@Override
	public List<Object[]> getVatDuty(int countryid, double productPrice,
			List<Object[]> product_type_price, int packageNumber) {
		//获取国家最大免查金额
		double checkfreePrice = zoneMapper.getCheckfreePrice(countryid);
		List<Object[]> vatDuty = new ArrayList<Object[]>();
		if(checkfreePrice != 0){
			if(productPrice/packageNumber > checkfreePrice*1.5){
				Map<String, Object[]> map = new HashMap<String, Object[]>();
				for (int i = 0; i < product_type_price.size(); i++) {
					double price = (Double) product_type_price.get(i)[0];
					String type = (String)product_type_price.get(i)[1];
					if(map.get("type"+type) != null){
						map.get("type"+type)[1] = price + (Double)map.get("type"+type)[1];
					}else{
						map.put("type"+type, new Object[]{type, price});
					}
				}
				vatDuty = getVat_Duty(map);
			}
		}
		return vatDuty;
	}
	
	private List<Object[]> getVat_Duty(Map<String, Object[]> type_prices) {
		List<Object[]> res = new ArrayList<Object[]>();
		List<String> list = new ArrayList<String>(type_prices.keySet());
		List<Map<String, Object>> listMap = zoneMapper.getVat_Duty(list);
		for (Map<String, Object> map : listMap) {
			double vat = (Double)map.get("vat");
			double custom_duty = (Double)map.get("custom_duty");
			double aliExpress_type_name = (Double)map.get("aliExpress_type_name");
			int aliExpress_type = (Integer)map.get("aliExpress_type");
			Object[] type_price = type_prices.get("type"+aliExpress_type);
			double price = (Double) type_price[1];
			res.add(new Object[]{aliExpress_type_name, new BigDecimal(price*vat).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(), new BigDecimal(price*custom_duty).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()});
		}
		
		return res;
	}
	
	 
	 
	
	@Override
	public List<ShippingBean> getAirport_SeaAir(double pre_weight) {
		List<Map<String, Object>> listMap = zoneMapper.getAirport_SeaAir();
		double freight = 0;
		List<ShippingBean> shippingBeans = new ArrayList<ShippingBean>();
		for (Map<String, Object> m : listMap) {
			String delivery_time = String.valueOf(m.get("delivery_time"));
			String shippingmethod = String.valueOf(m.get("shippingmethod_en"));
			double under = Double.valueOf(String.valueOf(m.get("under")));
			double over = Double.valueOf(String.valueOf(m.get("over")));
			double divisionweight = Double.valueOf(String.valueOf(m.get("divisionweight")));
			double minweight = Double.valueOf(String.valueOf(m.get("minweight")));
			double maxweight = Double.valueOf(String.valueOf(m.get("maxweight")));
			if(pre_weight >= minweight && pre_weight <= maxweight){
				if(divisionweight < pre_weight){
					//运费 = 首重价 + （（总重量-整除重量） 整除   整除重量 +1） * 续重价
					freight = under + Math.ceil((pre_weight-divisionweight)/divisionweight)*over;
				}else{
					freight = under;
				}
				freight = new BigDecimal(freight/Util.EXCHANGE_RATE).setScale(0,   BigDecimal.ROUND_HALF_UP).doubleValue();
				ShippingBean shippingBean = new ShippingBean();
				shippingBean.setDays(delivery_time);
				shippingBean.setName(shippingmethod);
				//6.4汇率
				shippingBean.setResult(freight);
				shippingBean.setResult1(1);
				shippingBean.setBattery(0);
				shippingBean.setVolumeweights(pre_weight);
				if(delivery_time.indexOf("5-7") > -1 && shippingBeans.size() > 0){
					for (int i = 0; i < shippingBeans.size(); i++) {
						if(shippingBeans.get(i).getDays().indexOf("5-7") > -1 && shippingBeans.get(i).getResult() > freight){
							shippingBeans.remove(i);
							shippingBeans.add(shippingBean);
						}
					}
				}else{
					shippingBeans.add(shippingBean);
				}
			}
		}
		return shippingBeans;
	}

	@Override
	public int addVerifyWeight(int userid, String remark, int packaging) {
		return	zoneMapper.addVerifyWeight(userid, remark, packaging);
	}

	@SuppressWarnings("unchecked") 
	@Override
	public double getSpidertFreight(int countryid,double weight, double volumes, String goods_catid, String method) {
		List<Object> transits = getDbTransit(countryid, new String[]{goods_catid}, 0);
		String isBattery = isBattery(null, goods_catid,(List<TransitType>) transits.get(2));
		List<TransitPricecost> transitPricecosts = (List<TransitPricecost>) transits.get(0);
		
		double freight = 0;
		if(method != null){
			for (int i = 0; i < transitPricecosts.size(); i++) {
				String shippingmethod = transitPricecosts.get(i).getShippingmethod_en();
				if(shippingmethod.toLowerCase().equals(method.toLowerCase())){
					double freight_ = getFreight(transitPricecosts.get(i), weight, 1, 0);//运费
					if(freight_ == 0)continue;
					if(freight_ < freight || freight == 0 ){
						freight = freight_;
					}
				}
			}
		}else{
			for (int i = 0; i < transitPricecosts.size(); i++) {
				String shippingmethod = transitPricecosts.get(i).getShippingmethod_en();
				if(isBattery.equals("N")){
					if((weight < 1.7 && shippingmethod.equals("ePacket")) || (weight >= 1.7 && shippingmethod.equals("IMExpress"))){
						double freight_ = getFreight(transitPricecosts.get(i), weight, 1, 0);//运费
						if(freight_ == 0)continue;
						if(freight_ < freight || freight == 0 ){
							freight = freight_;
						}
					}
				}else{
					double freight_ = getFreight(transitPricecosts.get(i), weight, 1, 0);//运费
					if(freight_ == 0)continue;
					if(freight_ < freight || freight == 0){
						freight = freight_;
					}
				}
			}
		}
		return new BigDecimal(freight).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public List<SearchGoods> getProductFreight(int countryid, List<SearchGoods> searchGoods) {
		List<Object>  transitCheckfrees = getDbTransit(countryid, null, 0);
		//获取运费表
		List<TransitPricecost> transitPricecosts = (List<TransitPricecost>) transitCheckfrees.get(0);
		for (int i = 0; i < searchGoods.size(); i++) {
			SearchGoods searchGood = searchGoods.get(i);
			String weight_str = searchGood.getGoodsWeight();
			double weight = Utility.getIsDouble(weight_str) ? Double.parseDouble(weight_str):0;
			if(weight != 0){
				int moq = Utility.getIsInt(searchGood.getGoods_minOrder()) ? Integer.parseInt(searchGood.getGoods_minOrder()) : 1;
				//单个重量，最小订量*重量，2倍最小订量*重量
				double[] weights = new double[]{weight,moq == 1 ? 0 : moq*weight , moq*weight*2};
				double[] freights = new double[3];
				for (int j = 0; j < weights.length; j++) {
					double freight = 0;
					//如果最小订量是1，则不需再计算Moq的运费
					if(j == 1 && weights[1] == 0){
						freights[1] = freights[0];
						continue;
					}
					for (int k = 0; k < freights.length; k++) {
						double minweight = transitPricecosts.get(k).getMinweight();
						double maxweight = transitPricecosts.get(k).getMaxweight();
						if(minweight > weights[j] || weights[j] > maxweight)continue;
						String shippingmethod = transitPricecosts.get(k).getShippingmethod_en();
						if((weights[j] < 1.7 && shippingmethod.equals("ePacket")) || (weights[j] >= 1.7 && shippingmethod.equals("IMExpress"))){
							double freight_ = getFreight(transitPricecosts.get(k), weight, 1, 0); 				
							if(freight_ == 0)continue;
							if(freight_ < freight || freight == 0){
								freight = freight_;
							}
						}
					}
					freights[j] = freight;
				}
				searchGood.setFreight(new BigDecimal(freights[0]).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				searchGood.setoMoqFreight(new BigDecimal(freights[1]).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				searchGood.settMoqFreight(new BigDecimal(freights[2]).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		}
		return searchGoods;
	}

	
	/**
	 *获取redis保存的计算运费相关数据
	 * @param countryid
	 * @param types
	 * @param isone是否计算单个商品 0-是(计算单个商品，重量<2则ePacket，重量>=2则佳成)，1-否(计算购物车商品，获取对应国家的所有运输方式)
	 * @param weight重量，计算单个商品值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Object> getDbTransit(int countryid,String[] types, int isone){
		ServletContext application=request.getSession().getServletContext();
		Object transitCheckfree = application.getAttribute("transitCheckfree");
		Object transitPricecost = application.getAttribute("transitPricecost");
		Object transitType = application.getAttribute("transitTypeBattery");
		Object zone = application.getAttribute("zone");
		List<TransitCheckfree>  transitCheckfrees = (List<TransitCheckfree>) transitCheckfree;
		//获取每个国家的 建议单包最大重量 和 单包免查报关金额 , price weight tariff
		int checkfrees = -1;
		for (int i = 0; i < transitCheckfrees.size(); i++) {
			TransitCheckfree transitCheckfree_ =  transitCheckfrees.get(i);
			if(transitCheckfree_.getCountryid() == countryid){
				checkfrees = i;
			}
		}
		
		//获取运输类别表,运输类别transit_type、ali类型IDaliExpress_type、是否有电池battery---------表中需增加一列运输类别和ali类型ID为0的
		List<TransitType>  transitTypes = (List<TransitType>) transitType;
		List<TransitType> checkfreeTypes = new ArrayList<TransitType>();
		if(types != null){
			for (int i = 0; i < types.length; i++) {
				ok:
				for (int j = 0; j < transitTypes.size(); j++) {
					TransitType transitType_ = (TransitType) transitTypes.get(j);
					String type = Utility.getStringIsNull(types[i]) ? types[i] : "0";
					if(transitType_.getAliExpress_type().equals(type)){
						checkfreeTypes.add(transitType_);
						break ok;
					}
				}
			}
		}
		
		//获取运费表
		List<TransitPricecost>  transitPricecost_obj = (List<TransitPricecost>) transitPricecost;
		List<TransitPricecost>  transitPricecosts = new ArrayList<TransitPricecost>();
		for (int i = 0; i < transitPricecost_obj.size(); i++) {
			TransitPricecost transitPricecost_ = (TransitPricecost) transitPricecost_obj.get(i);
			/*if(isone == 1){
				if(transitPricecost_.getCountryId() == countryid && transitPricecost_.getDays() != 90){
					transitPricecosts.add(transitPricecost_);
				}
			}else{*/
				if(transitPricecost_.getCountryId() == countryid && transitPricecost_.getDays() != 90){
//					if(transitPricecost_.getShippingmethod_en().equals("IMExpress") || transitPricecost_.getShippingmethod_en().equals("ePacket")){
						transitPricecosts.add(transitPricecost_);
//					}
				}
		/*}*/
		}
		String area = "";
		List<ZoneBean>  zones = (List<ZoneBean>) zone;
		for (int i = 0; i < zones.size(); i++) {
			if(zones.get(i).getId() == countryid){
				area = zones.get(i).getArea();
				if(area == null)area = "";
			}
		}
		
		List<Object> dbTransit = new ArrayList<Object>();
		dbTransit.add(transitPricecosts);
		dbTransit.add(checkfrees == -1 ? new TransitCheckfree() : transitCheckfrees.get(checkfrees));
		dbTransit.add(checkfreeTypes);
		dbTransit.add(area);
		return dbTransit;
	}

	/**
	 *获取其他国家的ePacket运费
	 * @param countryid
	 * @param weight
	 * @param shippingtime购物车合并运输方式时间
	 * @return fright运费
	 */
	@Override
	public double getEpacketFreight(int countryid, double weight, int weight_three){
		ServletContext application=request.getSession().getServletContext();
		Object transitPricecost = application.getAttribute("transitPricecost_car");
		double fright = 0;
		//获取运费表
		List<TransitPricecost>  transitPricecost_obj = (List<TransitPricecost>) transitPricecost;
		for (int i = 0; i < transitPricecost_obj.size(); i++) {
			TransitPricecost transitPricecost_ = (TransitPricecost) transitPricecost_obj.get(i);
			int countryId = transitPricecost_.getCountryId();
			if( (countryId == countryid && (transitPricecost_.getShippingmethod_en().equals("ePacket") ))){
				// weight_three 产品单页重量在 300克以下则首重价*2
				fright = getFreight(transitPricecost_, weight, 1, transitPricecost_.getUnder() * weight_three)*Util.EXCHANGE_RATE;//运费 
				if(fright != 0){
					break;
				}
			}
		}
		return fright;
	}
	
	/**
	 *获取其他国家的快递运费
	 * @param countryid
	 * @param weight
	 * @param shippingtime购物车合并运输方式时间
	 * @return fright运费
	 */
	@Override
	public double[] getTransitJCEXFeight(int countryid, double[] weight, int weight_three){
		ServletContext application=request.getSession().getServletContext();
		Object countryEpacket_obj = application.getAttribute("countryEpacket");
		List<CountryEpacketjcexBean> countryEpacket = (List<CountryEpacketjcexBean>) countryEpacket_obj;
		double[] freight = new double[weight.length];
		for (int i = 0; i < weight.length; i++) {
			if(weight[i] == 0){
				freight[i]= 0;
				break;
			}
			for (int j = 0; j < countryEpacket.size(); j++) {
				if(countryEpacket.get(j).getCountryid() == countryid){
					double under = countryEpacket.get(j).getJcex_add();
					double over = countryEpacket.get(j).getJcex_ratio();
					freight[i] = new BigDecimal(under*weight_three + (weight[i] - 0.5) / 0.5 * over + 20).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					break;
				}
			}
		}
		return freight;
	}

	/**
	 *获取其他国家的快递运费
	 * @param countryid
	 * @param weight
	 * @param shippingtime购物车合并运输方式时间
	 * @return fright运费
	 */
	@Override
	public double getTransitJCEXFeight(int countryid, double weight){
		ServletContext application=request.getSession().getServletContext();
		Object countryEpacket_obj = application.getAttribute("countryEpacket");
		List<CountryEpacketjcexBean> countryEpacket = (List<CountryEpacketjcexBean>) countryEpacket_obj;
			for (int j = 0; j < countryEpacket.size(); j++) {
				if(countryEpacket.get(j).getCountryid() == countryid){
					double under = countryEpacket.get(j).getJcex_add();
					double over = countryEpacket.get(j).getJcex_ratio();
					return new BigDecimal(under + (weight - 0.5) / 0.5 * over).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				}
			}
			return 0;
	}
	
	@Override
	public List<ShippingBean> getShippingBeans2(int countryid,
			double[] weights, double[] prices, double[] volumes,
			String[] types, int[] number, int isVlo) {
		List<Object>  transitCheckfrees = getDbTransit(countryid, types, 1);
		//获取每个国家的 建议单包最大重量 和 单包免查报关金额 , price weight tariff
//		TransitCheckfree checkfrees = (TransitCheckfree) transitCheckfrees.get(1);
		////获取运输类别表,运输类别transit_type、ali类型IDaliExpress_type、是否有电池battery---------表中需增加一列运输类别和ali类型ID为0的
		List<TransitType> checkfreeTypes = (List<TransitType>) transitCheckfrees.get(2);
		//获取运费表
		List<TransitPricecost> transitPricecosts = (List<TransitPricecost>) transitCheckfrees.get(0);
		//运输类别拆分后的包裹，key:运输类别、value:金额、重量、是否有电池,重量包裹数量，每个包裹平均重量，产品件数，体积重量,体积重量包裹数量，体积重量的平均重量,（带电的 在 算 20-30天运费的时候 增加 一个 限制 每个包裹 重量=<2KG），2KG包裹数量
//		Map<String, TypeSplitPackg> type_splits= new HashMap<String, TypeSplitPackg>();
		//仅按照有无电池拆包裹，key:电池有无、value:包裹总金额、包裹总重量，产品件数，体积重量,（带电的 在 算 20-30天运费的时候 增加 一个 限制 每个包裹 重量=<2KG），2KG包裹数量
		Map<String, TypeSplitPackg> typeSplitPackgs= new HashMap<String, TypeSplitPackg>();
		double volumeweights = 0;//总体积重
		float sum_weights = 0;
		String isBattery = "N";
		for (int i = 0; i < types.length; i++) {
			double weight = weights[i];
			double price = prices[i];
			double volumeweight=weight;
			if(isVlo == 1){//计算体积重
				volumeweight=volumes[i]*200;
				if(volumeweight<=weight){
					volumeweight=weight;
				}
			}
			volumeweights += volumeweight; 
			sum_weights += weight;
			int s = 0;
			for (int j = 0; j < checkfreeTypes.size(); j++) {
				String checkfreeType = checkfreeTypes.get(j).getAliExpress_type();
				String battery = checkfreeTypes.get(j).getBattery();
				String type = Utility.getStringIsNull(types[i]) ? types[i] : "0";
				if(checkfreeType.equals(type)){
					if(battery.equals("Y")){
						isBattery = "Y";
						s = 1;
					}
					break;
					
				}
			}

			 if(typeSplitPackgs.get(s == 1 ? "Y" : "N") == null){
				TypeSplitPackg typeSplitPackg = new TypeSplitPackg();
				typeSplitPackg.setPrice(price);
				typeSplitPackg.setWeight(weight);
				typeSplitPackg.setProduct_number(number[i]);
				typeSplitPackg.setVolumeweight(volumeweight);
				typeSplitPackg.setBattery(s == 1 ? "Y" : "N");
				typeSplitPackg.setPackage_number(1);
				typeSplitPackg.setPackage_avg(typeSplitPackg.getWeight());  
				typeSplitPackg.setWw_package(1);
				typeSplitPackg.setWw_avg(typeSplitPackg.getVolumeweight());
				typeSplitPackgs.put(s == 1 ? "Y" : "N", typeSplitPackg);
			 }else{
				TypeSplitPackg typeSplitPackg = typeSplitPackgs.get(s == 1 ? "Y" : "N");
				typeSplitPackg.setPrice(price+typeSplitPackg.getPrice());
				typeSplitPackg.setWeight(weight+typeSplitPackg.getWeight());
				typeSplitPackg.setProduct_number(2);
				typeSplitPackg.setVolumeweight(volumeweight+typeSplitPackg.getVolumeweight());
				typeSplitPackg.setPackage_number(1);
				typeSplitPackg.setPackage_avg(typeSplitPackg.getWeight());  
				typeSplitPackg.setWw_package(1);
				typeSplitPackg.setWw_avg(typeSplitPackg.getVolumeweight());
			 }
		}
		
		 
			
		List<ShippingBean> shippingBeans = new ArrayList<ShippingBean>();
		int h = -1;
		
		if(typeSplitPackgs.get("N") != null){
			TypeSplitPackg typeSplitPackg = typeSplitPackgs.get("N");
			for (int i = 0; i < transitPricecosts.size(); i++) {
				TransitPricecost transitPricecost_ = transitPricecosts.get(i);
				if(transitPricecost_.getBattery().equals("Y"))continue;
				//总重1.7 KG 以下
				if(typeSplitPackg.getVolumeweight() < 1.7 ){
					//普通商品按  小包运费计算 （ePacket)
					if(transitPricecost_.getShippingmethod_en().equals("ePacket") && typeSplitPackg.getBattery().equals("N")){
						h = i;
					}
				}else{
					//普通商品按  大包运费计算 （JCEX)
					if(transitPricecost_.getShippingmethod_en().equals("IMExpress") && typeSplitPackg.getBattery().equals("N")){
						h = i;
					}
				}
				if(h == -1)continue;
				transitPricecost_ = transitPricecosts.get(h);
				int package_number_vw =  typeSplitPackg.getWw_package();
				String delivery_time = transitPricecost_.getDelivery_time();
				String shippingmethod = transitPricecost_.getShippingmethod_en();
				double package_freight = getFreight(transitPricecost_, typeSplitPackg.getWw_avg(),typeSplitPackg.getPackage_number(), 0);//运费
				if(package_freight == 0)continue;
				ShippingBean shippingBean = new ShippingBean();
				shippingBean.setBattery(0);
				shippingBean.setDays(delivery_time);
				shippingBean.setName(shippingmethod);
				shippingBean.setResult(package_freight);
				shippingBean.setFeight(package_freight);
				shippingBean.setResult1(package_number_vw);
				shippingBean.setVolumeweights(volumeweights);
				shippingBeans.add(shippingBean);
				break;
			}
		}
		if(typeSplitPackgs.get("Y") != null){
			TypeSplitPackg typeSplitPackg = typeSplitPackgs.get("Y");
			for (int i = 0; i < transitPricecosts.size(); i++) {
				TransitPricecost transitPricecost_ = transitPricecosts.get(i);
				String battery = transitPricecost_.getBattery();
				if(battery.equals("N"))continue;
				int package_number_vw =  typeSplitPackg.getWw_package();
				String delivery_time = transitPricecost_.getDelivery_time();
				String shippingmethod = transitPricecost_.getShippingmethod_en();
				double package_freight = getFreight(transitPricecost_, typeSplitPackg.getWw_avg(),typeSplitPackg.getPackage_number(), 0);//运费
				if(package_freight == 0)continue;
				ShippingBean shippingBean = new ShippingBean();
				shippingBean.setBattery(1);
				shippingBean.setDays(delivery_time);
				shippingBean.setName(shippingmethod);
				shippingBean.setResult(package_freight);
				shippingBean.setFeight(package_freight);
				shippingBean.setResult1(package_number_vw);
				shippingBean.setVolumeweights(volumeweights);
				shippingBeans.add(shippingBean);
			}
		}
		if(typeSplitPackgs.get("N") != null && isBattery.equals("Y")){
			for (int i = 1; i < shippingBeans.size(); i++) {
				ShippingBean shippingBean = shippingBeans.get(i);
				shippingBean.setResult(new   BigDecimal(shippingBean.getResult()+shippingBeans.get(i).getResult()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				shippingBean.setFeight(shippingBean.getResult());
				shippingBean.setResult1(2);
			}
			shippingBeans.remove(0);
		}
		
		Collections.sort(shippingBeans, new ShipComparator());
		if(shippingBeans.size() > 0){
			if(isBattery.equals("N")){
				double result = shippingBeans.get(0).getResult();
				if(typeSplitPackgs.get("N").getVolumeweight() < 1.7 ){
					ShippingBean shippingBean59 = new ShippingBean();
					shippingBean59.setBattery(shippingBeans.get(0).getBattery());
					shippingBean59.setDays("5-9");
					shippingBean59.setName("59");
					shippingBean59.setResult(new BigDecimal(result + result * 0.4).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
					shippingBean59.setResult1(typeSplitPackgs.get("N").getPackage_number());
					shippingBean59.setFeight(result);
					shippingBean59.setPromote_shipping(new BigDecimal(result * 0.4).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
					ShippingBean shippingBean35 = new ShippingBean();
					shippingBean35.setBattery(shippingBeans.get(0).getBattery());
					shippingBean35.setDays("3-5");
					shippingBean35.setName("35");
					shippingBean35.setResult(new BigDecimal(shippingBeans.get(0).getResult() * 2).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
					shippingBean35.setResult1(typeSplitPackgs.get("N").getPackage_number());
					shippingBean35.setPromote_shipping(shippingBeans.get(0).getResult());
					shippingBean35.setFeight(result);
					shippingBeans.add(shippingBean59);
					shippingBeans.add(shippingBean35);
				}else {
					ShippingBean shippingBean35 = new ShippingBean();
					shippingBean35.setBattery(shippingBeans.get(0).getBattery());
					shippingBean35.setDays("3-5");
					shippingBean35.setName("35");
					shippingBean35.setResult(new BigDecimal(result + 10 + (result * 0.3)).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
					shippingBean35.setPromote_shipping(new BigDecimal(result * 0.3 + 10).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
					shippingBean35.setResult1(typeSplitPackgs.get("N").getPackage_number());
					shippingBean35.setFeight(result);
					shippingBeans.add(shippingBean35);
				}
			}else if(shippingBeans.size() > 1){
				double result = shippingBeans.get(0).getResult();
				shippingBeans.get(1).setPromote_shipping(new BigDecimal(shippingBeans.get(1).getResult()-result).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		}
		
		
		if(sum_weights >= 30){
			
			ShippingBean sb[] = {new ShippingBean(12,"LTL trucking","20-35",0,1),new ShippingBean(13,"","",0,1),new ShippingBean(14,"Airport Pickup","5-10",0,1)};
			for(int i=0;i<3;i++){
				float temp=getCost(countryid, sum_weights, i+12,(float)0,0f)*1.05f;
				if(temp!=0){
					BigDecimal   b   =   new   BigDecimal(temp);
					double   f   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					if(f == 0){
						continue;
					}
					sb[i].setResult(f);
					if(i==1){
						sb[i].setIsJCEX(1);
						switch(countryid){
						case 36:
							sb[i].setDays("35");
							sb[i].setName("Sea Port Pickup");
							break;
						case 42:
							sb[i].setDays("15");
							sb[i].setName("Sea Port Pickup(Los Angeles)");
							break;
						case 43:
							sb[i].setDays("15");
							sb[i].setName("Sea Port Pickup(Los Angeles)");
							break;
						case 35:
							sb[i].setDays("32");
							sb[i].setName("Sea Port Pickup(Felixstowe)");
							break;
						case 2:
							sb[i].setDays("23");
							sb[i].setName("Sea Port Pickup(Melbourne)");
							break;
						case 6:
							sb[i].setDays("17");
							sb[i].setName("Sea Port Pickup(Vancouver)");
							break;
						case 13:
							sb[i].setDays("32");
							sb[i].setName("Sea Port Pickup(Hamburg)");
							break;
						case 29:
							sb[i].setDays("35");
							sb[i].setName("Sea Port Pickup(Johannesburg)");
							break;
						default:
							break;
						}
					}
					if(Utility.getStringIsNull(sb[i].getDays())){
						sb[i].setFeight(f);
						sb[i].setVolumeweights(volumeweights);
						if(shippingBeans.size()>0 && shippingBeans.get(0).getResult() > sb[i].getResult()){
							shippingBeans.add(sb[i]);
						}
					}
				}
			}
			System.out.println(JSONArray.fromObject(shippingBeans));
			int k = shippingBeans.size();
			int t = 0;
			for (int j = 0; j < k; j++) {
				j = t == 1 ? j-1:j;
				if(shippingBeans.get(j).getResult() == 0){
					shippingBeans.remove(j);
					t = 1;
					k--;
				}else {
					t = 0;
				}
			}
		}
//		if(results.get("result_JCEX") != null && shippingBean_jcex.getIsJCEX()==0){
//			shippingBeans.add(shippingBean_jcex);
//		}
	
		return shippingBeans;
	}
	
	/**
	 *计算运费
	 * @param weight重量
	 * @param freight上一次的运费
	 * @param transitPricecost
	 * @return
	 */
	private double getFreight(TransitPricecost transitPricecost_, double package_vw, int package_number,double under_){
		double divisionweight = transitPricecost_.getDivisionweight();
		double under = under_ == 0 ? transitPricecost_.getUnder() : under_;
		double over = transitPricecost_.getOver();
		double minweight = transitPricecost_.getMinweight();
		double maxweight = transitPricecost_.getMaxweight();
		if(minweight > package_vw || package_vw > maxweight)return 0;
		double package_freight = 0;//运费
		if(minweight > package_vw || package_vw > maxweight)return 0;
		
		if(transitPricecost_.getShippingmethod_en().indexOf("ePacket") > -1){
			double divisionweight_ = divisionweight < 0.05 ? 0.05 : divisionweight;
			if(divisionweight_ < package_vw){
				package_freight = under + Math.ceil((package_vw-divisionweight_)/divisionweight)*over;
			}else{
				package_freight = under;
			}
		}else{
			if(divisionweight < package_vw){
				//运费 = 首重价 + （（总重量-整除重量） 整除   整除重量 +1） * 续重价
				package_freight = under + Math.ceil((package_vw-divisionweight)/divisionweight)*over;
			}else{
				package_freight = under;
			}
		
		}
		package_freight = new BigDecimal(package_freight * package_number / Util.EXCHANGE_RATE).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
		 
		return package_freight;
	}

	@Override
	public ShippingBean getOrderFreight(int countryid,List<Double> weightLis,List<String> typeLis) {
		int weightLength = weightLis.size();
		int typeLength = typeLis.size();
		double[] weights = new double[weightLength];
		String[] types = new String[typeLength];
		for (int i = 0; i < weightLength; i++) {
			weights[i] = weightLis.get(i);
			types[i] = typeLis.get(i);
		}
		List<ShippingBean> shippingBean =  this.getShippingBeans2(countryid, weights, new double[weightLength], new double[weightLength], types, new int[weightLength], 0);
		return shippingBean.size() > 0 ? shippingBean.get(0) : null;
	}
	

	@Override
	public double getSpidertFreight(int countryid_now,double weight, double volumes, String goods_catid) {
//		List<Map<String, Object>> listMap = zoneMapper.spidertFreight(countryid, weight);
		ServletContext application=request.getSession().getServletContext();
		
		String isBattery = isBattery(application, goods_catid, null);
		
		Object transitPricecost = application.getAttribute("transitPricecost");
		//获取运费表
		List<TransitPricecost>  transitPricecost_obj = (List<TransitPricecost>) transitPricecost;
		
		List<TransitPricecost>  transitPricecosts = new ArrayList<TransitPricecost>();
		for (int i = 0; i < transitPricecost_obj.size(); i++) {
			TransitPricecost transitPricecost_ = (TransitPricecost) transitPricecost_obj.get(i);
			int countryId = transitPricecost_.getCountryId();
				if(( countryId == countryid_now) && (transitPricecost_.getBattery().equals(isBattery))){
					transitPricecosts.add(transitPricecost_);
				}
		}
		
		double freight =0;
			for (int i = 0; i < transitPricecosts.size(); i++) {
				TransitPricecost transitPricecost_ = (TransitPricecost) transitPricecosts.get(i);
				String name = transitPricecost_.getShippingmethod_en();

				if(countryid_now != 0 && transitPricecost_.getCountryId() == countryid_now){
					if(isBattery.equals("N")){
						if((name.equals("ePacket")  && weight < 1.7 )|| (name.equals("IMExpress") && weight >= 1.7)){
								double freight_ = getFreight(transitPricecost_, weight, 1, 0);
								if(freight_ == 0)continue;
								if(freight == 0 || freight_ < freight ){
									freight = freight_;
								}
							}
					}else{
						double freight_ = getFreight(transitPricecost_, weight, 1, 0);
						if(freight_ == 0)continue;
						if(freight == 0 || freight_ < freight ){
							freight = freight_;
						}
					}
				}
			}
			
		return freight;
	}
	
	/**
	 *获取该商品是否带电
	 * @param goods_catid商品类别ID
	 * @return 1-带电，0-不带电
	 */
	@SuppressWarnings("unchecked")
	private  String isBattery(ServletContext application, String goods_catid, List<TransitType>  transitTypes){
		if(transitTypes == null){
			Object transitType = application.getAttribute("transitTypeBattery");
			transitTypes = (List<TransitType>) transitType;
		}
		if(goods_catid != null){
			for (int j = 0; j < transitTypes.size(); j++) {
				TransitType transitType_ = transitTypes.get(j);
				if(transitType_.getAliExpress_type().equals(goods_catid)){
					return transitType_.getBattery();
				}
			}
		}
		return "N";
	}
	
	/**
	 *获取该国家是否存在E邮宝
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int exsitCountryEpacket(int countryid){
		ServletContext application=request.getSession().getServletContext();
		Object countryEpacket_obj = application.getAttribute("countryEpacket");
		List<CountryEpacketjcexBean> countryEpacket = (List<CountryEpacketjcexBean>) countryEpacket_obj;
		for (int j = 0; j < countryEpacket.size(); j++) {
			if(countryEpacket.get(j) != null && countryEpacket.get(j).getCountryid() == countryid){
				return countryEpacket.get(j).getExist_epacket();
			}
		}
		return 1;
	}
	
	class TypeSplitPackg{
	    private double price;//金额
		private double weight;//重量
		private String battery;//是否有电池
		private int package_number;//重量包裹数量
		private double package_avg;//每个包裹平均重量
		private int product_number;//产品件数
		private double volumeweight;//体积重
		private int ww_package;//体积重量包裹数量
		private double ww_avg;//体积重量的平均重量
		private int packg_number_2kg;//2KG包裹数量
		private double batteryY_avg;//带电的 在 算 20-30天运费的时候 增加 一个 限制 每个包裹 重量=<2KG
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public double getWeight() {
			return weight;
		}
		public void setWeight(double weight) {
			this.weight = weight;
		}
		public double getBatteryY_avg() {
			return batteryY_avg;
		}
		public void setBatteryY_avg(double batteryY_avg) {
			this.batteryY_avg = batteryY_avg;
		}
		public String getBattery() {
			return battery;
		}
		public void setBattery(String battery) {
			this.battery = battery;
		}
		public int getPackage_number() {
			return package_number;
		}
		public void setPackage_number(int package_number) {
			this.package_number = package_number;
		}
		public double getPackage_avg() {
			return package_avg;
		}
		public void setPackage_avg(double package_avg) {
			this.package_avg = package_avg;
		}
		public int getProduct_number() {
			return product_number;
		}
		public void setProduct_number(int product_number) {
			this.product_number = product_number;
		}
		public double getVolumeweight() {
			return volumeweight;
		}
		public void setVolumeweight(double volumeweight) {
			this.volumeweight = volumeweight;
		}
		public int getWw_package() {
			return ww_package;
		}
		public void setWw_package(int ww_package) {
			this.ww_package = ww_package;
		}
		public double getWw_avg() {
			return ww_avg;
		}
		public void setWw_avg(double ww_avg) {
			this.ww_avg = ww_avg;
		}
		public int getPackg_number_2kg() {
			return packg_number_2kg;
		}
		public void setPackg_number_2kg(int packg_number_2kg) {
			this.packg_number_2kg = packg_number_2kg;
		}
		
	}


}
