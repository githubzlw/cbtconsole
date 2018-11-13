package com.cbt.redNet.service;

import com.cbt.redNet.pojo.redNet;
import com.cbt.redNet.pojo.redNetStatistics;

import java.util.List;

public interface RedNetService {

	/**
	 * 查询网红统计报表
	 * @param redNetId
	 * @param pushTime
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<redNetStatistics> showRedNetStatistics(String redNetId, String pushTime, String startTime, String endTime, int page);

	/**
	 * 查询网红
	 * @param redNetId
	 * @param redNetName
	 * @param site
	 * @param page
	 * @return
	 */
	List<redNet> showRedNetInfo(String redNetId, String redNetName, String site, int page);

	
	List<redNet> showAllRedNet();

	/**
	 * 保存bean
	 * @param bean
	 * @return
	 */
	int addRedNet(redNet bean);

	/**
	 * 根据id 查询网红信息
	 * @param parseInt
	 * @return
	 */
	redNet showRedNetById(int parseInt);

	/**
	 * 分享shareId 给网红
	 * @param bean
	 * @return
	 */
	int addRedNetStatistics(redNetStatistics bean);

	/**
	 * 根据网红name 判断唯一性
	 * @param redNetName
	 * @return
	 */
	int selectRedNetByName(String redNetName);

	/**
	 * 根据shareId 判断唯一性
	 * @param shareId
	 * @return
	 */
	int selectRedNetStatistics(String shareId);

	int updateRedNet(redNet bean);

	redNetStatistics showRedNetStatisticsById(int id);

	int updateRedNetStatistics(redNetStatistics bean);

}
