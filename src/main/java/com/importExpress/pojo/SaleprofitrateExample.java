package com.importExpress.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleprofitrateExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SaleprofitrateExample() {
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

        public Criteria andGoodidIsNull() {
            addCriterion("goodid is null");
            return (Criteria) this;
        }

        public Criteria andGoodidIsNotNull() {
            addCriterion("goodid is not null");
            return (Criteria) this;
        }

        public Criteria andGoodidEqualTo(String value) {
            addCriterion("goodid =", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotEqualTo(String value) {
            addCriterion("goodid <>", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidGreaterThan(String value) {
            addCriterion("goodid >", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidGreaterThanOrEqualTo(String value) {
            addCriterion("goodid >=", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidLessThan(String value) {
            addCriterion("goodid <", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidLessThanOrEqualTo(String value) {
            addCriterion("goodid <=", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidLike(String value) {
            addCriterion("goodid like", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotLike(String value) {
            addCriterion("goodid not like", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidIn(List<String> values) {
            addCriterion("goodid in", values, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotIn(List<String> values) {
            addCriterion("goodid not in", values, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidBetween(String value1, String value2) {
            addCriterion("goodid between", value1, value2, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotBetween(String value1, String value2) {
            addCriterion("goodid not between", value1, value2, "goodid");
            return (Criteria) this;
        }

        public Criteria andSalesnumIsNull() {
            addCriterion("salesnum is null");
            return (Criteria) this;
        }

        public Criteria andSalesnumIsNotNull() {
            addCriterion("salesnum is not null");
            return (Criteria) this;
        }

        public Criteria andSalesnumEqualTo(Integer value) {
            addCriterion("salesnum =", value, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumNotEqualTo(Integer value) {
            addCriterion("salesnum <>", value, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumGreaterThan(Integer value) {
            addCriterion("salesnum >", value, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumGreaterThanOrEqualTo(Integer value) {
            addCriterion("salesnum >=", value, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumLessThan(Integer value) {
            addCriterion("salesnum <", value, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumLessThanOrEqualTo(Integer value) {
            addCriterion("salesnum <=", value, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumIn(List<Integer> values) {
            addCriterion("salesnum in", values, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumNotIn(List<Integer> values) {
            addCriterion("salesnum not in", values, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumBetween(Integer value1, Integer value2) {
            addCriterion("salesnum between", value1, value2, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalesnumNotBetween(Integer value1, Integer value2) {
            addCriterion("salesnum not between", value1, value2, "salesnum");
            return (Criteria) this;
        }

        public Criteria andSalepriceIsNull() {
            addCriterion("saleprice is null");
            return (Criteria) this;
        }

        public Criteria andSalepriceIsNotNull() {
            addCriterion("saleprice is not null");
            return (Criteria) this;
        }

        public Criteria andSalepriceEqualTo(BigDecimal value) {
            addCriterion("saleprice =", value, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceNotEqualTo(BigDecimal value) {
            addCriterion("saleprice <>", value, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceGreaterThan(BigDecimal value) {
            addCriterion("saleprice >", value, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("saleprice >=", value, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceLessThan(BigDecimal value) {
            addCriterion("saleprice <", value, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("saleprice <=", value, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceIn(List<BigDecimal> values) {
            addCriterion("saleprice in", values, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceNotIn(List<BigDecimal> values) {
            addCriterion("saleprice not in", values, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("saleprice between", value1, value2, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSalepriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("saleprice not between", value1, value2, "saleprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceIsNull() {
            addCriterion("sourceprice is null");
            return (Criteria) this;
        }

        public Criteria andSourcepriceIsNotNull() {
            addCriterion("sourceprice is not null");
            return (Criteria) this;
        }

        public Criteria andSourcepriceEqualTo(BigDecimal value) {
            addCriterion("sourceprice =", value, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceNotEqualTo(BigDecimal value) {
            addCriterion("sourceprice <>", value, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceGreaterThan(BigDecimal value) {
            addCriterion("sourceprice >", value, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sourceprice >=", value, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceLessThan(BigDecimal value) {
            addCriterion("sourceprice <", value, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sourceprice <=", value, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceIn(List<BigDecimal> values) {
            addCriterion("sourceprice in", values, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceNotIn(List<BigDecimal> values) {
            addCriterion("sourceprice not in", values, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sourceprice between", value1, value2, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andSourcepriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sourceprice not between", value1, value2, "sourceprice");
            return (Criteria) this;
        }

        public Criteria andProfitrateIsNull() {
            addCriterion("profitrate is null");
            return (Criteria) this;
        }

        public Criteria andProfitrateIsNotNull() {
            addCriterion("profitrate is not null");
            return (Criteria) this;
        }

        public Criteria andProfitrateEqualTo(String value) {
            addCriterion("profitrate =", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateNotEqualTo(String value) {
            addCriterion("profitrate <>", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateGreaterThan(String value) {
            addCriterion("profitrate >", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateGreaterThanOrEqualTo(String value) {
            addCriterion("profitrate >=", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateLessThan(String value) {
            addCriterion("profitrate <", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateLessThanOrEqualTo(String value) {
            addCriterion("profitrate <=", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateLike(String value) {
            addCriterion("profitrate like", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateNotLike(String value) {
            addCriterion("profitrate not like", value, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateIn(List<String> values) {
            addCriterion("profitrate in", values, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateNotIn(List<String> values) {
            addCriterion("profitrate not in", values, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateBetween(String value1, String value2) {
            addCriterion("profitrate between", value1, value2, "profitrate");
            return (Criteria) this;
        }

        public Criteria andProfitrateNotBetween(String value1, String value2) {
            addCriterion("profitrate not between", value1, value2, "profitrate");
            return (Criteria) this;
        }

        public Criteria andCreattimeIsNull() {
            addCriterion("creattime is null");
            return (Criteria) this;
        }

        public Criteria andCreattimeIsNotNull() {
            addCriterion("creattime is not null");
            return (Criteria) this;
        }

        public Criteria andCreattimeEqualTo(Date value) {
            addCriterion("creattime =", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotEqualTo(Date value) {
            addCriterion("creattime <>", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeGreaterThan(Date value) {
            addCriterion("creattime >", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeGreaterThanOrEqualTo(Date value) {
            addCriterion("creattime >=", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeLessThan(Date value) {
            addCriterion("creattime <", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeLessThanOrEqualTo(Date value) {
            addCriterion("creattime <=", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeIn(List<Date> values) {
            addCriterion("creattime in", values, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotIn(List<Date> values) {
            addCriterion("creattime not in", values, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeBetween(Date value1, Date value2) {
            addCriterion("creattime between", value1, value2, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotBetween(Date value1, Date value2) {
            addCriterion("creattime not between", value1, value2, "creattime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeIsNull() {
            addCriterion("updatatime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeIsNotNull() {
            addCriterion("updatatime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeEqualTo(Date value) {
            addCriterion("updatatime =", value, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeNotEqualTo(Date value) {
            addCriterion("updatatime <>", value, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeGreaterThan(Date value) {
            addCriterion("updatatime >", value, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updatatime >=", value, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeLessThan(Date value) {
            addCriterion("updatatime <", value, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeLessThanOrEqualTo(Date value) {
            addCriterion("updatatime <=", value, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeIn(List<Date> values) {
            addCriterion("updatatime in", values, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeNotIn(List<Date> values) {
            addCriterion("updatatime not in", values, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeBetween(Date value1, Date value2) {
            addCriterion("updatatime between", value1, value2, "updatatime");
            return (Criteria) this;
        }

        public Criteria andUpdatatimeNotBetween(Date value1, Date value2) {
            addCriterion("updatatime not between", value1, value2, "updatatime");
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