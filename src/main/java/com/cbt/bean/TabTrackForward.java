package com.cbt.bean;

import lombok.Data;

import java.util.Date;

@Data
public class TabTrackForward {

    private Long id;

    //抓单信息获取来源 对应tab_track_details中的id
    private Long ttdId = 0L;

    //运单号
    private String trackNo;

    //使用的运输公司
    private String trackCompany;

    //转单 运单号
    private String trackNoForward;

    //转单号 使用的运输公司
    private String trackCompanyForward;

    //转单单号状态：1-备货中；2-已发货；3-已签收；4-退回；5-异常(海关扣押等)；6-内部异常（抓取方式没有，单号问题等）;
    private Integer trackState = 2;

    //最后一次运单变更时间（对应tab_track_details中action_date）
    private Date trackUpdate;

    //最后一次抓取的时间
    private Date updatDate;

    //运单完成时间
    private Date deliveredTime;

    //0 需要抓取，1 不需要抓取
    private Integer grabState = 0;

    //数据获取的来源
    private String sourceType;

    private Date createTime;

    private Date updateTime;

    private String trackCompanyTrack; //快递100识别的单号公司，.one开头的是原始的，其他的是测试能抓取的

    private String actionInfo;

    private String actionInfoCn;

    private Integer flag;//处理标记：0-默认；1-yfh获取转单号成功；2-未成功获取转单号；3-处理异常；4-emsinten获取转单号正常

}