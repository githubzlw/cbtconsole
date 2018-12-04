package com.cbt.messages.service;

import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.BusiessBean;
import com.cbt.dao.BusiessDao;
import com.cbt.messages.dao.MessagesMapper;
import com.cbt.messages.vo.AdminRUser;
import com.cbt.messages.vo.MessagesCountVo;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Messages;
import com.cbt.pojo.page.Page;
import com.importExpress.mapper.CustomerDisputeMapper;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service("MessagesService")
public class MessagesServiceImpl implements MessagesService{
	private SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
	@Autowired
	private MessagesMapper messagesDao;
	
    @Autowired
    private BusiessDao busiessDao;

	@Override
	public int insertSelective(Messages record) {
		return messagesDao.insertSelective(record);
	}

	/**
	 * 查询消息分页
	 * @author zlg
	 * @param record  商品查询条件对象
	 * @param currentPage 分页开始数量
	 * @param onePageCount 分页每页数量
	 * @return page 消息列表
	 */
	@Override
	public Page<Messages> selectByMessages(Messages record, int currentPage,
			int onePageCount) {
		int pgCount = messagesDao.selectByMessagesCount(record);

		Page<Messages> page = new Page<Messages>(currentPage,pgCount,onePageCount);

		//根据商品查询条件查询商品列表信息
		List<Messages> pgList = messagesDao.selectByMessages(record, page.getStartIndex(), page.getOnePageCount());
		if(pgList!=null &&pgList.size()>0){
			page.setList(pgList);
		}else{
			page=null;
		}
		
		return page;
	}

	@Override
	public int deleteByPrimaryKey(Messages record) {
		if(messagesDao == null)
		{
			try
			{
				 	messagesDao=(MessagesMapper)Class.forName("MessagesMapper").newInstance();
			}
			catch(Exception ex)
			{}
		}
		return messagesDao.deleteByPrimaryKey(record);
	}

	/**
	 * 根据时间统计系统信息
	 */
	@Override
	public List<MessagesCountVo> selectBasicMessagesBytime(
			MessagesCountVo messagesCountVo) {
		return messagesDao.selectBasicMessagesBytime(messagesCountVo);
	}

	/**
	 * 统计系统总信息
	 */
	@Override
	public List<MessagesCountVo> selectBasicMessagesAll() {
		return messagesDao.selectBasicMessagesAll();
	}

	@Override
	public List<Admuser> selectAdmuser() {
		return messagesDao.selectAdmuser();
	}

