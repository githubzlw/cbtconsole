package com.importExpress.utli;

import com.importExpress.pojo.ShopCarMarketing;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 切换域名
 */
public class SwitchDomainNameUtil {
    private static final String OLD_DOMAIN_NAME1 = "import-express";
    private static final String NEW_DOMAIN_NAME1 = "kidsproductwholesale";
    private static final String OLD_DOMAIN_NAME2 = "ImportExpress";
    private static final String NEW_DOMAIN_NAME2 = "KidsProductWholesale";
    private static final String OLD_DOMAIN_NAME3 = "Import Express";
    private static final String NEW_DOMAIN_NAME3 = "Kids Product Wholesale";


    /**
     * 购物车营销List的bean域名切换
     *
     * @param shopCarMarketingList
     */
    public static void changeShopCarMarketingList(List<ShopCarMarketing> shopCarMarketingList) {
        if (shopCarMarketingList != null && !shopCarMarketingList.isEmpty()) {
            for (ShopCarMarketing shopCarMarketing : shopCarMarketingList) {
                changeShopCarMarketingSingle(shopCarMarketing);
            }
        }
    }

    /**
     * 购物车营销单个bean域名切换
     *
     * @param shopCarMarketing
     */
    public static void changeShopCarMarketingSingle(ShopCarMarketing shopCarMarketing) {
        shopCarMarketing.setGoogsImg(checkNullAndReplace(shopCarMarketing.getGoogsImg()));
    }

    /**
     * 判断是否为空和包含的域名修改
     *
     * @param str
     * @return
     */
    public static String checkNullAndReplace(String str) {
        if (StringUtils.isNotBlank(str)) {
            if (str.contains(OLD_DOMAIN_NAME1)) {
                return str.replace(OLD_DOMAIN_NAME1, NEW_DOMAIN_NAME1);
            } else if (str.contains(OLD_DOMAIN_NAME2)) {
                return str.replace(OLD_DOMAIN_NAME2, NEW_DOMAIN_NAME2);
            } else if (str.contains(OLD_DOMAIN_NAME3)) {
                return str.replace(OLD_DOMAIN_NAME3, NEW_DOMAIN_NAME3);
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
    public static void changeObjectList(List<Object[]> objectList) {
        if (objectList != null && !objectList.isEmpty()) {
            for (Object[] obj : objectList) {
                changeObjectSingle(obj);
            }
        }
    }

    /**
     * 拆单取消单个object域名切换
     *
     * @param obj
     */
    public static void changeObjectSingle(Object[] obj) {
        if (obj[5] != null) {
            obj[5] = checkNullAndReplace(obj[5].toString());
        }
        if (obj[7] != null) {
            obj[7] = checkNullAndReplace(obj[7].toString());
        }
    }
}
