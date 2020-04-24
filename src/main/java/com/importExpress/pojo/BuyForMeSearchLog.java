package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.common.pojo
 * @date:2020/4/15
 */
@Data
public class BuyForMeSearchLog {

    public static int KEY_SEARCH_FLAG = 1;
    public static int IMG_SEARCH_FLAG = 2;
    public static int URL_SEARCH_FLAG = 3;

    private Integer id;

    /**
     * 搜索类型 0默认 1关键词 2图片 3url
     */
    private int search_type;


    private String search_content;
    private String create_time;
    private String ip;
    private Integer user_id;
    private String session_id;

    private String end_time;
    private int limitNum;
    private int startNum;
    private String countryName;
}
