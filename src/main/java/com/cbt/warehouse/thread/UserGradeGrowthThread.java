package com.cbt.warehouse.thread;

import com.cbt.util.SpringContextUtil;
import com.cbt.warehouse.pojo.GradeDiscountBean;
import com.cbt.warehouse.pojo.OrderBean;
import com.cbt.warehouse.pojo.UserGradeGrowthBean;
import com.cbt.warehouse.pojo.UserGradeGrowthLogBean;
import com.cbt.warehouse.service.GradeDiscountService;

import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 用户等级积分
 * @author Administrator
 *
 */
public class UserGradeGrowthThread implements Runnable {
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(UserGradeGrowthThread.class);
	
	private GradeDiscountService gradeDiscountService = (GradeDiscountService) SpringContextUtil.getBean("gradeDiscountServiceImpl");
	private int userId;
	private String orderNo;
	private int status;
	
	private double payPrice;
	private double deletePrice;
	
	private static final int backValue = 100;
	
	public UserGradeGrowthThread(){
		
	}
	
	public UserGradeGrowthThread(int userId,String orderNo,int status){
		this.userId = userId;
		this.orderNo = orderNo;
		this.status = status;
	}
	
	/**
	 * 退单后减少积分所用的参数
	 * @param userId
	 * @param orderNo
	 * @param payPrice 删除后剩余的金额
	 * @param deletePrice 删除的金额
	 * @param status
	 */
	public UserGradeGrowthThread(int userId,String orderNo,double payPrice,double deletePrice,int status){
		this.userId = userId;
		this.orderNo = orderNo;
		this.payPrice = payPrice;
		this.deletePrice = deletePrice;
		this.status = status;
	}
	
	@Override
	public void run() {
		if(status == 1){
			this.addUserGradeGrowth();
		}else if(status == 2){
			//判断订单是否是拆单后的订单
			if(!gradeDiscountService.isSpitOrder(userId, orderNo.split("_")[0])) {
				this.deleteUserGradeGrowth();
			}else{
				this.deleteUserGradeGrowthBySplitOrder();
			}
		}
	}
	
