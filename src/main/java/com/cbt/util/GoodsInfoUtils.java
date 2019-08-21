package com.cbt.util;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.ImportExSku;
import com.cbt.bean.ImportExSkuShow;
import com.cbt.bean.TypeBean;
import com.cbt.parse.service.StrUtils;
import com.cbt.website.util.JsonResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GoodsInfoUtils {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsInfoUtils.class);

    public static final String SERVICE_LOCAL_IMPORT_PATH = "/usr/local/goodsimg";
    public static final String SERVICE_SHOW_IMPORT_URL_1 = "http://img.import-express.com";
    public static final String SERVICE_SHOW_IMPORT_URL_2 = "http://img1.import-express.com";
    public static final String SERVICE_SHOW_IMPORT_URL_3 = "https://img.import-express.com";
    public static final String SERVICE_SHOW_IMPORT_URL_4 = "https://img1.import-express.com";

    public static final String SERVICE_LOCAL_KIDS_PATH = "/data";
    public static final String SERVICE_SHOW_KIDS_URL_1 = "http://img.import-express.com";
    public static final String SERVICE_SHOW_KIDS_URL_2 = "http://img1.import-express.com";
    public static final String SERVICE_SHOW_KIDS_URL_3 = "https://img.import-express.com";
    public static final String SERVICE_SHOW_KIDS_URL_4 = "https://img1.import-express.com";

    private static String chineseChar = "([\\一-\\龥]+)";

    public final static String PATE = "([hH]ot\\s*[sS]ale,*!*\\:*)|"
            + "([fF][rR][eE]*\\s*[sS][hH][iI][pP]+[iI][nN][gG],*!*\\:*)|"
            + "(\\d*%*\\s*[bB]rand\\s*[nN]ew,*!*\\:*)"
            + "|([tT]op\\s*[rR]ated,*!*\\:*)|([lL]owest\\s*[pP]rice,*!*\\:*)|"
            + "(\\(*[sS]hip\\s*[fF]rom\\s*[uU][sS]\\)*,*!*\\:*)"
            + "|([hH]igh\\s*[qQ]uality,*!*\\:*)|(20\\d+,*!*\\:*)|([pP][aA][nN][dD][oO][rR][aA])";

    /**
	 * 产品单页静态化文件中的名字，
	 */
	private static final String[] goodsNameWords={"hotsale","aliexpress","free-shipping","new","shipping","HOT","fashion","Hot sale","Hot Worldwide"};

    /**
     * 产品类型:自定义上传商品
     */
    public static final String UPLOAD_PRODUCT = "D";

    /**
     * 文字尺码表转换纯数据
     */
    private static final Map<String, String> CLOSE_SIZE_MAP = new HashMap<>();

    /**
     * 替换包含转换数据
     */
    private static final Map<String, String> REPLACE_SIZE_MAP = new HashMap<>();

    static {
        CLOSE_SIZE_MAP.put("XS", "01");
        CLOSE_SIZE_MAP.put("S", "02");
        CLOSE_SIZE_MAP.put("M", "03");
        CLOSE_SIZE_MAP.put("L", "04");
        CLOSE_SIZE_MAP.put("XL", "05");
        CLOSE_SIZE_MAP.put("2L", "06");
        CLOSE_SIZE_MAP.put("XXL", "06");
        CLOSE_SIZE_MAP.put("3L", "07");
        CLOSE_SIZE_MAP.put("XXXL", "07");
        CLOSE_SIZE_MAP.put("4L", "08");
        CLOSE_SIZE_MAP.put("XXXXL", "08");
        CLOSE_SIZE_MAP.put("800 or more", "ZZZZ");
        REPLACE_SIZE_MAP.put("10cm", "010cm");
        REPLACE_SIZE_MAP.put("20cm", "020cm");
        REPLACE_SIZE_MAP.put("30cm", "030cm");
        REPLACE_SIZE_MAP.put("40cm", "040cm");
        REPLACE_SIZE_MAP.put("50cm", "050cm");
        REPLACE_SIZE_MAP.put("60cm", "060cm");
        REPLACE_SIZE_MAP.put("70cm", "070cm");
        REPLACE_SIZE_MAP.put("80cm", "080cm");
        REPLACE_SIZE_MAP.put("90cm", "090cm");
        REPLACE_SIZE_MAP.put("1 yards", "001 yards");
        REPLACE_SIZE_MAP.put("2 yards", "002 yards");
        REPLACE_SIZE_MAP.put("3 yards", "003 yards");
        REPLACE_SIZE_MAP.put("4 yards", "004 yards");
        REPLACE_SIZE_MAP.put("5 yards", "005 yards");
        REPLACE_SIZE_MAP.put("6 yards", "006 yards");
        REPLACE_SIZE_MAP.put("7 yards", "007 yards");
        REPLACE_SIZE_MAP.put("8 yards", "008 yards");
        REPLACE_SIZE_MAP.put("9 yards", "009 yards");
        REPLACE_SIZE_MAP.put("10 yards", "010 yards");
        REPLACE_SIZE_MAP.put("20 yards", "020 yards");
        REPLACE_SIZE_MAP.put("30 yards", "030 yards");
        REPLACE_SIZE_MAP.put("40 yards", "040 yards");
        REPLACE_SIZE_MAP.put("50 yards", "050 yards");
        REPLACE_SIZE_MAP.put("60 yards", "060 yards");
        REPLACE_SIZE_MAP.put("70 yards", "070 yards");
        REPLACE_SIZE_MAP.put("80 yards", "080 yards");
        REPLACE_SIZE_MAP.put("90 yards", "090 yards");
        REPLACE_SIZE_MAP.put("1Month", "01Month");
        REPLACE_SIZE_MAP.put("2Month", "02Month");
        REPLACE_SIZE_MAP.put("3Month", "03Month");
        REPLACE_SIZE_MAP.put("4Month", "04Month");
        REPLACE_SIZE_MAP.put("5Month", "05Month");
        REPLACE_SIZE_MAP.put("6Month", "06Month");
        REPLACE_SIZE_MAP.put("7Month", "07Month");
        REPLACE_SIZE_MAP.put("8Month", "08Month");
        REPLACE_SIZE_MAP.put("9Month", "09Month");
    }


    /**
     * 处理1688商品的规格图片数据
     *
     * @param cgbean
     * @param isRemote
     * @return
     */
    public static List<TypeBean> deal1688GoodsType(CustomGoodsPublish cgbean, boolean isRemote) {// 规格
        List<TypeBean> typeList = new ArrayList<TypeBean>();
        if (!(cgbean.getEntype() == null || "".equals(cgbean.getEntype()))) {
            Map<String, List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
            String types = cgbean.getEntype();
            String remotPath = cgbean.getRemotpath();
            // String localPath = cgbean.getLocalpath();
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
                            tem = StringUtils.isBlank(tem) || StringUtils.equals(tem, "null") ? "" : (isRemote ? remotPath + tem : tem);
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
                Iterator<Map.Entry<String, List<TypeBean>>> iterator = typeMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    typeList.addAll(iterator.next().getValue());
                }

            }
        }

        return typeList;

    }

    // 处理1688商品的规格图片数据
    public static List<String> deal1688GoodsImg(String img, String remotPath) {

        List<String> imgList = new ArrayList<String>();

        if (StringUtils.isNotBlank(img)) {
            img = img.replace("[", "").replace("]", "").trim();
            String[] imgs = img.split(",\\s*");

            for (int i = 0; i < imgs.length; i++) {
                if (!imgs[i].isEmpty()) {
                    if (imgs[i].indexOf("http://") > -1 || imgs[i].indexOf("https://") > -1) {
                        imgList.add(imgs[i]);
                    } else {
                        imgList.add(remotPath + imgs[i]);
                    }
                }
            }
        }
        return imgList;
    }

    public static HashMap<String, String> deal1688Sku(CustomGoodsPublish cgbean) {
        // detail明细
        HashMap<String, String> pInfo = new HashMap<String, String>();
        String detail = cgbean.getEndetail() == null ? "" : cgbean.getEndetail();
        if (StringUtils.isNotBlank(detail)) {
            String[] details = detail.substring(1, detail.length() - 1).split(",");
            int details_length = details.length;
            for (int i = 0; i < details_length; i++) {
                String str_detail = details[i].trim().replaceAll(chineseChar, "");
                if (str_detail.isEmpty() || com.cbt.parse.service.StrUtils.isMatch(str_detail.substring(0, 1), "\\d+")) {
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
    public static List<ImportExSkuShow> combineSkuList(List<TypeBean> typeList, List<ImportExSku> skuList) {

        List<ImportExSkuShow> cbSkuLst = new ArrayList<ImportExSkuShow>();

        for (ImportExSku ites : skuList) {
            String skuAttrs = "";
            String enType = "";
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
                        if (CLOSE_SIZE_MAP.containsKey(tyb.getValue().toUpperCase())) {
                            enType += CLOSE_SIZE_MAP.get(tyb.getValue().toUpperCase()) + ",";
                        } else {
                            enType += genNewTypeVal(tyb.getValue()) + ",";
                        }
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
            ipes.setFianlWeight(ites.getFianlWeight());
            ipes.setSpecId(ites.getSpecId());
            if (ites.getVolumeWeight() > 0) {
                ipes.setVolumeWeight(ites.getVolumeWeight());
            } else {
                ipes.setVolumeWeight(ites.getFianlWeight());
            }
            if (skuAttrs == null || "".equals(skuAttrs)) {
                ipes = null;
            } else {
                if (StringUtils.isNotBlank(enType)) {
                    ipes.setEnType(enType);
                }
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


    private static String genNewTypeVal(String typeVal) {
        String tempVal = typeVal;
        for (String mapKey : REPLACE_SIZE_MAP.keySet()) {
            if (typeVal.contains(mapKey)) {
                tempVal = typeVal.replace(mapKey, REPLACE_SIZE_MAP.get(mapKey));
                break;
            }else if(typeVal.contains(mapKey.toUpperCase())){
                tempVal = typeVal.replace(mapKey.toUpperCase(), REPLACE_SIZE_MAP.get(mapKey).toUpperCase());
                break;
            }
        }
        return tempVal;
    }

    public static String changeRemotePathToLocal(String remotepath, int isKids) {

        String localPathByRemote = remotepath;
        if (remotepath.contains(SERVICE_SHOW_IMPORT_URL_1)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_IMPORT_URL_1, SERVICE_LOCAL_IMPORT_PATH);
        } else if (remotepath.contains(SERVICE_SHOW_IMPORT_URL_2)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_IMPORT_URL_2, SERVICE_LOCAL_IMPORT_PATH);
        } else if (remotepath.contains(SERVICE_SHOW_IMPORT_URL_3)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_IMPORT_URL_3, SERVICE_LOCAL_IMPORT_PATH);
        } else if (remotepath.contains(SERVICE_SHOW_IMPORT_URL_4)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_IMPORT_URL_4, SERVICE_LOCAL_IMPORT_PATH);
        }
        if(isKids > 0){
            localPathByRemote = localPathByRemote.replace(SERVICE_LOCAL_IMPORT_PATH, SERVICE_LOCAL_KIDS_PATH);
        }
        return localPathByRemote;
    }


    public static boolean uploadFileToRemoteSSM(String pid, String remoteSavePath, String localImgPath, FtpConfig ftpConfig) {
        boolean isSc = false;
        JsonResult json = new JsonResult();
        json.setOk(false);
        // 重试5次
        int count = 0;
        while (!(json.isOk() || count > 5)) {
            count++;
            json = NewFtpUtil.uploadFileToRemoteSSM(remoteSavePath, localImgPath, ftpConfig);
            if (json.isOk()) {
                isSc = true;
                break;
            } else {
                isSc = false;
            }
        }
        if (!isSc) {
            System.err.println("this pid:" + pid + "," + localImgPath + " upload error,"
                    + json.getMessage());
            LOG.error("this pid:" + pid + "," + localImgPath + " upload error,"
                    + json.getMessage());
        }
        return isSc;
    }


    public static String getProductImgUrl(String entypes, String good_types, String remotPath, String imgs) {
        String img_url = null;
        //解析出规格图片
        ArrayList<String> typeids = new ArrayList<String>(3);
        if (StringUtils.isNotBlank(good_types)) {
            String[] ids = good_types.split(",");
            for (String idstr : ids) {
                if (StringUtils.isNotBlank(idstr) && idstr.split("@").length > 1) {
                    typeids.add(idstr.split("@")[1].replaceAll("\\s*", ""));
                }
            }
        }
        String types = entypes;
        if (StringUtils.isNotBlank(types) && !StringUtils.equals(types, "[]")) {
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
                        tem = tem.replaceAll("\\s*", "");
                        typeBean.setId(tem);
                    } else if (type[j].indexOf("img=") > -1) {
                        tems = type[j].split("img=");
                        tem = tems.length > 1 ? tems[1] : "";
                        tem = tem.endsWith(".jpg") ? tem : "";
                        tem = StringUtils.isBlank(tem) || StringUtils.equals(tem, "null") ? "" : remotPath + tem;
                        typeBean.setImg(tem);
                    }
                    if (StringUtils.isNotBlank(typeBean.getId()) && StringUtils.isNotBlank(typeBean.getImg())) {
                        break;
                    }
                }
                if (typeids.contains(typeBean.getId())) {
                    img_url = typeBean.getImg();
                }
                if (StringUtils.isNotBlank(img_url)) {
                    break;
                }
            }
        }
        if (StringUtils.isBlank(img_url) && StringUtils.isNotBlank(imgs)) {
            String[] imgArr = imgs.replaceAll("\\[|\\]", "").split(",");
            if (imgArr.length > 0 && StringUtils.isNotBlank(imgArr[0])) {
                if (imgArr[0].indexOf("import-express") > -1 && imgArr[0].indexOf("http") > -1) {
                    img_url = imgArr[0];
                } else {
                    img_url = remotPath + imgArr[0];
                }
            }
        }
        return img_url;
    }


    public static String uUidToStaticUrl_cart(String uuid, String itemID, String pname, String urlHead, String catid1, String catid2) {
        try {
            if (StringUtils.isBlank(itemID) || StringUtils.isBlank(uuid) || StringUtils.isBlank(pname)) {
                return "";
            }
            uuid = uuid.toUpperCase();
            if (pname != null && StrUtils.isFind(pname, PATE)) {
                pname = pname.replaceAll(PATE, "").replaceAll("\\s+", " ").trim();
                pname = pname.replace("/", "").replace("\\", "").replace("'", " ");
                pname = java.net.URLEncoder.encode(pname, "UTF-8").replaceAll("\\+", " ");
            }
            String dataType = "1";
            if (uuid.startsWith("D")) {
                dataType = "1";
            } else if (uuid.startsWith("A")) {
                dataType = "2";
            } else if (uuid.startsWith("N")) {
                dataType = "3";
            } else if (uuid.startsWith("M")) {
                dataType = "4";
            } else if (uuid.startsWith("I")) {
                dataType = "5";
            } else if (uuid.startsWith("T")) {
                dataType = "6";
            } else if (uuid.startsWith("E")) {
                dataType = "7";
            }

            String goodsPageUrl = getGoodsPageUrl(pname, itemID, catid1, catid2);
            if (goodsPageUrl.lastIndexOf("-") > -1) {
                goodsPageUrl = goodsPageUrl.substring(0, goodsPageUrl.lastIndexOf("-")) + "-" + dataType + goodsPageUrl.substring(goodsPageUrl.lastIndexOf("-") + 1);
                if (StringUtils.isNotBlank(urlHead) && urlHead.indexOf("import-express") > -1) {
                    if (urlHead.endsWith("/")) {
                        return urlHead + "goodsinfo/" + goodsPageUrl;
                    }
                    return urlHead + "goodsinfo/" + goodsPageUrl;
                }
                return "/goodsinfo/" + goodsPageUrl;
            } else {
                return "/spider/getSpider?item=" + itemID + "&source=" + uuid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 得到产品单页静态化页面的名称
     * 规则：商品前七个单词-pid
     *
     * @param goodname
     * @param goodsid
     * @param
     * @return
     */
    public static String getGoodsPageUrl(String goodname, String goodsid, String catid1, String catid2) {
        goodname = goodname
                .toLowerCase()
                .replaceAll("[^a-zA-Z0-9]", " ")//非英文字母，非数字，非单引号，都去掉
                .trim();
        //去除静态页名字中一些不需要的词
        goodname = removeGoodsNameWords(goodname);
        String[] nameArr = goodname.split("\\s+");
        StringBuffer goodnameNew = new StringBuffer();
        if (nameArr.length > 10) {
            for (int i = 0; i < 10; i++) {
                if (StringUtils.isNotBlank(nameArr[i])) {
                    goodnameNew.append(nameArr[i] + "-");
                }
            }
        } else {
            for (int i = 0; i < nameArr.length; i++) {
                if (StringUtils.isNotBlank(nameArr[i])) {
                    goodnameNew.append(nameArr[i] + "-");
                }
            }
        }
        if (StringUtils.isNotBlank(goodnameNew.toString())) {
            String filename = goodnameNew.deleteCharAt(goodnameNew.length() - 1) + "-" + catid1 + "-" + catid2 + "-" + goodsid + ".html";
            return filename;
        } else {
            return goodsid + ".html";
        }
//       String filename="";
//       filename="A"+Md5Util.encoder(url)+"-"+goodsid+".html";
    }


    /**
     * 移除产品单页名字中一些乱七八糟的词
     *
     * @param goodsName
     * @return
     */
    public static String removeGoodsNameWords(String goodsName) {

        if (goodsName != null && !"".equals(goodsName)) {
            for (String name : goodsNameWords) {
//				goodsName=goodsName.replace(name.trim(), "");
                //替换忽略大小写
                String newName = "(?i)" + name;
                goodsName = goodsName.replaceAll(newName, "");
            }
            return goodsName;
        }
        return goodsName;
    }

    public static String genOnlineUrl(CustomGoodsPublish bean) {

        String itemid = bean.getPid();
        String dataType = "1";
        String type = "D";
        String catid1 = "0";
        String catid2 = "0";
        String pathCatid = bean.getPathCatid();

        if (StringUtils.isNotBlank(pathCatid) && pathCatid.indexOf(",") > -1) {
            String[] catidList = pathCatid.split(",");
            catid1 = catidList[0];
            catid2 = catidList[1];
        }

        String enName = bean.getEnname().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ").trim();

        //去除静态页名字中一些不需要的词
        enName = removeGoodsNameWords(enName);
        String[] nameArr = enName.split("\\s+");
        StringBuffer goodNameNew = new StringBuffer();
        if (nameArr.length > 10) {
            for (int i = 0; i < 10; i++) {
                if (StringUtils.isNotBlank(nameArr[i])) {
                    goodNameNew.append(nameArr[i] + "-");
                }
            }
        } else {
            goodNameNew.append(enName.replaceAll("(\\s+)", "-"));
        }
        String url = "";
        if (StringUtils.isNotBlank(goodNameNew.toString())) {
            if (StringUtils.isBlank(catid1) || StringUtils.isBlank(catid2)) {
                url = "https://www.import-express.com/goodsinfo/" + goodNameNew.deleteCharAt(goodNameNew.length() - 1) + "-" + dataType + itemid + ".html";
            } else {
                url = "https://www.import-express.com/goodsinfo/" + goodNameNew.deleteCharAt(goodNameNew.length() - 1) + "-" + catid1 + "-" + catid2 + "-" + dataType + itemid + ".html";
            }
        } else {
            url = "https://www.import-express.com/spider/getSpider?item=" + itemid + "&source=" + itemIDToUUID(itemid, type);
        }
        return url;
    }


    public static String itemIDToUUID(String itemId, String type) {
        if (StringUtils.isBlank(itemId)) {
            return "";
        }
        return type + Md5Util.encoder(itemId);
    }


    public static String compilePriceListStr(String str) {
        StringBuilder pricestr = new StringBuilder();
        Pattern compile = Pattern.compile("[(\\d+\\.\\d)|\\d|\\$|\\-|\\,]");
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            pricestr.append(matcher.group());
        }
        return pricestr.toString();
    }

    public static String getPriceListStrTwo(int priceListSize, String[] priceListStr) {
        int Batch_Quantity = 0;
        //初始值等于moq 就行
        double Batch_Price = 0;
        //初始值等于产品单页的价格就行
        int Batch_Quantity1 = 0;
        //初始值等于moq 就行
        double Batch_Price1 = 0;
        //初始值等于产品单页的价格就行
        int Batch_Quantity0 = 0;
        //初始值等于moq 就行
        double Batch_Price0 = 0;
        String priceListString = null;
        //JSONArray jsonArray = JSONArray.fromObject(priceListStr);
        int listSize = priceListStr.length;
        try {
            switch (listSize) {
                case 1:
                    String Batch_Quantity_Price_List0 = priceListStr[0];
                    String[] wholesalePriceTems = Batch_Quantity_Price_List0.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity0 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0] : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price0 = Double.parseDouble(wholesalePriceTems[1].toString().indexOf("-") > -1 ? wholesalePriceTems[1].toString().split("-")[0] : wholesalePriceTems[1].toString());
                    }
                    break;
                case 2:
                    Batch_Quantity_Price_List0 = priceListStr[0];
                    wholesalePriceTems = Batch_Quantity_Price_List0.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity0 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0] : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price0 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }

                    String Batch_Quantity_Price_List1 = priceListStr[1];
                    wholesalePriceTems = Batch_Quantity_Price_List1.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity1 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0] : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price1 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }
                    break;


                case 3:
                    Batch_Quantity_Price_List0 = priceListStr[0];
                    wholesalePriceTems = Batch_Quantity_Price_List0.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity0 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0] : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price0 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }

                    Batch_Quantity_Price_List1 = priceListStr[1];
                    wholesalePriceTems = Batch_Quantity_Price_List1.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity1 = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0] : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price1 = Double.parseDouble(wholesalePriceTems[1].toString());
                    }
                    String Batch_Quantity_Price_List2 = priceListStr[2];
                    wholesalePriceTems = Batch_Quantity_Price_List2.split("(\\$)");
                    if (StringUtils.isNotBlank(wholesalePriceTems[0])) {
                        Batch_Quantity = Integer.valueOf(wholesalePriceTems[0].toString().indexOf("-") > -1 ? wholesalePriceTems[0].toString().split("-")[0] : wholesalePriceTems[0].toString().replace("≥", ""));
                    }
                    if (StringUtils.isNotBlank(wholesalePriceTems[1])) {
                        Batch_Price = Double.parseDouble(wholesalePriceTems[1].toString());
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("getPriceListStrTwo error, priceListStr: " + Arrays.toString(priceListStr));
        }

