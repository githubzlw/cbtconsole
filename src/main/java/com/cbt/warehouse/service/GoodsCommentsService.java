package com.cbt.warehouse.service;

import com.cbt.pojo.page.Page;
import com.cbt.warehouse.pojo.CustGoodsCommentsBean;
import com.cbt.warehouse.pojo.GoodsCommentsBean;

import java.util.List;

public interface GoodsCommentsService {
	
	/**
	 * 
	 * @Title insertComments 
	 * @Description 插入评论数据
	 * @param goodsComments
	 * @return int
	 */
	public int insertComments(GoodsCommentsBean goodsComments);
	
	/**
	 * 根据产品的id去查询是否存在销售评论 一个订单下的该产品只能对应一个销售评论
	 * @Title selectByAdminId 
	 * @Description TODO
	 * @param goodsid
	 * @return
	 * @return GoodsCommentsBean
	 */
	public GoodsCommentsBean selectByGoodsPidAndOrderNo(String goodsid, String orderNo);
	/**
	 * 查询该商品来自所有客户的评论
	 * @Title selectCustomerComments 
	 * @Description TODO
	 * @param goodsid
	 * @return
	 * @return List<GoodsCommentsBean>
	 */
	public List<GoodsCommentsBean> selectCustomerComments(String goodsid);
	/**
	 * 查询所有的评论,在评论管理首页显示,每个产品取最新的客户评论
	 */
	
	public Page<CustGoodsCommentsBean> selectOneCustomerCommentOrderByTime(GoodsCommentsBean commentsBean, int start, int pagesize);
	/**
	 * 根据指定的产品pid去查询改产品下对应的所有的销售评论
	 * @Title selectByGoodsPid
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return GoodsCommentsBean
	 */
	public List<GoodsCommentsBean> selectSaleByGoodsPid(String goods_pid);
	/**
	 * 根据pid去查询该产品查所有的销售产品中最新的
	 * @Title selectOneSaleCommentOrderByTimes
	 * @Description TODO
	 * @param goodsPid
	 * @return
	 * @return GoodsCommentsBean
	 */
	public List<GoodsCommentsBean> selectOneSaleCommentOrderByTimes(GoodsCommentsBean commentsBean);
	/**
	 * 更新商品
	 * @Title updateComments
	 * @Description TODO
	 * @param commentsBean
	 * @return void
	 */
	public int updateComments(GoodsCommentsBean commentsBean);
	/**
	 * 邮件的
	 * @Title sendMailToCustomer
	 * @Description TODO
	 * @param userId
	 * @param content
	 * @return
	 * @return int
	 */
	public int sendMailToCustomer(int userId, String content);
	/**
	 * 批量查询同一订单号下的商品
	 * @Title searchGoodsCommnetsByList
	 * @Description TODO
	 * @param listPids
	 * @param orderNo
	 * @return
	 * @return List<GoodsCommentsBean>
	 */
	public List<GoodsCommentsBean> searchGoodsCommnetsByList(
            List<String> listPids, String orderNo);


	 

}
