package com.cbt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.cbt.bean.TypeBean;
import com.cbt.bean.ZoneBean;
import com.cbt.bean.*;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.customer.service.IShopUrlService;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.parse.bean.Set;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.*;
import com.cbt.pojo.EntypeBen;
import com.cbt.service.CategoryService;
import com.cbt.service.CustomGoodsService;
import com.cbt.service.SingleGoodsService;
import com.cbt.util.*;
import com.cbt.warehouse.pojo.HotCategory;
import com.cbt.warehouse.pojo.HotSellingGoods;
import com.cbt.warehouse.service.HotGoodsService;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.UploadByOkHttp;
import com.importExpress.pojo.*;
import com.importExpress.service.HotManageService;
import com.importExpress.thread.DeleteImgByMd5Thread;
import com.importExpress.utli.*;
import okhttp3.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/newProduct")
public class AddNewProductController {
    private static final Log LOG = LogFactory.getLog(AddNewProductController.class);
    private String rootPath = "F:/console/tomcatImportCsv/webapps/";
    private String localIP = "http://27.115.38.42:8083/";
    private String wanlIP = "http://192.168.1.27:8083/";
    private DecimalFormat format = new DecimalFormat("#0.00");
    private static List<String> kidsCatidList = new ArrayList<>();

    private static Map<String, String> pidMap = new HashMap<>();

    private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();

    // private static final String OCR_URL = "http://192.168.1.84:5000/photo";
    private static final String OCR_URL = "http://192.168.1.251:5000/photo";

    private Map<String, String> offLineMap = new HashMap<>();

    @Autowired
    private CustomGoodsService customGoodsService;

    @Autowired
    private IShopUrlService shopUrlService;
    @Autowired
    private HotGoodsService hotGoodsService;
    @Autowired
    private HotManageService hotManageService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SingleGoodsService singleGoodsService;

