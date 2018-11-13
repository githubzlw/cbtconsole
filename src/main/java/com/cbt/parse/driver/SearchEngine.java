package com.cbt.parse.driver;

import com.cbt.parse.bean.CatPvdBean;
import com.cbt.parse.bean.ParamBean;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.bean.SqlBean;
import com.cbt.parse.dao.AliCategoryDao;
import com.cbt.parse.dao.CatPvdDao;
import com.cbt.parse.daoimp.IAliCategoryDao;
import com.cbt.parse.daoimp.ICatPvdDao;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SearchEngine {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SearchEngine.class);
	
	/**sql匹配
	 * @param keyword
	 * @return
	 */
	public static ArrayList<SqlBean> pate(ParamBean bean,String cname){
		String keyword = bean.getKeyword();
		if(keyword!=null&&!keyword.isEmpty()){
			ArrayList<SqlBean> sql = new ArrayList<SqlBean>();
			String[] words = keyword.split("[(\\s+)(-+)(;)]");
			int length  = words.length;
			SqlBean sb = new SqlBean();
//			if(!keyword.contains("\\s")){
//				sb.setPara("name");
//				sb.setValue(keyword);
//				sql.add(sb);
//			}
			
			if(length>0){
				for(int i=0;i<length;i++){
					if(!"and".equals(words[i].toLowerCase())){
						sb = new SqlBean();
						sb.setPara("name");
						sb.setValue(words[i]);
						sql.add(sb);
						sb = null;
					}
				}
			}
			
			String pvid = bean.getPvid();//类型pvid
			if(pvid!=null&&!pvid.isEmpty()&&!"0".equals(pvid)){
				if(!"0".equals(pvid)){
					sb = new SqlBean();
					sb.setPara("pid");
					sb.setValue(pvid);
					sql.add(sb);
					sb = null;
				}
			}
			sb = new SqlBean();
			sb.setPara("valid");
			sb.setValue(bean.getValid());
			sql.add(sb);
			
			if(cname!=null&&!cname.isEmpty()){
				sb = new SqlBean();
				sb.setPara("cname");
				sb.setValue(cname);
				sql.add(sb);
				sb = null;
				
			}
			
			String minq = bean.getMinq();//最小订量
			if(minq!=null&&!minq.isEmpty()){
				sb = new SqlBean();
				sb.setPara("minq");
				sb.setValue(minq);
				sql.add(sb);
				sb = null;
			}
			String maxq = bean.getMaxq();//最大订量
			if(maxq!=null&&!maxq.isEmpty()){
				sb = new SqlBean();
				sb.setPara("maxq");
				sb.setValue(maxq);
				sql.add(sb);
				sb = null;
			}
			//价格区间
			String price1 = bean.getMinprice();
			if(price1!=null&&!price1.isEmpty()){
				sb = new SqlBean();
				sb.setPara("price1");
				sb.setValue(price1);
				sql.add(sb);
				sb = null;
				
			}
			String price2 = bean.getMaxprice();
			if(price2!=null&&!price2.isEmpty()){
				sb = new SqlBean();
				sb.setPara("price2");
				sb.setValue(price2);
				sql.add(sb);
				sb = null;
			}
			String catid = bean.getCatid();
			if(catid!=null&&!catid.isEmpty()&&!"0".equals(catid)){
				sb = new SqlBean();
				sb.setPara("cid");
				sb.setValue(String.valueOf(catid));
				sql.add(sb);
				sb = null;
			}
			
			//排序(放在最后  方便sql语句插入)
			String sort = bean.getSort();
			if(sort!=null&&!sort.isEmpty()&&!"default".equals(sort)){
				sb = new SqlBean();
				if("bbPrice-asc".equals(sort)){
					sb.setPara("price");
					sb.setValue("asc");
				}else if("bbPrice-desc".equals(sort)){
					sb.setPara("price");
					sb.setValue("desc");
				}else if("order-desc".equals(sort)){
					sb.setPara("solder");
					sb.setValue("desc");
				}
				sql.add(sb);
				sb = null;
			}
			//分页数据
			int page = bean.getCurrent();
			if(page!=0){
				sb = new SqlBean();
				sb.setPara("page");
				sb.setValue(page+"");
				sql.add(sb);
				sb = null;
			}
			return sql;
		}else{
			return null;
		}
		
	}
	
	/**搜索数据分页
	 * @return
	 */
	public static  ArrayList<SearchGoods>  page(ParamBean bean){
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		int amount = bean.getAmount();
		int current = bean.getCurrent();
		if(amount!=0&&current!=0){
			//page amount
			SearchGoods sg = new SearchGoods();
			sg.setKey_type("page amount");
			sg.setKey_name(String.valueOf(amount));
			list.add(sg);
			//page current
			sg = new SearchGoods();
			sg.setKey_type("page current");
			sg.setKey_name(String.valueOf(current));
			list.add(sg);
			//next page
			String pageinfo = pagesInfo(bean);
			sg = new SearchGoods();
			sg.setKey_type("next page");
			sg.setKey_name(pageinfo);
			list.add(sg);
		}
		LOG.warn("get page num info");
		return list;
	}
	/**分页html拼接,即next page的值
	 * @return
	 */
	private static String pagesInfo(ParamBean bean){
		StringBuffer sb = new StringBuffer();
		String kword = bean.getKeyword();
		kword = kword==null?"":kword.replaceAll("\\s+", "%20");
		StringBuffer sb_href = new StringBuffer();
		sb_href.append("/cbtconsole/").append(bean.getCom())
				.append("?keyword=").append(kword)
				.append("&website=").append(bean.getWebsite());
		String sort = bean.getSort();
		if(sort!=null&&!sort.isEmpty()){
			sb_href.append("&srt=").append(sort);
		}
		String price1 = bean.getMinprice();
		String price2 = bean.getMaxprice();
		if((price1!=null&&!price1.isEmpty())||(price2!=null&&!price2.isEmpty())){
			sb_href.append("&price1=").append(price1).append("&price2=").append(price2);
		}
		String minq = bean.getMinq();
		String maxq = bean.getMaxq();
		if((minq!=null&&!minq.isEmpty())||(maxq!=null&&!maxq.isEmpty())){
			sb_href.append("&minq=").append(minq).append("&maxq=").append(maxq);
		}
		String cid = bean.getCatid();
		if(cid!=null&&!cid.isEmpty()){
			sb_href.append("&catid=").append(cid);
		}
		String pid = bean.getPvid();
		if(pid!=null&&!pid.isEmpty()&&!"0".equals(pid)){
			sb_href.append("&pid=").append(pid);
		}
		String sid = bean.getSid();
		if(sid!=null&&!sid.isEmpty()){
			sb_href.append("&store=1&sid=").append(sid);
		}
		sb_href.append("&page=");
		String href = sb_href.toString();
		int total = bean.getAmount();
		int current = bean.getCurrent();
		//拼接分页链接
		//该产品总页数大于7
		if(total>7){
		  if(total-3>current){
			//当前页小于5
			if(current<5){
				sb.delete(0, sb.length());
				if(current==1){
					sb.append("<span>pre</span>&nbsp;&nbsp;")
					  .append("<span class=\"ui-pagination-active\">1</span>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append("2\">2</a>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append("3\">3</a>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append("4\">4</a>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append("5\">5</a>&nbsp;&nbsp;")
					  .append("<span>....</span>&nbsp;&nbsp;")
					  .append( "<a href=\"").append(href).append(total).append("\">").append(total).append("</a>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append("2\">next</a>");
				}else{
					sb.append("<a href=\"").append(href).append(current-1).append("\">pre</a>&nbsp;&nbsp;");
					for(int i=1;i<6;i++){
						if(i==current){
							sb.append("<span class=\"ui-pagination-active\">").append(i).append("</span>").append("&nbsp;&nbsp;");
						}else{
							sb.append("<a href=\"").append(href).append(i).append("\">").append(i).append("</a>&nbsp;&nbsp;");
						}
					}
					sb.append("<span>....</span>&nbsp;&nbsp;<a href=\"").append(href)
					  .append(total).append("\">").append(total).append("</a>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append(current+1).append("\">next</a>");
				}
		 }else{
				//当前页等于5
			 sb.delete(0, sb.length());
			 sb.append("<a href=\"").append(href).append(current-1).append("\">pre</a>&nbsp;&nbsp;")
			   .append("<a href=\"").append(href).append("1\">1</a>&nbsp;&nbsp;")
			   .append("<a href=\"").append(href).append("2\">2</a>&nbsp;&nbsp;")
			   .append("<span>....</span>&nbsp;&nbsp;");
				if(current==5){
					sb.append("<a href=\"").append(href).append("4\">4</a>&nbsp;&nbsp;")
					  .append("<span class=\"ui-pagination-active\" >5</span>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append("6\">6</a>&nbsp;&nbsp;");
				}else{//当前页大于5
					for(int i=current-1;i<current+2;i++){
						if(i==current){
							sb.append("<span class=\"ui-pagination-active\">").append(i).append("</span>"+"&nbsp;&nbsp;");
						}else{
							sb.append("<a href=\"").append(href+i).append("\">").append(i).append("</a>&nbsp;&nbsp;");
						}
					}
				}
				sb.append("<span>....</span>&nbsp;&nbsp;")
				  .append("<a href=\"").append(href).append(total)
				  .append("\">").append(total).append("&nbsp;&nbsp;")
				  .append("</a><a href=\"").append(href).append(current+1).append("\">next</a>");
			}
		}else{
				//当前页已经属于最后三页的时候
			    sb.delete(0, sb.length());
				sb.append("<a href=\"").append(href).append(current-1).append("\">pre</a>&nbsp;&nbsp;")
				  .append("<a href=\"").append(href).append("1\">1</a>&nbsp;&nbsp;")
				  .append("<a href=\"").append(href).append("2\">2</a>&nbsp;&nbsp;")
				  .append("<span>....</span>&nbsp;&nbsp;");
				for(int i=current-1;i<total;i++){
					if(i==current){
						sb.append("<span class=\"ui-pagination-active\">").append(i).append("</span>&nbsp;&nbsp;");
					}else{
						sb.append("<a href=\"").append(href).append(i).append("\">").append(i).append("</a>&nbsp;&nbsp;");
					}
				}
				if(current==total){
					sb.append("<span class=\"ui-pagination-active\">").append(total).append("</span>&nbsp;&nbsp;")
					  .append("<span >next</span>");
				}else{
					sb.append("<a href=\"").append(href).append(total).append("\">").append(total).append("</a>&nbsp;&nbsp;")
					  .append("<a href=\"").append(href).append(current+1).append("\">next</a>");
				}
			}
		}else{
			//总页数小于7
			sb.delete(0, sb.length());
			if(total!=1){
				if(current==1){
					sb.append("<span>pre</span>&nbsp;&nbsp;");
				}else{
					sb.append("<a href=\"").append(href).append((current-1)).append("\">pre</a>"+"&nbsp;&nbsp;");
				}
				for(int i=1;i<total+1;i++){
					if(i==current){
						sb.append("<span class=\"ui-pagination-active\">").append(i).append("</span>&nbsp;&nbsp;");
					}else{
						sb.append("<a href=\"").append(href+i).append("\">").append(i).append("</a>&nbsp;&nbsp;");
					}
				}
				if(current==total){
					sb.append("<span >next</span>");
				}else{
					sb.append("<a href=\"").append(href).append(current+1).append("\">next</a>");
				}
			}else{
				sb.append("<span >pre&nbsp;&nbsp;</span>")
				  .append("<span class=\"ui-pagination-active\">1</span>")
				  .append("<span >&nbsp;&nbsp;next</span>");
			}
		}
		return sb.toString();
	}
	
	/**aliexpress类别数据拼接
	 * 类别数据通过ali_catpvd表获取
	 * @date 2016年3月1日
	 * @author abc
	 * @param keyword
	 * @param catid
	 * @return  
	 */
	public static ArrayList<SearchGoods> category(String keyword,String catid){
		ArrayList<SearchGoods> list_search = new ArrayList<SearchGoods>();
		ICatPvdDao dao = new CatPvdDao();
		ArrayList<CatPvdBean> list = dao.query(keyword, catid);
		if(list!=null&&!list.isEmpty()){
			String catids = list.get(0).getCatidlist();
			SearchGoods sg = null;
			//类别
			if(catids!=null&&!catids.isEmpty()){
				IAliCategoryDao cate = new AliCategoryDao();
				String[] catis_s = catids.split(",");
				String catid_name = null;
				String catid_id = null;
				for(int i=0;i<catis_s.length;i++){
					catid_id = catis_s[i];
					if(!catid_id.isEmpty()){
						catid_name = cate.getCname(catid_id);
						if(catid_name!=null&&!catid_name.isEmpty()){
							sg = new SearchGoods();
							sg.setKey_type("Related Categories");
							sg.setKey_name(catid_name);
							if(!catid_id.equals(catid)){
								sg.setKey_url("&catid="+catid_id);
							}else{
								sg.setKey_url("&catid="+catid_id+"&k0=id&k1=ov&k2=vo&k3=di");
							}
							list_search.add(sg);
							sg= null;
						}
						catid_name = null;
					}
					catid_id = null;
				}
				catid_name = cate.getCname(catid);
				if(catid_name!=null&&!catid_name.isEmpty()){
					sg = new SearchGoods();
					sg.setKey_type("Related Categories");
					sg.setKey_name(catid_name);
					sg.setKey_url("&k0=id&k1=ov&k2=vo&k3=di");
					list_search.add(sg);
					sg= null;
				}
				catid_name = null;
			}
		}
		return list_search;
	}

}
