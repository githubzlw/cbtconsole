package com.importExpress.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.FileMeta;
import com.cbt.parse.service.StrUtils;
import com.cbt.stripe.StripeService;
import com.cbt.util.FileUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SysParamUtil;
import com.cbt.warehouse.ctrl.GroupBuyManageCtrl;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.CustomerDisputeBean;
import com.importExpress.service.APIService;
import com.importExpress.service.CustomerDisputeService;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Dispute;

@Controller
@RequestMapping("/customer/dispute")
public class CustomerDisputeController {
	 private static final Log LOG = LogFactory.getLog(GroupBuyManageCtrl.class);
	 @Autowired
	 private CustomerDisputeService customerDisputeService;
	 @Autowired
	 private APIService apiService;
	 @Autowired
	 private StripeService stripeService;
	
	
	 /**
     * 
             *??????????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public EasyUiJsonResult list(HttpServletRequest request, HttpServletResponse response) {
    	EasyUiJsonResult json = new EasyUiJsonResult();
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("??????????????????");
            return json;
        }
//        int adminId = 0;
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        int startNum = 0;
        int limitNum = 50;
        
        String pageStr = request.getParameter("page");
        String limitNumStr = request.getParameter("rows");
        
        if (!(limitNumStr == null || "".equals(limitNumStr) || "0".equals(limitNumStr))) {
        	limitNum = Integer.valueOf(limitNumStr) ;
        }
        
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String disputeid = request.getParameter("disputeid");
        
        String sttime = request.getParameter("sttime");
        if (sttime == null || "".equals(sttime)) {
            sttime = "";
        } else {
            sttime += " 00:00:00";
        }
        String edtime = request.getParameter("edtime");
        if (edtime == null || "".equals(edtime)) {
            edtime = "";
        } else {
            edtime += " 23:59:59";
        }
        String status = request.getParameter("status");
        status = StringUtils.equals("-1", status) ? null : status;
        status = StringUtils.equals("0", status) ? "OPEN" : status;
        status = StringUtils.equals("1", status) ? "WAITING_FOR_SELLER_RESPONSE" : status;
        status = StringUtils.equals("3", status) ? "UNDER_REVIEW" : status;
        status = StringUtils.equals("4", status) ? "RESOLVED" : status;
        
        try {
            // ????????????????????????????????????
        	int count = customerDisputeService.count(null, "0");
        	
        	json.setMessage(String.valueOf(count));
        	
        	Map<String, Object> map = customerDisputeService.list(disputeid,startNum, limitNum, 
        			sttime, edtime, status,adm.getId(),adm.getRoletype());
        	long total = 0;
        	if(map != null && !map.isEmpty() ) {
        		total = (Long)map.get("total");
        		json.setRows(map.get("data"));
        	}
            
            json.setSuccess(true);
            json.setTotal((int)total);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage("??????????????????????????????" + e.getMessage());
            LOG.error("??????????????????????????????" + e.getMessage());
        }
        return json;
    }
   
    
    
    
    /**??????????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public ModelAndView info(HttpServletRequest request, HttpServletResponse response) {
    	 ModelAndView mv = new ModelAndView("disputeinfo");
    	 String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
         if (StringUtil.isBlank(admuserJson)) {
             mv.addObject("success", 0);
             mv.addObject("message", "??????????????????");
             return mv;
         }
//         int adminId = 0;
         Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
         mv.addObject("operatorId", adm.getId());
         mv.addObject("roleType", adm.getRoletype());
         mv.addObject("operatorName", adm.getAdmName());
         /*if ("0".equals(adm.getRoletype())) {
             adminId = 0;
         } else {
             adminId = adm.getId();
         }*/
        mv.addObject("role", adm.getId() == 1||adm.getId() == 8 ? 1 : 2);
    	String disputeID = request.getParameter("disputeid");
    	String merchant = request.getParameter("merchant");
    	String isread = request.getParameter("isread");
    	JSONObject infoByDisputeID = null;
    	int resonFlag = 0;
    	int statusFlag = 0;
    	int disputeLifeCycleFlag = 0;
    	int apiType = 0;
    	mv.addObject("success", 1);
		try {
			int confim = customerDisputeService.count(disputeID,"0");
			mv.addObject("confim", confim);
			String showDisputeDetails = "";//
			if(StringUtils.startsWith(disputeID, "PP")) {
				showDisputeDetails = apiService.showDisputeDetails(disputeID,merchant);
				LOG.info(showDisputeDetails);
//				System.out.println(showDisputeDetails);
				if(StringUtils.isNotEmpty(showDisputeDetails)) {
					infoByDisputeID = JSONObject.parseObject(showDisputeDetails);
				}
				if(infoByDisputeID != null) {
					mv.addObject("result", infoByDisputeID);
					JSONArray disputedTransactions = (JSONArray)infoByDisputeID.get("disputed_transactions");
					
					JSONObject seller = (JSONObject)((JSONObject)disputedTransactions.get(0)).get("seller");
					merchant = seller.getString("merchant_id");
					
					String custom = ((JSONObject)disputedTransactions.get(0)).getString("custom");
					mv.addObject("orderNo", "");
					if(StringUtils.indexOf(custom, "@") > -1) {
						String[] split = custom.indexOf("{@}") > -1 ? custom.split("\\{@\\}") : custom.split("@");
						String userid = split.length > 3 ? split[0] : "";
						String orderNo = split.length > 5 ? split[6] : split.length > 3 ?split[2] : "";
						mv.addObject("userid", userid);
						mv.addObject("orderNo", orderNo);
					}
					//??????
					String reason = infoByDisputeID.getString("reason");
					resonFlag = StringUtils.equals(reason, "MERCHANDISE_OR_SERVICE_NOT_RECEIVED") ? 1 : resonFlag;
					//??????
					String status = infoByDisputeID.getString("status");
					statusFlag = StringUtils.equals("OPEN", status) ? 0 : statusFlag;
					statusFlag = StringUtils.equals("WAITING_FOR_SELLER_RESPONSE", status) ? 1 : statusFlag;
					statusFlag = StringUtils.equals("WAITING_FOR_BUYER_RESPONSE", status) ? 2 : statusFlag;
					statusFlag = StringUtils.equals("UNDER_REVIEW", status) ? 3 : statusFlag;
					statusFlag = StringUtils.equals("RESOLVED", status) ? 4 : statusFlag;
					
					String disputeLifeCycleStage = infoByDisputeID.getString("dispute_life_cycle_stage");
					disputeLifeCycleFlag = StringUtils.equals("INQUIRY", disputeLifeCycleStage) ? 1 : disputeLifeCycleFlag;
					disputeLifeCycleFlag = StringUtils.equals("CHARGEBACK", disputeLifeCycleStage) ? 2 : disputeLifeCycleFlag;
					//
					JSONObject disputeOutcome = (JSONObject)infoByDisputeID.get("dispute_outcome");
					if(disputeOutcome != null) {
						JSONObject amountRefunded = (JSONObject)disputeOutcome.get("amount_refunded");
						mv.addObject("dispute_outcome_amount", amountRefunded != null ? amountRefunded.getString("value") : null);
					}
					JSONObject offer = (JSONObject)infoByDisputeID.get("offer");
					if(offer != null) {
						JSONObject sellerOfferedAmount = (JSONObject)offer.get("seller_offered_amount");
						JSONObject buyerRequestedAmount = (JSONObject)offer.get("buyer_requested_amount");
						mv.addObject("buyer_requested_amount", buyerRequestedAmount != null ? buyerRequestedAmount.getString("value") : null);
						mv.addObject("seller_offered_amount", sellerOfferedAmount != null ? sellerOfferedAmount.getString("value") : null);
					}
					mv.addObject("resonFlag", resonFlag);
					mv.addObject("statusFlag", statusFlag);
					mv.addObject("disputeLifeCycleFlag", disputeLifeCycleFlag);
					mv.addObject("merchant", merchant);
				}
			}else {
				apiType = 1;
				Dispute dispute = stripeService.dispute(disputeID);
				if(dispute != null) {
					Long amount = dispute.getAmount();
					String strAmount = String.valueOf(amount);
					strAmount = strAmount.substring(0,strAmount.length()-2)+"."+strAmount.substring(strAmount.length()-2);
					mv.addObject("disputeAmount", strAmount);
				}
				mv.addObject("status","needs_response".equals(dispute.getStatus().toLowerCase()) ? 0:1);
				mv.addObject("dispute", dispute);
				
				resonFlag = "fraudulent".equals(dispute.getReason().toLowerCase())? 1: resonFlag;
				resonFlag = "product_not_received".equals(dispute.getReason().toLowerCase())? 2: resonFlag;
				resonFlag = "product_unacceptable".equals(dispute.getReason().toLowerCase())? 3: resonFlag;
				resonFlag = "unrecognized".equals(dispute.getReason().toLowerCase())? 4: resonFlag;
				resonFlag = "subscription_canceled".equals(dispute.getReason().toLowerCase())? 5: resonFlag;
				resonFlag = "duplicate".equals(dispute.getReason().toLowerCase())? 6: resonFlag;
				resonFlag = "credit_not_processed".equals(dispute.getReason().toLowerCase())? 7: resonFlag;
				int submitFor = 0;
				mv.addObject("submitFor", submitFor);
			}	
			mv.addObject("resonFlag", resonFlag);
			mv.addObject("apiType", apiType);
			if(!StringUtils.equals(isread, "true")) {
				long updateMessage = customerDisputeService.updateMessage(disputeID);
				LOG.info("updateMessage:"+updateMessage);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mv.addObject("message", e.getMessage());
			mv.addObject("success", 0);
		}
    	return mv;
    }
   
