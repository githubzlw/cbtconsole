package com.cbt.parse.driver;

import com.cbt.parse.bean.*;
import com.cbt.parse.dao.YiWuDao;
import com.cbt.parse.daoimp.IYiWuDao;
import com.cbt.parse.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Pattern;

/**yiwugou商品搜索引擎  yiwu.sql
 * @author abc
 *
 */
public class YiWuDriver {
	private static final Log LOG = LogFactory.getLog(YiWuDriver.class);
	private static IYiWuDao iywd = new YiWuDao();
	private ArrayList<String> price = new ArrayList<String>();
	private static DecimalFormat format = new DecimalFormat("#0.00");
	
	/**检查商品有效性
	 * @param url
	 * @return
	 */
	public  boolean checkDriver(String url){
		if(url!=null&&!url.isEmpty()){
			String jsoup = DownloadMain.getJsoup(url,1,null);
			if(jsoup!=null){
				String title = Jsoup.parse(jsoup).title();
				if(title!=null&&"China Commodity City,The Only Official Website".equals(title)){
					iywd.updateValid(url, 0);
					return true;
				}
			}
			jsoup = null;
		}
		return false;
	}

	
	/**义乌购商品类别校正
	 * @param key
	 * @param cate
	 * @return
	 */
	public ArrayList<SearchGoods> searchDriver(String key,String aid,String pid){
		ArrayList<SearchGoods> goods = null;
//		//通过关键词  类别来匹配义乌购关键词
//		YiWuFilterDao filter = new YiWuFilterDao();
//		ArrayList<String> fkeys = filter.query(key,cate);
//		if(fkeys!=null&&!fkeys.isEmpty()){
//			String fkey = fkeys.get(0);
//			goods = searchFilter(key, fkey,true);
//			System.out.println("-----******************--");
//		}else{
			//若没有匹配到数据  则使用aliexpress与义乌购类别对照来获取数据
			goods = searchAll(key, aid,pid);
			if(goods==null||goods.isEmpty()){
				goods = searchAll(key, null,null);
			}
//		}
		return goods;
	}
	
	/**保存yiwu数据(爬取)
	 * @param urls
	 */
	public void saveDriver(String urls){
		if(urls!=null&&!urls.isEmpty()){
			Element body = DriverEngine.pageBody(urls, 0, 0);
			String cid = DownloadMain.getSpiderContext(urls, "(&s=\\d+)").replaceAll("&s=","").trim();
			System.out.println(cid);
			if(body!=null){
				String val = body.select("input[id=sumpage]").val();
				val = val==null?"0":val.replaceAll("\\D+", "").trim();
				int num = val.isEmpty()?0:Integer.valueOf(val)+1;
				Element tem_body;
				System.out.println("num:"+num);
				for(int i=1;i<num;i++){
					if(i==1){
						tem_body = body;
						body = null;
					}else{
						urls = urls.replaceAll("cpage=\\d+", "cpage="+i);
						System.out.println("page-url:"+urls);
						tem_body = DriverEngine.pageBody(urls, 0, 0);
					}
					//保存搜索数据
					if(tem_body!=null){
						parseSearch(tem_body,cid);
					}
					tem_body = null;
				}
			}
		}
	}
	
