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

@Controller
@RequestMapping("/customer/dispute")
public class CustomerDisputeController {
	 private static final Log LOG = LogFactory.getLog(GroupBuyManageCtrl.class);
	 @Autowired
	 private CustomerDisputeService customerDisputeService;
	 @Autowired
	 private APIService apiService;
	
	
	 /**
     * 
             *申诉消息列表
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
            json.setMessage("请登录后操作");
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
            // 查询所有数据库表中的类别
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
            json.setMessage("获取数据失败，原因：" + e.getMessage());
            LOG.error("获取数据失败，原因：" + e.getMessage());
        }
        return json;
    }
   
    
    
    
    /**申诉消息详情
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
             mv.addObject("message", "请登录后操作");
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
    	mv.addObject("success", 1);
		try {
			int confim = customerDisputeService.count(disputeID,"0");
			mv.addObject("confim", confim);
			String showDisputeDetails = "";//
			if(StringUtils.startsWith(disputeID, "PP")) {
				showDisputeDetails = apiService.showDisputeDetails(disputeID,merchant);
			}	
			LOG.info(showDisputeDetails);
//			System.out.println(showDisputeDetails);
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
				//原因
				String reason = infoByDisputeID.getString("reason");
				resonFlag = StringUtils.equals(reason, "MERCHANDISE_OR_SERVICE_NOT_RECEIVED") ? 1 : resonFlag;
				//状态
				String status = infoByDisputeID.getString("status");
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
   
    /**申诉消息回复
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
				//数据
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
    /**提议
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offer")
    public String makeOffer(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
        	attr.addFlashAttribute("sendResult", "请登录");
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
    	
    	attr.addFlashAttribute("sendResult", "Error:提议内容为空!");
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
   
    /**接受索赔
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/acceptClaim")
    public String acceptClaim(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
    	
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
        	attr.addFlashAttribute("sendResult", "请登录");
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
    	
    	
    	String invoice_id = request.getParameter("invoice_id");
    	bean.setTransactionID(invoice_id);
    	
    	String accept_claim_reason = request.getParameter("accept_claim_reason");
    	bean.setAcceptClaimReason(accept_claim_reason);
    	
    	attr.addFlashAttribute("sendResult", "Failed");
    	if(StringUtils.isBlank(content)) {
    		attr.addFlashAttribute("sendResult", "备注不能为空,请重新提交");
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
    /**提供证据
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
        in.put("evidence_type", evidence_type);
        
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
        
        in.put("evidence_info", evidence_info);
        in.put("notes", content);
		
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
    	//数据
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
			map.put("data", "无文件上传");
			return map;
		}
		String uploadPath = SysParamUtil.getParam("uploadPath");
		String saveFileUrl = uploadPath.endsWith("/")?uploadPath+"paypal":uploadPath+"/paypal";
		saveFileUrl = saveFileUrl + "/" + System.currentTimeMillis() + "/";
		
		File headPath = new File(saveFileUrl);//获取文件夹路径
//		//LOG.info(getRootPath(multipartRequest));
        if(!headPath.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
        	headPath.mkdirs();
        }
		//获取上传的所有文件名
		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile mpf = null; 
		List<String> fileList = new ArrayList<String>();
		while (itr.hasNext()) {
			//取出文件
			mpf = multipartRequest.getFile(itr.next());
			LOG.info(mpf.getOriginalFilename() + " 开始上传! 队列:" + files.size());
			//如果上传文件数大于10,则移除第一个文件
			if (files.size() >= 10)
				files.pop();
			//创建文件属性对象
			fileMeta = new FileMeta();
			String fileName= mpf.getOriginalFilename();
			fileMeta.setFileName(fileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			fileMeta.setBytes(mpf.getBytes());

			try {
				// 保存文件名
				String newFileName = FileUtil.getNewFileName(mpf);
				//输出(保存)文件
//				FileCopyUtils.copy(("\r\n--"+mpf+"--\r\n").getBytes(), new FileOutputStream(getRootPath(multipartRequest)+"/upimg/" +newFileName));
				FileCopyUtils.copy((mpf).getBytes(), new FileOutputStream(saveFileUrl+newFileName));
				//获取后缀名
				map.put("success", true);
				fileList.add(newFileName);
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("message", "File upload occured problem.");
			}
			//压入栈顶
			files.add(fileMeta);
		}
		map.put("names", fileList);
		map.put("saveLoaction", saveFileUrl);
		return map;
	}
    /**申诉消息详情
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
            json.setMessage("请登录后操作");
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
    				c.setRemark("Send Message To Buyer:"+c.getRemark()+"<br><br>(内部查看)Refuse Reason:"+c.getRefuseReason());
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
            json.setMessage("获取数据失败，原因：" + e.getMessage());
            LOG.error("获取数据失败，原因：" + e.getMessage());
    	}
    	return json;
    }
    /**退款确定
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
    		result.put("message", "请登录");
        	return  result;
        }
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if(!StringUtils.equals(adm.getRoletype(), "0")) {
    		result.put("state", false);
    		result.put("message", "没有权限退款，请重新登录");
        	return  result;
    	}
        
    	String disputeid = request.getParameter("disputeid");
    	CustomerDisputeBean comfirmByDisputeID = customerDisputeService.getComfirmByDisputeID(disputeid);
    	if(comfirmByDisputeID == null) {
    		result.put("state", false);
    		result.put("message",  "没有该退款信息，无法完成退款");
        	return  result;
    	}
    	//退款类型  0-接受paypal买家索赔accept claim   1-paypal卖家提议退款offer
    	String refundType = comfirmByDisputeID.getRefundType();
    	String disputeID = comfirmByDisputeID.getDisputeID();
    	String merchant = comfirmByDisputeID.getMerchantID();
    	String content = comfirmByDisputeID.getRemark();
    	String offerAmount = comfirmByDisputeID.getSellerOfferedAmount();
    	String currencyCode = comfirmByDisputeID.getCurrencyCode();
    	
    	boolean isPremint = Double.valueOf(offerAmount)<500.0099999;
    	if(!((adm.getId() == 1 && isPremint)||(adm.getId() == 8 && isPremint)||(adm.getId()==83))) {
    		result.put("state", false);
    		result.put("message", "该用户没有权限退款，请重新登录");
    		return  result;
    	}
    	try {
    		String invoice_id = comfirmByDisputeID.getTransactionID();
    		if(StringUtils.isBlank(content)) {
    			result.put("state", false);
        		result.put("message",    "退款无法完成,缺少退款备注");
    		}else {
    			//数据
        		JSONObject data = new JSONObject();
        		data.put("note", content);
        		if(StringUtils.isNotBlank(offerAmount)) {
        			JSONObject Amount = new JSONObject();
        			Amount.put("value", offerAmount);
        			Amount.put("currency_code", currencyCode);
        			data.put(StringUtils.equals(refundType, "0") ? "refund_amount" : "offer_amount" , Amount);
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
        		result.put("message",   "退款完成");
        		LOG.info(content);
    		}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("state", false);
			result.put("message",  "Error:"+e.getMessage());
		}
    	return  result;
    }
    /**退款确定
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
    		result.put("message", "请登录");
        	return  result;
        }
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if(!StringUtils.equals(adm.getRoletype(), "0")) {
    		result.put("state", false);
    		result.put("message", "没有权限退款，请重新登录");
        	return  result;
    	}

        if(!(adm.getId() == 1||adm.getId() == 8||adm.getId()==83)) {
    		result.put("state", false);
    		result.put("message", "该用户没有权限操作，请重新登录");
        	return  result;
    	}
    	
    	String disputeid = request.getParameter("disputeid");
    	String refuseReason = request.getParameter("reason");
    	if(StringUtils.isBlank(disputeid) || StringUtils.isBlank(refuseReason)) {
    		result.put("state", false);
    		result.put("message", "备注内容不能为空");
        	return  result;
    	}
    	Integer updateRefuseReason = customerDisputeService.updateRefuseReason(disputeid, refuseReason);
    	result.put("state", updateRefuseReason != null && updateRefuseReason > 0 ?true : false);
    	result.put("message", updateRefuseReason != null && updateRefuseReason > 0 ? "操作成功" : "操作失败");
    	return  result;
    }
    /**提议
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
}
