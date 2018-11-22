package com.importExpress.service;

import com.importExpress.pojo.QueAns;

import java.util.List;


public interface QuestionAndAnswerService {
	
	/**根据查询条件获得列表
	 * @date 2018年3月23日
	 * @author user4
	 * @param goodsPid 产品id
	 * @param goodsName 产品名称
	 * @param Admin_id 回复人
	 * @param replyFlag 回复状态
	 * @param startdate 提问题时间
	 * @param enddate 提问题时间
	 * @return  
	 */
	public List<QueAns> findByQuery(String goodsPid, String goodsName, int adminId, int replyFlag, int replyStatus, String startdate, String enddate, int page);
	/**根据查询条件获得总数
	 * @date 2018年3月23日
	 * @author user4
	 * @param goodsPid 产品id
	 * @param goodsName 产品名称
	 * @param Admin_id 回复人
	 * @param replyFlag 回复状态
	 * @param startdate 提问题时间
	 * @param enddate 提问题时间
	 * @return
	 */
	public int getCountByQuery(String goodsPid, String goodsName, int adminId, int replyFlag, int replyStatus, String startdate, String enddate);
	/**更新回复内容
	 * @date 2018年3月26日
	 * @author user4
	 * @param qid 问题编号
	 * @param adminId 回复人id
	 * @param rContent 回复内容
	 * @return
	 */
	public int updateReplyContent(int qid, int adminId, String rContent, String isShow, String shop_id);
	/**
	 * 该条问答影响同店铺下其他同类别产品
	 * @param qid
	 * @param shop_id
	 * @return
	 */
	public int influenceShop(int qid, String shop_id, String state);
	/**
	 * 获取产品单页提问信息
	 * @Title getQueAnsinfo
	 * @Description TODO
	 * @param qid
	 * @return
	 * @return QueAns
	 */
	public QueAns getQueAnsinfo(int qid);

	/**
	 *产品单页问答删除操作
	 * @param pid
	 * @return
	 */
	public int deleteQuestion(int pid);
	/**
	 * 变更提问回复是否在产品单页显示
	 * @Title changeIsShow
	 * @Description TODO
	 * @param pid
	 * @param type
	 * @return
	 * @return int
	 */
	public int changeIsShow(String pid, String type);
	/**更新回复内容
	 * @date 2018年3月26日
	 * @author user4
	 * @param qid 问题编号
	 * @param adminId 回复人id
	 * @param isReview 审核状态
	 * @param reviewRemark 审核备注
	 * @return
	 */
	public int updateRemark(int qid, int adminId, int isReview, String reviewRemark);
	
	
	
	

//	 @SuppressWarnings("rawtypes")
//	public List findAllQueAnwByQuery(String pid,String pname,String replyName,Integer replyStatus,
//			Integer isshow,String startdate,String enddate,int startRow, int pageSize);
//	 public Integer queAnwByQuerytotal(String pid,String pname,String replyName,Integer replyStatus,
//			 Integer isshow,String startdate,String enddate);
//	 public QueAns selectByPrimaryKey(Integer questionid);
//	
//	 int updateByPrimaryKey(QueAns queAnw);
//	 
//	 public void updateAll(Integer[] ids) throws Exception;
//	 public void deleteAll(Integer[] ids) throws Exception;
}
