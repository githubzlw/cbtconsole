package com.importExpress.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.jcys.util.HttpUtil;
import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SysParamUtil;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.pojo.ShopUrlAuthorizedInfoPO;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;
import com.importExpress.service.TabSeachPageService;
import com.importExpress.utli.*;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ?????????????????????
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/tabseachpage")
public class TabSeachPageController {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CategoryResearchController.class);

    @Autowired
    private TabSeachPageService tabSeachPageService;

    private static List<Map<String, Object>> aliCategory;

    private static String cbtstaticizePath = SysParamUtil.getParam("cbtstaticize");

    @RequestMapping("getAllCat")
    @ResponseBody
    public List<TabSeachPageBean> getAllCat(Integer webSite) {
        if (webSite == null || webSite < 1) {
            webSite = 1;
        }
        List<TabSeachPageBean> list = new ArrayList<TabSeachPageBean>();
        try {
            // DataSourceSelector.set("dataSource127hop");
            list = tabSeachPageService.list(0, webSite);
            // DataSourceSelector.restore();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        return list;
    }

    /**
     * ???????????????
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/add")
    public void add(HttpServletRequest request, HttpServletResponse response,
                    @RequestParam(value = "uploadfile", required = true) MultipartFile file) throws Exception {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String result = null;
        String keyword = request.getParameter("keyword");
        // DataSourceSelector.set("dataSource127hop");
        try {
            if (tabSeachPageService.getWordsCount(keyword) > 0) {
                result = "{\"status\":false,\"message\":\"????????????" + keyword + "???????????????\"}";
            } else {
                TabSeachPageBean bean = new TabSeachPageBean();
                String keyword1 = request.getParameter("keyword1");
                String pid = request.getParameter("parentId");
                String pageTitle = request.getParameter("pageTitle");
                String pageKeywords = request.getParameter("pageKeywords");
                String pageDescription = request.getParameter("pageDescription");
                bean.setKeyword(keyword);
                bean.setKeyword1(keyword1);
                bean.setPageTitle(pageTitle);
                bean.setPageKeywords(pageKeywords);
                bean.setPageDescription(pageDescription);
                if (StringUtils.isNotBlank(pid)) {
                    bean.setParentId(Integer.parseInt(pid));
                }
                boolean updateFlag = true;
                String[] imgInfo = SearchFileUtils.comFileUpload(file, String.valueOf(bean.getParentId()), null, null, null, 0);
                if (null != imgInfo && imgInfo.length > 0) {
                    //????????????????????????
                    Long imgSize = SearchFileUtils.getImgSize(imgInfo[0]);
                    if (imgSize > 102401) {//???????????????????????????100k
                        result = "{\"status\":false,\"message\":\"?????????????????????100K??????????????????QQ????????????????????????\"}";
                        updateFlag = false;
                    } else {
                        bean.setPageBannerName(imgInfo[0]);
                        bean.setPageBannerUrl(imgInfo[1]);
                    }
                }
                if (updateFlag) {
                    // ????????????????????????id
                    Integer catid = tabSeachPageService.getCategoryId(keyword);
                    if (catid != null) {
                        bean.setCatId(catid);
                    }
                    int res = tabSeachPageService.insert(bean);
                    // MQ????????????
                    insertMq(bean);

                    if (res > 0) {
                        result = "{\"status\":true,\"message\":\"????????????" + keyword + "??????????????????\",\"id\":\"" + bean.getId() + "\"}";
                    } else {
                        result = "{\"status\":false,\"message\":\"????????????" + keyword + "??????????????????\"}";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    private void insertMq(TabSeachPageBean bean) {

        String keyword = bean.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(keyword);
        } else {
            keyword = "";
        }

        String keyword1 = bean.getKeyword1();
        if (StringUtils.isNotBlank(keyword1)) {
            keyword1 = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(keyword1);
        } else {
            keyword1 = "";
        }


        String pageTitle = bean.getPageTitle();
        if (StringUtils.isNotBlank(pageTitle)) {
            pageTitle = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(pageTitle);
        } else {
            pageTitle = "";
        }

        String pageKeywords = bean.getPageKeywords();
        if (StringUtils.isNotBlank(pageKeywords)) {
            pageKeywords = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(pageKeywords);
        } else {
            pageKeywords = "";
        }

        String page_description = bean.getPageDescription();
        if (StringUtils.isNotBlank(page_description)) {
            page_description = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(page_description);
        } else {
            page_description = "";
        }

        String pageBannerName = bean.getPageBannerName();
        if (StringUtils.isNotBlank(pageBannerName)) {
            pageBannerName = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(pageBannerName);
        } else {
            pageBannerName = "";
        }

        List<String> paramList = new ArrayList<>();
        paramList.add(keyword);
        paramList.add(keyword1);
        paramList.add(bean.getBanner() == null ? "" : bean.getBanner());
        paramList.add(bean.getFilename());
        paramList.add("0");
        paramList.add(String.valueOf(bean.getCatId()));
        paramList.add(String.valueOf(bean.getParentId()));
        paramList.add(pageTitle);
        paramList.add(pageKeywords);
        paramList.add(page_description);
        paramList.add(pageBannerName);
        paramList.add(bean.getPageBannerUrl());
        paramList.add(String.valueOf(bean.getWebSite()));
        String sql = "insert into tab_seach_pages(keyword,keyword1,banner,filename,sate,catid,pid," +
                "page_title, page_keywords, page_description, page_banner_name, page_banner_url,web_site)" +
                "values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";

        NotifyToCustomerUtil.sendSqlByMq(String.format(sql, paramList));
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pid = request.getParameter("id");
        if (org.apache.commons.lang.StringUtils.isBlank(pid)) {
            pid = "0";
        }
        String webSiteStr = request.getParameter("webSite");
        int webSite = Integer.parseInt(webSiteStr);

        String jsontree = "";
        try {
            // DataSourceSelector.set("dataSource127hop");
            List<TabSeachPageBean> li = this.tabSeachPageService.list(Integer.parseInt(pid), webSite);
            jsontree = JsonTreeUtils.jsonTree(li);
            if (StringUtils.isNotBlank(jsontree)) {  // ????????????
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(jsontree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
    }


    @RequestMapping("/setWebSite")
    public void setWebSite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String id = request.getParameter("id");
        String website = request.getParameter("website");
        String result = null;
        int res = 1;
        try {
            res = tabSeachPageService.updateSeachPagesSite(Integer.parseInt(website), Integer.parseInt(id));
            if (res > 0) {
                SendMQ.sendMsg(new RunSqlModel("update tab_seach_pages set web_site=" + website + " where id=" + id));
                result = "{\"status\":true,\"message\":\"???????????????\"}";
            } else {
                result = "{\"status\":false,\"message\":\"???????????????\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }


    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String id = request.getParameter("id");
        String result = null;
        int res = 1;
        try {

            res = tabSeachPageService.updateSeachPagesSate(Integer.parseInt(id), 1);
            if (res > 0) {
                SendMQ.sendMsg(new RunSqlModel("update tab_seach_pages set sate=1 where id=" + id + ""));
                result = "{\"status\":true,\"message\":\"???????????????\"}";
            } else {
                result = "{\"status\":false,\"message\":\"???????????????\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ????????????
     *
     * @throws IOException
     */
    @RequestMapping("deleteCate")
    public void deleteCate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = "";
        try {

            String id = request.getParameter("id");
            int res = tabSeachPageService.deleteCate(Integer.parseInt(id));
            if (res > 0) {
                SendMQ.sendMsg(new RunSqlModel("update tab_seach_pages set pid = -1 where pid='" + id + "';"));
                SendMQ.sendMsg(new RunSqlModel("update tab_seach_pages set sate=1 where id='" + id + "';"));
                result = "{\"status\":true,\"message\":\"???????????????\"}";
            } else {
                result = "{\"status\":false,\"message\":\"???????????????\"}";
            }

        } catch (Exception e) {
            result = "{\"status\":false,\"message\":\"???????????????\"}";
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/get")
    public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        TabSeachPageBean bean = new TabSeachPageBean();
        try {
            // DataSourceSelector.set("dataSource127hop");
            String id = request.getParameter("id");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(id)) {
                bean = tabSeachPageService.get(Integer.parseInt(id));
                switch (bean.getWebSite()) {
                    case 1:
                        bean.setImportPath(SearchFileUtils.importexpressPath);
                        break;
                    case 2:
                        bean.setImportPath(SearchFileUtils.kidsPath);
                        break;
                    case 3:
                        bean.setImportPath(SearchFileUtils.petsPath);
                        break;
                    default:
                        bean.setImportPath(SearchFileUtils.importexpressPath);
                }


                if (StringUtil.isNotBlank(bean.getFilename())) {
                    bean.setFilename(bean.getImportPath() + bean.getFilename());
                }
                if (StringUtil.isNotBlank(bean.getPageBannerName())) {
                    bean.setPageBannerName(SearchFileUtils.IMAGEHOSTURL + bean.getPageBannerName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(bean));
        out.close();
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/update")
    public void update(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "uploadfile", required = true) MultipartFile file) throws Exception {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String result = null;
        String id = request.getParameter("update_id");
        String keyword = request.getParameter("update_keyword");
        try {
            // DataSourceSelector.set("dataSource127hop");
            if (tabSeachPageService.getWordsCount1(keyword, Integer.parseInt(id)) > 0) {
                result = "{\"status\":false,\"message\":\"????????????" + keyword + "???????????????\"}";
            } else {
                TabSeachPageBean bean = new TabSeachPageBean();
                String keyword1 = request.getParameter("update_keyword1");
                String pid = request.getParameter("parentId");
                String pageTitle = request.getParameter("update_pageTitle");
                String pageKeywords = request.getParameter("update_pageKeywords");
                String pageDescription = request.getParameter("update_pageDescription");
                bean.setId(Integer.parseInt(id));
                bean.setKeyword(keyword);
                bean.setKeyword1(keyword1);
                bean.setPageTitle(pageTitle);
                bean.setPageKeywords(pageKeywords);
                bean.setPageDescription(pageDescription);
                if (StringUtils.isNotBlank(pid)) {
                    bean.setParentId(Integer.parseInt(pid));
                }

                boolean updateFlag = true;
                if ("on".equals(request.getParameter("del_pageBannerName"))) { //????????????????????????????????????????????????banner??????
                    bean.setPageBannerName("del");
                } else {
                    String[] imgInfo = SearchFileUtils.comFileUpload(file, String.valueOf(bean.getParentId()), null, null, null, 0);
                    if (null != imgInfo && imgInfo.length > 0) {
                        //????????????????????????
                        Long imgSize = SearchFileUtils.getImgSize(imgInfo[0]);
                        if (imgSize > 102402) {//???????????????????????????100k
                            result = "{\"status\":false,\"message\":\"?????????????????????100K??????????????????QQ????????????????????????\"}";
                            updateFlag = false;
                        } else {
                            bean.setPageBannerName(imgInfo[0]);
                            bean.setPageBannerUrl(imgInfo[1]);
                        }
                    }
                }
                if (updateFlag) {

                    int res = tabSeachPageService.update(bean);
                    if (res > 0) {
                        updateSearchPage(bean);
                        result = "{\"status\":true,\"message\":\"???????????????\"}";
                    } else {
                        result = "{\"status\":false,\"message\":\"???????????????\"}";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    public void updateSearchPage(TabSeachPageBean bean) {

        String keyword = bean.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(keyword);
        } else {
            keyword = "";
        }

        String keyword1 = bean.getKeyword1();
        if (StringUtils.isNotBlank(keyword1)) {
            keyword1 = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(keyword1);
        } else {
            keyword1 = "";
        }


        String pageTitle = bean.getPageTitle();
        if (StringUtils.isNotBlank(pageTitle)) {
            pageTitle = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(pageTitle);
        } else {
            pageTitle = "";
        }

        String pageKeywords = bean.getPageKeywords();
        if (StringUtils.isNotBlank(pageKeywords)) {
            pageKeywords = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(pageKeywords);
        } else {
            pageKeywords = "";
        }

        String page_description = bean.getPageDescription();
        if (StringUtils.isNotBlank(page_description)) {
            page_description = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(page_description);
        } else {
            page_description = "";
        }

        String pageBannerName = bean.getPageBannerName();
        if (StringUtils.isNotBlank(pageBannerName)) {
            pageBannerName = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(pageBannerName);
        } else {
            pageBannerName = "";
        }

        List<String> paramList = new ArrayList<>();
        paramList.add(keyword);
        paramList.add(keyword1);
        paramList.add(bean.getBanner() == null ? "" : bean.getBanner());
        paramList.add(String.valueOf(bean.getParentId()));
        paramList.add(pageTitle);
        paramList.add(pageKeywords);
        paramList.add(page_description);

        if (pageBannerName != null) {
            paramList.add(pageBannerName);
            paramList.add(bean.getPageBannerUrl());
        }

        String sql = "update tab_seach_pages set keyword='%s',keyword1='%s',banner='%s',pid='%s'," +
                "page_title='%s',page_keywords='%s',page_description='%s'";
        if (pageBannerName != null) {
            sql += ", page_banner_name='%s', page_banner_url='%s'";
        }
        if ("del".equalsIgnoreCase(pageBannerName)) {
            sql += ", page_banner_name=NULL, page_banner_url=NULL";
        }
        sql += " where id=#{id}";

        NotifyToCustomerUtil.sendSqlByMq(String.format(sql, paramList));
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/aliCategory")
    public void aliCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (aliCategory == null) {
//			// DataSourceSelector.set("dataSource127hop");
            aliCategory = tabSeachPageService.aliCategory();
        }

        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : aliCategory) {
            int lv = (Integer) map.get("lv");
            if (lv == 0 || lv == 1) {
                list1.add(map);
            } else if (lv == 2) {
                list2.add(map);
            } else if (lv == 3) {
                list3.add(map);
            } else if (lv == 4) {
                list4.add(map);
            } else if (lv == 5) {
                list5.add(map);
            }
        }

        JSONArray root = new JSONArray();
        for (Map<String, Object> map1 : list1) {
            JSONObject jsonob1 = new JSONObject();
            jsonob1.put("id", (String) map1.get("cid"));
            jsonob1.put("text",
                    org.apache.commons.lang.StringUtils.isBlank((String) map1.get("category")) ? "All Category"
                            : (String) map1.get("category"));

            String path1 = (String) map1.get("path");
            JSONArray jsonarr1 = new JSONArray();
            for (Map<String, Object> map2 : list2) {
                String path2 = (String) map2.get("path");
                if (path2.startsWith(path1 + ",")) {
                    JSONObject jsonob2 = new JSONObject();
                    jsonob2.put("id", (String) map2.get("cid"));
                    jsonob2.put("text", (String) map2.get("category"));

                    JSONArray jsonarr2 = new JSONArray();
                    for (Map<String, Object> map3 : list3) {
                        String path3 = (String) map3.get("path");
                        if (path3.startsWith(path2 + ",")) {
                            JSONObject jsonob3 = new JSONObject();
                            jsonob3.put("id", (String) map3.get("cid"));
                            jsonob3.put("text", (String) map3.get("category"));

                            JSONArray jsonarr3 = new JSONArray();
                            for (Map<String, Object> map4 : list4) {
                                String path4 = (String) map4.get("path");
                                if (path4.startsWith(path3 + ",")) {
                                    JSONObject jsonob4 = new JSONObject();
                                    jsonob4.put("id", (String) map4.get("cid"));
                                    jsonob4.put("text", (String) map4.get("category"));

                                    JSONArray jsonarr4 = new JSONArray();
                                    for (Map<String, Object> map5 : list5) {
                                        String path5 = (String) map5.get("path");
                                        if (path5.startsWith(path4 + ",")) {
                                            JSONObject jsonob5 = new JSONObject();
                                            jsonob5.put("id", (String) map5.get("cid"));
                                            jsonob5.put("text", (String) map5.get("category"));

                                            jsonarr4.add(jsonob5);
                                        }
                                    }

                                    if (jsonarr4.size() > 0) {
                                        jsonob4.put("state", "closed");
                                        jsonob4.put("children", jsonarr4);
                                    }

                                    jsonarr3.add(jsonob4);
                                }
                            }

                            if (jsonarr3.size() > 0) {
                                jsonob3.put("state", "closed");
                                jsonob3.put("children", jsonarr3);
                            }

                            jsonarr2.add(jsonob3);
                        }
                    }
                    if (jsonarr2.size() > 0) {
                        jsonob2.put("state", "closed");
                        jsonob2.put("children", jsonarr2);
                    }

                    jsonarr1.add(jsonob2);
                }
            }
            if (jsonarr1.size() > 0) {
                jsonob1.put("state", "closed");
                jsonob1.put("children", jsonarr1);
            }
            root.add(jsonob1);
        }

        PrintWriter out = response.getWriter();
        out.print(root);
        out.close();
        // DataSourceSelector.restore();
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/addDetail")
    public void addDetail(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "uploadfile", required = true) MultipartFile file) throws Exception {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        TabSeachPagesDetailBean bean = new TabSeachPagesDetailBean();

        String result = null;
        String sid = request.getParameter("detail_sid");
        String name = request.getParameter("detail_name");
        String catid = request.getParameter("addcatid");
        String keyword = request.getParameter("detail_keyword");

        String relateKeyWordUrl = request.getParameter("detail_relateKeyword_url_add");// ??????????????????????????????????????????
        // qiqing
        // 2018/05/11

        String seach_url = request.getParameter("detail_seach_url");
        String anti_words = request.getParameter("detail_anti_words");
        String detail_sort = request.getParameter("detail_sort");
        try {
            // DataSourceSelector.set("dataSource127hop");
            if (tabSeachPageService.getNameCount(name, Integer.parseInt(sid)) > 0) {
                result = "{\"status\":false,\"message\":\"????????????????????????\"}";
            } else {
                // List<Map<String,Object>> goodslist = null;
                // try{
                // goodslist = getGoodslist(seach_url,anti_words);
                // }catch(Exception e){
                //
                // }

                // if(goodslist != null && goodslist.size() >= 4) {
                bean.setSid(Integer.parseInt(sid));
                bean.setName(name);
                bean.setCatid(org.apache.commons.lang.StringUtils.isBlank(catid) ? 0 : Integer.parseInt(catid));
                bean.setKeyword(keyword);
                bean.setRelateKeyWordUrl(relateKeyWordUrl);
                // ???????????????????????????????????????
                bean.setSeachUrl(seach_url);
                bean.setAntiWords(anti_words);
                bean.setSort(org.apache.commons.lang.StringUtils.isNotBlank(detail_sort) ? Integer.parseInt(detail_sort)
                        : null);

                // ????????????
                String detail_banner_name = request.getParameter("detail_banner_name");
                String detail_banner_describe = request.getParameter("detail_banner_describe");
                bean.setBannerName(detail_banner_name);
                bean.setBannerDescribe(detail_banner_describe);

                boolean updateFlag = true;
                String[] imgInfo = SearchFileUtils.comFileUpload(file, sid, null, null, null, 0);
                if (null != imgInfo && imgInfo.length > 0) {
                    //????????????????????????
                    Long imgSize = SearchFileUtils.getImgSize(imgInfo[0]);
                    if (imgSize > 102400) {//???????????????????????????100k
                        result = "{\"status\":false,\"message\":\"?????????????????????100K??????????????????QQ????????????????????????\"}";
                        updateFlag = false;
                    } else {
                        bean.setBannerImgName(imgInfo[0]);
                        bean.setBannerImgUrl(imgInfo[1]);
                    }
                }

                if (updateFlag) {
                    int res = tabSeachPageService.insertDetail(bean);

                    if (res > 0) {
                        insertDetail(bean);
                        result = "{\"status\":true,\"message\":\"???????????????\"}";
                    } else {
                        result = "{\"status\":false,\"message\":\"???????????????\"}";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }


    public void insertDetail(TabSeachPagesDetailBean bean) {


        String name = bean.getName();
        if (StringUtils.isNotBlank(name)) {
            name = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(name);
        } else {
            name = "";
        }


        String keyword = bean.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(keyword);
        } else {
            keyword = "";
        }

        String anti_words = bean.getAntiWords();
        if (StringUtils.isNotBlank(anti_words)) {
            anti_words = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(anti_words);
        } else {
            anti_words = "";
        }

        String banner_img_name = bean.getBannerImgName();
        if (StringUtils.isNotBlank(banner_img_name)) {
            banner_img_name = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(banner_img_name);
        } else {
            banner_img_name = "";
        }

        String banner_name = bean.getBannerName();
        if (StringUtils.isNotBlank(banner_name)) {
            banner_name = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(banner_name);
        } else {
            banner_name = "";
        }

        String banner_describe = bean.getBannerDescribe();
        if (StringUtils.isNotBlank(banner_describe)) {
            banner_describe = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(banner_describe);
        } else {
            banner_describe = "";
        }

        List<String> paramList = new ArrayList<>();
        paramList.add(bean.getSid().toString());
        paramList.add(name);
        paramList.add(bean.getSeachUrl());
        paramList.add(keyword);
        paramList.add(bean.getCatid().toString());
        paramList.add(bean.getRelateKeyWordUrl());
        paramList.add(anti_words);
        paramList.add(bean.getSort().toString());
        paramList.add(banner_img_name);
        paramList.add(bean.getBannerImgUrl());
        paramList.add(banner_name);
        paramList.add(banner_describe);


        String sql = "insert into tab_seach_pages_details(sid,name,seach_url,keyword,catid,relateKeyWordUrl,anti_words,sort," +
                " banner_img_name, banner_img_url, banner_name, banner_describe)" +
                " values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";

        NotifyToCustomerUtil.sendSqlByMq(String.format(sql, paramList));
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/detailList")
    public void detailList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sid = request.getParameter("sid");
        List<TabSeachPagesDetailBean> list = new ArrayList<TabSeachPagesDetailBean>();
        try {
            // DataSourceSelector.set("dataSource127hop");
            list = tabSeachPageService.detailList(Integer.parseInt(sid));
            if (null != list && list.size() > 0) {
                for (TabSeachPagesDetailBean bean : list) {
                    if (StringUtils.isNotBlank(bean.getBannerImgName())) {
                        bean.setBannerImgName(SearchFileUtils.IMAGEHOSTURL + bean.getBannerImgName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONArray.toJSON(list));
        out.close();
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/deleteDetail")
    public void deleteDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String id = request.getParameter("id");
        String result = null;
        int res = 1;
        try {

            res = tabSeachPageService.deleteDetail(Integer.parseInt(id));
            if (res > 0) {
                SendMQ.sendMsg(new RunSqlModel("update tab_seach_pages_details set sate=1 where id='" + id + "'"));
                result = "{\"status\":true,\"message\":\"???????????????\"}";
            } else {
                result = "{\"status\":false,\"message\":\"???????????????\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ??????Banner
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/deleteBanner")
    public void deleteBanner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String id = request.getParameter("id");
        String result = null;
        int res = 1;
        try {

            res = tabSeachPageService.deleteBanner(Integer.parseInt(id));

            if (res > 0) {
                SendMQ.sendMsg(new RunSqlModel("update tab_seach_pages_details set banner_img_name = null, banner_img_url = null, banner_name = null, banner_describe = null where id='" + id + "'"));
                result = "{\"status\":true,\"message\":\"???????????????\"}";
            } else {
                result = "{\"status\":false,\"message\":\"???????????????\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/getDetail")
    public void getDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        TabSeachPagesDetailBean bean = new TabSeachPagesDetailBean();
        try {
            // DataSourceSelector.set("dataSource127hop");
            String id = request.getParameter("id");
            bean = tabSeachPageService.getDetail(Integer.parseInt(id));
            if (StringUtils.isNotBlank(bean.getBannerImgName())) {
                bean.setBannerImgName(SearchFileUtils.IMAGEHOSTURL + bean.getBannerImgName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(bean));
        out.close();
    }

    @RequestMapping("/preUpdateDetail")
    public void preUpdateDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        TabSeachPagesDetailBean bean = new TabSeachPagesDetailBean();
        try {
            // DataSourceSelector.set("dataSource127hop");
            String id = request.getParameter("id");
            bean = tabSeachPageService.getDetail(Integer.parseInt(id));
            if (StringUtils.isNotBlank(bean.getBannerImgName())) {
                bean.setBannerImgName(SearchFileUtils.IMAGEHOSTURL + bean.getBannerImgName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(bean));
        out.close();
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/updateDetail")
    public void updateDetail(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "uploadfile", required = true) MultipartFile file) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String result = null;
        String id = request.getParameter("detail_update_id");
        String sid = request.getParameter("detail_update_sid");
        String name = request.getParameter("detail_update_name");
        String catid = request.getParameter("updatecatid");
        String keyword = request.getParameter("detail_update_keyword");
        String relateKeyWordUrl = request.getParameter("detail_relateKeyword_url");// ??????????????????????????????????????????
        // qiqing
        // 2018/05/11
        String seach_url = request.getParameter("detail_update_seach_url");
        String anti_words = request.getParameter("detail_update_anti_words");
        String detail_update_sort = request.getParameter("detail_update_sort");
        try {
            // DataSourceSelector.set("dataSource127hop");
            if (tabSeachPageService.getNameCount1(name, Integer.parseInt(sid), Integer.parseInt(id)) > 0) {
                result = "{\"status\":false,\"message\":\"????????????????????????\"}";
            } else {
                List<Map<String, Object>> goodslist = null;
                try {

                    // goodslist = getGoodslist(seach_url,anti_words);
                    // if(goodslist != null && goodslist.size() >= 4) {
                    TabSeachPagesDetailBean bean = new TabSeachPagesDetailBean();

                    // ????????????
                    String detail_banner_name = request.getParameter("detail_banner_name");
                    String detail_banner_describe = request.getParameter("detail_banner_describe");
                    bean.setBannerName(detail_banner_name);
                    bean.setBannerDescribe(detail_banner_describe);

                    boolean updateFlag = true;
                    String[] imgInfo = SearchFileUtils.comFileUpload(file, sid, null, null, null, 0);
                    if (null != imgInfo && imgInfo.length > 0) {
                        //????????????????????????
                        Long imgSize = SearchFileUtils.getImgSize(imgInfo[0]);
                        if (imgSize > 102403) {//???????????????????????????100k
                            result = "{\"status\":false,\"message\":\"?????????????????????100K??????????????????QQ????????????????????????\"}";
                            updateFlag = false;
                        } else {
                            bean.setBannerImgName(imgInfo[0]);
                            bean.setBannerImgUrl(imgInfo[1]);
                        }
                    }
                    if (updateFlag) {
                        bean.setId(Integer.parseInt(id));
                        bean.setSid(Integer.parseInt(sid));
                        bean.setName(name);
                        bean.setCatid(org.apache.commons.lang.StringUtils.isBlank(catid) ? 0 : Integer.parseInt(catid));
                        bean.setKeyword(keyword);
                        bean.setRelateKeyWordUrl(relateKeyWordUrl);
                        bean.setSeachUrl(seach_url);
                        bean.setAntiWords(anti_words);
                        bean.setSort(org.apache.commons.lang.StringUtils.isNotBlank(detail_update_sort) ? Integer
                                .parseInt(detail_update_sort) : null);

                        String sql = "update tab_seach_pages_details set name='" + SendMQ.repCha(bean.getName()) + "',catid='"
                                + bean.getCatid() + "',relateKeyWordUrl='" + SendMQ.repCha(bean.getRelateKeyWordUrl()) + "',"
                                + "seach_url='" + SendMQ.repCha(bean.getSeachUrl()) + "',keyword='"
                                + SendMQ.repCha(bean.getKeyword()) + "',anti_words='" + SendMQ.repCha(bean.getAntiWords())
                                + "',sort='" + bean.getSort();
                        if (!file.isEmpty()) {
                            sql += "',banner_img_name='" + bean.getBannerImgName() + "',banner_img_url='"
                                    + bean.getBannerImgUrl();
                        }
                        sql += "',banner_name='" + SendMQ.repCha(bean.getBannerName()) + "',banner_describe='"
                                + SendMQ.repCha(bean.getBannerDescribe()) + "' where id='" + bean.getId() + "'";
                        int res = tabSeachPageService.updateDetail(bean);
                        if (res > 0) {
                            SendMQ.sendMsg(new RunSqlModel(sql));
                            result = "{\"status\":true,\"message\":\"???????????????\",\"id\":\"" + id + "\"}";
                        } else {
                            result = "{\"status\":false,\"message\":\"???????????????\"}";
                        }

                    }
                } catch (Exception e) {
                    result = "{\"status\":false,\"message\":\"???????????????\"}";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * ?????????????????? ?????? ??????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/updateAuthorizedInfo")
    @ResponseBody
    public Map<String, Object> updateAuthorizedInfo(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestParam(value = "uploadfile", required = true) MultipartFile file) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer adminId;
        String admName;
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user != null) {
            adminId = user.getId(); //??????id
            admName = user.getAdmName();
        } else {
            result.put("status", false);
            result.put("message", "noLogin");
            return result;
        }
        String id = request.getParameter("authorized_id");
        String shopId = request.getParameter("authorized_shop_id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String remark = request.getParameter("authorized_remark");
        String shopBrand = request.getParameter("authorized_shop_brand");
        ShopUrlAuthorizedInfoPO bean = new ShopUrlAuthorizedInfoPO(StringUtils.isBlank(id) ? null : Long.parseLong(id), shopId, adminId, admName,
                StringUtils.isNotBlank(startTime) ? DATEFORMAT.parse(startTime) : null,
                StringUtils.isNotBlank(endTime) ? DATEFORMAT.parse(endTime) : null, remark, shopBrand);

        String[] imgInfo = SearchFileUtils.comFileUpload(file, "AuthorizedFile", null, null, null, 0);
        if (null != imgInfo && imgInfo.length > 0) {
            bean.setFileUrl(imgInfo[0]);
            bean.setImgFileUrl(imgInfo[1]);
            bean.setFileName(imgInfo[2]);
        }
        long count = 0L;
        try {
            DataSourceSelector.set("dataSource28hop");
            count = tabSeachPageService.updateAuthorizedInfo(bean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataSourceSelector.restore();
        }
        if (count > 0) {
            result.put("status", true);
            result.put("message", "???????????????");
            result.put("bean", bean);
        } else {
            result.put("status", false);
            result.put("message", "???????????????");
        }
        return result;
    }

    /**
     * ly  2018/12/05 16:49
     * ???????????? ????????? ????????????????????????
     *
     * @param request
     */
    @RequestMapping("/deleteAuthorizedInfo")
    @ResponseBody
    public Map<String, Object> deleteAuthorizedInfo(HttpServletRequest request, String shopId) {
        Map<String, Object> result = new HashMap<String, Object>();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        if (userJson == null) {
            result.put("status", false);
            result.put("message", "?????????,????????????????????????!");
            return result;
        }
        long count = 0L;
        try {
            DataSourceSelector.set("dataSource28hop");
            count = tabSeachPageService.updateAuthorizedInfoValid(shopId, 3);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataSourceSelector.restore();
        }
        if (count > 0) {
            result.put("status", true);
            result.put("message", "???????????????");
        } else {
            result.put("status", false);
            result.put("message", "???????????????");
        }
        return result;
    }

    /**
     * ?????????????????? ??????????????????
     * http://192.168.1.57:8086/cbtconsole/tabseachpage/queryAuthorizedInfo?shopId=
     */
    @RequestMapping("/queryAuthorizedInfo")
    @ResponseBody
    public Map<String, Object> queryAuthorizedInfo(String shopId) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(shopId)) {
                result.put("status", false);
                result.put("message", "????????????");
                return result;
            }
            ShopUrlAuthorizedInfoPO bean = new ShopUrlAuthorizedInfoPO();
            try {
                DataSourceSelector.set("dataSource28hop");
                bean = tabSeachPageService.queryAuthorizedInfo(shopId);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DataSourceSelector.restore();
            }

            if (null == bean) {
                result.put("status", false);
                result.put("message", "?????????");
            } else {
                if (StringUtils.isNotBlank(bean.getFileUrl())) {
                    bean.setFileUrl(SearchFileUtils.IMAGEHOSTURL + bean.getFileUrl());
                }
                result.put("status", true);
                result.put("bean", bean);
            }
        } catch (Exception e) {
            System.out.println(e);
            result.put("status", false);
            result.put("message", "????????????");
        }
        return result;
    }


    /**
     * ?????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/preview")
    public void preview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sid = request.getParameter("sid");
        String result = "";
        try {
            // DataSourceSelector.set("dataSource127hop");
            List<TabSeachPagesDetailBean> list = tabSeachPageService.detailList(Integer.parseInt(sid));
            if (list != null && list.size() > 0) {
                for (TabSeachPagesDetailBean bean : list) {
                    if (StringUtils.isNotBlank(bean.getBannerImgName())) {
                        bean.setBannerImgName(SearchFileUtils.IMAGEHOSTURL + bean.getBannerImgName());
                    }
                }
                result = "{\"status\":true}";
            } else {
                result = "{\"status\":false,\"message\":\"????????????????????????????????????????????????????????????\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/staticize")
    public void staticize(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sid = request.getParameter("sid");
        String keyword = request.getParameter("keyword");
        String webSite = request.getParameter("webSite");
        String result = "";
        try {
            // DataSourceSelector.set("dataSource127hop");
            List<TabSeachPagesDetailBean> list = tabSeachPageService.detailList(Integer.parseInt(sid));
            if (list != null && list.size() > 0) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sid", sid));
                params.add(new BasicNameValuePair("keyword", keyword));
                params.add(new BasicNameValuePair("webSite", webSite));
                // String url = "http://192.168.1.65:8383/CbtStaticize/tabseachpage/staticize.do";
                String url = cbtstaticizePath + "/tabseachpage/staticize.do";
                result = getContentClientPost(url, params);
            } else {
                result = "{\"status\":false,\"message\":\"????????????????????????????????????????????????????????????\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }

        if (StringUtil.isBlank(result)) {
            result = "{\"status\":false,\"message\":\"???????????????????????????\"}";
        }

        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ???????????????????????? ??????
     * ?????????????????????????????????
     * http://localhost:8086/cbtconsole/tabseachpage/staticizeAll
     */
    @RequestMapping("/staticizeAll")
    public void staticizeAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String result = null;
        // ??????????????????????????????
        List<TabSeachPageBean> list = new ArrayList<TabSeachPageBean>();
        try {
            String refRes;
            for (String webSite : MultiSiteUtil.webSiteMap.keySet()) {
                refRes = HttpUtil.doGet(MultiSiteUtil.webSiteMap.get(webSite) + "/seachpage/clear.do", "success", 3);
                System.out.println(webSite + " message1" + "?????????????????????????????????????????????(??????????????????):" + refRes);
                refRes = HttpUtil.doGet(MultiSiteUtil.webSiteMap.get(webSite) + "/app/rlevel.do", "status\":\"1", 3);
                System.out.println(webSite + " message2" + "?????????????????????????????????????????????(????????????):" + refRes);
            }

            // DataSourceSelector.set("dataSource127hop");
            list = tabSeachPageService.queryStaticizeAll();
            // DataSourceSelector.restore();
            if (list == null || list.size() == 0) {
                result = "{\"status\":false,\"message\":\"???????????????????????????!\"}";
                return;
            }
            int total = 0;
            for (TabSeachPageBean bean : list) {
                if (bean.getWebSite() > 0) {
                    total++;
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sid", bean.getId() + ""));
                    params.add(new BasicNameValuePair("keyword", bean.getKeyword()));
                    params.add(new BasicNameValuePair("webSite", String.valueOf(bean.getWebSite())));
                    // String url = "http://192.168.1.67:8383/CbtStaticize/tabseachpage/staticize.do";
                    String url = cbtstaticizePath + "/tabseachpage/staticize.do";
                    result = getContentClientPost(url, params, "\"status\":true", 3);
                }
            }
            list.clear();
            result = "{\"status\":true,\"message\":\"???????????????????????????, ?????????:" + total + "\"}";

            if (StringUtil.isBlank(result)) {
                result = "{\"status\":false,\"message\":\"???????????????????????????\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ????????????????????????title keywords description
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/createTitleAndKey")
    public void createTitleAndKey(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sid = request.getParameter("sid");
        String result;

        try {
            // DataSourceSelector.set("dataSource127hop");
            boolean boo = tabSeachPageService.updateTitleAndKey(Integer.parseInt(sid));
            if (boo) {
                result = "{\"status\":true,\"message\":\"????????????\"}";
            } else {
                result = "{\"status\":false,\"message\":\"????????????????????????\"}";
            }
        } catch (Exception e) {
            LOG.error(e.toString());
            result = "{\"status\":false,\"message\":\"????????????\"}";
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/isshow")
    public void isshow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String id = request.getParameter("id");
        String isshow = request.getParameter("isshow");
        String result = "";
        try {
            // DataSourceSelector.set("dataSource127hop");
            TabSeachPageBean bean = tabSeachPageService.get(Integer.parseInt(id));
            if (StringUtil.isNotBlank(bean.getFilename())) {
                int res = tabSeachPageService.updateIsshow(Integer.parseInt(isshow), Integer.parseInt(id));
                if (res > 0) {
                    String refRes;
                    for (String webSite : MultiSiteUtil.webSiteMap.keySet()) {
                        refRes = HttpUtil.doGet(MultiSiteUtil.webSiteMap.get(webSite) + "/app/rlevel.do", "status\":\"1", 3);
                        System.out.println(webSite + " message2" + "?????????????????????????????????????????????(????????????):" + refRes);
                    }
                    if ("1".equals(isshow)) {
                        result = "{\"status\":true,\"message\":\"????????????\"}";
                    } else {
                        result = "{\"status\":true,\"message\":\"????????????\"}";
                    }
                } else {
                    if ("1".equals(isshow)) {
                        result = "{\"status\":false,\"message\":\"????????????\"}";
                    } else {
                        result = "{\"status\":false,\"message\":\"????????????\"}";
                    }
                }
            } else {
                result = "{\"status\":false,\"message\":\"????????????????????????????????????\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataSourceSelector.restore();
        }
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSON(result));
        out.close();
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param url
     * @param unkey
     * @return
     */
    public static List<Map<String, Object>> getGoodslist(String url, String unkey) {

        String text = null;
        if (url.endsWith(".html")) {
            String str = url.substring(url.lastIndexOf("/") + 1).replace(".html", "");
            String keyword = str.substring(0, str.lastIndexOf("-"));
            String catid = str.substring(str.lastIndexOf("-") + 1);

            keyword = keyword.toLowerCase();
            try {
                keyword = URLEncoder.encode(keyword, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (keyword != null && !"".equals(keyword)) {
                keyword = keyword.replace(" ", "%20");
            }

            url = SearchFileUtils.importexpressPath + "/goodslistInterface?keyword=" + keyword + "&catid=" + catid;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if (StringUtil.isNotBlank(unkey)) {
                params.add(new BasicNameValuePair("unkey", unkey.replace(";", ",")));
            }
            text = getContentClientPost(url, params);
        } else {
            url = url.replace("goodslist", "goodslistInterface");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if (StringUtil.isNotBlank(unkey)) {
                params.add(new BasicNameValuePair("unkey", unkey.replace(";", ",")));
            }
            String[] split = url.split("&");
            StringBuilder sb = new StringBuilder();
            int j = 1;
            if (url.contains("unkey")) {
                j = 2;
            }
            for (int i = 0; i < split.length; i++) {
                if (org.apache.commons.lang.StringUtils.isBlank(split[i])) {
                    continue;
                }
                if (split[i].contains("unkey")) {
                    continue;
                } else if (i == split.length - j) {
                    sb.append(split[i]);
                } else {
                    sb.append(split[i]).append("&");
                }
            }
            url = sb.toString();
            text = getContentClientPost(url, params);
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (StringUtil.isNotBlank(text)) {
            JSONArray jsonarr = JSONArray.parseArray(text);
            for (int i = 0; i < jsonarr.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonob = jsonarr.getJSONObject(i);
                map.put("goods_name", jsonob.getString("goods_name"));
                map.put("goods_image", jsonob.getString("goods_image"));
                String goods_url = jsonob.getString("goods_url");
                if (!goods_url.startsWith("http")) {
                    goods_url = SearchFileUtils.importexpressPath + goods_url;
                }
                map.put("goods_url", goods_url);
                map.put("goods_price", jsonob.getString("goods_price"));
                map.put("goodsPriceUnit", jsonob.getString("goodsPriceUnit"));
                list.add(map);
            }
        }

        return list;
    }

    /**
     * ??????????????????
     *
     * @param urls
     * @param params
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getContentClientPost(String urls, List<NameValuePair> params) {
        java.util.logging.Logger.getLogger("org.apache.http.client.protocol").setLevel(java.util.logging.Level.OFF);
        if (urls == null || urls.isEmpty()) {
            return "";
        }
        String co = "";
        // HttpClient4.1?????????????????????????????????
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response;
        try {
            String url = urls.replaceAll("\\s", "%20");
            // ????????????
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 60 * 1000);
            // ????????????
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5 * 60 * 1000);
            HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            response = client.execute(httpPost);

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStreamReader in = null;
                    if (Pattern.compile("(taobao)|(tmall)|(1688)").matcher(urls).find()) {
                        in = new InputStreamReader(entity.getContent(), "gbk");
                    } else {
                        in = new InputStreamReader(entity.getContent(), HTTP.UTF_8);
                    }
                    BufferedReader reader = new BufferedReader(in);
                    co = IOUtils.toString(reader);
                    in.close();
                    reader.close();
                    in = null;
                    reader = null;
                }
                entity = null;
                httpPost = null;
            }
        } catch (ClientProtocolException e) {
            co = "";
        } catch (IOException e) {
            co = "";
        } finally {
            if (client != null) {
                client.close();
            }
            response = null;
        }
        return co;
    }

    public static String getContentClientPost(String urls, List<NameValuePair> params, String contains, Integer num) {
        String refRes = getContentClientPost(urls, params);
        if (refRes.contains(contains)) {
            return refRes;
        } else {
            return getContentClientPost(urls, params, contains, --num);
        }
    }

    /**
     * ??????????????????????????????????????????
     */
    public static String filterKeyWord(String keyword) {
        keyword = keyword.indexOf("<") == 0 ? keyword.substring(1) : keyword;
        if (keyword.length() > 50)
            keyword = keyword.substring(0, 50);
        return keyword.toLowerCase().replaceAll("\"", " ").replaceAll("!", " ").replaceAll("@", " ")
                .replaceAll("#", " ").replaceAll("$", " ").replaceAll("%", " ").replaceAll("\\^", " ")
                .replaceAll("\\s+&\\s+", "&").replaceAll("\\*", " ").replaceAll(",", " ").replaceAll("\\.", " ")
                .replaceAll("/", " ")// ??????
                .replaceAll("\\u005C", " ")// ?????????
                .replaceAll("\\u007C", " ")// ??????|
                .replaceAll(":", " ")// ??????
                .replaceAll("\\u005B", " ")// ????????????[
                .replaceAll("\\u005D", " ")// ????????????]
                .replaceAll("\\u005B", " ")// ????????????[
                .replaceAll("\\u005D", " ")// ????????????]
                .replaceAll("\\u007B", " ")// ????????????{
                .replaceAll("\\u007D", " ")// ????????????}
                .replaceAll("\\n", " ")// ??????
                .replaceAll("\\r", " ")// ??????
                .replaceAll("\\t", " ")// tab
                .replaceAll("\\v", " ")// ???????????????
                .trim().replaceAll("\\s+", "-");
    }

}
