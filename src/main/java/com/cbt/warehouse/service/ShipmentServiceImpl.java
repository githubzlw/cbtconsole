package com.cbt.warehouse.service;

import com.cbt.util.Util;
import com.cbt.warehouse.dao.ShipmentMapper;
import com.cbt.warehouse.pojo.Shipment;
import com.cbt.website.bean.DataGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**   
 * @Title: ShipmentServiceImpl.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年7月5日
 * @version V1.0   
 */

@Service
public class ShipmentServiceImpl implements ShipmentService {
	
	@Autowired
	private ShipmentMapper shipmentMapper;

	@Override
	public int insertShipment(Shipment shipment) {
		return shipmentMapper.insertSelective(shipment);
	}

	@Override
	public List<Shipment> insertShipment(List<Shipment> list) {
		int count = 0;
		List<Shipment>   payedList  = new  ArrayList<Shipment>();
		for (Shipment shipment : list) {
			//在插入之前判断该运单是否已经冻结(即已付过款)||跟我们系统没关联的运单信息
			String  orderno = shipment.getOrderno();
			Shipment sm =  shipmentMapper.selectPayedShipMent(orderno);
			if(sm!=null){ 
				payedList.add(sm);
			}else{
				count += shipmentMapper.insertSelective(shipment);
			}
		}
		if(payedList.size()==0){
			Shipment shipment = new Shipment();
			shipment.setCount(count);
			payedList.add(shipment);
		}else{
			//将不符合条件的运单信息插入UnExistOrPayedShipment 表
//			for (Shipment shipment : payedList) {
//				 int ret = shipmentMapper.insertUnExistOrPayedSelective(shipment);
//			}
			payedList.get(0).setCount(count);
		}
		return payedList;
	}

