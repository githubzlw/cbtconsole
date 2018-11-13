package com.cbt.messages.service;

import com.cbt.pojo.MessageNotification;
import com.cbt.pojo.MessageNotificationStatistical;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageNotificationService {

	/**
	 * 根据adminId查询消息提醒的数据
	 * 
	 * @param adminId:销售和采购的id
	 * @param sendType:消息类别
	 * @return
	 */
	public List<MessageNotification> queryForListByAdminId(Integer adminId, Integer sendType);

	/**
	 * 根据type查询消息提醒的数据
	 * 
	 * @param type:消息提醒类型
	 * @return
	 */
	public List<MessageNotification> queryForListByType(Integer type);

	/**
	 * 根据角色查询对应角色的消息提醒
	 * 
	 * @param roleType:角色类型
	 * @return
	 */
	List<MessageNotification> queryByRoleType(Integer roleType);

	/**
	 * 根据isRead查询消息提醒的数据
	 * 
	 * @param isRead：是否已读，其值只能是Y或者N
	 * @return
	 */
	public List<MessageNotification> queryForListByIsRead(String isRead);

	/**
	 * 根据orderNo查询消息提醒的数据
	 * 
	 * @param orderNo：订单编号
	 * @return
	 */
	public List<MessageNotification> queryForListByOrderNo(String orderNo);

	/**
	 * 根据id查询消息提醒的数据
	 * 
	 * @param id：消息ID
	 * @return
	 */
	public MessageNotification queryById(Integer id);

	/**
	 * 查询消息提醒的数据
	 * 
	 * @param messageNotification
	 */
	public void insertMessageNotification(MessageNotification messageNotification);

	/**
	 * 根据id更新消息已读标识
	 * 
	 * @param id:消息id
	 */
	public void updateIsReadById(Integer id);

	/**
	 * 获取未读取消息数量
	 * 
	 * @date 2017年1月13日
	 * @author abc
	 * @return
	 */
	public int countUnReaded(Integer type);

	/**
	 * 获取消息列表
	 * 
	 * @date 2017年1月13日
	 * @author abc
	 * @param type
	 * @param isread
	 * @return
	 */
	public List<MessageNotification> getMsgList(Integer type, String isread, String orderno);

	/**
	 * 修改出库中已审核订单的消息提醒的创建日期，更新为当前时间
	 */
	public void updateApprovedOrder();

	/**
	 * 根据订单号，消息类别和推送人更新已读
	 * 
	 * @param orderNo
	 * @param type
	 * @param senderId
	 */
	public void updateByOrderNoTypeSenderId(String orderNo, int type, int senderId, int message_id);

	/**
	 * 全部已读UserId所属类别的所有消息提醒数据
	 *
	 * @param userId
	 * @param type
	 */
	public void updateIsReadByUserId(int userId, int type);

	/**
	 * 单独查询Ling的消息提醒数据
	 *
	 * @return
	 */
	public List<MessageNotification> queryForListByLingId();

	/**
	 * 根据类型查询是否存在的消息
	 *
	 * @param orderNo
	 *            订单号
	 * @param senderId
	 *            被发送人id
	 * @param sendType
	 *            发送类型
	 * @param remarkUserId
	 *            消息备注人id
	 * @param sendContent
	 *            消息内容
	 * @return
	 */
	public MessageNotification queryExistsMsgByType(@Param("orderNo") String orderNo, @Param("senderId") int senderId,
                                                    @Param("sendType") int sendType, @Param("remarkUserId") String remarkUserId,
                                                    @Param("sendContent") String sendContent);

	/**
	 * 查询类型为系统消息是否存在的消息
	 *
	 * @param senderId
	 *            被发送人id
	 * @param remarkUserId
	 *            消息备注人id
	 * @param sendContent
	 *            消息内容
	 * @return
	 */
	public MessageNotification queryExistsSysMsg(@Param("senderId") int senderId,
                                                 @Param("remarkUserId") String remarkUserId, @Param("sendContent") String sendContent);

	/**
	 * 删除仓库Eric(id=15),类别出库(type=6)的数据
	 */
	public void deleteBySendIdAndType();

	public int queryAdmuserId(String username);

	/**
	 * 根据接收人id,查询每个类别下的数量
	 * 
	 * @param senderId
	 * @return
	 */
	public List<MessageNotificationStatistical> queryGroupNumber(int senderId);

}
