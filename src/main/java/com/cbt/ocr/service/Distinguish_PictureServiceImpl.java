package com.cbt.ocr.service;

import com.cbt.ocr.dao.Distinguish_PictureDao;
import com.cbt.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Distinguish_PictureServiceImpl implements Distinguish_PictureService{

	@Autowired
	public Distinguish_PictureDao distinguish_PictureDao;

	@Override
	public List<CustomGoods> showDistinguish_Pircture(String pid,  int isdelete, int page,String type) {
		page=(page-1)*40;
		List<CustomGoods> list=distinguish_PictureDao.showDistinguish_Pircture(pid,isdelete,page,type);
		int count=distinguish_PictureDao.queryDistinguish_PirctureCount(pid,isdelete,type);
		String len="/usr/local/goodsimg";
		for (int i=0;i<list.size();i++){
			list.get(i).setRemotepath("https://img.import-express.com"+list.get(i).getRemotepath().substring(len.length(),list.get(i).getRemotepath().length()));
		}
		if(list.size()>0){
			list.get(0).setCount(count);
		}

		return list;
	}

	@Override
	public int queryDistinguish_PirctureCount(String pid, int isdelete,String type) {
		return distinguish_PictureDao.queryDistinguish_PirctureCount(pid,isdelete,type);
	}
	@Override
	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList,int type) {
		int data;
		if(type==2)
			data=distinguish_PictureDao.updateSomePirctu_risdelete(bgList);
		else
			data=distinguish_PictureDao.updateSomePirctu_risdelete_tow(bgList);
		return data;
	}
}