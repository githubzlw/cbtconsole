package com.cbt.report.service;

import com.cbt.bean.ExpbuyGoods;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.*;
import com.cbt.report.dao.GeneralReportMapper;
import com.cbt.report.vo.StatisticalReportVo;
import com.cbt.util.BigDecimalUtil;
import com.cbt.warehouse.pojo.JcexPrintInfo;
import com.cbt.warehouse.pojo.Shipments;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.cbt.website.bean.InventoryCheckRecord;
import com.cbt.website.bean.InventoryCheckWrap;
import com.cbt.website.bean.InventoryData;
import com.cbt.website.bean.InventoryLog;
import com.cbt.website.bean.LossInventoryWrap;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author zlg
 *
 */
@Service("GeneralReportService")
public class GeneralReportServiceImpl implements GeneralReportService{

	@Autowired
	private GeneralReportMapper generalReportDao;

	/**
	 * @param example
	 * 按条件删除总报表信息
	 */
	@Override
	public int deleteByExample(GeneralReportExample example) {
		return generalReportDao.deleteByExample(example);
	}

	/**
	 * @param record
	 * 插入总报表
	 */
	@Override
	public int insertSelective(GeneralReport record) {
		generalReportDao.insertSelective(record);
		int rows =	 record.getId();
		return rows;
	}

	/**
	 * @param example
	 * 查询总报表
	 */
	@Override
	public List<GeneralReport> selectByExample(GeneralReportExample example) {
		return generalReportDao.selectByExample(example);
	}

	String[] excelHeader = { "序号", "商品分类", "采购金额","销售金额","采购数量","销售数量","平均价","盈亏(%)"};
	String[] excelTotal = { "序号", "分类数量","总支出","总收入","商品数","无效采购量","均价","盈亏(%)"};
	String[] excelHeader1 = { "序号", "商品订单", "采购金额","销售金额","采购数量","销售数量","平均价","运费","盈亏(%)"};
	String[] excelHeader2 = { "序号", "商品订单","运单号", "采购金额","销售金额","采购数量","销售数量","平均价","实际运费","盈亏金额","盈亏(%)","估算重量","计费重量","实收重量","出货类型"};
	String[] excelTotal1 = { "序号", "总支出","总收入","商品数","无效采购量","均价","总运费","盈亏(%)"};
	String[] excelTotal2 = { "序号","商品类别","商品名称","销售数量","销售价格($)","采购次数","采购总数","采购价格(￥)","采购总成本(￥)"};
	String[] excelTotal3 = { "序号","采购订单","订单总金额","运费","采购商品数量","快递单号","采购账号","订单日期","支付时间","订单状态","入库状态","备注"};
	String[] excelTotal4 = { "序号","商品名称","网站链接","库存库位","商品规格","商品货源","采购价（￥）","商品类别","库存数量","盘点后库存数量","库存金额","盘点后库存金额","首次盘点时间","最后更新时间","是否上架"};
	String[] excelTotal5 = { "序号","月份","销售额(S)","余额补偿","月初支付宝余额(A)￥","月末支付宝余额(B)￥","银行转账给支付宝(C)￥","抓取采购订单金额(D1)￥","公式计算(D3)￥","支付宝付款(D2)￥","对应正常订单采购金额(E)￥","对应取消订单采购金额(F)￥","无订单匹配采购金额(G)￥","未入库采购金额(H)￥","对应上月订单采购金额(J)￥","库存金额(I)￥","销售库存金额(K)￥","预估运费金额(W1)￥","物流公式运费(W2)￥","订单运费总和(W3)￥","实际支付运费(W4)","月度利润(P)"};
	String[] excelTotal6 = { "序号","下单来源","订单号","订单状态","商品名称","商品链接","商品单价","商品数量","国内运费","订单总价","商品规格","订单时间","支付时间","发货时间","采购人"};
	String[] excelTotal7 = { "序号","公司出云号","出运订单号","合并出运单号","出运时间","实际出运时间","我司重量","运输公司重量","我司体积","抛重","运输公司","运输方式","运输国家","运输单号","预估运费","实际运费","出运备注"};
	String[] excelTotal8 = { "序号","运单单号","出运时间","出运国家","运输公司","运输方式","包裹类型","实际重量","体积重量","结算重量","运费金额"};
	String[] excelTotal9 = { "序号","商品名称","商品规格","商品图片","采购价格","库存数量","库存金额","库存位置","库存时间"};
	String[] excelTotal10 = { "序号","订单号","商品编号","商品图片","商品规格","使用数量","使用总价","采购是否使用","是否已使用","状态","创建时间"};
	String[] excelTotal11 = { "序号","运单条","寄件人","英文公司名称","发件人邮编","收件人","收件地址","收件国家","收件城市","州名","收件邮编","收件人电话","结算方式","包装","中文品名","HS CODE","申报币种","支付币种","重量","体积","英文品名","内件数","申报价值","备注","参考单号"};
	String[] excelTotal12 = { "序号","订单号","商品编号","产品规格","产品图片","采购数量","采购价格","审核状态","批准人","申请人","生成时间","付款时间","申请总额","申请备注"};
	String[] excelTotal13 = { "序号","商品分类","采购金额（人民币）","销售金额（人民币）","平均价格（人民币）","销售数量（个）","采购数量（个）","盈亏（%）"};
	String[] excelTota20 = { "序号","商品名称","网站链接","采购链接","商品规格","盘点前数量","盘点后数量","盘点前库位","盘点后库位","盘点人","盘点时间","盘点备注"};
	String[] excelTota21 = { "序号","商品名称","网站链接","商品库位","商品规格","采购价","首次库存数量","首次库存金额","盘点后库存数量","盘点后库存金额","可用库存数量","首次录入时间","最后更新库存时间","删除人","删除时间","删除备注"};
	String[] excelTota22 = { "序号","盘点前库存","盘点后库存","盘点前库位","盘点后库位","商品规格","损耗单价","损耗库存金额","损耗时间","损耗人","损耗原因"};
	String[] excelTota23 = { "序号","支付月份","用户ID","用户邮箱","VIP等级","订单数量","实际重量（kg）","预估重量(kg)","实际支付金额(RMB)","实际采购金额(RMB)","实际运费(RMB)","客户付的运费(RMB)","用户利润(RMB)","用户利润率(%)","预计运费(RMB)","用户利润预估(RMB)","用户预估利润率(%)"};
	String[] excelTota24 = { "序号","商品ID","产品名","skuid","specid","sku","盘点前库存","盘点后库存","差异值","库位","时间"};
	String[] excelTota25 = { "序号","类别","商品ID","skuid","商品名称","商品sku","商品图片","上次盘点数量","库存数量","库位","盘点数量"};
	String[] excelTota26 = { "序号","时间","odid","商品ID","skuid","商品名称","商品sku","库存数量","库位","价格","盘点数量"};
	String[] excelTota27 = { "序号","时间","商品ID","skuid","类型","数量","备注"};
	String[] excelTota28 = { "序号","时间","商品ID","skuid","产品名称","产品Sku","变更前库存","变更数量","变更后库存","变更类型","备注"};
	
