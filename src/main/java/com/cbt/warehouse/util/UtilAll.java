package com.cbt.warehouse.util;

import com.cbt.pojo.TaoBaoOrderInfo;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UtilAll {
	
	// 判读字符串是为null 为空
	public boolean isStringNull(String str) {
		if ("".equals(str) || str == null) {
			return true;
		} else {
			return false;
		}
	}
	
	// 打印不干纸
	public static BufferedImage printDryPaper(List<String> list, String shipmentno,
			BufferedImage img1, BufferedImage img2, BufferedImage img3, String userid,
			String number, String typeNum, String gjCode,String imgName) {
		try {
		//	JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(),
		//			WidthCodedPainter.getInstance(),
		//			BaseLineTextPainter.getInstance());
			
			JBarcode localJBarcode = new JBarcode(
					Code128Encoder.getInstance(),
					WidthCodedPainter.getInstance(),
					BaseLineTextPainter.getInstance());

			//设置localJBarcode  一些属性
			localJBarcode.setXDimension(Double.valueOf(0.6).doubleValue());  // 设置尺寸，大小 密集程度 
			BufferedImage jbCodeImg = localJBarcode.createBarcode(shipmentno);
			
			
	//		ImageIO.write(jbCodeImg, "jpg", new File("D:\\images\\hh123.png"));
			int n = 150; // 图片宽度
			// int sumWidth =
			// img1.getWidth()+img2.getWidth()+img3.getWidth()+jbCodeImg.getWidth()+10;
			int sumWidth = n * 4 + 30; // 图片总宽度
			int jbw2 = 150; // 额外多加宽度
			int bufWidth = sumWidth + 100;
			int bufHeight = 250; // 主图片宽度

			BufferedImage buffImg = new BufferedImage(bufWidth, bufHeight,
					BufferedImage.TYPE_INT_RGB);
		

			int jbWidth = (int) (jbCodeImg.getWidth());
			int jbHeight = (int) (jbCodeImg.getHeight());

			//
			int startLeft = 5; // 左边开始位置
			int startG = 20; // 左边开始位置

			Graphics2D g2 = (Graphics2D) buffImg.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setBackground(Color.WHITE);// 设置背景色
			g2.clearRect(0, 0, bufWidth, bufHeight);// 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

			//设置图片位置
			g2.drawImage(img1, n + jbw2, startLeft, null);
			g2.drawImage(img2, n * 2 + jbw2, startLeft, null);
			g2.drawImage(img3, n * 3 + jbw2, startLeft, null);

			g2.setColor(Color.BLACK);
			Font f = new Font("宋体", Font.BOLD, 35);
			g2.setFont(f);
			//设置用户id位置
			g2.drawString(userid, startLeft, 80);

			Font f2 = new Font("宋体", Font.CENTER_BASELINE, 20); 
			g2.setFont(f2);
			//设置type weizhi
			g2.drawString(typeNum + "种  " + number + "件 " + gjCode,
					startLeft + 100, 80);

			// 添加订单号
			int tempHeight = 150;
			for (int i = 0; i < list.size(); i += 3) {
				String temp = "";
				for (int j = 0; (j + i) < list.size() && j < 3; j++) {
					temp += list.get(i + j) + ",";
				}
				g2.drawString(temp, startLeft, tempHeight);
				tempHeight += 20;
			}

			//设置条形码位置
			g2.drawImage(jbCodeImg, startG, tempHeight+10, jbWidth, jbHeight, null);
			
			g2.dispose();

	//		ImageIO.write(buffImg, "jpg", new File(imgName));
			return buffImg;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
	
	// 打印不干纸
		public static BufferedImage print(List<BufferedImage> list,String imgName) {
			try {
				int bufWidth = 750;
				int bufHeight = 270*4; // 主图片宽度
				
//				for(int i=0; i<list.size(); i++){
//					
//					BufferedImage bi = list.get(i);
//					bufHeight += bi.getHeight()+20;
//					bufWidth = bi.getWidth()+20;                                                                   
//				}
                                                                    
				BufferedImage buffImg = new BufferedImage(bufWidth, bufHeight,
						BufferedImage.TYPE_INT_RGB);
				
				Graphics2D g2 = (Graphics2D) buffImg.getGraphics();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setBackground(Color.WHITE);// 设置背景色
				g2.clearRect(0, 0, bufWidth, bufHeight);// 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
				int tg = 0;
				for(int i=0; i<list.size(); i++){
					BufferedImage bi = list.get(i);
					g2.drawImage(bi,10, 270*i, null);                                  
				}
				                                                                                                                              
                                      
			//			ImageIO.write(buffImg, "jpg", new File(imgName));
			//			printImg(buffImg);
						return buffImg;                                                     
						                                       
			} catch (Exception localException) {
				localException.printStackTrace();
			}
			return null;
		}


	public static BufferedImage getImageBytes2NetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			return readInputStream(inStream, 1024);// 得到图片的二进制数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage readInputStream(InputStream inStream,
			int fileLength) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[fileLength];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		ByteArrayInputStream in = new ByteArrayInputStream(
				outStream.toByteArray()); // 将b作为输入流；
		BufferedImage image = ImageIO.read(in);
		return image;
	}

	public static BufferedImage zoomOutImage(BufferedImage originalImage) {

		// int width = originalImage.getWidth()/times;

		// int height = originalImage.getHeight()/times;

		int width = 150;

		int height = 170;

		BufferedImage newImage = new BufferedImage(width, height,
				originalImage.getType());

		Graphics g = newImage.getGraphics();

		g.drawImage(originalImage, 0, 0, width, height, null);

		g.dispose();

		return newImage;

	}
	//操作记录
	public static void printBufInfo (String writeStr) {
		
		try{//     
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "buginfo.txt";
			System.out.println(path);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path), true));
			//BufferedWriter writer = new BufferedWriter(new FileWriter(new File("../../../buginfo.txt"), true));

			//时间
			Date nowTime=new Date(); 
			System.out.println(nowTime); 
			SimpleDateFormat time=new SimpleDateFormat("yyyy MM dd HH mm ss"); 
		//	System.out.println(); 
			
		//    writer.write(writeStr);
		     
		    writer.close();

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void printImg(BufferedImage buffImg){
		  
        //设置打印数据的格式，此处为图片gif格式  
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.JPEG;  
        //创建打印数据  
//      DocAttributeSet docAttr = new HashDocAttributeSet();//设置文档属性  
//      Doc myDoc = new SimpleDoc(psStream, psInFormat, docAttr);  
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        try {
			ImageIO.write(buffImg, "jpg", os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
                                          
        InputStream is = new ByteArrayInputStream(os.toByteArray());  
        Doc myDoc = new SimpleDoc(is, psInFormat, null);  
          
        //设置打印属性  
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();  
        aset.add(new Copies(1));//打印份数，3份  
          
        //查找所有打印服务  
        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);  
  
        //打印机包含字符串
 //       String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "cbt.properties";
        InputStream ins = UtilAll.class.getResourceAsStream("../../../../cbt.properties");
		Properties p = new Properties();
		try {
			p.load(ins);        
		} catch (Exception e) {
			e.printStackTrace();
		}
		String dyjName = p.getProperty("Printer");
		
        // this step is necessary because I have several printers configured  
        //将所有查找出来的打印机与自己想要的打印机进行匹配，找出自己想要的打印机  
        PrintService myPrinter = null;  
        for (int i = 0; i < services.length; i++) {  
            System.out.println("service found: " + services[i]);  
            String svcName = services[i].toString();  
            
            if (svcName.contains(dyjName) || svcName.contains("192.168.1.55")) {  
                myPrinter = services[i];  
            	 System.out.println("my printer found: " + svcName);  
                 System.out.println("my printer found: " + myPrinter);  
                break;  
            }  
          
        }  
  
        //可以输出打印机的各项属性  
//        AttributeSet att = myPrinter.getAttributes();  
//  
//        for (Attribute a : att.toArray()) {  
//  
//            String attributeName;  
//            String attributeValue;  
//  
//            attributeName = a.getName();  
//            attributeValue = att.get(a.getClass()).toString();  
//  
//            System.out.println(attributeName + " : " + attributeValue);  
//        }  
  
        if (myPrinter != null) {  
            DocPrintJob job = myPrinter.createPrintJob();//创建文档打印作业  
            try {  
                job.print(myDoc, aset);//打印文档  
  
            } catch (Exception pe) {  
                pe.printStackTrace();  
            }  
        } else {  
            System.out.println("no printer services found");  
        } 
        
    }
	
	public static String fun(String urlString) {
		urlString = urlString.replaceAll(" ", "");
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(urlString);
			URLConnection uc = urlObject.openConnection();
			// uc.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), "GBK"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Document doc=null;
		String str = json.toString();
		return str;
	}


	//中文翻译英文
	public static String fun2(String val) {
		String ret = "";
		URL url = null;
		HttpURLConnection httpurlconnection = null;
		try {http://fanyi.youdao.com/openapi.do?keyfrom=A1AAA3A&key=976945089&type=data&doctype=xml&version=1.1&q=%E4%B8%AD%E5%9B%BD%E4%B8%8A%E6%B5%B7%E6%93%8D%E4%BD%9C%E4%B8%AD%E5%BF%83
			url = new URL(
					"http://fanyi.youdao.com/openapi.do?keyfrom=A1AAA3A&key=976945089&type=data&doctype=xml&version=1.1&q="+URLEncoder.encode(val,"UTF-8"));
			// 以post方式请求
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("POST");
		//	httpurlconnection.getOutputStream().write(val.getBytes());
			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();
			// 获取响应代码
			int code = httpurlconnection.getResponseCode();

			// 获取页面内容
			InputStream in = httpurlconnection.getInputStream();
			BufferedReader breader = new BufferedReader(
					new InputStreamReader(in, "utf-8"));

			String str = breader.readLine();
			while (str != null) {
				ret += str;
				str = breader.readLine();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpurlconnection != null) {
				httpurlconnection.disconnect();
			}
		}
		if("no query".equals(ret)){
			return val;
		}
		SAXBuilder sb = new SAXBuilder(false);
		StringReader read = new StringReader(ret);
		Document doc = null;
		// System.out.println(ret);

		try {
			doc = sb.build(read);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Element books = doc.getRootElement();
		List datatime = books.getChildren("translation");
		Iterator iterInfo = datatime.iterator();

		String ex = "";
		while (iterInfo.hasNext()) {
			Element book3 = (Element) iterInfo.next();
			ex = book3.getChildTextTrim("paragraph");
		}
		return ex;
	}

//	是否中文
	public static boolean isChineseChar(String str) {
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

	public static int getTbId(List<TaoBaoOrderInfo> tList,String skuOne,String skuTwo){
		for(TaoBaoOrderInfo t:tList){
			String sku=t.getSku();
			if(StringUtil.isNotBlank(sku) && sku.contains(" ") && StringUtil.isNotBlank(skuTwo)){
				String []skus=sku.split(" ");
				if(skus.length >= 2){
					if(skus[0].split("：")[1].equals(skuOne) && skus[1].split("：")[1].equals(skuTwo)){
						return t.getId();
					}
				}
			}else if(StringUtil.isNotBlank(sku) && StringUtil.isBlank(skuTwo)){
				String []skus=sku.split("：");
				if(skus.length >= 2){
					if(skus[1].equals(skuOne)){
						return t.getId();
					}
				}
			}
		}
		return 0;
	}

	public static void main(String[] args) {
//		String path = UtilAll.class.getResource("/").getPath();
//		System.out.println(path);
		String s="颜色：黑色 尺码：L";
		String ss []=s.split(" ");
		System.out.println("===");
	}

}
