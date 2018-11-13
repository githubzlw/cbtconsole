package com.cbt.warehouse.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DropshiporderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DropshiporderExample() {
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

        public Criteria andOrderidIsNull() {
            addCriterion("orderid is null");
            return (Criteria) this;
        }

        public Criteria andOrderidIsNotNull() {
            addCriterion("orderid is not null");
            return (Criteria) this;
        }

        public Criteria andOrderidEqualTo(Integer value) {
            addCriterion("orderid =", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotEqualTo(Integer value) {
            addCriterion("orderid <>", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThan(Integer value) {
            addCriterion("orderid >", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThanOrEqualTo(Integer value) {
            addCriterion("orderid >=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThan(Integer value) {
            addCriterion("orderid <", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThanOrEqualTo(Integer value) {
            addCriterion("orderid <=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidIn(List<Integer> values) {
            addCriterion("orderid in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotIn(List<Integer> values) {
            addCriterion("orderid not in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidBetween(Integer value1, Integer value2) {
            addCriterion("orderid between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotBetween(Integer value1, Integer value2) {
            addCriterion("orderid not between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoIsNull() {
            addCriterion("parent_order_no is null");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoIsNotNull() {
            addCriterion("parent_order_no is not null");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoEqualTo(String value) {
            addCriterion("parent_order_no =", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoNotEqualTo(String value) {
            addCriterion("parent_order_no <>", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoGreaterThan(String value) {
            addCriterion("parent_order_no >", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("parent_order_no >=", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoLessThan(String value) {
            addCriterion("parent_order_no <", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoLessThanOrEqualTo(String value) {
            addCriterion("parent_order_no <=", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoLike(String value) {
            addCriterion("parent_order_no like", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoNotLike(String value) {
            addCriterion("parent_order_no not like", value, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoIn(List<String> values) {
            addCriterion("parent_order_no in", values, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoNotIn(List<String> values) {
            addCriterion("parent_order_no not in", values, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoBetween(String value1, String value2) {
            addCriterion("parent_order_no between", value1, value2, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andParentOrderNoNotBetween(String value1, String value2) {
            addCriterion("parent_order_no not between", value1, value2, "parentOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoIsNull() {
            addCriterion("child_order_no is null");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoIsNotNull() {
            addCriterion("child_order_no is not null");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoEqualTo(String value) {
            addCriterion("child_order_no =", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoNotEqualTo(String value) {
            addCriterion("child_order_no <>", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoGreaterThan(String value) {
            addCriterion("child_order_no >", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("child_order_no >=", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoLessThan(String value) {
            addCriterion("child_order_no <", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoLessThanOrEqualTo(String value) {
            addCriterion("child_order_no <=", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoLike(String value) {
            addCriterion("child_order_no like", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoNotLike(String value) {
            addCriterion("child_order_no not like", value, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoIn(List<String> values) {
            addCriterion("child_order_no in", values, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoNotIn(List<String> values) {
            addCriterion("child_order_no not in", values, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoBetween(String value1, String value2) {
            addCriterion("child_order_no between", value1, value2, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andChildOrderNoNotBetween(String value1, String value2) {
            addCriterion("child_order_no not between", value1, value2, "childOrderNo");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNull() {
            addCriterion("address_id is null");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNotNull() {
            addCriterion("address_id is not null");
            return (Criteria) this;
        }

        public Criteria andAddressIdEqualTo(Integer value) {
            addCriterion("address_id =", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotEqualTo(Integer value) {
            addCriterion("address_id <>", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThan(Integer value) {
            addCriterion("address_id >", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("address_id >=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThan(Integer value) {
            addCriterion("address_id <", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThanOrEqualTo(Integer value) {
            addCriterion("address_id <=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdIn(List<Integer> values) {
            addCriterion("address_id in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotIn(List<Integer> values) {
            addCriterion("address_id not in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdBetween(Integer value1, Integer value2) {
            addCriterion("address_id between", value1, value2, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotBetween(Integer value1, Integer value2) {
            addCriterion("address_id not between", value1, value2, "addressId");
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

        public Criteria andPackagStyleIsNull() {
            addCriterion("packag_style is null");
            return (Criteria) this;
        }

        public Criteria andPackagStyleIsNotNull() {
            addCriterion("packag_style is not null");
            return (Criteria) this;
        }

        public Criteria andPackagStyleEqualTo(Integer value) {
            addCriterion("packag_style =", value, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleNotEqualTo(Integer value) {
            addCriterion("packag_style <>", value, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleGreaterThan(Integer value) {
            addCriterion("packag_style >", value, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleGreaterThanOrEqualTo(Integer value) {
            addCriterion("packag_style >=", value, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleLessThan(Integer value) {
            addCriterion("packag_style <", value, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleLessThanOrEqualTo(Integer value) {
            addCriterion("packag_style <=", value, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleIn(List<Integer> values) {
            addCriterion("packag_style in", values, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleNotIn(List<Integer> values) {
            addCriterion("packag_style not in", values, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleBetween(Integer value1, Integer value2) {
            addCriterion("packag_style between", value1, value2, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andPackagStyleNotBetween(Integer value1, Integer value2) {
            addCriterion("packag_style not between", value1, value2, "packagStyle");
            return (Criteria) this;
        }

        public Criteria andModeTransportIsNull() {
            addCriterion("mode_transport is null");
            return (Criteria) this;
        }

        public Criteria andModeTransportIsNotNull() {
            addCriterion("mode_transport is not null");
            return (Criteria) this;
        }

        public Criteria andModeTransportEqualTo(String value) {
            addCriterion("mode_transport =", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportNotEqualTo(String value) {
            addCriterion("mode_transport <>", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportGreaterThan(String value) {
            addCriterion("mode_transport >", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportGreaterThanOrEqualTo(String value) {
            addCriterion("mode_transport >=", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportLessThan(String value) {
            addCriterion("mode_transport <", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportLessThanOrEqualTo(String value) {
            addCriterion("mode_transport <=", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportLike(String value) {
            addCriterion("mode_transport like", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportNotLike(String value) {
            addCriterion("mode_transport not like", value, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportIn(List<String> values) {
            addCriterion("mode_transport in", values, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportNotIn(List<String> values) {
            addCriterion("mode_transport not in", values, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportBetween(String value1, String value2) {
            addCriterion("mode_transport between", value1, value2, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andModeTransportNotBetween(String value1, String value2) {
            addCriterion("mode_transport not between", value1, value2, "modeTransport");
            return (Criteria) this;
        }

        public Criteria andServiceFeeIsNull() {
            addCriterion("service_fee is null");
            return (Criteria) this;
        }

        public Criteria andServiceFeeIsNotNull() {
            addCriterion("service_fee is not null");
            return (Criteria) this;
        }

        public Criteria andServiceFeeEqualTo(String value) {
            addCriterion("service_fee =", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeNotEqualTo(String value) {
            addCriterion("service_fee <>", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeGreaterThan(String value) {
            addCriterion("service_fee >", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeGreaterThanOrEqualTo(String value) {
            addCriterion("service_fee >=", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeLessThan(String value) {
            addCriterion("service_fee <", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeLessThanOrEqualTo(String value) {
            addCriterion("service_fee <=", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeLike(String value) {
            addCriterion("service_fee like", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeNotLike(String value) {
            addCriterion("service_fee not like", value, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeIn(List<String> values) {
            addCriterion("service_fee in", values, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeNotIn(List<String> values) {
            addCriterion("service_fee not in", values, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeBetween(String value1, String value2) {
            addCriterion("service_fee between", value1, value2, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andServiceFeeNotBetween(String value1, String value2) {
            addCriterion("service_fee not between", value1, value2, "serviceFee");
            return (Criteria) this;
        }

        public Criteria andProductCostIsNull() {
            addCriterion("product_cost is null");
            return (Criteria) this;
        }

        public Criteria andProductCostIsNotNull() {
            addCriterion("product_cost is not null");
            return (Criteria) this;
        }

        public Criteria andProductCostEqualTo(String value) {
            addCriterion("product_cost =", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostNotEqualTo(String value) {
            addCriterion("product_cost <>", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostGreaterThan(String value) {
            addCriterion("product_cost >", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostGreaterThanOrEqualTo(String value) {
            addCriterion("product_cost >=", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostLessThan(String value) {
            addCriterion("product_cost <", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostLessThanOrEqualTo(String value) {
            addCriterion("product_cost <=", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostLike(String value) {
            addCriterion("product_cost like", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostNotLike(String value) {
            addCriterion("product_cost not like", value, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostIn(List<String> values) {
            addCriterion("product_cost in", values, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostNotIn(List<String> values) {
            addCriterion("product_cost not in", values, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostBetween(String value1, String value2) {
            addCriterion("product_cost between", value1, value2, "productCost");
            return (Criteria) this;
        }

        public Criteria andProductCostNotBetween(String value1, String value2) {
            addCriterion("product_cost not between", value1, value2, "productCost");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightIsNull() {
            addCriterion("domestic_freight is null");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightIsNotNull() {
            addCriterion("domestic_freight is not null");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightEqualTo(String value) {
            addCriterion("domestic_freight =", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightNotEqualTo(String value) {
            addCriterion("domestic_freight <>", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightGreaterThan(String value) {
            addCriterion("domestic_freight >", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightGreaterThanOrEqualTo(String value) {
            addCriterion("domestic_freight >=", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightLessThan(String value) {
            addCriterion("domestic_freight <", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightLessThanOrEqualTo(String value) {
            addCriterion("domestic_freight <=", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightLike(String value) {
            addCriterion("domestic_freight like", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightNotLike(String value) {
            addCriterion("domestic_freight not like", value, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightIn(List<String> values) {
            addCriterion("domestic_freight in", values, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightNotIn(List<String> values) {
            addCriterion("domestic_freight not in", values, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightBetween(String value1, String value2) {
            addCriterion("domestic_freight between", value1, value2, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andDomesticFreightNotBetween(String value1, String value2) {
            addCriterion("domestic_freight not between", value1, value2, "domesticFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightIsNull() {
            addCriterion("foreign_freight is null");
            return (Criteria) this;
        }

        public Criteria andForeignFreightIsNotNull() {
            addCriterion("foreign_freight is not null");
            return (Criteria) this;
        }

        public Criteria andForeignFreightEqualTo(String value) {
            addCriterion("foreign_freight =", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightNotEqualTo(String value) {
            addCriterion("foreign_freight <>", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightGreaterThan(String value) {
            addCriterion("foreign_freight >", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightGreaterThanOrEqualTo(String value) {
            addCriterion("foreign_freight >=", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightLessThan(String value) {
            addCriterion("foreign_freight <", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightLessThanOrEqualTo(String value) {
            addCriterion("foreign_freight <=", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightLike(String value) {
            addCriterion("foreign_freight like", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightNotLike(String value) {
            addCriterion("foreign_freight not like", value, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightIn(List<String> values) {
            addCriterion("foreign_freight in", values, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightNotIn(List<String> values) {
            addCriterion("foreign_freight not in", values, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightBetween(String value1, String value2) {
            addCriterion("foreign_freight between", value1, value2, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andForeignFreightNotBetween(String value1, String value2) {
            addCriterion("foreign_freight not between", value1, value2, "foreignFreight");
            return (Criteria) this;
        }

        public Criteria andActualAllincostIsNull() {
            addCriterion("actual_allincost is null");
            return (Criteria) this;
        }

        public Criteria andActualAllincostIsNotNull() {
            addCriterion("actual_allincost is not null");
            return (Criteria) this;
        }

        public Criteria andActualAllincostEqualTo(Double value) {
            addCriterion("actual_allincost =", value, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostNotEqualTo(Double value) {
            addCriterion("actual_allincost <>", value, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostGreaterThan(Double value) {
            addCriterion("actual_allincost >", value, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostGreaterThanOrEqualTo(Double value) {
            addCriterion("actual_allincost >=", value, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostLessThan(Double value) {
            addCriterion("actual_allincost <", value, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostLessThanOrEqualTo(Double value) {
            addCriterion("actual_allincost <=", value, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostIn(List<Double> values) {
            addCriterion("actual_allincost in", values, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostNotIn(List<Double> values) {
            addCriterion("actual_allincost not in", values, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostBetween(Double value1, Double value2) {
            addCriterion("actual_allincost between", value1, value2, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andActualAllincostNotBetween(Double value1, Double value2) {
            addCriterion("actual_allincost not between", value1, value2, "actualAllincost");
            return (Criteria) this;
        }

        public Criteria andPayPriceIsNull() {
            addCriterion("pay_price is null");
            return (Criteria) this;
        }

        public Criteria andPayPriceIsNotNull() {
            addCriterion("pay_price is not null");
            return (Criteria) this;
        }

        public Criteria andPayPriceEqualTo(String value) {
            addCriterion("pay_price =", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceNotEqualTo(String value) {
            addCriterion("pay_price <>", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceGreaterThan(String value) {
            addCriterion("pay_price >", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceGreaterThanOrEqualTo(String value) {
            addCriterion("pay_price >=", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceLessThan(String value) {
            addCriterion("pay_price <", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceLessThanOrEqualTo(String value) {
            addCriterion("pay_price <=", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceLike(String value) {
            addCriterion("pay_price like", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceNotLike(String value) {
            addCriterion("pay_price not like", value, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceIn(List<String> values) {
            addCriterion("pay_price in", values, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceNotIn(List<String> values) {
            addCriterion("pay_price not in", values, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceBetween(String value1, String value2) {
            addCriterion("pay_price between", value1, value2, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceNotBetween(String value1, String value2) {
            addCriterion("pay_price not between", value1, value2, "payPrice");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowIsNull() {
            addCriterion("pay_price_tow is null");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowIsNotNull() {
            addCriterion("pay_price_tow is not null");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowEqualTo(String value) {
            addCriterion("pay_price_tow =", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowNotEqualTo(String value) {
            addCriterion("pay_price_tow <>", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowGreaterThan(String value) {
            addCriterion("pay_price_tow >", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowGreaterThanOrEqualTo(String value) {
            addCriterion("pay_price_tow >=", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowLessThan(String value) {
            addCriterion("pay_price_tow <", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowLessThanOrEqualTo(String value) {
            addCriterion("pay_price_tow <=", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowLike(String value) {
            addCriterion("pay_price_tow like", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowNotLike(String value) {
            addCriterion("pay_price_tow not like", value, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowIn(List<String> values) {
            addCriterion("pay_price_tow in", values, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowNotIn(List<String> values) {
            addCriterion("pay_price_tow not in", values, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowBetween(String value1, String value2) {
            addCriterion("pay_price_tow between", value1, value2, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceTowNotBetween(String value1, String value2) {
            addCriterion("pay_price_tow not between", value1, value2, "payPriceTow");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeIsNull() {
            addCriterion("pay_price_three is null");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeIsNotNull() {
            addCriterion("pay_price_three is not null");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeEqualTo(String value) {
            addCriterion("pay_price_three =", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeNotEqualTo(String value) {
            addCriterion("pay_price_three <>", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeGreaterThan(String value) {
            addCriterion("pay_price_three >", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeGreaterThanOrEqualTo(String value) {
            addCriterion("pay_price_three >=", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeLessThan(String value) {
            addCriterion("pay_price_three <", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeLessThanOrEqualTo(String value) {
            addCriterion("pay_price_three <=", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeLike(String value) {
            addCriterion("pay_price_three like", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeNotLike(String value) {
            addCriterion("pay_price_three not like", value, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeIn(List<String> values) {
            addCriterion("pay_price_three in", values, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeNotIn(List<String> values) {
            addCriterion("pay_price_three not in", values, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeBetween(String value1, String value2) {
            addCriterion("pay_price_three between", value1, value2, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andPayPriceThreeNotBetween(String value1, String value2) {
            addCriterion("pay_price_three not between", value1, value2, "payPriceThree");
            return (Criteria) this;
        }

        public Criteria andActualFfreightIsNull() {
            addCriterion("actual_ffreight is null");
            return (Criteria) this;
        }

        public Criteria andActualFfreightIsNotNull() {
            addCriterion("actual_ffreight is not null");
            return (Criteria) this;
        }

        public Criteria andActualFfreightEqualTo(String value) {
            addCriterion("actual_ffreight =", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightNotEqualTo(String value) {
            addCriterion("actual_ffreight <>", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightGreaterThan(String value) {
            addCriterion("actual_ffreight >", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightGreaterThanOrEqualTo(String value) {
            addCriterion("actual_ffreight >=", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightLessThan(String value) {
            addCriterion("actual_ffreight <", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightLessThanOrEqualTo(String value) {
            addCriterion("actual_ffreight <=", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightLike(String value) {
            addCriterion("actual_ffreight like", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightNotLike(String value) {
            addCriterion("actual_ffreight not like", value, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightIn(List<String> values) {
            addCriterion("actual_ffreight in", values, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightNotIn(List<String> values) {
            addCriterion("actual_ffreight not in", values, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightBetween(String value1, String value2) {
            addCriterion("actual_ffreight between", value1, value2, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andActualFfreightNotBetween(String value1, String value2) {
            addCriterion("actual_ffreight not between", value1, value2, "actualFfreight");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceIsNull() {
            addCriterion("remaining_price is null");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceIsNotNull() {
            addCriterion("remaining_price is not null");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceEqualTo(Double value) {
            addCriterion("remaining_price =", value, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceNotEqualTo(Double value) {
            addCriterion("remaining_price <>", value, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceGreaterThan(Double value) {
            addCriterion("remaining_price >", value, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceGreaterThanOrEqualTo(Double value) {
            addCriterion("remaining_price >=", value, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceLessThan(Double value) {
            addCriterion("remaining_price <", value, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceLessThanOrEqualTo(Double value) {
            addCriterion("remaining_price <=", value, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceIn(List<Double> values) {
            addCriterion("remaining_price in", values, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceNotIn(List<Double> values) {
            addCriterion("remaining_price not in", values, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceBetween(Double value1, Double value2) {
            addCriterion("remaining_price between", value1, value2, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andRemainingPriceNotBetween(Double value1, Double value2) {
            addCriterion("remaining_price not between", value1, value2, "remainingPrice");
            return (Criteria) this;
        }

        public Criteria andActualVolumeIsNull() {
            addCriterion("actual_volume is null");
            return (Criteria) this;
        }

        public Criteria andActualVolumeIsNotNull() {
            addCriterion("actual_volume is not null");
            return (Criteria) this;
        }

        public Criteria andActualVolumeEqualTo(String value) {
            addCriterion("actual_volume =", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeNotEqualTo(String value) {
            addCriterion("actual_volume <>", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeGreaterThan(String value) {
            addCriterion("actual_volume >", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeGreaterThanOrEqualTo(String value) {
            addCriterion("actual_volume >=", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeLessThan(String value) {
            addCriterion("actual_volume <", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeLessThanOrEqualTo(String value) {
            addCriterion("actual_volume <=", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeLike(String value) {
            addCriterion("actual_volume like", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeNotLike(String value) {
            addCriterion("actual_volume not like", value, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeIn(List<String> values) {
            addCriterion("actual_volume in", values, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeNotIn(List<String> values) {
            addCriterion("actual_volume not in", values, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeBetween(String value1, String value2) {
            addCriterion("actual_volume between", value1, value2, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualVolumeNotBetween(String value1, String value2) {
            addCriterion("actual_volume not between", value1, value2, "actualVolume");
            return (Criteria) this;
        }

        public Criteria andActualWeightIsNull() {
            addCriterion("actual_weight is null");
            return (Criteria) this;
        }

        public Criteria andActualWeightIsNotNull() {
            addCriterion("actual_weight is not null");
            return (Criteria) this;
        }

        public Criteria andActualWeightEqualTo(String value) {
            addCriterion("actual_weight =", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightNotEqualTo(String value) {
            addCriterion("actual_weight <>", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightGreaterThan(String value) {
            addCriterion("actual_weight >", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightGreaterThanOrEqualTo(String value) {
            addCriterion("actual_weight >=", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightLessThan(String value) {
            addCriterion("actual_weight <", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightLessThanOrEqualTo(String value) {
            addCriterion("actual_weight <=", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightLike(String value) {
            addCriterion("actual_weight like", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightNotLike(String value) {
            addCriterion("actual_weight not like", value, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightIn(List<String> values) {
            addCriterion("actual_weight in", values, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightNotIn(List<String> values) {
            addCriterion("actual_weight not in", values, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightBetween(String value1, String value2) {
            addCriterion("actual_weight between", value1, value2, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andActualWeightNotBetween(String value1, String value2) {
            addCriterion("actual_weight not between", value1, value2, "actualWeight");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherIsNull() {
            addCriterion("custom_discuss_other is null");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherIsNotNull() {
            addCriterion("custom_discuss_other is not null");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherEqualTo(String value) {
            addCriterion("custom_discuss_other =", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherNotEqualTo(String value) {
            addCriterion("custom_discuss_other <>", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherGreaterThan(String value) {
            addCriterion("custom_discuss_other >", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherGreaterThanOrEqualTo(String value) {
            addCriterion("custom_discuss_other >=", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherLessThan(String value) {
            addCriterion("custom_discuss_other <", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherLessThanOrEqualTo(String value) {
            addCriterion("custom_discuss_other <=", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherLike(String value) {
            addCriterion("custom_discuss_other like", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherNotLike(String value) {
            addCriterion("custom_discuss_other not like", value, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherIn(List<String> values) {
            addCriterion("custom_discuss_other in", values, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherNotIn(List<String> values) {
            addCriterion("custom_discuss_other not in", values, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherBetween(String value1, String value2) {
            addCriterion("custom_discuss_other between", value1, value2, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussOtherNotBetween(String value1, String value2) {
            addCriterion("custom_discuss_other not between", value1, value2, "customDiscussOther");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightIsNull() {
            addCriterion("custom_discuss_fright is null");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightIsNotNull() {
            addCriterion("custom_discuss_fright is not null");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightEqualTo(String value) {
            addCriterion("custom_discuss_fright =", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightNotEqualTo(String value) {
            addCriterion("custom_discuss_fright <>", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightGreaterThan(String value) {
            addCriterion("custom_discuss_fright >", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightGreaterThanOrEqualTo(String value) {
            addCriterion("custom_discuss_fright >=", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightLessThan(String value) {
            addCriterion("custom_discuss_fright <", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightLessThanOrEqualTo(String value) {
            addCriterion("custom_discuss_fright <=", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightLike(String value) {
            addCriterion("custom_discuss_fright like", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightNotLike(String value) {
            addCriterion("custom_discuss_fright not like", value, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightIn(List<String> values) {
            addCriterion("custom_discuss_fright in", values, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightNotIn(List<String> values) {
            addCriterion("custom_discuss_fright not in", values, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightBetween(String value1, String value2) {
            addCriterion("custom_discuss_fright between", value1, value2, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andCustomDiscussFrightNotBetween(String value1, String value2) {
            addCriterion("custom_discuss_fright not between", value1, value2, "customDiscussFright");
            return (Criteria) this;
        }

        public Criteria andTransportTimeIsNull() {
            addCriterion("transport_time is null");
            return (Criteria) this;
        }

        public Criteria andTransportTimeIsNotNull() {
            addCriterion("transport_time is not null");
            return (Criteria) this;
        }

        public Criteria andTransportTimeEqualTo(Date value) {
            addCriterion("transport_time =", value, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeNotEqualTo(Date value) {
            addCriterion("transport_time <>", value, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeGreaterThan(Date value) {
            addCriterion("transport_time >", value, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("transport_time >=", value, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeLessThan(Date value) {
            addCriterion("transport_time <", value, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeLessThanOrEqualTo(Date value) {
            addCriterion("transport_time <=", value, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeIn(List<Date> values) {
            addCriterion("transport_time in", values, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeNotIn(List<Date> values) {
            addCriterion("transport_time not in", values, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeBetween(Date value1, Date value2) {
            addCriterion("transport_time between", value1, value2, "transportTime");
            return (Criteria) this;
        }

        public Criteria andTransportTimeNotBetween(Date value1, Date value2) {
            addCriterion("transport_time not between", value1, value2, "transportTime");
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

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andCancelObjIsNull() {
            addCriterion("cancel_obj is null");
            return (Criteria) this;
        }

        public Criteria andCancelObjIsNotNull() {
            addCriterion("cancel_obj is not null");
            return (Criteria) this;
        }

        public Criteria andCancelObjEqualTo(Integer value) {
            addCriterion("cancel_obj =", value, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjNotEqualTo(Integer value) {
            addCriterion("cancel_obj <>", value, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjGreaterThan(Integer value) {
            addCriterion("cancel_obj >", value, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjGreaterThanOrEqualTo(Integer value) {
            addCriterion("cancel_obj >=", value, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjLessThan(Integer value) {
            addCriterion("cancel_obj <", value, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjLessThanOrEqualTo(Integer value) {
            addCriterion("cancel_obj <=", value, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjIn(List<Integer> values) {
            addCriterion("cancel_obj in", values, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjNotIn(List<Integer> values) {
            addCriterion("cancel_obj not in", values, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjBetween(Integer value1, Integer value2) {
            addCriterion("cancel_obj between", value1, value2, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andCancelObjNotBetween(Integer value1, Integer value2) {
            addCriterion("cancel_obj not between", value1, value2, "cancelObj");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeIsNull() {
            addCriterion("expect_arrive_time is null");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeIsNotNull() {
            addCriterion("expect_arrive_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeEqualTo(Date value) {
            addCriterion("expect_arrive_time =", value, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeNotEqualTo(Date value) {
            addCriterion("expect_arrive_time <>", value, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeGreaterThan(Date value) {
            addCriterion("expect_arrive_time >", value, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("expect_arrive_time >=", value, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeLessThan(Date value) {
            addCriterion("expect_arrive_time <", value, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeLessThanOrEqualTo(Date value) {
            addCriterion("expect_arrive_time <=", value, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeIn(List<Date> values) {
            addCriterion("expect_arrive_time in", values, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeNotIn(List<Date> values) {
            addCriterion("expect_arrive_time not in", values, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeBetween(Date value1, Date value2) {
            addCriterion("expect_arrive_time between", value1, value2, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andExpectArriveTimeNotBetween(Date value1, Date value2) {
            addCriterion("expect_arrive_time not between", value1, value2, "expectArriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeIsNull() {
            addCriterion("arrive_time is null");
            return (Criteria) this;
        }

        public Criteria andArriveTimeIsNotNull() {
            addCriterion("arrive_time is not null");
            return (Criteria) this;
        }

        public Criteria andArriveTimeEqualTo(Date value) {
            addCriterion("arrive_time =", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeNotEqualTo(Date value) {
            addCriterion("arrive_time <>", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeGreaterThan(Date value) {
            addCriterion("arrive_time >", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("arrive_time >=", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeLessThan(Date value) {
            addCriterion("arrive_time <", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeLessThanOrEqualTo(Date value) {
            addCriterion("arrive_time <=", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeIn(List<Date> values) {
            addCriterion("arrive_time in", values, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeNotIn(List<Date> values) {
            addCriterion("arrive_time not in", values, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeBetween(Date value1, Date value2) {
            addCriterion("arrive_time between", value1, value2, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeNotBetween(Date value1, Date value2) {
            addCriterion("arrive_time not between", value1, value2, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andClientUpdateIsNull() {
            addCriterion("client_update is null");
            return (Criteria) this;
        }

        public Criteria andClientUpdateIsNotNull() {
            addCriterion("client_update is not null");
            return (Criteria) this;
        }

        public Criteria andClientUpdateEqualTo(Integer value) {
            addCriterion("client_update =", value, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateNotEqualTo(Integer value) {
            addCriterion("client_update <>", value, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateGreaterThan(Integer value) {
            addCriterion("client_update >", value, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateGreaterThanOrEqualTo(Integer value) {
            addCriterion("client_update >=", value, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateLessThan(Integer value) {
            addCriterion("client_update <", value, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateLessThanOrEqualTo(Integer value) {
            addCriterion("client_update <=", value, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateIn(List<Integer> values) {
            addCriterion("client_update in", values, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateNotIn(List<Integer> values) {
            addCriterion("client_update not in", values, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateBetween(Integer value1, Integer value2) {
            addCriterion("client_update between", value1, value2, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andClientUpdateNotBetween(Integer value1, Integer value2) {
            addCriterion("client_update not between", value1, value2, "clientUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateIsNull() {
            addCriterion("server_update is null");
            return (Criteria) this;
        }

        public Criteria andServerUpdateIsNotNull() {
            addCriterion("server_update is not null");
            return (Criteria) this;
        }

        public Criteria andServerUpdateEqualTo(Integer value) {
            addCriterion("server_update =", value, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateNotEqualTo(Integer value) {
            addCriterion("server_update <>", value, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateGreaterThan(Integer value) {
            addCriterion("server_update >", value, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateGreaterThanOrEqualTo(Integer value) {
            addCriterion("server_update >=", value, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateLessThan(Integer value) {
            addCriterion("server_update <", value, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateLessThanOrEqualTo(Integer value) {
            addCriterion("server_update <=", value, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateIn(List<Integer> values) {
            addCriterion("server_update in", values, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateNotIn(List<Integer> values) {
            addCriterion("server_update not in", values, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateBetween(Integer value1, Integer value2) {
            addCriterion("server_update between", value1, value2, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andServerUpdateNotBetween(Integer value1, Integer value2) {
            addCriterion("server_update not between", value1, value2, "serverUpdate");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andOrderAcIsNull() {
            addCriterion("order_ac is null");
            return (Criteria) this;
        }

        public Criteria andOrderAcIsNotNull() {
            addCriterion("order_ac is not null");
            return (Criteria) this;
        }

        public Criteria andOrderAcEqualTo(Double value) {
            addCriterion("order_ac =", value, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcNotEqualTo(Double value) {
            addCriterion("order_ac <>", value, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcGreaterThan(Double value) {
            addCriterion("order_ac >", value, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcGreaterThanOrEqualTo(Double value) {
            addCriterion("order_ac >=", value, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcLessThan(Double value) {
            addCriterion("order_ac <", value, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcLessThanOrEqualTo(Double value) {
            addCriterion("order_ac <=", value, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcIn(List<Double> values) {
            addCriterion("order_ac in", values, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcNotIn(List<Double> values) {
            addCriterion("order_ac not in", values, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcBetween(Double value1, Double value2) {
            addCriterion("order_ac between", value1, value2, "orderAc");
            return (Criteria) this;
        }

        public Criteria andOrderAcNotBetween(Double value1, Double value2) {
            addCriterion("order_ac not between", value1, value2, "orderAc");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberIsNull() {
            addCriterion("purchase_number is null");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberIsNotNull() {
            addCriterion("purchase_number is not null");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberEqualTo(Integer value) {
            addCriterion("purchase_number =", value, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberNotEqualTo(Integer value) {
            addCriterion("purchase_number <>", value, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberGreaterThan(Integer value) {
            addCriterion("purchase_number >", value, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("purchase_number >=", value, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberLessThan(Integer value) {
            addCriterion("purchase_number <", value, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberLessThanOrEqualTo(Integer value) {
            addCriterion("purchase_number <=", value, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberIn(List<Integer> values) {
            addCriterion("purchase_number in", values, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberNotIn(List<Integer> values) {
            addCriterion("purchase_number not in", values, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberBetween(Integer value1, Integer value2) {
            addCriterion("purchase_number between", value1, value2, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andPurchaseNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("purchase_number not between", value1, value2, "purchaseNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberIsNull() {
            addCriterion("details_number is null");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberIsNotNull() {
            addCriterion("details_number is not null");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberEqualTo(Integer value) {
            addCriterion("details_number =", value, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberNotEqualTo(Integer value) {
            addCriterion("details_number <>", value, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberGreaterThan(Integer value) {
            addCriterion("details_number >", value, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("details_number >=", value, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberLessThan(Integer value) {
            addCriterion("details_number <", value, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberLessThanOrEqualTo(Integer value) {
            addCriterion("details_number <=", value, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberIn(List<Integer> values) {
            addCriterion("details_number in", values, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberNotIn(List<Integer> values) {
            addCriterion("details_number not in", values, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberBetween(Integer value1, Integer value2) {
            addCriterion("details_number between", value1, value2, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andDetailsNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("details_number not between", value1, value2, "detailsNumber");
            return (Criteria) this;
        }

        public Criteria andIpnaddressIsNull() {
            addCriterion("ipnaddress is null");
            return (Criteria) this;
        }

        public Criteria andIpnaddressIsNotNull() {
            addCriterion("ipnaddress is not null");
            return (Criteria) this;
        }

        public Criteria andIpnaddressEqualTo(String value) {
            addCriterion("ipnaddress =", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressNotEqualTo(String value) {
            addCriterion("ipnaddress <>", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressGreaterThan(String value) {
            addCriterion("ipnaddress >", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressGreaterThanOrEqualTo(String value) {
            addCriterion("ipnaddress >=", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressLessThan(String value) {
            addCriterion("ipnaddress <", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressLessThanOrEqualTo(String value) {
            addCriterion("ipnaddress <=", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressLike(String value) {
            addCriterion("ipnaddress like", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressNotLike(String value) {
            addCriterion("ipnaddress not like", value, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressIn(List<String> values) {
            addCriterion("ipnaddress in", values, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressNotIn(List<String> values) {
            addCriterion("ipnaddress not in", values, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressBetween(String value1, String value2) {
            addCriterion("ipnaddress between", value1, value2, "ipnaddress");
            return (Criteria) this;
        }

        public Criteria andIpnaddressNotBetween(String value1, String value2) {
            addCriterion("ipnaddress not between", value1, value2, "ipnaddress");
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

        public Criteria andDiscountAmountIsNull() {
            addCriterion("discount_amount is null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIsNotNull() {
            addCriterion("discount_amount is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountEqualTo(Double value) {
            addCriterion("discount_amount =", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotEqualTo(Double value) {
            addCriterion("discount_amount <>", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThan(Double value) {
            addCriterion("discount_amount >", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThanOrEqualTo(Double value) {
            addCriterion("discount_amount >=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThan(Double value) {
            addCriterion("discount_amount <", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThanOrEqualTo(Double value) {
            addCriterion("discount_amount <=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIn(List<Double> values) {
            addCriterion("discount_amount in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotIn(List<Double> values) {
            addCriterion("discount_amount not in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountBetween(Double value1, Double value2) {
            addCriterion("discount_amount between", value1, value2, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotBetween(Double value1, Double value2) {
            addCriterion("discount_amount not between", value1, value2, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysIsNull() {
            addCriterion("purchase_days is null");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysIsNotNull() {
            addCriterion("purchase_days is not null");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysEqualTo(Integer value) {
            addCriterion("purchase_days =", value, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysNotEqualTo(Integer value) {
            addCriterion("purchase_days <>", value, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysGreaterThan(Integer value) {
            addCriterion("purchase_days >", value, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysGreaterThanOrEqualTo(Integer value) {
            addCriterion("purchase_days >=", value, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysLessThan(Integer value) {
            addCriterion("purchase_days <", value, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysLessThanOrEqualTo(Integer value) {
            addCriterion("purchase_days <=", value, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysIn(List<Integer> values) {
            addCriterion("purchase_days in", values, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysNotIn(List<Integer> values) {
            addCriterion("purchase_days not in", values, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysBetween(Integer value1, Integer value2) {
            addCriterion("purchase_days between", value1, value2, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andPurchaseDaysNotBetween(Integer value1, Integer value2) {
            addCriterion("purchase_days not between", value1, value2, "purchaseDays");
            return (Criteria) this;
        }

        public Criteria andActualLwhIsNull() {
            addCriterion("actual_lwh is null");
            return (Criteria) this;
        }

        public Criteria andActualLwhIsNotNull() {
            addCriterion("actual_lwh is not null");
            return (Criteria) this;
        }

        public Criteria andActualLwhEqualTo(String value) {
            addCriterion("actual_lwh =", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhNotEqualTo(String value) {
            addCriterion("actual_lwh <>", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhGreaterThan(String value) {
            addCriterion("actual_lwh >", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhGreaterThanOrEqualTo(String value) {
            addCriterion("actual_lwh >=", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhLessThan(String value) {
            addCriterion("actual_lwh <", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhLessThanOrEqualTo(String value) {
            addCriterion("actual_lwh <=", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhLike(String value) {
            addCriterion("actual_lwh like", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhNotLike(String value) {
            addCriterion("actual_lwh not like", value, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhIn(List<String> values) {
            addCriterion("actual_lwh in", values, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhNotIn(List<String> values) {
            addCriterion("actual_lwh not in", values, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhBetween(String value1, String value2) {
            addCriterion("actual_lwh between", value1, value2, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualLwhNotBetween(String value1, String value2) {
            addCriterion("actual_lwh not between", value1, value2, "actualLwh");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateIsNull() {
            addCriterion("actual_weight_estimate is null");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateIsNotNull() {
            addCriterion("actual_weight_estimate is not null");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateEqualTo(Double value) {
            addCriterion("actual_weight_estimate =", value, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateNotEqualTo(Double value) {
            addCriterion("actual_weight_estimate <>", value, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateGreaterThan(Double value) {
            addCriterion("actual_weight_estimate >", value, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateGreaterThanOrEqualTo(Double value) {
            addCriterion("actual_weight_estimate >=", value, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateLessThan(Double value) {
            addCriterion("actual_weight_estimate <", value, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateLessThanOrEqualTo(Double value) {
            addCriterion("actual_weight_estimate <=", value, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateIn(List<Double> values) {
            addCriterion("actual_weight_estimate in", values, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateNotIn(List<Double> values) {
            addCriterion("actual_weight_estimate not in", values, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateBetween(Double value1, Double value2) {
            addCriterion("actual_weight_estimate between", value1, value2, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualWeightEstimateNotBetween(Double value1, Double value2) {
            addCriterion("actual_weight_estimate not between", value1, value2, "actualWeightEstimate");
            return (Criteria) this;
        }

        public Criteria andActualFreightCIsNull() {
            addCriterion("actual_freight_c is null");
            return (Criteria) this;
        }

        public Criteria andActualFreightCIsNotNull() {
            addCriterion("actual_freight_c is not null");
            return (Criteria) this;
        }

        public Criteria andActualFreightCEqualTo(Double value) {
            addCriterion("actual_freight_c =", value, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCNotEqualTo(Double value) {
            addCriterion("actual_freight_c <>", value, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCGreaterThan(Double value) {
            addCriterion("actual_freight_c >", value, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCGreaterThanOrEqualTo(Double value) {
            addCriterion("actual_freight_c >=", value, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCLessThan(Double value) {
            addCriterion("actual_freight_c <", value, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCLessThanOrEqualTo(Double value) {
            addCriterion("actual_freight_c <=", value, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCIn(List<Double> values) {
            addCriterion("actual_freight_c in", values, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCNotIn(List<Double> values) {
            addCriterion("actual_freight_c not in", values, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCBetween(Double value1, Double value2) {
            addCriterion("actual_freight_c between", value1, value2, "actualFreightC");
            return (Criteria) this;
        }

        public Criteria andActualFreightCNotBetween(Double value1, Double value2) {
            addCriterion("actual_freight_c not between", value1, value2, "actualFreightC");
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

        public Criteria andOrderShowIsNull() {
            addCriterion("order_show is null");
            return (Criteria) this;
        }

        public Criteria andOrderShowIsNotNull() {
            addCriterion("order_show is not null");
            return (Criteria) this;
        }

        public Criteria andOrderShowEqualTo(Integer value) {
            addCriterion("order_show =", value, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowNotEqualTo(Integer value) {
            addCriterion("order_show <>", value, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowGreaterThan(Integer value) {
            addCriterion("order_show >", value, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_show >=", value, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowLessThan(Integer value) {
            addCriterion("order_show <", value, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowLessThanOrEqualTo(Integer value) {
            addCriterion("order_show <=", value, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowIn(List<Integer> values) {
            addCriterion("order_show in", values, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowNotIn(List<Integer> values) {
            addCriterion("order_show not in", values, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowBetween(Integer value1, Integer value2) {
            addCriterion("order_show between", value1, value2, "orderShow");
            return (Criteria) this;
        }

        public Criteria andOrderShowNotBetween(Integer value1, Integer value2) {
            addCriterion("order_show not between", value1, value2, "orderShow");
            return (Criteria) this;
        }

        public Criteria andPackagNumberIsNull() {
            addCriterion("packag_number is null");
            return (Criteria) this;
        }

        public Criteria andPackagNumberIsNotNull() {
            addCriterion("packag_number is not null");
            return (Criteria) this;
        }

        public Criteria andPackagNumberEqualTo(Integer value) {
            addCriterion("packag_number =", value, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberNotEqualTo(Integer value) {
            addCriterion("packag_number <>", value, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberGreaterThan(Integer value) {
            addCriterion("packag_number >", value, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("packag_number >=", value, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberLessThan(Integer value) {
            addCriterion("packag_number <", value, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberLessThanOrEqualTo(Integer value) {
            addCriterion("packag_number <=", value, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberIn(List<Integer> values) {
            addCriterion("packag_number in", values, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberNotIn(List<Integer> values) {
            addCriterion("packag_number not in", values, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberBetween(Integer value1, Integer value2) {
            addCriterion("packag_number between", value1, value2, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andPackagNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("packag_number not between", value1, value2, "packagNumber");
            return (Criteria) this;
        }

        public Criteria andOrderremarkIsNull() {
            addCriterion("orderRemark is null");
            return (Criteria) this;
        }

        public Criteria andOrderremarkIsNotNull() {
            addCriterion("orderRemark is not null");
            return (Criteria) this;
        }

        public Criteria andOrderremarkEqualTo(String value) {
            addCriterion("orderRemark =", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkNotEqualTo(String value) {
            addCriterion("orderRemark <>", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkGreaterThan(String value) {
            addCriterion("orderRemark >", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkGreaterThanOrEqualTo(String value) {
            addCriterion("orderRemark >=", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkLessThan(String value) {
            addCriterion("orderRemark <", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkLessThanOrEqualTo(String value) {
            addCriterion("orderRemark <=", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkLike(String value) {
            addCriterion("orderRemark like", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkNotLike(String value) {
            addCriterion("orderRemark not like", value, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkIn(List<String> values) {
            addCriterion("orderRemark in", values, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkNotIn(List<String> values) {
            addCriterion("orderRemark not in", values, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkBetween(String value1, String value2) {
            addCriterion("orderRemark between", value1, value2, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderremarkNotBetween(String value1, String value2) {
            addCriterion("orderRemark not between", value1, value2, "orderremark");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeIsNull() {
            addCriterion("orderpaytime is null");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeIsNotNull() {
            addCriterion("orderpaytime is not null");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeEqualTo(Date value) {
            addCriterion("orderpaytime =", value, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeNotEqualTo(Date value) {
            addCriterion("orderpaytime <>", value, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeGreaterThan(Date value) {
            addCriterion("orderpaytime >", value, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeGreaterThanOrEqualTo(Date value) {
            addCriterion("orderpaytime >=", value, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeLessThan(Date value) {
            addCriterion("orderpaytime <", value, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeLessThanOrEqualTo(Date value) {
            addCriterion("orderpaytime <=", value, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeIn(List<Date> values) {
            addCriterion("orderpaytime in", values, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeNotIn(List<Date> values) {
            addCriterion("orderpaytime not in", values, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeBetween(Date value1, Date value2) {
            addCriterion("orderpaytime between", value1, value2, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andOrderpaytimeNotBetween(Date value1, Date value2) {
            addCriterion("orderpaytime not between", value1, value2, "orderpaytime");
            return (Criteria) this;
        }

        public Criteria andCashbackIsNull() {
            addCriterion("cashback is null");
            return (Criteria) this;
        }

        public Criteria andCashbackIsNotNull() {
            addCriterion("cashback is not null");
            return (Criteria) this;
        }

        public Criteria andCashbackEqualTo(Double value) {
            addCriterion("cashback =", value, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackNotEqualTo(Double value) {
            addCriterion("cashback <>", value, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackGreaterThan(Double value) {
            addCriterion("cashback >", value, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackGreaterThanOrEqualTo(Double value) {
            addCriterion("cashback >=", value, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackLessThan(Double value) {
            addCriterion("cashback <", value, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackLessThanOrEqualTo(Double value) {
            addCriterion("cashback <=", value, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackIn(List<Double> values) {
            addCriterion("cashback in", values, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackNotIn(List<Double> values) {
            addCriterion("cashback not in", values, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackBetween(Double value1, Double value2) {
            addCriterion("cashback between", value1, value2, "cashback");
            return (Criteria) this;
        }

        public Criteria andCashbackNotBetween(Double value1, Double value2) {
            addCriterion("cashback not between", value1, value2, "cashback");
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