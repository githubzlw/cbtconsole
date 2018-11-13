package com.importExpress.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodsCarconfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GoodsCarconfigExample() {
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

        public Criteria andShippingnameIsNull() {
            addCriterion("shippingname is null");
            return (Criteria) this;
        }

        public Criteria andShippingnameIsNotNull() {
            addCriterion("shippingname is not null");
            return (Criteria) this;
        }

        public Criteria andShippingnameEqualTo(String value) {
            addCriterion("shippingname =", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameNotEqualTo(String value) {
            addCriterion("shippingname <>", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameGreaterThan(String value) {
            addCriterion("shippingname >", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameGreaterThanOrEqualTo(String value) {
            addCriterion("shippingname >=", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameLessThan(String value) {
            addCriterion("shippingname <", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameLessThanOrEqualTo(String value) {
            addCriterion("shippingname <=", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameLike(String value) {
            addCriterion("shippingname like", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameNotLike(String value) {
            addCriterion("shippingname not like", value, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameIn(List<String> values) {
            addCriterion("shippingname in", values, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameNotIn(List<String> values) {
            addCriterion("shippingname not in", values, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameBetween(String value1, String value2) {
            addCriterion("shippingname between", value1, value2, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingnameNotBetween(String value1, String value2) {
            addCriterion("shippingname not between", value1, value2, "shippingname");
            return (Criteria) this;
        }

        public Criteria andShippingcostIsNull() {
            addCriterion("shippingcost is null");
            return (Criteria) this;
        }

        public Criteria andShippingcostIsNotNull() {
            addCriterion("shippingcost is not null");
            return (Criteria) this;
        }

        public Criteria andShippingcostEqualTo(Double value) {
            addCriterion("shippingcost =", value, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostNotEqualTo(Double value) {
            addCriterion("shippingcost <>", value, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostGreaterThan(Double value) {
            addCriterion("shippingcost >", value, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostGreaterThanOrEqualTo(Double value) {
            addCriterion("shippingcost >=", value, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostLessThan(Double value) {
            addCriterion("shippingcost <", value, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostLessThanOrEqualTo(Double value) {
            addCriterion("shippingcost <=", value, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostIn(List<Double> values) {
            addCriterion("shippingcost in", values, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostNotIn(List<Double> values) {
            addCriterion("shippingcost not in", values, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostBetween(Double value1, Double value2) {
            addCriterion("shippingcost between", value1, value2, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingcostNotBetween(Double value1, Double value2) {
            addCriterion("shippingcost not between", value1, value2, "shippingcost");
            return (Criteria) this;
        }

        public Criteria andShippingdaysIsNull() {
            addCriterion("shippingdays is null");
            return (Criteria) this;
        }

        public Criteria andShippingdaysIsNotNull() {
            addCriterion("shippingdays is not null");
            return (Criteria) this;
        }

        public Criteria andShippingdaysEqualTo(String value) {
            addCriterion("shippingdays =", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysNotEqualTo(String value) {
            addCriterion("shippingdays <>", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysGreaterThan(String value) {
            addCriterion("shippingdays >", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysGreaterThanOrEqualTo(String value) {
            addCriterion("shippingdays >=", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysLessThan(String value) {
            addCriterion("shippingdays <", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysLessThanOrEqualTo(String value) {
            addCriterion("shippingdays <=", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysLike(String value) {
            addCriterion("shippingdays like", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysNotLike(String value) {
            addCriterion("shippingdays not like", value, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysIn(List<String> values) {
            addCriterion("shippingdays in", values, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysNotIn(List<String> values) {
            addCriterion("shippingdays not in", values, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysBetween(String value1, String value2) {
            addCriterion("shippingdays between", value1, value2, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andShippingdaysNotBetween(String value1, String value2) {
            addCriterion("shippingdays not between", value1, value2, "shippingdays");
            return (Criteria) this;
        }

        public Criteria andChangeexpressIsNull() {
            addCriterion("changeexpress is null");
            return (Criteria) this;
        }

        public Criteria andChangeexpressIsNotNull() {
            addCriterion("changeexpress is not null");
            return (Criteria) this;
        }

        public Criteria andChangeexpressEqualTo(Integer value) {
            addCriterion("changeexpress =", value, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressNotEqualTo(Integer value) {
            addCriterion("changeexpress <>", value, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressGreaterThan(Integer value) {
            addCriterion("changeexpress >", value, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressGreaterThanOrEqualTo(Integer value) {
            addCriterion("changeexpress >=", value, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressLessThan(Integer value) {
            addCriterion("changeexpress <", value, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressLessThanOrEqualTo(Integer value) {
            addCriterion("changeexpress <=", value, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressIn(List<Integer> values) {
            addCriterion("changeexpress in", values, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressNotIn(List<Integer> values) {
            addCriterion("changeexpress not in", values, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressBetween(Integer value1, Integer value2) {
            addCriterion("changeexpress between", value1, value2, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andChangeexpressNotBetween(Integer value1, Integer value2) {
            addCriterion("changeexpress not between", value1, value2, "changeexpress");
            return (Criteria) this;
        }

        public Criteria andSaveFreightIsNull() {
            addCriterion("save_freight is null");
            return (Criteria) this;
        }

        public Criteria andSaveFreightIsNotNull() {
            addCriterion("save_freight is not null");
            return (Criteria) this;
        }

        public Criteria andSaveFreightEqualTo(Double value) {
            addCriterion("save_freight =", value, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightNotEqualTo(Double value) {
            addCriterion("save_freight <>", value, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightGreaterThan(Double value) {
            addCriterion("save_freight >", value, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightGreaterThanOrEqualTo(Double value) {
            addCriterion("save_freight >=", value, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightLessThan(Double value) {
            addCriterion("save_freight <", value, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightLessThanOrEqualTo(Double value) {
            addCriterion("save_freight <=", value, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightIn(List<Double> values) {
            addCriterion("save_freight in", values, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightNotIn(List<Double> values) {
            addCriterion("save_freight not in", values, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightBetween(Double value1, Double value2) {
            addCriterion("save_freight between", value1, value2, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andSaveFreightNotBetween(Double value1, Double value2) {
            addCriterion("save_freight not between", value1, value2, "saveFreight");
            return (Criteria) this;
        }

        public Criteria andAddflagIsNull() {
            addCriterion("addFlag is null");
            return (Criteria) this;
        }

        public Criteria andAddflagIsNotNull() {
            addCriterion("addFlag is not null");
            return (Criteria) this;
        }

        public Criteria andAddflagEqualTo(Integer value) {
            addCriterion("addFlag =", value, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagNotEqualTo(Integer value) {
            addCriterion("addFlag <>", value, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagGreaterThan(Integer value) {
            addCriterion("addFlag >", value, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagGreaterThanOrEqualTo(Integer value) {
            addCriterion("addFlag >=", value, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagLessThan(Integer value) {
            addCriterion("addFlag <", value, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagLessThanOrEqualTo(Integer value) {
            addCriterion("addFlag <=", value, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagIn(List<Integer> values) {
            addCriterion("addFlag in", values, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagNotIn(List<Integer> values) {
            addCriterion("addFlag not in", values, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagBetween(Integer value1, Integer value2) {
            addCriterion("addFlag between", value1, value2, "addflag");
            return (Criteria) this;
        }

        public Criteria andAddflagNotBetween(Integer value1, Integer value2) {
            addCriterion("addFlag not between", value1, value2, "addflag");
            return (Criteria) this;
        }

        public Criteria andUsercookieidIsNull() {
            addCriterion("userCookieId is null");
            return (Criteria) this;
        }

        public Criteria andUsercookieidIsNotNull() {
            addCriterion("userCookieId is not null");
            return (Criteria) this;
        }

        public Criteria andUsercookieidEqualTo(String value) {
            addCriterion("userCookieId =", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidNotEqualTo(String value) {
            addCriterion("userCookieId <>", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidGreaterThan(String value) {
            addCriterion("userCookieId >", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidGreaterThanOrEqualTo(String value) {
            addCriterion("userCookieId >=", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidLessThan(String value) {
            addCriterion("userCookieId <", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidLessThanOrEqualTo(String value) {
            addCriterion("userCookieId <=", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidLike(String value) {
            addCriterion("userCookieId like", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidNotLike(String value) {
            addCriterion("userCookieId not like", value, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidIn(List<String> values) {
            addCriterion("userCookieId in", values, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidNotIn(List<String> values) {
            addCriterion("userCookieId not in", values, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidBetween(String value1, String value2) {
            addCriterion("userCookieId between", value1, value2, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andUsercookieidNotBetween(String value1, String value2) {
            addCriterion("userCookieId not between", value1, value2, "usercookieid");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceIsNull() {
            addCriterion("sum_type_price is null");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceIsNotNull() {
            addCriterion("sum_type_price is not null");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceEqualTo(String value) {
            addCriterion("sum_type_price =", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceNotEqualTo(String value) {
            addCriterion("sum_type_price <>", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceGreaterThan(String value) {
            addCriterion("sum_type_price >", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceGreaterThanOrEqualTo(String value) {
            addCriterion("sum_type_price >=", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceLessThan(String value) {
            addCriterion("sum_type_price <", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceLessThanOrEqualTo(String value) {
            addCriterion("sum_type_price <=", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceLike(String value) {
            addCriterion("sum_type_price like", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceNotLike(String value) {
            addCriterion("sum_type_price not like", value, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceIn(List<String> values) {
            addCriterion("sum_type_price in", values, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceNotIn(List<String> values) {
            addCriterion("sum_type_price not in", values, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceBetween(String value1, String value2) {
            addCriterion("sum_type_price between", value1, value2, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andSumTypePriceNotBetween(String value1, String value2) {
            addCriterion("sum_type_price not between", value1, value2, "sumTypePrice");
            return (Criteria) this;
        }

        public Criteria andTotalFreightIsNull() {
            addCriterion("total_freight is null");
            return (Criteria) this;
        }

        public Criteria andTotalFreightIsNotNull() {
            addCriterion("total_freight is not null");
            return (Criteria) this;
        }

        public Criteria andTotalFreightEqualTo(Double value) {
            addCriterion("total_freight =", value, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightNotEqualTo(Double value) {
            addCriterion("total_freight <>", value, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightGreaterThan(Double value) {
            addCriterion("total_freight >", value, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightGreaterThanOrEqualTo(Double value) {
            addCriterion("total_freight >=", value, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightLessThan(Double value) {
            addCriterion("total_freight <", value, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightLessThanOrEqualTo(Double value) {
            addCriterion("total_freight <=", value, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightIn(List<Double> values) {
            addCriterion("total_freight in", values, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightNotIn(List<Double> values) {
            addCriterion("total_freight not in", values, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightBetween(Double value1, Double value2) {
            addCriterion("total_freight between", value1, value2, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andTotalFreightNotBetween(Double value1, Double value2) {
            addCriterion("total_freight not between", value1, value2, "totalFreight");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("updateTime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("updateTime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("updateTime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updateTime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("updateTime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("updateTime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("updateTime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("updateTime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("updateTime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("updateTime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceIsNull() {
            addCriterion("fastShipBalance is null");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceIsNotNull() {
            addCriterion("fastShipBalance is not null");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceEqualTo(Double value) {
            addCriterion("fastShipBalance =", value, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceNotEqualTo(Double value) {
            addCriterion("fastShipBalance <>", value, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceGreaterThan(Double value) {
            addCriterion("fastShipBalance >", value, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceGreaterThanOrEqualTo(Double value) {
            addCriterion("fastShipBalance >=", value, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceLessThan(Double value) {
            addCriterion("fastShipBalance <", value, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceLessThanOrEqualTo(Double value) {
            addCriterion("fastShipBalance <=", value, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceIn(List<Double> values) {
            addCriterion("fastShipBalance in", values, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceNotIn(List<Double> values) {
            addCriterion("fastShipBalance not in", values, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceBetween(Double value1, Double value2) {
            addCriterion("fastShipBalance between", value1, value2, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andFastshipbalanceNotBetween(Double value1, Double value2) {
            addCriterion("fastShipBalance not between", value1, value2, "fastshipbalance");
            return (Criteria) this;
        }

        public Criteria andUsermarkIsNull() {
            addCriterion("usermark is null");
            return (Criteria) this;
        }

        public Criteria andUsermarkIsNotNull() {
            addCriterion("usermark is not null");
            return (Criteria) this;
        }

        public Criteria andUsermarkEqualTo(String value) {
            addCriterion("usermark =", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkNotEqualTo(String value) {
            addCriterion("usermark <>", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkGreaterThan(String value) {
            addCriterion("usermark >", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkGreaterThanOrEqualTo(String value) {
            addCriterion("usermark >=", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkLessThan(String value) {
            addCriterion("usermark <", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkLessThanOrEqualTo(String value) {
            addCriterion("usermark <=", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkLike(String value) {
            addCriterion("usermark like", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkNotLike(String value) {
            addCriterion("usermark not like", value, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkIn(List<String> values) {
            addCriterion("usermark in", values, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkNotIn(List<String> values) {
            addCriterion("usermark not in", values, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkBetween(String value1, String value2) {
            addCriterion("usermark between", value1, value2, "usermark");
            return (Criteria) this;
        }

        public Criteria andUsermarkNotBetween(String value1, String value2) {
            addCriterion("usermark not between", value1, value2, "usermark");
            return (Criteria) this;
        }

        public Criteria andNeedcheckIsNull() {
            addCriterion("needCheck is null");
            return (Criteria) this;
        }

        public Criteria andNeedcheckIsNotNull() {
            addCriterion("needCheck is not null");
            return (Criteria) this;
        }

        public Criteria andNeedcheckEqualTo(Integer value) {
            addCriterion("needCheck =", value, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckNotEqualTo(Integer value) {
            addCriterion("needCheck <>", value, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckGreaterThan(Integer value) {
            addCriterion("needCheck >", value, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckGreaterThanOrEqualTo(Integer value) {
            addCriterion("needCheck >=", value, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckLessThan(Integer value) {
            addCriterion("needCheck <", value, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckLessThanOrEqualTo(Integer value) {
            addCriterion("needCheck <=", value, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckIn(List<Integer> values) {
            addCriterion("needCheck in", values, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckNotIn(List<Integer> values) {
            addCriterion("needCheck not in", values, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckBetween(Integer value1, Integer value2) {
            addCriterion("needCheck between", value1, value2, "needcheck");
            return (Criteria) this;
        }

        public Criteria andNeedcheckNotBetween(Integer value1, Integer value2) {
            addCriterion("needCheck not between", value1, value2, "needcheck");
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