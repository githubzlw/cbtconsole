
package com.cbt.feedback.bean;

public class ErrorFeedback {

	private  int  id ;
	private  int  type ;        //异常问题所属模块    例如 1.物流跟踪  2.XX  
	private  String  content;       //异常反馈内容 
	private  int     positionFlag ; // 前后台标识  1 前台 , 2后台
	private  int     delFlag  ;    // 处理机制
	private  String  belongTo ;    //负责人
	private  String  remark ;      //备注信息
	private  String  createtime ;   //发生时间
	private   String   logLocation ;  //log 日志文件路径
	private    int     count ;   
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPositionFlag() {
		return positionFlag;
	}
	public void setPositionFlag(int positionFlag) {
		this.positionFlag = positionFlag;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	public String getBelongTo() {
		return belongTo;
	}
	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getLogLocation() {
		return logLocation;
	}
	public void setLogLocation(String logLocation) {
		this.logLocation = logLocation;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
	
	
}
