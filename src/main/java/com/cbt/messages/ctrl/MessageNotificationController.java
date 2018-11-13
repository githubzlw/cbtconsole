package com.cbt.messages.ctrl;

import com.cbt.messages.service.MessageNotificationService;
import com.cbt.pojo.MessageNotification;
import com.cbt.pojo.MessageNotificationStatistical;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.website.util.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/messageNotification")
public class MessageNotificationController {
	private static final Log LOG = LogFactory.getLog(MessageNotificationController.class);

	@Resource
	private MessageNotificationService messageNotificationService;
	@Autowired
	private IWarehouseService iWarehouseService;

	/**
	 * 根据用户的id查询消息提醒数据
	 */
	@RequestMapping(value = "/queryByAdminId")
	@ResponseBody
	public JsonResult queryByAdminId(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		List<MessageNotification> messageNotifications = new ArrayList<MessageNotification>();
		try {
			String adminId = request.getParameter("adminId");
			if (adminId == null || "".equals(adminId)) {
				json.setOk(false);
				json.setMessage("获取adminId失败！");
				json.setTotal(Long.valueOf(0));
				return json;
			}

			String sendType = request.getParameter("sendType");
			if (sendType == null || "".equals(sendType)) {
				json.setOk(false);
				json.setMessage("获取sendType失败！");
				json.setTotal(Long.valueOf(0));
				return json;
			}
            //查询所有未读的消息和最近三天已读的消息
			messageNotifications = messageNotificationService.queryForListByAdminId(Integer.valueOf(adminId),Integer.valueOf(sendType));
			List<MessageNotification> rList=new ArrayList<MessageNotification>();
			if("9".equals(sendType)){
				for (MessageNotification m : messageNotifications) {
					//{username=, page=0, data=null, shipno=70045004543062, type=null}
					Map<Object, Object> map = new HashMap<Object, Object>();
					map.put("page", 0);
					map.put("data", null);
					map.put("username", "");
					map.put("type", null);
					map.put("shipno", m.getOrderNo());
					List<TaoBaoOrderInfo> orders = iWarehouseService.getPurchaseOrderDetails(map);
					if(orders.size()<=0){
						rList.add(m);
					}
				}
			}
			//删除已经入库的运单信息
            for (MessageNotification m : rList) {
            	messageNotifications.remove(m);
			}
			int total = 0;
			for (MessageNotification sNotification : messageNotifications) {
				if (!(sNotification.getOrderNo() == null || "".equals(sNotification.getOrderNo()))) {
					sNotification.setLinkUrl(sNotification.getOrderNo());
				}
				if (!sNotification.getIsRead().equals("Y")) {
					total++;
				}
			}

			json.setOk(true);
			json.setData(messageNotifications);
			json.setTotal(Long.valueOf(total));
			return json;

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			json.setTotal(Long.valueOf(0));
			return json;
		}

	}
	

	/**
	 * 根据用户的id查询消息每个类别的数据
	 */
	@RequestMapping(value = "/queryStatistical")
	@ResponseBody
	public JsonResult queryStatisticalByAdminId(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		List<MessageNotificationStatistical> statisticals = new ArrayList<MessageNotificationStatistical>();
		try {
			String adminId = request.getParameter("adminId");
			if (adminId == null || "".equals(adminId)) {
				json.setOk(false);
				json.setMessage("获取adminId失败！");
				json.setTotal(Long.valueOf(0));
				return json;
			}
            //更新type为9的的消息
			List<String> list=iWarehouseService.getInfos(Integer.valueOf(adminId));
			for (String s : list) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("page", 0);
				map.put("data", null);
				map.put("username", "");
				map.put("type", null);
				map.put("shipno", s);
				List<TaoBaoOrderInfo> orders = iWarehouseService.getPurchaseOrderDetails(map);
				if(orders.size()<=0){
					iWarehouseService.updateIsRead(s);
				}
			}
			statisticals = messageNotificationService.queryGroupNumber(Integer.valueOf(adminId));
			//屏蔽类型是3、4、6、8的统计数据
			for (MessageNotificationStatistical sts : statisticals) {
				if (sts.getSendType() == 3 || sts.getSendType() == 4 || sts.getSendType() == 64 || sts.getSendType() == 8) {
					sts.setCount(0);
				}
			}
			json.setOk(true);
			json.setData(statisticals);
			return json;

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("queryStatisticalByAdminId查询失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			json.setTotal(Long.valueOf(0));
			return json;
		}

	}

