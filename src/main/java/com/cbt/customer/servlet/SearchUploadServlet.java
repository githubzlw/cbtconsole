package com.cbt.customer.servlet;

import ceRong.tools.bean.CustomOrderBean;
import ceRong.tools.bean.DorpDwonBean;
import ceRong.tools.bean.GoodsCheckBean;
import ceRong.tools.bean.taobaoBean;
import ceRong.tools.servlet.FileDataService;
import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;
import com.cbt.parse.service.DownloadMain;
import com.cbt.processes.servlet.Currency;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.Naming;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String[] RMI_PATHS = {"rmi://192.168.1.27:6600/FileDataService"};
	/**
	 * 画面初期化
	 */
	public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		IPictureComparisonService ipcs = new PictureComparisonServiceImpl();
    	
		//大类索引取得
		List<DorpDwonBean> largeIndexList = ipcs.getLargeIndexInfo();
		request.setAttribute("largeIndexList", largeIndexList);
		
		try {
			request.getRequestDispatcher("/website/imgUpload.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

	
	
	/**
	 * 方法描述:上传图片
	 */
	public void upLoadImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String dirFileName = "";
		String fileName ="";
		String path = "";
		boolean flag = true;
		
		// 为解析类提供配置信息
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 创建解析类的实例
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// 开始解析
		sfu.setFileSizeMax(2*1024*1024);
		// 每个表单域中数据会封装到一个对应的FileItem对象上
		try {
			List<FileItem> items = sfu.parseRequest(request);
			// 区分表单域
			for (int i = 0; i < items.size(); i++) {
				FileItem item = items.get(i);
				// isFormField为true，表示这不是文件上传表单域
				if (!item.isFormField()) {

					// 获得存放文件的物理路径
					// upload下的某个文件夹 得到当前在线的用户 找到对应的文件夹
					ServletContext sctx = request.getSession()
							.getServletContext();
					path = sctx.getRealPath("/searchupload");
//					String path = sctx.getRealPath(request.getRequestURI());
					System.out.println(path);
					// 获得文件名
				/*	String fileName = item.getName();
					System.out.println(fileName);*/
					
					fileName = UUID.randomUUID().toString().replaceAll("-", "");
		             
					if (item.getSize()>2*1024*1024 || item.getSize()<5*1024){
						request.setAttribute("errorMessage", "请上传5k以上2M以下的图片");
						flag = false;
					}
					//记录上传者ip
					String ip = request.getServerName();
					
					
					
					// 该方法在某些平台(操作系统),会返回路径+文件名
					fileName = fileName
							.substring(fileName.lastIndexOf("/") + 1);
					File file = new File(path + "\\" + fileName+".jpg");
					
					if (!file.exists()) {
						if(flag){
							item.write(file);
						}
						
						BufferedImage bufferedImage = ImageIO.read(new File(path + "\\" + fileName+".jpg"));   
						int width = bufferedImage.getWidth();   
						int height = bufferedImage.getHeight();  
						if(width<200 || height <200){
							request.setAttribute("errorMessage", "图片尺寸不能小于200*200");
						}
						// 将上传图片的名字记录到数据库中
						// response.sendRedirect("/searchupload/ok.html");
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		dirFileName = "\\" + fileName+".jpg";
		
		IPictureComparisonService ipcs = new PictureComparisonServiceImpl();
		//大类索引取得
  		List<DorpDwonBean> largeIndexList = ipcs.getLargeIndexInfo();
  		request.setAttribute("largeIndexList", largeIndexList);
		dirFileName = dirFileName.replace("\\", "/");       
        request.setAttribute("uploadFileName", dirFileName);
		try {
			request.getRequestDispatcher("/website/imgUpload.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 方法描述:上传图片搜索
	 */
	public void findImg(HttpServletRequest request, HttpServletResponse response) {
		
		//上传图片名
		String dirFileName = request.getParameter("uploadImg");
		//大类索引
		final String maxSelectId = request.getParameter("selectId");
		// upload下的某个文件夹 得到当前在线的用户 找到对应的文件夹
		ServletContext sctx = request.getSession()
				.getServletContext();
		String searchImgPath = sctx.getRealPath("/searchupload");
		
		//上传的搜索图片路径
		final String path = searchImgPath+dirFileName;
        
        //rmi总的返回list
		final List<CustomOrderBean> picIdListAll = new ArrayList<CustomOrderBean>();
		//1688准备数据
		List<GoodsCheckBean> cgbList= new ArrayList<GoodsCheckBean>();
        SimpleDateFormat dfw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		System.out.println("1688 time start ="+dfw.format(new Date()));// new Date()为获取当前系统时间
		//大类索引搜索
		if(!"".equals(maxSelectId)){
			for(int j=0;j<RMI_PATHS.length;j++){
				
				final int taskJ = j;
				System.out.println("rmi path ="+taskJ);
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				cachedThreadPool.execute(new Runnable() {
	    			
					@Override
					public void run() {
						System.out.println(Thread.currentThread().getName());
						
						try {
							final List<byte[]> imgByte = new FileClient().fileToByte(path);
							FileDataService fileDataService;
							fileDataService = (FileDataService) Naming.lookup(RMI_PATHS[taskJ]);
							List<CustomOrderBean> picIdList = new ArrayList<CustomOrderBean>();
							picIdList = fileDataService.upload("D:/home/hadoop/pic/" + System.currentTimeMillis() + ".jpg", imgByte,
									"","",0,"",maxSelectId, picIdList);
							picIdListAll.addAll(picIdList);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return;
						}
					}
	    		});
			}
		}
		
		cachedThreadPool.shutdown();
    	
    	while(true) {
    		if (cachedThreadPool.isTerminated()) {
    			System.out.println("1688 time end ="+dfw.format(new Date()));// new Date()为获取当前系统时间
    			System.out.println("处理完成");
    			
    			break;
    		}
//    		try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    	}
    	
    	IPictureComparisonService ipcs = new PictureComparisonServiceImpl();
    	
		//1688准备数据
        cgbList= ipcs.getDownLoadCsvInfo(picIdListAll);
        //test
    	System.out.println("downcsv查询结果数："+cgbList.size());
        List<Comparator<GoodsCheckBean>> mCmpList = new ArrayList<Comparator<GoodsCheckBean>>();
        mCmpList.add(comparePointASC); 
        mCmpList.add(comparePriceASC);
        mCmpList.add(compareIdASC);
        sort(cgbList, mCmpList);  
        //去掉重复的产品数据
        cgbList = distinctByOfferid(cgbList);
        //test
    	System.out.println("去掉重复的产品结果："+cgbList.size());
        //移除4条以上的数据
        cgbList = removeByOfferid(cgbList,20);
        
		double exchange_rate = 1;
		DecimalFormat df=new DecimalFormat("#0.##");
		//获取汇率
		Map<String, Double> maphl = Currency.getMaphl(request);
		exchange_rate = maphl.get("USD");
		exchange_rate = exchange_rate/maphl.get("RMB");
		for(int i=0;i<cgbList.size();i++){
			 cgbList.get(i).setNewPrice(Double.parseDouble(df.format(cgbList.get(i).getNewPrice()*exchange_rate)));
			 cgbList.get(i).setNewName(this.translate(cgbList.get(i).getNewName()));
			 
		}
		
		//抓取淘宝数据
        String strUrl = "http://" + request.getServerName() //服务器地址  
		        + ":" + request.getServerPort()   //端口号  
		        + request.getContextPath();    //项目名称 
        System.out.println("taobao time start ="+dfw.format(new Date()));// new Date()为获取当前系统时间
        taobaoBean tbb = this.getTaoBaoName(path, strUrl);
        System.out.println("taobao time end ="+dfw.format(new Date()));// new Date()为获取当前系统时间
        //淘宝数据
      	GoodsCheckBean gcb = new GoodsCheckBean();
        if(null!=tbb.getPic_url()){
    		//图片地址1
    		gcb.setTbImg(tbb.getPic_url());
    		//商品地址1
    		gcb.setTbUrl(tbb.getDetail_url());
    		//商品价格1
    		gcb.setTbprice(df.format(Double.parseDouble(tbb.getView_price())*exchange_rate));
    		//商品名字1
    		gcb.setTbName(this.translate(tbb.getTitle()));
    		//图片地址2
    		gcb.setTbImg1(tbb.getPic_url1());
    		//商品地址2
    		gcb.setTbUrl1(tbb.getDetail_url1());
    		//商品价格2
    		gcb.setTbprice1(df.format(Double.parseDouble(tbb.getView_price1())*exchange_rate));
    		//商品名字2
    		gcb.setTbName1(this.translate(tbb.getTitle1()));
    		//图片地址3
    		gcb.setTbImg2(tbb.getPic_url2());
    		//商品地址3
    		gcb.setTbUrl2(tbb.getDetail_url2());
    		//商品价格3
    		gcb.setTbprice2(df.format(Double.parseDouble(tbb.getView_price2())*exchange_rate));
    		//商品名字3
    		gcb.setTbName2(this.translate(tbb.getTitle2()));
    		//图片地址4
    		gcb.setTbImg3(tbb.getPic_url3());
    		//商品地址4
    		gcb.setTbUrl3(tbb.getDetail_url3());
    		//商品价格4
    		gcb.setTbprice3(df.format(Double.parseDouble(tbb.getView_price3())*exchange_rate));
    		//商品名字4
    		gcb.setTbName3(this.translate(tbb.getTitle3()));
        }
        
      	
        //大类索引取得
  		List<DorpDwonBean> largeIndexList = ipcs.getLargeIndexInfo();
  		request.setAttribute("largeIndexList", largeIndexList);
        dirFileName = dirFileName.replace("\\", "/");       
        request.setAttribute("uploadFileName", dirFileName);
        //1688结果数据
		request.setAttribute("gbbs", cgbList);
		//淘宝结果数据
		request.setAttribute("gcb", gcb);
		request.setAttribute("maxSelectId", maxSelectId);
		
		try {
			request.getRequestDispatcher("/website/imgUpload.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	//抓取淘宝数据
	public taobaoBean getTaoBaoName(String filePath,String str_url) {
		taobaoBean tbb = new taobaoBean();
		final WebClient webClient=new WebClient(BrowserVersion.CHROME);
		HtmlPage page = null;
		try {
			page = webClient.getPage(str_url+"/website/imgUpload.jsp");
			
			if(page == null) {
				System.out.println("Connection Refused!");
			} else {
				HtmlForm form = (HtmlForm)page.getElementById("idform");
				HtmlSubmitInput button = form.getInputByName("subsearch");
				HtmlFileInput textField = form.getInputByName("imgfile");
				textField.setContentType("image/jpeg");
				textField.setValueAttribute(filePath);
				HtmlPage hpage = button.click();
				String pageText = hpage.asText();
				JSONObject jsonObject = JSONObject.fromObject(pageText);
				int status= jsonObject.getInt("status");
				if(status==0) { //上传失败
					System.out.println("------上传失败！");
				} else { //上传成功
					String img_name= jsonObject.getString("name");
					tbb = this.getLikeDetails(img_name);
				}
			}
		} catch (Exception e) {
			System.out.println("------获取淘宝name失败！");
		}
		return tbb;
	}
	
	//获取相似产品信息
	public taobaoBean getLikeDetails(String name) {
		taobaoBean tbb = new taobaoBean();
		URL url = null;
		HttpURLConnection conn = null;
		try {
			url = new URL("https://s.taobao.com/search?q=&commend=all&ssid=s5-e&search_type=item"
					+ "&sourceId=tb.index&spm=a21bo.7724922.8452-taobao-item.2&ie=utf8&initiativ"
					+ "e_id=tbindexz_20151226&tfsid="+name+"&app=imgsearch");
			conn = (HttpURLConnection) url.openConnection();
			if(conn == null) {
				return tbb;
			}
			InputStream is = null;
			is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			InputStreamReader isr = null;
			String line = null;
			isr = new InputStreamReader(bis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("g_page_config =")) {
					JSONObject jsonObject = JSONObject.fromObject(line.substring(20,line.length()-1));
					JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.getString("mods"));
					JSONObject jsonObject2 = JSONObject.fromObject(jsonObject1.getString("itemlist"));
					JSONObject jsonObject3 = JSONObject.fromObject(jsonObject2.getString("data"));
					String collections = (jsonObject3.getString("collections")).substring(1,(jsonObject3.getString("collections")).length()-1);
					String auctions= (JSONObject.fromObject(collections)).getString("auctions");
					JSONArray jsonArray = new JSONArray();
					jsonArray = JSONArray.fromObject(auctions);
					Object[] strs = jsonArray.toArray();
					for(int i=0;i<4;i++) {
						JSONObject jsonArra = JSONObject.fromObject(strs[i]);
						if(i==0) {
							tbb.setPic_url(jsonArra.getString("pic_url"));//图片地址
							tbb.setDetail_url("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//商品地址
							tbb.setView_price(jsonArra.getString("view_price"));//商品价格
							tbb.setTitle(jsonArra.getString("title"));//商品价格
						}
						if(i==1) {
							tbb.setPic_url1(jsonArra.getString("pic_url"));//图片地址
							tbb.setDetail_url1("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//商品地址
							tbb.setView_price1(jsonArra.getString("view_price"));//商品价格
							tbb.setTitle1(jsonArra.getString("title"));//商品价格
						}
						if(i==2) {
							tbb.setPic_url2(jsonArra.getString("pic_url"));//图片地址
							tbb.setDetail_url2("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//商品地址
							tbb.setView_price2(jsonArra.getString("view_price"));//商品价格
							tbb.setTitle2(jsonArra.getString("title"));//商品价格
						}
						if(i==3) {
							tbb.setPic_url3(jsonArra.getString("pic_url"));//图片地址
							tbb.setDetail_url3("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//商品地址
							tbb.setView_price3(jsonArra.getString("view_price"));//商品价格
							tbb.setTitle3(jsonArra.getString("title"));//商品价格
						}
//						System.out.println("图片"+(i+1)+"地址："+jsonArra.getString("pic_url"));
//						System.out.println("商品"+(i+1)+"地址："+jsonArra.getString("detail_url"));
//						System.out.println("图片"+(i+1)+"价格："+jsonArra.getString("view_price"));
//						System.out.println("商品"+(i+1)+"名称："+jsonArra.getString("title"));
					}
				}
			}
			br.close();
			isr.close();
			bis.close();
			is.close();
		} catch (Exception e) {
			System.out.println("------淘宝类似产品此图抓取失败！name="+name);
		}
		conn.disconnect();
		return tbb;
	}
	
	/**
	 * 方法描述:相似图片搜索
	 */
//	public void findSimilarityImg(HttpServletRequest request, HttpServletResponse response) {
//
//		//被选择图片
//		String searchImg = request.getParameter("searchImg");
//		//去掉开始\
//		searchImg = searchImg.replace("/", "\\").substring(1);
//		// upload下的某个文件夹 得到当前在线的用户 找到对应的文件夹
//		ServletContext sctx = request.getSession()
//				.getServletContext();
//		String searchImgPath = sctx.getRealPath("/");
//
//		//上传的搜索图片路径
//        String path = searchImgPath+searchImg;
//        //imgIndex路径
//        String imgIndexPath = sctx.getRealPath("/clothingIndex");
//        List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
//                try {
////                    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("E:\\lyl\\eclipseworkspace\\lireDemo\\clothingIndex")));
//                    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(imgIndexPath)));
//                    int numDocs = reader.numDocs();
//                    System.out.println("numDocs = " + numDocs);
//                    int numResults = 50;
//                    try {
//                    	//图片显示结果数目
//                        numResults = 10;
//                    } catch (Exception e) {
//                        // nothing to do ...
//                    }
//
//                    ImageSearcher searcher = ImageSearcherFactory.createColorLayoutImageSearcher(numResults);
//                    searcher = ImageSearcherFactory.createCEDDImageSearcher(numResults);
//
//                    ImageSearchHits hits = searcher.search(ImageIO.read(new FileInputStream(path)), reader);
//
//                    for (int i = 0; i < hits.length(); i++) {
//                        try {
//                        	DecimalFormat df=new DecimalFormat("#0.##");
//                        	//分数
//                        	float fraction=hits.score(i);
//                        	String strScore = df.format(100-fraction)+"%";
//                        	int Similarity = (int)(100-fraction+0.5);
//                            String fileIdentifier = hits.doc(i).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
//
//                            CustomOrderBean gbb = new CustomOrderBean();
//                            fileIdentifier = fileIdentifier.replace("F:", "").replace("\\", "/");
//
//            				gbb.setImg(fileIdentifier);
////            				gbb.setScore(Similarity);
//            				gbbList.add(gbb);
//
//                        } catch (Exception ex) {
//                            Logger.getLogger("global").log(Level.SEVERE, null, ex);
//                        }
//                    }
//
//                    reader.close();
//                } catch (Exception e) {
//                    // Nothing to do here ....
//                } finally {
//                }
//        request.setAttribute("gbbs", gbbList);
//
//		try {
//			request.getRequestDispatcher("/website/imgUpload.jsp").forward(request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
	//去掉重复产品数据
	private List<GoodsCheckBean> distinctByOfferid(List<GoodsCheckBean> cgbList){
		
		for(int i=0; i<cgbList.size(); i++){
			for(int j=cgbList.size()-1; j>i; j--){
				if(cgbList.get(i).getOfferId()==cgbList.get(j).getOfferId()){
					cgbList.remove(cgbList.get(j));
				}
			}
		}
		return cgbList;
	}
	
	//去掉指定num以上的数据
	private List<GoodsCheckBean> removeByOfferid(List<GoodsCheckBean> cgbList,int num){
		
		for(int i=0; i<cgbList.size(); i++){
			if(i>=num){
				cgbList.remove(i);
				i--;
			}
		}
		System.out.println("---"+cgbList.size());
		return cgbList;
	}
	
    //排序
	public void sort(List<GoodsCheckBean> list, final List<Comparator<GoodsCheckBean>> comList) {  
        if (comList == null)  
            return;  
        Comparator<GoodsCheckBean> cmp = new Comparator<GoodsCheckBean>() {  
            @Override  
            public int compare(GoodsCheckBean o1, GoodsCheckBean o2) {  
                for (Comparator<GoodsCheckBean> comparator : comList) {  
                    if (comparator.compare(o1, o2) > 0) {  
                        return 1;  
                    } else if (comparator.compare(o1, o2) < 0) {  
                        return -1;  
                    }  
                }  
                return 0;  
            }  
        };  
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 
        Collections.sort(list, cmp);  
    }  
	
	//id升序
	private Comparator<GoodsCheckBean> compareIdASC = new Comparator<GoodsCheckBean>() {  
		  
        @Override  
        public int compare(GoodsCheckBean o1, GoodsCheckBean o2) {  
            
            if(o1.getId() > o2.getId()){
        		return 1;
        	}else if(o1.getId() == o2.getId()){
        		return 0;
        	}else{
        		return -1;
        	}
            
        }  
    }; 
    
	//销量升序
	private Comparator<GoodsCheckBean> comparePriceASC = new Comparator<GoodsCheckBean>() {  
		  
        @Override  
        public int compare(GoodsCheckBean o1, GoodsCheckBean o2) {  
            
            if(o1.getNewPrice().compareTo(o2.getNewPrice()) >0 ){
        		return 1;
        	}else if(o1.getNewPrice().compareTo(o2.getNewPrice()) ==0){
        		return 0;
        	}else{
        		return -1;
        	}
            
        }  
    }; 
    //相似值升序
    private Comparator<GoodsCheckBean> comparePointASC = new Comparator<GoodsCheckBean>() {  
    	  
        @Override  
        public int compare(GoodsCheckBean o1, GoodsCheckBean o2) { 
        	
        	if(o1.getScore().compareTo(o2.getScore()) > 0){
        		return 1;
        	}else if(o1.getScore().compareTo(o2.getScore()) == 0){
        		return 0;
        	}else{
        		return -1;
        	}
        }  
    }; 
	    
    private String translate(String cnName){
    	
    	return DownloadMain.getContentClient("http://192.168.1.210:8090/translation/gettrans?catid=0&cntext="+cnName,null);
    }
	
}
