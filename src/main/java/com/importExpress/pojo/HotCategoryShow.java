package com.importExpress.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 热卖商品类别
 *
 * @author jxw
 */
public class HotCategoryShow implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -853479256L;

    private int id;
    private String showName;// 显示名称
    private String showImg;// 显示图片
    private int sorting;// 排序号
    private int hotType;// 热卖类别，1热卖区、2今日折扣、3新品

    private List<HotSellGoodsShow> goodsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public int getHotType() {
        return hotType;
    }

    public void setHotType(int hotType) {
        this.hotType = hotType;
    }

    public List<HotSellGoodsShow> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<HotSellGoodsShow> goodsList) {
        this.goodsList = goodsList;
    }
}
