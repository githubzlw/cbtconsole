package com.cbt.email.servlet;

import com.cbt.email.util.PathUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;


public class ServletDownload extends HttpServlet {
    private static final long serialVersionUID = 1L;  
         
    public void service(HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub   
          
        //获得请求文件名   
        String filename = request.getParameter("filename");  
        try {
			filename = new String(filename.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        System.out.println(filename);  
          
        //设置文件MIME类型   
        response.setContentType(getServletContext().getMimeType(filename));  
        //设置Content-Disposition   
        response.setHeader("Content-Disposition", "attachment;filename="+filename);  
        //读取目标文件，通过response将目标文件写到客户端   
        //获取目标文件的绝对路径   
        //String fullFileName = getServletContext().getRealPath("/accessories/" + filename);  
        String fullFileName = PathUtil.SENDFile+File.separator + filename; 
        //System.out.println(fullFileName);   
        InputStream in = null;  
        OutputStream out = null;
        //写文件   
        int b;  
        try {
	    	//读取文件   
	        in = new FileInputStream(fullFileName);  
	        out = response.getOutputStream();  
	        while((b=in.read())!= -1)  
	        {  
	            out.write(b);  
	        }  
        }catch(Exception e) {
        	e.printStackTrace();
        }finally {
        	try {
        		if(in != null) {
        			in.close();
        		}
        		if(out != null) {
        			out.close(); 
        		}
			} catch (IOException e) {
				e.printStackTrace();
			} 
   	        
        }
    }  

    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }
    
    
    
    
    
    
}