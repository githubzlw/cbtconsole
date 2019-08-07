package com.cbt.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TabTrackInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;

    private String orderNo;

    private String tarOrderNo;  // 主单号跳转到订单详情的单号 区分dp和普通订单

    private String orderList;

    private Integer packageNo;

    private String trackNo;

    private String trackCompany;

    private String tarckAddress;

    private Date orderPaytime;

    private Date orderDeliverDate;

    private Integer trackState; //1-备货中；2-已发货；3-已签收；4-退回；5-异常(海关扣押等)

    private Date addTime;

    private Date trackUpdate;

    private Date updatDate;

    private Date deliveredTime;

    private String trackUser;

    private int grabState;

    private String sourceType;

    private List<TabTrackDetails> tabTrackDetailsList;

    private Date senttime;// 新增 发货时间
    
    private String trackNote;

    private Date trackNoteTime; //备注日期

    private String info;//最后一条物流信息

    private String admName;//负责人

    private Date upStateTime;//手动标记正常时间

    private String email;//客户邮箱

	//添加转单信息查询  2018/08/07 15:16
	private String forwardNo;//转单单号

	private String forwardCompany; //转单物流公司

	private String forwardSourceType; //转单物流抓取地址

	private List<TabTrackForward> forwardList;  // 转单信息

    public TabTrackInfo() {
        super();
    }
    //TabTrackInfo(trackNo, trackState, trackNote)
    public TabTrackInfo(String trackNo, Integer trackState, String trackNote) {
        super();
        this.trackNo = trackNo;
        this.trackState = trackState;
        this.trackNote = trackNote;
    }


}