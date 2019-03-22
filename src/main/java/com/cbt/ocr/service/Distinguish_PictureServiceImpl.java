package com.cbt.ocr.service;

import com.cbt.ocr.dao.Distinguish_PictureDao;
import com.cbt.ocr.pojo.CustomGoods;
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
	public List<CustomGoods> showDistinguish_Pircture(String pid, String shopid, int isdelete,int page) {
		page=(page-1)*40;
		List<CustomGoods> list=distinguish_PictureDao.showDistinguish_Pircture(pid,shopid,isdelete,page);
		int count=distinguish_PictureDao.queryDistinguish_PirctureCount(pid,shopid,isdelete);
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
	public int queryDistinguish_PirctureCount(String pid, String shopid, int isdelete) {
		return distinguish_PictureDao.queryDistinguish_PirctureCount(pid,shopid,isdelete);
	}

	@Override
	public int updatePirctu_risdelete(int id, int ocrneeddelete) {

		return distinguish_PictureDao.updatePirctu_risdelete(id,ocrneeddelete);
	}

	@Override
	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList) {
		return distinguish_PictureDao.updateSomePirctu_risdelete(bgList);
	}
}