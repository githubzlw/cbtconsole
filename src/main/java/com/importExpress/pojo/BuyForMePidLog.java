package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.common.pojo
 * @date:2020/4/29
 */
@Data
public class BuyForMePidLog {

    private int id;
    private String pid;
    private int type;

    private String title;
    private String img_url;
    private int user_id;
    private String session_id;
    private String create_time;
    private String ip;

    private String end_time;
    private int limitNum;
    private int startNum;

}
