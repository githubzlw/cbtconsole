package com.cbt.messages.ctrl;

import com.cbt.change.util.StateToChineseUtil;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.MessageNotification;
import com.cbt.util.UniqueIdUtil;
import com.cbt.website.dao.MsgNotificationDao;
import com.cbt.website.dao.MsgNotificationDaoImpl;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息提醒数据插入
 * 
 * @author JiangXianwei
 *
 */
public class InsertMessageNotification {
	private static final Log LOG = LogFactory.getLog(InsertMessageNotification.class);

	/**
	 * 提醒类型1，订单备注时给出的提醒
	 * 
	 * @param sendContent
	 *            消息内容
	 * @param orderRemark
	 *            订单备注信息
	 * @param orderid
	 *            订单号
	 * @param remarkUser
	 *            备注人
	 * @param fromStr
	 *            来源页面
	 */
	public void insertByOrderComment(String sendContent, String orderRemark, String orderid, Admuser remarkUser,
			String fromStr) {
		try {
			int uid=UniqueIdUtil.queryByDbForMessage();
			// 判断是否含有@的数据，进行@处理，数据格式必须要两个@之间或者@的数据一定要正确
			int nameBegin = orderRemark.indexOf("@");
			AdmUserDao admDao = new AdmUserDaoImpl();
			List<Integer> ids = new ArrayList<Integer>();
			if (nameBegin != -1) {
				List<Admuser> allAdmusers = admDao.queryForList();
				// 如果有@，则取出数据
				String[] names = orderRemark.substring(nameBegin + 1).split("@");
				for (String name : names) {
					if (!"".equals(name.trim())) {
						if (name.trim().equals("全体销售")) {
							List<Admuser> sellAdmusers = admDao.queryByRoleType(1);// 0:管理员;1:销售;2:采购;3:仓管
							for (Admuser adm : sellAdmusers) {
								if (!ids.contains(adm.getId())) {
									ids.add(adm.getId());
								}
							}
						} else if (name.trim().equals("全体采购")) {
							List<Admuser> procurementAdmusers = admDao.queryByRoleType(2);
							for (Admuser adm : procurementAdmusers) {
								if (!ids.contains(adm.getId())) {
									ids.add(adm.getId());
								}
							}
						} else {
							for (Admuser adm : allAdmusers) {
								if (adm.getAdmName().toLowerCase().equals(name.trim().toLowerCase())) {
									if (!ids.contains(adm.getId())) {
										ids.add(adm.getId());
									}
									break;
								}
							}
						}
					}
				}
			}

			// 是否存在@的人员ids，存在则进行订单对应人员非系统提醒，其他系统提醒；不存在就全部非系统提醒
			if (ids.size() > 0) {
				// 查找订单对应的销售
				List<Admuser> admusers = admDao.queryByOrderNo(orderid);
				//给当前订单的销售发消息
				if (admusers.size() > 0) {
					for (Admuser admuser : admusers) {
						if (ids.contains(admuser.getId())) {
							ids.remove(admuser.getId());
						}
						insertMessageInsertByType(orderid, sendContent + ":" + orderRemark, admuser.getId(), 1,
								remarkUser.getId(),uid);
					}
				}
				//给@的人员发消息
				for (Integer senderId : ids) {
					//insertSystemMessage(remarkUser, orderid, orderRemark, senderId, fromStr);
					insertMessageInsertByType(orderid, sendContent + ":" + orderRemark, senderId, 1,
							remarkUser.getId(),uid);
				}
			} else {
				// 查找订单对应的销售
				List<Admuser> admusers = admDao.queryByOrderNo(orderid);
				if (admusers.size() > 0) {
					for (Admuser admuser : admusers) {
						insertMessageInsertByType(orderid, sendContent + ":" + orderRemark, admuser.getId(), 1,
								remarkUser.getId(),uid);
					}
				}
			}
			// 单独给ling发送同样的信息
			//暂时不推送给ling,她的消息数量太多了
			//insertMessageInsertByType(orderid, sendContent + ":" + orderRemark, 1, 1, remarkUser.getId());

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("insertByOrderComment error:" + e.getMessage());
		}

	}

