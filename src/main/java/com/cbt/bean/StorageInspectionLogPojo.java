package com.cbt.bean;

import java.io.Serializable;

public class StorageInspectionLogPojo implements Serializable{
    private static final long serialVersionUID = 5743567148073659801L;

    private String in_count;
    private String yhww_count;
    private String yywt_count;
    private String no_count;
    private String out_counts;
    public String getIn_count() {
        return in_count;
    }

    public void setIn_count(String in_count) {
        this.in_count = in_count;
    }

    public String getYhww_count() {
        return yhww_count;
    }

    public void setYhww_count(String yhww_count) {
        this.yhww_count = yhww_count;
    }

    public String getYywt_count() {
        return yywt_count;
    }

    public void setYywt_count(String yywt_count) {
        this.yywt_count = yywt_count;
    }

    public String getNo_count() {
        return no_count;
    }

    public void setNo_count(String no_count) {
        this.no_count = no_count;
    }

    public String getOut_counts() {
        return out_counts;
    }

    public void setOut_counts(String out_counts) {
        this.out_counts = out_counts;
    }

}
