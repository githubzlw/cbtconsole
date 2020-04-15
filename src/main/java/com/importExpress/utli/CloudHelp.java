package com.importExpress.utli;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.importExpress.pojo.CommonResult;
import com.importExpress.pojo.PageWrap;
import com.importExpress.pojo.SearchParam;
import com.importExpress.pojo.SearchResultWrap;

import lombok.extern.slf4j.Slf4j;

/**
 *搜索微服务调用
 *@author abc
 *@date 2017年2月8日
 *
 */
@Component
@Slf4j
public class CloudHelp {

	private UrlUtil instance = UrlUtil.getInstance();
	private  final String SEARCH_URL = UrlUtil.ZUUL_SEARCH + "search/products";


	private Gson gson = new Gson();
	private SearchResultWrap initResultWrap(){
		SearchResultWrap wap = new SearchResultWrap();
		PageWrap page = new PageWrap();
		page.setAmount(0);
		page.setRecordCount(0L);
		wap.setPage(page);
		return wap;
	}

	/**
	 * 搜索商品数据集合
	 * @date 2017年2月8日
	 * @author abc
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public SearchResultWrap searchProducts(SearchParam param){
		SearchResultWrap wrap = initResultWrap();
		wrap.setParam(param);
		if(param.getUserType() == 0){
			return wrap;
		}
		CommonResult commonResult = cloudCallback(param,SEARCH_URL);
		if(commonResult.getCode() == 200){
			wrap = gson.fromJson(commonResult.getData().toString(),SearchResultWrap.class);
		}
		return wrap;
	}

	/**请求微服务
	 * @param param
	 * @param url
	 * @return
	 */
	private CommonResult cloudCallback(SearchParam param,String url){
		try {
			JSONObject jsonObject = instance.callUrlByPost(url, param);
			CommonResult commonResult = JSON.toJavaObject(jsonObject,CommonResult.class);
			return commonResult;
		}catch (Exception e){
			log.error("Call Cloud ERROR HAPPEND:"+url,e);
		}
		return CommonResult.failed("CALL SEARCH SERVICE ERROR");
	}

	
}
