package com.cbt.auto.dao;

import com.cbt.auto.ctrl.AutoToSalePojo;
import com.cbt.auto.ctrl.OrderAutoBean;
import com.cbt.auto.ctrl.PureAutoPlanBean;
import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.onlinesql.ctr.SaveSyncTable;
import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.PaymentDao;
import com.cbt.pojo.GoodsDistribution;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.UniqueIdUtil;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.dao.OrderInfoDao;
import com.cbt.website.dao.OrderInfoImpl;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class OrderAutoDaoImpl implements OrderAutoDao {
	private static String[] countryList = {"UK","SE","PL","NO","NL","FR","FI","ES","DE","CH"};
	public static void main(String[] args) {
		boolean index = Arrays.asList(countryList).contains("22");
		System.out.println(index);
	}


	/**
	 * 获取未分配采购的订单
	 * （1）PayPal到了钱（不是Pending）或者（2）余额支付而且该客户余额不是负数而且账户余额基本等于应有余额，就直接启动
	 */
	@Override
	public List<String> getUnassignedOrders() {
		List<String> list=new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			//
			String sql="SELECT distinct oi.order_no"
					+" FROM orderinfo oi "
					+" INNER JOIN order_address oa ON oi.order_no=oa.orderNo"
					+" INNER JOIN zone z ON oa.country=z.country or oa.country=z.id"
					+" INNER JOIN USER u ON oi.user_id=u.id"
					+" INNER JOIN payment p ON oi.order_no=p.orderid "
					+" LEFT JOIN goods_distribution g ON oi.order_no=g.orderid"
					+" WHERE g.id IS NULL AND p.createtime>= DATE_ADD(NOW(),INTERVAL -30 DAY)"
					+" AND oi.user_id in (SELECT id FROM USER WHERE email NOT LIKE '%qq.com%' AND email NOT LIKE '%ww.com%' AND " +
					"email NOT LIKE 'test%'   AND  email NOT LIKE '%qq.ss' AND email NOT LIKE '%@q.ocm' AND " +
					"email NOT LIKE '%qqsss.com' AND  email NOT LIKE '%csmfg.com%'  AND  email NOT LIKE '%@sourcing-cn.com%'  AND " +
					"email NOT LIKE '%@china-synergy%'  AND email<>'sb33@gmail.com'  AND email<>'sbtest@gmail.com'  AND " +
					"email NOT LIKE '%@qq.co%' AND email NOT LIKE '%11.com' AND email NOT LIKE '%@qq.ocm' AND email NOT LIKE '%@163.com'   AND " +
					"email NOT LIKE 'zhouxueyun%') AND"
					+" oi.state>0 AND oi.state<6"
					+" AND u.available_m>=0 AND oi.ipnaddress is not null and p.paystatus=1 and  ((oi.ipnaddress !='' and oi.ipnaddress=z.shorthand and p.paytype=0) or p.paytype<>0)";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				String order_no=rs.getString("order_no");
				list.add(order_no);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null){
					stmt.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	@Override
	public Map<String, String> getAllOrderRefresh() {
		Connection conn = DBHelper.getInstance().getConnection();
		Connection conn1 = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		ResultSet rs1=null;
		try{
			SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
			StringBuilder pids=new StringBuilder();
			String sql="SELECT DISTINCT t.orderid,od.goods_pid,t.last_return "
						+" FROM orderinfo oi "
						+" INNER JOIN order_details od ON oi.order_no=od.orderid"
						+" INNER JOIN taobao_1688_order_history t ON od.goods_pid=t.itemid"
						+" WHERE oi.state>0 AND oi.state<6 "
						+" AND oi.isDropshipOrder=3 "
						+" AND od.state<2";// AND (t.last_return IS NULL OR t.last_return ='')
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				pids.append("'").append(rs.getString("goods_pid")).append("',");
			}
			if(pids.toString().length()>0){
				String sql1="SELECT DISTINCT a.1688_pid as pid,a.replacement_days FROM ali_info_data a "
					+" WHERE a.1688_pid IN ("+pids.toString().substring(0,pids.toString().length()-1)+")";
				stmt=conn1.prepareStatement(sql1);
				rs1=stmt.executeQuery();
				while(rs1.next()){
					String pid=rs1.getString("pid");
					String return_days=rs1.getString("replacement_days");
					sql="SELECT DISTINCT t.orderid,id.createtime FROM taobao_1688_order_history t"
						+" INNER JOIN id_relationtable id ON t.orderid=id.tborderid"
						+" where t.itemid='"+pid+"'";
					stmt=conn.prepareStatement(sql);
					rs=stmt.executeQuery();
					if(rs.next()){
						String createtime=rs.getString("createtime");
						if(StringUtils.isStrNull(return_days)){
							return_days=createtime.split("(\\s+)")[0]+" 23:59:59";
							Date date = s.parse(return_days);
						    Calendar calendar  =   Calendar.getInstance();
						    calendar.setTime(date); //需要将date数据转移到Calender对象中操作
						    calendar.add(calendar.DATE, 3);//把日期往后增加n天.正数往后推,负数往前移动 
						    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
							return_days=s.format(date)+" 23:59:59";
						}else{
							Date date = s.parse(createtime);
						    Calendar calendar  =   Calendar.getInstance();
						    calendar.setTime(date); //需要将date数据转移到Calender对象中操作
						    calendar.add(calendar.DATE, 2);//把日期往后增加n天.正数往后推,负数往前移动 
						    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
							return_days=s.format(date)+" 23:59:59";
						}
						sql="update taobao_1688_order_history set last_return='"+return_days+"' where orderid='"+rs.getString("orderid")+"'";
						stmt=conn.prepareStatement(sql);
						stmt.executeUpdate();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null){
					stmt.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(rs1!=null){
					rs1.close();
				}
				if(conn!=null){
					DBHelper.getInstance().closeConnection(conn);
				}
				if(conn1!=null){
					DBHelper.getInstance().closeConnection(conn1);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	/**
	 * 根据该订单号查询该订单的商品信息
	 */
	public List<OrderAutoBean> getAllOrderAutoInfo(String orderid) {
		List<OrderAutoBean> list=new ArrayList<OrderAutoBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="SELECT od.goodsid,od.id,od.goods_pid,od.goodsdata_id,od.goodscatid,od.userid,od.car_urlMD5 FROM order_details od WHERE od.orderid='"+orderid+"' AND od.state<2";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				OrderAutoBean oab=new OrderAutoBean();
				oab.setGoodsid(rs.getString("goodsid"));
				oab.setOdid(rs.getInt("id"));
				oab.setGoods_pid(rs.getString("goods_pid"));
				oab.setGoodsdataid(rs.getInt("goodsdata_id"));
				oab.setGoodscatid(rs.getString("goodscatid"));
				oab.setUserid(rs.getInt("userid"));
				oab.setCar_urlMD5(rs.getString("car_urlMD5"));
				list.add(oab);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}
	
	/**
	 * 从buy_goods_perday中获取昨天采购为完成的采购数量
	 */
	public Map getBeforeNoComplete(){
		Map map=new HashMap();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="SELECT adminid as adminid,unfinished as nocount FROM buy_goods_perday WHERE TO_DAYS( NOW( ) ) - TO_DAYS(logday)=1";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("adminid"), rs.getInt("nocount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return map;
	}
	/**
	 * 自动分配销售
	 * @author  王宏杰
	 * 2016-10-10
	 */
	public List<AutoToSalePojo> getOrderAutoToSale(){
		List userIdList=new ArrayList();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="SELECT DISTINCT o.user_id AS userid,u.name AS username,u.email AS email,pic.order_admin AS orderAdmin FROM orderinfo o INNER JOIN USER u ON o.user_id=u.id LEFT JOIN payment_invoice pic ON u.id=pic.userid WHERE o.orderpaytime> DATE_ADD(NOW(),INTERVAL -50 DAY) AND o.pay_price>100 AND o.state>0";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				AutoToSalePojo b=new AutoToSalePojo();
				b.setUserid(rs.getInt("userid"));
				b.setEmail(rs.getString("email"));
				b.setUsername(rs.getString("username"));
				b.setOrderAdmin(rs.getString("orderAdmin"));
				userIdList.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return userIdList;
	}
	
	/**
	 * 获取采购人员的采购数量前一天的
	 */
	public Map getAdmAutoCount(String time2){
		Map map=new HashMap();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="SELECT adminid,goodscount FROM buy_goods_perday  WHERE logday='"+time2+"'";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("adminid"), rs.getInt("goodscount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return map;
	}
	
	/**
	 * 获取前一天未完成的采购人对应的采购数量
	 */
	public Map getNoCompleteCount(){
		Map map=new HashMap();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="SELECT g.admuserid admuserid,COUNT(g.id) goodscount FROM goods_distribution g,admuser a WHERE a.roleType=3 AND a.`buyAuto`=1 AND a.id=g.`admuserid` AND a.`status`=1 AND g.createtime>DATE_ADD(NOW(),INTERVAL -1 DAY) AND g.iscomplete=0 GROUP BY g.admuserid";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getInt("admuserid"), rs.getInt("goodscount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return map;
	}

	@Override
	/**
	 * 查询商品是否被分配过采购人员
	 */
	public int getGoodsDistributionCount(String carUrl) {
	    int admuserid=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		//String sql="SELECT admuserid  FROM goods_distribution  WHERE id IN (SELECT MAX(id)  FROM  goods_distribution WHERE goods_url='"+carUrl+"')";
		String sql="SELECT admuserid  FROM goods_distribution  WHERE id IN (SELECT MAX(id)  FROM  goods_distribution WHERE goodscatid='"+carUrl+"')";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				admuserid=rs.getInt("admuserid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return admuserid;
	}

	@Override
	public Map<Integer, Integer> getLessSaler() {
		Map<Integer, Integer> map=new HashMap<Integer,Integer>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			String sql="SELECT adminid,COUNT(DISTINCT userid) as counts FROM admin_r_user WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(createdate) and adminid in (select id from admuser where roleType=4 and status=1 and buyAuto=1) GROUP BY adminid";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				int admuserid=rs.getInt("adminid");
				map.put(admuserid,rs.getInt("counts"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return map;
	}

	@Override
   	public Map<Integer, Integer> getAllBuyerCount() {
	    Map<Integer, Integer> map=new HashMap<Integer, Integer>();
		Calendar cal=Calendar.getInstance();
		String y=String.valueOf(cal.get(Calendar.YEAR));    
		String m=String.valueOf(cal.get(Calendar.MONTH)+1);    
		String d=String.valueOf(cal.get(Calendar.DATE)); 
		if(Integer.valueOf(m)<10){
			m="0"+m;
		}
		if(Integer.valueOf(d)<10){
			d="0"+d;
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql=" select admuserid,count(distinct goods_pid) as counts from goods_distribution where LEFT(createtime,10)='"+(y+"-"+m+"-"+d)+"' and admuserid in (SELECT id FROM admuser WHERE roleType=3 AND buyAuto=1 AND STATUS=1) group by admuserid;";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getInt("admuserid"), rs.getInt("counts"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return map;
   	}	
	
	public String getTwoCid(String goodsCatId){
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String cid="";
		String sql="SELECT path FROM ali_category WHERE cid =?";
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, goodsCatId);
			rs = stmt.executeQuery();
			while(rs.next()){
				cid=rs.getString("path");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return cid;
	}
	
	@Override
	/**
	 * 根据订单详情的goodscatid获取商品类别
	 * @param goodsCatId
	 * @return 商品类别
	 * @author 王宏杰
	 * @ 2016-09-23
	 */
	public String getAliCategoryType(String goodsCatId){
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String type="";
		String sql="SELECT DISTINCT a.cid FROM ali_category ac INNER JOIN ali_category a ON SUBSTRING_INDEX(ac.path, ',',1)=a.cid WHERE ac.cid=?";
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, goodsCatId);
			rs = stmt.executeQuery();
			while(rs.next()){
				type=rs.getString("cid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return type;
	}

	@Override
	public int insertDG(GoodsDistribution gd) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int result=0;
		try {
			String sql="select * from goods_distribution where orderid=? and odid=?";
		    stmt = conn.prepareStatement(sql);
		    stmt.setString(1,gd.getOrderid());
			stmt.setInt(2,gd.getOdid());
			rs = stmt.executeQuery();
			if(!rs.next()){
				sql = "insert into goods_distribution (orderid, odid, goodsid, admuserid, createtime, distributionid,goodsdataid,goods_pid,goodscatid) values(?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,gd.getOrderid());
				stmt.setInt(2,gd.getOdid());
				stmt.setString(3, gd.getGoodsid());
				stmt.setInt(4, gd.getAdmuserid());
				stmt.setString(5, gd.getCreateTime().toString());
				stmt.setString(6, gd.getDistributionid());
				stmt.setString(7, gd.getGoodsdataid());
				stmt.setString(8, gd.getGoods_pid());
				stmt.setString(9, gd.getGoodscatid());
				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result=0;
		}if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return result;
	}

	@Override
	public String getPayPalAddress(String orderNo) {
		String info="";
		String odAddress="";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int user_id=0;
		String sql="";
		try{
			sql="select oi.user_id,z.shorthand,i.ipnInfo from orderinfo oi inner join  order_address oa on oi.order_no=oa.orderNo inner join zone z on oa.country=z.country or oa.country=z.id left join  ipn_info  i on oa.orderNo=i.orderNo where oa.orderNo='"+orderNo+"'";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				info =rs.getString("ipninfo");
				odAddress=rs.getString("shorthand");
				user_id=rs.getInt("user_id");
				if(StringUtil.isNotBlank(info) && info.startsWith("{")){
					info=info.replace("{","").replace("}","");
					String [] infos=info.split(",");
					for(String s:infos){
						String [] data=s.split("=");
						if(data.length>=2 && "address_country_code".equals(data[0].replace(" ",""))){
							info=data[1];
						}
					}
				}
			}
			if(StringUtil.isNotBlank(odAddress) && StringUtil.isNotBlank(info)){
				if(Arrays.asList(countryList).contains(info) && info.equals(odAddress)){
					//自动确认到账
					insertPaymentConfirm(orderNo,"Ling", Utility.format(new Date(), Utility.datePattern1),"0",null,null,user_id);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return info;
	}
	@Override
	public int insertPaymentConfirm(String orderNo, String confirmname, String confirmtime,
	                                String paytype, String tradingencoding, String wtprice,
	                                int userId) {
		// TODO Auto-generated method stub
		String sql = "insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) SELECT ?,?,?,?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM paymentconfirm WHERE orderno='"+orderNo+"')";
		if("1".equals(paytype)|| paytype == "1"){
			IPaymentDao payDao = new PaymentDao();
			Payment pay = new Payment();
			pay.setUserid(userId);// 添加用户id
			pay.setOrderid(orderNo);
			pay.setPaystatus(5);// 添加付款状态
			pay.setPaymentid(tradingencoding);// 添加付款流水号（paypal返回的）
			pay.setPayment_amount(Float.parseFloat(wtprice.replaceAll("[^0-9/.]", "")));// 添加付款金额（paypal返回的）
			pay.setPayment_cc(wtprice.replaceAll("[^(A-Za-z)]", ""));// 添加付款币种（paypal返回的）
			pay.setPaySID(tradingencoding);
			pay.setPaytype(paytype);
			payDao.addPayment(pay);
			//更新订单状态
			OrderInfoDao orderDao = new OrderInfoImpl();
			try {
				orderDao.updateOrderStatu(userId, orderNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		//PreparedStatement stmt1 = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderNo);
			stmt.setString(2, confirmname);
			stmt.setString(3, confirmtime);
			stmt.setString(4, paytype);
			stmt.setString(5, tradingencoding);
			res = stmt.executeUpdate();
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String sqlStr = "insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) "
						+ "values('"+orderNo+"','"+confirmname+"','"+confirmtime+"','"+paytype+"','"+(tradingencoding==null ? "":tradingencoding)+"')";
				SaveSyncTable.InsertOnlineDataInfo(userId, orderNo, "确认到账详情", "paymentconfirm", sqlStr);
			} else{
				/*stmt1 = conn1.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt1.setString(1, orderNo);
				stmt1.setString(2, confirmname);
				stmt1.setString(3, confirmtime);
				stmt1.setString(4, paytype);
				stmt1.setString(5, tradingencoding);
				res = stmt1.executeUpdate();*/

				StringBuffer sqlBf = new StringBuffer();
				sqlBf.append("insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) select ");
				sqlBf.append("'" + orderNo + "' as order_no,");
				sqlBf.append("'" + confirmname + "' as confirmname,");
				sqlBf.append("'" + confirmtime + "' as confirmtime,");
				sqlBf.append("'" + paytype + "' as paytype,");
				sqlBf.append("'" + (tradingencoding == null ? "": tradingencoding ) + "' as tradingencoding");
				sqlBf.append(" FROM DUAL WHERE NOT EXISTS (SELECT * FROM paymentconfirm WHERE orderno='" + orderNo + "')");

				////使用MQ更新远程
				NotifyToCustomerUtil.sendSqlByMq(sqlBf.toString());
				////订单状态修改，通知客户
				NotifyToCustomerUtil.confimOrderPayment(userId, orderNo);

				res = 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			/*if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
			DBHelper.getInstance().closeConnection(conn);
			//DBHelper.getInstance().closeConnection(conn1);
		}
		return res;
	}

	@Override
	public int updateIsStockFlag() {
		Connection conn = DBHelper.getInstance().getConnection();
		Connection conn28 = DBHelper.getInstance().getConnection8();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="";
		try{

			String sqls="update custom_benchmark_ready set is_stock_flag=0 where is_stock_flag=1 and valid=1";
			SendMQ.sendMsg(new RunSqlModel(sqls));
			sqls="update custom_benchmark_ready set is_stock_flag=0 where is_stock_flag=1 and valid=1";
			stmt = conn.prepareStatement(sqls);
			stmt.executeUpdate();
//			sqls="update custom_benchmark_ready_newest set is_stock_flag=0 where is_stock_flag=1 and valid=1";
//			stmt = conn28.prepareStatement(sqls);
//			stmt.executeUpdate();
			sql=" SELECT DISTINCT i.goods_pid FROM inventory i\n" + "      LEFT JOIN storage_outbound_details s ON i.id=s.in_id AND s.type=1\n" + "      LEFT JOIN custom_benchmark_ready c ON i.goods_pid=c.pid\n" + "      WHERE 1=1\n" + "    AND IF(i.flag=1,i.new_remaining,i.remaining)>0";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				String goods_pid=rs.getString("goods_pid");
				sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				sql="update custom_benchmark_ready_newest set is_stock_flag=1 where pid='"+goods_pid+"'";
				stmt = conn28.prepareStatement(sql);
				stmt.executeUpdate();
				//线上表
				sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
				SendMQ.sendMsg(new RunSqlModel(sql));
			}

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return 0;
	}

	@Override
	public void queryChangeLogToAotu() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="";
		try{
			sql="select id,orderid,old_goodsid,new_goodsid from change_details_log where flag=0";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				sql="update goods_distribution set goodsid="+rs.getInt("new_goodsid")+" where goodsid="+rs.getInt("old_goodsid")+" and orderid='"+rs.getString("orderid")+"'";
				stmt=conn.prepareStatement(sql);
				int row=stmt.executeUpdate();
				if(row>0){
					sql="update change_details_log set flag=1 where id='"+rs.getInt("id")+"'";
					stmt=conn.prepareStatement(sql);
					stmt.executeUpdate();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
		}
	}

	@Override
	public void inventoryLock(String orderNo) {
		String sql = "SELECT distinct i.goods_pid,od.id AS odid,i.goods_p_price,i.id AS iid,od.orderid,od.yourorder,IFNULL(od.seilUnit,'piece') AS seilUnit,i.can_remaining,od.goodsid "
					+ " FROM order_details od  "
					+ " INNER JOIN inventory i ON ltrim(od.car_type)=i.sku AND od.goods_pid=i.goods_pid "
					+ " LEFT JOIN lock_inventory li ON li.od_id=od.id "
					+ " WHERE od.state=0 and  i.can_remaining>0 AND od.orderid='"+orderNo+"' AND li.id IS NULL group by od.id";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		ResultSet rs1=null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				String str=rs.getString("seilUnit").trim();
				String goods_pid=rs.getString("goods_pid");
				String goods_p_price=rs.getString("goods_p_price").indexOf(",")>-1?rs.getString("goods_p_price").split(",")[0]:rs.getString("goods_p_price");
				StringBuffer bf=new StringBuffer("1");
				if(str != null && !"".equals(str)){
					for(int i=0;i<str.length();i++){
						if(str.charAt(i)>=48 && str.charAt(i)<=57){
						   bf.append(str.charAt(i));
						}
					}
				}
				int use_remaining=0;
				if(rs.getInt("can_remaining")>=rs.getInt("yourorder")*Integer.valueOf(bf.toString())){
					use_remaining=rs.getInt("yourorder")*Integer.valueOf(bf.toString());
				}else if(rs.getInt("can_remaining")>0){
					use_remaining=rs.getInt("can_remaining");
				}
				sql="update inventory set can_remaining="+(rs.getInt("can_remaining")-use_remaining)+",od_id="+rs.getInt("odid")+" where id='"+rs.getInt("iid")+"'";
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				if((rs.getInt("can_remaining")-use_remaining)<=0){
					//如果库存锁定后可用库存为0则更新线上产品表。预上线表。后台产品表库存标识为无库存
					//本地
					sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
					stmt = conn.prepareStatement(sql);
					stmt.executeUpdate();
					//预上线表
					Connection conn28 = DBHelper.getInstance().getConnection8();
					sql="update custom_benchmark_ready_newest set is_stock_flag=0 where pid='"+goods_pid+"'";
					stmt = conn28.prepareStatement(sql);
					stmt.executeUpdate();
					DBHelper.getInstance().closeConnection(conn28);
					//线上表
					sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
					SendMQ.sendMsg(new RunSqlModel(sql));
				}
				//增加库存锁定记录
				sql="INSERT INTO lock_inventory (in_id,lock_remaining,od_id,createtime,lock_inventory_amount) select "+rs.getInt("iid")+","+use_remaining+","+rs.getInt("odid")+",now(),'"+(Double.valueOf(goods_p_price)*Integer.valueOf(use_remaining))+"' from dual where not exists (select * from lock_inventory where in_id='"+rs.getInt("iid")+"' and od_id='"+rs.getInt("odid")+"')";
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				//通过仓库确认库存
				InsertMessageNotification in=new InsertMessageNotification();
				String sendContent="订单【"+orderNo+"】的商品【"+rs.getInt("goodsid")+"】有库存可用，请及时确认并移库";
				in.insertMessageInsertByType(rs.getString("orderid"),sendContent,15,0,1,UniqueIdUtil.queryByDbForMessage());
			}
			sql="select od.id,od.goodsid,od.goods_pid from order_details od left join lock_inventory li on od.id=li.od_id where od.orderid=? and od.state=0 and li.id is null";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs=stmt.executeQuery();
			while(rs.next()){
				int od_id=rs.getInt("id");
				String goodsid=rs.getString("goodsid");
				String goods_pid=rs.getString("goods_pid");
				sql="SELECT i.barcode,i.can_remaining FROM inventory i INNER JOIN custom_benchmark_ready c ON i.goods_pid=c.ali_pid WHERE i.can_remaining>0 AND c.pid=? limit 1";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, goods_pid);
				rs1=stmt.executeQuery();
				if(rs1.next()){
					sql="update order_details set inventory_remark=该商品疑似有库存【"+rs1.getInt("can_remaining")+"】件,请至库位【"+rs1.getString("barcode")+"】查看' where id=?";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(1, od_id);
					stmt.executeUpdate();
				}
			}
		} catch (Exception e) {
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
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public OrderBean getUserOrderInfoByOrderNo(String orderNo) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		OrderBean ob=new OrderBean();
		String sql = " SELECT oi.order_no,u.email,u.id,ad.Email as adminEmail  FROM orderinfo oi " +
				" INNER JOIN USER u ON oi.user_id=u.id " +
				" INNER JOIN admin_r_user a ON u.id=a.userid  " +
				"INNER JOIN admuser ad ON a.adminid=ad.id " +
				"WHERE oi.order_no='"+orderNo+"'";
		try {
			stmt = conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				ob.setOrderNo(rs.getString("order_no"));
				ob.setUserEmail(rs.getString("email"));
				ob.setAdminemail(rs.getString("adminEmail"));
				ob.setUserid(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
		}
		return ob;
	}

	@Override
	public int insertBGP(OrderAutoBean o,String time) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String delsql="DELETE FROM buy_goods_perday WHERE adminid='"+o.getAdminid()+"' AND logday='"+time+"'";
		int result=0;
		String sql = "insert into buy_goods_perday (adminid, logday, goodscount) values(?,?,?)";
		try {
			stmt = conn.prepareStatement(delsql);
			stmt.execute();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,o.getAdminid());
			stmt.setString(2,o.getLogday().toString());
			stmt.setInt(3, o.getGoodscount());
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			result=0;
		}if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return result;
	}

	@Override
	public int insertPap(PureAutoPlanBean pap) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int result=0;
		String sql = "insert into pure_autoplan (autoState, autitime, orderid, orderno, paystatus, paytime) values(?,?,?,?,?,?)";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pap.getAutoState());
			stmt.setString(2,pap.getAutitime().toString());
			stmt.setString(3, pap.getOrderid());
			stmt.setString(4, pap.getOrderno());
			stmt.setInt(5, pap.getPaystatus());
			stmt.setString(6, pap.getPaytime().toString());
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			result=0;
		}if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return result;
	}

	@Override
	public List<AdmDsitribution> getALLAdmDsitribution() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<AdmDsitribution> list=new ArrayList<AdmDsitribution>();
		//String sql="SELECT gd.adminid AS adminid,SUM(gd.goodscount) AS COUNT FROM admuser ad INNER JOIN  buy_goods_perday  gd ON ad.id=gd.adminid  WHERE ad.buyAuto=1 AND ad.roleType=2 and ad.status=1 GROUP BY gd.adminid";
		String sql="SELECT admuserid as adminid,COUNT(DISTINCT goods_url) as COUNT FROM goods_distribution WHERE iscomplete=0 GROUP BY admuserid";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				AdmDsitribution a=new AdmDsitribution();
				a.setAdid(rs.getInt("adminid"));
				a.setCount(rs.getInt("COUNT"));
				list.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	@Override
	public List<String> getPureSalesAdmUser() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<String> list=new ArrayList<String>();
		String sql="select id from admuser where roleType=4 and status=1 and buyAuto=1";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				list.add(String.valueOf(rs.getInt("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	@Override
	public List<String> getPurePurchaseAdmUser() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<String> list=new ArrayList<String>();
		String sql="select id from admuser where roleType=5 and buyAuto=1 and status=1";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				list.add(String.valueOf(rs.getInt("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	@Override
	public List<String> getAllAdmUser() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<String> list=new ArrayList<String>();
		String sql="select id from admuser where roleType=3 and buyAuto=1 and status=1";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				list.add(String.valueOf(rs.getInt("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	/**
	 * 判断该订单用户是否被分配过采销员
	 * @param orderNo
	 * @return
	 */
	@Override
	public int getAdminId(String orderNo) {
		int adminid=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			String sql="SELECT a.adminid FROM admin_r_user a INNER JOIN orderinfo oi ON a.userid=oi.user_id WHERE oi.order_no=?";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1,orderNo);
			rs=stmt.executeQuery();
			if(rs.next()){
				adminid=rs.getInt("adminid");
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
		}
		return adminid;
	}

	/**
	 * 分配销售
	 * @param orderNo
	 * @param adminid
	 * @return
	 */
	@Override
	public int insertAdminUser(String orderNo, int adminid) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int row=0;
		String sql="";
		try{
			//该订单用户没有分配过销售
			sql="SELECT u.id as userId,u.email,(SELECT admName FROM admuser WHERE id=?) AS admName FROM USER u inner join orderinfo oi on u.id=oi.user_id WHERE oi.order_no=?";
			stmt=conn.prepareStatement(sql);
			stmt.setInt(1,adminid);
			stmt.setString(2,orderNo);
			rs=stmt.executeQuery();
			if(rs.next()){
				int userId=rs.getInt("userId");
				String email=rs.getString("email");
				String admName=rs.getString("admName");
				sql="delete from admin_r_user where userid='"+userId+"'";
				stmt=conn.prepareStatement(sql);
				row=stmt.executeUpdate();
				sql="insert into admin_r_user(userid,username,useremail,adminid,createdate,admName) values(?,?,?,?,now(),?)";
				stmt=conn.prepareStatement(sql);
				stmt.setInt(1,userId);
				stmt.setString(2,email);
				stmt.setString(3,email);
				stmt.setInt(4,adminid);
				stmt.setString(5,admName);
				row=stmt.executeUpdate();
				if(row>0){

					SendMQ.sendMsg(new RunSqlModel("delete from admin_r_user where userid='"+userId+"'"));
					SendMQ.sendMsg(new RunSqlModel("insert into admin_r_user(userid,username,useremail,adminid,createdate,admName) " +
							"values("+userId+",'"+email+"','"+email+"',"+adminid+",now(),'"+admName+"')"));

				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return row;
	}

	/**
	 * 获取最近7天采销员采购商品情况
	 * @return
	 */
	@Override
	public Map<Integer, Integer> getSevenLeastAdmuser() {
		Map<Integer, Integer> map=new HashMap<Integer,Integer>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			String sql="SELECT g.admuserid,COUNT(DISTINCT g.goods_pid) AS counts FROM goods_distribution g " +
					" INNER JOIN order_details od ON od.goods_pid=g.goods_pid inner join admuser ad on g.admuserid=ad.id" +
					" WHERE date_sub(curdate(), INTERVAL 7 DAY) <= date(g.createtime) and ad.buyAuto=1 and ad.status=1 and ad.roleType in (3,5) GROUP BY g.admuserid";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				int admuserid=rs.getInt("admuserid");
				int counts=rs.getInt("counts");
				if(map.get(admuserid) != null){
					counts+=map.get(admuserid);
				}
				map.put(admuserid,counts);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return map;
	}

	/**
	 * 获取该订单中商品以前的分配情况
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<Integer, Integer> getMoreGoodsPid(String orderNo) {
		Map<Integer, Integer> map=new HashMap<Integer,Integer>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			String sql="SELECT g.admuserid,COUNT(DISTINCT g.goods_pid) AS counts FROM goods_distribution g " +
					"  INNER JOIN order_details od ON od.goods_pid=g.goods_pid " +
					"  WHERE od.orderid=? and g.admuserid in (select id from admuser where buyAuto=1 and status=1 and roleType in (3,5)) GROUP BY g.admuserid";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1,orderNo);
			rs=stmt.executeQuery();
			while(rs.next()){
			    int admuserid=rs.getInt("admuserid");
//			    if(admuserid == 58){
//			        //sale1==>buy2
//                    admuserid=66;
//                }else if(admuserid == 51){
//                    //sale3==>buy5
//                    admuserid=68;
//                }else if(admuserid == 50){
//                    //sale4==>alisa
//                    admuserid=69;
//                }else if(admuserid == 53){
//                    //sale5==>mindy
//                    admuserid=70;
//                }
				map.put(admuserid,rs.getInt("counts"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return map;
	}

	@Override
	public List<OrderAutoDetail> getOrderDetail(String goodid, String order) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<OrderAutoDetail> list=new ArrayList<OrderAutoDetail>();
		String sql="select id as odid,goodsdata_id as goodsdataid from order_details where goodsid='"+goodid+"' and orderid='"+order+"'";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				OrderAutoDetail o=new OrderAutoDetail();
				o.setOdid(rs.getString("odid"));
				o.setGoodsdataid(rs.getString("goodsdataid"));
				list.add(o);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	@Override
	public int getMaxId() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int result=0;
        String sql="select max(orderid) as max from pure_autoplan";
        try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				if(String.valueOf(rs.getInt("max"))!=null){
					result=rs.getInt("max");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
        DBHelper.getInstance().closeConnection(conn);
		return result;
	}
	
	@Override
	public List<OrderProductSource> getPreAutoSourceAddTime() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="";
		try{
			sql="select ops.id,gd.admuserid from order_product_source ops inner join goods_distribution gd on ops.od_id=gd.odid where addtime is null order by ops.id desc";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				sql="update order_product_source set addtime=now(),adminid="+rs.getInt("admuserid")+" where id="+rs.getInt("id")+"";
				stmt=conn.prepareStatement(sql);
				stmt.executeUpdate();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
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
		}
        DBHelper.getInstance().closeConnection(conn);
		return null;
	}

	@Override
	/**
	 * 查询近4天的所有订单
	 */
	public List getAllOrderAuto(int maxId) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List list=new ArrayList<OrderAutoBean>();
		//String sql="SELECT distinct o.order_no AS orderno FROM orderinfo o   WHERE  o.state>0 AND o.state<6  AND o.orderpaytime> DATE_ADD(NOW(),INTERVAL -4 DAY) ORDER BY o.orderid ASC";
		String sql="SELECT DISTINCT o.order_no AS orderno FROM orderinfo o INNER JOIN paymentconfirm pf ON o.order_no=pf.orderno INNER JOIN admin_r_user a ON o.user_id=a.userid  WHERE o.state>0  AND o.state<6 AND a.adminid<>18  AND o.orderpaytime> DATE_ADD(NOW(),INTERVAL -30 DAY) ORDER BY o.orderid ASC";//
		//String sql="SELECT DISTINCT o.order_no AS orderno FROM orderinfo o INNER JOIN paymentconfirm pf ON o.order_no=pf.orderno  WHERE o.order_no='OA17880257592043'";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("orderno"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}
	
	@Override
	/**
     * 根据还未分配的订单号查询商品个数
     * @param orderids
     * @return 商品数量
     * @author 王宏杰
     * @ 2016-09-26
     */
	public int getAllCount(String orderids){
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int allCount=0;
		String sql="select count(id) as count from order_details where orderid in ("+orderids+") and goodsdata_id is not null";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				allCount=rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return allCount;
	}

	@Override
	/**
	 * 查询已分配过采购的订单
	 */
	public List getAllocatedOrder() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List list=new ArrayList();
//		String sql="SELECT DISTINCT o.order_no FROM ORDERINFO O INNER JOIN goods_distribution G ON o.order_no=G.orderid WHERE  o.orderpaytime> DATE_ADD(NOW(),INTERVAL -7 DAY) AND o.state>0 AND o.state<6";
		String sql=" SELECT DISTINCT o.order_no FROM orderinfo o "
				+ "INNER JOIN paymentconfirm pf ON o.order_no=pf.orderno "
				+ "INNER JOIN admin_r_user a ON o.user_id=a.userid "
				+ "LEFT JOIN goods_distribution g ON o.order_no=G.orderid "
				+ "WHERE o.state>0 AND o.state<6  AND o.orderpaytime> DATE_ADD(NOW(),INTERVAL -30 DAY) AND a.adminid<>18 AND g.id IS NOT NULL ORDER BY o.orderid ASC";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("order_no"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}
	
	@Override
	public int getNewColudBuyer(String pid) {
		int admuserid=0;
		Connection conn = DBHelper.getInstance().getConnection3();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="SELECT pb.buyer_id FROM product_buyer pb INNER JOIN business b ON pb.email=b.email INNER JOIN product p ON p.business_id=b.id WHERE p.product_id='"+pid+"' and pb.buyer_id in (SELECT id FROM crossshop.admuser WHERE roleType=3 AND buyAuto=1 AND STATUS=1)";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				admuserid=rs.getInt("buyer_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return admuserid;
	}
	
	@Override
	public int getExitGoodsPid(String goods_pid) {
		int admuserid=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="SELECT admuserid FROM goods_distribution WHERE goods_pid='"+goods_pid+"' and admuserid in (SELECT id FROM admuser WHERE roleType=3 AND buyAuto=1 AND STATUS=1) ORDER BY id DESC LIMIT 1";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				admuserid=rs.getInt("admuserid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return admuserid;
	}
	
	public Map<Integer,List<String>> getAllProcurementExpertise(){
		Map<Integer,List<String>> map=new HashMap<Integer,List<String>>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="select admuserid,specialty from procurement_expertise";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list=new ArrayList<String>();
				String specialtys=rs.getString("specialty");
				if(null!=specialtys && specialtys.indexOf(",")>-1){
					for(int i=0;i<specialtys.split(",").length;i++){
						list.add(specialtys.split(",")[i]);
					}
				}else if(null!=specialtys && specialtys.length()>0){
					list.add(specialtys);
				}
				map.put(rs.getInt("admuserid"), list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return map;
	}
	
	@Override
	public Map<String,List<OrderAutoBean>> getItems(String orderid) {
		Map<String,List<OrderAutoBean>> list=new HashMap<String,List<OrderAutoBean>>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		PreparedStatement stmt1 = null;
		ResultSet rs1=null;
		String sql="SELECT DISTINCT car_url FROM order_details WHERE orderid='"+orderid+"'";
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				String car_url=rs.getString("car_url");
				List<OrderAutoBean> beanList=new ArrayList<OrderAutoBean>();
				String sql1="select od.goodsid,od.id,od.car_url,od.goodsdata_id,od.goodscatid,od.userid from order_details od where od.orderid='"+orderid+"' and od.car_url='"+car_url+"'";
				stmt1 = conn.prepareStatement(sql1.toString());
				rs1 = stmt1.executeQuery();
				while(rs1.next()){
					OrderAutoBean oab=new OrderAutoBean();
					oab.setGoodsid(rs1.getString("goodsid"));
					oab.setOdid(rs1.getInt("id"));
					oab.setCarUrl(rs1.getString("car_url"));
					oab.setGoodsdataid(rs1.getInt("goodsdata_id"));
					oab.setGoodscatid(rs1.getString("goodscatid"));
					oab.setUserid(rs1.getInt("userid"));
					beanList.add(oab);
				}
				list.put(car_url, beanList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

@Override
public List<AdmDsitribution> getAdmuserType(String time) {
	List<AdmDsitribution> map=new ArrayList<AdmDsitribution>();
	Connection conn = DBHelper.getInstance().getConnection();
	PreparedStatement stmt = null;
	ResultSet rs=null;
	String sql="select admuserid as adid,count(distinct goods_url) as count from goods_distribution where createtime>'"+time+"' group by admuserid";
	try {
		stmt = conn.prepareStatement(sql.toString());
		rs = stmt.executeQuery();
		while(rs.next()){
			AdmDsitribution a=new AdmDsitribution();
			a.setAdid(rs.getInt("adid"));
			a.setCount(rs.getInt("count"));
			map.add(a);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}finally {
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
	}
	DBHelper.getInstance().closeConnection(conn);
	return map;
}

	@Override
	public String getFirdstCid(String id) {
		String cid="";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			String sql="SELECT func_get_split_string(path,',',1) as cid FROM 1688_category WHERE category_id='"+id+"'";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				cid=rs.getString("cid");
			}
		}catch(Exception e){
			cid="";
		}
		return cid;
	}

	@Override
	public AdmDsitribution getAdmuserNoComplete(String admuserid) {
		AdmDsitribution a=new AdmDsitribution();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			String sql="SELECT COUNT(gb.goods_url) as counts FROM	("
					+" SELECT gd.goods_url ,ops.goods_url AS goodsurl,MAX(gd.orderid) orderid,gd.createtime FROM("
					+" SELECT goods_distribution.goods_url,goods_distribution.odid,goods_distribution.orderid,goods_distribution.createtime FROM goods_distribution, order_details WHERE goods_distribution.admuserid = '"+admuserid+"' "
					+" AND goods_distribution.odid = order_details.id "
					+" AND order_details.state  < 2 "
					+" AND order_details.userid NOT IN (SELECT userid FROM	 admin_r_user WHERE	 admin_r_user.admName ='ling') "
					+" GROUP BY goods_distribution.goods_url,goods_distribution.odid,goods_distribution.orderid "
					+" ) gd "
					+" LEFT JOIN order_product_source ops ON gd.orderid = ops.orderid AND gd.odid = ops.od_id "
					+" WHERE gd.orderid NOT IN 	 (SELECT order_no AS  gs_orderid FROM	 orderinfo WHERE	 state IN ('-1','6')) "
					+" GROUP BY gd.goods_url "
					+" HAVING goodsurl IS NULL) gb";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				a.setAdid(Integer.valueOf(admuserid));
				a.setCount(rs.getInt("counts"));
			}else{
				a.setAdid(Integer.valueOf(admuserid));
				a.setCount(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return a;
	}
}
