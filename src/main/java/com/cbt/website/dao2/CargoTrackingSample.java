package com.cbt.website.dao2;

import com.cbt.website.util.IOrderOnlineSampleBasic;
import fpx.api.client.OrderOnlineToolDelegate;
import fpx.api.orderonlinetool.entity.CargoTrackingResponse;
import fpx.api.orderonlinetool.entity.Errors;
import fpx.api.orderonlinetool.entity.Track;
import fpx.api.orderonlinetool.entity.TrackInfo;

import java.rmi.RemoteException;

/**
 * @author Terence chen 2012-7-3 下午2:38:32
 * @version 1.0
 */
public class CargoTrackingSample {
	/**
	 * 测试入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CargoTrackingSample sample = new CargoTrackingSample();
		sample.cargoTrackingService("MP03150332XSG");
	}

	/**
	 * 查询轨迹API
	 */
	public String cargoTrackingService(String orderno) {
		String[] astrOrderNo = new String[1];
		astrOrderNo[0] = orderno;
		OrderOnlineToolDelegate objDelegate = new OrderOnlineToolDelegate();
		CargoTrackingResponse[] aobjResponse = null;
		try {
			aobjResponse = objDelegate.cargoTrackingService(IOrderOnlineSampleBasic.PRODUCT_AUTHTOKEN, astrOrderNo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//printResponse(aobjResponse);//打印返回结果
		String bs = "";
		StringBuffer sb = new StringBuffer("[");
		String searchState = aobjResponse[0].getAck();
		if(searchState.equals("Success")){
			Track[] dd = aobjResponse[0].getTracks();
			if (dd == null) {
			} else {
				TrackInfo[] cc = dd[0].getTrackInfo();
				for (int t = 0; t < cc.length; t++) {
					//sb.append("{\"TrackCode\" :\""+cc[t].getTrackCode()+"\",");
					sb.append("{\"time\" :\""+cc[t].getOccurDate()+"\",");
					//sb.append("\"location\" :\""+cc[t].getOccurAddress()+"\",");
					sb.append("\"context\" :\"["+cc[t].getOccurAddress()+"]"+cc[t].getTrackContent()+"\"},");
				}
			}
		}
		sb.append("]");
		bs = "[{\"data\":"+sb.toString()+"}]";
//		JSONArray jsonArray = new JSONArray();
//		jsonArray = JSONArray.fromObject(bs);
//		System.out.println(jsonArray);
//		return jsonArray;
		System.out.println(bs);
		return bs;
	}

	public void printResponse(CargoTrackingResponse[] aobjResponse) {
		if (aobjResponse != null && aobjResponse.length > 0) {
			for (int i = 0, iLength = aobjResponse.length; i < iLength; i++) {
				CargoTrackingResponse objResponse = aobjResponse[i];
				// 服务器响应时间
				System.out.println("Timestamp = " + objResponse.getTimestamp());
				// 操作 成功: Success 失败: Failure
				System.out.println("Ack = " + objResponse.getAck());
				// 引用单号，一般为客户单号
				System.out.println("ReferenceNumber = " + objResponse.getReferenceNumber());
				// 错误信息
				Errors[] aobjError = objResponse.getErrors();
				if (aobjError != null && aobjError.length > 0) {
					for (int j = 0, iErrorLength = aobjError.length; j < iErrorLength; j++) {
						Errors objError = aobjError[j];
						// 错识代码
						System.out.println("Code = " + objError.getCode());
						// 错误详细内容中文描述
						System.out.println("CnMessage = " + objError.getCnMessage());
						// 错误的处理方法中文描述
						System.out.println("CnAction = " + objError.getCnAction());
						// 错误详细内容英文描述
						System.out.println("EnMessage = " + objError.getEnMessage());
						// 错误的处理方法英文描述
						System.out.println("EnAction = " + objError.getEnAction());
						// 错误信息补充说明
						System.out.println("DefineMessage = " + objError.getDefineMessage());
						System.out.println("****************************我是华丽的分割线*********************");
					}
				}
				// 轨迹信息
				Track[] aobjTrack = objResponse.getTracks();
				if (aobjTrack != null && aobjTrack.length > 0) {
					for (Track objTrack : aobjTrack) {
						System.out.println("TrackingNumber = " + objTrack.getTrackingNumber());
						System.out.println("ProductCode = " + objTrack.getProductCode());
						System.out.println("DestinationAddrEg = " + objTrack.getDestinationCountryCode());
						System.out.println("SignForPerson = " + objTrack.getSignForPerson());
						System.out.println("SignForDate = " + objTrack.getSignForDate());
						System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&我是华丽的分割线&&&&&&&&&&&&&&&&&&&&&&&&");
						// 轨迹发生信息
						TrackInfo[] aobjTrackInfo = objTrack.getTrackInfo();
						if (aobjTrackInfo != null && aobjTrackInfo.length > 0) {
							//for (int k=aobjTrackInfo.length-1; k >=0 ; k--) {//反序输出
							for (int k = 0; k < aobjTrackInfo.length; k++) {
								TrackInfo objTrackInfo = aobjTrackInfo[k];
								System.out.println("TrackCode = " + objTrackInfo.getTrackCode());
								System.out.println("OccurDate = " + objTrackInfo.getOccurDate());//1
								System.out.println("OccurAddressCn = " + objTrackInfo.getOccurAddress());//2
								System.out.println("TrackContent = " + objTrackInfo.getTrackContent());//3
								System.out.println("~~~~~~~~~~~~~~~~~~~~~~~我是华丽的分割线~~~~~~~~~~~~~~~~~~~~~");
							}
						}
					}
				}
				System.out.println("---------------------------------我是华丽的分割线--------------------------------");
			}
		}
	}
}
