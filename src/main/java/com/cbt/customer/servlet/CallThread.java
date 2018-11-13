package com.cbt.customer.servlet;

import com.cbt.bean.CustomOrderBean;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CallThread {
    @SuppressWarnings("unchecked")
//    public static void main(String[] args) throws ExecutionException,
//            InterruptedException {
    public static void main(String[] args) throws Exception {
//    	CallThread test = new CallThread();
// 
//        // 创建一个线程池
//        ExecutorService pool = Executors.newFixedThreadPool(2);
//        // 创建两个有返回值的任务
//        Callable c1 = test.new MyCallable("A");
//        Callable c2 = test.new MyCallable("B");
// 
//        // 执行任务并获取Future对象
//        Future f1 = pool.submit(c1);
//        Future f2 = pool.submit(c2);
// 
//        // 从Future对象上获取任务的返回值，并输出到控制台
//        //System.out.println(">>>" + f1.get().toString());
//       // System.out.println(">>>" + f2.get().toString());
//        System.out.println("任务fanh>>>" + f2.get().toString()+f1.get().toString());
//        // 关闭线程池
//        pool.shutdown();
//    	CallThread test = new CallThread();
//    	IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex1")));
//		IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex2")));
//		IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex3")));
//		IndexReader reader4 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex4")));
//        // 创建一个线程池
//        ExecutorService pool = Executors.newFixedThreadPool(4);
//       // 创建两个有返回值的任务
//       Callable c1 = test.new MyCallable("D:\\clothingIndex1","E:\\myproject\\localPhotos\\1.jpg",reader1);
//        Callable c2 = test.new MyCallable("D:\\clothingIndex2","E:\\myproject\\localPhotos\\1.jpg",reader2);
//        Callable c3 = test.new MyCallable("D:\\clothingIndex3","E:\\myproject\\localPhotos\\1.jpg",reader3);
//        Callable c4 = test.new MyCallable("D:\\clothingIndex4","E:\\myproject\\localPhotos\\1.jpg",reader4);
//
//        // 执行任务并获取Future对象
//       Future f1 = pool.submit(c1);
//        Future f2 = pool.submit(c2);
//       Future f3 = pool.submit(c3);
//        Future f4 = pool.submit(c4);
//
//       // System.out.println(">>>" + f1.get().toString());
//
//
//        List<CustomOrderBean> ls4=(List<CustomOrderBean>)f4.get();
//        List<CustomOrderBean> ls3=(List<CustomOrderBean>)f3.get();
//        List<CustomOrderBean> ls2=(List<CustomOrderBean>)f2.get();
//        List<CustomOrderBean> ls1=(List<CustomOrderBean>)f1.get();
//
//        ls1.addAll(ls2);
//        ls1.addAll(ls3);
//        ls1.addAll(ls4);
//        System.out.println("返回列表大小f4："+ls4.size());
//        System.out.println("返回列表大小f3："+ls3.size());
//        System.out.println("返回列表大小f2："+ls2.size());
//        System.out.println("返回总列表大小："+ls1.size());
//        pool.shutdown();
    }
 
    @SuppressWarnings("unchecked")
    class MyCallable implements Callable {
    	
//        private String name;
// 
//        MyCallable(String name) {
//            this.name = name;
//        }
 
    	private String indexPath;
    	private String imgPath;
    	private IndexReader reader;
    	
        MyCallable(String indexPath,String imgPath,IndexReader reader) {
            this.indexPath = indexPath;
            this.imgPath = imgPath;
            this.reader = reader;
        }
        
        
        public Object call() throws Exception {
//            IndexReader reader;
            List<CustomOrderBean> picIdList = new ArrayList<CustomOrderBean>();
//            String imgPath="D:\\clothingIndex";
    		try {
    			SimpleDateFormat dfw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//    			reader = DirectoryReader.open(FSDirectory.open(new File(syncTableInfo.getImgPath())));
//    			for(int j=1;j<5;j++){
//    				reader = DirectoryReader.open(FSDirectory.open(new File(this.indexPath)));
//    				System.out.println("Thread start="+syncTableInfo.getImgPath());// index
//    				System.out.println("Thread start="+imgPath+j);// index
    				System.out.println("time start="+dfw.format(new Date()));// new Date()为获取当前系统时间
//    		        int numDocs = reader.numDocs();
//    		        System.out.println("numDocs = " + numDocs);
//    		        ImageSearcher searcher = getSearcher();
    		        int numResults = 50;
    		        try {
    		        	//图片显示结果数目
    		            numResults = 8;
    		        } catch (Exception e) {
    		            // nothing to do ...
    		        }
    		        
    		        ImageSearcher searcher = ImageSearcherFactory.createColorLayoutImageSearcher(numResults);
    		        searcher = ImageSearcherFactory.createCEDDImageSearcher(numResults);
    		        
//    		        ImageSearchHits hits = searcher.search(ImageIO.read(new FileInputStream(path)), reader);
    		        ImageSearchHits hits = searcher.search(ImageIO.read(new FileInputStream(this.imgPath)), this.reader);
//    		        System.out.println("Thread end="+syncTableInfo.getImgPath());// index
//    		        System.out.println("Thread end="+imgPath+j);// index
    		        System.out.println("time end="+dfw.format(new Date()));// new Date()为获取当前系统时间
    	            for (int i = 0; i < hits.length(); i++) {
    	                try {
    	                	DecimalFormat df=new DecimalFormat("#0.##");
    	                	//分数
    	                	float fraction=hits.score(i);
//    	                	String strScore = df.format(100-fraction)+"%";
    	                	int Similarity = (int)(100-fraction+0.5);
    	                    String fileIdentifier = hits.doc(i).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
    	        
    	                    int pos = fileIdentifier.lastIndexOf("\\");
//    	                    String strFilePath = fileIdentifier.substring(10,pos);
    	                    String strFilePath = fileIdentifier.substring(10, fileIdentifier.length());
    	                    fileIdentifier = fileIdentifier.substring(pos+1,fileIdentifier.length()-4);
    	                    
    	                    strFilePath = strFilePath.replace("\\", "/");
    	                    CustomOrderBean gbb = new CustomOrderBean();
    	                    gbb.setPurl(strFilePath);
    	                    gbb.setImg(fileIdentifier);
//    	                    gbb.setScore(Similarity);
    	                    picIdList.add(gbb);
    	    				
//    	                    System.out.println("picIdList = " + gbb.getImg());
    	                } catch (Exception ex) {
    	                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
    	                }
    	            }
//    	            reader.close();
//    			}
    			
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		
    		return picIdList;
        }
    }
}