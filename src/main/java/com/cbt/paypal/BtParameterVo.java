package com.cbt.paypal;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/*
 * 	Helpers for maintaining the request parameter data  
 */


public class BtParameterVo {

    private Map<String, String> map = new HashMap<String, String>();

    private List<String> list = new ArrayList<String>(
            Arrays.asList(
//			"checkout", "payment-method-nonce", "total-amount", "currency", "item-description", "first-name", "last-name", 
//			"recipient", "line1", "line2", "city", "state", "postal-code", "country-code"
                    "checkout", "payment-method-nonce", "total-amount", "currency"
            )
    );

    // store data from request parameters
    public void addParameter(HttpServletRequest request) {
        for (String entry : list) {
            map.put(entry, request.getParameter(entry));
        }
    }

    public void add(String key, String value) {
        map.put(key, value);
    }

    public String getd(String key) {
        return map.get(key);
    }

    public void setRequestAttributes(HttpServletRequest request) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }

}
