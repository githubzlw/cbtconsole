package com.cbt.report.service;

import com.cbt.pojo.BasicReport;
import com.cbt.refund.bean.PayPalImportInfo;
import com.cbt.report.dao.BasicReportMapper;
import com.cbt.report.vo.*;
import com.cbt.util.DateFormatUtil;
import com.cbt.util.Util;
import com.cbt.warehouse.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class BasicReportServiceImpl implements BasicReportService {

	@Autowired
	private BasicReportMapper reportMapper;

	@Override
	public int updateByPrimaryKey(BasicReport record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderFinancialBean> queryOrderFinancial(String year, String month) {
		return reportMapper.queryOrderFinancial(year, month);
	}

	@Override
	public void insertOrderFinancial(String beginDate, String endDate) {
		reportMapper.insertOrderFinancial(beginDate, endDate);
	}

	@Override
	public List<RefundInfoBean> queryPaypalRefunds(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryPaypalRefunds(beginDate, endDate, start, rows);
	}

	@Override
	public int queryPaypalRefundsCount(String beginDate, String endDate) {
		return reportMapper.queryPaypalRefundsCount(beginDate, endDate);
	}

	@Override
	public List<CustomerBalanceChangeBean> queryCustomerBalanceChange(String beginDate, String endDate, int start,
			int rows) {
		return reportMapper.queryCustomerBalanceChange(beginDate, endDate, start, rows);
	}

	@Override
	public int queryCustomerBalancesChangeCount(String beginDate, String endDate) {
		return reportMapper.queryCustomerBalancesChangeCount(beginDate, endDate);
	}

	@Override
	public int onloadOrderFinancialDate(Map<String, String> map) {
		map.put("time",map.get("year")+"-"+map.get("month"));
		return reportMapper.onloadOrderFinancialDate(map);
	}

	@Override
	public List<OrderInfoBean> queryOrderSales(String beginDate, String endDate, int start, int rows,int type) {

	    List<OrderInfoBean> list;
	    LocalDateTime beginDateTime = DateFormatUtil.getTimeWithStr(beginDate);
		LocalDateTime checkDate = LocalDateTime.of(2019, 11, 1, 0, 0, 0);

		// 2019-12月份之前的数据查询老订单，之后的数据查询副本数据
		if(beginDateTime.isBefore(checkDate)){
		    list=reportMapper.queryOrderSales(beginDate, endDate, start, rows);
        }else{
		    list=reportMapper.queryOrderTranscriptSales(beginDate, endDate, start, rows);
        }

		DecimalFormat df = new DecimalFormat("######0.00");
		for(int i=0;i<list.size();i++){
			OrderInfoBean o=list.get(i);
			if(type == 0){
				o.setOrderNo("<a target='_blank' href='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+o.getOrderNo()+"'>"+o.getOrderNo()+"</a>");
				o.setUserId("<a target='_blank' href='/cbtconsole/userinfo/getUserInfo.do?userId="+o.getUserId()+"'>"+o.getUserId()+"</a>");
			}
			o.setProfit(Double.parseDouble(df.format(o.getPayPrice()-o.getBuyAmount()/Util.EXCHANGE_RATE-o.getFrightAmount()/Util.EXCHANGE_RATE)));
			o.setBuyAmount(Double.parseDouble(df.format((o.getBuyAmount()+o.getPid_amount())/ Util.EXCHANGE_RATE)));
			o.setFrightAmount(Double.parseDouble(df.format(o.getFrightAmount()/Util.EXCHANGE_RATE)));
			o.setExpressNo(Util.getStr(o.getExpressNo()).toString());
			
			if(StringUtil.isBlank(o.getExchange_rate()) || "null".equals(o.getExchange_rate())){
				o.setExchange_rate("6.3");
			}
			o.setEstimatefreight(Double.parseDouble(df.format(o.getEstimatefreight()*Double.parseDouble(o.getExchange_rate()))));
		}
		return list;
	}

    @Override
    public List<OrderInfoBean> queryOrderTranscriptSales(String beginDate, String endDate, int start, int rows) {
        return reportMapper.queryOrderTranscriptSales(beginDate, endDate, start, rows);
    }

    @Override
    public int queryOrderTranscriptSalesCount(String beginDate, String endDate) {
        return reportMapper.queryOrderTranscriptSalesCount(beginDate, endDate);
    }

    @Override
	public int queryOrderSalesCount(String beginDate, String endDate) {
	    // 2019-12月份之前的数据查询老订单，之后的数据查询副本数据
	    LocalDateTime beginDateTime = DateFormatUtil.getTimeWithStr(beginDate);
		LocalDateTime checkDate = LocalDateTime.of(2019, 11, 1, 0, 0, 0);
		if(beginDateTime.isBefore(checkDate)){
		    return reportMapper.queryOrderSalesCount(beginDate, endDate);
        }else{
		    return reportMapper.queryOrderTranscriptSalesCount(beginDate, endDate);
        }
	}

	@Override
	public List<PayPalInfoBean> queryPayPalRevenue(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryPayPalRevenue(beginDate, endDate, start, rows);
	}

	@Override
	public List<PayPalInfoBean> queryWireRevenue(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryWireRevenue(beginDate, endDate, start, rows);
	}

	@Override
	public int queryWireRevenueCount(String beginDate, String endDate) {
		return reportMapper.queryWireRevenueCount(beginDate, endDate);
	}

	@Override
	public int queryPayPalRevenueCount(String beginDate, String endDate) {
		return reportMapper.queryPayPalRevenueCount(beginDate, endDate);
	}

	@Override
	public List<PayPalInfoBean> queryNotStartedOrderAmount(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryNotStartedOrderAmount(beginDate, endDate, start, rows);
	}

	@Override
	public int queryNotStartedOrderAmountCount(String beginDate, String endDate) {
		return reportMapper.queryNotStartedOrderAmountCount(beginDate, endDate);
	}

	@Override
	public List<RefundInfoBean> queryTotalRefund(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryTotalRefund(beginDate, endDate, start, rows);
	}

	@Override
	public int queryTotalRefundCount(String beginDate, String endDate) {
		return reportMapper.queryTotalRefundCount(beginDate, endDate);
	}

	@Override
	public List<PayPalInfoBean> queryBalancePayment(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryBalancePayment(beginDate, endDate, start, rows);
	}

	@Override
	public int queryBalancePaymentCount(String beginDate, String endDate) {
		return reportMapper.queryBalancePaymentCount(beginDate, endDate);
	}

	@Override
	public List<RefundInfoBean> queryBalanceWithdrawal(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryBalanceWithdrawal(beginDate, endDate, start, rows);
	}

	@Override
	public int queryBalanceWithdrawalCount(String beginDate, String endDate) {
		return reportMapper.queryBalanceWithdrawalCount(beginDate, endDate);
	}

	@Override
	public void batchSavePayPalInfoByExcel(List<PayPalImportInfo> infos) {
		reportMapper.batchSavePayPalInfoByExcel(infos);
	}

	@Override
	public List<BalanceCompensation> queryBalanceCompensation(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryBalanceCompensation(beginDate, endDate, start, rows);
	}

	@Override
	public int queryBalanceCompensationCount(String beginDate, String endDate) {
		return reportMapper.queryBalanceCompensationCount(beginDate, endDate);
	}

	@Override
	public List<OrderCancelBean> queryOrderCancel(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryOrderCancel(beginDate, endDate, start, rows);
	}

	@Override
	public List<OrderCancelBean> queryOrderConfirmCancel(String beginDate, int start, int rows) {
		return reportMapper.queryOrderConfirmCancel(beginDate, start, rows);
	}

	@Override
	public int queryOrderConfirmCancelCount(String beginDate) {
		return reportMapper.queryOrderConfirmCancelCount(beginDate);
	}

	@Override
	public int queryOrderCancelCount(String beginDate, String endDate) {
		return reportMapper.queryOrderCancelCount(beginDate, endDate);
	}

	@Override
	public List<EditedProductProfits> queryEditedProductProfits(String beginDate, String endDate, int start, int rows,
			int sorting,double exchageRate) {
		return reportMapper.queryEditedProductProfits(beginDate, endDate, start, rows, sorting,exchageRate) ;
	}

	@Override
	public int queryEditedProductProfitsCount(String beginDate, String endDate) {
		return reportMapper.queryEditedProductProfitsCount(beginDate, endDate);
	}

	@Override
	public List<PayPalInfoBean> queryStripPayInfo(String beginDate, String endDate, int start, int rows) {
		return reportMapper.queryStripPayInfo(beginDate,endDate,start,rows);
	}

	@Override
	public int queryStripPayInfoCount(String beginDate, String endDate) {
		return reportMapper.queryStripPayInfoCount(beginDate, endDate);
	}

}