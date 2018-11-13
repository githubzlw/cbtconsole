package com.cbt.website.dao2;

import java.util.List;

public class Page {
	private List records;
	private int pagenum;//当前页数
	private int pagesize=20;// 默认容量
	private int totalrecords;//总记录数
	private int totalpage;// 总页数
	private int startindex;//开始索引

	public double getPid_amount() {
		return pid_amount;
	}

	public void setPid_amount(double pid_amount) {
		this.pid_amount = pid_amount;
	}

	private double pid_amount;
	
	public Page() {
		
	}
	
	public Page(int pagenum,int totalrecords){
		this.totalrecords = totalrecords;
		//计算总页数
		totalpage = totalrecords%pagesize==0?totalrecords/pagesize:(totalrecords/pagesize+1);
		if(pagenum>totalpage){
			this.pagenum=totalpage;
		}else if(pagenum<1){
			this.pagenum =1;
		}else{
			this.pagenum=pagenum;
		}
		//计算开始索引
		if(totalrecords==0){
			startindex=0;
		}else{
			startindex = (this.pagenum-1)*pagesize;
		}
	}
	public Page(int pagenum,int totalrecords,int pagesize){
		this.totalrecords = totalrecords;
		this.pagesize = pagesize;
		//计算总页数
		totalpage = totalrecords%pagesize==0?totalrecords/pagesize:(totalrecords/pagesize+1);
		//
		if(pagenum>totalpage){
			this.pagenum=totalpage;
		}else if(pagenum<1){
			this.pagenum =1;
		}else{
			this.pagenum=pagenum;
		}
		//计算开始索引
		if(totalrecords==0){
			startindex=0;
		}else{
			startindex = (this.pagenum-1)*pagesize;
		}
	}

	public List getRecords() {
		return records;
	}

	public void setRecords(List records) {
		this.records = records;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getPagesize() {
		
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getTotalrecords() {
		return totalrecords;
	}

	public void setTotalrecords(int totalrecords) {
		this.totalrecords = totalrecords;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}
	
}
