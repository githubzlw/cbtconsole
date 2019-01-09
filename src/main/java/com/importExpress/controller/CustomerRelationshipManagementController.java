package com.importExpress.controller;


import com.cbt.admuser.service.AdmuserService;
import com.cbt.bean.Orderinfo;
import com.cbt.bean.TabTransitFreightinfoUniteNew;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.pojo.Admuser;
import com.cbt.processes.dao.IUserDao;
import com.cbt.report.service.TabTransitFreightinfoUniteNewExample;
import com.cbt.util.Utility;
import com.cbt.website.util.JsonResult;
import com.importExpress.mail.TemplateType;
import com.importExpress.pojo.AdminRUser;
import com.importExpress.pojo.AdminRUserExample;
import com.importExpress.pojo.UserBean;
import com.importExpress.service.AdminRUserServiece;
import com.importExpress.service.OrderSplitService;
import com.importExpress.service.ReorderService;
import com.importExpress.service.SendChaPsendEmailService;
import com.importExpress.utli.FreightUtlity;
import com.importExpress.utli.RedisModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

/**
 * *****************************************************************************************
 *
 * @ClassName CustomerRelationshipManagementController
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2018/10/10 9:44
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       9:442018/10/10     cjc                       初版
 * ******************************************************************************************
 */
@Controller
@RequestMapping("/customerRelationshipManagement")
public class CustomerRelationshipManagementController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerRelationshipManagementController.class);
    @Autowired
    private AdminRUserServiece adminRUserServiece;
    @Autowired
    private AdmuserService admuserService;
    @Autowired
    private  IOrderinfoService iOrderinfoService;
    @Autowired
    private OrderSplitService orderSplitService;
    @Autowired
    private ReorderService reorderService;
    @Autowired
    private SendChaPsendEmailService sendChaPsendEmailService;
    /**
     * @Title: queryTheNumCustomersUnderSaler
     * @Author: cjc
     * @Despricetion:TODO 查询出来当前销售下有多少用户
     * @Date: 2018/10/10 10:50
     * @Param: [userId]
     * @Return: int
     */
    @RequestMapping("/queryTheNumCustomersUnderSaler")
    @ResponseBody
    public int queryTheNumCustomersUnderSaler(int userId){
        AdminRUserExample example = new AdminRUserExample();
        AdminRUserExample.Criteria criteria = example.createCriteria();
        criteria.andAdminidEqualTo(userId);
        int count = adminRUserServiece.countByExample(example);
        return count;
    }
    @RequestMapping("/updateSalerToCustomer")
    @ResponseBody
    public int updateSalerToCustomer(int salerOldId, int salerNewId){
        AdminRUserExample example = new AdminRUserExample();
        AdminRUserExample.Criteria criteria = example.createCriteria();
        criteria.andAdminidEqualTo(salerOldId);
        AdminRUser adminRUser = new AdminRUser();
        Admuser admuser = admuserService.selectByPrimaryKey(salerNewId);
        adminRUser.setAdmname(admuser.getAdmname());
        adminRUser.setAdminid(salerNewId);
        int count = adminRUserServiece.updateByExampleSelective(adminRUser, example);
        return  count;
    }
    @RequestMapping("/queryAllSaler")
    @ResponseBody
    public List<Admuser> queryAllSaler(HttpServletRequest request, HttpServletResponse response){
        List<Admuser> admusers = admuserService.selectAdmuser();
        request.setAttribute("admusersList",admusers);
        return admusers;
    }
    @RequestMapping("/orderinfoShippingMehtodRfresh")
    public void orderinfoShippingMehtodRfresh(HttpServletRequest request, HttpServletResponse response){
        //第一步从数据库中查出所有 运输方式为空的订单
        List<Orderinfo> allOrderShippingMehtodIsNull = iOrderinfoService.getAllOrderShippingMehtodIsNull();
       /* IZoneServer zs = new ZoneServer();
        List<ZoneBean> zonelist = zs.getAllZone();*/

        for (int i = 0; i < allOrderShippingMehtodIsNull.size(); i++) {
            Orderinfo orderinfo =  allOrderShippingMehtodIsNull.get(i);
            String modeTransport = orderinfo.getModeTransport();
            if(modeTransport != null){
                String countryNameEn = modeTransport.split("@")[2];
                String shippingTime = modeTransport.split("@")[1];
                int countryid = iOrderinfoService.getCountryIdByName(countryNameEn);
                //第2步 根据国家id 运输方式 获取相关运费信息
                TabTransitFreightinfoUniteNewExample example = new TabTransitFreightinfoUniteNewExample();
                TabTransitFreightinfoUniteNewExample.Criteria criteria = example.createCriteria();
                criteria.andCountryidEqualTo(countryid);
                criteria.andShippingTimeEqualTo(shippingTime);
                List<TabTransitFreightinfoUniteNew> list = iOrderinfoService.selectByExample(example);
                if(list.size()<=0){
                    TabTransitFreightinfoUniteNewExample example1 = new TabTransitFreightinfoUniteNewExample();
                    TabTransitFreightinfoUniteNewExample.Criteria criteria1 = example1.createCriteria();
                    criteria1.andCountryidEqualTo(999);
                    criteria.andShippingTimeEqualTo(shippingTime);
                    list = iOrderinfoService.selectByExample(example1);
                }
                if(list.size() > 0){
                    String transportMode = list.get(0).getTransportMode();
                    String substring = modeTransport.substring(modeTransport.indexOf("@"));
                    modeTransport = transportMode + substring;
                    //去从新更新运输方式信息
                    int i1 = iOrderinfoService.updateOrderinfomodeTransport(modeTransport, orderinfo.getOrderNo());
                    System.out.println("订单："+orderinfo.getOrderNo() +" 更新条数："+i1);
                }
            }

        }
    }
    @RequestMapping(value = "/reorderTrue")
    @ResponseBody
    public String reorderTrue(String orderNo,String  userId) throws Exception {
        String message = reorderService.reorder(orderNo, userId);
        //清除redis 里面数据
        //2.清空redis数据
        //使用MQ清空购物车数据
        //redis示例
        SendMQ sendMQ = null;
        String userIdStr = String.valueOf(userId);
        sendMQ = new SendMQ();
        sendMQ.sendMsg(new RedisModel(new String[]{userIdStr}));
        sendMQ.closeConn();
        return message;
    }
    @RequestMapping(value = "/reorder")
    public String reorder(String orderNo,HttpServletRequest request) throws Exception {
        request.setAttribute("orderNo",orderNo);
        return "reorder";
    }
    @ResponseBody
    @RequestMapping(value = "/sendChaPsendEmail")
    public JsonResult sendChaPsendEmail(HttpServletRequest request, HttpServletResponse response){
        String emailInfo=request.getParameter("emailInfo");
        String email=request.getParameter("email");
        String copyEmail=request.getParameter("copyEmail");
        String orderNo=request.getParameter("orderNo");
        String userId=request.getParameter("userId");
        String title = request.getParameter("title");
        String reason3 = request.getParameter("reason3");
        JsonResult result = sendChaPsendEmailService.sendChaPsendEmail( emailInfo,  email,  copyEmail,  orderNo,  userId,  title ,  reason3);
        return result;
    }
}
