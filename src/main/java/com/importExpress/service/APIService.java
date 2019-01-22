package com.importExpress.service;

import java.io.File;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface APIService {
		/**
		 * 获取token
		 * @return
		 * @throws Exception 
		 */
		String getAccessToken(String merchantID) throws Exception;
		
		/**
		  * 指定交易申诉列表
		 * @param accessToken
		 * @param disputedTransactionID
		 * @return
		 * @throws Exception
		 */
		String listDispute(String disputedTransactionID,String merchantID) throws Exception;
		/**申诉详情
		 * @param accessToken access_token
		 * @param disputeID 事件号
		 * @throws Exception 
		 */
		String showDisputeDetails(String disputeID,String merchantID) throws Exception;
		
		/**Partially updates a dispute
		 * @param accessToken access_token
		 * @param disputeID 事件号
		 * @throws Exception 
		 */
		String partiallyUpdates(String disputeID, String merchantID,JSONObject param) throws Exception;
		/**接受索赔
		 * @param accessToken
		 * @param disputeID
		 * @return
		 * @throws Exception
		 */
		String acceptClaim(String disputeID,String merchantID,JSONObject param) throws Exception;
		/**
		 * note:sandbox only
		 * @param accessToken
		 * @param disputeID
		 * @return
		 * @throws Exception 
		 */
		String updateDisputeStatus(String disputeID,String merchantID) throws Exception;
		/**提议
		 * @param accessToken
		 * @param disputeID
		 * @return
		 * @throws Exception 
		 */
		String makeOffer(String disputeID,String merchantID,JSONObject param) throws Exception;
		/**Acknowledge returned item
		 * @param accessToken
		 * @param disputeID
		 * @return
		 * @throws Exception 
		 */
		String acknowledgeReturnedItem(String disputeID,String merchantID,JSONObject param) throws Exception;
		/**发送消息
		 * @param accessToken
		 * @param disputeID
		 * @return
		 * @throws Exception 
		 */
		String sendMessage(String disputeID,String merchantID,JSONObject param) throws Exception;
		/**提供证据
		 * @param accessToken
		 * @param disputeID
		 * @return
		 * @throws Exception 
		 */
		String provideEvidence(String disputeID,String merchantID,Map<String,Object> param,Map<String,File> fileMap) throws Exception;

}
