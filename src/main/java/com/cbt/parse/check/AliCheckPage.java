package com.cbt.parse.check;

import com.cbt.parse.service.DownloadMain;
import com.cbt.processes.service.SendEmail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

public class AliCheckPage {
	
	public static void Check(){
		boolean checkAll = checkWholesaleAll();
		System.out.println("checkAll: "+checkAll);
		if(checkAll){
			StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
	        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear ,</div><div style='font-size: 13px;'>"); 
	        sb.append("The wholesale page may be changed ,please check it</div>"); 
	        sb.append("<div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>"); 
	        SendEmail.send(null,null,"sunny517309@163.com", sb.toString(),"ImportExpress","", 1);
		}
		boolean ckeck = checkAlibabaAll();
		System.out.println("ckeck: "+ckeck);
		if(ckeck){
			StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
			sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear ,</div><div style='font-size: 13px;'>"); 
			sb.append("The alibaba page may be changed ,please check it</div>"); 
			sb.append("<div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>"); 
			SendEmail.send(null,null,"sunny517309@163.com", sb.toString(),"ImportExpress","", 1);
		}
		
		boolean chek = checkAliespressAll();
		System.out.println("chek: "+chek);
		if(chek){
			StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
			sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear ,</div><div style='font-size: 13px;'>"); 
			sb.append("The aliexpress page may be changed ,please check it</div>"); 
			sb.append("<div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>"); 
			SendEmail.send(null,null,"sunny517309@163.com", sb.toString(),"ImportExpress","", 1);
		}
		
	}
	
	private static boolean checkAliespressAll(){
		String url = "http://www.aliexpress.com/item/Jacket-Coat-2015-Women-Candy-Coat-Jacket-One-Button-Outerwear-Coat-Jacket-Tops-Blaser-Basic-Jackets/32345912939.html";
		String url1 = "http://www.aliexpress.com/item/High-Power-48-LED-Bulb-Desk-lamp-nature-white-reading-table-lamp-LED-table-lamp-for/32260496032.html";
		String url2 = "http://www.aliexpress.com/item/Premium-28-Years-Old-Chinese-Ripe-Pu-er-tea-Yunnan-Puer-tea-250g-China-Organic-Puerh/2012549625.html";
		String ch1 = checkAliexpress(url);
		String ch2 = checkAliexpress(url1);
		String ch3 = checkAliexpress(url2);
		if(ch1!=null&&ch2!=null&&ch3!=null){
			if(ch1.equals(ch2)&&ch2.equals(ch3)){
				return false;
			}else{
				return true;
			}
		}else{
			if(ch1!=null&&ch2!=null&&ch1.equals(ch2)){
				return false;
			}else if(ch1!=null&&ch3!=null&&ch1.equals(ch3)){
				return false;
			}else if(ch2!=null&&ch3!=null&&ch2.equals(ch3)){
				return false;
			}else{
				return true;
			}
		}
	}
	private static boolean checkAlibabaAll(){
		String url = "http://www.alibaba.com/product-detail/NK-series-hot-selling-office-desk_60193960157.html";
		String url1 = "http://www.alibaba.com/product-detail/original-smartphone-4-7-inch-QHD_60172276965.html";
		String url2 = "http://www.alibaba.com/product-detail/Women-s-jacket-Classic-Fashion-Pink_2010216903.html";
		String ch1 = checkAlibaba(url);
		String ch2 = checkAlibaba(url1);
		String ch3 = checkAlibaba(url2);
		if(ch1!=null&&ch2!=null&&ch3!=null){
			if(ch1.equals(ch2)&&ch2.equals(ch3)){
				return false;
			}else{
				return true;
			}
		}else{
			if(ch1!=null&&ch2!=null&&ch1.equals(ch2)){
				return false;
			}else if(ch1!=null&&ch3!=null&&ch1.equals(ch3)){
				return false;
			}else if(ch2!=null&&ch3!=null&&ch2.equals(ch3)){
				return false;
			}else{
				return true;
			}
		}
	}
	private static boolean checkWholesaleAll(){
		String url = "http://www.alibaba.com/product-detail/Solid-Wood-Computer-Desk_563901652.html";
		String url1 = "http://www.alibaba.com/product-detail/3D-Embroidery-Snapback-Caps-and-Hats_1409166436.html";
		String url2 = "http://www.alibaba.com/product-detail/Hot-sale-Inew-V3-MTK6582-Quad_1970052091.html";
		String ch1 = checkWholesale(url);
		String ch2 = checkWholesale(url1);
		String ch3 = checkWholesale(url2);
		if(ch1!=null&&ch2!=null&&ch3!=null){
			if(ch1.equals(ch2)&&ch2.equals(ch3)){
				return false;
			}else{
				return true;
			}
		}else{
			if(ch1!=null&&ch2!=null&&ch1.equals(ch2)){
				return false;
			}else if(ch1!=null&&ch3!=null&&ch1.equals(ch3)){
				return false;
			}else if(ch2!=null&&ch3!=null&&ch2.equals(ch3)){
				return false;
			}else{
				return true;
			}
		}
	}
	
	
	
