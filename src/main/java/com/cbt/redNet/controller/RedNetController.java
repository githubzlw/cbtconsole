package com.cbt.redNet.controller;

import com.alibaba.fastjson.JSON;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.StrUtils;
import com.cbt.redNet.pojo.redNet;
import com.cbt.redNet.pojo.redNetStatistics;
import com.cbt.redNet.service.RedNetService;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 网红后台管理
 * @author wkk
 *
 */
@Controller
@RequestMapping("redNet")
public class RedNetController {

	
	@Autowired
	private   RedNetService  redNetService ;
	
	
	/**
	 * 网红统计
	 * 按条件显示所有网红统计数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showRedNetStatistics")
	@ResponseBody
	public  String   showRedNetStatistics(HttpServletRequest request , HttpServletResponse response){
		//网红id
		String  redNetId = request.getParameter("redNetId");
		//发文时间
		String  pushTime  = request.getParameter("pushTime");
		//创建开始时间
		String startTime  = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String  page = request.getParameter("page");
		if(StrUtils.isNullOrEmpty(page)){
			page ="1";
		}
		redNetId = redNetId==""?null:redNetId;
		pushTime = pushTime==""?null:pushTime;
		startTime = startTime==""?null:startTime;
		endTime = endTime==""?null:endTime;
		List<redNetStatistics>    list =  redNetService.showRedNetStatistics(redNetId,pushTime,startTime,endTime,Integer.parseInt(page));
		if(list.size()>0){
			int totalcount  = list.get(0).getCount();
			int  count  = totalcount/30;
			int  totalPage = totalcount%30==0?count:count+1;
			list.get(0).setTotalPage(totalPage);
			list.get(0).setCurrentPage(Integer.parseInt(page));
		}
		String  json = JSON.toJSONString(list);
		return json;
	}
	
	
	/**
	 * 网红管理
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showRedNetInfo")
	@ResponseBody
	public  String  showRedNetInfo(HttpServletRequest request , HttpServletResponse response){
		//网红id
		String  redNetId = request.getParameter("redNetId");
		//网红名称
		String  redNetName = request.getParameter("redNetName");
		//博客/链接
		String  site = request.getParameter("site");
		String  page = request.getParameter("page");
		redNetId =redNetId==""?null:redNetId;
		redNetName =redNetName==""?null:redNetName;
		site =site==""?null:site;
		if(StrUtils.isNullOrEmpty(page)){
			page ="1";
		}	
		List<redNet>   list = redNetService.showRedNetInfo(redNetId,redNetName,site,Integer.parseInt(page));
		if(list.size()>0){
			int  totalCount = list.get(0).getCount();
			int  count = totalCount/30;
			int  totalPage =totalCount%30==0?count:count+1;
			list.get(0).setCurrentPage(Integer.parseInt(page));
			list.get(0).setTotalPage(totalPage);
		}
		String json = JSON.toJSONString(list);
		return json;
	}
	
	
	/**
	 * 查询所有网红
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showAllRedNet")
	@ResponseBody
	public  String  showAllRedNet(HttpServletRequest request , HttpServletResponse response){
		List<redNet>   list = redNetService.showAllRedNet();
        String  json = JSON.toJSONString(list);
		return  json;
	}
	
	
	/**
	 * 
	 * 根据网红id 查询 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showRedNetById")
	@ResponseBody
	public  String  showRedNetById(HttpServletRequest request , HttpServletResponse response){
		String   id = request.getParameter("id");
		redNet bean = redNetService.showRedNetById(Integer.parseInt(id));
		String  cooperationTime  = bean.getCooperationTime().trim();
		cooperationTime = cooperationTime.substring(0,cooperationTime.indexOf(" ")).trim();
		bean.setCooperationTime(cooperationTime);
		String  json  = JSON.toJSONString(bean);
		return    json;
	}
	
	
	/**
	 * 
	 * 根据网红统计表 id 查询 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showRedNetStatisticsById")
	@ResponseBody
	public  String  showRedNetStatisticsById(HttpServletRequest request , HttpServletResponse response){
		String   id = request.getParameter("id");
		redNetStatistics bean = redNetService.showRedNetStatisticsById(Integer.parseInt(id));
		String  pushTime  = bean.getPushTime().trim();
		pushTime = pushTime.substring(0,pushTime.indexOf(" ")).trim();
		bean.setPushTime(pushTime);
		String  json  = JSON.toJSONString(bean);
		return    json;
	}
	
	
	
	/**
	 * 添加网红
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addRedNet")
	@ResponseBody
	public  String  addRedNet(HttpServletRequest request , HttpServletResponse response){
		DataSourceSelector.restore();
		SimpleDateFormat  sdf  = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String  id = request.getParameter("id");
		//网红名
		String  redNetName = request.getParameter("redNetName");  
		//博客
		String  site = request.getParameter("site");
        //网红Tomoson资质评估链接
		String  tomosonUrl = request.getParameter("tomosonUrl");
		 //发文次数
		String  pushSum = request.getParameter("pushSum");
		 //价格
		String  redNetOffer = request.getParameter("redNetOffer");
		//邮件
		String  email = request.getParameter("email");
		 //合作时间
		String  cooperationTime = request.getParameter("cooperationTime");
		 //备注
		String  bz = request.getParameter("bz");
		Date  date  = new  Date();
		int ret = 0 ;
		try{
			redNet bean = new redNet();
			bean.setRedNetName(redNetName);
			bean.setSite(site);
			bean.setTomosonUrl(tomosonUrl);
			bean.setPushSum(pushSum==""?"0":pushSum);
			bean.setRedNetOffer(redNetOffer==""?"0":redNetOffer);
			bean.setEmail(email);
			bean.setCooperationTime(cooperationTime==""?sdf.format(date):cooperationTime);
			bean.setBz(bz);
			if(StrUtils.isNotNullEmpty(id)){
				bean.setId(Integer.parseInt(id));
				ret = redNetService.updateRedNet(bean);
			}else{
				if(redNetService.selectRedNetByName(redNetName)>0){
					return String.valueOf(2);
				}
				ret = redNetService.addRedNet(bean);
			}
			SendMQ sendMQ = new SendMQ();
			StringBuilder sql=new StringBuilder();
			//切换数据源
			if(StrUtils.isNotNullEmpty(id)){
				bean.setId(Integer.parseInt(id));
				sql.append(" update  rednet  set");
				if(StringUtil.isNotBlank(bean.getRedNetName())){
					sql.append("redNetName ='"+bean.getRedNetName()+"' ,");
				}
				if(StringUtil.isNotBlank(bean.getSite())){
					sql.append("site ='"+bean.getSite()+"' ,");
				}
				if(StringUtil.isNotBlank(bean.getTomosonUrl())){
					sql.append("tomosonUrl ='"+bean.getTomosonUrl()+"' ,");
				}
				if(StringUtil.isNotBlank(bean.getPushSum())){
					sql.append("pushSum ='"+bean.getPushSum()+"' ,");
				}
				if(StringUtil.isNotBlank(bean.getRedNetOffer())){
					sql.append("redNetOffer ='"+bean.getRedNetOffer()+"' ,");
				}
				if(StringUtil.isNotBlank(bean.getEmail())){
					sql.append("email ='"+bean.getEmail()+"' ,");
				}
				if(StringUtil.isNotBlank(bean.getCooperationTime())){
					sql.append("cooperationTime ='"+bean.getCooperationTime()+"' ,");
				}
				if(StringUtil.isNotBlank(bean.getBz())){
					sql.append("bz ='"+bean.getBz()+"' ,");
				}
				sql.append(" where  id  = '"+bean.getId()+"'");
				sendMQ.sendMsg(new RunSqlModel(sql.toString()));
			}else{
				DataSourceSelector.set("dataSource127hop");
				if(redNetService.selectRedNetByName(redNetName)>0){
					return String.valueOf(2);
				}
				sql.append("insert into  rednet(redNetName,site,tomosonUrl,pushSum,redNetOffer,email,cooperationTime,bz) " +
						"values('"+bean.getRedNetName()+"','"+bean.getSite()+"','"+bean.getTomosonUrl()+"','"+bean.getPushSum()+"','"+bean.getRedNetOffer()+"','"+bean.getEmail()+"','"+bean.getCooperationTime()+"','"+bean.getBz()+"')");
				sendMQ.sendMsg(new RunSqlModel(sql.toString()));
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}

		return   String.valueOf(ret);
	}
	

	/**
	 * 修改网红
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateRedNet")
	public  String  updateRedNet(HttpServletRequest request , HttpServletResponse response){
		String  id = request.getParameter("id");
		//网红名
		String  redNetName = request.getParameter("redNetName");  
		//博客
		String  site = request.getParameter("site");
        //网红Tomoson资质评估链接
		String  tomosonUrl = request.getParameter("tomosonUrl");
		 //发文次数
		String  pushSum = request.getParameter("pushSum");
		 //价格
		String  redNetOffer = request.getParameter("redNetOffer");
		//邮件
		String  email = request.getParameter("email");
		 //合作时间
		String  cooperationTime = request.getParameter("cooperationTime");
		 //备注
		String  bz = request.getParameter("bz");
		
		redNet bean = new redNet();
		bean.setId(Integer.parseInt(id));
		bean.setRedNetName(redNetName);
		bean.setSite(site);
		bean.setTomosonUrl(tomosonUrl);
		bean.setPushSum(pushSum);
		bean.setRedNetOffer(redNetOffer);
		bean.setEmail(email);
		bean.setCooperationTime(cooperationTime);
		bean.setBz(bz);
		
		int ret = redNetService.updateRedNet(bean);
		return   String.valueOf(ret);
	}
	
	
	/**
	 * 生成shareId 后分配给网红
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("createShareIdToRedNet")
	@ResponseBody
	public  String  createShareIdToRedNet(HttpServletRequest request , HttpServletResponse response){
		//生成网红
		String shareId = System.currentTimeMillis() + Utility.genNumericalRandom(6);
        //返回页面
		String  json = JSON.toJSONString(shareId);
		return  json;
	}
	
	/**
	 * 分配shareId给具体网红
	 * 保存数据库
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addRedNetStatistics")
	@ResponseBody
	public  String   addRedNetStatistics(HttpServletRequest request, HttpServletResponse response){
		DataSourceSelector.restore();
		SimpleDateFormat  sdf  = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String   id = request.getParameter("id");
		String   shareId =request.getParameter("shareId");
		String   redNetId =request.getParameter("redNetId");
		String   publishAddress =request.getParameter("publishAddress");
		String   pushTime =request.getParameter("pushTime");
		String   bz =request.getParameter("bz");
		int  ret =  0;
		try{
			DataSourceSelector.restore();
			Date  date  = new  Date();
			redNetStatistics bean = new redNetStatistics();
			pushTime = pushTime==""?sdf.format(date):pushTime+" 00:00:00";
			bean.setCreateTime(sdf.format(date));
			bean.setShareId(shareId);
			bean.setRedNetId(Integer.parseInt(redNetId));
			bean.setPublishAddress(publishAddress);
			bean.setPushTime(pushTime);
			bean.setBz(bz);
			if(StrUtils.isNotNullEmpty(id)){
				bean.setId(Integer.parseInt(id));
				ret = redNetService.updateRedNetStatistics(bean);
			}else{
				//存储数据库的同时,也要往线上同步该数据
				if(redNetService.selectRedNetStatistics(shareId)>0){
					return  String.valueOf(2);
				}
				ret = redNetService.addRedNetStatistics(bean);
			}

			//切换数据源
			DataSourceSelector.set("dataSource127hop");
			if(StrUtils.isNotNullEmpty(id)){
				bean.setId(Integer.parseInt(id));
				ret = redNetService.updateRedNetStatistics(bean);
			}else{
				//存储数据库的同时,也要往线上同步该数据
				if(redNetService.selectRedNetStatistics(shareId)>0){
					return  String.valueOf(2);
				}
				ret = redNetService.addRedNetStatistics(bean);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return String.valueOf(ret);
	}
}
