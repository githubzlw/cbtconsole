package com.importExpress.pojo;

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
