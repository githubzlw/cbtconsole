package com.importExpress.pojo;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class CategoryWrap {
	/**
     * id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父类
     */
    private String parentCategory;
    /**
     * 子类
     */
    private List<CategoryWrap> childen = Lists.newArrayList();
    /**
     * 级
     */
    private int level;

    /**
     * 链接
     */
    private String url;

    /**
     * 选中
     */
    private int selected;

    /**
     * 数量
     */
    private long count;
}
