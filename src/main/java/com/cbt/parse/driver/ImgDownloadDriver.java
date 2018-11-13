package com.cbt.parse.driver;

import com.cbt.parse.bean.OneSixExpressBean;
import com.cbt.parse.dao.ImgFileDao;
import com.cbt.parse.dao.OneSixExpressDao;
import com.cbt.parse.daoimp.IOneSixExpressDao;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.TypeUtils;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImgDownloadDriver {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ImgDownloadDriver.class);
	
	public static String driver(int id1,int id2,int index) throws Exception{
		ImgFileDao  imgdao = new ImgFileDao();
//		String path = TypeUtils.path+"/1688-0108";
		String path = "F:/1688-0119/";
		IOneSixExpressDao dao = new OneSixExpressDao();
		ArrayList<OneSixExpressBean> queryImg = dao.queryImg(id1,id2);
		int img_size = queryImg.size();
		String img = null;
		String bean_img = null;
		String[] imgs = null;
		String imgs_p = null;
		String info = null;
		String imgpath = null;
		OneSixExpressBean bean = null;
		String url = null;
		int id;
		int total = 0;
		List<String> infos = null;
		for(int i=0;i<img_size;i++){
			LOG.warn("driver-----img------"+i);
			bean = queryImg.get(i);
			if(total>1000){
				index++;
				total = 0;
			}
			//搜索图片
			img = bean.getImg();
			url = bean.getUrl();
			id = bean.getId();
			if(img!=null&&!img.isEmpty()){
				imgpath = path+"/index"+index+"/"+getPath(img);
				if(ImgDownload.execute(img,imgpath )){
					total++;
					if(imgdao.queryExsis(img, imgpath,url)==0){
						imgdao.add(img, imgpath,url,id);
					}
				}
			}
			//图片集
			bean_img = bean.getImgs();
			if(img!=null&&!img.isEmpty()){
				imgs = bean_img.replace("[", "").replace("]", "").split(",\\s*");
				int imgs_length = imgs.length;
				for(int j=0;j<imgs_length;j++){
					if(total>1000){
						index++;
						total = 0;
					}
					imgs_p = imgs[j]+"310x310.jpg";
//					System.out.println("imgs_p:"+imgs_p);
					imgpath = path+"/index"+index+"/"+getPath(imgs_p);
//					System.out.println(i+"-"+j+"-bean_img:"+imgpath);
					if(ImgDownload.execute(imgs_p,imgpath )){
						total++;
						if(imgdao.queryExsis(imgs_p, imgpath,url)==0){
							imgdao.add(imgs_p, imgpath,url,id);
						}
					}
					imgpath = null;
					imgs_p = null;
				}
			}
			//详情图片
			info = bean.getInfo();
			if(info!=null&&!info.isEmpty()){
				infos = DownloadMain.getSpiderContextList1("(?:src=\")(.*?)(?:\")", info);
				int info_length = infos.size();
				for(int j=0;j<info_length;j++){
					if(total>1000){
						index++;
						total = 0;
					}
					imgs_p = infos.get(j);
					if("jpg".equals(imgs_p.substring(imgs_p.length()-3))){
						//System.out.println("imgs_p:"+imgs_p);
						imgpath = path+"/index"+index+"/"+getPath(imgs_p);
						//System.out.println(i+"-"+j+"-info_img:"+imgpath);
						if(ImgDownload.execute(imgs_p,imgpath )){
							total++;
							if(imgdao.queryExsis(imgs_p, imgpath,url)==0){
								imgdao.add(imgs_p, imgpath,url,id);
							}
						}
						imgpath = null;
					}
					imgs_p = null;
				}
				infos = null;
			}
			imgpath = null;
			img = null;
			url = null;
			LOG.warn("----imgdownloaddriver---");
		}
		return "";
	}
	
	public static String testdriver(int id1,int id2,int index) throws Exception{
		ImgFileDao  imgdao = new ImgFileDao();
		String path = TypeUtils.path+"/1688-0115";
//		String path = "F:/1688/";
		IOneSixExpressDao dao = new OneSixExpressDao();
		
		ArrayList<OneSixExpressBean> queryImg = dao.queryImg(id1,id2);
		int img_size = queryImg.size();
		String img = null;
		String bean_img = null;
		String[] imgs = null;
		String imgs_p = null;
		String info = null;
		String imgpath = null;
		OneSixExpressBean bean = null;
		String url = null;
		int id;
		int total = 0;
		List<String> infos = null;
		StringBuilder  sb = new StringBuilder();
		String file = null;
		for(int i=0;i<img_size;i++){
			LOG.warn("driver-----img------"+i);
			bean = queryImg.get(i);
			if(total>1000){
				index++;
				total = 0;
			}
			//搜索图片
			img = bean.getImg();
			url = bean.getUrl();
			id = bean.getId();
			if(img!=null&&!img.isEmpty()){
				file = imgdao.getImgFile(img, url);
				if(file.isEmpty()||!new File(file).exists()){
					imgpath = path+"/index-"+index+"/"+getPath(img);
					if(ImgDownload.execute(img,imgpath )){
						total++;
						if(imgdao.queryExsis(img, imgpath,url)==0){
							imgdao.add(img, imgpath,url,id);
						}
					}else{
						sb.append("******************************").append("\n\n")
						   .append(i).append("\n")
						   .append("imgurl:").append(img).append("\n")
						   .append("imgpath:").append(imgpath).append("\n")
						   .append("******************************");
					}
				}
			}
			//图片集
			bean_img = bean.getImgs();
			if(img!=null&&!img.isEmpty()){
				imgs = bean_img.replace("[", "").replace("]", "").split(",\\s*");
				int imgs_length = imgs.length;
				for(int j=0;j<imgs_length;j++){
					if(total>1000){
						index++;
						total = 0;
					}
					imgs_p = imgs[j]+"310x310.jpg";
					
					file = imgdao.getImgFile(imgs_p, url);
					
					if(file.isEmpty()||!new File(file).exists()){
						
						imgpath = path+"/index-"+index+"/"+getPath(imgs_p);
						
						if(ImgDownload.execute(imgs_p,imgpath )){
							total++;
							if(imgdao.queryExsis(imgs_p, imgpath,url)==0){
								imgdao.add(imgs_p, imgpath,url,id);
							}
						}else{
							sb.append("******************************").append("\n\n")
							   .append(i).append("\n")
							   .append("imgurl:").append(imgs_p).append("\n")
							   .append("imgpath:").append(imgpath).append("\n")
							   .append("******************************");
						}
					}
					imgpath = null;
					imgs_p = null;
				}
			}
			//详情图片
			info = bean.getInfo();
			if(info!=null&&!info.isEmpty()){
				infos = DownloadMain.getSpiderContextList1("(?:src=\")(.*?)(?:\")", info);
				int info_length = infos.size();
				for(int j=0;j<info_length;j++){
					if(total>1000){
						index++;
						total = 0;
					}
					imgs_p = infos.get(j);
					if("jpg".equals(imgs_p.substring(imgs_p.length()-3))){
						file = imgdao.getImgFile(imgs_p, url);
						
						if(file.isEmpty()||!new File(file).exists()){
							
							imgpath = path+"/index-"+index+"/"+getPath(imgs_p);
							
							if(ImgDownload.execute(imgs_p,imgpath )){
								total++;
								if(imgdao.queryExsis(imgs_p, imgpath,url)==0){
									imgdao.add(imgs_p, imgpath,url,id);
								}
							}else{
								sb.append("******************************").append("\n\n")
								   .append(i).append("\n")
								   .append("imgurl:").append(imgs_p).append("\n")
								   .append("imgpath:").append(imgpath).append("\n")
								   .append("******************************");
							}
						}
						
						imgpath = null;
					}
					imgs_p = null;
				}
				infos = null;
			}
			imgpath = null;
			img = null;
			url = null;
			LOG.warn("----imgdownloaddriver---");
		}
		return "";
	}
	
	public static String getPath(String imgurl){
		String path = null;
		if(imgurl!=null&&!imgurl.isEmpty()){
			String tem_path = DownloadMain.getSpiderContext(imgurl, "https*://.*com/");
			String replace = tem_path.replace("http://", "").replace(".com", "")
					         .replace("https://", "").replace(".", "/");
			path = imgurl.replace(tem_path, replace).replace("/", "_");
		}
		return path;
	}
	
	public static void main(String[] args) {
//		String imgurl = "http://img.china.alibaba.com/img/ibank/2014/106/698/1572896601_167640634.310x310.jpg";
//		
//		System.out.println(getPath(imgurl));
//		ImgDownload.execute(imgurl, "C:/Users/abc/Desktop/"+getPath(imgurl));
		
//		testImgfile();
	}
	
	
	
	private static void testImgfile(){
		ImgFileDao  dao  =new ImgFileDao();
		StringBuilder  sb = new StringBuilder();
		ArrayList<HashMap<String,String>> list = dao.queryTest(0, 999999);
		int list_num = list.size();
		String imgfile = null;
		String img_url = null;
		File file = null;
		String imgpath = null;
		String path = TypeUtils.path+"/1688-0115/";
		for(int i=0;i<list_num;i++){
			imgfile = list.get(i).get("file");
			file = new File(imgfile);
			if(!file.exists()){
				img_url = list.get(i).get("imgurl");
				imgpath = path +getPath(img_url);
				
				if(ImgDownload.execute(img_url,imgpath )){
					System.out.println("-----------------"+i);
					System.out.println(img_url);
					System.out.println(imgpath);
				}else{
					sb.append("******************************").append("\n\n")
					   .append(i).append("\n")
					   .append("imgurl:").append(img_url).append("\n")
					   .append("imgpath:").append(imgpath).append("\n")
					   .append("******************************");
				}
			}
			imgfile = null;
			file = null;
			img_url = null;
		}
	}
	
}
