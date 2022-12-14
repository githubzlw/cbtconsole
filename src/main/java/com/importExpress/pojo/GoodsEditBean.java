package com.importExpress.pojo;

import org.apache.commons.lang3.StringUtils;

public class GoodsEditBean {

    private int id;
    private String pid;
    private String old_title;
    private String new_title;
    private int admin_id;
    private String admin_name;
    private int weight_flag;//重量不合理标注
    private int ugly_flag;//难看标识
    private int repaired_flag;//修复标识
    private int benchmarking_flag;//对标标记
    private int describe_good_flag;//描述很精彩标识
    private int uniqueness_flag;
    private int never_off_flag;
    private int publish_flag;
    private String create_time;
    private int is_edited;
    private String off_time;
    private int off_flag;
    private String publish_time;
    private double price_1688;
    private int search_num;
    private int click_num;
    private int weight_is_edit;
    private int limitNum;
    private int startNum;
    private int promotion_flag;
    private String price_old;
    private String wprice_old;
    private String feeprice_old;
    private String range_price_old;
    private String weight_old;
    private String revise_weight_old;
    private String final_weight_old;
    private String fprice_str_old;

    private String price_new;
    private String wprice_new;
    private String feeprice_new;
    private String range_price_new;
    private String weight_new;
    private String revise_weight_new;
    private String final_weight_new;
    private String fprice_str_new;

    private int priceShowFlag;
    private int weightShowFlag;

    public int getPriceShowFlag() {
        return priceShowFlag;
    }

    public void setPriceShowFlag(int priceShowFlag) {
        this.priceShowFlag = priceShowFlag;
    }

    public int getWeightShowFlag() {
        return weightShowFlag;
    }

    public void setWeightShowFlag(int weightShowFlag) {
        this.weightShowFlag = weightShowFlag;
    }

    public String getFprice_str_old() {
        return fprice_str_old;
    }

    public void setFprice_str_old(String fprice_str_old) {
        this.fprice_str_old = fprice_str_old;
    }

    public String getFprice_str_new() {
        return fprice_str_new;
    }

    public void setFprice_str_new(String fprice_str_new) {
        this.fprice_str_new = fprice_str_new;
        if(StringUtils.isNotBlank(fprice_str_new)){
            priceShowFlag ++;
        }
    }

    public String getPrice_old() {
        return price_old;
    }

    public void setPrice_old(String price_old) {
        this.price_old = price_old;
    }

    public String getWprice_old() {
        return wprice_old;
    }

    public void setWprice_old(String wprice_old) {
        this.wprice_old = wprice_old;
    }

    public String getFeeprice_old() {
        return feeprice_old;
    }

    public void setFeeprice_old(String feeprice_old) {
        this.feeprice_old = feeprice_old;
    }

    public String getRange_price_old() {
        return range_price_old;
    }

    public void setRange_price_old(String range_price_old) {
        this.range_price_old = range_price_old;
    }

    public String getWeight_old() {
        return weight_old;
    }

    public void setWeight_old(String weight_old) {
        this.weight_old = weight_old;
    }

    public String getRevise_weight_old() {
        return revise_weight_old;
    }

    public void setRevise_weight_old(String revise_weight_old) {
        this.revise_weight_old = revise_weight_old;
    }

    public String getFinal_weight_old() {
        return final_weight_old;
    }

    public void setFinal_weight_old(String final_weight_old) {
        this.final_weight_old = final_weight_old;
    }

    public String getPrice_new() {
        return price_new;
    }

    public void setPrice_new(String price_new) {
        this.price_new = price_new;
        if(StringUtils.isNotBlank(price_new)){
            priceShowFlag ++;
        }
    }

    public String getWprice_new() {
        return wprice_new;
    }

    public void setWprice_new(String wprice_new) {
        this.wprice_new = wprice_new;
        if(StringUtils.isNotBlank(wprice_new)){
            priceShowFlag ++;
        }
    }

    public String getFeeprice_new() {
        return feeprice_new;
    }

    public void setFeeprice_new(String feeprice_new) {
        this.feeprice_new = feeprice_new;
        if(StringUtils.isNotBlank(feeprice_new)){
            priceShowFlag ++;
        }
    }

    public String getRange_price_new() {
        return range_price_new;
    }

