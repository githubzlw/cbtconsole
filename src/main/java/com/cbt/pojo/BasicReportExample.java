package com.cbt.pojo;

import java.util.ArrayList;
import java.util.List;

public class BasicReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BasicReportExample() {
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

        public Criteria andBreportNameIsNull() {
            addCriterion("breport_name is null");
            return (Criteria) this;
        }

        public Criteria andBreportNameIsNotNull() {
            addCriterion("breport_name is not null");
            return (Criteria) this;
        }

        public Criteria andBreportNameEqualTo(String value) {
            addCriterion("breport_name =", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameNotEqualTo(String value) {
            addCriterion("breport_name <>", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameGreaterThan(String value) {
            addCriterion("breport_name >", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameGreaterThanOrEqualTo(String value) {
            addCriterion("breport_name >=", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameLessThan(String value) {
            addCriterion("breport_name <", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameLessThanOrEqualTo(String value) {
            addCriterion("breport_name <=", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameLike(String value) {
            addCriterion("breport_name like", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameNotLike(String value) {
            addCriterion("breport_name not like", value, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameIn(List<String> values) {
            addCriterion("breport_name in", values, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameNotIn(List<String> values) {
            addCriterion("breport_name not in", values, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameBetween(String value1, String value2) {
            addCriterion("breport_name between", value1, value2, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportNameNotBetween(String value1, String value2) {
            addCriterion("breport_name not between", value1, value2, "breportName");
            return (Criteria) this;
        }

        public Criteria andBreportTypeIsNull() {
            addCriterion("breport_type is null");
            return (Criteria) this;
        }

        public Criteria andBreportTypeIsNotNull() {
            addCriterion("breport_type is not null");
            return (Criteria) this;
        }

        public Criteria andBreportTypeEqualTo(String value) {
            addCriterion("breport_type =", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeNotEqualTo(String value) {
            addCriterion("breport_type <>", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeGreaterThan(String value) {
            addCriterion("breport_type >", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeGreaterThanOrEqualTo(String value) {
            addCriterion("breport_type >=", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeLessThan(String value) {
            addCriterion("breport_type <", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeLessThanOrEqualTo(String value) {
            addCriterion("breport_type <=", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeLike(String value) {
            addCriterion("breport_type like", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeNotLike(String value) {
            addCriterion("breport_type not like", value, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeIn(List<String> values) {
            addCriterion("breport_type in", values, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeNotIn(List<String> values) {
            addCriterion("breport_type not in", values, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeBetween(String value1, String value2) {
            addCriterion("breport_type between", value1, value2, "breportType");
            return (Criteria) this;
        }

        public Criteria andBreportTypeNotBetween(String value1, String value2) {
            addCriterion("breport_type not between", value1, value2, "breportType");
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