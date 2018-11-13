package com.cbt.warehouse.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MabangshipmentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MabangshipmentExample() {
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

        public Criteria andPaymenttimeIsNull() {
            addCriterion("paymentTime is null");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeIsNotNull() {
            addCriterion("paymentTime is not null");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeEqualTo(Date value) {
            addCriterion("paymentTime =", value, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeNotEqualTo(Date value) {
            addCriterion("paymentTime <>", value, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeGreaterThan(Date value) {
            addCriterion("paymentTime >", value, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeGreaterThanOrEqualTo(Date value) {
            addCriterion("paymentTime >=", value, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeLessThan(Date value) {
            addCriterion("paymentTime <", value, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeLessThanOrEqualTo(Date value) {
            addCriterion("paymentTime <=", value, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeIn(List<Date> values) {
            addCriterion("paymentTime in", values, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeNotIn(List<Date> values) {
            addCriterion("paymentTime not in", values, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeBetween(Date value1, Date value2) {
            addCriterion("paymentTime between", value1, value2, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andPaymenttimeNotBetween(Date value1, Date value2) {
            addCriterion("paymentTime not between", value1, value2, "paymenttime");
            return (Criteria) this;
        }

        public Criteria andShipmethodIsNull() {
            addCriterion("shipMethod is null");
            return (Criteria) this;
        }

        public Criteria andShipmethodIsNotNull() {
            addCriterion("shipMethod is not null");
            return (Criteria) this;
        }

        public Criteria andShipmethodEqualTo(String value) {
            addCriterion("shipMethod =", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodNotEqualTo(String value) {
            addCriterion("shipMethod <>", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodGreaterThan(String value) {
            addCriterion("shipMethod >", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodGreaterThanOrEqualTo(String value) {
            addCriterion("shipMethod >=", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodLessThan(String value) {
            addCriterion("shipMethod <", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodLessThanOrEqualTo(String value) {
            addCriterion("shipMethod <=", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodLike(String value) {
            addCriterion("shipMethod like", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodNotLike(String value) {
            addCriterion("shipMethod not like", value, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodIn(List<String> values) {
            addCriterion("shipMethod in", values, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodNotIn(List<String> values) {
            addCriterion("shipMethod not in", values, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodBetween(String value1, String value2) {
            addCriterion("shipMethod between", value1, value2, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipmethodNotBetween(String value1, String value2) {
            addCriterion("shipMethod not between", value1, value2, "shipmethod");
            return (Criteria) this;
        }

        public Criteria andShipnoIsNull() {
            addCriterion("shipNo is null");
            return (Criteria) this;
        }

        public Criteria andShipnoIsNotNull() {
            addCriterion("shipNo is not null");
            return (Criteria) this;
        }

        public Criteria andShipnoEqualTo(String value) {
            addCriterion("shipNo =", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoNotEqualTo(String value) {
            addCriterion("shipNo <>", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoGreaterThan(String value) {
            addCriterion("shipNo >", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoGreaterThanOrEqualTo(String value) {
            addCriterion("shipNo >=", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoLessThan(String value) {
            addCriterion("shipNo <", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoLessThanOrEqualTo(String value) {
            addCriterion("shipNo <=", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoLike(String value) {
            addCriterion("shipNo like", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoNotLike(String value) {
            addCriterion("shipNo not like", value, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoIn(List<String> values) {
            addCriterion("shipNo in", values, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoNotIn(List<String> values) {
            addCriterion("shipNo not in", values, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoBetween(String value1, String value2) {
            addCriterion("shipNo between", value1, value2, "shipno");
            return (Criteria) this;
        }

        public Criteria andShipnoNotBetween(String value1, String value2) {
            addCriterion("shipNo not between", value1, value2, "shipno");
            return (Criteria) this;
        }

        public Criteria andStorenameIsNull() {
            addCriterion("storeName is null");
            return (Criteria) this;
        }

        public Criteria andStorenameIsNotNull() {
            addCriterion("storeName is not null");
            return (Criteria) this;
        }

        public Criteria andStorenameEqualTo(String value) {
            addCriterion("storeName =", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameNotEqualTo(String value) {
            addCriterion("storeName <>", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameGreaterThan(String value) {
            addCriterion("storeName >", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameGreaterThanOrEqualTo(String value) {
            addCriterion("storeName >=", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameLessThan(String value) {
            addCriterion("storeName <", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameLessThanOrEqualTo(String value) {
            addCriterion("storeName <=", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameLike(String value) {
            addCriterion("storeName like", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameNotLike(String value) {
            addCriterion("storeName not like", value, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameIn(List<String> values) {
            addCriterion("storeName in", values, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameNotIn(List<String> values) {
            addCriterion("storeName not in", values, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameBetween(String value1, String value2) {
            addCriterion("storeName between", value1, value2, "storename");
            return (Criteria) this;
        }

        public Criteria andStorenameNotBetween(String value1, String value2) {
            addCriterion("storeName not between", value1, value2, "storename");
            return (Criteria) this;
        }

        public Criteria andClientacountIsNull() {
            addCriterion("clientAcount is null");
            return (Criteria) this;
        }

        public Criteria andClientacountIsNotNull() {
            addCriterion("clientAcount is not null");
            return (Criteria) this;
        }

        public Criteria andClientacountEqualTo(String value) {
            addCriterion("clientAcount =", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountNotEqualTo(String value) {
            addCriterion("clientAcount <>", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountGreaterThan(String value) {
            addCriterion("clientAcount >", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountGreaterThanOrEqualTo(String value) {
            addCriterion("clientAcount >=", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountLessThan(String value) {
            addCriterion("clientAcount <", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountLessThanOrEqualTo(String value) {
            addCriterion("clientAcount <=", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountLike(String value) {
            addCriterion("clientAcount like", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountNotLike(String value) {
            addCriterion("clientAcount not like", value, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountIn(List<String> values) {
            addCriterion("clientAcount in", values, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountNotIn(List<String> values) {
            addCriterion("clientAcount not in", values, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountBetween(String value1, String value2) {
            addCriterion("clientAcount between", value1, value2, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientacountNotBetween(String value1, String value2) {
            addCriterion("clientAcount not between", value1, value2, "clientacount");
            return (Criteria) this;
        }

        public Criteria andClientnameIsNull() {
            addCriterion("clientName is null");
            return (Criteria) this;
        }

        public Criteria andClientnameIsNotNull() {
            addCriterion("clientName is not null");
            return (Criteria) this;
        }

        public Criteria andClientnameEqualTo(String value) {
            addCriterion("clientName =", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameNotEqualTo(String value) {
            addCriterion("clientName <>", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameGreaterThan(String value) {
            addCriterion("clientName >", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameGreaterThanOrEqualTo(String value) {
            addCriterion("clientName >=", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameLessThan(String value) {
            addCriterion("clientName <", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameLessThanOrEqualTo(String value) {
            addCriterion("clientName <=", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameLike(String value) {
            addCriterion("clientName like", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameNotLike(String value) {
            addCriterion("clientName not like", value, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameIn(List<String> values) {
            addCriterion("clientName in", values, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameNotIn(List<String> values) {
            addCriterion("clientName not in", values, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameBetween(String value1, String value2) {
            addCriterion("clientName between", value1, value2, "clientname");
            return (Criteria) this;
        }

        public Criteria andClientnameNotBetween(String value1, String value2) {
            addCriterion("clientName not between", value1, value2, "clientname");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneIsNull() {
            addCriterion("clientTelphone is null");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneIsNotNull() {
            addCriterion("clientTelphone is not null");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneEqualTo(String value) {
            addCriterion("clientTelphone =", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneNotEqualTo(String value) {
            addCriterion("clientTelphone <>", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneGreaterThan(String value) {
            addCriterion("clientTelphone >", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneGreaterThanOrEqualTo(String value) {
            addCriterion("clientTelphone >=", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneLessThan(String value) {
            addCriterion("clientTelphone <", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneLessThanOrEqualTo(String value) {
            addCriterion("clientTelphone <=", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneLike(String value) {
            addCriterion("clientTelphone like", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneNotLike(String value) {
            addCriterion("clientTelphone not like", value, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneIn(List<String> values) {
            addCriterion("clientTelphone in", values, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneNotIn(List<String> values) {
            addCriterion("clientTelphone not in", values, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneBetween(String value1, String value2) {
            addCriterion("clientTelphone between", value1, value2, "clienttelphone");
            return (Criteria) this;
        }

        public Criteria andClienttelphoneNotBetween(String value1, String value2) {
            addCriterion("clientTelphone not between", value1, value2, "clienttelphone");
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

        public Criteria andProductunitpriceIsNull() {
            addCriterion("productUnitPrice is null");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceIsNotNull() {
            addCriterion("productUnitPrice is not null");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceEqualTo(BigDecimal value) {
            addCriterion("productUnitPrice =", value, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceNotEqualTo(BigDecimal value) {
            addCriterion("productUnitPrice <>", value, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceGreaterThan(BigDecimal value) {
            addCriterion("productUnitPrice >", value, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("productUnitPrice >=", value, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceLessThan(BigDecimal value) {
            addCriterion("productUnitPrice <", value, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("productUnitPrice <=", value, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceIn(List<BigDecimal> values) {
            addCriterion("productUnitPrice in", values, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceNotIn(List<BigDecimal> values) {
            addCriterion("productUnitPrice not in", values, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("productUnitPrice between", value1, value2, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProductunitpriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("productUnitPrice not between", value1, value2, "productunitprice");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountIsNull() {
            addCriterion("productTotalAmount is null");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountIsNotNull() {
            addCriterion("productTotalAmount is not null");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountEqualTo(BigDecimal value) {
            addCriterion("productTotalAmount =", value, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountNotEqualTo(BigDecimal value) {
            addCriterion("productTotalAmount <>", value, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountGreaterThan(BigDecimal value) {
            addCriterion("productTotalAmount >", value, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("productTotalAmount >=", value, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountLessThan(BigDecimal value) {
            addCriterion("productTotalAmount <", value, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("productTotalAmount <=", value, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountIn(List<BigDecimal> values) {
            addCriterion("productTotalAmount in", values, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountNotIn(List<BigDecimal> values) {
            addCriterion("productTotalAmount not in", values, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("productTotalAmount between", value1, value2, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalamountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("productTotalAmount not between", value1, value2, "producttotalamount");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostIsNull() {
            addCriterion("productTotalCost is null");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostIsNotNull() {
            addCriterion("productTotalCost is not null");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostEqualTo(BigDecimal value) {
            addCriterion("productTotalCost =", value, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostNotEqualTo(BigDecimal value) {
            addCriterion("productTotalCost <>", value, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostGreaterThan(BigDecimal value) {
            addCriterion("productTotalCost >", value, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("productTotalCost >=", value, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostLessThan(BigDecimal value) {
            addCriterion("productTotalCost <", value, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostLessThanOrEqualTo(BigDecimal value) {
            addCriterion("productTotalCost <=", value, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostIn(List<BigDecimal> values) {
            addCriterion("productTotalCost in", values, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostNotIn(List<BigDecimal> values) {
            addCriterion("productTotalCost not in", values, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("productTotalCost between", value1, value2, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andProducttotalcostNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("productTotalCost not between", value1, value2, "producttotalcost");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueIsNull() {
            addCriterion("freightRevenue is null");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueIsNotNull() {
            addCriterion("freightRevenue is not null");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueEqualTo(BigDecimal value) {
            addCriterion("freightRevenue =", value, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueNotEqualTo(BigDecimal value) {
            addCriterion("freightRevenue <>", value, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueGreaterThan(BigDecimal value) {
            addCriterion("freightRevenue >", value, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("freightRevenue >=", value, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueLessThan(BigDecimal value) {
            addCriterion("freightRevenue <", value, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("freightRevenue <=", value, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueIn(List<BigDecimal> values) {
            addCriterion("freightRevenue in", values, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueNotIn(List<BigDecimal> values) {
            addCriterion("freightRevenue not in", values, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("freightRevenue between", value1, value2, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andFreightrevenueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("freightRevenue not between", value1, value2, "freightrevenue");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesIsNull() {
            addCriterion("shipmentExpenses is null");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesIsNotNull() {
            addCriterion("shipmentExpenses is not null");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesEqualTo(BigDecimal value) {
            addCriterion("shipmentExpenses =", value, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesNotEqualTo(BigDecimal value) {
            addCriterion("shipmentExpenses <>", value, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesGreaterThan(BigDecimal value) {
            addCriterion("shipmentExpenses >", value, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("shipmentExpenses >=", value, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesLessThan(BigDecimal value) {
            addCriterion("shipmentExpenses <", value, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("shipmentExpenses <=", value, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesIn(List<BigDecimal> values) {
            addCriterion("shipmentExpenses in", values, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesNotIn(List<BigDecimal> values) {
            addCriterion("shipmentExpenses not in", values, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("shipmentExpenses between", value1, value2, "shipmentexpenses");
            return (Criteria) this;
        }

        public Criteria andShipmentexpensesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("shipmentExpenses not between", value1, value2, "shipmentexpenses");
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

        public Criteria andWeightEqualTo(BigDecimal value) {
            addCriterion("weight =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(BigDecimal value) {
            addCriterion("weight <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(BigDecimal value) {
            addCriterion("weight >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("weight >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(BigDecimal value) {
            addCriterion("weight <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(BigDecimal value) {
            addCriterion("weight <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<BigDecimal> values) {
            addCriterion("weight in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<BigDecimal> values) {
            addCriterion("weight not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("weight between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("weight not between", value1, value2, "weight");
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

        public Criteria andProductnameIsNull() {
            addCriterion("productName is null");
            return (Criteria) this;
        }

        public Criteria andProductnameIsNotNull() {
            addCriterion("productName is not null");
            return (Criteria) this;
        }

        public Criteria andProductnameEqualTo(String value) {
            addCriterion("productName =", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameNotEqualTo(String value) {
            addCriterion("productName <>", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameGreaterThan(String value) {
            addCriterion("productName >", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameGreaterThanOrEqualTo(String value) {
            addCriterion("productName >=", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameLessThan(String value) {
            addCriterion("productName <", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameLessThanOrEqualTo(String value) {
            addCriterion("productName <=", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameLike(String value) {
            addCriterion("productName like", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameNotLike(String value) {
            addCriterion("productName not like", value, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameIn(List<String> values) {
            addCriterion("productName in", values, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameNotIn(List<String> values) {
            addCriterion("productName not in", values, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameBetween(String value1, String value2) {
            addCriterion("productName between", value1, value2, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnameNotBetween(String value1, String value2) {
            addCriterion("productName not between", value1, value2, "productname");
            return (Criteria) this;
        }

        public Criteria andProductnumbersIsNull() {
            addCriterion("productNumbers is null");
            return (Criteria) this;
        }

        public Criteria andProductnumbersIsNotNull() {
            addCriterion("productNumbers is not null");
            return (Criteria) this;
        }

        public Criteria andProductnumbersEqualTo(Integer value) {
            addCriterion("productNumbers =", value, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersNotEqualTo(Integer value) {
            addCriterion("productNumbers <>", value, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersGreaterThan(Integer value) {
            addCriterion("productNumbers >", value, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersGreaterThanOrEqualTo(Integer value) {
            addCriterion("productNumbers >=", value, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersLessThan(Integer value) {
            addCriterion("productNumbers <", value, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersLessThanOrEqualTo(Integer value) {
            addCriterion("productNumbers <=", value, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersIn(List<Integer> values) {
            addCriterion("productNumbers in", values, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersNotIn(List<Integer> values) {
            addCriterion("productNumbers not in", values, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersBetween(Integer value1, Integer value2) {
            addCriterion("productNumbers between", value1, value2, "productnumbers");
            return (Criteria) this;
        }

        public Criteria andProductnumbersNotBetween(Integer value1, Integer value2) {
            addCriterion("productNumbers not between", value1, value2, "productnumbers");
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

        public Criteria andOrdertotalamoutIsNull() {
            addCriterion("orderTotalAmout is null");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutIsNotNull() {
            addCriterion("orderTotalAmout is not null");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutEqualTo(BigDecimal value) {
            addCriterion("orderTotalAmout =", value, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutNotEqualTo(BigDecimal value) {
            addCriterion("orderTotalAmout <>", value, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutGreaterThan(BigDecimal value) {
            addCriterion("orderTotalAmout >", value, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("orderTotalAmout >=", value, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutLessThan(BigDecimal value) {
            addCriterion("orderTotalAmout <", value, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutLessThanOrEqualTo(BigDecimal value) {
            addCriterion("orderTotalAmout <=", value, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutIn(List<BigDecimal> values) {
            addCriterion("orderTotalAmout in", values, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutNotIn(List<BigDecimal> values) {
            addCriterion("orderTotalAmout not in", values, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("orderTotalAmout between", value1, value2, "ordertotalamout");
            return (Criteria) this;
        }

        public Criteria andOrdertotalamoutNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("orderTotalAmout not between", value1, value2, "ordertotalamout");
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