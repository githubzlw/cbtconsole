package com.cbt.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 1688商品对应英文翻译和ali商品信息
 *
 * @author JXW
 */
public class CustomGoodsPublish extends CustomGoodsBean {
    private int aliId;// aliexpress_goods表id
    private String aliGoodsPid;// ali商品pid
    private String aliGoodsUrl;// ali商品链接
    private String aliGoodsImgUrl;// ali商品图片链接
    private String aliGoodsName;// ali商品名称
    private String aliGoodsPrice;// ali商品价格

    private String aliGoodsInfo;// ali商品info_ori
    private List<TypeBean> showTypes;// 展示的规格图集合
    private List<String> showImages;// 展示橱窗图集合
    private String showMainImage;// 默认展示图
    private int canEdit;// 数据是否可操作，数据未编辑或者离职人员值为0
    //private String rangePrice;// 价格区间
    private String reviseWeight;// 人为修正重量
    private String finalWeight;// 最终重量

    private int isEdited;// 是否编辑 0:未编辑1:标题已编辑2:详情已编辑

    private String isAbnormal;// 异常数据 0未选择，1类别错误，2商品太贵，3太便宜
    private String abnormalValue;// 异常数据值

    private int isBenchmark;// 货源对标情况 1:精确对标, 2:近似对标,0:没找到对标
    private String benchmarkValue;// 货源对标情况值

    private int bmFlag;// 人为对标货源 1:是， 2：不是 ,0:默认值

    private int sourceProFlag;// 货源属性 1:同店铺商品,2:同款商品 ,3:对标商品,4:1688商品(原始老数据)
    private String sourceProValue;// 货源属性值

    private int soldFlag; // 是否售卖0全部1:卖过,2:没有卖过
    private int priorityFlag;// 商品优先级 1:核心,2：非核心

    private int addCarFlag;// 是否加入购物车 0全部，1已加入购物车，2未加入购物车，3已加入购物车后删除
    private String carValue;// 是否加入购物车值

    private int sourceUsedFlag;// 货源信息可用度 0:不可用 1:可用 2:描述很精彩
    private int ocrMatchFlag;// OCR判断情况
    // 1：人工OCR判断(详情)，2：未判断，3：未判断(全禁)，4：未判断(全免)，5：无中文，6：有中文，但不删除
    // （因为图片比例少），7：有中文，部分图应被删除，8：没图
    private String ocrMatchValue;// OCR判断情况值

    private int rebidFlag;// 重新对标标识，1是重新对标

    private int imgDownFlag;// 网络图片是否下载本地，0:未下载 1下载完成

    private String shopId;// 店铺id

    private int weightFlag;// 重量标识 1：1688原始重量，2：平均重量

    private String wordSizeInfo;//文字尺码表信息

    private int infringingFlag;//侵权标识 0未侵权 1侵权 15精选

    private String ocrSizeInfo1;//ocr识别的尺码表信息
    private String ocrSizeInfo2;//ocr识别的尺码表信息
    private String ocrSizeInfo3;//ocr识别的尺码表信息

    private String sellUnit;//售卖单位
    private int matchSource;
    private int adminId;

    private double oldProfit;//原利润率
    private double editProfit;//编辑利润率
    private int lockProfit;//锁定利润率标识 1锁定

    private int unsellAbleReason;//软下架状态
    private String unsellAbleReasonDesc;//软下架说明

    private String offReason;//下架原因
    private int clickNum;//点击次数
    private int weightNotFlag;//重量不合理标注
    private int uglyFlag;//难看标识
    private int repairedFlag;//修复标识
    private int weightIsEdit;//重量手动更新标识 1产品编辑更新,2入库称重
    
    private String complainId;//投诉id
    private List<String> complain;
    //评论
    private String review_name;
    private String review_score;
    private String createtime;
    private String review_remark;
    private String country;
    private String reviews;
    private int reviewCount;
    private String updatetime;
    private String review_flag;


    private String goods_pid;
    private int priceIsEdit;
    private int isUpdateImg;// 1自动设置第一张图片为橱窗图，2设置选中的封面图
    private String sizeInfoEn;

