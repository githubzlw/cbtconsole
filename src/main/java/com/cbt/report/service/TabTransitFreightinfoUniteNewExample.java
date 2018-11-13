package com.cbt.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TabTransitFreightinfoUniteNewExample {
	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public TabTransitFreightinfoUniteNewExample() {
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

		public Criteria andTransportModeIsNull() {
			addCriterion("transport_mode is null");
			return (Criteria) this;
		}

		public Criteria andTransportModeIsNotNull() {
			addCriterion("transport_mode is not null");
			return (Criteria) this;
		}

		public Criteria andTransportModeEqualTo(String value) {
			addCriterion("transport_mode =", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeNotEqualTo(String value) {
			addCriterion("transport_mode <>", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeGreaterThan(String value) {
			addCriterion("transport_mode >", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeGreaterThanOrEqualTo(String value) {
			addCriterion("transport_mode >=", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeLessThan(String value) {
			addCriterion("transport_mode <", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeLessThanOrEqualTo(String value) {
			addCriterion("transport_mode <=", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeLike(String value) {
			addCriterion("transport_mode like", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeNotLike(String value) {
			addCriterion("transport_mode not like", value, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeIn(List<String> values) {
			addCriterion("transport_mode in", values, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeNotIn(List<String> values) {
			addCriterion("transport_mode not in", values, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeBetween(String value1, String value2) {
			addCriterion("transport_mode between", value1, value2, "transportMode");
			return (Criteria) this;
		}

		public Criteria andTransportModeNotBetween(String value1, String value2) {
			addCriterion("transport_mode not between", value1, value2, "transportMode");
			return (Criteria) this;
		}

		public Criteria andShippingTimeIsNull() {
			addCriterion("shipping_time is null");
			return (Criteria) this;
		}

		public Criteria andShippingTimeIsNotNull() {
			addCriterion("shipping_time is not null");
			return (Criteria) this;
		}

		public Criteria andShippingTimeEqualTo(String value) {
			addCriterion("shipping_time =", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeNotEqualTo(String value) {
			addCriterion("shipping_time <>", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeGreaterThan(String value) {
			addCriterion("shipping_time >", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeGreaterThanOrEqualTo(String value) {
			addCriterion("shipping_time >=", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeLessThan(String value) {
			addCriterion("shipping_time <", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeLessThanOrEqualTo(String value) {
			addCriterion("shipping_time <=", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeLike(String value) {
			addCriterion("shipping_time like", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeNotLike(String value) {
			addCriterion("shipping_time not like", value, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeIn(List<String> values) {
			addCriterion("shipping_time in", values, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeNotIn(List<String> values) {
			addCriterion("shipping_time not in", values, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeBetween(String value1, String value2) {
			addCriterion("shipping_time between", value1, value2, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andShippingTimeNotBetween(String value1, String value2) {
			addCriterion("shipping_time not between", value1, value2, "shippingTime");
			return (Criteria) this;
		}

		public Criteria andCountryidIsNull() {
			addCriterion("countryid is null");
			return (Criteria) this;
		}

		public Criteria andCountryidIsNotNull() {
			addCriterion("countryid is not null");
			return (Criteria) this;
		}

		public Criteria andCountryidEqualTo(Integer value) {
			addCriterion("countryid =", value, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidNotEqualTo(Integer value) {
			addCriterion("countryid <>", value, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidGreaterThan(Integer value) {
			addCriterion("countryid >", value, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidGreaterThanOrEqualTo(Integer value) {
			addCriterion("countryid >=", value, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidLessThan(Integer value) {
			addCriterion("countryid <", value, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidLessThanOrEqualTo(Integer value) {
			addCriterion("countryid <=", value, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidIn(List<Integer> values) {
			addCriterion("countryid in", values, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidNotIn(List<Integer> values) {
			addCriterion("countryid not in", values, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidBetween(Integer value1, Integer value2) {
			addCriterion("countryid between", value1, value2, "countryid");
			return (Criteria) this;
		}

		public Criteria andCountryidNotBetween(Integer value1, Integer value2) {
			addCriterion("countryid not between", value1, value2, "countryid");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightIsNull() {
			addCriterion("normal_base_weight is null");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightIsNotNull() {
			addCriterion("normal_base_weight is not null");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightEqualTo(Double value) {
			addCriterion("normal_base_weight =", value, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightNotEqualTo(Double value) {
			addCriterion("normal_base_weight <>", value, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightGreaterThan(Double value) {
			addCriterion("normal_base_weight >", value, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightGreaterThanOrEqualTo(Double value) {
			addCriterion("normal_base_weight >=", value, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightLessThan(Double value) {
			addCriterion("normal_base_weight <", value, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightLessThanOrEqualTo(Double value) {
			addCriterion("normal_base_weight <=", value, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightIn(List<Double> values) {
			addCriterion("normal_base_weight in", values, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightNotIn(List<Double> values) {
			addCriterion("normal_base_weight not in", values, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightBetween(Double value1, Double value2) {
			addCriterion("normal_base_weight between", value1, value2, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBaseWeightNotBetween(Double value1, Double value2) {
			addCriterion("normal_base_weight not between", value1, value2, "normalBaseWeight");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceIsNull() {
			addCriterion("normal_base_price is null");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceIsNotNull() {
			addCriterion("normal_base_price is not null");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceEqualTo(BigDecimal value) {
			addCriterion("normal_base_price =", value, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceNotEqualTo(BigDecimal value) {
			addCriterion("normal_base_price <>", value, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceGreaterThan(BigDecimal value) {
			addCriterion("normal_base_price >", value, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("normal_base_price >=", value, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceLessThan(BigDecimal value) {
			addCriterion("normal_base_price <", value, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("normal_base_price <=", value, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceIn(List<BigDecimal> values) {
			addCriterion("normal_base_price in", values, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceNotIn(List<BigDecimal> values) {
			addCriterion("normal_base_price not in", values, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("normal_base_price between", value1, value2, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalBasePriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("normal_base_price not between", value1, value2, "normalBasePrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceIsNull() {
			addCriterion("normal_ratio_price is null");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceIsNotNull() {
			addCriterion("normal_ratio_price is not null");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceEqualTo(BigDecimal value) {
			addCriterion("normal_ratio_price =", value, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceNotEqualTo(BigDecimal value) {
			addCriterion("normal_ratio_price <>", value, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceGreaterThan(BigDecimal value) {
			addCriterion("normal_ratio_price >", value, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("normal_ratio_price >=", value, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceLessThan(BigDecimal value) {
			addCriterion("normal_ratio_price <", value, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("normal_ratio_price <=", value, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceIn(List<BigDecimal> values) {
			addCriterion("normal_ratio_price in", values, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceNotIn(List<BigDecimal> values) {
			addCriterion("normal_ratio_price not in", values, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("normal_ratio_price between", value1, value2, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalRatioPriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("normal_ratio_price not between", value1, value2, "normalRatioPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceIsNull() {
			addCriterion("normal_big_weight_price is null");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceIsNotNull() {
			addCriterion("normal_big_weight_price is not null");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceEqualTo(BigDecimal value) {
			addCriterion("normal_big_weight_price =", value, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceNotEqualTo(BigDecimal value) {
			addCriterion("normal_big_weight_price <>", value, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceGreaterThan(BigDecimal value) {
			addCriterion("normal_big_weight_price >", value, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("normal_big_weight_price >=", value, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceLessThan(BigDecimal value) {
			addCriterion("normal_big_weight_price <", value, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("normal_big_weight_price <=", value, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceIn(List<BigDecimal> values) {
			addCriterion("normal_big_weight_price in", values, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceNotIn(List<BigDecimal> values) {
			addCriterion("normal_big_weight_price not in", values, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("normal_big_weight_price between", value1, value2, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andNormalBigWeightPriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("normal_big_weight_price not between", value1, value2, "normalBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightIsNull() {
			addCriterion("special_base_weight is null");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightIsNotNull() {
			addCriterion("special_base_weight is not null");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightEqualTo(Double value) {
			addCriterion("special_base_weight =", value, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightNotEqualTo(Double value) {
			addCriterion("special_base_weight <>", value, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightGreaterThan(Double value) {
			addCriterion("special_base_weight >", value, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightGreaterThanOrEqualTo(Double value) {
			addCriterion("special_base_weight >=", value, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightLessThan(Double value) {
			addCriterion("special_base_weight <", value, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightLessThanOrEqualTo(Double value) {
			addCriterion("special_base_weight <=", value, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightIn(List<Double> values) {
			addCriterion("special_base_weight in", values, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightNotIn(List<Double> values) {
			addCriterion("special_base_weight not in", values, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightBetween(Double value1, Double value2) {
			addCriterion("special_base_weight between", value1, value2, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBaseWeightNotBetween(Double value1, Double value2) {
			addCriterion("special_base_weight not between", value1, value2, "specialBaseWeight");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceIsNull() {
			addCriterion("special_base_price is null");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceIsNotNull() {
			addCriterion("special_base_price is not null");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceEqualTo(BigDecimal value) {
			addCriterion("special_base_price =", value, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceNotEqualTo(BigDecimal value) {
			addCriterion("special_base_price <>", value, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceGreaterThan(BigDecimal value) {
			addCriterion("special_base_price >", value, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("special_base_price >=", value, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceLessThan(BigDecimal value) {
			addCriterion("special_base_price <", value, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("special_base_price <=", value, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceIn(List<BigDecimal> values) {
			addCriterion("special_base_price in", values, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceNotIn(List<BigDecimal> values) {
			addCriterion("special_base_price not in", values, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("special_base_price between", value1, value2, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBasePriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("special_base_price not between", value1, value2, "specialBasePrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceIsNull() {
			addCriterion("special_ratio_price is null");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceIsNotNull() {
			addCriterion("special_ratio_price is not null");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceEqualTo(BigDecimal value) {
			addCriterion("special_ratio_price =", value, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceNotEqualTo(BigDecimal value) {
			addCriterion("special_ratio_price <>", value, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceGreaterThan(BigDecimal value) {
			addCriterion("special_ratio_price >", value, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("special_ratio_price >=", value, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceLessThan(BigDecimal value) {
			addCriterion("special_ratio_price <", value, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("special_ratio_price <=", value, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceIn(List<BigDecimal> values) {
			addCriterion("special_ratio_price in", values, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceNotIn(List<BigDecimal> values) {
			addCriterion("special_ratio_price not in", values, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("special_ratio_price between", value1, value2, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialRatioPriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("special_ratio_price not between", value1, value2, "specialRatioPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceIsNull() {
			addCriterion("special_big_weight_price is null");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceIsNotNull() {
			addCriterion("special_big_weight_price is not null");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceEqualTo(BigDecimal value) {
			addCriterion("special_big_weight_price =", value, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceNotEqualTo(BigDecimal value) {
			addCriterion("special_big_weight_price <>", value, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceGreaterThan(BigDecimal value) {
			addCriterion("special_big_weight_price >", value, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("special_big_weight_price >=", value, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceLessThan(BigDecimal value) {
			addCriterion("special_big_weight_price <", value, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("special_big_weight_price <=", value, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceIn(List<BigDecimal> values) {
			addCriterion("special_big_weight_price in", values, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceNotIn(List<BigDecimal> values) {
			addCriterion("special_big_weight_price not in", values, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("special_big_weight_price between", value1, value2, "specialBigWeightPrice");
			return (Criteria) this;
		}

		public Criteria andSpecialBigWeightPriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("special_big_weight_price not between", value1, value2, "specialBigWeightPrice");
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

		public Criteria andSplitIsNull() {
			addCriterion("split is null");
			return (Criteria) this;
		}

		public Criteria andSplitIsNotNull() {
			addCriterion("split is not null");
			return (Criteria) this;
		}

		public Criteria andSplitEqualTo(Integer value) {
			addCriterion("split =", value, "split");
			return (Criteria) this;
		}

		public Criteria andSplitNotEqualTo(Integer value) {
			addCriterion("split <>", value, "split");
			return (Criteria) this;
		}

		public Criteria andSplitGreaterThan(Integer value) {
			addCriterion("split >", value, "split");
			return (Criteria) this;
		}

		public Criteria andSplitGreaterThanOrEqualTo(Integer value) {
			addCriterion("split >=", value, "split");
			return (Criteria) this;
		}

		public Criteria andSplitLessThan(Integer value) {
			addCriterion("split <", value, "split");
			return (Criteria) this;
		}

		public Criteria andSplitLessThanOrEqualTo(Integer value) {
			addCriterion("split <=", value, "split");
			return (Criteria) this;
		}

		public Criteria andSplitIn(List<Integer> values) {
			addCriterion("split in", values, "split");
			return (Criteria) this;
		}

		public Criteria andSplitNotIn(List<Integer> values) {
			addCriterion("split not in", values, "split");
			return (Criteria) this;
		}

		public Criteria andSplitBetween(Integer value1, Integer value2) {
			addCriterion("split between", value1, value2, "split");
			return (Criteria) this;
		}

		public Criteria andSplitNotBetween(Integer value1, Integer value2) {
			addCriterion("split not between", value1, value2, "split");
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
