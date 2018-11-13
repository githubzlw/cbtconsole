package com.importExpress.controller;

import com.cbt.FtpUtil.ContinueFTP2;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.ImgDownload;
import com.cbt.util.ImageCompression;
import com.cbt.util.SyncSingleGoodsToOnlineUtil;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.pojo.FineCategory;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.service.CategoryResearchService;
import com.importExpress.utli.JsonTreeUtils;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 品类精研
 * @ClassName CategoryResearchController 
 * @Description TODO
 * @author Administrator
 * @date 2018年3月8日 下午8:00:18
 */
@Controller
@RequestMapping("/categoryResearch")
public class CategoryResearchController {
	
	@Autowired
	private CategoryResearchService categoryResearchService;
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CategoryResearchController.class);
	private static List<Map<String,Object>> search1688Category;
	
//	private static String importexpressPath = SysParamUtil.getParam("importexpress");
	private static String importexpressPath = "https://www.import-express.com/";
	private static String IMAGEHOSTURL="http://192.168.1.34:8002/";
	private static final String LOCALPATH =  "D:/shopimgzip/";//图片上传的位置
	private static final String LOCALPATHZIPIMG =  "D:/shopimgzip/research/";//图片上传的位置
	private static final String IMAGESEARCHURL="https://img1.import-express.com/importcsvimg/stock_picture/researchimg/";//图片访问路径
	//新增关键词
	@RequestMapping("/add")
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String result = null;
		String keyword = request.getParameter("keyword");
		String keyword1 = request.getParameter("keyword1");
		String keyword2 = request.getParameter("keyword2");
		String keyword3 = request.getParameter("keyword3");
		String parentId = request.getParameter("parentId");
		DataSourceSelector.set("dataSource127hop");
		if(categoryResearchService.getWordsCount(keyword)>0){//查询该热搜词是否存在
			result = "{\"status\":false,\"message\":\"关键词“"+keyword+"”已存在！\"}";
		} else {
			TabSeachPageBean bean = new TabSeachPageBean();
			bean.setKeyword(keyword);
			bean.setKeyword1(keyword1);
			bean.setParentId(StringUtils.isNotBlank(parentId)?Integer.parseInt(parentId):0);
			bean.setFilename(importexpressPath+"/goodslist?keyword="+keyword+"&catid=0&srt=default");
			int res = categoryResearchService.insert(bean);
			
			if(res>0){
				result = "{\"status\":true,\"message\":\"关键词“"+keyword+"”添加成功！\",\"id\":\""+bean.getId()+"\"}";
			} else {
				result = "{\"status\":false,\"message\":\"关键词“"+keyword+"”添加失败！\"}";
			}
		}
		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	@RequestMapping("getAllCat")
	public @ResponseBody
    List<TabSeachPageBean> getAllCat(){
		DataSourceSelector.set("dataSource127hop");
		List<TabSeachPageBean> list = categoryResearchService.list(0);
		DataSourceSelector.restore();
		return list;
	}
	
	
	/**
	 * 删除分类
	 * @throws IOException 
	 */
	@RequestMapping("deleteCate")
	public void deleteCate(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String result = "";
		try{
			String id = request.getParameter("id");
			DataSourceSelector.set("dataSource127hop");
			categoryResearchService.deleCate(Integer.parseInt(id));	
			result = "{\"status\":true,\"message\":\"删除成功！\"}";
		}catch(Exception e){
			result = "{\"status\":false,\"message\":\"删除失败！\"}";
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		DataSourceSelector.restore();
	}
	//关键词列表
	@RequestMapping("/list")
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response) throws IOException {
	/*	response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DataSourceSelector.set("dataSource127hop");
		List<TabSeachPageBean> list = categoryResearchService.list();
		DataSourceSelector.restore();
		JSONArray jsonarr = JSONArray.fromObject(list);
		PrintWriter out = response.getWriter();
		out.print(jsonarr);
		out.close();*/
		String pid = request.getParameter("id");
		if(StringUtils.isBlank(pid)){
			pid="0";
		}
		 DataSourceSelector.set("dataSource127hop");
	        //在此只id及name属性, 
//	        String[] s = new String[] { "getId", "getKeyword","getParentId","getIsshow" };  
	        List<TabSeachPageBean> li = this.categoryResearchService.list(Integer.parseInt(pid));
	        //生成json格式的数据
	        String jsontree = JsonTreeUtils.jsonTree(li);
	        //this.tree(tm, li, s, true); 
			DataSourceSelector.restore();
			return jsontree;
		
	}
	
	


	// 构造组织机构树数据    ----修改成另一种解析方式
   /* private void tree(TreeModel tm, List<TabSeachPageBean> li, String[] s,  
            boolean containsDept) {  
    	
        if (!CollectionUtils.isEmpty(li)) {  
  
            for (int i = 0, l = li.size(); i < l; i++) {  
                TreeModel ntm = new TreeModel();  
                TabSeachPageBean tabSeachPageBean = li.get(i);  
                TreeUtil.copyModel(ntm, tabSeachPageBean, s);// 复制值到TreeModel  
                tm.getChildren().add(ntm);// 
               // List<TabSeachPageBean> list = this.categoryResearchService.list(tabSeachPageBean.getId());  
               // tree(ntm, list, s, containsDept);// 递归，实现无限层级  
            }  
        }  
    }  */
	//删除关键词
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DataSourceSelector.set("dataSource127hop");
		String id = request.getParameter("id");
		int res = categoryResearchService.delete(Integer.parseInt(id));
		DataSourceSelector.restore();
		String result = null;
		if(res>0){
			result = "{\"status\":true,\"message\":\"删除成功！\"}";
		} else {
			result = "{\"status\":false,\"message\":\"删除失败！\"}";
		}
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	//查询关键词对应的详情
	@RequestMapping("/get")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DataSourceSelector.set("dataSource127hop");
		String id = request.getParameter("id");
		TabSeachPageBean bean = categoryResearchService.get(Integer.parseInt(id));
		DataSourceSelector.restore();
		bean.setImportPath(importexpressPath);
		
		/*if(StringUtil.isNotBlank(bean.getFilename())) {
			bean.setFilename(importexpressPath+bean.getFilename());
		}*/
		
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(bean);
		out.print(jsonob);
		out.close();
	}
	/**
	 * 关键词专页预览
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/preview")
	public void preview(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String keyword = request.getParameter("keyword");
//		DataSourceSelector.set("dataSource127hop");
		//List<TabSeachPagesDetailBean> list = tabSeachPageService.detailList(Integer.parseInt(sid));
//		DataSourceSelector.restore();
		String result = "{status: true,message: \"ok\"}";
		
//		List <NameValuePair> params1 = new ArrayList<NameValuePair>();
//		params1.add(new BasicNameValuePair("keyword", keyword));  
//		String post = getContentClientPost("https://www.import-express.com/categortpage/detail",params1);
//		result = post;
//		if(StringUtils.isBlank(result)){
//			result="请稍后再试";
//			System.out.println("调用远程服务器异常");
//		}
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}
	//修改保存后的关键词
	@RequestMapping("/update")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String result = null;
		String id = request.getParameter("update_id");
		String keyword = request.getParameter("update_keyword");
		String keyword1 = request.getParameter("update_keyword1");
		String keyword2 = request.getParameter("update_keyword2");
		String keyword3 = request.getParameter("update_keyword3");
		String parentId = request.getParameter("parentId");
		
		
		DataSourceSelector.set("dataSource127hop");
		if(categoryResearchService.getWordsCount1(keyword,Integer.parseInt(id))>0){
			result = "{\"status\":false,\"message\":\"关键词“"+keyword+"”已存在！\"}";
		} else {
			TabSeachPageBean bean = new TabSeachPageBean();
			bean.setId(Integer.parseInt(id));
			bean.setKeyword(keyword);
			bean.setKeyword1(keyword1);
			bean.setFilename(importexpressPath+"/goodslist?keyword="+keyword+"&catid=0&srt=default");
			bean.setParentId(StringUtils.isNotBlank(parentId)?Integer.parseInt(parentId):0);
			int res = categoryResearchService.update(bean);
			if(res>0){
				result = "{\"status\":true,\"message\":\"修改成功！\"}";
			} else {
				result = "{\"status\":false,\"message\":\"修改失败！\"}";
			}
		}
		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	
	//新增细分类
	@RequestMapping("/addDetail")
	public void addDetail(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "uploadfile", required = true) MultipartFile file) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		//文件不能为空
		String result;
		try {
			result = null;
			if (!file.isEmpty()) {
				String detailProductName = request.getParameter("detail_name");//产品线简述
				String positivekeywords = request.getParameter("detail_keyword");//英文正关键词
				String reversekeywords = request.getParameter("detail_anti_words");//英文排除关键词
				String detail_seach_1688cid = request.getParameter("add_detail_seach_1688cid_update");
				String sid = request.getParameter("detail_sid");//对应关键词的id
				String catid = request.getParameter("catid");
				 //preClear(positivekeywords);
				//多个关键词处理
				//接收参数
				FineCategory fineCategory = new FineCategory();
				fineCategory.setDetailProductName(detailProductName);
				fineCategory.setPositivekeywords(positivekeywords);
				
				fineCategory.setReversekeywords(reversekeywords);
				fineCategory.setCatId1688(detail_seach_1688cid);
				fineCategory.setSid(org.apache.commons.lang.StringUtils.isBlank(sid)?0:Integer.parseInt(sid));
				fineCategory.setStatus(0);
				fineCategory.setCreateTime(new Date());
				//fineCategory.setCatid(Integer.parseInt(catid));
				//https://www.import-express.com
				String url="/goodslist?&srt=default";
				String newpositivekeywords="";
				if(StringUtil.isNotBlank(positivekeywords)){
					//String newpositivekeywords = positivekeywords.replaceAll(";", " or ");
					String[] splits = positivekeywords.split(";");
					if(splits!=null && splits.length>0){
						newpositivekeywords = splits[0];
					}
					url+="&keyword="+newpositivekeywords;
					}
				if(StringUtil.isNotBlank(reversekeywords)){
					String newreversekeywords = reversekeywords.replaceAll(";", " ");
					url+="&unkey="+newreversekeywords;
				}
				if(StringUtil.isNotBlank(detail_seach_1688cid)){
					url+="&catid="+detail_seach_1688cid;
					}
				
				fineCategory.setSearchUrl(url);
				
				//图片的接收
				// 获取配置文件信息
				// 文件的后缀取出来
				Random random = new Random();
				String originalName = file.getOriginalFilename();
				String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
				String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
				// 本地服务器磁盘全路径
				String localFilePath =LOCALPATH+sid+"/"+ saveFilename + fileSuffix;
				
				// 文件流输出到本地服务器指定路径
				ImgDownload.writeImageToDisk(file.getBytes(),localFilePath);
				//解压图片为120*120
				// 本地压缩图片
				File file2 = new File(LOCALPATHZIPIMG+sid);
				if(!file2.exists()){
					file2.mkdirs();
				}
				//上传图片文件名
				String fileCurrName = System.currentTimeMillis() + ".150x150" + fileSuffix;
				//判断图片是否da于150*150
				boolean checked = ImageCompression.checkImgResolution(localFilePath, 150, 150);
				if(!checked){
					//小于时直接输出到
					outputImage(localFilePath,LOCALPATHZIPIMG+sid+"/"+fileCurrName);
				}else{
				//将图片压缩到制定的目录 并重命名图片
				boolean is150 = reduceImgOnlyWidth(150, localFilePath, LOCALPATHZIPIMG + sid + "/" + fileCurrName,null);
				if(is150){
					System.out.println("压缩图片成功");
				}
				}
				
				fineCategory.setFileName(sid + "/" + fileCurrName);
				//保存图片的url访问路径,
				fineCategory.setImageUrl(IMAGESEARCHURL + fileCurrName);
				
				//支持断点续存上传图片,ss
				ContinueFTP2 f1 = new ContinueFTP2("104.247.194.50", "importweb", "importftp@123", "21", "/stock_picture/researchimg/" 
						+fileCurrName, LOCALPATHZIPIMG+sid+"/"+fileCurrName);
				//远程上传到图片服务器
				f1.start();
				
				DataSourceSelector.set("dataSource127hop");
                int res = categoryResearchService.insertDetail(fineCategory);
				if(res>0){
					//添加此细分类关键词到对应的主关键词下
					TabSeachPageBean bean = categoryResearchService.get(StringUtils.isBlank(sid)?0:Integer.parseInt(sid));
						String keyword2 = bean.getKeyword2();
						String newkeyword2=null;
						String newkeyword3=null;
						if(StringUtils.isNotBlank(keyword2)){
							String[] spitkey2 = keyword2.split(";");
							if(spitkey2.length>20){
								newkeyword2 = keyword2;
								//存到keyword3中
								String keyword3 = bean.getKeyword3();
								if(StringUtils.isBlank(keyword3)){
									newkeyword3 = detailProductName+";";
								}else{
									newkeyword3 = keyword3+detailProductName.trim()+";";
								}
							}else{
								 newkeyword2 = keyword2+detailProductName.trim()+";";
							}
						}else{
							    newkeyword2 = detailProductName.trim()+";";
						}
						bean.setKeyword2(newkeyword2);
						bean.setKeyword3(newkeyword3);
						//更新主关键词
						categoryResearchService.update(bean);
						
					result = "{\"status\":true,\"message\":\"添加成功！\"}";
				} else {
					result = "{\"status\":false,\"message\":\"添加失败！\"}";
				}
				DataSourceSelector.restore();
				PrintWriter out = response.getWriter();
				JSONObject jsonob = JSONObject.fromObject(result);
				out.print(jsonob);
				out.close();
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}	
		

	private void preClear(String positivekeywords) {
		// TODO Auto-generated method stub
		
	}
			//查询一个细分类商品
			@RequestMapping("/getOneFineCategory")
			public void getOneFineCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
			
			response.setContentType("text/json;charset=utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			DataSourceSelector.set("dataSource127hop");
			String id = request.getParameter("id");
			FineCategory bean = categoryResearchService.getOneFineCategory(Integer.parseInt(id));
			bean.setFileName(IMAGEHOSTURL+bean.getFileName());
			DataSourceSelector.restore();
			PrintWriter out = response.getWriter();
			JSONObject jsonob = JSONObject.fromObject(bean);
			out.print(jsonob);
			out.close();
			}
		

	//查询该关键字下的细分类详情
	@RequestMapping("/detailCategoryList")
	public void detailList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String sid = request.getParameter("sid");
		DataSourceSelector.set("dataSource127hop");
		List<FineCategory> list = categoryResearchService.detailFineCategoryList(Integer.parseInt(sid));
		for (FineCategory fineCategory : list) {
			fineCategory.setFileName(IMAGEHOSTURL+fineCategory.getFileName());
		}
		DataSourceSelector.restore();
		JSONArray jsonarr = JSONArray.fromObject(list);
		PrintWriter out = response.getWriter();
		out.print(jsonarr);
		out.close();
	}
	
	//保存更新后的细分类商品
	@RequestMapping("/updateFineCategory")
	public void updateFineCategory(HttpServletRequest request, @RequestParam(value = "uploadfile", required = true) MultipartFile file , HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		//参数的封装
		String id = request.getParameter("id");
		String sid = request.getParameter("detail_sid_update");
		String detailProductName = request.getParameter("detail_name_update");//名字
		String positivekeywords = request.getParameter("detail_keyword_update");//正关键词
		String reversekeywords = request.getParameter("detail_anti_words_update");//反关键词
		String detail_seach_1688cid = request.getParameter("update_detail_seach_1688cid_update");//1688类别
		String old_positivekeywords = request.getParameter("old_positivekeywords");
//		String old_detail_keyword_update = request.getParameter("old_detail_keyword_update");
		FineCategory fineCategory = new FineCategory();
		String result="";
		try {
			if(!file.isEmpty()){//修改图片
				//图片的接收
				// 获取配置文件信息
				// 文件的后缀取出来
				Random random = new Random();
				String originalName = file.getOriginalFilename();
				String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
				String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
				// 本地服务器磁盘全路径
				String localFilePath =LOCALPATH+sid+"/"+ saveFilename + fileSuffix;
				
				// 文件流输出到本地服务器指定路径
				ImgDownload.writeImageToDisk(file.getBytes(),localFilePath);
				//解压图片为120*120
				// 本地压缩图片
				File file2 = new File(LOCALPATHZIPIMG+sid);
				if(!file2.exists()){
					file2.mkdirs();
				}
				//上传图片文件名
				String fileCurrName = System.currentTimeMillis() + ".150x150" + fileSuffix;		
				//判断图片是否da于120*120
				boolean checked = ImageCompression.checkImgResolution(localFilePath, 150, 150);
				if(!checked){
					//小于时直接输出到
					outputImage(localFilePath,LOCALPATHZIPIMG + sid + "/" + fileCurrName);
				}else{
					//将图片压缩到制定的目录 并重命名图片
					boolean is150 = reduceImgOnlyWidth(150, localFilePath, LOCALPATHZIPIMG+sid+"/"+fileCurrName,null);
					if(is150){
						System.out.println("压缩图片成功");
					}
				}
				
				fineCategory.setFileName(sid+"/"+fileCurrName);
				//保存图片的url访问路径,
				fineCategory.setImageUrl(IMAGESEARCHURL + fileCurrName);
				
				//支持断点续存上传图片,ss
				ContinueFTP2 f1 = new ContinueFTP2("104.247.194.50", "importweb", "importftp@123", "21", "/stock_picture/researchimg/" 
						+fileCurrName, LOCALPATHZIPIMG+sid+"/"+fileCurrName);
				//远程上传到图片服务器
				f1.start();
				
			}
			fineCategory.setId(Integer.parseInt(id));
			fineCategory.setDetailProductName(detailProductName);
			fineCategory.setPositivekeywords(positivekeywords);
			fineCategory.setReversekeywords(reversekeywords);
			fineCategory.setCatId1688(detail_seach_1688cid);
			fineCategory.setSid(org.apache.commons.lang.StringUtils.isBlank(sid)?0:Integer.parseInt(sid));
			String url="/goodslist?&srt=default";
			String newpositivekeywords ="";
			if(StringUtil.isNotBlank(positivekeywords)){
				//String newpositivekeywords = positivekeywords.replaceAll(";", " or ");
				String[] split = positivekeywords.split(";");
				newpositivekeywords=split[0];
				url+="&keyword="+split[0];
				}
			if(StringUtil.isNotBlank(reversekeywords)){
				String newreversekeywords = reversekeywords.replaceAll(";", " ");
				url+="&unkey="+newreversekeywords;
			}
			if(StringUtil.isNotBlank(detail_seach_1688cid)){
				url+="&catid="+detail_seach_1688cid;
				}
			
			fineCategory.setSearchUrl(url);
			DataSourceSelector.set("dataSource127hop");
		    int res = categoryResearchService.updateFineCategory(fineCategory);
			if(res>0){
				TabSeachPageBean bean = categoryResearchService.get(StringUtils.isBlank(sid)?0:Integer.parseInt(sid));
				String keyword2 = StringUtils.isBlank(bean.getKeyword2())?"":bean.getKeyword2();
				String newpositivekeyword=null;
				String oldkey = old_positivekeywords.split(";")[0];
				if(keyword2.contains(oldkey+";")){
					newpositivekeyword = keyword2.replace(old_positivekeywords+";", positivekeywords.split(";")[0].trim()+";");
					bean.setKeyword2(newpositivekeyword);
				}else if(StringUtils.isNotBlank(bean.getKeyword3())){
					   if(bean.getKeyword3().contains(oldkey+";")){
						   newpositivekeyword = bean.getKeyword3().replace(oldkey+";", positivekeywords.split(";")[0].trim()+";");
					   }else{
						   newpositivekeyword=bean.getKeyword3()+positivekeywords.split(";")[0].trim()+";";
					   }
					
					bean.setKeyword3(newpositivekeyword);
				}else{
					newpositivekeyword = keyword2+positivekeywords.split(";")[0].trim()+";";
					bean.setKeyword2(newpositivekeyword);
				}
				
				int i = categoryResearchService.update(bean);
				if(i>0){
//					List <NameValuePair> params1 = new ArrayList<NameValuePair>();
//					getContentClientPost(importexpressPath+"/app/flushReasearch",params1);
					result = "{\"status\":true,\"message\":\"修改成功！\"}";
					
				}else{
					result = "{\"status\":false,\"message\":\"修改失败！\"}";
				}
				
				}
				
			PrintWriter out = response.getWriter();
			JSONObject jsonob = JSONObject.fromObject(result);
			out.print(jsonob);
			out.close();
			DataSourceSelector.restore();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	//删除选择的细分类商品
	@RequestMapping("/deleteFineCategory")
	public void deleteFineCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DataSourceSelector.set("dataSource127hop");
		String id = request.getParameter("id");
		String oldpositivekeywords = request.getParameter("oldpositivekeywords");
		String sid = request.getParameter("sid");
		
		int res = categoryResearchService.deleteFineCategory(Integer.parseInt(id));
		String result = null;
		if(res>0){
			TabSeachPageBean bean = categoryResearchService.get(StringUtils.isBlank(sid)?0:Integer.parseInt(sid));
			if(bean!=null ){
				String newoldpositivekeywords = "";
				if(bean.getKeyword2().contains(oldpositivekeywords.trim()+";")){
					//判断是不是最后一个,是的话,替换前面的分号,不是的话,替换后面的分号
//					String[] split = bean.getKeyword2().split(";");
//					if(split.length>0 && oldpositivekeywords.equals(split[split.length-1])){
//						newoldpositivekeywords = bean.getKeyword2().replace(";"+oldpositivekeywords, "");
//					}else{
//						newoldpositivekeywords = bean.getKeyword2().replace(oldpositivekeywords+";", "");
//					}
						newoldpositivekeywords = bean.getKeyword2().replace(oldpositivekeywords.trim()+";", "");
						/*if(newoldpositivekeywords.equals(oldpositivekeywords)){
							newoldpositivekeywords = bean.getKeyword2().replace(oldpositivekeywords, "");
						}*/
					bean.setKeyword2(newoldpositivekeywords);
				}else{
					if(bean.getKeyword3().contains(oldpositivekeywords.trim()+";")){
						//判断是不是最后一个,是的话,替换前面的分号,不是的话,替换后面的分号
//						String[] split = bean.getKeyword3().split(";");
//						if(split.length>0 && oldpositivekeywords.equals(split[split.length-1])){
//							newoldpositivekeywords = bean.getKeyword3().replace(";"+oldpositivekeywords, "");
//						}else{
//							newoldpositivekeywords = bean.getKeyword3().replace(oldpositivekeywords+";", "");
//						}
						newoldpositivekeywords = bean.getKeyword3().replace(oldpositivekeywords.trim()+";", "");
							/*if(newoldpositivekeywords.equals(oldpositivekeywords)){
								newoldpositivekeywords = bean.getKeyword3().replace(oldpositivekeywords, "");
							}*/
						bean.setKeyword3(newoldpositivekeywords);
				}
			}
				categoryResearchService.update(bean);
