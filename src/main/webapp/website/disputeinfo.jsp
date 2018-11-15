<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>消息</title>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/fine-uploader-new.min.css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/fine-uploader/jquery.fine-uploader.js"></script>
<style type="text/css">
	.avatar.isMe {
    color: #333;
    border-color: #333;
}
.avatar {
    border: 2px solid #cdcdcd;
    border-radius: 50%;
    background-color: rgba(50,50,50,0);
    height: 58px;
    width: 58px;
    line-height: 55px;
    vertical-align: middle;
    text-align: center;
}
.col-xs-10 {
    width: 83.33333333%;
}
 .messageBubble {
    padding: 20px;
    border: 1px solid #cdcdcd;
    position: relative;
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -khtml-border-radius: 4px;
    border-radius: 4px;
    -webkit-box-shadow: 0 0 3px 1px rgba(205,205,205,0.5);
    box-shadow: 0 0 3px 1px rgba(205,205,205,0.5);
}
 .messageBubble:before {
    content: '';
    width: 14px;
    height: 14px;
    background: #fff;
    position: absolute;
    top: 1.5em;
    left: -0.5em;
    border: 1px solid #cdcdcd;
    border-top: 0;
    border-right: 0;
    -webkit-transform: rotate(45deg);
    -moz-transform: rotate(45deg);
    -ms-transform: rotate(45deg);
    -o-transform: rotate(45deg);
    transform: rotate(45deg);
    -webkit-box-shadow: -4px 4px 5px -2px rgba(205,205,205,0.5);
    box-shadow: -4px 4px 5px -2px rgba(205,205,205,0.5);
}	
	.sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    margin: -1px;
    padding: 0;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
}
.panel{

	width:800px;
	-webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -khtml-border-radius: 4px;
    border-radius: 4px;
    -webkit-box-shadow: 0 0 3px 1px rgba(205,205,205,0.5);
    box-shadow: 0 0 3px 1px rgba(205,205,205,0.5);
    margin: 12px 0 0;
    padding: 8px;
    border: 1px solid #cdcdcd;
    position: relative;}
    
    .form-control{    
    width: 785px;
    height:34px;}
    
    .message_form{
    display: none;
    }
    .offerbody{
    display: none;
    }
    .hide{
    display: none;
    }
	</style>

<script type="text/template" id="qq-template-manual-trigger">
        <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="Please upload a file here" style="min-height: 45px;">
            <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container">
                <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar"></div>
            </div>
            <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
                <span class="qq-upload-drop-area-text-selector"></span>
            </div>
            <div class="buttons">
                <div class="qq-upload-button-selector qq-upload-button">
                    <div>choose file</div>
                </div>
                <button type="button" id="trigger-upload" class="btn btn-primary" style="display:none">
                    <i class="icon-upload icon-white"></i>upload
                </button>
            </div>
			<span id="upmessage" style="color:red"></span>
            <span class="qq-drop-processing-selector qq-drop-processing">
                <span>Processing dropped files...</span>
                <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
            </span>
            <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals">
                <li>
                    <div class="qq-progress-bar-container-selector">
                        <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>
                    </div>
                    <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
                    <img class="qq-thumbnail-selector" qq-max-size="100" qq-server-scale>
                    <span class="qq-upload-file-selector qq-upload-file"></span>
                    <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>
                    <input class="qq-edit-filename-selector qq-edit-filename" tabindex="0" type="text">
                    <span class="qq-upload-size-selector qq-upload-size"></span>
                    <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel">cancel</button>
                    <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry">reupload</button>
                    <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">remove</button>
                    <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
                </li>
            </ul>

            <dialog class="qq-alert-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">close</button>
                </div>
            </dialog>

            <dialog class="qq-confirm-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">No</button>
                    <button type="button" class="qq-ok-button-selector">Yes</button>
                </div>
            </dialog>

            <dialog class="qq-prompt-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <input type="text">
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">Cancel</button>
                    <button type="button" class="qq-ok-button-selector">Ok</button>
                </div>
            </dialog>
        </div>
    </script>
</head>
<body>

<div style="width:980px; position: absolute;margin-left: 200px;">
<div>
<h1>消息</h1>
</div>
<hr>

<div >

<section id="messages">
<h3 class="subject">reason:<strong>${result.reason }</strong>&nbsp;&nbsp;&nbsp;&nbsp;Status:${result.status }</h3>
<p class="caseId">Case ID:&nbsp; ${result.dispute_id }&nbsp;&nbsp;&nbsp;&nbsp;
Transaction ID:${result.disputed_transactions[0].seller_transaction_id }
&nbsp;&nbsp;&nbsp;&nbsp;Gross Amount:<strong>${result.disputed_transactions[0].gross_amount.value } ${result.disputed_transactions[0].gross_amount.currency_code }</strong>
</p>
<hr>
<p>
<c:set value="" var="buyer_note"></c:set>

<strong>
<c:if test="${not empty  buyer_requested_amount}">
Buyer Requested Amount:${result.offer.buyer_requested_amount.value } ${result.offer.buyer_requested_amount.currency_code }
</c:if>
<c:set value="0" var="buyerOfferFlag"></c:set>
<c:if test="${not empty  seller_offered_amount}">
<c:set value="1" var="buyerOfferFlag"></c:set>
&nbsp;&nbsp;&nbsp;&nbsp;Seller Offered Amount:${result.offer.seller_offered_amount.value } ${result.offer.seller_offered_amount.currency_code }
</c:if>

