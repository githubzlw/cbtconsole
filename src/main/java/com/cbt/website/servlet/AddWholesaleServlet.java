package com.cbt.website.servlet;

import com.cbt.parse.bean.HotWordBean;
import com.cbt.parse.dao.HotWordDao;
import com.cbt.parse.daoimp.IHotWordDao;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class AddWholesaleServlet
 */
public class AddWholesaleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddWholesaleServlet() {
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
		String hotwords = req.getParameter("kwywords");
		String minprice = req.getParameter("minprice");
		String maxprice = req.getParameter("maxprice");
		String morder = req.getParameter("morder");
		String gunit = req.getParameter("gunit");
		String perweight = req.getParameter("perweight");
		String weight = req.getParameter("weight");
		String volume = req.getParameter("volume");
		String punit = req.getParameter("punit");
		String url = req.getParameter("url");
		HotWordBean bean = new HotWordBean();
		bean.setValid(0);
		if(hotwords!=null&&!hotwords.isEmpty()&&url!=null&&!url.isEmpty()){
			gunit = gunit==null?"piece":gunit;
			gunit = gunit.isEmpty()?"piece":gunit;
			punit = punit==null?"$":punit;
			punit = punit.isEmpty()?"$":punit;
			bean.setGunit(gunit);
			bean.setPunit(punit);
			bean.setHotwords(hotwords);
			minprice = minprice!=null?DownloadMain.getSpiderContext(minprice, "\\d+\\.*\\d*"):"0";
			minprice = minprice.isEmpty()?"0":minprice;
			bean.setMinprice(Double.valueOf(minprice));
			
			maxprice = maxprice!=null?DownloadMain.getSpiderContext(maxprice, "\\d+\\.*\\d*"):"0";
			maxprice = maxprice.isEmpty()?"0":maxprice;
			bean.setMaxprice(Double.valueOf(maxprice));
			
			morder = morder!=null?DownloadMain.getSpiderContext(morder, "\\d+"):"1";
			morder = morder.isEmpty()?"1":morder;
			bean.setMorder(Integer.valueOf(morder));
			bean.setWeight(weight);
			perweight = perweight!=null?perweight:weight;
			perweight = perweight.isEmpty()?weight:perweight;
			bean.setPweight(perweight);
			bean.setWidth(volume);
			bean.setUrl(url);
			IHotWordDao dao = new HotWordDao();
			GoodsBean goodsBean = ParseGoodsUrl.parseGoodsw(url, 0);
			if(goodsBean!=null&&!goodsBean.isEmpty()){
				bean.setWprice(goodsBean.getpWprice().toString());
				bean.setImg(goodsBean.getpImage().get(0));
				bean.setPid(DownloadMain.getSpiderContext(url, "/\\d+\\.html").replaceAll("\\D+", ""));
				bean.setInfo(goodsBean.getInfo_ori());
				bean.setName(goodsBean.getpName());
				bean.setValid(1);
			}
			HotWordBean goods = dao.queryGoods(url, null);
			if(goods==null){
				int add = dao.add(bean);
				if(add==1){
					bean.setValid(1);
				}else{
					bean.setValid(0);
				}
			}else{
				dao.update(bean);
				bean.setValid(2);
			}
		}else{
			bean.setValid(0);
		}
		req.setAttribute("spider", bean);
		javax.servlet.RequestDispatcher homeDispatcher = req.getRequestDispatcher("website/addwholesale.jsp");
		homeDispatcher.forward(req, resp);
		
	}
}
