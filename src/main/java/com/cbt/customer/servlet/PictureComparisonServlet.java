package com.cbt.customer.servlet;

import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.SimilarImageSearch;
import com.cbt.pojo.Admuser;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SplitPage;
import com.cbt.util.StrUtils;

import net.sf.json.JSONArray;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

public class PictureComparisonServlet extends HttpServlet {
	private String chineseChar = "([\\一-\\龥]+)";
	private final static int PAGESIZE = 40;
	private static final long serialVersionUID = 1L;
    @Autowired
    private CustomGoodsService customGoodsService;

	/**
	 * 方法描述:ali图片下载
	 * 
	 */
	public void pictureDown(HttpServletRequest request, HttpServletResponse response) {

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		int maxCount = ips.getMaxCount();

		List<GoodsFarBean> goodsFarBeans = ips.findByAliPicture(maxCount);
		if(goodsFarBeans !=null){
			for(int i=0; i<goodsFarBeans.size(); i++){

//				String[] imgs = goodsFarBeans.get(i).getImg().split(",");
//				for(int j=0; j<imgs.length; j++){
//					int imgInt = j+1;
//					String fileName = "F:/img/"+goodsFarBeans.get(i).getpId()+"/"+"ali"+"/"+"ali"+imgInt+".jpg";
//					Boolean flag = ImgDownload.execute(imgs[j], fileName);
//					if(!flag){
//						//如果第一张图片没有，更新goodsdata_far[Valid]=9,无效。
//						if(j==0){
//							ips.updateImgUrlValid(goodsFarBeans.get(i).getpId());
//						}
//					}
//				}

				String imgs = goodsFarBeans.get(i).getImg();
				String fileName = "F:/img/"+goodsFarBeans.get(i).getpId()+"/"+"ali"+"/"+"ali1.jpg";
				Boolean flag = ImgDownload.execute(imgs, fileName);
//				if(!flag){
//					//如果第一张图片没有，更新goodsdata_far[Valid]=9,无效。
//					if(j==0){
//						ips.updateImgUrlValid(goodsFarBeans.get(i).getpId());
//					}
//				}

			}

			request.setAttribute("aliCount", goodsFarBeans.size());
		}

		try {
			request.getRequestDispatcher("/website/picture_comparison.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述:新订单图片抓取
	 *
	 */
//	public void newOrderPictureDown(HttpServletRequest request, HttpServletResponse response) {
//
////		IPictureComparisonService ips = new PictureComparisonServiceImpl();
////		int maxCount = ips.getMaxCount();
////
////		List<GoodsFarBean> goodsFarBeans = ips.findByAliPicture(maxCount);
////		if(goodsFarBeans !=null){
////			for(int i=0; i<goodsFarBeans.size(); i++){
////
////				String[] imgs = goodsFarBeans.get(i).getImg().split(",");
////				for(int j=0; j<imgs.length; j++){
////					int imgInt = j+1;
////					String fileName = "F:/img/"+goodsFarBeans.get(i).getpId()+"/"+"ali"+"/"+"ali"+imgInt+".jpg";
////					Boolean flag = ImgDownload.execute(imgs[j], fileName);
////					if(!flag){
////						//如果第一张图片没有，更新goodsdata_far[Valid]=9,无效。
////						if(j==0){
////							ips.updateImgUrlValid(goodsFarBeans.get(i).getpId());
////						}
////					}
////				}
////			}
////
////			request.setAttribute("aliCount", goodsFarBeans.size());
////		}
//		try{
//
////	    	String path = "E:\\myproject\\localPhotos\\";
//	    	String path = "E:";
//			IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex1")));
//			IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex2")));
//			IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex3")));
//			IndexReader reader4 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex4")));
//			List<IndexReader>  lisrd= new  ArrayList<IndexReader>();
//			lisrd.add(reader1);
//			lisrd.add(reader2);
//			lisrd.add(reader3);
//			lisrd.add(reader4);
//
//	    	IPictureComparisonService ips = new PictureComparisonServiceImpl();
//
//			List<GoodsFarBean> beanList = ips.getImgFile();
//
//			if(beanList!=null){
//				for(int i=0;i<beanList.size();i++){
//					syncTableInfo csn = new syncTableInfo();
//					//搜索图片路径
//					String strImgPath = beanList.get(i).getImgPath();
//					strImgPath = strImgPath.replace("/", "\\");
//					csn.setUploadImgPath(path+strImgPath);
//					//原图片路径
//					csn.setSourceImg(beanList.get(i).getImgPath());
//					//原url
//					csn.setSourceUrl(beanList.get(i).getUrl());
//					//类别id
//					csn.setCatId(beanList.get(i).getCatId());
//					//类别名
//					csn.setCatName(beanList.get(i).getCatName());
//
//					//循环4个index
//					for(int j=0;j<lisrd.size();j++){
//						SyncTableData data = new SyncTableData(csn,lisrd.get(j));
//		    			Thread t1 = new Thread(data);
//		    			t1.start();
//		     		}
//				}
//				//更新抓取标志
//		        ips.updateImgFileUpload(beanList);
//			}
//
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//
//		try {
//			request.getRequestDispatcher("/website/picture_comparison.jsp").forward(request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 方法描述:淘宝图片下载
	 *
	 */
	public void tbPictureDown(HttpServletRequest request, HttpServletResponse response) {

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		int maxCount = ips.getTbMaxCount();

		List<GoodsFarBean> goodsFarBeans = ips.findByTbPicture(maxCount);
//		if(goodsFarBeans !=null){
//			for(int i=0; i<goodsFarBeans.size(); i++){
//				
//				String imgLenght = goodsFarBeans.get(i).getTbimg()+","+goodsFarBeans.get(i).getTbimg1()+","+goodsFarBeans.get(i).getTbimg2()+","+goodsFarBeans.get(i).getTbimg3();
//				String[] imgs = imgLenght.split(",");
//				for(int j=0; j<imgs.length; j++){
//					int imgInt = j+1;
//					String fileName = "F:/imgTb/"+goodsFarBeans.get(i).getTbPId()+"/"+"taobao"+"/"+"taobao"+imgInt+".jpg";
//					Boolean flag = ImgDownload.execute(imgs[j], fileName);
//				}
//			}
//			
//			request.setAttribute("aliCount", goodsFarBeans.size());
//		}

		if(goodsFarBeans !=null){
			for(int i=0; i<goodsFarBeans.size(); i++){
				String img = "http:"+goodsFarBeans.get(i).getTbimg();
				String fileName = "F:/imgTb/"+goodsFarBeans.get(i).getTbPId()+"/"+"taobao"+"/"+"taobao1.jpg";
				Boolean flag = ImgDownload.execute(img, fileName);
			}

			request.setAttribute("aliCount", goodsFarBeans.size());
		}

		try {
			request.getRequestDispatcher("/website/taobao_down.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述:规格保存
	 *
	 */
	public void tbStyleDown(HttpServletRequest request, HttpServletResponse response) {

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		int maxCount = ips.getTbStyMaxCount();

		List<GoodsFarBean> goodsFarBeans = ips.findByTbStyUrl(maxCount);
		if(goodsFarBeans !=null){
			for(int i=0; i<goodsFarBeans.size(); i++){

				String url1 = goodsFarBeans.get(i).getTburl();
				GoodsBean bean1= ParseGoodsUrl.parseGoodsw(url1, 0);
				String style1 = JSONArray.fromObject(bean1.getType()).toString();
				ips.updateStyle(goodsFarBeans.get(i).getTbPId(),"1",style1);
				System.out.print(style1);
				String url2 = goodsFarBeans.get(i).getTburl1();
				GoodsBean bean2= ParseGoodsUrl.parseGoodsw(url2, 0);
				String style2 = JSONArray.fromObject(bean2.getType()).toString();
				ips.updateStyle(goodsFarBeans.get(i).getTbPId(),"2",style2);
				System.out.print(style2);

				String url3 = goodsFarBeans.get(i).getTburl2();
				GoodsBean bean3= ParseGoodsUrl.parseGoodsw(url3, 0);
				String style3 = JSONArray.fromObject(bean3.getType()).toString();
				ips.updateStyle(goodsFarBeans.get(i).getTbPId(),"3",style3);
				System.out.print(style3);
				String url4 = goodsFarBeans.get(i).getTburl3();
				GoodsBean bean4= ParseGoodsUrl.parseGoodsw(url4, 0);
				String style4 = JSONArray.fromObject(bean4.getType()).toString();
				ips.updateStyle(goodsFarBeans.get(i).getTbPId(),"4",style4);
				System.out.print(style4);

			}

			request.setAttribute("aliCount", goodsFarBeans.size());
		}

		try {
			request.getRequestDispatcher("/website/picture_comparison.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述:图片比较相似度
	 *
	 */
	public void pictureCompare(HttpServletRequest request, HttpServletResponse response) {


		SimilarImageSearch.pictureCompare();

		try {
			request.getRequestDispatcher("/website/picture_comparison.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述:查询图片比较结果一览
	 * author:zlw
	 * date:2015年11月25日
	 */
	public void findAllPicture(HttpServletRequest request, HttpServletResponse response) {

		//相似度
		String similarityId = request.getParameter("similarityId");
		//销量
		String su = request.getParameter("selled");
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("categoryId1");

		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);

		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);
		//根据分类查结果
		List<GoodsCheckBean> goodsCheckBeans = ips.findGoodsDataCheck(selled,similarityId,categoryId1,start, PAGESIZE);

		for(int i=0;i<goodsCheckBeans.size();i++){
			//淘宝价格1
			String tbPrice=goodsCheckBeans.get(i).getTbprice();
			//淘宝价格2
			String tbPrice1=goodsCheckBeans.get(i).getTbprice1();
			//淘宝价格3
			String tbPrice2=goodsCheckBeans.get(i).getTbprice2();
			//淘宝价格4
			String tbPrice3=goodsCheckBeans.get(i).getTbprice3();
			//淘宝4个产品最低价格
			String minPrice=goodsCheckBeans.get(i).getMinPrice();

			//淘宝1个产品相似度值
			int imgcheck0=goodsCheckBeans.get(i).getImgCheck0();
			//淘宝2个产品相似度值
			int imgcheck1=goodsCheckBeans.get(i).getImgCheck1();
			//淘宝3个产品相似度值
			int imgcheck2=goodsCheckBeans.get(i).getImgCheck2();
			//淘宝4个产品相似度值
			int imgcheck3=goodsCheckBeans.get(i).getImgCheck3();

			//if(goodsCheckBeans.get(i).getMinImgCheck()<=10){
				//红框选择：
				//如果 第2个的相似度值 小于等于  第1个 （也就是比第一个更像),而且价格比第1个低，就选 第2个
				if(imgcheck1 <= imgcheck0 && Double.valueOf(tbPrice1) < Double.valueOf(tbPrice)){
					goodsCheckBeans.get(i).setTbFlag1("1");
				}
				//如果 第3个 比 第1个 更相似，而且 价格比第1个低 也比 第2个低， 就选 第3个。
				else if(imgcheck2 < imgcheck0 && Double.valueOf(tbPrice2) < Double.valueOf(tbPrice) && Double.valueOf(tbPrice2) < Double.valueOf(tbPrice1)){
					goodsCheckBeans.get(i).setTbFlag2("1");

				}else{
					//默认选第1个 的价格
					goodsCheckBeans.get(i).setTbFlag("1");
				}

//				//淘宝价格重复最低价格选中
//				if(tbPrice.equals(minPrice)){
//					goodsCheckBeans.get(i).setTbFlag("1");
//				}else if(tbPrice1.equals(minPrice)){
//					goodsCheckBeans.get(i).setTbFlag1("1");
//				}else if(tbPrice2.equals(minPrice)){
//					goodsCheckBeans.get(i).setTbFlag2("1");
//				}else if(tbPrice3.equals(minPrice)){
//					goodsCheckBeans.get(i).setTbFlag3("1");
//				}
			//}

		}
		//总条数
//		int goodsCheckCount = ips.getGoodsCheckCount(selled,similarityId,categoryId1);
		//List<GoodsCheckBean> beans = ips.findGoodsDataCheckCount(selled,similarityId,categoryId1);
//		List<GoodsCheckBean> beans = ips.findGoodsDataCheckCount1(selled,similarityId,categoryId1);
//		SplitPage.buildPager(request, beans.size(), PAGESIZE, page);
		request.setAttribute("gbbs", goodsCheckBeans);
		request.setAttribute("cid", cid);
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("categoryId1", categoryId1);
		request.setAttribute("similarityId", similarityId);
		request.setAttribute("page", page);
//		//总条数
//		request.setAttribute("counto1", goodsCheckCount);
//		//总页数
//		request.setAttribute("count1", goodsCheckCount/PAGESIZE);
//		//当前页
//		request.setAttribute("pageCount", page);
		//每页
//		request.setAttribute("pagesize", PAGESIZE);


//		//相似度高的产品
//		int count1 = ips.getTbCount1(categoryId1);
//		request.setAttribute("count1", count1);
//		//相似度很低的产品
//		int count2 = ips.getTbCount2(categoryId1);
//		request.setAttribute("count2", count2);
//		//相似度高，但 第一个产品的价格 > AliExpress价格*0.8 的产品
//		int count3 = ips.getTbCount3(categoryId1);
//		request.setAttribute("count3", count3);
//		//相似度高，但 第一个产品的价格 > AliExpress价格 的产品
//		int count4 = ips.getTbCount4(categoryId1);
//		request.setAttribute("count4", count4);
//		//相似度高，但 第一个产品的价格 < AliExpress价格*0.3 的产品
//		int count5 = ips.getTbCount5(categoryId1);
//		request.setAttribute("count5", count5);
//		//相似度高，但 第一个产品的价格 < AliExpress价格*0.2 的产品
//		int count6 = ips.getTbCount6(categoryId1);
//		request.setAttribute("count6", count6);
//		//相似度高，但 第一个产品的价格 < AliExpress价格*0.1 的产品
//		int count7 = ips.getTbCount7(categoryId1);
//		request.setAttribute("count7", count7);
//		//第1和第2 个产品都和AliExpress相似度高, 而且  Min (第一产品价格 和 第一产品价格）> AliExpress*0.8 的产品
//		int count8 = ips.getTbCount8(categoryId1);
//		request.setAttribute("count8", count8);
//		//第1,第2 ,第3个产品 都和AliExpress相似度高, 而且  Min (第1,第2,第3 产品价格）> AliExpress*0.8 的产品
//		int count9 = ips.getTbCount9(categoryId1);
//		request.setAttribute("count9", count9);
//		//有ali区间价格
//		int count10 = ips.getTbCount10(categoryId1);
//		request.setAttribute("count10", count10);
//		//前3个淘宝产品最低价格 < AliExpress价格
//		List<GoodsCheckBean> count11 = ips.findGoodsDataCheckCount1(selled,similarityId,categoryId1);
//		request.setAttribute("count11", count11.size());

		try {
			request.getRequestDispatcher("/website/picturecompare.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 方法描述:查询图片比较结果一览
	 * author:zlw
	 * date:2016年03月30日
	 */
	public void findPictureCompare(HttpServletRequest request, HttpServletResponse response) {

		//相似度
		String similarityId = request.getParameter("similarityId");
		//销量
		String su = request.getParameter("selled");
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("categoryId1");

		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);

		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);
		//根据分类查结果
		List<GoodsCheckBean> goodsCheckBeans = ips.findLireData(selled,similarityId,categoryId1,start, PAGESIZE);

//		for(int i=0;i<goodsCheckBeans.size();i++){
//			//lireData
//			List<GoodsCheckBean> pictureList = goodsCheckBeans.get(i).getPictureList();
//			for(int j=0;j<pictureList.size();j++){
//				if(goodsCheckBeans.get(i).getAligSourceUrl().equals(pictureList.get(j).getAligSourceUrl())){
//					//lire价格1
//					String Price1=pictureList.get(0).getPrice();
//					//lire价格2
//					String Price2=pictureList.get(1).getPrice();
//					//lire价格3
//					String Price3=pictureList.get(2).getPrice();
//					
//					//lire1个产品相似度值
//					Double imgcheck0=pictureList.get(0).getdScore();
//					//lire2个产品相似度值
//					Double imgcheck1=pictureList.get(1).getdScore();
//					//lire3个产品相似度值
//					Double imgcheck2=pictureList.get(2).getdScore();
//					
//					//红框选择： 
//					//如果 第2个的相似度值 小于等于  第1个 （也就是比第一个更像),而且价格比第1个低，就选 第2个
//					if(imgcheck1.compareTo(imgcheck0) <= 0 && Double.valueOf(Price2) < Double.valueOf(Price1)){
//						pictureList.get(1).setTbFlag("1");
//					}
//					//如果 第3个 比 第1个 更相似，而且 价格比第1个低 也比 第2个低， 就选 第3个。
//					else if(imgcheck2.compareTo(imgcheck0) < 0  && Double.valueOf(Price3) < Double.valueOf(Price1) && Double.valueOf(Price3) < Double.valueOf(Price2)){
//						pictureList.get(2).setTbFlag("1");
//						
//					}else{
//						//默认选第1个 的价格
//						pictureList.get(0).setTbFlag("1");
//					}
//					break;
//				}
//				
//			}
//				
//		}


		int resoultCount = ips.findResoultCount();
		int count = ips.findCount(selled,similarityId,categoryId1);
		SplitPage.buildPager(request, count, PAGESIZE, page);
//		SplitPage.buildPager(request, goodsCheckBeans.size(), PAGESIZE, page);
		request.setAttribute("gbbs", goodsCheckBeans);
		request.setAttribute("resoultCount", resoultCount);
//		request.setAttribute("cid", cid);
//		request.setAttribute("categoryId", categoryId);
//		request.setAttribute("categoryId1", categoryId1);
//		request.setAttribute("similarityId", similarityId);
		request.setAttribute("page", page);

		try {
			request.getRequestDispatcher("/website/lirepicturecompare.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 方法描述:查询已筛选好数据
	 * author:zlw
	 */
	public void findAllTaoBaoInfo(HttpServletRequest request, HttpServletResponse response) {

		//相似度
//		String similarityId = request.getParameter("similarityId");
		//alipid
		String aliPid = request.getParameter("aliPid");
		//1688pid
		String ylbbPid = request.getParameter("ylbbPid");
//		String ylbbPid = "12323";
		//销量
		String su = request.getParameter("selled");
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("categoryId1");

		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);

		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);
		String getSourceTbl = ips.getSourceTbl(aliPid);
		if(!"".equals(getSourceTbl)){
			//根据分类查结果
			List<GoodsCheckBean> goodsCheckBeans = ips.findTbGoodsDataCheck(selled,aliPid,getSourceTbl,start, PAGESIZE);
			request.setAttribute("gbbs", goodsCheckBeans);
		}

//		//总条数 批量筛选用
//		int goodsCheckCount = ips.getTbgooddataCount();
//		SplitPage.buildPager(request, goodsCheckCount, PAGESIZE, page);
		request.setAttribute("cid", cid);
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("categoryId1", categoryId1);
		request.setAttribute("similarityId", aliPid);
		request.setAttribute("ylbbPid", ylbbPid);

		try {
			request.getRequestDispatcher("/website/taobaoSave.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	/**
	 * 方法描述:查询ali对标1688所以产品
	 * author:zlw
	 */
	public void findAllYLbbInfo(HttpServletRequest request, HttpServletResponse response) {

		//相似度
//		String similarityId = request.getParameter("similarityId");
		//无对标标识
		String noBenchFlag = request.getParameter("noBenchFlag");
		//alipid
		String aliPid = request.getParameter("aliPid");
		//1688pid
		String ylbbPid = request.getParameter("ylbbPid");
//		String ylbbPid = "12323";
		//销量
		String su = request.getParameter("selled");
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("categoryId1");

		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);

		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);

		//取得登录的用户名字
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		noBenchFlag = adm.getAdmName();
//		noBenchFlag="buy1";
//		String getSourceTbl = ips.getSourceTbl(aliPid);
		String getSourceTbl = "";
//		if(!"".equals(getSourceTbl)){
			//根据分类查结果
			List<GoodsCheckBean> goodsCheckBeans = ips.findYLGoodsDataCheck(selled,noBenchFlag,aliPid,start, PAGESIZE);

			for(int i=0;i<goodsCheckBeans.size();i++){

				int sold = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold(), "(\\d+(\\.\\d+){0,1})"));
				int sold1 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold1(), "(\\d+(\\.\\d+){0,1})"));
				int sold2 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold2(), "(\\d+(\\.\\d+){0,1})"));
				int sold3 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold3(), "(\\d+(\\.\\d+){0,1})"));
				int sold4 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold4(), "(\\d+(\\.\\d+){0,1})"));
				int sold5 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold5(), "(\\d+(\\.\\d+){0,1})"));
				int sold6 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold6(), "(\\d+(\\.\\d+){0,1})"));
				int sold7 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold7(), "(\\d+(\\.\\d+){0,1})"));
				int sold8 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold8(), "(\\d+(\\.\\d+){0,1})"));
				int sold9 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold9(), "(\\d+(\\.\\d+){0,1})"));
				int sold10 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold10(), "(\\d+(\\.\\d+){0,1})"));
				int sold11 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold11(), "(\\d+(\\.\\d+){0,1})"));

				int[] soldAry = {sold,sold1,sold2,sold3,sold4,sold5,sold6,sold7,sold8,sold9,sold10,sold11};

				Arrays.sort(soldAry);
				for(int k=soldAry.length-1; k>=0;k--){
					if(k>8){
						if(soldAry[k]==sold){
							goodsCheckBeans.get(i).setGoodsSoldFlag(1);
						}else if(soldAry[k]==sold1){
							goodsCheckBeans.get(i).setGoodsSoldFlag1(1);
						}else if(soldAry[k]==sold2){
							goodsCheckBeans.get(i).setGoodsSoldFlag2(1);
						}else if(soldAry[k]==sold3){
							goodsCheckBeans.get(i).setGoodsSoldFlag3(1);
						}else if(soldAry[k]==sold4){
							goodsCheckBeans.get(i).setGoodsSoldFlag4(1);
						}else if(soldAry[k]==sold5){
							goodsCheckBeans.get(i).setGoodsSoldFlag5(1);
						}else if(soldAry[k]==sold6){
							goodsCheckBeans.get(i).setGoodsSoldFlag6(1);
						}else if(soldAry[k]==sold7){
							goodsCheckBeans.get(i).setGoodsSoldFlag7(1);
						}else if(soldAry[k]==sold8){
							goodsCheckBeans.get(i).setGoodsSoldFlag8(1);
						}else if(soldAry[k]==sold9){
							goodsCheckBeans.get(i).setGoodsSoldFlag9(1);
						}else if(soldAry[k]==sold10){
							goodsCheckBeans.get(i).setGoodsSoldFlag10(1);
						}else if(soldAry[k]==sold11){
							goodsCheckBeans.get(i).setGoodsSoldFlag11(1);
						}
					}else{
						break;
					}

				}

