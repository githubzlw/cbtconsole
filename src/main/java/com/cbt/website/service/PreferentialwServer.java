package com.cbt.website.service;

import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.PreferentialWeb;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;
import com.cbt.website.dao.PreferentialDao;
import com.cbt.website.dao.WebsitePreferentialDao;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreferentialwServer implements IPreferentialwServer {

	private WebsitePreferentialDao dao = new PreferentialDao();
	@Override
	public List<PreferentialWeb> getPreferentials(int type, int userid, int page, String hdate, String cdate, String email, int adminid) {
		return  dao.getPreferentials(type, userid, (page-1)*40, 40,hdate,cdate,email,adminid);
	}
	
	@Override
	public int savePai(String ids, String gids, String prices, String title,String sprices,String number,int userid,String emial,
			int confirm,String eend,String sessionid,String username,String content,String currency,String imgs,String question) {
		//保存交互信息
		List<Object[]> list = new ArrayList<Object[]>();
		String[] ids_ = ids.split("@");
		String[] gids_ = gids.split("@");
		String gid = gids.split("@")[0];
		String[] prices_ = prices.split("@");
		String[] title_ = title.split("@.@");
		String[] sprices_ = sprices.split("@");
		String[] number_ = number.split("@");
		String[] eend_ = eend.split("@");
		String[] img_ = imgs.split("@");
		String[] question_ = null;
		if (question != null) {
			question_ = question.split("@");
		}
		//String[] question_ = question.split("@");
		String idsString = new Date().getTime()+""+userid;
		for (int i = 0; i < ids_.length; i++) {
			Object[] objects = {ids_[i],gids_[i],prices_[i],title_[i],sprices_[i],number_[i],eend_[i],img_[i]};
			list.add(objects);
		}
		int res = dao.savePai(list,idsString,userid,sessionid);
		/*//修改优惠申请的确认信息
		if(confirm == 1){
			dao.upPaconfirm(ids.split("@"));
		}*/
		Double allSaveMoney = 0.0;
		for (int i = 0; i < ids_.length; i++) {
			double oldMoney=Double.parseDouble(prices_[i]);
			double newMoney=Double.parseDouble(sprices_[i]);
			int count = Integer.parseInt(number_[i]);
//			allSaveMoney += (oldMoney-newMoney) * count;
			allSaveMoney += (newMoney-oldMoney) * count;
		}
		String uuid = UUIDUtil.getEffectiveUUID(0, emial);
		String path = "";
		try {
			path = UUIDUtil.getAutoLoginPath("/individual/getCenter#"+gid, uuid);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(res > 0){
			//发送邮件信息
			if(!Utility.getStringIsNull(username)){
				username = "Valued Customer";
			}
			DecimalFormat df = new DecimalFormat("######0.00");
			StringBuffer sbBuffer = new StringBuffer();
			sbBuffer.append("Thanks for interested in shopping with us. <br/>");
			sbBuffer.append("We have reduced price for your ImportExpress volume discount application !<Strong>Total saving:"
//			+currency.split("@")[0]+df.format(allSaveMoney)+"</Strong><a href='http://www.import-express.com"+path+">Details</a> <br/>");
			+currency.split("@")[0]+df.format(allSaveMoney)+"</Strong> <a href='"+AppConfig.ip_email+"' style='text-decoration: underline;' >Details</a> <br/>");
			sbBuffer.append("<br><table cellpadding='0' cellspacing='0' >");
			sbBuffer.append("<tr align='center' style='font-weight: bold;'><td style='border: 1px solid #B7B7B7;border-right:0px;width: 500px;'>Item</td><td style='border: 1px solid #B7B7B7;'>Quantity</td><td style='border: 1px solid #B7B7B7;border-right:0px;'>Original price</td><td style='border: 1px solid #B7B7B7;border-right:0px;'>New price</td><td style='border: 1px solid #B7B7B7;'>Discount</td><tr>");
			for (int i = 0; i < list.size(); i++) {
				double prices_d =  Double.parseDouble(list.get(i)[2].toString());
				double sprices_d =  Double.parseDouble(list.get(i)[4].toString());
				int s = (int) ((sprices_d-prices_d)/sprices_d*100);
				if(s<0)s=0;
				sbBuffer.append("<tr align='center'><td style='border-bottom: 1px solid #B7B7B7;border-left: 1px solid #B7B7B7;width:450px;height:120px;position:relative'>");
				System.out.println(list.get(i)[7]);
				sbBuffer.append("<img src='"+list.get(i)[7]+"' height='100px' width='100px' style='position: absolute;top: 5px;left: 5px;' /><p style='margin-left:115px;text-align:left'>"+list.get(i)[3]+"</p></td>");
				sbBuffer.append("<td style='border-bottom: 1px solid #B7B7B7;border-left: 1px solid #B7B7B7'>");
				sbBuffer.append(list.get(i)[5]);//数量
				sbBuffer.append("</td><td style='border-bottom: 1px solid #B7B7B7;border-left: 1px solid #B7B7B7'>");
				sbBuffer.append("<span style='color:red'>USD "+df.format(sprices_d)+"</span>");
				sbBuffer.append("</td><td style='border-bottom: 1px solid #B7B7B7;border-left: 1px solid #B7B7B7'>");
				sbBuffer.append("<span style='color:green'>USD "+df.format(prices_d)+"</span>"); //
				sbBuffer.append("</td><td style='border-bottom: 1px solid #B7B7B7;border-left: 1px solid #B7B7B7'>");
				sbBuffer.append(s+" %");
				sbBuffer.append("</td><td style='border-bottom: 1px solid #B7B7B7;border-left: 1px solid #B7B7B7;border-right:0px;");
				if(i == list.size()-1){
					sbBuffer.append("border-right: 1px solid #B7B7B7;'>");
				}else{
					sbBuffer.append("'>");
				}
				sbBuffer.append("</td><tr>");
			}
			sbBuffer.append("<tr><td colspan='2'></td><td colspan='3'>Total saving:<span style='color:green'>USD "+df.format(allSaveMoney)+"</span></td></tr>");
			sbBuffer.append("</table><div>");
			if(question_ != null && question_.length>0){
				sbBuffer.append("<br><Strong>Your question:</Strong>"+question_[0]);
				sbBuffer.append("<br><Strong>Answer:</Strong>"+content);
			}
			try {
				path = UUIDUtil.getAutoLoginPath("/preferential/sendcart?uid="+idsString, uuid);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			sbBuffer.append("<br>If you have any question related to our website, you may find answers <a href='http://www.import-express.com/apa/FAQ.html'>here.</a>");
//			sbBuffer.append("<br>You may also contact you client service  manager which displayed on the right side of <a href='"+AppConfig.ip_email+path+"'>your ImportExpress Account.</a></div>");
			sbBuffer.append("<br>You may also contact you client service  manager which displayed on the right side of <a href='"+AppConfig.ip_email+"'>your ImportExpress Account.</a></div>");
			String sendemail = null;
    	    String pwd = null;
    	    if(userid != 0){
    		    IUserDao userDao = new UserDao();
    		    String[] adminEmail =  userDao.getAdminUser(0, null, userid);
    		    if(adminEmail != null){
    			   sendemail = adminEmail[0];
    			   pwd = adminEmail[1];
    		    }
    	    }
    	    sbBuffer = SendEmail.SetContent(username, sbBuffer);
			SendEmail.send(sendemail,pwd, emial, sbBuffer.toString(),"Discount Offer from Import-Express","", 1);
		}
		return res;
	}
	
	
	public int updatePriceById(double price, int id){
		return dao.updatePriceById(price, id);
	}
	
	public int updateGoodsCarPrice(double price, String itemId){
		return dao.updateGoodsCarPrice(price, itemId);
	}
	
	
	
	public int updatePayPrice(String itemId,double payprice){
		return dao.updatePayPrice(itemId, payprice);
	}
	
	
	@Override
	public int delPaprestrain(int pid, String cancel_reason) {
		return dao.delPaprestrain(pid, cancel_reason);
	}
	@Override
	public int uptimePainteracted(int pid, String endtime) {
		return dao.uptimePainteracted(pid, endtime);
	}
	@Override
	public int getPreferentialsNumber(int type, int userid) {
		return dao.getPreferentialsNumber(type, userid);
	}
	
	@Override
	public double getPreferentialsPayPrice(int id) {
		return dao.getPreferentialsPayPrice(id);
	}
	
	
	@Override
	public List<PostageDiscounts> getPostageD(int userid, int type,int page,String email) {
		return dao.getPostageD(userid, type, (page-1)*40, 40,email);
	}
	@Override
	public int upPostageD(int id, int type, String handleman) {
		return dao.upPostageD(id, type, handleman);
	}
	@Override
	public int getPostageDNumber(int userid, int type) {
		return dao.getPostageDNumber(userid, type);
	}

	
}
