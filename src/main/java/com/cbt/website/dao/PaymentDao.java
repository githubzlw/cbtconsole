package com.cbt.website.dao;

import com.cbt.bean.AutoOrderBean;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderPaymentBean;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.AddBalanceInfo;
import com.cbt.pojo.RechangeRecord;
import com.cbt.refund.bean.RefundBean;
import com.cbt.website.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class PaymentDao implements PaymentDaoImp {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PaymentDao.class);
    private DecimalFormat format = new DecimalFormat("#0.00");

    @Override
    public List<PaymentBean> query(PayCheckBean bean) {
        List<PaymentBean> list = new ArrayList<PaymentBean>();
        String sql = "select sql_calc_found_rows *, if(paymentid='',(select concat(paymentid,',',p.payment_amount) "
                + "from payment p where orderid=left(p1.orderid ,length(p1.orderid )-1)  "
                + "and paymentid!='' and p.paystatus=1 group by orderid limit 1 ),paymentid ) as allpay, "
                + "(select sum(p.payment_amount) from payment p where p1.orderid= p.orderid and p.paystatus=1  "
                + "group by orderid ) as payamount, "
                + "if(paytype!=2,(select sum(p.payment_amount) from payment p where "
                + "p1.orderid= p.orderid and p.paystatus=1 "
                + "and paytype=2 group by p.orderid  ),payment_amount) as bpay,  "
                + " if(orderid!='',(select count(distinct func_get_split_string(p7.orderid,'_',1)))  "
                + " from payment p7  where p7.userid=p1.userid),0) as ordersum "
                + "from payment  p1  where p1.paystatus=1 ";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        PreparedStatement stmt2 = null;
        ResultSet st2 = null;
        PaymentBean pbean = null;
        int userid = bean.getUserid();
        String payEmail = bean.getPayEmail();
        String userEmail = bean.getEmail();
        String paytype = bean.getPaytype();
        String dateend = bean.getDataEnd();
        String datestart = bean.getDataStart();
        int ordersum = bean.getOrdersum();
        int index = 0;
        int page_total = 0;
        try {

            if (userid > 0) {
                sql += " and p1.userid=?";
            } else {
                sql += " and p1.userid>0";
            }

            if (payEmail != null && !payEmail.isEmpty()) {
                sql += " and p1.orderdesc like ('%" + payEmail + "%')";
            }

            if (userEmail != null && !userEmail.isEmpty()) {
                sql += " and p1.userid in  (select id from user where email like '%" + userEmail + "%' )";
            }

            if (paytype != null && !"-1".equals(paytype)) {
                sql += " and p1.paytype=?";
            }
            if (datestart != null && !datestart.isEmpty()) {
                sql += " and p1.createtime>=?";
            }
            if (dateend != null && !dateend.isEmpty()) {
                sql += " and p1.createtime<=?";
            }

            sql += " group by p1.orderid ";
            if (ordersum == 1) {
                sql += " having ordersum=1";
            }

            sql += " order by p1.orderid desc,p1.createtime desc,p1.paymentid desc limit ?,?";
            System.out.println("sql:" + sql);
            stmt = conn.prepareStatement(sql);
            if (userid > 0) {
                index++;
                stmt.setInt(index, userid);
            }
            if (paytype != null && !"-1".equals(paytype)) {
                index++;
                stmt.setString(index, paytype);
            }
            if (datestart != null) {
                index++;
                stmt.setString(index, datestart);
            }
            if (dateend != null) {
                index++;
                stmt.setString(index, dateend);
            }
            index++;
            stmt.setInt(index, (bean.getPage() - 1) * bean.getRows());
            index++;
            stmt.setInt(index, bean.getRows());
            stmt2 = conn.prepareStatement("select found_rows();");
            st = stmt.executeQuery();
            st2 = stmt2.executeQuery();
            if (st2.next()) {
                page_total = st2.getInt("found_rows()");
            }
            String type = null;
            String type_str = null;

            Map<String, String> orderisList = new HashMap<String, String>();
            Map<String, String> orderisPayList = new HashMap<String, String>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // int i=0;
            long time = df.parse("2016-10-13 02:55:59.0").getTime();
            while (st.next()) {

                String orderid = st.getString("orderid");
                String paymentid = st.getString("paymentid");
                paymentid = paymentid == null ? "" : paymentid;
                // 订单既有余额支付 又有paypal支付，只显示paypal支付，并列出其中余额支付金额以及该订单总支付金额
                String mapPay = orderisList.get(orderid);
                /*
                 * if (mapPay != null && paymentid.isEmpty() &&
                 * !mapPay.isEmpty()) { continue; }
                 */
                // i++;
                orderisList.put(orderid, paymentid);
                double payment_amount = st.getDouble("payment_amount");
                // System.err.println(payment_amount+"------------------"+i);
                String strPayAmount = st.getString("payamount");
                strPayAmount = StrUtils.isNotNullEmpty(strPayAmount) ? strPayAmount.trim() : "";
                String bpay = st.getString("bpay");
                bpay = StrUtils.isNullOrEmpty(bpay) ? "0.00" : bpay;
                // 合并汇总单号
                // System.err.println("orderid:"+orderid);
                // System.err.println("orderisPayList:"+orderisPayList);
                // System.err.println("strPayAmount:"+strPayAmount);
                if (orderid.length() == 16 && orderisPayList.get(orderid) == null && !strPayAmount.isEmpty()) {
                    orderisPayList.put(orderid, strPayAmount + "-" + payment_amount + "-" + bpay);
                }

                pbean = new PaymentBean();
                pbean.setTotal(page_total + "");
                String createtime = st.getString("createtime");
                pbean.setCreatetime(createtime);
                long time2 = createtime != null ? df.parse(createtime).getTime() : 0;
                if (orderid.length() == 16 && orderid.indexOf('N') == -1 && time2 < time) {
                    pbean.setIsMainOrder(1);
                }

                pbean.setUserid(st.getInt("userid"));
                pbean.setPayment_amount(format.format(payment_amount));
                pbean.setPayment_cc(st.getString("payment_cc"));
                pbean.setPayAll(strPayAmount);
                pbean.setOrderSum(st.getInt("ordersum"));

                String orderDesc = st.getString("orderdesc");
                orderDesc = orderDesc == null ? "" : orderDesc;
                // System.err.println(orderDesc);
                if (orderDesc.indexOf("@") > -1 && orderDesc.indexOf(".") > -1) {
                    pbean.setOrderdesc(orderDesc);
                } else {
                    pbean.setOrderdesc("");
                    // pbean.setOrderSplit("余额支付".equals(orderDesc)?"":orderDesc);
                }
                pbean.setOrderid(orderid);
                type = st.getString("paytype");
                if (type != null) {
                    if ("0".equals(type)) {
                        pbean.setOrderSplit("");
                        type_str = "Paypal";
                    } else if ("1".equals(type)) {
                        type_str = "Wire Transfer";
                        pbean.setOrderSplit("");
                    } else if ("2".equals(type)) {
                        type_str = "余额支付";
                        pbean.setOrderSplit("");
                    } else if ("3".equals(type)) {
                        pbean.setOrderSplit("2");
                        type_str = "";
                    } else if ("4".equals(type)) {
                        pbean.setOrderSplit("1");
                        type_str = "";
                    }
                }
                pbean.setPaytype(type_str);
                pbean.setPaymentid(paymentid);
                pbean.setPayAmount(format.format(payment_amount));
                pbean.setBalancePay(bpay);
                if (format.format(payment_amount).equals(bpay)) {
                    pbean.setBalancePayFlag(0);
                } else {
                    pbean.setBalancePayFlag("0.00".equals(bpay) ? 0 : 1);
                }

                // 余额支付或者合并支付或者拆单
                if (paymentid.isEmpty()) {
                    // paymentid,paypal 付款总额
                    String allpay = st.getString("allpay");
                    allpay = allpay == null ? "" : allpay;

                    if (StrUtils.isNotNullEmpty(allpay)) {
                        String[] allpays = allpay.split(",");
                        pbean.setPaymentid(allpays[0]);
                        // 合并汇总单号
                        if (orderid.length() == 17) {
                            String str = orderisPayList.get(orderid.substring(0, 16));
                            if (str == null) {
                                pbean.setPayAmount(allpays.length > 1 ? allpays[1] : format.format(payment_amount));
                            } else {
                                String[] strsOrderisPay = str.split("-");
                                pbean.setPayAll(strsOrderisPay[0]);
                                double paypalMoney = strsOrderisPay.length > 1 ? Double.valueOf(strsOrderisPay[1]) : 0;
                                pbean.setPayAmount(format.format(paypalMoney));
                                if (!"0.00".equals(strsOrderisPay[2])) {
                                    pbean.setBalancePay(strsOrderisPay[2]);
                                    pbean.setBalancePayFlag(1);
                                }
                            }
                        }
                    }

                    // 余额支付
                    if (StrUtils.isNotNullEmpty(strPayAmount)) {
                        strPayAmount = StrUtils.matchStr(strPayAmount, "(\\d+\\.*\\d*)");
                        if (StrUtils.isNotNullEmpty(strPayAmount)) {
                            // System.err.println("strPayAmount:"+strPayAmount);
                            double payamount = Double.valueOf(strPayAmount);
                            payamount = payamount - payment_amount;
                            if (!"0.00".equals(format.format(payamount))) {
                                pbean.setBalancePay(format.format(payamount));
                                pbean.setBalancePayFlag(1);
                            }
                        }
                    }
                }
                if (pbean.getBalancePayFlag() == 1) {
                    pbean.setPaytype("合并支付");
                }
                list.add(pbean);
            }
            orderisList = null;
            orderisPayList = null;
        } catch (SQLException e) {
            LOG.error("",e);
        } catch (ParseException e) {

            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st2 != null) {
                try {
                    st2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int queryTotalNum(PayCheckBean bean) {

        String sql = "select count(*) from (select orderid,if(orderid!='',(select count(distinct func_get_split_string(p7.orderid,'_',1))  "
                + " from payment p7  where p7.userid=p1.userid),0) as ordersum from payment p1 where p1.paystatus=1 ";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        int userid = bean.getUserid();
        String payEmail = bean.getPayEmail();
        String userEmail = bean.getEmail();
        String paytype = bean.getPaytype();
        String dateend = bean.getDataEnd();
        String datestart = bean.getDataStart();
        int ordersum = bean.getOrdersum();
        int index = 0;
        int page_total = 0;
        try {

            if (userid > 0) {
                sql += " and p1.userid=?";
            } else {
                sql += " and p1.userid>0";
            }

            if (payEmail != null && !payEmail.isEmpty()) {
                sql += " and p1.orderdesc like ('%" + payEmail + "%')";
            }

            if (userEmail != null && !userEmail.isEmpty()) {
                sql += " and p1.userid in  (select id from user where email like '%" + userEmail + "%' )";
            }

            if (paytype != null && !"-1".equals(paytype)) {
                sql += " and p1.paytype=?";
            }
            if (datestart != null && !datestart.isEmpty()) {
                sql += " and p1.createtime>?";
            }
            if (dateend != null && !dateend.isEmpty()) {
                sql += " and p1.createtime<?";
            }

            sql += " group by p1.orderid ";
            if (ordersum == 1) {
                sql += " having ordersum=1";
            }
            sql += ") p2";
            System.out.println("sql:" + sql);
            stmt = conn.prepareStatement(sql);
            if (userid > 0) {
                index++;
                stmt.setInt(index, userid);
            }
            if (paytype != null && !"-1".equals(paytype)) {
                index++;
                stmt.setString(index, paytype);
            }
            if (datestart != null) {
                index++;
                stmt.setString(index, datestart);
            }
            if (dateend != null) {
                index++;
                stmt.setString(index, dateend);
            }

            st = stmt.executeQuery();
            if (st.next()) {
                page_total = st.getInt(1);
            }

        } catch (SQLException e) {
            LOG.error(e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return page_total;
    }

    @Override
    public double getAllTotalMoney(PayCheckBean bean) {// whj
        double d = 0.00;
        String sql = "select  sum(payment_amount) from payment where paystatus=1 ";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        int userid = bean.getUserid();
        String payEmail = bean.getPayEmail();
        String userEmail = bean.getEmail();
        String paytype = bean.getPaytype();
        String dateend = bean.getDataEnd();
        String datestart = bean.getDataStart();
        if (userid >= 0) {
            sql += " and userid=" + userid + "";
        }

        if (payEmail != null && !payEmail.isEmpty()) {
            sql += " and orderdesc like ('%" + payEmail + "%')";
        }

        if (userEmail != null && !userEmail.isEmpty()) {
            sql += " and userid in  (select id from user where email like '%" + userEmail + "%' )";
        }

        if (paytype != null) {
            sql += " and paytype='" + paytype + "'";
        }
        if (datestart != null && !datestart.isEmpty()) {
            sql += " and createtime>'" + datestart + "'";
        }
        if (dateend != null && !dateend.isEmpty()) {
            sql += " and createtime<'" + dateend + "'";
        }
        sql += " order by createtime desc";
        // int index=0;
        try {
            // if(userid>=0){
            // index++;
            // stmt.setInt(index, userid);
            // }
            // if(payEmail!=null){
            // index++;
            // stmt.setString(index, payEmail);
            // }
            // if(paytype!=null){
            // index++;
            // stmt.setString(index, paytype);
            // }
            // if(datestart!=null){
            // index++;
            // stmt.setString(index, datestart);
            // }
            // if(dateend!=null){
            // index++;
            // stmt.setString(index, dateend);
            // }
            stmt = conn.prepareStatement(sql);
            st = stmt.executeQuery();
            while (st.next()) {
                d = st.getDouble("sum(payment_amount)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeConnection(conn);
        }
        return d;
    }

    @Override
    public List<PaymentBean> queryAll(PayCheckBean bean) {
        List<PaymentBean> list = new ArrayList<PaymentBean>();
        String sql = "select sql_calc_found_rows *from payment where paystatus=1 "
                + "group by userid order by userid desc limit ?,40";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        PreparedStatement stmt2 = null;
        ResultSet st2 = null;
        int page_total = 0;
        PaymentBean pbean = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (bean.getPage() - 1) * 40);
            st = stmt.executeQuery();
            stmt2 = conn.prepareStatement("select found_rows();");
            st2 = stmt2.executeQuery();

            String type = null;
            String type_str = null;
            while (st2.next()) {
                page_total = st2.getInt("found_rows()");
                page_total = page_total % 40 == 0 ? page_total / 40 : page_total / 40 + 1;
            }
            while (st.next()) {
                pbean = new PaymentBean();
                pbean.setTotal(page_total + "");
                pbean.setCreatetime(st.getString("createtime"));
                pbean.setUserid(st.getInt("userid"));
                pbean.setPayment_amount(st.getString("payment_amount"));
                pbean.setPayment_cc(st.getString("payment_cc"));
                pbean.setOrderdesc(st.getString("orderdesc"));
                pbean.setOrderid(st.getString("orderid"));
                type = st.getString("paytype");
                if (type != null) {
                    if ("0".equals(type)) {
                        type_str = "Paypal";
                    } else if ("1".equals(type)) {
                        type_str = "Wire Transfer";
                    } else if ("2".equals(type)) {
                        type_str = "余额支付";
                    } else if ("3".equals(type)) {
                        type_str = "拆分订单";
                    } else if ("4".equals(type)) {
                        type_str = "合并支付";
                    }
                }
                pbean.setPaytype(type_str);
                pbean.setPaymentid(st.getString("paymentid"));
                list.add(pbean);
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st2 != null) {
                try {
                    st2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<PayInfoBean> getPaymentForCheck(int page) {

        List<PayInfoBean> list = new ArrayList<PayInfoBean>();
        String sql = "select sql_calc_found_rows userid,email,available_m,n.num "
                + "from payment p,user u,(select count(m.id) as num, m.userid as uid "
                + "from payment m  group by m.orderid) as n "
                + "where p.paystatus=1 and p.userid=u.id and p.userid= n.uid "
                + "group by p.userid order by p.userid desc";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        PayInfoBean pbean = null;
        PreparedStatement stmt2 = null;
        ResultSet st2 = null;
        int page_total = 0;
        try {
            stmt = conn.prepareStatement(sql);
            // stmt.setInt(1, (page-1)*40);
            st = stmt.executeQuery();
            stmt2 = conn.prepareStatement("select found_rows();");
            st2 = stmt2.executeQuery();
            while (st2.next()) {
                page_total = st2.getInt("found_rows()");
                page_total = page_total % 40 == 0 ? page_total / 40 : page_total / 40 + 1;
            }
            while (st.next()) {
                pbean = new PayInfoBean();
                pbean.setUserId(st.getString("userid"));
                pbean.setUserEmail(st.getString("email"));
                pbean.setCurrencyBalance(st.getString("available_m"));
                pbean.setPageTotal(page_total);
                pbean.setOrderNum(st.getInt("num"));
                pbean.setPaypalAmount(st.getString("num"));
                list.add(pbean);
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st2 != null) {
                try {
                    st2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public double getPayAllUserid(int userid) {
        String sql = "select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amount from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and (p.paytype='0' or p.paytype='1' or p.paytype='2') and p.userid=?";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        double total_pay = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            st = stmt.executeQuery();
            while (st.next()) {
                total_pay = st.getDouble("payment_amount");
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return total_pay;
    }

    @Override
    public double getPayUserid(int userid) {
        String sql = "select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amount from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and (p.paytype='0' or p.paytype='1') and p.userid=?";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        double total_pay = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            st = stmt.executeQuery();
            while (st.next()) {
                total_pay = st.getDouble("payment_amount");
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return total_pay;
    }

    @Override
    public Map<String, Double> getPaysUserid(int userid) {
        String sql = "select a.userid,IFNULL(a.payment_amounta,0) as payment_amounta,"
                + "IFNULL(b.payment_amountb,0) as payment_amountb,IFNULL(c.payment_amountc,0) as payment_amountc,"
                + "IFNULL(d.payment_amountc,0) as payment_amountd,IFNULL(f.payment_amountc,0) as payment_amountf from "
                + "(select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amounta from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and p.paytype='0' and p.userid=?) a,"
                + "(select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amountb from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and p.paytype='1' and p.userid=?) b,"
                + "(select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amountc from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and p.paytype='2' and p.userid=?) c,"
                + "(select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amountc from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and p.paytype=5 and p.userid=?) d,"
                + "(select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amountc from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and p.paytype in(0,1) and LENGTH(p.orderid)=8 and p.userid=?) f ";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        Map<String, Double> result = new HashMap<String, Double>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setInt(2, userid);
            stmt.setInt(3, userid);
            stmt.setInt(4, userid);
            stmt.setInt(5, userid);
            st = stmt.executeQuery();
            while (st.next()) {
                result.put("paypal", st.getDouble("payment_amounta"));
                result.put("wiretransfer", st.getDouble("payment_amountb"));
                result.put("balancePay", st.getDouble("payment_amountc"));
                result.put("stripePay", st.getDouble("payment_amountd"));
                result.put("payForBalance", st.getDouble("payment_amountf"));
                result.put("all", st.getDouble("payment_amountb") + st.getDouble("payment_amounta") + st.getDouble("payment_amountd"));
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public Map<String, String> getPayByUserids(List<Integer> list) {
        String sql = "select p.userid,sum(p.payment_amount/e.exchange_rate)  "
                + "as payment_amount from payment p,exchange_rate e  where "
                + "p.payment_cc=e.country  and p.paystatus=1 and (p.paytype='0' "
                + "or p.paytype='1') and p.userid in (";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            for (Integer l : list) {
                sql = sql + l + ",";
            }
            sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
            sql = sql + ") group by p.userid";

            stmt = conn.prepareStatement(sql);
            st = stmt.executeQuery();
            while (st.next()) {
                map.put(st.getString("userid"), st.getString("payment_amount"));
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return map;
    }

    @Override
    public Map<String, String> getPaypalByUserids(List<Integer> list) {
        String sql = "select userid,sum(payment_amount) as paypal from payment "
                + "where paystatus=0 and payment_amount<0 and userid in (";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            for (Integer l : list) {
                sql = sql + l + ",";
            }
            sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
            sql = sql + ") group by userid";

            stmt = conn.prepareStatement(sql);
            st = stmt.executeQuery();
            while (st.next()) {
                map.put(st.getString("userid"), st.getString("paypal"));
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return map;
    }

    @Override
    public double getPaypalByUserid(int userid) {
        String sql = "select userid,sum(payment_amount) as paypal from payment "
                + "where paystatus=0 and payment_amount<0 and userid=?";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        double result = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            st = stmt.executeQuery();
            while (st.next()) {
                result = st.getDouble("paypal");
            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public PaymentBean getPayment(String userid, String orderid, String paymentid) {
        StringBuffer sql = new StringBuffer("select * from payment where 1 = 1 ");
        if (userid != null || userid != "") {
            sql.append("and userid = " + userid + " ");
        }
        if (orderid != null || orderid != "") {
            sql.append("and orderid = '" + orderid + "' ");
        }
        if (paymentid != null || paymentid != "") {
            sql.append("and paymentid ='" + paymentid + "' ");
        }
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PaymentBean payment = null;
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                payment = new PaymentBean();
                payment.setUserid(rs.getInt("userid"));
                payment.setOrderid(rs.getString("orderid"));
                payment.setPaymentid(rs.getString("paymentid"));
                payment.setPayment_other(rs.getFloat("payment_other") + "");
                payment.setPayment_amount(rs.getString("payment_amount" + ""));
                payment.setPayment_cc(rs.getString("payment_cc"));
                payment.setOrderdesc(rs.getString("orderdesc"));
                payment.setUsername(rs.getString("username"));
                payment.setPaystatus(rs.getInt("paystatus") + "");
                payment.setCreatetime(rs.getString("createtime"));
                payment.setPaySID(rs.getString("paySID"));
                payment.setPayflag(rs.getString("payflag"));
                payment.setPaytype(rs.getString("paytype"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }

        return payment;
    }

    @Override
    public int addPayment(PaymentBean payment) {
        String sql0 = "select  count(*) from payment where orderid=?";
        String sql2 = " update payment set userid=?,paymentid=?,payment_amount=?,payment_cc=?,"
                + "orderdesc=?,paystatus=?,createtime=?,paySID=?,payflag=?,paytype=?,payment_other=?,"
                + " username=? where orderid=?";

        String sql = "insert into  payment (userid,orderid,paymentid,payment_amount,payment_cc,orderdesc,"
                + "paystatus,createtime,paySID,payflag,paytype,payment_other,username) "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        PreparedStatement stmt0 = null;
        ResultSet rs = null;
        int result = 0;
        try {
            stmt0 = conn.prepareStatement(sql0);
            stmt0.setString(1, payment.getOrderid());
            rs = stmt0.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count(*)") > 0) {
                    stmt = conn.prepareStatement(sql2);
                    stmt.setInt(1, payment.getUserid());
                    stmt.setString(2, payment.getPaymentid());
                    stmt.setString(3, payment.getPayment_amount());
                    stmt.setString(4, payment.getPayment_cc());
                    stmt.setString(5, payment.getOrderdesc());
                    stmt.setString(6, payment.getPaystatus());
                    stmt.setString(7, payment.getCreatetime());
                    stmt.setString(8, payment.getPaySID());
                    stmt.setString(9, payment.getPayflag());
                    stmt.setString(10, payment.getPaytype());
                    stmt.setString(11, payment.getPayment_other());
                    stmt.setString(12, payment.getUsername());
                    stmt.setString(13, payment.getOrderid());
                    result = stmt.executeUpdate();
                } else {
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, payment.getUserid());
                    stmt.setString(2, payment.getOrderid());
                    stmt.setString(3, payment.getPaymentid());
                    stmt.setString(4, payment.getPayment_amount());
                    stmt.setString(5, payment.getPayment_cc());
                    stmt.setString(6, payment.getOrderdesc());
                    stmt.setString(7, payment.getPaystatus());
                    stmt.setString(8, payment.getCreatetime());
                    stmt.setString(9, payment.getPaySID());
                    stmt.setString(10, payment.getPayflag());
                    stmt.setString(11, payment.getPaytype());
                    stmt.setString(12, payment.getPayment_other());
                    stmt.setString(13, payment.getUsername());
                    result = stmt.executeUpdate();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public int getCountOrders(int ueserid) {
        int result = 0;
        String sql = "select count(*) from payment where 1 = 1 and userid=? group by orderid";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ueserid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("count(*)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public int addOrderNote(String userid, String orderid, String orderAdmin, String invoice) {
        String sql = "insert into  payment_invoice (userid,orderid,order_admin,invoice,"
                + "createtime,ispayment) values(?,?,?,?,now(),?)";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userid);
            stmt.setString(2, orderid);
            stmt.setString(3, orderAdmin);
            stmt.setString(4, invoice);
            stmt.setInt(5, 0);
            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public int addPaymentNote(String userid, String orderid, String paymentAdmin) {
        String sql = "insert into  payment_invoice (userid,orderid,payment_admin,"
                + "createtime,ispayment) values(?,?,?,now(),?)";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userid);
            stmt.setString(2, orderid);
            stmt.setString(3, paymentAdmin);
            stmt.setInt(4, 1);
            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public int updateOrderNote(String userid, String orderid, String orderAdmin, String invoice) {
        String sql = "update  payment_invoice set order_admin=?,invoice=? " + "where userid=? and orderid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderAdmin);
            stmt.setString(2, invoice);
            stmt.setString(3, userid);
            stmt.setString(4, orderid);
            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public int updatePaymentNote(String userid, String orderid, String paymentAdmin, int isPayment) {
        String sql = "update  payment_invoice set payment_admin=?,ispayment=? " + "where userid=? and orderid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, paymentAdmin);
            stmt.setInt(2, isPayment);
            stmt.setString(3, userid);
            stmt.setString(4, orderid);
            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public int countPaymentInvoiceByorderuser(String userid, String orderid) {
        int result = 0;
        String sql = "select count(*) from payment_invoice where 1 = 1 and userid=? and orderid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userid);
            stmt.setString(2, orderid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("count(*)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public String getFileByOrderid(String orderid) {
        String result = "";
        String sql = "select invoice from payment_invoice where  orderid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("invoice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public List<AutoOrderBean> getOrderList(String orderid, String userid, int page) {
        String sql = "SELECT sql_calc_found_rows p.id,p.createtime,p.orderid,p.userid,p.order_admin,p.payment_admin,"
                + "o.state,o.pay_price,o.currency,o.orderid as oid,"

                + " (select concat_ws(',',py.paystatus,py.paytype,py.id) from payment py where "
                + " py.orderid=p.orderid and py.paystatus !=0 limit 1) as paymentstr "

                + "FROM payment_invoice p,orderinfo o where p.valid=1 and p.orderid=o.order_no ";
        if (orderid != null && !orderid.isEmpty()) {
            sql = sql + " and p.orderid=?";
        }
        if (userid != null && !userid.isEmpty()) {
            sql = sql + " and p.userid=?";
        }
        sql = sql + " limit ?,40";

        String sql2 = "select found_rows();";

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        List<AutoOrderBean> list = new ArrayList<AutoOrderBean>();
        try {
            stmt = conn.prepareStatement(sql);
            int index = 1;
            if (orderid != null && !orderid.isEmpty()) {
                stmt.setString(index, orderid);
                index++;
            }
            if (userid != null && !userid.isEmpty()) {
                stmt.setString(index, userid);
                index++;
            }
            stmt.setInt(index, page);
            int count = 0;
            rs = stmt.executeQuery();

            stmt2 = conn.prepareStatement(sql2);
            rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                count = rs2.getInt("found_rows()");
            }
            while (rs.next()) {
                AutoOrderBean bean = new AutoOrderBean();
                bean.setCurrency(rs.getString("currency"));
                bean.setOrderAdmin(rs.getString("order_admin"));
                bean.setOrderid(rs.getString("orderid"));
                bean.setPaymentAdmin(rs.getString("payment_admin"));
                bean.setPayPrice(rs.getString("pay_price"));
                int state = rs.getInt("state");
                String strState = state == 0 ? "等待付款" : "";
                strState = state == 5 ? "确认价格中" : strState;
                strState = state == -1 ? "后台取消" : strState;
                strState = state == 6 ? "用户取消" : strState;
                strState = state == 1 ? "购买中" : strState;
                strState = state == 2 ? "已到仓库" : strState;
                strState = state == 3 ? "出运中" : strState;
                strState = state == 4 ? "完结" : strState;
                strState = state == 7 ? "预订单" : strState;
                bean.setOrderState(strState);
                bean.setUserid(rs.getString("userid"));
                bean.setCount(count);
                bean.setIndex(rs.getInt("oid") + "");

                String paymentstr = rs.getString("paymentstr");
                if (paymentstr == null || paymentstr.isEmpty()) {
                    bean.setPayStatus("未付款");
                    bean.setPayType("");
                    bean.setPayId(0);
                } else {
                    String[] paymentstrs = paymentstr.split(",");
                    bean.setPayStatus(paymentstrs[0].equals("0") ? "未付款" : "已付款");
                    bean.setPayType(paymentstrs[1].equals("0") ? "paypal" : "Wire Transfer");
                    bean.setPayId(Integer.valueOf(paymentstrs[2]));
                }
                bean.setCreateTime(rs.getString("createtime"));
                bean.setId(rs.getInt("id"));
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }

        return list;
    }

    @Override
    public int cancelPayment(String pid) {
        String sql = "update payment set paystatus=0  where id=?";
        Connection conn1 = DBHelper.getInstance().getConnection();
        Connection conn2 = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        int res = 0;
        try {
            stmt2 = conn2.prepareStatement(sql);
            stmt2.setInt(1, Integer.valueOf(pid));
            res = stmt2.executeUpdate();

            stmt1 = conn1.prepareStatement(sql);
            stmt1.setInt(1, Integer.valueOf(pid));
            res = stmt1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt1 != null) {
                try {
                    stmt1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn2);
            DBHelper.getInstance().closeConnection(conn1);
        }
        return res;
    }

    @Override
    public int AddPayment(String userid) {
        int row = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            String sql = "select id from user_coupon where userid='" + userid + "'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                sql = "insert into user_coupon (userid,starttime,endtime,percentage,maxdis,state,usetype,type) values"
                        + "('" + userid + "', now(), date_add(now(), interval 14 day), 10, 30, 0, 0, 1)";
                stmt = conn.prepareStatement(sql);
                row = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return row;
    }

    @Override
    public List<PaymentStatistics> queryPaymentStatistics(PayCheckBean bean) {
        List<PaymentStatistics> psList = new ArrayList<PaymentStatistics>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select pm.*,pb.order_num,orderinfo.state from"
                + "(select userid,orderid,sum(payment_amount) as payment_amount,createtime,"
                + "paytype,payment_cc,paymentid from payment where paystatus=1 and paytype in(0,1,2,5) " +
                " and userid not in(select id from `user` where email like 'test%' " +
                " or email like '%qq.com' or email like '%163.com' or email like '%ww.com' or email like 'rlef%' " +
                " or email = 'undefined' or email like 'asdf%'  or email like 'importexpress@%' or email like 'ling@tes%' " +
                " or email like '%@qq.com%' or email like '%@test%' or email like 'eee@%' or email like 'ceshi@%' " +
                " or email like '%www@%' or email like '%aaaa@%' or email like '%@import-express.com') ";
        int userid = bean.getUserid();
        String payEmail = bean.getPayEmail();
        String userEmail = bean.getEmail();
        String paytype = bean.getPaytype();
        String datestart = bean.getDataStart();
        String dateend = bean.getDataEnd();
        int ordersum = bean.getOrdersum();
        if (userid > 0) {
            sql += " and userid=?";
        } else {
            sql += " and userid>0";
        }
        if (payEmail != null && !payEmail.isEmpty()) {
            sql += " and orderdesc like ('%" + payEmail + "%')";
        }
        if (userEmail != null && !userEmail.isEmpty()) {
            sql += " and userid in  (select id from user where email like '%" + userEmail + "%' )";
        }
        if (paytype != null && !"-1".equals(paytype)) {
            sql += " and paytype=?";
        }
        if (datestart != null && !datestart.isEmpty()) {
            sql += " and createtime >= ?";
        }
        if (dateend != null && !dateend.isEmpty()) {
            sql += " and createtime <= ?";
        }
        if(StringUtils.isNotBlank(bean.getPayId())){
            sql += " and paymentid = ?";
        }
        sql += " group by orderid ) pm INNER JOIN "
                + "(select userid,count(userid) as order_num from (select userid,count(orderid) "
                + "from payment where paystatus=1  "
                + "and LENGTH(orderid) = 16 ";
        if (userid > 0) {
            sql += "and userid=? ";
        } else {
            sql += "and userid>0 ";
        }

        sql += "group by orderid,userid) pp group by userid) pb "
                + "on pm.userid = pb.userid";
        if (ordersum > 0) {
            //老用户
            if (ordersum == 1) {
                sql += " and pb.order_num > 1";
            } else {
                //新用户
                sql += " and pb.userid not in(select user_id from orderinfo where  " +
                " create_time <= '" + datestart + "' and state in(1,2,3,4,5))";
            }
        }
        sql += " left JOIN orderinfo on pm.orderid = orderinfo.order_no  order by pm.createtime desc,userid,orderid limit ?,?";
        Connection conn = DBHelper.getInstance().getConnection();
        //System.out.println(sql);
        try {
            int index = 1;
            stmt = conn.prepareStatement(sql);
            if (userid > 0) {
                stmt.setInt(index++, userid);
            }
            if (paytype != null && !"-1".equals(paytype)) {
                stmt.setString(index++, paytype);
            }
            if (datestart != null) {
                stmt.setString(index++, datestart);
            }
            if (dateend != null) {
                stmt.setString(index++, dateend);
            }
            if(StringUtils.isNotBlank(bean.getPayId())){
                stmt.setString(index++, bean.getPayId());
            }
            if (userid > 0) {
                stmt.setInt(index++, userid);
            }

            stmt.setInt(index++, bean.getPage());
            stmt.setInt(index++, bean.getRows());
            rs = stmt.executeQuery();
            while (rs.next()) {
                PaymentStatistics pms = new PaymentStatistics();
                pms.setCurrency(rs.getString("payment_cc"));
                pms.setOrderNo(rs.getString("orderid"));
                pms.setOrderNum(rs.getInt("order_num"));
                pms.setPaymentAmount(rs.getFloat("payment_amount"));
                pms.setPaymentTime(rs.getString("createtime"));
                pms.setPayType(rs.getString("paytype"));
                /*String paytypeStr = rs.getString("paytype");
                if ("3".equals(paytypeStr) || "4".equals(paytypeStr)) {
                    pms.setSplitFlag(1);
                }*/
                pms.setUserId(rs.getInt("userId"));
                pms.setOrderState(rs.getInt("state"));
                pms.setPayId(rs.getString("paymentid"));
                psList.add(pms);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return psList;
    }

    @Override
    public int queryPaymentStatisticsCount(PayCheckBean bean) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //select count(0) from (
        String sql = "select count(odd.orderid) from (select userid,count(orderid) as order_num,orderid,sum(payment_amount) as payment_amount,"
                + "createtime,paytype from payment where paystatus=1 and paytype in(0,1,2,5) and userid not in(select id from `user` where email like 'test%' " +
                " or email like '%qq.com' or email like '%163.com' or email like '%ww.com' or email like 'rlef%' " +
                " or email = 'undefined' or email like 'asdf%'  or email like 'importexpress@%' or email like 'ling@tes%'  " +
                " or email like '%@qq.com%' or email like '%@test%' or email like 'eee@%' or email like 'ceshi@%' " +
                " or email like '%www@%' or email like '%aaaa@%' or email like '%@import-express.com') ";
        int userid = bean.getUserid();
        String payEmail = bean.getPayEmail();
        String userEmail = bean.getEmail();
        String paytype = bean.getPaytype();
        String datestart = bean.getDataStart();
        String dateend = bean.getDataEnd();
        int ordersum = bean.getOrdersum();
        if (userid > 0) {
            sql += " and userid=?";
        } else {
            sql += " and userid>0";
        }
        if (payEmail != null && !payEmail.isEmpty()) {
            sql += " and orderdesc like ('%" + payEmail + "%')";
        }
        if (userEmail != null && !userEmail.isEmpty()) {
            sql += " and userid in  (select id from user where email like '%" + userEmail + "%' )";
        }
        if (paytype != null && !"-1".equals(paytype)) {
            sql += " and paytype=?";
        }
        if (datestart != null && !datestart.isEmpty()) {
            sql += " and createtime>=?";
        }
        if (dateend != null && !dateend.isEmpty()) {
            sql += " and createtime<=?";
        }
        if(StringUtils.isNotBlank(bean.getPayId())){
            sql += " and paymentid = ?";
        }
        sql += " group by orderid ";
        sql += " order by createtime desc) odd INNER JOIN ";
        sql += "(select userid,count(userid) as order_num from (select userid,count(orderid) "
                + "from payment where paystatus=1  "
                + "and LENGTH(orderid) = 16 ";
        if (userid > 0) {
            sql += "and userid= " + userid;
        } else {
            sql += "and userid > 0";
        }
        sql += " group by orderid) pp group by userid) pb "
                + "on odd.userid = pb.userid";
        if (ordersum > 0) {
            //老用户
            if (ordersum == 1) {
                sql += " and pb.order_num > 1";
            } else {
                //新用户
                //sql += " and pb.order_num = 1";
                sql += " and pb.userid not in(select payment.userid  from payment,orderinfo where  "
                        + " payment.createtime < '" + datestart + "' and payment.paystatus=1 and payment.paytype in(0,1,2,5)  "
                        + " and payment.orderid = orderinfo.order_no and orderinfo.state in(1,2,3,4,5) )";
            }
        }
        //sql += " inner JOIN orderinfo on odd.orderid = orderinfo.order_no and  orderinfo.state in(1,2,3,4,5) GROUP BY odd.userid)a";
        //System.out.println(sql);
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            int index = 1;
            stmt = conn.prepareStatement(sql);
            if (userid > 0) {
                stmt.setInt(index++, userid);
            }
            if (paytype != null && !"-1".equals(paytype)) {
                stmt.setString(index++, paytype);
            }
            if (datestart != null) {
                stmt.setString(index++, datestart);
            }
            if (dateend != null) {
                stmt.setString(index++, dateend);
            }
            if(StringUtils.isNotBlank(bean.getPayId())){
                stmt.setString(index++, bean.getPayId());
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return 0;
    }

    @Override
    public List<PaymentDetails> queryPaymentDetails(String orderNo) {

        List<PaymentDetails> pdList = new ArrayList<PaymentDetails>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select orderid,paymentid,payment_amount,payment_cc,createtime,paytype,orderdesc "
                + "from payment where paystatus=1 and orderid=? ";
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PaymentDetails pds = new PaymentDetails();
                pds.setCurrency(rs.getString("payment_cc"));
                pds.setOrderNo(rs.getString("orderid"));
                pds.setPaymentAmount(rs.getFloat("payment_amount"));

                String orderDesc = rs.getString("orderdesc");
                orderDesc = orderDesc == null ? "" : orderDesc;
                pds.setOrderDesc(orderDesc);
                pds.setPaymentNo(rs.getString("paymentid"));
                pds.setPaymentTime(rs.getString("createtime"));
                pds.setPayType(rs.getInt("paytype"));
                pdList.add(pds);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return pdList;
    }


    @Override
    public PaymentDetails queryBalancePayment(String orderNo) {

        PaymentDetails pds = new PaymentDetails();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select orderid,paymentid,payment_amount,payment_cc,createtime,paytype,orderdesc "
                + "from payment where paystatus=1 and paytype = '2' and orderid=? ";
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {

                pds.setCurrency(rs.getString("payment_cc"));
                pds.setOrderNo(rs.getString("orderid"));
                pds.setPaymentAmount(rs.getFloat("payment_amount"));

                String orderDesc = rs.getString("orderdesc");
                orderDesc = orderDesc == null ? "" : orderDesc;
                if (orderDesc.indexOf("@") > -1 && orderDesc.indexOf(".") > -1) {
                    pds.setPaymentEmail(orderDesc);
                } else {
                    pds.setPaymentEmail("");
                }
                pds.setPaymentNo(rs.getString("paymentid"));
                pds.setPaymentTime(rs.getString("createtime"));
                pds.setPayType(rs.getInt("paytype"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return pds;
    }

    @Override
    public List<OrderPaymentBean> getOrderPayList(int userid, int page) {
        String sql = " select sql_calc_found_rows a.user_id,a.create_time,a.order_no,a.pay_price,"
                + "b.payment_amount,c.payment_all from "
                + "(select o.create_time,func_get_split_string(order_no,'_',1) as order_no,o.user_id,sum(if(o.state=-1 or o.state=6,0.0,o.pay_price)/e.exchange_rate) as pay_price "
                + "from orderinfo o ,exchange_rate e where  o.currency=e.country and  "
                + "(o.state=-1 or (o.state>0 and o.state<7)) and o.user_id=?  group by func_get_split_string(order_no,'_',1)) as a ,"
                + " (select  func_get_split_string(o.orderid,'_',1) as orderid,sum(o.payment_amount/e.exchange_rate) as payment_amount "
                + "from  payment o,exchange_rate e where  o.payment_cc=e.country and  o.paystatus=1 and "
                + "(o.paytype='0' or o.paytype='1' or o.paytype='2') and o.userid=?  group by func_get_split_string(o.orderid,'_',1)) as b ,"
                + "(select  func_get_split_string(o.orderid,'_',1) as orderid,sum(o.payment_amount/e.exchange_rate) as payment_all "
                + "from  payment o,exchange_rate e where  o.payment_cc=e.country and  o.paystatus=1 and "
                + "(o.paytype='0' or o.paytype='1') and o.userid=?  group by func_get_split_string(o.orderid,'_',1)) as c  "
                + "where a.order_no=b.orderid and c.orderid=b.orderid  order by a.create_time desc limit ?,20 ";//order by a.create_time desc


        String sql2 = "select sum(price) as price,func_get_split_string(remark_id,'_',1) as remark_id,balanceAfter from recharge_record where  userid=? and "
                + " func_get_split_string(remark_id,'_',1) in (?) and (type=1 or remark like '%cancel%' or remark like '%取消%')  "
                + " group by func_get_split_string(reamrk_id,'_',1)";//order by datatime


        //status 状态 0-申请退款 1-销售同意退款 2-退款完结 3-管理员同意退款  -1-销售驳回退款 -2 -客户取消退款 -3-管理员拒绝退款
        String sql3 = "select  sum(account) as account,orderid "
                + "from refund where type=1 and userid=? and orderid in(?)";


        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet st = null;
        PreparedStatement stmt2 = null;
        ResultSet st2 = null;
        PreparedStatement stmt3 = null;
        ResultSet st3 = null;
        PreparedStatement stmt4 = null;
        ResultSet st4 = null;
        List<OrderPaymentBean> list = new ArrayList<OrderPaymentBean>();
        StringBuilder remarkid = new StringBuilder();
        int page_total = 0;
        Map<String, Double> cancelMap = new HashMap<String, Double>();
        Map<String, Double> balanceMap = new HashMap<String, Double>();
        Map<String, Double> refundMap = new HashMap<String, Double>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setInt(2, userid);
            stmt.setInt(3, userid);
            stmt.setInt(4, (page - 1) * 20);
            st = stmt.executeQuery();
            stmt2 = conn.prepareStatement("select found_rows();");
            st2 = stmt2.executeQuery();
            if (st2.next()) {
                page_total = st2.getInt("found_rows()");
            }
            OrderPaymentBean bean = null;
            while (st.next()) {
                bean = new OrderPaymentBean();
                bean.setAdditionalBalance("0");
                bean.setCountTotal(page_total);
                bean.setDateTime(st.getString("create_time"));
                String order_no = st.getString("order_no");
                bean.setOrderNo(order_no);
                bean.setPayment(format.format(Double.valueOf(st.getString("payment_all"))));
                bean.setPayPrice(format.format(Double.valueOf(st.getString("pay_price"))));
                bean.setPaymentAmount(format.format(Double.valueOf(st.getString("payment_amount"))));
                bean.setUserid(st.getString("user_id"));
                bean.setCancelOrder("0.00");
                bean.setRefund("0.00");
                bean.setBalance("0.00");
                remarkid.append(",'").append(order_no).append("'");
                list.add(bean);
            }
            String remark_id = remarkid.toString();
            remark_id = remark_id.startsWith(",") ? remark_id.substring(1) : remark_id;
            if (StringUtils.isNotBlank(remark_id)) {
                stmt3 = conn.prepareStatement(sql2.replace("(?)", "(" + remark_id + ")"));
                stmt3.setInt(1, userid);
                st3 = stmt3.executeQuery();
                while (st3.next()) {
                    cancelMap.put(st3.getString("remark_id"), st3.getDouble("price"));
                    balanceMap.put(st3.getString("remark_id"), Double.valueOf(com.cbt.util.StrUtils.object2PriceStr(st3.getString("balanceAfter"))));

                }
                stmt4 = conn.prepareStatement(sql3.replace("(?)", "(" + remark_id + ")"));
                stmt4.setInt(1, userid);
                st4 = stmt4.executeQuery();
                while (st4.next()) {
                    refundMap.put(st4.getString("orderid"), st4.getDouble("account"));
                }
            }
            for (OrderPaymentBean payBean : list) {
                Double cancelPrice = cancelMap.get(payBean.getOrderNo());
                Double banlance = balanceMap.get(payBean.getOrderNo());
                Double refund = refundMap.get(payBean.getOrderNo());
                payBean.setCancelOrder(cancelPrice == null ? "0.00" : format.format(cancelPrice));
                payBean.setBalance(banlance == null ? "0.00" : format.format(banlance));
                payBean.setRefund(refund == null ? "0.00" : format.format(refund));
                //付款总金额
                String paymentAmount = payBean.getPaymentAmount();
                //实际完成金额
                String payPrice = payBean.getPayPrice();
                //订单取消金额
                String cancel = payBean.getCancelOrder();

                String isBalance = format.format(Double.valueOf(paymentAmount) - Double.valueOf(cancel));

                payBean.setIsBalance(isBalance.equals(payPrice) ? 0 : 1);

            }
        } catch (SQLException e) {
            LOG.warn("",e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st2 != null) {
                try {
                    st2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmt3 != null) {
                try {
                    stmt3.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st3 != null) {
                try {
                    st3.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmt4 != null) {
                try {
                    stmt4.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (st4 != null) {
                try {
                    st4.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> payDetailByUserid(int userid, int page) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String temporary_table = "temporary_pay_detail_" + userid;
        //删除临时表
        String strDrop = "DROP TABLE IF EXISTS  " + temporary_table;
        //创建临时表
        String strCreate = "CREATE temporary  TABLE " + temporary_table + " ("
                + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                + "`type` varchar(2) DEFAULT NULL,"
                + "`createtime` datetime DEFAULT NULL,"
                + "`remark` varchar(2000) DEFAULT NULL ,"
                + "`currency` varchar(500) DEFAULT NULL,"
                + "`money_in` varchar(20) DEFAULT NULL ,"
                + "`money_out` varchar(20) DEFAULT NULL,  "
                + " PRIMARY KEY (`id`),"
                + " KEY `createtime` (`createtime`) USING BTREE"
                + ")ENGINE=MEMORY DEFAULT CHARSET=utf8";

        //插入付款信息
        String sqlpay = "insert into " + temporary_table + "(type,createtime,remark,currency,money_in)"
                + "(select paytype,createtime,orderid,payment_cc,payment_amount  "
                + "from payment where (paytype='0' or paytype='1') "
                + "and paystatus=1 and userid=?)";

        //插入补偿信息
        String sqladd = "insert into " + temporary_table + "(type,createtime,remark,currency,money_in)"
                + "(select '2',createtime,remark,'USD',money  "
                + "from additional_balance  where valid=1 and state=1 and userid=?)";


        //插入订单消费信息
        String sqlorder = "insert into " + temporary_table + "(type,createtime,remark,currency,money_out)"
                + "(select '3',orderpaytime,order_no,currency,pay_price "
                + "from  orderinfo where (state>0 and state<6) and user_id=?)";

        //已完结退款
        String sqlref1 = "insert into " + temporary_table + "(type,createtime,remark,currency,money_out)"
                + "(select '4',endtime,remark,currency,account "
                + "from  refund where status=2 and valid=1 and account>0.009 and userid=?)";

        //提现处理中
        String sqlref2 = "insert into " + temporary_table + "(type,createtime,remark,currency,money_out)"
                + "(select '7',apptime,IFNULL(remark,'提现正在处理中...'),currency,account "
                + "from  refund where status>-1 and status<2 and valid=1 and account>0.009 and type=0 and userid=?)";

        //payple申诉中
//		String sqlref3 = "insert into "+temporary_table+"(type,createtime,remark,currency,money_out)"
//				+ "(select '6',apptime,IFNULL(remark,'paypal申诉正在处理中...'),currency,account "
//				+ "from  refund where status>-1 and status<2 and valid=1 and account>0.009 and type=1 and userid=?)";


        //余额变更信息
        String recharge_record_in = "insert into " + temporary_table + "(type,createtime,remark,currency,money_out)"
                + "(select '8' as type,datatime,remark_id,currency,price from recharge_record where userid = ? " +
                "and usesign  =1 and LENGTH(remark_id) > 6)";
        String recharge_record_out = "insert into " + temporary_table + "(type,createtime,remark,currency,money_in)"
                + "(select '9' as type,datatime,remark_id,currency,price from recharge_record where userid = ? " +
                "and usesign in(0,2) and LENGTH(remark_id) > 6)";


        //按照时间排序取出所有数据
        String sqlall = "select sql_calc_found_rows type,createtime,remark,currency,money_out,money_in"
                + " from " + temporary_table + " order by createtime asc,type asc limit ?,20";

        Connection conn = null;
        PreparedStatement stmtDrop = null;
        PreparedStatement stmtCreate = null;
        PreparedStatement stmtPay = null;
        PreparedStatement stmtAdd = null;
        PreparedStatement stmtOrder = null;
        PreparedStatement stmtAll = null;
        PreparedStatement stmtref1 = null;
        PreparedStatement stmtref2 = null;
        PreparedStatement stmtref3 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        int count = 0;
        try {

            conn = DBHelper.getInstance().getConnection();
            conn.setAutoCommit(false);
            //删除临时表
            stmtDrop = conn.prepareStatement(strDrop);
            stmtDrop.execute();
            //创建临时表
            stmtCreate = conn.prepareStatement(strCreate);
            stmtCreate.execute();

            stmtPay = conn.prepareStatement(sqlpay);
            stmtPay.setInt(1, userid);
            stmtPay.executeUpdate();

            stmtAdd = conn.prepareStatement(sqladd);
            stmtAdd.setInt(1, userid);
            stmtAdd.executeUpdate();

            stmtOrder = conn.prepareStatement(sqlorder);
            stmtOrder.setInt(1, userid);
            stmtOrder.executeUpdate();

            stmtref1 = conn.prepareStatement(sqlref1);
            stmtref1.setInt(1, userid);
            stmtref1.executeUpdate();

            stmtref2 = conn.prepareStatement(sqlref2);
            stmtref2.setInt(1, userid);
            stmtref2.executeUpdate();

//			stmtref3 = conn.prepareStatement(sqlref3);
//			stmtref3.setInt(1, userid);
//			stmtref3.executeUpdate();

            //recharge_record表信息
            stmtref2 = conn.prepareStatement(recharge_record_in);
            stmtref2.setInt(1, userid);
            stmtref2.executeUpdate();

            stmtref2 = conn.prepareStatement(recharge_record_out);
            stmtref2.setInt(1, userid);
            stmtref2.executeUpdate();
            //recharge_record表信息


            stmtAll = conn.prepareStatement(sqlall);
            stmtAll.setInt(1, (page - 1) * 20);
            rs = stmtAll.executeQuery();

            stmt2 = conn.prepareStatement("select found_rows();");
            rs2 = stmt2.executeQuery();
            if (rs2.next()) {
                count = rs2.getInt("found_rows()");
            }
            Map<String, Double> sumMap = new HashMap<String, Double>(20);
            sumMap.put("USD_in", 0.00);
            sumMap.put("USD_out", 0.00);
            sumMap.put("GBP_in", 0.00);
            sumMap.put("GBP_out", 0.00);
            sumMap.put("AUD_in", 0.00);
            sumMap.put("AUD_out", 0.00);
            sumMap.put("EUR_in", 0.00);
            sumMap.put("EUR_out", 0.00);
            sumMap.put("CAD_in", 0.00);
            sumMap.put("CAD_out", 0.00);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("count", count);
                map.put("type", rs.getString("type"));
                map.put("createtime", rs.getString("createtime"));
                map.put("remark", StringUtils.isBlank(rs.getString("remark")) ? "" : rs.getString("remark").replace("\n", ""));
                map.put("currency", rs.getString("currency"));
                String moneyIn = rs.getString("money_in");
                if (StringUtils.isNotBlank(moneyIn)) {
                    map.put("money_in", format.format(Double.valueOf(moneyIn)) + " " + rs.getString("currency"));
                    Double currencyMoneyAllIn = sumMap.get(rs.getString("currency") + "_in");
                    currencyMoneyAllIn = currencyMoneyAllIn + Double.valueOf(moneyIn);
                    sumMap.put(rs.getString("currency") + "_in", currencyMoneyAllIn);

                } else {
                    map.put("money_in", "");
                }
                String moneyOut = rs.getString("money_out");
                if (StringUtils.isNotBlank(moneyOut)) {
                    map.put("money_out", "-" + format.format(Double.valueOf(moneyOut)) + " " + rs.getString("currency"));
                    Double currencyMoneyAllOut = sumMap.get(rs.getString("currency") + "_out");
                    currencyMoneyAllOut = currencyMoneyAllOut + Double.valueOf(moneyOut);
                    sumMap.put(rs.getString("currency") + "_out", currencyMoneyAllOut);
                } else {
                    map.put("money_out", "");
                }

                result.add(map);
            }
            //删除临时表
            stmtDrop.execute();
            conn.commit();
            //GBP:0.00;AUD:0.00  USD:1411.61;EUR:0.00;CAD:0.00;
            Map<String, Object> map = new HashMap<String, Object>();
            Iterator<Entry<String, Double>> iterator = sumMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Double> entry = (Entry<String, Double>) iterator.next();
                String key = entry.getKey();
                Double value = entry.getValue();
                value = value == null ? 0 : value;
                map.put(key, format.format(value));
            }
            result.add(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmtDrop != null) {
                try {
                    stmtDrop.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtCreate != null) {
                try {
                    stmtCreate.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtPay != null) {
                try {
                    stmtPay.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtAdd != null) {
                try {
                    stmtAdd.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtOrder != null) {
                try {
                    stmtOrder.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtAll != null) {
                try {
                    stmtAll.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtref1 != null) {
                try {
                    stmtref1.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtref2 != null) {
                try {
                    stmtref2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmtref3 != null) {
                try {
                    stmtref3.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    LOG.warn("",e);
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public List<OrderBean> queryOrderInfoByUserId(int userId) {

        List<OrderBean> list = new ArrayList<OrderBean>();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select o.*,(SELECT COUNT(1) FROM tblacklist a INNER JOIN order_address b ON a.blackVlue=b.address2 WHERE b.orderNo=o.order_no AND a.flag=0) as backAddressCount " +
                "from orderinfo o  where o.user_id = ? and o.state !=0 order by o.create_time desc";
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {

                OrderBean info = new OrderBean();
                info.setOrderNo(rs.getString("order_no"));
                info.setCashback(rs.getDouble("cashback"));
                info.setCoupon_discount(rs.getDouble("coupon_discount"));
                info.setCurrency(rs.getString("currency"));
                info.setDiscount_amount(rs.getDouble("discount_amount"));
                info.setExtra_discount(rs.getDouble("extra_discount"));
                info.setExtra_freight(rs.getDouble("extra_freight"));
                info.setForeign_freight(rs.getString("foreign_freight"));
                info.setOrder_ac(rs.getDouble("order_ac"));
                info.setPay_price(rs.getDouble("pay_price"));
                info.setPay_price_three(rs.getString("pay_price_three"));
                info.setProduct_cost(rs.getString("product_cost"));
                info.setState(rs.getInt("state"));
                info.setCreatetime(rs.getString("create_time"));
                info.setBackAddressCount(rs.getInt("backAddressCount"));
                list.add(info);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<RechangeRecord> queryRechangeRecordByUserId(int userId) {

        List<RechangeRecord> list = new ArrayList<RechangeRecord>();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select * from recharge_record  where userid = ? order by datatime desc";
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {

                RechangeRecord info = new RechangeRecord();
                info.setUserId(rs.getInt("userid"));
                info.setPrice(rs.getDouble("price"));
                //info.setType(rs.getInt("type"));
                info.setRemark(rs.getString("remark"));
                info.setCurrency(rs.getString("currency"));
                info.setRemarkId(rs.getString("remark_id"));
                info.setDataTime(rs.getString("datatime"));
                info.setAdminUser(rs.getString("adminuser"));
                info.setUseSign(rs.getInt("usesign"));
                info.setBalanceAfter(rs.getDouble("balanceAfter"));
                list.add(info);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<AddBalanceInfo> quertAddBalanceInfoByUserId(int userId) {

        List<AddBalanceInfo> list = new ArrayList<AddBalanceInfo>();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select * from additional_balance  where userid = ? order by createtime desc";
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {

                AddBalanceInfo info = new AddBalanceInfo();
                info.setUserId(rs.getInt("userid"));
                info.setMoney(rs.getDouble("money"));
                //info.setType(rs.getInt("type"));
                info.setRemark(rs.getString("remark"));
                info.setAdmin(rs.getString("admin"));
                info.setCreatetime(rs.getString("createtime"));
                list.add(info);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<RefundBean> quertRefundByUserId(int userId) {

        List<RefundBean> rfbList = new ArrayList<RefundBean>();

        String _sql = "select  * from refund where userid= ? and status in(0,1,2,3,4)";

        Connection conn = DBHelper.getInstance().getConnection2();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(_sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                RefundBean rfb = new RefundBean();
                int _appStatus = rs.getInt("status");
                String agr = "";
                if (_appStatus == 0) {
                    agr = "还未同意退款";
                } else if (_appStatus == -2) {
                    agr = "客户已取消";
                } else if (_appStatus == -1) {
                    agr = "拒绝";
                } else if (_appStatus == 1) {
                    agr = "银行处理中";
                } else {
                    agr = "已完结 ";
                }
                rfb.setStatus(_appStatus);
                rfb.setId(rs.getInt("id"));
                rfb.setUserid(rs.getInt("userid"));
                rfb.setAppcount(rs.getDouble("appcount"));
                int refundtype = rs.getInt("refundtype");
                String refundType = "";
                if (refundtype == 1) {
                    refundType = "产品无货";
                } else if (refundtype == 2) {
                    refundType = "无法发货";
                } else if (refundtype == 3) {
                    refundType = "交期延误";
                } else {
                    refundType = refundtype == 4 ? "客户申请" : "其他原因";
                    refundType = refundtype == 0 ? "" : "其他原因";
                }
                rfb.setRefundType(refundType);
                rfb.setApptime(rs.getString("apptime"));
                rfb.setAgreetime(rs.getString("agreetime"));
                rfb.setFeedback(rs.getString("feedback"));
                rfb.setRefuse(rs.getString("refuse"));
                rfb.setAccount(rs.getDouble("account"));
                rfb.setOrderid(rs.getString("orderid"));
                rfb.setPayid(rs.getString("payid"));
                rfb.setAdditionid(rs.getInt("additionid"));
                rfb.setPaypalstate(rs.getInt("paypalstate"));
                rfb.setReasoncode(rs.getString("reasoncode"));
                rfb.setReasonnote(rs.getString("reasonnote"));
                rfb.setCasetype(rs.getString("casetype"));
                rfb.setComplainId(rs.getInt("complainid"));
                rfb.setOutcomeCode(rs.getString("outcomecode"));
                if (rs.getString("endtime") == null) {
                    rfb.setEndtime("");
                } else {
                    rfb.setEndtime(rs.getString("endtime"));
                }
                rfb.setAgreetime(rs.getString("agreetime"));

                rfb.setAgree(agr);
                String remark = rs.getString("remark");
                rfb.setRemark(remark == null ? "" : remark.trim());
                if (rs.getString("paypalname") == "付款时记录") {
                    rfb.setPaypalname("");
                } else {
                    rfb.setPaypalname(rs.getString("paypalname"));
                }
                rfb.setBalanceCurrency("USD");
                rfb.setCurrency(rs.getString("currency"));
                rfbList.add(rfb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rfbList;
    }

    @Override
    public List<PaymentBean> queryPayMentInfoByUserId(int userId,int type) {
        List<PaymentBean> list = new ArrayList<PaymentBean>();
        StringBuffer sql = new StringBuffer("select * from payment where 1=1 ");
        sql.append(" and paystatus = 1 and paytype = 0 and userid = ?");
        if(type == 1){
            sql.append(" and createtime >= DATE_SUB(NOW(), INTERVAL 1 MONTH) ");
        }else if(type == 2){
            sql.append(" and createtime >= DATE_SUB(NOW(), INTERVAL 2 MONTH) ");
        }else if(type == 3){
            sql.append(" and createtime >= DATE_SUB(NOW(), INTERVAL 3 MONTH) ");
        }
        sql.append(" order by createtime desc");
        Connection conn = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql.toString());
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PaymentBean payment = new PaymentBean();
                payment.setUserid(rs.getInt("userid"));
                payment.setOrderid(rs.getString("orderid"));
                payment.setPaymentid(rs.getString("paymentid"));
                payment.setPayment_other(rs.getString("payment_other"));
                payment.setPayment_amount(rs.getString("payment_amount"));
                payment.setPayment_cc(rs.getString("payment_cc"));
                payment.setOrderdesc(rs.getString("orderdesc"));
                payment.setUsername(rs.getString("username"));
                payment.setPaystatus(rs.getString("paystatus"));
                payment.setCreatetime(rs.getString("createtime"));
                payment.setPaySID(rs.getString("paySID"));
                payment.setPayflag(rs.getString("payflag"));
                payment.setPaytype(rs.getString("paytype"));
                list.add(payment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }

        return list;
    }

    @Override
    public RefundBean queryRefundBeanById(int rfId, int userId) {

        RefundBean rfb = new RefundBean();

        String _sql = "select  * from refund where userid= ? and id = ? and status in(0,1,2,3)";

        Connection conn = DBHelper.getInstance().getConnection2();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(_sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, rfId);
            rs = stmt.executeQuery();
            if (rs.next()) {

                int _appStatus = rs.getInt("status");
                //状态 0-申请退款 1-销售同意退款 2-退款完结 3-管理员同意退款  -1-销售驳回退款 -2 -客户取消退款 -3-管理员拒绝退款
                String agr = "";
                if (_appStatus == 0) {
                    agr = "申请退款";
                } else if (_appStatus == -3) {
                    agr = "管理员拒绝退款";
                } else if (_appStatus == -2) {
                    agr = "客户已取消";
                } else if (_appStatus == -1) {
                    agr = "销售驳回退款";
                } else if (_appStatus == 1) {
                    agr = "销售同意退款";
                } else if (_appStatus == 2) {
                    agr = "退款完结";
                } else if (_appStatus == 3) {
                    agr = "管理员同意退款";
                } else {
                    agr = "已完结 ";
                }
                rfb.setStatus(_appStatus);
                rfb.setId(rs.getInt("id"));
                rfb.setUserid(rs.getInt("userid"));
                rfb.setAppcount(rs.getDouble("appcount"));
                int refundtype = rs.getInt("refundtype");
                String refundType = "";
                if (refundtype == 1) {
                    refundType = "产品无货";
                } else if (refundtype == 2) {
                    refundType = "无法发货";
                } else if (refundtype == 3) {
                    refundType = "交期延误";
                } else {
                    refundType = refundtype == 4 ? "客户申请" : "其他原因";
                    refundType = refundtype == 0 ? "" : "其他原因";
                }
                rfb.setRefundType(refundType);
                rfb.setApptime(rs.getString("apptime"));
                rfb.setAgreetime(rs.getString("agreetime"));
                rfb.setFeedback(rs.getString("feedback"));
                rfb.setRefuse(rs.getString("refuse"));
                rfb.setAccount(rs.getDouble("account"));
                rfb.setOrderid(rs.getString("orderid"));
                rfb.setPayid(rs.getString("payid"));
                rfb.setAdditionid(rs.getInt("additionid"));
                rfb.setPaypalstate(rs.getInt("paypalstate"));
                rfb.setReasoncode(rs.getString("reasoncode"));
                rfb.setReasonnote(rs.getString("reasonnote"));
                rfb.setCasetype(rs.getString("casetype"));
                rfb.setComplainId(rs.getInt("complainid"));
                rfb.setOutcomeCode(rs.getString("outcomecode"));
                if (rs.getString("endtime") == null) {
                    rfb.setEndtime("");
                } else {
                    rfb.setEndtime(rs.getString("endtime"));
                }
                rfb.setAgreetime(rs.getString("agreetime"));

                rfb.setAgree(agr);
                String remark = rs.getString("remark");
                rfb.setRemark(remark == null ? "" : remark.trim());
                if (rs.getString("paypalname") == "付款时记录") {
                    rfb.setPaypalname("");
                } else {
                    rfb.setPaypalname(rs.getString("paypalname"));
                }
                rfb.setBalanceCurrency("USD");
                rfb.setCurrency(rs.getString("currency"));
                rfb.setAccount(rs.getDouble("account"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rfb;
    }

    @Override
    public RechangeRecord querySystemCancelOrder(String orderNo) {

        RechangeRecord record = new RechangeRecord();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select * from recharge_record  where remark_id = ? and type = 1 and remark like '%system closeOrder%' limit 1";
        Connection conn = DBHelper.getInstance().getConnection();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                record.setUserId(rs.getInt("userid"));
                record.setPrice(rs.getDouble("price"));
                //info.setType(rs.getInt("type"));
                record.setRemark(rs.getString("remark"));
                record.setCurrency(rs.getString("currency"));
                record.setRemarkId(rs.getString("remark_id"));
                record.setDataTime(rs.getString("datatime"));
                record.setAdminUser(rs.getString("adminuser"));
                record.setUseSign(rs.getInt("usesign"));
                record.setBalanceAfter(rs.getDouble("balanceAfter"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return record;
    }

}
