package com.cbt.parse.service;

import com.cbt.parse.bean.DataReturn;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConnectKeyWords {
	
/**请求关键词列表
 * @param url
 * @return
 */
private static String getKeyWords(String url){
	
	String content  ="";//网页内容
	String agent;//伪客户端
	agent = DownloadMain.getAgent();
	String ip = IpSelect.ip();
	try {
		content = Jsoup.connect(url)
				.header("CLIENT-IP", ip)
				.header("X-FORWARDED-FOR", ip)
				.header("Connection", "keep-alive")
				.header("Accept","text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*,q=0.8,q=1.0") 
				.userAgent(agent)
				.timeout(2*1000+500)
				.followRedirects(true)
				.get().toString();
	} catch (IOException e) {
		content = null;
	}
	return content;
}

public static List<DataReturn> getConnectData(String keyword){
//	long st = new Date().getTime();
	List<DataReturn> dlist = null;
	String url=null;
	url = "http://connectkeyword.alibaba.com/lenoIframeJson.htm?iframe_delete=true&varname=intelSearchData&keyword="
			+keyword+ "&searchType=forums_en";
	if(url!=null){
		String page = getKeyWords(url);
		if(page!=null&&!page.isEmpty()){
			dlist = new ArrayList<DataReturn>();
			DataReturn dr;
			List<String> list = DownloadMain.getSpiderContextList1("(?:keywords:')(.*?)(?:',)", page);
			if(list!=null&&!list.isEmpty()){
				if(dlist!=null){
					dlist.clear();
				}
				for(String l:list){
					dr = new DataReturn();
					if(!l.isEmpty()){
						dr.setKeyword(l.trim());
						dlist.add(dr);
					}
					dr = null;
				}
				page = null;
			}
		}
	}
	return dlist;
}
public static List<DataReturn> getConnectAli(String keyword, String wordslist){
	List<DataReturn> dlist = null;
	String url=null;
	if(keyword==null){
		url = null;
	}else{
		keyword = keyword.replaceAll("\\s+", "%20").replace("\\'", "%27").replace("'", "%27").replace("\"", "%22");
		url = "http://connectkeyword.aliexpress.com/lenoIframeJson.htm?iframe_delete=true&varname=intelSearchData&__number=1&catId=0&hiskeyword=&keyword="
				+keyword;
	}
	if(url!=null){
		String page = DownloadMain.getContentClient(url,null);
		if(page!=null&&!page.isEmpty()){
			dlist = new ArrayList<DataReturn>();
			DataReturn dr;
			List<String> list = DownloadMain.getSpiderContextList1("(?:\\{)(.*?)(?:\\})", page);
			if(list!=null&&!list.isEmpty()){
				if(dlist!=null){
					dlist.clear();
				}
				String word = null;
				String word_p = null;
				String catid = null;
				String catname = null;
				String count = null;
				List<String> list2 = DownloadMain.getSpiderContextList1("(?:\\{)(.*?)(?:\\})", wordslist);
				int num = list2.size();
				String in_word = null;
				for(String l:list){
				  if(!l.isEmpty()){
					word = DownloadMain.getSpiderContext(l, "(?:keywords.\\s*')(.*?)(?:',)").trim();
					for(int i=0;i<num;i++){
						in_word = DownloadMain.getSpiderContext(list2.get(i), "(,*word=.*,)").replaceAll(",*word=", "").replaceAll("catid=\\d+", "")
								.replaceAll("cid=\\d+", "").replace(",", "").trim();
						word_p = word.replaceAll("[-\\*\\:&\\+]+", " ");
						if(in_word!=null&&Pattern.compile("("+in_word+")").matcher(word_p).find()){
							word = null;
							break;
						}
					}
					if(word!=null&&!word.isEmpty()){
						dr = new DataReturn();
						dr.setKeyword(word.replace("\\'", "'"));
						catid = DownloadMain.getSpiderContext(l, "(?:catId.\\s*')(.*?)(?:',)").trim();
						catname = DownloadMain.getSpiderContext(l, "(?:catName.\\s*')(.*?)(?:',)").trim();
						count = DownloadMain.getSpiderContext(l, "(?:count.\\s*')(.*?)(?:')").trim();
						if(!catid.isEmpty()){
							dr.setCatId(catid);
						}
						if(!catname.isEmpty()){
							dr.setCatName(catname.replace("\\'", "'"));
						}
						dr.setCount(count);
						dlist.add(dr);
						dr = null;
						word = null;
						catid = null;
						catname = null;
						count = null;
					}
					}
				}
				page = null;
			}
		}
	}
	return dlist;
}

}
