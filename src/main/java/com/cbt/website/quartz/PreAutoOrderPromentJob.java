package com.cbt.website.quartz;

import com.cbt.auto.ctrl.OrderAutoServlet;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;

public class PreAutoOrderPromentJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Calendar c = Calendar.getInstance();
		int hh = c.get(Calendar.HOUR_OF_DAY);
		int mm = c.get(Calendar.MINUTE);
		
		//每天在早上9点和下午1点执行分配采购
//		if((hh==9 && mm>=30) || hh==14){
			OrderAutoServlet o=new OrderAutoServlet();
			o.newPreOrderAutoDistribution();
//			o.PreOrderAutoDistribution();
//			o.importInventory();
//			o.cancalTbOrder1();
//			o.importComments();
//			o.compareSF();
//			o.importType();
//			o.insertCheck();
//		}

		//获取订单出运最优运费的订单
//		o.updateShippingPackCost();
	}
	

}
