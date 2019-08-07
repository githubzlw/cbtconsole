package com.importExpress.pojo;

import lombok.Data;

@Data
public class TimingWarningInfo {

    private Integer id;

    private Integer parentId;

    //排序
    private Integer infoIndex = 0;

    //标题
    private String title;

    //预警信息
    private String info;

    //预警sql对应数据库
    private String waringPool;

    //预警查询 SQL
    private String waringSql;

    //预警查询 SQL 查询的结果
    private Long waringResult = -1L;

    //预警单位
    private String waringUnit = "";

    //预警阀值(数量或天数大于)
    private Long quotaGt;

    //预警阀值(数量或天数小于)
    private Long quotaLt;

    //预警阀值(数量或天数大于等于)
    private Long quotaGe;

    //预警阀值(数量或天数小于等于)
    private Long quotaLe;

    //预警阀值单位
    private String quotaUnit = "";

    //是否超过阀值 0 false-默认;1 true-超过阀值;（代码计算好, 超过阀值的页面红色显示）
    private Boolean waringQuota = false;

    //是否有效: 0-无效 不进行预警; 1-有效 进行预警; 10-无效 无效的是历史记录; 11-有效 有效的用于页面显示; 12-异常 提供的sql查询异常
    private Integer valid = 0;

    //备注(说明及提供人)
    private String remark;

    //预警数据查询时间
    private Long curTime;

    //创建时间
    private Long createTime;

    //创建时间
    private Long updateTime;

    public void judgeWaringQuota() {
        if (waringResult < 0) {
            return;
        }
        Long result = null;
        if ("天".equals(waringUnit)) {
            result = (curTime - waringResult) / 3600 / 24;
        } else {
            result = waringResult;
        }

        if (quotaGt != null && result > quotaGt) {
            waringQuota = true;
        }
        if (quotaLt != null && result < quotaLt) {
            waringQuota = true;
        }
        if (quotaGe != null && result >= quotaGe) {
            waringQuota = true;
        }
        if (quotaLe != null && result <= quotaLe) {
            waringQuota = true;
        }
    }

    public void setWaringResult(Long waringResult) {
        if (waringResult != null && waringResult >= 0) {
            this.waringResult = waringResult;
        }
    }
}