//				//解析规格
//				List<TypeBean> typeList = getTypesList(goodsCheckBeans.get(i).getTypes());
//						
//				List<ImportExSku> skuList = new ArrayList<ImportExSku>();
//				JSONArray sku_json = JSONArray.fromObject(goodsCheckBeans.get(i).getSku().replace(";",""));
//				skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
//				if(typeList!=null && skuList!=null){
//					// sku解析
//					List<ImportExSkuShow> cbSkusList = combineSkuList(typeList, skuList);
//					String showTypeImg="";
//					String showTypeQuantity="";
//					for (ImportExSkuShow tyb : cbSkusList) {
//						String skuAtt = tyb.getSkuAttrs();
//						String  availQuantity = String.valueOf(tyb.getPpIds());
//						
//						showTypeImg +=";"+skuAtt;
//						showTypeQuantity +=";"+availQuantity;
//					}
//					goodsCheckBeans.get(i).setShowTypeImg(showTypeImg);
//					goodsCheckBeans.get(i).setShowTypeNum(showTypeQuantity);
//				}
			}


//	        List<Comparator<GoodsCheckBean>> mCmpList = new ArrayList<Comparator<GoodsCheckBean>>();
//	        mCmpList.add(soldASC); 
//	        sort(goodsCheckBeans, mCmpList);

			request.setAttribute("gbbs", goodsCheckBeans);
