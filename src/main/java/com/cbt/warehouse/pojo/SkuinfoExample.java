package com.cbt.warehouse.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SkuinfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SkuinfoExample() {
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

        public Criteria andSkuIsNull() {
            addCriterion("sku is null");
            return (Criteria) this;
        }

        public Criteria andSkuIsNotNull() {
            addCriterion("sku is not null");
            return (Criteria) this;
        }

        public Criteria andSkuEqualTo(String value) {
            addCriterion("sku =", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotEqualTo(String value) {
            addCriterion("sku <>", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuGreaterThan(String value) {
            addCriterion("sku >", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuGreaterThanOrEqualTo(String value) {
            addCriterion("sku >=", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLessThan(String value) {
            addCriterion("sku <", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLessThanOrEqualTo(String value) {
            addCriterion("sku <=", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLike(String value) {
            addCriterion("sku like", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotLike(String value) {
            addCriterion("sku not like", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuIn(List<String> values) {
            addCriterion("sku in", values, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotIn(List<String> values) {
            addCriterion("sku not in", values, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuBetween(String value1, String value2) {
            addCriterion("sku between", value1, value2, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotBetween(String value1, String value2) {
            addCriterion("sku not between", value1, value2, "sku");
            return (Criteria) this;
        }

        public Criteria andSkucnnameIsNull() {
            addCriterion("skuCnName is null");
            return (Criteria) this;
        }

        public Criteria andSkucnnameIsNotNull() {
            addCriterion("skuCnName is not null");
            return (Criteria) this;
        }

        public Criteria andSkucnnameEqualTo(String value) {
            addCriterion("skuCnName =", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameNotEqualTo(String value) {
            addCriterion("skuCnName <>", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameGreaterThan(String value) {
            addCriterion("skuCnName >", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameGreaterThanOrEqualTo(String value) {
            addCriterion("skuCnName >=", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameLessThan(String value) {
            addCriterion("skuCnName <", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameLessThanOrEqualTo(String value) {
            addCriterion("skuCnName <=", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameLike(String value) {
            addCriterion("skuCnName like", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameNotLike(String value) {
            addCriterion("skuCnName not like", value, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameIn(List<String> values) {
            addCriterion("skuCnName in", values, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameNotIn(List<String> values) {
            addCriterion("skuCnName not in", values, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameBetween(String value1, String value2) {
            addCriterion("skuCnName between", value1, value2, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkucnnameNotBetween(String value1, String value2) {
            addCriterion("skuCnName not between", value1, value2, "skucnname");
            return (Criteria) this;
        }

        public Criteria andSkuennameIsNull() {
            addCriterion("skuEnName is null");
            return (Criteria) this;
        }

        public Criteria andSkuennameIsNotNull() {
            addCriterion("skuEnName is not null");
            return (Criteria) this;
        }

        public Criteria andSkuennameEqualTo(String value) {
            addCriterion("skuEnName =", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameNotEqualTo(String value) {
            addCriterion("skuEnName <>", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameGreaterThan(String value) {
            addCriterion("skuEnName >", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameGreaterThanOrEqualTo(String value) {
            addCriterion("skuEnName >=", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameLessThan(String value) {
            addCriterion("skuEnName <", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameLessThanOrEqualTo(String value) {
            addCriterion("skuEnName <=", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameLike(String value) {
            addCriterion("skuEnName like", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameNotLike(String value) {
            addCriterion("skuEnName not like", value, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameIn(List<String> values) {
            addCriterion("skuEnName in", values, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameNotIn(List<String> values) {
            addCriterion("skuEnName not in", values, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameBetween(String value1, String value2) {
            addCriterion("skuEnName between", value1, value2, "skuenname");
            return (Criteria) this;
        }

        public Criteria andSkuennameNotBetween(String value1, String value2) {
            addCriterion("skuEnName not between", value1, value2, "skuenname");
            return (Criteria) this;
        }

        public Criteria andStatuIsNull() {
            addCriterion("statu is null");
            return (Criteria) this;
        }

        public Criteria andStatuIsNotNull() {
            addCriterion("statu is not null");
            return (Criteria) this;
        }

        public Criteria andStatuEqualTo(Integer value) {
            addCriterion("statu =", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuNotEqualTo(Integer value) {
            addCriterion("statu <>", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuGreaterThan(Integer value) {
            addCriterion("statu >", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuGreaterThanOrEqualTo(Integer value) {
            addCriterion("statu >=", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuLessThan(Integer value) {
            addCriterion("statu <", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuLessThanOrEqualTo(Integer value) {
            addCriterion("statu <=", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuIn(List<Integer> values) {
            addCriterion("statu in", values, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuNotIn(List<Integer> values) {
            addCriterion("statu not in", values, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuBetween(Integer value1, Integer value2) {
            addCriterion("statu between", value1, value2, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuNotBetween(Integer value1, Integer value2) {
            addCriterion("statu not between", value1, value2, "statu");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryIsNull() {
            addCriterion("productDirectory is null");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryIsNotNull() {
            addCriterion("productDirectory is not null");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryEqualTo(String value) {
            addCriterion("productDirectory =", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryNotEqualTo(String value) {
            addCriterion("productDirectory <>", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryGreaterThan(String value) {
            addCriterion("productDirectory >", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryGreaterThanOrEqualTo(String value) {
            addCriterion("productDirectory >=", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryLessThan(String value) {
            addCriterion("productDirectory <", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryLessThanOrEqualTo(String value) {
            addCriterion("productDirectory <=", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryLike(String value) {
            addCriterion("productDirectory like", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryNotLike(String value) {
            addCriterion("productDirectory not like", value, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryIn(List<String> values) {
            addCriterion("productDirectory in", values, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryNotIn(List<String> values) {
            addCriterion("productDirectory not in", values, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryBetween(String value1, String value2) {
            addCriterion("productDirectory between", value1, value2, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andProductdirectoryNotBetween(String value1, String value2) {
            addCriterion("productDirectory not between", value1, value2, "productdirectory");
            return (Criteria) this;
        }

        public Criteria andSkusumIsNull() {
            addCriterion("skuSum is null");
            return (Criteria) this;
        }

        public Criteria andSkusumIsNotNull() {
            addCriterion("skuSum is not null");
            return (Criteria) this;
        }

        public Criteria andSkusumEqualTo(Integer value) {
            addCriterion("skuSum =", value, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumNotEqualTo(Integer value) {
            addCriterion("skuSum <>", value, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumGreaterThan(Integer value) {
            addCriterion("skuSum >", value, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumGreaterThanOrEqualTo(Integer value) {
            addCriterion("skuSum >=", value, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumLessThan(Integer value) {
            addCriterion("skuSum <", value, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumLessThanOrEqualTo(Integer value) {
            addCriterion("skuSum <=", value, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumIn(List<Integer> values) {
            addCriterion("skuSum in", values, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumNotIn(List<Integer> values) {
            addCriterion("skuSum not in", values, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumBetween(Integer value1, Integer value2) {
            addCriterion("skuSum between", value1, value2, "skusum");
            return (Criteria) this;
        }

        public Criteria andSkusumNotBetween(Integer value1, Integer value2) {
            addCriterion("skuSum not between", value1, value2, "skusum");
            return (Criteria) this;
        }

        public Criteria andShippedIsNull() {
            addCriterion("shipped is null");
            return (Criteria) this;
        }

        public Criteria andShippedIsNotNull() {
            addCriterion("shipped is not null");
            return (Criteria) this;
        }

        public Criteria andShippedEqualTo(String value) {
            addCriterion("shipped =", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedNotEqualTo(String value) {
            addCriterion("shipped <>", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedGreaterThan(String value) {
            addCriterion("shipped >", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedGreaterThanOrEqualTo(String value) {
            addCriterion("shipped >=", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedLessThan(String value) {
            addCriterion("shipped <", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedLessThanOrEqualTo(String value) {
            addCriterion("shipped <=", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedLike(String value) {
            addCriterion("shipped like", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedNotLike(String value) {
            addCriterion("shipped not like", value, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedIn(List<String> values) {
            addCriterion("shipped in", values, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedNotIn(List<String> values) {
            addCriterion("shipped not in", values, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedBetween(String value1, String value2) {
            addCriterion("shipped between", value1, value2, "shipped");
            return (Criteria) this;
        }

        public Criteria andShippedNotBetween(String value1, String value2) {
            addCriterion("shipped not between", value1, value2, "shipped");
            return (Criteria) this;
        }

        public Criteria andWeightIsNull() {
            addCriterion("weight is null");
            return (Criteria) this;
        }

        public Criteria andWeightIsNotNull() {
            addCriterion("weight is not null");
            return (Criteria) this;
        }

        public Criteria andWeightEqualTo(String value) {
            addCriterion("weight =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(String value) {
            addCriterion("weight <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(String value) {
            addCriterion("weight >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(String value) {
            addCriterion("weight >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(String value) {
            addCriterion("weight <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(String value) {
            addCriterion("weight <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLike(String value) {
            addCriterion("weight like", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotLike(String value) {
            addCriterion("weight not like", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<String> values) {
            addCriterion("weight in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<String> values) {
            addCriterion("weight not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(String value1, String value2) {
            addCriterion("weight between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(String value1, String value2) {
            addCriterion("weight not between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkIsNull() {
            addCriterion("skuImgLink is null");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkIsNotNull() {
            addCriterion("skuImgLink is not null");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkEqualTo(String value) {
            addCriterion("skuImgLink =", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkNotEqualTo(String value) {
            addCriterion("skuImgLink <>", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkGreaterThan(String value) {
            addCriterion("skuImgLink >", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkGreaterThanOrEqualTo(String value) {
            addCriterion("skuImgLink >=", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkLessThan(String value) {
            addCriterion("skuImgLink <", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkLessThanOrEqualTo(String value) {
            addCriterion("skuImgLink <=", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkLike(String value) {
            addCriterion("skuImgLink like", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkNotLike(String value) {
            addCriterion("skuImgLink not like", value, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkIn(List<String> values) {
            addCriterion("skuImgLink in", values, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkNotIn(List<String> values) {
            addCriterion("skuImgLink not in", values, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkBetween(String value1, String value2) {
            addCriterion("skuImgLink between", value1, value2, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andSkuimglinkNotBetween(String value1, String value2) {
            addCriterion("skuImgLink not between", value1, value2, "skuimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkIsNull() {
            addCriterion("shipImgLink is null");
            return (Criteria) this;
        }

        public Criteria andShipimglinkIsNotNull() {
            addCriterion("shipImgLink is not null");
            return (Criteria) this;
        }

        public Criteria andShipimglinkEqualTo(String value) {
            addCriterion("shipImgLink =", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkNotEqualTo(String value) {
            addCriterion("shipImgLink <>", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkGreaterThan(String value) {
            addCriterion("shipImgLink >", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkGreaterThanOrEqualTo(String value) {
            addCriterion("shipImgLink >=", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkLessThan(String value) {
            addCriterion("shipImgLink <", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkLessThanOrEqualTo(String value) {
            addCriterion("shipImgLink <=", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkLike(String value) {
            addCriterion("shipImgLink like", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkNotLike(String value) {
            addCriterion("shipImgLink not like", value, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkIn(List<String> values) {
            addCriterion("shipImgLink in", values, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkNotIn(List<String> values) {
            addCriterion("shipImgLink not in", values, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkBetween(String value1, String value2) {
            addCriterion("shipImgLink between", value1, value2, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andShipimglinkNotBetween(String value1, String value2) {
            addCriterion("shipImgLink not between", value1, value2, "shipimglink");
            return (Criteria) this;
        }

        public Criteria andWarehouseIsNull() {
            addCriterion("warehouse is null");
            return (Criteria) this;
        }

        public Criteria andWarehouseIsNotNull() {
            addCriterion("warehouse is not null");
            return (Criteria) this;
        }

        public Criteria andWarehouseEqualTo(String value) {
            addCriterion("warehouse =", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseNotEqualTo(String value) {
            addCriterion("warehouse <>", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseGreaterThan(String value) {
            addCriterion("warehouse >", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseGreaterThanOrEqualTo(String value) {
            addCriterion("warehouse >=", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseLessThan(String value) {
            addCriterion("warehouse <", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseLessThanOrEqualTo(String value) {
            addCriterion("warehouse <=", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseLike(String value) {
            addCriterion("warehouse like", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseNotLike(String value) {
            addCriterion("warehouse not like", value, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseIn(List<String> values) {
            addCriterion("warehouse in", values, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseNotIn(List<String> values) {
            addCriterion("warehouse not in", values, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseBetween(String value1, String value2) {
            addCriterion("warehouse between", value1, value2, "warehouse");
            return (Criteria) this;
        }

        public Criteria andWarehouseNotBetween(String value1, String value2) {
            addCriterion("warehouse not between", value1, value2, "warehouse");
            return (Criteria) this;
        }

        public Criteria andUnitcostIsNull() {
            addCriterion("unitCost is null");
            return (Criteria) this;
        }

        public Criteria andUnitcostIsNotNull() {
            addCriterion("unitCost is not null");
            return (Criteria) this;
        }

        public Criteria andUnitcostEqualTo(BigDecimal value) {
            addCriterion("unitCost =", value, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostNotEqualTo(BigDecimal value) {
            addCriterion("unitCost <>", value, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostGreaterThan(BigDecimal value) {
            addCriterion("unitCost >", value, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("unitCost >=", value, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostLessThan(BigDecimal value) {
            addCriterion("unitCost <", value, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostLessThanOrEqualTo(BigDecimal value) {
            addCriterion("unitCost <=", value, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostIn(List<BigDecimal> values) {
            addCriterion("unitCost in", values, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostNotIn(List<BigDecimal> values) {
            addCriterion("unitCost not in", values, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unitCost between", value1, value2, "unitcost");
            return (Criteria) this;
        }

        public Criteria andUnitcostNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unitCost not between", value1, value2, "unitcost");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("createTime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("createTime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("createTime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createtime");
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