	/**
	 * 提醒类型2，订单状态修改时给出的提醒
	 * 
	 * @param orderid
	 *            订单号
	 * @param remarkUser
	 *            备注人信息
	 * @param state
	 *            订单状态
	 */
	public void insertOrderStatusChanges(String orderid, Admuser remarkUser, int state) {
		try {
			int uid=UniqueIdUtil.queryByDbForMessage();
			String sendContent = remarkUser.getAdmName() + "修改订单状态为:["
					+ StateToChineseUtil.orderInfoState(String.valueOf(state)) + "]";
			AdmUserDao admDao = new AdmUserDaoImpl();
			List<Admuser> admusers = admDao.queryByOrderNo(orderid);
			for (Admuser admuser : admusers) {
				if (admuser != null) {
					insertMessageInsertByType(orderid, sendContent, admuser.getId(), 2, remarkUser.getId(),uid);
				}
			}
			// 单独给ling发送同样的信息
			insertMessageInsertByType(orderid, sendContent, 1, 2, remarkUser.getId(),uid);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("insertOrderStatusChanges error:" + e.getMessage());
		}

	}

	/**
	 * 提醒类型6，已审核未出库时给出的提醒
	 * 
	 * @param count
	 */
	public void insertCheckedWithoutOutbound(int count) {
		try {
			MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
			MsgNtftDao.deleteBySendIdAndType();// 清理原来的数据
			MsgNtftDao.insertBySendIdAndType(count);// 新增数据

			/*
			 * messageNotificationDao.deleteBySendIdAndType();
			 * MessageNotification nwMsgNtf = new MessageNotification();
			 * nwMsgNtf.setCreateTime(new
			 * Timestamp(System.currentTimeMillis())); nwMsgNtf.setIsRead("N");
			 * nwMsgNtf.setSendContent("有" + count + " 个订单已审核，待出库");
			 * nwMsgNtf.setSenderId(15);// 消息发送人 nwMsgNtf.setSendType(6);
			 * messageNotificationDao.insertMessageNotification(nwMsgNtf);
			 * 
			 * nwMsgNtf.setSenderId(1);
			 * messageNotificationDao.insertMessageNotification(nwMsgNtf);
			 */

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("insertCheckedWithoutOutbound error:" + e.getMessage());
		}

	}

	public int queryAdmuserId(String username) {
		MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
		return MsgNtftDao.queryAdmuserId(username);
	}

	public int getInfoSendId(String id) {
		MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
		return MsgNtftDao.getInfoSendId(id);
	}
	
	public void updateTbStatus(String shipno){
		MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
		MsgNtftDao.updateTbStatus(shipno);
	}

	public String getIdrealtionInfo(String tbshipno){
		StringBuilder sb=new StringBuilder();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql="SELECT DISTINCT orderid,barcode FROM id_relationtable WHERE tborderid IN " +
					"(SELECT DISTINCT orderid FROM taobao_1688_order_history WHERE shipno='"+tbshipno+"') AND is_delete=0 AND is_replenishment=1";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				sb.append(rs.getString("orderid")).append("@").append(rs.getString("barcode")).append("&");
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return sb.toString().length()>0?sb.toString().substring(0,sb.toString().length()-1):"";
	}

	public int insertTreasuryNoteFor(String tbOrderid, String admuserid, String remarkUserId, String buyUrl,
			String text) {
		int row = 0;
		MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
		try {
			if (admuserid != null) {
				MessageNotification nwMsgNtf = new MessageNotification();
				nwMsgNtf.setCreateTime(new Timestamp(System.currentTimeMillis()));
				nwMsgNtf.setIsRead("N");
				nwMsgNtf.setSendContent(text);
				nwMsgNtf.setSenderId(Integer.valueOf(remarkUserId));// 消息发送人
				nwMsgNtf.setSendType(9);
				nwMsgNtf.setLinkUrl(tbOrderid);
				nwMsgNtf.setOrderNo(tbOrderid);
				nwMsgNtf.setReservation1(admuserid);
				nwMsgNtf.setMessage_id(UniqueIdUtil.queryByDbForMessage());
				//发给对应的采购人
				MsgNtftDao.insertMessageNotification(nwMsgNtf);
				//发给ling
				nwMsgNtf.setSenderId(1);
				MsgNtftDao.insertMessageNotification(nwMsgNtf);
				row = 1;
			}

		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("insertTreasuryNoteFor error :" + e.getMessage());
		}
		return row;
	}

