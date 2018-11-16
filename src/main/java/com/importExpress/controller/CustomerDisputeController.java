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
import com.cbt.util.SysParamUtil;
import com.cbt.warehouse.ctrl.GroupBuyManageCtrl;
import com.cbt.website.util.EasyUiJsonResult;
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
            sttime += "T00:00:00.000Z";
        }
        String edtime = request.getParameter("edtime");
        if (edtime == null || "".equals(edtime)) {
            edtime = "";
        } else {
            edtime += "T23:59:59.000Z";
        }
        String status = request.getParameter("status");
        status = StringUtils.equals("-1", status) ? null : status;
        status = StringUtils.equals("0", status) ? "OPEN" : status;
        status = StringUtils.equals("1", status) ? "WAITING_FOR_SELLER_RESPONSE" : status;
        status = StringUtils.equals("3", status) ? "UNDER_REVIEW" : status;
        status = StringUtils.equals("4", status) ? "RESOLVED" : status;
        
        try {
            // 查询所有数据库表中的类别
        	
        	Map<String, Object> map = customerDisputeService.list(disputeid,startNum, limitNum, sttime, edtime, status);
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
            json.setMessage("获取团购商品信息失败，原因：" + e.getMessage());
            LOG.error("获取团购商品信息失败，原因：" + e.getMessage());
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
    	String disputeID = request.getParameter("disputeid");
    	JSONObject infoByDisputeID = null;
    	int resonFlag = 0;
    	int statusFlag = 0;
    	int disputeLifeCycleFlag = 0;
		try {
			String showDisputeDetails = "";//customerDisputeService.info(disputeID);
			if(StringUtils.isBlank(showDisputeDetails)) {
				showDisputeDetails = apiService.showDisputeDetails(disputeID);
			}		
			if(StringUtils.isNotEmpty(showDisputeDetails)) {
				infoByDisputeID = JSONObject.parseObject(showDisputeDetails);
			}
			if(infoByDisputeID != null) {
				mv.addObject("result", infoByDisputeID);
				JSONArray disputedTransactions = (JSONArray)infoByDisputeID.get("disputed_transactions");
				
				String custom = ((JSONObject)disputedTransactions.get(0)).getString("custom");
				mv.addObject("orderNo", "");
				if(StringUtils.indexOf(custom, "@") > -1) {
					if(custom.indexOf("{@}") > -1) {
						String[] split = custom.split("\\{@\\}");
						if(split.length > 3) {
							mv.addObject("userid", split[0]);
							mv.addObject("orderNo", split[2]);
						}
					}else {
						String[] split = custom.split("@");
						if(split.length == 10) {
							mv.addObject("userid", split[0]);
							mv.addObject("orderNo", split[6]);
						}
					}
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
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mv.addObject("error", e.getMessage());
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
					result = apiService.sendMessage(disputeID, data);
//				}
				System.out.println(result);
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
    	String content = request.getParameter("messageBodyForGeneric");
    	String offerAmount = request.getParameter("refundAmount");
    	String offerType = request.getParameter("offerType");
    	String currencyCode = request.getParameter("refundCurrency");
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
    	attr.addFlashAttribute("sendResult", "Error:提议内容为空!");
    	if(StringUtils.isBlank(content)) {
    		return "redirect:/customer/dispute/info?disputeid="+disputeID;
    	}
    	//数据
    	JSONObject data = new JSONObject();
    	data.put("note", content);
//    	data.put("invoice_id", invoice_id);
    	if(StringUtils.isNotBlank(offerAmount)) {
    		JSONObject amount = new JSONObject();
    		amount.put("currency_code", currencyCode);
    		amount.put("value", offerAmount);
    		data.put("offer_amount", amount);
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
    		data.put("return_shipping_address", address);
    	}
    	data.put("offer_type", offerType);
    	System.out.println(JSONObject.toJSONString(data));
		
    	String result = null;
		try {
			result = apiService.makeOffer(disputeID, data);
			attr.addFlashAttribute("sendResult", "Successed");
			System.out.println(result);
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
    	String content = request.getParameter("messageBodyForGeneric");
    	String refundAmount = request.getParameter("refundAmount");
    	String refundCurrency = request.getParameter("refundCurrency");
    	String invoice_id = request.getParameter("invoice_id");
    	String accept_claim_reason = request.getParameter("accept_claim_reason");
    	attr.addFlashAttribute("sendResult", "Failed");
    	if(StringUtils.isBlank(content)) {
    		return "redirect:/customer/dispute/info?disputeid="+disputeID;
    	}
//    	"DID_NOT_SHIP_ITEM"
    	//数据
    	JSONObject data = new JSONObject();
    	data.put("note", content);
    	data.put("accept_claim_reason", accept_claim_reason);
    	data.put("invoice_id", invoice_id);
    	
    	if(StringUtils.isNotBlank(refundAmount)) {
    		JSONObject Amount = new JSONObject();
    		Amount.put("value", refundAmount);
    		Amount.put("currency_code", refundCurrency);
    		data.put("refund_amount", Amount);
    	}
		System.out.println(JSONObject.toJSONString(data));
    	String result = null;
		try {
			result = apiService.acceptClaim(disputeID, data);
			attr.addFlashAttribute("sendResult", "Successed");
			System.out.println(result);
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
    @ResponseBody
    public String provideEvidence(HttpServletRequest request, HttpServletResponse response) {
    	String disputeID = request.getParameter("disputeid");
    	String content = request.getParameter("messageBodyForGeneric");
    	String fileLocation = request.getParameter("fileLocation");
    	
    	System.out.println("fileLocation:"+fileLocation);
    	File file = new File(fileLocation);
    	
    	//数据
    	JSONObject data = new JSONObject();
    	data.put("note", content);
    	data.put("evidence_type", "PROOF_OF_FULFILLMENT");
    	
    	
    	JSONObject returnAddress =  new JSONObject();
    	returnAddress.put("address_line_1", "14,Kimberly st");
    	returnAddress.put("address_line_2", "Open Road North");
    	returnAddress.put("country_code", "US");
    	returnAddress.put("admin_area_1", "Gotham City");
    	returnAddress.put("admin_area_2", "Gotham");
    	returnAddress.put("postal_code", "124566");
    	
    	JSONObject evidence_info =  new JSONObject();
    	
    	JSONObject tracking =  new JSONObject();
    	tracking.put("carrier_name", "FEDEX");
    	tracking.put("tracking_number", "122533485");
    	
    	List<JSONObject> tracking_info = new ArrayList<JSONObject>();
    	tracking_info.add( tracking);
    	
    	evidence_info.put("tracking_info", tracking_info);
    	
    	
    	data.put("evidence_info", evidence_info);
    	
    	data.put("return_shipping_address", returnAddress);
    	
    	
    	String result = null;
		try {
			result = apiService.provideEvidence(disputeID, JSONObject.toJSONString(data),file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
//			attr.addFlashAttribute("sendResult", "Error:"+e.getMessage());
		}
    	System.out.println(result);
    	
    	return result;
    }
    /**Acknowledge returned item
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/acknowledge-return-item")
    public String acknowledgeReturnedItem(HttpServletRequest request, HttpServletResponse response,RedirectAttributes attr) {
    	String disputeID = request.getParameter("disputeid");
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
    		result = apiService.acknowledgeReturnedItem(disputeID, data);
    		attr.addFlashAttribute("sendResult", "Successed");
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		result = e.getMessage();
    		attr.addFlashAttribute("sendResult", "Error:"+e.getMessage());
    	}
    	System.out.println(result);
    	
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
//		//System.out.println(getRootPath(multipartRequest));
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
			System.out.println(mpf.getOriginalFilename() + " 开始上传! 队列:" + files.size());
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
    
}
