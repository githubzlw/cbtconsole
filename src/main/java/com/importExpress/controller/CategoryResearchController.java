package com.importExpress.controller;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.util.SyncSingleGoodsToOnlineUtil;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.pojo.FineCategory;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.service.CategoryResearchService;
import com.importExpress.utli.JsonTreeUtils;
import com.importExpress.utli.SearchFileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 品类精研
 * @ClassName CategoryResearchController
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
			bean.setFilename(SearchFileUtils.importexpressPath+"/goodslist?keyword="+keyword+"&catid=0&srt=default");
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
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        if (StringUtils.isNotBlank(jsontree)) {  // 解决乱码
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(jsontree);
        }
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
		bean.setImportPath(SearchFileUtils.importexpressPath);

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
			bean.setFilename(SearchFileUtils.importexpressPath+"/goodslist?keyword="+keyword+"&catid=0&srt=default");
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

                String[] imgInfo = SearchFileUtils.comFileUpload(file, sid, 150, 150, null, 1);
                if (null != imgInfo && imgInfo.length > 0) {
                    fineCategory.setFileName(imgInfo[0]);
                    fineCategory.setImageUrl(imgInfo[1]);
                }

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

	//查询一个细分类商品
	@RequestMapping("/getOneFineCategory")
	public void getOneFineCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DataSourceSelector.set("dataSource127hop");
		String id = request.getParameter("id");
		FineCategory bean = categoryResearchService.getOneFineCategory(Integer.parseInt(id));
		bean.setFileName(SearchFileUtils.IMAGEHOSTURL+bean.getFileName());
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
			fineCategory.setFileName(SearchFileUtils.IMAGEHOSTURL+fineCategory.getFileName());
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
            String[] imgInfo = SearchFileUtils.comFileUpload(file, sid, 150, 150, null, 1);
            if (null != imgInfo && imgInfo.length > 0) {
                fineCategory.setFileName(imgInfo[0]);
                fineCategory.setImageUrl(imgInfo[1]);
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
		File file = new File(SearchFileUtils.LOCALPATHZIPIMG+sid);
		if(!file.exists()){
			result = "{\"status\":false,\"message\":\"当前分类已全部发布\"}";
		}else{
			//解压上传文件
			boolean isSuccess = SyncSingleGoodsToOnlineUtil.downCategoryResearchImgs(sid, "/data/importcsvimg/researchimg/");
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
			File file = new File(SearchFileUtils.LOCALPATHZIPIMG+sid);
			if (file.exists()) {
				File[] chidsFls = file.listFiles();
				for (File tempFl : chidsFls) {
					tempFl.delete();
				}
				file.delete();
				chidsFls = null;
			}
			File file2 = new File(SearchFileUtils.LOCALPATHZIPIMG);
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


}