    /**??????????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/sendmessage")
    public String sendMessage(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	String content = request.getParameter("messageBodyForGeneric");
    	String merchant = request.getParameter("merchant");
    	String email = request.getParameter("sellerEmailForPhotoEvidence");
    	
		
    	String result = null;
    	attr.addFlashAttribute("sendResult", "Failed");
		try {
			if(StringUtils.isNotBlank(content)) {
				//??????
				JSONObject data = new JSONObject();
				
//				if(StringUtils.isNotBlank(email)) {
//					data.put("op", "add");
//					data.put("path", "/communication_details");
//					JSONObject value = new JSONObject();
//					value.put("email", email);
//					value.put("note", content);
//					data.put("value", value);
//					result = customerDisputeService.updateMessage(disputeID, data);
//				}else {
					data.put("message", content);
					result = apiService.sendMessage(disputeID,merchant, data);
//				}
				LOG.info(result);
				attr.addFlashAttribute("sendResult", "Successed");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			attr.addFlashAttribute("sendResult", "Error:"+e.getMessage());
		}
		
		
    	return "redirect:/customer/dispute/info?disputeid="+disputeID;
    }
    /**??????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offer")
    public String makeOffer(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
        	attr.addFlashAttribute("sendResult", "?????????");
        	return "redirect:/customer/dispute/info?disputeid="+disputeID;
        }
//        int adminId = 0;
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        
    	CustomerDisputeBean bean = new CustomerDisputeBean();
    	bean.setOprateAdm(adm.getAdmName());
    	bean.setDisputeID(disputeID);
    	
    	String userid = request.getParameter("userid");
    	bean.setUserid(userid);
    	
    	String orderNo = request.getParameter("orderNo");
    	bean.setOrderNo(orderNo);
    	
    	String merchant = request.getParameter("merchant");
    	bean.setMerchantID(merchant);
    	
    	String content = request.getParameter("messageBodyForGeneric");
    	bean.setRemark(content);
    	
    	String offerAmount = request.getParameter("refundAmount");
    	bean.setSellerOfferedAmount(StringUtils.isBlank(offerAmount) ? "0.00" : offerAmount);
    	
    	String buyer_request_amount = request.getParameter("buyer_request_amount");
    	bean.setBuyerRequestAmount(StringUtils.isBlank(buyer_request_amount) ? "0.00" : buyer_request_amount);
    	
    	String gross_amount = request.getParameter("gross_amount");
    	bean.setGrossAmount(gross_amount);
    	
    	String offerType = request.getParameter("offerType");
    	bean.setOfferType(offerType);
    	
    	String currencyCode = request.getParameter("refundCurrency");
    	bean.setCurrencyCode(currencyCode);
    	bean.setRefundType("1");
    	bean.setApiType("paypal");
    	String reason = request.getParameter("reason");
    	bean.setReason(reason);
    	
    	
    	String postalCode = request.getParameter("postalCode");
    	String address_line_1 = request.getParameter("address_line_1");
    	String address_line_2 = request.getParameter("address_line_2");
    	String address_line_3 = request.getParameter("address_line_3");
    	String admin_area_4 = request.getParameter("admin_area_4");
    	String admin_area_3 = request.getParameter("admin_area_3");
    	String admin_area_2 = request.getParameter("admin_area_2");
    	String admin_area_1 = request.getParameter("admin_area_1");
    	String country_code = request.getParameter("country_code");
    	
    	String invoice_id = request.getParameter("invoice_id");
    	bean.setTransactionID(invoice_id);
    	
    	attr.addFlashAttribute("sendResult", "Error:??????????????????!");
    	if(StringUtils.isBlank(content)) {
    		return "redirect:/customer/dispute/info?disputeid="+disputeID;
    	}
    	
    	if(StringUtils.equals(offerType,"REFUND_WITH_RETURN")
    			&& StringUtils.isNotBlank(address_line_1)){
    		JSONObject address = new JSONObject();
    		address.put("address_line_1", address_line_1);
    		address.put("address_line_2", address_line_2);
    		address.put("address_line_3", address_line_3);
    		address.put("admin_area_4", admin_area_4);
    		address.put("admin_area_3", admin_area_3);
    		address.put("admin_area_2", admin_area_2);
    		address.put("admin_area_1", admin_area_1);
    		address.put("postal_code", postalCode);
    		address.put("country_code", country_code );
    		bean.setReturnShippingAddress(JSONObject.toJSONString(address));
    	}
    	
		try {
			customerDisputeService.confirm(bean);
			attr.addFlashAttribute("sendResult", "Successed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			attr.addFlashAttribute("sendResult", "Error:"+e.getMessage());
		}
    	
		return "redirect:/customer/dispute/info?disputeid="+disputeID;
    }
   
    /**????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/acceptClaim")
    public String acceptClaim(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
        	attr.addFlashAttribute("sendResult", "?????????");
        	return "redirect:/customer/dispute/info?disputeid="+disputeID;
        }
//        int adminId = 0;
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        
    	CustomerDisputeBean bean = new CustomerDisputeBean();
    	bean.setOprateAdm(adm.getAdmName());
    	bean.setDisputeID(disputeID);
    	
    	String userid = request.getParameter("userid");
    	bean.setUserid(userid);
    	
    	String orderNo = request.getParameter("orderNo");
    	bean.setOrderNo(orderNo);
    	
    	String merchant = request.getParameter("merchant");
    	bean.setMerchantID(merchant);
    	
    	String content = request.getParameter("messageBodyForGeneric");
    	bean.setRemark(content);
    	
    	String offerAmount = request.getParameter("refundAmount");
    	bean.setSellerOfferedAmount(StringUtils.isBlank(offerAmount) ? "0.00" : offerAmount);
    	
    	String buyer_request_amount = request.getParameter("buyer_request_amount");
    	bean.setBuyerRequestAmount(StringUtils.isBlank(buyer_request_amount) ? "0.00" : buyer_request_amount);
    	
    	String gross_amount = request.getParameter("gross_amount");
    	bean.setGrossAmount(gross_amount);
    	
    	String currencyCode = request.getParameter("refundCurrency");
    	bean.setCurrencyCode(currencyCode);
    	bean.setRefundType("0");
    	bean.setApiType("paypal");
    	
    	String reason = request.getParameter("reason");
    	bean.setReason(reason);
    	
    	
    	String invoice_id = request.getParameter("invoice_id");
    	bean.setTransactionID(invoice_id);
    	
    	String accept_claim_reason = request.getParameter("accept_claim_reason");
    	bean.setAcceptClaimReason(accept_claim_reason);
    	
    	attr.addFlashAttribute("sendResult", "Failed");
    	if(StringUtils.isBlank(content)) {
    		attr.addFlashAttribute("sendResult", "??????????????????,???????????????");
    		return "redirect:/customer/dispute/info?disputeid="+disputeID;
    	}
    	
		try {
			customerDisputeService.confirm(bean);
			attr.addFlashAttribute("sendResult", "Successed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			attr.addFlashAttribute("sendResult", "Error:"+e.getMessage());
		}
    	
		return "redirect:/customer/dispute/info?disputeid="+disputeID;
    }
    /**????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/evidence")
    public String provideEvidence(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	String merchant = request.getParameter("merchant");
    	String content = request.getParameter("messageBodyForGeneric");
    	String fileLocation = request.getParameter("fileLocation");
    	String evidence_type = request.getParameter("evidence_type");
    	String carrier_name = request.getParameter("carrier_name");
    	String tracking_number = request.getParameter("tracking_number");
    	String refund_ids = request.getParameter("refund_ids");
    	Map<String,File> fileMap = new HashMap<>();
    	LOG.info("fileLocation:"+fileLocation);
    	if(StringUtils.isNotBlank(fileLocation)) {
    		File file = new File(fileLocation);
    		fileMap.put("file1", file);
    	}
    	JSONObject in = new JSONObject();
    	JSONArray evidences = new JSONArray();
    	JSONObject evidence = new JSONObject();
    	evidence.put("evidence_type", evidence_type);
        
        JSONObject evidence_info = new JSONObject();
        if(StringUtils.isNotBlank(tracking_number) && StringUtils.isNotBlank(carrier_name)) {
        	
        	JSONArray tracking_info = new JSONArray();
        	JSONObject info = new JSONObject();
        	info.put("carrier_name", carrier_name);
        	info.put("tracking_number", tracking_number);
        	tracking_info.add(info);
        	evidence_info.put("tracking_info", tracking_info);
        }
        
        if(StringUtils.isNotBlank(refund_ids)) {
        	JSONArray refund_id = new JSONArray();
        	String[] refund_idss = refund_ids.split(",");
        	for(String id : refund_idss) {
        		refund_id.add(id);
        	}
        	evidence_info.put("refund_ids", refund_id);
        }
        
        evidence.put("evidence_info", evidence_info);
        evidence.put("notes", content);
        evidences.add(evidence);
        
        in.put("evidences", evidences);
		
        Map<String,Object> param = new HashMap<>();
        
        param.put("input", in);
        
        
        String address_line_1 = request.getParameter("address_line_1");
        String address_line_2 = request.getParameter("address_line_2");
        String address_line_3 = request.getParameter("address_line_3");
        String country_code = request.getParameter("country_code");
        String admin_area_1 = request.getParameter("admin_area_1");
        String admin_area_2 = request.getParameter("admin_area_2");
        String admin_area_3 = request.getParameter("admin_area_3");
        String admin_area_4 = request.getParameter("admin_area_4");
        String postal_code = request.getParameter("postal_code");
        if(StringUtils.isNotBlank(postal_code) && StringUtils.isNotBlank(country_code)) {
        	JSONObject returnAddress =  new JSONObject();
        	returnAddress.put("address_line_1", address_line_1);
        	returnAddress.put("address_line_2", address_line_2);
        	if(StringUtils.isNotBlank(address_line_3)) {
        		returnAddress.put("address_line_3", address_line_3);
        	}
        	if(StringUtils.isNotBlank(admin_area_3)) {
        		returnAddress.put("admin_area_3", admin_area_3);
        	}
        	if(StringUtils.isNotBlank(admin_area_4)) {
        		returnAddress.put("admin_area_4", admin_area_4);
        	}
        	returnAddress.put("country_code", country_code);
        	returnAddress.put("admin_area_1", admin_area_1);
        	returnAddress.put("admin_area_2", admin_area_2);
        	returnAddress.put("postal_code", postal_code);
        	param.put("return_shipping_address", returnAddress);
        }
//        System.out.println(param.toString());
    	String result = null;
		try {
			if(StringUtils.isNotBlank(content)) {
				result = apiService.provideEvidence(disputeID, merchant, param,fileMap);
			}else {
				attr.addFlashAttribute("sendResult", "Error:Please send a friendly message to the buyer");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
			attr.addFlashAttribute("sendResult", "Error:"+e.getMessage());
		}
    	LOG.info(result);
    	
    	return "redirect:/customer/dispute/info?disputeid="+disputeID+"&merchant="+merchant+"&isread=true";
    }
    /**Acknowledge returned ite
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/acknowledge-return-item")
    public String acknowledgeReturnedItem(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	String merchant = request.getParameter("merchant");
    	String content = request.getParameter("messageBodyForGeneric");
    	
    	attr.addFlashAttribute("sendResult", "Failed");
    	if(StringUtils.isBlank(content)) {
    		return "redirect:/customer/dispute/info?disputeid="+disputeID;
    	}
    	//??????
    	JSONObject data = new JSONObject();
    	data.put("note", content);
    	
    	String result = null;
    	try {
    		result = apiService.acknowledgeReturnedItem(disputeID, merchant, data);
    		attr.addFlashAttribute("sendResult", "Successed");
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		result = e.getMessage();
    		attr.addFlashAttribute("sendResult", "Error:"+e.getMessage());
    	}
//    	LOG.info(result);
    	
    	return "redirect:/customer/dispute/info?disputeid="+disputeID;
    }
    @RequestMapping("/uploads")
	@ResponseBody 
	public Map<String,Object> upload(HttpServletRequest  request, HttpServletResponse response) throws Exception {
    	LinkedList<FileMeta> files = new LinkedList<FileMeta>();
    	FileMeta fileMeta = null;
    	Map<String,Object> map = new HashMap<String, Object>();
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		MultipartHttpServletRequest multipartRequest = null;
		if (resolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest)request;
		} else {
			map.put("data", "???????????????");
			return map;
		}
		String uploadPath = SysParamUtil.getParam("uploadPath");
		String saveFileUrl = uploadPath.endsWith("/")?uploadPath+"dispute":uploadPath+"/dispute";
		
		saveFileUrl = saveFileUrl + "/" + System.currentTimeMillis() + "/";
		
		File headPath = new File(saveFileUrl);//?????????????????????
//		//LOG.info(getRootPath(multipartRequest));
        if(!headPath.exists()){//???????????????????????????????????????????????????????????????
        	headPath.mkdirs();
        }
		//??????????????????????????????
		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile mpf = null; 
		List<String> fileList = new ArrayList<String>();
		while (itr.hasNext()) {
			//????????????
			mpf = multipartRequest.getFile(itr.next());
			LOG.info(mpf.getOriginalFilename() + " ????????????! ??????:" + files.size());
			//???????????????????????????10,????????????????????????
			if (files.size() >= 10)
				files.pop();
			//????????????????????????
			fileMeta = new FileMeta();
			String fileName= mpf.getOriginalFilename();
			fileMeta.setFileName(fileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			fileMeta.setBytes(mpf.getBytes());

			try {
				// ???????????????
				String newFileName = FileUtil.getNewFileName(mpf);
				//??????(??????)??????
//				FileCopyUtils.copy(("\r\n--"+mpf+"--\r\n").getBytes(), new FileOutputStream(getRootPath(multipartRequest)+"/upimg/" +newFileName));
				FileCopyUtils.copy((mpf).getBytes(), new FileOutputStream(saveFileUrl+newFileName));
				//???????????????
				map.put("success", true);
				fileList.add(newFileName);
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("message", "File upload occured problem.");
			}
			//????????????
			files.add(fileMeta);
		}
		map.put("names", fileList);
		map.put("saveLoaction", saveFileUrl);
		return map;
	}
    /**??????????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/confirm/list")
    @ResponseBody
    public EasyUiJsonResult confirmList(HttpServletRequest request, HttpServletResponse response) {
    	EasyUiJsonResult json = new EasyUiJsonResult();
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
    	json.setRows(new ArrayList<CustomerDisputeBean>());
    	if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("??????????????????");
            return json;
        }
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
    	
        int startNum = 0;
        int limitNum = 50;
        
        String pageStr = request.getParameter("page");
        String limitNumStr = request.getParameter("rows");
        
        if (!(limitNumStr == null || "".equals(limitNumStr) || "0".equals(limitNumStr))) {
        	limitNum = Integer.valueOf(limitNumStr) ;
        }
        
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String disputeid = request.getParameter("disputeid");
        disputeid = StringUtils.isBlank(disputeid) ? null : disputeid;
        
      
        String status = request.getParameter("status");
        status = StringUtils.isBlank(status) ? "-2" : status;
    	try {
    		List<CustomerDisputeBean> confirmList = customerDisputeService.confirmList(disputeid, status,startNum,limitNum);
    		confirmList.stream().forEach(c -> {
    			c.setAmount(c.getSellerOfferedAmount()+" "+c.getCurrencyCode());
    			c.setAdminRolyType(adm.getRoletype());
    			if(StringUtils.equals(c.getStatus(), "-1")) {
    				c.setRemark("Send Message To Buyer:"+c.getRemark()+"<br><br>(????????????)Refuse Reason:"+c.getRefuseReason());
    			}else {
    				c.setRemark("Send Message To Buyer:"+c.getRemark());
    			}
    		});
    		
    		int count = customerDisputeService.count(disputeid, status);
    		
    		json.setRows(confirmList);
    		json.setSuccess(true);
    		json.setTotal(count);
    			
    	} catch (Exception e) {
    		e.printStackTrace();
    		json.setSuccess(false);
            json.setMessage("??????????????????????????????" + e.getMessage());
            LOG.error("??????????????????????????????" + e.getMessage());
    	}
    	return json;
    }
    /**????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/refund")
    @ResponseBody
    public Map<String,Object> refund(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> result = new HashMap<String, Object>();
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
    	if (StringUtil.isBlank(admuserJson)) {
    		result.put("state", false);
    		result.put("message", "?????????");
        	return  result;
        }
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if(!StringUtils.equals(adm.getRoletype(), "0")) {
    		result.put("state", false);
    		result.put("message", "????????????????????????????????????");
        	return  result;
    	}
        
    	String disputeid = request.getParameter("disputeid");
    	CustomerDisputeBean comfirmByDisputeID = customerDisputeService.getComfirmByDisputeID(disputeid);
    	if(comfirmByDisputeID == null) {
    		result.put("state", false);
    		result.put("message",  "??????????????????????????????????????????");
        	return  result;
    	}
    	//????????????  0-??????paypal????????????accept claim   1-paypal??????????????????offer
    	String refundType = comfirmByDisputeID.getRefundType();
    	String disputeID = comfirmByDisputeID.getDisputeID();
    	String merchant = comfirmByDisputeID.getMerchantID();
    	String content = comfirmByDisputeID.getRemark();
    	String offerAmount = comfirmByDisputeID.getSellerOfferedAmount();
    	String currencyCode = comfirmByDisputeID.getCurrencyCode();
    	
    	boolean isPremint = Double.valueOf(offerAmount)<500.0099999;
    	if(!((adm.getId() == 1 && isPremint)||(adm.getId() == 8 && isPremint)||(adm.getId()==83))) {
    		result.put("state", false);
    		result.put("message", "?????????????????????????????????????????????");
    		return  result;
    	}
    	try {
    		String invoice_id = comfirmByDisputeID.getTransactionID();
    		if(StringUtils.isBlank(content)) {
    			result.put("state", false);
        		result.put("message",    "??????????????????,??????????????????");
    		}else {
    			//??????
        		JSONObject data = new JSONObject();
        		data.put("note", content);
        		if(StringUtils.isNotBlank(offerAmount)) {
        			JSONObject Amount = new JSONObject();
        			Amount.put("value", offerAmount);
        			Amount.put("currency_code", currencyCode);
        			if(StringUtils.equals(refundType, "0")) {
//        				For MERCHANDISE_OR_SERVICE_NOT_RECEIVED dsiputes, 
//        				refund amount cannot be specified in accept claim as this feature is not yet 
//        				supported in PayPal Dispute system. You cannot specify the refund amount in an accept claim call.
        				if(!StringUtils.equals(comfirmByDisputeID.getReason(), "MERCHANDISE_OR_SERVICE_NOT_RECEIVED")) {
        					data.put("refund_amount", Amount);
        				}
        			}else {
        				data.put("offer_amount" , Amount);
        				
        			}
        		}
        		customerDisputeService.updateStatus(disputeID, "1");
        		String refundResult = null;
        		if(StringUtils.equals(refundType, "0")) {
        			
                	data.put("accept_claim_reason", comfirmByDisputeID.getAcceptClaimReason());
                	data.put("invoice_id", invoice_id);
                	
            		LOG.info("claim : "+JSONObject.toJSONString(data));
                	refundResult = apiService.acceptClaim(disputeID, merchant, data);
            	}else {
//                	data.put("invoice_id", invoice_id);
            		String offerType = comfirmByDisputeID.getOfferType();
            		data.put("offer_type", offerType);
            		
            		String returnShippingAddress = comfirmByDisputeID.getReturnShippingAddress();
                	if(StringUtils.equals(offerType,"REFUND_WITH_RETURN")
                			&& StringUtils.isNotBlank(returnShippingAddress)){
                		JSONObject address = JSONObject.parseObject(returnShippingAddress);
                		data.put("return_shipping_address", address);
                	}
                	
                	LOG.info("offer:"+JSONObject.toJSONString(data));
                	refundResult = apiService.makeOffer(disputeID,merchant, data);
            	}
        		result.put("state", true);
        		result.put("message",   "????????????");
        		LOG.info(content);
        		customerDisputeService.updateRefund(disputeid, offerAmount);
    		}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("state", false);
			result.put("message",  "Error:"+e.getMessage());
		}
    	return  result;
    }
    /**????????????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/refuse")
    @ResponseBody
    public Map<String,Object> refuse(HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> result = new HashMap<String, Object>();
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
    	if (StringUtil.isBlank(admuserJson)) {
    		result.put("state", false);
    		result.put("message", "?????????");
        	return  result;
        }
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if(!StringUtils.equals(adm.getRoletype(), "0")) {
    		result.put("state", false);
    		result.put("message", "????????????????????????????????????");
        	return  result;
    	}

        if(!(adm.getId() == 1||adm.getId() == 8||adm.getId()==83)) {
    		result.put("state", false);
    		result.put("message", "?????????????????????????????????????????????");
        	return  result;
    	}
    	
    	String disputeid = request.getParameter("disputeid");
    	String refuseReason = request.getParameter("reason");
    	if(StringUtils.isBlank(disputeid) || StringUtils.isBlank(refuseReason)) {
    		result.put("state", false);
    		result.put("message", "????????????????????????");
        	return  result;
    	}
    	Integer updateRefuseReason = customerDisputeService.updateRefuseReason(disputeid, refuseReason);
    	result.put("state", updateRefuseReason != null && updateRefuseReason > 0 ?true : false);
    	result.put("message", updateRefuseReason != null && updateRefuseReason > 0 ? "????????????" : "????????????");
    	return  result;
    }
    /**??????
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/confirm")
    public String confirm(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	String seller_transaction_id = request.getParameter("seller_transaction_id");
    	String userid = request.getParameter("userid");
    	String orderNo = request.getParameter("orderNo");
    	String remark = request.getParameter("messageBodyForGeneric");
    	String operatorName = request.getParameter("operatorName");
    	String merchant = request.getParameter("merchant");
    	
    	CustomerDisputeBean customer = new CustomerDisputeBean();
    	
    	customer.setDisputeID(disputeID);
    	customer.setOprateAdm(operatorName);
    	customer.setOrderNo(orderNo);
    	customer.setRemark(remark);
    	customer.setTransactionID(seller_transaction_id);
    	customer.setUserid(userid);
    	customer.setStatus("0");
    	customer.setMerchantID(merchant);
    	int confirm = customerDisputeService.confirm(customer);
    	
    	return "redirect:/customer/dispute/info?disputeid="+disputeID;
    }
    @RequestMapping(value="/stripe/update",method= {RequestMethod.POST,RequestMethod.GET})
    public String stripeUpdate(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeid = request.getParameter("disputeId");
    	String isRead = request.getParameter("isRead");
    	String resonFlag = request.getParameter("resonFlag");
    	
    	Map<String, Object> evidence = new HashMap<>();
    	int reson = StrUtils.isNum(resonFlag) ? Integer.valueOf(resonFlag) : 0;
    	evidence.put("product_description", request.getParameter("product_description"));
    	if(reson < 5 && reson >0) {
    		evidence.put("shipping_date", request.getParameter("shipping_date"));
    		evidence.put("shipping_tracking_number", request.getParameter("shipping_number"));
    		evidence.put("shipping_carrier", request.getParameter("shipping_carrier"));
    		evidence.put("shipping_address", request.getParameter("shipping_address"));
    		evidence.put("shipping_documentation",uploadFile(request.getParameter("shipping_documentation")));
    		if(reson == 4) {
    			evidence.put("access_activity_log", request.getParameter("access_activity_log"));
    		}
    	}else if(reson == 5) {
    		evidence.put("cancellation_policy_disclosure", request.getParameter("cancellation_policy_disclosure"));
    		evidence.put("cancellation_rebuttal", request.getParameter("cancellation_rebuttal"));
    		evidence.put("cancellation_policy",uploadFile(request.getParameter("cancellation_policy")));
    		evidence.put("customer_communication",uploadFile(request.getParameter("customer_communication")));
    	}else if(reson == 6) {
    		evidence.put("duplicate_charge_id", request.getParameter("duplicate_charge_id"));
    		evidence.put("duplicate_charge_explanation", request.getParameter("duplicate_charge_explanation"));
    		evidence.put("duplicate_charge_documentation", uploadFile(request.getParameter("duplicate_charge_documentation")));
    		
    		String  submitFor = request.getParameter("submit_for");
    		if(StringUtils.isBlank(submitFor)) {
    			evidence.put("shipping_documentation", uploadFile(request.getParameter("shipping_documentation")));
    		}else if("service".equals(submitFor)) {
    			evidence.put("service_documentation", uploadFile(request.getParameter("service_documentation")));
    		}
    		
    	}else if(reson == 7) {
    		evidence.put("refund_policy", request.getParameter("refund_policy"));
    		evidence.put("refund_policy_disclosure", request.getParameter("refund_policy_disclosure"));
    		evidence.put("refund_refusal_explanation", request.getParameter("refund_refusal_explanation"));
    	}
    	
    	try {
    		Dispute dispute = stripeService.update(disputeid, evidence );
    		attr.addAttribute("sendResult", "Successed");
		} catch (Exception e) {
			attr.addAttribute("sendResult", e.getMessage());
		}
    	
    	return "redirect:/customer/dispute/info?disputeid="+disputeid+"&isread=true";
    }
    
    /**
     * @param file
     * @return
     */
    public String uploadFile(String file) {
    	if(StringUtils.isNotBlank(file)) {
    		Map<String, Object> fileParam = new HashMap<String, Object>();
    		fileParam.put("purpose", "dispute_evidence");
    		fileParam.put("file", new File(file));
    		com.stripe.model.File createFile = stripeService.createFile(fileParam);
    		if(createFile == null) {
    			return null;
    		}
    		return createFile.getFilename();
    	}
    	return null;
    }
}
