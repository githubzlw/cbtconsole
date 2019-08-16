package com.importExpress.utli;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.website.bean.PurchasesBean;
import com.importExpress.pojo.ShopCarMarketing;
import com.importExpress.pojo.ShopRecommendGoods;
import com.importExpress.pojo.ShopRecommendInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 切换域名
 */
public class SwitchDomainNameUtil {

    // https://www.import-express.com
    private static final String IMPORT_DOMAIN_NAME_1 = "import-express";
    private static final String IMPORT_DOMAIN_NAME_2 = "ImportExpress";
    private static final String IMPORT_DOMAIN_NAME_3 = "Import Express";

    // https://www.kidsproductwholesale.com/
    private static final String KIDS_DOMAIN_NAME_1 = "kidsproductwholesale";
    private static final String KIDS_DOMAIN_NAME_2 = "KidsProductWholesale";
    private static final String KIDS_DOMAIN_NAME_3 = "Kids Product Wholesale";

    // https://www.lovelypetsupply.com/
    private static final String PETS_DOMAIN_NAME_1 = "lovelypetsupply";
    private static final String PETS_DOMAIN_NAME_2 = "LovelyPetSupply";
    private static final String PETS_DOMAIN_NAME_3 = "Lovely Pet Supply";

    // www.restaurantkitchenequipments.com
    private static final String RESTAURANT_DOMAIN_NAME_1 = "restaurantkitchenequipments";
    private static final String RESTAURANT_DOMAIN_NAME_2 = "RestaurantKitchenEquipments";
    private static final String RESTAURANT_DOMAIN_NAME_3 = "Restaurant Kitchen Equipments";


    /**
     * 购物车营销List的bean域名切换
     *
     * @param shopCarMarketingList
     */
    public static void changeShopCarMarketingList(List<ShopCarMarketing> shopCarMarketingList, int webSite) {
        if (shopCarMarketingList != null && !shopCarMarketingList.isEmpty()) {
            for (ShopCarMarketing shopCarMarketing : shopCarMarketingList) {
                changeShopCarMarketingSingle(shopCarMarketing, webSite);
            }
        }
    }


    /**
     * 产品单个bean域名切换
     *
     * @param goods
     */
    public static void changeCustomGoodsPublishBean(CustomGoodsPublish goods, int webSite) {
        goods.setRemotpath(checkNullAndReplace(goods.getRemotpath(), webSite));
        goods.setCustomMainImage(checkNullAndReplace(goods.getCustomMainImage(), webSite));
        goods.setShowMainImage(checkNullAndReplace(goods.getShowMainImage(), webSite));
        goods.setImg(checkNullAndReplace(goods.getImg(), webSite));
        goods.setEninfo(checkNullAndReplace(goods.getEninfo(), webSite));
    }

    /**
     * 店铺推荐List域名切换
     *
     * @param infoList
     */
    public static void changeShopRecommendInfoList(List<ShopRecommendInfo> infoList, int webSite) {
        if (infoList != null && !infoList.isEmpty()) {
            for (ShopRecommendInfo info : infoList) {
                changeShopRecommendInfoBean(info, webSite);
            }
        }
    }

    /**
     * 店铺推荐Bean域名切换
     *
     * @param recommendInfo
     */
    public static void changeShopRecommendInfoBean(ShopRecommendInfo recommendInfo, int webSite) {
        recommendInfo.setShopUrl(checkNullAndReplace(recommendInfo.getShopUrl(), webSite));
        recommendInfo.setCoverImg(checkNullAndReplace(recommendInfo.getCoverImg(), webSite));
        changeShopRecommendGoodsList(recommendInfo.getGoodsList(), webSite);
    }

    /**
     * 店铺推荐商品List域名切换
     *
     * @param goodsList
     */
    public static void changeShopRecommendGoodsList(List<ShopRecommendGoods> goodsList, int webSite) {
        if (goodsList != null && !goodsList.isEmpty()) {
            for (ShopRecommendGoods gd : goodsList) {
                changeShopRecommendGoodsBean(gd, webSite);
            }
        }
    }

    /**
     * 店铺推荐商品Bean域名切换
     *
     * @param recommendGoods
     */
    public static void changeShopRecommendGoodsBean(ShopRecommendGoods recommendGoods, int webSite) {
        recommendGoods.setGoodsImg(checkNullAndReplace(recommendGoods.getGoodsImg(), webSite));
        recommendGoods.setMainImg(checkNullAndReplace(recommendGoods.getMainImg(), webSite));
        recommendGoods.setOnlineUrl(checkNullAndReplace(recommendGoods.getOnlineUrl(), webSite));
    }

    /**
     * 购物车营销单个bean域名切换
     *
     * @param shopCarMarketing
     */
    public static void changeShopCarMarketingSingle(ShopCarMarketing shopCarMarketing, int webSite) {
        shopCarMarketing.setGoogsImg(checkNullAndReplace(shopCarMarketing.getGoogsImg(), webSite));
    }

