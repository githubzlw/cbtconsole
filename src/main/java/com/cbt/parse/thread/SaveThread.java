package com.cbt.parse.thread;

import com.cbt.parse.dao.GoodsHtmlDao;
import com.cbt.parse.dao.SaveKeyDao;
import com.cbt.parse.daoimp.IGoodsHtmlDao;
import com.cbt.parse.daoimp.ISaveKeyDao;
import com.cbt.parse.service.SaveHtml;

/**flag;//标志  true是添加到数据库，false 修改数据库
 * com_flag;//标志  true是商品单页，false 搜索页面
 * 
 * @author abc
 *
 */
public class SaveThread extends Thread{
	private Boolean flag;//标志  true是添加到数据库，false 修改数据库
	private Boolean com_flag;//标志  true是商品单页，false 搜索页面
	private String url;//要保存成静态页的链接
	private String keyword;//关键词
	private String website;//来源网站
	private String path;//保存路径
	
	public SaveThread() {
		
	}
	public SaveThread(Boolean com_flag,String url,String keyword,String website,String path,Boolean flag){
		this.url = url;
		this.keyword = keyword;//搜索关键词
		this.website = website;//搜索来源网站
		this.path = path;//保存路径
		this.flag = flag;//数据表中是否有记录，有则更新
		this.com_flag = com_flag;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			//商品单页静态
			if(com_flag){
				SaveHtml saveHtml =  new SaveHtml();
				Boolean isSaved = saveHtml.saveGoods(url,path,com_flag);
				IGoodsHtmlDao dao = new GoodsHtmlDao();
				if(flag&&isSaved){
					//静态保存成功， 存入数据库表
					dao.add(keyword, path, website);
				}else if((!flag)&&isSaved){
					//静态保存成功， 修改数据库表
					dao.update(keyword, path, website);
				}
				dao = null;
				saveHtml = null;
			}else{
				//搜索静态页面
				SaveHtml saveHtml =  new SaveHtml();
				Boolean isSaved = saveHtml.saveSearch(url,path,com_flag);
				ISaveKeyDao sd = new SaveKeyDao();//数据库对象
				if(flag&&isSaved){
					//静态保存成功， 存入数据库表
					sd.addData(keyword, website, path, url,"1");
				}else if((!flag)&&isSaved){
					//静态保存成功， 修改数据库表
					sd.updateData(keyword, website, path, url,"1");
				}
				sd = null;
				saveHtml = null;
			}
		} catch (Exception e) {
		} catch (Throwable e) {
		}
	}

}
