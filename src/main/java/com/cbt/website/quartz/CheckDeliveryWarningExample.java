package com.cbt.website.quartz;

import com.cbt.util.GetConfigureInfo;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CheckDeliveryWarningExample {
	public void run() throws Exception {

		boolean isOpen = GetConfigureInfo.openJob();
		System.err.println("定时任务启动状态：" + isOpen);
		if(isOpen){
			SchedulerFactory schedulerfactory = new StdSchedulerFactory();
			Scheduler scheduler = null;
			try {
				// 通过schedulerFactory获取一个调度器
				scheduler = schedulerfactory.getScheduler();

				// 创建jobDetail实例，绑定Job实现类
				// 指明job的名称，所在组的名称，以及绑定job类
				JobDetail CheckDeliveryWarningJob = JobBuilder.newJob(com.cbt.website.quartz.CheckDeliveryWarningJob.class)
						.withIdentity("job1", "jgroup1").build();

				JobDetail CheckUnpaidOrderJob = JobBuilder.newJob(com.cbt.website.quartz.CheckUnpaidOrderJob.class)
						.withIdentity("job2", "jgroup2").build();

				JobDetail DelPropertiesJob = JobBuilder.newJob(com.cbt.website.quartz.DelPropertiesJob.class)
						.withIdentity("job3", "jgroup3").build();

				JobDetail PreAutoOrderPromentJob = JobBuilder.newJob(com.cbt.website.quartz.PreAutoOrderPromentJob.class)
						.withIdentity("job4", "jgroup4").build();

				JobDetail AutoAddToSale = JobBuilder.newJob(com.cbt.website.quartz.AutoAddToSale.class)
						.withIdentity("job6", "jgroup6").build();

				JobDetail downErrorFile = JobBuilder.newJob(com.cbt.website.quartz.downErrorFile.class)
						.withIdentity("job8", "jgroup8").build();
				JobDetail ReportSyncFreightJob = JobBuilder.newJob(com.cbt.website.quartz.ReportSyncFreightJob.class)
						.withIdentity("job7", "jgroup7").build();
				JobDetail PreAutoSourceAddTime = JobBuilder.newJob(com.cbt.website.quartz.PreAutoSourceAddTime.class)
						.withIdentity("job9", "jgroup9").build();

				JobDetail orderSnapshotJob = JobBuilder.newJob(com.cbt.website.quartz.OrderSnapshotJob.class)
						.withIdentity("job10", "jgroup10").build();

				JobDetail sameTypeGoodsJob = JobBuilder.newJob(com.cbt.website.quartz.SameTypeGoodsJob.class)
						.withIdentity("job11", "jgroup11").build();

				JobDetail goodsOffShelfJob = JobBuilder.newJob(com.cbt.website.quartz.GoodsOffShelfJob.class)
						.withIdentity("job11", "jgroup11").build();

				JobDetail latestReturnDateRefresh = JobBuilder.newJob(com.cbt.website.quartz.LatestReturnDateRefreshJob.class)
						.withIdentity("job13", "jgroup13").build();

				JobDetail goodsCanceToInventory = JobBuilder.newJob(com.cbt.website.quartz.LoodsCanceToInventory.class)
						.withIdentity("job14", "jgroup14").build();

				JobDetail pic_upload_reload = JobBuilder.newJob(com.cbt.website.quartz.PicUploadReloadjob.class)
						.withIdentity("job15", "jgroup15").build();

				JobDetail goodsSoldUnsellableReasonJob = JobBuilder.newJob(com.cbt.website.quartz.GoodsSoldUnsellableReasonJob.class)
						.withIdentity("job16", "jgroup16").build();

				JobDetail orderProductSourceLogJob = JobBuilder.newJob(com.cbt.website.quartz.OrderProductSourceLogJob.class)
						.withIdentity("job17", "jgroup17").build();

				JobDetail groupBuyJob = JobBuilder.newJob(com.cbt.website.quartz.GroupBuyJob.class)
						.withIdentity("job18", "jgroup18").build();

				JobDetail syncInfringingGoodsJob = JobBuilder.newJob(com.cbt.website.quartz.SyncInfringingGoodsJob.class)
						.withIdentity("job19", "jgroup19").build();
				JobDetail flushboughtAndBoughtJob = JobBuilder.newJob(com.cbt.website.quartz.FlushboughtAndBoughtJob.class)
						.withIdentity("job20", "jgroup20").build();

				Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger_1", "group_1")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(12).repeatForever())
						.startNow().build();

				Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger_2", "group_2")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(2).repeatForever())
						.startNow().build();

				Trigger trigger3 = TriggerBuilder.newTrigger().withIdentity("trigger_3", "group_3")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).repeatForever())
						.startNow().build();// 一天执行一次

				Trigger trigger4 = TriggerBuilder.newTrigger().withIdentity("trigger_4", "group_4")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(2).repeatForever())
//							.withIntervalInMinutes(5).repeatForever())
						.startNow().build();

				Trigger trigger5 = TriggerBuilder.newTrigger().withIdentity("trigger_5", "group_5")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(40).repeatForever())
						.startNow().build();

				Trigger trigger6 = TriggerBuilder.newTrigger().withIdentity("trigger_6", "group_6")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(2).repeatForever())
						.startNow().build();

				Trigger trigger8 = TriggerBuilder.newTrigger().withIdentity("trigger_8", "group_8")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever())
						.startNow().build();
				Trigger trigger9 = TriggerBuilder.newTrigger().withIdentity("trigger_9", "group_9")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(30).repeatForever())
						.startNow().build();

				// Trigger trigger8
				// =TriggerBuilder.newTrigger().withIdentity("trigger_8",
				// "group_8").withSchedule(cronSchedule("0 35 10 ? *
				// *")).startNow().build();

				Trigger trigger7 = TriggerBuilder.newTrigger().withIdentity("trigger_7", "group_7")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(12).repeatForever())
						.startNow().build();

				Trigger trigger10 = TriggerBuilder.newTrigger().withIdentity("trigger_10", "group_10")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(10).repeatForever())
						.startNow().build();

				Trigger trigger11 = TriggerBuilder.newTrigger().withIdentity("trigger_11", "group_11")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(5).repeatForever())
						.startNow().build();

				Trigger trigger12 = TriggerBuilder.newTrigger().withIdentity("trigger_12", "group_12")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(60).repeatForever())
						.startNow().build();

				Trigger trigger13 = TriggerBuilder.newTrigger().withIdentity("trigger_13", "group_13")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(5).repeatForever())
						.startNow().build();

				Trigger trigger14 = TriggerBuilder.newTrigger().withIdentity("trigger_14", "group_14")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(10).repeatForever())
						.startNow().build();

				Trigger trigger15 = TriggerBuilder.newTrigger().withIdentity("trigger_15", "group_15")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(10).repeatForever())
						.startNow().build();

				Trigger trigger16 = TriggerBuilder.newTrigger().withIdentity("trigger_16", "group_16")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(3).repeatForever())
						.startNow().build();

				Trigger trigger17 = TriggerBuilder.newTrigger().withIdentity("trigger_17", "group_17")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(3).repeatForever())
						.startNow().build();

				Trigger trigger18 = TriggerBuilder.newTrigger().withIdentity("trigger_18", "group_18")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(6).repeatForever())
						.startNow().build();

				Trigger trigger19 = TriggerBuilder.newTrigger().withIdentity("trigger_19", "group_19")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(1).repeatForever())
						.startNow().build();
				Trigger trigger20 = TriggerBuilder.newTrigger().withIdentity("trigger_20", "group_20")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(24).repeatForever())
						.startNow().build();
				// 把作业和触发器注册到任务调度中