<c:if test="${not empty dispute_outcome_amount }">
Amount Refunded:${result.dispute_outcome.amount_refunded.value } ${result.dispute_outcome.amount_refunded.currency_code }
</c:if>
</strong></p>
<br>
<c:set value="1" var="buyerFlag"></c:set>
<c:forEach items="${result.messages }" var="message">
<div id="message" class="message">
<div class="row">
<div class="col-md-1 col-sm-2 col-xs-2 col-lg-1" style="float: left;">
<div class="avatar isMe" style="font-size: 13px;">${message.posted_by=='BUYER'?'Buyer':'You' }</div></div>
<div class="col-md-10 col-lg-7 col-xs-10 col-sm-10" style="float:left;">
<div class="messageBubble">
<c:if test="${buyerFlag=='1' }">
<div class="row buyerPurchasedUrl">
<div class="col-lg-12">
<p class="messageHeader snad">
<strong>${result.reason }</strong></p></div>
<div class="col-lg-3 "></div>
</div>
<hr>
</c:if>
<p class="messageBody"> 
<c:if test="${message.posted_by=='BUYER'}">${result.disputed_transactions[0].seller.name }</c:if>
<c:if test="${message.posted_by=='SELLER'}">${result.disputed_transactions[0].buyer.name }</c:if>,
<br>
${message.content }
</p>
<p class="messageFooter"><span class="signature">-&nbsp;
<c:if test="${message.posted_by=='BUYER'}">
<c:set value="${message.content }" var="buyer_note"></c:set>
${result.disputed_transactions[0].buyer.name }</c:if>
<c:if test="${message.posted_by=='SELLER'}">${result.disputed_transactions[0].seller.name }</c:if>,</span>&nbsp;
<span class="timestamp">${message.time_posted }</span></p>

</div>
</div>
</div>
<div style="clear: both;"></div>
<br>
<br>
</div>
<c:set value="2" var="buyerFlag"></c:set>
</c:forEach>
<%-- <div id="message" class="message">
<div class="row">
<div class="col-md-1 col-sm-2 col-xs-2 col-lg-1" style="float: left;">
<div class="avatar isMe" style="font-size: 13px;">Info</div></div>
<div class="col-md-10 col-lg-7 col-xs-10 col-sm-10" style="float:left;">
<div class="messageBubble">
<p class="messageBody"> 
buyer test 给您发送了一条消息。您需要在${before}之前回复买家的消息
</p>
</div>
</div>
</div>
<div style="clear: both;"></div>
<br>
<br>
</div> --%>

</section>
<section id="compose">

<c:if test="${statusFlag==0 || statusFlag ==1}">
<div class="row">

<div class="col-md-1 col-sm-2 col-xs-2 col-lg-1" style="float: left;">
<div class="avatar isMe" style="font-size: 13px;">You</div>
</div>



<div class="col-md-7 seller-response-options ">
<span>How would you like to respond?</span>
<c:if test="${statusFlag != 1}">

<div class="sr-onlyy">
<input type="button" value="Message Buyer" id="messageBuyerRadio">

<c:if test="${buyerOfferFlag== 0 && resonFlag == 0}">
<input type="button" value="Refund and Close" id="refundCloseRadio">
</c:if>

<c:if test="${buyerOfferFlag==0 && resonFlag == 1}">
<input type="button" value="Offer Full Refund" id="refundCloseRadio">
</c:if>

<c:if test="${buyerOfferFlag==0 && resonFlag == 0 && disputeLifeCycleFlag!=0 }">
<input type="button" value="Send an Offer" id="offerRadio">
</c:if>


<!-- <input type="button" value="输入跟踪信息和订单状态" id="addTrackingRadio"> -->

</div>

</c:if>
<c:if test="${statusFlag ==9 }">
<div style="width:800px;margin-left: 69px;">
<div class="issuediv">
<label for="issue_refund">
<input type="radio" id="issue_refund" class="sellerresponse" name="resolve_option" value="issue_refund">
Issue the buyer a refund for the disputed amount of $${result.dispute_amount.value } ${result.dispute_amount.currency_code }. 
The buyer can keep the item(s).
</label>
<div id="content" class="panel col-md-10 col-lg-7 col-xs-10 col-sm-10 hide sellerresponseform">
<div id="headline"><h2>Confirm Refund Details</h2>
</div><div id="messageBox"></div>
<div id="main"><div class="layout1">
<p>Confirm the details below and click the <strong>Process Refund</strong> 
button to send this refund and close the case.</p>
<form method="post" name="MIPSrefundConfirm" action="/cbtconsole/customer/dispute/acceptClaim" class="edit">
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<input type="hidden" name="invoice_id" value="${result.disputed_transactions[0].seller_transaction_id }">
<input type="hidden" name="refundAmount" value="${result.offer.buyer_requested_amount.value }">
<input type="hidden" name="refundCurrency" value="${result.offer.buyer_requested_amount.currency_code }">
<!-- <input type="hidden" name="accept_claim_reason" value="DID_NOT_SHIP_ITEM"> -->
<p class="group"><span class="label">Transaction ID:</span>
<span class="field">${result.disputed_transactions[0].seller_transaction_id }</span></p><p class="group">
<span class="label">Name:</span><span class="field">${result.disputed_transactions[0].buyer.name }</span>
</p>
<!-- <p class="group">
<span class="label">Email:</span>
<span class="field">sxwwan-buyer@hotmail.com</span></p> -->
<p class="group"><span class="label">Original Amount:</span>
<span class="field">$${result.disputed_transactions[0].gross_amount.value } ${result.disputed_transactions[0].gross_amount.currency_code }</span></p><p class="group">
<span class="label">Gross Refund Amount:</span><span class="field">$${result.dispute_amount.value } ${result.dispute_amount.currency_code }</span>
</p>
<!-- <p class="group"><span class="label">Net Refund Amount:</span>
<span class="field">$256.22 USD</span></p><p class="group">
<span class="label">Fee Refunded:</span><span class="field">$9.33 USD</span>
</p> -->
<p class="group">
<span class="label">Source of Funds:</span>
<span class="field">PayPal Balance</span></p>
<p class="group">
<span class="label">Note:</span><span class="field">
If your PayPal account balance does not have sufficient funds to cover the refund, 
PayPal will transfer funds from your default bank account.</span></p>
<p class="group">
<label for="note_to_buyer">
<span class="labelText">Note to Buyer:</span></label>
<br>
<span class="field"><textarea cols="100" rows="5" id="messageBodyForGeneric" name="messageBodyForGeneric"></textarea>
</span></p><p class="buttons">
<input type="submit" value="Process Refund" class="button primary">
<input type="button" value="Cancel" class="button issu_button"></p>
</form></div>
</div></div>

