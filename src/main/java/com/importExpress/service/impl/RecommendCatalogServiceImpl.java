package com.importExpress.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.TypeUtils;
import com.google.common.collect.Lists;
import com.importExpress.mapper.RecommendCatalogMapper;
import com.importExpress.pojo.CatalogProduct;
import com.importExpress.pojo.PriceBean;
import com.importExpress.pojo.RecommendCatalog;
import com.importExpress.service.RecommendCatalogService;
import com.importExpress.utli.MongoDBHelp;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.importExpress.utli.SwitchDomainUtil;
import com.mongodb.BasicDBObject;
@Service
public class RecommendCatalogServiceImpl implements RecommendCatalogService {
	@Autowired
	private RecommendCatalogMapper recommendCatalogMapper;
	@Override
	public CatalogProduct product(String pid,int site) {
		
		BasicDBObject find = new BasicDBObject("pid",Long.parseLong(pid));
		List<String> findAnyFromMongo2 = MongoDBHelp.INSTANCE.findAnyFromMongo2("product", find , null, 0, 0);
		
		if(findAnyFromMongo2 == null  || findAnyFromMongo2.isEmpty()) {
			return null;
		}
		CatalogProduct product = productFromMongo(findAnyFromMongo2.get(0), site,pid);
		return product;
	}

	@Override
	public int addCatelog(RecommendCatalog catalog) {
		String sql  ="insert into recommend_catalog (catalog_name,template,create_admin,"
				+ "create_time,product_count,product_list,status) values(?,?,?,now(),?,?,?)";
		List<String> lstValues = Lists.newArrayList();
		lstValues.add(catalog.getCatalogName().replace("'", "\\'"));
		lstValues.add(String.valueOf(catalog.getTemplate()));
		lstValues.add(catalog.getCreateAdmin());
		lstValues.add(String.valueOf(catalog.getProductCount()));
		lstValues.add(catalog.getProductList().replace("'", "\\'"));
		lstValues.add("1");
		
		String runSql = DBHelper.covertToSQL(sql, lstValues );
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		return StrUtils.isNum(sendMsgByRPC) ? Integer.parseInt(sendMsgByRPC) : 0;
//		return recommendCatalogMapper.insertCatalog(catalog);
	}
	
	/**解析mongo数据到产品数据bean
	 * @param productStr
	 * @param site
	 * @return
	 */
	private CatalogProduct productFromMongo(String productStr,int site,String pid) {
		CatalogProduct product = new CatalogProduct();
		product.setPid(pid);
		
		JSONObject json = JSONObject.parseObject(productStr);
		product.setCatid(json.getString("catid1"));
		
		String path= productImg(json, site);
		product.setImg(path);
		
		product.setName(json.getString("enname"));
		
		String url = prouctUrl(json,pid);
		product.setUrl(url);
		
		String unit = productUnit(json);
		product.setUnit(unit);
		
		int sold = productSold(json);
		product.setSold(sold);
		
		String price = productPrice(json);
		product.setPrice(price);
		return product;
	}
	/**产品销量
	 * @param json
	 * @return
	 */
	private int productSold(JSONObject json) {
		int aliSold = Integer.parseInt(json.getString("ali_sold"));
		int sold = Integer.parseInt(json.getString("sold"));
		sold += aliSold;
		return sold;
	}
	/**产品图片
	 * @param json
	 * @param site
	 * @return
	 */
	private String productImg(JSONObject json,int site){
		String remotPath = json.getString("remotpath");
		String img = json.getString("custom_main_image");
		String path= img.contains("import-express.com") ? img : remotPath + img;
		path = SwitchDomainUtil.checkIsNullAndReplace(path,site);
		return path;
	}

	/**产品链接
	 * @param json
	 * @param pid
	 * @return
	 */
	private String prouctUrl(JSONObject json,String pid) {
		String itemIDToUUID = TypeUtils.itemIDToUUID(pid,"D");
		String catid = json.getString("path_catid");
		String catid1="0";
		String catid2="0";
		if(StringUtils.isNotBlank(catid) && catid.indexOf(" ")>-1){
			String [] catids=catid.split(" ");
			catid1=catids[0];
			catid2=catids[1];
		}
		String goods_url = TypeUtils.uUidToStaticUrl_cart(itemIDToUUID, pid, json.getString("enname"),"",catid1,catid2);
		return goods_url.replaceAll("\\%", "");
		
	}
	/**产品单位
	 * @param json
	 * @return
	 */
	private String productUnit(JSONObject json) {
		String unit = json.getString("sellunit");
		unit = StringUtils.isBlank(unit) ? "piece" : unit;

		String setSellUnits_ = StrUtils.matchStr(unit, "(\\(.*\\))");
		unit = StringUtils.isNotBlank(setSellUnits_) ? unit.replace(setSellUnits_, "").trim() : unit;
		return unit;
	}
	
	/**产品价格
	 * @param json
	 * @return
	 */
	private String productPrice(JSONObject json) {
		 String price = json.getString("price");
		 String rangePrice = json.getString("range_price");
		 if(StringUtils.isBlank(rangePrice) || rangePrice.indexOf("-") == -1){
			return priceFromWholesalePrice(json,price);
		 }
		 return rangePrice;
	}
	