//		}

		//总条数 批量筛选用
		int goodsCheckCount = ips.getYlbbGooddataCount(noBenchFlag,selled);
		SplitPage.buildPager(request, goodsCheckCount, PAGESIZE, page);
		request.setAttribute("cid", cid);
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("categoryId1", categoryId1);
		request.setAttribute("similarityId", aliPid);
		request.setAttribute("ylbbPid", ylbbPid);
		request.setAttribute("selled", selled);

		try {
			request.getRequestDispatcher("/website/ylbbSave.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 方法描述:健康页面以及404跟踪页面
	 */
	public void getErrorInfo(HttpServletRequest request, HttpServletResponse response) {

		//无对标标识
		String userId = request.getParameter("userId");
		//alipid
		String timeFrom = request.getParameter("timeFrom");
		//1688pid
		String timeTo = request.getParameter("timeTo");
		//销量
		String su = request.getParameter("selled");
		String valid1 = request.getParameter("valid");
		if (valid1 ==null) {
			valid1="5";
		}
		int valid=Integer.parseInt(valid1);
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("categoryId1");

		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);

		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);

		//取得登录的用户名字
//		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
//		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
//		noBenchFlag = adm.getAdmName();
//		String getSourceTbl = "";
		//根据分类查结果
		List<GoodsCheckBean> goodsCheckBeans = ips.getErrorInfo(userId,timeFrom,timeTo,start,valid, PAGESIZE);


		request.setAttribute("gbbs", goodsCheckBeans);

		//总条数 批量筛选用
		int goodsCheckCount = ips.getErrorInfoCount(userId,timeFrom,timeTo);
		SplitPage.buildPager(request, goodsCheckCount, PAGESIZE, page);
		request.setAttribute("cid", cid);
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("categoryId1", categoryId1);
		request.setAttribute("userId", userId);
		request.setAttribute("timeFrom", timeFrom);
		request.setAttribute("timeTo", timeTo);
		request.setAttribute("valid", valid);

		try {
			request.getRequestDispatcher("/website/health_page.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 方法描述:查询lire查询产品
	 */
	public void findLireImgInfo(HttpServletRequest request, HttpServletResponse response) {

		//相似度
//		String similarityId = request.getParameter("similarityId");
		//无对标标识
		String noBenchFlag = request.getParameter("noBenchFlag");
		//alipid
		String aliPid = request.getParameter("aliPid");
		//1688pid
		String ylbbPid = request.getParameter("ylbbPid");
//		String ylbbPid = "12323";
		//销量
		String su = request.getParameter("selled");
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("categoryId1");

		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);

		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);

		//取得登录的用户名字
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		noBenchFlag = adm.getAdmName();
		String getSourceTbl = "";
		String serveIp = request.getServerName();
		int flag=0;
		if(serveIp.indexOf("192.")>-1){
			flag=1;
		}
			//根据分类查结果
			List<GoodsCheckBean> goodsCheckBeans = ips.findLireImgInfo(selled,noBenchFlag,aliPid,start, PAGESIZE,flag);

//			for(int i=0;i<goodsCheckBeans.size();i++){
//				
//				int sold = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold(), "(\\d+(\\.\\d+){0,1})"));
//				int sold1 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold1(), "(\\d+(\\.\\d+){0,1})"));
//				int sold2 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold2(), "(\\d+(\\.\\d+){0,1})"));
//				int sold3 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold3(), "(\\d+(\\.\\d+){0,1})"));
//				int sold4 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold4(), "(\\d+(\\.\\d+){0,1})"));
//				int sold5 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold5(), "(\\d+(\\.\\d+){0,1})"));
//				int sold6 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold6(), "(\\d+(\\.\\d+){0,1})"));
//				int sold7 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold7(), "(\\d+(\\.\\d+){0,1})"));
//				int sold8 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold8(), "(\\d+(\\.\\d+){0,1})"));
//				int sold9 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold9(), "(\\d+(\\.\\d+){0,1})"));
//				int sold10 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold10(), "(\\d+(\\.\\d+){0,1})"));
//				int sold11 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold11(), "(\\d+(\\.\\d+){0,1})"));
//				
//				int[] soldAry = {sold,sold1,sold2,sold3,sold4,sold5,sold6,sold7,sold8,sold9,sold10,sold11};
//				
//				Arrays.sort(soldAry);
//				for(int k=soldAry.length-1; k>=0;k--){
//					if(k>8){
//						if(soldAry[k]==sold){
//							goodsCheckBeans.get(i).setGoodsSoldFlag(1);
//						}else if(soldAry[k]==sold1){
//							goodsCheckBeans.get(i).setGoodsSoldFlag1(1);
//						}else if(soldAry[k]==sold2){
//							goodsCheckBeans.get(i).setGoodsSoldFlag2(1);
//						}else if(soldAry[k]==sold3){
//							goodsCheckBeans.get(i).setGoodsSoldFlag3(1);
//						}else if(soldAry[k]==sold4){
//							goodsCheckBeans.get(i).setGoodsSoldFlag4(1);
//						}else if(soldAry[k]==sold5){
//							goodsCheckBeans.get(i).setGoodsSoldFlag5(1);
//						}else if(soldAry[k]==sold6){
//							goodsCheckBeans.get(i).setGoodsSoldFlag6(1);
//						}else if(soldAry[k]==sold7){
//							goodsCheckBeans.get(i).setGoodsSoldFlag7(1);
//						}else if(soldAry[k]==sold8){
//							goodsCheckBeans.get(i).setGoodsSoldFlag8(1);
//						}else if(soldAry[k]==sold9){
//							goodsCheckBeans.get(i).setGoodsSoldFlag9(1);
//						}else if(soldAry[k]==sold10){
//							goodsCheckBeans.get(i).setGoodsSoldFlag10(1);
//						}else if(soldAry[k]==sold11){
//							goodsCheckBeans.get(i).setGoodsSoldFlag11(1);
//						}
//					}else{
//						break;
//					}
//					
//				}
//				
//			}

			request.setAttribute("gbbs", goodsCheckBeans);

		//总条数 批量筛选用
		int goodsCheckCount = ips.getLireImgCount(noBenchFlag,selled);
//		int goodsCheckCount = 10;
		SplitPage.buildPager(request, goodsCheckCount, PAGESIZE, page);
		request.setAttribute("cid", cid);
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("categoryId1", categoryId1);
		request.setAttribute("similarityId", aliPid);
		request.setAttribute("ylbbPid", ylbbPid);
		request.setAttribute("selled", selled);

		try {
//			request.getRequestDispatcher("/website/lire_img_info.jsp").forward(request, response);
			request.getRequestDispatcher("/website/lire_same_img_info.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}




public void findAllYLbbInfo1(HttpServletRequest request, HttpServletResponse response) {

		//相似度
//		String similarityId = request.getParameter("similarityId");
		//无对标标识
		String noBenchFlag = request.getParameter("noBenchFlag");
		//alipid
		String aliPid = request.getParameter("aliPid");
		//1688pid
		String ylbbPid = request.getParameter("ylbbPid");
//		String ylbbPid = "12323";
		//销量
		String su = request.getParameter("selled");
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("categoryId1");

		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * 400;

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);

		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);

		//取得登录的用户名字
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		noBenchFlag = adm.getAdmName();
		String getSourceTbl = "";
			//根据分类查结果
			List<GoodsCheckBean> goodsCheckBeans = ips.findTbGoodsDataCheck(selled,noBenchFlag,aliPid,start, 400);

//			for(int i=0;i<goodsCheckBeans.size();i++){
//				
//				int sold = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold(), "(\\d+(\\.\\d+){0,1})"));
//				int sold1 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold1(), "(\\d+(\\.\\d+){0,1})"));
//				int sold2 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold2(), "(\\d+(\\.\\d+){0,1})"));
//				int sold3 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold3(), "(\\d+(\\.\\d+){0,1})"));
//				int sold4 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold4(), "(\\d+(\\.\\d+){0,1})"));
//				int sold5 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold5(), "(\\d+(\\.\\d+){0,1})"));
//				int sold6 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold6(), "(\\d+(\\.\\d+){0,1})"));
//				int sold7 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold7(), "(\\d+(\\.\\d+){0,1})"));
//				int sold8 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold8(), "(\\d+(\\.\\d+){0,1})"));
//				int sold9 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold9(), "(\\d+(\\.\\d+){0,1})"));
//				int sold10 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold10(), "(\\d+(\\.\\d+){0,1})"));
//				int sold11 = Integer.valueOf(StrUtils.matchStr(goodsCheckBeans.get(i).getGoodsSold11(), "(\\d+(\\.\\d+){0,1})"));
//				
//				int[] soldAry = {sold,sold1,sold2,sold3,sold4,sold5,sold6,sold7,sold8,sold9,sold10,sold11};
//				
//				Arrays.sort(soldAry);
//				for(int k=soldAry.length-1; k>=0;k--){
//					if(k>8){
//						if(soldAry[k]==sold){
//							goodsCheckBeans.get(i).setGoodsSoldFlag(1);
//						}else if(soldAry[k]==sold1){
//							goodsCheckBeans.get(i).setGoodsSoldFlag1(1);
//						}else if(soldAry[k]==sold2){
//							goodsCheckBeans.get(i).setGoodsSoldFlag2(1);
//						}else if(soldAry[k]==sold3){
//							goodsCheckBeans.get(i).setGoodsSoldFlag3(1);
//						}else if(soldAry[k]==sold4){
//							goodsCheckBeans.get(i).setGoodsSoldFlag4(1);
//						}else if(soldAry[k]==sold5){
//							goodsCheckBeans.get(i).setGoodsSoldFlag5(1);
//						}else if(soldAry[k]==sold6){
//							goodsCheckBeans.get(i).setGoodsSoldFlag6(1);
//						}else if(soldAry[k]==sold7){
//							goodsCheckBeans.get(i).setGoodsSoldFlag7(1);
//						}else if(soldAry[k]==sold8){
//							goodsCheckBeans.get(i).setGoodsSoldFlag8(1);
//						}else if(soldAry[k]==sold9){
//							goodsCheckBeans.get(i).setGoodsSoldFlag9(1);
//						}else if(soldAry[k]==sold10){
//							goodsCheckBeans.get(i).setGoodsSoldFlag10(1);
//						}else if(soldAry[k]==sold11){
//							goodsCheckBeans.get(i).setGoodsSoldFlag11(1);
//						}
//					}else{
//						break;
//					}
//					
//				}
//				
//			}



			request.setAttribute("gbbs", goodsCheckBeans);

		//总条数 批量筛选用
//		int goodsCheckCount = ips.getYlbbGooddataCount(noBenchFlag,selled);
		int goodsCheckCount = 60000;
		SplitPage.buildPager(request, goodsCheckCount, PAGESIZE, page);
		request.setAttribute("cid", cid);
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("categoryId1", categoryId1);
		request.setAttribute("similarityId", aliPid);
		request.setAttribute("ylbbPid", ylbbPid);
		request.setAttribute("selled", selled);

		try {
			request.getRequestDispatcher("/website/ylbbSaveValid.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public List<TypeBean> getTypesList(String types){
		if(types != null && !types.isEmpty()){
			List<TypeBean> typeList = new ArrayList<TypeBean>();
			if(types.indexOf("+#") > -1){
				try {
					String types_ = types.replace("[", "[{\"").replace("]", "\"}]").trim();
					types_ = types_.replace("=", "\":\"").trim();
					types_ = types_.replaceAll(",\\s+", "\"},{\"").trim();
					types_ = types_.replaceAll("\\+#\\s+", "\",\"").trim();
					JSONArray fromObject = JSONArray.fromObject(types_);
					typeList = (List<TypeBean>)JSONArray.toCollection(fromObject,TypeBean.class);
				} catch (Exception e) {
					types = types.replace("[", "").replace("]", "").trim();
					String[] types_s = types.split(",\\s+");
					TypeBean bean = null;
					String[] tems = null;
					String tem = null;
					for(int k=0;k<types_s.length;k++){
						if(types_s[k].isEmpty()){
							continue;
						}
						bean = new TypeBean();
						String[] type = types_s[k].split("\\+#\\s+");
						for(int j=0;j<type.length;j++){
							if(StrUtils.isFind(type[j], "(id=)")){
								tems = type[j].split("id=");
								tem = tems.length>1?tems[1]:"";
								bean.setId(tem);
							}else if(StrUtils.isFind(type[j], "(type=)")){
								tems = type[j].split("type=");
								tem = tems.length>1?tems[1]:"";
								bean.setType(tem);
							}else if(StrUtils.isFind(type[j], "(value=)")){
								tems = type[j].split("value=");
								tem = tems.length>1?tems[1]:"";
								bean.setValue(tem);
							}else if(StrUtils.isFind(type[j], "(img=)")){
								tems = type[j].split("img=");
								tem = tems.length>1?tems[1]:"";
								bean.setImg(tem);
							}
						}
						typeList.add(bean);
					}
				}
			}else{
				JSONArray fromObject = JSONArray.fromObject(types);
				typeList = (List<TypeBean>)JSONArray.toCollection(fromObject,TypeBean.class);
			}
//			goods.setType(typeList);
			return typeList;
		}
		return null;
	}


	public List<ImportExSkuShow> combineSkuList(List<TypeBean> typeList, List<ImportExSku> skuList) {

		List<ImportExSkuShow> cbSkuLst = new ArrayList<ImportExSkuShow>();

		for (ImportExSku ites : skuList) {
			String skuAttrs = "";
			ImportExSkuShow ipes = new ImportExSkuShow();
			// PropIds分组循环
			String[] ppidLst = ites.getSkuPropIds().split(",");
			int totalCount = 0;
			int arrLength = ppidLst.length;
			for (String ppid : ppidLst) {
				// 比较type类别的数据，获取类别信息
				for (TypeBean tyb : typeList) {
					if (ppid.equals(tyb.getId())) {
//						skuAttrs += ";" + tyb.getId() + "@" + tyb.getType() + "@" + tyb.getValue();
						skuAttrs += ";" +tyb.getImg();
						totalCount++;
						break;
					}
				}
			}
			ppidLst = null;
			// 解析attr数据，获取类别名称对应的ID
			String[] skuAtLst = ites.getSkuAttr().split(";");
			for (String ska : skuAtLst) {
				String[] cbLst = ska.split(":");
				if (cbLst.length == 2) {
					for (TypeBean tyb : typeList) {
						if (cbLst[1].equals(tyb.getId())) {
							tyb.setTypeId(cbLst[0]);
							break;
						}
					}
				}
			}
			skuAtLst = null;
//			ipes.setPpIds(ites.getSkuPropIds().replace(",", "_"));
			ipes.setPpIds(String.valueOf(ites.getSkuVal().getAvailQuantity()));
			if (skuAttrs == null || "".equals(skuAttrs)) {
				ipes = null;
			} else {
				ipes.setSkuAttrs(skuAttrs.substring(1));
				// skuAttrs获取失败，则不显示sku数据，并且在更新后覆盖原数据
				// type多规格生成的数据中sku只有单规格的数据也剔除掉
				if (arrLength > 0 && arrLength == totalCount) {
					cbSkuLst.add(ipes);
				}

			}
			skuAttrs = null;
		}
		skuList = null;
		return cbSkuLst;
	}


	/**
	 * 方法描述:查询货源工厂
	 *
	 */
	public void findSuppliesFactory(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		String shopId = request.getParameter("shopId");
		try {
			int page = 1;
			if(str != null) {
				page = Integer.parseInt(str);
			}
			int start = (page-1) * PAGESIZE;
			IPictureComparisonService ips = new PictureComparisonServiceImpl();
			List<CategoryBean> categoryList=ips.getCategoryInfo();
			request.setAttribute("categoryList", categoryList);
			List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
			//查询结果集
			List<GoodsCheckBean> goodsCheckBeans = ips.findSamplInfo(0,"",shopId,start, PAGESIZE);
			request.setAttribute("gbbs", goodsCheckBeans);
			//总条数 批量筛选用分页
			int goodsCheckCount = ips.getFactoryCount("",shopId);
			SplitPage.buildPager(request, goodsCheckCount, PAGESIZE, page);
			request.setAttribute("shopId", shopId);
			//异步查询时间
			int id=0;
			Map<String, Object> times = ips.selectupdateTime();
			request.setAttribute("times",times);
			request.getRequestDispatcher("/website/sampingInfo.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//数据更新 去掉下架的
	public void validationDateUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException{

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();
		int flag = ips.validationDateUpdate("");
		int id = 0 ;
		String parameter = request.getParameter("id");
		if(!org.apache.commons.lang.StringUtils.isBlank(parameter)){
			id=Integer.parseInt(parameter);
		}
		ips.updateTime(id,1);
		out.print(flag);
		out.flush();
		out.close();
	}

	//店铺聚合优化
	public void shopPolymerization(HttpServletRequest request, HttpServletResponse response) throws IOException{

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();
		List<GoodsCheckBean> beans = ips.getOneShopInFo("");
		if(beans.size()>0){
			for(int i=0;i<beans.size();i++){
				String sourceShopId = beans.get(i).getShopId();
				String sourceGoodsPid = beans.get(i).getGoodsPid();
				String newShopId = beans.get(i).getShopId1();
				if(newShopId!=null && !"".equals(newShopId)){
					String[] aryShopId =  newShopId.split(",");
					for(int j=0;j<aryShopId.length;j++){
						if(aryShopId[j]!=null && !"".equals(aryShopId[j])){
							//更新表聚合店铺
							ips.updatePolymerizationShopId(sourceShopId,sourceGoodsPid,aryShopId[j]);
						}

					}

				}
			}

			//更新时间
			int id=0;
			String parameter = request.getParameter("id");
			if(!org.apache.commons.lang.StringUtils.isBlank(parameter)){
				id=Integer.parseInt(parameter);
			}

			ips.updateTime(id,2);

		}

		out.print(0);
		out.flush();
		out.close();
	}



	/**
	 * 方法描述:根据工厂ID查询工厂下面所有商品
	 *
	 */
	public void fingGoodsByShopId(HttpServletRequest request, HttpServletResponse response) {
		    IPictureComparisonService ips = new PictureComparisonServiceImpl();
            String shop_id=request.getParameter("shop_id");
            String str1 = request.getParameter("page");
            int total=Integer.valueOf(request.getParameter("total"));
    		int page = 1;
    		if(str1 != null) {
    			page = Integer.parseInt(str1);
    		}
    		int start = (page-1) * PAGESIZE;
    		List<AliInfoDataBean> list = ips.fingGoodsByShopId(shop_id,start, PAGESIZE);
    		for (AliInfoDataBean a : list) {
    			List<TypeBean> typeList = new ArrayList<TypeBean>();
    			Map<String,List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
    			String types = StrUtils.object2Str(a.getEntype());
    			if( !StringUtils.isStrNull(types) && !StringUtils.equals(types, "[]")){
    				types = types.replace("[[", "[").replace("]]", "]").trim();
    				String[] matchStrList = types.split(",\\s*\\[");
//    						List<String> matchStrList = StrUtils.matchStrList("(\\[.*\\])", types);
    				TypeBean typeBean = null;
    				String[] tems = null;
    				String tem = null;
    				for(String str:matchStrList){
    					str = str.replace("[", "").replace("]", "");
    					if(str.isEmpty()){
    						continue;
    					}
    					typeBean = new TypeBean();
    					String[] type = str.split(",\\s*");
    					for(int j=0;j<type.length;j++){
    						if(type[j].indexOf("id=") > -1){
    							tems = type[j].split("id=");
    							tem = tems.length > 1 ? tems[1] : "";
    							typeBean.setId(tem);
    						}else if(type[j].indexOf("type=") > -1){
    							tems = type[j].split("type=");
    							tem = tems.length > 1 ? tems[1] : "";
    							typeBean.setType(tem.replaceAll(chineseChar, ""));
    						}else if(type[j].indexOf("value=") > -1){
    							tems = type[j].split("value=");
    							tem = tems.length>1 ? tems[1] : "";
    							tem = StringUtils.equals(tem, "null") ? "" : tem;
    							typeBean.setValue(tem.replaceAll(chineseChar, ""));
    						}else if(type[j].indexOf("img=") > -1){
    							tems = type[j].split("img=");
    							tem = tems.length > 1 ? tems[1] : "";
    							tem = tem.endsWith(".jpg") ? tem : "";
    							tem = StringUtils.isStrNull(tem) || StringUtils.equals(tem, "null") ? "" : "" + tem;
    							typeBean.setImg(tem);
    						}
    					}
    					List<TypeBean> list1 = typeMap.get(typeBean.getType());
    					if(list1 == null ){
    						list1 = new ArrayList<TypeBean>();
    					}
    					if(StringUtils.isStrNull(typeBean.getType())){
    						continue;
    					}
    					if(StringUtils.isStrNull(typeBean.getValue())){
    						continue;
    					}
    					list1.add(typeBean);
    					typeMap.put(typeBean.getType(), list1);
    				}
    				Iterator<Entry<String, List<TypeBean>>> iterator = typeMap.entrySet().iterator();
    				List<TypeBean> valueImg = new ArrayList<TypeBean>();
    				List<TypeBean> valueTitle = new ArrayList<TypeBean>();
    				while(iterator.hasNext()){
    					List<TypeBean> values = iterator.next().getValue();
    					valueImg.clear();
    					valueTitle.clear();
    					for(TypeBean value : values){
    						if(!StringUtils.isStrNull(value.getImg())){
    							valueImg.add(value);
    						}else{
    							valueTitle.add(value);
    						}
    					}
    					if(!valueImg.isEmpty()){
    						typeList.addAll(valueImg);
    					}
    					if(!valueTitle.isEmpty()){
    						typeList.addAll(valueTitle);
    					}
    				}
    				a.setType(typeList);
    			}
    			//
    			StringBuilder list_type=new StringBuilder();
    			List<TypeBean> type = a.getType();
    			for(TypeBean typeBean : type){
					if(StringUtils.isStrNull(typeBean.getType())){
						continue;
					}
					list_type.append(typeBean.getType());
					String typeParam  = typeBean.getType().trim();
					typeParam = !StringUtils.isStrNull(typeParam) ?
							typeParam.replaceAll("(\\.\\s*)", " ").replaceAll("[',\";#\\:]", "")
							.replace("<", "&lt;").replace(">", "&gt;") : "";

					typeBean.setType(typeParam.substring(0,1).toUpperCase()+typeParam.substring(1));

					String labType = !StringUtils.isStrNull(typeParam) ?
							typeParam.replaceAll("(\\W+)", "").toLowerCase() : "";
					typeBean.setLableType(labType);
					String value = typeBean.getValue();
					value = !StringUtils.isStrNull(value) ?
							value.replaceAll("(\\(.*\\))", "").replaceAll("(\\[.*\\])", "") : "";
					if(!StringUtils.isStrNull(value)){
						value = value.replace("<", "&lt;").replace(">", "&gt;");
						typeBean.setValue(value.replaceAll("(\\s+)", " "));
					}
				}

    			a.setList_type(list_type.toString());
			}
    		SplitPage.buildPager(request, total, PAGESIZE, page);
    		for(int i=0;i<list.size();i++){
    			StringBuilder sb=new StringBuilder();
    			String type_name="";
    			AliInfoDataBean a=list.get(i);
    			List<TypeBean> b_list=a.getType();
    			for(int j=0;j<b_list.size();j++){
    				TypeBean t=b_list.get(j);
    				if(type_name.equals(t.getLableType())){
    					if(StringUtils.isStrNull(t.getImg())){
    						sb.append("<em id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getGoods_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
    								+ "style='margin: 5px; padding: 3px; display: inline-block; border: 1px solid green; "
    								+ "font-style: normal' title='"+t.getValue()+"'>"+t.getValue()+"</em>");
    					}else{
    						sb.append("<img id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getGoods_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
        							+ "style='margin: 5px; float: left; width: 44px; height: 44px; display: inline-block; "
        							+ "border: 1px solid green' title='"+t.getValue()+"' src='"+t.getImg().replace("http:", "https:")+"'>");
    					}
    					if((b_list.size()>j+1 && !b_list.get(j+1).getLableType().equals(type_name)) || b_list.size()==j+1){
    						sb.append("<input type='hidden' class='css_"+a.getGoods_pid()+"' id='"+i+""+t.getLableType()+"_"+a.getGoods_pid()+"'>");
    						sb.append("</div>");
    					}
    				}else{
    					if(!StringUtils.isStrNull(type_name)){
    						sb.append("</div>");
    					}
    					sb.append("<br>"+t.getLableType()).append(":<div style='overflow:hidden;width:300px;'>");
    					if(StringUtils.isStrNull(t.getImg())){
    						sb.append("<em id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getGoods_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
    								+ "style='margin: 5px; padding: 3px; display: inline-block; border: 1px solid green; "
    								+ "font-style: normal' title='"+t.getValue()+"'>"+t.getValue()+"</em>");
    					}else{
    						sb.append("<img id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getGoods_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
        							+ "style='margin: 5px; float: left; width: 44px; height: 44px; display: inline-block; "
        							+ "border: 1px solid green' title='"+t.getValue()+"' src='"+t.getImg().replace("http:", "https:")+"'>");
    					}
    				}
    				type_name=t.getLableType();
    			}
    			a.setType_msg(sb.toString());
    			//规格库存
    			a.setSku_inventory(getSkuCount(a.getSku_inventory()));
    		}
    		request.setAttribute("list", list);
		try {
			request.getRequestDispatcher("/website/sampingInfoDetails.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static String getSkuCount(String skuMap){
		StringBuilder sb=new StringBuilder();
		if(!StringUtils.isStrNull(skuMap) && skuMap.contains("skuMap:") && skuMap.contains(",end")){
			skuMap=skuMap.replaceAll("\"","");
			skuMap=skuMap.split("skuMap:")[1];
			skuMap=skuMap.split(",end")[0];
			skuMap=skuMap.substring(1,skuMap.length());
			skuMap=skuMap.substring(0,skuMap.length()-2);
			String [] skus =skuMap.split("},");
			int count=0;
			for(int i=0;i<skus.length;i++){
				String sku_=skus[i];
				sku_=sku_.replace("&gt;", "&");
				String []maps=sku_.split("\\{");
				String []sku1=maps[1].split(",");
				String name=maps[0];
				for (int j=0;j<sku1.length;j++) {
					if(sku1[j].contains("canBookCount") && Integer.valueOf(sku1[j].split(":")[1])<=0){
						sb.append(name).append(":").append("<span style='color:red'>"+sku1[j].split(":")[1]+"</span>").append("<br>");
						count++;
						break;
					}
				}
			}
			if(count>=10){
				sb=new StringBuilder("该商品库存数量超过10个为0，不建议采样");
			}
		}
		return sb.toString();
	}




//    //销量降序
//    private Comparator<GoodsCheckBean> soldASC = new Comparator<GoodsCheckBean>() {  
//    	  
//        @Override  
//        public int compare(GoodsCheckBean o1, GoodsCheckBean o2) { 
//        	
//        	if(o1.getScore().compareTo(o2.getScore()) > 0){
//        		return -1;
//        	}else if(o1.getScore().compareTo(o2.getScore()) == 0){
//        		return 0;
//        	}else{
//        		return 1;
//        	}
//        }  
//    }; 
//    
//    //排序
//	public void sort(List<GoodsCheckBean> list, final List<Comparator<GoodsCheckBean>> comList) {  
//        if (comList == null)  
//            return;  
//        Comparator<GoodsCheckBean> cmp = new Comparator<GoodsCheckBean>() {  
//            @Override  
//            public int compare(GoodsCheckBean o1, GoodsCheckBean o2) {  
//                for (Comparator<GoodsCheckBean> comparator : comList) {  
//                    if (comparator.compare(o1, o2) > 0) {  
//                        return 1;  
//                    } else if (comparator.compare(o1, o2) < 0) {  
//                        return -1;  
//                    }  
//                }  
//                return 0;  
//            }  
//        };  
//        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 
//        Collections.sort(list, cmp);  
//    }  

	/**
	 * 方法描述:查询分类
	 * author:zlw
	 * date:2015年11月27日
	 */
	public void findCategory(HttpServletRequest request, HttpServletResponse response) {
		String cid = request.getParameter("cid");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		//取得二级分类
		List<CategoryBean> categoryList1=ips.getCategoryInfo1(cid);

		request.setAttribute("categoryList1", categoryList1);

		request.setAttribute("categoryList", categoryList);

		try {
			request.getRequestDispatcher("/website/picturecompare.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//保存淘宝信息
	public void saveTbgooddata(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodId = request.getParameter("goodId");
		String ylUrl = request.getParameter("tbUrl");
		String tbUrl = request.getParameter("tbUrl").replace(".html", "");
		String shopId = request.getParameter("shopId");
		//1688价格
		String moqPrice = request.getParameter("moqPrice");
		//1688类别
		String catId = request.getParameter("catId");

		int idx = tbUrl.lastIndexOf("/");
		//1688pid
		String ylId = tbUrl.substring(idx+1,tbUrl.length());

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(goodId != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodId);
			tbgood.setYlUrl(ylUrl);
			tbgood.setYlPid(ylId);
			tbgood.setShopId(shopId);
			tbgood.setYlPrice(moqPrice);
			tbgood.setCatId(catId);
			//1688产品插入
//			int tbg1 = ips.saveTbGood(tbgood);
			//更新已处理完避免再次出现
			int tbg = ips.updateAliFlag(tbgood);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}



	//保存淘宝信息
	public void updateAliFlag(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodId = request.getParameter("goodId");
//		String tbUrl = request.getParameter("tbUrl").replace(".html", "");
//		String tbPrice = request.getParameter("tbPrice");
//		String tbName = request.getParameter("tbName");
//		String delFlag = request.getParameter("delFlag");

//		int idx = tbUrl.lastIndexOf("/");
//		//1688pid
//		String ylId = tbUrl.substring(idx+1,tbUrl.length());

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(goodId != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodId);
//			tbgood.setTbUrl(ylId);
//			tbgood.setTbPrice(tbPrice);
//			tbgood.setTbName(tbName);
			tbgood.setDelFlag(1);
//			//1688产品插入
//			int tbg1 = ips.saveTbGood(tbgood);

			//删除1688产品数据
			ips.delForGoodsPid(tbgood);
			//更新已处理完避免再次出现
			int tbg = ips.updateAliFlag(tbgood);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}


	//更新无对标
	public void updateYlFlag(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodId = request.getParameter("goodId");
//		String tbUrl = request.getParameter("tbUrl").replace(".html", "");
		String delFlag = request.getParameter("delFlag");
//		String sourceYlpid = request.getParameter("sourceYlpid");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(goodId != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodId);
			tbgood.setDelFlag(Integer.valueOf(delFlag));
			//更新无对标状态
			int tbg = ips.updateSourceAliFlag(tbgood);
			//删除已选的对标商品
			ips.delForGoodsPid(tbgood);

			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}



	//更新标识状态
	public void updateAliInfoFlag(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodId = request.getParameter("goodId");
//		String tbUrl = request.getParameter("tbUrl").replace(".html", "");
		String delFlag = request.getParameter("delFlag");
		String sourceYlpid = request.getParameter("sourceYlpid");
		String ylWeight = request.getParameter("weight");
		String lotUnit = request.getParameter("lotUnit");



		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(goodId != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodId);
			tbgood.setDelFlag(Integer.valueOf(delFlag));
			tbgood.setSourceYlpid(sourceYlpid);
			tbgood.setYlWeight(ylWeight);
			tbgood.setLotUnit(lotUnit);
			//取得登录的用户名字
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			tbgood.setUserName(adm.getAdmName());
			//更新对标情况
			int tbg = ips.updateAliInfoFlag(tbgood);
//			//更新27对标表
//			ips.updateRebidFlag(tbgood);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}

	public void updateYlPid(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodsPid = request.getParameter("goodsPid");
		String sourceYlpid = request.getParameter("sourceYlpid");
		String ylPid = request.getParameter("ylPid");
		String ylUrl ="https://detail.1688.com/offer/"+ylPid+".html";
//		String tbUrl = request.getParameter("tbUrl").replace(".html", "");
//		String delFlag = request.getParameter("delFlag");
		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(ylPid != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodsPid);
			tbgood.setYlPid(ylPid);
			tbgood.setYlUrl(ylUrl);
			tbgood.setSourceYlpid(sourceYlpid);
			tbgood.setDelFlag(4);
			//更新对标情况
			int tbg = ips.updateYlFlag(tbgood);
			//更新27对标表
			ips.updateRebidFlag(tbgood);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}


	//保存对标商品信息
	public void saveDbYlgooddata(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodId = request.getParameter("goodId");
		String ylUrl = request.getParameter("tbUrl");
		String tbUrl = request.getParameter("tbUrl").replace(".html", "");
		String shopId = request.getParameter("shopId");
		//1688价格
		String moqPrice = request.getParameter("moqPrice");
		//1688类别
		String catId = request.getParameter("catId");
		//产品来源标识
		String pidSource = request.getParameter("selled");

		int idx = tbUrl.lastIndexOf("/");
		//1688pid
		String ylId = tbUrl.substring(idx+1,tbUrl.length());

		String priorityFlag = request.getParameter("priorityFlag");
		String sourceProFlag = request.getParameter("sourceProFlag");
		String ylImg = request.getParameter("ylImg");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(goodId != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodId);
			tbgood.setYlUrl(ylUrl);
			tbgood.setYlPid(ylId);
			tbgood.setShopId(shopId);
			tbgood.setYlPrice(moqPrice);
			tbgood.setCatId(catId);
			tbgood.setPriorityFlag(Integer.valueOf(priorityFlag));
			tbgood.setSourceProFlag(Integer.valueOf(sourceProFlag));
			tbgood.setYlImg(ylImg);
			tbgood.setPidSource(Integer.valueOf(pidSource));
			//1688产品插入
			ips.saveDbYlGood(tbgood);
			//更新已处理完避免再次出现
			tbgood.setDelFlag(1);
			int tbg = ips.updateSourceAliFlag(tbgood);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}


	//保存lire商品信息
	public void saveDbLiredata(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodId = request.getParameter("goodsPid");

		String flag = request.getParameter("flag");
//		String tbUrl = request.getParameter("tbUrl").replace(".html", "");
//		String shopId = request.getParameter("shopId");
//		//1688价格
//		String moqPrice = request.getParameter("moqPrice");
//		//1688类别
//		String catId = request.getParameter("catId");
//		//产品来源标识
//		String pidSource = request.getParameter("selled");
//		
//		int idx = tbUrl.lastIndexOf("/");
//		//1688pid
//		String ylId = tbUrl.substring(idx+1,tbUrl.length());
//		
//		String priorityFlag = request.getParameter("priorityFlag");
//		String sourceProFlag = request.getParameter("sourceProFlag");
//		String ylImg = request.getParameter("ylImg");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(goodId != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodId);
			tbgood.setDelFlag(Integer.valueOf(flag));
			if("6".equals(flag)){
				tbgood.setYlPid(request.getParameter("pidSame"));
				tbgood.setDelFlag(6);
			}
//			tbgood.setYlPid(ylId);
//			tbgood.setShopId(shopId);
//			tbgood.setYlPrice(moqPrice);
//			tbgood.setCatId(catId);
//			tbgood.setPriorityFlag(Integer.valueOf(priorityFlag));
//			tbgood.setSourceProFlag(Integer.valueOf(sourceProFlag));
//			tbgood.setYlImg(ylImg);
//			tbgood.setPidSource(Integer.valueOf(pidSource));
			//1688产品插入
			ips.saveLireGood(tbgood);

			//删除同款
			ips.setGoodsValid(goodId, "yunying", 18, -1, "同款下架");


			//更新已处理完避免再次出现
//			tbgood.setDelFlag(1);
//			int tbg = ips.updateSourceAliFlag(tbgood);
			out.print(1);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}


	//手工录入1688产品更新
	public void updateDbYlbbPid(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String goodsPid = request.getParameter("goodsPid");
		String ylUrl =request.getParameter("ylUrl");
		String ylPid = "";
		//https://detail.1688.com/offer/557648242586.html?spm=b26110380.8880418.csimg003.56.hYQ1dg
		if(ylUrl.indexOf("?")>-1){
			ylPid=ylUrl.substring(ylUrl.lastIndexOf("/")+1, ylUrl.lastIndexOf("?")).replace(".html", "");
		}else{
			//1688pid
			ylPid = ylUrl.substring(ylUrl.lastIndexOf("/")+1,ylUrl.length()).replace(".html", "");
		}
		String priorityFlag = request.getParameter("priorityFlag");
		String sourceProFlag = request.getParameter("sourceProFlag");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		if(goodsPid != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(goodsPid);
			tbgood.setYlUrl(ylUrl);
			tbgood.setYlPid(ylPid);
			tbgood.setShopId("");
			tbgood.setYlPrice("");
			tbgood.setCatId("");
			tbgood.setPriorityFlag(Integer.valueOf(priorityFlag));
			tbgood.setSourceProFlag(Integer.valueOf(sourceProFlag));
			tbgood.setYlImg("");
			//1688产品插入
			ips.saveDbYlGood(tbgood);
			//更新已处理完避免再次出现
			tbgood.setDelFlag(1);
			int tbg = ips.updateSourceAliFlag(tbgood);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
	}



	//更新缓存表有无货源信息
	public void updateCachedata(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String url = request.getParameter("url");
		String flag = request.getParameter("flag");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		int tbg = ips.updateSourceFlag(url,Integer.valueOf(flag));
		out.print(tbg);
		out.flush();
		out.close();

	}

	//保存淘宝信息
	public void pageSaveTbgooddata(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String pId = request.getParameter("pId");
		String tbId = request.getParameter("tbId");
		String delFlag = "0";

		IPictureComparisonService ips = new PictureComparisonServiceImpl();

		GoodsCheckBean goodsCheckBean= ips.getGoodsDataCheck(Integer.valueOf(pId),tbId);
		PrintWriter out = response.getWriter();

		if(pId != null){
			TbGoodBean tbgood = new TbGoodBean();
			tbgood.setGoodId(pId);
			if("tb1".equals(tbId)){
				tbgood.setTbUrl(goodsCheckBean.getTbUrl());
				tbgood.setTbPrice(goodsCheckBean.getTbprice());
				tbgood.setTbName(goodsCheckBean.getTbName());
				tbgood.setTbImg(goodsCheckBean.getTbImg());
				tbgood.setTbFlag(tbId);
			}else if("tb2".equals(tbId)){
				tbgood.setTbUrl(goodsCheckBean.getTbUrl1());
				tbgood.setTbPrice(goodsCheckBean.getTbprice1());
				tbgood.setTbName(goodsCheckBean.getTbName1());
				tbgood.setTbImg(goodsCheckBean.getTbImg1());
				tbgood.setTbFlag(tbId);
			}else if("tb3".equals(tbId)){
				tbgood.setTbUrl(goodsCheckBean.getTbUrl2());
				tbgood.setTbPrice(goodsCheckBean.getTbprice2());
				tbgood.setTbName(goodsCheckBean.getTbName2());
				tbgood.setTbImg(goodsCheckBean.getTbImg2());
				tbgood.setTbFlag(tbId);
			}else if("tb4".equals(tbId)){
				tbgood.setTbUrl(goodsCheckBean.getTbUrl3());
				tbgood.setTbPrice(goodsCheckBean.getTbprice3());
				tbgood.setTbName(goodsCheckBean.getTbName3());
				tbgood.setTbImg(goodsCheckBean.getTbImg3());
				tbgood.setTbFlag(tbId);
			}

			tbgood.setDelFlag(Integer.valueOf(delFlag));
			int tbg = ips.saveTbGood(tbgood);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}

	}


	//保存lire信息
	public void pageUpdateLireFlag(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String pIds = request.getParameter("pId");

		String pidSors = request.getParameter("pidSor");
//		String tbId = request.getParameter("tbId");
//		String delFlag = "0";

		String[] pIdAry = pIds.split(",");
		String[] pidSorsAry = pidSors.split(",");
		IPictureComparisonService ips = new PictureComparisonServiceImpl();

		PrintWriter out = response.getWriter();
		int tbg = 0;
		if(pIds != null){
			for(int i=0; i<pidSorsAry.length;i++){
				tbg = ips.updateLireFlag(pidSorsAry[i]);
			}
			// zlw 2019/01/15 del start
//			for(int i=0; i<pIdAry.length;i++){
//				TbGoodBean tbgood = new TbGoodBean();
//				tbgood.setGoodId(pIdAry[i]);
//				tbgood.setDelFlag(10);
//				//1688产品插入
//				ips.saveLireGood(tbgood);
//			}
			//zlw 2019/01/15 del end
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}

	}


	//更新缓存表有货
	public void updateCacheFlag(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {

		String url = request.getParameter("url");
		String flag = request.getParameter("flag");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		PrintWriter out = response.getWriter();

		int tbg = ips.updateSourceFlag(url,Integer.valueOf(flag));
		out.print(tbg);
		out.flush();
		out.close();

	}

	/**
	 * 去除不售卖的分类
	 * @Title delNorSoldCategory
	 * @Description TODO
	 * @param request
	 * @param response
	 * @return void
	 */
	public void delNorSoldCategory(HttpServletRequest request, HttpServletResponse response) {

		try {
			String catid1 = request.getParameter("catid1");
			String goodspid = request.getParameter("goodspid");
			IPictureComparisonService ips = new PictureComparisonServiceImpl();
			if(org.apache.commons.lang.StringUtils.isBlank(catid1)){
				return ;
			}
			//保存分类,以便找回和修改shel_flag状态
			int tbg = ips.delNotSoldCategory(catid1);
			PrintWriter out = response.getWriter();
			out.print(tbg);
			out.flush();
			out.close();
			//request.getRequestDispatcher("/customerServlet?action=findAllYLbbInfo&className=PictureComparisonServlet").forward(request, response);
			//response.sendRedirect(request.getContextPath()+"/customerServlet?action=findAllYLbbInfo&className=PictureComparisonServlet");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	//查看不售卖的商品
	public void  getNoSoldCategory(HttpServletRequest request, HttpServletResponse response){
		try {

			IPictureComparisonService ips = new PictureComparisonServiceImpl();
			//保存分类,以便找回和修改shel_flag状态
			List<Map<String,Object>> listmaps = ips.getNoSoldCategory();
			JSONArray jsonArray = JSONArray.fromObject(listmaps);
			PrintWriter out = response.getWriter();
			out.print(jsonArray);
			out.flush();
			out.close();
			//response.sendRedirect(request.getContextPath()+"/customerServlet?action=findAllYLbbInfo&className=PictureComparisonServlet");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 修改不售卖的分类为售卖状态
	 */
	public void  updateNoSoldCategoryToSold(HttpServletRequest request, HttpServletResponse response){
		try {
			String[] valus = request.getParameterValues("nocoldcat");
			List<String> lists = Arrays.asList(valus);
			IPictureComparisonService ips = new PictureComparisonServiceImpl();
			int tbg  = ips.updateNoSoldCategoryToSold(lists);
			response.sendRedirect(request.getContextPath()+"/customerServlet?action=findAllYLbbInfo&className=PictureComparisonServlet");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