	/**单个商品搜索
	 * @param url
	 * @param pid
	 * @return
	 */
	public GoodsBean goodsDriver(String url, String pid){
		GoodsBean goods = new GoodsBean();
		if(url!=null&&!url.isEmpty()){
			url = TypeUtils.modefindUrl(url, 1);
		}
		ArrayList<YiWuBean> querry = iywd.querry(url,pid);
		if(querry!=null&&!querry.isEmpty()){
			if(querry.get(0).getValid()==1){
//				goods.setCategory(querry.get(0).getCategoryName());
//				goods.setCateurl("&cid="+querry.get(0).getCategory_id());
//				goods.setCid(querry.get(0).getCategory_id());
				goods.setCom("sql");
//				goods.setCtime(querry.get(0).getTime());
				goods.setInfo_ori(querry.get(0).getInfo());
				goods.setMinOrder(querry.get(0).getMinorder());
				goods.setpGoodsUnit(querry.get(0).getgUnit());
				goods.setpName(querry.get(0).getProductName());
				goods.setValid(querry.get(0).getValid());
				goods.setsName(querry.get(0).getStoreName());
				goods.setsID(querry.get(0).getStoreNumber());
				goods.setSupplierUrl("&cm=y&sid="+querry.get(0).getStoreNumber());
				goods.setpUrl(querry.get(0).getProductUrl());
				goods.setpPriceUnit(querry.get(0).getpUnit());
				goods.setpID(querry.get(0).getProductId());
				//获取图片集
				String img = querry.get(0).getImgs();
				if(img!=null&&!img.isEmpty()){
					img = img.replace("[", "").replace("]", "").trim();
					String[] imgs = img.split(",\\s+");
					ArrayList<String> imgList = new ArrayList<String>();
					String tem_img = null;
					File file = null;
					for(int i=0;i<imgs.length;i++){
						if(!imgs[i].isEmpty()){
							tem_img = imgs[i];
							if("/".equals(tem_img.substring(0, 1))){
								tem_img = tem_img.substring(1);
							}
							file = new File(TypeUtils.path+"/"+tem_img);
							if(file.exists()){
								imgList.add(TypeUtils.myhome+tem_img);
							}
							tem_img = null;
							file = null;
						}
					}
					imgs = null;
					if(imgList!=null&&!imgList.isEmpty()){
						goods.setpImage(imgList);
					}
					imgList = null;
				}
				img = null;
				//获取批发价格
				String wprice = querry.get(0).getwPrice();
				if(wprice!=null&&!wprice.isEmpty()){
					wprice = wprice.replace("[", "").replace("]", "").trim();
					String[] prices = wprice.split(",\\s+");
					ArrayList<String> wList = new ArrayList<String>();
					for(int i=0;i<prices.length;i++){
						if(!prices[i].isEmpty()){
							wList.add(prices[i]);
						}
					}
					prices = null;
					if(wList!=null&&!wList.isEmpty()){
						goods.setpWprice(wList);
					}
					wList = null;
				}
				wprice  =null;
				//获取尺寸信息
				String types =querry.get(0).getType();
				if(types!=null&&!types.isEmpty()){
					types = types.replace("[", "").replace("]", "").trim();
					ArrayList<TypeBean> typeList = new ArrayList<TypeBean>();
					String[] types_s = types.split(",\\s+");
					String typename;
					String typeid;
					String typevalue;
					String typeimg;
					TypeBean bean = null;
					String[] ids = null;
					String[] names = null;
					String[] values = null;
					String[] imggs = null;
					for(int i=0;i<types_s.length;i++){
						if(!types_s[i].isEmpty()){
							bean = new TypeBean();
							String[] type = types_s[i].split("\\+#\\s+");
							for(int j=0;j<type.length;j++){
								if(Pattern.compile("(id=)").matcher(type[j]).find()){
									ids = type[j].split("id=");
									typeid = ids.length>1?ids[1]:"";
									bean.setId(typeid);
									typeid = null;
								}else if(Pattern.compile("(type=)").matcher(type[j]).find()){
									names = type[j].split("type=");
									typename = names.length>1?names[1]:"";
									bean.setType(typename);
									typename = null;
								}else if(Pattern.compile("(value=)").matcher(type[j]).find()){
									values = type[j].split("value=");
									typevalue = values.length>1?values[1]:"";
									bean.setValue(typevalue);
									typevalue = null;
								}else if(Pattern.compile("(img=)").matcher(type[j]).find()){
									imggs = type[j].split("img=");
									typeimg = imggs.length>1?imggs[1]:"";
									bean.setImg(typeimg);
									typeimg = null;
								}
							}
							typeList.add(bean);
						}
					}
					if(typeList!=null&&!typeList.isEmpty()){
						goods.setType(typeList);
					}
				}
				types  =null;
			}
		}
		return goods;
	}
	
	
	/**从yiwu表中搜索数据
	 * @param key
	 */
	private  ArrayList<SearchGoods> searchAll(String key,String aid,String pid){
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg = null;
		ParamBean   param = new ParamBean();
		param.setKeyword(key);
		ArrayList<SqlBean> sqls = SearchEngine.pate(param, null);
		if(sqls!=null&&!sqls.isEmpty()){
			ArrayList<YiWuBean> beans = iywd.querryAll(sqls,aid,pid);
			int beans_num = beans==null?0:beans.size();
//			beans_num = beans_num>100?100:beans_num;
			String minprice = null;
			String maxprice = null;
			String morder = null;
			String img = null;
			String name = null;
			StringBuilder sb = new StringBuilder();
			File file = null;
			int index = 0;
			for(int i=0;i<beans_num;i++){
				if(beans.get(i).getValid()==1){
					img = beans.get(i).getImgs();
					if(img!=null&&!img.isEmpty()){
						img = img.split(",")[0];
						file = new File(TypeUtils.path+img);
//						if(file.exists()){
							//0.1613;
							minprice = beans.get(i).getProductMinPrice();
							Double dminprice = minprice!=null?Double.valueOf(minprice)*0.1613:0;
							if(dminprice>0.1){
								minprice = String.valueOf(format.format(dminprice));
								maxprice = beans.get(i).getProductMaxPrice();
								if(!"0".equals(minprice)&&!"0.0".equals(minprice)&&!"0.00".equals(minprice)){
									if(!"0".equals(maxprice)&&!"0.0".equals(maxprice)&&!"0.00".equals(maxprice)){
										sb.delete(0, sb.length());
										minprice = sb.append(minprice).append(" - ").append(format.format(Double.valueOf(maxprice)*0.1613)).toString();
									}
								}
								morder = beans.get(i).getMinorder();
								if(minprice!=null&&!minprice.isEmpty()&&!"0".equals(minprice)&&!"0.0".equals(minprice)&&!"0.00".equals(minprice)){
									if(morder!=null&&!"1".equals(morder)){
										sg = new SearchGoods();
										sg.setGoods_price(minprice);
										sg.setKey_type("goods");
										if("/".equals(img.substring(0, 1))){
											img = img.substring(1);
										}
										sg.setGoods_image(TypeUtils.myhome+img);
										name = beans.get(i).getProductName();
										name = name!=null?name.replaceAll("【*.*】", ""):"";
										sg.setGoods_name(SearchUtils.nameVert(name, 28));
										sg.setGoods_minOrder(morder);
										sg.setGoods_url(TypeUtils.encodeGoods(beans.get(i).getProductUrl()));
										list.add(sg);
										sg = null;
										
										index ++;
										if(index>19){
											break;
										}
									}
								}
							}
						}
						img = null;
						file = null;
					}
//				}
			}
		}
		if(list!=null&&!list.isEmpty()){
			Collections.sort(list, new SortByPriceMin());
		}
		return list;
	}
	