</div>

<br>
<div class="issuediv">
<label for="issue_refund_on_return">
<input type="radio" id="issue_refund_on_return" class="sellerresponse" name="resolve_option" value="issue_refund_on_return">
Issue the buyer a refund for the disputed amount of $${result.dispute_amount.value } ${result.dispute_amount.currency_code }. The buyer must return the item(s). 
Do not select this option for virtual/intangible items or services.</label>

<div id="content" class="panel col-md-10 col-lg-7 col-xs-10 col-sm-10 hide sellerresponseform">
<div id="headline"><h2>Request Return Of Item</h2>
</div><div id="messageBox"></div>
<div id="main"><div class="layout1">
<p>Click Continue to send the buyer an email requesting the return of your item.</p>
<p>The buyer will be asked to provide proof that the package was shipped. 
This information will be verified by you or PayPal in order for a 
refund to be processed and the claim closed. A refund will not be issued 
until the return has been verified.</p><p><strong>Note:</strong> 
If you are transacting with a non-U.S. or non-Canadian buyer who is 
unable to provide an online tracking number, you will be asked to confirm 
delivery as a courtesy to the buyer before a refund will be issued.</p>
<form method="post" name="MIPSrefundConfirm" action="/cbtconsole/customer/dispute/offer" class="edit">
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<input type="hidden" name="invoice_id" value="${result.disputed_transactions[0].seller_transaction_id }">
<input type="hidden" name="refundAmount" value="${result.offer.buyer_requested_amount.value }">
<input type="hidden" name="refundCurrency" value="${result.offer.buyer_requested_amount.currency_code }">
<input type="hidden" name="offerType" value="REFUND_WITH_RETURN">
<p class="group"><span class="label">Transaction ID:</span>
<span class="field">${result.disputed_transactions[0].seller_transaction_id }</span></p><p class="group">
<span class="label">Name:</span><span class="field">${result.disputed_transactions[0].buyer.name }</span>
</p>
<p class="group">
<span class="label">Gross Refund Amount:</span>
<span class="field">$${result.dispute_amount.value } ${result.dispute_amount.currency_code }</span>
</p>
<p>This refund will be issued from your PayPal balance once the item has been returned. 
Please ensure that you have sufficient funds in your PayPal balance.</p>
<div class="form-group return-address ">
<label for="partialRefundReturnAddress">Select return address</label>
<select class="form-control" id="partialRefundReturnAddress">
<option value="0"> ${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_1 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_2 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_1 }
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.postal_code }
 </option>
<option value="custom">Other Adress</option>
</select>
<span class="others-info hide">如果地址不在列表中，您可以在备注栏中输入。</span></div>

<p class="group">
<label for="note_to_buyer">
<span class="labelText">Note to Buyer:</span></label>
<br>
<span class="field"><textarea cols="100" rows="5" id="messageBodyForGeneric" name="messageBodyForGeneric"></textarea>
</span></p><p class="buttons">
<input type="submit"  value="Process Refund" class="button primary">
<input type="button"  value="Cancel" class="button issu_button"></p>
</form></div>
</div></div>


</div>
<br>

<div class="issuediv">
<label for="offer_partial">
<input type="radio" id="offer_partial" class="sellerresponse" name="resolve_option" value="offer_partial">
Offer the buyer a partial refund in an attempt to close this claim.</label>
<div id="content" class="panel col-md-10 col-lg-7 col-xs-10 col-sm-10 hide sellerresponseform">
<div id="headline"><h1>Partial refund offer</h1>
</div><div id="messageBox"></div><div id="main">
<div class="layout1">
<form class="edit" id="partialRefund" method="post" action="/cbtconsole/customer/dispute/offer">
<p>To offer a partial refund, enter the amount you are offering to the buyer below and click 
<span class="emphasis">Calculate</span> 
to determine the Net Refund Amount and the Fee Refunded. Click 
<span class="emphasis">Process Refund</span> to initiate the refund.</p>
<p>The amount you enter for the partial refund will be checked against the amount the buyer requested.
 If the amounts match, the claim will be closed automatically. If the partial refund you offer is less than 
 the buyer's request, or if the buyer did not specify an amount, the buyer will be asked to accept or deny 
 your refund offer.</p>
 <p>The buyer will have 10 days to respond to your offer. If the buyer accepts it, 
 the claim will close automatically. If the buyer denies, PayPal will continue to investigate the complaint.</p>
 <p class="group"><span class="label">Transaction ID:</span>
<span class="field">${result.disputed_transactions[0].seller_transaction_id }</span></p><p class="group">
<span class="label">Name:</span><span class="field">${result.disputed_transactions[0].buyer.name }</span>
</p>
 <p class="group"><label for="">Disputed amount:</label><span class="field">$${result.dispute_amount.value } ${result.dispute_amount.currency_code }</span></p>
 <p class="group"><label for="">Buyer's refund request:</label><span class="field">$${result.offer.buyer_requested_amount.value } ${result.offer.buyer_requested_amount.currency_code }</span></p>
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<input type="hidden" name="invoice_id" value="${result.disputed_transactions[0].seller_transaction_id }">
<input type="hidden" name="refundCurrency" value="${result.offer.buyer_requested_amount.currency_code }">
<input type="hidden" name="offerType" value="REFUND_WITH_RETURN">

 <p class="group"><label for="">Gross refund:</label>
 <span class="field">
 <input type="text" id="partial_amount" size="10" 
 maxlength="16" class="partialAmount" name="refundAmount" value="">
