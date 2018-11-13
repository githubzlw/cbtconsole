package com.cbt.warehouse.util;

import com.cbt.warehouse.pojo.Shipment;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadExcel {
	public static List<Shipment> readDYExcel(String filePath, String uuid, String company){
		List<Shipment> list = new ArrayList<Shipment>();
		try{
			File readfile = new File(filePath);
			Workbook book = Workbook.getWorkbook(readfile);
			Sheet sheet1 = book.getSheet(0);
			for (int j = 1; j < sheet1.getRows(); j++) {
				Shipment s=new Shipment();
				String time=sheet1.getCell(1, j).getContents();//日期
				if(StringUtil.isBlank(time)){
					break;
				}
				if(!time.contains("20")){
					time="20"+time;
				}
				time=time.replace("/", "-");
            	System.out.println("时间="+time);
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            	Date sentDate = sdf.parse(time);
				String transfer_number=sheet1.getCell(4, j).getContents();//转单号
				String weight=sheet1.getCell(5, j).getContents();//重量
				double chargeable_weight=Double.valueOf(StringUtil.isBlank(sheet1.getCell(6, j).getContents())?"0":sheet1.getCell(6, j).getContents());//计费重量
				String destination=sheet1.getCell(7, j).getContents();//目的地
				String channel=sheet1.getCell(8, j).getContents();//渠道
				String totalprice=sheet1.getCell(12, j).getContents();//物流公司
				s.setSenttime(sentDate);
				s.setCountry(destination);
				s.setOrderno(transfer_number);
				s.setTransportcompany(company);
				s.setTransporttype(channel);
				s.setNumbers(1);
				s.setType("包裹");
				BigDecimal bd=new BigDecimal(chargeable_weight);
				s.setRealweight(String.valueOf(chargeable_weight));//计费重量
				s.setSettleweight(String.valueOf(chargeable_weight));
				s.setBulkweight(weight);
				bd=new BigDecimal(totalprice);
				s.setCharge(String.valueOf(totalprice));
				s.setTotalprice(String.valueOf(totalprice));
				s.setUuid(uuid);
				s.setCreatetime(new Date());
				list.add(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 衡欣物流公司运费上传
	 * @param filePath
	 * @param uuid
	 * @param company
	 * @return
	 */
	public static List<Shipment> readHXExcel(String filePath, String uuid, String company){
		List<Shipment> list = new ArrayList<Shipment>();
		try{
			File readfile = new File(filePath);
			Workbook book = Workbook.getWorkbook(readfile);
			Sheet sheet1 = book.getSheet(0);
			for (int j = 9; j < sheet1.getRows(); j++) {
				Shipment s=new Shipment();
				String time=sheet1.getCell(0, j).getContents();//日期
				if(StringUtil.isBlank(time)){
					break;
				}
				if(!time.contains("20")){
					time="20"+time;
				}
				time=time.replace("/", "-");
				System.out.println("时间="+time);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date sentDate = sdf.parse(time);
				String transfer_number=sheet1.getCell(4, j).getContents();//转单号
				String weight=sheet1.getCell(6, j).getContents();//重量
				double chargeable_weight=Double.valueOf(StringUtil.isBlank(weight)?"0":weight);//计费重量
				String destination=sheet1.getCell(1, j).getContents();//目的地
				String number=sheet1.getCell(5, j).getContents().trim();//件数
				String type=sheet1.getCell(2, j).getContents();//
				String totalprice=sheet1.getCell(8, j).getContents();
				s.setSenttime(sentDate);
				s.setCountry(destination);
				s.setOrderno(transfer_number);
				s.setTransportcompany(company);
				s.setTransporttype("衡欣");
				s.setNumbers(Integer.valueOf(number));
				s.setType(type);
				BigDecimal bd=new BigDecimal(chargeable_weight);
				s.setRealweight(String.valueOf(chargeable_weight));//计费重量
				s.setSettleweight(String.valueOf(chargeable_weight));
				s.setBulkweight(weight);
				bd=new BigDecimal(totalprice);
				s.setCharge(String.valueOf(totalprice));
				s.setTotalprice(String.valueOf(totalprice));
				s.setUuid(uuid);
				s.setCreatetime(new Date());
				list.add(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static void main(String[] args) {
		readDYExcel("D:/大于国际.xls","111111","大誉国际");
	}

}
