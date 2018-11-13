package com.cbt.feedback.service;

import com.cbt.feedback.bean.Questionnaire;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionnaireService {

	/**
	 * 根据条件查询客户反馈数据
	 * 
	 * @param type
	 *            : 类别
	 * @param userId
	 *            : 用户id
	 * @param userEmail
	 *            : 用户email
	 * @param queryStart
	 *            : 开始位置
	 * @return
	 */
	public List<Questionnaire> queryForList(@Param("type") int type, @Param("userId") int userId,
                                            @Param("userEmail") String userEmail, @Param("queryStart") int queryStart);

	/**
	 * 根据条件查询客户反馈的总数
	 *
	 * @param type
	 * @param userId
	 * @param userEmail
	 * @return
	 */
	public Long queryForCount(@Param("type") int type, @Param("userId") int userId,
                              @Param("userEmail") String userEmail);

	/**
	 * 查询全部数据
	 * 
	 * @return
	 */
	public List<Questionnaire> queryForAllList();

}
