package com.importExpress.pojo;


/**
 * mongodb 更新json数据对象
 */
public class InputData {

    private String id;
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
    private String cur_time;//若更新solr  该字段一定要设置
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
    private Character crud;//u表示更新；c表示创建，d表示删除
    private String rw_keyword;
    private String size_info_en;
    private String custom_video_flag;
    private String entype_new ;//新的尺码表
    /**
     * 描述很精彩标识
     */
    private String describe_good_flag;

    private String volume_weight;

    private String searchable;

    private String salable;
    private String is_simplify;
    private String top_sort;


    private String free_price_new;
    private String range_price_free_new;
    private String sku_new;
    private String price_import;
    private String price_kids;
    private String price_pets;

    private String brand_name;

    public String getFree_price_new() {
        return free_price_new;
    }

    public void setFree_price_new(String free_price_new) {
        this.free_price_new = free_price_new;
    }

    public String getRange_price_free_new() {
        return range_price_free_new;
    }

    public void setRange_price_free_new(String range_price_free_new) {
        this.range_price_free_new = range_price_free_new;
    }

    public String getSku_new() {
        return sku_new;
    }

    public void setSku_new(String sku_new) {
        this.sku_new = sku_new;
    }

    public String getPrice_import() {
        return price_import;
    }

    public void setPrice_import(String price_import) {
        this.price_import = price_import;
    }

    public String getPrice_kids() {
        return price_kids;
    }

    public void setPrice_kids(String price_kids) {
        this.price_kids = price_kids;
    }

    public String getPrice_pets() {
        return price_pets;
    }

    public void setPrice_pets(String price_pets) {
        this.price_pets = price_pets;
    }

    public String getTop_sort() {
        return top_sort;
    }

    public void setTop_sort(String top_sort) {
        this.top_sort = top_sort;
    }

    public String getIs_simplify() {
        return is_simplify;
    }

    public void setIs_simplify(String is_simplify) {
        this.is_simplify = is_simplify;
    }

    public String getSalable() {
        return salable;
    }

    public void setSalable(String salable) {
        this.salable = salable;
    }

    public String getSearchable() {
        return searchable;
    }

    public void setSearchable(String searchable) {
        this.searchable = searchable;
    }

    public String getVolume_weight() {
        return volume_weight;
    }

    public void setVolume_weight(String volume_weight) {
        this.volume_weight = volume_weight;
    }

    public String getDescribe_good_flag() {
        return describe_good_flag;
    }

    public void setDescribe_good_flag(String describe_good_flag) {
        this.describe_good_flag = describe_good_flag;
    }

    public String getEntype_new() {
		return entype_new;
	}

	public void setEntype_new(String entype_new) {
		this.entype_new = entype_new;
	}

	public String getCustom_video_flag() {
		return custom_video_flag;
	}

	public void setCustom_video_flag(String custom_video_flag) {
		this.custom_video_flag = custom_video_flag;
	}

	public String getSize_info_en() {
        return size_info_en;
    }

    public void setSize_info_en(String size_info_en) {
        this.size_info_en = size_info_en;
    }

    public String getRw_keyword() {
		return rw_keyword;
	}