	/**从yiwu表中搜索数据（去除反关键词组的数据）
	 * @param key
	 */
	public  ArrayList<SearchGoods> searchFilter(String key,String fkey,Boolean flag){
		long st = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg = null;
		YiWuDao iyd = new YiWuDao();
		ParamBean   param = new ParamBean();
		param.setKeyword(key);
		ArrayList<SqlBean> sqls = SearchEngine.pate(param, null);
		if(fkey!=null&&!fkey.isEmpty()){
			String[] keys = fkey.split(";");
			int key_num = keys.length;
			SqlBean s = null;
			String fke = null;
			for(int i=0;i<key_num;i++){
				fke = keys[i];
				if(fke!=null&&!fke.isEmpty()){
					s = new SqlBean();
					s.setPara("fkey");
					s.setValue(fke);
					sqls.add(s);
					s = null;
				}
				fke = null;
			}
		}
		
		if(sqls!=null&&!sqls.isEmpty()){
			ArrayList<YiWuBean> beans = iyd.querryFilter(sqls);
			int beans_num = beans==null?0:beans.size();
//			beans_num = beans_num>100?100:beans_num;
			String minprice = null;
			String maxprice = null;
			String morder = null;
			String img = null;
			String utime = null;
			String name = null;
			StringBuilder sb = new StringBuilder();
			File file = null;
			int index = 0;
			for(int i=0;i<beans_num;i++){
				utime = beans.get(i).getUpdateTime();
				if(TypeUtils.isNew(utime, 60)&&beans.get(i).getValid()==1){
					img = beans.get(i).getImgs();
					if(img!=null&&!img.isEmpty()){
						img = img.split(",")[0];
						file = new File(TypeUtils.path+img);
//						if(file.exists()){
						//0.1613;
							minprice = beans.get(i).getProductMinPrice();
							minprice = minprice!=null?String.valueOf(format.format(Double.valueOf(minprice)*0.1613)):"0";
							maxprice = beans.get(i).getProductMaxPrice();
							if(!"0".equals(minprice)&&!"0.0".equals(minprice)&&!"0.00".equals(minprice)){
								if(!"0".equals(maxprice)&&!"0.0".equals(maxprice)&&!"0.00".equals(maxprice)){
									sb.delete(0, sb.length());
									minprice = sb.append(minprice).append(" - ").append(format.format(Double.valueOf(maxprice)*0.1613)).toString();
								}
							}
							morder = beans.get(i).getMinorder();
							if(minprice!=null&&!minprice.isEmpty()&&!"0".equals(minprice)&&!"0.0".equals(minprice)&&!"0.00".equals(minprice)){
								if(morder!=null&&!"1".equals(morder)){
									sg = new SearchGoods();
									sg.setGoods_price(minprice);
									sg.setKey_type("goods");
									if("/".equals(img.substring(0, 1))){
										img = img.substring(1);
									}
									sg.setGoods_image(TypeUtils.myhome+img);
									name = beans.get(i).getProductName();
									name = name!=null?name.replaceAll("【*.*】", ""):"";
									sg.setGoods_name(name);
									sg.setGoods_minOrder(morder);
									sg.setGoods_url(TypeUtils.encodeGoods(beans.get(i).getProductUrl()));
									list.add(sg);
									sg = null;
									index ++;
									if(flag){
										if(index>19){
											break;
										}
									}else{
										if(index>200){
											break;
										}
									}
								}
							}
//						}
						img = null;
						file = null;
					}
				}
				utime = null;
			}
		}
		if(flag&&list!=null&&!list.isEmpty()){
			Collections.sort(list, new SortByPriceMin());
		}
		LOG.warn("YiWuGou search all:"+(new Date().getTime()-st));
		return list;
	}
	
