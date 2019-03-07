package com.cbt.warehouse.pojo;

import java.util.List;


public class ReturnRes {
private String cusorder;//获取客户订单

List<Returndetails>	list;

public String getCusorder() {
	return cusorder;
}

public void setCusorder(String cusorder) {
	this.cusorder = cusorder;
}

public List<Returndetails> getList() {
	return list;
}

public void setList(List<Returndetails> list) {
	this.list = list;
}


}
