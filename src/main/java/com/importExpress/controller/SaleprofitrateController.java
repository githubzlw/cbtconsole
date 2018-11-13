package com.importExpress.controller;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.Saleprofitrate;
import com.importExpress.pojo.SaleprofitrateExample;
import com.importExpress.pojo.SaleprofitrateExample.Criteria;
import com.importExpress.service.SaleprofitrateService;
import com.importExpress.utli.SaleProfitRateUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;

/** 
* @author 作者 E-mail: saycjc@outlook.com
* @version 创建时间：2018年4月24日 下午8:29:08 
* 类说明 
*/
@Controller
@RequestMapping("/saleprofitrate")
public class SaleprofitrateController{
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SaleprofitrateController.class);
    @Autowired
    private SaleprofitrateService saleprofitrateService; 
    @RequestMapping("/insertInfo")
    public void insertInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int uid = SaleProfitRateUtil.getUserId(request, response);
        String result = "";
        Saleprofitrate salePrifigeRate = new Saleprofitrate();
        salePrifigeRate.setGoodid(StringUtils.isNotBlank(request.getParameter("goodsId")) ? request.getParameter("goodsId") : "1");
        salePrifigeRate.setUserid(uid);
        salePrifigeRate.setSaleprice(new BigDecimal(StringUtils.isNotBlank(request.getParameter("salePrice")) ? Integer.valueOf(request.getParameter("salePrice")) : 0 ));
        salePrifigeRate.setSourceprice(new BigDecimal(StringUtils.isNotBlank(request.getParameter("SourcePrice")) ? Integer.valueOf(request.getParameter("SourcePrice")) : 0));
        logger.info("插入商品数据： 用户id:"+uid + " 商品id:"+salePrifigeRate.getGoodid());
        DataSourceSelector.set("localDataSource");
        int conunt = saleprofitrateService.insertSelective(salePrifigeRate);
        DataSourceSelector.restore();
        if(conunt>0) {
            result = "{\"status\":true,\"message\":\"添加成功！\"}";
        } else {
            result = "{\"status\":false,\"message\":\"添加失败！\"}";
        }
        PrintWriter out = response.getWriter();
        JSONObject jsonob = JSONObject.fromObject(result);
        out.print(jsonob);
        out.close();
    }
   /* @RequestMapping("/getInfo")
    public void getInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        int userId = StringUtils.isNotBlank(request.getParameter("userId")) ? Integer.valueOf(request.getParameter("userId")) : 0;
        SaleprofitrateExample example = new SaleprofitrateExample();
        Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userId);
        List<Saleprofitrate> list = saleprofitrateService.selectByExample(example);
        request.setAttribute("infoList", list);
    }*/
    @RequestMapping("/list")
    @ResponseBody
    public EasyUiJsonResult query(HttpServletRequest request, HttpServletResponse response){
        int uid = SaleProfitRateUtil.getUserId(request, response);
        EasyUiJsonResult json = new EasyUiJsonResult();
        String pageNumStr = request.getParameter("rows");
        int pageSize = 20;
        if (pageNumStr == null || "".equals(pageNumStr) || "0".equals(pageNumStr)) {
            json.setSuccess(false);
            json.setMessage("获取分页数失败");
            return json;
        } else {
            pageSize = Integer.valueOf(pageNumStr);
        }

        String stateNumStr = request.getParameter("page");
        int currentPage = 1;
        if (!(stateNumStr == null || "".equals(stateNumStr) || "0".equals(stateNumStr))) {
            currentPage = Integer.valueOf(stateNumStr);
        }
        
        SaleprofitrateExample example = new SaleprofitrateExample();
        Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(uid);
        DataSourceSelector.set("localDataSource");
        Map<String,Object> map = saleprofitrateService.getList(example, currentPage, pageSize,uid);
        DataSourceSelector.restore();
        json.setSuccess(true);
        json.setRows(map.get("list"));
        json.setTotal((Integer)map.get("count"));
        
        return json;
    }
}
 