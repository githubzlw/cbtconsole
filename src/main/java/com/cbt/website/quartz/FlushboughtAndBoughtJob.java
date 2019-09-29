package com.cbt.website.quartz;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.jdbc.DBHelper;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.utli.NewSendMQ;
import net.sf.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 刷新新购物车页面展示买过该商品的客户还买过哪些商品数据
 */
public class FlushboughtAndBoughtJob implements Job{

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try{
			NewSendMQ sendMQ = new NewSendMQ();
			//客户买过的产品并且是在线商品
//			List<OrderDetailsBean> pidLiusts=getAllPidss();
//			int psiaze=pidLiusts.size();
//			//查询所有的买过的商品
//			List<String> adList=new ArrayList<String>();
//			for(int i=1;i<=psiaze;i++){
//				Map<String,Object> map=new HashMap<String,Object>();
//				map.put("type","1");
//				OrderDetailsBean o=pidLiusts.get(i);
//				System.out.println("开始缓存第【"+i+"】个商品；pid为："+o.getGoods_pid()+"总共["+psiaze+"]个商品");
//				//买过该商品的客户还买过其他那些商品
//				List<String> buyPidList=getBuyPidList(o.getGoods_pid());
//				if(buyPidList.size()==0){
//					//买过此类别商品的客户还买过哪些商品
//					buyPidList=boughtAndCatId(o.getGoods_pid(),o.getGoodscatid());
//				}
//				if(buyPidList.size()==0){
//					buyPidList=saleMoreGoods(o.getGoods_pid());
//				}
//				if(buyPidList.size()>0){
//					map.put(o.getGoods_pid(),buyPidList);
//				}else{
//					System.out.println(o.getGoods_pid());
//				}
//				map.put(o.getGoods_pid(),buyPidList);
//				JSONObject json = JSONObject.fromObject(map);
//				sendMQ.sendRecommend(json.toString());
////				adList.add(o.getGoods_pid()+"@"+buyPidList.size());
//			}
			//存放所有上架产品的价格，搜索图片，名称等信息
//			List<OrderDetailsBean> pidLiusts=getAllGoodsInfos();
//			int idex=pidLiusts.size()%100;
//			if(idex == 0){
//				idex=pidLiusts.size()/100;
//			}else{
//				idex=pidLiusts.size()/100+1;
//			}
//			for(int i=1;i<=idex;i++){
//				System.out.println("开始刷新第【"+i+"】也数据");
//				int page = (i - 1) * 100;
//				String goodsInfo=getAllGoodsInfo(page);
//				if(StringUtil.isNotBlank(goodsInfo) && goodsInfo.length()>50){
//					try{
//						JSONObject json1 = JSONObject.fromObject(goodsInfo);
//						sendMQ.sendRecommend(json1.toString());
//					}catch (Exception e){
//						System.out.println("第【"+i+"】页数据缓存错误");
//					}
//				}
//			}
			//刷新线上guess you like数据
			//查询类别数据销售、
			List<String> allUser =getAllUser();
			for(String userId:allUser){
				//用户最近下过的订单
				List<String> allCatid=getCatidByUser(userId);
				List<String> pids=new ArrayList<String>();
				StringBuilder sbPid=new StringBuilder();
				for(int i=0;i<allCatid.size();i++){
					String catid=allCatid.get(i);
					//根据类别查询卖过的产品数据
					List<String> pid=getPidByCatid(catid);
					if(pid.size()>0 && pids.size()<=10 && !pids.contains(pid.get(0))){
						pids.add(pid.get(0));
					}else if(pids.size()>=10){
						break;
					}
				}
				outterLoop:for(int j=1;j<100;j++){
					if(pids.size()<=10){
						for(int i=0;i<allCatid.size();i++){
							String catid=allCatid.get(i);
							//根据类别查询卖过的产品数据
							List<String> pid=getPidByCatid(catid);
							if(pid.size()>=(j+1) && pids.size()<=10 && !pids.contains(pid.get(j))){
								pids.add(pid.get(j));
							}else if(pids.size()>=10){
								break outterLoop;
							}
						}
					}
				}
//				pids=removeDuplicate(pids);
				if(pids.size()>10){
					pids= pids.subList(0, 10);
				}
				if(pids.size()>0){
					System.out.println("用户【"+userId+"】推荐商品pid：【"+pids.toString()+"】");
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("type","1");
					map.put(userId,pids);
					JSONObject json = JSONObject.fromObject(map);
					sendMQ.sendRecommend(json.toString());
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public   static   List  removeDuplicate(List list)  {
		for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
			for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
				if  (list.get(j).equals(list.get(i)))  {
					list.remove(j);
				}
			}
		}
		return list;
	}

	/**
	 * 最近半年下过订单的用户
	 * @return
	 */
	public List<String> getAllUser(){
		List<String> list=new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql=" SELECT DISTINCT u.id FROM USER u INNER JOIN orderinfo oi ON u.id=oi.user_id\n" + " WHERE u.id IN (\n" + " SELECT id FROM USER WHERE email NOT LIKE '%qq.com%' AND email NOT LIKE '%ww.com%' AND \n" + "email NOT LIKE 'test%'   AND  email NOT LIKE '%qq.ss' AND email NOT LIKE '%@q.ocm' AND \n" + "email NOT LIKE '%qqsss.com' AND  email NOT LIKE '%csmfg.com%'  AND  email NOT LIKE '%@sourcing-cn.com%'  AND \n" + "email NOT LIKE '%@china-synergy%'  AND email<>'sb33@gmail.com'  AND email<>'sbtest@gmail.com'  AND \n" + "email NOT LIKE '%@qq.co%' AND email NOT LIKE '%11.com' AND email NOT LIKE '%@qq.ocm' AND email NOT LIKE '%@163.com'   AND \n" + "email NOT LIKE 'zhouxueyun%' AND u.id IN (SELECT user_id FROM orderinfo WHERE state BETWEEN 1 AND 5 AND create_time>DATE_SUB(CURDATE(), INTERVAL 6 MONTH))\n" + " ) AND oi.state BETWEEN 1 AND 5";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("id"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	/**
	 * 用户最近下过的3个订单的类别数据
	 * @return
	 */
	public List<String> getCatidByUser(String userId){
		List<String> list=new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql="  SELECT DISTINCT goodscatid FROM order_details od INNER JOIN (\n" + " SELECT order_no FROM orderinfo WHERE user_id ="+userId+" AND state BETWEEN 1 AND 5 ORDER BY orderid DESC LIMIT 3) oi \n" + " ON od.orderid=oi.order_no WHERE od.state IN (0,1) AND od.goodscatid!='' order by od.id desc";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("goodscatid"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	/**
	 * 根据类别查询卖过的产品数据
	 * @param catid
	 * @return
	 */
	public List<String> getPidByCatid(String catid){
		List<String> list=new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql=" SELECT distinct od.goods_pid FROM order_details od \n" + "INNER JOIN orderinfo oi ON od.orderid=oi.order_no\n" + "INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid\n" + "WHERE c.valid=1 AND oi.user_id IN (\n" + "SELECT id FROM USER WHERE email NOT LIKE '%qq.com%' AND email NOT LIKE '%ww.com%' AND \n" + "email NOT LIKE 'test%'   AND  email NOT LIKE '%qq.ss' AND email NOT LIKE '%@q.ocm' AND \n" + "email NOT LIKE '%qqsss.com' AND  email NOT LIKE '%csmfg.com%'  AND  email NOT LIKE '%@sourcing-cn.com%'  AND \n" + "email NOT LIKE '%@china-synergy%'  AND email<>'sb33@gmail.com'  AND email<>'sbtest@gmail.com'  AND \n" + "email NOT LIKE '%@qq.co%' AND email NOT LIKE '%11.com' AND email NOT LIKE '%@qq.ocm' AND email NOT LIKE '%@163.com'   AND \n" + "email NOT LIKE 'zhouxueyun%'\n" + ") AND oi.state BETWEEN 1 AND 5 AND oi.create_time>DATE_SUB(CURDATE(), INTERVAL 1 YEAR) AND od.goodscatid='"+catid+"' ORDER BY od.goodsprice DESC ";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("goods_pid"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	public List<OrderDetailsBean> getAllCatIdDta(){
		List<OrderDetailsBean> list=new ArrayList<OrderDetailsBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql="SELECT od.goodscatid,COUNT(od.goods_pid) as pids FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no " +
					"INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid WHERE c.valid=1 AND oi.user_id IN (SELECT id FROM USER WHERE email NOT LIKE '%qq.com%' AND email NOT LIKE '%ww.com%' AND " +
					"email NOT LIKE 'test%'   AND  email NOT LIKE '%qq.ss' AND email NOT LIKE '%@q.ocm' AND " +
					"email NOT LIKE '%qqsss.com' AND  email NOT LIKE '%csmfg.com%'  AND  email NOT LIKE '%@sourcing-cn.com%'  AND " +
					"email NOT LIKE '%@china-synergy%'  AND email<>'sb33@gmail.com'  AND email<>'sbtest@gmail.com'  AND " +
					"email NOT LIKE '%@qq.co%' AND email NOT LIKE '%11.com' AND email NOT LIKE '%@qq.ocm' AND email NOT LIKE '%@163.com'   AND " +
					"email NOT LIKE 'zhouxueyun%') AND oi.state BETWEEN 1 AND 5 AND oi.create_time>DATE_SUB(CURDATE(), INTERVAL 1 YEAR) AND od.goodscatid IS NOT NULL AND od.goodscatid!='' " +
					" GROUP BY goodscatid";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				OrderDetailsBean o=new OrderDetailsBean();
				o.setGoods_pid(rs.getString("pid"));
				o.setGoodscatid(rs.getString("catid1"));
				list.add(o);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	public List<OrderDetailsBean> getAllGoodsInfos(){
		List<OrderDetailsBean> list=new ArrayList<OrderDetailsBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql="SELECT pid,catid1 FROM custom_benchmark_ready WHERE valid=1";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				OrderDetailsBean o=new OrderDetailsBean();
				o.setGoods_pid(rs.getString("pid"));
				o.setGoodscatid(rs.getString("catid1"));
				list.add(o);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	public String getAllGoodsInfo(int page){
		String msg="";
		StringBuilder goods=new StringBuilder();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			goods.append("{\"type\":\"2\",\"items\":[");
			String sql="SELECT pid,catid1,wprice,range_price,price,custom_main_image,feeprice,enname,remotpath,wholesale_price,is_sold_flag FROM custom_benchmark_ready WHERE valid=1 limit "+page+",100";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				goods.append("{");
				goods.append("\"pid\":\""+rs.getString("pid")+"\",");
				goods.append("\"catid1\":\""+rs.getString("catid1")+"\",");
				goods.append("\"custom_main_image\":\""+rs.getString("custom_main_image")+"\",");
				goods.append("\"wprice\":\""+rs.getString("wprice")+"\",");
				goods.append("\"range_price\":\""+rs.getString("range_price")+"\",");
				goods.append("\"feeprice\":\""+rs.getString("feeprice")+"\",");
				goods.append("\"enname\":\""+rs.getString("enname")+"\",");
				goods.append("\"custom_price\":\""+rs.getString("price")+"\",");
				goods.append("\"remotpath\":\""+rs.getString("remotpath")+"\",");
				goods.append("\"wholesale_price\":\""+rs.getString("wholesale_price")+"\",");
				goods.append("\"is_sold_flag\":\""+rs.getString("is_sold_flag")+"\"");
				goods.append("},");
			}
			msg=goods.toString().substring(0,goods.toString().length()-1)+"]}";
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return msg;
	}

	public List<String> getBuyPidList(String pid){
		List<String> list = new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql="SELECT c.pid  FROM custom_benchmark_ready c INNER JOIN (SELECT od1.goods_pid " +
					" FROM order_details od1 INNER JOIN order_details od ON od1.userid=od.userid " +
					" WHERE od.goods_pid='"+pid+"' AND od1.goods_pid != '"+pid+"' " +
					" GROUP BY od1.goods_pid ORDER BY COUNT(od1.id) DESC ) a " +
					" ON c.pid=a.goods_pid WHERE c.valid=1 and c.price>=10 LIMIT 12";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("pid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	public List<String> boughtAndCatId(String pid,String catid){
		List<String> list = new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql=" SELECT c.pid" +
					"  FROM custom_benchmark_ready c INNER JOIN (SELECT od1.goods_pid  FROM order_details od1 " +
					" INNER JOIN order_details od ON od1.userid=od.userid WHERE od.goodscatid='"+catid+"' AND od1.goods_pid != '"+pid+"' " +
					" GROUP BY od1.goods_pid ORDER BY COUNT(od1.id) DESC ) a ON c.pid=a.goods_pid WHERE c.valid=1 and c.price>=10 LIMIT 12";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("pid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	public List<String> saleMoreGoods(String pid){
		List<String> list = new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql="SELECT od.goods_pid FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no " +
					"INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid WHERE od.state<2 AND oi.state BETWEEN 1 AND 5 " +
					"AND oi.user_id IN (SELECT id FROM USER WHERE email NOT LIKE '%qq.com%' AND email NOT LIKE '%ww.com%' AND " +
					"email NOT LIKE 'test%'   AND  email NOT LIKE '%qq.ss' AND email NOT LIKE '%@q.ocm' AND " +
					"email NOT LIKE '%qqsss.com' AND  email NOT LIKE '%csmfg.com%'  AND  email NOT LIKE '%@sourcing-cn.com%'  AND " +
					"email NOT LIKE '%@china-synergy%'  AND email<>'sb33@gmail.com'  AND email<>'sbtest@gmail.com'  AND " +
					"email NOT LIKE '%@qq.co%' AND email NOT LIKE '%11.com' AND email NOT LIKE '%@qq.ocm' AND email NOT LIKE '%@163.com'   AND " +
					"email NOT LIKE 'zhouxueyun%') AND c.valid=1 and c.price>=10 AND od.goods_pid IS NOT NULL AND od.goods_pid != '' AND od.goods_pid!='"+pid+"' " +
					"AND  oi.orderpaytime BETWEEN DATE_SUB(NOW(),INTERVAL 6 MONTH) AND NOW() GROUP BY od.goods_pid ORDER BY COUNT(od.id) DESC LIMIT 12";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("goods_pid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	public List<OrderDetailsBean> getAllPidss() {
		List<OrderDetailsBean> list=new ArrayList<OrderDetailsBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql="SELECT distinct od.goods_pid,od.goodscatid FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no  INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid" +
					" WHERE c.valid=1 and c.price>=10 AND od.state<2 AND oi.state BETWEEN 1 AND 5 AND oi.user_id IN (SELECT id FROM USER WHERE email NOT LIKE '%qq.com%' AND email NOT LIKE '%ww.com%' AND " +
					" email NOT LIKE 'test%'   AND  email NOT LIKE '%qq.ss' AND email NOT LIKE '%@q.ocm' AND email NOT LIKE '%qqsss.com' AND  email " +
					" NOT LIKE '%csmfg.com%'  AND  email NOT LIKE '%@sourcing-cn.com%'  AND " +
					" email NOT LIKE '%@china-synergy%'  AND email<>'sb33@gmail.com'  AND email<>'sbtest@gmail.com'  AND " +
					" email NOT LIKE '%@qq.co%' AND email NOT LIKE '%11.com' AND email NOT LIKE '%@qq.ocm' AND email NOT LIKE '%@163.com'   AND " +
					" email NOT LIKE 'zhouxueyun%') AND od.goods_pid IS NOT NULL AND od.goods_pid != ''";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				OrderDetailsBean o=new OrderDetailsBean();
				o.setGoods_pid(rs.getString("goods_pid"));
				o.setGoodscatid(rs.getString("goodscatid"));
				list.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	public List<OrderDetailsBean> getAllPids(int page) {
		List<OrderDetailsBean> list = new ArrayList<OrderDetailsBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql="SELECT DISTINCT od.goods_pid,od.goodscatid FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no  INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid" +
					" WHERE c.valid=1 AND od.state<2 AND oi.state BETWEEN 1 AND 5 AND oi.user_id IN (SELECT id FROM USER WHERE email NOT LIKE '%qq.com%' AND email NOT LIKE '%ww.com%' AND " +
					" email NOT LIKE 'test%'   AND  email NOT LIKE '%qq.ss' AND email NOT LIKE '%@q.ocm' AND email NOT LIKE '%qqsss.com' AND  email " +
					" NOT LIKE '%csmfg.com%'  AND  email NOT LIKE '%@sourcing-cn.com%'  AND " +
					" email NOT LIKE '%@china-synergy%'  AND email<>'sb33@gmail.com'  AND email<>'sbtest@gmail.com'  AND " +
					" email NOT LIKE '%@qq.co%' AND email NOT LIKE '%11.com' AND email NOT LIKE '%@qq.ocm' AND email NOT LIKE '%@163.com'   AND " +
					" email NOT LIKE 'zhouxueyun%') AND od.goods_pid IS NOT NULL AND od.goods_pid != '' limit "+page+",50";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				OrderDetailsBean o=new OrderDetailsBean();
				o.setGoods_pid(rs.getString("goods_pid"));
				o.setGoodscatid(rs.getString("goodscatid"));
				list.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
}