package com.cbt.parse.thread;

import com.cbt.parse.bean.GoodsDaoBean;
import com.cbt.parse.bean.TypeBean;
import com.cbt.parse.dao.GoodsDao;
import com.cbt.parse.daoimp.IGoodsDao;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.TypeUtils;
import com.cbt.util.Md5Util;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GoodsThread extends Thread {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsThread.class);
	private IGoodsDao gd = new GoodsDao();
	private GoodsBean goods;
	private Boolean isSource;//货源商品
	
	public GoodsThread() {
	}
	public  GoodsThread(GoodsBean goods, boolean isSource){
		this.goods = new GoodsBean();
		this.goods = goods;
		this.isSource = isSource;
	}
	
	@Override
	public synchronized void run() {
		addUpdate(goods,isSource);
		this.goods = null;
	}
	/**添加  更新数据库
	 * @return
	 */
	private synchronized void addUpdate(GoodsBean goods, boolean isSource){
		long st = new Date().getTime();
		GoodsDaoBean bean = new GoodsDaoBean();
		if(goods==null){
			return;
		}
		String purl = goods.getpUrl();
		if(goods.isEmpty()||goods.getValid()==0){
			//无数据
			//表示原来数据库中已经有数据，但是现在商品下架了
			return ;
		}
		//商品链接
		purl = purl!=null?TypeUtils.modefindUrl(purl,1):purl;
		bean.setUrl(purl);
		//商品pid
		if(purl.indexOf("taobao") > -1){
			bean.setpID("T"+goods.getpID());
		}else if(purl.indexOf("tmall") > -1){
			bean.setpID("L"+goods.getpID());
		}else if(purl.indexOf("detail.1688.com") > -1){
			bean.setpID("D"+goods.getpID());
		}else{
			return ;
		}
		bean.setUuid(Md5Util.encoder(purl));
		
		//商品名称
		String name = goods.getpName();
		name = name!=null&&name.length()>250?name.substring(0,250):name;
		bean.setName(name);
		
		//商品原价
		String oPrice = goods.getpOprice();
		oPrice  =oPrice!=null&&"0".equals(oPrice)?null:oPrice;
		bean.setoPrice(oPrice);
		//商品现价
		String sPrice = goods.getpSprice();
		sPrice  =sPrice!=null&&"0".equals(sPrice)?null:sPrice;
		bean.setsPrice(sPrice);
		//货币单位
		String pUnit = goods.getpPriceUnit();
		pUnit = pUnit!=null&&pUnit.length()>10?pUnit.substring(0, 10):pUnit;
		if(pUnit==null||pUnit.equals("RUB")){
			return ;
		}
		bean.setpUnit(pUnit);
		//售卖单位
		String gUnit = goods.getpGoodsUnit();
		gUnit = gUnit!=null&&gUnit.length()>50?gUnit.substring(0, 50):gUnit;
		if(gUnit==null||gUnit.equals("шт.")){
			return ;
		}
		bean.setgUnit(gUnit);
		//保存图片集合
		ArrayList<String> imgs = goods.getpImage();
		if(imgs!=null){
			while(imgs.size()>3&&imgs.toString().length()>1500){
				imgs.remove(imgs.size()-1);
			}
			bean.setImg(imgs.toString());
		}
		//保存图片尺寸
		String[] imgSizes = goods.getImgSize();
		if(imgSizes!=null&&imgSizes.length==2){
			bean.setImgSize(imgSizes[0]+";"+imgSizes[1]);
		}
		//保存最小订量
		String mOrder = goods.getMinOrder();
		mOrder = mOrder!=null&&mOrder.length()>50?mOrder.substring(0,50):mOrder;
		bean.setmOrder(StringUtils.isEmpty(mOrder)? "1":mOrder);
		//单个商品重量
		//免邮标志
		/*//商品类别链接20151209
		String cateurl = goods.getCateurl();
		bean.setCateurl(cateurl);*/
		//卖家id
		bean.setsID(goods.getsID());
//			bean.setsName(goods.getsName());
		//商品页面title
		//供应商链接
		bean.setsUrl(goods.getSupplierUrl());
		
		//保存商品颜色数据
		ArrayList<TypeBean> type = goods.getType();
		String types = type!=null&&!type.isEmpty()?type.toString():null;
		bean.setTypes(types);
		//保存商品详细图文信息
		bean.setInfo("");
		//保存商品detail数据
		HashMap<String,String> getpDetail = goods.getpInfo();
		String detail = getpDetail!=null&&!getpDetail.isEmpty()?getpDetail.toString():null;
		bean.setDetail(detail);
		//保存商品已经售出数量
		String sell = goods.getSell();
		sell = sell!=null&&sell.length()>250?sell.substring(0,255):sell;
		bean.setSell(sell);
		
		bean.setValid(goods.getValid()+"");
		bean.setFree("1");
		
		//goodsdata 扩展
		//flag true 添加  false 更新
		if(isSource){
			gd.addSourceData(bean);
		}
		
		getpDetail  =null;
		detail = null;
		types = null;
		type = null;
		name = null;
		purl = null;
		oPrice  = null;
		sPrice = null;
		pUnit = null;
		gUnit = null;
		imgs = null;
		imgSizes = null;
		mOrder = null;
		sell  =null;
		goods = null;
		long ed = new Date().getTime();
		LOG.warn("goodsdata insert sql:"+(ed-st));
	}
	
}
