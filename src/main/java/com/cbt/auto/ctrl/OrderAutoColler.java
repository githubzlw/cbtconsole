package com.cbt.auto.ctrl;//package com.cbt.auto.ctrl;
//
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import sun.util.logging.resources.logging;
//
//import com.cbt.auto.service.IOrderAutoService;
//import com.cbt.auto.service.OrderAutoService;
//import com.cbt.bean.AdmDsitribution;
//import com.cbt.bean.OrderAutoDetail;
//import com.cbt.orderinfo.service.IOrderinfoService;
//import com.cbt.pojo.GoodsDistribution;
//import com.cbt.util.AppConfig;
//import com.cbt.util.Utility;
//
//@Controller
//@RequestMapping("/OrderAuto")
//public class OrderAutoColler {
//	@Autowired
//	private IOrderAutoService iOrderAutoService;
//   
//	@RequestMapping(value = "/getOrderAutoInfo.do", method = RequestMethod.GET)
//	public void getOrderAutoInfo(HttpServletRequest request, Model model) throws ParseException {
//		//获取pure_autoplan表的最大id
//		int maxId=iOrderAutoService.getMaxId();
//		//获取orderinfo表的记录从查出来的最大id开始拉去订单数据
//		List<OrderAutoBean> orderPayList=iOrderAutoService.getAllOrderAuto(maxId);
//		//筛选分配过采购的订单
//		List AllocatedOrderList=iOrderAutoService.getAllocatedOrder();
//		//获取未分配过的订单号返回还未分配过的订单号7
//		for(int j=0;j<orderPayList.size();j++){
//			OrderAutoBean oab=orderPayList.get(j);
//			if(AllocatedOrderList.contains(oab.getOrderno())){
//				orderPayList.remove(oab);
//			}
//		}
//		for(int k=0;k<orderPayList.size();k++){
//			OrderAutoBean oab=orderPayList.get(k);
//			String order=oab.getOrderno();
//			String adminId="";
//			String goodsDataId="";
//			String odId="";
//			Date date = new Date();
//			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			String time = df1.format(date);
//			Timestamp CreateDate = Timestamp.valueOf(time);
//			//根据订单号找到对应的商品ID
//			try{
//				if(AppConfig.isAuto){//是否采用自动分配
//					List orderDetailInfo=iOrderAutoService.getAllOrderAutoInfo(order);
//					System.out.println(orderDetailInfo.size());
//					for(int i=0;i<orderDetailInfo.size();i++){
//						String goodid=orderDetailInfo.get(i).toString();
//						System.out.println("订单号:"+order+"\t订单对应的商品ID："+goodid);
//						//根据商品ID查询goods_distribution表看是否该商品有过对应的采购人员，如果有就将该采购人员分配到该商品上如果没有就另查找
//						List admuserid=iOrderAutoService.getGoodsDistributionCount(goodid);
//						if(admuserid!=null && admuserid.size()>0){//以前被分配过采购人员采购该商品
//							System.out.println("该商品是否被分配过的采购人员:"+admuserid.get(0));
//							List<OrderAutoDetail> list=iOrderAutoService.getOrderDetail(goodid, order);
//							if(list.size()>0){
//								OrderAutoDetail oad=list.get(0);
//								adminId=admuserid.get(0).toString();
//								goodsDataId=oad.getGoodsdataid();
//								odId=oad.getOdid();
//							}else{
//								System.out.println("没有找到该工单对应商品的信息");
//							}
//						}else{//该商品是新商品还没有被分配过采购人员
//							List<AdmDsitribution> adList=iOrderAutoService.getALLAdmDsitribution();//按照采购人员分组查找所有采购人员未完成的采购数量
//							List userList=iOrderAutoService.getAllAdmUser();//查找所有采购人员名单
//							List<OrderAutoDetail> detailList=iOrderAutoService.getOrderDetail(goodid,order);
//							boolean flag=true;
//							List list=new ArrayList();
//							for(int t=0;t<adList.size();t++){
//								list.add(String.valueOf(adList.get(t).getAdid()));
//							}
//							if(adList==null || adList.size()==0){//所有的采购人员均还没有采购任务
//								if(detailList!=null && detailList.size()>0){
//									adminId=userList.get(0).toString();
//									goodsDataId=detailList.get(0).getGoodsdataid();
//									odId=detailList.get(0).getOdid();
//								}else{
//									System.out.println("没有找到商品商业的明细信息");
//								}
//								flag=false;
//							}else{//一部分采购人员没有采购任务
//								for(int m=0;m<userList.size();m++){
//									if(!list.contains(userList.get(m).toString())){//如果某个采购人员还没有采购任务
//										if(detailList!=null && detailList.size()>0){
//											adminId=userList.get(m).toString();
//											goodsDataId=detailList.get(0).getGoodsdataid();
//											odId=detailList.get(0).getOdid();
//										}else{
//											System.out.println("没有找到商品商业的明细信息");
//										}
//										flag=false;
//										break;
//									}
//								}
//							}
//							//如果每个采购人员都有采购任务
//							if(flag){
//								int admuserId=CalculationMin(adList);
//								if(detailList!=null && detailList.size()>0 && admuserId!=-99){
//									adminId=String.valueOf(admuserId);
//									goodsDataId=detailList.get(0).getGoodsdataid();
//									odId=detailList.get(0).getOdid();
//								}else{
//									System.out.println("没有找到商品商业的明细信息");
//								}
//							}
//						}
//						GoodsDistribution gd=new GoodsDistribution();
//						gd.setOdid(odId);//order_detail id
//						gd.setAdmuserid(adminId);//admuser id
//						gd.setGoodsdataid(goodsDataId);//
//						gd.setGoodsid(goodid);//商品id
//						gd.setCreateTime(CreateDate);//创建记录时间
//						gd.setOrderid(order);//工单号
//						gd.setDistributionid("0");//0代表自动分配1代表手动分配
//						int row=iOrderAutoService.insertDG(gd);
//						if(row>0){
//							System.out.println("分配采购新增成功!!!!");
//							//新增pure_autoplan记录
//							PureAutoPlanBean pap=new PureAutoPlanBean();
//							pap.setAutoState(1);
//							pap.setAutitime(CreateDate);
//							pap.setOrderid(String.valueOf(oab.getOrderid()));
//							pap.setOrderno(order);
//							pap.setPaystatus(oab.getPaystaus());
//							pap.setPaytime(oab.getPaytime());
//							int rowPap=iOrderAutoService.insertPap(pap);
//							if(rowPap>0){
//								System.out.println("分配采购计划新增成功!!!!");
//							}else{
//								System.out.println("分配采购计划新增失败!!!!");
//							}
//						}else{
//							System.out.println("分配采购新增失败");
//						}
//					}
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	}
//	/**
//	 * 获取最少采购数量的采购人员
//	 * @param sampleList
//	 * @return
//	 * @author whj
//	 */
//	public int CalculationMin(List<AdmDsitribution> sampleList){
//		   int index=-99;
//	           try{
//	               int totalCount = sampleList.size();
//	               if (totalCount >= 1){
//	                     int min = sampleList.get(0).getCount();
//	                     for (int i = 0; i < totalCount; i++)
//	                     {
//	                        int temp = sampleList.get(i).getCount();
//	                         if (min >= temp){
//	                             min = temp;
//	                             index=i;
//	                        }
//	                     } 
//	               }
//	          }catch (Exception ex){
//	                 ex.printStackTrace();
//	          }
//	           return sampleList.get(index).getAdid();
//	    }
//}
