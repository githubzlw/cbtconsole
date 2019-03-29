package com.cbt.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 *  entype2数据生成
 *       2019/03/22 10:23
 *
 */
public class ChangeEntypeUtils {

    public static void main(String[] args) {
        //测试数据
        String entype = "[[id=32161, type=Color, value=White, img=577343465096/9409448989_495241798.60x60.jpg], [id=32162, type=Color, value=black, img=577343465096/9389800785_495241798.60x60.jpg], [id=4501, type=Size, value=35, img=], [id=4502, type=Size, value=36, img=], [id=4503, type=Size, value=37, img=], [id=4504, type=Size, value=38, img=], [id=4505, type=Size, value=39, img=], [id=4506, type=Size, value=40, img=], [id=4507, type=Size, value=41, img=], [id=4508, type=Size, value=42, img=], [id=4509, type=Size, value=43, img=], [id=45010, type=Size, value=44, img=]]";
        String sku = "[{\"skuAttr\":\"3216:32161;450:4506\", \"skuPropIds\":\"32161-4506\", \"specId\":\"0933002c114de99a88de2b0fc2e1da2\", \"skuId\":\"3813358265808\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":0, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4507\", \"skuPropIds\":\"32161:4507\", \"specId\":\"f1a259b7b1b1c76c2a2971fe9d40b85d\", \"skuId\":\"3813358265809\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":499, \"inventory\":499, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4508\", \"skuPropIds\":\"32161,4508\", \"specId\":\"56b5771a6073ac50c9b770fba594ebec\", \"skuId\":\"3813358265810\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4509\", \"skuPropIds\":\"32161,4509\", \"specId\":\"b797ebdd2572671814a601289fbad8d4\", \"skuId\":\"3813358265811\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:45010\", \"skuPropIds\":\"32161,45010\", \"specId\":\"e1bf87d1cbf3e11c4b32d1b4e90c9b39\", \"skuId\":\"3813358265812\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4501\", \"skuPropIds\":\"32161,4501\", \"specId\":\"2e3733144b80ee75220f6790e51df728\", \"skuId\":\"3813358265803\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4503\", \"skuPropIds\":\"32162,4503\", \"specId\":\"988984c04b103c093f25b4ecd71c7220\", \"skuId\":\"3813358265815\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":499, \"inventory\":499, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4502\", \"skuPropIds\":\"32162,4502\", \"specId\":\"3aeab1a8a943887e495ecb51201d9c81\", \"skuId\":\"3813358265814\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4505\", \"skuPropIds\":\"32162,4505\", \"specId\":\"409e988ccd22beae7ed1373859c47d15\", \"skuId\":\"3813358265817\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4504\", \"skuPropIds\":\"32162,4504\", \"specId\":\"24a8dfcd3e63f946b66b8a1713ac8c51\", \"skuId\":\"3813358265816\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:45010\", \"skuPropIds\":\"32162,45010\", \"specId\":\"1ef04a8c3262c38535070c23ddc0e091\", \"skuId\":\"3813358265822\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":499, \"inventory\":499, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4509\", \"skuPropIds\":\"32162,4509\", \"specId\":\"eea47f53753ea744a2915efa56dd1263\", \"skuId\":\"3813358265821\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":499, \"inventory\":499, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4501\", \"skuPropIds\":\"32162,4501\", \"specId\":\"9a319bccb4f6ad1dda7535f98f728834\", \"skuId\":\"3813358265813\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4502\", \"skuPropIds\":\"32161,4502\", \"specId\":\"5101a8f894ca17b47bc8f328b42e4232\", \"skuId\":\"3813358265804\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4506\", \"skuPropIds\":\"32162,4506\", \"specId\":\"f279f76ad1b737009211a09179cb4d19\", \"skuId\":\"3813358265818\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4503\", \"skuPropIds\":\"32161,4503\", \"specId\":\"6ee65dceae5ce22995484d8436190225\", \"skuId\":\"3813358265805\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4504\", \"skuPropIds\":\"32161,4504\", \"specId\":\"6ff6071792c8ab520b9b867c61b990bd\", \"skuId\":\"3813358265806\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4508\", \"skuPropIds\":\"32162,4508\", \"specId\":\"b825000ae9a12f71815406c77db3c440\", \"skuId\":\"3813358265820\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":499, \"inventory\":499, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32161;450:4505\", \"skuPropIds\":\"32161,4505\", \"specId\":\"8392c83bf1f5acc659f56cc174bf31b4\", \"skuId\":\"3813358265807\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":500, \"inventory\":500, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}, {\"skuAttr\":\"3216:32162;450:4507\", \"skuPropIds\":\"32162,4507\", \"specId\":\"f13a9daaf289f5b716d04a2b3b8fc2b0\", \"skuId\":\"3813358265819\", \"fianlWeight\":\"1.0\", \"skuVal\":{\"actSkuCalPrice\":\"25.37\", \"actSkuMultiCurrencyCalPrice\":\"25.37\", \"actSkuMultiCurrencyDisplayPrice\":\"25.37\", \"availQuantity\":498, \"inventory\":498, \"isActivity\":true, \"skuCalPrice\":\"25.37\", \"skuMultiCurrencyCalPrice\":\"25.37\", \"skuMultiCurrencyDisplayPrice\":\"25.37\"}}]";
        String remotpath = "https://img.import-express.com/importcsvimg/singleimg20/";

        //示例
        String entypeNew = ChangeEntypeUtils.getEntypeNew(entype, sku, remotpath);

        //返回结果保存在entype2字段 后续线上产品单页直接使用
        System.out.println(entypeNew);

    }

