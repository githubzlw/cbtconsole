package com.cbt.ocr.dao;

import com.cbt.ocr.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

public interface Distinguish_PictureDao {


	public List<CustomGoods> showDistinguish_Pircture(@Param("pid")String pid,@Param("shopid")String shopid,@Param("isdelete")int isdelete,@Param("page")int page);

	public int queryDistinguish_PirctureCount(@Param("pid")String pid,@Param("shopid")String shopid,@Param("isdelete")int isdelete);

	public int updatePirctu_risdelete(@Param("id")int id,@Param("ocrneeddelete")int ocrneeddelete);

	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList);
}