<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@page import="com.cbt.util.Redis" %>
<%@page import="com.cbt.util.SerializeUtil" %>
<%@page import="com.cbt.website.userAuth.bean.Admuser" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/main.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/website/order_detail_new.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
    <style type="text/css">
        #prinum {
            width: 250px;
            position: absolute;
            top: 50%;
            left: 50%;
            background: #f6cece;
            border: 1px solid #ddd;
            padding: 20px;
            z-index: 999;
            padding-top: 20px;
            display: none;
        }

        .content_line {
            width: 100%;
            height: auto;
            word-wrap: break-word;
            word-break: break-all;
            overflow: hidden;
            color: red;
        }
        .show_h3 {
            height: 20px;
            text-align: left;
        }

        .pridclose {
            position: absolute;
            top: 3px;
            right: 3px;
            color: #f00;
        }

        .imagetable tr td {
            border-top: 1px solid #ddd;
            border-left: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
            text-align: center;
        }

        .imagetable tr td:last-child {
            border-right: 1px solid #ddd;
        }

        .peimask {
            background: #777;
            opacity: 0.8;
            filter: alpha(opacity=80);
            position: absolute;
            top: 0;
            left: 0;
            z-index: 998;
            width: 100%;
            height: 100%;
            display: none;
        }

        .mod_pay3 {
            width: 500px;
            position: fixed;
            margin-left: 40%;
            margin-top: 10%;
            z-index: 1011;
            background: gray;
            padding: 5px;
            padding-bottom: 20px;
            z-index: 1011;
            border: 15px solid #33CCFF;
        }

        .repalyBtn {
            height: 30px;
            width: 70px;
            background: #1c9439;
            border: 0px solid #dcdcdc;
            color: #ffffff;
            cursor: pointer;
        }

        .mask {
            display: none;
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            right: 0;
            margin: auto;
            background: rgba(0, 0, 0, 0.6);
            width: 300px;
            height: 60px;
            line-height: 60px;
            text-align: center;
            border-radius: 10px;
            font-size: 20px;
            color: #fff;
            z-index: 100;
        }

        #div_clothing, #ss_div, #dz_div {
            position: fixed;
            top: 50%;
            left: 50%;
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            -o-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
        }

        #div_clothing table, #ss_div table, #dz_div table {
            background-color: pink;
        }

        #div_clothing input, #ss_div input, #dz_div input {
            background-color: #eee;
        }
    </style>

    <script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".imgclass").lazyload({//????????????
                effect: 'fadeIn',
                threshold: 300
            });
        });
    </script>
    <script type="text/javascript">
        var adminid = ${order.adminid};
        <%String sessionId = request.getSession().getId();
            String userJson = Redis.hget(sessionId, "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            int uid =0;
            String roleType="";
            if(user !=null){
                uid = user.getId();
                roleType=user.getRoletype();
            }else{
                user = new Admuser();
                user.setId(0);
                user.setAdmName("");
            }%>
        $(function () {
            admid = '<%=uid%>';
        })
        //??????????????????
        $(document).ready(function () {
            var sale = <%=request.getAttribute("sale")%>;  //?????????
            var buy = <%=request.getAttribute("buy")%>;  //?????????
            var country = <%=request.getAttribute("country")%>;  //??????id
            var volume = <%=request.getAttribute("volume")%>; //?????????
            var weight = <%=request.getAttribute("weight")%>; //?????????
            var goodsWeight = <%=request.getAttribute("goodsWeight")%>; //???????????????
            var jq = <%=request.getAttribute("avg_jq")%>; //???????????????
            var rate = <%=request.getAttribute("rate")%>; //??????
            var allFreight = <%=request.getAttribute("allFreight")%>; //????????????
            var order_state = <%=request.getAttribute("order_state")%>;
            var actual_freight = <%=request.getAttribute("actual_freight")%>;
            var piaAmount =<%=request.getAttribute("piaAmount")%>;
            var estimatefreight = <%=request.getAttribute("estimatefreight")%>; //????????????????????????
            var es_buyAmount = <%=request.getAttribute("es_prices")%>;//??????????????????
            var transportcompany = '<%=request.getAttribute("transportcompany")%>';//????????????
            var shippingtype = '<%=request.getAttribute("shippingtype")%>';//????????????
            var allWeight =<%=request.getAttribute("allWeight")%>;//????????????????????????????????????custom_benchmark_ready-final_weight
            var awes_freight = '<%=request.getAttribute("awes_freight")%>';//??????????????????????????????
            var ac_weight = <%=request.getAttribute("ac_weight")%>; //eric??????????????????
            if (buy > 0) {
                buy = Number(buy) + Number(piaAmount);
            }
            if (allFreight == null || allFreight == "") {
                allFreight = 0.00;
            }
            if (estimatefreight == null || estimatefreight == "") {
                estimatefreight = 0.00;
            }
            if (es_buyAmount == null || es_buyAmount == "") {
                es_buyAmount = 0.00;
                piaAmount = 0.00;
            } else {
                es_buyAmount = Number(es_buyAmount) + Number(piaAmount);
            }
            var es_profit = sale - es_buyAmount - allFreight;
            es_profit = es_profit.toFixed(2);
            var ec_p = (sale - es_buyAmount - allFreight) / sale * 100;
            if (ec_p == null || ec_p == "") {
                ec_p = "0.00";
            } else {
                ec_p = ec_p.toFixed(2);
            }
            var ac_profit = "--";
            var ac_p = "--";
            var end_profit = "--";
            var end_p = "--";
            if (actual_freight == "654321") {
                actual_freight = "--";
            }
            if (awes_freight != "-") {
                ac_profit = sale - buy - awes_freight;
                ac_profit = ac_profit.toFixed(2);
                ac_p = (sale - buy - awes_freight) / sale * 100;
                if (ac_p == null || ac_p == "") {
                    ac_p = "--";
                } else {
                    ac_p = ac_p.toFixed(2);
                }
            }
            if (buy > 0 && actual_freight != '--') {
                end_profit = sale - buy - actual_freight;
                end_profit = end_profit.toFixed(2);
                end_p = end_profit / sale * 100;
                end_p = end_p.toFixed(2);
            }
            $("#es_price").html(es_buyAmount.toFixed(2));
            $("#esPidAmount").html("(????????????????????????:" + piaAmount + ")");
            $("#pay_price").html(sale + ";??????:(" + rate + ")");
            $("#end_profit").html(end_profit);
            $("#end_p").html(end_p + "%");
            $("#transportcompany").html(transportcompany == null || transportcompany == "" ? "--" : transportcompany);
            $("#shippingtype").html(shippingtype == null || shippingtype == "" ? "--" : shippingtype);
            $("#buyAmount").html(buy.toFixed(2));
            $("#es_weight").html(weight);
            $("#ac_weight").html(ac_weight);
            $("#es_freight").html(allFreight.toFixed(2));
            $("#awes_freight").html(awes_freight);
            $("#ac_freight").html(actual_freight);
            $("#es_profit").html(es_profit);
            $("#ac_profit").html(ac_profit);
            // $("#goodsWeight").html(goodsWeight);
            $("#es_p").html(ec_p + "%");
            $("#ac_p").html(ac_p + "%");

            var cname = "<%=request.getAttribute("countryName")%>"; //?????????????????????????????????
            var orderNo = "<%=request.getAttribute("orderNo")%>"; //?????????
            var oids = "<%=request.getAttribute("str_oid")%>"; //??????????????????oid
            if (orderNo != "" && cname != "" && cname != null) {
                searchCountry(cname, orderNo);
            }

            //????????????????????????
            saveLrData(orderNo,es_profit,ac_profit,end_profit);

            var userid = <%=request.getAttribute("userid")%>;
            queryRepeat(userid);
            getBuyer(oids);
            getAllBuyuser();
            fnGetAddress();
            var adminName = '<%=user.getAdmName()%>';
            var roleType = '<%=user.getRoletype()%>';
            if (roleType == 0) {
                // $("#buyuser1").attr("disabled",true);
                $("#buy_but").attr("disabled", true);
                $("#saler").attr("disabled", true);
                $("#saler_but").attr("disabled", true);
            }
            $("#ordercountry_value").val("${order.address.country}");
            $("#ordercountry").val("${order.address.country}");
        });
        //???????????????pid????????????
        function pidchec(odid,orderNo){
            var admid=$("#buyer"+odid).val()
            // alert(admid)
            $.ajax({
                url: "/cbtconsole/orderDetails/changeBuyerByPid",
                type: "post",
                dataType: "json",
                data: {"odid": odid, "orderNo": orderNo, "admid": admid},
                success: function (data) {
                    if (data.ok) {
                        $("#info" + odid).text("????????????");
                    } else {
                        $("#info" + odid).text("????????????");
                    }
                    window.location.reload();
                },
                error: function (res) {
                    $("#info" + odid).text("????????????,??????????????????");
                }
            });

        }


    </script>

    <link type="text/css" rel="stylesheet"
          href="/cbtconsole/css/web-ordetail.css"/>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <title>????????????</title>
    <style type="text/css">
        em {
            font-style: normal;
        }

        .orderInfo {
            border-collapse: separate;
            width: 95%;
            font-size: 13px;
            border-spacing: 0px 10px;
        }

    </style>
</head>
<body onload="fninitbuy()">
<div class="mask"></div>
<div
        style="width: 50px; height: 50px; position: fixed; right: 50px; top: 800px; background:no-repeat;">
    <a href="#top"> <img src="/cbtconsole/img/website/top.png"></a>
</div>
<div class="mod_pay3" style="display: none;" id="repalyDiv1">
    <div>
        <a href="javascript:void(0)" class="show_x"
           onclick="$('#repalyDiv1').hide();" style="float: right;">???</a>
    </div>
    <input id="rk_orderNo" type="hidden" value="">
    <input id="rk_odid" type="hidden" value="">
    <input type="hidden" id="ordercountry_value">
    <input type="hidden" id="rk_goodsid">
    ????????????:
    <textarea name="remark_content" rows="8" cols="50" style="margin-top: 20px;" id="remark_content_"></textarea>
    <input type="button" id="repalyBtnId" onclick="saveRepalyContent()" value="????????????">
</div>
<!-- ??????????????????????????? -->
<div class="mod_pay3" style="display: none;width:720px;" id="supplierDiv">
    <div>
        <a href="javascript:void(0)" style="margin-left:700px" class="show_x" onclick="FncloseSupplierDiv()">???</a>
    </div>
    <center>
        <h3 class="show_h3">?????????????????????</h3>
        <table id="supplierDivInfos" class="imagetable">
            <thead>
            <tr>
                <td rowspan="5"><span style="color:red">[??????]</span>:1:??? 2:?????? 3: ?????? 4:????????? 5: ??????</td>
            </tr>
            <tr>
                <td>??????</td>
                <td>??????</td>
                <td>?????????????????????</td>
                <td>??????????????????</td>
            </tr>
            <tr>
                <td>
                    <span id="su_shop_id"></span>
                </td>
                <td>
                    <select id="quality">
                        <option value="0">---?????????---</option>
                        <option value="1">1???</option>
                        <option value="2">2???</option>
                        <option value="3">3???</option>
                        <option value="4">4???</option>
                        <option value="5">5???</option>
                    </select>
                </td>
                <td>
                    <input name="protocol" type="radio" value="2"/>???<input name="protocol" type="radio" value="1"/>???
                </td>
                <td>
                    <input type="text" id="su_data"
                           onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                           onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>
                    <input type="button" onclick="saveSupplier();" value="??????"/>
                </td>
                <td></td>
                <td></td>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </center>
</div>
<!-- ??????????????????????????? -->
<div class="mod_pay3" style="display: none;" id="supplierGoodsDiv">
    <div>
        <a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierGoodsDiv()">???</a>
    </div>
    <center>
        <h3 class="show_h3">??????????????????</h3>
        <table id="supplierGoodsDivInfos" class="imagetable">
            <thead>
            <tr>
                <td rowspan="5"><span style="color:red">[??????]</span>:1:??? 2:?????? 3: ?????? 4:????????? 5: ??????</td>
            </tr>
            <tr>
                <td>??????</td>
                <td>??????</td>
                <td>??????</td>
            </tr>
            <tr>
                <td>
                    <span id="su_goods_id"></span><br>
                    <span id="su_goods_p_id"></span>
                </td>
                <td>
                    <select id="g_quality">
                        <option value="0">---?????????---</option>
                        <option value="1">1???</option>
                        <option value="2">2???</option>
                        <option value="3">3???</option>
                        <option value="4">4???</option>
                        <option value="5">5???</option>
                    </select>
                </td>
                <td>
                    <textarea rows="10" cols="15" id="su_g_remark"></textarea>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>
                    <input type="button" onclick="saveGoodsSupplier();" value="??????"/>
                </td>
                <td></td>
                <td></td>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </center>
</div>
<!-- ????????? start yyl-->
<div class="mod_pay3" style="display: none;" id="commentDiv1">
    <div>
        <a href="javascript:void(0)" class="show_x"
           onclick="$('#commentDiv1').hide();" style="float: right;">???</a>
    </div>
    <input id="cm_id" type="hidden" value="">
    <input id="cm_adminname" type="hidden" value="">
    <input id="cm_orderNo" type="hidden" value="">
    <input id="cm_goodsSource" type="hidden" value="">
    <input id="cm_goodsPid" type="hidden" value="">
    <input id="cm_adminId" type="hidden" value="">
    <input type="hidden" id="cm_country" value="">
    <input type="hidden" id="cm_oid" value="">
    <input type="hidden" id="cm_carType" value="">
    ????????????:
    <textarea name="comment_content" rows="8" cols="50" style="margin-top: 20px;" id="comment_content_"></textarea>
    <input type="button" id="commentBtnId2" onclick="saveCommentContent()" value="????????????">
</div>
<!-- ??????end -->
<div class="ormacon">
    <h3 class="ordmatit">??????????????????</h3>
    <div>
        <input type="hidden" value="${order.pay_price_tow}"
               id="pay_price_tow"> <input type="hidden"
                                          value="${order.expect_arrive_time}" id="expect_arrive_time">
        <input type="hidden" value="${order.state}" id="order_state">
        <input type="hidden" value="${order.userName}" id="order_name">
        <input type="hidden" value="${payToTime}" id="payToTime">
        <table id="orderInfo" cellpadding="1" cellspacing="1" class="orderInfo" align="center">
            <tr class="ormatrname">
                <td class="ornmatd1">?????????:<input class="ormnum"
                                                            type="text" name="orderNo" id="orderNo" readonly="readonly"
                                                            value="${order.orderNo}"/>
                </td>
                <td class="ornmatd1"><em id="state_text">
                    ${order.state==-1 || order.state==6?'????????????':'' }${order.state==1?'?????????':'' }
                    <c:choose>
                        <c:when test="${order.state==2 && order.checked==order.countOd}">
                            ????????????,????????????
                        </c:when>
                        <c:when test="${order.state==2 && order.no_checked>0}">
                            ????????????????????????
                        </c:when>
                        <c:when test="${order.state==2 && order.problem!=0}">
                            ??????????????????????????????
                        </c:when>
                        <c:when test="${order.state==2}">
                            ???????????????????????????
                        </c:when>
                    </c:choose>
                    ${order.state==0?'????????????':'' }${order.state==3?'?????????':'' }
                    ${order.state==4?'??????':'' }
                    ${order.state==5?'???????????????':'' }${order.state==7?'?????????':'' }
                </em>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <c:if test="${order.backList>0}">
                        <span style="color:Red">???????????????&nbsp;&nbsp;</span>
                    </c:if>
                    <c:if test="${order.payBackList>0}">
                        <span style="color:Red">?????????????????????&nbsp;&nbsp;</span>
                    </c:if>
                    <c:if test="${order.backAddressCount>0}">
                        <span style="color:Red">?????????????????????</span>
                    </c:if>
                </td>
                <td colspan="2">
                    <c:if test="${fn:length(orderNos) > 0}">
                        <span class="ornmatd1">????????????:</span>
                        <div class="ormrelanum">
                            <c:forEach items="${orderNos}" var="order_correlation">
                                <a target="_blank"
                                   href="/cbtconsole/orderDetails/queryByOrderNo.do?&orderNo=${order_correlation}&state=${order.state}&username=${order.userName}">${order_correlation}</a>&nbsp;
                            </c:forEach>
                        </div>
                    </c:if><a style="color: red" href="/cbtconsole/customerRelationshipManagement/reorder?orderNo=${order.orderNo}">AddOrderToTest</a>
                </td>
            </tr>
            <!-- ???????????????????????? -->
            <tr>
                <td>
                    <span>name:</span>${order.userName}(ID:<em id="userId">${order.userid}</em>)
                    <a class="ordmlink" target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId=${order.userid}">????????????</a>
                    <br>
                    <span style="color: red; display: none" id="other_id"></span>
                </td>
                <td>
                    <span>Email:<a href="mailto:${order.userEmail}">${order.userEmail}</a></span>
                </td>
                <td>
                    <span>??????????????????:<em>${order.businessName}</em></span>
                </td>
                <td>
                    <span>????????????:${order.deliveryTime}</span>
                </td>
            </tr>
            <c:if test="${order.state != 0}">
                <tr>
                    <td>
                        <span>????????????:
                        <c:forEach
                                items="${pays}" var="pay">
                            <c:if test="${pay.orderid == order.orderNo}">
                                ${pay.createtime}
                            </c:if>
                        </c:forEach>
                        </span>
                    </td>
                    <td>
                        <span>????????????:<em
                            id="dzConfirmtime">${order.dzConfirmtime}</em></span>
                    </td>
                    <td>
                        <span>????????????:${order.pay_price}<em
                            id="currency">${order.currency}</em></span>
                    </td>
                    <td>
                        <span>????????????${order.remaining_price}${order.currency}</span>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td>
                    <span>???????????????:
                    <c:if test="${not empty order.mode_transport}">
                        ${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[1]:""}[
                        ${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[0]:""}]
                    </c:if></span>
                </td>
                <td>
                    <span>VIP??????:${order.gradeName} (${order.grade})</span>
                </td>
                <td>
                    <span>????????????(kg):${order.volumeweight}</span><span style="color:red;">?????????????????????(kg):${feeWeight}???</span>
                </td>
                <td>
                    <%--<span class="ormtittd"  style="margin-left:100px">???????????????</span><span style="color:red;">${order.svolume}</span>--%>
                </td>
            </tr>
            <tr>
                <c:if test="${order.state == 3 || order.state == 4 || order.state == 1 || order.state == 5 || order.state == 2}">
                    <td colspan="4">
                        <span>???????????????:???<fmt:formatNumber
                            value="${order.product_cost+preferential_price}" pattern="#0.00" type="number"
                            maxFractionDigits="2"/>???</span>
                        <c:if test="${actual_ffreight_+foreign_freight>0}">
                            + ??????????????????<span>???${actual_ffreight_+foreign_freight}???</span>
                        </c:if>
                        <c:if test="${service_fee>0}">
                            + ????????? <span>???${service_fee}???</span>
                        </c:if>
                        <c:if test="${order.processingfee>0}">
                            + ??????????????????15????????? <span>???${order.processingfee}???</span>
                        </c:if>
                        <c:if test="${actual_lwh>0}">
                            + ????????? <span>???${actual_lwh}???</span>
                        </c:if>
                        <c:if test="${order.vatBalance>0}">
                            +??????????????????<span>???${order.vatBalance}??? </span>
                        </c:if>
                        <c:if test="${order.memberFee>0}">
                            + ????????? <span>???${order.memberFee}???</span>
                        </c:if>
                        <c:if test="${order.actual_allincost>0}">
                            + ????????? <span>???${order.actual_allincost}???</span>
                        </c:if>
                        <c:if test="${order.extra_freight>0}">
                            +??????<span>(${order.extra_freight})</span>
                        </c:if>
                        <c:if test="${order.actual_freight_c>0}">
                            +EUB handling fee<span>???${order.actual_freight_c}??? </span>
                        </c:if>
                        <c:if test="${order.order_ac != 0}">
                            - ??????????????????<span>???${preferential_price}??? </span>
                        </c:if>
                        <c:if test="${order.discount_amount>0}">
                            -??????????????????<span>???${order.discount_amount}???</span>
                        </c:if>
                        <c:if test="${order.cashback>0}">
                            -?????????200??????<span>??? ${order.cashback}??? </span>
                        </c:if>
                        <c:if test="${order.extra_discount>0}">
                            -????????????<span>??? ${order.extra_discount}???</span>
                        </c:if>
                        <c:if test="${order.share_discount>0}">
                            -????????????<span>??? ${order.share_discount}???</span>
                        </c:if>
                        <c:if test="${order.couponAmount>0}">
                            -coupon??????<span>??? ${order.couponAmount}???</span>
                        </c:if>
                        <c:if test="${order.coupon_discount>0}">
                            -????????????<span>???${order.coupon_discount}??? </span>
                        </c:if>
                        <c:if test="${order.gradeDiscount>0}">
                            -${order.gradeName}<span>???${order.gradeDiscount}??? </span>
                        </c:if>
                        <c:if test="${firstdiscount>0}">
                            -??????????????????<span>???${firstdiscount}??? </span>
                        </c:if>
                        =<b>????????????</b><span class="ormtittdred ormtittdb"> ???<fmt:formatNumber
                            value="${(order.product_cost+actual_ffreight_+foreign_freight+order.actual_allincost+order.processingfee+actual_lwh+order.memberFee+order.extra_freight-order.discount_amount+service_fee-order.cashback-order.share_discount-order.extra_discount-order.coupon_discount-order.order_ac + order.vatBalance-firstdiscount+order.actual_freight_c-order.gradeDiscount-order.couponAmount) >0 ?
									(order.product_cost+actual_ffreight_+foreign_freight+order.processingfee+order.extra_freight+actual_lwh+order.memberFee-order.discount_amount+service_fee-order.cashback-order.share_discount-order.extra_discount-order.coupon_discount-order.couponAmount-0-
									order.gradeDiscount + order.vatBalance - firstdiscount + order.actual_freight_c+order.actual_allincost) : 0.00 }"
                            pattern="#0.00" type="number" maxFractionDigits="2"/>???
						</span>
                        <a href="/cbtconsole/orderDetails/orderPayDetails.do?orderNo=${order.orderNo}&userId=${order.userid}"
                           target="_blank" style="text-decoration: none;">??????????????? ???</a>
                    </td>
                </c:if>
                <c:if test="${order.state == -1 || order.state == 6 || order.state == 0}">
                    <td class="ormtittd1" colspan="4">
                        <a href="/cbtconsole/orderDetails/orderPayDetails.do?orderNo=${order.orderNo}&userId=${order.userid}"
                           target="_blank" style="text-decoration: none;">??????????????? ???</a>
                    </td>
                </c:if>
            </tr>
            <tr>
                <td colspan="2">${order.paytypes}</td>
                <c:if test="${hasSampleOrder > 0}">
                    <td colspan="2"><b style="color: red;font-size: 16px;">?????????????????????:<a href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${order.orderNo}_SP" target="_blank">${order.orderNo}_SP</a></b></td>
                </c:if>

            </tr>
            <tr>
                <td>
                    <c:if test="${order.state == 3 || order.state == 4 || order.state == 1 || order.state == 2}">
                        <a class="ordmlink" target="_blank"
                           href="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=${order.buyid}&userid=&orderno=${order.orderNo}&goodid=&date=&days=&state=&unpaid=0&pagesize=50&search_state=0">????????????</a>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${order.state == 3 || order.state == 4 }">
                        <a class="ordmlink" target="_blank"
                           href="/cbtconsole/website/forwarderpageplck.jsp?orderid=${order.orderNo}&flag=true">????????????</a>&nbsp;&nbsp;
                    </c:if>
                    <br/>
                    <c:if test="${order.state !=0 && order.state !=-1 && order.state !=6 }">
                        <a class="ordmlink" style="text-decoration: underline;cursor: pointer;"
                           onclick="jumpTracking('${order.orderNo}', '${order.dropShipList}')">Tracking</a>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${order.state !=6 }">
                        <a class="ordmlink" style="text-decoration: underline;cursor: pointer;"
                           onclick="jumpDetails('${order.orderNo}', '${order.dropShipList}')">??????????????????</a>&nbsp;&nbsp;
                    </c:if>

                    <c:if test="${order.complainFlag >0 }">
                        <a class="ordmlink" target="_blank"
                           href="/cbtconsole/complain/searchComplainByParam?userid=${order.userid}&creatTime=&complainState=-1&username=&toPage=1&currentPage=1">?????????</a>&nbsp;&nbsp;
                    </c:if>

                    <c:if test="${sampleschoice }">
                        <br /><br />
                        <span style="color: red; font-size: 16px;">
                            ??????????????????????????????????????????????????????: ???(???????????????)
                        </span>
                    </c:if>

                </td>
                <td>
                    <c:if test="${evaluate.evaluate != null && evaluate.evaluate !=''}">
                        <span>???????????????<span style="color:red;font-size:20px">${evaluate.evaluate}</span></span>
                    </c:if>
                </td>
                <td colspan="2">
                    <c:if test="${order.orderRemark != null && order.orderRemark!=''}">
                        <span>?????????????????????<span style="color:red;font-size:20px">${order.orderRemark}</span></span>
                    </c:if>
                </td>
            </tr>

            <tr>
                <td colspan="4">
                    <c:if test="${fn:length(userIds) > 0}">
                        <span>???????????????????????????ID:</span>
                        <c:forEach items="${userIds}" var="userid_correlation">
                            <span class="ormtittdred">${userid_correlation}</span>
                        </c:forEach>
                    </c:if>
                </td>
            </tr>

            <tr>
                <td colspan="4">
                    <input type="hidden" value="${isDropshipOrder}">
                    <label style="display: none; color: red; position: fixed; bottom: 610px; right: 70px; width: 150px; height: 30px;"
                           id="msg">(????????????)</label>
                    &nbsp;&nbsp; &nbsp;&nbsp;
                    <c:if test="${order.state!=3 || order.state!=4 }">
                        <c:if test="${overSeaTotal > 0}">
                            <input type="button" style="position: fixed; bottom: 556px; right: 50px; width: 150px; height: 30px;" id="closeOrder" value="????????????(????????????)">
                        </c:if>
                        <c:if test="${overSeaTotal == 0}">
                            <input type="button"
                               style="position: fixed; bottom: 556px; right: 50px; width: 150px; height: 30px;"
                               id="closeOrder"
                               onclick="fnCloseOrder('${order.orderNo}',${order.userid},${order.pay_price},
                                       '${order.currency}',${order.order_ac},'${order.userEmail}','${order.email}',
                                   ${order.product_cost+preferential_price},${actual_ffreight_+foreign_freight},
                                   ${order.actual_weight_estimate},${isDropshipOrder })" value="????????????">
                        </c:if>
                    </c:if>
                    <c:if test="${count==1 }">
                        <input type="button"
                               style="position: fixed; bottom: 350px; right: 50px; width: 150px; height: 30px; border-color: red"
                               onclick="fnChangeProduct('${order.orderNo}')" value="????????????">
                    </c:if>
                    &nbsp;&nbsp;
                    <c:if test="${isSplitOrder == 0 && order.state != 3 && order.state != 4}">
                        <c:if test="${overSeaTotal > 0}">
                            <input type="button" style="position: fixed; bottom: 524px; right: 50px; width: 150px; height: 30px;" id="split_order_btn" value="??????(????????????)">
                        </c:if>
                        <c:if test="${overSeaTotal == 0}">
                            <input type="button"
                               style="position: fixed; bottom: 524px; right: 50px; width: 150px; height: 30px;"
                               id="split_order_btn"
                               onclick="fnSplitOrder('${order.orderNo}','${order.email}','${param.paytime}')"
                               value="??????">
                        </c:if>
                    </c:if>
                    <c:if test="${isSplitOrder == 1}">
                        <c:if test="${overSeaTotal > 0}">
                            <input type="button"
                               style="position: fixed; bottom: 524px; right: 50px; width: 150px; height: 30px;"
                               id="split_order_btn"  value="??????(????????????)">
                        </c:if>
                        <c:if test="${overSeaTotal == 0}">
                            <input type="button"
                               style="position: fixed; bottom: 524px; right: 50px; width: 150px; height: 30px;"
                               id="split_order_btn"
                               onclick="fnSplitDropShipOrder('${order.orderNo}','${order.email}','${param.paytime}')"
                        value="??????">
                        </c:if>

                        &nbsp;&nbsp;
                    </c:if>
                    <%--<input type="button"--%>
                           <%--style="position: fixed; bottom: 492px; right: 50px; width: 150px; height: 30px;" id="openout" onclick="Down_sample('${order.orderNo}','${order.email}','${param.paytime}')"--%>
                            <%--value="??????">--%>
                    <input type="button"
                           style="position: fixed; bottom: 492px; right: 50px; width: 150px; height: 30px;" id="itop" onclick="deliver('${order.orderNo}','${order.userid}','${param.paytime}','${order.product_cost+preferential_price}')"
                            value="??????">
                </td>
            </tr>
            <tr>
                <td>
                    <input type="button"
                           style="position: fixed; bottom: 460px; right: 50px; width: 150px; height: 30px;" id="open"
                           onclick="afterReplenishment()" value="????????????">
                </td>
                <td>
                    <c:if test="${overSeaTotal > 0}">
                        <input type="button"
                           style="position: fixed; bottom: 425px; right: 50px; width: 150px; height: 30px;" id="spilt_num" value="????????????(????????????)">
                    </c:if>
                    <c:if test="${overSeaTotal == 0}">
                        <input type="button"
                           style="position: fixed; bottom: 425px; right: 50px; width: 150px; height: 30px;" id="spilt_num"
                           onclick="openSplitNumPage('${order.orderNo}')" value="????????????">
                    </c:if>

                </td>
                <td>
                    <input type="button"
                           style="position: fixed; bottom: 388px; right: 50px; width: 150px; height: 30px;"
                           onclick="openOverSeaSplit('${order.orderNo}')" value="???????????????">
                </td>
                <td>??????????????????????????? <select id="Abuyer" onchange="changeAllBuyer('${order.orderNo}',this.value)">
                    <option value=""></option>
                    <c:forEach var="aub" items="${aublist }">
                        <option value="${aub.id }">${aub.admName}</option>
                    </c:forEach>
                </select><span id="orderbuyer"></span></td>
                <td id="td_buyuser">
                    <span onclick="fnmessage();">???????????????????????????</span>
                    <select id="saler" name="saler" style="width: 110px;"></select>
                    <input type="submit" value="??????" id="saler_but"
                           onclick="addUser(${order.userid},'${order.userName}','${order.userEmail}')">
                    <span style="font-size: 15px; font-weight: bold; color: red;" id="salerresult"></span>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <span style="background-color: red">??????????????????</span>
        <div style="background-color: aqua">
            ????????????:<span style="color:red">${order.countOd}</span>;????????????:<span style="color:red">${order.cg}</span>;
            ????????????:<span style="color:red">${order.rk}</span>;??????????????????:<span style="color:red">${order.checkeds}</span>;
            ??????????????????:<span style="color:red">${order.yhCount}</span>
        </div>
                </td>
            </tr>
        </table>

        <div id="remarkdiv">
            <div class="ormamark">
                <table style="border-collapse:separate; border-spacing:5px;">
                    <tbody>
                    <tr>
                        <td><input id="remarkbtn" type="button" value="??????????????????(??????)"
                                   onclick="addremark('${order.orderNo}')"></td>
                        <td rowspan="2">????????????:</td>
                        <td rowspan="2"><textarea id="orderremark_" style="width:400px;height:50px;"></textarea></td>
                        <td id="success" style="color:red"></td>
                        <td></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div>
            <div style="margin-top: 10px; padding: 5px;"
                 id="tab_forwarder">
                <table
                        style="display: block; border-collapse: separate; border-spacing: 5px; border: 1px solid #00afff;width:1400px;">
                    <tr>
                        <td>??????????????????</td>
                        <td><a href="/cbtconsole/website/forwarderpageplck.jsp?expressNo=${forwarder.express_no}"
                               target="_blank">${forwarder.express_no}</a></td>
                        <td>?????????????????????</td>
                        <td><c:if test="${not empty forwarder.express_no}">${forwarder.logistics_name}</c:if></td>
                    </tr>
                </table>
            </div>
        </div>
        <div id="orderaddressdiv"
             style="margin-top: 10px; width: 1380px; height: 280px; border: 1px solid #0000FF; padding: 10px; display: block; float: left;margin-left:6px">
            <div style="float: left;margin-left:150px" id="div_address">
                ????????????:&nbsp;&nbsp;<br>
                <table style="height: 150px;border-collapse:separate; border-spacing:0px 10px;">
                    <tr>
                        <td>Recipients:</td>
                        <td><input id="orderrecipients" type="text" maxlength="50"
                                   value="${order.address.recipients}" style="width: 200px" disabled="disabled">
                            <input id="is_drop_flag" type="hidden" value="${isDropFlag}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Street:</td>
                        <td>
                            <input id="orderstreet" type="text" maxlength="50" value="${order.address.address}"
                                   style="width: 200px" disabled="disabled"><br>
                            <input id="orderstreet2" maxlength="50" type="text" value="${order.address.street}"
                                   style="width: 200px;" disabled="disabled">
                        </td>
                    </tr>
                    <tr>
                        <td>City:</td>
                        <td><input id="ordercity" type="text" maxlength="50"
                                   value="${order.address.address2}" style="width: 200px" disabled="disabled"></td>
                    </tr>
                    <tr>
                        <td>State:</td>
                        <td><input id="orderstate" type="text" maxlength="50"
                                   value="${order.address.statename}" style="width: 200px" disabled="disabled"></td>
                    </tr>
                    <tr>
                        <td>Country:</td>
                        <td>
                            <%--<input type="text" id="ordercountry" style="width: 180px" disabled="disabled" value="">--%>
                            <select id="ordercountry" style="width: 180px" disabled="disabled" >
                                <c:forEach items="${countryList }" var="zone">
                                    <option value="${zone.country}">${zone.country}</option>
                                </c:forEach>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Zip Code:</td>
                        <td><input id="orderzipcode" type="text" maxlength="10"
                                   value="${order.address.zip_code}" style="width: 200px" disabled="disabled"></td>
                    </tr>
                    <tr>
                        <td>Phone:</td>
                        <td><input id="orderphone" type="text" maxlength="18"
                                   value="${order.address.phone_number}" style="width: 200px" disabled="disabled"></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input id="OrderAddress" type="button" value="??????????????????"
                                   onclick="OrderAddress()"></td>
                    </tr>
                </table>
            </div>
            <div style="float: left; margin-left: 20px;margin-left:200px;" id="paypal_div">
                ????????????:&nbsp;&nbsp;<br>
                <table style="height: 150px;border-collapse:separate; border-spacing:0px 10px;">
                    <tr>
                        <td></td>
                        <td><input id="address_name" type="text" value=""
                                   style="width: 200px;" disabled="disabled"></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input id="address_street" type="text" value=""
                                   style="width: 200px" disabled="disabled">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input id="address_city" type="text" value=""
                                   style="width: 200px" disabled="disabled">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input id="address_state" type="text" value=""
                                   style="width: 200px" disabled="disabled">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input id="address_country_code" type="text" value=""
                                   style="width: 200px;" disabled="disabled"></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <a id="receiveremail" href="#"></a>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <br>
    <div>
        <table border="1" style="margin-left:160px;">
            <tr>
                <td>????????????????????????(???):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="pay_price" style="color:red;">-</span>
                </td>
                <%--<td rowspan="10">
                    ????????????:
                    <span style="color:red;" id="transportcompany">-</span>;
                    ????????????:<span style="color:red;" id="shippingtype">-</span><br>
                    ??????????????????=??????????????????-?????????/??????*????????????+????????????<br>
                    ??????????????????=SUM(???????????????)+??????????????????<br>
                    ??????????????????=?????????????????????<br>
                    ??????????????????????????????????????????????????????RMB??????????????????%???=????????????????????????-????????????????????????-??????????????????<br>
                    ???????????????????????????????????????????????????RMB??????????????????%???= ????????????????????????-??????????????????-????????????????????????<br>
                    ????????????????????????????????????????????????RMB??????????????????%???= ????????????????????????-??????????????????-?????????????????????
                </td>--%>
                <td>
                    ????????????:
                    <span style="color:red;" id="transportcompany">-</span>;
                    ????????????:<span style="color:red;" id="shippingtype">-</span><br>
                </td>
            </tr>
            <tr>
                <td>??????????????????(???):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_price" style="color:red;">-</span>
                    <span style="color:red;" id="esPidAmount"></span>
                    <c:if test="${not empty tipprice}">
                        <span style="color:blue">(${tipprice})</span>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>??????????????????(???):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="buyAmount" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>??????????????????(???):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="acBuyAmount" style="color:red;">-</span>
                </td>
                <td>??????????????????=??????????????????)</td>
            </tr>
            <tr>
                <td>????????????(???????????????)(kg):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_weight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>????????????(????????????)(kg):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="ac_weight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>???????????????????????????????????????+???????????????????????????(???):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_freight" style="color:red;">-</span>(
                    <c:if test="${not empty order.mode_transport}">
                        ${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[0]:""}
                    </c:if>)
                </td>
                <td>??????????????????=??????????????????-?????????/??????*????????????+????????????
                    ??????????????????=SUM(???????????????)+??????????????????</td>
            </tr>
            <tr>
                <td>????????????????????????(???):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="awes_freight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>???????????????????????????????????????(???):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="ac_freight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>??????????????????????????????????????????????????????(???)?????????????????????:</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_profit" style="color:red;">-</span>(<span id="es_p" style="color:red;">-</span>)
                </td>
                <td>??????????????????????????????????????????????????????RMB??????????????????%???=????????????????????????-????????????????????????-??????????????????</td>
            </tr>
            <tr>
                <td>???????????????????????????????????????????????????(???)?????????????????????:</td>
                <td style="text-align:center;vertical-align:middle;width:300px"><span id="ac_profit" style="color:red;">-</span>(<span
                        id="ac_p" style="color:red;">-</span>)
                </td>
                <td>???????????????????????????????????????????????????RMB??????????????????%???= ????????????????????????-??????????????????-????????????????????????</td>
            </tr>
            <tr>
                <td>????????????????????????????????????????????????(???)?????????????????????:</td>
                <td style="text-align:center;vertical-align:middle;width:300px"><span id="end_profit"
                                                                                      style="color:red;">-</span>(<span
                id="end_p" style="color:red;">-</span>)
                </td>
                <td>????????????????????????????????????????????????RMB??????????????????%???= ????????????????????????-??????????????????-?????????????????????</td>
            </tr>
        </table>
    </div>
    <div>
        <span class="d"></span><span id="sumFreight" style="color: green;"
                                     class="c"></span>
    </div>
    <div>
        <br/>
        <c:if test="${invoice=='1'}">
            <input type="button" value="??????invoice"
                   onclick="window.open('/cbtconsole/autoorder/show?orderid=${param.orderNo}','_blank')">
        </c:if>
        <c:if test="${invoice=='2'}">
            <input type="button" value="??????invoice"
                   onclick="window.open('/cbtconsole/autoorder/shows?orderid=${param.orderNo}','_blank')">
        </c:if>
        <br/>
    </div>
    <br>
    <c:if test="${orderRecord.recommend > 0}">
    <span style="margin-left:160px;">??????????????????</span>
    <table border="1" style="margin-left:160px;">
    <tr>
    <td style="width: 320px;">???????????????($)</td>
    <td style="width: 320px;">${orderRecord.cost}</td>
    </tr>
    <tr>
    <td style="width: 320px;">????????????(kg)</td>
    <td style="width: 320px;">${orderRecord.weight }</td>
    </tr>
    <tr>
    <td style="width: 320px;">??????????????????(???)</td>
    <td style="width: 320px;">${orderRecord.feight }</td>
    </tr>
    <tr>
    <td style="width: 320px;">????????????????????????</td>
    <td style="color: red;font-size: 24px;width: 320px;">${orderRecord.recommend==1?'??????':'?????????'}</td>
    </tr>
    </table>
    <br>
    </c:if>
    <c:if test="${isDropshipOrder == 0 || isDropshipOrder == 2 || isDropshipOrder == 3}">
        <!-- ??????dropship?????? -->
        <div style="width:1440px;">
            <table id="orderDetail" class="ormtable2" align="center">
                <caption><b style="font-size: 26px;color: red;">????????????[${totalSize}]</b></caption>
                <tbody>
                <tr class="detfretit">
                    <td>????????????/?????????id</td>
                    <td colspan="2">??????</td>
                    <td style="width:400px;">????????????</td>
                    <td style="width: 300px;">??????</td>
                    <td>??????</td>
                    <td>??????/??????</td>
                    <td>?????????</td>
                    <td style="width:500px;">????????????</td>
                </tr>
                </tbody>


                <c:forEach items="${orderDetailMap}" var="orderMap">
                    <tr style="text-align: center"><td colspan="7"><b style="color: red;font-size: 22px">?????????${orderMap.key}</b></td></tr>
                    <c:forEach items="${orderMap.value}" var="orderd" varStatus="sd">
                        <tr id="goodsid_${orderd.goodsid}"
                        style="${orderd.state == 2?'background-color: #FF8484':''}">
                        <td>${sd.index+1}<br>${orderd.goodsid}/${orderd.id}</td>
                        <td><input type="hidden" value="${orderd.state}"> <a>
                            <img class="imgclass" onclick="fnRend('${orderd.goods_url}')"
                                 width="200px" height="200px;" src="/cbtconsole/img/wy/grey.gif"
                                 data-original=" ${fn:replace(orderd.goods_img,'50x50','') }"
                                 style="cursor: pointer;">
                        </a>
                            <c:choose>
                                <c:when test="${orderd.match_url!=null}">
                                    <a href="${orderd.match_url}"
                                       style="width: 200px; display: block; word-wrap: break-word;"
                                       target="_blank">??????????????????</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${orderd.oldUrl}"
                                       style="width: 200px; display: block; word-wrap: break-word;"
                                       target="_blank">??????????????????</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style='width: 150px;'><span
                                style="color: red;">????????????:</span><br>${orderd.state == 2? "<br>???????????????":""}${orderd.goodsname}<br>
                            <span style="color: red;">??????????????????:</span><br> <span
                                    style="color: #00B1FF;display: inline-block;max-width: 250px;overflow: hidden;word-wrap: break-word;">
                                    ${orderd.goods_type}
                            </span><br> <span style="color: #8064A2; word-break: break-all;">${orderd.remark}</span>
                            <c:if test="${not empty orderd.img_type}">
                                <c:forEach items="${fn:split(orderd.img_type,'@')}"
                                           var="img_type" varStatus="i">
                                    <c:if test="${fn:indexOf(img_type,'http') > -1}">
                                        <img style="max-width: 60px;max-height: 60px;" src="${img_type}">&nbsp;
                                    </c:if>
                                </c:forEach>
                            </c:if></td>
                        <td style="width: 400px;">
                            <c:if test="${not empty orderd.inventoryRemark}">
                                <span style="background-color:chartreuse">????????????:${orderd.inventoryRemark}</span><br>
                            </c:if>
                            <span style="color: red;">??????:</span><em
                                id="number_${orderd.goodsid}" style="font-weight: bold;">${orderd.yourorder}
                                ${orderd.goodsUnit}</em><br>
                            <em id="change_number_${sd.index}" style="color: red;"> <c:if
                                    test="${not empty orderd.change_number }">
                                <br>${orderd.change_number}
                            </c:if>
                            </em><br>
                            <span style="color:red">(
								<c:if test="${orderd.is_sold_flag !=0}">
                                    ????????????
                                </c:if>
								<c:if test="${orderd.is_sold_flag ==0}">
                                    ???????????????
                                </c:if>
									)</span>
                            <c:if test="${orderd.overSeaFlag > 0}">
                                <br>
                                <a href="/cbtconsole/owstock/log?odid=${orderd.id}" target="_blank"><span style="color: red;background-color: #35de2a;">???????????????</span></a>
                                <br>
                            </c:if>
                            <span style="color: red">???????????????</span>$
                                ${orderd.goodsprice} <em id="change_price_${sd.index}"
                                                         style="color: red;"> <c:if
                                test="${not empty orderd.change_price }">
                            <br>${orderd.change_price}</c:if></em><br/>
                            <br>
                            <c:if test="${orderd.bm_flag == 1 and orderd.isBenchmark == 1}">
                                <span style="color: red">ali???????????????</span>
                                <span>$${orderd.ali_price}</span>
                                <span><br/><a target="_blank" href="${orderd.alipid }">ali????????????</a></span>
                            </c:if>
                            <br/>
                            <span class="content_line">??????:${orderd.remark}</span>  <c:if
                                test="${orderd.extra_freight != 0}">&nbsp;????????????:${orderd.extra_freight}</c:if>
                            <em id="change_delivery_${sd.index}" style="color: red;">
                                <c:if test="${not empty orderd.change_delivery }">
                                    <br>
                                    ????????????${orderd.change_delivery}
                                </c:if>
                            </em><br>
                            <c:if test="${not empty  orderd.replacement_product }">
                                <span >????????????:</span><br>
                                <button class="basic-r" style="display: inline-block;overflow: hidden;width: 70px;word-break: keep-all;white-space: nowrap;text-overflow: ellipsis;line-height: 28px;float: left;"  title="${orderd.replacement_product}"  onclick="showMsg('${orderd.replacement_product}')">${orderd.replacement_product}</button>
                            </c:if>
                        </td>
                        </td>
                        <td><input type="hidden"
                                   value="${orderd.state},${order.state},${orderd.orsstate},${orderd.od_state},${orderd.checked}">
                            <c:set value="${orderd.state}" var="ostate"></c:set> <em>
                                <c:if test="${ostate==0}">
                                    ${order.state==-1?'????????????':'' }${order.state==0?'????????????':'' }${order.state==1?'?????????':'' }${order.state==3?'?????????':'' }
                                    <c:if test="${order.state==4}">
                                        <!-- yyl ??????start -->
                                        <c:if test="${admuserinfo.roletype==0 }">
                                            <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                                ??????/????????????
                                            </button>
                                        </c:if>
                                        <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">
                                            ??????????????????
                                            <input type="hidden" id="goods_img${orderd.goods_pid }"
                                                   value="${orderd.goods_img}"/>
                                            <input type="hidden" id="goods_url${orderd.goods_pid }"
                                                   value="${orderd.goods_url }"/>
                                            <input type="hidden" id="goodsname${orderd.goods_pid }"
                                                   value="${orderd.goodsname }"/>
                                            <input type="hidden" id="goodsprice${orderd.goods_pid }"
                                                   value="${orderd.goodsprice }"/>
                                            <input type="hidden" id="orderDetailId${orderd.goods_pid }"
                                                   value="${orderd.id }"/>
                                            <input type="hidden" id="carType${orderd.goods_pid }"
                                                   value="${orderd.car_type }"/>
                                        </button>
                                        ??????
                                        <font color="red">????????????????????? <font name="${orderd.goods_pid }ID">?????????</font></font>
                                    </c:if>
                                    ${order.state==5?'???????????????':'' }
                                    <c:if test="${orderd.orsstate==1 && order.state==1}">
                                        <br>
                                        <br>
                                        <font color="red">????????????????????????</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==0 && order.state==1 && orderd.iscancel!=1 && orderd.od_state!=13}">
                                        <br>
                                        <br>
                                        <font color="red">????????????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==12 && order.state==1}">
                                        <br>
                                        <br>
                                        <font color="red">??????????????????</font>
                                        <c:if test="${not empty orderd.noChnageRemark }">
                                            <br>
                                            ?????????????????????:<span style="color: red;">${orderd.noChnageRemark}</span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==12 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">??????????????????</font>
                                        <c:if test="${not empty orderd.noChnageRemark }">
                                            <br>
                                            ?????????????????????:<span style="color: red;">${orderd.noChnageRemark}</span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${orderd.od_state==13 && order.state==1}">
                                        <br>
                                        <br>
                                        <font color="red">??????????????????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.od_state==13 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">??????????????????????????????</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==5 && order.state==1 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">??????????????????</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==5 && order.state==5 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">??????????????????</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==6 && order.state==1 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">?????????????????????????????????</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==6 && order.state==5 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">?????????????????????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==1 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">?????????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==3 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">???????????????????????????</font>
                                    </c:if>

                                </c:if>
                                <c:if test="${ostate==1 && not empty orderd.locationNum}">
                                    <br>
                                    <span style="background-color: #b3fbb5;font-size: 13px;">
                                        <b>?????????:</b><font color="red">${orderd.locationNum}</font></span>
                                </c:if>
                                    <%-- 									<c:if test="${ostate==1 }"> --%>
                                <!-- 										???????????????????????????????????? -->
                                    <%-- 									</c:if>  --%> <input type="hidden"
                                                                                                  value="${ostate},${orderd.checked},${orderd.goodstatus}">
                                <c:if test="${ostate==1 && orderd.checked==0}">
                                    ????????????
                                    <c:if test="${orderd.goodstatus==6}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,????????????????????????</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==5}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,?????????????????????</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==4}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,??????????????????</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==3}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,?????????????????????????????????</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==1}">
                                        <font color="red">,?????????</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==2}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,?????????????????????</a>
                                    </c:if>
                                </c:if>
                                <c:if test="${ostate==1 && orderd.checked==1 && orderd.goodstatus==1}">
                                <!-- yyl ??????start -->
                                <c:if test="${admuserinfo.roletype==0}">
                                    <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                        ??????/????????????
                                    </button>
                                </c:if>
                                <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">??????????????????
                                    <input type="hidden" id="goods_img${orderd.goods_pid }"
                                           value="${orderd.goods_img}"/>
                                    <input type="hidden" id="goods_url${orderd.goods_pid }"
                                           value="${orderd.goods_url }"/>
                                    <input type="hidden" id="goodsname${orderd.goods_pid }"
                                           value="${orderd.goodsname }"/>
                                    <input type="hidden" id="goodsprice${orderd.goods_pid }"
                                           value="${orderd.goodsprice }"/>
                                    <input type="hidden" id="orderDetailId${orderd.goods_pid }" value="${orderd.id }"/>
                                    <input type="hidden" id="carType${orderd.goods_pid }" value="${orderd.car_type }"/>
                                </button>
                                </br>????????????
                                <!-- '${orderd.goods_img}','${orderd.goods_url }','${orderd.goodsname }','${orderd.goodsprice }',${orderd.goodsid }' -->
                                <font color="green">,????????????</font>
                                </br>
                                <font color="red">
                                    ????????????????????? <font name="${orderd.goods_pid }ID">?????????</font>
                                    <!-- yyl ??????end -->
                                    </c:if>
                                    <c:if test="${ostate==1 && orderd.checked==1 && orderd.goodstatus !=1}">
                                    <font color="red" style="font-size:20px;">????????????????????????????????????????????????????????????</font>
                                    </c:if>
                            </em>


                            <input type="hidden" name="ostate" value="${ostate}"> <em
                                    id="change_cancel_${sd.index}">${orderd.iscancel==1?'<br>????????????':''}</em>
                            <em id="user_cancel_${sd.index}">${orderd.state == 2? "<br>???????????????":""}</em>
                            </br>?????????:<em>${orderd.shipno}</em> <br>????????????????????????:<em>${orderd.shipstatus}</em>
                        </td>
                        <td style="width: 200px;"><c:if test="${orderd.state!=2 }">
                            <button
                                    onclick="pricechange('${orderd.orderid}',${orderd.id},${orderd.goodsprice}+'',${sd.index},${isDropshipOrder})"
                                ${order.state==5||order.state==1?'':'disabled=disabled' }>????????????
                            </button>
                            <button
                                    onclick="deliverychange('${orderd.orderid}',${orderd.id},${orderd.delivery_time}+'',${sd.index},${isDropshipOrder})"
                                ${order.state==5||order.state==1?'':'disabled=disabled' }>????????????
                            </button>
                            <button
                                    onclick="rationchange('${orderd.orderid}',${orderd.id},${orderd.yourorder}+'',${sd.index},${isDropshipOrder})"
                                ${order.state==5||order.state==1?'':'disabled=disabled' }>????????????
                            </button>
                            <br/>
                            <!--???????????????????????????????????? start 5.9 ?????????-->
                            <div class="shield">
                                <button
                                        onclick="cancelchange('${orderd.orderid}',${orderd.goodsid},${sd.index})"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>??????(???)
                                </button>
                                <!--????????????????????????????????????  end 5.9 ?????????-->
                                <!--??????????????????????????????????????????????????????????????? 4.7 start 4.27 ?????????-->
                                <button id="deleteGoods${orderd.orderid}${orderd.goodsid}"
                                        onclick="deleteOrderGoods('${orderd.orderid}',${orderd.goodsid},${orderd.purchase_state},${order.userid })"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>??????
                                </button>
                            </div>
                            <!--??????????????????????????????????????????????????????????????? 4.7 end-->
                            <c:if test="${isDropshipOrder!=1}">
                                <button
                                        onclick="communicatechange('${orderd.orderid}',${orderd.id},${isDropshipOrder})"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>?????????
                                </button>
                            </c:if>
                            <c:if test="${isDropshipOrder==1}">
                                <button
                                        onclick="communicatechange('${orderd.orderid}',${orderd.id},${isDropshipOrder})"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>?????????
                                </button>
                            </c:if>
                            <c:if test="${orderd.shopFlag==1}">
                                <input type="button"
                                       onclick="openSupplierGoodsDiv('${orderd.goods_pid}','${orderd.shop_id}');"
                                       value="????????????"/>
                                <input type="button" onclick="openSupplierDiv('${orderd.shop_id}');" value="????????????"/>
                            </c:if>
                            <h3 style="color: red;"
                                id="t_delectGoods${orderd.orderid}${orderd.goodsid}"></h3>
                            <h3 style="color: red;"
                                id="f_delectGoods${orderd.orderid}${orderd.goodsid}"></h3>
                            <input type="hidden" value="${isDropshipOrder1}" id="isDropshipOrder1">
                            <input type="hidden" value="${isDropshipOrder}">
                        </c:if>
                            <!-- ????????????????????? -->
                            <c:if test="${fn:length(orderd.change_communication)>0}">
                                <br/>
                                <hr>
                                <c:if test="${orderd.ropType==5 && orderd.del_state==0 }">
                                    <button
                                            onclick="fnResolve('${order.orderNo}',${orderd.goodsid})">???????????????
                                    </button>
                                    <label style="display: none; color: red;" id="msg2">(?????????)</label>
                                </c:if>
                                <div id="change_cmmunication_${orderd.goodsid}"
                                     style="overflow-y:scroll;height:200px;width:200px;">
                                    <c:forEach items="${orderd.change_communication}"
                                               var="change_communication">
                                        <span style='word-break: break-all; display: block;color:green'>${change_communication}</span>
                                        <hr>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </td>
                        <td
                                style="word-wrap: break-word; word-break: break-all; width: 240px;">
                            <font class="newsourceurl">

								<span id="spanurl${sd.index}">
											<p>?????????:<fmt:formatNumber value="${orderd.pd_profit_price}" pattern="#0.00"
                                                                     type="number" maxFractionDigits="2"/>
											%</p>
								</span>
                                <a href="/cbtconsole/editc/detalisEdit?pid=${orderd.goods_pid}" target="_blank">????????????</a>
                                <c:if test="${'8' == orderd.match_source}">
                                    <b style="color:red;">(B2C??????)</b>
                                </c:if>
									<br>
                                <span id="spanurl${sd.index}">
									<p > 1688??????????????????(RMB):<em style="color:red;">${orderd.price1688}</em> </p>
								</span>
                                <span id="spanurl${sd.index}">

									<p >  1688?????????:<em style="color:red;">${orderd.morder}</em> </p>
								</span>
								<span id="spanurl${sd.index}">
									<p> ????????????????????????(kg):<em style="color:red;">${orderd.final_weight}</em> </p>
								</span>
                                <span id="spanurl${sd.index}">
									<p style="width:200px;">??????????????????: ${orderd.goodsPName}</p>
								</span>
                                <span id="spanurl${sd.index}" style="color:red;">
									<p style="width:200px;">???????????????????????????(kg): ${orderd.od_total_weight}</p>
								</span>
                                <span>
									<p>????????????: ${orderd.buycount}</p>
								</span>
                                <span>
									<p>?????????ID??? <a target="_blank" style="color:red;" title="????????????????????????????????????"
                                                 href="/cbtconsole/website/shopBuyLog.jsp?shopId=${orderd.shop_id}">${orderd.shop_id}</a></p>
								</span>
                                <span>
										<p>??????????????????(RMB):${orderd.sourc_price}</p>
									<c:if test="${orderd.pidInventory >0}">
                                        <a target="_blank"
                                           href="/cbtconsole/StatisticalReport/goodsInventoryReport?pid=${orderd.goods_pid}">?????????????????????????????????</a><br>
                                    </c:if>
								<c:choose>
                                    <c:when test="${orderd.newValue!=null}">
                                        <a href="${orderd.newValue}" target="_blank">????????????????????????</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span>?????????????????????</span>
                                    </c:otherwise>
                                </c:choose>
										<p>${orderd.oremark}</p>
								</span>
                            </font>

                        </td>

                        <td id="odid${orderd.id}">????????????:${orderd.purchase_time}<br> <select
                                id="buyer${orderd.id}"
                                onchange="changeBuyer(${orderd.id},this.value);">
                            <option value=""></option>
                            <c:forEach var="aub" items="${aublist }">
                                <option value="${aub.id }">${aub.admName}</option>
                            </c:forEach>
                        </select><span id="info${orderd.id}"></span>
                           <input type="checkbox" id="ch${orderd.id}" onchange="pidchec('${orderd.id}','${order.orderNo}')">??????pid????????????(?????????????????????pid??????????????????????????????)

                            <!-- ??????????????????????????????-->
                            <div style="overflow-y:scroll;height:200px;width:200px;">
                                <div class="w-font">
                                    <font style="font-size: 15px;"
                                          id="rk_remark_${order.orderNo}${orderd.id}">${orderd.goods_info}</font>
                                </div>
                            </div>
                            <div class="w-margin-top">
                                <input type="button" value="??????" onclick="doReplay1('${order.orderNo}',${orderd.id},${orderd.goodsid});"
                                       class="repalyBtn"/>
                            </div>

                        </td>
                        <td style="word-break: break-all; width: 30px;"><input
                                type="checkbox" style="zoom:140%;" class="choose_chk" onchange="fnChange(${orderd.id},this);"
                            ${orderd.state == 2?'checked="checked" disabled="true"':''}
                                value="${orderd.id}"> <span>?????????????????????</span><input type="hidden"
                                                                                 value="${orderd.state}">
                            <br>
                            <input type="checkbox" style="zoom:140%;" name="replenishment"
                                   onchange="fnChange(${orderd.id},this);"
                                   value="${orderd.goodsid}"/><span>???????????????</span>
                            <input type="text"
                                   id="count_${orderd.goodsid}" style="width:50px;" value="????????????"
                                   onfocus="if (value =='????????????'){value =''}"
                                   onblur="if (value ==''){value='????????????'}"/><br/>
                            <input type="checkbox" style="zoom:140%;" name="Split_open">
                                  <span>???????????????</span>
                            <input type="text" style="width: 20px" id="Split_openNum${orderd.id}" onchange="getNum(${orderd.yourorder},this)" value="1"><span>????????????</span>
                        </td>
                    </tr>
                    </c:forEach>

                </c:forEach>



            </table>
        </div>
    </c:if>


    <!-- ???dropship?????? -->
    <c:if test="${isDropshipOrder==1}">
        <div id="dropshipOrderDiv">
            <c:forEach items="${orderDetail}" var="orderd" varStatus="sd">
                <div class="wrapper">
                    <div class="tab_box">
                        <c:set value="${orderd.dropshipid}" var="new_iorderid"/>
                        <c:if test="${new_iorderid != iorderid}">
                            <div class="tab_top">
                                <a href="#" class="toggle_btn">-</a> <strong>???????????????</strong> <a
                                    target='_blank'
                                    href='/cbtconsole/orderDetails/queryChildrenOrderDetail.do?orderNo=
												${orderd.dropshipid}&state=${order.state}&username=${order.userEmail}&paytime=&isDropshipOrder=1'
                                    id="2">${orderd.dropshipid} </a> <strong>?????????</strong>
                                <c:if test="${orderd.dropShipState!=0}">
                                    ${orderd.dropShipState==-1?'??????????????????':'' }${orderd.dropShipState==0?'????????????':'' }${orderd.dropShipState==1?'?????????':'' }${orderd.dropShipState==3?'?????????':'' }

                                    <c:if test="${order.dropShipState==4}">
                                        <!-- yyl ??????start -->
                                        <c:if test="${admuserinfo.roletype==0}">
                                            <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                                ??????/????????????
                                            </button>
                                        </c:if>
                                        <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">
                                            ??????????????????
                                            <input type="hidden" id="goods_img${orderd.goods_pid }"
                                                   value="${orderd.goods_img}"/>
                                            <input type="hidden" id="goods_url${orderd.goods_pid }"
                                                   value="${orderd.goods_url }"/>
                                            <input type="hidden" id="goodsname${orderd.goods_pid }"
                                                   value="${orderd.goodsname }"/>
                                            <input type="hidden" id="goodsprice${orderd.goods_pid }"
                                                   value="${orderd.goodsprice }"/>
                                            <input type="hidden" id="orderDetailId${orderd.goods_pid }"
                                                   value="${orderd.id }"/>
                                            <input type="hidden" id="carType${orderd.goods_pid }"
                                                   value="${orderd.car_type }"/>
                                        </button>
                                        ??????
                                        <br><font color="red">????????????????????? <font
                                            name="${orderd.goods_pid }ID">?????????</font></font>
                                    </c:if>

                                    ${orderd.dropShipState==5?'???????????????':'' }${orderd.dropShipState==2?'????????????':'' }${orderd.dropShipState==6?'??????????????????':'' }
                                </c:if>
                                <strong>?????????????????????</strong>--

                                <strong>???????????????</strong>-- <strong>???????????????</strong>--

                            </div>
                        </c:if>
                        <table id="${orderd.dropshipid}" class="tab_data"
                               style="display: block;">
                            <c:if test="${new_iorderid != iorderid}">
                                <tr class="detfretit">
                                    <td style="width: 5%;">Item</td>
                                    <td colspan="2" style="width: 20%;">??????</td>
                                    <td style="width: 10%;">????????????</td>
                                    <td style="width: 3%;">??????</td>
                                    <td style="width: 8%;">??????</td>
                                    <td style="width: 10%;">??????/??????</td>
                                    <td style="width: 5%;">????????????</td>
                                    <td style="width: 2%;">?????????</td>
                                    <td style="text-align: center;">????????????</td>
                                        <%--<td>????????????</td>--%>
                                </tr>
                            </c:if>
                            <c:set value="${orderd.dropshipid}" var="iorderid"/>
                            <tr id="goodsid_${orderd.goodsid}"
                                style="${orderd.state == 2?'background-color: #FF8484':''}">
                                <td style="width: 5%;">${sd.index+1}<br>${orderd.goodsid}
                                </td>
                                <td style="width: 5%;"><a> <img class="imgclass"
                                                                onclick="fnRend('${orderd.goods_url}')" width="200px"
                                                                height="200px;" src="/cbtconsole/img/wy/grey.gif"
                                                                data-original=" ${fn:replace(orderd.goods_img,'50x50','') }"
                                                                style="cursor: pointer;">
                                </a></td>
                                <td style="width: 8%;"><span
                                        style="color: red;">????????????:<br></span>${orderd.state == 2? "<br>???????????????":""}${orderd.goodsname}<br>
                                    <spna style="color:red;">??????????????????:</spna>
                                    <br> <span
                                            style="color: #00B1FF"> <c:if
                                            test="${not empty orderd.goods_type}">
                                        ${orderd.goods_type }
                                    </c:if></span><br> <span
                                            style="color: #8064A2; word-break: break-all;">${orderd.remark}</span>
                                    <c:if test="${not empty orderd.img_type}">
                                        <c:forEach items="${fn:split(orderd.img_type,'@')}"
                                                   var="img_type" varStatus="i">
                                            <c:if test="${fn:indexOf(img_type,'http') > -1}">
                                                <img style="max-width: 60px;max-height: 60px;" src="${img_type}">&nbsp;
                                            </c:if>
                                        </c:forEach>
                                    </c:if></td>
                                <td style="width: 5%;"><input type="hidden"
                                                              id="${orderd.dropshipid}${pbsi.index}_s"
                                                              value="${orderd.goodsprice}"/> <input type="hidden"
                                                                                                    id="${orderd.dropshipid}${pbsi.index}_sQuantity"
                                                                                                    value="${orderd.yourorder}"/>
                                    <input type="hidden"
                                           id="${orderd.dropshipid}${pbsi.index }_jg"
                                           value="${orderd.goodsprice}"/> <span style="color: red">?????????</span>
                                    <em id="number_${orderd.goodsid}"
                                        style="font-weight: bold;">${orderd.yourorder}</em>
                                    <em id="change_number_${sd.index}" style="color: red;"> <c:if
                                            test="${not empty orderd.change_number }">
                                        <br>
                                        ${orderd.change_number}
                                    </c:if>
                                    </em><br> <span style="color: red;">??????????????????:</span>${orderd.goodsprice}<em
                                            id="change_price_${sd.index}" style="color: red;"><c:if
                                            test="${not empty orderd.change_price }">
                                        <br>${orderd.change_price}</c:if></em> <c:if
                                            test="${not empty orderd.ffreight }">
                                        <br>
                                        <span style="color: red">??????????????????</span>
                                        ${orderd.ffreight}
                                    </c:if> <br> <span style="color: red">??????(???)???</span> <em
                                            id="orderd_delivery_${sd.index}">${orderd.delivery_time}</em>
                                    <em id="change_delivery_${sd.index}" style="color: red;">
                                        <c:if test="${not empty orderd.change_delivery }">
                                            <br>
                                            ${orderd.change_delivery}
                                        </c:if>
                                    </em><br> <span style="color: red">?????????</span> ${orderd.remark}
                                    <c:if test="${orderd.extra_freight != 0}">&nbsp;????????????:${orderd.extra_freight}</c:if>

                                </td>
                                <td style="width: 20%;"><c:set value="${orderd.state}"
                                                               var="ostate">
                                </c:set> <em> <c:if test="${ostate==0}">
                                    ${orderd.dropShipState==-1?'????????????':'' }${orderd.dropShipState==0?'????????????':'' }${orderd.dropShipState==1?'?????????':'' }${orderd.dropShipState==3?'?????????':'' }
                                    <c:if test="${order.dropShipState==4}">
                                        <!-- yyl ??????start -->
                                        <c:if test="${admuserinfo.roletype==0 }">
                                            <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                                ??????/????????????
                                            </button>
                                        </c:if>
                                        <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">
                                            ??????????????????
                                            <input type="hidden" id="goods_img${orderd.goods_pid }"
                                                   value="${orderd.goods_img}"/>
                                            <input type="hidden" id="goods_url${orderd.goods_pid }"
                                                   value="${orderd.goods_url }"/>
                                            <input type="hidden" id="goodsname${orderd.goods_pid }"
                                                   value="${orderd.goodsname }"/>
                                            <input type="hidden" id="goodsprice${orderd.goods_pid }"
                                                   value="${orderd.goodsprice }"/>
                                            <input type="hidden" id="orderDetailId${orderd.goods_pid }"
                                                   value="${orderd.id }"/>
                                            <input type="hidden" id="carType${orderd.goods_pid }"
                                                   value="${orderd.car_type }"/>
                                        </button>
                                        ??????
                                        <br>????????????????????? <font name="${orderd.goods_pid }ID">?????????</font>
                                    </c:if>


                                    ${orderd.dropShipState==5?'???????????????':'' }
                                    <c:if
                                            test="${orderd.dropShipState==3 && orderd.dropShipState==5}">
                                        <br>
                                        <br>
                                        <font color="red">???????????????</font>
                                    </c:if>
                                </c:if> <c:if test="${ostate==1 && orderd.checked==0}">
                                    ????????????,
                                    <c:if test="${orderd.goodstatus==6}">
                                        <font color="red">????????????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==5}">
                                        <font color="red">?????????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==4}">
                                        <font color="red">??????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==3}">
                                        <font color="red">??????????????????</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==1}">
                                        <font color="red">?????????</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==2}">
                                        <font color="red">?????????????????????</font>
                                    </c:if>
                                </c:if>
                                    <c:if test="${ostate==1 && orderd.checked==1}">
                                    <!-- yyl ??????start -->
                                    <c:if test="${admuserinfo.roletype==0}">
                                        <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                            ??????/????????????
                                        </button>
                                    </c:if>
                                    <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">??????????????????
                                        <input type="hidden" id="goods_img${orderd.goods_pid }"
                                               value="${orderd.goods_img}"/>
                                        <input type="hidden" id="goods_url${orderd.goods_pid }" value="${goods_url }"/>
                                        <input type="hidden" id="goodsname${orderd.goods_pid }"
                                               value="${orderd.goodsname }"/>
                                        <input type="hidden" id="goodsprice${orderd.goods_pid }"
                                               value="${orderd.goodsprice }"/>
                                        <input type="hidden" id="orderDetailId${orderd.goods_pid }"
                                               value="${orderd.id }"/>
                                        <input type="hidden" id="carType${orderd.goods_pid }"
                                               value="${orderd.car_type }"/>
                                    </button>
                                    </br> ????????????,
                                    <font color="green">????????????</font>
                                    </br>
                                    <font color="red">
                                        ?????????????????????<font name="${orderd.goods_pid }ID">?????????</font>
                                        </c:if>

                                </em> <input type="hidden" name="ostate" value="${ostate}">
                                    <em id="change_cancel_${sd.index}">${orderd.iscancel==1?'<br>????????????':''}</em>
                                    <em id="user_cancel_${sd.index}">${orderd.state == 2? "<br>?????????":""}</em>
                                    </br>?????????:<span style="color:red;">${orderd.shipno}</span> <br>????????????????????????:<span
                                            style="color:red;">${orderd.shipstatus}</span>
                                </td>
                                <input type="hidden" value="${isDropshipOrder}" id="h_isDropshipOrder1"/>
                                <td style="width: 8%; text-align: center;">
                                    <button
                                            onclick="pricechange('${orderd.orderid}',${orderd.goodsid},${orderd.goodsprice}+'',${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>????????????
                                    </button>
                                    <button
                                            onclick="deliverychange('${orderd.orderid}',${orderd.goodsid},${orderd.delivery_time}+'',${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>????????????
                                    </button>
                                    <button
                                            onclick="rationchange('${orderd.orderid}',${orderd.goodsid},${orderd.yourorder}+'',${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>????????????
                                    </button>
                                    <br/>
                                    <button
                                            onclick="cancelchange('${orderd.orderid}',${orderd.goodsid},${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>????????????
                                    </button>
                                    <button
                                            onclick="communicatechange('${orderd.orderid}',${orderd.goodsid})"
                                        ${order.state==5?'':'disabled=disabled' }>?????????
                                    </button>
                                    <!-- ????????????????????? -->
                                    <c:if test="${fn:length(orderd.change_communication)>0}">
                                        <br/>
                                        <hr>
                                        <c:if test="${orderd.ropType==5 && orderd.del_state==0 }">
                                            <button
                                                    onclick="fnResolve('${order.orderNo}',${orderd.goodsid})">???????????????
                                            </button>
                                            <label style="display: none; color: red;" id="msg2">(?????????)</label>
                                        </c:if>
                                        <div id="change_cmmunication_${orderd.goodsid}"
                                             style="overflow-y:scroll;height:200px;width:200px;">
                                            <c:forEach items="${orderd.change_communication}"
                                                       var="change_communication">
                                                <span style='word-break: break-all; display: block;color:green'>${change_communication}</span>
                                                <hr>
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </td>
                                <!-- ????????? -->
                                <td style="word-wrap: break-word; word-break: break-all; width: 180px; color: red; width: 10%;">
                                    <font class="newsourceurl">

                                        <a href="http://192.168.1.34:8086/cbtconsole/editc/detalisEdit?pid=${orderd.goods_pid}"
                                           target="_blank">????????????</a>
                                        <span id="spanurl${sd.index}">
											<p>??????????????????(RMB): ${orderd.price1688}</p>
										</span>
                                        <span id="spanurl${sd.index}">
											<p>1688????????????????????????(kg): ${orderd.weight1688}</p>
										</span>

                                        <span id="spanurl${sd.index}">
											<p>????????????:${orderd.buycount}</p>
										</span>
                                        <span id="spanurl${sd.index}">
											<p>????????????(RMB):${orderd.sourc_price}</p>
										<c:choose>
                                            <c:when test="${orderd.oldsourceurl!=null}">
                                                <a href="${orderd.oldsourceurl}" target="_blank">????????????????????????</a>
                                            </c:when>
                                            <c:when test="${orderd.newsourceurl !=null}">
                                                <a href="${orderd.newsourceurl}" target="_blank">????????????????????????</a>
                                            </c:when>
                                            <c:otherwise>
                                                <span>?????????????????????</span>
                                            </c:otherwise>
                                        </c:choose>
										<p>${orderd.oremark}</p> <br/>
										<a href="${orderd.oldUrl}" title='${orderd.oldUrl}'
                                           target="_blank">????????????</a> </span>
                                    </font>
                                </td>
                                <td style="color: red; width: 5%;">${orderd.purchase_time}</td>
                                <td id="odid${orderd.id}" style="width: 2%;">
                                    <select id="buyer${orderd.id}" onchange="changeBuyer(${orderd.id},this.value);">
                                        <option value=""></option>
                                        <c:forEach var="aub" items="${aublist }">
                                            <option value="${aub.id }">${aub.admName}</option>
                                        </c:forEach>
                                    </select><span id="info${orderd.id}"></span>
                                    <!-- ??????????????????????????????-->
                                    <div style="overflow-y:scroll;height:200px;width:200px;">
                                        <div class="w-font">
                                            <font style="font-size: 15px;"
                                                  id="rk_remark_${order.orderNo}${orderd.id}">${orderd.goods_info}</font>
                                        </div>
                                    </div>
                                    <div class="w-margin-top">
                                        <input type="button" value="??????"
                                               onclick="doReplay1('${order.orderNo}',${orderd.id},${orderd.goodsid});" class="repalyBtn"/>
                                    </div>

                                </td>
                                <td style="text-align: center; width: 5%;zoom:140%;"><input class="choose_chk"
                                        type="checkbox" onchange="fnChange(${orderd.id},this);"
                                    ${orderd.state == 2?'checked="checked" disabled="true"':''}
                                        value="${orderd.id}"><input type="hidden"
                                                                    value="${orderd.state}"></td>
                            </tr>
                        </table>
                        <script>jslr('${orderd.dropshipid}')</script>
                    </div>
                </div>
            </c:forEach>
        </div>

    </c:if>
    <div id="prinum"></div>
    <div class="peimask"></div>
