package com.cbt.shopcar.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodsCarExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GoodsCarExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdIsNull() {
            addCriterion("goodsdata_id is null");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdIsNotNull() {
            addCriterion("goodsdata_id is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdEqualTo(Integer value) {
            addCriterion("goodsdata_id =", value, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdNotEqualTo(Integer value) {
            addCriterion("goodsdata_id <>", value, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdGreaterThan(Integer value) {
            addCriterion("goodsdata_id >", value, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("goodsdata_id >=", value, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdLessThan(Integer value) {
            addCriterion("goodsdata_id <", value, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdLessThanOrEqualTo(Integer value) {
            addCriterion("goodsdata_id <=", value, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdIn(List<Integer> values) {
            addCriterion("goodsdata_id in", values, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdNotIn(List<Integer> values) {
            addCriterion("goodsdata_id not in", values, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdBetween(Integer value1, Integer value2) {
            addCriterion("goodsdata_id between", value1, value2, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andGoodsdataIdNotBetween(Integer value1, Integer value2) {
            addCriterion("goodsdata_id not between", value1, value2, "goodsdataId");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("userid is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userid is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Integer value) {
            addCriterion("userid =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Integer value) {
            addCriterion("userid <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Integer value) {
            addCriterion("userid >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("userid >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Integer value) {
            addCriterion("userid <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Integer value) {
            addCriterion("userid <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Integer> values) {
            addCriterion("userid in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Integer> values) {
            addCriterion("userid not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Integer value1, Integer value2) {
            addCriterion("userid between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("userid not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andSessionidIsNull() {
            addCriterion("sessionid is null");
            return (Criteria) this;
        }

        public Criteria andSessionidIsNotNull() {
            addCriterion("sessionid is not null");
            return (Criteria) this;
        }

        public Criteria andSessionidEqualTo(String value) {
            addCriterion("sessionid =", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidNotEqualTo(String value) {
            addCriterion("sessionid <>", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidGreaterThan(String value) {
            addCriterion("sessionid >", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidGreaterThanOrEqualTo(String value) {
            addCriterion("sessionid >=", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidLessThan(String value) {
            addCriterion("sessionid <", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidLessThanOrEqualTo(String value) {
            addCriterion("sessionid <=", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidLike(String value) {
            addCriterion("sessionid like", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidNotLike(String value) {
            addCriterion("sessionid not like", value, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidIn(List<String> values) {
            addCriterion("sessionid in", values, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidNotIn(List<String> values) {
            addCriterion("sessionid not in", values, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidBetween(String value1, String value2) {
            addCriterion("sessionid between", value1, value2, "sessionid");
            return (Criteria) this;
        }

        public Criteria andSessionidNotBetween(String value1, String value2) {
            addCriterion("sessionid not between", value1, value2, "sessionid");
            return (Criteria) this;
        }

        public Criteria andGuidIsNull() {
            addCriterion("guid is null");
            return (Criteria) this;
        }

        public Criteria andGuidIsNotNull() {
            addCriterion("guid is not null");
            return (Criteria) this;
        }

        public Criteria andGuidEqualTo(String value) {
            addCriterion("guid =", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotEqualTo(String value) {
            addCriterion("guid <>", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThan(String value) {
            addCriterion("guid >", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThanOrEqualTo(String value) {
            addCriterion("guid >=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThan(String value) {
            addCriterion("guid <", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThanOrEqualTo(String value) {
            addCriterion("guid <=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLike(String value) {
            addCriterion("guid like", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotLike(String value) {
            addCriterion("guid not like", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidIn(List<String> values) {
            addCriterion("guid in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotIn(List<String> values) {
            addCriterion("guid not in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidBetween(String value1, String value2) {
            addCriterion("guid between", value1, value2, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotBetween(String value1, String value2) {
            addCriterion("guid not between", value1, value2, "guid");
            return (Criteria) this;
        }

        public Criteria andItemidIsNull() {
            addCriterion("itemId is null");
            return (Criteria) this;
        }

        public Criteria andItemidIsNotNull() {
            addCriterion("itemId is not null");
            return (Criteria) this;
        }

        public Criteria andItemidEqualTo(String value) {
            addCriterion("itemId =", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotEqualTo(String value) {
            addCriterion("itemId <>", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidGreaterThan(String value) {
            addCriterion("itemId >", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidGreaterThanOrEqualTo(String value) {
            addCriterion("itemId >=", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidLessThan(String value) {
            addCriterion("itemId <", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidLessThanOrEqualTo(String value) {
            addCriterion("itemId <=", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidLike(String value) {
            addCriterion("itemId like", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotLike(String value) {
            addCriterion("itemId not like", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidIn(List<String> values) {
            addCriterion("itemId in", values, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotIn(List<String> values) {
            addCriterion("itemId not in", values, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidBetween(String value1, String value2) {
            addCriterion("itemId between", value1, value2, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotBetween(String value1, String value2) {
            addCriterion("itemId not between", value1, value2, "itemid");
            return (Criteria) this;
        }

        public Criteria andShopidIsNull() {
            addCriterion("shopId is null");
            return (Criteria) this;
        }

        public Criteria andShopidIsNotNull() {
            addCriterion("shopId is not null");
            return (Criteria) this;
        }

        public Criteria andShopidEqualTo(String value) {
            addCriterion("shopId =", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotEqualTo(String value) {
            addCriterion("shopId <>", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidGreaterThan(String value) {
            addCriterion("shopId >", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidGreaterThanOrEqualTo(String value) {
            addCriterion("shopId >=", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidLessThan(String value) {
            addCriterion("shopId <", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidLessThanOrEqualTo(String value) {
            addCriterion("shopId <=", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidLike(String value) {
            addCriterion("shopId like", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotLike(String value) {
            addCriterion("shopId not like", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidIn(List<String> values) {
            addCriterion("shopId in", values, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotIn(List<String> values) {
            addCriterion("shopId not in", values, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidBetween(String value1, String value2) {
            addCriterion("shopId between", value1, value2, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotBetween(String value1, String value2) {
            addCriterion("shopId not between", value1, value2, "shopid");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlIsNull() {
            addCriterion("goods_url is null");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlIsNotNull() {
            addCriterion("goods_url is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlEqualTo(String value) {
            addCriterion("goods_url =", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlNotEqualTo(String value) {
            addCriterion("goods_url <>", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlGreaterThan(String value) {
            addCriterion("goods_url >", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlGreaterThanOrEqualTo(String value) {
            addCriterion("goods_url >=", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlLessThan(String value) {
            addCriterion("goods_url <", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlLessThanOrEqualTo(String value) {
            addCriterion("goods_url <=", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlLike(String value) {
            addCriterion("goods_url like", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlNotLike(String value) {
            addCriterion("goods_url not like", value, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlIn(List<String> values) {
            addCriterion("goods_url in", values, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlNotIn(List<String> values) {
            addCriterion("goods_url not in", values, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlBetween(String value1, String value2) {
            addCriterion("goods_url between", value1, value2, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsUrlNotBetween(String value1, String value2) {
            addCriterion("goods_url not between", value1, value2, "goodsUrl");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleIsNull() {
            addCriterion("goods_title is null");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleIsNotNull() {
            addCriterion("goods_title is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleEqualTo(String value) {
            addCriterion("goods_title =", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleNotEqualTo(String value) {
            addCriterion("goods_title <>", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleGreaterThan(String value) {
            addCriterion("goods_title >", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleGreaterThanOrEqualTo(String value) {
            addCriterion("goods_title >=", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleLessThan(String value) {
            addCriterion("goods_title <", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleLessThanOrEqualTo(String value) {
            addCriterion("goods_title <=", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleLike(String value) {
            addCriterion("goods_title like", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleNotLike(String value) {
            addCriterion("goods_title not like", value, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleIn(List<String> values) {
            addCriterion("goods_title in", values, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleNotIn(List<String> values) {
            addCriterion("goods_title not in", values, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleBetween(String value1, String value2) {
            addCriterion("goods_title between", value1, value2, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoodsTitleNotBetween(String value1, String value2) {
            addCriterion("goods_title not between", value1, value2, "goodsTitle");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerIsNull() {
            addCriterion("googs_seller is null");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerIsNotNull() {
            addCriterion("googs_seller is not null");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerEqualTo(String value) {
            addCriterion("googs_seller =", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerNotEqualTo(String value) {
            addCriterion("googs_seller <>", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerGreaterThan(String value) {
            addCriterion("googs_seller >", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerGreaterThanOrEqualTo(String value) {
            addCriterion("googs_seller >=", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerLessThan(String value) {
            addCriterion("googs_seller <", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerLessThanOrEqualTo(String value) {
            addCriterion("googs_seller <=", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerLike(String value) {
            addCriterion("googs_seller like", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerNotLike(String value) {
            addCriterion("googs_seller not like", value, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerIn(List<String> values) {
            addCriterion("googs_seller in", values, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerNotIn(List<String> values) {
            addCriterion("googs_seller not in", values, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerBetween(String value1, String value2) {
            addCriterion("googs_seller between", value1, value2, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsSellerNotBetween(String value1, String value2) {
            addCriterion("googs_seller not between", value1, value2, "googsSeller");
            return (Criteria) this;
        }

        public Criteria andGoogsImgIsNull() {
            addCriterion("googs_img is null");
            return (Criteria) this;
        }

        public Criteria andGoogsImgIsNotNull() {
            addCriterion("googs_img is not null");
            return (Criteria) this;
        }

        public Criteria andGoogsImgEqualTo(String value) {
            addCriterion("googs_img =", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgNotEqualTo(String value) {
            addCriterion("googs_img <>", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgGreaterThan(String value) {
            addCriterion("googs_img >", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgGreaterThanOrEqualTo(String value) {
            addCriterion("googs_img >=", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgLessThan(String value) {
            addCriterion("googs_img <", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgLessThanOrEqualTo(String value) {
            addCriterion("googs_img <=", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgLike(String value) {
            addCriterion("googs_img like", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgNotLike(String value) {
            addCriterion("googs_img not like", value, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgIn(List<String> values) {
            addCriterion("googs_img in", values, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgNotIn(List<String> values) {
            addCriterion("googs_img not in", values, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgBetween(String value1, String value2) {
            addCriterion("googs_img between", value1, value2, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsImgNotBetween(String value1, String value2) {
            addCriterion("googs_img not between", value1, value2, "googsImg");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceIsNull() {
            addCriterion("googs_price is null");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceIsNotNull() {
            addCriterion("googs_price is not null");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceEqualTo(String value) {
            addCriterion("googs_price =", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceNotEqualTo(String value) {
            addCriterion("googs_price <>", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceGreaterThan(String value) {
            addCriterion("googs_price >", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceGreaterThanOrEqualTo(String value) {
            addCriterion("googs_price >=", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceLessThan(String value) {
            addCriterion("googs_price <", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceLessThanOrEqualTo(String value) {
            addCriterion("googs_price <=", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceLike(String value) {
            addCriterion("googs_price like", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceNotLike(String value) {
            addCriterion("googs_price not like", value, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceIn(List<String> values) {
            addCriterion("googs_price in", values, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceNotIn(List<String> values) {
            addCriterion("googs_price not in", values, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceBetween(String value1, String value2) {
            addCriterion("googs_price between", value1, value2, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsPriceNotBetween(String value1, String value2) {
            addCriterion("googs_price not between", value1, value2, "googsPrice");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberIsNull() {
            addCriterion("googs_number is null");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberIsNotNull() {
            addCriterion("googs_number is not null");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberEqualTo(Integer value) {
            addCriterion("googs_number =", value, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberNotEqualTo(Integer value) {
            addCriterion("googs_number <>", value, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberGreaterThan(Integer value) {
            addCriterion("googs_number >", value, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("googs_number >=", value, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberLessThan(Integer value) {
            addCriterion("googs_number <", value, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberLessThanOrEqualTo(Integer value) {
            addCriterion("googs_number <=", value, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberIn(List<Integer> values) {
            addCriterion("googs_number in", values, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberNotIn(List<Integer> values) {
            addCriterion("googs_number not in", values, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberBetween(Integer value1, Integer value2) {
            addCriterion("googs_number between", value1, value2, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("googs_number not between", value1, value2, "googsNumber");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeIsNull() {
            addCriterion("googs_size is null");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeIsNotNull() {
            addCriterion("googs_size is not null");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeEqualTo(String value) {
            addCriterion("googs_size =", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeNotEqualTo(String value) {
            addCriterion("googs_size <>", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeGreaterThan(String value) {
            addCriterion("googs_size >", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeGreaterThanOrEqualTo(String value) {
            addCriterion("googs_size >=", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeLessThan(String value) {
            addCriterion("googs_size <", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeLessThanOrEqualTo(String value) {
            addCriterion("googs_size <=", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeLike(String value) {
            addCriterion("googs_size like", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeNotLike(String value) {
            addCriterion("googs_size not like", value, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeIn(List<String> values) {
            addCriterion("googs_size in", values, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeNotIn(List<String> values) {
            addCriterion("googs_size not in", values, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeBetween(String value1, String value2) {
            addCriterion("googs_size between", value1, value2, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoogsSizeNotBetween(String value1, String value2) {
            addCriterion("googs_size not between", value1, value2, "googsSize");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeIsNull() {
            addCriterion("goods_type is null");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeIsNotNull() {
            addCriterion("goods_type is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeEqualTo(String value) {
            addCriterion("goods_type =", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotEqualTo(String value) {
            addCriterion("goods_type <>", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeGreaterThan(String value) {
            addCriterion("goods_type >", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeGreaterThanOrEqualTo(String value) {
            addCriterion("goods_type >=", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeLessThan(String value) {
            addCriterion("goods_type <", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeLessThanOrEqualTo(String value) {
            addCriterion("goods_type <=", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeLike(String value) {
            addCriterion("goods_type like", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotLike(String value) {
            addCriterion("goods_type not like", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeIn(List<String> values) {
            addCriterion("goods_type in", values, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotIn(List<String> values) {
            addCriterion("goods_type not in", values, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeBetween(String value1, String value2) {
            addCriterion("goods_type between", value1, value2, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotBetween(String value1, String value2) {
            addCriterion("goods_type not between", value1, value2, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoogsColorIsNull() {
            addCriterion("googs_color is null");
            return (Criteria) this;
        }

        public Criteria andGoogsColorIsNotNull() {
            addCriterion("googs_color is not null");
            return (Criteria) this;
        }

        public Criteria andGoogsColorEqualTo(String value) {
            addCriterion("googs_color =", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorNotEqualTo(String value) {
            addCriterion("googs_color <>", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorGreaterThan(String value) {
            addCriterion("googs_color >", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorGreaterThanOrEqualTo(String value) {
            addCriterion("googs_color >=", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorLessThan(String value) {
            addCriterion("googs_color <", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorLessThanOrEqualTo(String value) {
            addCriterion("googs_color <=", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorLike(String value) {
            addCriterion("googs_color like", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorNotLike(String value) {
            addCriterion("googs_color not like", value, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorIn(List<String> values) {
            addCriterion("googs_color in", values, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorNotIn(List<String> values) {
            addCriterion("googs_color not in", values, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorBetween(String value1, String value2) {
            addCriterion("googs_color between", value1, value2, "googsColor");
            return (Criteria) this;
        }

        public Criteria andGoogsColorNotBetween(String value1, String value2) {
            addCriterion("googs_color not between", value1, value2, "googsColor");
            return (Criteria) this;
        }

        public Criteria andFreightIsNull() {
            addCriterion("freight is null");
            return (Criteria) this;
        }

        public Criteria andFreightIsNotNull() {
            addCriterion("freight is not null");
            return (Criteria) this;
        }

        public Criteria andFreightEqualTo(String value) {
            addCriterion("freight =", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotEqualTo(String value) {
            addCriterion("freight <>", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightGreaterThan(String value) {
            addCriterion("freight >", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightGreaterThanOrEqualTo(String value) {
            addCriterion("freight >=", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightLessThan(String value) {
            addCriterion("freight <", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightLessThanOrEqualTo(String value) {
            addCriterion("freight <=", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightLike(String value) {
            addCriterion("freight like", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotLike(String value) {
            addCriterion("freight not like", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightIn(List<String> values) {
            addCriterion("freight in", values, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotIn(List<String> values) {
            addCriterion("freight not in", values, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightBetween(String value1, String value2) {
            addCriterion("freight between", value1, value2, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotBetween(String value1, String value2) {
            addCriterion("freight not between", value1, value2, "freight");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeIsNull() {
            addCriterion("delivery_time is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeIsNotNull() {
            addCriterion("delivery_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEqualTo(String value) {
            addCriterion("delivery_time =", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeNotEqualTo(String value) {
            addCriterion("delivery_time <>", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeGreaterThan(String value) {
            addCriterion("delivery_time >", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeGreaterThanOrEqualTo(String value) {
            addCriterion("delivery_time >=", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeLessThan(String value) {
            addCriterion("delivery_time <", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeLessThanOrEqualTo(String value) {
            addCriterion("delivery_time <=", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeLike(String value) {
            addCriterion("delivery_time like", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeNotLike(String value) {
            addCriterion("delivery_time not like", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeIn(List<String> values) {
            addCriterion("delivery_time in", values, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeNotIn(List<String> values) {
            addCriterion("delivery_time not in", values, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBetween(String value1, String value2) {
            addCriterion("delivery_time between", value1, value2, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeNotBetween(String value1, String value2) {
            addCriterion("delivery_time not between", value1, value2, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andNormMostIsNull() {
            addCriterion("norm_most is null");
            return (Criteria) this;
        }

        public Criteria andNormMostIsNotNull() {
            addCriterion("norm_most is not null");
            return (Criteria) this;
        }

        public Criteria andNormMostEqualTo(String value) {
            addCriterion("norm_most =", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostNotEqualTo(String value) {
            addCriterion("norm_most <>", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostGreaterThan(String value) {
            addCriterion("norm_most >", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostGreaterThanOrEqualTo(String value) {
            addCriterion("norm_most >=", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostLessThan(String value) {
            addCriterion("norm_most <", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostLessThanOrEqualTo(String value) {
            addCriterion("norm_most <=", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostLike(String value) {
            addCriterion("norm_most like", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostNotLike(String value) {
            addCriterion("norm_most not like", value, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostIn(List<String> values) {
            addCriterion("norm_most in", values, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostNotIn(List<String> values) {
            addCriterion("norm_most not in", values, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostBetween(String value1, String value2) {
            addCriterion("norm_most between", value1, value2, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormMostNotBetween(String value1, String value2) {
            addCriterion("norm_most not between", value1, value2, "normMost");
            return (Criteria) this;
        }

        public Criteria andNormLeastIsNull() {
            addCriterion("norm_least is null");
            return (Criteria) this;
        }

        public Criteria andNormLeastIsNotNull() {
            addCriterion("norm_least is not null");
            return (Criteria) this;
        }

        public Criteria andNormLeastEqualTo(String value) {
            addCriterion("norm_least =", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastNotEqualTo(String value) {
            addCriterion("norm_least <>", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastGreaterThan(String value) {
            addCriterion("norm_least >", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastGreaterThanOrEqualTo(String value) {
            addCriterion("norm_least >=", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastLessThan(String value) {
            addCriterion("norm_least <", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastLessThanOrEqualTo(String value) {
            addCriterion("norm_least <=", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastLike(String value) {
            addCriterion("norm_least like", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastNotLike(String value) {
            addCriterion("norm_least not like", value, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastIn(List<String> values) {
            addCriterion("norm_least in", values, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastNotIn(List<String> values) {
            addCriterion("norm_least not in", values, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastBetween(String value1, String value2) {
            addCriterion("norm_least between", value1, value2, "normLeast");
            return (Criteria) this;
        }

        public Criteria andNormLeastNotBetween(String value1, String value2) {
            addCriterion("norm_least not between", value1, value2, "normLeast");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Integer value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Integer value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Integer value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Integer value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Integer value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Integer> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Integer> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Integer value1, Integer value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Integer value1, Integer value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andDatatimeIsNull() {
            addCriterion("datatime is null");
            return (Criteria) this;
        }

        public Criteria andDatatimeIsNotNull() {
            addCriterion("datatime is not null");
            return (Criteria) this;
        }

        public Criteria andDatatimeEqualTo(Date value) {
            addCriterion("datatime =", value, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeNotEqualTo(Date value) {
            addCriterion("datatime <>", value, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeGreaterThan(Date value) {
            addCriterion("datatime >", value, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeGreaterThanOrEqualTo(Date value) {
            addCriterion("datatime >=", value, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeLessThan(Date value) {
            addCriterion("datatime <", value, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeLessThanOrEqualTo(Date value) {
            addCriterion("datatime <=", value, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeIn(List<Date> values) {
            addCriterion("datatime in", values, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeNotIn(List<Date> values) {
            addCriterion("datatime not in", values, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeBetween(Date value1, Date value2) {
            addCriterion("datatime between", value1, value2, "datatime");
            return (Criteria) this;
        }

        public Criteria andDatatimeNotBetween(Date value1, Date value2) {
            addCriterion("datatime not between", value1, value2, "datatime");
            return (Criteria) this;
        }

        public Criteria andFlagIsNull() {
            addCriterion("flag is null");
            return (Criteria) this;
        }

        public Criteria andFlagIsNotNull() {
            addCriterion("flag is not null");
            return (Criteria) this;
        }

        public Criteria andFlagEqualTo(Integer value) {
            addCriterion("flag =", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagNotEqualTo(Integer value) {
            addCriterion("flag <>", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagGreaterThan(Integer value) {
            addCriterion("flag >", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("flag >=", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagLessThan(Integer value) {
            addCriterion("flag <", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagLessThanOrEqualTo(Integer value) {
            addCriterion("flag <=", value, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagIn(List<Integer> values) {
            addCriterion("flag in", values, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagNotIn(List<Integer> values) {
            addCriterion("flag not in", values, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagBetween(Integer value1, Integer value2) {
            addCriterion("flag between", value1, value2, "flag");
            return (Criteria) this;
        }

        public Criteria andFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("flag not between", value1, value2, "flag");
            return (Criteria) this;
        }

        public Criteria andPwpriceIsNull() {
            addCriterion("pWprice is null");
            return (Criteria) this;
        }

        public Criteria andPwpriceIsNotNull() {
            addCriterion("pWprice is not null");
            return (Criteria) this;
        }

        public Criteria andPwpriceEqualTo(String value) {
            addCriterion("pWprice =", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceNotEqualTo(String value) {
            addCriterion("pWprice <>", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceGreaterThan(String value) {
            addCriterion("pWprice >", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceGreaterThanOrEqualTo(String value) {
            addCriterion("pWprice >=", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceLessThan(String value) {
            addCriterion("pWprice <", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceLessThanOrEqualTo(String value) {
            addCriterion("pWprice <=", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceLike(String value) {
            addCriterion("pWprice like", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceNotLike(String value) {
            addCriterion("pWprice not like", value, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceIn(List<String> values) {
            addCriterion("pWprice in", values, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceNotIn(List<String> values) {
            addCriterion("pWprice not in", values, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceBetween(String value1, String value2) {
            addCriterion("pWprice between", value1, value2, "pwprice");
            return (Criteria) this;
        }

        public Criteria andPwpriceNotBetween(String value1, String value2) {
            addCriterion("pWprice not between", value1, value2, "pwprice");
            return (Criteria) this;
        }

        public Criteria andTrueShippingIsNull() {
            addCriterion("true_shipping is null");
            return (Criteria) this;
        }

        public Criteria andTrueShippingIsNotNull() {
            addCriterion("true_shipping is not null");
            return (Criteria) this;
        }

        public Criteria andTrueShippingEqualTo(String value) {
            addCriterion("true_shipping =", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingNotEqualTo(String value) {
            addCriterion("true_shipping <>", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingGreaterThan(String value) {
            addCriterion("true_shipping >", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingGreaterThanOrEqualTo(String value) {
            addCriterion("true_shipping >=", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingLessThan(String value) {
            addCriterion("true_shipping <", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingLessThanOrEqualTo(String value) {
            addCriterion("true_shipping <=", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingLike(String value) {
            addCriterion("true_shipping like", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingNotLike(String value) {
            addCriterion("true_shipping not like", value, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingIn(List<String> values) {
            addCriterion("true_shipping in", values, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingNotIn(List<String> values) {
            addCriterion("true_shipping not in", values, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingBetween(String value1, String value2) {
            addCriterion("true_shipping between", value1, value2, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andTrueShippingNotBetween(String value1, String value2) {
            addCriterion("true_shipping not between", value1, value2, "trueShipping");
            return (Criteria) this;
        }

        public Criteria andFreightFreeIsNull() {
            addCriterion("freight_free is null");
            return (Criteria) this;
        }

        public Criteria andFreightFreeIsNotNull() {
            addCriterion("freight_free is not null");
            return (Criteria) this;
        }

        public Criteria andFreightFreeEqualTo(Integer value) {
            addCriterion("freight_free =", value, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeNotEqualTo(Integer value) {
            addCriterion("freight_free <>", value, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeGreaterThan(Integer value) {
            addCriterion("freight_free >", value, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeGreaterThanOrEqualTo(Integer value) {
            addCriterion("freight_free >=", value, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeLessThan(Integer value) {
            addCriterion("freight_free <", value, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeLessThanOrEqualTo(Integer value) {
            addCriterion("freight_free <=", value, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeIn(List<Integer> values) {
            addCriterion("freight_free in", values, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeNotIn(List<Integer> values) {
            addCriterion("freight_free not in", values, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeBetween(Integer value1, Integer value2) {
            addCriterion("freight_free between", value1, value2, "freightFree");
            return (Criteria) this;
        }

        public Criteria andFreightFreeNotBetween(Integer value1, Integer value2) {
            addCriterion("freight_free not between", value1, value2, "freightFree");
            return (Criteria) this;
        }

        public Criteria andWidthIsNull() {
            addCriterion("width is null");
            return (Criteria) this;
        }

        public Criteria andWidthIsNotNull() {
            addCriterion("width is not null");
            return (Criteria) this;
        }

        public Criteria andWidthEqualTo(String value) {
            addCriterion("width =", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthNotEqualTo(String value) {
            addCriterion("width <>", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthGreaterThan(String value) {
            addCriterion("width >", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthGreaterThanOrEqualTo(String value) {
            addCriterion("width >=", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthLessThan(String value) {
            addCriterion("width <", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthLessThanOrEqualTo(String value) {
            addCriterion("width <=", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthLike(String value) {
            addCriterion("width like", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthNotLike(String value) {
            addCriterion("width not like", value, "width");
            return (Criteria) this;
        }

        public Criteria andWidthIn(List<String> values) {
            addCriterion("width in", values, "width");
            return (Criteria) this;
        }

        public Criteria andWidthNotIn(List<String> values) {
            addCriterion("width not in", values, "width");
            return (Criteria) this;
        }

        public Criteria andWidthBetween(String value1, String value2) {
            addCriterion("width between", value1, value2, "width");
            return (Criteria) this;
        }

        public Criteria andWidthNotBetween(String value1, String value2) {
            addCriterion("width not between", value1, value2, "width");
            return (Criteria) this;
        }

        public Criteria andPerweightIsNull() {
            addCriterion("perWeight is null");
            return (Criteria) this;
        }

        public Criteria andPerweightIsNotNull() {
            addCriterion("perWeight is not null");
            return (Criteria) this;
        }

        public Criteria andPerweightEqualTo(String value) {
            addCriterion("perWeight =", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightNotEqualTo(String value) {
            addCriterion("perWeight <>", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightGreaterThan(String value) {
            addCriterion("perWeight >", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightGreaterThanOrEqualTo(String value) {
            addCriterion("perWeight >=", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightLessThan(String value) {
            addCriterion("perWeight <", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightLessThanOrEqualTo(String value) {
            addCriterion("perWeight <=", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightLike(String value) {
            addCriterion("perWeight like", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightNotLike(String value) {
            addCriterion("perWeight not like", value, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightIn(List<String> values) {
            addCriterion("perWeight in", values, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightNotIn(List<String> values) {
            addCriterion("perWeight not in", values, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightBetween(String value1, String value2) {
            addCriterion("perWeight between", value1, value2, "perweight");
            return (Criteria) this;
        }

        public Criteria andPerweightNotBetween(String value1, String value2) {
            addCriterion("perWeight not between", value1, value2, "perweight");
            return (Criteria) this;
        }

        public Criteria andSeilunitIsNull() {
            addCriterion("seilUnit is null");
            return (Criteria) this;
        }

        public Criteria andSeilunitIsNotNull() {
            addCriterion("seilUnit is not null");
            return (Criteria) this;
        }

        public Criteria andSeilunitEqualTo(String value) {
            addCriterion("seilUnit =", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitNotEqualTo(String value) {
            addCriterion("seilUnit <>", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitGreaterThan(String value) {
            addCriterion("seilUnit >", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitGreaterThanOrEqualTo(String value) {
            addCriterion("seilUnit >=", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitLessThan(String value) {
            addCriterion("seilUnit <", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitLessThanOrEqualTo(String value) {
            addCriterion("seilUnit <=", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitLike(String value) {
            addCriterion("seilUnit like", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitNotLike(String value) {
            addCriterion("seilUnit not like", value, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitIn(List<String> values) {
            addCriterion("seilUnit in", values, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitNotIn(List<String> values) {
            addCriterion("seilUnit not in", values, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitBetween(String value1, String value2) {
            addCriterion("seilUnit between", value1, value2, "seilunit");
            return (Criteria) this;
        }

        public Criteria andSeilunitNotBetween(String value1, String value2) {
            addCriterion("seilUnit not between", value1, value2, "seilunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitIsNull() {
            addCriterion("goodsUnit is null");
            return (Criteria) this;
        }

        public Criteria andGoodsunitIsNotNull() {
            addCriterion("goodsUnit is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsunitEqualTo(String value) {
            addCriterion("goodsUnit =", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitNotEqualTo(String value) {
            addCriterion("goodsUnit <>", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitGreaterThan(String value) {
            addCriterion("goodsUnit >", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitGreaterThanOrEqualTo(String value) {
            addCriterion("goodsUnit >=", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitLessThan(String value) {
            addCriterion("goodsUnit <", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitLessThanOrEqualTo(String value) {
            addCriterion("goodsUnit <=", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitLike(String value) {
            addCriterion("goodsUnit like", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitNotLike(String value) {
            addCriterion("goodsUnit not like", value, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitIn(List<String> values) {
            addCriterion("goodsUnit in", values, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitNotIn(List<String> values) {
            addCriterion("goodsUnit not in", values, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitBetween(String value1, String value2) {
            addCriterion("goodsUnit between", value1, value2, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andGoodsunitNotBetween(String value1, String value2) {
            addCriterion("goodsUnit not between", value1, value2, "goodsunit");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeIsNull() {
            addCriterion("bulk_volume is null");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeIsNotNull() {
            addCriterion("bulk_volume is not null");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeEqualTo(String value) {
            addCriterion("bulk_volume =", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeNotEqualTo(String value) {
            addCriterion("bulk_volume <>", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeGreaterThan(String value) {
            addCriterion("bulk_volume >", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeGreaterThanOrEqualTo(String value) {
            addCriterion("bulk_volume >=", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeLessThan(String value) {
            addCriterion("bulk_volume <", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeLessThanOrEqualTo(String value) {
            addCriterion("bulk_volume <=", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeLike(String value) {
            addCriterion("bulk_volume like", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeNotLike(String value) {
            addCriterion("bulk_volume not like", value, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeIn(List<String> values) {
            addCriterion("bulk_volume in", values, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeNotIn(List<String> values) {
            addCriterion("bulk_volume not in", values, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeBetween(String value1, String value2) {
            addCriterion("bulk_volume between", value1, value2, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andBulkVolumeNotBetween(String value1, String value2) {
            addCriterion("bulk_volume not between", value1, value2, "bulkVolume");
            return (Criteria) this;
        }

        public Criteria andTotalWeightIsNull() {
            addCriterion("total_weight is null");
            return (Criteria) this;
        }

        public Criteria andTotalWeightIsNotNull() {
            addCriterion("total_weight is not null");
            return (Criteria) this;
        }

        public Criteria andTotalWeightEqualTo(String value) {
            addCriterion("total_weight =", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightNotEqualTo(String value) {
            addCriterion("total_weight <>", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightGreaterThan(String value) {
            addCriterion("total_weight >", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightGreaterThanOrEqualTo(String value) {
            addCriterion("total_weight >=", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightLessThan(String value) {
            addCriterion("total_weight <", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightLessThanOrEqualTo(String value) {
            addCriterion("total_weight <=", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightLike(String value) {
            addCriterion("total_weight like", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightNotLike(String value) {
            addCriterion("total_weight not like", value, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightIn(List<String> values) {
            addCriterion("total_weight in", values, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightNotIn(List<String> values) {
            addCriterion("total_weight not in", values, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightBetween(String value1, String value2) {
            addCriterion("total_weight between", value1, value2, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andTotalWeightNotBetween(String value1, String value2) {
            addCriterion("total_weight not between", value1, value2, "totalWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightIsNull() {
            addCriterion("per_weight is null");
            return (Criteria) this;
        }

        public Criteria andPerWeightIsNotNull() {
            addCriterion("per_weight is not null");
            return (Criteria) this;
        }

        public Criteria andPerWeightEqualTo(String value) {
            addCriterion("per_weight =", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightNotEqualTo(String value) {
            addCriterion("per_weight <>", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightGreaterThan(String value) {
            addCriterion("per_weight >", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightGreaterThanOrEqualTo(String value) {
            addCriterion("per_weight >=", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightLessThan(String value) {
            addCriterion("per_weight <", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightLessThanOrEqualTo(String value) {
            addCriterion("per_weight <=", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightLike(String value) {
            addCriterion("per_weight like", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightNotLike(String value) {
            addCriterion("per_weight not like", value, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightIn(List<String> values) {
            addCriterion("per_weight in", values, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightNotIn(List<String> values) {
            addCriterion("per_weight not in", values, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightBetween(String value1, String value2) {
            addCriterion("per_weight between", value1, value2, "perWeight");
            return (Criteria) this;
        }

        public Criteria andPerWeightNotBetween(String value1, String value2) {
            addCriterion("per_weight not between", value1, value2, "perWeight");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailIsNull() {
            addCriterion("goods_email is null");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailIsNotNull() {
            addCriterion("goods_email is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailEqualTo(String value) {
            addCriterion("goods_email =", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailNotEqualTo(String value) {
            addCriterion("goods_email <>", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailGreaterThan(String value) {
            addCriterion("goods_email >", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailGreaterThanOrEqualTo(String value) {
            addCriterion("goods_email >=", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailLessThan(String value) {
            addCriterion("goods_email <", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailLessThanOrEqualTo(String value) {
            addCriterion("goods_email <=", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailLike(String value) {
            addCriterion("goods_email like", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailNotLike(String value) {
            addCriterion("goods_email not like", value, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailIn(List<String> values) {
            addCriterion("goods_email in", values, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailNotIn(List<String> values) {
            addCriterion("goods_email not in", values, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailBetween(String value1, String value2) {
            addCriterion("goods_email between", value1, value2, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andGoodsEmailNotBetween(String value1, String value2) {
            addCriterion("goods_email not between", value1, value2, "goodsEmail");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyIsNull() {
            addCriterion("free_shopping_company is null");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyIsNotNull() {
            addCriterion("free_shopping_company is not null");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyEqualTo(String value) {
            addCriterion("free_shopping_company =", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyNotEqualTo(String value) {
            addCriterion("free_shopping_company <>", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyGreaterThan(String value) {
            addCriterion("free_shopping_company >", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyGreaterThanOrEqualTo(String value) {
            addCriterion("free_shopping_company >=", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyLessThan(String value) {
            addCriterion("free_shopping_company <", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyLessThanOrEqualTo(String value) {
            addCriterion("free_shopping_company <=", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyLike(String value) {
            addCriterion("free_shopping_company like", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyNotLike(String value) {
            addCriterion("free_shopping_company not like", value, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyIn(List<String> values) {
            addCriterion("free_shopping_company in", values, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyNotIn(List<String> values) {
            addCriterion("free_shopping_company not in", values, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyBetween(String value1, String value2) {
            addCriterion("free_shopping_company between", value1, value2, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeShoppingCompanyNotBetween(String value1, String value2) {
            addCriterion("free_shopping_company not between", value1, value2, "freeShoppingCompany");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysIsNull() {
            addCriterion("free_sc_days is null");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysIsNotNull() {
            addCriterion("free_sc_days is not null");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysEqualTo(String value) {
            addCriterion("free_sc_days =", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysNotEqualTo(String value) {
            addCriterion("free_sc_days <>", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysGreaterThan(String value) {
            addCriterion("free_sc_days >", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysGreaterThanOrEqualTo(String value) {
            addCriterion("free_sc_days >=", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysLessThan(String value) {
            addCriterion("free_sc_days <", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysLessThanOrEqualTo(String value) {
            addCriterion("free_sc_days <=", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysLike(String value) {
            addCriterion("free_sc_days like", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysNotLike(String value) {
            addCriterion("free_sc_days not like", value, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysIn(List<String> values) {
            addCriterion("free_sc_days in", values, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysNotIn(List<String> values) {
            addCriterion("free_sc_days not in", values, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysBetween(String value1, String value2) {
            addCriterion("free_sc_days between", value1, value2, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andFreeScDaysNotBetween(String value1, String value2) {
            addCriterion("free_sc_days not between", value1, value2, "freeScDays");
            return (Criteria) this;
        }

        public Criteria andPreferentialIsNull() {
            addCriterion("preferential is null");
            return (Criteria) this;
        }

        public Criteria andPreferentialIsNotNull() {
            addCriterion("preferential is not null");
            return (Criteria) this;
        }

        public Criteria andPreferentialEqualTo(Integer value) {
            addCriterion("preferential =", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialNotEqualTo(Integer value) {
            addCriterion("preferential <>", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialGreaterThan(Integer value) {
            addCriterion("preferential >", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialGreaterThanOrEqualTo(Integer value) {
            addCriterion("preferential >=", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialLessThan(Integer value) {
            addCriterion("preferential <", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialLessThanOrEqualTo(Integer value) {
            addCriterion("preferential <=", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialIn(List<Integer> values) {
            addCriterion("preferential in", values, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialNotIn(List<Integer> values) {
            addCriterion("preferential not in", values, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialBetween(Integer value1, Integer value2) {
            addCriterion("preferential between", value1, value2, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialNotBetween(Integer value1, Integer value2) {
            addCriterion("preferential not between", value1, value2, "preferential");
            return (Criteria) this;
        }

        public Criteria andDepositRateIsNull() {
            addCriterion("deposit_rate is null");
            return (Criteria) this;
        }

        public Criteria andDepositRateIsNotNull() {
            addCriterion("deposit_rate is not null");
            return (Criteria) this;
        }

        public Criteria andDepositRateEqualTo(Integer value) {
            addCriterion("deposit_rate =", value, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateNotEqualTo(Integer value) {
            addCriterion("deposit_rate <>", value, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateGreaterThan(Integer value) {
            addCriterion("deposit_rate >", value, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateGreaterThanOrEqualTo(Integer value) {
            addCriterion("deposit_rate >=", value, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateLessThan(Integer value) {
            addCriterion("deposit_rate <", value, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateLessThanOrEqualTo(Integer value) {
            addCriterion("deposit_rate <=", value, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateIn(List<Integer> values) {
            addCriterion("deposit_rate in", values, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateNotIn(List<Integer> values) {
            addCriterion("deposit_rate not in", values, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateBetween(Integer value1, Integer value2) {
            addCriterion("deposit_rate between", value1, value2, "depositRate");
            return (Criteria) this;
        }

        public Criteria andDepositRateNotBetween(Integer value1, Integer value2) {
            addCriterion("deposit_rate not between", value1, value2, "depositRate");
            return (Criteria) this;
        }

        public Criteria andFeepriceIsNull() {
            addCriterion("feeprice is null");
            return (Criteria) this;
        }

        public Criteria andFeepriceIsNotNull() {
            addCriterion("feeprice is not null");
            return (Criteria) this;
        }

        public Criteria andFeepriceEqualTo(String value) {
            addCriterion("feeprice =", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceNotEqualTo(String value) {
            addCriterion("feeprice <>", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceGreaterThan(String value) {
            addCriterion("feeprice >", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceGreaterThanOrEqualTo(String value) {
            addCriterion("feeprice >=", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceLessThan(String value) {
            addCriterion("feeprice <", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceLessThanOrEqualTo(String value) {
            addCriterion("feeprice <=", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceLike(String value) {
            addCriterion("feeprice like", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceNotLike(String value) {
            addCriterion("feeprice not like", value, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceIn(List<String> values) {
            addCriterion("feeprice in", values, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceNotIn(List<String> values) {
            addCriterion("feeprice not in", values, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceBetween(String value1, String value2) {
            addCriterion("feeprice between", value1, value2, "feeprice");
            return (Criteria) this;
        }

        public Criteria andFeepriceNotBetween(String value1, String value2) {
            addCriterion("feeprice not between", value1, value2, "feeprice");
            return (Criteria) this;
        }

        public Criteria andGoodsClassIsNull() {
            addCriterion("goods_class is null");
            return (Criteria) this;
        }

        public Criteria andGoodsClassIsNotNull() {
            addCriterion("goods_class is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsClassEqualTo(Integer value) {
            addCriterion("goods_class =", value, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassNotEqualTo(Integer value) {
            addCriterion("goods_class <>", value, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassGreaterThan(Integer value) {
            addCriterion("goods_class >", value, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_class >=", value, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassLessThan(Integer value) {
            addCriterion("goods_class <", value, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassLessThanOrEqualTo(Integer value) {
            addCriterion("goods_class <=", value, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassIn(List<Integer> values) {
            addCriterion("goods_class in", values, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassNotIn(List<Integer> values) {
            addCriterion("goods_class not in", values, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassBetween(Integer value1, Integer value2) {
            addCriterion("goods_class between", value1, value2, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andGoodsClassNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_class not between", value1, value2, "goodsClass");
            return (Criteria) this;
        }

        public Criteria andCurrencyIsNull() {
            addCriterion("currency is null");
            return (Criteria) this;
        }

        public Criteria andCurrencyIsNotNull() {
            addCriterion("currency is not null");
            return (Criteria) this;
        }

        public Criteria andCurrencyEqualTo(String value) {
            addCriterion("currency =", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotEqualTo(String value) {
            addCriterion("currency <>", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThan(String value) {
            addCriterion("currency >", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("currency >=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThan(String value) {
            addCriterion("currency <", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThanOrEqualTo(String value) {
            addCriterion("currency <=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLike(String value) {
            addCriterion("currency like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotLike(String value) {
            addCriterion("currency not like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyIn(List<String> values) {
            addCriterion("currency in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotIn(List<String> values) {
            addCriterion("currency not in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyBetween(String value1, String value2) {
            addCriterion("currency between", value1, value2, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotBetween(String value1, String value2) {
            addCriterion("currency not between", value1, value2, "currency");
            return (Criteria) this;
        }

        public Criteria andExtraFreightIsNull() {
            addCriterion("extra_freight is null");
            return (Criteria) this;
        }

        public Criteria andExtraFreightIsNotNull() {
            addCriterion("extra_freight is not null");
            return (Criteria) this;
        }

        public Criteria andExtraFreightEqualTo(Double value) {
            addCriterion("extra_freight =", value, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightNotEqualTo(Double value) {
            addCriterion("extra_freight <>", value, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightGreaterThan(Double value) {
            addCriterion("extra_freight >", value, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightGreaterThanOrEqualTo(Double value) {
            addCriterion("extra_freight >=", value, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightLessThan(Double value) {
            addCriterion("extra_freight <", value, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightLessThanOrEqualTo(Double value) {
            addCriterion("extra_freight <=", value, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightIn(List<Double> values) {
            addCriterion("extra_freight in", values, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightNotIn(List<Double> values) {
            addCriterion("extra_freight not in", values, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightBetween(Double value1, Double value2) {
            addCriterion("extra_freight between", value1, value2, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andExtraFreightNotBetween(Double value1, Double value2) {
            addCriterion("extra_freight not between", value1, value2, "extraFreight");
            return (Criteria) this;
        }

        public Criteria andBuyForMeIsNull() {
            addCriterion("buy_for_me is null");
            return (Criteria) this;
        }

        public Criteria andBuyForMeIsNotNull() {
            addCriterion("buy_for_me is not null");
            return (Criteria) this;
        }

        public Criteria andBuyForMeEqualTo(Integer value) {
            addCriterion("buy_for_me =", value, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeNotEqualTo(Integer value) {
            addCriterion("buy_for_me <>", value, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeGreaterThan(Integer value) {
            addCriterion("buy_for_me >", value, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeGreaterThanOrEqualTo(Integer value) {
            addCriterion("buy_for_me >=", value, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeLessThan(Integer value) {
            addCriterion("buy_for_me <", value, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeLessThanOrEqualTo(Integer value) {
            addCriterion("buy_for_me <=", value, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeIn(List<Integer> values) {
            addCriterion("buy_for_me in", values, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeNotIn(List<Integer> values) {
            addCriterion("buy_for_me not in", values, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeBetween(Integer value1, Integer value2) {
            addCriterion("buy_for_me between", value1, value2, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andBuyForMeNotBetween(Integer value1, Integer value2) {
            addCriterion("buy_for_me not between", value1, value2, "buyForMe");
            return (Criteria) this;
        }

        public Criteria andSourceUrlIsNull() {
            addCriterion("source_url is null");
            return (Criteria) this;
        }

        public Criteria andSourceUrlIsNotNull() {
            addCriterion("source_url is not null");
            return (Criteria) this;
        }

        public Criteria andSourceUrlEqualTo(Integer value) {
            addCriterion("source_url =", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlNotEqualTo(Integer value) {
            addCriterion("source_url <>", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlGreaterThan(Integer value) {
            addCriterion("source_url >", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlGreaterThanOrEqualTo(Integer value) {
            addCriterion("source_url >=", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlLessThan(Integer value) {
            addCriterion("source_url <", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlLessThanOrEqualTo(Integer value) {
            addCriterion("source_url <=", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlIn(List<Integer> values) {
            addCriterion("source_url in", values, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlNotIn(List<Integer> values) {
            addCriterion("source_url not in", values, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlBetween(Integer value1, Integer value2) {
            addCriterion("source_url between", value1, value2, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlNotBetween(Integer value1, Integer value2) {
            addCriterion("source_url not between", value1, value2, "sourceUrl");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}