package com.cbt.util;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.ImportExSku;
import com.cbt.bean.ImportExSkuShow;
import com.cbt.bean.TypeBean;
import com.cbt.parse.service.StrUtils;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.*;


public class GoodsInfoUtils {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsInfoUtils.class);

    private static final String SERVICE_LOCAL_PATH = "/usr/local/goodsimg";
    private static final String SERVICE_SHOW_URL_1 = "http://img.import-express.com";
    private static final String SERVICE_SHOW_URL_2 = "http://img1.import-express.com";
    private static final String SERVICE_SHOW_URL_3 = "https://img.import-express.com";
    private static final String SERVICE_SHOW_URL_4 = "https://img1.import-express.com";

    private static String chineseChar = "([\\一-\\龥]+)";

    // 处理1688商品的规格图片数据
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


    public static String changeRemotePathToLocal(String remotepath) {

        String localPathByRemote = remotepath;
        if (remotepath.contains(SERVICE_SHOW_URL_1)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_1, SERVICE_LOCAL_PATH);
        } else if (remotepath.contains(SERVICE_SHOW_URL_2)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_2, SERVICE_LOCAL_PATH);
        } else if (remotepath.contains(SERVICE_SHOW_URL_3)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_3, SERVICE_LOCAL_PATH);
        } else if (remotepath.contains(SERVICE_SHOW_URL_4)) {
            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_4, SERVICE_LOCAL_PATH);
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
}
