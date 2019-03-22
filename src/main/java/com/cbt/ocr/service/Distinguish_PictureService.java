package com.cbt.ocr.service;

import com.cbt.pojo.CustomGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface Distinguish_PictureService {

	/***
	 * 按条件查询OCR识别错误图片
	 * @param pid
	 * @param shopid
	 * @param isdelete
	 * @return
	 *
	 */
	public List<CustomGoods> showDistinguish_Pircture(@Param("pid") String pid, @Param("shopid") String shopid, @Param("isdelete") int isdelete, @Param("page") int page);

	/***
	 * 查询OCR识别错误图片的总数
	 * @param pid
	 * @param shopid
	 * @param isdelete
	 * @return
	 */
	public int queryDistinguish_PirctureCount(@Param("pid")String pid,@Param("shopid")String shopid,@Param("isdelete")int isdelete);

	/***
	 *  单条更新线上是否删除状态
	 * @param id
	 * @param ocrneeddelete
	 * @return
	 */
	public int updatePirctu_risdelete(@Param("id")int id,@Param("ocrneeddelete")int ocrneeddelete);

	/***
	 *批发更新线上是否删除状态
	 * @param bgList
	 * @return
	 */
	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList);
}