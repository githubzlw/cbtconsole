package com.cbt.util;

import com.cbt.newProduct.pojo.NewProduct;
import com.cbt.parse.service.StrUtils;
import com.cbt.warehouse.util.DownloadMainUtil;
import com.cbt.warehouse.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抓取aliexpress 商品数据
 * 提供新品区使用
 * @author admin
 *
 */
@Component
public class SpiderAliexpress {
	private static final Log LOG = LogFactory.getLog(SpiderAliexpress.class);
	/**
	 * 根据url 获取抓取的相关信息
	 * @param url
	 * @return
	 */
	public  NewProduct  getSpider(NewProduct bean){
		String url = bean.getGoods_url();
		Set set = new Set();
		String  page = DownloadMainUtil.getByJsoup(url, 1, set);

		//JSoup解析HTML生成document
		Document doc = Jsoup.parse(page);
		
		//获取页面失败
		if( StringUtils.isEmpty(page)){
			LOG.warn("新品区抓取aliexpress数据 url返回失败:"+url+"--------page isEmpty");
			return  null;
		}
		/**
		 * 获取各个字段数据信息
		 */
		//标题
		String  goods_name ="";
		Elements  goodsNameElement = doc.select("h1[class=product-name]");
		if(goodsNameElement.size()>0){
			goods_name = goodsNameElement.text();
			bean.setGoods_name(goods_name);
		}
		if(goods_name==""){
			return null;
		}
				
		//goods_pid 
		Elements  links = doc.select("link");
		String   linkContent = "";
		for(Element linkE:links){
			  if(linkE.attr("href").toString().indexOf("android-app:")>-1){
				  linkContent = linkE.attr("href").toString() ;
				  break;
			  }
		}
		if(linkContent.length()>0){
			linkContent= linkContent.substring(linkContent.indexOf("&productId=")+11);
			bean.setGoods_pid(linkContent);
		}
		
		//主图信息
		Elements  imgElement = doc.select("div[id=j-detail-gallery-main]").select("div[id=magnifier")
				.select("a[class=ui-image-viewer-thumb-frame]").select("img");
		String  img = "";
		if(imgElement.size()>0){
			try {
				img = imgElement.attr("src");
				bean.setGoods_img(img);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		//价格
		String   goods_price ="";
		Elements  priceElement = doc.select("span[id=j-sku-discount-price]");
		if(priceElement.size()>0){
			goods_price = priceElement.text();
			bean.setGoods_price(goods_price);
		}else{
			priceElement = doc.select("span[id=j-sku-price]"); 
			if(priceElement.size()>0){
				goods_price = priceElement.text();
				bean.setGoods_price(goods_price);
			}
		}
		//单位
		String   goods_price_unit ="";
		Elements  unitElement = doc.select("div[class=p-current-price]").select("span[class=p-unit]");
		if(unitElement.size()>0){
			goods_price_unit = unitElement.text();
			bean.setGoods_price_unit(goods_price_unit);
		}
		
		//goods_unit
		String  goods_unit = "";
		Elements  goodsUnitElement = doc.select("div[class=product-price-main]").select("span[class=p-unit-lot-disc]");
		if(goodsUnitElement.size()>0){
			goods_unit = goodsUnitElement.text();
			goods_unit =goods_unit.substring(0,goods_unit.indexOf(","));
			bean.setGoods_price_unit(goods_unit);
		}
		
		//sold 
		int   goods_sold = 1;
		Elements  soldElement = doc.select("span[id=j-order-num]");
		if(soldElement.size()>0){
			try {
				String goodsold = soldElement.text();
				String regEx="[^0-9]";   
				Pattern p = Pattern.compile(regEx);   
				Matcher m = p.matcher(goodsold);
				goodsold = m.replaceAll("").trim() ;
				bean.setGoods_sold(Integer.parseInt(goodsold));
			} catch (Exception e) {
				bean.setGoods_sold(goods_sold);
			}
		}
		//goods_free 免邮
		int goods_free = 0 ;
		String fre_url = "http://freight.aliexpress.com/ajaxFreightCalculateService."
		+ "htm?callback=jQuery&productid="+linkContent+"&country=US&count=1&sendGoodsCountry=CN";
		String content = DownloadMainUtil.getContentClient(fre_url,null);
		String pFreight = StrUtils.matchStr(content, "(?:localPriceFormatStr\":\")(.*?)(?:\",)")
				.replace("US", "").replace("$", "").replace(",", "").trim();

		if(pFreight.equals("0.00")){
			goods_free = 1;
		}
		bean.setGoods_free(goods_free);
		
		//moq
		int  moq = 1;
		Elements  goodsMoqElement = doc.select("div[class=quantity-info-main]").select("span[class=p-quantity-modified]").select("input");
		if(goodsMoqElement.size()>0){
			try {
				String goodsShipText = goodsMoqElement.attr("quantity");
				bean.setGoods_morder(Integer.parseInt(goodsShipText));
			} catch (Exception e) {
				bean.setGoods_morder(moq);
			}
		}
		return  bean;
	}
	
//	public static void main(String[] args) {
//		String goods_url ="https://sale.aliexpress.com/album.htm";
//		NewProduct  bean = new NewProduct();
//		bean.setGoods_url(goods_url);
//		getSpider(bean);
//	}
	
	
  /**正则匹配相应格式的内容
     * @param page   内容
     * @param pattern  正则
     * @return  满足正则的字符串
     */
    public static String patternContext(String page,String pattern){
    	if(page!=null&&!page.isEmpty()&&pattern!=null){
			if(!"(".equals(pattern.substring(0,1))){
				pattern = "("+pattern;
			}
			if(!")".equals(pattern.substring(pattern.length()-1))){
				pattern = pattern+")";
			}
    		Pattern p = Pattern.compile(pattern); 
    		Matcher m = p.matcher(page);
    		if(m.find()){  
    			return m.group(1).toString();
    		}else{
    			return "";
    		}
    	}
    	return "";
    }
}
