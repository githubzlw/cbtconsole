package com.cbt.pojo;

import java.util.ArrayList;
import java.util.List;

public class ReportInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReportInfoExample() {
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

        public Criteria andTotalExpenditureIsNull() {
            addCriterion("total_expenditure is null");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureIsNotNull() {
            addCriterion("total_expenditure is not null");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureEqualTo(Double value) {
            addCriterion("total_expenditure =", value, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureNotEqualTo(Double value) {
            addCriterion("total_expenditure <>", value, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureGreaterThan(Double value) {
            addCriterion("total_expenditure >", value, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureGreaterThanOrEqualTo(Double value) {
            addCriterion("total_expenditure >=", value, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureLessThan(Double value) {
            addCriterion("total_expenditure <", value, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureLessThanOrEqualTo(Double value) {
            addCriterion("total_expenditure <=", value, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureIn(List<Double> values) {
            addCriterion("total_expenditure in", values, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureNotIn(List<Double> values) {
            addCriterion("total_expenditure not in", values, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureBetween(Double value1, Double value2) {
            addCriterion("total_expenditure between", value1, value2, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalExpenditureNotBetween(Double value1, Double value2) {
            addCriterion("total_expenditure not between", value1, value2, "totalExpenditure");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueIsNull() {
            addCriterion("total_revenue is null");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueIsNotNull() {
            addCriterion("total_revenue is not null");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueEqualTo(Double value) {
            addCriterion("total_revenue =", value, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueNotEqualTo(Double value) {
            addCriterion("total_revenue <>", value, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueGreaterThan(Double value) {
            addCriterion("total_revenue >", value, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueGreaterThanOrEqualTo(Double value) {
            addCriterion("total_revenue >=", value, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueLessThan(Double value) {
            addCriterion("total_revenue <", value, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueLessThanOrEqualTo(Double value) {
            addCriterion("total_revenue <=", value, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueIn(List<Double> values) {
            addCriterion("total_revenue in", values, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueNotIn(List<Double> values) {
            addCriterion("total_revenue not in", values, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueBetween(Double value1, Double value2) {
            addCriterion("total_revenue between", value1, value2, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andTotalRevenueNotBetween(Double value1, Double value2) {
            addCriterion("total_revenue not between", value1, value2, "totalRevenue");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIsNull() {
            addCriterion("goods_count is null");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIsNotNull() {
            addCriterion("goods_count is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsCountEqualTo(Integer value) {
            addCriterion("goods_count =", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotEqualTo(Integer value) {
            addCriterion("goods_count <>", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountGreaterThan(Integer value) {
            addCriterion("goods_count >", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_count >=", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountLessThan(Integer value) {
            addCriterion("goods_count <", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountLessThanOrEqualTo(Integer value) {
            addCriterion("goods_count <=", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIn(List<Integer> values) {
            addCriterion("goods_count in", values, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotIn(List<Integer> values) {
            addCriterion("goods_count not in", values, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountBetween(Integer value1, Integer value2) {
            addCriterion("goods_count between", value1, value2, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_count not between", value1, value2, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andAveragePriceIsNull() {
            addCriterion("average_price is null");
            return (Criteria) this;
        }

        public Criteria andAveragePriceIsNotNull() {
            addCriterion("average_price is not null");
            return (Criteria) this;
        }

        public Criteria andAveragePriceEqualTo(Double value) {
            addCriterion("average_price =", value, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceNotEqualTo(Double value) {
            addCriterion("average_price <>", value, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceGreaterThan(Double value) {
            addCriterion("average_price >", value, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceGreaterThanOrEqualTo(Double value) {
            addCriterion("average_price >=", value, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceLessThan(Double value) {
            addCriterion("average_price <", value, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceLessThanOrEqualTo(Double value) {
            addCriterion("average_price <=", value, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceIn(List<Double> values) {
            addCriterion("average_price in", values, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceNotIn(List<Double> values) {
            addCriterion("average_price not in", values, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceBetween(Double value1, Double value2) {
            addCriterion("average_price between", value1, value2, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andAveragePriceNotBetween(Double value1, Double value2) {
            addCriterion("average_price not between", value1, value2, "averagePrice");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumIsNull() {
            addCriterion("invalid_purchase_num is null");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumIsNotNull() {
            addCriterion("invalid_purchase_num is not null");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumEqualTo(Integer value) {
            addCriterion("invalid_purchase_num =", value, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumNotEqualTo(Integer value) {
            addCriterion("invalid_purchase_num <>", value, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumGreaterThan(Integer value) {
            addCriterion("invalid_purchase_num >", value, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("invalid_purchase_num >=", value, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumLessThan(Integer value) {
            addCriterion("invalid_purchase_num <", value, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumLessThanOrEqualTo(Integer value) {
            addCriterion("invalid_purchase_num <=", value, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumIn(List<Integer> values) {
            addCriterion("invalid_purchase_num in", values, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumNotIn(List<Integer> values) {
            addCriterion("invalid_purchase_num not in", values, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumBetween(Integer value1, Integer value2) {
            addCriterion("invalid_purchase_num between", value1, value2, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andInvalidPurchaseNumNotBetween(Integer value1, Integer value2) {
            addCriterion("invalid_purchase_num not between", value1, value2, "invalidPurchaseNum");
            return (Criteria) this;
        }

        public Criteria andProfitLossIsNull() {
            addCriterion("profit_loss is null");
            return (Criteria) this;
        }

        public Criteria andProfitLossIsNotNull() {
            addCriterion("profit_loss is not null");
            return (Criteria) this;
        }

        public Criteria andProfitLossEqualTo(Double value) {
            addCriterion("profit_loss =", value, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossNotEqualTo(Double value) {
            addCriterion("profit_loss <>", value, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossGreaterThan(Double value) {
            addCriterion("profit_loss >", value, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossGreaterThanOrEqualTo(Double value) {
            addCriterion("profit_loss >=", value, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossLessThan(Double value) {
            addCriterion("profit_loss <", value, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossLessThanOrEqualTo(Double value) {
            addCriterion("profit_loss <=", value, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossIn(List<Double> values) {
            addCriterion("profit_loss in", values, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossNotIn(List<Double> values) {
            addCriterion("profit_loss not in", values, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossBetween(Double value1, Double value2) {
            addCriterion("profit_loss between", value1, value2, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andProfitLossNotBetween(Double value1, Double value2) {
            addCriterion("profit_loss not between", value1, value2, "profitLoss");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdIsNull() {
            addCriterion("genral_report_id is null");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdIsNotNull() {
            addCriterion("genral_report_id is not null");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdEqualTo(Integer value) {
            addCriterion("genral_report_id =", value, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdNotEqualTo(Integer value) {
            addCriterion("genral_report_id <>", value, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdGreaterThan(Integer value) {
            addCriterion("genral_report_id >", value, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("genral_report_id >=", value, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdLessThan(Integer value) {
            addCriterion("genral_report_id <", value, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdLessThanOrEqualTo(Integer value) {
            addCriterion("genral_report_id <=", value, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdIn(List<Integer> values) {
            addCriterion("genral_report_id in", values, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdNotIn(List<Integer> values) {
            addCriterion("genral_report_id not in", values, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdBetween(Integer value1, Integer value2) {
            addCriterion("genral_report_id between", value1, value2, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andGenralReportIdNotBetween(Integer value1, Integer value2) {
            addCriterion("genral_report_id not between", value1, value2, "genralReportId");
            return (Criteria) this;
        }

        public Criteria andCategroyNumIsNull() {
            addCriterion("categroy_num is null");
            return (Criteria) this;
        }

        public Criteria andCategroyNumIsNotNull() {
            addCriterion("categroy_num is not null");
            return (Criteria) this;
        }

        public Criteria andCategroyNumEqualTo(Integer value) {
            addCriterion("categroy_num =", value, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumNotEqualTo(Integer value) {
            addCriterion("categroy_num <>", value, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumGreaterThan(Integer value) {
            addCriterion("categroy_num >", value, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("categroy_num >=", value, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumLessThan(Integer value) {
            addCriterion("categroy_num <", value, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumLessThanOrEqualTo(Integer value) {
            addCriterion("categroy_num <=", value, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumIn(List<Integer> values) {
            addCriterion("categroy_num in", values, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumNotIn(List<Integer> values) {
            addCriterion("categroy_num not in", values, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumBetween(Integer value1, Integer value2) {
            addCriterion("categroy_num between", value1, value2, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andCategroyNumNotBetween(Integer value1, Integer value2) {
            addCriterion("categroy_num not between", value1, value2, "categroyNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumIsNull() {
            addCriterion("order_num is null");
            return (Criteria) this;
        }

        public Criteria andOrderNumIsNotNull() {
            addCriterion("order_num is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNumEqualTo(Integer value) {
            addCriterion("order_num =", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotEqualTo(Integer value) {
            addCriterion("order_num <>", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThan(Integer value) {
            addCriterion("order_num >", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_num >=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThan(Integer value) {
            addCriterion("order_num <", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThanOrEqualTo(Integer value) {
            addCriterion("order_num <=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumIn(List<Integer> values) {
            addCriterion("order_num in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotIn(List<Integer> values) {
            addCriterion("order_num not in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumBetween(Integer value1, Integer value2) {
            addCriterion("order_num between", value1, value2, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("order_num not between", value1, value2, "orderNum");
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