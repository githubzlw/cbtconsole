package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2020-10-19
 */
@Data
public class CustomPidComboBean {

    private int id;
    private String pid;
    private int is_main;
    private String uuid;
    private double div_price;
    private String create_time;
    private String update_time;
    private int admin_id;
    private String admin_name;
    private String img_url;
    private int valid = 0;
    private int update_admin;
    private String update_admin_name;
}
