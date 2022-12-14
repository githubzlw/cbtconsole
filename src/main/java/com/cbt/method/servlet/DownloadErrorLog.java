package com.cbt.method.servlet;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

public class DownloadErrorLog {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DownloadErrorLog.class);
	public static void main(String[] args) {
		///usr/local/tomcat7/logs/cbtprogram2016  "192.168.1.32", 22, "downloaderrlog", "root@123", "./cbtprogram2016"
		//new DownloadErrorLog().listFileNames("192.168.1.32", 22, "root", "root123", "/root/log");
//		new DownloadErrorLog().listFileNames("216.244.83.218", 22, "downloaderrlog", "root@123", "./cbtprogram2016/pay");
//		Calendar c = Calendar.getInstance();
//		String day = String.valueOf(c.get(Calendar.DATE)-1);
//		if(day.equals("1")){
//			day="01";
//		}
//		String month = String.valueOf(c.get(Calendar.MONDAY)+1);
//		//F:\console\tomcatConsoleBAK\webapps\cbtconsole\log
//		//File dir=new File("F:/cbtconsole/log");
//		File dir=new File("D://logs/");
//		JsonResult json = new JsonResult();
//		List<DownFileInfo> list=new ArrayList<DownFileInfo>();
//		TaoBaoInfoList Alllist=new TaoBaoInfoList();
//	    if(dir.exists()){
//		     File []files=dir.listFiles();
//		     System.out.println(day+"-"+month);
//		     for(int i=0;i<files.length;i++){
//		    	 System.out.println(files[i].getName());
//		    	 if("cbt.log".equals(files[i].getName()) || "pay.log".equals(files[i].getName()) || (files[i].getName().indexOf("-")>-1 && files[i].getName().split("-")[1].equals(month) && files[i].getName().split("-")[2].equals(day))){
//		    		 System.out.println("files[i].getName()=="+files[i].getName());
//		    		 DownFileInfo info=new DownFileInfo();
//			    	 info.setFileName(files[i].getName());
//			    	 list.add(info);
//		    	 }
//		     }
//	    }
	}
	
	
	public List<String> listFileNames(String host, int port, String username, final String password, String dir) {
        List<String> list = new ArrayList<String>();
        ChannelSftp sftp = null;
        Channel channel = null;
        Session sshSession = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            LOG.info("Session connected!");
            channel = sshSession.openChannel("sftp");
            channel.connect();
            LOG.info("Channel connected!");
            //F:\cbtconsole\log
            File extiFile = new File("F:\\cbtconsole\\log\\");
//          File extiFile = new File("D:\\logs\\");
            String [] fileList=extiFile.list();
            List<String> fileName=new ArrayList<String>();
            for(int i=0;i<fileList.length;i++){
            	if(!fileList[i].equals("cbt.log") && !fileList[i].equals("pay.log")){
            		fileName.add(fileList[i]);
            	}
            }
            sftp = (ChannelSftp) channel;
            Vector<?> vector = sftp.ls(dir);
            sftp.cd(dir+"/");
            LOG.info("??????????????????????????????:"+vector.size());
            for (Object item:vector) {
                LsEntry entry = (LsEntry) item;
                LOG.info("????????????????????????:"+entry.getFilename());
                if(Pattern.compile("(?i)[a-z]").matcher(entry.getFilename()).find() && !fileName.contains(entry.getFilename())){
                	long start=System.currentTimeMillis();
                	LOG.info("????????????:"+entry.getFilename());
        			File file = new File("F:\\cbtconsole\\log\\"+entry.getFilename());
//                	File file = new File("D:\\logs\\"+entry.getFilename());
        			try{
        				sftp.get(entry.getFilename(), new FileOutputStream(file));
        				LOG.info("????????????:"+entry.getFilename()+"\t ??????:"+(System.currentTimeMillis()-start)/1000+"???");
        			}catch(Exception e){
        				LOG.info("????????????:"+entry.getFilename()+"\t ???????????????????????????");
        			}
                }else{
                	LOG.info("??????:"+entry.getFilename()+"????????? \t ???????????????????????????");
                }
            }
            System.out.println("????????????");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeChannel(sftp);
            closeChannel(channel);
            closeSession(sshSession);
        }
        return list;
    }
	
	private static void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private static void closeSession(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }
}
