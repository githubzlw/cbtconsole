package com.importExpress.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbt.pojo.BuyForMeStatistic;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.UploadByOkHttp;
import com.google.gson.Gson;
import com.importExpress.pojo.*;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.common.aliasing.qual.LeakedToResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.ImgDownload;
import com.cbt.pojo.Admuser;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.util.StrUtils;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.UploadByOkHttp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.importExpress.pojo.*;
import com.importExpress.service.BuyForMeService;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/bf")
public class BuyForMeController {

    @Autowired
    private BuyForMeService buyForMeService;


    @RequestMapping("/orders")
    @ResponseBody
    public ModelAndView lstOrders(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("buyforme_mg");
        try {

            Map<String, Object> map = Maps.newHashMap();
            String strPage = request.getParameter("page");
            strPage = StrUtils.isNum(strPage) ? strPage : "1";

            String strState = request.getParameter("state");
            strState = StringUtils.isBlank(strState) ? "-2" : strState;

            String userId = request.getParameter("userid");
            String orderno = request.getParameter("orderno");

            String admid = request.getParameter("admid");

            int current_page = Integer.parseInt(strPage);
            map.put("current_page", current_page);
            map.put("page", (current_page - 1) * 30);
            map.put("state", Integer.valueOf(strState));
            map.put("orderNo", StringUtils.isBlank(orderno) ? null : orderno);
            map.put("userId", StringUtils.isBlank(userId) ? null : userId);
            map.put("admid", StringUtils.isBlank(admid) ? "0" : admid);

            int ordersCount = buyForMeService.getOrdersCount(map);
            if (ordersCount > 0) {
                List<BFOrderInfo> orders = buyForMeService.getOrders(map);
                mv.addObject("orders", orders);
            }
            List<Admuser> lstAdms = buyForMeService.lstAdms();
            mv.addObject("lstAdms", lstAdms);

            int totalPage = ordersCount % 30 == 0 ? ordersCount / 30 : ordersCount / 30 + 1;
            mv.addObject("listCount", ordersCount);
            mv.addObject("totalPage", totalPage);
            mv.addObject("queryParam", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 申请单详情
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ModelAndView orderDetail(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView mv = new ModelAndView("buyforme_detail");
    	try {
			
    		String orderNo = request.getParameter("no");
    		String bfid = request.getParameter("bfid");
    		List<BFOrderDetail> orderDetails = buyForMeService.getOrderDetails(orderNo,bfid);
    		mv.addObject("orderDetails", orderDetails);
    		//订单状态：-1 取消，0申请，1处理中 2销售处理完成 3已支付
    		
    		Map<String, Object> order = buyForMeService.getOrder(orderNo);
    		if(order != null) {
    			int state = Integer.parseInt(StrUtils.object2Str(order.get("state")));
    			String strState = state == -1?"申请已取消":state==0?
    					"申请待处理":state==1?"申请处理中":state==2?"销售处理完成":state==3?"待支付":state==4?"已支付":"";
    			order.put("stateContent", strState);
    			String delivery_method = StrUtils.object2Str(order.get("delivery_method"));
    			order.put("delivery_method", delivery_method);
    		}
    		List<Map<String,String>> remark = buyForMeService.getRemark(orderNo);
    		mv.addObject("remark", remark);
    		mv.addObject("order", order);
    		
    		List<ZoneBean> lstCountry = buyForMeService.lstCountry();
    		mv.addObject("countrys", lstCountry);
    		
//    		Map<String, List<String>> transport = buyForMeService.getTransport();
//    		mv.addObject("transport", transport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 获取交期方式
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/transport")
    @ResponseBody
    public Map<String, Object> transport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {

            List<TransportMethod> transport = buyForMeService.getTransport();
            mv.put("methodList", transport);
            mv.put("state", transport.size() > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 录入、修改规格
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Map<String, Object> addDetailSku(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String id = request.getParameter("id");
            String delete = request.getParameter("delete");
            int addOrderDetailsSku = 0;
            if (StringUtils.isNotBlank(delete) && "1".equals(delete)) {
                addOrderDetailsSku = buyForMeService.updateOrderDetailsSkuState(StringUtils.isNotBlank(id) ? Integer.parseInt(id) : 0, -1);
            } else {
                String bfId = request.getParameter("bfid");
                String bfDetailsId = request.getParameter("bfdid");
                String num = request.getParameter("num");
                BFOrderDetailSku detailSku = new BFOrderDetailSku();
                detailSku.setBfDetailsId(Integer.parseInt(bfDetailsId));
                detailSku.setBfId(Integer.parseInt(bfId));
                detailSku.setId(StringUtils.isNotBlank(id) ? Integer.parseInt(id) : 0);
                detailSku.setNum(Integer.parseInt(num));
                detailSku.setNumIid(request.getParameter("numiid"));
                detailSku.setPrice(request.getParameter("price"));
                detailSku.setPriceBuy(request.getParameter("priceBuy"));
                detailSku.setPriceBuyc(request.getParameter("priceBuyc"));
                detailSku.setShipFeight(request.getParameter("shipFeight"));
                detailSku.setProductUrl(request.getParameter("url"));
                detailSku.setSku(request.getParameter("sku"));
                detailSku.setWeight(request.getParameter("weight"));
                detailSku.setUnit(request.getParameter("unit"));
                String skuid = "";
                detailSku.setSkuid(skuid);
                detailSku.setState(1);
                addOrderDetailsSku = buyForMeService.addOrderDetailsSku(detailSku);
            }
            mv.put("state", addOrderDetailsSku > 0 ? 200 : 500);
            mv.put("orderDetails", addOrderDetailsSku);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 无效规格
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/invalid")
    @ResponseBody
    public Map<String, Object> invalidDetailSku(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {

            String id = request.getParameter("id");
            int updateOrderDetailsSkuState =
                    buyForMeService.updateOrderDetailsSkuState(StringUtils.isNotBlank(id) ? Integer.parseInt(id) : 0, 0);

            mv.put("state", updateOrderDetailsSkuState > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 无效规格
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/invalidproduct")
    @ResponseBody
    public Map<String, Object> invalidProduct(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {

            String bfdid = request.getParameter("bfdid");
            int update =
                    buyForMeService.deleteProduct(StringUtils.isNotBlank(bfdid) ? Integer.parseInt(bfdid) : 0);

            mv.put("state", update > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 确认交期时间
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/time")
    @ResponseBody
    public Map<String, Object> deliveryTime(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String orderNo = request.getParameter("orderNo");
            String time = request.getParameter("time");
            String feight = request.getParameter("feight");
            String method = request.getParameter("method");
            int update = buyForMeService.updateDeliveryTime(orderNo, time, feight, method);
            mv.put("state", update > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 内部回复
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/remark")
    @ResponseBody
    public Map<String, Object> remark(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String orderNo = request.getParameter("orderNo");
            String remark = request.getParameter("remark");
            int update = buyForMeService.insertRemark(orderNo, remark);
            mv.put("state", update > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 修改重量
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/weight")
    @ResponseBody
    public Map<String, Object> weightDetailSku(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String weight = request.getParameter("weight");
            String sbfdid = request.getParameter("bfdid");
            int bfdid = StrUtils.isNum(sbfdid) ? Integer.valueOf(sbfdid) : 0;
            int updateOrderDetailsSkuState =
                    buyForMeService.updateOrderDetailsSkuWeight(weight, bfdid);

            mv.put("state", updateOrderDetailsSkuState > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 回复客户备注
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/deremark")
    @ResponseBody
    public Map<String, Object> replayRemark(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String remark = request.getParameter("remark");
            String sbfdid = request.getParameter("bfdid");
            String pid = request.getParameter("pid");
            String order_no = request.getParameter("order_no");
            int bfdid = StrUtils.isNum(sbfdid) ? Integer.parseInt(sbfdid) : 0;

            if (StringUtils.isBlank(sbfdid) || StringUtils.isBlank(pid) || StringUtils.isBlank(order_no)) {
                mv.put("state", 500);
                mv.put("message", "param error");
                return mv;
            }

            List<String> lstValues = Lists.newArrayList();
            String sql = "insert into buyforme_pid_chat(pid,bd_id,order_no,content) values(?,?,?,?)";

            lstValues.add(pid);
            lstValues.add(String.valueOf(bfdid));
            lstValues.add(order_no);
            lstValues.add(GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(remark));
            String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(DBHelper.covertToSQL(sql, lstValues)));

            int updateOrderDetailsSkuState = 0;
            if (StringUtils.isNotBlank(sendMsgByRPC) && Integer.parseInt(sendMsgByRPC) > 0) {
                updateOrderDetailsSkuState = buyForMeService.updateOrdersDetailsRemark(bfdid, remark);
				/*BFChat bfChat = new BFChat();
				bfChat.setBd_id(bfdid);
				bfChat.setPid(pid);
				bfChat.setContent(remark);
				updateOrderDetailsSkuState = buyForMeService.insertBFChat(bfChat);*/
            }
            mv.put("state", updateOrderDetailsSkuState > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 回复客户备注
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/adms")
    @ResponseBody
    public Map<String, Object> lstAdms(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            List<Admuser> lstAdms = buyForMeService.lstAdms();
            mv.put("state", 200);
            mv.put("lstAdms", lstAdms);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 回复客户备注
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cancel/order")
    @ResponseBody
    public Map<String, Object> cancelOrder(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String strid = request.getParameter("bfid");
            strid = StrUtils.isNum(strid) ? strid : "0";
            int update = buyForMeService.cancelOrders(Integer.parseInt(strid));
            mv.put("state", update > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 修改地址
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/address")
    @ResponseBody
    public Map<String, Object> address(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String id = request.getParameter("id");
            String country = request.getParameter("country");
            String statename = request.getParameter("statename");
            String address = request.getParameter("address");
            String street = request.getParameter("street");
            String address2 = request.getParameter("address2");
            String phone = request.getParameter("phone");
            String code = request.getParameter("code");
            String recipients = request.getParameter("recipients");
            Map<String, String> map = Maps.newHashMap();
            map.put("address", address);
            map.put("address2", address2);
            map.put("country", country);
            map.put("phone", phone);
            map.put("code", code);
            map.put("statename", statename);
            map.put("street", street);
            map.put("recipients", recipients);
            map.put("id", id);
            int update =
                    buyForMeService.updateOrdersAddress(map);

            mv.put("state", update > 0 ? 200 : 500);
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 确认
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/finsh")
    @ResponseBody
    public Map<String, Object> finsh(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mv = Maps.newHashMap();
        try {
            String bfId = request.getParameter("bfid");
            bfId = StrUtils.isNum(bfId) ? bfId : "0";
            int addOrderDetailsSku = buyForMeService.finshOrder(Integer.parseInt(bfId));
            mv.put("state", addOrderDetailsSku > 0 ? 200 : 500);
            List<String> lstValues = Lists.newArrayList();
            if (addOrderDetailsSku > 0) {
                String sql1 = "update buyforme_orderinfo set state=? where id=?";
                lstValues.add("2");
                lstValues.add(bfId);
                SendMQ.sendMsgByRPC(new RunSqlModel(DBHelper.covertToSQL(sql1, lstValues)));
            }


            List<BFOrderDetailSku> orderDetailsSku = buyForMeService.getOrderDetailsSku(bfId);
            String sql1 = " update buyforme_details_sku set sku=?,product_url=?,num=?,price=?,price_buy=?,price_buy_c=?,ship_feight=?,weight=?,unit=? ,state=? where id=?";
            String sql2 = "insert into buyforme_details_sku(sku,product_url,num,price,price_buy,price_buy_c,ship_feight,weight,unit,state,id,bf_id,bf_details_id,num_iid,skuid,remark)" +
                    "  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            lstValues.clear();
            for (BFOrderDetailSku o : orderDetailsSku) {
                lstValues.clear();
                lstValues.add(o.getSku());
                lstValues.add(o.getProductUrl());
                lstValues.add(String.valueOf(o.getNum()));
                lstValues.add(o.getPrice());
                lstValues.add(o.getPriceBuy());
                lstValues.add(o.getPriceBuyc());
                lstValues.add(o.getShipFeight());
                lstValues.add(o.getWeight());
                lstValues.add(o.getUnit());
                lstValues.add(String.valueOf(o.getState()));
                lstValues.add(String.valueOf(o.getId()));
                String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(DBHelper.covertToSQL(sql1, lstValues)));
                if (Integer.parseInt(sendMsgByRPC) < 1) {
                    lstValues.add(String.valueOf(o.getBfId()));
                    lstValues.add(String.valueOf(o.getBfDetailsId()));
                    lstValues.add(o.getNumIid());
                    lstValues.add(o.getSkuid());
                    lstValues.add(o.getRemark());
                    lstValues.add(String.valueOf(o.getState()));
                    SendMQ.sendMsg(new RunSqlModel(DBHelper.covertToSQL(sql2, lstValues)));
                }
            }
        } catch (Exception e) {
            mv.put("state", 500);
            e.printStackTrace();
        }
        return mv;
    }

    private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();

    /**
     * 接受上传文件
     *
     * @param request
     * @param response
     * @return
     * @date 2016年12月16日
     * @author abc
     */
    @RequestMapping(value = "/xheditorUploads", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> xheditorUploads(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "";
        String err = "";
        try {
            String orderNo = request.getParameter("orderNo");
            if (StringUtils.isBlank(orderNo)) {
                msg = "";
                err = "获取PID失败";
            } else {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                // 获取文件域
                List<MultipartFile> fileList = multipartRequest.getFiles("filedata");
                Random random = new Random();
                // 获取配置文件信息
                if (ftpConfig == null) {
                    ftpConfig = GetConfigureInfo.getFtpConfig();
                }
                // 检查配置文件信息是否正常读取
                JsonResult json = new JsonResult();
                GetConfigureInfo.checkFtpConfig(ftpConfig, json);
                if (json.isOk()) {
                    String localDiskPath = ftpConfig.getLocalDiskPath();
                    for (MultipartFile mf : fileList) {
                        if (!mf.isEmpty()) {
                            // 得到文件保存的名称mf.getOriginalFilename()
                            String originalName = mf.getOriginalFilename();
                            // 文件的后缀取出来
                            String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                            String saveFilename = GoodsInfoUtils.makeFileName(String.valueOf(random.nextInt(1000)));
                            // 本地服务器磁盘全路径
                            String localFilePath = "buyforme/remark/" + saveFilename + fileSuffix;
                            // 文件流输出到本地服务器指定路径
                            ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
                            File file = new File(localDiskPath + localFilePath);
                            if (file.exists()) {
                                // msg = ftpConfig.getLocalShowPath() + localFilePath;
                                String remotePath = "/usr/local/goodsimg/importcsvimg/buyforme/" + orderNo;
                                boolean isSuccess = UploadByOkHttp.uploadFile(file, remotePath, 0);
                                if (!isSuccess) {
                                    isSuccess = UploadByOkHttp.uploadFile(file, remotePath, 0);
                                }
                                if (isSuccess) {
                                    //返回新的链接
                                    msg = ftpConfig.getRemoteShowPath() + "buyforme/" + orderNo + "/" + file.getName();
                                    json.setOk(true);
                                } else {
                                    msg = "";
                                    err = "上传错误";
                                }
                            }
                        }
                    }
                } else {
                    msg = "";
                    err = json.getMessage();
                }
            }
        } catch (Exception e) {
            msg = "";
            err = "上传错误";
            e.printStackTrace();
            log.error("上传错误：" + e.getMessage());
        }
        map.put("err", err);
        map.put("msg", msg);
        return map;
    }

    @RequestMapping("/searchList")
    @ResponseBody
    public JsonResult searchList(HttpServletRequest request, String beginTime, String endTime, Integer searchType,
                                 Integer userId, String sessionId, Integer page, Integer rows) {
        JsonResult json = new JsonResult();
        try {
            BuyForMeSearchLog searchLog = new BuyForMeSearchLog();
            if (rows == null) {
                rows = 30;
            }
            if (page == null) {
                page = 1;
            }
            searchLog.setStartNum((page - 1) * rows);
            searchLog.setLimitNum(rows);
            if (StringUtils.isNotBlank(beginTime)) {
                searchLog.setCreate_time(beginTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                LocalDate today = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                searchLog.setEnd_time(today.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            if (null != searchType && searchType > 0) {
                searchLog.setSearch_type(searchType);
            }
            if (null != userId && userId > 0) {
                searchLog.setUser_id(userId);
            }
            if (StringUtils.isNotBlank(sessionId)) {
                searchLog.setSession_id(sessionId);
            }

            int total = buyForMeService.querySearchListCount(searchLog);
            if (total > 0) {
                List<BuyForMeSearchLog> searchLogs = buyForMeService.querySearchList(searchLog);

                json.setSuccess(searchLogs, total);
            } else {
                json.setSuccess(new ArrayList<>(), 0);
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("searchList beginTime:[{}],endTime:[{}],searchType:[{}]", beginTime, endTime, searchType, e);
            json.setErrorInfo(e.getMessage());
            return json;
        }
    }

    /**
     * 确认
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryBFChatList")
    @ResponseBody
    public JsonResult queryBFChatList(HttpServletRequest request, HttpServletResponse response, BFChat bfChat) {
        try {
            Assert.notNull(bfChat, "bfChat null");
            Assert.notNull(bfChat.getBd_id(), "bd_id null");
            List<BFChat> bfChatList = buyForMeService.queryBFChatList(bfChat);
            return JsonResult.success(bfChatList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("queryBFChatList,bfChat[{}],error", bfChat, e);
            return JsonResult.error(e.getMessage());
        }
    }
    @RequestMapping("/queryCustomers")
	@ResponseBody
	public EasyUiJsonResult queryCustomers(HttpServletRequest request, HttpServletResponse response){
		EasyUiJsonResult json = new EasyUiJsonResult();
		ShopCarUserStatistic statistic = new ShopCarUserStatistic();
		int startNum = 0;
		int limitNum = 10;
		String rowsStr = request.getParameter("rows");
		if (StringUtils.isNotBlank(rowsStr)) {
			limitNum = Integer.valueOf(rowsStr);
		}
		String pageStr = request.getParameter("page");
		if (StringUtils.isNotBlank(pageStr)) {
			startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
		}

		String userIdStr = request.getParameter("userId");
		int userId = 0;
		if (StringUtils.isNotBlank(userIdStr)) {
			userId = Integer.parseInt(userIdStr);
		}
		String adminId = request.getParameter("adminId");
		if (StringUtils.isNotBlank(adminId) && !adminId.equals("0")) {
			statistic.setAdmname (adminId);
		}
		statistic.setUserId(userId);
		statistic.setStartNum(startNum);
		statistic.setLimitNum(limitNum);
		json = buyForMeService. queryCustomers(statistic);
    	return json;
	}
	@GetMapping("/{userId}")
	public ModelAndView getCartDetailByCustomerId(@PathVariable(value = "userId") String userId, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("buyforme_cart_detail");
		JsonResult customerCartDetails = buyForMeService.getCustomerCartDetails(userId);
		List<ZoneBean> lstCountry = buyForMeService.lstCountry();
		mv.addObject("countrys", lstCountry);
		if(customerCartDetails.getData() != null){
            request.setAttribute("result",customerCartDetails.getData());
			//mv.addObject("result",new Gson().toJson(customerCartDetails.getData()));
		}
		return mv;
	}
	@PostMapping("/{userId}/{itemid}")
	@ResponseBody
	public CommonResult putMsg(@PathVariable(value = "userId") String userId,
							   @PathVariable(value = "itemid") String itemid,
							   @RequestParam(value = "msg",defaultValue = "1")String msg){
		CommonResult commonResult = buyForMeService.putMsg(userId, itemid, msg);
		return commonResult;
	}

	@RequestMapping("/updateCountryList")
    @ResponseBody
    public CommonResult updateCountryList() {
        try {
            int limitNum = 500;
            BuyForMeSearchLog searchLog = new BuyForMeSearchLog();
            int count = buyForMeService.querySearchListCount(searchLog);
            int fc = 0;
            if (count > 0) {
                fc = count / limitNum;
                if (fc % limitNum > 0) {
                    fc++;
                }
                searchLog.setLimitNum(limitNum);
                count = 0;
                for (int i = 1; i <= fc; i++) {
                    searchLog.setStartNum((i - 1) * limitNum);
                    List<BuyForMeSearchLog> searchLogList = buyForMeService.querySearchList(searchLog);
                    if (CollectionUtils.isNotEmpty(searchLogList)) {
                        System.err.println("i/fc:" + i + "/" + fc + ", size:" + searchLogList.size());
                        try {
                            count += buyForMeService.updateSearchLogList(searchLogList);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("updateCountryList error:", e);
                        }
                    } else {
                        System.err.println("i/fc:" + i + "/" + fc + ", size:0");
                    }
                }
            }
            return CommonResult.success(count);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateCountryList error:", e);
            return CommonResult.failed(e.getMessage());
        }
    }

}
