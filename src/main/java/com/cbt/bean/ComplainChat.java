package com.cbt.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ComplainChat implements Serializable {

    private static final long serialVersionUID = 9977586222L;

    private int id;
    private int complainid;
    private String chatText;
    private Date chatTime;
    private String chatAdmin;
    private int chatAdminid;
    private String uuid;

}
