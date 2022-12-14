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
	 * ???????????????
	 */
	public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		IPictureComparisonService ipcs = new PictureComparisonServiceImpl();
    	
		//??????????????????
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
	 * ????????????:????????????
	 */
	public void upLoadImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String dirFileName = "";
		String fileName ="";
		String path = "";
		boolean flag = true;
		
		// ??????????????????????????????
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// ????????????????????????
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// ????????????
		sfu.setFileSizeMax(2*1024*1024);
		// ???????????????????????????????????????????????????FileItem?????????
		try {
			List<FileItem> items = sfu.parseRequest(request);
			// ???????????????
			for (int i = 0; i < items.size(); i++) {
				FileItem item = items.get(i);
				// isFormField???true???????????????????????????????????????
				if (!item.isFormField()) {

					// ?????????????????????????????????
					// upload????????????????????? ??????????????????????????? ????????????????????????
					ServletContext sctx = request.getSession()
							.getServletContext();
					path = sctx.getRealPath("/searchupload");
//					String path = sctx.getRealPath(request.getRequestURI());
					System.out.println(path);
					// ???????????????
				/*	String fileName = item.getName();
					System.out.println(fileName);*/
					
					fileName = UUID.randomUUID().toString().replaceAll("-", "");
		             
					if (item.getSize()>2*1024*1024 || item.getSize()<5*1024){
						request.setAttribute("errorMessage", "?????????5k??????2M???????????????");
						flag = false;
					}
					//???????????????ip
					String ip = request.getServerName();
					
					
					
					// ????????????????????????(????????????),???????????????+?????????
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
							request.setAttribute("errorMessage", "????????????????????????200*200");
						}
						// ?????????????????????????????????????????????
						// response.sendRedirect("/searchupload/ok.html");
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		dirFileName = "\\" + fileName+".jpg";
		
		IPictureComparisonService ipcs = new PictureComparisonServiceImpl();
		//??????????????????
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
	 * ????????????:??????????????????
	 */
	public void findImg(HttpServletRequest request, HttpServletResponse response) {
		
		//???????????????
		String dirFileName = request.getParameter("uploadImg");
		//????????????
		final String maxSelectId = request.getParameter("selectId");
		// upload????????????????????? ??????????????????????????? ????????????????????????
		ServletContext sctx = request.getSession()
				.getServletContext();
		String searchImgPath = sctx.getRealPath("/searchupload");
		
		//???????????????????????????
		final String path = searchImgPath+dirFileName;
        
        //rmi????????????list
		final List<CustomOrderBean> picIdListAll = new ArrayList<CustomOrderBean>();
		//1688????????????
		List<GoodsCheckBean> cgbList= new ArrayList<GoodsCheckBean>();
        SimpleDateFormat dfw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		System.out.println("1688 time start ="+dfw.format(new Date()));// new Date()???????????????????????????
		//??????????????????
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
    			System.out.println("1688 time end ="+dfw.format(new Date()));// new Date()???????????????????????????
    			System.out.println("????????????");
    			
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
    	
		//1688????????????
        cgbList= ipcs.getDownLoadCsvInfo(picIdListAll);
        //test
    	System.out.println("downcsv??????????????????"+cgbList.size());
        List<Comparator<GoodsCheckBean>> mCmpList = new ArrayList<Comparator<GoodsCheckBean>>();
        mCmpList.add(comparePointASC); 
        mCmpList.add(comparePriceASC);
        mCmpList.add(compareIdASC);
        sort(cgbList, mCmpList);  
        //???????????????????????????
        cgbList = distinctByOfferid(cgbList);
        //test
    	System.out.println("??????????????????????????????"+cgbList.size());
        //??????4??????????????????
        cgbList = removeByOfferid(cgbList,20);
        
		double exchange_rate = 1;
		DecimalFormat df=new DecimalFormat("#0.##");
		//????????????
		Map<String, Double> maphl = Currency.getMaphl(request);
		exchange_rate = maphl.get("USD");
		exchange_rate = exchange_rate/maphl.get("RMB");
		for(int i=0;i<cgbList.size();i++){
			 cgbList.get(i).setNewPrice(Double.parseDouble(df.format(cgbList.get(i).getNewPrice()*exchange_rate)));
			 cgbList.get(i).setNewName(this.translate(cgbList.get(i).getNewName()));
			 
		}
		
		//??????????????????
        String strUrl = "http://" + request.getServerName() //???????????????  
		        + ":" + request.getServerPort()   //?????????  
		        + request.getContextPath();    //???????????? 
        System.out.println("taobao time start ="+dfw.format(new Date()));// new Date()???????????????????????????
        taobaoBean tbb = this.getTaoBaoName(path, strUrl);
        System.out.println("taobao time end ="+dfw.format(new Date()));// new Date()???????????????????????????
        //????????????
      	GoodsCheckBean gcb = new GoodsCheckBean();
        if(null!=tbb.getPic_url()){
    		//????????????1
    		gcb.setTbImg(tbb.getPic_url());
    		//????????????1
    		gcb.setTbUrl(tbb.getDetail_url());
    		//????????????1
    		gcb.setTbprice(df.format(Double.parseDouble(tbb.getView_price())*exchange_rate));
    		//????????????1
    		gcb.setTbName(this.translate(tbb.getTitle()));
    		//????????????2
    		gcb.setTbImg1(tbb.getPic_url1());
    		//????????????2
    		gcb.setTbUrl1(tbb.getDetail_url1());
    		//????????????2
    		gcb.setTbprice1(df.format(Double.parseDouble(tbb.getView_price1())*exchange_rate));
    		//????????????2
    		gcb.setTbName1(this.translate(tbb.getTitle1()));
    		//????????????3
    		gcb.setTbImg2(tbb.getPic_url2());
    		//????????????3
    		gcb.setTbUrl2(tbb.getDetail_url2());
    		//????????????3
    		gcb.setTbprice2(df.format(Double.parseDouble(tbb.getView_price2())*exchange_rate));
    		//????????????3
    		gcb.setTbName2(this.translate(tbb.getTitle2()));
    		//????????????4
    		gcb.setTbImg3(tbb.getPic_url3());
    		//????????????4
    		gcb.setTbUrl3(tbb.getDetail_url3());
    		//????????????4
    		gcb.setTbprice3(df.format(Double.parseDouble(tbb.getView_price3())*exchange_rate));
    		//????????????4
    		gcb.setTbName3(this.translate(tbb.getTitle3()));
        }
        
      	
        //??????????????????
  		List<DorpDwonBean> largeIndexList = ipcs.getLargeIndexInfo();
  		request.setAttribute("largeIndexList", largeIndexList);
        dirFileName = dirFileName.replace("\\", "/");       
        request.setAttribute("uploadFileName", dirFileName);
        //1688????????????
		request.setAttribute("gbbs", cgbList);
		//??????????????????
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
	
	//??????????????????
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
				if(status==0) { //????????????
					System.out.println("------???????????????");
				} else { //????????????
					String img_name= jsonObject.getString("name");
					tbb = this.getLikeDetails(img_name);
				}
			}
		} catch (Exception e) {
			System.out.println("------????????????name?????????");
		}
		return tbb;
	}
	
	//????????????????????????
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
							tbb.setPic_url(jsonArra.getString("pic_url"));//????????????
							tbb.setDetail_url("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//????????????
							tbb.setView_price(jsonArra.getString("view_price"));//????????????
							tbb.setTitle(jsonArra.getString("title"));//????????????
						}
						if(i==1) {
							tbb.setPic_url1(jsonArra.getString("pic_url"));//????????????
							tbb.setDetail_url1("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//????????????
							tbb.setView_price1(jsonArra.getString("view_price"));//????????????
							tbb.setTitle1(jsonArra.getString("title"));//????????????
						}
						if(i==2) {
							tbb.setPic_url2(jsonArra.getString("pic_url"));//????????????
							tbb.setDetail_url2("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//????????????
							tbb.setView_price2(jsonArra.getString("view_price"));//????????????
							tbb.setTitle2(jsonArra.getString("title"));//????????????
						}
						if(i==3) {
							tbb.setPic_url3(jsonArra.getString("pic_url"));//????????????
							tbb.setDetail_url3("http:"+(jsonArra.getString("detail_url")).substring(0, (jsonArra.getString("detail_url")).indexOf("&")));//????????????
							tbb.setView_price3(jsonArra.getString("view_price"));//????????????
							tbb.setTitle3(jsonArra.getString("title"));//????????????
						}
//						System.out.println("??????"+(i+1)+"?????????"+jsonArra.getString("pic_url"));
//						System.out.println("??????"+(i+1)+"?????????"+jsonArra.getString("detail_url"));
//						System.out.println("??????"+(i+1)+"?????????"+jsonArra.getString("view_price"));
//						System.out.println("??????"+(i+1)+"?????????"+jsonArra.getString("title"));
					}
				}
			}
			br.close();
			isr.close();
			bis.close();
			is.close();
		} catch (Exception e) {
			System.out.println("------???????????????????????????????????????name="+name);
		}
		conn.disconnect();
		return tbb;
	}
	
	/**
	 * ????????????:??????????????????
	 */
//	public void findSimilarityImg(HttpServletRequest request, HttpServletResponse response) {
//
//		//???????????????
//		String searchImg = request.getParameter("searchImg");
//		//????????????\
//		searchImg = searchImg.replace("/", "\\").substring(1);
//		// upload????????????????????? ??????????????????????????? ????????????????????????
//		ServletContext sctx = request.getSession()
//				.getServletContext();
//		String searchImgPath = sctx.getRealPath("/");
//
//		//???????????????????????????
//        String path = searchImgPath+searchImg;
//        //imgIndex??????
//        String imgIndexPath = sctx.getRealPath("/clothingIndex");
//        List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
//                try {
////                    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("E:\\lyl\\eclipseworkspace\\lireDemo\\clothingIndex")));
//                    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(imgIndexPath)));
//                    int numDocs = reader.numDocs();
//                    System.out.println("numDocs = " + numDocs);
//                    int numResults = 50;
//                    try {
//                    	//????????????????????????
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
//                        	//??????
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
	//????????????????????????
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
	
	//????????????num???????????????
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
	
    //??????
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
	
	//id??????
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
    
	//????????????
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
    //???????????????
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
