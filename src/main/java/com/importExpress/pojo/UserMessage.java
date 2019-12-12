package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2019/12/12
 */
@Data
public class UserMessage {

    private int id;
    private int userId;
    private String orderNo;
    private String content;
    private int type = -1;
    private String jumpUrl;
    private int readyState = -1;
    private int delState = -1;
    private String createTime;

    private int startNum;
    private int limitNum;
    private String question;
    private String answer;

}