    private int fromFlag;// 产品上线来源：1店铺上线，2单个商品上线录入，3速卖通对标上线，4跨境上线，5爆款开发上线
    private String fromFlagDesc;

    private String maxPrice;// 最高价格
    private int isSoldFlag; // 是否免邮 flag:1老客户免邮价,2：新的免邮价
    private String weight1688; // 1688重量
    private int isWeigthZero;// 1688重量为空标识  1是空的 0不是

    private double addPriceLv;// 加价率

    private String crawlAliPrice; // 抓取ali价格
    private String crawlAliDate; // 抓取ali价格时间
    private String entypeNew ;//新的尺码表
    private String categoryName ;//产品名称太短增加类别名称

    /**
     * 描述很精彩标识
     */
    private int describeGoodFlag = -1;

    private String volumeWeight;

    private String onlineUrl;
    private String pathCatid;

    private int isShowDetImgFlag;

    private int overSeaFlag;

    public int getOverSeaFlag() {
        return overSeaFlag;
    }

    public void setOverSeaFlag(int overSeaFlag) {
        this.overSeaFlag = overSeaFlag;
    }

    public int getIsShowDetImgFlag() {
        return isShowDetImgFlag;
    }

    public void setIsShowDetImgFlag(int isShowDetImgFlag) {
        this.isShowDetImgFlag = isShowDetImgFlag;
    }

    public String getPathCatid() {
        return pathCatid;
    }

    public void setPathCatid(String pathCatid) {
        this.pathCatid = pathCatid;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }

    public String getVolumeWeight() {
        return volumeWeight;
    }

