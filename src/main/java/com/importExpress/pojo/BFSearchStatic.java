package com.importExpress.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2020/4/26
 */
@Data
public class BFSearchStatic {

    private int id;
    private String keyword;
    private String pid1;
    private String pid2;
    private String create_time;
    private String update_time;
    private int admin_id;
    private String admin_name;

    private String end_time;

    private int del_flag;
    private int limitNum;
    private int startNum;

    private List<BFSearchPid> pidList;
}