	/**
	 * 根据消息类型查询消息提醒数据 消息的类型目前有：0:系统消息,1:订单备注,2:订单状态更改,
	 * 3:新订单,4:拆单,5:客户留言,6:销售对仓库, 7:入库,8:退款有反馈
	 */
	@RequestMapping(value = "/queryByType")
	@ResponseBody
	public JsonResult queryByType(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String type = request.getParameter("type");
			if (type == null || "".equals(type)) {
				json.setOk(false);
				json.setMessage("获取type失败！");
				return json;
			}
			List<MessageNotification> messageNotifications = messageNotificationService
					.queryForListByType(Integer.valueOf(type));
			json.setOk(true);
			json.setData(messageNotifications);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 根据是否已读获取消息提醒数据
	 */
	@RequestMapping(value = "/queryByIsRead")
	@ResponseBody
	public JsonResult queryByIsRead(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String isRead = request.getParameter("isRead");
			if (isRead == null || "".equals(isRead)) {
				json.setOk(false);
				json.setMessage("获取isRead失败！");
				return json;
			}
			List<MessageNotification> messageNotifications = messageNotificationService.queryForListByIsRead(isRead);
			json.setOk(true);
			json.setData(messageNotifications);
			return json;

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 根据订单的no获取消息提醒的数据
	 */
	@RequestMapping(value = "/queryByOrderNo")
	@ResponseBody
	public JsonResult queryByOrderNo(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String orderNo = request.getParameter("orderNo");
			if (orderNo == null || "".equals(orderNo)) {
				json.setOk(false);
				json.setMessage("获取orderNo失败！");
				return json;
			}
			List<MessageNotification> messageNotifications = messageNotificationService.queryForListByOrderNo(orderNo);
			json.setOk(true);
			json.setData(messageNotifications);
			return json;

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 根据消息id获取消息提醒的数据
	 */
	@RequestMapping(value = "/queryById")
	@ResponseBody
	public JsonResult queryById(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String id = request.getParameter("id");
			if (id == null || "".equals(id)) {
				json.setOk(false);
				json.setMessage("获取id失败！");
				return json;
			}
			MessageNotification messageNotification = messageNotificationService.queryById(Integer.valueOf(id));
			json.setOk(true);
			json.setData(messageNotification);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询失败，原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 插入消息提醒的数据
	 */
	@RequestMapping(value = "/insertMessageNotification")
	@ResponseBody
	public JsonResult insertMessageNotification(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		try {
			MessageNotification messageNotification = getMessageNotification(request);
			messageNotification.setCreateTime(new Timestamp(System.currentTimeMillis()));
			messageNotification.setIsRead("N");
			messageNotificationService.insertMessageNotification(messageNotification);
			json.setOk(true);
			json.setMessage("新增成功!");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("插入失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("插入失败，原因：" + e.getMessage());
			return json;
		}

	}

	/**
	 * 更新消息提醒的已读数据
	 */
	@RequestMapping(value = "/updateIsRead")
	@ResponseBody
	public JsonResult updateIsRead(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String id = request.getParameter("id");
			if (id == null || "".equals(id)) {
				json.setOk(false);
				json.setMessage("获取id失败！");
				return json;
			}
			messageNotificationService.updateIsReadById(Integer.valueOf(id));
			json.setOk(true);
			json.setMessage("更新已读成功!");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("更新失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("更新失败，原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 根据订单号，消息类别和推送人更新已读
	 */
	@RequestMapping(value = "/updateByOrderNoTypeSenderId")
	@ResponseBody
	public JsonResult updateByOrderNoTypeSenderId(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String id = request.getParameter("id");
			int message_id=Integer.valueOf(request.getParameter("message_id"));
			if (id == null || "".equals(id)) {
				json.setOk(false);
				json.setMessage("获取id失败！");
				return json;
			}
			String type = request.getParameter("type");
			if (type == null || "".equals(type)) {
				json.setOk(false);
				json.setMessage("获取type失败！");
				return json;
			}

			//根据id设置已读
			messageNotificationService.updateIsReadById(Integer.valueOf(id));
			// 如果是系统消息，则根据id设置已读
//			if ("0".equals(type)) {
//				messageNotificationService.updateIsReadById(Integer.valueOf(id));
//			} else {
//				String orderNo = request.getParameter("orderNo");
//				if (orderNo == null || "".equals(orderNo)) {
//					json.setOk(false);
//					json.setMessage("获取orderNo失败！");
//					return json;
//				}
//
//				String senderid = request.getParameter("senderid");
//				if (senderid == null || "".equals(senderid)) {
//					json.setOk(false);
//					json.setMessage("获取senderid失败！");
//					return json;
//				}
//				messageNotificationService.updateByOrderNoTypeSenderId(orderNo, Integer.valueOf(type),Integer.valueOf(senderid),message_id);
//			}

			json.setOk(true);
			json.setMessage("更新已读成功!");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("更新失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("更新失败，原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 修改出库中已审核订单的消息提醒的创建日期，更新为当前时间
	 */
	@RequestMapping(value = "/updateApprovedOrder")
	@ResponseBody
	public JsonResult updateApprovedOrder(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			messageNotificationService.updateApprovedOrder();
			json.setOk(true);
			json.setMessage("更新已审核订单的消息提醒的创建日期成功!");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("更新已审核订单的消息提醒的创建日期失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("更新已审核订单的消息提醒的创建日期失败，\n原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/doAllReadedByUserId")
	@ResponseBody
	public JsonResult doAllReadedByUserId(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String userIdStr = request.getParameter("userId");
		if (userIdStr == null || "".equals(userIdStr)) {
			json.setOk(false);
			json.setMessage("获取userId失败!");
			return json;
		}
		String typeStr = request.getParameter("type");
		if (typeStr == null || "".equals(typeStr)) {
			json.setOk(false);
			json.setMessage("获取类型失败!");
			return json;
		}
		try {
			messageNotificationService.updateIsReadByUserId(Integer.valueOf(userIdStr), Integer.valueOf(typeStr));
			json.setOk(true);
			json.setMessage("全部标记已读成功!");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("全部标记已读失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("全部标记已读失败，\n原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 获取消息提醒bean需要的数据
	 */
	private MessageNotification getMessageNotification(HttpServletRequest request) {

		MessageNotification messageNotification = new MessageNotification();
		String id = request.getParameter("id");
		if (!(id == null || "".equals(id))) {
			messageNotification.setId(Integer.valueOf(id));
		}
		String orderNo = request.getParameter("orderNo");
		if (!(orderNo == null || "".equals(orderNo))) {
			messageNotification.setOrderNo(orderNo);
		}
		String senderId = request.getParameter("senderId");
		if (!(senderId == null || "".equals(senderId))) {
			messageNotification.setSenderId(Integer.valueOf(senderId));
		}
		String sendType = request.getParameter("sendType");
		if (!(sendType == null || "".equals(sendType))) {
			messageNotification.setSendType(Integer.valueOf(sendType));
		}
		String sendContent = request.getParameter("sendContent");
		if (!(sendContent == null || "".equals(sendContent))) {
			messageNotification.setSendContent(sendContent);
		}
		String linkUrl = request.getParameter("linkUrl");
		if (!(linkUrl == null || "".equals(linkUrl))) {
			messageNotification.setLinkUrl(linkUrl);
		}
		String isRead = request.getParameter("isRead");
		if (!(isRead == null || "".equals(isRead))) {
			messageNotification.setLinkUrl(isRead);
		}
		return messageNotification;
	}

}
