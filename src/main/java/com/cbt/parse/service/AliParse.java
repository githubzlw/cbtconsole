package com.cbt.parse.service;

import com.cbt.parse.bean.TypeBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Alibaba
 * @author abc
 *
 */
public class AliParse {
	private  ArrayList<String> pImage = new ArrayList<String>();//图片
	private  HashMap<String, String> pInfo = new HashMap<String, String>();//信息
	private  ArrayList<String> price = new ArrayList<String>();
	private  ArrayList<TypeBean> types = new ArrayList<TypeBean>();//颜色 尺寸
	
	/**
	 * 商品信息							
	 * 匹配  url = http://detail.1688.com/offer/37351531817.html
	 * @param body
	 * @throws Throwable 
	 */
	public  GoodsBean getType16(GoodsBean p,Element body)
	{
		//1.名称
		String pName = productName(body, "div[class=mod-detail-title]");
		pName = pName.isEmpty()?productName(body, "h1[class=d-title]"):pName;
		p.setpName(pName);
		pName = null;
		
		String script = body.select("script").toString();
		String catid1 = StrUtils.matchStr(script, "dcatid':'\\d+'")
									.replaceAll("\\D+", "").trim();
		String catid2 = StrUtils.matchStr(script, "parentdcatid':'\\d+'")
									.replaceAll("\\D+", "").trim();
		p.setCatid1(catid1);
		p.setCatid2(catid2);
		
		//2.id
		String pID = body.select("input[id=offer_video_refKey]").attr("value")
						 .replaceAll("\\D+", "");
		p.setpID(pID);
		
		//4.图片nav nav-tabs fd-clr
		Elements productImag = body.select("div[id=dt-tab]").select("div[class=vertical-img]").select("img"); 
        int imgsize = productImag.size();
        String ig = null;
    	for(int i=0;i<imgsize;i++)
    	{
    		if( i > 5 )
    		{
    			ig = productImag.get(i).attr("data-lazy-src");
    			if(ig!=null&&!ig.isEmpty())
    			{
    				pImage.add(ig);
    			}
    		}
    		else
    		{
    			ig = productImag.get(i).attr("src");
    			if(ig!=null&&!ig.isEmpty())
    			{
    				pImage.add(ig);
    			}
    		}
    		ig = null;
    	}
    	
        int pimgs = pImage.size();
		if(pimgs > 0 &&( pImage.get(0).indexOf("32x32.jpg") > 0 
		   || pImage.get(0).indexOf("60x60.jpg") > 0))
		{
			String[] imgSize = {"32x32.jpg","400x400.jpg"};
			p.setImgSize(imgSize);
			imgSize = null;
		}
		String img = null;
		for(int i=0;i<pimgs;i++)
		{
			if(pImage.get(i).indexOf("32x32.jpg") > 0)
			{
				img = pImage.get(i).substring(0, pImage.get(i).indexOf("32x32.jpg"));
				if( img != null && !img.isEmpty() )
				{
					pImage.set(i, img);
				}
			}
			if(pImage.get(i).indexOf("60x60.jpg") > 0)
			{
				img = pImage.get(i).substring(0, pImage.get(i).indexOf("60x60.jpg"));
				if( img != null && !img.isEmpty() )
				{
					pImage.set(i, img);
				}
			}
			img = null;
		}
		
		p.setpImage(pImage);
		
		//规格
		Elements block1 = body.select("div[class=obj-leading]");
		Elements block2 = body.select("div[class=obj-sku]");
		if(block1 != null && block2 != null && !block2.isEmpty())
		{
			block1.addAll(block2);
		}
		if(block1 != null && !block1.isEmpty())
		{
			TypeBean bean = null;
			String value = null;
			String color_img = null;
			String color_id = null;
			Date date = new Date();
			String type_name = null;
			for(int i=0;i<block1.size();i++)
			{
				Elements dt = block1.get(i).select("span[class=obj-title]");
				Elements bs = block1.get(i).select("li");
				bs = bs.isEmpty()?block1.get(i).select("td[class=name]"):bs;
				for(int j=0;j<bs.size();j++)
				{
					bean = new TypeBean();
					if(!Pattern.compile("(ships.*from)").matcher(dt.text().toLowerCase()).find())
					{
						value = bs.get(j).text();
						value = value.isEmpty()?bs.get(j).attr("title"):value;
						color_img = bs.get(j).select("img").attr("data-lazy-src");
						color_img = color_img.isEmpty()?bs.get(j).select("img").attr("src"):color_img;
						color_img = color_img.replaceAll("\\d+x\\d+\\.jpg", "400x400.jpg");
						color_id =date.getYear()+""+date.getMonth()+""+date.getDate()+j;
						type_name = dt.text();
						type_name = type_name.replaceAll("\\s+", "").trim();
						bean.setType(type_name);
						bean.setValue(value);
						bean.setImg(color_img);
						bean.setId(color_id.trim());
						types.add(bean);
					}
				}
			}
		}
		p.setType(types);
		
			
		//7.价格
		Elements prices = body.select("div[class=d-content]").select("tr[class=price]").select("td");
		Elements punits = body.select("span[class=fd-cny]");
		String pUnit = punits.size() > 0 ? punits.get(0).text() : "RMB";
		Elements qunits = body.select("span[class=unit]");
		String qunit = qunits.size() > 0 ? qunits.get(0).text() : "piece";
		
		int price_size = prices.size();
		int morder = 0;
		if(price_size>0)
		{
			String bamount = null;
			String eamount = null;
			String mprice = null;
			String mprice_amount = null;
			String price_m = null;
			String mount_m = null;
		   for(int i=0;i<price_size;i++)
		   {
			   mprice_amount = prices.get(i).attr("data-range");
			   if(!mprice_amount.isEmpty())
			   {
				   bamount = StrUtils.matchStr(mprice_amount, "begin\":\"\\d+").replaceAll("\\D+", "").trim();
				   eamount = StrUtils.matchStr(mprice_amount, "end\":\"\\d+").replaceAll("\\D+", "").trim();
				   mprice = StrUtils.matchStr(mprice_amount, "price\":\"\\d+\\.*\\d*").trim();
				   mprice = StrUtils.matchStr(mprice, "\\d+\\.*\\d*").trim();
				   if(!bamount.isEmpty()&&!eamount.isEmpty())
				   {
					   if(morder==0)
					   {
						   morder = Integer.valueOf(bamount);
					   }
					   else if(morder>Integer.valueOf(bamount))
					   {
						   morder = Integer.valueOf(bamount);
					   }
					   price.add(bamount+"-"+eamount+pUnit+mprice);
				   }
				   else if(!bamount.isEmpty())
				   {
					   if(morder==0)
					   {
						   morder = Integer.valueOf(bamount);
					   }
					   else if(morder>Integer.valueOf(bamount))
					   {
						   morder = Integer.valueOf(bamount);
					   }
					   price.add("≥"+bamount+pUnit+mprice);
				   }
				   bamount = null;
				   eamount = null;
				   mprice = null;
				   mprice_amount = null;
			   }
			   else
			   {
				   if(i!=0)
				   {
					   continue;
				   }
				   Elements select = body.select("div[class=price-original-sku]").select("span[class=value]");
				   Elements mounts = body.select("div[class=d-content]").select("tr[class=amount]");
				   if(mounts.size()==0||select.size()==0)
				   {
					   continue;
				   }
				   price_m =select.get(0).text();
				   mount_m = mounts.get(0).select("span[class=value]").text().replaceAll("\\D+", "").trim();
				   if(price_m!=null&&mount_m!=null&&!mount_m.isEmpty()&&!price_m.isEmpty())
				   {
					   price.add("≥"+mount_m+pUnit+price_m);
				   }
				   price_m = null;
				   mount_m = null;
			   }
		   }
		}
		p.setpWprice(price);
		p.setpPriceUnit(pUnit); 
		p.setpGoodsUnit(qunit);
		
		prices = null;
		pUnit = null;
		qunit = null;
		
		//8.最小订量
		morder = morder==0?1:morder;
		p.setMinOrder(""+morder);
		p.setSell(body.select("p[class=bargain-number]").select("em[class=value]").text());
		
		//11.卖家id
		String sID =  body.select("input[id=complaintOb]").attr("value");
		p.setsID(sID);
		sID = null;
		//13.详细信息
		String sprice = "";
		Elements de = body.select("div[id=mod-detail-attributes]");
		Elements detail_name = de.size()>0?de.get(0).select("td[class=de-feature]"):null;
		Elements detail_data = de.size()>0?de.get(0).select("td[class=de-value]"):null;
		if(detail_name!=null&&detail_data!=null)
		{
			int data_size =  detail_data.size();
			for(int i=0;i<data_size;i++)
			{
				if(detail_name.get(i).text().isEmpty()||
						detail_data.get(i).text().isEmpty())
				{
					continue;
				}
				pInfo.put(i+"", detail_name.get(i).text()+":"+detail_data.get(i).text());
				if(StrUtils.isFind(detail_name.get(i).text(), "(零售价)")){
					sprice = StrUtils.matchStr(detail_data.get(i).text(), "(\\d+\\.*\\d*)");
				}
			}
		}
		if(sprice.isEmpty()&&!price.isEmpty()){
			sprice = StrUtils.matchStr(price.get(0), "(\\d+\\.\\d+)");
		}
		sprice = sprice.isEmpty()?"0":sprice;
		
		DecimalFormat format = new DecimalFormat("#0.00");
		p.setpSprice(format.format(Double.valueOf(sprice)*1.3));
		p.setpInfo(pInfo);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		de = null;
		detail_name = null;
		detail_data = null;
		
		String info_url = body.select("div[id=desc-lazyload-container]").attr("data-tfs-url");
		info_url = info_url.replace("|", "?&");
		info_url = info_url.replace("^", "=");
		info_url = info_url.replace(";", "&");
		p.setInfourl(info_url);
		
		if(!info_url.isEmpty()){
			String page = DownloadMain.getContentClient(info_url,null);
			page = page.replace("var desc='", "").replace("</p>';", "</p>");
			Element body2 = Jsoup.parse(page).body();
			Elements imgs = body2.select("img");
			Elements img1 = body2.select("table").select("img");
			for(int i=0;i<img1.size();i++){
				imgs.remove(img1.get(i));
			}
			Elements img2 = body2.select("ul").select("img");
			for(int i=0;i<img2.size();i++){
				imgs.remove(img2.get(i));
			}
			Elements img3 = body2.select("li").select("img");
			for(int i=0;i<img3.size();i++){
				imgs.remove(img3.get(i));
			}
			Elements img4 = body2.select("ul[class=attributes-list]").select("img");
			for(int i=0;i<img4.size();i++){
				imgs.add(img4.get(i));
			}
			if(imgs.isEmpty()){
				imgs.addAll(img1);
			}
			p.setInfo_ori(imgs.toString().replace("&quot;", "").replaceAll("\\\\\"", "\"").replaceAll("\\\\", ""));
			String weight = StrUtils.matchStr(body2.text(), "(重量是\\s*\\d+\\.*\\d*\\s*([gG][kK]))");
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(商品重量\\s*\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(重量：约\\s*\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(重量：\\s*\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(重量\\s*\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(净重量.*\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(净重.*\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(重量\\s*s\\d+\\.*\\d*)");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(重量.*\\s*\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(重量.*\\d+\\.*\\d*)");
			}
			if(weight.isEmpty())
			{
				weight = StrUtils.matchStr(body2.text(), "(&nbsp;\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			}
			if(Pattern.compile("\\d+\\.\\d+[gG][kK]").matcher(weight).find()){
				weight = weight.replaceAll("[gG][kK]", "kg");
			}
			String tem_weight = weight;
			weight = StrUtils.matchStr(tem_weight, "(\\d+\\.*\\d*\\s*(([Kk][gG])|([gG])))");
			weight = weight.isEmpty()?StrUtils.matchStr(tem_weight, "(\\d+\\.*\\d*\\s*(([Kk][gG])|([gG]))*)"):weight;
			if(Pattern.compile("\\d+\\.\\d+").matcher(weight).matches())
			{
				weight = weight+"kg";
			}
			else if(Pattern.compile("\\d+").matcher(weight).matches())
			{
				weight = weight+"g";
			}
			if(Pattern.compile("\\d+([kK][gG])").matcher(weight).matches())
			{
				weight = "";
			}
			page = null;
			body2 = null;
			p.setWeight(weight);
			p.setSellUnits(p.getpGoodsUnit());
		}
		info_url = null;		
		return p;
		
	}
	/**
	 * 商品信息							
	 * 匹配 url = http://www.alibaba.com/product-detail/
	 * automatic-car-front-windshield-sun-shade_60030002511.html?s=p	
	 * @param body
	 */
	public  GoodsBean getTypeAny(GoodsBean p,Element body){
		//获取重量体积
		Elements box = body.select("div[class=box]");
		Elements table_tr;
		String tem_unit;
		String sellUnits=null;
		String weight=null;
		String width=null;
		String items = null;
		String gross = null;
		for(Element b:box){
			if(!Pattern.compile("(Packaging)|(packaging)"
					+ "|(Packaging Detail)").matcher(b.select("h3").text()).find()){
				continue;
			}
			table_tr = b.select("table").select("tr");
			if(table_tr!=null&&!table_tr.isEmpty()){
				for(Element t:table_tr){
					tem_unit = t.select("td[class=name mk-name]").text();
					if(Pattern.compile("(Units)|(units)|(Items)").matcher(tem_unit).find()){
						items = tem_unit;
						sellUnits = t.text().replace(tem_unit, "").trim();
						if(Pattern.compile("(\\d+\\s*pcs)|(\\d+\\s*[pP]iece(s*))").matcher(sellUnits).find()){
							sellUnits = StrUtils.matchStr(sellUnits, "((\\d+\\s*pcs)|(\\d+\\s*[pP]iece(s*)))");
						}
						p.setSellUnits(sellUnits);
					}else if(Pattern.compile("(Gross Weight:)|(weight)|(Weight)").matcher(tem_unit).find()){
						gross = tem_unit;
						weight = t.text().replace(tem_unit, "").trim();
						p.setWeight(weight);
					}else if(Pattern.compile("(Package Measurements:)|(Measurementss)|(size)|(Size)|([vV]olume)").matcher(tem_unit).find()){
						width = t.text().replace(tem_unit, "").trim();
						p.setWidth(width);
					}
					if(sellUnits!=null&&weight!=null&&width!=null){
						break;
					}
					
				}
				if(items!=null&&!items.isEmpty()&&gross!=null&&!gross.isEmpty()){
					if(Pattern.compile("(per\\s*[cC]arton)").matcher(items).find()&&
							Pattern.compile("(per\\s*[uU]nit)").matcher(gross).find()){
						p.setSellUnits("single");
					}
				}
				tem_unit = null;
				sellUnits=null;
				weight=null;
				width=null;
			}
			break;
		}
		box = null;
		table_tr = null;
		
		
		//1.名称
		String pName = body.select("div[class=head]").select("h1").text();
		p.setpName(pName);
		pName = null;
		
		//2.id
		Elements id = body.select("script");
		String pID = StrUtils.matchStr(id.toString(),"(?:\"productId\":)(.*?)(?:,)");
		if(pID.isEmpty()){
			pID = StrUtils.matchStr(id.toString(),"(?:ecomm_prodid:')(.*?)(?:',)");
		}
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pName = null;
		pID = null;
		
		//4.图片
		Elements img = body.select("div[data-role=thumbWrap]").select("img");
		for (Element i : img) {
			String tem_img = StrUtils.matchStr(i.toString(), "(?:src=\")(.*?)(?:\")");
			if(tem_img!=null&&!tem_img.isEmpty()){
				pImage.add(tem_img);
			}
		}
		p.setpImage(pImage);
		
		//7.价格
		String pricex = body.select("div[class=price]").select("span[id=J-price]").text();
		p.setpSprice(pricex);
		String pUnit = body.select("div[class=price]").select("span[class=J-country-name]").text();
		if(pUnit!=null){
			p.setpPriceUnit(pUnit.replace("U", "").replace("S", "").replace(pricex, "").trim());
		}
		
		String minOrder = body.select("div[class=order-attr util-clearfix]").select("div[class=value]").text();
		p.setMinOrder(minOrder);
		
		String qunit = body.select("div[class=price]").select("span[id=measurement]").text();
		if(qunit!=null){
			p.setpGoodsUnit(qunit.replace("/", ""));
		}
		pricex = null;
		pUnit = null;
		minOrder = null;
		qunit = null;
		
		
		//10.订单处理时间
		Elements ti =body.select("div[class=processing-time]");
		String pTime = null;
		String time = ti.select("span[class=brief-title]").text();
		if(ti!=null&&time!=null){
			pTime = ti.text().replace(time, "").trim();
			pTime = StrUtils.matchStr(pTime, "(\\d+)");
			
		}
		p.setpTime(pTime);
		pTime = null;
		ti = null;
		
		//11.卖家id
		String sid_pat = "(?:'companyId',')(.*?)(?:'\\);)";
		String sID = StrUtils.matchStr(id.toString(),sid_pat);
		if(sID.isEmpty()){
			sID = "alibabadefault";
		}
		p.setsID(sID);
		id = null;
		sID = null;
		sid_pat = null;
		
		//12.卖家名称
		Elements company = body.select("div[class=company]").select("span[class=company-name]");
		String sName = "ALIBABA";
		if(company.size() >0){
			String com = company.get(0).text();
			sName = com.length()>0?com:"ALIBABA"; 
			com = null;
		}
		if(sName.isEmpty()){
			sName = "ALIBABA";
		}
		p.setsName(sName);
		company = null;
		sName = null;
		
		//13.详细信息
		pInfo = productInfo(body, "div[class=box box-first]",
				"span[class=attr-name J-attr-name]","td[class=value J-value]");
		p.setpInfo(pInfo);
		
		Elements info_img = body.select("div[id=J-product-detail]").select("div[class=box]"); 
		String info = info_img.toString();
		Elements  reinfo = info_img.select("script");
		if(reinfo!=null&&!reinfo.isEmpty()){
			for(Element r:reinfo){
				info = info.replace(r.toString(), "");
			}
		}
		List<String> list3 = StrUtils.matchStrList("(?:<img src=\")(.*?)(?:\" data-src)", info);
		if(Pattern.compile("data-src").matcher(info).find()){
			int length = list3.size();
			for(int i=0;i<length;i++){
				info = info.replace(list3.get(i), "");
			}
			info = info.replaceAll("src=\"\" data-", "");
		}
		
		List<String> list4 = StrUtils.matchStrList("(?:<a.*href=\")(.*?)(?:\")", info);
	    int length = list4.size();
	    String listtem;
	    String ttem;
	    String ut = "processesServlet?action=getSpider&className=SpiderServlet";
	    for(int i=0;i<length;i++){
	    	listtem = list4.get(i);
	    	if(Pattern.compile("(/productgrouplist)").matcher(listtem).find()){
	    	  info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/search)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/collection_product/)").matcher(listtem).find()){
	        	info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/product/)|(/product-detail/)").matcher(listtem).find()){
	    		ttem = TypeUtils.encodeGoods(listtem);
	    		info = info.replace(listtem, ut+ttem);
	    	}else if(Pattern.compile("(.*%20.*/store/)").matcher(listtem).find()){
	    		listtem = StrUtils.matchStr(listtem+"&m", "(?:%20)(.*?)(?:&m)");
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/store/)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(http://.*com/*)").matcher(listtem).matches()){
	    		info = info.replace(listtem, "");
	    	}
	    	listtem = null;
	    	ttem = null;
	    }
	    list4 = null;
		
		p.setInfo_ori(info);
		info = null;
		info_img = null;
		reinfo = null;
		list3 = null;
		
		return p;
		
	}
	/**
	 * 商品信息							
	 * 匹配 url = http://www.alibaba.com/product-detail/
	 * automatic-car-front-windshield-sun-shade_60030002511.html?s=p	
	 * @param body
	 */
	public  GoodsBean getTypeDefault(GoodsBean p,Element body){
//		Elements bodys = body.select("div[class=ls-wrap new-ls-wrap]")
//				 .select("div[class=ls-content");
		//1.名称
		String pName = productName(body, "h1[class=title fn]");
		p.setpName(pName);
		pName = null;
		
		//2.id
		Elements id = body.select("script");
		String pID = StrUtils.matchStr(id.toString(),"(?:'productId',')(.*?)(?:'\\);)");
		if(pID.isEmpty()){
			pID = StrUtils.matchStr(id.toString(),"(?:ecomm_prodid:')(.*?)(?:',)");
		}
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pName = null;
		pID = null;
		
		//3.类别
		Elements span = body.select("div[class=ui-breadcrumb]")
							.select("a");
		StringBuffer sb = new StringBuffer();
		if(span!=null&&!span.isEmpty()){
			for(Element s:span){
				if(s == span.first()){
					sb.append(s.text());
				}else if(s == span.last()){
					sb.append("^^").append(s.text());
				}else{
					sb.append(">").append(s.text());
				}
			}
		}
        
        p.setCategory(sb.toString());
        sb = null;
        span = null;
		
		//4.图片
		if(pImage!=null){
			pImage.clear();
		 }else{
	        pImage = new ArrayList<String>();
		}
		pImage = productImag(body);
		int pimg_size = pImage.size();
		if(pimg_size > 0 && pImage.get(0).indexOf("_250x250.jpg") > 0){
			String[] imgSize = {"_250x250.jpg","_350x350.jpg"};
			p.setImgSize(imgSize);
			imgSize = null;
		}
		String img = null;
		for(int i=0;i<pimg_size;i++){
			if(pImage.get(i).indexOf("_250x250.jpg") > 0){
				img = pImage.get(i).substring(0, pImage.get(i).indexOf("_250x250.jpg"));
				if(img!=null&&!img.isEmpty()){
					pImage.set(i, img);
				}
			}
			img = null;
		}
		
		p.setpImage(pImage);
		
		
		//7.价格ls-brief
		Elements prices = body.select("div[class=ls-icon ls-brief]").select("table").select("tr");
		prices = prices.isEmpty()?body.select("div[class=ls-brief]").select("table").select("tr"):prices;
		String pricex = "";
		String pricey = "";
		String pUnit = "$";
		String qunit = "piece";
		String minOrder = "1";
		String tem;
//		//System.out.println(prices.size());
		if(!prices.isEmpty()){
			for(Element t:prices){
				if(Pattern.compile("Promotional Price").matcher(t.select("th").text()).find()){
					tem = t.select("td").text();
					pricex = tem.length()>4?tem.substring(4).replace("/", "")
							.replace("Piece", "").trim():"";
							tem = null;
				}else if(Pattern.compile("Wholesale Price").matcher(t.select("th").text()).find()){
					tem = t.select("td").text();
					pricey = tem.length()>4?tem.substring(4)
							.replace("/", "")
							.replace("Piece", "").trim():"";
							pUnit = tem.length()>4?tem.substring(3,4):pUnit;
							tem = null;
				}else if(Pattern.compile("FOB").matcher(t.select("th").text()).find()){
					tem = t.select("td").text();
					pUnit =tem.length()>4? tem.substring(3,4):pUnit;
					
					pricey = tem.length() - 22 > 0?tem.substring(4,tem.length() - 22)
							.replace("/", "")
							.replace("Piece", "").trim():"";
							tem = null;
				}else if(Pattern.compile("Min.").matcher(t.select("th").text()).find()){
					minOrder = t.select("td").text();
					if(minOrder.indexOf("/")>0){
						minOrder = minOrder.substring(0,minOrder.indexOf("/"));
//					String qunitep = StrUtils.matchStr(minOrder, "((\\D+))");
						minOrder = minOrder+"(s)";
					}
					qunit = StrUtils.matchStr(minOrder, "((\\D+))");
				}
			}
		}else{
			prices = body.select("div[class=brief-info]")
					.select("dl[class=util-clearfix]");
			for(Element ind:prices){
				if(Pattern.compile("(Promotion Price)").matcher(ind.select("dt").text()).find()){
					tem = ind.select("span[class=measurement]").text().replace("/", "").trim();
					pricex = ind.select("span").text()
							.replace("/", "")
							.replace(tem, "").trim();
					tem =  ind.select("h3").text();
					pUnit =tem.length()>4?tem.substring(3,4):pUnit;
					tem = null;
				}else if(Pattern.compile("(Wholesale Price)").matcher(ind.select("dt").text()).find()){
					tem = ind.select("dd").select("span[class=measurement]").text();
					pricey = ind.select("dd").text()
							.replace("US", "").replace("$", "")
							.replace(tem, "").trim();
					tem =  ind.select("dd").text();
					pUnit = tem.length()>4?tem.substring(3,4):pUnit;
					tem = null;
				}
			}
			
			Elements info_data = body.select("div[class=ls-brief]")
					.select("dl[class=min-order util-clearfix]")
					.select("dd");
		    minOrder = info_data.text();
		    Elements delete = info_data.select("span[class=brief-mark]");
		    String tem_qunit;
		    if(minOrder.isEmpty()){
		    	Elements mmind = body.select("div[class=order-attr util-clearfix]");
		    	for(Element m:mmind){
		    		if(Pattern.compile("(Min)").matcher(m.text()).find()){
		    			minOrder = m.select("div[class=value]").text();
		    		}
		    	}
		    }
		    if(!delete.text().isEmpty()){
		    	tem_qunit = minOrder.replace(delete.text(), "");
		    }else{
		    	tem_qunit = minOrder;
		    }
		    qunit = StrUtils.matchStr(tem_qunit, "(\\D+)");
		   if(minOrder.isEmpty()){
			  minOrder =  StrUtils.matchStr(id.toString(), "(?:moq: ')(.*?)(?:',)");
			  qunit =  StrUtils.matchStr(id.toString(), "(?:moqUnit: ')(.*?)(?:',)");
		   }
			
		}
		if(pricex.isEmpty()){
			pricex = pricey;
			pricey = null;
		}
		if(pricey!=null&&!pricey.isEmpty()){
			pricey = StrUtils.matchStr(pricey, "(\\d+(.)\\d+)");
		}
		if(pricex!=null&&!pricex.isEmpty()){
			pricex = StrUtils.matchStr(pricex, "(\\d+(.+)\\d+)");
		}
		p.setpOprice(pricey);
		p.setpSprice(pricex);
		p.setpPriceUnit(pUnit);
		
		pricey = null;
		pricex = null;
		pUnit = null;
		//8.最小订量
        p.setMinOrder(minOrder);
        p.setpGoodsUnit(qunit);
        minOrder = null; 
        qunit = null;
		
		//10.订单处理时间
		Elements ti =body.select("div[class=box]").select("tr");
		String pTime = null;
		String time;
		for(Element t:ti){
			if(Pattern.compile("(Delivery)").matcher(t.text()).find()){
				if(t.select("td").size()>1){
					time = t.select("td").get(1).text();
					pTime = StrUtils.matchStr(time, "(\\d+(-)\\d+)");
					time = null;
				}
			}
		}
		p.setpTime(pTime);
		pTime = null;
		ti = null;
		
		//11.卖家id
        String sid_pat = "(?:'companyId',')(.*?)(?:'\\);)";
        String sID = StrUtils.matchStr(id.toString(),sid_pat);
        if(sID.isEmpty()){
        	sID = "alibabadefault";
        }
        p.setsID(sID);
        id = null;
        sID = null;
        sid_pat = null;
        
		//12.卖家名称
        Elements company = body.getElementsByClass("company");
        String sName = "ALIBABA";
		if(company.size() >0){
			String com = company.get(0).text();
        	sName = com.length()>20?com.substring(20):"ALIBABA"; 
        	com = null;
        }
		if(sName.isEmpty()){
    		sName = "ALIBABA";
    	}
	    p.setsName(sName);
	    company = null;
	    sName = null;
	    
	    //13.详细信息
	    pInfo = productInfo(body, "div[class=box box-first]", 
				    			  "span[class=attr-name J-attr-name]",
				    			  "td[class=value J-value]");
	    p.setpInfo(pInfo);
	    
	    Elements info_img = body.select("div[id=J-product-detail]")
								.select("div[class=box]"); 
		String info = info_img.toString();
		Elements  reinfo = info_img.select("script");
	    if(reinfo!=null&&!reinfo.isEmpty()){
	    	for(Element r:reinfo){
	    		info = info.replace(r.toString(), "");
	    	}
	    }
	    List<String> list3 = StrUtils.matchStrList("(?:<img src=\")(.*?)(?:\" data-src)", info);
	    if(Pattern.compile("data-src").matcher(info).find()){
	    	int length = list3.size();
			for(int i=0;i<length;i++){
				info = info.replace(list3.get(i), "");
			}
			 info = info.replaceAll("src=\"\" data-", "");
	    }
	    
	    List<String> list4 = StrUtils.matchStrList("(?:<a.*href=\")(.*?)(?:\")", info);
	    int length = list4.size();
	    String listtem;
	    String ttem;
	    String ut = "processesServlet?action=getSpider&className=SpiderServlet";
	    for(int i=0;i<length;i++){
	    	listtem = list4.get(i);
	    	if(Pattern.compile("(/productgrouplist)").matcher(listtem).find()){
	    	  info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/search)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/collection_product/)").matcher(listtem).find()){
	        	info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/product/)|(/product-detail/)").matcher(listtem).find()){
	    		ttem = TypeUtils.encodeGoods(listtem);
	    		info = info.replace(listtem, ut+ttem);
	    	}else if(Pattern.compile("(.*%20.*/store/)").matcher(listtem).find()){
	    		listtem = StrUtils.matchStr(listtem+"&m", "(?:%20)(.*?)(?:&m)");
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/store/)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(http://.*com/*)").matcher(listtem).matches()){
	    		info = info.replace(listtem, "");
	    	}
	    	listtem = null;
	    	ttem = null;
	    }
	    list4 = null;
	    
		p.setInfo_ori(info);
		info = null;
		info_img = null;
		reinfo = null;
		list3 = null;
		
		return p;
		
	}
	
