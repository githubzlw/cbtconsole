package com.cbt.stripe;

import java.util.Map;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Dispute;
import com.stripe.model.File;
import com.stripe.model.Refund;

public interface StripeService {

    /**
     * do pay
     *
     * @param amount
     * @param orderId
     * @param receiptEmail
     * @param token
     * @return
     */
    Charge doPay(long amount, String orderId, String receiptEmail, String token);

    Refund refund(String chargeId, long amount, boolean isNew);
    
    Dispute dispute(String disputeId);
    
    Dispute update(String disputeId,Map<String,Object> evidence)throws StripeException;
    
    
    File createFile(Map<String,Object> fileParams);
    
    
}
