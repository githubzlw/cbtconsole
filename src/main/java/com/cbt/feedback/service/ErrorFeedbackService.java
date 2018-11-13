package com.cbt.feedback.service;

import com.cbt.feedback.bean.ErrorFeedback;

import java.util.List;
import java.util.Map;

/** 
 * 监控反馈异常接口
 * @author wkk
 *
 */
public interface ErrorFeedbackService {

	/**
	 * 按条件查询反馈信息
	 * @param type       所属模块   
	 * @param belongto   负责人
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @param deFlag     是否已处理标识
	 * @param page       分页
	 * @return
	 */
	List<ErrorFeedback> showErrorFb(int type, String belongto, String startTime, String endTime, int deFlag, int page);

	/**
	 * 异常反馈数据插入
	 * @param content       异常内容
	 * @param createtime    发生时间
	 * @param belongTo      负责人
	 * @param type          模块
	 * @param logLocation   LOG日志文件路径
	 * @param positionFlag  前后台标识
	 */
	int InsertErrorInfo(String content, String createtime, String belongTo, int type, String logLocation, int positionFlag);
	
	
	/**
	 * 更新问题状态并添加相应的备注
	 * @param id
	 * @param remark 
	 * @return
	 */
	int updateErrorFlag(int id, String remark);

	/**
	 * 批量更新状态
	 * @param bgList
	 * @return
	 */
	int updateSomeErrorFlag(List<Map<String, String>> bgList);

	 
	
}
