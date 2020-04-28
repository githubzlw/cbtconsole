package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2020/4/26
 */
@Data
public class BFSearchPid {

    private int id;
    private int static_id;
    private String pid;
    private String title;
    private String create_time;
    private String update_time;
    private int admin_id;
    private String admin_name;

    private int del_flag;

    private int num;

}