//			scheduler.scheduleJob(CheckDeliveryWarningJob, trigger1);
//			scheduler.scheduleJob(CheckUnpaidOrderJob, trigger2);//暂时屏蔽掉给未付款订单用户发邮件
//				scheduler.scheduleJob(DelPropertiesJob, trigger3);
				scheduler.scheduleJob(PreAutoOrderPromentJob, trigger4);
//				scheduler.scheduleJob(PreAutoSourceAddTime, trigger9);//定时查询货源链接没有录入时间
//			 scheduler.scheduleJob(downErrorFile, trigger8);//远程下载错误日志
//			scheduler.scheduleJob(ReportSyncFreightJob, trigger7);
//			 scheduler.scheduleJob(AutoAddTo, trigger5);

//				scheduler.scheduleJob(orderSnapshotJob, trigger10);//订单快照数据同步和图片下载处理

				//scheduler.scheduleJob(sameTypeGoodsJob, trigger11);//1688同款数据同步和图片下载处理
//				scheduler.scheduleJob(goodsOffShelfJob, trigger12);//商品下架定时任务
//				scheduler.scheduleJob(latestReturnDateRefresh, trigger13);
//				scheduler.scheduleJob(goodsCanceToInventory, trigger14);//拆单后如果已采购商品则退回到库存中
//				scheduler.scheduleJob(pic_upload_reload, trigger15);//验货上传图片服务器后失败重试
//				scheduler.scheduleJob(goodsSoldUnsellableReasonJob, trigger16);//低库存标志定时更新任务
//				scheduler.scheduleJob(orderProductSourceLogJob, trigger17);//tracking页面status状态信息同步到线上

//			scheduler.scheduleJob(groupBuyJob, trigger18);//团购活动结束定时任务执行客户的优惠金额计算和增加客户优惠金额
//				scheduler.scheduleJob(syncInfringingGoodsJob, trigger19);//侵权商品定时任务
//				scheduler.scheduleJob(flushboughtAndBoughtJob,trigger20);


				// 启动调度
				scheduler.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
