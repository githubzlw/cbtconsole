package com.cbt.fee.service;

import com.cbt.bean.ShippingBean;
import com.cbt.bean.StateName;
import com.cbt.bean.TransitPricecost;
import com.cbt.bean.ZoneBean;
import com.cbt.fee.dao.IZoneDao;
import com.cbt.fee.dao.ZoneDao;
import com.cbt.util.ShipComparator;
import com.cbt.util.Util;
import com.cbt.util.Utility;
import net.sf.json.JSONArray;

import java.math.BigDecimal;
import java.util.*;

public class ZoneServer implements IZoneServer {
	IZoneDao dao=new ZoneDao();
//	private final double usa_et = 6.75; //美元汇率 
	@Override
	public List<ZoneBean> getAllZone() {
		// TODO Auto-generated method stub
		return dao.getAllZone();
	}
	@Override
	public List<StateName> getStateName() {
		// TODO Auto-generated method stub
		return dao.getStateName();
	}
	@Override
	public float getCost(int countryid, float weight,int type,float volume,float singleweightmax) {
		// TODO Auto-generated method stub
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
		
		switch(type){
		case 1://运费方式为fedexie
			shiptype="fedexie";
			name=dao.getShippingType(countryid, shiptype);
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
					
					result=(float) (dao.getFedexiePrice(weight, colname, shiptype)*0.16*0.9);
				}else if(weight>20.5&&weight<=44){
					result=(float) (dao.getFedexiePrice(44, colname, shiptype)*weight*0.16*0.9);
				}else if(weight>44&&weight<=70){
					result=(float) (dao.getFedexiePrice(70, colname, shiptype)*weight*0.16*0.9);
				}else if(weight>70&&weight<=99){
					result=(float) (dao.getFedexiePrice(99, colname, shiptype)*weight*0.16*0.9);
				}else if(weight>99&&weight<=299){
					result=(float) (dao.getFedexiePrice(299, colname, shiptype)*weight*0.16*0.9);
				}else if(weight>299){
					result=(float) (dao.getFedexiePrice(300, colname, shiptype)*weight*0.16*0.9);
				}
			}

			break;
		case 2://运费方式为epacket
			shiptype="epacket";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
			   result=dao.getEpacketPrice(name,weight)*0.16f;	
			}
			
			break;
		case 3://运费方式为dhlfba
			shiptype="dhlfba";
			name=dao.getShippingType(countryid, shiptype);