    public void setRange_price_new(String range_price_new) {
        this.range_price_new = range_price_new;
        if(StringUtils.isNotBlank(range_price_new)){
            priceShowFlag ++;
        }
    }

    public String getWeight_new() {
        return weight_new;
    }

    public void setWeight_new(String weight_new) {
        this.weight_new = weight_new;
        if(StringUtils.isNotBlank(weight_new)){
            weightShowFlag ++;
        }
    }

    public String getRevise_weight_new() {
        return revise_weight_new;
    }

    public void setRevise_weight_new(String revise_weight_new) {
        this.revise_weight_new = revise_weight_new;
        if(StringUtils.isNotBlank(revise_weight_new)){
            weightShowFlag ++;
        }
    }

    public String getFinal_weight_new() {
        return final_weight_new;
    }

    public void setFinal_weight_new(String final_weight_new) {
        this.final_weight_new = final_weight_new;
        if(StringUtils.isNotBlank(final_weight_new)){
            weightShowFlag ++;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOld_title() {
        return old_title;
    }

    public void setOld_title(String old_title) {
        this.old_title = old_title;
    }

    public String getNew_title() {
        return new_title;
    }

    public void setNew_title(String new_title) {
        this.new_title = new_title;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public int getWeight_flag() {
        return weight_flag;
    }

    public void setWeight_flag(int weight_flag) {
        this.weight_flag = weight_flag;
    }

    public int getUgly_flag() {
        return ugly_flag;
    }

    public void setUgly_flag(int ugly_flag) {
        this.ugly_flag = ugly_flag;
    }

    public int getBenchmarking_flag() {
        return benchmarking_flag;
    }

    public void setBenchmarking_flag(int benchmarking_flag) {
        this.benchmarking_flag = benchmarking_flag;
    }

    public int getDescribe_good_flag() {
        return describe_good_flag;
    }

    public void setDescribe_good_flag(int describe_good_flag) {
        this.describe_good_flag = describe_good_flag;
    }

    public int getUniqueness_flag() {
        return uniqueness_flag;
    }

    public void setUniqueness_flag(int uniqueness_flag) {
        this.uniqueness_flag = uniqueness_flag;
    }

    public int getNever_off_flag() {
        return never_off_flag;
    }

    public void setNever_off_flag(int never_off_flag) {
        this.never_off_flag = never_off_flag;
    }

    public int getPublish_flag() {
        return publish_flag;
    }

    public void setPublish_flag(int publish_flag) {
        this.publish_flag = publish_flag;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOff_time() {
        return off_time;
    }

    public void setOff_time(String off_time) {
        this.off_time = off_time;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public double getPrice_1688() {
        return price_1688;
    }

    public void setPrice_1688(double price_1688) {
        this.price_1688 = price_1688;
    }

    public int getSearch_num() {
        return search_num;
    }

    public void setSearch_num(int search_num) {
        this.search_num = search_num;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }

    public int getIs_edited() {
        return is_edited;
    }

    public void setIs_edited(int is_edited) {
        this.is_edited = is_edited;
    }

    public int getWeight_is_edit() {
        return weight_is_edit;
    }

    public void setWeight_is_edit(int weight_is_edit) {
        this.weight_is_edit = weight_is_edit;
    }

    public int getOff_flag() {
        return off_flag;
    }

    public void setOff_flag(int off_flag) {
        this.off_flag = off_flag;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }


    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getRepaired_flag() {
        return repaired_flag;
    }

    public void setRepaired_flag(int repaired_flag) {
        this.repaired_flag = repaired_flag;
    }

    public int getPromotion_flag() {
        return promotion_flag;
    }

    public void setPromotion_flag(int promotion_flag) {
        this.promotion_flag = promotion_flag;
    }

    @Override
    public String toString() {
        return "GoodsEditBean{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", old_title='" + old_title + '\'' +
                ", new_title='" + new_title + '\'' +
                ", admin_id=" + admin_id +
                ", weight_flag=" + weight_flag +
                ", ugly_flag=" + ugly_flag +
                ", benchmarking_flag=" + benchmarking_flag +
                ", describe_good_flag=" + describe_good_flag +
                ", uniqueness_flag=" + uniqueness_flag +
                ", never_off_flag=" + never_off_flag +
                ", publish_flag=" + publish_flag +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
