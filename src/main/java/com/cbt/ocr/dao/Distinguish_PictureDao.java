package com.cbt.ocr.dao;

import com.cbt.pojo.Category1688;
import com.cbt.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

public interface Distinguish_PictureDao {


	public List<CustomGoods> showDistinguish_Pircture(@Param("pid")String pid,  @Param("page")int page, @Param("imgtype")String imgtype, @Param("state")String state, @Param("Change_user")String Change_user);

	public List<CustomGoods> showDistinguish_Pircture_2();

	public int queryDistinguish_PirctureCount(@Param("pid")String pid, @Param("imgtype")String imgtype, @Param("state")String state, @Param("Change_user")String Change_user);

	public int updateSomePirctu_risdelete(@Param("bgList")List<Map<String, String>> bgList,@Param("type")int type,@Param("userName")String userName);

	public List<Category1688> showCategory1688_type();



}