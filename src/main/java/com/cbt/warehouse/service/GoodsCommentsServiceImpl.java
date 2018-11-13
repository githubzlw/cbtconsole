package com.cbt.warehouse.service;

import com.cbt.pojo.page.Page;
import com.cbt.processes.service.SendEmail;
import com.cbt.warehouse.dao.GoodsCommentsMapper;
import com.cbt.warehouse.pojo.CustGoodsCommentsBean;
import com.cbt.warehouse.pojo.GoodsCommentsBean;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品的评论
 * @ClassName GoodsCommentsServiceImpl 
 * @Description TODO
 * @author Administrator
 * @date 2018年1月23日 下午3:51:48
 */
@Service
public class GoodsCommentsServiceImpl implements GoodsCommentsService {
	
	@Autowired
	private GoodsCommentsMapper goodsCommentsMapper;
	//新增一条新的评论
	@Override
	public int insertComments(GoodsCommentsBean goodsComments) {
		return goodsCommentsMapper.insertComments(goodsComments);
	}
	
	//根据产品pid去查对应的销售评论
	@Override
	public GoodsCommentsBean selectByGoodsPidAndOrderNo(String goodsid, String orderNo) {
		return goodsCommentsMapper.selectByGoodsPidAndOrderNo(goodsid,orderNo);
	}
	//根据产品pid查询对应的所有的客户评论
	@Override
	public List<GoodsCommentsBean> selectCustomerComments(String goodsid) {
		return goodsCommentsMapper.selectCustomerComments(goodsid);
	}
	//取出每个产品最新的客户评论
	@Override
	public Page<CustGoodsCommentsBean> selectOneCustomerCommentOrderByTime(GoodsCommentsBean commentsBean, int start, int pagesize) {
		//分页

		Page<CustGoodsCommentsBean> page = new Page<CustGoodsCommentsBean>();
		int startindex = (start-1)*pagesize;//起始位置
		List<CustGoodsCommentsBean> customerCommentOrderByTime = goodsCommentsMapper.selectOneCustomerCommentOrderByTime(commentsBean,startindex,pagesize);
		for(int i=0;i<customerCommentOrderByTime.size();i++){
			String flag=String.valueOf(customerCommentOrderByTime.get(i).getShowFlag());
			if(StringUtil.isBlank(flag)){
				customerCommentOrderByTime.get(i).setShowFlag(0);
			}
		}
		//查询总数
		int total = goodsCommentsMapper.getCustomerCommentOrderByTimeCount(commentsBean);
		double countPage = Math.ceil(total*1.0/pagesize);
		//封装数据
		page.setCurrentPage(start);
		page.setCountPage((int)countPage);//总页数
		page.setCountRecord(total);//总数
		page.setOnePageCount(pagesize);
		page.setList(customerCommentOrderByTime);
		return page;
	}
	//查询改商品下对应的所有的销售评论
	@Override
	public List<GoodsCommentsBean> selectSaleByGoodsPid(String goods_pid) {
		return goodsCommentsMapper.selectSaleByGoodsPid(goods_pid);
	}
	//取出每个产品最新的客户评论
	@Override
	public List<GoodsCommentsBean> selectOneSaleCommentOrderByTimes(GoodsCommentsBean commentsBean) {
		return goodsCommentsMapper.selectOneSaleCommentOrderByTimes(commentsBean);
	}
	//更新产品
	@Override
	public int updateComments(GoodsCommentsBean commentsBean) {
		return goodsCommentsMapper.updateComments(commentsBean);
		
	}

	@Override
	public int sendMailToCustomer(int userId, String content) {
		UserDao userDao = new UserDaoImpl();
		String email = userDao.getUserEmailByUserName(null, userId);
		//String email="522290631@qq.com";
		int result = SendEmail.send(null, null, email, content, "Mail from the Import Express", null, null, 1);
		return result;
	}

	@Override
	public List<GoodsCommentsBean> searchGoodsCommnetsByList(
			List<String> listPids, String orderNo) {
		return goodsCommentsMapper.searchGoodsCommnetsByList(listPids,orderNo);
	}


}
