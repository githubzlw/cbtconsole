package com.cbt.messages.dao;

import com.cbt.messages.vo.AdminRUser;
import com.cbt.messages.vo.MessagesCountVo;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Messages;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface MessagesMapper {

    int deleteByPrimaryKey(Messages record);

    int insert(Messages record);

    int insertSelective(Messages record);

    Messages selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Messages record);
    
    int selectByMessagesCount(Messages record);
    
    List<Messages> selectByMessages(@Param("messages") Messages messages, @Param("startIndex") int startIndex, @Param("onePageCount") int onePageCount);

    List<MessagesCountVo> selectBasicMessagesBytime(MessagesCountVo messagesCountVo);

    List<MessagesCountVo> selectBasicMessagesAll();

    List<Admuser> selectAdmuser();

    List<MessagesCountVo> selectBasicMessagesBytimeAndAdmin(MessagesCountVo messagesCountVo);

    List<MessagesCountVo> selectBasicMessagesAllbyAndAdmin(MessagesCountVo messagesCountVo);

    List<Messages> selectByMessagesByOne(Messages record);

    MessagesCountVo selectComplainNum(@Param("adminid") int adminid);

    MessagesCountVo selectComplainNum1(@Param("adminid") int adminid);

    MessagesCountVo selectnoArrgOrderNum(@Param("adminid") int adminid);

    MessagesCountVo selectOrderessNum(@Param("adminid") int adminid);
    
    int selectDelOrderessNum(@Param("adminid") int adminid);
    
    MessagesCountVo selectGuestbookNum(@Param("adminid") int adminid);
    
    MessagesCountVo selectCustomerInfoCollectionNum(@Param("adminid") int adminid);
    
    MessagesCountVo selectQuestionNum(@Param("adminid") int adminid);
    
    List<MessagesCountVo> selectApplicationNum(MessagesCountVo messagesCountVo);
    
    List<MessagesCountVo> selectBusiessNum(MessagesCountVo messagesCountVo);
      
    MessagesCountVo selectGoodscarNum(MessagesCountVo messagesCountVo);
    
    List<AdminRUser> selectNoArrageGuestbook();

	MessagesCountVo selectCountAllCartMarketingNum();
	
	MessagesCountVo selectSystemFailure();

	int selectNoArrgCountNum();

	Integer selectReviewManagementNum();

	Integer selectQuestionAnswerNum();

	List<HashMap<String, String>> queryAuthNameByIds(List<String> list);

}