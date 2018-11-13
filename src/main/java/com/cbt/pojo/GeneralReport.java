package com.cbt.pojo;

import java.util.Date;

public class GeneralReport {
    private Integer id;

    private Integer breportId;

    private String breportName;

    private String reportYear;

    private String reportMonth;

    private String reportWeek;

    private String reportDay;

    private String createOpertor;

    private Date createDate;
    
    private int noFreeFreight;

    public int getNoFreeFreight() {
		return noFreeFreight;
	}

	public void setNoFreeFreight(int noFreeFreight) {
		this.noFreeFreight = noFreeFreight;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBreportId() {
        return breportId;
    }

    public void setBreportId(Integer breportId) {
        this.breportId = breportId;
    }

    public String getBreportName() {
        return breportName;
    }

    public void setBreportName(String breportName) {
        this.breportName = breportName == null ? null : breportName.trim();
    }

    public String getReportYear() {
        return reportYear;
    }

    public void setReportYear(String reportYear) {
        this.reportYear = reportYear == null ? null : reportYear.trim();
    }

    public String getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(String reportMonth) {
        this.reportMonth = reportMonth == null ? null : reportMonth.trim();
    }

    public String getReportWeek() {
        return reportWeek;
    }

    public void setReportWeek(String reportWeek) {
        this.reportWeek = reportWeek == null ? null : reportWeek.trim();
    }

    public String getReportDay() {
        return reportDay;
    }

    public void setReportDay(String reportDay) {
        this.reportDay = reportDay == null ? null : reportDay.trim();
    }

    public String getCreateOpertor() {
        return createOpertor;
    }

    public void setCreateOpertor(String createOpertor) {
        this.createOpertor = createOpertor == null ? null : createOpertor.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}