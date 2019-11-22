package com.importExpress.service.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.service.CustomGoodsService;
import com.google.common.collect.Sets;
import com.importExpress.mapper.GoodsSkuAttrMapper;
import com.importExpress.pojo.GoodsSkuAttr;
import com.importExpress.service.GoodsSkuAttrService;
@Service
public class GoodsSkuAttrServiceImpl implements GoodsSkuAttrService {
	@Autowired
	private GoodsSkuAttrMapper goodsSkuAttrMapper;
	@Autowired
	private CustomGoodsService customGoodsService;
	@Override
	public GoodsSkuAttr insertGoodsSkuAttr(GoodsSkuAttr skuAttr) {
		GoodsSkuAttr id = goodsSkuAttrMapper.countGoodsSku(skuAttr.getPid(),skuAttr.getSkuattr());
		if(id == null) {
			goodsSkuAttrMapper.insertGoodsSku(skuAttr);
			return skuAttr;
		}
		return id;
	}


	@Override
	public GoodsSkuAttr countGoodsSku(String pid, String skuattr	) {
		return  goodsSkuAttrMapper.countGoodsSku(pid,skuattr);
	}

	@Override
	public GoodsSkuAttr parseGoodsSku(String code) {
		String[] codeArray = code.split("-");
		String pid = codeArray[0];
		String skuattr = codeArray.length > 1 ? codeArray[1] : "";
		skuattr = codeArray.length > 2 ? skuattr + "," + codeArray[2] : skuattr;
		skuattr = codeArray.length > 3 ? skuattr + "," + codeArray[3] : skuattr;
		GoodsSkuAttr matchSkuId = matchSkuId(pid, skuattr);
		if(matchSkuId.getErrorCode() != 200) {
			goodsSkuAttrMapper.addGoodsSkuErrorlog(matchSkuId);
		}
		return matchSkuId;
	}
	
	
	private GoodsSkuAttr matchSkuId(String pid, String skuattr) {
    	GoodsSkuAttr matchSkuId = countGoodsSku(pid, skuattr);
		if(matchSkuId != null) {
			matchSkuId.setErrorCode(200);
			return matchSkuId;
		}
		matchSkuId = new GoodsSkuAttr();
		matchSkuId.setPid(pid);
		Map<String, String> goodsByPid = customGoodsService.getGoodsByPid(pid);
		if(goodsByPid == null || goodsByPid.size() == 0) {
			matchSkuId.setErrorCode(101);
			return matchSkuId;
		}
		
		String sku = goodsByPid.get("sku");
		if(StringUtils.isBlank(sku)) {
			matchSkuId.setErrorCode(103);
			matchSkuId.setSkuid(pid);
			matchSkuId.setSpecid(pid);
			return matchSkuId;
		}
		JSONArray parseArray = null ;
		try {
			parseArray = JSONObject.parseArray(sku);
		} catch (Exception e) {
			parseArray = null ;
//			System.err.println("产品ID:"+pid);
		}
		if(parseArray == null ) {
			matchSkuId.setErrorCode(100);
			return matchSkuId;
		}
		matchSkuId.setErrorCode(102);
		matchSkuId = loopParseGoodsAttr(parseArray, matchSkuId, pid, skuattr);
		return matchSkuId;
	}
	
	/**遍历sku
	 * @param parseArray
	 * @param matchSkuId
	 * @param pid
	 * @param skuattr
	 * @return
	 */
	private GoodsSkuAttr loopParseGoodsAttr(JSONArray parseArray,GoodsSkuAttr matchSkuId,String pid,String skuattr) {
		GoodsSkuAttr skuAttr;
		boolean isMatch = false;
		for(int i=0,size=parseArray.size();i<size;i++) {
			JSONObject skubject = JSONObject.parseObject(String.valueOf(parseArray.get(i)));
			String skuPropIds = skubject.getString("skuPropIds");
			String specId = skubject.getString("specId");
			String skuId = skubject.getString("skuId");
			skuAttr = new GoodsSkuAttr();
			skuAttr.setPid(pid);
			skuAttr.setSkuattr(skuPropIds);
			skuAttr.setSkuid(skuId);
			skuAttr.setSpecid(specId);
			GoodsSkuAttr insertGoodsSkuAttr = insertGoodsSkuAttr(skuAttr);
			insertGoodsSkuAttr.setErrorCode(200);
			if(!isMatch && isMatch(skuattr, skuPropIds)) {
				matchSkuId = insertGoodsSkuAttr;
				isMatch = true;
			}
			
		}
		return matchSkuId;
	}
	
	
	
	/**匹配规格组合
	 * @param skuattr
	 * @param skuPropIds
	 * @return
	 */
	private boolean isMatch(String skuattr,String skuPropIds) {
		Set<String> skuattrList = Sets.newHashSet(Arrays.asList(skuattr.split(",")));
		Set<String> skuPropIdsList = Sets.newHashSet(Arrays.asList(skuPropIds.split(",")));
		Set<String> resultSet  = Sets.newHashSet();
		resultSet.addAll(skuattrList);
		resultSet.addAll(skuPropIdsList);
		return resultSet.size() == skuattrList.size() || resultSet.size() < skuattrList.size();
	}
	
}