	/**
	 * 结算后增加用户等级成长积分
	 */
	public void addUserGradeGrowth(){
		UserGradeGrowthBean bean = gradeDiscountService.getUserGradeGrowth(userId);
		int comOrderCount =  gradeDiscountService.getCompleteOrderCount(userId);
		OrderBean orderBean = gradeDiscountService.getCompleteOrder(orderNo);
		/**
		 * 判断是否是dropship订单，是的话不能进行vip操作
		 */
		if(orderBean!=null && orderBean.getIsDropshipOrder()!=1){
			/**
			 * 支付金额
			 */
			double pay_price = orderBean.getPay_price();
			UserGradeGrowthLogBean logBean = new UserGradeGrowthLogBean();
			//判断支付金额大于0，由金额计算积分
			if(pay_price>0){
				int growthValue = (int)pay_price;
				int orderGrowthValue = (int)pay_price;
				int backGrowthValue = 0;
				logBean.setUserId(userId);
				logBean.setOrderNo(orderNo);
				logBean.setOrderPrice(pay_price);
				//判断是否是返单，是的话获得返单积分100
				if(comOrderCount>0){
					growthValue += backValue;
					backGrowthValue = backValue;
				}
				logBean.setBackGrowthValue(backGrowthValue);
				
				if(bean == null) {
					/**
					 * 没有积分记录，插入一条新的积分记录
					 */
					GradeDiscountBean gradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(growthValue);
					bean = new UserGradeGrowthBean();
					bean.setUserId(userId);
					bean.setGradeDisId(gradeDis.getId());
					bean.setGrade(gradeDis.getGid());
					bean.setDiscount(gradeDis.getDiscount());
					bean.setGrowthValue(growthValue);
					bean.setTimes(gradeDis.getTimes());
					bean.setValid(1);
					gradeDiscountService.insertUserGradeGrowth(bean);
					gradeDiscountService.updateUserGrade(gradeDis.getGid(), userId);
					
					/**
					 * 添加新的积分操作日志
					 */
					logBean.setGrade(gradeDis.getGid());
					logBean.setBeforeGrowthValue(0);
					logBean.setAfterGrowthValue(growthValue);
					logBean.setGapGrowthValue(growthValue);
					logBean.setBackGrowthValue(backGrowthValue);
					logBean.setOrderGrowthValue(orderGrowthValue);
					logBean.setTimes(0);
					LOG.info("userId为"+userId+"的用户增加一条积分记录");
				}else{
					
					logBean.setGrade(bean.getGrade());
					if(bean.getTimes()>0){
						logBean.setTimes(bean.getTimes()-1);
					}else{
						logBean.setTimes(0);
					}
					/**
					 * 有积分记录，积分增加修改
					 */
					logBean.setBackGrowthValue(backGrowthValue);
					logBean.setOrderGrowthValue(orderGrowthValue);
					logBean.setGapGrowthValue(growthValue);
					if(bean.getGrowthValue()>0){
						growthValue += bean.getGrowthValue();
						logBean.setBeforeGrowthValue(bean.getGrowthValue());
					}else{
						logBean.setBeforeGrowthValue(0);
					}
					bean.setGrowthValue(growthValue);
					logBean.setAfterGrowthValue(growthValue);
					
					GradeDiscountBean gradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(growthValue);
					/**
					 * 最新总积分的等级和原等级是否相同，相同并且优惠次数不为0，优惠次数减少
					 * 不相同，更新等级id、优惠率、次数，升级
					 */
					if(bean.getGrade() == gradeDis.getGid()){
						int times = bean.getTimes();
						if(times>0){
							bean.setTimes(times-1);
						}
					}else{
						bean.setGradeDisId(gradeDis.getId());
						bean.setGrade(gradeDis.getGid());
						bean.setDiscount(gradeDis.getDiscount());
						bean.setTimes(gradeDis.getTimes());
						
					}
					if(bean.getTimes() == 0) {
						/**
						 * 当优惠次数为0时，其他未支付订单不再有vip折扣，将其他的未支付订单vip折扣去掉
						 */
						List<OrderBean> otherOlist = gradeDiscountService.getOtherGradeDiscount(userId);
						updateOtherOrderDiscount(userId, otherOlist);
					}
					gradeDiscountService.updateUserGradeGrowth(bean);
					gradeDiscountService.updateUserGrade(gradeDis.getGid(), userId);
					LOG.info("userId为"+userId+"的用户修改积分记录");
				}
			}
//			logBean.setComment("订单完成后，用户积分增加");
			logBean.setComment("After the order finished,the growth value of user increases.");
			//等级积分日志
			gradeDiscountService.insertUserGradeGrowthLog(logBean);
		}
	}
	/**
	 * 退单后减少积分
	 */
	public void deleteUserGradeGrowth(){
		int comOrderCount =  gradeDiscountService.getCompleteOrderCount(userId);
		int nowOrderGrowthValue = (int)payPrice;
		int backGrowthValue = 0;
		int deleteGrowthValue = 0;
		if(comOrderCount>0){
			backGrowthValue = backValue;
		}
		//用户等级信息
		UserGradeGrowthBean bean = gradeDiscountService.getUserGradeGrowth(userId);
		//订单信息
		OrderBean orderBean = gradeDiscountService.getCompleteOrder(orderNo);
		//本次订单最新操作日志
		UserGradeGrowthLogBean orderLogBean = gradeDiscountService.getLogByUserIdAndOrderNo(userId, orderNo);
		//用户最新操作日志
		UserGradeGrowthLogBean userLogBean = gradeDiscountService.getLogByUserId(userId);
		//创建新日志
		UserGradeGrowthLogBean newLogBean = new UserGradeGrowthLogBean();
		
		newLogBean.setUserId(userId);
		newLogBean.setOrderNo(orderNo);
		/**
		 * vip等级信息为null或订单信息为null或订单为drop shopping订单时，系统不会操作减分功能
		 */
		if(bean!=null && orderBean!=null && orderBean.getIsDropshipOrder()!=1){
			if(userLogBean != null){
				/**
				 * 判断是否订单日志，如果有，则是新订单
				 */
				int nowTotalValue = 0;
				GradeDiscountBean oGradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(bean.getGrowthValue());
				if(orderLogBean != null && orderLogBean.getIsOldOrder() == 0) {
					//订单取消，6为客户订单取消，整个订单取消
					if(orderBean.getState() == 6) {
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						deleteGrowthValue = orderLogBean.getOrderGrowthValue()+backGrowthValue;
						newLogBean.setGapGrowthValue(-deleteGrowthValue);
						if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
							nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
							newLogBean.setAfterGrowthValue(nowTotalValue);
						}else{
							nowTotalValue = 0;
							newLogBean.setAfterGrowthValue(0);
						}
						newLogBean.setBackGrowthValue(0);
						newLogBean.setOrderGrowthValue(0);
						newLogBean.setOrderPrice(0);
						
						if(bean.getTimes()<oGradeDis.getTimes()) {
							newLogBean.setTimes(bean.getTimes()+1);
						}else{
							newLogBean.setTimes(oGradeDis.getTimes());
						}
					}else{
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						newLogBean.setBackGrowthValue(backGrowthValue);
						//剩余本订单分数<上次本订单分数
						if(nowOrderGrowthValue<orderLogBean.getOrderGrowthValue()){
							deleteGrowthValue = orderLogBean.getOrderGrowthValue() - nowOrderGrowthValue;
							newLogBean.setGapGrowthValue(-deleteGrowthValue);
							
							if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
								nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
								newLogBean.setAfterGrowthValue(nowTotalValue);
							}else{
								nowTotalValue = 0;
								newLogBean.setAfterGrowthValue(0);
							}
							newLogBean.setOrderGrowthValue(nowOrderGrowthValue);
							newLogBean.setOrderPrice(payPrice);
						}else{
							deleteGrowthValue = 0;
							newLogBean.setGapGrowthValue(0);
							
							nowTotalValue = userLogBean.getAfterGrowthValue();
							newLogBean.setAfterGrowthValue(nowTotalValue);
							newLogBean.setOrderGrowthValue(orderLogBean.getOrderGrowthValue());
							newLogBean.setOrderPrice(orderLogBean.getOrderPrice());
						}
						newLogBean.setTimes(bean.getTimes());
					}
					
					bean.setGrowthValue(nowTotalValue);
					
					newLogBean.setGrade(bean.getGrade());
//					newLogBean.setComment("退单后，用户积分减少");
					newLogBean.setComment("After the order unsubscribe,the growth value of user decreases.");
					GradeDiscountBean gradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(nowTotalValue);
					if(nowTotalValue >= 0) {
						/**
						 * 判断等级是否更新，如果等级不变，只更新优惠使用次数就行
						 */
						if(bean.getGrade() == gradeDis.getGid()){
							//等级不变，
							int times = bean.getTimes();
							/**
							 * 整个订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
							 */
							if(orderBean.getState() == 6 && times<gradeDis.getTimes()){
								bean.setTimes(times+1);
							}
						}else{
							/**
							 * 降低后的等级与上次用户操作日志的等级相等，优惠使用次数就是上次操作日志的剩余次数
							 */
							if(userLogBean.getGrade() == gradeDis.getGid()){
								int times = userLogBean.getTimes();
								/**
								 * 整个订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
								 */
								if(orderBean.getState() == 6 && times<gradeDis.getTimes()) {
									bean.setTimes(times+1);
								}else{
									bean.setTimes(times);
								}
							}else{
								//查询该等级在日志中使用记录
								Integer times = gradeDiscountService.getTimesByUserIdAndGrade(userId, gradeDis.getGid());
								/**
								 * 查询降低后的等级是否在日志中有使用过的记录，如果没有则设置等级最高使用次数，如果有则设置日志记录的剩余次数
								 */
								if(times != null) {
									/**
									 * 整个订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
									 */
									if(orderBean.getState() == 6 && times<gradeDis.getTimes()) {
										bean.setTimes(times+1);
									}else{
										bean.setTimes(times);
									}
									
								} else {
									bean.setTimes(gradeDis.getTimes());
								}
									
							}
						}
						bean.setGradeDisId(gradeDis.getId());
						bean.setGrade(gradeDis.getGid());
						bean.setDiscount(gradeDis.getDiscount());
					}
				
					/**
					 * 修改vip等级信息
					 */
					gradeDiscountService.updateUserGradeGrowth(bean);
					/**
					 * 修改用户表中等级
					 */
					gradeDiscountService.updateUserGrade(gradeDis.getGid(), userId);
					/**
					 * 增加一条用户等级优惠操作日志
					 */
					gradeDiscountService.insertUserGradeGrowthLog(newLogBean);
				}else{
					deleteGrowthValue = (int)deletePrice;
					/**
					 * 原来订单删除后，减分操作
					 */
					if(orderBean.getState() == 6) {
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						newLogBean.setGapGrowthValue(-deleteGrowthValue);
						if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
							nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
							newLogBean.setAfterGrowthValue(nowTotalValue);
						}else{
							nowTotalValue = 0;
							newLogBean.setAfterGrowthValue(0);
						}
						newLogBean.setBackGrowthValue(0);
						newLogBean.setOrderGrowthValue(0);
						newLogBean.setOrderPrice(0);
						
						if(bean.getTimes()<oGradeDis.getTimes()) {
							newLogBean.setTimes(bean.getTimes()+1);
						}else{
							newLogBean.setTimes(oGradeDis.getTimes());
						}
					}else{
					
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						newLogBean.setBackGrowthValue(0);
						//剩余本订单分数<上次本订单分数
						if(deleteGrowthValue>0){
							newLogBean.setGapGrowthValue(-deleteGrowthValue);
							
							if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
								nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
								newLogBean.setAfterGrowthValue(nowTotalValue);
							}else{
								nowTotalValue = 0;
								newLogBean.setAfterGrowthValue(0);
							}
							newLogBean.setOrderGrowthValue(nowOrderGrowthValue);
							newLogBean.setOrderPrice(payPrice);
						}else{
							deleteGrowthValue = 0;
							newLogBean.setGapGrowthValue(0);
							
							nowTotalValue = userLogBean.getAfterGrowthValue();
							newLogBean.setAfterGrowthValue(nowTotalValue);
							newLogBean.setOrderGrowthValue(nowOrderGrowthValue);
							newLogBean.setOrderPrice(payPrice);
						}
						newLogBean.setTimes(bean.getTimes());
					}
					bean.setGrowthValue(nowTotalValue);
					