	@Override
	public int updateByPrimaryKeySelective(Messages record) {
		return messagesDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<MessagesCountVo> selectBasicMessagesBytimeAndAdmin(
			MessagesCountVo messagesCountVo) {
		return messagesDao.selectBasicMessagesBytimeAndAdmin(messagesCountVo);
	}

	@Override
	public List<MessagesCountVo> selectBasicMessagesAllbyAndAdmin(
			MessagesCountVo messagesCountVo) {
		return messagesDao.selectBasicMessagesAllbyAndAdmin(messagesCountVo);
	}

	/**
	 * 按照客服查询消息并置顶
	 */
	@Override
	public List<Messages> selectByMessagesByOne(Messages record) {
		return messagesDao.selectByMessagesByOne(record);
	}

	/**
	 * 查询退款和投诉的数量
	 */
	@Override
	public MessagesCountVo selectComplainNum(int adminid) {
		return messagesDao.selectComplainNum(adminid);
	}
	
	/**
	 * 显示客户回复的未读个数
	 */
	@Override
	public MessagesCountVo selectComplainNum1(int adminid) {
		return messagesDao.selectComplainNum1(adminid);
	}

	/**
	 * 查询未分配订单数量
	 */
	@Override
	public MessagesCountVo selectnoArrgOrderNum(int adminid) {
		return messagesDao.selectnoArrgOrderNum(adminid);
	}
	/**
	 * 查询订单留言数量
	 */
	//所有数量
	@Override
	public MessagesCountVo selectOrderessNum(int adminid) {
		return messagesDao.selectOrderessNum( adminid);
	}
	//未处理数量
	@Override
	public int selectDelOrderessNum(int adminid) {
		return messagesDao.selectDelOrderessNum( adminid);
	}
	/**
	 * 查询客户留言数量
	 */
	@Override
	public MessagesCountVo selectGuestbookNum(int admuserid) {
		return messagesDao.selectGuestbookNum(admuserid);
	}
	@Override
	public MessagesCountVo selectCustomerInfoCollectionNum(int admuserid) {
		return messagesDao.selectCustomerInfoCollectionNum(admuserid);
	}
	@Override
	public MessagesCountVo selectQuestionNum(int admuserid) {
		return messagesDao.selectQuestionNum(admuserid);
	}
	/**
	 * 后台首页 各种客户留言 区块 待处理消息数量
	 */
	@Override
	public List<HashMap<String, String>> findCustomerMessages(int admuserid) {
		//产品单页客户折扣和定制需求，如果没回复 或者删除 就属于未处理 (请限制为最近1个月) 对应用户数	19
		MessagesCountVo guestbookNum = messagesDao.selectGuestbookNum(admuserid);
		Integer count19 = guestbookNum.getNoDeleteCount();
		
		//购物车&支付页面问题统计，如果没回复 或者删除 就属于未处理 (请限制为最近1个月) 对应用户数	1027
		MessagesCountVo customerInfoCollectionNum = messagesDao.selectCustomerInfoCollectionNum(admuserid);
		Integer count1027 = customerInfoCollectionNum.getNoDeleteCount();
		
		//客户评论管理：如果 没删除 或者 让他 前台显示  就属于 未处理 (请限制为最近1个月)	1078
		Integer count1078 = messagesDao.selectReviewManagementNum();
		
		//投诉管理 ， 如果 没回复 就属于未处理	29
		//http://192.168.1.34:8086/cbtconsole/complain/searchComplainByParam?userid=&creatTime=&complainState=0&username=&toPage=1&currentPage=1
		guestbookNum = messagesDao.selectComplainNum(admuserid);
		Integer count29 = guestbookNum.getNoDeleteCount();
		
		//查看产品单页用户留言，如果没同意 前台显示 或者删除，就 属于 未处理 	1073
//		Integer count1073 = messagesDao.selectQuestionAnswerNum();
		//产品单页用户留言(Customer Questions & Answers) 没回复 就属于未处理 最近1个月
		MessagesCountVo questionnum = messagesDao.selectQuestionNum(admuserid);
		Integer count1073 = questionnum.getNoDeleteCount();
		
		//申诉未读消息 1114
		MongoDBHelp instance = MongoDBHelp.INSTANCE;
		BasicDBObject q = new BasicDBObject();
		q.put("resource_type", "dispute");
		BasicDBObject s = new BasicDBObject("_id",-1);
		List<String> documents = 
				instance.findAny("data",q,s);
		List<String> disputeList = new ArrayList<String>();
		int count1114  = 0;
		for(String content : documents) {
    		if(StringUtils.indexOf(content,"dispute_id") > -1) {
    			JSONObject document = JSONObject.parseObject(content);
    			JSONObject  resource = (JSONObject)document.get("resource");
    			String dispute_id = resource.getString("dispute_id");
    			if(disputeList.contains(dispute_id)) {
    				continue;
    			}
    			disputeList.add(dispute_id);
    			if(!document.getBooleanValue("isRead")) {
    				count1114 ++;
    			}
    		}
    	}
		
		
		//查询需要加待处理消息数量入口的名称，用于入口页面中搜索入口按钮及拼接对应数量
		List<HashMap<String, String>> result = messagesDao.queryAuthNameByIds(Arrays.asList(new String[]{"19","29","1073","1078","1027","1114"}));
		for (HashMap<String, String> bean : result) {
			String id = String.valueOf(bean.get("authId"));
			switch (id) {
			case "19":
				bean.put("count", String.valueOf(count19));
				break;
			case "29":
				bean.put("count", String.valueOf(count29));
				break;
			case "1073":
				bean.put("count", String.valueOf(count1073));
				break;
			case "1078":
				bean.put("count", String.valueOf(count1078));
				break;
			case "1027":
				bean.put("count", String.valueOf(count1027));
				break;
			case "1114":
				bean.put("count", String.valueOf(count1114));
				break;
			}
		}
		return result;
	}

    @Override
    public MessagesCountVo selectBusiessNumNew(int admuserid) {
        BusiessBean busiessBean = new BusiessBean();
        busiessBean.setAdmName(String.valueOf(admuserid));
        busiessBean.setState(0);
        int count = busiessDao.queryByChoiceCountPage(busiessBean, 1);
        MessagesCountVo messagesCountVo = new MessagesCountVo();
        messagesCountVo.setNoDeleteCount(count);
        messagesCountVo.setType("businquiries");
        return messagesCountVo;
    }

    /**
	 * 查询批量优惠申请数量
	 */
	@Override
	public List<MessagesCountVo> selectApplicationNum(MessagesCountVo messagesCountVo) {
		return messagesDao.selectApplicationNum(messagesCountVo);
	}

	/**
	 * 查询商业询盘数量
	 */
	@Override
	public List<MessagesCountVo> selectBusiessNum(MessagesCountVo messagesCountVo) {
		return messagesDao.selectBusiessNum(messagesCountVo);
	}

	/**
	 * 查询购物车营销数量
	 */
	@Override
	public MessagesCountVo selectGoodscarNum(MessagesCountVo messagesCountVo) {
		return messagesDao.selectGoodscarNum(messagesCountVo);
	}

	/**
	 * 查询未被分配的产品页留言
	 */
	@Override
	public List<AdminRUser> selectNoArrageGuestbook() {
		return messagesDao.selectNoArrageGuestbook();
	}

	@Override
	public MessagesCountVo selectCountAllCartMarketingNum() {
		return messagesDao.selectCountAllCartMarketingNum();
	}

	@Override
	public int selectNoArrgCountNum() {
		return messagesDao.selectNoArrgCountNum();
	}
	
	@Override
	public MessagesCountVo selectSystemFailure() {
		return messagesDao.selectSystemFailure();
	}

}