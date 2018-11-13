package com.cbt.util;

import com.cbt.bean.ShippingBean;

import java.util.Comparator;

public class ShipComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		ShippingBean sb1=(ShippingBean)o1;
		ShippingBean sb2=(ShippingBean)o2;
		if(sb1.getResult()>sb2.getResult()){
			return 1;
		}
		if(sb1.getResult()<sb2.getResult()){
			return -1;
		}
		return 0;
	}

}