    /**
     * @param entype     商品原entype数据
     * @param sku       商品对应sku数据
     * @param remotpath entype中图片对应服务器地址(对应该产品表中remotpath字段)
     * @return
     */
    public static String getEntypeNew(String entype, String sku, String remotpath) {
        if (entype == null || entype.length() < 5) {
            return "";
        }
        //最终entype数据
        List<TypeBean> typeList = new ArrayList<TypeBean>();
        //原线上产品单页规格解析代码一
        formatChange(entype, remotpath, typeList);
        //原线上产品单页规格解析代码二
        typeList = sortTypeList(typeList);
        // 新加对应规格是否可卖字段 2019-3-21
        addTypeSell(typeList, sku);
        //返回
        return typeList.toString();
    }

    private static final String chineseChar = "([\\一-\\龥]+)";//()表示匹配字符串，[]表示在首尾字符范围  从 \\一 到 \\龥字符之间，+号表示至少出现一次

    private static void formatChange(String types, String remotpath, List<TypeBean> typeList) {
        if (StringUtils.isNotBlank(types) && !StringUtils.equals(types, "[]")) {
            Map<String, List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
            types = types.replace("[[", "[").replace("]]", "]").trim();
            String[] matchStrList = types.split(",\\s*\\[");
            // List<String> matchStrList = StrUtils.matchStrList("(\\[.*\\])", types);
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
                    } else if (type[j].indexOf("value=") > -1) {
                        tems = type[j].split("value=");
                        tem = tems.length > 1 ? tems[1] : "";
                        tem = StringUtils.equals(tem, "null") ? "" : tem;
                        //如果 规格名称是 #键开头，在页面展示时，会发生冲突。
                        typeBean.setValue(tem.replace("#", "1#").replaceAll(chineseChar, ""));
                    } else if (type[j].indexOf("img=") > -1) {

                        tems = type[j].split("img=");
                        tem = tems.length > 1 ? tems[1] : "";
                        tem = tem.endsWith(".jpg") ? tem : "";
                        tem = StringUtils.isBlank(tem) || StringUtils.equals(tem, "null") ? "" : remotpath + tem;
                        typeBean.setImg(tem);
                    }
                }
                //规格名称没有给默认值，保证能加购物车，同时 LOG记录什么产品有问题
                if (StringUtils.isBlank(typeBean.getType()) || "null".equals(typeBean.getType())) {
                    typeBean.setType("Color");
//                    logger.error("getSpider:规格的 type值 为 null !"+String.valueOf(map.get("pid")));
                }
                //2018/12/13 qiqing  暂时处理 规格数据中值为null的情况，防止产品加不了购物车，后续张立伟数据层面还要做优化改进。
                if (StringUtils.isBlank(typeBean.getValue()) || "null".equals(typeBean.getValue())) {
                    if (StringUtils.isNotBlank(typeBean.getImg()) && !"null".equals(typeBean.getImg())) {
                        typeBean.setValue("123");
                    } else if ((StringUtils.isBlank(typeBean.getImg()) || "null".equals(typeBean.getImg())) && (StringUtils.isBlank(typeBean.getValue()) || "null".equals(typeBean.getValue()))) {
                        typeBean.setValue("456");
                    } else {
                        continue;
                    }
                }

