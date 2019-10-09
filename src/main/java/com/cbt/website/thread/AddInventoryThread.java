package com.cbt.website.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cbt.jdbc.DBHelper;
import com.cbt.util.NewFtpUtil;
import com.cbt.util.Util;
import com.google.common.collect.Lists;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

public class AddInventoryThread extends Thread{
	private String storePath;//文件名称
	private String imgPath;//本地文件路径
	private String orderid;
	private String odid;
	private int index;
	public AddInventoryThread(String storePath,String imgPath,String orderid,String odid,int index) {
		super();
		this.storePath = storePath;
		this.imgPath = imgPath;
		this.orderid = orderid;
		this.odid = odid;
		this.index=index;
	}
	@Override
	public void run() {
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="";
		String goods_pid="";
		try {
			boolean flag=NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/inspectionImg/", storePath, imgPath);
			if(flag){
				sql = "UPDATE order_details t SET t.picturepath = ? WHERE t.orderid = ? AND t.id = ?;";
				List<String> lstValues = Lists.newArrayList();
				lstValues.add(Util.PIC_URL+storePath);
				lstValues.add(orderid);
				lstValues.add(odid);
				String runSql = DBHelper.covertToSQL(sql, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));
				
				stmt = conn1.prepareStatement(sql);
				stmt.setString(1, Util.PIC_URL+storePath+"");
				stmt.setString(2, orderid);
				stmt.setString(3, odid);
				stmt.executeUpdate();
				sql="select id from inspection_picture where orderid=? and odid=? and pic_path=? and isdelete=0";
				stmt=conn1.prepareStatement(sql);
				stmt.setString(1, orderid);
				stmt.setString(2, odid);
				stmt.setString(3, Util.PIC_URL+storePath+"");
				rs=stmt.executeQuery();
				if(rs.next()){
					sql="update inspection_picture set pic_path=?,updatetime=now() where orderid=? and odid=? and isdelete=0 and pic_path=?";
					stmt=conn1.prepareStatement(sql);
					stmt.setString(1, Util.PIC_URL+storePath+"");
					stmt.setString(2, orderid);
					stmt.setString(3, odid);
					stmt.setString(4, Util.PIC_URL+storePath+"");
					stmt.executeUpdate();
					
					lstValues = Lists.newArrayList();
					lstValues.add(Util.PIC_URL+storePath);
					lstValues.add(orderid);
					lstValues.add(odid);
					lstValues.add(Util.PIC_URL+storePath);
					runSql = DBHelper.covertToSQL(sql, lstValues);
					SendMQ.sendMsg(new RunSqlModel(runSql));
				}else{
					sql="select goods_pid from order_details where orderid=? and id=?";
					stmt=conn1.prepareStatement(sql);
					stmt.setString(1,orderid);
					stmt.setString(2,odid);
					rs=stmt.executeQuery();
					if(rs.next()){
						goods_pid=rs.getString("goods_pid");
					}
					sql="INSERT INTO inspection_picture (pid,orderid,odid,pic_path,createtime) values(?,?,?,?,now())";
					stmt=conn1.prepareStatement(sql);
					stmt.setString(1, goods_pid);
					stmt.setString(2, orderid);
					stmt.setString(3, odid);
					stmt.setString(4, Util.PIC_URL+storePath+"");
					stmt.executeUpdate();
					
					lstValues = Lists.newArrayList();
					lstValues.add(goods_pid);
					lstValues.add(orderid);
					lstValues.add(odid);
					lstValues.add(Util.PIC_URL+storePath);
					runSql = DBHelper.covertToSQL(sql, lstValues);
					SendMQ.sendMsg(new RunSqlModel(runSql));
				}
				if(index==1){
					sql="updtae id_relationtable set picturepath='"+storePath+"' where orderid=? and odid=?";
					/*stmt = conn.prepareStatement(sql);
					stmt.setString(1, orderid);
					stmt.setString(2, odid);
					stmt.executeUpdate();*/
					lstValues = Lists.newArrayList();
					lstValues.add(orderid);
					lstValues.add(odid);
					runSql = DBHelper.covertToSQL(sql, lstValues);
					SendMQ.sendMsg(new RunSqlModel(runSql));
				}
			}else{
				//上传图片服务器失败记录s
				System.out.println("===============上传验货图片失败==========");
				sql="insert into pic_fail (storePath,imgPath,orderid,odid,index) values (?,?,?,?,?)";
				/*stmt = conn.prepareStatement(sql);
				stmt.setString(1, storePath);
				stmt.setString(2, imgPath);
				stmt.setString(3, orderid);
				stmt.setString(4, odid);
				stmt.setInt(5, index);
				stmt.executeUpdate();*/
				List<String> lstValues = Lists.newArrayList();
				lstValues.add(storePath);
				lstValues.add(imgPath);
				lstValues.add(orderid);
				lstValues.add(odid);
				lstValues.add(String.valueOf(index));
				lstValues.add(Util.PIC_URL+storePath);
				String runSql = DBHelper.covertToSQL(sql, lstValues);
				SendMQ.sendMsg(new RunSqlModel(runSql));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closeConnection(conn1);
		}
	}
}
