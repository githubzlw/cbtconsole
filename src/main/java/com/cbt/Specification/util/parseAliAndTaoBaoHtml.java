package com.cbt.Specification.util;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class parseAliAndTaoBaoHtml {

	/**
	 * 根据1688的url解析规格详情区的数据
	 * 
	 * @param url
	 * @return
	 */
	public static List<String> parseDetailBy1688Url(String url) {
		// String testUrl =
		// "https://detail.1688.com/offer/543794868623.html?spm=a2604.8117111.ix8ra8z9.7.XnRx7M";
		Response response = null;
		List<String> reStrings = new ArrayList<String>();
		try {
			response = Jsoup.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
					.timeout(30000).method(Connection.Method.GET).execute();
			Document html1688 = response.parse();
			if (html1688 == null) {
				System.out.println("1688 Html Is Null");
			} else {
				Elements offerdetailDivs = html1688.select("#mod-detail-attributes").first()
						.getElementsByClass("obj-content").first().getElementsByTag("table").first()
						.getElementsByTag("tbody").first().getElementsByTag("tr");
				for (Element trElement : offerdetailDivs) {
					Elements tds = trElement.getElementsByTag("td");
					for (Element tdElement : tds) {
						reStrings.add(tdElement.text());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error : 1688 Html Parse Failure , " + e.getMessage());
		}
		return filterSpecification(reStrings);
	}

	/**
	 * 根据taobao的url解析规格详情区的数据
	 * 
	 * @param url
	 * @return
	 */
	public static List<String> parseDetailByTaobaoUrl(String url) {
		// String testUrl =
		// "https://item.taobao.com/item.htm?spm=a1z0d.6639537.1997196601.3.RluwSI&id=17226980287";
		Response response = null;
		List<String> reStrings = new ArrayList<String>();
		try {
			response = Jsoup.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
					.timeout(30000).method(Connection.Method.GET).execute();
			Document htmlTaobao = response.parse();
			if (htmlTaobao == null) {
				System.out.println("Taobao Html Is Null");
			} else {
				Elements attributesUls = htmlTaobao.getElementsByClass("attributes").first().getElementsByTag("ul")
						.first().getElementsByTag("li");
				for (Element liElement : attributesUls) {
					String[] liElementTextLst = liElement.text().replaceAll("&nbsp", "").split(":");
					if (liElementTextLst.length == 2) {
						reStrings.add(liElementTextLst[0].trim());
						reStrings.add(liElementTextLst[1].trim());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error : Taobao Html Parse Failure , " + e.getMessage());
		}
		return filterSpecification(reStrings);
	}

	/**
	 * 将url中不需要的规格名称过滤
	 * 
	 * @param reStrings
	 * @return
	 */
	private static List<String> filterSpecification(List<String> reStrings) {

		// 过滤读取html页面节点信息时，最后一个节点有规格名但没有属性值存在的情况
		int conunt = reStrings.size() % 2 == 0 ? reStrings.size() / 2 : (reStrings.size() - 1) / 2;
		List<String> nwReStrings = new ArrayList<String>();
		for (int i = 0; i < conunt; i++) {
			String spcName = reStrings.get(i * 2);
			if (spcName.equals("货号") || spcName.equals("产地") || spcName.equals("主图来源") || spcName.equals("货源类型")
					|| spcName.equals("上市年份") || spcName.equals("品牌")) {
				continue;
			} else {
				nwReStrings.add(reStrings.get(i * 2));
				nwReStrings.add(reStrings.get(i * 2 + 1));
			}
		}
		return nwReStrings;
	}

}
