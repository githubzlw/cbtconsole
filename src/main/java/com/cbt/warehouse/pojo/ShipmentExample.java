package com.cbt.warehouse.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShipmentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShipmentExample() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSenttimeIsNull() {
            addCriterion("sentTime is null");
            return (Criteria) this;
        }

        public Criteria andSenttimeIsNotNull() {
            addCriterion("sentTime is not null");
            return (Criteria) this;
        }

        public Criteria andSenttimeEqualTo(Date value) {
            addCriterion("sentTime =", value, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeNotEqualTo(Date value) {
            addCriterion("sentTime <>", value, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeGreaterThan(Date value) {
            addCriterion("sentTime >", value, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeGreaterThanOrEqualTo(Date value) {
            addCriterion("sentTime >=", value, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeLessThan(Date value) {
            addCriterion("sentTime <", value, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeLessThanOrEqualTo(Date value) {
            addCriterion("sentTime <=", value, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeIn(List<Date> values) {
            addCriterion("sentTime in", values, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeNotIn(List<Date> values) {
            addCriterion("sentTime not in", values, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeBetween(Date value1, Date value2) {
            addCriterion("sentTime between", value1, value2, "senttime");
            return (Criteria) this;
        }

        public Criteria andSenttimeNotBetween(Date value1, Date value2) {
            addCriterion("sentTime not between", value1, value2, "senttime");
            return (Criteria) this;
        }

        public Criteria andOrdernoIsNull() {
            addCriterion("orderNo is null");
            return (Criteria) this;
        }

        public Criteria andOrdernoIsNotNull() {
            addCriterion("orderNo is not null");
            return (Criteria) this;
        }

        public Criteria andOrdernoEqualTo(String value) {
            addCriterion("orderNo =", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotEqualTo(String value) {
            addCriterion("orderNo <>", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoGreaterThan(String value) {
            addCriterion("orderNo >", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoGreaterThanOrEqualTo(String value) {
            addCriterion("orderNo >=", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoLessThan(String value) {
            addCriterion("orderNo <", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoLessThanOrEqualTo(String value) {
            addCriterion("orderNo <=", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoLike(String value) {
            addCriterion("orderNo like", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotLike(String value) {
            addCriterion("orderNo not like", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoIn(List<String> values) {
            addCriterion("orderNo in", values, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotIn(List<String> values) {
            addCriterion("orderNo not in", values, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoBetween(String value1, String value2) {
            addCriterion("orderNo between", value1, value2, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotBetween(String value1, String value2) {
            addCriterion("orderNo not between", value1, value2, "orderno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoIsNull() {
            addCriterion("switchNo is null");
            return (Criteria) this;
        }

        public Criteria andSwitchnoIsNotNull() {
            addCriterion("switchNo is not null");
            return (Criteria) this;
        }

        public Criteria andSwitchnoEqualTo(String value) {
            addCriterion("switchNo =", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoNotEqualTo(String value) {
            addCriterion("switchNo <>", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoGreaterThan(String value) {
            addCriterion("switchNo >", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoGreaterThanOrEqualTo(String value) {
            addCriterion("switchNo >=", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoLessThan(String value) {
            addCriterion("switchNo <", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoLessThanOrEqualTo(String value) {
            addCriterion("switchNo <=", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoLike(String value) {
            addCriterion("switchNo like", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoNotLike(String value) {
            addCriterion("switchNo not like", value, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoIn(List<String> values) {
            addCriterion("switchNo in", values, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoNotIn(List<String> values) {
            addCriterion("switchNo not in", values, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoBetween(String value1, String value2) {
            addCriterion("switchNo between", value1, value2, "switchno");
            return (Criteria) this;
        }

        public Criteria andSwitchnoNotBetween(String value1, String value2) {
            addCriterion("switchNo not between", value1, value2, "switchno");
            return (Criteria) this;
        }

        public Criteria andAddresseeIsNull() {
            addCriterion("addressee is null");
            return (Criteria) this;
        }

        public Criteria andAddresseeIsNotNull() {
            addCriterion("addressee is not null");
            return (Criteria) this;
        }

        public Criteria andAddresseeEqualTo(String value) {
            addCriterion("addressee =", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeNotEqualTo(String value) {
            addCriterion("addressee <>", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeGreaterThan(String value) {
            addCriterion("addressee >", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeGreaterThanOrEqualTo(String value) {
            addCriterion("addressee >=", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeLessThan(String value) {
            addCriterion("addressee <", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeLessThanOrEqualTo(String value) {
            addCriterion("addressee <=", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeLike(String value) {
            addCriterion("addressee like", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeNotLike(String value) {
            addCriterion("addressee not like", value, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeIn(List<String> values) {
            addCriterion("addressee in", values, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeNotIn(List<String> values) {
            addCriterion("addressee not in", values, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeBetween(String value1, String value2) {
            addCriterion("addressee between", value1, value2, "addressee");
            return (Criteria) this;
        }

        public Criteria andAddresseeNotBetween(String value1, String value2) {
            addCriterion("addressee not between", value1, value2, "addressee");
            return (Criteria) this;
        }

        public Criteria andCountryIsNull() {
            addCriterion("country is null");
            return (Criteria) this;
        }

        public Criteria andCountryIsNotNull() {
            addCriterion("country is not null");
            return (Criteria) this;
        }

        public Criteria andCountryEqualTo(String value) {
            addCriterion("country =", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotEqualTo(String value) {
            addCriterion("country <>", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryGreaterThan(String value) {
            addCriterion("country >", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryGreaterThanOrEqualTo(String value) {
            addCriterion("country >=", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLessThan(String value) {
            addCriterion("country <", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLessThanOrEqualTo(String value) {
            addCriterion("country <=", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLike(String value) {
            addCriterion("country like", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotLike(String value) {
            addCriterion("country not like", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryIn(List<String> values) {
            addCriterion("country in", values, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotIn(List<String> values) {
            addCriterion("country not in", values, "country");
            return (Criteria) this;
        }

        public Criteria andCountryBetween(String value1, String value2) {
            addCriterion("country between", value1, value2, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotBetween(String value1, String value2) {
            addCriterion("country not between", value1, value2, "country");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyIsNull() {
            addCriterion("transportCompany is null");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyIsNotNull() {
            addCriterion("transportCompany is not null");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyEqualTo(String value) {
            addCriterion("transportCompany =", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyNotEqualTo(String value) {
            addCriterion("transportCompany <>", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyGreaterThan(String value) {
            addCriterion("transportCompany >", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyGreaterThanOrEqualTo(String value) {
            addCriterion("transportCompany >=", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyLessThan(String value) {
            addCriterion("transportCompany <", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyLessThanOrEqualTo(String value) {
            addCriterion("transportCompany <=", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyLike(String value) {
            addCriterion("transportCompany like", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyNotLike(String value) {
            addCriterion("transportCompany not like", value, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyIn(List<String> values) {
            addCriterion("transportCompany in", values, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyNotIn(List<String> values) {
            addCriterion("transportCompany not in", values, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyBetween(String value1, String value2) {
            addCriterion("transportCompany between", value1, value2, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransportcompanyNotBetween(String value1, String value2) {
            addCriterion("transportCompany not between", value1, value2, "transportcompany");
            return (Criteria) this;
        }

        public Criteria andTransporttypeIsNull() {
            addCriterion("transportType is null");
            return (Criteria) this;
        }

        public Criteria andTransporttypeIsNotNull() {
            addCriterion("transportType is not null");
            return (Criteria) this;
        }

        public Criteria andTransporttypeEqualTo(String value) {
            addCriterion("transportType =", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeNotEqualTo(String value) {
            addCriterion("transportType <>", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeGreaterThan(String value) {
            addCriterion("transportType >", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeGreaterThanOrEqualTo(String value) {
            addCriterion("transportType >=", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeLessThan(String value) {
            addCriterion("transportType <", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeLessThanOrEqualTo(String value) {
            addCriterion("transportType <=", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeLike(String value) {
            addCriterion("transportType like", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeNotLike(String value) {
            addCriterion("transportType not like", value, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeIn(List<String> values) {
            addCriterion("transportType in", values, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeNotIn(List<String> values) {
            addCriterion("transportType not in", values, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeBetween(String value1, String value2) {
            addCriterion("transportType between", value1, value2, "transporttype");
            return (Criteria) this;
        }

        public Criteria andTransporttypeNotBetween(String value1, String value2) {
            addCriterion("transportType not between", value1, value2, "transporttype");
            return (Criteria) this;
        }

        public Criteria andNumbersIsNull() {
            addCriterion("numbers is null");
            return (Criteria) this;
        }

        public Criteria andNumbersIsNotNull() {
            addCriterion("numbers is not null");
            return (Criteria) this;
        }

        public Criteria andNumbersEqualTo(Integer value) {
            addCriterion("numbers =", value, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersNotEqualTo(Integer value) {
            addCriterion("numbers <>", value, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersGreaterThan(Integer value) {
            addCriterion("numbers >", value, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersGreaterThanOrEqualTo(Integer value) {
            addCriterion("numbers >=", value, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersLessThan(Integer value) {
            addCriterion("numbers <", value, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersLessThanOrEqualTo(Integer value) {
            addCriterion("numbers <=", value, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersIn(List<Integer> values) {
            addCriterion("numbers in", values, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersNotIn(List<Integer> values) {
            addCriterion("numbers not in", values, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersBetween(Integer value1, Integer value2) {
            addCriterion("numbers between", value1, value2, "numbers");
            return (Criteria) this;
        }

        public Criteria andNumbersNotBetween(Integer value1, Integer value2) {
            addCriterion("numbers not between", value1, value2, "numbers");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andRealweightIsNull() {
            addCriterion("realWeight is null");
            return (Criteria) this;
        }

        public Criteria andRealweightIsNotNull() {
            addCriterion("realWeight is not null");
            return (Criteria) this;
        }

        public Criteria andRealweightEqualTo(BigDecimal value) {
            addCriterion("realWeight =", value, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightNotEqualTo(BigDecimal value) {
            addCriterion("realWeight <>", value, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightGreaterThan(BigDecimal value) {
            addCriterion("realWeight >", value, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("realWeight >=", value, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightLessThan(BigDecimal value) {
            addCriterion("realWeight <", value, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightLessThanOrEqualTo(BigDecimal value) {
            addCriterion("realWeight <=", value, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightIn(List<BigDecimal> values) {
            addCriterion("realWeight in", values, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightNotIn(List<BigDecimal> values) {
            addCriterion("realWeight not in", values, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("realWeight between", value1, value2, "realweight");
            return (Criteria) this;
        }

        public Criteria andRealweightNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("realWeight not between", value1, value2, "realweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightIsNull() {
            addCriterion("bulkWeight is null");
            return (Criteria) this;
        }

        public Criteria andBulkweightIsNotNull() {
            addCriterion("bulkWeight is not null");
            return (Criteria) this;
        }

        public Criteria andBulkweightEqualTo(BigDecimal value) {
            addCriterion("bulkWeight =", value, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightNotEqualTo(BigDecimal value) {
            addCriterion("bulkWeight <>", value, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightGreaterThan(BigDecimal value) {
            addCriterion("bulkWeight >", value, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("bulkWeight >=", value, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightLessThan(BigDecimal value) {
            addCriterion("bulkWeight <", value, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightLessThanOrEqualTo(BigDecimal value) {
            addCriterion("bulkWeight <=", value, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightIn(List<BigDecimal> values) {
            addCriterion("bulkWeight in", values, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightNotIn(List<BigDecimal> values) {
            addCriterion("bulkWeight not in", values, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bulkWeight between", value1, value2, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andBulkweightNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bulkWeight not between", value1, value2, "bulkweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightIsNull() {
            addCriterion("settleWeight is null");
            return (Criteria) this;
        }

        public Criteria andSettleweightIsNotNull() {
            addCriterion("settleWeight is not null");
            return (Criteria) this;
        }

        public Criteria andSettleweightEqualTo(BigDecimal value) {
            addCriterion("settleWeight =", value, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightNotEqualTo(BigDecimal value) {
            addCriterion("settleWeight <>", value, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightGreaterThan(BigDecimal value) {
            addCriterion("settleWeight >", value, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("settleWeight >=", value, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightLessThan(BigDecimal value) {
            addCriterion("settleWeight <", value, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightLessThanOrEqualTo(BigDecimal value) {
            addCriterion("settleWeight <=", value, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightIn(List<BigDecimal> values) {
            addCriterion("settleWeight in", values, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightNotIn(List<BigDecimal> values) {
            addCriterion("settleWeight not in", values, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("settleWeight between", value1, value2, "settleweight");
            return (Criteria) this;
        }

        public Criteria andSettleweightNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("settleWeight not between", value1, value2, "settleweight");
            return (Criteria) this;
        }

        public Criteria andChargeIsNull() {
            addCriterion("charge is null");
            return (Criteria) this;
        }

        public Criteria andChargeIsNotNull() {
            addCriterion("charge is not null");
            return (Criteria) this;
        }

        public Criteria andChargeEqualTo(BigDecimal value) {
            addCriterion("charge =", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeNotEqualTo(BigDecimal value) {
            addCriterion("charge <>", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeGreaterThan(BigDecimal value) {
            addCriterion("charge >", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("charge >=", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeLessThan(BigDecimal value) {
            addCriterion("charge <", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("charge <=", value, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeIn(List<BigDecimal> values) {
            addCriterion("charge in", values, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeNotIn(List<BigDecimal> values) {
            addCriterion("charge not in", values, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("charge between", value1, value2, "charge");
            return (Criteria) this;
        }

        public Criteria andChargeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("charge not between", value1, value2, "charge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeIsNull() {
            addCriterion("fuelSurcharge is null");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeIsNotNull() {
            addCriterion("fuelSurcharge is not null");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeEqualTo(BigDecimal value) {
            addCriterion("fuelSurcharge =", value, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeNotEqualTo(BigDecimal value) {
            addCriterion("fuelSurcharge <>", value, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeGreaterThan(BigDecimal value) {
            addCriterion("fuelSurcharge >", value, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fuelSurcharge >=", value, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeLessThan(BigDecimal value) {
            addCriterion("fuelSurcharge <", value, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fuelSurcharge <=", value, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeIn(List<BigDecimal> values) {
            addCriterion("fuelSurcharge in", values, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeNotIn(List<BigDecimal> values) {
            addCriterion("fuelSurcharge not in", values, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fuelSurcharge between", value1, value2, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andFuelsurchargeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fuelSurcharge not between", value1, value2, "fuelsurcharge");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsIsNull() {
            addCriterion("securityCosts is null");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsIsNotNull() {
            addCriterion("securityCosts is not null");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsEqualTo(BigDecimal value) {
            addCriterion("securityCosts =", value, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsNotEqualTo(BigDecimal value) {
            addCriterion("securityCosts <>", value, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsGreaterThan(BigDecimal value) {
            addCriterion("securityCosts >", value, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("securityCosts >=", value, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsLessThan(BigDecimal value) {
            addCriterion("securityCosts <", value, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsLessThanOrEqualTo(BigDecimal value) {
            addCriterion("securityCosts <=", value, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsIn(List<BigDecimal> values) {
            addCriterion("securityCosts in", values, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsNotIn(List<BigDecimal> values) {
            addCriterion("securityCosts not in", values, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("securityCosts between", value1, value2, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andSecuritycostsNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("securityCosts not between", value1, value2, "securitycosts");
            return (Criteria) this;
        }

        public Criteria andTaxsIsNull() {
            addCriterion("taxs is null");
            return (Criteria) this;
        }

        public Criteria andTaxsIsNotNull() {
            addCriterion("taxs is not null");
            return (Criteria) this;
        }

        public Criteria andTaxsEqualTo(BigDecimal value) {
            addCriterion("taxs =", value, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsNotEqualTo(BigDecimal value) {
            addCriterion("taxs <>", value, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsGreaterThan(BigDecimal value) {
            addCriterion("taxs >", value, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("taxs >=", value, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsLessThan(BigDecimal value) {
            addCriterion("taxs <", value, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsLessThanOrEqualTo(BigDecimal value) {
            addCriterion("taxs <=", value, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsIn(List<BigDecimal> values) {
            addCriterion("taxs in", values, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsNotIn(List<BigDecimal> values) {
            addCriterion("taxs not in", values, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("taxs between", value1, value2, "taxs");
            return (Criteria) this;
        }

        public Criteria andTaxsNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("taxs not between", value1, value2, "taxs");
            return (Criteria) this;
        }

        public Criteria andTotalpriceIsNull() {
            addCriterion("totalPrice is null");
            return (Criteria) this;
        }

        public Criteria andTotalpriceIsNotNull() {
            addCriterion("totalPrice is not null");
            return (Criteria) this;
        }

        public Criteria andTotalpriceEqualTo(BigDecimal value) {
            addCriterion("totalPrice =", value, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceNotEqualTo(BigDecimal value) {
            addCriterion("totalPrice <>", value, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceGreaterThan(BigDecimal value) {
            addCriterion("totalPrice >", value, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("totalPrice >=", value, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceLessThan(BigDecimal value) {
            addCriterion("totalPrice <", value, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("totalPrice <=", value, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceIn(List<BigDecimal> values) {
            addCriterion("totalPrice in", values, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceNotIn(List<BigDecimal> values) {
            addCriterion("totalPrice not in", values, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("totalPrice between", value1, value2, "totalprice");
            return (Criteria) this;
        }

        public Criteria andTotalpriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("totalPrice not between", value1, value2, "totalprice");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("reMark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("reMark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("reMark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("reMark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("reMark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("reMark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("reMark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("reMark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("reMark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("reMark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("reMark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("reMark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("reMark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("reMark not between", value1, value2, "remark");
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

        public Criteria andUuidIsNull() {
            addCriterion("uuid is null");
            return (Criteria) this;
        }

        public Criteria andUuidIsNotNull() {
            addCriterion("uuid is not null");
            return (Criteria) this;
        }

        public Criteria andUuidEqualTo(String value) {
            addCriterion("uuid =", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotEqualTo(String value) {
            addCriterion("uuid <>", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThan(String value) {
            addCriterion("uuid >", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThanOrEqualTo(String value) {
            addCriterion("uuid >=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThan(String value) {
            addCriterion("uuid <", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThanOrEqualTo(String value) {
            addCriterion("uuid <=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLike(String value) {
            addCriterion("uuid like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotLike(String value) {
            addCriterion("uuid not like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidIn(List<String> values) {
            addCriterion("uuid in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotIn(List<String> values) {
            addCriterion("uuid not in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidBetween(String value1, String value2) {
            addCriterion("uuid between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotBetween(String value1, String value2) {
            addCriterion("uuid not between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andValidateIsNull() {
            addCriterion("validate is null");
            return (Criteria) this;
        }

        public Criteria andValidateIsNotNull() {
            addCriterion("validate is not null");
            return (Criteria) this;
        }

        public Criteria andValidateEqualTo(Integer value) {
            addCriterion("validate =", value, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateNotEqualTo(Integer value) {
            addCriterion("validate <>", value, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateGreaterThan(Integer value) {
            addCriterion("validate >", value, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateGreaterThanOrEqualTo(Integer value) {
            addCriterion("validate >=", value, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateLessThan(Integer value) {
            addCriterion("validate <", value, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateLessThanOrEqualTo(Integer value) {
            addCriterion("validate <=", value, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateIn(List<Integer> values) {
            addCriterion("validate in", values, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateNotIn(List<Integer> values) {
            addCriterion("validate not in", values, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateBetween(Integer value1, Integer value2) {
            addCriterion("validate between", value1, value2, "validate");
            return (Criteria) this;
        }

        public Criteria andValidateNotBetween(Integer value1, Integer value2) {
            addCriterion("validate not between", value1, value2, "validate");
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