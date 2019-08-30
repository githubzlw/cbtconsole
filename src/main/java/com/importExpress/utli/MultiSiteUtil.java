package com.importExpress.utli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class MultiSiteUtil {

    /**
     *  根据订单号判断订单所在网站
     *      线上订单生成方法: com.importExpress.common.util.Utility#generateOrderNumber
     *
     * <pre>
     * MultiSiteUtil.getSiteTypeNum(null)               = 1
     * MultiSiteUtil.getSiteTypeNum("")                 = 1
     * MultiSiteUtil.getSiteTypeNum("1190704K721")      = 2
     * MultiSiteUtil.getSiteTypeNum("  1190704K721  ")  = 2
     * MultiSiteUtil.getSiteTypeNum("1190704721")       = 1
     * MultiSiteUtil.getSiteTypeNum("  1190704721  ")   = 1
     * </pre>
     *
     * @param orderNo
     * @return 1-非kids网站(importx网站); 2-kids网站; 3 pets网站
     */
    public static Integer getSiteTypeNum(String orderNo){
        if (StringUtils.isNotBlank(orderNo)
                && StringUtils.indexOfIgnoreCase(orderNo.trim(), "k") == 7) {
            return 2;
        }
        if (StringUtils.isNotBlank(orderNo)
                && StringUtils.indexOfIgnoreCase(orderNo.trim(), "p") == 7) {
            return 3;
        }
        return 1;
    }


    /**
     *  根据线上地址判断网站类型
     *
     * <pre>
     * MultiSiteUtil.getSiteTypeNum(null)               = 1
     * MultiSiteUtil.getSiteTypeNum("")                 = 1
     * MultiSiteUtil.getSiteTypeNum("https://www.import-express.com/goodsinfo/")      = 1
     * MultiSiteUtil.getSiteTypeNum("https://www.kidsproductwholesale.com/goodsinfo")  = 2
     * </pre>
     *
     * @param url
     * @return 1-非kids网站(importx网站); 2-kids网站; 3-pets网站;
     */
    public static Integer getSiteTypeNumByUrl(String url){
        if (StringUtils.isNotBlank(url)
                && StringUtils.containsIgnoreCase(url, "www.kidsproductwholesale.com")) {
            return 2;
        }
        if (StringUtils.isNotBlank(url)
                && StringUtils.containsIgnoreCase(url, "www.lovelypetsupply.com")) {
            return 3;
        }
        return 1;
    }

    /**
     *  返回线上网站类型
     * @return 1-非kids网站(importx网站); 2-kids网站;
     */
    public static Integer getSiteTypeNumByType(String siteType){
        if ("k".equalsIgnoreCase(siteType)) {
            return 2;
        }
        if ("p".equalsIgnoreCase(siteType)) {
            return 3;
        }
        return 1;
    }


    /**
     *  返回线上网站名
     *
     */
    public static String getSiteTypeStrByNum(Integer num){
        if (num != null) {
            if (num == 2) {
                return "kids";
            } else if (num == 3) {
                return "pets";
            }
        }
        return "importx";
    }

    /**
     *  返回线上网站名
     *
     */
    public static String getSiteTypeStrByType(String siteType){
        if ("k".equalsIgnoreCase(siteType)) {
            return "kids";
        } else if ("p".equalsIgnoreCase(siteType)) {
            return "pets";
        }
        return "importx";
    }

    /**
     *  返回线上网址
     *
     */
    public static String getSiteUrlByNum(Integer num){
        if (num != null) {
            if (num == 2) {
                return SearchFileUtils.kidsPath;
            } else if (num == 3) {
                return SearchFileUtils.petsPath;
            }
        }
        return SearchFileUtils.importexpressPath;
    }
    public static String getSiteTypeStrBySit(Integer num){
        String webName = "importX";
        switch (num){
            case 2:
                webName = "kids";
                break;
            case 3:
                webName = "importX/kids";
                break;
            case 4:
                webName = "pets";
                break;
           default:
               break;
        }
        return webName;
    }

    public static String getWebSiteUrl(Integer site) {
        switch (site) {
            case 1:
                return "https://www.import-express.com";
            case 2:
                return "https://www.kidsproductwholesale.com";
            case 3:
                return "https://www.lovelypetsupply.com";
        }
        return "";
    }


    public static void main(String[] args) {
        System.err.println(getSiteTypeNum("2190822P588"));
    }

}
