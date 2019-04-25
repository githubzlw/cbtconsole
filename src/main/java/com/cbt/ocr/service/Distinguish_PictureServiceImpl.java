package com.cbt.ocr.service;

import com.cbt.ocr.dao.Distinguish_PictureDao;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Category1688;
import com.cbt.pojo.CustomGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Distinguish_PictureServiceImpl implements Distinguish_PictureService{

	@Autowired
	public Distinguish_PictureDao distinguish_PictureDao;

	@Override
	public List<CustomGoods> showDistinguish_Pircture(int page,String imgtype,String state,String Change_user) {
		page=(page-1)*35;
		List<CustomGoods> list=distinguish_PictureDao.showDistinguish_Pircture(page,imgtype,state,Change_user);
		int count=distinguish_PictureDao.queryDistinguish_PirctureCount(imgtype,state,Change_user);
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
	public List<Admuser> showDistinguish_Pircture_2() {

		return distinguish_PictureDao.showDistinguish_Pircture_2();
	}
	@Override
	public int queryDistinguish_PirctureCount(String imgtype,String state,String Change_user) {
		return distinguish_PictureDao.queryDistinguish_PirctureCount(imgtype,state,Change_user);
	}
	@Override
	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList,String userName,int type) {

		return distinguish_PictureDao.updateSomePirctu_risdelete(bgList,userName,type);
	}
	@Override
	public int updateSomePirctu_risdelete_s(List<Map<String, String>> maList_s,String userName) {

		return distinguish_PictureDao.updateSomePirctu_risdelete_s(maList_s,userName);
	}
	@Override
	public List<Category1688> showCategory1688_type(){
		return distinguish_PictureDao.showCategory1688_type();
	};
}