	/**搜索同一商店的商品
	 * @param sid  商店id
	 * @return
	 */
	public ArrayList<SearchGoods> storeDriver(String sid){
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		ArrayList<YiWuBean> searchs = iywd.querryStore(sid);
		int searchs_num = searchs!=null?searchs.size():0;
		SearchGoods searchgoods =null;
		String price1 = null;
		String price2 = null;
		String minorder = null;
		String img = null;
		File file = null;
		StringBuilder price = new StringBuilder();
		for(int i=0;i<searchs_num;i++){
			if(searchs.get(i).getValid()==1){
				img = searchs.get(i).getImgs();
				if(img!=null){
					if(img.indexOf(",")>0){
						img = img.split(",")[0];
					}
					if(img!=null&&!"/".equals(img.substring(0, 1))){
						img = "/"+img;
					}
					file = new File(TypeUtils.path+img);
					if(file.exists()){
						searchgoods = new SearchGoods();
						searchgoods.setGoods_image(TypeUtils.myhome+img);
						minorder = searchs.get(i).getMinorder();
						if(minorder!=null&&!minorder.isEmpty()){
							searchgoods.setGoods_minOrder(minorder+" /"+searchs.get(i).getgUnit());
						}
						price1 = searchs.get(i).getProductMinPrice();
						price2 = searchs.get(i).getProductMaxPrice();
						if(price1!=null&&!price1.isEmpty()&&!"0".equals(price1)&&!"0.0".equals(price1)&&!"0.00".equals(price1)){
							price1 = format.format(Double.valueOf(price1)*0.1613);
							price.append(price1);
						}
						if(price2!=null&&!price2.isEmpty()&&!"0".equals(price2)&&!"0.0".equals(price2)&&!"0.00".equals(price2)){
							price2 = format.format(Double.valueOf(price2)*0.1613);
							price.append("-").append(price2);
						}
						searchgoods.setGoods_price(price.toString());
						price.delete(0, price.length());
						searchgoods.setKey_type("goods");
						searchgoods.setGoods_similar("1");
						searchgoods.setGoods_name(SearchUtils.nameVert(searchs.get(i).getProductName(), 26));
						searchgoods.setGoods_url(TypeUtils.encodeGoods(searchs.get(i).getProductUrl()));
						list.add(searchgoods);
						searchgoods = null;
					}
				}
				img = null;
				file = null;
			}
		}
		return list;
	}
	
	
	
