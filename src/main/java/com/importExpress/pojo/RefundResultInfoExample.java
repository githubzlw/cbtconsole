package com.importExpress.pojo;

import java.util.ArrayList;
import java.util.List;

public class RefundResultInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RefundResultInfoExample() {
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

        public Criteria andUseridIsNull() {
            addCriterion("userId is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userId is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(String value) {
            addCriterion("userId =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(String value) {
            addCriterion("userId <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(String value) {
            addCriterion("userId >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(String value) {
            addCriterion("userId >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(String value) {
            addCriterion("userId <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(String value) {
            addCriterion("userId <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLike(String value) {
            addCriterion("userId like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotLike(String value) {
            addCriterion("userId not like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<String> values) {
            addCriterion("userId in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<String> values) {
            addCriterion("userId not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(String value1, String value2) {
            addCriterion("userId between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(String value1, String value2) {
            addCriterion("userId not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andPaypriceIsNull() {
            addCriterion("payprice is null");
            return (Criteria) this;
        }

        public Criteria andPaypriceIsNotNull() {
            addCriterion("payprice is not null");
            return (Criteria) this;
        }

        public Criteria andPaypriceEqualTo(String value) {
            addCriterion("payprice =", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceNotEqualTo(String value) {
            addCriterion("payprice <>", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceGreaterThan(String value) {
            addCriterion("payprice >", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceGreaterThanOrEqualTo(String value) {
            addCriterion("payprice >=", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceLessThan(String value) {
            addCriterion("payprice <", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceLessThanOrEqualTo(String value) {
            addCriterion("payprice <=", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceLike(String value) {
            addCriterion("payprice like", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceNotLike(String value) {
            addCriterion("payprice not like", value, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceIn(List<String> values) {
            addCriterion("payprice in", values, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceNotIn(List<String> values) {
            addCriterion("payprice not in", values, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceBetween(String value1, String value2) {
            addCriterion("payprice between", value1, value2, "payprice");
            return (Criteria) this;
        }

        public Criteria andPaypriceNotBetween(String value1, String value2) {
            addCriterion("payprice not between", value1, value2, "payprice");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeIsNull() {
            addCriterion("refund_from_transaction_fee is null");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeIsNotNull() {
            addCriterion("refund_from_transaction_fee is not null");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeEqualTo(String value) {
            addCriterion("refund_from_transaction_fee =", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeNotEqualTo(String value) {
            addCriterion("refund_from_transaction_fee <>", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeGreaterThan(String value) {
            addCriterion("refund_from_transaction_fee >", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeGreaterThanOrEqualTo(String value) {
            addCriterion("refund_from_transaction_fee >=", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeLessThan(String value) {
            addCriterion("refund_from_transaction_fee <", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeLessThanOrEqualTo(String value) {
            addCriterion("refund_from_transaction_fee <=", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeLike(String value) {
            addCriterion("refund_from_transaction_fee like", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeNotLike(String value) {
            addCriterion("refund_from_transaction_fee not like", value, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeIn(List<String> values) {
            addCriterion("refund_from_transaction_fee in", values, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeNotIn(List<String> values) {
            addCriterion("refund_from_transaction_fee not in", values, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeBetween(String value1, String value2) {
            addCriterion("refund_from_transaction_fee between", value1, value2, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromTransactionFeeNotBetween(String value1, String value2) {
            addCriterion("refund_from_transaction_fee not between", value1, value2, "refundFromTransactionFee");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountIsNull() {
            addCriterion("refund_from_received_amount is null");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountIsNotNull() {
            addCriterion("refund_from_received_amount is not null");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountEqualTo(String value) {
            addCriterion("refund_from_received_amount =", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountNotEqualTo(String value) {
            addCriterion("refund_from_received_amount <>", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountGreaterThan(String value) {
            addCriterion("refund_from_received_amount >", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountGreaterThanOrEqualTo(String value) {
            addCriterion("refund_from_received_amount >=", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountLessThan(String value) {
            addCriterion("refund_from_received_amount <", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountLessThanOrEqualTo(String value) {
            addCriterion("refund_from_received_amount <=", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountLike(String value) {
            addCriterion("refund_from_received_amount like", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountNotLike(String value) {
            addCriterion("refund_from_received_amount not like", value, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountIn(List<String> values) {
            addCriterion("refund_from_received_amount in", values, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountNotIn(List<String> values) {
            addCriterion("refund_from_received_amount not in", values, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountBetween(String value1, String value2) {
            addCriterion("refund_from_received_amount between", value1, value2, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundFromReceivedAmountNotBetween(String value1, String value2) {
            addCriterion("refund_from_received_amount not between", value1, value2, "refundFromReceivedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountIsNull() {
            addCriterion("total_refunded_amount is null");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountIsNotNull() {
            addCriterion("total_refunded_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountEqualTo(String value) {
            addCriterion("total_refunded_amount =", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountNotEqualTo(String value) {
            addCriterion("total_refunded_amount <>", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountGreaterThan(String value) {
            addCriterion("total_refunded_amount >", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountGreaterThanOrEqualTo(String value) {
            addCriterion("total_refunded_amount >=", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountLessThan(String value) {
            addCriterion("total_refunded_amount <", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountLessThanOrEqualTo(String value) {
            addCriterion("total_refunded_amount <=", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountLike(String value) {
            addCriterion("total_refunded_amount like", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountNotLike(String value) {
            addCriterion("total_refunded_amount not like", value, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountIn(List<String> values) {
            addCriterion("total_refunded_amount in", values, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountNotIn(List<String> values) {
            addCriterion("total_refunded_amount not in", values, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountBetween(String value1, String value2) {
            addCriterion("total_refunded_amount between", value1, value2, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andTotalRefundedAmountNotBetween(String value1, String value2) {
            addCriterion("total_refunded_amount not between", value1, value2, "totalRefundedAmount");
            return (Criteria) this;
        }

        public Criteria andRefundidIsNull() {
            addCriterion("refundid is null");
            return (Criteria) this;
        }

        public Criteria andRefundidIsNotNull() {
            addCriterion("refundid is not null");
            return (Criteria) this;
        }

        public Criteria andRefundidEqualTo(String value) {
            addCriterion("refundid =", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidNotEqualTo(String value) {
            addCriterion("refundid <>", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidGreaterThan(String value) {
            addCriterion("refundid >", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidGreaterThanOrEqualTo(String value) {
            addCriterion("refundid >=", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidLessThan(String value) {
            addCriterion("refundid <", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidLessThanOrEqualTo(String value) {
            addCriterion("refundid <=", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidLike(String value) {
            addCriterion("refundid like", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidNotLike(String value) {
            addCriterion("refundid not like", value, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidIn(List<String> values) {
            addCriterion("refundid in", values, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidNotIn(List<String> values) {
            addCriterion("refundid not in", values, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidBetween(String value1, String value2) {
            addCriterion("refundid between", value1, value2, "refundid");
            return (Criteria) this;
        }

        public Criteria andRefundidNotBetween(String value1, String value2) {
            addCriterion("refundid not between", value1, value2, "refundid");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIsNull() {
            addCriterion("amount_total is null");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIsNotNull() {
            addCriterion("amount_total is not null");
            return (Criteria) this;
        }

        public Criteria andAmountTotalEqualTo(String value) {
            addCriterion("amount_total =", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotEqualTo(String value) {
            addCriterion("amount_total <>", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalGreaterThan(String value) {
            addCriterion("amount_total >", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalGreaterThanOrEqualTo(String value) {
            addCriterion("amount_total >=", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalLessThan(String value) {
            addCriterion("amount_total <", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalLessThanOrEqualTo(String value) {
            addCriterion("amount_total <=", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalLike(String value) {
            addCriterion("amount_total like", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotLike(String value) {
            addCriterion("amount_total not like", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIn(List<String> values) {
            addCriterion("amount_total in", values, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotIn(List<String> values) {
            addCriterion("amount_total not in", values, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalBetween(String value1, String value2) {
            addCriterion("amount_total between", value1, value2, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotBetween(String value1, String value2) {
            addCriterion("amount_total not between", value1, value2, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andSaleIdIsNull() {
            addCriterion("sale_id is null");
            return (Criteria) this;
        }

        public Criteria andSaleIdIsNotNull() {
            addCriterion("sale_id is not null");
            return (Criteria) this;
        }

        public Criteria andSaleIdEqualTo(String value) {
            addCriterion("sale_id =", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdNotEqualTo(String value) {
            addCriterion("sale_id <>", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdGreaterThan(String value) {
            addCriterion("sale_id >", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdGreaterThanOrEqualTo(String value) {
            addCriterion("sale_id >=", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdLessThan(String value) {
            addCriterion("sale_id <", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdLessThanOrEqualTo(String value) {
            addCriterion("sale_id <=", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdLike(String value) {
            addCriterion("sale_id like", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdNotLike(String value) {
            addCriterion("sale_id not like", value, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdIn(List<String> values) {
            addCriterion("sale_id in", values, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdNotIn(List<String> values) {
            addCriterion("sale_id not in", values, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdBetween(String value1, String value2) {
            addCriterion("sale_id between", value1, value2, "saleId");
            return (Criteria) this;
        }

        public Criteria andSaleIdNotBetween(String value1, String value2) {
            addCriterion("sale_id not between", value1, value2, "saleId");
            return (Criteria) this;
        }

        public Criteria andParentPaymentIsNull() {
            addCriterion("parent_payment is null");
            return (Criteria) this;
        }

        public Criteria andParentPaymentIsNotNull() {
            addCriterion("parent_payment is not null");
            return (Criteria) this;
        }

        public Criteria andParentPaymentEqualTo(String value) {
            addCriterion("parent_payment =", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentNotEqualTo(String value) {
            addCriterion("parent_payment <>", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentGreaterThan(String value) {
            addCriterion("parent_payment >", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentGreaterThanOrEqualTo(String value) {
            addCriterion("parent_payment >=", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentLessThan(String value) {
            addCriterion("parent_payment <", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentLessThanOrEqualTo(String value) {
            addCriterion("parent_payment <=", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentLike(String value) {
            addCriterion("parent_payment like", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentNotLike(String value) {
            addCriterion("parent_payment not like", value, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentIn(List<String> values) {
            addCriterion("parent_payment in", values, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentNotIn(List<String> values) {
            addCriterion("parent_payment not in", values, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentBetween(String value1, String value2) {
            addCriterion("parent_payment between", value1, value2, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andParentPaymentNotBetween(String value1, String value2) {
            addCriterion("parent_payment not between", value1, value2, "parentPayment");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(String value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(String value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(String value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(String value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(String value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLike(String value) {
            addCriterion("create_time like", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotLike(String value) {
            addCriterion("create_time not like", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<String> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<String> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(String value1, String value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(String value1, String value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(String value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(String value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(String value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(String value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(String value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLike(String value) {
            addCriterion("update_time like", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotLike(String value) {
            addCriterion("update_time not like", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<String> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<String> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(String value1, String value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(String value1, String value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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