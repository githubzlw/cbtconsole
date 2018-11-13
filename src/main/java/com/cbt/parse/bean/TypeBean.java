package com.cbt.parse.bean;

/**规格数据
 * @author abc
 *
 */
public class TypeBean {
	private String type;//规格类型
	private String value;//规格值
	private String img;//图片
	private String id;//id
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		if(value!=null){
			value = value.replaceAll("\"", "").replaceAll("'", "");
		}
		this.value = value;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return "id=" + id+
				"+# type=" + type + 
				"+# value=" + value+ 
				"+# img=" + img ;
	}
	
	
	
	

}
