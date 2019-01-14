package com.cbt.controller;

import com.alibaba.fastjson.JSON;
import com.cbt.bean.*;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.bean.Set;
import com.cbt.parse.service.*;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.GoodsEditBean;
import com.importExpress.utli.GoodsPriceUpdateUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/editc")
public class EditorController {
    private static final Log LOG = LogFactory.getLog(EditorController.class);
    private String rootPath = "F:/console/tomcatImportCsv/webapps/";
    private String localIP = "http://27.115.38.42:8083/";
    private String wanlIP = "http://192.168.1.27:8083/";
    private DecimalFormat format = new DecimalFormat("#0.00");

    private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
    // 重量清洗的访问路径
//	public static final String SHOPGOODSWEIGHTCLEARURL = "http://127.0.0.1:8080/checkimage/clear/shopGoodsWeight?";
    public static final String SHOPGOODSWEIGHTCLEARURL = "http://192.168.1.31:8080/checkimage/clear/shopGoodsWeight?";

    @Autowired
    private CustomGoodsService customGoodsService;

    @SuppressWarnings({"static-access", "unchecked"})
    @RequestMapping(value = "/detalisEdit", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView detalisEdit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataSourceSelector.restore();
        // ModelAndView mv = new ModelAndView("customgoods_detalis");
        ModelAndView mv = new ModelAndView("customgoods_detalis_new");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("uid", 0);
            return mv;
        } else {
            mv.addObject("uid", user.getId());
            mv.addObject("roletype", user.getRoletype());
        }
        // 获取需要编辑的内容
        String pid = request.getParameter("pid");
        if (pid == null || pid.isEmpty()) {
            return mv;
        }