	/**
	 * 商品信息
	 * 匹配 url = http://xinxiuli.en.alibaba.com	
	 * @param body
	 * @throws Throwable 
	 * 
	 */
	public  GoodsBean getTypeEn(GoodsBean p,Element body){
		
		//获取重量体积
		Elements box = body.select("article[class=article]");
		Elements table_tr;
		String tem_unit;
		String sellUnits=null;
		String weight=null;
		String width=null;
		String items = null;
		String gross = null;
		if(box!=null&&!box.isEmpty()){
			for(Element b:box){
				if(Pattern.compile("(Packaging)|(packaging)|(Packaging Detail)").matcher(b.select("h2").text()).find()){
					table_tr = b.select("table").select("tr");
					if(table_tr!=null&&!table_tr.isEmpty()){
						for(Element t:table_tr){
							tem_unit = t.text();
							if(Pattern.compile("(Units)|(units)|(Items)").matcher(tem_unit).find()){
								items = t.select("th").text();
								sellUnits = t.select("td").text().trim();
								if(Pattern.compile("(\\d+\\s*pcs)|(\\d+\\s*[pP]iece(s*))").matcher(sellUnits).find()){
									sellUnits = StrUtils.matchStr(sellUnits, "((\\d+\\s*pcs)|(\\d+\\s*[pP]iece(s*)))");
								}
								p.setSellUnits(sellUnits);
							}else if(Pattern.compile("(Gross Weight:)|(weight)|(Weight)").matcher(tem_unit).find()){
								gross =  t.select("th").text();
								weight = t.select("td").text().trim();
								p.setWeight(weight);
							}else if(Pattern.compile("(Package Measurements:)|(Measurementss)|(size)|(Size)|([vV]olume)").matcher(tem_unit).find()){
								width = t.select("td").text().trim();
								p.setWidth(width);
							}
							if(sellUnits!=null&&weight!=null&&width!=null){
								break;
							}
							
						}
						if(items!=null&&!items.isEmpty()&&gross!=null&&!gross.isEmpty()){
							if(Pattern.compile("(per\\s*[cC]arton)").matcher(items).find()&&
							   Pattern.compile("(per\\s*[uU]nit)").matcher(gross).find()){
								p.setSellUnits("single");
							}
						}
						tem_unit = null;
						sellUnits=null;
						weight=null;
						width=null;
					}
					break;
				}
			}
		}
		box = null;
		table_tr = null;
		
		//1.名称
		Elements names = body.select("div[class=b-property-wrap]").select("h1");
		String pName = names.text();
		if(names.select("span")!=null){
			pName = pName.replace(names.select("span").text(), "");
		}
		p.setpName(pName);
		pName = null;
		
		//2.id
		Elements id = body.select("li[class=add-favorite-li]");
		String pID = StrUtils.matchStr(id.toString(),"(?:data-pid=\")(.*?)(?:\")");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		
		//3.类别
		String category = productCategory(body);
		p.setCategory(category);
		category = null;
		
		//4.图片
		if(pImage!=null){
			pImage.clear();
		 }else{
	        pImage = new ArrayList<String>();
		}
		pImage = productImag(body);
		p.setpImage(pImage);
		
		//7.价格
		Elements p_se = body.select("div[class=brief-info]")
							.select("dl");
		String pricex = "";
		String pricey = "";
		String pUnit = "$";
		String qunit = "piece";
		String minOrder = "1 piece";
		String tem;
		String p_x;
		String p_y;
		String p_unit;
		String pr;
		if(p_se!=null&&!p_se.isEmpty()){
			for(Element t:p_se){
				if(Pattern.compile("Promotion Price").matcher(t.select("dt").text()).find()){
					tem = t.select("dd").select("span[class=measurement]").text();
					p_x = t.select("dd").text();
					pricex = p_x.length()>4?p_x.substring(4).replace(tem, "").trim():"";
				}else if(Pattern.compile("Wholesale Price").matcher(t.select("dt").text()).find()){
					tem = t.select("dd").select("span[class=measurement]").text();
					p_y = t.select("dd").text();
					pricey = p_y.length()>4?p_y.substring(4).replace(tem, "").trim():"";
					p_unit = t.select("dd").text();
					pUnit = p_unit.length()>4?p_unit.substring(3,4):pUnit;
				}else if(Pattern.compile("FOB Price").matcher(t.select("dt").text()).find()){
					pr = t.select("dd").text();
					pUnit = pr.length() - 4 > 0?pr.substring(3,4):pUnit;
					pricey = StrUtils.matchStr(pr, "(?:\\$)(.*?)(?:/)").trim();
				}else if(Pattern.compile("Min.").matcher(t.select("dt").text()).find()){
					minOrder  = t.select("dd").text();
					qunit = StrUtils.matchStr(minOrder, "((\\D+))");
				}
			}
			if(pricey.isEmpty()&&pricex.isEmpty()){
				tem = body.select("h3[class=price]").text();
				pUnit = tem.length()>3?tem.substring(3,4):pUnit;
				pricey = tem.length()>4?tem.substring(4).trim():"";
			}
		}else{
			p_se = body.select("div[class=b-property-wrap]")
						.select("table").select("tr");
			if(p_se!=null&&!p_se.isEmpty()){
				for(Element tt:p_se){
					if(Pattern.compile("(fob\\s*price)").matcher(tt.select("th").text().toLowerCase()).find()){
						pr = tt.select("td").text().replaceAll("\\s+", "").trim();
						pUnit = pr.length() - 4 > 0?pr.substring(3,4):pUnit;
						pricey = StrUtils.matchStr(pr, "(\\d+\\.*\\d*-*\\d*\\.*\\d*)").trim();
					}else if(Pattern.compile("Min.").matcher(tt.select("th").text()).find()){
						minOrder  = tt.select("td").text();
						qunit = StrUtils.matchStr(minOrder, "((\\D+))");
					}}}}
		if(pricex.isEmpty()){
			pricex = pricey;
			pricey = null;
		}
		if(pricey!=null&&Pattern.compile("(\\d+\\.*\\d*)").matcher(pricey).find()){
			p.setpOprice(pricey);
		}
		if(pricex!=null&&Pattern.compile("(\\d+\\.*\\d*)").matcher(pricex).find()){
			p.setpSprice(pricex);
		}
		p.setpPriceUnit(pUnit);
		p_se = null;
		pricey = null;
		pricex = null;
		pUnit = null;
		tem = null;
		p_x = null;
		p_y = null;
		p_unit = null;
		pr = null;
		
		Elements wprice = body.select("dl[class=ladder-price-item util-clearfix]");
		String wp;
		StringBuilder sb = new StringBuilder();
		for(Element w:wprice){
			wp = sb.append(w.select("dd[class=value ladder-price-quantity]").text())
					.append( w.select("span[class=priceVal]").text().replace("US", "")).toString();
			price.add(wp);
			sb.delete(0, sb.length());
			wp = null;
		}
		p.setpWprice(price);
		wprice = null;
		
		//8.最小订量
        p.setMinOrder(minOrder);
        p.setpGoodsUnit(qunit);
        minOrder = null;
        qunit = null;
        pID = null;
		//10.订单处理时间
		String pTime = body.select("tr[class=g-shipping-date]")
							.select("td[class=prop-value]").text();
		if(pTime.isEmpty()){
			Elements time = body.select("article[class=article]")
			.select("table[class=table]")
			.select("td");
			if(time.size()>1){
				String timee= time.get(1).text();
				pTime = StrUtils.matchStr(timee, "(\\d+)");
				timee = null;
			}
			time = null;
		}
		p.setpTime(pTime);
		pTime = null;
		
		//11.卖家id
        String sid_pat = "(?:data-cid=\")(.*?)(?:\")";
        String sID = StrUtils.matchStr(id.toString(),sid_pat);
        if(sID.isEmpty()){
        	sID = "alibabaxin";
        }
        p.setsID(sID);
        sid_pat = null;
        sID = null;
        id = null;
        
		//12.卖家名称
        Elements company = body.getElementsByClass("company");
        String sName = "";
		if(company.size() >0&&company.get(0).text().length()>20){
        	sName = company.get(0).text().substring(20); 
        }
	    if(sName.isEmpty()){
	    	sName = "ALIBABA";
	    }
		p.setsName(sName);
		company = null;
		sName = null;
		
//	    13.详细信息
	    pInfo = productInfo(body, "ul[class=attr-list]", 
						    		"span[class=attr-name]",
						    		"span[class=attr-value]");
	    p.setpInfo(pInfo);
	    
	    Elements acticl = body.select("article[class=article]");
	    
	    Elements info_img = body.select("article[id=richTextContainer]"); 
	    String info = info_img.toString();
	    List<String> list3 = StrUtils.matchStrList("(?:<img src=\")(.*?)(?:\" data-src)", info);
	    if(Pattern.compile("data-src").matcher(info).find()){
	    	int length = list3.size();
			for(int i=0;i<length;i++){
				info = info.replace(list3.get(i), "");
			}
			 info = info.replaceAll("src=\"\" data-", "");
	    }
	    if(!acticl.isEmpty()){
	    	info = acticl.toString()+info;
	    }
	    
	    List<String> list4 = StrUtils.matchStrList("(?:<a.*href=\")(.*?)(?:\")", info);
	    int length = list4.size();
	    String listtem;
	    String ttem;
	    String ut = "processesServlet?action=getSpider&className=SpiderServlet";
	    for(int i=0;i<length;i++){
	    	listtem = list4.get(i);
	    	//http://linkedfashion.en.alibaba.com/product/60048766541-221731301/MOQ_100PCS_NEW_STOCK_quatrefoil_infinity_scarf_12_Colors_cachecol_bufanda_infinito_bufanda.html
	    	if(Pattern.compile("(/productgrouplist)").matcher(listtem).find()){
	    	  info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/search)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/collection_product/)").matcher(listtem).find()){
	        	info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/product/)|(/product-detail/)").matcher(listtem).find()){
	    		ttem = TypeUtils.encodeGoods(listtem);
	    		info = info.replace(listtem, ut+ttem);
	    	}else if(Pattern.compile("(.*%20.*/store/)").matcher(listtem).find()){
	    		listtem = StrUtils.matchStr(listtem+"&m", "(?:%20)(.*?)(?:&m)");
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/store/)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(http://.*com/*)").matcher(listtem).matches()){
	    		info = info.replace(listtem, "");
	    	}
	    	listtem = null;
	    	ttem = null;
	    }
	    list4 = null;
	    
	    p.setInfo_ori(info);
	    info_img = null;
	    info = null;
		return p;		
	}
	/**
	 * 商品信息
	 * 匹配 http://bestway020.en.alibaba.com
	 * @param body
	 * 
	 */
	public  GoodsBean getTypeBest(GoodsBean p,Element body){
		//1.名称
		String pName = body.select("div[class=b-property-wrap]").select("h1").text();
		p.setpName(pName);
		pName = null;
		
		//2.id
		Elements id = body.select("li[class=add-favorite-li]");
		String pID = StrUtils.matchStr(id.toString(),"(?:data-pid=\")(.*?)(?:\")");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		
		//3.类别
		String category = productCategory(body);
		p.setCategory(category);
		category = null;
		
		//4.图片
		pImage = productImag(body);
		p.setpImage(pImage);
		
		//7.价格
		Elements info_name = body.select("table[class=table]")
						.select("tr");
		String qunit = "piece";
		String punit = "$";
		String pricex= "";
		String minOrder = "1 piece";
		String n;
		if(info_name!=null&&!info_name.isEmpty()){
			for(Element in:info_name){
				if(Pattern.compile("FOB Price").matcher(in.select("th").text()).find()){
					n = in.select("td").text();
					punit = n.length()>3?n.substring(3,4):punit;
					pricex = n.substring(4,n.length() - 18).replace("/", "").replace("Piece", "").trim();
					n = null;
				}else if(Pattern.compile("Min.").matcher(in.select("th").text()).find()){
					minOrder = in.select("td").text();
					qunit = StrUtils.matchStr(minOrder, "((\\D+))");
				}
			}
		}
		p.setpSprice(pricex);
		p.setpOprice(null);
		p.setpPriceUnit(punit);
		pricex = null;
		punit = null;
		info_name = null;
		
		//8.最小订量
	    int index = qunit.indexOf("/");
        if(index>0){
        	qunit = qunit.substring(0, index);
        }
        String sub = qunit.substring(qunit.length()-1);
        if("s".equals(sub)){
        	qunit = qunit.substring(0,qunit.length()-1);
        }
        minOrder = StrUtils.matchStr(minOrder, "(\\d+)")+qunit+"(s)";
        p.setMinOrder(minOrder);
		p.setpGoodsUnit(qunit);
		minOrder  = null;
		qunit = null;
		sub = null;
		
		//10.订单处理时间
		String time = body.select("tr[class=g-shipping-date]")
						  .select("td[class=prop-value]").text();
		
		if(time.isEmpty()){
			Elements ti = body.select("article[class=article]")
					.select("td");
			time=ti.size() > 1? ti.get(1).text():"";
			ti = null;
		}
		String pTime = StrUtils.matchStr(time, "(\\d+(-)\\d+)");
		p.setpTime(pTime);
		time = null;
		pTime = null;
		
		//11.卖家id
		String sid_pat = "(?:sid:')(.*?)(?:',)";
        String sID = StrUtils.matchStr(id.toString(),sid_pat);
        if(sID.isEmpty()){
        	sID = "alibababest";
        }
		p.setsID(sID);
		sid_pat = null;
		sID = null;
		id = null;
		
		//12.卖家名称
		Elements company = body.getElementsByClass("company");
        String sName="";
    	sName = company.size() >0?company.get(0).text():"ALIBABA"; 
    	if(sName.isEmpty()){
    		sName = "ALIBABA";
    	}
    	p.setsName(sName);
    	company = null;
    	sName = null;
		//13.详情
		 pInfo = productInfo(body, "ul[class=attr-list]", 
						    		"span[class=attr-name]",
						    		"span[class=attr-value]");
		p.setpInfo(pInfo);
		
		Elements info_img = body.select("article[id=richTextContainer]");
		String info = info_img.toString();
		Elements  reinfo = info_img.select("script");
	    if(reinfo!=null&&!reinfo.isEmpty()){
	    	for(Element r:reinfo){
	    		info = info.replace(r.toString(), "");
	    	}
	    }
	    List<String> list4 = StrUtils.matchStrList("(?:<a.*href=\")(.*?)(?:\")", info);
	    int length = list4.size();
	    String listtem;
	    String ttem;
	    String ut = "processesServlet?action=getSpider&className=SpiderServlet";
	    for(int i=0;i<length;i++){
	    	listtem = list4.get(i);
	    	if(Pattern.compile("(/productgrouplist)").matcher(listtem).find()){
	    	  info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/search)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/collection_product/)").matcher(listtem).find()){
	        	info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/product/)|(/product-detail/)").matcher(listtem).find()){
	    		ttem = TypeUtils.encodeGoods(listtem);
	    		info = info.replace(listtem, ut+ttem);
	    	}else if(Pattern.compile("(.*%20.*/store/)").matcher(listtem).find()){
	    		listtem = StrUtils.matchStr(listtem+"&m", "(?:%20)(.*?)(?:&m)");
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/store/)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(http://.*com/*)").matcher(listtem).matches()){
	    		info = info.replace(listtem, "");
	    	}
	    	listtem = null;
	    	ttem = null;
	    }
	    list4 = null;
	    
		p.setInfo_ori(info);
		info = null;
		info_img =null;
		info = null;
		reinfo = null;
		return p;		
	}
	
	/**
	 *Wholesale Product
	 * ������  url = http://www.alibaba.com/product-detail/220V-Aluminum-Convector-Electric-Heater-for_1532928404.html	
	 * @param body
	 * @throws Throwable 
	 */
	public  GoodsBean getTypeWh(GoodsBean p,Element body){
		//获取公司链接
		Elements address = body.select("div[class=supplier_detail]").select("div[class=address]");
		String addre = StrUtils.matchStr(address.toString(), "(?:href=\")(.*?)(?:\")");
		if(!addre.isEmpty()){
			addre = TypeUtils.encodeSearch(addre);
			p.setSupplierUrl(addre);
		}
		address = null;
		addre = null;
		
		//获取重量体积
		Elements box = body.select("div[class=box]");
		Elements table_tr;
		String tem_unit;
		String sellUnits=null;
		String weight=null;
		String width=null;
		String items = null;
		String gross = null;
		for(Element b:box){
			if(Pattern.compile("(Packaging)|(packaging)|(Packaging Detail)").matcher(b.select("h3").text()).find()){
				table_tr = b.select("table").select("tr");
				if(table_tr!=null&&!table_tr.isEmpty()){
					for(Element t:table_tr){
						tem_unit = t.select("td[class=name mk-name]").text();
						if(Pattern.compile("(Units)|(units)|(Items)").matcher(tem_unit).find()){
							items = tem_unit;
							sellUnits = t.text().replace(tem_unit, "").trim();
							if(Pattern.compile("(\\d+\\s*pcs)|(\\d+\\s*[pP]iece(s*))").matcher(sellUnits).find()){
								sellUnits = StrUtils.matchStr(sellUnits, "((\\d+\\s*pcs)|(\\d+\\s*[pP]iece(s*)))");
							}
							p.setSellUnits(sellUnits);
						}else if(Pattern.compile("(Gross Weight:)|(weight)|(Weight)").matcher(tem_unit).find()){
							gross = tem_unit;
							weight = t.text().replace(tem_unit, "").trim();
							p.setWeight(weight);
						}else if(Pattern.compile("(Package Measurements:)|(Measurementss)|(size)|(Size)|([vV]olume)").matcher(tem_unit).find()){
							width = t.text().replace(tem_unit, "").trim();
							p.setWidth(width);
						}
						if(sellUnits!=null&&weight!=null&&width!=null){
							break;
						}
						
					}
					if(items!=null&&!items.isEmpty()&&gross!=null&&!gross.isEmpty()){
						if(Pattern.compile("(per\\s*[cC]arton)").matcher(items).find()&&
								Pattern.compile("(per\\s*[uU]nit)").matcher(gross).find()){
							p.setSellUnits("single");
						}
					}
					tem_unit = null;
					sellUnits=null;
					weight=null;
					width=null;
				}
				break;
			}
		}
		box = null;
		table_tr = null;
		
		
		//1.名称
		String pName = body.select("div[class=title]").select("h1").text(); 
		p.setpName(pName);
		pName = null;
				
		//2.id
		Elements id = body.select("script");
		String pID = StrUtils.matchStr(id.toString(),"(?:productId: ')(.*?)(?:',)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		//3.类别
		Elements span = body.select("div[class=ui-breadcrumb]")
							.select("a");
		if(span!=null&&!span.isEmpty()){
			StringBuffer sb = new StringBuffer();
			for(Element s:span){
				if(s == span.first()){
					sb.append(s.text());
				}else if(s == span.last()){
					sb.append("^^").append(s.text());
				}else{
					sb.append(">").append(s.text());
				}
			}
			p.setCategory(sb.toString());
			sb = null;
		}
		span = null;
		//4.图片
		pImage = productImag(body);
		if(pImage.isEmpty()){
			String immg = body.select("div[class=J-item]").toString();
			String iimgg = StrUtils.matchStr(immg, "(?:src=\")(.*?)(?:\")");
			if(!iimgg.isEmpty()){
				pImage.add(iimgg);
			}
			immg =  null;
			iimgg = null;
		}
		if(pImage.isEmpty()){
			String img = body.select("div[class=thumb]").toString();
			String imgg = StrUtils.matchStr(img, "(?:src=\")(.*?)(?:\")");
			if(!imgg.isEmpty()){
				pImage.add(imgg);
			}
			imgg = null;
			img = null;
		}
		int pimg_size = pImage.size();
		if(pimg_size > 0 && pImage.get(0).indexOf("_250x250.jpg") > 0){
			String[] imgSize = {"_250x250.jpg","_350x350.jpg"};
			p.setImgSize(imgSize);
			imgSize = null;
		}
		String iimg;
		for(int i=0;i<pimg_size;i++){
			if(pImage.get(i).indexOf("_250x250.jpg") > 0){
				iimg = pImage.get(i).substring(0, pImage.get(i).indexOf("_250x250.jpg"));
				if(iimg!=null&&!iimg.isEmpty()){
					pImage.set(i, iimg);
				}
				iimg = null;
			}
		}
		p.setpImage(pImage);
		//7.价格
		Elements info_name = body.select("div[class=brief-info]")
						.select("dl[class=util-clearfix]");
		if(info_name==null||info_name.isEmpty()){
			info_name = body.select("div[class=brief-info util-clearfix]")
					.select("dl[class=util-clearfix]");
		}
		String pricex = "";
		String pricey="";
		String punit = "$";
		String tem;
		if(info_name!=null&&!info_name.isEmpty()){
			for(Element ind:info_name){
				if(Pattern.compile("(Promotion Price)").matcher(ind.select("dt").text()).find()){
					tem = ind.select("span[class=measurement]").text().replace("/", "").trim();
					pricex = ind.select("span").text()
							.replace("/", "")
							.replace(tem, "").trim();
					tem =  ind.select("h3").text();
					punit =tem.length()>4?tem.substring(3,4):punit;
					tem = null;
				}else if(Pattern.compile("(Wholesale Price)")
						.matcher(ind.select("dt").text()).find()){
					tem = ind.select("dd").select("span[class=measurement]").text();
					pricey = ind.select("dd").text()
							.replace("US", "").replace("$", "")
							.replace(tem, "").trim();
					tem =  ind.select("dd").text();
					punit = tem.length()>4?tem.substring(3,4):punit;
					tem = null;
				}
			}
		}
		if(info_name.isEmpty()){
//			punit = body.select("span[class=measurement]").text().replace("/", "");
			pricey = body.select("span[id=J-oriPrice]").text().trim();
			String pr = body.select("span[id=J-price]").text().trim();
			if(pricey.isEmpty()){
				pricey = pr;
			}else{
				pricex = pr;
			}
		}
		if(pricex.isEmpty()){
			pricex = pricey; 
			pricey = null;
		}
		p.setpPriceUnit(punit);
		p.setpSprice(pricex);
		p.setpOprice(pricey);
		info_name = null;
		pricex = null;
		pricey=null;
		punit = null;
		tem = null;
		
		Elements wprice = body.select("dl[class=ladder-price-item util-clearfix]");
		String wp;
		if(wprice!=null&&wprice.size()>0){
			for(Element w:wprice){
				wp = w.select("dd[class=value ladder-price-quantity]").text()+
						w.select("span[class=priceVal]").text().replace("US", "");
				price.add(wp);
				wp = null;
			}
		}
		if(price==null||price.isEmpty()){
			wprice = body.select("div[class=ladder-price-item-wrap]");
			if(wprice!=null&&wprice.size()>0){
				String pat = null;
				StringBuilder sb = new StringBuilder();
				for(Element w:wprice){
					pat = StrUtils.matchStr(w.select("div[class=ladder-price-quantity]")
							.text(), "(.*\\d+)");
					wp = sb.append(pat).append(w.select("span[class=priceVal]")
							.text().replace("US", "")).toString();
					price.add(wp);
					wp = null;
					pat = null;
					sb.delete(0, sb.length());
				}
			}
		}
		p.setpWprice(price);
		wprice = null;
        
		//8.最小订量
		String minOrder = "1 piece";
        Elements info_data = body.select("div[class=ls-brief]")
						.select("dl[class=min-order util-clearfix]")
						.select("dd");
        minOrder = info_data.text();
        Elements delete = info_data.select("span[class=brief-mark]");
        String tem_qunit;
        if(minOrder.isEmpty()){
        	Elements mmind = body.select("div[class=order-attr util-clearfix]");
        	for(Element m:mmind){
        		if(Pattern.compile("(Min)").matcher(m.text()).find()){
        			minOrder = m.select("div[class=value]").text();
        		}
        	}
        }
        if(!delete.text().isEmpty()){
        	tem_qunit = minOrder.replace(delete.text(), "");
        }else{
        	tem_qunit = minOrder;
        }
        String qunit = StrUtils.matchStr(tem_qunit, "(\\D+)");
       if(minOrder.isEmpty()){
    	  minOrder =  StrUtils.matchStr(id.toString(), "(?:moq: ')(.*?)(?:',)");
    	  qunit =  StrUtils.matchStr(id.toString(), "(?:moqUnit: ')(.*?)(?:',)");
       }
        p.setMinOrder(minOrder);
        p.setpGoodsUnit(qunit);
        
		minOrder = null;
		pID = null;
		qunit = null;
		info_data = null;
		delete = null;
		tem_qunit = null;
		//10.订单处理时间
		Elements time = body.select("dl[class=time util-clearfix]")
							.select("div[class=processing-time]");
		String tempp = time.select("span[class=brief-title]").text();
		String time_re = time.text().replace(tempp, "");
		String pTime = StrUtils.matchStr(time_re, "(\\d+)");
		p.setpTime(pTime);
		time = null;
		tempp = null;
		time_re = null;
		pTime = null;
		
		//11.卖家id
        String sid_pat = "(?:ompanyId:\\s*')(.*?)(?:',)";
        String sID = StrUtils.matchStr(id.toString(),sid_pat);
        if(sID.isEmpty()){
        	sID = StrUtils.matchStr(id.toString(),"(?:svId)(.*?)(?:',)")
        			.replace(":", "").replace("'", "");
        	sID = sID.length()>0?sID:"alibabawholesale";
        }
        p.setsID(sID);
        sID = null;
        sid_pat = null;
        id = null;
        
		//12.卖家名称
	    String sName = body.select("div[class=ls-supplier]")
	    			.select("div[class=address]")
	    			.text(); 
	    if(sName.isEmpty()){
	    	sName = body.select("div[class=company]")
	    			.select("span[class=company-name]")
	    			.text(); 
	    	sName =sName.length()>0? sName:"ALIBABA";
	    }
	    p.setsName(sName);
	    sName = null;
		
	    //13.详细信息
	    pInfo = productInfo(body, "div[class=box box-first]", 
						    		"span[class=attr-name J-attr-name]",
						    		"div[class=ellipsis]");
	    p.setpInfo(pInfo);
	    
	    Elements info_img = body.select("div[id=J-product-detail]")
	    						.select("div[class=box]"); 
	    String info = info_img.toString();
	    Elements  reinfo = info_img.select("script");
	    if(reinfo!=null&&!reinfo.isEmpty()){
	    	for(Element r:reinfo){
	    		info = info.replace(r.toString(), "");
	    	}
	    }
	    List<String> list3 = StrUtils.matchStrList("(?:<img src=\")(.*?)(?:\" data-src)", info);
	    int length;
	    if(Pattern.compile("data-src").matcher(info).find()){
	    	length = list3.size();
			for(int i=0;i<length;i++){
				info = info.replace(list3.get(i), "");
			}
			 info = info.replaceAll("src=\"\" data-", "");
	    }
	    
	    List<String> list4 = StrUtils.matchStrList("(?:<a.*href=\")(.*?)(?:\")", info);
	    length = list4.size();
	    String listtem;
	    String ttem;
	    String ut = "processesServlet?action=getSpider&className=SpiderServlet";
	    for(int i=0;i<length;i++){
	    	listtem = list4.get(i);
	    	if(Pattern.compile("(/productgrouplist)").matcher(listtem).find()){
	    	  info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/search)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	        }else if(Pattern.compile("(/collection_product/)").matcher(listtem).find()){
	        	info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/product/)|"
	    			+ "(/product-detail/)").matcher(listtem).find()){
	    		ttem = TypeUtils.encodeGoods(listtem);
	    		info = info.replace(listtem, ut+ttem);
	    	}else if(Pattern.compile("(.*%20.*/store/)").matcher(listtem).find()){
	    		listtem = StrUtils.matchStr(listtem+"&m", "(?:%20)(.*?)(?:&m)");
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(/store/)").matcher(listtem).find()){
	    		info = info.replace(listtem, "");
	    	}else if(Pattern.compile("(http://.*com/*)").matcher(listtem).matches()){
	    		info = info.replace(listtem, "");
	    	}
	    	listtem = null;
	    	
	    }
	    list4 = null;
	   
	    p.setInfo_ori(info);
	    info_img = null;
	    info = null;
	    reinfo = null;
	    list3 =null;
		return p;		
	}
	
	/**��ȡ��Ʒ�����Ϣ
	 * @param body
	 */
	public  String productCategory(Element body){
	        Elements span = body.select("div[class=ui-breadcrumb]")
	        					.select("a"); 
	        StringBuffer sb = new StringBuffer();
	        if(span!=null&&!span.isEmpty()){
	        	for(Element s:span){
	        		if(s == span.last()){
	        			sb.append(s.text());
	        		}else{
	        			sb.append(s.text()).append(">");
	        		}
	        	}
	        }
	        span = null;
			return sb.toString();
	}
	
	public  Boolean  getKey(Element body){
		Elements span = body.select("div[class=title]")
				.select("span[class=wholesale_product]"); 
		if(span!=null&&!span.isEmpty()){
			span = null;
			return true;
		}
			span = null;
			return false;
	}
	
	
	/**��ȡ��Ʒ����
	 * @param body
	 */
	private  String productName(Element body,String pat){
		Elements name = body.select(pat).select("h1"); 
		return name.text();
	}
	
	/**��ȡ��ƷͼƬ������
	 * @param body
	 */
	private  ArrayList<String> productImag(Element body){
		ArrayList<String> ima = new ArrayList<String>();
        Elements productImag = body.getElementsByClass("thumb").select("img"); 
        if(productImag.size() == 0){
        	productImag = body.select("div[class=ione J-item]").select("img"); 
        }
        if(productImag.size() == 0){
        	productImag = body.select("div[class=iwrap nopic]").select("img"); 
        }
        String ig;
        if(productImag!=null&&!productImag.isEmpty()){
        	for(Element pI:productImag){
        		ig = StrUtils.matchStr(pI.toString(),"(?:src=\")(.*?)(\")");
        		if(!ig.isEmpty()){
        			ima.add(ig);
        		}
        	}
        }
        productImag = null;
        ig = null;
        return ima;
	}
	
	/**��ȡ��Ʒ��ϸ��Ϣ
	 * @param body
	 */
	private  HashMap<String, String> productInfo(Element body,
			String pat1,String pat2,String pat3){
		
		HashMap<String, String> info =  new HashMap<String, String>();
		
		if(body.select(pat1).size()>0){
			Elements detail_name = body.select(pat1)
					.get(0)
					.select(pat2);
			Elements detail_data = body.select(pat1)
					.select(pat3);
			int size = 0;
			if(detail_name.size()==detail_data.size()){
				size = detail_name.size();
			}else if(detail_name.size()>detail_data.size()){
				size = detail_data.size();
			}else{
				size = detail_name.size();
			}
			for(int i=0;i<size;i++){
				if(!(detail_name.get(i).text().isEmpty()
				   ||detail_data.get(i).text().isEmpty())){
					info.put(i+"", detail_name.get(i).text()
							+detail_data.get(i).text());
				}
		}
			detail_name = null;
			detail_data = null;
		}
		
		return info;
	}
}
