package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2019/10/18
 */
@Data
public class UserRecommendEmail {
    private Integer id;
    private Integer userId;
    private String emailContent;
    private Integer adminId;
    private String adminName;
    private String createTime;
    private String sendUrl;
    private int openFlag;
    private Integer catalogId;

}
