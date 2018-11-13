package com.cbt.parse.bean;

/**查询数据表规则数据结构
 * @author abc
 *
 */
public class SqlBean {
	String para;//代表匹配查询的参数
	String value;//参数对应的值
	
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
