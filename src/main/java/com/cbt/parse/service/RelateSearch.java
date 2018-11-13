package com.cbt.parse.service;

import com.cbt.parse.bean.SearchWordBean;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RelateSearch {
	
	public static List<SearchWordBean> searchRelate(Element body){
		List<SearchWordBean> list = new ArrayList<SearchWordBean>();
		String website = null;
		String type = null;
		String url = null;
		String name = null;
		SearchWordBean bean;
		Elements relate = body.select("div[class=related-search util-clearfix]");
		Elements dotted = body.select("div[class=cross]");
		if(dotted!=null&&!dotted.isEmpty()){
			relate.addAll(dotted);
		}
		if(relate.isEmpty()){
			relate = body.select("div[class=related-keywords-box util-clearfix]");
		}
		if(relate!=null&&!relate.isEmpty()){
			for(Element r:relate){
				type = r.select("h3").text();
				if(!Pattern.compile("(Manufacturers)").matcher(type).find()&&
						!Pattern.compile("(suppliers)").matcher(type).find()	
				   ){
					Elements relate_a = r.select("a");
					if(relate_a!=null&&!relate_a.isEmpty()){
						for(Element a:relate_a){
							bean = new SearchWordBean();
							name = a.text();
							url = DownloadMain.getSpiderContext(a.toString(), "(?:href=\")(.*?)(?:\")");
							if(Pattern.compile("http://wholesale.alibaba.com/").matcher(url).find()){
								website = "w";
							}else if(Pattern.compile("http://www.alibaba.com/").matcher(url).find()){
								website = "h";
							}else if(Pattern.compile("").matcher(url).find()){
								website = "a";
							}
							url = TypeUtils.encodeSearch(url);
							if(!name.isEmpty()&&!url.isEmpty()){
								bean.setWebsite(website);
								bean.setType(type);
								bean.setName(name);
								bean.setUrl(url);
								list.add(bean);
							}
							bean = null;
							url  =null;
							name = null;
							website = null;
						}
					}
					relate_a = null;
				}
				type = null;
			}
		}
		return list;
	}
	
}
