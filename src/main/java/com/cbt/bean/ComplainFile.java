package com.cbt.bean;

import lombok.Data;

@Data
public class ComplainFile {
    private int id;
    private int complainid;
    private int complainChatid;
    private String imgUrl;
    private int delState;
    private int flag;
    private String uuid;

}
