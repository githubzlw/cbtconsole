package com.cbt.controller;

import com.cbt.pojo.ImgPojo;
import com.cbt.pojo.JsonResults;
import com.cbt.pojo.SizeChart;
import com.cbt.service.GetPriceService;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.ImgDownByOkHttpUtils;
import com.importExpress.utli.OKHttpUtils;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/get")
public class GetPriceController {
    /*@Autowired
    private GetPriceService getPriceService;
    private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
    private static final String OCR_URL = "http://192.168.1.251:5000/photo";
    private static final Log LOG = LogFactory.getLog(GetPriceController.class);
    @RequestMapping("/getImg")
    public JsonResults GetPrice(@RequestParam(value = "categoryId",defaultValue = "-1")int categoryId, @RequestParam(value = "page",defaultValue = "0")int page){
        JsonResults json=new JsonResults();
        if (categoryId==-1){
            json.setMessage(404);
           return json;
       }
        List<List<String>> imgUrl=this.getPriceService.FindAllImgUrl(categoryId,page);
       if (imgUrl.size()<1){
           json.setMessage(404);
           return json;
       }
       int totalpage=this.getPriceService.FindAllImgUrlCount(categoryId);
       json.setTotalpage(totalpage);
       json.setResults(imgUrl);
        return json;
    }
    @RequestMapping("/Category")
    public  List<ImgPojo> Category(){
        List<ImgPojo> Categorylist=this.getPriceService.FindCategory();
        if (Categorylist.size()<1){
            return null;
        }
        return Categorylist;
    }
    @RequestMapping("/SizeChart")
    public  JsonResults SizeChart(@RequestBody SizeChart sizeChartList){
        JsonResults json=new JsonResults();
        Boolean bo=this.getPriceService.AddSizeChartList(sizeChartList.getSizeChartList());
        if (bo){
            json.setMessage(200);
        }else {
            json.setMessage(500);
        }
        return json;
    }
    @RequestMapping("/gettranslationImg")
    public  JsonResults gettranslationImg(@RequestParam(value = "page",defaultValue = "0")int page){
        JsonResults json=new JsonResults();
        List<List<String>> imgs=this.getPriceService.FindAllTranslationImg(page);
        if (imgs.size()<1){
            json.setMessage(404);
            return json;
        }
        int totalpage=this.getPriceService.FindAllTranslationImgCount();
        json.setTotalpage(totalpage);
        json.setResults(imgs);
        return json;
    }
    @RequestMapping("/delImgurl")
    public  JsonResults delImgurl(@RequestBody SizeChart sizeChartList){
        JsonResults json=new JsonResults();
        if (sizeChartList.getSizeChartList().size()<1){
            json.setMessage(404);
            return json;
        }
       Boolean bo=this.getPriceService.delImgurlByList(sizeChartList.getSizeChartList());
        if (bo){
            json.setMessage(200);
        }else {
            json.setMessage(500);
        }
        return json;
    }

    @RequestMapping("/getchangeChineseImgToEnglishImg")
    public JsonResults changeChineseImgToEnglishImg(@RequestBody SizeChart sizeChartList) {
        JsonResults json = new JsonResults();
        if (sizeChartList.getSizeChartList().size()<1){
            json.setMessage(404);
        }
        List<List<String>> imgUrls=new ArrayList<>();
        List<String> imglist=new ArrayList<>();
       for (String imgUrl:sizeChartList.getSizeChartList()) {
           String url = this.getPriceService.changeChineseImgToEnglishImg(imgUrl);
           imglist.add(url);
       }
       if (imglist.size()>3){
           List<String> imglist2 = new ArrayList<>();
           imglist2.add(imglist.get(0));
           imglist2.add(imglist.get(1));
           imglist2.add(imglist.get(2));
           imglist2.add(imglist.get(3));
           imgUrls.add(imglist2);
           imglist.clear();
       }
        imgUrls.add(imglist);
       json.setResults(imgUrls);
       return json;
    }*/
    
}
