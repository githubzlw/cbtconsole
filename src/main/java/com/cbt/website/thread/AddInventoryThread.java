package com.cbt.website.thread;

import com.cbt.jdbc.DBHelper;
import com.cbt.util.NewFtpUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		Connection conn = DBHelper.getInstance().getConnection2();// 仓库不用
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="";
		String goods_pid="";
		try {
			boolean flag=NewFtpUtil.uploadFileToRemote("104.247.194.50", 21, "importweb", "importftp@123", "/inspectionImg/", storePath, imgPath);
			if(flag){
				sql = "UPDATE order_details t SET t.picturepath = ? WHERE t.orderid = ? AND t.id = ?;";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
				stmt.setString(2, orderid);
				stmt.setString(3, odid);
				stmt.executeUpdate();
				stmt = conn1.prepareStatement(sql);
				stmt.setString(1, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
				stmt.setString(2, orderid);
				stmt.setString(3, odid);
				stmt.executeUpdate();
				sql="select id from inspection_picture where orderid=? and odid=? and pic_path=? and isdelete=0";
				stmt=conn1.prepareStatement(sql);
				stmt.setString(1, orderid);
				stmt.setString(2, odid);
				stmt.setString(3, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
				rs=stmt.executeQuery();
				if(rs.next()){
					sql="update inspection_picture set pic_path=?,updatetime=now() where orderid=? and odid=? and isdelete=0 and pic_path=?";
					stmt=conn1.prepareStatement(sql);
					stmt.setString(1, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
					stmt.setString(2, orderid);
					stmt.setString(3, odid);
					stmt.setString(4, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
					stmt.executeUpdate();
					stmt=conn.prepareStatement(sql);
					stmt.setString(1, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
					stmt.setString(2, orderid);
					stmt.setString(3, odid);
					stmt.setString(4, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
					stmt.executeUpdate();
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
					stmt.setString(4, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
					stmt.executeUpdate();
					stmt=conn.prepareStatement(sql);
					stmt.setString(1, goods_pid);
					stmt.setString(2, orderid);
					stmt.setString(3, odid);
					stmt.setString(4, "https://img.import-express.com/importcsvimg/inspectionImg/"+storePath+"");
					stmt.executeUpdate();
				}
				if(index==1){
					sql="updtae id_relationtable set picturepath='"+storePath+"' where orderid=? and odid=?";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, orderid);
					stmt.setString(2, odid);
					stmt.executeUpdate();
				}
			}else{
				//上传图片服务器失败记录s
				System.out.println("===============上传验货图片失败==========");
				sql="insert into pic_fail (storePath,imgPath,orderid,odid,index) values (?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, storePath);
				stmt.setString(2, imgPath);
				stmt.setString(3, orderid);
				stmt.setString(4, odid);
				stmt.setInt(5, index);
				stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn1);
		}
	}
}