	public void setRw_keyword(String rw_keyword) {
		this.rw_keyword = rw_keyword;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatid1() {
        return catid1;
    }

    public void setCatid1(String catid1) {
        this.catid1 = catid1;
    }

    public String getPath_catid() {
        return path_catid;
    }

    public void setPath_catid(String path_catid) {
        this.path_catid = path_catid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWprice() {
        return wprice;
    }

    public void setWprice(String wprice) {
        this.wprice = wprice;
    }

    public String getCustom_main_image() {
        return custom_main_image;
    }

    public void setCustom_main_image(String custom_main_image) {
        this.custom_main_image = custom_main_image;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFeeprice() {
        return feeprice;
    }

    public void setFeeprice(String feeprice) {
        this.feeprice = feeprice;
    }

    public String getFprice() {
        return fprice;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getMorder() {
        return morder;
    }

    public void setMorder(String morder) {
        this.morder = morder;
    }

    public String getEntype() {
        return entype;
    }

    public void setEntype(String entype) {
        this.entype = entype;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getEndetail() {
        return endetail;
    }

    public void setEndetail(String endetail) {
        this.endetail = endetail;
    }

    public String getEninfo() {
        return eninfo;
    }

    public void setEninfo(String eninfo) {
        this.eninfo = eninfo;
    }

    public String getAli_sold() {
        return ali_sold;
    }

    public void setAli_sold(String ali_sold) {
        this.ali_sold = ali_sold;
    }

    public String getAli_pid() {
        return ali_pid;
    }

    public void setAli_pid(String ali_pid) {
        this.ali_pid = ali_pid;
    }

    public String getAli_price() {
        return ali_price;
    }

    public void setAli_price(String ali_price) {
        this.ali_price = ali_price;
    }

    public String getAli_weight() {
        return ali_weight;
    }

    public void setAli_weight(String ali_weight) {
        this.ali_weight = ali_weight;
    }

    public String getAli_freight() {
        return ali_freight;
    }

    public void setAli_freight(String ali_freight) {
        this.ali_freight = ali_freight;
    }

    public String getAli_sellunit() {
        return ali_sellunit;
    }

    public void setAli_sellunit(String ali_sellunit) {
        this.ali_sellunit = ali_sellunit;
    }

    public String getAli_morder() {
        return ali_morder;
    }

    public void setAli_morder(String ali_morder) {
        this.ali_morder = ali_morder;
    }

    public String getAli_unit() {
        return ali_unit;
    }

    public void setAli_unit(String ali_unit) {
        this.ali_unit = ali_unit;
    }

    public String getAli_name() {
        return ali_name;
    }

    public void setAli_name(String ali_name) {
        this.ali_name = ali_name;
    }

    public String getRemotpath() {
        return remotpath;
    }

    public void setRemotpath(String remotpath) {
        this.remotpath = remotpath;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getLocalpath() {
        return localpath;
    }

    public void setLocalpath(String localpath) {
        this.localpath = localpath;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatidparenta() {
        return catidparenta;
    }

    public void setCatidparenta(String catidparenta) {
        this.catidparenta = catidparenta;
    }

    public String getCatidparentb() {
        return catidparentb;
    }

    public void setCatidparentb(String catidparentb) {
        this.catidparentb = catidparentb;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getCatidb() {
        return catidb;
    }

    public void setCatidb(String catidb) {
        this.catidb = catidb;
    }

    public String getCatpath() {
        return catpath;
    }

    public void setCatpath(String catpath) {
        this.catpath = catpath;
    }

    public String getOriginalcatid() {
        return originalcatid;
    }

    public void setOriginalcatid(String originalcatid) {
        this.originalcatid = originalcatid;
    }

    public String getOriginalcatpath() {
        return originalcatpath;
    }

    public void setOriginalcatpath(String originalcatpath) {
        this.originalcatpath = originalcatpath;
    }

    public String getAli_img() {
        return ali_img;
    }

    public void setAli_img(String ali_img) {
        this.ali_img = ali_img;
    }

    public String getImg_check() {
        return img_check;
    }

    public void setImg_check(String img_check) {
        this.img_check = img_check;
    }

    public String getRevise_weight() {
        return revise_weight;
    }

    public void setRevise_weight(String revise_weight) {
        this.revise_weight = revise_weight;
    }

    public String getFinal_weight() {
        return final_weight;
    }

    public void setFinal_weight(String final_weight) {
        this.final_weight = final_weight;
    }

    public String getRange_price() {
        return range_price;
    }

    public void setRange_price(String range_price) {
        this.range_price = range_price;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getWholesale_price() {
        return wholesale_price;
    }

    public void setWholesale_price(String wholesale_price) {
        this.wholesale_price = wholesale_price;
    }

    public String getFprice_str() {
        return fprice_str;
    }

    public void setFprice_str(String fprice_str) {
        this.fprice_str = fprice_str;
    }

    public String getPvids() {
        return pvids;
    }

    public void setPvids(String pvids) {
        this.pvids = pvids;
    }

    public String getInfoReviseFlag() {
        return infoReviseFlag;
    }

    public void setInfoReviseFlag(String infoReviseFlag) {
        this.infoReviseFlag = infoReviseFlag;
    }

    public String getPriceReviseFlag() {
        return priceReviseFlag;
    }

    public void setPriceReviseFlag(String priceReviseFlag) {
        this.priceReviseFlag = priceReviseFlag;
    }

    public String getIsBenchmark() {
        return isBenchmark;
    }

    public void setIsBenchmark(String isBenchmark) {
        this.isBenchmark = isBenchmark;
    }

    public String getIsNewCloud() {
        return isNewCloud;
    }

    public void setIsNewCloud(String isNewCloud) {
        this.isNewCloud = isNewCloud;
    }

    public String getFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public String getSellunit() {
        return sellunit;
    }

    public void setSellunit(String sellunit) {
        this.sellunit = sellunit;
    }

    public String getCur_time() {
        return cur_time;
    }

    public void setCur_time(String cur_time) {
        this.cur_time = cur_time;
    }

    public String getBm_flag() {
        return bm_flag;
    }

    public void setBm_flag(String bm_flag) {
        this.bm_flag = bm_flag;
    }

    public String getSource_pro_flag() {
        return source_pro_flag;
    }

    public void setSource_pro_flag(String source_pro_flag) {
        this.source_pro_flag = source_pro_flag;
    }

    public String getIs_sold_flag() {
        return is_sold_flag;
    }

    public void setIs_sold_flag(String is_sold_flag) {
        this.is_sold_flag = is_sold_flag;
    }

    public String getPriority_flag() {
        return priority_flag;
    }

    public void setPriority_flag(String priority_flag) {
        this.priority_flag = priority_flag;
    }

    public String getIs_add_car_flag() {
        return is_add_car_flag;
    }

    public void setIs_add_car_flag(String is_add_car_flag) {
        this.is_add_car_flag = is_add_car_flag;
    }

    public String getSource_used_flag() {
        return source_used_flag;
    }

    public void setSource_used_flag(String source_used_flag) {
        this.source_used_flag = source_used_flag;
    }

    public String getOcr_match_flag() {
        return ocr_match_flag;
    }

    public void setOcr_match_flag(String ocr_match_flag) {
        this.ocr_match_flag = ocr_match_flag;
    }

    public String getIs_show_det_img_flag() {
        return is_show_det_img_flag;
    }

    public void setIs_show_det_img_flag(String is_show_det_img_flag) {
        this.is_show_det_img_flag = is_show_det_img_flag;
    }

    public String getIs_show_det_table_flag() {
        return is_show_det_table_flag;
    }

    public void setIs_show_det_table_flag(String is_show_det_table_flag) {
        this.is_show_det_table_flag = is_show_det_table_flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getGoodsstate() {
        return goodsstate;
    }

    public void setGoodsstate(String goodsstate) {
        this.goodsstate = goodsstate;
    }

    public String getBeforesku() {
        return beforesku;
    }

    public void setBeforesku(String beforesku) {
        this.beforesku = beforesku;
    }

    public String getIs_stock_flag() {
        return is_stock_flag;
    }

    public void setIs_stock_flag(String is_stock_flag) {
        this.is_stock_flag = is_stock_flag;
    }

    public String getUnsellableReason() {
        return unsellableReason;
    }

    public void setUnsellableReason(String unsellableReason) {
        this.unsellableReason = unsellableReason;
    }

    public String getSamplingStatus() {
        return samplingStatus;
    }

    public void setSamplingStatus(String samplingStatus) {
        this.samplingStatus = samplingStatus;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getMatchSource() {
        return matchSource;
    }

    public void setMatchSource(String matchSource) {
        this.matchSource = matchSource;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(String validationDate) {
        this.validationDate = validationDate;
    }

    public String getBest_match() {
        return best_match;
    }

    public void setBest_match(String best_match) {
        this.best_match = best_match;
    }

    public String getOcean_price() {
        return ocean_price;
    }

    public void setOcean_price(String ocean_price) {
        this.ocean_price = ocean_price;
    }

    public String getValidationDealDate() {
        return validationDealDate;
    }

    public void setValidationDealDate(String validationDealDate) {
        this.validationDealDate = validationDealDate;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getAverage_deliver_time() {
        return average_deliver_time;
    }

    public void setAverage_deliver_time(String average_deliver_time) {
        this.average_deliver_time = average_deliver_time;
    }

    public String getCore_flag() {
        return core_flag;
    }

    public void setCore_flag(String core_flag) {
        this.core_flag = core_flag;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIs_edited() {
        return is_edited;
    }

    public void setIs_edited(String is_edited) {
        this.is_edited = is_edited;
    }

    public Character getCrud() {
        return crud;
    }

    public void setCrud(Character crud) {
        this.crud = crud;
    }

    public InputData() {
    }

    public InputData(Character crud) {
        this.crud = crud;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }
}
