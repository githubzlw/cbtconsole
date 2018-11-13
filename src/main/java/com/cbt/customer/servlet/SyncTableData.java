package com.cbt.customer.servlet;

import com.cbt.bean.CustomOrderBean;
import com.cbt.bean.GoodsFarBean;
import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;
import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**   
 * @Title: SyncTableData.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年3月29日
 * @version V1.0   
 */
public class SyncTableData implements Runnable{
	
//	//停止线程的标记值boolean;
//	private boolean flag = true;
//	
//	public void stopThread(){
//		flag = false;
//	}
	
    public static void main(String[] args) throws Exception {    	


		
//    	String path = "E:\\myproject\\localPhotos\\";
//    	String path = "G:";
//    	String path1 = "G:\\sjDownload00\\cache";
//    	String path2 = "G:\\sjDownload56\\cache";
//    	String path3 = "G:\\sjDownload66\\cache";
//    	String path4 = "G:\\sjDownload124\\cache";
//    	String path5 = "G:\\sjDownload188\\cache";
//    	String path6 = "G:\\sjDownload247\\cache";
//		IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex1")));
//		IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex2")));
//		IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex3")));
//		IndexReader reader4 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex4")));
//		List<IndexReader>  lisrd= new  ArrayList<IndexReader>();
//		lisrd.add(reader1);
//		lisrd.add(reader2);
//		lisrd.add(reader3);
//		lisrd.add(reader4);
//
//    	IPictureComparisonService ips = new PictureComparisonServiceImpl();
//
//		List<GoodsFarBean> beanList = ips.getImgFile();
//
//		if(beanList!=null){
//			for(int i=0;i<beanList.size();i++){
//				syncTableInfo csn = new syncTableInfo();
//				//搜索图片路径
//				String strImgPath = beanList.get(i).getImgPath();
//				strImgPath = strImgPath.replace("/", "\\");
////				csn.setUploadImgPath(path+strImgPath);
//
//				if(isExist(path1+strImgPath)){
//					csn.setUploadImgPath(path1+strImgPath);
//				}else if(isExist(path2+strImgPath)){
//					csn.setUploadImgPath(path2+strImgPath);
//				}else if(isExist(path3+strImgPath)){
//					csn.setUploadImgPath(path3+strImgPath);
//				}else if(isExist(path4+strImgPath)){
//					csn.setUploadImgPath(path4+strImgPath);
//				}else if(isExist(path5+strImgPath)){
//					csn.setUploadImgPath(path5+strImgPath);
//				}else if(isExist(path6+strImgPath)){
//					csn.setUploadImgPath(path6+strImgPath);
//				}
//				//原图片路径
//				csn.setSourceImg(beanList.get(i).getImgPath());
//				//原url
//				csn.setSourceUrl(beanList.get(i).getUrl());
//				//类别id
//				csn.setCatId(beanList.get(i).getCatId());
//				//类别名
//				csn.setCatName(beanList.get(i).getCatName());
//
//				//循环4个index
//				for(int j=0;j<lisrd.size();j++){
//					SyncTableData data = new SyncTableData(csn,lisrd.get(j));
//	    			Thread t1 = new Thread(data);
//	    			t1.start();
//	     		}
//			}
//			//更新抓取标志
//	        ips.updateImgFileUpload(beanList);
//		}
		
    }
    	
	public static boolean isExist(String path) {
		File file = new File(path);
		// 判断文件夹是否存在
		if (file.exists()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void run() {
		init();
	}

	/**
	 * 初始化arrayList,读取getinfotable初始数据
	 * @throws Exception
	 */
	public void init() {

	        List<CustomOrderBean> picIdList = new ArrayList<CustomOrderBean>();
			try {
				SimpleDateFormat dfw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				System.out.println("time start="+dfw.format(new Date()));// new Date()为获取当前系统时间
		        ImageSearcher searcher = ImageSearcherFactory.createColorLayoutImageSearcher(8);
		        searcher = ImageSearcherFactory.createCEDDImageSearcher(8);
		        ImageSearchHits hits = searcher.search(ImageIO.read(new FileInputStream(syncTableInfo.getUploadImgPath())), reader);
		        System.out.println("time end1="+dfw.format(new Date()));// new Date()为获取当前系统时间
	            for (int i = 0; i < hits.length(); i++) {
	                try {
//	                	DecimalFormat df=new DecimalFormat("#0.##");
	                	//分数
	                	float fraction=hits.score(i);
//	                	String strScore = df.format(100-fraction)+"%";
	                	int Similarity = (int)(100-fraction+0.5);
	                    String fileIdentifier = hits.doc(i).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
	        
	                    int pos = fileIdentifier.lastIndexOf("\\");
//		                    String strFilePath = fileIdentifier.substring(10,pos);
	                    String strFilePath = fileIdentifier.substring(10, fileIdentifier.length());
	                    fileIdentifier = fileIdentifier.substring(pos+1,fileIdentifier.length()-4);
	                    
	                    strFilePath = strFilePath.replace("\\", "/");
	                    CustomOrderBean gbb = new CustomOrderBean();
	                    //原图片路径
	                    gbb.setSourceImg(syncTableInfo.getSourceImg());
						//原链接
	                    gbb.setSourceUrl(syncTableInfo.getSourceUrl());
	                    //新图片路径
	                    gbb.setNewImg(strFilePath);
	                    //单一图片名
	                    gbb.setImgName(fileIdentifier);
	                    //相似值
//	                    gbb.setScore(Similarity);
						//类别Id
	                    gbb.setCatId(syncTableInfo.getCatId());
						//类别名
	                    gbb.setCatName(syncTableInfo.getCatName());
	                    picIdList.add(gbb);
	                } catch (Exception ex) {
	                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
	                }
	            }
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally
			{
			}
//		IPictureComparisonService ips = new PictureComparisonServiceImpl();
//		int count= ips.updateDownLoadInfo(picIdList);
			
			IPictureComparisonService ips = new PictureComparisonServiceImpl();
//			//准备数据
//	        List<GoodsCheckBean> cgbList= ips.getDownLoadCsvInfo(picIdList);
//	        //插入lire_search_data
//	        ips.insertLireSearchData(cgbList);

	}
	
	private syncTableInfo syncTableInfo;

	private IndexReader reader;
	public List<CustomOrderBean> picIdList;
	
	public syncTableInfo getSyncTableInfo() {
		return syncTableInfo;
	}

	public void setSyncTableInfo(syncTableInfo syncTableInfo) {
		this.syncTableInfo = syncTableInfo;
	}

	public SyncTableData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SyncTableData(syncTableInfo syncTableInfo) {
		super();
		this.syncTableInfo = syncTableInfo;
	}
	
	public SyncTableData(syncTableInfo syncTableInfo,IndexReader reader) {
		super();
		this.syncTableInfo = syncTableInfo;
		this.reader = reader;
	}
	
	
}
