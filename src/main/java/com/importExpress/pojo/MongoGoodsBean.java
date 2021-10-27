package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: MongoDB中的商品Bean
 * @date:2019/11/22
 */
@Data
public class MongoGoodsBean {

    /*private _id _id;

    public class _id {
        private String $oid;

        public void set$oid(String $oid) {
            this.$oid = $oid;
        }

        public String get$oid() {
            return this.$oid;
        }

        @Override
        public String toString() {
            return this.$oid;
        }
    }*/

    private String pid;
    private String shop_id;
    private String catid1;
    private Integer valid;
    private Integer goodsstate;
    private Integer from_flag;
    private String enname;
    private String name;

    private String update_time;
    private String publish_time;
    private Integer admin_id;
    private String is_edited;

    private String off_reason;
    private String weight;
    private String final_weight;

    private String custom_main_image;
    private String localpath;
    private String remotpath;

    private String keyword;
    private String max_price;

    private String price_1688;
    private String unsellableReason;

    private String weight_flag;
    private String bm_flag;
    private String isBenchmark;
    private String source_pro_flag;
    private String source_used_flag;
    private String ocr_match_flag;

    private String infringing_flag;
    private String priority_flag;
    private String rebid_flag;
    private String is_sold_flag;
    private String is_add_car_flag;
    private String is_abnormal;


    private Integer click_num = 0;
    private Integer review_count = 0;
    private Integer complain_count = 0;

    private String weight_check;
    private String ali_name;
    private String ali_img;
    private String ali_pid;
    private String ali_price;

    /**
     * 类别总数
     */
    private Integer category_total = 0;

    private String sku;

    private Integer sold_flag;
    private Integer searchable;
    private Integer describe_good_flag;

    private String brand_name;

}