                String value = typeBean.getValue().toLowerCase();
                if (StrUtils.isFind(value, "(customized)")) {
                    continue;
                }
                List<TypeBean> list = typeMap.get(typeBean.getType());
                list = list == null ? new ArrayList<TypeBean>() : list;
                list.add(typeBean);
                typeMap.put(typeBean.getType(), list);
            }
            Iterator<Map.Entry<String, List<TypeBean>>> iterator = typeMap.entrySet().iterator();
            List<TypeBean> valueImg = new ArrayList<TypeBean>();
            List<TypeBean> valueTitle = new ArrayList<TypeBean>();
            while (iterator.hasNext()) {
                List<TypeBean> values = iterator.next().getValue();
                valueImg.clear();
                valueTitle.clear();
                for (TypeBean value : values) {
                    if (StringUtils.isNotBlank(value.getImg())) {
                        valueImg.add(value);
                    } else {
                        valueTitle.add(value);
                    }
                }
                if (!valueImg.isEmpty()) {
                    typeList.addAll(valueImg);
                }
                if (!valueTitle.isEmpty()) {
                    typeList.addAll(valueTitle);
                }
            }
        }
    }

    /**
     * 规格排序
     * 1,没有规格情况 直接可以加购物车
     * 2，有1种规格情况无论是图片还是文字规格都竖排显示 也就是直接循环出来
     * 3，有2种规格情况
     * （1）如果2种规格都是图片，尽量选择多的那种有加减按钮 少的那种作为选择规格
     * （2）如果2种规格一种图片另一种是文字规格，图片在上面作为规格显示，文字规格有加减按钮
     * （3）如果2种规格都是文字，尽量选择多的那种有加减按钮 少的那种作为选择规格
     * 4，有大于2种规格情况 也尽量要求图片在上面
     *
     * @param type
     * @return
     * @data 2018年3月7日
     * @author user4
     */
    private static List<TypeBean> sortTypeList(List<TypeBean> type) {
        if (type == null || type.size() == 0) {
            return type;
        }
        Map<String, String> markMap = new HashMap<String, String>();
        List<TypeBean> type1 = null, type2 = null, type3 = null;
        boolean isTypeImg1 = false, isTypeImg2 = false, isTypeImg3 = false;
        int typeSize1 = 0, typeSize2 = 0, typeSize3 = 0;
        int typeIndex = 1;
        for (TypeBean typeBean : type) {
            if (StringUtils.isBlank(typeBean.getType())) {
                continue;
            }
            String typeParam = typeBean.getType().trim();
            typeParam = StringUtils.isNotBlank(typeParam) ?
                    typeParam.replaceAll("(\\.\\s*)", " ").replaceAll("[',\";#\\:]", "")
                            .replace("<", "&lt;").replace(">", "&gt;") : "";

            typeBean.setType(typeParam.substring(0, 1).toUpperCase() + typeParam.substring(1));
            String labType = StringUtils.isNotBlank(typeParam) ?
                    typeParam.replaceAll("(\\W+)", "").toLowerCase() : "";
            typeBean.setLableType(labType);
            String value = typeBean.getValue();
            //value = StringUtils.isNotBlank(value) ?value.replaceAll("(\\(.*\\))", "").replaceAll("(\\[.*\\])", "") : "";

            //qiqing  不去掉括号内容，有用处  2018/06/13
            value = StringUtils.isNotBlank(value) ? value.replaceAll("(\\[.*\\])", "") : "";


            if (StringUtils.isNotBlank(value)) {
                value = value.replace("<", "&lt;").replace(">", "&gt;");
                typeBean.setValue(value.replaceAll("(\\s+)", " "));
            }
            String markType = markMap.get(typeBean.getType());
            markType = markType == null ? "type" + (typeIndex++) : markType;
            markMap.put(typeBean.getType(), markType);
            if ("type1".equals(markType)) {
                type1 = type1 == null ? new ArrayList<TypeBean>() : type1;
                isTypeImg1 = !isTypeImg1 ? StringUtils.isNotBlank(typeBean.getImg()) : isTypeImg1;
                type1.add(typeBean);
                typeSize1 = type1.size();
            } else if ("type2".equals(markType)) {
                type2 = type2 == null ? new ArrayList<TypeBean>() : type2;
                isTypeImg2 = !isTypeImg2 ? StringUtils.isNotBlank(typeBean.getImg()) : isTypeImg2;
                type2.add(typeBean);
                typeSize2 = type2.size();
            } else if ("type3".equals(markType)) {
                type3 = type3 == null ? new ArrayList<TypeBean>() : type3;
                isTypeImg3 = !isTypeImg3 ? StringUtils.isNotBlank(typeBean.getImg()) : isTypeImg3;
                type3.add(typeBean);
                typeSize3 = type3.size();
            }
        }
//		1,没有规格情况 直接可以加购物车
//		2，有1种规格情况无论是图片还是文字规格都竖排显示 也就是直接循环出来
//		3，有2种规格情况
//			（1）如果2种规格都是图片，尽量选择多的那种有加减按钮 少的那种作为选择规格
//			（2）如果2种规格一种图片另一种是文字规格，图片在上面作为规格显示，文字规格有加减按钮
//			（3）如果2种规格都是文字，尽量选择多的那种有加减按钮 少的那种作为选择规格
//		4，有大于2种规格情况 也尽量要求图片在上面
        if (typeSize1 > 0 && typeSize2 > 0) {
            List<TypeSortBean> sortList = new ArrayList<TypeSortBean>();
            List<TypeBean> typeResult = new ArrayList<TypeBean>();
            sortList.add(new TypeSortBean(typeSize1, isTypeImg1 ? 1 : 0, "type1"));
            sortList.add(new TypeSortBean(typeSize2, isTypeImg2 ? 1 : 0, "type2"));
            if (typeSize3 > 0) {
                sortList.add(new TypeSortBean(typeSize3, isTypeImg3 ? 1 : 0, "type3"));
            }
            Collections.sort(sortList, new Comparator<TypeSortBean>() {
                public int compare(TypeSortBean arg0, TypeSortBean arg1) {
//					int result = arg1.getIsTypeImage() - arg0.getIsTypeImage();
//					result = result == 0 ? arg0.getSize() - arg1.getSize() : result;
                    int result = arg0.getSize() - arg1.getSize();
                    result = result == 0 ? arg1.getIsTypeImage() - arg0.getIsTypeImage() : result;
                    return result;
                }
            });
            for (TypeSortBean sort : sortList) {
                if ("type1".equals(sort.getTypeName())) {
                    typeResult.addAll(type1);
                } else if ("type2".equals(sort.getTypeName())) {
                    typeResult.addAll(type2);
                } else if ("type3".equals(sort.getTypeName())) {
                    typeResult.addAll(type3);
                }
            }
            return typeResult;
        }
        return type;
    }

    private static void addTypeSell(List<TypeBean> typeList, String sku) {
        if (typeList != null && typeList.size() > 0) {
            if (sku == null || sku.length() < 10) {
                //没有sku库存 不可卖
                return;
            } else {
                //有sku库存数据 sku数据解析 判断是否可卖
                try {
                    //正在卖的规格(防止去除一手等规格后 和一手组合的规格标记为了可卖)
                    List<String> idList = new ArrayList<String>();
                    for (TypeBean bean : typeList) {
                        idList.add(bean.getId());
                    }
                    //可卖规格
                    HashSet<String> sellSet = new HashSet<String>();
                    JSONArray jsonArray = JSONArray.fromObject(sku);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject skuBean = jsonArray.getJSONObject(i);
                        String skuPropIds = skuBean.getString("skuPropIds");
                        JSONObject skuVal = skuBean.getJSONObject("skuVal");
                        int inventory = skuVal.getInt("inventory");
                        if (StringUtils.isNotBlank(skuPropIds) && inventory > 0) {
                            if (!StringUtils.isNumeric(skuPropIds)) {
                                String spl = skuPropIds.replaceAll("\\d", "");
                                List<String> temList = new ArrayList<String>(Arrays.asList(skuPropIds.split(spl)));
                                if (idList.containsAll(temList)) { //sku对应可卖规格都在是可卖中的（去除一手可能导致不可卖）
                                    sellSet.addAll(temList);
                                }
                            } else {
                                sellSet.add(skuPropIds);
                            }
                        }
                    }
                    //可卖判断
                    for (TypeBean bean : typeList) {
                        if (sellSet.contains(bean.getId())) {
                            bean.setSell("1");
                        }
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

}

class TypeSortBean {
    private int size;//规格数量
    private int isTypeImage;//是否图片规格
    private String typeName;//

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getIsTypeImage() {
        return isTypeImage;
    }

    public void setIsTypeImage(int isTypeImage) {
        this.isTypeImage = isTypeImage;
    }

    public TypeSortBean(int size, int isTypeImage, String typeName) {
        super();
        this.size = size;
        this.isTypeImage = isTypeImage;
        this.typeName = typeName;
    }
}

class TypeBean implements Serializable {

    private static final long serialVersionUID = 6798772789095149437L;

    private String type;//规格类型
    private String value;//规格值
    private String img;//图片
    private String id;//id
    private String lableType;
    private String sell = "0";//是否可卖：0-不可卖；1-可卖；

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getLableType() {
        return lableType;
    }

    public void setLableType(String lableType) {
        this.lableType = lableType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value != null) {
            value = value.replaceAll("\"", "").replaceAll("'", "");
        }
        this.value = value;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\":\"");
        builder.append(type);
        builder.append("\", \"value\":\"");
        builder.append(value);
        builder.append("\", \"img\":\"");
        builder.append(img);
        builder.append("\", \"id\":\"");
        builder.append(id);
        builder.append("\", \"lableType\":\"");
        builder.append(lableType);
        builder.append("\", \"sell\":\"");
        builder.append(sell);
        builder.append("\"}");
        return builder.toString();
    }

    public TypeBean() {
        super();
    }

    public TypeBean(String type, String value, String img, String id,
                    String lableType) {
        super();
        this.type = type;
        this.value = value;
        this.img = img;
        this.id = id;
        this.lableType = lableType;
    }

    public TypeBean(String type, String id) {
        super();
        this.type = type;
        this.id = id;
    }
}