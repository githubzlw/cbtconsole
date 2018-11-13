package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.CustGoodsCommentsBean;
import com.cbt.warehouse.pojo.GoodsCommentsBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCommentsMapper {
	
	/**
	 * 
	 * @Title insertComments 
	 * @Description 插入评论数据
	 * @param goodsComments
	 * @return int
	 */
	public int insertComments(@Param("goodsComments") GoodsCommentsBean goodsComments);

	/**
	 * 根据产品pid查询是否存在销售评论
	 * @Title selectByGoodsPid
	 * @Description TODO
	 * @param goodsid
	 * @return
	 * @return GoodsCommentsBean
	 */
	public GoodsCommentsBean selectByGoodsPidAndOrderNo(@Param("goodpid") String goodpid, @Param("orderNo") String orderNo);
	/**
	 * 根据产品id去查询改产品下所有的用户评论
	 * @Title selectCustomerComments
	 * @Description TODO
	 * @param goodsid
	 * @return
	 * @return List<GoodsCommentsBean>
	 */
	public List<GoodsCommentsBean> selectCustomerComments(String goodsid);
	/**
	 * 查询所有产品的客户最新评论
	 * @param pagesize
	 * @param startindex
	 * @Title selectOneCustomerCommentOrderByTime
	 * @Description TODO
	 * @return
	 * @return List<GoodsCommentsBean>
	 */
	public List<CustGoodsCommentsBean> selectOneCustomerCommentOrderByTime(@Param("commentsBean") GoodsCommentsBean commentsBean, @Param("start") int startindex, @Param("end") int pagesize);

	/**
	 * 取出所有产品中最新的销售评论
	 * @Title selectOneSaleCommentOrderByTimes
	 * @Description TODO
	 * @return
	 * @return List<GoodsCommentsBean>
	 */
	public List<GoodsCommentsBean> selectOneSaleCommentOrderByTimes(@Param("commentsBean") GoodsCommentsBean commentsBean);
	/**
	 * 根据pid查询该对应下的所有的销售评论
	 * @Title selectSaleByGoodsPid
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return List<GoodsCommentsBean>
	 */
	public List<GoodsCommentsBean> selectSaleByGoodsPid(String goods_pid);

	/**
	 * 更新产品,
	 * @Title updateComments
	 * @Description TODO
	 * @param commentsBean
	 * @return
	 * @return int
	 */
	public int updateComments(@Param("commentsBean") GoodsCommentsBean commentsBean);
	/**
	 * 总数
	 * @Title getCustomerCommentOrderByTimeCount
	 * @Description TODO
	 * @param commentsBean
	 * @return
	 * @return int
	 */
	public int getCustomerCommentOrderByTimeCount(@Param("commentsBean") GoodsCommentsBean commentsBean);
	/**
	 * 批量查询
	 * @Title searchGoodsCommnetsByList
	 * @Description TODO
	 * @param listPids
	 * @param orderNo
	 * @return
	 * @return List<GoodsCommentsBean>
	 */
	public List<GoodsCommentsBean> searchGoodsCommnetsByList(
            @Param("listPids") List<String> listPids, @Param("orderNo") String orderNo);

}
