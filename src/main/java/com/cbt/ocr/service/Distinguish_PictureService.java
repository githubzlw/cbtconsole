package com.cbt.ocr.service;

import com.cbt.pojo.Admuser;
import com.cbt.pojo.Category1688;
import com.cbt.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface Distinguish_PictureService {

	/***
	 * 按条件查询OCR识别错误图片
	 * @param
	 * @param
	 * @param
	 * @return
	 *
	 */
	public List<CustomGoods> showDistinguish_Pircture( int page,String imgtype,String state,String Change_user);
	public List<Admuser> showDistinguish_Pircture_2();

	/***
	 * 查询OCR识别错误图片的总数
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	public int queryDistinguish_PirctureCount(String imgtype,String state,String Change_user);

	/***
	 *批发更新线上是否删除状态
	 * @param bgList
	 * @return
	 */
	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList,String userName,int type);
	public int updateSomePirctu_risdelete_s(List<Map<String, String>> maList,String userName);

	public List<Category1688> showCategory1688_type();
}