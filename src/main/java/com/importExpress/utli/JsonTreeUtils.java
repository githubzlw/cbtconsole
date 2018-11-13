package com.importExpress.utli;

import com.importExpress.pojo.TabSeachPageBean;

import java.util.List;

/**
 * 处理关键词专页和品类精研的tree树
 * @ClassName JsonTreeUtils 
 * @Description TODO
 * @author Administrator
 * @date 2018年6月4日 下午1:14:17
 */
public class JsonTreeUtils {
	
	public static String jsonTree(List<TabSeachPageBean> li) {
		if(li==null || li.size()==0){return null;}
		 StringBuilder json = new StringBuilder();
         json.append("[");
         int i = 0;
         for (TabSeachPageBean bean : li){
                 json.append("{");
                 json.append("\"id\":" + bean.getId());
                 //json.append(",\"text\":\"" + bean.getKeyword() + "\"");
                 StringBuilder sb=new StringBuilder();
                 sb.append(",\"text\":\""+ bean.getKeyword());
               
                 if(bean.getParentId()!=0){
                	if(bean.getIsshow()==1){
     					sb.append("<font id="+bean.getId()+"f color=red>[已启用]</font>");
     				}else{
     					sb.append("<font id="+bean.getId()+"f color=red></font>");
     				}	
                }
                 
             	
             	sb.append("\"");
             	json.append(sb);
                if(bean.getParentId()==0){
                 json.append(",\"state\":\"" + "closed"+ "\"");
                }else{
                	 json.append(",\"state\":\"" + "open"+ "\"");
                }
                json.append(",\"pid\":" + bean.getParentId());
                 json.append("},");  
             }
           
         String str = json.toString();
         if (str.endsWith(","))
         {
        	 str = str.substring(0, str.length()-1); 
         }
         str+="]";
         return str;
		
	}
}