	/**数据爬取
	 * @param body_s
	 * @param cid
	 */
	private  void parseSearch(Element body_s,String cid){
		if(body_s!=null){
			String image_url = null;
			String name_url = null;
			String goods_price = null;
			//获取element
			Elements Goods = body_s.select("ul[class=product_inf]");
			if(Goods==null||Goods.isEmpty()){
				Goods = body_s.select("ul[class=product_class]");
//				if(Goods==null||Goods.isEmpty()){
//					Goods = body_s.select("div[class=product_inf]");
//				}
//				if(Goods==null||Goods.isEmpty()){
//					Goods = body_s.select("div[class=product_class]");
//				}
			}
			if(Goods!=null&&!Goods.isEmpty()){
				System.out.println("**********************************");
				for(Element goods:Goods){
					goods_price = goods.select("font[class=fontfc7215]")
							.text().replace("RMB", "")
							.replace("¥", "").trim();
					if(!"Price Negotiable".equals(goods_price)){
						//获取商品图片附带的商品链接
						image_url = DownloadMain.getSpiderContext(goods.select("p[class=imgsize]")
								.select("a")
								.toString(), "(?:href=\")(.*?)(\")");
						//获取商品名称附带的链接
						name_url = DownloadMain.getSpiderContext(goods.select("ul[class=pro]").select("li[class=fontbold font14px fontbluelink]")
								.select("a")
								.toString(), "(?:href=\")(.*?)(\")");
						System.out.println("url:"+image_url);
						if(image_url.isEmpty()){
							image_url = name_url;
						}
						if(!Pattern.compile("(http://en.yiwugou.com/)").matcher(image_url).find()){
							image_url = "http://en.yiwugou.com"+image_url;
						}
						System.out.println("url:"+image_url);
						if(Pattern.compile("(/\\d+\\.htm)").matcher(image_url).find()){
							String id = DownloadMain.getSpiderContext(image_url, "(/\\d+\\.htm)")
									.replaceAll("/", "").replaceAll("\\.htm", "").trim();
							id = id.isEmpty()?"y110110":id;
							save(image_url, id,cid);
						}
					}
				}
			}
		}
		body_s = null;
	}
	
	/**解析义乌购的商品数据存入yiwu表中
	 * @param url
	 */
	private  void save(String url,String id,String cid){
		if(url!=null&&!url.isEmpty()){
			String urls = TypeUtils.modefindUrl(url, 1);
			ArrayList<YiWuBean> querry = iywd.querry(urls,null);
			if(querry==null||querry.isEmpty()){
				Element body = DriverEngine.pageBody(urls, 0,1);
				if(body!=null){
					YiWuBean bean = parseDate(body,id);
					String minprice= bean.getProductMinPrice();
					if(minprice!=null&&!minprice.isEmpty()){
						if(bean!=null&&!bean.isEmpty()){
							bean.setProductUrl(urls);
							bean.setCategory_id(cid);
							
							ArrayList<TypeBean> filterYiWu = ParseGoodsUrl.filterYiWu(urls);
							if(filterYiWu!=null&&!filterYiWu.isEmpty()){
								bean.setType(filterYiWu.toString());
							}
								System.out.println("------------add----------------");
								iywd.addDate(bean);
	//						}else{
	//							System.out.println("------------update----------------");
	//							iywd.updateDate(bean);
							}
						}
					}
					body = null;
			}
		}
	}
	
