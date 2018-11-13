package com.cbt.website.servlet;

import com.cbt.bean.GoodsCheckBean;
import com.cbt.website.bean.GoodsSource;
import com.cbt.website.service.AliExpress240Sercive;
import com.cbt.website.service.IAliExpress240Sercive;
import com.cbt.website.thread.DownloadImgThred;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Aliexpress_top120 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(Aliexpress_top120.class);
    public Aliexpress_top120() {
        super();
    }
    
	/*protected void search_aliexpress(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String searchinfo_req = request.getParameter("searchinfo");
		String sort_res = request.getParameter("sort");
		String sort_ = "";
		String price1 = "";
		if(Utility.getStringIsNull(sort_res)){
			if(sort_res.equals("1")){//销量从高到低
				sort_ = "order-desc";
			}else{//价格从低到高
				sort_ = "bbPrice-asc";
			}
		}
		try {
			String[] searchinfos = searchinfo_req.split("@");
			List<String[]> searchinfoList = new ArrayList<String[]>();
			for (int i = 0; i < searchinfos.length; i++) {
				String[] searchinfo_split = searchinfos[i].split(",");
				searchinfoList.add(searchinfo_split);
			}
			IAliExpress240Sercive service = new AliExpress240Sercive();
			searchinfoList = service.getSearch240_type(searchinfoList);
			List<AliExpressTop240Bean> aliList = new ArrayList<AliExpressTop240Bean>();
			GoodsTypeServerlet goodstype = new GoodsTypeServerlet();
			int page_amount = 0;
			int res = 0;
			for (int i = 0; i < searchinfoList.size(); i++) {
				String[] searchinfo = searchinfoList.get(i);
				String keyword = searchinfo[0];
				String catid = searchinfo[1];
				if("bbPrice-asc".equals(sort_)){
					price1 =price1.isEmpty()? GetFilterUtils.priceFilter(catid):price1;
					price1 = price1==null?"":price1;
				}
				Map<String, Object> map =goodstype.web(catid, keyword, "", price1, "", "", "a", sort_, "", request, false);
				if(map.size()>0){
					ArrayList<SearchGoods> searchGoods = (ArrayList<SearchGoods>) map.get("responsetext");
					List<String> page_urls = new ArrayList<String>();
					for (int j = 0; j < searchGoods.size(); j++) {
						if("next page".equalsIgnoreCase(searchGoods.get(j).getKey_type())){
							String page_str = searchGoods.get(j).getKey_name();
							String[] pages = page_str.split("\"&");
							for (int k = 1; k < pages.length-1 && k < 6; k++) {
								String page_url = pages[k].split("\">")[0];
								if(Utility.getStringIsNull(page_url)){
									String[] k0123 = page_url.split("&");
									String encodeUrl = "http://"+TypeUtils.decodeUrl(k0123[2]+k0123[0]+k0123[3]+k0123[1]);
									page_urls.add(encodeUrl);
								}
							}
						}
						if("page amount".equalsIgnoreCase(searchGoods.get(j).getKey_type())){
							page_amount = Integer.parseInt(searchGoods.get(j).getKey_name());
						}
						if("goods".equalsIgnoreCase(searchGoods.get(j).getKey_type())){
							//添加数据
							AliExpressTop240Bean aliExpressTop240Bean = new AliExpressTop240Bean();
							aliExpressTop240Bean.setGname(searchGoods.get(j).getGoods_name());
							aliExpressTop240Bean.setAliexpress_url(searchGoods.get(j).getGoods_url());
							String image = searchGoods.get(j).getGoods_image();
							if(Utility.getStringIsNull(image)){
								aliExpressTop240Bean.setGimgurl(image);
//								String filename = image.substring(image.lastIndexOf("/"),image.length());
								String[] url_img = {searchGoods.get(j).getGoods_url(),image};
								DownloadImgThred.listimg.add(url_img);
//								aliExpressTop240Bean.setImg("http://www.china-clothing-wholesale.com/hotproduct/" + filename);
							}
							aliExpressTop240Bean.setKeyword(keyword);
							aliExpressTop240Bean.setPrice(searchGoods.get(j).getGoods_price());
							aliExpressTop240Bean.setSales(Utility.getIsInt(searchGoods.get(j).getGoods_solder())?Integer.parseInt(searchGoods.get(j).getGoods_solder()):0);
							aliExpressTop240Bean.setGfree(searchGoods.get(j).getGoods_free());
							aliExpressTop240Bean.setTypeid(catid);
							aliExpressTop240Bean.setSort(sort_res);
							aliExpressTop240Bean.setMinOrder(searchGoods.get(j).getGoods_minOrder());
							aliList.add(aliExpressTop240Bean);
						}
					}
					int results_typeid = service.saveSearch240_type(keyword, catid, page_amount);
					for (int j = 0; j < page_urls.size(); j++) {
						Map<String, Object> map_page =goodstype.web(catid, keyword, "", "", "", "", "a", sort_, page_urls.get(j), request, false);
						ArrayList<SearchGoods> searchGoods_page = (ArrayList<SearchGoods>) map_page.get("responsetext");
						for (int k = 0; k < searchGoods_page.size(); k++) {
							if("goods".equalsIgnoreCase(searchGoods_page.get(k).getKey_type())){
								//添加数据
								AliExpressTop240Bean aliExpressTop240Bean = new AliExpressTop240Bean();
								aliExpressTop240Bean.setGname(searchGoods_page.get(k).getGoods_name());
								aliExpressTop240Bean.setAliexpress_url(searchGoods_page.get(k).getGoods_url());
								String image = searchGoods.get(k).getGoods_image();
								aliExpressTop240Bean.setGimgurl(image);
								if(Utility.getStringIsNull(image)){
//									String filename = image.substring(image.lastIndexOf("/"),image.length());
									String[] url_img = {searchGoods_page.get(k).getGoods_url(),image};
									DownloadImgThred.listimg.add(url_img);
//									aliExpressTop240Bean.setImg("http://www.china-clothing-wholesale.com/hotproduct/" + filename);
								}
//								FileTool.upLoadFromProduction(filename, image);
								aliExpressTop240Bean.setKeyword(keyword);
								aliExpressTop240Bean.setPrice(searchGoods_page.get(k).getGoods_price());
								aliExpressTop240Bean.setSales(Utility.getIsInt(searchGoods_page.get(k).getGoods_solder())?Integer.parseInt(searchGoods.get(k).getGoods_solder()):0);
								aliExpressTop240Bean.setGfree(searchGoods.get(j).getGoods_free());
								aliExpressTop240Bean.setTypeid(catid);
								aliExpressTop240Bean.setSort(sort_res);
								aliExpressTop240Bean.setMinOrder(searchGoods.get(k).getGoods_minOrder());
								aliList.add(aliExpressTop240Bean);
							}
						}
					}
					res += service.saveAliExpress240(aliList, keyword, catid,results_typeid);
					aliList.clear();
				}
				
			}
			aliList = null;
			if(res > 0){
				res = DownloadImgThred.listimg.size();
			}
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	
	//查询搜索关键字处理列表
	public void get_aliexpress_results(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		IAliExpress240Sercive service = new AliExpress240Sercive();
		List<String[]> aliexpress_results = service.getSearch240_type();
		request.setAttribute("results", aliexpress_results);
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("website/aliexpress_top120results.jsp");
		homeDispatcher.forward(request, response);
	}
	
	//显示某个类型的240个淘宝相关图片
	public void show_aliexpress_results_tb(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id_req = request.getParameter("id");
		IAliExpress240Sercive service = new AliExpress240Sercive();
		List<GoodsCheckBean> goodsCheckBeans = service.getGoodsCheckBeans(Integer.parseInt(id_req));
		request.setAttribute("results", goodsCheckBeans);
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("website/aliexpress_top120show_tb.jsp");
		homeDispatcher.forward(request, response);
	}
	
	//保存货源地址
	public void saveAliexpressSourch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String datainfo = request.getParameter("datainfo");
		String typeid = request.getParameter("typeid");//搜素主表的ID
		String search_number = request.getParameter("search_number");//搜素从表中的对应搜索词的总条数
		IAliExpress240Sercive service = new AliExpress240Sercive();
		String res = service.saveGoodsSourch(Integer.parseInt(search_number),Integer.parseInt(typeid),datainfo);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//保存手动添加货源地址
	public void saveAliexpressSourchURL(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String purl = request.getParameter("purl");
		String url = request.getParameter("url");
		String name = request.getParameter("name");
		String state = request.getParameter("state");//原来是否是无货源
		String typeid = request.getParameter("typeid");//搜素主表的ID
		String gid = request.getParameter("gid");//搜素主表的ID
		IAliExpress240Sercive service = new AliExpress240Sercive();
		GoodsSource goodsSource = new GoodsSource();
		goodsSource.setGoodsId(0);
		goodsSource.setGoodsPurl(purl);
		goodsSource.setGoodsUrl(url);
		goodsSource.setSourceType(typeid);
		goodsSource.setGoodsPrice("0");
		goodsSource.setGoodsName(name);
		goodsSource.setOrderDesc("TB".equals(typeid)?10:50);
		int res = service.addGoodsSource(goodsSource, Integer.parseInt(typeid), Integer.parseInt(state),Integer.parseInt(gid));
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//下载图片
	public void downloadImgThred(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		for (int i = 0; i < 10; i++) {
			 new Thread(new DownloadImgThred()).start();
		}
		PrintWriter out = response.getWriter();
		out.print(1);
		out.flush();
		out.close();
	}
	
	//返回当前图片下载剩余数量
	public void downloadImg(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int res = DownloadImgThred.Flag ? DownloadImgThred.listimg.size() : -1;
		out.print(res);
		out.flush();
		out.close();
	}
}