        // 取出1688商品的全部信息
        CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);

        if (goods == null) {
            mv.addObject("uid", -1);
            return mv;
        } else if (user.getId() == 63) {
            goods.setCanEdit(0);
        }

        if (StringUtils.isNotBlank(goods.getFeeprice())) {
            goods.setFeeprice(goods.getFeeprice().replace("[", "").replace("]", "").replace("$", "@"));
        }

        if (StringUtils.isNotBlank(goods.getWprice())) {
            goods.setWprice(goods.getWprice().replace("[", "").replace("]", "").replace("$", "@"));
        }

        if (!"".equals(goods.getOcrSizeInfo1())) {
            request.setAttribute("ocrSizeInfo1", goods.getOcrSizeInfo1());
        }
        if (!"".equals(goods.getOcrSizeInfo2())) {
            request.setAttribute("ocrSizeInfo2", goods.getOcrSizeInfo2());
        }
        if (!"".equals(goods.getOcrSizeInfo3())) {
            request.setAttribute("ocrSizeInfo3", goods.getOcrSizeInfo3());
        }

        // 根据shopid查询店铺数据
        int queryId = 0;
        if (!(goods.getShopId() == null || "".equals(goods.getShopId()))) {
            ShopManagerPojo spmg = customGoodsService.queryByShopId(goods.getShopId());
            if (spmg != null) {
                queryId = spmg.getId();
            }
        }

        mv.addObject("shopId", queryId);
        //查询商品评论信息
        List<CustomGoodsPublish> reviewList=customGoodsService.getAllReviewByPid(pid);
        request.setAttribute("reviewList", JSONArray.fromObject(reviewList));
        // 取出主图筛选数量
        GoodsPictureQuantity pictureQt = customGoodsService.queryPictureQuantityByPid(pid);
        pictureQt.setImgDeletedSize(pictureQt.getTypeOriginalSize() + pictureQt.getImgOriginalSize()
                - pictureQt.getImgSize() - pictureQt.getTypeSize());
        // pictureQt.setTypeDeletedSize(pictureQt.getTypeOriginalSize()-pictureQt.getTypeSize());
        pictureQt.setInfoDeletedSize(pictureQt.getInfoOriginalSize() - pictureQt.getInfoSize());
        request.setAttribute("pictureQt", pictureQt);

        // 取出1688原货源链接

        // 将goods的entype属性值取出来,即规格图
        List<TypeBean> typeList = GoodsInfoUtils.deal1688GoodsType(goods, true);

        // 将goods的img属性值取出来,即橱窗图
        request.setAttribute("showimgs", JSONArray.fromObject("[]"));
        List<String> imgs = GoodsInfoUtils.deal1688GoodsImg(goods.getImg(), goods.getRemotpath());
        if (imgs.size() > 0) {
            request.setAttribute("showimgs", JSONArray.fromObject(imgs));
            String firstImg = imgs.get(0);

            goods.setShowMainImage(firstImg.replace(".60x60.", ".400x400."));
        }

        HashMap<String, String> pInfo = GoodsInfoUtils.deal1688Sku(goods);
        request.setAttribute("showattribute", pInfo);


        request.setAttribute("isSoldFlag", goods.getSoldFlag());


        // 处理Sku数据
        // 判断是否是区间价格，含有区间价格的获取sku数据进行处理
        if (StringUtils.isNotBlank(goods.getRangePrice()) && StringUtils.isNotBlank(goods.getSku())) {
            List<ImportExSku> skuList = new ArrayList<ImportExSku>();
            JSONArray sku_json = JSONArray.fromObject(goods.getSku());
            skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
            // 规格标题名称集合
            List<ImportExSkuShow> cbSkus = GoodsInfoUtils.combineSkuList(typeList, skuList);
            // 集合排序
            Collections.sort(cbSkus, new Comparator<ImportExSkuShow>() {
                public int compare(ImportExSkuShow o1, ImportExSkuShow o2) {
                    return o1.getPpIds().compareTo(o2.getPpIds());
                }
            });
            request.setAttribute("showSku", JSONArray.fromObject(cbSkus));

            Map<String, Object> typeNames = new HashMap<String, Object>();
            for (TypeBean tyb : typeList) {
                if (!typeNames.containsKey(tyb.getTypeId())) {
                    typeNames.put(tyb.getTypeId(), tyb.getType());
                }
            }
            request.setAttribute("typeNames", typeNames);
        }

        //判断是否是免邮商品(isSoldFlag > 0)，如果是则显示免邮价格显示
        if (goods.getSoldFlag() > 0) {
            if (StringUtils.isNotBlank(goods.getFeeprice())) {
                request.setAttribute("feePrice", goods.getFeeprice());
            } else {
                request.setAttribute("feePrice", "");
            }
        }


        if (typeList.size() > 0) {
            request.setAttribute("showtypes", JSONArray.fromObject(typeList));
        } else {
            request.setAttribute("showtypes", JSONArray.fromObject("[]"));
        }

        //进行利润率计算,区分免邮和费免邮商品
        goods.setWeight(StrUtils.matchStr(goods.getWeight(), "(\\d+\\.*\\d*)"));
        //运费计算公式
        double freight = 0.076 * Double.valueOf(goods.getFinalWeight()) * 1000;
        //获取1688价格(1piece)
        String wholePriceStr = goods.getWholesalePrice();
        if (StringUtils.isNotBlank(wholePriceStr)) {
            String firstPrice = wholePriceStr.split(",")[0].split("\\$")[1].trim();
            firstPrice = firstPrice.replace("]", "");
            double wholePrice = 0;
            if (firstPrice.contains("-")) {
                wholePrice = Double.valueOf(firstPrice.split("-")[1].trim());
            } else {
                wholePrice = Double.valueOf(firstPrice.trim());
            }
            //判断免邮非免邮
            double oldProfit = 0;
            double singlePrice = 0;
            String singlePriceStr = "0";
            if (goods.getSoldFlag() > 0) {
                //先取range_price 为空则再取feeprice
                if (StringUtils.isNotBlank(goods.getRangePrice())) {
                    if(goods.getRangePrice().contains("-")){
                        singlePriceStr = goods.getRangePrice().split("-")[1].trim();
                    }else{
                        singlePriceStr = goods.getRangePrice().trim();
                    }
                } else if (StringUtils.isNotBlank(goods.getFeeprice())) {
                    singlePriceStr = goods.getFeeprice().split(",")[0];
                    if (singlePriceStr.contains("\\$")) {
                        singlePriceStr = singlePriceStr.split("\\$")[1].trim();
                    } else if (singlePriceStr.contains("@")) {
                        singlePriceStr = singlePriceStr.split("@")[1].trim();
                    } else {
                        singlePriceStr = singlePriceStr.trim();
                    }
                }
            } else {
                //先取range_price 为空则wprice 再为空取price
                if (StringUtils.isNotBlank(goods.getRangePrice())) {
                    if(goods.getRangePrice().contains("-")){
                        singlePriceStr = goods.getRangePrice().split("-")[1].trim();
                    }else{
                        singlePriceStr = goods.getRangePrice().trim();
                    }
                } else if (StringUtils.isNotBlank(goods.getFeeprice())) {
                    singlePriceStr = goods.getFeeprice().split(",")[0];
                    if (singlePriceStr.contains("\\$")) {
                        singlePriceStr = singlePriceStr.split("\\$")[1].trim();
                    } else if (singlePriceStr.contains("@")) {
                        singlePriceStr = singlePriceStr.split("@")[1].trim();
                    } else {
                        singlePriceStr = singlePriceStr.trim();
                    }
                } else {
                    singlePriceStr = goods.getPrice();
                }
            }
            singlePriceStr = singlePriceStr.replace("[", "").replace("]", "");
            //获取1piece的最高价格
            if (singlePriceStr.contains("-")) {
                singlePrice = Double.valueOf(singlePriceStr.split("-")[1].trim());
            } else {
                singlePrice = Double.valueOf(singlePriceStr);
            }
            //计算利润率
            //oldProfit = (singlePrice * 6.6 - wholePrice) / wholePrice * 100;
            //goods.setOldProfit(BigDecimalUtil.truncateDouble(oldProfit,2));


            //计算加价率
            if ((goods.getIsBenchmark() == 1 && goods.getBmFlag() == 1) || goods.getIsBenchmark() == 2) {
                //对标时
                //priceXs = (aliFinalPrice(速卖通价格)-feepriceSingle(运费0.076)/StrUtils.EXCHANGE_RATE(6.6))/(factory(1688人民币p1价格)/StrUtils.EXCHANGE_RATE(6.6));
                double priceXs = (Double.valueOf(goods.getAliGoodsPrice()) * GoodsPriceUpdateUtil.EXCHANGE_RATE - freight) / wholePrice;
                //加价率
                oldProfit = GoodsPriceUpdateUtil.getAddPriceJz(priceXs);
                goods.setOldProfit(BigDecimalUtil.truncateDouble(oldProfit, 2));
            } else {
                //非对标
                //catXs = DetailDataUtils.getCatxs(catid1【产品表catid1】,String.valueOf(factory)[1688人民币p1]);
                double catXs = GoodsPriceUpdateUtil.getCatxs(goods.getCatid1(), String.valueOf(wholePrice));
                //加价率= 0.55+类别调整值
                oldProfit = 0.55 + catXs;
                goods.setOldProfit(BigDecimalUtil.truncateDouble(oldProfit, 2));
            }
        } else {
            System.err.println("pid:" + pid + ",wholePrice is null");
        }


        // 直接使用远程路径
        String localpath = goods.getRemotpath();
        // 设置默认图的路径
        if (!(goods.getShowMainImage().indexOf("http://") > -1 || goods.getShowMainImage().indexOf("https://") > -1)) {
            goods.setShowMainImage(localpath + goods.getShowMainImage());
        }
        // 分割eninfo数据，不替换remotepath相同的路径
        String enInfo = goods.getEninfo().replaceAll("<br><img", "<img").replaceAll("<br /><img", "<img");
        // 使用img标签进行分割
        String[] enInfoLst = enInfo.split("<img");
        StringBuffer textBf = new StringBuffer();
        for (String srcStr : enInfoLst) {
            // 判断是否含有全路径的图片
            if (srcStr.indexOf("http:") > -1 || srcStr.indexOf("https:") > -1) {
                // 是否存在img标签的判断，使用img含有src的判断
                if (srcStr.indexOf("src=") > -1) {
                    textBf.append("<br><img " + srcStr);
                } else {
                    textBf.append(srcStr);
                }
            } else {
                // 是否存在img标签的判断，使用img含有src的判断
                if (srcStr.indexOf("src=") > -1) {
                    textBf.append("<br><img " + srcStr.replaceAll("src=\"", "src=\"" + localpath));
                } else {
                    textBf.append(srcStr);
                }
            }
        }
        // 使用完成后清理数据
        enInfoLst = null;

        // 判断是否是人为修改的重量，如果是则显示修改的重量，否则显示默认的重量
        if (goods.getReviseWeight() == null || "".equals(goods.getReviseWeight())) {
            goods.setReviseWeight(goods.getFinalWeight());
        }

        String text = textBf.toString();

        // 已经放入产品表的size_info_en字段
        //获取文字尺码数据
        // String wordSizeInfo = customGoodsService.getWordSizeInfoByPid(pid);
        String wordSizeInfo = goods.getSizeInfoEn();
        if (StringUtils.isNotBlank(wordSizeInfo)) {
            if (wordSizeInfo.indexOf("[") == 0) {
                wordSizeInfo = wordSizeInfo.substring(1);
            }
            if (wordSizeInfo.lastIndexOf("]") == wordSizeInfo.length() - 1) {
                wordSizeInfo = wordSizeInfo.substring(0, wordSizeInfo.length() - 1);
            }
            goods.setSizeInfoEn(wordSizeInfo);
        }

        // 当前抓取aliexpress的商品数据
        boolean isLocal = true;
        if (!(goods.getAliGoodsPid() == null || "".equals(goods.getAliGoodsPid()))) {
            GoodsBean algood = null;
            String aliUrl = "https://www.aliexpress.com/item/"
                    + (goods.getAliGoodsName() == null ? "ali goods" : goods.getAliGoodsName())
                    + "/" + goods.getAliGoodsPid() + ".html";
            goods.setAliGoodsUrl(aliUrl);
            System.err.println("url:" + aliUrl);
            if (isLocal) {
                // 本地直接获取ali商品信息
                algood = ParseGoodsUrl.parseGoodsw(aliUrl, 3);
            } else {
                // 远程访问获取ali商品信息
                String resultJson = DownloadMain.getContentClient(ContentConfig.CRAWL_ALI_URL + aliUrl,
                        null);
                JSONObject goodsObj = new JSONObject().fromObject(resultJson);
                algood = (GoodsBean) JSONObject.toBean(goodsObj, GoodsBean.class);
            }

            if (algood == null || algood.getValid() == 0) {
                goods.setAliGoodsName("get aliexpress goodsinfo failure this is a test goods");
                goods.setAliGoodsImgUrl(
                        "https://ae01.alicdn.com/kf/HTB1AYzjSpXXXXbHXpXXq6xXFXXXx/960P-1-3MP-HD-Wireless-IP-Camera-wi-fi-Robot-camera-Wifi-Night-Vision-Camera-IP.jpg");
            } else {
                // goods.setAliGoodsName(algood.getpName());
                // if (algood.getImgSize().length >= 2) {
                // goods.setAliGoodsImgUrl(algood.getpImage().get(0) +
                // algood.getImgSize()[1]);
                // } else {
                // goods.setAliGoodsImgUrl(algood.getpImage().get(0) +
                // algood.getImgSize()[0]);
                // }
                // 获取ali商品的详情文字，并去掉文字中敏感词的数据

                getTextByHtml(goods, algood, isLocal);
            }
        }
        // 返回待编辑数据到编辑页面
        mv.addObject("text", text);
        mv.addObject("pid", pid);

        mv.addObject("goods", goods);
        // 上传图片保存路径----酌情配置
        String savePath = localpath.replace(localIP, rootPath);
        if (IpCheckUtil.checkIsIntranet(request)) {
            savePath = localpath.replace(wanlIP, rootPath);
        }
        mv.addObject("savePath", savePath);
        mv.addObject("localpath", localpath);

        return mv;
    }

    private void getTextByHtml(CustomGoodsPublish goods, GoodsBean algood, boolean isLocal) {
        // 敏感词库
        List<String> sensitiveWords = new ArrayList<String>();
        sensitiveWords.add("Aliexpress:");
        sensitiveWords.add("aliexpress:");
        sensitiveWords.add("alibaba:");
        sensitiveWords.add("Alibaba:");
        sensitiveWords.add("QQ:");
        sensitiveWords.add("qq:");
        sensitiveWords.add("微信:");
        sensitiveWords.add("weixin:");
        sensitiveWords.add("WeiXin:");
        sensitiveWords.add("Logistics:");
        sensitiveWords.add("logistics:");
        sensitiveWords.add("Ships From:");
        sensitiveWords.add("ships from:");

        // 判断商品是否编辑过，是否有infourl链接，没有编辑和有则infourl链接的则进行信息获取
        if (!(algood.getInfourl() == null || "".equals(algood.getInfourl().trim()))) {
            String page = "";
            if (isLocal) {
                // 本地解析
                Set set = new Set();
                page = DownloadMain.getJsoup("https:" + algood.getInfourl(), 1, set);
            } else {
                // 远程解析
                page = DownloadMain.getContentClient(ContentConfig.CRAWL_ALI_INFO_URL + algood.getInfourl(), null);
            }
            if (page == null || page.isEmpty() || "".equals(page.trim()) || "httperror".equals(page.trim())) {
                System.err.println(algood.getInfourl() + ":获取失败");
            } else {
                goods.setAliGoodsInfo(dealAliInfoData(page.replaceAll("window.productDescription=\'", "")));
            }
            page = null;
        }
    }


    /**
     * 处理阿里详情数据
     *
     * @param content
     * @return
     * @author jxw
     * @date 2017-11-10
     */
    private String dealAliInfoData(String content) {

        Document nwDoc = Jsoup.parseBodyFragment(content);
        // 移除所有的页面效果 kse标签,实际div
        Elements divLst = nwDoc.getElementsByTag("div");
        for (Element dEl : divLst) {
            if (!(dEl.attr("name") == null || "".equals(dEl.attr("name").trim()))) {
                if ("productItem".equalsIgnoreCase(dEl.attr("name").trim())) {
                    dEl.remove();
                    continue;
                }
            }
            // 移除所有div下面包含a标签的数据
            Elements aLst = dEl.getElementsByTag("a");
            if (aLst.size() > 0) {
                dEl.remove();
            }
        }
        // 移除所有的 a标签
        Elements aLst = nwDoc.getElementsByTag("a");
        for (Element ael : aLst) {
            ael.remove();
        }

        // 移除所有的 包裹列表div主体
        nwDoc.select(".pnl-packaging-main").remove();
        // 移除所有的 买家交易信息div主体
        nwDoc.select(".transaction-feedback-main").remove();
        // 移除所有的 更多产品，相关产品
        nwDoc.select(".related-products-main").remove();
        // 移除所有的 相关产品搜索
        nwDoc.select("#j-related-searches").remove();

        // 移除所有的 img属性含有以下字符的图片
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
            // 移除所有的 a标签
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
                json.setMessage("获取登录信息失败，请登录");
                return json;
            }

            CustomGoodsPublish cgp = new CustomGoodsPublish();

            String contentStr = request.getParameter("content");
            // String localpath = request.getParameter("localpath");

            String pidStr = request.getParameter("pid");
            if (!(pidStr == null || "".equals(pidStr))) {
                cgp.setPid(pidStr);
            } else {
                json.setOk(false);
                json.setMessage("获取pid失败");
                return json;
            }
            String sellUtil = request.getParameter("sellUtil");
            if (StringUtils.isNotBlank(sellUtil)) {
                cgp.setSellUnit(sellUtil);
            } else {
                json.setOk(false);
                json.setMessage("获取售卖单位失败");
                return json;
            }
            // 获取商品信息
            CustomGoodsPublish orGoods = customGoodsService.queryGoodsDetails(pidStr, 0);

            //判断售卖单位是否一致
