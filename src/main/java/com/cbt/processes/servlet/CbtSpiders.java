package com.cbt.processes.servlet;

import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.service.ParseSearchUrl;
import com.cbt.parse.service.SearchUtils;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * 首页谷歌链接过来的相关商品列表
 */
public class CbtSpiders extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CbtSpiders.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CbtSpiders() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter  out = null;
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>() ;
		try{	
			out = resp.getWriter();
			String keyword = req.getParameter("keyword");
			StringBuilder sbtem = new StringBuilder();
			String search = null;
			if(keyword!=null&&!keyword.isEmpty()){
				/*链接关键字搜索需要对特殊字符：空格进行转码*/
				keyword=URLEncoder.encode(keyword, "utf-8");
				keyword = SearchUtils.largeWord(keyword.replaceAll("(\\+)+", "+"));
				sbtem.delete(0, sbtem.length());
				search = sbtem.append("http://www.aliexpress.com/wholesale?catId=0").append("&groupsort=1")
						.append("&site=glo&shipCountry=US&shipFromCountry=cn&g=y&SortType=total_tranpro_desc&SearchText=").append(keyword).toString();	
			}else{
				LOG.warn("keyword is empty");
				return ;
			}
			if(search!=null){
				search=search.replaceAll("\\s+", "%20").replaceAll("(\\+)+", "%20");
				//取得Application对象     过滤屏蔽词汇
		        ServletContext application=this.getServletContext(); 
				Object attribute_words = application.getAttribute("words");
				String inwords = attribute_words!=null?attribute_words.toString():null;
				String intensave = SearchUtils.chenckKey("0",keyword,inwords);
				if("0".equals(intensave)){
					/*获取关键字搜索结果,得到商品列表*/
					ArrayList<SearchGoods> goods = ParseSearchUrl.parseSearch(search,0,keyword,"0",false);
					int goods_num = goods.size();
					SearchGoods searchGoods = null;
					int goods_count = 0;
					int keys_count = 0;
					for(int i=0;i<goods_num;i++){
						searchGoods = goods.get(i);
						if("goods".equals(searchGoods.getKey_type())&&goods_count<8){
							list.add(searchGoods);
							goods_count++;
						}else if("Related Searches".equals(searchGoods.getKey_type())&&keys_count<1){
							list.add(searchGoods);
							keys_count++;
						}else if(goods_count>=8&&keys_count>=1){
							break;
						}
					}
					
					
					if(goods_count>4&&goods_count<8){
						for(int i=0;i<goods_count-4;i++){
							list.remove(i);
						}
					}else if(goods_count<4){
						list.clear();
					}
					
					goods = null;
				}else{
					LOG.warn("search url "+"Contains sensitive words");
					list.clear();
				}
			}
			search = null;
		}catch(Exception e){
			LOG.warn("",e);
		}finally{
			if(out!=null){
				out.print(JSONArray.fromObject(list));
				out.flush();
				out.close();
			}
		}
	}

}
