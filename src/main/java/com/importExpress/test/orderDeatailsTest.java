package com.importExpress.test;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.controller.PurchaseController;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.warehouse.ctrl.NewOrderDetailsCtr;
import com.cbt.warehouse.ctrl.UserLoginController;
import com.cbt.website.util.JsonResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * 采购详情页面和订单详情页面JUNIT测试类
 * 王宏杰 2018-09-20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml","classpath:spring-mvc.xml","classpath:applicationContext-shiro.xml"})
public class orderDeatailsTest {
	MockHttpServletRequest request;
	MockHttpServletResponse response;
	@Autowired
	private IOrderinfoService iOrderinfoService;
	@Autowired
	private NewOrderDetailsCtr newOrderDetailsCtr;
	@Autowired
	private PurchaseController purchaseController;
	@Autowired
	private UserLoginController userLoginController;
	@Before
	public void setUp() throws Exception {
		request=new MockHttpServletRequest();
		response=new MockHttpServletResponse();
	}
	/**
	 * 订单详情页面打开数据是否报错junit测试
	 */
	@Test
	public void testDeleteByExample() {
		//最近半年的订单
		List<String> orderList=iOrderinfoService.getOrderIdList();
		for(String orderNo:orderList){
			System.out.println("orderNo=="+orderNo);
			request.setParameter("orderNo",orderNo);
			newOrderDetailsCtr.queryByOrderNo(request,response);
			List<OrderDetailsBean> odb=(List<OrderDetailsBean>)request.getAttribute("orderDetail");
			Assert.assertTrue(odb.size()>0);
		}
	}

	/**
	 * 采购详情页面打开数据是否报错junit测试
	 */
	@Test
	public void testPurchaseByExample() {
		request.setParameter("userName","ling");
		request.setParameter("passWord","ling123");
		JsonResult json=userLoginController.checkUserInfo(request,response);
		if(json.isOk()){
			//最近半年的订单
			List<String> orderList=iOrderinfoService.getOrderIdList();
			for(String orderNo:orderList){
				System.out.println("orderNo=="+orderNo);
				request.setParameter("pagenum","1");
				request.setParameter("orderid","");
				request.setParameter("admid","0");
				request.setParameter("userid","");
				request.setParameter("orderno",orderNo);
				request.setParameter("goodid","");
				request.setParameter("date","");
				request.setParameter("days","");
				request.setParameter("state","");
				request.setParameter("unpaid","0");
				request.setParameter("pagesize","50");
				request.setParameter("search_state","0");
				purchaseController.queryPurchaseInfo(request,response);
				List odb=(List)request.getAttribute("pblist");
				Assert.assertTrue(odb.size()>0);
			}
		}else{
			Assert.assertTrue(1>2);
		}
	}
}