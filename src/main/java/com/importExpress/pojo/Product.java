package com.importExpress.pojo;

import lombok.Data;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luohao
 * @String 2018/11/28
 */
@Data
public class Product {

    private long _id;
    private String catid1;
    private String path_catid;
    private String pid;
    private String price;
    private String wprice;
    private String custom_main_image;
    private String img;
    private String weight;
    private String feeprice;
    private String fprice;
    private String name;
    private String enname;
    private String morder;
    private String entype;
    private String sku;
    private String endetail;
    private String eninfo;
    private String ali_sold;
    private String ali_pid;
    private String ali_price;
    private String ali_weight;
    private String ali_freight;
    private String ali_sellunit;
    private String ali_morder;
    private String ali_unit;
    private String ali_name;
    private String remotpath;
    private String valid;
    private String localpath;
    private String createtime;
    private String catid;
    private String catidparenta;
    private String catidparentb;
    private String keyword;
    private String sold;
    private String catidb;
    private String catpath;
    private String originalcatid;
    private String originalcatpath;
    private String ali_img;
    private String img_check;
    private String revise_weight;
    private String final_weight;
    private String range_price;
    private String shop_id;
    private String wholesale_price;
    private String fprice_str;
    private String pvids;
    private String infoReviseFlag;
    private String priceReviseFlag;
    private String isBenchmark;
    private String isNewCloud;
    private String finalName;
    private String sellunit;
    private String cur_time;
    private String bm_flag;
    private String source_pro_flag;
    private String is_sold_flag;
    private String priority_flag;
    private String is_add_car_flag;
    private String source_used_flag;
    private String ocr_match_flag;
    private String is_show_det_img_flag;
    private String is_show_det_table_flag;
    private String flag;
    private String goodsstate;
    private String beforesku;
    private String is_stock_flag;
    private String unsellableReason;
    private String samplingStatus;
    private String sendFrom;
    private String matchSource;
    private String updateDate;
    private String validationDate;
    private String best_match;
    private String ocean_price;
    private String validationDealDate;
    private String video_url;
    private String average_deliver_time;
    private String core_flag;
    private String score;
    private String is_edited;
    private String _class;
    private String newSku;

    public Product(Document document) {
        this.pid = String.valueOf(document.get("pid"));
        this.enname = String.valueOf(document.get("enname"));
    }

    public Product(String pid, String custom_main_image) {
        this.pid = pid;
        this.custom_main_image =custom_main_image;
    }

    public Product(String pid, String remotpath, String localpath) {
        this.pid = pid;
        this.remotpath = remotpath;
        this.localpath = localpath;
    }

    public static List<String> getPidList(List<Product> proList){
        List<String> resList = new ArrayList<String>();
        for (Product bean : proList) {
            resList.add(bean.getPid());
        }
        return resList;
    }
}
