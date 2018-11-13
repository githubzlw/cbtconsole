package com.cbt.paypal;


import com.braintreegateway.*;
import com.cbt.paypal.util.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.Map;

public class BtTransactionHelper {
    private static final Log LOG = LogFactory.getLog("pay");
    // Braintree Gateway via PayPal Access Token
    protected BraintreeGateway gateway = new BraintreeGateway(PropertyUtils.getValueFromPaypelFile("sandbox".equals(PropertyUtils.getValueFromPaypelFile("paypal_type")) ? "SANDBOX_ACCESS_TOKEN" : "LIVE_ACCESS_TOKEN"));
    private boolean success;
    private Transaction transaction;
    private Result<Transaction> result;

    public String getToken() {
        return gateway.clientToken().generate();
    }

    // "Classic" EC flow
    public void doTransactionEc(BtParameterVo vo, String orderid, String description, Map<String, String> failure) {

        String paymentNonce = vo.getd("payment-method-nonce");
        String amount = vo.getd("total-amount");
        String itemDescription = vo.getd("item-description");
        LOG.warn("================开始验证付款信息===================");
        LOG.warn("  paymentNonce: " + paymentNonce);
        LOG.warn("  amount: " + amount);
        LOG.warn("  description: " + description);
        failure.put("itemNumber", orderid);
        failure.put("per_payprice", amount);
        failure.put("payment_method_nonce", paymentNonce);
        try {
            // transaction
            TransactionRequest transactionRequest = new TransactionRequest()
                    .channel(PropertyUtils.getValueFromPaypelFile("SBN_CODES"))
                    .amount(new BigDecimal(amount))
                    .paymentMethodNonce(paymentNonce)
                    .orderId(orderid)
                    .options()
                    .paypal()
                    .customField("paypal支付")
                    .description(orderid)
                    .done()
                    .submitForSettlement(true)
                    .done();

            result = gateway.transaction().sale(transactionRequest);
//		LOG.warn("result==="+result);
        } catch (NullPointerException ex) {
            failure.put("message", "ERROR: The transaction contained invalid data.");
            LOG.warn("ERROR: The transaction contained invalid data.");
            return;
        }

        setSuccess(result.isSuccess());

        // true/false
        LOG.warn("是否支付成功: " + result.isSuccess());

        if (result.isSuccess() == false) {
            failure.put("message", result.getMessage());
            LOG.warn("=================【" + orderid + "】支付错误信息: " + result.getMessage());
            setTransaction(result.getTransaction());
//			System.out.println("result.isSuccess(): " + result.getTransaction().toString());
        } else {
            setTransaction(result.getTarget());

            // show PayPal details
//			logPayPalDetails(result);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean result) {
        this.success = result;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Result<Transaction> getResult() {
        return result;
    }

    public void addSuccessParameters(BtParameterVo vo) {

        // use PayPal transaction id if available
        String transId = (transaction.getPaymentInstrumentType().equals("paypal_account")) ?
                transaction.getPayPalDetails().getCaptureId() : // PayPal transaction ID
                transaction.getId();                            // Braintree transaction ID

        vo.add("email", transaction.getStatusHistory().get(0).getUser());
        vo.add("status", "Approved");
        vo.add("transactionid", transId);
    }

    public void failurTransactionMessage() {
        LOG.error("Error processing transaction:");
        LOG.error("  Status: " + transaction.getStatus());
        LOG.error("  Code: " + transaction.getProcessorResponseCode());
        LOG.error("  Text: " + transaction.getProcessorResponseText());
    }

    public String getFailureTransactionMessage() {
        return (
                "<b>Status:</b> " + transaction.getStatus() + "<br /><br />" +
                        "<b>Code: </b>" + transaction.getProcessorResponseCode() + "<br /><br />" +
                        "<b>Text: </b>" + transaction.getProcessorResponseText()
        );

    }

    public void addFailueLog() {
        for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
            LOG.error("Attribute: " + error.getAttribute());
            LOG.error("  Code: " + error.getCode());
            LOG.error("  Message: " + error.getMessage());
        }
    }

    public String getErrorMessage() {
        String message = null;

        for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
            message = error.getMessage();
        }
        return message;
    }

}
