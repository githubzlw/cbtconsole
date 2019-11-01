package com.cbt.fee.dao;

import com.cbt.bean.StateName;
import com.cbt.bean.TransitPricecost;
import com.cbt.bean.TransitType;
import com.cbt.bean.ZoneBean;
import com.cbt.jdbc.DBHelper;

import java.sql.*;
import java.util.*;

public class ZoneDao implements IZoneDao {


	public static void main(String[] args) {
		ZoneDao sDao= new ZoneDao();
		List<ZoneBean> zone = sDao.getAllZone();
		for (int i = 0; i < zone.size(); i++) {
			if("北美".equals(zone.get(i).getArea())){
				zone.get(i).setArea("1");
			}else if("澳洲".equals(zone.get(i).getArea())){
				zone.get(i).setArea("2");
			}else if("西欧".equals(zone.get(i).getArea())){
				zone.get(i).setArea("3");
			}
		}
		//System.out.println( JSONArray.fromObject(zone));
	}
	@Override
	public List<ZoneBean> getAllZone() {
		// TODO Auto-generated method stub
		String sql = "select * from zone where id not in(43,42) and del != 1 order by country asc";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		List<ZoneBean> zonelist = new ArrayList<ZoneBean>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ZoneBean zone = new ZoneBean();
				zone.setId(rs.getInt("id"));
				zone.setCountry(rs.getString("country").trim());
				zone.setZone(rs.getInt("zone"));
				zone.setArea(rs.getString("area"));
				zonelist.add(zone);
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
		return zonelist;
	}

	@Override
	public List<StateName> getStateName() {
		// TODO Auto-generated method stub
		String sql = "select * from statename";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<StateName> statelist = new ArrayList<StateName>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				StateName state = new StateName();
				state.setStatecode(rs.getString("statecode"));
				state.setStatename(rs.getString("statename"));
				statelist.add(state);
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
		return statelist;
	}

	@Override
	public String getShippingType(int countryid,String shiptype) {
		// TODO Auto-generated method stub
		String result="";

		String sql = "select * from zone where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, countryid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result=rs.getString(shiptype);
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
        
		return result;
	}

	@Override
	public float getFedexiePrice(float weight,String name,String type) {
		// TODO Auto-generated method stub
		float result=0;
		
		String sql = "select * from "+type+" where weight=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setFloat(1, weight);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result=rs.getFloat(name);
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
		return result;
	}
	
