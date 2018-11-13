package com.cbt.dao.impl;

import com.cbt.dao.OffShelfDao;
import com.cbt.jdbc.DBHelper;
import com.importExpress.service.SendMQService;
import com.importExpress.service.impl.SendMQServiceImpl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OffShelfDaoImpl implements OffShelfDao {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OffShelfDao.class);

	@Autowired
	private SendMQService sendMQ;
	
	@Override
	public Integer updateValidAndUnsellableReasonByPid(String pid, Integer valid, Integer unsellableReason) {
		Connection conn28 = DBHelper.getInstance().getConnection8();
        Connection conn = DBHelper.getInstance().getConnection();
        String up28Sql = "update custom_benchmark_ready_newest set unsellableReason = ? ";
        String upSql = "update custom_benchmark_ready set unsellableReason = ?, cur_time=sysdate() ";
        String upRemoteSql = "update custom_benchmark_ready set unsellableReason = " + unsellableReason + ", cur_time=sysdate() ";
        if (null != valid) {
        	if (valid == 1) {
        		upSql += " ,goodsstate = 4,valid = ?";
        		up28Sql += " ,goodsstate = 4,valid = ?";
        		upRemoteSql += ",goodsstate = 4,valid = " + valid;
			} else {
				upSql += " ,goodsstate = 2,valid = ?";
        		up28Sql += " ,goodsstate = 2,valid = ?";
        		upRemoteSql += ",goodsstate = 2,valid = " + valid;
			}
		}
        upSql += "where pid = ?";
        up28Sql += "where pid = ?";
        upRemoteSql += " where pid = \"" + pid + "\"";
        
        PreparedStatement stmt28 = null;
        PreparedStatement stmt = null;
        int rs = 0;
        int cur = 1;
        try {
            conn.setAutoCommit(false);
            conn28.setAutoCommit(false);
            
            //更新28
            stmt28 = conn28.prepareStatement(up28Sql);
            stmt28.setInt(cur++, unsellableReason);
            if (null != valid) {
            	stmt28.setInt(cur++, valid);
			}
            stmt28.setString(cur++, pid);
            rs = stmt28.executeUpdate();
            
            if (rs > 0) {
                rs = 0;
                cur = 1;
                //更新27
                stmt = conn.prepareStatement(upSql);
                stmt.setInt(cur++, unsellableReason);
                if (null != valid) {
                	stmt.setInt(cur++, valid);
    			}
                stmt.setString(cur++, pid);
                rs = stmt.executeUpdate();
                
                if (rs > 0) {
                    rs = 0;
                    //mq方式 更新线上
                	rs = sendMQ.runSqlOnline(pid, upRemoteSql);
                    
                    if (rs > 0) {
                        conn.commit();
                        conn28.commit();
                        return 1;
                    } else {
                        conn.rollback();
                        conn28.rollback();
                    }
                } else {
                    conn.rollback();
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + " updateValidAndUnsellableReasonByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + " updateValidAndUnsellableReasonByPid error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt28 != null) {
                try {
                    stmt28.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return 0;
	}
	
	public Integer updateValidAndUnsellableReasonByPidJDBC(String pid, Integer valid, Integer unsellableReason) {
		SendMQService sendMQ = new SendMQServiceImpl();
		Connection conn28 = DBHelper.getInstance().getConnection8();
        Connection conn = DBHelper.getInstance().getConnection();
        String up28Sql = "update custom_benchmark_ready_newest set unsellableReason = ? ";
        String upSql = "update custom_benchmark_ready set unsellableReason = ?, cur_time=sysdate() ";
        String upRemoteSql = "update custom_benchmark_ready set unsellableReason = " + unsellableReason + ", cur_time=sysdate() ";
        if (null != valid) {
        	if (valid == 1) {
        		upSql += " ,goodsstate = 4,valid = ?";
        		up28Sql += " ,goodsstate = 4,valid = ?";
        		upRemoteSql += ",goodsstate = 4,valid = " + valid;
			} else {
				upSql += " ,goodsstate = 2,valid = ?";
        		up28Sql += " ,goodsstate = 2,valid = ?";
        		upRemoteSql += ",goodsstate = 2,valid = " + valid;
			}
		}
        upSql += " where pid = ?";
        up28Sql += " where pid = ?";
        upRemoteSql += " where pid = \"" + pid + "\"";
        
        PreparedStatement stmt28 = null;
        PreparedStatement stmt = null;
        int rs = 0;
        int cur = 1;
        try {
            conn.setAutoCommit(false);
            conn28.setAutoCommit(false);
            
            //更新28
            stmt28 = conn28.prepareStatement(up28Sql);
            stmt28.setInt(cur++, unsellableReason);
            if (null != valid) {
            	stmt28.setInt(cur++, valid);
			}
            stmt28.setString(cur++, pid);
            rs = stmt28.executeUpdate();
            
            if (rs >= 0) {
                rs = 0;
                cur = 1;
                //更新27
                stmt = conn.prepareStatement(upSql);
                stmt.setInt(cur++, unsellableReason);
                if (null != valid) {
                	stmt.setInt(cur++, valid);
    			}
                stmt.setString(cur++, pid);
                rs = stmt.executeUpdate();
                
                if (rs >= 0) {
                    rs = 0;
                    //mq方式 更新线上
                	rs = sendMQ.runSqlOnline(pid, upRemoteSql);
                    
                    if (rs > 0) {
                        conn.commit();
                        conn28.commit();
                        return 1;
                    } else {
                        conn.rollback();
                        conn28.rollback();
                    }
                } else {
                    conn.rollback();
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + " updateValidAndUnsellableReasonByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + " updateValidAndUnsellableReasonByPid error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt28 != null) {
                try {
                    stmt28.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return 0;
	}

	@Override
	public Integer querySameProductIds(String pid) {
		String sql = "SELECT same_count FROM alidata.1688_same_goods WHERE goods_pid = ?";
		Connection conn = DBHelper.getInstance().getConnection8();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer sameProductIds = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			rs = stmt.executeQuery();
			while(rs.next()){
				sameProductIds = rs.getInt("same_count");
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
		return sameProductIds;
	}

	@Override
	public Integer queryRemainingByPid(String pid) {
		String sql = "select count(id) as n from inventory where goods_pid= ?";
		Connection conn = DBHelper.getInstance().getConnection(); // 27
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer remaining = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			rs = stmt.executeQuery();
			while(rs.next()){
				remaining = rs.getInt("n");
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
		return remaining;
	}

	@Override
	public List<String> queryGoodsSoftOffShelf() {
		Connection conn28 = DBHelper.getInstance().getConnection5();
        String querySql = "select pid,unsellableReason from cross_border.custom_benchmark_ready_newest where valid = 2 AND unsellableReason IN(1,4,8,9)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> pids = new ArrayList<String>();

        try {
            stmt = conn28.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pids.add(rs.getString("pid") + "@" + rs.getInt("unsellableReason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryByShopId error :" + e.getMessage());
            LOG.error("queryByShopId error :" + e.getMessage());
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
            DBHelper.getInstance().closeConnection(conn28);
        }
        return pids;
	}
	
	

}