    @SuppressWarnings({"static-access", "unchecked"})
    @RequestMapping(value = "/addNewProduct", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addNewProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataSourceSelector.restore();
        // ModelAndView mv = new ModelAndView("customgoods_detalis");
        ModelAndView mv = new ModelAndView("new_customgoods_detalis");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("uid", 0);
            mv.addObject("message", "?????????");
            return mv;
        } else {
            mv.addObject("uid", user.getId());
            mv.addObject("roletype", user.getRoletype());
        }
        try {
            // ???????????????????????????
            int pid = customGoodsService.queryNewPid();

            // ??????1688?????????????????????
            CustomGoodsPublish goods = new CustomGoodsPublish();
            goods.setPid(String.valueOf(pid));
            String remotePath = ftpConfig.getLocalShowPath();
            goods.setRemotpath(remotePath);
            mv.addObject("text", "");
            mv.addObject("goods", goods);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("uid", 0);
            mv.addObject("message", e.getMessage());
        }
        return mv;
    }

    @SuppressWarnings({"static-access", "unchecked"})
    @RequestMapping(value = "/getNewProductDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getNewProductDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataSourceSelector.restore();
        // ModelAndView mv = new ModelAndView("customgoods_detalis");
        ModelAndView mv = new ModelAndView("new_customgoods_detalis");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("uid", 0);
            mv.addObject("message", "?????????");
            return mv;
        } else {
            mv.addObject("uid", user.getId());
            mv.addObject("roletype", user.getRoletype());
        }
        try {
            // ???????????????????????????
            String pid = request.getParameter("pid");

            // ??????1688?????????????????????
            CustomGoodsPublish goods = customGoodsService.queryNewGoodsDetails(pid);
            if (goods != null) {
                String regEx = "[\\[\\]]";
                if (StringUtils.isNotBlank(goods.getWprice())) {
                    goods.setWprice(goods.getWprice().replaceAll(regEx, "").replace("$", "@").trim());
                }
                if (StringUtils.isNotBlank(goods.getFree_price_new())) {
                    goods.setFree_price_new(goods.getFree_price_new().replaceAll(regEx, "").replace("$", "@").trim());
                }


                List<String> imgs = GoodsInfoUtils.deal1688GoodsImg(goods.getImg(),"");
                if (imgs.size() > 0) {

                    request.setAttribute("showimgs", JSONArray.toJSON(imgs));
                    String firstImg = imgs.get(0);

                    goods.setShowMainImage(firstImg.replace(".60x60.", ".400x400."));
                }

                HashMap<String, String> pInfo = GoodsInfoUtils.deal1688Sku(goods);
                request.setAttribute("showattribute", pInfo);
            }
            String text = GoodsInfoUtils.dealEnInfoImg(goods.getEninfo(), "");
            mv.addObject("text", text);
            mv.addObject("goods", goods);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("uid", 0);
            mv.addObject("message", e.getMessage());
        }
        return mv;
    }

    private void getTextByHtml(CustomGoodsPublish goods, GoodsBean algood, boolean isLocal) {
        // ????????????
        List<String> sensitiveWords = new ArrayList<String>();
        sensitiveWords.add("Aliexpress:");
        sensitiveWords.add("aliexpress:");
        sensitiveWords.add("alibaba:");
        sensitiveWords.add("Alibaba:");
        sensitiveWords.add("QQ:");
        sensitiveWords.add("qq:");
        sensitiveWords.add("??????:");
        sensitiveWords.add("weixin:");
        sensitiveWords.add("WeiXin:");
        sensitiveWords.add("Logistics:");
        sensitiveWords.add("logistics:");
        sensitiveWords.add("Ships From:");
        sensitiveWords.add("ships from:");

        // ???????????????????????????????????????infourl??????????????????????????????infourl??????????????????????????????
        if (!(algood.getInfourl() == null || "".equals(algood.getInfourl().trim()))) {
            String page = "";
            if (isLocal) {
                // ????????????
                Set set = new Set();
                page = DownloadMain.getJsoup("https:" + algood.getInfourl(), 1, set);
            } else {
                // ????????????
                page = DownloadMain.getContentClient(ContentConfig.CRAWL_ALI_INFO_URL + algood.getInfourl(), null);
            }
            if (page == null || page.isEmpty() || "".equals(page.trim()) || "httperror".equals(page.trim())) {
                System.err.println(algood.getInfourl() + ":????????????");
            } else {
                goods.setAliGoodsInfo(GoodsInfoUtils.dealAliInfoData(page.replaceAll("window.productDescription=\'", "")));
            }
            page = null;
        }
    }


    @RequestMapping("/querySkuByPid")
    public ModelAndView querySkuByPid(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("custom_sku_details");
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("success", 0);
            mv.addObject("message", "??????????????????");
            return mv;
        } else {
            mv.addObject("uid", user.getId());
        }
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            mv.addObject("success", 0);
            mv.addObject("message", "??????PID??????");
            return mv;
        } else {
            mv.addObject("pid", pid);
        }

        try {
            CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);

            // ???goods???entype??????????????????,????????????
            List<TypeBean> typeList = GoodsInfoUtils.deal1688GoodsType(goods, true);
            if (StringUtils.isNotBlank(goods.getSku_new())) {
                // List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
                List<ImportExSku> skuList = JSONArray.parseArray(goods.getSku_new(), ImportExSku.class);
                List<ImportExSkuShow> cbSkus = GoodsInfoUtils.combineSkuList(typeList, skuList);
                for (ImportExSkuShow exSku : cbSkus) {
                    if (StringUtils.isNotBlank(exSku.getSpecId())) {
                        String chType = customGoodsService.queryChTypeBySkuId(exSku.getSpecId());
                        if (StringUtils.isBlank(chType)) {
                            chType = "";
                        }
                        exSku.setChType(chType);
                    }
                }

                Collections.sort(cbSkus, Comparator.comparing(ImportExSkuShow::getEnType));
                mv.addObject("showSku", JSONArray.toJSON(cbSkus));

                Map<String, Object> typeNames = new HashMap<String, Object>();
                for (TypeBean tyb : typeList) {
                    if (!typeNames.containsKey(tyb.getTypeId())) {
                        typeNames.put(tyb.getTypeId(), tyb.getType());
                    }
                }
                mv.addObject("typeNames", typeNames);
                skuList.clear();
            }
            typeList.clear();
            mv.addObject("success", 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            mv.addObject("success", 0);
            mv.addObject("message", "????????????????????????" + e.getMessage());
        }
        return mv;
    }

    @RequestMapping("/saveSkuInfo")
    @ResponseBody
    public JsonResult saveSkuInfo(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        String skuStr = request.getParameter("sku");
        if (StringUtils.isBlank(skuStr)) {
            json.setOk(false);
            json.setMessage("??????sku??????");
            return json;
        }
        String volumeSkuStr = request.getParameter("volumeSku");
        if (StringUtils.isBlank(volumeSkuStr)) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        String singPriceStr = request.getParameter("singPrice");
        if (StringUtils.isBlank(singPriceStr)) {
            json.setOk(false);
            json.setMessage("???????????????????????????");
            return json;
        }
        String singFreePriceStr = request.getParameter("singFreePrice");
        if (StringUtils.isBlank(singFreePriceStr)) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }
        try {

            CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);
            List<TypeBean> typeList = GoodsInfoUtils.deal1688GoodsType(goods, true);
            /*JSONArray sku_json = JSONArray.fromObject(goods.getSku());
            List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);*/
            List<ImportExSku> skuList = JSONArray.parseArray(goods.getSku_new(), ImportExSku.class);
            String[] skuStrList = skuStr.split(";");
            String[] volumeSkuList = volumeSkuStr.split(";");
            String[] singPriceList = singPriceStr.split(";");
            String[] singFreePriceList = singFreePriceStr.split(";");
            double finalWeight = 0;


            List<GoodsWeightChange> changeList = new ArrayList<>(skuStrList.length * 2);
            Map<String, GoodsWeightChange> changeMap = new HashMap<>(skuStrList.length * 2);
            for (String singleSku : skuStrList) {
                String[] slSkuList = singleSku.split("@");
                String ppid = slSkuList[0].replace("_", ",");
                String pWeight = slSkuList[1];
                for (ImportExSku exSku : skuList) {
                    if (ppid.equals(exSku.getSkuPropIds())) {
                        finalWeight = BigDecimalUtil.truncateDouble(Float.parseFloat(pWeight), 3);
                        if (Math.abs(exSku.getFianlWeight() - finalWeight) < 0.01) {
                            GoodsWeightChange weightChange = new GoodsWeightChange();
                            weightChange.setSkuid(exSku.getSkuId());
                            weightChange.setAdminId(user.getId());
                            weightChange.setPid(pid);
                            weightChange.setWeight(pWeight);

                            changeMap.put(ppid, weightChange);
                            changeList.add(weightChange);
                        }
                        exSku.setFianlWeight(finalWeight);
                        break;
                    }
                }
            }
            double volumeWeight = 0;
            for (String volumeSku : volumeSkuList) {
                String[] vlSkuList = volumeSku.split("@");
                String ppid = vlSkuList[0].replace("_", ",");
                String pWeight = vlSkuList[1];
                for (ImportExSku exSku : skuList) {
                    if (ppid.equals(exSku.getSkuPropIds())) {
                        volumeWeight = BigDecimalUtil.truncateDouble(Float.parseFloat(pWeight), 3);
                        exSku.setVolumeWeight(volumeWeight);
                        if (changeMap.containsKey(ppid)) {
                            changeMap.get(ppid).setVolumeWeight(pWeight);
                        } else {
                            GoodsWeightChange weightChange = new GoodsWeightChange();
                            weightChange.setSkuid(exSku.getSkuId());
                            weightChange.setAdminId(user.getId());
                            weightChange.setPid(pid);
                            weightChange.setVolumeWeight(pWeight);
                            changeMap.put(ppid, weightChange);
                            changeList.add(weightChange);
                        }
                        break;
                    }
                }
            }

            // ????????????????????????????????????????????????
            float priceMin = 0;
            float priceMax = 0;
            float freeMin = 0;
            float freeMax = 0;

            float singlePrice = 0;
            for (String sPrice : singPriceList) {
                String[] priceList = sPrice.split("@");
                String ppid = priceList[0].replace("_", ",");
                String pPrice = priceList[1];
                for (ImportExSku exSku : skuList) {
                    if (ppid.equals(exSku.getSkuPropIds())) {
                        singlePrice = BigDecimalUtil.truncateFloat(Float.parseFloat(pPrice), 2);
                        if (priceMin == 0 || priceMin > singlePrice) {
                            priceMin = singlePrice;
                        }
                        if (priceMax < singlePrice) {
                            priceMax = singlePrice;
                        }
                        exSku.getSkuVal().setActSkuCalPrice(singlePrice);
                        if (changeMap.containsKey(ppid)) {
                            changeMap.get(ppid).setPrice(pPrice);
                        } else {
                            GoodsWeightChange weightChange = new GoodsWeightChange();
                            weightChange.setSkuid(exSku.getSkuId());
                            weightChange.setAdminId(user.getId());
                            weightChange.setPid(pid);
                            weightChange.setPrice(pPrice);
                            changeMap.put(ppid, weightChange);
                            changeList.add(weightChange);
                        }
                        break;
                    }
                }
            }


            float singleFreePrice = 0;
            for (String fPrice : singFreePriceList) {
                String[] priceList = fPrice.split("@");
                String ppid = priceList[0].replace("_", ",");
                String pPrice = priceList[1];
                for (ImportExSku exSku : skuList) {
                    if (ppid.equals(exSku.getSkuPropIds())) {
                        singleFreePrice = BigDecimalUtil.truncateFloat(Float.parseFloat(pPrice), 2);
                        if (freeMin == 0 || freeMin > singleFreePrice) {
                            freeMin = singleFreePrice;
                        }
                        if (freeMax < singleFreePrice) {
                            freeMax = singleFreePrice;
                        }
                        exSku.getSkuVal().setFreeSkuPrice(String.valueOf(singleFreePrice));
                        if (changeMap.containsKey(ppid)) {
                            changeMap.get(ppid).setPrice(pPrice);
                        } else {
                            GoodsWeightChange weightChange = new GoodsWeightChange();
                            weightChange.setSkuid(exSku.getSkuId());
                            weightChange.setAdminId(user.getId());
                            weightChange.setPid(pid);
                            weightChange.setPrice(pPrice);
                            changeMap.put(ppid, weightChange);
                            changeList.add(weightChange);
                        }
                        break;
                    }
                }
            }

            /*if(CollectionUtils.isNotEmpty(changeList)){
                for(GoodsWeightChange changeBean : changeList){
                    customGoodsService.saveGoodsWeightChange(changeBean);
                }
            }*/
            String range_price = null;
            if (priceMin > 0 && priceMax > 0) {
                if (priceMin == priceMax) {
                    range_price = String.valueOf(priceMin);
                } else {
                    range_price = priceMin + "-" + priceMax;
                }

            }
            String range_price_free = null;
            if (freeMin > 0 && freeMax > 0) {
                if (freeMin == freeMax) {
                    range_price_free = String.valueOf(freeMin);
                } else {
                    range_price_free = freeMin + "-" + freeMax;
                }
            }
            customGoodsService.updateGoodsSku(pid, goods.getSku(), skuList.toString(), user.getId(), finalWeight, range_price, range_price_free, Math.max(freeMin, 0));

            json.setOk(true);
            json.setMessage("???????????????????????????????????????????????????");
            skuList.clear();
            typeList.clear();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param content
     * @return
     * @author jxw
     * @date 2017-11-10
     */
    private String dealAliInfoData(String content) {

        Document nwDoc = Jsoup.parseBodyFragment(content);
        // ??????????????????????????? kse??????,??????div
        Elements divLst = nwDoc.getElementsByTag("div");
        for (Element dEl : divLst) {
            if (!(dEl.attr("name") == null || "".equals(dEl.attr("name").trim()))) {
                if ("productItem".equalsIgnoreCase(dEl.attr("name").trim())) {
                    dEl.remove();
                    continue;
                }
            }
            // ????????????div????????????a???????????????
            Elements aLst = dEl.getElementsByTag("a");
            if (aLst.size() > 0) {
                dEl.remove();
            }
        }
        // ??????????????? a??????
        Elements aLst = nwDoc.getElementsByTag("a");
        for (Element ael : aLst) {
            ael.remove();
        }

        // ??????????????? ????????????div??????
        nwDoc.select(".pnl-packaging-main").remove();
        // ??????????????? ??????????????????div??????
        nwDoc.select(".transaction-feedback-main").remove();
        // ??????????????? ???????????????????????????
        nwDoc.select(".related-products-main").remove();
        // ??????????????? ??????????????????
        nwDoc.select("#j-related-searches").remove();

        // ??????????????? img?????????????????????????????????
        Elements imgLst = nwDoc.getElementsByTag("img");
        for (Element imel : imgLst) {
            if (imel.hasAttr("alt")) {
                String attrVal = imel.attr("alt");
                if ("Shipping".equalsIgnoreCase(attrVal)) {
                    imel.remove();
                } else if ("Payment".equalsIgnoreCase(attrVal)) {
                    imel.remove();
                } else if ("Feedback".equalsIgnoreCase(attrVal)) {
                    imel.remove();
                } else if ("Contact us".equalsIgnoreCase(attrVal)) {
                    imel.remove();
                } else if ("Return".equalsIgnoreCase(attrVal)) {
                    imel.remove();
                }
            }
        }
        Elements ckImgLst = nwDoc.getElementsByTag("img");
        if (ckImgLst.size() == 0) {
            nwDoc = Jsoup.parseBodyFragment(content);
            // ??????????????? a??????
            Elements nwALst = nwDoc.getElementsByTag("a");
            for (Element nael : nwALst) {
                nael.remove();
            }
        }
        return nwDoc.html();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveEditDetalis")
    @ResponseBody
    public JsonResult saveEditDetalis(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");

        try {

            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("????????????????????????????????????");
                return json;
            }

            CustomGoodsPublish cgp = new CustomGoodsPublish();

            String newCatid =  request.getParameter("catid");

            cgp.setCatid1(newCatid);

            // ???????????????
            CategoryBean oldBean = categoryService.queryCategoryById(newCatid);
            if (oldBean == null) {
                json.setOk(false);
                json.setMessage("????????????ID");
            } else {

                cgp.setPathCatid(oldBean.getPath());
            }

            String contentStr = request.getParameter("content");
            if (!(contentStr == null || "".equals(contentStr))) {
                // ????????????
                cgp.setEninfo(contentStr);
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
                return json;
            }

            // String localpath = request.getParameter("localpath");

            String pidStr = request.getParameter("pid");
            if (!(pidStr == null || "".equals(pidStr))) {
                cgp.setPid(pidStr);
            } else {
                json.setOk(false);
                json.setMessage("??????pid??????");
                return json;
            }

            String enname = request.getParameter("enname");
            if (!(enname == null || "".equals(enname))) {
                cgp.setEnname(enname);
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
                return json;
            }

            String mainImg = request.getParameter("mainImg");
            if (!(mainImg == null || "".equals(mainImg))) {
                cgp.setShowMainImage(mainImg);
            } else {
                json.setOk(false);
                json.setMessage("?????????????????????");
                return json;
            }

            String goods_finalWeight = request.getParameter("goods_finalWeight");
            cgp.setFinalWeight(goods_finalWeight);

            String goods_volum_weight = request.getParameter("goods_volum_weight");
            cgp.setVolumeWeight(goods_volum_weight);



            String endetailStr = request.getParameter("endetail");
            if (!(endetailStr == null || "".equals(endetailStr))) {
                // ???????????????????????????????????????
                cgp.setEndetail("[" + endetailStr.replaceAll(";", ", ") + "]");
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
                return json;
            }

            String rangePrice = request.getParameter("rangePrice");

            if (rangePrice == null || "".equals(rangePrice)) {
                String wprice = request.getParameter("wprice");
                if (wprice == null || "".equals(wprice)) {
                   /* json.setOk(false);
                    json.setMessage("??????????????????????????????");
                    return json;*/
                } else {

                    String[] priceLst = wprice.split(",");
                    double price = Double.valueOf(priceLst[0].split("@")[1]);
                    for (String priceStr : priceLst) {
                        double tempPrice = Double.valueOf(priceStr.split("@")[1]);
                        if (tempPrice < price) {
                            price = tempPrice;
                        }
                    }
                    DecimalFormat df = new DecimalFormat("######0.00");
                    cgp.setPrice(df.format(price));
                    cgp.setWprice("[" + wprice.replace("@", " $ ") + "]");
                }
            } else {
                String sku = request.getParameter("sku");
                if (sku == null || "".equals(sku)) {
                    json.setOk(false);
                    json.setMessage("??????????????????????????????");
                    return json;
                }
            }

            String sellUtil = request.getParameter("sellUtil");
            if (StringUtils.isNotBlank(sellUtil)) {
                cgp.setSellUnit(sellUtil);
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
                return json;
            }



            String bizPrice = request.getParameter("bizPrice");
            if (StringUtils.isNotBlank(bizPrice) || "0".equals(bizPrice)) {
                cgp.setFpriceStr(bizPrice);
            } else {
                cgp.setFpriceStr("");
            }


            String rangePriceFree = request.getParameter("rangePriceFree");
            cgp.setRange_price_free_new(rangePriceFree);
            String gd_moq = request.getParameter("gd_moq");
            if (StringUtils.isNotBlank(gd_moq)) {
                cgp.setMorder(Integer.parseInt(gd_moq));
            }

            if (StringUtils.isBlank(rangePriceFree)) {
                //?????????????????????????????????
                String feePrice = request.getParameter("feePrice");
                double minPrice = 0;
                double maxPrice = 0;
                int moq = 0;
                if (StringUtils.isNotBlank(feePrice)) {
                    String[] priceLst = feePrice.split(",");
                    minPrice = Double.parseDouble(priceLst[0].split("@")[1]);
                    maxPrice = minPrice;
                    String[] tempList = null;
                    for (String priceStr : priceLst) {
                        tempList = priceStr.split("@");
                        double tempPrice = Double.parseDouble(tempList[1]);
                        if (tempPrice < minPrice) {
                            minPrice = tempPrice;
                        }
                        if (tempPrice > maxPrice) {
                            maxPrice = tempPrice;
                        }
                        String tempMoq = tempList[0];
                        if (tempMoq.contains("-")) {
                            int tempMoqInt = Integer.parseInt(tempMoq.split("-")[0]);
                            if (moq == 0 || tempMoqInt < moq) {
                                moq = tempMoqInt;
                            }
                        } else if (tempMoq.contains("???")) {
                            int tempMoqInt = Integer.parseInt(tempMoq.replace("???", "").trim());
                            if (moq == 0 || tempMoqInt < moq) {
                                moq = tempMoqInt;
                            }
                        }
                    }
                    //?????????
                    DecimalFormat df = new DecimalFormat("######0.00");
                    cgp.setPrice(df.format(minPrice));
                    cgp.setFeeprice("[" + feePrice.replace("@", " $ ") + "]");
                    cgp.setMorder(moq);
                }
            }

            if (StringUtils.isBlank(rangePrice)) {
                double minPrice = 0;
                double maxPrice = 0;
                int moq = 0;
                String wprice = request.getParameter("wprice");
                if (StringUtils.isBlank(wprice)) {

                        cgp.setWprice("[]", 1);

                } else {
                    String[] priceLst = wprice.split(",");
                    minPrice = Double.parseDouble(priceLst[0].split("@")[1]);
                    maxPrice = minPrice;
                    for (String priceStr : priceLst) {
                        String[] tempList = priceStr.split("@");
                        double tempPrice = Double.parseDouble(tempList[1]);
                        if (tempPrice < minPrice) {
                            minPrice = tempPrice;
                        }
                        if (tempPrice > maxPrice) {
                            maxPrice = tempPrice;
                        }
                        String tempMoq = tempList[0];
                        if (tempMoq.contains("-")) {
                            int tempMoqInt = Integer.parseInt(tempMoq.split("-")[0]);
                            if (moq == 0 || tempMoqInt < moq) {
                                moq = tempMoqInt;
                            }
                        } else if (tempMoq.contains("???")) {
                            int tempMoqInt = Integer.parseInt(tempMoq.replace("???", "").trim());
                            if (moq == 0 || tempMoqInt < moq) {
                                moq = tempMoqInt;
                            }
                        }
                    }
                    //?????????
                    DecimalFormat df = new DecimalFormat("######0.00");
                    //cgp.setPrice(df.format(minPrice));
                    cgp.setWprice("[" + wprice.replace("@", " $ ") + "]");
                    cgp.setMorder(moq);
                }
            }

            if (StringUtils.isNotBlank(gd_moq)) {
              cgp.setMorder(Integer.parseInt(gd_moq));
            }


            //???????????????????????????
            String typeRepalceIds = request.getParameter("typeRepalceIds");

            String[] tpList;
            if (StringUtils.isNotBlank(typeRepalceIds)) {
                //?????????????????????
                tpList = typeRepalceIds.split(",");
                String[] spSt;
                for (String tpCt : tpList) {
                    spSt = tpCt.split("@");

                }


            }


            // ?????????????????????
            String wordSizeInfo = request.getParameter("wordSizeInfo");
            if (StringUtils.isNotBlank(wordSizeInfo)) {
                cgp.setSizeInfoEn(wordSizeInfo.replace("\\n", "<br>"));
            } else {
                cgp.setSizeInfoEn("");
            }

            String skuCount = request.getParameter("skuCount");
            if (StringUtils.isBlank(skuCount)) {
                json.setOk(false);
                json.setMessage("???????????????????????????");
                return json;
            }

            // ?????????????????? mainImg
            //String mainImg = request.getParameter("mainImg");
            if (StringUtils.isNotBlank(mainImg)) {
                //mainImg = mainImg.replace(orGoods.getRemotpath(), "");
                // ???????????????????????????  ????????????????????????????????????285x285??????285x380,???????????????
                if (mainImg.contains(".60x60")) {
                    cgp.setCustomMainImage(mainImg.replace(".60x60", ".220x220"));
                } else if (mainImg.contains(".400x400")) {
                    cgp.setCustomMainImage(mainImg.replace(".400x400", ".220x220"));
                }
                cgp.setShowMainImage(mainImg);
                cgp.setIsUpdateImg(2);
            }

            String imgInfo = request.getParameter("imgInfo");
            if (!(imgInfo == null || "".equals(imgInfo))) {
                // ????????????????????????????????????
                cgp.setImg("[" + imgInfo.replace(";", ",") + "]");
            } else {
                json.setOk(false);
                json.setMessage("?????????????????????");
                return json;
            }
            String entype = request.getParameter("entype");
            cgp.setEntype(entype);

            String type = request.getParameter("type");
            // type 0 ?????? 1 ???????????????
            int tempId = user.getId();
            int success = 0;
            CustomGoodsPublish goods = customGoodsService.queryNewGoodsDetails(cgp.getPid());
            if (goods != null && "0".equals(type)) {
                success = customGoodsService.updateNewGoodsDetailsByInfo(cgp);
            } else {
                success = customGoodsService.saveNewGoodsDetails(cgp, tempId, Integer.parseInt(type));

            }
            if ("1".equals(type)) {
                int isEdit = customGoodsService.checkIsEditedByPid(cgp.getPid());
                if (isEdit == 0) {
                    customGoodsService.insertPidIsEdited("", cgp.getPid(), user.getId());
                }
            }

            if (success > 0) {
                json.setMessage("????????????");
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
            LOG.error("????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setGoodsOff")
    @ResponseBody
    public JsonResult setGoodsOff(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");

        String pidStr = request.getParameter("pid");
        if (pidStr == null || "".equals(pidStr)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }

        String reason = request.getParameter("reason");
        if (StringUtils.isBlank(reason)) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }

        try {

            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("????????????????????????????????????");
                return json;
            }

            int count = customGoodsService.setGoodsValid(pidStr, user.getAdmName(), user.getId(), -1, reason);
            if (count > 0) {
                // ???????????????kids???????????????????????????????????????????????????
                boolean isSu = GoodsInfoUtils.deleteImgByUrl(pidStr);
                // boolean isSu = true;
                if (isSu) {
                    json.setOk(true);
                    json.setMessage("????????????");
                } else {
                    json.setOk(false);
                    json.setMessage("???????????????????????????");
                }
            } else {
                json.setOk(false);
                json.setMessage("???????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " ????????????????????????" + e.getMessage());
            LOG.error("pid : " + pidStr + " ????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setGoodsOnline")
    @ResponseBody
    public JsonResult setGoodsOnline(HttpServletRequest request, HttpServletResponse response) {


        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");

        String pidStr = request.getParameter("pid");
        if (pidStr == null || "".equals(pidStr)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }
        try {
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("????????????????????????????????????");
                return json;
            }

            int count = customGoodsService.setGoodsValid(pidStr, user.getAdmName(), user.getId(), 1, "");
            if (count > 0) {
                // ???????????????kids???????????????????????????????????????????????????
                boolean isSu = GoodsInfoUtils.deleteImgByUrl(pidStr);
                if (isSu) {
                    json.setOk(true);
                    json.setMessage("????????????");
                } else {
                    json.setOk(false);
                    json.setMessage("???????????????????????????");
                }
            } else {
                json.setOk(false);
                json.setMessage("???????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " ????????????????????????" + e.getMessage());
            LOG.error("pid : " + pidStr + " ????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setNoUpdatePrice")
    @ResponseBody
    public JsonResult setNoUpdatePrice(HttpServletRequest request, HttpServletResponse response) {


        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");

        String pidStr = request.getParameter("pid");
        if (pidStr == null || "".equals(pidStr)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }

        String flagStr = request.getParameter("flag");
        if (StringUtils.isBlank(flagStr)) {
            json.setOk(false);
            json.setMessage("??????flag??????");
            return json;
        }
        try {
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("????????????????????????????????????");
                return json;
            }

            int isFreeFlag = 0;
            // ??????1688?????????????????????
            CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pidStr, 0);
            if (StringUtils.isNotBlank(goods.getRange_price_free_new())) {

                AtomicInteger skuUpdateCount = new AtomicInteger();
                // ??????sku???????????????
                // ???goods???entype??????????????????,????????????
                List<TypeBean> typeList = GoodsInfoUtils.deal1688GoodsType(goods, true);
                double finalWeight = 0;
                if (StringUtils.isNotBlank(goods.getSku_new())) {
                    // List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
                    List<ImportExSku> skuList = JSONArray.parseArray(goods.getSku_new(), ImportExSku.class);

                    double priceMin = 0;
                    double priceMax = 0;

                    for (ImportExSku exSku : skuList) {
                        skuUpdateCount.getAndIncrement();
                        float costPrice = exSku.getSkuVal().getCostPrice();
                        if (costPrice > 0) {
                            double tempPrice = B2CPriceUtil.getFreePrice(costPrice, goods.getFinalWeight(), goods.getVolumeWeight());
                            exSku.getSkuVal().setFreeSkuPrice(String.valueOf(tempPrice));
                            if (priceMin == 0 || priceMin > tempPrice) {
                                priceMin = tempPrice;
                            }
                            if (priceMax == 0 || priceMax < tempPrice) {
                                priceMax = tempPrice;
                            }
                        }
                    }
                    if (priceMin > 0 && priceMax > 0) {
                        if (priceMin == priceMax) {
                            goods.setRange_price_free_new(String.valueOf(priceMin));
                        } else {
                            goods.setRange_price_free_new(priceMin + "-" + priceMax);
                        }
                        goods.setPrice_kids(String.valueOf(priceMin));
                    } else if (priceMin > 0) {
                        goods.setRange_price_free_new(String.valueOf(priceMin));
                        goods.setPrice_kids(String.valueOf(priceMin));
                    } else if (priceMax > 0) {
                        goods.setRange_price_free_new(String.valueOf(priceMax));
                        goods.setPrice_kids(String.valueOf(priceMax));
                    }

                    if (skuUpdateCount.get() > 0) {
                        goods.setSku_new(JSONArray.toJSONString(skuList));
                    }

                    List<ImportExSkuShow> cbSkus = GoodsInfoUtils.combineSkuList(typeList, skuList);
                    for (ImportExSkuShow exSku : cbSkus) {
                        if (finalWeight == 0 || finalWeight < Math.max(exSku.getFianlWeight(), exSku.getVolumeWeight())) {
                            finalWeight = Math.max(exSku.getFianlWeight(), exSku.getVolumeWeight());
                        }
                        if (StringUtils.isNotBlank(exSku.getSpecId())) {
                            String chType = customGoodsService.queryChTypeBySkuId(exSku.getSpecId());
                            if (StringUtils.isBlank(chType)) {
                                chType = "";
                            }
                            exSku.setChType(chType);
                        }
                    }
                }
                typeList.clear();
                if (finalWeight < 0.5) {
                    isFreeFlag = 1;
                }
            } else {
                if (Math.max(Float.parseFloat(goods.getFinalWeight()), Float.parseFloat(goods.getVolumeWeight())) < 0.5) {
                    isFreeFlag = 1;
                }
            }

            /**
             *
             * B2C???????????? ??????
             * 1.morder=1
             * 2.B2C????????? =(1688???????????????wholesale_price??? *1.4+5+0.042*max(?????????????????????))/6.6
             * wprice ???free_price_new [3-98 $ 5.09, 99-198 $ 4.23, ???199 $ 3.85]  ---??? [???1 $ 3.85]
             *
             * ????????? ??????  1688???????????? sku_new[costPrice]
             *
             */
            goods.setMorder(1);

            if (StringUtils.isBlank(goods.getRange_price_free_new())) {
                // [2-2400 $ 6.13,2401-17279 $ 5.57,???17280 $ 5.15]
                String[] wpriceList = goods.getWholesalePrice().replace("[", "").replace("]", "").replace("$", "@").trim().split(",");
                String priceStr = B2CPriceUtil.getFreePriceStr(Float.parseFloat(wpriceList[0].split("@")[1].trim()), goods.getFinalWeight(), goods.getVolumeWeight());
                String free_price_new = "[???1 $ " + priceStr + "]";
                goods.setFree_price_new(free_price_new);

                wpriceList = goods.getWprice().replace("[", "").replace("]", "").replace("$", "@").trim().split(",");
                goods.setWprice("[???1 $ " + wpriceList[0].split("@")[1].trim() + "]");
                goods.setPrice_kids(priceStr);
            }


            goods.setMatchSource(8);
            goods.setImg_check(String.valueOf(isFreeFlag));

            int count = customGoodsService.setNoUpdatePrice(goods);
            if (count > 0) {
                json.setOk(true);
                json.setMessage("????????????");
            } else {
                json.setOk(false);
                json.setMessage("????????????");
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " ????????????????????????" + e.getMessage());
            LOG.error("pid : " + pidStr + " ????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/checkIsHotGoods")
    @ResponseBody
    public JsonResult checkIsHotGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");

        String pidStr = request.getParameter("pid");
        if (pidStr == null || "".equals(pidStr)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }
        try {
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("????????????????????????????????????");
                return json;
            }
            if (customGoodsService.checkIsHotGoods(pidStr)) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setTotal(1L);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " ????????????????????????" + e.getMessage());
            LOG.error("pid : " + pidStr + " ????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setGoodsInvalid")
    @ResponseBody
    public JsonResult setGoodsInvalid(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String pidStr = request.getParameter("pid");
        if (pidStr == null || "".equals(pidStr)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }
        try {

            String adminId = request.getParameter("adminId");
            if (adminId == null || "".equals(adminId)) {
                json.setOk(false);
                json.setMessage("???????????????id??????");
                return json;
            }
            // type -1 ??????????????? 1 ????????????
            customGoodsService.setGoodsValid(pidStr, "", Integer.parseInt(adminId), -1, "");
            json.setOk(true);
            json.setMessage("????????????");

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " ????????????????????????" + e.getMessage());
            LOG.error("pid : " + pidStr + " ????????????????????????" + e.getMessage());
        }
        return json;
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param response
     * @return
     * @date 2016???12???15???
     * @author abc
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public String getSave(HttpServletRequest request, HttpServletResponse response) {
        // ???????????????????????????
        String pid = "";
        try {
            String content = request.getParameter("content");
            System.err.println("text----------" + content);

            CustomGoodsBean bean = new CustomGoodsBean();
            pid = request.getParameter("pid");

            String localpath = request.getParameter("localpath");
            // ????????????
            String eninfo = content.replace(localpath, "");
            if (IpCheckUtil.checkIsIntranet(request)) {
                eninfo = content.replace(localpath.replace(localIP, wanlIP), "");
            }

            bean.setPid(pid);
            bean.setEninfo(eninfo);
            String goodskeyword = request.getParameter("goodskeyword");
            bean.setKeyword(goodskeyword);

            String goodsname = request.getParameter("goodsname");
            bean.setEnname(goodsname);

            String goodsprice = request.getParameter("goodsprice");
            System.err.println(goodsprice);
            if (goodsprice != null && !goodsprice.isEmpty()) {
                goodsprice = StrUtils.matchStr(goodsprice, "(\\d+\\.*\\d*)");
                System.err.println("goodsprice:" + goodsprice);
            }
            bean.setPrice(goodsprice);
            // ????????????
            String lastPrice = request.getParameter("lastPrice");
            bean.setLastPrice(lastPrice);
            lastPrice = StrUtils.isRangePrice(lastPrice) ? lastPrice : "0";
            double minPrice = Double.parseDouble(lastPrice.split("-")[0]);
            double maxPrice = minPrice;

            String sku = request.getParameter("sku");
            // System.err.println("sku:"+sku);
            if (sku != null && !sku.isEmpty() && sku.startsWith("[")) {
                /*JSONArray sku_json = JSONArray.fromObject(sku);
                List<SkuAttrBean> skuList = (List<SkuAttrBean>) JSONArray.toCollection(sku_json, SkuAttrBean.class);*/
                List<SkuAttrBean> skuList = JSONArray.parseArray(sku, SkuAttrBean.class);
                for (SkuAttrBean skuBean : skuList) {
                    // System.err.println(skuBean.toString());
                    SkuValBean skuVal = skuBean.getSkuVal();
                    String actSkuCalPrice = request.getParameter("actSkuCalPrice_" + skuBean.getSkuPropIds());
                    double price = Double.parseDouble(actSkuCalPrice);
                    if (price - 0.001 < minPrice) {
                        minPrice = price;
                    }
                    if (price - 0.001 > maxPrice) {
                        maxPrice = price;
                    }

                    skuVal.setActSkuCalPrice(actSkuCalPrice);
                    skuVal.setActSkuMultiCurrencyCalPrice(actSkuCalPrice);
                    skuVal.setActSkuMultiCurrencyDisplayPrice(actSkuCalPrice);
                    skuVal.setSkuCalPrice(actSkuCalPrice);
                    skuVal.setSkuMultiCurrencyCalPrice(actSkuCalPrice);
                    skuVal.setSkuMultiCurrencyDisplayPrice(actSkuCalPrice);
                    skuBean.setSkuVal(skuVal);
                }
                String skuStr = JSON.toJSONString(skuList);
                bean.setSku(skuStr);

            }

            if (minPrice > 0) {
                lastPrice = format.format(minPrice);
                if (maxPrice > minPrice) {
                    lastPrice = lastPrice + "" + format.format(maxPrice);
                }
                bean.setLastPrice(lastPrice);
            }

            if ((lastPrice == null || lastPrice.isEmpty() || "0".equals(lastPrice))
                    && (goodsprice == null || goodsprice.isEmpty())) {

                return "redirect:/editc/edit?pid=" + pid;
            }

            String goodsdetail = request.getParameter("goodsdetail");
            bean.setEndetail(goodsdetail);

            String goodsweight = request.getParameter("goodsweight");
            goodsweight = StrUtils.matchStr(goodsweight, "(\\d+\\.*\\d*)");
            goodsweight = goodsweight.isEmpty() ? "0.5kg" : goodsweight + "kg";
            bean.setWeight(goodsweight);

            // String goodsmethod = request.getParameter("goodsmethod");
            // bean.setMethod(goodsmethod);

            // String goodsfeeprice = request.getParameter("goodsfeeprice");
            // bean.setFeeprice(goodsfeeprice);

            // String goodsposttime = request.getParameter("goodsposttime");
            // bean.setPosttime(goodsposttime);

            System.err.println("eninfo----------" + eninfo);
            customGoodsService.updateInfo(bean);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save error :" + e.getMessage());
        }
        return "redirect:/editc/edit?pid=" + pid;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteImages(HttpServletRequest request, HttpServletResponse response) {
        // ???????????????????????????
        String pid = "";
        try {
            pid = request.getParameter("deletepid");
            String catid = request.getParameter("deletecatid");
            String image = request.getParameter("deleteimage");
            // <img
            // src="http://192.168.1.27:8083/importsvimg/img/1494837116435/electroniccigaretts/desc/541135342407/4164109109_1488196098.jpg"
            // alt="" />
            System.err.println("11image:" + image);
            if (image.indexOf("<img") > -1) {
                image = StrUtils.matchStr(image, "(?:src=\")(.*?)(?:\\.jpg)");
                image = image.isEmpty() ? "" : image + ".jpg";
            }
            System.err.println("22image:" + image);
            if (!image.endsWith(".jpg")) {
                return "redirect:/editc/edit?pid=" + pid;
            }
            int lastIndexOf = image.lastIndexOf("/");
            image = image.substring(lastIndexOf + 1);
            System.err.println("33image:" + image);
            List<CustomGoodsBean> goodsListByCatid = customGoodsService.getGoodsListByCatid(catid);
            List<CustomGoodsBean> list = new ArrayList<CustomGoodsBean>();
            for (CustomGoodsBean bean : goodsListByCatid) {
                String eninfo = bean.getEninfo();
                if (eninfo.indexOf(image) == -1) {
                    continue;
                }
                Element document = Jsoup.parse(eninfo).body();
                Elements customInfo = document.select("div[class=custom_info]");
                Elements seImgs_result = new Elements();
                seImgs_result.addAll(customInfo);
                Elements seImgs = document.select("img");
                for (Element seimg : seImgs) {
                    // ????????????????????????
                    String src = seimg.attr("src");
                    if (src == null || src.isEmpty()) {
                        continue;
                    }
                    if (src.indexOf(image) > -1) {
                        continue;
                    }
                    seImgs_result.add(seimg);
                }
                bean.setEninfo(seImgs_result.toString());
                list.add(bean);
            }
            customGoodsService.updateInfoList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/editc/edit?pid=" + pid;
    }


    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @return
     * @date 2016???12???16???
     * @author abc
     */
    @RequestMapping(value = "/xheditorUploads", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> xheditorUploads(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "";
        String err = "";
        String pid = request.getParameter("pid");

        if (pid == null || "".equals(pid)) {
            msg = "";
            err = "??????PID??????";
        } else {
            System.out.println("pid:" + pid);
            try {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                // ???????????????
                List<MultipartFile> fileList = multipartRequest.getFiles("filedata");
                Random random = new Random();
                // ????????????????????????
                if (ftpConfig == null) {
                    ftpConfig = GetConfigureInfo.getFtpConfig();
                }
                // ??????????????????????????????????????????
                JsonResult json = new JsonResult();
                GetConfigureInfo.checkFtpConfig(ftpConfig, json);
                String localDiskPath = ftpConfig.getLocalDiskPath();
                if (json.isOk()) {
                    for (MultipartFile mf : fileList) {
                        if (!mf.isEmpty()) {
                            // ???????????????????????????mf.getOriginalFilename()
                            String originalName = mf.getOriginalFilename();
                            // ????????????????????????
                            String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                            String saveFilename = GoodsInfoUtils.makeFileName(String.valueOf(random.nextInt(1000)));
                            // ??????????????????????????????
                            String localFilePath = "importimg/" + pid + "/desc/" + saveFilename + fileSuffix;
                            // ?????????????????????????????????????????????
                            ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
                            System.err.println(localDiskPath + localFilePath);
                            // ?????????????????????
                            boolean is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
                            System.err.println("check Img Resolution:" + is);
                            if (is) {
                                is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 400);
                                if (is) {
                                    String newLocalPath = "importimg/" + pid + "/desc/" + saveFilename + "_700" + fileSuffix;
                                    // is = ImageCompression.reduceImgByWidth(700.00, localDiskPath + localFilePath, localDiskPath + newLocalPath);
                                    is = ImageCompressionByNoteJs.compressByOkHttp(localDiskPath + localFilePath, 1);
                                    System.err.println("check Img Resolution 700:" + is);
                                    if (is) {
                                        msg = ftpConfig.getLocalShowPath() + localFilePath;
                                    } else {
                                        json.setOk(false);
                                        json.setMessage("???????????????700*700?????????????????????");
                                        break;
                                    }
                                } else {
                                    msg = ftpConfig.getLocalShowPath() + localFilePath;
                                }
                            } else {
                                // ????????????????????????????????????
                                File file = new File(localFilePath);
                                if (file.exists()) {
                                    file.delete();
                                }
                                msg = "";
                                err = "?????????????????????100";
                            }
                        }
                    }
                } else {
                    msg = "";
                    err = json.getMessage();
                }
            } catch (Exception e) {
                msg = "";
                err = "????????????";
                e.printStackTrace();
                LOG.error("???????????????" + e.getMessage());
            }
        }
        map.put("err", err);
        map.put("msg", msg);
        return map;
    }

    /**
     * ??????????????????
     *
     * @param files
     * @param request
     * @return
     * @date 2016???12???16???
     * @author abc
     */
    @RequestMapping(value = "/uploadMultiFile", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult uploadMultiFile(@RequestParam(value = "file", required = false) CommonsMultipartFile[] files,
                                      HttpServletRequest request, @RequestParam(value = "pid", required = true) String pid) {
        JsonResult json = new JsonResult();

        System.out.println("pid:" + pid);
        try {
            Random random = new Random();
            // ????????????????????????
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            GetConfigureInfo.checkFtpConfig(ftpConfig, json);
            String localDiskPath = ftpConfig.getLocalDiskPath();
            List<String> imgList = new ArrayList<>();
            if (json.isOk()) {
                for (CommonsMultipartFile mf : files) {
                    if (!mf.isEmpty()) {
                        // ???????????????????????????mf.getOriginalFilename()
                        String originalName = mf.getOriginalFilename();
                        // ????????????????????????
                        String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                        String saveFilename = GoodsInfoUtils.makeFileName(String.valueOf(random.nextInt(1000)));
                        // ??????????????????????????????
                        String localFilePath = "importimg/" + pid + "/desc/" + saveFilename + fileSuffix;
                        // ?????????????????????????????????????????????
                        ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
                        // ?????????????????????
                        boolean is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
                        if (is) {
                            is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 400);
                            if (is) {
                                String newLocalPath = "importimg/" + pid + "/desc/" + saveFilename + "_700" + fileSuffix;
                                // is = ImageCompression.reduceImgByWidth(700.00, localDiskPath + localFilePath, localDiskPath + newLocalPath);
                                is = ImageCompressionByNoteJs.compressByOkHttp(localDiskPath + localFilePath, 1);
                                if (is) {
                                    imgList.add(ftpConfig.getLocalShowPath() + localFilePath);
                                } else {
                                    json.setOk(false);
                                    json.setMessage("???????????????700*700?????????????????????");
                                }
                            } else {
                                imgList.add(ftpConfig.getLocalShowPath() + localFilePath);
                            }
                        } else {
                            // ????????????????????????????????????
                            File file = new File(localFilePath);
                            if (file.exists()) {
                                file.delete();
                            }
                            json.setOk(false);
                            json.setMessage("?????????????????????100");
                        }
                    }
                }
                if (imgList.size() > 0) {
                    json.setData(imgList);
                }
            }
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("????????????:" + e.getMessage());
            e.printStackTrace();
            LOG.error("???????????????", e);
        }
        return json;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @return
     */
    @RequestMapping(value = "/uploadEinfoNetImg", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult uploadEinfoNetImg(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String imgs = request.getParameter("imgs");
        if (imgs == null || "".equals(imgs)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }
        System.err.println("pid:" + pid + ";imgs" + imgs);

        String newImgUrl = "";
        Random random = new Random();
        String[] imgLst = imgs.split(";");
        boolean isSuccess = true;
        try {
            // ????????????????????????
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            // ??????????????????????????????????????????
            GetConfigureInfo.checkFtpConfig(ftpConfig, json);
            if (!json.isOk()) {
                return json;
            }
            String localDiskPath = ftpConfig.getLocalDiskPath();
            for (String imgUrl : imgLst) {
                if (!(imgUrl == null || "".equals(imgUrl.trim()) || imgUrl.length() < 10)) {
                    // ???????????????????????????
                    if (imgUrl.indexOf("?") > -1) {
                        imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                    }
                    // ????????????http?????????src
                    if (imgUrl.indexOf("//") == 0) {
                        imgUrl = "http:" + imgUrl;
                    }
                    // ????????????????????????
                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                    // ????????????????????????
                    String saveFilename = GoodsInfoUtils.makeFileName(String.valueOf(random.nextInt(1000)));
                    // ??????????????????????????????
                    String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;
                    // ???????????????????????????
                    boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                    if (is) {
                        // ????????????????????????????????????100*100
                        boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
                        if (checked) {
                            // ????????????????????????????????????700*400????????????????????????????????????
                            checked = false;
                            checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 400);
                            if (checked) {
                                checked = false;
                                String newLocalPath = "importimg/" + pid + "/" + saveFilename + "_700" + fileSuffix;
                                checked = ImageCompression.reduceImgByWidth(700.00, localDiskPath + localFilePath,
                                        localDiskPath + newLocalPath);
                                if (checked) {
                                    newImgUrl += ";" + ftpConfig.getLocalShowPath() + newLocalPath;
                                } else {
                                    json.setOk(false);
                                    json.setMessage("???????????????700*700?????????????????????");
                                    break;
                                }
                            } else {
                                newImgUrl += ";" + ftpConfig.getLocalShowPath() + localFilePath;
                            }

                            json.setOk(true);
                            json.setMessage("????????????????????????");
                        } else {
                            // ????????????????????????????????????
                            File file = new File(localFilePath);
                            if (file.exists()) {
                                file.delete();
                            }
                            isSuccess = false;
                            json.setOk(false);
                            json.setMessage("?????????????????????100*100???????????????");
                            break;
                        }
                    } else {
                        isSuccess = false;
                        json.setOk(false);
                        json.setMessage("?????????????????????????????????????????????");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("???????????????" + e.getMessage());
            LOG.error("???????????????" + e.getMessage());
        }
        if (isSuccess) {
            json.setOk(true);
            json.setData(newImgUrl.substring(1));
            json.setMessage("????????????");
        }

        return json;
    }


    /**
     * ???????????????????????????????????????????????????
     */
    @RequestMapping(value = "/uploadTypeNetImg", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult uploadTypeNetImg(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String imgs = request.getParameter("imgs");
        if (imgs == null || "".equals(imgs)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }
        System.err.println("pid:" + pid + ";imgs" + imgs);

        String newImgUrl = "";
        Random random = new Random();
        String[] imgLst = imgs.split(";");
        boolean isSuccess = true;
        try {
            // ????????????????????????
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            // ??????????????????????????????????????????
            GetConfigureInfo.checkFtpConfig(ftpConfig, json);
            if (!json.isOk()) {
                return json;
            }
            String localDiskPath = ftpConfig.getLocalDiskPath();
            for (String imgUrl : imgLst) {
                if (!(imgUrl == null || "".equals(imgUrl.trim()))) {
                    // ???????????????????????????
                    if (imgUrl.indexOf("?") > -1) {
                        imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                    }
                    // ????????????http?????????src
                    if (imgUrl.indexOf("//") == 0) {
                        imgUrl = "http:" + imgUrl;
                    }
                    // ????????????????????????
                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                    // ????????????????????????
                    String saveFilename = GoodsInfoUtils.makeFileName(String.valueOf(random.nextInt(1000)));
                    // ??????????????????????????????
                    String localFilePath = "importimg\\" + pid + "\\" + saveFilename + fileSuffix;
                    // ???????????????????????????
                    boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                    if (is) {
                        // ????????????????????????????????????400*200
                        boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
                        if (checked) {
                            // ????????????400x400
                            String localFilePath400x400 = "importimg/" + pid + "/" + saveFilename + ".400x400" + fileSuffix;
                            // ????????????60x60
                            String localFilePath60x60 = "importimg/" + pid + "/" + saveFilename + ".60x60" + fileSuffix;

                            String localFilePathTest = "importimg/" + pid + "/" + saveFilename  + fileSuffix;

                            //boolean is400 = ImageCompressionByNoteJs.compressByOkHttp(localDiskPath + localFilePath, 5);
                            //boolean is60 = ImageCompressionByNoteJs.compressByOkHttp(localDiskPath + localFilePath, 6);
                            //if (is60 && is400) {
                                //newImgUrl += ";" + ftpConfig.getLocalShowPath() + localFilePath60x60;
                                newImgUrl += ";" + ftpConfig.getLocalShowPath() + localFilePathTest;
                                json.setOk(true);
                                json.setMessage("????????????????????????");
                         /*   } else {
                                // ????????????????????????????????????
                                File file400 = new File(localDiskPath + localFilePath400x400);
                                if (file400.exists()) {
                                    file400.delete();
                                }
                                File file60 = new File(localDiskPath + localFilePath60x60);
                                if (file60.exists()) {
                                    file60.delete();
                                }
                                // ??????????????????????????????
                                isSuccess = false;
                                json.setOk(false);
                                json.setMessage("????????????60x60???400x400?????????????????????");
                                break;
                            }*/
                        } else {
                            // ?????????????????????400*200??????????????????
                            isSuccess = false;
                            json.setOk(false);
                            json.setMessage("?????????????????????400*200???????????????");
                            break;
                        }
                    } else {
                        isSuccess = false;
                        json.setOk(false);
                        json.setMessage("?????????????????????????????????????????????");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
            json.setOk(false);
            json.setMessage("???????????????" + e.getMessage());
        }
        if (isSuccess) {
            json.setOk(true);
            json.setData(newImgUrl.substring(1));
            json.setMessage("????????????");
        }

        return json;
    }

    /**
     * ??????js????????????
     *
     * @return
     */
    @RequestMapping(value = "/uploadByJs", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult uploadByJs(@RequestParam(value = "pid", required = true) String pid,
                                 @RequestParam(value = "uploadfile", required = true) MultipartFile file, HttpServletRequest request) {
        JsonResult json = new JsonResult();

        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
        } else {
            System.out.println("pid:" + pid);
            try {
                if (!file.isEmpty()) {
                    // ????????????????????????
                    if (ftpConfig == null) {
                        ftpConfig = GetConfigureInfo.getFtpConfig();
                    }
                    // ??????????????????????????????????????????
                    GetConfigureInfo.checkFtpConfig(ftpConfig, json);
                    if (!json.isOk()) {
                        return json;
                    }
                    String localDiskPath = ftpConfig.getLocalDiskPath();
                    Random random = new Random();
                    String originalName = file.getOriginalFilename();
                    // ????????????????????????
                    String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                    String saveFilename = GoodsInfoUtils.makeFileName(String.valueOf(random.nextInt(1000)));
                    // ??????????????????????????????
                    String localFilePath = "importimg\\" + pid + "\\" + saveFilename + fileSuffix;
                    // ?????????????????????????????????????????????
                    ImgDownload.writeImageToDisk(file.getBytes(), localDiskPath + localFilePath);

                    // ????????????????????????????????????400*200
                    boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
                    if (checked) {
                        // ????????????400x400
                        String localFilePath400x400 = "importimg\\" + pid + "\\" + saveFilename + ".400x400" + fileSuffix;
                        // ????????????60x60
                        String localFilePath60x60 = "importimg\\" + pid + "\\" + saveFilename + ".60x60" + fileSuffix;

                        String localFilePathTest = "importimg/" + pid + "/" + saveFilename  + fileSuffix;

                        //boolean is400 = ImageCompressionByNoteJs.compressByOkHttp(localDiskPath + localFilePath, 5);
                        //boolean is60 = ImageCompressionByNoteJs.compressByOkHttp(localDiskPath + localFilePath, 6);
                        //if (is60 && is400) {
                        json.setData(ftpConfig.getLocalShowPath() + localFilePathTest);
                        json.setOk(true);
                        json.setMessage("?????????????????????????????????");
                       /* } else {
                            // ????????????????????????????????????
                            File file60 = new File(localFilePath60x60);
                            if (file60.exists()) {
                                file60.delete();
                            }
                            File file400 = new File(localFilePath400x400);
                            if (file400.exists()) {
                                file400.delete();
                            }
                            // ??????????????????????????????
                            json.setOk(false);
                            json.setMessage("????????????60x60???400x400?????????????????????");
                        }*/
                    } else {
                        // ?????????????????????400*200??????????????????
                        json.setOk(false);
                        json.setMessage("?????????????????????400*200???????????????");
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("??????????????????????????????");
                }
            } catch (Exception e) {
                e.printStackTrace();
                json.setOk(false);
                json.setMessage("????????????:" + e.getMessage());
                LOG.error("???????????????" + e.getMessage());
            }
        }
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateReviewRemark", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult updateReviewRemark(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        json.setOk(true);
        try {
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            com.cbt.pojo.Admuser adm = (com.cbt.pojo.Admuser) SerializeUtil.JsonToObj(admuserJson, com.cbt.pojo.Admuser.class);
            if (adm == null) {
                json.setOk(false);
                json.setMessage("?????????????????????");
                return json;
            }
            Map<String, String> paramMap = new HashMap<String, String>();
            String oldCreateTime = request.getParameter("oldCreateTime");
            String goods_pid = request.getParameter("goods_pid");

            String edit_remark = request.getParameter("edit_remark");
            String editcountry = request.getParameter("editcountry");
            String edit_score = request.getParameter("edit_score");
            String update_flag = request.getParameter("update_flag");
            String id = request.getParameter("id");
            if (StringUtils.isBlank(id)) {
                json.setOk(false);
                return json;
            }
            paramMap.put("oldCreateTime", oldCreateTime);
            paramMap.put("goods_pid", goods_pid);
            paramMap.put("edit_remark", edit_remark);
            paramMap.put("editcountry", editcountry);
            paramMap.put("edit_score", edit_score);
            paramMap.put("update_flag", update_flag);
            paramMap.put("review_name", adm.getAdmName());
            paramMap.put("id", id);
            int index = customGoodsService.updateReviewRemark(paramMap);
            if (index > 0) {
                //?????????????????????

//                String sql = "update goods_review set review_remark='" + edit_remark + "',country='" + editcountry + "',review_score='" + edit_score + "',review_flag='" + update_flag + "',updatetime=now() where goods_pid='" + goods_pid + "' and createtime='" + oldCreateTime + "'";
                String sql = "update goods_review set review_remark='" + edit_remark + "',country='" + editcountry + "',review_score='" + edit_score + "',review_flag='" + update_flag + "',updatetime=now() where id='" + id + "';";
                SendMQ.sendMsg(new RunSqlModel(sql));

            }
            json.setOk(index > 0 ? true : false);
            json.setMessage(index > 0 ? "????????????" : "????????????");
        } catch (Exception e) {
            json.setOk(false);
            LOG.error("updateReviewRemark error", e);
        }
        return json;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addReviewRemark", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult addReviewRemark(HttpServletRequest request, HttpServletResponse response) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JsonResult json = new JsonResult();
        json.setOk(true);
        try {
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            com.cbt.pojo.Admuser adm = (com.cbt.pojo.Admuser) SerializeUtil.JsonToObj(admuserJson, com.cbt.pojo.Admuser.class);
            if (adm == null) {
                json.setOk(false);
            }
            Map<String, String> paramMap = new HashMap<String, String>();
            String goods_pid = request.getParameter("goods_pid");
            String review_remark = request.getParameter("review_remark");
            String review_score = request.getParameter("review_score");
            String country = request.getParameter("country");
            paramMap.put("goods_pid", goods_pid);
            paramMap.put("review_remark", review_remark);
            paramMap.put("review_score", review_score);
            paramMap.put("country", country);
            paramMap.put("review_name", adm.getAdmName());
            String createTime = df.format(new Date());
            paramMap.put("createTime", createTime);
            // ??????????????????????????? ????????????????????????????????? ?????????????????????; ????????????????????????
            int index = customGoodsService.addReviewRemark(paramMap);
            if (index > 0) {
                //?????????????????????

                String sql = "insert into goods_review(goods_pid,country,review_name,createtime,review_remark,review_score) values('" + goods_pid + "','" + country + "','" + adm.getAdmName() + "','" + createTime + "','" + SendMQ.repCha(review_remark) + "','" + review_score + "')";
                SendMQ.sendMsg(new RunSqlModel(sql));

            }
            json.setOk(index > 0 ? true : false);
            json.setMessage("?????????????????????????????????????????????, ????????????????????????10??????????????????. ");
        } catch (Exception e) {
            json.setOk(false);
            e.printStackTrace();
        }
        return json;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @RequestMapping(value = "/useAliGoodsDetails", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult useAliGoodsDetails(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        json.setOk(true);
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }
        String aliGoodsInfo = request.getParameter("aliGoodsInfo");
        if (aliGoodsInfo == null || "".equals(aliGoodsInfo)) {
            json.setOk(false);
            json.setMessage("???????????????????????????");
            return json;
        }

        Document nwDoc = null;
        try {
            // string???html????????????
            nwDoc = Jsoup.parseBodyFragment(aliGoodsInfo);
            // ??????html??????a????????????
            nwDoc.getElementsByTag("a").remove();
            // ??????img??????
            Elements imgEls = nwDoc.getElementsByTag("img");
            Random random = new Random();
            // ???????????????
            // ????????????????????????
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            // ??????????????????????????????????????????
            GetConfigureInfo.checkFtpConfig(ftpConfig, json);
            if (json.isOk()) {
                String localDiskPath = ftpConfig.getLocalDiskPath();
                for (Element imgEl : imgEls) {
                    String imgUrl = imgEl.attr("src");
                    System.out.println("src:" + imgUrl);
                    // ?????????????????????????????????
                    if (StringUtils.isNotBlank(imgUrl) && imgUrl.lastIndexOf(".") > -1) {
                        // ???????????????????????????
                        if (imgUrl.indexOf("?") > -1) {
                            imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                        }
                        // ????????????http?????????src
                        if (imgUrl.indexOf("//") == 0) {
                            imgUrl = "http:" + imgUrl;
                        }
                        // ????????????????????????
                        String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                        // ????????????????????????
                        String saveFilename = GoodsInfoUtils.makeFileName(String.valueOf(random.nextInt(1000)));
                        // ??????????????????????????????
                        String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;

                        // ???????????????????????????
                        boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                        if (is) {
                            // ????????????????????????????????????700*400????????????????????????????????????
                            boolean checked = false;
                            checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 400);
                            if (checked) {
                                checked = false;
                                String newLocalPath = "importimg/" + pid + "/" + saveFilename + "_700" + fileSuffix;
                                checked = ImageCompression.reduceImgByWidth(700.00, localDiskPath + localFilePath,
                                        localDiskPath + newLocalPath);
                                if (checked) {
                                    imgEl.attr("src", ftpConfig.getLocalShowPath() + newLocalPath);
                                } else {
                                    json.setOk(false);
                                    json.setMessage("???????????????700*700?????????????????????");
                                    break;
                                }
                            } else {
                                json.setOk(true);
                                json.setMessage("????????????????????????");
                                imgEl.attr("src", ftpConfig.getLocalShowPath() + localFilePath);
                            }
                        } else {
                            json.setOk(false);
                            json.setMessage("?????????????????????????????????");
                            break;
                        }
                    } else {
                        imgEl.remove();
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + ",???????????????" + e.getMessage());
            LOG.error("pid:" + pid + ",???????????????" + e.getMessage());
        }

        if (nwDoc == null) {
            json.setOk(false);
            json.setMessage("??????html????????????????????????");
        } else if (json.isOk()) {
            json.setData(nwDoc.toString());
            nwDoc = null;
        }
        return json;
    }

    /**
     * @return JsonResult
     * @Title addSimilarGoods
     * @Description ??????????????????
     */
    @RequestMapping(value = "/addSimilarGoods", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult addSimilarGoods(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String mainPid = request.getParameter("mainPid");
        if (mainPid == null || "".equals(mainPid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }
        String similarPids = request.getParameter("similarPids");
        if (similarPids == null || "".equals(similarPids)) {
            json.setOk(false);
            json.setMessage("??????????????????pid??????");
            return json;
        }
        List<SimilarGoods> existSimilarGoods = null;
        List<String> pidList = new ArrayList<String>();
        try {
            existSimilarGoods = customGoodsService.querySimilarGoodsByMainPid(mainPid);
            pidList = new ArrayList<String>();
            List<String> showExistPids = new ArrayList<String>();
            String[] similarPidList = similarPids.split(";");
            if (!(existSimilarGoods == null || existSimilarGoods.size() == 0)) {
                for (SimilarGoods sml : existSimilarGoods) {
                    pidList.add(sml.getSimilarPid());
                }
            }
            if (pidList.size() > 0) {
                for (String similarPid : similarPidList) {
                    if (similarPid.equals(mainPid)) {
                        showExistPids.add(similarPid);
                    } else if (pidList.contains(similarPid)) {
                        showExistPids.add(similarPid);
                    }
                }
            }

            boolean is = customGoodsService.batchInsertSimilarGoods(mainPid, similarPids, user.getId(), pidList);
            if (is) {
                json.setOk(true);
                if (showExistPids.size() > 0) {
                    json.setMessage("????????????,????????????????????????:" + showExistPids);
                } else {
                    json.setMessage("????????????");
                }
            } else {
                json.setOk(false);
                if (showExistPids.size() > 0) {
                    json.setMessage("??????????????????,????????????????????????:" + showExistPids);
                } else {
                    json.setMessage("??????????????????????????????");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("mainPid:" + mainPid + " addSimilarGoods ???????????????" + e.getMessage());
            LOG.error("mainPid:" + mainPid + " addSimilarGoods ???????????????" + e.getMessage());
        } finally {
            if (existSimilarGoods != null) {
                existSimilarGoods.clear();
            }
            if (pidList != null) {
                pidList.clear();
            }
        }
        return json;
    }

    @RequestMapping(value = "/querySimilarGoodsByMainPid", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult querySimilarGoodsByMainPid(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String mainPid = request.getParameter("mainPid");
        if (mainPid == null || "".equals(mainPid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }

        try {
            List<SimilarGoods> goodsList = customGoodsService.querySimilarGoodsByMainPid(mainPid);
            json.setOk(true);
            json.setData(goodsList);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("mainPid:" + mainPid + " querySimilarGoodsByMainPid ???????????????" + e.getMessage());
            LOG.error("mainPid:" + mainPid + " querySimilarGoodsByMainPid ???????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/deleteWordSizeInfoByPid", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult deleteWordSizeInfoByPid(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }

        try {
            boolean isDelete = customGoodsService.deleteWordSizeInfoByPid(pid);
            if (isDelete) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("?????????????????????????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " deleteWordSizeInfoByPid ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " deleteWordSizeInfoByPid ???????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/setGoodsFlagByPid", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult setGoodsFlagByPid(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        GoodsEditBean editBean = new GoodsEditBean();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        } else {
            editBean.setAdmin_id(user.getId());
        }

        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        } else {
            editBean.setPid(pid);
        }

        String weight_flag_str = request.getParameter("weight_flag");
        int weight_flag = 0;
        if (StringUtils.isNotBlank(weight_flag_str)) {
            weight_flag = Integer.parseInt(weight_flag_str);
        }
        editBean.setWeight_flag(weight_flag);

        String ugly_flag_str = request.getParameter("ugly_flag");
        int ugly_flag = 0;
        if (StringUtils.isNotBlank(ugly_flag_str)) {
            ugly_flag = Integer.parseInt(ugly_flag_str);
        }
        editBean.setUgly_flag(ugly_flag);

        String benchmarking_flag_str = request.getParameter("benchmarking_flag");
        int benchmarking_flag = 0;
        if (StringUtils.isNotBlank(benchmarking_flag_str)) {
            benchmarking_flag = Integer.parseInt(benchmarking_flag_str);
        }
        editBean.setBenchmarking_flag(benchmarking_flag);

        String describe_good_flag_str = request.getParameter("describe_good_flag");
        int describe_good_flag = 0;
        if (StringUtils.isNotBlank(describe_good_flag_str)) {
            describe_good_flag = Integer.parseInt(describe_good_flag_str);
        }
        editBean.setDescribe_good_flag(describe_good_flag);

        String never_off_flag_str = request.getParameter("never_off_flag");
        int never_off_flag = 0;
        if (StringUtils.isNotBlank(never_off_flag_str)) {
            never_off_flag = Integer.parseInt(never_off_flag_str);
        }
        editBean.setNever_off_flag(never_off_flag);

        String uniqueness_flag_str = request.getParameter("uniqueness_flag");
        int uniqueness_flag = 0;
        if (StringUtils.isNotBlank(uniqueness_flag_str)) {
            uniqueness_flag = Integer.parseInt(uniqueness_flag_str);
        }
        editBean.setUniqueness_flag(uniqueness_flag);

        String promotion_flag_str = request.getParameter("promotion_flag");
        int promotion_flag = 0;
        if (StringUtils.isNotBlank(promotion_flag_str)) {
            promotion_flag = Integer.parseInt(promotion_flag_str);
        }
        editBean.setPromotion_flag(promotion_flag);

        if (weight_flag > 0 || ugly_flag > 0 || benchmarking_flag > 0 || describe_good_flag > 0 || never_off_flag > 0 || uniqueness_flag > 0 || promotion_flag > 0) {
            System.err.println("pid:" + pid + ",????????????????????????");
        } else {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        try {
            //boolean is = customGoodsService.setGoodsFlagByPid(editBean);
            customGoodsService.updatePidIsEdited(editBean);
            customGoodsService.insertIntoGoodsEditBean(editBean);
            if (promotion_flag > 0) {
                customGoodsService.updatePromotionFlag(pid);
            }
            if (describe_good_flag > 0) {
                // ??????MongoDB,????????????
                //u???????????????c???????????????d????????????
                InputData inputData = new InputData('u');
                inputData.setPid(pid);
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
                inputData.setDescribe_good_flag(String.valueOf(describe_good_flag));
                GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
                // ????????????
                customGoodsService.insertIntoDescribeLog(pid, user.getId());
            }
            json.setOk(true);
            json.setMessage("????????????");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setGoodsFlagByPid ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " setGoodsFlagByPid ???????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/saveGoodsDescInfo", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult saveGoodsDescInfo(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        GoodsEditBean editBean = new GoodsEditBean();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        } else {
            editBean.setAdmin_id(user.getId());
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        } else {
            editBean.setPid(pid);
        }

        String hotTypeId = request.getParameter("hotTypeId");
        if (StringUtils.isBlank(hotTypeId)) {
            json.setOk(false);
            json.setMessage("????????????ID??????");
            return json;
        }

        editBean.setDescribe_good_flag(1);

        if (pidMap.containsKey(pid + hotTypeId)) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        } else {
            pidMap.put(pid + hotTypeId, pid);
        }
        try {
            customGoodsService.updatePidIsEdited(editBean);
            customGoodsService.insertIntoGoodsEditBean(editBean);
            // ??????MongoDB,????????????
            new Thread(() -> {
                //u???????????????c???????????????d????????????
                InputData inputData = new InputData('u');
                inputData.setPid(pid);
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
                inputData.setDescribe_good_flag("1");
                boolean isSu = GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
                if (!isSu) {
                    LOG.error("saveGoodsDescInfo update mongodb error,pid:" + pid + ",hotTypeId:" + hotTypeId);
                }
            }).start();

            // ????????????
            customGoodsService.insertIntoDescribeLog(pid, user.getId());

            new Thread(() -> {
                // ?????????????????????
                saveHotGoods(pid, Integer.parseInt(hotTypeId), user.getId());
            }).start();


            json.setOk(true);
            json.setMessage("????????????");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setGoodsFlagByPid ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " setGoodsFlagByPid ???????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/saveGoodsDescInfoByList")
    @ResponseBody
    public JsonResult saveGoodsDescInfoByList(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        GoodsEditBean editBean = new GoodsEditBean();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        } else {
            editBean.setAdmin_id(user.getId());
        }
        try {
            HotCategory param = new HotCategory();
            param.setHotType(24);
            param.setIsOn(-1);

            List<HotCategory> res = hotManageService.queryForList(param);
            Map<Integer, Integer> categoryMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(res)) {
                for (int i = 0; i < res.size(); i++) {
                    categoryMap.put(i, res.get(i).getId());
                }
            } else {
                json.setOk(false);
                json.setMessage("???????????????");
                return json;
            }

            int categorySize = categoryMap.size();
            List<String> pidList = customGoodsService.queryDescribeLogList();
            if (CollectionUtils.isNotEmpty(pidList)) {
                for (int j = 0; j < pidList.size(); j++) {
                    // ?????????????????????
                    saveHotGoods(pidList.get(j), categoryMap.get((j % categorySize)), user.getId());
                }
            }
            json.setOk(true);
            json.setMessage("????????????");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("saveGoodsDescInfoByPid ???????????????" + e.getMessage());
            LOG.error("saveGoodsDescInfoByPid ???????????????" + e.getMessage());
        }
        return json;
    }

    private void saveHotGoods(String goodsPid, int categoryId, int adminId) {

        try {
            CustomGoodsBean goods = hotGoodsService.queryFor1688Goods(goodsPid);
            // SendMQ sendMQ = new SendMQ();

            // ???????????????goodsPid??????
            boolean isExists = hotGoodsService.checkExistsGoods(categoryId, goodsPid);
            if (isExists) {
                System.err.println("goodsPid:" + goodsPid + ",categoryIdStr:" + categoryId + ",?????????????????????????????????");
            } else {
                HotSellingGoods hsGoods = new HotSellingGoods();
                hsGoods.setHotSellingId(categoryId);

                if (StringUtils.isNotBlank(goods.getName())) {
                    hsGoods.setGoodsName(goods.getName());
                } else {
                    hsGoods.setGoodsName("");
                }

                hsGoods.setShowName(goods.getEnname());
                hsGoods.setGoodsImg(goods.getImg().split(",")[0].replace("[", "").replace("]", ""));
                if (StringUtils.isNotBlank(hsGoods.getGoodsImg())
                        && !(hsGoods.getGoodsImg().contains("http:") || hsGoods.getGoodsImg().contains("https:"))) {
                    hsGoods.setGoodsImg(goods.getRemotpath() + hsGoods.getGoodsImg());
                }
                hsGoods.setGoodsUrl("https://www.import-express.com/product/detail?&source=D"
                        + Md5Util.encoder(goods.getPid()) + "&item=" + goods.getPid());
                hsGoods.setGoodsPid(goodsPid);

                hsGoods.setAsinCode("");
                hsGoods.setCreateAdmid(adminId);
                hsGoods.setIsOn("1");

                String showName = hsGoods.getShowName();


                if (showName.contains("'")) {
                    showName = showName.replace("'", "\\'");
                }
                if (showName.contains("\"")) {
                    showName = showName.replace("\"", "\\\"");
                }
                SendMQ.sendMsg(new RunSqlModel("insert into hot_selling_goods (hot_selling_id,goods_pid,show_name," +
                        "goods_url,goods_img,goods_price,is_on,profit_margin,selling_price,wholesale_price_1,wholesale_price_2," +
                        "wholesale_price_3,wholesale_price_4,wholesale_price_5,create_admid,amazon_price,asin_code) values(" + hsGoods.getHotSellingId() + "," + hsGoods.getGoodsPid() + "," +
                        "'" + showName + "'," +
                        "'" + hsGoods.getGoodsUrl() + "','" + hsGoods.getGoodsImg() + "','" + hsGoods.getGoodsPrice() + "','" + hsGoods.getIsOn() + "'," +
                        "'" + hsGoods.getProfitMargin() + "','" + hsGoods.getSellingPrice() + "','" + hsGoods.getWholesalePrice_1() + "','" + hsGoods.getWholesalePrice_2() + "'," +
                        "'" + hsGoods.getWholesalePrice_3() + "','" + hsGoods.getWholesalePrice_4() + "','" + hsGoods.getWholesalePrice_5() + "'," +
                        "'" + hsGoods.getCreateAdmid() + "','" + hsGoods.getAmazonPrice() + "','" + hsGoods.getAsinCode() + "')"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("????????????????????????????????????" + e.getMessage());
        }
    }


    @RequestMapping(value = "/setGoodsRepairedByPid", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult setGoodsRepairedByPid(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        GoodsEditBean editBean = new GoodsEditBean();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        } else {
            editBean.setAdmin_id(user.getId());
        }

        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        } else {
            editBean.setPid(pid);
        }
        editBean.setRepaired_flag(1);

        try {
            boolean is = customGoodsService.updatePidIsEdited(editBean) > 0;
            if (is) {
                customGoodsService.insertIntoGoodsEditBean(editBean);
                json.setOk(true);
                json.setMessage("????????????");
            } else {
                json.setOk(true);
                json.setMessage("????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setGoodsRepairedByPid ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " setGoodsRepairedByPid ???????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setNoBenchmarking", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult setNoBenchmarking(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        /*String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }*/

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }
        String finalWeight = request.getParameter("finalWeight");
        if (StringUtils.isBlank(finalWeight)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }

        try {
            boolean is = customGoodsService.setNoBenchmarking(pid, Double.parseDouble(finalWeight));
            if (is) {
                json.setOk(true);
                json.setMessage("????????????");
            } else {
                json.setOk(true);
                json.setMessage("????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setNoBenchmarking ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " setNoBenchmarking ???????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setNeverOff", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult setNeverOff(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        /*String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }*/

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }

        try {
            boolean is = customGoodsService.setNeverOff(pid);
            if (is) {
                json.setOk(true);
                json.setMessage("????????????");
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setNoBenchmarking ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " setNoBenchmarking ???????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/saveBenchmarking", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult saveBenchmarking(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }
        String benchmarkingPid = request.getParameter("benchmarkingPid");
        if (StringUtils.isBlank(benchmarkingPid)) {
            json.setOk(false);
            json.setMessage("??????????????????ID??????");
            return json;
        }
        String benchmarkingPrice = request.getParameter("benchmarkingPrice");
        if (StringUtils.isBlank(benchmarkingPrice)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }

        try {
            boolean is = customGoodsService.saveBenchmarking(pid, benchmarkingPid, benchmarkingPrice) > 0;
            if (is) {
                json.setOk(true);
                json.setMessage("????????????");
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " saveBenchmarking ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " saveBenchmarking ???????????????" + e.getMessage());
        }
        return json;
    }


    /**
     * @param filename ?????????????????????
     * @return uuid+"_"+?????????????????????
     * @Method: makeFileName
     * @Description: ????????????????????????????????????????????????uuid+"_"+?????????????????????
     */
    public static String makeFileName(String filename) { // 2.jpg
        // ???????????????????????????????????????????????????????????????????????????????????????
        return UUID.randomUUID().toString() + "_" + filename;
    }


    @RequestMapping(value = "/goodsEditLog")
    public ModelAndView goodsEditLog(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsEditLog");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("uid", 0);
            return mv;
        } else {
            mv.addObject("uid", user.getId());
        }
        // ???????????????????????????
        String pid = request.getParameter("pid");
        if (pid == null || pid.isEmpty()) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "??????PID??????");
            return mv;
        } else {
            mv.addObject("pid", pid);
        }

        try {
            GoodsEditBean editBean = new GoodsEditBean();
            editBean.setPid(pid);
            List<GoodsEditBean> editList = customGoodsService.queryGoodsEditBean(editBean);
            mv.addObject("isShow", 1);
            mv.addObject("editList", editList);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",goodsEditLog error:" + e.getMessage());
            System.err.println("pid:" + pid + ",goodsEditLog error:" + e.getMessage());
            mv.addObject("isShow", 0);
            mv.addObject("message", "????????????????????????" + e.getMessage());
        }
        return mv;
    }


    @RequestMapping(value = "/updateGoodsWeight", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult updateGoodsWeight(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }
        String newWeight = request.getParameter("newWeight");
        if (StringUtils.isBlank(newWeight)) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        String weight = request.getParameter("weight");
        if (StringUtils.isBlank(weight)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }

        try {
            //boolean is = customGoodsService.updateGoodsWeightByPid(pid, Double.parseDouble(newWeight), Double.parseDouble(weight), 1) > 0;
            /*if (is) {
                // ????????????????????????
                String ip = request.getRemoteAddr();
                int is27 = 29;
                if (ip.contains("1.34") || ip.contains("38.42") || ip.contains("1.27") || ip.contains("1.9")) {
                    is27 = 27;
                }
                String url = SHOPGOODSWEIGHTCLEARURL + "pid=" + pid + "&finalWeight=" + newWeight
                        + "&sourceTable=custom_benchmark_ready&database=" + is27;
                String resultJson = DownloadMain.getContentClient(url, null);
                System.err.println("pid=" + pid + ",result:[" + resultJson + "]");
                JSONObject jsonJt = JSONObject.fromObject(resultJson);
                System.out.println(json.toString());
                if (!jsonJt.getBoolean("ok")) {
                    json.setOk(false);
                    json.setMessage("???????????????????????????????????????" + jsonJt.getString("message"));
                } else {
                    json.setOk(true);
                    json.setMessage("????????????");
                }
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
            }*/
            // ?????????????????????????????????????????????
            customGoodsService.setGoodsWeightByWeigherNew(pid, newWeight, 1, user.getId());
            json.setOk(true);
            json.setMessage("????????????");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " updateGoodsWeight ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " updateGoodsWeight ???????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setGoodsWeightByWeigher")
    @ResponseBody
    public JsonResult setGoodsWeightByWeigher(HttpServletRequest request, String pid, String newWeight) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }
        if (StringUtils.isBlank(newWeight)) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        return customGoodsService.setGoodsWeightByWeigherNew(pid, newWeight, 2, user.getId());
    }


    @RequestMapping(value = "/editAndLockProfit", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult editAndLockProfit(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("????????????PID??????");
            return json;
        }

        String editProfit = request.getParameter("editProfit");
        if (StringUtils.isBlank(editProfit)) {
            json.setOk(false);
            json.setMessage("?????????????????????");
            return json;
        }

        String typeStr = request.getParameter("type");
        if (StringUtils.isBlank(typeStr)) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        try {
            boolean is = customGoodsService.editAndLockProfit(pid, Integer.parseInt(typeStr), Double.parseDouble(editProfit)) > 0;
            if (is) {
                json.setOk(true);
                json.setMessage("????????????");
            } else {
                json.setOk(false);
                json.setMessage("????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " editAndLockProfit ???????????????" + e.getMessage());
            LOG.error("pid:" + pid + " editAndLockProfit ???????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/queryGoodsEditLog")
    @ResponseBody
    public EasyUiJsonResult queryGoodsEditLog(HttpServletRequest request, HttpServletResponse response) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setSuccess(false);
            json.setMessage("??????????????????");
            return json;
        }
        GoodsEditBean editBean = new GoodsEditBean();

        // ???????????????????????????
        String pid = request.getParameter("pid");
        if (StringUtils.isNotBlank(pid)) {
            editBean.setPid(pid);
        }

        String adminIdStr = request.getParameter("adminId");
        if (StringUtils.isNotBlank(adminIdStr)) {
            editBean.setAdmin_id(Integer.parseInt(adminIdStr));
        }
        String weightFlagStr = request.getParameter("weightFlag");
        if (StringUtils.isNotBlank(adminIdStr)) {
            editBean.setWeight_flag(Integer.parseInt(weightFlagStr));
        }
        String uglyFlagStr = request.getParameter("uglyFlag");
        if (StringUtils.isNotBlank(uglyFlagStr)) {
            editBean.setUgly_flag(Integer.parseInt(uglyFlagStr));
        }
        String repairedFlagStr = request.getParameter("repairedFlag");
        if (StringUtils.isNotBlank(repairedFlagStr)) {
            editBean.setRepaired_flag(Integer.parseInt(repairedFlagStr));
        }
        String benchmarkingFlagStr = request.getParameter("benchmarkingFlag");
        if (StringUtils.isNotBlank(benchmarkingFlagStr)) {
            editBean.setBenchmarking_flag(Integer.parseInt(benchmarkingFlagStr));
        }

        int startNum = 0;
        int limitNum = 30;
        String limitNumStr = request.getParameter("rows");
        if (StringUtils.isNotBlank(limitNumStr)) {
            limitNum = Integer.parseInt(limitNumStr);
        }

        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.parseInt(pageStr) - 1) * limitNum;
        }
        editBean.setStartNum(startNum);
        editBean.setLimitNum(limitNum);

        try {
            List<GoodsEditBean> editList = customGoodsService.queryGoodsEditBean(editBean);
            Map<String, List<String>> pidMapNews = new HashMap<>(limitNum + 1);
            Map<String, List<String>> pidMapOlds = new HashMap<>(limitNum + 1);
            for (GoodsEditBean gdEd : editList) {
                if (StringUtils.isNotBlank(gdEd.getOld_title()) && StringUtils.isNotBlank(gdEd.getNew_title())) {
                    if (gdEd.getNew_title().equals(gdEd.getOld_title())) {
                        gdEd.setNew_title("");
                    }
                }
                if (pidMapOlds.containsKey(gdEd.getPid())) {
                    if (StringUtils.isNotBlank(gdEd.getOld_title())) {
                        if (checkListContains(pidMapOlds.get(gdEd.getPid()), gdEd.getOld_title())) {
                            gdEd.setOld_title("");
                        } else {
                            pidMapOlds.get(gdEd.getPid()).add(gdEd.getOld_title());
                        }
                    }
                } else {
                    List<String> titleList = new ArrayList<>();
                    titleList.add(gdEd.getOld_title());
                    pidMapOlds.put(gdEd.getPid(), titleList);
                }
                if (pidMapNews.containsKey(gdEd.getPid())) {
                    if (StringUtils.isNotBlank(gdEd.getNew_title())) {
                        if (checkListContains(pidMapNews.get(gdEd.getPid()), gdEd.getNew_title())) {
                            gdEd.setNew_title("");
                        } else {
                            pidMapNews.get(gdEd.getPid()).add(gdEd.getNew_title());
                        }
                    }
                } else {
                    List<String> titleList = new ArrayList<>();
                    titleList.add(gdEd.getNew_title());
                    pidMapNews.put(gdEd.getPid(), titleList);
                }

            }
            pidMapNews.clear();
            pidMapOlds.clear();
            int total = customGoodsService.queryGoodsEditBeanCount(editBean);
            json.setSuccess(true);
            json.setRows(editList);
            json.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",queryGoodsEditLog error:" + e.getMessage());
            System.err.println("pid:" + pid + ",queryGoodsEditLog error:" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/queryMd5ByImgUrl")
    @ResponseBody
    public JsonResult queryMd5ByImgUrl(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        // ???????????????????????????
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("??????shopId??????");
            return json;
        }

        String url = request.getParameter("url");
        if (StringUtils.isBlank(url)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }
        try {
            String remotePath = GoodsInfoUtils.changeRemotePathToLocal(url, 0);
            int total = customGoodsService.queryMd5ImgByUrlCount(pid, remotePath, shopId);
            List<GoodsMd5Bean> md5BeanList;
            if (total > 1) {
                md5BeanList = customGoodsService.queryMd5ImgByUrlList(pid, remotePath, shopId);
                json.setRows(md5BeanList);
            }
            json.setOk(true);
            json.setTotal((long) total);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",queryMd5ByImgUrl error:" + e.getMessage());
            System.err.println("pid:" + pid + ",queryMd5ByImgUrl error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("??????Md5??????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/deleteImgByMd5")
    @ResponseBody
    public JsonResult deleteImgByMd5(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        // ???????????????????????????
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("??????shopId??????");
            return json;
        }

        String url = request.getParameter("url");
        if (StringUtils.isBlank(url)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }
        try {
            DeleteImgByMd5Thread deleteImgByMd5Thread = new DeleteImgByMd5Thread(pid, user.getId(), shopId, url,
                    customGoodsService, shopUrlService);
            deleteImgByMd5Thread.start();
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",deleteImgByMd5 error:" + e.getMessage());
            System.err.println("pid:" + pid + ",deleteImgByMd5 error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("??????Md5??????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setNewAliPidInfo")
    @ResponseBody
    public JsonResult setNewAliPidInfo(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }

        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isBlank(aliPid)) {
            json.setOk(false);
            json.setMessage("??????aliPid??????");
            return json;
        }

        String aliPrice = request.getParameter("aliPrice");
        if (StringUtils.isBlank(aliPrice)) {
            json.setOk(false);
            json.setMessage("??????aliPrice??????");
            return json;
        }
        try {
            customGoodsService.setNewAliPidInfo(pid, aliPid, aliPrice);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",setNewAliPidInfo error:" + e.getMessage());
            System.err.println("pid:" + pid + ",setNewAliPidInfo error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/publicToOnline")
    @ResponseBody
    public String publicToOnline() {
        String rs = "0";
        try {
            List<String> pidList = customGoodsService.queryOnlineSync();
            if (CollectionUtils.isNotEmpty(pidList)) {
                for (String pid : pidList) {
                    if (StringUtils.isNotBlank(pid)) {
                        PublishGoodsToOnlineThread pbCallable = new PublishGoodsToOnlineThread(pid, customGoodsService, ftpConfig, 1, 0, 0, null);
                        FutureTask futureTask = new FutureTask(pbCallable);
                        Thread thread = new Thread(futureTask);
                        thread.start();
                        try {
                            Thread.sleep(25000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                pidList.clear();
            }
            rs = "1";
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("publicToOnline ???????????????" + e.getMessage());
        }
        return rs;
    }


    @RequestMapping(value = "/updateWeightFlag")
    @ResponseBody
    public JsonResult updateWeightFlag(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }
        try {
            customGoodsService.updateWeightFlag(pid, 0);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",updateWeightFlag error:" + e.getMessage());
            System.err.println("pid:" + pid + ",updateWeightFlag error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/deletePidImgByUrl")
    @ResponseBody
    public JsonResult deletePidImgByUrl(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }
        String imgUrl = request.getParameter("imgUrl");
        if (StringUtils.isBlank(imgUrl)) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        } else if (!(imgUrl.contains("http://") || imgUrl.contains("https://"))) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        try {
            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/"));
            CustomGoodsPublish gd = customGoodsService.queryGoodsDetails(pid, 0);
            Document nwDoc = Jsoup.parseBodyFragment(gd.getEninfo());
            // ??????????????????????????? kse??????,??????div
            Elements imgEls = nwDoc.getElementsByTag("img");
            for (Element imgEl : imgEls) {
                if (imgEl.attr("src").contains(fileName)) {
                    imgEl.remove();
                }
            }
            gd.setEninfo(nwDoc.html());
            customGoodsService.updatePidEnInfo(gd);
            System.err.println(nwDoc.html());
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",deletePidImgByUrl error:" + e.getMessage());
            System.err.println("pid:" + pid + ",deletePidImgByUrl error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/deleteEnInfoImgByParam")
    @ResponseBody
    public JsonResult deleteEnInfoImgByParam(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        HttpSession session = request.getSession();
        // ??????  (pid:)xx;(imgUrl:)xx@(pid:)xx;(imgUrl:)xx ??????
        // 123;https://img.import-express.com/123.jpg@456;https://img.import-express.com/456.jpg
        String pidImgList = request.getParameter("pidImgList");
        if (StringUtils.isBlank(pidImgList)) {
            try {
                pidImgList = (String) session.getAttribute("pidImgList");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isBlank(pidImgList)) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        int pidTotal = 0;
        int imgTotal = 0;
        int deleteImgTotal = 0;
        try {
            // ????????????
            String[] list = pidImgList.split("@");
            imgTotal = list.length;
            Map<String, List<String>> pidImgMap = new HashMap<>(pidTotal);
            for (String lStr : list) {
                String[] tempList = lStr.split(";");
                if (pidImgMap.containsKey(tempList[0])) {
                    pidImgMap.get(tempList[0]).add(tempList[1]);
                } else {
                    List<String> imgList = new ArrayList<>();
                    imgList.add(tempList[1]);
                    pidImgMap.put(tempList[0], imgList);
                }
            }

            for (String tempPid : pidImgMap.keySet()) {
                pidTotal++;
                // ??????????????????
                try {
                    CustomGoodsPublish gd = customGoodsService.queryGoodsDetails(tempPid, 0);
                    Document nwDoc = Jsoup.parseBodyFragment(gd.getEninfo());
                    // ??????????????????????????? kse??????,??????div
                    Elements imgEls = nwDoc.getElementsByTag("img");
                    int thisPidImgTotal = imgEls.size();
                    for (Element imgEl : imgEls) {
                        for (String tempImgUrl : pidImgMap.get(tempPid)) {
                            String tempFileName = tempImgUrl.substring(tempImgUrl.lastIndexOf("/"));
                            if (imgEl.attr("src").contains(tempFileName)) {
                                imgEl.remove();
                                thisPidImgTotal--;
                                deleteImgTotal++;
                                break;
                            }
                        }
                    }
                    gd.setEninfo(nwDoc.html());
                    customGoodsService.updatePidEnInfo(gd);
                    // ??????????????????????????????1?????????????????????
                    if (thisPidImgTotal <= 1) {
                        customGoodsService.remarkSoftGoodsValid(tempPid, 27);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
            json.setOk(true);
            json.setMessage("pidTotal:" + pidTotal + ",imgTotal:" + imgTotal + ",success delete:" + deleteImgTotal);
            pidImgMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("imgTotal:" + imgTotal + ",deleteEnInfoImgByParam error:" + e.getMessage());
            System.err.println("imgTotal:" + imgTotal + ",deleteEnInfoImgByParam error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("imgTotal:" + imgTotal + ",????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/updateVolumeWeight")
    @ResponseBody
    public JsonResult updateVolumeWeight(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????pid??????");
            return json;
        }

        String newWeight = request.getParameter("newWeight");
        if (StringUtils.isBlank(newWeight)) {
            json.setOk(false);
            json.setMessage("??????????????????????????????");
            return json;
        }

        try {
            customGoodsService.updateVolumeWeight(pid, newWeight);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",newWeight:" + newWeight + ",updateVolumeWeight error:", e);
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/changeChineseImgToEnglishImg")
    @ResponseBody
    public JsonResult changeChineseImgToEnglishImg(HttpServletRequest request) {
        JsonResult json = new JsonResult();
        try {
            String pid = request.getParameter("pid");
            String imgUrl = request.getParameter("imgUrl");
            if (StringUtils.isBlank(pid) || StringUtils.isBlank(imgUrl)) {
                json.setOk(false);
                json.setMessage("?????????????????????");
                return json;
            }

            // ?????????????????????
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            String suffixName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
            // String imgPrePath = imgUrl.substring(0, imgUrl.lastIndexOf("/"));
            // String remotePath = GoodsInfoUtils.changeRemotePathToLocal(imgPrePath);
            String prePath = ftpConfig.getLocalDiskPath() + "importimg/" + pid + "/desc/";
            String pidEnInfoFile = prePath + suffixName;
            boolean isDown = ImgDownByOkHttpUtils.downFromImgServiceWithApache(imgUrl, pidEnInfoFile);
            // ????????????
            if (!isDown) {
                isDown = ImgDownByOkHttpUtils.downFromImgServiceWithApache(imgUrl, pidEnInfoFile);
            }
            File imgFile = new File(pidEnInfoFile);
            if (isDown && imgFile.exists() && imgFile.isFile()) {
                // ????????????????????????????????????????????????
                OkHttpClient client = OKHttpUtils.getClientInstance();

                String imageType = "image/jpg";
                RequestBody fileBody = RequestBody.create(MediaType.parse(imageType), imgFile);
                MultipartBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", imgFile.getName(), fileBody)
                        .build();
                Request okHttpRequest = new Request.Builder().addHeader("Accept", "*/*").addHeader("Connection", "close")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                        .post(body)
                        .url(OCR_URL)
                        .build();

                Response okHttpResponse = client.newCall(okHttpRequest).execute();
                if (okHttpResponse.isSuccessful()) {
                    // ????????????????????????
                    Random random = new Random();
                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                    String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                    String changeLocalFilePath = prePath + saveFilename + fileSuffix;

                    BASE64Decoder decoder = new BASE64Decoder();
                    FileUtils.writeByteArrayToFile(new File(changeLocalFilePath), decoder.decodeBuffer(okHttpResponse.body().byteStream()));

                    File checkFile = new File(changeLocalFilePath);
                    if (checkFile.exists() && checkFile.isFile()) {
                        /*boolean isSuccess = UploadByOkHttp.uploadFile(checkFile, remotePath);
                        if (!isSuccess) {
                            isSuccess = UploadByOkHttp.uploadFile(checkFile, remotePath);
                        }
                        if (isSuccess) {
                            //??????????????????
                            json.setData(imgPrePath + "/" + saveFilename + fileSuffix);
                            json.setOk(true);
                        } else {
                            json.setOk(false);
                            json.setMessage("????????????????????????");
                        }*/

                        json.setData(ftpConfig.getLocalShowPath() + "importimg/" + pid + "/desc/" + saveFilename + fileSuffix);
                        json.setOk(true);
                    } else {
                        json.setOk(false);
                        json.setMessage("?????????????????????");
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("????????????????????????");
                }
            } else {
                json.setOk(false);
                json.setMessage("??????????????????,?????????");
                return json;
            }


        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("changeChineseImgToEnglishImg", e);
            json.setOk(false);
            json.setMessage("changeChineseImgToEnglishImg error:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/checkEnInfoAndUploadImg")
    @ResponseBody
    public JsonResult checkEnInfoAndUploadImg(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            int limitNum = 100;
            int maxId = customGoodsService.queryMaxIdFromCustomGoods();
            int fc = maxId / limitNum;
            if (maxId % limitNum > 0) {
                fc++;
            }
            int count = 0;
            List<GoodsParseBean> list;
            for (int i = 1; i <= fc; i++) {
                list = customGoodsService.queryCustomGoodsByLimit((i - 1) * limitNum, i * limitNum);
                if (list != null && list.size() > 0) {
                    for (GoodsParseBean gd : list) {
                        praseEninfoAndUpdate(gd);
                        if (gd.getAliUpdate() > 0) {
                            count++;
                            // break;
                        }
                    }
                }
                if (count > 0) {
                    // break;
                }
                list.clear();
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/setGoodsOverSea")
    @ResponseBody
    public JsonResult setGoodsOverSea(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }
        String countryId = request.getParameter("countryId");
        if (StringUtils.isBlank(countryId)) {
            json.setOk(false);
            json.setMessage("??????countryId??????");
            return json;
        }
        String isSupport = request.getParameter("isSupport");
        if (StringUtils.isBlank(isSupport)) {
            json.setOk(false);
            json.setMessage("????????????????????????");
            return json;
        }
        String categoryIdStr = request.getParameter("categoryId");
        int categoryId = 0;
        if (StringUtils.isBlank(categoryIdStr)) {
            json.setOk(false);
            json.setMessage("???????????????????????????");
            return json;
        }

        try {
            categoryId = Integer.parseInt(categoryIdStr);
            int isUpdate = 0;
            List<GoodsOverSea> goodsOverSeaList = customGoodsService.queryGoodsOverSeaInfoByPid(pid);
            if (CollectionUtils.isNotEmpty(goodsOverSeaList)) {
                for (GoodsOverSea goods : goodsOverSeaList) {
                    if (goods.getCountryId() == Integer.parseInt(countryId)) {
                        isUpdate = 1;
                        break;
                    }
                }
            }

            int supportFlag = Integer.parseInt(isSupport);
            GoodsOverSea overSea = new GoodsOverSea();
            overSea.setAdminId(admuser.getId());
            overSea.setPid(pid);
            overSea.setCountryId(Integer.parseInt(countryId));
            overSea.setIsSupport(supportFlag);


            if (isUpdate > 0) {
                customGoodsService.updateGoodsOverSeaInfo(overSea);
                String sql = "update custom_goods_oversea set is_support = " + isSupport + " ,admin_id = " + admuser.getId() + " " +
                        "where pid =  " + pid + " and country_id = " + countryId;
                NotifyToCustomerUtil.sendSqlByMq(sql);
            } else {
                customGoodsService.insertIntoGoodsOverSeaInfo(overSea);
                String sql = "insert into custom_goods_oversea(pid,country_id,admin_id,is_support)" +
                        " values('" + pid + "'," + countryId + "," + admuser.getId() + "," + isSupport + ")";
                NotifyToCustomerUtil.sendSqlByMq(sql);
            }

            // ??????????????????
            if (supportFlag > 0) {
                // ??????
                saveHotGoods(pid, categoryId, admuser.getId());
            } else {
                // ??????
                hotGoodsService.deleteGoodsByPid(categoryId, pid);
                String sqlDel = "delete from hot_selling_goods where hot_selling_id = " + categoryId + " and goods_pid = '" + pid + "'";
                NotifyToCustomerUtil.sendSqlByMq(sqlDel);
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/getAllZone")
    @ResponseBody
    public JsonResult getAllZone(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String isUsd = request.getParameter("isUsd");
        try {
            IZoneServer os = new ZoneServer();
            List<ZoneBean> zoneBeanList = os.getAllZone();
            if (StringUtils.isNotBlank(isUsd) && Integer.parseInt(isUsd) > 0) {
                List<ZoneBean> listNew = zoneBeanList.stream().filter(e -> {
                    return "USA".equalsIgnoreCase(e.getCountry());
                }).collect(Collectors.toList());

                json.setData(listNew);
                zoneBeanList.clear();
            } else {
                json.setData(zoneBeanList);
            }

            json.setOk(true);

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/setSearchable")
    @ResponseBody
    public JsonResult setSearchable(HttpServletRequest request, String pid, Integer flag) {
        JsonResult json = new JsonResult();
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }
        if (flag == null || flag < 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        try {
            InputData inputData = new InputData('u'); //u???????????????c???????????????d????????????
            inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
            inputData.setPid(pid);
            inputData.setSearchable(String.valueOf(flag));
            boolean isSu = GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
            if (isSu) {
                customGoodsService.setSearchable(pid, flag, admuser.getId());
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("??????mongodb??????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setTopSort")
    @ResponseBody
    public JsonResult setTopSort(HttpServletRequest request, String pid, Integer newSort) {
        JsonResult json = new JsonResult();
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }
        if (newSort == null || newSort < 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        try {
            InputData inputData = new InputData('u'); //u???????????????c???????????????d????????????
            inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
            inputData.setPid(pid);
            inputData.setTop_sort(String.valueOf(newSort));
            boolean isSu = GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
            // boolean isSu = true;
            if (isSu) {
                customGoodsService.setTopSort(pid, newSort, admuser.getId());
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("??????mongodb??????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setSalable")
    @ResponseBody
    public JsonResult setSalable(HttpServletRequest request, String pid, Integer flag) {
        JsonResult json = new JsonResult();
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("??????PID??????");
            return json;
        }
        if (flag == null || flag < 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        try {
            InputData inputData = new InputData('u'); //u???????????????c???????????????d????????????
            inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
            inputData.setPid(pid);
            inputData.setSalable(String.valueOf(flag));
            boolean isSu = GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
            // boolean isSu = true;
            if (isSu) {
                customGoodsService.setSalable(pid, flag, admuser.getId());
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("??????mongodb??????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/testOkHttp")
    @ResponseBody
    public JsonResult testOkHttp() {
        JsonResult json = new JsonResult();
        try {
            // ??????????????????
            File testFile = new File("/home/data/cbtconsole/cbtimg/test");
            String filePath = "/data/importcsvimg/test/1122456";
            UploadByOkHttp.uploadFileBatchOld(testFile, filePath);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("????????????????????????" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/changePidToNewCatid")
    @ResponseBody
    public JsonResult changePidToNewCatid(HttpServletRequest request, @RequestParam(name = "pid", required = true) String pid,
                                          @RequestParam(name = "oldCatid", required = true) String oldCatid,
                                          @RequestParam(name = "newCatid", required = true) String newCatid) {
        JsonResult json = new JsonResult();
        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if (admuser == null || admuser.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        try {
            // ???????????????
            CategoryBean oldBean = categoryService.queryCategoryById(newCatid);
            if (oldBean == null) {
                json.setOk(false);
                json.setMessage("????????????ID");
            } else {
                CustomGoodsPublish good = new CustomGoodsPublish();
                good.setPid(pid);
                good.setCatid1(newCatid);
                good.setPathCatid(oldBean.getPath());
                InputData inputData = new InputData('u'); //u???????????????c???????????????d????????????
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
                inputData.setPid(pid);
                inputData.setCatid1(good.getCatid1());
                inputData.setPath_catid(good.getPathCatid());

                boolean isSu = GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
                if (isSu) {
                    good.setAdminId(admuser.getId());
                    good.setCatid(oldCatid);
                    categoryService.changePidToNewCatid(good);
                    json.setOk(true);
                } else {
                    json.setOk(false);
                    json.setMessage("??????MongoDB??????????????????");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("changePidToNewCatid ????????????????????????" + e.getMessage());
        }
        return json;
    }


    private void praseEninfoAndUpdate(GoodsParseBean gd) {
        try {
            // ????????????????????????
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            JsonResult json = new JsonResult();
            // ??????????????????????????????????????????
            GetConfigureInfo.checkFtpConfig(ftpConfig, json);
            //??????eninfo??????
            Document nwDoc = Jsoup.parseBodyFragment(gd.getEnInfo());
            Elements imgEls = nwDoc.getElementsByTag("img");
            if (imgEls.size() > 0) {
                int count = 0;
                int isKids = checkIsKidsCatid(gd.getCatid1()) ? 1 : 0;
                String servicePrePath = GoodsInfoUtils.changeRemotePathToLocal(gd.getRemotePath() + gd.getPid() + "/desc/", isKids);
                String localPrePath = ftpConfig.getLocalDiskPath() + "checkImg/" + gd.getPid() + "/desc/";
                String remotePrePath = GoodsInfoUtils.changeLocalPathToRemotePath(servicePrePath).replace("kidscharming", "import-express");
                Random random = new Random();
                for (Element imel : imgEls) {
                    String imgUrl = imel.attr("src");
                    if (imgUrl == null || "".equals(imgUrl)) {
                        continue;
                    } else if (imgUrl.contains("http://") || imgUrl.contains("https://")) {
                        if (imgUrl.contains("192.168.") || imgUrl.contains(".import-express.")) {
                            continue;
                        }
                        count++;
                        if (json.isOk()) {
                            String localDiskPath = ftpConfig.getLocalDiskPath();
                            if (!(imgUrl == null || "".equals(imgUrl.trim()) || imgUrl.length() < 10)) {
                                // ???????????????????????????
                                if (imgUrl.indexOf("?") > -1) {
                                    imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                                }
                                // ????????????http?????????src
                                if (imgUrl.indexOf("//") == 0) {
                                    imgUrl = "http:" + imgUrl;
                                }
                                // ????????????????????????
                                String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                                // ????????????????????????
                                String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                                // ??????????????????????????????
                                String localFilePath = "checkImg/" + gd.getPid() + "/desc/" + saveFilename + fileSuffix;
                                // ???????????????????????????
                                boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                                if (is) {
                                    // ????????????????????????????????????100*100
                                    boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 40, 40);
                                    if (checked) {
                                        // ????????????????????????????????????700*400????????????????????????????????????
                                        checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 400);
                                        if (checked) {
                                            String newLocalPath = "checkImg/" + gd.getPid() + "/desc/" + saveFilename + "_700" + fileSuffix;
                                            checked = ImageCompression.reduceImgByWidth(700.00, localDiskPath + localFilePath,
                                                    localDiskPath + newLocalPath);
                                            if (checked) {
                                                imel.attr("src", remotePrePath + saveFilename + "_700" + fileSuffix);
                                            } else {
                                                json.setOk(false);
                                                json.setMessage("???????????????700*700?????????????????????");
                                                count--;
                                                imel.remove();
                                            }
                                        } else {
                                            imel.attr("src", remotePrePath + saveFilename + fileSuffix);
                                        }
                                    } else {
                                        // ????????????????????????????????????
                                        File file = new File(localDiskPath + localFilePath);
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                        imel.remove();
                                        count--;
                                    }
                                } else {
                                    json.setOk(false);
                                    json.setMessage("?????????????????????????????????????????????");
                                    break;
                                }
                            }
                        }
                    }
                }
                if (count > 0) {
                    gd.setAliUpdate(1);
                    // ???????????????????????????
                    File tempFile = new File(localPrePath);
                    boolean isUpload = false;
                    if (tempFile.exists() && tempFile.isDirectory()) {
                        if (tempFile.listFiles().length > 0) {
                            if (isKids > 0) {
                                isUpload = UploadByOkHttp.uploadFileBatchAll(tempFile, servicePrePath);
                                if (!isUpload) {
                                    isUpload = UploadByOkHttp.uploadFileBatchAll(tempFile, servicePrePath);
                                }
                            } else {
                                isUpload = UploadByOkHttp.uploadFileBatchOld(tempFile, servicePrePath);
                                if (!isUpload) {
                                    isUpload = UploadByOkHttp.uploadFileBatchOld(tempFile, servicePrePath);
                                }
                            }

                        }
                    }
                    if (isUpload) {
                        // ???????????????????????????
                        CustomGoodsPublish gcp = new CustomGoodsPublish();
                        gcp.setPid(gd.getPid());
                        gcp.setEninfo(nwDoc.html());

                        customGoodsService.updatePidEnInfo(gcp);
                    }
                }
                imgEls.clear();
            }
            nwDoc = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private boolean checkListContains(List<String> list, String str) {
        boolean isOk = false;
        if (list == null || list.isEmpty() || StringUtils.isBlank(str)) {
            return isOk;
        } else {
            for (String tempStr : list) {
                if (str.equals(tempStr)) {
                    isOk = true;
                    break;
                }
            }
        }
        return isOk;
    }

    private boolean checkIsKidsCatid(String catid) {
        boolean isCheck = false;
        if (kidsCatidList == null || kidsCatidList.size() == 0) {
            kidsCatidList = customGoodsService.queryKidsCanUploadCatid();
        }
        for (String tempCatid : kidsCatidList) {
            if (tempCatid.equals(catid)) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    @ResponseBody
    @RequestMapping(value = "/addEnType", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResult addEnType(HttpServletRequest request) {
        DataSourceSelector.restore();
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("??????????????????");
            return json;
        }
        try {

            String enTypeName = request.getParameter("enTypeName");

            String enTypeValue = request.getParameter("enTypeValue");

            String pidStr = request.getParameter("pid");
            // ??????????????????
            CustomGoodsPublish orGoods = customGoodsService.queryGoodsDetails(pidStr, 0);
            //???????????????????????????ids???????????????????????????
            String typeDeleteIds = request.getParameter("typeDeleteIds");
            String time = String.valueOf(System.currentTimeMillis());
            List<TypeBean> newTypeList = new ArrayList<TypeBean>();
            List<TypeBean> typeList = GoodsInfoUtils.deal1688GoodsType(orGoods, false);
            String tyteLable = enTypeName;
            if (typeList != null && typeList.size() > 0) {
                for (TypeBean typeBean : typeList) {
                    if (enTypeName.equals(typeBean.getType())) {
                        tyteLable = typeBean.getLableType();
                        break;
                    }
                }
            }
            TypeBean typeBean = new TypeBean();
            typeBean.setType(enTypeName);
            typeBean.setValue(enTypeValue);
            typeBean.setLableType(tyteLable);
            typeBean.setId(time.substring(6));
            typeBean.setImg("");
            typeBean.setSell("1");
            if (typeList == null) {
                typeList = new ArrayList<>();
            }
            typeList.add(typeBean);

            System.out.println(typeList);
           /* if (StringUtils.isNotBlank(typeDeleteIds)) {
                String[] tpList = typeDeleteIds.split(",");

                if (!(tpList.length == 0 || typeList.isEmpty())) {
                    //?????????????????????
                    for (TypeBean tpBean : typeList) {
                        boolean notPt = true;
                        for (String tpId : tpList) {
                            if (tpId.equals(tpBean.getId())) {
                                notPt = false;
                                break;
                            }
                        }
                        if (notPt) {
                            newTypeList.add(tpBean);
                        }
                    }
                    //cgp.setType(newTypeList.toString());
                }
            }*/

            Date d = new Date();
            System.out.println(d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateNowStr = sdf.format(d);
            CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pidStr, 0);
            boolean isSkuFlag = false;
            if (StringUtils.isNotBlank(goods.getRangePrice())) {
                isSkuFlag = true;
            }
            List<EntypeBen> entypeBens = JSONArray.parseArray(goods.getEntypeNew(), EntypeBen.class);
            List<ImportExSku> skuNewList = JSONArray.parseArray(goods.getSku_new(), ImportExSku.class);
            List<ImportExSku> skuList = JSONArray.parseArray(goods.getSku(), ImportExSku.class);
            System.out.println(skuList);
            if (entypeBens != null && entypeBens.size() > 0) {
                for (EntypeBen entypeBen : entypeBens) {
                    if (enTypeName.equals(entypeBen.getType())) {
                        tyteLable = entypeBen.getLableType();
                        break;
                    }
                }
            }
            EntypeBen entypeBen = new EntypeBen();
            entypeBen.setType(typeBean.getType());
            entypeBen.setValue(typeBean.getValue());
            entypeBen.setId(typeBean.getId());
            entypeBen.setLableType(tyteLable);
            entypeBen.setSell(typeBean.getSell());
            entypeBen.setImg("");
            if (entypeBens == null) {
                entypeBens = new ArrayList<>();
            }
            entypeBens.add(entypeBen);
            for (TypeBean typeBean1 : typeList) {
                if (StringUtils.isNotBlank(enTypeName)
                        && !enTypeName.equals(typeBean1.getType())) {
                    ImportExSku importExSku = new ImportExSku();
                    importExSku.setSkuId(time + dateNowStr);
                    importExSku.setSpecId(time + dateNowStr);
                    importExSku.setSkuAttr(time.substring(0, 6) + ":" + time.substring(6) + ";" +
                            typeBean1.getId().substring(0, typeBean1.getId().length() - 1) + ":" + typeBean1.getId());
                    importExSku.setSkuPropIds(time.substring(6) + "," + typeBean1.getId());
                    importExSku.setFianlWeight(skuList.get(0).getFianlWeight());
                    importExSku.setVolumeWeight(skuList.get(0).getVolumeWeight());
                    importExSku.setWholesalePrice(skuList.get(0).getWholesalePrice());
                    ImportExSkuVal importExSkuVal = new ImportExSkuVal();
                    importExSkuVal.setFreeSkuPrice("0.0");
                    importExSkuVal.setActivity(true);
                    importExSkuVal.setAvailQuantity(1);
                    if (!isSkuFlag) {
                        importExSkuVal.setFreeSkuPrice(skuList.get(0).getSkuVal().getFreeSkuPrice());
                        importExSkuVal.setActSkuCalPrice(skuList.get(0).getSkuVal().getActSkuCalPrice());
                        importExSkuVal.setActSkuMultiCurrencyCalPrice(skuList.get(0).getSkuVal().getActSkuMultiCurrencyCalPrice());
                        importExSkuVal.setActSkuMultiCurrencyDisplayPrice(skuList.get(0).getSkuVal().getActSkuMultiCurrencyDisplayPrice());
                        importExSkuVal.setSkuCalPrice(skuList.get(0).getSkuVal().getSkuCalPrice());
                        importExSkuVal.setSkuMultiCurrencyCalPrice(skuList.get(0).getSkuVal().getSkuMultiCurrencyCalPrice());
                        importExSkuVal.setSkuMultiCurrencyDisplayPrice(skuList.get(0).getSkuVal().getSkuMultiCurrencyDisplayPrice());
                        importExSkuVal.setCostPrice(skuList.get(0).getSkuVal().getCostPrice());
                    }
                    importExSku.setSkuVal(importExSkuVal);
                    if (skuList == null) {
                        skuList = new ArrayList<>();
                    }
                    skuList.add(importExSku);
                }
            }

            for (TypeBean typeBean1 : typeList) {
                if (StringUtils.isNotBlank(enTypeName)
                        && !enTypeName.equals(typeBean1.getType())) {
                    ImportExSku importExSku = new ImportExSku();
                    importExSku.setSkuId(time + dateNowStr);
                    importExSku.setSpecId(time + dateNowStr);
                    importExSku.setSkuAttr(time.substring(0, 6) + ":" + time.substring(6) + ";" +
                            typeBean1.getId().substring(0, typeBean1.getId().length() - 1) + ":" + typeBean1.getId());
                    importExSku.setSkuPropIds(time.substring(6) + "," + typeBean1.getId());
                    importExSku.setFianlWeight(skuList.get(0).getFianlWeight());
                    importExSku.setVolumeWeight(skuList.get(0).getVolumeWeight());
                    importExSku.setWholesalePrice(skuList.get(0).getWholesalePrice());
                    ImportExSkuVal importExSkuVal = new ImportExSkuVal();
                    importExSkuVal.setFreeSkuPrice("0.0");
                    importExSkuVal.setActivity(true);
                    importExSkuVal.setAvailQuantity(1);
                    if (!isSkuFlag) {
                        importExSkuVal.setFreeSkuPrice(skuList.get(0).getSkuVal().getFreeSkuPrice());
                        importExSkuVal.setActSkuCalPrice(skuList.get(0).getSkuVal().getActSkuCalPrice());
                        importExSkuVal.setActSkuMultiCurrencyCalPrice(skuList.get(0).getSkuVal().getActSkuMultiCurrencyCalPrice());
                        importExSkuVal.setActSkuMultiCurrencyDisplayPrice(skuList.get(0).getSkuVal().getActSkuMultiCurrencyDisplayPrice());
                        importExSkuVal.setSkuCalPrice(skuList.get(0).getSkuVal().getSkuCalPrice());
                        importExSkuVal.setSkuMultiCurrencyCalPrice(skuList.get(0).getSkuVal().getSkuMultiCurrencyCalPrice());
                        importExSkuVal.setSkuMultiCurrencyDisplayPrice(skuList.get(0).getSkuVal().getSkuMultiCurrencyDisplayPrice());
                        importExSkuVal.setCostPrice(skuList.get(0).getSkuVal().getCostPrice());

                    }
                    importExSku.setSkuVal(importExSkuVal);
                    if (skuNewList == null) {
                        skuNewList = new ArrayList<>();
                    }
                    skuNewList.add(importExSku);
                }
            }

            if (skuNewList != null) {
                goods.setSku_new(skuNewList.toString());
            }else{
                skuNewList =  new ArrayList<>();
                ImportExSku importExSku = new ImportExSku();
                importExSku.setSkuId(time + dateNowStr);
                importExSku.setSpecId(time + dateNowStr);
                importExSku.setSkuAttr(time.substring(0, 6) + ":" + time.substring(6) + ";" +
                        typeBean.getId().substring(0, typeBean.getId().length() - 1) + ":" + typeBean.getId());
                importExSku.setSkuPropIds(time.substring(6) + "," + typeBean.getId());
                if (!isSkuFlag) {
                    ImportExSkuVal importExSkuVal = new ImportExSkuVal();
                    importExSkuVal.setFreeSkuPrice("0.0");
                    importExSkuVal.setActivity(true);
                    importExSkuVal.setAvailQuantity(1);
                    importExSku.setSkuVal(importExSkuVal);
                }
                skuNewList.add(importExSku);
            }
            if(skuList != null){
                goods.setSku(skuList.toString());
            }
            else{
                skuList =  new ArrayList<>();
                ImportExSku importExSku = new ImportExSku();
                importExSku.setSkuId(time + dateNowStr);
                importExSku.setSpecId(time + dateNowStr);
                importExSku.setSkuAttr(time.substring(0, 6) + ":" + time.substring(6) + ";" +
                        typeBean.getId().substring(0, typeBean.getId().length() - 1) + ":" + typeBean.getId());
                importExSku.setSkuPropIds(time.substring(6) + "," + typeBean.getId());
                if (!isSkuFlag) {
                    ImportExSkuVal importExSkuVal = new ImportExSkuVal();
                    importExSkuVal.setFreeSkuPrice("0.0");
                    importExSkuVal.setActivity(true);
                    importExSkuVal.setAvailQuantity(1);
                }
                skuList.add(importExSku);
            }

            goods.setEntype(typeList.toString());
            goods.setEntypeNew(entypeBens.toString());

            int result = customGoodsService.updateEntypeSkuByPid(goods);
            if (result > 0) {
                InputData inputData = new InputData('u'); //u???????????????c???????????????d????????????
                inputData.setPid(goods.getPid());
                inputData.setEntype(goods.getEntype());
                inputData.setEntype_new(goods.getEntypeNew());
                inputData.setSku(goods.getSku());
                inputData.setSku_new(goods.getSku_new());
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));
                GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
                int isEdit = customGoodsService.checkIsEditedByPid(goods.getPid());
                if (isEdit == 0) {
                    customGoodsService.insertPidIsEdited("", goods.getPid(), user.getId());
                }
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
        }
        return json;
    }


}
