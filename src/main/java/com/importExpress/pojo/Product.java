package com.importExpress.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.bson.Document;

@Data
public class Product {
	/**
	 * 商品id
	 */
	private String id;
	/**
	 * 商品链接
	 */
	private String url;
	/**
	 * 商品名
	 */
	private String name;
	/**
	 * 商品图片
	 */
	private String image;
	/**
	 * 商品最小订单
	 */
	private String minOrder;
	/**
	 * 已经售出数量
	 */
	private String sold;
	/**
	 * 订量单位
	 */
	private String moqUnit;
	/**
	 *价格后面单位
	 */
	private String priceUnit;
	/**
	 * 订量单位2  100 pieces/lot
	 */
	private String goodsUnit;
	/**
	 * 产品重量
	 */
	private String weight;
	/**
	 * 价格
	 */
	private String price;

	/**
	 * 产品类别
	 */
	private String category;

	/**
	 * 多批量批发价格
	 */
	private List<Price> wholesalePrice;

	/**
	 *多批量批发价格-非免邮中间价格
	 */
	private String wholesaleMiddlePrice;

	/**
	 * 货币单位
	 */
	private String currencySymbol = "USD";
	/**
	 * 商品id
	 */
	private String shopId;

	/**
	 * 1-有视频  0-无视频
	 */
	private int isVideo;//是否有视频

	/**
	 * 商品库存标识  0没有库存  1有库存  hot
	 */
	private int isStock;


	/**
	 * B2C标识 8 B2C，其他 B2B
	 */
	private int matchSource;

	public Product(Document document) {
        this.id = String.valueOf(document.get("pid"));
        this.name = String.valueOf(document.get("enname"));
    }

    public Product(String pid, String custom_main_image) {
        this.id = pid;
        this.image =custom_main_image;
    }

    public Product(String pid, String custom_main_image, int matchSource) {
        this.id = pid;
        this.image =custom_main_image;
        this.matchSource =matchSource;
    }

    /*public Product(String pid, String remotpath, String localpath) {
        this.id = pid;
        this.remotpath = remotpath;
        this.localpath = localpath;
    }*/

    public static List<String> getPidList(List<Product> proList){
        List<String> resList = new ArrayList<String>();
        for (Product bean : proList) {
            resList.add(bean.getId());
        }
        return resList;
    }
}
