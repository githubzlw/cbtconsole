package com.cbt.service.impl;

import com.cbt.bean.RechargeRecord;
import com.cbt.dao.IRechargeRecordSSMDao;
import com.cbt.dao.RefundDaoPlus;
import com.cbt.dao.impl.RechargeRecordDaoImpl;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.refund.bean.RefundBean;
import com.cbt.refund.bean.RefundBeanExtend;
import com.cbt.service.RefundSSService;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RefundServiceImpl implements RefundSSService {

    @Autowired
    private RefundDaoPlus refundDao;// = new RefundDaoImpl();

    private IOrderwsDao dao = new OrderwsDao();
    private IRechargeRecordSSMDao record = new RechargeRecordDaoImpl();

    private PaymentDaoImp paymentDao = new PaymentDao();

    @Override
    public int agreeRefund(int uid, double appcount, int rid, String admuser, int agreeOrNot,
                           String appcurrency, String refuse, int type) {
        // TODO Auto-generated method stub
        //同意
        int redult = 0;
        if (agreeOrNot == 1) {
            redult = refundDao.agreeRefund(rid, admuser);
        } else if (agreeOrNot == 2) {
            redult = refundDao.refuseRefund(uid, rid, admuser, appcount, refuse, appcurrency, type);
        }
        return redult;
    }

    @Override
    public List<RefundBean> searchRefund(int userid, String username, String appdate,
                                         String agreeTime, int statue, int startpage, int type, int rid, String admin) {
        // TODO Auto-generated method stub
        return refundDao.searchByPramer(userid, username, appdate, agreeTime, statue, startpage, type, rid, admin);
    }

    @Override
    public List<RefundBean> searchRefundByState(int state) {
        // TODO Auto-generated method stub
        return refundDao.findByState(state);
    }

    @Override
    public List<AdminUserBean> getAllAdmUser() {
        // TODO Auto-generated method stub
        return refundDao.getAllAdmUser();
    }


    @Override
    public int finishRefund(int refundId) {
        return refundDao.finishRefund(refundId);
    }

    @Override
    public List<RefundBean> reportGet(String sdate, String edate, String reason, int page) {
        // TODO Auto-generated method stub
        return refundDao.reportGet(sdate, edate, reason, page);
    }

    @Override
    public List<RefundBeanExtend> searchAllRefundByUid(int userid) {
        // TODO Auto-generated method stub
        return refundDao.searchAllRefundByUid(userid);
    }

    @Override
    public int addFeedback(int rid, String feedback) {
        // TODO Auto-generated method stub
        return refundDao.addFeedback(rid, feedback);
    }

    @Override
    public int addRemark(double account, int rid, String remark, String agreepeople, int additionId, int status, int resontype) {

        return refundDao.addRemark(account, rid, remark, agreepeople, additionId, status, resontype);
    }

    @Override
    public double getApplyRefund(int userid) {
        return refundDao.getApplyRefund(userid);
    }

    @Override
    public double getApplyPaypal(int userid) {
        return refundDao.getApplyPaypal(userid);
    }

    @Override
    public Map<String, String> getApplyRefundByUserids(List<Integer> list) {
        return refundDao.getApplyRefundByUserids(list);
    }

    @Override
    public Map<String, String> getApplyPaypalByUserids(List<Integer> list) {
        return refundDao.getApplyPaypalByUserids(list);
    }

    @Override
    public double getRefund(int userid) {

        return refundDao.getRefundByUserid(userid);
    }

    @Override
    public Map<String, String> getRefundByUserids(List<Integer> list) {

        return refundDao.getRefundByUserids(list);
    }

    @Override
    public int addRefundFromAppeal(int uid, Double appcount, String paypalname,
                                   String payid, String ordeid, int type) {

        return refundDao.addRefundFromAppeal(uid, appcount, "USD", paypalname, payid, ordeid, type);
    }

    @Override
    public Map<String, String> getComplainRefundByUserids(String useridList) {
        return refundDao.getComplainRefundByUserids(useridList);
    }

    @Override
    public int closeOrderForPaypal(int userid, double available, String orderno) {
        int res = dao.closeOrder(orderno);
        if (res > 0 && Math.abs(available) > 0) {
            refundDao.insertRechangeRecord(userid, available, "paypal申诉,取消订单:" + orderno, "Ling", 0, 1);
            refundDao.insertRechangeRecord(userid, 0 - available, "paypal申诉,退款至paypal账户", "Ling", 1, 8);
        }
        return res;
    }

    @Override
    public List<RefundBean> getRefundList(int page) {

        return refundDao.getRefundList(page);
    }

    @Override
    public int updateRefund(int id, int userid, String orerid, String paypalName) {

        return refundDao.updateRefund(id, userid, orerid, paypalName);
    }

    @Override
    public List<PaymentBean> getPayment(double appCount, String curreny, int page) {

        return refundDao.getPayment(appCount, curreny, page);
    }

    @Override
    public List<RechargeRecord> getRecordList(int userid, int page) {

        return record.getRecordList(userid, page);
    }

    @Override
    public int confirmAndRemark(int id, int userid, String refundOrderNo, String remark, double account, int status, String admName) {
        return refundDao.confirmAndRemark(id, userid, refundOrderNo, remark, account, status, admName);
    }

    @Override
    public List<com.cbt.bean.Payment> getRefundOrderNo(String paypalEmail) {
        return refundDao.getRefundOrderNo(paypalEmail);
    }

    @Override
    public List<PaymentBean> queryPayMentInfoByUserId(int userId,int type) {
        return paymentDao.queryPayMentInfoByUserId(userId,type);
    }

    @Override
    public boolean updateRefundState(int userId, int refundId, int state) {
        return refundDao.updateRefundState(userId,refundId,state);
    }

}