//			List <NameValuePair> params1 = new ArrayList<NameValuePair>();
//			getContentClientPost(importexpressPath+"/app/flushReasearch",params1);
			result = "{\"status\":true,\"message\":\"删除成功！\"}";
		} 
		}else {
			result = "{\"status\":false,\"message\":\"删除失败！\"}";
		}
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
		DataSourceSelector.restore();
	}

	
	
	//发布图片去线上
	@RequestMapping("/submitFineCategoryToLine")
	public void submitFineCategoryToLine(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String result;
		//发布的操作
		String sid = request.getParameter("sid");
		File file = new File(LOCALPATHZIPIMG+sid);
		if(!file.exists()){
			result = "{\"status\":false,\"message\":\"当前分类已全部发布\"}";
		}else{
			//解压上传文件
			boolean isSuccess = SyncSingleGoodsToOnlineUtil.downCategoryResearchImgs(sid, "/usr/local/goodsimg/importcsvimg/researchimg/");
//			List <NameValuePair> params1 = new ArrayList<NameValuePair>();
//			getContentClientPost(importexpressPath+"/app/flushReasearch",params1);
			if(isSuccess){
				 result="{\"status\":false,\"message\":\"发布成功\"}";
				 //发布之后先清理图片临时文件
					deleteTempZip(sid);
			 }else{
				 result="{\"status\":false,\"message\":\"发布失败\"}";
			 }
		
			
		}
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	@RequestMapping("/isshow")
	public void isshow(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String id = request.getParameter("id");
		String isshow = request.getParameter("isshow");
		DataSourceSelector.set("dataSource127hop");
		String result = null;
		if(StringUtil.isNotBlank(id)) {
			int res = categoryResearchService.updateIsshow(Integer.parseInt(isshow), Integer.parseInt(id));
			if(res>0){
//				List <NameValuePair> params1 = new ArrayList<NameValuePair>();
//				getContentClientPost(importexpressPath+"/categortpage/detail",params1);
				if("1".equals(isshow)) {
					result = "{\"status\":true,\"message\":\"启用成功\"}";
				}else{
					result = "{\"status\":true,\"message\":\"停用成功\"}";
				}
			}else{
				if("1".equals(isshow)) {
					result = "{\"status\":false,\"message\":\"启用失败\"}";
				}else{
					result = "{\"status\":false,\"message\":\"停用失败\"}";
				}
			}
		}else{
			result = "{\"status\":false,\"message\":\"请选择关键词\"}";
		}
		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	
		/**
		 * 查询分类列表search1688Category
		 * @Title makeFileName 
		 * @Description TODO
		 * @param filename
		 * @return
		 * @return String
		 */
	
	@RequestMapping("/search1688Category")
	public void aliCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
//		DataSourceSelector.set("dataSource127hop");
		if(search1688Category == null) {
			search1688Category = categoryResearchService.search1688Category();
		}
		
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>(); 
		List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>(); 
		List<Map<String,Object>> list3 = new ArrayList<Map<String,Object>>(); 
		List<Map<String,Object>> list4 = new ArrayList<Map<String,Object>>(); 
		List<Map<String,Object>> list5 = new ArrayList<Map<String,Object>>(); 
		for(Map<String,Object> map : search1688Category) {
			int lv = (Integer) map.get("lv");
			if(lv == 0 || lv == 1) {
				list1.add(map);
			}else if(lv == 2) {
				list2.add(map);
			}else if(lv == 3) {
				list3.add(map);
			}else if(lv == 4) {
				list4.add(map);
			}else if(lv == 5) {
				list5.add(map);
			}
		}
		
		JSONArray root = new JSONArray();
		for(Map<String,Object> map1 : list1) {
			JSONObject jsonob1 = new JSONObject();
			jsonob1.put("id", (String)map1.get("cid"));
			jsonob1.put("text", StringUtils.isBlank((String)map1.get("category"))? "All Categoty" : (String)map1.get("category")+"  /  "+ (String)map1.get("name") );
			
			String path1 = (String) map1.get("path");
			JSONArray jsonarr1 = new JSONArray();
			for(Map<String,Object> map2 : list2) {
				String path2 = (String) map2.get("path");
				if(path2.startsWith(path1+",")) {
					JSONObject jsonob2 = new JSONObject();
					jsonob2.put("id", (String)map2.get("cid"));
					jsonob2.put("text", (String)map2.get("category")+"  /  "+(String)map2.get("name"));
					
					
					JSONArray jsonarr2 = new JSONArray();
					for(Map<String,Object> map3 : list3) {
						String path3 = (String) map3.get("path");
						if(path3.startsWith(path2+",")) {
							JSONObject jsonob3 = new JSONObject();
							jsonob3.put("id", (String)map3.get("cid"));
							jsonob3.put("text", (String)map3.get("category")+"  /  "+(String)map3.get("name"));
							
							
							JSONArray jsonarr3 = new JSONArray();
							for(Map<String,Object> map4 : list4) {
								String path4 = (String) map4.get("path");
								if(path4.startsWith(path3+",")) {
									JSONObject jsonob4 = new JSONObject();
									jsonob4.put("id", (String)map4.get("cid"));
									jsonob4.put("text", (String)map4.get("category")+"  /  "+(String)map4.get("name"));
									
									
									JSONArray jsonarr4 = new JSONArray();
									for(Map<String,Object> map5 : list5) {
										String path5 = (String) map5.get("path");
										if(path5.startsWith(path4+",")) {
											JSONObject jsonob5 = new JSONObject();
											jsonob5.put("id", (String)map5.get("cid"));
											jsonob5.put("text", (String)map5.get("category")+"  /  "+(String)map5.get("name"));
											
											jsonarr4.add(jsonob5);
										}
									}
									
									if(jsonarr4.size()>0) {
										jsonob4.put("state", "closed");
										jsonob4.put("children", jsonarr4);
									}
									
									jsonarr3.add(jsonob4);
								}
							}
							
							if(jsonarr3.size()>0) {
								jsonob3.put("state", "closed");
								jsonob3.put("children", jsonarr3);
							}
							
							jsonarr2.add(jsonob3);
						}
					}
					if(jsonarr2.size()>0) {
						jsonob2.put("state", "closed");
						jsonob2.put("children", jsonarr2);
					}
					
					jsonarr1.add(jsonob2);
				}
			}
			if(jsonarr1.size()>0) {
				jsonob1.put("state", "closed");
				jsonob1.put("children", jsonarr1);
			}
			root.add(jsonob1);
		}
//		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		out.print(root);
		out.close();
		
	}
	

	
	
	@RequestMapping("/search1688CategoryLazy")
	public void search1688CategoryLazy(HttpServletRequest request, HttpServletResponse response) throws IOException {
			response.setContentType("text/json;charset=utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");	
			String id = request.getParameter("id");
			JSONArray jsonarr = new JSONArray();
			DataSourceSelector.set("dataSource127hop");
			List<Map<String,Object>> listCategorys = null;//categoryResearchService.search1688CategoryLazy(id);
			for (Map<String, Object> map : listCategorys) {
				JSONObject jsonob1 = new JSONObject();
				jsonob1.put("id", (String)map.get("cid"));
				jsonob1.put("text", (String)map.get("category")+"  /  "+(String)map.get("name"));
				//此处操作的是是父节点还是子节点
				if(StringUtils.isNotBlank((String)map.get("childids"))){
					jsonob1.put("state", "closed");
					jsonob1.put("children", "true");
				}
				
				jsonarr.add(jsonob1);
			}
			PrintWriter out = response.getWriter();
			out.print(jsonarr);
			out.close();
			/*if(StringUtils.isBlank(id)){//此时表示是用第一级别的数据
				listCategorys.search1688CategoryLazy(id);
			}else{//通过父节点来的
				listCategorys.search1688CategoryLazy(id);
			}*/
			DataSourceSelector.restore();
			
	   }
	
	
	/*@RequestMapping("/listLazy")
	public void aliCategoryLazy(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DataSourceSelector.set("dataSource127hop");
		String parentid = request.getParameter("parentid");
		int id= 1;
		if(StringUtils.isNotBlank(parentid)){
			id=Integer.parseInt(parentid);
		}
		
		if(search1688Category == null) {
			search1688Category = categoryResearchService.search1688CategoryLazy(id);
		}
		
		
		
		JSONArray root = new JSONArray();
		for(Map<String,Object> map1 : list1) {
			JSONObject jsonob1 = new JSONObject();
			jsonob1.put("id", (String)map1.get("cid"));
			jsonob1.put("text", (String)map1.get("category"));
			
			String path1 = (String) map1.get("path");
			JSONArray jsonarr1 = new JSONArray();
			for(Map<String,Object> map2 : list2) {
				String path2 = (String) map2.get("path");
				if(path2.startsWith(path1+",")) {
					JSONObject jsonob2 = new JSONObject();
					jsonob2.put("id", (String)map2.get("cid"));
					jsonob2.put("text", (String)map2.get("category"));
					
					
					JSONArray jsonarr2 = new JSONArray();
					for(Map<String,Object> map3 : list3) {
						String path3 = (String) map3.get("path");
						if(path3.startsWith(path2+",")) {
							JSONObject jsonob3 = new JSONObject();
							jsonob3.put("id", (String)map3.get("cid"));
							jsonob3.put("text", (String)map3.get("category"));
							
							
							JSONArray jsonarr3 = new JSONArray();
							for(Map<String,Object> map4 : list4) {
								String path4 = (String) map4.get("path");
								if(path4.startsWith(path3+",")) {
									JSONObject jsonob4 = new JSONObject();
									jsonob4.put("id", (String)map4.get("cid"));
									jsonob4.put("text", (String)map4.get("category"));
									
									
									JSONArray jsonarr4 = new JSONArray();
									for(Map<String,Object> map5 : list5) {
										String path5 = (String) map5.get("path");
										if(path5.startsWith(path4+",")) {
											JSONObject jsonob5 = new JSONObject();
											jsonob5.put("id", (String)map5.get("cid"));
											jsonob5.put("text", (String)map5.get("category"));
											
											jsonarr4.add(jsonob5);
										}
									}
									
									if(jsonarr4.size()>0) {
										jsonob4.put("state", "closed");
										jsonob4.put("children", jsonarr4);
									}
									
									jsonarr3.add(jsonob4);
								}
							}
							
							if(jsonarr3.size()>0) {
								jsonob3.put("state", "closed");
								jsonob3.put("children", jsonarr3);
							}
							
							jsonarr2.add(jsonob3);
						}
					}
					if(jsonarr2.size()>0) {
						jsonob2.put("state", "closed");
						jsonob2.put("children", jsonarr2);
					}
					
					jsonarr1.add(jsonob2);
				}
			}
			if(jsonarr1.size()>0) {
				jsonob1.put("state", "closed");
				jsonob1.put("children", jsonarr1);
			}
			root.add(jsonob1);
		}
		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		out.print(root);
		out.close();
		
	}*/
	

	
	
	
	
	
	
	
	
	
	private String makeFileName(String filename) { // 2.jpg
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString() + "_" + filename;
	}
	
	//清理临时和缓存图片
	private static void deleteTempZip(String sid) {
		try {
			File file = new File(LOCALPATHZIPIMG+sid);
			if (file.exists()) {
				File[] chidsFls = file.listFiles();
				for (File tempFl : chidsFls) {
					tempFl.delete();
				}
				file.delete();
				chidsFls = null;
			}
			File file2 = new File(LOCALPATHZIPIMG);
			if (file2.exists()) {
				File[] chidsFls = file2.listFiles();
				for (File tempFl : chidsFls) {
					tempFl.delete();
				}
			}
			
		} catch (Exception e) {
			System.err.println("deleteTempZip error:" + e.getMessage());
			LOG.error("deleteTempZip error:" + e.getMessage());
		}
	}
	
	/**
	 * 根据宽度压缩图片，高度等比例压缩
	 * 
	 * @param width
	 *            压缩宽度，必填
	 * @param imgUrl
	 *            源图片地址
	 * @param targetImgUrl
	 *            目标图片地址
	 */
	public static boolean reduceImgOnlyWidth(double width, String imgUrl, String targetImgUrl, Float rate) {
		boolean is = false;
		FileOutputStream out = null;
		try {
			
			  File srcfile = new File(imgUrl);
	            // 检查图片文件是否存在
	            if (!srcfile.exists()) {
	                System.out.println("文件不存在");
	            }
	            // 如果比例不为空则说明是按比例压缩
	            if (rate != null && rate > 0) {
	                //获得源图片的宽高存入数组中
	                int[] results = getImgWidth(srcfile);
	                if (results == null || results[0] == 0 || results[1] == 0) {
	                    return false;
	                } else {
	                    //按比例缩放或扩大图片大小，将浮点型转为整型
	                	width = (int) (results[0] * rate);
	                	width = (int) (results[1] * rate);
	                }
	            }
				// 开始读取文件并进行压缩
				Image src = javax.imageio.ImageIO.read(srcfile);
				BufferedImage tag = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(src.getScaledInstance((int) 150, 150, Image.SCALE_SMOOTH), 0, 0, null);
				out = new FileOutputStream(targetImgUrl);
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//				encoder.encode(tag);
				is = true;
			
		} catch (IOException ex) {
			ex.getStackTrace();
			System.out.println("imgUrl:" + imgUrl + ",targetImgUrl" + targetImgUrl);
			System.out.print(ex.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return is;
	}
	/**
	 * 获取图片宽度
	 * 
	 * @param file
	 *            图片文件
	 * @return 宽度
	 */
	public static int[] getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			result[0] = src.getWidth(null); // 得到源图宽
			result[1] = src.getHeight(null); // 得到源图高
		} catch (Exception e) {
			e.getStackTrace();
			System.out.print(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 接口调用方法
	 * @param urls
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getContentClientPost(String urls,List <NameValuePair> params){ 
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol")
						.setLevel(java.util.logging.Level.OFF);
		if(urls==null||urls.isEmpty()){
			return "";
		}
		String co = "";
		//HttpClient4.1的调用与之前的方式不同     
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try {
			String url = urls.replaceAll("\\s", "%20");
			//链接超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5*60*1000); 
			//读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5*60*1000);
			HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
			
			HttpPost httpPost = new HttpPost(url); 
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
			
			response = client.execute(httpPost);
			
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {  
				HttpEntity entity = response.getEntity();     
				if (entity != null) { 
					InputStreamReader in = null;
					if(Pattern.compile("(taobao)|(tmall)|(1688)").matcher(urls).find()){
						in = new InputStreamReader(entity.getContent(), "gbk");
					}else{
						in = new InputStreamReader(entity.getContent(), HTTP.UTF_8);
					}
					BufferedReader reader = new BufferedReader(in);     
					co = IOUtils.toString(reader);
					in.close();
					reader.close();
					in = null;
					reader = null;
				}  
				entity = null;
				httpPost = null;
			}
		} catch (ClientProtocolException e) {
			co = "";
		} catch (IOException e) {
			co = "";
		}finally{
			if(client!=null){
				client.close();
			}
			response = null;
		}
		return co;     
	}
	

	
	public static void outputImage(String srcPath, String destPath) {
		  // 打开输入流
      try {
		FileInputStream fis = new FileInputStream(srcPath);
		  // 打开输出流
		  FileOutputStream fos = new FileOutputStream(destPath);
		  
		  // 读取和写入信息
		  int len = 0;
		  // 创建一个字节数组，当做缓冲区
		  byte[] b = new byte[1024];
		  while ((len = fis.read(b)) != -1) {
		      fos.write(b, 0, len);
		  }
		  
		  // 关闭流  先开后关  后开先关
		  fos.close(); // 后开先关
		  fis.close(); // 先开后关
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}
	
	

public static void readfile(String filepath) {
					File file1 = new File("K:\\researchimgs");
					File[] files = file1.listFiles();
					for (File file : files) {
						if (file.isDirectory()) {
							File file2 = new File(file.getAbsolutePath().replace("K:\\researchimgs", "K:/shopimgzip/research"));
							if(!file2.exists()){
								file2.mkdirs();
							}
							String[] filelist = file.list();
								for (int i = 0; i < filelist.length; i++) {
										//File srcfile = new File(file.getAbsolutePath() + "\\" + filelist[i]);
										boolean checked = ImageCompression.checkImgResolution(file.getAbsolutePath() + "\\" + filelist[i], 150, 150);
										
										String src = file.getAbsolutePath() + "\\" + filelist[i];
										String desc = src.replace("K:\\researchimgs", "K:/shopimgzip/research");
										String fileSuffix = desc.substring(desc.lastIndexOf("."));
										String newdesc = desc.substring(0, desc.lastIndexOf("."));
										if(!checked){
											//小于时直接输出到
											outputImage(file.getAbsolutePath() + "\\" + filelist[i],newdesc+".150x150"+fileSuffix);
										}else{
										//将图片压缩到制定的目录 并重命名图片
										  boolean is120 = reduceImgOnlyWidth(150, file.getAbsolutePath() + "\\" + filelist[i],newdesc+".150x150"+fileSuffix,null);
										}
												
								}
								}
					}
					
					

}



	
	
	public static void main(String[] args) {
		readfile(null);
		/*//读取所有的图片.压缩后到k盘
		
		
		
		//判断图片是否小于150*150
		boolean checked = ImageCompression.checkImgResolution(localFilePath, 150, 150);
		if(checked){
			//小于时直接输出到
			outputImage(localFilePath,LOCALPATHZIPIMG+sid+"/"+saveFilename+".150x150"+fileSuffix);
		}else{
		//将图片压缩到制定的目录 并重命名图片
		boolean is120 = reduceImgOnlyWidth(150, localFilePath, LOCALPATHZIPIMG+sid+"/"+saveFilename+".150x150"+fileSuffix,null);*/
	
		
	}
		
		
		
		
		
		
	
}