					newLogBean.setGrade(bean.getGrade());
//					newLogBean.setComment("退单后，用户积分减少");
					newLogBean.setComment("After the order unsubscribe,the growth value of user decreases.");
					newLogBean.setIsOldOrder(1);
					
					GradeDiscountBean gradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(nowTotalValue);
					if(nowTotalValue>=0){
						/**
						 * 判断等级是否更新，如果等级不变，只更新优惠使用次数就行
						 */
						if(bean.getGrade() == gradeDis.getGid()){
							//等级不变，
							int times = bean.getTimes();
							/**
							 * 整个订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
							 */
							if(orderBean.getState() == 6 && times<gradeDis.getTimes()){
								bean.setTimes(times+1);
							}
						}else{
							/**
							 * 降低后的等级与上次用户操作日志的等级相等，优惠使用次数就是上次操作日志的剩余次数
							 */
							if(userLogBean.getGrade() == gradeDis.getGid()){
								int times = userLogBean.getTimes();
								/**
								 * 整个订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
								 */
								if(orderBean.getState() == 6 && times<gradeDis.getTimes()) {
									bean.setTimes(times+1);
								}else{
									bean.setTimes(times);
								}
							}else{
								//查询该等级在日志中使用记录
								Integer times = gradeDiscountService.getTimesByUserIdAndGrade(userId, gradeDis.getGid());
								/**
								 * 查询降低后的等级是否在日志中有使用过的记录，如果没有则设置等级最高使用次数，如果有则设置日志记录的剩余次数
								 */
								if(times != null) {
									/**
									 * 整个订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
									 */
									if(orderBean.getState() == 6 && times<gradeDis.getTimes()) {
										bean.setTimes(times+1);
									}else{
										bean.setTimes(times);
									}
									
								} else {
									bean.setTimes(gradeDis.getTimes());
								}
							}
						}
						bean.setGradeDisId(gradeDis.getId());
						bean.setGrade(gradeDis.getGid());
						bean.setDiscount(gradeDis.getDiscount());
					}
					/**
					 * 修改vip等级信息
					 */
					gradeDiscountService.updateUserGradeGrowth(bean);
					/**
					 * 修改用户表中等级
					 */
					gradeDiscountService.updateUserGrade(gradeDis.getGid(), userId);
					/**
					 * 增加一条用户等级优惠操作日志
					 */
					gradeDiscountService.insertUserGradeGrowthLog(newLogBean);
				}
			}
		}
	}
	
	/**
	 * 拆单后订单删除后积分计算
	 */
	public void deleteUserGradeGrowthBySplitOrder() {
		int comOrderCount =  gradeDiscountService.getCompleteOrderCountExcludeTOrder(userId, orderNo.split("_")[0]);
//		int nowOrderGrowthValue = (int)payPrice;
		int backGrowthValue = 0;
		int deleteGrowthValue = 0;
		if(comOrderCount>0){
			backGrowthValue = backValue;
		}
		//用户等级信息
		UserGradeGrowthBean bean = gradeDiscountService.getUserGradeGrowth(userId);
		//订单信息
		OrderBean orderBean = gradeDiscountService.getCompleteOrder(orderNo);
		//本次订单最新操作日志
		UserGradeGrowthLogBean orderLogBean = gradeDiscountService.getLogByUserIdAndOrderNo(userId, orderNo.split("_")[0]);
		//用户最新操作日志
		UserGradeGrowthLogBean userLogBean = gradeDiscountService.getLogByUserId(userId);
		//创建新日志
		UserGradeGrowthLogBean newLogBean = new UserGradeGrowthLogBean();
		//本次订单对应同源拆单的订单的数量
		int TSplitCount = gradeDiscountService.getCountLikeOrderNo(userId, orderNo.split("_")[0]);
		//本次订单对应同源拆单的订单退单的数量
		int deleteTSpiltCount = gradeDiscountService.getDeleteCountLikeOrderNo(userId, orderNo.split("_")[0]); 
		
		newLogBean.setUserId(userId);
		newLogBean.setOrderNo(orderNo.split("_")[0]);
		/**
		 * vip等级信息为null或订单信息为null或订单为drop shopping订单时，系统不会操作减分功能
		 */
		if(bean!=null && orderBean!=null && orderBean.getIsDropshipOrder()!=1){
			if(userLogBean != null){
				/**
				 * 判断是否订单日志，如果有，则是新订单
				 */
				int nowTotalValue = 0;
				GradeDiscountBean oGradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(bean.getGrowthValue());
				if(orderLogBean != null && orderLogBean.getIsOldOrder() == 0) {
					//订单取消，6为客户订单取消，整个订单取消
					if(TSplitCount == deleteTSpiltCount) {
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						deleteGrowthValue = orderLogBean.getOrderGrowthValue()+backGrowthValue;
						newLogBean.setGapGrowthValue(-deleteGrowthValue);
						if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
							nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
							newLogBean.setAfterGrowthValue(nowTotalValue);
						}else{
							nowTotalValue = 0;
							newLogBean.setAfterGrowthValue(0);
						}
						newLogBean.setBackGrowthValue(0);
						newLogBean.setOrderGrowthValue(0);
						newLogBean.setOrderPrice(0);
						
						if(bean.getTimes()<oGradeDis.getTimes()) {
							newLogBean.setTimes(bean.getTimes()+1);
						}else{
							newLogBean.setTimes(oGradeDis.getTimes());
						}
					}else{
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						newLogBean.setBackGrowthValue(backGrowthValue);
						deleteGrowthValue = (int)deletePrice;
						//剩余本订单分数<上次本订单分数
						if(deleteGrowthValue>0){
							newLogBean.setGapGrowthValue(-deleteGrowthValue);
							
							if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
								nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
								newLogBean.setAfterGrowthValue(nowTotalValue);
							}else{
								nowTotalValue = 0;
								newLogBean.setAfterGrowthValue(0);
							}
							if(orderLogBean.getOrderGrowthValue()>deleteGrowthValue){
								int nowOrderGrowthValue = orderLogBean.getOrderGrowthValue() - deleteGrowthValue;
								newLogBean.setOrderGrowthValue(nowOrderGrowthValue);
							}else{
								int nowOrderGrowthValue = 0;
								newLogBean.setOrderGrowthValue(0);
							}
							if(deletePrice<orderLogBean.getOrderPrice()) {
								payPrice = orderLogBean.getOrderPrice() - deletePrice;
								newLogBean.setOrderPrice(payPrice);
							}else{
								newLogBean.setOrderPrice(0);
							}
						}else{
							deleteGrowthValue = 0;
							newLogBean.setGapGrowthValue(0);
							
							nowTotalValue = userLogBean.getAfterGrowthValue();
							newLogBean.setAfterGrowthValue(nowTotalValue);
							newLogBean.setOrderGrowthValue(orderLogBean.getOrderGrowthValue());
							newLogBean.setOrderPrice(orderLogBean.getOrderPrice());
						}
						newLogBean.setTimes(bean.getTimes());
					}
					
					bean.setGrowthValue(nowTotalValue);
					
					newLogBean.setGrade(bean.getGrade());
//					newLogBean.setComment("退单后，用户积分减少");
					newLogBean.setComment("After the order unsubscribe,the growth value of user decreases.");
					GradeDiscountBean gradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(nowTotalValue);
					if(nowTotalValue >= 0) {
						/**
						 * 判断等级是否更新，如果等级不变，只更新优惠使用次数就行
						 */
						if(bean.getGrade() == gradeDis.getGid()){
							//等级不变，
							int times = bean.getTimes();
							/**
							 * 所有同源拆单后的订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
							 */
							if(TSplitCount == deleteTSpiltCount && times<gradeDis.getTimes()){
								bean.setTimes(times+1);
							}
						}else{
							/**
							 * 降低后的等级与上次用户操作日志的等级相等，优惠使用次数就是上次操作日志的剩余次数
							 */
							if(userLogBean.getGrade() == gradeDis.getGid()){
								int times = userLogBean.getTimes();
								/**
								 * 所有同源拆单后的订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
								 */
								if(TSplitCount == deleteTSpiltCount && times<gradeDis.getTimes()) {
									bean.setTimes(times+1);
								}else{
									bean.setTimes(times);
								}
							}else{
								//查询该等级在日志中使用记录
								Integer times = gradeDiscountService.getTimesByUserIdAndGrade(userId, gradeDis.getGid());
								/**
								 * 查询降低后的等级是否在日志中有使用过的记录，如果没有则设置等级最高使用次数，如果有则设置日志记录的剩余次数
								 */
								if(times != null) {
									/**
									 * 所有同源拆单后的订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
									 */
									if(TSplitCount == deleteTSpiltCount && times<gradeDis.getTimes()) {
										bean.setTimes(times+1);
									}else{
										bean.setTimes(times);
									}
									
								} else {
									bean.setTimes(gradeDis.getTimes());
								}
									
							}
						}
						bean.setGradeDisId(gradeDis.getId());
						bean.setGrade(gradeDis.getGid());
						bean.setDiscount(gradeDis.getDiscount());
					}
				
					/**
					 * 修改vip等级信息
					 */
					gradeDiscountService.updateUserGradeGrowth(bean);
					/**
					 * 修改用户表中等级
					 */
					gradeDiscountService.updateUserGrade(gradeDis.getGid(), userId);
					/**
					 * 增加一条用户等级优惠操作日志
					 */
					gradeDiscountService.insertUserGradeGrowthLog(newLogBean);
				}else{
					deleteGrowthValue = (int)deletePrice;
					Double payPrice_1 = gradeDiscountService.getTOrderNowTotalPayPrice(userId, orderNo.split("_")[0]);
					if(payPrice_1 != null) {
						payPrice = payPrice_1;
					}else{
						payPrice = 0;
					}
					int nowOrderGrowthValue = (int)payPrice;
					/**
					 * 原来订单删除后，减分操作
					 */
					if(TSplitCount == deleteTSpiltCount) {
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						newLogBean.setGapGrowthValue(-deleteGrowthValue);
						if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
							nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
							newLogBean.setAfterGrowthValue(nowTotalValue);
						}else{
							nowTotalValue = 0;
							newLogBean.setAfterGrowthValue(0);
						}
						newLogBean.setBackGrowthValue(0);
						newLogBean.setOrderGrowthValue(0);
						newLogBean.setOrderPrice(0);
						
						if(bean.getTimes()<oGradeDis.getTimes()) {
							newLogBean.setTimes(bean.getTimes()+1);
						}else{
							newLogBean.setTimes(oGradeDis.getTimes());
						}
					}else{
					
						newLogBean.setBeforeGrowthValue(userLogBean.getAfterGrowthValue());
						newLogBean.setBackGrowthValue(0);
						//剩余本订单分数<上次本订单分数
						if(deleteGrowthValue>0){
							newLogBean.setGapGrowthValue(-deleteGrowthValue);
							
							if(userLogBean.getAfterGrowthValue()>deleteGrowthValue){
								nowTotalValue = userLogBean.getAfterGrowthValue()-deleteGrowthValue;
								newLogBean.setAfterGrowthValue(nowTotalValue);
							}else{
								nowTotalValue = 0;
								newLogBean.setAfterGrowthValue(0);
							}
							if(nowOrderGrowthValue>0){
								newLogBean.setOrderGrowthValue(nowOrderGrowthValue);
							}else{
								nowOrderGrowthValue = 0;
								newLogBean.setOrderGrowthValue(0);
							}
							
							newLogBean.setOrderPrice(payPrice);
						}else{
							deleteGrowthValue = 0;
							newLogBean.setGapGrowthValue(0);
							
							nowTotalValue = userLogBean.getAfterGrowthValue();
							newLogBean.setAfterGrowthValue(nowTotalValue);
							newLogBean.setOrderGrowthValue(nowOrderGrowthValue);
							newLogBean.setOrderPrice(payPrice);
						}
						newLogBean.setTimes(bean.getTimes());
					}
					bean.setGrowthValue(nowTotalValue);
					
					newLogBean.setGrade(bean.getGrade());
//					newLogBean.setComment("退单后，用户积分减少");
					newLogBean.setComment("After the order unsubscribe,the growth value of user decreases.");
					newLogBean.setIsOldOrder(1);
					
					GradeDiscountBean gradeDis = gradeDiscountService.getGradeDiscountByGrowthValue(nowTotalValue);
					if(nowTotalValue>=0){
						/**
						 * 判断等级是否更新，如果等级不变，只更新优惠使用次数就行
						 */
						if(bean.getGrade() == gradeDis.getGid()){
							//等级不变，
							int times = bean.getTimes();
							/**
							 * 所有同源拆单后的订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
							 */
							if(TSplitCount == deleteTSpiltCount && times<gradeDis.getTimes()){
								bean.setTimes(times+1);
							}
						}else{
							/**
							 * 降低后的等级与上次用户操作日志的等级相等，优惠使用次数就是上次操作日志的剩余次数
							 */
							if(userLogBean.getGrade() == gradeDis.getGid()){
								int times = userLogBean.getTimes();
								/**
								 * 所有同源拆单后的订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
								 */
								if(TSplitCount == deleteTSpiltCount && times<gradeDis.getTimes()) {
									bean.setTimes(times+1);
								}else{
									bean.setTimes(times);
								}
							}else{
								//查询该等级在日志中使用记录
								Integer times = gradeDiscountService.getTimesByUserIdAndGrade(userId, gradeDis.getGid());
								/**
								 * 查询降低后的等级是否在日志中有使用过的记录，如果没有则设置等级最高使用次数，如果有则设置日志记录的剩余次数
								 */
								if(times != null) {
									/**
									 * 所有同源拆单后的订单取消且使用次数小于当前等级最高使用次数，优惠次数增加1
									 */
									if(TSplitCount == deleteTSpiltCount && times<gradeDis.getTimes()) {
										bean.setTimes(times+1);
									}else{
										bean.setTimes(times);
									}
									
								} else {
									bean.setTimes(gradeDis.getTimes());
								}
							}
						}
						bean.setGradeDisId(gradeDis.getId());
						bean.setGrade(gradeDis.getGid());
						bean.setDiscount(gradeDis.getDiscount());
					}
					/**
					 * 修改vip等级信息
					 */
					gradeDiscountService.updateUserGradeGrowth(bean);
					/**
					 * 修改用户表中等级
					 */
					gradeDiscountService.updateUserGrade(gradeDis.getGid(), userId);
					/**
					 * 增加一条用户等级优惠操作日志
					 */
					gradeDiscountService.insertUserGradeGrowthLog(newLogBean);
				}
			}
		}
	}
	
	
	public void updateOtherOrderDiscount(int userId,List<OrderBean> list) {
		if(list != null && list.size()>0) {
			for(OrderBean bean : list) {
				double grade_discount = bean.getGrade_discount();
				String order_no = bean.getOrderNo();
				if(grade_discount>0) {
					grade_discount = 0;
					gradeDiscountService.updateOtherOrderGradeDiscout(grade_discount, order_no, userId);
				}
			}
		}
	}

}
