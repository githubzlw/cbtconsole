package com.winit.api.constants;

public class RequestParam {

    /**
     * 调用URL,可根据实际情况，修改为相应的环境URL
     */
    public static String URL           = "http://openapi.winit.com.cn/openapi/service";

    /**
     * 调用者账号:即卖家账号
     */
    public static String USERNAME      = "sunny517309@163.com";
    /**
     * 调用者账号密码：即卖家账号的密码
     */
    public static String PASSWORD      = "S520309";

    /**
     * 应用ID，标识应用身份。由开发者创建应用时系统分配
     */
    public static String CLIENT_ID     = "OGMXZDHJYTGTNWZHOS00MTRJLWE3N2MTMTA1Y2QZMDY0MTRM";//"NGRKYWY2YJITYTE2NY00YTBLLWE4YZITYWMWYJEZN2YXMWVK";
    /**
     * 应用秘钥，用于加密验证应用身份。同用户token。由开发者创建应用时系统分配
     */
    public static String CLIENT_SECRET = "MJGYMZY5ZJUTOTI3NI00YWE5LTK4NTITZGNLNMQWMWY4OWMZMTMZMJU0MTM0NZUXNZY2OTC=";//"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM";
    /**
     * 即platform:平台值。与Client_id一一对应，即应用创建时填写的CODE，推荐使用公司英文简称或中文汉语首字母组合，大写
     */
    public static String CLIENT_CODE = "RTYU";//"WINIT";
    
    

}