    /**
     * 订单详情List bean域名切换
     *
     * @param orderDetailsBeanList
     */
    public static void changeOrderDetailsList(List<OrderDetailsBean> orderDetailsBeanList, int webSite) {
        for (OrderDetailsBean orderDetails : orderDetailsBeanList) {
            changeOrderDetailsSingle(orderDetails, webSite);
        }
    }


    /**
     * 订单详情单个bean域名切换
     *
     * @param orderDetailsBean
     */
    public static void changeOrderDetailsSingle(OrderDetailsBean orderDetailsBean, int webSite) {
        orderDetailsBean.setGoods_img(checkNullAndReplace(orderDetailsBean.getGoods_img(), webSite));
        orderDetailsBean.setCar_img(checkNullAndReplace(orderDetailsBean.getCar_img(), webSite));
        orderDetailsBean.setGoods_url(checkNullAndReplace(orderDetailsBean.getGoods_url(), webSite));
        orderDetailsBean.setMatch_url(checkNullAndReplace(orderDetailsBean.getMatch_url(), webSite));
    }


    /**
     * 采购详情List bean域名切换
     *
     * @param purchasesBeanList
     */
    public static void changePurchasesBeanList(List<PurchasesBean> purchasesBeanList, int webSite) {
        for (PurchasesBean purchasesBean : purchasesBeanList) {
            changePurchasesBeanSingle(purchasesBean, webSite);
        }
    }


    /**
     * 采购详情单个bean域名切换
     *
     * @param purchasesBean
     */
    public static void changePurchasesBeanSingle(PurchasesBean purchasesBean, int webSite) {
        purchasesBean.setGoods_url(checkNullAndReplace(purchasesBean.getGoods_url(), webSite));
        purchasesBean.setGoogs_img(checkNullAndReplace(purchasesBean.getGoogs_img(), webSite));
        purchasesBean.setImportExUrl(checkNullAndReplace(purchasesBean.getImportExUrl(), webSite));
        purchasesBean.setImg_type(checkNullAndReplace(purchasesBean.getImg_type(), webSite));
    }


    /**
     * 判断是否为空和包含的域名修改
     *
     * @param str
     * @param webSite : 1 import 2 kids 3 pets 4 restaurant
     * @return
     */
    public static String checkNullAndReplace(String str, int webSite) {
        if (StringUtils.isNotBlank(str)) {
            if (webSite == 2) {
                if (str.contains(IMPORT_DOMAIN_NAME_1)) {
                    return str.replace(IMPORT_DOMAIN_NAME_1, KIDS_DOMAIN_NAME_1);
                } else if (str.contains(IMPORT_DOMAIN_NAME_2)) {
                    return str.replace(IMPORT_DOMAIN_NAME_2, KIDS_DOMAIN_NAME_2);
                } else if (str.contains(IMPORT_DOMAIN_NAME_3)) {
                    return str.replace(IMPORT_DOMAIN_NAME_3, KIDS_DOMAIN_NAME_3);
                } else {
                    return str;
                }
            } else if (webSite == 3) {
                if (str.contains(IMPORT_DOMAIN_NAME_1)) {
                    return str.replace(IMPORT_DOMAIN_NAME_1, PETS_DOMAIN_NAME_1);
                } else if (str.contains(IMPORT_DOMAIN_NAME_2)) {
                    return str.replace(IMPORT_DOMAIN_NAME_2, PETS_DOMAIN_NAME_2);
                } else if (str.contains(IMPORT_DOMAIN_NAME_3)) {
                    return str.replace(IMPORT_DOMAIN_NAME_3, PETS_DOMAIN_NAME_3);
                } else {
                    return str;
                }
            } else if (webSite == 3) {
                if (str.contains(IMPORT_DOMAIN_NAME_1)) {
                    return str.replace(IMPORT_DOMAIN_NAME_1, RESTAURANT_DOMAIN_NAME_1);
                } else if (str.contains(IMPORT_DOMAIN_NAME_2)) {
                    return str.replace(IMPORT_DOMAIN_NAME_2, RESTAURANT_DOMAIN_NAME_2);
                } else if (str.contains(IMPORT_DOMAIN_NAME_3)) {
                    return str.replace(IMPORT_DOMAIN_NAME_3, RESTAURANT_DOMAIN_NAME_3);
                } else {
                    return str;
                }
            } else {
                return str;
            }

        } else {
            return str;
        }
    }

    /**
     * 拆单取消List的object域名切换
     *
     * @param objectList
     */
    public static void changeObjectList(List<Object[]> objectList, int webSite) {
        if (objectList != null && !objectList.isEmpty()) {
            for (Object[] obj : objectList) {
                changeObjectSingle(obj, webSite);
            }
        }
    }

    /**
     * 拆单取消单个object域名切换
     *
     * @param obj
     */
    public static void changeObjectSingle(Object[] obj, int webSite) {
        if (obj[5] != null) {
            obj[5] = checkNullAndReplace(obj[5].toString(), webSite);
        }
        if (obj[7] != null) {
            obj[7] = checkNullAndReplace(obj[7].toString(), webSite);
        }
    }
}
