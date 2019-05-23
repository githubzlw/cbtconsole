package com.cbt.ocr.dao;

import com.cbt.pojo.Admuser;
import com.cbt.pojo.Category1688;
import com.cbt.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface Distinguish_PictureDao {


	public List<CustomGoods> showDistinguish_Pircture(@Param("page")int page, @Param("imgtype")String imgtype, @Param("state")String state, @Param("Change_user")String Change_user);

	public List<CustomGoods> FindRecognition_delete_details(Map<String, Object> map);

	public int FindRecognition_delete_count(Map<String, Object> map);

	public List<Admuser> showDistinguish_Pircture_operationUser();

	public int queryDistinguish_PirctureCount( @Param("imgtype")String imgtype, @Param("state")String state, @Param("Change_user")String Change_user);

	public int updateSomePirctu_risdelete(@Param("bgList")List<Map<String, String>> bgList,@Param("userName")String userName,@Param("type")int type);

	public int updateSomePirctu_risdelete_s(@Param("bgList_s")List<Map<String, String>> bgList_s,@Param("userName")String userName);
	public List<Category1688> showCategory1688_type();

	public int updateSomePirctu_risdelete_date(@Param("bgList_s")List<Map<String, String>> bgList);

    @Select("SELECT cdm.id,cdm.pid,cdm.goods_md5,cdm.remote_path as remotepath FROM " +
            "   custom_goods_md5 cdm where goods_md5  in (SELECT DISTINCT goods_md5 FROM custom_goods_md5 where " +
            " ocr_need_delete=1 and ocr_need_date=1 AND is_delete !=1 AND user_operation=#{admName})")
    List<CustomGoods> deleteAllPriceByAdmname(@Param("admName") String admName);
}