	@Override
	public float getEpacketPrice(String name,float weight) {
		float result=0;
		float fee1=0;
		float fee2=0;
		// TODO Auto-generated method stub
		String sql = "select * from epacket where country=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
			}
			if(weight<0.06){
				weight=0.06f;
			}
			result=fee1*weight*1000+fee2;
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
		return result;
	}

	@Override
	public float getDhlfbaPrice(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float weight1=0;
		float weight2=0;
		float fee1=0;
		float fee2=0;
		String sql = "select * from dhlfba";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				weight1=rs.getFloat("weight1");
				weight2=rs.getFloat("weight2");
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
				
				if((weight>weight1&&weight<=weight2)&&weight2<=500&&weight<20){
					result=(float) (fee1+fee2*((weight-0.5)/0.5));
				}else if(weight>=20){
					result=weight*fee2;
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
		return result;
	}

	@Override
	public float getChinapostsalPrice(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float under=0;
		float over=0;
		String sql = "select * from chinapostbig where country=? and type='China Post SAL'";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				under=rs.getFloat("under");
				over=rs.getFloat("over");
			}
			////System.out.println(weight+"under"+under+"over"+over);
			result=under+((weight-1)>0?(weight-1):0)*over;
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
		return result;
	}
	
	@Override
	public float getChinapostsurfacePrice(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float under=0;
		float over=0;
		String sql = "select * from chinapostbig where country=? and type='China Post AIR'";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				under=rs.getFloat("under");
				over=rs.getFloat("over");
			}
			result=under+((weight-1)>0?(weight-1):0)*over;
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
		return result;
	}

	@Override
	public float getSweden(String name, float weight) {
		// TODO Auto-generated method stub
		int price=0;
		int guahao=0;
		float result=0;
		String sql = "select * from sweden where zone=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				price=rs.getInt("price");
				guahao=rs.getInt("guahao");
			}
			result=price*weight+guahao;
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
		return result;
	}

	@Override
	public float getTNT(String name, float weight) {
		// TODO Auto-generated method stub
		int price=0;
		float guahao=0;
		float result=0;
		String sql = "select * from tnt where zone=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				price=rs.getInt("price");
				guahao=rs.getFloat("guahao");
			}
			result=price*weight+guahao;
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
		return result;
	}

	@Override
	public float getKyd(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;
		float fee2=0;
		String sql = "select * from kyd where zone=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
			}
			/*
			 * 标准价计算公式=（重量-0.05）*续重+首重 （最低收费50g，1g为一个单位）                                                                                            
			 *  例如：200g巴西，                                                                                                                               
			 *  1：标准价=（0.2-0.05）*120+5  （最低收费5元）     
			 *  2：运费=（标准价+8）*折扣
			*/                                                                                          
			 
			result=(float) ((fee2*1000*(weight-0.05)+fee1+8)*0.85);
			
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
		return result;
	}

	@Override
	public float getEMS(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;//首重0.5kg费用，不足0.5kg按0.5kg计算
		float fee2=0;//续重0.5kg费用
		String sql = "select * from ems where zone=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
			}
			if(weight<=0.5){
				result=fee1;
			}else{
				result=(float) (fee1+Math.ceil((weight-0.5)/0.5)*fee2);
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
		return result;
	}

	@Override
	public float getEfast(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;//首重0.05kg费用，不足0.05kg按0.05kg计算
		float fee2=0;//续重0.05kg费用
		String sql = "select * from efast where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
			}
			
			if(weight<=0.05){
				result=fee1;
			}else{
				result=(float) (fee1+Math.ceil((weight-0.05)/0.05)*fee2);
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
		return result;
	}

	@Override
	public float getFedexground(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;
		String sql = "select * from fedexground where weight=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setFloat(1, weight);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat(name);
			}
			result=fee1+60+weight/1000*50;
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
		return result;
	}

	@Override
	public float getPortpickup(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;
		float fee2=0;
		
		String sql = "select * from portpickup where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
			}
			result=fee1+fee2/1000*weight;
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
		
		return result;
	}

	@Override
	public float getAirportpickup(int countryid, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;
		float fee2=0;
		float fee3=0;
		String sql = "select fee1,fee2,fee3 from airportpickup where countryid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, countryid);
			rs = stmt.executeQuery();
			if(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
				fee3=rs.getFloat("fee3");
			}
			if(weight < 100){
				result=fee1+fee2*weight;
			}else{
				result=fee1+fee3*weight;
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
		
		return result;
	}

	@Override
	public float getAramex(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;
		float fee2=0;
		float fee3=0;
		
		String sql = "select * from aramexmideast where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
				fee3=rs.getFloat("fee3");
			}
			if(weight<=20){
				result=(float) (fee1+fee2*(weight-0.5)/0.5);
			}else{
				result=fee3*weight;
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
		
		return result;
	}

	@Override
	public float getDHL(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;
		float fee2=0;
		float fee3=0;
		float fee4=0;
		float fee5=0;
		float fee6=0;
		float fee7=0;
		float fee8=0;
		float fee9=0;
		float fee10=0;
		float fee11=0;
		float fee12=0;
		float fee13=0;
		float fee14=0;
		float fee15=0;
		float fee16=0;
		
		String sql = "select * from dhl where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
				fee3=rs.getFloat("fee3");
				fee4=rs.getFloat("fee4");
				fee5=rs.getFloat("fee5");
				fee6=rs.getFloat("fee6");
				fee7=rs.getFloat("fee7");
				fee8=rs.getFloat("fee8");
				fee9=rs.getFloat("fee9");
				fee10=rs.getFloat("fee10");
				fee11=rs.getFloat("fee11");
				fee12=rs.getFloat("fee12");
				fee13=rs.getFloat("fee13");
				fee14=rs.getFloat("fee14");
				fee15=rs.getFloat("fee15");
				fee16=rs.getFloat("fee16");
			}
			if(weight<=0.5){
				result=fee3;
			}else if(weight>0.5&&weight<=5){
				result=(float) (fee3+fee4*(weight-0.5)/0.5);
			}else if(weight>5&&weight<=5.5){
				result=fee5;
			}else if(weight>5.5&&weight<=10){
				result=(float) (fee5+fee6*(weight-0.5)/0.5);
			}else if(weight>10&&weight<=10.5){
				result=fee7;
			}else if(weight>10.5&&weight<=21){
				result=(float) (fee7+fee8*(weight-0.5)/0.5);
			}else if(weight>21&&weight<=31){
				result=weight*fee9;
			}else if(weight>31&&weight<=51){
				result=weight*fee10;
			}else if(weight>51&&weight<=71){
				result=weight*fee11;
			}else if(weight>71&&weight<=101){
				result=weight*fee12;
			}else if(weight>101&&weight<=201){
				result=weight*fee13;
			}else if(weight>201&&weight<=300){
				result=weight*fee14;
			}else if(weight>300&&weight<=500){
				result=weight*fee15;
			}else if(weight>500){
				result=weight*fee16;
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
		
		return result;
	}

	@Override
	public float getDhlfbaother(String name, float weight) {
		// TODO Auto-generated method stub
		float result=0;
		float fee1=0;
		float fee2=0;
		float fee3=0;
		float fee4=0;
		
		String sql = "select * from dhlfbaother where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while(rs.next()){
				fee1=rs.getFloat("fee1");
				fee2=rs.getFloat("fee2");
				fee3=rs.getFloat("fee3");
				fee4=rs.getFloat("fee4");
			}
			if(weight<=0.5){
				result=fee1;
			}else if(weight>0.5&&weight<=11){
				result=(float) (fee1+fee2*(weight-0.5)/0.5);
			}else if(weight>11&&weight<=20){
				result=fee3*weight;
			}else if(weight>20){
				result=fee4*weight;
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
		
		return result;
	}

	@Override
	public double[] getCheckfreeLimit(int countryid) {
		String sql = "select price,weight,tariff from tab_transit_checkfree where countryid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		double[] result = new double[3];
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, countryid);
			rs = stmt.executeQuery();
			if(rs.next()){
				result[0] = rs.getDouble("price");
				result[1] = rs.getDouble("weight");
				result[2] = rs.getDouble("tariff");
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
		
		return result;
	}

	@Override
	public List<TransitType> getCheckfreeType(String[] typeIds) {
		List<TransitType> res = new ArrayList<TransitType>();
		if(typeIds.length == 0){
			return res;
		}
		String sql = "select transit_type,aliExpress_type,battery from tab_transit_type where aliExpress_type=?";
		for (int i = 1; i < typeIds.length; i++) {
			sql += " or aliExpress_type=? ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < typeIds.length; i++) {
					stmt.setString(i+1, typeIds[i]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				TransitType transitType = new TransitType();
				transitType.setBattery(rs.getString("battery"));
				transitType.setAliExpress_type(rs.getString("aliExpress_type"));
				transitType.setTransit_type(rs.getString("transit_type"));
				res.add(transitType);
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
		
		return res;
	}

	/*@Override
	public LinkedHashMap<String, List<Object[]>> getFreightFates(int countryid,double[] type_weights,String[] batterys,double[] battery_weights) {
		String sql = "select shippingmethod,days,under,over,divisionweight from tab_transit_pricecost where countryid=? and battery=? and minweight>? and maxweight<? ";
		String sql1 = "select shippingmethod,days,under,over,divisionweight from tab_transit_pricecost where countryid=? and battery=? and minweight>? and maxweight<? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		LinkedHashMap<String, List<Object[]>> map = new LinkedHashMap<String, List<Object[]>>();
		try {
			for (int i = 0; i < type_weights.length; i++) {
				List<Object[]> res = new ArrayList<Object[]>();
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, countryid);
				stmt.setString(2, batterys[i]);
				stmt.setDouble(3, type_weights[i]);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Object[] result = {rs.getString("shippingmethod"),rs.getInt("days"),rs.getDouble("under"),rs.getDouble("over"),batterys[i]};
					res.add(result);
				}
				map.put("frightfates"+i, res);
			}
			for (int i = 0; i < 2; i++) {
				if(battery_weights[i] != 0){
					List<Object[]> res = new ArrayList<Object[]>();
					stmt = conn.prepareStatement(sql1);
					stmt.setInt(1, countryid);
					String isBattery = i == 0 ? "N" : "Y";
					stmt.setString(2, isBattery);
					stmt.setDouble(3, battery_weights[i]);
					rs = stmt.executeQuery();
					while (rs.next()) {
						Object[] result = {rs.getString("shippingmethod"),rs.getInt("days"),rs.getDouble("under"),rs.getDouble("over"),isBattery};
						res.add(result);
					}
					map.put("battery_"+isBattery, res);
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
		
		return map;
	}*/

	@Override
	public List<TransitPricecost> getFreightFates(int countryid) {
		String sql = "select shippingmethod_en,days,under,over,divisionweight,delivery_time,battery,maxweight,minweight from tab_transit_pricecost where countryid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		List<TransitPricecost> map = new ArrayList<TransitPricecost>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, countryid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TransitPricecost transitPricecost = new TransitPricecost();
				transitPricecost.setBattery(rs.getString("battery"));
				transitPricecost.setShippingmethod_en(rs.getString("shippingmethod_en"));
				transitPricecost.setDays(rs.getInt("days"));
				transitPricecost.setUnder(rs.getDouble("under"));
				transitPricecost.setOver(rs.getDouble("over"));
				transitPricecost.setDivisionweight(rs.getDouble("divisionweight"));
				transitPricecost.setMinweight(rs.getDouble("minweight"));
				transitPricecost.setMaxweight(rs.getDouble("maxweight"));
				transitPricecost.setDelivery_time(rs.getString("delivery_time"));
				map.add(transitPricecost);
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
		
		return map;
	}
	
	
	public LinkedHashMap<String, List<Object[]>> getFreightFates1(int countryid,double[] type_weights,double[] type_vws,String[] batterys,double[] battery_weights,double[] battery_vws,java.util.Set<Double> sqlWeights) {
		String sql = "select shippingmethod_en,days,under,over,divisionweight,battery,weight from tab_transit_pricecost2 where countryid=? and days !=90 and ( weight = ?";
		for (int i = 1; i < sqlWeights.size(); i++) {
			sql += " or weight = ? ";
		}
		sql += ") and battery='N' group by days,weight";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		LinkedHashMap<String, List<Object[]>> map = new LinkedHashMap<String, List<Object[]>>();
		try {
			List<Object[]> pricecost = new ArrayList<Object[]>();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, countryid);
			int k = 2;
			for(Double sqlWeight : sqlWeights){
				stmt.setDouble(k, sqlWeight);
				k++;
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String shippingmethod = rs.getString("shippingmethod_en");
				int days = rs.getInt("days");
				double under = rs.getDouble("under");
				double over = rs.getDouble("over");
				double divisionweight = rs.getDouble("divisionweight");
				double weight = rs.getDouble("weight");
				pricecost.add(new Object[]{shippingmethod,days,under,over,divisionweight,"N",weight});
				
			}
			for (int i = 0; i < type_weights.length; i++) {
				List<Object[]> res = new ArrayList<Object[]>();
				double type_weight = type_weights[i];//重量
				double type_vw = type_vws[i];//体积重量
				String battery = batterys[i];
				for (int j = 0; j < pricecost.size(); j++) {
					String shippingmethod = (String) pricecost.get(j)[0];
					String battery_ = (String) pricecost.get(j)[5];
					double minweight = (Double) pricecost.get(j)[6];
					if(shippingmethod.equals("SAL") || shippingmethod.equals("SLL")){
						if(type_weight == minweight && (battery.equals(battery_))){
//						if(type_weight == minweight && (battery.equals(battery_) || battery.equals("N"))){
							Object[] result = {shippingmethod,pricecost.get(j)[1],pricecost.get(j)[2],pricecost.get(j)[3],pricecost.get(j)[4],battery};
							res.add(result);
						}
					}
					else{
						if(type_vw == minweight && (battery.equals(battery_))){
//						if(type_vw == minweight && (battery.equals(battery_) || battery.equals("N"))){
							Object[] result = {shippingmethod,pricecost.get(j)[1],pricecost.get(j)[2],pricecost.get(j)[3],pricecost.get(j)[4],battery};
							res.add(result);
						}
					}
				}
				map.put("frightfates"+i, res);
			}
			 
			for (int i = 0; i < 3; i++) {
				if(battery_weights[i] != 0){
					List<Object[]> res = new ArrayList<Object[]>();
					double type_weight = battery_weights[i];//重量
					double type_vw = battery_vws[i];//体积重量
					String isBattery = i == 0 ? "N" : "Y";
					for (int j = 0; j < pricecost.size(); j++) {
						String shippingmethod = (String) pricecost.get(j)[0];
						String battery_ = (String) pricecost.get(j)[5];
						double minweight = (Double) pricecost.get(j)[6];
						int days = (Integer) pricecost.get(j)[1];
						if(i == 2){
							if(days == 30 && battery_.equals("Y")){
								Object[] result = {shippingmethod,pricecost.get(j)[1],pricecost.get(j)[2],pricecost.get(j)[3],pricecost.get(j)[4],isBattery};
								res.add(result);
								break;
							}
							continue;
						}
						if(shippingmethod.equals("SAL") || shippingmethod.equals("SLL")){
//							if(type_weight == minweight && (isBattery.equals(battery_)|| isBattery.equals("N"))){
							if(type_weight == minweight && isBattery.equals(battery_)){
								Object[] result = {shippingmethod,pricecost.get(j)[1],pricecost.get(j)[2],pricecost.get(j)[3],pricecost.get(j)[4],isBattery};
								res.add(result);
							}
						}else{
							if(type_vw == minweight && isBattery.equals(battery_)){
//							if(type_weight == minweight && (isBattery.equals(battery_)|| isBattery.equals("N"))){
								Object[] result = {shippingmethod,pricecost.get(j)[1],pricecost.get(j)[2],pricecost.get(j)[3],pricecost.get(j)[4],isBattery};
								res.add(result);
							}
						}
					}
					if(i == 2){
						map.put("battery2_"+isBattery, res);
					}else{
						map.put("battery_"+isBattery, res);
					}
					
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
		
		return map;
	}
	
	@Override
	public double getCheckfreePrice(int countryid) {
		String sql = "select price from tab_transit_checkfree where countryid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		double result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, countryid);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getDouble("price");
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
		
		return result;
	}

	@Override
	public Map<String, List<TransitPricecost>> getTransitPricecost() {
		String sql = "select id,countryid,shippingmethod,shippingmethod_en,days,delivery_time,under,over,divisionweight,battery,minweight,maxweight,countryname,countrycname from tab_transit_pricecost where countryid  is not null  order by countryid,battery,days,shippingmethod";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		Map<String, List<TransitPricecost>> pricecoMap = new HashMap<String, List<TransitPricecost>>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				List<TransitPricecost> pricecost = new ArrayList<TransitPricecost>();
				TransitPricecost transitPricecost = new TransitPricecost();
				transitPricecost.setId(rs.getInt("id"));
				transitPricecost.setShippingmethod(rs.getString("shippingmethod"));
				transitPricecost.setCountryId(rs.getInt("countryid"));
				transitPricecost.setDays(rs.getInt("days"));
				transitPricecost.setDelivery_time(rs.getString("delivery_time"));
				transitPricecost.setDivisionweight(rs.getDouble("divisionweight"));
				transitPricecost.setMaxweight(rs.getDouble("maxweight"));
				transitPricecost.setMinweight(rs.getDouble("minweight"));
				transitPricecost.setBattery(rs.getString("battery"));
				transitPricecost.setUnder(rs.getDouble("under"));
				transitPricecost.setOver(rs.getDouble("over"));
				transitPricecost.setCountrycname(rs.getString("countrycname"));
				transitPricecost.setCountryname(rs.getString("countryname"));
				transitPricecost.setShippingmethod_en(rs.getString("shippingmethod_en"));
				transitPricecost.setMaxweight(rs.getDouble("maxweight"));
				transitPricecost.setMinweight(rs.getDouble("minweight"));
				if(pricecoMap.get("countryid"+rs.getInt("countryid")) == null){
					pricecost.add(transitPricecost);
					pricecoMap.put("countryid"+rs.getInt("countryid"), pricecost);
				}else{
					pricecoMap.get("countryid"+rs.getInt("countryid")).add(transitPricecost);
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
		
		return pricecoMap;
	}
	
	@Override
	public LinkedHashMap<String, List<Object[]>> getDHL_FedEX(int countryid,double sqlWeight, int isDHL, int isFex) {
		String sql = "select id,countryid,shippingmethod,shippingmethod_en,days,delivery_time,under,over,divisionweight,battery,minweight,maxweight,countryname,countrycname from tab_transit_pricecost where   days !=90 and battery='N' and countryid=? and  minweight<? and maxweight>? ";
		if(isDHL == 1){
			sql += " and  ( shippingmethod_en like '%DHL%' ";
		}
		if(isFex == 1){
			if(isDHL == 1){
				sql += " or ";
			}else{
				sql += " and ( ";
			}
			sql += "   shippingmethod_en like '%FedEx%' ";
		}
		sql += " ) order by shippingmethod_en";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs  = null;
		PreparedStatement stmt = null;
		LinkedHashMap<String, List<Object[]>> map = new LinkedHashMap<String, List<Object[]>>();
		try {
			List<Object[]> pricecost_dhl = new ArrayList<Object[]>();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, countryid);
			stmt.setDouble(2, sqlWeight);
			stmt.setDouble(3, sqlWeight);
			rs = stmt.executeQuery();
			List<Object[]> pricecost_FedEx = new ArrayList<Object[]>();
			while (rs.next()) {
				String battery = rs.getString("battery");
				String shippingmethod = rs.getString("shippingmethod_en");
				int days = rs.getInt("days");
				double under = rs.getDouble("under");
				double over = rs.getDouble("over");
				double divisionweight = rs.getDouble("divisionweight");
				double minweight = rs.getDouble("minweight");
				double maxweight = rs.getDouble("maxweight");
//				double weight = rs.getDouble("weight");
				if(shippingmethod.equals("DHL")){
					pricecost_dhl.add(new Object[]{shippingmethod,days,under,over,divisionweight,battery,minweight,maxweight});
				}else{
					pricecost_FedEx.add(new Object[]{shippingmethod,days,under,over,divisionweight,battery,minweight,maxweight});
				}
			}
			map.put("FedEx", pricecost_FedEx);
			map.put("DHL", pricecost_dhl);
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
		
		return map;
	}
	
	
	@Override
	public void addTransitPricecost(List<TransitPricecost> transitPricecost) {
		// TODO Auto-generated method stub
		String sql = "insert tab_transit_pricecost3(countryid,shippingmethod,days,delivery_time,under,over,divisionweight,battery,weight,shippingmethod_en,minweight,maxweight) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for(int i=0;i<transitPricecost.size();i++){
				stmt.setInt(1,transitPricecost.get(i).getCountryId());
				stmt.setString(2,transitPricecost.get(i).getShippingmethod());
				stmt.setInt(3,transitPricecost.get(i).getDays());
				stmt.setString(4,transitPricecost.get(i).getDelivery_time());
				stmt.setDouble(5, transitPricecost.get(i).getUnder());
				stmt.setDouble(6, transitPricecost.get(i).getOver());
				stmt.setDouble(7, transitPricecost.get(i).getDivisionweight());
				stmt.setString(8,transitPricecost.get(i).getBattery());
				stmt.setDouble(9, transitPricecost.get(i).getWeight());
				stmt.setString(10,transitPricecost.get(i).getShippingmethod_en());
				stmt.setDouble(11, transitPricecost.get(i).getMinweight());
				stmt.setDouble(12, transitPricecost.get(i).getMaxweight());
				stmt.addBatch();
			}
			int[] result = stmt.executeBatch();
			if (result.length > 0) {
				stmt.getGeneratedKeys();
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
		}
	}
	
}
