package com.cbt.paypal;
/*==================================================================
  Call to Get Access Token API and Create Payment (to get approval url or redirect url) from PayPal
 ===================================================================
*/

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BtCheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPost");

        BtTransactionHelper btHelper = new BtTransactionHelper();

        BtParameterVo vo = new BtParameterVo();
        vo.addParameter(request);

        RequestDispatcher dispatcher = null;

        try {

//			btHelper.doTransactionEc(vo,"","",null);

            // perform either Classic or PayNow transaction
            /*
			if (vo.getd("checkout").equals("classic")) {
				btHelper.doTransactionEcClassic(vo);
			}
			else {
				btHelper.doTransactionEcPayNow(vo);
			}
			*/

            if (btHelper.isSuccess()) {

                dispatcher = request.getRequestDispatcher("/success.jsp");
                btHelper.addSuccessParameters(vo);
                vo.setRequestAttributes(request);
            } else if (btHelper.getTransaction() != null) {


                btHelper.failurTransactionMessage();
                dispatcher = request.getRequestDispatcher("/error.jsp?message=" + btHelper.getFailureTransactionMessage());
            } else {

                try {
                    btHelper.addFailueLog();
                    dispatcher = request.getRequestDispatcher("/error.jsp?message=" + btHelper.getErrorMessage());
                } catch (Exception ex) {
                    dispatcher = request.getRequestDispatcher("/error.jsp?message=The transaction contained invalid data.");
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            dispatcher = request.getRequestDispatcher("/error.jsp");
        }

        dispatcher.forward(request, response);
    }
}

