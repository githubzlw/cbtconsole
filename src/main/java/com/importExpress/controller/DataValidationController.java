package com.importExpress.controller;

import com.cbt.paypal.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * *****************************************************************************************
 *
 * @ClassName DataValidationController
 * @Author: cjc
 * @Descripeion 自动化校验各种数据
 * @Date： 2019/5/22 11:22:02
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       11:22:022019/5/22     cjc                       初版
 * ******************************************************************************************
 */
@RequestMapping("/checkData")
@Controller
public class DataValidationController {
    @Autowired
    com.importExpress.service.CheckGoodsCarDataService checkGoodsCarDataService;
    @RequestMapping("/checkGoodsCarData")
    public String checkGoodsCarData(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = checkGoodsCarDataService.checkGoodsCarData();
        request.setAttribute("data",responseData);
        return "checkoutGoodsCar";
    }
}
