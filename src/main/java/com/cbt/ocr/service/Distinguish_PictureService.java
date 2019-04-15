package com.cbt.ocr.service;

import com.cbt.pojo.Category1688;
import com.cbt.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface Distinguish_PictureService {

	/***
	 * 按条件查询OCR识别错误图片
	 * @param pid
	 * @param
	 * @param
	 * @return
	 *
	 */
	public List<CustomGoods> showDistinguish_Pircture( String pid,  int page,String type);

	/***
	 * 查询OCR识别错误图片的总数
	 * @param pid
	 * @param
	 * @param
	 * @return
	 */
	public int queryDistinguish_PirctureCount(String pid,String type);

	/***
	 *批发更新线上是否删除状态
	 * @param bgList
	 * @return
	 */
	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList,String type,String userName);

	public List<Category1688> showCategory1688_type();
}