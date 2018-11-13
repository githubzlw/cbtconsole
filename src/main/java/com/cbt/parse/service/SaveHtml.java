package com.cbt.parse.service;

import com.cbt.parse.bean.GoodsExpandBean;
import com.cbt.parse.dao.GoodsHtmlDao;
import com.cbt.parse.dao.SaveKeyDao;
import com.cbt.parse.daoimp.IGoodsHtmlDao;
import com.cbt.parse.daoimp.ISaveKeyDao;
import com.cbt.parse.thread.SaveThread;
import com.cbt.util.AppConfig;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SaveHtml {
	public static Boolean isRun_search = true;
	public static Boolean isRun_goods = true;
//	private static  String filepath;
	private static final Log LOG = LogFactory.getLog(SaveHtml.class);
	
	/**解析url内容，写入html文件
	 * @param url
	 * @param flag  true:商品单页 false:搜索页面
	 * @throws Throwable 
	 */
	public  Boolean saveGoods(String url,String path,boolean flag){
		isRun_goods = false;
		Boolean return_flag;
		String tem_url = url;
		String tem_page = null;
		if(tem_url!=null&&!tem_url.isEmpty()){
			//如果是商品页面  先要获取商品的keywords  description
			String keywords = null;
			String desctiption = null;
			if(flag){
				String goods_url = DownloadMain.getSpiderContext(tem_url, "&url=.*").replaceAll("&url=", "").trim();
				//先查询数据库是否存在keywords   description
				/*IGoodsExpandDao expand = new GoodsExpandDao();
				ArrayList<GoodsExpandBean> list = expand.queryKeyDesc(goods_url);
				if(list!=null&&!list.isEmpty()){
					//存在则读取keywords   description
					keywords = list.get(0).getKeywords();
					desctiption = list.get(0).getDescription();
				}*/
				//数据库不存在keywords 则重新获取
				if(keywords==null||keywords.isEmpty()){
					if(!goods_url.isEmpty()&&Pattern.compile("(http://.+)").matcher(goods_url).matches()){
						int count =0;
						String page = DownloadMain.getJsoup(goods_url,1,null);
						while((page==null||page.isEmpty())&&count<3){
							page = DownloadMain.getJsoup(goods_url,1,null);
						}
						if(page!=null&&!"httperror".equals(page)){
							//JSoup解析HTML生成document
							Document doc = Jsoup.parse(page);
							//获取element
							Element head = doc.head();
							keywords = head.select("meta[name=keywords]").attr("content");
							desctiption = head.select("meta[name=description]").attr("content");
							doc = null;
							head = null;
							//更新数据库  keywords   description
							String pid = DownloadMain.getSpiderContext(goods_url, "(/\\d+\\.htm)");
							if(pid!=null&&!pid.isEmpty()){
								pid = pid.replaceAll("(\\D+)", "").trim();
							}else{
								pid = DownloadMain.getSpiderContext(goods_url, "(_\\d+\\.htm)");
								if(pid!=null&&!pid.isEmpty()){
									pid = pid.replaceAll("(\\D+)", "").trim();
								}else if(Pattern.compile("(en\\.alibaba)").matcher(goods_url).find()){
									pid = DownloadMain.getSpiderContext(url, "(/product/\\d+-)");
									pid = pid.replaceAll("(\\D+)", "").trim();
								}else{
									pid = "0000";
								}
							}
							GoodsExpandBean bean = new GoodsExpandBean();
							bean.setCid(TypeUtils.getType(goods_url)+"");
							bean.setPid(pid);
							bean.setKeywords(keywords);
							bean.setUrl(goods_url);
							bean.setDescription(desctiption);
							/*if(list!=null&&!list.isEmpty()){
								expand.updateAll(bean);
							}else{
								expand.addAll(bean);
							}*/
							bean = null;
						}
						page= null;
					}
				}
				/*expand = null;*/
			}
			//获取静态页面
			tem_page = "";
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.IncorrectnessListenerImpl").setLevel(java.util.logging.Level.OFF);
	        HtmlPage pagehtml;
	        WebClient wc = null;  
	        NicelyResynchronizingAjaxController ctr = null;
			try {
				 wc = new WebClient(BrowserVersion.FIREFOX_45);
				wc.getOptions().setUseInsecureSSL(false);
				wc.getOptions().setDoNotTrackEnabled(true);
				wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
				wc.getOptions().setCssEnabled(false); //启用css支持  
				wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
				wc.getOptions().setTimeout(5*1000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
				wc.waitForBackgroundJavaScript(10*1000);
				wc.setJavaScriptTimeout(10*1000);
				ctr = new NicelyResynchronizingAjaxController();
				wc.setAjaxController(ctr);
				pagehtml = wc.getPage(tem_url);
				tem_page = pagehtml.asXml(); //以xml的形式获取响应文本  
			} catch (Exception e) {
			}finally{
				ctr = null;
				pagehtml = null;
//				wc.closeAllWindows();
				wc = null;
			}
			tem_page = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"+tem_page;
			tem_page =  tem_page.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
						.replace("<script src=\"//www.google-analytics.com/analytics.js\">", "");
			//商品单页静态化
			if(flag){
				if(keywords!=null&&!keywords.isEmpty()){
					desctiption = desctiption.replaceAll("(www)*\\.*wholesaler\\.alibaba\\.*(com)*","")
											 .replaceAll("(www)*\\.*alibaba\\.*(com)*","")
											 .replaceAll("(www)*\\.*aliexpress\\*.(com)*","")
											 .replaceAll("\"", "");
					
					String pname = DownloadMain.getSpiderContext(tem_page, "(?:pname=\")(.*?)(?:\"\\+1;)");
					String newReplace = new StringBuilder().append("  <title>")
							.append(pname)
							.append(" |import-express </title>")
							.append("<meta name=\"keywords\" content=\"")
							.append(keywords)
							.append("\"/>")
							.append("<meta name=\"description\" content=\"")
							.append(desctiption)
							.append("\"/>").toString();
					tem_page = tem_page.replaceAll("\\s{3}", "");
					//tem_page = tem_page.replace("document.title = pname == 1 ? \"import-express\" : pname;", "");
					tem_page = tem_page.replaceAll("<title>\\s*import-express</title>",newReplace) ;
					//tem_page = tem_page.replace("id=\"li-currenyid\" style=\"display:block;","id=\"li-currenyid\" style=\"display:none;") ;
					tem_page = tem_page.replaceAll("//<!\\[CDATA\\[", "").replaceAll("//\\]\\]>", "");
					
					List<String> list = DownloadMain.getSpiderContextList1("(?:<!--)(.*?)(?:-->)", tem_page);
					for(int i=0;i<list.size();i++){
						tem_page = tem_page.replace(list.get(i), "");
					}
					tem_page = tem_page.replaceAll("<!--\\s*-->", "");
				}else{
					tem_page = null;
				}
			}else{
				tem_page = tem_page.replaceAll("\\s{3}", "");
			}
		}
		if(path!=null&&!path.isEmpty()&&tem_page!=null&&!tem_page.isEmpty()){
			String tem_path  = path;
			File file = new File(tem_path); 
			try {
				if (!file.exists()) {//测试此抽象路径名表示的文件或目录是否存在。 
					file.getParentFile().mkdirs();//返回此抽象路径名父目录的抽象路径名；创建
					file.createNewFile();//不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件
				}
				FileUtils.writeStringToFile(file, tem_page, "utf-8");
				if(file.exists()&&(file.length()/1024)>20){
					return_flag = true;
				}else{
					file.delete();
					return_flag = false;
				}
			} catch (IOException e) {
				return_flag = false;
			} 
			file = null;
			tem_path = null;
			tem_url = null;
			tem_page = null;
		}else{
			return_flag = false;
			tem_page = null;
		}
		isRun_goods = true;
		return return_flag;
	}
	/**解析url内容，写入html文件
	 * @param url
	 * @param flag  true:商品单页 false:搜索页面
	 * @throws Throwable 
	 */
	public  Boolean saveSearch(String url,String path,boolean flag){
		isRun_search = false;
		Boolean return_flag;
		String tem_url = url;
		String tem_page = null;
		if(tem_url!=null&&!tem_url.isEmpty()){
			//获取静态页面
			tem_page = "";
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.IncorrectnessListenerImpl").setLevel(java.util.logging.Level.OFF);
			HtmlPage pagehtml;
			WebClient wc = null;  
			NicelyResynchronizingAjaxController ctr = null;
			try {
				wc = new WebClient(BrowserVersion.FIREFOX_45);
				wc.getOptions().setUseInsecureSSL(false);
				wc.getOptions().setDoNotTrackEnabled(true);
				wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
				wc.getOptions().setCssEnabled(false); //启用css支持  
				wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
				wc.getOptions().setTimeout(5*1000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
				wc.waitForBackgroundJavaScript(10*1000);
				wc.setJavaScriptTimeout(10*1000);
				ctr = new NicelyResynchronizingAjaxController();
				wc.setAjaxController(ctr);
				pagehtml = wc.getPage(tem_url);
				tem_page = pagehtml.asXml(); //以xml的形式获取响应文本  
			} catch (Exception e) {
			}finally{
				ctr = null;
				pagehtml = null;
				///wc.closeAllWindows();
				wc = null;
			}
			tem_page = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"+tem_page;
			tem_page =  tem_page.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
					.replace("<script src=\"//www.google-analytics.com/analytics.js\">", "");
		}
		if(path!=null&&!path.isEmpty()&&tem_page!=null&&!tem_page.isEmpty()){
			String tem_path  = path;
			File file = new File(tem_path); 
			try {
				if (!file.exists()) {//测试此抽象路径名表示的文件或目录是否存在。 
					file.getParentFile().mkdirs();//返回此抽象路径名父目录的抽象路径名；创建
					file.createNewFile();//不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件
				}
				FileUtils.writeStringToFile(file, tem_page, "utf-8");
				if(file.exists()&&(file.length()/1024)>20){
					return_flag = true;
				}else{
					file.delete();
					return_flag = false;
				}
			} catch (IOException e) {
				return_flag = false;
			} 
			file = null;
			tem_path = null;
			tem_url = null;
			tem_page = null;
		}else{
			return_flag = false;
			tem_page = null;
		}
		isRun_search = true;
		return return_flag;
	}
	
 	/**利用htmlunit获取网页数据
	 * @param url
	 * @return
	 */
	/*private static String getContentUnit(String url){
		  turn off annoying htmlunit warnings 
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.IncorrectnessListenerImpl").setLevel(java.util.logging.Level.OFF);
		String page ="";
        HtmlPage pagehtml;
        WebClient wc = null;  
        NicelyResynchronizingAjaxController ctr = null;
		try {
			 wc = new WebClient(BrowserVersion.FIREFOX_17);
			wc.getOptions().setUseInsecureSSL(false);
			wc.getOptions().setDoNotTrackEnabled(true);
			wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true  
			wc.getOptions().setCssEnabled(false); //启用css支持  
			wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
			wc.getOptions().setTimeout(5*1000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
			wc.waitForBackgroundJavaScript(10*1000);
			wc.setJavaScriptTimeout(10*1000);
			ctr = new NicelyResynchronizingAjaxController();
			wc.setAjaxController(ctr);
			pagehtml = wc.getPage(url);
			page = pagehtml.asXml(); //以xml的形式获取响应文本  
		} catch (Exception e) {
			
		}finally{
			ctr = null;
			pagehtml = null;
			wc.closeAllWindows();
			wc = null;
		}
		page = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"+page;
		page =  page.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
					.replace("<script src=\"//www.google-analytics.com/analytics.js\">", "");
		return page;
	}*/
	
	
	/**获取静态页面存在状态
	 * @param flag true-商品单页 false-搜索页面
	 * @param url 需要保存的页面链接
	 * @param key  （flag=true keyword为商品单页静态文件名称  flag=false keyword为搜索关键词）
	 * @param path 静态文件保存路径
	 * @param website （flag=true website为商品原网站链接  flag=false website为网站来源）
	 * @return
	 */
	public  String getSave(Boolean flag,String url,String key,String path,String website){
		String tem_url = url;
		String tem_key = key;
		String tem_path = path;
		String tem_website = website;
		String result;
		SaveThread sThread = null;
		if((!flag)&&tem_key!=null&&!tem_key.isEmpty()){
			tem_key = tem_key.toLowerCase().trim();
			String tem = tem_key.substring(0,tem_key.length()-1);
			if(tem_key.equals(SearchUtils.largeWord(tem))){
				tem_key = tem;
			}
		}
		
		//1.根据传来的关键字首先查询数据库最新数据是否已经有保存的静态页面
		ArrayList<HashMap<String,String>> list = null;
		if(flag){
			//商品单页静态文件数据查询
			IGoodsHtmlDao dao = new GoodsHtmlDao();
			list = dao.query(tem_key);
			dao = null;
		}else{
			//搜索页面静态文件数据查询
			ISaveKeyDao sd = new SaveKeyDao();
			list = sd.queryData(tem_key, tem_website);
			sd = null;
		}
		
		if(list!=null&&!list.isEmpty()){
			//①数据库已经有此关键字数据，比较时间戳，是否是最新的（相差时间不能超过一周）
			String timeOld = list.get(0).get("time");
			Boolean isNew = TypeUtils.isNew(timeOld,7.0);
			if(isNew){
				//时间戳是最新的
				String temfile = list.get(0).get("file").replaceAll("\\\\", "/");
				if(temfile!=null&&!temfile.isEmpty()){
					//1.匹配数据库以保存的文件名是否合理
					File tem_file = new File(temfile); 
					if (tem_file.exists()&&tem_path.equals(temfile)&&tem_file.length()/1024>20){
						//静态文件存在
						result = temfile;
					}else{
						//静态文件不存在
						sThread = new SaveThread(flag,tem_url, tem_key, tem_website, tem_path, false);
						result = null;
					}
				}else{
					//2.匹配数据库以保存的文件名不合理
					sThread = new SaveThread(flag,tem_url, tem_key, tem_website, tem_path, false);
					result = null;
				}
			}else{
				//时间戳不是最新的
				result = null;
				sThread = new SaveThread(flag,tem_url, tem_key, tem_website, tem_path, false);
			}
		}else{
			//②数据库中没有关键字数据，则保存静态页面
			result = null;
			sThread = new SaveThread(flag,tem_url, tem_key, tem_website, tem_path, true);
		}
		if(sThread!=null){
			sThread.start();
		}
		tem_url = null;
		tem_key = null;
		tem_path = null;
		tem_website = null;
		return result;
	}
	
	/**返回静态文件名称
	 * @return
	 */
	/*public static String getFile(){
		if(filepath!=null&&!filepath.isEmpty()){
			File file = new File(filepath); 
			if (file.exists()){
				return filepath;
			}
				return null;
		}
		return null;
	}
	private static void setFile(String file){
		filepath = file;
	}*/
	
	/**搜索页面的静态页面查询  若存在 直接将静态页面返回给客户端   不存在 则保存静态页
	 * @return
	 */
	public  String searchHtml(String catid,String keyword,String price1,String price2,
			String minq,String maxq,String website,String sort,String keyurl,
			String file,String index){
		long st = new Date().getTime();
		String flag = null;
		StringBuffer sbtem = null;
		if(Pattern.compile("(import-express)|(198.38.90.14)").matcher(AppConfig.ips).find()){
			if(SaveHtml.isRun_search&&(keyword!=null&&!keyword.isEmpty())){
				if(index!=null||(catid!=null&&"0".equals(catid)&&price1.isEmpty()&&price2.isEmpty()&&minq.isEmpty()&&maxq.isEmpty()
					&&("order-desc".equals(sort)||"total_tranpro_desc".equals(sort))
					&&!website.isEmpty()&&(keyurl==null||keyurl.isEmpty()))){
					
					
					sbtem = new StringBuffer();
					sbtem.append(AppConfig.ip)
					.append("/goodsTypeServerlet?keyword=")
					.append(keyword.replaceAll("\\s+", "%20").replaceAll("&", "%26"));
					if(index!=null){
						sbtem.append("&price1=").append(price1)
							 .append("&price2=").append(price2)
							 .append("&minq=").append(minq)
							 .append("&maxq=").append(maxq)
							 .append("&website=").append(website)
							 .append("&srt=").append(sort)
							 .append("&catid=").append(catid)
							 .append("&keyurl=");
						if(keyurl!=null){
							sbtem.append(keyurl);
						}
					}else{
						sbtem.append("&price1=&price2=&minq=&maxq=&website=")
							 .append(website).append("&catid=0").append("&srt=order-desc&keyurl=");
					}
					
					String tem_keyword = SearchUtils.fileName(keyword);
					if(tem_keyword!=null){
						tem_keyword = tem_keyword.toLowerCase().trim();
					}
					keyword = keyword.replaceAll("\"+", "%22")
									 .replaceAll("\\s+", "%20")
									 .replaceAll("(\\+)+", "%20")
					                 .replaceAll("(%20)+", "%20");
					StringBuffer path = new StringBuffer() ;
					path.append(file);
					if(index!=null){
						path.append("index/");
						website += "i";
					}else{
						if("a".equals(website)){
							path.append("free/");
						}else if("w".equals(website)){
							path.append("wholesale/");
						}else if("h".equals(website)){
							path.append("factory/");
						}
					}
					String url = sbtem.toString();
					sbtem = null;
					path.append(tem_keyword).append(".html").toString();
					String save = getSave(false,url, tem_keyword, path.toString(), website);
					path = null;
					if(save!=null){
						//已经有静态页面,直接将静态页面返回给客户端
						flag = save;
					}else{
						//没有静态页面
						flag =null;
					}
					tem_keyword = null;
					path = null;
					url = null;
				}else{
					flag = null;
				}
			}else{
					flag = null;
			}
		}
		LOG.warn("static html execute:"+(new Date().getTime()-st));
		return flag;
	}
	
	/**搜索页面的静态页面查询  若存在 直接将静态页面返回给客户端   不存在 则保存静态页
	 * @return
	 */
	public String goodsHtml(String url,String sp){
		long st = new Date().getTime();
		String flag = null;
		if(Pattern.compile("(import-express)|(198.38.90.14)").matcher(AppConfig.ips).find()){
			if(SaveHtml.isRun_goods){
				StringBuffer sbtem = new StringBuffer();
				url = TypeUtils.modefindUrl(url, 1);
				sbtem.append(AppConfig.ip).append("/processesServlet?action=getSpider&className=SpiderServlet&session=false&url=")
					 .append(url);
				if(sp!=null){
					sbtem.append("&sp="+sp);
				}
				String pid = DownloadMain.getSpiderContext(url, "(/\\d+\\.htm)");
				
				if(pid!=null&&!pid.isEmpty()){
					pid = pid.replaceAll("(\\D+)", "").trim();
				}else{
					pid = DownloadMain.getSpiderContext(url, "(_\\d+\\.htm)");
					if(pid!=null&&!pid.isEmpty()){
						pid = pid.replaceAll("(\\D+)", "").trim();
					}else if(Pattern.compile("(en\\.alibaba)").matcher(url).find()){
						pid = DownloadMain.getSpiderContext(url, "(/product/\\d+-)");
						pid = pid.replaceAll("(\\D+)", "").trim();
					}else{
						pid = "0000";
					}
				}
				//商品id有效
				if(pid!=null&&!"0000".equals(pid)){
					String filename = TypeUtils.getType(url)+"-"+pid;
					String path = TypeUtils.path+"/Cbt/products/"+filename+".html";
					String save = getSave(true,sbtem.toString(), filename, path,url );
					if(save!=null){
						//已经有静态页面,直接将静态页面返回给客户端
						flag = save;
					}else{
						//没有静态页面
						flag =null;
					}
				}else{
					//商品id无效
					flag = null;
				}
				url = null;
			}else{
				flag = null;
			}
		}
		LOG.warn("static html execute:"+(new Date().getTime()-st));
		return flag;
	}

}
