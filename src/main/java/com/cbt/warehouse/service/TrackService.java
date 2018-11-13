package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.TrankBean;

import java.util.List;
import java.util.Map;

public interface TrackService  {

	//将物流信息存入数据库
    public void saveTrackInfo(List<TrankBean> list);
    
  //获取JCEX运单信息
  	public List<TrankBean> getAllJCEX(String company, String senttimeBegin, String senttimeEnd);

	public void deleteInfoByJCEX(String company);

	public List<TrankBean> selectByCompany(String company);

	public List<TrankBean> selectByCompany1(String company, String admName);

	public int saveBzByExpressnoAndCompany(String bz, int id);

	public int deleteInfoList(List<TrankBean> newList);

	public List<String> getadmInfo();

	public void getAllTracks();

	public void saveBugTrack(List<Map<String, Object>> list);

	public List<Map<String,String>>  selectTrackInfoByAdmId(Integer id, String readFlag, int page, String transportcompany);

	public int updateBzById(int id, String bz, String expressno);

	public void updateReadFlag(String expressno);
}
