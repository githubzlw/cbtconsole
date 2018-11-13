package com.cbt.FreightFee.service.impl;

import com.cbt.FreightFee.dao.FreightFeeMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FreightData {
	@Autowired
	private static FreightFeeMapper freightFeeMapper;
	static String JsonStr = "{新西兰:["
	            +"{0.5:'172',1:'156',1.5:'156',2:'172',2.5:'199',3:204,3.5:'221',4:'238',4.5:'255',5:'272',5.5:303,6:'323',6.5:'343',7:'364',7.5:'384',8:'404',8.5:'424',9:'444',9.5:'464',10:'484',10.5:'512',11:'530',11.5:'549',12:'567',12.5:'586',13:'604',13.5:'623',14:'641',14.5:'660',15:'789',15.5:'697',16:'715',16.5:'734',17:'752',17.5:'771',18:'789',18.5:'808',19:'826',19.5:'845',20:'863',20.5:'882'}"
	            +"],加拿大:["
	            +"{0.5:'129',1:'129',1.5:'141',2:'168',2.5:'195',3:'231',3.5:'254',4:'277',4.5:'300',5:'323',5.5:'361',6:'388',6.5:'415',7:'442',7.5:'469',8:'496',8.5:'522',9:'549',9.5:'576',10:'603',10.5:'617',11:'640',11.5:'664',12:'687',12.5:'710',13:'734',13.5:'757',14:'780',14.5:'804',15:'827',15.5:'851',16:'874',16.5:'897',17:'921',17.5:'944',18:'967',18.5:'991',19:'1014',19.5:'1037',20:'1061',20.5:'1084'}"
	            +"]}";
	public static double getFreight(String countryId,String weight){
		String freight="0.00";
		try{
			JSONObject json = new JSONObject(JsonStr); 
			JSONArray jsonArray =json.getJSONArray(countryId);
			freight=jsonArray.getJSONObject(0).getString(weight);
		}catch(Exception e){
			freight="0.00";
		}
		return Double.valueOf(freight);
	}

}