	@Override
	public HSSFWorkbook exportUserProfitByMonth(List<OrderSalesAmountPojo> list) {
		String sheetName = "用户月利润统计报表";
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0);
		hcell.setCellValue("用户月利润统计报表");
		row = sheet.createRow(rows++);
		for (int i = 0; i < excelTota23.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota23[i]);
			cell.setCellStyle(style);
		}
		try{
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				OrderSalesAmountPojo bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getTimes());
				row.createCell(2).setCellValue(bg.getId());
				row.createCell(3).setCellValue(bg.getEmail());
				row.createCell(4).setCellValue(bg.getGrade());
				row.createCell(5).setCellValue(bg.getOrderCount());
				row.createCell(6).setCellValue(bg.getAc_weight());
				row.createCell(7).setCellValue(bg.getEs_weight());
				row.createCell(8).setCellValue(bg.getSalesAmount());
				row.createCell(9).setCellValue(bg.getBuyAmount());
				row.createCell(10).setCellValue(bg.getFreight());
				row.createCell(11).setCellValue(bg.getCustom_freight());
				row.createCell(12).setCellValue(bg.getForecastProfits());
				row.createCell(13).setCellValue(bg.getProfits());
				row.createCell(14).setCellValue(bg.getEstimateFreight());
				row.createCell(15).setCellValue(bg.getEstimateProfit());
				row.createCell(16).setCellValue(bg.getEsprofits());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 报表导出类
	 * @param
	 */
	@Override
	public HSSFWorkbook export(StatisticalReportVo statisticalReportVo,String type) {
		List<ReportInfo> reportInfoList = statisticalReportVo.getReportInfoList();
		List<ReportDetailOrder> reportDetailOrder = null;
		List<ReportDetail> reportDetailList = null;
		if(type.equals("order")){
			reportDetailOrder =  statisticalReportVo.getReportDetailOrder();
		}else{
			reportDetailList = statisticalReportVo.getReportDetailList();
		}
		String sheetName = statisticalReportVo.getYear()+"年"+statisticalReportVo.getMonth()+"月";
		if(statisticalReportVo.getWeek() != null && statisticalReportVo.getWeek() != ""){
			sheetName += "第"+ statisticalReportVo.getWeek() +"周";
		}
		else if(statisticalReportVo.getDay() != null && statisticalReportVo.getDay() != ""){
			sheetName += statisticalReportVo.getDay() +"日";
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("报表合计：");
		row = sheet.createRow(rows++);  //到下一行添加数据
		if(type!=null && type.equalsIgnoreCase("order")){
			for (int i = 0; i < excelTotal1.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal1[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0; i < reportInfoList.size(); i++) {
				row = sheet.createRow(rows++);
				ReportInfo reportInfo = reportInfoList.get(i);
				row.createCell(0).setCellValue(i+1);
				//row.createCell(1).setCellValue(reportInfo.getOrderNum());
				row.createCell(1).setCellValue(reportInfo.getTotalExpenditure());
				row.createCell(2).setCellValue(reportInfo.getTotalRevenue());
				row.createCell(3).setCellValue(reportInfo.getGoodsCount());
				row.createCell(4).setCellValue(0); //无效采购量暂时为0
				row.createCell(5).setCellValue(reportInfo.getAveragePrice());
				row.createCell(6).setCellValue(reportInfo.getFreight());
				row.createCell(7).setCellValue(reportInfo.getProfitLoss());
			}

			row = sheet.createRow(rows++);  //让行数增加
			HSSFCell hcell2 = row.createCell(0); //添加标题
			hcell2.setCellValue("报表详细：");
			row = sheet.createRow(rows++);  //到下一行添加数据
			if(type.equals("order")){
				for (int i = 0; i < excelHeader2.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellValue(excelHeader2[i]);
					cell.setCellStyle(style);
				}
			}else{
				for (int i = 0; i < excelHeader1.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellValue(excelHeader1[i]);
					cell.setCellStyle(style);
				}
			}
			//写入报表详细
			if(type.equals("order")){
				for (int i = 0; i < reportDetailOrder.size(); i++) {
					row = sheet.createRow(rows++);
					ReportDetailOrder reportDetail = reportDetailOrder.get(i);
					row.createCell(0).setCellValue(i+1);
					String  remarks = reportDetail.getRemarks();
					if(StrUtils.isNotNullEmpty(remarks)){
						if(remarks.length()>20&&remarks.indexOf("O")>-1){
							row.createCell(1).setCellValue(remarks);
						}else{
							row.createCell(1).setCellValue(reportDetail.getOrderno());
						}
					}else{
						row.createCell(1).setCellValue(reportDetail.getOrderno());
					}
					row.createCell(2).setCellValue(reportDetail.getExpressno());   // 添加运单号
					row.createCell(3).setCellValue(reportDetail.getPurchasePrice());
					row.createCell(4).setCellValue(reportDetail.getSalesPrice());
					row.createCell(5).setCellValue(reportDetail.getBuycount());
					row.createCell(6).setCellValue(reportDetail.getSalesVolumes());
					row.createCell(7).setCellValue(reportDetail.getAveragePrice());
					row.createCell(8).setCellValue(reportDetail.getFreight());
					row.createCell(9).setCellValue(reportDetail.getSalesPrice()-reportDetail.getPurchasePrice()-reportDetail.getFreight()); //盈亏金额
					row.createCell(10).setCellValue(reportDetail.getProfitLoss());
					row.createCell(11).setCellValue(reportDetail.getOd_weight());
					row.createCell(12).setCellValue(reportDetail.getFee_weight());
					row.createCell(13).setCellValue(reportDetail.getFw_weight());
					if(reportDetail.getTypeship().equals("2")){
						row.createCell(14).setCellValue("合并出货");
					}else{
						row.createCell(14).setCellValue("正常出货");
					}
				}
			}else{
				for (int i = 0; i < reportDetailList.size(); i++) {
					row = sheet.createRow(rows++);
					ReportDetail reportDetail = reportDetailList.get(i);
					row.createCell(0).setCellValue(i+1);
					row.createCell(1).setCellValue(reportDetail.getCategroy());
					row.createCell(2).setCellValue(reportDetail.getPurchasePrice());
					row.createCell(3).setCellValue(reportDetail.getSalesPrice());
					row.createCell(4).setCellValue(reportDetail.getBuycount());
					row.createCell(5).setCellValue(reportDetail.getSalesVolumes());
					row.createCell(6).setCellValue(reportDetail.getAveragePrice());
					row.createCell(7).setCellValue(reportDetail.getFreight());
					row.createCell(8).setCellValue(reportDetail.getProfitLoss());
				}
			}
		}else{
			for (int i = 0; i < excelTotal.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0; i < reportInfoList.size(); i++) {
				row = sheet.createRow(rows++);
				ReportInfo reportInfo = reportInfoList.get(i);
				row.createCell(0).setCellValue(i+1);
				row.createCell(1).setCellValue(reportInfo.getCategroyNum());
				row.createCell(2).setCellValue(reportInfo.getTotalExpenditure());
				row.createCell(3).setCellValue(reportInfo.getTotalRevenue());
				row.createCell(4).setCellValue(reportInfo.getGoodsCount());
				row.createCell(5).setCellValue(0); //无效采购量暂时为0
				row.createCell(6).setCellValue(reportInfo.getAveragePrice());
				row.createCell(7).setCellValue(reportInfo.getProfitLoss());
			}

			row = sheet.createRow(rows++);  //让行数增加
			HSSFCell hcell2 = row.createCell(0); //添加标题
			hcell2.setCellValue("报表详细：");
			row = sheet.createRow(rows++);  //到下一行添加数据
			for (int i = 0; i < excelHeader.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelHeader[i]);
				cell.setCellStyle(style);
			}
			//写入报表详细
			if(type.equals("order")){
				for (int i = 0; i < reportDetailOrder.size(); i++) {
					row = sheet.createRow(rows++);
					ReportDetailOrder reportDetail = reportDetailOrder.get(i);
					row.createCell(0).setCellValue(i+1);
					row.createCell(1).setCellValue(reportDetail.getOrderno());
					row.createCell(2).setCellValue(reportDetail.getPurchasePrice());
					row.createCell(3).setCellValue(reportDetail.getSalesPrice());
					row.createCell(4).setCellValue(reportDetail.getBuycount());
					row.createCell(5).setCellValue(reportDetail.getSalesVolumes());
					row.createCell(6).setCellValue(reportDetail.getAveragePrice());
					row.createCell(7).setCellValue(reportDetail.getProfitLoss());
					row.createCell(8).setCellValue(reportDetail.getOd_weight());
					row.createCell(9).setCellValue(reportDetail.getFee_weight());
					row.createCell(10).setCellValue(reportDetail.getFw_weight());
				}
			}else{
				for (int i = 0; i < reportDetailList.size(); i++) {
					row = sheet.createRow(rows++);
					ReportDetail reportDetail = reportDetailList.get(i);
					row.createCell(0).setCellValue(i+1);
					row.createCell(1).setCellValue(reportDetail.getCategroy());
					row.createCell(2).setCellValue(reportDetail.getPurchasePrice());
					row.createCell(3).setCellValue(reportDetail.getSalesPrice());
					row.createCell(4).setCellValue(reportDetail.getBuycount());
					row.createCell(5).setCellValue(reportDetail.getSalesVolumes());
					row.createCell(6).setCellValue(reportDetail.getAveragePrice());
					row.createCell(7).setCellValue(reportDetail.getProfitLoss());
				}
			}
		}
		return wb;
	}

	/**
	 * 按条件查询报表信息
	 * @param generalReport
	 * @return GeneralReport
	 */
	@Override
	public GeneralReport selectBySelective(GeneralReport generalReport) {
		GeneralReport generalReport1= generalReportDao.selectBySelective(generalReport);
		return generalReport1;
	}

	/**
	 * 报表导出类
	 * @param
	 */
	@Override
	public HSSFWorkbook exportTaoBaoOrder(List<TaoBaoOrderInfo> list,double pagePrice) {
		String sheetName = "对账报表"; //报表页名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("采购总金额："+pagePrice);
		row = sheet.createRow(rows++);  //到下一行添加数据
		if(1==1){
			for (int i = 0; i < excelTotal3.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal3[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				TaoBaoOrderInfo bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getOrderid());
				row.createCell(2).setCellValue(bg.getTotalprice());
				row.createCell(3).setCellValue(bg.getPreferential());
				row.createCell(4).setCellValue(bg.getTotalqty());
				row.createCell(5).setCellValue(bg.getShipno());
				row.createCell(6).setCellValue(bg.getUsername());
				row.createCell(7).setCellValue(bg.getOrderdate());
				row.createCell(8).setCellValue(bg.getPaydata());
				row.createCell(9).setCellValue(bg.getOrderstatus());
				row.createCell(10).setCellValue(bg.getIid()>0?"已入库":"未入库");
				row.createCell(11).setCellValue(bg.getOpsorderid());
			}
		}
		return wb;
	}
	/**
	 * 当月产生库存明细报表导出
	 */
	@Override
	public HSSFWorkbook exportInventoryAmount(List<InventoryDetailsPojo> list) {
		String sheetName = "当月产生库存明细报表"; //报表页名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		row = sheet.createRow(rows++);  //到下一行添加数据
		if(1==1){
			for (int i = 0; i < excelTotal9.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal9[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				InventoryDetailsPojo bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getGoodName());
				row.createCell(2).setCellValue(bg.getCar_type());
				row.createCell(3).setCellValue(bg.getCar_img());
				row.createCell(4).setCellValue(bg.getInventory_price());
				row.createCell(5).setCellValue(bg.getInventory_acount());
				row.createCell(6).setCellValue(bg.getInvetory_amount());
				row.createCell(7).setCellValue(bg.getInventory_barcode());
				row.createCell(8).setCellValue(bg.getCreatetime());
			}
		}
		return wb;
	}
	/**
	 * 线下采购付款申请明细报表
	 */
	@Override
	public HSSFWorkbook exportOfflinePayment(List<OfflinePaymentApplicationPojo> list) {
		String sheetName = "线下采购付款申请明细报表"; //报表页名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTotal12.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTotal12[i]);
			cell.setCellStyle(style);
		}
		//写入报表汇总
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rows++);
			OfflinePaymentApplicationPojo bg = list.get(i);
			row.createCell(0).setCellValue((i+1));
			row.createCell(1).setCellValue(bg.getOrderid());
			row.createCell(2).setCellValue(bg.getGoodsid());
			row.createCell(3).setCellValue(bg.getCar_type());
			row.createCell(4).setCellValue(bg.getCar_img());
			row.createCell(5).setCellValue(bg.getBuycount());
			row.createCell(6).setCellValue(bg.getGoods_p_price());
			row.createCell(7).setCellValue("1".equals(bg.getFlag())?"已批准":"未批准");
			row.createCell(8).setCellValue(bg.getAdmName());
			row.createCell(9).setCellValue(bg.getApplicantName());
			row.createCell(10).setCellValue(bg.getCreatetime());
			row.createCell(11).setCellValue(bg.getPaydata());
			row.createCell(12).setCellValue(bg.getAmount());
			row.createCell(13).setCellValue(bg.getOff_remark());
		}
		return wb;
	}
	/**
	 * 当月产生库存明细报表
	 */
	@Override
	public HSSFWorkbook exportSaleInventory(List<InventoryDetailsPojo> list) {
		String sheetName = "当月产生库存明细报表"; //报表页名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		row = sheet.createRow(rows++);  //到下一行添加数据
		if(1==1){
			for (int i = 0; i < excelTotal10.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal10[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				InventoryDetailsPojo bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getOrderno());
				row.createCell(2).setCellValue(bg.getGoodsid());
				row.createCell(3).setCellValue(bg.getCar_img());
				row.createCell(4).setCellValue(bg.getCar_type());
				row.createCell(5).setCellValue(bg.getLock_remaining());
				row.createCell(6).setCellValue(bg.getLock_inventory_amount());
				row.createCell(7).setCellValue(bg.getIs_use());
				row.createCell(8).setCellValue(bg.getFlag());
				row.createCell(9).setCellValue(bg.getIs_delete());
				row.createCell(10).setCellValue(bg.getCreatetime());
			}
		}
		return wb;
	}
	/**
	 * 采购订单报表导出
	 * @param list
	 * @return
	 */
	@Override
	public HSSFWorkbook exportBuyOrderDetails(List<TaoBaoOrderInfo> list,String type, BuyReconciliationPojo buyReconciliationPojo) {
		String sheetName = ""; //报表页名
		if ("1".equals(type)) {
			sheetName = "有匹配货源无入库采购订单明细";
		} else if ("2".equals(type)) {
			sheetName = "采购订单对应取消销售订单明细";
		} else if ("3".equals(type)) {
			sheetName = "无订单匹配采购订单明细";
		} else if ("4".equals(type)) {
			sheetName = "采购订单对应上月销售订单";
		} else if ("5".equals(type)) {
			sheetName = "抓取正常采购订单明细";
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		// 订单汇总数据
		String totalName = "";
		if (sheetName.contains("明细")) {
			totalName = sheetName.replace("明细", "汇总");
		} else {
			totalName = sheetName + "汇总";
		}
		Map<String, List<TaoBaoOrderInfo>> listMap = list.stream().collect(Collectors.groupingBy(TaoBaoOrderInfo::getOrderid));
		HSSFSheet sheetOne = wb.createSheet(totalName);
		int rowsOne = 0;  //记录行数
		HSSFRow rowOne = sheetOne.createRow(rowsOne++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowOne = sheetOne.createRow(rowsOne++);  //到下一行添加数据
		for (int i = 0; i < excelTotal6.length; i++) {
			HSSFCell cell = rowOne.createCell(i);
			cell.setCellValue(excelTotal6[i]);
			cell.setCellStyle(style);
		}
		int total = 0;
		double totalBalance = 0;
		for (String orderId : listMap.keySet()) {
			rowOne = sheetOne.createRow(rowsOne++);
			TaoBaoOrderInfo bg = listMap.get(orderId).get(0);
			totalBalance += Double.valueOf(bg.getTotalprice());
			rowOne.createCell(0).setCellValue(total++);
			rowOne.createCell(1).setCellValue(bg.getTbOr1688());
			rowOne.createCell(2).setCellValue(bg.getOrderid());
			rowOne.createCell(3).setCellValue(bg.getOrderstatus());
			rowOne.createCell(4).setCellValue(bg.getItemname());
			rowOne.createCell(5).setCellValue(bg.getItemurl());
			rowOne.createCell(6).setCellValue(bg.getItemprice());
			rowOne.createCell(7).setCellValue(bg.getItemqty());
			rowOne.createCell(8).setCellValue(bg.getPreferential());
			rowOne.createCell(9).setCellValue(bg.getTotalprice());
			rowOne.createCell(10).setCellValue(bg.getSku());
			rowOne.createCell(11).setCellValue(bg.getOrderdate());
			rowOne.createCell(12).setCellValue(bg.getPaydata());
			rowOne.createCell(13).setCellValue(bg.getDelivery_date());
			rowOne.createCell(14).setCellValue(bg.getUsername());
		}
		listMap.clear();
		rowOne = sheetOne.createRow(rowsOne + 2);
		String stStr = buyReconciliationPojo.getBeginBlance() + "(月初余额)+"
				+ buyReconciliationPojo.getTransfer() + "(银行转账支付宝金额)-"
				+ buyReconciliationPojo.getEndBlance() + "(月末支付宝余额)-"
				+ buyReconciliationPojo.getEbayAmount() + "(亚马逊公司支付宝支出金额)-"
				+ buyReconciliationPojo.getMaterialsAmount() + "(电商物料)-"
				+ buyReconciliationPojo.getZfbFright() + "(支付宝支出运费)="
				+ buyReconciliationPojo.getGrabAmountsCopy() + "(计算抓取采购订单金额)";
		rowOne.createCell(1).setCellValue(stStr);
		rowOne = sheetOne.createRow(rowsOne + 3);
		rowOne.createCell(1).setCellValue("导出采购总金额:" + BigDecimalUtil.truncateDouble(totalBalance,2));

		// 订单明细数据
		HSSFSheet sheetTwo = wb.createSheet(sheetName);
		int rowsTwo = 0;  //记录行数
		HSSFRow rowTwo = sheetTwo.createRow(rowsTwo++);
		HSSFCellStyle styleTwo = wb.createCellStyle();
		styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowTwo = sheetTwo.createRow(rowsTwo++);  //到下一行添加数据
		for (int i = 0; i < excelTotal6.length; i++) {
			HSSFCell cell = rowTwo.createCell(i);
			cell.setCellValue(excelTotal6[i]);
			cell.setCellStyle(styleTwo);
		}
		//写入报表汇总
		for (int i = 0; i < list.size(); i++) {
			rowTwo = sheetTwo.createRow(rowsTwo++);
			TaoBaoOrderInfo bg = list.get(i);
			rowTwo.createCell(0).setCellValue((i + 1));
			rowTwo.createCell(1).setCellValue(bg.getTbOr1688());
			rowTwo.createCell(2).setCellValue(bg.getOrderid());
			rowTwo.createCell(3).setCellValue(bg.getOrderstatus());
			rowTwo.createCell(4).setCellValue(bg.getItemname());
			rowTwo.createCell(5).setCellValue(bg.getItemurl());
			rowTwo.createCell(6).setCellValue(bg.getItemprice());
			rowTwo.createCell(7).setCellValue(bg.getItemqty());
			rowTwo.createCell(8).setCellValue(bg.getPreferential());
			rowTwo.createCell(9).setCellValue(bg.getTotalprice());
			rowTwo.createCell(10).setCellValue(bg.getSku());
			rowTwo.createCell(11).setCellValue(bg.getOrderdate());
			rowTwo.createCell(12).setCellValue(bg.getPaydata());
			rowTwo.createCell(13).setCellValue(bg.getDelivery_date());
			rowTwo.createCell(14).setCellValue(bg.getUsername());
		}
		return wb;
	}
	/**
	 * 佳成面单信息导出
	 */
	@Override
	public HSSFWorkbook exportJcexPrintInfo(List<JcexPrintInfo> list) {
		String sheetName = "佳成面单信息";
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0;i < excelTotal11.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTotal11[i]);
			cell.setCellStyle(style);
		}
		//写入报表汇总
		for (int i = 0;i < list.size(); i++) {
			row = sheet.createRow(rows++);
			JcexPrintInfo bg = list.get(i);
			row.createCell(0).setCellValue((i+1));
			row.createCell(1).setCellValue(bg.getExpress_no());
			row.createCell(2).setCellValue(bg.getAdminname());
			row.createCell(3).setCellValue(bg.getAdmincompany());
			row.createCell(4).setCellValue(bg.getAdmincode());
			row.createCell(5).setCellValue(bg.getRecipients());
			row.createCell(6).setCellValue(bg.getAddress());
			row.createCell(7).setCellValue(bg.getZone());
			row.createCell(8).setCellValue(bg.getAddress2());
			row.createCell(9).setCellValue(bg.getStatename());
			row.createCell(10).setCellValue(bg.getZipcode());
			row.createCell(11).setCellValue(bg.getPhone());
			row.createCell(12).setCellValue(bg.getPayType());
			row.createCell(13).setCellValue(bg.getProductname());
			row.createCell(14).setCellValue(bg.getHscode());
			row.createCell(15).setCellValue(bg.getProductcurreny());
			row.createCell(16).setCellValue(bg.getPay_curreny());
			row.createCell(17).setCellValue(bg.getWeight());
			row.createCell(18).setCellValue(bg.getVolume_lwh());
			row.createCell(19).setCellValue(bg.getProducenglishtname());
			row.createCell(20).setCellValue(bg.getProductnum());
			row.createCell(21).setCellValue(bg.getProductprice());
			row.createCell(22).setCellValue(bg.getProductremark());
			row.createCell(23).setCellValue(bg.getUseridAndOrderid());
		}
		return wb;
	}
	/**
	 * 订单运费明细报表导出
	 * @param list
	 * @return
	 */
	@Override
	public HSSFWorkbook exportForecastAmount(List<ShippingPackage> list, List<Shipments> list2, String type) {
		String sheetName = ""; //报表页名
		if("1".equals(type)){
			sheetName = "预估订单运费明细报表";
		}else if("2".equals(type)){
			sheetName = "实际支付运费运单明细报表";
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		row = sheet.createRow(rows++);  //到下一行添加数据
		if("1".equals(type)){
			for (int i = 0;i < excelTotal7.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal7[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0;i < list.size(); i++) {
				row = sheet.createRow(rows++);
				ShippingPackage bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getShipmentno());
				row.createCell(2).setCellValue(bg.getOrderid());
				row.createCell(3).setCellValue(bg.getRemarks());
				row.createCell(4).setCellValue(bg.getCreatetime());
				row.createCell(5).setCellValue(bg.getSentTime());
				row.createCell(6).setCellValue(bg.getSweight());
				row.createCell(7).setCellValue(bg.getRealWeight());
				row.createCell(8).setCellValue(bg.getSvolume());
				row.createCell(9).setCellValue(bg.getVolumeweight());
				row.createCell(10).setCellValue(bg.getTransportcompany());
				row.createCell(11).setCellValue(bg.getShippingtype());
				row.createCell(12).setCellValue(bg.getCountry());
				row.createCell(13).setCellValue(bg.getExpressno());
				row.createCell(14).setCellValue(bg.getEstimatefreight());
				row.createCell(15).setCellValue(bg.getTotalPrice());
				row.createCell(16).setCellValue(bg.getReMark());
			}
		}else if("2".equals(type)){
			for (int i = 0;i < excelTotal8.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal8[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0;i < list2.size(); i++) {
				row = sheet.createRow(rows++);
				Shipments bg = list2.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getOrderno());
				row.createCell(2).setCellValue(bg.getSenttimes());
				row.createCell(3).setCellValue(bg.getCountry());
				row.createCell(4).setCellValue(bg.getTransportcompany());
				row.createCell(5).setCellValue(bg.getTransporttype());
				row.createCell(6).setCellValue(bg.getType());
				row.createCell(7).setCellValue(bg.getRealweight().toString());
				row.createCell(8).setCellValue(bg.getBulkweight());
				row.createCell(9).setCellValue(bg.getSettleweight().toString());
				row.createCell(10).setCellValue(bg.getTotalprice().toString());
			}
		}
		return wb;
	}
	/**
	 * 月采购对账报表结果导出excel
	 * @param list
	 * @return
	 */
	@Override
	public HSSFWorkbook exportBuyReconciliation(List<BuyReconciliationPojo> list) {
		String sheetName = "月采购对账报表"; //报表页名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTotal5.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTotal5[i]);
			cell.setCellStyle(style);
		}
		//写入报表汇总
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rows++);
			BuyReconciliationPojo bg = list.get(i);
			row.createCell(0).setCellValue((i+1));
			row.createCell(1).setCellValue(bg.getTimes());
			row.createCell(2).setCellValue(bg.getOrder_sales());//S
			row.createCell(3).setCellValue(bg.getBalance_compensation());//BC
			row.createCell(4).setCellValue(bg.getBeginBlance());//A
			row.createCell(5).setCellValue(bg.getEndBlance());//B
			row.createCell(6).setCellValue(bg.getTransfer());//C
			row.createCell(7).setCellValue(bg.getGrabAmount());//D1
			row.createCell(8).setCellValue(bg.getGrabAmounts());//D3
			row.createCell(9).setCellValue(bg.getZfbPayAmount());//D2
			row.createCell(10).setCellValue(bg.getNormalAmount());//E
			row.createCell(11).setCellValue(bg.getCancelAmount());//F
			row.createCell(12).setCellValue(bg.getNoMatchingOrder());//G
			row.createCell(13).setCellValue(bg.getNoStorage());//H
			row.createCell(14).setCellValue(bg.getLastMonth());//J
			row.createCell(15).setCellValue(bg.getInventory_amount());//I
			row.createCell(16).setCellValue(bg.getSale_inventory());//K
			row.createCell(17).setCellValue(bg.getForecastAmount());//W1
			row.createCell(18).setCellValue(bg.getActualAmount());//W2
			row.createCell(19).setCellValue(bg.getOrderFreight());//W3
			row.createCell(20).setCellValue(bg.getPayFreight());//W4
			row.createCell(21).setCellValue(bg.getProfit());//P
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportCategoryList(List<StatisticalReportPojo> list) {
		String sheetName = "分类销售统计报表"; //报表页名
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("分类销售信息统计");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTotal13.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTotal13[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				StatisticalReportPojo bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getEn_name());
				row.createCell(2).setCellValue(bg.getBuyAmount());
				row.createCell(3).setCellValue(bg.getSalesAmount());
				row.createCell(4).setCellValue(bg.getAvgSalesPrice());
				row.createCell(5).setCellValue(bg.getSalesCount());
				row.createCell(6).setCellValue(bg.getBuyCount());
				row.createCell(7).setCellValue(Integer.valueOf(bg.getProfitLoss())*100);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportLossInventoryExcel(List<LossInventoryPojo> list) {
		String sheetName = "库存损耗日志报表"; //报表页名
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("商品损耗删除信息统计");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota22.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota22[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//{ "序号","盘点前库存","盘点后库存","盘点前库位","盘点后库位","商品规格","损耗单价","损耗库存金额","损耗时间","损耗人","损耗原因"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				LossInventoryPojo bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getOld_remaining());
				row.createCell(2).setCellValue(bg.getNew_remaining());
				row.createCell(3).setCellValue(bg.getOld_barcode());
				row.createCell(4).setCellValue(bg.getNew_barcode());
				row.createCell(5).setCellValue(bg.getSku());
				row.createCell(6).setCellValue(bg.getLoss_price());
				row.createCell(7).setCellValue(bg.getLoss_amount());
				row.createCell(8).setCellValue(bg.getCreatetime());
				row.createCell(9).setCellValue(bg.getAdmName());
				row.createCell(10).setCellValue(bg.getRemark());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportDeleteInventoryExcel(List<Inventory> list) {
		String sheetName = "库存删除日志报表"; //报表页名
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("商品库存删除信息统计");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota21.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota21[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//{ "序号","商品名称","网站链接","商品库位","商品规格","采购价","首次库存数量","首次库存金额","盘点后库存数量","盘点后库存金额","可用库存数量","首次录入时间","最后更新库存时间","删除人","删除时间"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				Inventory bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getGood_name());
				row.createCell(2).setCellValue(bg.getGoods_url());
				row.createCell(3).setCellValue(bg.getBarcode());
				row.createCell(4).setCellValue(bg.getSku());
				row.createCell(5).setCellValue(bg.getGoods_p_price());
				row.createCell(6).setCellValue(bg.getRemaining());
				row.createCell(7).setCellValue(bg.getInventory_amount());
				row.createCell(8).setCellValue(bg.getNew_remaining());
				row.createCell(9).setCellValue(bg.getNew_inventory_amount());
				row.createCell(10).setCellValue(bg.getCan_remaining());
				row.createCell(11).setCellValue(bg.getCreatetime());
				row.createCell(12).setCellValue(bg.getUpdatetime());
				row.createCell(13).setCellValue(bg.getAdmName());
				row.createCell(14).setCellValue(bg.getCreate_time());
				row.createCell(15).setCellValue(bg.getDelRemark());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportUpdateInventoryExcel(List<Inventory> list) {
		String sheetName = "库存盘点日志报表"; //报表页名
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("商品库存盘点信息统计");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota20.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota20[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//{ "序号","商品名称","网站链接","采购链接","商品规格","盘点前数量","盘点后数量","盘点前库位","盘点后库位","盘点人","盘点时间","盘点备注"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				Inventory bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getGood_name());
				row.createCell(2).setCellValue(bg.getGoods_url());
				row.createCell(3).setCellValue(bg.getGoods_p_url());
				row.createCell(4).setCellValue(bg.getSku());
				row.createCell(5).setCellValue(bg.getOld_inventory());
				row.createCell(6).setCellValue(bg.getNew_inventroy());
				row.createCell(7).setCellValue(bg.getOld_barcode());
				row.createCell(8).setCellValue(bg.getNew_barcode());
				row.createCell(9).setCellValue(bg.getAdmName());
				row.createCell(10).setCellValue(bg.getCreatetime());
				row.createCell(11).setCellValue(bg.getRemark());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 报表导出类
	 * @param list
	 */
	@Override
	public HSSFWorkbook exportGoodsInventory(List<InventoryData> list) {
		String sheetName = "库存统计报表"; //报表页名
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("商品库存盘点信息统计");
		row = sheet.createRow(rows++);  //到下一行添加数据
//       String[] excelTotal4 = { "序号","商品名称","网站链接","库存库位","商品规格","商品货源","采购价","商品类别","库存数量","盘点后库存数量","库存金额","盘点后库存金额","商品图片"};
		for (int i = 0; i < excelTotal4.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTotal4[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				InventoryData bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
//                row.createCell(1).setCellValue(bg.getGoods_pid());
				row.createCell(1).setCellValue(bg.getGoodsName());
				row.createCell(2).setCellValue(bg.getGoodsUrl());
				row.createCell(3).setCellValue(bg.getBarcode());
				row.createCell(4).setCellValue(bg.getSku());
				row.createCell(5).setCellValue(bg.getGoodsPUrl());
				row.createCell(6).setCellValue(bg.getGoodsPPrice());//采购价
//                row.createCell(7).setCellValue(StringUtils.isStrNull(bg.getGoodsprice())?"-":("$"+bg.getGoodsprice()));
				row.createCell(7).setCellValue(bg.getGoodsCatid());//商品类别
				row.createCell(8).setCellValue(bg.getRemaining());
//				row.createCell(9).setCellValue(bg.getNew_remaining());
//				row.createCell(10).setCellValue(bg.getInventory_amount());//库存金额
//				row.createCell(11).setCellValue(bg.getNew_inventory_amount());//盘点后库存金额
				row.createCell(12).setCellValue(bg.getCreatetime());//首次盘点时间
				row.createCell(13).setCellValue(bg.getUpdatetime());//最后盘点时间
				//是否上架
				row.createCell(14).setCellValue(bg.getOnline());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}

	public static void main(String[] args) {
//		FileOutputStream fileOut = null;
//		BufferedImage bufferImg = null;
//		try {
//			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//			HSSFWorkbook wb = new HSSFWorkbook();
//			HSSFSheet sheet1 = wb.createSheet("test picture");
//			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
//			URL url = new URL("");
//			URLConnection con = url.openConnection();
//			con.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//			con.setConnectTimeout(2000);
//			con.setReadTimeout(2000);
//			InputStream is = con.getInputStream();
//			bufferImg=ImageIO.read(is);
//			ImageIO.write(bufferImg, "jpg", byteArrayOut);
//			HSSFClientAnchor anchor = new HSSFClientAnchor(1, 1, 1023, 255,(short) 14, 1, (short) 14,1);
//			anchor.setAnchorType(0);
//			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
//			fileOut = new FileOutputStream("D:/测试Excel.xls");
//			// 写入excel文件
//			wb.write(fileOut);
//			System.out.println("----Excle文件已生成------");
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(fileOut != null){
//				try {
//					fileOut.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	/**
	 * 报表导出类
	 * @param list
	 */
	@Override
	public HSSFWorkbook export1(List<ExpbuyGoods> list) {
		//List<ReportInfo> reportInfoList = statisticalReportVo.getReportInfoList();
		//List<ReportDetail> reportDetailList = statisticalReportVo.getReportDetailList();
		String sheetName = "商品报表"; //报表页名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("报表详细：");
		row = sheet.createRow(rows++);  //到下一行添加数据
		if(1==1){
			for (int i = 0; i < excelTotal2.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelTotal2[i]);
				cell.setCellStyle(style);
			}
			//写入报表汇总
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				ExpbuyGoods bg = list.get(i);
				row.createCell(0).setCellValue(bg.getId());
				row.createCell(1).setCellValue(bg.getCategory());
				row.createCell(2).setCellValue(bg.getName());
				row.createCell(3).setCellValue(bg.getSaleNums());
				row.createCell(4).setCellValue(bg.getPrice());
				row.createCell(5).setCellValue(bg.getCount());
				row.createCell(6).setCellValue(bg.getBuycount());
				row.createCell(7).setCellValue(bg.getBuyprices());
				row.createCell(8).setCellValue(bg.getBuysum());
			}
		}
		return wb;
	}

	public int selectExistExport(GeneralReport generalReport){
		return generalReportDao.selectExistExport(generalReport);
	}

	@Override
	public int insertOrderReport(Map<String, String> map) {
		return generalReportDao.insertOrderReport(map);
	}

	public int deleteReportDetailOrder(GeneralReport generalReport){
		return generalReportDao.deleteReportDetailOrder(generalReport);
	}

	/**
	 * 导出 销售报表
	 */
	public HSSFWorkbook exportSalesReport(List<SalesReport> list, ReportSalesInfo info, String sheetName){
		String[] heji = {"用户数量","本月消费合计","本月订单数量","历史总消费","历史订单数量","新用户数量","老用户数量"};
		String[] table = {"序号","用户id","管理员","本月消费","本月首单支付时间","本月订单数量","消费总金额","总下单数量","首单支付时间","上月最后订单支付时间","新用户标识"};
		sheetName += "销售统计报表"; //报表页名
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		//写入报表汇总
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("报表合计：");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < heji.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(heji[i]);
			cell.setCellStyle(style);
		}
		row = sheet.createRow(rows++);
		row.createCell(0).setCellValue(info.getUserCount());
		row.createCell(1).setCellValue(info.getCost());
		row.createCell(2).setCellValue(info.getOrderCount());
		row.createCell(3).setCellValue(info.getAllCost());
		row.createCell(4).setCellValue(info.getAllOrder());
		row.createCell(5).setCellValue(info.getNewUser());
		row.createCell(6).setCellValue(info.getOldUser());

		row = sheet.createRow(rows++);
		//写入报表详细
		HSSFCell hcell2 = row.createCell(0); //添加标题
		hcell2.setCellValue("报表详细：");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < table.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(table[i]);
			cell.setCellStyle(style);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rows++);
			SalesReport sale = list.get(i);
			row.createCell(0).setCellValue(i+1);
			row.createCell(1).setCellValue(sale.getUser_id());
			row.createCell(2).setCellValue(sale.getAdmName());
			row.createCell(3).setCellValue(sale.getPayprice());
			row.createCell(4).setCellValue(sale.getOrderpaytime());
			row.createCell(5).setCellValue(sale.getSl());
			row.createCell(6).setCellValue(sale.getZje());
			row.createCell(7).setCellValue(sale.getZsl());
			row.createCell(8).setCellValue(sale.getFirstbuy());
			row.createCell(9).setCellValue(sale.getScgm());
			row.createCell(10).setCellValue(sale.getNewsign());
		}
		return wb;
	}
	@Override
	public HSSFWorkbook exportInventoryCheckExcel(List<InventoryCheckRecord> list) {
		String sheetName = "库存盘点日志报表"; //报表页名
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("商品盘点信息统计");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota24.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota24[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//{"序号","商品ID","产品名","skuid","specid","sku","盘点前库存","盘点后库存","差异值","库位","时间"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				InventoryCheckRecord bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getGoodsPid());
				row.createCell(2).setCellValue(bg.getGoodsName());
				row.createCell(3).setCellValue(bg.getGoodsSkuid());
				row.createCell(4).setCellValue(bg.getGoodsSpecid());
				row.createCell(5).setCellValue(bg.getGoodsSku());
				row.createCell(6).setCellValue(bg.getInventoryRemaining());
				row.createCell(7).setCellValue(bg.getCheckRemaining());
				row.createCell(8).setCellValue(Math.abs(bg.getCheckRemaining() - bg.getInventoryRemaining() ));
				row.createCell(9).setCellValue(bg.getAfterBarcode());
				row.createCell(10).setCellValue(bg.getCreateTime());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}
	@Override
	public HSSFWorkbook exportInventoryExcel(List<InventoryCheckWrap> list) {
		String sheetName = "库存盘点报表"; //报表页名
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("库存盘点列表");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota25.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota25[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//{"序号","类别","商品ID","商品名称","商品sku","商品图片","上次盘点数量","库存数量","库位","盘点数量"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				InventoryCheckWrap bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getCategoryName());
				row.createCell(2).setCellValue(bg.getGoodsPid());
				row.createCell(3).setCellValue(bg.getGoodsSkuid());
				row.createCell(4).setCellValue(bg.getGoodsName());
				row.createCell(5).setCellValue(bg.getGoodsSku()+"\n"+bg.getGoodsSpecid()+"\n"+bg.getGoodsSkuid());
				row.createCell(6).setCellValue(bg.getGoodsImg());
				row.createCell(7).setCellValue(bg.getLastCheckRemaining());
				row.createCell(8).setCellValue(bg.getRemaining());
				row.createCell(9).setCellValue(bg.getBarcode());
				row.createCell(10).setCellValue("");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}
	@Override
	public HSSFWorkbook exportInventory(List<InventoryCheckWrap> list) {
		String sheetName = "库存报表"; //报表页名
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("库存列表");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota26.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota26[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//String[] excelTota26 = { "序号","时间","odid","商品ID","商品名称","商品sku","库存数量","库位","盘点数量"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				InventoryCheckWrap bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getTime());
				row.createCell(2).setCellValue(bg.getOdid());
				row.createCell(3).setCellValue(bg.getGoodsPid());
				row.createCell(4).setCellValue(bg.getGoodsSkuid());
				row.createCell(5).setCellValue(bg.getGoodsName());
				row.createCell(6).setCellValue(bg.getGoodsSku()+"\n"+bg.getGoodsSpecid()+"\n"+bg.getGoodsSkuid());
				row.createCell(7).setCellValue(bg.getRemaining());
				row.createCell(8).setCellValue(bg.getBarcode());
				row.createCell(9).setCellValue(bg.getGoodsPrice());
				row.createCell(10).setCellValue("");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}
	@Override
	public HSSFWorkbook exportInventoryLog(List<InventoryLog> list) {
		String sheetName = "库存报表"; //报表页名
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("库存列表");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota28.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota28[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//{ "序号","时间","商品ID","产品名称","产品Sku","变更前库存","变更数量","变更后库存","变更类型","备注"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				InventoryLog bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getCreatetime());
				row.createCell(2).setCellValue(bg.getGoodsPid());
				row.createCell(3).setCellValue(bg.getSkuid());
				row.createCell(4).setCellValue(bg.getGoodsName());
				row.createCell(5).setCellValue(bg.getSku()+"\n"+"\n"+bg.getSpecid());
				row.createCell(6).setCellValue(bg.getBeforeRemaining());
				/*if(bg.getChangeType() == 3) {
					row.createCell(7).setCellValue(
							bg.getAfterRemaining()>bg.getBeforeRemaining() ? 
							""+bg.getRemaining(): "-" +bg.getRemaining());
				}else {
				}*/
				row.createCell(7).setCellValue(bg.getRemaining());
				row.createCell(8).setCellValue(bg.getAfterRemaining());
				if(bg.getChangeType() == 1) {
					row.createCell(9).setCellValue("入库");
				}else if(bg.getChangeType() == 2) {
					row.createCell(9).setCellValue("出库");
				}else if(bg.getChangeType() == 3) {
					row.createCell(9).setCellValue("盘点");
				}else if(bg.getChangeType() == 4) {
					row.createCell(9).setCellValue("占用");
				}else if(bg.getChangeType() == 5) {
					row.createCell(9).setCellValue("取消占用");
				}
				row.createCell(10).setCellValue(bg.getRemark());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}
	@Override
	public HSSFWorkbook exportInventoryLoss(List<LossInventoryWrap> list){
		String sheetName = "库存报损报表"; //报表页名
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		int rows =0;  //记录行数
		HSSFRow row = sheet.createRow(rows++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell hcell = row.createCell(0); //添加标题
		hcell.setCellValue("库存报损列表");
		row = sheet.createRow(rows++);  //到下一行添加数据
		for (int i = 0; i < excelTota27.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelTota27[i]);
			cell.setCellStyle(style);
		}
		try{
			//写入报表汇总
			//String[] excelTota26 = { "序号","时间","商品ID","skuid","类型","数量","备注"};
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(rows++);
				LossInventoryWrap bg = list.get(i);
				row.createCell(0).setCellValue((i+1));
				row.createCell(1).setCellValue(bg.getChangeTime());
				row.createCell(2).setCellValue(bg.getGoodsPid());
				row.createCell(3).setCellValue(bg.getSkuid());
				int type = bg.getChangeType();
				String content = type==1?"遗失":type==3?"添加":type==4?"补货":type==5?"漏发":type==7?"其他原因":type==8?"送样":"损坏";
				row.createCell(4).setCellValue(type+"("+content+")");
				row.createCell(5).setCellValue(bg.getChangeNumber());
				row.createCell(6).setCellValue(bg.getRemark());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
	}
}