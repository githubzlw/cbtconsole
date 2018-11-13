package com.cbt.customer.servlet;

import ceRong.tools.bean.CustomOrderBean;
import ceRong.tools.servlet.FileDataService;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDataServiceImpl extends UnicastRemoteObject implements FileDataService {
	
	private static final long serialVersionUID = 6593133654984360437L;
	
	private List<syncTableInfo> synList = new ArrayList<syncTableInfo>();

	private static List<IndexReader>  lisrd= new  ArrayList<IndexReader>();
	
	static {
		try {
//    		IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex1")));
//    		IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex2")));
//    		IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex3")));
//    		IndexReader reader4 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex4")));
//
//    		lisrd.add(reader1);
//    		lisrd.add(reader2);
//    		lisrd.add(reader3);
//    		lisrd.add(reader4);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected FileDataServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public List<CustomOrderBean> upload(String filename, List<byte[]> fileContent,String sourceImg,String sourceUrl,int catId,String catName,String maxIndexName, List<CustomOrderBean> picIdList) throws RemoteException {
		File file = new File(filename);
		BufferedOutputStream os = null;
        try {
            if (!file.exists()) {
            	file.getParentFile().mkdirs();
            	file.createNewFile();
            }
            os = new BufferedOutputStream(new FileOutputStream(file));
            if (fileContent != null && fileContent.size() > 0) {
            	for (byte[] b : fileContent) {
            		os.write(b);
				}
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
        	if (os != null) {
        		try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
       }
		
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        
        try {
        	for (int i = 0; i < 4; i++) {
        		final int taskID = i;
        		cachedThreadPool.execute(new Runnable() {
        			
    				@Override
    				public void run() {
    					System.out.println(Thread.currentThread().getName());
    					
    					ImageSearcher searcher = ImageSearcherFactory.createColorLayoutImageSearcher(8);
    		            searcher = ImageSearcherFactory.createCEDDImageSearcher(8);
//						try {
//							ImageSearchHits hits = searcher.search(ImageIO.read(new FileInputStream(filename)), lisrd.get(taskID));
//							
//	    		            for (int z = 0; z < hits.length(); z++) {
//	    		            	syncTableInfo synInfo = new syncTableInfo();
//	    		            	//分数
//	    		            	float fraction=hits.score(z);
//	    		            	int Similarity = (int)(100-fraction+0.5);
//	    		                String fileIdentifier = hits.doc(z).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
//	    		                int pos = fileIdentifier.lastIndexOf("\\");
//	    		                String strFilePath = fileIdentifier.substring(10, fileIdentifier.length());
//	    		                fileIdentifier = fileIdentifier.substring(pos+1,fileIdentifier.length()-4);
//	    		                synInfo.setScore(fraction);
//	    		                synInfo.setFileIdentifier(fileIdentifier);
//	    		                synList.add(synInfo);
//	    		            }
//	    		            
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
    		            
    				}
    			});
        	}
        	cachedThreadPool.shutdown();
        	
        	while(true) {
        		if (cachedThreadPool.isTerminated()) {
        			return picIdList;
        		}
        		Thread.sleep(200);
        	}
            
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return picIdList;
        
	}

}
