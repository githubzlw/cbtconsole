package com.cbt.website.dao;

import com.cbt.bean.UserBean;
import com.cbt.bean.goodsCarPresale;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.TypeUtils;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.CurrencyService;
import com.cbt.processes.service.ICurrencyService;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.util.UUIDUtil;
import com.cbt.website.bean.TblPreshoppingcarInfo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.Utility;
import com.ibm.icu.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class shoppingCartDaoImpl implements shoppingCartDao {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(shoppingCartDaoImpl.class);
	
	
	@Override
	public List<Object[]> getAllUserShopCarList(int userid,Admuser admuser,int beginPage,int pageSize,int isorder,int status) {
//		public List<Object[]> getAllUserShopCarList(int userid) {
		StringBuffer sql = new StringBuffer();
				
			//	"select sql_calc_found_rows u.id,u.email,count(distinct gc.goodsdata_id) as species,SUM(gc.googs_price * gc.googs_number) as totalAmount, (SUM(gc.googs_price * gc.googs_number) / sum(gc.googs_number)) as avgPrice ,gdcf.updateTime as lastAccTime,u.currency,au.adminid,au.admName from goods_car gc LEFT JOIN user u on gc.userid = u.id LEFT JOIN admin_r_user au on gc.userid=au.userid where 1 = 1 and u.id != 0 and gc.state = 0 ");
		if(status ==3){//未处理
				sql.append("select sql_calc_found_rows u.id,u.email,count(distinct gc.catid) as species," +
						"SUM(gc.googs_price * gc.googs_number) as totalAmount, " +
						"(SUM(gc.googs_price * gc.googs_number) / sum(gc.googs_number)) as avgPrice ,gdcf.updateTime as lastAccTime," +
						"u.currency,au.adminid,au.admName,gc.datatime from shop_car_marketing gc LEFT JOIN user u on gc.userid = u.id LEFT JOIN " +
						"admin_r_user au on gc.userid=au.userid where 1 = 1 and u.id != 0 and gc.state = 0 AND " +
						"gc.datatime>=DATE_SUB(NOW(), INTERVAL 7 DAY)  "
						+ " and u.id NOT IN (select userId from cart_market_email_send cmes WHERE " +
						"cmes.sentEmailTime >= DATE_SUB(NOW(), INTERVAL 7 DAY) GROUP BY userId ORDER BY cmes.sentEmailTime desc)");
		}else if(status ==2){
			   sql.append("select sql_calc_found_rows u.id,u.email,count(distinct gc.catid) as species,SUM(gc.googs_price * gc.googs_number) " +
					   "as totalAmount, (SUM(gc.googs_price * gc.googs_number) / sum(gc.googs_number)) as avgPrice ," +
					   "gdcf.updateTime as lastAccTime,u.currency,au.adminid,au.admName,gc.datatime from shop_car_marketing gc " +
					   "LEFT JOIN user u on gc.userid = u.id LEFT JOIN admin_r_user au on gc.userid=au.userid where 1 = 1 " +
					   "and gc.userid > 0 and gc.state = 0 AND gc.datatime>=DATE_SUB(NOW(), INTERVAL 7 DAY)");
		}else{
			sql.append("select sql_calc_found_rows u.id,u.email,count(distinct gc.catid) as species,SUM(gc.googs_price * gc.googs_number) " +
					"as totalAmount, (SUM(gc.googs_price * gc.googs_number) / sum(gc.googs_number)) as avgPrice ,gdcf.updateTime as lastAccTime," +
					"u.currency,au.adminid,au.admName from shop_car_marketing gc LEFT JOIN user u on gc.userid = u.id LEFT JOIN admin_r_user au " +
					"on gc.userid=au.userid left join goods_carconfig gdcf on gc.userid = gdcf.userid where 1 = 1 and gc.userid > 0 and gc.state = 0 ");
		}		
		if (admuser != null) {
			if ("1".equals(admuser.getRoletype())) {
				sql.append(" and au.adminid = "+admuser.getId());
			}
		}
		if (isorder == 1) {//是否下单
			sql.append(" and gc.userid not in(select DISTINCT user_id from orderinfo)");
		}
		if (userid != 0) {
			sql.append(" and gc.userid = " + userid);
			sql.append(" and gc.state = 0 group by gc.userid order by lastAccTime desc");
		}else{
			sql.append(" and gc.state = 0 group by gc.userid order by lastAccTime desc limit "+beginPage+","+pageSize);
		}
		System.out.println(status+"--sql-->"+sql);
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		int total = 0;
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt2 = conn.prepareStatement("select found_rows();");
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			DecimalFormat df = new DecimalFormat("0.00");
			while (rs2.next()) {
				total = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				Object[] obj = new Object[10];
				obj[0] = rs.getInt("id");
				obj[1] = rs.getString("email");
				obj[2] = rs.getInt("species");
				obj[3] = df.format(rs.getFloat("totalAmount"));
				obj[4] = df.format(rs.getFloat("avgPrice"));
				obj[5] = rs.getDate("lastAccTime");
				obj[6] = rs.getString("currency");
				obj[7] = total;
				obj[8] = rs.getInt("adminid");
				obj[9] = rs.getString("admName");				
				list.add(obj);
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
	
	@Override
	public int getAllUserShopCarListCount(int userid) {
		StringBuffer sql = new StringBuffer(
				" select count(*) as count from (select u.id,u.email,count(distinct gc.catid) as species,SUM(gc.googs_price * gc.googs_number) as totalAmount, (SUM(gc.googs_price * gc.googs_number) / sum(gc.googs_number)) as avgPrice ,gdcf.updateTime as lastAccTime,u.currency from goods_car gc LEFT JOIN user u on gc.userid = u.id where 1 = 1 and u.id != 0 and gc.state = 0 ");
		if (userid != 0) {
			sql.append("and gc.userid = " + userid);
		}
		sql.append(" and gc.state = 0 group by gc.userid order by gc.userid) t");
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int total = 0;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				total=rs.getInt("count");
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
		return total;
	}

	@Override
	public List<Object[]> getShopCarListFromUser(int userid, String goodcarid) {
		/*StringBuffer sql = new StringBuffer("select DISTINCT(gc.id),gc.goodsdata_id,gc.goods_title,gc.goods_type,gc.googs_price,gc.googs_number,gt.img as smallimg, ifnull(gd.weight,0.00) as weight, ifnull(gd.width,'') as width,");
		sql.append(" gd.img,gc.currency,gc.goods_url,gc.preferential,gdc.tbname,gdc.tbprice,gdc.tbimg,gdc.tburl,gdc.tbname1,gdc.tbprice1,gdc.tbimg1,gdc.tburl1,");
		sql.append(" gdc.tbname2,gdc.tbprice2,gdc.tbimg2,gdc.tburl2,gdc.tbname3,gdc.tbprice3,gdc.tbimg3,gdc.tburl3, cd.price,cd.deposit_rate" ); // , (gc.googs_number*gc.googs_price) as money ");
		sql.append(" from goods_car gc LEFT JOIN goodsdatacheck gdc on gc.goodsdata_id = gdc.id LEFT JOIN goodsdata gd on gc.goodsdata_id = gd.id LEFT JOIN goods_typeimg gt on gc.id = gt.goods_id ");
		sql.append(" LEFT JOIN class_discount cd on gc.goods_class = cd.id where 1 = 1 ");*/
		StringBuffer sql = new StringBuffer("select DISTINCT(gc.id),gc.goodsdata_id,gc.goods_title,gc.goods_type,gc.price3,gc.notfreeprice,gc.googs_number,gt.img as smallimg, ifnull(gc.total_weight,0.00) as weight, ifnull(gc.width,'') as width,");
		sql.append("gc.googs_img img, gc.currency,gc.goods_url,gc.preferential,gdc.tbname,gdc.tbprice,gdc.tbimg,gdc.tburl,gdc.tbname1,gdc.tbprice1,gdc.tbimg1,gdc.tburl1, ");
		sql.append("gdc.tbname2,gdc.tbprice2,gdc.tbimg2,gdc.tburl2,gdc.tbname3,gdc.tbprice3,gdc.tbimg3,gdc.tburl3, cd.price,cd.deposit_rate,gc.freight,cd.showname,cd.deposit_rate  ");
		sql.append("from goods_car gc LEFT JOIN goodsdatacheck gdc on gc.goods_url = gdc.url  ");
		sql.append("LEFT JOIN goods_typeimg gt on gc.id = gt.goods_id  ");
		sql.append("LEFT JOIN class_discount cd on gc.goods_class = cd.id where 1 = 1 ");
		if (userid != 0) {
			sql.append("and gc.userid = " + userid);
		}
		if (goodcarid != null) {
			sql.append(" and gc.id in (" + goodcarid + ") ");
		}
		sql.append(" and gc.state = 0 ");
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			ICurrencyService currencyDao = new CurrencyService();
			DecimalFormat df = new DecimalFormat("0.00");
			Double rate = currencyDao.currencyConverter("RMB");
//			Double exchange_rate = 0.0;
			
			while (rs.next()) {
				Object[] obj = new Object[37];
				obj[0] = rs.getInt("goodsdata_id");
				obj[1] = rs.getString("goods_title");
				if ("".equals(rs.getString("goods_type")) || rs.getString("goods_type") == null || rs.getString("goods_type").indexOf(",")<0 || rs.getString("goods_type").indexOf("@")<0) {
					obj[2] = rs.getString("goods_type");
				} else {
					obj[2] = Utility.TypeMatch(rs.getString("goods_type"));
				}
				//exchange_rate = rate/currencyDao.currencyConverter(rs.getString("currency"));
				obj[3] = rs.getFloat("price3");
				obj[27] = df.format(rs.getFloat("price3") * rate);
				// 部分购物车preferential的值超过int范围
				int pref = -1;
				String preferential = rs.getString("preferential");
				if(StringUtils.isNotBlank(preferential)){
					pref = preferential.length() > 9 ? -1 : Integer.valueOf(preferential);
				}
				obj[28] = pref;
				//obj[28] = rs.getInt("preferential");
				obj[4] = rs.getString("currency");
				if (rs.getString("img") != null || !"".equals(rs.getString("img"))) {
					obj[5] = rs.getString("img").substring(0, rs.getString("img").indexOf(".jpg")+4);
//					obj[5] = rs.getString("img").replace("[", "").replace("]", "");
//				} else {
//					obj[5] = rs.getString("img");
				}
				obj[6] = rs.getString("goods_url");
				obj[7] = rs.getString("tbname");

//				if (goodcarid != null) {
//					obj[8] = rs.getDouble("tbprice");
//				} else {
					obj[8] = df.format(rs.getDouble("tbprice") / rate);
//				}
				if (rs.getString("tbimg") == null
						|| "".equals(rs.getString("tbimg"))) {
					obj[9] = rs.getString("tbimg");
				} else {
					obj[9] = rs.getString("tbimg").replace("[", "")
							.replace("]", "");
				}
				obj[10] = rs.getString("tburl");

				obj[11] = rs.getString("tbname1");
//				if (goodcarid != null) {
//					obj[12] = rs.getDouble("tbprice1");
//				} else {
					obj[12] = df.format(rs.getDouble("tbprice1")/rate);
//				}
				if (rs.getString("tbimg1") == null
						|| "".equals(rs.getString("tbimg1"))) {
					obj[13] = rs.getString("tbimg1");
				} else {
					obj[13] = rs.getString("tbimg1").replace("[", "")
							.replace("]", "");
				}
				obj[14] = rs.getString("tburl1");

				obj[15] = rs.getString("tbname2");
//				if (goodcarid != null) {
//					obj[16] = rs.getDouble("tbprice2");
//				} else {
					obj[16] = df.format(rs.getDouble("tbprice2")/rate);
//				}
				if (rs.getString("tbimg2") == null
						|| "".equals(rs.getString("tbimg2"))) {
					obj[17] = rs.getString("tbimg2");
				} else {
					obj[17] = rs.getString("tbimg2").replace("[", "")
							.replace("]", "");
				}
				obj[18] = rs.getString("tburl2");

				obj[19] = rs.getString("tbname3");
//				if (goodcarid != null) {
//					obj[20] = rs.getDouble("tbprice3");
//				} else {
					obj[20] = df.format(rs.getDouble("tbprice3")/rate);
//				}
				if (rs.getString("tbimg3") == null
						|| "".equals(rs.getString("tbimg3"))) {
					obj[21] = rs.getString("tbimg3");
				} else {
					obj[21] = rs.getString("tbimg3").replace("[", "")
							.replace("]", "");
				}
				obj[22] = rs.getString("tburl3");

				obj[23] = "";
				if ("".equals(rs.getString("smallimg")) || rs.getString("smallimg") == null) {
					obj[24] = rs.getString("smallimg");
				} else {
					obj[24] = Utility.ImgMatch(rs.getString("smallimg"));
				}
				obj[25] = rs.getInt("id");
				obj[26] = rs.getInt("googs_number");
				
				obj[29] = rs.getDouble("price"); //多少金额进行折扣
				obj[30] = rs.getDouble("deposit_rate"); //折扣率
				obj[31] = rs.getDouble("weight"); //重量
				String width = rs.getString("width"); //体积
				double tjz = 0.0;
				if(!"".equals(width)){
					String str[] = width.split("x");
					String l = str[0].trim().substring(0, str[0].trim().indexOf("cm"));
					String w = str[1].trim().substring(0, str[1].trim().indexOf("cm"));
					String h = str[2].trim().substring(0, str[2].trim().indexOf("cm"));
					tjz = (Double.parseDouble(l)/100.0) * (Double.parseDouble(w)/100.0) * (Double.parseDouble(h)/100.0) * 200;
				}
				obj[32] = tjz;
				obj[33] = rs.getDouble("freight");
				obj[34] = rs.getString("showname");
				obj[35] = rs.getDouble("deposit_rate");
				obj[36] = rs.getDouble("notfreeprice");
				list.add(obj);
			}
			List<Object[]> GoodSourceList=  getPurchasedSupplyFromGoodDataUrl(userid,rate);
			for (int i = 0; i < list.size(); i++) {
				Object[] gcobj = list.get(i);
				List<Object[]> gsl = new ArrayList<Object[]>();
				for (int j = 0; j < GoodSourceList.size(); j++) {
					Object[] gsobj = GoodSourceList.get(j);
					if (gcobj[6].equals(gsobj[4])) {
						gsl.add(gsobj);
					}
				}
				if (gsl.size()>0) {
					gcobj[23] = gsl;
				}
				gcobj[6] = changegoodUrl(gcobj[6]+"");
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
	
	
	
	/**
	 * 复制购物车数据到营销表
	 * @param userid
	 * @param goodcarid
	 * @throws Exception 
	 */
	public int copyGoodsCarData(int userid,String goodcarid,String countList,String oldprice,String newprice,String reduPrice) throws Exception{
		StringBuffer sql = new StringBuffer("insert into goods_car_market(goodscar_id,goodsdata_id,userid,sessionid,catid,itemId,shopId,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,delivery_time,norm_most,norm_least,state,remark,datatime,flag,pWprice,true_shipping,freight_free,width,perWeight,seilUnit,goodsUnit,bulk_volume,total_weight,per_weight,goods_email,free_shopping_company,free_sc_days,preferential,deposit_rate,guid,goods_type,feeprice,currency,goods_class,extra_freight,source_url,isshipping_promote,method_feight,price1,price2,price3,notfreeprice,theproductfrieght,isvolume,freeprice,firstprice,firstnumber,updatetime,addPrice,isFeight,isBattery,aliPosttime) select id,goodsdata_id,userid,sessionid,catid,itemId,shopId,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,delivery_time,norm_most,norm_least,state,remark,datatime,flag,pWprice,true_shipping,freight_free,width,perWeight,seilUnit,goodsUnit,bulk_volume,total_weight,per_weight,goods_email,free_shopping_company,free_sc_days,preferential,deposit_rate,guid,goods_type,feeprice,currency,goods_class,extra_freight,source_url,isshipping_promote,method_feight,price1,price2,price3,notfreeprice,theproductfrieght,isvolume,freeprice,firstprice,firstnumber,updatetime,addPrice,isFeight,isBattery,aliPosttime from goods_car where 1 = 1 and state = 0");
		if (userid != 0) {
			sql.append(" and userid = " + userid);
		}
		if (!goodcarid.isEmpty()) {
			sql.append(" and id in (" + goodcarid+") ");
		}
		StringBuffer presql = new StringBuffer("insert into goods_car_presale(user_id,goods_car_market_id,original_price,cost_price,discount_price) values (");
		presql.append(userid+",'"+goodcarid+"',"+new BigDecimal(oldprice)+","+new BigDecimal(newprice)+","+new BigDecimal(reduPrice)+")");
		
		Connection conn = DBHelper.getInstance().getConnection2();
		conn.setAutoCommit(false);
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.execute();
			stmt = conn.prepareStatement(presql.toString());
			stmt.execute();
			String[] goodcarsplit = goodcarid.split(",");
			String[] countsplit = countList.split(",");
			for (int i = 0; i < goodcarsplit.length; i++) {
				String numbersql = "update goods_car_market set googs_number = " + Integer.parseInt(countsplit[i]) + " where goodscar_id = " + goodcarsplit[i];
				stmt = conn.prepareStatement(numbersql);
				stmt.executeUpdate();
			}
			conn.commit();
			return 1;
		}catch(Exception e){
//			e.printStackTrace();
			conn.rollback();
			return 0;
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
			DBHelper.getInstance().closeConnection(conn);
		}
	}
	
	
	
	
	public List<Object[]> getSelectShopCarListFromUser(int userid,String goodcarid){
		StringBuffer sql = new StringBuffer("select gc.id,gc.googs_number,gc.price3,gc.freight,gc.total_weight,gc.bulk_volume,u.countryId,gc.catid from goods_car gc left join user u on gc.userid = u.id where 1 = 1 and gc.state = 0 ");
		if (userid != 0) {
			sql.append("and gc.userid = " + userid);
		}
		if (!goodcarid.isEmpty()) {
			sql.append(" and gc.id in (" + goodcarid+") ");
		}
		
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				Object[] obj = new Object[8];
				obj[0]=rs.getInt("id");
				obj[1]=rs.getInt("googs_number");
				obj[2]=rs.getDouble("price3");
				obj[3]=rs.getDouble("freight");
				obj[4]=rs.getString("total_weight");
				obj[5]=rs.getString("bulk_volume");
				obj[6]=rs.getInt("countryId");
				obj[7]=rs.getString("catid");
				list.add(obj);
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	
	
	/**
	 * 修改价格
	 */
	public int updateGoodsCarPrice(List<Integer> carid, List<Double> price){
		String sql = "update goods_car set notfreeprice=? where id=? ;";
		Connection con = DBHelper.getInstance().getConnection();
		Connection conn2 = DBHelper.getInstance().getConnection2();
		PreparedStatement pst = null,pst2 = null;;
		int i = 0;
		try {
			pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pst2 = conn2.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			for(int j=0;j<carid.size();j++){
				pst.setDouble(1, price.get(j));
				pst.setInt(2, carid.get(j));
				pst.addBatch();
				
				pst2.setDouble(1, price.get(j));
				pst2.setInt(2, carid.get(j));
				pst2.addBatch();
			}
			
			int[] arr = pst.executeBatch();
			
			int[] brr = pst2.executeBatch();
			i = arr.length+brr.length;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst2!=null){
				try {
					pst2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(con);
			DBHelper.getInstance().closeConnection(conn2);
		}
		return i;
	}

	@Override
	public List<Object[]> getPurchasedSupplyFromUserId(int userid) {
		String sql = "select goodsdataid,goods_name,goods_img_url,goods_url,goods_price from goods_source where goodsdataid in (select distinct goodsdata_id from goods_car where userid = ? and goodsdataid != 0 and state = 0)";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = rs.getInt("goodsdataid");
				obj[1] = rs.getString("goods_name");
				obj[2] = rs.getString("goods_img_url");
				obj[3] = rs.getString("goods_url");
				obj[4] = rs.getDouble("goods_price");
				list.add(obj);
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

	@Override
	public List<Object[]> getPurchasedSupplyFromGoodDataUrl(int userid,Double rate) {
		//String sql = "select goodsdataid,goods_name,goods_img_url,goods_purl,goods_price from goods_source where goods_url = ? ";
		String sql = 	"select goodsdataid,goods_name,goods_img_url,goods_purl,goods_url,goods_price from goods_source where goods_url in (select distinct goods_url from goods_car where userid =? and  state = 0) ORDER BY id desc limit 2";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Object[]> list = new ArrayList<Object[]>();
		
		ICurrencyService currencyDao = new CurrencyService();
		DecimalFormat df = new DecimalFormat("0.00");
		//Double rate = currencyDao.currencyConverter("RMB");
//		Double exchange_rate = rate/currencyDao.currencyConverter(currency);
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Object[] obj = new Object[6];
				obj[0] = rs.getInt("goodsdataid");
				obj[1] = rs.getString("goods_name");
				obj[2] = rs.getString("goods_img_url");
				obj[3] = rs.getString("goods_purl");
				obj[4] = rs.getString("goods_url");
				obj[5] = df.format(rs.getDouble("goods_price")/rate);
				list.add(obj);
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
	
	
	//发送购物车营销邮件
	public void sendGoodcarPreEmail(int userid,String caridStr,String oldprice,String newprice,String reduPrice){
		IUserDao userDao = new UserDao();
		UserBean user = userDao.getUserFromId(userid);
		//拼接邮件内容
		StringBuffer od = new StringBuffer();
		List<Object[]> list = spliceEmail(userid, caridStr);
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = list.get(i);
			od.append("<tr>");
		      od.append("<td><div style='width: 350px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>" + obj[2] + "</a></div><div>");

		      String[] type = null;
		      od.append("<img style='cursor: pointer' width='50px;' height='50px;' src='" + obj[10] + "'></img>");
		      if (obj[3] != null && !"".equals(((String) obj[3] + "").trim())) {
		        type = (obj[3] + "").split(",");
		        for (int j = 0; j < type.length; j++) {
		          od.append("<span style='border: 1px solid #E1E1E1;'>"
		              + type[j] + "</span>&nbsp;");
		        }
		      }
		      od.append("</div></td>");
		      od.append("<td align='center' width='140px'>USD <span id='oldprices'>" + obj[4] + "</span></td>");
		      od.append("<td align='center' width='140px'>USD <span id='newprices'>" + obj[14] + "</span></td>");
		      od.append("<td align='center' width='140px'>" + obj[6] + "&nbsp;piece&nbsp;</td>");
		      od.append("<td align='center'>USD <span id=''>" + new BigDecimal(obj[4].toString()).multiply(new BigDecimal(obj[6].toString())).add(new BigDecimal(obj[14].toString())).setScale(2, BigDecimal.ROUND_HALF_UP) + "</span></td></tr>");
		}
		
		StringBuffer sb = new StringBuffer();
		String emailRemark = "11";
	    // String EmailTitle =
	    // "We have reduced price for your ImportExpress shopping cart selection!";
		double prop = Double.parseDouble(reduPrice) / Double.parseDouble(oldprice) * 100;
		DecimalFormat df = new DecimalFormat("0.00");
	    DecimalFormat dfprop = new DecimalFormat("0");
	    sb.append("<div style='font-size: 14px;'>");
	    sb.append("<a href='" + AppConfig.ip_email
	        + "'><img style='cursor: pointer' src='" + AppConfig.ip_email
	        + "/img/logo.png'></img></a>");
	    sb.append("<div style='font-weight: bolder; margin-bottom: 10px;'>Dear "
	        + user.getName() + ",</div>");
	    
	    String path = "";
	    String uuid = UUIDUtil.getEffectiveUUID(userid, "");
	    try {
	       path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
	    } catch (UnsupportedEncodingException e1) {
	      // TODO Auto-generated catch block
	      e1.printStackTrace();
	    }
	    sb.append("<div style='margin-bottom: 10px;'><span style='font-weight: bold;font-size: 12px;'>You have saved :"
	        + "USD"
	        + "&nbsp;"
	        + df.format(Double.parseDouble(reduPrice)) +" ("+ dfprop.format(prop)  +"% Off)"
	        + "</span>&nbsp;<a style='color: #0070C0' href='"
	        + AppConfig.ip_email+path
	        + "' target='_blank'>Details</a>. We have reduced price for your ImportExpress shopping cart selection!</div><br>");
	    
	    sb.append("<table style='width: 750px;font-size: 12px;' id='emailtable'><tr><td colspan='6' style='border-top: 1px solid;'></td></tr>"
	    	+ "<tr style='font-weight: bold;font-size: 12px;'>"
	        + "<td align='center' width='350px'>Item Name & Details</td><td align='center' width='140px'>Item Price</td><td align='center' width='140px'>Shipping</td>"
	        + "<td align='center' width='140px'>Quantity</td><td align='center' width='140px'>Total Price</td><td align='center' width='140px'>Remark</td></tr>");

	    sb.append(od);
	    
	    sb.append("<div><table style='width: 750px;font-size: 12px;text-align: right'>"
	    		+ "<tr><td>Products Cart Total:</td><td style='width: 150px'>USD "+oldprice+"</td></tr>"
	    		+"<tr><td>Handling Fee:</td><td style='width: 150px'>USD 15</td></tr>"
	    		+"<tr><td>Shipping Coupon:</td><td style='width: 150px'>USD 5</td></tr>"
	    		+"<tr><td>Due:</td><td style='width: 150px'>USD 22.22</td></tr>"
	    		+"<tr><td>FINAL DUE:</td><td style='width: 150px'>USD 20</td></tr>"
	    		+ "</table></div>");
	    sb.append("<div><input type='button' value='BUY NOW' onclick=\"window.location=\'www.import-express.com\'\"/></div>");
		System.out.println(sb.toString());
	}
	
	
	
	/**
	 * 将aliexpress商品链接转换成我们的产品链接
	 * @param url
	 * @return
	 */
	private String changegoodUrl(String url){
//		return "http://www.import-express.com/processesServlet?action=getSpider&className=SpiderServlet"+TypeUtils.encodeGoods(url);
		return "http://www.import-express.com/spider/getSpider?"+TypeUtils.encodeGoods(url);
	}

	@Override
	public String showEmail(int userid, String caridStr, String seleteStr,
			String priceStr, String emailTitle, String emailRemark, int action) {
		IUserDao userDao = new UserDao();
		UserBean user = userDao.getUserFromId(userid);
		String uuid = UUIDUtil.getEffectiveUUID(userid, "");
		List<Object[]> list = getShopCarListFromUser(userid, caridStr);

		List<String> caridList = Arrays.asList(caridStr.split(","));
		List<String> priceList = Arrays.asList(priceStr.split(","));
		List<String> seleteList = Arrays.asList(seleteStr.split(","));
		
		float OriginalPrice = 0;//原价
		float OriginalSumPrice = 0;//商品原总价
		float NewSumPrice = 0;//新价格
		float TotalSavingPrice = 0;//总共节省的价格
		StringBuffer od = new StringBuffer();
		int successNum = 0;
		float prop = 0;//节省比例=节省金额/原商品价格总数
		String currency = null;
		List<TblPreshoppingcarInfo> preShopCarList = new ArrayList<TblPreshoppingcarInfo>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = list.get(i);
			int index = caridList.indexOf(obj[25] + "");//购物车id
			String confirmprice = priceList.get(index);//对应的新价格
			int productNum = Integer.parseInt(obj[26]+"");//商品总数
			NewSumPrice = Float.parseFloat(confirmprice);//新价格
			
			OriginalPrice =  Float.parseFloat(obj[3] + "");//跨境电商原价
			
			currency = (String) obj[4];
			
			DecimalFormat df = new DecimalFormat("0.00");
			TotalSavingPrice += Float.parseFloat(df.format(productNum * (OriginalPrice - NewSumPrice)));//当前商品共节省的钱
			OriginalSumPrice += productNum * OriginalPrice;//商品的原总价
			od.append("<tr>");
			od.append("<td><img style='cursor: pointer' width='50px;' height='50px;' src='"
					+ obj[5] + "'></img></td>");
			od.append("<td><div style='width: 435px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>" + obj[1] + "</a></div><div>");

			String[] type = null;
			String[] sallimg = null;
			
			if (obj[2] != null && !"".equals(((String) obj[2] + "").trim())) {
				type = (obj[2] + "").split(",");
				for (int j = 0; j < type.length; j++) {
					od.append("<span style='border: 1px solid #E1E1E1;'>"
							+ type[j] + "</span>&nbsp;");
				}
			}
			if (obj[24] != null && ((String[]) obj[24]).length > 0) {
				sallimg = (String[]) obj[24];
				for (int k = 0; k < sallimg.length; k++) {
					if (sallimg[k] != null && !"".equals(sallimg[k])) {
						od.append("&nbsp;<img height='20' width='20' src='"
								+ sallimg[k] + "'/>");
					}
				}
			}
			od.append("</div></td>");
			od.append("<td width='100px' align='center'>" + obj[26] + "&nbsp;piece&nbsp;</td>");
			od.append("<td align='center'>" + obj[4] + " <span id='oldprices'>" + obj[3] + "</span></td>");
			od.append("<td align='center'>USD <span id='newprices'>" + confirmprice + "</span></td>");
			od.append("<td align='center'>USD <span id='newprices'>" + obj[33] + "</span></td></tr>");
			
			if (action == 1) {
				String selected = seleteList.get(index);
//				NewSumPrice += Double.parseDouble(confirmprice);
//				OriginalSumPrice += Double.parseDouble(obj[3] + "");
				String sort = selected.substring(selected.length() - 2,
						selected.length() - 1);// 花样作死了.
				int numer = Integer.parseInt(selected.substring(
						selected.indexOf(sort) + 1, selected.length()));
				TblPreshoppingcarInfo preshop = new TblPreshoppingcarInfo();
				preshop.setUserid(userid);
				preshop.setGoodscarid((Integer) obj[25]);
				preshop.setGoodsdataid((Integer) obj[0]);
				preshop.setOldgoodstitle((String) obj[1]);
				preshop.setOldgoodsimg((String) obj[5]);
				preshop.setOldgoodsurl((String) obj[6]);
				preshop.setOldgoodstype((String) obj[2]);
				preshop.setOldgoodsprice(obj[3] + "");
				String Newgoodstitle = "";
				String Newgoodsprice = "";
				String Newgoodsimg = "";
				String Newgoodsurl = "";
				if ("s".equals(sort)) {
					if (numer == 1) {
						Newgoodstitle = (String) obj[7];
						Newgoodsprice = obj[8] + "";
						Newgoodsimg = (String) obj[9];
						Newgoodsurl = (String) obj[10];
					} else if (numer == 2) {
						Newgoodstitle = (String) obj[11];
						Newgoodsprice = obj[12] + "";
						Newgoodsimg = (String) obj[13];
						Newgoodsurl = (String) obj[14];
					} else if (numer == 3) {
						Newgoodstitle = (String) obj[15];
						Newgoodsprice = obj[16] + "";
						Newgoodsimg = (String) obj[17];
						Newgoodsurl = (String) obj[18];
					} else if (numer == 4) {
						Newgoodstitle = (String) obj[19];
						Newgoodsprice = obj[20] + "";
						Newgoodsimg = (String) obj[21];
						Newgoodsurl = (String) obj[22];
					}
				} else if ("d".equals(sort)) {
					List<Object[]> resourceList = (List<Object[]>) obj[23];
					Object[] resource = resourceList.get(numer);
					Newgoodstitle = (String) resource[1];
					Newgoodsprice = resource[4] + "";
					Newgoodsimg = (String) resource[2];
					Newgoodsurl = (String) resource[3];
				}
				preshop.setNewgoodstitle(Newgoodstitle);
				preshop.setNewgoodsprice(Newgoodsprice);
				preshop.setNewgoodsimg(Newgoodsimg);
				preshop.setNewgoodsurl(Newgoodsurl);
				preshop.setConfirmprice(confirmprice);
//				successNum += savePreshoppingcarInfo(preshop);
				preShopCarList.add(preshop);
			}
		}
		
		
		prop = TotalSavingPrice / OriginalSumPrice * 100;
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormat dfprop = new DecimalFormat("0");
//		TotalSavingPrice = df.format(OriginalSumPrice - NewSumPrice);

		StringBuffer sb = new StringBuffer();
		// String EmailTitle =
		// "We have reduced price for your ImportExpress shopping cart selection!";
		sb.append("<div style='font-size: 14px;'>");
		sb.append("<a href='" + AppConfig.ip_email
				+ "'><img style='cursor: pointer' src='" + AppConfig.ip_email
				+ "/img/logo.png'></img></a>");
		sb.append("<div style='font-weight: bolder; margin-bottom: 10px;'>Dear "
				+ user.getName() + ",</div>");
		
		String path = "";
		try {
			 path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sb.append("<div style='margin-bottom: 10px;'><span style='font-weight: bold;font-size: 12px;'>You have saved :"
				+ currency
				+ "&nbsp;"
				+ df.format(TotalSavingPrice) +" ("+ dfprop.format(prop)  +"% Off)"
				+ "</span>&nbsp;<a style='color: #0070C0' href='"
				+ AppConfig.ip_email+path
				+ "' target='_blank'>Details</a>. We have reduced price for your ImportExpress shopping cart selection!</div><br>");
		sb.append("<table style='width: 750px;font-size: 12px;' id='emailtable'><tr><td colspan='6' style='border-top: 1px solid;'></td></tr><tr style='font-weight: bold;font-size: 12px;'>"
				+ "<td colspan='3' align='right'></td>"
				+ "<td align='center' width='140px'>Original price</td><td align='center' width='340px'>New Price</td><td align='center' width='140px'>Shipping Fee</td></tr>");

		sb.append(od);
		sb.append("<tr style='font-weight: bold;font-size: 12px;'><td colspan='5' align='right'>Total Saving:</td><td >"
				+ currency
				+ "&nbsp;<span id='totalsaveprice'>"
				+ df.format(TotalSavingPrice)
				+ "</span></td></tr>");
		sb.append("<tr style='font-weight: bold;font-size: 12px;'><td colspan='6' style='border-bottom: 1px solid;' align='right'><a style='color: #0070C0' href='"
				+ AppConfig.ip_email+path
				+ "' target='_blank'>>>Learn More</a></td></tr></table>");
		sb.append("<div style='margin-top: 10px;'>You may also contact your client service manager which displayed on the right side of your ImportExpress Account.</div>");
		sb.append("<div style='margin-top: 3px;'>Enjoy your shopping with us!</div>");
		if (emailRemark != null && !"".equals(emailRemark.trim())) {
			sb.append("<div style='margin-top: 10px;'><span style='font-weight: bold;font-size: 12px;'>Remark:&nbsp;</span>"
					+ emailRemark + "</div>");
		}
		sb.append("<div style='margin-top: 10px;font-weight: bold'>Best regards, </div><div style='font-weight: bold'><a href='http://www.Import-Express.com' target='_blank'>www.Import-Express.com</a></div></div>");
		
		if (action == 0) {
			return sb.toString();
		} else if (action == 1) {
			int res = SendEmail.send(null, null, user.getEmail(), sb.toString(), emailTitle, null, 1);
			if (res != 1) {
				return "0";
			} else {
				for (int i = 0; i < preShopCarList.size(); i++) {
					try {
						savePreshoppingcarInfo(preShopCarList.get(i));
					} catch (Exception e) {
						e.printStackTrace();
						LOG.error("添加购物车营销记录失败!-"+preShopCarList.get(i).toString());
					}
				}
				return "1";
			}
		}
		return "0";
	}

	@Override
	public int savePreshoppingcarInfo(TblPreshoppingcarInfo preshoppingcar) {
		String sql = "insert into tbl_preshoppingcar_info(userid,goods_car_id,goods_data_id,old_goods_title,old_goods_img,old_goods_url,old_goods_type,old_goods_price,new_goods_title,new_goods_img,new_goods_url,new_goods_price,goods_source_type,confirm_price) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		Connection conn = DBHelper.getInstance().getConnection2();
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, preshoppingcar.getUserid());
			stmt.setInt(2, preshoppingcar.getGoodscarid());
			stmt.setInt(3, preshoppingcar.getGoodsdataid());
			stmt.setString(4, preshoppingcar.getOldgoodstitle());
			stmt.setString(5, preshoppingcar.getOldgoodsimg());
			stmt.setString(6, preshoppingcar.getOldgoodsurl());
			stmt.setString(7, preshoppingcar.getOldgoodstype());
			stmt.setString(8, preshoppingcar.getOldgoodsprice());
			stmt.setString(9, preshoppingcar.getNewgoodstitle());
			stmt.setString(10, preshoppingcar.getNewgoodsimg());
			stmt.setString(11, preshoppingcar.getNewgoodsurl());
			stmt.setString(12, preshoppingcar.getNewgoodsprice());
			stmt.setString(13, preshoppingcar.getGoodssourcetype());
			stmt.setString(14, preshoppingcar.getConfirmprice());
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
			}
			stmt.close();
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
		return result;
	}

	@Override
	public void saveEmailSentMessage(String userid, String goodscarid,
			 String admName) {
		String sql = "insert into cart_market_email_send(userId,cartId,sentEmailTime,adminName) values (?,?,now(),?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,Integer.parseInt( userid));
			stmt.setInt(2, Integer.parseInt(goodscarid));
			stmt.setString(3, admName);	
			stmt.executeUpdate();
		}catch (Exception e) {
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
	
	public int updateWeight(int carid, double weight, int number){
		String sql = "update goods_car set total_weight=?,per_weight=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		Connection conn2 = DBHelper.getInstance().getConnection2();
		PreparedStatement pst = null,pst2 = null;
		int i  = 0;
		try {
			pst = conn.prepareStatement(sql);
			pst.setDouble(1, weight);
			pst.setDouble(2, weight/number);
			pst.setInt(3, carid);
			pst2 = conn2.prepareStatement(sql);
			pst2.setDouble(1, weight);
			pst2.setDouble(2, weight/number);
			pst2.setInt(3, carid);
			i = pst.executeUpdate();
			i += pst2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pst!=null){
				try{
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst2!=null){
				try {
					pst2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn2);
		}
		return i;
	}

	
	
	
	@Override
	public List<goodsCarPresale> getGoodsCarPresale(int userId) {
		String sql = "select * from goods_car_presale where user_id = ?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<goodsCarPresale> list = new ArrayList<goodsCarPresale>();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			rs = pst.executeQuery();
			goodsCarPresale carPresale = null;
			while (rs.next()) {
				carPresale = new goodsCarPresale();
				carPresale.setId(rs.getInt("id"));
				carPresale.setUserId(rs.getInt("user_id"));
				carPresale.setGoodsCarId(rs.getString("goods_car_id"));
				carPresale.setOriginalPrice(rs.getBigDecimal("original_price"));
				carPresale.setCostPrice(rs.getBigDecimal("cost_price"));
				carPresale.setDiscountPrice(rs.getBigDecimal("discount_price"));
				carPresale.setCreateTime(rs.getDate("create_time"));
				
				list.add(carPresale);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pst!=null){
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
	 * 
	 * @param userid
	 * @param goodcarid
	 * @return
	 */
	public List<Object[]> spliceEmail(int userid, String goodcarid) {
		StringBuffer sql = new StringBuffer("select DISTINCT(gc.id),gc.goodsdata_id,gc.goods_title,gc.goods_type,gc.price3,gc.notfreeprice,gc.googs_number,gt.img as smallimg, ifnull(gc.total_weight,0.00) as weight, ifnull(gc.width,'') as width,");
		sql.append("gc.googs_img as img, gc.currency,gc.goods_url,gc.preferential,gc.freight ");
		sql.append("from goods_car gc LEFT JOIN goodsdatacheck gdc on gc.goods_url = gdc.url ");
		sql.append("LEFT JOIN goods_typeimg gt on gc.id = gt.goods_id  where 1 = 1 ");
		if (userid != 0) {
			sql.append("and gc.userid = " + userid);
		}
		if (goodcarid != null) {
			sql.append(" and gc.id in (" + goodcarid + ") ");
		}
		sql.append(" and gc.state = 0 ");
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Object[]> list = new ArrayList<Object[]>();
		try {
		      stmt = conn.prepareStatement(sql.toString());
		      rs = stmt.executeQuery();
		      while (rs.next()) {
		    	  Object[] obj = new Object[15];
		    	  obj[0] = rs.getInt("id");
		    	  obj[1] = rs.getInt("goodsdata_id");
		    	  obj[2] = rs.getString("goods_title");
		    	  obj[3] = rs.getString("goods_type");
		    	  obj[4] = rs.getDouble("price3");
		    	  obj[5] = rs.getDouble("notfreeprice");
		    	  obj[6] = rs.getInt("googs_number");
		    	  obj[7] = rs.getString("smallimg");
		    	  obj[8] = rs.getString("weight");
		    	  obj[9] = rs.getString("width");
		    	  obj[10] = rs.getString("img");
		    	  obj[11] = rs.getString("currency");
		    	  obj[12] = rs.getString("goods_url");
		    	  obj[13] = rs.getString("preferential");
		    	  obj[14] = rs.getDouble("freight");
		    	  list.add(obj);
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public int deleteUnMarketData(int userId, String goodscarid, String countList,String oldprice,String newprice,String reduPrice) throws Exception{
		int res = 0;
		if (userId == 0 || goodscarid.isEmpty()) {
			return res;
		}
		StringBuffer sql = new StringBuffer("delete from goods_car where 1 = 1 ");
		if (userId != 0) {
			sql.append("and userid = " + userId);
		}
		if (goodscarid != null) {
			sql.append(" and id not in (" + goodscarid + ") ");
		}
		sql.append(" and state = 0 ");
		
		StringBuffer presql = new StringBuffer("insert into goods_car_presale(user_id,goods_car_id,original_price,cost_price,discount_price) values (");
		presql.append(userId+",'"+goodscarid+"',"+new BigDecimal(oldprice)+","+new BigDecimal(newprice)+","+new BigDecimal(reduPrice)+")");
		
		
		Connection conn = DBHelper.getInstance().getConnection2();
		conn.setAutoCommit(false);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql.toString());
			pst.executeUpdate();
			pst = conn.prepareStatement(presql.toString());
			pst.execute();
			String[] goodcarsplit = goodscarid.split(",");
			String[] countsplit = countList.split(",");
			for (int i = 0; i < goodcarsplit.length; i++) {
				String numbersql = "update goods_car set googs_number = " + Integer.parseInt(countsplit[i]) + " where id = " + goodcarsplit[i];
				pst = conn.prepareStatement(numbersql);
				pst.executeUpdate();
			}
			conn.commit();
			res = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	
}