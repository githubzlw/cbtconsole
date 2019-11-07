package com.cbt.website.dao;

import com.cbt.auto.service.IOrderAutoService;
import com.cbt.auto.service.PreOrderAutoService;
import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.onlinesql.ctr.SaveSyncTable;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.GoodsDistribution;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.report.vo.UserBehaviorBean;
import com.cbt.report.vo.UserBehaviorDetails;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.OrderDetailsConstantUtil;
import com.cbt.util.OrderInfoConstantUtil;
import com.cbt.util.Utility;
import com.cbt.warehouse.pojo.Shipment;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.PaymentConfirm;
import com.cbt.website.bean.QualityResult;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;
import com.cbt.website.bean.UserBehavior;
import com.importExpress.pojo.GoodsCarActiveSimplBean;
import com.importExpress.service.impl.SendMQServiceImpl;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.mysql.jdbc.JDBC4PreparedStatement;
import net.sf.json.JSONArray;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

// 订单管理功能的订单查询过程
//由 孙秀文 2016-01-26 优化
public class OrderwsDao implements IOrderwsDao {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderwsDao.class);
    String gdsUrl = null;
    @SuppressWarnings("rawtypes")
    List grlist = new ArrayList();

    @Override
    public String getAliPid(String goods_pid) {
        StringBuilder sb = new StringBuilder();
        final String sql = "select goods_pid,shop_id from ali_info_data where 1688_pid='" + goods_pid + "' limit 1";
        Connection conn = DBHelper.getInstance().getConnection5();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("goods_pid")).append("&").append(rs.getString("shop_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try{
                if(rs!=null){
                    rs.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return sb.toString();
    }

    @Override
    public List<OrderBean> getOrders(int userID, int state, Date startdate, Date enddate, String username, String email,
                                     String orderno, String phone, int startpage, int page, int admuserid, int buyid, int showUnpaid,
                                     int status) {
        /*
         * String sql =
         * " select sql_calc_found_rows distinct  o.user_id,ifnull((select admuser.admName from admuser as admuser where admuser.id=ifnull(aru.adminid,0)),'') as adminname,"
         * +
         * " name,o.orderid,order_no,delivery_time,product_cost,create_time,o.state,'' address,'' address2,ifnull((select max(zone.country) from zone ,"
         * +
         * "	order_address oa where zone.id=oa.country and oa.orderno=o.order_no),'') countrys,' ' statename,'' phoneNumber,'' street,'' zipcode,"
         * +
         * "	packag_style,mode_transport,domestic_freight,foreign_freight,(ifnull(pay_price,0)-ifnull(pay_price_three,0)) as service_fee,o.client_update,"
         * +
         * "	o.server_update,actual_ffreight,ip,email,purchase_number,details_number,ifnull((select distinct min(createtime)  from payment p where p.orderid=o.order_no and  p.paystatus=1 ),create_time) as createtime,"
         * +
         * " (select count(*) from (select left(d.order_no,16) ordercnt ,d.user_id   from orderinfo d where d.state<6 and d.state>0  group by d.user_id, left(d.order_no,16) ) as  act  where act.user_id=o.user_id) as order_count, "
         * + "o.currency," +
         * "	(select count(id) from order_change oc where oc.orderno=o.order_no and roptype = 5 and oldvalue= 1) oc_c, ifnull(order_buy.buyuser,'') as buyer"
         *
         * + " from  orderinfo   o " +
         * "  #left join admin_r_user  aru on  aru.userid=o.user_id " +
         * " @left join order_buy  on  o.order_no =order_buy.orderid  where 1=1 "
         * ; //+
         * ", (select count(user_id) from orderinfo where  user_id=u.id group by user_id) as count"
         * //+ "	from user u " //+
         * "	left join  admin_r_user   aru on (aru.useremail=u.email or aru.userid=u.id) , orderinfo o left join order_buy  on  o.order_no =order_buy.orderid  where o.user_id=u.id  and 1=1 "
         * ;
         */
        String sql = "select sql_calc_found_rows distinct   o.* ,ifnull(aru.admName,'') adminname,user.name username,user.email , ifnull(order_buy.buyuser,'') as buyer, func_get_split_string(mode_transport,'@',3) as countrys,'' address,'' address2,' ' statename,'' phoneNumber,'' street,'' zipcode,"
                + " (select count(id) from order_change oc where oc.orderno=o.order_no and roptype = 5 and oldvalue= 1) oc_c,"
                + " ifnull((select distinct min(createtime)  from payment p where p.orderid=o.order_no and  p.paystatus=1 ),create_time) as createtime,"
                + " (select count(*) from (select left(d.order_no,16) ordercnt ,d.user_id   from orderinfo d where d.state<6 and d.state>0  group by d.user_id, left(d.order_no,16) ) as  act  where act.user_id=o.user_id) as order_count,(CASE  (ifnull(pay_price,0)-ifnull(pay_price_three,0))>0 when   true then  (ifnull(pay_price,0)-ifnull(pay_price_three,0)) else 0 end )   service_fee1"
                + " ,o.packag_style as changenum from orderinfo    o  inner join user  on o.user_id=user.id  "
                + " #left  join admin_r_user  aru on  aru.userid=o.user_id "
                + " @left join order_buy  on  o.order_no =order_buy.orderid where  1=1 ";

        int i = 1;
        if (status == -1) {
            sql = sql
                    + " and o.user_id not in(select oi.user_id from  orderinfo oi,admin_r_user aru where oi.user_id=aru.userid)";
        }
        if (state != -2) {
            sql = sql + " and o.state=?";
        }
        if (startdate != null) {
            sql = sql + " and to_days(o.create_time) > to_days(?) ";
        }
        if (enddate != null) {
            sql = sql + " and to_days(o.create_time) < to_days(?) ";
        }
        if (userID != 0) {
            sql = sql + " and o.user_ID=? ";
        }
        if (admuserid != 0) {
            sql = sql + " and aru.adminid=?";
            sql = sql.replace("#left", "inner ");
        }

        if (buyid != 0) {
            sql = sql + " and order_buy.buyid=?";
            sql = sql.replace("@left", "inner ");
        }

        if (Utility.getStringIsNull(username)) {
            sql = sql + " and  name = ? ";
        }
        if (Utility.getStringIsNull(email)) {
            sql = sql + " and  email = ? ";
        }
        if (Utility.getStringIsNull(orderno)) {
            sql = sql + " and  order_no = ? ";
        }
        if (Utility.getStringIsNull(phone)) {
            sql = sql + " and  phone_number = ? ";
        }
        /*
         * if(i == 1){ sql = sql +
         * " and   to_days(create_time) = to_days(now())"; }
         */
        if (showUnpaid == 0) {
            sql = sql + " and o.state <> 0  ";
        }
        sql = sql + "   order by createtime  desc  limit ?, ?";
        sql = sql.replace("#left", "left ");
        sql = sql.replace("@left", "left ");
        // System.out.println(sql);
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        ResultSet rs2 = null;
        PreparedStatement stmt2 = null;
        int total = 0;
        List<OrderBean> oblist = new ArrayList<OrderBean>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement("select found_rows();");
            i = 0;
            if (state != -2) {
                i = i + 1;
                stmt.setInt(i, state);
            }
            if (startdate != null) {
                i = i + 1;
                // Timestamp start = new Timestamp(startdate.getTime());
                // stmt.setTimestamp(i, start);
                stmt.setDate(i, new java.sql.Date(startdate.getTime()));
            }
            if (enddate != null) {
                i = i + 1;
                // Timestamp end = new Timestamp(enddate.getTime());
                // stmt.setTimestamp(i, end);
                stmt.setDate(i, new java.sql.Date(enddate.getTime()));
            }

            if (userID != 0) {
                i = i + 1;
                stmt.setInt(i, userID);
            }
            if (admuserid != 0) {
                i = i + 1;
                stmt.setInt(i, admuserid);
            }
            if (buyid != 0) {
                i = i + 1;
                stmt.setInt(i, buyid);
            }
            if (Utility.getStringIsNull(username)) {
                i = i + 1;
                stmt.setString(i, username);
            }
            if (Utility.getStringIsNull(email)) {
                i = i + 1;
                stmt.setString(i, email);
            }
            if (Utility.getStringIsNull(orderno)) {
                i = i + 1;
                stmt.setString(i, orderno);
            }
            if (Utility.getStringIsNull(phone)) {
                i = i + 1;
                stmt.setString(i, phone);
            }
            stmt.setInt(i + 1, startpage);
            stmt.setInt(i + 2, page);

            rs = stmt.executeQuery();
            rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                total = rs2.getInt("found_rows()");
            }
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            while (rs.next()) {
                OrderBean ob = new OrderBean();
                ob.setTotal(total);
                ob.setUserid(rs.getInt("user_id"));
                ob.setId(rs.getInt("orderid"));
                ob.setOrderNo(rs.getString("order_no"));
                ob.setProduct_cost(rs.getString("product_cost"));
                ob.setCreatetime(rs.getString("createtime"));
                ob.setState(rs.getInt("state"));
                ob.setPackage_style(rs.getInt("packag_style"));
                ob.setOrder_count(rs.getInt("order_count"));
                String mode_transport = "";
                if (Utility.getStringIsNull(rs.getString("mode_transport"))) {
                    String[] mode_transport_ = rs.getString("mode_transport").split("@");
                    int isFree = 0;
                    for (int j = 0; j < mode_transport_.length; j++) {
                        if (j == mode_transport_.length - 2
                                && Utility.getIsDouble(mode_transport_[mode_transport_.length - 2])) {
                            if (Double.parseDouble(mode_transport_[mode_transport_.length - 2]) == 0) {
                                if (mode_transport_[mode_transport_.length - 1].equals("all")) {
                                    mode_transport_[mode_transport_.length - 1] = "免邮";
                                    isFree = 1;
                                } else {
                                    mode_transport_[mode_transport_.length - 1] = "不知重量";
                                    isFree = 1;
                                }
                            }
                        }
                        if (j == mode_transport_.length - 1) {
                            if (mode_transport_[j].equals("product")) {
                                mode_transport_[j] = "未付运费";
                            } else if (mode_transport_[j].equals("all")) {
                                if (isFree != 1) {
                                    mode_transport_[j] = "全付";
                                }
                            }
                        }
                        mode_transport += mode_transport_[j];
                        if (mode_transport.indexOf(".") > -1) {
                            mode_transport = mode_transport.split("\\.")[0];
                        }
                        if (j != mode_transport_.length - 1)
                            mode_transport += "@";
                    }
                }
                ob.setMode_transport(mode_transport);
                ob.setService_fee(rs.getString("service_fee"));
                ob.setDomestic_freight(rs.getString("domestic_freight"));
                String foreign_freight_ = rs.getString("foreign_freight");
                ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
                ob.setClient_update(rs.getInt("client_update"));
                int server_update = rs.getInt("server_update");
                if (rs.getInt("oc_c") > 0) {
                    server_update = 2;
                }
                ob.setServer_update(server_update);
                Address address = new Address();
                address.setAddress(rs.getString("address"));
                address.setStreet(rs.getString("street"));
                address.setAddress2(rs.getString("address2"));
                address.setCountry(rs.getString("countrys"));
                address.setPhone_number(rs.getString("phonenumber"));
                address.setZip_code(rs.getString("zipcode"));
                address.setStatename(rs.getString("statename"));
                ob.setActual_ffreight(rs.getString("actual_ffreight"));
                ob.setAddress(address);
                ob.setService_fee(rs.getString("service_fee1"));
                ob.setUserName(rs.getString("username"));
                ob.setIp(rs.getString("ip"));
                ob.setEmail(rs.getString("email"));
                ob.setPurchase_number(rs.getInt("purchase_number"));
                ob.setDetails_number(rs.getInt("details_number"));
                ob.setAdminname(rs.getString("adminname"));
                ob.setCurrency(rs.getString("currency"));
                Date date = rs.getDate("createtime");
                int delivery_time = rs.getInt("delivery_time");
                String details_pay = "";
                if (delivery_time != 0 && date != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    c.add(Calendar.DATE, delivery_time);
                    details_pay = f.format(c.getTime());
                }
                ob.setDetails_pay(details_pay);
                ob.setDeliveryTime(delivery_time);
                ob.setBuyuser(rs.getString("buyer"));
                ob.setChangenum(rs.getInt("changenum"));
                ob.setIsDropshipOrder(rs.getInt("isDropshipOrder"));
                oblist.add(ob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closePreparedStatement(stmt2);
            DBHelper.getInstance().closeConnection(conn);
        }

        return oblist;
    }

    @Override
    public int getOrdersPage(int userID, int state, Date time, String username, String email, String orderno,
                             String phone) {
        String sql = "select count(orderid) from orderinfo o where 1=1 ";
        int i = 1;
        if (state != -2) {
            sql = sql + " and state=?";
        }
        if (time != null) {
            sql = sql + " and to_days(create_time) = to_days(?) ";
        }
        if (userID != 0) {
            sql = sql + " and user_ID=? ";
        }

        if (username != null) {
            if (!username.equals("")) {
                sql = sql + " and  user_id=(select id from user where name = ?) ";
            }
        }
        if (email != null) {
            if (!email.equals("")) {
                sql = sql + " and  user_id=(select id from user where email = ?) ";
            }
        }
        if (orderno != null) {
            if (!orderno.equals("")) {
                sql = sql + " and  order_no = ? ";
            }
        }
        if (Utility.getStringIsNull(phone)) {
            sql = sql + " and  phone_number = ? ";
        }
        /*
         * if(i == 1){ sql = sql +
         * " and   to_days(create_time) = to_days(now())"; }
         */
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            i = 0;
            if (state != -2) {
                i = i + 1;
                stmt.setInt(i, state);
            }
            if (time != null) {
                i = i + 1;
                Timestamp ts = new Timestamp(time.getTime());
                stmt.setTimestamp(i, ts);
            }
            if (userID != 0) {
                i = i + 1;
                stmt.setInt(i, userID);
            }
            if (username != null) {
                if (!username.equals("")) {
                    i = i + 1;
                    stmt.setString(i, username);
                }
            }
            if (email != null) {
                if (!email.equals("")) {
                    i = i + 1;
                    stmt.setString(i, email);
                }
            }
            if (orderno != null) {
                if (!orderno.equals("")) {
                    i = i + 1;
                    stmt.setString(i, orderno);
                }
            }
            if (Utility.getStringIsNull(phone)) {
                i = i + 1;
                stmt.setString(i, phone);
            }
            System.err.println(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }

        return res;
    }

    @Override
    public int upOrderDeatail(int orderDatailId, int state) {
        String sql = "update order_details set state=?  where id = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, state);
            stmt.setInt(2, orderDatailId);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int upOrderDeatailstate(String orderId, int state) {
        String sql = "update order_details set state=?  where orderid = ? and state!=2";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, state);
            stmt.setString(2, orderId);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public TaoBaoOrderInfo getShipStatusInfo(String tb_1688_itemid, String last_tb_1688_itemid, String time,
                                             String admName, String shipno, int offline_purchase, String orderid, int goodsid) {
        TaoBaoOrderInfo t = new TaoBaoOrderInfo();
        String sql = "";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (offline_purchase == 1) {
                sql = "SELECT DISTINCT t.tbOr1688,t.shipstatus,t.shipno,t.support_info FROM taobao_1688_order_history t where t.importorderid='"
                        + orderid + "' and t.importgoodsid='" + goodsid + "'";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    t.setShipno(rs.getString("shipno"));
                    t.setShipstatus(rs.getString("shipstatus"));
                    t.setTbOr1688(rs.getString("tbOr1688"));
                    t.setSupport_info(StringUtils.isStrNull(rs.getString("support_info"))
                            || rs.getString("support_info").length() < 5 ? "" : rs.getString("support_info"));
                }
            } else {
                sql = "select tb_1688_itemid,createtime from order_replenishment where orderid=? and goodsid=? and tb_1688_itemid is not null order by id desc limit 1";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, orderid);
                stmt.setInt(2, goodsid);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    String re_tb_1688_itemid = rs.getString("tb_1688_itemid");
                    sql = "SELECT DISTINCT t.tbOr1688,t.shipstatus,t.shipno,t.support_info FROM taobao_1688_order_history t  "
                            + " WHERE t.itemid='" + re_tb_1688_itemid + "' " + " AND t.username='" + admName
                            + "' and t.delivery_date>='" + rs.getString("createtime")
                            + "' order by t.delivery_date asc limit 1";
                    stmt = conn.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        t.setShipno(rs.getString("shipno"));
                        t.setShipstatus(rs.getString("shipstatus"));
                        t.setTbOr1688(rs.getString("tbOr1688"));
                        t.setSupport_info(StringUtils.isStrNull(rs.getString("support_info"))
                                || rs.getString("support_info").length() < 5 ? "" : rs.getString("support_info"));
                    }
                } else {
                    sql = "SELECT DISTINCT t.tbOr1688,t.shipstatus,t.shipno,t.support_info FROM taobao_1688_order_history t INNER JOIN order_product_source os ON "
                            + "t.itemid = os.tb_1688_itemid OR t.itemid = os.last_tb_1688_itemid WHERE (os.tb_1688_itemid='"
                            + tb_1688_itemid + "' OR last_tb_1688_itemid='" + last_tb_1688_itemid + "') "
                            + " AND t.username='" + admName + "' and t.delivery_date>='" + time
                            + "' and os.confirm_userid=(select adminid from tb_1688_accounts where account='" + admName
                            + "' and del=1) order by t.delivery_date asc limit 1";
                    stmt = conn.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        t.setShipno(rs.getString("shipno"));
                        t.setShipstatus(rs.getString("shipstatus"));
                        t.setTbOr1688(rs.getString("tbOr1688"));
                        t.setSupport_info(StringUtils.isStrNull(rs.getString("support_info"))
                                || rs.getString("support_info").length() < 5 ? "" : rs.getString("support_info"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return t;
    }
    @Override
    public void updateGoodsCarMessage(String orderNo) {
        String sql = "update order_change set is_read=1 where orderNo='" + orderNo + "'";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
    }

    @Override
    public double getAllFreightByOrderid(String orderid) {
        double allFreight = 0.00;
        double allWeight = 0.00;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT cast(ifnull(SUM(freight),0.00) as decimal(10,2)) as allFreight,cast(ifnull(SUM(perWeight*googs_number),0.00) as decimal(10,2)) as allWeight FROM goods_car WHERE id IN (SELECT goodsid FROM order_details WHERE orderid=?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                allWeight = rs.getDouble("allWeight");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return allWeight;
    }

    @Override
    public List<OrderDetailsBean> getOrdersDetails(String orderNo) {
        String sql = "select DISTINCT oi.isDropshipOrder,adr.country,ifnull(cast(getExchangeRMB(ors.goods_price*(ifnull(buycount,1)),ifnull(ors.currency,'USD'))as DECIMAL(10,2)),0) as sumGoods_price,"
                + " IFNULL((ors.goods_p_price*(ifnull(ors.buycount,1))),0)as sumGoods_p_price,"
                + " ors.purchase_state orsstate,od.od_state,od.shipno, 6 as ropType,ors.goods_p_url newValue,ors.last_goods_p_url AS lastValue,ors.tb_1688_itemid,ors.last_tb_1688_itemid,ors.confirm_time,ors.confirm_userid,"
                + " ors.goods_p_price oldValue,ors.buycount,ors.del del_state,od.userid,od.id oid,od.goodsname,od.checkprice_fee,od.state,od.paytime,od.goodsid,"
                + " car_img,od.car_url as car_url, format(od.goodsprice,2) as goodsprice,od.yourorder,goodsfreight,od.remark,od.delivery_time,od.Actual_price,od.Actual_freight,od.Actual_weight, od.od_bulk_volume,od.od_total_weight,"
                + " od.Actual_volume,od.goodsid,od.extra_freight,od.fileupload,od.freight_free,od.purchase_state,ifnull(od.purchase_time,ors.purchasetime) as purchase_time,purchase_confirmation,car_type,"
                + " (SELECT z.id FROM zone z WHERE REPLACE(z.country,' ','')=REPLACE(adr.country,' ','')) new_zid ,"
                +"( SELECT COUNT(DISTINCT ods.goods_pid)*5 FROM order_details ods  WHERE ods.orderid=od.orderid AND ods.state in (0,1)) as pid_amount,"
                + " date_format(od.createtime,'%Y-%m-%d') as create_time,gt.img goods_typeimg, ors.bargainRemark,ors.deliveryRemark,ors.colorReplaceRemark,ors.sizeReplaceRemark,ors.orderNumRemarks,ors.questionsRemarks,ors.unquestionsRemarks,ors.againRemarks, od.remark,pa.sprice,dropshipid,checked,id.goodstatus,"
                + " case when od.seilUnit=1 then gc.seilUnit else od.seilUnit end as seilUnit,gci.context as goods_info,dso.state as dropshipstate,ifnull(ors.shop_id,'') as shop_id,ors.is_replenishment,"
                + " od.goods_pid,od.car_urlMD5,ors.offline_purchase, "
                + " cbr.weight cbrWeight,cbr.final_weight,cbr.wholesale_price cbrPrice,cbrd.wholesale_price cbrdPrice,cbr.ali_price,cbr.ali_pid alipid"
                + " from order_details od "
                + " left join order_address adr on od.orderid=adr.orderNo"
                + " left join order_product_source ors on ors.goodsid=od.goodsid and  od.orderid=ors.orderid "
                + " left join goods_typeimg gt on od.goodsid= gt.goods_id "
                + " left join preferential_application pa on  od.goodsid= pa.goodsid  "
                + " left join id_relationtable id on od.goodsid=id.goodid and id.orderid=od.orderid and id.is_replenishment=1"
                + "  left join zone z ON adr.country=z.country " + "  left join orderinfo oi ON oi.order_no=od.orderid "
                + " left join dropshiporder dso on od.dropshipid = dso.child_order_no"
                + " left join goods_car gc on od.goodsid = gc.id"
                + " left join goods_communication_info gci on od.orderid=gci.orderid and od.goodsid=gci.goodsid"
                + " LEFT join custom_benchmark_ready  cbr ON cbr.pid = od.goods_pid "
                + " LEFT join custom_benchmark_ready_delete  cbrd ON cbrd.pid = od.goods_pid "
                + " where od.orderid=?  order by dropshipid, car_url,od.id";
        LOG.info("查询订单详情：" + sql);
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<OrderDetailsBean> spiderlist = new ArrayList<OrderDetailsBean>();
        try {
            String sql2 = "select ropType,oldValue,newValue,del_state,goodId,dateline from order_change where orderNo=?  and  (((ropType  BETWEEN 2 AND 4)  and del_state=0 ) or (ropType=1 ) or (ropType=5 )) order by id desc  ";

            stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, orderNo);
            rs2 = stmt2.executeQuery();
            Map<String, ArrayList<Object[]>> changInfo = new HashMap<String, ArrayList<Object[]>>();
            while (rs2.next()) {
                int ropType = rs2.getInt("ropType");
                int del_state = rs2.getInt("del_state");
                String values = rs2.getString("newValue");
                String oldValue = rs2.getString("oldValue");
                int goodid = rs2.getInt("goodId");
                if (ropType == 1) {
                    if (del_state == 1) {
                        values = "原始价格:" + oldValue;

                    }
                }
                if (ropType == 5) {
                    if (Utility.getStringIsNull(oldValue)) {
                        if (oldValue.equals("1")) {
                            values = rs2.getString("dateline") + "   公司：" + values;
                        } else {
                            values = rs2.getString("dateline") + "   客户：" + values;
                        }
                    }
                }
                if (changInfo.get("order" + goodid) == null) {
                    ArrayList<Object[]> order_change = new ArrayList<Object[]>();
                    order_change.add(new Object[]{ropType, del_state, values, oldValue, rs2.getString("goodId")});
                    changInfo.put("order" + goodid, order_change);
                } else {
                    changInfo.get("order" + goodid).add(new Object[]{ropType, del_state, values});
                }
            }

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                OrderDetailsBean odb = new OrderDetailsBean();

                //ali link
                String aliLink = "";
                String alipid = Utility.getStringIsNull(rs.getString("alipid")) ? rs.getString("alipid") : "0";
                if (!"0".equals(alipid)) {
                    aliLink = "https://www.aliexpress.com/item/aaa/" + alipid + ".html";
                }
                odb.setAlipid(aliLink);

                // 16 88 的重 量
                String weitht1688 = Utility.getStringIsNull(rs.getString("cbrWeight")) ? rs.getString("cbrWeight")
                        : "0";
                // 1688的价格
                String price1688 = Utility.getStringIsNull(rs.getString("cbrPrice")) ? rs.getString("cbrPrice") : "0";
                if("0".equals(price1688) &&  Utility.getStringIsNull(rs.getString("cbrdPrice"))){
                    price1688=rs.getString("cbrdPrice");
                }
                odb.setWeight1688(weitht1688);
                odb.setPrice1688(price1688);
                odb.setFinal_weight(rs.getString("final_weight"));
                odb.setPid_amount(rs.getDouble("pid_amount"));
                // 拆分采购备注
                String oremark = "";
                // 新增 到账
                odb.setOd_bulk_volume(rs.getString("od_bulk_volume"));
                odb.setAli_price(
                        StringUtils.isStrNull(rs.getString("ali_price")) ? "非精确对标" : rs.getString("ali_price"));
                odb.setOd_total_weight(rs.getDouble("od_total_weight"));
                odb.setCountry(
                        StringUtils.isStrNull(rs.getString("new_zid")) ? rs.getString("country") : rs.getString("new_zid"));
                odb.setSumGoods_price(rs.getFloat("sumGoods_price")); // 金额
                odb.setOffline_purchase(rs.getInt("offline_purchase"));// 是否线下采购
                odb.setSumGoods_p_price(rs.getFloat("sumGoods_p_price")); // 金额
                // 数量*价格
                odb.setOrsstate(rs.getInt("orsstate")); // 状态 用来判断状态大于1才算金额
                odb.setShipno(rs.getString("shipno"));
                odb.setUserid(rs.getInt("od.userid"));
                odb.setOrderid(orderNo);
                odb.setId(rs.getInt("oid"));
                odb.setCheckproduct_fee(rs.getInt("od.checkprice_fee"));
                int state = rs.getInt("od.state");
                odb.setState(state);
                int od_state = rs.getInt("od_state");// 获取替换货源的状态
                odb.setOd_state(od_state);
                odb.setPaytime(rs.getString("od.paytime"));
                int yourorder = rs.getInt("od.yourorder");
                String goodssprice = rs.getString("goodsprice");
                odb.setYourorder(yourorder);
                odb.setGoodsprice(goodssprice);
                int carId = rs.getInt("goodsid");
                odb.setGoodsid(carId);
                odb.setFreight(rs.getString("goodsfreight"));
                odb.setExtra_freight(rs.getDouble("od.extra_freight"));
                odb.setGoodsname(rs.getString("goodsname"));
                // odb.setGoods_url(rs.getString("car_url"));
                String goods_pid = rs.getString("od.goods_pid") == null ? "0" : rs.getString("od.goods_pid");
                odb.setGoods_pid(goods_pid);// yyl;
                odb.setCar_type(rs.getString("car_type") == null ? "" : rs.getString("car_type"));
                String car_urlMD5 = rs.getString("car_urlMD5");
                String es_price=price1688;
                if(state ==1 || state == 0){
                    es_price=StringUtil.getEsPrice(es_price);
                }else{
                    es_price="0.00";
                }
                System.out.println("goodsid="+rs.getInt("goodsid")+" \t es_price="+es_price+"\t yourorder=="+yourorder);
                odb.setEs_price(Double.valueOf(es_price)*yourorder);
                String shop_id = "";
                List<String> shop_id_b = new ArrayList<String>();
                if (rs.getInt("isDropshipOrder") == 3) {
                    shop_id = "";
                } else {
                    shop_id = rs.getString("shop_id");
                    if (StringUtils.isStrNull(shop_id) || "-".equals(shop_id)) {
                        shop_id = "0000";
                    }
                    if (rs.getInt("is_replenishment") == 1) {
                        shop_id_b = getBhShopId(orderNo, rs.getInt("goodsid"));

                    }
                }
                odb.setShop_id(shop_id);
                odb.setBh_shop_id(shop_id_b);
                String buy_url = "";
                if (!StringUtils.isStrNull(rs.getString("car_url"))) {
                    buy_url = rs.getString("car_url");
                } else if (!StringUtils.isStrNull(rs.getString("lastValue"))) {
                    buy_url = rs.getString("lastValue");
                } else if (!StringUtils.isStrNull(rs.getString("newValue"))) {
                    buy_url = rs.getString("newValue");
                } else {
                    buy_url = "";
                }
                buy_url=StringUtil.getNewUrl(buy_url,goods_pid,car_urlMD5);
                odb.setGoods_url(buy_url);
                // 转换图片小图到大图
                String car_img = rs.getString("car_img");
                if (car_img == null || "".equals(car_img)) {
                    odb.setGoods_img(car_img);
                } else {
                    if (car_img.indexOf(".jpg") != car_img.lastIndexOf(".jpg")) {
                        car_img = car_img.substring(0, car_img.indexOf(".jpg") + ".jpg".length());
                    } else if (car_img.indexOf("32x32") > -1) {
                        car_img = car_img.replace("32x32", "400x400");
                    } else if (car_img.indexOf("60x60") > -1) {
                        car_img = car_img.replace("60x60", "400x400");
                    }
                    odb.setGoods_img(car_img);
                }
                odb.setGoods_freight(rs.getString("goodsfreight"));
                odb.setDelivery_time(rs.getString("delivery_time"));
                odb.setBuycount(rs.getInt("buycount"));
                odb.setFreight_free(rs.getInt("freight_free"));
                odb.setGoods_type(rs.getString("car_type"));
                odb.setGoods_info(rs.getString("goods_info"));
                // 存储商品备注
                odb.setRemark(rs.getString("remark"));
                odb.setNewsourceurl(rs.getString("newValue"));
                odb.setOldsourceurl(rs.getString("lastValue"));
                // 存储采购备注
                // 拆分采购备注
                if (StrUtils.isNotNullEmpty(rs.getString("bargainRemark"))) {
                    oremark += "砍价情况:" + rs.getString("bargainRemark") + " ,";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("deliveryRemark"))) {
                    oremark += "交期偏长:" + rs.getString("deliveryRemark") + " ,";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("colorReplaceRemark"))) {
                    oremark += "颜色替换:" + rs.getString("colorReplaceRemark") + " ,";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("sizeReplaceRemark"))) {
                    oremark += "尺寸替换:" + rs.getString("sizeReplaceRemark") + " ,";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("orderNumRemarks"))) {
                    oremark += "订量问题:" + rs.getString("orderNumRemarks") + " ,";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("questionsRemarks"))) {
                    oremark += "有疑问备注:" + rs.getString("questionsRemarks") + " ,";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("unquestionsRemarks"))) {
                    oremark += "无疑问备注:" + rs.getString("unquestionsRemarks") + " ,";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("againRemarks"))) {
                    oremark += "再次备注:" + rs.getString("againRemarks") + " ,";
                }
                odb.setOremark(oremark);
                gdsUrl = rs.getString("car_url");
                // odb.setDelivery_time(rs.getString("od.delivery_time"));
                odb.setActual_price(rs.getDouble("od.Actual_price"));
                odb.setActual_freight(rs.getDouble("od.Actual_freight"));
                odb.setActual_weight(rs.getString("od.Actual_weight"));
                odb.setActual_volume(rs.getString("od.Actual_volume"));
                // 临时替代产品金额
                odb.setSourc_price(rs.getString("oldValue"));
                // lzj end
                odb.setChange_price("");
                odb.setChange_delivery("");
                // odb.setNewsourceurl("");
                odb.setIscancel(0);
                odb.setRemark(rs.getString("od.remark"));
                odb.setPurchase_confirmation(rs.getString("purchase_confirmation"));
                odb.setPurchase_state(rs.getInt("purchase_state"));
                Date purchase_timeDate = rs.getDate("purchase_time");
                odb.setPurchase_time(purchase_timeDate == null ? "" : f.format(purchase_timeDate));
                odb.setCreatetime(rs.getString("create_time"));
                odb.setImg_type(rs.getString("goods_typeimg"));
                odb.setDropshipid(rs.getString("dropshipid"));
                odb.setChecked(rs.getInt("checked"));
                odb.setTb_1688_itemid(rs.getString("tb_1688_itemid"));
                odb.setLast_tb_1688_itemid(rs.getString("last_tb_1688_itemid"));
                odb.setConfirm_time(rs.getString("confirm_time"));
                odb.setConfirm_userid(rs.getString("confirm_userid"));
                odb.setGoodstatus(rs.getInt("goodstatus"));
                // odb.setFfreight(rs.getDouble("freight"));
                odb.setIsDropshipOrder(rs.getInt("isDropshipOrder"));
                odb.setGoods_pid(goods_pid);
                // odb.setOldUrl("https://www.import-express.com/spider/getSpider?url="
                // + TypeUtils.encodeGoods(gdsUrl));// 速卖通链接转换为我司链接
                odb.setOldUrl(rs.getString("car_url"));// 我司链接
                double sprice = rs.getDouble("pa.sprice");// 批量优惠前金额，原价
                if (!(rs.getString("car_urlMD5") == null || "".equals(rs.getString("car_urlMD5")))) {
                    if (rs.getString("car_urlMD5").startsWith("D")) {
                        odb.setMatch_url("https://detail.1688.com/offer/" + (rs.getString("goods_pid")) + ".html");
                    } else if (rs.getString("car_urlMD5").startsWith("A")) {
                        odb.setMatch_url("http://www.aliexpress.com/item/a/" + rs.getString("goods_pid") + ".html");
                    }
                }

                // dropship订单状态
                odb.setDropShipState(rs.getString("dropshipstate"));
                try {

                    String seilUnit_ = rs.getString("seilUnit");
                    seilUnit_ = seilUnit_.trim();
                    if (seilUnit_.indexOf("(") > -1) {
                        seilUnit_ = seilUnit_.substring(seilUnit_.indexOf("("));
                    }
                    odb.setSeilUnit(seilUnit_);
                    odb.setGoodsUnit(seilUnit_);
                } catch (Exception e) {
                    LOG.info("获取单位失败,单位不存在：" + orderNo);
                }

                if (sprice != 0 && state != 2) {
                    double goodssprice_d = Double.parseDouble(goodssprice);
                    odb.setPreferential_price(new BigDecimal((sprice - goodssprice_d) * yourorder)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }

                if (changInfo.get("order" + carId) != null) {
                    List<Object[]> changeInfoList = changInfo.get("order" + carId);
                    for (int i = 0; i < changeInfoList.size(); i++) {
                        int ropType = (Integer) changeInfoList.get(i)[0];
                        int del_state = (Integer) changeInfoList.get(i)[1];
                        String values = (String) changeInfoList.get(i)[2];
                        if (ropType == 1) {
                            odb.setChange_price(values);
                        } else if (ropType == 2) {
                            odb.setChange_delivery(values);
                        } else if (ropType == 3) {
                            odb.setChange_number(values);
                        } else if (ropType == 4) {
                            odb.setIscancel(1);
                        } else if (ropType == 5) {
                            if (odb.getChange_communication() == null) {
                                List<String> communications = new ArrayList<String>();
                                communications.add(values);
                                odb.setChange_communication(communications);
                            } else {
                                odb.getChange_communication().add(values);
                            }
                            odb.setRopType(ropType);
                            odb.setDel_state(del_state);
                        }
                    }
                }
                spiderlist.add(odb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
            // DBHelper.getInstance().closeConnection(conn2);
        }
        return spiderlist;
    }

    public static List<String> getBhShopId(String orderid, int goodsid) {
        List<String> shop_ids = new ArrayList<String>();
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT distinct shop_id FROM order_replenishment WHERE orderid=? AND goodsid=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            stmt.setInt(2, goodsid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String shop_id = rs.getString("shop_id");
                shop_id = shop_id.split("\\//")[1].split("\\.")[0];
                shop_ids.add(shop_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return shop_ids;
    }

    // 获取dropship子订单的 cjc12.09 getChildrenOrdersDetails
    @Override
    public List<OrderDetailsBean> getChildrenOrdersDetails(String orderNo) {

        String sql = "select adr.country,ifnull(cast(getExchangeRMB(ors.goods_price*(ifnull(buycount,1)),ifnull(ors.currency,'USD'))as DECIMAL(10,2)),0) as sumGoods_price,"
                + "IFNULL((ors.goods_p_price*(ifnull(buycount,1))),0)as sumGoods_p_price,"
                + "ors.purchase_state orsstate,od.od_state, 6 as ropType,ors.goods_p_url newValue,"
                + " ors.goods_p_price oldValue,ors.buycount,ors.del del_state,  od.delivery_time,od.userid,od.id oid,od.goodsname,od.checkprice_fee,od.state,od.paytime,od.goodsid,"
                + " car_img,car_url, od.goodsprice,od.yourorder,goodsfreight,od.remark,od.delivery_time,od.Actual_price,od.Actual_freight,od.Actual_weight, od.od_bulk_volume,od.od_total_weight,"
                + " od.Actual_volume,od.goodsid,od.extra_freight,od.fileupload,od.freight_free,od.purchase_state,purchase_time,purchase_confirmation,car_type,"
                + " date_format(od.createtime,'%Y-%m-%d') as create_time,gt.img goods_typeimg, 	ors.bargainRemark,ors.deliveryRemark,ors.colorReplaceRemark,ors.sizeReplaceRemark,ors.orderNumRemarks,ors.questionsRemarks,ors.unquestionsRemarks,ors.againRemarks, od.remark,pa.sprice,dropshipid,checked,id.goodstatus,gc.freight ,od.goods_pid"
                + " ,cbr.weight cbrWeight,cbr.wholesale_price cbrPrice,od.car_urlMD5 " + " from order_details od "
                + " left join order_address adr on od.orderid=adr.orderNo"
                + " left join order_product_source ors on ors.goodsid=od.goodsid and  od.orderid=ors.orderid "
                + " left join goods_typeimg gt on od.goodsid= gt.goods_id "
                + " left join preferential_application pa on  od.goodsid= pa.goodsid  "
                + " left join id_relationtable id on od.goodsid=id.goodid  "
                + "LEFT JOIN goods_car gc ON gc.id = od.goodsid"
                + " LEFT join custom_benchmark_ready  cbr ON cbr.pid = od.goods_pid"
                + " where od.dropshipid=?  order by car_url,od.id";
        LOG.info("查询drop子订单订单详情：" + sql);
        Connection conn = DBHelper.getInstance().getConnection();
        // Connection conn2 = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<OrderDetailsBean> spiderlist = new ArrayList<OrderDetailsBean>();
        try {
            String sql2 = "select ropType,oldValue,newValue,del_state,goodId,dateline from order_change where orderNo=?  and  (((ropType  BETWEEN 2 AND 4)  and del_state=0 ) or (ropType=1 ) or (ropType=5 )) order by id desc  ";

            stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, orderNo);
            rs2 = stmt2.executeQuery();
            Map<String, ArrayList<Object[]>> changInfo = new HashMap<String, ArrayList<Object[]>>();
            while (rs2.next()) {
                int ropType = rs2.getInt("ropType");
                int del_state = rs2.getInt("del_state");
                String values = rs2.getString("newValue");
                String oldValue = rs2.getString("oldValue");
                int goodid = rs2.getInt("goodId");
                if (ropType == 1) {
                    if (del_state == 1) {
                        values = "原始价格:" + oldValue;

                    }
                }
                if (ropType == 5) {
                    if (Utility.getStringIsNull(oldValue)) {
                        if (oldValue.equals("1")) {
                            values = rs2.getString("dateline") + "   公司：" + values;
                        } else {
                            values = rs2.getString("dateline") + "   客户：" + values;
                        }
                    }
                }
                if (changInfo.get("order" + goodid) == null) {
                    ArrayList<Object[]> order_change = new ArrayList<Object[]>();
                    order_change.add(new Object[]{ropType, del_state, values, oldValue, rs2.getString("goodId")});
                    changInfo.put("order" + goodid, order_change);
                } else {
                    changInfo.get("order" + goodid).add(new Object[]{ropType, del_state, values});
                }
            }

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                OrderDetailsBean odb = new OrderDetailsBean();

                // 1688的重量
                String weitht1688 = Utility.getStringIsNull(rs.getString("cbrWeight")) ? rs.getString("cbrWeight")
                        : "0";
                // 1688的价格
                String price1688 = Utility.getStringIsNull(rs.getString("cbrPrice")) ? rs.getString("cbrPrice") : "0";
                //无效代码（jack）
//                if (price1688.indexOf("$") > -1) {
//                    price1688.replaceAll("\\s*", "").replaceAll("\\$", "￥");
//                }
                odb.setWeight1688(weitht1688);
                odb.setPrice1688(price1688);

                String oremark = "";
                // 新增 到账
                odb.setOd_bulk_volume(rs.getString("od_bulk_volume"));
                odb.setOd_total_weight(rs.getDouble("od_total_weight"));
                odb.setCountry(rs.getString("country"));
                odb.setSumGoods_price(rs.getFloat("sumGoods_price")); // 金额
                // 数量*价格
                odb.setSumGoods_p_price(rs.getFloat("sumGoods_p_price")); // 金额
                // 数量*价格
                odb.setOrsstate(rs.getInt("orsstate")); // 状态 用来判断状态大于1才算金额
                odb.setUserid(rs.getInt("od.userid"));
                odb.setOrderid(orderNo);
                odb.setId(rs.getInt("oid"));
                odb.setCheckproduct_fee(rs.getInt("od.checkprice_fee"));
                int od_state = rs.getInt("od_state");// 获取替换货源的状态
                odb.setOd_state(od_state);
                int state = rs.getInt("od.state");
                odb.setState(state);
                odb.setPaytime(rs.getString("od.paytime"));
                int yourorder = rs.getInt("od.yourorder");
                String goodssprice = rs.getString("od.goodsprice");
                odb.setYourorder(yourorder);
                odb.setGoodsprice(goodssprice);
                int carId = rs.getInt("goodsid");
                String goods_pid = rs.getString("goods_pid");// yyl
                odb.setGoods_pid(goods_pid);
                odb.setGoodsid(carId);
                odb.setFreight(rs.getString("goodsfreight"));
                odb.setExtra_freight(rs.getDouble("od.extra_freight"));
                odb.setGoodsname(rs.getString("goodsname"));
                odb.setGoods_url(rs.getString("car_url"));
                odb.setGoods_img(rs.getString("car_img"));
                odb.setGoods_freight(rs.getString("goodsfreight"));
                odb.setDelivery_time(rs.getString("delivery_time"));
                odb.setBuycount(rs.getInt("buycount"));
                odb.setFreight_free(rs.getInt("freight_free"));
                odb.setGoods_type(rs.getString("car_type"));
                // 存储商品备注
                odb.setRemark(rs.getString("remark"));
                odb.setNewsourceurl(rs.getString("newValue"));
                // 存储采购备注
                // 拆分采购备注
                if (StrUtils.isNotNullEmpty(rs.getString("bargainRemark"))) {
                    oremark += "砍价情况:" + rs.getString("bargainRemark") + ",";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("deliveryRemark"))) {
                    oremark += "交期偏长:" + rs.getString("deliveryRemark") + ",";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("colorReplaceRemark"))) {
                    oremark += "颜色替换:" + rs.getString("colorReplaceRemark") + ",";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("sizeReplaceRemark"))) {
                    oremark += "尺寸替换:" + rs.getString("sizeReplaceRemark") + ",";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("orderNumRemarks"))) {
                    oremark += "订量问题:" + rs.getString("orderNumRemarks") + ",";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("questionsRemarks"))) {
                    oremark += "有疑问备注:" + rs.getString("questionsRemarks") + ",";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("unquestionsRemarks"))) {
                    oremark += "无疑问备注:" + rs.getString("unquestionsRemarks") + ",";
                }
                if (StrUtils.isNotNullEmpty(rs.getString("againRemarks"))) {
                    oremark += "再次备注:" + rs.getString("againRemarks") + ",";
                }
                odb.setOldUrl(oremark);
                gdsUrl = rs.getString("car_url");
                odb.setDelivery_time(rs.getString("od.delivery_time"));
                odb.setActual_price(rs.getDouble("od.Actual_price"));
                odb.setActual_freight(rs.getDouble("od.Actual_freight"));
                odb.setActual_weight(rs.getString("od.Actual_weight"));
                odb.setActual_volume(rs.getString("od.Actual_volume"));
                // 临时替代产品金额
                odb.setSourc_price(rs.getString("oldValue"));
                // lzj end
                odb.setChange_price("");
                odb.setChange_delivery("");
                // odb.setNewsourceurl("");
                odb.setIscancel(0);
                odb.setRemark(rs.getString("od.remark"));
                odb.setPurchase_confirmation(rs.getString("purchase_confirmation"));
                odb.setPurchase_state(rs.getInt("purchase_state"));
                Date purchase_timeDate = rs.getDate("purchase_time");
                odb.setPurchase_time(purchase_timeDate == null ? "" : f.format(purchase_timeDate));
                odb.setCreatetime(rs.getString("create_time"));
                odb.setImg_type(rs.getString("goods_typeimg"));
                odb.setDropshipid(rs.getString("dropshipid"));// 获取dropship 的id
                odb.setChecked(rs.getInt("checked")); // 获取商品是否校验
                odb.setGoodstatus(rs.getInt("goodstatus"));
                odb.setFfreight(rs.getDouble("freight"));
                if (!(rs.getString("car_urlMD5") == null || "".equals(rs.getString("car_urlMD5")))) {
                    if (rs.getString("car_urlMD5").startsWith("D")) {
                        odb.setMatch_url("https://detail.1688.com/offer/" + (rs.getString("goods_pid")) + ".html");
                    } else if (rs.getString("car_urlMD5").startsWith("A")) {
                        odb.setMatch_url("http://www.aliexpress.com/item/a/" + rs.getString("goods_pid") + ".html");
                    }
                }
                // 优惠的金额
                double sprice = rs.getDouble("pa.sprice");// 批量优惠前金额，原价
                if (sprice != 0 && state != 2) {
                    double goodssprice_d = Double.parseDouble(goodssprice);
                    odb.setPreferential_price(new BigDecimal((sprice - goodssprice_d) * yourorder)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }

                if (changInfo.get("order" + carId) != null) {
                    List<Object[]> changeInfoList = changInfo.get("order" + carId);
                    for (int i = 0; i < changeInfoList.size(); i++) {
                        int ropType = (Integer) changeInfoList.get(i)[0];
                        int del_state = (Integer) changeInfoList.get(i)[1];
                        String values = (String) changeInfoList.get(i)[2];
                        if (ropType == 1) {
                            odb.setChange_price(values);
                        } else if (ropType == 2) {
                            odb.setChange_delivery(values);
                        } else if (ropType == 3) {
                            odb.setChange_number(values);
                        } else if (ropType == 4) {
                            odb.setIscancel(1);
                        } else if (ropType == 5) {
                            if (odb.getChange_communication() == null) {
                                List<String> communications = new ArrayList<String>();
                                communications.add(values);
                                odb.setChange_communication(communications);
                            } else {
                                odb.getChange_communication().add(values);
                            }
                            odb.setRopType(ropType);
                            odb.setDel_state(del_state);
                        }
                    }
                }
                spiderlist.add(odb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
            // DBHelper.getInstance().closeConnection(conn2);
        }
        return spiderlist;
    }

