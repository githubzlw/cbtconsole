package com.cbt.messages.service;

import com.cbt.messages.vo.AdminRUser;
import com.cbt.messages.vo.MessagesCountVo;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Messages;
import com.cbt.pojo.page.Page;

import java.util.HashMap;
import java.util.List;


public interface MessagesService {
	int insertSelective(Messages record);
	
	Page<Messages> selectByMessages(Messages record, int startIndex, int onePageCount);
	
	int deleteByPrimaryKey(Messages record);
	 
	 List<MessagesCountVo> selectBasicMessagesBytime(MessagesCountVo messagesCountVo);
	    
	 List<MessagesCountVo> selectBasicMessagesAll();
	 
	 List<Admuser> selectAdmuser();
	 
	 int updateByPrimaryKeySelective(Messages record);
	 
    List<MessagesCountVo> selectBasicMessagesBytimeAndAdmin(MessagesCountVo messagesCountVo);
    
    List<MessagesCountVo> selectBasicMessagesAllbyAndAdmin(MessagesCountVo messagesCountVo);
    
    List<Messages> selectByMessagesByOne(Messages record);
    
    MessagesCountVo selectComplainNum(int adminid);
    
    MessagesCountVo selectComplainNum1(int adminid);
    
    MessagesCountVo selectSystemFailure();
    
    MessagesCountVo selectnoArrgOrderNum(int adminid);
    
    MessagesCountVo selectGuestbookNum(int admuserid);
    
    MessagesCountVo selectCustomerInfoCollectionNum(int admuserid);
    
    MessagesCountVo selectQuestionNum(int admuserid);
    
    List<MessagesCountVo> selectApplicationNum(MessagesCountVo messagesCountVo);
    
    List<MessagesCountVo> selectBusiessNum(MessagesCountVo messagesCountVo);
      
    MessagesCountVo selectGoodscarNum(MessagesCountVo messagesCountVo);
    
    List<AdminRUser> selectNoArrageGuestbook();

	MessagesCountVo selectCountAllCartMarketingNum();

	int selectNoArrgCountNum();
	
	MessagesCountVo selectOrderessNum(int adminid);
	
	int selectDelOrderessNum(int adminid);

	List<HashMap<String, String>> findCustomerMessages(int admuserid);

    MessagesCountVo selectBusiessNumNew(int admuserid);

}