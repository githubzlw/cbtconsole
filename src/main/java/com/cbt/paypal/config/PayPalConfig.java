package com.cbt.paypal.config;


import com.cbt.util.GetConfigureInfo;

/**
 * *****************************************************************************************
 * 类描述：PayPal支付配置类
 *
 * @author: luohao
 * @date： 2018-04-27
 * @version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0        2018-04-27          luohao                    初版
 * ******************************************************************************************
 */


public class PayPalConfig {

    public static String clientId;
    public static String clientSecret;
    public static String mode;

    public static String clientIdOld;
    public static String clientSecretOld;

    static {
//        final String SANDBOX_FLAG = PropertyUtils.getValueFromPaypelFile("SANDBOX_FLAG");
//        final String SANDBOX_CLIENT_ID = PropertyUtils.getValueFromPaypelFile("SANDBOX_CLIENT_ID");
//        final String SANDBOX_CLIENT_SECRET = PropertyUtils.getValueFromPaypelFile("SANDBOX_CLIENT_SECRET");
//        final String LIVE_CLIENT_ID = PropertyUtils.getValueFromPaypelFile("LIVE_CLIENT_ID");
//        final String LIVE_CLIENT_SECRET = PropertyUtils.getValueFromPaypelFile("LIVE_CLIENT_SECRET");

        final String SANDBOX_FLAG = GetConfigureInfo.getValueByPapPal("SANDBOX_FLAG");
        final String SANDBOX_CLIENT_ID = GetConfigureInfo.getValueByPapPal("SANDBOX_CLIENT_ID");
        final String SANDBOX_CLIENT_SECRET = GetConfigureInfo.getValueByPapPal("SANDBOX_CLIENT_SECRET");
        final String LIVE_CLIENT_OLD_ID = GetConfigureInfo.getValueByPapPal("LIVE_CLIENT_OLD_ID");
        final String LIVE_CLIENT_OLD_SECRET = GetConfigureInfo.getValueByPapPal("LIVE_CLIENT_OLD_SECRET");

        final String LIVE_CLIENT_NEW_ID = GetConfigureInfo.getValueByPapPal("LIVE_CLIENT_NEW_ID");
        final String LIVE_CLIENT_NEW_SECRET = GetConfigureInfo.getValueByPapPal("LIVE_CLIENT_NEW_SECRET");

        if (SANDBOX_FLAG.equals("true")) {
            clientId = SANDBOX_CLIENT_ID;
            clientSecret = SANDBOX_CLIENT_SECRET;
            mode = "sandbox";
        } else {
            clientId = LIVE_CLIENT_NEW_ID;
            clientSecret = LIVE_CLIENT_NEW_SECRET;
            mode = "live";
        }
        clientIdOld = LIVE_CLIENT_OLD_ID;
        clientSecretOld = LIVE_CLIENT_OLD_SECRET;
    }
}