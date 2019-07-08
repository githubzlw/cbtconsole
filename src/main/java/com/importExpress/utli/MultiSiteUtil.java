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
     * @return 1-非kids网站(importx网站); 2-kids网站;
     */
    public static Integer getSiteTypeNum(String orderNo){
        if (StringUtils.isNotBlank(orderNo)
                && StringUtils.indexOfIgnoreCase(orderNo.trim(), "k") == 7) {
            return 2;
        }
        return 1;
    }

}
