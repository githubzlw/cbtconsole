package com.cbt.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.cbt.jdbc.DBHelper;
import com.google.common.collect.Lists;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

/***
 * 邮件中个人中心自动登录辅助功能
* @ClassName: UUIDUtil 
* @Description: TODO(userid或者email必须有一个有效参数) 
* @author lyb
* @date 2016年9月14日 下午4:30:52 
*
 */
public class UUIDUtil {
	//查询uuid是否有效, 无效则替换。 userid或者email必须有一个参数
	public static String getEffectiveUUID(int userid, String email){
		int i = 0;
		if(userid<=0 && ( email==null || "".equals(email))){
			return "";
		}
		String uuid = "";
		//为了线上与本地的uuidCreatetime相同
		Calendar cd = Calendar.getInstance();
		int yy = cd.get(Calendar.YEAR);
		int mm = cd.get(Calendar.MONDAY)+1;
		int dd = cd.get(Calendar.DATE);
		int hh = cd.get(Calendar.HOUR_OF_DAY);
		int mi = cd.get(Calendar.MINUTE);
		int ss = cd.get(Calendar.SECOND);
		String time = yy+"-"+mm+"-"+dd+" "+hh+":"+mi+":"+ss;
		//根据userid或者email查询最近3天的uuid是否有效
		String sql = "select uuid from user ";
		if(userid==0){
			sql += "where email=? and uuid is not null and DATE_ADD(uuidCreatetime ,INTERVAL 3 DAY)> now()";
		}else{
			sql += "where id=? and uuid is not null and DATE_ADD(uuidCreatetime ,INTERVAL 3 DAY)> now()";
		}
		String sql2 = "update user set uuid=?,uuidCreatetime=? where id=?";
		Connection con = DBHelper.getInstance().getConnection();
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			if(userid==0){
				pst.setString(1, email);
			}else{
				pst.setInt(1, userid);
			}
			rs = pst.executeQuery();
			if(rs.next()){
				i = 1; //标记为1 表示有
				uuid = rs.getString("uuid");
			}
			//如果i为0，表示不存在uuid或者uuid已过期，则重新创建一个
			if(i==0){
				uuid = UUID.randomUUID().toString();
				pst1 = con.prepareStatement(sql2);
				pst1.setString(1, uuid);
				pst1.setString(2, time);
				pst1.setInt(3, userid);
				i = pst1.executeUpdate();
				
				List<String> lstValues = Lists.newArrayList();
				lstValues.add(uuid);
				lstValues.add(time);
				lstValues.add(String.valueOf(userid));
				String runSql = DBHelper.covertToSQL(sql2, lstValues);
				i = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst1!=null){
				try {
					pst1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				}
			if(pst2!=null){
				try {
					pst2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(con);
		DBHelper.getInstance().closeConnection(conn);
		return uuid;
	}
	
	/**
	 * 拼接自动登录路径 lyb
	* url: 实际访问路径，uuid:用户的uuid
	 */
	public static String getAutoLoginPath(String url, String uuid) throws UnsupportedEncodingException{
		if(uuid==null || "".equals(uuid) || url==null || "".equals(url)){
			return "";
		}
		String eurl = URLEncoder.encode(url, "UTF-8");
		String path = "/user/toEmailPage?uid="+uuid+"&url="+eurl;
		return path;
	}
/*	public static void main(String[] args) {
		getEffectiveUUID(2113,"");
	}*/
	
	/**
	 * 生成密钥，最大32位
	 * @param numbers
	 * @return
	 */
	public static String generateUUID(int numbers){
		String tmp = UUID.randomUUID().toString().replace("-", "");
		if (numbers > tmp.length()) {
			return null;
		}
		return tmp.substring(0, numbers);
	}
	
}
