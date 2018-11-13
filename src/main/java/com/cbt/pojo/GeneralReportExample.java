package com.cbt.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneralReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GeneralReportExample() {
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

        public Criteria andBreportIdIsNull() {
            addCriterion("breport_id is null");
            return (Criteria) this;
        }

        public Criteria andBreportIdIsNotNull() {
            addCriterion("breport_id is not null");
            return (Criteria) this;
        }

        public Criteria andBreportIdEqualTo(Integer value) {
            addCriterion("breport_id =", value, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdNotEqualTo(Integer value) {
            addCriterion("breport_id <>", value, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdGreaterThan(Integer value) {
            addCriterion("breport_id >", value, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("breport_id >=", value, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdLessThan(Integer value) {
            addCriterion("breport_id <", value, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdLessThanOrEqualTo(Integer value) {
            addCriterion("breport_id <=", value, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdIn(List<Integer> values) {
            addCriterion("breport_id in", values, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdNotIn(List<Integer> values) {
            addCriterion("breport_id not in", values, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdBetween(Integer value1, Integer value2) {
            addCriterion("breport_id between", value1, value2, "breportId");
            return (Criteria) this;
        }

        public Criteria andBreportIdNotBetween(Integer value1, Integer value2) {
            addCriterion("breport_id not between", value1, value2, "breportId");
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

        public Criteria andReportYearIsNull() {
            addCriterion("report_year is null");
            return (Criteria) this;
        }

        public Criteria andReportYearIsNotNull() {
            addCriterion("report_year is not null");
            return (Criteria) this;
        }

        public Criteria andReportYearEqualTo(String value) {
            addCriterion("report_year =", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearNotEqualTo(String value) {
            addCriterion("report_year <>", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearGreaterThan(String value) {
            addCriterion("report_year >", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearGreaterThanOrEqualTo(String value) {
            addCriterion("report_year >=", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearLessThan(String value) {
            addCriterion("report_year <", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearLessThanOrEqualTo(String value) {
            addCriterion("report_year <=", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearLike(String value) {
            addCriterion("report_year like", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearNotLike(String value) {
            addCriterion("report_year not like", value, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearIn(List<String> values) {
            addCriterion("report_year in", values, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearNotIn(List<String> values) {
            addCriterion("report_year not in", values, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearBetween(String value1, String value2) {
            addCriterion("report_year between", value1, value2, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportYearNotBetween(String value1, String value2) {
            addCriterion("report_year not between", value1, value2, "reportYear");
            return (Criteria) this;
        }

        public Criteria andReportMonthIsNull() {
            addCriterion("report_month is null");
            return (Criteria) this;
        }

        public Criteria andReportMonthIsNotNull() {
            addCriterion("report_month is not null");
            return (Criteria) this;
        }

        public Criteria andReportMonthEqualTo(String value) {
            addCriterion("report_month =", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthNotEqualTo(String value) {
            addCriterion("report_month <>", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthGreaterThan(String value) {
            addCriterion("report_month >", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthGreaterThanOrEqualTo(String value) {
            addCriterion("report_month >=", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthLessThan(String value) {
            addCriterion("report_month <", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthLessThanOrEqualTo(String value) {
            addCriterion("report_month <=", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthLike(String value) {
            addCriterion("report_month like", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthNotLike(String value) {
            addCriterion("report_month not like", value, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthIn(List<String> values) {
            addCriterion("report_month in", values, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthNotIn(List<String> values) {
            addCriterion("report_month not in", values, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthBetween(String value1, String value2) {
            addCriterion("report_month between", value1, value2, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportMonthNotBetween(String value1, String value2) {
            addCriterion("report_month not between", value1, value2, "reportMonth");
            return (Criteria) this;
        }

        public Criteria andReportWeekIsNull() {
            addCriterion("report_week is null");
            return (Criteria) this;
        }

        public Criteria andReportWeekIsNotNull() {
            addCriterion("report_week is not null");
            return (Criteria) this;
        }

        public Criteria andReportWeekEqualTo(String value) {
            addCriterion("report_week =", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekNotEqualTo(String value) {
            addCriterion("report_week <>", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekGreaterThan(String value) {
            addCriterion("report_week >", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekGreaterThanOrEqualTo(String value) {
            addCriterion("report_week >=", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekLessThan(String value) {
            addCriterion("report_week <", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekLessThanOrEqualTo(String value) {
            addCriterion("report_week <=", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekLike(String value) {
            addCriterion("report_week like", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekNotLike(String value) {
            addCriterion("report_week not like", value, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekIn(List<String> values) {
            addCriterion("report_week in", values, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekNotIn(List<String> values) {
            addCriterion("report_week not in", values, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekBetween(String value1, String value2) {
            addCriterion("report_week between", value1, value2, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportWeekNotBetween(String value1, String value2) {
            addCriterion("report_week not between", value1, value2, "reportWeek");
            return (Criteria) this;
        }

        public Criteria andReportDayIsNull() {
            addCriterion("report_day is null");
            return (Criteria) this;
        }

        public Criteria andReportDayIsNotNull() {
            addCriterion("report_day is not null");
            return (Criteria) this;
        }

        public Criteria andReportDayEqualTo(String value) {
            addCriterion("report_day =", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayNotEqualTo(String value) {
            addCriterion("report_day <>", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayGreaterThan(String value) {
            addCriterion("report_day >", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayGreaterThanOrEqualTo(String value) {
            addCriterion("report_day >=", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayLessThan(String value) {
            addCriterion("report_day <", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayLessThanOrEqualTo(String value) {
            addCriterion("report_day <=", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayLike(String value) {
            addCriterion("report_day like", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayNotLike(String value) {
            addCriterion("report_day not like", value, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayIn(List<String> values) {
            addCriterion("report_day in", values, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayNotIn(List<String> values) {
            addCriterion("report_day not in", values, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayBetween(String value1, String value2) {
            addCriterion("report_day between", value1, value2, "reportDay");
            return (Criteria) this;
        }

        public Criteria andReportDayNotBetween(String value1, String value2) {
            addCriterion("report_day not between", value1, value2, "reportDay");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorIsNull() {
            addCriterion("create_opertor is null");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorIsNotNull() {
            addCriterion("create_opertor is not null");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorEqualTo(String value) {
            addCriterion("create_opertor =", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorNotEqualTo(String value) {
            addCriterion("create_opertor <>", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorGreaterThan(String value) {
            addCriterion("create_opertor >", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorGreaterThanOrEqualTo(String value) {
            addCriterion("create_opertor >=", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorLessThan(String value) {
            addCriterion("create_opertor <", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorLessThanOrEqualTo(String value) {
            addCriterion("create_opertor <=", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorLike(String value) {
            addCriterion("create_opertor like", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorNotLike(String value) {
            addCriterion("create_opertor not like", value, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorIn(List<String> values) {
            addCriterion("create_opertor in", values, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorNotIn(List<String> values) {
            addCriterion("create_opertor not in", values, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorBetween(String value1, String value2) {
            addCriterion("create_opertor between", value1, value2, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateOpertorNotBetween(String value1, String value2) {
            addCriterion("create_opertor not between", value1, value2, "createOpertor");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
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