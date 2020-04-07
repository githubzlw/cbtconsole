package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2020/4/7
 */
@Data
public class BFChat {

    private Integer id;
    private String pid;
    private Integer bd_id;
    private String content;
    private String createTime;
}
