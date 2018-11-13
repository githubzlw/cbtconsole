package com.cbt.Specification.dao;

import com.cbt.jdbc.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecificationDao {

	public List<String> queryTranslationEnName() {
		Connection conn = DBHelper.getInstance().getConnection();
		List<String> transEnNames = new ArrayList<String>();
		try {
			String selectSql = "select DISTINCT enName from specification_translation where 1=1 and ifnull(chName,'') = ''";
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectSql);
				while (rs.next()) {
					String enName = rs.getString("enName");
					if (enName != null && !"".equals(enName)) {
						transEnNames.add(enName);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.getInstance().closeConnection(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transEnNames;

	}

	public List<String> queryMappingEnName() {
		Connection conn = DBHelper.getInstance().getConnection();
		List<String> transEnNames = new ArrayList<String>();
		try {
			String selectSql = "select DISTINCT enName from specification_mapping where 1=1 and ifnull(chName,'') = ''";
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectSql);
				while (rs.next()) {
					String enName = rs.getString("enName");
					if (enName != null && !"".equals(enName)) {
						transEnNames.add(enName);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.getInstance().closeConnection(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transEnNames;

	}

	public void updateSpecificationByEnName(String chName, String enName) {

		Connection conn = DBHelper.getInstance().getConnection();
		try {
			String selectSql = "update specification_translation set chName =? where enName =?";
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(selectSql);
				stmt.setString(1, chName);
				stmt.setString(2, enName);
				stmt.executeUpdate();
				System.out.println("update translation success:" + enName + " is " + chName);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateMappingByEnName(String chName, String enName) {

		Connection conn = DBHelper.getInstance().getConnection();
		try {
			String selectSql = "update specification_mapping set chName =? where enName =?";
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(selectSql);
				stmt.setString(1, chName);
				stmt.setString(2, enName);
				stmt.executeUpdate();
				System.out.println("update translation success:" + enName + " is " + chName);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
