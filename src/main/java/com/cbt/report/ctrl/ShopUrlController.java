
package com.cbt.report.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.*;
import com.cbt.bean.TypeBean;
import com.cbt.customer.service.IShopUrlService;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.ImgDownload;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.thread.ShopGoodsDealThread;
import com.cbt.website.thread.ShopGoodsSyncThread;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.MD5Util;
import com.importExpress.pojo.ShopBrandAuthorization;
import com.importExpress.pojo.ShopGoodsSalesAmount;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.SearchFileUtils;
import com.importExpress.utli.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/ShopUrlC")
public class ShopUrlController {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ShopUrlController.class);
    private String rootPath = "F:/console/tomcatImportCsv/webapps/";
    private String localIP = "http://27.115.38.42:8083/";
    private String wanlIP = "http://192.168.1.27:8083/";
    private String chineseChar = "([\\一-\\龥]+)";
    private List<Category1688Bean> category1688List = new ArrayList<Category1688Bean>();
    private static final String SHOP_GOODS_WEIGHT_CLEARURL = "http://192.168.1.31:8080/checkimage/clear/shopGoodsWeight?";
    // private static final String SHOP_GOODS_SHOW_URL = "http://117.144.21.74:8000";
    private static final String SHOP_GOODS_SHOW_URL_K = "http://192.168.1.28:8499";
    private static final String SHOP_GOODS_LOCAL_PATH_K = "K:/shopimages";
    private static final String SHOP_GOODS_SHOW_URL_J = "http://192.168.1.28:8399";
    private static final String SHOP_GOODS_LOCAL_PATH_J = "J:/shopimages";//J:/shopimages
    private static final String REMOTE_WEB_SITE = "http://112.64.174.34";// 外网
    private static final String LOCAL_WEB_SITE = "http://192.168.1.28";// 内网
    private FtpConfig ftpConfig;
    private static List<ConfirmUserInfo> allAdminList = UserInfoUtils.queryAllAdminList();
    private static List<String> allShopBlackList = new ArrayList<>();

    @Autowired
    private IShopUrlService shopUrlService;

    @Autowired
    private CustomGoodsService customGoodsService;

    /**
     * 方法描述:查询所有的店铺
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/findAll")
    @ResponseBody
    protected EasyUiJsonResult findAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();
        // 获取登录用户
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        // String parse = null;
        String timeFrom = null;
        String timeTo = null;
        String shopUserName = request.getParameter("shopUserName");
        /*if ((shopUserName == null || "".equals(shopUserName)) && adm != null) {
            if (adm.getAdmName().equalsIgnoreCase("Ling") || adm.getAdmName().equalsIgnoreCase("camry")
                    || adm.getAdmName().equalsIgnoreCase("testAdm")) {
                shopUserName = "";
            } else {
                shopUserName = adm.getAdmName();
            }

        }*/
        if (StringUtils.isBlank(shopUserName)) {
            shopUserName = "";
        }
        String str = request.getParameter("page");
        String rowStr = request.getParameter("rows");
        String date = request.getParameter("createTime");
        // String type = request.getParameter("questionType");
        String shopId = request.getParameter("shopId");
        String shopBrand = request.getParameter("shopBrand");
        String time1 = request.getParameter("timeFrom");
        String time2 = request.getParameter("timeTo");
        String isOnStr = request.getParameter("isOn");
        String isAutoStr = request.getParameter("isAuto");
        String readyDelStr = request.getParameter("readyDel");
        String translateDescriptionStr = request.getParameter("translateDescription");
        String isShopFlagStr = request.getParameter("isShopFlag");
        String stateStr = request.getParameter("state");
        String shopTypeStr = request.getParameter("shopType");
        String authorizedFlagStr = request.getParameter("authorizedFlag");
        String authorizedFileFlagStr = request.getParameter("authorizedFileFlag");
        String ennameBrandFlagStr = request.getParameter("ennameBrandFlag"); //-1-无筛选;1-店铺英文为空;2-品牌属性为空;3-店铺英文+品牌属性为空;
        String admName = request.getParameter("admName");
        String days = request.getParameter("days");
        String catid = request.getParameter("catid");
        int authorizedFlag = -1;
        if (StringUtils.isNotBlank(authorizedFlagStr)) {
            authorizedFlag = Integer.valueOf(authorizedFlagStr);
        }
        int authorizedFileFlag = -1;
        if (StringUtils.isNotBlank(authorizedFileFlagStr)) {
            authorizedFileFlag = Integer.valueOf(authorizedFileFlagStr);
        }
        int ennameBrandFlag = -1;
        if (StringUtils.isNotBlank(ennameBrandFlagStr)) {
            ennameBrandFlag = Integer.valueOf(ennameBrandFlagStr);
        }
        int shopType = -1;
        if (StringUtils.isNotBlank(shopTypeStr)) {
            shopType = Integer.valueOf(shopTypeStr);
        }
        int isOn = -1;
        if (!(isOnStr == null || "".equals(isOnStr))) {
            isOn = Integer.valueOf(isOnStr);
        }
        int isAuto = -1;
        if (StringUtil.isNotBlank(isAutoStr)) {
            isAuto = Integer.valueOf(isAutoStr);
        }
        int readyDel = -1;
        if (!StringUtils.isBlank(readyDelStr)) {
            readyDel = Integer.valueOf(readyDelStr);
        }
        int translateDescription = -1;
        if (!StringUtils.isBlank(translateDescriptionStr)) {
            translateDescription = Integer.valueOf(translateDescriptionStr);
        }
        int isShopFlag = -1;
        if (!StringUtils.isBlank(isShopFlagStr)) {
            isShopFlag = Integer.valueOf(isShopFlagStr);
        }

        if (time1 != null && time1 != "") {
            timeFrom = time1;
        }
        if (time2 != null && time2 != "") {
            timeTo = time2;
        }
        int state = -1;
        if (!(stateStr == null || "".equals(stateStr))) {
            state = Integer.valueOf(stateStr);
        }
        try {
            if (date != null && !"".equals(date)) {
                // parse = date;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        int page = 1;
        if (str == null) {
            str = "1";
        } else {
            page = Integer.parseInt(str);
        }
        int rows = 25;
        if (StringUtils.isNotBlank(rowStr)) {
            rows = Integer.valueOf(rowStr);
        }
        int start = (page - 1) * rows;
        String shopids = "";
        if (StringUtil.isNotBlank(admName)) {
            shopids = shopUrlService.getShopList(admName, days);
        }
        List<ShopUrl> findAll = shopUrlService.findAll(shopId, shopBrand, shopUserName, date, start, rows, timeFrom,
                timeTo, isOn, state, isAuto, readyDel, shopType, authorizedFlag, authorizedFileFlag, ennameBrandFlag, shopids,
                translateDescription, isShopFlag, catid);
        List<ShopGoodsSalesAmount> shopGoodsSalesAmountList = customGoodsService.queryShopGoodsSalesAmountAll();

        if(allShopBlackList == null || allShopBlackList.size() == 0){
            allShopBlackList = customGoodsService.queryAllShopBlackList();
        }
        for (ShopUrl shopUrlBean : findAll) {
            if(allShopBlackList.contains(shopUrlBean.getShopId())){
                shopUrlBean.setIsBlack(1);
                if(StringUtils.isBlank(shopUrlBean.getInputShopDescription())){
                    shopUrlBean.setInputShopDescription("<b style='color:red'>黑名单店铺</b>");
                }else{
                    shopUrlBean.setInputShopDescription(shopUrlBean.getInputShopDescription() + "<b style='color:red'>黑名单店铺</b>");
                }
            }
            genShopPrice(shopUrlBean, shopGoodsSalesAmountList);
            List<ShopBrandAuthorization> authorizationList =  shopUrlService.queryBrandAuthorizationByShopId(shopUrlBean.getShopId());
            if(authorizationList == null || authorizationList.size() == 0){
                shopUrlBean.setAuthorizedFlag(3);
                shopUrlBean.setBrandNames("");
            }else{
                int count = 0;
                StringBuffer brandNameS = new StringBuffer();
                for (ShopBrandAuthorization authorization : authorizationList) {
                    brandNameS.append("," + authorization.getBrandName());
                    if(authorization.getAuthorizeState() == 3){
                        count ++;
                    }
                }
                shopUrlBean.setBrandNames(brandNameS.toString().substring(1));
                shopUrlBean.setAuthorizationList(authorizationList);
                if(count == authorizationList.size()){
                    // 3 未授权
                    shopUrlBean.setAuthorizedFlag(3);
                }else if(count < authorizationList.size() && count >0){
                    // 2 部分授权
                    shopUrlBean.setAuthorizedFlag(2);
                }else if(count == 0){
                    // 1已全部授权
                    shopUrlBean.setAuthorizedFlag(1);
                }
            }
        }
        shopGoodsSalesAmountList.clear();
        int total = shopUrlService.total(shopId, shopBrand, shopUserName, date, timeFrom, timeTo, isOn, state, isAuto, readyDel, shopType, authorizedFlag,
                authorizedFileFlag, ennameBrandFlag, shopids, translateDescription, isShopFlag, catid);
        json.setRows(findAll);
        json.setTotal(total);
        return json;
    }

    private void genShopPrice(ShopUrl shopUrlBean, List<ShopGoodsSalesAmount> shopGoodsSalesAmountList) {

        if (shopUrlBean.getAdminId() > 0) {
            for (ConfirmUserInfo adm : allAdminList) {
                if (shopUrlBean.getAdminId() == adm.getId() && StringUtils.isBlank(shopUrlBean.getAdmUser())) {
                    shopUrlBean.setAdmUser(adm.getConfirmusername());
                    shopUrlService.reUpdateShopAdminId(shopUrlBean.getShopId(),adm.getId(),adm.getConfirmusername());
                    break;
                }
            }
        }
        for (ShopGoodsSalesAmount salesAmount : shopGoodsSalesAmountList) {
            if (salesAmount.getShopId().equals(shopUrlBean.getShopId())) {
                shopUrlBean.setShopPrice(salesAmount.getTotalPrice());
                break;
            }
        }
    }

    /**
     * 方法描述:查询授权
     * http://192.168.1.57:8086/cbtconsole/ShopUrlC/queryAuthorizedFileFlag
     */
    @RequestMapping(value = "/queryAuthorizedFileFlag")
    @ResponseBody
    protected Map<String, Integer> queryAuthorizedFileFlag() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        try {
            //1-已授权但无授权文件
            int authorizedFileFlag1 = shopUrlService.total(null, null, null, null, null, null, -1, -1, -1, -1, -1, -1, 1, -1, null, -1, -1, null);
            result.put("authorizedFileFlag1", authorizedFileFlag1);
            //2-授权文件到期
            int authorizedFileFlag2 = shopUrlService.total(null, null, null, null, null, null, -1, -1, -1, -1, -1, -1, 2, -1, null, -1, -1, null);
            result.put("authorizedFileFlag2", authorizedFileFlag2);
            //3-已授权但无授权文件+授权文件到期
            int authorizedFileFlag3 = shopUrlService.total(null, null, null, null, null, null, -1, -1, -1, -1, -1, -1, 3, -1, null, -1, -1, null);
            result.put("authorizedFileFlag3", authorizedFileFlag3);
            result.put("state", 1);
        } catch (Exception e) {
            System.out.println(e);
            result.put("state", 0);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/selectOneShop.do")
    public JsonResult getOneProduct(HttpServletRequest request, HttpServletResponse response) {

        ShopUrl shopUrl = null;
        JsonResult jr = new JsonResult();
        try {

            String sid = request.getParameter("id");
            int id = Integer.parseInt(sid);
            shopUrl = shopUrlService.findById(id);

            if (shopUrl != null) {
                jr.setData(shopUrl);
                jr.setOk(true);
                return jr;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        jr.setOk(false);
        jr.setData(null);
        return jr;
    }

    @ResponseBody
    @RequestMapping("/insertOrUpdate.do")
    public JsonResult insertOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        JsonResult jr = new JsonResult();

        String sid = request.getParameter("id");
        String shopUrl = request.getParameter("shopUrl");
        String shopId = request.getParameter("shopId");
        String salesVolume = request.getParameter("salesVolume");
        String downloadNum = request.getParameter("downloadNum");
        String isValid = request.getParameter("isValid");
        String isTrade = request.getParameter("isTrade");
        String inputShopName = request.getParameter("inputShopName");
        String inputShopDescription = request.getParameter("inputShopDescription");
        String inputShopEnName = request.getParameter("inputShopEnName");
        String inputShopBrand = request.getParameter("inputShopBrand");
        String urlType = request.getParameter("urlType");
        String saveTypeStr = request.getParameter("saveType");
        String memberId = request.getParameter("memberId");
        String[] typeShopUrls = request.getParameter("typeShopUrls").split(",");
        // 获取登录用户
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        ShopUrl su = new ShopUrl();
        if (StringUtils.isNotBlank(sid)) {
            su.setId(Integer.parseInt(sid));
            ShopUrl temp = shopUrlService.findById(Integer.parseInt(sid));
            if (temp.getOnlineStatus() == 0) {
                su.setFlag(0);
            } else {
                su.setFlag(1);
            }
        }
        if (StringUtils.isNotBlank(salesVolume)) {
            su.setSalesVolume(Integer.parseInt(salesVolume));
        } else {
            su.setSalesVolume(1);
        }

        if (StringUtils.isNotBlank(downloadNum)) {
            su.setDownloadNum(Integer.parseInt(downloadNum));
        } else {
            su.setDownloadNum(1000);
        }

        int preCount = 8;
        if(shopUrl.contains("http://")){
            preCount = "http://".length();
        } else if(shopUrl.contains("https://")){
            preCount = "https://".length();
        }
        if (StringUtils.isBlank(shopId) && urlType.equals("0")) {
            shopId = shopUrl.substring(preCount, shopUrl.indexOf(".1688.com"));
            shopUrl = shopUrl.substring(0, shopUrl.indexOf(".1688.com") + ".1688.com".length());
        } else if (StringUtils.isBlank(shopId) && urlType.equals("1")) {
            shopId = typeShopUrls[0].substring(preCount, typeShopUrls[0].indexOf(".1688.com"));
        }

        boolean isBlack = shopUrlService.checkIsBlackShopByShopId(shopId);
        if (isBlack) {
            jr.setOk(false);
            jr.setMessage("黑名单店铺，不能执行操作");
            return jr;
        }


        if (StringUtils.isBlank(sid)) {
            boolean isExists = shopUrlService.checkExistsShopByShopId(shopId) > 0;
            if (isExists) {
                jr.setOk(false);
                jr.setMessage("店铺id已经存在");
                jr.setTotal(1L);
                jr.setData(shopId);
                return jr;
            }
        }

        int isChange = 0;
        if (StringUtils.isNotBlank(saveTypeStr)) {
            isChange = Integer.valueOf(saveTypeStr);
        }
        su.setAdminId(adm.getId());
        su.setAdmUser(adm.getAdmName());
        su.setIsValid(Integer.parseInt(isValid));
        su.setUrlType(Integer.parseInt(urlType));
        su.setShopUrl(shopUrl);
        su.setShopId(shopId);
        su.setInputShopName(inputShopName);
        su.setInputShopDescription(inputShopDescription);
        su.setInputShopEnName(inputShopEnName);
        su.setInputShopBrand(inputShopBrand);
        su.setCreateTime(new Date());
        su.setUpdatetime(new Date());
        su.setIsTrade(Integer.valueOf(isTrade));
        su.setMemberId(memberId);

        int result = shopUrlService.insertOrUpdate(su, typeShopUrls, isChange);
        if (result == 1) {
            jr.setOk(true);
        } else {
            jr.setOk(false);
            jr.setMessage("店铺id重复");
        }
        return jr;
    }

    @ResponseBody
    @RequestMapping("/deleShopUrl.do")
    public JsonResult deleShopUrl(HttpServletRequest request, HttpServletResponse response) {
        JsonResult jr = new JsonResult();
        String sid = request.getParameter("id");
        if (sid == null && "".equals(sid)) {
            jr.setOk(false);
            return jr;
        }

        int result = shopUrlService.delById(Integer.parseInt(sid));
        if (result == 1) {
            jr.setOk(true);
        } else {
            jr.setOk(false);
        }
        return jr;
    }

    @RequestMapping("/updateExcel.do")
    @ResponseBody
    public JsonResult updateExcel(HttpServletRequest request, HttpServletResponse rs) {

        JsonResult jr = new JsonResult();
        try {

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("files");
            if (file == null || file.isEmpty()) {
                jr.setOk(false);
                return jr;
            }
            InputStream is = new ByteArrayInputStream(file.getBytes());
            Map<String, String> map = readExcel2Map(is);
            if (map == null) {
                jr.setOk(false);
                return jr;
            }
            int line = 0;
            for (Entry<String, String> entry : map.entrySet()) {
                ShopUrl su = new ShopUrl();
                su.setShopId(entry.getKey());
                su.setShopUrl(entry.getValue());
                int result = shopUrlService.insertOrUpdate(su, null, 0);
                if (result == 1) {
                    line++;
                }
            }
            jr.setData(line);
            jr.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setOk(false);
        }

        return jr;
    }

    @SuppressWarnings("resource")
    private Map<String, String> readExcel2Map(InputStream is) {
        Map<String, String> map = null;
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            int rowstart = xssfSheet.getFirstRowNum();
            int rowEnd = xssfSheet.getLastRowNum();
            map = new HashMap<String, String>();
            for (int i = rowstart; i <= rowEnd; i++) {
                XSSFRow row = xssfSheet.getRow(i);
                if (null == row)
                    continue;
                XSSFCell cell = row.getCell(0);
                String oldUrl = cell.getStringCellValue();
                if (oldUrl != null && oldUrl != "") {
                    String temp = oldUrl.substring(8, oldUrl.indexOf(".1688.com/"));
                    String temp1 = oldUrl.substring(0, oldUrl.indexOf(".1688.com/") + 10);
                    map.put(temp, temp1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 方法描述:查询同店铺上线商品
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/findAllGoods")
    @ResponseBody
    protected EasyUiJsonResult findAllGoods(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        EasyUiJsonResult json = new EasyUiJsonResult();

        String str = request.getParameter("page");
        String shopId = request.getParameter("shopId");

        int page = 1;
        if (str == null) {
            str = "1";
        } else {
            page = Integer.parseInt(str);
        }
        int start = (page - 1) * 30;
        List<ShopGoods> findAll = shopUrlService.findAllGoods(shopId, start, 30);
        int total = shopUrlService.goodsTotal(shopId);
        json.setRows(findAll);
        json.setTotal(total);
        return json;
    }

    /**
     * @param request
     * @param response
     * @return ModelAndView
     * @Title jumpGoodsReady
     * @Description 点击店铺链接，进入数据准备界面
     */
    @RequestMapping("/jumpGoodsReady.do")
    public ModelAndView jumpGoodsReady(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsReady");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }

        try {
            if(allShopBlackList == null || allShopBlackList.size() == 0){
                allShopBlackList = customGoodsService.queryAllShopBlackList();
            }
            if(allShopBlackList.contains(shopId)){
                 mv.addObject("isBlack", 1);
            }else{
                 mv.addObject("isBlack", 0);
            }
            List<ShopInfoBean> infos = shopUrlService.queryInfoByShopId(shopId, "");
            List<ShopGoodsInfo> goodsList = shopUrlService.query1688GoodsByShopId(shopId);
            boolean is = false;

            // 获取不到数据的情况下，自动生成店铺类别统计数据
            List<ShopInfoBean> newInfos = new ArrayList<ShopInfoBean>();
            Map<String, ShopInfoBean> resultMap = new HashMap<String, ShopInfoBean>();
            for (ShopGoodsInfo goods : goodsList) {
                if (goods.getWeight() == null || "".equals(goods.getWeight())) {
                    // 循环数据的统计
                    if (resultMap.containsKey(goods.getCategoryId())) {
                        ShopInfoBean spInfo = resultMap.get(goods.getCategoryId());
                        spInfo.setGoodsNum(spInfo.getGoodsNum() + 1);
                        spInfo.setMinWeight(0);
                        spInfo.setMaxWeight(0);
                        spInfo.setWeightInterval(spInfo.getMinWeight() + "-" + spInfo.getMaxWeight());
                        spInfo.setShopId(shopId);
                    } else {
                        ShopInfoBean otSpInfo = new ShopInfoBean();
                        otSpInfo.setCategoryId(goods.getCategoryId());
                        otSpInfo.setMinWeight(0);
                        otSpInfo.setMaxWeight(0);
                        otSpInfo.setGoodsNum(1);
                        otSpInfo.setShopId(shopId);
                        otSpInfo.setWeightInterval(otSpInfo.getMinWeight() + "-" + otSpInfo.getMaxWeight());
                        resultMap.put(goods.getCategoryId(), otSpInfo);
                    }
                } else {
                    // 处理重量,使用正则匹配取出重量
                    String weight_1688 = StrUtils.object2Str(goods.getWeight());
                    weight_1688 = StrUtils.matchStr(weight_1688, "(\\d+(\\.\\d+){0,1})");
                    float weight = Float.valueOf(weight_1688);
                    // 循环数据的统计
                    if (resultMap.containsKey(goods.getCategoryId())) {
                        ShopInfoBean spInfo = resultMap.get(goods.getCategoryId());
                        spInfo.setGoodsNum(spInfo.getGoodsNum() + 1);
                        // 最小值
                        if (spInfo.getMinWeight() > weight) {
                            spInfo.setMinWeight(weight);
                        }
                        // 最大值
                        if (spInfo.getMaxWeight() < weight) {
                            spInfo.setMaxWeight(weight);
                        }
                        spInfo.setWeightInterval(spInfo.getMinWeight() + "-" + spInfo.getMaxWeight());
                        spInfo.setShopId(shopId);
                    } else {
                        ShopInfoBean otSpInfo = new ShopInfoBean();
                        otSpInfo.setCategoryId(goods.getCategoryId());
                        otSpInfo.setMinWeight(weight);
                        otSpInfo.setMaxWeight(weight);
                        otSpInfo.setGoodsNum(1);
                        otSpInfo.setWeightInterval(weight + "-" + weight);
                        otSpInfo.setShopId(shopId);
                        resultMap.put(goods.getCategoryId(), otSpInfo);
                    }
                }
            }

            List<ShopInfoBean> updateInfos = new ArrayList<ShopInfoBean>();
            if (!(infos.size() == resultMap.size())) {
                for (ShopInfoBean newSpInfo : resultMap.values()) {
                    boolean check = false;
                    for (ShopInfoBean spIf : infos) {
                        if (spIf.getCategoryId().equals(newSpInfo.getCategoryId())) {
                            if (!spIf.getWeightInterval().equals(newSpInfo.getWeightInterval())) {
                                updateInfos.add(newSpInfo);
                            }
                            check = true;
                            break;
                        }
                    }
                    if (!check) {
                        newInfos.add(newSpInfo);
                    }
                }
                if (updateInfos.size() > 0) {
                    shopUrlService.updateShopInfos(updateInfos);
                    updateInfos.clear();
                }
                if (newInfos.size() > 0) {
                    mv.addObject("noSave", 1);
                    is = shopUrlService.insertShopInfos(newInfos);
                    if (!is) {
                        shopUrlService.insertShopInfos(newInfos);
                    }
                    if (is) {
                        infos.clear();
                        infos = shopUrlService.queryInfoByShopId(shopId, "");
                    } else {
                        mv.addObject("msgStr", "生成的数据失败，请重试");
                        mv.addObject("show", 0);
                        return mv;
                    }
                } else {
                    mv.addObject("noSave", 0);
                }
            } else {
                mv.addObject("noSave", 0);
                for (ShopInfoBean newSpInfo : resultMap.values()) {
                    for (ShopInfoBean spIf : infos) {
                        if (spIf.getCategoryId().equals(newSpInfo.getCategoryId())) {
                            if (!spIf.getWeightInterval().equals(newSpInfo.getWeightInterval())) {
                                updateInfos.add(newSpInfo);
                            }
                            break;
                        }
                    }
                }
                if (updateInfos.size() > 0) {
                    shopUrlService.updateShopInfos(updateInfos);
                    updateInfos.clear();
                    infos.clear();
                    infos = shopUrlService.queryInfoByShopId(shopId, "");
                }
            }

            Map<String, ShopInfoBean> cidMap = new HashMap<String, ShopInfoBean>();// 统计利润率
            // 计算整体利润率
            float wholeRate = 0;
            int count = 0;
            if (category1688List == null || category1688List.size() == 0) {
                category1688List = shopUrlService.queryAll1688Category();
            }
            // 获取店铺类别关键词重量
            List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
            List<ShopInfoKeyWeight> infoNews = new ArrayList<ShopInfoKeyWeight>();
            for (ShopInfoBean tpSp : infos) {
                if (tpSp.getFirstIntervalRate() > 0) {
                    wholeRate += tpSp.getFirstIntervalRate();
                    count++;
                }
                cidMap.put(tpSp.getCategoryId(), tpSp);
                for (Category1688Bean category : category1688List) {
                    if (tpSp.getCategoryId().equals(category.getCategoryId())) {
                        tpSp.setCategoryName(category.getCategoryName());
                        break;
                    }
                }
                // 复制数据
                ShopInfoKeyWeight infoKw = new ShopInfoKeyWeight();
                infoKw.setCategoryId(tpSp.getCategoryId());
                infoKw.setCategoryName(tpSp.getCategoryName());
                infoKw.setFirstIntervalRate(tpSp.getFirstIntervalRate());
                infoKw.setGoodsNum(tpSp.getGoodsNum());
                infoKw.setIsChoose(tpSp.getIsChoose());
                infoKw.setOtherIntervalRate(tpSp.getOtherIntervalRate());
                infoKw.setShopId(tpSp.getShopId());
                infoKw.setSuggestRate(tpSp.getSuggestRate());
                infoKw.setWeightInterval(tpSp.getWeightInterval());
                infoKw.setIsForbid(tpSp.getIsForbid());
                if (tpSp.getWeightVal() == 0) {
                    float tempWeight = shopUrlService.calculateAvgWeightByCatid(shopId, tpSp.getCategoryId());
                    BigDecimal b = new BigDecimal(tempWeight);
                    infoKw.setWeightVal(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                } else {
                    infoKw.setWeightVal(tpSp.getWeightVal());
                }

                List<ShopCatidWeight> ctwts = new ArrayList<ShopCatidWeight>();
                infoKw.setCtwts(ctwts);

                // 放入类别关键词重量数据
                for (ShopCatidWeight scWt : ctwtList) {
                    if (scWt.getCatid().equals(infoKw.getCategoryId())
                            && !("".equals(scWt.getKeyword()) || scWt.getKeyword() == null)) {
                        infoKw.getCtwts().add(scWt);
                    }
                }

                // 判断类别重量是否存在，不存在给出默认值

                infoNews.add(infoKw);

            }
            if (count == 0) {
                mv.addObject("wholeRate", 0);
            } else {
                mv.addObject("wholeRate",
                        new BigDecimal(wholeRate / count).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());
            }

            // mv.addObject("infos", infos);

            infos.clear();

            List<GoodsProfitReference> profits = shopUrlService.queryGoodsProfitReferences(shopId);
            if (!(profits == null || profits.size() == 0)) {

                // 使用张立伟的计算利润率的逻辑
                for (GoodsProfitReference pft : profits) {
                    if (pft.getFinalWeight() == null || "".equals(pft.getFinalWeight())) {
                        continue;
                    }
                    String wprice = pft.getWprice();
                    if (wprice == null || "".equals(wprice) || wprice.length() < 3) {
                        pft.setFirstPrice(pft.getPrice());
                    } else {
                        wprice = wprice.replace("[", "").replace("]", "");
                        String[] priceLis = wprice.split(",");
                        String[] priceVs = priceLis[0].split("\\$");
                        pft.setFirstPrice(priceVs[1].trim());
                        priceLis = null;
                        priceVs = null;
                    }
                    // 最终售卖价格的
                    double price = Double.valueOf(pft.getFirstPrice());
                    // 重量
                    double finalWeight = Double.valueOf(pft.getFinalWeight());
                    // 运费
                    double feeprice = FeightUtils.getCarFeightNew(finalWeight, Integer.valueOf(pft.getCategoryId()));
                    pft.setFreight(new BigDecimal(feeprice / StrUtils.EXCHANGE_RATE)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    if (pft.getWholesalePrice() == null || "".equals(pft.getWholesalePrice())) {
                        continue;
                    }
                    String[] wholesalePrice = pft.getWholesalePrice().replace("[", "").replace("]", "").split(",");
                    // 1688价格SKU的最高价
                    String strWholesalePrice = wholesalePrice[0].split("\\$")[1];
                    if (strWholesalePrice.indexOf("-") > -1) {
                        strWholesalePrice = strWholesalePrice.split("-")[1];
                    }
                    // 成本利润率=
                    // (我们网站最终展现价格/（1688价格SKU的最高价+运费(这里按照单件运费进行计算))-1
                    double profitMargin = (price
                            / ((Float.valueOf(strWholesalePrice) + feeprice) / StrUtils.EXCHANGE_RATE)) - 1;
                    pft.setRate(new BigDecimal(profitMargin * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    if (cidMap.containsKey(pft.getCategoryId())) {
                        ShopInfoBean spInf = cidMap.get(pft.getCategoryId());
                        spInf.setTotalNum(spInf.getTotalNum() + 1);
                        spInf.setTotalRate(spInf.getTotalRate() + profitMargin);
                    }

                    // 计算5件商品的平均运费
                    double feeprice5 = FeightUtils.getCarFeightNew(finalWeight * 5,
                            Integer.valueOf(pft.getCategoryId()));
                    pft.setFreight5Gd(new BigDecimal(feeprice5 / (5 * StrUtils.EXCHANGE_RATE) * 100)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
            if (cidMap.size() > 0) {
                for (ShopInfoBean tpIf : cidMap.values()) {
                    if (tpIf.getTotalNum() > 0) {
                        tpIf.setSuggestRate(new BigDecimal(tpIf.getTotalRate() / tpIf.getTotalNum())
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                }
            }
            mv.addObject("profits", profits);
            // 查询店铺商品数据处理的状态
            Map<String, Integer> rsMap = shopUrlService.queryDealState(shopId);
            if (rsMap == null || rsMap.size() == 0) {
                mv.addObject("dealState", 0);
            } else {
                if (rsMap.get("shop_state") > 0) {
                    if (rsMap.get("shop_state") == 1) {
                        mv.addObject("dealState", 1);
                    } else if (rsMap.get("shop_state") == 2) {
                        mv.addObject("dealState", 2);
                    } else {
                        mv.addObject("dealState", 0);
                    }
                } else {
                    mv.addObject("dealState", 0);
                }
            }

            // 统计右边异常重量的数据
            Map<String, Double> catidWeightMap = new HashMap<String, Double>();
            for (ShopInfoKeyWeight infoKey : infoNews) {
                catidWeightMap.put(infoKey.getCategoryId(), (double) infoKey.getWeightVal());
            }

            List<GoodsOfferBean> goodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
            // resultMap统计数据
            Map<String, CatidStatisticalResult> catidResultMap = new HashMap<String, CatidStatisticalResult>();
            List<GoodsOfferBean> goodsErrInfos = new ArrayList<GoodsOfferBean>();
            // 计算需要的数据
            Set<String> catidSet = goodsInfos.stream().map(GoodsOfferBean::getCatid).collect(Collectors.toSet());
            for (GoodsOfferBean gdOf : goodsInfos) {
                // “类别的平均重量” * 运费
                // “该产品重量” * 运费
                if (!(catidWeightMap.get(gdOf.getCatid()) == null || catidWeightMap.get(gdOf.getCatid()) == 0)) {
                    // “类别的平均重量” * 运费
                    gdOf.setAvgWeightfreight(FeightUtils.getCarFeightNew(catidWeightMap.get(gdOf.getCatid()), Integer.valueOf(gdOf.getCatid())));
                    gdOf.setGoodsWeightfreight(
                            FeightUtils.getCarFeightNew(gdOf.getWeight(), Integer.valueOf(gdOf.getCatid())));
                    gdOf.setAvgWeightfreight(FeightUtils.getCarFeightNew(catidWeightMap.get(gdOf.getCatid()), Integer.valueOf(gdOf.getCatid())));
                }
                // 如果平均运费为0，则说明是第一次的数据，存在空值，不做判断
                if (gdOf.getAvgWeightfreight() == 0) {
                    continue;
                }
                // 或者商品本身的重量为0，直接标识异常未处理数据，不做判断
                if (gdOf.getGoodsWeightfreight() == 0) {
                    if (gdOf.getWeightFlag() == 0 && gdOf.getWeightFlag() != 5) {
                        gdOf.setWeightFlag(5);
                        goodsErrInfos.add(gdOf);
                    }
                    if (catidResultMap.containsKey(gdOf.getCatid())) {
                        CatidStatisticalResult csRe1 = catidResultMap.get(gdOf.getCatid());
                        csRe1.setWeightZoneNum(csRe1.getWeightZoneNum() + 1);
                    } else {
                        CatidStatisticalResult csRe = new CatidStatisticalResult();
                        csRe.setCatid(gdOf.getCatid());
                        csRe.setShopId(shopId);
                        csRe.setWeightZoneNum(1);
                        catidResultMap.put(gdOf.getCatid(), csRe);
                    }
                    continue;
                }
                // 1太轻:对于 该产品 所在的 “类别的平均重量” * 运费 > 该产品工厂价*0.4 的情况，如果其重量 至少比
                // 类别平均重量 低 30%，就列为重量有问题（太轻）。
                if (gdOf.getAvgWeightfreight() > gdOf.getPrice() * 0.4) {
                    if (gdOf.getWeight() < catidWeightMap.get(gdOf.getCatid()) * 0.7) {
                        if (gdOf.getWeightFlag() == 0 && gdOf.getWeightFlag() != 3) {
                            gdOf.setWeightFlag(3);
                            goodsErrInfos.add(gdOf);
                        }
                        if (catidResultMap.containsKey(gdOf.getCatid())) {
                            CatidStatisticalResult csRe1 = catidResultMap.get(gdOf.getCatid());
                            csRe1.setTooLightNum(csRe1.getTooLightNum() + 1);
                        } else {
                            CatidStatisticalResult csRe = new CatidStatisticalResult();
                            csRe.setCatid(gdOf.getCatid());
                            csRe.setShopId(shopId);
                            csRe.setTooLightNum(1);
                            catidResultMap.put(gdOf.getCatid(), csRe);
                        }
                    }
                }

                // 2太重: “该产品重量” * 运费 > 该产品工厂价*0.8 的 所有商品的情况，如果其重量 比 类别平均重量 高 30%
                // 以外，就列为重量有问题（太重）。
                if (gdOf.getGoodsWeightfreight() > gdOf.getPrice() * 0.8) {
                    if (gdOf.getWeight() > catidWeightMap.get(gdOf.getCatid()) * 1.3) {
                        if (gdOf.getWeightFlag() == 0 && gdOf.getWeightFlag() != 4) {
                            gdOf.setWeightFlag(4);
                            goodsErrInfos.add(gdOf);
                        }
                        if (catidResultMap.containsKey(gdOf.getCatid())) {
                            CatidStatisticalResult csRe1 = catidResultMap.get(gdOf.getCatid());
                            csRe1.setTooHeavyNum(csRe1.getTooHeavyNum() + 1);
                        } else {
                            CatidStatisticalResult csRe = new CatidStatisticalResult();
                            csRe.setCatid(gdOf.getCatid());
                            csRe.setShopId(shopId);
                            csRe.setTooHeavyNum(1);
                            catidResultMap.put(gdOf.getCatid(), csRe);
                        }
                    }
                }
            }
            // 更新异常重量的商品数据
            if (goodsErrInfos.size() > 0) {
                shopUrlService.batchUpdateErrorWeightGoods(goodsErrInfos);
            }

            // 统计生成结果
            for (ShopInfoKeyWeight infoKey : infoNews) {
                if (catidResultMap.containsKey(infoKey.getCategoryId())) {
                    CatidStatisticalResult csRe3 = catidResultMap.get(infoKey.getCategoryId());
                    infoKey.setTooLightNum(csRe3.getTooLightNum());
                    infoKey.setTooHeavyNum(csRe3.getTooHeavyNum());
                    infoKey.setWeightZoneNum(csRe3.getWeightZoneNum());
                }
            }
            goodsInfos.clear();
            catidResultMap.clear();
            ctwtList.clear();
            mv.addObject("infos", infoNews);

            mv.addObject("show", 1);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("jumpGoodsReady error:" + e.getMessage());
            LOG.error("jumpGoodsReady error:", e);
            mv.addObject("msgStr", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("show", 0);
        }
        return mv;
    }

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title saveAndUpdateInfos
     * @Description 保存和更新商品的重量以及价格等信息
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/saveAndUpdateInfos.do")
    public JsonResult saveAndUpdateInfos(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null && "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        String infos = request.getParameter("infos");
        if (infos == null && "".equals(infos)) {
            json.setOk(false);
            json.setMessage("获取参数失败");
            return json;
        }
        try {
            List<ShopInfoBean> shopInfos = JSONArray.parseArray(infos, ShopInfoBean.class);

            List<ShopCatidWeight> cidWtList = new ArrayList<ShopCatidWeight>();

            for (ShopInfoBean spInfo : shopInfos) {
                spInfo.setShopId(shopId);
                spInfo.setAdminId(user.getId());
                // 提取类别关键词重量数据
                if (!(spInfo.getKeyWeight() == null || "".equals(spInfo.getKeyWeight()))) {
                    String[] tempWeightList = spInfo.getKeyWeight().split(",");
                    for (String keyWt : tempWeightList) {
                        String[] keyWtLs = keyWt.split("@");
                        if (keyWtLs != null && keyWtLs.length == 3) {
                            ShopCatidWeight ctwt = new ShopCatidWeight();
                            ctwt.setId(Integer.valueOf(keyWtLs[0]));
                            ctwt.setAdminId(user.getId());
                            ctwt.setAvgWeight(Double.valueOf(keyWtLs[1]));
                            ctwt.setCatid(spInfo.getCategoryId());
                            ctwt.setKeyword(keyWtLs[2]);
                            ctwt.setShopId(shopId);
                            cidWtList.add(ctwt);
                        }
                        keyWtLs = null;
                    }
                    tempWeightList = null;
                }
            }
            shopUrlService.saveAndUpdateInfos(shopInfos);
            // 判断是否存在类别关键词重量数据，存在的话插入和更新数据
            if (cidWtList.size() > 0) {
                List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
                if (ctwtList == null || ctwtList.size() == 0) {
                    shopUrlService.batchInsertCatidWeight(cidWtList);
                } else {
                    List<ShopCatidWeight> insertCidWtList = new ArrayList<ShopCatidWeight>();
                    List<ShopCatidWeight> updateCidWtList = new ArrayList<ShopCatidWeight>();
                    for (ShopCatidWeight newCtwt : cidWtList) {
                        if (newCtwt.getId() > 0) {
                            for (ShopCatidWeight oldCtwt : ctwtList) {
                                if (newCtwt.getId() == oldCtwt.getId()) {
                                    updateCidWtList.add(newCtwt);
                                    break;
                                }
                            }
                        } else {
                            insertCidWtList.add(newCtwt);
                        }
                    }
                    if (insertCidWtList.size() > 0) {
                        shopUrlService.batchInsertCatidWeight(insertCidWtList);
                    }
                    if (updateCidWtList.size() > 0) {
                        shopUrlService.batchUpdateCatidWeight(updateCidWtList);
                    }
                }
            }
            // 更新默认标识
            boolean isSuccess = shopUrlService.updateWeightFlag(shopId);
            if (!isSuccess) {
                shopUrlService.updateWeightFlag(shopId);
            }
            // 异步调用清洗商品的方法
            // Thread thread = new Thread(new
            // ShopGoodsDealThread(shopId));thread.start();
            json.setOk(true);
            json.setMessage("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存失败，原因：" + e.getMessage());
            System.err.println("saveAndUpdateInfos error:" + e.getMessage());
            LOG.error("saveAndUpdateInfos error:", e);
        }
        return json;
    }

    @RequestMapping("/doGoodsClear.do")
    public JsonResult doGoodsClear(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setMessage("用户未登录");
            json.setOk(false);
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setMessage("获取shopId失败");
            json.setOk(false);
            return json;
        }

        try {// 异步调用清洗商品的方法
            Thread thread = new Thread(new ShopGoodsDealThread(shopId));
            thread.start();
            json.setOk(true);
            json.setMessage("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行失败，原因：" + e.getMessage());
            System.err.println("doGoodsClear error:" + e.getMessage());
            LOG.error("doGoodsClear error:", e);
        }
        return json;
    }

    /**
     * @param request
     * @param response
     * @return ModelAndView
     * @Title beforeOnlineGoodsShow
     * @Description 即将上线商品展示
     */
    @RequestMapping("/beforeOnlineGoodsShow.do")
    public ModelAndView beforeOnlineGoodsShow(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsReadyShow");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }

        try {
            List<ShopGoodsInfo> goodsList = shopUrlService.queryDealGoodsByShopId(shopId);
            if (goodsList == null || goodsList.size() == 0) {
                mv.addObject("msgStr", "当前店铺已无处理好的商品!");
                mv.addObject("show", 0);
                mv.addObject("goodsNum", 0);
            } else {
                String ip = request.getRemoteAddr();
                System.err.println("ip:" + ip);
                if(ip.contains("27.115.38.42") || ip.contains("192.168.1.27")){
                    goodsList.forEach(e->{
                        e.setImgUrl(e.getImgUrl().replace(LOCAL_WEB_SITE,REMOTE_WEB_SITE));
                        e.setRemotePath(e.getRemotePath().replace(LOCAL_WEB_SITE,REMOTE_WEB_SITE));
                    });
                }

                // 获取上线状态和编辑状态
                List<CustomGoodsPublish> gdList = shopUrlService.queryOnlineGoodsByShopId(shopId);
                List<ShopGoodsInfo> newList = new ArrayList<ShopGoodsInfo>();
                List<ShopGoodsInfo> isEditedList = new ArrayList<ShopGoodsInfo>();
                List<ShopGoodsInfo> unEditedList = new ArrayList<ShopGoodsInfo>();
                Map<String, Integer> validMap = new HashMap<String, Integer>();
                Map<String, String> isEditMap = new HashMap<String, String>();
                for (CustomGoodsPublish onlGd : gdList) {
                    validMap.put(onlGd.getPid(), onlGd.getValid());
                    isEditMap.put(onlGd.getPid(), String.valueOf(onlGd.getIsEdited()));
                }

                for (ShopGoodsInfo spGoods : goodsList) {
                    if (validMap.containsKey(spGoods.getPid())) {
                        spGoods.setOnlineFlag(1);
                        spGoods.setOnlineValid(validMap.get(spGoods.getPid()));
                    }
                    if (isEditMap.containsKey(spGoods.getPid())) {
                        spGoods.setOnlineEdit(Integer.valueOf(isEditMap.get(spGoods.getPid())));
                    }
                    String range_price = StrUtils.object2Str(spGoods.getRangePrice());
                    if (StringUtils.isBlank(range_price)) {
                        List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",
                                StrUtils.object2Str(spGoods.getWprice()));
                        if (matchStrList != null && !matchStrList.isEmpty()) {
                            range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                            if (matchStrList.size() > 1) {
                                range_price = range_price + "-"
                                        + StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                            }
                        } else {
                            range_price = StrUtils.object2Str(spGoods.getPrice());
                        }
                    }
                    spGoods.setShowPrice(range_price);
                    // 完整图片路径
                    if (!(spGoods.getRemotePath() == null || "".equals(spGoods.getRemotePath()))) {
                        spGoods.setImgUrl(spGoods.getRemotePath() + spGoods.getImgUrl());
                    }
                    spGoods.setGoodsUrl("&source=D" + Md5Util.encoder(spGoods.getPid()) + "&item=" + spGoods.getPid());

                    // 检查是否存在详情图片
                    if (spGoods.getEnInfo() == null || "".equals(spGoods.getEnInfo())) {
                        spGoods.setEnInfoNum(0);
                    } else {
                        // jsoup解析eninfo数据
                        Document tempDoc = Jsoup.parseBodyFragment(spGoods.getEnInfo());
                        Elements imgLst = tempDoc.getElementsByTag("img");
                        if (!(imgLst == null || imgLst.size() == 0)) {
                            spGoods.setEnInfoNum(imgLst.size());
                        }
                        tempDoc = null;
                    }
                    if (spGoods.getOnlineFlag() > 0) {
                        if (spGoods.getOnlineEdit() > 0) {
                            isEditedList.add(spGoods);
                        } else {
                            unEditedList.add(spGoods);
                        }
                    } else {
                        newList.add(spGoods);
                    }

                }
                mv.addObject("isEditedList", isEditedList);
                mv.addObject("unEditedList", unEditedList);
                mv.addObject("newList", newList);
                mv.addObject("goodsNum", goodsList.size());
                goodsList.clear();
                // 查询店铺商品数据处理的状态
                Map<String, String> stateMap = shopUrlService.queryShopDealState(shopId);
                if (stateMap.size() > 0) {
                    // 0待发布 1正在发布 2发布成功3发布失败
                    if ("0".equals(stateMap.get("onlineState"))) {
                        mv.addObject("onlineState", 0);
                        mv.addObject("stateDescribe", "待处理");
                    } else if ("1".equals(stateMap.get("onlineState"))) {
                        mv.addObject("onlineState", 1);
                        mv.addObject("stateDescribe", "正在发布");
                    } else if ("2".equals(stateMap.get("onlineState"))) {
                        mv.addObject("onlineState", 2);
                        mv.addObject("stateDescribe", "发布成功");
                    } else if ("3".equals(stateMap.get("onlineState"))) {
                        mv.addObject("onlineState", 3);
                        mv.addObject("stateDescribe", "发布失败");
                    } else if ("4".equals(stateMap.get("onlineState"))) {
                        mv.addObject("onlineState", 4);
                        mv.addObject("stateDescribe", "待上线");
                    }
                } else {
                    mv.addObject("onlineState", 0);
                    mv.addObject("stateDescribe", "待处理");
                }
                mv.addObject("show", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("beforeOnlineGoodsShow error:" + e.getMessage());
            LOG.error("beforeOnlineGoodsShow error:", e);
            mv.addObject("msgStr", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("show", 0);
        }
        return mv;
    }

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title deleteShopReadyGoods
     * @Description 删除店铺商品清洗后的数据（非物理删除）
     */
    @ResponseBody
    @RequestMapping("/deleteShopReadyGoods.do")
    public JsonResult deleteShopReadyGoods(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null && "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        String pids = request.getParameter("pids");
        if (pids == null && "".equals(pids)) {
            json.setOk(false);
            json.setMessage("获取pid失败");
            return json;
        }
        try {

            shopUrlService.deleteShopReadyGoods(shopId, pids);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行失败，原因：" + e.getMessage());
            System.err.println("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:" + e.getMessage());
            LOG.error("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:", e);
        }
        return json;
    }


     /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title setShopGoodsNoSold
     * @Description 设置店铺商品不过滤销量
     */
    @ResponseBody
    @RequestMapping("/setShopGoodsNoSold.do")
    public JsonResult setShopGoodsNoSold(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null && "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        String pids = request.getParameter("pids");
        if (pids == null && "".equals(pids)) {
            json.setOk(false);
            json.setMessage("获取pid失败");
            return json;
        }
        try {

            shopUrlService.setShopGoodsNoSold(shopId, pids);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行失败，原因：" + e.getMessage());
            System.err.println("shopId:" + shopId + ",pids:" + pids + ",setShopGoodsNoSold error:" + e.getMessage());
            LOG.error("shopId:" + shopId + ",pids:" + pids + ",setShopGoodsNoSold error:", e);
        }
        return json;
    }



    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title deleteShopOfferGoods
     * @Description 删除店铺商品原始表数据
     */
    @ResponseBody
    @RequestMapping("/deleteShopOfferGoods.do")
    public JsonResult deleteShopOfferGoods(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null && "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        String pids = request.getParameter("pids");
        if (pids == null && "".equals(pids)) {
            json.setOk(false);
            json.setMessage("获取pid失败");
            return json;
        }
        try {

            shopUrlService.deleteShopOfferGoods(shopId, pids);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行失败，原因：" + e.getMessage());
            System.err.println("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:" + e.getMessage());
            LOG.error("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:", e);
        }
        return json;
    }

    /**
     * @param request
     * @param response
     * @return ModelAndView
     * @Title showShopPublicImg
     * @Description 展示店铺商品的公共图片
     */
    @RequestMapping("/showShopPublicImg.do")
    public ModelAndView showShopPublicImg(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsAllImgShow");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }

        String useHmStr = request.getParameter("useHm");
        int useHm = 0;
        if (useHmStr == null || "".equals(useHmStr)) {
            useHm = 0;
        } else {
            useHm = Integer.valueOf(useHmStr);
        }
        mv.addObject("useHm", useHm);

        try {
            List<ShopGoodsEnInfo> imgsList = shopUrlService.queryDealGoodsWithInfoByShopId(shopId);
            if (imgsList == null || imgsList.size() == 0) {
                mv.addObject("msgStr", "获取数据失败");
                mv.addObject("show", 0);
            } else {

                List<ShopGoodsLocalImg> localPidImgs = new ArrayList<ShopGoodsLocalImg>();
                for (ShopGoodsEnInfo spGoods : imgsList) {
                    // jsoup解析eninfo数据
                    if (spGoods.getEnInfo() == null || "".equals(spGoods.getEnInfo())) {
                        continue;
                    }
                    Document tempDoc = Jsoup.parseBodyFragment(spGoods.getEnInfo());
                    Elements imgLst = tempDoc.getElementsByTag("img");
                    if (!(imgLst == null || imgLst.size() == 0)) {
                        List<String> imgs = new ArrayList<String>();
                        for (Element imgEl : imgLst) {
                            String imgUrl = imgEl.attr("src");
                            if (imgUrl == null || "".equals(imgUrl)) {
                                continue;
                            } else {
                                imgs.add(imgUrl);
                                if (useHm > 0) {
                                    if (imgUrl.contains("http://")) {
                                        continue;
                                    }
                                    ShopGoodsLocalImg lcImg = new ShopGoodsLocalImg();
                                    lcImg.setPid(spGoods.getPid());
                                    lcImg.setLpImg(spGoods.getLocalPath().replace("\\", "/") + imgUrl);
                                    File tempFile = new File(lcImg.getLpImg());
                                    long tempLength = tempFile.length();
                                    lcImg.setImgSize(tempLength);
                                    lcImg.setImgMd5(MD5Util.getFileMD5String(tempFile));
                                    localPidImgs.add(lcImg);
                                }
                            }
                        }
                        spGoods.setImgs(imgs);
                    }
                    tempDoc = null;
                    imgLst.clear();
                }
                // 公共图片的处理，做成相同的图片只展示一张
                Map<String, ShopGoodsPublicImg> resultMap = new HashMap<String, ShopGoodsPublicImg>();
                // 使用 图片识别
                if (useHm > 0) {
                    // 公共图片的处理，做成相同的图片只展示一张
                    for (ShopGoodsLocalImg lcImg : localPidImgs) {
                        if (resultMap.containsKey(lcImg.getImgMd5())) {
                            ShopGoodsPublicImg tpLcImg = resultMap.get(lcImg.getImgMd5());
                            tpLcImg.setTotalNum(tpLcImg.getTotalNum() + 1);
                        } else {
                            ShopGoodsPublicImg tpLcImg = new ShopGoodsPublicImg();
                            if (lcImg.getLpImg().contains(SHOP_GOODS_LOCAL_PATH_K)) {
                                tpLcImg.setImgUrl(lcImg.getLpImg().replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K));
                            } else {
                                tpLcImg.setImgUrl(lcImg.getLpImg().replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J));
                            }
                            tpLcImg.setPids(lcImg.getImgMd5());
                            tpLcImg.setTotalNum(tpLcImg.getTotalNum() + 1);
                            resultMap.put(lcImg.getImgMd5(), tpLcImg);
                        }
                    }
                } else {
                    // 普通文件名称对比
                    Map<String, List<String>> pidEninfoImgs = new HashMap<String, List<String>>();
                    // 1.图片名称相同的进行匹配
                    for (ShopGoodsEnInfo enInfo : imgsList) {
                        if (!(enInfo.getImgs() == null || enInfo.getImgs().size() == 0)) {
                            // 汉明距离数据准备
                            List<String> localImgs = new ArrayList<String>();
                            if (pidEninfoImgs.containsKey(enInfo.getPid())) {
                                localImgs = pidEninfoImgs.get(enInfo.getPid());
                            } else {
                                pidEninfoImgs.put(enInfo.getPid(), localImgs);
                            }

                            for (String tempImg : enInfo.getImgs()) {
                                // 获取详情图片的本地全路径
                                localImgs.add(enInfo.getLocalPath().replace("\\", "/") + tempImg);
                                String checkImg = tempImg.substring(tempImg.lastIndexOf("/") + 1);
                                if (resultMap.containsKey(checkImg)) {
                                    ShopGoodsPublicImg gdImg = resultMap.get(checkImg);
                                    String pids = (gdImg.getPids() == null ? "" : gdImg.getPids()) + ",";
                                    if (!(pids.contains(enInfo.getPid() + ","))) {
                                        gdImg.setPids((gdImg.getPids() == null ? "" : gdImg.getPids()) + ","
                                                + enInfo.getPid());
                                        gdImg.setTotalNum(gdImg.getTotalNum() + 1);
                                    }
                                } else {
                                    ShopGoodsPublicImg gdImg = new ShopGoodsPublicImg();
                                    gdImg.setPids(
                                            (gdImg.getPids() == null ? "" : gdImg.getPids()) + "," + enInfo.getPid());
                                    gdImg.setTotalNum(gdImg.getTotalNum() + 1);
                                    gdImg.setImgUrl(enInfo.getRemotePath() + tempImg);
                                    resultMap.put(checkImg, gdImg);
                                }
                            }
                        }
                    }
                }

                List<ShopGoodsPublicImg> newImgsList = new ArrayList<ShopGoodsPublicImg>();
                if (resultMap.size() > 0) {
                    for (ShopGoodsPublicImg tempGd : resultMap.values()) {
                        newImgsList.add(tempGd);
                    }
                }
                if (newImgsList.size() > 0) {
                    // 选中排列第一
                    Collections.sort(newImgsList, new Comparator<ShopGoodsPublicImg>() {
                        public int compare(ShopGoodsPublicImg o1, ShopGoodsPublicImg o2) {
                            return o2.getTotalNum() - o1.getTotalNum();
                        }
                    });
                }
                imgsList.clear();
                localPidImgs.clear();
                mv.addObject("newImgsList", newImgsList);
                mv.addObject("showTotal", newImgsList.size());
                mv.addObject("show", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",showShopPublicImg error:" + e.getMessage());
            LOG.error("shopId:" + shopId + ",showShopPublicImg error:", e);
            mv.addObject("msgStr", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("show", 0);
        }
        return mv;
    }

    @RequestMapping("/showShopPublicImgTest.do")
    public ModelAndView showShopPublicImgTest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsAllImgShow");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }

        String useHmStr = request.getParameter("useHm");
        int useHm = 0;
        if (useHmStr == null || "".equals(useHmStr)) {
            useHm = 0;
        } else {
            useHm = Integer.valueOf(useHmStr);
        }
        mv.addObject("useHm", useHm);

        try {
            List<ShopGoodsEnInfo> imgsList = shopUrlService.queryDealGoodsWithInfoByShopId(shopId);
            if (imgsList == null || imgsList.size() == 0) {
                mv.addObject("msgStr", "获取数据失败");
                mv.addObject("show", 0);
            } else {

                List<ShopGoodsLocalImg> localPidImgs = new ArrayList<ShopGoodsLocalImg>();

                for (ShopGoodsEnInfo spGoods : imgsList) {
                    // jsoup解析eninfo数据
                    if (spGoods.getEnInfo() == null || "".equals(spGoods.getEnInfo())) {
                        continue;
                    }
                    Document tempDoc = Jsoup.parseBodyFragment(spGoods.getEnInfo());
                    Elements imgLst = tempDoc.getElementsByTag("img");
                    if (!(imgLst == null || imgLst.size() == 0)) {
                        List<String> tempImgList = new ArrayList<String>();
                        for (Element imgEl : imgLst) {
                            String imgUrl = imgEl.attr("src");
                            if (imgUrl == null || "".equals(imgUrl)) {
                                continue;
                            } else {
                                if (imgUrl.contains("http://")) {
                                    continue;
                                } else {
                                    tempImgList.add(imgUrl.replace("\\", "/"));
                                }
                            }
                        }
                        spGoods.setImgs(tempImgList);
                        parseImgMd5(localPidImgs, spGoods.getPid(), spGoods.getLocalPath(), genImgMd5(spGoods.getLocalPath(), tempImgList));
                    }
                    tempDoc = null;
                    imgLst.clear();
                }
                // 公共图片的处理，做成相同的图片只展示一张
                Map<String, ShopGoodsPublicImg> resultMap = new HashMap<String, ShopGoodsPublicImg>();
                // 使用 图片识别
                // 公共图片的处理，做成相同的图片只展示一张
                for (ShopGoodsLocalImg lcImg : localPidImgs) {
                    if (resultMap.containsKey(lcImg.getImgMd5())) {
                        ShopGoodsPublicImg tpLcImg = resultMap.get(lcImg.getImgMd5());
                        tpLcImg.setTotalNum(tpLcImg.getTotalNum() + 1);
                    } else {
                        ShopGoodsPublicImg tpLcImg = new ShopGoodsPublicImg();
                        if (lcImg.getLpImg().contains(SHOP_GOODS_LOCAL_PATH_K)) {
                            tpLcImg.setImgUrl(lcImg.getLpImg().replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K));
                        } else {
                            tpLcImg.setImgUrl(lcImg.getLpImg().replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J));
                        }
                        tpLcImg.setPids(lcImg.getImgMd5());
                        tpLcImg.setTotalNum(tpLcImg.getTotalNum() + 1);
                        resultMap.put(lcImg.getImgMd5(), tpLcImg);
                    }
                }
                List<ShopGoodsPublicImg> newImgsList = new ArrayList<ShopGoodsPublicImg>();
                if (resultMap.size() > 0) {
                    for (ShopGoodsPublicImg tempGd : resultMap.values()) {
                        newImgsList.add(tempGd);
                    }
                }
                if (newImgsList.size() > 0) {
                    // 选中排列第一
                    String ip = request.getRemoteAddr();
                    System.err.println("ip:" + ip);
                    if(ip.contains("27.115.38.42") || ip.contains("192.168.1.27")){
                        newImgsList.forEach(e->{
                            e.setImgUrl(e.getImgUrl().replace(LOCAL_WEB_SITE, REMOTE_WEB_SITE));
                        });
                    }
                    newImgsList.sort(Comparator.comparingInt(ShopGoodsPublicImg::getTotalNum).reversed());
                }
                imgsList.clear();
                localPidImgs.clear();
                mv.addObject("newImgsList", newImgsList);
                mv.addObject("showTotal", newImgsList.size());
                mv.addObject("show", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",showShopPublicImg error:" + e.getMessage());
            LOG.error("shopId:" + shopId + ",showShopPublicImg error:", e);
            mv.addObject("msgStr", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("show", 0);
        }
        return mv;
    }

    private Map<String, String> genImgMd5(String localPath, List<String> tempImgList) throws IOException {
        Map<String, String> resultMap = new HashMap<String, String>();
        String resultStr = "";
        String filenames = "";
        for (String imgUrl : tempImgList) {
            filenames += "," + imgUrl.trim();
        }
        String url = "http://192.168.1.28:3000/calcuMD5?path=" + localPath.replace("\\", "/")
                + "&filenames=" + filenames.substring(1);
        BufferedReader in = null;
        //try {
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NTshowShopPublicImgTest 5.1;SV1)");
        connection.setReadTimeout(20 * 1000);
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        // 定义 BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            resultStr += line;
        }
        //} catch (Exception e) {
        //e.printStackTrace();
        //System.err.println("发送GET请求出现异常！" + e.getMessage());
        //}
        // 使用finally块来关闭输入流
        //finally {
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        //}
        if (StringUtils.isNotBlank(resultStr)) {
            try {
                JSONObject myJson = JSONObject.parseObject(resultStr);
                if (!myJson.isEmpty()) {
                    List<String> md5List = JSONArray.parseArray(myJson.getString("files"), String.class);
                    if (!(md5List == null || md5List.isEmpty())) {
                        for (String md5 : md5List) {
                            String[] imgMd5Arr = md5.split(":");
                            if (imgMd5Arr.length == 2) {
                                resultMap.put(imgMd5Arr[0], imgMd5Arr[1]);
                            } else {
                                System.err.println("md5:" + md5 + ",split error!");
                            }
                            imgMd5Arr = null;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (tempImgList.size() != resultMap.size()) {
            System.err.println("tempImgList size:" + tempImgList.size() + " != resultMap size:" + resultMap.size());
            // resultMap.clear();
        }
        return resultMap;
    }

    private void parseImgMd5(List<ShopGoodsLocalImg> localPidImgs, String pid, String localPath, Map<String, String> resultMap) {
        if (!(resultMap == null || resultMap.isEmpty())) {
            try {
                for (String keyVal : resultMap.keySet()) {
                    ShopGoodsLocalImg lcImg = new ShopGoodsLocalImg();
                    lcImg.setPid(pid);
                    lcImg.setLpImg((localPath + keyVal).replace("\\", "/"));
                    File tempFile = new File(lcImg.getLpImg());
                    long tempLength = tempFile.length();
                    lcImg.setImgSize(tempLength);
                    lcImg.setImgMd5(resultMap.get(keyVal));
                    localPidImgs.add(lcImg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title deleteGoodsImgs
     * @Description 删除店铺商品的公共图片
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/deleteGoodsImgs.do")
    public JsonResult deleteGoodsImgs(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null && "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        String useHmStr = request.getParameter("useHm");
        int useHm = 0;
        if (useHmStr == null && "".equals(useHmStr)) {
            json.setOk(false);
            json.setMessage("获取图片识别标识失败");
            return json;
        } else {
            useHm = Integer.parseInt(useHmStr);
        }

        String imgs = request.getParameter("imgs");
        if (imgs == null && "".equals(imgs)) {
            json.setOk(false);
            json.setMessage("获取参数失败");
            return json;
        }
        try {
            List<ShopGoodsPublicImg> gdImgList = JSONArray.parseArray(imgs, ShopGoodsPublicImg.class);

            if (gdImgList == null || gdImgList.size() == 0) {
                json.setOk(false);
                json.setMessage("转换数据失败，请重试");
                return json;
            } else {
                // 使用图片识别的方法时，进行图片的删除
                if (useHm > 0) {
                    // 取出MD5，放入list集合，方便匹配
                    List<String> tempMd5List = new ArrayList<String>();
                    for (ShopGoodsPublicImg pbImg : gdImgList) {
                        tempMd5List.add(pbImg.getPids());
                    }
                    // 匹配删除商品的pid,移除需要删除的图片
                    List<ShopGoodsEnInfo> imgsList = shopUrlService.queryDealGoodsWithInfoByShopId(shopId);

                    //获取全部图片的MD5数据
                    Map<String, String> resultMap = new HashMap<String, String>();

                    for (ShopGoodsEnInfo imgGd : imgsList) {
                        List<String> tempImgList = new ArrayList<String>();
                        if (imgGd.getEnInfo() == null || "".equals(imgGd.getEnInfo())) {
                            continue;
                        }
                        Document tempDoc = Jsoup.parseBodyFragment(imgGd.getEnInfo());
                        Elements imgLst = tempDoc.getElementsByTag("img");
                        if (!(imgLst == null || imgLst.size() == 0)) {
                            for (Element imgEl : imgLst) {
                                String imgUrl = imgEl.attr("src");
                                if (imgUrl == null || "".equals(imgUrl)) {
                                    continue;
                                } else {
                                    tempImgList.add(imgUrl);
                                }
                            }
                            Map<String, String> tempMap = new HashMap();
                            try {
                                tempMap = genImgMd5(imgGd.getLocalPath(), tempImgList);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println("发送GET请求出现异常！" + e.getMessage());
                                LOG.error("发送GET请求出现异常！", e);
                                //step v1. @author: cjc @date：2019/3/29 11:55:35   Description : 请求失败就break
                                break;
                            }
                            tempImgList.clear();
                            if (tempMap.size() > 0) {
                                resultMap.putAll(tempMap);
                            }
                        }
                        tempDoc = null;
                    }

                    List<ShopGoodsEnInfo> deletLst = new ArrayList<ShopGoodsEnInfo>();
                    // 保存删除记录
                    List<ShopGoodsInfo> deleteGoodsInfos = new ArrayList<ShopGoodsInfo>();
                    for (ShopGoodsEnInfo imgGd : imgsList) {

                        if (imgGd.getEnInfo() == null || "".equals(imgGd.getEnInfo())) {
                            continue;
                        }
                        Document tempDoc = Jsoup.parseBodyFragment(imgGd.getEnInfo());
                        Elements imgLst = tempDoc.getElementsByTag("img");
                        boolean isExists = false;
                        if (!(imgLst == null || imgLst.size() == 0)) {
                            for (Element imgEl : imgLst) {
                                String imgUrl = imgEl.attr("src");
                                if (imgUrl == null || "".equals(imgUrl)) {
                                    continue;
                                } else {
                                    String tempMd5 = resultMap.get(imgUrl);
                                    if (StringUtils.isNotBlank(tempMd5) && tempMd5List.contains(tempMd5)) {
                                        isExists = true;
                                        ShopGoodsInfo delGd = new ShopGoodsInfo();
                                        delGd.setShopId(shopId);
                                        delGd.setPid(imgGd.getPid());
                                        delGd.setImgUrl(imgUrl);
                                        delGd.setLocalPath(imgGd.getLocalPath());
                                        delGd.setRemotePath(imgGd.getRemotePath());
                                        deleteGoodsInfos.add(delGd);
                                        imgEl.remove();
                                    }
                                }
                            }
                        }
                        if (isExists) {
                            imgGd.setEnInfo(tempDoc.toString());
                            deletLst.add(imgGd);
                        }
                        tempDoc = null;
                        imgLst.clear();
                    }
                    tempMd5List.clear();
                    shopUrlService.updateGoodsEninfo(deletLst);
                    shopUrlService.insertShopGoodsDeleteImgs(deleteGoodsInfos, user.getId());
                    deleteGoodsInfos.clear();
                    json.setOk(true);
                    json.setMessage("执行成功");
                } else {

                    // 转换成list数据,放入map中，方便后面匹配
                    Map<String, List<String>> pidImgs = new HashMap<String, List<String>>();
                    for (ShopGoodsPublicImg gdImg : gdImgList) {
                        if (!(gdImg.getPids() == null || "".equals(gdImg.getPids()))) {
                            String tempImgName = gdImg.getImgUrl().substring(gdImg.getImgUrl().lastIndexOf("/"));
                            String[] tempPids = gdImg.getPids().split(",");
                            for (String tpPid : tempPids) {
                                if (!(tpPid == null || "".equals(tpPid))) {
                                    if (pidImgs.containsKey(tpPid)) {
                                        List<String> imgList = pidImgs.get(tpPid);
                                        imgList.add(tempImgName);
                                    } else {
                                        List<String> tempImgs = new ArrayList<String>();
                                        tempImgs.add(tempImgName);
                                        pidImgs.put(tpPid, tempImgs);
                                    }
                                }
                            }
                        }
                    }
                    // 匹配删除商品的pid,移除需要删除的图片
                    List<ShopGoodsEnInfo> imgsList = shopUrlService.queryDealGoodsWithInfoByShopId(shopId);
                    List<ShopGoodsEnInfo> deletLst = new ArrayList<ShopGoodsEnInfo>();
                    // 保存删除记录
                    List<ShopGoodsInfo> deleteGoodsInfos = new ArrayList<ShopGoodsInfo>();
                    for (ShopGoodsEnInfo imgGd : imgsList) {
                        if (pidImgs.containsKey(imgGd.getPid())) {
                            if (imgGd.getEnInfo() == null || "".equals(imgGd.getEnInfo())) {
                                continue;
                            }
                            Document tempDoc = Jsoup.parseBodyFragment(imgGd.getEnInfo());
                            Elements imgLst = tempDoc.getElementsByTag("img");
                            if (!(imgLst == null || imgLst.size() == 0)) {
                                List<String> tempImgs = pidImgs.get(imgGd.getPid());
                                for (Element imgEl : imgLst) {
                                    String imgUrl = imgEl.attr("src");
                                    if (imgUrl == null || "".equals(imgUrl)) {
                                        continue;
                                    } else {
                                        String tempImgName = imgUrl.substring(imgUrl.lastIndexOf("/"));
                                        if (tempImgs.contains(tempImgName)) {
                                            ShopGoodsInfo delGd = new ShopGoodsInfo();
                                            delGd.setShopId(shopId);
                                            delGd.setPid(imgGd.getPid());
                                            delGd.setImgUrl(imgUrl);
                                            delGd.setLocalPath(imgGd.getLocalPath());
                                            delGd.setRemotePath(imgGd.getRemotePath());
                                            deleteGoodsInfos.add(delGd);
                                            imgEl.remove();
                                        }
                                    }
                                }
                            }
                            imgGd.setEnInfo(tempDoc.toString());
                            deletLst.add(imgGd);
                            tempDoc = null;
                            imgLst.clear();
                        }
                    }
                    shopUrlService.updateGoodsEninfo(deletLst);
                    shopUrlService.insertShopGoodsDeleteImgs(deleteGoodsInfos, user.getId());
                    deleteGoodsInfos.clear();
                    json.setOk(true);
                    json.setMessage("执行成功");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("保存失败，原因：" + e.getMessage());
            System.err.println("shopId:" + shopId + ",deleteGoodsImgs error:" + e.getMessage());
            LOG.error("shopId:" + shopId + ",deleteGoodsImgs error:", e);
        }
        return json;
    }

    /**
     * @param request
     * @param response
     * @return ModelAndView
     * @Title editGoods
     * @Description 店铺商品编辑
     */
    @SuppressWarnings({"unchecked"})
    @RequestMapping("/editGoods.do")
    public ModelAndView editGoods(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("goodsEdit");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            mv.addObject("msgStr", "获取pid失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("pid", pid);
        }

        try {
            CustomGoodsPublish goods = shopUrlService.queryGoodsInfo(shopId, pid);
            if (goods == null) {
                mv.addObject("msgStr", "获取商品失败");
                mv.addObject("show", 0);
                return mv;
            }

            // 根据shopid查询店铺数据
            int queryId = 0;
            if (!(goods.getShopId() == null || "".equals(goods.getShopId()))) {
                ShopManagerPojo spmg = customGoodsService.queryByShopId(goods.getShopId());
                if (spmg != null) {
                    queryId = spmg.getId();
                }
            }

            mv.addObject("jumpShopId", queryId);

            // 取出1688原货源链接

            // 将goods的entype属性值取出来,即规格图
            List<TypeBean> typeList = deal1688GoodsType(goods);

            // 将goods的img属性值取出来,即橱窗图
            request.setAttribute("showimgs", JSONArray.toJSON("[]"));
            List<String> imgs = GoodsInfoUtils.deal1688GoodsImg(goods.getImg(), goods.getLocalpath());
            String ip = request.getRemoteAddr();
            System.err.println("ip:" + ip);
            if (ip.contains("27.115.38.42") || ip.contains("192.168.1.27")) {
                if (CollectionUtils.isNotEmpty(imgs)) {
                    List<String> tempImgs = new ArrayList<>(imgs.size());
                    imgs.forEach(e -> {
                        tempImgs.add(e.replace(LOCAL_WEB_SITE, REMOTE_WEB_SITE));
                    });
                    goods.setShowImages(tempImgs);
                }
            } else {
                goods.setShowImages(imgs);
            }

            if (CollectionUtils.isNotEmpty(goods.getShowImages())) {
                String firstImg = goods.getShowImages().get(0);
                goods.setShowMainImage(firstImg.replace(".60x60.", ".400x400."));
                request.setAttribute("showimgs", JSONArray.toJSON(goods.getShowImages()));
            }

            HashMap<String, String> pInfo = deal1688Sku(goods);
            request.setAttribute("showattribute", pInfo);

            // 处理Sku数据
            // 判断是否是区间价格，含有区间价格的获取sku数据进行处理

            if (goods.getRangePrice() == null || "".equals(goods.getRangePrice()) || goods.getSku() == null
                    || "".equals(goods.getSku())) {
                // request.setAttribute("showSku", JSONArray.fromObject("[]"));
            } else {
                List<ImportExSku> skuList = new ArrayList<ImportExSku>();
                skuList = (List<ImportExSku>) JSONArray.parseArray(goods.getSku(), ImportExSku.class);
                // 规格标题名称集合
                List<ImportExSkuShow> cbSkus = combineSkuList(typeList, skuList);
                // 集合排序
                Collections.sort(cbSkus, new Comparator<ImportExSkuShow>() {
                    public int compare(ImportExSkuShow o1, ImportExSkuShow o2) {
                        return o1.getPpIds().compareTo(o2.getPpIds());
                    }
                });
                request.setAttribute("showSku", JSONArray.toJSON(cbSkus));

                Map<String, Object> typeNames = new HashMap<String, Object>();
                for (TypeBean tyb : typeList) {
                    if (!typeNames.containsKey(tyb.getTypeId())) {
                        typeNames.put(tyb.getTypeId(), tyb.getType());
                    }
                }
                request.setAttribute("typeNames", typeNames);
            }

            if (typeList.size() > 0) {
                if (ip.contains("27.115.38.42") || ip.contains("192.168.1.27")) {
                    typeList.forEach(e->{
                        if(StringUtils.isNotBlank(e.getImg())){
                            e.setImg(e.getImg().replace(LOCAL_WEB_SITE, REMOTE_WEB_SITE));
                        }
                    });
                }
                request.setAttribute("showtypes", JSONArray.toJSON(typeList));
            } else {
                request.setAttribute("showtypes", JSONArray.toJSON("[]"));
            }

            // 直接使用远程路径
            String localpath = goods.getLocalpath();
            if (localpath == null || "".equals(localpath)) {
                localpath = "";
                goods.setLocalpath("");
            }
            // 设置默认图的路径
            if (!(goods.getShowMainImage().indexOf("http://") > -1
                    || goods.getShowMainImage().indexOf("https://") > -1)) {
                goods.setShowMainImage(localpath + goods.getShowMainImage());
            }
            // 分割eninfo数据，不替换remotepath相同的路径
            if (StringUtil.isBlank(goods.getEninfo())) {
                mv.addObject("text", "");
            } else {
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

                //
                if (ip.contains("27.115.38.42") || ip.contains("192.168.1.27")) {
                    text = text.replace(LOCAL_WEB_SITE, REMOTE_WEB_SITE);
                }
                // 返回待编辑数据到编辑页面
                mv.addObject("text", text);
            }

            mv.addObject("pid", pid);
            goods.setWeight(StrUtils.matchStr(goods.getWeight(), "(\\d+\\.*\\d*)"));
            mv.addObject("goods", goods);
            // 上传图片保存路径----酌情配置
            String savePath = localpath.replace(localIP, rootPath);
            if (IpCheckUtil.checkIsIntranet(request)) {
                savePath = localpath.replace(wanlIP, rootPath);
            }
            mv.addObject("savePath", savePath);
            mv.addObject("localpath", localpath);
            mv.addObject("show", 1);
            return mv;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",editGoods error:" + e.getMessage());
            LOG.error("shopId:" + shopId + ",editGoods error:", e);
            mv.addObject("msgStr", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("show", 0);
        }
        return mv;
    }

    // 处理1688商品的规格图片数据
    private List<TypeBean> deal1688GoodsType(CustomGoodsPublish cgbean) {// 规格
        List<TypeBean> typeList = new ArrayList<TypeBean>();
        if (!(cgbean.getEntype() == null || "".equals(cgbean.getEntype()))) {
            Map<String, List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
            String types = cgbean.getEntype();
            // String remotPath = cgbean.getRemotpath();
            String localPath = cgbean.getLocalpath();
            if (StringUtils.isNotBlank(types) && !StringUtils.equals(types, "[]")) {
                types = types.replace("[[", "[").replace("]]", "]").trim();
                String[] matchStrList = types.split(",\\s*\\[");
                TypeBean typeBean = null;
                String[] tems = null;
                String tem = null;
                for (String str : matchStrList) {
                    str = str.replace("[", "").replace("]", "");
                    if (str.isEmpty()) {
                        continue;
                    }
                    typeBean = new TypeBean();
                    String[] type = str.split(",\\s*");
                    for (int j = 0; j < type.length; j++) {
                        if (type[j].indexOf("id=") > -1) {
                            tems = type[j].split("id=");
                            tem = tems.length > 1 ? tems[1] : "";
                            typeBean.setId(tem);
                        } else if (type[j].indexOf("type=") > -1) {
                            tems = type[j].split("type=");
                            tem = tems.length > 1 ? tems[1] : "";
                            typeBean.setType(tem.replaceAll(chineseChar, ""));
                            typeBean.setLableType(tem.replaceAll(chineseChar, ""));
                        } else if (type[j].indexOf("value=") > -1) {
                            tems = type[j].split("value=");
                            tem = tems.length > 1 ? tems[1] : "";
                            tem = StringUtils.equals(tem, "null") ? String.valueOf(j) : tem;
                            typeBean.setValue(tem.replaceAll(chineseChar, ""));
                        } else if (type[j].indexOf("img=") > -1) {
                            tems = type[j].split("img=");
                            tem = tems.length > 1 ? tems[1] : "";
                            tem = tem.endsWith(".jpg") ? tem : "";
                            tem = StringUtils.isBlank(tem) || StringUtils.equals(tem, "null") ? "" : localPath + tem;
                            typeBean.setImg(tem);
                        }
                    }
                    List<TypeBean> list = typeMap.get(typeBean.getType());
                    if (list == null) {
                        list = new ArrayList<TypeBean>();
                    }
                    if (StringUtils.isBlank(typeBean.getType())) {
                        continue;
                    }
                    if (StringUtils.isBlank(typeBean.getValue())) {
                        typeBean.setType(typeBean.getId());
                    }
                    list.add(typeBean);
                    typeMap.put(typeBean.getType(), list);
                }
                Iterator<Entry<String, List<TypeBean>>> iterator = typeMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    typeList.addAll(iterator.next().getValue());
                }

            }
        }

        return typeList;

    }

    private HashMap<String, String> deal1688Sku(CustomGoodsPublish cgbean) {
        // detail明细
        HashMap<String, String> pInfo = new HashMap<String, String>();
        String detail = cgbean.getEndetail() == null ? "" : cgbean.getEndetail();
        if (StringUtils.isNotBlank(detail)) {
            String[] details = detail.substring(1, detail.length() - 1).split(",");
            int details_length = details.length;
            for (int i = 0; i < details_length; i++) {
                String str_detail = details[i].trim().replaceAll(chineseChar, "");
                if (str_detail.isEmpty() || StrUtils.isMatch(str_detail.substring(0, 1), "\\d+")) {
                    continue;
                }
                if (StrUtils.isFind(str_detail, "(brand\\:)")) {
                    continue;
                }
                if (str_detail.length() < 6) {
                    continue;
                }
                pInfo.put(i + "",
                        str_detail.substring(0, 1).toUpperCase() + str_detail.substring(1, str_detail.length()));
            }
        }
        return pInfo;
    }

    /**
     * 组合生成展示的Sku信息
     *
     * @param typeList
     * @param skuList
     * @return
     */
    private List<ImportExSkuShow> combineSkuList(List<TypeBean> typeList, List<ImportExSku> skuList) {

        List<ImportExSkuShow> cbSkuLst = new ArrayList<ImportExSkuShow>();

        for (ImportExSku ites : skuList) {
            String skuAttrs = "";
            ImportExSkuShow ipes = new ImportExSkuShow();
            // PropIds分组循环
            String[] ppidLst = ites.getSkuPropIds().split(",");
            int totalCount = 0;
            int arrLength = ppidLst.length;
            for (String ppid : ppidLst) {
                // 比较type类别的数据，获取类别信息
                for (TypeBean tyb : typeList) {
                    if (ppid.equals(tyb.getId())) {
                        skuAttrs += ";" + tyb.getId() + "@" + tyb.getType() + "@" + tyb.getValue();
                        totalCount++;
                        break;
                    }
                }
            }
            ppidLst = null;
            // 解析attr数据，获取类别名称对应的ID
            String[] skuAtLst = ites.getSkuAttr().split(";");
            for (String ska : skuAtLst) {
                String[] cbLst = ska.split(":");
                if (cbLst.length == 2) {
                    for (TypeBean tyb : typeList) {
                        if (cbLst[1].equals(tyb.getId())) {
                            tyb.setTypeId(cbLst[0]);
                            break;
                        }
                    }
                }
            }
            skuAtLst = null;
            ipes.setPpIds(ites.getSkuPropIds().replace(",", "_"));
            ipes.setPrice(ites.getSkuVal().getActSkuCalPrice());
            if (skuAttrs == null || "".equals(skuAttrs)) {
                ipes = null;
            } else {
                ipes.setSkuAttrs(skuAttrs.substring(1));
                // skuAttrs获取失败，则不显示sku数据，并且在更新后覆盖原数据
                // type多规格生成的数据中sku只有单规格的数据也剔除掉
                if (arrLength > 0 && arrLength == totalCount) {
                    cbSkuLst.add(ipes);
                }

            }
            skuAttrs = null;
        }
        skuList = null;
        return cbSkuLst;
    }

    @RequestMapping(value = "/uploads", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> uploadsFile(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "";
        String err = "";
        String pid = request.getParameter("pid");

        String localpath = request.getParameter("localpath");
        if (pid == null || "".equals(pid)) {
            msg = "";
            err = "获取PID失败";
        }
        if (localpath == null || "".equals(localpath)) {
            msg = "";
            err = "获取本地路径失败";
        } else {
            System.out.println("pid:" + pid + ",localpath" + localpath);
            try {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                // 获取文件域
                List<MultipartFile> fileList = multipartRequest.getFiles("filedata");
                // 检查配置文件信息是否正常读取
                JsonResult json = new JsonResult();
                if (json.isOk()) {
                    String newLocal = "";
                    if (localpath.contains(SHOP_GOODS_LOCAL_PATH_K)) {
                        newLocal = localpath.replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K);
                    } else {
                        newLocal = localpath.replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J);
                    }

                    newLocal = newLocal.replace("\\", "/");
                    String allLocalPath = "";
                    if (newLocal.endsWith("/")) {
                        allLocalPath = newLocal + pid + "/desc/up_";
                    } else {
                        allLocalPath = newLocal + "/" + pid + "/desc/up_";
                    }
                    for (MultipartFile mf : fileList) {
                        if (!mf.isEmpty()) {
                            // 得到文件保存的名称mf.getOriginalFilename()
                            String originalName = mf.getOriginalFilename();
                            // 文件的后缀取出来
                            String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                            // 文件名称的构成，直接用当前时间，防止重名
                            Long currentTime = System.currentTimeMillis();
                            // 本地服务器磁盘全路径
                            String localFilePath = allLocalPath + currentTime + fileSuffix;
                            // 文件流输出到本地服务器指定路径
                            ImgDownload.writeImageToDisk(mf.getBytes(), localFilePath);
                            // 检查图片分辨率
                            boolean is = ImageCompression.checkImgResolution(localFilePath, 100, 100);
                            if (is) {
                                if (localFilePath.contains(SHOP_GOODS_LOCAL_PATH_K)) {
                                    msg = localFilePath.replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K);
                                } else {
                                    msg = localFilePath.replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J);
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
                LOG.error("上传错误：", e);
            }
        }
        map.put("err", err);
        map.put("msg", msg);
        return map;
    }

    /**
     * 普通js上传文件
     *
     * @return
     */
    @RequestMapping(value = "/uploadByJs", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult uploadByJs(@RequestParam(value = "pid", required = true) String pid,
                                 @RequestParam(value = "localpath", required = true) String localpath,
                                 @RequestParam(value = "uploadfile", required = true) MultipartFile file, HttpServletRequest request) {
        JsonResult json = new JsonResult();

        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("获取PID失败");
        }
        if (localpath == null || "".equals(localpath)) {
            json.setOk(false);
            json.setMessage("获取本地路径失败");
        } else {
            System.out.println("pid:" + pid + ",localpath:" + localpath);
            try {
                String newLocal = "";
                if (localpath.contains(SHOP_GOODS_LOCAL_PATH_K)) {
                    newLocal = localpath.replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K);
                } else {
                    newLocal = localpath.replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J);
                }
                newLocal = newLocal.replace("\\", "/");
                String allLocalPath = "";
                if (newLocal.endsWith("/")) {
                    allLocalPath = newLocal + pid + "/up_";
                } else {
                    allLocalPath = newLocal + "/" + pid + "/up_";
                }
                if (!file.isEmpty()) {
                    String originalName = file.getOriginalFilename();
                    // 文件的后缀取出来
                    String fileSuffix = originalName.substring(originalName.lastIndexOf("."));

                    // 文件名称的构成，直接用当前时间，防止重名
                    Long currentTime = System.currentTimeMillis();
                    // 本地服务器磁盘全路径
                    String localFilePath = allLocalPath + currentTime + fileSuffix;
                    // 文件流输出到本地服务器指定路径
                    ImgDownload.writeImageToDisk(file.getBytes(), localFilePath);

                    // 判断图片的分辨率是否小于400*200
                    boolean checked = ImageCompression.checkImgResolution(localFilePath, 400, 200);
                    if (checked) {
                        // 压缩图片400x400
                        String localFilePath400x400 = allLocalPath + currentTime + ".400x400" + fileSuffix;
                        boolean is400 = ImageCompression.reduceImgByWidth(400, localFilePath, localFilePath400x400);
                        // 压缩图片60x60
                        String localFilePath60x60 = allLocalPath + currentTime + ".60x60" + fileSuffix;
                        boolean is60 = ImageCompression.reduceImgByWidth(60, localFilePath, localFilePath60x60);
                        if (is60 && is400) {
                            if (localFilePath60x60.contains(SHOP_GOODS_LOCAL_PATH_K)) {
                                json.setData(localFilePath60x60.replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K));
                            } else {
                                json.setData(localFilePath60x60.replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J));
                            }
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
                LOG.error("上传错误：", e);
            }
        }
        return json;
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
        String localpath = request.getParameter("localpath");
        if (localpath == null || "".equals(localpath)) {
            json.setOk(false);
            json.setMessage("获取本地路径失败");
            return json;
        }
        System.err.println("pid:" + pid + ",localpath:" + localpath + ";imgs:" + imgs);

        String newImgUrl = "";
        String[] imgLst = imgs.split(";");
        boolean isSuccess = true;
        try {
            String newLocal = "";
            if (localpath.contains(SHOP_GOODS_LOCAL_PATH_K)) {
                newLocal = localpath.replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K);
            } else {
                newLocal = localpath.replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J);
            }
            newLocal = newLocal.replace("\\", "/");
            String allLocalPath = "";
            if (newLocal.endsWith("/")) {
                allLocalPath = newLocal + pid + "/up_";
            } else {
                allLocalPath = newLocal + "/" + pid + "/up_";
            }
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
                    // 文件名称的构成，直接用当前时间，防止重名
                    Long currentTime = System.currentTimeMillis();
                    // 本地服务器磁盘全路径
                    String localFilePath = allLocalPath + currentTime + fileSuffix;
                    // 下载网络图片到本地
                    boolean is = ImgDownload.execute(imgUrl, localFilePath);
                    if (is) {
                        // 判断图片的分辨率是否小于400*200
                        boolean checked = ImageCompression.checkImgResolution(localFilePath, 400, 200);
                        if (checked) {
                            // 压缩图片400x400
                            String localFilePath400x400 = allLocalPath + currentTime + ".400x400" + fileSuffix;

                            boolean is400 = ImageCompression.reduceImgByWidth(400, localFilePath, localFilePath400x400);
                            // 压缩图片60x60
                            String localFilePath60x60 = allLocalPath + currentTime + ".60x60" + fileSuffix;
                            boolean is60 = ImageCompression.reduceImgByWidth(60, localFilePath, localFilePath60x60);
                            if (is60 && is400) {
                                if (localFilePath60x60.contains(SHOP_GOODS_LOCAL_PATH_K)) {
                                    newImgUrl += ";" + localFilePath60x60.replace(SHOP_GOODS_LOCAL_PATH_K, SHOP_GOODS_SHOW_URL_K);
                                } else {
                                    newImgUrl += ";" + localFilePath60x60.replace(SHOP_GOODS_LOCAL_PATH_J, SHOP_GOODS_SHOW_URL_J);
                                }

                                json.setOk(true);
                                json.setMessage("本地图片上传成功");
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

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveEditGoods")
    @ResponseBody
    public JsonResult saveEditGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        String pidStr = request.getParameter("pid");
        CustomGoodsPublish cgp = new CustomGoodsPublish();
        if (!(pidStr == null || "".equals(pidStr))) {
            cgp.setPid(pidStr);
        } else {
            json.setOk(false);
            json.setMessage("获取pid失败");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        try {
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                json.setOk(false);
                json.setMessage("获取登录信息失败，请登录");
                return json;
            }

            String contentStr = request.getParameter("content");

            // 获取商品信息
            CustomGoodsPublish orGoods = shopUrlService.queryGoodsInfo(shopId, pidStr);
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
            String weightStr = request.getParameter("weight");
            if (!(weightStr == null || "".equals(weightStr))) {
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
            }
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
                cgp.setEninfo(eninfo);
            } else {
                json.setOk(false);
                json.setMessage("获取商品详情失败");
                return json;
            }

            String rangePrice = request.getParameter("rangePrice");

            if (rangePrice == null || "".equals(rangePrice)) {
                String wprice = request.getParameter("wprice");
                if (wprice == null || "".equals(wprice)) {
                    // 判断wprice是不是空的，如果是，不更新wprice和price值
                    if (orGoods.getWprice() == null || "".equals(orGoods.getWprice())
                            || orGoods.getWprice().trim().length() < 3) {
                        cgp.setPrice(orGoods.getPrice());
                        cgp.setWprice("[]");
                    } else {
                        json.setOk(false);
                        json.setMessage("获取区间价格数据失败");
                        return json;
                    }
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
                    json.setMessage("获取单规格价数据失败");
                    return json;
                } else {
                    List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.parseArray(orGoods.getSku(), ImportExSku.class);
                    boolean isSuccess = dealSkuByParam(skuList, sku, cgp);
                    if (!isSuccess) {
                        json.setOk(false);
                        json.setMessage("商品单规格价格生成异常，请确认价格！");
                        return json;
                    }
                }
            }
            boolean success = shopUrlService.saveEditGoods(cgp, shopId, user.getId());
            if (success) {
                if (weightStr.equals(orFinalWeight)) {
                    json.setOk(true);
                    json.setMessage("更新成功");
                } else {
                    String url = SHOP_GOODS_WEIGHT_CLEARURL + "pid=" + cgp.getPid() + "&finalWeight=" + weightStr
                            + "&sourceTable=shop_goods_ready&database=28";
                    String resultJson = DownloadMain.getContentClient(url, null);
                    System.err.println("pid=" + cgp.getPid() + ",result:[" + resultJson + "]");
                    JSONObject jsonJt = JSONObject.parseObject(resultJson);
                    System.out.println(json.toString());
                    if (!jsonJt.getBoolean("ok")) {
                        json.setOk(false);
                        json.setMessage("清洗价格失败：" + jsonJt.getString("message"));
                    } else {
                        json.setOk(true);
                        json.setMessage("更新成功");
                    }
                }
            } else {
                json.setOk(false);
                json.setMessage("更新失败，请重试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + ",pid:" + pidStr + ",保存错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",pid:" + pidStr + ",保存错误，原因：", e);
        }
        return json;
    }

    @RequestMapping(value = "/publishEditGoods")
    @ResponseBody
    public JsonResult publishEditGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        try {

            Map<String, Integer> rsMap = shopUrlService.queryDealState(shopId);
            if (rsMap == null || rsMap.size() == 0) {
                json.setOk(false);
                json.setMessage("获取店铺清洗状态失败，请等待");
            } else {
                if (rsMap.get("shop_state") > 0) {
                    if (rsMap.get("shop_state") == 1) {
                        json.setOk(false);
                        json.setMessage("店铺数据正在清洗，请等待");
                        return json;
                    } else if (rsMap.get("shop_state") == 2) {
                        if (rsMap.get("online_state") == 1) {
                            // 判断更新时间，超过两天的就放行
                            String updateTime = null;
                            for (Entry<String, Integer> stateMap : rsMap.entrySet()) {
                                if ("shop_state".equals(stateMap.getKey()) || "online_state".equals(stateMap.getKey())) {
                                    continue;
                                } else {
                                    updateTime = stateMap.getKey();
                                }
                            }
                            if (StringUtils.isNotBlank(updateTime)) {
                                LocalDateTime today = LocalDateTime.now().minusDays(2);
                                int subLength = "2019-09-30 15:35:30".length();
                                if(updateTime.length() > subLength){
                                    updateTime = updateTime.substring(0,subLength);
                                }
                                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                LocalDateTime upTime = LocalDateTime.parse(updateTime, df);
                                if (upTime.isAfter(today)) {
                                    json.setOk(false);
                                    json.setMessage("店铺数据正在上线，拒绝执行操作！");
                                    return json;
                                }
                            } else {
                                json.setOk(false);
                                json.setMessage("店铺数据正在上线，拒绝执行操作！");
                                return json;
                            }

                        } else if (rsMap.get("online_state") == 2) {
                            json.setOk(false);
                            json.setMessage("店铺数据已经上线，拒绝执行操作！");
                            return json;
                        }
                    }
                }
            }

            /*
             * //检查店铺已经在线商品，应该对他们进行更新和重新上线，如果其中有产品是人为编辑过的，请在上线时提醒
             * List<CustomGoodsPublish> gdList =
             * shopUrlService.queryOnlineGoodsByShopId(shopId); //店铺所有清洗商品
             * List<ShopGoodsInfo> gdRdList =
             * shopUrlService.queryDealGoodsByShopId(shopId); Map<String,String>
             * syncPidsMap = new HashMap<String, String>(); for(ShopGoodsInfo
             * gdRd : gdRdList){ if(gdRd.getSyncFlag() == 0){
             * syncPidsMap.put(gdRd.getPid(), gdRd.getPid()); } } List<String>
             * pids = new ArrayList<String>(); if(gdList.size() > 0){
             * for(CustomGoodsPublish onlineGd : gdList){
             * if(!"0".equals(onlineGd.getIsEdited()) &&
             * syncPidsMap.containsKey(onlineGd.getPid())){
             * pids.add(onlineGd.getPid()); } } } if(pids.size() > 0){ int size
             * = pids.size(); json.setTotal(Long.valueOf(size));
             * json.setData(pids.toString()); }else{ // 异步调用商品上传图片和同步数据的方法
             * Thread thread = new Thread(new ShopGoodsSyncThread(shopId));
             * thread.start(); } gdRdList.clear(); gdList.clear();
             * syncPidsMap.clear();
             */

            // 给错误商品使用的，直接更新待上线状态
            shopUrlService.setShopGoodsFailureGoodsToReady(shopId);

            // 异步调用商品上传图片和同步数据的方法
            Thread thread = new Thread(new ShopGoodsSyncThread(shopId));
            thread.start();
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + ",发布错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",发布错误，原因：", e);
        }
        return json;
    }

    @RequestMapping(value = "/publishCheckEditGoods")
    @ResponseBody
    public JsonResult publishCheckEditGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        try {

            Map<String, Integer> rsMap = shopUrlService.queryDealState(shopId);
            if (rsMap == null || rsMap.size() == 0) {
                json.setOk(false);
                json.setMessage("获取店铺清洗状态失败，请等待");
            } else {
                if (rsMap.get("shop_state") > 0) {
                    if (rsMap.get("shop_state") == 1) {
                        json.setOk(false);
                        json.setMessage("店铺数据正在清洗，请等待");
                        return json;
                    } else if (rsMap.get("shop_state") == 2) {
                        if (rsMap.get("online_state") == 1) {
                            json.setOk(false);
                            json.setMessage("店铺数据正在上线，拒绝执行操作！");
                            return json;
                        } else if (rsMap.get("online_state") == 2) {
                            json.setOk(false);
                            json.setMessage("店铺数据已经上线，拒绝执行操作！");
                            return json;
                        }
                    }
                }
            }
            // 异步调用商品上传图片和同步数据的方法
            Thread thread = new Thread(new ShopGoodsSyncThread(shopId));
            thread.start();

            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + ",发布错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",发布错误，原因：", e);
        }
        return json;
    }

    @RequestMapping(value = "/readyToOnline")
    @ResponseBody
    public JsonResult readyToOnline(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }
        try {

            Map<String, Integer> rsMap = shopUrlService.queryDealState(shopId);
            if (rsMap == null || rsMap.size() == 0) {
                json.setOk(false);
                json.setMessage("获取店铺清洗状态失败，请等待");
            } else {
                if (rsMap.get("shop_state") > 0) {
                    if (rsMap.get("shop_state") == 1) {
                        json.setOk(false);
                        json.setMessage("店铺数据正在清洗，请等待");
                        return json;
                    } else if (rsMap.get("shop_state") == 2) {
                        if (rsMap.get("online_state") == 1) {
                            json.setOk(false);
                            json.setMessage("店铺数据正在上线，拒绝执行操作！");
                            return json;
                        } else if (rsMap.get("online_state") == 2) {
                            json.setOk(false);
                            json.setMessage("店铺数据已经上线，拒绝执行操作！");
                            return json;
                        }
                    }
                }
            }
            shopUrlService.updateShopState(shopId, 4);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + ",发布错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",发布错误，原因：", e);
        }
        return json;
    }

    @RequestMapping(value = "/showCatidErrorWeightGoods")
    @ResponseBody
    public ModelAndView showCatidErrorWeightGoods(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("goodsWeightErrpor");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }
        try {
            List<GoodsOfferBean> goodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);

            // 排序，重小到大
            Collections.sort(goodsInfos, new Comparator<GoodsOfferBean>() {
                public int compare(GoodsOfferBean o1, GoodsOfferBean o2) {
                    return o1.getWeightFlag() - o2.getWeightFlag();
                }
            });

            List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
            List<ShopInfoBean> infos = shopUrlService.queryInfoByShopId(shopId, "");

            // 异常列表
            Map<String, List<GoodsOfferBean>> resultMapError = new HashMap<String, List<GoodsOfferBean>>();
            // 正常列表
            Map<String, List<GoodsOfferBean>> resultMapNomal = new HashMap<String, List<GoodsOfferBean>>();
            int errorTotal = 0;
            int nomalTotal = 0;
            for (GoodsOfferBean gdOf : goodsInfos) {
                BigDecimal b = new BigDecimal(gdOf.getPrice());
                gdOf.setPrice(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                if (gdOf.getWeightFlag() > 0) {
                    errorTotal++;
                    if (gdOf.getSetWeight() == 0) {
                        // 如果有 匹配了重量关键词 就 用 该关键词对应的重量
                        // 如果没匹配的重量关键词，就直接 填入 本类别的平均重量
                        checkCatidAvgWeright(ctwtList, infos, gdOf);
                    } else {
                        BigDecimal bWe = new BigDecimal(gdOf.getSetWeight());
                        gdOf.setSetWeight(bWe.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }

                    if (resultMapError.containsKey(gdOf.getCatid())) {
                        resultMapError.get(gdOf.getCatid()).add(gdOf);
                    } else {
                        List<GoodsOfferBean> gdOfLs = new ArrayList<GoodsOfferBean>();
                        gdOfLs.add(gdOf);
                        resultMapError.put(gdOf.getCatid(), gdOfLs);
                    }
                } else {
                    nomalTotal++;
                    if (resultMapNomal.containsKey(gdOf.getCatid())) {
                        resultMapNomal.get(gdOf.getCatid()).add(gdOf);
                    } else {
                        List<GoodsOfferBean> gdOfLs = new ArrayList<GoodsOfferBean>();
                        gdOfLs.add(gdOf);
                        resultMapNomal.put(gdOf.getCatid(), gdOfLs);
                    }
                }
            }

            List<ShopErrorGoodsInfo> errorList = new ArrayList<ShopErrorGoodsInfo>();
            List<ShopErrorGoodsInfo> nomalList = new ArrayList<ShopErrorGoodsInfo>();

            // 类别重量数据
            List<ShopInfoBean> shopInfos = shopUrlService.queryInfoByShopId(shopId, "");
            for (Entry<String, List<GoodsOfferBean>> errMap : resultMapError.entrySet()) {
                ShopErrorGoodsInfo shErGd = new ShopErrorGoodsInfo();
                shErGd.setCategoryId(errMap.getKey());
                shErGd.setCategoryName(genCategoryName(errMap.getKey()));
                shErGd.setWeightVal(queryWeightVal(shopInfos, errMap.getKey()));
                shErGd.setGdOfLs(errMap.getValue());
                shErGd.setTotalNum(errMap.getValue().size());
                errorList.add(shErGd);
            }

            for (Entry<String, List<GoodsOfferBean>> nomMap : resultMapNomal.entrySet()) {
                ShopErrorGoodsInfo shErGd = new ShopErrorGoodsInfo();
                shErGd.setCategoryId(nomMap.getKey());
                shErGd.setCategoryName(genCategoryName(nomMap.getKey()));
                shErGd.setWeightVal(queryWeightVal(shopInfos, nomMap.getKey()));
                shErGd.setGdOfLs(nomMap.getValue());
                shErGd.setTotalNum(nomMap.getValue().size());
                nomalList.add(shErGd);
            }

            resultMapError.clear();
            resultMapNomal.clear();
            shopInfos.clear();
            goodsInfos.clear();

            mv.addObject("errorList", errorList);
            mv.addObject("nomalList", nomalList);
            mv.addObject("errorTotal", errorTotal);
            mv.addObject("nomalTotal", nomalTotal);
            mv.addObject("show", 1);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("msgStr", "展示重量异常商品错误，原因：" + e.getMessage());
            mv.addObject("show", 0);
            LOG.error("shopId:" + shopId + ",展示重量异常商品错误，原因：", e);
        }
        return mv;
    }

    private void checkCatidAvgWeright(List<ShopCatidWeight> ctwtList, List<ShopInfoBean> infos, GoodsOfferBean gdOf) {

        boolean noKeyWeigth = true;
        for (ShopCatidWeight cdWt : ctwtList) {
            if (cdWt.getCatid().equals(gdOf.getCatid())) {
                if (gdOf.getGoodsName().contains(cdWt.getKeyword())) {
                    BigDecimal bWe = new BigDecimal(cdWt.getAvgWeight());
                    gdOf.setSetWeight(bWe.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    noKeyWeigth = false;
                    break;
                }
            }
        }
        if (noKeyWeigth) {
            for (ShopInfoBean spIf : infos) {
                if (spIf.getCategoryId().equals(gdOf.getCatid())) {
                    BigDecimal bWe = new BigDecimal(spIf.getWeightVal());
                    gdOf.setSetWeight(bWe.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    break;
                }
            }
        }
    }

    @RequestMapping(value = "/saveKeyWordWeight")
    @ResponseBody
    public JsonResult saveKeyWordWeight(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }
        String catid = request.getParameter("catid");
        if (catid == null || "".equals(catid)) {
            json.setOk(false);
            json.setMessage("获取类别ID失败");
            return json;
        }
        String avgWeight = request.getParameter("avgWeight");
        if (avgWeight == null || "".equals(avgWeight)) {
            json.setOk(false);
            json.setMessage("获取重量数据失败");
            return json;
        }
        String keyword = request.getParameter("keyword");
        if (keyword == null || "".equals(keyword)) {
            json.setOk(false);
            json.setMessage("获取关键词数据失败");
            return json;
        }
        try {

            List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);

            List<ShopCatidWeight> insertCidWtList = new ArrayList<ShopCatidWeight>();
            List<ShopCatidWeight> updateCidWtList = new ArrayList<ShopCatidWeight>();

            ShopCatidWeight newCtwt = new ShopCatidWeight();
            newCtwt.setAvgWeight(Double.valueOf(avgWeight));
            newCtwt.setCatid(catid);
            newCtwt.setKeyword(keyword);
            newCtwt.setShopId(shopId);
            newCtwt.setAdminId(user.getId());
            boolean isInsert = true;
            for (ShopCatidWeight oldCtwt : ctwtList) {
                if (oldCtwt.getCatid().equals(catid) && oldCtwt.getKeyword().equals(keyword)) {
                    newCtwt.setId(oldCtwt.getId());
                    updateCidWtList.add(newCtwt);
                    isInsert = false;
                    break;
                }
            }
            if (isInsert) {
                insertCidWtList.add(newCtwt);
            }
            if (insertCidWtList.size() > 0) {
                shopUrlService.batchInsertCatidWeight(insertCidWtList);
            }
            if (updateCidWtList.size() > 0) {
                shopUrlService.batchUpdateCatidWeight(updateCidWtList);
            }

            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + ",发布错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",发布错误，原因：", e);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveDealErrorShopGoods")
    @ResponseBody
    public JsonResult saveDealErrorShopGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }
        String infos = request.getParameter("infos");
        if (infos == null && "".equals(infos)) {
            json.setOk(false);
            json.setMessage("获取参数失败");
            return json;
        }

        try {
            List<GoodsOfferBean> goodsErrInfos = JSONArray.parseArray(infos, GoodsOfferBean.class);
            if (goodsErrInfos == null || goodsErrInfos.size() == 0) {
                json.setOk(false);
                json.setMessage("生成数据失败，无法保存，请重试");
                return json;
            } else {
                for (GoodsOfferBean gdOf : goodsErrInfos) {
                    gdOf.setShopId(shopId);
                }
            }
            boolean isSuccess = shopUrlService.batchUpdateErrorWeightGoods(goodsErrInfos);
            if (isSuccess) {
                /**
                 * 下面两种产品不需要被列出 A.对于 重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品，保持 原抓取的
                 * 重量数字，不用做任何操作。 B.标记了 “特例标记”的产品，不列出也不做任何修改
                 */

                // 获取店铺类别关键词重量
                List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
                // 生成店铺类别关键词重量map数据
                Map<String, ShopCatidWeight> ctwtMap = new HashMap<String, ShopCatidWeight>();
                for (ShopCatidWeight ctWg : ctwtList) {
                    ctwtMap.put(ctWg.getCatid(), ctWg);
                }
                List<GoodsOfferBean> orGoodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
                List<GoodsOfferBean> resultInfos = new ArrayList<GoodsOfferBean>();
                List<GoodsOfferBean> updateGoodsInfos = new ArrayList<GoodsOfferBean>();
                for (GoodsOfferBean orGd : orGoodsInfos) {
                    // 屏蔽正常 0和特例 1的商品
                    if (orGd.getWeightFlag() <= 2) {
                        // orGd.setWeightDeal(1);
                        // updateGoodsInfos.add(orGd);
                    } else {
                        // 屏蔽 重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品
                        if (ctwtMap.size() == 0) {
                            resultInfos.add(orGd);
                        } else {
                            boolean isSuit = ctwtMap.containsKey(orGd.getCatid());
                            isSuit = isSuit && orGd.getGoodsName().contains(ctwtMap.get(orGd.getCatid()).getKeyword());
                            isSuit = isSuit && Math.abs(ctwtMap.get(orGd.getCatid()).getAvgWeight() - orGd.getWeight())
                                    / ctwtMap.get(orGd.getCatid()).getAvgWeight() <= 0.3;
                            if (isSuit) {
                                orGd.setWeightFlag(2);
                                // orGd.setWeightDeal(1);
                                updateGoodsInfos.add(orGd);
                            } else {
                                resultInfos.add(orGd);
                            }
                        }
                    }
                }
                // 更新重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品处理标识
                if (updateGoodsInfos.size() > 0) {
                    boolean isUpdate = shopUrlService.batchUpdateErrorWeightGoods(updateGoodsInfos);
                    if (isUpdate) {
                        json.setOk(true);
                    } else {
                        json.setOk(false);
                        json.setMessage("更新商品处理标识失败，请重试");
                        return json;
                    }
                    updateGoodsInfos.clear();
                }

                goodsErrInfos.clear();
                orGoodsInfos.clear();
                resultInfos.clear();
            } else {
                json.setOk(false);
                json.setMessage("更新商品处理数据失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",保存展示重量商品错误，原因：", e);
        }
        return json;
    }

    @RequestMapping(value = "/showHasDealShopGoods")
    public ModelAndView showHasDealShopGoods(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("goodsWeightDealShow");
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }

        try {

            /**
             * 下面两种产品不需要被列出 A.对于 重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品，保持 原抓取的
             * 重量数字，不用做任何操作。 B.标记了 “特例标记”的产品，不列出也不做任何修改
             */

            // 获取店铺类别关键词重量
            List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
            // 生成店铺类别关键词重量map数据
            Map<String, ShopCatidWeight> ctwtMap = new HashMap<String, ShopCatidWeight>();
            for (ShopCatidWeight ctWg : ctwtList) {
                ctwtMap.put(ctWg.getCatid(), ctWg);
            }
            List<GoodsOfferBean> orGoodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
            List<GoodsOfferBean> resultInfos = new ArrayList<GoodsOfferBean>();
            List<GoodsOfferBean> updateGoodsInfos = new ArrayList<GoodsOfferBean>();
            for (GoodsOfferBean orGd : orGoodsInfos) {
                // 屏蔽正常 0和特例 1的商品
                if (orGd.getWeightFlag() <= 2) {
                    orGd.setWeightDeal(1);
                    // updateGoodsInfos.add(orGd);
                } else {
                    // 屏蔽 重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品
                    if (ctwtMap.size() == 0) {
                        resultInfos.add(orGd);
                    } else {
                        // A.对于 重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品，保持 原抓取的
                        boolean isSuit = ctwtMap.containsKey(orGd.getCatid());
                        isSuit = isSuit && orGd.getGoodsName().contains(ctwtMap.get(orGd.getCatid()).getKeyword());
                        isSuit = isSuit && Math.abs(ctwtMap.get(orGd.getCatid()).getAvgWeight() - orGd.getWeight())
                                / ctwtMap.get(orGd.getCatid()).getAvgWeight() <= 0.3;
                        if (isSuit) {
                            orGd.setWeightFlag(2);
                            // orGd.setWeightDeal(1);
                            updateGoodsInfos.add(orGd);
                        } else {
                            resultInfos.add(orGd);
                        }
                    }
                }
            }
            // 更新重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品处理标识
            if (updateGoodsInfos.size() > 0) {
                shopUrlService.batchUpdateErrorWeightGoods(updateGoodsInfos);
                updateGoodsInfos.clear();
            }
            orGoodsInfos.clear();

            // 异常列表
            Map<String, List<GoodsOfferBean>> resultMapError = new HashMap<String, List<GoodsOfferBean>>();
            int errorTotal = 0;
            for (GoodsOfferBean gdOf : resultInfos) {
                BigDecimal b = new BigDecimal(gdOf.getPrice());
                gdOf.setPrice(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                errorTotal++;
                if (resultMapError.containsKey(gdOf.getCatid())) {
                    resultMapError.get(gdOf.getCatid()).add(gdOf);
                } else {
                    List<GoodsOfferBean> gdOfLs = new ArrayList<GoodsOfferBean>();
                    gdOfLs.add(gdOf);
                    resultMapError.put(gdOf.getCatid(), gdOfLs);
                }
            }

            List<ShopErrorGoodsInfo> errorList = new ArrayList<ShopErrorGoodsInfo>();

            // 类别重量数据
            List<ShopInfoBean> shopInfos = shopUrlService.queryInfoByShopId(shopId, "");
            for (Entry<String, List<GoodsOfferBean>> errMap : resultMapError.entrySet()) {
                ShopErrorGoodsInfo shErGd = new ShopErrorGoodsInfo();
                shErGd.setCategoryId(errMap.getKey());
                shErGd.setCategoryName(genCategoryName(errMap.getKey()));
                shErGd.setWeightVal(queryWeightVal(shopInfos, errMap.getKey()));
                shErGd.setGdOfLs(errMap.getValue());
                shErGd.setTotalNum(errMap.getValue().size());
                errorList.add(shErGd);
            }

            resultInfos.clear();
            resultMapError.clear();
            shopInfos.clear();

            mv.addObject("errorList", errorList);
            mv.addObject("errorTotal", errorTotal);
            mv.addObject("show", 1);

        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("msgStr", "执行错误，原因：" + e.getMessage());
            mv.addObject("show", 0);
            LOG.error("shopId:" + shopId + ",保存展示重量商品错误，原因：", e);
        }
        return mv;
    }

    @RequestMapping(value = "/checkShowDealShopGoodsAndClear")
    @ResponseBody
    public JsonResult checkShowDealShopGoodsAndClear(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }

        try {

            // 校检类别的平均重量，存在0的数据说明没有录入，需要给出提醒
            List<ShopInfoBean> catidInfos = shopUrlService.queryInfoByShopId(shopId, "");
            boolean isZone = false;
            String catid = "";
            for (ShopInfoBean catInfo : catidInfos) {
                if (catInfo.getWeightVal() == 0) {
                    isZone = true;
                    catid = catInfo.getCategoryId();
                    break;
                }
            }
            if (isZone) {
                json.setOk(false);
                json.setMessage("类别：" + catid + ",存在平均重量没有设定的情况，请设置");
                return json;
            }

            List<GoodsOfferBean> orGoodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
            List<GoodsOfferBean> updateGoodsInfos = new ArrayList<GoodsOfferBean>();
            for (GoodsOfferBean orGd : orGoodsInfos) {
                orGd.setWeightDeal(1);
                updateGoodsInfos.add(orGd);
            }
            // 更新重量在 “类别+关键词”平均重量 正负 30%的以内的所有商品处理标识
            if (updateGoodsInfos.size() > 0) {
                boolean isUpdate = shopUrlService.batchUpdateErrorWeightGoods(updateGoodsInfos);
                if (isUpdate) {
                    json.setOk(true);
                } else {
                    json.setOk(false);
                    json.setMessage("更新商品处理标识失败，请重试");
                    return json;
                }
                updateGoodsInfos.clear();
            }
            orGoodsInfos.clear();

            Thread thread = new Thread(new ShopGoodsDealThread(shopId));
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",保存展示重量商品错误，原因：", e);
        }
        return json;
    }


    @RequestMapping(value = "/deleteCatidGoods")
    @ResponseBody
    public JsonResult deleteCatidGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }
        String catids = request.getParameter("catids");
        if (StringUtils.isBlank(catids)) {
            json.setOk(false);
            json.setMessage("获取店铺类别ID集合失败");
            return json;
        }

        try {
            boolean isDelete = shopUrlService.deleteCatidGoods(shopId, catids);
            if (isDelete) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("删除商品失败，请重试");
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行错误，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + ",执行错误，原因：", e);
        }
        return json;
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


    @RequestMapping(value = "/saveReadyDeleteShop.do")
    @ResponseBody
    public JsonResult saveReadyDeleteShop(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }

        String type = request.getParameter("type");
        if (StringUtils.isBlank(type)) {
            json.setOk(false);
            json.setMessage("获取问题类型失败");
            return json;
        }

        String remark = request.getParameter("remark");
        if (StringUtils.isBlank(remark)) {
            remark = "";
        }

        try {
            shopUrlService.saveReadyDeleteShop(shopId, Integer.valueOf(type), remark);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + "，更新问题店铺标识失败，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + "，更新问题店铺标识失败，原因：", e);
        }
        return json;
    }


    @RequestMapping(value = "/setShopType.do")
    @ResponseBody
    public JsonResult setShopType(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }

        String type = request.getParameter("type");
        if (StringUtils.isBlank(type)) {
            json.setOk(false);
            json.setMessage("获取类型失败");
            return json;
        }

        try {
            shopUrlService.setShopType(shopId, Integer.valueOf(type));
            LOG.info("shopId:" + shopId + "，更新店铺类型:" + type + ",操作人：" + user.getId() + "@" + user.getAdmName());
            json.setOk(true);

            if ("1".equals(type)) {
                // 批量更新店铺商品的打分数据
                List<String> pidList = customGoodsService.queryPidByShopId(shopId);
                if (pidList != null && pidList.size() > 0) {
                    GoodsInfoUpdateOnlineUtil.batchSetGoodsShopScoreLocal(shopId, pidList, 2,
                            1, 1);
                    pidList.clear();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + "，更新店铺类型失败，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + "，更新店铺类型失败，原因：", e);
        }
        return json;
    }


    @RequestMapping(value = "/setAuthorizedFlag.do")
    @ResponseBody
    public JsonResult setAuthorizedFlag(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }

        String authorizedFlag = request.getParameter("authorizedFlag");
        if (StringUtils.isBlank(authorizedFlag) && "-1|0|1|2".contains(authorizedFlag)) {
            json.setOk(false);
            json.setMessage("获取authorizedFlag失败");
            return json;
        }

        try {
            shopUrlService.setAuthorizedFlag(shopId, Integer.parseInt(authorizedFlag));
            LOG.info("shopId:" + shopId + "，更新店铺已授权,操作人：" + user.getId() + "@" + user.getAdmName());
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + "，更新店铺已授权失败，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + "，更新店铺已授权失败，原因：", e);
        }
        return json;
    }


    @RequestMapping(value = "/setShopTranslate.do")
    @ResponseBody
    public JsonResult setShopTranslate(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }


        try {
            shopUrlService.setShopTranslate(shopId);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + "，更新标识失败，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + "，更新标识失败，原因：", e);
        }
        return json;
    }

    @RequestMapping(value = "/reDownShopGoods.do")
    @ResponseBody
    public JsonResult reDownShopGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String userJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取shopId失败");
            return json;
        }

        String memberId = request.getParameter("memberId");
        if(StringUtils.isBlank(memberId)){
            memberId = "";
        }
        try {
            shopUrlService.reDownShopGoods(shopId, user, memberId);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("shopId:" + shopId + "，设置重新下载失败，原因：" + e.getMessage());
            LOG.error("shopId:" + shopId + "，设置重新下载失败，原因：", e);
        }
        return json;
    }


    @RequestMapping("/shopBrandAuthorizationList")
    public ModelAndView shopBrandAuthorizationList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("shopBrandAuthorizationList");
        if (!UserInfoUtils.checkIsLogin(request)) {
            mv.addObject("msgStr", "用户未登录");
            mv.addObject("show", 0);
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (shopId == null || "".equals(shopId)) {
            mv.addObject("msgStr", "获取shopId失败");
            mv.addObject("show", 0);
            return mv;
        } else {
            mv.addObject("shopId", shopId);
        }

        try {
            List<ShopBrandAuthorization> brandAuthorizationList = shopUrlService.queryBrandAuthorizationByShopId(shopId);
            mv.addObject("brandAuthorizationList", brandAuthorizationList);
            mv.addObject("brandsNum", brandAuthorizationList.size());
            mv.addObject("show", 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopBrandAuthorizationList error:" + e.getMessage());
            LOG.error("shopBrandAuthorizationList error:", e);
            mv.addObject("msgStr", "执行过程失败，原因：" + e.getMessage());
            mv.addObject("show", 0);
        }
        return mv;
    }


    @RequestMapping(value = "/uploadBrandFile", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult uploadBrandFile(@RequestParam(value = "file", required = true) CommonsMultipartFile[] files,
                                      HttpServletRequest request) {
        JsonResult json = new JsonResult();

        String today = DateFormatUtil.formatDateToStringByYear(new Date());
        String imgUrl = "";
        try {
            Random random = new Random();
            // 获取配置文件信息
            FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
            GetConfigureInfo.checkFtpConfig(ftpConfig, json);
            String localDiskPath = ftpConfig.getLocalDiskPath();
            List<String[]> list = new ArrayList<>();
            int count = 0;
            if (json.isOk()) {
                for (CommonsMultipartFile mf : files) {
                    if (!mf.isEmpty()) {
                        count ++;
                        /*// 得到文件保存的名称mf.getOriginalFilename()
                String originalName = mf.getOriginalFilename();
                // 文件的后缀取出来
                String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
                String saveFilename = EditorController.makeFileName(String.valueOf(random.nextInt(1000)));
                // 本地服务器磁盘全路径
                String localFilePath = "shopbrand/" + saveFilename + fileSuffix;
                // 文件流输出到本地服务器指定路径
                ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
                // 检查图片分辨率
                imgUrl = ftpConfig.getLocalShowPath() + localFilePath;*/

                        String[] imgInfo = SearchFileUtils.comFileUpload(mf, "AuthorizedFile", null, null, null, 0);
                        if(imgInfo != null){
                            list.add(imgInfo);
                        }
                    }
                }
                json.setData(list);
                if(count == list.size()){
                    json.setMessage("全部成功");
                }else{
                    json.setMessage("部分成功");
                }
            }
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("uploadBrandFile error:" + e.getMessage());
            e.printStackTrace();
            LOG.error("uploadBrandFile error：", e);
        }
        return json;
    }


    @RequestMapping(value = "/saveBrandInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult saveBrandInfo(@RequestParam(value = "file", required = false) CommonsMultipartFile mf,
                                    HttpServletRequest request) {
        JsonResult json = new JsonResult();

        // 获取配置文件信息
        if (ftpConfig == null) {
            ftpConfig = GetConfigureInfo.getFtpConfig();
        }
        String shopId = request.getParameter("shopId");
        String brandIdStr = request.getParameter("brandId");
        String brandName = request.getParameter("brandName");
        String inAuthorizeState = request.getParameter("inAuthorizeState");
        String termOfValidity = request.getParameter("termOfValidity");
        String certificateFile = request.getParameter("certificateFile");
        String remotePath = request.getParameter("remotePath");
        String localPath = request.getParameter("localPath");
        if (StringUtils.isBlank(shopId) || StringUtils.isBlank(brandName)
                || StringUtils.isBlank(inAuthorizeState)) {
            json.setOk(false);
            json.setMessage("请输参数不完整");
            return json;
        } else if ("1".equals(inAuthorizeState)) {
            if (StringUtils.isBlank(termOfValidity)) {
                json.setOk(false);
                json.setMessage("请输入有效期");
                return json;
            } else if (StringUtils.isBlank(certificateFile) || StringUtils.isBlank(remotePath) || StringUtils.isBlank(localPath)) {
                json.setOk(false);
                json.setMessage("请上传文件");
                return json;
            }
        }
        Integer brandId = null;
        if(StringUtils.isNotBlank(brandIdStr) && Integer.valueOf(brandIdStr) > 0){
            brandId = Integer.valueOf(brandIdStr);
        }
        try {
            ShopBrandAuthorization authorization = new ShopBrandAuthorization();
            authorization.setAuthorizeState(Integer.valueOf(inAuthorizeState));
            authorization.setBrandName(brandName);
            authorization.setShopId(shopId);

            authorization.setTermOfValidity(termOfValidity);
            // 上传文件
            /*if ("1".equals(inAuthorizeState) && certificateFile.contains("192.168.")) {
                String localFilePath = certificateFile.replace(ftpConfig.getLocalShowPath(), ftpConfig.getLocalDiskPath());

                String remoteShowPath = certificateFile.replace(ftpConfig.getLocalShowPath(), ftpConfig.getRemoteShowPath());
                String remoteLocalPath = certificateFile.replace(ftpConfig.getLocalShowPath(), FtpConfig.REMOTE_LOCAL_PATH);
                String destPath = remoteLocalPath.substring(0,remoteLocalPath.lastIndexOf("/"));
                // destPath = destPath.replace("/usr/local/goodsimg/importcsvimg","");
                *//*json = NewFtpUtil.uploadFileToRemoteSSM(localFilePath, destPath, ftpConfig);
                if (!json.isOk()) {
                    json = NewFtpUtil.uploadFileToRemoteSSM(localFilePath, destPath, ftpConfig);
                }*//*

                File localFile = new File(localFilePath);

                boolean isSuccess = UploadByOkHttp.uploadFile(localFile, destPath , 1);
                if (!isSuccess) {
                    isSuccess = UploadByOkHttp.uploadFile(localFile, destPath , 1);
                }
                if (isSuccess) {
                    isSuccess = UploadByOkHttp.uploadFile(localFile, destPath, 0);
                    if (!isSuccess) {
                        isSuccess = UploadByOkHttp.uploadFile(localFile, destPath, 0);
                    }
                }
                if (isSuccess) {
                    String fileName = localFilePath.substring(localFilePath.lastIndexOf("/") + 1);
                    authorization.setCertificateFile(fileName);
                    authorization.setLocalPath(localFilePath);
                    authorization.setRemotePath(remoteShowPath);
                } else {
                    json.setOk(false);
                    json.setMessage("上传文件失败，请重试");
                    return json;
                }
            }*/
            int checkCount = shopUrlService.checkBrandAuthorizationByName(shopId, brandName, brandId);
            if (checkCount > 0) {
                json.setOk(false);
                json.setMessage("已经存在此店铺品牌");
                return json;
            }
            authorization.setCertificateFile(certificateFile);
            authorization.setRemotePath(remotePath);
            authorization.setLocalPath(localPath);
            if (brandId != null) {
                authorization.setId(Integer.valueOf(brandId));
                shopUrlService.updateShopBrandAuthorization(authorization);
            } else {
                // 检查是否已经存在此品牌
                shopUrlService.insertIntoShopBrandAuthorization(authorization);
            }
            json.setOk(true);
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("saveBrandInfo error:" + e.getMessage());
            e.printStackTrace();
            LOG.error("saveBrandInfo error：", e);
        }
        return json;
    }


    @RequestMapping(value = "/deleteAuthorizedInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult deleteAuthorizedInfo(HttpServletRequest request,HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String shopId = request.getParameter("shopId");
        String brandIdStr = request.getParameter("brandId");
        if (StringUtils.isBlank(shopId) || StringUtils.isBlank(brandIdStr)) {
            json.setOk(false);
            json.setMessage("请输参数不完整");
            return json;
        }
        try {
            shopUrlService.deleteShopBrandAuthorizationById(Integer.valueOf(brandIdStr));
            json.setOk(true);
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("deleteAuthorizedInfo error:" + e.getMessage());
            e.printStackTrace();
            LOG.error("deleteAuthorizedInfo error：", e);
        }
        return json;
    }

    @RequestMapping(value = "/reUpdateShopAdminId", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult reUpdateShopAdminId(HttpServletRequest request,HttpServletResponse response) {
        JsonResult json = new JsonResult();

        com.cbt.pojo.Admuser admuser = UserInfoUtils.getUserInfo(request);
        if(admuser == null || admuser.getId() == 0){
            json.setOk(false);
            json.setMessage("请登录后重试");
            return json;
        }
        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺失败");
            return json;
        }
        try {
            shopUrlService.reUpdateShopAdminId(shopId,admuser.getId(),admuser.getAdmName());
            json.setOk(true);
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage("deleteAuthorizedInfo error:" + e.getMessage());
            e.printStackTrace();
            LOG.error("deleteAuthorizedInfo error：", e);
        }
        return json;
    }

    private float genFloatWidthTwoDecimalPlaces(float numVal) {
        BigDecimal bd = new BigDecimal(numVal);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private float queryWeightVal(List<ShopInfoBean> shopInfos, String catid) {
        float weight = 0;
        for (ShopInfoBean spIf : shopInfos) {
            if (spIf.getCategoryId().equals(catid)) {
                weight = spIf.getWeightVal();
                break;
            }
        }
        return weight;
    }

    private String genCategoryName(String categoryId) {
        String categoryName = "";
        if (category1688List == null || category1688List.size() == 0) {
            category1688List = shopUrlService.queryAll1688Category();
        }
        for (Category1688Bean ct1688 : category1688List) {
            if (ct1688.getCategoryId().equals(categoryId)) {
                categoryName = ct1688.getCategoryName();
                break;
            }
        }
        return categoryName;
    }


}