	/**
	 * 提醒类型7,入库备注时给出提醒
	 * 
	 * @param orderid
	 *            订单号
	 * @param goodid
	 *            商品id
	 * @param goodstatus
	 *            商品状态
	 * @param remarkUserId
	 *            备注人id
	 * @param remarkUserName
	 *            备注人名称
	 */
	public void insertTreasuryNote(String orderid, int goodid, int goodstatus, int remarkUserId,
			String remarkUserName) {
		AdmUserDao admDao = new AdmUserDaoImpl();
		String stateStr = "";
		if (goodstatus == 0) {
			stateStr = "验货无误";
		} else if (goodstatus == 1) {
			stateStr = "到货了";
		} else if (goodstatus == 2) {
			stateStr = "该货没到";
		} else if (goodstatus == 3) {
			stateStr = "破损";
		} else if (goodstatus == 4) {
			stateStr = "有疑问";
		} else if (goodstatus == 5) {
			stateStr = "数量不够";
		}

		try {
			// 查找订单商品对应的采购
			Admuser admuser = admDao.queryByOrderNoAndGoodid(orderid, goodid);
			// 查找订单商品对应的销售
			Admuser sales = admDao.querySalesByOrderNoAndGoodid(orderid, goodid);
			int uid=UniqueIdUtil.queryByDbForMessage();
			if (admuser != null) {
				String sendContent = "验货入库," + remarkUserName + "对订单商品[" + goodid + "]入库状态修改为:“" + stateStr + "”";
				insertMessageInsertByType(orderid, sendContent, admuser.getId(), 7, remarkUserId,uid);
				// 单独给ling发送同样的信息
				insertMessageInsertByType(orderid, sendContent, 1, 7, remarkUserId,uid);
				// 给对应的销售发送同样的信息
				insertMessageInsertByType(orderid, sendContent, sales.getId(), 7, remarkUserId,uid);
			}

		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("insertTreasuryNote error:" + e.getMessage());
		}

	}

	/**
	 * 提醒类型0,订单状态更改出错时给出提醒
	 * 
	 * @param orderid
	 * @param adminId
	 * @param remarkContent
	 */
	public void orderChangeError(String orderid, int adminId, String remarkContent) throws Exception{
		MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
		try {
			MessageNotification nwMsgNtf = new MessageNotification();
			//nwMsgNtf.setOrderNo(orderid);
			nwMsgNtf.setLinkUrl(orderid);
			nwMsgNtf.setCreateTime(new Timestamp(System.currentTimeMillis()));
			nwMsgNtf.setIsRead("N");
			nwMsgNtf.setSendContent(remarkContent);
			nwMsgNtf.setSenderId(adminId);
			nwMsgNtf.setSendType(0);
			nwMsgNtf.setReservation1(String.valueOf(adminId));
			nwMsgNtf.setMessage_id(UniqueIdUtil.queryByDbForMessage());
			MsgNtftDao.insertMessageNotification(nwMsgNtf);
			// messageNotificationDao.insertMessageNotification(nwMsgNtf);

			// 发给ling
			if(adminId != 1){
				nwMsgNtf.setSenderId(1);
				MsgNtftDao.insertMessageNotification(nwMsgNtf);
			}
			
		} catch (Exception e) {
			LOG.error("插入消息提醒失败,订单号:" + orderid + ",原因:" + e.getMessage());
			throw new Exception(e);
		}

	}

