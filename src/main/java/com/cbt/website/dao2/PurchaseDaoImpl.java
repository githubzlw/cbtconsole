package com.cbt.website.dao2;

import com.alibaba.fastjson.JSONArray;
import com.cbt.bean.CodeMaster;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDatailsNew;
import com.cbt.bean.OrderProductSource;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.Constant;
import com.cbt.jdbc.DBHelper;
import com.cbt.onlinesql.ctr.SaveSyncTable;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.TypeUtils;
import com.cbt.pojo.StraightHairPojo;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.util.AppConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.Util;
import com.cbt.util.Utility;
import com.cbt.warehouse.pojo.ChangeGoodsLogPojo;
import com.cbt.warehouse.pojo.PreferentialPrice;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.*;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.cbt.website.thread.PurchaseThred;
import com.google.common.collect.Lists;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

public class PurchaseDaoImpl implements PurchaseDao {
	IExpressTrackDao dao = new ExpressTrackDaoImpl();
	IOrderwsServer server = new OrderwsServer();
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PurchaseServerImpl.class);
	int total;
	int goodsnum = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public List<OrderProductSource> getAllGoodsids(int admuserid) {
		List<OrderProductSource> list = new ArrayList<OrderProductSource>();
//		String sql = "SELECT DISTINCT ops.goodsid FROM order_product_source ops INNER JOIN (SELECT t.itemid FROM taobao_1688_order_history t WHERE t.orderstatus='??????????????????'"
//				+ " AND TO_DAYS( NOW( ) ) - TO_DAYS(t.paydata)>3 AND t.itemurl<>'') tb  ON ops.tb_1688_itemid=tb.itemid AND ops.purchase_state='3' AND ops.confirm_userid='"
//				+ admuserid + "'";
		String sql="SELECT DISTINCT ops.od_id FROM order_product_source ops "
				+ " INNER JOIN orderinfo oi ON ops.orderid=oi.order_no"
				+ " INNER JOIN order_details od ON ops.orderid=od.orderid AND ops.goodsid=od.goodsid"
				+ " INNER JOIN taobao_1688_order_history t ON ops.tb_1688_itemid=t.itemid"
				+ " WHERE t.orderstatus='??????????????????' AND oi.state>0 AND oi.state<6 AND od.state<2"
				+ "  AND ops.goodsid<>'1400' AND ops.confirm_userid="+admuserid+" AND t.username=(SELECT account FROM tb_1688_accounts WHERE adminid="+admuserid+" AND del=1)  AND ops.addtime<t.creatTime AND ops.purchase_state=3";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderProductSource ops = new OrderProductSource();
				ops.setOdId(rs.getInt("od_id"));
				list.add(ops);
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
		return list;
	}
	@Override
	public String getAliPid(String goods_pid) {
		StringBuilder sb=new StringBuilder();
		String sql = "select goods_pid,shop_id from ali_info_data where 1688_pid='"+goods_pid+"' limit 1";
		Connection connection5 = DBHelper.getInstance().getConnection5();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = connection5.prepareStatement(sql);
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
			DBHelper.getInstance().closeConnection(connection5);
		}
		return sb.toString();
	}

	// ??????????????????????????????
	@Override
	public List<AdminUserBean> getAllAdmUser() {
		List<AdminUserBean> aubList = new ArrayList<AdminUserBean>();
		String sql = "select * from admuser where status =1 and roleType=2 order by admName asc";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				AdminUserBean admuser = new AdminUserBean();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("Email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoleType(rs.getInt("roleType"));
				admuser.setState(rs.getInt("status"));
				aubList.add(admuser);
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
		return aubList;
	}

	public int getAllOut() {
		int i = 0;
		String sql = "select count(oi.orderid) as sum from orderinfo oi,payment pm "
				+ "where pm.orderid=oi.order_no and oi.state ='2' and pm.paystatus=1";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				i = rs.getInt("sum");
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
		return i;
	}
	//???????????????????????????????????????
	@Override
	public int updateGoodsCommunicationInfo(String orderid, int admid) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try{
			String sql=" UPDATE goods_communication_info AS a "
					+ "INNER JOIN goods_distribution AS b ON a.orderid=b.orderid AND a.goodsid=b.goodsid SET a.is_read=1 "
					+ "WHERE b.orderid='"+orderid+"' AND b.admuserid="+admid+"";
			stmt=conn.prepareStatement(sql);
			stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
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


	// ????????????????????????
	@Override
	public List<PurchasesBean> getPurchaseByXXX(int startindex, int pagesize,
                                                int admin, int user, String orderno, int good, String date,
                                                int day, int stat, int unpaid, String orderid_no_array,
                                                String goodsid, String goodname, String orderarrs, String search_state) {
		PaymentDaoImp paymentDao = new PaymentDao();
		IExpressTrackDao dao1 = new ExpressTrackDaoImpl();
		IOrderwsServer server1 = new OrderwsServer();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rss = null, rs = null,rs2 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		java.sql.CallableStatement cst=null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		List<PurchasesBean> pbList = new ArrayList<PurchasesBean>();
		StringBuilder tborder=new StringBuilder();
		Calendar cale = Calendar.getInstance();
		cale.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cale.getTime());
		String time=yesterday + "23:59:59";

		String dayy = "";
		if (day != 111111111) {
			Date dat = new Date();
			dayy = this.beforNumDay(dat, -day);
		}else{
			dayy = day + "";
		}
		OrderBean orderInfo = server.getOrders(orderno);
		try {
			cst = conn.prepareCall("{call purchase_search_bygoods(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cst.setInt(1, startindex);
			cst.setInt(2, good==5201314 || good==5201315?500:pagesize);
			cst.setInt(3, admin);
			cst.setInt(4, user);
			cst.setString(5, orderno == null ? "" : orderno);
			cst.setInt(6,  good==5201314 || good==5201315?111111111:good);
			cst.setString(7, date == null ? "" : date);
			cst.setString(8, dayy == null ? "" : dayy);
			cst.setInt(9, Integer.valueOf(search_state)==0?11111111:Integer.valueOf(search_state));
			cst.setInt(10, unpaid);
			cst.setString(11, orderid_no_array == null ? "" : orderid_no_array);
			cst.setString(12, goodsid == null ? "" : goodsid);
			cst.setString(13, goodname == null ? "" : goodname);
			System.out.println("[1]:"+startindex+"\t [2]:"+( good==5201314 || good==5201315?500:pagesize)+"\t [3]:"+admin+"\t [4]:"+user+"\t [5]:"+orderno+"\t [6]:"+( good==5201314 || good==5201315?111111111:good)+"\t [7]:"+date+"\t [8]:"+dayy+"\t [9]:"
					+stat+"\t [10]:"+unpaid+"\t [11]:"+orderid_no_array+"\t [12]:"+goodsid+"\t [13]:"+goodname+"\t [14]:"+search_state);
			System.out.println("call purchase_search_bygoods("+startindex+","+( good==5201314 || good==5201315?500:pagesize)+","+admin+","+user+","
					+ "'"+(orderno == null ? "" : orderno)+"',"+( good==5201314 || good==5201315?111111111:good)+",'"+(date == null ? "" : date)+"','"+(dayy == null ? "" : dayy)+"'"
					+ ","+(Integer.valueOf(search_state)==0?11111111:Integer.valueOf(search_state))+","+unpaid+",'"+(orderid_no_array == null ? "" : orderid_no_array)+"'"
					+ ",'"+(goodsid == null ? "" : goodsid)+"','"+(goodname == null ? "" : goodname)+"')");
			boolean hadResults = cst.execute();
			String shop_id_1688 = "";
			if (hadResults) {
				rss = cst.getResultSet();
				while (rss != null && rss.next()) {
					if(good==5201314 && (StringUtils.isStrNull(rss.getString("rkgoodstatus").trim()) || "0".equals(rss.getString("rkgoodstatus").trim()) || "1".equals(rss.getString("rkgoodstatus").trim()))){
						continue;
					}
					if(good==5201315){
						String admName = dao1.queryBuyCount(rss.getInt("confirm_userid"));
						TaoBaoOrderInfo t = server1.getShipStatusInfo(rss.getString("tb_1688_itemid"), rss.getString("last_tb_1688_itemid"),
								rss.getString("confirm_time").substring(0, 10), admName,"",rss.getInt("offline_purchase"),rss.getString("order_no"),rss.getInt("goodsid"));
						if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {
							continue;
						}
					}
					String sql = "";
					String tb_remark = "";
					PurchasesBean purchaseBean = new PurchasesBean();
					int straight_flag=0;
					String address="";
					if(rss.getInt("straight_flag") == 0){
						sql="SELECT DISTINCT a.shop_id,a.1688_pid,a.address,s.level FROM ali_info_data a "
								+ "LEFT JOIN supplier_scoring s ON a.shop_id=s.shop_id WHERE a.1688_pid='"+rss.getString("goods_pid")+"'";
						stmt2 = conn.prepareStatement(sql);
						rs2=stmt2.executeQuery();
						String level="";
						String s_id="";
						try{
							s_id=rss.getString("shop_id");
						}catch(Exception e){
							s_id="99879887988778899";
						}
						if(rs2.next()){
							shop_id_1688 = rs2.getString("shop_id");
							address=rs2.getString("address");
							level=rs2.getString("level");
							if(StringUtils.isStrNull(address)){
								sql="select distinct a.address,s.level from ali_info_data a left join supplier_scoring s on a.shop_id=s.shop_id "
										+ "where a.shop_id='"+s_id+"' AND a.address IS NOT NULL AND a.address<>'' limit 1";
								stmt2 = conn.prepareStatement(sql);
								rs2=stmt2.executeQuery();
								if(rs2.next()){
									address=rs2.getString("address");
									level=rs2.getString("level");
								}
							}
							if(!StringUtils.isStrNull(address) && !StringUtils.isStrNull(level) && address.contains("??????") && "???????????????".equals(level)){
								straight_flag=1;
							}
						}else{
							sql="select distinct a.address,s.level from ali_info_data a left join supplier_scoring s on a.shop_id=s.shop_id "
									+ "where a.shop_id='"+s_id+"' ORDER BY address DESC limit 1";
							stmt2 = conn.prepareStatement(sql);
							rs2=stmt2.executeQuery();
							if(rs2.next()){
								address=rs2.getString("address");
								level=rs2.getString("level");
							}
							if(!StringUtils.isStrNull(address) && !StringUtils.isStrNull(level) && address.contains("??????") && "???????????????".equals(level)){
								straight_flag=1;
							}
						}
					}else{
						straight_flag=rss.getInt("straight_flag");
						address="??????";
					}
					purchaseBean.setCbrWeight(rss.getString("cbrWeight"));
					purchaseBean.setCarWeight(rss.getString("od_total_weight"));
					purchaseBean.setStraight_flag(straight_flag);
					purchaseBean.setStraight_address(StringUtils.isStrNull(address)?"":address);
					purchaseBean.setStraight_time(StringUtils.isStrNull(rss.getString("straight_time"))?"???":rss.getString("straight_time"));
					sql="update order_details set straight_flag="+straight_flag+" where orderid='"+rss.getString("order_no")+"' and goodsid='"+rss.getString("goodsid")+"'";
					stmt = conn.prepareStatement(sql);
					stmt.executeUpdate();
					String source_shop_id="";
					if(rss.getInt("sampling_flag")==1){
						//?????????????????????????????????
						sql="select shop_id from samplinginfomation where od_id";
						stmt=conn.prepareStatement(sql);
						rs=stmt.executeQuery();
						if(rs.next() && !StringUtils.isStrNull(rs.getString("shop_id"))){
							source_shop_id=rs.getString("shop_id");
						}
					}
					purchaseBean.setSource_shop_id(source_shop_id);
					String fileByOrderid = paymentDao.getFileByOrderid(rss.getString("order_no"));
					if (fileByOrderid == null || fileByOrderid.length() < 10) {
						purchaseBean.setInvoice(0);
					}else if (fileByOrderid.indexOf(".pdf") > -1){
						purchaseBean.setInvoice(1);
					}else {
						purchaseBean.setInvoice(2);
					}
					//?????????????????????1688?????????
					String shipnos=rss.getString("shipnos");
					if(!"0".equals(shipnos) && !"undefined".equals(shipnos)){
						sql="select distinct orderid,totalprice from taobao_1688_order_history where shipno in ("+shipnos+")";
						stmt = conn.prepareStatement(sql);
						rs=stmt.executeQuery();
						while(rs.next()){
							if(!tborder.toString().contains(rs.getString("orderid"))){
								tborder.append(rs.getString("orderid")).append(":???").append(rs.getString("totalprice")).append(";");
							}
						}
					}
					purchaseBean.setTborderInfo(tborder.toString());
					if(rss.getInt("purchase_state")==3 && rss.getString("confirm_time").compareTo(time)<0){
						String admName = dao.queryBuyCount(Integer.valueOf(rss.getString("confirm_userid")));
						TaoBaoOrderInfo t = server.getShipStatusInfo(rss.getString("tb_1688_itemid"), rss.getString("last_tb_1688_itemid"),
								rss.getString("confirm_time"), admName,"",rss.getInt("offline_purchase"),rss.getString("order_no"),rss.getInt("goodsid"));
						if(t==null){
							//1.???????????????????????????1688??????
							purchaseBean.setShipstatus("?????????????????????????????????????????????");
						}else if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {
							String shipstatus = t.getShipstatus().split("\n")["2".equals(t.getTbOr1688())?0:t.getShipstatus().split("\n").length - 1];
							if("2".equals(t.getTbOr1688()) && !"????????????????????????".equals(shipstatus)){
								String msg=t.getShipstatus().split("\n")[1];
								purchaseBean.setShipstatus(shipstatus+"\n "+msg);
							}else{
								purchaseBean.setShipstatus(shipstatus);
							}
						}else if(t!=null && !StringUtils.isStrNull(t.getShipno()) && StringUtils.isStrNull(t.getShipstatus())){
							//?????????????????? ??????1688????????????????????????
							purchaseBean.setShipstatus("????????????????????????????????????");
						}else{
							purchaseBean.setShipstatus("?????????????????????????????????????????????");
						}
						if(t!=null &&  !StringUtils.isStrNull(t.getSupport_info()) && t.getSupport_info().length()>5 && t.getSupport_info().indexOf(";")>-1){
							String imgs[]=t.getSupport_info().split(";");
							StringBuilder b=new StringBuilder();
							for (String s : imgs) {
								if(!StringUtils.isStrNull(s) && s.indexOf("https")>=0){
									String s1=s.indexOf("&")>=0?s.split("&")[0]:s;
									String msg=s.indexOf("&")>=0?s.split("&")[1]:"";
									b.append("<img src='"+s1+"' title='"+msg+"'/>");
								}
							}
							purchaseBean.setSupport_info(b.toString());
						}
					}else{
						purchaseBean.setShipstatus("");
					}
					purchaseBean.setUserid(rss.getInt("userid"));
					purchaseBean.setShop_ids(StringUtils.isStrNull(rss.getString("shop_id")) || "null".equals(rss.getString("shop_id"))?"":rss.getString("shop_id"));
					purchaseBean.setBuyid(rss.getString("buyid"));
					purchaseBean.setLock_remaining(rss.getString("lock_remaining"));
					purchaseBean.setRemaining(rss.getString("remaining"));
					purchaseBean.setIn_id(rss.getString("in_id"));
					purchaseBean.setTb_1688_itemid(rss.getString("tb_1688_itemid"));
					purchaseBean.setNew_remaining(rss.getString("new_remaining"));
					purchaseBean.setOrderNo(rss.getString("order_no"));
					purchaseBean.setOrderid(rss.getInt("orderid"));
					purchaseBean.setGoodssourcetype(StringUtils.isStrNull(rss.getString("goodssourcetype"))?"":rss.getString("goodssourcetype").replace("'", ""));
					purchaseBean.setOrdertime((rss.getString("create_time")).substring(0, 10));
					purchaseBean.setOrderremarkNew(rss.getString("orderremark"));
					purchaseBean.setConfirm_userid(rss.getInt("confirm_userid"));
					purchaseBean.setIs_replenishment(rss.getInt("is_replenishment"));
					purchaseBean.setOrderProblem(rss.getString("problem"));
					purchaseBean.setGoods_info(rss.getString("goods_info"));
					purchaseBean.setRefund_flag(rss.getInt("refund_flag"));
					String mode_transport = "", od_country = "";
					if (rss.getString("mode_transport") != null && !rss.getString("mode_transport").equals("")) {
						mode_transport = "?????????";
						String[] mode_transport_ = rss.getString("mode_transport").split("@");
						for (int j = 0; j < mode_transport_.length; j++) {
							if (j == mode_transport_.length - 2 && Utility.getIsDouble(mode_transport_[mode_transport_.length - 2])) {
								if (Double.parseDouble(mode_transport_[mode_transport_.length - 2]) == 0) {
									mode_transport_[mode_transport_.length - 1] = "??????";
									mode_transport = "??????";
								}
							}
							if (j == mode_transport_.length - 3) {
								od_country = mode_transport_[mode_transport_.length - 3];
							}
						}
					}
					purchaseBean.setMode_transport(mode_transport);
					purchaseBean.setOrderaddress(od_country);
					Date datee = null;
					try {
						if (rss.getString("paytime") == null || rss.getString("paytime").trim().length() < 1) {
							purchaseBean.setPaytime(rss.getString("paytime"));
							datee = sdf.parse(rss.getString("create_time"));
						}else {
							purchaseBean.setPaytime((rss.getString("paytime")).substring(0, 10));// ????????????
							datee = sdf.parse(rss.getString("paytime"));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					purchaseBean.setDeliveryTime(this.beforNumDay(datee,rss.getInt("delivery_time")));// ????????????
					purchaseBean.setDetails_number(rss.getInt("details_number")- rss.getInt("purchase_number"));// ??????????????????
					purchaseBean.setPurchase_number(rss.getInt("purchase_number"));// ???????????????
					purchaseBean.setGoodsid(rss.getInt("goodsid"));
					purchaseBean.setOd_id(rss.getInt("od_id"));
					purchaseBean.setGoodsdata_id(rss.getInt("goodsdata_id"));
					purchaseBean.setRemarkpurchase(rss.getString("odremark"));
					purchaseBean.setInventory_remark(rss.getString("inventory_remark"));
					String str = rss.getString("odremark");
					String str2 = "";
					int i = 0;
					if (str != null && str.length() > 30) {
						for (; i < str.length() / 30; i++) {
							str2 += str.substring(i * 30, (i + 1) * 30)+ "</br>";
						}
						if (i > 0) {
							str2 += str.substring(i * 30, str.length());
						}
					} else {
						str2 = str;
					}
					purchaseBean.setRemarkpurchase(str2);
					String goods_img = rss.getString("googs_img").replace("60x60", "400x400").replace("32x32", "400x400").replace("50x50", "200x200");
					if (goods_img.contains(".jpg")) {
						purchaseBean.setGoogs_img((goods_img.substring(0,goods_img.lastIndexOf(".jpg") + 4)));
					} else if (goods_img.contains(".png")) {
						purchaseBean.setGoogs_img(goods_img.substring(0, (goods_img.lastIndexOf(".png") + 4)));
					} else {
						purchaseBean.setGoogs_img(goods_img);
					}
					String goodtype = "<br/>";
					String cGoodstype = null;
					if (rss.getString("goods_type") != null) {
						String types = rss.getString("goods_type");
						String types1 = "";
						if (types.indexOf("<") > -1 && types.indexOf(">") > -1) {
							for (int j = 1; j < 4 && types.indexOf("<") > -1 && types.indexOf(">") > -1; j++) {
								types1 = types.substring(types.indexOf("<"),types.indexOf(">") + 1);
								types = types.replace(types1, "");
							}
						}
						cGoodstype = types;// ??????????????? 2/8
						if ((types).contains("@")) {
							String[] gdtp = (types).split(",");
							for (String ty : gdtp) {
								if (!(ty == null || "".equals(ty.trim())) && ty.indexOf("@") > 0) {
									goodtype = goodtype+ ty.substring(0, (ty.indexOf("@")))+ ";<br/>";
								}
							}
						} else if ((types).contains(",")) {
							goodtype = types.replace(",", ";<br/>");
						} else {
							goodtype = types;
						}
					}
					purchaseBean.setcGoodstype(StringUtils.isStrNull(cGoodstype)?"":cGoodstype.replace("'", ""));
					purchaseBean.setGoods_type(goodtype);
					purchaseBean.setGoods_title(rss.getString("goods_title").replace("'", ""));
					DecimalFormat df = new DecimalFormat("########0.00");
					double fright_=Double.valueOf(StringUtils.isStrNull(rss.getString("goodsprice"))?"0.00":rss.getString("goodsprice"));//-rss.getDouble("freight");
					purchaseBean.setGoods_price(String.valueOf(df.format(fright_)));
					purchaseBean.setCurrency(rss.getString("currency"));
					purchaseBean.setGoogs_number(rss.getInt("yourorder"));
					String fileimgname = rss.getString("fileimgname");
					if (!"".equals(fileimgname) && fileimgname != null) {
						String t = fileimgname.split(",")[0];
						purchaseBean.setFileimgname(AppConfig.product.substring(0,AppConfig.product.indexOf(":", 12))+ ":90" + t);
					}
					if ("".equals(purchaseBean.getFileimgname()) || purchaseBean.getFileimgname() == null) {
						purchaseBean.setFileimgname(purchaseBean.getGoogs_img());
					}
					String mk = "";
					StringBuffer sbf = new StringBuffer();
					if (rss.getString("bargainRemark") != null && !(rss.getString("bargainRemark")).equals("")) {
						mk = "????????????:" + rss.getString("bargainRemark") + " ;";
						sbf.append(mk);
					}
					if (rss.getString("deliveryRemark") != null && !(rss.getString("deliveryRemark")).equals("")) {
						mk = "????????????:" + rss.getString("deliveryRemark") + " ;";
						sbf.append(mk);
					}
					if (rss.getString("colorReplaceRemark") != null && !(rss.getString("colorReplaceRemark")).equals("")) {
						mk = "????????????:" + rss.getString("colorReplaceRemark")+ " ;";
						sbf.append(mk);
					}
					if (rss.getString("sizeReplaceRemark") != null && !(rss.getString("sizeReplaceRemark")).equals("")) {
						mk = "????????????:" + rss.getString("sizeReplaceRemark")+ " ;";
						sbf.append(mk);
					}
					if (rss.getString("orderNumRemarks") != null && !(rss.getString("orderNumRemarks")).equals("")) {
						mk = "????????????:" + rss.getString("orderNumRemarks") + " ;";
						sbf.append(mk);
					}
					if (rss.getString("questionsRemarks") != null && !(rss.getString("questionsRemarks")).equals("")) {
						mk = "????????????:" + rss.getString("questionsRemarks") + " ;";
						sbf.append(mk);
					}
					if (rss.getString("unquestionsRemarks") != null && !(rss.getString("unquestionsRemarks")).equals("")) {
						mk = "???????????????:" + rss.getString("unquestionsRemarks")+ " ;";
						sbf.append(mk);
					}
					if (rss.getString("againRemarks") != null && !(rss.getString("againRemarks")).equals("")) {
						mk = "????????????:" + rss.getString("againRemarks") + " ;";
						sbf.append(mk);
					}
					if(rss.getInt("lock_remaining1")>0){
						mk = "???????????????"+rss.getInt("lock_remaining1")+"??????";
						sbf.append(mk);
					}
					purchaseBean.setRemark(sbf.toString() + tb_remark);// ??????????????????
					String issure = "", purtime = "", pursure = "";
					String pt = rss.getString("confirm_time");
					int product_state = rss.getInt("product_state");// 1:??????????????????
					if(rss.getInt("purchase_state")==100){
						purchaseBean.setPurchase_state(0);
					}else{
						purchaseBean.setPurchase_state(rss.getInt("purchase_state"));
					}
					if (pt == null || pt.equals("")) {
						purtime = "";
					} else {
						purtime = pt.substring(0, 19);
					}
					String times=StringUtils.isStrNull(rss.getString("purchasetime"))?"":rss.getString("purchasetime");
					if(rss.getInt("purchase_state")==3){
						times=StringUtils.isStrNull(rss.getString("purchasetime"))?rss.getString("confirm_time"):rss.getString("purchasetime");
					}
					purchaseBean.setPurchasetime(times);
					String goods_pid=rss.getString("goods_pid");
					String ali_pid="";
					String shop_id="";
					String shop_ids="";
					String car_urlMD5 = rss.getString("car_urlMD5");
					List<String> shop_id_b=new ArrayList<String>();
					if(rss.getInt("isDropshipOrder")==3){
//						String str_[]=getAliPid(goods_pid).split("&");
						ali_pid="";
						shop_id="";
					}else{
						shop_id=rss.getString("shop_id");
						if(StringUtils.isStrNull(shop_id) || "-".equals(shop_id)){
							shop_id="0000";
						}else if(!StringUtils.isStrNull(shop_id) && !"null".equals(shop_id) && shop_id.contains("\\//")){
							shop_id=shop_id.split("\\//")[1].split("\\.")[0];
						}
						if(rss.getInt("is_replenishment")==1){
							shop_id_b=getBhShopId(rss.getString("order_no"),rss.getInt("goodsid"));

						}
					}
					purchaseBean.setBh_shop_id(shop_id_b);
					purchaseBean.setAli_pid(ali_pid);
					if(org.apache.commons.lang.StringUtils.isBlank(shop_id_1688)){
						shop_id_1688=shop_id;
					}
					purchaseBean.setShop_id(shop_id_1688);
					//?????????????????????
					String level = supplierScoringLevel(shop_id);
					purchaseBean.setLevel(level);
					purchaseBean.setGoods_pid(goods_pid);
					purchaseBean.setCar_urlMD5(car_urlMD5);
					String new_car_url="";
					String old_url = rss.getString("old_url").trim();
					if(!StringUtils.isStrNull(rss.getString("car_url"))){
						new_car_url=rss.getString("car_url");
					}else if(!StringUtils.isStrNull(old_url)){
						new_car_url=old_url;
					}else if(StringUtils.isStrNull(rss.getString("car_url")) && !StringUtils.isStrNull(car_urlMD5)){
						new_car_url="https://www.import-express.com/product/detail?&source="+car_urlMD5+"&item="+goods_pid;
					}
					new_car_url=StringUtil.getNewUrl(new_car_url,goods_pid,car_urlMD5);
					purchaseBean.setImportExUrl(new_car_url);
					if(car_urlMD5 == null || "".equals(car_urlMD5)){
						purchaseBean.setGoods_url("https://detail.1688.com/offer/0.html");
					}else{
						if(car_urlMD5.substring(0, 1).equals("D")){
							//??????????????????
							//purchaseBean.setImportExUrl("https://www.import-express.com/spider/detail?&source=D" + Md5Util.encoder(goods_pid) + "&item=" + goods_pid);
							purchaseBean.setGoods_url("https://detail.1688.com/offer/"+goods_pid+".html");
						}else if(car_urlMD5.substring(0, 1).equals("M")){
							purchaseBean.setGoods_url("https://www.amazon.com/"+(rss.getString("goods_title")==null?"a":rss.getString("goods_title").replace("'", ""))+"/dp/"+goods_pid);
						}else if(car_urlMD5.substring(0, 1).equals("A")){
							purchaseBean.setGoods_url("http://www.aliexpress.com/item/a/"+goods_pid+".html");
						}else{
							purchaseBean.setGoods_url(rss.getString("car_url"));
						}
//						purchaseBean.setImportExUrl(rss.getString("car_url"));
					}
					String goods_p_url=rss.getString("newValue")==null?"":rss.getString("newValue");//?????????????????????
					if("".equals(goods_p_url)){
						goods_p_url=purchaseBean.getGoods_url();
					}
					purchaseBean.setNewValue(goods_p_url.replace("'", " "));
					purchaseBean.setLastValue(rss.getString("lastValue"));
					purchaseBean.setOldValue(rss.getString("oldValue"));
					String nm = null;
					try {
						nm = rss.getString("inventory");
						int s = nm.length();
						if (s > 10) {
							nm = "0";
							purchaseBean.setInventory(nm);// ??????????????? 2017.2/4
						} else {
							purchaseBean.setInventory(nm);// ??????????????? 2017.2/4
						}

					} catch (Exception e) {
						nm = "0";
						purchaseBean.setInventory(nm);
					}
					String sog = null;
					try {
						sog = rss.getString("source_of_goods");
						if (sog.length() > 10) {
							sog = "0";
							purchaseBean.setSource_of_goods(Integer.valueOf(sog));// ?????????????????????// ???1??????????????????0????????????
						} else {
							purchaseBean.setSource_of_goods(Integer.valueOf(sog));// ?????????????????????// ???1??????????????????0????????????
						}
					} catch (Exception e2) {
						sog = "0";
						purchaseBean.setSource_of_goods(Integer.valueOf(sog));
					}
					String cn = null;
					cn = rss.getString("companyname");
					if (!" ".equals(cn) && cn != null) {
						purchaseBean.setCompanyName(cn);
					} else {
						cn = "???";
						purchaseBean.setCompanyName(cn);
					}
					try {
						String seilUnit_ = rss.getString("seilUnit");
						seilUnit_ = seilUnit_.trim();
						String goodsUnit_ = rss.getString("goodsUnit");
						goodsUnit_ = goodsUnit_.trim();
						if (seilUnit_.indexOf("(") > -1) {
							seilUnit_ = seilUnit_.substring(seilUnit_.indexOf("("));
						}
						if (seilUnit_ == goodsUnit_) {
							seilUnit_ = null;
						}
						purchaseBean.setSeilUnit(seilUnit_);
						purchaseBean.setGoodsUnit(goodsUnit_);
					} catch (Exception e) {
						LOG.info("????????????????????????????????????" + rss.getString("order_no")+ "," + rss.getInt("goodsid"));
					}
					String admin1 = rss.getString("admin");
					if (!"  ".equals(admin1) && admin1 != null && admin1.length() < 3) {
						PurchaseServer purchaseServer = new PurchaseServerImpl();
						String aduser = purchaseServer.getUserbyID(admin1);
						purchaseBean.setAdmin(aduser);
					}
					String isDropshipOrder1 = rss.getString("isDropshipOrder");
					if (!"  ".equals(isDropshipOrder1) && isDropshipOrder1 != null && admin1.length() > 0) {
						purchaseBean.setIsDropshipOrder(isDropshipOrder1);
					}
					String child_order_no1 = rss.getString("dropshipid");// ??????dropship????????????
					if (!"  ".equals(child_order_no1) && child_order_no1 != null && child_order_no1.length() > 0) {
						purchaseBean.setChild_order_no(child_order_no1);
					}
					//??????????????????
					String goods_price = purchaseBean.getGoods_price();
					//????????????(????????????)
					String car_weight=rss.getString("car_weight");
					car_weight=StringUtil.isBlank(car_weight)?"0":car_weight;
					//????????????????????????
					double freightFee = 0.00;//PurchaseController.getFreightFee(car_weight, orderInfo);
					//??????????????????????????????
					double es_price=getEsPrice(rss.getString("goodsid"));
					String oldValue = purchaseBean.getOldValue();
					// ???????????????????????????RMB????????????????????????%??????=??????????????????????????????-??????????????????????????????-????????????????????????
//					if ((!"".equals(goods_price) && goods_price != null) && (!"".equals(oldValue) && oldValue != null)) {
//						double td = Double.parseDouble(goods_price) * 6.89- Double.parseDouble(oldValue);
//						td = td * 100/ (Double.parseDouble(goods_price) * 6.89);
//						purchaseBean.setProfit((int) td);
//					} else {
//						purchaseBean.setProfit(0);
//					}
					double p=Double.valueOf(goods_price)*rss.getDouble("exchange_rate")-es_price-freightFee;
					String fit=df.format(p/(Double.valueOf(goods_price)*rss.getDouble("exchange_rate")));
					purchaseBean.setProfit(df.format(p)+"(<span style=color:"+(Double.parseDouble(fit)<=0?"red":"green")+">"+fit+"</span>)");
					purchaseBean.setCginfo(rss.getString("cginfo"));
					purchaseBean.setOd_state(rss.getInt("od_state"));
					String ShoworiginalUrl = "";
					int od_state = rss.getInt("od_state");
					int purchase_state = rss.getInt("purchase_state");
					if (rss.getString("newValue") == null || rss.getString("newValue").equals("")) {
						if (purchase_state == 12) {
							issure = "??????????????????";
						}
					} else {
						if (product_state == 1) { // ????????????
							issure = "????????????";
							if (purchase_state == 12) {
								issure = "??????????????????";
							} else if (purchase_state == 13 || od_state == 13) {
								issure = "?????????????????????";
							} else if (purchase_state == 14) {
								issure = "???????????????";
							}
						} else if (product_state == 0) { // ??????
							if (purchase_state == 1) {
								issure = "???????????????";
							} else if (purchase_state == 3) {
								issure = "???????????????"; // ps ==3 ???????????????
							} else if (purchase_state == 3) {
								issure = "???????????????"; // ps ==4 ???????????????
							} else if (purchase_state == 12) {
								issure = "??????????????????";
							} else if (purchase_state == 13 || od_state == 13) {
								issure = "?????????????????????";
							} else if (purchase_state == 14) {
								issure = "???????????????";
							}
						}
					}
					int bc = rss.getInt("buycount");
					if (bc == 0) {
						purchaseBean.setPurchaseCount(rss.getInt("yourorder"));
					} else {
						purchaseBean.setPurchaseCount(rss.getInt("buycount"));
					}
					purchaseBean.setIssure(issure);
					purchaseBean.setOriginalGoodsUrl(ShoworiginalUrl);
					purchaseBean.setPuechaseTime(purtime);
					String querneGoods = ""; // ????????????botton
					String remarkAgainBtn = "";// // ??????????????????
					String oistate=rss.getString("oistate");
					purchaseBean.setOistate(oistate);
					String odstate=rss.getString("odstate");
					purchaseBean.setOdstate(odstate);
					if("6".equals(oistate) || "-1".equals(oistate) || "2".equals(odstate)){
						//???????????????
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid")+ ")' disabled='disabled' />";
					}else if (purchase_state == 0) {
						//?????????????????????
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f'/>";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid") + ")' />";
					}else if (purchase_state == 1) {
						// ??????????????????
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f'/>";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f'/>";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid") + ")'/>";
					}else if (purchase_state == 3) {
						// ?????????????????????
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' />";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid") + ")'/>";
					}else if(purchase_state == 4 || purchase_state == 6 || purchase_state == 8){
						//?????????
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid")+ ")' disabled='disabled' />";
					}else if (purchase_state == 5) {
						// ???????????????
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f'";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'//>";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid") + ")' />";
					} else if (purchase_state == 12) {
						// ??????????????????
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid")+ ")' disabled='disabled' />";
					} else if (purchase_state == 13) {
						// ?????????????????? || od_state==13
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f'";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' />";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid") + ")'/>";
					} else if (purchase_state == 100) {
						//?????????????????????
						querneGoods = "<input type='button' id='hyqr"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????'  class='f'";
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' disabled='disabled' class='f' disabled='disabled'/>";
						remarkAgainBtn = "<input class='remarkBtn' type='button' value='????????????' onclick='remarkAgain(\""+ rss.getString("order_no")+ "\","+ rss.getInt("od_id")+ ","+ rss.getInt("goodsid") + ")'";
						if(rss.getString("inventory_remark")!=null && rss.getString("inventory_remark").indexOf("????????????")>-1){
							querneGoods+=" disabled='disabled'";
							remarkAgainBtn+=" disabled='disabled'";
						}
						querneGoods+="/>";
						remarkAgainBtn+="/>";
					}
					// 13 ??????????????????
					if (od_state == 13) {
						purchaseBean.setNewValue(rss.getString("car_url"));
						purchaseBean.setIssure("?????????????????????");
					}
					purchaseBean.setQuerneGoods(querneGoods);
					purchaseBean.setRemarkAgainBtn(remarkAgainBtn);
					String strProductValue = "";
					strProductValue = rss.getString("productchange");
					if (strProductValue.equals("1")) { // ???????????????
						purchaseBean.setProductState("?????????????????????????????????");
						pursure = "<input type='button' id='"+ rss.getString("order_no")+ rss.getInt("goodsid")+ "' value='????????????' class='f' disabled='disabled'/>";
					}
					purchaseBean.setPurchaseSure(pursure);
					String adtime = rss.getString("addtime");
					String addtime = "";
					if (adtime == "" || adtime == null) {
						addtime = "";
					} else {
						addtime = adtime.substring(0, 19);
					}
					purchaseBean.setAddtime(addtime);
					purchaseBean.setRukuTime(rss.getString("ruku_time"));
					purchaseBean.setTb_orderid(rss.getString("tb_orderid"));
					purchaseBean.setPosition(rss.getString("position"));
					purchaseBean.setRkgoodstatus(rss.getString("rkgoodstatus"));
					String img_type = rss.getString("img_type").replace("60x60", "400x400").replace("32x32", "400x400").replace("50x50", "200x200");
					purchaseBean.setImg_type(img_type);
					purchaseBean.setYiruku("");
					if (rss.getString("aruadminid") == null || (rss.getString("aruadminid").trim()).equals("")) {
						purchaseBean.setSaler("????????????");// ???????????????
					} else {
						purchaseBean.setSaler(rss.getString("aruadminid"));// ???????????????
					}




					String order_remark = rss.getString("adminRemark");
					String od_remark = "";
					if (order_remark == null || order_remark.equals("")) {
						od_remark = "";
					} else {
						od_remark = (order_remark.replace(":", "")).replace(";", "");
					}
					if (od_remark == null || od_remark.equals("")) {
						purchaseBean.setOrderremark_btn("");
						purchaseBean.setOrderremark("");
					} else {
						purchaseBean.setOrderremark(order_remark);
						if (order_remark.length() > 175) {
							purchaseBean.setOrderremark_btn("<a id=\"remark_"+ rss.getString("order_no")+ "\" class=\"order_remark\" onclick=\"fnShow_od_remark(this.id);\">????????????>></a>");
						}
					}
					if (purchaseBean.getImg_type() != null) {
						String temp = purchaseBean.getImg_type();
						if (temp.indexOf(".jpg") != -1) {
							temp = temp.substring(0, temp.indexOf(".jpg") + 4);
						} else if (temp.indexOf(".png") != -1) {
							temp = temp.substring(0, temp.indexOf(".png") + 4);
						}
						purchaseBean.setImg_type(temp);
					}
					purchaseBean.setOffline_purchase(rss.getInt("offline_purchase"));
					pbList.add(purchaseBean);
				}
			}
			// ?????????????????????????????????
			if (cst.getMoreResults()) {// ?????????????????????????????????????????????
				rs = cst.getResultSet();
				while (rs.next()) {
					total = Integer.parseInt(rs.getString("fr"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
			try{
				if (rss != null) {
					rss.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
				if (cst != null) {
					cst.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return pbList;
	}

	// ????????????
	public String beforNumDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, day);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	public static List<String> getBhShopId(String orderid,int goodsid){
		List<String> shop_ids=new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			String sql="SELECT distinct shop_id FROM order_replenishment WHERE orderid=? AND goodsid=?";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setInt(2, goodsid);
			rs=stmt.executeQuery();
			while(rs.next()){
				String shop_id=rs.getString("shop_id");
				shop_id=shop_id.split("\\//")[1].split("\\.")[0];
				shop_ids.add(shop_id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return shop_ids;
	}



	// ????????????
	@Override
	public void AddRecource(String type, int admid, int userid,int goodsdataid, String goods_url, String googs_img,double goodsprice, String goods_title, int googsnumber,
	                        String orderNo, int od_id, int goodid, double price,String resource, int buycount, String reason, String currency,String pname, String cGoodstypee,
	                        String issuree,String shop_id,String state_flag,String straight_address) {
		String companyName = null;
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null, rsa = null, rsb = null;
		int ii = 0, iia = 0, iib = 0;
		PreparedStatement stmt = null, stmta = null, stmtb = null, stmtn = null;
		PreparedStatement stmtt = null, stmttt = null, stmtttt = null;
		PreparedStatement stmtupdate = null;
		PreparedStatement stmc = null, stmc2 = null, stms = null, stms2 = null;
		String sql = "select count(id) as sum from order_change where orderNo=? and goodId=? and ropType=6 and del_state=0";
		String sqla = "select count(id) as suma from order_product_source where goods_url=? and orderid=? and od_id=?";
		String sqlb = "select count(id) as sumb from goods_source where goods_url=? and goods_purl=? ";
		String sqln = "UPDATE sourceofgoods SET source_of_goods = 0 WHERE goods_url=? and	goods_type=?";
		if (resource != null && !"".equals(resource)) {
			try {
				resource = TypeUtils.modefindUrl(resource, 1);
			} catch (Exception e) {
				LOG.info("??????????????????????????????:" + e);
			}
		}
//		if(!StringUtils.isStrNull(shop_id)){
//			shop_id=shop_id.split("\\//")[1].split("\\.")[0];
//		}else{
//			shop_id="";
//		}
		String itemid="0000";
		if(resource!=null && !"".equals(resource)){
			itemid = Util.getItemid(resource);
		}

		if (resource.contains("1688.com")) {
			resource = resource.substring(0, resource.indexOf(".html") + 5);
		} else if (resource.contains("taobao")) {
			System.out.println("url==" + resource);
			String x = resource.split("\\?")[0];
			String y[] = resource.split("\\?")[1].split("&");
			for (int i = 0; i < y.length; i++) {
				if (y[i].contains("id")) {
					resource = x + "?" + y[i];
				}
			}
		}
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ii = rs.getInt("sum");
			}

			stmta = conn.prepareStatement(sqla);
			stmta.setString(1, goods_url);
			stmta.setString(2, orderNo);
			stmta.setInt(3, od_id);
			rsa = stmta.executeQuery();
			if (rsa.next()) {
				iia = rsa.getInt("suma");
			}
			stmtb = conn.prepareStatement(sqlb);
			stmtb.setString(1, goods_url);
			stmtb.setString(2, resource);
			rsb = stmtb.executeQuery();
			if (rsb.next()) {
				iib = rsb.getInt("sumb");
			}
			if (ii != 0) {
				String sqlupdate = "update order_change set del_state=1 where order_change.orderNo=? and order_change.goodId=? and ropType=6 and del_state=0";
				stmtupdate = conn.prepareStatement(sqlupdate);
				stmtupdate.setString(1, orderNo);
				stmtupdate.setInt(2, goodid);
				stmtupdate.executeUpdate();
			}
			stmtn = conn.prepareStatement(sqln);
			stmtn.setString(1, goods_url);
			stmtn.setString(2, cGoodstypee);
			stmtn.executeUpdate();
			String tempSql = "delete from order_change where orderNo='"+ orderNo + "' and goodId='" + goodid + "'  and ropType=6";
			String sqlll = "";
			String sqlc = "";
			String sqls = "";
			String bargainRemark = "";
			String deliveryRemark = "";
			String colorReplaceRemark = "";
			String sizeReplaceRemark = "";
			String orderNumRemarks = "";
			String questionsRemarks = "";
			String unquestionsRemarks = "";
			// ????????????:
			StringBuffer questionBz = new StringBuffer();
			if (StrUtils.isNotNullEmpty(reason)) {
				String[] reasons = reason.split("//,");
				for (String str : reasons) {
					String bz = str.split(":")[1];
					String bzType = str.split(":")[0];
					if ("????????????".equals(bzType)) {
						bargainRemark = bz;
						questionBz.append(bz);
					}
					if ("????????????".equals(bzType)) {
						deliveryRemark = bz;
						questionBz.append(bz);
					}
					if ("????????????".equals(bzType)) {
						colorReplaceRemark = bz;
						questionBz.append(bz);
					}
					if ("????????????".equals(bzType)) {
						sizeReplaceRemark = bz;
						questionBz.append(bz);
					}
					if ("????????????".equals(bzType)) {
						orderNumRemarks = bz;
						questionBz.append(bz);
					}
					if ("???????????????".equals(bzType)) {
						questionsRemarks = bz;
						questionBz.append(bz);
					}
					if ("???????????????".equals(bzType)) {
						unquestionsRemarks = bz;
					}
				}
			}
			if (iia == 0) {
				// if (reason.contains("?????????")) {
				// ????????????
				if (StrUtils.isNotNullEmpty(questionBz.toString())) {
					tempSql = "delete from order_product_source where orderid='"+ orderNo + "' and goodsid='" + goodid + "'";
					stmttt = conn.prepareStatement(tempSql);
					stmttt.executeUpdate();
					// ????????????
					sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,goodsid,goodsdataid,goods_url,"
							+ "goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,usecount,buycount,currency,goods_p_name,bargainRemark,"
							+ "deliveryRemark,colorReplaceRemark,sizeReplaceRemark,orderNumRemarks,questionsRemarks,unquestionsRemarks,"
							+ "purchase_state,od_id,tb_1688_itemid,shop_id,tborderid) values(?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,5,?,?,?,'??????4')";
					stmttt = conn.prepareStatement(sqlll);
					stmttt.setDouble(1, admid);
					stmttt.setInt(2, userid);
					stmttt.setString(3, orderNo);
					stmttt.setInt(4, goodid);
					stmttt.setInt(5, goodsdataid);
					stmttt.setString(6, goods_url);
					stmttt.setString(7, resource);
					stmttt.setString(8, googs_img);
					stmttt.setDouble(9, goodsprice);
					stmttt.setDouble(10, price);
					stmttt.setString(11, goods_title);
					stmttt.setInt(12, googsnumber);
					stmttt.setInt(13, buycount);
					stmttt.setString(14, currency);
					stmttt.setString(15, pname);
					stmttt.setString(16, bargainRemark);
					stmttt.setString(17, deliveryRemark);
					stmttt.setString(18, colorReplaceRemark);
					stmttt.setString(19, sizeReplaceRemark);
					stmttt.setString(20, orderNumRemarks);
					stmttt.setString(21, questionsRemarks);
					stmttt.setString(22, unquestionsRemarks);
					stmttt.setInt(23, od_id);
					stmttt.setString(24, itemid);
					stmttt.setString(25, shop_id);
				} else {
					if (reason.equals("") || reason == null) {
						tempSql = "delete from order_product_source where orderid='"+ orderNo + "' and goodsid='" + goodid + "'";
						stmt=conn.prepareStatement(tempSql);
						stmt.executeUpdate();
//						this.deleteFromSql(tempSql);
						sqlll = " insert  into order_product_source(adminid,userid,addtime,orderid,goodsid,goodsdataid,goods_url,goods_p_url, goods_img_url,goods_price,"
								+ " goods_p_price,goods_name,usecount,buycount,currency,goods_p_name,od_id,companyname,tb_1688_itemid,shop_id,tborderid) "
								+ " SELECT ?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'??????17' "
								+ " FROM DUAL WHERE NOT EXISTS "
								+ " (SELECT * FROM order_product_source WHERE orderid=? and goodsid=? ) ";
						if ("?????????????????????".equals(issuree)) {
							sqls = "update order_details set car_url = ? where	orderid=? and goodsid = ?";
							// ??????????????????????????????????????????
							if (GetConfigureInfo.openSync()) {
								SaveSyncTable.InsertOnlineDataInfo(userid,orderNo, "??????", "order_details","update order_details set car_url ='"+ resource+ "'  where	 orderid='"+ orderNo + "' and goodsid = "+ goodid);
							} else {
								List<String> lstValues = Lists.newArrayList();
								lstValues.add(resource);
								lstValues.add(orderNo);
								lstValues.add(String.valueOf(goodid));
								String runSql = DBHelper.covertToSQL(sqls, lstValues);
								SendMQ.sendMsg(new RunSqlModel(runSql));
								/*Connection conn2 = DBHelper.getInstance().getConnection2();
								stms = conn2.prepareStatement(sqls);
								stms.setString(1, resource);
								stms.setString(2, orderNo);
								stms.setInt(3, goodid);
								stms.executeUpdate();
								DBHelper.getInstance().closeConnection(conn2);*/
							}
							stms2 = conn.prepareStatement(sqls);
							stms2.setString(1, resource);
							stms2.setString(2, orderNo);
							stms2.setInt(3, goodid);
							if (stms2 != null) {
								stms2.executeUpdate();
							}
						}
						stmttt = conn.prepareStatement(sqlll);
						stmttt.setString(14, currency);
						stmttt.setString(15, pname);
						stmttt.setInt(16, od_id);
						stmttt.setString(17, companyName);
						stmttt.setString(18, itemid);
						stmttt.setString(19, shop_id);
						stmttt.setString(20, orderNo);
						stmttt.setInt(21, goodid);
					} else {
						tempSql = "delete from order_product_source where orderid='"+ orderNo + "' and goodsid='" + goodid + "'";
//						this.deleteFromSql(tempSql);
						stmttt = conn.prepareStatement(tempSql);
						stmttt.executeUpdate();
						if (type.indexOf("type") > -1) {
							sqlll = "";
						} else {
							sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,goodsid,goodsdataid,goods_url,goods_p_url, goods_img_url,goods_price,"
									+ "	goods_p_price,goods_name,usecount,buycount,bargainRemark,"
									+ "deliveryRemark,colorReplaceRemark,sizeReplaceRemark,orderNumRemarks,questionsRemarks,unquestionsRemarks,purchase_state, currency,goods_p_name,od_id,tb_1688_itemid,companyname,shop_id,tborderid) "
									+ "	SELECT ?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,?,?,?,?,'??????5' FROM DUAL "
									+ "	WHERE NOT EXISTS (SELECT * FROM order_product_source WHERE orderid=? and goodsid=? );";
						}
						stmttt = conn.prepareStatement(sqlll);
						stmttt.setString(14, bargainRemark);
						stmttt.setString(15, deliveryRemark);
						stmttt.setString(16, colorReplaceRemark);
						stmttt.setString(17, sizeReplaceRemark);
						stmttt.setString(18, orderNumRemarks);
						stmttt.setString(19, questionsRemarks);
						stmttt.setString(20, unquestionsRemarks);
						stmttt.setString(21, currency);
						stmttt.setString(22, pname);
						stmttt.setInt(23, od_id);
						stmttt.setString(24, itemid);
						stmttt.setString(25, companyName);
						stmttt.setString(26, shop_id);
						stmttt.setString(27, orderNo);
						stmttt.setInt(28, goodid);
					}
					stmttt.setDouble(1, admid);
					stmttt.setInt(2, userid);
					stmttt.setString(3, orderNo);
					stmttt.setInt(4, goodid);
					stmttt.setInt(5, goodsdataid);
					stmttt.setString(6, goods_url);
					stmttt.setString(7, resource);
					stmttt.setString(8, googs_img);
					stmttt.setDouble(9, goodsprice);
					stmttt.setDouble(10, price);
					stmttt.setString(11, goods_title);
					stmttt.setInt(12, googsnumber);
					stmttt.setInt(13, buycount);
				}
			}



			else {
				// ??????????????????
				boolean flag = false;
				sqlll = "select goods_p_url from order_product_source where od_id=? and orderid=?;";
				stmttt = conn.prepareStatement(sqlll);
				stmttt.setInt(1, od_id);
				stmttt.setString(2, orderNo);
				rs = stmttt.executeQuery();
				if (rs.next() && rs.getString("goods_p_url") != null && !rs.getString("goods_p_url").equals("")) {
					flag = true;
				}
				// ???????????????
				if (StrUtils.isNotNullEmpty(questionBz.toString())) {
					sqlll = "update order_product_source set tb_1688_itemid="+ itemid
							+ " ,adminid=?,userid=?,addtime=now(),confirm_userid=null,confirm_time=null,"
							+ "purchase_state=5,bargainRemark=?,deliveryRemark=?,colorReplaceRemark=?,sizeReplaceRemark=?,orderNumRemarks=?,questionsRemarks=?,unquestionsRemarks=?,goods_img_url=?,goods_price=?,usecount=?,buycount=?,goods_p_url=?,"
							+ "goods_p_price=?,goods_p_name=?,od_id=?,shop_id=? where goods_url=? and orderid=? and od_id=? ;";
					stmttt = conn.prepareStatement(sqlll);
					stmttt.setDouble(1, admid);
					stmttt.setInt(2, userid);
					stmttt.setString(3, bargainRemark);
					stmttt.setString(4, deliveryRemark);
					stmttt.setString(5, colorReplaceRemark);
					stmttt.setString(6, sizeReplaceRemark);
					stmttt.setString(7, orderNumRemarks);
					stmttt.setString(8, questionsRemarks);
					stmttt.setString(9, unquestionsRemarks);
					stmttt.setString(10, googs_img);
					stmttt.setDouble(11, goodsprice);
					stmttt.setInt(12, googsnumber);
					stmttt.setInt(13, buycount);
					stmttt.setString(14, resource);
					stmttt.setDouble(15, price);
					stmttt.setString(16, pname);
					stmttt.setInt(17, od_id);
					stmttt.setString(18, shop_id);
					stmttt.setString(19, goods_url);
					stmttt.setString(20, orderNo);
					stmttt.setInt(21, od_id);
				} else {
					if (reason.equals("") || reason == null) {
						// 2017-04-13?????????????????????????????????????????????
						if (flag) {
							sqlll = "update order_product_source set purchase_state=0, last_tb_1688_itemid="
									+ itemid
									+ " , adminid=?,userid=?,addtime=now(),remark='',"
									+ "goodsid=?,last_goods_p_url=?,goods_img_url=?,goods_price=?,goods_p_price=?,"
									+ "usecount=?,buycount=?,bargainRemark=?,deliveryRemark=?,colorReplaceRemark=?,sizeReplaceRemark=?,orderNumRemarks=?,questionsRemarks=?,unquestionsRemarks=?,goods_p_name=?,od_id=?,companyname=?,shop_id=? where goods_url=? and orderid=? and od_id=? ";
						} else {
							sqlll = "update order_product_source set purchase_state=0, tb_1688_itemid="
									+ itemid
									+ " , adminid=?,userid=?,addtime=now(),remark='',"
									+ "goodsid=?,goods_p_url=?,goods_img_url=?,goods_price=?,goods_p_price=?,"
									+ "usecount=?,buycount=?,bargainRemark=?,deliveryRemark=?,colorReplaceRemark=?,sizeReplaceRemark=?,orderNumRemarks=?,questionsRemarks=?,unquestionsRemarks=?,goods_p_name=?,od_id=?,companyname=?,shop_id=? where goods_url=? and orderid=? and od_id=? ";
						}
						sqlc = "update order_details set state=0,purchase_state=0 where id=? and orderid=? and state <> 2;";
						stmttt = conn.prepareStatement(sqlll);
						stmc = conn.prepareStatement(sqlc);
						stmttt.setString(10, bargainRemark);
						stmttt.setString(11, deliveryRemark);
						stmttt.setString(12, colorReplaceRemark);
						stmttt.setString(13, sizeReplaceRemark);
						stmttt.setString(14, orderNumRemarks);
						stmttt.setString(15, questionsRemarks);
						stmttt.setString(16, unquestionsRemarks);
						stmttt.setString(17, pname);
						stmttt.setInt(18, od_id);
						stmttt.setString(19, companyName);
						stmttt.setString(20, shop_id);
						stmttt.setString(21, goods_url);
						stmttt.setString(22, orderNo);
						stmttt.setInt(23, od_id);
						stmc.setInt(1, od_id);
						stmc.setString(2, orderNo);
						if ("?????????????????????".equals(issuree)) {
							sqls = "update order_details set car_url = ? where	orderid=? and goodsid = ?";
							// ??????????????????????????????????????????
							if (GetConfigureInfo.openSync()) {
								SaveSyncTable.InsertOnlineDataInfo(userid,orderNo, "??????", "order_details","update order_details set car_url ='"+ resource+ "' where	orderid='" + orderNo+ "' and goodsid = " + goodid);
							} else {
								List<String> lstValues = Lists.newArrayList();
								lstValues.add(resource);
								lstValues.add(orderNo);
								lstValues.add(String.valueOf(goodid));
								String runSql = DBHelper.covertToSQL(sqls, lstValues);
								SendMQ.sendMsg(new RunSqlModel(runSql));
								
							}
							stms2 = conn.prepareStatement(sqls);
							stms2.setString(1, resource);
							stms2.setString(2, orderNo);
							stms2.setInt(3, goodid);
							if (stms2 != null) {
								stms2.executeUpdate();
							}
						}
						// ????????????
						LOG.info("222?????????????????????state,purchase_state?????????0");
						// ??????????????????????????????????????????
						if (GetConfigureInfo.openSync()) {
							String sycnSql = "update order_details set state=0,purchase_state=0 where id="+ od_id+ " and orderid='"+ orderNo+ "' and state <> 2";
							SaveSyncTable.InsertOnlineDataInfo(0, orderNo,"??????", "order_details", sycnSql);
						} else {
							//??????MQ??????????????????
							String tempSqlc = "update order_details set state=0,purchase_state=0 where id="+od_id+" and orderid='"+ orderNo +"' and state <> 2";
							NotifyToCustomerUtil.sendSqlByMq(tempSqlc);
							/*PurchaseThred thread = new PurchaseThred(sqlc,od_id, orderNo);
							thread.start();*/
						}
					} else {
						if (type.indexOf("type") > -1) {
							if (flag) {
								sqlll = "update order_product_source set last_tb_1688_itemid="
										+ itemid
										+ " , adminid=?,userid=?,addtime=now(),goodsid=?,last_goods_p_url=?,"
										+ "goods_img_url=?,goods_price=?,goods_p_price=?,usecount=?,buycount=?,bargainRemark=?,deliveryRemark=?,colorReplaceRemark=?,sizeReplaceRemark=?,orderNumRemarks=?,questionsRemarks=?,unquestionsRemarks=?,"
										+ "purchase_state=5,goods_p_name=?,od_id=?,goodssourcetype=?,companyname=?,shop_id=? where goods_url=? and orderid=? and od_id=?";
							} else {
								sqlll = "update order_product_source set tb_1688_itemid="
										+ itemid
										+ " , adminid=?,userid=?,addtime=now(),goodsid=?,goods_p_url=?,"
										+ "goods_img_url=?,goods_price=?,goods_p_price=?,usecount=?,buycount=?,bargainRemark=?,deliveryRemark=?,colorReplaceRemark=?,sizeReplaceRemark=?,orderNumRemarks=?,questionsRemarks=?,unquestionsRemarks=?,"
										+ "purchase_state=5,goods_p_name=?,od_id=?,goodssourcetype=?,companyname=?,shop_id=? where goods_url=? and orderid=? and od_id=?";
							}
						} else {
							if (flag) {
								sqlll = "update order_product_source set last_tb_1688_itemid="
										+ itemid
										+ " , adminid=?,userid=?,addtime=now(),goodsid=?,last_goods_p_url=?,"
										+ "goods_img_url=?,goods_price=?,goods_p_price=?,usecount=?,buycount=?,bargainRemark=?,deliveryRemark=?,colorReplaceRemark=?,sizeReplaceRemark=?,orderNumRemarks=?,questionsRemarks=?,unquestionsRemarks=?,"
										+ "purchase_state=0,goods_p_name=?,od_id=?,companyname=?,shop_id=? where goods_url=? and orderid=? and od_id=? ";
							} else {
								sqlll = "update order_product_source set tb_1688_itemid="
										+ itemid
										+ " , adminid=?,userid=?,addtime=now(),goodsid=?,goods_p_url=?,"
										+ "goods_img_url=?,goods_price=?,goods_p_price=?,usecount=?,buycount=?,bargainRemark=?,deliveryRemark=?,colorReplaceRemark=?,sizeReplaceRemark=?,orderNumRemarks=?,questionsRemarks=?,unquestionsRemarks=?,"
										+ "purchase_state=0,goods_p_name=?,od_id=?,companyname=?,shop_id=? where goods_url=? and orderid=? and od_id=? ";
							}
							sqlc = "update order_details set state=0,purchase_state=0 where id=? and orderid=?;";
						}
						stmttt = conn.prepareStatement(sqlll);
						stmc = conn.prepareStatement(sqlc);
						stmc.setInt(1, od_id);
						stmc.setString(2, orderNo);
						if (type.indexOf("type") > -1) {
							stmttt.setString(10, bargainRemark);
							stmttt.setString(11, deliveryRemark);
							stmttt.setString(12, colorReplaceRemark);
							stmttt.setString(13, sizeReplaceRemark);
							stmttt.setString(14, orderNumRemarks);
							stmttt.setString(15, questionsRemarks);
							stmttt.setString(16, unquestionsRemarks);
							stmttt.setString(17, pname);
							stmttt.setInt(18, od_id);
							stmttt.setString(19, type.split("@")[0].toString());
							stmttt.setString(20, companyName);
							stmttt.setString(22, shop_id);
							stmttt.setString(23, goods_url);
							stmttt.setString(24, orderNo);
							stmttt.setInt(25, od_id);
						} else {
							stmttt.setString(10, bargainRemark);
							stmttt.setString(11, deliveryRemark);
							stmttt.setString(12, colorReplaceRemark);
							stmttt.setString(13, sizeReplaceRemark);
							stmttt.setString(14, orderNumRemarks);
							stmttt.setString(15, questionsRemarks);
							stmttt.setString(16, unquestionsRemarks);
							stmttt.setString(17, pname);
							stmttt.setInt(18, od_id);
							stmttt.setString(19, companyName);
							stmttt.setString(20, shop_id);
							stmttt.setString(21, goods_url);
							stmttt.setString(22, orderNo);
							stmttt.setInt(23, od_id);
							// ????????????
							LOG.info("222?????????????????????state,purchase_state?????????0");
							// ??????????????????????????????????????????
							if (GetConfigureInfo.openSync()) {
								String syncSql = "update order_details set state=0,purchase_state=0 where id="+ od_id+ " and orderid='"+ orderNo+ "' and state <> 2";
								SaveSyncTable.InsertOnlineDataInfo(0, orderNo,"??????", "order_details", syncSql);
							} else {
								PurchaseThred thread = new PurchaseThred(sqlc,od_id, orderNo);
								thread.start();
							}
						}
					}
					stmttt.setDouble(1, admid);
					stmttt.setInt(2, userid);
					stmttt.setInt(3, goodid);
					stmttt.setString(4, resource);
					stmttt.setString(5, googs_img);
					stmttt.setDouble(6, goodsprice);
					stmttt.setDouble(7, price);
					stmttt.setInt(8, googsnumber);
					stmttt.setInt(9, buycount);
				}
			}
			stmttt.executeUpdate();
			String sql3 = "select id from order_product_source where orderid=? and goodsid=?";
			stmttt = conn.prepareStatement(sql3);
			stmttt.setString(1, orderNo);
			stmttt.setInt(2, goodid);
			rs = stmttt.executeQuery();
			if (rs.next()) {
				// ???????????????????????? whj
				recod_change_source_log(rs.getInt("id"), admid, resource);
			}
			// ?????????
			if (stmc != null) {
				stmc.executeUpdate();
			}
			String sqllll = "SELECT distinct od.goods_pid,od.car_urlMD5,gs.shop_id,gs.address FROM goods_source gs "
					+ "INNER JOIN order_details od ON gs.goods_pid=od.goods_pid AND gs.car_urlMD5=od.car_urlMD5 WHERE od.id='"+od_id+"' AND gs.goods_purl='"+resource+"'";
			stmtttt = conn.prepareStatement(sqllll);
			rs = stmtttt.executeQuery();
			if(rs.next()){
				sqllll = "update goods_source set goods_purl='" + resource
						+ "', goods_img_url=?,goods_price=?,buycount=?,"
						+ "updatetime=now(),shop_id='"+(StringUtils.isStrNull(shop_id)?rs.getString("shop_url"):shop_id)+"',address='"+(StringUtils.isStrNull(straight_address)?rs.getString("address"):straight_address)+"' where goods_pid='"+rs.getString("goods_pid")+"' and car_urlMD5='"+rs.getString("car_urlMD5")+"' and goods_purl=?";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setString(1, googs_img);
				stmtttt.setDouble(2, price);
				stmtttt.setInt(3, buycount);
				stmtttt.setString(4, resource);
			}else{
				sqllll="select goods_pid,car_urlMD5 from order_details where id='"+od_id+"'";
				stmtttt = conn.prepareStatement(sqllll);
				rs = stmtttt.executeQuery();
				if(rs.next()){
					sqllll = "insert into goods_source(goodsdataid,goods_url,goods_purl,goods_img_url,"
							+ "goods_price,goods_name,moq,goodssourcetype,buycount,del,updatetime,goods_pid,car_urlMD5,shop_id,address) "
							+ "values(?,?,?,?,?,?,1,1,?,0,now(),?,?,?,?)";
					stmtttt = conn.prepareStatement(sqllll);
					stmtttt.setInt(1, goodsdataid);
					stmtttt.setString(2, goods_url);
					stmtttt.setString(3, resource);
					stmtttt.setString(4, googs_img);
					stmtttt.setDouble(5, price);
					stmtttt.setString(6, pname);
					stmtttt.setInt(7, buycount);
					stmtttt.setString(8, rs.getString("goods_pid"));
					stmtttt.setString(9, rs.getString("car_urlMD5"));
					stmtttt.setString(10, StringUtils.isStrNull(shop_id)?"":shop_id);
					stmtttt.setString(11, (StringUtils.isStrNull(straight_address)?"":straight_address));
				}
			}
			stmtttt.executeUpdate();
			sqllll="update order_product_source set tb_1688_itemid=?,last_tb_1688_itemid=?,goods_p_url=?,last_goods_p_url=? where orderid=? and od_id=?";
			stmtttt = conn.prepareStatement(sqllll);
			stmtttt.setString(1, itemid);
			stmtttt.setString(2, itemid);
			stmtttt.setString(3, resource);
			stmtttt.setString(4, resource);
			stmtttt.setString(5, orderNo);
			stmtttt.setInt(6, od_id);
			stmtttt.executeUpdate();
			//????????????????????????ID?????????????????????
			sqllll="SELECT c.shop_id FROM order_details od INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid WHERE od.orderid='"+orderNo+"' AND od.goodsid='"+goodid+"'";
			stmtttt = conn.prepareStatement(sqllll);
			rs=stmtttt.executeQuery();
			if(rs.next()){
				String old_shopid=rs.getString("shop_id");
				sqllll="update order_product_source set old_shopid='"+old_shopid+"' where orderid='"+orderNo+"' AND goodsid='"+goodid+"'";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.executeUpdate();
			}
			//?????????ID??????ali_info_data
			try{
				String shop_id_=shop_id;
				sqllll="select * from ali_info_data where shop_id='"+shop_id_+"'";
				stmt=conn.prepareStatement(sqllll);
				rs=stmt.executeQuery();
				if(!rs.next()){
					sqllll = "insert into ali_info_data (address,shop_id) value(?,?)";
					stmt=conn.prepareStatement(sqllll);
					stmt.setString(1, straight_address);
					stmt.setString(2, shop_id_);
					stmt.executeUpdate();
				}
			}catch(Exception e){

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources(rs);
			closeResources(rsa);
			closeResources(rsb);
			closeResources(stmtupdate);
			closeResources(stmt);
			closeResources(stmta);
			closeResources(stmtb);
			closeResources(stmtt);
			closeResources(stmttt);
			closeResources(stmtttt);
			closeResources(conn);
			closeResources(stmc);
			closeResources(stmc2);
			closeResources(stmtn);
			closeResources(stms);
		}
//		 updateOrderPs(orderNo,goodid+"",itemid);
		if ("2".equals(type)) {
			updateGoodsPurl(orderNo, resource, itemid, goods_url, price,od_id,admid,buycount,straight_address,shop_id);
		}
		// updateGoodsPurlgoodsid(orderNo, itemid, goodid + "");
		// updateOrderinfo(orderNo);

		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String writeStr = "######??????????????????" + goodid + ">>>>>>     ??????:"+ time.format(nowTime) + "      ";
		writeStr += "userid:" + userid + "      ";
		writeStr += "orderNo:" + orderNo + "      ";
		writeStr += "goodsid:" + goodid + "      ";
		writeStr += "????????????:" + resource + "      ";
		writeStr += "\r\n\r\n";
		UtilAll.printBufInfo(writeStr);

	}

	/**
	 * ???????????????????????? op_id ???????????????Id adminid ????????????????????? source_url ???????????????????????? whj 2017-04-13
	 * **/
	public void recod_change_source_log(int op_id, int adminid,
	                                    String source_url) {
		PreparedStatement stmt = null;
		Connection con = DBHelper.getInstance().getConnection();
		String sql = "insert into change_source_log (op_id,change_time,change_adminid,change_desc,source_url) values(?,now(),?,?,?)";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, op_id);
			stmt.setInt(2, adminid);
			stmt.setInt(3, Constant.SOURCE_TYPE);
			stmt.setString(4, source_url);
			stmt.executeUpdate();
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
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void AddNoGS(String goods_url, String goods_type, String userid) { // ?????????????????????
		PreparedStatement stmt = null;
		Connection con = DBHelper.getInstance().getConnection();
		String temSql;
		temSql = "delete from sourceofgoods where goods_url='" + goods_url
				+ "' and goods_type='" + goods_type + "'";
//		this.deleteFromSql(temSql);
		try {
			stmt=con.prepareStatement(temSql);
			stmt.executeUpdate();
			String sql = "INSERT  sourceofgoods (goods_url,goods_type,admin,addtime) "
					+ " SELECT ?,?,?,NOW()  FROM DUAL WHERE NOT EXISTS "
					+ " (SELECT * FROM sourceofgoods WHERE goods_url=? AND goods_type=?)";
			LOG.info("?????????????????????");
			stmt = con.prepareStatement(sql);
			stmt.setString(1, goods_url);
			stmt.setString(2, goods_type);
			int admin = Integer.valueOf(userid);
			stmt.setInt(3, admin);
			stmt.setString(4, goods_url);
			stmt.setString(5, goods_type);
			stmt.executeUpdate();
		} catch (NumberFormatException e) {
			e.printStackTrace();
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
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void closeResources(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void closeResources(ResultSet stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void closeResources(Connection stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// ??????????????????
	public void AddRmark(String orderNo, int gooddataid, String remark,
	                     int goodid) {
		String sql = "update order_product_source set remark=?,purchase_state=5 where orderid=? and goodsdataid=? and goodsid=?";
		LOG.info("1069");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, remark);
			stmt.setString(2, orderNo);
			stmt.setInt(3, gooddataid);
			stmt.setInt(4, goodid);
			stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public OrderProductSource ShowRmark(String orderNo, int gooddataid,
                                        int goodid) {
		String remarks = "";
		String sql = "select *  from order_product_source where orderid=? and goodsdataid=? and goodsid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OrderProductSource bean = new OrderProductSource();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, gooddataid);
			stmt.setInt(3, goodid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				bean.setBargainRemark(rs.getString("bargainRemark"));
				bean.setDeliveryRemark(rs.getString("deliveryRemark"));
				bean.setColorReplaceRemark(rs.getString("colorReplaceRemark"));
				bean.setSizeReplaceRemark(rs.getString("sizeReplaceRemark"));
				bean.setOrderNumRemarks(rs.getString("orderNumRemarks"));
				bean.setQuestionsRemarks(rs.getString("questionsRemarks"));
				bean.setUnquestionsRemarks(rs.getString("unquestionsRemarks"));
				bean.setAgainRemarks(rs.getString("againRemarks"));
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
		return bean;
	}

	// ????????????
	@Override
	public String getOtherSources(String orderNo, int goodid, String goods_url) {
		String uuu = "";
		String gUrl = "<table border='1'><tr><td width='20%'>????????????</td><td width='50%'>????????????</td><td width='20%'>????????????</td><td width='10%'>????????????</td></tr>";
		String sql = "";
		sql="SELECT gs.goods_purl AS nv,MIN(gs.goods_price) AS gp,gs.updatetime AS ut,gs.shop_id,gs.address FROM goods_source gs"
				+ " INNER JOIN order_details od ON gs.goods_pid=od.goods_pid AND gs.car_urlMD5=od.car_urlMD5"
				+ " WHERE od.orderid='"+orderNo+"' AND od.goodsid='"+goodid+"' group by ut DESC";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				String url = rs.getString("nv");
				uuu = uuu
						+ "<tr><td width='20%'>"
						+ rs.getString("gp")
						+ "RMB</td><td width='60%'><a href='"
						+ rs.getString("nv")
						+ "' target='block'>"
						+ url
						+ "</a></td>"
						+ "<td width='20%'><input type='hidden' id='"+i+"_this_shop_id' value='"+(StringUtils.isStrNull(rs.getString("shop_id")) || "".equals(rs.getString("shop_id"))?"":rs.getString("shop_id"))+"'/>"
						+ "<input type='hidden' id='"+i+"_this_address' value='"+rs.getString("address")+"'/><input type='button' value='????????????' onclick='FnUseThis("
						+ rs.getString("gp") + "," + i + ");'>"
						+ "<input type='hidden' id='id_" + i + "' value='"
						+ url + "'></td><td width='10%'>" + rs.getString("ut")
						+ "</td></tr>";
				i++;
			}
			if (uuu == "" || uuu == null) {
				uuu = "<tr><td colspan='3' align='center'>?????????????????????</td></tr>";
			}
			gUrl = gUrl + uuu + "</table>";
		} catch (SQLException e) {
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
		return gUrl;
	}

	// ???????????????????????????ID//PurchaseExportBefore.jsp
	@Override
	public List<outIdBean> getOutNowId() {
		List<outIdBean> idList = new ArrayList<outIdBean>();
		// String sql =
		// "select distinct(oi.user_id) as id,of.orderno from orderinfo oi inner
		// join payment pm on "
		// +
		// "pm.orderid=oi.order_no and oi.state='2' inner join user on
		// user.id=oi.user_id left join "
		// +
		// "order_fee of on oi.order_no=of.orderno having orderno is null order
		// by oi.user_id ";

		String sql = "select distinct(userid) as id from order_fee where state=4 or state=0 or state=1 "; // ???????????????????????????
		// ???????????????????????????
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				outIdBean oib = new outIdBean();
				oib.setId(rs.getInt("id"));
				// oib.setEmail(rs.getString("email"));
				idList.add(oib);
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
		return idList;
	}



	@Override
	// PurchaseExport.jsp
	public List<outIdBean> getOutNowIdTwo() {
		List<outIdBean> idList = new ArrayList<outIdBean>();
		String sql = "select distinct(userid) as id from order_fee where state=1 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				outIdBean oib = new outIdBean();
				oib.setId(rs.getInt("id"));
				idList.add(oib);
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
		return idList;
	}

	// ????????????????????????
	@Override
	public String getAddressByOrderID(String orderNo) {
		String AddRess = "";
		StringBuffer sb = new StringBuffer();
		String sql = "select z.country,oa.statename,oa.address2,oa.address,oa.zipcode,oa.street "
				+ "from zone z,order_address oa where z.id=oa.country and orderNo=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if ((rs.getString("street") != null)
						&& !(rs.getString("street")).equals("")) {
					sb.append(rs.getString("street") + ",");
				}
				if ((rs.getString("address") != null)
						&& !(rs.getString("address")).equals("")) {
					sb.append(rs.getString("address") + ",");
				}
				if ((rs.getString("address2") != null)
						&& !(rs.getString("address2")).equals("")) {
					sb.append(rs.getString("address2") + ",");
				}
				if (rs.getString("statename") != null
						&& !(rs.getString("statename")).equals("")) {
					sb.append(rs.getString("statename") + " ");
				}
				if (rs.getString("zipcode") != null
						&& !(rs.getString("zipcode")).equals("")) {
					sb.append(rs.getString("zipcode") + ",");
				}
				if ((rs.getString("country") != null)
						&& !(rs.getString("country")).equals("")) {
					sb.append(rs.getString("country") + " ");
				}
			} else {
				sb.append("???????????????");
			}
			AddRess = sb.toString();
		} catch (SQLException e) {
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
		return AddRess;
	}

	// ??????//??????forwarder???
	@Override
	public void OutPortNow(String orderno, String wuliuNumber,
	                       String transport, String userid) {
		// String sqlorderinfo =
		// "update orderinfo set state='3' where order_no=?";//??????orderinfo???;
		String sqlorderinfo = "";// "update orderinfo set state='3' where
		// order_no in (?)";//??????orderinfo???;
		// String sqlorder_fee =
		// "update order_fee set state=2 where orderno=?";//??????order_fee???;
		String sqlorder_fee = "";// "update order_fee set state=2 where orderno
		// in (?)";//??????order_fee???;

		// ?????????????????????
		String sql = "select group_concat(\"'\",orderno,\"'\") orderarr from order_fee  where mergeOrders= '"
				+ orderno + "' and state='1' ";

		String orderNew = orderno;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null, stmtf = null, stmtf2 = null, stmtfee = null, mergeStmt, gxzt, stmtid = null;
		ResultSet rs = null;
		try {

			// ?????????????????????
			mergeStmt = conn.prepareStatement(sql);
			rs = mergeStmt.executeQuery();
			if (rs.next()) {
				orderNew = rs.getString("orderarr");
			}

			sqlorderinfo = "update orderinfo set state='3' where order_no in ("
					+ orderNew + ")";
			stmt = conn.prepareStatement(sqlorderinfo);
			// stmt.setString(1,orderNew);
			stmt.executeUpdate();

			// ????????????????????????
			// ??????????????????????????????????????????
			if (GetConfigureInfo.openSync()) {
				SaveSyncTable.InsertOnlineDataInfo(0, orderNew, "????????????",
						orderNew,
						"update orderinfo set state='3' where order_no in ("
								+ orderNew + ")");
			} else {

				SendMQ.sendMsgByRPC(new RunSqlModel(sqlorderinfo));
			}
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// try {
			// stmt2.executeUpdate();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// }).start();

			// ?????????????????????
			sqlorderinfo = "update id_relationtable set state='1' where  is_delete != 1 and orderid in ("
					+ orderNew + ")";
			stmtid = conn.prepareStatement(sqlorderinfo);
			stmtid.executeUpdate();

			sql = "select orderno from order_fee  where mergeOrders= '"
					+ orderno + "' and state='1' ";
			mergeStmt = conn.prepareStatement(sql);
			rs = mergeStmt.executeQuery();
			while (rs.next()) {
				String strTemp = rs.getString("orderno");
				String sqlforwarder = "insert into forwarder(order_no,express_no,logistics_name,createtime,isneed,yfhorder,ship_name,userid,morderno,comType) values(?,?,?,now(),?,?,?,?,'"
						+ orderno + "',1) ";// ??????forwarder???;

				if (strTemp.equals(orderno)) { // ??????????????????
					sqlforwarder = "insert into forwarder(order_no,express_no,logistics_name,createtime,isneed,yfhorder,ship_name,userid,morderno) values(?,?,?,now(),?,?,?,?,'"
							+ orderno + "') ";// ??????forwarder???;
				}
				stmtf = conn.prepareStatement(sqlforwarder);
				stmtf.setString(1, strTemp);
				stmtf.setString(7, userid);
				int in;
				if (transport.equals("yyffhh")) { // ???????????????
					stmtf.setString(2, wuliuNumber); // ???????????????????????????
					stmtf.setString(3, "?????????");
					stmtf.setString(5, "");
					stmtf.setString(6, "?????????");
					in = 1;
				} else {
					stmtf.setString(2, wuliuNumber);
					stmtf.setString(3, transport);// 4PX ??? ??????????????????
					stmtf.setString(5, "");
					stmtf.setString(6, "");
					in = 0;
				}
				stmtf.setInt(4, in);
				stmtf.executeUpdate();

				// ????????????
				List<String> lstValues = Lists.newArrayList();
//				stmtf2 = conn2.prepareStatement(sqlforwarder);
				lstValues.add( strTemp);
				if (transport.equals("yyffhh")) { // ???????????????
					lstValues.add(wuliuNumber); // ???????????????????????????
					lstValues.add("?????????");
					lstValues.add("1");
					lstValues.add( "");
					lstValues.add( "?????????");
					in = 1;
				} else {
					lstValues.add( wuliuNumber);
					lstValues.add(transport);// 4PX ??? ??????????????????
					lstValues.add("0");
					lstValues.add("");
					lstValues.add("");
					in = 0;
				}
				lstValues.add(userid);
//				stmtf2.executeUpdate();
				String runSql = DBHelper.covertToSQL(sqlforwarder, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));

				String gxztSql = "update order_details set state='0' where orderid=? ";
				/*gxzt = conn2.prepareStatement(gxztSql);
				gxzt.setString(1, strTemp);
				gxzt.executeUpdate();*/
				lstValues = Lists.newArrayList();
				lstValues.add(strTemp);
				runSql = DBHelper.covertToSQL(gxztSql, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));

			}

			sqlorder_fee = "update order_fee set state=2 where orderno in ("
					+ orderNew + ")";// ??????order_fee???;
			stmtfee = conn.prepareStatement(sqlorder_fee);
			// stmtfee.setString(1,orderNew );
			stmtfee.executeUpdate();
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

			if (stmtid != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtf != null) {
				try {
					stmtf.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfee != null) {
				try {
					stmtfee.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	// ????????????Email
	@Override
	public String getEmail(int userid) {
		String Email = "";
		String sql = "select email from user where id=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Email = rs.getString("email");
			} else {
				Email = "";
			}
		} catch (SQLException e) {
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
		return Email;
	}

	// ????????????
	@Override
	public int PurchaseComfirmOne(String orderNo, int goodsid, int adminid) {
		int i = 0;
		String sql = "update order_product_source set confirm_userid=null,confirm_time=null,purchase_state=1 where orderid=? and goodsid=? and del=0";
		String sqltwo = "update order_details set purchase_state=0,purchase_time=null,purchase_confirmation=null where orderid=? and goodsid=?";
		String sqlthree = "update orderinfo  set state='5',purchase_number=purchase_number-1 where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null, stmttwo = null, stmtinfo = null, stmttwo2 = null, stmtinfo2 = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodsid);
			stmt.executeUpdate();
			i++;
			stmttwo = conn.prepareStatement(sqltwo);
			stmttwo.setString(1, orderNo);
			stmttwo.setInt(2, goodsid);
			stmttwo.executeUpdate();

			stmtinfo = conn.prepareStatement(sqlthree);
			stmtinfo.setString(1, orderNo);
			stmtinfo.executeUpdate();
			i++;
			// ????????????
			/*stmttwo2 = conn2.prepareStatement(sqltwo);
			stmttwo2.setString(1, orderNo);
			stmttwo2.setInt(2, goodsid);
			stmttwo2.executeUpdate();*/
			List<String> lstValues = Lists.newArrayList();
			lstValues.add(orderNo);
			lstValues.add(String.valueOf(goodsid));
			String runSql = DBHelper.covertToSQL(sqltwo, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));

			/*stmtinfo2 = conn2.prepareStatement(sqlthree);
			stmtinfo2.setString(1, orderNo);
			stmtinfo2.executeUpdate();*/
			lstValues = Lists.newArrayList();
			lstValues.add(orderNo);
			runSql = DBHelper.covertToSQL(sqlthree, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));

			// this.order_state(conn, orderNo);
			// this.order_state(conn2, orderNo);
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
			if (stmttwo != null) {
				try {
					stmttwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtinfo != null) {
				try {
					stmtinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttwo2 != null) {
				try {
					stmttwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtinfo2 != null) {
				try {
					stmtinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	// ????????????
	@Override
	public int purchaseConfirmation(String type, int admid, String orderNo,
	                                int goodid) {
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int r = 0;
		String sql = "update order_product_source set confirm_userid=" + admid
				+ ",confirm_time=now(),purchase_state=3 " + "where orderid='"
				+ orderNo + "' and del=0";

		if ("1".equals(type)) {
			sql += " and goodsid='" + goodid + "'";
		}

		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return r;
	}

	// ??????????????????
	@Override
	public int PurchaseComfirmTwo(int userid, String orderNo, int od_id,
	                              int goodid, int goodsdataid, int admid, String goodsurl,
	                              String googsimg, String goodsprice, String goodstitle,
	                              int googsnumber, String oldValue, String newValue, int purchaseCount) {

		int i = 0;

		String sql = "select count(id) as sum from order_change where orderNo=? and goodId=?";
		String sqltwo = "update order_details set purchase_state=1,purchase_time=now(),purchase_confirmation=? where orderid=? and goodsid=?";
		String sqlthree = "update orderinfo set purchase_number=purchase_number+1 where order_no=?";
		Connection conn2 = DBHelper.getInstance().getConnection2();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null, rse = null, rsb = null;
		PreparedStatement stmtb = null, stmtttt = null, stmtfind = null, stmt = null, stmtdel = null;
		PreparedStatement stmtinfo = null, stmtinfo2 = null, stmttwo = null, stmttwo2 = null, stmtt = null, stmttt = null;
		int ii = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodid);
			stmttwo = conn.prepareStatement(sqltwo);
			stmttwo.setString(1, this.getUserbyID(admid + ""));
			stmttwo.setString(2, orderNo);
			stmttwo.setInt(3, goodid);
			// order_details ??????2???
			/*stmttwo2 = conn2.prepareStatement(sqltwo);
			stmttwo2.setString(1, this.getUserbyID(admid + ""));
			stmttwo2.setString(2, orderNo);
			stmttwo2.setInt(3, goodid);
			stmttwo2.executeUpdate();*/
			List<String> lstValues = Lists.newArrayList();
			lstValues.add(this.getUserbyID(admid + ""));
			lstValues.add(orderNo);
			lstValues.add(String.valueOf(goodid));
			String runSql = DBHelper.covertToSQL(sqltwo, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));


			rs = stmt.executeQuery();
			stmttwo.executeUpdate();
			while (rs.next()) {
				ii = rs.getInt("sum");
			}
			if (ii > 1) {
				String sqlsetdel = "update order_change set del_state=1 where order_change.orderNo=? and order_change.goodId=?";
				stmtdel = conn.prepareStatement(sqlsetdel);
				stmtdel.setString(1, orderNo);
				stmtdel.setInt(2, goodid);
				stmtdel.executeUpdate();
			}
			String sqll = "";
			if (ii != 1) {
				// sqll = "insert into
				// order_change(order_change.oldValue,order_change.newValue,"
				// +
				// "order_change.orderNo,order_change.goodId,order_change.ropType,order_change.status,"
				// + "order_change.dateline,order_change.del_state)
				// values(?,?,?,?,6,0,now(),0);";
			} else if (ii == 1) {
				// sqll = "update order_change set
				// order_change.oldValue=?,order_change.newValue=?,order_change.dateline=now(),"
				// + " order_change.ropType=6 where order_change.orderNo=? and
				// order_change.goodId=?";
			}
			// stmtt = conn.prepareStatement(sqll);
			// stmtt.setString(1,oldValue);
			// stmtt.setString(2,newValue);
			// stmtt.setString(3,orderNo);
			// stmtt.setInt(4,goodid);
			// stmtt.executeUpdate();
			i++;

			// ?????????????????????
			int res = 0;
			String sqlfind = "select count(id) as sid from order_product_source where orderid='"
					+ orderNo + "' and goodsid=" + goodid;
			stmtfind = conn.prepareStatement(sqlfind);
			rse = stmtfind.executeQuery();
			if (rse.next()) {
				res = rse.getInt("sid");
			}

			if (res == 0) {
				String sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,confirm_userid,confirm_time,"
						+ "goodsid,goodsdataid,goods_url,goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,"
						+ "usecount,buycount,purchase_state,od_id,tborderid) values(?,?,now(),?,?,now(),?,?,?,?,?,?,?,?,?,?,3,?,'??????7') ";
				stmttt = conn.prepareStatement(sqlll);
				stmttt.setDouble(1, admid);
				stmttt.setInt(2, userid);
				stmttt.setString(3, orderNo);
				stmttt.setInt(4, userid);
				stmttt.setInt(5, goodid);
				stmttt.setInt(6, goodsdataid);
				stmttt.setString(7, goodsurl);
				stmttt.setString(8, newValue);
				stmttt.setString(9, googsimg);
				stmttt.setString(10, goodsprice);
				stmttt.setString(11, oldValue);
				stmttt.setString(12, goodstitle);
				stmttt.setInt(13, googsnumber);
				stmttt.setInt(14, purchaseCount);
				stmttt.setInt(15, od_id);
				stmttt.executeUpdate();
				i++;
			} else {
				String sqlll = "update order_product_source set confirm_userid="
						+ admid
						+ ",confirm_time=now(),purchase_state=3 "
						+ "where orderid='"
						+ orderNo
						+ "' and goodsid="
						+ goodid + " and del=0";
				stmttt = conn.prepareStatement(sqlll);
				stmttt.executeUpdate();
				i++;
			}
			int iib = 0;
			String sqlb = "select count(id) as sumb from goods_source where goods_url=? and goods_purl=? ";
			stmtb = conn.prepareStatement(sqlb);
			stmtb.setString(1, goodsurl);
			stmtb.setString(2, newValue);
			rsb = stmtb.executeQuery();
			i++;
			if (rsb.next()) {
				iib = rsb.getInt("sumb");
			}
			String sqllll = "";
			if (iib == 0) {// goods_source???//
				sqllll = "insert into goods_source(goodsdataid,goods_url,goods_purl,goods_img_url,"
						+ "goods_price,goods_name,moq,goodssourcetype,buycount,del,updatetime) "
						+ "values(?,?,?,?,?,?,1,1,?,0,now())";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setInt(1, goodsdataid);
				stmtttt.setString(2, goodsurl);
				stmtttt.setString(3, newValue);
				stmtttt.setString(4, googsimg);
				stmtttt.setString(5, oldValue);
				stmtttt.setString(6, goodstitle);
				stmtttt.setInt(7, purchaseCount);
			} else {
				sqllll = "update goods_source set goods_img_url=?,goods_price=?,buycount=?,"
						+ "updatetime=now() where goods_url=? and goods_purl=?";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setString(1, googsimg);
				stmtttt.setString(2, oldValue);
				stmtttt.setInt(3, purchaseCount);
				stmtttt.setString(4, goodsurl);
				stmtttt.setString(5, newValue);
			}
			stmtttt.executeUpdate();
			i++;
			stmtinfo = conn.prepareStatement(sqlthree);
			stmtinfo.setString(1, orderNo);
			stmtinfo.executeUpdate();
			// ??????????????????2???
			/*stmtinfo2 = conn2.prepareStatement(sqlthree);
			stmtinfo2.setString(1, orderNo);
			stmtinfo2.executeUpdate();*/
			lstValues = Lists.newArrayList();
			lstValues.add(orderNo);
			runSql = DBHelper.covertToSQL(sqlthree, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));
			
			i++;
			this.order_state(conn, orderNo);
			this.order_state(conn2, orderNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rsb != null) {
				try {
					rsb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtdel != null) {
				try {
					stmtdel.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtb != null) {
				try {
					stmtb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtttt != null) {
				try {
					stmtttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rse != null) {
				try {
					rse.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfind != null) {
				try {
					stmtfind.close();
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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttwo != null) {
				try {
					stmttwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttt != null) {
				try {
					stmttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtinfo != null) {
				try {
					stmtinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	public void order_state(Connection conn, String orderno) {
		try {
			java.sql.CallableStatement cst = conn
					.prepareCall("{call update_order_purchase_state(?)}");
			cst.setString(1, orderno);
			cst.execute();
			cst.close();
			cst = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	// PurchaseExportBefore.jsp
	public List<outIdBean> findOutId(int uid, String ordernolist) {
		List<outIdBean> idList = new ArrayList<outIdBean>();
		StringBuffer sb = new StringBuffer(
				"select distinct(oi.user_id) as id,user.email from orderinfo oi,payment pm,user "
						+ "where pm.orderid=oi.order_no and oi.state='2' and user.id=oi.user_id ");
		if (uid != 0) {
			sb.append("and oi.user_id=? order by oi.user_id limit 1");
		} else {
			sb.append("order by oi.user_id limit 1");
		}

		String sql = sb.toString();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (uid != 0) {
				stmt.setInt(1, uid);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				outIdBean oib = new outIdBean();
				oib.setId(rs.getInt("id"));
				oib.setEmail(rs.getString("email"));
				List<PurchaseBean> plist = this.getOutByID(rs.getInt("id"),
						ordernolist);
				oib.setPurchaseBean(plist);
				if (plist == null) {

				} else {
					idList.add(oib);
				}
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
		return idList;
	}

	//
	public List<PurchaseBean> getOutByID(int userid, String ordernolist) {
		String[] ck = null;
		if (ordernolist == null || ordernolist.equals("")) {
			ordernolist = "<br/>";
			ck = ordernolist.split("<br/>");
		} else {
			ordernolist = ordernolist.replace(",", "<br/>");
			ck = ordernolist.split("<br/>");
		}
		List<PurchaseBean> pbList = new ArrayList<PurchaseBean>();
		// StringBuffer sb = new
		// StringBuffer("select
		// distinct(oi.orderid),oi.order_no,of.orderno,oi.create_time,oi.user_id,user.email,min(pm.createtime)
		// as ct,"
		// +
		// "oi.delivery_time,oi.details_number,oi.purchase_number from
		// user,orderinfo oi inner join payment pm "
		// +
		// "on pm.orderid=oi.order_no left join order_fee of on
		// oi.order_no=of.orderno where oi.state='2' and user.id=oi.user_id and
		// pm.paystatus=1 and user_id=? ");

		// ???????????? ??????order_fee ?????????4??? ??????????????????having of.orderno is null
		// StringBuffer sb = new
		// StringBuffer("select
		// distinct(oi.orderid),oi.order_no,of.state,of.orderno,oi.create_time,oi.user_id,user.email,min(pm.createtime)
		// as ct,"
		// +
		// "oi.delivery_time,oi.details_number,oi.purchase_number from
		// user,orderinfo oi inner join payment pm "
		// +
		// "on pm.orderid=oi.order_no left join order_fee of on
		// oi.order_no=of.orderno where oi.state='2' and user.id=oi.user_id and
		// pm.paystatus=1 and user_id=? and (of.state=4 or of.state=0) ");

		StringBuffer sb = new StringBuffer(
				"select distinct(oi.orderid),(select group_concat(orderno) orderarr from order_fee  "
						+ "where mergeOrders= of.mergeOrders)as orderarr,of.mergeOrders as order_no,of.state,of.orderno,oi.create_time,"
						+ "oi.user_id,user.email,min(pm.createtime) as ct,oi.delivery_time,oi.details_number,oi.purchase_number  from user,"
						+ "orderinfo oi inner join payment pm on pm.orderid=oi.order_no inner join "
						+ "(select * from order_fee GROUP BY mergeOrders) of on oi.order_no=of.mergeOrders "
						+ "where oi.state='2' and user.id=oi.user_id and pm.paystatus=1 and user_id=? and (of.state=4 or of.state=0)  ");
		if (ck.length != 0) {
			sb.append("and oi.order_no in (");
			for (int i = 0; i < ck.length; i++) {
				if (i != ck.length - 1) {
					sb.append("?,");
				} else {
					sb.append("?");
				}
			}
			sb.append(")");
		}
		// sb.append("group by oi.orderid,oi.order_no having of.orderno is
		// null");
		sb.append("group by oi.orderid,oi.order_no"); // ?????????order_fee ?????????0??????????????????
		String sql = sb.toString();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			if (ck.length != 0) {
				for (int i = 2; i < ck.length + 2; i++) {
					stmt.setString(i, ck[i - 2]);
				}
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				PurchaseBean purchaseBean = new PurchaseBean();
				purchaseBean.setUserid(rs.getInt("user_id"));// ??????ID
				purchaseBean.setOfState(rs.getString("state")); // order_fee ??????
				// ??????????????????
				purchaseBean.setName(rs.getString("email"));// ??????email
				purchaseBean.setOrderid(rs.getInt("orderid"));// ??????ID
				purchaseBean.setOrderNo(rs.getString("order_no"));// ????????????
				purchaseBean.setOrdertime((rs.getString("create_time"))
						.substring(0, 10));// ????????????
				purchaseBean.setPaytime((rs.getString("ct")).substring(0, 10));// ????????????
				purchaseBean.setOrderarr(rs.getString("orderarr")); // ?????????????????????
				Date date = sdf.parse(rs.getString("ct"));
				purchaseBean.setDeliveryTime(this.beforNumDay(date,
						rs.getInt("delivery_time")));// ????????????
				purchaseBean.setDetails_number(rs.getInt("details_number")
						- rs.getInt("purchase_number"));// ???????????????
				purchaseBean.setPurchase_number(rs.getInt("purchase_number"));// ???????????????
				purchaseBean.setOrderaddress(this.getAddressByOrderID(rs
						.getString("order_no")));
				List<PurchaseDetailsBean> pplist = this.getPurchaseDetails(rs
						.getString("order_no"));
				purchaseBean.setPurchaseDetailsBean(pplist);
				if (pplist == null) {
					System.out.println("???????????????");
				} else {
					pbList.add(purchaseBean);
				}
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
		return pbList;
	}

	public List<outIdBean> findOutIdTwo(int uid) {
		List<outIdBean> idList = new ArrayList<outIdBean>();
		StringBuffer sb = new StringBuffer(
				"select distinct(of.userid) as id,user.email from order_fee of,payment pm,user "
						+ "where pm.orderid=of.orderno and user.id=of.userid ");
		if (uid != 0) {
			sb.append("and of.userid=? and of.state=1 order by of.userid limit 1 ");
		} else {
			sb.append("and of.state=1 order by of.userid limit 1 ");
		}
		String sql = sb.toString();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (uid != 0) {
				stmt.setInt(1, uid);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				outIdBean oib = new outIdBean();
				oib.setId(rs.getInt("id"));
				oib.setEmail(rs.getString("email"));
				List<PurchaseBean> plist = this.getOutByIDTwo(rs.getInt("id"));
				oib.setPurchaseBean(plist);
				if (plist == null) {

				} else {
					idList.add(oib);
				}
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
		return idList;
	}

	// ????????????????????????????????????//PurchaseExport.jsp
	public List<PurchaseBean> getOutByIDTwo(int userid) {
		List<PurchaseBean> pbList = new ArrayList<PurchaseBean>();
		StringBuffer sb = new StringBuffer(
				"select info.*,of.state from order_fee of inner join (select distinct(oi.orderid),"
						+ "oi.order_no,oi.create_time,oi.user_id,user.email,oi.delivery_time,"
						+ "oi.details_number,oi.purchase_number from order_fee,user,orderinfo oi inner join payment pm "
						+ "on pm.orderid=oi.order_no where oi.state='2' and user.id=oi.user_id and pm.paystatus=1 and "
						+ "user_id=?) info on info.order_no=of.orderno and of.state=1 ");
		sb.append("group by info.orderid,info.order_no");
		String sql = sb.toString();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				PurchaseBean purchaseBean = new PurchaseBean();
				purchaseBean.setUserid(rs.getInt("user_id"));// ??????ID
				purchaseBean.setName(rs.getString("email"));// ??????email
				purchaseBean.setOrderid(rs.getInt("orderid"));// ??????ID
				purchaseBean.setOrderNo(rs.getString("order_no"));// ????????????
				purchaseBean.setOrdertime((rs.getString("create_time"))
						.substring(0, 10));// ????????????
				purchaseBean.setDetails_number(rs.getInt("details_number")
						- rs.getInt("purchase_number"));// ???????????????
				purchaseBean.setPurchase_number(rs.getInt("purchase_number"));// ???????????????
				purchaseBean.setOrderaddress(this.getAddressByOrderID(rs
						.getString("order_no")));
				List<PurchaseDetailsBean> pplist = this.getPurchaseDetails(rs
						.getString("order_no"));
				purchaseBean.setPurchaseDetailsBean(pplist);
				if (pplist == null) {
					System.out.println("???????????????");
				} else {
					pbList.add(purchaseBean);
				}
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
		return pbList;
	}

	// ??????????????????????????????????????????//
	public List<PurchaseDetailsBean> getPurchaseDetails(String orderno) {
		List<PurchaseDetailsBean> pdbList = new ArrayList<PurchaseDetailsBean>();
		String sql = "select odinfo.*,oc.oldValue,oc.newValue,oc.dateline from "
				+ "(select ods.goodsprice,ods.goodsname,ods.yourorder,gc.id,gc.goodsdata_id,gc.remark,gc.goods_url,"
				+ "gc.goods_title,gc.googs_img,gc.googs_price,gc.goods_type,gc.currency,ods.orderid "
				+ "from order_details ods,goods_car gc where ods.goodsid=gc.id and ods.orderid=? and ods.state!=2) "
				+ "odinfo left join order_change oc on oc.orderNo=odinfo.orderid "
				+ "and oc.goodid=odinfo.id and oc.ropType=6 group by odinfo.id";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderno);
			rs = stmt.executeQuery();
			while (rs.next()) {
				PurchaseDetailsBean purchaseDetailsBean = new PurchaseDetailsBean();
				purchaseDetailsBean.setGoodsid(rs.getInt("id"));
				purchaseDetailsBean.setGoodsdata_id(rs.getInt("goodsdata_id"));
				purchaseDetailsBean.setGoogs_img(rs.getString("googs_img"));
				purchaseDetailsBean.setGoods_url(rs.getString("goods_url"));
				String goodtype = "<br/>";
				if ((rs.getString("goods_type")).contains("@")) {
					String[] gdtp = (rs.getString("goods_type")).split(",");
					for (String ty : gdtp) {
						goodtype = goodtype
								+ ty.substring(0, (ty.indexOf("@"))) + ";<br/>";
					}
				} else {
					goodtype = rs.getString("goods_type");
				}
				purchaseDetailsBean.setGoods_type(goodtype);
				purchaseDetailsBean.setGoods_title(rs.getString("goods_title"));
				purchaseDetailsBean.setGoods_price(rs.getString("googs_price"));
				purchaseDetailsBean.setCurrency(rs.getString("currency"));
				purchaseDetailsBean.setGoogs_number(rs.getInt("yourorder"));
				purchaseDetailsBean.setRemark(rs.getString("remark"));
				pdbList.add(purchaseDetailsBean);
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
		return pdbList;
	}

	@Override
	public int splitPage() {
		return total;
	}

	@Override
	public List<StraightHairPojo> StraightHairList() {
		List<StraightHairPojo> list=new ArrayList<StraightHairPojo>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			String sql="SELECT distinct od.goodsname,oi.user_id AS userid,od.orderid,od.goodsid,od.car_type AS sku,od.car_img AS img," +
					"od.car_url,od.straight_time AS times,a.admName AS buyer,od.straight_flag AS state,case when od.shipno='0' then '????????????' else od.shipno end as shipno," +
					"(SELECT DISTINCT LOCATE('??????',shipstatus) FROM taobao_1688_order_history WHERE shipno=od.shipno) AS states," +
					"CASE WHEN od.remark='' THEN '?????????' ELSE od.remark END AS remark " +
					"FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no INNER JOIN admin_r_user a ON oi.user_id=a.userid " +
					"INNER JOIN goods_distribution g ON od.id=g.odid INNER JOIN admuser ad ON g.admuserid=ad.id " +
					"WHERE od.straight_flag=2 AND oi.state=1 AND od.state<2 ";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				StraightHairPojo s=new StraightHairPojo();
				s.setOrderid(rs.getString("orderid"));
				s.setGoodsid(rs.getString("goodsid"));
				s.setStates(rs.getString("states"));
				list.add(s);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
		}
		return list;
	}

	@Override
	public int updateState(String orderid, String goodsid) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{

			String sql="update order_details set state=1,checked=1 where orderid='"+orderid+"' and goodsid='"+goodsid+"'";
			stmt=conn.prepareStatement(sql);
			stmt.executeUpdate();
			SendMQ.sendMsg(new RunSqlModel(sql));
			sql="SELECT IFNULL(SUM(od.state),0) AS states,COUNT(od.id) AS counts FROM orderinfo oi " +
					"INNER JOIN order_details od ON oi.order_no=od.orderid AND oi.state=1 AND od.state<2 AND od.orderid='"+orderid+"'";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next() && rs.getInt("states") >0  && rs.getInt("states") == rs.getInt("counts")){
				sql="update orderinfo set  state=2 where order_no='"+orderid+"'";
				stmt=conn.prepareStatement(sql);
				stmt.executeUpdate();
				SendMQ.sendMsg(new RunSqlModel(sql));
			}

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return 0;
	}

	public double getEsPrice(String goodsid){
		String es_price="0.00";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try{
			String sql="SELECT od.goodsid,od.yourorder,IFNULL(a.wholesale_price,0) AS a_price FROM order_details od " +
					"LEFT JOIN custom_benchmark_ready a ON od.goods_pid=a.pid " +
					"WHERE od.goodsid="+goodsid+" AND od.state<2";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				es_price=rs.getString("a_price");
				if(es_price.indexOf(",")>-1) {
					String prices=es_price.split(",")[0].replace("[","").replace(" ","").trim();
					if(prices.indexOf("$") > -1){
						es_price =prices .split("\\$")[1];
					}else if(prices.indexOf("???") > -1){
						es_price =prices .split("???")[1];
					}
				}else if(es_price.indexOf("[")>-1){
					es_price=es_price.replace("[","").replace("]","").replace(" ","");
					if(es_price.indexOf("$") > -1){
						es_price =es_price .split("\\$")[1];
					}else if(es_price.indexOf("???") > -1){
						es_price =es_price .split("???")[1];
					}
				}
				if(es_price.indexOf("-")>-1){
					es_price=es_price.split("-")[0];
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closeConnection(conn);
		}
		return Double.parseDouble(es_price);
	}

	@Override
	public int getBuyId(String orderid, String goodsid) {
		int admuserid = 0;
		String sql = "SELECT admuserid FROM goods_distribution WHERE orderid='"
				+ orderid + "' AND goodsid='" + goodsid + "'";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				admuserid = rs.getInt("admuserid");
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
		return admuserid;
	}

	@Override
	public String getUserbyID(String admid) {
		String adminname = "";
		String sql = "select admname from admuser where id=? and status=1";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, admid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				adminname = rs.getString("admname");
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
		return adminname;
	}

	// ???????????????
	@Override
	public List<CountryCodeBean> getCountryCode() {
		List<CountryCodeBean> ccbList = new ArrayList<CountryCodeBean>();
		String sql = "select country_code,chinese_name,english_name from fpx_country_code order by english_name ";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CountryCodeBean ccb = new CountryCodeBean();
				ccb.setCountrycode(rs.getString("country_code"));
				ccb.setChinesename(rs.getString("chinese_name"));
				ccb.setEnglishname(rs.getString("english_name"));
				ccbList.add(ccb);
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
		return ccbList;
	}

	// ????????????
	@Override
	public List<ProductCodeBean> getProductCode() {
		List<ProductCodeBean> pcbList = new ArrayList<ProductCodeBean>();
		String sql = "select productcode,chinesename,englishname from fpx_product_code order by englishname ";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ProductCodeBean pcb = new ProductCodeBean();
				pcb.setProductcode(rs.getString("productcode"));
				pcb.setChinesename(rs.getString("chinesename"));
				pcb.setEnglishname(rs.getString("englishname"));
				pcbList.add(pcb);
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
		return pcbList;
	}

	// ????????????
	public List<ProductBean> getProduct(String str) {
		String[] ck = str.split(",");
		List<ProductBean> pbList = new ArrayList<ProductBean>();
		StringBuffer sb = new StringBuffer(
				"select od.goodsname,od.yourorder,od.goodsprice,oi.currency "
						+ "from order_details od left join orderinfo oi on oi.order_no=od.orderid where od.orderid in (");
		for (int i = 0; i < ck.length; i++) {
			if (i != ck.length - 1) {
				sb.append("?,");
			} else {
				sb.append("?");
			}
		}
		sb.append(") and od.state<>2");
		String sql = sb.toString();
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 1; i <= ck.length; i++) {
				stmt.setString(i, ck[i - 1]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				ProductBean pb = new ProductBean();
				pb.setProductname(rs.getString("goodsname"));
				pb.setProductnum(rs.getString("yourorder"));
				goodsnum = goodsnum
						+ Integer.parseInt(rs.getString("yourorder"));
				pb.setProductprice(rs.getString("goodsprice"));
				pb.setProductcurreny(rs.getString("currency"));
				pbList.add(pb);
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
		return pbList;
	}

	// ????????????????????????
	@Override
	public UserOrderDetails getUserDetails(String orderlist) {
		String[] ck = orderlist.split(",");
		String orderno = ck[0];
		String dropshipOrderNo = "";
		if (orderno.contains("_")) {
			dropshipOrderNo = orderno.substring(0, orderno.indexOf("_"));
		}
		UserOrderDetails uod = new UserOrderDetails();
		// String
		// sql="select (select sum(yourorder) from order_details where orderid=?
		// and state<>2) as goodsum,"
		// +
		// "id,name,email from user where id=(select user_id from orderinfo
		// where order_no=?) ";

		String sql = "select (select sum(yourorder) from order_details where (orderid=? or dropshipid =? )  and state<>2) as goodsum,"
				+ "id,name,email from user where id=(select DISTINCT user_id from orderinfo where (order_no=? or order_no = ?) ) ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmtaddr = null;
		ResultSet rsaddr = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderno);
			stmt.setString(2, orderno);
			stmt.setString(3, orderno);
			stmt.setString(4, dropshipOrderNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				// ???????????????
				uod.setUserid(rs.getString("id"));
				uod.setUserName(rs.getString("name"));
				uod.setEmail(rs.getString("email"));
				uod.setGoodsnum(rs.getString("goodsum"));

				// String
				// sqladdr="select
				// z.country,oa.statename,oa.address2,oa.address,oa.zipcode,oa.phoneNumber,
				// "
				// +
				// "oa.street from zone z,order_address oa where z.id=oa.country
				// and orderNo=? ";
				String sqladdr = "select oa.recipients,of.cargo_type,of.country_code,of.trans_details,of.weight,z.country,oa.statename,oa.address2,oa.address,oa.zipcode,oa.phoneNumber, oa.street from zone z,order_address oa,order_fee of where z.country=oa.country  and of.orderNo=oa.orderNo and of.orderNo=? ";
				stmtaddr = conn.prepareStatement(sqladdr);
				stmtaddr.setString(1, orderno);
				rsaddr = stmtaddr.executeQuery();
				if (rsaddr.next()) {
					if (StrUtils.isNotNullEmpty(rsaddr.getString("street"))) {
						uod.setUserstreet(rsaddr.getString("street") + ",");//
					}
					if (StrUtils.isNotNullEmpty(rsaddr.getString("address"))) {
						uod.setAddress(rsaddr.getString("address") + ",");// ????????????
					}
					if (StrUtils.isNotNullEmpty(rsaddr.getString("address2"))) {
						uod.setAddress2(rsaddr.getString("address2") + ",");// ???
					}
					if (StrUtils.isNotNullEmpty(rsaddr.getString("statename"))) {
						uod.setStatename(rsaddr.getString("statename") + ",");// ?????????
					}
					uod.setZipcode(rsaddr.getString("zipcode"));// ????????????
					uod.setZone(rsaddr.getString("country"));// ??????
					uod.setPhone(rsaddr.getString("phoneNumber"));// ??????
					uod.setWeight(rsaddr.getString("weight")); // ????????????
					uod.setTransMethod(rsaddr.getString("trans_details")); // ????????????
					uod.setCountryCode(rsaddr.getString("country_code"));
					uod.setCargoType(rsaddr.getString("cargo_type"));
					uod.setRecipients(rsaddr.getString("recipients"));
				}else{
					sqladdr = "select oa.recipients,of.cargo_type,of.country_code,of.trans_details,of.weight,z.country,oa.statename,oa.address2,oa.address,oa.zipcode,oa.phoneNumber, oa.street from zone z,order_address oa,order_fee of where z.id=oa.country  and of.orderNo=oa.orderNo and of.orderNo=? ";
					stmtaddr = conn.prepareStatement(sqladdr);
					stmtaddr.setString(1, orderno);
					rsaddr = stmtaddr.executeQuery();
					if (rsaddr.next()) {
						if (StrUtils.isNotNullEmpty(rsaddr.getString("street"))) {
							uod.setUserstreet(rsaddr.getString("street") + ",");//
						}
						if (StrUtils.isNotNullEmpty(rsaddr.getString("address"))) {
							uod.setAddress(rsaddr.getString("address") + ",");// ????????????
						}
						if (StrUtils.isNotNullEmpty(rsaddr.getString("address2"))) {
							uod.setAddress2(rsaddr.getString("address2") + ",");// ???
						}
						if (StrUtils.isNotNullEmpty(rsaddr.getString("statename"))) {
							uod.setStatename(rsaddr.getString("statename") + ",");// ?????????
						}
						uod.setZipcode(rsaddr.getString("zipcode"));// ????????????
						uod.setZone(rsaddr.getString("country"));// ??????
						uod.setPhone(rsaddr.getString("phoneNumber"));// ??????
						uod.setWeight(rsaddr.getString("weight")); // ????????????
						uod.setTransMethod(rsaddr.getString("trans_details")); // ????????????
						uod.setCountryCode(rsaddr.getString("country_code"));
						uod.setCargoType(rsaddr.getString("cargo_type"));
						uod.setRecipients(rsaddr.getString("recipients"));
					}
				}
				// ???????????????
				uod.setAdminaddress(AppConfig.adminaddress);
				uod.setAdmincode(AppConfig.admincode);
				uod.setAdmincompany(AppConfig.admincompany);
				uod.setAdminname(AppConfig.adminname);
				uod.setAdminzone(AppConfig.adminzone);
				uod.setAdminphone(AppConfig.adminphone);
				uod.setAdminprovince(AppConfig.adminprovince);
				uod.setAdmincity(AppConfig.admincity);

				// ????????????
				uod.setOrderno(orderno);
				uod.setProductCodeBean(this.getProductCode());
				uod.setCountryCodeBean(this.getCountryCode());
				uod.setProductBean(this.getProduct(orderlist));
				String gdsn = goodsnum + "";
				uod.setGoodsnum(gdsn);
				String mk = orderlist.replace(",", "<br/>");
				uod.setMark(mk);
				goodsnum = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rsaddr != null) {
				try {
					rsaddr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtaddr != null) {
				try {
					stmtaddr.close();
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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return uod;
	}

	// ?????????????????????????????????????????????//
	@Override
	public int findOrderService(String ordernolist) {
		int ttt = 0;
		String[] ck = ordernolist.split("<br/>");
		StringBuffer sb = new StringBuffer("select count(id) as sum from forwarder where order_no in (");
		for (int i = 0; i < ck.length; i++) {
			if (i != ck.length - 1) {
				sb.append("?,");
			} else {
				sb.append("?");
			}
		}
		sb.append(")");
		String sql = sb.toString();
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 1; i <= ck.length; i++) {
				stmt.setString(i, ck[i - 1]);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				ttt = rs.getInt("sum");
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
		return ttt;
	}

	@Override
	public int YiRuKu(String url, String orderno, int goodid) {
		int rk = 0;
		String sql = "update order_product_source set purchase_state=4 where goods_url=? and orderid=? and goodsid=?";
		String sqlorder = "update order_details set state=1 where orderid=? and goodsid=?";
		String sqlorderinfo = "select state from order_details where orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();

		PreparedStatement stmt = null;
		PreparedStatement stmtorder = null;
		PreparedStatement stmtorder2 = null;
		PreparedStatement stmtorderinfo = null;
		PreparedStatement stmtoi = null, stmtoi2 = null;
		ResultSet rsorderinfo = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			stmt.setString(2, orderno);
			stmt.setInt(3, goodid);
			stmt.executeUpdate();
			rk = 1;
			stmtorder = conn.prepareStatement(sqlorder);
			stmtorder.setString(1, orderno);
			stmtorder.setInt(2, goodid);
			stmtorder.executeUpdate();

			/*stmtorder2 = conn2.prepareStatement(sqlorder);
			stmtorder2.setString(1, orderno);
			stmtorder2.setInt(2, goodid);
			stmtorder2.executeUpdate();*/
			List<String> lstValues = Lists.newArrayList();
			lstValues.add(orderno);
			lstValues.add(String.valueOf(goodid));
			String runSql = DBHelper.covertToSQL(sqlorder, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));

			stmtorderinfo = conn.prepareStatement(sqlorderinfo);
			stmtorderinfo.setString(1, orderno);
			rsorderinfo = stmtorderinfo.executeQuery();
			int i = 0;
			while (rsorderinfo.next()) {
				int state = rsorderinfo.getInt("state");
				if (state == 0)
					i++;
			}
			if (i == 0) {
				String sqloi = "update orderinfo set state=2 where order_no=?";
				stmtoi = conn.prepareStatement(sqloi);
				stmtoi.setString(1, orderno);
				stmtoi.executeUpdate();

				// ????????????2???
				/*stmtoi2 = conn2.prepareStatement(sqloi);
				stmtoi2.setString(1, orderno);
				stmtoi2.executeUpdate();*/
				lstValues = Lists.newArrayList();
				lstValues.add(orderno);
				runSql = DBHelper.covertToSQL(sqloi, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmtoi != null) {
				try {
					stmtoi.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rsorderinfo != null) {
				try {
					rsorderinfo.close();
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
			if (stmtorder2 != null) {
				try {
					stmtorder2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtorder != null) {
				try {
					stmtorder.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtorderinfo != null) {
				try {
					stmtorderinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rk;
	}

	@Override
	public void ProblemGoods(String url, String orderno, int goodid) {
		String sql = "update order_product_source set purchase_state=6 where goods_url=? and orderid=? and goodsid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			stmt.setString(2, orderno);
			stmt.setInt(3, goodid);
			stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	// ???????????????????????????
	public String getYFHTurnOrder(String yfhOrder) {
		String json = this
				.getOrderMsg("http://www.yfhex.com/ServicePlatform/track?num="
						+ yfhOrder);
		JSONArray jsonArr = JSONArray.parseArray(json);
		String a[] = new String[jsonArr.size()];
		String b[] = new String[jsonArr.size()];
		for (int i = 0; i < jsonArr.size(); i++) {
			a[i] = jsonArr.getJSONObject(i).getString("SecondBillID");
			b[i] = jsonArr.getJSONObject(i).getString("URLAddress");
		}
		for (int i = 0; i < a.length; i++) {
			a[i] = jsonArr.getJSONObject(i).getString("SecondBillID");
		}
		String shipurl = "";
		for (int i = 0; i < b.length; i++) {
			if (b[i].length() != 0) {
				shipurl = b[i];
			}
		}
		String yfhTurnOrder = "";
		if (a.length != 0) {
			yfhTurnOrder = a[0];
		}
		String yfhshipname = "";
		if (shipurl.length() > 7) {
			yfhshipname = shipurl.substring(4, 7) + "en";
		}
		if (yfhTurnOrder.length() == 0) {

		} else {
			String sql = "update forwarder set express_no=?,logistics_name=? where yfhorder=?";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, yfhTurnOrder);
				stmt.setString(2, yfhshipname);
				stmt.setString(3, yfhOrder);
				stmt.executeUpdate();
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
				DBHelper.getInstance().closeConnection(conn);
			}
		}
		return yfhTurnOrder;
	}

	public String getOrderMsg(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL reurl = new URL(url);
			URLConnection connection = reurl.openConnection();
			// ???????????????????????????
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ?????????????????????
			connection.connect();
			// ?????? BufferedReader??????????????????URL?????????
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in = null;
			}
		}
		return result;
	}

	// ?????????????????? ??????codemaster??????
	@Override
	public List<CodeMaster> getCodeMaster() {
		List<CodeMaster> cmlist = new ArrayList<CodeMaster>();
		String sql = "select code_id,code_name from codemaster where code_type='001' and delete_flg=0";
		Connection con = DBHelper.getInstance().getConnection();
		PreparedStatement ppst = null;
		ResultSet rs = null;
		try {
			ppst = con.prepareStatement(sql);
			rs = ppst.executeQuery();
			while (rs.next()) {
				CodeMaster cm = new CodeMaster();
				cm.setCodeId(rs.getString("code_id"));
				cm.setCodeName(rs.getString("code_name"));
				cmlist.add(cm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmlist;
	}

	// ????????????????????????2015-12-30
	@Override
	public OrderPayDetails getDetailsByOrder(String adminname, String uid,
                                             String orderno) {// ??????orderinfo???

		OrderPayDetails orderPayDetail = new OrderPayDetails();
		String sql = "SELECT u.available_m,u.applicable_credit,u.currency as ucurrency,ob.buyuser,"
				+ "(select ifnull(min(createtime),oi.create_time) from payment pm where pm.orderid="
				+ "oi.order_no and pm.paystatus=1) as paytime,adm.admName as aruadminid,"
				+ "oi.order_no,oi.user_id,oi.address_id,oi.delivery_time,oi.mode_transport,oi.service_fee,"
				+ "oi.product_cost,oi.domestic_freight,oi.foreign_freight,oi.actual_allincost,oi.pay_price,"
				+ "oi.pay_price_tow,oi.pay_price_three,oi.actual_ffreight,oi.remaining_price,oi.actual_volume,"
				+ "oi.actual_weight,oi.transport_time,oi.cancel_obj,oi.expect_arrive_time,oi.create_time,"
				+ "oi.currency,oi.discount_amount,oi.actual_lwh,oi.actual_weight_estimate,oi.actual_freight_c "
				+ "FROM orderinfo oi left join user u on u.id=oi.user_id left join order_buy ob on ob.orderid="
				+ "oi.order_no left join admin_r_user aru on oi.user_id=aru.userid left join admuser adm on "
				+ "aru.adminid=adm.id where 1=1 ";

		String sql2 = " select *,((select group_concat(orderno) orderarr from order_fee  where mergeOrders= of.mergeOrders)) orderarr from order_fee of where orderno= '"
				+ orderno + "' ";
		if (adminname != null && !adminname.equals("")) {
			sql = sql + " and ob.buyuser=" + adminname;
		}
		if (uid != null && !uid.equals("")) {
			sql = sql + " and oi.user_id=" + Integer.valueOf(uid);
		}
		if (orderno != null && !orderno.equals("")) {
			sql = sql + " and oi.order_no='" + orderno + "'";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;

		Connection conn2 = DBHelper.getInstance().getConnection2();
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			stmt2 = conn2.prepareStatement(sql2);
			rs2 = stmt2.executeQuery();
			if (rs.next()) {

				orderPayDetail.setOrder_no(orderno);// ?????????
				orderPayDetail.setAvailable_m(rs.getString("available_m"));// ????????????
				orderPayDetail.setApplicable_credit(rs
						.getString("applicable_credit"));// ??????????????????
				orderPayDetail.setUcurrency(rs.getString("ucurrency"));// ??????????????????
				orderPayDetail.setBuyuser(rs.getString("buyuser"));// ???????????????
				orderPayDetail.setUser_id(rs.getString("user_id"));// ??????ID
				orderPayDetail.setDelivery_time(rs.getString("delivery_time"));// ????????????
				String addres = this.getAddressByOrderID(orderno);
				orderPayDetail.setAddress_id(addres);// ????????????
				String[] cty = addres.split(",");
				orderPayDetail.setCountry(cty[cty.length - 1]);
				orderPayDetail.setDelivery_time(rs.getString("delivery_time"));// ??????????????????
				orderPayDetail
						.setMode_transport(rs.getString("mode_transport"));// ????????????
				String mode_transport = "";
				if (rs.getString("mode_transport") != null
						&& !rs.getString("mode_transport").equals("")) {
					String[] mode_transports = rs.getString("mode_transport")
							.split("@");
					if (mode_transports.length > 3) {
						if (mode_transports[mode_transports.length - 4] != null) {
							mode_transport = mode_transports[mode_transports.length - 4];
						}
					}
				}
				orderPayDetail.setDelivery_time_abroad(mode_transport);// ??????????????????
				orderPayDetail.setService_fee(rs.getDouble("service_fee"));// ????????????
				orderPayDetail.setProduct_cost(rs.getDouble("product_cost"));// ????????????
				orderPayDetail.setDomestic_freight(rs
						.getString("domestic_freight"));// ????????????
				orderPayDetail.setForeign_freight(rs
						.getString("foreign_freight"));// ????????????
				orderPayDetail.setActual_allincost(rs
						.getString("actual_allincost"));// ???????????????
				orderPayDetail.setPay_price(rs.getDouble("pay_price"));// ????????????????????????????????????
				orderPayDetail.setPay_price_tow(rs.getDouble("pay_price_tow"));// ?????????????????????
				orderPayDetail.setPay_price_three(rs
						.getString("pay_price_three"));// ??????????????????
				orderPayDetail.setActual_ffreight(rs
						.getString("actual_ffreight"));// ??????????????????
				orderPayDetail.setRemaining_price(rs
						.getString("remaining_price"));// ?????????????????????????????????-|?????????????????????
				orderPayDetail.setActual_volume(rs.getString("actual_volume"));// ???????????????
				orderPayDetail.setActual_weight(rs.getString("actual_weight"));// ????????????
				orderPayDetail
						.setTransport_time(rs.getString("transport_time"));// ??????????????????
				orderPayDetail.setCancel_obj(rs.getString("cancel_obj"));// ??????????????????(0-?????????1-??????)
				orderPayDetail.setExpect_arrive_time(rs
						.getString("expect_arrive_time"));// ??????????????????
				String str = "";
				if (!"".equals((rs.getString("paytime")))
						&& rs.getString("paytime") != null) {
					str = (rs.getString("paytime")).substring(0, 19);
				}
				orderPayDetail.setCreate_time(str);// ????????????
				orderPayDetail.setCurrency(rs.getString("currency"));// ????????????
				orderPayDetail.setDiscount_amount(rs
						.getString("discount_amount"));// ????????????
				orderPayDetail.setActual_lwh(rs.getString("actual_lwh"));// ????????????
				orderPayDetail.setActual_weight_estimate(rs
						.getString("actual_weight_estimate"));// ????????????
				orderPayDetail.setActual_freight_c(rs
						.getString("actual_freight_c"));// ????????????????????????

				// orrder??????fee
				if (rs2.next()) {
					orderPayDetail.setOfweight(rs2.getString("weight"));
					orderPayDetail.setOfvolume_lwh(rs2.getString("volume_lwh"));
					orderPayDetail.setOfcountry_code(rs2
							.getString("country_code"));
					orderPayDetail.setOrderarr(rs2.getString("orderarr"));

					orderPayDetail.setOfdelivery_time(rs2
							.getString("delivery_time"));
					orderPayDetail.setOfacture_get_fee(rs2
							.getString("acture_get_fee"));
					orderPayDetail.setOfacture_fee(rs2.getString("acture_fee"));
					orderPayDetail.setOftrans_method(rs2
							.getString("trans_method"));
					orderPayDetail.setOftrans_details(rs2
							.getString("trans_details"));
					orderPayDetail.setOfcargo_type(rs2.getString("cargo_type"));
					orderPayDetail.setOfunpay(rs2.getString("unpay"));
					orderPayDetail.setOforder_area(rs2.getString("order_area")); //
					orderPayDetail.setOfyfhNum(rs2.getString("yfhNum"));

				}
				// private String ofweight;
				// private String ofvolume_lwh;
				// private String ofcountry_code;
				// private String orderarr;
				// private String ofdelivery_time; // ???????????????????????????
				// private String ofacture_get_fee; //?????????
				// private String ofacture_fee; // ????????????
				// private String oftrans_method; // ???????????? 4px ?????? ?????????
				// private String oftrans_details; // ??????????????????
				// private String ofcargo_type; // ????????????

				if (rs.getString("aruadminid") == null
						|| rs.getString("aruadminid").equals("")) { // ???????????????
					orderPayDetail.setAdmname("????????????");
				} else {
					orderPayDetail.setAdmname(rs.getString("aruadminid"));
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
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn2);
		}
		return orderPayDetail;
	}

	public void updateOrderInfo(Connection conn, PreparedStatement stmt,
	                            OrderFeeDetails ofd) {
		String sql = "update orderinfo set actual_ffreight=?,remaining_price=?,actual_volume=?,actual_weight=?,order_ac=? where order_no=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, ofd.getActcgetfee() + ofd.getPackagefee());
			stmt.setDouble(2, ofd.getFeecount());
			stmt.setString(3, ofd.getVolume());
			stmt.setDouble(4, ofd.getWeight());
			stmt.setDouble(5, ofd.getDeduction());
			stmt.setString(6, ofd.getOrder());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int saveOrderFee(OrderFeeDetails ofd) {
		int i = 0;
		// String sqlOne =
		// "SELECT count(id) as id from order_fee where orderno=?";
		String sqlOne = "SELECT count(id) as id from order_fee where mergeOrders=?";

		String sqlTwo = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmtOne = null;
		ResultSet rsOne = null;
		PreparedStatement stmtTwo = null;
		PreparedStatement stmtuser = null;
		try {
			stmtOne = conn.prepareStatement(sqlOne);
			stmtOne.setString(1, ofd.getOrder());
			rsOne = stmtOne.executeQuery();
			int records = 0;
			if (rsOne.next()) {
				records = rsOne.getInt("id");
			}
			int state = 1;
			if (records == 0) {
				if (ofd.getFeecount() > 3) { // ?????????????????????????????????3
					state = 0; // ????????????
				}
				sqlTwo = "INSERT INTO order_fee (order_fee.userid,order_fee.orderno,order_fee.delivery_time,order_fee.currency,"
						+ "order_fee.weight,order_fee.volume_lwh,order_fee.acture_fee,order_fee.acture_get_fee,"
						+ "order_fee.unpay,order_fee.trans_method,order_fee.trans_details,order_fee.order_country,order_fee.order_area,"
						+ "order_fee.admin,order_fee.package_fee,order_fee.state,order_fee.create_time,order_fee.country_code,order_fee.cargo_type) values(?,'"
						+ ofd.getOrder()
						+ "',?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ state
						+ ",now(),'"
						+ ofd.getZone()
						+ "','"
						+ ofd.getCargoType() + "') ";
			} else {
				if (ofd.getFeecount() > 3) {// ?????????????????????????????????3
					state = 0; // ????????????
				}
				// ??????where mergeOrders=???
				sqlTwo = "UPDATE order_fee set order_fee.yfhNum = '"
						+ ofd.getYfhNum()
						+ "' ,order_fee.userid=?,order_fee.delivery_time=?,order_fee.currency=?,"
						+ "order_fee.weight=?,order_fee.volume_lwh=?,order_fee.acture_fee=?,order_fee.acture_get_fee=?,order_fee.unpay=?,"
						+ "order_fee.trans_method=?,order_fee.trans_details=?,order_fee.order_country=?,order_fee.order_area=?,"
						+ "order_fee.update_admin=?,order_fee.package_fee=?,order_fee.state="
						+ state + ",order_fee.update_time=now(),"
						+ "order_fee.country_code='" + ofd.getZone()
						+ "',order_fee.cargo_type='" + ofd.getCargoType()
						+ "' where mergeOrders='" + ofd.getOrder() + "' ";
			}
			stmtTwo = conn.prepareStatement(sqlTwo);
			stmtTwo.setInt(1, ofd.getUid());
			if (ofd.getDelivery_time().length() > 2) {
				ofd.setDelivery_time(ofd.getDelivery_time().substring(0,
						ofd.getDelivery_time().length() - 1));
			}

			stmtTwo.setString(2, ofd.getDelivery_time());
			stmtTwo.setString(3, ofd.getCurrency());
			stmtTwo.setDouble(4, ofd.getWeight());
			stmtTwo.setString(5, ofd.getVolume());
			stmtTwo.setDouble(6, ofd.getActfee());
			stmtTwo.setDouble(7, ofd.getActcgetfee());
			stmtTwo.setDouble(8, ofd.getFeecount());
			stmtTwo.setString(
					9,
					(ofd.getTransport().equals("nul") ? "" : (ofd
							.getTransport())));
			stmtTwo.setString(10,
					(ofd.getTrans().equals("nul") ? "" : (ofd.getTrans())));
			stmtTwo.setString(11, ofd.getCountry());
			stmtTwo.setString(12, (ofd.getOrder_area().equals("nul") ? ""
					: (ofd.getOrder_area())));// order_area
			stmtTwo.setString(13, ofd.getAdmin());
			stmtTwo.setDouble(14, ofd.getPackagefee());
			stmtTwo.executeUpdate();
			this.updateOrderInfo(conn, stmtTwo, ofd);
			if (ofd.getIdts() == 2) { // ???????????????????????????
				String sqluser = "update user set applicable_credit=? where id=?";
				stmtuser = conn.prepareStatement(sqluser);
				stmtuser.setDouble(1, ofd.getApp_credit());
				stmtuser.setInt(2, ofd.getUid());
				stmtuser.executeUpdate();
			}
			i++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rsOne != null) {
				try {
					rsOne.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtOne != null) {
				try {
					stmtOne.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtTwo != null) {
				try {
					stmtTwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtuser != null) {
				try {
					stmtuser.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	@Override
	public String getOriginalGoodsUrl(String orderno, int goodsid) {
		String original = "";
		String sql = "select aliurl from changegooddata where orderno like '%"
				+ orderno + "%' and goodid like '%" + goodsid + "%' LIMIT 1 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			// ????????????????????? ??????_1
			// if (orderno.indexOf("_1") != -1) {
			// orderno = orderno.substring(0, orderno.indexOf("_1"));
			// }
			//
			// stmt.setString(1, orderno);
			// stmt.setInt(2, goodsid);

			rs = stmt.executeQuery();
			if (rs.next()) {
				original = rs.getString("aliurl");
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
		return original;
	}

	@Override
	public String allcgqrQrNew(final String orderNo,final int id) {
		String datas = "";
		StringBuffer bf = new StringBuffer();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String isDropshipOrder = "";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final List<String> list1=new ArrayList<String>();
		int row = 0;
		// ??????????????????
		try {
			String sql = "SELECT isDropshipOrder FROM orderinfo WHERE order_no='"
					+ orderNo + "'";
			stmt = conn.prepareStatement(sql);
			rs1 = stmt.executeQuery();
			if (rs1.next()) {
				isDropshipOrder = rs1.getString("isDropshipOrder");// ????????????????????????Dropship??????
			}
			sql = "SELECT distinct ops.goodsid,ops.goods_p_url,ops.last_goods_p_url FROM order_product_source ops INNER JOIN goods_distribution gd ON ops.od_id=gd.odid WHERE ops.od_id=gd.odid AND ops.orderid='"
					+ orderNo
					+ "' AND ops.purchase_state=1 AND gd.admuserid='"
					+ id + "' AND ops.del=0";
			stmt1 = conn.prepareStatement(sql);
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				list1.add(rs1.getString("goodsid"));
				// ????????????LOG????????????
				String sql1 = "select id from status_change_log where order_no='"
						+ orderNo
						+ "' and goodsid='"
						+ rs1.getString("goodsid") + "'";
				stmt2 = conn.prepareStatement(sql1);
				if (stmt2.executeQuery().next()) {
					sql1 = "update status_change_log set opStatus='????????????',opTime=now(),opName='"
							+ id
							+ "' where order_no='"
							+ orderNo
							+ "' and goodsid='"
							+ rs1.getString("goodsid")
							+ "'";
				} else {
					sql1 = "insert into status_change_log (opName,opTime,opStatus,order_no,goodsid) values ('"
							+ id
							+ "',now(),'????????????','"
							+ orderNo
							+ "','"
							+ rs1.getString("goodsid") + "')";
				}
				stmt2 = conn.prepareStatement(sql1);
				stmt2.executeUpdate();
				sql = "UPDATE order_product_source  SET purchase_state=3,confirm_userid="
						+ id
						+ ",confirm_time=now(),purchasetime=now() WHERE goodsid='"
						+ rs1.getString("goodsid")
						+ "' and orderid='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				sql = "UPDATE order_details  SET purchase_state=3,purchase_time=now() WHERE goodsid='"
						+ rs1.getString("goodsid")
						+ "' and orderid='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
//				stmt = conn2.prepareStatement(sql);
//				row = stmt.executeUpdate();

				sql = "update orderinfo set purchase_number = (select count(*) from order_product_source where orderid='"
						+ orderNo
						+ "' and purchase_state in(3,4)) where order_no='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				if (isDropshipOrder.equals("1")) {
					sql = "SELECT dropshipid FROM order_details WHERE orderid='"
							+ orderNo + "'";
					stmt = conn.prepareStatement(sql);
					rs = stmt.executeQuery();
					String dropshipid = "";
					if (rs.next()) {
						dropshipid = rs.getString("dropshipid");
					}
					sql = "update dropshiporder set state= (SELECT IF ((select count(*) from ( select * from order_details where dropshipid='"
							+ dropshipid
							+ "' and state!=2 AND purchase_state=3) a)>0,'1','5')) where child_order_no='"
							+ dropshipid + "'";
					stmt = conn.prepareStatement(sql);
					row = stmt.executeUpdate();
//					stmt = conn2.prepareStatement(sql);
//					row = stmt.executeUpdate();
				}
				sql = "update orderinfo set state= (SELECT IF ((select count(*) from ( select * from order_details where orderid='"
						+ orderNo
						+ "' and state!=2 AND purchase_state=3) a)>0,'1','5')) where order_no='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
//				sql = "SELECT IF ((select count(*) from ( select * from order_details where orderid='"
//						+ orderNo
//						+ "' and state!=2 AND purchase_state=3) a)>0,'1','5') as counts ";
//				stmt = conn.prepareStatement(sql);
//				rs = stmt.executeQuery();
//				int state = 0;
//				if (rs.next()) {
//					state = rs.getInt("counts");
//				}
//				sql = "update orderinfo set state = '" + state
//						+ "' where order_no='" + orderNo + "'";
//				stmt = conn2.prepareStatement(sql);
//				row = stmt.executeUpdate();
				bf.append(orderNo).append(";").append(rs1.getString("goodsid"))
						.append(";").append(sdf.format(date)).append("&");
			}
			datas = bf.toString().length() > 0 ? bf.toString().substring(0,bf.toString().length() - 1) : "";
			new Thread(){
				public void run() {
					Connection conn2 = DBHelper.getInstance().getConnection2();
					ResultSet rs1 = null;
					PreparedStatement stmt = null;
					try{
						String sql = "SELECT isDropshipOrder,user_id FROM orderinfo WHERE order_no='"
								+ orderNo + "'";
						stmt = conn2.prepareStatement(sql);
						rs1 = stmt.executeQuery();
						String isDropshipOrder="0";
						int userId = 0;
						if (rs1.next()) {
							isDropshipOrder = rs1.getString("isDropshipOrder");// ????????????????????????Dropship??????
							userId = rs1.getInt("user_id");
						}
						for(int i=0;i<list1.size();i++){
							String goodsid=list1.get(i).toString();
							sql = "UPDATE order_details  SET purchase_state=3,purchase_time=now() WHERE goodsid='"
									+ goodsid
									+ "' and orderid='"
									+ orderNo + "'";
							/*stmt = conn2.prepareStatement(sql);
							stmt.executeUpdate();*/
							//??????MQ????????????
							NotifyToCustomerUtil.sendSqlByMq(sql);

							if (isDropshipOrder.equals("1")) {
								sql = "SELECT dropshipid FROM order_details WHERE orderid='"
										+ orderNo + "'";
								stmt = conn2.prepareStatement(sql);
								rs1 = stmt.executeQuery();
								String dropshipid = "";
								if (rs1.next()) {
									dropshipid = rs1.getString("dropshipid");
								}
								sql = "update dropshiporder set state= (SELECT IF ((select count(*) from ( select * from order_details where dropshipid='"
										+ dropshipid
										+ "' and state!=2 AND purchase_state=3) a)>0,'1','5')) where child_order_no='"
										+ dropshipid + "'";
								/*stmt = conn2.prepareStatement(sql);
								stmt.executeUpdate();*/
								SendMQ.sendMsg(new RunSqlModel(sql));
							}
							sql = "SELECT IF ((select count(*) from ( select * from order_details where orderid='"
									+ orderNo
									+ "' and state!=2 AND purchase_state=3) a)>0,'1','5') as counts," +
									"(select state from orderinfo where order_no = '" + orderNo + "') as old_state  ";
							stmt = conn2.prepareStatement(sql);
							rs1 = stmt.executeQuery();
							int state = 0;
							int oldState = -2;
							if (rs1.next()) {
								state = rs1.getInt("counts");
								oldState =rs1.getInt("old_state");
							}
							sql = "update orderinfo set state = '" + state + "' where order_no='" + orderNo + "'";
							/*stmt = conn2.prepareStatement(sql);
							stmt.executeUpdate();*/
							//??????MQ????????????
							NotifyToCustomerUtil.sendSqlByMq(sql);
							//???????????????????????????????????????
							if(oldState != state){
								NotifyToCustomerUtil.updateOrderState(userId,orderNo,oldState,state);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if (stmt != null) {
							try {
								stmt.close();
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
						DBHelper.getInstance().closeConnection(conn2);
					}
				};
			}.start();
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
//			DBHelper.getInstance().closeConnection(conn2);
		}
		return datas;
	}

	@Override
	public String allQxcgQrNew(final String orderNo,final int id) {
		long start=System.currentTimeMillis();
		String datas = "";
		StringBuffer bf = new StringBuffer();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		boolean flag = false;
		String isDropshipOrder = "";
		final List<String> list_goodsid=new ArrayList<String>();
		int row = 0;
		// ??????????????????
		try {
			String sql = "SELECT isDropshipOrder FROM orderinfo WHERE order_no='"+ orderNo + "'";
			stmt = conn.prepareStatement(sql);
			rs1 = stmt.executeQuery();
			if (rs1.next()) {
				isDropshipOrder = rs1.getString("isDropshipOrder");// ????????????????????????Dropship??????
			}
			sql = "SELECT distinct ops.goodsid FROM order_product_source ops INNER JOIN goods_distribution gd ON ops.od_id=gd.odid WHERE ops.od_id=gd.odid AND ops.orderid='"
					+ orderNo
					+ "' AND ops.purchase_state=3 AND gd.admuserid='"
					+ id + "' AND ops.del=0";
			stmt1 = conn.prepareStatement(sql);
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				list_goodsid.add(rs1.getString("goodsid"));
				// ????????????LOG????????????
				String sql1 = "select id from status_change_log where order_no='"+ orderNo+ "' and goodsid='"+ rs1.getString("goodsid") + "'";
				stmt2 = conn.prepareStatement(sql1);
				if (stmt2.executeQuery().next()) {
					sql1 = "update status_change_log set opStatus='????????????',opTime=now(),opName='"
							+ id
							+ "' where order_no='"
							+ orderNo
							+ "' and goodsid='"
							+ rs1.getString("goodsid")
							+ "'";
				} else {
					sql1 = "insert into status_change_log (opName,opTime,opStatus,order_no,goodsid) values ('"
							+ id
							+ "',now(),'????????????','"
							+ orderNo
							+ "','"
							+ rs1.getString("goodsid") + "')";
				}
				stmt2 = conn.prepareStatement(sql1);
				stmt2.executeUpdate();
				sql = "UPDATE order_product_source   SET purchase_state=1,purchasetime='' WHERE goodsid='"
						+ rs1.getString("goodsid")
						+ "' and orderid='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				sql = "UPDATE order_details  SET purchase_state=1,purchase_time=null WHERE goodsid='"
						+ rs1.getString("goodsid")
						+ "' and orderid='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
//				stmt = conn2.prepareStatement(sql);
//				row = stmt.executeUpdate();
				sql = "update orderinfo set purchase_number = (select count(*) from order_product_source where orderid='"
						+ orderNo
						+ "' and purchase_state in(3,4))where order_no='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				if (isDropshipOrder.equals("1")) {
					sql = "SELECT dropshipid FROM order_details WHERE orderid='"
							+ orderNo + "'";
					stmt = conn.prepareStatement(sql);
					rs = stmt.executeQuery();
					String dropshipid = "";
					if (rs.next()) {
						dropshipid = rs.getString("dropshipid");
					}
					sql = "update dropshiporder set state= (SELECT IF ((select count(*) from ( select * from order_details where dropshipid='"
							+ dropshipid
							+ "' and state!=2 AND purchase_state=3) a)>0,'1','5')) where child_order_no='"
							+ dropshipid + "'";
					stmt = conn.prepareStatement(sql);
					row = stmt.executeUpdate();
//					stmt = conn2.prepareStatement(sql);
//					row = stmt.executeUpdate();
				}
				sql = "update orderinfo set state= (SELECT IF ((select count(*) from ( select * from order_details where orderid='"
						+ orderNo
						+ "' and state!=2 AND purchase_state=3) a)>0,'1','5')) where order_no='"
						+ orderNo + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
//				sql = "SELECT IF ((select count(*) from ( select * from order_details where orderid='"
//						+ orderNo
//						+ "' and state!=2 AND purchase_state=3) a)>0,'1','5') as counts ";
//				stmt = conn.prepareStatement(sql);
//				rs = stmt.executeQuery();
//				int state = 0;
//				if (rs.next()) {
//					state = rs.getInt("counts");
//				}
//				sql = "update orderinfo set state = '" + state
//						+ "' where order_no='" + orderNo + "'";
//				stmt = conn2.prepareStatement(sql);
//				row = stmt.executeUpdate();
				bf.append(orderNo).append(";").append(rs1.getString("goodsid")).append("&");
			}
			datas = bf.toString().length() > 0 ? bf.toString().substring(0,bf.toString().length() - 1) : "";
			new Thread(){
				public void run() {
					PreparedStatement stmt = null;
					Connection conn2 = DBHelper.getInstance().getConnection2();
					ResultSet rs1 = null;
					try {
						String isDropshipOrder="0";
						String sql = "SELECT isDropshipOrder,user_id FROM orderinfo WHERE order_no='"+ orderNo + "'";
						stmt = conn2.prepareStatement(sql);
						rs1 = stmt.executeQuery();
						int userId = 0;
						if (rs1.next()) {
							isDropshipOrder = rs1.getString("isDropshipOrder");// ????????????????????????Dropship??????
							userId = rs1.getInt("user_id");
						}
						for(int i=0;i<list_goodsid.size();i++){
							String goodsid=list_goodsid.get(i).toString();
							sql = "UPDATE order_details  SET purchase_state=1,purchase_time=null WHERE goodsid='"
									+ goodsid
									+ "' and orderid='"
									+ orderNo + "'";
							/*stmt = conn2.prepareStatement(sql);
							stmt.executeUpdate();*/
							SendMQ.sendMsg(new RunSqlModel(sql));
							if (isDropshipOrder.equals("1")) {
								sql = "SELECT dropshipid FROM order_details WHERE orderid='"
										+ orderNo + "'";
								stmt = conn2.prepareStatement(sql);
								rs1 = stmt.executeQuery();
								String dropshipid = "";
								if (rs1.next()) {
									dropshipid = rs1.getString("dropshipid");
								}
								sql = "update dropshiporder set state= (SELECT IF ((select count(0) from ( " +
										"select id,userid,goodscatid,orderid from order_details where dropshipid='"
										+ dropshipid
										+ "' and state!=2 AND purchase_state=3) a)>0,'1','5')) where child_order_no='"
										+ dropshipid + "'";
								/*stmt = conn2.prepareStatement(sql);
								stmt.executeUpdate();*/
								//??????MQ????????????
								NotifyToCustomerUtil.sendSqlByMq(sql);
							}
							sql = "SELECT IF ((select count(0) from ( select id,userid,goodscatid,orderid from order_details where orderid='"
									+ orderNo
									+ "' and state!=2 AND purchase_state=3) a)>0,'1','5') as counts," +
									"(select state from orderinfo where order_no = '" + orderNo + "') as old_state ";
							stmt = conn2.prepareStatement(sql);
							rs1 = stmt.executeQuery();
							int state = 0;
							int oldState = -2;
							if (rs1.next()) {
								state = rs1.getInt("counts");
								oldState =rs1.getInt("old_state");
							}
							sql = "update orderinfo set state = '" + state + "' where order_no='" + orderNo + "'";
							/*stmt = conn2.prepareStatement(sql);
							stmt.executeUpdate();*/
							//??????MQ????????????
							NotifyToCustomerUtil.sendSqlByMq(sql);
							//???????????????????????????????????????
							if(oldState != state){
								NotifyToCustomerUtil.updateOrderState(userId,orderNo,oldState,state);
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						if (stmt != null) {
							try {
								stmt.close();
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
						DBHelper.getInstance().closeConnection(conn2);
					}
				};
			}.start();
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
//			DBHelper.getInstance().closeConnection(conn2);
		}
		System.out.println("????????????="+(System.currentTimeMillis()-start));
		return datas;
	}

	@Override
	public String allQxQrNew(final String orderNo, int id) {
		String datas = "";
		StringBuffer bf = new StringBuffer();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		final List<String> list1=new ArrayList<String>();
		int row = 0;
		// ??????????????????
		String sql = "SELECT distinct ops.goodsid FROM order_product_source ops INNER JOIN goods_distribution gd ON ops.od_id=gd.odid WHERE ops.od_id=gd.odid AND ops.orderid='"
				+ orderNo
				+ "' AND ops.purchase_state=1 AND gd.admuserid='"
				+ id + "' AND ops.del=0";
		try {
			stmt1 = conn.prepareStatement(sql);
			rs = stmt1.executeQuery();
			while (rs.next()) {
				list1.add(rs.getString("goodsid"));
				// ????????????LOG????????????
				String sql1 = "select id from status_change_log where order_no='"
						+ orderNo
						+ "' and goodsid='"
						+ rs.getString("goodsid")
						+ "'";
				stmt2 = conn.prepareStatement(sql1);
				if (stmt2.executeQuery().next()) {
					sql1 = "update status_change_log set opStatus='????????????',opTime=now(),opName='"
							+ id
							+ "' where order_no='"
							+ orderNo
							+ "' and goodsid='" + rs.getString("goodsid") + "'";
				} else {
					sql1 = "insert into status_change_log (opName,opTime,opStatus,order_no,goodsid) values ('"
							+ id
							+ "',now(),'????????????','"
							+ orderNo
							+ "','"
							+ rs.getString("goodsid") + "')";
				}
				stmt2 = conn.prepareStatement(sql1);
				stmt2.executeUpdate();
				// flag=true;
				sql = "UPDATE order_product_source  SET purchase_state=0,confirm_userid=null,confirm_time=null WHERE goodsid='"
						+ rs.getString("goodsid")
						+ "' and orderid='"
						+ orderNo
						+ "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				sql = "UPDATE order_details  SET purchase_state=0 WHERE goodsid='"
						+ rs.getString("goodsid")
						+ "' and orderid='"
						+ orderNo
						+ "'";
				stmt = conn.prepareStatement(sql);
//				row = stmt.executeUpdate();
//				stmt = conn2.prepareStatement(sql);
				row = stmt.executeUpdate();
				bf.append(orderNo).append(";").append(rs.getString("goodsid"))
						.append("&");
			}
			datas = bf.toString().length() > 0 ? bf.toString().substring(0,bf.toString().length() - 1) : "";
			new Thread(){
				public void run() {
					PreparedStatement stmt = null;
					try{
						for(int i=0;i<list1.size();i++){
							String goodsid=list1.get(i).toString();
							String sql = "UPDATE order_details  SET purchase_state=0 WHERE goodsid='"
									+ goodsid
									+ "' and orderid='"
									+ orderNo
									+ "'";
							/*stmt = conn2.prepareStatement(sql);
							stmt.executeUpdate();*/
							SendMQ.sendMsg(new RunSqlModel(sql));
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if (stmt != null) {
							try {
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				};
			}.start();
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return datas;
	}

	@Override
	public String allQrNew(String orderNo, int admid) {
		StringBuffer bf = new StringBuffer();
		String datas = "";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int row = 0;
		// ??????????????????
		String sql = "SELECT ops.userid,ops.orderid,ops.od_id as id,ops.goods_p_url,ops.goodsid,ops.goods_p_price,ops.goodsdataid as goodsdata_id,ops.goods_url as car_url,od.car_urlMD5,od.goods_pid,od.car_img,ops.goods_price as goodsprice,ops.goods_name as goodsname,ops.usecount as yourorder,oi.isDropshipOrder,od.dropshipid,ops.buycount"
				+ " FROM order_product_source ops INNER JOIN order_details od ON ops.od_id=od.id INNER JOIN orderinfo oi ON od.orderid=oi.order_no INNER JOIN goods_distribution gd ON od.id=gd.odid"
				+ " WHERE ops.orderid='"
				+ orderNo
				+ "' AND gd.admuserid='"
				+ admid
				+ "' AND ops.purchase_state in (0,5)";
//				+ " UNION"
//				+ " SELECT distinct od.userid,od.orderid,od.id,od.goodsid,od.goodsdata_id,od.car_url,od.car_img,od.goodsprice,od.car_urlMD5M,od.gooods_pid,od.goodsname,"
//				+ " od.yourorder,oi.isDropshipOrder,od.dropshipid,os.buycount FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no "
//				+ " INNER JOIN goods_distribution gd ON od.id=gd.odid LEFT JOIN order_product_source os ON os.orderid=od.orderid AND os.goodsid=od.goodsid"
//				+ " left join goods_source gs on SUBSTRING_INDEX(gs.goods_url,':',-1)=SUBSTRING_INDEX(od.car_url,':',-1)"
//				+ " WHERE od.orderid='"
//				+ orderNo
//				+ "' AND gd.admuserid='"
//				+ admid + "' AND gs.id IS NOT NULL AND os.id IS NULL";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String oldValue = rs.getString("goods_p_price");
				String newValue = rs.getString("goods_p_url");
				int purchaseCount = rs.getInt("buycount");
				if (purchaseCount == 0) {
					purchaseCount = rs.getInt("yourorder");
				} else {
					purchaseCount = rs.getInt("buycount");
				}
				String url="";
				String car_urlMD5=rs.getString("car_urlMD5");
				if(car_urlMD5!=null && car_urlMD5.substring(0, 1).equals("D")){
					url="https://detail.1688.com/offer/"+rs.getString("goods_pid")+".html";
				}else if(car_urlMD5!=null && car_urlMD5.substring(0, 1).equals("M")){//M??????????????????????????????
					url="https://www.amazon.com/"+(rs.getString("goodsname")==null?"a":rs.getString("goodsname"))+"/dp/"+rs.getString("goods_pid");
				}else{
					url=rs.getString("car_url");
				}
//				String sql1 = "SELECT goods_purl,goods_price FROM goods_source WHERE goods_url='"+ url + "'";
//				stmt1 = conn.prepareStatement(sql1);
//				rs1 = stmt1.executeQuery();
//				if (rs1.next()) {
//					oldValue = rs1.getString("goods_price");
//					newValue = rs1.getString("goods_purl");
//				}
				row = PurchaseComfirmTwoHyqr(rs.getInt("userid"),
						rs.getString("orderid"), rs.getInt("id"),
						rs.getInt("goodsid"), rs.getInt("goodsdata_id"), admid,
						url, rs.getString("car_img"),
						rs.getString("goodsprice"), rs.getString("goodsname"),
						rs.getInt("yourorder"), oldValue, newValue,purchaseCount, rs.getString("dropshipid"),
						rs.getString("isDropshipOrder"));

				bf.append(rs.getString("orderid")).append(";")
						.append(rs.getInt("goodsid")).append(";")
						.append(sdf.format(date)).append("&");
			}
			datas = bf.toString().length() > 0 ? bf.toString().substring(0,bf.toString().length() - 1) : "";
		} catch (SQLException e) {
			LOG.info("=====================????????????????????????" + e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
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
		return datas;
	}


	// ????????????
	@Override
	public int PurchaseComfirmTwoHyqr(int userid, String orderNo, int od_id,
	                                  int goodid, int goodsdataid, int admid, String goodsurl,
	                                  String googsimg, String goodsprice, String goodstitle,
	                                  int googsnumber, String oldValue, String newValue,
	                                  int purchaseCount, String child_order_no, String isDropshipOrder) {
		int i = 0;
		// Connection conn = DBHelper.getInstance().getConnection();
		Connection conn = DBHelper.getInstance().getConnection(); // ??????
		ResultSet rse = null;
		PreparedStatement stmtfind = null;
		PreparedStatement stmttt = null, stmc = null, stmcc = null;
		try {
			i++;
			int res = 0;
			String goods_p_url = "";
			int buyCount = 0;
			String sqlfind = "select count(id) as sid from order_product_source where orderid='"
					+ orderNo + "' and goodsid=" + goodid;
			stmtfind = conn.prepareStatement(sqlfind);
			rse = stmtfind.executeQuery();
			if (rse.next()) {
				res = rse.getInt("sid");
			}
			if (res == 0) {
				String sql1 = "select yourorder,goods_pid,car_img,goodsname from order_details where id=?";
				stmttt = conn.prepareStatement(sql1);
				stmttt.setInt(1, od_id);
				rse = stmttt.executeQuery();
				if (rse.next()) {
//					goods_p_url = "https://detail.1688.com/offer/"+ rse.getString("goods_pid") + ".html";
					Pattern pattern = Pattern.compile("(\\d{6})");
					boolean flag=pattern.matcher(goods_p_url).matches();
					if(flag){
						goods_p_url = "https://detail.1688.com/offer/"+ rse.getString("goods_pid") + ".html";
					}else{
						goods_p_url = "https://www.amazon.com/"+rse.getString("goodsname")+"/dp/"+rse.getString("goods_pid")+"";
					}
					buyCount = rse.getInt("yourorder");
					googsimg=rse.getString("car_img");
				}
				if (googsnumber <= 0) {
					googsnumber = buyCount;
				}
				if ("".equals(newValue)) {
					newValue = goods_p_url;
				}
				String itemid = Util.getItemid(newValue);
				String sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,confirm_userid,"
						+ "goodsid,goodsdataid,goods_url,goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,"
						+ "usecount,buycount,purchase_state,od_id,tb_1688_itemid,tborderid) select ?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,'??????8' "
						+ "FROM DUAL WHERE NOT EXISTS (SELECT * FROM order_product_source WHERE orderid=? and goodsid=?);";
				String sqlc = "update order_details set purchase_state=1 where orderid=? and goodsid=?";
				stmttt = conn.prepareStatement(sqlll);
				stmc = conn.prepareStatement(sqlc);
				stmttt.setDouble(1, admid);
				stmttt.setInt(2, userid);
				stmttt.setString(3, orderNo);
				stmttt.setInt(4, 0);
				stmttt.setInt(5, goodid);
				stmttt.setInt(6, goodsdataid);
				stmttt.setString(7, goods_p_url);//????????????
//				stmttt.setString(7, goodsurl);//????????????
				stmttt.setString(8, newValue);//????????????
				stmttt.setString(9, googsimg);
				stmttt.setString(10, goodsprice);
				stmttt.setString(11, oldValue);//?????????
				stmttt.setString(12, goodstitle);
				stmttt.setInt(13, googsnumber);//????????????
				stmttt.setInt(14, purchaseCount);
				stmttt.setInt(15, goodid);
				stmttt.setString(16, itemid);
				stmttt.setString(17, orderNo);
				stmttt.setInt(18, od_id);
				stmc.setString(1, orderNo);
				stmc.setInt(2, goodid);
				stmttt.executeUpdate();
				stmc.executeUpdate();
				i++;
				PurchaseComfirmTwoHyqrThred thread = new PurchaseComfirmTwoHyqrThred(
						sqlc, goodid, orderNo);
				thread.start();
			} else {
				String sqlll = "update order_product_source set confirm_userid="
						+ admid
						+ ",confirm_time=now(),purchase_state=1,goodsid='"
						+ goodid
						+ "' "
						+ "where orderid='"
						+ orderNo
						+ "' and od_id=" + od_id + " and del=0";
				String sqlc = "update order_details set purchase_state=1 "
						+ "where orderid='" + orderNo + "'and id=" + od_id;
				String sqlc1 = "update order_details set purchase_state=1 where orderid=? and id=?";
				stmttt = conn.prepareStatement(sqlll);
				stmcc = conn.prepareStatement(sqlc);
				stmttt.executeUpdate();
				stmcc.executeUpdate();
				PurchaseComfirmTwoHyqrThred thread = new PurchaseComfirmTwoHyqrThred(
						sqlc1, od_id, orderNo);
				thread.start();
				i++;

			}
			// ????????????LOG????????????
			String sql = "select id from status_change_log where order_no='"
					+ orderNo + "' and goodsid='" + goodid + "'";
			stmttt = conn.prepareStatement(sql);
			if (stmttt.executeQuery().next()) {
				sql = "update status_change_log set opStatus='????????????',opTime=now(),opName='"
						+ admid
						+ "' where order_no='"
						+ orderNo
						+ "' and goodsid='" + goodid + "'";
			} else {
				sql = "insert into status_change_log (opName,opTime,opStatus,order_no,goodsid) values ('"
						+ admid
						+ "',now(),'????????????','"
						+ orderNo
						+ "','"
						+ goodid
						+ "')";
			}
			stmttt = conn.prepareStatement(sql);
			stmttt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources(rse);
			closeResources(stmtfind);
			closeResources(stmttt);
			closeResources(conn);
			closeResources(stmc);
			closeResources(stmcc);
		}
		return i;
	}

	// ??????????????????
	@Override
	public int PurchaseComfirmOneQxhy(String orderNo, int odid, int adminid) {
		int i = 0;
		String sql = "update order_product_source set confirm_userid=null,confirm_time=null,purchase_state=0 where orderid=? and od_id=? and del=0";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, odid);
			stmt.executeUpdate();

			// final PreparedStatement stmt2 = conn2.prepareStatement(sql);
			// stmt2.setString(1, orderNo);
			// stmt2.setInt(2, goodsid);
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// try {
			// stmt2.executeUpdate();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// }).start();

			i++;
			// ????????????LOG????????????
			sql = "select id from status_change_log where order_no='" + orderNo
					+ "' and odid='" + odid + "'";
			stmt = conn.prepareStatement(sql);
			if (stmt.executeQuery().next()) {
				sql = "update status_change_log set opStatus='??????????????????',opTime=now(),opName='"
						+ adminid
						+ "' where order_no='"
						+ orderNo
						+ "' and odid='" + odid + "'";
			} else {
				sql = "insert into status_change_log (opName,opTime,opStatus,order_no,odid) values ('"
						+ adminid
						+ "',now(),'??????????????????','"
						+ orderNo
						+ "','"
						+ odid + "')";
			}
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	// ????????????????????????
	@Override
	public List<OrderDatailsNew> getOrderdataelsNew(String order) {
		List<OrderDatailsNew> cmlist = new ArrayList<OrderDatailsNew>();

		// ??????????????????
		String orderSql = "select group_concat(\"'\",orderno,\"'\") orderarr from order_fee  where mergeOrders= '"
				+ order + "'";

		Connection con = DBHelper.getInstance().getConnection();
		PreparedStatement ppst = null, ppst2 = null;
		ResultSet rs = null, rs2 = null;
		try {
			String orderarr = order;
			ppst2 = con.prepareStatement(orderSql);
			rs2 = ppst2.executeQuery();
			if (rs2.next()) {
				orderarr = rs2.getString("orderarr");
			}
			String sql = "select DISTINCT od.car_type,od.yourorder,od.userid,ir.position,od.orderid,od.goodsid,od.car_img as googs_img,"
					+ "od.goodsname as goods_title,space(500) as img_type from order_details od "
					+ "left join id_relationtable ir on  od.goodsid=ir.goodid and ir.is_delete != 1  where od.orderid in("
					+ orderarr + ") order by od.orderid,od.userid";
			ppst = con.prepareStatement(sql);
			rs = ppst.executeQuery();
			while (rs.next()) {
				OrderDatailsNew od = new OrderDatailsNew();
				od.setCar_type(rs.getString("car_type"));
				od.setGoodsid(rs.getString("goodsid"));
				od.setGoods_title(rs.getString("goods_title"));
				od.setGoogs_img(rs.getString("googs_img"));
				od.setOrderid(rs.getString("orderid"));
				cmlist.add(od);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DBHelper.getInstance().closeConnection(con);
		return cmlist;
	}

	// ?????????????????? state 0 or 4
	public List<PurchaseBean> getOrdersbyidNew(String userid) {
		List<PurchaseBean> list = new ArrayList<PurchaseBean>();

		String sql = "select DISTINCT mergeOrders,state from order_fee where state in ('0','1','4') and userid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				PurchaseBean purchaseBean = new PurchaseBean();
				purchaseBean.setOfState(rs.getString("state")); // order_fee ??????
				// ??????????????????
				purchaseBean.setOrderNo(rs.getString("mergeOrders"));// ????????????
				list.add(purchaseBean);
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
		return list;
	}

	// ??????sql??????
	public int deleteFromSql(String sql) {
		int i = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			i = stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	public int deleteFromSql127(String sql) {
		int i = 0;
		try {
			/*stmt = conn.prepareStatement(sql);
			i = stmt.executeUpdate();*/
			SendMQ.sendMsg(new RunSqlModel(sql));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public int updateOrderPs(String orderNo, String goodsid, String itemid) {
		int i = 0;
		String sql = "update order_product_source set tb_1688_itemid=? where orderid=? and goodsid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, itemid);
			stmt.setString(2, orderNo);
			stmt.setString(3, goodsid);
			stmt.executeUpdate();
			i++;
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	public int updateOrderPs127(String orderNo, String goodsid, String itemid) {
		int i = 0;
		String sql = "update order_product_source set tb_1688_itemid=? where orderid=? and goodsid=?";
		try {
			/*stmt = conn.prepareStatement(sql);
			stmt.setString(1, itemid);
			stmt.setString(2, orderNo);
			stmt.setString(3, goodsid);
			stmt.executeUpdate();*/
			List<String> lstValues = Lists.newArrayList();
			lstValues.add(itemid);
			lstValues.add(orderNo);
			lstValues.add(goodsid);
			String runSql = DBHelper.covertToSQL(sql, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));
			i++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public int updateGoodsPurl(String orderNo, String goodspurl, String itemid,
	                           String goods_url, double price,int od_id,int admid,int buycount,String address,String shop_id) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		ResultSet rs1=null;
		int i = 0;
		try {
			String sql="select od1.* from order_details od1 inner join order_details od2 on od1.car_urlMD5=od2.car_urlMD5 and od1.goods_pid=od2.goods_pid "
					+ "where od2.id='"+od_id+"' and od1.id<>'"+od_id+"'";
			stmt = conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				sql="select * from order_product_source where orderid='"+orderNo+"' and goodsid='"+ rs.getInt("goodsid")+"'";
				stmt = conn.prepareStatement(sql);
				rs1=stmt.executeQuery();
				if(rs1.next()){
					//???????????????
					if(rs1.getString("last_goods_p_url")!=null && !"".equals(rs1.getString("last_goods_p_url"))){
						sql = "update order_product_source set purchase_state=0, goods_p_price="+ price+ ", last_goods_p_url='"+goodspurl+"',last_tb_1688_itemid='"+itemid+"',shop_id='"+shop_id+"' where orderid='"+orderNo+"' and goodsid='"+ rs.getInt("goodsid")+"'";
					}else{
						sql = "update order_product_source set purchase_state=0, goods_p_price="+ price+ ", goods_p_url='"+goodspurl+"',tb_1688_itemid='"+itemid+"',shop_id='"+shop_id+"' where orderid='"+orderNo+"' and goodsid='"+ rs.getInt("goodsid")+"'";
					}
					stmt = conn.prepareStatement(sql);
				}else{
					sql = "insert into order_product_source(adminid,userid,addtime,orderid,confirm_time,"
							+ "goodsid,goodsdataid,goods_url,goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,"
							+ "usecount,buycount,purchase_state,od_id,tb_1688_itemid,shop_id,tborderid) values(?,?,now(),?,now(),?,?,?,?,?,?,?,?,?,?,0,?,?,?,'??????9') ";
					stmt = conn.prepareStatement(sql);
					stmt.setDouble(1, admid);
					stmt.setInt(2, rs.getInt("userid"));
					stmt.setString(3, orderNo);
					stmt.setInt(4, rs.getInt("goodsid"));
					stmt.setInt(5, rs.getInt("goodsdata_id"));
					stmt.setString(6,goods_url);
					stmt.setString(7, goodspurl);
					stmt.setString(8, rs.getString("car_img"));
					stmt.setString(9, rs.getString("goodsprice"));
					stmt.setDouble(10, price);
					stmt.setString(11, rs.getString("goodsname"));
					stmt.setInt(12, rs.getInt("yourorder"));
					stmt.setInt(13, buycount);
					stmt.setInt(14, rs.getInt("id"));
					stmt.setString(15, itemid);
					stmt.setString(16, shop_id);
				}
				stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	public int updateGoodsPurlgoodsid(String orderNo, String itemid,
	                                  String goodsid) {
		int i = 0;
		// purchase_state=0,
		// == 2 ?????? String sql = "update order_product_source set
		// purchase_state=0, goods_p_price="+price+",
		// goods_p_url=?,tb_1688_itemid=? where orderid=? and
		// goods_url='"+goods_url+"'";
		String sql = "update order_product_source set tb_1688_itemid=? where orderid=? and goodsid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, itemid);
			stmt.setString(2, orderNo);
			stmt.setString(3, goodsid);
			stmt.executeUpdate();
			i++;
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	public int updateOrderinfo(String orderNo) {
		int i = 0;
		// purchase_state=0,
		String sql = "update orderinfo set state=1 where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.executeUpdate();


			List<String> lstValues = new ArrayList<>();
			lstValues.add(orderNo);

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
			i++;
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	public int updateGoodsPurl127(String orderNo, String goodspurl,
	                              String itemid, String goods_url, double price) {
		int i = 0;
		// purchase_state=0,
		String sql = "update order_product_source set goods_p_price="
				+ price
				+ ", goods_p_url=?,tb_1688_itemid=? where orderid=? and goods_url='"
				+ goods_url + "'";

		try {

			List<String> lstValues = new ArrayList<>();
			lstValues.add(goodspurl);
			lstValues.add(itemid);
			lstValues.add(orderNo);

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

			i++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return i;
	}

	// ????????????127?????????
	@Deprecated
	@Override
	public int PurchaseComfirmTwoHyqr127(int userid, String orderNo, int od_id,
	                                     int goodid, int goodsdataid, int admid, String goodsurl,
	                                     String googsimg, String goodsprice, String goodstitle,
	                                     int googsnumber, String oldValue, String newValue, int purchaseCount) {
		// TODO Auto-generated method stub
		int i = 0;
		String itemid = Util.getItemid(newValue);
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rse = null;
		PreparedStatement stmtfind = null;
		try {
			i++;
			int res = 0;
			String sqlfind = "select count(id) as sid from order_product_source where orderid='"
					+ orderNo + "' and goodsid=" + goodid;
			stmtfind = conn.prepareStatement(sqlfind);
			rse = stmtfind.executeQuery();
			if (rse.next()) {
				res = rse.getInt("sid");
			}
			if (res == 0) {
				String sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,confirm_userid,confirm_time,"
						+ "goodsid,goodsdataid,goods_url,goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,"
						+ "usecount,buycount,purchase_state,od_id,tb_1688_itemid,tborderid) values(?,?,now(),?,?,now(),?,?,?,?,?,?,?,?,?,?,1,?,?,'??????10') ";

				List<String> lstValues = new ArrayList<>();
				lstValues.add(String.valueOf(admid));
				lstValues.add(String.valueOf(userid));
				lstValues.add(String.valueOf(orderNo));
				lstValues.add(String.valueOf(userid));
				lstValues.add(String.valueOf(goodid));
				lstValues.add(String.valueOf(goodsdataid));
				lstValues.add(String.valueOf(goodsurl));
				lstValues.add(String.valueOf(newValue));
				lstValues.add(String.valueOf(googsimg));
				lstValues.add(String.valueOf(goodsprice));
				lstValues.add(String.valueOf(oldValue));
				lstValues.add(String.valueOf(goodstitle));
				lstValues.add(String.valueOf(googsnumber));
				lstValues.add(String.valueOf(googsnumber));
				lstValues.add(String.valueOf(purchaseCount));
				lstValues.add(String.valueOf(od_id));
				lstValues.add(String.valueOf(itemid));

				String runSql = DBHelper.covertToSQL(sqlll,lstValues);
				Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

				i++;
			} else {
				String sqlll = "update order_product_source set confirm_userid="
						+ admid
						+ ",confirm_time=now(),purchase_state=1 "
						+ "where orderid='"
						+ orderNo
						+ "' and goodsid="
						+ goodid + " and del=0";

				SendMQ.sendMsgByRPC(new RunSqlModel(sqlll));
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rse != null) {
				try {
					rse.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfind != null) {
				try {
					stmtfind.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}



			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	@Deprecated
	@Override
	public int PurchaseComfirmTwo127(int userid, String orderNo, int od_id,
	                                 int goodid, int goodsdataid, int admid, String goodsurl,
	                                 String googsimg, String goodsprice, String goodstitle,
	                                 int googsnumber, String oldValue, String newValue, int purchaseCount) {
		// TODO Auto-generated method stub
		int i = 0;
		// zp ?????? ????????????order_change
		String sql = "select count(id) as sum from order_change where orderNo=? and goodId=?";
		String sqltwo = "update order_details set purchase_state=1,purchase_time=now(),purchase_confirmation=? where orderid=? and goodsid=?";
		String sqlthree = "update orderinfo set purchase_number=purchase_number+1 where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null, rse = null, rsb = null;
		PreparedStatement stmtb = null, stmtttt = null, stmtfind = null, stmt = null, stmtdel = null;
		PreparedStatement stmtinfo = null, stmttwo = null, stmtt = null, stmttt = null;
		int ii = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodid);
			stmttwo = conn.prepareStatement(sqltwo);
			stmttwo.setString(1, this.getUserbyID(admid + ""));
			stmttwo.setString(2, orderNo);
			stmttwo.setInt(3, goodid);
			rs = stmt.executeQuery();
			stmttwo.executeUpdate();
			while (rs.next()) {
				ii = rs.getInt("sum");
			}
			// if (ii > 1) {
			// String sqlsetdel = "update order_change set del_state=1 where
			// order_change.orderNo=? and order_change.goodId=?";
			// stmtdel = conn.prepareStatement(sqlsetdel);
			// stmtdel.setString(1, orderNo);
			// stmtdel.setInt(2, goodid);
			// stmtdel.executeUpdate();
			// }
			// String sqll = "";
			// if (ii != 1) {
			// sqll = "insert into
			// order_change(order_change.oldValue,order_change.newValue,"
			// +
			// "order_change.orderNo,order_change.goodId,order_change.ropType,order_change.status,"
			// + "order_change.dateline,order_change.del_state)
			// values(?,?,?,?,6,0,now(),0);";
			// } else if (ii == 1) {
			// sqll = "update order_change set
			// order_change.oldValue=?,order_change.newValue=?,order_change.dateline=now(),"
			// + " order_change.ropType=6 where order_change.orderNo=? and
			// order_change.goodId=?";
			// }
			// stmtt = conn.prepareStatement(sqll);
			// stmtt.setString(1,oldValue);
			// stmtt.setString(2,newValue);
			// stmtt.setString(3,orderNo);
			// stmtt.setInt(4,goodid);
			// stmtt.executeUpdate();
			i++;

			// ?????????????????????
			int res = 0;
			String sqlfind = "select count(id) as sid from order_product_source where orderid='"
					+ orderNo + "' and goodsid=" + goodid;
			stmtfind = conn.prepareStatement(sqlfind);
			rse = stmtfind.executeQuery();
			if (rse.next()) {
				res = rse.getInt("sid");
			}

			if (res == 0) {
				String sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,confirm_userid,confirm_time,"
						+ "goodsid,goodsdataid,goods_url,goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,"
						+ "usecount,buycount,purchase_state,od_id,tborderid) values(?,?,now(),?,?,now(),?,?,?,?,?,?,?,?,?,?,3,?,'??????11') ";
				stmttt = conn.prepareStatement(sqlll);
				stmttt.setDouble(1, admid);
				stmttt.setInt(2, userid);
				stmttt.setString(3, orderNo);
				stmttt.setInt(4, userid);
				stmttt.setInt(5, goodid);
				stmttt.setInt(6, goodsdataid);
				stmttt.setString(7, goodsurl);
				stmttt.setString(8, newValue);
				stmttt.setString(9, googsimg);
				stmttt.setString(10, goodsprice);
				stmttt.setString(11, oldValue);
				stmttt.setString(12, goodstitle);
				stmttt.setInt(13, googsnumber);
				stmttt.setInt(14, purchaseCount);
				stmttt.setInt(15, od_id);
				stmttt.executeUpdate();
				i++;
			} else {
				String sqlll = "update order_product_source set confirm_userid="
						+ admid
						+ ",confirm_time=now(),purchase_state=3 "
						+ "where orderid='"
						+ orderNo
						+ "' and goodsid="
						+ goodid + " and del=0";
				stmttt = conn.prepareStatement(sqlll);
				stmttt.executeUpdate();
				i++;
			}
			int iib = 0;
			String sqlb = "select count(id) as sumb from goods_source where goods_url=? and goods_purl=? ";
			stmtb = conn.prepareStatement(sqlb);
			stmtb.setString(1, goodsurl);
			stmtb.setString(2, newValue);
			rsb = stmtb.executeQuery();
			i++;
			if (rsb.next()) {
				iib = rsb.getInt("sumb");
			}
			String sqllll = "";
			if (iib == 0) {// goods_source???//
				sqllll = "insert into goods_source(goodsdataid,goods_url,goods_purl,goods_img_url,"
						+ "goods_price,goods_name,moq,goodssourcetype,buycount,del,updatetime) "
						+ "values(?,?,?,?,?,?,1,1,?,0,now())";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setInt(1, goodsdataid);
				stmtttt.setString(2, goodsurl);
				stmtttt.setString(3, newValue);
				stmtttt.setString(4, googsimg);
				stmtttt.setString(5, oldValue);
				stmtttt.setString(6, goodstitle);
				stmtttt.setInt(7, purchaseCount);
			} else {
				sqllll = "update goods_source set goods_img_url=?,goods_price=?,buycount=?,"
						+ "updatetime=now() where goods_url=? and goods_purl=?";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setString(1, googsimg);
				stmtttt.setString(2, oldValue);
				stmtttt.setInt(3, purchaseCount);
				stmtttt.setString(4, goodsurl);
				stmtttt.setString(5, newValue);
			}
			stmtttt.executeUpdate();
			i++;
			stmtinfo = conn.prepareStatement(sqlthree);
			stmtinfo.setString(1, orderNo);
			stmtinfo.executeUpdate();
			i++;
			this.order_state(conn, orderNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rsb != null) {
				try {
					rsb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtdel != null) {
				try {
					stmtdel.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtb != null) {
				try {
					stmtb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtttt != null) {
				try {
					stmtttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rse != null) {
				try {
					rse.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfind != null) {
				try {
					stmtfind.close();
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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttwo != null) {
				try {
					stmttwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttt != null) {
				try {
					stmttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtinfo != null) {
				try {
					stmtinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	@Deprecated
	@Override
	public int YiRuKu127(String url, String orderno, int goodid) {
		// TODO Auto-generated method stub
		int rk = 0;
		String sql = "update order_product_source set purchase_state=4 where goods_url=? and orderid=? and goodsid=?";
		String sqlorder = "update order_details set state=1 where orderid=? and goodsid=?";
		String sqlorderinfo = "select state from order_details where orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmtorder = null;
		PreparedStatement stmtorderinfo = null;
		PreparedStatement stmtoi = null;
		ResultSet rsorderinfo = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			stmt.setString(2, orderno);
			stmt.setInt(3, goodid);
			stmt.executeUpdate();
			rk = 1;
			stmtorder = conn.prepareStatement(sqlorder);
			stmtorder.setString(1, orderno);
			stmtorder.setInt(2, goodid);
			stmtorder.executeUpdate();
			stmtorderinfo = conn.prepareStatement(sqlorderinfo);
			stmtorderinfo.setString(1, orderno);
			rsorderinfo = stmtorderinfo.executeQuery();
			int i = 0;
			while (rsorderinfo.next()) {
				int state = rsorderinfo.getInt("state");
				if (state == 0)
					i++;
			}
			if (i == 0) {
				String sqloi = "update orderinfo set state=2 where order_no=?";
				stmtoi = conn.prepareStatement(sqloi);
				stmtoi.setString(1, orderno);
				stmtoi.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmtoi != null) {
				try {
					stmtoi.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rsorderinfo != null) {
				try {
					rsorderinfo.close();
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
			if (stmtorder != null) {
				try {
					stmtorder.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtorderinfo != null) {
				try {
					stmtorderinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rk;
	}

	// ????????????order_product_source ????????? ???????????????
	@Deprecated
	@Override
	public void AddRecource127(String type, int admid, int userid,
	                           int goodsdataid, String goods_url, String googs_img,
	                           double goodsprice, String goods_title, int googsnumber,
	                           String orderNo, int od_id, int goodid, double price,
	                           String resource, int buycount, String reason, String currency,
	                           String pname) {
		String itemid = Util.getItemid(resource);
		if (itemid == null || "".equals(itemid)) {
			itemid = "0000";
		}

		if (resource.contains("1688.com")) {
			resource = resource.substring(0, resource.indexOf(".html") + 5);
		}
		String sql = "select count(id) as sum from order_change where orderNo=? and goodId=? and ropType=6 and del_state=0";
		String sqla = "select count(id) as suma from order_product_source where goods_url=? and orderid=? and goodsid=?";
		String sqlb = "select count(id) as sumb from goods_source where goods_url=?";// and
		// goods_purl=?
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null, rsa = null, rsb = null;
		int ii = 0, iia = 0, iib = 0;
		PreparedStatement stmt = null, stmta = null, stmtb = null;
		PreparedStatement stmtt = null, stmttt = null, stmtttt = null;
		PreparedStatement stmtupdate = null;
		PreparedStatement stmc = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ii = rs.getInt("sum");
			}
			stmta = conn.prepareStatement(sqla);
			stmta.setString(1, goods_url);
			stmta.setString(2, orderNo);
			stmta.setInt(3, goodid);
			rsa = stmta.executeQuery();
			if (rsa.next()) {
				iia = rsa.getInt("suma");
			}
			stmtb = conn.prepareStatement(sqlb);
			stmtb.setString(1, goods_url);
			// stmtb.setString(2, resource);
			rsb = stmtb.executeQuery();
			if (rsb.next()) {
				iib = rsb.getInt("sumb");
			}
			// if (ii != 0) {
			// String sqlupdate = "update order_change set del_state=1 where
			// order_change.orderNo=? "
			// + "and order_change.goodId=? and ropType=6 and del_state=0";
			// stmtupdate = conn.prepareStatement(sqlupdate);
			// stmtupdate.setString(1, orderNo);
			// stmtupdate.setInt(2, goodid);
			// stmtupdate.executeUpdate();
			// }
			// // ????????????
			String tempSql;
			// tempSql = "delete from order_change where orderNo='";
			// + orderNo + "' and goodId='" + goodid + "'";
			// int temp2 = this.deleteFromSql127(tempSql);
			// String sqll = "insert into
			// order_change(order_change.oldValue,order_change.newValue,"
			// +
			// "order_change.orderNo,order_change.goodId,order_change.ropType,order_change.status,"
			// + "order_change.dateline,order_change.del_state)
			// values(?,?,?,?,6,0,now(),0);";
			// stmtt = conn.prepareStatement(sqll);
			// stmtt.setDouble(1, price);
			// stmtt.setString(2, resource);
			// stmtt.setString(3, orderNo);
			// stmtt.setInt(4, goodid);
			// stmtt.executeUpdate();
			String sqlll = "";
			String sqlc = "";
			List<String> lstValues = Lists.newArrayList();
			if (iia == 0) { // order_product_source???//
				if (reason.contains("?????????")) {
					tempSql = "delete from order_product_source where orderid='"
							+ orderNo + "' and goodsid='" + goodid + "'";
					this.deleteFromSql127(tempSql);
					sqlll = "insert into order_product_source(adminid,userid,orderid,goodsid,goodsdataid,goods_url,"
							+ "goods_img_url,goods_price,goods_name,usecount,buycount,currency,goods_p_name,remark,"
							+ "purchase_state,od_id,tb_1688_itemid,tborderid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,5,?,?,'??????12') ";
//					stmttt = conn.prepareStatement(sqlll);
					lstValues.add(String.valueOf(admid));
					lstValues.add(String.valueOf(userid));
					lstValues.add(orderNo);
					lstValues.add(String.valueOf(goodid));
					lstValues.add(String.valueOf(goodsdataid));
					lstValues.add(goods_url);
					lstValues.add(googs_img);
					lstValues.add(String.valueOf(goodsprice));
					lstValues.add(goods_title);
					lstValues.add(String.valueOf(googsnumber));
					lstValues.add(String.valueOf(buycount));
					lstValues.add(currency);
					lstValues.add(pname);
					lstValues.add(reason);
					lstValues.add(String.valueOf(od_id));
					lstValues.add(itemid);
					String runSql = DBHelper.covertToSQL(sqlll, lstValues);
					SendMQ.sendMsg(new RunSqlModel(runSql));
				} else {
					lstValues.add(String.valueOf(admid));
					lstValues.add(String.valueOf(userid));
					lstValues.add(orderNo);
					lstValues.add(String.valueOf(goodid));
					lstValues.add(String.valueOf(goodsdataid));
					lstValues.add(goods_url);
					lstValues.add(resource);
					lstValues.add(googs_img);
					lstValues.add(String.valueOf(goodsprice));
					lstValues.add(String.valueOf(price));
					lstValues.add(goods_title);
					lstValues.add(String.valueOf(googsnumber));
					lstValues.add(String.valueOf(buycount));
					
					if (reason.equals("") || reason == null) {
						tempSql = "delete from order_product_source where orderid='"
								+ orderNo + "' and goodsid='" + goodid + "'";
						int temp = this.deleteFromSql127(tempSql);
						sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,goodsid,goodsdataid,goods_url,goods_p_url,"
								+ "goods_img_url,goods_price,goods_p_price,goods_name,usecount,buycount,currency,goods_p_name,od_id,tb_1688_itemid,tborderid) "
								+ "values(?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'??????13') ";
//						stmttt = conn.prepareStatement(sqlll);
//						stmttt.setString(14, currency);
//						stmttt.setString(15, pname);
//						stmttt.setInt(16, od_id);
//						stmttt.setString(17, itemid);
						lstValues.add(currency);
						lstValues.add(pname);
						lstValues.add(String.valueOf(od_id));
						lstValues.add(itemid);
						String runSql = DBHelper.covertToSQL(sqlll, lstValues);
						SendMQ.sendMsg(new RunSqlModel(runSql));
					} else {
						tempSql = "delete from order_product_source where orderid='"
								+ orderNo + "' and goodsid='" + goodid + "'";
						int temp = this.deleteFromSql127(tempSql);
						sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,goodsid,goodsdataid,goods_url,goods_p_url,"
								+ "goods_img_url,goods_price,goods_p_price,goods_name,usecount,buycount,remark,purchase_state,"
								+ "currency,goods_p_name,od_id,tb_1688_itemid,tborderid) values(?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,5,?,?,?,?,'??????14') ";
						/*stmttt = conn.prepareStatement(sqlll);
						stmttt.setString(14, reason);
						stmttt.setString(15, currency);
						stmttt.setString(16, pname);
						stmttt.setInt(17, od_id);
						stmttt.setString(18, itemid);*/
						lstValues.add(reason);
						lstValues.add(currency);
						lstValues.add(pname);
						lstValues.add(String.valueOf(od_id));
						lstValues.add(itemid);
						String runSql = DBHelper.covertToSQL(sqlll, lstValues);
						SendMQ.sendMsg(new RunSqlModel(runSql));
					}
					
				}
			} else {
				if (reason.contains("?????????")) {
					sqlll = "update order_product_source set tb_1688_itemid="
							+ itemid
							+ " ,adminid=?,userid=?,addtime=null,confirm_userid=null,confirm_time=null,"
							+ "purchase_state=5,remark=?,goods_img_url=?,goods_price=?,usecount=?,buycount=?,goods_p_url=null,"
							+ "goods_p_price=null,goods_p_name=?,od_id=? where goods_url=? and orderid=? and goodsid=? ";
//					stmttt = conn.prepareStatement(sqlll);
//					stmttt.setString(10, goods_url);
//					stmttt.setString(11, orderNo);
//					stmttt.setInt(12, goodid);
					lstValues.add(String.valueOf(admid));
					lstValues.add(String.valueOf(userid));
					lstValues.add(reason);
					lstValues.add(googs_img);
					lstValues.add(String.valueOf(goodsprice));
					lstValues.add(String.valueOf(googsnumber));
					lstValues.add(String.valueOf(buycount));
					lstValues.add(pname);
					lstValues.add(String.valueOf(od_id));
					lstValues.add(goods_url);
					lstValues.add(orderNo);
					lstValues.add(String.valueOf(goodid));
					String runSql = DBHelper.covertToSQL(sqlll, lstValues);
					SendMQ.sendMsg(new RunSqlModel(runSql));
				} else {
					lstValues.add(String.valueOf(admid));
					lstValues.add(String.valueOf(userid));
					lstValues.add(String.valueOf(goodid));
					lstValues.add(resource);
					lstValues.add(googs_img);
					lstValues.add(String.valueOf(goodsprice));
					lstValues.add(String.valueOf(price));
					lstValues.add(String.valueOf(googsnumber));
					lstValues.add(String.valueOf(buycount));
					
					if (reason.equals("") || reason == null) {
						// purchase_state=0,
						sqlll = "update order_product_source set tb_1688_itemid="
								+ itemid
								+ " , adminid=?,userid=?,addtime=now(),remark='',"
								+ "goodsid=?,goods_p_url=?,goods_img_url=?,goods_price=?,goods_p_price=?,"
								+ "usecount=?,buycount=?,goods_p_name=?,od_id=? where goods_url=? and orderid=? and goodsid=? ";
						sqlc = "update order_details set state=0 where goodsid=? and orderid=?;";
						LOG.info("????????????????????????");
//						stmttt = conn.prepareStatement(sqlll);
						lstValues.add(pname);
						lstValues.add(String.valueOf(od_id));
						lstValues.add(goods_url);
						lstValues.add(orderNo);
						lstValues.add(String.valueOf(goodid));
//						stmttt.setString(10, pname);
//						stmttt.setInt(11, od_id);
//						stmttt.setString(12, goods_url);
//						stmttt.setString(13, orderNo);
//						stmttt.setInt(14, goodid);
						String runSql = DBHelper.covertToSQL(sqlll, lstValues);
						SendMQ.sendMsg(new RunSqlModel(runSql));
						
//						stmc = conn.prepareStatement(sqlc);
//						stmc.setString(2, orderNo);
//						stmc.setInt(1, goodid);
						lstValues = Lists.newArrayList();
						lstValues.add(String.valueOf(goodid));
						lstValues.add(orderNo);
						runSql = DBHelper.covertToSQL(sqlll, lstValues);
						SendMQ.sendMsg(new RunSqlModel(runSql));
						
					} else {
						// ????????????
						sqlll = "update order_product_source set tb_1688_itemid="
								+ itemid
								+ " , adminid=?,userid=?,addtime=now(),goodsid=?,goods_p_url=?,"
								+ "goods_img_url=?,goods_price=?,goods_p_price=?,usecount=?,buycount=?,remark=?,"
								+ "purchase_state=5,goods_p_name=?,od_id=? where goods_url=? and orderid=? and goodsid=? ";
						/*stmttt = conn.prepareStatement(sqlll);
						stmttt.setString(10, reason);
						stmttt.setString(11, pname);
						stmttt.setInt(12, od_id);
						stmttt.setString(13, goods_url);
						stmttt.setString(14, orderNo);
						stmttt.setInt(15, goodid);*/
						lstValues.add(reason);
						lstValues.add(pname);
						lstValues.add(String.valueOf(od_id));
						lstValues.add(goods_url);
						lstValues.add(orderNo);
						lstValues.add(String.valueOf(goodid));
						
						String runSql = DBHelper.covertToSQL(sqlll, lstValues);
						SendMQ.sendMsg(new RunSqlModel(runSql));
					}
					
				}
			}
//			stmttt.executeUpdate();
			/*if (stmc != null) {
				stmc.executeUpdate();
			} else {

			}*/
			lstValues = Lists.newArrayList();
			String sqllll = "";
			if (iib == 0) {// goods_source???//
				sqllll = "insert into goods_source(goodsdataid,goods_url,goods_purl,goods_img_url,"
						+ "goods_price,goods_name,moq,goodssourcetype,buycount,del,updatetime) "
						+ "values(?,?,?,?,?,?,1,1,?,0,now())";
				/*stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setInt(1, goodsdataid);
				stmtttt.setString(2, goods_url);
				stmtttt.setString(3, resource);
				stmtttt.setString(4, googs_img);
				stmtttt.setDouble(5, price);
				stmtttt.setString(6, pname);
				stmtttt.setInt(7, buycount);*/
				lstValues.add(String.valueOf(goodsdataid));
				lstValues.add(goods_url);
				lstValues.add(resource);
				lstValues.add(googs_img);
				lstValues.add(String.valueOf(price));
				lstValues.add(pname);
				lstValues.add(String.valueOf(buycount));
			} else {
				// and goods_purl=?
				sqllll = "update goods_source set goods_purl='" + resource
						+ "',goods_img_url=?,goods_price=?,buycount=?,"
						+ "updatetime=now() where goods_url=? ";
				/*stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setString(1, googs_img);
				stmtttt.setDouble(2, price);
				stmtttt.setInt(3, buycount);
				stmtttt.setString(4, goods_url);*/
				lstValues.add(googs_img);
				lstValues.add(String.valueOf(price));
				lstValues.add(String.valueOf(buycount));
				lstValues.add(goods_url);
				// stmtttt.setString(5, resource);
			}
			if (!reason.contains("?????????")) {
				String runSql = DBHelper.covertToSQL(sqlll, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));
//				stmtttt.executeUpdate();
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
			if (rsa != null) {
				try {
					rsa.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rsb != null) {
				try {
					rsb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtupdate != null) {
				try {
					stmtupdate.close();
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
			if (stmta != null) {
				try {
					stmta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtb != null) {
				try {
					stmtb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttt != null) {
				try {
					stmttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtttt != null) {
				try {
					stmtttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			if (stmc != null) {
				try {
					stmc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			DBHelper.getInstance().closeConnection(conn);
		}
		// updateOrderPs127(orderNo,goodid+"",itemid);
		if ("2".equals(type)) {
			updateGoodsPurl127(orderNo, resource, itemid, goods_url, price);
		}
	}

	@Deprecated
	@Override
	public void OutPortNow127(String orderno, String wuliuNumber,
	                          String transport, String userid) {
		// String sqlorderinfo =
		// "update orderinfo set state='3' where order_no=?";//??????orderinfo???;
		String sqlorderinfo = "";// "update orderinfo set state='3' where
		// order_no in (?)";//??????orderinfo???;
		// String sqlorder_fee =
		// "update order_fee set state=2 where orderno=?";//??????order_fee???;
		String sqlorder_fee = "";// "update order_fee set state=2 where orderno
		// in (?)";//??????order_fee???;

		// ?????????????????????
		String sql = "select group_concat(\"'\",orderno,\"'\") orderarr from order_fee  where mergeOrders= '"
				+ orderno + "' and state='1' ";

		String orderNew = orderno;
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null, stmtf = null, stmtfee = null, mergeStmt, stmtid = null;
		ResultSet rs = null;
		try {
			// ?????????????????????
			mergeStmt = conn.prepareStatement(sql);
			rs = mergeStmt.executeQuery();
			if (rs.next()) {
				orderNew = rs.getString("orderarr");
			}

			sqlorderinfo = "update orderinfo set state='3' where order_no in ("
					+ orderNew + ")";
//			stmt = conn.prepareStatement(sqlorderinfo);
//			// stmt.setString(1,orderNew);
//			stmt.executeUpdate();
			SendMQ.sendMsg(new RunSqlModel(sqlorderinfo));

			// ?????????????????????
			sqlorderinfo = "update id_relationtable set state='1' where  is_delete != 1 and  orderid in ("
					+ orderNew + ")";
//			stmtid = conn.prepareStatement(sqlorderinfo);
//			stmtid.executeUpdate();
			SendMQ.sendMsg(new RunSqlModel(sqlorderinfo));
			
			sql = "select orderno from order_fee  where mergeOrders= '"
					+ orderno + "' and state='1' ";
			mergeStmt = conn.prepareStatement(sql);
			rs = mergeStmt.executeQuery();
			while (rs.next()) {
				String strTemp = rs.getString("orderno");
				String sqlforwarder = "insert into forwarder(order_no,express_no,logistics_name,createtime,isneed,yfhorder,ship_name,userid) values(?,?,?,now(),?,?,?,?) ";// ??????forwarder???;

//				stmtf = conn.prepareStatement(sqlforwarder);
				List<String> lstValues = Lists.newArrayList();
				lstValues.add(strTemp);
				int in;
				if (transport.equals("yyffhh")) { // ???????????????
//					stmtf.setString(2, wuliuNumber); // ???????????????????????????
//					stmtf.setString(3, "");
//					stmtf.setString(5, "");
//					stmtf.setString(6, "?????????");
					lstValues.add(wuliuNumber);
					lstValues.add("");
					lstValues.add("1");
					lstValues.add("");
					lstValues.add("?????????");
					in = 1;
				} else {
//					stmtf.setString(2, wuliuNumber);
//					stmtf.setString(3, transport);// 4PX ??? ??????????????????
//					stmtf.setString(5, "");
//					stmtf.setString(6, "");
					lstValues.add(wuliuNumber);
					lstValues.add(transport);
					lstValues.add("0");
					lstValues.add("");
					lstValues.add("");
					in = 0;
				}
				lstValues.add(String.valueOf(userid));
//				stmtf.setInt(4, in);
//				stmtf.executeUpdate();
				
				String runSql = DBHelper.covertToSQL(sqlforwarder, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));

			}

			sqlorder_fee = "update order_fee set state=2 where orderno in ("
					+ orderNew + ")";// ??????order_fee???;
//			stmtfee = conn.prepareStatement(sqlorder_fee);
			// stmtfee.setString(1,orderNew );
//			stmtfee.executeUpdate();
			SendMQ.sendMsg(new RunSqlModel(sqlorder_fee));
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
			if (stmtid != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtf != null) {
				try {
					stmtf.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfee != null) {
				try {
					stmtfee.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	/**
	 * ???????????????????????????????????????
	 *
	 * @param map
	 * @return
	 * @author whj
	 */
	@Override
	public int addPreferentialPrice(final Map<Object, Object> map) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int row = 0;
		try {
			sql = "select goods_pid,car_urlMD5,goodsname from order_details where orderid=? and goodsid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, map.get("orderid").toString());
			stmt.setInt(2, Integer.valueOf(map.get("goodsid").toString()));
			rs = stmt.executeQuery();
			if (rs.next()) {
				String goods_pid = rs.getString("goods_pid");
				String uuid = rs.getString("car_urlMD5");
				String goods_url="";
				String reg = "^.*\\d{6}.*$";
				boolean flag= goods_pid.matches(reg);
				if(flag){
					goods_url = "https://detail.1688.com/offer/"+ goods_pid + ".html";
				}else{
					goods_url = "https://www.amazon.com/"+rs.getString("goodsname")+"/dp/"+goods_pid+"";
				}
				String goods_p_url=map.get("goods_p_url").toString();
				String goods_p_itemid="0000";
				if(goods_p_url.indexOf("1688")>-1 || goods_p_url.indexOf("taobao")>-1){
					goods_p_itemid = Util.getItemid(map.get("goods_p_url").toString());
				}
				sql = "select id from preferential_goods where goods_pid=? and goods_p_itemid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, goods_pid);
				stmt.setString(2, goods_p_itemid);
				rs = stmt.executeQuery();
				if (rs.next()) {
					int pg_id = rs.getInt("id");
					sql = "insert into preferential_goods_price (pgid,goods_p_itemid,begin,end,price,is_manual) values(?,?,?,?,?,1)";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(1, pg_id);
					stmt.setString(2, goods_p_itemid);
					stmt.setInt(3,Integer.valueOf(map.get("add_begin").toString()));
					stmt.setInt(4,Integer.valueOf(map.get("add_end").toString()));
					stmt.setDouble(5,Double.valueOf(map.get("add_price").toString()));
					row = stmt.executeUpdate();
				} else {
					sql = "insert into preferential_goods (goods_pid,goods_url,goods_p_url,goods_p_itemid,createtime,price,uuid,is_manual) values(?,?,?,?,now(),?,?,1)";
					stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					stmt.setString(1, goods_pid);
					stmt.setString(2, goods_url);
					stmt.setString(3, map.get("goods_p_url").toString());
					stmt.setString(4, goods_p_itemid);
					stmt.setString(5, map.get("goods_p_price").toString());
					stmt.setString(6, uuid);
					row = stmt.executeUpdate();
					rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						int id = rs.getInt(1);
						sql = "insert into preferential_goods_price (pgid,goods_p_itemid,begin,end,price,is_manual) values(?,?,?,?,?,1)";
						stmt = conn.prepareStatement(sql);
						stmt.setInt(1, id);
						stmt.setString(2, goods_p_itemid);
						stmt.setInt(3, Integer.valueOf(map.get("add_begin").toString()));
						stmt.setInt(4,Integer.valueOf(map.get("add_end").toString()));
						stmt.setDouble(5,Double.valueOf(map.get("add_price").toString()));
						row = stmt.executeUpdate();
					}
				}
			}
			// ?????????????????????
			new Thread() {
				Connection conn2 = DBHelper.getInstance().getConnection2();
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String sql = "";
				public void run() {
					try {
						sql = "select goods_pid,car_urlMD5,goodsname from order_details where orderid=? and goodsid=?";
						stmt = conn2.prepareStatement(sql);
						stmt.setString(1, map.get("orderid").toString());
						stmt.setInt(2, Integer.valueOf(map.get("goodsid").toString()));
						rs = stmt.executeQuery();
						if (rs.next()) {
							String goods_pid = rs.getString("goods_pid");
							String uuid = rs.getString("car_urlMD5");
							String goods_url="";
							String reg = "^.*\\d{6}.*$";
							boolean flag= goods_pid.matches(reg);
							if(flag){
								goods_url = "https://detail.1688.com/offer/"+ goods_pid + ".html";
							}else{
								goods_url = "https://www.amazon.com/"+rs.getString("goodsname")+"/dp/"+goods_pid+"";
							}
							String goods_p_url=map.get("goods_p_url").toString();
							String goods_p_itemid="0000";
							if(goods_p_url.indexOf("1688")>-1 || goods_p_url.indexOf("taobao")>-1){
								goods_p_itemid = Util.getItemid(map.get("goods_p_url").toString());
							}
							sql = "select id from preferential_goods where goods_pid=? and goods_p_itemid=?";
							stmt = conn2.prepareStatement(sql);
							stmt.setString(1, goods_pid);
							stmt.setString(2, goods_p_itemid);
							rs = stmt.executeQuery();
							if (rs.next()) {
								int pg_id = rs.getInt("id");
								sql = "insert into preferential_goods_price (pgid,goods_p_itemid,begin,end,price,is_manual) values(?,?,?,?,?,1)";
								/*stmt = conn2.prepareStatement(sql);
								stmt.setInt(1, pg_id);
								stmt.setString(2, goods_p_itemid);
								stmt.setInt(3, Integer.valueOf(map.get(
										"add_begin").toString()));
								stmt.setInt(4, Integer.valueOf(map.get(
										"add_end").toString()));
								stmt.setDouble(5, Double.valueOf(map.get(
										"add_price").toString()));
								stmt.executeUpdate();*/
								List<String> lstValues = Lists.newArrayList();
								lstValues.add(String.valueOf(pg_id));
								lstValues.add(goods_p_itemid);
								lstValues.add(map.get(
										"add_begin").toString());
								lstValues.add(map.get(
										"add_end").toString());
								lstValues.add(map.get(
										"add_price").toString());
								String runSql = DBHelper.covertToSQL(sql, lstValues);
								SendMQ.sendMsg(new RunSqlModel(runSql));
							} else {
								sql = "select id from preferential_goods order by id desc limit 1";
								stmt = conn2.prepareStatement(sql);
								rs = stmt.executeQuery();
								int id = 0;
								if(rs.next()) {
									id = rs.getInt("id");
								}
								
								sql = "insert into preferential_goods (goods_pid,goods_url,goods_p_url,goods_p_itemid,createtime,price,uuid,is_manual) values(?,?,?,?,now(),?,?,1)";
								/*stmt = conn2.prepareStatement(sql,
										Statement.RETURN_GENERATED_KEYS);
								stmt.setString(1, goods_pid);
								stmt.setString(2, goods_url);
								stmt.setString(3, map.get("goods_p_url")
										.toString());
								stmt.setString(4, goods_p_itemid);
								stmt.setString(5, map.get("goods_p_price")
										.toString());
								stmt.setString(6, uuid);
								stmt.executeUpdate();
								rs = stmt.getGeneratedKeys();*/
								List<String> lstValues = Lists.newArrayList();
								lstValues.add(goods_pid);
								lstValues.add(goods_url);
								lstValues.add(map.get("goods_p_url")
										.toString());
								lstValues.add(goods_p_itemid);
								lstValues.add(map.get("goods_p_price")
										.toString());
								lstValues.add(uuid);
								String runSql = DBHelper.covertToSQL(sql, lstValues);
								int r = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
								if (r > 0) {
									id = id+1;
//									int id = rs.getInt(1);
									sql = "insert into preferential_goods_price (pgid,goods_p_itemid,begin,end,price,is_manual) values(?,?,?,?,?,1)";
									/*stmt = conn2.prepareStatement(sql);
									stmt.setInt(1, id);
									stmt.setString(2, goods_p_itemid);
									stmt.setInt(3, Integer.valueOf(map.get(
											"add_begin").toString()));
									stmt.setInt(4, Integer.valueOf(map.get(
											"add_end").toString()));
									stmt.setDouble(5, Double.valueOf(map.get(
											"add_price").toString()));
									stmt.executeUpdate();*/
									lstValues = Lists.newArrayList();
									lstValues.add(String.valueOf(id));
									lstValues.add(goods_p_itemid);
									lstValues.add(map.get(
											"add_begin").toString());
									lstValues.add(map.get(
											"add_end").toString());
									lstValues.add(map.get(
											"add_price").toString());
									runSql = DBHelper.covertToSQL(sql, lstValues);
									SendMQ.sendMsg(new RunSqlModel(runSql));
								}
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
						DBHelper.getInstance().closeConnection(conn2);
					}
				};
			}.start();
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	/**
	 * ???????????????????????????????????????
	 *
	 * @param orderid
	 * @param goodsid
	 * @return
	 * @author whj
	 */
	@Override
	public List<PreferentialPrice> queryPreferentialPrice(String orderid,
                                                          int goodsid, String goods_p_url) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<PreferentialPrice> list = new ArrayList<PreferentialPrice>();
		String sql = "";
		String goods_p_itemid="0000";
		if(goods_p_url.indexOf("1688")>-1 || goods_p_url.indexOf("taobao")>-1){
			goods_p_itemid = Util.getItemid(goods_p_url);
		}
		try {
			sql = "select distinct c.begin,c.end,c.price,c.id,a.createtime,a.goods_p_itemid,a.goods_pid from preferential_goods_price c inner join preferential_goods a on c.pgid=a.id inner join order_product_source b on a.goods_url=b.goods_url where b.orderid=? and b.goodsid=? and a.is_delete=0 and c.is_delete=0 and c.goods_p_itemid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setInt(2, goodsid);
			stmt.setString(3, goods_p_itemid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				PreferentialPrice p = new PreferentialPrice();
				p.setBegin(rs.getInt("begin"));
				p.setCreatetime(rs.getString("createtime"));
				p.setEnd(rs.getInt("end"));
				p.setPrice(rs.getDouble("price"));
				p.setGoods_p_itemid(rs.getString("goods_p_itemid"));
				p.setGoods_pid(rs.getString("goods_pid"));
				p.setId(rs.getInt("id"));
				list.add(p);
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	/**
	 * ???????????????????????????????????????
	 *
	 * @param map
	 * @return ???????????????
	 */
	@Override
	public int updatePreferentialPrice(final Map<Object, Object> map) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int row = 0;
		String sql = "";
		try {
			if ("1".equals(map.get("type"))) {// ??????
				sql = "update preferential_goods_price set begin=?,end=?,price=? where id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, Integer.valueOf(map.get("new_begin").toString()));
				stmt.setInt(2, Integer.valueOf(map.get("new_end").toString()));
				stmt.setDouble(3,
						Double.valueOf(map.get("new_price").toString()));
				stmt.setInt(4, Integer.valueOf(map.get("id").toString()));
				row = stmt.executeUpdate();
			} else if ("2".equals(map.get("type"))) {// ??????
				sql = "update preferential_goods_price set is_delete=1 where id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, Integer.valueOf(map.get("id").toString()));
				row = stmt.executeUpdate();
			}
			new Thread() {
				PreparedStatement stmt = null;

				public void run() {
					try {
						String sql = "";
						if ("1".equals(map.get("type"))) {
							sql = "update preferential_goods_price set begin=?,end=?,price=? where begin=? and end=? and goods_p_itemid=?";
//							stmt = conn2.prepareStatement(sql);
							List<String> lstValues = Lists.newArrayList();
							lstValues.add(map.get("new_begin").toString());
							lstValues.add(map.get("new_end")
									.toString());
							lstValues.add(map.get(
									"new_price").toString());
							lstValues.add(map.get("old_begin")
									.toString());
							lstValues.add(map.get("old_end")
									.toString());
							lstValues.add(map.get("goods_p_itemid")
									.toString());
							String runSql = DBHelper.covertToSQL(sql, lstValues);
							SendMQ.sendMsg(new RunSqlModel(runSql));
//							stmt.executeUpdate();
						} else if ("2".equals(map.get("type"))) {
							sql = "update preferential_goods_price set is_delete=1 where begin=? and end=? and goods_p_itemid=?";
							/*stmt = conn2.prepareStatement(sql);
							stmt.setInt(1, Integer.valueOf(map.get("old_begin")
									.toString()));
							stmt.setInt(2, Integer.valueOf(map.get("old_end")
									.toString()));
							stmt.setString(3, map.get("goods_p_itemid")
									.toString());
							stmt.executeUpdate();*/
							List<String> lstValues = Lists.newArrayList();
							lstValues.add(map.get("old_begin")
									.toString());
							lstValues.add(map.get("old_end")
									.toString());
							lstValues.add(map.get("goods_p_itemid")
									.toString());
							String runSql = DBHelper.covertToSQL(sql, lstValues);
							SendMQ.sendMsg(new RunSqlModel(runSql));
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
//						DBHelper.getInstance().closeConnection(conn2);
					}
				};
			}.start();
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Deprecated
	@Override
	public int PurchaseComfirmOne127(String orderNo, int goodsid, int adminid) {
		int i = 0;
		String sql = "update order_product_source set confirm_userid=null,confirm_time=null,purchase_state=1 where orderid=? and goodsid=? and del=0";
		String sqltwo = "update order_details set purchase_state=0,purchase_time=null,purchase_confirmation=null where orderid=? and goodsid=?";
		String sqlthree = "update orderinfo set purchase_number=purchase_number-1 where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null, stmttwo = null, stmtinfo = null;
		try {
			/*stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodsid);
			stmt.executeUpdate();*/
			List<String> lstValues = Lists.newArrayList();
			lstValues.add(orderNo);
			lstValues.add(String.valueOf(goodsid));
			String runSql = DBHelper.covertToSQL(sql, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));
			
			/*stmttwo = conn.prepareStatement(sqltwo);
			stmttwo.setString(1, orderNo);
			stmttwo.setInt(2, goodsid);
			stmttwo.executeUpdate();*/
//			lstValues = Lists.newArrayList();
//			lstValues.add(orderNo);
//			lstValues.add(String.valueOf(goodsid));
			i++;
			
			runSql = DBHelper.covertToSQL(sqltwo, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));
			i++;
			
			lstValues = Lists.newArrayList();
			lstValues.add(orderNo);
			runSql = DBHelper.covertToSQL(sqlthree, lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));
			/*stmtinfo = conn.prepareStatement(sqlthree);
			stmtinfo.setString(1, orderNo);
			stmtinfo.executeUpdate();*/
			i++;
			this.order_state(conn, orderNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttwo != null) {
				try {
					stmttwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtinfo != null) {
				try {
					stmtinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;
	}

	@Deprecated
	@Override
	public int PurchaseComfirmTwo_crossshop(int userid, String orderNo,
	                                        int od_id, int goodid, int goodsdataid, int admid, String goodsurl,
	                                        String googsimg, String goodsprice, String goodstitle,
	                                        int googsnumber, String oldValue, String newValue, int purchaseCount) {
		// TODO Auto-generated method stub
		int i = 0;
		// String sql = "select count(id) as sum from order_change where
		// orderNo=? and goodId=?";
		String sqltwo = "update order_details set purchase_state=1,purchase_time=now(),purchase_confirmation=? where orderid=? and goodsid=?";
		String sqlthree = "update orderinfo set purchase_number=purchase_number+1 where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null, rse = null, rsb = null;
		PreparedStatement stmtb = null, stmtttt = null, stmtfind = null, stmt = null, stmtdel = null;
		PreparedStatement stmtinfo = null, stmttwo = null, stmtt = null, stmttt = null;
		int ii = 0;
		try {
			// stmt = conn.prepareStatement(sql);
			// stmt.setString(1, orderNo);
			// stmt.setInt(2, goodid);
			// stmttwo = conn.prepareStatement(sqltwo);
			// stmttwo.setString(1, this.getUserbyID(admid + ""));
			// stmttwo.setString(2, orderNo);
			// stmttwo.setInt(3, goodid);
			// rs = stmt.executeQuery();
			stmttwo.executeUpdate();
			// while (rs.next()) {
			// ii = rs.getInt("sum");
			// }
			if (ii > 1) {
				// String sqlsetdel = "update order_change set del_state=1 where
				// order_change.orderNo=? and order_change.goodId=?";
				// stmtdel = conn.prepareStatement(sqlsetdel);
				// stmtdel.setString(1, orderNo);
				// stmtdel.setInt(2, goodid);
				// stmtdel.executeUpdate();
			}
			// String sqll = "";
			// if (ii != 1) {
			// sqll = "insert into
			// order_change(order_change.oldValue,order_change.newValue,"
			// +
			// "order_change.orderNo,order_change.goodId,order_change.ropType,order_change.status,"
			// + "order_change.dateline,order_change.del_state)
			// values(?,?,?,?,6,0,now(),0);";
			// } else if (ii == 1) {
			// sqll = "update order_change set
			// order_change.oldValue=?,order_change.newValue=?,order_change.dateline=now(),"
			// + " order_change.ropType=6 where order_change.orderNo=? and
			// order_change.goodId=?";
			// }
			// stmtt = conn.prepareStatement(sqll);
			// stmtt.setString(1,oldValue);
			// stmtt.setString(2,newValue);
			// stmtt.setString(3,orderNo);
			// stmtt.setInt(4,goodid);
			// stmtt.executeUpdate();
			// i++;

			// ?????????????????????
			int res = 0;
			String sqlfind = "select count(id) as sid from order_product_source where orderid='"
					+ orderNo + "' and goodsid=" + goodid;
			stmtfind = conn.prepareStatement(sqlfind);
			rse = stmtfind.executeQuery();
			if (rse.next()) {
				res = rse.getInt("sid");
			}

			if (res == 0) {
				String sqlll = "insert into order_product_source(adminid,userid,addtime,orderid,confirm_userid,confirm_time,"
						+ "goodsid,goodsdataid,goods_url,goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,"
						+ "usecount,buycount,purchase_state,od_id,tborderid) values(?,?,now(),?,?,now(),?,?,?,?,?,?,?,?,?,?,3,?,'??????15') ";
				stmttt = conn.prepareStatement(sqlll);
				stmttt.setDouble(1, admid);
				stmttt.setInt(2, userid);
				stmttt.setString(3, orderNo);
				stmttt.setInt(4, userid);
				stmttt.setInt(5, goodid);
				stmttt.setInt(6, goodsdataid);
				stmttt.setString(7, goodsurl);
				stmttt.setString(8, newValue);
				stmttt.setString(9, googsimg);
				stmttt.setString(10, goodsprice);
				stmttt.setString(11, oldValue);
				stmttt.setString(12, goodstitle);
				stmttt.setInt(13, googsnumber);
				stmttt.setInt(14, purchaseCount);
				stmttt.setInt(15, od_id);
				stmttt.executeUpdate();
				i++;
			} else {
				String sqlll = "update order_product_source set confirm_userid="
						+ admid
						+ ",confirm_time=now(),purchase_state=3 "
						+ "where orderid='"
						+ orderNo
						+ "' and goodsid="
						+ goodid + " and del=0";
				stmttt = conn.prepareStatement(sqlll);
				stmttt.executeUpdate();
				i++;
			}
			int iib = 0;
			String sqlb = "select count(id) as sumb from goods_source where goods_url=? and goods_purl=? ";
			stmtb = conn.prepareStatement(sqlb);
			stmtb.setString(1, goodsurl);
			stmtb.setString(2, newValue);
			rsb = stmtb.executeQuery();
			i++;
			if (rsb.next()) {
				iib = rsb.getInt("sumb");
			}
			String sqllll = "";
			if (iib == 0) {// goods_source???//
				sqllll = "insert into goods_source(goodsdataid,goods_url,goods_purl,goods_img_url,"
						+ "goods_price,goods_name,moq,goodssourcetype,buycount,del,updatetime) "
						+ "values(?,?,?,?,?,?,1,1,?,0,now())";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setInt(1, goodsdataid);
				stmtttt.setString(2, goodsurl);
				stmtttt.setString(3, newValue);
				stmtttt.setString(4, googsimg);
				stmtttt.setString(5, oldValue);
				stmtttt.setString(6, goodstitle);
				stmtttt.setInt(7, purchaseCount);
			} else {
				sqllll = "update goods_source set goods_img_url=?,goods_price=?,buycount=?,"
						+ "updatetime=now() where goods_url=? and goods_purl=?";
				stmtttt = conn.prepareStatement(sqllll);
				stmtttt.setString(1, googsimg);
				stmtttt.setString(2, oldValue);
				stmtttt.setInt(3, purchaseCount);
				stmtttt.setString(4, goodsurl);
				stmtttt.setString(5, newValue);
			}
			stmtttt.executeUpdate();
			i++;
			stmtinfo = conn.prepareStatement(sqlthree);
			stmtinfo.setString(1, orderNo);
			stmtinfo.executeUpdate();
			i++;
			this.order_state(conn, orderNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rsb != null) {
				try {
					rsb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtdel != null) {
				try {
					stmtdel.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtb != null) {
				try {
					stmtb.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtttt != null) {
				try {
					stmtttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rse != null) {
				try {
					rse.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfind != null) {
				try {
					stmtfind.close();
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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttwo != null) {
				try {
					stmttwo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmttt != null) {
				try {
					stmttt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtinfo != null) {
				try {
					stmtinfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return i;

	}

	/*
	 * ?????????????????????order_details
	 */
	class PurchaseComfirmTwoHyqrThred extends Thread {
		private String orderNo;
		private Integer goodid;
		private String sqlc;
		PreparedStatement stmc = null, stmc2 = null;

		public PurchaseComfirmTwoHyqrThred(String sqlc, int goodid,
		                                   String orderNo) {
			super();
			this.orderNo = orderNo;
			this.goodid = goodid;
			this.sqlc = sqlc;
		}

		@Override
		public synchronized void run() {
			Connection conn2 = null;
			try {
				// ????????????????????????
				LOG.info("???????????????????????????order_details purchase_state?????????1");
				List<String> lstValues = Lists.newArrayList();
				lstValues.add(orderNo);
				lstValues.add(String.valueOf(goodid));
				String runSql = DBHelper.covertToSQL(sqlc, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (stmc2 != null) {
					stmc2.close();
				}
				if (conn2 != null) {
					conn2.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param map
	 *            ????????????????????????????????????
	 * @author ????????? 2017-07-26
	 */
	@Override
	public String saveRepalyContent(Map<String, String> map) {
		String rk = "";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select context from goods_communication_info where odid=? and orderid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, map.get("odid"));
			stmt.setString(2, map.get("orderid"));
			rs = stmt.executeQuery();
			if (rs.next()) {
				String context = rs.getString("context");
				context += "<br>" + map.get("text");
				if("2".equals(map.get("type"))){
					//??????????????????
					sql = "update goods_communication_info set context=?,is_read=0,goodsid=? where odid=? and orderid=?";
				}else{
					//??????????????????
					sql = "update goods_communication_info set context=?,is_read_sale=0,goodsid=? where odid=? and orderid=?";
				}
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, context);
				stmt.setInt(2, Integer.valueOf(map.get("goodsid")));
				stmt.setString(3, map.get("odid"));
				stmt.setString(4, map.get("orderid"));
				stmt.executeUpdate();
				rk = context;
			} else {
				if("1".equals(map.get("type"))){
					sql = "insert into goods_communication_info (context,orderid,odid,goodsid,create_time,is_read) values(?,?,?,?,now(),?)";
				}else{
					sql = "insert into goods_communication_info (context,orderid,odid,goodsid,create_time,is_read_sale) values(?,?,?,?,now(),?)";
				}
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, map.get("text"));
				stmt.setString(2, map.get("orderid"));
				stmt.setString(3, map.get("odid"));
				stmt.setInt(4, Integer.valueOf(map.get("goodsid")));
				stmt.setInt(5,1);
				stmt.executeUpdate();
				rk = map.get("text");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return rk;
	}

	/**
	 * ?????????????????????????????????????????????samplinginfomation
	 */
	@Override
	public int insertDateToAliInfoData(String od_id,String name) {
		int row=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql="";
		try{
			sql="SELECT od.orderid,od.goodsid,c.shop_id,od.goods_pid,od.car_type,od.car_img,c.enname,c.price"
					+" FROM order_details od "
					+" INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid "
					+" WHERE od.id="+od_id+"";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				sql="insert into samplinginfomation (shop_id,ali_name,ali_url,img,price,sku,goods_pid,od_id,admuser,createtime,goods_purl) values"
						+ "(?,?,?,?,?,?,?,?,?,now(),?)";
				stmt=conn.prepareStatement(sql);
				stmt.setString(1, rs.getString("shop_id"));
				stmt.setString(2, "");
				stmt.setString(3, "");
				stmt.setString(4, rs.getString("car_img"));
				stmt.setString(5, rs.getString("price"));
				stmt.setString(6, rs.getString("car_type"));
				stmt.setString(7, rs.getString("goods_pid"));
				stmt.setString(8, od_id);
				stmt.setString(9, name);
				stmt.setString(10,"https://detail.1688.com/offer/"+rs.getString("goods_pid")+".html");
				row=stmt.executeUpdate();
				if(row>0){
					sql="update order_product_source set sampling_flag=1 where orderid=? and goodsid=?";
					stmt=conn.prepareStatement(sql);
					stmt.setString(1, rs.getString("orderid"));
					stmt.setString(2, rs.getString("goodsid"));
					row=stmt.executeUpdate();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return row;
	}

	@Override
	public String getUserName(String name) {
		String account = "";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.account FROM tb_1688_accounts a INNER JOIN admuser b ON a.adminid=b.id WHERE b.admName LIKE '%"
				+ name + "%'";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				account = rs.getString("account");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return account;
	}
	/**
	 * ????????????????????????
	 */
	@Override
	public int useInventory(Map<String, String> map) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int row = 0;
		String sql="";
		try{
			if("1".equals(map.get("isUse"))){
				//????????????
				sql="update lock_inventory set is_use=1 where od_id="+map.get("od_id")+"";
			}else{
				sql="UPDATE inventory AS a LEFT JOIN lock_inventory AS b ON a.id=b.in_id SET a.can_remaining=a.can_remaining+b.lock_remaining WHERE a.id=b.in_id  and b.od_id="+map.get("od_id")+"";
				stmt=conn.prepareStatement(sql);
				stmt.executeUpdate();
				//???????????????????????????????????????????????????????????????????????????????????????????????????
				sql="SELECT i.goods_pid FROM inventory i INNER JOIN lock_inventory li ON i.id=li.in_id WHERE li.od_id='"+map.get("od_id")+"' LIMIT 1";
				stmt=conn.prepareStatement(sql);
				rs=stmt.executeQuery();
				if(rs.next()){
					String goods_pid=rs.getString("goods_pid");
					//??????
					sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
					stmt = conn.prepareStatement(sql);
					stmt.executeUpdate();
					//????????????
					Connection conn28 = DBHelper.getInstance().getConnection8();
					sql="update custom_benchmark_ready_newest set is_stock_flag=1 where pid='"+goods_pid+"'";
					stmt = conn28.prepareStatement(sql);
					stmt.executeUpdate();
					DBHelper.getInstance().closeConnection(conn28);
					//?????????
					GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"1");
				}
				sql="update order_details set inventory_remark='?????????????????????' where id="+map.get("od_id")+"";
				stmt=conn.prepareStatement(sql);
				stmt.executeUpdate();
				sql="update lock_inventory set is_delete=1 where od_id="+map.get("od_id")+"";
			}
			stmt=conn.prepareStatement(sql);
			stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return row;
	}


	/**
	 * ??????order_details  straight_flag???2  ????????????
	 */
	@Override
	public int determineStraighthair(Map<Object, Object> map) {
		int row=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try{
			String sql="update order_details set straight_flag=2,straight_time=now() where orderid='"+map.get("orderid")+"' and goodsid='"+map.get("goodsid")+"'";
			stmt=conn.prepareStatement(sql);
			row=stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		DBHelper.getInstance().closeConnection(conn);
		return row;
	}
	/**
	 * ????????????????????????
	 */
	@Override
	public int OfflinePaymentApplication(Map<String, String> map) {
		Connection conn = DBHelper.getInstance().getConnection();
		DecimalFormat df = new DecimalFormat("########0.00");
		int row[] = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try{
			stmt = conn.prepareStatement("insert into offline_payment_application (orderid,goodsid,car_type,car_img,buycount,goods_p_price,applicantName,createtime,amount,off_remark) values(?,?,?,?,?,?,?,now(),?,?)");
			String str[]=map.get("list").split("&");
			for (String s : str) {
				String orderid=s.split(",")[0];
				String goodsid=s.split(",")[1];
				String sql1="select od.car_type,od.car_img,ops.buycount,ops.goods_p_price from order_details od inner join order_product_source ops on od.orderid=ops.orderid and od.goodsid=ops.goodsid where od.orderid='"+orderid+"' and od.goodsid='"+goodsid+"'";
				stmt1=conn.prepareStatement(sql1);
				rs1=stmt1.executeQuery();
				if(rs1.next()){
					stmt.setString(1,orderid);
					stmt.setString(2, goodsid);
					stmt.setString(3,rs1.getString("car_type"));
					stmt.setString(4, rs1.getString("car_img"));
					stmt.setInt(5,rs1.getInt("buycount"));
					stmt.setDouble(6, rs1.getDouble("goods_p_price"));
					stmt.setString(7,map.get("admName"));
					stmt.setString(8,df.format(rs1.getInt("buycount")*rs1.getDouble("goods_p_price")));
					stmt.setString(9, map.get("off_remark")==null?"":map.get("off_remark"));
					stmt.addBatch();
				}
			}
			row =stmt.executeBatch();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null){
					stmt.close();
				}
				if(stmt1!=null){
					stmt1.close();
				}
				if(rs1!=null){
					rs1.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return row.length>0?1:0;
	}

	@Override
	public int insertSources(Map<String, String> map) {
		Connection conn = DBHelper.getInstance().getConnection();
		int row = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer orderid = new StringBuffer();
		try {
			String itemid = Util.getItemid(map.get("taobao_url"));
			if (itemid == null || "".equals(itemid)) {
				itemid = "0000";
			}
			if (map.get("shipno") == null || map.get("shipno").equals("")) {
				map.remove("shipno");
				StringBuffer orderid1 = new StringBuffer();
				for (int i = 0; i < 7; i++) {
					orderid1.append((int) (Math.random() * 100));
				}
				map.put("shipno", orderid1 + itemid);
				for (int i = 0; i < 7; i++) {
					orderid.append((int) (Math.random() * 100));
				}
			} else {
				String sql1 = "select orderid from taobao_1688_order_history where shipno='"
						+ map.get("shipno") + "' limit 1";
				stmt = conn.prepareStatement(sql1);
				rs = stmt.executeQuery();
				if (rs.next()) {
					orderid.append(rs.getString("orderid"));
				} else {
					for (int i = 0; i < 7; i++) {
						orderid.append((int) (Math.random() * 100));
					}
				}
			}
			String sql = "select id from taobao_1688_order_history where importOrderid='"
					+ map.get("TbOrderid")
					+ "' and importGoodsid='"
					+ map.get("TbGoodsid") + "'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				sql = "update taobao_1688_order_history set totalprice='"
						+ map.get("totalprice") + "',itemurl='"
						+ map.get("taobao_url") + "',preferential='"
						+ map.get("preferential") + "',itemprice='"
						+ map.get("taobaoPrice") + "',shipno='"
						+ map.get("shipno") + "',itemid='" + itemid
						+ "' where importOrderid='" + map.get("TbOrderid")
						+ "' and importGoodsid='" + map.get("TbGoodsid") + "'";
				row = 1;
			} else {
				sql = "insert into taobao_1688_order_history (importGoodsid,importOrderid,tbOr1688,orderid,totalprice,itemname,itemid,itemprice,itemqty,sku,shipno,itemurl,username,preferential,paydata,imgurl,shipper,shipstatus,creattime,delivery_date,orderstatus,orderdate,off_line) values"
						+ "('"
						+ map.get("TbGoodsid")
						+ "','"
						+ map.get("TbOrderid")
						+ "',2,'"
						+ orderid.toString()
						+ "','"
						+ map.get("totalprice")
						+ "','"
						+ map.get("taobao_name")
						+ "','"
						+ itemid
						+ "','"
						+ map.get("taobaoPrice")
						+ "','"
						+ map.get("goodsQty")
						+ "','"
						+ map.get("goods_sku")
						+ "','"
						+ map.get("shipno")
						+ "','"
						+ map.get("taobao_url")
						+ "','"
						+ map.get("username")
						+ "',"
						+ map.get("preferential")
						+ ",now(),'"
						+ map.get("goods_imgs")
						+ "','???','????????????????????????',now(),now(),'????????????????????????',now(), 1)";
				row = 2;
			}
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			sql = "update order_product_source set offline_purchase=1 where orderid='"
					+ map.get("TbOrderid")
					+ "' and goodsid='"
					+ map.get("TbGoodsid") + "'";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			sql = "insert into offline_purchase_records (shipno,createtime,admuserid,goods_p_url,tb_orderid,goodsid,orderid) values (?,now(),?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, map.get("shipno"));
			stmt.setString(2, map.get("adminid"));
			stmt.setString(3, map.get("taobao_url"));
			stmt.setString(4, orderid.toString());
			stmt.setString(5, map.get("TbGoodsid"));
			stmt.setString(6, map.get("TbOrderid"));
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			row = 3;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return row;
	}

	@Override
	public void notePurchaseAgain(String orderNo, int od_id, int goodsid,
	                              String remarkContent) {

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String updatesql = "update order_product_source  set againRemarks = if(isnull(againRemarks),?,CONCAT(againRemarks,';',?))  where orderid= ? and od_id=? ;";
		// String querySql =
		// "select againRemarks from order_product_source where orderid='" +
		// orderNo + "' and od_id=" + od_id
		// + " and goodsid=" + goodsid;
		try {
			stmt = conn.prepareStatement(updatesql);
			stmt.setString(1, remarkContent);
			stmt.setString(2, remarkContent);
			stmt.setString(3, orderNo);
			stmt.setInt(4, od_id);
			stmt.executeUpdate();
			/*
			 * ResultSet resultSet = stmt.executeQuery(querySql); String
			 * updateSql = ""; if (resultSet.next()) { String resRemark =
			 * resultSet.getString("againRemarks"); // remark?????????'' if
			 * (StrUtils.isNullOrEmpty(resRemark)) { updateSql =
			 * "update order_product_source set remark=concat('???????????????','" +
			 * remarkContent + "','//,') " + "where remark= '' and orderid='" +
			 * orderNo + "' and od_id=" + od_id + " and goodsid=" + goodsid; }
			 * else { // ????????????????????????????????????"???????????????"????????????????????????????????? int beginIndex =
			 * resRemark.indexOf("???????????????"); if (beginIndex >= 0) { String preStr
			 * = resRemark.substring(0, beginIndex);// ???????????????????????? String nwString
			 * = resRemark.substring(beginIndex).replace("//", "").replace(",",
			 * "");// "???????????????"?????????????????? // ??????????????????remark?????????remark?????????null??? updateSql =
			 * "update order_product_source set remark='" + preStr + nwString +
			 * ";" + remarkContent + "//,'" + "where remark != '' and orderid='"
			 * + orderNo + "' and od_id=" + od_id + " and goodsid=" + goodsid; }
			 * else { // ??????????????????"???????????????"?????????remark?????????remark?????????null??? updateSql =
			 * "update order_product_source set remark=concat(remark,concat('???????????????','"
			 * + remarkContent + "'),'//,') " +
			 * "where remark != '' and orderid='" + orderNo + "' and od_id=" +
			 * od_id + " and goodsid=" + goodsid; } } }
			 * stmt.executeUpdate(updateSql);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("update order_product_source error,reason is "
					+ e.getMessage());
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

	}

	@Override
	public UserOrderDetails getUserAddr(String orderid) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select oa.address, oa.street  from  order_address oa  INNER JOIN order_fee of on oa.orderNo = of.orderno and of.orderno = ? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			rs = stmt.executeQuery();
			UserOrderDetails uod = new UserOrderDetails();
			while (rs.next()) {
				uod.setAddress(rs.getString("address"));
				uod.setUserstreet(rs.getString("street"));
			}
			return uod;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			DBHelper.getInstance().closeConnection(conn);
		}
		return null;
	}

	private String supplierScoringLevel(String shop_id){
		// TODO Auto-generated method stub
		String sql = "select level from supplier_scoring where shop_id = ? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String level="";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, shop_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				level = rs.getString("level");
			}
			return level;
		} catch (Exception e) {

		}finally{
			DBHelper.getInstance().closeConnection(conn);
		}
		return null;
	}



}
