package com.cbt.parse.service;

import com.cbt.parse.bean.TypeBean;
import com.cbt.util.AppConfig;

import org.slf4j.LoggerFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


/**解析其他（除了alibaba）网站商品数据
 * @author abc
 *
 */
public class OtherParse {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OtherParse.class);
	private  ArrayList<String> pImage = new ArrayList<String>();//商品图片
	private  ArrayList<String> price = new ArrayList<String>();//批发价格
	private  ArrayList<TypeBean> types = new ArrayList<TypeBean>();//颜色 尺寸
	private  HashMap<String, String> info = new HashMap<String, String>();
	
	/**商品信息							
	 * www.eelly.com
	 * @param body
	 */
	public GoodsBean getYiLian(GoodsBean p, Element body){
		//1.名称
		String pName =body.select("h2[class=gv-gd-name]").text();
		p.setpName(pName);
		pName = null;
		
		//2.id
		String script = body.select("script").toString();
		String pID = StrUtils.matchStr(script,"(?:goods_id = \")(.*?)(?:\";)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		
		//4.图片
		Elements productImag = body.select("ul[class=pic-group]")
								   .select("img[class=js_bigpipe img_small]"); 
        String ig = null;
    	for(Element img:productImag){
    		ig = img.attr("simg");
    		if(!ig.isEmpty()){
    			pImage.add(ig);
    		}
    	}
		p.setpImage(pImage);
		
		//规格
		Elements esTypes = body.select("div[class=gv-hd-right-b]");
		TypeBean bean = null;
		for(Element type:esTypes){
			Elements dt = type.select("span[class=item-name]");
			Elements bs = type.select("div[class=item-text clr-list]")
							  .select("span");
			for(int j=0;j<bs.size();j++){
				bean = new TypeBean();
				bean.setType(dt.get(0).text());
				bean.setValue(bs.get(j).text());
				types.add(bean);
				bean = null;
			}
		}
		
		String size = StrUtils.matchStr(script, "(?:clrSizeManager.CLR_JSON = \\{)(.*?)(?:\\};)");
		List<String> sizes = StrUtils.matchStrList("(?:\\[)(.*?)(?:\\])",size);
		String value = null;
		List<String> tem_sizes  = new ArrayList<String>();
		for(int i=0;i<sizes.size();i++){
			value =StrUtils.matchStr(sizes.get(i), "(?:\")(.*?)(?:\")");
			if(!tem_sizes.contains(value)){
				tem_sizes.add(value);
				bean = new TypeBean();
				bean.setType("Size");
				bean.setValue(SearchUtils.dencodeUnicode(value));
				types.add(bean);
				bean = null;
			}
		}
		p.setType(types);
			
		//7.价格
		Elements prices = body.select("table[class=step-price]")
							  .select("tr[class=price-group]")
							  .select("td[class=price-item col-1]");
		
		Elements amount = body.select("table[class=step-price]")
							  .select("tr[class=J_sum]")
							  .select("td[class=price-item]");
		if(prices.isEmpty()){
			Elements price_group = body.select("table[class=activty-price]")
									   .select("tr[class=price-group]");
			if(price_group.size()==2){
				prices = price_group.get(0).select("td[class=price-item]");
				amount = price_group.get(1).select("td[class=price-item]");
			}
		}
		
		int price_size = prices.size();
		int amount_size = amount.size();
		int count=0;
		if(price_size>amount_size){
			count = amount_size;
		}else{
			count = price_size;
		}
		Elements current = null;
	   for(int i=0;i<count;i++){
		   current = prices.get(i).select("p[class=cur-price J_price]");
		   if(current.isEmpty()){
			   current = prices.get(i).select("span[class=cur-price J_price]");
		   }
		   if(current!=null&&!current.isEmpty()){
			   price.add( amount.get(i).text()
					   .replaceAll("件", "")+current.text());
		   }else{
			   price.add( amount.get(i).text()
					   .replaceAll("件", "")+prices.get(i).text());
		   }
	   }
		p.setpWprice(price);
		p.setpPriceUnit("RMB");
		p.setpGoodsUnit("piece");
		//8.最小订量
		if(count>0){
			p.setMinOrder(amount.get(0).text()
					.replaceAll("-\\s*\\d*", "").replaceAll("\\D+", ""));
		}else{
			p.setMinOrder("1");
		}
		
		prices = null;
		amount = null;
		
		//商品下架
		String valid = body.select("span[class=gv-icon gv-i-ely]").text();
		if(valid!=null&&!valid.isEmpty()){
			p.setValid(0);
		}
		
		//11.卖家id
		String sID = StrUtils.matchStr(script,"(?:STORE_ID = \")(.*?)(?:\";)");
		p.setsID(sID);
		sID = null;
		
		//12.卖家名称
		Elements company = body.select("div[id=store_name]")
							   .select("strong");
		String sName = company.size() >0? company.text():"YILIAN";
		p.setsName(sName);
		company = null;
		sName = null;
		
		//13.详细信息
		Elements detail_name = body.select("div[class=tab_con]")
								   .select("ul").select("li");
		int data_size = detail_name.size();
		Element li = null;
		for(int i=0;i<data_size;i++){
			li = detail_name.get(i);
			info.put(i+"", li.select("label").text().replace("：", ":")
							+li.select("div").text());
			li = null;
		}
		p.setpInfo(info);
		detail_name = null;
		
		
		Elements info = body.select("div[class=f-style]")
							.select("div[class=goods-view]"); 
		if(info.size()==1){
			p.setInfo_ori(info.toString());
		}else if(info.size()>1){
			p.setInfo_ori(info.get(1).toString());
		}
		return p;
		
	}
	
	
	/**获取商品信息
	 *small-order.hktdc.com				
	 * @param body  
	 * @throws Throwable 
	 */
	public GoodsBean getFromhktdc(GoodsBean p, Element body){
		//1.商品名称
		String pName = body.select("h2[id=productName]").text();
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		Elements pid = body.select("script");
		String pID = StrUtils.matchStr(pid.toString(),
							"(?:var productID = \")(.*?)(?:\";)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		
//		String category = productCategory(body, "ul[class=breadcrumbs]", "a");
//		p.setCategory(category);
//		category = null;
		
		//4.商品图片
		Elements image = body.select("div[class=multizoom1 thumbs]")
				.select("a");
		if(image.size() >0&&image.get(0).toString().indexOf("/small/")>0){
			String mg = "";
			String img;
			String[] split;
			for(Element i:image){
				if(Pattern.compile("(.jpg)").matcher(i.toString()).find()){
					img = i.attr("src");
					if(img.indexOf("small") > 0){
						split = img.split("small");
						if(split.length>1){
							img = split[0];
							mg = split[1];
						}
					}
					if(img!=null&&!img.isEmpty()){
						pImage.add(img);
					}
				}
			}
			p.setpImage(pImage);
			String[] imgSize = {"small"+mg,"enlarge"+mg};
			p.setImgSize(imgSize);
			imgSize = null;
			mg = null;
			img = null;
			split = null;
		}
		image = null;
		
		//7.商品价格
		String oprice = StrUtils.matchStr(pid.toString(),
				"(?:listPriceRangeStr = \")(.*?)(?:\";)");
		String sprice = StrUtils.matchStr(pid.toString(),
				"(?:priceRangeStr = \")(.*?)(?:\";)");
		if(!oprice.isEmpty()&&!sprice.isEmpty()){
			if(oprice.equals(sprice)){
				p.setpSprice(sprice.split("-").length>1?
						sprice.split("-")[1].trim():null);
			}else{
				p.setpOprice(oprice.split("-").length>1?
						oprice.split("-")[1].trim():null);
				p.setpSprice(sprice.split("-").length>1?
						sprice.split("-")[1].trim():null);
			}
		}
		if(price!=null){
			price.clear();
		}else{
			price = new ArrayList<String>();
		}
		String smap = body.select("td[class=second-td]").text()
				+body.select("td[class=notranslate]").text();
		String smal = body.select("td[class=second-td gray-td]").text()
				+body.select("td[class=gray-td notranslate]").text();
		if(!smap.isEmpty()){
			price.add(smap.replace("US", "").trim());
		}
		if(!smal.isEmpty()){
			price.add(smal.replace("US", "").trim());
		}
		p.setpWprice(price);
		p.setpPriceUnit("$");
		smap = null;
		smal = null;
		oprice = null;
		sprice = null;
		pid = null;
		//8.最小订量
		String minOrder = body.select("dl[id=quantityDL]")
				.select("input[id=quantitytf]").attr("value");
		
		Elements qunit = body.select("dl[id=quantityDL]")
				.select("span[id=quantityMeasureUnitPlural]");
		String text = qunit.text();
        p.setMinOrder(minOrder);
        p.setpGoodsUnit(text);
		//11.卖家id
		String sID = body.select("input[name=companyid]").attr("value");
		if(sID.isEmpty()){
        	sID = "hktdc";
        }
		p.setsID(sID);
		sID = null;
		text = null;
		minOrder = null;
		qunit = null;
		
		//12.卖家名称
		String sName = body.select("div[class=company_name notranslate]").text(); 
		if(sName.isEmpty()){
			sName = "HKTDC";
		}
		p.setsName(sName);
		sName = null;
		
		//13.详情
		Elements detail_name = body.select("div[id=productdetailtab]");
		p.setInfo_ori(detail_name.toString().replaceAll("</li>", "</li><br/>")
											.replaceAll("</ul>", "</ul><br/>"));
		detail_name = null;
		return p;
	}

	
	/**商品页面
	 * detail.tmall.com					
	 * @param body  
	 */
	public GoodsBean getFromTmall(GoodsBean p, Element body){
		//1.名称
		String pName = productName(body,"h1[data-spm=1000983]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String pid = body.select("div[id=LineZing]").toString();
		String pID = StrUtils.matchStr(pid,"(?:itemid=\")(.*?)(?:\")");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		
		//4.图片
		
		Elements image = body.select("ul[id=J_UlThumb]").select("img");
		if(image.size() >0&&image.get(0).toString().indexOf("_60x60q90")>0){
			String[] imgSize = {"_60x60q90.jpg","_430x430q90.jpg"};
			p.setImgSize(imgSize);
		}
		String img;
		for(Element i:image){
			if(Pattern.compile("(.jpg)").matcher(i.toString()).find()){
				img = i.attr("src");
				 if(img.indexOf("_60x60q90") > 0){
					 img = img.substring(0, img.indexOf("_60x60q90"));
				 }
				 if(img!=null&&!img.isEmpty()){
					 pImage.add(img);
				 }
			}
			img = null;
		}
		p.setpImage(pImage);
		image = null;
		
		//5.尺码
		TypeBean bean = null;
		Elements types_elment = body.select("div[class=tb-skin]")
									.select("ul");
		String type_name = null;
		String type_img = null;
		Elements types_value = null;
		for(Element type:types_elment){
			type_name = type.attr("data-property");
			if(type_name==null||type_name.isEmpty()){
				continue;
			}
			types_value = type.select("li").select("a");
			for(Element type_value:types_value){
				bean = new TypeBean();
				bean.setType(type_name);
				bean.setValue(type_value.select("span").text());
				type_img = StrUtils.matchStr(type_value.toString(),
						"(?:background\\:url\\()(.*?)(?:\\))");
				if(type_img!=null&&!type_img.isEmpty()){
					bean.setImg("https:"+type_img);
				}
				types.add(bean);
				bean = null;
				type_img = null;
			}
			types_value = null;
			type_name = null;
		}
		p.setType(types);
		types_elment = null;
		
		String qunit = body.select("span[class=mui-amount-unit]").text();
		p.setpGoodsUnit(qunit);
		qunit = null;
		String script = body.select("script").toString();
		//7.价格
		String pr = body.select("span[class=originPrice]").text();
		 String pricex = pr.replace("RMB", "");
		 String punit = StrUtils.matchStr(pr, "(\\D+)");
		 if(pr.isEmpty()){
			 pricex = StrUtils.matchStr(script,
					 "(?:defaultItemPrice\":\")(.*?)(?:\",\"goNewAuctionFlow\":)");
			 punit = "RMB";
		 }
		 p.setpSprice(pricex.trim());
		 p.setpPriceUnit(punit);
		 pr = null;
		 pricex = null;
		 punit = null;
		 pID = null;
		 
		//8.最小订量
		String minOrder = body.select("span[class=tb-amount-widget mui-amount-wrap]")
				.select("input").attr("value");
        p.setMinOrder(minOrder);
        minOrder = null;
		//11.卖家id
		String sID = StrUtils.matchStr(pid, "(?:shopid=\")(.*?)(?:\")");
		if(sID.isEmpty()){
        	sID = "tmall";
        }
		p.setsID(sID);
		sID = null;
		//12.卖家名称（公司名称）
		String sName = body.select("a[class=slogo-shopname]").text(); 
		if(sName.isEmpty()){
			sName = "TMALL";
		}
		p.setsName(sName);
		sName = null;
		
		//13.详细参数信息
		Elements detail_name = body.select("ul[id=J_AttrUL]").select("li");
		int llen = detail_name.size();
		String det;
		for(int j=0;j<llen;j++){
			det =  detail_name.get(j).text();
			if(det!=null&&!det.isEmpty()){
				info.put(String.valueOf(j), det);
			}
			det = null;
		}
		p.setpInfo(info);
		detail_name = null;
		//详情源码
		String desUrl = StrUtils.matchStr(script, "(?:descUrl\":\")(.*?)(?:\",)");
		if(!desUrl.isEmpty()){
			p.setInfourl("http:"+desUrl);
		}
		desUrl = null;
		script = null;
		return p;
	}
	
	/**商品信息
	 *www.amazon.com					
	 * @param body  
	 */
	public GoodsBean getFromAmazon(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"span[id=productTitle]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String pID = body.select("input[name=qid]").attr("value");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		
		//3.类别
/*		String category = productCategory(body, "ul[class=a-horizontal a-size-small]","a");
		if(category.isEmpty()){
			category = productCategory(body, "div[class=detailBreadcrumb]","a");
		}
		p.setCategory(category);
		category = null;
	*/	
		//4.图片
		Elements image = body.select("div[id=imageBlock]")
				.select("li");
		if(image.size()>0&&image.get(0).toString().indexOf("_S")>0){
			String[] img = {"SS40_.jpg","SX355_.jpg"};
			p.setImgSize(img);
			img = null;
		}
		if(pImage!=null){
			pImage.clear();
		}else{
			pImage = new ArrayList<String>();
		}
		String img;
		for(Element i:image){
			if(Pattern.compile("(.jpg)").matcher(i.toString()).find()){
				  img = i.attr("src");
				    if(img.indexOf("_S")+1 > 0){
				    	img = img.substring(0, img.indexOf("_S")+1);
				    }
				    if(img!=null&&!img.isEmpty()){
				    	pImage.add(img);
				    }
			}
			img = null;
		}
		p.setpImage(pImage);
		image = null;
		
		//5.尺寸
		Elements si = body.select("ul[class=a-nostyle a-button-list"
				+ " a-horizontal a-spacing-top-micro swatches swatchesSquare]")
				.select("div[class=twisterSwatchWrapper]");
		int l_s = si.size();
		int i;
		if(l_s == 0){
			si = body.select("div[id=variation_size_name]")
					 .select("option");
			l_s = si.size();
			i = 1;
		}else{
			i =0;
		}
		TypeBean bean = null;
		for(;i<l_s;i++){
			bean = new TypeBean();
			bean.setType("size");
			bean.setValue(si.get(i).text());
			types.add(bean);
		}
		si = null;
		//6.颜色
		Elements co = body.select("div[id=variation_color_name]")
				.select("li");
		int l_c = co.size();
		for(int j=0;j<l_c;j++){
			bean = new TypeBean();
			bean.setType("color");
			bean.setValue(co.get(j).attr("alt"));
			bean.setImg(co.get(j).attr("src"));
			types.add(bean);
		}
		p.setType(types);
		
		//7.价格
		Elements sel_inf = body.select("div[id=price_feature_div]")
							   .select("tr");
		String tem;
		for(Element s:sel_inf){
			if(Pattern.compile("(List Price)").matcher(s.text()).find()){
				if(s.text().length()>13){
					p.setpOprice(s.text().substring(13).trim());
				}
			}else if(Pattern.compile("(Price)").matcher(s.text()).find()){
				tem = s.select("span[id=priceblock_ourprice]").text();
				p.setpSprice(tem.length()<1?null:tem.substring(1).trim());
				p.setpPriceUnit(tem.length()<1?null:tem.substring(0,1));
			}
		}
		sel_inf = null;
		tem = null;
		//8.最小订量
		String minOrder = body.select("div[id=selectQuantity]")
					   .select("span[class=a-dropdown-prompt]")
						.text();
		minOrder = minOrder.length()>0?minOrder:"1 piece";
		p.setMinOrder(minOrder);
		minOrder = null;
		
		//11.卖家id
		p.setsID("amazon");
		
		//12.卖家名称
		p.setsName("AMAZON");
		
		//13.详情
		Elements detail_name = body.select("div[id=detailBullets").select("li");
		if(detail_name.isEmpty()){
			detail_name = body.select("div[id=detail-bullets").select("li");
		}
		int llen = detail_name.size();
		String det;
		for(int j=0;j<llen;j++){
			det =  detail_name.get(j).text();
//			//System.out.println("det:"+det);
			if(det!=null&&!detail_name.isEmpty()){
				info.put(String.valueOf(j), det);
			}
			det = null;
		}
		ArrayList<String> pDetail = new ArrayList<String>();
		Elements detail = body.select("div[class=bucket");
		if(!detail.isEmpty()){
			pDetail.add(detail.toString());
		}
		Elements prodDetails = body.select("div[id=prodDetails")
				.select("div[class=section techD]");
		if(!prodDetails.isEmpty()){
			String temp;
			for(Element pd:prodDetails){
				temp = pd.select("span").text();
				if(Pattern.compile("(Details)").matcher(temp).find()){
					pDetail.add(pd.toString());
				}
			}
		}
		
		Elements prodDe = body.select("script");
		String des = StrUtils.matchStr(prodDe.toString(),
				"(?:%3Cbody%3E%0A%20%20)(.*?)(?:%3Cscript%3E%0A%20%20%20%20)");
		if(!des.isEmpty()){
			pDetail.add(des);
		}
		if(!pDetail.isEmpty()){
			p.setInfo_ori(pDetail.toString());;
		}else{
			p.setpInfo(info);
		}
		pDetail = null;
		prodDe = null;
		des = null;
		prodDetails = null;
		detail = null;
		detail_name = null;
		return p;
	}
	
	
	/**商品信息
	 *www.globalsources.com					
	 * @param body  
	 */
	public GoodsBean getFromGlobal(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"p[class=pp_spd]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String script = body.select("script").toString();
		String pID = StrUtils.matchStr(script,
				"(?:dynx_itemid2: ')(.*?)(?:',)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		
		//3.类别
//		String category = productCategory(body, "p[class=path mt5]","a[class=path_link]");
//		p.setCategory(category);
//		category = null;
		//4.图片
		Elements image = body.select("div[class=img_frame ui_scroll_win]")
							.select("img");
		if(pImage!=null){
			pImage.clear();
		}else{
			pImage = new ArrayList<String>();
		}
		for(Element i:image){
			pImage.add(i.attr("src"));
		}
		p.setpImage(pImage);
		image = null;
		
		//7.价格
		Elements sel_inf = body.select("ul[class=pp_infoList mt25]").select("li");
		String re;
		for(Element s:sel_inf){
			if(Pattern.compile("(FOB Price:)").matcher(s.text()).find()){
				re = s.select("span[class=pp_infoCon]").text();
				p.setpPriceUnit(re.length()>3?null:re.substring(2,3));
				p.setpSprice(re.replace("US$", "").trim());
			}else if(Pattern.compile("(MOQ:)").matcher(s.text()).find()){
				String minOrder = s.select("span[class=pp_infoCon]").text();
				p.setMinOrder(minOrder);
				if(minOrder.length()>7){
					p.setpGoodsUnit(minOrder.substring(minOrder.length() - 7,minOrder.length()-1));
				}
			}else if(Pattern.compile("(Min.*Order)").matcher(s.text()).find()){
				String minOrder = s.select("span[class=pp_infoCon]").text();
				minOrder = minOrder.replaceAll("\\s+", " ");
				p.setMinOrder(minOrder);
				if(minOrder.length()>7){
					p.setpGoodsUnit(minOrder.substring(minOrder.length() - 7,minOrder.length()-1));
				}
			}
		}
		sel_inf = null;
		re = null;
		//11.卖家id
		String sID = StrUtils.matchStr(script, "(?:dynx_itemid: ')(.*?)(?:',)");
		if(sID.isEmpty()){
        	sID = "globalsources";
        }
		p.setsID(sID);
		sID = null;
		script = null;
		
		//12.卖家名称
		String sName = body.select("p[class=pp_supplier]")
						   .select("a[class=pp_supName]")
						   .text(); 
		if(sName.isEmpty()){
			sName = "GLOBALSOURCES";
		}
		p.setsName(sName);
		sName  = null;
		
		Elements in = body.select("div[class=pp_section mt25]");
		if(in.size()>0){
			p.setInfo_ori(in.get(0).toString());
		}
		return p;
	}
	/**商品信息
	 * www.dx.com					
	 * @param body  
	 */
	public GoodsBean getFromDx(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"span[id=headline]");
		p.setpName(pName);
		pName = null;
		//2.商品id
		Elements pid = body.select("span[class=product_sku]");
		String pID = pid.text().length()>5?pid.text().substring(5):"";
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pid = null;
		pID = null;
		//3.类别
//		String category = productCategory(body, "div[class=position]","a");
//		p.setCategory(category);
//		category = null;
		//4.图片
		Elements image = body.select("div[class=small_photo]")
							 .select("li");
		if(image.size()>0&&image.get(0).toString().indexOf("_small")>0){
			String[] imgSize = new String[]{"_small.jpg",".jpg"};
			p.setImgSize(imgSize);
			imgSize = null;
		}
		String img ;
		for(Element i:image){
			img = i.attr("src");
			 if(img.indexOf("_small") > 0){
				 img = img.substring(0, img.indexOf("_small"));
			 }
			 if(img!=null&&!pImage.isEmpty()){
				 pImage.add(img);
			 }
			img = null;
		}
		p.setpImage(pImage);
		image = null;
		
		//5.尺寸
		TypeBean bean = null;
		Elements type = body.select("div[class=choose_quantity]")
						    .select("div[class=product_attr clearfix]");
		String typev = null;
		Elements nodes = null;
		
		for(int i=0;i<types.size();i++){
			typev = type.get(i).select("span[class=label]").text();
			nodes = type.get(i).select("li");
			for(int j=0;j<nodes.size();j++){
				bean = new TypeBean();
				bean.setType(typev);
				bean.setValue(nodes.get(j).text());
				types.add(bean);
			}
		}
		p.setType(types);
		
		
		//7.价格
		Elements pricex = body.select("span[id=price]");
		Elements pricey = body.select("span[id=list-price]");
		if(pricey.text().equals("US$"+pricex.text())){
			p.setpSprice(pricex.text().trim());
			p.setpOprice(null);
		}else{
			p.setpSprice(pricex.text().trim());
			p.setpOprice(pricey.text().length()>3?
					null:pricey.text().substring(3).trim());
		}
		p.setpPriceUnit(pricey.text().length()>3?
				null:pricey.text().substring(2,3).trim());
		
		
		//8.最小订量
		String minOrder = body.select("input[id=qty]").attr("value");
		p.setMinOrder(minOrder);
		
		//9.运费
		String pFreight = body.select("td[class=sc]").text(); 
		p.setpFreight(pFreight);
		pFreight = null;
		minOrder = null;
		pricex = null;
		pricey = null;
		
		//10.订单处理时间
		String time = body.select("td[id=delivery]").text();
		String pTime = StrUtils.matchStr(time, "(\\d+(-)*\\d*)");
		p.setpTime(pTime);
		pTime = null;
		time = null;
		p.setsID("dx");
		
		//12.卖家名称
		p.setsName("DX");
		
		//13.详情
		Elements detail_name = body.select("div[id=overview-detailinfo")
								   .select("tr");
		int llen = detail_name.size();
		String det;
		for(int i=0;i<llen;i++){
		    det =  detail_name.get(i).text();
		    if(det!=null&&!det.isEmpty()){
		    	info.put(String.valueOf(i), det);
		    }
		    det = null;
		}
		if(info!=null&&!info.isEmpty()){
			p.setpInfo(info);
		}
		detail_name = null;
		String infoPage=body.select("div[id=tabProInfo]").toString();
		Elements infotem=body.select("div[class=pinfobox active]")
				.select("p[class=introduce]").select("a");
		if(infotem!=null&&!infotem.isEmpty()){
			for(Element in:infotem){
				infoPage = infoPage.replace(in.toString(), "");
			}
		}
		p.setInfo_ori(infoPage);
		infoPage= null;
		infotem= null;
		return p;
	}
	/**商品信息			
	 * www.ecvv.com				
	 * @param body  
	 */
	public GoodsBean getFromEcvv(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"h1[id=ViewProductname]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String pID = StrUtils.matchStr(body.select("div[class=contact]").toString(),
										"(?:null,')(.*?)(?:'\\);)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		
		
		//3.类别
		String category = productCategory(body, "div[class=crumb-warp]","a");
		category += body.select("div[class=crumb-warp]")
						.select("strong")
						.text();
		p.setCategory(category);
		category = null;
		
		//4.图片
		Elements image = body.select("div[class=thumb-main ProductMinImgItem]")
				.select("img[src$=.jpg]"); 
		
		if(image.size() == 0){
			image = body.select("img[id=icImg]"); 
		}
		for(Element i:image){
			pImage.add(i.attr("src"));
		}
		p.setpImage(pImage);
		image = null;
		//7.价格
		Elements sel_inf = body.select("div[class=property]").select("tr");
		String minOrder = "";
		String pTime = "";
		String pricex;
		for(Element s:sel_inf){
			if(Pattern.compile("(FOB Price)").matcher(s.text()).find()){
				pricex = s.text().length()>11?s.text().substring(11):"";
				if(pricex.length()-10>0){
					p.setpSprice(pricex.substring(0,pricex.length()-10).trim());
				}
				p.setpPriceUnit("$");
			}else if(Pattern.compile("(Min.Order)").matcher(s.text()).find()){
				minOrder = s.text().length()>20?s.text().substring(20):"";
			}else if(Pattern.compile("(Shipping)").matcher(s.text()).find()){
				pTime= s.text().length()>10?s.text().substring(10):"";
			}
		}
		
		//8.最小订量
		p.setMinOrder(minOrder);
		String qUnit = StrUtils.matchStr(minOrder, "(\\D+)");
		p.setpGoodsUnit(qUnit);
		
		//10.订单处理时间
		p.setpTime(pTime);
		pTime = null;
		qUnit = null;
		minOrder = null;
		sel_inf = null;
		pricex = null;
		
		//11.卖家id
		p.setsID("ecvv");
		
		//12.卖家名称
		String sName = body.select("td[class=company-name]")
							.select("a")
						    .text(); 
		if(sName.isEmpty()){
			sName = "ECVV";
		}
		p.setsName(sName);
		sName = null;
		
		//13.详情
		Elements detail_name = body.select("div[class=quick-detail clearfix")
								   .select("li");
		int llen = detail_name.size();
		String det;
		for(int i=0;i<llen;i++){
			det =  detail_name.get(i).text();
			if(det!=null&&!det.isEmpty()){
				info.put(String.valueOf(i), det);
			}
			det = null;
		}
		p.setpInfo(info);
		detail_name = null;
		String page = body.select("div[class=PDmod-detail]").toString();
		p.setInfo_ori(page);
		page = null;
		return p;
	}
	
	/**商品信息
	 *www.tinydeal.com					
	 * @param body  
	 */
	public GoodsBean getFromTiny(GoodsBean p, Element body){
		/*Elements box = body.select("div[class=ui-box ui-box-normal pnl-packaging]");
		Elements table_tr;
		String tem_unit;
		String sellUnits=null;
		String weight=null;
		String width=null;
		for(Element b:box){
			if(Pattern.compile("(Packaging\\s+Details)|(Packaging Detail)|(packaging)|(Packaging)").matcher(b.select("h2").text()).find()){
				table_tr = b.select("dl[class=ui-attr-list util-clearfix]");
				if(table_tr!=null&&!table_tr.isEmpty()){
					for(Element t:table_tr){
						tem_unit = t.select("dt").text();
						if(Pattern.compile("(Unit\\s+Type)|(Units)|(units)|(Items)").matcher(tem_unit).find()){
							sellUnits = t.select("dd").text();
							p.setSellUnits(sellUnits);
						}else if(Pattern.compile("(Package\\s+Weight)|(weight)|(Weight)").matcher(tem_unit).find()){
							weight = t.select("dd").text();
							p.setWeight(weight);
						}else if(Pattern.compile("(Package\\s+Size)|(size)|(Size)|(Measurements)|(Volume)").matcher(tem_unit).find()){
							width = t.select("dd").text();
							p.setWidth(width);
						}}
					tem_unit = null;
					sellUnits=null;
					weight=null;
					width=null;
				}
				break;
			}
			}*/
		
		//1.商品名称
		String pName = productName(body,"h1[id=productName]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String pID = body.select("input[name=products_id]").attr("value");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		
		//3.类别
//		String category = productCategory(body, "div[id=navBreadCrumb]","a");
//		p.setCategory(category);
//		category = null;
		
		//4.图片
		Elements image = body.select("ul[class=p_btnImg]")
							 .select("li"); 
		if(image.size() == 0){
			image = body.select("img[id=icImg]"); 
		}
		for(Element i:image){
			pImage.add(i.attr("src"));
		}
		p.setpImage(pImage);
		image = null;
		
		//5.尺寸
		Elements type = body.select("dl[class=stock clearfix  houseinfo]");
		TypeBean bean = null;
		String typev = null;
		Elements nodes = null;
		for(int i=0;i<type.size();i++){
			typev = type.get(i).select("dt").text();
			nodes = type.get(i).select("dd").select("a");
			for(int j=0;j<nodes.size();j++){
				bean = new TypeBean();
				bean.setType(typev);
				bean.setValue(nodes.get(j).text());
				types.add(bean);
			}
		}
		p.setType(types);
		
		//7.价格
		Elements pricex = body.select("span[class=site-price]")
				.select("span[class=normalprice cen]");
		Elements pricey = body.select("span[class=site-price]")
				.select("span[class=productSpecialPrice fl sprice]");
		
		if(pricex.text().isEmpty()){
			p.setpSprice(pricey.text().length()<2?
					null:pricey.text().substring(1).trim());
			p.setpOprice(null);
		}else{
			p.setpOprice(pricex.text().length()<2?
					null:pricex.text().substring(1).trim());
			p.setpSprice(pricey.text().length()<2?
					null:pricey.text().substring(1).trim());
		}
		p.setpPriceUnit(pricey.text().length()<2?
				null:pricey.text().substring(0,1));
		
		Elements priced = body.select("div[id=productQuantityDiscounts]")
							  .select("dd");
		int len = priced.size();
		for(int i=0;i<len;i++){
			price.add(priced.get(i).text().trim());
		}
		p.setpWprice(price);
		priced = null;
		//8.最小订量
		String minOrder = body.select("div[class=prd_qty]")
					   		  .select("input[id=cart_quantity]")
					   		  .attr("value");
		String qunit = body.select("div[class=prd_qty]")
						  .select("span[class=fl]")
						  .text();
		p.setMinOrder(minOrder);
		p.setpGoodsUnit(qunit);
		minOrder = null;
		qunit = null;
		//11.卖家id
		p.setsID("tinydeal");
		//12.卖家名称
		p.setsName("TINYDEAL");
		
		//13.详情
		Elements detail = body.select("ul[class=specifications_ul");
		if(detail.isEmpty()){
			detail = body.select("ul[class=features_ul");
		}
		Elements detail_name = detail.select("li");
		int llen = detail_name.size();
		if(llen == 0){
		  detail_name = detail.select("tr");
		  llen = detail_name.size();
		}
		String det;
		for(int i=0;i<llen;i++){
			det =  detail_name.get(i).text();
			if(det!=null&&!det.isEmpty()){
				info.put(String.valueOf(i), det);
			}
			det = null;
		}
		detail_name = null;
		detail = null;
		
		String infoPage = body.select("div[id=tabs]").toString();
		p.setInfo_ori(infoPage);
		infoPage = null;
		return p;
	}
	
	/**商品信息
	 *www.ebay.com				
	 * @param body  
	 */
	public GoodsBean getFromEbay(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"span[id=vi-lkhdr-itmTitl]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String pID = body.select("div[class=u-flL iti-act-num]").text()
				.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		
		//3.类别
//		String category = productCategory(body, "td[class=vi-VR-brumblnkLst vi-VR-brumb-hasNoPrdlnks]","a");
//		p.setCategory(category);
//		category = null;
		//4.图片
		
		Elements image = body.select("div[id=vi_main_img_fs_slider]")
							 .select("img[src$=.jpg]"); 
		int img_size = image.size();
		if(img_size == 0){
			image = body.select("img[id=icImg]"); 
			img_size = image.size();
		}
		if(img_size>0 && (image.get(0).toString().indexOf("$_35")>0||
				image.get(0).toString().indexOf("$_14")>0)||
				image.get(0).toString().indexOf("$_12")>0){
			String[] imgSize = {"$_14.jpg","$_12.jpg"};
			p.setImgSize(imgSize);
			imgSize = null;
		}
		String img;
		for(Element i:image){
			img = i.attr("src");
			if(img.indexOf("$_35") > 0){
				img = img.substring(0,img.indexOf("$_35"));
			}else if(img.indexOf("$_14") > 0){
				img = img.substring(0,img.indexOf("$_14"));
			}else if(img.indexOf("$_12") > 0){
				img = img.substring(0,img.indexOf("$_12"));
			}
			if(img!=null&&!pImage.isEmpty()){
				pImage.add(img);
			}
			img = null;
		}
		p.setpImage(pImage);
		image = null;
		
		//规格
		Elements type = body.select("form[name=viactiondetails]")
					        .select("select");
		TypeBean bean = null;
		for(int i=0;i<type.size();i++){
			String nodeName = type.get(i).attr("name");
			Elements option = type.get(i).select("option");
			for(int j=0;j<option.size();j++){
				bean = new TypeBean();
				bean.setType(nodeName);
				bean.setValue(option.get(j).text());
				types.add(bean);
			}
		}
		p.setType(types);
		
		//7.价格
		Elements pricex = body.select("span[id=prcIsum]");
		Elements pricey = body.select("span[id=orgPrc]");
		if(pricey.text().isEmpty()){
			String tem_p = pricex.text().length() >4? 
					pricex.text().substring(4):"";
			p.setpSprice(StrUtils.matchStr(tem_p.replace(",", ""), "(\\d+\\.*\\d*)"));
			p.setpOprice(null);
		}else{
			p.setpSprice(StrUtils.matchStr(pricex.text()
					.replace(",", ""),"(\\d+\\.*\\d*)"));
			p.setpOprice(StrUtils.matchStr(pricey.text()
					.replace(",", ""),"(\\d+\\.*\\d*)"));
		}
		
		String punit = pricex.text().length() >4?
				pricex.text().substring(3,4):null;
		p.setpPriceUnit(punit);
		punit = null;
		pricex = null;
		pricey = null;
		
		//8.最小订量
		String minOrder = body.select("input[class=qtyInput]").attr("value");
		p.setMinOrder(minOrder);
		p.setpGoodsUnit("set");
		minOrder = null;
		
		//10.订单处理时间
		String tem= body.select("span[id=delSummary]")
					.select("span[class=sh-DlvryDtl]").text();
		String pTime = tem.indexOf("-")>0?
				tem.substring(0,tem.indexOf("-")):"";
		String time  = StrUtils.matchStr(pTime, "(\\d+)");
		p.setpTime(time);
		time = null;
		pTime = null;
		tem = null;
		
		//11.卖家id
        String sID = body.select("div[class=mbg vi-VR-margBtm3]")
				.select("span").select("a").text();
		p.setsID(sID);
		sID  =null;
		
		//12.卖家名称
		String sName = body.select("div[class=mbg vi-VR-margBtm3]")
					.select("a").select("span").text(); 
		p.setsName(sName);
		sName = null;
		
		//13.详情
		Elements detail_name = body.select("div[class=itemAttr")
								   .select("td[class=attrLabels]");
		Elements detail_data = body.select("div[class=itemAttr")
								   .select("td[width=50.0%]");
		int detail_name_size = detail_name.size();
		int detail_data_size = detail_data.size();
		if(detail_data_size<detail_name_size){
			detail_name_size = detail_data_size;
		}
		for(int i=0;i<detail_name_size;i++){
			info.put(String.valueOf(i), 
					detail_name.get(i).text()+detail_data.get(i).text());
		}
		p.setpInfo(info);
		detail_name = null;
		detail_data = null;
		String infoPage = body.select("div[id=desc_div]").toString();
		p.setInfo_ori(infoPage);
		infoPage = null;
		return p;
	}
	
	/**商品信息
	 *lightinthebox					
	 * @param body  
	 */
	public GoodsBean getFromLight(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"div[class=widget prod-info-title]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String pID = StrUtils.matchStr(body.toString(),
				"(?:name=\"products_id\" value=\")(.*?)(?:\">)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		
		//3.类别
//		String category = "Home"+productCategory(body, "ul[class=breadcrumb clearfix]","a");
//		p.setCategory(category);
//		category = null;
		
		//4.图片
		Elements i_sel = body.select("ul[class=list]");
		Elements image;
		if(i_sel.size() != 0){
			image = i_sel.select("img[data-normal$=.jpg]");
		}else{
			image = body.select("div[class=list]").select("div");
		}
		for(Element i:image){
			pImage.add(i.attr("src"));
		}
		p.setpImage(pImage);
		i_sel = null;
		image = null;
		
		//5.尺寸
		TypeBean bean = null;
		Elements select = body.select("li[class=attributes-value-show-li]")
				.select("select");
		Elements options = null;
		String typev = null;
		for(int i=0;i<select.size();i++){
			options = select.get(i).select("option");
			typev = options.get(0).text();
			for(int j=1;j<options.size();j++){
				bean = new TypeBean();
				bean.setType(typev);
				bean.setValue(options.get(j).text());
				types.add(bean);
			}
		}
		p.setType(types);
		
		//7.价格
		Elements pricex = body.select("div[class=current-price clearfix]")
							  .select("strong[itemprop=price]");
		String sprice="";
		String oprice="";
		if(pricex.text().length()>1){
			sprice =pricex.text().substring(1).trim();
		}
		Elements pricey = body.select("div[class=price-left]").select("del");
		if(pricey.text().length()>5){
			oprice = pricey.text().substring(5).trim();
			p.setpPriceUnit(pricey.text().substring(4,5));
		}
		if(sprice.isEmpty()){
			sprice = oprice;
			oprice = null;
		}
		p.setpSprice(sprice);
		p.setpOprice(oprice);
		sprice = null;
		oprice = null;
		pricex  =null;
		pricey  =null;
		//8.最小订量
		Elements morder = body.select("select[name=cart_quantity]")
					   .select("option");
		String minOrder = null;
		if(!morder.isEmpty()){
			minOrder = morder.get(0).text();
		}else{
			minOrder = "1 piece";
		}
		p.setMinOrder(minOrder);
		minOrder = null;
		morder = null;
		//11.卖家id
		p.setsID("lightinthebox");
		
		//12.卖家名称
		p.setsName("LIFHTINTHEBOX");
		
		//13.详情
		Elements detail = body.select("div[class=specTitle").select("tr");
		if(info!=null){
			info.clear();
		}else{
			info = new HashMap<String, String>();
		}
		int i = 0;
		String det;
		for(Element d:detail){
			det = d.select("th").text()+":"+d.select("td").text();
			info.put(String.valueOf(i), det);
			det  = null;
			i++;
		}
		p.setpInfo(info);
		detail = null;
		
		String infoPage = body.select("div[class=widget prod-description-gallery]")
							  .toString()+"\n"+
				          body.select("div[class=widget prod-note-marketing]").toString();
		if(infoPage.isEmpty()){
			infoPage = body.select("div[id=prod_description]").toString();
		}
		p.setInfo_ori(infoPage);
		infoPage = null;
		return p;
	}
	
	/**商品信息
	 *MadeinChina					
	 * @param body  
	 */
	public GoodsBean getFromMade(GoodsBean p, Element body){
		Elements box = body.select("div[class=full-description]");
		String tem_unit;
		List<String> list_tem;
		for(Element b:box){
			tem_unit = b.select("div").select("font[size=2]").text();
			if(!Pattern.compile("(Dimensions)|(weight)|(Weight)").matcher(tem_unit).find()){
				continue;
			}
			list_tem = StrUtils.matchStrList("(?:\\()(.*?)(?:\\))", tem_unit);
			if(list_tem==null||list_tem.isEmpty()){
				continue;
			}
			for(String l:list_tem){
				if(Pattern.compile("(cm)|(CM)").matcher(l).find()){
					p.setWidth(l);
				}else if(Pattern.compile("(g)|(kg)").matcher(l).find()){
					p.setWeight(l);
				}
			}
			tem_unit = null;
			list_tem = null;
			break;
		}
		box = null;
		//1.商品名称
		String pName = productName(body,"h1[class=product-name]");
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String tem_pid = productId(body, "div[class=productSku]",null);
		String pID = tem_pid.length()>4?tem_pid.substring(4):"";
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
		tem_pid = null;
		
		//3.类别
//		String category = "Home"+productCategory(body, "div[class=progressDiv]","a");
//		p.setCategory(category);
//		category = null;
		
		//4.图片
		String[] imgSize = {"_small.jpg",".jpg"};
		p.setImgSize(imgSize);
		
		Elements image = body.select("div[id=thumbs]").select("img[src$=.jpg]"); 
		if(image.size() == 0){
			image = body.select("div[class=tb-pic tb-s50]"); 
		}
		String img;
		for(Element i:image){
			img = i.attr("src");
			if(img.indexOf("_small") > 0){
				img = img.substring(0,img.indexOf("_small"));
				if(img!=null&&!img.isEmpty()){
					pImage.add(img);
				}
				img = null;
			}
		}
		p.setpImage(pImage);
		image = null;
		imgSize = null;
		
		
		//7.价格
		Elements pricex = body.select("div[class=detailProprice]")
							  .select("span[id=currentProprice]");
		Elements pricey = body.select("div[class=detailProprice]")
							  .select("span[id=currentProoldprice]");
		if(!pricey.text().isEmpty()){
			p.setpOprice(pricey.text().trim());
			p.setpSprice(pricex.text().trim());
		}else{
			p.setpOprice(null);
			p.setpSprice(pricex.text().trim());
		}
		Elements punit = body.select("div[class=detailProprice]")
							 .select("span[class=currentpriceSpan]");
		if(!punit.isEmpty()&&punit.get(0).text().length()>2){
			p.setpPriceUnit(punit.get(0).text().substring(2));
		}
		pricex = null;
		pricey = null;
		
		//8.最小订量
		String minOrder = body.select("input[class=qty-input]").attr("value");
		p.setMinOrder(minOrder);
		p.setpGoodsUnit("unit(s)");
		minOrder = null;
		//9.运费
		String tem_f  =body.select("div[style=color: #666666;font-size:12px;"
							+ "float:right; margin-right: 115px;height:39px; "
							+ "text-align: left;width:218px;]")
							.text(); 
		
		//10.订单处理时间
		String pTime = tem_f.length()>24?tem_f.substring(24):"";
		String time = StrUtils.matchStr(pTime, "(\\d+)");
		p.setpTime(time);
		tem_f = null;
		time = null;
		pTime = null;
		
		//11.卖家id
		p.setsID("madeinchina");
		
		//12.卖家名称
		p.setsName("MADEINCHINA");
		
		//13.详情
		p.setpInfo(null);
		
		String infPage = body.select("div[id=productDes]").toString();
		p.setInfo_ori(infPage);
		infPage = null;
		return p;
	}
	
	/**商品信息
	 *www.jd.com					
	 * @param body  
	 */
	public GoodsBean getFromJD(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"div[class=p-name]");
		p.setpName(pName);
		pName = null;				
		//2.商品id
		String tem_pid = productId(body, "ul[id=skuid]",null);
		String pID = tem_pid.length()>8?tem_pid.substring(8):"";
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		tem_pid = null;
		pID = null;
				
		//3.类别
//		String category = productCategory(body, "div[class=breadcrumb]","a");
//		p.setCategory(category);
//		category = null;
		
		//4.图片
		Elements image = body.select("ul[class=list-h]")
					         .select("img[src$=.jpg]"); 
		if(image.size() == 0){
			image = body.select("div[id=preview]"); 
		}
		for(Element i:image){
			pImage.add(i.attr("src"));
		}
		p.setpImage(pImage);
		image = null;
		//8.最小订量
		Elements qunit = body.select("dl[class=quantity1]");
		String minOrder = qunit.select("input")
								.attr("value");
        p.setMinOrder(minOrder);
        p.setpGoodsUnit(qunit.select("span").text());
        qunit = null;
        minOrder = null;
		//11.卖家id
        p.setsID("jd");
		//12.卖家名称
	    p.setsName("JD");
		
	    //13.详情
        p.setpInfo(null);
         
        String infoPage = body.select("div[itemprop=description]").toString();
        p.setInfo_ori(infoPage);
        infoPage = null;
		return p;
	}
	
	/**商品信息
	 *item.taobao.com				
	 * @param body  
	 */
	public GoodsBean getFromTaobao(GoodsBean p, Element body, Element  head){
		//1.商品名称
		String pName = productName(body,"h3[class=tb-main-title]");
		p.setpName(pName);
		pName = null;
				
		//2.商品id
		String pID =body.select("input[name=item_id]")
				.attr("value").replaceAll("\\D+", "").trim();
		p.setpID(pID);
		
		//4.图片
		Elements image = body.select("ul[id=J_ThumbNav]").select("img"); 
		String img;
		for(int i=0;i<image.size();i++){
			img = image.get(i).attr("src").replaceAll("_\\d+x\\d+\\.jpg(_\\.webp)*", "");
			if(!img.isEmpty()){
				pImage.add("https:"+img);
			}
		}
	    if(pImage.isEmpty()){
	    	image = body.select("ul[class=tb-thumb tb-clearfix]")
	    			.select("img[data-src$=.jpg]"); 
	    	image = image.size() == 0?body.select("div[class=tb-pic tb-s50]")
	    			.select("img[data-src$=.jpg]"):image; 
	        if(image.size() >0&&image.get(0).toString().indexOf("_50x50.jpg")>0){
	        	p.setImgSize(new String[]{"_50x50.jpg","_400x400.jpg"});
	        }
	        for(Element pI:image){
	        	img = pI.attr("data-src");
	        	img = img.isEmpty()?pI.attr("src"):img;
	        	img = img.indexOf("_50x50.jpg") > 0?img.substring(0,img.indexOf("_50x50.jpg")):img;
	        	if(img!=null&&!img.isEmpty()){
	        		img = "https:".equals(img.substring(0, 6))?img:"https:"+img;
	        		pImage.add(img);
	        	}
	        	img = null;
			}	
	        image = null;
        }
		p.setpImage(pImage);
		
		//5.尺码
		TypeBean bean = null;
		Elements types_elment = body.select("div[class=tb-skin]").select("ul");
		String type_name = null;
		String type_img = null;
		Elements types_value = null;
		String type_id = null;
		for(Element type:types_elment){
			type_name = type.attr("data-property");
			if(type_name==null||type_name.isEmpty()){
				continue;
			}
			types_value = type.select("li");
			for(Element type_value:types_value){
				bean = new TypeBean();
				bean.setType(type_name);
				type_id = type_value.attr("data-value");
				type_id = type_id.isEmpty()?null:type_id;
				bean.setId(type_id);
				bean.setValue(type_value.select("a").select("span").text());
				type_img = StrUtils.matchStr(type_value.select("a").toString(),
									    "(?:background\\:url\\()(.*?)(?:\\))");
				if(type_img!=null&&!type_img.isEmpty()){
					bean.setImg("https:"+type_img);
				}
				types.add(bean);
				bean = null;
				type_img = null;
			}
			types_value = null;
			type_name = null;
		}
		p.setType(types);
		types_elment = null;
		
		//7.价格
		Elements pricex = body.select("strong[id=J_StrPrice]");
		if(pricex.size() != 0&&pricex.text().length()>3){
			p.setpSprice(pricex.select("em[class=tb-rmb-num]").text().trim());
			p.setpOprice(null);
		}
		p.setpPriceUnit("RMB");
		pricex = null;
		//8.最小订量
		String minOrder = body.select("dl[class=tb-amount tb-clearfix]").select("input")
					            .attr("value");
        p.setMinOrder(minOrder);
        
        String tem_qUnit = body.select("dl[class=tb-amount tb-clearfix]")
        					   .select("span[class=tb-stock]").text();
    	String qUnit = tem_qUnit.length() > 2?tem_qUnit.substring(2).trim():null;
        p.setpGoodsUnit(qUnit);
        qUnit = null;
        tem_qUnit = null;
        minOrder = null;
        pID = null;
		
		//11.卖家id
        String sID = StrUtils.matchStr(body.select("div[class=tb-booth tb-pic tb-main-pic]")
        					.toString(),"(shopId=\\d+)")
        					.replaceAll("\\D+", "").trim();
        sID =sID.isEmpty()? "taobao":sID;
        p.setsID(sID);
        sID = null;
        
		//12.卖家名称
	    String sName = body.select("div[class=tb-shop-name]")
	    				   .select("strong").text(); 
	    sName = sName.isEmpty()?"TAOBAO":sName;
	    p.setsName(sName);
	    sName = null;
		
	    //13.详情
	    info = productDetail(body, "ul[class=attributes-list]", "li");
	    p.setpInfo(info);
	    
	    String pInfoUrl =  StrUtils.matchStr(body.select("script").toString(),
	    									"(?:apiItemDesc\":\")(.*?)(?:\",)");
	    p.setInfourl(pInfoUrl);
	    pInfoUrl = null;
		return p;
	}

		
	/**获取商品信息
	 * hero-tech.en.made-in-china.com
	 * @param body  
	 */
	public GoodsBean getFromHero(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body,"h1[itemprop=name]");
		p.setpName(pName);
		pName  =null;
						
		//2.商品id
		String pID = body.select("input").attr("value");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
				
		//4.图片
		Elements image = body.select("img[itemprop=image]"); 
		if(image.size() == 0){
			image = body.select("a[class=ui-image-viewer-thumb-frame]"); 
		}
		for(Element i:image){
			pImage.add(i.attr("src"));
		}
		p.setpImage(pImage);
		image = null;	
		
		//7.价格
		Elements ele = body.select("div[class=property sh-table]")
				           .select("div[class=tr]");
		
		String minOrder = "1 piece";
		String pricex;
		Elements un;
		String unit;
		for(Element e:ele){
			if("Min. order:".equals(e.select("span[class=th]").text())){
				minOrder = e.select("span[class=td]").text();
			}else if(!e.select("table[id=priceTable]").isEmpty()){
				un = e.select("table[id=priceTable]").select("td");
				pricex= un.size()>1?un.get(1).text():"";
				p.setpSprice(pricex.replace("US", "").replace("$", "")
						.replace("/", "").replace("Piece", "").trim());
				p.setpOprice(null);
				p.setpPriceUnit(pricex.length()>4?pricex.substring(3,4):"$");
				
				un = e.select("table[id=priceTable]").select("th");
				unit = un.size()>0?un.get(0).text():"";
				unit = unit.length()>11?unit.substring(11).replace("(", " /")
										  .replace(")", ""):"";
			    
				un = e.select("table[id=priceTable]").select("td");
				minOrder = un.size()>0?un.get(0).text()+unit:minOrder;
				p.setpGoodsUnit(unit.replace("/", ""));
				un = null;
				unit = null;
				pricex = null;
			}
		}
		        
		//8.最小订量
        p.setMinOrder(minOrder);
        minOrder = null;
        ele = null;
				
		//11.卖家id
        p.setsID("herotech");
		        
		//12.卖家名称
        Elements name = body.select("div[class=sub-side-item supplier-profile]").select("li");
        if(!name.isEmpty()){
        	String sName = name.get(0).text(); 
        	sName = sName.isEmpty()?"HEROTECH":sName;
        	p.setsName(sName);
        	sName = null;
        }
        name= null;
				
	    //13.详情
	    info = productDetail(body, "div[class=detail-item]", "div[class=tr]");
	    p.setpInfo(info);
	    
	    String infoPage = body.select("div[itemprop=description]").toString();
	    p.setInfo_ori(infoPage);
	    infoPage = null;
		return p;
	}
	
	/**商品信息
	 *www.aliexpress.com					
	 * @param body  
	 */
	public GoodsBean getFromAliexpress(GoodsBean p, Element body){
		String script = body.select("script").toString();
		//已经售出数量
		String sell = body.select("span[class=orders-count]").select("b").text();
		sell = sell.isEmpty()?StrUtils.matchStr(script, "productTradeCount=\"\\d+,*\\d*")
								.replaceAll("\\D+", "").trim():sell;
		p.setSell(sell);
		
		//商品价格优惠失效时间
		String discount_time = body.select("span[id=discount-count-down]").text();
		if(!discount_time.isEmpty()){
			String day = StrUtils.matchStr(discount_time, "\\d+d")
					             .replaceAll("\\D+", "").trim();
			String hour = StrUtils.matchStr(discount_time, "\\d+h")
					 			  .replaceAll("\\D+", "").trim();
			String minter = StrUtils.matchStr(discount_time, "\\d+m")
									.replaceAll("\\D+", "").trim();
			String second = StrUtils.matchStr(discount_time, "\\d+s")
									.replaceAll("\\D+", "").trim();
			if((!day.isEmpty())||!second.isEmpty()||!hour.isEmpty()||!minter.isEmpty()){
				p.setDtime(1);
			}
		}else{
			discount_time = body.select("span[class=time-left]").text();
			if(!discount_time.isEmpty()){
				p.setDtime(1);
			}
		}
		
		//重量信息
		Elements weight_ul = body.select("ul[class=product-packaging-list util-clearfix]");
		Elements box = !weight_ul.isEmpty()?weight_ul.get(0).select("li"):weight_ul;
		String tem_unit;
		String tem_str=null;
		String gross=null;
		String items=null;
		for(Element b:box){
			tem_unit = b.select("span[class=packaging-title]").text();
			tem_str = b.select("span[class=packaging-des]").text();
			if(Pattern.compile("(Unit\\s+Type)|(Units)"
					+ "|(units)|(Items)").matcher(tem_unit).find()){
				items = tem_unit;
				p.setSellUnits(tem_str);
			}else if(Pattern.compile("(Package\\s+Weight)"
					+ "|(weight)|(Weight)").matcher(tem_unit).find()){
				gross = tem_unit;
				p.setWeight(tem_str);
			}else if(Pattern.compile("(Package\\s+Size)|(size)"
					+ "|(Size)|(Measurements)|(Volume)").matcher(tem_unit).find()){
				p.setWidth(tem_str);
			}
			tem_str = null;
		}
		if(items!=null&&gross!=null){
			if(Pattern.compile("(per\\s*Carton)").matcher(items).find()&&
			   Pattern.compile("(per\\s*Unit)").matcher(items).find()){
				p.setSellUnits("single");
			}
		}
		box = null;
		
		//Bulk Price
		String bprice = body.select("div[id=bulk-price-tip]").text();
		p.setbPrice(bprice);
		
		//1.商品名称
		String pName = productName(body,"h1[class=product-name]");
		p.setpName(pName);
								
		//2.商品id
		String pID = productId(body, "div[class=prod-id]", "span");
		pID = pID.length() >12?pID.substring(12).replaceAll("\\D+", "").trim():"";
		p.setpID(pID);
		pID = null;
		
		//3.类别
		Elements span = body.select("div[class=module m-sop m-sop-crumb]").select("a");
		if(span.size()==0){
			span = body.select("div[class=ui-breadcrumb]").select("a");
		}
		Element spa = null;
		StringBuffer sb = new StringBuffer();
		for(int i=2;i<span.size();i++){
			spa = span.get(i);
			if(spa == span.get(2)){
				sb.append(spa.text());
			}else if(spa == span.last()){
				sb.append("^^").append(spa.text());
			}else{
				sb.append(">").append(spa.text());
			}
		}
		p.setCategory(sb.toString());
		sb = null;
		span = null;
		spa = null;
		
		
		//4.图片
		Elements image = body.select("ul[class=image-nav util-clearfix]")
							 .select("li"); 
		image =image.isEmpty()? body.select("ul[class=image-thumb-list]")
									.select("li"):image; 
		
		image = image.isEmpty()?
				body.select("a[class=ui-image-viewer-thumb-frame]"):image; 
		
		if(image.size()>0&&image.get(0).toString().indexOf("_50x50")>0){
			String[] imgSize = {"_50x50.jpg","_350x350.jpg"};
			p.setImgSize(imgSize);
			imgSize = null;
		}
		String img;
		for(Element i:image){
			img = i.select("img").attr("src");
			if(img.indexOf("_50x50") > 0){
				img = img.substring(0,img.indexOf("_50x50"));
			}else if(img.indexOf("_350x350") > 0){
				img = img.substring(0,img.indexOf("_350x350"));
			}else if(img.indexOf("_640x640") > 0){
				img = img.substring(0,img.indexOf("_640x640"));
			}
			pImage.add(img);
		}
		p.setpImage(pImage);
		img = null;
		image = null;
		
		//根据规格  获取价格
		String skuProducts = StrUtils.matchStr(script, "(?:skuProducts=)(.*?)(?:}}];)");
		skuProducts = !skuProducts.isEmpty()?skuProducts+	"}}];":skuProducts;
		p.setSkuProducts(skuProducts);
		
		//规格
		Elements block1 = body.select("dl[class=product-info-size]");
		Elements block2 = body.select("dl[class=product-info-color]");
		if(block1!=null&&block2!=null&&!block2.isEmpty()){
			block1.addAll(block2);
		}
		block1 = block1.isEmpty()?body.select("dl[class=p-property-item]"):block1;
		TypeBean bean = null;
		String value = null;
		String color_img = null;
		String color_id = null;
		for(int i=0;i<block1.size();i++){
			Elements dt = block1.get(i).select("dt");
			Elements bs = block1.get(i).select("li");
			for(int j=0;j<bs.size();j++){
				if(Pattern.compile("(ships.*from)").matcher(dt.text().toLowerCase()).find()){
					continue;
				}
				bean = new TypeBean();
				value = bs.get(j).text();
				value = value.isEmpty()?bs.get(j).select("a").attr("title"):value;
				color_img = bs.get(j).select("img").attr("src");
				color_id = bs.get(j).select("a").attr("id")
						.replaceAll("(sku-.*-)", "").trim();
				bean.setType(dt.text().replaceAll("(\\(.*\\))", ""));
				bean.setValue(value);
				bean.setImg(color_img);
				bean.setId(color_id);
				types.add(bean);
			}
		}
		
		
		//页面加载不完全的情况下获取规格参数
		/*if(types.isEmpty()){
			List<String> skuAttrIdList = StrUtils.matchStrList("(?:\"skuAttr\"\\:\")(.*?)(?:\",\"skuPropIds\")", skuProducts);
			int skuAttrIdList_size = skuAttrIdList.size();
			ArrayList<String> color = new ArrayList<String>();
			ArrayList<String> size = new ArrayList<String>();
			ArrayList<String> other = new ArrayList<String>();
			for(int i=0;i<skuAttrIdList_size;i++){
				String[] skuAttrIds = skuAttrIdList.get(i).split(";");
				//14:173#as pic;5:361386
				int skuAttrIds_length = skuAttrIds.length;
				if(skuAttrIds_length==0){
					continue;
				}
				if(skuAttrIds_length>0&&!color.contains(skuAttrIds[0])){
					color.add(skuAttrIds[0]);
				}
				if(skuAttrIds_length>1&&!size.contains(skuAttrIds[1])){
					size.add(skuAttrIds[1]);
				}
				if(skuAttrIds_length>2&&!other.contains(skuAttrIds[2])){
					other.add(skuAttrIds[2]);
				}
			}
			if((!color.isEmpty())&&!size.isEmpty()){
				color.addAll(size);
			}
			if((!color.isEmpty())&&!other.isEmpty()){
				color.addAll(other);
			}
			int color_size = color.size();
			IPvidDao  dao  = new PvidDao();
			TypeBean typeBean = null;
			for(int i=0;i<color_size;i++){
				String[] colors = color.get(i).split("#");
				if(colors.length<1){
					continue;
				}
				String[] colors_ids = colors[0].split(":");
				if(colors_ids.length<2){
					continue;
				}
				PvidBean pvidBean = dao.getPvid(colors[0].replace(":", "_"));
				if(pvidBean!=null){
					typeBean = new TypeBean();
					typeBean.setType(pvidBean.getName()+":");
					typeBean.setValue(colors.length>1?colors[1]:pvidBean.getValue());
					typeBean.setImg(pvidBean.getImg());
					typeBean.setId(pvidBean.getPvid().replaceAll("\\d+_", ""));
					types.add(typeBean);
				}
			}
		}*/
		
		
		p.setType(types);
		//7.商品单位
		Elements es_unit = body.select("span[class=p-unit]");
		es_unit = es_unit.isEmpty()?body.select("span[id=p-unit]"):es_unit;
		es_unit = es_unit.isEmpty()?body.select("span[id=product-info-unit]"):es_unit;
		String unit = es_unit.isEmpty()?"piece":es_unit.get(0).text();
		//原价
		String pricex= body.select("span[id=sku-price]").text();
		pricex= pricex.isEmpty()?body.select("span[id=j-sku-price]").text():pricex;
		pricex = pricex.replace(",", "").trim();
		//折扣后价格
		String pricey= body.select("span[id=sku-discount-price]").text();
		pricey= pricey.isEmpty()?body.select("span[id=j-sku-discount-price]").text():pricey;
		pricey = pricey.replace(",", "").trim();
		
		DecimalFormat format = new DecimalFormat("#0.00");
		//页面加载不完全的情况下获取价格
		if(pricex.isEmpty()&&pricey.isEmpty()&&!pName.isEmpty()){
			LOG.warn("***************get---price---from---script*************");
			String minpeice = StrUtils.matchStr(script, "(?:runParams\\.minPrice=\")(.*?)(?:\";)");
			String maxpeice = StrUtils.matchStr(script, "(?:runParams\\.maxPrice=\")(.*?)(?:\";)");
			String discount = StrUtils.matchStr(script, "(?:runParams\\.discount=\")(.*?)(?:\";)");
			pricex = minpeice;
			if(!discount.isEmpty()&&!minpeice.isEmpty()){
				pricey =format.format(Double.valueOf(minpeice)*(100-Double.valueOf(discount))/100.0) ;
				if(!maxpeice.isEmpty()&&!minpeice.equals(maxpeice)){
					String pricey2 =format.format(Double.valueOf(maxpeice)*(100-Double.valueOf(discount))/100.0) ;
					pricey +="-"+pricey2;
				}
			}
			if(!maxpeice.isEmpty()&&!minpeice.equals(maxpeice)){
				pricex += "-"+maxpeice;
			}
		}
		if(!pricex.isEmpty()){
			p.setpOprice(pricex);
		}
		if(!pricey.isEmpty()){
			p.setpSprice(pricey);
			p.setpOprice(pricex);
			p.setDtime(0);
			String[] price_split = pricey.split("-");
			//现价
			String pricey_tem = StrUtils.matchStr(price_split[0], "(\\d+\\.*\\d*)");
			//原价
			String pricex_tem = StrUtils.matchStr(pricex.split("-")[0], "(\\d+\\.*\\d*)");
			//若商品折扣超过了30% 则现价price = price * 1.2
			if((!pricey_tem.isEmpty())&&!pricex_tem.isEmpty()){
				Double  old_price = Double.valueOf(format.format(Double.valueOf(pricex_tem)*0.7));
				Double curent_price = Double.valueOf(pricey_tem);
				if(old_price>curent_price||curent_price-old_price<0.0001){
					String price_currency = format.format(curent_price*1.2);
					if(price_split.length>1){
						String pricey_tem2 = StrUtils.matchStr(price_split[1], "(\\d+\\.*\\d*)");
						price_currency +="-"+ format.format(Double.valueOf(pricey_tem2)*1.2);
					}
					p.setpSprice(price_currency);
					p.setpOprice(pricex);
					p.setDtime(1);
				}
			}
		}else{
			p.setpSprice(pricex);	
			p.setpOprice(null);
		}
		p.setpGoodsUnit(unit);
		Elements pUnit = body.select("span[itemprop=priceCurrency]");
		if(!pUnit.isEmpty()){
			String attr = pUnit.get(0).attr("content");
			attr = "USD".equals(attr)?"$":attr;
			p.setpPriceUnit(attr);
		}else{
			p.setpPriceUnit("$");
		}
		pUnit = null;
		pricey = null;
		pricex = null;
		unit = null;
		
		//8.最小订量
		Elements minOrder = body.select("dl[class=product-info-quantity]").select("input");
		minOrder = minOrder.isEmpty()? body.select("dl[class=p-property-item p-quantity-info]")
										  .select("input"):minOrder;
        p.setMinOrder(minOrder.attr("value"));
        minOrder = null;
				
		//10.订单处理时间
		String pTime= body.select("div[id=product-info-shipping-sub]").text();
		pTime = pTime.isEmpty()? body.select("p[class=p-delivery-day-tips]").text():pTime;
		p.setpTime(StrUtils.matchStr(pTime, "(\\d+)"));
		pTime = null;
						
		//11.卖家id
        String sID = StrUtils.matchStr(script, "(?:companyId=\")(.*?)(?:\";)");
        p.setsID(sID.isEmpty()?"aliexpress":sID);
        sID = null;
		//12.卖家名称
	    String sName = body.select("div[class=company-name]").text(); 
	    p.setsName(sName.isEmpty()?"ALIEXPRESS":sName);
	    sName = null;
				
	    //13.详情
	    if(info!=null){
	    	info.clear();
	    }else{
	    	info = new HashMap<String, String>();
	    }
	    Elements detail = body.select("div[class=ui-box ui-box-normal product-params]").select( "dl");
	    detail = detail.isEmpty()?
	    		body.select("div[class=ui-box ui-box-normal product-params]"):detail;
	    detail = detail.isEmpty()?
	    		body.select("ul[class=product-property-list util-clearfix]")
	    							  .select("li[class=property-item]"):detail;
		
	    int detail_size = detail.size();
		String pvid = "";
		Element element = null;
		String pvid_p = null;
		String pvid_v = null;
		for(int i=0;i<detail_size;i++){
			element = detail.get(i);
			info.put(String.valueOf(i), element.text());
			pvid_p = element.absUrl("id").replaceAll("\\D+", "").trim();
			pvid_v = element.attr("data-attr").trim();
			if(!pvid_p.isEmpty()&&!pvid_v.isEmpty()){
				pvid = pvid + pvid_p+"_"+pvid_v+",";
			}
			element = null;
			pvid_p = null;
			pvid_v = null;
		}
		detail = null;
	    p.setpInfo(info);
	    p.setPvid(pvid);
	    
	    String infoUrl = StrUtils.matchStr(script, "(?:descUrl=\")(.*?)(?:\";)");
	    p.setInfourl(infoUrl);
	    infoUrl = null;
	    Elements element_pack = body.select("div[class=ui-box pnl-packaging-main]");
	    String pack = element_pack.size()!=0?element_pack.get(0).toString():"";
	    p.setPackages(pack);
	    pack = null;
	    //商店url
	    String supplierUrl = body.select("a[id=visit-store]").attr("href");
	    supplierUrl = supplierUrl.isEmpty()?
	    		body.select("span[class=shop-name]").select("a").attr("href"):supplierUrl;
	    supplierUrl = supplierUrl.isEmpty()?
	    		supplierUrl:TypeUtils.encodeSearch(supplierUrl);
	    p.setSupplierUrl(supplierUrl);
	    //用户反馈
	    String thesrc = StrUtils.matchStr(body.select("div[id=feedback]").toString(), "(?:thesrc=\")(.*?)(?:\">)");
	    if(thesrc!=null&&!thesrc.isEmpty()){
	    	p.setNoteUrl(thesrc);
	    }
	    script = null;
		return p;
	}
	
	/**商品数据	
	 * yiwubuy			
	 * @param body  
	 */
	public GoodsBean getFromYiwubuy(GoodsBean p, Element body){
		
		//1.商品名称
		Elements spans = body.select("div[class=pro-view-nav]").select("span");
		if(spans.size()>0){
			String name = spans.get(0).text();
			name = ParseGoodsUrl.filterName(name);
			p.setpName(name);
		}
		
		//2.商品id
		String pID = StrUtils.matchStr(body.select("p[class=pro_recom_img]").toString(),
										"(?:detail/)(.*?)(?:.html)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		pID = null;
						
		//3.商品类别
//		String category = productCategory(body, "div[class=en_nav fontbluelink]",null);
//		p.setCategory(category);
//		category = null;
						
		//4.商品图片
		Elements image = body.select("div[class=pro_view_left_img]").select("a"); 
		for(Element i:image){
			pImage.add(i.attr("src"));
		}
		p.setpImage(pImage);
		image = null;
		
		//7.商品价格
		Elements ele = body.select("table[class=mt15px]").select("tr[height=27]");
		int ele_size = ele.size()-1;
		Element e;
		String amount;
		String pricex;
		for (int i = ele_size; i > -1; i--) {
		   e = ele.get(i);
		   if(e.select("td").size() > 2){
		    amount = e.select("td").get(0).text();
		    if(amount.indexOf("(pcs)") > -1){
		     amount = amount.replace("pcs", "")
		    		 .replace("Above", "≥").replace("~", "-");
		    }
		    
		    pricex = e.select("td").get(2).text();
		    if(pricex.indexOf("yuan/pc") > -1){
		     pricex = pricex.replace("yuan/pc", "");
		    }
		    price.add((amount+" RMB "+pricex).trim());
		   }
		   e = null;
		}
        p.setpWprice(price);
        p.setpPriceUnit("RMB");  
        ele = null;
        pricex = null;
        //8.商品最小订单数量
        Elements quantity = body.select("td[height=25]").select("input");
        String minOrder = "1 piece";
		if(quantity.size() != 0){
        	minOrder = StrUtils.matchStr(quantity.toString(),
        				 "(?:value=\")(.*?)(?:\" style=\")");
        }
        p.setMinOrder(minOrder);
        
        Elements element = body.select("table[class=mt15px]").select("tr");
        String qunit = null;
        for(Element el:element){
        	if(el.text().indexOf("Supply Quantity:") > -1){
        		qunit = StrUtils.matchStr(el.select("td").get(1).text(), "(\\D+)");
        	}
        }
        p.setpGoodsUnit(qunit);
        quantity = null;
        element = null;
        qunit = null;
        minOrder = null;
								
		//11.卖家id(可能为销售公司id)
        p.setsID("yiwubuy");
        //12.卖家名称(可能为销售公司名称)
        Elements sNam = body.select("ul[class=pro_view_isright_com]").select("li"); 
	    String sName =sNam.size()>0?sNam.get(0).text():"YIWUBUY"; 
	    p.setsName(sName);
	    sNam = null;
	    sName = null;
				
	    //13.详细信息
	    p.setpInfo(null);
	    
	    //详情源码
	    String page = body.select("div[class=pro_view_info]").toString(); 
	    if(page==null||page.isEmpty()){
	    	page = body.select("div[class=pro_view_bold]").toString(); 
	    }
	    String replas = body.select("img[title=Click here to message me]").toString();
	    if(page!=null&&!page.isEmpty()&&!replas.isEmpty()){
	    	page = page.replace(replas, "");
	    }
	    p.setInfo_ori(page);
	    page = null;
		return p;
	}
	
	/**商品数据	
	 * dhgate			
	 * @param body
	 */
	public GoodsBean getFromDhgate(GoodsBean p, Element body){
		//1.商品名称
		String pName = productName(body, "h1[itemprop=name]");
		if(pName.isEmpty()){
			pName = productName(body, "h2[class=fn]");
		}
		p.setpName(pName);
		pName = null;
		
		//2.商品id
		String script = body.select("script").toString();
		String pID = StrUtils.matchStr(script,"(?:itemcode:\")(.*?)(?:\",)");
		pID = pID.replaceAll("\\D+", "").trim();
		p.setpID(pID);
		
		//3.商品类别
//		String category = productCategory(body,"div[class=bread-crumbs-inner]",null);
//		p.setCategory(category);
//		category = null;
		
		//4.商品图片
		Elements productImag = body.getElementsByClass("thumb"); 
		if(productImag.size() == 0){
			productImag = body.select("ul[id=simgList]").select("li"); 
		}
		for(Element i:productImag){
			pImage.add(StrUtils.matchStr(i.toString(),"(?:b-init=\")(.*?)(\")"));
		}
		p.setpImage(pImage);
		productImag = null;
		
		//规格
		Elements type1 = body.select("ul[id=listBuy_1]");
		Elements type2 = body.select("ul[id=listBuy_2]");
		if(type1!=null&&type2!=null&&!type2.isEmpty()){
			type1.addAll(type2);
		}
		String s_value = null;
		TypeBean bean = null;
		Elements type = null;
		String typev = null;
		Elements select = body.select("div[class=key-info-lt optionlf-tit]");
		for(int i=0;i<type1.size();i++){
			typev = type1.get(i).select("input").val();
			typev = typev.replaceAll("\\s+\\|\\s+","").replaceAll("''","")
						 .replaceAll("\\d+","").trim();
			if(type1.size()==select.size()&&typev.isEmpty()){
				typev = select.get(i).text();
			}
			type = type1.get(i).select("li");
			for(Element s:type){
				s_value = s.text();
				if(s.text().indexOf("selected") > -1){
					s_value = s_value.length()>8?s_value.substring(8):"";
				}
				if(s_value.isEmpty()){
					s_value = s.select("span").attr("title");
				}
				if(s_value.isEmpty() || "more".equals(s_value)){
					continue;
				}
				bean  = new TypeBean();
				bean.setType(typev);
				bean.setValue(s_value);
				bean.setImg(s.select("span").attr("src"));
				types.add(bean);
			}
		}
		p.setType(types);
		//7.商品价格
	    Elements pricex = body.select("strong[class=unit-price]");
	    String pOPrice = null;
	    Elements pricey = body.select("strong[class=w-price unit-linethrough]");
	    String sprice = "";
	    if(pricey.text().isEmpty()){
	    	pOPrice = pricex.text();
	    	sprice = "";
	    }else{
	    	pOPrice = pricey.text();
	    	sprice = pricex.text().replace("US", "").replace("$", "")
	    			.replace("/", "").replace("Piece", "").trim();
	    }
	    String oprice = pOPrice.replace("US", "").replace("$", "")
	    				.replace("/", "").replace("Piece", "").trim();
	    
	    if(sprice.isEmpty()){
	    	sprice = oprice;
	    	oprice = null;
	    }
	    p.setpSprice(sprice);
	    p.setpOprice(oprice);
	    p.setpPriceUnit(pOPrice.length()>4?pOPrice.substring(3,4):null);
        
	    Elements price_Wholesale = body.select("table[class=wholesale-price-list]")
	      							   .select("tr");
	    
	    String t1;
	    String t2;
	    String t3;
	    for(Element price_Wh:price_Wholesale){
	    	if(price_Wh.toString().indexOf("display: none") == -1){
	    		t2 = price_Wh.text();
	    		t1 = price_Wh.select("td[class=col1]").text();
	    		t3 = price_Wh.select("td[class=col3]").text();
	    		if(!t3.isEmpty()&&!price.contains((t1+t3).replace("US", "").trim())){
	    			price.add((t1+t3).replace("US", "").trim());
	    		}else{
	    			if(!price.contains(t2.replace("US", "").trim())){
	    				price.add(t2.replace("US", "").trim());
	    			}
	    		}
	    	}
	    	t1 = null;
	    	t2 = null;
	    	t3 = null;
	    }
	    if(price==null||price.isEmpty()){
	    	price_Wholesale = body.select("ul[class=js-wholesale-list]")
	    						 .select("li");
	    	String wp = null;
	    	for(Element price_Wh:price_Wholesale){
	    		wp = price_Wh.text().replace("US", "").trim();
	    		if(!price.contains(wp)){
	    			price.add(wp);
	    		}
		    }
	    }
	    p.setpWprice(price);
	    pricex = null;
	    pOPrice = null;
	    pricey = null;
	    sprice = null;
	    price_Wholesale = null;
	    oprice = null;
	    
	    //获取重量 体积
  		Elements box = body.select("div[class=description]").select("li");
  		String tem_unit;
  		String tem_str=null;
  		for(Element t:box){
  			tem_unit = t.select("strong").text();
  			if(StrUtils.isFind(tem_unit, "([uU]nits)|([iI]tems)|(Quantity)")){
  				tem_str = t.text().replace(tem_unit, "").trim();
  				p.setSellUnits(tem_str);
  			}else if(StrUtils.isFind(tem_unit, "([pP]ackage\\s*Weight)|([wW]eight)")){
  				tem_str = t.text().replace(tem_unit, "").trim();
  				p.setWeight(tem_str);
  			}else if(StrUtils.isFind(tem_unit, "([pP]ackage\\s*Size)|([sS]ize)|([mM]easurements)|([vV]olume)")){
  				tem_str = t.text().replace(tem_unit, "").trim();
  				p.setWidth(tem_str);
  			}
  		}
  		tem_unit = null;
  		tem_str = null;
  		box = null;
  		
	    //8.商品最小订单数量
	    String qUnit = "";
	    Elements unit = body.select("div[class=key-info-line]")
							.select("div[class=key-info-ri]")
							.select("p");
		if(!unit.isEmpty()&&unit.text().indexOf("Delivery estimate") == -1){
				qUnit= unit.text();
		}
	    String minOrder = body.select("input[id=defaultQuantity]").attr("initial");
	    
	    String qUnit2 = body.select("span[class=purchase-quantity-lot]").text();
	    minOrder += " "+qUnit2;
	    
	    p.setMinOrder(minOrder);
	    p.setpGoodsUnit(qUnit);
	    qUnit = null;
	    unit = null;
	    minOrder = null;
		
		//10.订单处理时间
		String pTime = body.select("span[id=prossingtime]").text();
		String time = StrUtils.matchStr(pTime, "(\\d+)");
		p.setpTime(time);
		pTime = null;
		time = null;
        
		//11.卖家id(可能为销售公司id)
        String sID = StrUtils.matchStr(script,"(?:sellerid:')(.*?)(?:',)");
        p.setsID(sID);
        sID = null;
        //12.卖家名称(可能为销售公司名称)
	    String sName = body.select("span[class=byseller]").select("a").text(); 
	    p.setsName(sName);
	    sName = null;
		
        //13.参数详细
	    Elements detail = body.select("div[id=itemDescription]")
	    			 		  .select("div[class=item-specifics]")
	    			 		  .select("li");
	    Elements detail2 = body.select("div[id=itemDescription]")
	    					   .select("div[class=description]")
	    					   .select("li");
	    detail.addAll(detail2);
		int detail_size = detail.size();
		for(int i=0;i<detail_size;i++){
			info.put(String.valueOf(i), detail.get(i).text());
		}
	    p.setpInfo(info);
	    detail = null;
	    detail2 = null;
	    //详情源码
	    String infoPage = body.select("div[id=con-info]")
	    					  .toString().replace("&amp;", "&");
	    String path = StrUtils.matchStr(infoPage,"(?:data-url=\")(.*?)(?:\")");
	    if(!path.isEmpty()){
	    	String epath ="http://www.dhgate.com"+path.replace("\\s*", "%20");
			String inpage = DownloadMain.getContentClient(epath,null);
			
			inpage = inpage.replaceAll("width=\"990\"", "width=\"650\"")
							.replaceAll("鈥檚", "'s");
			
	    	List<String> klist = StrUtils.matchStrList("(?:<ul>)(.*?)(?:</ul></div>)", inpage);
			for(String k:klist){
				inpage = inpage.replace(k, "");
			}
			p.setInfo_ori(inpage);
			inpage = null;
			epath = null;
	    }
	    infoPage = null;
	    path = null;
		return p;
	}
	
	
	/**获取产品类别信息
	 * @param body
	 * @param pat_2  可为空null
	 * 
	 */
	public static String productCategory(Element body,String pat_1,String pat_2){
			Elements category;
			if(pat_2 == null){
				category = body.select(pat_1); 
			}else{
	        	category = body.select(pat_1).select(pat_2);
	        }
	        StringBuffer title = new StringBuffer();
	        for(Element c:category){
	        	if(c == category.last()){
	        		title.append(c.text());
	        	}else{
	        		title.append(c.text()).append(">");
	        	}
	        }
	        category = null;
			return title.toString();
	}
	
	/**获取商品名称
	 * @param body
	 * @param pat
	 */
	private  String productName(Element body,String pat){
		Elements span = body.select(pat);
		return span.text();
	}
	
	/**获取商品ID
	 * @param body
	 * @param pat
	 */
	private  String productId(Element body,String pat_1,String pat_2){
		Elements id;
		if(pat_2 == null){
			id = body.select(pat_1);
		}else{
			id = body.select(pat_1).select(pat_2);
		}
		return id.text();
	}
	
	/**获取商品详细信息
	 * @param body
	 * @param pat
	 */
	private  HashMap<String, String> productDetail(Element body,String pat_1,String pat_2){
		Elements detail;
		HashMap<String, String> inf = new HashMap<String, String>();
		if(pat_2 == null){
			 detail = body.select(pat_1);
		}else{
			detail = body.select(pat_1).select(pat_2);
		}
		int size = detail.size();
		for(int i=0;i<size;i++){
			inf.put(String.valueOf(i), detail.get(i).text());
		}
		detail = null;
		return inf;
	}
	
	public static String getInfo(String infoUrl,String ip){
		if(!infoUrl.isEmpty()){
	    	String page = DownloadMain.getContentClient(infoUrl,ip).replace("window.productDescription='", "").replace("var desc='", "");
	    	List<String> list = StrUtils.matchStrList("(?:href=\")(.*?)(?:\")", page);
	    	String teml;
	    	String temll;
	    	String ut =AppConfig.path+"/processesServlet?action=getSpider&className=SpiderServlet";
	    	int length = list.size();
	    	for(int i=0;i<length;i++){
	    		teml = list.get(i);//+".html";
	    		if(Pattern.compile("(>"+teml+"</a>)").matcher(page).find()){
	    			page = page.replace(">"+teml+"</a>", "></a>");
	    		}
	    		if(Pattern.compile("(/product/)|(/product-fm/)|(/item/)").matcher(teml).find()){
	    			temll = ut+TypeUtils.encodeGoods(teml);
	    			page = page.replace(teml, temll);
	    		}
//	    		else if(Pattern.compile("(/store/.*SearchText=.*)").matcher(teml).find()){
//	    			page = page.replace(teml, "");
//	    		}
	    		else if(Pattern.compile("(/group/)|(/store/)|(/productgrouplist)").matcher(teml).find()){
	    			String remain = teml.replaceAll("(http.*/store/\\d+)", "");
	    			page = page.replace(teml, "");
	    			page = remain.isEmpty()?page:page.replace(remain, "");
	    			page = page.replaceAll("(/search(\\?).*&SearchText=([a-zA-Z%(\\+)-_&0-9])+(&SortType=([a-zA-Z%-_(\\+)&0-9])*)*)", "");
	    			
	    		}
	    		else if(Pattern.compile("(http.*com)").matcher(teml).find()){
	    			page = page.replace(teml, "");
	    		}
	    		temll = null;
	    		teml = null;
	    	}
	    	page = page.replaceAll("width=\"100%\"", "");
	    	ut  =null;
	    	list = null;
	    	return page;
	    }
		return "";
	}
	
}
