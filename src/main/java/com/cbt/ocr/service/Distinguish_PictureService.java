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
	 * @param  page  列表页数
	 * @param  imgtype  产品类型
	 * @param  state    状态位   空格-未处理  1-已处理（含中文字图）  2-已处理（不含中文字图）
	 * @param Change_user   操作用户
	 * @return  md5数据集合
	 *
	 */
	public List<CustomGoods> showDistinguish_Pircture( int page,String imgtype,String state,String Change_user);


	/***
	 * 查询运营人员  包括人员类型 4 5 类型
	 * @return
	 */
	public List<Admuser> showDistinguish_Pircture_operationUser();

	/***
	 * 查询OCR识别错误图片的总数
	 * @param imgtype  产品类型
	 * @param  state  状态位  空格-未处理  1-已处理（含中文字图）  2-已处理（不含中文字图）
	 * @param Change_user  操作用户
	 * @return   图片的总数
	 */
	public int queryDistinguish_PirctureCount(String imgtype,String state,String Change_user);

	/***
	 *批发更新线上是否删除状态
	 * @param bgList    传送删除的数据集合
	 * @param userName 操作用户
	 * @param type 操作类型  空格-未处理  1-已处理（含中文字图）  2-已处理（不含中文字图）
	 * @return  返回删除成功状态
	 */
	public int updateSomePirctu_risdelete(List<Map<String, String>> bgList,String userName,int type);
	public int updateSomePirctu_risdelete_s(List<Map<String, String>> maList,String userName);

	/***
	 * 查询ocr根据产品类型分类及统计
	 * @return
	 */
	public List<Category1688> showCategory1688_type();

	/***
	 * 查询所有ocr已经下架图片记录
	 * @param map  分页数据和查询条件
	 * @return  图片下架详情数据集合
	 */
	public List<CustomGoods> FindRecognition_delete_details(Map<String, Object> map);

	/***
	 *  更新线上下架的图片状态位为1
	 * @param bgList
	 * @return
	 */
	public int updateSomePirctu_risdelete_date(List<Map<String, String>> bgList);

	/***
	 * 查看下架图片的总数据
	 * @param map
	 * @return
	 */
	public int FindRecognition_delete_count(Map<String, Object> map);
}