	/**
	 * 义乌购搜索商品数据
	 */
	private YiWuBean parseDate(Element body, String id){
		YiWuBean bean = new YiWuBean();
		if(id==null||id.isEmpty()){
			id = "00000";
		}
		bean.setProductId(id);
		//1.商品名称
		Elements pname = body.select("div[class=pro-view-nav]").select("span");
		if(pname!=null&&pname.size()>0){
			String name = pname.get(0).text();
			name = name.length()>500?name.substring(0,500):name;
			name = ParseGoodsUrl.filterName(name);
			bean.setProductName(name);
		}
		pname = null;
						
		//3.商品类别
		Elements category = body.select("div[class=en_nav fontbluelink]").select("a");
		if(category!=null&&category.size()>4){
			String cate = category.get(4).text();
			cate = cate.length()>50?cate.substring(0, 50):cate;
			bean.setCategoryName(cate);
		}
		category = null;
						
		//4.商品图片
		Elements image = body.select("div[class=pro_view_left_img]").select("a"); 
		String img_url = null;
		String imgs = "";
		StringBuilder sb = new StringBuilder();
		StringBuilder sbb = new StringBuilder();
		if(image!=null&&image.size()>0){
			int image_num =image.size();
			String imgName = null;
			String fileName = null;
			Boolean execute  = false;
			for(int i=0;i<image_num;i++){
				img_url = DownloadMain.getSpiderContext(image.get(i).toString(),"(?:src=\")(.*?)(\")");
				imgName = img_url.substring(img_url.lastIndexOf("/"));
				fileName = sbb.append(TypeUtils.path).append("/Cbt/yiwu/").append(id).append(imgName).toString();
				execute = ImgDownload.execute(img_url,fileName);
				if(execute){
					sb.append("/Cbt/yiwu/").append(id).append(imgName);
					if(i!=image_num-1){
						sb.append(",");
					}
				}
				imgName = null;
				fileName = null;
				sbb.delete(0, sbb.length());
			}
			imgs = sb.toString();
			bean.setImgs(imgs);
			sb.delete(0, sb.length());
		}
		
		//7.商品价格
		Elements ele = body.select("table[class=mt15px]").select("tr[height=27]");
		String minprice;
		String maxprice;
		if(ele!=null&&ele.size()>0){
			minprice = ele.get(0).select("font[class=fontbold c-yellow font14px]").text();
			minprice = DownloadMain.getSpiderContext(minprice, "(\\d+\\.*\\d*)");
			minprice = minprice.isEmpty()?"0":minprice;
			bean.setProductMinPrice(minprice);
			if(ele.size()>1){
				maxprice = ele.get(ele.size()-1).select("font[class=fontbold c-yellow font14px]").text();
				maxprice = DownloadMain.getSpiderContext(maxprice, "(\\d+\\.*\\d*)");
				maxprice = maxprice.isEmpty()?"0":maxprice;
				bean.setProductMaxPrice(maxprice);
			}
		}
        minprice = null;
        maxprice = null;
        
		if(price!=null){
			price.clear();
		}else{
			price = new ArrayList<String>();
		}
		int ele_size = ele.size()-1;
		Element e;
		String amount;
		String pricex;
		for (int i = ele_size; i > -1; i--) {
			   e = ele.get(i);
			   if(e.select("td").size() > 2){
			    amount = e.select("td").get(0).text();
			    if(Pattern.compile("(pcs)").matcher(amount).find()){
			     amount = amount.replace("pcs", "")
			           .replace("Above", "≥")
			           .replace("~", "-");
			    }
			    
			    pricex = e.select("td").get(2).text();
			    if(Pattern.compile("yuan/pc").matcher(pricex).find()){
			     pricex = pricex.replace("yuan/pc", "");
			    }
			    price.add((amount+" RMB "+pricex).trim());
			   }
			   e = null;
		}
		if(price!=null&&!price.isEmpty()){
			bean.setwPrice(price.toString());
		}
        bean.setpUnit("RMB");
        
        //商品最小订单数量
        Elements quantity = body.select("td[height=25]").select("input");
        String minOrder = "1";
		if(quantity.size() != 0){
        	minOrder = DownloadMain.getSpiderContext(quantity.toString(), "(?:value=\")(.*?)(?:\" style=\")");
        	minOrder = DownloadMain.getSpiderContext(minOrder, "(\\d+)");
        	minOrder = minOrder.isEmpty()?"1":minOrder;
		}
        bean.setMinorder(minOrder);
        quantity = null;
        minOrder = null;
        
        Elements element = body.select("table[class=mt15px]")
				   .select("tr");
		String qunit = null;
		String tem;
		for(Element el:element){
			if(Pattern.compile("Supply Quantity:").matcher(el.text()).find()){
				tem = el.select("td").get(1).text();
				qunit = DownloadMain.getSpiderContext(tem, "(\\D+)");
			}
			tem = null;
		}
		bean.setgUnit(qunit);
        
      //卖家名称(可能为销售公司名称)
        Elements sNam = body.select("ul[class=pro_view_isright_com]").select("li"); 
	    String sName =sNam.size()>0?sNam.get(0).text():"yiwubuy"; 
	    sName = sName.length()>100?sName.substring(0,100):sName;
	    bean.setStoreName(sName);
	    sNam = null;
	    sName = null;
	    
	    Elements select = body.select("ul[class=pro_view_isright_com]").select("li").select("a");
	   for(int i=0;i<select.size();i++){
		   String sid = DownloadMain.getSpiderContext(select.get(i).toString(), "(?:href=\")(.*?)(?:\")");
		   
		   if(sid!=null&&!sid.isEmpty()&&Pattern.compile("(shopdetail)").matcher(sid).find()){
			   sid = DownloadMain.getSpiderContext(sid, "(/\\d+_)").replaceAll("(/)|(_)", "").trim();
			   if(!sid.isEmpty()){
				   bean.setStoreNumber(sid);
				   break;
			   }
		   }
	   }
	    //更新时间   简介
	    Elements time = body.select("div[class=pro_view_isleft]").select("ul"); 
	   
	    if(time!=null&&time.size()>0){
	    	String update = time.get(0).select("li[class=class-time]").text();
	    	update = update.replaceAll("[uU]pdated\\:", "").trim();
	    	update = update.replaceAll("\\s+", "-").replaceAll("\\:", "-").trim()+"-0";
	    	bean.setUpdateTime(update);
	    	String product_desc = time.get(0).select("li[class=font666 mt10px]").text();
	    	product_desc = product_desc.length()>2000?product_desc.substring(0, 2000):product_desc;
	    	bean.setProductDesc(product_desc);
	    }
	    
	  //详情源码
	    String page = body.select("div[class=pro_view_info]").toString(); 
	    if(page==null||page.isEmpty()){
	    	page = body.select("div[class=pro_view_bold]").toString(); 
	    }
	    String replas = body.select("img[title=Click here to message me]").toString();
	    if(page!=null&&!page.isEmpty()&&!replas.isEmpty()){
	    	page = page.replace(replas, "");
	    }
	    if(page!=null){
	    	if(page.length()<30000){
	    		bean.setInfo(page);
	    	}
	    }
	    page = null;
	    
	    return bean;
	}
	
