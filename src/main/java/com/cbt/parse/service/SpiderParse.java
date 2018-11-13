package com.cbt.parse.service;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @author abc
 *
 */
public class SpiderParse {
	
	/**商品id
	 * @date 2016年4月28日
	 * @author abc
	 * @param url
	 * @param int_type
	 * @param pid
	 * @return  
	 */
	public static String getPidByUrl(String url,int int_type,String pid)
	{
		
		if( pid != null && !pid.isEmpty() && !"1111110".equals(pid))
		{
			pid = int_type>0&&int_type<4?pid.replaceAll("\\D+", "").trim():pid;
			if( !pid.isEmpty() )
			{
				return pid;
			}
		}
		
		pid = StrUtils.matchStr(url, "(/\\d+\\.htm)").replaceAll("\\D+", "");
		pid = pid.isEmpty()?StrUtils.matchStr(url, "(_\\d+\\.htm)")
									.replaceAll("\\D+", ""):pid;
		if( !pid.isEmpty() )
		{
			return pid;
		}
		//alibaba
		if( int_type > 0 && int_type < 4 )
		{
			pid = StrUtils.matchStr(url, "(/product/\\d+)")
							  .replaceAll("\\D+", "");
			return pid;
		}
		//taobao  tmall
		if(int_type==5||int_type==18)
		{
			pid = StrUtils.matchStr(url, "(id=\\d+)")
							  .replaceAll("\\D+", "");
			return pid;
		}
		//aliexpress
		if( int_type==7 )
		{
			pid = StrUtils.matchStr(url, "(/product-fm/\\d+-)")
					.replaceAll("\\D+", "");
			return pid;
		}
		//ebay  amazon
		if( int_type == 13 || int_type == 17 )
		{
			pid = StringUtils.substringAfterLast(url, "/");
			return pid;
		}
		return null;
	}
	
	/**商品名称
	 * @date 2016年4月28日
	 * @author abc
	 * @param p
	 * @param body
	 * @param int_type
	 * @return  
	 */
	public static GoodsBean getNameByBody(GoodsBean p, Element body, int int_type)
	{
		
		String pName = null;
		
		if(int_type == 1)
		{//alibaba
			pName = body.select("h1[class=title fn]").text();
			if(pName.isEmpty())
			{
				pName = body.select("div[class=head]").select("h1").text();
			}
			if(pName.isEmpty())
			{
				pName = body.select("div[class=title]").select("h1").text(); 
			}
		}
		
		if(int_type == 2)
		{//en.alibaba
			Elements names = body.select("div[class=b-property-wrap]").select("h1");
			pName = names.text();
			Elements esName = names.select("span");
			if( !esName.isEmpty() )
			{
				pName = pName.replace(esName.text(), "");
			}
		}
		
		if(int_type == 3)
		{//bestway
			pName = body.select("div[class=b-property-wrap]").select("h1").text();
		}
		
		if(int_type == 4)
		{//www.dhgate.com
			pName = body.select("div[class=mod-detail-title]").select("h1").text();
			if(pName.isEmpty())
			{
				pName = body.select("h1[class=d-title]").text();
			}
		}
		
		if(int_type == 5)
		{//item.taobao.com
			pName = body.select("div[class=mod-detail-title]").select("h1").text();
			if( pName.isEmpty() )
			{
				pName = body.select("h1[class=d-title]").text();
			}
		}
		
		if(int_type == 6)
		{//yiwugou.com
			Elements spans = body.select("div[class=pro-view-nav]").select("span");
			if( spans.size() > 0 ){
				pName = ParseGoodsUrl.filterName(spans.get(0).text());
			}
		}
		
		
		if(int_type == 7 || int_type == 11)
		{//www.aliexpress.com   www.madeinchina.com
			pName = body.select("h1[class=product-name]").text();
		}
		
		if( int_type == 8 )
		{//www.globalsources.com
			pName = body.select("p[class=pp_spd]").text();
		}
		
		if( int_type == 9 )
		{//en.jd.com
			pName = body.select("div[class=p-name]").text();
		}
		
		if( int_type == 10 )
		{//hero-tech.en.made-in-china.com
			pName = body.select("h1[itemprop=name]").text();
		}
		
		if( int_type == 12 )
		{//www.lightinthebox.com
			pName = body.select("div[class=widget prod-info-title]").text();
		}
		
		if( int_type == 13 )
		{//www.ebay.com
			pName = body.select("span[id=vi-lkhdr-itmTitl]").text();
		}
		
		if( int_type == 14 )
		{//www.ecvv.com
			pName = body.select("h1[id=ViewProductname]").text();
		}
		
		if( int_type == 15 )
		{//www.dx.com
			pName = body.select("span[id=headline]").text();
		}
		
		if( int_type == 16 )
		{//www.tinydeal.com
			pName = body.select("h1[id=productName]").text();
		}
		
		if( int_type == 17 )
		{//www.amazon.com
			pName = body.select("span[id=productTitle]").text();
		}
		
		if( int_type == 18 )
		{//detail.tmall.com
			pName = body.select("h1[data-spm=1000983]").text();
		}
		
		if( int_type == 19 )
		{//detail.1688.com
			pName = body.select("div[class=mod-detail-title]").select("h1").text();
			if(pName.isEmpty())
			{
				pName = body.select("h1[class=d-title]").text();
			}
		}
		
		if( int_type == 20 )
		{//small-order.hktdc.com
			pName = body.select("h2[id=productName]").text();
		}
		
		if( int_type == 21 )
		{//www.eelly.com
			pName = body.select("h2[class=gv-gd-name]").text();
		}
		
		p.setpName(pName);
		return p;
	}
	
	
	
	
	

}