</span>
 </p>
 <p><strong>Note:</strong> If your PayPal account balance doesn't have enough money in 
 it to cover this refund, money will be automatically transferred from your backup funding 
 source.</p><p class="group"><label for="">Message to buyer test (optional):</label>
 <br>
 <span class="field"><textarea cols="100" rows="6" class="wishContent255"  name="messageBodyForGeneric"></textarea>
 <span class="characters">
 <input type="text" size="3" maxlength="3"  name="remLen" value="255" id="remlen255" readonly="readonly">
 <span class="small"> characters left</span></span>
 </span></p>
 <table ><tbody>
 <tr>
 <td colspan="3" class="tableCellRegularSmall">If the partial refund you offer is less than the buyer's 
 request, or if the buyer did not specify an amount, your buyer has the option of accepting or denying this 
 partial refund offer. If the buyer denies the refund and the complaint is resolved in the buyer's favor, 
 we may require the buyer to return the item to you. Please select the address where you would like the item 
 to be sent.</td></tr><tr><td>
 <span class="smallEmphasis">Ship to:</span></td>

 <td align="left">
 <select name="shipping_address_id" onchange="">
 <option value="fiYRhUqmcMyeHp-6J9NzCRE3rBaCmmPT1-8MiNarvoRS_OGodmhjn5StzUq">
 NO 1 Nan Jin Road, Shanghai Municipality 200000 China</option></select></td></tr></tbody>
 </table>
 <p class="refundButton">
 <input type="submit"  value="Offer refund" class="button primary">
  <input type="button"  value="Cancel" class="button issu_button">
  </p>
  </form>
  </div></div></div>
</div>
<br>
<div class="issuediv">
<label for="already_refunded">
<input type="radio" id="already_refunded" class="sellerresponse" name="resolve_option" value="already_refunded">
Provide proof that the buyer has already been refunded.</label>
</div>
<br>
<div class="issuediv">
<label for="disagree">
<input type="radio" id="disagree" class="sellerresponse" name="resolve_option" value="disagree">
Disagree with claim.  I would like to provide additional information.</label>
<div id="content" class="panel col-md-10 col-lg-7 col-xs-10 col-sm-10 hide sellerresponseform">
<div id="headline"><h2>Disagree With Complaint</h2>
</div><div id="messageBox" class="legacyErrors"></div>
<div id="main" class="legacyErrors">
<div class="layout1"><div id="mipsDisagree">
<form method="post" action="/cbtconsole/customer/dispute/sendmessage" 
class=" edit">


<input type="hidden" name="disputeid" value="${result.dispute_id }">

<p>You'll find the buyer's reason for disputing each item below. You can disagree with all or part of the buyer's claim.
 Please address each item separately.</p><p>This information will be used by PayPal to decide the outcome of the claim.</p>
 <div class="review"><h3>Significantly Not as Described</h3>
 <table class="buyer" summary="buyer reasons">
 <tbody>
 <tr>
 <th>Buyers Reasons</th>
 <td>${result.extensions.merchandize_dispute_properties.product_details.sub_reasons[0] }<br></td>
 </tr>
<!--  <tr>
 <th>Item</th>
 <td>Off Ebay - Transaction</td>
 </tr> -->
 <tr>
 <th>Buyer Note</th>
 <td>${buyer_note}</td>
 </tr>
 </tbody>
 </table>
 
 <p class="group">
 <label for="textarea0" class="sellerlabel"><span class="labelText">Seller Explanation</span>
 </label>
 <br>
 <span class="field">
 <textarea cols="100" rows="5" id="textarea0" class="wishContent2000" name="messageBodyForGeneric"></textarea>
 <input type="text" size="3" maxlength="4"  value="2000" id="remlen2000" readonly="readonly">&nbsp;characters left<br></span>
</p>
 </div>
 <p class="buttons">
 <input type="submit"  value="Submit" class="button primary">
 <input type="button"  value="Cancel" class="button issu_button"></p>
</form>
</div></div></div></div>

</div>
<br>
</div>
</c:if>



</div>


<div style="margin-left: 60px;"  class="panel sendmessage_form message_form hide">
<form method="post" action="/cbtconsole/customer/dispute/sendmessage" >
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<div class="col-md-10 col-lg-7 col-xs-10 col-sm-10" style="float:left;margin-top: 15px;">
<div class="form-group ">
<label for="messageBody" class="sr-only">Message to buyer (optional)</label>
<textarea class="form-controltext " name="messageBodyForGeneric" id="messageBody" rows="10" cols="100" maxlength="2000" placeholder="Send a friendly message to the buyer"></textarea>
<input type="hidden" name="solution" value="generic"></div></div>
<div style="clear: both;"></div>
<br>
<!-- <div>
<div>
<input type="checkbox" aria-labelledby="photoEvidenceCheck" name="photoEvidenceCheck" value="true" id="photoEvidenceCheck">
要求买家发送物品图片到：</div>
<input type="text" value="" class="form-control hide email-box" name="sellerEmailForPhotoEvidence" aria-labelledby="sellerEmailForPhotoEvidence" id="sellerEmailForPhotoEvidence" placeholder="输入邮箱地址" size="75">
</div> -->
<br>

<div class="message-actions" >
<input type="submit" name="send" value="Submint" class="button" >
<input type="button" name="secondary" value="Cancel" class="cancel_button" >
</div>

</form>

</div>

