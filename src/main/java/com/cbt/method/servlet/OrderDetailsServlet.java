package com.cbt.method.servlet;

import com.cbt.method.service.OrderDetailsService;
import com.cbt.method.service.OrderDetailsServiceImpl;
import com.cbt.pojo.Admuser;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.StringUtil;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 
 * 存储采购需要购买商品的信息到F盘的文本中
 * @author 王宏杰
 * 最后修改时间:2016-10-08
 */
public class OrderDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	OrderDetailsService ods=new OrderDetailsServiceImpl();
	
	
	public void addGoodNoForRedis(HttpServletRequest request, HttpServletResponse response) {
		String parameters=request.getParameter("parameters");
		System.out.println("parameters为"+parameters);
		String admIds=request.getParameter("admId");
		int admid=Integer.valueOf(StringUtil.isBlank(admIds)?"0":admIds);
		System.out.println("当前用户id为:"+admIds);
		String str[]=new String[parameters.indexOf(",")>-1?parameters.split(",").length:1];
		if(parameters.indexOf(",")>-1){
			str=parameters.split(",");
		}else{
			str[0]=parameters;
			
		}
		String goodNo="";
		String orderNo="";
		if("0".equals(admIds)){
			//获取登录用户
			String admJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
			admid=user.getId();
		}
		String admname=ods.getUserName(admid);
		boolean flag=true;
		Properties properties = new Properties();
        try
        {
        	String filePath= GetConfigureInfo.getAddGoodsPath();//获取项目路径
            InputStream inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
            inputStream.close(); //关闭流
            String result = properties.getProperty("result");
            if(result!=null && !result.equals("")){
            	JSONArray json=JSONArray.fromObject(result);
                for(int i=0;i<json.size();i++){
                	Map map=(Map) json.get(i);
                	if(map.get(String.valueOf(admname))!=null){//如果该用户已经存在
                		List list=(List) map.get(String.valueOf(admname));
                		for(int j=0;j<str.length;j++){
                			goodNo=str[j].split(":")[0];
                			orderNo=str[j].split(":")[1];
                			if(!list.contains(goodNo+":"+orderNo)){
                    			list.add(goodNo+":"+orderNo);
                        		json.remove(i);
                        		json.add(map);
                        		this.writeData("result", json.toString(), filePath);
                    		}
                		}
                		flag=false;
                		break;
                	}
                }
                if(flag){
            		Map mapn = new HashMap();
        		    List list=new ArrayList();
        		    for(int j=0;j<str.length;j++){
       		    	 list.add(str[j].split(":")[0]+":"+str[j].split(":")[1]);
       		        }
        		    mapn.put(String.valueOf(admname), list);
        		    json.add(mapn);
        		    this.writeData("result", json.toString(), filePath);
            	}
            }else{//文件里面为空
            	Map map = new HashMap();
    		    List list=new ArrayList();
    		    for(int j=0;j<str.length;j++){
    		    	 list.add(str[j].split(":")[0]+":"+str[j].split(":")[1]);
    		    }
    		    map.put(String.valueOf(admname), list);
    		    JSONArray json = JSONArray.fromObject(map);
    		    this.writeData("result", json.toString(), filePath);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}
	
	/**
	 * 将需要加入购物车的商品信息存入Ali1688_PreOrder_List表中
	 */
	public void AutoAddToCart(){
		ods.autoAddToCart(null);
	}
	
	public void writeData(String key, String value,String fileURL) {  
        Properties prop = new Properties();  
        InputStream fis = null;  
        OutputStream fos = null;  
        try {  
            File file = new File(fileURL);  
            if (!file.exists())  
            	file.deleteOnExit();
                file.createNewFile();  
            fis = new FileInputStream(file);  
            prop.load(fis);  
            fis.close();//一定要在修改值之前关闭fis  
            fos = new FileOutputStream(file);  
            prop.setProperty(key, value);  
            prop.store(fos, "Update '" + key + "' value");  
            fos.close();  
              
        } catch (IOException e) {  
            System.out.println("Visit " + fileURL + " for updating "  
            + value + " value error");  
        }  
        finally{  
            try {  
                fos.close();  
                fis.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    } 
	

}
