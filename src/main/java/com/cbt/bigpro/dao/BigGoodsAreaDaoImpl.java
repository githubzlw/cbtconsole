package com.cbt.bigpro.dao;

import com.cbt.bigpro.bean.AliCategoryPojo;
import com.cbt.bigpro.bean.BigGoodsArea;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BigGoodsAreaDaoImpl implements BigGoodsAreaDao {
	public List<BigGoodsArea> getBigGoodsIfo(String id, int page, int pagesize) {
	  return  null;
	}

/*	@Override
	public List<BigGoodsArea> getBigGoodsIfo(String id, int page, int pagesize) {
		// TODO Auto-generated method stub
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		ResultSet rs2 = null;
 		PreparedStatement stmt = null;
 		PreparedStatement stmt2 = null;
 		List<BigGoodsArea> res=new ArrayList<BigGoodsArea>();
 		String sql ="";
 		if(id==""||id==null){
 			sql ="select SQL_CALC_FOUND_ROWS bg.gid,bg.gurl,bg.num,bg.title,bg.price,bc.category from bigGoodsArea_detail as bg left join bigGoodscategory as bc"+
 		" on bg.categoryId = bc.id where bg.flag = 0 limit ?,?";
 			try{
 			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, (page-1)*pagesize);
			stmt.setInt(2, pagesize);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			int total = 0 ;
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while(rs.next()){
				BigGoodsArea temp=new BigGoodsArea();
				temp.setGoodsId(rs.getString("gid"));
				temp.setGoodsurl(rs.getString("gurl"));
				temp.setDiscount(rs.getDouble("discount"));
				temp.setPrice(rs.getDouble("price"));
				temp.setNum(rs.getDouble("num"));
				temp.setTitle(rs.getString("title"));
				temp.setTotal(total);
				res.add(temp);
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
 		}else{
 			if(page==-2){
 				sql ="select SQL_CALC_FOUND_ROWS bg.gid,bg.gurl,bg.discount,bg.num,bg.title,bg.price,bc.category from bigGoodsArea_detail as bg left join bigGoodscategory as bc"+
 				 		" on bg.categoryId = bc.id where bg.flag = 0 and bg.gid = ?";
 			 try {
 				stmt = conn.prepareStatement(sql);
 				stmt.setString(1,id);
 				rs = stmt.executeQuery();
 				stmt2 = conn.prepareStatement("select found_rows();");
 				rs2 = stmt2.executeQuery();
 				int total = 0 ;
 				if(rs2.next()){
 					total = rs2.getInt("found_rows()");
 				}
 				while(rs.next()){
 					BigGoodsArea temp=new BigGoodsArea();
 					temp.setGoodsId(rs.getString("gid"));
 					temp.setGoodsurl(rs.getString("gurl"));
 					temp.setCategory(rs.getString("category"));
 					temp.setDiscount(rs.getDouble("discount"));
 					temp.setPrice(rs.getDouble("price"));
 					temp.setTitle(rs.getString("title"));
 					temp.setNum(rs.getDouble("num"));
 					temp.setTotal(total);
 					res.add(temp);
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
 			}
 		}
		return res;
	}*/

	@Override
	public int save(BigGoodsArea bean) {
		// TODO Auto-generated method stub
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		PreparedStatement stmt_1 = null;
 		PreparedStatement stmt_2 = null;
 		PreparedStatement stmt_3 = null;
 		PreparedStatement stmt3 = null;
 		PreparedStatement stmt5 = null;
 		ResultSet  res_1 = null;
 		ResultSet  res_3 = null;
 		ResultSet  res1 = null;
 		int rs =0;
 		int rs1 = 0;
 		String path  = "";
 		String  category ="";
 		String sql5 ="select count(*) as num from biggoodscategory  where catid1 = ?";
 			try{
 				String  sql_1 = "select * from ali_category where cid = ? ";
				stmt_1 =conn.prepareStatement(sql_1);
				stmt_1.setString(1,bean.getCatid1());
				res_1 = stmt_1.executeQuery();
				while(res_1.next()){
					 path = res_1.getString("path").split(",")[0];
				}
				//得到大类别
				String sql_3 ="select * from ali_category where cid =? ";
				stmt_3 =conn.prepareStatement(sql_3);
				stmt_3.setString(1,path);
				res_3= stmt_3.executeQuery();
				while(res_3.next()){
					 category = res_3.getString("category");
				}
				
 				stmt5 = conn.prepareStatement(sql5);
 				stmt5.setString(1, bean.getCatid1());
 				res1 = stmt5.executeQuery();
 				while(res1.next()){
 					rs =Integer.parseInt(res1.getString("num"));
 				}
 				if(rs==0){
 					String sql_2 = "insert into biggoodscategory(category,catid,catid1,category1) values(?,?,?,?)";
 					stmt_2 =conn.prepareStatement(sql_2);
 					stmt_2.setString(1,bean.getCategory());
 					stmt_2.setString(2,path);
 					stmt_2.setString(3,bean.getCatid1());
 					stmt_2.setString(4,category);
 					stmt_2.executeUpdate();
 					}
			String sql3 = "insert into biggoodsarea_detail(gid,gurl,gimg,weight,price,num,discount,title,flag,keyWord,catid,catid1) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt3= conn.prepareStatement(sql3);
			stmt3.setString(1, bean.getGoodsId());
			stmt3.setString(2, bean.getGoodsurl());
			stmt3.setString(3, bean.getImg());
			stmt3.setString(4, bean.getWeight());
			stmt3.setDouble(5, bean.getPrice());
			stmt3.setDouble(6, bean.getNum());
			stmt3.setDouble(7,bean.getDiscount());
			stmt3.setString(8,bean.getTitle());
			stmt3.setInt(9,0);
			stmt3.setString(10,bean.getKeyWord());
			stmt3.setString(11,path);
			stmt3.setString(12,bean.getCatid1());
			rs1 = stmt3.executeUpdate();
 			} catch (Exception e) {
				e.printStackTrace();
			} 
				finally {
				if (stmt != null) {
					try {
						stmt.close();
						stmt5.close();
						stmt3.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				DBHelper.getInstance().closeConnection(conn);
			}
 			
 	  return rs1;
	}

	@Override
	public int updateBigGoodsArea(String[] pids) {
		// TODO Auto-generated method stub
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		int rs = 0;
 		for(String row:pids){
 			String sql ="update biggoodsarea_detail set flag =1 where gid =?";
 	 		try {
 				stmt = conn.prepareStatement(sql);
 				stmt.setString(1, row);
 				rs = stmt.executeUpdate();
 				
 			} catch (Exception e) {
 				// TODO: handle exception
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
 		
		return rs;
	}

	@Override
	public int isExistence(String pid) {
		// TODO Auto-generated method stub
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		ResultSet  res =null;
 		int row =0;
 		String sql ="select count(*) as num from biggoodsarea_detail where gid =?  and flag = 0 ";
 	 		try {
 				stmt = conn.prepareStatement(sql);
 				stmt.setString(1, pid);
 				res = stmt.executeQuery();
 				while(res.next()){
 					row =Integer.parseInt(res.getString("num"));
 				}
 				
 			} catch (Exception e) {
 				// TODO: handle exception
 				e.printStackTrace();
 			}
 	 		finally {
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

	@Override
	public List<AliCategoryPojo> getAliCategory() {
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		ResultSet  res =null;
 		List<AliCategoryPojo>  list = new ArrayList<AliCategoryPojo>();
 		String sql ="select * from ali_category where LENGTH(0+path)=LENGTH(path) order by path*1 ";
 	 		try {
 				stmt = conn.prepareStatement(sql);
 				res = stmt.executeQuery();
 				while(res.next()){
 					AliCategoryPojo category = new AliCategoryPojo();
 					category.setCategory(res.getString("category"));
 					category.setCid(res.getString("cid"));
 					category.setId(String.valueOf(res.getInt("id")));
 					category.setPath(res.getString("path"));
 					list.add(category);
 				}
 				
 			} catch (Exception e) {
 				// TODO: handle exception
 				e.printStackTrace();
 			}
 	 		finally {
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
	public List<AliCategoryPojo> getSubType(String id) {
		// TODO Auto-generated method stub
		String sql ="select * from ali_category where path like  '"+id+",%' and LENGTH(replace(path,',','--'))-LENGTH(path)=1 order by path*1 ";
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		PreparedStatement stmt_1 = null;
 		ResultSet  res =null;
 		ResultSet  res_1 =null;
 		String  catid1 = "";
 		List<AliCategoryPojo>  list = new ArrayList<AliCategoryPojo>();
 	 		try {
 				stmt = conn.prepareStatement(sql);
 				res = stmt.executeQuery();
 				while(res.next()){
 					AliCategoryPojo category = new AliCategoryPojo();
 					category.setCategory(res.getString("category"));
 					category.setCid(res.getString("cid"));
 					category.setId(String.valueOf(res.getInt("id")));
 					category.setPath(res.getString("path"));
 					catid1 = res.getString("cid");
 					String sql_1 = "select count(*) as num from biggoodsarea_detail where catid1 = ?  and flag = 0 ";
 					stmt_1 = conn.prepareStatement(sql_1);
 					stmt_1.setString(1, catid1);
 					res_1 = stmt_1.executeQuery();
 					while(res_1.next()){
 						category.setNum(res_1.getDouble("num"));
 					}
 					list.add(category);
 				}
 				
 			} catch (Exception e) {
 				// TODO: handle exception
 				e.printStackTrace();
 			}
 	 		finally {
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
	public List<BigGoodsArea> findGoodsByCategoryId(String catid,String catid1) {
		// TODO Auto-generated method stub
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		ResultSet  res =null;
		String sql ="";
		List<BigGoodsArea> list = new ArrayList<BigGoodsArea>();
		if((catid!=null||catid!="")&&(catid1!=null||catid1!="")){
			sql ="select * from biggoodsarea_detail where catid = ? and catid1 = ?  and flag = 0 ";
		}
		//if((catid!=null||catid!="")&&(catid1!=null||catid1!=""))
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,catid);
				stmt.setString(2,catid1);
				res = stmt.executeQuery();
				while(res.next()){
					BigGoodsArea  bean = new BigGoodsArea();
					bean.setId(res.getInt("id"));
					bean.setTitle(res.getString("title"));
					bean.setImg(res.getString("gimg"));
					bean.setNum(res.getDouble("num"));
					bean.setPrice(res.getDouble("price"));
					bean.setDiscount(res.getDouble("discount"));
					list.add(bean);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 	 		finally {
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
	public int delteCommodityByid(String id) {
		// TODO Auto-generated method stub
		String sql = "update biggoodsarea_detail set flag= 1 where id = ? ";
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		int rs =  0;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,id);
				rs = stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 	 		finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
	       DBHelper.getInstance().closeConnection(conn);
	  }
		return rs;
	}
}
