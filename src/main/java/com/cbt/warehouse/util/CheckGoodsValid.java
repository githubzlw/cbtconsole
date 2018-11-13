package com.cbt.warehouse.util;

import com.cbt.parse.service.GoodsBean;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 检查商品的有效性
 * 
 * @author JXW
 *
 */
public class CheckGoodsValid {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CheckGoodsValid.class);

	/**
	 * 根据url判断商品有效性
	 * 
	 * @param urlStr
	 *            商品链接
	 * @param cycle
	 *            重试次数
	 * @return
	 */
	public static boolean parseGoodsByUrl(String urlStr, int cycle) {
		String url = TypeUtils.modefindUrl(urlStr, 1);
		GoodsBean goods = new GoodsBean();
		goods.setValid(1);
		if (!StrUtils.isMatch(url, "(https*://.+)")) {
			LOG.warn("The url is unvalid");
			System.out.println("The url is unvalid");
			return false;
		}
		int tem = cycle;
		Set set = new Set();
		String page = DownloadMainUtil.getJsoup(url, 1, set);

		while (StringUtils.isEmpty(page) && tem < 3) {
			tem++;
			long sw = System.currentTimeMillis();
			page = DownloadMainUtil.getJsoup(url, 1, set);
			LOG.warn("url:" + url + ",tem:" + tem + ",time:" + (System.currentTimeMillis() - sw));
			System.out.println("url:" + url + ",tem:" + tem + ",time:" + (System.currentTimeMillis() - sw));

		}
		// 获取页面失败
		if (StringUtils.isEmpty(page)) {
			LOG.warn("url:" + url + "--------page isEmpty");
			System.out.println("url:" + url + "--------page isEmpty");
			return false;
		}
		// 请求出现错误
		if ("httperror".equals(page)) {
			goods.setValid(0);
			LOG.warn("url:" + url + "-------page httperror");
			System.out.println("url:" + url + "-------page httperror");
			return false;
		}
		// aliexpress俄文
		if (StrUtils.isFind(page, "([БбГгДдЁёЖжЗзИиЙйКкЛлПпФфЦцЧчШшЩщЪъЫыЭэЮюЯян])")) {
			page = DownloadMainUtil.getJsoup(url, 1, set);
			// 获取页面失败
			if (StringUtils.isEmpty(page) || "httperror".equals(page)) {
				goods.setValid(0);
				return false;
			}
		}
		// JSoup解析HTML生成document
		Document doc = Jsoup.parse(page);
		// System.err.println("page:"+page);
		page = null;
		// 获取element
		Elements Links = doc.head().select("Link[hreflang=en]");
		String purl = "";//
		if (Links.size() > 0) {
			purl = Links.size() > 1 ? Links.get(1).attr("href") : Links.get(0).attr("href");
			purl = purl.replace("https://", "http://");
		}
		goods.setpUrl(purl);
		Element body = doc.body();
		String url_title = doc.title();
		//LOG.warn("	product title:" + url_title);
		//System.out.println("	product title:" + url_title);
		// 商品下架
		String text = body.select("div[class=shadow-text]").select("h1").text();
		Elements available = body.select("div[id=no-longer-available]");
		String detail_404 = body.select("div[class=m-detail-404]").text();
		String no_found = "Page Not Found - Aliexpress.com";
		if ("Oops!".equals(text) || no_found.equals(url_title) || available.size() > 0 || !detail_404.isEmpty()) {
			goods.setValid(0);
			doc = null;
			body = null;
			LOG.warn("url:" + url + "-------Page Not Found");
			System.out.println("url:" + url + "-------Page Not Found");
			return false;
		} 
		
		/*//另外一种判断？
		String script = body.select("script").toString();
		String offline = StrUtils.matchStr(script, "(?:runParams.offline=)(.*?)(?:;)");
		if ("true".equals(offline)) {
			return false;
		}*/
		return true;

	}

}
