package com.cbt.customer.servlet;

import com.cbt.customer.dao.IPictureComparisonDao;
import com.cbt.customer.dao.PictureComparisonDaoImpl;
import com.cbt.util.StrUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class UpDataServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	IPictureComparisonDao dao = new PictureComparisonDaoImpl();
	private DecimalFormat format = new DecimalFormat("#0.00");
	//MAX(最近速卖通历史免邮价，当前速卖通免邮价) 除以 (1688价格*1.1 + 运费) > 3.5
	public void updateDelFlagMax(HttpServletRequest request, HttpServletResponse response) {
		
		int count = Integer.valueOf(request.getParameter("count"));
		String startid = request.getParameter("startid");
		String initid = "";
		try {
			for(int i=0;i<count;i++){
				if(initid.equals(startid)){
					break;
				}
				List<Map<String,String>> mapList = dao.getDataForDel(startid);
				initid = startid;
				if(mapList == null || mapList.isEmpty()){
					break;
				}
				for(Map<String,String> map : mapList){
					//数据表id
					startid = map.get("id");
//					1.①免邮价大于  2000美元 的商品  && ②MOQ*单价（MOQ时） >2000美元的商品 && ③单件重量 > 21 KG 的商品
					String wprice = map.get("wprice");
					wprice = StrUtils.matchStr(wprice, "(\\d+\\.\\d+)");
					double price  = 0;
					if(StrUtils.isMatch(wprice, "(\\d+\\.\\d+)")){
						price = Double.valueOf(wprice);
					}else{
						String range_price = map.get("range_price");
						range_price = StringUtils.indexOf(range_price, "-") > -1? range_price.split("-")[1] : map.get("price");
						price = Double.valueOf(range_price);
					}
					
					String ali_pid = map.get("ali_pid");
					double aliPrice = 0;
					double hisPrice = 0;
					//对标ali数据
					if(StringUtils.isNotBlank(ali_pid) && StrUtils.isRangePrice(map.get("ali_price"))){
						String aliexpressHistory = dao.getAliexpressHistory(ali_pid);
						map.put("ali_history_price", aliexpressHistory);
						aliexpressHistory = StrUtils.isRangePrice(aliexpressHistory) ?aliexpressHistory : "0";
						hisPrice = Double.valueOf(aliexpressHistory.split("-")[0]);
						//ali价格
						double ali_price = Double.valueOf(map.get("ali_price").split("-")[0]);
						aliPrice = Math.max(ali_price, hisPrice);
					}else{
						
					}
					String delFlag ="";
					//不同类别不同比例
					double rate  = StrUtils.getRate(map.get("catpath"));
					
					//4.C.	MAX(最近速卖通历史免邮价，当前速卖通免邮价) 除以 (1688价格*1.1 + 运费) > 2.5
					delFlag = price > 0.001 && aliPrice /price > rate ? delFlag+",6" : delFlag;
					map.put("rate", rate+"");
					map.put("price_1688", format.format(price));
					System.err.println(startid+"|pid:"+map.get("pid")+"|ali_pid:"+ali_pid+"|aliPrice:"+aliPrice+"|price:"+price);
					
					map.put("del_flag", delFlag);
					
					dao.insertDelFlag_New(map);
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(startid);
		}
		
	}
	
	
}
