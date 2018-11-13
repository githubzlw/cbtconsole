package com.importExpress.controller;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.util.ExportTextUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.KeywordCatidRecordBean;
import com.importExpress.service.KeywordCatidRecordService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/keywordCatidRecord")
public class KeywordCatidRecordController {
	
	@Autowired
	private KeywordCatidRecordService keywordCatidRecordService;
	
	private static List<Map<String,Object>> category;
	
	@RequestMapping("/list")
	@ResponseBody
	public EasyUiJsonResult query(HttpServletRequest request, HttpServletResponse response){
		EasyUiJsonResult json = new EasyUiJsonResult();
		
		String pageNumStr = request.getParameter("rows");
		int pageSize = 20;
		if (pageNumStr == null || "".equals(pageNumStr) || "0".equals(pageNumStr)) {
			json.setSuccess(false);
			json.setMessage("获取分页数失败");
			return json;
		} else {
			pageSize = Integer.valueOf(pageNumStr);
		}

		String stateNumStr = request.getParameter("page");
		int currentPage = 1;
		if (!(stateNumStr == null || "".equals(stateNumStr) || "0".equals(stateNumStr))) {
			currentPage = Integer.valueOf(stateNumStr);
		}
		
		String keyword = request.getParameter("keyword");
		DataSourceSelector.set("dataSource127hop");
		Map<String,Object> map = keywordCatidRecordService.getList(keyword, currentPage, pageSize);
		DataSourceSelector.restore();
		json.setSuccess(true);
		json.setRows(map.get("list"));
		json.setTotal((Integer)map.get("count"));
		
		return json;
	}
	
