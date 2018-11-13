package com.cbt.parse.driver;


import org.slf4j.LoggerFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**FTP上传文件
 * @author abc
 *
 */
public class FtpDriver {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(FtpDriver.class);
	 /**  
     *   
     * @param path 上传到ftp服务器哪个路径下     
     * @param addr 地址  
     * @param port 端口号  
     * @param username 用户名  
     * @param password 密码  
     * @return  
     * @throws Exception  
     */    
    public static FTPClient connect(String path,String addr,int port,String username,String password){      
        int reply; 
        LOG.warn("addr:"+addr);
        LOG.warn("port:"+port);
        FTPClient ftp =new FTPClient();
        try {
			ftp.connect(addr,port);
			ftp.login(username,password);      
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);      
			reply = ftp.getReplyCode(); 
			LOG.warn("connect reply:"+reply);
			if (!FTPReply.isPositiveCompletion(reply)) {      
				ftp.disconnect(); 
				ftp = null;
				return ftp;      
			}else{
				ftp.changeWorkingDirectory(path);      
			}      
		} catch (IOException e) {
			ftp = null;
			LOG.warn("",e);
		}      
        return ftp;      
    } 
    
    
    /**关闭ftp链接
     * @param ftp
     */
    public static void returnConnection(FTPClient ftp){
    	if(ftp!=null){
    		try {
				ftp.disconnect();
			} catch (IOException e) {
				LOG.warn("",e);
			}
    	}
    }
    
    /**  
     *   
     * @param file 上传的文件或文件夹  
     * @throws Exception  
     */    
    public static int upload(File file,FTPClient ftp) throws Exception{  
    	int  result = 0;
    	if(ftp!=null){
    		if(file.isDirectory()){
    			//文件夹上传
    			ftp.makeDirectory(file.getName());                
    			ftp.changeWorkingDirectory(file.getName());      
    			String[] files = file.list();
    			File file1 = null;
    			for (int i = 0; i < files.length; i++) {      
    				file1 = new File(file.getPath()+"\\"+files[i] );      
    				if(file1.isDirectory()){      
    					result = upload(file1,ftp);      
    					ftp.changeToParentDirectory();      
    				}else{                    
    					file1 = new File(file.getPath()+"\\"+files[i]);      
    					FileInputStream input = new FileInputStream(file1); 
    					if(ftp.storeFile(file1.getName(), input)){
    						result = 1;
    						file1.delete();
    					}
    					input.close();                            
    				} 
    				file1 = null;
    			} 
    		}else{  
    			//文件上传
    			File file2 = new File(file.getPath());      
    			FileInputStream input = new FileInputStream(file2); 
    			boolean storeFile = ftp.storeFile(file2.getName(), input);
    			if(storeFile){
    				result = 1;
    				file2.delete();
    			}
    			input.close(); 
    		} 
    	}
        return result;
    }   
    
    public static void main(String[] args) throws Exception {
    	FTPClient ftp = FtpDriver.connect("", "ftp.china-clothing-wholesale.com", 21, "import@china-clothing-wholesale.com", "import416");
        upload(new File("C:/Users/abc/Desktop/img_china_alibaba_img_ibank_2014_106_698_1572896601_167640634.310x310.jpg"), ftp);
         returnConnection(ftp);
    }
    
}
