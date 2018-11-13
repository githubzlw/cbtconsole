package com.importExpress.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopCarMarketingExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopCarMarketingExample() {
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

        public Criteria andCatidIsNull() {
            addCriterion("catid is null");
            return (Criteria) this;
        }

        public Criteria andCatidIsNotNull() {
            addCriterion("catid is not null");
            return (Criteria) this;
        }

        public Criteria andCatidEqualTo(String value) {
            addCriterion("catid =", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidNotEqualTo(String value) {
            addCriterion("catid <>", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidGreaterThan(String value) {
            addCriterion("catid >", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidGreaterThanOrEqualTo(String value) {
            addCriterion("catid >=", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidLessThan(String value) {
            addCriterion("catid <", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidLessThanOrEqualTo(String value) {
            addCriterion("catid <=", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidLike(String value) {
            addCriterion("catid like", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidNotLike(String value) {
            addCriterion("catid not like", value, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidIn(List<String> values) {
            addCriterion("catid in", values, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidNotIn(List<String> values) {
            addCriterion("catid not in", values, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidBetween(String value1, String value2) {
            addCriterion("catid between", value1, value2, "catid");
            return (Criteria) this;
        }

        public Criteria andCatidNotBetween(String value1, String value2) {
            addCriterion("catid not between", value1, value2, "catid");
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

        public Criteria andFreightEqualTo(Double value) {
            addCriterion("freight =", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotEqualTo(Double value) {
            addCriterion("freight <>", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightGreaterThan(Double value) {
            addCriterion("freight >", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightGreaterThanOrEqualTo(Double value) {
            addCriterion("freight >=", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightLessThan(Double value) {
            addCriterion("freight <", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightLessThanOrEqualTo(Double value) {
            addCriterion("freight <=", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightIn(List<Double> values) {
            addCriterion("freight in", values, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotIn(List<Double> values) {
            addCriterion("freight not in", values, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightBetween(Double value1, Double value2) {
            addCriterion("freight between", value1, value2, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotBetween(Double value1, Double value2) {
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

        public Criteria andPreferentialEqualTo(String value) {
            addCriterion("preferential =", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialNotEqualTo(String value) {
            addCriterion("preferential <>", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialGreaterThan(String value) {
            addCriterion("preferential >", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialGreaterThanOrEqualTo(String value) {
            addCriterion("preferential >=", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialLessThan(String value) {
            addCriterion("preferential <", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialLessThanOrEqualTo(String value) {
            addCriterion("preferential <=", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialLike(String value) {
            addCriterion("preferential like", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialNotLike(String value) {
            addCriterion("preferential not like", value, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialIn(List<String> values) {
            addCriterion("preferential in", values, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialNotIn(List<String> values) {
            addCriterion("preferential not in", values, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialBetween(String value1, String value2) {
            addCriterion("preferential between", value1, value2, "preferential");
            return (Criteria) this;
        }

        public Criteria andPreferentialNotBetween(String value1, String value2) {
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

        public Criteria andIsshippingPromoteIsNull() {
            addCriterion("isshipping_promote is null");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteIsNotNull() {
            addCriterion("isshipping_promote is not null");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteEqualTo(Integer value) {
            addCriterion("isshipping_promote =", value, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteNotEqualTo(Integer value) {
            addCriterion("isshipping_promote <>", value, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteGreaterThan(Integer value) {
            addCriterion("isshipping_promote >", value, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteGreaterThanOrEqualTo(Integer value) {
            addCriterion("isshipping_promote >=", value, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteLessThan(Integer value) {
            addCriterion("isshipping_promote <", value, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteLessThanOrEqualTo(Integer value) {
            addCriterion("isshipping_promote <=", value, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteIn(List<Integer> values) {
            addCriterion("isshipping_promote in", values, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteNotIn(List<Integer> values) {
            addCriterion("isshipping_promote not in", values, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteBetween(Integer value1, Integer value2) {
            addCriterion("isshipping_promote between", value1, value2, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andIsshippingPromoteNotBetween(Integer value1, Integer value2) {
            addCriterion("isshipping_promote not between", value1, value2, "isshippingPromote");
            return (Criteria) this;
        }

        public Criteria andMethodFeightIsNull() {
            addCriterion("method_feight is null");
            return (Criteria) this;
        }

        public Criteria andMethodFeightIsNotNull() {
            addCriterion("method_feight is not null");
            return (Criteria) this;
        }

        public Criteria andMethodFeightEqualTo(Double value) {
            addCriterion("method_feight =", value, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightNotEqualTo(Double value) {
            addCriterion("method_feight <>", value, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightGreaterThan(Double value) {
            addCriterion("method_feight >", value, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightGreaterThanOrEqualTo(Double value) {
            addCriterion("method_feight >=", value, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightLessThan(Double value) {
            addCriterion("method_feight <", value, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightLessThanOrEqualTo(Double value) {
            addCriterion("method_feight <=", value, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightIn(List<Double> values) {
            addCriterion("method_feight in", values, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightNotIn(List<Double> values) {
            addCriterion("method_feight not in", values, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightBetween(Double value1, Double value2) {
            addCriterion("method_feight between", value1, value2, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andMethodFeightNotBetween(Double value1, Double value2) {
            addCriterion("method_feight not between", value1, value2, "methodFeight");
            return (Criteria) this;
        }

        public Criteria andPrice1IsNull() {
            addCriterion("price1 is null");
            return (Criteria) this;
        }

        public Criteria andPrice1IsNotNull() {
            addCriterion("price1 is not null");
            return (Criteria) this;
        }

        public Criteria andPrice1EqualTo(Double value) {
            addCriterion("price1 =", value, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1NotEqualTo(Double value) {
            addCriterion("price1 <>", value, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1GreaterThan(Double value) {
            addCriterion("price1 >", value, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1GreaterThanOrEqualTo(Double value) {
            addCriterion("price1 >=", value, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1LessThan(Double value) {
            addCriterion("price1 <", value, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1LessThanOrEqualTo(Double value) {
            addCriterion("price1 <=", value, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1In(List<Double> values) {
            addCriterion("price1 in", values, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1NotIn(List<Double> values) {
            addCriterion("price1 not in", values, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1Between(Double value1, Double value2) {
            addCriterion("price1 between", value1, value2, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice1NotBetween(Double value1, Double value2) {
            addCriterion("price1 not between", value1, value2, "price1");
            return (Criteria) this;
        }

        public Criteria andPrice2IsNull() {
            addCriterion("price2 is null");
            return (Criteria) this;
        }

        public Criteria andPrice2IsNotNull() {
            addCriterion("price2 is not null");
            return (Criteria) this;
        }

        public Criteria andPrice2EqualTo(Double value) {
            addCriterion("price2 =", value, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2NotEqualTo(Double value) {
            addCriterion("price2 <>", value, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2GreaterThan(Double value) {
            addCriterion("price2 >", value, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2GreaterThanOrEqualTo(Double value) {
            addCriterion("price2 >=", value, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2LessThan(Double value) {
            addCriterion("price2 <", value, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2LessThanOrEqualTo(Double value) {
            addCriterion("price2 <=", value, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2In(List<Double> values) {
            addCriterion("price2 in", values, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2NotIn(List<Double> values) {
            addCriterion("price2 not in", values, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2Between(Double value1, Double value2) {
            addCriterion("price2 between", value1, value2, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice2NotBetween(Double value1, Double value2) {
            addCriterion("price2 not between", value1, value2, "price2");
            return (Criteria) this;
        }

        public Criteria andPrice3IsNull() {
            addCriterion("price3 is null");
            return (Criteria) this;
        }

        public Criteria andPrice3IsNotNull() {
            addCriterion("price3 is not null");
            return (Criteria) this;
        }

        public Criteria andPrice3EqualTo(Double value) {
            addCriterion("price3 =", value, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3NotEqualTo(Double value) {
            addCriterion("price3 <>", value, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3GreaterThan(Double value) {
            addCriterion("price3 >", value, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3GreaterThanOrEqualTo(Double value) {
            addCriterion("price3 >=", value, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3LessThan(Double value) {
            addCriterion("price3 <", value, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3LessThanOrEqualTo(Double value) {
            addCriterion("price3 <=", value, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3In(List<Double> values) {
            addCriterion("price3 in", values, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3NotIn(List<Double> values) {
            addCriterion("price3 not in", values, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3Between(Double value1, Double value2) {
            addCriterion("price3 between", value1, value2, "price3");
            return (Criteria) this;
        }

        public Criteria andPrice3NotBetween(Double value1, Double value2) {
            addCriterion("price3 not between", value1, value2, "price3");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceIsNull() {
            addCriterion("notfreeprice is null");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceIsNotNull() {
            addCriterion("notfreeprice is not null");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceEqualTo(Double value) {
            addCriterion("notfreeprice =", value, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceNotEqualTo(Double value) {
            addCriterion("notfreeprice <>", value, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceGreaterThan(Double value) {
            addCriterion("notfreeprice >", value, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceGreaterThanOrEqualTo(Double value) {
            addCriterion("notfreeprice >=", value, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceLessThan(Double value) {
            addCriterion("notfreeprice <", value, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceLessThanOrEqualTo(Double value) {
            addCriterion("notfreeprice <=", value, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceIn(List<Double> values) {
            addCriterion("notfreeprice in", values, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceNotIn(List<Double> values) {
            addCriterion("notfreeprice not in", values, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceBetween(Double value1, Double value2) {
            addCriterion("notfreeprice between", value1, value2, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andNotfreepriceNotBetween(Double value1, Double value2) {
            addCriterion("notfreeprice not between", value1, value2, "notfreeprice");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtIsNull() {
            addCriterion("theproductfrieght is null");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtIsNotNull() {
            addCriterion("theproductfrieght is not null");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtEqualTo(Double value) {
            addCriterion("theproductfrieght =", value, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtNotEqualTo(Double value) {
            addCriterion("theproductfrieght <>", value, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtGreaterThan(Double value) {
            addCriterion("theproductfrieght >", value, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtGreaterThanOrEqualTo(Double value) {
            addCriterion("theproductfrieght >=", value, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtLessThan(Double value) {
            addCriterion("theproductfrieght <", value, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtLessThanOrEqualTo(Double value) {
            addCriterion("theproductfrieght <=", value, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtIn(List<Double> values) {
            addCriterion("theproductfrieght in", values, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtNotIn(List<Double> values) {
            addCriterion("theproductfrieght not in", values, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtBetween(Double value1, Double value2) {
            addCriterion("theproductfrieght between", value1, value2, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andTheproductfrieghtNotBetween(Double value1, Double value2) {
            addCriterion("theproductfrieght not between", value1, value2, "theproductfrieght");
            return (Criteria) this;
        }

        public Criteria andIsvolumeIsNull() {
            addCriterion("isvolume is null");
            return (Criteria) this;
        }

        public Criteria andIsvolumeIsNotNull() {
            addCriterion("isvolume is not null");
            return (Criteria) this;
        }

        public Criteria andIsvolumeEqualTo(Integer value) {
            addCriterion("isvolume =", value, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeNotEqualTo(Integer value) {
            addCriterion("isvolume <>", value, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeGreaterThan(Integer value) {
            addCriterion("isvolume >", value, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeGreaterThanOrEqualTo(Integer value) {
            addCriterion("isvolume >=", value, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeLessThan(Integer value) {
            addCriterion("isvolume <", value, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeLessThanOrEqualTo(Integer value) {
            addCriterion("isvolume <=", value, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeIn(List<Integer> values) {
            addCriterion("isvolume in", values, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeNotIn(List<Integer> values) {
            addCriterion("isvolume not in", values, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeBetween(Integer value1, Integer value2) {
            addCriterion("isvolume between", value1, value2, "isvolume");
            return (Criteria) this;
        }

        public Criteria andIsvolumeNotBetween(Integer value1, Integer value2) {
            addCriterion("isvolume not between", value1, value2, "isvolume");
            return (Criteria) this;
        }

        public Criteria andFreepriceIsNull() {
            addCriterion("freeprice is null");
            return (Criteria) this;
        }

        public Criteria andFreepriceIsNotNull() {
            addCriterion("freeprice is not null");
            return (Criteria) this;
        }

        public Criteria andFreepriceEqualTo(Double value) {
            addCriterion("freeprice =", value, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceNotEqualTo(Double value) {
            addCriterion("freeprice <>", value, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceGreaterThan(Double value) {
            addCriterion("freeprice >", value, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceGreaterThanOrEqualTo(Double value) {
            addCriterion("freeprice >=", value, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceLessThan(Double value) {
            addCriterion("freeprice <", value, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceLessThanOrEqualTo(Double value) {
            addCriterion("freeprice <=", value, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceIn(List<Double> values) {
            addCriterion("freeprice in", values, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceNotIn(List<Double> values) {
            addCriterion("freeprice not in", values, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceBetween(Double value1, Double value2) {
            addCriterion("freeprice between", value1, value2, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFreepriceNotBetween(Double value1, Double value2) {
            addCriterion("freeprice not between", value1, value2, "freeprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceIsNull() {
            addCriterion("firstprice is null");
            return (Criteria) this;
        }

        public Criteria andFirstpriceIsNotNull() {
            addCriterion("firstprice is not null");
            return (Criteria) this;
        }

        public Criteria andFirstpriceEqualTo(Double value) {
            addCriterion("firstprice =", value, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceNotEqualTo(Double value) {
            addCriterion("firstprice <>", value, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceGreaterThan(Double value) {
            addCriterion("firstprice >", value, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceGreaterThanOrEqualTo(Double value) {
            addCriterion("firstprice >=", value, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceLessThan(Double value) {
            addCriterion("firstprice <", value, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceLessThanOrEqualTo(Double value) {
            addCriterion("firstprice <=", value, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceIn(List<Double> values) {
            addCriterion("firstprice in", values, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceNotIn(List<Double> values) {
            addCriterion("firstprice not in", values, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceBetween(Double value1, Double value2) {
            addCriterion("firstprice between", value1, value2, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstpriceNotBetween(Double value1, Double value2) {
            addCriterion("firstprice not between", value1, value2, "firstprice");
            return (Criteria) this;
        }

        public Criteria andFirstnumberIsNull() {
            addCriterion("firstnumber is null");
            return (Criteria) this;
        }

        public Criteria andFirstnumberIsNotNull() {
            addCriterion("firstnumber is not null");
            return (Criteria) this;
        }

        public Criteria andFirstnumberEqualTo(Integer value) {
            addCriterion("firstnumber =", value, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberNotEqualTo(Integer value) {
            addCriterion("firstnumber <>", value, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberGreaterThan(Integer value) {
            addCriterion("firstnumber >", value, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("firstnumber >=", value, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberLessThan(Integer value) {
            addCriterion("firstnumber <", value, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberLessThanOrEqualTo(Integer value) {
            addCriterion("firstnumber <=", value, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberIn(List<Integer> values) {
            addCriterion("firstnumber in", values, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberNotIn(List<Integer> values) {
            addCriterion("firstnumber not in", values, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberBetween(Integer value1, Integer value2) {
            addCriterion("firstnumber between", value1, value2, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andFirstnumberNotBetween(Integer value1, Integer value2) {
            addCriterion("firstnumber not between", value1, value2, "firstnumber");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updatetime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updatetime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("updatetime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("updatetime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("updatetime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updatetime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("updatetime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("updatetime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("updatetime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("updatetime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("updatetime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("updatetime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andAddpriceIsNull() {
            addCriterion("addPrice is null");
            return (Criteria) this;
        }

        public Criteria andAddpriceIsNotNull() {
            addCriterion("addPrice is not null");
            return (Criteria) this;
        }

        public Criteria andAddpriceEqualTo(Double value) {
            addCriterion("addPrice =", value, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceNotEqualTo(Double value) {
            addCriterion("addPrice <>", value, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceGreaterThan(Double value) {
            addCriterion("addPrice >", value, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceGreaterThanOrEqualTo(Double value) {
            addCriterion("addPrice >=", value, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceLessThan(Double value) {
            addCriterion("addPrice <", value, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceLessThanOrEqualTo(Double value) {
            addCriterion("addPrice <=", value, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceIn(List<Double> values) {
            addCriterion("addPrice in", values, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceNotIn(List<Double> values) {
            addCriterion("addPrice not in", values, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceBetween(Double value1, Double value2) {
            addCriterion("addPrice between", value1, value2, "addprice");
            return (Criteria) this;
        }

        public Criteria andAddpriceNotBetween(Double value1, Double value2) {
            addCriterion("addPrice not between", value1, value2, "addprice");
            return (Criteria) this;
        }

        public Criteria andIsfeightIsNull() {
            addCriterion("isFeight is null");
            return (Criteria) this;
        }

        public Criteria andIsfeightIsNotNull() {
            addCriterion("isFeight is not null");
            return (Criteria) this;
        }

        public Criteria andIsfeightEqualTo(Integer value) {
            addCriterion("isFeight =", value, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightNotEqualTo(Integer value) {
            addCriterion("isFeight <>", value, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightGreaterThan(Integer value) {
            addCriterion("isFeight >", value, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("isFeight >=", value, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightLessThan(Integer value) {
            addCriterion("isFeight <", value, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightLessThanOrEqualTo(Integer value) {
            addCriterion("isFeight <=", value, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightIn(List<Integer> values) {
            addCriterion("isFeight in", values, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightNotIn(List<Integer> values) {
            addCriterion("isFeight not in", values, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightBetween(Integer value1, Integer value2) {
            addCriterion("isFeight between", value1, value2, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsfeightNotBetween(Integer value1, Integer value2) {
            addCriterion("isFeight not between", value1, value2, "isfeight");
            return (Criteria) this;
        }

        public Criteria andIsbatteryIsNull() {
            addCriterion("isBattery is null");
            return (Criteria) this;
        }

        public Criteria andIsbatteryIsNotNull() {
            addCriterion("isBattery is not null");
            return (Criteria) this;
        }

        public Criteria andIsbatteryEqualTo(Integer value) {
            addCriterion("isBattery =", value, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryNotEqualTo(Integer value) {
            addCriterion("isBattery <>", value, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryGreaterThan(Integer value) {
            addCriterion("isBattery >", value, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryGreaterThanOrEqualTo(Integer value) {
            addCriterion("isBattery >=", value, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryLessThan(Integer value) {
            addCriterion("isBattery <", value, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryLessThanOrEqualTo(Integer value) {
            addCriterion("isBattery <=", value, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryIn(List<Integer> values) {
            addCriterion("isBattery in", values, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryNotIn(List<Integer> values) {
            addCriterion("isBattery not in", values, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryBetween(Integer value1, Integer value2) {
            addCriterion("isBattery between", value1, value2, "isbattery");
            return (Criteria) this;
        }

        public Criteria andIsbatteryNotBetween(Integer value1, Integer value2) {
            addCriterion("isBattery not between", value1, value2, "isbattery");
            return (Criteria) this;
        }

        public Criteria andAliposttimeIsNull() {
            addCriterion("aliPosttime is null");
            return (Criteria) this;
        }

        public Criteria andAliposttimeIsNotNull() {
            addCriterion("aliPosttime is not null");
            return (Criteria) this;
        }

        public Criteria andAliposttimeEqualTo(String value) {
            addCriterion("aliPosttime =", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeNotEqualTo(String value) {
            addCriterion("aliPosttime <>", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeGreaterThan(String value) {
            addCriterion("aliPosttime >", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeGreaterThanOrEqualTo(String value) {
            addCriterion("aliPosttime >=", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeLessThan(String value) {
            addCriterion("aliPosttime <", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeLessThanOrEqualTo(String value) {
            addCriterion("aliPosttime <=", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeLike(String value) {
            addCriterion("aliPosttime like", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeNotLike(String value) {
            addCriterion("aliPosttime not like", value, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeIn(List<String> values) {
            addCriterion("aliPosttime in", values, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeNotIn(List<String> values) {
            addCriterion("aliPosttime not in", values, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeBetween(String value1, String value2) {
            addCriterion("aliPosttime between", value1, value2, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andAliposttimeNotBetween(String value1, String value2) {
            addCriterion("aliPosttime not between", value1, value2, "aliposttime");
            return (Criteria) this;
        }

        public Criteria andPrice4IsNull() {
            addCriterion("price4 is null");
            return (Criteria) this;
        }

        public Criteria andPrice4IsNotNull() {
            addCriterion("price4 is not null");
            return (Criteria) this;
        }

        public Criteria andPrice4EqualTo(Double value) {
            addCriterion("price4 =", value, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4NotEqualTo(Double value) {
            addCriterion("price4 <>", value, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4GreaterThan(Double value) {
            addCriterion("price4 >", value, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4GreaterThanOrEqualTo(Double value) {
            addCriterion("price4 >=", value, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4LessThan(Double value) {
            addCriterion("price4 <", value, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4LessThanOrEqualTo(Double value) {
            addCriterion("price4 <=", value, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4In(List<Double> values) {
            addCriterion("price4 in", values, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4NotIn(List<Double> values) {
            addCriterion("price4 not in", values, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4Between(Double value1, Double value2) {
            addCriterion("price4 between", value1, value2, "price4");
            return (Criteria) this;
        }

        public Criteria andPrice4NotBetween(Double value1, Double value2) {
            addCriterion("price4 not between", value1, value2, "price4");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5IsNull() {
            addCriterion("goodsUrlMD5 is null");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5IsNotNull() {
            addCriterion("goodsUrlMD5 is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5EqualTo(String value) {
            addCriterion("goodsUrlMD5 =", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5NotEqualTo(String value) {
            addCriterion("goodsUrlMD5 <>", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5GreaterThan(String value) {
            addCriterion("goodsUrlMD5 >", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5GreaterThanOrEqualTo(String value) {
            addCriterion("goodsUrlMD5 >=", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5LessThan(String value) {
            addCriterion("goodsUrlMD5 <", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5LessThanOrEqualTo(String value) {
            addCriterion("goodsUrlMD5 <=", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5Like(String value) {
            addCriterion("goodsUrlMD5 like", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5NotLike(String value) {
            addCriterion("goodsUrlMD5 not like", value, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5In(List<String> values) {
            addCriterion("goodsUrlMD5 in", values, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5NotIn(List<String> values) {
            addCriterion("goodsUrlMD5 not in", values, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5Between(String value1, String value2) {
            addCriterion("goodsUrlMD5 between", value1, value2, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andGoodsurlmd5NotBetween(String value1, String value2) {
            addCriterion("goodsUrlMD5 not between", value1, value2, "goodsurlmd5");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountIsNull() {
            addCriterion("bizPriceDiscount is null");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountIsNotNull() {
            addCriterion("bizPriceDiscount is not null");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountEqualTo(String value) {
            addCriterion("bizPriceDiscount =", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountNotEqualTo(String value) {
            addCriterion("bizPriceDiscount <>", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountGreaterThan(String value) {
            addCriterion("bizPriceDiscount >", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountGreaterThanOrEqualTo(String value) {
            addCriterion("bizPriceDiscount >=", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountLessThan(String value) {
            addCriterion("bizPriceDiscount <", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountLessThanOrEqualTo(String value) {
            addCriterion("bizPriceDiscount <=", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountLike(String value) {
            addCriterion("bizPriceDiscount like", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountNotLike(String value) {
            addCriterion("bizPriceDiscount not like", value, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountIn(List<String> values) {
            addCriterion("bizPriceDiscount in", values, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountNotIn(List<String> values) {
            addCriterion("bizPriceDiscount not in", values, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountBetween(String value1, String value2) {
            addCriterion("bizPriceDiscount between", value1, value2, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andBizpricediscountNotBetween(String value1, String value2) {
            addCriterion("bizPriceDiscount not between", value1, value2, "bizpricediscount");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceIsNull() {
            addCriterion("spiderPrice is null");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceIsNotNull() {
            addCriterion("spiderPrice is not null");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceEqualTo(Double value) {
            addCriterion("spiderPrice =", value, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceNotEqualTo(Double value) {
            addCriterion("spiderPrice <>", value, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceGreaterThan(Double value) {
            addCriterion("spiderPrice >", value, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceGreaterThanOrEqualTo(Double value) {
            addCriterion("spiderPrice >=", value, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceLessThan(Double value) {
            addCriterion("spiderPrice <", value, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceLessThanOrEqualTo(Double value) {
            addCriterion("spiderPrice <=", value, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceIn(List<Double> values) {
            addCriterion("spiderPrice in", values, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceNotIn(List<Double> values) {
            addCriterion("spiderPrice not in", values, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceBetween(Double value1, Double value2) {
            addCriterion("spiderPrice between", value1, value2, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andSpiderpriceNotBetween(Double value1, Double value2) {
            addCriterion("spiderPrice not between", value1, value2, "spiderprice");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeIsNull() {
            addCriterion("priceListSize is null");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeIsNotNull() {
            addCriterion("priceListSize is not null");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeEqualTo(String value) {
            addCriterion("priceListSize =", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeNotEqualTo(String value) {
            addCriterion("priceListSize <>", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeGreaterThan(String value) {
            addCriterion("priceListSize >", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeGreaterThanOrEqualTo(String value) {
            addCriterion("priceListSize >=", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeLessThan(String value) {
            addCriterion("priceListSize <", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeLessThanOrEqualTo(String value) {
            addCriterion("priceListSize <=", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeLike(String value) {
            addCriterion("priceListSize like", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeNotLike(String value) {
            addCriterion("priceListSize not like", value, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeIn(List<String> values) {
            addCriterion("priceListSize in", values, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeNotIn(List<String> values) {
            addCriterion("priceListSize not in", values, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeBetween(String value1, String value2) {
            addCriterion("priceListSize between", value1, value2, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andPricelistsizeNotBetween(String value1, String value2) {
            addCriterion("priceListSize not between", value1, value2, "pricelistsize");
            return (Criteria) this;
        }

        public Criteria andComparealipriceIsNull() {
            addCriterion("comparealiPrice is null");
            return (Criteria) this;
        }

        public Criteria andComparealipriceIsNotNull() {
            addCriterion("comparealiPrice is not null");
            return (Criteria) this;
        }

        public Criteria andComparealipriceEqualTo(String value) {
            addCriterion("comparealiPrice =", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceNotEqualTo(String value) {
            addCriterion("comparealiPrice <>", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceGreaterThan(String value) {
            addCriterion("comparealiPrice >", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceGreaterThanOrEqualTo(String value) {
            addCriterion("comparealiPrice >=", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceLessThan(String value) {
            addCriterion("comparealiPrice <", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceLessThanOrEqualTo(String value) {
            addCriterion("comparealiPrice <=", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceLike(String value) {
            addCriterion("comparealiPrice like", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceNotLike(String value) {
            addCriterion("comparealiPrice not like", value, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceIn(List<String> values) {
            addCriterion("comparealiPrice in", values, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceNotIn(List<String> values) {
            addCriterion("comparealiPrice not in", values, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceBetween(String value1, String value2) {
            addCriterion("comparealiPrice between", value1, value2, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andComparealipriceNotBetween(String value1, String value2) {
            addCriterion("comparealiPrice not between", value1, value2, "comparealiprice");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdIsNull() {
            addCriterion("group_buy_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdIsNotNull() {
            addCriterion("group_buy_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdEqualTo(Integer value) {
            addCriterion("group_buy_id =", value, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdNotEqualTo(Integer value) {
            addCriterion("group_buy_id <>", value, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdGreaterThan(Integer value) {
            addCriterion("group_buy_id >", value, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("group_buy_id >=", value, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdLessThan(Integer value) {
            addCriterion("group_buy_id <", value, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdLessThanOrEqualTo(Integer value) {
            addCriterion("group_buy_id <=", value, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdIn(List<Integer> values) {
            addCriterion("group_buy_id in", values, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdNotIn(List<Integer> values) {
            addCriterion("group_buy_id not in", values, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdBetween(Integer value1, Integer value2) {
            addCriterion("group_buy_id between", value1, value2, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andGroupBuyIdNotBetween(Integer value1, Integer value2) {
            addCriterion("group_buy_id not between", value1, value2, "groupBuyId");
            return (Criteria) this;
        }

        public Criteria andSkuid1688IsNull() {
            addCriterion("skuid_1688 is null");
            return (Criteria) this;
        }

        public Criteria andSkuid1688IsNotNull() {
            addCriterion("skuid_1688 is not null");
            return (Criteria) this;
        }

        public Criteria andSkuid1688EqualTo(String value) {
            addCriterion("skuid_1688 =", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688NotEqualTo(String value) {
            addCriterion("skuid_1688 <>", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688GreaterThan(String value) {
            addCriterion("skuid_1688 >", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688GreaterThanOrEqualTo(String value) {
            addCriterion("skuid_1688 >=", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688LessThan(String value) {
            addCriterion("skuid_1688 <", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688LessThanOrEqualTo(String value) {
            addCriterion("skuid_1688 <=", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688Like(String value) {
            addCriterion("skuid_1688 like", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688NotLike(String value) {
            addCriterion("skuid_1688 not like", value, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688In(List<String> values) {
            addCriterion("skuid_1688 in", values, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688NotIn(List<String> values) {
            addCriterion("skuid_1688 not in", values, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688Between(String value1, String value2) {
            addCriterion("skuid_1688 between", value1, value2, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andSkuid1688NotBetween(String value1, String value2) {
            addCriterion("skuid_1688 not between", value1, value2, "skuid1688");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductIsNull() {
            addCriterion("isFreeShipProduct is null");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductIsNotNull() {
            addCriterion("isFreeShipProduct is not null");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductEqualTo(Integer value) {
            addCriterion("isFreeShipProduct =", value, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductNotEqualTo(Integer value) {
            addCriterion("isFreeShipProduct <>", value, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductGreaterThan(Integer value) {
            addCriterion("isFreeShipProduct >", value, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductGreaterThanOrEqualTo(Integer value) {
            addCriterion("isFreeShipProduct >=", value, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductLessThan(Integer value) {
            addCriterion("isFreeShipProduct <", value, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductLessThanOrEqualTo(Integer value) {
            addCriterion("isFreeShipProduct <=", value, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductIn(List<Integer> values) {
            addCriterion("isFreeShipProduct in", values, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductNotIn(List<Integer> values) {
            addCriterion("isFreeShipProduct not in", values, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductBetween(Integer value1, Integer value2) {
            addCriterion("isFreeShipProduct between", value1, value2, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andIsfreeshipproductNotBetween(Integer value1, Integer value2) {
            addCriterion("isFreeShipProduct not between", value1, value2, "isfreeshipproduct");
            return (Criteria) this;
        }

        public Criteria andSamplefeeIsNull() {
            addCriterion("sampleFee is null");
            return (Criteria) this;
        }

        public Criteria andSamplefeeIsNotNull() {
            addCriterion("sampleFee is not null");
            return (Criteria) this;
        }

        public Criteria andSamplefeeEqualTo(Double value) {
            addCriterion("sampleFee =", value, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeNotEqualTo(Double value) {
            addCriterion("sampleFee <>", value, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeGreaterThan(Double value) {
            addCriterion("sampleFee >", value, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeGreaterThanOrEqualTo(Double value) {
            addCriterion("sampleFee >=", value, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeLessThan(Double value) {
            addCriterion("sampleFee <", value, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeLessThanOrEqualTo(Double value) {
            addCriterion("sampleFee <=", value, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeIn(List<Double> values) {
            addCriterion("sampleFee in", values, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeNotIn(List<Double> values) {
            addCriterion("sampleFee not in", values, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeBetween(Double value1, Double value2) {
            addCriterion("sampleFee between", value1, value2, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplefeeNotBetween(Double value1, Double value2) {
            addCriterion("sampleFee not between", value1, value2, "samplefee");
            return (Criteria) this;
        }

        public Criteria andSamplemoqIsNull() {
            addCriterion("sampleMoq is null");
            return (Criteria) this;
        }

        public Criteria andSamplemoqIsNotNull() {
            addCriterion("sampleMoq is not null");
            return (Criteria) this;
        }

        public Criteria andSamplemoqEqualTo(Integer value) {
            addCriterion("sampleMoq =", value, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqNotEqualTo(Integer value) {
            addCriterion("sampleMoq <>", value, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqGreaterThan(Integer value) {
            addCriterion("sampleMoq >", value, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqGreaterThanOrEqualTo(Integer value) {
            addCriterion("sampleMoq >=", value, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqLessThan(Integer value) {
            addCriterion("sampleMoq <", value, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqLessThanOrEqualTo(Integer value) {
            addCriterion("sampleMoq <=", value, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqIn(List<Integer> values) {
            addCriterion("sampleMoq in", values, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqNotIn(List<Integer> values) {
            addCriterion("sampleMoq not in", values, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqBetween(Integer value1, Integer value2) {
            addCriterion("sampleMoq between", value1, value2, "samplemoq");
            return (Criteria) this;
        }

        public Criteria andSamplemoqNotBetween(Integer value1, Integer value2) {
            addCriterion("sampleMoq not between", value1, value2, "samplemoq");
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