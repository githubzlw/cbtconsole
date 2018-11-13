package com.cbt.warehouse.ctrl;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.pojo.page.Page;
import com.cbt.util.Utility;
import com.cbt.warehouse.pojo.CustGoodsCommentsBean;
import com.cbt.warehouse.pojo.GoodsCommentsBean;
import com.cbt.warehouse.service.GoodsCommentsService;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台评论入口
 * @ClassName GoodsCommentCtrl 
 * @Description TODO
 * @author Administrator
 * @date 2018年2月1日 下午2:48:39
 */
@Controller
@RequestMapping("/goodsComment")
public class GoodsCommentCtrl {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsCommentCtrl.class);
	@Autowired
	private GoodsCommentsService goodsCommentsService;
	/**
	 * 保存评论
	 * @Title saveComment 
	 * @Description TODO
	 * @param request
	 * @param response
	 * @return void
	 */
	@RequestMapping("savecomment.do")
	@ResponseBody
	public Map<String,Object> saveComment(HttpServletRequest request , HttpServletResponse response){
				Map<String,Object> map = new HashMap<String, Object>();
				try {
					SendMQ sendMQ=new SendMQ();
					DataSourceSelector.set("dataSource127hop");
					GoodsCommentsBean commentsBean = new GoodsCommentsBean();
					String userName = request.getParameter("userName");
					String orderNo = request.getParameter("orderNo");
					int userId = Integer.parseInt(StringUtils.isNotBlank(request.getParameter("adminId"))?request.getParameter("adminId"):null);
					String goodsPid = request.getParameter("goodsPid");
					int countryId = Integer.parseInt(request.getParameter("countryId"));
					String commentsContent  = request.getParameter("commentsContent");
					int adminId = Integer.parseInt(request.getParameter("adminId"));
					commentsBean.setOrderDetailId(StringUtils.isNotBlank(request.getParameter("oid"))?Integer.parseInt(request.getParameter("oid")):0);
					commentsBean.setCarType(request.getParameter("car_type"));
					commentsBean.setUserName(userName);
					commentsBean.setOrderNo(orderNo);
					commentsBean.setUserId(userId);
					commentsBean.setGoodsPid(goodsPid);
					commentsBean.setCountryId(countryId);
					commentsBean.setAdminId(adminId);
					commentsBean.setCommentsContent(commentsContent);
					commentsBean.setShowFlag(1);
					commentsBean.setCommentsTime(new Timestamp(System.currentTimeMillis()));
					//判断页面是否传递过来评论id
					if(StringUtils.isNotBlank(request.getParameter("id"))){
						int id = Integer.parseInt(request.getParameter("id"));
						commentsBean.setId(id);
						goodsCommentsService.updateComments(commentsBean);
						StringBuilder sql=new StringBuilder();
						sql.append("update goods_comments_real set ");
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getOrderDetailId()))){
							sql.append("country_id='"+commentsBean.getCountryId()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getCarType()))){
							sql.append("country_id='"+commentsBean.getCountryId()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getCountryId()))){
							sql.append("country_id='"+commentsBean.getCountryId()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getOrderNo()))){
							sql.append("order_no='"+commentsBean.getOrderNo()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getUserId()))){
							sql.append("user_id='"+commentsBean.getUserId()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getUserName()))){
							sql.append("user_name='"+commentsBean.getUserName()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getGoodsPid()))){
							sql.append("goods_pid='"+commentsBean.getGoodsPid()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getGoodsSource()))){
							sql.append("goods_source='"+commentsBean.getGoodsSource()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getCommentsContent()))){
							sql.append("comments_content='"+commentsBean.getCommentsContent()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getCommentsTime()))){
							sql.append("comments_time='"+commentsBean.getUserName()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getAdminId()))){
							sql.append("admin_id='"+commentsBean.getAdminId()+"',");
						}
						if(StringUtil.isNotBlank(String.valueOf(commentsBean.getShowFlag()))){
							sql.append("show_flag='"+commentsBean.getShowFlag()+"',");
						}
						String sqls=sql.toString().substring(0,sql.toString().length()-1);
						sendMQ.sendMsg(new RunSqlModel(sqls+" where id ='"+commentsBean.getId()+"'"));
						sendMQ.closeConn();
						map.put("success", true);
						map.put("cmid", id);
						return map;
					}
					int comments = goodsCommentsService.insertComments(commentsBean);
					if(comments > 0){
						map.put("success", true);
						map.put("cmid", commentsBean.getId());
						return map;
					}
					sendMQ.closeConn();
				} catch (Exception e) {
					LOG.error("处理评论是出现错误"+e.getMessage());
					map.put("success", false);
					DataSourceSelector.restore();
					return map;
				}finally {
					DataSourceSelector.restore();
				}
				return null;
		
			}
	
		/**
		 * 查询商品下对应的所有的评论
		 * @Title selectAllCommentsByGoodsId 
		 * @Description TODO
		 * @param request
		 * @param response
		 * @return void
		 * 
		 * 产品id,评论账号id,时间,销售(销售人名称),评论者身份(用户,销售)
		 */
		@RequestMapping("selectcomments.do")
		public String selectAllCommentsByGoodsId(HttpServletRequest request , HttpServletResponse response){
				try {
					String goods_pid = request.getParameter("goods_pid");
					String goods_img = request.getParameter("goods_img");
					String goods_url = request.getParameter("goods_url");
					String goodsname = request.getParameter("goodsname");
					String goodsprice = request.getParameter("goodsprice");
					request.setAttribute("goods_img", goods_img);
					request.setAttribute("goods_url", goods_url);
					request.setAttribute("goodsname", goodsname);
					request.setAttribute("goodsprice", goodsprice);
					//查询产品信息
					
					//查询该商品下的所有用户评论
					 List<GoodsCommentsBean> CustCommentsBeans = goodsCommentsService.selectCustomerComments(goods_pid);
					//查询该商品下的所有销售评论
					 List<GoodsCommentsBean> SaleCommentsBean = goodsCommentsService.selectSaleByGoodsPid(goods_pid);
					//查询商品信息	
						request.setAttribute("CustCommentsBeans", CustCommentsBeans);
					    request.setAttribute("SaleCommentsBean", SaleCommentsBean);
				} catch (Exception e) {
					LOG.error("查询评论出现错误,错误信息:");
					e.printStackTrace();
				}
				return "goodsAllComments";
			}
		/**
		 * 查询所有的商品评论,只显示最新的,以及对应的销售评论   没有客户 只有销售 不显示,
		 * @Title selectOneCustomerCommentsOrderByTime 
		 * @Description TODO
		 * @param request
		 * @return
		 * @return String
		 */
		@RequestMapping("queryNewSaleAndCustomerComment.do")
		public String selectNewSaleAndCustomerCommentOrderByTime(HttpServletRequest request){
		
			try {
				String goodsPid = request.getParameter("goodsPid");
				String userId = request.getParameter("commentid");
				String orderid=request.getParameter("orderid");
				String commenttime = request.getParameter("commenttime");
				String commentsale = request.getParameter("commentsale");
				String commentIdentity = request.getParameter("commentIdentity");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				GoodsCommentsBean commentsBean = new GoodsCommentsBean();
				commentsBean.setGoodsPid(goodsPid == "" || goodsPid == null ? null:goodsPid);
				commentsBean.setUserId(userId == "" || userId == null ? null:Integer.parseInt(userId));
				commentsBean.setCommentsTime(commenttime == "" || userId == null ? null : Timestamp.valueOf(dateFormat.format(dateFormat.parse(commenttime))));
				commentsBean.setUserName(commentsale == "" || commentsale == null ? null:commentsale);
				commentsBean.setAdminId(commentIdentity == ""||commentIdentity==null ? null:Integer.parseInt(commentIdentity));
				commentsBean.setOrderNo(StringUtil.isBlank(orderid)?null:orderid);
				int start = Utility.getStringIsNull(request.getParameter("currpage"))? Integer.parseInt(request.getParameter("currpage")):1;
				int pagesize = 20 ;
				
				//查询所有商品的所有客户评论,只取最新的
				Page<CustGoodsCommentsBean> page = goodsCommentsService.selectOneCustomerCommentOrderByTime(commentsBean,start,pagesize);
				//查询该商品下的所有的销售评论,只取最新的
				List<GoodsCommentsBean>  saleCommentOrderByTimes  = goodsCommentsService.selectOneSaleCommentOrderByTimes(commentsBean);
				Map<String,GoodsCommentsBean > maps = new HashMap<String, GoodsCommentsBean>();
				for (GoodsCommentsBean saleGoodsCommentsBean : saleCommentOrderByTimes) {
					maps.put(saleGoodsCommentsBean.getGoodsPid()+"ID", saleGoodsCommentsBean);
				}
				request.setAttribute("page", page);
				request.setAttribute("maps", maps);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return "comments_management";
		}
		/**
		 * 条件更新,评论是否展示 的修改
		 */
		@RequestMapping("updateComments.do")
		@ResponseBody
		public String updateXComments(HttpServletRequest request){
			try {
				SendMQ sendMQ = new SendMQ();
				GoodsCommentsBean commentsBean = new GoodsCommentsBean();
				commentsBean.setId(Integer.parseInt(request.getParameter("id")));
				commentsBean.setShowFlag(Integer.parseInt(request.getParameter("showFlag")));
				int i = goodsCommentsService.updateComments(commentsBean);
				StringBuilder sqls=new StringBuilder();
				sqls.append("update goods_comments_real set ");
				if(commentsBean.getCountryId() != null){
					sqls.append(" country_id = '"+commentsBean.getCountryId()+"',");
				}
				if(commentsBean.getOrderNo() != null){
					sqls.append(" order_no = '"+commentsBean.getOrderNo()+"',");
				}
				if(commentsBean.getUserId() != null){
					sqls.append(" user_id = '"+commentsBean.getUserId()+"',");
				}
				if(commentsBean.getUserName() != null){
					sqls.append(" user_name = '"+commentsBean.getUserName()+"',");
				}
				if(commentsBean.getGoodsPid() != null){
					sqls.append(" goods_pid = '"+commentsBean.getGoodsPid()+"',");
				}
				if(commentsBean.getGoodsSource() != null){
					sqls.append(" goods_source = '"+commentsBean.getGoodsSource()+"',");
				}
				if(commentsBean.getCommentsContent() != null){
					sqls.append(" comments_content = '"+commentsBean.getCommentsContent()+"',");
				}
				if(commentsBean.getCommentsTime() != null){
					sqls.append(" comments_time = '"+commentsBean.getCommentsTime()+"',");
				}
				if(commentsBean.getAdminId()!= null){
					sqls.append(" admin_id = '"+commentsBean.getAdminId()+"',");
				}
				if(String.valueOf(commentsBean.getShowFlag()) != null){
					sqls.append(" show_flag = '"+commentsBean.getShowFlag()+"',");
				}
				if(sqls.indexOf("=")>-1){
					sendMQ.sendMsg(new RunSqlModel(sqls.substring(0,sqls.length()-1)+" where id = '"+commentsBean.getId()+""));
				}
				if(i==1){
					return "success";
				}
				sendMQ.closeConn();
			} catch (Exception e) {
				e.printStackTrace();
			}
				return null;
		}
		
		/**
		 * 发送邮件
		 */
		@RequestMapping("sendMailToCustomer.do")
		@ResponseBody
		public String sendMailToCustomer(HttpServletRequest request){
			try {
				int userId = Integer.parseInt(request.getParameter("custId"));
				String content = request.getParameter("content");
				int result = goodsCommentsService.sendMailToCustomer(userId,content);
				if(result>0){
					return "success";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} 
		/**
		 *  异步加载该订单下的商品是否已经销售评论
		 * @Title searchIsComments 
		 * @Description TODO
		 * @param request
		 * @return
		 * @return List<GoodsCommentsBean>
		 */
		@RequestMapping("searchIsComment")
		@ResponseBody
		public List<GoodsCommentsBean> searchIsComments(HttpServletRequest request){
			String orderNo = request.getParameter("orderNo");
			String goodPids = request.getParameter("goodPids");
			String goodPids1=goodPids.replace("[", "");
			String goodPids2=goodPids1.replace("]", "");
			String[] goods_pids = goodPids2.split(",");
			List<String> listPids = new ArrayList<String>();
			for (String pid : goods_pids) {
				if(!listPids.contains(pid)){
					listPids.add(pid.trim());
				}
			}
			//查询
			Map<String,Object> mapsComments = new HashMap<String,Object>();
			List<GoodsCommentsBean> listComments = goodsCommentsService.searchGoodsCommnetsByList(listPids,orderNo);
			return listComments;
		}
}