</div>
</body>
<script type="text/javascript">
    $(function () {
        $(".toggle_btn").click(function () {
            $(this).parent().next().toggle();

            ($(this).html() == "+") ? $(this).html("-") : $(this).html("+");
            return false;
        });
    })
    /* ????????????????????????????????????????????????????????? yyl*/
    $(function () {
        var orderNo = getUrl('orderNo');
        var goodPids = '${lists}';
        $.ajax({
            type: "post",
            url: "/cbtconsole/goodsComment/searchIsComment",
            datatype: "json",
            data: {"orderNo": orderNo, "goodPids": goodPids},
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    var pid = data[i].goodsPid;
                    var controls = document.getElementsByName(pid + "ID");
                    for (var j = 0; j < controls.length; j++) {//cmid???????????????id,
                        controls[j].innerHTML = "????????? &nbsp;&nbsp;<button cmid='" + data[i].id + "' name='but" + pid + "' style='cursor:pointer' title=\"" + data[i].commentsContent + "\">????????????</button>"
                    }
                }
            }
        });

    })

    //?????? url ???????????????
    function getUrl(para) {
        var paraArr = location.search.substring(1).split('&');
        for (var i = 0; i < paraArr.length; i++) {
            if (para == paraArr[i].split('=')[0]) {
                return paraArr[i].split('=')[1];
            }
        }
        return '';
    }

    var sum = 0;
    admid = '<%=uid%>';
    if (admid != 40) {
        $(".shield").hide();
    }
    function blink(){
        var color="#f00|#0f0|#00f|#880|#808|#088|yellow|green|blue|gray";
        color=color.split("|");
        $(".blink").css('color',color[parseInt(Math.random()*color.length)]);
    }
    setInterval("blink()",1000);
</script>
</html>
<!-- ???????????????????????? -->
<%
    request.getSession().setAttribute("cgtz", 1);
%>