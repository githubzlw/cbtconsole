package com.cbt.website.servlet2;

import com.cbt.bean.OrderBean;
import com.cbt.change.util.CheckCanUpdateUtil;
import com.cbt.change.util.ErrorLogDao;
import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.pojo.RechangeRecord;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.dao.*;
import com.cbt.website.dao2.IWebsiteOrderDetailDao;
import com.cbt.website.dao2.WebsiteOrderDetailDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.SourcingOrderUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Servlet implementation class UpdateOrderStateServlet
 */
public class UpdateOrderStateServlet extends HttpServlet {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(UpdateOrderStateServlet.class);

    private static final long serialVersionUID = 1L;

    private PaymentDaoImp paymentDao = new PaymentDao();

    private SourcingOrderUtils sourcingOrderUtils = new SourcingOrderUtils();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateOrderStateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        this.doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sessionId = request.getSession().getId();
        String admuserJson = Redis.hget(sessionId, "admuser");
        if (admuserJson == null) {
            response.sendRedirect("website/main_login.jsp");
            return;
        }
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        int state = Integer.parseInt(request.getParameter("updatestate"));
        String remark = request.getParameter("remark");
        String oldStateStr = request.getParameter("oldState");
        if(StringUtils.isBlank(oldStateStr)){
            oldStateStr = "0";
        }
        int res = 0;

        try {
            // jxw 2017-4-24????????????????????????,?????????????????????state????????????admuser.getId()???
            boolean isCheck = CheckCanUpdateUtil.updateOnlineOrderInfoByLocal(orderid, state, admuser.getId());
            if (isCheck) {
                if ((state == -1 || state == 6) && StringUtils.isBlank(remark)) {
                    System.err.println("orderid:" + orderid + " update state:" + state + " no remark");
                    LOG.warn("orderid:" + orderid + " update state:" + state + " no remark");
                } else {
                    IWebsiteOrderDetailDao dao = new WebsiteOrderDetailDaoImpl();
                    // ???????????????????????????????????????????????????????????????????????????????????????????????????
                    if (state == -1 || state == 6) {
                        RechangeRecord record = paymentDao.querySystemCancelOrder(orderid);
                        if (record == null || record.getUserId() == 0 || record.getPrice() <= 0) {
                            res = dao.websiteUpdateOrderState(orderid, state);
                            // ?????????????????????????????????
                            dao.updateOrderStateLog(orderid, state,Integer.valueOf(oldStateStr), remark, admuser.getId());
                            sourcingOrderUtils.updateSourcingOrder(orderid, state);
                            // ????????????????????????
                            InsertMessageNotification msgNtDao = new InsertMessageNotification();
                            msgNtDao.insertOrderStatusChanges(orderid, admuser, state);

                            OrderInfoDao orderInfoDao = new OrderInfoImpl();
                            OrderBean orderBean = orderInfoDao.getOrderInfo(orderid, null);
                            //??????????????????????????????
                            UserDao userDao = new UserDaoImpl();
                            userDao.updateUserAvailable(orderBean.getUserid(),
                                    new BigDecimal(String.valueOf(orderBean.getPay_price())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
                                    " system closeOrder:" + orderid,orderid, String.valueOf(admuser.getId()), 0, 0, 1);
                        } else {
                            res = 0;
                        }
                    } else {

                        res = dao.websiteUpdateOrderState(orderid, state);
                        sourcingOrderUtils.updateSourcingOrder(orderid, state);
                        // ?????????????????????????????????
                        dao.updateOrderStateLog(orderid, state, Integer.valueOf(oldStateStr),remark, admuser.getId());
                        // ????????????????????????
                        InsertMessageNotification msgNtDao = new InsertMessageNotification();
                        msgNtDao.insertOrderStatusChanges(orderid, admuser, state);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("????????????orderinfo??????????????????," + "?????????:" + orderid, e);
            // jxw 2017-4-24 ???????????????????????????????????????????????????
            String sqlStr = "update orderinfo set state ='" + state + "' where order_no = '" + orderid + "';";
            ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, admuser.getId(), state, "????????????,??????:" + e.getMessage());
        }

        //?????????
		/*IWebsiteOrderDetailDao dao = new WebsiteOrderDetailDaoImpl();
		res = dao.websiteUpdateOrderState(orderid, state);
		// ????????????????????????
		InsertMessageNotification msgNtDao = new InsertMessageNotification();
		msgNtDao.insertOrderStatusChanges(orderid, admuser,state);*/

        PrintWriter out = response.getWriter();
        out.print(res);
        out.close();
    }

}
