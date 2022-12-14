package com.cbt.customer.service;

import com.cbt.bean.GuestBookBean;
import com.cbt.bean.OrderBean;
import com.cbt.customer.dao.GuestBookDaoImpl;
import com.cbt.customer.dao.IGuestBookDao;
import com.cbt.parse.service.TypeUtils;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail1;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.utli.MultiSiteUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestBookServiceImpl implements IGuestBookService{
	@Autowired
	private SendMailFactory sendMailFactory;
	IGuestBookDao dao = new GuestBookDaoImpl();
	@Override
	public int addComment(GuestBookBean gbb) {
		return dao.addComment(gbb);
	}

	@Override
	public List<GuestBookBean> findByPid(String pid,int userId,int start,int number) {
		return dao.findByPid(pid,userId,start,number);
	}

	@Override
	public GuestBookBean findById(int id) {
		return dao.findById(id);
	}
	
	@Override
	public int replyReport(final int id,final String replyContent,final String date,final String name,
			final String qustion,final String email,final int userId,final String sale_email) {
		new Thread(){
			public void run() {
				StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
		        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear Sir/Madam,</div><br><div style='font-size: 13px;'>"); 
		        sb.append("<div >"+replyContent+" </div>"); 
		        sb.append("<br><div style='margin-bottom: 10px;'><span style='font-weight: bold'>Your Question:</span>["+id+"]"+qustion+" </div>"); 
		        //sb.append("<br><div>We hope you enjoy the shopping experience on Import-Express.com!</div><br>");
		        sb.append("<div style='style='font-weight: bold''>Best regards, </div><div style='font-weight: bold'><a href='http://www.import-express.com'>www.Import-Express.com</a></div></div>");
		    	   String sendemail = null;
		    	   String pwd = null;
		    	   if(userId != 0){
		    		   IUserDao userDao = new UserDao();
		    		   String[] adminEmail =  userDao.getAdminUser(0, null, userId);
		    		   if(adminEmail != null){
		    			   sendemail = adminEmail[0];
		    			   pwd = adminEmail[1];
		    		   }
		    	   }
				   SendEmail1.send(sendemail,pwd,email, sb.toString(),"["+id+"]"+"Inquiry Reply From ImportExpress","", 1,"sale1@import-express.com");
			};
		}.start();
		return Utility.getStringIsNull(sale_email)?dao.replyReport(id, replyContent,date):0;
	}
	/**
	 * ????????????????????????????????????
	 * @return
	 */
	@Override
	public int SendEmailForBatck(OrderBean ob) {
		return 0;
	}

	@Override
	public int replyReportQes(final int id,final String replyContent,final String date,final String name,
			final String qustion,final String email,final int userId,final String sale_email,String url) {
		Map<String,Object> model = new HashMap<>();
		new Thread(){
			public void run() {
				StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
		        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear Sir/Madam,</div><br><div style='font-size: 13px;'>"); 
		        sb.append("<div >"+replyContent+" </div>"); 
		        sb.append("<br><div style='margin-bottom: 10px;'><span style='font-weight: bold'>Your Question:</span>["+id+"]"+qustion+" </div>");
				sb.append("<br><div style='color:red;'>On this page: <a target='_blank' href='https://www.import-express.com/goodsinfo/...-1"+url+".html'>https://www.import-express.com/goodsinfo/...-1"+url+".html</a></div><br>");
		        //sb.append("<br><div>We hope you enjoy the shopping experience on Import-Express.com!</div><br>");
		        sb.append("<div style='style='font-weight: bold''>Best regards, </div><div style='font-weight: bold'><a href='http://www.import-express.com'>www.Import-Express.com</a></div></div>");
		    	   String sendemail = null;
		    	   String pwd = null;
		    	   if(userId != 0){
		    		   IUserDao userDao = new UserDao();
		    		   String[] adminEmail =  userDao.getAdminUser(0, null, userId);
		    		   if(adminEmail != null){
		    			   sendemail = adminEmail[0];
		    			   pwd = adminEmail[1];
		    		   }
		    	   }
				//SendEmail1.send(sendemail,pwd,email, sb.toString(),"["+id+"]"+"Inquiry Reply From ImportExpress","", 1,StringUtils.isStrNull(sale_email)?"sale1@import-express.com":sale_email);
				String urlTem = "https://www.import-express.com/goodsinfo/...-1"+url+".html";
				model.put("email",email);
				model.put("replyContent",replyContent);
				model.put("id",id);
				model.put("qustion",qustion);
				model.put("itemUrl",urlTem);

				sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "["+id+"]"+"Inquiry Reply From ImportExpress", model, TemplateType.BUSINESS_INQUIRIES);
			};
		}.start();
		return Utility.getStringIsNull(sale_email)?dao.replyReport(id, replyContent,date):0;
	}

	@Override
	public int reply(final int id,final String replyContent,final String date,final String name,final String qustion,final String pname,
	                 final String email,final int userId,final String purl,final String sale_email,final String picPath) {
        // ??????????????? ????????????????????? ????????? com.importExpress.service.impl.TabCouponServiceImpl.SendGuestbook
//		new Thread(){
//			public void run() {
//				StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
//		        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear Sir/Madam,</div><br><div style='font-size: 13px;'>");
//		        sb.append("<div >"+replyContent+" </div>");
//		        sb.append("<br><div style='margin-bottom: 10px;'><span style='font-weight: bold'>Your Question:</span>["+id+"]"+qustion+" </div>");
//		        sb.append("<div style='margin-bottom: 10px;'><span style='font-weight: bold'>Item:</span><a href='"+purl+"'> "+pname+"</a></div>");
//		        if(StringUtil.isNotBlank(picPath)){
//			        sb.append("<br><img src='"+picPath+"'></img><br>");
//		        }
//		        //sb.append("<br><div>We hope you enjoy the shopping experience on Import-Express.com!</div><br>");
//		        sb.append("<div style='style='font-weight: bold''>Best regards, </div><div style='font-weight: bold'><a href='http://www.import-express.com'>www.Import-Express.com</a></div></div>");
//		       if(Utility.getStringIsNull(email)){
//		    	   String sendemail = null;
//		    	   String pwd = null;
//		    	   if(userId != 0){
//		    		   IUserDao userDao = new UserDao();
//		    		   String[] adminEmail =  userDao.getAdminUser(0, null, userId);
//		    		   if(adminEmail != null){
//		    			   sendemail = adminEmail[0];
//		    			   pwd = adminEmail[1];
//		    		   }
//		    	   }
//				        SendEmail1.send(sendemail,pwd,email, sb.toString(),"["+id+"]"+"Inquiry Reply From ImportExpress","", 1,sale_email);
//		       }
//			};
//		}.start();
		return dao.reply(id, replyContent,date);
	}
	/**
	 * ????????????:??????ID????????????
	 * author:lizhanjun
	 * date:2015???4???24???
	 * @return
	 */
	@Override
	public GuestBookBean getGuestBookBean(String gid) {
		return dao.getGuestBookBean(gid);
	}

	@Override
	public int delreply(int id) {
		return dao.delreply(id);
	}

	@Override
	public List<GuestBookBean> findAll(int userId, String date, int state,
			String userName, String pname, int start, int end,String useremail,String TimeFrom,String TimeTo,int adminid,int type) {
	    List<GuestBookBean>   list = dao.findAll(userId, date, state, userName, pname, start, end,useremail,TimeFrom,TimeTo,adminid,type);;
		//????????????????????????
	    if(type==0){
			if(list!=null){
				for(GuestBookBean bean:list){
					String  content = "";
					//questionType : 1 question ???2 bussiness discount;3 customization
					int  questionType = bean.getQuestionType();
					if(questionType==1){
						content = bean.getContent();
					}
					if(questionType==2){
						content = "orderQuantity: "+bean.getOrderQuantity()+"; targetPrice: "+bean.getTargetPrice() ;
					}
					if(questionType==3){
						content ="orderQuantity: "+bean.getOrderQuantity()+"; customizationNeed: "+bean.getCustomizationNeed();
					}
					bean.setContent(content);
				}
			}
		}
		// ?????????????????????????????????
		for (GuestBookBean gBookBean : list) {
		    gBookBean.setSite(MultiSiteUtil.getSiteTypeNumByUrl(gBookBean.getOnlineUrl()));
            gBookBean.setSiteStr(MultiSiteUtil.getSiteTypeStrByNum(gBookBean.getSite()));

			gBookBean.setUserInfos("<a target='_blank' href='/cbtconsole/website/user.jsp?userid="
					+ gBookBean.getUserId() + "'>" + gBookBean.getUserId() + "</a>");

			String reply = "<button onclick=\"reply(\'" + gBookBean.getId() + "\', \'" + gBookBean.getSite() + "\')\">??????</button>";
			if (gBookBean.getEid() != 0) {
				gBookBean.setStatusinfo(reply + "<button onclick=\"replyrecord(" + gBookBean.getId() + "," + adminid
						+ ")\">????????????</button><button onclick=\"delreply(" + gBookBean.getId() + ")\">??????</button>");
			} else {
				gBookBean.setStatusinfo(reply + "<button onclick=\"delreply(" + gBookBean.getId() + ")\">??????</button>");
			}

			if (gBookBean.getBname() != null && !"".equals(gBookBean.getBname())) {
				gBookBean.setEmail(gBookBean.getEmail()
						+ "<br><span style='color:red'>BusinessName:</span><span style='color:green'>"
						+ gBookBean.getBname() + "</span>");
			} else {
				gBookBean.setEmail(gBookBean.getEmail() + "<br>");
			}
			gBookBean.setPname("<span title='" + gBookBean.getPname() + "'></span><a href='" + gBookBean.getOnlineUrl()
					+ "'  target='_blank'>" + gBookBean.getPname().substring(0, +gBookBean.getPname().length() / 3)
					+ "...</a>");
			if (gBookBean.getOnlineUrl() == null || "".endsWith(gBookBean.getOnlineUrl())) {
				String onlineUrl = TypeUtils.encodeGoods(gBookBean.getPurl());// ??????????????????????????????
				gBookBean.setOnlineUrl("https://www.import-express.com/spider/getSpider?" + onlineUrl);
			}
			StringBuilder path=new StringBuilder();
			if(StringUtil.isNotBlank(gBookBean.getPicPath())){
				path.append("<img src=\""+gBookBean.getPicPath()+"\" style='width:60px;height:60px' onmouseout=\"closeBigImg();\" onmouseover='BigImg(\""+gBookBean.getPicPath()+"\");'></img>");
			}
			gBookBean.setPicPath(path.toString());
		}
	    return  list ;
	}

	@Override
	public int total(int userId, String date, int state,
			String userName, String pname, int start, int end,String useremail,String TimeFrom,String TimeTo,int adminid,int type) {
		return dao.total(userId, date, state, userName, pname, start, end,useremail,TimeFrom,TimeTo,adminid,type);
	}
	

}