<div style="margin-left: 60px;"  class="panel refund_form message_form hide">
<!-- 退款 -->
<form method="post" action="${resonFlag == 1?'/cbtconsole/customer/dispute/offer': '/cbtconsole/customer/dispute/acceptClaim'}" >
<c:if test="${resonFlag == 1}">
<input type="hidden" name="offerType" id="offer_type" value="REFUND">
</c:if>
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<input type="hidden" name="invoice_id" value="${result.disputed_transactions[0].seller_transaction_id }">
<c:if test="${resonFlag == 0}">
<input type="hidden" name="accept_claim_reason" value="DID_NOT_SHIP_ITEM">
<input type="hidden" name="refundAmount" value="${result.offer.buyer_requested_amount.value }">
<input type="hidden" name="refundCurrency" value="${result.offer.buyer_requested_amount.currency_code }">
</c:if>
<c:if test="${resonFlag == 1}">
<input type="hidden" name="refundAmount" value="${result.dispute_amount.value }">
<input type="hidden" name="refundCurrency" value="${result.dispute_amount.currency_code }">
</c:if>
<div class="col-md-10 col-lg-7 col-sm-10" style="float:left;margin-top: 15px;">
<div class="form-group ">
<div>
<c:if test="${buyerOfferFlag==0 && resonFlag == 0}"></c:if>
<c:if test="${resonFlag == 0}">
<c:if test="${disputeLifeCycleFlag!=2 }">
<label>Accept buyer's offer</label>
<p>
${result.disputed_transactions[0].buyer.name }  has requested a refund 
of ${result.offer.buyer_requested_amount.value }${result.offer.buyer_requested_amount.currency_code }.
You're refunding an amount 
of ${result.offer.buyer_requested_amount.value }${result.offer.buyer_requested_amount.currency_code }.
The buyer is under no requirement to return the item. 
This case will be closed once you send the refund to the buyer.
</p>
</c:if>

<c:if test="${disputeLifeCycleFlag==2 }">
<label>Full refund without return</label>
<p>You're refunding an amount of $${result.dispute_amount.value }&nbsp;${result.dispute_amount.currency_code }.</p>

<p>You're issuing a full refund and the buyer can retain the item. 
This case will be closed once you send the refund to the buyer.</p>
</c:if>
</c:if>
<c:if test="${resonFlag == 1}">
Once the refund is sent to ${result.disputed_transactions[0].buyer.name }, 
this problem will be resolved and closed.
</c:if>
</div>

<textarea class="form-controltext " name="messageBodyForGeneric" id="messageBody" rows="10" cols="100" maxlength="2000" placeholder="Enter your offer details"></textarea>
<input type="hidden" name="solution" value="generic"></div></div>
<div style="clear: both;"></div>
<br>
<br>

<div class="message-actions" >
<input type="submit" name="send" value="Refund and Close" class="button" >
<input type="button" name="secondary" value="Cancel" class="cancel_button" >
</div>

</form>
</div>


<div  class="offer_form message_form hide">
<ul style="margin-left: 30px;">
<li class="panel panel-default nomark full-refund-offer">
<div class="head"><label for="fullRefundSubOffer">Full refund offer with item return</label>
<input type="radio" name="offerGroupType" id="fullRefundSubOffer" value="fullRefund" class="sr-only offer-sub-type">
</div>

<div class="body offerbody fullofferbody hide"><span class="info-text">Refund amount——<span id="full_amount">${result.dispute_amount.value }</span>&nbsp;${result.dispute_amount.currency_code }</span>
<div class="form-group return-address ">
<label for="fullRefundReturnAddress">Select return address</label>
<select class="form-control" id="fullRefundReturnAddress">
<option value="0">
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_1 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_2 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_1 }
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.postal_code }
 
 </option>
<option value="custom">Other Adress</option></select>
<span class="others-info hide">If the address isn't available in the list, you may enter it in the notes field.</span>
</div><p class="status-text">The refund will be processed once you confirm that you've received the item from the buyer.</p><div class="form-group ">
<textarea class="form-control" id="messageBodyForSellerFullOffer" rows="7" cols="72" maxlength="2000" placeholder="Enter your offer details"></textarea>
</div><div class="button-group">
<input type="submit" name="continueOffer" value="Send Offer" class="button offer_send">
<input type="button" name="resetOffer" value="Cancel" class="secondary button cancel-offer" ></div>
</div>
</li>




<li class="panel panel-default nomark partial-refund-offer">
<div class="head">
<label for="partialRefundSubOffer">Partial refund offer</label>
<input type="radio" name="offerGroupType" id="partialRefundSubOffer" value="partialRefund" class="sr-only offer-sub-type">
</div>
<div class="body  offerbody hide">
<div class="partial-options"><div class="option-item">
<div class="form-group radio">
<input type="radio" name="partialOfferType" value="partialOnly" id="partialOnlyRadio" class="offer-type" checked="checked">
<label for="partialOnlyRadio">I'll issue a partial refund.</label></div>
<div class="more-options">
<div class="form-group input-group ">
<input type="text" placeholder="Enter refund amount" size="20" value="" class="partial-amount partialOnly" style="width:110px;height:24px;">
<label class="input-group-addon">${result.offer.buyer_requested_amount.currency_code } </label></div></div>
</div>
<div class="option-item">
<div class="form-group radio">
<input type="radio" name="partialOfferType" value="partialReturn" id="partialReturnRadio" class="offer-type">
<label for="partialReturnRadio">I'll issue a partial refund. I want the item to be returned.</label></div>
<div class="more-options hide">
<p class="with-return">The refund will be processed once you confirm that you've received the item from the buyer.</p>
<div class="form-group input-group ">
<input type="text" placeholder="Enter refund amount" size="20" value="" class="partial-amount partialReturn" style="width:110px;height:24px;">
<label class="input-group-addon">${result.dispute_amount.currency_code } </label>
</div><div class="form-group return-address ">
<label for="partialRefundReturnAddress">Select return address</label>
<select class="form-control" id="partialRefundReturnAddress">
<option value="0"> ${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_1 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_2 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_1 }
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.postal_code }
 </option>
<option value="custom">Other Adress</option>
</select>
<span class="others-info hide">如果地址不在列表中，您可以在备注栏中输入。</span></div>
</div></div><div class="option-item">
<div class="form-group radio">
<input type="radio" name="partialOfferType" value="partialReplace" id="partialReplaceRadio" class="offer-type">
<label for="partialReplaceRadio">I'll issue a partial refund and send a replacement.</label></div>
<div class="more-options hide"><p class="with-replacement">The refund will be processed once the buyer accepts your offer. You may send the replacement after that.</p>
<div class="form-group input-group ">
<input type="text" placeholder="Enter refund amount" size="20" value="" class="partial-amount partialReplace" style="width:110px;height:24px;">
<label class="input-group-addon">${result.dispute_amount.currency_code } </label></div><div class="form-group shipping-address">
<label>Buyer's shipping address</label>
<br>
<label>
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_1 }
<br>
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_2 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_1 }
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.postal_code }
</label>
</div></div></div></div><div class="form-group ">
<textarea class="form-control" id="messageBodyForSellerPartialOffer" rows="7" cols="72" maxlength="2000" placeholder="Enter your offer details"></textarea></div>
<div class="button-group"><input type="submit" name="continueOffer" value="Send Offer" class="button offer_send">
<input type="button" name="resetOffer" value="Cancel" class="secondary button cancel-offer"></div></div></li>


