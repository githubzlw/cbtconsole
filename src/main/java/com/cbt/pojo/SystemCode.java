package com.cbt.pojo;

import java.util.Date;

public class SystemCode {
    private Integer id;

    private String codeType;

    private String codeTypeName;

    private String codeValueCode;

    private String codeValueName;

    private Date createDatetime;

    private String createOperator;

    private Date updateDatetime;

    private String updateOperator;

    private Integer status;

    private String pictureUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType == null ? null : codeType.trim();
    }

    public String getCodeTypeName() {
        return codeTypeName;
    }

    public void setCodeTypeName(String codeTypeName) {
        this.codeTypeName = codeTypeName == null ? null : codeTypeName.trim();
    }

    public String getCodeValueCode() {
        return codeValueCode;
    }

    public void setCodeValueCode(String codeValueCode) {
        this.codeValueCode = codeValueCode == null ? null : codeValueCode.trim();
    }

    public String getCodeValueName() {
        return codeValueName;
    }

    public void setCodeValueName(String codeValueName) {
        this.codeValueName = codeValueName == null ? null : codeValueName.trim();
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator == null ? null : createOperator.trim();
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator == null ? null : updateOperator.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl == null ? null : pictureUrl.trim();
    }
}