//	        }
        if (Batch_Quantity0 > 0 && Batch_Price0 > 0) {
            priceListString = String.valueOf(Batch_Quantity0) + "$" + String.valueOf(Batch_Price0);
            if (Batch_Quantity1 > 0 && Batch_Price1 > 0) {
                priceListString = priceListString + String.valueOf("," + Batch_Quantity1) + "$" + String.valueOf(Batch_Price1);
                if (Batch_Quantity > 0 && Batch_Price > 0) {
                    priceListString = priceListString + String.valueOf("," + Batch_Quantity) + "$" + String.valueOf(Batch_Price);
                }
            }
        }
        return priceListString;
    }

    /**
     * @Title: getThisTypePrice
     * @Author: cjc
     * @Despricetion:TODO 更具redis bean type 和 产品bean sku 获取当前规格的价格
     * @Date: 2019/1/14 15:09:30
     * @Param: [skuStr, types]
     * @Return: java.lang.String
     */
    public static String getThisTypePrice(String skuStr, String types) throws Exception {
        //step v1. @author: cjc @date：2019/1/14 14:05:22  TODO 解析出skuid
        String skuId = "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(types)) {
            String[] split = types.split(",");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                String[] split1 = s.split("@");
                if (split1.length > 1) {
                    if (org.apache.commons.lang3.StringUtils.isBlank(skuId)) {
                        skuId = split1[1];
                    } else {
                        skuId += "," + split1[1];
                    }
                }
            }
        }
        String[] split = skuId.split(",");
        String skuid2 = "";
        for (int i = 0; i < split.length; i++) {
            if (org.apache.commons.lang3.StringUtils.isBlank(skuid2)) {
                skuid2 = split[split.length - (i + 1)];
            } else {
                skuid2 += "," + split[split.length - (i + 1)];
            }
        }
        //step v1. @author: cjc @date：2019/1/14 14:05:34  TODO 根据skuid 解析出价格
        String typePrice = "";
        JSONArray jsonArray = JSONArray.fromObject(skuStr);
        for (int i = 0; i < jsonArray.size(); i++) {
            Object o = jsonArray.get(i);
            JSONObject o1 = (JSONObject) o;
            Object skuPropIds = o1.get("skuPropIds");
            if (skuId.equals(skuPropIds) || skuid2.equals(skuPropIds)) {
                Object skuVal = o1.get("skuVal");
                JSONObject skuVal1 = (JSONObject) skuVal;
                typePrice = (String) skuVal1.get("actSkuCalPrice");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(typePrice)) {
                    break;
                }
                typePrice = (String) skuVal1.get("actSkuPrice");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(typePrice)) {
                    break;
                }
                typePrice = (String) skuVal1.get("actSkuMultiCurrencyDisplayPrice");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(typePrice)) {
                    break;
                }
                typePrice = (String) skuVal1.get("skuMultiCurrencyDisplayPrice");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(typePrice)) {
                    break;
                }
                typePrice = (String) skuVal1.get("skuPrice");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(typePrice)) {
                    break;
                }
            }
        }
        return typePrice.replace(",", "");
    }

    /**
     * @param aliTime 抓取的交期时间
     * @return deliveryTime 我司国内交期时间
     */
    public static String getDeliveryTime(String aliTime) {
        aliTime = aliTime.replace("\\s*", "").replace("小时", "").replaceAll("内", "")
                .replaceAll("上", "").replaceAll("以", "").replaceAll("内", "");
        String deliveryTime = "3-6";
        boolean trueAndFalse = !getStringIsNull(aliTime);
        if (trueAndFalse) {
            return deliveryTime;
        }
        boolean trueAndFalseOne = aliTime.indexOf("-") > -1;
        String aliTimeOne = "";
        String aliTimeTwo = "";
        if (trueAndFalseOne) {
            String[] aliTimeList = aliTime.split("-");
            boolean trueAndFalseTwo = aliTimeList.length > 1;
            if (trueAndFalseTwo) {
                aliTimeOne = aliTimeList[0];
                aliTimeTwo = aliTimeList[1];
            }
            aliTimeOne = getOurTime(aliTimeOne);
            aliTimeTwo = getOurTime(aliTimeTwo);
            if (!("".equals(aliTimeOne) && "".equals(aliTimeTwo))) {
                deliveryTime = aliTimeOne + "-" + aliTimeTwo;
            }
        } else {
            aliTimeOne = getOurTime(aliTime);
            if (!"".equals(aliTimeOne)) {
                deliveryTime = aliTimeOne;
            }
        }
		/*if("".equals(deliveryTime)){
			deliveryTime = "3-6";
		}*/
        return deliveryTime;
    }

    /**
     * @param time 根据抓取的小时数返回我司天数
     * @return ourtime 我司天数
     */
    public static String getOurTime(String time) {
        String ourTime = "";
        if ("".equals(ourTime)) {
            ourTime = "24".equals(time) ? "3" : "";
        }
        if ("".equals(ourTime)) {
            ourTime = "48".equals(time) ? "4" : "";
        }
        if ("".equals(ourTime)) {
            ourTime = "72".equals(time) ? "5" : "";
        }
        return ourTime;
    }

    public static boolean getStringIsNull(String str) {
        if (str != null && !"".equals(str.trim()) && !"null".equals(str)) {
            return true;
        }
        return false;
    }

    public static String changeLocalPathToRemotePath(String localPath) {
        if (StringUtils.isNotBlank(localPath)) {
            return localPath.replace(SERVICE_LOCAL_IMPORT_PATH, SERVICE_SHOW_IMPORT_URL_3);
        } else {
            return null;
        }
    }

    /**
     * 全部图片数据
     * @param gd
     * @return
     */
    public static List<String> getAllImgList(CustomGoodsPublish gd, int isKids){
		List<String> changeImglist = new ArrayList<>();
		// 主图
		String orMainImg220x220 = null;
        if (StringUtils.isNotBlank(gd.getShowMainImage())) {
            if (gd.getShowMainImage().contains("http://") || gd.getShowMainImage().contains("https://")) {
                orMainImg220x220 = gd.getShowMainImage().trim();
            } else {
                orMainImg220x220 = gd.getRemotpath().trim() + gd.getShowMainImage().trim();
            }
        } else if (StringUtils.isNotBlank(gd.getCustomMainImage())) {
            if (gd.getCustomMainImage().contains("http://") || gd.getCustomMainImage().contains("https://")) {
                orMainImg220x220 = gd.getCustomMainImage().trim();
            } else {
                orMainImg220x220 = gd.getRemotpath().trim() + gd.getCustomMainImage().trim();
            }
        }

        if (StringUtils.isNotBlank(orMainImg220x220)) {
            List<String> mainImgList = getMainImgByPath(orMainImg220x220);
            changeImglist.addAll(mainImgList);
            mainImgList.clear();
        }

		// 橱窗图
		List<String> imgList = deal1688GoodsImg(gd.getImg(), gd.getRemotpath(), 1);
		changeImglist.addAll(imgList);
		imgList.clear();

		// 详情图
		List<String> detailsImgList = genDetailsImgList(gd.getEninfo(), gd.getRemotpath());
		if (detailsImgList != null && detailsImgList.size() > 0) {
			changeImglist.addAll(detailsImgList);
		}
		// 规格图片
		List<String> entypeImgList = genEntypeImgList(gd);
		if (entypeImgList != null && entypeImgList.size() > 0) {
			changeImglist.addAll(entypeImgList);
			entypeImgList.clear();
		}
		List<String> nwList = new ArrayList<>();
        for(String imgL : changeImglist){
            nwList.add(changeRemotePathToLocal(imgL, isKids));
        }
        changeImglist.clear();
        return nwList;
	}

	public static List<String> getMainImgByPath(String remotPathImg) {
		String changeRemote;
		if (remotPathImg.contains("http:")) {
			changeRemote = remotPathImg.replace("http:", "https:");
		} else {
			changeRemote = remotPathImg;
		}
		List<String> imgList = new ArrayList<String>();
		if (changeRemote.contains("220x220")) {
			imgList.add(changeRemote);
			imgList.add(changeRemote.replace("220x220", "285x285"));
		} else if (changeRemote.contains("400x400")) {
			imgList.add(changeRemote);
			imgList.add(changeRemote.replace("400x400", "220x220"));
			imgList.add(changeRemote.replace("400x400", "285x285"));
		}
		return imgList;
	}

	// 处理1688商品的规格图片数据
	public static List<String> deal1688GoodsImg(String img, String remotPath, int is400) {

		String changeRemote;
		if (remotPath.contains("http:")) {
			changeRemote = remotPath.replace("http:", "https:");
		} else {
			changeRemote = remotPath;
		}
		List<String> imgList = new ArrayList<String>();

		if (StringUtils.isNotBlank(img)) {
			img = img.replace("[", "").replace("]", "").trim();
			String[] imgs = img.split(",\\s*");

			for (int i = 0; i < imgs.length; i++) {
				if (!imgs[i].isEmpty()) {
					if (imgs[i].indexOf("http://") > -1 || imgs[i].indexOf("https://") > -1) {
						imgList.add(imgs[i].replace("http:", "https:"));
						if (is400 > 0) {
							imgList.add(imgs[i].replace("http:", "https:").replace("60x60", "400x400"));
						}
					} else {
						imgList.add(changeRemote + imgs[i]);
						if (is400 > 0) {
							imgList.add(changeRemote + imgs[i].replace("60x60", "400x400"));
						}
					}
				}
			}
		}
		return imgList;
	}

	public static List<String> genDetailsImgList(String detailStr, String remotPath) {
		String changeRemote;
		// 详情数据的获取和解析img数据
		List<String> imgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(detailStr)) {
			if (remotPath.contains("http:")) {
				changeRemote = remotPath.replace("http:", "https:");
			} else {
				changeRemote = remotPath;
			}

			Document nwDoc = Jsoup.parseBodyFragment(detailStr);
			Elements imgEls = nwDoc.getElementsByTag("img");
			if (imgEls.size() > 0) {
				for (Element imel : imgEls) {
					String imgUrl = imel.attr("src");
					if (StringUtils.isBlank(imgUrl)) {
						continue;
					} else if (imgUrl.contains("http")) {
						if (imgUrl.length() > 500) {
							continue;
						} else {
							imgList.add(imgUrl.replace("http:", "https:"));
						}
					} else {
						if (imgUrl.length() > 500) {
							continue;
						} else {
							imgList.add(changeRemote + imgUrl);
						}
					}
				}
				imgEls.clear();
			}
		}

		return imgList;
	}

	public static List<String> genEntypeImgList(CustomGoodsPublish gd) {
		List<TypeBean> typeList = deal1688GoodsType(gd, true);
		List<String> imgList = new ArrayList<>();
		for (TypeBean typeBean : typeList) {
			if (StringUtils.isNotBlank(typeBean.getImg()) && typeBean.getImg().length() > 10) {
				if (typeBean.getImg().contains("http://") || typeBean.getImg().contains("https://")) {
					imgList.add(typeBean.getImg().replace("60x60", "400x400"));
				}
			}
		}
		typeList.clear();
		return imgList;
	}
}
