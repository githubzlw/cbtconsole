package com.cbt.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.cbt.pojo
 * @date:2020/5/8
 */
@Data
public class SearchStatic {
    private int id;
    private String keyword;
    private int site;
    private String create_time;
    private int valid;
    private int state;
    private String json_name;
    private String update_time;

    private int admin_id;
    private String admin_name;

    private int up_admin_id;
    private String up_admin_name;

    private int startNum;
    private int limitNum;

    private Integer page;
    private Integer rows;





}
