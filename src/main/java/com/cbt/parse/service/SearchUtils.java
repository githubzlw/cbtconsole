package com.cbt.parse.service;

import com.cbt.bean.ClassDiscount;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.dao.AliCategoryDao;
import com.cbt.parse.dao.IntensveDao;
import com.cbt.parse.dao.WordsDao;
import com.cbt.parse.daoimp.IAliCategoryDao;
import com.cbt.util.Application;
import com.cbt.util.PropertiesFile;
import com.cbt.util.Utility;

import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUtils {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SearchUtils.class);
	
	/**unicode解码
	 * @param str
	 * @return
	 */
	public static String dencodeUnicode(String str){
		String temp = str;
		 Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
			Matcher matcher = pattern.matcher(str);
	        char ch;
	        while (matcher.find()) {
	            ch = (char) Integer.parseInt(matcher.group(2), 16);
	            temp = temp.replace(matcher.group(1), ch + "");    
	        }
		return temp;
	}
	
	/**2015-10-23***
	 * 价格折扣
	 * @param goods
	 * @return
	 */
	public static ArrayList<SearchGoods> discount(ArrayList<SearchGoods> goods){
		if(goods!=null&&!goods.isEmpty()){
			Double discount = PropertiesFile.getDiscount();
			if(discount!=1){
				String price = null;
				Double min_price = 0.0;
				Double max_price = 0.0;
				DecimalFormat format = new DecimalFormat("0.00");
				for(int i=0;i<goods.size();i++){
					price = goods.get(i).getGoods_price();
					if(price!=null&&!price.isEmpty()){
						price = DownloadMain.getSpiderContext(price.replaceAll("(\\s+)|(,)", "").trim(), "(\\d+\\.*\\d*-*\\d*\\.*\\d*)");
						int index = price.indexOf("-");
						if(index>0){
							min_price = Double.valueOf(price.substring(0, index))*discount;
							max_price = Double.valueOf(price.substring(index+1))*discount;
						}else{
							price =  DownloadMain.getSpiderContext(price, "\\d+\\.*\\d*");
							min_price = Double.valueOf(price)*discount;
						}
						if(max_price!=0){
							goods.get(i).setGoods_price(format.format(min_price)+"-"+format.format(max_price));
						}else{
							goods.get(i).setGoods_price(format.format(min_price));
						}
					}
					price = null;
					min_price = 0.0;
					max_price = 0.0;
				}
			}
		}
		return goods;
	}
	
	/**2015-10-29 ylm***
	 * 商品类型混批折扣
	 * @param goods
	 * @return
	 */
	public static ArrayList<SearchGoods> class_discount(ArrayList<SearchGoods> goods, String type, String search, HttpServletRequest request, String cid){
		if(goods!=null&&!goods.isEmpty()){
			//获取混批折扣率
	 		List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
	 		list_cd = Application.getClassDiscount(request);
	 		double deposit_rate = 1;
	 		IAliCategoryDao li_categoryDao = new AliCategoryDao();
	 		if(Utility.getIsInt(cid) && !cid.equals("0")){
	 			String searchs = li_categoryDao.getAliCategory(Integer.parseInt(cid), "");
	 			if(searchs != null){
	 				search = searchs;
	 			}
	 		}
	 		type = Utility.getStringIsNull(type)?type.replaceAll("20%", " ").replaceAll("\\+", " ").replaceAll("%26", "&").replaceAll(" ", ""):null;
	 		search = search.replaceAll("20%", " ").replaceAll("\\+", " ").replaceAll("%26", "&").replaceAll(" ", "");
	 		ok:
	   	 		for (int k = 0; k < list_cd.size(); k++) {
	   	 			String[] classname = list_cd.get(k).getClassname().split(",");
	   	 			for (int i = 0; i < classname.length; i++) {
	   	 			 String classname_ = classname[i].toLowerCase().replaceAll(" ", "");
	   	 			 if( search.toLowerCase().indexOf(classname_) > -1){
	   	 				deposit_rate = list_cd.get(k).getDeposit_rate();
	   	 		    	 break ok;
	   	 			 }else if(Utility.getStringIsNull(type) && type.toLowerCase().indexOf(classname_) > -1){
	   	 				deposit_rate = list_cd.get(k).getDeposit_rate();
	   	 		    	 break ok;
	   	 			 }
					}
	   	 			
	   	 		}
			if(deposit_rate!=1){
				String price = null;
				String max_price = "";
				Double min_price = 0.0;
				DecimalFormat format = new DecimalFormat("0.00");
				for(int i=0;i<goods.size();i++){
					price = goods.get(i).getGoods_price();
					if(price!=null&&!price.isEmpty()){
						price = DownloadMain.getSpiderContext(price.replaceAll("(\\s+)|(,)", "").trim(), "(\\d+\\.*\\d*-*\\d*\\.*\\d*)");
						int index = price.indexOf("-");
						if(index>0){
							min_price = Double.valueOf(price.substring(0, index));
							max_price = "-" + format.format(Double.valueOf(price.substring(index+1))*deposit_rate);
						}else{
							try {
								min_price = Double.valueOf(price);
							} catch (Exception e) {
								LOG.debug("class_discount-131:"+price);
							}
							max_price = "";
						}
						if(min_price <= 150){
							goods.get(i).setGoods_business(format.format(min_price*deposit_rate) + max_price);
						}
					}
				}
			}
		}
		return goods;
	}
	
	/**其他字符解码
	 * @param input
	 * @return
	 */
	public static String decodeString(String input){
		if(input!=null){
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if ('\u3000' == c[i]) {
					c[i] = ' ';
				} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
					c[i] = (char) (c[i] - 65248);
				}
			}
			String dbc = new String(c);
			return dbc;
		}
		return "";
	}
	
	public static String getIntensiveWords(){
		String result = "";
		IntensveDao id = new IntensveDao();
		ArrayList<HashMap<String, String>> list = id.querryIntensive();
		if(list!=null){
			result = list.toString();
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(Pattern.compile("((iphone\\s*\\d*)$)").matcher("iphone 6 ").find());
		
	}
	//[{catid=0, word=adidas, cid=1}, {catid=0, word=armani, cid=1}, {catid=0, word=burberry, cid=1}, {catid=0, word=celine, cid=1}, {catid=0, word=cuuci, cid=1}, {catid=0, word=cartier, cid=1}, {catid=0, word=chanel, cid=1}, {catid=0, word=designerglasses, cid=1}, {catid=0, word=designerpurses, cid=1}, {catid=0, word=designerhandbags, cid=1}, {catid=0, word=designerclothe, cid=1}, {catid=0, word=dior, cid=1}, {catid=0, word=disney, cid=1}, {catid=0, word=dunhill, cid=1}, {catid=0, word=ermenegildo, cid=1}, {catid=0, word=fakebrand, cid=1}, {catid=0, word=fakedesigner, cid=1}, {catid=0, word=fakewatch, cid=1}, {catid=0, word=fisher-price, cid=1}, {catid=0, word=fisherprice, cid=1}, {catid=0, word=fakebag, cid=1}, {catid=0, word=famousbrand, cid=1}, {catid=0, word=fendi, cid=1}, {catid=0, word=ferragamo, cid=1}, {catid=0, word=gucci, cid=1}, {catid=0, word=givenchy, cid=1}, {catid=0, word=(iphone\s*\d*)$, cid=1}, {catid=0, word=(ipad\s*\d*)$, cid=1}, {catid=0, word=luxury handbag, cid=1}, {catid=0, word=miumiu, cid=1}, {catid=0, word=molyneux, cid=1}, {catid=0, word=montblanc, cid=1}, {catid=0, word=nike, cid=1}, {catid=0, word=omega, cid=1}, {catid=0, word=piaget, cid=1}, {catid=0, word=reebok, cid=1}, {catid=0, word=rolex, cid=1}, {catid=0, word=timberland, cid=1}, {catid=0, word=vuitton, cid=1}, {catid=0, word=versace, cid=1}, {catid=0, word=sony ericsson, cid=1}, {catid=0, word=sony%20ericsson, cid=1}, {catid=15, word=seeds, cid=2}, {catid=0, word=nail[(\s*)(%20)*(\+*)].*polish, cid=2}, {catid=15, word=seed, cid=2}, {catid=0, word=designer\s*brand, cid=1}, {catid=0, word=famous\s*brand, cid=1}, {catid=0, word=famous\s*designer, cid=1}, {catid=0, word=Moisturizing\s*Hyaluronic\s*Acid\s*Liquid, cid=2}, {catid=0, word=Moisturizing\s*Hyaluronic, cid=2}, {catid=0, word=Hyaluronic\s*Acid\s*Liquid, cid=2}, {catid=0, word=Acid.*Liquid, cid=2}, {catid=0, word=designer, cid=2}, {catid=5090301, word=samsung, cid=1}, {catid=200002556, word=null, cid=3}]
	
	/**校验搜索关键字是否含有敏感词   2016-01-06
	 * @param keyword
	 * @return不含有敏感词返回true
	 */
	public static String chenckKey(String catid,String keyword,String wordslist){
		String flag = "0";
		try {
			if(keyword!=null&&!keyword.isEmpty()&&wordslist!=null){
				String  key = keyword.toLowerCase().replace("+", " ");
				List<String> list = DownloadMain.getSpiderContextList1("(?:\\{)(.*?)(?:\\})", wordslist);
				if(list!=null&&!list.isEmpty()){
					int size = list.size();
					String cat = null;
					String words =  null;
					String cid = null;
					for(int i=0;i<size;i++){
						flag = "0";
						cat = DownloadMain.getSpiderContext(list.get(i), "(,*catid=\\d+,)").replaceAll(",*catid=", "").replace(",", "").trim();
						words = DownloadMain.getSpiderContext(list.get(i), "(,*word=.*,)").replaceAll(",*word=", "").replaceAll("cid=\\d+", "")
								.replaceAll("catid=\\d+", "").replace(",", "").trim();
						cid = DownloadMain.getSpiderContext(list.get(i), ",*cid=\\d+").replaceAll(",*cid=", "").trim();
						if(words!=null&&"null".equals(words)){
							words = null;
						}
						if(words!=null){
							if(Pattern.compile("("+words.toLowerCase()+")").matcher(key).find()){
								if(!"0".equals(catid)&&!"0".equals(cat)&&!catid.equals(cat)){
									flag = "0";
								}else{
									flag = cid;
									break;
								}
							}
						}else{
							if(cat.equals(catid)){
								flag = cid;
								break;
							}
						}
						cat = null;
					}
				}else{
					flag = "0";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag;
	}
	
	
	/**校验搜索关键字是否含有敏感词
	 * @param keyword-    2015-8-1
	 * @return不含有敏感词返回true
	 */
	public static String chenckKey2(String catid,String keyword){
		String flag = "0";
		if(keyword!=null&&!keyword.isEmpty()){
			String  key = keyword.toLowerCase();
			IntensveDao id = new IntensveDao();
			ArrayList<HashMap<String, String>> list = id.querryIntensive();
			
			if(list!=null&&!list.isEmpty()){
				int size = list.size();
				String cat = null;
				String words =  null;
				for(int i=0;i<size;i++){
					flag = "0";
					cat = list.get(i).get("catid");
					words = list.get(i).get("word");
					if(words!=null){
						if(Pattern.compile("("+words.toLowerCase()+")").matcher(key).find()){
							if(!"0".equals(catid)&&!"0".equals(cat)&&!catid.equals(cat)){
								flag = "0";
							}else{
								flag = list.get(i).get("cid");
								break;
							}
						}
					}else{
						if(cat.equals(catid)){
							flag = list.get(i).get("cid");
							break;
						}
					}
					cat = null;
				}
			}else{
				flag = "0";
			}
		}
		return flag;
	}
	
	/**搜索商品名称分段
	 * @param name
	 * @param length
	 * @return
	 */
	public static String nameVert(String name,int length){
		if(name!=null&&!name.isEmpty()&&length>0){
			String g_name = name.replaceAll("\"", "").toLowerCase().trim();
			g_name = g_name.replaceAll("\\\\", "/");
			String pat = "([hH]ot\\s*[sS]ale,*!*\\:*)|([fF]ree\\s*[sS]hip*ing,*!*\\:*)|(\\d*%*\\s*[bB]rand\\s*[nN]ew,*!*\\:*)"
					+ "|([tT]op\\s*[rR]ated,*!*\\:*)|([lL]owest\\s*[pP]rice,*!*\\:*)|(\\(*[sS]hip\\s*[fF]rom\\s*[uU][sS]\\)*,*!*\\:*)"
					+ "|([hH]igh\\s*[qQ]uality,*!*\\:*)|(2015,*!*\\:*)|(by\\s*dhl\\s*or\\s*ems)";
			if(Pattern.compile(pat).matcher(g_name).find()){
				g_name = g_name.replaceAll(pat, "").replaceAll("\\s+", " ").trim();
			}
			
			String[] split = g_name.split("(\\s+)|(/+)|(-+)|(,+)|(\\*+)");
			int size = split.length;
			String tem = "";
			String result="";
			int tem_length = 0;
			for(int i=0;i<size;i++){
				tem += split[i]+" ";
				tem_length = tem.length();
				
				String digtal = DownloadMain.getSpiderContext(tem, "(\\d+)");
				if(digtal!=null&&!digtal.isEmpty()){
					tem_length = tem_length+digtal.length()*3;
				}
				
				if(tem_length>length){
					tem = tem.replace(split[i], "").trim();
					if(tem.length()>length){
						int tem_in = tem.lastIndexOf(",");
						tem = tem_in>0?tem.substring(0,tem_in):tem;
					}
					result +=tem.trim()+"+";
					tem = split[i]+" ";
				}
			}
			if(tem.length()>length){
				int tem_in = tem.lastIndexOf(",");
				tem = tem_in>0?tem.substring(0,tem_in).trim():tem;
			}
			result +=tem.trim();
			tem = null;
			g_name = null;
			split = null;
			result = result.replaceAll("\\s+", " ");
			return result.trim();
		}
			return "";
	}
	
	/**aliexpress搜索词存在大类的情况处理
	 * @param keyword
	 * @return
	 */
	public static String largeWord(String keyword){
		if(keyword!=null&&!keyword.isEmpty()){
			WordsDao wd = new WordsDao();
			ArrayList<HashMap<String, String>> list = wd.querryData(keyword.toLowerCase());
			while(list!=null&&list.size()>0){
				keyword = keyword+"s";
				list = wd.querryData(keyword.toLowerCase());
			}
			wd = null;
			return keyword;
		}else{
			return "default";
		}
	}
	
	/**文件命名时特殊字符处理
	 * @return
	 */
	public static String fileName(String name){
		String result = "default";
		if(name!=null){
			result = name.replaceAll("(%20)+", "-")
						 .replaceAll("\\s+", "-").replaceAll("(\\+)+", "-")
						 .replaceAll("/", "").replaceAll("\\\\", "")
						 .replaceAll(":", "").replaceAll("\\*", "")
						 .replaceAll("\\?", "").replaceAll("\"", "")
						 .replaceAll("<", "").replaceAll(">", "")
						 .replaceAll("!", "").replaceAll(";", "")
						 .replaceAll("\\.", "").replaceAll(",", "")
						 .replaceAll("`", "").replaceAll("~", "")
						 .replaceAll("@", "").replaceAll("\\$", "")
						 .replaceAll("%", "").replaceAll("^", "")
						 .replaceAll("&", "-").replaceAll("\\(", "")
						 .replaceAll("\\)", "").replaceAll("#", "")
						 .replaceAll("=", "").replaceAll("\\|", "")
						 .replaceAll("%7C", "").replaceAll("%5C", "")
						 .replaceAll("%40", "").replaceAll("%3F", "")
						 .replaceAll("%3E", "-").replaceAll("%3D", "")
						 .replaceAll("%3C", "-").replaceAll("%3B", "")
						 .replaceAll("%3A", "").replaceAll("%2F", "")
						 .replaceAll("%2C", "").replaceAll("%2B", "")
						 .replaceAll("%29", "").replaceAll("%28", "")
						 .replaceAll("%26", "").replaceAll("%25", "")
						 .replaceAll("%22", "").replaceAll("%27", "'");
		}
		return result;
		
	}
	
	/**特殊关键字（扭扭车） 
	 * 对于个别关键字的商品价格做个调整
	 * @param key
	 * @return
	 */
	public static boolean key(String key){
		if(key!=null&&Pattern.compile("(electric.*scooter)|(scooter)").matcher(key.toLowerCase()).find()){
			return true;
		}
		return false;
	}
	
	
	
}
