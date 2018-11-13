package com.cbt.email.dao;

import com.cbt.email.entity.EmailReceive;
import com.cbt.email.entity.EmailReceive1;
import com.cbt.jdbc.DBHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class IGuestBookDaoImpl implements IEmailReceiveDao {

	@Override
	public int add(EmailReceive receive) {
		
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int result = 0;
			int id = 0;
			String sql = "insert into email_receive(title,content,send_date,code_date,user_id,admin_id,question_id,email) values(?,?,now(),now(),?,?,?,?)";
			conn = DBHelper.getInstance().getConnection();
			try {
				stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
				
				stmt.setString(1, receive.getTitle());
				stmt.setString(2, receive.getContent());
				stmt.setInt(3, receive.getUserId());
				stmt.setInt(4, receive.getAdminId());
				stmt.setInt(5, receive.getQuestionid());
				stmt.setString(6, receive.getEmail());
				result = stmt.executeUpdate();
				if (result == 1) {
					rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						id = rs.getInt(1);
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
				DBHelper.getInstance().closeConnection(conn);
			}
			return id;
		}

	@Override
	public List<EmailReceive> getall(EmailReceive er) {
		List<EmailReceive> list = new ArrayList<EmailReceive>();
		String sql = "select * from(select g.id, g.content ,g.create_time,ifnull(g.id,'')question_id, "
				+ "aru.admName,ifnull(0,0)user_id,g.reply_content,g.reply_time,ifnull(0,0)include,ifnull('','')original_message  from guestbook g  left join admin_r_user aru on g.user_id=aru.userid where g.id=? "
				+ "UNION "
				+ "select er.id, er.content,er.send_date,er.question_id, gk.user_name,ifnull(1,1)user_id,er.re_content,er.re_date"
				+ ", er.include,er.original_message  from email_receive  er  left join    guestbook gk on gk.id=er.question_id "
				+ "where er.question_id=?) ec  limit ?,?  ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		conn = DBHelper.getInstance().getConnection();
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, er.getId());
			stmt.setInt(2, er.getId());
			
			stmt.setInt(3, er.getStart());
			stmt.setInt(4, er.getEnd());
			rs = stmt.executeQuery();
			while(rs.next()) {
				EmailReceive info =  new EmailReceive();
				info.setId(rs.getInt("id"));
				info.setUserId(rs.getInt("user_id"));
				info.setQuestionid(rs.getInt("question_id"));
				info.setContent(rs.getString("content"));
				info.setSend(rs.getString("admName"));
				info.setSendDate(rs.getString("create_time"));
				info.setReDate(rs.getString("reply_time"));
				info.setRecontent(rs.getString("reply_content"));
				info.setInclude(rs.getInt("include"));
				info.setOriginalmessage(rs.getString("original_message"));
				list.add(info);
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
	public List<EmailReceive> getall1(int id,EmailReceive er) {
		List<EmailReceive> list = new ArrayList<EmailReceive>();
		String sql = "select er.id, er.content,er.send_date,er.question_id, gk.user_name,ifnull(1,1)user_id,er.re_content,er.re_date"
				+ ", er.include,er.original_message  from email_receive  er  left join    guestbook gk on gk.id=er.question_id "
				+ "where er.question_id=?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String username="";
		conn = DBHelper.getInstance().getConnection();
		try {
			sql += " order by er.send_date limit ?,? ";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setInt(2, er.getStart());
			stmt.setInt(3, er.getEnd());
			rs = stmt.executeQuery();
			while(rs.next()) {
				EmailReceive info =  new EmailReceive();
				username=rs.getString("user_name");
				info.setId(rs.getInt("id"));
				info.setQuestionid(rs.getInt("question_id"));
				info.setContent(rs.getString("content"));
				if(username!=null&&!"".equals(username)){
					info.setSend(rs.getString("user_name"));
					}else{
						info.setSend(rs.getString("email"));	
					}
				info.setSendDate(rs.getString("send_date"));
				info.setReDate(rs.getString("er.re_date"));
				info.setRecontent(rs.getString("re_content"));
				info.setInclude(rs.getInt("include"));
				info.setOriginalmessage(rs.getString("original_message"));
				list.add(info);
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
	public List<EmailReceive> getall2(EmailReceive er) {
		List<EmailReceive> list = new ArrayList<EmailReceive>();
		String sql = "select er.content,er.send_date,er.id, gk.user_name ,ifnull(er.question_id,er.id)question_id,er.re_content,er.re_date,er.email, er.include,er.original_message "
				+ " from email_receive  er left join   guestbook gk on gk.user_id=er.user_id  where  1=1 ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String username="";
		conn = DBHelper.getInstance().getConnection();
		try {
			sql += " order by er.send_date limit ?,? ";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, er.getStart());
			stmt.setInt(2, er.getEnd());
			rs = stmt.executeQuery();
			while(rs.next()) {
				EmailReceive info =  new EmailReceive();
				username=rs.getString("user_name");
				info.setId(rs.getInt("id"));
				info.setQuestionid(rs.getInt("question_id"));
				info.setContent(rs.getString("content"));
				if(username!=null&&!"".equals(username)){
				info.setSend(rs.getString("user_name"));
				}else{
					info.setSend(rs.getString("email"));	
				}
				info.setReDate(rs.getString("er.re_date"));
				info.setRecontent(rs.getString("re_content"));
				info.setSendDate(rs.getString("send_date"));
				info.setInclude(rs.getInt("include"));
				info.setOriginalmessage(rs.getString("original_message"));
				list.add(info);
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
	public EmailReceive getEmail(int id) {
		EmailReceive info =null;
		String sql = "select *  from email_receive  er left join   guestbook gk on gk.user_id=er.user_id  where  er.id=? ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int questionid=0;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while(rs.next()) {
			 info =  new EmailReceive();
			 questionid=rs.getInt("question_id");
				info.setId(rs.getInt("id"));
				info.setTitle(rs.getString("title"));
				if(questionid!=0){
				info.setQuestionid(rs.getInt("question_id"));
				}
				info.setEmail(rs.getString("email"));
				info.setContent(rs.getString("content"));
				info.setSendDate(rs.getString("send_date"));
				
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
		return info;
	}

	@Override
	public int reply(int id, String content, String title, Date date) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		sql = "update email_receive set re_content=?,re_date=?,status=?,re_title=? where id=?";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, content);
			stmt.setDate(2, date);
			stmt.setInt(3, 1);// 状态改为已回复
			stmt.setString(4, title);
			stmt.setInt(5, id);
			result = stmt.executeUpdate();
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
	public int add1(EmailReceive receive) {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		int id = 0;
		String sql = "insert into email_receive(title,content,send_date,code_date,user_id,admin_id,question_id,email,include,original_message) values(?,?,now(),now(),?,?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, receive.getTitle());
			stmt.setString(2, receive.getContent());
			stmt.setInt(3, receive.getUserId());
			stmt.setInt(4, receive.getAdminId());
			stmt.setInt(5, receive.getQuestionid());
			stmt.setString(6, receive.getEmail());
			stmt.setInt(7, receive.getInclude());
			stmt.setString(8, receive.getOriginalmessage());
			
			result = stmt.executeUpdate();
			if (result == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getInt(1);
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return id;
	}

	@Override
	public int getalltotal(EmailReceive er) {
		int total=0;
			EmailReceive list = new EmailReceive();
			String sql = "select count(*) from(select g.id, g.content ,g.create_time,ifnull(g.id,'')question_id, "
					+ "aru.admName,ifnull(0,0)user_id,g.reply_content,g.reply_time,ifnull(0,0)include,ifnull('','')original_message  from guestbook g  left join admin_r_user aru on g.user_id=aru.userid where g.id=? "
					+ "UNION "
					+ "select er.id, er.content,er.send_date,er.question_id, gk.user_name,ifnull(1,1)user_id,er.re_content,er.re_date"
					+ ", er.include,er.original_message  from email_receive  er  left join    guestbook gk on gk.id=er.question_id "
					+ "where er.question_id=?) ec    ";
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			conn = DBHelper.getInstance().getConnection();
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setInt(1, er.getId());
				stmt.setInt(2, er.getId());
				
			
				rs = stmt.executeQuery();
				if(rs.next()) {
				total=rs.getInt(1);
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
	public int getalltotal1(int id, EmailReceive er) {
		int total=0;
		String sql = "select count(*)  from email_receive  er  left join    guestbook gk on gk.id=er.question_id "
				+ "where er.question_id=?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String username="";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			if(rs.next()) {
				total=rs.getInt(1);
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
	public int getalltotal2(EmailReceive er) {
		int total=0;
		String sql = "select count(*) from email_receive  er left join   guestbook gk on gk.user_id=er.user_id  where  1=1 ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String username="";
		conn = DBHelper.getInstance().getConnection();
		try {
		
			stmt = conn.prepareStatement(sql);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				total=rs.getInt(1);
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
	public List<EmailReceive1> getall(String orderNo) {
		List<EmailReceive1> list = new ArrayList<EmailReceive1>();
		String sql = "select * from(select er.id,er.cname,er.title,er.content,er.create_time,er.sale_name,er.customer_id,er.orderid from email_receive1 er where er.orderid=? "
				+ " union "
				+ "select ei.id,ei.cname,ei.title,ei.content,ei.create_time,ei.sale_name,ei.customer_id,ei.orderid from email_info ei where ei.orderid=?) a order by a.create_time desc" ;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String username="";
		conn = DBHelper.getInstance().getConnection();
		try {
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setString(2, orderNo);
			rs = stmt.executeQuery();
			while(rs.next()) {
				EmailReceive1 info =  new EmailReceive1();
				
				String content=rs.getString("content");
				
					try{
						String content1="";
					String encoding="utf-8";
			                File file=new File(content);
			                if(file.isFile() && file.exists()){ //判断文件是否存在
			                    InputStreamReader read = new InputStreamReader(
			                    new FileInputStream(file),encoding);//考虑到编码格式
			                    BufferedReader bufferedReader = new BufferedReader(read);
			                    String lineTxt = null;
			                    while((lineTxt = bufferedReader.readLine()) != null){
			                        content1+=lineTxt;
			                    }
			                    read.close();
			                }
						info.setContent(content1);
						}catch(Exception e){
							
						}
					
				
				info.setId(rs.getInt("id"));
				info.setCname(rs.getString("cname"));
				
				info.setCreateTime(rs.getString("create_time"));
				info.setSaleName(rs.getString("sale_name"));
				info.setOrderid(rs.getString("orderid"));
				info.setCustomerId(rs.getInt("customer_id"));
				info.setTitle(rs.getString("title"));
				list.add(info);
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
	
	}