//            if(sellUtil.equalsIgnoreCase(orGoods.getSellUnit())){
//                cgp.setSellUnit(null);
//            }

            String orFinalWeight = orGoods.getFinalWeight();
            String remotepath = request.getParameter("remotepath");
            if (remotepath == null || "".equals(remotepath)) {
                json.setOk(false);
                json.setMessage("获取图片远程路径失败");
                return json;
            } else {
                cgp.setRemotpath(remotepath);
            }
            String enname = request.getParameter("enname");
            if (!(enname == null || "".equals(enname))) {
                cgp.setEnname(enname);
            } else {
                json.setOk(false);
                json.setMessage("获取产品名称失败");
                return json;
            }
            //String weightStr = request.getParameter("weight");
            /*if (!(weightStr == null || "".equals(weightStr))) {
                // 判断重量是否被修改,只有被修改后才进行更新重量和运费
                // 判断显示的重量是否来自weight字段的值，是则不更新数据
                if (weightStr.equals(orGoods.getFinalWeight())) {
                    cgp.setReviseWeight("0");
                    cgp.setFeeprice("0");
                } else {
                    // 判断显示的重量是否来自reviseWeight字段的值，是则不更新数据
                    if (weightStr.equals(orGoods.getReviseWeight())) {
                        cgp.setReviseWeight("0");
                        cgp.setFeeprice("0");
                    } else {
                        cgp.setReviseWeight(weightStr);
                        // 更新运费，逻辑：运输方式E邮宝，运费计算公式（0.08*克重+9）/6.75
                        DecimalFormat df = new DecimalFormat("######0.00");
                        // 判断重量是否是很小的值，如果是很小值则设置为1kg
                        double weight = Double.valueOf(weightStr) < 0.000001 ? 1.00 : Double.valueOf(weightStr);
                        double cFreight = (0.08 * weight * 1000 + 9) / Util.EXCHANGE_RATE;
                        cgp.setFeeprice(df.format(cFreight));
                    }
                }
            } else {
                json.setOk(false);
                json.setMessage("获取产品重量失败");
                return json;
            }*/
            String imgInfo = request.getParameter("imgInfo");
            if (!(imgInfo == null || "".equals(imgInfo))) {
                // 获取的橱窗图进行集合封装
                cgp.setImg("[" + imgInfo.replace(";", ",").replace(remotepath, "") + "]");
            } else {
                json.setOk(false);
                json.setMessage("获取橱窗图失败");
                return json;
            }
            String endetailStr = request.getParameter("endetail");
            if (!(endetailStr == null || "".equals(endetailStr))) {
                // 获取的商品属性进行集合封装
                cgp.setEndetail("[" + endetailStr.replaceAll(";", ", ") + "]");
            } else {
                // 不校检商品属性
                cgp.setEndetail("[]");
            }
            if (!(contentStr == null || "".equals(contentStr))) {
                // 产品详情
                String eninfo = contentStr.replaceAll(remotepath, "");
                //解析和上传阿里商品的图片
                eninfo = uploadAliImgToLocal(pidStr, eninfo);
                cgp.setEninfo(eninfo);
            } else {
                json.setOk(false);
                json.setMessage("获取商品详情失败");
                return json;
            }

            String bizPrice = request.getParameter("bizPrice");
            if (StringUtils.isNotBlank(bizPrice) || "0".equals(bizPrice)) {
                cgp.setFpriceStr(bizPrice);
            } else {
                json.setOk(false);
                json.setMessage("获取bizPrice失败");
                return json;
            }

            String rangePrice = request.getParameter("rangePrice");

            if (rangePrice == null || "".equals(rangePrice)) {
                //获取最大值和最小值信息
                String feePrice = request.getParameter("feePrice");
                double minPrice = 0;
                double maxPrice = 0;
                if (StringUtils.isNotBlank(feePrice)) {
                    String[] priceLst = feePrice.split(",");
                    minPrice = Double.valueOf(priceLst[0].split("@")[1]);
                    maxPrice = minPrice;
                    for (String priceStr : priceLst) {
                        double tempPrice = Double.valueOf(priceStr.split("@")[1]);
                        if (tempPrice < minPrice) {
                            minPrice = tempPrice;
                        }
                        if (tempPrice > maxPrice) {
                            maxPrice = tempPrice;
                        }
                    }
                    //格式化
                    DecimalFormat df = new DecimalFormat("######0.00");
                    cgp.setPrice(df.format(minPrice));
                    cgp.setFeeprice("[" + feePrice.replace("@", " $ ") + "]");
                } else {
                    String wprice = request.getParameter("wprice");
                    if (wprice == null || "".equals(wprice)) {
                        // 判断wprice是不是空的，如果是，不更新wprice和price值
                        if (orGoods.getWprice() == null || "".equals(orGoods.getWprice())
                                || orGoods.getWprice().trim().length() < 3) {
                            cgp.setPrice(orGoods.getPrice());
                            cgp.setWprice("[]", 1);
                        } else {
                            String goodsPrice = request.getParameter("goodsPrice");
                            if (StringUtils.isBlank(goodsPrice)) {
                                json.setOk(false);
                                json.setMessage("获取区间价格数据失败");
                                return json;
                            }
                        }
                    } else {
                        String[] priceLst = wprice.split(",");
                        minPrice = Double.valueOf(priceLst[0].split("@")[1]);
                        maxPrice = minPrice;
                        for (String priceStr : priceLst) {
                            double tempPrice = Double.valueOf(priceStr.split("@")[1]);
                            if (tempPrice < minPrice) {
                                minPrice = tempPrice;
                            }
                            if (tempPrice > maxPrice) {
                                maxPrice = tempPrice;
                            }
                        }
                        //格式化
                        DecimalFormat df = new DecimalFormat("######0.00");
                        cgp.setPrice(df.format(minPrice));
                        cgp.setWprice("[" + wprice.replace("@", " $ ") + "]");
                    }
                }
            } else {
                String sku = request.getParameter("sku");
                if (sku == null || "".equals(sku)) {
                    json.setOk(false);
                    json.setMessage("获取单规格价数据失败");
                    return json;
                } else {
                    JSONArray sku_json = JSONArray.fromObject(orGoods.getSku());
                    List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
                    boolean isSuccess = dealSkuByParam(skuList, sku, cgp);
                    if (!isSuccess) {
                        json.setOk(false);
                        json.setMessage("商品单规格价格生成异常，请确认价格！");
                        return json;
                    }
                }
            }

            // 判断是否改价 wprice range_price feeprice price  fprice_str
            if(checkPriceIsUpdate(cgp,orGoods)){
                System.err.println("pid:" + pidStr + ",not update price");
            }else{
                cgp.setPriceIsEdit(1);
            }

            //获取需要删除的规格ids数据，进行匹配删除
            String typeDeleteIds = request.getParameter("typeDeleteIds");

            List<TypeBean> newTypeList = new ArrayList<TypeBean>();
            List<TypeBean> typeList = GoodsInfoUtils.deal1688GoodsType(orGoods, false);
            if (StringUtils.isNotBlank(typeDeleteIds)) {
                String[] tpList = typeDeleteIds.split(",");

                if (!(tpList.length == 0 || typeList.isEmpty())) {
                    boolean notPt = true;
                    //剔除选中的规格
                    for (TypeBean tpBean : typeList) {
                        for (String tpId : tpList) {
                            if (tpId.equals(tpBean.getId())) {
                                notPt = false;
                                break;
                            }
                        }
                        if (notPt) {
                            newTypeList.add(tpBean);
                        }
                        notPt = true;
                    }
                    //cgp.setType(newTypeList.toString());
                }
            }
            //获取替换规格的数据
            String typeRepalceIds = request.getParameter("typeRepalceIds");

            String[] tpList;
            if (StringUtils.isNotBlank(typeRepalceIds)) {
                //进行数据的分割
                tpList = typeRepalceIds.split(",");
                String[] spSt;
                for (String tpCt : tpList) {
                    spSt = tpCt.split("@");
                    if (spSt.length == 2) {
                        //判断是否存在删除的规格，如果存在则用newTypeList,否则用typeList
                        if (StringUtils.isNotBlank(typeDeleteIds)) {
                            for (TypeBean nwType : newTypeList) {
                                if (nwType.getId().equals(spSt[0]) && !nwType.getType().trim().equals(spSt[1].trim())) {
                                    nwType.setValue(spSt[1].trim());
                                    break;
                                }
                            }
                        } else {
                            for (TypeBean nwType : typeList) {
                                if (nwType.getId().equals(spSt[0]) && !nwType.getType().trim().equals(spSt[1].trim())) {
                                    nwType.setValue(spSt[1].trim());
                                    break;
                                }
                            }
                        }
                    }
                    spSt = null;
                }

                if (StringUtils.isNotBlank(typeDeleteIds)) {
                    cgp.setType(newTypeList.toString());
                } else {
                    cgp.setType(typeList.toString());
                }
                tpList = null;
            } else {
                if (StringUtils.isNotBlank(typeDeleteIds)) {
                    cgp.setType(newTypeList.toString());
                    newTypeList.clear();
                }
            }
            newTypeList.clear();
            typeList.clear();


            // 获取文字尺码表
            String wordSizeInfo = request.getParameter("wordSizeInfo");
            if(StringUtils.isNotBlank(wordSizeInfo)){
                cgp.setSizeInfoEn(wordSizeInfo.replace("\\n","<br>"));
            }

            // 设置主图数据 mainImg
            String mainImg = request.getParameter("mainImg");
            if(StringUtils.isNotBlank(mainImg)){
                mainImg = mainImg.replace(orGoods.getRemotpath(),"");
                // 进行主图相关的修改  替换主图数据，压缩图片为285x285或者285x380,上传服务器
                if(mainImg.contains(".60x60")){
                    cgp.setCustomMainImage(mainImg.replace(".60x60",".220x220"));
                }else if(mainImg.contains(".400x400")){
                    cgp.setCustomMainImage(mainImg.replace(".400x400",".220x220"));
                }
                cgp.setShowMainImage(mainImg);
                cgp.setIsUpdateImg(2);
            }

            GoodsEditBean editBean = new GoodsEditBean();

            String type = request.getParameter("type");
            // type 0 保存 1 保存并发布
            int tempId = user.getId();
            String tempName = user.getAdmName();
            editBean.setPublish_flag(Integer.valueOf(type));
            editBean.setAdmin_id(tempId);
            editBean.setNew_title(cgp.getEnname());
            editBean.setOld_title(orGoods.getEnname());
            editBean.setPid(cgp.getPid());

            int success = customGoodsService.saveEditDetalis(cgp, tempName, tempId, Integer.valueOf(type));
            if (success > 0) {
                customGoodsService.insertIntoGoodsEditBean(editBean);
                //更新编辑标识
                /*int isExists = customGoodsService.checkIsEditedByPid(cgp.getPid());
                if(isExists > 0){
                    customGoodsService.updatePidIsEdited(cgp.getPid(),user.getId());
                }else{
                    customGoodsService.insertPidIsEdited(orGoods.getShopId(),cgp.getPid(),user.getId());
                }*/
                editBean.setIs_edited(1);
                editBean.setPublish_flag(0);
                customGoodsService.updatePidIsEdited(editBean);


                json.setOk(true);
                if (!(type == null || "".equals(type) || "0".equals(type))) {
                    String updateTimeStr = orGoods.getUpdateTimeAll();
                    //判断不是正式环境的，不进行搜图图片更新
                    String ip = request.getRemoteAddr();

                    if (ip.contains("1.34") || ip.contains("38.42") || ip.contains("1.27") || ip.contains("1.9")) {
                        if(cgp.getIsUpdateImg() == 0){
                            cgp.setIsUpdateImg(1);
                            // 设置图片信息
                        }
                    }else{
                        cgp.setIsUpdateImg(0);
                    }
                    if (StringUtils.isNotBlank(updateTimeStr)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //离上次编辑小于15分钟，不能发布
                        if (System.currentTimeMillis() - sdf.parse(updateTimeStr).getTime() < 1000 * 60 * 15) {
                            json.setOk(false);
                            json.setMessage("数据已经保存成功，离上次发布小于15分钟，不能发布");
                        } else {
                            PublishGoodsToOnlineThread pbThread = new PublishGoodsToOnlineThread(pidStr, customGoodsService, ftpConfig, cgp.getIsUpdateImg());
                            pbThread.start();
                            json.setMessage("更新成功,异步上传图片中，请等待");
                        }
                    } else {
                        PublishGoodsToOnlineThread pbThread = new PublishGoodsToOnlineThread(pidStr, customGoodsService, ftpConfig, cgp.getIsUpdateImg());
                        pbThread.start();
                        json.setMessage("更新成功,异步上传图片中，请等待");
                    }
                } else {
                    json.setMessage("更新成功");
                }
            } else {
                json.setOk(false);
                json.setMessage("更新失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存错误，原因：" + e.getMessage());
            LOG.error("保存错误，原因：" + e.getMessage());
        }
        return json;
    }

    private boolean checkPriceIsUpdate(CustomGoodsPublish cgp,CustomGoodsPublish orGoods){
        int count = 0;
        // 判断是否改价 wprice range_price feeprice price  fprice_str,判断相同的，加一
        // wprice
        if(StringUtils.isNotBlank(cgp.getWprice()) && StringUtils.isNotBlank(orGoods.getWprice())){
            if(cgp.getWprice().equals(orGoods.getWprice())){
                count ++;
            }
        }else if(StringUtils.isBlank(cgp.getWprice()) && StringUtils.isBlank(orGoods.getWprice())){
            count ++;
        }
        // range_price
        if(StringUtils.isNotBlank(cgp.getRangePrice()) && StringUtils.isNotBlank(orGoods.getRangePrice())){
            if(cgp.getRangePrice().equals(orGoods.getRangePrice())){
                count ++;
            }
        }else if(StringUtils.isBlank(cgp.getRangePrice()) && StringUtils.isBlank(orGoods.getRangePrice())){
            count ++;
        }
        // feeprice
        if(StringUtils.isNotBlank(cgp.getFeeprice()) && StringUtils.isNotBlank(orGoods.getFeeprice())){
            if(cgp.getFeeprice().equals(orGoods.getFeeprice())){
                count ++;
            }
        }else if(StringUtils.isBlank(cgp.getFeeprice()) && StringUtils.isBlank(orGoods.getFeeprice())){
            count ++;
        }
        // price
        if(StringUtils.isNotBlank(cgp.getPrice()) && StringUtils.isNotBlank(orGoods.getPrice())){
            if(cgp.getPrice().equals(orGoods.getPrice())){
                count ++;
            }
        }else if(StringUtils.isBlank(cgp.getPrice()) && StringUtils.isBlank(orGoods.getPrice())){
            count ++;
        }
        // fprice_str
        if(StringUtils.isNotBlank(cgp.getFpriceStr()) && StringUtils.isNotBlank(orGoods.getFpriceStr())){
            if(cgp.getFpriceStr().equals(orGoods.getFpriceStr())){
                count ++;
            }
        }else if(StringUtils.isBlank(cgp.getFpriceStr()) && StringUtils.isBlank(orGoods.getFpriceStr())){
            count ++;
        }

        return count == 5;
    }

    // 处理sku数据，跟参数传递过来的价格数据进行赋值
    private boolean dealSkuByParam(List<ImportExSku> skuList, String sku, CustomGoodsPublish cgp) {
        List<ImportExSku> newSkuList = new ArrayList<ImportExSku>();

        float minPrice = 0;
        float maxPrice = 0;
        int count = 1;
        String[] skuSplits = sku.split(";");
        for (String skuIds : skuSplits) {
            String[] idAndPrice = skuIds.split("@");
            String ppids = idAndPrice[0].replace("_", ",");
            for (ImportExSku ies : skuList) {
                if (ppids.equals(ies.getSkuPropIds())) {
                    float tempPrice = Float.valueOf(idAndPrice[1]);
                    if (count == 1) {
                        minPrice = tempPrice;
                        maxPrice = tempPrice;
                        count++;
                    }
                    if (minPrice > tempPrice) {
                        minPrice = tempPrice;
                    }
                    if (maxPrice < tempPrice) {
                        maxPrice = tempPrice;
                    }
                    ies.getSkuVal().setActSkuCalPrice(tempPrice);
                    ies.getSkuVal().setActSkuMultiCurrencyCalPrice(tempPrice);
                    ies.getSkuVal().setActSkuMultiCurrencyDisplayPrice(tempPrice);
                    ies.getSkuVal().setSkuCalPrice(tempPrice);
                    ies.getSkuVal().setSkuMultiCurrencyCalPrice(tempPrice);
                    ies.getSkuVal().setSkuMultiCurrencyDisplayPrice(tempPrice);
                    newSkuList.add(ies);
                    break;
                }
            }
            ppids = null;
        }
        cgp.setRangePrice(genFloatWidthTwoDecimalPlaces(minPrice) + "-" + genFloatWidthTwoDecimalPlaces(maxPrice));
        cgp.setSku(newSkuList.toString());
        if (minPrice == 0 || maxPrice == 0 || newSkuList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 生成两位小数的float类型数据
     *
     * @param numVal
     * @return
     */
    private float genFloatWidthTwoDecimalPlaces(float numVal) {
        BigDecimal bd = new BigDecimal(numVal);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
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
            json.setMessage("获取pid失败");
            return json;
        }

        String reason = request.getParameter("reason");
        if (StringUtils.isBlank(reason)) {
            json.setOk(false);
            json.setMessage("获取下架原因失败");
            return json;
        }

        try {
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("获取登录信息失败，请登录");
                return json;
            }

            int count = customGoodsService.setGoodsValid(pidStr, user.getAdmName(), user.getId(), -1, reason);
            if (count > 0) {
                json.setOk(true);
                json.setMessage("执行成功");
            } else {
                json.setOk(false);
                json.setMessage("执行失败，请重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
            LOG.error("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
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
            json.setMessage("获取pid失败");
            return json;
        }
        try {
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("获取登录信息失败，请登录");
                return json;
            }

            int count = customGoodsService.setGoodsValid(pidStr, user.getAdmName(), user.getId(), 1, "");
            if (count > 0) {
                json.setOk(true);
                json.setMessage("执行成功");
            } else {
                json.setOk(false);
                json.setMessage("执行失败，请重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
            LOG.error("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
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
            json.setMessage("获取pid失败");
            return json;
        }
        try {
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("获取登录信息失败，请登录");
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
            json.setMessage("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
            LOG.error("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
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
            json.setMessage("获取pid失败");
            return json;
        }
        try {

            String adminId = request.getParameter("adminId");
            if (adminId == null || "".equals(adminId)) {
                json.setOk(false);
                json.setMessage("获取操作人id失败");
                return json;
            }
            // type -1 下架该商品 1 检查通过
            customGoodsService.setGoodsValid(pidStr, "", Integer.valueOf(adminId), -1, "");
            json.setOk(true);
            json.setMessage("执行成功");

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
            LOG.error("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 编辑器内容保存
     *
     * @param request
     * @param response
     * @return
     * @date 2016年12月15日
     * @author abc
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public String getSave(HttpServletRequest request, HttpServletResponse response) {
        // 获取需要保存的内容
        String pid = "";
        try {
            String content = request.getParameter("content");
            System.err.println("text----------" + content);

            CustomGoodsBean bean = new CustomGoodsBean();
            pid = request.getParameter("pid");

            String localpath = request.getParameter("localpath");
            // 产品详情
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
            // 价格区间
            String lastPrice = request.getParameter("lastPrice");
            bean.setLastPrice(lastPrice);
            lastPrice = StrUtils.isRangePrice(lastPrice) ? lastPrice : "0";
            double minPrice = Double.valueOf(lastPrice.split("-")[0]);
            double maxPrice = minPrice;

            String sku = request.getParameter("sku");
            // System.err.println("sku:"+sku);
            if (sku != null && !sku.isEmpty() && sku.startsWith("[")) {
                JSONArray sku_json = JSONArray.fromObject(sku);
                List<SkuAttrBean> skuList = (List<SkuAttrBean>) JSONArray.toCollection(sku_json, SkuAttrBean.class);
                for (SkuAttrBean skuBean : skuList) {
                    // System.err.println(skuBean.toString());
                    SkuValBean skuVal = skuBean.getSkuVal();
                    String actSkuCalPrice = request.getParameter("actSkuCalPrice_" + skuBean.getSkuPropIds());
                    double price = Double.valueOf(actSkuCalPrice);
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
        // 获取需要保存的内容
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
                    // 详情描述图片路径
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
     * 接受上传文件
     *
     * @param request
     * @param response
     * @return
     * @date 2016年12月16日
     * @author abc
     */
    @RequestMapping(value = "/uploads", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getLoads(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "";
        String err = "";
        String pid = request.getParameter("pid");

        if (pid == null || "".equals(pid)) {
            msg = "";
            err = "获取PID失败";
        } else {
            System.out.println("pid:" + pid);
            try {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                // 获取文件域
                List<MultipartFile> fileList = multipartRequest.getFiles("filedata");
                Random random = new Random();
                // 获取配置文件信息
                if (ftpConfig == null) {
                    ftpConfig = GetConfigureInfo.getFtpConfig();
                }
                // 检查配置文件信息是否正常读取
                JsonResult json = new JsonResult();
                checkFtpConfig(ftpConfig, json);
                String localDiskPath = ftpConfig.getLocalDiskPath();
                if (json.isOk()) {
                    for (MultipartFile mf : fileList) {
                        if (!mf.isEmpty()) {
                            // 得到文件保存的名称mf.getOriginalFilename()
                            String originalName = mf.getOriginalFilename();
                            // 文件的后缀取出来
                            String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                            String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                            // 本地服务器磁盘全路径
                            String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;
                            // 文件流输出到本地服务器指定路径
                            ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
                            // 检查图片分辨率
                            boolean is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
                            if (is) {
                                is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 400);
                                if (is) {
                                    String newLocalPath = "importimg/" + pid + "/" + saveFilename + "_700" + fileSuffix;
                                    is = ImageCompression.reduceImgByWidth(700.00, localDiskPath + localFilePath,
                                            localDiskPath + newLocalPath);
                                    if (is) {
                                        msg = ftpConfig.getLocalShowPath() + newLocalPath;
                                    } else {
                                        json.setOk(false);
                                        json.setMessage("压缩图片到700*700失败，终止执行");
                                        break;
                                    }
                                } else {
                                    msg = ftpConfig.getLocalShowPath() + localFilePath;
                                }
                            } else {
                                // 判断分辨率不通过删除图片
                                File file = new File(localFilePath);
                                if (file.exists()) {
                                    file.delete();
                                }
                                msg = "";
                                err = "图片分辨率小于100";
                            }
                        }
                    }
                } else {
                    msg = "";
                    err = json.getMessage();
                }
            } catch (Exception e) {
                msg = "";
                err = "上传错误";
                e.printStackTrace();
                LOG.error("上传错误：" + e.getMessage());
            }
        }
        map.put("err", err);
        map.put("msg", msg);
        return map;
    }

    /**
     * 编辑详情网路图片下载本地并上传服务器
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
            json.setMessage("获取网路图片路径失败");
            return json;
        }
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("获取pid失败");
            return json;
        }
        System.err.println("pid:" + pid + ";imgs" + imgs);

        String newImgUrl = "";
        Random random = new Random();
        String[] imgLst = imgs.split(";");
        boolean isSuccess = true;
        try {
            // 获取配置文件信息
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            // 检查配置文件信息是否正常读取
            checkFtpConfig(ftpConfig, json);
            if (!json.isOk()) {
                return json;
            }
            String localDiskPath = ftpConfig.getLocalDiskPath();
            for (String imgUrl : imgLst) {
                if (!(imgUrl == null || "".equals(imgUrl.trim()) || imgUrl.length() < 10)) {
                    // 得到文件保存的名称
                    if (imgUrl.indexOf("?") > -1) {
                        imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                    }
                    // 兼容没有http头部的src
                    if (imgUrl.indexOf("//") == 0) {
                        imgUrl = "http:" + imgUrl;
                    }
                    // 文件的后缀取出来
                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                    // 生成唯一文件名称
                    String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                    // 本地服务器磁盘全路径
                    String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;
                    // 下载网络图片到本地
                    boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                    if (is) {
                        // 判断图片的分辨率是否小于100*100
                        boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
                        if (checked) {
                            // 判断图片的分辨率是否大于700*400，如果大于则进行图片压缩
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
                                    json.setMessage("压缩图片到700*700失败，终止执行");
                                    break;
                                }
                            } else {
                                newImgUrl += ";" + ftpConfig.getLocalShowPath() + localFilePath;
                            }

                            json.setOk(true);
                            json.setMessage("图片上传本地成功");
                        } else {
                            // 判断分辨率不通过删除图片
                            File file = new File(localFilePath);
                            if (file.exists()) {
                                file.delete();
                            }
                            isSuccess = false;
                            json.setOk(false);
                            json.setMessage("图片分辨率小于100*100，终止执行");
                            break;
                        }
                    } else {
                        isSuccess = false;
                        json.setOk(false);
                        json.setMessage("下载网路图片到本地失败，请重试");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行出错：" + e.getMessage());
            LOG.error("执行出错：" + e.getMessage());
        }
        if (isSuccess) {
            json.setOk(true);
            json.setData(newImgUrl.substring(1));
            json.setMessage("执行成功");
        }

        return json;
    }

    private String uploadAliImgToLocal(String pid, String eninfo) {

        String tempEninfo = "";
        try {
            // 详情数据的获取和解析img数据
            if (StringUtils.isNotBlank(eninfo)) {
                //循环上传图片到本地
                JsonResult json = new JsonResult();
                // 获取配置文件信息
                if (ftpConfig == null) {
                    ftpConfig = GetConfigureInfo.getFtpConfig();
                }
                //解析eninfo数据
                Document nwDoc = Jsoup.parseBodyFragment(eninfo);
                Elements imgEls = nwDoc.getElementsByTag("img");
                if (imgEls.size() > 0) {
                    Random random = new Random();
                    for (Element imel : imgEls) {
                        String imgUrl = imel.attr("src");
                        if (imgUrl == null || "".equals(imgUrl)) {
                            continue;
                        } else if (imgUrl.contains("alicdn.com")) {
                            // 检查配置文件信息是否正常读取
                            checkFtpConfig(ftpConfig, json);
                            if (json.isOk()) {
                                String localDiskPath = ftpConfig.getLocalDiskPath();
                                if (!(imgUrl == null || "".equals(imgUrl.trim()) || imgUrl.length() < 10)) {
                                    // 得到文件保存的名称
                                    if (imgUrl.indexOf("?") > -1) {
                                        imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                                    }
                                    // 兼容没有http头部的src
                                    if (imgUrl.indexOf("//") == 0) {
                                        imgUrl = "http:" + imgUrl;
                                    }
                                    // 文件的后缀取出来
                                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                                    // 生成唯一文件名称
                                    String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                                    // 本地服务器磁盘全路径
                                    String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;
                                    // 下载网络图片到本地
                                    boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                                    if (is) {
                                        // 判断图片的分辨率是否小于100*100
                                        boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
                                        if (checked) {
                                            // 判断图片的分辨率是否大于700*400，如果大于则进行图片压缩
                                            checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 400);
                                            if (checked) {
                                                String newLocalPath = "importimg/" + pid + "/" + saveFilename + "_700" + fileSuffix;
                                                checked = ImageCompression.reduceImgByWidth(700.00, localDiskPath + localFilePath,
                                                        localDiskPath + newLocalPath);
                                                if (checked) {
                                                    imel.attr("src", ftpConfig.getLocalShowPath() + newLocalPath);
                                                } else {
                                                    json.setOk(false);
                                                    json.setMessage("压缩图片到700*700失败，终止执行");
                                                    break;
                                                }
                                            } else {
                                                imel.attr("src", ftpConfig.getLocalShowPath() + localFilePath);
                                            }
                                        } else {
                                            // 判断分辨率不通过删除图片
                                            File file = new File(localFilePath);
                                            if (file.exists()) {
                                                file.delete();
                                            }
                                            json.setOk(false);
                                            json.setMessage("图片分辨率小于100*100，终止执行");
                                        }
                                    } else {
                                        json.setOk(false);
                                        json.setMessage("下载网路图片到本地失败，请重试");
                                    }
                                }
                            }
                        }
                    }
                    tempEninfo = nwDoc.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + ",uploadAliImgToLocal error:" + e.getMessage());
        }
        return tempEninfo;

    }

    /**
     * 橱窗图网路图片下载本地并上传服务器
     */
    @RequestMapping(value = "/uploadTypeNetImg", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult uploadTypeNetImg(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String imgs = request.getParameter("imgs");
        if (imgs == null || "".equals(imgs)) {
            json.setOk(false);
            json.setMessage("获取网路图片路径失败");
            return json;
        }
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("获取pid失败");
            return json;
        }
        System.err.println("pid:" + pid + ";imgs" + imgs);

        String newImgUrl = "";
        Random random = new Random();
        String[] imgLst = imgs.split(";");
        boolean isSuccess = true;
        try {
            // 获取配置文件信息
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            // 检查配置文件信息是否正常读取
            checkFtpConfig(ftpConfig, json);
            if (!json.isOk()) {
                return json;
            }
            String localDiskPath = ftpConfig.getLocalDiskPath();
            for (String imgUrl : imgLst) {
                if (!(imgUrl == null || "".equals(imgUrl.trim()))) {
                    // 得到文件保存的名称
                    if (imgUrl.indexOf("?") > -1) {
                        imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                    }
                    // 兼容没有http头部的src
                    if (imgUrl.indexOf("//") == 0) {
                        imgUrl = "http:" + imgUrl;
                    }
                    // 文件的后缀取出来
                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                    // 生成唯一文件名称
                    String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                    // 本地服务器磁盘全路径
                    String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;
                    // 下载网络图片到本地
                    boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                    if (is) {
                        // 判断图片的分辨率是否小于400*200
                        boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
                        if (checked) {
                            // 压缩图片400x400
                            String localFilePath400x400 = "importimg/" + pid + "/" + saveFilename + ".400x400"
                                    + fileSuffix;

                            boolean is400 = ImageCompression.reduceImgByWidth(400, localDiskPath + localFilePath,
                                    localDiskPath + localFilePath400x400);
                            // 压缩图片60x60
                            String localFilePath60x60 = "importimg/" + pid + "/" + saveFilename + ".60x60" + fileSuffix;
                            boolean is60 = ImageCompression.reduceImgByWidth(60, localDiskPath + localFilePath,
                                    localDiskPath + localFilePath60x60);
                            if (is60 && is400) {
                                newImgUrl += ";" + ftpConfig.getLocalShowPath() + localFilePath60x60;
                                json.setOk(true);
                                json.setMessage("本地图片上传成功");
                            } else {
                                // 判断分辨率不通过删除图片
                                File file400 = new File(localDiskPath + localFilePath400x400);
                                if (file400.exists()) {
                                    file400.delete();
                                }
                                File file60 = new File(localDiskPath + localFilePath60x60);
                                if (file60.exists()) {
                                    file60.delete();
                                }
                                // 压缩失败整体终止执行
                                isSuccess = false;
                                json.setOk(false);
                                json.setMessage("压缩图片60x60和400x400失败，终止执行");
                                break;
                            }
                        } else {
                            // 图片分辨率小于400*200整体终止执行
                            isSuccess = false;
                            json.setOk(false);
                            json.setMessage("图片分辨率小于400*200，终止执行");
                            break;
                        }
                    } else {
                        isSuccess = false;
                        json.setOk(false);
                        json.setMessage("下载网路图片到本地失败，请重试");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
            json.setOk(false);
            json.setMessage("执行出错：" + e.getMessage());
        }
        if (isSuccess) {
            json.setOk(true);
            json.setData(newImgUrl.substring(1));
            json.setMessage("执行成功");
        }

        return json;
    }

    /**
     * 普通js上传文件
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
            json.setMessage("获取PID失败");
        } else {
            System.out.println("pid:" + pid);
            try {
                if (!file.isEmpty()) {
                    // 获取配置文件信息
                    if (ftpConfig == null) {
                        ftpConfig = GetConfigureInfo.getFtpConfig();
                    }
                    // 检查配置文件信息是否正常读取
                    checkFtpConfig(ftpConfig, json);
                    if (!json.isOk()) {
                        return json;
                    }
                    String localDiskPath = ftpConfig.getLocalDiskPath();
                    Random random = new Random();
                    String originalName = file.getOriginalFilename();
                    // 文件的后缀取出来
                    String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                    String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                    // 本地服务器磁盘全路径
                    String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;
                    // 文件流输出到本地服务器指定路径
                    ImgDownload.writeImageToDisk(file.getBytes(), localDiskPath + localFilePath);

                    // 判断图片的分辨率是否小于400*200
                    boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
                    if (checked) {
                        // 压缩图片400x400
                        String localFilePath400x400 = "importimg/" + pid + "/" + saveFilename + ".400x400" + fileSuffix;
                        boolean is400 = ImageCompression.reduceImgByWidth(400, localDiskPath + localFilePath,
                                localDiskPath + localFilePath400x400);
                        // 压缩图片60x60
                        String localFilePath60x60 = "importimg/" + pid + "/" + saveFilename + ".60x60" + fileSuffix;
                        boolean is60 = ImageCompression.reduceImgByWidth(60, localDiskPath + localFilePath,
                                localDiskPath + localFilePath60x60);
                        if (is60 && is400) {
                            json.setData(ftpConfig.getLocalShowPath() + localFilePath60x60);
                            json.setOk(true);
                            json.setMessage("上传本地图片成功");
                        } else {
                            // 判断分辨率不通过删除图片
                            File file400 = new File(localFilePath400x400);
                            if (file400.exists()) {
                                file400.delete();
                            }
                            File file60 = new File(localFilePath60x60);
                            if (file60.exists()) {
                                file60.delete();
                            }
                            // 压缩失败整体终止执行
                            json.setOk(false);
                            json.setMessage("压缩图片60x60和400x400失败，终止执行");
                        }
                    } else {
                        // 图片分辨率小于400*200整体终止执行
                        json.setOk(false);
                        json.setMessage("图片分辨率小于400*200，终止执行");
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("获取文件失败，请重试");
                }
            } catch (Exception e) {
                e.printStackTrace();
                json.setOk(false);
                json.setMessage("上传错误:" + e.getMessage());
                LOG.error("上传错误：" + e.getMessage());
            }
        }
        return json;
    }

    /**
     * 编辑产品评论内容
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateReviewRemark", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult updateReviewRemark(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        json.setOk(true);
        try{
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            com.cbt.pojo.Admuser adm =(com.cbt.pojo.Admuser)SerializeUtil.JsonToObj(admuserJson, com.cbt.pojo.Admuser.class);
            if(adm==null){
                json.setOk(false);
            }
            Map<String,String> paramMap=new HashMap<String,String>();
            String update_aliId=request.getParameter("update_aliId");
            String edit_remark=request.getParameter("edit_remark");
            String editcountry=request.getParameter("editcountry");
            String edit_score=request.getParameter("edit_score");
            String update_flag=request.getParameter("update_flag");
            paramMap.put("update_aliId",update_aliId);
            paramMap.put("edit_remark",edit_remark);
            paramMap.put("editcountry",editcountry);
            paramMap.put("edit_score",edit_score);
            paramMap.put("update_flag",update_flag);
            paramMap.put("review_name",adm.getAdmName());
            int index=customGoodsService.updateReviewRemark(paramMap);
            if(index>0){
                //插入数据到线上
                SendMQ sendMQ=new SendMQ();
                String sql="update goods_review set review_remark='"+edit_remark+"',country='"+editcountry+"',review_score='"+edit_score+"',review_flag='"+update_flag+"',updatetime=now() where id='"+update_aliId+"'";
                sendMQ.sendMsg(new RunSqlModel(sql));
                sendMQ.closeConn();
            }
            json.setOk(index>0?true:false);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 添加产品评论内容
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addReviewRemark", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult addReviewRemark(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        json.setOk(true);
        try{
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            com.cbt.pojo.Admuser adm =(com.cbt.pojo.Admuser)SerializeUtil.JsonToObj(admuserJson, com.cbt.pojo.Admuser.class);
            if(adm==null){
                json.setOk(false);
            }
            Map<String,String> paramMap=new HashMap<String,String>();
            String goods_pid=request.getParameter("goods_pid");
            String review_remark=request.getParameter("review_remark");
            String review_score=request.getParameter("review_score");
            String country=request.getParameter("country");
            paramMap.put("goods_pid",goods_pid);
            paramMap.put("review_remark",review_remark);
            paramMap.put("review_score",review_score);
            paramMap.put("country",country);
            paramMap.put("review_name",adm.getAdmName());
            int index=customGoodsService.addReviewRemark(paramMap);
            if(index>0){
                //插入数据到线上
                SendMQ sendMQ=new SendMQ();
                String sql=" insert into goods_review(goods_pid,country,review_name,createtime,review_remark,review_score) values('"+goods_pid+"','"+country+"','"+adm.getAdmName()+"',now(),'"+review_remark+"','"+review_score+"')";
                sendMQ.sendMsg(new RunSqlModel(sql));
                sendMQ.closeConn();
            }
            json.setOk(index>0?true:false);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 使用速卖通详情
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
            json.setMessage("获取PID失败");
            return json;
        }
        String aliGoodsInfo = request.getParameter("aliGoodsInfo");
        if (aliGoodsInfo == null || "".equals(aliGoodsInfo)) {
            json.setOk(false);
            json.setMessage("获取速卖通详情失败");
            return json;
        }

        Document nwDoc = null;
        try {
            // string转html文档类型
            nwDoc = Jsoup.parseBodyFragment(aliGoodsInfo);
            // 去掉html里面a标签数据
            nwDoc.getElementsByTag("a").remove();
            // 获取img标签
            Elements imgEls = nwDoc.getElementsByTag("img");
            Random random = new Random();
            // 使用线程池
            // 获取配置文件信息
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            // 检查配置文件信息是否正常读取
            checkFtpConfig(ftpConfig, json);
            if (json.isOk()) {
                String localDiskPath = ftpConfig.getLocalDiskPath();
                for (Element imgEl : imgEls) {
                    System.out.println("src:" + imgEl.attr("src"));

                    String imgUrl = imgEl.attr("src");
                    // 得到文件保存的名称
                    if (imgUrl.indexOf("?") > -1) {
                        imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                    }
                    // 兼容没有http头部的src
                    if (imgUrl.indexOf("//") == 0) {
                        imgUrl = "http:" + imgUrl;
                    }
                    // 文件的后缀取出来
                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                    // 生成唯一文件名称
                    String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
                    // 本地服务器磁盘全路径
                    String localFilePath = "importimg/" + pid + "/" + saveFilename + fileSuffix;

                    // 下载网络图片到本地
                    boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
                    if (is) {
                        // 判断图片的分辨率是否大于700*400，如果大于则进行图片压缩
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
                                json.setMessage("压缩图片到700*700失败，终止执行");
                                break;
                            }
                        } else {
                            json.setOk(true);
                            json.setMessage("图片上传本地成功");
                            imgEl.attr("src", ftpConfig.getLocalShowPath() + localFilePath);
                        }
                    } else {
                        json.setOk(false);
                        json.setMessage("下载图片失败，请重试！");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行错误：" + e.getMessage());
            LOG.error("执行错误：" + e.getMessage());
        }

        if (nwDoc == null) {
            json.setOk(false);
            json.setMessage("解析html数据失败，请重试");
        } else if (json.isOk()) {
            json.setData(nwDoc.toString());
            nwDoc = null;
        }
        return json;
    }

    /**
     * @return JsonResult
     * @Title addSimilarGoods
     * @Description 插入相似商品
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
            json.setMessage("请登录后操作");
            return json;
        }

        String mainPid = request.getParameter("mainPid");
        if (mainPid == null || "".equals(mainPid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }
        String similarPids = request.getParameter("similarPids");
        if (similarPids == null || "".equals(similarPids)) {
            json.setOk(false);
            json.setMessage("获取相似商品pid失败");
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
                    json.setMessage("插入成功,部分商品已经存在:" + showExistPids);
                } else {
                    json.setMessage("插入成功");
                }
            } else {
                json.setOk(false);
                if (showExistPids.size() > 0) {
                    json.setMessage("批量插入错误,部分商品已经存在:" + showExistPids);
                } else {
                    json.setMessage("批量插入错误，请重试");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("mainPid:" + mainPid + " addSimilarGoods 执行错误：" + e.getMessage());
            LOG.error("mainPid:" + mainPid + " addSimilarGoods 执行错误：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        }

        String mainPid = request.getParameter("mainPid");
        if (mainPid == null || "".equals(mainPid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        try {
            List<SimilarGoods> goodsList = customGoodsService.querySimilarGoodsByMainPid(mainPid);
            json.setOk(true);
            json.setData(goodsList);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("mainPid:" + mainPid + " querySimilarGoodsByMainPid 执行错误：" + e.getMessage());
            LOG.error("mainPid:" + mainPid + " querySimilarGoodsByMainPid 执行错误：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        try {
            boolean isDelete = customGoodsService.deleteWordSizeInfoByPid(pid);
            if (isDelete) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("删除商品文字尺码表失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " deleteWordSizeInfoByPid 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " deleteWordSizeInfoByPid 执行错误：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        } else {
            editBean.setAdmin_id(user.getId());
        }

        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        } else {
            editBean.setPid(pid);
        }

        String weight_flag_str = request.getParameter("weight_flag");
        int weight_flag = 0;
        if (StringUtils.isNotBlank(weight_flag_str)) {
            weight_flag = Integer.valueOf(weight_flag_str);
        }
        editBean.setWeight_flag(weight_flag);

        String ugly_flag_str = request.getParameter("ugly_flag");
        int ugly_flag = 0;
        if (StringUtils.isNotBlank(ugly_flag_str)) {
            ugly_flag = Integer.valueOf(ugly_flag_str);
        }
        editBean.setUgly_flag(ugly_flag);

        String benchmarking_flag_str = request.getParameter("benchmarking_flag");
        int benchmarking_flag = 0;
        if (StringUtils.isNotBlank(benchmarking_flag_str)) {
            benchmarking_flag = Integer.valueOf(benchmarking_flag_str);
        }
        editBean.setBenchmarking_flag(benchmarking_flag);

        String describe_good_flag_str = request.getParameter("describe_good_flag");
        int describe_good_flag = 0;
        if (StringUtils.isNotBlank(describe_good_flag_str)) {
            describe_good_flag = Integer.valueOf(describe_good_flag_str);
        }
        editBean.setDescribe_good_flag(describe_good_flag);

        String never_off_flag_str = request.getParameter("never_off_flag");
        int never_off_flag = 0;
        if (StringUtils.isNotBlank(never_off_flag_str)) {
            never_off_flag = Integer.valueOf(never_off_flag_str);
        }
        editBean.setNever_off_flag(never_off_flag);

        String uniqueness_flag_str = request.getParameter("uniqueness_flag");
        int uniqueness_flag = 0;
        if (StringUtils.isNotBlank(uniqueness_flag_str)) {
            uniqueness_flag = Integer.valueOf(uniqueness_flag_str);
        }
        editBean.setUniqueness_flag(uniqueness_flag);

        String promotion_flag_str = request.getParameter("promotion_flag");
        int promotion_flag = 0;
        if (StringUtils.isNotBlank(promotion_flag_str)) {
            promotion_flag = Integer.valueOf(promotion_flag_str);
        }
        editBean.setPromotion_flag(promotion_flag);

        if (weight_flag > 0 || ugly_flag > 0 || benchmarking_flag > 0 || describe_good_flag > 0 || never_off_flag > 0 || uniqueness_flag > 0 || promotion_flag > 0) {
            System.err.println("pid:" + pid + ",获取标识信息成功");
        } else {
            json.setOk(false);
            json.setMessage("获取标识信息失败");
            return json;
        }
        try {
            //boolean is = customGoodsService.setGoodsFlagByPid(editBean);
            customGoodsService.updatePidIsEdited(editBean);
            customGoodsService.insertIntoGoodsEditBean(editBean);
            if(promotion_flag > 0){
                customGoodsService.updatePromotionFlag(pid);
            }
            json.setOk(true);
            json.setMessage("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setGoodsFlagByPid 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " setGoodsFlagByPid 执行错误：" + e.getMessage());
        }
        return json;
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
            json.setMessage("请登录后操作");
            return json;
        } else {
            editBean.setAdmin_id(user.getId());
        }

        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
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
                json.setMessage("执行成功");
            } else {
                json.setOk(true);
                json.setMessage("执行错误，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setGoodsRepairedByPid 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " setGoodsRepairedByPid 执行错误：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        }*/

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }
        String finalWeight = request.getParameter("finalWeight");
        if (StringUtils.isBlank(finalWeight)) {
            json.setOk(false);
            json.setMessage("获取商品重量失败失败");
            return json;
        }

        try {
            boolean is = customGoodsService.setNoBenchmarking(pid, Double.valueOf(finalWeight));
            if (is) {
                json.setOk(true);
                json.setMessage("执行成功");
            } else {
                json.setOk(true);
                json.setMessage("执行错误，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setNoBenchmarking 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " setNoBenchmarking 执行错误：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        }*/

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        try {
            boolean is = customGoodsService.setNeverOff(pid);
            if (is) {
                json.setOk(true);
                json.setMessage("执行成功");
            } else {
                json.setOk(false);
                json.setMessage("执行错误，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " setNoBenchmarking 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " setNoBenchmarking 执行错误：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }
        String benchmarkingPid = request.getParameter("benchmarkingPid");
        if (StringUtils.isBlank(benchmarkingPid)) {
            json.setOk(false);
            json.setMessage("获取对标商品ID失败");
            return json;
        }
        String benchmarkingPrice = request.getParameter("benchmarkingPrice");
        if (StringUtils.isBlank(benchmarkingPrice)) {
            json.setOk(false);
            json.setMessage("获取对标商品价格失败");
            return json;
        }

        try {
            boolean is = customGoodsService.saveBenchmarking(pid, benchmarkingPid, benchmarkingPrice) > 0;
            if (is) {
                json.setOk(true);
                json.setMessage("执行成功");
            } else {
                json.setOk(false);
                json.setMessage("执行错误，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " saveBenchmarking 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " saveBenchmarking 执行错误：" + e.getMessage());
        }
        return json;
    }


    /**
     * @param filename 文件的原始名称
     * @return uuid+"_"+文件的原始名称
     * @Method: makeFileName
     * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
     */
    private String makeFileName(String filename) { // 2.jpg
        // 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        return UUID.randomUUID().toString() + "_" + filename;
    }

    private void checkFtpConfig(FtpConfig ftpConfig, JsonResult json) {
        json.setOk(true);
        // 判断获取的配置信息是否有效
        if (ftpConfig == null || !ftpConfig.isOk()) {
            json.setOk(false);
            json.setMessage("获取配置文件失败");
        } else {
            if (StringUtil.isBlank(ftpConfig.getFtpURL())) {
                json.setOk(false);
                json.setMessage("获取ftpURL失败");
            } else if (StringUtil.isBlank(ftpConfig.getFtpPort())) {
                json.setOk(false);
                json.setMessage("获取ftpPort失败");
            } else if (StringUtil.isBlank(ftpConfig.getFtpUserName())) {
                json.setOk(false);
                json.setMessage("获取ftpUserName失败");
            } else if (StringUtil.isBlank(ftpConfig.getFtpPassword())) {
                json.setOk(false);
                json.setMessage("获取ftpPassword失败");
            } else if (StringUtil.isBlank(ftpConfig.getRemoteShowPath())) {
                json.setOk(false);
                json.setMessage("获取remoteShowPath失败");
            } else if (StringUtil.isBlank(ftpConfig.getLocalDiskPath())) {
                json.setOk(false);
                json.setMessage("获取localDiskPath失败");
            } else if (StringUtil.isBlank(ftpConfig.getLocalShowPath())) {
                json.setOk(false);
                json.setMessage("获取localShowPath失败");
            }
        }
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
        // 获取需要编辑的内容
        String pid = request.getParameter("pid");
        if (pid == null || pid.isEmpty()) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取PID失败");
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
            mv.addObject("message", "查询错误，原因：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }
        String newWeight = request.getParameter("newWeight");
        if (StringUtils.isBlank(newWeight)) {
            json.setOk(false);
            json.setMessage("获取商品重量失败");
            return json;
        }
        String weight = request.getParameter("weight");
        if (StringUtils.isBlank(weight)) {
            json.setOk(false);
            json.setMessage("获取商品原始重量失败");
            return json;
        }

        try {
            boolean is = customGoodsService.updateGoodsWeightByPid(pid, Double.valueOf(newWeight), Double.valueOf(weight), 1) > 0;
            /*if (is) {
                // 重新刷新价格数据
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
                    json.setMessage("修改重量后，价格清洗失败：" + jsonJt.getString("message"));
                } else {
                    json.setOk(true);
                    json.setMessage("执行成功");
                }
            } else {
                json.setOk(false);
                json.setMessage("执行错误，请重试");
            }*/
            // 修改重量非直接显示价格数据更新
            customGoodsService.setGoodsWeightByWeigherNew(pid, newWeight);
            json.setOk(true);
            json.setMessage("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " updateGoodsWeight 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " updateGoodsWeight 执行错误：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/setGoodsWeightByWeigher")
    @ResponseBody
    public JsonResult setGoodsWeightByWeigher(String pid, String newWeight) {
        JsonResult json = new JsonResult();
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }
        if (StringUtils.isBlank(newWeight)) {
            json.setOk(false);
            json.setMessage("获取商品重量失败");
            return json;
        }
        return customGoodsService.setGoodsWeightByWeigherNew(pid, newWeight);
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
            json.setMessage("请登录后操作");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        String editProfit = request.getParameter("editProfit");
        if (StringUtils.isBlank(editProfit)) {
            json.setOk(false);
            json.setMessage("获取利润率失败");
            return json;
        }

        String typeStr = request.getParameter("type");
        if (StringUtils.isBlank(typeStr)) {
            json.setOk(false);
            json.setMessage("获取标识失败");
            return json;
        }

        try {
            boolean is = customGoodsService.editAndLockProfit(pid, Integer.valueOf(typeStr), Double.valueOf(editProfit)) > 0;
            if (is) {
                json.setOk(true);
                json.setMessage("执行成功");
            } else {
                json.setOk(false);
                json.setMessage("执行错误，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("pid:" + pid + " editAndLockProfit 执行错误：" + e.getMessage());
            LOG.error("pid:" + pid + " editAndLockProfit 执行错误：" + e.getMessage());
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
            json.setMessage("请登录后操作");
            return json;
        }
        GoodsEditBean editBean = new GoodsEditBean();

        // 获取需要编辑的内容
        String pid = request.getParameter("pid");
        if (StringUtils.isNotBlank(pid)) {
            editBean.setPid(pid);
        }

        String adminIdStr = request.getParameter("adminId");
        if (StringUtils.isNotBlank(adminIdStr)) {
            editBean.setAdmin_id(Integer.valueOf(adminIdStr));
        }
        String weightFlagStr = request.getParameter("weightFlag");
        if (StringUtils.isNotBlank(adminIdStr)) {
            editBean.setWeight_flag(Integer.valueOf(weightFlagStr));
        }
        String uglyFlagStr = request.getParameter("uglyFlag");
        if (StringUtils.isNotBlank(uglyFlagStr)) {
            editBean.setUgly_flag(Integer.valueOf(uglyFlagStr));
        }
        String repairedFlagStr = request.getParameter("repairedFlag");
        if (StringUtils.isNotBlank(repairedFlagStr)) {
            editBean.setRepaired_flag(Integer.valueOf(repairedFlagStr));
        }
        String benchmarkingFlagStr = request.getParameter("benchmarkingFlag");
        if (StringUtils.isNotBlank(benchmarkingFlagStr)) {
            editBean.setBenchmarking_flag(Integer.valueOf(benchmarkingFlagStr));
        }

        int startNum = 0;
        int limitNum = 30;
        String limitNumStr = request.getParameter("rows");
        if (StringUtils.isNotBlank(limitNumStr)) {
            limitNum = Integer.valueOf(limitNumStr);
        }

        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        editBean.setStartNum(startNum);
        editBean.setLimitNum(limitNum);

        try {
            List<GoodsEditBean> editList = customGoodsService.queryGoodsEditBean(editBean);
            Map<String, List<String>> pidMapNews = new HashMap<>(limitNum + 1);
            Map<String, List<String>> pidMapOlds = new HashMap<>(limitNum + 1);
            for (GoodsEditBean gdEd : editList) {
                if(StringUtils.isNotBlank(gdEd.getOld_title()) && StringUtils.isNotBlank(gdEd.getNew_title())){
                    if(gdEd.getNew_title().equals(gdEd.getOld_title())){
                        gdEd.setNew_title("");
                    }
                }
                if (pidMapOlds.containsKey(gdEd.getPid())) {
                    if (StringUtils.isNotBlank(gdEd.getOld_title())) {
                        if (checkListContains(pidMapOlds.get(gdEd.getPid()),gdEd.getOld_title())) {
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
                        if (checkListContains(pidMapNews.get(gdEd.getPid()),gdEd.getNew_title())) {
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
            json.setMessage("查询错误，原因：" + e.getMessage());
        }
        return json;
    }

    private boolean checkListContains(List<String> list,String str){
        boolean isOk = false;
        if(list == null || list.isEmpty() || StringUtils.isBlank(str)){
            return isOk;
        }else{
            for(String tempStr : list){
                if(str.equals(tempStr)){
                    isOk = true;
                    break;
                }
            }
        }
        return isOk;
    }

}
