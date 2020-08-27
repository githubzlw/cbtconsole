package com.importExpress.utli;

import com.importExpress.pojo.Product;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importexpress.utils
 * @date:2020/2/14
 */
public class SolrProductUtils {

    private static final String SOLR_URL_REMOTE = "http://52.34.56.133:9001/solr";
    private static final String SOLR_URL_LOCAL = "http://192.168.1.31:8984/solr";


    public static List<Product> getSolrKidsProducts(Integer site, boolean isOnline) {

        List<Product> productList = null;
        int count = 0;
        while (CollectionUtils.isEmpty(productList) && count < 5) {
            productList = getSolrData(site, isOnline);
            count ++;
        }
        return productList;
    }

    private static List<Product> getSolrData(Integer site, boolean isOnline) {
        List<Product> productList = new ArrayList<Product>();
        Set<String> pidSet = new HashSet<String>();  // 去重
        int rows = 2000;  // 每次读取条数
        int page = 0;  // 页码
        String url;
        if(isOnline){
            url = SOLR_URL_REMOTE;
        } else {
            url = SOLR_URL_LOCAL;
        }
        int total = 0;
        while (true) {
            System.out.println("从solr获取商品数据 page " + page + " 每页条数 rows " + rows);
            String solrUrl = url + "/product/select?fl=custom_pid,custom_main_image&q=*:*&rows="
                    + rows + "&start=" + (page++ * rows) + "&wt=csv";
            if (site == 2) {  // kids网站的
                solrUrl += "&fq=custom_path_catid:%22311%22%20OR%20custom_path_catid:%22125386001%22%20OR%20custom_path_catid:%22126128002%22%20OR%20custom_path_catid:%221813%22%20OR%20custom_path_catid:%221037002%22%20OR%20custom_path_catid:%221501%22%20OR%20custom_path_catid:%22125296002%22%20OR%20custom_path_catid:%22123184002%22%20OR%20custom_path_catid:%22122110001%22%20OR%20custom_path_catid:%221752%22";
            } else if (site == 3) {
                solrUrl += "&fq=custom_path_catid:%22122916001%22";
            }

            String res = HttpClientUtil.doGet(solrUrl);
            for (String line : res.split("\n")) {
                if ("custom_pid,custom_main_image".equals(line)) {
                    continue;
                }
                String[] lineArr = line.split(",");
                if (!pidSet.contains(lineArr[0])) {
                    pidSet.add(lineArr[0]);
                    productList.add(new Product(lineArr[0], lineArr[1]));  // 每行的对应数据
                    total ++;
                }
            }
            if ("custom_pid,custom_main_image\n".equals(res)) {  // 最后一页了
                break;
            }
            if(total > 0){
                // break;
            }
        }
        return productList;
    }
}