	// 插入非系统消息的消息提醒数据
	public void insertMessageInsertByType(String orderid, String sendContent, int senderId, int type, int remarkUserId,int uid)
			throws Exception {
		MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
		if (remarkUserId != 0) {
			MessageNotification msgNtf = MsgNtftDao.queryExistsMsgByType(orderid, sendContent, senderId, type,
					remarkUserId);
			// 判断订单备注是否存在，存在未读不进行插入，存在已读进行插入,不存在直接插入
			if (msgNtf.getId() != 0) {
				if ("Y".equals(msgNtf.getIsRead())) {
					MessageNotification nwMsgNtf = new MessageNotification();
					nwMsgNtf.setCreateTime(new Timestamp(System.currentTimeMillis()));
					nwMsgNtf.setIsRead("N");
					nwMsgNtf.setOrderNo(orderid);
					nwMsgNtf.setLinkUrl(orderid);
					nwMsgNtf.setSendContent(sendContent);
					nwMsgNtf.setSenderId(senderId);
					nwMsgNtf.setSendType(type);
					nwMsgNtf.setReservation1(String.valueOf(remarkUserId));
					nwMsgNtf.setMessage_id(uid);
					MsgNtftDao.insertMessageNotification(nwMsgNtf);
					// messageNotificationDao.insertMessageNotification(nwMsgNtf);
				}
			} else {
				MessageNotification nwMsgNtf = new MessageNotification();
				nwMsgNtf.setCreateTime(new Timestamp(System.currentTimeMillis()));
				nwMsgNtf.setIsRead("N");
				nwMsgNtf.setOrderNo(orderid);
				nwMsgNtf.setLinkUrl(orderid);
				nwMsgNtf.setSendContent(sendContent);
				nwMsgNtf.setSenderId(senderId);
				nwMsgNtf.setSendType(type);
				nwMsgNtf.setReservation1(String.valueOf(remarkUserId));
				nwMsgNtf.setMessage_id(uid);
				MsgNtftDao.insertMessageNotification(nwMsgNtf);
				// messageNotificationDao.insertMessageNotification(nwMsgNtf);
			}
		}

	}

	// 插入系统消息的消息提醒数据
	private void insertSystemMessage(Admuser remarkUser, String orderid, String orderRemark, int senderId,
                                     String fromStr) throws Exception {
		MsgNotificationDao MsgNtftDao = new MsgNotificationDaoImpl();
		int uid=UniqueIdUtil.queryByDbForMessage();
		if (remarkUser.getId() != 0) {
			MessageNotification msgNtf = MsgNtftDao.queryExistsSysMsg(senderId, orderRemark, remarkUser.getId());
			// 判断订单备注是否存在，存在未读不进行插入，存在已读进行插入,不存在直接插入
			if (msgNtf.getId() != 0) {
				if ("Y".equals(msgNtf.getIsRead())) {
					MessageNotification nwMsgNtf = new MessageNotification();
					nwMsgNtf.setCreateTime(new Timestamp(System.currentTimeMillis()));
					nwMsgNtf.setIsRead("N");
					nwMsgNtf.setSendContent(
							fromStr + remarkUser.getAdmName() + "备注了订单[" + orderid + "]: " + orderRemark);
					nwMsgNtf.setSenderId(senderId);
					nwMsgNtf.setSendType(0);
					nwMsgNtf.setReservation1(remarkUser.getId().toString());
					nwMsgNtf.setMessage_id(uid);
					MsgNtftDao.insertMessageNotification(nwMsgNtf);
					// messageNotificationDao.insertMessageNotification(nwMsgNtf);
				}
			} else {
				MessageNotification nwMsgNtf = new MessageNotification();
				nwMsgNtf.setCreateTime(new Timestamp(System.currentTimeMillis()));
				nwMsgNtf.setIsRead("N");
				nwMsgNtf.setSendContent(fromStr + remarkUser.getAdmName() + "备注了订单[" + orderid + "]: " + orderRemark);
				nwMsgNtf.setSenderId(senderId);
				nwMsgNtf.setSendType(0);
				nwMsgNtf.setReservation1(remarkUser.getId().toString());
				nwMsgNtf.setMessage_id(uid);
				MsgNtftDao.insertMessageNotification(nwMsgNtf);
				// messageNotificationDao.insertMessageNotification(nwMsgNtf);
			}
		}

	}

}
