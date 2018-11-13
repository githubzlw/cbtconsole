package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.TrankBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TrackDao {

	public void saveTrackInfo(@Param("list") List<TrankBean> list);

	//获取JCEX运单号
	public List<TrankBean> getAllJCEX(@Param("company") String company, @Param("senttimeBegin") String senttimeBegin, @Param("senttimeEnd") String senttimeEnd);

	public void deleteInfoByJCEX(String company);

	public List<TrankBean> selectByCompany(String company);

	public int selectByExpressnoAndCompany(@Param("company") String company, @Param("expressno") String expressno);

	public List<TrankBean> selectByCompany1(@Param("company") String company, @Param("admName") String admName);

	public int saveBzByExpressnoAndCompany(@Param("bz") String bz, @Param("id") int id);

	public int deleteInfoList(@Param("list") List<TrankBean> newList);

	public List<String> getadmInfo();

	public List<TrankBean> getAllTracks(String createtime);

	public void saveBugTrack(List<Map<String, Object>> list);

	public void saveBugTrack1(List<Map<String, Object>> list);

	public void saveBugTrack2(List<Map<String, Object>> list);

	public List<Map<String,String>> selectTrackInfoByAdmId(@Param("id") Integer id, @Param("readFlag") String readFlag, @Param("start") int start, @Param("transportcompany") String transportcompany);

	public int updateBzById(@Param("bz") String bz, @Param("expressno") String expressno);

	public void updateReadFlag(String expressno);

	public int countSelectTrackInfoByAdmId(@Param("id") Integer id, @Param("readFlag") String readFlag, @Param("transportcompany") String transportcompany);
}
