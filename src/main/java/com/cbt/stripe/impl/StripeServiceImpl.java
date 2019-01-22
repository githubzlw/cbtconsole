package com.cbt.stripe.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cbt.stripe.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Dispute;
import com.stripe.model.File;
import com.stripe.model.Refund;

/**
 * @author lhao
 */
@Service
public class StripeServiceImpl implements StripeService {

    public static final String API_KEY = "sk_live_tP8CyVx39v29ar8pZQStuo1q";

    private static Logger logger = LoggerFactory.getLogger(StripeServiceImpl.class);

    public static void main(String[] args) throws Exception {
//        new StripeServiceImpl().doPay(10000, "luohao518@yeah.net", "xxxx", "111111");
        new StripeServiceImpl().refund("ch_1DTgnGLiluVmKKa3ZZL84nXS",6600);
    }

    /**
     * do pay
     *
     * @param amount
     * @param orderId
     * @param receiptEmail
     * @param token
     * @return
     */
    @Override
    public Charge doPay(long amount, String orderId, String receiptEmail, String token) {
        Stripe.apiKey = API_KEY;

        Map<String, Object> params = new HashMap<>(5);
        params.put("amount", amount);
        params.put("currency", "usd");
        params.put("description", "import-express.com");
        params.put("source", token);

        Map<String, String> metadata = new HashMap<>(3);
        metadata.put("order_id", orderId);

        params.put("metadata", metadata);
        params.put("receipt_email", receiptEmail);
        try {
            Charge charge = Charge.create(params);
            logger.debug("charge=[{}]", charge.toJson());
            logger.info("charge.Status=[{}]", charge.getStatus());
            return charge;
        } catch (StripeException e) {
        	logger.error("StripeException:", e);
            throw new RuntimeException(e);
		}
    }

    @Override
    public Refund refund(String chargeId, long amount)  {

        Stripe.apiKey = API_KEY;

        Map<String, Object> params = new HashMap<>();
        params.put("charge", chargeId);
        params.put("amount", amount);
        Refund refund = null;
        try {
            refund = Refund.create(params);
        } catch (StripeException e) {
        	logger.error("StripeException:", e);
        	throw new RuntimeException(e);
		}
        logger.info(refund.toJson());
        if("succeeded".equals(refund.getStatus())){
            logger.info("refund succeeded.");
            return refund;
        }else{
            throw new RuntimeException("refund failed");
        }
    }

	@Override
	public Dispute dispute(String disputeId) {
		Stripe.apiKey = API_KEY;
		Dispute retrieve;
		try {
			retrieve = Dispute.retrieve(disputeId);
		} catch (StripeException e) {
			 throw new RuntimeException(e);
		}
		return retrieve;
	}

	@Override
	public Dispute update(String disputeId, Map<String, Object> evidence) {
		Stripe.apiKey = API_KEY;
		Dispute update = null;
		try {
			Dispute dp = Dispute.retrieve(disputeId);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("evidence", evidence);
			update = dp.update(params);
			
		}catch (StripeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return update;
	}

	@Override
	public File createFile(Map<String, Object> fileParam) {
		Stripe.apiKey = API_KEY;
		File file = null;
		try {
			file = File.create(fileParam);
		} catch (StripeException e) {
			throw new RuntimeException(e);
		}
		return file;
	}
}