<li class="panel panel-default nomark replacement-offer">
<div class="head">
<label for="replacementSubOffer">Replacement offer</label>
<input type="radio" name="offerGroupType" id="replacementSubOffer" value="replacement" class="sr-only offer-sub-type"></div>
<div class="body  offerbody hide"><span class="info-text">You're offering a replacement to the buyer without a refund or return of the original item.</span>
<div class="form-group shipping-address">
<label>Buyer's shipping address</label>
<br>
<label>
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_1 }
<br>
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_2 }, 
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_1 }
 ${result.extensions.merchandize_dispute_properties.return_shipping_address.postal_code }
</label>
</div>
<p class="status-text">Once buyer test accepts your offer, you can send the replacement item. This case will be closed once the buyer confirms the delivery of item.</p>
<div class="form-group ">
<textarea class="form-control" id="messageBodyForSellerReplacementOffer" rows="7" cols="72" maxlength="2000" placeholder="Enter your offer details"></textarea>
</div>
<div class="button-group">
<input type="submit" name="continueOffer" value="Send Offer" class="button offer_send" >
<input type="button" name="resetOffer" value="Cancel" class="secondary button cancel-offer cancel_offer" ></div>
</div></li>
</ul>
</div>

<div style="margin-left: 60px;"  class="panel addTrackingLink_form message_form addTrackingLink_form hide">
<!-- 订单跟踪 -->
<form method="post" action="/cbtconsole/customer/dispute/evidence" >
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<input type="hidden" name="refundAmount" value="${result.offer.buyer_requested_amount.value }">
<input type="hidden" name="refundCurrency" value="${result.offer.buyer_requested_amount.currency_code }">
<div class="col-md-10 col-lg-7 col-sm-10" >
<div class="form-group ">

<div>
<label>输入跟踪信息，以便于您的客户跟踪订单状态。</label>
</div>

<div class="Col" style="min-height: 1px; padding-left: 10px; padding-right: 10px; width: 58.3333%;">
<div class="FormField"><div class="FormFieldAddOn">
<div class="vx_form-group vx_floatingLabel">
<label for="status">订单状态</label>
<div class="vx_select" data-label-content="订单状态">
<select id="status" name="status" class="validate vx_form-control" style="height: 50px; width: 329px;">
<option value="SHIPPED">已发货</option><option value="IN_PROCESS">正在处理</option>
<option value="ON_HOLD">冻结</option><option value="CANCELLED">已取消</option>
<option value="RETURNED">已退还</option><option value="PROCESSED">订单已处理</option>
</select></div></div>
<span class="trailingNodes"><!-- react-text: 23 --> <!-- /react-text --></span>
<span class="leadingNodes"></span></div></div><div>

<!-- <div class="vx_form-group vx_checkbox">
<input type="checkbox" class="vx_form-control" id="tracking-not-required" name="tracking-not-required" value="No Shipment Tracking">
<label for="tracking-not-required">不要求提供跟踪信息</label></div> -->
<br>
<div class="vx_form-group vx_has-error-with-message vx_floatingLabel_active" data-label-content="跟踪号">
<label for="trackingNumer">跟踪号</label>
<br>
<input style="height: 35px; width: 324px;" type="text" value="" class="vx_form-control" id="trackingNumer" name="trackingNumer" aria-required="true" placeholder="请输入跟踪号">
<!-- <span class="vx_form-control-message ">请输入跟踪号。</span> --></div>
<br>
<div class="vx_form-group vx_floatingLabel">
<label for="carrier">运送方式</label>
<div class="vx_select" data-label-content="运送方式">
<select id="carrier" name="carrier" class="validate vx_form-control" style="height: 50px; width: 329px;">
<option value="CN_EMS">China EMS</option>
<option value="FEDEX">FedEx</option>
<option value="CN_TNT">TNT</option>
<option value="UPS">UPS</option>
<option value="BUYLOGIC">Buy Logic International</option>
<option value="CHINA_POST">China Post</option>
<option value="CNEXPS">CNE Express</option>
<option value="CPACKET">cPacket</option>
<option value="ARAMEX">Aramex</option>
<option value="CN_EC">EC First Class</option>
<option value="CN_EMPS">EMPS Express</option>
<option value="YANWEN">YanWen</option>
<option value="DHL">DHL Express</option>
<option value="TNT">TNT</option>
<option value="TOLL_IPEC">Toll IPEC</option>
<option value="CN_SF_EXPRESS">SF EXPRESS</option>
<option value="HK_HONGKONG_POST">HONGKONG Post</option>
<option value="SG_SG_POST">Singapore Post</option>
<option value="DHL_GLOBAL_ECOMMERCE">DHL Global Mail</option>
<option value="UBI_LOGISTICS">UBI Logistics</option>
<option value="EU_BPOST">Belgium Post</option>
<option value="FOUR_PX_EXPRESS">Four PX Express</option>
<option value="FLYT_EXPRESS">FLYT Express</option>
<option value="INTERNATIONAL_BRIDGE">International Bridge</option>
<option value="SFC_LOGISTICS">SFC Logistics</option>
<option value="BQC_EXPRESS">BQC Express</option>
<option value="ONE_WORLD">One World</option>
<option value="CN_OTHER">Other</option>
</select>
</div></div></div></div>

