package com.cbt.ocr.dao;

import com.cbt.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

public interface Distinguish_PictureDao {


	public List<CustomGoods> showDistinguish_Pircture(@Param("pid")String pid, @Param("isdelete")int isdelete, @Param("page")int page, @Param("type")String type);

	public int queryDistinguish_PirctureCount(@Param("pid")String pid,@Param("isdelete")int isdelete, @Param("type")String type);

	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList);
	public int updateSomePirctu_risdelete_tow(List<Map<String, String>> bgList);


}