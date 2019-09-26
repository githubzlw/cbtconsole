package com.importExpress.utli;

import com.importExpress.pojo.*;
import org.apache.commons.lang3.StringUtils;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 切换域名
 */
public class SwitchDomainUtil {
    private static final String IMPORT_WEBSITE = "import-express.com";
    private static final String KID_WEBSITE = "kidsproductwholesale.com";
    private static final String PET_WEBSITE = "lovelypetsupply.com";
    public static final String HTTP_IMPORT_WEBSITE = "https://www.import-express.com";
    public static final String HTTP_KID_WEBSITE = "https://www.kidsproductwholesale.com";
    public static final String HTTP_PET_WEBSITE = "https://www.lovelypetsupply.com";
    private static final char IMPORT = 'I';
    private static final char KID = 'K';
    private static final char PET = 'P';

//    private static final String IMPORT_WEBSITE_VIDEOURL_1 = "img1.import-express.com";
//    private static final String IMPORT_WEBSITE_VIDEOURL = "img.import-express.com";
//    private static final String KID_WEBSITE_VIDEOURL = "img1.kidsproductwholesale.com";
//    private static final String PET_WEBSITE_VIDEOURL = "img1.lovelypetsupply.com";
    public static char getIMPORT() {
        return IMPORT;
    }

    public static char getKID() {
        return KID;
    }

    public static char getPET() {
        return PET;
    }


    /**
     * 检查是否未null和替换
     *
     * @param oldStr
     * @return
     */
    public static String checkIsNullAndReplace(String oldStr,int site) {
        if (StringUtils.isNotBlank(oldStr)) {
            String tempStr;
            switch (site) {
                case 2:
                    tempStr = oldStr.replace(IMPORT_WEBSITE, KID_WEBSITE);
                    break;
                case 4:
                    tempStr = oldStr.replace(IMPORT_WEBSITE, PET_WEBSITE);
                    break;
                default:
                    tempStr = oldStr;
            }
            return tempStr;
        } else {
            return oldStr;
        }
    }
}
