package com.cbt.website.dao2;

import com.cbt.website.bean.ProductBean;
import com.cbt.website.bean.UserOrderDetails;
import com.cbt.website.util.IOrderOnlineSampleBasic;
import fpx.api.client.OrderOnlineDelegate;
import fpx.api.orderonline.entity.CreateAndPreAlertOrderRequest;
import fpx.api.orderonline.entity.CreateAndPreAlertOrderResponse;
import fpx.api.orderonline.entity.DeclareInvoice;
import fpx.api.orderonline.entity.Error;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Terence chen 2012-7-2 下午3:19:15
 * @version 1.0
 */
public class CreateAndPreAlertOrderSample{
    /**
     * 创建并预报订单API
     */
    public String createAndPreAlertOrderService(UserOrderDetails uod){
        /**
         * 封装订单信息
         */
        CreateAndPreAlertOrderRequest objRequest = getCreateAndPreAlertOrderRequest(uod);
        /**
         * 创建并预报订单支持批量操作,这里只创建并预报单个订单
         */
        CreateAndPreAlertOrderRequest[] aobjRequest = new CreateAndPreAlertOrderRequest[1];
        aobjRequest[0] = objRequest;
        /**
         * 创建4PX提供的SDK调用方式代理类 - 在线订单操作代理类
         */
        OrderOnlineDelegate objDelegate = new OrderOnlineDelegate();
        /**
         * 远程调用创建并预报订单API
         */
        CreateAndPreAlertOrderResponse[] aobjResponse = null;
        try{
            aobjResponse = objDelegate.createAndPreAlertOrderService(IOrderOnlineSampleBasic.PRODUCT_AUTHTOKEN, aobjRequest);
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        /**
         * 打印返回结果
         */
        printResponse(aobjResponse);
        if(aobjResponse[0].getAck().equals("Success")){
			return aobjResponse[0].getTrackingNumber();
		} else {
			return "Failure";
		}
    }
    /**
     * 封装订单信息
     * @return
     */
    public CreateAndPreAlertOrderRequest getCreateAndPreAlertOrderRequest(UserOrderDetails uod){
		CreateAndPreAlertOrderRequest objRequest = new CreateAndPreAlertOrderRequest();
		// 客户订单号码，由客户自己定义(length <=20)
		String temp=uod.getOrderno();
		if(uod.getOrderno().indexOf("_1") != -1){
			temp = uod.getOrderno().substring(0,uod.getOrderno().indexOf("_1"));
		}
		                                           
	//	objRequest.setOrderNo(temp);
		objRequest.setOrderNo(temp+(uod.getShipmentno().substring(3,uod.getShipmentno().length())));                                                    
		// 服务商跟踪号码，由4PX提供，客户如果有相应服务商跟踪号，也可以提供，但我司会校验是否有效，无效则重新分配(Length < =30)
		objRequest.setTrackingNumber("");
		// 产品代码，指DHL、新加坡小包挂号、联邮通挂号等，参照产品代码表(Length = 2)
		objRequest.setProductCode(uod.getTransport());// 新加坡小包挂号
		// 货物类型(默认：P)，参照货物类型表(Length = 1)
		objRequest.setCargoCode(uod.getGoodstype()); // 包裹
		// 付款类型(默认：P)，参照付款类型表(Length = 1)
		objRequest.setPaymentCode("P");// 预付
		// 起运国家二字代码，参照国家代码表(Length = 2)
		objRequest.setInitialCountryCode("CN"); // 中国
		// 目的国家二字代码，参照国家代码表(Length = 2)
		objRequest.setDestinationCountryCode(uod.getUserzone());// 美国
		// 货物件数(默认：1)(0 < Amount <= 999)
		objRequest.setPieces(uod.getGoodsnum());
		// 保险类型，参照保险类型表(Length = 2)
		// objRequest.setInsurType("6P");//0.6%保费
		// 保险价值(单位：USD)(0 < Amount <= [10,2])
		// objRequest.setInsurValue("100");
		// 买家ID(Length <= 30)
		objRequest.setBuyerId(uod.getUserid());
		// 发件人姓名(Length <= 60)
		objRequest.setShipperName(uod.getAdminname());
		// 发件人公司名称(Length <= 60)
		objRequest.setShipperCompanyName(uod.getAdmincompany());
		// 发件人地址(Length <= 180)
		objRequest.setShipperAddress(uod.getAdminaddress());
		// 发件人邮编(Length <= 10)
		objRequest.setShipperPostCode(uod.getAdmincode());
		// 发件人电话号码(Length <= 30)
		objRequest.setShipperTelephone(uod.getAdminphone());
		// 发件人传真号码(Length <= 30)
		objRequest.setShipperFax(uod.getAdminphone());
		// 收件人公司名称(Length <= 60)
		objRequest.setConsigneeCompanyName(uod.getUsercompany());
		// 收件人姓名(Length <= 60)
		objRequest.setConsigneeName(uod.getUserName());
		// 街道(Length <= 60)
		objRequest.setStreet(uod.getUserstreet());
		// 城市(Length <= 60)
		objRequest.setCity(uod.getUsercity());
		// 州 / 省(Length <= 60)
		objRequest.setStateOrProvince(uod.getUserstate());
		// 收件人邮编(Length <= 10)
		objRequest.setConsigneePostCode(uod.getUsercode());
		// 收件人电话号码(Length <= 30)
		objRequest.setConsigneeTelephone(uod.getUserphone());
		// 收件人传真号码(Length <= 30)
		objRequest.setConsigneeFax(uod.getUserphone());
		// 收件人Email(Length <= 50)
		objRequest.setConsigneeEmail(uod.getEmail());
		// 客户自己称的重量(单位：KG)(0 < Amount <= [10,2])
		objRequest.setCustomerWeight(uod.getWeight());
		// 交易ID(Length <= 30)
		objRequest.setTransactionId(uod.getOrderno());
		// 订单备注信息(Length <= 256)
		objRequest.setOrderNote(uod.getMark());///////#######
		// 小包退件标识(Length = 1)
		objRequest.setReturnSign("N");// 发件人要求退回
		
		List<ProductBean> proBeanList = new ArrayList<ProductBean>();
		proBeanList = uod.getProductBean();
		
		//申报信息
		int ppp = proBeanList.size();
		DeclareInvoice[] aobjDeclareInvoice = new DeclareInvoice[ppp];
		for(int p=0;p<ppp;p++){
			DeclareInvoice objDeclareInvoice = new DeclareInvoice();
			// 海关申报英文品名(Length <= 200)
			objDeclareInvoice.setEName(proBeanList.get(p).getProducenglishtname());
			// 海关申报中文品名(Length <= 200)
			objDeclareInvoice.setName(proBeanList.get(p).getProductname());///////#######
			System.out.println("中文名："+proBeanList.get(p).getProductname());
			// 件数(默认: 1)(0 < Amount <= 999)
			objDeclareInvoice.setDeclarePieces(proBeanList.get(p).getProductnum());
			// 申报单位类型代码(默认: PCE)，参照申报单位类型代码表(Length = 3)
			objDeclareInvoice.setDeclareUnitCode("PCE");// 件
			// 单价(0 < Amount <= [10,2])
			objDeclareInvoice.setUnitPrice(proBeanList.get(p).getProductprice());
			// 配货备注(Length <= 256)
			objDeclareInvoice.setDeclareNote(proBeanList.get(p).getProductremark());
			
//			objDeclareInvoice.setItemURL("HHH"); //URL
//			objDeclareInvoice.setHsCode("TTT");  //编号
			
			aobjDeclareInvoice[p] = objDeclareInvoice;
		}
		objRequest.setDeclareInvoice(aobjDeclareInvoice);
		return objRequest;
	}
    /**
     * 打印返回结果
     */
    public void printResponse(CreateAndPreAlertOrderResponse[] aobjResponse){
        if(aobjResponse != null && aobjResponse.length > 0){
            for(int i=0,iLength=aobjResponse.length;i<iLength;i++){
                CreateAndPreAlertOrderResponse objResponse = aobjResponse[i];
                //服务器响应时间
                System.out.println("Timestamp = "+objResponse.getTimestamp());
                //操作 成功: Success 失败: Failure
                System.out.println("Ack = "+objResponse.getAck());
                //引用单号，一般为客户单号
                System.out.println("ReferenceNumber = "+objResponse.getReferenceNumber());
                //服务商跟踪单号
                System.out.println("TrackingNumber = "+objResponse.getTrackingNumber());
                //错误信息
                Error[] aobjError =  objResponse.getErrors();
                if(aobjError !=null && aobjError.length > 0){
                    for(int j=0,iErrorLength=aobjError.length;j<iErrorLength;j++){
                        Error objError = aobjError[j];
                        //错识代码
                        System.out.println("Code = "+objError.getCode());
                        //错误详细内容中文描述
                        System.out.println("CnMessage = "+objError.getCnMessage());
                        //错误的处理方法中文描述
                        System.out.println("CnAction = "+objError.getCnAction());
                        //错误详细内容英文描述
                        System.out.println("EnMessage = "+objError.getEnMessage());
                        //错误的处理方法英文描述
                        System.out.println("EnAction = "+objError.getEnAction());
                        //错误信息补充说明
                        System.out.println("DefineMessage = "+objError.getDefineMessage());
                        System.out.println("*******************我是华丽的分割线******************************");
                    }
                }
                System.out.println("--------------------------------我是华丽的分割线---------------------------------");
            }
        }
    }

}