//    public String findgoods(String gdsUrl) {
//        String gUrl = "";
//        String sql = "select distinct oc.newValue from order_change oc where oc.goodId in (select id from goods_car where goods_url = ?) and oc.ropType = 6";
//        Connection conn = DBHelper.getInstance().getConnection();
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        try {
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, gdsUrl);
//            rs = stmt.executeQuery();
//            while (rs.next()) {
//                gUrl = gUrl + "<a href='" + rs.getString("newValue") + "' target='block'>" + rs.getString("newValue")
//                        + "</a><br/><br/>";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            DBHelper.getInstance().closeConnection(conn);
//        }
//        return gUrl;
//    }

    @Override
    public int upOrderDeatail(int orderDatailId, String file, String weight, String volume, String actual_price,
                              String actual_freight, String file_upload) {
        String sql = "update order_details set actual_price=?,actual_freight=?,actual_weight=?,actual_volume=?,fileupload=?  where id = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, actual_price);
            stmt.setString(2, actual_freight);
            stmt.setString(3, weight);
            stmt.setString(4, volume);
            stmt.setString(5, file_upload);
            stmt.setInt(6, orderDatailId);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int upOrder(String orderNo, String actual_ffreight, String custom_discuss_other, int state, Date utilDate,
                       String actual_weight, String actual_volume, Date expect_arrive_date, String actual_allincost,
                       double remaining_price_d, double order_ac, String service_fee, String domestic_freight,
                       String mode_transport, double actual_freight_c_, float payPrice) {
        String sql = "update orderinfo set actual_ffreight=?,custom_discuss_other=?,transport_time=?,actual_weight=?,actual_volume=?,actual_allincost=?,state=?,remaining_price=?,order_ac=?,service_fee=?,domestic_freight=? ,actual_freight_c=?";
        if (expect_arrive_date != null) {
            sql += ",expect_arrive_time=?";
        }
        if (Utility.getStringIsNull(mode_transport)) {
            sql += ",mode_transport=?";
        }
        sql += " ,pay_price = CONVERT(pay_price +?,decimal(12,2)  )  where order_no = ?";
        int res = 0;

        List<String> lstValues = new ArrayList<>();
        lstValues.add(actual_ffreight);
        lstValues.add(custom_discuss_other);
        if (utilDate != null) {
            Timestamp time = new Timestamp(utilDate.getTime());
            lstValues.add(String.valueOf(time));
        } else {
            lstValues.add(null);
        }
        lstValues.add(actual_weight);
        lstValues.add(actual_volume);

        double actual_allincost_d = 0;
        if (actual_allincost != null) {
            if (!actual_allincost.trim().equals("")) {
                actual_allincost_d = Double.parseDouble(actual_allincost);
            }
        }
        lstValues.add( String.valueOf(actual_allincost_d));
        lstValues.add( String.valueOf(state));
        lstValues.add( String.valueOf(remaining_price_d));
        lstValues.add(String.valueOf(order_ac));
        lstValues.add( service_fee);
        lstValues.add( domestic_freight);
        lstValues.add( String.valueOf(actual_freight_c_));
        int i = 13;
        if (expect_arrive_date != null) {
            Timestamp time1 = new Timestamp(expect_arrive_date.getTime());
            lstValues.add( String.valueOf(time1));
            i++;
        }
        if (Utility.getStringIsNull(mode_transport)) {
            lstValues.add( String.valueOf(mode_transport));
            i++;
        }
        lstValues.add( String.valueOf(payPrice));
        lstValues.add( orderNo);

        String runSql = DBHelper.covertToSQL(sql,lstValues);
        res=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

        return res;
    }

    @Override
    public double getAllWeight(String orderNo) {
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        double allWeight =0.00;
        try{
            String sql="SELECT SUM(IFNULL(c.final_weight,0)) AS finalWeight FROM custom_benchmark_ready c " +
                    "INNER JOIN order_details od ON c.pid=od.goods_pid WHERE od.orderid='"+orderNo+"'";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                allWeight+=rs.getDouble("finalWeight");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return allWeight;
    }

    /**
     * 获取订单预估运费
     * @param orderNo
     * @return
     */
    @Override
    public double getEstimatefreight(String orderNo) {
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        double estimatefreight =0.00;
        try{
            String sql="select ifnull(estimatefreight,0) as estimatefreight from shipping_package where  orderid='"+orderNo+"' or LOCATE('"+orderNo+"',remarks)>0 and LOCATE('"+orderNo+"',orderid)<=0";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                estimatefreight+=rs.getDouble("estimatefreight");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return estimatefreight;
    }

    @Override
    public ShippingBean getShipPackmentInfo(String orderid) {
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        ShippingBean s=new ShippingBean();
        try{
            String sql="SELECT sp.transportcompany,ifnull(sp.shippingtype,'-') as shippingtype,IF(sp.actual_freight<=0,s.totalprice,sp.actual_freight) AS actual_freight,ifnull(sp.estimatefreight,0.00) as estimatefreight " +
                    "FROM shipping_package sp LEFT JOIN shipment s ON sp.expressno=s.orderNo " +
                    "WHERE sp.orderid='"+orderid+"' OR (LOCATE(CONCAT('"+orderid+"',','),sp.remarks)>0 AND sp.orderid != '"+orderid+"')";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                s.setActual_freight(rs.getString("actual_freight"));
                s.setTransportcompany(rs.getString("transportcompany"));
                s.setShippingtype(rs.getString("shippingtype"));
                s.setEstimatefreight(rs.getString("estimatefreight"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closePreparedStatement(stmt);
        }
        return s;
    }

    @Override
    public OrderBean getOrders(String orderNo) {
        String sql = "select GROUP_CONCAT(pm.paytype) AS paytypes,(select count(tb_complain.id) from tb_complain where complainState in(1,2) and  ref_orderid=order_no)complain,"
                + "0 deliver, "
                + "(select count(ops.id) from order_product_source ops  where  ((purchase_state=5  and remark!=null) or purchase_state=12)  and ops.orderid = o.order_no) purchase, "
                + "(SELECT COUNT(id) FROM order_details od WHERE od.orderid=o.order_no AND od.state<2) as countOd, "
                + "(SELECT SUM(checked) FROM order_details od WHERE od.orderid=o.order_no AND od.state<2) as checked, "
                + "(SELECT IFNULL(COUNT(id)-SUM(goodstatus),0) FROM id_relationtable WHERE orderid=o.order_no) as problem, "
                + "(SELECT COUNT(od.id) FROM order_details od LEFT JOIN id_relationtable id ON od.orderid=id.orderid AND od.goodsid=id.goodid " +
                "WHERE od.orderid=o.order_no AND od.checked=0 AND id.goodstatus=1 AND id.is_replenishment=1) as no_checked, "
                + "pf.id dzid, pf.orderno dzorderno, pf.confirmname dzconfirmname,pf.confirmtime dzconfirmtime, "
                + "a.address,o.user_id,actual_ffreight,pay_price_tow,remaining_price, actual_weight,actual_volume,custom_discuss_other,o.state,a.address2,a.country, "
                + "(select country from zone z where z.id=a.country) countryName ,  "
                + "(SELECT z.id FROM zone z WHERE REPLACE(z.country,' ','')=REPLACE(a.country,' ','')) new_zid ,"
                + "a.statename,a.phonenumber,a.zipcode,packag_style,mode_transport,expect_arrive_time,ug.grade, "
                + "actual_ffreight,arrive_time,actual_allincost,service_fee, applicable_credit,extra_freight,order_ac,date_format(o.create_time,'%Y-%m-%d') as create_time, "
                + "recipients,pay_price_three,pay_price,foreign_freight,o.currency,discount_amount,product_cost,  "
                + "(select email from admuser au where aru.adminid=au.id) adminemail,"
                + "actual_lwh,0 ordernum,ifnull(aru.admName,'') adminname,ifnull(aru.adminid,'') adminid,  actual_weight_estimate,actual_freight_c,packag_number,u.name username, "
                + "u.email,'' as buyer,exporttime,  " + " '' storagetime, " + " o.delivery_time, " + " 0  buyid, "
                + " o.orderRemark,o.actual_weight_estimate as freightFee,if(o.exchange_rate<=0,6.3,o.exchange_rate) as exchange_rate,o.cashback,o.firstdiscount,o.isDropshipOrder,o.share_discount,o.extra_discount,o.extra_discount,o.coupon_discount,o.grade_discount,u.businessName"
                + " ,(select chinapostbig from zone z where a.country=z.country) countryNameCN" + " ,vatbalance,if(sp.sweight>sp.volumeweight,sp.sweight,sp.volumeweight) as volumeweight,sp.svolume"
                + "  from orderinfo o " + " left join order_address a on o.order_no=a.orderNo "
                + "  left join zone z ON a.country=z.country" + " left join user u  on  u.id =o.user_id "
                + " left join order_fee of  on  of.orderno =o.order_no " +
                // left join order_buy on o.order_no =order_buy.orderid
                " left join  admin_r_user aru on aru.userid= o.user_id  "
                + " left join  user_grade ug on ug.id= u.grade  "
                + " left join  shipping_package sp on sp.orderid=o.order_no"
                + " LEFT JOIN payment pm ON pm.orderid=o.order_no"
                + " left join  paymentconfirm  pf on pf.orderno=o.order_no where order_no =?";

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        OrderBean ob = new OrderBean();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            // stmt.setString(2, orderNo);
            System.out.println(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                ob.setVatBalance(rs.getDouble("vatbalance"));
                String countryNameCn = Utility.getStringIsNull(rs.getString("countryNameCN"))
                        ? rs.getString("countryNameCN") : "USA";
                ob.setCountryNameCN(countryNameCn);
                ob.setDzId(rs.getInt("dzid"));
                ob.setDzConfirmname(rs.getString("dzconfirmname"));
                String dzconfirmtime = rs.getString("dzconfirmtime");
                ob.setDzConfirmtime(Utility.getStringIsNull(dzconfirmtime)
                        ? dzconfirmtime.substring(0, dzconfirmtime.indexOf(" ")) : "");
                ob.setDzOrderno(rs.getString("dzorderno"));
                ob.setUserName(rs.getString("username"));
                ob.setUserEmail(rs.getString("u.email"));
                ob.setOrderNo(orderNo);
                ob.setFirstdiscount(rs.getString("firstdiscount"));
                ob.setUserid(rs.getInt("user_id"));
                ob.setExchange_rate(rs.getString("exchange_rate"));
                ob.setGrade(rs.getString("grade"));
                ob.setActual_ffreight(rs.getString("actual_ffreight"));
                ob.setRemaining_price(rs.getDouble("remaining_price"));
                ob.setActual_weight(rs.getString("actual_weight"));
                ob.setCustom_discuss_other(rs.getString("custom_discuss_other"));
                ob.setState(rs.getInt("state"));
                ob.setVolumeweight(rs.getString("volumeweight"));
                ob.setSvolume(rs.getString("svolume"));
                String pay_price_tow = rs.getString("pay_price_tow");
                ob.setPay_price_tow(Utility.getIsDouble(pay_price_tow) ? pay_price_tow : "0");
                Timestamp time = rs.getTimestamp("expect_arrive_time");
                ob.setExpect_arrive_time(time != null ? dateFormat.format(time) : null);
                ob.setActual_volume(rs.getString("actual_volume"));
                Timestamp transport_time = rs.getTimestamp("exporttime");
                ob.setTransport_time(transport_time != null ? dateFormat.format(transport_time) : null);
                ob.setActual_ffreight(rs.getString("actual_ffreight"));
                ob.setArrive_time(rs.getString("arrive_time"));
                ob.setActual_allincost(rs.getDouble("actual_allincost"));
                ob.setActual_freight_c(rs.getDouble("actual_freight_c"));
                ob.setChecked(rs.getString("checked"));
                ob.setProblem(rs.getString("problem"));
                ob.setNo_checked(rs.getString("no_checked"));
                ob.setCountOd(rs.getString("countOd"));
                Address address = new Address();
                String country = "0";
                if (StringUtils.isStrNull(rs.getString("new_zid"))) {
                    country = StringUtils.isStrNull(rs.getString("a.country")) ? "0" : rs.getString("a.country");
                } else {
                    country = rs.getString("new_zid");
                }
                // 国家ID
                address.setId(Integer.parseInt(country));
                if(rs.getString("a.country") != null && rs.getString("a.country").contains("AFRICA")){
                    address.setCountry(StringUtils.isStrNull(rs.getString("new_zid")) ? rs.getString("countryName")
                            :rs.getString("a.country") .replace(" ", ""));// 国家名称
                }else{
                    address.setCountry(StringUtils.isStrNull(rs.getString("new_zid")) ? rs.getString("countryName")
                            :rs.getString("a.country"));// 国家名称
                }
                address.setAddress(rs.getString("address"));
                address.setPhone_number(rs.getString("phonenumber"));
                address.setZip_code(
                        StringUtils.isStrNull(rs.getString("zipcode")) || "null".equals(rs.getString("zipcode")) ? "无"
                                : rs.getString("zipcode"));
                address.setStatename(rs.getString("statename"));
                address.setAddress2(rs.getString("address2"));
                address.setRecipients(rs.getString("recipients"));
                ob.setAddress(address);
                ob.setPackage_style(rs.getInt("packag_style"));
                ob.setMode_transport(rs.getString("mode_transport"));
                String service_fee = rs.getString("service_fee");
                ob.setOrderNumber(rs.getInt("ordernum") == 1);
                ob.setService_fee(service_fee);
                ob.setApplicable_credit(rs.getDouble("applicable_credit"));
                ob.setOrder_ac(rs.getDouble("order_ac"));
                ob.setCreatetime(rs.getString("create_time"));
                ob.setCurrency(rs.getString("currency"));
                ob.setDiscount_amount(rs.getDouble("discount_amount"));
                ob.setProduct_cost(rs.getString("product_cost"));
                ob.setPay_price(Double.parseDouble(Utility.formatPrice(rs.getString("pay_price")).replaceAll(",", "")));
                String foreign_freight_ = rs.getString("foreign_freight");
                ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
                ob.setEmail(rs.getString("adminemail"));
                ob.setActual_lwh(rs.getString("actual_lwh"));
                ob.setActual_weight_estimate(rs.getDouble("actual_weight_estimate"));
                ob.setExtra_freight(rs.getDouble("extra_freight"));
                ob.setPackag_number(rs.getInt("packag_number"));
                ob.setBuyuser(rs.getString("buyer"));
                ob.setAdminname(rs.getString("adminname"));
                ob.setStoragetime(rs.getString("storagetime"));
                ob.setDeliveryTime(rs.getInt("delivery_time"));
                ob.setReminded(new int[]{rs.getInt("complain"), rs.getInt("purchase"), rs.getInt("deliver")});// 提醒，0-客户投诉，1-入库问题，2，-出库问题
                ob.setBuyid(rs.getInt("buyid"));
                ob.setOrderRemark(rs.getString("orderRemark"));
                ob.setAdminid(rs.getInt("adminid"));
                ob.setCashback(rs.getDouble("cashback"));// 200美元优惠10元的--cjc--20161104
                ob.setIsDropshipOrder(rs.getInt("isDropshipOrder"));// 获取当前订单是否是dropship
                // 订单。share_discount
                ob.setShare_discount(rs.getDouble("share_discount"));// 分享折扣的--cjc--2017111
                // extra_discount
                ob.setExtra_discount(rs.getDouble("extra_discount"));// 手动改价--cjc--2017111
                // coupon_discount\
                ob.setCoupon_discount(rs.getDouble("coupon_discount"));// 手动改价--cjc--2017111
                ob.setBusinessName(rs.getString("businessName"));
                ob.setGradeDiscount(rs.getFloat("grade_discount"));
                String paytype=rs.getString("paytypes");
                String tp="支付错误";
                if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")>-1){
                    StringBuilder types=new StringBuilder();
                    String [] t=paytype.split(",");
                    for(int i=0;i<t.length;i++){
                        types.append(StringUtil.getPayType(t[i]));
                        if(i != t.length-1){
                            types.append(",");
                        }
                    }
                    tp=types.toString();
                }else if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")<=-1){
                    tp=StringUtil.getPayType(paytype);
                }
                ob.setPaytypes(tp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return ob;
    }

    // 获取dropship订单--12.9cjc
    @Override
    public OrderBean getChildrenOrders(String orderNo) {
        String sql = "select (select count(tb_complain.id) from tb_complain where complainState in(1,2) and  ref_orderid=child_order_no)complain, "
                + "0 deliver, (select count(ops.id) from order_product_source ops  where  ((purchase_state=5  and remark!=null) or purchase_state=12)  "
                + "and ops.orderid = o.child_order_no) purchase, pf.id dzid, pf.orderno dzorderno, pf.confirmname dzconfirmname,pf.confirmtime dzconfirmtime, "
                + "a.address,o.user_id,actual_ffreight,pay_price_tow,remaining_price, actual_weight,actual_volume,custom_discuss_other,o.state,a.address2," +
                "(select id from zone z where z.id=a.country or z.country=a.country) as country, "
                + "(select country from zone z where z.id=a.country or z.country=a.country) countryName , a.statename,a.phonenumber,a.zipcode,packag_style,mode_transport,"
                + "expect_arrive_time, actual_ffreight,arrive_time,actual_allincost,service_fee, applicable_credit,extra_freight,order_ac,"
                + "date_format(o.create_time,'%Y-%m-%d') as create_time, recipients,pay_price_three,pay_price,foreign_freight,o.currency,discount_amount,"
                + "product_cost, (select email from admuser au where aru.adminid=au.id) adminemail, actual_lwh,0 ordernum,ifnull(aru.admName,'') adminname,"
                + "ifnull(aru.adminid,'') adminid,  actual_weight_estimate,actual_freight_c,packag_number,u.name username, u.email,'' as buyer,exporttime,ug.grade, "
                + "'' storagetime, o.delivery_time, 0  buyid, o.orderRemark ,o.cashback  " + "from dropshiporder o "
                + "left join order_address a " + "on a.orderNo=o.child_order_no "
                + "left join user u  on  u.id =o.user_id " + "left join user_grade ug  on  u.grade =ug.id "
                + "left join order_fee of  on  of.orderno =o.child_order_no "
                + "left join  admin_r_user aru on aru.userid= o.user_id "
                + "left join  paymentconfirm  pf on pf.orderno=o.child_order_no " + "where child_order_no =?";

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        OrderBean ob = new OrderBean();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            // stmt.setString(2, orderNo);
            System.out.println(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                ob.setDzId(rs.getInt("dzid"));
                ob.setDzConfirmname(rs.getString("dzconfirmname"));
                String dzconfirmtime = rs.getString("dzconfirmtime");
                ob.setDzConfirmtime(Utility.getStringIsNull(dzconfirmtime)
                        ? dzconfirmtime.substring(0, dzconfirmtime.indexOf(" ")) : "");
                ob.setDzOrderno(rs.getString("dzorderno"));
                ob.setUserName(rs.getString("username"));
                ob.setUserEmail(rs.getString("u.email"));
                ob.setGrade(rs.getString("grade"));
                ob.setOrderNo(orderNo);
                ob.setUserid(rs.getInt("user_id"));
                ob.setActual_ffreight(rs.getString("actual_ffreight"));
                ob.setRemaining_price(rs.getDouble("remaining_price"));
                ob.setActual_weight(rs.getString("actual_weight"));
                ob.setCustom_discuss_other(rs.getString("custom_discuss_other"));
                ob.setState(rs.getInt("state"));
                String pay_price_tow = rs.getString("pay_price_tow");
                ob.setPay_price_tow(Utility.getIsDouble(pay_price_tow) ? pay_price_tow : "0");
                Timestamp time = rs.getTimestamp("expect_arrive_time");
                ob.setExpect_arrive_time(time != null ? dateFormat.format(time) : null);
                ob.setActual_volume(rs.getString("actual_volume"));
                Timestamp transport_time = rs.getTimestamp("exporttime");
                ob.setTransport_time(transport_time != null ? dateFormat.format(transport_time) : null);
                ob.setActual_ffreight(rs.getString("actual_ffreight"));
                ob.setArrive_time(rs.getString("arrive_time"));
                ob.setActual_allincost(rs.getDouble("actual_allincost"));
                ob.setActual_freight_c(rs.getDouble("actual_freight_c"));
                Address address = new Address();
                address.setId(rs.getInt("country"));
                address.setAddress(rs.getString("address"));
                address.setCountry(rs.getString("countryName"));
                address.setPhone_number(rs.getString("phonenumber"));
                address.setZip_code(rs.getString("zipcode"));
                address.setStatename(rs.getString("statename"));
                address.setAddress2(rs.getString("address2"));
                address.setRecipients(rs.getString("recipients"));
                ob.setAddress(address);
                ob.setPackage_style(rs.getInt("packag_style"));
                ob.setMode_transport(rs.getString("mode_transport"));
                String service_fee = rs.getString("service_fee");
                ob.setOrderNumber(rs.getInt("ordernum") == 1);
                ob.setService_fee(service_fee);
                ob.setApplicable_credit(rs.getDouble("applicable_credit"));
                ob.setOrder_ac(rs.getDouble("order_ac"));
                ob.setCreatetime(rs.getString("create_time"));
                ob.setCurrency(rs.getString("currency"));
                ob.setDiscount_amount(rs.getDouble("discount_amount"));
                ob.setProduct_cost(rs.getString("product_cost"));
                ob.setPay_price(Double.parseDouble(Utility.formatPrice(rs.getString("pay_price")).replaceAll(",", "")));
                String foreign_freight_ = rs.getString("foreign_freight");
                ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
                ob.setEmail(rs.getString("adminemail"));
                ob.setActual_lwh(rs.getString("actual_lwh"));
                ob.setActual_weight_estimate(rs.getDouble("actual_weight_estimate"));
                ob.setExtra_freight(rs.getDouble("extra_freight"));
                ob.setPackag_number(rs.getInt("packag_number"));
                ob.setBuyuser(rs.getString("buyer"));
                ob.setAdminname(rs.getString("adminname"));
                ob.setStoragetime(rs.getString("storagetime"));
                ob.setDeliveryTime(rs.getInt("delivery_time"));
                ob.setReminded(new int[]{rs.getInt("complain"), rs.getInt("purchase"), rs.getInt("deliver")});// 提醒，0-客户投诉，1-入库问题，2，-出库问题
                ob.setBuyid(rs.getInt("buyid"));
                ob.setOrderRemark(rs.getString("orderRemark"));
                ob.setAdminid(rs.getInt("adminid"));
                ob.setCashback(rs.getDouble("cashback"));// 200美元优惠10元的--cjc--20161104
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return ob;
    }

    @Override
    public int isTblack(String userName) {
        int row = 0;
        String sql = "select count(id) as counts from tblacklist where email=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                row = rs.getInt("counts");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return row;
    }

    @Override
    public PaymentConfirm queryForPaymentConfirm(String orderNo) {
        String sql = "SELECT t.id,t.orderno orderno,t.confirmname confirmname,t.confirmtime confirmtime,CASE t.paytype WHEN '0' THEN 'paypal支付'WHEN '1' THEN 'WireTransfer 支付'WHEN '2' THEN '余额支付' END paytype,t.paymentid FROM paymentconfirm t where t.orderno = ?";
        PaymentConfirm paymentConfirm = new PaymentConfirm();
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                paymentConfirm.setId((long) rs.getInt("id"));
                paymentConfirm.setOrderno(rs.getString("orderno"));
                paymentConfirm.setConfirmname(rs.getString("confirmname"));
                paymentConfirm.setConfirmtime(rs.getString("confirmtime"));
                paymentConfirm.setPaytype(rs.getString("paytype"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return paymentConfirm;
    }

    @Override
    public List<OrderBean> getListOrders(String orderNo) {

        String sql = "SELECT order_no,if(memberFee>=10,pay_price-memberFee,pay_price) as pay_price ,foreign_freight ,product_cost ,actual_allincost ,"
                + "pay_price_tow ,pay_price_three ,remaining_price ,currency,actual_ffreight,"
                +"(SELECT  ifnull(sum(amount),0)  FROM tab_coupon_use_record WHERE  order_no=o.order_no AND state=1) as couponAmount,"
                + "coupon_discount,extra_discount,grade_discount,share_discount,discount_amount,cashback, "
                + "service_fee,extra_freight,firstdiscount,vatbalance,actual_freight_c,processingfee,actual_lwh,memberFee,isDropshipOrder" +
                " FROM orderinfo o where LEFT(order_no,17) = LEFT(?,17) ";
        List<OrderBean> list = new ArrayList<OrderBean>();
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OrderBean ob = new OrderBean();

                ob.setOrderNo(rs.getString("order_no"));
                ob.setPay_price(Double.parseDouble(Utility.formatPrice(rs.getString("pay_price")).replaceAll(",", "")));
                String foreign_freight_ = rs.getString("foreign_freight");
                ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
                ob.setProduct_cost(rs.getString("product_cost"));
                ob.setCouponAmount(StringUtil.isBlank(rs.getString("couponAmount"))?"0":rs.getString("couponAmount"));
                ob.setActual_allincost(rs.getDouble("actual_allincost"));
                String pay_price_tow = rs.getString("pay_price_tow");
                ob.setRemaining_price(rs.getDouble("remaining_price"));
                ob.setPay_price_tow(Utility.getIsDouble(pay_price_tow) ? pay_price_tow : "0");
                ob.setCurrency(rs.getString("currency"));
                ob.setActual_ffreight(rs.getString("actual_ffreight"));
                ob.setCoupon_discount(rs.getDouble("coupon_discount"));
                ob.setExtra_discount(rs.getDouble("extra_discount"));
                ob.setGradeDiscount(rs.getFloat("grade_discount"));
                ob.setShare_discount(rs.getDouble("share_discount"));
                ob.setDiscount_amount(rs.getDouble("discount_amount"));
                ob.setCashback(rs.getDouble("cashback"));
                ob.setService_fee(rs.getString("service_fee"));
                ob.setExtra_freight(rs.getDouble("extra_freight"));
                ob.setFirstdiscount(rs.getString("firstdiscount"));
                ob.setVatBalance(rs.getDouble("vatbalance"));
                ob.setActual_freight_c(rs.getDouble("actual_freight_c"));
                ob.setActual_lwh(rs.getString("actual_lwh"));
                ob.setProcessingfee(rs.getDouble("processingfee"));
                ob.setMemberFee(rs.getDouble("memberFee"));
                ob.setIsDropshipOrder(rs.getInt("isDropshipOrder"));

                /*
                 * SimpleDateFormat dateFormat = new
                 * SimpleDateFormat("yyyy-MM-dd");
                 * ob.setDzId(rs.getInt("dzid"));
                 * ob.setDzConfirmname(rs.getString("dzconfirmname")); String
                 * dzconfirmtime = rs.getString("dzconfirmtime");
                 * ob.setDzConfirmtime(Utility.getStringIsNull(dzconfirmtime) ?
                 * dzconfirmtime.substring(0,dzconfirmtime.indexOf(" ")):"");
                 * ob.setDzOrderno(rs.getString("dzorderno"));
                 * ob.setUserName(rs.getString("username"));
                 * ob.setUserEmail(rs.getString("u.email"));
                 * ob.setOrderNo(rs.getString("order_no"));
                 * ob.setUserid(rs.getInt("user_id"));
                 * ob.setActual_ffreight(rs.getString("actual_ffreight"));
                 * ob.setRemaining_price(rs.getDouble("remaining_price"));
                 * ob.setActual_weight(rs.getString("actual_weight"));
                 * ob.setCustom_discuss_other(rs.getString(
                 * "custom_discuss_other")); ob.setState(rs.getInt("state"));
                 * String pay_price_tow = rs.getString("pay_price_tow");
                 * ob.setPay_price_tow(Utility.getIsDouble(pay_price_tow) ?
                 * pay_price_tow : "0"); Timestamp time =
                 * rs.getTimestamp("expect_arrive_time");
                 * ob.setExpect_arrive_time(time != null ?
                 * dateFormat.format(time):null);
                 * ob.setActual_volume(rs.getString("actual_volume")); Timestamp
                 * transport_time = rs.getTimestamp("exporttime");
                 * ob.setTransport_time(transport_time != null ?
                 * dateFormat.format(transport_time):null);
                 * ob.setActual_ffreight(rs.getString("actual_ffreight"));
                 * ob.setArrive_time(rs.getString("arrive_time"));
                 * ob.setActual_allincost(rs.getDouble("actual_allincost"));
                 * ob.setActual_freight_c(rs.getDouble("actual_freight_c"));
                 * Address address = new Address();
                 * address.setId(rs.getInt("a.country"));
                 * address.setAddress(rs.getString("address"));
                 * address.setCountry(rs.getString("countryName"));
                 * address.setPhone_number(rs.getString("phonenumber"));
                 * address.setZip_code(rs.getString("zipcode"));
                 * address.setStatename(rs.getString("statename"));
                 * address.setAddress2(rs.getString("address2"));
                 * address.setRecipients(rs.getString("recipients"));
                 * ob.setAddress(address);
                 * ob.setPackage_style(rs.getInt("packag_style"));
                 * ob.setMode_transport(rs.getString("mode_transport")); String
                 * service_fee = rs.getString("service_fee");
                 * ob.setOrderNumber(rs.getInt("ordernum") == 1);
                 * ob.setService_fee(service_fee);
                 * ob.setApplicable_credit(rs.getDouble("applicable_credit"));
                 * ob.setOrder_ac(rs.getDouble("order_ac"));
                 * ob.setCreatetime(rs.getString("create_time"));
                 * ob.setCurrency(rs.getString("currency"));
                 * ob.setDiscount_amount(rs.getDouble("discount_amount"));
                 * ob.setProduct_cost(rs.getString("product_cost"));
                 * ob.setPay_price(Double.parseDouble(Utility.formatPrice(rs.
                 * getString("pay_price")).replaceAll(",", ""))); String
                 * foreign_freight_ = rs.getString("foreign_freight");
                 * ob.setForeign_freight(Utility.getStringIsNull(
                 * foreign_freight_)?foreign_freight_:"0");
                 * ob.setEmail(rs.getString("adminemail"));
                 * ob.setActual_lwh(rs.getString("actual_lwh"));
                 * ob.setActual_weight_estimate(rs.getDouble(
                 * "actual_weight_estimate"));
                 * ob.setExtra_freight(rs.getDouble("extra_freight"));
                 * ob.setPackag_number(rs.getInt("packag_number"));
                 * ob.setBuyuser(rs.getString("buyer"));
                 * ob.setAdminname(rs.getString("adminname"));
                 * ob.setStoragetime(rs.getString("storagetime"));
                 * ob.setDeliveryTime(rs.getInt("delivery_time"));
                 * ob.setReminded(new
                 * int[]{rs.getInt("complain"),rs.getInt("purchase"),rs.getInt(
                 * "deliver")});//提醒，0-客户投诉，1-入库问题，2，-出库问题
                 * ob.setBuyid(rs.getInt("buyid"));
                 */
                list.add(ob);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int saveForwarder(Forwarder forwarder) {
        String sql = "insert forwarder(order_no,express_no,logistics_name,transport_details,new_state,createtime) values(?,?,?,?,?,now())";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, forwarder.getOrder_no());
            stmt.setString(2, forwarder.getExpress_no());
            stmt.setString(3, forwarder.getLogistics_name());
            stmt.setString(4, forwarder.getTransport_details());
            stmt.setString(5, forwarder.getNew_state());
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public Forwarder getForwarder(String orderNo) {
        // 如果只拿订单号去模糊查询(对于拆单后的订单)运单号可能不正确
        String newOrderNo = orderNo + ",";
        // String sql = "select
        // order_no,express_no,logistics_name,transport_details,new_state,createtime,isneed
        // from forwarder where order_no = ?";
        // 订单+','
        String sql = "select orderid order_no,expressno express_no, transportcompany logistics_name,'' as transport_details,'' as new_state ,createtime,''as isneed "
                + "from shipping_package where remarks like concat('%','" + newOrderNo + "','%')";
        // 订单
        String sql1 = "select orderid order_no,expressno express_no, transportcompany logistics_name,'' as transport_details,'' as new_state ,createtime,''as isneed "
                + "from shipping_package where remarks like concat('%','" + orderNo + "','%')";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null, rs1 = null;
        PreparedStatement stmt = null, stmt1 = null;
        Forwarder forw = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            String expressno = "";
            String logistics_name = "";
            while (rs.next()) {
                expressno += rs.getString("express_no") + "/";
                logistics_name += rs.getString("logistics_name") + "/";
            }
            if (StrUtils.isNotNullEmpty(expressno) || StrUtils.isNotNullEmpty(logistics_name)) {
                forw = new Forwarder();
                forw.setOrder_no(orderNo);
                forw.setExpress_no(expressno.substring(0, expressno.lastIndexOf("/")));
                forw.setLogistics_name(logistics_name.substring(0, logistics_name.lastIndexOf("/")));
                forw.setNew_state("");
                forw.setTransport_details("");
                forw.setIsneed(0);
            }
            // if(rs.next()){
            // forw = new Forwarder();
            // forw.setOrder_no(orderNo);
            //// forw.setExpress_no(rs.getString("express_no"));
            // forw.setCreatetime(rs.getTimestamp("createtime"));
            // forw.setNew_state(rs.getString("new_state"));
            // forw.setTransport_details(rs.getString("transport_details"));
            // forw.setLogistics_name(rs.getString("logistics_name"));
            // forw.setIsneed(rs.getInt("isneed"));
            // forw.setExpress_no(expressno);
            // }
            // 订单
            if (forw == null) {
                stmt1 = conn.prepareStatement(sql1);
                rs1 = stmt1.executeQuery();
                String expressno1 = "";
                String logistics_name1 = "";
                while (rs1.next()) {
                    expressno1 += rs1.getString("express_no") + "/";
                    logistics_name1 += rs1.getString("logistics_name") + "/";
                }
                if (StrUtils.isNotNullEmpty(expressno1) || StrUtils.isNotNullEmpty(logistics_name1)) {

                    forw = new Forwarder();
                    forw.setOrder_no(orderNo);
                    forw.setExpress_no(expressno1.substring(0, expressno1.lastIndexOf("/")));
                    forw.setLogistics_name(logistics_name1.substring(0, logistics_name1.lastIndexOf("/")));
                    forw.setNew_state("");
                    forw.setTransport_details("");
                    forw.setIsneed(0);
                }
                rs1.close();
                stmt1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return forw;
    }

    @Override
    public int getChangegooddata(String orderNo) {
        String sql = "select packag_style as count from orderinfo where order_no = ? ";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int forw = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                // if(rs.getInt("count")>0){
                forw = rs.getInt("count");
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return forw;
    }

    @Override
    public int upForwarder(Forwarder forwarder) {
        String sql = "update forwarder set express_no=?,logistics_name=?,transport_details=?,new_state=?,createtime=now() where order_no=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(5, forwarder.getOrder_no());
            stmt.setString(1, forwarder.getExpress_no());
            stmt.setString(2, forwarder.getLogistics_name());
            stmt.setString(3, forwarder.getTransport_details());
            stmt.setString(4, forwarder.getNew_state());
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public Evaluate getEvaluate(String orderNo) {
        String sql = "select id,userid,order_no,service,products,evaluate,createtime  from evaluate where order_no= ?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Evaluate evaluate = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                evaluate = new Evaluate();
                evaluate.setId(rs.getInt("id"));
                evaluate.setUserid(rs.getInt("userid"));
                evaluate.setOrderNo(rs.getString("order_no"));
                evaluate.setService(rs.getInt("service"));
                evaluate.setProducts(rs.getInt("products"));
                evaluate.setEvaluate(rs.getString("evaluate"));
                evaluate.setCreatetime(rs.getTimestamp("createtime"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return evaluate;
    }

    @Override
    public int updateOrderChange(String orderNo, int goodId, String oldInfo, String newInfo, int changeType) {
        /*String sql = "select oc.id,oc.orderNo,oc.goodId,oi.user_id from order_change oc,orderinfo oi " +
                "where oc.orderNo=?  and oc.orderNo= oi.order_no and oc.goodId=? and oc.ropType=? and oc.del_state=0 limit 1";*/
        String sql = "select oc.id,oc.orderNo,oc.goodId,oi.user_id from orderinfo oi left join  order_change oc " +
                "on oc.orderNo= oi.order_no and  oc.goodId=? and oc.ropType=? and oc.del_state=0 where oi.order_no = ? limit 1";
        Connection conn = DBHelper.getInstance().getConnection();
        Connection conn2 = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        int result = 0;
        int userId = 0;
        try {
            stmt = conn2.prepareStatement(sql);

            stmt.setInt(1, goodId);
            stmt.setInt(2, changeType);
            stmt.setString(3, orderNo);
            rs = stmt.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
                userId = rs.getInt("user_id");
            }
            if (id > 0 && changeType != 5) {
                // 修改
                sql = "update order_change set oldValue=?,newValue=? where id=? and del_state=0";
                stmt3 = conn.prepareStatement(sql);
                stmt3.setString(1, oldInfo);
                stmt3.setString(2, newInfo);
                stmt3.setInt(3, id);
                result = stmt3.executeUpdate();


                /*stmt1 = conn2.prepareStatement(sql);
                stmt1.setString(1, oldInfo);
                stmt1.setString(2, newInfo);
                stmt1.setInt(3, id);
                result = stmt1.executeUpdate();*/
                //使用MQ更新线上
                String upSql = "update order_change set oldValue='"+ oldInfo +"',newValue='"+ newInfo +"' where id= "+id+" and del_state=0";
                NotifyToCustomerUtil.sendSqlByMq(upSql);
                //通知客户
                NotifyToCustomerUtil.updateOrderGoodsPrice(userId,orderNo);
                result =1;

            } else {
                // 插入
                /*sql = "insert into order_change(orderNo,goodId,ropType,oldValue,newValue,status,is_read) values(?,?,?,?,?,?,1)";
                stmt2 = conn2.prepareStatement(sql);
                stmt2.setString(1, orderNo);
                stmt2.setInt(2, goodId);
                stmt2.setInt(3, changeType);
                stmt2.setString(4, oldInfo);
                stmt2.setString(5, newInfo);
                stmt2.setInt(6, 0);
                result = stmt2.executeUpdate();*/
                //使用MQ插入线上
                StringBuffer upSql = new StringBuffer("insert into order_change(orderNo,goodId,ropType,oldValue,newValue,status,is_read) values(");
                upSql.append("'"+orderNo+"'");
                upSql.append(","+goodId);
                upSql.append(","+changeType);
                upSql.append(",'"+oldInfo+"'");
                upSql.append(",'"+newInfo+"'");
                upSql.append(",1");
                upSql.append(",1)");
                NotifyToCustomerUtil.sendSqlByMq(upSql.toString());
                //通知客户
                NotifyToCustomerUtil.updateOrderGoodsPrice(userId,orderNo);
                result =1;
            }
            sql="update orderinfo set server_update=1 where order_no='"+orderNo+"'";
            stmt3 = conn.prepareStatement(sql);
            stmt3.executeUpdate();

            SendMQ.sendMsg(new RunSqlModel(sql));

        } catch (Exception e) {
            e.printStackTrace();
            result =0;
        } finally {
            DBHelper.getInstance().closeResultSet(rs);

            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closePreparedStatement(stmt1);
            DBHelper.getInstance().closePreparedStatement(stmt2);
            DBHelper.getInstance().closePreparedStatement(stmt3);
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn2);
        }
        return result;
    }

    @Override
    public Map<String, Object> getOrderChange(String orderNo, int goodId, int changeType, int lastNum) {
        String sql = "select id,oldValue,newValue,ropType,dateline from order_change where orderNo=? and goodId=? and ropType=? and id>? and del_state=0 order by id desc";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<String> resultList = new ArrayList<String>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            stmt.setInt(2, goodId);
            stmt.setInt(3, changeType);
            stmt.setInt(4, lastNum);
            rs = stmt.executeQuery();
            int maxId = 0;
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("id");
                String oldValue = rs.getString("oldValue");
                String newValue = rs.getString("newValue");
                if (changeType == 5) {
                    if (Utility.getStringIsNull(oldValue)) {
                        if (oldValue.equals("1")) {
                            resultList.add(rs.getString("dateline") + "   公司：" + newValue);
                        } else {
                            resultList.add(rs.getString("dateline") + "   客户：" + newValue);
                        }
                    }
                    maxId = maxId > id ? maxId : id;
                } else {
                    resultList.add(oldValue + "==" + newValue);
                }
            }
            resultMap.put("maxId", maxId);
            resultMap.put("cont", resultList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeStatement(stmt);

            DBHelper.getInstance().closeConnection(conn);
        }
        return resultMap;
    }

    @Override
    public List<Object[]> getOrderChanges(String orderNo, int lastNum) {
        String sql = "select id,goodId,oldValue,newValue,ropType,dateline from order_change where orderNo=? and (ropType=5 or ropType=3) and id>? and del_state=0 order by goodId,id desc";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Object[]> resultList = new ArrayList<Object[]>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            stmt.setInt(2, lastNum);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String oldValue = rs.getString("oldValue");
                String newValue = rs.getString("newValue");
                int ropType = rs.getInt("ropType");
                if (ropType == 5) {

                    if (Utility.getStringIsNull(oldValue)) {
                        if (oldValue.equals("1")) {
                            newValue = rs.getString("dateline") + "   公司：" + newValue;
                        } else {
                            newValue = rs.getString("dateline") + "   客户：" + newValue;
                        }
                    }
                } else {
                    newValue = "old最小订单：" + oldValue + "   new最小订单：" + newValue;
                }
                Object[] obj = {rs.getInt("id"), rs.getInt("goodId"), newValue, ropType};
                resultList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeStatement(stmt);

            DBHelper.getInstance().closeConnection(conn);
        }
        return resultList;
    }

    @Override
    public UserBean getUserBeanByOrderNo(String orderNo) {
        String sql = "select us.id,us.email,us.name,us.activationState,us.activationCode,us.activationTime,us.activationPassCode,us.activationPassTime from user us,orderinfo oi where oi.order_no=? and us.id=oi.user_id";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserBean user = new UserBean();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user.setEmail(rs.getString("us.email"));
                user.setId(rs.getInt("us.id"));
                user.setName(rs.getString("us.name"));
                user.setActivationCode(rs.getString("us.activationCode"));
                user.setActivationState(rs.getInt("us.activationState"));
                Timestamp date = rs.getTimestamp("us.activationTime");
                user.setActivationTime(date);
                user.setActivationPassCode(rs.getString("us.activationPassCode"));
                user.setActivationPassTime(rs.getTimestamp("us.activationPassTime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return user;
    }

    @Override
    public int updateOrderStatus(String orderNo, int orderStatus) {

    	//更新线上数据改为mq方式
    	String sql = "update orderinfo set state="+ orderStatus +" where order_no='" + orderNo + "'";
    	SendMQServiceImpl sendMQ = new SendMQServiceImpl();
    	return sendMQ.runSqlOnline(orderNo, sql);
    }

    @Override
    public int updateOrderStatus(String orderNo) {

        //更新线上数据改为mq方式
    	String sql = "update orderinfo set state =2 where order_no='" + orderNo + "' and ( select count(id) from order_details where orderid=order_no and state=0)=0";
    	SendMQServiceImpl sendMQ = new SendMQServiceImpl();
    	return sendMQ.runSqlOnline(orderNo, sql);
    }

    @Override
    public int updateOrderChangeStatus(String orderNo) {
        String sql = "update order_change set status=1 where orderNo=? and ropType!=6 and del_state=0";
        Connection conn = DBHelper.getInstance().getConnection();
//        Connection conn2 = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
//        PreparedStatement stmt1 = null;
        int result = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            result = stmt.executeUpdate();
//            stmt1 = conn2.prepareStatement(sql);
//            stmt1.setString(1, orderNo);
//            result = stmt1.executeUpdate();
            //更新线上数据改为mq方式
        	SendMQServiceImpl sendMQ = new SendMQServiceImpl();
        	return sendMQ.runSqlOnline(orderNo, "update order_change set status=1 where orderNo='" + orderNo + "' and ropType!=6 and del_state=0");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
//            if (stmt1 != null) {
//                try {
//                    stmt1.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
            DBHelper.getInstance().closeConnection(conn);
//            DBHelper.getInstance().closeConnection(conn2);
        }
        return result;
    }

    @Override
    public int updateClinetAndServerUpdateState(String orderNo, int clientUpdate, int serverUpdate) {
        String sql = "update orderinfo set client_update=?,server_update=? where order_no=?";

        List<String> lstValues = new ArrayList<>(3);
        lstValues.add(String.valueOf(clientUpdate) );
        lstValues.add(String.valueOf(serverUpdate ));
        lstValues.add(orderNo );

        String runSql = DBHelper.covertToSQL(sql,lstValues);
        return Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
    }

    @Override
    public Object[] getOrdersPay(String orderNo) {
        // String sql = "select sum(payment_amount) counts from payment where
        // orderid=? group by orderid";
        String sql = "select pay_price,currency from orderinfo where order_no=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        // double payment = 0;
        Object[] orderinfo = new Object[2];
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                // payment = rs.getDouble("payprice");
                String pay_price = rs.getString("pay_price");
                String currency = rs.getString("currency");
                /*
                 * String pay_price_three = rs.getString("pay_price_three");
                 * double pay_price_thress_ = 0;
                 * if(Utility.getStringIsNull(pay_price_three)){
                 * pay_price_thress_ =
                 * Double.parseDouble(Utility.formatPrice(pay_price_three).
                 * replaceAll(",", "")); }
                 */
                orderinfo[0] = Utility.formatPrice(pay_price).replaceAll(",", "");
                orderinfo[1] = currency;
                // payment=Double.parseDouble(Utility.formatPrice(pay_price).replaceAll(",",
                // ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return orderinfo;
    }

    @Override
    public double getOrdersPayUserid(int userid) {
        String sql = "select o.user_id,sum(o.pay_price/e.exchange_rate) as pay_price "
                + "from orderinfo o,exchange_rate e where  o.currency=e.country and  "
                + "(o.state>0 and o.state<6) and o.user_id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        double total = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                total = rs.getDouble("pay_price");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return total;
    }

    @Override
    public double getOrdersCancelUserid(int userid) {
        String sql = "select r.userid,sum(r.price/e.exchange_rate) as pay_price from recharge_record r,exchange_rate e " +
                "where r.currency=e.country and r.type in(1,5) and usesign=0 and r.userid = ?;";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        double total = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("pay_price");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return total;
    }

    @Override
    public Map<String, String> getOrdersPayByUserids(List<Integer> list) {
        String sql = "select o.user_id,sum(o.pay_price/e.exchange_rate) as pay_price "
                + "from orderinfo o,exchange_rate e where  o.currency=e.country and  "
                + "(o.state>0 and o.state<6) and o.user_id  in (";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            for (Integer l : list) {
                sql = sql + l + ",";
            }
            sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
            sql = sql + ") group by o.user_id ";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("user_id"), rs.getString("pay_price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return map;
    }

    @Override
    public List<Map<String, String>> getOrdersPays(String orderNo) {
        String sql = "select orderid,payment_cc,payment_amount,createtime,paytype,paymentid "
                + "from payment where orderid=? and paystatus=1 order by id desc";
        if (orderNo.indexOf("_") == -1) {
            orderNo = orderNo.substring(0, orderNo.length() - 1) + "%";
            sql = "select orderid,payment_cc,payment_amount,createtime,paytype,paymentid "
                    + "from payment where orderid like ? and paystatus=1 order by id desc";
        }
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> payment = new ArrayList<Map<String, String>>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orderid", rs.getString("orderid"));
                map.put("payment_cc", rs.getString("payment_cc"));
                map.put("payment_amount", rs.getString("payment_amount"));
                map.put("paymentid", rs.getString("paymentid"));
                if (rs.getDate("createtime") == null) {
                    map.put("createtime", "");
                } else {
                    map.put("createtime", sf.format(rs.getDate("createtime")));
                }
                map.put("paytype", rs.getString("paytype"));
                payment.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return payment;
    }

    @Override
    public int upOrderChangeResolve(String orderNo, int goodId, int changeType) {
        String sql = "update order_change set del_state=1 where  orderNo=? and goodId=? and ropType=?";
        String sql1 = "update orderinfo set server_update=0 where 1>(select count(id) from order_change where orderno=? and del_state=0) and order_no=?";
        Connection conn = DBHelper.getInstance().getConnection();
        // Connection conn2 = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        int result = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            stmt.setInt(2, goodId);
            stmt.setInt(3, changeType);
            result = stmt.executeUpdate();

            /*stmt2 = conn2.prepareStatement(sql);
            stmt2.setString(1, orderNo);
            stmt2.setInt(2, goodId);
            stmt2.setInt(3, changeType);
            result = stmt2.executeUpdate();*/

            List<String> listValues = new ArrayList<>();
            listValues.add(String.valueOf(orderNo));
            listValues.add(String.valueOf(goodId));
            listValues.add(String.valueOf(changeType));
            String runSql = DBHelper.covertToSQL(sql, listValues);
            String rsStr = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
            int countRs = 0;
            if(org.apache.commons.lang3.StringUtils.isBlank(rsStr)){
                countRs = Integer.valueOf(rsStr);
            }
            result = countRs;

            /*stmt1 = conn2.prepareStatement(sql1);
            stmt1.setString(1, orderNo);
            stmt1.setString(2, orderNo);
            stmt1.execute();*/

            listValues.clear();
            listValues.add(String.valueOf(orderNo));
            listValues.add(String.valueOf(orderNo));
            runSql = DBHelper.covertToSQL(sql1, listValues);
            SendMQ.sendMsg(new RunSqlModel(runSql));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            if (stmt1 != null) {
                try {
                    stmt1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
            // DBHelper.getInstance().closeConnection(conn2);
        }
        return result;
    }

    @Override
    public OrderDetailsBean getById(int id) {
        String sql = "select actual_weight,actual_volume,actual_price,actual_freight from order_details where id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrderDetailsBean order = new OrderDetailsBean();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                order.setActual_freight(rs.getDouble("actual_freight"));
                order.setActual_price(rs.getDouble("actual_price"));
                order.setActual_volume(rs.getString("actual_volume"));
                order.setActual_weight(rs.getString("actual_weight"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return order;
    }

    @Override
    public List<Object[]> getOrderChanges(String orderNo) {
        String sql = "select goodid,orderno,roptype,newvalue from (select goodid,orderno,roptype,newvalue from order_change  where orderNo=? and del_state='1' and roptype!=5  order by id desc) as a group by roptype";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] objects = {rs.getInt("goodid"), rs.getInt("roptype"), rs.getString("newvalue")};
                list.add(objects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<Integer> getOrderChangeStatus(String orderNo) {
        String sql = "select  ropType from order_change where orderNo=? and ropType!=6 and del_state=0";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Integer> res = new ArrayList<Integer>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res.add(rs.getInt("roptype"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int upQuestions(String orderid, String answer, String freight, String tariffs) {
        String sql = "update advance_order set answer=?,freight=?,tariffs=?  where orderno = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, answer);
            stmt.setString(2, freight);
            stmt.setString(3, tariffs);
            stmt.setString(4, orderid);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public String getAdvance(String orderid) {
        String sql = "select orderno from advance_order where orderno=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String res = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getString("orderno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int addAdvance(String orderid, String freight, String tariffs) {
        String sql = "insert advance_order(orderno,freight,tariffs,createtime) values(?,?,?,now())";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            stmt.setString(2, freight);
            stmt.setString(3, tariffs);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public AdvanceOrderBean getAdvanceBean(String orderid) {
        String sql = "select orderno,freight,tariffs,createtime,questions,answer from advance_order where orderno=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        AdvanceOrderBean res = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = new AdvanceOrderBean();
                res.setOrderNo(orderid);
                res.setFreight(rs.getString("freight"));
                res.setTariffs(rs.getString("tariffs"));
                res.setCreatetime(rs.getString("createtime"));
                res.setQuestions(rs.getString("questions"));
                res.setAnswer(rs.getString("answer"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public Set<String> getOrderIpByUserId(String userId) {
        String sql = "select ip from orderinfo where user_id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Set<String> set = new HashSet<String>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getString("ip"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return set;
    }

    @Override
    public int updateGoods(int type, int goodsid, String value) {
        String sql = "";
        if (type == 0) {
            sql = "update goods_car set googs_number=? where id=?";
        } else {
            sql = "update goods_car set notfreeprice=? where id=?";
        }

        int res = 0;

        List<String> lstValues = new ArrayList<>();
        lstValues.add(value);
        lstValues.add(String.valueOf(goodsid));

        String runSql = DBHelper.covertToSQL(sql,lstValues);
        res=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));


        return res;
    }

    @Override
    public int iscloseOrder(String orderNo) {
        String sql = "select orderid from   orderinfo   where order_no=? and  (state=-1 or state=6)";
        Connection conn = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("orderid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }



    @Override
    public void cancelInventory1(String orderNo) {
        Connection conn = DBHelper.getInstance().getConnection();
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select orderid,goodsid,id from order_details where orderid='" + orderNo + "'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String odid = rs.getString("id");
                splitDao.addInventory(odid, "整单取消库存");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
    }

    /**
     * 针对取消订单中有商品使用过库存商品做库存退回处理
     */
    @Override
    public void cancelInventory(String orderNo) {
        int row = 0;
        Connection conn = DBHelper.getInstance().getConnection();
      /*  String sql = "SELECT i.goods_p_price,od.car_img,i.barcode as i_barcode,ir.barcode,od.orderid,od.goodsid,li.in_id,li.id,li.od_id,i.remaining,li.lock_remaining,i.can_remaining,i.flag,li.flag as li_flag FROM lock_inventory li "
                + "INNER JOIN order_details od ON li.od_id=od.id LEFT JOIN inventory i ON li.in_id=i.id left join id_relationtable ir on od.orderid=ir.orderid and od.goodsid=ir.goodid WHERE li.is_delete=0 AND od.orderid='"
                + orderNo + "'";*/
        String sql = "SELECT i.goods_p_price,od.car_img,i.barcode as i_barcode,ir.barcode,od.orderid,od.goodsid,li.in_id,li.id,li.od_id,i.remaining,li.lock_remaining,i.can_remaining,i.flag,li.flag as li_flag FROM lock_inventory li "
        		+ "INNER JOIN order_details od ON li.od_id=od.id LEFT JOIN inventory i ON li.in_id=i.id left join id_relationtable ir on od.orderid=ir.orderid and od.goodsid=ir.goodid WHERE li.is_delete=0 AND od.orderid='"
        		+ orderNo + "'";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int flag = rs.getInt("li_flag");
                String goods_p_price = rs.getString("goods_p_price");
                if (goods_p_price.indexOf(",") > -1) {
                    goods_p_price = goods_p_price.split(",")[0];
                }
                if (flag == 0) {
                    // 如果商品还没有移库，只需把锁定的库存释放回去
                    sql = "update inventory set can_remaining=(" + rs.getInt("can_remaining")
                            + rs.getInt("lock_remaining") + ") where id=" + rs.getInt("in_id") + "";
                    stmt = conn.prepareStatement(sql);
                } else {
                    // 商品已移库，把移库结果曾现给仓库，等待仓库移库
                    sql = "insert into cancel_goods_inventory (orderid,goodsid,order_barcode,inventory_qty,inventory_barcode,od_id,car_img,createtime,in_id,i_flag,goods_p_price) values(?,?,?,?,?,?,?,now(),?,?,?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, rs.getString("orderid"));
                    stmt.setString(2, rs.getString("goodsid"));
                    stmt.setString(3, rs.getString("barcode"));
                    stmt.setInt(4, rs.getInt("lock_remaining"));
                    stmt.setString(5, rs.getString("i_barcode"));
                    stmt.setString(6, rs.getString("od_id"));
                    stmt.setString(7, rs.getString("car_img"));
                    stmt.setString(8, rs.getString("in_id"));
                    stmt.setString(9, rs.getString("flag"));
                    stmt.setDouble(10, Double.valueOf(goods_p_price));
                }
                row = stmt.executeUpdate();
                sql = "update lock_inventory set is_delete=1 where id=" + rs.getInt("id") + "";
                stmt = conn.prepareStatement(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
    }

    @Override
    public boolean checkTestOrder(String orderid) {
        boolean flag=true;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            String sql="SELECT oi.order_no FROM orderinfo oi  " +
                    "LEFT JOIN admin_r_user a ON oi.user_id=a.userid WHERE adminid='18' AND oi.order_no='"+orderid+"'";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            if(rs.next()){
                flag=false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
        }
        return flag;
    }

    @Override
    public int closeOrder(String orderNo) {
        String sql = "update orderinfo set state=-1, cancel_obj=1 where order_no=?";

        int result = 0;

        if (GetConfigureInfo.openSync()) {
            String upSql = "update orderinfo set state=-1, cancel_obj=1 where order_no='" + orderNo + "'";
            SaveSyncTable.InsertOnlineDataInfo(0, orderNo, "取消订单", "orderinfo", upSql);
            upSql = "update order_details set state=-1 where orderid='" + orderNo + "'";
            SaveSyncTable.InsertOnlineDataInfo(0, orderNo, "取消订单详情", "order_details", upSql);
            result = 1;
        } else {

            List<String> lstValues = new ArrayList<>();
            lstValues.add(orderNo);

            String runSql = DBHelper.covertToSQL(sql,lstValues);
            result+=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

            sql = "update order_details set state=-1 where orderid=?";
            lstValues = new ArrayList<>();
            lstValues.add(orderNo);

            runSql = DBHelper.covertToSQL(sql,lstValues);
            result+=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
        }

        return result;
    }

    @Override
    public List<Object[]> getGoodpostage(int userid, int page, int endpage) {
        String sql = "select g.id,user.id,user.name,user.email,goods_url,googs_img,googs_price,googs_number,freight_free,preferential,goods_email,goods_title from goods_car g,user where user.id=g.userid and state=0 ";
        if (userid != 0) {
            sql += "  and userid=? ";
        }
        sql = sql + "  and goods_email is not null order by datatime desc  limit ?, ?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            stmt = conn.prepareStatement(sql);
            int i = 1;
            if (userid != 0) {
                stmt.setInt(1, userid);
                i++;
            }
            stmt.setInt(i, page);
            stmt.setInt(i + 1, endpage);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] objects = {rs.getInt("g.id"), rs.getInt("user.id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("goods_url"), rs.getString("googs_img"),
                        rs.getString("googs_price"), rs.getString("googs_number"), rs.getString("freight_free"),
                        rs.getString("preferential"), rs.getString("goods_email"), rs.getString("goods_title")};
                list.add(objects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int getGoodpostageNumber(int userid) {
        String sql = "select count(id) coun from goods_car g  where state=0 and userid !=0 ";
        if (userid != 0) {
            sql += "  and userid=? ";
        }
        sql = sql + "  and goods_email is not null ";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            if (userid != 0) {
                stmt.setInt(1, userid);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("coun");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int upOrderPurchase(int orderdetailid, String orderNo, String purchase_confirmation) {
        String sql = "update order_details set purchase_state=1,purchase_confirmation=?,purchase_time=now()  where id = ?";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        int res = 0;
        try {

            List<String> lstValues = new ArrayList<>();
            lstValues.add(purchase_confirmation);
            lstValues.add(String.valueOf(orderdetailid));

            String runSql = DBHelper.covertToSQL(sql,lstValues);
            res=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

            if (res > 0) {
                sql = "update orderinfo set purchase_number=purchase_number+1 where order_no = ?";

                lstValues = new ArrayList<>();
                lstValues.add(orderNo);

                runSql = DBHelper.covertToSQL(sql,lstValues);
                res=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(sql)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    @Override
    public List<Object[]> getOrderRemark(String orderid) {
        // TODO Auto-generated method stub
        String sql_setname = "set names utf8";
        String sql = "select * from order_remark where orderid=? ";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            conn.prepareStatement(sql_setname).executeQuery();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] objects = {rs.getString("orderid"), rs.getString("orderremark"), rs.getString("remarkuserid"),
                        rs.getString("createtime")};
                list.add(objects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int addOrderRemark(String orderid, String orderremark, int remarkuserid, int type) {
        // TODO Auto-generated method stub
        String sql_setname = "set names utf8";
        String sql = "insert into order_remark(orderid,orderremark,remarkuserid,createtime,type) values(?,?,(select admName from admuser where id=?),now(),?)";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int result = 0;
        try {
            // conn.prepareStatement(sql_setname).executeQuery();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            stmt.setString(2, orderremark);
            stmt.setInt(3, remarkuserid);
            stmt.setInt(4, type);
            result = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public int cancelOrderPurchase(int orderdetailid, String orderNo) {
        // TODO Auto-generated method stub
        String sql = "update order_details set purchase_state=0,purchase_confirmation='',purchase_time= null  where id = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderdetailid);
            res = stmt.executeUpdate();
            if (res > 0) {
                sql = "update orderinfo set purchase_number=purchase_number-1 where order_no = ?";
                stmt2 = conn.prepareStatement(sql);
                stmt2.setString(1, orderNo);
                res = stmt2.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<String> getNewTotalPrice(String orderNo) {
        // TODO Auto-generated method stub
        String sql = "select * from order_change where orderNo=? and ropType=6 ";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<String> list = new ArrayList<String>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String temp = rs.getString("oldValue");
                list.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    // @Override
    // public int addUserInCharge(int userid,String username,String
    // useremail,int adminid,String admName) {
    // String Dsql = "delete admin_r_user from admin_r_user where (useremail!=''
    // and useremail=?) or ( userid=? and userid<>0) ";
    // //String sql = "insert into
    // admin_r_user(userid,username,useremail,adminid,createdate,admName)
    // values(?,?,?,?,now(),?)";
    // String sql="insert into
    // admin_r_user(userid,username,useremail,adminid,createdate,admName)
    // values( "+(userid==0?"ifnull((select id from user where
    // email=?),0)":"?")+",?,?,?,now(),?)";
    // Connection conn = DBHelper.getInstance().getConnection();
    // ResultSet rs = null;
    // PreparedStatement stmt = null;
    // int result=0;
    // try {
    // stmt=conn.prepareStatement(Dsql);
    // stmt.setString(1, useremail);
    // stmt.setInt(2, userid);
    // stmt.executeUpdate();
    // stmt = conn.prepareStatement(sql);
    // if(userid == 0){
    // stmt.setString(1,useremail);
    // }else{
    // stmt.setInt(1,userid);
    // }
    // stmt.setString(2, username);
    // stmt.setString(3, useremail);
    // stmt.setInt(4, adminid);
    // stmt.setString(5, admName);
    // result = stmt.executeUpdate();
    // System.out.println(sql);
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if (rs != null) {
    // try {
    // rs.close();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }
    // if (stmt != null) {
    // try {
    // stmt.close();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }
    // DBHelper.getInstance().closeConnection(conn);
    // }
    // return result;
    // }
    @Override
    public int updateUserInCharge(int userid, int adminid, String admName) {
        String sql = "update admin_r_user set adminid=?,admName=(select admName from admuser where id=?) where userid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int result = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adminid);
            stmt.setInt(2, adminid);
            stmt.setInt(3, userid);
            result = stmt.executeUpdate();

            List<String> lstValues = new ArrayList<>();
            lstValues.add(String.valueOf(adminid));
            lstValues.add(admName);
            lstValues.add(String.valueOf(userid));

            String runSql = DBHelper.covertToSQL(sql,lstValues);
            result =Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);

            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public int queryUserInCharge(int userid) {
        String sql = "select count(*) from admin_r_user where userid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int result = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            rs = stmt.executeQuery();
            System.out.println(sql);
            while (rs.next()) {
                result = rs.getInt("count(*)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public int getCountofRegisterUser(String date) {
        // TODO Auto-generated method stub
        String sql = "select count(*) as count from user as us where date(createtime)=DATE_FORMAT(?,'%Y-%m-%d')";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofUserEnterinsite(String date) {
        // TODO Auto-generated method stub
        String sql = "select count(*) as count from (select ip from behavior_record where view_date_day=? and ip NOT like '66.249.%' and IP<>'116.228.150.218' group by ip) as t;";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofSearchNotAdd(String date) {
        // TODO Auto-generated method stub
        String sql = "{call searchnotadd(?)}";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        CallableStatement cstmt = null;
        int res = 0;
        try {
            cstmt = conn.prepareCall(sql);
            cstmt.setString(1, date);
            rs = cstmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofAddGoodscar(String date) {
        // TODO Auto-generated method stub
        String sql1 = "select count(*) as count from (select * from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid=0 group by userid,sessionid) as t";
        // String sql2 = "select count(*) as count from (select * from goods_car
        // where date(datatime) = date_format(?,'%Y-%m-%d') and userid!=0 group
        // by userid) as t";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs1 = null;
        // ResultSet rs2 = null;
        PreparedStatement stmt1 = null;
        // PreparedStatement stmt2 = null;
        int res1 = 0;
        // int res2=0;
        try {
            stmt1 = conn.prepareStatement(sql1);
            stmt1.setString(1, date);
            rs1 = stmt1.executeQuery();
            while (rs1.next()) {
                res1 = rs1.getInt("count");
            }

            // stmt2 = conn.prepareStatement(sql2);
            // stmt2.setString(1, date);
            // rs2 = stmt2.executeQuery();
            // while(rs2.next()){
            // res2=rs2.getInt("count");
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
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
            // if (rs2 != null) {
            // try {
            // rs2.close();
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            // }
            // if (stmt2 != null) {
            // try {
            // stmt2.close();
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            // }
            DBHelper.getInstance().closeConnection(conn);
        }
        return res1;
    }

    @Override
    public int getCountofAddNotRegister_1(String date) {
        // TODO Auto-generated method stub
        String sql = "select count(*) as count from (select sessionid from goods_car where datatime = date_format(?,'%Y-%m-%d') and userid=0 and state!=9 and state!=1 group by sessionid having count(*)=1) as t";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofAddNotRegister_2(String date) {
        // TODO Auto-generated method stub
        String sql = "select count(*) as count from (select sessionid from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid=0 and state!=9 and state!=1 group by sessionid having count(*)>1) as t";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofRegisterNotAddr(String date) {
        // TODO Auto-generated method stub
        String sql = "select count(*) as count from (select userid from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid<>0  and userid  not in"
                + "(select ad.userid from address as ad where ad.delflag=0) group by userid) as t";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofPaypal(String date) {
        // TODO Auto-generated method stub
        String sql = "select count(*) as count from"
                + "(select userid from behavior_record where action like '%pay%' and view_date_day=date_format(?,'%Y-%m-%d') and userid not in "
                + "(select userid from payment where date(createtime)=date_format(?,'%Y-%m-%d') and payflag=0 and paytype=0 and paystatus=1) "
                + "and userid in (select userid from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid!=0 group by userid) group by userid) as t";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, date);
            stmt.setString(3, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofNeworder(String date) {
        // TODO Auto-generated method stub
        String sql = "select count(*) as count from (select user_id from orderinfo where date(create_time) = date_format(?,'%Y-%m-%d')  group by user_id) as t;";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofRegister(String date) {
        // TODO Auto-generated method stub
        String sql = "select * from user as us where date(createtime)=DATE_FORMAT(?,'%Y-%m-%d')";
        String count_sql = "select us.id,us.name,us.email,count(*) as count,t.* from user us left join goods_car gc on gc.userid=us.id "
                + " left join (select userid,recipients,address,phone_number,statename from address where userid=?) as t on t.userid=us.id"
                + " where gc.state!=9 and gc.state!=1 and us.id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("id");
                temp.add(userid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                stmt1.setString(2, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(rs1.getInt("id"));
                    tempuser.setName(rs1.getString("name"));
                    tempuser.setEmail(rs1.getString("email"));
                    tempuser.setCount(rs1.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients(rs1.getString("recipients"));
                    tempaddr.setAddress(rs1.getString("address"));
                    tempaddr.setPhone_number(rs1.getString("phone_number"));
                    tempaddr.setStatename(rs1.getString("statename"));
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofEnterinsite(String date) {
        // TODO Auto-generated method stub
        String sql = "select userid from behavior_record where IP NOT like '66.249.%' and IP<>'116.228.150.218' and view_date_day=?  group by userid";
        String count_sql = "select us.id,us.name,us.email,count(*) as count,t.* from user us left join goods_car gc on gc.userid=us.id "
                + " left join (select userid,recipients,address,phone_number,statename from address where userid=?) as t on t.userid=us.id"
                + " where gc.state!=9 and gc.state!=1 and us.id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("userid");
                temp.add(userid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                stmt1.setString(2, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(rs1.getInt("id"));
                    tempuser.setName(rs1.getString("name"));
                    tempuser.setEmail(rs1.getString("email"));
                    tempuser.setCount(rs1.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients(rs1.getString("recipients"));
                    tempaddr.setAddress(rs1.getString("address"));
                    tempaddr.setPhone_number(rs1.getString("phone_number"));
                    tempaddr.setStatename(rs1.getString("statename"));
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofSearchNotAdd(String date) {
        // TODO Auto-generated method stub
        String sql = "select userid from behavior_record where keywords <>'' and keywords not like '%@%' and view_date_day = date_format(?,'%Y-%m-%d') "
                + "and (userid,sessionid) not in (select userid,sessionid from goods_car where datatime = date_format(?,'%Y-%m-%d') group by userid,sessionid) group by userid;";
        String count_sql = "select us.id,us.name,us.email,count(*) as count,t.* from user us left join goods_car gc on gc.userid=us.id "
                + " left join (select userid,recipients,address,phone_number,statename from address where userid=?) as t on t.userid=us.id"
                + " where gc.state!=9 and gc.state!=1 and us.id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("userid");
                temp.add(userid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                stmt1.setString(2, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(rs1.getInt("id"));
                    tempuser.setName(rs1.getString("name"));
                    tempuser.setEmail(rs1.getString("email"));
                    tempuser.setCount(rs1.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients(rs1.getString("recipients"));
                    tempaddr.setAddress(rs1.getString("address"));
                    tempaddr.setPhone_number(rs1.getString("phone_number"));
                    tempaddr.setStatename(rs1.getString("statename"));
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofAddGoodscar(String date) {
        // TODO Auto-generated method stub
        String sql = "select sessionid,'' as name,'' as email,count(sessionid) as count from goods_car where date(datatime) = date_format(?,'%Y-%m-%d')and userid=0 and state!=9 and state!=1 group by sessionid";
        // String count_sql="select '0' as id,'' as name ,'' as email,count(*)
        // as count from goods_car where date(datatime) =
        // date_format(?,'%Y-%m-%d')and sessionid=? and state!=9 and state!=1";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        // ResultSet rs1 = null;
        PreparedStatement stmt = null;
        // PreparedStatement stmt1 = null;
        // List<Object> temp=new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UserBean tempuser = new UserBean();
                tempuser.setId(0);
                tempuser.setName(rs.getString("name"));
                tempuser.setEmail(rs.getString("email"));
                tempuser.setCount(rs.getString("count"));
                Address tempaddr = new Address();
                tempaddr.setRecipients("");
                tempaddr.setAddress("");
                tempaddr.setPhone_number("");
                tempaddr.setStatename("");
                res.add(tempuser);
                res.add(tempaddr);
                //
                // String sessionid=rs.getString("sessionid");
                // String name=rs.getString("name");
                // String email=rs.getString("email");
                // int count = rs.getInt("count");
                // res.add(sessionid);
                // res.add(name);
                // res.add(email);
                // res.add(count);
            }
            // stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            // for(int i=0;i<temp.size();i++){
            // stmt1.setString(1, date);
            // stmt1.setString(2, temp.get(i).toString());
            // rs1 = stmt1.executeQuery();
            // while(rs1.next()){
            // int userid=rs1.getInt("id");
            // String username=rs1.getString("name");
            // String useremail=rs1.getString("email");
            // int count=rs1.getInt("count");
            // res.add(userid);
            // res.add(username);
            // res.add(useremail);
            // res.add(count);
            // }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofAddNotRegister_1(String date) {
        // TODO Auto-generated method stub
        String sql = "select sessionid from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid=0 group by sessionid having count(*)=1";
        String count_sql = "select 0 as id,'' as name,'' as email,count(*) as count from goods_car where state!=9 and state!=1 and sessionid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sessionid = rs.getString("sessionid");
                temp.add(sessionid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(0);
                    tempuser.setName("");
                    tempuser.setEmail("");
                    tempuser.setCount(rs.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients("");
                    tempaddr.setAddress("");
                    tempaddr.setPhone_number("");
                    tempaddr.setStatename("");
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofAddNotRegister_2(String date) {
        // TODO Auto-generated method stub
        String sql = "select sessionid from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid=0 and state!=9 and state!=1 group by sessionid having count(*)>1";
        String count_sql = "select 0 as id,'' as name,'' as email,count(*) as count from goods_car where state!=9 and state!=1 and sessionid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sessionid = rs.getString("sessionid");
                temp.add(sessionid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(0);
                    tempuser.setName("");
                    tempuser.setEmail("");
                    tempuser.setCount(rs1.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients("");
                    tempaddr.setAddress("");
                    tempaddr.setPhone_number("");
                    tempaddr.setStatename("");
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofRegisterNotAddr(String date) {
        // TODO Auto-generated method stub
        String sql = "select userid from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid<>0  and userid  not in (select ad.userid from address as ad where ad.delflag=0) group by userid";
        String count_sql = "select us.id,us.name,us.email,count(*) as count,t.* from user us left join goods_car gc on gc.userid=us.id "
                + " left join (select userid,recipients,address,phone_number,statename from address where userid=?) as t on t.userid=us.id"
                + " where gc.state!=9 and gc.state!=1 and us.id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("userid");
                temp.add(userid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                stmt1.setString(2, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(rs1.getInt("id"));
                    tempuser.setName(rs1.getString("name"));
                    tempuser.setEmail(rs1.getString("email"));
                    tempuser.setCount(rs1.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients(rs1.getString("recipients"));
                    tempaddr.setAddress(rs1.getString("address"));
                    tempaddr.setPhone_number(rs1.getString("phone_number"));
                    tempaddr.setStatename(rs1.getString("statename"));
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofPaypal(String date) {
        // TODO Auto-generated method stub
        String sql = "select userid from behavior_record where action like '%pay%' and view_date_day=date_format(?,'%Y-%m-%d') and userid not in"
                + " (select userid from payment where date(createtime)=date_format(?,'%Y-%m-%d') and payflag=0 and paytype=0 and paystatus=1) "
                + " and userid in (select userid from goods_car where date(datatime) = date_format(?,'%Y-%m-%d') and userid!=0 group by userid) group by userid";
        String count_sql = "select us.id,us.name,us.email,count(*) as count,t.* from user us left join goods_car gc on gc.userid=us.id "
                + " left join (select userid,recipients,address,phone_number,statename from address where userid=?) as t on t.userid=us.id"
                + " where gc.state!=9 and gc.state!=1 and us.id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, date);
            stmt.setString(3, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("userid");
                temp.add(userid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                stmt1.setString(2, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(rs1.getInt("id"));
                    tempuser.setName(rs1.getString("name"));
                    tempuser.setEmail(rs1.getString("email"));
                    tempuser.setCount(rs1.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients(rs1.getString("recipients"));
                    tempaddr.setAddress(rs1.getString("address"));
                    tempaddr.setPhone_number(rs1.getString("phone_number"));
                    tempaddr.setStatename(rs1.getString("statename"));
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object> getUserofNeworder(String date) {
        // TODO Auto-generated method stub
        String sql = "select user_id as userid from orderinfo where date(create_time) = date_format(?,'%Y-%m-%d')  group by user_id";
        String count_sql = "select us.id,us.name,us.email,count(*) as count,t.* from user us left join goods_car gc on gc.userid=us.id "
                + " left join (select userid,recipients,address,phone_number,statename from address where userid=?) as t on t.userid=us.id"
                + " where gc.state!=9 and gc.state!=1 and us.id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        List<Object> temp = new ArrayList<Object>();
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("userid");
                temp.add(userid);
            }
            stmt1 = conn.prepareStatement(count_sql);
            // System.out.println(temp.size());
            for (int i = 0; i < temp.size(); i++) {
                stmt1.setString(1, temp.get(i).toString());
                stmt1.setString(2, temp.get(i).toString());
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    UserBean tempuser = new UserBean();
                    tempuser.setId(rs1.getInt("id"));
                    tempuser.setName(rs1.getString("name"));
                    tempuser.setEmail(rs1.getString("email"));
                    tempuser.setCount(rs1.getString("count"));
                    Address tempaddr = new Address();
                    tempaddr.setRecipients(rs1.getString("recipients"));
                    tempaddr.setAddress(rs1.getString("address"));
                    tempaddr.setPhone_number(rs1.getString("phone_number"));
                    tempaddr.setStatename(rs1.getString("statename"));
                    res.add(tempuser);
                    res.add(tempaddr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehavior> recordUserBehavior(int userid, int page, int pagesize, String view_date_time) {

        // TODO Auto-generated method stub
        // String sql = "select * from behavior_record where userid=? order by
        // view_date_time desc limit ?,? ";
        Connection conn = DBHelper.getInstance().getConnection2();
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        List<UserBehavior> res = new ArrayList<UserBehavior>();
        String sql = "";
        if (view_date_time != null) {
            sql = "select SQL_CALC_FOUND_ROWS  * from behavior_record  where sessionid in "
                    + "(select DISTINCT sessionid from behavior_record  where userid = ? AND sessionid<>'') "
                    + "and view_date_time > ? order by view_url_count desc limit ?,?";
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userid);
                stmt.setString(2, view_date_time);
                stmt.setInt(3, (page - 1) * pagesize);
                stmt.setInt(4, pagesize);
                rs = stmt.executeQuery();
                stmt2 = conn.prepareStatement("select found_rows();");
                rs2 = stmt2.executeQuery();
                int total = 0;
                if (rs2.next()) {
                    total = rs2.getInt("found_rows()");
                }
                while (rs.next()) {
                    UserBehavior temp = new UserBehavior();
                    temp.setUserid(rs.getInt("userid"));
                    temp.setKeywords(rs.getString("keywords"));
                    temp.setView_url(rs.getString("view_url"));
                    temp.setAction(rs.getString("action"));
                    temp.setView_date_day(rs.getString("view_date_day"));
                    temp.setView_date_time(rs.getString("view_date_time"));
                    temp.setTotal(total);
                    res.add(temp);
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
        } else {
            sql = "select SQL_CALC_FOUND_ROWS * from behavior_record  where "
                    + "sessionid in (select DISTINCT sessionid from behavior_record  where userid = ? AND sessionid<>'') "
                    + "order by id desc limit ?,?";
            try {
                stmt = conn.prepareStatement(sql);
                stmt2 = conn.prepareStatement("select found_rows();");
                stmt.setInt(1, userid);
                stmt.setInt(2, (page - 1) * pagesize);
                stmt.setInt(3, pagesize);
                rs = stmt.executeQuery();
                rs2 = stmt2.executeQuery();
                int total = 0;
                if (rs2.next()) {
                    total = rs2.getInt("found_rows()");
                }
                while (rs.next()) {
                    UserBehavior temp = new UserBehavior();
                    temp.setUserid(rs.getInt("userid"));
                    temp.setKeywords(rs.getString("keywords"));
                    temp.setView_url(rs.getString("view_url"));
                    temp.setAction(rs.getString("action"));
                    temp.setView_date_day(rs.getString("view_date_day"));
                    temp.setView_date_time(rs.getString("view_date_time"));
                    temp.setTotal(total);
                    res.add(temp);
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
        }

        return res;
    }

    @Override
    public int getCountofAddGoodscar_1(String date) {
        // TODO Auto-generated method stub
        String date1 = dateTransform(date);
        String sql1 = "select count(t1.id) as count from "
                + " (select * from user where createtime>? and createtime<?) as t1"
                + " inner join (select userid from behavior_record where action='Add To Cart' and view_date_day =? and userid!=0 group by userid) as t2 on t1.id = t2.userid";
        // String sql2 = "select count(*) as count from (select * from goods_car
        // where date(datatime) = date_format(?,'%Y-%m-%d') and userid!=0 group
        // by userid) as t";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs1 = null;
        PreparedStatement stmt1 = null;
        int res1 = 0;
        try {
            stmt1 = conn.prepareStatement(sql1);
            stmt1.setString(1, date);
            stmt1.setString(2, date1);
            stmt1.setString(3, date);
            rs1 = stmt1.executeQuery();
            while (rs1.next()) {
                res1 = rs1.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
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
            DBHelper.getInstance().closeConnection(conn);
        }
        return res1;
    }

    @Override
    public List<Object> getUserofAddGoodscar_1(String date) {
        // TODO Auto-generated method stub
        String date1 = dateTransform(date);
        String sql = "select t1.*,t2.userid,count(gc.id) as count,t3.recipients,t3.address,t3.phone_number,t3.statename from "
                + " (select * from user where createtime>? and createtime<?) as t1 "
                + " inner join (select userid from behavior_record where action='Add To Cart' and view_date_day =? and userid!=0 group by userid) as t2 on t1.id = t2.userid "
                + " left join goods_car gc on t1.id=gc.userid and gc.state!=9 and gc.state!=1 "
                + " left join address as t3 on t1.id= t3.userid" + " group by t1.id";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, date1);
            stmt.setString(3, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UserBean tempuser = new UserBean();
                tempuser.setId(rs.getInt("id"));
                tempuser.setName(rs.getString("name"));
                tempuser.setEmail(rs.getString("email"));
                tempuser.setCount(rs.getString("count"));
                Address tempaddr = new Address();
                tempaddr.setRecipients(rs.getString("recipients"));
                tempaddr.setAddress(rs.getString("address"));
                tempaddr.setPhone_number(rs.getString("phone_number"));
                tempaddr.setStatename(rs.getString("statename"));
                res.add(tempuser);
                res.add(tempaddr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int getCountofAddGoodscar_2(String date) {
        // TODO Auto-generated method stub
        String sql1 = "select count(t1.id) as count from " + " (select * from user where createtime<?) as t1"
                + " inner join (select userid from behavior_record where action='Add To Cart' and view_date_day =? and userid!=0 group by userid) as t2 on t1.id = t2.userid";
        // String sql2 = "select count(*) as count from (select * from goods_car
        // where date(datatime) = date_format(?,'%Y-%m-%d') and userid!=0 group
        // by userid) as t";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs1 = null;
        PreparedStatement stmt1 = null;
        int res1 = 0;
        try {
            stmt1 = conn.prepareStatement(sql1);
            stmt1.setString(1, date);
            stmt1.setString(2, date);
            rs1 = stmt1.executeQuery();
            while (rs1.next()) {
                res1 = rs1.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
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
            DBHelper.getInstance().closeConnection(conn);
        }
        return res1;
    }

    @Override
    public List<Object> getUserofAddGoodscar_2(String date) {
        // TODO Auto-generated method stub
        // String date1=dateTransform(date);
        String sql = "select t1.*,t2.userid,count(gc.id) as count,t3.recipients,t3.address,t3.phone_number,t3.statename from "
                + " (select * from user where createtime<?) as t1 "
                + " inner join (select userid from behavior_record where action='Add To Cart' and view_date_day =? and userid!=0 group by userid) as t2 on t1.id = t2.userid "
                + " left join goods_car gc on t1.id=gc.userid and gc.state!=9 and gc.state!=1"
                + " left join address as t3 on t1.id= t3.userid" + " group by t1.id";
        ;
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Object> res = new ArrayList<Object>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UserBean tempuser = new UserBean();
                tempuser.setId(rs.getInt("id"));
                tempuser.setName(rs.getString("name"));
                tempuser.setEmail(rs.getString("email"));
                tempuser.setCount(rs.getString("count"));
                Address tempaddr = new Address();
                tempaddr.setRecipients(rs.getString("recipients"));
                tempaddr.setAddress(rs.getString("address"));
                tempaddr.setPhone_number(rs.getString("phone_number"));
                tempaddr.setStatename(rs.getString("statename"));
                res.add(tempuser);
                res.add(tempaddr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    private String dateTransform(String date) {
        Calendar cl = Calendar.getInstance();
        cl.set(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]) - 1,
                Integer.parseInt(date.split("-")[2]) + 1);
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH) + 1;
        int day = cl.get(Calendar.DATE);
        String date1 = year + "-" + month + "-" + day;
        return date1;
    }

    @Override
    public List<Object[]> getOrderClass(String orderNo) {
        String sql = "select od.goodsprice,od.yourorder,goods_class,goodsid,discount_amount from orderinfo oi,order_details od where oi.order_no=? and oi.state=5 and od.state!=2 and od.orderid=oi.order_no ";
        String sql2 = "select goodId,ropType,oldValue,newValue,status,dateline from order_change where orderNo=? and goodId=? and (ropType=1 or ropType=3 or ropType=4) and status=1 and del_state=0 order by id asc";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            stmt2 = conn.prepareStatement(sql2);
            while (rs.next()) {
                boolean a = true;
                Object[] objects = new Object[4];
                objects[0] = rs.getString("goodsprice");
                objects[1] = rs.getInt("yourorder");
                objects[2] = rs.getString("goods_class");
                objects[3] = rs.getString("discount_amount");
                stmt2.setString(1, orderNo);
                stmt2.setInt(2, rs.getInt("goodsid"));
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    int ropType = rs2.getInt("ropType");
                    String newValue = rs2.getString("newValue");
                    switch (ropType) {
                        case 1:// 价格变动
                            objects[0] = newValue;
                            break;
                        case 3:// 最小定量
                            objects[1] = newValue;
                            break;
                        case 4:// 取消商品
                            a = false;
                            break;
                        default:
                            LOG.error("switch ropType is error"+ropType);
                            break;
                    }
                }
                if (a) {
                    list.add(objects);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    /*
     * @Override public int addDiscountChenge(String orderNo,String
     * sprice,String price,String info){ String sql =
     * "insert discount_chenge(orderno,sprice,price,info,createtime) values(?,?,?,?,now())"
     * ; Connection conn = DBHelper.getInstance().getConnection(); PreparedStatement stmt =
     * null; int res = 0; try { stmt = conn.prepareStatement(sql);
     * stmt.setString(1, orderNo); stmt.setString(2, sprice); stmt.setString(3,
     * price); stmt.setString(4, info); res = stmt.executeUpdate(); } catch
     * (Exception e) { e.printStackTrace(); } finally { if (stmt != null) { try
     * { stmt.close(); } catch (SQLException e) { e.printStackTrace(); } }
     * DBHelper.getInstance().closeConnection(conn); } return res; }
     */

    @Override
    public List<CodeMaster> getLogisticsInfo() {

        String sql = "select * from codemaster where code_type='001' and delete_flg=0 ";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<CodeMaster> logisticsList = new ArrayList<CodeMaster>();
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CodeMaster codeMaster = new CodeMaster();
                codeMaster.setCodeId(rs.getString("code_id"));
                codeMaster.setCodeName(rs.getString("code_name"));
                logisticsList.add(codeMaster);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return logisticsList;
    }

    @Override
    public List<Object[]> getDeliveryWarningList(int adminid, int userid) {
        // TODO Auto-generated method stub
        String sql = "{call DeliveryWarning(?,?)}";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        CallableStatement cstmt = null;
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, adminid);
            cstmt.setInt(2, userid);
            rs = cstmt.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[15];
                obj[0] = rs.getString("order_no");
                obj[1] = rs.getDate("create_time");
                obj[2] = rs.getString("email");
                obj[3] = rs.getString("name");
                obj[4] = rs.getString("googs_img");
                obj[5] = rs.getInt("details_number");
                obj[6] = rs.getString("admName");
                obj[7] = rs.getString("orderremark");
                obj[8] = rs.getInt("state");
                obj[9] = rs.getFloat("product_cost");
                obj[10] = rs.getInt("purchase_number");
                obj[11] = rs.getInt("purchase_days");
                obj[12] = rs.getString("time");
                obj[13] = rs.getInt("user_id");
                obj[14] = rs.getString("buyuser");
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int updatePurchaseDays(String orderid, int days) {
        // TODO Auto-generated method stub
        String sql = "update orderinfo set purchase_days=? where order_no=?";
        int res = 0;

        List<String> lstValues = new ArrayList<>();
        lstValues.add(String.valueOf(days));
        lstValues.add(orderid);

        String runSql = DBHelper.covertToSQL(sql,lstValues);
        res=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

        return res;
    }

    @Override
    public int updateOrderState(String orderid, int state) {
        // TODO Auto-generated method stub
        String sql = "update orderinfo set state=? where order_no=?";
        int res = 0;


        List<String> lstValues = new ArrayList<>();
        lstValues.add(String.valueOf(state));
        lstValues.add(orderid);

        String runSql = DBHelper.covertToSQL(sql,lstValues);
        res=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

        return res;
    }

    @Override
    public List<String> getOrdersNos(int userID) {
        List<String> orderNos = new ArrayList<String>();
        String sql = "select order_no from orderinfo  where state=5 and user_id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                orderNos.add(rs.getString("order_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return orderNos;
    }

    @Override
    public int existOrders(String orderNo) {
        String sql = "select count(orderid) counts  from orderinfo o where order_no = ? ";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("counts");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int addOrder_reductionfreight(String orderNo, double freight_price) {
        String sql = "INSERT INTO order_reductionfreight(orderNo,freight_price,createtime) SELECT ?,?,now() FROM order_reductionfreight WHERE NOT EXISTS(SELECT freight_price from order_reductionfreight WHERE orderno = ?) limit 1";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            stmt.setDouble(2, freight_price);
            stmt.setString(3, orderNo);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int upOrder_reductionfreight(String orderNo, double price, String remark) {
        String sql = "update order_reductionfreight set price=?,remark=? where orderno = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, price);
            stmt.setString(2, remark);
            stmt.setString(3, orderNo);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public double getOrder_reductionfreight(String orderNo) {
        String sql = "select max(freight_price) from order_reductionfreight where orderno = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        double res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getDouble("freight_price");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Object[]> CheckUnpaidOrder() {
        String sql = "{call CheckUnpaidOrder}";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        CallableStatement cstmt = null;
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            cstmt = conn.prepareCall(sql);
            rs = cstmt.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[15];
                obj[0] = rs.getInt("orderid");
                obj[1] = rs.getString("order_no");
                obj[2] = rs.getString("state");
                obj[3] = rs.getInt("id");
                obj[4] = rs.getString("name");
                obj[5] = rs.getString("email");
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<String> getOrderNos(int userid, String orderid) {
        // 5-确认价格中,1-购买中,2-已到仓库,0-等待付款,3-出运中,4-完结,-1-台后取消订单,6-客户取消订单,7-预订单
        String sql = "select order_no from orderinfo where (state=5 or state=1 or state=2) and user_id = ? and order_no<>? ORDER BY orderid desc  limit 10";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> orderNos = new ArrayList<String>();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            stmt.setString(2, orderid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                orderNos.add(rs.getString("order_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeStatement(stmt);

            DBHelper.getInstance().closeConnection(conn);
        }
        return orderNos;
    }

    @Override
    public String getOrdersEvaluateByOrderNo(String orderNo) {
        String sql = "select e.evaluate  from evaluate e  where order_no=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res += rs.getString("evaluate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<Shipment> validateShipmentList(List<Shipment> list, String uuid) {
        List<Shipment> passList = new ArrayList<Shipment>();
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        for (Shipment shipment : list) {
            String sql = "select * from forwarder where express_no = '" + shipment.getOrderno() + "'";
            String updateShipMentSql = "update shipment set validate = 1 where orderNo = '" + shipment.getOrderno()
                    + "' and uuid = '" + uuid + "'";
            String updateForwarderSql = "update forwarder set bulkWeight = " + shipment.getBulkweight()
                    + ",settleWeight=" + shipment.getSettleweight() + ",shipprice= " + shipment.getTotalprice()
                    + " where express_no = '" + shipment.getOrderno() + "' and order_no = morderno";
            try {
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    stmt = conn.prepareStatement(updateShipMentSql);
                    stmt.executeUpdate();
                    stmt = conn.prepareStatement(updateForwarderSql);
                    stmt.executeUpdate();
                } else {
                    passList.add(shipment);
                }
            } catch (Exception e) {
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
        return passList;
    }

    @Override
    public OrderBean getOrder_remainingPrice(String orderNo) {
        Connection conn = DBHelper.getInstance().getConnection();
        String sql = "select order_ac,remaining_price from orderinfo where order_no = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrderBean order = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                order = new OrderBean();
                order.setOrderNo(orderNo);
                order.setOrder_ac(rs.getDouble("order_ac"));
                order.setRemaining_price(rs.getDouble("remaining_price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return order;
    }

    @Override
    public int upOrder_remainingPrice(String orderNo, double remainingPrice, double order_ac) {

        String sql = "update orderinfo set order_ac=?,remaining_price=? where order_no = ?";

        int result = 0;


        List<String> lstValues = new ArrayList<>();
        lstValues.add(String.valueOf(order_ac));
        lstValues.add(String.valueOf(remainingPrice));
        lstValues.add(orderNo);

        String runSql = DBHelper.covertToSQL(sql,lstValues);
        result=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
        return result;
    }

    public List<OrderBean> getOrderChangeState(String orderNo) {
        List<OrderBean> list = new ArrayList<OrderBean>();
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        // String sql = "select ropType,oldValue,del_state from order_change
        // where orderNo='"+orderNo+"' and ropType=5 order by dateline desc
        // limit 1";
        String sql = "select t1.orderNo,ropType,oldValue,del_state from ( select orderNo, max(dateline) maxDateline from order_change where ropType=5 and del_state=0   group by orderNo ) t1 "
                + " inner join order_change t2 on t1.orderno = t2.orderno and t1.maxDateline = t2.dateline and t2.orderNo in ( "
                + orderNo + " ) and t2.ropType=5 and t2.del_state=0";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OrderBean ob = new OrderBean();
                ob.setOrderNo(rs.getString("orderNo"));
                ob.setRopType(rs.getInt("ropType"));
                ob.setOldValue(rs.getString("oldValue"));
                ob.setDel_state(rs.getInt("del_state"));
                list.add(ob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    public String queryCountryNameByOrderNo(String orderNo) {
        if ("".equals(orderNo) || orderNo == null) {
            return "USA";
        }
        String sql = "select country from zone where id = (select country from order_address where orderNo = '"
                + orderNo + "' limit 1)";
        String str = "";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                str = rs.getString("country");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return str;
    }

    public List<Integer> getRepeatUserid(int id) {
        List<Integer> list = new ArrayList<Integer>();
        if (id == 0) {
            return list;
        }
        String sql1 = "SELECT SUBSTR(email, 1, instr(email, '@')-1) as email from user where id=" + id; // email
        String sql2 = "select DISTINCT(userid),orderdesc from payment where orderdesc in (select DISTINCT(orderdesc) from payment where  userid="
                + id + " and orderdesc like '%@%' and orderdesc<>'wanyangnumberone@163.com') and userid<>" + id; // paypal
        String sql3 = "SELECT DISTINCT(userid) from address where (address in (select DISTINCT(address) from address where userid="
                + id + ") " + "or phone_number in (select DISTINCT(phone_number) from address where userid=" + id
                + ")) and userid<>" + id; // address phone_number
        String email = "";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement pst1 = null, pst2 = null, pst3 = null, pst4 = null;
        ;
        ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null;
        try {
            // 当前订单用户邮箱
            pst1 = conn.prepareStatement(sql1);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                email = rs1.getString("email");
            }
            // paypal一致
            pst2 = conn.prepareStatement(sql2);
            rs2 = pst2.executeQuery();
            while (rs2.next()) {
                list.add(rs2.getInt("userid"));
            }
            // 地址电话 一致
            pst3 = conn.prepareStatement(sql3);
            rs3 = pst3.executeQuery();
            while (rs3.next()) {
                int uid = rs3.getInt("userid");
                if (!list.contains(uid)) {
                    list.add(uid);
                }
            }
            // 邮箱相似 (排除 sales info contact 开头的邮箱)
            if (!"".equals(email)) {
                String sql4 = "select DISTINCT(id) from user where email like '" + email
                        + "@%' and email not like 'sales@%' and email not like 'info@%' and email not like 'contact@%' and id<>"
                        + id;
                pst4 = conn.prepareStatement(sql4);
                rs4 = pst4.executeQuery();
                while (rs4.next()) {
                    int uid = rs4.getInt("id");
                    if (!list.contains(uid)) {
                        list.add(uid);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs1);
            DBHelper.getInstance().closePreparedStatement(pst1);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    // 查询客户国家 String orderno 格式： 'orderno1','orderno2','orderno3'
    public List<Map<String, String>> getCustCountry(String orderno) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String sql = "SELECT orderno,z.country from order_address ad LEFT JOIN zone z on ad.country=z.id where orderno in("
                + orderno + ");";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orderno", rs.getString("orderno"));
                map.put("country", rs.getString("country"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<Map<String, String>> getBuyerByOrderNo(String orderno) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String sql = "SELECT DISTINCT g.odid,ad.admName FROM goods_distribution g LEFT JOIN order_details od ON g.orderid=od.orderid and g.odid=od.id"
                + " LEFT JOIN admuser ad ON g.admuserid=ad.id WHERE od.id in (" + orderno + ")";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("odid", rs.getString("odid"));
                map.put("admName", rs.getString("admName"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    /**
     * 根据订单号获取IPN付款信息
     * @param orderNo
     * @return
     */
    @Override
    public int getIpnPaymentStatus(String orderNo) {
        int status=0;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql="";
        try{
            sql="SELECT GROUP_CONCAT(orderdesc) as orderdesc FROM payment WHERE orderid=?";
            stmt=conn.prepareStatement(sql);
            stmt.setString(1,orderNo);
            rs=stmt.executeQuery();
            if(rs.next() && StringUtil.isNotBlank(rs.getString("orderdesc")) && rs.getString("orderdesc").contains("余额足够抵扣订单金额")){
                status=1;
            }else{
                sql="select id from ipn_info where paymentStatus = 1 and orderNo=?";
                stmt=conn.prepareStatement(sql);
                stmt.setString(1,orderNo);
                rs=stmt.executeQuery();
                if(rs.next()){
                    status=1;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return status;
    }

    @Override
    public List<QualityResult> openCheckResult(Map<String, String> map) {
        List<QualityResult> list = new ArrayList<QualityResult>();
        String sql = "select goodscatid,createtime,admName,result from quality_result where orderid=? and goodsid=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        OrderBean orderBean = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, map.get("orderid"));
            pst.setString(2, map.get("goodsid"));
            rs = pst.executeQuery();
            while (rs.next()) {
                QualityResult q = new QualityResult();
                Map<String, String> maps = new HashMap<String, String>(50);
                q.setCatid(rs.getString("goodscatid"));
                q.setCreatetime(rs.getString("createtime"));
                q.setAdmName(rs.getString("admName"));
                String results = rs.getString("result");
                String[] strs = results.split("&");
                for (int i = 0; i < strs.length; i++) {
                    String[] str = strs[i].split(":");
                    maps.put(str[0], str[1]);
                }
                q.setResult(maps);
//				maps.clear();
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    /**
     * 采购页面查询订单支付金额等信息
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> queryOrderAmount(String orderNo) {
        DecimalFormat df = new DecimalFormat("#0.###");
        Map<String, String> map=new HashMap<String,String>();
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double es_price=0.00;
        double weight=0.00;
        try{
            String sql="SELECT cast(if(oi.memberFee>=10,oi.pay_price-oi.memberFee,oi.pay_price)*if(oi.exchange_rate<=0,6.3,oi.exchange_rate) as decimal(10,2)) AS pay_price," +
                    "SUM(ops.goods_p_price*ops.buycount) as sumPrice,COUNT(DISTINCT od.goods_pid)*5 as pidAmount FROM orderinfo oi " +
                    " INNER JOIN order_details od ON oi.order_no=od.orderid" +
                    " LEFT JOIN order_product_source ops ON od.orderid=ops.orderid AND od.goodsid=ops.goodsid" +
                    " WHERE oi.order_no='"+orderNo+"' AND od.state in (0,1)";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            if(rs.next()){
                map.put("pay_price",rs.getString("pay_price"));
                if(rs.getDouble("sumPrice")<=0){
                    map.put("acAmount","--");
                }else{
                    map.put("acAmount",df.format(rs.getDouble("sumPrice")+rs.getDouble("pidAmount")));
                }
            }else{
                map.put("pay_price","--");
                map.put("acAmount","--");
            }
            //预计采购金额
            sql="SELECT od.goodsid,od.od_total_weight,od.yourorder,ifnull(a.wholesale_price,0) AS a_price,ifnull(b.wholesale_price,0) AS b_price,ifnull(a.weight,ifnull(b.weight,0)) as weight  " +
                    " FROM order_details od LEFT JOIN custom_benchmark_ready a ON od.goods_pid=a.pid " +
                    " LEFT JOIN custom_benchmark_ready_delete b ON od.goods_pid=b.pid " +
                    " WHERE od.orderid='"+orderNo+"' AND od.state in (0,1)";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                String a_price=rs.getString("a_price");
                String b_price=rs.getString("b_price");
                a_price= StringUtil.isBlank(a_price)?b_price:a_price;
                int yourorder=rs.getInt("yourorder");
                String price=StringUtil.getEsPrice(a_price);
                es_price+=Double.parseDouble(price)*yourorder;
                weight+=rs.getDouble("od_total_weight");
            }
            String pidAmount="0.00";
            sql="SELECT COUNT(DISTINCT ods.goods_pid)*5 as amount FROM order_details ods  WHERE ods.orderid='"+orderNo+"' AND ods.state in (0,1)";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            if(rs.next()){
                pidAmount=rs.getString("amount");
            }
            map.put("es_price",df.format(es_price+Double.valueOf(pidAmount)));
            map.put("weights",df.format(weight));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
        }
        return map;
    }

    @Override
    public int checkOrderState(String orderNo,String isDropshipOrder1) {
        int state=-2;
        String sql = "select state from orderinfo where order_no=?";
        //如果 ！= 0 则是dropship 订单查询
        if("1".equals(isDropshipOrder1)){
            sql = "select state from dropshiporder where child_order_no=?";
        }
        Connection conn = DBHelper.getInstance().getConnection2();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, orderNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                state=rs.getInt("state");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closePreparedStatement(pst);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return state;
    }

    @Override
    public OrderBean getStateByOrderNo(int userid, String orderno) {
        String sql = "SELECT DISTINCT user_id,state,order_no,pay_price " + "from orderinfo where state=5 and user_id=? "
                + "and order_no=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        OrderBean orderBean = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, userid);
            pst.setString(2, orderno);
            rs = pst.executeQuery();
            while (rs.next()) {
                orderBean = new OrderBean(userid, rs.getString("order_no"), rs.getInt("state"));
                orderBean.setPay_price(rs.getDouble("pay_price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return orderBean;
    }

    @Override
    public List<Admuser> getAllBuyer() {
        List<Admuser> list = new ArrayList<Admuser>();
        String sql = "SELECT * FROM admuser WHERE roleType=3 AND status=1";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Admuser adm = new Admuser();
                adm.setId(rs.getInt("id"));
                adm.setAdmname(rs.getString("admName"));
                list.add(adm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int changeBuyer(int odid, int admuserid) {
        String sql = "update goods_distribution set admuserid=?  where odid = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, admuserid);
            stmt.setInt(2, odid);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int changeOrderBuyer(String orderid, int admuserid, String odids) {
        String sql = "update goods_distribution set admuserid=" + admuserid + "  where orderid = '" + orderid
                + "' and odid in (" + odids + ")";
        if ("0".equals(odids)) {
            sql = "update goods_distribution set admuserid=" + admuserid + "  where orderid = '" + orderid + "'";
        }
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = conn.prepareStatement(sql);
            // stmt.setInt(1, admuserid);
            // stmt.setString(2, orderid);
            // stmt.setString(3, goodsids);
            res = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int addOrderDetails(String goodsid, String count, String newOrderid, String orderid, int admuserid) {
        String sql = "";
        IOrderAutoService preOrderAutoService = new PreOrderAutoService();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int row = 0;
        boolean flag = true;
        Connection conn = DBHelper.getInstance().getConnection2();
        try {
            sql = "select * from order_details where goodsid='" + goodsid + "' and orderid='" + newOrderid + "'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                flag = false;
                row = 1;
            }
            sql = "select * from order_details where goodsid='" + goodsid + "' and orderid='" + orderid + "'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next() && flag) {
                sql = " insert into order_details(goodsid,orderid,dropshipid,delivery_time,checkprice_fee,checkproduct_fee,state,fileupload,yourorder,userid,goodsname,goodsprice,goodsfreight,"
                        + "goodsdata_id,remark,goods_class,extra_freight,car_url,car_img,car_type,freight_free,od_bulk_volume,od_total_weight,discount_ratio,goodscatid,car_urlMD5,goods_pid,actual_weight) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                List<String> lstValues = new ArrayList<>(30);
                lstValues.add( goodsid);
                lstValues.add( newOrderid);
                lstValues.add( rs.getString("dropshipid"));
                lstValues.add( rs.getString("delivery_time"));
                lstValues.add( rs.getString("checkprice_fee"));
                lstValues.add( rs.getString("checkproduct_fee"));
                lstValues.add( "0");
                lstValues.add( rs.getString("fileupload"));
                lstValues.add( count);
                lstValues.add( rs.getString("userid"));
                String goodsName = rs.getString("goodsname");
                if(StringUtil.isNotBlank(goodsName)){
                    lstValues.add(GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(goodsName));
                }else{
                    lstValues.add("");
                }

                lstValues.add( rs.getString("goodsprice"));
                lstValues.add( rs.getString("goodsfreight"));
                lstValues.add( rs.getString("goodsdata_id"));
                lstValues.add( rs.getString("remark"));
                lstValues.add( rs.getString("goods_class"));
                lstValues.add( rs.getString("extra_freight"));
                lstValues.add( rs.getString("car_url"));
                lstValues.add( rs.getString("car_img"));

                String car_type = rs.getString("car_type");
                if(StringUtil.isNotBlank(car_type)){
                    lstValues.add(GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(car_type));
                }else{
                    lstValues.add("");
                }
                lstValues.add( rs.getString("freight_free"));
                String od_bulk_volume = rs.getString("od_bulk_volume");
                if(StringUtil.isNotBlank(od_bulk_volume)){
                    lstValues.add(GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(od_bulk_volume));
                }else{
                    lstValues.add("");
                }
                lstValues.add( rs.getString("od_total_weight"));
                lstValues.add( rs.getString("discount_ratio"));
                lstValues.add( rs.getString("goodscatid"));
                lstValues.add( rs.getString("car_urlMD5"));
                lstValues.add( rs.getString("goods_pid"));
                lstValues.add( rs.getString("actual_weight"));

                String runSql = DBHelper.covertToSQL(sql,lstValues);

                String rsStr = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
                if(org.apache.commons.lang3.StringUtils.isNotBlank(rsStr)){
                    row += Integer.parseInt(rsStr);
                }

//                row = stmt.executeUpdate();
                sql = "select id,goodsdata_id,goodscatid,car_url from order_details where orderid='" + newOrderid
                        + "' and goodsid='" + goodsid + "'";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                if (admuserid != 0 && rs.next()) {
                    Date date = new Date();
                    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = df1.format(date);
                    Timestamp CreateDate = Timestamp.valueOf(time);
                    GoodsDistribution gd = new GoodsDistribution();
                    gd.setOdid(Integer.valueOf(rs.getInt("id")));// order_detail
                    // id
                    gd.setAdmuserid(Integer.valueOf(admuserid));// admuser id
                    gd.setGoodsdataid(String.valueOf(rs.getString("goodsdata_id")));//
                    gd.setGoodsid(goodsid);// 商品id
                    gd.setCreateTime(CreateDate);// 创建记录时间
                    gd.setOrderid(newOrderid);// 工单号
                    gd.setGoodscatid(rs.getString("goodscatid"));
                    gd.setDistributionid("0");// 0代表自动分配1代表手动分配
                    gd.setCarUrl(rs.getString("car_url"));
                    preOrderAutoService.insertDG(gd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("addOrderDetails",e);
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return row;
    }

    @Override
    public int addAutoAdmuser(String orderid) {
        Connection con = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int admuserid = 0;
        String sql = "select admuserid from goods_distribution where orderid='" + orderid + "' limit 1";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                admuserid = rs.getInt("admuserid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBHelper.getInstance().closeConnection(con);
        return admuserid;
    }

    @Override
    public int addOrderInfo(String orderid, String newOrderid, int length) {
        String sql = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int row = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        try {
            sql = "select * from orderinfo where order_no='" + newOrderid + "'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                row = 1;
            } else {
                sql = "select * from order_address where orderNo='" + orderid + "'";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    sql = "insert into order_address (AddressID,orderNo,Country,statename,address,address2,phoneNumber,zipcode,Adstatus,Updatetimr,admUserID,street,recipients,flag) "
                            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = conn.prepareStatement(sql);

                    List<String> lstValues = new ArrayList<>();
                    lstValues.add(rs.getString("AddressID"));
                    lstValues.add(newOrderid);
                    lstValues.add(rs.getString("Country"));
                    lstValues.add(rs.getString("statename"));
                    lstValues.add(rs.getString("address"));
                    lstValues.add(rs.getString("address2"));
                    lstValues.add(rs.getString("phoneNumber"));
                    lstValues.add(rs.getString("zipcode"));
                    lstValues.add(rs.getString("Adstatus"));
                    lstValues.add( rs.getString("Updatetimr"));
                    lstValues.add( rs.getString("admUserID"));
                    lstValues.add( rs.getString("street"));
                    lstValues.add( rs.getString("recipients"));
                    lstValues.add( rs.getString("flag"));
                    String runSql = DBHelper.covertToSQL(sql,lstValues);
                    row=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
                }

                sql = "select * from orderinfo where order_no='" + orderid + "'";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (rs.next()) {
                    sql = "insert into orderinfo(order_no,user_id,product_cost,state,delivery_time,service_fee,ip,mode_transport,create_time,details_number,pay_price_three,"
                            + "foreign_freight,pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,share_discount,order_ac,actual_lwh,actual_weight,actual_weight_estimate,"
                            + "extra_freight,orderRemark,cashback,isDropshipOrder,address_id,packag_number,coupon_discount,orderpaytime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
                    List<String> lstValues = new ArrayList<>();
                    lstValues.add(newOrderid);
                    lstValues.add(rs.getString("user_id"));
                    lstValues.add("0");
                    lstValues.add("5");// 订单状态
                    lstValues.add(rs.getString("delivery_time"));
                    lstValues.add(rs.getString("service_fee"));
                    lstValues.add("0");
                    lstValues.add(rs.getString("mode_transport"));
                    lstValues.add(sdf.format(date).toString());
                    lstValues.add(String.valueOf(length));
                    lstValues.add( "-999");
                    lstValues.add( rs.getString("foreign_freight"));
                    lstValues.add( "0");
                    lstValues.add( rs.getString("pay_price_tow"));
                    lstValues.add( rs.getString("currency"));
                    lstValues.add( rs.getString("actual_ffreight"));
                    lstValues.add( rs.getString("discount_amount"));
                    lstValues.add( rs.getString("share_discount"));
                    lstValues.add( rs.getString("order_ac"));
                    lstValues.add( rs.getString("actual_lwh"));
                    lstValues.add( rs.getString("actual_weight"));
                    lstValues.add( rs.getString("actual_weight_estimate"));
                    lstValues.add( rs.getString("extra_freight"));
                    lstValues.add( rs.getString("orderRemark"));
                    lstValues.add( rs.getString("cashback"));
                    lstValues.add( "2");// 售后补货订单
                    lstValues.add( rs.getString("address_id"));
                    lstValues.add( rs.getString("packag_number"));
                    lstValues.add( rs.getString("coupon_discount"));

                    String runSql = DBHelper.covertToSQL(sql,lstValues);
                    row=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            row = 0;
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return row;
    }

    @Override
    public boolean deleteOrderInfo(String newOrderid) {
        boolean flag = true;
        PreparedStatement stmt = null;
        Connection conn = DBHelper.getInstance().getConnection2();
        String Dsql = "delete from orderinfo where order_no=?";

        List<String> lstValues = new ArrayList<>();
        lstValues.add(newOrderid);
        String runSql = DBHelper.covertToSQL(Dsql,lstValues);
        SendMQ.sendMsgByRPC(new RunSqlModel(runSql));


        Dsql = "delete from order_details where orderid=?";
        lstValues = new ArrayList<>();
        lstValues.add(newOrderid);
        runSql = DBHelper.covertToSQL(Dsql,lstValues);
        SendMQ.sendMsgByRPC(new RunSqlModel(runSql));

        Dsql = "delete from goods_distribution where orderid=?";
        lstValues = new ArrayList<>();
        lstValues.add(newOrderid);
        runSql = DBHelper.covertToSQL(Dsql,lstValues);
        SendMQ.sendMsgByRPC(new RunSqlModel(runSql));

        flag = true;

        return flag;
    }

    @Override
    public int isDropshipOrder(String orderNo) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        String qSql = "select count(*) from dropshiporder where parent_order_no=? or parent_order_no in"
                + "(select parent_order_no from dropshiporder where child_order_no = ?)";
        try {
            stmt = conn.prepareStatement(qSql);
            stmt.setString(1, orderNo);
            stmt.setString(2, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return count;
    }

    @Override
    public int isCloseDropshipOrder(String orderNo) throws Exception {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        String qSql = "select count(*) from dropshiporder where child_order_no = ? and  state=? ";
        try {
            stmt = conn.prepareStatement(qSql);
            stmt.setString(1, orderNo);
            stmt.setString(2, OrderInfoConstantUtil.OFFLINECANCEL);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return count;
    }

    @Override
    public int isCloseByDropshipMainOrder(String mainOrderNo) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        String qSql = "select count(*) from orderinfo where order_no = ? and  state=? ";
        try {
            stmt = conn.prepareStatement(qSql);
            stmt.setString(1, mainOrderNo);
            stmt.setString(2, OrderInfoConstantUtil.OFFLINECANCEL);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return count;
    }

    @Override
    public int closeDropshipOrder(int userId, String orderNo) throws Exception {
        PreparedStatement stmt = null;
        int rs = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        String qSql = "update dropshiporder set state = ? where child_order_no = ?";
        try {
            if (GetConfigureInfo.openSync()) {
                String syncSql = "update dropshiporder set state = '" + OrderInfoConstantUtil.OFFLINECANCEL
                        + "' where child_order_no ='" + orderNo + "'";
                SaveSyncTable.InsertOnlineDataInfo(0, orderNo, "取消dropship子订单", "dropshiporder", syncSql);
                rs = 1;
            } else {
                stmt = conn.prepareStatement(qSql);
                stmt.setString(1, OrderInfoConstantUtil.OFFLINECANCEL);
                stmt.setString(2, orderNo);
                rs = stmt.executeUpdate();
            }

        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public int closeDropshipOrderByMainOrderNo(String mainOrderNo) throws Exception {

        String upSql = "update dropshiporder set state = ? where parent_order_no = ?";

        if (GetConfigureInfo.openSync()) {
            String syncSql = "update dropshiporder set state = '" + OrderInfoConstantUtil.OFFLINECANCEL
                    + "' where parent_order_no='" + mainOrderNo + "'";
            SaveSyncTable.InsertOnlineDataInfo(0, mainOrderNo, "更新dropship主订单状态", "dropshiporder", syncSql);
            return 1;
        } else {
            List<String> lstValues = new ArrayList<>();
            lstValues.add(OrderInfoConstantUtil.OFFLINECANCEL);
            lstValues.add(mainOrderNo);
            String runSql = DBHelper.covertToSQL(upSql,lstValues);
            return Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
        }


    }

    @Override
    public int closeDropshipOrderGoodsByMainOrderNo(String mainOrderNo) throws Exception {

        PreparedStatement stmt = null;
        int rs = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        String upSql = "update order_details set state = ? where orderid = ?";
        try {
            stmt = conn.prepareStatement(upSql);
            stmt.setInt(1, OrderDetailsConstantUtil.CANCEL);
            stmt.setString(2, mainOrderNo);
            rs = stmt.executeUpdate();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public boolean checkDropshipIsCancel(String mainOrderNo) throws Exception {

        boolean isCancel = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = DBHelper.getInstance().getConnection2();
        String qSql = "select (select count(*) from dropshiporder where parent_order_no = ?"
                + " and (state = ? or state = ?)) csct,"
                + "(select count(*) from dropshiporder where parent_order_no = ? ) alct from dual";
        try {
            stmt = conn.prepareStatement(qSql);
            stmt.setString(1, mainOrderNo);
            stmt.setString(2, OrderInfoConstantUtil.OFFLINECANCEL);
            stmt.setString(3, OrderInfoConstantUtil.CUSTOMERCANCEL);
            stmt.setString(4, mainOrderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                isCancel = rs.getInt(1) == rs.getInt(2);
            }
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return isCancel;
    }

    @Override
    public int closeOrderByDropshipOrder(int userId, String orderNo) throws Exception {

        PreparedStatement stmt = null;
        int rs = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        String upSql = "update orderinfo set state= ? where order_no in(select parent_order_no "
                + "from dropshiporder where parent_order_no = ? or child_order_no = ?)";
        try {
            if (GetConfigureInfo.openSync()) {
                String syncSql = "update orderinfo set state= '" + OrderInfoConstantUtil.OFFLINECANCEL
                        + "' where order_no in(select parent_order_no " + "from dropshiporder where parent_order_no = '"
                        + orderNo + "' or child_order_no = '" + orderNo + "')";
                SaveSyncTable.InsertOnlineDataInfo(userId, orderNo, "取消dropship主订单", "orderinfo", syncSql);
                rs = 1;
            } else {
                stmt = conn.prepareStatement(upSql);
                stmt.setString(1, OrderInfoConstantUtil.OFFLINECANCEL);
                stmt.setString(2, orderNo);
                stmt.setString(3, orderNo);
                rs = stmt.executeUpdate();
            }

        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public int closeOrderGoodsByDropshipOrder(int userId, String orderNo) throws Exception {

        PreparedStatement stmt = null;
        int rs = 0;
        Connection conn = DBHelper.getInstance().getConnection2();
        String upSql = "update order_details set state=? where dropshipid = ?";
        try {
            if (GetConfigureInfo.openSync()) {
                String syncSql = "update order_details set state= '" + OrderDetailsConstantUtil.CANCEL
                        + "' where dropshipid ='" + orderNo + "'";
                SaveSyncTable.InsertOnlineDataInfo(userId, orderNo, "更新dropship子订单商品状态", "order_details", syncSql);
                rs = 1;
            } else {
                stmt = conn.prepareStatement(upSql);
                stmt.setInt(1, OrderDetailsConstantUtil.CANCEL);
                stmt.setString(2, orderNo);
                rs = stmt.executeUpdate();
            }
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public String queryMainOrderByDropship(String orderNo) throws Exception {

        String mainOrderNo = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = DBHelper.getInstance().getConnection2();
        String qSql = "select parent_order_no from dropshiporder where " + "parent_order_no = ? or child_order_no = ?";
        try {
            stmt = conn.prepareStatement(qSql);
            stmt.setString(1, orderNo);
            stmt.setString(2, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                mainOrderNo = rs.getString(1);
            }
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return mainOrderNo;
    }

    @Override
    public int judgeOrderState(String orderid) {
        int state = 0;
        PreparedStatement stmt = null;
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        try {
            String sql = "select state from orderinfo where order_no='" + orderid + "'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                state = rs.getInt("state");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return state;
    }

    @Override
    public int updateMainOrderNoTotalPriceAndFreight(int userId, String mainOrderNo, float totalPrice, float orderAc,float extraFreight,
                                                     float weight) throws Exception {

        int rs = 0;
        String qSql = "update orderinfo set product_cost = convert(product_cost, decimal(11,2)) - ? ," +
                "extra_freight = convert(extra_freight, decimal(11,2)) - ?,order_ac = "
                + "convert(order_ac, decimal(11,2)) - ? ,pay_price = convert(pay_price, decimal(11,2)) - ? "
                + ",actual_weight_estimate = actual_weight_estimate - ? where order_no = ? ";
        float childPrice = new BigDecimal(totalPrice + orderAc + extraFreight).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        if (GetConfigureInfo.openSync()) {
            String syncSql = "update orderinfo set product_cost = convert(product_cost, decimal(11,2)) - "
                    + totalPrice + " ,order_ac = convert(order_ac, decimal(11,2)) - " + orderAc
                    + " ,pay_price = convert(pay_price, decimal(11,2)) - " + childPrice
                    + ",actual_weight_estimate = actual_weight_estimate - " + weight + " where order_no = '"
                    + mainOrderNo + "'";
            SaveSyncTable.InsertOnlineDataInfo(userId, mainOrderNo, "更新dropship主订单总价", "orderinfo", syncSql);
            rs = 1;
        } else {
            List<String> lstValues = new ArrayList<>();
            lstValues.add(String.valueOf(totalPrice));
            lstValues.add(String.valueOf(extraFreight));
            lstValues.add(String.valueOf(orderAc));
            lstValues.add(String.valueOf(childPrice));
            lstValues.add(String.valueOf(weight));
            lstValues.add(mainOrderNo);


            String runSql = DBHelper.covertToSQL(qSql,lstValues);
            rs=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
        }


        return rs;
    }

    @Override
    public float queryGoodsPriceFromDetails(String orderNo) {
        String sql = "select CAST(ifnull(sum(yourorder * goodsprice),0) AS DECIMAL(11,2)) as total_price"
                + " from order_details where orderid=? and state <2";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        float total = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getFloat(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return total;
    }

    @Override
    public TabTransitFreightinfoUniteOur getFreightInfo(String countryNameCn, int isEub) {
        TabTransitFreightinfoUniteOur fo = new TabTransitFreightinfoUniteOur();
        String sql = "SELECT  *  from  tab_transit_freightinfo_unite_our "
                + "where     countrynamecn = ? and eub_type = ? limit 1";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, countryNameCn);
            stmt.setString(2, String.valueOf(isEub));
            rs = stmt.executeQuery();
            if (rs.next()) {
                fo.setJcexBaseWeight(rs.getDouble("jcex_base_weight"));
                fo.setJcexBasePrice(rs.getBigDecimal("jcex_base_price"));
                fo.setJcexRatioPrice(rs.getBigDecimal("jcex_ratio_price"));
                fo.setJcexRatioWeight(rs.getDouble("jcex_ratio_weight"));
                fo.setEubBasePrice(rs.getBigDecimal("eub_base_price"));
                fo.setEubBaseWeight(rs.getDouble("eub_base_weight"));
                fo.setEubRatioPrice(rs.getBigDecimal("eub_ratio_price"));
                fo.setEubRatioWeight(rs.getDouble("eub_ratio_weight"));
                fo.setEubType(rs.getInt("eub_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return fo;
    }

    @Override
    public int statisticsRegisterUser(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select count(0) as count from user where 1=1 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and createtime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and createtime <= '" + endDate + "'";
        }
        sql += " and id not in(select id from `user` where  is_test = 1)";
        if(ipFlag > 0){
            sql += " and id not in(select user_id from ip_record  where user_id > 0 and is_china =1)";
        }
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            
            DBHelper.getInstance().closeStatement(stmt);            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryRegisterUserDetails(String beginDate, String endDate, int startNum,
                                                              int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select a.*,b.buyForMeCarConfig from (select id,email,createtime from user where 1=1 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and createtime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and createtime <= '" + endDate + "'";
        }
        sql += " and id not in(select id from `user` where  is_test = 1)";
        if(ipFlag > 0){
            sql += " and id not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += ") a left join goods_carconfig b on a.id = b.userid and b.userid > 0 ";
        sql += " order by a.createtime desc";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("id"));
                bhDtl.setEmail(rs.getString("email"));
                bhDtl.setCreateTime(rs.getString("createtime"));
                String carStr = rs.getString("buyForMeCarConfig");
                if(org.apache.commons.lang3.StringUtils.isNotBlank(carStr)){
                    List<GoodsCarActiveSimplBean> listActive = (List<GoodsCarActiveSimplBean>) JSONArray.toCollection(JSONArray.fromObject(carStr), GoodsCarActiveSimplBean.class);
                    bhDtl.setCarNum(listActive.size());
                    listActive.clear();
                }
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int statisticsAddFirstAddress(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select count(0) as count from (select userid,count(userid) from address where 1=1 ";
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and createtime <= '" + endDate + "'";
        }
        sql += " and userid not in(select userid from address where 1=1";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and createtime <= '" + beginDate + "'";
        }
        sql += ") GROUP BY userid) a "
                + "where a.userid not in(select id from `user` where  is_test = 1)";
        if(ipFlag > 0){
            sql += " and a.userid not in(select user_id from ip_record  where user_id > 0 and is_china =1)";
        }
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryAddFirstAddressDetails(String beginDate, String endDate, int startNum,
                                                                 int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select a.userid,a.createtime,b.email from (select userid,count(userid),createtime from address where 1=1 ";
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and createtime <= '" + endDate + "'";
        }
        sql += " and userid not in(select userid from address where 1=1";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and createtime <= '" + beginDate + "'";
        }
        sql += ") GROUP BY userid ) a,user b "
                + "where a.userid not in(select id from `user` where  is_test = 1) and a.userid = b.id";
        if(ipFlag > 0){
            sql += " and a.userid not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += " order by a.createtime desc";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("userid"));
                bhDtl.setEmail(rs.getString("email"));
                bhDtl.setCreateTime(rs.getString("createtime"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int statisticsAddCarWithNoRegisterUser(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select count(0) as count from (select sessionid from goods_car where userid=0 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and datatime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and datatime <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and sessionid not in(select sessionid from ip_record  where user_id = 0 and is_china =1)";
        }
        sql += " group by sessionid) as a";
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryAddCarWithNoRegisterUserDetails(String beginDate, String endDate,
                                                                          int startNum, int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select userid,sessionid from (select sessionid,datatime from goods_car where userid=0 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and datatime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and datatime <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and sessionid not in(select sessionid from ip_record where user_id = 0 and sessionid is not null and is_china =1 )";
        }
        sql += " group by sessionid) as a";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("userid"));
                bhDtl.setEmail(rs.getString("sessionid"));
                bhDtl.setCreateTime(rs.getString("createtime"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int statisticsAddCarWithHasRegisterUser(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select count(0) as count from (select userid from goods_car where userid > 0 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and datatime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and datatime <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and userid not in(select user_id from ip_record where user_id > 0 and is_china = 1)";
        }
        sql += " group by userid) as b "
                + " where b.userid not in(select id from `user` where  is_test = 1)";
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryAddCarWithHasRegisterUserDetails(String beginDate, String endDate,
                                                                           int startNum, int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select b.userid,c.email as count from (select userid from goods_car where userid > 0 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and datatime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and datatime <= '" + endDate + "'";
        }
        sql += " group by userid) as b,user c "
                + " where b.userid not in(select id from `user` where  is_test = 1) and b.userid = c.id ";
        if(ipFlag > 0){
            sql += " and b.userid not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("userid"));
                bhDtl.setEmail(rs.getString("email"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int statisticsAddCarWithOldUser(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select count(0) as count from (select userid from goods_car where userid > 0 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and datatime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and datatime <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and userid not in(select user_id from ip_record where user_id > 0 and is_china = 1)";
        }
        sql += " group by userid) as b "
                + " where b.userid not in(select id from `user` where  is_test = 1)" +
                " and b.userid in(select user_id from orderinfo where create_time <= '" + endDate + "'"
                + "group by user_id) and b.userid in(select user_id from orderinfo where create_time <= '" + endDate + "'"
                + " group by user_id having count(user_id) > 1)";
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryAddCarWithOldUserDetails(String beginDate, String endDate, int startNum,
                                                                   int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select b.userid,c.email from (select userid from goods_car where userid > 0 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and datatime >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and datatime <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and userid not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += " group by userid) as b,user c "
                + " where b.userid not in(select id from `user` where  is_test = 1)"
                + " and b.userid = c.id and b.userid in(select user_id from orderinfo where create_time <= '" + endDate + "'" +
                " group by user_id having count(user_id) > 1)";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("userid"));
                bhDtl.setEmail(rs.getString("email"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int statisticsMakeOrderAllUser(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select count(0) as count from (select user_id,order_no from orderinfo where state in(1,2,3,4,5) ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and create_time >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and create_time <= '" + endDate + "'";
        }
        sql += " and locate('_',order_no) = 0 group by user_id) a  "
                + " where a.user_id not in(select id from `user` where  is_test = 1)";
        if(ipFlag > 0){
            sql += " and a.user_id not in(select user_id from ip_record where user_id > 0 and is_china = 1)";
        }
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryMakeOrderAllUserDetails(String beginDate, String endDate, int startNum,
                                                                  int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select a.user_id,a.create_time,b.email from (select user_id,order_no,create_time from orderinfo where state in(1,2,3,4,5) ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and create_time >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and create_time <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and user_id not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += " and locate('_',order_no) = 0  group by user_id) a,user b  "
                + " where a.user_id not in(select id from `user` where  is_test = 1) and a.user_id = b.id";
        sql += " order by a.create_time desc";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("user_id"));
                bhDtl.setEmail(rs.getString("email"));
                bhDtl.setCreateTime(rs.getString("create_time"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int statisticsMakeOrderNewUser(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;

        /*String sql = "select count(0) from (select user_id from orderinfo where " +
                " create_time> '" + beginDate + "' and " +
                " create_time< '" + endDate + "' and state in(1,2,3,4,5)" +
                " and user_id not in (select user_id from orderinfo where  " +
                " create_time< '" + beginDate + "' and state in(1,2,3,4,5)) " +
                " and user_id not in (select id from `user` where  is_test = 1)" +
                " group by user_id) a";*/
        String sql = "select count(0) from (select user_id from orderinfo where " +
                " orderpaytime >= '" + beginDate + "' and " +
                " orderpaytime <= '" + endDate + "' and state in(1,2,3,4,5)" +
                " and user_id not in (select user_id from orderinfo where  " +
                " orderpaytime < '" + beginDate + "' and state in(1,2,3,4,5)) " +
                " and user_id not in (select id from `user` where  is_test = 1)" ;
        if(ipFlag > 0){
            sql += " and user_id not in(select user_id from ip_record where user_id > 0 and is_china = 1)";
        }
        sql += " group by user_id) a";
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryMakeOrderNewUserDetails(String beginDate, String endDate, int startNum,
                                                                  int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select a.user_id,a.create_time,b.email from (select user_id,create_time from orderinfo where " +
                "  orderpaytime>= '" + beginDate + "'  and " +
                " orderpaytime<= '" + endDate + "' and state in(1,2,3,4,5)" +
                " and user_id not in (select user_id from orderinfo where " +
                " orderpaytime < '" + beginDate + "' and state in(1,2,3,4,5))" +
                " and user_id not in (select id from `user` where  is_test = 1)";
        if(ipFlag > 0){
            sql += " and user_id not in (select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += " group by user_id) a, user b where a.user_id = b.id order by a.create_time desc";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("user_id"));
                bhDtl.setEmail(rs.getString("email"));
                bhDtl.setCreateTime(rs.getString("create_time"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int statisticsRecentView(String beginDate, String endDate, int ipFlag) {
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "SELECT COUNT(a.pid) as counts FROM (SELECT r.sessionid,r.pid FROM recent_view r inner join " +
                "custom_benchmark_ready c on r.pid=c.pid WHERE r.createtime>='"+beginDate+"' " +
                    "AND r.createtime<='"+endDate+"' and c.valid=1 AND r.uid=0 AND r.sessionid IS NOT NULL ";
        if(ipFlag > 0){
            sql += " and r.sessionid not in(select sessionid from ip_record where user_id =0 and sessionid is not null and is_china =1)";
        }
        sql += "GROUP BY r.sessionid,r.pid,LEFT(r.createtime,10)) a ";
        int res=0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res += rs.getInt("counts");
            }
            sql="SELECT COUNT(a.pid) as counts FROM (SELECT r.uid,r.pid FROM recent_view r inner join custom_benchmark_ready c " +
                    "on r.pid=c.pid WHERE r.createtime>='"+beginDate+"' " +
                    "AND r.createtime<='"+endDate+"' and c.valid=1 AND r.uid<>0 " ;
            if(ipFlag > 0){
                sql += " and r.uid not in(select user_id from ip_record where user_id > 0 and is_china =1)";
            }
            sql += "GROUP BY r.uid,r.pid,LEFT(r.createtime,10)) a ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res += rs.getInt("counts");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public int statisticsPayOrderUser(String beginDate, String endDate, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select count(0) as count from (select userid from behavior_record where 1=1 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and view_date_day >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and view_date_day <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and userid not in(select user_id from ip_record where user_id > 0 and is_china = 1)";
        }
        sql += " and (action ='pay' or action ='1,pay for product|Pay for all' or action='Pay Now paypal' or action='Pay Now stripe') "
                + " and userid not in(select id from `user` where is_test = 1) group by userid)a";
        int res = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return res;
    }

    @Override
    public List<UserBehaviorDetails> queryUserPayLogDetails(String beginDate, String endDate, int startNum, int offSet, int ipFlag) {
        Connection conn = DBHelper.getInstance().getConnection();
        int num=0;
        ResultSet rs = null;
        Statement stmt = null;
        List<UserBehaviorDetails> list=new ArrayList<UserBehaviorDetails>();
        String sql="SELECT paylog.Operationtime,user.email as username,paylog.orderid,paylog.orderAmount FROM paylog ,user WHERE user.id = paylog.userid and paylog.userid IN(select id from user where is_test = 0) and paylog.userid not in (select user_id from ip_record where user_id > 0 and is_china =1)";
        try{
            if(StringUtil.isNotBlank(beginDate)){
                sql+=" and paylog.Operationtime>='"+beginDate+"'";
            }
            if(StringUtil.isNotBlank(endDate)){
                sql+=" and paylog.Operationtime<='"+endDate+"'";
            }
            sql+=" order by paylog.id desc ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UserBehaviorDetails u=new UserBehaviorDetails();
                u.setCreateTime(rs.getString("Operationtime"));
                u.setEmail(rs.getString("username"));
                u.setPid(rs.getString("orderid"));
                u.setOrderAmount(rs.getString("orderAmount"));
                list.add(u);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<UserBehaviorDetails> queryUserRecentView(String beginDate, String endDate, int startNum, int offSet, int ipFlag) {
        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "SELECT pid,b.counts FROM (\n" +
                "SELECT pid,COUNT(sessionid) AS counts FROM (\n" +
                "SELECT r.sessionid,r.pid FROM recent_view r inner join custom_benchmark_ready c " +
                "on r.pid=c.pid WHERE r.createtime>='" + beginDate + "' " +
                "AND r.createtime<='" + endDate + "' and c.valid=1 AND r.uid=0 AND r.sessionid IS NOT NULL\n";
        if (ipFlag > 0) {
            sql += " and r.sessionid not in(select sessionid from ip_record where sessionid is not null and is_china =1 and user_id = 0) ";
        }
        sql += "GROUP BY r.sessionid,r.pid,LEFT(r.createtime,10)) a GROUP BY a.pid\n" +
                "UNION\n" +
                "SELECT pid,COUNT(uid) AS counts FROM (\n" +
                "SELECT r.uid,r.pid FROM recent_view r inner join custom_benchmark_ready c on r.pid=c.pid  WHERE r.createtime>='" + beginDate + "' " +
                "AND r.createtime<='" + endDate + "' and c.valid=1 AND r.uid<>0 \n";
        if (ipFlag > 0) {
            sql += " and r.uid not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += "GROUP BY r.uid,r.pid,LEFT(r.createtime,10)) a GROUP BY a.pid ) b ORDER BY b.counts DESC ";
        if (offSet > 0) {
            sql += "LIMIT " + startNum + "," + offSet;
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("counts"));
                String pid = rs.getString("pid");
                bhDtl.setPid(pid);
                boolean flag = StringUtil.isNumeric(pid);
                bhDtl.setEmail("<a title='跳转到产品单页' target='_blank' href='https://www.import-express.com/goodsinfo/a-" + (flag ? "1" + pid + "" : "3" + pid + "") + ".html'>" + pid + "</a>");
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<UserBehaviorDetails> queryPayOrderUserDetails(String beginDate, String endDate, int startNum,
                                                              int offSet, int ipFlag) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select a.userid,a.view_date_day,b.email from (select userid,view_date_day from behavior_record where 1=1 ";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and view_date_day >= '" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and view_date_day <= '" + endDate + "'";
        }
        if(ipFlag > 0){
            sql += " and userid not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += " and (action ='pay' or action ='1,pay for product|Pay for all' or action='Pay Now paypal' or action='Pay Now stripe') "
                + " and userid not in(select id from `user` where  is_test = 1) group by userid)a,user b "
                + " where a.userid = b.id";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet + "";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails bhDtl = new UserBehaviorDetails();
                bhDtl.setUserId(rs.getInt("userid"));
                bhDtl.setEmail(rs.getString("email"));
                bhDtl.setCreateTime(rs.getString("view_date_day"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int queryUserPayLog(String beginDate, String endDate, int ipFlag) {
        Connection conn = DBHelper.getInstance().getConnection();
        int num=0;
        ResultSet rs = null;
        Statement stmt = null;
        String sql="SELECT COUNT(1) as counts FROM paylog WHERE userid IN (SELECT id FROM USER WHERE is_test = 0)";
        if(ipFlag > 0){
            sql += " and userid not in(select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        try{
            if(StringUtil.isNotBlank(beginDate)){
                sql+=" and Operationtime>='"+beginDate+"'";
            }
            if(StringUtil.isNotBlank(endDate)){
                sql+=" and Operationtime<='"+endDate+"'";
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
               num=rs.getInt("counts");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return num;
    }

    @Override
    public List<UserBehaviorBean> queryExistsBehaviorData(String beginDate, String endDate) {

        Connection conn = DBHelper.getInstance().getConnection();
        List<UserBehaviorBean> list = new ArrayList<UserBehaviorBean>(30);
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select statistics_num,statistics_date,type_flag from behavior_statistics_details "
                + "where statistics_date between date('" + beginDate + "') and date('" + endDate + "')";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorBean bhDtl = new UserBehaviorBean();
                bhDtl.setRecordDate(rs.getString("statistics_date"));
                bhDtl.setStatisticsNum(rs.getInt("statistics_num"));
                bhDtl.setTypeFlag(rs.getInt("type_flag"));
                list.add(bhDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }
    @Override
    public int queryBehaviorRecord(String beginDate, String endDate, int ipFlag) {
        Connection conn = DBHelper.getInstance().getConnection();
        int num=0;
        ResultSet rs = null;
        Statement stmt = null;
        String sql="SELECT count(id) as counts FROM behavior_record WHERE action = 'Add to Order' ";
        sql += " and ((userid > 0  and userid IN(select id from user where is_test = 0) ";
        if (ipFlag > 0) {
            sql += " and userid not in (select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += ")";
        sql += " or (userid = 0 and sessionid is not null))";
        try{
            if(StringUtil.isNotBlank(beginDate)){
                sql+=" and view_date_time>='"+beginDate+"'";
            }
            if(StringUtil.isNotBlank(endDate)){
                sql+=" and view_date_time<='"+endDate+"'";
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                num=rs.getInt("counts");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return num;
    }



    @Override
    public List<UserBehaviorDetails> queryUserAddToOrderDetails(String beginDate, String endDate, int startNum, int offSet, int ipFlag) {
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
        String sql = "select userid,sessionid,view_url,view_url_count,view_date_time from behavior_record where action = 'Add to Order' " +
                "and ((userid > 0  and userid IN(select id from user where is_test = 0) ";
        if (ipFlag > 0) {
            sql += " and userid not in (select user_id from ip_record where user_id > 0 and is_china =1)";
        }
        sql += ")";
        sql += " or (userid = 0 and sessionid is not null))";
        if (StringUtil.isNotBlank(beginDate)) {
            sql += " and view_date_time>='" + beginDate + "'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and view_date_time<='" + endDate + "'";
        }
        sql += " order by view_date_time desc ";
        if (offSet > 0) {
            sql += " limit " + startNum + "," + offSet;
        }
        System.err.println(sql);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UserBehaviorDetails u = new UserBehaviorDetails();
                u.setCreateTime(rs.getString("view_date_time"));
                u.setUserId(rs.getInt("userid"));
                u.setEmail(rs.getString("sessionid"));
                //u.setPid(rs.getString("view_url"));
                String view_url = rs.getString("view_url");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(view_url)) {

                    try {
                        u.setPid(view_url.substring(view_url.lastIndexOf("-") + 2, view_url.lastIndexOf(".html")));
                    } catch (Exception e) {

                    }
                }
                u.setCarNum(rs.getInt("view_url_count"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int check_user_info_by_type(String beginDate, String endDate, int type, int site) {
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        int count = 0;

        // 新注册
        String sql = "insert into check_user_info(user_id,create_time,email,country,site,type) " +
                "select a.*," + site + " as site," + type + " as type  from (select id,createtime,email,countryId from user where 1=1 ";
        if (site == 2) {
            sql += " and site in(2,3) ";
        } else {
            sql += " and site in(0,1)";
        }
        sql += " and createtime >= '" + beginDate + "'" +
                " and createtime < '" + endDate + "'" +
                " and id not in(select id from `user` where  is_test = 1)" +
                " and id not in(select user_id from ip_record where user_id > 0 and is_china =1)" +
                " ) a left join goods_carconfig b on a.id = b.userid and b.userid > 0 " +
                " order by a.createtime desc";
        if (type == 2) {
            // 新客户加购物车
            sql = "insert into check_user_info(user_id,create_time,email,country,site,type) " +
                    " select b.userid,b.datatime,c.email,c.countryId, " + site + " as site," + type + " as type " +
                    " from (select userid,datatime from goods_car where userid > 0 " +
                    " and datatime >=  '" + beginDate + "' " +
                    "        and datatime < '" + endDate + "' group by userid) as b,user c " +
                    " where b.userid not in(select id from `user` where  is_test = 1) and b.userid = c.id  ";
            if (site == 2) {
                sql += " and c.site in(2,3) ";
            } else {
                sql += " and c.site in(0,1)";
            }
            sql += " and b.userid not in(select user_id from ip_record where user_id > 0 and is_china =1) " +
                    "and b.userid not in(select user_id from orderinfo where orderpaytime < '" + beginDate + "' and state in(1,2,3,4,5) ";
            sql += " group by user_id having count(user_id) > 0)";
            sql += " and b.userid not in (select userid from goods_car where datatime < '"+beginDate+"')";
        } else if (type == 3) {
            // 新客户下单
            sql = "insert into check_user_info(user_id,create_time,email,country,site,type) " +
                    "select a.user_id,a.orderpaytime,b.email,b.countryId," + site + " as site," + type + " as type " +
                    "from (select user_id,orderpaytime from orderinfo where " +
                    "orderpaytime >= '" + beginDate + "'  and " +
                    "orderpaytime < '" + endDate + "' and state in(1,2,3,4,5)";

            if (site == 2) {
                sql += " and locate('K',order_no) = 8";
            } else {
                sql += " and locate('K',order_no) < 8 ";
            }
            sql += " and user_id not in (select user_id from orderinfo where " +
                    "orderpaytime < '" + beginDate + "' and state in(1,2,3,4,5))" +
                    "and user_id not in (select id from `user` where  is_test = 1)" +
                    "and user_id not in (select user_id from ip_record where user_id > 0 and is_china =1)" +
                    "group by user_id) a, user b where a.user_id = b.id ";
            if (site == 2) {
                sql += " and b.site in(2,3)";
            } else {
                sql += " and b.site in(0,1)";
            }
            sql += " order by a.orderpaytime desc ";
        }
        System.err.println(sql + ";");
        try {
            stmt = conn.createStatement();
            count = stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
        return count;
    }


}
