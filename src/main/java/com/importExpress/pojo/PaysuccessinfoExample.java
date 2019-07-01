package com.importExpress.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaysuccessinfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PaysuccessinfoExample() {
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

        public Criteria andOrdernoIsNull() {
            addCriterion("orderno is null");
            return (Criteria) this;
        }

        public Criteria andOrdernoIsNotNull() {
            addCriterion("orderno is not null");
            return (Criteria) this;
        }

        public Criteria andOrdernoEqualTo(String value) {
            addCriterion("orderno =", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotEqualTo(String value) {
            addCriterion("orderno <>", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoGreaterThan(String value) {
            addCriterion("orderno >", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoGreaterThanOrEqualTo(String value) {
            addCriterion("orderno >=", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoLessThan(String value) {
            addCriterion("orderno <", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoLessThanOrEqualTo(String value) {
            addCriterion("orderno <=", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoLike(String value) {
            addCriterion("orderno like", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotLike(String value) {
            addCriterion("orderno not like", value, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoIn(List<String> values) {
            addCriterion("orderno in", values, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotIn(List<String> values) {
            addCriterion("orderno not in", values, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoBetween(String value1, String value2) {
            addCriterion("orderno between", value1, value2, "orderno");
            return (Criteria) this;
        }

        public Criteria andOrdernoNotBetween(String value1, String value2) {
            addCriterion("orderno not between", value1, value2, "orderno");
            return (Criteria) this;
        }

        public Criteria andPaymentamountIsNull() {
            addCriterion("paymentamount is null");
            return (Criteria) this;
        }

        public Criteria andPaymentamountIsNotNull() {
            addCriterion("paymentamount is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentamountEqualTo(String value) {
            addCriterion("paymentamount =", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountNotEqualTo(String value) {
            addCriterion("paymentamount <>", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountGreaterThan(String value) {
            addCriterion("paymentamount >", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountGreaterThanOrEqualTo(String value) {
            addCriterion("paymentamount >=", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountLessThan(String value) {
            addCriterion("paymentamount <", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountLessThanOrEqualTo(String value) {
            addCriterion("paymentamount <=", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountLike(String value) {
            addCriterion("paymentamount like", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountNotLike(String value) {
            addCriterion("paymentamount not like", value, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountIn(List<String> values) {
            addCriterion("paymentamount in", values, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountNotIn(List<String> values) {
            addCriterion("paymentamount not in", values, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountBetween(String value1, String value2) {
            addCriterion("paymentamount between", value1, value2, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andPaymentamountNotBetween(String value1, String value2) {
            addCriterion("paymentamount not between", value1, value2, "paymentamount");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceIsNull() {
            addCriterion("sampleschoice is null");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceIsNotNull() {
            addCriterion("sampleschoice is not null");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceEqualTo(Integer value) {
            addCriterion("sampleschoice =", value, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceNotEqualTo(Integer value) {
            addCriterion("sampleschoice <>", value, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceGreaterThan(Integer value) {
            addCriterion("sampleschoice >", value, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceGreaterThanOrEqualTo(Integer value) {
            addCriterion("sampleschoice >=", value, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceLessThan(Integer value) {
            addCriterion("sampleschoice <", value, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceLessThanOrEqualTo(Integer value) {
            addCriterion("sampleschoice <=", value, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceIn(List<Integer> values) {
            addCriterion("sampleschoice in", values, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceNotIn(List<Integer> values) {
            addCriterion("sampleschoice not in", values, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceBetween(Integer value1, Integer value2) {
            addCriterion("sampleschoice between", value1, value2, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSampleschoiceNotBetween(Integer value1, Integer value2) {
            addCriterion("sampleschoice not between", value1, value2, "sampleschoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceIsNull() {
            addCriterion("sharechoice is null");
            return (Criteria) this;
        }

        public Criteria andSharechoiceIsNotNull() {
            addCriterion("sharechoice is not null");
            return (Criteria) this;
        }

        public Criteria andSharechoiceEqualTo(Integer value) {
            addCriterion("sharechoice =", value, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceNotEqualTo(Integer value) {
            addCriterion("sharechoice <>", value, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceGreaterThan(Integer value) {
            addCriterion("sharechoice >", value, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceGreaterThanOrEqualTo(Integer value) {
            addCriterion("sharechoice >=", value, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceLessThan(Integer value) {
            addCriterion("sharechoice <", value, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceLessThanOrEqualTo(Integer value) {
            addCriterion("sharechoice <=", value, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceIn(List<Integer> values) {
            addCriterion("sharechoice in", values, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceNotIn(List<Integer> values) {
            addCriterion("sharechoice not in", values, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceBetween(Integer value1, Integer value2) {
            addCriterion("sharechoice between", value1, value2, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andSharechoiceNotBetween(Integer value1, Integer value2) {
            addCriterion("sharechoice not between", value1, value2, "sharechoice");
            return (Criteria) this;
        }

        public Criteria andInfoIsNull() {
            addCriterion("info is null");
            return (Criteria) this;
        }

        public Criteria andInfoIsNotNull() {
            addCriterion("info is not null");
            return (Criteria) this;
        }

        public Criteria andInfoEqualTo(String value) {
            addCriterion("info =", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotEqualTo(String value) {
            addCriterion("info <>", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoGreaterThan(String value) {
            addCriterion("info >", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoGreaterThanOrEqualTo(String value) {
            addCriterion("info >=", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLessThan(String value) {
            addCriterion("info <", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLessThanOrEqualTo(String value) {
            addCriterion("info <=", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLike(String value) {
            addCriterion("info like", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotLike(String value) {
            addCriterion("info not like", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoIn(List<String> values) {
            addCriterion("info in", values, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotIn(List<String> values) {
            addCriterion("info not in", values, "info");
            return (Criteria) this;
        }

        public Criteria andInfoBetween(String value1, String value2) {
            addCriterion("info between", value1, value2, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotBetween(String value1, String value2) {
            addCriterion("info not between", value1, value2, "info");
            return (Criteria) this;
        }

        public Criteria andCreatimeIsNull() {
            addCriterion("creatime is null");
            return (Criteria) this;
        }

        public Criteria andCreatimeIsNotNull() {
            addCriterion("creatime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatimeEqualTo(Date value) {
            addCriterion("creatime =", value, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeNotEqualTo(Date value) {
            addCriterion("creatime <>", value, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeGreaterThan(Date value) {
            addCriterion("creatime >", value, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeGreaterThanOrEqualTo(Date value) {
            addCriterion("creatime >=", value, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeLessThan(Date value) {
            addCriterion("creatime <", value, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeLessThanOrEqualTo(Date value) {
            addCriterion("creatime <=", value, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeIn(List<Date> values) {
            addCriterion("creatime in", values, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeNotIn(List<Date> values) {
            addCriterion("creatime not in", values, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeBetween(Date value1, Date value2) {
            addCriterion("creatime between", value1, value2, "creatime");
            return (Criteria) this;
        }

        public Criteria andCreatimeNotBetween(Date value1, Date value2) {
            addCriterion("creatime not between", value1, value2, "creatime");
            return (Criteria) this;
        }

        public Criteria andDelIsNull() {
            addCriterion("del is null");
            return (Criteria) this;
        }

        public Criteria andDelIsNotNull() {
            addCriterion("del is not null");
            return (Criteria) this;
        }

        public Criteria andDelEqualTo(Integer value) {
            addCriterion("del =", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelNotEqualTo(Integer value) {
            addCriterion("del <>", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelGreaterThan(Integer value) {
            addCriterion("del >", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelGreaterThanOrEqualTo(Integer value) {
            addCriterion("del >=", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelLessThan(Integer value) {
            addCriterion("del <", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelLessThanOrEqualTo(Integer value) {
            addCriterion("del <=", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelIn(List<Integer> values) {
            addCriterion("del in", values, "del");
            return (Criteria) this;
        }

        public Criteria andDelNotIn(List<Integer> values) {
            addCriterion("del not in", values, "del");
            return (Criteria) this;
        }

        public Criteria andDelBetween(Integer value1, Integer value2) {
            addCriterion("del between", value1, value2, "del");
            return (Criteria) this;
        }

        public Criteria andDelNotBetween(Integer value1, Integer value2) {
            addCriterion("del not between", value1, value2, "del");
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