package com.cbt.parse.check;

import com.cbt.bean.Eightcatergory;
import com.cbt.parse.dao.CatergoryDao;
import com.cbt.parse.dao.GoodsDao;
import com.cbt.parse.daoimp.ICatergoryDao;
import com.cbt.parse.daoimp.IGoodsDao;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;

import java.util.ArrayList;

public class CheckGoodsUrl {
	
	public static ArrayList<Eightcatergory> check(){
		ArrayList<Eightcatergory> list = new ArrayList<Eightcatergory>();
		ICatergoryDao cd = new CatergoryDao();
		IGoodsDao gd = new GoodsDao();
		ArrayList<Eightcatergory> urls = cd.queryData();
		String url = null;
		GoodsBean goods = null;
		
		if(urls!=null&&!urls.isEmpty()){
			int urls_num = urls.size();
			for(int i=0;i<urls_num;i++){
				url = urls.get(i).getUrl();
				goods = ParseGoodsUrl.parseGoodsw(url,0);
				if(goods==null||goods.isEmpty()){
					list.add(urls.get(i));
					gd.updatevalid("0", url);
					cd.updateValid(url, 0);
				}
				url = null;
				goods = null;
			}
		}
		return list;
	}
}
