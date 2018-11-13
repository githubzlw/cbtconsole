package com.cbt.pojo;

import java.util.ArrayList;
import java.util.List;

public class ReportDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReportDetailExample() {
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

        public Criteria andCategroyIsNull() {
            addCriterion("categroy is null");
            return (Criteria) this;
        }

        public Criteria andCategroyIsNotNull() {
            addCriterion("categroy is not null");
            return (Criteria) this;
        }

        public Criteria andCategroyEqualTo(String value) {
            addCriterion("categroy =", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyNotEqualTo(String value) {
            addCriterion("categroy <>", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyGreaterThan(String value) {
            addCriterion("categroy >", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyGreaterThanOrEqualTo(String value) {
            addCriterion("categroy >=", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyLessThan(String value) {
            addCriterion("categroy <", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyLessThanOrEqualTo(String value) {
            addCriterion("categroy <=", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyLike(String value) {
            addCriterion("categroy like", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyNotLike(String value) {
            addCriterion("categroy not like", value, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyIn(List<String> values) {
            addCriterion("categroy in", values, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyNotIn(List<String> values) {
            addCriterion("categroy not in", values, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyBetween(String value1, String value2) {
            addCriterion("categroy between", value1, value2, "categroy");
            return (Criteria) this;
        }

        public Criteria andCategroyNotBetween(String value1, String value2) {
            addCriterion("categroy not between", value1, value2, "categroy");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceIsNull() {
            addCriterion("purchase_price is null");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceIsNotNull() {
            addCriterion("purchase_price is not null");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceEqualTo(Double value) {
            addCriterion("purchase_price =", value, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceNotEqualTo(Double value) {
            addCriterion("purchase_price <>", value, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceGreaterThan(Double value) {
            addCriterion("purchase_price >", value, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceGreaterThanOrEqualTo(Double value) {
            addCriterion("purchase_price >=", value, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceLessThan(Double value) {
            addCriterion("purchase_price <", value, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceLessThanOrEqualTo(Double value) {
            addCriterion("purchase_price <=", value, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceIn(List<Double> values) {
            addCriterion("purchase_price in", values, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceNotIn(List<Double> values) {
            addCriterion("purchase_price not in", values, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceBetween(Double value1, Double value2) {
            addCriterion("purchase_price between", value1, value2, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andPurchasePriceNotBetween(Double value1, Double value2) {
            addCriterion("purchase_price not between", value1, value2, "purchasePrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceIsNull() {
            addCriterion("sales_price is null");
            return (Criteria) this;
        }

        public Criteria andSalesPriceIsNotNull() {
            addCriterion("sales_price is not null");
            return (Criteria) this;
        }

        public Criteria andSalesPriceEqualTo(Double value) {
            addCriterion("sales_price =", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceNotEqualTo(Double value) {
            addCriterion("sales_price <>", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceGreaterThan(Double value) {
            addCriterion("sales_price >", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceGreaterThanOrEqualTo(Double value) {
            addCriterion("sales_price >=", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceLessThan(Double value) {
            addCriterion("sales_price <", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceLessThanOrEqualTo(Double value) {
            addCriterion("sales_price <=", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceIn(List<Double> values) {
            addCriterion("sales_price in", values, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceNotIn(List<Double> values) {
            addCriterion("sales_price not in", values, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceBetween(Double value1, Double value2) {
            addCriterion("sales_price between", value1, value2, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceNotBetween(Double value1, Double value2) {
            addCriterion("sales_price not between", value1, value2, "salesPrice");
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

        public Criteria andSalesVolumesIsNull() {
            addCriterion("sales_volumes is null");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesIsNotNull() {
            addCriterion("sales_volumes is not null");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesEqualTo(Integer value) {
            addCriterion("sales_volumes =", value, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesNotEqualTo(Integer value) {
            addCriterion("sales_volumes <>", value, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesGreaterThan(Integer value) {
            addCriterion("sales_volumes >", value, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesGreaterThanOrEqualTo(Integer value) {
            addCriterion("sales_volumes >=", value, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesLessThan(Integer value) {
            addCriterion("sales_volumes <", value, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesLessThanOrEqualTo(Integer value) {
            addCriterion("sales_volumes <=", value, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesIn(List<Integer> values) {
            addCriterion("sales_volumes in", values, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesNotIn(List<Integer> values) {
            addCriterion("sales_volumes not in", values, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesBetween(Integer value1, Integer value2) {
            addCriterion("sales_volumes between", value1, value2, "salesVolumes");
            return (Criteria) this;
        }

        public Criteria andSalesVolumesNotBetween(Integer value1, Integer value2) {
            addCriterion("sales_volumes not between", value1, value2, "salesVolumes");
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

        public Criteria andBuycountIsNull() {
            addCriterion("buycount is null");
            return (Criteria) this;
        }

        public Criteria andBuycountIsNotNull() {
            addCriterion("buycount is not null");
            return (Criteria) this;
        }

        public Criteria andBuycountEqualTo(Integer value) {
            addCriterion("buycount =", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountNotEqualTo(Integer value) {
            addCriterion("buycount <>", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountGreaterThan(Integer value) {
            addCriterion("buycount >", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountGreaterThanOrEqualTo(Integer value) {
            addCriterion("buycount >=", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountLessThan(Integer value) {
            addCriterion("buycount <", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountLessThanOrEqualTo(Integer value) {
            addCriterion("buycount <=", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountIn(List<Integer> values) {
            addCriterion("buycount in", values, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountNotIn(List<Integer> values) {
            addCriterion("buycount not in", values, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountBetween(Integer value1, Integer value2) {
            addCriterion("buycount between", value1, value2, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountNotBetween(Integer value1, Integer value2) {
            addCriterion("buycount not between", value1, value2, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceIsNull() {
            addCriterion("buy_average_price is null");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceIsNotNull() {
            addCriterion("buy_average_price is not null");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceEqualTo(Double value) {
            addCriterion("buy_average_price =", value, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceNotEqualTo(Double value) {
            addCriterion("buy_average_price <>", value, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceGreaterThan(Double value) {
            addCriterion("buy_average_price >", value, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceGreaterThanOrEqualTo(Double value) {
            addCriterion("buy_average_price >=", value, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceLessThan(Double value) {
            addCriterion("buy_average_price <", value, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceLessThanOrEqualTo(Double value) {
            addCriterion("buy_average_price <=", value, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceIn(List<Double> values) {
            addCriterion("buy_average_price in", values, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceNotIn(List<Double> values) {
            addCriterion("buy_average_price not in", values, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceBetween(Double value1, Double value2) {
            addCriterion("buy_average_price between", value1, value2, "buyAveragePrice");
            return (Criteria) this;
        }

        public Criteria andBuyAveragePriceNotBetween(Double value1, Double value2) {
            addCriterion("buy_average_price not between", value1, value2, "buyAveragePrice");
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