package com.cbt.warehouse.ctrl;

import com.alibaba.fastjson.JSON;
import com.cbt.warehouse.service.IWarehouseService1;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequestMapping("WarehouseCtrl1")
@Controller
public class WarehouseCtrl1 {

	@Autowired
	private IWarehouseService1 iWarehouseService;
	
	
	// 库存 新添 一条记录
	@RequestMapping(value="/insertSP",method = RequestMethod.POST)
	@ResponseBody
	public  String  insertSP(HttpServletRequest request, HttpServletResponse response){
		String shipmentno = request.getParameter("shipmentno");
		String result = "false";
		try {
			//本地
			iWarehouseService.insertSp(shipmentno);
			//线上

			SendMQ.sendMsg(new RunSqlModel(" insert  into shipping_package(shipmentno,orderid,remarks,createtime,sweight,svolume,volumeweight,sflag,transportcompany,shippingtype," +
					"transportcountry,expressno,freight,estimatefreight,issendmail,settleWeight,totalPrice) select concat(shipmentno,'_1')," +
					"orderid,remarks,createtime,sweight,svolume,volumeweight,sflag,transportcompany,shippingtype,transportcountry,expressno,freight,estimatefreight,issendmail," +
					"settleWeight,totalPrice from  shipping_package where shipmentno ='"+shipmentno+"'  and sflag = 3"));

			result = "true";
		} catch (Exception e) {
			// TODO: handle exception
		}
		String json = JSON.toJSONString(result);
		return json;
	}
}
