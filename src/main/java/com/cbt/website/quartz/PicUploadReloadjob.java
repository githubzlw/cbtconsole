package com.cbt.website.quartz;

import com.cbt.jdbc.DBHelper;
import com.cbt.util.NewFtpUtil;
import com.cbt.util.Util;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PicUploadReloadjob implements Job{
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Connection conn70 = DBHelper.getInstance().getConnection2();// 仓库不用
		Connection conn27 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			String sql="select * from pic_fail where flag=0";
			stmt=conn27.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				String storePath=rs.getString("storePath");
				String imgPath=rs.getString("imgPath");
				String orderid=rs.getString("orderid");
				String goodsid=rs.getString("goodsid");
				boolean flag=NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/inspectionImg/", storePath, imgPath);
				if(flag){
					sql = "update pic_fail set flag=1 where storePath=? and imgPath=? and orderid=? and goodsid=?";
					stmt = conn27.prepareStatement(sql);
					stmt.setString(1, storePath);
					stmt.setString(2, imgPath);
					stmt.setString(3, orderid);
					stmt.setString(4, goodsid);
					stmt.executeUpdate();
					sql = "UPDATE order_details t SET t.picturepath = ? WHERE t.orderid = ? AND t.goodsid = ?;";
					stmt = conn27.prepareStatement(sql);
					stmt.setString(1, Util.PIC_URL+storePath+"");
					stmt.setString(2, orderid);
					stmt.setString(3, goodsid);
					stmt.executeUpdate();
					stmt = conn70.prepareStatement(sql);
					stmt.setString(1, Util.PIC_URL+storePath+"");
					stmt.setString(2, orderid);
					stmt.setString(3, goodsid);
					stmt.executeUpdate();
					sql="select id from inspection_picture where orderid=? and goods_id=? and isdelete=0";
					stmt=conn27.prepareStatement(sql);
					stmt.setString(1, orderid);
					stmt.setString(2, goodsid);
					rs=stmt.executeQuery();
					if(rs.next()){
						sql="update inspection_picture set pic_path=?,updatetime=now() where orderid=? and goods_id=? and isdelete=0";
						stmt=conn27.prepareStatement(sql);
						stmt.setString(1, Util.PIC_URL+storePath+"");
						stmt.setString(2, orderid);
						stmt.setString(3, goodsid);
						stmt.executeUpdate();
						stmt=conn70.prepareStatement(sql);
						stmt.setString(1, Util.PIC_URL+storePath+"");
						stmt.setString(2, orderid);
						stmt.setString(3, goodsid);
						stmt.executeUpdate();
					}else{
						String goods_pid="";
						sql="select goods_pid from order_details where orderid=? and goodsid=?";
						stmt=conn27.prepareStatement(sql);
						stmt.setString(1,orderid);
						stmt.setString(2,goodsid);
						rs=stmt.executeQuery();
						if(rs.next()){
							goods_pid=rs.getString("goods_pid");
						}
						sql="INSERT INTO inspection_picture (pid,orderid,goods_id,pic_path,createtime) values(?,?,?,?,now())";
						stmt=conn27.prepareStatement(sql);
						stmt.setString(1, goods_pid);
						stmt.setString(2, Util.PIC_URL+storePath+"");
						stmt.setString(3, orderid);
						stmt.setString(4, goodsid);
						stmt.executeUpdate();
						stmt=conn70.prepareStatement(sql);
						stmt.setString(1, goods_pid);
						stmt.setString(2, Util.PIC_URL+storePath+"");
						stmt.setString(3, orderid);
						stmt.setString(4, goodsid);
						stmt.executeUpdate();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn70);
			DBHelper.getInstance().closeConnection(conn27);
		}
	}

}
