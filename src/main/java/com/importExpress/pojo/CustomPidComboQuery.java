package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2020-10-19
 */
@Data
public class CustomPidComboQuery extends CustomPidComboBean {

    private int limitNum;
    private int startNum;
    private int page;
}
