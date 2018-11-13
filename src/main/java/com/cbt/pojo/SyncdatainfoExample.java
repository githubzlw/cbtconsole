package com.cbt.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SyncdatainfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SyncdatainfoExample() {
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

        public Criteria andSqlstrIsNull() {
            addCriterion("sqlStr is null");
            return (Criteria) this;
        }

        public Criteria andSqlstrIsNotNull() {
            addCriterion("sqlStr is not null");
            return (Criteria) this;
        }

        public Criteria andSqlstrEqualTo(String value) {
            addCriterion("sqlStr =", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrNotEqualTo(String value) {
            addCriterion("sqlStr <>", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrGreaterThan(String value) {
            addCriterion("sqlStr >", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrGreaterThanOrEqualTo(String value) {
            addCriterion("sqlStr >=", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrLessThan(String value) {
            addCriterion("sqlStr <", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrLessThanOrEqualTo(String value) {
            addCriterion("sqlStr <=", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrLike(String value) {
            addCriterion("sqlStr like", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrNotLike(String value) {
            addCriterion("sqlStr not like", value, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrIn(List<String> values) {
            addCriterion("sqlStr in", values, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrNotIn(List<String> values) {
            addCriterion("sqlStr not in", values, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrBetween(String value1, String value2) {
            addCriterion("sqlStr between", value1, value2, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andSqlstrNotBetween(String value1, String value2) {
            addCriterion("sqlStr not between", value1, value2, "sqlstr");
            return (Criteria) this;
        }

        public Criteria andCreattimeIsNull() {
            addCriterion("creatTime is null");
            return (Criteria) this;
        }

        public Criteria andCreattimeIsNotNull() {
            addCriterion("creatTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreattimeEqualTo(Date value) {
            addCriterion("creatTime =", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotEqualTo(Date value) {
            addCriterion("creatTime <>", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeGreaterThan(Date value) {
            addCriterion("creatTime >", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeGreaterThanOrEqualTo(Date value) {
            addCriterion("creatTime >=", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeLessThan(Date value) {
            addCriterion("creatTime <", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeLessThanOrEqualTo(Date value) {
            addCriterion("creatTime <=", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeIn(List<Date> values) {
            addCriterion("creatTime in", values, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotIn(List<Date> values) {
            addCriterion("creatTime not in", values, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeBetween(Date value1, Date value2) {
            addCriterion("creatTime between", value1, value2, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotBetween(Date value1, Date value2) {
            addCriterion("creatTime not between", value1, value2, "creattime");
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

        public Criteria andRemaekIsNull() {
            addCriterion("remaek is null");
            return (Criteria) this;
        }

        public Criteria andRemaekIsNotNull() {
            addCriterion("remaek is not null");
            return (Criteria) this;
        }

        public Criteria andRemaekEqualTo(String value) {
            addCriterion("remaek =", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekNotEqualTo(String value) {
            addCriterion("remaek <>", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekGreaterThan(String value) {
            addCriterion("remaek >", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekGreaterThanOrEqualTo(String value) {
            addCriterion("remaek >=", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekLessThan(String value) {
            addCriterion("remaek <", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekLessThanOrEqualTo(String value) {
            addCriterion("remaek <=", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekLike(String value) {
            addCriterion("remaek like", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekNotLike(String value) {
            addCriterion("remaek not like", value, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekIn(List<String> values) {
            addCriterion("remaek in", values, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekNotIn(List<String> values) {
            addCriterion("remaek not in", values, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekBetween(String value1, String value2) {
            addCriterion("remaek between", value1, value2, "remaek");
            return (Criteria) this;
        }

        public Criteria andRemaekNotBetween(String value1, String value2) {
            addCriterion("remaek not between", value1, value2, "remaek");
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