package com.cbt.warehouse.util;

import com.cbt.util.WebTool;
import com.cbt.warehouse.pojo.Mabangshipment;
import com.cbt.warehouse.pojo.Shipment;
import com.cbt.warehouse.pojo.Skuinfo;
import com.importExpress.pojo.AliBillingDetails;
import com.importExpress.pojo.AliPayInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import org.springframework.web.util.NestedServletException;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {

	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_XLSX = "xlsx";


	/**
	 * 文件检查
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws FileFormatException
	 * @throws Exception
	 */
	private static void fileCheck(String filePath) throws FileNotFoundException, FileFormatException{
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("传入的文件不存在：" + filePath);
		}
		if (!(filePath.endsWith(EXTENSION_XLS) || filePath.endsWith(EXTENSION_XLSX))) {
			throw new FileFormatException("传入的文件不是excel");
		}
	}


	/**
	 * 读取佳成运单表格
	 * @param filePath
	 * @param uuid
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws IllegalArgumentException
	 * @throws NestedServletException
	 * @throws Exception
	 */
	public static List<Shipment> readJCExcel(String filePath, String uuid) throws EncryptedDocumentException, InvalidFormatException, IOException, NestedServletException, IllegalArgumentException{
		List<Shipment> list = new ArrayList<Shipment>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		workbook = getWorkbook(filePath);
		try {
			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				//System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer sentTimeIndex = null;
				Integer orderNoIndex = null;
				Integer countryIndex = null;
				Integer typeIndex = null;
				Integer weightIndex = null;
				Integer numbersIndex = null;
				Integer chargeIndex = null;
				Integer fuelSurchargeIndex = null;
				Integer securityCostsIndex = null;
				Integer reMarkIndex = null;
				Integer taxsIndex = null;
				Integer totalPriceIndex = null;
				for (int i = firstRowIndex; i <= lastRowIndex; i++) {
					Row currentRow = sheet.getRow(i);// 当前行
					for (int j = 0; j < currentRow.getLastCellNum(); j++) {
						String cellValue = getCellValue(currentRow.getCell(j), true);
						if ("DATE\n发件日期".equals(cellValue)) {
							sentTimeIndex = j;
						}else if ("CONS\n运单号".equals(cellValue)) {
							orderNoIndex = j;
						}else if ("DEST\n收件地".equals(cellValue)) {
							countryIndex = j;
						}else if ("PACKAGING".equals(cellValue)) {
							typeIndex = j;
						}else if ("WT\n重量(kg)".equals(cellValue)) {
							weightIndex = j;
						}else if ("PCS\n件数".equals(cellValue)) {
							numbersIndex = j;
						}else if ("NET CHARGE".equals(cellValue)) {
							chargeIndex = j;
						}else if ("PUEL SURCHARGE".equals(cellValue)) {
							fuelSurchargeIndex = j;
						}else if ("安检费".equals(cellValue)) {
							securityCostsIndex = j;
						}else if ("OTHER\n其他".equals(cellValue)) {
							reMarkIndex = j;
						}else if ("税金".equals(cellValue)) {
							taxsIndex = j;
						}else if ("Total\n小计".equals(cellValue)) {
							totalPriceIndex = j;
						}
					}
					if (sentTimeIndex != null) {
						break;
					}
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// 读取数据行
				for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						Shipment shipMent = new Shipment();
						String date = getCellValue(currentRow.getCell(sentTimeIndex), true);
						try {
							if (WebTool.isEmpty(date) || !(sdf.parse(date) instanceof Date)) {
								continue;
							}
						} catch (Exception e) {
							continue;
						}
						shipMent.setSenttime(sdf.parse(date));
						//System.out.println(getCellValue(currentRow.getCell(1), true));
						shipMent.setOrderno(getCellValue(currentRow.getCell(orderNoIndex), true));
						if (countryIndex != null) {
							if (!WebTool.isEmpty(getCellValue(currentRow.getCell(countryIndex), true))) {
								shipMent.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
							}
						}
						if (typeIndex != null) {
							if (!WebTool.isEmpty(getCellValue(currentRow.getCell(typeIndex), true))) {
								shipMent.setType(getCellValue(currentRow.getCell(typeIndex), true));
							}
						}
						if (weightIndex != null) {
							String weight = getCellValue(currentRow.getCell(weightIndex), true).trim();
							shipMent.setSettleweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setRealweight(WebTool.isEmpty(weight) == true?"0.00" :weight);
							shipMent.setBulkweight(WebTool.isEmpty(weight) == true? new String("0.00") :new String(weight));
						}
						if (numbersIndex != null) {
							String numbers = getCellValue(currentRow.getCell(numbersIndex), true);
							shipMent.setNumbers(WebTool.isEmpty(numbers) == true ? 0 : Integer.parseInt(numbers));
						}
						if (chargeIndex != null) {
							String charge = getCellValue(currentRow.getCell(chargeIndex), true).trim();
							shipMent.setCharge(WebTool.isEmpty(charge) == true? "0.00" :charge);
						}
						if (fuelSurchargeIndex != null) {
							String addfuelTax = getCellValue(currentRow.getCell(fuelSurchargeIndex), true).trim();
							shipMent.setFuelsurcharge(WebTool.isEmpty(addfuelTax) == true?"0.00":addfuelTax);
						}
						if (securityCostsIndex != null) {
							String securityCosts = getCellValue(currentRow.getCell(securityCostsIndex), true).trim();
							shipMent.setSecuritycosts(WebTool.isEmpty(securityCosts) == true?"0.00":securityCosts);
						}
						if (reMarkIndex != null) {
							String other = getCellValue(currentRow.getCell(reMarkIndex), true).trim();
							shipMent.setRemark(other);
						}
						if (taxsIndex != null) {
							String taxs = getCellValue(currentRow.getCell(taxsIndex), true).trim();
							shipMent.setTaxs(WebTool.isEmpty(taxs) == true?"0.00":taxs);
						}
						if (totalPriceIndex != null) {
							String price = getCellValue(currentRow.getCell(totalPriceIndex), true).trim();
							shipMent.setTotalprice(WebTool.isEmpty(price) == true?"0.00":price);
						}
						shipMent.setTransportcompany("JCEX");//佳成
						shipMent.setCreatetime(new Date());
						shipMent.setUuid(uuid);
						if (list.contains(shipMent)) {
							int index = list.indexOf(shipMent);
							Shipment oldShipment = list.get(index);
							//金额累加
							oldShipment.setTotalprice(String.valueOf(Double.parseDouble(oldShipment.getTotalprice().toString())+Double.parseDouble(shipMent.getTotalprice().toString())));
							oldShipment.setRemark(oldShipment.getRemark()+"-"+shipMent.getRemark());
							list.set(index, oldShipment);//替换掉原始的
						}else {
							list.add(shipMent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				System.out.println("==========================excel解析完成============================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}


	/**
	 * 读取邮政运单excel的内容
	 * @param filePath
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws IllegalArgumentException
	 * @throws NestedServletException
	 * @throws Exception
	 */
	public static List<Shipment> readYZExcel(String filePath, String uuid) throws EncryptedDocumentException, InvalidFormatException, IOException, NestedServletException, IllegalArgumentException{
		List<Shipment> list = new ArrayList<Shipment>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		workbook = getWorkbook(filePath);
		try {
			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				//System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer orderNoIndex = null;
				Integer switchNoIndex = null;
				Integer netIndex = null;
				Integer sentTimeIndex = null;
				Integer countryIndex = null;
				Integer addresseeIndex = null;
				Integer numbersIndex = null;
				Integer weightIndex = null;
				Integer priceIndex = null;
				Integer typeIndex = null;
				Integer remarkIndex = null;
				// 读取首行:表头(如果行大于1则表示当前sheet有数据)
				if (lastRowIndex>=1) {
					Row firstRow = sheet.getRow(firstRowIndex);
					for (int i = firstRow.getFirstCellNum(); i <= firstRow.getLastCellNum(); i++) {
						Cell cell = firstRow.getCell(i);
						String cellValue = getCellValue(cell, true);
						//System.out.print(" " + cellValue + "\t");
						if ("运单号".equals(cellValue)) {
							orderNoIndex = i;
						}else if ("转单号".equals(cellValue)) {
							switchNoIndex = i;
						}else if ("快递类别".equals(cellValue)) {
							netIndex = i;
						}else if ("发件日期".equals(cellValue)) {
							sentTimeIndex = i;
						}else if ("目的地".equals(cellValue)) {
							countryIndex = i;
						}else if ("收件人".equals(cellValue)) {
							addresseeIndex = i;
						}else if ("件数".equals(cellValue)) {
							numbersIndex = i;
						}else if ("重量".equals(cellValue)) {
							weightIndex = i;
						}else if ("费用".equals(cellValue)) {
							priceIndex = i;
						}else if ("类型".equals(cellValue)) {
							typeIndex = i;
						}else if ("备注".equals(cellValue)) {
							remarkIndex = i;
						}
					}
					//System.out.println("");
				}

				// 读取数据行
				Shipment shipMent = null;
				for (int rowIndex = 1; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						shipMent = new Shipment();
						String orderNo = getCellValue(currentRow.getCell(orderNoIndex), true);
						if (WebTool.isEmpty(orderNo)) {
							continue;
						}
						shipMent.setOrderno(orderNo);
						if (switchNoIndex != null) {
							shipMent.setSwitchno(getCellValue(currentRow.getCell(switchNoIndex), true));
						}
						shipMent.setTransportcompany("emsinten");//邮政
						if (netIndex != null) {
							shipMent.setTransporttype(getCellValue(currentRow.getCell(netIndex), true));
						}
						Date javaDate = HSSFDateUtil.getJavaDate(currentRow.getCell(sentTimeIndex).getNumericCellValue());
						shipMent.setSenttime(javaDate);
						if (countryIndex != null) {
							shipMent.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
						}
						if (addresseeIndex != null) {
							shipMent.setAddressee(getCellValue(currentRow.getCell(addresseeIndex), true));
						}
						if (numbersIndex != null) {
							String numbers = getCellValue(currentRow.getCell(numbersIndex), true);
							shipMent.setNumbers(WebTool.isEmpty(numbers) == true ? 0 : Integer.parseInt(numbers));
						}
						if (weightIndex != null) {
							String weight = getCellValue(currentRow.getCell(weightIndex), true).trim();
							shipMent.setRealweight(WebTool.isEmpty(weight)==true ? "0.00" : weight);
							shipMent.setSettleweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setBulkweight(WebTool.isEmpty(weight) == true? new String("0.00") :new String(weight));
						}
						if (priceIndex != null) {
							String price = getCellValue(currentRow.getCell(priceIndex), true).trim();
							shipMent.setCharge(WebTool.isEmpty(price) == true? "0.00" :price);
							shipMent.setTotalprice(WebTool.isEmpty(price) == true? "0.00" :price);
						}
						if (typeIndex != null) {
							shipMent.setType(getCellValue(currentRow.getCell(typeIndex), true));
						}
						if (remarkIndex != null) {
							shipMent.setRemark(getCellValue(currentRow.getCell(remarkIndex), true));
						}
						shipMent.setCreatetime(new Date());
						shipMent.setUuid(uuid);
						if (list.contains(shipMent)) {
							int index = list.indexOf(shipMent);
							Shipment oldShipment = list.get(index);
							//金额累加
							oldShipment.setTotalprice(String.valueOf(Double.parseDouble(oldShipment.getTotalprice().toString())+Double.parseDouble(shipMent.getTotalprice().toString())));
							oldShipment.setRemark(oldShipment.getRemark()+"-"+shipMent.getRemark());
							list.set(index, oldShipment);//替换掉原始的
						}else {
							list.add(shipMent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				System.out.println("==========================excel解析完成============================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}


	/**
	 * 读取原飞航运单excel的内容
	 * @param filePath
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws IllegalArgumentException
	 * @throws NestedServletException
	 * @throws Exception
	 */
	public static List<Shipment> readYFHExcel(String filePath, String uuid) throws EncryptedDocumentException, InvalidFormatException, IOException, NestedServletException, IllegalArgumentException {
		List<Shipment> list = new ArrayList<Shipment>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		workbook = getWorkbook(filePath);
		try {
			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				//System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer sentTimeIndex = null;
				Integer orderNoIndex = null;
				Integer addresseeIndex = null;
				Integer countryIndex = null;
				Integer netIndex = null;
				Integer numbersIndex = null;
				Integer packageTypeIndex = null;
				Integer realWeightIndex = null;
				Integer bulkWeightIndex = null;
				Integer settleWeightIndex = null;
				Integer shipmentIndex = null;
				Integer fuelTaxIndex = null;
				Integer securityCostsIndex = null;
				Integer taxsIndex = null;
				Integer totalPriceIndex = null;
				Integer remarkIndex = null;

				// 读取首行:表头(如果行大于1则表示当前sheet有数据)
				if (lastRowIndex>=1) {
					Row firstRow = sheet.getRow(firstRowIndex);
					for (int i = firstRow.getFirstCellNum(); i <= firstRow.getLastCellNum(); i++) {
						Cell cell = firstRow.getCell(i);
						String cellValue = getCellValue(cell, true);
						//System.out.print(" " + cellValue + "\t");
						if ("sentTime".equals(cellValue)) {
							sentTimeIndex = i;
						}else if ("orderNo".equals(cellValue)) {
							orderNoIndex = i;
						}else if ("addressee".equals(cellValue)) {
							addresseeIndex = i;
						}else if ("country".equals(cellValue)) {
							countryIndex = i;
						}else if ("net".equals(cellValue)) {
							netIndex = i;
						}else if ("numbers".equals(cellValue)) {
							numbersIndex = i;
						}else if ("packageType".equals(cellValue)) {
							packageTypeIndex = i;
						}else if ("realWeight".equals(cellValue)) {
							realWeightIndex = i;
						}else if ("bulkWeight".equals(cellValue)) {
							bulkWeightIndex = i;
						}else if ("settleWeight".equals(cellValue)) {
							settleWeightIndex = i;
						}else if ("shipment".equals(cellValue)) {
							shipmentIndex = i;
						}else if ("fuelTax".equals(cellValue)) {
							fuelTaxIndex = i;
						}else if ("securityCosts".equals(cellValue)) {
							securityCostsIndex = i;
						}else if ("taxs".equals(cellValue)) {
							taxsIndex = i;
						}else if ("totalPrice".equals(cellValue)) {
							totalPriceIndex = i;
						}else if ("reMark".equals(cellValue)) {
							remarkIndex = i;
						}
					}
					//System.out.println("");
				}

				// 读取数据行
				Shipment shipMent = null;
				for (int rowIndex = 2; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						shipMent = new Shipment();
						String orderNo = getCellValue(currentRow.getCell(orderNoIndex), true);
						if (WebTool.isEmpty(orderNo)) {
							continue;
						}
						shipMent.setOrderno(orderNo);
						if (addresseeIndex != null) {
							shipMent.setAddressee(getCellValue(currentRow.getCell(addresseeIndex), true));
						}
						if (countryIndex != null) {
							shipMent.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
						}
						shipMent.setTransportcompany("原飞航");
						if (netIndex != null) {
							shipMent.setTransporttype(getCellValue(currentRow.getCell(netIndex), true));
						}
						if (numbersIndex != null) {
							String numbers = getCellValue(currentRow.getCell(numbersIndex), true);
							shipMent.setNumbers(WebTool.isEmpty(numbers) == true ? 0 : Integer.parseInt(numbers));
						}
						if (packageTypeIndex != null) {
							shipMent.setType(getCellValue(currentRow.getCell(packageTypeIndex), true));
						}
						try {
							Date javaDate = HSSFDateUtil.getJavaDate(currentRow.getCell(sentTimeIndex).getNumericCellValue());
							shipMent.setSenttime(javaDate);
						} catch (Exception e) {
//							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String date = getCellValue(currentRow.getCell(sentTimeIndex), true).trim();
							Date sentDate = sdf.parse(date);
							shipMent.setSenttime(sentDate);
						}
						if (realWeightIndex != null) {
							String realWeight = getCellValue(currentRow.getCell(realWeightIndex), true).trim();
							shipMent.setRealweight(WebTool.isEmpty(realWeight)==true ? "0.00": realWeight);
						}
						if (bulkWeightIndex != null) {
							String bulkWeight = getCellValue(currentRow.getCell(bulkWeightIndex), true).trim();
							shipMent.setBulkweight(WebTool.isEmpty(bulkWeight)==true ? new String("0.00") : new String(bulkWeight));
						}
						if (settleWeightIndex != null) {
							String settleWeight = getCellValue(currentRow.getCell(settleWeightIndex), true).trim();
							shipMent.setSettleweight(WebTool.isEmpty(settleWeight)==true ? "0.00": settleWeight);
						}
						if (shipmentIndex != null) {
							String shipment = getCellValue(currentRow.getCell(shipmentIndex), true).trim();
							shipMent.setCharge(WebTool.isEmpty(shipment) == true? "0.00":shipment);
						}
						if (fuelTaxIndex != null) {
							String fuelTax = getCellValue(currentRow.getCell(fuelTaxIndex), true).trim();
							shipMent.setFuelsurcharge(WebTool.isEmpty(fuelTax) == true? "0.00":fuelTax);
						}
						if (securityCostsIndex != null) {
							String securityCosts = getCellValue(currentRow.getCell(securityCostsIndex), true).trim();
							shipMent.setSecuritycosts(WebTool.isEmpty(securityCosts) == true?"0.00":securityCosts);
						}
						if (taxsIndex != null) {
							String taxs = getCellValue(currentRow.getCell(taxsIndex), true).trim();
							shipMent.setTaxs(WebTool.isEmpty(taxs) == true?"0.00":taxs);
						}
						if (totalPriceIndex != null) {
							String totalPrice = getCellValue(currentRow.getCell(totalPriceIndex), true).trim();
							shipMent.setTotalprice(WebTool.isEmpty(totalPrice) == true?"0.00":totalPrice);
						}
						if (remarkIndex != null) {
							shipMent.setRemark(getCellValue(currentRow.getCell(remarkIndex), true));
						}
						shipMent.setCreatetime(new Date());
						shipMent.setUuid(uuid);
						if (list.contains(shipMent)) {
							int index = list.indexOf(shipMent);
							Shipment oldShipment = list.get(index);
							//金额累加
							oldShipment.setTotalprice(String.valueOf(Double.parseDouble(oldShipment.getTotalprice().toString())+Double.parseDouble(shipMent.getTotalprice().toString())));
							oldShipment.setRemark(oldShipment.getRemark()+"-"+shipMent.getRemark());
							list.set(index, oldShipment);//替换掉原始的
						}else {
							list.add(shipMent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				System.out.println("==========================excel解析完成============================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}


	/**
	 * 读取顺丰运单表格
	 * @param filePath
	 * @param uuid
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws IllegalArgumentException
	 * @throws NestedServletException
	 * @throws Exception
	 */
	public static List<Shipment> readSFExcel(String filePath, String uuid) throws EncryptedDocumentException, InvalidFormatException, IOException, NestedServletException, IllegalArgumentException{
		List<Shipment> list = new ArrayList<Shipment>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		workbook = getWorkbook(filePath);
		try {
			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				//System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer addresseeIndex = null;
				Integer countryIndex = null;
				Integer numbersIndex = null;
				Integer typeIndex = null;
				Integer weightIndex = null;
				Integer switchNoIndex = null;
				Integer transportTypeIndex = null;
				Integer orderNoIndex = null;
				Integer sentTimeIndex = null;
				Integer reMarkIndex = null;
				Integer totalPriceIndex = null;
				for (int i = firstRowIndex; i <= lastRowIndex; i++) {
					Row currentRow = sheet.getRow(i);// 当前行
					for (int j = 0; j < currentRow.getLastCellNum(); j++) {
						String cellValue = getCellValue(currentRow.getCell(j), true);
						if ("收件人".equals(cellValue)) {
							addresseeIndex = j;
						}else if ("收件国家".equals(cellValue)) {
							countryIndex = j;
						}else if ("件数".equals(cellValue)) {
							if (numbersIndex == null) {
								numbersIndex = j;
							}
						}else if ("包装".equals(cellValue)) {
							typeIndex = j;
						}else if (cellValue.contains("计价重量")) {
							weightIndex = j;
						}else if ("参考单号".equals(cellValue)) {
							switchNoIndex = j;
						}else if ("物流商".equals(cellValue)) {
							transportTypeIndex = j;
						}else if ("运单号".equals(cellValue)) {
							orderNoIndex = j;
						}else if ("客户运费".equals(cellValue)) {
							totalPriceIndex = j;
						}else if ("下单日期".equals(cellValue)) {
							sentTimeIndex = j;
						}else if ("特殊备注".equals(cellValue)) {
							reMarkIndex = j;
						}
					}
					if (sentTimeIndex != null) {
						break;
					}
				}
				// 读取数据行
				for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						Shipment shipMent = new Shipment();

						String orderNo = getCellValue(currentRow.getCell(orderNoIndex), true);
						if (WebTool.isEmpty(orderNo)) {
							continue;
						}

						try {
							Date javaDate = HSSFDateUtil.getJavaDate(currentRow.getCell(sentTimeIndex).getNumericCellValue());
							shipMent.setSenttime(javaDate);
						} catch (Exception e) {
//							e.printStackTrace();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
							String date = getCellValue(currentRow.getCell(sentTimeIndex), true).trim();
							Date sentDate = sdf.parse(date);
							shipMent.setSenttime(sentDate);
						}
						shipMent.setOrderno(getCellValue(currentRow.getCell(orderNoIndex), true));
						if (countryIndex != null) {
							if (!WebTool.isEmpty(getCellValue(currentRow.getCell(countryIndex), true))) {
								shipMent.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
							}
						}
						if (typeIndex != null) {
							if (!WebTool.isEmpty(getCellValue(currentRow.getCell(typeIndex), true))) {
								shipMent.setType(getCellValue(currentRow.getCell(typeIndex), true));
							}
						}
						if (addresseeIndex != null) {
							shipMent.setAddressee(getCellValue(currentRow.getCell(addresseeIndex), true));
						}
						if (weightIndex != null) {
							String weight = getCellValue(currentRow.getCell(weightIndex), true).trim();
							shipMent.setSettleweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setRealweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setBulkweight(WebTool.isEmpty(weight) == true? new String("0.00") :new String(weight));
						}
						if (numbersIndex != null) {
							String numbers = getCellValue(currentRow.getCell(numbersIndex), true);
							shipMent.setNumbers(WebTool.isEmpty(numbers) == true ? 0 : Integer.parseInt(numbers));
						}
						shipMent.setCharge("0.00");
						shipMent.setFuelsurcharge("0.00");
						shipMent.setSecuritycosts("0.00");
						String switchNo = "";
						if (switchNoIndex != null) {
							switchNo = getCellValue(currentRow.getCell(switchNoIndex), true).trim();
						}
						if (reMarkIndex != null) {
							String other = getCellValue(currentRow.getCell(reMarkIndex), true).trim();
							shipMent.setRemark(switchNo+"|"+other);
						}else {
							shipMent.setRemark(switchNo);
						}
						shipMent.setTaxs("0.00");
						if (totalPriceIndex != null) {
							String price = getCellValue(currentRow.getCell(totalPriceIndex), true).trim();
							shipMent.setTotalprice(WebTool.isEmpty(price) == true? "0.00" :price);
						}
						shipMent.setTransportcompany("SF");//顺丰
						if (transportTypeIndex != null) {
							String transportType = getCellValue(currentRow.getCell(transportTypeIndex), true).trim();
							shipMent.setTransporttype(transportType);
						}
						shipMent.setCreatetime(new Date());
						shipMent.setUuid(uuid);
						if (list.contains(shipMent)) {
							int index = list.indexOf(shipMent);
							Shipment oldShipment = list.get(index);
							//金额累加
							oldShipment.setTotalprice(String.valueOf(Double.parseDouble(oldShipment.getTotalprice().toString())+Double.parseDouble(shipMent.getTotalprice().toString())));
							oldShipment.setRemark(oldShipment.getRemark()+"-"+shipMent.getRemark());
							list.set(index, oldShipment);//替换掉原始的
						}else {
							list.add(shipMent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				System.out.println("==========================excel解析完成============================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}



	/**
	 * 读取迅邮运单表格
	 * @param filePath
	 * @param uuid
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws IllegalArgumentException
	 * @throws NestedServletException
	 * @throws Exception
	 */
	public static List<Shipment> readXYExcel(String filePath, String uuid, String company) throws EncryptedDocumentException, InvalidFormatException, IOException, NestedServletException, IllegalArgumentException{
		List<Shipment> list = new ArrayList<Shipment>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		workbook = getWorkbook(filePath);
		try {
			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				//System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer sentTimeIndex = null;
				Integer innerNoIndex = null;
				Integer orderNoIndex = null;
				Integer countryIndex = null;
				Integer numbersIndex = null;
				Integer weightIndex = null;
				Integer typeIndex = null;
				Integer totalPriceIndex = null;
				Integer reMarkIndex = null;

				for (int i = firstRowIndex; i <= lastRowIndex; i++) {
					Row currentRow = sheet.getRow(i);// 当前行
					for (int j = 0; j < currentRow.getLastCellNum(); j++) {
						String cellValue = getCellValue(currentRow.getCell(j), true);
						if ("业务日期".equals(cellValue)) {
							sentTimeIndex = j;
						}else if ("内部单号".equals(cellValue)) {
							innerNoIndex = j;
						}else if ("转单号码".equals(cellValue)) {
							orderNoIndex = j;
						}else if ("目的地".equals(cellValue)) {
							countryIndex = j;
						}else if ("件数".equals(cellValue)) {
							numbersIndex = j;
						}else if ("重量".equals(cellValue)) {
							weightIndex = j;
						}else if ("类型".equals(cellValue)) {
							typeIndex = j;
						}else if ("折扣价".equals(cellValue)) {
							totalPriceIndex = j;
						}else if ("备注".equals(cellValue)) {
							reMarkIndex = j;
						}
					}
					if (sentTimeIndex != null) {
						break;
					}
				}
				// 读取数据行
				for (int rowIndex = 4; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						Shipment shipMent = new Shipment();

						String sentTime = getCellValue(currentRow.getCell(sentTimeIndex), true);
						if (WebTool.isEmpty(sentTime)) {
							continue;
						}

						String time=currentRow.getCell(sentTimeIndex).toString();
						time="20"+time.split("\\.")[0]+"-"+time.split("\\.")[1]+"-"+time.split("\\.")[2];
						System.out.println("时间="+time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date sentDate = sdf.parse(time);
						shipMent.setSenttime(sentDate);
//                    	try {
//							Date javaDate = HSSFDateUtil.getJavaDate(currentRow.getCell(sentTimeIndex).getNumericCellValue());
//							shipMent.setSenttime(javaDate);
//						} catch (Exception e) {
////							e.printStackTrace();
//							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
////							String date = getCellValue(currentRow.getCell(sentTimeIndex), true).trim();
//							Date sentDate = sdf.parse(time);
//							shipMent.setSenttime(sentDate);
//						}
						shipMent.setOrderno(getCellValue(currentRow.getCell(orderNoIndex), true));
						if (!WebTool.isEmpty(getCellValue(currentRow.getCell(countryIndex), true))) {
							shipMent.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
						}
						if (weightIndex != null) {
							String weight = getCellValue(currentRow.getCell(weightIndex), true).trim();
							shipMent.setSettleweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setRealweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setType(getCellValue(currentRow.getCell(typeIndex), true));
						}
						if (numbersIndex != null) {
							String numbers = getCellValue(currentRow.getCell(numbersIndex), true);
							shipMent.setNumbers(WebTool.isEmpty(numbers) == true ? 0 : Integer.parseInt(numbers));
						}
						shipMent.setCharge("0.00");
						shipMent.setFuelsurcharge("0.00");
						shipMent.setSecuritycosts("0.00");
						String innerNo ="";
						String other = "";
						if (innerNoIndex != null) {
							innerNo = getCellValue(currentRow.getCell(innerNoIndex), true).trim();
						}
						if (reMarkIndex != null) {
							other = getCellValue(currentRow.getCell(reMarkIndex), true).trim();
						}
						if (!"".equals(other)) {
							shipMent.setRemark(innerNo+"|"+other);
						}
						shipMent.setTaxs("0.00");
						if (totalPriceIndex != null) {
							String price = getCellValue(currentRow.getCell(totalPriceIndex), true).trim();
							shipMent.setTotalprice(WebTool.isEmpty(price) == true? "0.00" :price);
						}
						shipMent.setTransportcompany(company);//航邮/灿鑫/深圳诚泰
						shipMent.setCreatetime(new Date());
						shipMent.setUuid(uuid);
						if (list.contains(shipMent)) {
							int index = list.indexOf(shipMent);
							Shipment oldShipment = list.get(index);
							//金额累加
							oldShipment.setTotalprice(String.valueOf(Double.parseDouble(oldShipment.getTotalprice().toString())+Double.parseDouble(shipMent.getTotalprice().toString())));
							oldShipment.setRemark(oldShipment.getRemark()+"-"+shipMent.getRemark());
							list.set(index, oldShipment);//替换掉原始的
						}else {
							list.add(shipMent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				System.out.println("==========================excel解析完成============================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}


	/**
	 * 读取航邮运单表格
	 * @param filePath
	 * @param uuid
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws IllegalArgumentException
	 * @throws NestedServletException
	 * @throws Exception
	 */
	public static List<Shipment> readHYExcel(String filePath, String uuid, String company) throws EncryptedDocumentException, InvalidFormatException, IOException, NestedServletException, IllegalArgumentException{
		List<Shipment> list = new ArrayList<Shipment>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		workbook = getWorkbook(filePath);
		try {
			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				//System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer sentTimeIndex = null;
				Integer innerNoIndex = null;
				Integer orderNoIndex = null;
				Integer countryIndex = null;
				Integer numbersIndex = null;
				Integer weightIndex = null;
				Integer bulkweightIndex = null;
				Integer totalPriceIndex = null;
				Integer reMarkIndex = null;

				for (int i = firstRowIndex; i <= lastRowIndex; i++) {
					Row currentRow = sheet.getRow(i);// 当前行
					for (int j = 0; j < currentRow.getLastCellNum(); j++) {
						String cellValue = getCellValue(currentRow.getCell(j), true);
						if ("发件时间".equals(cellValue)) {
							sentTimeIndex = j;
						}else if ("内部单号".equals(cellValue)) {
							innerNoIndex = j;
						}else if ("运单号码".equals(cellValue)) {
							orderNoIndex = j;
						}else if ("收件国家".equals(cellValue)) {
							countryIndex = j;
						}else if ("件数".equals(cellValue)) {
							numbersIndex = j;
						}else if ("重量".equals(cellValue)) {
							weightIndex = j;
						}else if ("体积".equals(cellValue)) {
							bulkweightIndex = j;
						}else if ("运费".equals(cellValue)) {
							totalPriceIndex = j;
						}else if ("备注".equals(cellValue)) {
							reMarkIndex = j;
						}
					}
					if (sentTimeIndex != null) {
						break;
					}
				}
				// 读取数据行
				for (int rowIndex = 1; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						Shipment shipMent = new Shipment();

						String sentTime = getCellValue(currentRow.getCell(sentTimeIndex), true);
						if (WebTool.isEmpty(sentTime)) {
							continue;
						}
						try {
							Date javaDate = HSSFDateUtil.getJavaDate(currentRow.getCell(sentTimeIndex).getNumericCellValue());
							shipMent.setSenttime(javaDate);
						} catch (Exception e) {
//							e.printStackTrace();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String date = getCellValue(currentRow.getCell(sentTimeIndex), true).trim();
							Date sentDate = sdf.parse(date);
							shipMent.setSenttime(sentDate);
						}
						shipMent.setOrderno(getCellValue(currentRow.getCell(orderNoIndex), true));
						if (!WebTool.isEmpty(getCellValue(currentRow.getCell(countryIndex), true))) {
							shipMent.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
						}
						if (weightIndex != null) {
							String weight = getCellValue(currentRow.getCell(weightIndex), true).trim();
							shipMent.setSettleweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setRealweight(WebTool.isEmpty(weight) == true?"0.00" :weight);
							shipMent.setBulkweight(getCellValue(currentRow.getCell(bulkweightIndex), true));
						}
						if (numbersIndex != null) {
							String numbers = getCellValue(currentRow.getCell(numbersIndex), true);
							shipMent.setNumbers(WebTool.isEmpty(numbers) == true ? 0 : Integer.parseInt(numbers));
						}
						shipMent.setCharge("0.00");
						shipMent.setFuelsurcharge("0.00");
						shipMent.setSecuritycosts("0.00");
						String innerNo ="";
						String other = "";
						if (innerNoIndex != null) {
							innerNo = getCellValue(currentRow.getCell(innerNoIndex), true).trim();
						}
						if (reMarkIndex != null) {
							other = getCellValue(currentRow.getCell(reMarkIndex), true).trim();
						}
						if (!"".equals(innerNo) ||!"".equals(other)) {
							shipMent.setRemark(innerNo+"|"+other);
						}
						shipMent.setTaxs("0.00");
						if (totalPriceIndex != null) {
							String price = getCellValue(currentRow.getCell(totalPriceIndex), true).trim();
							shipMent.setTotalprice(WebTool.isEmpty(price) == true? "0.00" :price);
						}
						shipMent.setTransportcompany(company);//航邮/灿鑫/深圳诚泰
						shipMent.setCreatetime(new Date());
						shipMent.setUuid(uuid);
						if (list.contains(shipMent)) {
							int index = list.indexOf(shipMent);
							Shipment oldShipment = list.get(index);
							//金额累加
							oldShipment.setTotalprice(String.valueOf(Double.parseDouble(oldShipment.getTotalprice().toString())+Double.parseDouble(shipMent.getTotalprice().toString())));
							oldShipment.setRemark(oldShipment.getRemark()+"-"+shipMent.getRemark());
							list.set(index, oldShipment);//替换掉原始的
						}else {
							list.add(shipMent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				System.out.println("==========================excel解析完成============================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}


	/**
	 * 读取泰蓝国际运单表格
	 * @param filePath
	 * @param uuid
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws IllegalArgumentException
	 * @throws NestedServletException
	 * @throws Exception
	 */
	public static List<Shipment> readTLExcel(String filePath, String uuid) throws EncryptedDocumentException, InvalidFormatException, IOException, NestedServletException, IllegalArgumentException{
		List<Shipment> list = new ArrayList<Shipment>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		workbook = getWorkbook(filePath);
		try {
			// 读文件 一个sheet一个sheet地读取
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				//System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer sentTimeIndex = null;
				Integer orderNoIndex = null;
				Integer netIndex = null;
				Integer countryIndex = null;
				Integer numbersIndex = null;
				Integer weightIndex = null;
				Integer priceIndex = null;
				Integer totalPriceIndex = null;
				Integer reMarkIndex = null;

				for (int i = firstRowIndex; i <= lastRowIndex; i++) {
					Row currentRow = sheet.getRow(i);// 当前行
					for (int j = 0; j < currentRow.getLastCellNum(); j++) {
						String cellValue = getCellValue(currentRow.getCell(j), true);
						if ("日期".equals(cellValue)) {
							sentTimeIndex = j;
						}else if ("运单号".equals(cellValue)) {
							orderNoIndex = j;
						}else if ("目的地".equals(cellValue)) {
							countryIndex = j;
						}else if ("件数".equals(cellValue)) {
							numbersIndex = j;
						}else if ("重量（KG）".equals(cellValue)) {
							weightIndex = j;
						}else if ("渠道".equals(cellValue)) {
							netIndex = j;
						}else if ("金额".equals(cellValue)) {
							totalPriceIndex = j;
						}else if ("单价".equals(cellValue)) {
							priceIndex = j;
						}else if ("备注".equals(cellValue)) {
							reMarkIndex = j;
						}
					}
					if (sentTimeIndex != null) {
						break;
					}
				}
				// 读取数据行
				for (int rowIndex = 1; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						Shipment shipMent = new Shipment();

						String sentTime = getCellValue(currentRow.getCell(sentTimeIndex), true);
						if (WebTool.isEmpty(sentTime)) {
							continue;
						}
						try {
							Date javaDate = HSSFDateUtil.getJavaDate(currentRow.getCell(sentTimeIndex).getNumericCellValue());
							shipMent.setSenttime(javaDate);
						} catch (Exception e) {
//							e.printStackTrace();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
							String date = getCellValue(currentRow.getCell(sentTimeIndex), true).trim();
							Date sentDate = sdf.parse(date);
							shipMent.setSenttime(sentDate);
						}
						shipMent.setOrderno(getCellValue(currentRow.getCell(orderNoIndex), true));
						if (!WebTool.isEmpty(getCellValue(currentRow.getCell(countryIndex), true))) {
							shipMent.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
						}
						if (weightIndex != null) {
							String weight = getCellValue(currentRow.getCell(weightIndex), true).trim();
							shipMent.setSettleweight(WebTool.isEmpty(weight) == true? "0.00" :weight);
							shipMent.setRealweight(WebTool.isEmpty(weight) == true? "0.00":weight);
							shipMent.setBulkweight(WebTool.isEmpty(weight) == true? "0.00" :new BigDecimal(weight).toString());
						}
						if (numbersIndex != null) {
							String numbers = getCellValue(currentRow.getCell(numbersIndex), true);
							shipMent.setNumbers(WebTool.isEmpty(numbers) == true ? 0 : Integer.parseInt(numbers));
						}
						shipMent.setCharge("0.00");
						shipMent.setFuelsurcharge("0.00");
						shipMent.setSecuritycosts("0.00");
						String innerNo ="";
						String other = "";
						if (reMarkIndex != null) {
							other = getCellValue(currentRow.getCell(reMarkIndex), true).trim();
						}
						if (!"".equals(innerNo) ||!"".equals(other)) {
							shipMent.setRemark(innerNo+"|"+other);
						}
						shipMent.setTaxs("0.00");

//						if (priceIndex != null) {
//							String price = getCellValue(currentRow.getCell(priceIndex), true).trim();
//							shipMent.setTotalprice(WebTool.isEmpty(price) == true? new BigDecimal(0.00) :new BigDecimal(price));
//						}
						if (totalPriceIndex != null) {
							String totalprice = getCellValue(currentRow.getCell(totalPriceIndex), true).trim();
							shipMent.setTotalprice(WebTool.isEmpty(totalprice) == true? "0.00" :totalprice);
						}
						shipMent.setTransportcompany("TL");//泰蓝国际
						if (netIndex != null) {
							String transportType = getCellValue(currentRow.getCell(netIndex), true).trim();
							shipMent.setTransporttype(transportType);
						}
						shipMent.setCreatetime(new Date());
						shipMent.setUuid(uuid);
						if (list.contains(shipMent)) {
							int index = list.indexOf(shipMent);
							Shipment oldShipment = list.get(index);
							//金额累加
							oldShipment.setTotalprice(String.valueOf(Double.parseDouble(oldShipment.getTotalprice().toString())+Double.parseDouble(shipMent.getTotalprice().toString())));
							oldShipment.setRemark(oldShipment.getRemark()+"-"+shipMent.getRemark());
							list.set(index, oldShipment);//替换掉原始的
						}else {
							list.add(shipMent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				System.out.println("==========================excel解析完成============================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}








	/**
	 * 读取马帮运单excel的内容
	 * @param filePath
	 * @throws Exception
	 */
	public static Map<String, List<?>> readMabangExcel(String filePath) throws Exception {
		Map<String, List<?>> resMap = new HashMap<String, List<?>>();
		List<Mabangshipment> list = new ArrayList<Mabangshipment>();
		List<String> failList = new ArrayList<String>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		try {
			workbook = getWorkbook(filePath);
			// 读文件 一个sheet一个sheet地读取
			for (int j = 0; j < 1; j++) {
				Sheet sheet = workbook.getSheetAt(0);
				if (sheet == null) {
					continue;
				}
				System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer ordernoIndex = null;
				Integer paymenttimeIndex = null;
				Integer transportmethodIndex = null;
				Integer shipnoIndex = null;
				Integer storenameIndex = null;
				Integer clientacountIndex = null;
				Integer clientnameIndex = null;
				Integer clienttelphoneIndex = null;
				Integer countryIndex = null;
				Integer productunitpriceIndex = null;
				Integer producttotalamountIndex = null;
				Integer producttotalcostIndex = null;
				Integer weightIndex = null;
				Integer skuIndex = null;
				Integer productnameIndex = null;
				Integer productnumbersIndex = null;
				Integer productdirectoryIndex = null;
				Integer currencyIndex = null;
				Integer ordertotalamoutIndex = null;
				Integer freightrevenueIndex = null;
				Integer shipmentexpensesIdex = null;
				// 读取首行:表头(如果行大于1则表示当前sheet有数据)
				if (lastRowIndex>=1) {
					Row firstRow = sheet.getRow(firstRowIndex);
					for (int i = firstRow.getFirstCellNum(); i <= firstRow.getLastCellNum(); i++) {
						Cell cell = firstRow.getCell(i);
						String cellValue = getCellValue(cell, true);
//                		System.out.print(" " + cellValue + "\t");
						if ("订单编号".equals(cellValue)) {
							ordernoIndex = i;
						}else if ("付款时间".equals(cellValue)) {
							paymenttimeIndex = i;
						}else if ("货运方式".equals(cellValue)) {
							transportmethodIndex = i;
						}else if ("货运单号".equals(cellValue)) {
							shipnoIndex = i;
						}else if ("店铺名".equals(cellValue)) {
							storenameIndex = i;
						}else if ("客户账号".equals(cellValue)) {
							clientacountIndex = i;
						}else if ("客户姓名".equals(cellValue)) {
							clientnameIndex = i;
						}else if ("电话1".equals(cellValue)) {
							clienttelphoneIndex = i;
						}else if ("国家".equals(cellValue)) {
							countryIndex = i;
						}else if ("商品销售单价".equals(cellValue)) {
							productunitpriceIndex = i;
						}else if ("商品总金额".equals(cellValue)) {
							producttotalamountIndex = i;
						}else if ("商品总成本".equals(cellValue)) {
							producttotalcostIndex = i;
						}else if ("商品重量".equals(cellValue)) {
							weightIndex = i;
						}else if ("SKU".equals(cellValue)) {
							skuIndex = i;
						}else if ("订单商品名称".equals(cellValue)) {
							productnameIndex = i;
						}else if ("商品数量".equals(cellValue)) {
							productnumbersIndex = i;
						}else if ("商品目录".equals(cellValue)) {
							productdirectoryIndex = i;
						}else if ("币种".equals(cellValue)) {
							currencyIndex = i;
						}else if ("订单总金额".equals(cellValue)) {
							ordertotalamoutIndex = i;
						}else if ("运费收入".equals(cellValue)) {
							freightrevenueIndex = i;
						}else if ("支出运费".equals(cellValue)) {
							shipmentexpensesIdex = i;
						}
					}
//                	System.out.println("");
				}

				// 读取数据行
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (int rowIndex = 1; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						if ("".equals(getCellValue(currentRow.getCell(shipnoIndex), true))) {
							failList.add(getCellValue(currentRow.getCell(ordernoIndex), true));
							continue;
						}
						Mabangshipment mabangshipment = new Mabangshipment();
						mabangshipment.setOrderno(getCellValue(currentRow.getCell(ordernoIndex), true));
						mabangshipment.setPaymenttime(sdf.parse(getCellValue(currentRow.getCell(paymenttimeIndex), true)));
						mabangshipment.setShipmethod(getCellValue(currentRow.getCell(transportmethodIndex), true));
						mabangshipment.setShipno(getCellValue(currentRow.getCell(shipnoIndex), true));
						mabangshipment.setStorename(getCellValue(currentRow.getCell(storenameIndex), true));
						mabangshipment.setClientacount(getCellValue(currentRow.getCell(clientacountIndex), true));
						mabangshipment.setClientname(getCellValue(currentRow.getCell(clientnameIndex), true));
						mabangshipment.setClienttelphone(getCellValue(currentRow.getCell(clienttelphoneIndex), true));
						mabangshipment.setCountry(getCellValue(currentRow.getCell(countryIndex), true));
						if (productunitpriceIndex != null) {
							String productunitprice = getCellValue(currentRow.getCell(productunitpriceIndex), true).trim();
							mabangshipment.setProductunitprice(WebTool.isEmpty(productunitprice) == true? new BigDecimal(0.00) :new BigDecimal(productunitprice));
						}
						if (producttotalamountIndex != null) {
							String producttotalamount = getCellValue(currentRow.getCell(producttotalamountIndex), true).trim();
							mabangshipment.setProducttotalamount(WebTool.isEmpty(producttotalamount) == true? new BigDecimal(0.00) :new BigDecimal(producttotalamount));
						}
						if (producttotalcostIndex != null) {
							String producttotalcost = getCellValue(currentRow.getCell(producttotalcostIndex), true).trim();
							mabangshipment.setProducttotalcost(WebTool.isEmpty(producttotalcost) == true? new BigDecimal(0.00) :new BigDecimal(producttotalcost));
						}
						if (freightrevenueIndex!=null) {
							String freightrevenue = getCellValue(currentRow.getCell(freightrevenueIndex), true).trim();
							mabangshipment.setFreightrevenue(WebTool.isEmpty(freightrevenue) == true? new BigDecimal(0.00) :new BigDecimal(freightrevenue));
						}
						if (shipmentexpensesIdex!=null) {
							String shipmentexpenses = getCellValue(currentRow.getCell(shipmentexpensesIdex), true).trim();
							mabangshipment.setFreightrevenue(WebTool.isEmpty(shipmentexpenses) == true? new BigDecimal(0.00) :new BigDecimal(shipmentexpenses));
						}
						if (weightIndex!=null) {
							String weight = getCellValue(currentRow.getCell(weightIndex), true).trim();
							mabangshipment.setWeight(WebTool.isEmpty(weight) == true? new BigDecimal(0.00) :new BigDecimal(weight));
						}
						mabangshipment.setSku(getCellValue(currentRow.getCell(skuIndex), true).trim());
						mabangshipment.setProductname(getCellValue(currentRow.getCell(productnameIndex), true).trim());
						if (productnumbersIndex!=null) {
							String productnumbers = getCellValue(currentRow.getCell(productnumbersIndex), true).trim();
							mabangshipment.setProductnumbers(WebTool.isEmpty(productnumbers) == true? Integer.parseInt("0") :Integer.parseInt(productnumbers));
						}
						mabangshipment.setProductdirectory(getCellValue(currentRow.getCell(productdirectoryIndex), true).trim());
						mabangshipment.setCurrency(getCellValue(currentRow.getCell(currencyIndex), true).trim());
						if (ordertotalamoutIndex!=null) {
							String ordertotalamout = getCellValue(currentRow.getCell(ordertotalamoutIndex), true).trim();
							mabangshipment.setOrdertotalamout(WebTool.isEmpty(ordertotalamout) == true? new BigDecimal(0.00) :new BigDecimal(ordertotalamout));
						}
						mabangshipment.setCreatetime(new Date());
						list.add(mabangshipment);
					} catch (Exception e) {
						e.printStackTrace();
						failList.add(getCellValue(currentRow.getCell(ordernoIndex), true));
					}
				}
				resMap.put("success", list);
				resMap.put("fail", failList);
				System.out.println("======================================================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return resMap;
		} finally {
//            if (workbook != null) {
//               try {
//                    workbook.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
		}
		return resMap;
	}

	/**
	 * 读取运单excel的内容
	 * @param filePath
	 * @throws Exception
	 */
	public static List<Skuinfo> readSkuExcel(String filePath) throws Exception {
		List<Skuinfo> list = new ArrayList<Skuinfo>();
		// 检查
		fileCheck(filePath);
		// 获取workbook对象
		Workbook workbook = null;
		try {
			workbook = getWorkbook(filePath);
			// 读文件 一个sheet一个sheet地读取
			for (int j = 0; j < 1; j++) {
				Sheet sheet = workbook.getSheetAt(0);
				if (sheet == null) {
					continue;
				}
				System.out.println("=======================" + sheet.getSheetName() + "=========================");
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				Integer skuIndex = null;
				Integer skuCnNameIndex = null;
				Integer skuEnNameIndex = null;
				Integer statuIndex = null;
				Integer productDirectoryIndex = null;
				Integer skuSumIndex = null;
				Integer shippedIndex = null;
				Integer weightIndex = null;
				Integer skuImgLinkIndex = null;
				Integer shipImgLinkIndex = null;
				Integer warehouseIndex = null;
				Integer unitCostIndex = null;


				// 读取首行:表头(如果行大于1则表示当前sheet有数据)
				if (lastRowIndex>=1) {
					Row firstRow = sheet.getRow(firstRowIndex);
					for (int i = firstRow.getFirstCellNum(); i <= firstRow.getLastCellNum(); i++) {
						Cell cell = firstRow.getCell(i);
						String cellValue = getCellValue(cell, true);
						System.out.print(" " + cellValue + "\t");
						if ("库存SKU".equals(cellValue)) {
							skuIndex = i;
						}else if ("库存SKU中文名称".equals(cellValue)) {
							skuCnNameIndex = i;
						}else if ("库存SKU英文名称".equals(cellValue)) {
							skuEnNameIndex = i;
						}else if ("状态".equals(cellValue)) {
							statuIndex = i;
						}else if ("商品目录".equals(cellValue)) {
							productDirectoryIndex = i;
						}else if ("库存总量".equals(cellValue)) {
							skuSumIndex = i;
						}else if ("已发货(7/28/42)".equals(cellValue)) {
							shippedIndex = i;
						}else if ("重量".equals(cellValue)) {
							weightIndex = i;
						}else if ("库存图片链接".equals(cellValue)) {
							skuImgLinkIndex = i;
						}else if ("销售图片链接".equals(cellValue)) {
							shipImgLinkIndex = i;
						}else if ("商品仓位".equals(cellValue)) {
							warehouseIndex = i;
						}else if ("统一成本价".equals(cellValue)) {
							unitCostIndex = i;
						}
					}
					System.out.println("");
				}

				// 读取数据行
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (int rowIndex = 1; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					try {
						Skuinfo skuinfo = new Skuinfo();
						skuinfo.setSku(getCellValue(currentRow.getCell(skuIndex), true));
						skuinfo.setSkucnname(getCellValue(currentRow.getCell(skuCnNameIndex), true));
						skuinfo.setSkuenname(getCellValue(currentRow.getCell(skuEnNameIndex), true));
						String statu = getCellValue(currentRow.getCell(statuIndex), true);
						Integer statuRes = null;
						if ("停止销售".equals(statu)) {
							statuRes = 0;
						}else if ("自动创建".equals(statu)) {
							statuRes = 1;
						}else if ("正常销售".equals(statu)) {
							statuRes = 2;
						}
						skuinfo.setStatu(statuRes);
						skuinfo.setProductdirectory(getCellValue(currentRow.getCell(productDirectoryIndex), true));
						skuinfo.setSkusum(Integer.parseInt(getCellValue(currentRow.getCell(skuSumIndex), true)));
						skuinfo.setShipped(getCellValue(currentRow.getCell(shippedIndex), true));
						skuinfo.setWeight(getCellValue(currentRow.getCell(weightIndex), true));
						skuinfo.setSkuimglink(getCellValue(currentRow.getCell(skuImgLinkIndex), true));
						skuinfo.setShipimglink(getCellValue(currentRow.getCell(shipImgLinkIndex), true));
						skuinfo.setWarehouse(getCellValue(currentRow.getCell(warehouseIndex), true));
						skuinfo.setUnitcost(new BigDecimal(getCellValue(currentRow.getCell(unitCostIndex), true)));
						skuinfo.setCreatetime(new Date());
						list.add(skuinfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("======================================================");
			}
		} catch (Exception e) {
			System.out.println("上传文件数据读取错误!");
			e.printStackTrace();
			return list;
		} finally {
//            if (workbook != null) {
//               try {
//                    workbook.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
		}
		return list;
	}



	/**
	 * 取单元格的值
	 * @param cell 单元格对象
	 * @param treatAsStr 为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
	 * @return
	 */
	private static String getCellValue(Cell cell, boolean treatAsStr) {
		if (cell == null) {
			return "";
		}
		if (treatAsStr) {
			// 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
			// 加上下面这句，临时把它当做文本来读取
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue()).trim();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue()).trim();
		} else {
			return String.valueOf(cell.getStringCellValue()).trim() == null ? "":String.valueOf(cell.getStringCellValue()).trim();
		}
	}


	public static Workbook getWorkbook(String filename) throws NestedServletException, IllegalArgumentException, EncryptedDocumentException, InvalidFormatException, FileNotFoundException, IOException{
		Workbook workbook = null;
		if (null != filename) {
			workbook = WorkbookFactory.create(new FileInputStream(filename));
//			workbook = createWorkBook(new FileInputStream(filename));
		}
		return workbook;
	}

	/**
	 * 获取Workbook对象(xls和xlsx对象不同,不过都是Workbook的实现类)
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
//    public static Workbook getWorkbook(String filePath) throws Exception{
//		Workbook workbook = null;
//		InputStream is = new FileInputStream(filePath);
//        if (filePath.endsWith(EXTENSION_XLS)) {
//            workbook = new HSSFWorkbook(is);
//        } else if (filePath.endsWith(EXTENSION_XLSX)) {
//            workbook = new XSSFWorkbook(is);
//        }
//        return workbook;
//	}

	/**
    * 静态方法  解决创建Workbook 创建产生的问题
    * @param inp
    * @return
    * @throws IOException
    * @throws InvalidFormatException
    */
	public static Workbook createWorkBook(InputStream inp) throws IOException, InvalidFormatException {
		if (!inp.markSupported()) {
			inp = new PushbackInputStream(inp, 8);
		}
		if (POIFSFileSystem.hasPOIFSHeader(inp)) {
			return new HSSFWorkbook(inp);
		}
		if (POIXMLDocument.hasOOXMLHeader(inp)) {
			return new XSSFWorkbook(OPCPackage.open(inp));
		}
		throw new IllegalArgumentException("你的excel版本目前poi解析不了");
	}


	/**
	 * 创建excel
	 * @param fileName
	 * @param
	 */
	public static void createExcel(String fileName,List<Shipment> list){
		try{
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
			String[] header1 = {"company","orderNo", "country", "net", "numbers", "type", "realWeight", "bulkWeight", "settleWeight", "shipment", "addfuelTax", "securityCosts", "taxs", "price","remark","passFlag"};
			String[] header2 = {"运输公司","运单条码", "收件人国家", "快件网络", "件数", "物品类型", "实重", "体积重量", "结算重量", "运费", "燃油附加费 ", "安检费 ", "税金", "总金额 (RMB)","备注","是否冻结"};
			List<String[]> header = new ArrayList<String[]>();
			header.add(header1);
			header.add(header2);
			for (int i = 0; i < header.size(); i++) {
				String[] str = header.get(i);
				XSSFRow headrow = sheet.createRow(i);// 创建一个行对象
				for (int j = 0; j < str.length; j++) {
					XSSFCell cell = headrow.createCell(j);// 创建单元格
					cell.setCellValue(str[j]);
				}
			}
			for (int i = 2; i < list.size()+2; i++) {
				Shipment shipment = list.get(i-2);
				XSSFRow row = sheet.createRow(i);
				// 创建单元格
				XSSFCell companyCell = row.createCell(0);
				companyCell.setCellValue(shipment.getTransportcompany());
				XSSFCell orderNoCell = row.createCell(1);
				orderNoCell.setCellValue(shipment.getOrderno());
				XSSFCell countryCell = row.createCell(2);
				countryCell.setCellValue(shipment.getCountry());
				XSSFCell netCell = row.createCell(3);
				netCell.setCellValue(shipment.getTransportcompany());
				XSSFCell numbersCell = row.createCell(4);
				numbersCell.setCellValue(shipment.getNumbers());
				XSSFCell typeCell = row.createCell(5);
				typeCell.setCellValue(shipment.getType());
				XSSFCell realWeightCell = row.createCell(6);
				realWeightCell.setCellValue(Double.parseDouble(shipment.getRealweight().toString()));
				XSSFCell bulkWeightCell = row.createCell(7);
				bulkWeightCell.setCellValue(shipment.getBulkweight());
				XSSFCell settleWeightCell = row.createCell(8);
				settleWeightCell.setCellValue(shipment.getSettleweight().toString());
				XSSFCell shipmentCell = row.createCell(9);
				shipmentCell.setCellValue(shipment.getCharge().toString());
				XSSFCell addfuelTaxCell = row.createCell(10);
				addfuelTaxCell.setCellValue(shipment.getFuelsurcharge().toString());
				XSSFCell securityCostsCell = row.createCell(11);
				securityCostsCell.setCellValue(shipment.getSecuritycosts().toString());
				XSSFCell taxsCell = row.createCell(12);
				taxsCell.setCellValue(shipment.getTaxs().toString());
				XSSFCell priceCell = row.createCell(13);
				priceCell.setCellValue(shipment.getTotalprice().toString());
				XSSFCell remarkCell = row.createCell(14);
				remarkCell.setCellValue(shipment.getRemark());
				XSSFCell passFlagCell = row.createCell(15);
				passFlagCell.setCellValue(shipment.getPassFlag()==1?"已冻结,不能再上传":"");
			}
			File file = new File(new File(fileName).getParent());
			if (!file.exists()) {
				file.mkdirs();
			}
			// 文件输出流
			FileOutputStream os = new FileOutputStream(fileName);
			workBook.write(os);
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}




	/**
	 * 导出前三个月的运费
	 * 创建excel
	 * @param fileName
	 * @param
	 */
	public static void createExcel2(String fileName,List<Shipment> list){
		try{
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
			List<String[]> header = new ArrayList<String[]>();
			String[]  header1 = {"月份","JCEX", "邮政", "原飞航", "其他", "总运费金额", "是否验证"};
			header.add(header1);
			for (int i = 0; i < header.size(); i++) {
				String[] str = header.get(i);
				XSSFRow headrow = sheet.createRow(i);// 创建一个行对象
				for (int j = 0; j < str.length; j++) {
					XSSFCell cell = headrow.createCell(j);// 创建单元格
					cell.setCellValue(str[j]);
				}
			}
			for (int i = 1; i < list.size()+1; i++) {
				Shipment shipment = list.get(i-1);
				XSSFRow row = sheet.createRow(i);
				// 创建单元格
				XSSFCell companyCell = row.createCell(0);
				companyCell.setCellValue(shipment.getCreateTime());
				XSSFCell jecxFreightCell = row.createCell(1);
				jecxFreightCell.setCellValue(shipment.getJecxFreight()==null?0:Double.parseDouble(shipment.getJecxFreight()));
				XSSFCell yzFreightCell = row.createCell(2);
				yzFreightCell.setCellValue(shipment.getEstimatefreight()==null?0:Double.parseDouble(shipment.getEstimatefreight()));
				XSSFCell yfhCell = row.createCell(3);
				yfhCell.setCellValue(shipment.getYfhFreight()==null?0:Double.parseDouble(shipment.getYfhFreight()));
				XSSFCell otherCell = row.createCell(4);
				otherCell.setCellValue(shipment.getOtherFreight()==null?0:Double.parseDouble(shipment.getOtherFreight()));
				XSSFCell totalPriceCell = row.createCell(5);
				totalPriceCell.setCellValue(shipment.getTotalprice()==null?0:Double.parseDouble(shipment.getTotalprice()));
				XSSFCell passFlagCell = row.createCell(6);
				passFlagCell.setCellValue(shipment.getPassFlag()==1?"已验证":"未验证");
			}
			File file = new File(new File(fileName).getParent());
			if (!file.exists()) {
				file.mkdirs();
			}
			// 文件输出流
			FileOutputStream os = new FileOutputStream(fileName);
			workBook.write(os);
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}




	/**
	 * 导出不同条件下筛选的EXCEL 数据
	 * 创建excel
	 * @param fileName
	 * @param
	 */
	public static void createExcel1(String fileName, List<Shipment> list, int choiseType, String type){
		try{
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
			List<String[]> header = new ArrayList<String[]>();
			if(choiseType==0 && "0".equals(type)){
				String[]  header1 = {"company","orderNo", "country", "net", "numbers", "type", "realWeight", "bulkWeight", "settleWeight", "shipment", "addfuelTax", "securityCosts", "taxs", "price","remark","realweight","sweight","volumeweights","estimatefreight","actual_freight"};
				String[]  header2 = {"运输公司","运单条码", "收件人国家", "快件网络", "件数", "物品类型", "实重", "体积重量", "结算重量", "运费", "燃油附加费 ", "安检费 ", "税金", "总金额 (RMB)","备注","重量(运输公司)","重量(我司)","抛重(我司)","预估金额","人工改后运费(RMB)"};
				header.add(header1);
				header.add(header2);
			}else if(choiseType==1 && "0".equals(type)){
				String[] header1 = {"company","orderNo", "country", "net", "numbers", "type", "realWeight", "bulkWeight", "settleWeight", "shipment", "addfuelTax", "securityCosts", "taxs", "price","remark","realweight","sweight","volumeweights"};
				String[] header2 = {"运输公司","运单条码", "收件人国家", "快件网络", "件数", "物品类型", "实重", "体积重量", "结算重量", "运费", "燃油附加费 ", "安检费 ", "税金", "总金额 (RMB)","备注","重量(运输公司)","重量(我司)","抛重(我司)"};
				header.add(header1);
				header.add(header2);
			}else if((choiseType==2||choiseType==3) && "0".equals(type)){
				String[] header1 = {"company","orderNo", "country", "net", "numbers", "type", "realWeight", "bulkWeight", "settleWeight", "shipment", "addfuelTax", "securityCosts", "taxs", "price","remark","realweight","sweight","volumeweights","sweight","svolume","tscompany"
						,"delivery","xtcountry","estimatefreight"};
				String[] header2 = {"运输公司","运单条码", "收件人国家", "快件网络", "件数", "物品类型", "实重", "体积重量", "结算重量", "运费", "燃油附加费 ", "安检费 ", "税金", "总金额 (RMB)","备注","重量(运输公司)","重量(我司)","抛重(我司)","重量","体积","运输方式","交期","国家","预估运费"};
				header.add(header1);
				header.add(header2);
			}else if("1".equals(type)){
				String[] header1 = {"orderNo", "country","estimatefreight"};
				String[] header2 = {"运单条码", "收件人国家", "预估运费"};
				header.add(header1);
				header.add(header2);
			}
			for (int i = 0; i < header.size(); i++) {
				String[] str = header.get(i);
				XSSFRow headrow = sheet.createRow(i);// 创建一个行对象
				for (int j = 0; j < str.length; j++) {
					XSSFCell cell = headrow.createCell(j);// 创建单元格
					cell.setCellValue(str[j]);
				}
			}
			if("0".equals(type)){
				for (int i = 2; i < list.size()+2; i++) {
					Shipment shipment = list.get(i-2);
					XSSFRow row = sheet.createRow(i);
					// 创建单元格
					XSSFCell companyCell = row.createCell(0);
					companyCell.setCellValue(shipment.getTransportcompany());
					XSSFCell orderNoCell = row.createCell(1);
					orderNoCell.setCellValue(shipment.getOrderno());
					XSSFCell countryCell = row.createCell(2);
					countryCell.setCellValue(shipment.getCountry());
					XSSFCell netCell = row.createCell(3);
					netCell.setCellValue(shipment.getTransportcompany());
					XSSFCell numbersCell = row.createCell(4);
					numbersCell.setCellValue(shipment.getNumbers());
					XSSFCell typeCell = row.createCell(5);
					typeCell.setCellValue(shipment.getType());
					XSSFCell realWeightCell = row.createCell(6);
					realWeightCell.setCellValue(shipment.getRealweight()==null?"0":shipment.getRealweight());
					XSSFCell bulkWeightCell = row.createCell(7);
					bulkWeightCell.setCellValue(shipment.getBulkweight());
					XSSFCell settleWeightCell = row.createCell(8);
					settleWeightCell.setCellValue(shipment.getSettleweight()==null?"0":shipment.getSettleweight());
					XSSFCell shipmentCell = row.createCell(9);
					shipmentCell.setCellValue(shipment.getCharge()==null?"0":shipment.getCharge());
					XSSFCell addfuelTaxCell = row.createCell(10);
					addfuelTaxCell.setCellValue(shipment.getFuelsurcharge()==null?"0":shipment.getFuelsurcharge());
					XSSFCell securityCostsCell = row.createCell(11);
					securityCostsCell.setCellValue(shipment.getSecuritycosts()==null?"0":shipment.getSecuritycosts());
					XSSFCell taxsCell = row.createCell(12);
					taxsCell.setCellValue(shipment.getTaxs()==null?"0":shipment.getTaxs());
					XSSFCell priceCell = row.createCell(13);
					priceCell.setCellValue(shipment.getTotalprice()==null?"0":shipment.getTotalprice());
					XSSFCell remarkCell = row.createCell(14);
					remarkCell.setCellValue(shipment.getRemark());

					XSSFCell w1 = row.createCell(15);
					w1.setCellValue(shipment.getRealweight());
					XSSFCell w2 = row.createCell(16);
					w2.setCellValue(shipment.getSweight());
					XSSFCell w3 = row.createCell(17);
					w3.setCellValue(shipment.getVolumeweight());

					if(choiseType==0){
						XSSFCell estimatefreightCell = row.createCell(18);
						estimatefreightCell.setCellValue(shipment.getEstimatefreight()==null?"0":shipment.getEstimatefreight());
						XSSFCell actualFreightCell = row.createCell(19);
						actualFreightCell.setCellValue(shipment.getActual_freight()==null?"0":shipment.getActual_freight());
					}
					if(choiseType==2||choiseType==3){
						XSSFCell sweightCell = row.createCell(18);
						sweightCell.setCellValue(shipment.getSweight());
						XSSFCell svolumeCell = row.createCell(19);
						svolumeCell.setCellValue(shipment.getSvolume());
						XSSFCell tscompanyCell = row.createCell(20);
						tscompanyCell.setCellValue(shipment.getTscompany());
						XSSFCell deliveryCell = row.createCell(21);
						deliveryCell.setCellValue(shipment.getDelivery());
						XSSFCell xtcountryCell = row.createCell(22);
						xtcountryCell.setCellValue(shipment.getXtcountry());
						XSSFCell estimatefreightCell = row.createCell(23);
						estimatefreightCell.setCellValue(shipment.getEstimatefreight()==null?"0":shipment.getEstimatefreight());
					}
				}
			}else if("1".equals(type)){
				for (int i = 2; i < list.size()+2; i++) {
					Shipment shipment = list.get(i-2);
					XSSFRow row = sheet.createRow(i);
					// 创建单元格
					XSSFCell orderNoCell = row.createCell(0);
					orderNoCell.setCellValue(shipment.getOrderno());
					XSSFCell countryCell = row.createCell(1);
					countryCell.setCellValue(shipment.getCountry());
					XSSFCell estimatefreightCell = row.createCell(2);
					estimatefreightCell.setCellValue(shipment.getEstimatefreight()==null?"0":shipment.getEstimatefreight());
				}
			}
			File file = new File(new File(fileName).getParent());
			if (!file.exists()) {
				file.mkdirs();
			}
			// 文件输出流
			FileOutputStream os = new FileOutputStream(fileName);
			workBook.write(os);
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 根据Excel获取支付宝账单信息
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<AliPayInfo> getAliPayInfoByExcel(String filePath) throws Exception {
		List<AliPayInfo> infoList = new ArrayList<>();
		// 检查
		fileCheck(filePath);
		Workbook workbook = null;
		workbook = getWorkbook(filePath);

		Sheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			throw new Exception("获取Excel文件失败");
		}
		// 首行索引
		int firstRowIndex = sheet.getFirstRowNum();
		// 最后一行
		int lastRowIndex = sheet.getLastRowNum();

		// 读取数据行
		for (int rowIndex = 3; rowIndex <= lastRowIndex - 1; rowIndex++) {
			// 当前行
			Row currentRow = sheet.getRow(rowIndex);
			AliPayInfo payInfo = new AliPayInfo();
			payInfo.setOrderCreateTime(getCellValue(currentRow.getCell(1), true));
			if(StringUtils.isBlank(currentRow.getCell(2).getStringCellValue())){
				continue;
			}
			payInfo.setOrderNo(getCellValue(currentRow.getCell(2), true));
			payInfo.setGoodsName(getCellValue(currentRow.getCell(3), true));
			if(StringUtils.isNotBlank(currentRow.getCell(4).getStringCellValue())){
				payInfo.setOrderAmount(Double.valueOf(getCellValue(currentRow.getCell(4), true)));
			}
			payInfo.setPayState(getCellValue(currentRow.getCell(5), true));
			payInfo.setMerchantAccount(getCellValue(currentRow.getCell(6), true));
			payInfo.setMerchantName(getCellValue(currentRow.getCell(7), true));
			payInfo.setTransactionNo(getCellValue(currentRow.getCell(8), true));
			payInfo.setTradeMark(getCellValue(currentRow.getCell(9), true));
			if(StringUtils.isNotBlank(currentRow.getCell(10).getStringCellValue())){
				payInfo.setDiscounts(Double.valueOf(getCellValue(currentRow.getCell(10), true)));
			}
			if(StringUtils.isNotBlank(currentRow.getCell(11).getStringCellValue())){
				payInfo.setRefundAmount(Double.valueOf(getCellValue(currentRow.getCell(11), true)));
			}
			if(StringUtils.isNotBlank(currentRow.getCell(13).getStringCellValue())){
				payInfo.setServiceFee(Double.valueOf(getCellValue(currentRow.getCell(13), true)));
			}
			payInfo.setPaymentChannel(getCellValue(currentRow.getCell(14), true));
			infoList.add(payInfo);
		}
		return infoList;
	}



	/**
	 * 根据Excel获取支付宝对账明细
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<AliBillingDetails> getAliBillingDetailsByExcel(String filePath) throws Exception {
		List<AliBillingDetails> infoList = new ArrayList<>();
		// 检查
		fileCheck(filePath);
		Workbook workbook = null;
		workbook = getWorkbook(filePath);

		Sheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			throw new Exception("获取Excel文件失败");
		}
		// 首行索引
		int firstRowIndex = sheet.getFirstRowNum();
		// 最后一行
		int lastRowIndex = sheet.getLastRowNum();

		// 读取数据行
		for (int rowIndex = 3; rowIndex <= lastRowIndex - 1; rowIndex++) {
			// 当前行
			Row currentRow = sheet.getRow(rowIndex);
			AliBillingDetails billingDt = new AliBillingDetails();
			billingDt.setBillTime(getCellValue(currentRow.getCell(1), true));
			if(StringUtils.isBlank(currentRow.getCell(2).getStringCellValue())){
				continue;
			}
			billingDt.setTransactionNo(getCellValue(currentRow.getCell(2), true));
			billingDt.setSerialNo(getCellValue(currentRow.getCell(3), true));
			billingDt.setOrderNo(getCellValue(currentRow.getCell(4), true));
			billingDt.setPayType(getCellValue(currentRow.getCell(5), true));
			if (StringUtils.isNotBlank(currentRow.getCell(6).getStringCellValue())) {
				billingDt.setIncome(Double.valueOf(getCellValue(currentRow.getCell(6), true)));
			}
			if (StringUtils.isNotBlank(currentRow.getCell(7).getStringCellValue())) {
				billingDt.setExpend(Double.valueOf(getCellValue(currentRow.getCell(7), true)));
			}
			if (StringUtils.isNotBlank(currentRow.getCell(8).getStringCellValue())) {
				billingDt.setBalance(Double.valueOf(getCellValue(currentRow.getCell(8), true)));
			}
			if (StringUtils.isNotBlank(currentRow.getCell(9).getStringCellValue())) {
				billingDt.setServiceFee(Double.valueOf(getCellValue(currentRow.getCell(9), true)));
			}
			billingDt.setPaymentChannel(getCellValue(currentRow.getCell(10), true));

			billingDt.setMerchantAccount(getCellValue(currentRow.getCell(12), true));
			billingDt.setMerchantName(getCellValue(currentRow.getCell(13), true));

			billingDt.setGoodsName(getCellValue(currentRow.getCell(15), true));
			billingDt.setRemark(getCellValue(currentRow.getCell(16), true));

			infoList.add(billingDt);
		}
		return infoList;
	}

	public static String cutOneByte(String str) {
        if (str != null && !str.equals("")) {
            byte[] a = str.getBytes();// 转化为字节流处理,去掉最前面一个字节，以防止第一个字母乱码
            byte[] b = new byte[a.length - 1];
            for (int i = 1; i < a.length; i++) {
                b[i - 1] = a[i];
            }
            str = new String(b);
        }
        return str;
    }

}