	@RequestMapping("/add")
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String keyword = request.getParameter("keyword");
		String catid1 = request.getParameter("catid1");
		String catid2 = request.getParameter("catid2");
		String catid3 = request.getParameter("catid3");
		String[] keyword1 = request.getParameterValues("keyword1");
		StringBuffer keyword1sb = new StringBuffer("");
		if(keyword1!=null) {
			for(int i=0;i<keyword1.length;i++) {
				if(StringUtil.isNotBlank(keyword1[i])){
					keyword1sb.append(keyword1[i].trim()+";");
				}
			}
		}
		String issyn = request.getParameter("issyn");
		DataSourceSelector.set("dataSource127hop");
		String result = null;
		if(keywordCatidRecordService.getCountByKeyword(keyword)==0) {
			KeywordCatidRecordBean bean = new KeywordCatidRecordBean();
			bean.setKeyword(keyword);
			bean.setCatid1(catid1);
			bean.setCatid2(catid2);
			bean.setCatid3(catid3);
			if(StringUtil.isNotBlank(keyword1sb.toString())) {
				keyword1sb.deleteCharAt(keyword1sb.length()-1);
				bean.setKeyword1(keyword1sb.toString());
			}
			bean.setIssyn(Integer.parseInt(issyn));
			int res = keywordCatidRecordService.addKeyword(bean);
			if(res>0) {
				result = "{\"status\":true,\"message\":\"添加成功！\"}";
			} else {
				result = "{\"status\":false,\"message\":\"添加失败！\"}";
			}
		}else{
			result = "{\"status\":false,\"message\":\"添加的关键词已存在！\"}";
		}
		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	@RequestMapping("/get")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DataSourceSelector.set("dataSource127hop");
		String id = request.getParameter("id");
		KeywordCatidRecordBean bean = keywordCatidRecordService.getDetail(Integer.parseInt(id));
		DataSourceSelector.restore();
		
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(bean);
		out.print(jsonob);
		out.close();
	}
	
	@RequestMapping("/update")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String keyword = request.getParameter("update_keyword");
		String catid1 = request.getParameter("update_catid1");
		String catid2 = request.getParameter("update_catid2");
		String catid3 = request.getParameter("update_catid3");
		String[] keyword1 = request.getParameterValues("update_keyword1");
		StringBuffer keyword1sb = new StringBuffer("");
		if(keyword1!=null) {
			for(int i=0;i<keyword1.length;i++) {
				if(StringUtil.isNotBlank(keyword1[i])){
					keyword1sb.append(keyword1[i].trim()+";");
				}
			}
		}
		String issyn = request.getParameter("update_issyn");
		String id = request.getParameter("update_id");
		
		DataSourceSelector.set("dataSource127hop");
		String result = null;
		if(keywordCatidRecordService.getCountByKeyword1(keyword,Integer.parseInt(id))==0) {
			KeywordCatidRecordBean bean = new KeywordCatidRecordBean();
			bean.setKeyword(keyword);
			bean.setCatid1(catid1);
			bean.setCatid2(catid2);
			bean.setCatid3(catid3);
			if(StringUtil.isNotBlank(keyword1sb.toString())) {
				keyword1sb.deleteCharAt(keyword1sb.length()-1);
				bean.setKeyword1(keyword1sb.toString());
			}
			bean.setIssyn(Integer.parseInt(issyn));
			bean.setId(Integer.parseInt(id));
			int res = keywordCatidRecordService.updateKeyword(bean);
			if(res>0) {
				result = "{\"status\":true,\"message\":\"修改成功！\"}";
			} else {
				result = "{\"status\":false,\"message\":\"修改失败！\"}";
			}
		}else{
			result = "{\"status\":false,\"message\":\"关键词已存在！\"}";
		}
		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String id = request.getParameter("id");
		DataSourceSelector.set("dataSource127hop");
		int res = keywordCatidRecordService.delKeyword(Integer.parseInt(id));
		DataSourceSelector.restore();
		String result = null;
		if(res>0) {
			result = "{\"status\":true,\"message\":\"删除成功！\"}";
		} else {
			result = "{\"status\":false,\"message\":\"删除失败！\"}";
		}
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	@RequestMapping("/updateSyn")
	public void updateSyn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String id = request.getParameter("id");
		String issyn = request.getParameter("issyn");
		DataSourceSelector.set("dataSource127hop");
		String result = null;
		int res = keywordCatidRecordService.updateSyn(Integer.parseInt(id), Integer.parseInt(issyn));
		if(res>0){
			if("1".equals(issyn)) {
				result = "{\"status\":true,\"message\":\"启用成功\"}";
			}else{
				result = "{\"status\":true,\"message\":\"停用成功\"}";
			}
		}else{
			if("1".equals(issyn)) {
				result = "{\"status\":false,\"message\":\"启用失败\"}";
			}else{
				result = "{\"status\":false,\"message\":\"停用失败\"}";
			}
		}
		DataSourceSelector.restore();
		PrintWriter out = response.getWriter();
		JSONObject jsonob = JSONObject.fromObject(result);
		out.print(jsonob);
		out.close();
	}
	
	@RequestMapping("/synonyms.txt")
    public void exportText(HttpServletResponse response){
		DataSourceSelector.set("dataSource127hop");
		List<Map<String,Object>> list = keywordCatidRecordService.getKeyword1s();
		DataSourceSelector.restore();
		StringBuffer sb = new StringBuffer("");
		if(list!=null&&list.size()>0){
			for(Map<String,Object> map : list) {
				String keyword1 = (String) map.get("keyword1");
				if(StringUtil.isNotBlank(keyword1)) {
					sb.append(keyword1.replace(";", ",")).append("\r\n");
				}
			}
		}
		
        ExportTextUtil.writeToTxt(response,sb.toString(),"synonyms");
    } 
	
	
	@RequestMapping("/getCategorys")
	public void aliCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(category == null) {
			category = keywordCatidRecordService.getCategorys();
		}
		
		String catname = request.getParameter("searchname");
		
		List<Map<String,Object>> newlist = new ArrayList<Map<String,Object>>();
		if(StringUtil.isNotBlank(catname)) {
			Set<String> set = new HashSet<String>();
			for(Map<String,Object> map : category) {
				String name = (String) map.get("name");
				if(name.toLowerCase().contains(catname.toLowerCase())) {
					set.add((String) map.get("category_id"));
					String path = (String) map.get("path");
					String[] strs = path.split(",");
					for(String str : strs) {
						set.add(str);
					}
				}
			}
			
			for(Map<String,Object> map : category) {
				String catid = (String) map.get("category_id");
				if(set.contains(catid)) {
					newlist.add(map);
				}
			}
			
			listSort(newlist);
		} else {
			newlist = category;
		}
		
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>(); 
		List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>(); 
		List<Map<String,Object>> list3 = new ArrayList<Map<String,Object>>(); 
		for(Map<String,Object> map : newlist) {
			int lv = (Integer) map.get("lv");
			if(lv == 0 || lv == 1) {
				list1.add(map);
			}else if(lv == 2) {
				list2.add(map);
			}else if(lv == 3) {
				list3.add(map);
			}
		}
		
		JSONArray root = new JSONArray();
		for(Map<String,Object> map1 : list1) {
			JSONObject jsonob1 = new JSONObject();
			jsonob1.put("id", (String)map1.get("category_id"));
			jsonob1.put("text", ((String)map1.get("name")).replace("root", "全类"));
			
			String catid1 = (String) map1.get("category_id");
			JSONArray jsonarr1 = new JSONArray();
			for(Map<String,Object> map2 : list2) {
				String parentId1 = (String) map2.get("parent_id");
				if(catid1.equals(parentId1)) {
					JSONObject jsonob2 = new JSONObject();
					jsonob2.put("id", (String)map2.get("category_id"));
					jsonob2.put("text", (String)map2.get("name"));

					String catid2 = (String) map2.get("category_id");
					JSONArray jsonarr2 = new JSONArray();
					for(Map<String,Object> map3 : list3) {
						String parentId2 = (String) map3.get("parent_id");
						if(catid2.startsWith(parentId2)) {
							JSONObject jsonob3 = new JSONObject();
							jsonob3.put("id", (String)map3.get("category_id"));
							jsonob3.put("text", (String)map3.get("name"));
							
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
		
		PrintWriter out = response.getWriter();
		out.print(root);
		out.close();
		
	}
	
	
	public void listSort(List<Map<String,Object>> resultList) {  
        // resultList是需要排序的list，其内放的是Map  
        // 返回的结果集  
        Collections.sort(resultList,new Comparator<Map<String,Object>>() {  
	         public int compare(Map<String, Object> o1,Map<String, Object> o2) {  
	             //o1，o2是list中的Map，可以在其内取得值，按其排序，此例为升序，s1和s2是排序字段值  
	             Integer s1 = (Integer) o1.get("id");  
	             Integer s2 = (Integer) o2.get("id");  
	
		         if(s1>s2) {  
		        	 return 1;  
		         }else {  
		        	 return -1;  
		         }  
	         }  
        });  
         
     }
}