//			//System.out.println(name);
			if(!name.equals("0")){
				result=dao.getDhlfbaPrice(name, weight)*0.16f;	
			}
			break;
		case 4://运费方式为china post sal
			shiptype="chinapostbig";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getChinapostsalPrice(name, weight)*0.16f;	
			}
			break;
		case 5://运费方式为china post air
			shiptype="chinapostbig";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getChinapostsurfacePrice(name, weight)*0.16f;	
			}
			break;
		case 6://运费方式为Sweden瑞典邮政小包
			shiptype="sweden";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getSweden(name, weight)*0.16f;	
			}
			break;
		case 7://运费方式为TNT荷兰小包价格,限重： 2KG
			shiptype="tnt";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getTNT(name, weight)*0.16f;	
			}
			break;
		case 8://运费方式为KYD 国际小包
			shiptype="kyd";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getKyd(name, weight)*0.16f;	
			}
			break;
		case 9://运费方式为EMS
			shiptype="ems";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getEMS(name, weight)*0.16f;	
			}
			break;
		case 10://运费方式为E特快
			shiptype="efast";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getEfast(name, weight)*0.16f;	
			}
			break;
		case 11://运费方式为FedexGround
			shiptype="fedexground";
			name=dao.getShippingType(countryid, shiptype);
			tempweight=weight/0.45f;
			if(!name.equals("0")){
				if(tempweight<=150){
					result=dao.getFedexground(name, (float)Math.ceil(tempweight));
				}else if(tempweight>150&&(singleweightmax/0.45)<150){
					//处理当总重量>150Lb, 而单件 最大重量<150Lb 时,重量单位是lb
					int i=(int) Math.floor((weight/0.45)/150);
					result=dao.getFedexground(name, 150)*i+dao.getFedexground(name, (float)Math.ceil(tempweight)-i*150);
				}
			}
			break;
		case 12://运费方式为ltlshipping
			shiptype="ltlshipping";
			/*name=dao.getShippingType(countryid, shiptype);
			tempweight=weight/0.45f;
			if(name.equals("eastofusa")){
				result=(float) (0.8*tempweight+286+200);
			}else if(name.equals("westofusa")){
				result=(float) (0.8*tempweight+150+200);
			}else if(name.equals("centralofusa")){
				result=(float) (0.8*tempweight+216+200);
			}*/
			result = (float)(weight * 0.8 + 400);
			break;
		case 13://运费方式为Port Pickup
			shiptype="portpickup";
			name=dao.getShippingType(countryid, shiptype);	
			if(!name.equals("0")){
				result=dao.getPortpickup(name, weight);
			}
			break;
		case 14://运费方式为Airport Pickup
			shiptype="airportpickup";
				result=dao.getAirportpickup(countryid, weight);
			break;
		case 15://运费方式为Aramex Mideast
			shiptype="aramexmideast";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getAramex(name,weight)*0.16f;
			}
			break;
		case 16://运费方式为DHL
			shiptype="dhl";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getDHL(name,weight)*0.16f;
			}
			break;
		case 17://运费方式为dhlfba其他几个国家
			shiptype="dhlfbaother";
			name=dao.getShippingType(countryid, shiptype);
			if(!name.equals("0")){
				result=dao.getDhlfbaother(name,weight)*0.16f;
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
	public List<ShippingBean> getShippingList(int countryid, float weight, float volume, float singleweightmax, int count) {
		// TODO Auto-generated method stub
		IZoneServer zs= new ZoneServer();
		List<ShippingBean> list=new ArrayList<ShippingBean>();
		ShippingBean sb[] = {
				new ShippingBean(1,"FedEx Economy","5-7",0,0),new ShippingBean(2,"China Post ePacket","10",0,0),new ShippingBean(3,"DHL-FBA","7-10",0,0),new ShippingBean(4,"China Post SAL","30",0,0),
				new ShippingBean(5,"China Post AIR","10-15",0,0),new ShippingBean(6,"Sweden Post","10-40",0,0),new ShippingBean(7,"Dutch post","10-20",0,0),new ShippingBean(8,"China Post Small Pak","15-20",0,0),
				new ShippingBean(9,"EMS","5",0,0),new ShippingBean(10,"China Post Express","10",0,0),new ShippingBean(11,"FedEx Ground","20-35",0,0),new ShippingBean(12,"LTL trucking","20-35",0,0),
				new ShippingBean(13,"","",0,0),new ShippingBean(14,"Airport Pickup","7",0,0),new ShippingBean(15,"Aramex MidEast","7",0,0),new ShippingBean(16,"DHL","3-5",0,0)
		};
		for(int i=0;i<count;i++){
			float temp=zs.getCost(countryid, weight, i+1,volume,singleweightmax)*1.05f;
			float temp1=zs.getCost(countryid, 2*weight, i+1,volume,singleweightmax)*1.05f;
			if(temp!=0){
				BigDecimal   b   =   new   BigDecimal(temp);
				float   f   =   b.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();
				BigDecimal   b1   =   new   BigDecimal(temp1);
				float   f1   =   b1.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();
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
	/*
	 *  (non-Javadoc)
	 * @see com.cbt.fee.service.IZoneServer#getShippingBeans(int, double[], double[], double[], java.lang.String[], int[])
	 * countryid：国家
	 * weights：重量
	 * prices：价格
	 * volumes：体积
	 * types类型：是否带电
	 * number：应该是产品数量
	 * 
	 */
	
	@Override
	public List<ShippingBean> getShippingBeans(int countryid, double[] weights, double[] prices,
                                               double[] volumes, String[] types, int[] number) {
		//该方法每次增加运输方式，保证最大重量低于1KG以下运输方式不为10的只能存在一个运输方式
//				String reString = "";
		
		//获取运输类别表,运输类别、ali类型ID、是否有电池---------表中需增加一列运输类别和ali类型ID为0的
		List<com.cbt.bean.TransitType> checkfreeTypes = dao.getCheckfreeType(types);
		List<TransitPricecost> transitPricecosts =  dao.getFreightFates(countryid);
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
			volumeweights += volumeweight; 
			sum_weights += weight;
			for (int j = 0; j < checkfreeTypes.size(); j++) {
				String checkfreeType = checkfreeTypes.get(j).getAliExpress_type();
				String battery = checkfreeTypes.get(j).getBattery();
				if(checkfreeType.equals(types[i])){
					if(battery.equals("Y")){
						isBattery = "Y";
					}
					 if(typeSplitPackgs.get(battery) == null){
						TypeSplitPackg typeSplitPackg = new TypeSplitPackg();
						typeSplitPackg.setPrice(price);
						typeSplitPackg.setWeight(weight);
						typeSplitPackg.setProduct_number(number[i]);
						typeSplitPackg.setVolumeweight(volumeweight);
						typeSplitPackg.setBattery(battery);
						typeSplitPackg.setPackage_number(1);
						typeSplitPackg.setPackage_avg(typeSplitPackg.getWeight());  
						typeSplitPackg.setWw_package(1);
						typeSplitPackg.setWw_avg(typeSplitPackg.getVolumeweight());
						typeSplitPackgs.put(battery, typeSplitPackg);
					 }else{
						TypeSplitPackg typeSplitPackg = typeSplitPackgs.get(battery);
						typeSplitPackg.setPrice(price+typeSplitPackg.getPrice());
						typeSplitPackg.setWeight(weight+typeSplitPackg.getWeight());
						typeSplitPackg.setProduct_number(number[i]+typeSplitPackg.getProduct_number());
						typeSplitPackg.setVolumeweight(volumeweight+typeSplitPackg.getVolumeweight());
						typeSplitPackg.setPackage_number(1);
						typeSplitPackg.setPackage_avg(typeSplitPackg.getWeight());  
						typeSplitPackg.setWw_package(1);
						typeSplitPackg.setWw_avg(typeSplitPackg.getVolumeweight());
					 }
					 break;
				}
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
				double package_freight = getFreight(transitPricecost_, typeSplitPackg);//运费
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
				double package_freight = getFreight(transitPricecost_, typeSplitPackg);//运费
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
		
		if(sum_weights >= 30){
			
			ShippingBean sb[] = {new ShippingBean(12,"LTL trucking","20-35",0,1),new ShippingBean(13,"","",0,1),new ShippingBean(14,"Airport Pickup","5-10",0,1)};
			for(int i=0;i<3;i++){
				float temp=getCost(countryid, sum_weights, i+12,(float)sum_weights,0f)*1.05f;
				if(temp!=0){
					BigDecimal   b   =   new   BigDecimal(temp);//运费
					float   f   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
					if(f == 0){
						continue;
					}
					sb[i].setResult(f);
					if(i==1){
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
						sb[i].setFeight(f);//运费
						sb[i].setVolumeweights(volumeweights);//总体积重
						if(shippingBeans.size()==0 || (shippingBeans.size() > 0 && shippingBeans.get(0).getResult() > sb[i].getResult())){
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
		/*if(shippingBeans.size() > 0 && isBattery.equals("N")){
			double result = shippingBeans.get(0).getResult();
			if(typeSplitPackgs.get("N").getVolumeweight() < 1.7 ){
				ShippingBean shippingBean59 = new ShippingBean();
				shippingBean59.setBattery(shippingBeans.get(0).getBattery());
				shippingBean59.setDays("5-9");
				shippingBean59.setName("59");
				shippingBean59.setResult(new BigDecimal(result + result * 0.4).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				shippingBean59.setResult1(1);
				shippingBean59.setFeight(result);

				ShippingBean shippingBean35 = new ShippingBean();
				shippingBean35.setBattery(shippingBeans.get(0).getBattery());
				shippingBean35.setDays("3-5");
				shippingBean35.setName("35");
				shippingBean35.setResult(new BigDecimal(shippingBeans.get(0).getResult() * 2).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				shippingBean35.setResult1(1);
				shippingBean35.setFeight(result);
				shippingBeans.add(shippingBean59);
				shippingBeans.add(shippingBean35);
			}else {
				ShippingBean shippingBean35 = new ShippingBean();
				shippingBean35.setBattery(shippingBeans.get(0).getBattery());
				shippingBean35.setDays("3-5");
				shippingBean35.setName("35");
				shippingBean35.setResult(new BigDecimal(result + 10 + (result * 0.3)).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				shippingBean35.setResult1(1);
				shippingBean35.setFeight(result);
				shippingBeans.add(shippingBean35);
			}
		}
		Collections.sort(shippingBeans, new ShipComparator());
		*/
		
		return shippingBeans;
 
	}
	
	/**
	 *计算运费
	 * @param weight重量
	 * @param freight上一次的运费
	 * @param transitPricecost
	 * @return
	 */
	private double getFreight(TransitPricecost transitPricecost_, TypeSplitPackg typeSplitPackg){
		double divisionweight = transitPricecost_.getDivisionweight();
		double under = transitPricecost_.getUnder();
		double over = transitPricecost_.getOver();
		double minweight = transitPricecost_.getMinweight();
		double maxweight = transitPricecost_.getMaxweight();
		double package_vw = typeSplitPackg.getWw_avg();
		if(minweight > package_vw || package_vw > maxweight)return 0;
		double package_freight = 0;//运费
		if(minweight > package_vw || package_vw > maxweight)return 0;
		if(divisionweight < package_vw){
			//运费 = 首重价 + （（总重量-整除重量） 整除   整除重量 +1） * 续重价
			package_freight = under + Math.ceil((package_vw-divisionweight)/divisionweight)*over;
		}else{
			package_freight = under;
		}
		package_freight = new BigDecimal(package_freight * typeSplitPackg.getPackage_number() / Util.EXCHANGE_RATE).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
		 
		return package_freight;
	}
	
	//获取不拆包运费
	private static LinkedHashMap<String, Object[]> getBatty(Map<String, Object[]> battery_splits,LinkedHashMap<String, List<Object[]>> freightFates){
		LinkedHashMap<String, Object[]> results_battery_n = new LinkedHashMap<String, Object[]>();
		LinkedHashMap<String, Object[]> results_battery_y = new LinkedHashMap<String, Object[]>();
		for (String key : battery_splits.keySet()) {
			double package_weight = (Double) battery_splits.get(key)[1];
			double package_vw = (Double) battery_splits.get(key)[3];
			double package_hl_vw = (Double) battery_splits.get(key)[4];//2KG包裹平均重量
			int package_hl_number = (Integer) battery_splits.get(key)[5];//2KG包裹数量
			List<Object[]> list_ff = freightFates.get(key);
			if(list_ff != null && list_ff.size() > 0){
				for (int k = 0; k < list_ff.size(); k++) {
					int days = (Integer) list_ff.get(k)[1];
					double divisionweight = (Double) list_ff.get(k)[4];
					double under = (Double) list_ff.get(k)[2];
					double over = (Double) list_ff.get(k)[3];
					//首重价格 + 向上取整（总重量 - 首重）*续重价格
					String shippingmethod = (String) list_ff.get(k)[0];
					double minweight = (Double) list_ff.get(k)[6];
					double maxweight = (Double)  list_ff.get(k)[7];
					double package_freight = 0;
					if(shippingmethod.equals("SLL") || shippingmethod.equals("SAL")){
						if(minweight < package_weight && package_weight < maxweight)continue;
						package_freight = under + Math.ceil(((package_weight)-divisionweight))*over;//运费
					}else{
						if(minweight > package_vw || package_vw > maxweight)continue;
						package_freight = under + Math.ceil(((package_vw)-divisionweight))*over;//运费
					}
					//同一个时间点，取最便宜的运费
					if(key.equals("battery_N")){
						Object[] result_battery = results_battery_n.get("result"+days);
						if(result_battery == null){
							results_battery_n.put("result"+days, new Object[]{days,shippingmethod,package_freight,1,1});
						}else{
							double pachkege_freight_map = (Double)results_battery_n.get("result"+days)[2];
							if(pachkege_freight_map > package_freight){
								results_battery_n.get("result"+days)[2] = (Double)results_battery_n.get("result"+days)[2];
							}
						}
					}else{
						Object[] result_battery = results_battery_y.get("result"+days);
						if(result_battery == null){
							results_battery_y.put("result"+days, new Object[]{days,shippingmethod,package_freight,1,1});
						}else{
							double pachkege_freight_map = (Double)results_battery_y.get("result"+days)[2];
							if(pachkege_freight_map > package_freight){
								results_battery_y.get("result"+days)[2] = (Double)results_battery_y.get("result"+days)[2];
							}
						}
					}
					
				}
			}
			//30天的并且带电的，使用2KG重量拆包结果
			List<Object[]> list_ff_hl = freightFates.get("battery2_Y");
			if(list_ff_hl != null){
				int days = (Integer) list_ff_hl.get(0)[1];
				String battery = (String) list_ff_hl.get(0)[5];
				if(days == 30 && "Y".equals(battery)){
					if(package_hl_vw != 0){
						double package_freight = (Double) list_ff_hl.get(0)[2] + Math.ceil((package_hl_vw)-(Double) list_ff_hl.get(0)[4])*(Double) list_ff_hl.get(0)[3];//运费
						results_battery_y.put("result30", new Object[]{days,"method30",package_freight*package_hl_number,package_hl_number,1});
					}
				}
			}
		}
		if(results_battery_y.size() > 0){
			for (String key : results_battery_n.keySet()) {
				int days = (Integer)results_battery_n.get(key)[0];
				for (String keyn : results_battery_y.keySet()) {
					int daysy = (Integer)results_battery_y.get(keyn)[0];
					if(daysy == days){
						results_battery_n.get("result"+days)[2] = (Double)results_battery_y.get("result"+days)[2] + (Double)results_battery_n.get("result"+days)[2];
						break;
					}
				}
			}
		}
		if(results_battery_n.size() == 0 && results_battery_y.size() > 0){
			results_battery_n = results_battery_y;
		}
		
		return results_battery_n;
	}
	
	@Override
	public void copyPricecost(){
		IZoneDao dao = new ZoneDao();
		//查询国家表
		//10天时间段的每个重量存在多个
		List<ZoneBean> zoneBeans = dao.getAllZone();
		Map<String, List<TransitPricecost>> transitList = dao.getTransitPricecost();
		List<TransitPricecost> saveTransitPricecosts = new ArrayList<TransitPricecost>();
		for (int i = 0; i < zoneBeans.size(); i++) {
			int countryid = zoneBeans.get(i).getId();
			if(countryid == 43)countryid=36;
			List<TransitPricecost> transitPricecosts = transitList.get("countryid"+countryid);
			if(transitPricecosts != null){
				
			for (int j2 = 1; j2 < 101; j2++) {
//					double freight = 0;
//					int days = 0;//速度档次
//					String battery = "";//电池
					List<TransitPricecost> list10 = new ArrayList<TransitPricecost>();
					for (int j = 0; j < transitPricecosts.size(); j++) {
						TransitPricecost transitPricecost = transitPricecosts.get(j);
						if(j2 < transitPricecost.getMinweight()){
							continue;
						}else if(j2 > transitPricecost.getMaxweight()){
							continue;
						}
						TransitPricecost transitPricecost_ = new TransitPricecost();
						transitPricecost_.setBattery(transitPricecost.getBattery());
						transitPricecost_.setCountryId(transitPricecost.getCountryId());
						transitPricecost_.setDays(transitPricecost.getDays());
						transitPricecost_.setDelivery_time(transitPricecost.getDelivery_time());
						transitPricecost_.setDivisionweight(transitPricecost.getDivisionweight());
						transitPricecost_.setWeight(j2);
						transitPricecost_.setOver(transitPricecost.getOver());
						transitPricecost_.setUnder(transitPricecost.getUnder());
						transitPricecost_.setShippingmethod(transitPricecost.getShippingmethod());
						transitPricecost_.setCountrycname(transitPricecost.getCountrycname());
						transitPricecost_.setShippingmethod_en(transitPricecost.getShippingmethod_en());
						transitPricecost_.setCountryname(transitPricecost.getCountryname());
						transitPricecost_.setMaxweight(transitPricecost.getMaxweight());
						transitPricecost_.setMinweight(transitPricecost.getMinweight());
						int days_ = transitPricecost.getDays();
						double divisionweight = transitPricecost.getDivisionweight();
						double freight_ = 0;
						String battery_ = transitPricecost.getBattery();
						String name_ = transitPricecost.getShippingmethod();
						if(j2 > divisionweight){
							freight_ = transitPricecost.getUnder()+Math.ceil((j2-divisionweight)/divisionweight)*transitPricecost.getOver();
						}else{
							freight_ = transitPricecost.getUnder();
						}
						transitPricecost_.setFreight(freight_);
						if(days_ != 10 || battery_.equals("Y")){
							if(saveTransitPricecosts.size() !=0 && days_ == saveTransitPricecosts.get(saveTransitPricecosts.size()-1).getDays() && battery_.equals(saveTransitPricecosts.get(saveTransitPricecosts.size()-1).getBattery())){
								if(freight_ < saveTransitPricecosts.get(saveTransitPricecosts.size()-1).getFreight() && freight_ !=0 ){
									saveTransitPricecosts.remove(saveTransitPricecosts.size()-1);
									saveTransitPricecosts.add(transitPricecost_);
								}
							}else{
								saveTransitPricecosts.add(transitPricecost_);
							}
						}else{
							if(list10.size() !=0 &&Utility.getStringIsNull(name_) &&name_.equals(list10.get(list10.size()-1).getShippingmethod())){
								if(list10.get(list10.size()-1).getFreight() > freight_ && freight_ !=0 ){
									list10.remove(list10.size()-1);
									list10.add(transitPricecost_);
								}
							}else{
								list10.add(transitPricecost_);
							}
						}
					}
					Collections.sort(list10, new ShipComparator());
					int list10size = list10.size() > 3 ? 3 : list10.size();
					for (int j = 0; j < list10size; j++) {
						saveTransitPricecosts.add(list10.get(j));
					}
				}
				
			for (int j = 0; j < transitPricecosts.size(); j++) {
				if(transitPricecosts.get(j).getMaxweight()<1){
					TransitPricecost transitPricecost = transitPricecosts.get(j);
					TransitPricecost transitPricecost_ = new TransitPricecost();
					transitPricecost_.setBattery(transitPricecost.getBattery());
					transitPricecost_.setCountryId(transitPricecost.getCountryId());
					transitPricecost_.setDays(transitPricecost.getDays());
					transitPricecost_.setDelivery_time(transitPricecost.getDelivery_time());
					transitPricecost_.setDivisionweight(transitPricecost.getDivisionweight());
					transitPricecost_.setWeight(1);
					transitPricecost_.setOver(transitPricecost.getOver());
					transitPricecost_.setUnder(transitPricecost.getUnder());
					transitPricecost_.setShippingmethod(transitPricecost.getShippingmethod());
					transitPricecost_.setCountrycname(transitPricecost.getCountrycname());
					transitPricecost_.setShippingmethod_en(transitPricecost.getShippingmethod_en());
					transitPricecost_.setCountryname(transitPricecost.getCountryname());
					transitPricecost_.setMaxweight(transitPricecost.getMaxweight());
					transitPricecost_.setMinweight(transitPricecost.getMinweight());
					saveTransitPricecosts.add(transitPricecost_);
				}
			}
			
			}
		}
		dao.addTransitPricecost(saveTransitPricecosts);
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
