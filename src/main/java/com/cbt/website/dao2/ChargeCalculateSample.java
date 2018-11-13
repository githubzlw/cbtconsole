package com.cbt.website.dao2;

import com.cbt.website.util.IOrderOnlineSampleBasic;
import fpx.api.client.OrderOnlineToolDelegate;
import fpx.api.orderonlinetool.entity.CalculateFee;
import fpx.api.orderonlinetool.entity.ChargeCalculateRequest;
import fpx.api.orderonlinetool.entity.ChargeCalculateResponse;
import fpx.api.orderonlinetool.entity.Errors;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * @author Terence chen 2012-7-4 上午10:29:31
 * @version 1.0
 */
public class ChargeCalculateSample{
	
    // 运费试算API
    public String chargeCalculateService(String country,String weight,String lwh,String pack,String procode,String currency, Map<String, Double> maphl){
        OrderOnlineToolDelegate objDelegate = new OrderOnlineToolDelegate();
        ChargeCalculateRequest objRequest = getChargeCalculateRequest(country,weight,lwh,pack,procode);
        ChargeCalculateResponse objResponse = null;
        try{
            objResponse = objDelegate.chargeCalculateService(IOrderOnlineSampleBasic.PRODUCT_AUTHTOKEN, objRequest); 
        } catch (RemoteException e){
            e.printStackTrace();
        }
        printResponse(objResponse);// 打印返回结果
        if(objResponse.getAck().equals("Success")){
        	DecimalFormat df = new DecimalFormat("0.00");
    		double excrate = maphl.get(currency) / maphl.get("RMB");
    		double rmn = Double.parseDouble(objResponse.getCalculateFee()[0].getTotalRmbAmount())*excrate;//RMB->currency
        	return df.format(rmn)+"#"+objResponse.getCalculateFee()[0].getDeliveryperiod();
        } else {
        	return "count_failure";
        }
        
    }
    // 封装运费试算请求参数
    public ChargeCalculateRequest getChargeCalculateRequest(String country,String weight,String lwh,String pack,String procode){
        ChargeCalculateRequest objRequest = new ChargeCalculateRequest();
        //目的国家二字代码，参照国家代码表(Length = 2)
        objRequest.setCountryCode(country);
        //计费重量，单位(kg)(0 < Amount <= [3,3])
        objRequest.setWeight(weight);
        String[] vo = (lwh.replace("*", ",")).split(",");
        double d0 = Double.parseDouble(vo[0])/100;
        double d1 = Double.parseDouble(vo[1])/100;
        double d2 = Double.parseDouble(vo[2])/100;
        vo[0] = d0+"";
        vo[1] = d1+"";
        vo[2] = d2+"";
        objRequest.setLength(vo[0]);
        objRequest.setWidth(vo[1]);
        objRequest.setHeight(vo[2]);
        //货物类型(默认：P)(Length = 1)
        objRequest.setCargoCode(pack);
        //计费结果产品显示级别(默认：1)(Length = 1)
        //objRequest.setDisplayOrder("1");
        //产品代码,该属性不为空，只返回该产品计费结果，参照产品代码表(Length = 2)
        String[] astrPkCode = new String[1];
        astrPkCode[0]=procode;
        objRequest.setProductCode(astrPkCode);
        //起运地ID，参照起运地ID表(Length <= 4)
        //objRequest.setStartShipmentId("10");
        //计费体积，单位(c㎡)(0 < Amount <= [3,3])
        //objRequest.setVolume("20");
        //邮编(Length <= 10)
        //objRequest.setPostCode("518000");
        return objRequest;
    }
    //打印返回结果
    public void printResponse(ChargeCalculateResponse objResponse){
        if(objResponse != null){
            //服务器响应时间
            System.out.println("Timestamp = "+objResponse.getTimeStamp());
            //操作 成功: Success 失败: Failure
            System.out.println("Ack = "+objResponse.getAck());
            //错误信息
            Errors[] aobjError =  objResponse.getErrors();
            if(aobjError !=null && aobjError.length > 0){
                for(int j=0,iErrorLength=aobjError.length;j<iErrorLength;j++){
                    Errors objError = aobjError[j];
                    //错识代码
               //     System.out.println("Code = "+objError.getCode());
                    //错误详细内容中文描述
               //     System.out.println("CnMessage = "+objError.getCnMessage());
                    //错误的处理方法中文描述
              //      System.out.println("CnAction = "+objError.getCnAction());
                    //错误详细内容英文描述
              //      System.out.println("EnMessage = "+objError.getEnMessage());
                    //错误的处理方法英文描述
               //     System.out.println("EnAction = "+objError.getEnAction());
                    //错误信息补充说明
             //       System.out.println("DefineMessage = "+objError.getDefineMessage());
          //          System.out.println("********************华丽的分割线*****************************");
                }
            }
            CalculateFee[] aobjCalculateFee = objResponse.getCalculateFee();
            if(aobjCalculateFee!=null && aobjCalculateFee.length>0){
                for(int i=0;i<aobjCalculateFee.length;i++){
                	/*
                    System.out.println("产品代码:"+aobjCalculateFee[i].getProductCode());
                    System.out.println("产品中文名称:"+aobjCalculateFee[i].getProductCName());
                    System.out.println("产品英文名称:"+aobjCalculateFee[i].getProductEName());
                    System.out.println("运费金额:"+aobjCalculateFee[i].getFreightAmount());
                    System.out.println("燃油附加费金额:"+aobjCalculateFee[i].getFreightOilAmount());
                    System.out.println("杂费金额:"+aobjCalculateFee[i].getIncidentalAmount());
                    System.out.println("总费用金额:"+aobjCalculateFee[i].getTotalAmount());
                    System.out.println("币种:"+aobjCalculateFee[i].getCurrencyCode());
                    System.out.println("递送时间:"+aobjCalculateFee[i].getDeliveryperiod());
                    System.out.println("可跟踪:"+aobjCalculateFee[i].getTracking());
                    System.out.println("按体积重量计费:"+aobjCalculateFee[i].getVolumn());
                    System.out.println("人民币总费用:"+aobjCalculateFee[i].getTotalRmbAmount());
                    System.out.println("人民币运费金额:"+aobjCalculateFee[i].getFreightRmbAmount());
                    System.out.println("人民燃油附加费金额:"+aobjCalculateFee[i].getFreightOilRmbAmount());
                    System.out.println("人民杂费金额:"+aobjCalculateFee[i].getIncidentalRmbAmount());
                    System.out.println("备注:"+aobjCalculateFee[i].getNote());
                    System.out.println("--------------------华丽的分割线----------------------");
                    */
                }
            }
        }
    }

}