	@Override
	public List<Shipment> validateShipmentList(List<Shipment> list, String uuid) {
		StringBuffer idStr = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Shipment shipment = list.get(i);
			if (i == (list.size() - 1)) {
				idStr.append("'"+shipment.getOrderno()+"'");
			}else {
				idStr.append("'"+shipment.getOrderno()+"',");
			}
		}
		return shipmentMapper.selectUnExistShipMent(idStr.toString());
		
		
//		IOrderwsDao orderwsDao = new OrderwsDao();
//		return orderwsDao.validateShipmentList(list, uuid);
	}

	@Override
	public List<Shipment> selectShipmentList(List<Integer> list) {
		return shipmentMapper.selectShipmentByids(list);
	}

	@Override
	public DataGridResult loadShipment(Map<String,String> map) {
		try{
			DecimalFormat    df   = new DecimalFormat("######0.00");   
			DataGridResult result = new DataGridResult();
			List<Shipment> list=new ArrayList<Shipment>();
			HashMap<String,Double> countMap=new HashMap<String,Double>();
			map.put("exchageRate",String.valueOf(Util.EXCHANGE_RATE));
			String choiseType=map.get("choiseType");
			if("0".equals(choiseType) || "1".equals(choiseType)){
				list = shipmentMapper.queryAllShipmentInfo(map);
				countMap=shipmentMapper.queryAllShipmentInfoCount(map);
			}else if("2".equals(choiseType) || "3".equals(choiseType)){
				list = shipmentMapper.queryBeyondFiveShipmentInfo(map);
				countMap=shipmentMapper.queryBeyondFiveShipmentInfoCount(map);
			}
			//统计物流商账单总运费、总预估运费、当月物流赔偿款、最终审核可支付总金额(已减赔偿款)
			if(list!=null && list.size()>0){
				double compensation=shipmentMapper.getCompensationAmounts(map.get("senttimeBegin"),map.get("senttimeEnd"), map.get("company"));
				double up_price=countMap.get("actual_freight")==null?0.00:countMap.get("actual_freight");
				up_price=up_price-compensation;
				result.setRemark("物流商账单总运费:【"+countMap.get("totalprice")+"】总预估运费:【"+(countMap.get("estimatefreight")==null?"0.00":countMap.get("estimatefreight"))+"】当月物流赔偿款:【"+compensation+"】最终审核可支付总金额(已减赔偿款):【"+up_price+"】");
				result.setCurrePage(Integer.valueOf(map.get("page")));
				result.setPageSize(Integer.valueOf(map.get("pageSize")));
				result.setRows(list);
				int totalCount = Integer.parseInt(String.valueOf(countMap.get("orderCount")));
				result.setTotalCount(totalCount);
				result.setTotalPage(totalCount % 40 == 0 ? totalCount / 40 :totalCount / 40 + 1);
				return result;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws ParseException {
		String s="2017-07-11";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(s);
		Calendar calendar = Calendar.getInstance();//日历对象  
        calendar.setTime(date);//设置当前日期  
        calendar.add(Calendar.MONTH, -1);//月份减一  
        System.out.println(sdf.format(calendar.getTime()));//输出上个月的日期  
        System.out.println(sdf.format(date));//输出当前时间  
	}
	
	 public boolean isLastDayOfMonth(String dateString) { 
		 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = sdf.parse(dateString);
				Calendar calendar = Calendar.getInstance(); 
		        calendar.setTime(date); 
		        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1)); 
		        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) { 
		            return true; 
		        } 
		        return false; 
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return false;
	    } 
	
	@Override
	public List<Shipment> loadExportShipment(String company, String senttimeBegin, String senttimeEnd, String choiseType) {
		return shipmentMapper.selectShipment(company, senttimeBegin, senttimeEnd, choiseType, 0, 0,Util.EXCHANGE_RATE);
	}

	@Override
	public HashMap<?, ?> selectCountShipment(String company, String senttimeBegin, String senttimeEnd,String choiseType,double exchageRate) {
		return shipmentMapper.selectCountShipment(company, senttimeBegin, senttimeEnd, choiseType,exchageRate);
	}
	
	@Override
	public String getNoTolPrice(String company, String senttimeBegin, String senttimeEnd,String choiseType) {
		return shipmentMapper.getNoTolPrice(company, senttimeBegin, senttimeEnd, choiseType);
	}

	@Override
	public int deleteShipment(List<Integer> idList) {
		return shipmentMapper.deleteShipment(idList);
	}
	@Override
	public int insertSources(String amounts, String datas,String comps) {
		//判断该物流商在该月的赔偿款是否已经录入
		int is_exit=shipmentMapper.getLogisticsCompensation(datas,comps);
		if(is_exit>0){
			return shipmentMapper.upSources(amounts,datas,comps);
		}else{
			return shipmentMapper.insertSources(amounts,datas,comps);
		}
	}
	/**
	 * 人工判断运输公司给的运费可以支付
	 * whj 2017-08-31
	 */
	@Override
	public int updateShipMentFlag(List<Integer> idList,String flag_remark,String totalprice) {
		return shipmentMapper.updateShipMentFlag(idList,flag_remark,totalprice);
	}
	
	@Override
	public List<String[]> PurchaseStatisticsInquiry(Map<Object, Object> map) {
		List<String[]> orderinfo_ = new ArrayList<String[]>();
		List<Map<String, Object>> list_data = shipmentMapper.PurchaseStatisticsInquiry(map);
		if(list_data != null && list_data.size() > 0){
			for (int i = 0; i < list_data.size(); i++) {
				Map<String, Object> orderinfo = list_data.get(i);
				orderinfo_.add(new String[]{orderinfo.get("times").toString(),
						String.valueOf(Integer.valueOf(orderinfo.get("od_buy_time").toString())/Integer.valueOf(orderinfo.get("oi_count").toString())),
						String.valueOf(Integer.valueOf(orderinfo.get("goods_buy_time").toString())/Integer.valueOf(orderinfo.get("od_count").toString())),
						String.valueOf(Integer.valueOf(orderinfo.get("ck_counts").toString())/Integer.valueOf(orderinfo.get("oi_count").toString())),
						orderinfo.get("od_count").toString()});
			}
		}
		return orderinfo_;
	}

	@Override
	public List<String[]> shipmentTimeCount(String paytimestart,String paytimeend,int days) {
		List<Map<String, Object>> orderinfos = shipmentMapper.shipmentTimeCount(paytimestart, paytimeend,days);
 
//		int count = shipmentMapper.countShipmentTime(paytimestart,paytimeend,days);
		
		int deliverGoodsCountTime = 0 ;  //总发货时效
		int delaysGoodsCount = 0 ;       //延迟发货总数
		int orderCount = 0 ;             // 订单总数
		List<String[]> orderinfo_ = new ArrayList<String[]>();
		if(orderinfos != null && orderinfos.size() > 0){
			for (int i = 0; i < orderinfos.size(); i++) {
				Map<String, Object> orderinfo = orderinfos.get(i);
				Object admname = orderinfo.get("admname");
				deliverGoodsCountTime += Integer.parseInt(orderinfo.get("intervals").toString());
				if(Double.parseDouble(orderinfo.get("delays").toString())>0){
					delaysGoodsCount++;
				}
				orderinfo_.add(new String[]{orderinfo.get("orderno").toString(),orderinfo.get("orderpaytime").toString(),
						orderinfo.get("createtime").toString(),orderinfo.get("delivery_time").toString(),orderinfo.get("intervals").toString(),
						orderinfo.get("delays").toString(),admname == null ?"":admname.toString(),orderinfo.get("state").toString(),orderinfo.get("od_count").toString(),orderinfo.get("cg_time")==null?"":orderinfo.get("cg_time").toString()});
			}
			orderCount = orderinfos.size();
			orderinfo_.add(new String[]{String.valueOf(orderCount),String.valueOf(deliverGoodsCountTime),String.valueOf(delaysGoodsCount)});
		}
		return orderinfo_;
	}
	@Override
	public int getChTime() {
		int times=shipmentMapper.getChTime();;
		return times;
	}
	@Override
	public int getDetailsCount() {
		
		return shipmentMapper.getDetailsCount();
	}
	@Override
	public int getOrderCount() {
		
		return shipmentMapper.getOrderCount();
	}
	@Override
	public int getGoodsbuytime() {
		int times=shipmentMapper.getGoodsbuytime();
		return times;
	}
	@Override
	public int getOrderBuyTime() {
		int times=shipmentMapper.getOrderBuyTime();
		return times;
	}

	@Override
	public List<Shipment> showCalculFreight(String time) {
		
		return shipmentMapper.showCalculFreight(time);
	}

	@Override
	public int check(String start ,String end) {
		
		return shipmentMapper.check(start,end);
	}

}
