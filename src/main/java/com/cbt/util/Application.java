package com.cbt.util;

import com.cbt.bean.ClassDiscount;
import com.cbt.processes.dao.IPreferentialDao;
import com.cbt.processes.dao.PreferentialDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class Application {

	//获取混批优惠值
	public static List<ClassDiscount> getClassDiscount(HttpServletRequest request){
		//获取混批折扣率
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("classdiscount");
		List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
		if(obj == null){
			IPreferentialDao pre = new PreferentialDao();
			list_cd = pre.getClass_discount();
			session.setAttribute("classdiscount", list_cd);
		}else{
			list_cd = (List<ClassDiscount>) obj;
			for (int i = 0; i < list_cd.size(); i++) {
				list_cd.get(i).setSum_price(0);
 			}
		}
		return list_cd;
	}
}