</div>
<div style="clear: both;"></div>
</div>
<br>
<br>
<div class="message-actions" >
<input type="submit" name="send" value="Submit" class="button" >
<input type="button" name="secondary" value="Cancel" class="cancel_button" >
</div>

</form>
</div>


</div>

<br><br>
</c:if>


</section>




</div>
<c:if test="${statusFlag==1 }">
<div id="main" class="legacyErrors" style="margin-left: 60px;">
<div class="layout1">

	 <input type="hidden" name="disputeid" value="${result.dispute_id }">
	  <div class="form-group">
	  <div class="col-sm-3 col-sm-offset-1">
	    <label for="uploadPic" class="control-label" style="text-align: left;">Upload Files:</label>
	    <div ><span style="color: red;">Note</span>:
		    <ul style="margin-left: 15px;">
		    	<li>1:jpg、png、gif 、pdf are allowed</li>
		    	<li>2:Size：lease than 5M</li>
		    </ul>
	    </div>
	  </div>
	    <div class="col-sm-7">
	    	 <div id="fine-uploader-manual-trigger"></div>
	    </div>
	  </div>
	   <div class="form-group">
	    <label for="remark" class="col-sm-3 col-sm-offset-1 control-label" style="text-align: left;">Description(optional)</label>
	    <div class="col-sm-7">
	    	<textarea class="wishContent60" rows="4" cols="100" id="Description" name="messageBodyForGeneric"></textarea>
	    	<br>
	    	<span class="field">
	    	<input type="text" value="60" id="remlen60" readonly="readonly" size="3" maxlength="3">
	    	characters left</span>
	    </div>
	  </div>
	  <div class="form-group">
	    <div class="col-sm-offset-6 col-sm-8">
	      <button type="button" id="subBtn" class="btn btn-success" onclick="submit()" >Submit</button>
	      <span class="errMsg"></span>
	      <p class="conpwar"></p>
	    </div>
	  </div>
 
 
 </div>
 </div>
</c:if>
</div>
<form id="evidenceform" class="form-horizontal" action="/cbtconsole/customer/dispute/evidence"  method="post"  style="display:none;">
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<input type="hidden" id="fileLocation" name="fileLocation"/>
<input type="hidden" id="evidenceformcontent" name="messageBodyForGeneric"/>
<input type="submit" value="submit">

</form>
<form action="/cbtconsole/customer/dispute/offer" method="post" id="makeoffer_form" style="display:none;">
<input type="hidden" name="invoice_id" value="${result.disputed_transactions[0].seller_transaction_id }">
<input type="hidden" name="disputeid" value="${result.dispute_id }">
<input type="hidden" name="refundCurrency" id="refundCurrency" value="${result.dispute_amount.currency_code }">
<input type="hidden" name="postalCode" id="postalCode" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.postal_code }">
<input type="hidden" name="address_line_1" id="address_line_1" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_1}">
<input type="hidden" name="address_line_2" id="address_line_2" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_2}">
<input type="hidden" name="address_line_3" id="address_line_3" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.address_line_3}">
<input type="hidden" name="admin_area_4" id="admin_area_4" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_4}">
<input type="hidden" name="admin_area_3" id="admin_area_3" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_3}">
<input type="hidden" name="admin_area_2" id="admin_area_2" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_2}">
<input type="hidden" name="admin_area_1" id="admin_area_1" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.admin_area_1}">
<input type="hidden" name="country_code" id="country_code" value="${result.extensions.merchandize_dispute_properties.return_shipping_address.country_code}">

<input type="hidden" name="offerType" id="offer_type" value="">
<input type="hidden" name="messageBodyForGeneric" id="note" value="">
<input type="hidden" name="refundAmount" id="refundAmount" value="">
<input type="submit" name="continueOffer" value="submit" id="offer_submit" >
</form>



</body>
<script type="text/javascript">
var sendResult = '${sendResult}'
if(sendResult !=''){
	alert(sendResult);
}
$("input:radio").click(function(){
		if($(this).is(':checked')){
			$(this).parents(".issuediv").find("#messageBodyForGeneric").val('');
			$(this).parents(".issuediv").siblings().find(".sellerresponseform").hide();
			$(this).parents(".issuediv").find(".sellerresponseform").show();
		}
})
$("#photoEvidenceCheck:checkbox").click(function () { 
	if($(this).is(':checked')){
		$(".email-box").show();
	}else{
		$(".email-box").hide();
		$(".email-box").val('');
	}
}); 

