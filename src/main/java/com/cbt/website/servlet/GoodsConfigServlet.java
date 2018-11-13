package com.cbt.website.servlet;

import com.cbt.parse.bean.GoodsDaoBean;
import com.cbt.parse.dao.*;
import com.cbt.parse.daoimp.IGoodsDao;
import com.cbt.parse.daoimp.IGoodsExpandDao;
import com.cbt.parse.daoimp.IHotWordDao;
import com.cbt.parse.driver.FtpDriver;
import com.cbt.parse.driver.ImgDownloadDriver;

import org.slf4j.LoggerFactory;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Servlet implementation class AddWholesaleServlet
 */
public class GoodsConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsConfigServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsConfigServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		String set = req.getParameter("set");
		String url = req.getParameter("url");
		try {
			if("1".equals(set)){
				//处理商品数据有效性
				String valid = req.getParameter("valid");
				String com = req.getParameter("com");
				if(com==null||com.isEmpty()){
					com = "g";
				}
				int update = 0;
				if(url!=null&&!url.isEmpty()&&valid!=null&&com!=null){
					valid = valid.replaceAll("\\D+", "");
					valid = valid.isEmpty()?"1":valid;
					if("h".equals(com)){
						IHotWordDao dao = new HotWordDao();
						update = dao.updateValid(url, Integer.valueOf(valid));
					}else if("g".equals(com)){
						IGoodsDao dao  = new GoodsDao();
						update = dao.updatevalid(valid, url);
						IGoodsExpandDao dao2  = new GoodsExpandDao();
						dao2.updateValid(url,Integer.valueOf(valid));
//					}else if("e".equals(com)){
//						IEllyGoodsDao dao = new EllyGoodsDao();
//						update = dao.updateValid(url,  Integer.valueOf(valid));
					}else if("s".equals(com)){
						IHotWordDao dao1 = new HotWordDao();
						update = dao1.updateValid(url, Integer.valueOf(valid));
						IGoodsDao dao2  = new GoodsDao();
						update = dao2.updatevalid(valid, url);
//						IEllyGoodsDao dao4 = new EllyGoodsDao();
//						update = dao4.updateValid(url,  Integer.valueOf(valid));
					}
				}
				req.setAttribute("valid", update);
			}else if("2".equals(set)){
				//添加屏蔽的商品
				IGoodsDao dao  = new GoodsDao();
				GoodsDaoBean data = dao.queryData("goodsdata_write",url);
				int add=0;
				if(data!=null){
					add = dao.updatevalid("0", url);
				}else{
//					add = dao.addUnValidWriteAndRead(url, "0");
				}
				req.setAttribute("add", add);
			}else if("3".equals(set)){
				//添加屏蔽词   品牌词
				String cid = req.getParameter("cid");
				String catid = req.getParameter("catid");
				String Regx = req.getParameter("Regx");
				int inten=0;
				if(Regx!=null&&!Regx.isEmpty()){
					cid = cid==null?"1":cid;
					catid = catid==null?"0":catid;
					IntensveDao dao  = new IntensveDao();
					ArrayList<String> data = dao.querryExsis(Regx, cid, catid);
					if(data==null||data.isEmpty()){
						inten = dao.add(Regx, cid, catid);
					}else{
						inten = -1;
					}
				}
				req.setAttribute("inten", inten);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			javax.servlet.RequestDispatcher homeDispatcher = req.getRequestDispatcher("website/goodsconfig.jsp");
			homeDispatcher.forward(req, resp);
		}
	}
	protected void imgDownload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		String sid1 = req.getParameter("id1");
		String sid2 = req.getParameter("id2");
		String index = req.getParameter("index");
		String re = null;
		try {
			int id1=0;
			int id2=0;
			int indexs=0;
			if(sid1!=null&&!sid1.isEmpty()){
				sid1 = sid1.replaceAll("\\D+", "");
				if(!sid1.isEmpty()){
					id1 = Integer.valueOf(sid1);
				}
			}
			if(sid2!=null){
				sid2 = sid2.replaceAll("\\D+", "");
				if(!sid2.isEmpty()){
					id2 = Integer.valueOf(sid2);
				}
			}
			if(index!=null){
				index = index.replaceAll("\\D+", "");
				if(!index.isEmpty()){
					indexs = Integer.valueOf(index);
				}
			}
//			FTPClient ftp = FtpDriver.connect("", "ftp.china-clothing-wholesale.com", 21, "import1@china-clothing-wholesale.com", "import416");
			re = ImgDownloadDriver.driver(id1, id2,indexs);
//			FtpDriver.returnConnect(ftp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter out=resp.getWriter();
		out.print(re);
		out.flush();
		out.close();
		
	}
	protected void imgUpload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		String sid1 = req.getParameter("id1");
		String sid2 = req.getParameter("id2");
		int  re = 0;
		try {
			int id1=0;
			int id2=0;
			if(sid1!=null&&!sid1.isEmpty()){
				sid1 = sid1.replaceAll("\\D+", "");
				if(!sid1.isEmpty()){
					id1 = Integer.valueOf(sid1);
				}
			}
			if(sid2!=null){
				sid2 = sid2.replaceAll("\\D+", "");
				if(!sid2.isEmpty()){
					id2 = Integer.valueOf(sid2);
				}
			}
			ImgFileDao  dao = new ImgFileDao();
			ArrayList<HashMap<String,String>> list = dao.queryTest(id1, id2);
			FTPClient ftp = FtpDriver.connect("", "ftp.china-clothing-wholesale.com", 21, "import@china-clothing-wholesale.com", "import416");
			int num = list.size();
			File file = null;
			for(int i=0;i<num;i++){
				LOG.warn(i+"---------"+list.get(i).get("file"));
				file = new File(list.get(i).get("file"));
				int upload = FtpDriver.upload(file, ftp);
				if(upload>0){
					LOG.warn("^^^^^^^^^^file upload success^^^^^^^^^^^^^^^");
					dao.addFlag(list.get(i).get("imgurl"), list.get(i).get("file"), list.get(i).get("url"));
				}
				file = null;
				re++;
			}
			FtpDriver.returnConnection(ftp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter out=resp.getWriter();
		out.print(re);
		out.flush();
		out.close();
		
	}
}
