package com.cbt.processes.utils;

import com.cbt.util.Utility;

import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processes {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(Processes.class);

	//价格根据数据改变
	public static String getWPrice(String wprice, int number, double mm, double feeprice_cost){
		//wprice = "[1 - 2 $135.87, 3 - 4 $130.9, 5 - 19 $115.19, 20 + $108.91]";
		String temp = wprice.replace("[", "").replace("]", "").replaceAll(" ", "");
		String[] wp = temp.split(",");
		String priceString = "0.0";
		try {
			DecimalFormat df = new DecimalFormat("#0.##");
			for (int i = 0; i < wp.length; i++) {
				String qj = wp[i].split("\\$")[0];
				int min = 1;int max = 1;
				if(qj.indexOf("-") > -1){
					min = Integer.parseInt(qj.split("-")[0]);
				    max = Integer.parseInt(qj.split("-")[1]);
				}else{
					String regEx="[^0-9]";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(qj);   
					min = Integer.parseInt(m.replaceAll("").trim());  
					max = min;
				}
				if(min > number){
					return df.format((Double.parseDouble(wp[i].split("\\$")[1])-feeprice_cost)*mm);
				}
				if(min <= number && max >= number){
					return df.format((Double.parseDouble(wp[i].split("\\$")[1])-feeprice_cost)*mm);
				}
			}
			priceString =  wp[wp.length-1].split("\\$")[1];
			priceString = df.format((Double.parseDouble(priceString)-feeprice_cost)*mm);
		} catch (Exception e) {
			LOG.error("",e);
			e.printStackTrace();
		}
		return priceString;
	}
	
	//免邮价格变非免邮价格
	public static String getFreeToFreight(String price,double ex,String feeprice, int is1688, String wprice, int number){
		DecimalFormat    df   = new DecimalFormat("######0.00");
		double freeprice = Double.parseDouble(price); 
		double freePrice_Cost = Double.parseDouble(feeprice);
		if(is1688 == 1){
			if(Utility.getStringIsNull(wprice)){
				return getWPrice(wprice, number, ex, freePrice_Cost);
			}
			return df.format(freeprice - freePrice_Cost);
		}else{
			if(Utility.getStringIsNull(wprice)){
				freeprice = Double.parseDouble(getWPrice(wprice, number, ex, freePrice_Cost));
			}
			freePrice_Cost  = freePrice_Cost * ex;
			double fp =  freeprice*0.25;//如果 免邮 运费> 大于产品价格的 25%， 就只减去产品价格的 25% 作为 非免邮价格
			if(freePrice_Cost > fp){
				price = df.format(freeprice - fp);
			}else{
				price = df.format(freeprice - freePrice_Cost*0.7);
			}
		}
		return price;
	}
	
	//非免邮变免邮价格
	public static String getFreightToFree(String price,double ex,String feeprice, int is1688, String wprice, int number){
		DecimalFormat    df   = new DecimalFormat("######0.00");
		double price_ = Double.parseDouble(price);
		double freePrice_Cost = Double.parseDouble(feeprice);
		if(is1688 == 1){
			if(Utility.getStringIsNull(wprice)){
				return getWPrice(wprice, number, ex, 0);
			}
			return df.format(price_ + freePrice_Cost);
		}else{
			freePrice_Cost  = freePrice_Cost * ex;
			double free_price = price_/0.75;
			double fp = free_price*0.25;
			if(free_price > fp){
				price = df.format(free_price);
			}else{
				price = df.format(free_price*0.7 + price_);
			}
		}
		return price;
	}
}