$("#messageBuyerRadio").click(function(){
	$(".message_form").hide();
	$(".offerbody").hide();
	$(".addTrackingLink_form").hide();
	$(".provideEvidence").hide();
	$(".sendmessage_form").show();
});
$("#refundCloseRadio").click(function(){
	$(".offerbody").hide();
	$(".message_form").hide();
	$(".addTrackingLink_form").hide();
	$(".provideEvidence").hide();
	$(".refund_form").show();
});
$("#offerRadio").click(function(){
	$(".addTrackingLink_form").hide();
	$(".offerbody").hide();
	$(".message_form").hide();
	$(".provideEvidence").hide();
	$(".offer_form").show();
});
$("#addTrackingRadio").click(function(){
	$(".offerbody").hide();
	$(".message_form").hide();
	$(".offer_form").hide();
	$(".addTrackingLink_form").show();
	$(".provideEvidence").hide();
	
});
$("#provideEvidence").click(function(){
	$(".offerbody").hide();
	$(".message_form").hide();
	$(".offer_form").hide();
	$(".addTrackingLink_form").hide();
	$(".provideEvidence").show();
	
});
$(".cancel_button").click(function(){
	$(".message_form").hide();
});
$(".nomark").each(function(){
	$(this).click(function(){
		$(this).siblings().find(".offerbody").hide();
		$(this).siblings().find(".offer-sub-type").removeAttr("checked");
		$(this).find(".offerbody").show();
		$(this).find(".offer-sub-type").attr("checked","checked");
		
	})
});
$(".offerbody").find(".cancel_offer").click(function(){
	$(this).parent().parent().hide();
		$(".offerbody").hide();
});
$(".offer-type").each(function(){
	$(this).click(function(){
		$(this).parents(".option-item").siblings().find(".more-options").hide();
		$(this).parents(".option-item").find(".more-options").show();
		$(this).parents(".option-item").siblings().find(".offer-type").removeAttr("checked");
		$(this).attr("checked","checked");
		
	})
});
$(".full-refund-offer").find(".offer_send").click(function(){
	$("#refundAmount").val($("#full_amount").html());
	$("#offer_type").val("REFUND_WITH_RETURN");
	$("#note").val($("#messageBodyForSellerFullOffer").val());
	//地址
	$("#makeoffer_form").submit();
})
$(".partial-refund-offer").find(".offer_send").click(function(){
	$("#note").val($("#messageBodyForSellerPartialOffer").val());
	if($("#partialOnlyRadio").attr("checked")){
		$("#refundAmount").val($(".partialOnly").val());
		$("#offer_type").val("REFUND");
	}
	if($("#partialReturnRadio").attr("checked")){
		$("#refundAmount").val($(".partialReturn").val());
		$("#offer_type").val("REFUND_WITH_RETURN");
		//$("#partialRefundReturnAddress").find("option:selected").text()
		//地址
	}
	if($("#partialReplaceRadio").attr("checked")){
		$("#refundAmount").val($(".partialReplace").val());
		$("#offer_type").val("REFUND_WITH_REPLACEMENT");
	}
	
	$("#makeoffer_form").submit();
})
$(".replacement-offer").find(".offer_send").click(function(){
	$("#refundAmount").val("");
	$("#offer_type").val("REPLACEMENT_WITHOUT_REFUND");
	$("#note").val($("#messageBodyForSellerReplacementOffer").val());
	$("#makeoffer_form").submit();
})

$(".wishContent255").on('input propertychange', function () {
    var _val = $(this).val();
    var count ="";
	if (_val.length > 255) {
		$(this).val(_val.substring(0, 255));
	}
	count = 255 - $(this).val().length;
	$("#remlen255").val(count);
});
$(".wishContent2000").on('input propertychange', function () {
    var _val = $(this).val();
    var count ="";
	if (_val.length > 2000) {
		$(this).val(_val.substring(0, 2000));
	}
	count = 2000 - $(this).val().length;
	$("#remlen2000").val(count);
});
$(".wishContent60").on('input propertychange', function () {
    var _val = $(this).val();
    var count ="";
	if (_val.length > 60) {
		$(this).val(_val.substring(0, 60));
	}
	count = 60 - $(this).val().length;
	$("#remlen60").val(count);
});
$(".issu_button").each(function(){
	$(this).click(function(){
		$(this).parents(".sellerresponseform").hide();
	})
})
function submit(){
	 var complainText =$("#Description").val();
	 if(complainText==''){
		 $('.errMsg').html('Note cannot be empty !');
		 return false;
	 }
	document.getElementById("trigger-upload").click();
	
}
</script>
	<script>
	 var manualUploader = new qq.FineUploader({
         element: document.getElementById('fine-uploader-manual-trigger'),
         template: 'qq-template-manual-trigger',
         request: {
             endpoint: '/cbtconsole/customer/dispute/uploads'
         },
         editFilename: {//编辑名字
             enable: true
         },
         thumbnails: {
             placeholders: {
                 waitingPath: '/cbtconsole/img/waiting-generic.png',
                 notAvailablePath: '/cbtconsole/img/not_available-generic.png'
             }
         },
         validation: {
             allowedExtensions: ['gif', 'jpg', 'png','pdf'],
             sizeLimit:10000000, //5*1000*1000 bytes
             accept:"image/jpg, image/gif , image/png, file/pdf"
         },
         callbacks: {
         	/* 开始上传 */
             onUpload: function (id, name) {
             },
             /* 选择文件后 */
             onSubmitted: function (id, name) {
             },
             /* 上传完成 */
             onComplete : function(id, name, responseJSON) {
            	 var str="";
            	 if(responseJSON.success){
           			var saveurl = responseJSON.saveLoaction;
            		var jsonArray= responseJSON.names;
           			$("#fileLocation").val(saveurl+jsonArray);
             	 }
				},
				onAllComplete : function (succeeded, failed) {
					if(failed.length==0){
						$("#upmessage").show(3000).delay(2000).hide(2000);
	        			$("#upmessage").html("Upload successfully.");
	        			$("#evidenceformcontent").val($("#Description").val());
	        			$("#evidenceform").submit();
					}else if(succeeded.length==0){
						$("#upmessage").show(3000).delay(2000).hide(2000);
	        			$("#upmessage").html("All failed.");
					}
				}
         },
         autoUpload: false,
         debug: true
     });

     qq(document.getElementById("trigger-upload")).attach("click", function() {
    	 manualUploader.uploadStoredFiles();
     });
     
     function fnsubmint(){
    		
    		var userid = $("#userid").val();
    		var upfile = $("#upfile").val();
    		 $.ajax({
    			type:'POST',
    			dataType:'text',
    			url:'/cbtconsole/autoorder/add',
    			data:{userid:userid,upfile:upfile},
    			success:function(res){
    				$(".qq-upload-list li").remove();
    				if(res!=''){
    					if(res=='0'){
    						alert('请重新登录');
    					}
    					
    					if(res=='-4'){
    						alert('失败--state=-4');
    					}
    					if(res=='-3'){
    						alert('失败--state=-3');
    					}
    					if(res=='-5'){
    						alert('失败--用户无可用地址');
    					}
    					
    					if(res.length>5){
    						$("#redult").val("订单生成成功,订单号:"+res);
    						$("#orderno").val(res);
    						$("#payment_amount").val(price);
    						$("#user_id").val(userid);
    					}	
    				}else{
    					alert('添加失败，请重新添加');
    				}
    			},
    			error:function(XMLResponse){
    				alert('error');
    			}
    		}); 
    		 $("#obt").attr("disabled", false); 
    	}
    </script>
</html>