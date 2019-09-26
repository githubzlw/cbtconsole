package com.importExpress.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.TypeUtils;
import com.importExpress.pojo.CatalogProduct;
import com.importExpress.service.RecommendCatalogService;
import com.importExpress.utli.MongoDBHelp;
import com.importExpress.utli.SwitchDomainUtil;
import com.mongodb.BasicDBObject;
@Service
public class RecommendCatalogServiceImpl implements RecommendCatalogService {
	@Override
	public CatalogProduct product(String pid,int site) {
		BasicDBObject find = new BasicDBObject("pid",Long.parseLong(pid));
		List<String> findAnyFromMongo2 = MongoDBHelp.INSTANCE.findAnyFromMongo2("product", find , null, 0, 0);
		if(findAnyFromMongo2 == null  || findAnyFromMongo2.isEmpty()) {
			return null;
		}
		CatalogProduct product = new CatalogProduct();
		String string = findAnyFromMongo2.get(0);
		JSONObject json = JSONObject.parseObject(string);
		product.setCatid(json.getString("catid1"));
		
		
		String remotPath = json.getString("remotpath");
		String img = json.getString("main_image");
		String path= img.contains("import-express.com") ? img : remotPath + img;
		path = SwitchDomainUtil.checkIsNullAndReplace(path,site);
		
		product.setImg(path);
		product.setName(json.getString("enname"));
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
		product.setUrl(goods_url.replaceAll("\\%", ""));
		String unit = json.getString("sellunit");
		unit = StringUtils.isBlank(unit) ? "piece" : unit;

		String setSellUnits_ = StrUtils.matchStr(unit, "(\\(.*\\))");
		unit = StringUtils.isNotBlank(setSellUnits_) ? unit.replace(setSellUnits_, "").trim() : unit;
		
		product.setUnit(unit);
		product.setSold(Integer.parseInt(json.getString("ali_sold"))+Integer.parseInt(json.getString("sold")));
		product.setPid(pid);
		
		return product;
	}

}