	private static String checkWholesale(String url){
		String page = DownloadMain.getJsoup(url, 1,null);
		String result = "";
		if(page!=null){
			Document doc = Jsoup.parse(page);
			//获取element
			Element body = doc.body();
			int ll = body.select("div[class=grid grid-c2-e5 ls-grid-bcl]")
						 .select("div[class=ui-breadcrumb]").size();
			if(ll==1){
				result +="Category--";
				int siz = body.select("div[class=brief-info]").select("dl").size();
				if(siz>1){
					result +="Price+Minorder--";
					int size = body.select("div[class=operation-wraper]").select("dl[class=time util-clearfix]").size();
					if(size==1){
						result +="Processing Time--";
						int img = body.select("ul[class=inav util-clearfix]").size();
						if(img == 1){
							result +="image--";
							int detail = body.select("table[class=table quick-detail]").size();
							if(detail==1){
								result +="Detail--";
								int info = body.select("div[id=J-product-detail]").select("div[class=box]").size();
								if(info==2){
									result +="Info";
								}
							}
						}
					}
				}
			}
		}else{
			result = null;
		}
		page = null;
		return result;
	}
	private static String checkAlibaba(String url){
		String page = DownloadMain.getJsoup(url, 1,null);
		String result = "";
		if(page!=null){
			Document doc = Jsoup.parse(page);
			//获取element
			Element body = doc.body();
			int ll = body.select("div[class=grid grid-c2-e5 ls-grid-bcl]")
					.select("div[class=ui-breadcrumb]").size();
			if(ll==1){
				result +="Category--";
				int siz = body.select("div[class=ls-icon ls-brief]").select("table").size();
				if(siz==1){
					result +="Price+Minorder--";
					int img = body.select("div[class=ione J-item]").size();
					if(img == 1){
						result +="image--";
						int detail = body.select("div[class=box box-first]").size();
						if(detail>0){
							result +="Detail--";
							int info = body.select("div[id=J-product-detail]").select("div[class=box]").size();
							if(info==2){
								result +="Info";
							}
						}
					}
				}
			}
		}else{
			result = null;
		}
		page = null;
		return result;
	}
	private static String checkAliexpress(String url){
		String page = DownloadMain.getJsoup(url, 1,null);
		String result = "";
		if(page!=null){
			Document doc = Jsoup.parse(page);
			//获取element
			Element body = doc.body();
			int ll = body.select("div[class=detail-page]")
					.select("div[class=ui-breadcrumb]").size();
			if(ll==1){
				result +="Category--";
				int siz = body.select("div[id=product-info-price-pnl]").select("dl").size();
				if(siz>0){
					result +="Price+Minorder--";
					int img = body.select("div[id=img]").size();
					if(img == 1){
						result +="image--";
						int detail = body.select("div[class=product-desc]").size();
						if(detail>0){
							result +="Detail--";
							 String info =  DownloadMain.getSpiderContext(body.select("script").toString(), "(?:descUrl=\")(.*?)(?:\";)");
							if(Pattern.compile("(http://desc.aliexpress.com/getDescModuleAjax.htm?.+)").matcher(info).matches()){
								result +="Info";
							}
						}
					}
				}
			}
		}else{
			result = null;
		}
		page = null;
		return result;
	}

}
