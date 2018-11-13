package com.cbt.newProduct.controller;

import com.alibaba.fastjson.JSON;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.newProduct.pojo.NewProduct;
import com.cbt.newProduct.service.NewProductService;
import com.cbt.util.SpiderAliexpress;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 新品区
 * @author admin
 *
 */
@RequestMapping("newProductController")
@Controller
public class NewProductController {

	@Autowired
	private NewProductService newProductService ;
	
	
	@Autowired
	private SpiderAliexpress spiderAliexpress;
	/**
	 * 添加新品
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addNewProductInfo")
	@ResponseBody
	public String  addNewProductInfo(HttpServletRequest request, HttpServletResponse response){
		String  createtime = request.getParameter("createtime");
	    String  category = request.getParameter("category");
	    String  cid = request.getParameter("cid");
	    String  purl = request.getParameter("url");
	    purl = purl.contains("?")?purl.substring(0, purl.indexOf("?")):purl;
	    int ret = 2;
	    try{
		    SendMQ sendMQ = new SendMQ();
			//判断是否重复插入
			if(newProductService.checkUrl(purl)==0){
				NewProduct bean  = new NewProduct();
				bean.setCreatetime(createtime);
				bean.setCategory(category);
				bean.setCid(cid);
				bean.setGoods_url(purl);
				//根据url链接抓取aliexpress商品数据
				bean = spiderAliexpress.getSpider(bean);
				if(bean!=null){
					newProductService.addNewProduct(bean);
				}
				DataSourceSelector.set("dataSource127hop");
				if(newProductService.checkUrl(purl)==0){
					if(bean!=null){
						sendMQ.sendMsg(new RunSqlModel("insert  into new_product_data (cid,category,goods_pid,goods_name,goods_price,goods_img,goods_url,goods_sold,goods_morder   " +
								",goods_free,goods_price_unit,goods_unit,createtime) values('"+bean.getCid()+"','"+bean.getCategory()+"','"+bean.getGoods_pid()+"','"+bean.getGoods_name()+"' " +
								",'"+bean.getGoods_price()+"' ,'"+bean.getGoods_img()+"' ,'"+bean.getGoods_url()+"' ,'"+bean.getGoods_sold()+"' ,'"+bean.getGoods_morder()+"'   " +
								" ,'"+bean.getGoods_free()+"' ,'"+bean.getGoods_price_unit()+"' ,'"+bean.getGoods_unit()+"' ,'"+bean.getCreatetime()+"')"));
						ret=1;
					}
				}
			}
		    sendMQ.closeConn();
		}catch (Exception e){
	    	e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return  String.valueOf(ret);
	}
	
	
	/**
	 * 根据category 筛选上传的新品数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showProductByCategory")
	public  String  showProductByCategory(HttpServletRequest request, HttpServletResponse response){
		String  cid = request.getParameter("cid");
		String  createtime = request.getParameter("createtime");
		//根据时间和类别获取该类别的数据
		List<NewProduct>  dataList = newProductService.findNewProductData(cid,createtime);
		//根据时间获取当天各个类别的次数
		List<NewProduct>  categoryList = newProductService.showCategoryData(createtime);
		request.setAttribute("createtime", createtime);
		request.setAttribute("cid", cid);
		request.setAttribute("dataList", dataList);
		request.setAttribute("categoryList", categoryList);
		return  "newProduct";
	}
	
	  @RequestMapping("getAllCategory")
	  @ResponseBody
	  public  String  getAllCategory(HttpServletRequest request, HttpServletResponse response){
		 List<NewProduct>   list = newProductService.getAllCategory();
		 String json = JSON.toJSONString(list);
		 return   json;
	  }
	
	/**
	 * 批量新增
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("pLAddNewProduct")
	@ResponseBody
	public  String  pLAddNewProduct(HttpServletRequest request, HttpServletResponse response){
        String  createtime  = request.getParameter("createtime");
        String  category  = request.getParameter("category");
        String  cid  = request.getParameter("cid");
        String  url  = request.getParameter("url");
        String[]  urls = url.split("##");
        List<NewProduct>  list = new ArrayList<NewProduct>();
		int ret = 0;
        try{
	        SendMQ sendMQ = new SendMQ();
			for(String  purl:urls){
				purl = purl.contains("?")?purl.substring(0, purl.indexOf("?")):purl;
				//判断是否重复插入
				if(newProductService.checkUrl(purl)==0){
					NewProduct bean  = new NewProduct();
					bean.setCreatetime(createtime);
					bean.setCategory(category);
					bean.setCid(cid);
					bean.setGoods_url(purl);
					//根据url链接抓取aliexpress商品数据
					bean = spiderAliexpress.getSpider(bean);
					if(bean!=null){
						list.add(bean);
					}
				}
			}
			for(NewProduct bean:list){
				newProductService.addNewProduct(bean);
			}
			//**切换数据源：插入线上
			DataSourceSelector.set("dataSource127hop");
			for(NewProduct bean:list){
				if(newProductService.checkUrl(bean.getGoods_url())==0){
					sendMQ.sendMsg(new RunSqlModel("insert  into new_product_data (cid,category,goods_pid,goods_name,goods_price,goods_img,goods_url,goods_sold,goods_morder   " +
							",goods_free,goods_price_unit,goods_unit,createtime) values('"+bean.getCid()+"','"+bean.getCategory()+"','"+bean.getGoods_pid()+"','"+bean.getGoods_name()+"' " +
							",'"+bean.getGoods_price()+"' ,'"+bean.getGoods_img()+"' ,'"+bean.getGoods_url()+"' ,'"+bean.getGoods_sold()+"' ,'"+bean.getGoods_morder()+"'   " +
							" ,'"+bean.getGoods_free()+"' ,'"+bean.getGoods_price_unit()+"' ,'"+bean.getGoods_unit()+"' ,'"+bean.getCreatetime()+"')"));
					ret+=1;
				}
			}
	        sendMQ.closeConn();
		}catch (Exception e){
        	e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return  String.valueOf(ret);
	}
	
	
	
	 
	

	
	/**
	 * 批量下架线上商品
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("pdownProducts")
	public  String  pDelProducts(HttpServletRequest request, HttpServletResponse response){

		
		return  null;
	}
	
	
	/**
	 * 下架线上商品
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("down")
	@ResponseBody
	public  String  down(HttpServletRequest request, HttpServletResponse response){
		int ret=0;
		try{
			SendMQ sendMQ = new SendMQ();
			String pid = request.getParameter("pid");
			ret = newProductService.down(pid);
			sendMQ.sendMsg(new RunSqlModel("update  new_product_data  set  goods_flag = 2  where  goods_pid = '"+pid+"'"));
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return  String.valueOf(ret);
	}
	
}