    public void setVolumeWeight(String volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

    public int getDescribeGoodFlag() {
        return describeGoodFlag;
    }

    public void setDescribeGoodFlag(int describeGoodFlag) {
        this.describeGoodFlag = describeGoodFlag;
    }

    public String getEntypeNew() {
		return entypeNew;
	}

	public void setEntypeNew(String entypeNew) {
		this.entypeNew = entypeNew;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCrawlAliPrice() {
        return crawlAliPrice;
    }

    public void setCrawlAliPrice(String crawlAliPrice) {
        this.crawlAliPrice = crawlAliPrice;
    }

    public String getCrawlAliDate() {
        return crawlAliDate;
    }

    public void setCrawlAliDate(String crawlAliDate) {
        this.crawlAliDate = crawlAliDate;
    }

    public double getAddPriceLv() {
        return addPriceLv;
    }

    public void setAddPriceLv(double addPriceLv) {
        this.addPriceLv = addPriceLv;
    }

    public int getIsWeigthZero() {
        return isWeigthZero;
    }

    public void setIsWeigthZero(int isWeigthZero) {
        this.isWeigthZero = isWeigthZero;
    }


    public String getWeight1688() {
        return weight1688;
    }

    public void setWeight1688(String weight1688) {
        this.weight1688 = weight1688;
        if(StringUtils.isBlank(weight1688) || "0".equals(weight1688)){
            isWeigthZero = 1;
        }
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getFromFlagDesc() {
        return fromFlagDesc;
    }

    public void setFromFlagDesc(String fromFlagDesc) {
        this.fromFlagDesc = fromFlagDesc;
    }

    public int getFromFlag() {
        return fromFlag;
    }

    public void setFromFlag(int fromFlag) {
        this.fromFlag = fromFlag;
        if(fromFlag == 1){
            fromFlagDesc = "店铺上线";
        }else if(fromFlag == 2){
            fromFlagDesc = "单个商品录入上线";
        }else if(fromFlag == 3){
            fromFlagDesc = "速卖通对标上线";
        }else if(fromFlag == 4){
            fromFlagDesc = "跨境上线";
        }else if(fromFlag == 5){
            fromFlagDesc = "爆款开发上线";
        }else if(fromFlag == 6){
            fromFlagDesc = "亚马逊对标上线";
        }
    }

    public String getGoods_pid() {
        return goods_pid;
    }

    public void setGoods_pid(String goods_pid) {
        this.goods_pid = goods_pid;
    }
    public String getSizeInfoEn() {
        return sizeInfoEn;
    }

    public void setSizeInfoEn(String sizeInfoEn) {
        this.sizeInfoEn = sizeInfoEn;
    }

    public int getIsUpdateImg() {
        return isUpdateImg;
    }

    public void setIsUpdateImg(int isUpdateImg) {
        this.isUpdateImg = isUpdateImg;
    }

    public int getPriceIsEdit() {
        return priceIsEdit;
    }

    public void setPriceIsEdit(int priceIsEdit) {
        this.priceIsEdit = priceIsEdit;
    }

    public String getReview_flag() {
        return review_flag;
    }

    public void setReview_flag(String review_flag) {
        this.review_flag = review_flag;
    }

    @Override
    public String getUpdatetime() {
        return updatetime;
    }

    @Override
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }


    public String getReview_name() {
        return review_name;
    }

    public void setReview_name(String review_name) {
        this.review_name = review_name;
    }

    public String getReview_score() {
        return review_score;
    }

    public void setReview_score(String review_score) {
        this.review_score = review_score;
    }

    @Override
    public String getCreatetime() {
        return createtime;
    }

    @Override
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getReview_remark() {
        return review_remark;
    }

    public void setReview_remark(String review_remark) {
        this.review_remark = review_remark;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private int promotionFlag;//促销flag: 0不促销，1促销
    private String fpriceStr;//bizprice 字段


    public List<String> getComplain() {
		return complain;
	}

	public void setComplain(List<String> complain) {
		this.complain = complain;
	}

	public String getComplainId() {
		return complainId;
	}

	public void setComplainId(String complainId) {
		this.complainId = complainId;
	}

	public String getAliGoodsPrice() {
        return aliGoodsPrice;
    }

    public void setAliGoodsPrice(String aliGoodsPrice) {
        this.aliGoodsPrice = aliGoodsPrice;
    }

    public int getMatchSource() {
        return matchSource;
    }

    public void setMatchSource(int matchSource) {
        this.matchSource = matchSource;
    }

    public String getOcrSizeInfo1() {
        return ocrSizeInfo1;
    }

    public void setOcrSizeInfo1(String ocrSizeInfo1) {
        this.ocrSizeInfo1 = ocrSizeInfo1;
    }

    public String getOcrSizeInfo2() {
        return ocrSizeInfo2;
    }

    public void setOcrSizeInfo2(String ocrSizeInfo2) {
        this.ocrSizeInfo2 = ocrSizeInfo2;
    }

    public String getOcrSizeInfo3() {
        return ocrSizeInfo3;
    }

    public void setOcrSizeInfo3(String ocrSizeInfo3) {
        this.ocrSizeInfo3 = ocrSizeInfo3;
    }

    public int getAliId() {
        return aliId;
    }

    public void setAliId(int aliId) {
        this.aliId = aliId;
    }

    public String getAliGoodsPid() {
        return aliGoodsPid;
    }

    public void setAliGoodsPid(String aliGoodsPid) {
        if (StringUtils.isNotBlank(aliGoodsPid)) {
            this.aliGoodsPid = aliGoodsPid;
        }else{
            this.aliGoodsPid = "0";
        }
    }

    public String getAliGoodsUrl() {
        return aliGoodsUrl;
    }

    public void setAliGoodsUrl(String aliGoodsUrl) {
        this.aliGoodsUrl = aliGoodsUrl;
    }

    public String getAliGoodsImgUrl() {
        return aliGoodsImgUrl;
    }

    public void setAliGoodsImgUrl(String aliGoodsImgUrl) {
        this.aliGoodsImgUrl = aliGoodsImgUrl;
    }

    public String getAliGoodsName() {
        return aliGoodsName;
    }

    public void setAliGoodsName(String aliGoodsName) {
        this.aliGoodsName = aliGoodsName;
        if (StringUtils.isBlank(aliGoodsName)) {
            this.aliGoodsName = "aliexpress goods url";
        } else {
            this.aliGoodsName = aliGoodsName.replaceAll("\\\\", "/").replace("100%", "").replace("%", "");
        }
        this.aliGoodsUrl = "https://www.aliexpress.com/item/" + this.aliGoodsName + "/" + this.aliGoodsPid + ".html";
    }

    public String getAliGoodsInfo() {
        return aliGoodsInfo;
    }

    public void setAliGoodsInfo(String aliGoodsInfo) {
        this.aliGoodsInfo = aliGoodsInfo;
    }

    public List<TypeBean> getShowTypes() {
        return showTypes;
    }

    public void setShowTypes(List<TypeBean> showTypes) {
        this.showTypes = showTypes;
    }

    public List<String> getShowImages() {
        return showImages;
    }

    public void setShowImages(List<String> showImages) {
        this.showImages = showImages;
    }

    public String getShowMainImage() {
        return showMainImage;
    }

    public void setShowMainImage(String showMainImage) {
        this.showMainImage = showMainImage;
    }

    public int getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(int canEdit) {
        this.canEdit = canEdit;
    }

//    public String getRangePrice() {
//        return rangePrice;
//    }
//
//    public void setRangePrice(String rangePrice) {
//        this.rangePrice = rangePrice;
//    }

    public String getReviseWeight() {
        return reviseWeight;
    }

    public void setReviseWeight(String reviseWeight) {
        this.reviseWeight = reviseWeight;
    }

    public String getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(String finalWeight) {
        this.finalWeight = finalWeight;
    }

    public int getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(int isEdited) {
        this.isEdited = isEdited;
    }

    public String getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
        if (isAbnormal == null || "".equals(isAbnormal)) {
            this.abnormalValue = "";
        } else {
            // 是否异常 '0':正常,'1':类别错误,'2':商品太贵,'3':太便宜
            if ("1".equals(isAbnormal)) {
                this.abnormalValue = "类别错误";
            } else if ("2".equals(isAbnormal)) {
                this.abnormalValue = "商品太贵";
            } else if ("3".equals(isAbnormal)) {
                this.abnormalValue = "太便宜";
            }
        }
    }

    public String getAbnormalValue() {
        return abnormalValue;
    }

    public void setAbnormalValue(String abnormalValue) {
        this.abnormalValue = abnormalValue;
    }

    public int getIsBenchmark() {
        return isBenchmark;
    }

    public void setIsBenchmark(int isBenchmark) {
        this.isBenchmark = isBenchmark;
        if (isBenchmark > 0) {
            if (isBenchmark == 1) {
                this.benchmarkValue = "精确对标";
            } else if (isBenchmark == 2) {
                this.benchmarkValue = "近似对标";
            }
        } else {
            this.benchmarkValue = "没找到对标";
        }
    }

    public String getBenchmarkValue() {
        return benchmarkValue;
    }

    public void setBenchmarkValue(String benchmarkValue) {
        this.benchmarkValue = benchmarkValue;
    }

    public int getBmFlag() {
        return bmFlag;
    }

    public void setBmFlag(int bmFlag) {
        this.bmFlag = bmFlag;
    }

    public int getSourceProFlag() {
        return sourceProFlag;
    }

    public void setSourceProFlag(int sourceProFlag) {
        this.sourceProFlag = sourceProFlag;
        if (sourceProFlag > 0) {
            if (sourceProFlag == 1) {
                this.sourceProValue = "同店铺商品";
            } else if (sourceProFlag == 2) {
                this.sourceProValue = "同款商品";
            } else if (sourceProFlag == 3) {
                this.sourceProValue = "对标商品";
            } else if (sourceProFlag == 4) {
                this.sourceProValue = "1688商品(原始老数据)";
            }
        } else {
            this.sourceProValue = "";
        }
    }

    public String getSourceProValue() {
        return sourceProValue;
    }

    public void setSourceProValue(String sourceProValue) {
        this.sourceProValue = sourceProValue;
    }

    public int getSoldFlag() {
        return soldFlag;
    }

    public void setSoldFlag(int soldFlag) {
        this.soldFlag = soldFlag;
    }

    public int getPriorityFlag() {
        return priorityFlag;
    }

    public void setPriorityFlag(int priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    public int getAddCarFlag() {
        return addCarFlag;
    }

    public void setAddCarFlag(int addCarFlag) {
        this.addCarFlag = addCarFlag;
        if (addCarFlag > 0) {
            if (addCarFlag == 1) {
                this.carValue = "已加入购物车";
            } else if (addCarFlag == 2) {
                this.carValue = "未加入购物车";
            } else if (addCarFlag == 3) {
                this.carValue = "已加入购物车后删除";
            }
        } else {
            this.carValue = "";
        }
    }

    public String getCarValue() {
        return carValue;
    }

    public void setCarValue(String carValue) {
        this.carValue = carValue;
    }

    public int getSourceUsedFlag() {
        return sourceUsedFlag;
    }

    public void setSourceUsedFlag(int sourceUsedFlag) {
        this.sourceUsedFlag = sourceUsedFlag;
    }

    public int getOcrMatchFlag() {
        return ocrMatchFlag;
    }

    public void setOcrMatchFlag(int ocrMatchFlag) {
        this.ocrMatchFlag = ocrMatchFlag;
        if (ocrMatchFlag > 0) {
            if (ocrMatchFlag == 1) {
                this.ocrMatchValue = "人工OCR判断(详情)";
            } else if (ocrMatchFlag == 2) {
                this.ocrMatchValue = "未判断";
            } else if (ocrMatchFlag == 3) {
                this.ocrMatchValue = "未判断(全禁)";
            } else if (ocrMatchFlag == 4) {
                this.ocrMatchValue = "未判断(全免)";
            } else if (ocrMatchFlag == 5) {
                this.ocrMatchValue = "无中文";
            } else if (ocrMatchFlag == 6) {
                this.ocrMatchValue = "有中文，但不删除";
            } else if (ocrMatchFlag == 7) {
                this.ocrMatchValue = "有中文，部分图应被删除";
            } else if (ocrMatchFlag == 8) {
                this.ocrMatchValue = "没图";
            }
        } else {
            this.ocrMatchValue = "";
        }
    }

    public String getOcrMatchValue() {
        return ocrMatchValue;
    }

    public void setOcrMatchValue(String ocrMatchValue) {
        this.ocrMatchValue = ocrMatchValue;
    }

    public int getRebidFlag() {
        return rebidFlag;
    }

    public void setRebidFlag(int rebidFlag) {
        this.rebidFlag = rebidFlag;
    }

    public int getImgDownFlag() {
        return imgDownFlag;
    }

    public void setImgDownFlag(int imgDownFlag) {
        this.imgDownFlag = imgDownFlag;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getWeightFlag() {
        return weightFlag;
    }

    public void setWeightFlag(int weightFlag) {
        this.weightFlag = weightFlag;
    }

    public String getWordSizeInfo() {
        return wordSizeInfo;
    }

    public void setWordSizeInfo(String wordSizeInfo) {
        this.wordSizeInfo = wordSizeInfo;
    }

    public int getInfringingFlag() {
        return infringingFlag;
    }

    public void setInfringingFlag(int infringingFlag) {
        this.infringingFlag = infringingFlag;
    }

    public String getSellUnit() {
        return sellUnit;
    }

    public void setSellUnit(String sellUnit) {
        this.sellUnit = sellUnit;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public double getOldProfit() {
        return oldProfit;
    }

    public void setOldProfit(double oldProfit) {
        this.oldProfit = oldProfit;
    }

    public double getEditProfit() {
        return editProfit;
    }

    public void setEditProfit(double editProfit) {
        this.editProfit = editProfit;
    }

    public int getLockProfit() {
        return lockProfit;
    }

    public void setLockProfit(int lockProfit) {
        this.lockProfit = lockProfit;
    }

    public int getUnsellAbleReason() {
        return unsellAbleReason;
    }

    public void setUnsellAbleReason(int unsellAbleReason) {
        this.unsellAbleReason = unsellAbleReason;
        if (unsellAbleReason == 1) {
            unsellAbleReasonDesc = "1688货源下架";
        } else if (unsellAbleReason == 2) {
            unsellAbleReasonDesc = "不满足库存条件";
        } else if (unsellAbleReason == 3) {
            unsellAbleReasonDesc = "销量无变化(低库存)";
        } else if (unsellAbleReason == 4) {
            unsellAbleReasonDesc = "页面404";
        } else if (unsellAbleReason == 5) {
            unsellAbleReasonDesc = "重复验证合格";
        } else if (unsellAbleReason == 6) {
            unsellAbleReasonDesc = "IP问题或运营直接下架";
        } else if (unsellAbleReason == 7) {
            unsellAbleReasonDesc = "店铺整体禁掉";
        } else if (unsellAbleReason == 8) {
            unsellAbleReasonDesc = "采样不合格";
        } else if (unsellAbleReason == 9) {
            unsellAbleReasonDesc = "有质量问题";
        } else if (unsellAbleReason == 10) {
            unsellAbleReasonDesc = "商品侵权";
        } else if (unsellAbleReason == 11) {
            unsellAbleReasonDesc = "店铺侵权";
        } else if (unsellAbleReason == 12) {
            unsellAbleReasonDesc = "难看";
        } else if (unsellAbleReason == 13) {
            unsellAbleReasonDesc = "中文";
        } else if (unsellAbleReason == 14) {
            unsellAbleReasonDesc = "1688商品货源变更";
        } else if (unsellAbleReason == 15) {
            unsellAbleReasonDesc = "除服装珠宝分类外的非精品数据更新到软下架";
        } else if (unsellAbleReason == 16) {
            unsellAbleReasonDesc = "“搜索展现点击比+添加购物车数据”指标不符合要求";
        } else if (unsellAbleReason == 17) {
            unsellAbleReasonDesc = "低价商品下架";
        } else if (unsellAbleReason == 18) {
            unsellAbleReasonDesc = "类别隐藏数据下架";
        } else if (unsellAbleReason == 19) {
            unsellAbleReasonDesc = "店铺小于5件商品软下架";
        } else if (unsellAbleReason == 20) {
            unsellAbleReasonDesc = "一手数据下架";
        } else if (unsellAbleReason == 21) {
            unsellAbleReasonDesc = "大于400美元商品下架";
        } else if (unsellAbleReason == 22) {
            unsellAbleReasonDesc = "老数据没有展示详情图片";
        } else if (unsellAbleReason == 23) {
            unsellAbleReasonDesc = "对应1688商品成交量小于4";
        }
    }

    public String getUnsellAbleReasonDesc() {
        return unsellAbleReasonDesc;
    }

    public void setUnsellAbleReasonDesc(String unsellAbleReasonDesc) {
        this.unsellAbleReasonDesc = unsellAbleReasonDesc;
    }

    public String getOffReason() {
        return offReason;
    }

    public void setOffReason(String offReason) {
        this.offReason = offReason;
    }

    public int getWeightNotFlag() {
        return weightNotFlag;
    }

    public void setWeightNotFlag(int weightNotFlag) {
        this.weightNotFlag = weightNotFlag;
    }

    public int getUglyFlag() {
        return uglyFlag;
    }

    public void setUglyFlag(int uglyFlag) {
        this.uglyFlag = uglyFlag;
    }

    public int getRepairedFlag() {
        return repairedFlag;
    }

    public void setRepairedFlag(int repairedFlag) {
        this.repairedFlag = repairedFlag;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public int getWeightIsEdit() {
        return weightIsEdit;
    }

    public void setWeightIsEdit(int weightIsEdit) {
        this.weightIsEdit = weightIsEdit;
    }

    public int getPromotionFlag() {
        return promotionFlag;
    }

    public void setPromotionFlag(int promotionFlag) {
        this.promotionFlag = promotionFlag;
    }

    public String getFpriceStr() {
        return fpriceStr;
    }

    public void setFpriceStr(String fpriceStr) {
        this.fpriceStr = fpriceStr;
    }
}