	class SortByPriceMin implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			SearchGoods s1 = (SearchGoods) o1;
			SearchGoods s2 = (SearchGoods) o2;
			return s2.compareTo(s1);
		}
	}
	
	/**
	 * 中文标题  店铺名称
	 */
	public static void update(){
		YiWuDao y = new YiWuDao();
		ArrayList<YiWuBean> list = y.querry();
		if(list!=null&&!list.isEmpty()){
			int t = list.size();
			String url = null;
			String name = null;
			String store = null;
			Element title = null;
			for(int i=0;i<t;i++){
				url = list.get(i).getProductUrl().replaceAll("(&amp;)+", "&");
				title = title(url,0);
				if(title!=null){
					name = title.select("li[class=font16px fontbold tit]").text();
					store = title.select("li[class=f12 fontblue fontbold]").text();
				   y.update(url, name,store);
				}
			}
		}
	}
	
	/**获取中文 网页
	 * @param url
	 * @param tem
	 * @return
	 */
	private static Element title(String url,int tem){
		String page = null;
		url = url.replace("http://en.yiwugou.com/", "http://www.yiwugou.com/");
		if(url!=null&&!url.isEmpty()&&Pattern.compile("(http://.+)").matcher(url).matches()){
			page = DownloadMain.getJsoup(url, 1,null);
			while(page==null||page.isEmpty()){
				if(tem<3){
					tem++;
					page = DownloadMain.getJsoup(url,1,null);
				}else{
					break;
				}
			}
		}
		if(page!=null&&!page.isEmpty()&&!"httperror".equals(page)){
			//JSoup解析HTML生成document
			Document doc = Jsoup.parse(page);
			page = null;
			//获取element
			return doc.body();
		}
		  return null;
	}
	
	/**重新自定义商品类别
	 * @param key
	 * @param fkey
	 * @param cnl
	 * @param cnm
	 * @param cns
	 */
	public static void updateCategroy(String key,String fkey,String cnl,String cnm,String cns){
		ParamBean   param = new ParamBean();
		param.setKeyword(key);
		ArrayList<SqlBean> sqls = SearchEngine.pate(param,null);
		if(fkey!=null&&!fkey.isEmpty()){
			String[] keys = fkey.split(";");
			int key_num = keys.length;
			SqlBean s = null;
			for(int i=0;i<key_num;i++){
				if(keys[i]!=null&&!keys[i].isEmpty()){
					s = new SqlBean();
					s.setPara("fkey");
					s.setValue(keys[i]);
					sqls.add(s);
					s = null;
				}
			}
		}
		
		YiWuDao y = new YiWuDao();
		int updateC = y.updateC(cnl, cnm, cns, sqls);
		System.out.println("updateC:"+updateC);
		
		
	}
	
}
