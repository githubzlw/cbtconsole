package com.importExpress.utli;


import com.importExpress.pojo.OrderCancelApproval;
import org.slf4j.LoggerFactory;

/**
 * 通知客户消息
 *
 * @author JXW
 */
public class NotifyToCustomerUtil {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(NotifyToCustomerUtil.class);


    /**
     * 订单确认进账消息通知
     *
     * @param userId
     * @param orderNo
     * @return
     */
    public static boolean confimOrderPayment(int userId, String orderNo) {

        boolean isSuccess;
        StringBuffer sqlBf = new StringBuffer("insert into user_message(user_id,order_no,content,type,jump_url) values(");
        sqlBf.append("" + userId + "");
        sqlBf.append(",'" + orderNo + "'");
        sqlBf.append(",'" + "we receive your payment order:" + orderNo + "'");//content
        sqlBf.append(",1");
        sqlBf.append(",'" + "/orderInfo/individualOrderdetail?orderNo=" + orderNo + "')");//jumpUrl

        try {
            sendSqlByMq(sqlBf.toString());
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            LOG.error("confimOrderPayment[" + sqlBf.toString() + "],error:" + e.getMessage());
        }
        return isSuccess;
    }


    /**
     * 修改订单状态
     *
     * @param userId
     * @param orderNo
     * @param oldState
     * @param newState
     * @return
     */
    public static boolean updateOrderState(int userId, String orderNo, int oldState, int newState) {

        boolean isSuccess;
        StringBuffer sqlBf = new StringBuffer("insert into user_message(user_id,order_no,content,type,jump_url) values(");
        sqlBf.append("" + userId + "");
        sqlBf.append(",'" + orderNo + "'");
        sqlBf.append(",'your order:" + orderNo + " state change[" + changeStateToString(oldState) + "] To [" + changeStateToString(newState) + "],please check your order'");
        sqlBf.append(",2");
        sqlBf.append(",'/orderInfo/individualOrderdetail?orderNo=" + orderNo + "')");

        try {
            sendSqlByMq(sqlBf.toString());
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            LOG.error("updateOrderState[" + sqlBf.toString() + "],error:" + e.getMessage());
        }
        return isSuccess;
    }


    /**
     * 修改订单价格或者商品价格
     *
     * @param userId
     * @param orderNo
     * @return
     */
    public static boolean updateOrderGoodsPrice(int userId, String orderNo) {

        boolean isSuccess;
        StringBuffer sqlBf = new StringBuffer("insert into user_message(user_id,order_no,content,type,jump_url) values(");
        sqlBf.append("" + userId + "");
        sqlBf.append(",'" + orderNo + "'");
        sqlBf.append(",'your order:" + orderNo + " goods price has change,please check it'");//content
        sqlBf.append(",3");
        sqlBf.append(",'/orderInfo/ctporders?orderNo=" + orderNo + "')");//jumpUrl

        try {
            sendSqlByMq(sqlBf.toString());
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            LOG.error("updateOrderGoodsPrice[" + sqlBf.toString() + "],error:" + e.getMessage());
        }
        return isSuccess;
    }


    /**
     * 确认或者拒绝退款
     *
     * @param userId
     * @param orderNo
     * @return
     */
    public static boolean confimOrRefuseRefund(int userId, String orderNo) {

        boolean isSuccess;
        StringBuffer sqlBf = new StringBuffer("insert into user_message(user_id,order_no,content,type,jump_url) values(");
        sqlBf.append("" + userId + "");
        sqlBf.append(",'" + orderNo + "'");
        sqlBf.append(",'" + "we receive your payment order:" + orderNo + "'");//content
        sqlBf.append(",4");
        sqlBf.append(",'" + "/orderInfo/individualOrderdetail?orderNo=" + orderNo + "')");//jumpUrl

        try {
            sendSqlByMq(sqlBf.toString());
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            LOG.error("confimOrRefuseRefund[" + sqlBf.toString() + "],error:" + e.getMessage());
        }
        return isSuccess;
    }


    public static boolean sendSqlByMq(String sql) {
        boolean isSuccess;
        SendMQ sendMQ = null;
        try {
            System.err.println("MQ:[" + sql + "]");
            sendMQ = new SendMQ();
            sendMQ.sendMsg(new RunSqlModel(sql));
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            LOG.error("sendSqlByMq[" + sql + "],error:" + e.getMessage());
        } finally {
            if (null != sendMQ) {
                try {
                    sendMQ.closeConn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }


    public static void insertIntoOrderCancelApproval(OrderCancelApproval cancelApproval){

        StringBuffer sql = new StringBuffer("insert into order_cancel_approval(user_id,order_no,pay_price,type," +
                "deal_state,order_state) values(");
        sql.append(cancelApproval.getUserId()+",");
        sql.append("'"+cancelApproval.getOrderNo()+"',");
        sql.append(cancelApproval.getPayPrice()+",");
        sql.append(cancelApproval.getType()+",");
        sql.append(cancelApproval.getDealState()+",");
        sql.append(cancelApproval.getOrderState()+")");
        sendSqlByMq(sql.toString());

    }

    private static String changeStateToString(int state) {
        String stateStr = "";
        if (state == -1) {
            stateStr = "System Cancle";
        } else if (state == 0) {
            stateStr = "ready to pay";
        } else if (state == 1) {
            stateStr = "purchase";
        } else if (state == 2) {
            stateStr = "Put in storage";
        } else if (state == 3) {
            stateStr = "shipment";
        } else if (state == 4) {
            stateStr = "complete";
        } else if (state == 5) {
            stateStr = "Order is confirmation";
        }
        return stateStr;
    }



}