	/**从wholesale price获取价格
	 * @param json
	 * @param price
	 * @return
	 */
	private String priceFromWholesalePrice(JSONObject json,String price) {
		 String custom_is_sold_flag = json.getString("is_sold_flag");
		 String feeprice = json.getString("feeprice");
		 String wprice = json.getString("wprice");
		//批量价格显示
		if(!"0".equals(custom_is_sold_flag) && StringUtils.isNotBlank(feeprice)){
			wprice=feeprice;
		}
		price = modefideWholesalePrice(wprice,price);
		return price;
		
	}
	
	 /**
	  * 将多区间价格字符串转换多区间价格--2017-09-14
	 * @param data
	 * @param wprice 多区间价格字符串
	 * @return
	 */
	
	private String modefideWholesalePrice(String wprice,String price) {
		if(wprice == null){
			return price;
		}
		wprice = wprice.replaceAll("[\\[\\]]", "").trim();
		if(StringUtils.isBlank(wprice)){
			return price;
		}
		
		List<PriceBean> priceList = new ArrayList<PriceBean>();
		String[] prices = wprice.split(",\\s*");
		int wPriceSize = 0;
		String preQuantity = "";
		String prePrice = "";
		String quantity = "";
		for(int i=0;i<prices.length;i++){
			if(StringUtils.isBlank(prices[i])){
				continue;
			}
			String[] wholePrices = prices[i].split("(\\$)");
			if(wholePrices.length < 2){
				continue;
			}
			quantity = wholePrices[0].trim();
			if(quantity.indexOf("-") > -1 && StringUtils.isNotBlank(StrUtils.matchStr(quantity, "(\\d+-\\d+)"))){
				String[] quantitys = quantity.split("-");
				if(quantitys[0].trim().equals(quantitys[1].trim())){
					quantity = quantitys[0];
				}
			}
			if(!StrUtils.isFind(quantity, "(\\d+)") || !StrUtils.isMatch(wholePrices[1].trim(), "(\\d+(\\.\\d+){0,1})")){
				continue;
			}
			/*如果后一个区间定量与前一个区间定量值一样 或者  前一区间的价格与后一个去加的价格一样，说明多区间价格不合理，不使用多区间价格，直接使用单一价格*/
			if(preQuantity.equals(quantity) || prePrice.equals(wholePrices[1].trim())){
				continue;
			}
			prePrice = wholePrices[1].trim();
			preQuantity = quantity;
			priceList.add(new PriceBean(wholePrices[1].trim(), quantity));
			wPriceSize++;
			if(wPriceSize > 2){
				break;
			}
		}
		if(wPriceSize == 0) {
			return price;
		}
		//只有一个区间价
		if(wPriceSize == 1){
			return priceList.get(0).getPrice();
		}
		//2个以上
		if(!userWprice(priceList, wPriceSize)){
			return price;
		}
		price = priceList.get(priceList.size()-1).getPrice();
		price = price +"-"+priceList.get(0).getPrice();
		return price;
	 }
	
	/**计算的whosale是否合理，不合理不使用该价格
	 * @param priceList
	 * @param wPriceSize
	 * @return
	 */
	private boolean userWprice(List<PriceBean> priceList,int wPriceSize) {
		List<String> matchStrList = StrUtils.matchStrList("(\\d+)",priceList.get(0).getQuantity());
		int quantity0_1 = Integer.valueOf(matchStrList.get(matchStrList.size()-1));
		matchStrList = StrUtils.matchStrList("(\\d+)",priceList.get(1).getQuantity());
		int quantity1_0 = Integer.valueOf(StrUtils.matchStr(priceList.get(1).getQuantity(), "(\\d+)"));
		int quantity1_1 = Integer.valueOf(matchStrList.get(matchStrList.size() - 1));
		int quantity2 = wPriceSize > 2 ? Integer.valueOf(StrUtils.matchStr(priceList.get(2).getQuantity(), "(\\d+)")) : 0;
		return quantity1_0 > quantity0_1 && (wPriceSize == 2 || (wPriceSize == 3 && quantity2 > quantity1_1));
	}

	@Override
	public List<RecommendCatalog> catalogList(int page, int template, String catalogName) {
		return recommendCatalogMapper.catalogList(page, template, catalogName);
	}

	@Override
	public int catalogCount(int template, String catalogName) {
		return recommendCatalogMapper.catalogCount(template, catalogName);
	}

	@Override
	public int deleteCatalog(int id) {
		String sql = "update recommend_catalog set status=0 where id="+id;
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(sql));
		return StrUtils.isNum(sendMsgByRPC) ? Integer.parseInt(sendMsgByRPC) : 0;
//		return recommendCatalogMapper.deleteCatalog(id);
	}

	@Override
	public RecommendCatalog catalogById(int id) {
		return recommendCatalogMapper.catalogById(id);
	}

	@Override
	public int updateCatalog(RecommendCatalog cataLog) {
		String sql = "update recommend_catalog set product_count=?,product_list=?,template=?,catalog_name=? where id=?";
		List<String> lstValues = Lists.newArrayList();
		lstValues.add(String.valueOf(cataLog.getProductCount()));
		lstValues.add(cataLog.getProductList().replace("'", "\\'"));
		lstValues.add(String.valueOf(cataLog.getTemplate()));
		lstValues.add(cataLog.getCatalogName().replace("'", "\\'"));
		lstValues.add(String.valueOf(cataLog.getId()));
		
		String runSql = DBHelper.covertToSQL(sql, lstValues );
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		return StrUtils.isNum(sendMsgByRPC) ? Integer.parseInt(sendMsgByRPC) : 0;
//		return recommendCatalogMapper.updateCatalog(cataLog);
	}
}
