package com.importExpress.mapper;

import com.importExpress.pojo.QueAns;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QueAnsMapper {
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
	public List<QueAns> findByQuery(@Param("goodsPid") String goodsPid, @Param("goodsName") String goodsName,
                                    @Param("adminId") int adminId, @Param("replyFlag") int replyFlag, @Param("startdate") String startdate,
                                    @Param("enddate") String enddate, @Param("page") int page);
	/**
	 * 获取产品单页提问信息
	 * @Title getQueAnsinfo
	 * @Description TODO
	 * @param qid
	 * @return
	 * @return QueAns
	 */
	public QueAns getQueAnsinfo(@Param("qid") int qid);
	/**
	 *产品单页问答删除操作
	 * @param qid
	 * @return
	 */
	public int deleteQuestion(@Param("qid") int qid);
	/**
	 * 该条问答影响同店铺下其他同类别产品
	 * @param qid
	 * @param shop_id
	 * @return
	 */
	public int influenceShop(@Param("qid") int qid, @Param("shop_id") String shop_id, @Param("state") String state);
	/**
	 * 变更提问回复是否在产品单页显示
	 * @Title changeIsShow
	 * @Description TODO
	 * @param pid
	 * @param type
	 * @return
	 * @return int
	 */
	public int changeIsShow(@Param("pid") String pid, @Param("type") String type);
	/**根据查询条件获总数
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
	public int getCountByQuery(@Param("goodsPid") String goodsPid, @Param("goodsName") String goodsName,
                               @Param("adminId") int adminId, @Param("replyFlag") int replyFlag, @Param("startdate") String startdate,
                               @Param("enddate") String enddate);

	/**更新回复内容
	 * @date 2018年3月26日
	 * @author user4
	 * @param qid 问题编号
	 * @param adminId 回复人id
	 * @param rContent 回复内容
	 * @return
	 */
	public int updateReplyContent(@Param("qid") int qid, @Param("adminId") int adminId, @Param("rContent") String rContent, @Param("isShow") String isShow, @Param("shop_id") String shop_id);

	/**更新回复内容
	 * @date 2018年3月26日
	 * @author user4
	 * @param qid 问题编号
	 * @param adminId 回复人id
	 * @param isReview 审核状态
	 * @param reviewRemark 审核备注
	 * @return
	 */
	public int updateRemark(@Param("qid") int qid, @Param("adminId") int adminId, @Param("isReview") int isReview, @Param("reviewRemark") String reviewRemark);
	
	
//    int deleteByPrimaryKey(Integer questionid);
//
//    int insert(QueAns record);
//
//    int insertSelective(QueAns record);
//
//    QueAns selectByPrimaryKey(Integer questionid);
//
//    int updateByPrimaryKeySelective(QueAns record);
//
//    int updateByPrimaryKey(QueAns queAnw);
//
//    //根据条件查询提问list
//	@SuppressWarnings("rawtypes")
//	List findAllQueAnwByQuery(QueAnsQuery queAnsQuery);
//	//查询总条数
//	Integer queAnwByQuerytotal(QueAnsQuery queAnsQuery);
//	
//	 void updateAll(Integer[] ids) throws Exception;
//
//	 void deleteAll(Integer[] ids) throws Exception;
}