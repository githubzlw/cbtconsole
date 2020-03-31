package com.importExpress.pojo;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SearchResultWrap {
	/**
     * 商品
     */
    private List<Product> products;
    /**
     * 类别
     */
    private List<CategoryWrap> categorys;
    /**
     * 页码
     */
    private PageWrap page;

    /**
     * 属性集合
     */
    private List<AttributeWrap> attributes;


    /**
     * 已选择属性集合
     */
    private AttributeWrap selectedAttr;

    /**
     * 是否推荐联想词
     * 0-无 1-有
     */
    private int suggest;

    /**
     * 推荐联想词
     */
    private List<AssociateWrap> associates;

    /**
     *搜索页增加面包屑导航
     */
    private Map<String, String> searchNavigation;

    /**
     * 搜索参数
     */
    private SearchParam param;
    /**
     * See more products in category
     */
    private String productsCate;
}
