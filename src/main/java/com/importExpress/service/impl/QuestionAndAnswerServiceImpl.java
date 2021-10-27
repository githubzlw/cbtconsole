package com.importExpress.service.impl;

import com.cbt.customer.dao.GuestBookDaoImpl;
import com.cbt.customer.dao.IGuestBookDao;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.mapper.QueAnsMapper;
import com.importExpress.pojo.QueAns;
import com.importExpress.service.QuestionAndAnswerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAndAnswerServiceImpl implements QuestionAndAnswerService {
    @Autowired
    private SendMailFactory sendMailFactory;
	@Autowired
	private QueAnsMapper queAnsMapper;
    IGuestBookDao dao = new GuestBookDaoImpl();
	@Override
	public List<QueAns> findByQuery(String goodsPid, String goodsName,
                                    int adminId, int replyFlag, int replyStatus,String startdate, String enddate, int page) {
		List<QueAns> list=queAnsMapper.findByQuery(goodsPid, goodsName, adminId,replyFlag,replyStatus, startdate,enddate, page);
		for(QueAns q:list){
            String purl = q.getPurl();
            if (StringUtils.isNotBlank(purl) && !purl.startsWith("http")){
                q.setPurl("https://www.importx.com" + purl);
            }
            if(StringUtil.isNotBlank(q.getReply_content())){
				q.setReply_content(q.getReply_content().replace("\n",""));
			}
			String flag="0";
			if(StringUtil.isBlank(q.getReply_content())){
				flag="1";
			}
			q.setContextFlag(flag);
		}
		return list;
	}

	@Override
	public int getCountByQuery(String goodsPid, String goodsName, int adminId,
			int replyFlag, int replyStatus, String startdate,String enddate) {
		
		return queAnsMapper.getCountByQuery(goodsPid, goodsName, adminId, replyFlag, replyStatus, startdate,enddate);
	}
	@Override
	public int changeIsShow(String pid, String type) {
		
		return queAnsMapper.changeIsShow(pid, type);
	}
	@Override
	public QueAns getQueAnsinfo(int qid) {
		
		return queAnsMapper.getQueAnsinfo(qid);
	}

	@Override
	public int deleteQuestion(int pid) {
		return queAnsMapper.deleteQuestion(pid);
	}

	@Override
	public int influenceShop(int qid, String shop_id,String state) {
		
		return queAnsMapper.influenceShop(qid,shop_id,state);
	}
	@Override
	public int updateReplyContent(int qid, int adminId, String rContent,String isShow,String shop_id) {
		
		return queAnsMapper.updateReplyContent(qid, adminId, rContent,isShow,shop_id);
	}

	@Override
	public int updateRemark(int qid, int adminId, int isReview,
			String reviewRemark) {
		
		return queAnsMapper.updateRemark(qid, adminId, isReview, reviewRemark);
	}
    @Override
    public int replyReportQes(final int id,final String replyContent,final String date,final String name,
                              final String qustion,final String email,final int userId,final String sale_email,String url,String Website) {
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
                model.put("websiteType",Website);
                if ("1".equals(Website)){
					sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "["+id+"]"+"Inquiry Reply From ImportExpress", model, TemplateType.BUSINESS_INQUIRIES);
				}
                if ("2".equals(Website)){
					 urlTem = "https://www.kidscharming.com/goodsinfo/...-1"+url+".html";
					model.put("itemUrl",urlTem);
					sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "["+id+"]"+"Inquiry Reply From KidsCharming", model, TemplateType.BUSINESS_INQUIRIES_KIDS);
				}
				if ("3".equals(Website)){
					urlTem = "https://www.petstoreinc.com/goodsinfo/...-1"+url+".html";
					model.put("itemUrl",urlTem);
					sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "["+id+"]"+"Inquiry Reply From PetStoreinc", model, TemplateType.BUSINESS_INQUIRIES_PET);
				}

            };
        }.start();
//        return Utility.getStringIsNull(sale_email)?dao.replyReport(id, replyContent,date):0;
        return 1;
    }
	

}
