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
            $(".imgclass").lazyload({//加载图片
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
        //计算预估运费
        $(document).ready(function () {
            var sale = <%=request.getAttribute("sale")%>;  //销售额
            var buy = <%=request.getAttribute("buy")%>;  //采购额
            var country = <%=request.getAttribute("country")%>;  //国家id
            var volume = <%=request.getAttribute("volume")%>; //总体积
            var weight = <%=request.getAttribute("weight")%>; //总重量
            var goodsWeight = <%=request.getAttribute("goodsWeight")%>; //产品总重量
            var jq = <%=request.getAttribute("avg_jq")%>; //交期平均值
            var rate = <%=request.getAttribute("rate")%>; //汇率
            var allFreight = <%=request.getAttribute("allFreight")%>; //国际运费
            var order_state = <%=request.getAttribute("order_state")%>;
            var actual_freight = <%=request.getAttribute("actual_freight")%>;
            var piaAmount =<%=request.getAttribute("piaAmount")%>;
            var estimatefreight = <%=request.getAttribute("estimatefreight")%>; //接口生成预估运费
            var es_buyAmount = <%=request.getAttribute("es_prices")%>;//预计采购金额
            var transportcompany = '<%=request.getAttribute("transportcompany")%>';//运输公司
            var shippingtype = '<%=request.getAttribute("shippingtype")%>';//运输方式
            var allWeight =<%=request.getAttribute("allWeight")%>;//产品表中公式计算的重量，custom_benchmark_ready-final_weight
            var awes_freight = '<%=request.getAttribute("awes_freight")%>';//仓库称重后预估的运费
            var ac_weight = <%=request.getAttribute("ac_weight")%>; //eric称重重量包裹
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
            $("#esPidAmount").html("(包含预计国内运费:" + piaAmount + ")");
            $("#pay_price").html(sale + ";汇率:(" + rate + ")");
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
            var cname = "<%=request.getAttribute("countryName")%>"; //国际运输方式中的国家名
            var orderNo = "<%=request.getAttribute("orderNo")%>"; //订单号
            var oids = "<%=request.getAttribute("str_oid")%>"; //订单下所有的oid
            if (orderNo != "" && cname != "" && cname != null) {
                searchCountry(cname, orderNo);
            }
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
        //手动调整同pid采购人员
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
                        $("#info" + odid).text("执行成功");
                    } else {
                        $("#info" + odid).text("执行失败");
                    }
                    window.location.reload();
                },
                error: function (res) {
                    $("#info" + odid).text("执行失败,请联系管理员");
                }
            });

        }


        function Down_sample(orderno, email, paytime) {
            alert(orderno)
            if(orderno == null || orderno == ""){
                alert('获取订单号失败');
                return ;
            }
            var s=0;
            var orderDetail=${orderDetail};
            var OrderMap = new Array();
            $('input:checkbox[name=Split_open]').each(function(k) {
                if ($(this).is(':checked')) {
                    alert(k)
                    var arr=$("#Split_openNum"+orderDetail[k].id);
                    if (arr>orderDetail[k].yourorder){
                        alert("你输入的拆单数量不能大于订单数量")
                        return
                    }
                    var yourorder=orderDetail[k].yourorder-arr;
                    OrderMap.push({id:orderDetail[k].id,userid:orderDetail[k].userid,goodsid:orderDetail[k].goodsid,goodsname:orderDetail[k].goodsname,orderid:orderDetail[k].orderid
                        ,delivery_time:orderDetail[k].delivery_time,checkprice_fee:orderDetail[k].checkprice_fee,checkproduct_fee:orderDetail[k].checkproduct_fee,state:orderDetail[k].state,
                        fileupload:orderDetail[k].fileupload, yourorder:yourorder,goodsprice:orderDetail[k].goodsprice,freight:orderDetail[k].freight,downSample:arr})
                }
            })
            $.ajax({
                type:"post",
                url:"/cbtconsole/orderSplit/DownSample",
                dataType:"json",
                contentType : 'application/json;charset=utf-8',
                data:JSON.stringify(OrderMap),
                success:function(res){
                    alert(res)
                    if(res==0){
                        alert("删除失败  !")
                        return;
                    }else{
                        alert("删除成功 !");
                        // window.location.reload();
                    }
                }

            })
        }
    </script>

    <link type="text/css" rel="stylesheet"
          href="/cbtconsole/css/web-ordetail.css"/>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <title>订单详情</title>
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
           onclick="$('#repalyDiv1').hide();" style="float: right;">╳</a>
    </div>
    <input id="rk_orderNo" type="hidden" value="">
    <input id="rk_odid" type="hidden" value="">
    <input type="hidden" id="ordercountry_value">
    <input type="hidden" id="rk_goodsid">
    回复内容:
    <textarea name="remark_content" rows="8" cols="50" style="margin-top: 20px;" id="remark_content_"></textarea>
    <input type="button" id="repalyBtnId" onclick="saveRepalyContent()" value="提交回复">
</div>
<!-- 采购商品打分弹出层 -->
<div class="mod_pay3" style="display: none;width:720px;" id="supplierDiv">
    <div>
        <a href="javascript:void(0)" style="margin-left:700px" class="show_x" onclick="FncloseSupplierDiv()">╳</a>
    </div>
    <center>
        <h3 class="show_h3">采购供应商打分</h3>
        <table id="supplierDivInfos" class="imagetable">
            <thead>
            <tr>
                <td rowspan="5"><span style="color:red">[质量]</span>:1:差 2:较差 3: 一般 4:无投诉 5: 优质</td>
            </tr>
            <tr>
                <td>店铺</td>
                <td>质量</td>
                <td>是否有库存协议</td>
                <td>支持退货天数</td>
            </tr>
            <tr>
                <td>
                    <span id="su_shop_id"></span>
                </td>
                <td>
                    <select id="quality">
                        <option value="0">---请选择---</option>
                        <option value="1">1分</option>
                        <option value="2">2分</option>
                        <option value="3">3分</option>
                        <option value="4">4分</option>
                        <option value="5">5分</option>
                    </select>
                </td>
                <td>
                    <input name="protocol" type="radio" value="2"/>有<input name="protocol" type="radio" value="1"/>无
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
                    <input type="button" onclick="saveSupplier();" value="提交"/>
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
<!-- 采样商品打分弹出层 -->
<div class="mod_pay3" style="display: none;" id="supplierGoodsDiv">
    <div>
        <a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierGoodsDiv()">╳</a>
    </div>
    <center>
        <h3 class="show_h3">采样商品打分</h3>
        <table id="supplierGoodsDivInfos" class="imagetable">
            <thead>
            <tr>
                <td rowspan="5"><span style="color:red">[质量]</span>:1:差 2:较差 3: 一般 4:无投诉 5: 优质</td>
            </tr>
            <tr>
                <td>产品</td>
                <td>质量</td>
                <td>备注</td>
            </tr>
            <tr>
                <td>
                    <span id="su_goods_id"></span><br>
                    <span id="su_goods_p_id"></span>
                </td>
                <td>
                    <select id="g_quality">
                        <option value="0">---请选择---</option>
                        <option value="1">1分</option>
                        <option value="2">2分</option>
                        <option value="3">3分</option>
                        <option value="4">4分</option>
                        <option value="5">5分</option>
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
                    <input type="button" onclick="saveGoodsSupplier();" value="提交"/>
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
<!-- 评论框 start yyl-->
<div class="mod_pay3" style="display: none;" id="commentDiv1">
    <div>
        <a href="javascript:void(0)" class="show_x"
           onclick="$('#commentDiv1').hide();" style="float: right;">╳</a>
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
    评论内容:
    <textarea name="comment_content" rows="8" cols="50" style="margin-top: 20px;" id="comment_content_"></textarea>
    <input type="button" id="commentBtnId2" onclick="saveCommentContent()" value="提交评论">
</div>
<!-- 评论end -->
<div class="ormacon">
    <h3 class="ordmatit">订单详情管理</h3>
    <div>
        <input type="hidden" value="${order.pay_price_tow}"
               id="pay_price_tow"> <input type="hidden"
                                          value="${order.expect_arrive_time}" id="expect_arrive_time">
        <input type="hidden" value="${order.state}" id="order_state">
        <input type="hidden" value="${order.userName}" id="order_name">
        <input type="hidden" value="${payToTime}" id="payToTime">
        <table id="orderInfo" cellpadding="1" cellspacing="1" class="orderInfo" align="center">
            <tr class="ormatrname">
                <td class="ornmatd1">订单号:<input class="ormnum"
                                                            type="text" name="orderNo" id="orderNo" readonly="readonly"
                                                            value="${order.orderNo}"/>
                </td>
                <td class="ornmatd1"><em id="state_text">
                    ${order.state==-1 || order.state==6?'取消订单':'' }${order.state==1?'购买中':'' }
                    <c:choose>
                        <c:when test="${order.state==2 && order.checked==order.countOd}">
                            已到仓库,验货无误
                        </c:when>
                        <c:when test="${order.state==2 && order.no_checked>0}">
                            已到仓库，未校验
                        </c:when>
                        <c:when test="${order.state==2 && order.problem!=0}">
                            已到仓库，校验有问题
                        </c:when>
                        <c:when test="${order.state==2}">
                            已到仓库，状态错误
                        </c:when>
                    </c:choose>
                    ${order.state==0?'等待付款':'' }${order.state==3?'出运中':'' }
                    ${order.state==4?'完结':'' }
                    ${order.state==5?'确认价格中':'' }${order.state==7?'预订单':'' }
                </em>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <c:if test="${order.backList>0}">
                        <span style="color:Red">用户黑名单&nbsp;&nbsp;</span>
                    </c:if>
                    <c:if test="${order.payBackList>0}">
                        <span style="color:Red">支付账号黑名单&nbsp;&nbsp;</span>
                    </c:if>
                    <c:if test="${order.backAddressCount>0}">
                        <span style="color:Red">订单城市黑名单</span>
                    </c:if>
                </td>
                <td colspan="2">
                    <c:if test="${fn:length(orderNos) > 0}">
                        <span class="ornmatd1">关联订单:</span>
                        <div class="ormrelanum">
                            <c:forEach items="${orderNos}" var="order_correlation">
                                <a target="_blank"
                                   href="/cbtconsole/orderDetails/queryByOrderNo.do?&orderNo=${order_correlation}&state=${order.state}&username=${order.userName}">${order_correlation}</a>&nbsp;
                            </c:forEach>
                        </div>
                    </c:if><a style="color: red" href="/cbtconsole/customerRelationshipManagement/reorder?orderNo=${order.orderNo}">AddOrderToTest</a>
                </td>
            </tr>
            <!-- 客户订单信息显示 -->
            <tr>
                <td>
                    <span>name:</span>${order.userName}(ID:<em id="userId">${order.userid}</em>)
                    <a class="ordmlink" target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId=${order.userid}">客户页面</a>
                    <br>
                    <span style="color: red; display: none" id="other_id"></span>
                </td>
                <td>
                    <span>Email:<a href="mailto:${order.userEmail}">${order.userEmail}</a></span>
                </td>
                <td>
                    <span>客户公司名称:<em>${order.businessName}</em></span>
                </td>
                <td>
                    <span>国内交期:${order.deliveryTime}</span>
                </td>
            </tr>
            <c:if test="${order.state != 0}">
                <tr>
                    <td>
                        <span>付款时间:
                        <c:forEach
                                items="${pays}" var="pay">
                            <c:if test="${pay.orderid == order.orderNo}">
                                ${pay.createtime}
                            </c:if>
                        </c:forEach>
                        </span>
                    </td>
                    <td>
                        <span>到账确认:<em
                            id="dzConfirmtime">${order.dzConfirmtime}</em></span>
                    </td>
                    <td>
                        <span>付款金额:${order.pay_price}<em
                            id="currency">${order.currency}</em></span>
                    </td>
                    <td>
                        <span>还需付款${order.remaining_price}${order.currency}</span>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td>
                    <span>国际运输段:
                    <c:if test="${not empty order.mode_transport}">
                        ${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[1]:""}[
                        ${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[0]:""}]
                    </c:if></span>
                </td>
                <td>
                    <span>VIP级别:${order.gradeName} (${order.grade})</span>
                </td>
                <td>
                    <span>订单重量(kg):${order.volumeweight}</span><span style="color:red;">（免邮产品重量(kg):${feeWeight}）</span>
                </td>
                <td>
                    <%--<span class="ormtittd"  style="margin-left:100px">订单体积：</span><span style="color:red;">${order.svolume}</span>--%>
                </td>
            </tr>
            <tr>
                <c:if test="${order.state == 3 || order.state == 4 || order.state == 1 || order.state == 5 || order.state == 2}">
                    <td colspan="4">
                        <span>商品总金额:（<fmt:formatNumber
                            value="${order.product_cost+preferential_price}" pattern="#0.00" type="number"
                            maxFractionDigits="2"/>）</span>
                        <c:if test="${actual_ffreight_+foreign_freight>0}">
                            + 订单实收运费<span>（${actual_ffreight_+foreign_freight}）</span>
                        </c:if>
                        <c:if test="${service_fee>0}">
                            + 服务费 <span>（${service_fee}）</span>
                        </c:if>
                        <c:if test="${order.processingfee>0}">
                            + 店铺金额低于15手续费 <span>（${order.processingfee}）</span>
                        </c:if>
                        <c:if test="${actual_lwh>0}">
                            + 质检费 <span>（${actual_lwh}）</span>
                        </c:if>
                        <c:if test="${order.vatBalance>0}">
                            +双清包税金额<span>（${order.vatBalance}） </span>
                        </c:if>
                        <c:if test="${order.memberFee>0}">
                            + 会员费 <span>（${order.memberFee}）</span>
                        </c:if>
                        <c:if test="${order.actual_allincost>0}">
                            + 保险费 <span>（${order.actual_allincost}）</span>
                        </c:if>
                        <c:if test="${order.extra_freight>0}">
                            +运费<span>(${order.extra_freight})</span>
                        </c:if>
                        <c:if test="${order.actual_freight_c>0}">
                            +EUB handling fee<span>（${order.actual_freight_c}） </span>
                        </c:if>
                        <c:if test="${order.order_ac != 0}">
                            - 批量优惠金额<span>（${preferential_price}） </span>
                        </c:if>
                        <c:if test="${order.discount_amount>0}">
                            -混批优惠金额<span>（${order.discount_amount}）</span>
                        </c:if>
                        <c:if test="${order.cashback>0}">
                            -订单满200减免<span>（ ${order.cashback}） </span>
                        </c:if>
                        <c:if test="${order.extra_discount>0}">
                            -手动优惠<span>（ ${order.extra_discount}）</span>
                        </c:if>
                        <c:if test="${order.share_discount>0}">
                            -分享折扣<span>（ ${order.share_discount}）</span>
                        </c:if>
                        <c:if test="${order.couponAmount>0}">
                            -coupon优惠<span>（ ${order.couponAmount}）</span>
                        </c:if>
                        <c:if test="${order.coupon_discount>0}">
                            -返单优惠<span>（${order.coupon_discount}） </span>
                        </c:if>
                        <c:if test="${order.gradeDiscount>0}">
                            -${order.gradeName}<span>（${order.gradeDiscount}） </span>
                        </c:if>
                        <c:if test="${firstdiscount>0}">
                            -首单运费抵扣<span>（${firstdiscount}） </span>
                        </c:if>
                        =<b>实收金额</b><span class="ormtittdred ormtittdb"> （<fmt:formatNumber
                            value="${(order.product_cost+actual_ffreight_+foreign_freight+order.actual_allincost+order.processingfee+actual_lwh+order.memberFee+order.extra_freight-order.discount_amount+service_fee-order.cashback-order.share_discount-order.extra_discount-order.coupon_discount-order.order_ac + order.vatBalance-firstdiscount+order.actual_freight_c-order.gradeDiscount-order.couponAmount) >0 ?
									(order.product_cost+actual_ffreight_+foreign_freight+order.processingfee+order.extra_freight+actual_lwh+order.memberFee-order.discount_amount+service_fee-order.cashback-order.share_discount-order.extra_discount-order.coupon_discount-order.couponAmount-0-
									order.gradeDiscount + order.vatBalance - firstdiscount + order.actual_freight_c+order.actual_allincost) : 0.00 }"
                            pattern="#0.00" type="number" maxFractionDigits="2"/>）
						</span>
                        <a href="/cbtconsole/orderDetails/orderPayDetails.do?orderNo=${order.orderNo}&userId=${order.userid}"
                           target="_blank" style="text-decoration: none;">【到账详情 】</a>
                    </td>
                </c:if>
                <c:if test="${order.state == -1 || order.state == 6 || order.state == 0}">
                    <td class="ormtittd1" colspan="4">
                        <a href="/cbtconsole/orderDetails/orderPayDetails.do?orderNo=${order.orderNo}&userId=${order.userid}"
                           target="_blank" style="text-decoration: none;">【到账详情 】</a>
                    </td>
                </c:if>
            </tr>
            <tr>
                <td colspan="2">${order.paytypes}</td>
                <c:if test="${hasSampleOrder > 0}">
                    <td colspan="2"><b style="color: red;font-size: 16px;">有免费样品订单:<a href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${order.orderNo}_SP" target="_blank">${order.orderNo}_SP</a></b></td>
                </c:if>

            </tr>
            <tr>
                <td>
                    <c:if test="${order.state == 3 || order.state == 4 || order.state == 1 || order.state == 2}">
                        <a class="ordmlink" target="_blank"
                           href="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=${order.buyid}&userid=&orderno=${order.orderNo}&goodid=&date=&days=&state=&unpaid=0&pagesize=50&search_state=0">采购页面</a>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${order.state == 3 || order.state == 4 }">
                        <a class="ordmlink" target="_blank"
                           href="/cbtconsole/website/forwarderpageplck.jsp?orderid=${order.orderNo}&flag=true">出货页面</a>&nbsp;&nbsp;
                    </c:if>
                    <br/>
                    <c:if test="${order.state !=0 && order.state !=-1 && order.state !=6 }">
                        <a class="ordmlink" style="text-decoration: underline;cursor: pointer;"
                           onclick="jumpTracking('${order.orderNo}', '${order.dropShipList}')">Tracking</a>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${order.state !=6 }">
                        <a class="ordmlink" style="text-decoration: underline;cursor: pointer;"
                           onclick="jumpDetails('${order.orderNo}', '${order.dropShipList}')">客户的订单页</a>&nbsp;&nbsp;
                    </c:if>

                    <c:if test="${order.complainFlag >0 }">
                        <a class="ordmlink" target="_blank"
                           href="/cbtconsole/complain/searchComplainByParam?userid=${order.userid}&creatTime=&complainState=-1&username=&toPage=1&currentPage=1">有申诉</a>&nbsp;&nbsp;
                    </c:if>

                    <c:if test="${sampleschoice }">
                        <br /><br />
                        <span style="color: red; font-size: 16px;">
                            支付成功页面用户勾选的是否先发送样品: 是(先发送样品)
                        </span>
                    </c:if>

                </td>
                <td>
                    <c:if test="${evaluate.evaluate != null && evaluate.evaluate !=''}">
                        <span>用户评价：<span style="color:red;font-size:20px">${evaluate.evaluate}</span></span>
                    </c:if>
                </td>
                <td colspan="2">
                    <c:if test="${order.orderRemark != null && order.orderRemark!=''}">
                        <span>用户订单备注：<span style="color:red;font-size:20px">${order.orderRemark}</span></span>
                    </c:if>
                </td>
            </tr>

            <tr>
                <td colspan="4">
                    <c:if test="${fn:length(userIds) > 0}">
                        <span>同地址不同账号客户ID:</span>
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
                           id="msg">(通知成功)</label>
                    &nbsp;&nbsp; &nbsp;&nbsp;
                    <c:if test="${order.state!=3 || order.state!=4 }">
                        <input type="button"
                               style="position: fixed; bottom: 556px; right: 50px; width: 150px; height: 30px;"
                               id="closeOrder"
                               onclick="fnCloseOrder('${order.orderNo}',${order.userid},${order.pay_price},
                                       '${order.currency}',${order.order_ac},'${order.userEmail}','${order.email}',
                                   ${order.product_cost+preferential_price},${actual_ffreight_+foreign_freight},
                                   ${order.actual_weight_estimate},${isDropshipOrder })" value="取消订单">&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${count==1 }">
                        <input type="button"
                               style="position: fixed; bottom: 400px; right: 50px; width: 150px; height: 30px; border-color: red"
                               onclick="fnChangeProduct('${order.orderNo}')" value="建议替换">
                    </c:if>
                    &nbsp;&nbsp;
                    <c:if test="${isSplitOrder == 0 && order.state != 3 && order.state != 4}">
                        <input type="button"
                               style="position: fixed; bottom: 524px; right: 50px; width: 150px; height: 30px;"
                               id="split_order_btn"
                               onclick="fnSplitOrder('${order.orderNo}','${order.email}','${param.paytime}')"
                               value="拆单">&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${isSplitOrder == 1}">
                        <input type="button"
                               style="position: fixed; bottom: 524px; right: 50px; width: 150px; height: 30px;"
                               id="split_order_btn"
                               onclick="fnSplitDropShipOrder('${order.orderNo}','${order.email}','${param.paytime}')"
                        value="拆单">
                        &nbsp;&nbsp;
                    </c:if>
                    <%--<input type="button"--%>
                           <%--style="position: fixed; bottom: 492px; right: 50px; width: 150px; height: 30px;" id="openout" onclick="Down_sample('${order.orderNo}','${order.email}','${param.paytime}')"--%>
                            <%--value="拆样">--%>
                    <input type="button"
                           style="position: fixed; bottom: 492px; right: 50px; width: 150px; height: 30px;" id="itop" onclick="deliver('${order.orderNo}','${order.userid}','${param.paytime}','${order.product_cost+preferential_price}')"
                            value="送样">
                </td>
            </tr>
            <tr>
                <td>
                    <input type="button"
                           style="position: fixed; bottom: 460px; right: 50px; width: 150px; height: 30px;" id="open"
                           onclick="afterReplenishment()" value="售后补货">
                </td>
                <td>
                    <input type="button"
                           style="position: fixed; bottom: 425px; right: 50px; width: 150px; height: 30px;" id="spilt_num"
                           onclick="openSplitNumPage('${order.orderNo}')" value="数量拆单">
                </td>
                <%--<td>
                    <input type="button"
                           style="position: fixed; bottom: 388px; right: 50px; width: 150px; height: 30px;"
                           onclick="openOverSeaSplit('${order.orderNo}')" value="海外仓拆单">
                </td>--%>
                <td>分配采购（整单）： <select id="Abuyer" onchange="changeAllBuyer('${order.orderNo}',this.value)">
                    <option value=""></option>
                    <c:forEach var="aub" items="${aublist }">
                        <option value="${aub.id }">${aub.admName}</option>
                    </c:forEach>
                </select><span id="orderbuyer"></span></td>
                <td id="td_buyuser">
                    <span onclick="fnmessage();">分配订单销售人员：</span>
                    <select id="saler" name="saler" style="width: 110px;"></select>
                    <input type="submit" value="确认" id="saler_but"
                           onclick="addUser(${order.userid},'${order.userName}','${order.userEmail}')">
                    <span style="font-size: 15px; font-weight: bold; color: red;" id="salerresult"></span>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <span style="background-color: red">采购情况汇总</span>
        <div style="background-color: aqua">
            商品总数:<span style="color:red">${order.countOd}</span>;采购总数:<span style="color:red">${order.cg}</span>;
            入库总数:<span style="color:red">${order.rk}</span>;验货无误总数:<span style="color:red">${order.checkeds}</span>;
            验货疑问总数:<span style="color:red">${order.yhCount}</span>
        </div>
                </td>
            </tr>
        </table>

        <div id="remarkdiv">
            <div class="ormamark">
                <table style="border-collapse:separate; border-spacing:5px;">
                    <tbody>
                    <tr>
                        <td><input id="remarkbtn" type="button" value="添加备注内容(对内)"
                                   onclick="addremark('${order.orderNo}')"></td>
                        <td rowspan="2">备注内容:</td>
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
                        <td>快递跟踪号：</td>
                        <td><a href="/cbtconsole/website/forwarderpageplck.jsp?expressNo=${forwarder.express_no}"
                               target="_blank">${forwarder.express_no}</a></td>
                        <td>物流公司名称：</td>
                        <td><c:if test="${not empty forwarder.express_no}">${forwarder.logistics_name}</c:if></td>
                    </tr>
                </table>
            </div>
        </div>
        <div id="orderaddressdiv"
             style="margin-top: 10px; width: 1380px; height: 280px; border: 1px solid #0000FF; padding: 10px; display: block; float: left;margin-left:6px">
            <div style="float: left;margin-left:150px" id="div_address">
                订单地址:&nbsp;&nbsp;<br>
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
                            <input type="text" id="ordercountry" style="width: 180px" disabled="disabled" value="">
                            <%--<select id="ordercountry" style="width: 180px" disabled="disabled" >
                                <c:forEach items="${countryList }" var="zone">
                                    <option value="${zone.country}">${zone.country}</option>
                                </c:forEach>

                            </select>--%>
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
                        <td><input id="OrderAddress" type="button" value="修改订单地址"
                                   onclick="OrderAddress()"></td>
                    </tr>
                </table>
            </div>
            <div style="float: left; margin-left: 20px;margin-left:200px;" id="paypal_div">
                付款地址:&nbsp;&nbsp;<br>
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
                <td>客户实际支付金额(￥):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="pay_price" style="color:red;">-</span>
                </td>
                <%--<td rowspan="10">
                    运输公司:
                    <span style="color:red;" id="transportcompany">-</span>;
                    运输方式:<span style="color:red;" id="shippingtype">-</span><br>
                    预估国际运费=（商品重量和-首重）/续重*续重价格+首重价格<br>
                    录入采购金额=SUM(录入采购额)+产品国内运费<br>
                    实际采购金额=暂无（待上线）<br>
                    根据预估重量预估运费计算预估利润金额RMB（预估利润率%）=客户实际支付金额-实际预估采购金额-预估国际运费<br>
                    根据实际重量预估的运费实际利润金额RMB（实际利润率%）= 客户实际支付金额-录入采购金额-实际重量预估运费<br>
                    根据物流公司运费计算最终利润金额RMB（最终利润率%）= 客户实际支付金额-录入采购金额-最终录入的运费
                </td>--%>
                <td>
                    运输公司:
                    <span style="color:red;" id="transportcompany">-</span>;
                    运输方式:<span style="color:red;" id="shippingtype">-</span><br>
                </td>
            </tr>
            <tr>
                <td>预计采购金额(￥):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_price" style="color:red;">-</span>
                    <span style="color:red;" id="esPidAmount"></span>
                    <c:if test="${not empty tipprice}">
                        <span style="color:blue">(${tipprice})</span>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>录入采购金额(￥):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="buyAmount" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>实际采购金额(￥):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="acBuyAmount" style="color:red;">-</span>
                </td>
                <td>实际采购金额=暂无（待上线)</td>
            </tr>
            <tr>
                <td>预计重量(购物车重量)(kg):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_weight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>实际重量(仓库称重)(kg):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="ac_weight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>预计国际运费（按照产品重量+客户所选运输方式）(￥):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_freight" style="color:red;">-</span>(
                    <c:if test="${not empty order.mode_transport}">
                        ${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[0]:""}
                    </c:if>)
                </td>
                <td>预估国际运费=（商品重量和-首重）/续重*续重价格+首重价格
                    录入采购金额=SUM(录入采购额)+产品国内运费</td>
            </tr>
            <tr>
                <td>实际重量预估运费(￥):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="awes_freight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>最终录入的运费（物流公司）(￥):</td>
                <td style="text-align:center;vertical-align:middle;"><span id="ac_freight" style="color:red;">-</span>
                </td>
            </tr>
            <tr>
                <td>根据预估重量预估运费计算预估利润金额(￥)（预估利润率）:</td>
                <td style="text-align:center;vertical-align:middle;"><span id="es_profit" style="color:red;">-</span>(<span id="es_p" style="color:red;">-</span>)
                </td>
                <td>根据预估重量预估运费计算预估利润金额RMB（预估利润率%）=客户实际支付金额-实际预估采购金额-预估国际运费</td>
            </tr>
            <tr>
                <td>根据实际重量预估的运费实际利润金额(￥)（实际利润率）:</td>
                <td style="text-align:center;vertical-align:middle;width:300px"><span id="ac_profit" style="color:red;">-</span>(<span
                        id="ac_p" style="color:red;">-</span>)
                </td>
                <td>根据实际重量预估的运费实际利润金额RMB（实际利润率%）= 客户实际支付金额-录入采购金额-实际重量预估运费</td>
            </tr>
            <tr>
                <td>根据物流公司运费计算最终利润金额(￥)（最终利润率）:</td>
                <td style="text-align:center;vertical-align:middle;width:300px"><span id="end_profit"
                                                                                      style="color:red;">-</span>(<span
                id="end_p" style="color:red;">-</span>)
                </td>
                <td>根据物流公司运费计算最终利润金额RMB（最终利润率%）= 客户实际支付金额-录入采购金额-最终录入的运费</td>
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
            <input type="button" value="查看invoice"
                   onclick="window.open('/cbtconsole/autoorder/show?orderid=${param.orderNo}','_blank')">
        </c:if>
        <c:if test="${invoice=='2'}">
            <input type="button" value="查看invoice"
                   onclick="window.open('/cbtconsole/autoorder/shows?orderid=${param.orderNo}','_blank')">
        </c:if>
        <br/>
    </div>
    <br>
    <c:if test="${orderRecord.recommend > 0}">
    <span style="margin-left:160px;">分批发货推荐</span>
    <table border="1" style="margin-left:160px;">
    <tr>
    <td style="width: 320px;">订单销售价($)</td>
    <td style="width: 320px;">${orderRecord.cost}</td>
    </tr>
    <tr>
    <td style="width: 320px;">订单重量(kg)</td>
    <td style="width: 320px;">${orderRecord.weight }</td>
    </tr>
    <tr>
    <td style="width: 320px;">订单预估运费(￥)</td>
    <td style="width: 320px;">${orderRecord.feight }</td>
    </tr>
    <tr>
    <td style="width: 320px;">是否推荐分批出货</td>
    <td style="color: red;font-size: 24px;width: 320px;">${orderRecord.recommend==1?'推荐':'不推荐'}</td>
    </tr>
    </table>
    <br>
    </c:if>
    <c:if test="${isDropshipOrder == 0 || isDropshipOrder == 2 || isDropshipOrder == 3}">
        <!-- 不是dropship订单 -->
        <div style="width:1440px;">
            <table id="orderDetail" class="ormtable2" align="center">
                <tbody>
                <tr class="detfretit">
                    <td>商品编号/购物车id</td>
                    <td colspan="2">详情</td>
                    <td style="width:400px;">订单信息</td>
                    <td style="width: 300px;">状态</td>
                    <td>沟通</td>
                    <td>货源/沟通</td>
                    <td>采购员</td>
                    <td style="width:500px;">订单操作</td>
                </tr>
                </tbody>
                <c:forEach items="${orderDetail}" var="orderd" varStatus="sd">
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
                                       target="_blank">商品货源链接</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${orderd.oldUrl}"
                                       style="width: 200px; display: block; word-wrap: break-word;"
                                       target="_blank">商品货源链接</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style='width: 150px;'><span
                                style="color: red;">商品名称:</span><br>${orderd.state == 2? "<br>用户已取消":""}${orderd.goodsname}<br>
                            <span style="color: red;">客户下单规格:</span><br> <span
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
                        <td style="widows: 400px;">
                            <c:if test="${not empty orderd.inventoryRemark}">
                                <span style="background-color:chartreuse">库存备注:${orderd.inventoryRemark}</span><br>
                            </c:if>
                            <span style="color: red;">数量:</span><em
                                id="number_${orderd.goodsid}" style="font-weight: bold;">${orderd.yourorder}
                                ${orderd.goodsUnit}</em><br>
                            <em id="change_number_${sd.index}" style="color: red;"> <c:if
                                    test="${not empty orderd.change_number }">
                                <br>${orderd.change_number}
                            </c:if>
                            </em><br>
                            <span style="color:red">(
								<c:if test="${orderd.is_sold_flag !=0}">
                                    免邮商品
                                </c:if>
								<c:if test="${orderd.is_sold_flag ==0}">
                                    非免邮商品
                                </c:if>
									)</span>
                            <c:if test="${orderd.overSeaFlag > 0}">
                                <br>
                                <span style="color: red;background-color: #35de2a;">海外仓商品</span>
                                <br>
                            </c:if>
                            <span style="color: red">成交价格：</span>$
                                ${orderd.goodsprice} <em id="change_price_${sd.index}"
                                                         style="color: red;"> <c:if
                                test="${not empty orderd.change_price }">
                            <br>${orderd.change_price}</c:if></em><br/>
                            <br>
                            <c:if test="${orderd.bm_flag == 1 and orderd.isBenchmark == 1}">
                                <span style="color: red">ali产品价格：</span>
                                <span>$${orderd.ali_price}</span>
                                <span><br/><a target="_blank" href="${orderd.alipid }">ali产品链接</a></span>
                            </c:if>
                            <br/>
                            <span
                                    style="color: red;">备注:</span> ${orderd.remark} <c:if
                                test="${orderd.extra_freight != 0}">&nbsp;额外运费:${orderd.extra_freight}</c:if>
                            <em id="change_delivery_${sd.index}" style="color: red;">
                                <c:if test="${not empty orderd.change_delivery }">
                                    <br>
                                    新交期：${orderd.change_delivery}
                                </c:if>
                            </em><br>
                        </td>
                        <td><input type="hidden"
                                   value="${orderd.state},${order.state},${orderd.orsstate},${orderd.od_state},${orderd.checked}">
                            <c:set value="${orderd.state}" var="ostate"></c:set> <em>
                                <c:if test="${ostate==0}">
                                    ${order.state==-1?'取消订单':'' }${order.state==0?'等待付款':'' }${order.state==1?'购买中':'' }${order.state==3?'出运中':'' }
                                    <c:if test="${order.state==4}">
                                        <!-- yyl 评论start -->
                                        <c:if test="${admuserinfo.roletype==0 }">
                                            <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                                添加/修改评论
                                            </button>
                                        </c:if>
                                        <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">
                                            查看所有评论
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
                                        完结
                                        <font color="red">销售评论状态： <font name="${orderd.goods_pid }ID">未评论</font></font>
                                    </c:if>
                                    ${order.state==5?'确认价格中':'' }
                                    <c:if test="${orderd.orsstate==1 && order.state==1}">
                                        <br>
                                        <br>
                                        <font color="red">【已经确认货源】</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==0 && order.state==1 && orderd.iscancel!=1 && orderd.od_state!=13}">
                                        <br>
                                        <br>
                                        <font color="red">【还未录入货源】</font>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==12 && order.state==1}">
                                        <br>
                                        <br>
                                        <font color="red">【替换货源】</font>
                                        <c:if test="${not empty orderd.noChnageRemark }">
                                            <br>
                                            客户不同意备注:<span style="color: red;">${orderd.noChnageRemark}</span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==12 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">【替换货源】</font>
                                        <c:if test="${not empty orderd.noChnageRemark }">
                                            <br>
                                            客户不同意备注:<span style="color: red;">${orderd.noChnageRemark}</span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${orderd.od_state==13 && order.state==1}">
                                        <br>
                                        <br>
                                        <font color="red">【客户已经同意替换】</font>
                                    </c:if>
                                    <c:if test="${orderd.od_state==13 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">【客户已经同意替换】</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==5 && order.state==1 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">【问题货源】</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==5 && order.state==5 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">【问题货源】</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==6 && order.state==1 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">【已入库，但货有问题】</font>
                                    </c:if>
                                    <c:if
                                            test="${orderd.orsstate==6 && order.state==5 && orderd.iscancel!=1}">
                                        <br>
                                        <br>
                                        <font color="red">【已入库，但货有问题】</font>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==1 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">【货源已确认】</font>
                                    </c:if>
                                    <c:if test="${orderd.orsstate==3 && order.state==5}">
                                        <br>
                                        <br>
                                        <font color="red">【订单确认价格中】</font>
                                    </c:if>

                                </c:if>
                                <c:if test="${ostate==1 && not empty orderd.locationNum}">
                                    <br>
                                    <span style="background-color: #b3fbb5;font-size: 13px;">
                                        <b>库位号:</b><font color="red">${orderd.locationNum}</font></span>
                                </c:if>
                                    <%-- 									<c:if test="${ostate==1 }"> --%>
                                <!-- 										产品买了并已经到我们仓库 -->
                                    <%-- 									</c:if>  --%> <input type="hidden"
                                                                                                  value="${ostate},${orderd.checked},${orderd.goodstatus}">
                                <c:if test="${ostate==1 && orderd.checked==0}">
                                    已到仓库
                                    <c:if test="${orderd.goodstatus==6}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已校验品牌未授权</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==5}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已校验数量不对</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==4}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已校验有疑问</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==3}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已到仓库，已校验有破损</a>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==1}">
                                        <font color="red">,未校验</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==2}">
                                        <a style="color: red;" target="_Blank"
                                           href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已校验该到没到</a>
                                    </c:if>
                                </c:if>
                                <c:if test="${ostate==1 && orderd.checked==1 && orderd.goodstatus==1}">
                                <!-- yyl 评论start -->
                                <c:if test="${admuserinfo.roletype==0}">
                                    <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                        添加/修改评论
                                    </button>
                                </c:if>
                                <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">查看所有评论
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
                                </br>已到仓库
                                <!-- '${orderd.goods_img}','${orderd.goods_url }','${orderd.goodsname }','${orderd.goodsprice }',${orderd.goodsid }' -->
                                <font color="green">,验货无误</font>
                                </br>
                                <font color="red">
                                    销售评论状态： <font name="${orderd.goods_pid }ID">未评论</font>
                                    <!-- yyl 评论end -->
                                    </c:if>
                                    <c:if test="${ostate==1 && orderd.checked==1 && orderd.goodstatus !=1}">
                                    <font color="red" style="font-size:20px;">已到仓库，验货状态错误，请联系管理员！！</font>
                                    </c:if>
                            </em>


                            <input type="hidden" name="ostate" value="${ostate}"> <em
                                    id="change_cancel_${sd.index}">${orderd.iscancel==1?'<br>系统取消':''}</em>
                            <em id="user_cancel_${sd.index}">${orderd.state == 2? "<br>用户已取消":""}</em>
                            </br>运单号:<em>${orderd.shipno}</em> <br>最近国内物流状态:<em>${orderd.shipstatus}</em>
                        </td>
                        <td style="width: 200px;"><c:if test="${orderd.state!=2 }">
                            <button
                                    onclick="pricechange('${orderd.orderid}',${orderd.id},${orderd.goodsprice}+'',${sd.index},${isDropshipOrder})"
                                ${order.state==5||order.state==1?'':'disabled=disabled' }>价格更新
                            </button>
                            <button
                                    onclick="deliverychange('${orderd.orderid}',${orderd.id},${orderd.delivery_time}+'',${sd.index},${isDropshipOrder})"
                                ${order.state==5||order.state==1?'':'disabled=disabled' }>交期偏长
                            </button>
                            <button
                                    onclick="rationchange('${orderd.orderid}',${orderd.id},${orderd.yourorder}+'',${sd.index},${isDropshipOrder})"
                                ${order.state==5||order.state==1?'':'disabled=disabled' }>订量偏低
                            </button>
                            <br/>
                            <!--后台取消商品前台客户确认 start 5.9 屏蔽掉-->
                            <div class="shield">
                                <button
                                        onclick="cancelchange('${orderd.orderid}',${orderd.goodsid},${sd.index})"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>取消(客)
                                </button>
                                <!--后台取消商品前台客户确认  end 5.9 屏蔽掉-->
                                <!--直接取消商品不需要客户确认，直接给客户退钱 4.7 start 4.27 屏蔽掉-->
                                <button id="deleteGoods${orderd.orderid}${orderd.goodsid}"
                                        onclick="deleteOrderGoods('${orderd.orderid}',${orderd.goodsid},${orderd.purchase_state},${order.userid })"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>退钱
                                </button>
                            </div>
                            <!--直接取消商品不需要客户确认，直接给客户退钱 4.7 end-->
                            <c:if test="${isDropshipOrder!=1}">
                                <button
                                        onclick="communicatechange('${orderd.orderid}',${orderd.id},${isDropshipOrder})"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>需沟通
                                </button>
                            </c:if>
                            <c:if test="${isDropshipOrder==1}">
                                <button
                                        onclick="communicatechange('${orderd.orderid}',${orderd.id},${isDropshipOrder})"
                                    ${order.state==5||order.state==1?'':'disabled=disabled' }>需沟通
                                </button>
                            </c:if>
                            <c:if test="${orderd.shopFlag==1}">
                                <input type="button"
                                       onclick="openSupplierGoodsDiv('${orderd.goods_pid}','${orderd.shop_id}');"
                                       value="商品打分"/>
                                <input type="button" onclick="openSupplierDiv('${orderd.shop_id}');" value="店铺打分"/>
                            </c:if>
                            <h3 style="color: red;"
                                id="t_delectGoods${orderd.orderid}${orderd.goodsid}"></h3>
                            <h3 style="color: red;"
                                id="f_delectGoods${orderd.orderid}${orderd.goodsid}"></h3>
                            <input type="hidden" value="${isDropshipOrder1}" id="isDropshipOrder1">
                            <input type="hidden" value="${isDropshipOrder}">
                        </c:if>
                            <!-- 需沟通消息记录 -->
                            <c:if test="${fn:length(orderd.change_communication)>0}">
                                <br/>
                                <hr>
                                <c:if test="${orderd.ropType==5 && orderd.del_state==0 }">
                                    <button
                                            onclick="fnResolve('${order.orderNo}',${orderd.goodsid})">问题解决了
                                    </button>
                                    <label style="display: none; color: red;" id="msg2">(已解决)</label>
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
											<p>利润率:<fmt:formatNumber value="${orderd.pd_profit_price}" pattern="#0.00"
                                                                     type="number" maxFractionDigits="2"/>
											%</p>
								</span>
                                <a href="/cbtconsole/editc/detalisEdit?pid=${orderd.goods_pid}" target="_blank">编辑链接</a>
									<br>
                                <span id="spanurl${sd.index}">
									<p > 1688原始货源价格(RMB):<em style="color:red;">${orderd.price1688}</em> </p>
								</span>
                                <span id="spanurl${sd.index}">

									<p >  1688起订量:<em style="color:red;">${orderd.morder}</em> </p>
								</span>
								<span id="spanurl${sd.index}">
									<p> 单件原始货源重量(kg):<em style="color:red;">${orderd.final_weight}</em> </p>
								</span>
                                <span id="spanurl${sd.index}">
									<p style="width:200px;">采购货源标题: ${orderd.goodsPName}</p>
								</span>
                                <span id="spanurl${sd.index}" style="color:red;">
									<p style="width:200px;">合计加入购物车重量(kg): ${orderd.od_total_weight}</p>
								</span>
                                <span>
									<p>采购数量: ${orderd.buycount}</p>
								</span>
                                <span>
									<p>供应商ID： <a target="_blank" style="color:red;" title="查看该供应商采购历史记录"
                                                 href="/cbtconsole/website/shopBuyLog.jsp?shopId=${orderd.shop_id}">${orderd.shop_id}</a></p>
								</span>
                                <span>
										<p>实际采购价格(RMB):${orderd.sourc_price}</p>
									<c:if test="${orderd.pidInventory >0}">
                                        <a target="_blank"
                                           href="/cbtconsole/StatisticalReport/goodsInventoryReport?pid=${orderd.goods_pid}">有类似库存但规格不一致</a><br>
                                    </c:if>
								<c:choose>
                                    <c:when test="${orderd.newValue!=null}">
                                        <a href="${orderd.newValue}" target="_blank">实际采购货源链接</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span>未录入货源链接</span>
                                    </c:otherwise>
                                </c:choose>
										<p>${orderd.oremark}</p>
								</span>
                            </font>

                        </td>

                        <td id="odid${orderd.id}">采购时间:${orderd.purchase_time}<br> <select
                                id="buyer${orderd.id}"
                                onchange="changeBuyer(${orderd.id},this.value);">
                            <option value=""></option>
                            <c:forEach var="aub" items="${aublist }">
                                <option value="${aub.id }">${aub.admName}</option>
                            </c:forEach>
                        </select><span id="info${orderd.id}"></span>
                           <input type="checkbox" id="ch${orderd.id}" onchange="pidchec('${orderd.id}','${order.orderNo}')">：按pid分配采购(勾选当前订单此pid商品都分配给当前采购)

                            <!-- 消息备注列合并过来的-->
                            <div style="overflow-y:scroll;height:200px;width:200px;">
                                <div class="w-font">
                                    <font style="font-size: 15px;"
                                          id="rk_remark_${order.orderNo}${orderd.id}">${orderd.goods_info}</font>
                                </div>
                            </div>
                            <div class="w-margin-top">
                                <input type="button" value="回复" onclick="doReplay1('${order.orderNo}',${orderd.id},${orderd.goodsid});"
                                       class="repalyBtn"/>
                            </div>

                        </td>
                        <td style="word-break: break-all; width: 30px;"><input
                                type="checkbox" style="zoom:140%;" class="choose_chk" onchange="fnChange(${orderd.id},this);"
                            ${orderd.state == 2?'checked="checked" disabled="true"':''}
                                value="${orderd.id}"> <span>拆单、延后发货</span><input type="hidden"
                                                                                 value="${orderd.state}">
                            <br>
                            <input type="checkbox" style="zoom:140%;" name="replenishment"
                                   onchange="fnChange(${orderd.id},this);"
                                   value="${orderd.goodsid}"/><span>补货，数量</span>
                            <input type="text"
                                   id="count_${orderd.goodsid}" style="width:50px;" value="补货数量"
                                   onfocus="if (value =='补货数量'){value =''}"
                                   onblur="if (value ==''){value='补货数量'}"/><br/>
                            <input type="checkbox" style="zoom:140%;" name="Split_open">
                                  <span>拆样，数量</span>
                            <input type="text" style="width: 20px" id="Split_openNum${orderd.id}" onchange="getNum(${orderd.yourorder},this)" value="1"><span>优先发货</span>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:if>


    <!-- 是dropship订单 -->
    <c:if test="${isDropshipOrder==1}">
        <div id="dropshipOrderDiv">
            <c:forEach items="${orderDetail}" var="orderd" varStatus="sd">
                <div class="wrapper">
                    <div class="tab_box">
                        <c:set value="${orderd.dropshipid}" var="new_iorderid"/>
                        <c:if test="${new_iorderid != iorderid}">
                            <div class="tab_top">
                                <a href="#" class="toggle_btn">-</a> <strong>子订单号：</strong> <a
                                    target='_blank'
                                    href='/cbtconsole/orderDetails/queryChildrenOrderDetail.do?orderNo=
												${orderd.dropshipid}&state=${order.state}&username=${order.userEmail}&paytime=&isDropshipOrder=1'
                                    id="2">${orderd.dropshipid} </a> <strong>状态：</strong>
                                <c:if test="${orderd.dropShipState!=0}">
                                    ${orderd.dropShipState==-1?'后台取消订单':'' }${orderd.dropShipState==0?'等待付款':'' }${orderd.dropShipState==1?'购买中':'' }${orderd.dropShipState==3?'出运中':'' }

                                    <c:if test="${order.dropShipState==4}">
                                        <!-- yyl 评论start -->
                                        <c:if test="${admuserinfo.roletype==0}">
                                            <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                                添加/修改评论
                                            </button>
                                        </c:if>
                                        <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">
                                            查看所有评论
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
                                        完结
                                        <br><font color="red">销售评论状态： <font
                                            name="${orderd.goods_pid }ID">未评论</font></font>
                                    </c:if>

                                    ${orderd.dropShipState==5?'确认价格中':'' }${orderd.dropShipState==2?'已到仓库':'' }${orderd.dropShipState==6?'客户取消订单':'' }
                                </c:if>
                                <strong>商品销售金额：</strong>--

                                <strong>采购金额：</strong>-- <strong>预估运费：</strong>--

                            </div>
                        </c:if>
                        <table id="${orderd.dropshipid}" class="tab_data"
                               style="display: block;">
                            <c:if test="${new_iorderid != iorderid}">
                                <tr class="detfretit">
                                    <td style="width: 5%;">Item</td>
                                    <td colspan="2" style="width: 20%;">详情</td>
                                    <td style="width: 10%;">订单信息</td>
                                    <td style="width: 3%;">状态</td>
                                    <td style="width: 8%;">沟通</td>
                                    <td style="width: 10%;">货源/沟通</td>
                                    <td style="width: 5%;">采购时间</td>
                                    <td style="width: 2%;">采购员</td>
                                    <td style="text-align: center;">订单操作</td>
                                        <%--<td>消息备注</td>--%>
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
                                        style="color: red;">商品名称:<br></span>${orderd.state == 2? "<br>用户已取消":""}${orderd.goodsname}<br>
                                    <spna style="color:red;">客户下单规格:</spna>
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
                                           value="${orderd.goodsprice}"/> <span style="color: red">数量：</span>
                                    <em id="number_${orderd.goodsid}"
                                        style="font-weight: bold;">${orderd.yourorder}</em>
                                    <em id="change_number_${sd.index}" style="color: red;"> <c:if
                                            test="${not empty orderd.change_number }">
                                        <br>
                                        ${orderd.change_number}
                                    </c:if>
                                    </em><br> <span style="color: red;">客户成交价格:</span>${orderd.goodsprice}<em
                                            id="change_price_${sd.index}" style="color: red;"><c:if
                                            test="${not empty orderd.change_price }">
                                        <br>${orderd.change_price}</c:if></em> <c:if
                                            test="${not empty orderd.ffreight }">
                                        <br>
                                        <span style="color: red">购物车邮费：</span>
                                        ${orderd.ffreight}
                                    </c:if> <br> <span style="color: red">交期(新)：</span> <em
                                            id="orderd_delivery_${sd.index}">${orderd.delivery_time}</em>
                                    <em id="change_delivery_${sd.index}" style="color: red;">
                                        <c:if test="${not empty orderd.change_delivery }">
                                            <br>
                                            ${orderd.change_delivery}
                                        </c:if>
                                    </em><br> <span style="color: red">备注：</span> ${orderd.remark}
                                    <c:if test="${orderd.extra_freight != 0}">&nbsp;额外运费:${orderd.extra_freight}</c:if>

                                </td>
                                <td style="width: 20%;"><c:set value="${orderd.state}"
                                                               var="ostate">
                                </c:set> <em> <c:if test="${ostate==0}">
                                    ${orderd.dropShipState==-1?'取消订单':'' }${orderd.dropShipState==0?'等待付款':'' }${orderd.dropShipState==1?'购买中':'' }${orderd.dropShipState==3?'出运中':'' }
                                    <c:if test="${order.dropShipState==4}">
                                        <!-- yyl 评论start -->
                                        <c:if test="${admuserinfo.roletype==0 }">
                                            <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                                添加/修改评论
                                            </button>
                                        </c:if>
                                        <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">
                                            查看所有评论
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
                                        完结
                                        <br>销售评论状态： <font name="${orderd.goods_pid }ID">未评论</font>
                                    </c:if>


                                    ${orderd.dropShipState==5?'确认价格中':'' }
                                    <c:if
                                            test="${orderd.dropShipState==3 && orderd.dropShipState==5}">
                                        <br>
                                        <br>
                                        <font color="red">【采购中】</font>
                                    </c:if>
                                </c:if> <c:if test="${ostate==1 && orderd.checked==0}">
                                    已到仓库,
                                    <c:if test="${orderd.goodstatus==6}">
                                        <font color="red">已校验品牌未授权</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==5}">
                                        <font color="red">已校验数量不对</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==4}">
                                        <font color="red">已校验有疑问</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==3}">
                                        <font color="red">已校验有破损</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==1}">
                                        <font color="red">未校验</font>
                                    </c:if>
                                    <c:if test="${orderd.goodstatus==2}">
                                        <font color="red">已校验该到没到</font>
                                    </c:if>
                                </c:if>
                                    <c:if test="${ostate==1 && orderd.checked==1}">
                                    <!-- yyl 评论start -->
                                    <c:if test="${admuserinfo.roletype==0}">
                                        <button onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">
                                            添加/修改评论
                                        </button>
                                    </c:if>
                                    <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">查看所有评论
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
                                    </br> 已到仓库,
                                    <font color="green">验货无误</font>
                                    </br>
                                    <font color="red">
                                        销售评论状态：<font name="${orderd.goods_pid }ID">未评论</font>
                                        </c:if>

                                </em> <input type="hidden" name="ostate" value="${ostate}">
                                    <em id="change_cancel_${sd.index}">${orderd.iscancel==1?'<br>系统取消':''}</em>
                                    <em id="user_cancel_${sd.index}">${orderd.state == 2? "<br>已取消":""}</em>
                                    </br>运单号:<span style="color:red;">${orderd.shipno}</span> <br>最近国内物流状态:<span
                                            style="color:red;">${orderd.shipstatus}</span>
                                </td>
                                <input type="hidden" value="${isDropshipOrder}" id="h_isDropshipOrder1"/>
                                <td style="width: 8%; text-align: center;">
                                    <button
                                            onclick="pricechange('${orderd.orderid}',${orderd.goodsid},${orderd.goodsprice}+'',${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>价格更新
                                    </button>
                                    <button
                                            onclick="deliverychange('${orderd.orderid}',${orderd.goodsid},${orderd.delivery_time}+'',${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>交期偏长
                                    </button>
                                    <button
                                            onclick="rationchange('${orderd.orderid}',${orderd.goodsid},${orderd.yourorder}+'',${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>订量偏低
                                    </button>
                                    <br/>
                                    <button
                                            onclick="cancelchange('${orderd.orderid}',${orderd.goodsid},${sd.index})"
                                        ${order.state==5?'':'disabled=disabled' }>直接取消
                                    </button>
                                    <button
                                            onclick="communicatechange('${orderd.orderid}',${orderd.goodsid})"
                                        ${order.state==5?'':'disabled=disabled' }>需沟通
                                    </button>
                                    <!-- 需沟通消息记录 -->
                                    <c:if test="${fn:length(orderd.change_communication)>0}">
                                        <br/>
                                        <hr>
                                        <c:if test="${orderd.ropType==5 && orderd.del_state==0 }">
                                            <button
                                                    onclick="fnResolve('${order.orderNo}',${orderd.goodsid})">问题解决了
                                            </button>
                                            <label style="display: none; color: red;" id="msg2">(已解决)</label>
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
                                <!-- 添加列 -->
                                <td style="word-wrap: break-word; word-break: break-all; width: 180px; color: red; width: 10%;">
                                    <font class="newsourceurl">

                                        <a href="http://192.168.1.34:8086/cbtconsole/editc/detalisEdit?pid=${orderd.goods_pid}"
                                           target="_blank">编辑链接</a>
                                        <span id="spanurl${sd.index}">
											<p>原始货源价格(RMB): ${orderd.price1688}</p>
										</span>
                                        <span id="spanurl${sd.index}">
											<p>1688货源重量（单件）(kg): ${orderd.weight1688}</p>
										</span>

                                        <span id="spanurl${sd.index}">
											<p>采购数量:${orderd.buycount}</p>
										</span>
                                        <span id="spanurl${sd.index}">
											<p>采购价格(RMB):${orderd.sourc_price}</p>
										<c:choose>
                                            <c:when test="${orderd.oldsourceurl!=null}">
                                                <a href="${orderd.oldsourceurl}" target="_blank">实际采购货源链接</a>
                                            </c:when>
                                            <c:when test="${orderd.newsourceurl !=null}">
                                                <a href="${orderd.newsourceurl}" target="_blank">实际采购货源链接</a>
                                            </c:when>
                                            <c:otherwise>
                                                <span>未录入货源链接</span>
                                            </c:otherwise>
                                        </c:choose>
										<p>${orderd.oremark}</p> <br/>
										<a href="${orderd.oldUrl}" title='${orderd.oldUrl}'
                                           target="_blank">网站链接</a> </span>
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
                                    <!-- 消息备注列合并过来的-->
                                    <div style="overflow-y:scroll;height:200px;width:200px;">
                                        <div class="w-font">
                                            <font style="font-size: 15px;"
                                                  id="rk_remark_${order.orderNo}${orderd.id}">${orderd.goods_info}</font>
                                        </div>
                                    </div>
                                    <div class="w-margin-top">
                                        <input type="button" value="回复"
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
    /* 异步加载该订单下的商品是否已经销售评论 yyl*/
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
                    for (var j = 0; j < controls.length; j++) {//cmid是该商品的id,
                        controls[j].innerHTML = "已评论 &nbsp;&nbsp;<button cmid='" + data[i].id + "' name='but" + pid + "' style='cursor:pointer' title=\"" + data[i].commentsContent + "\">显示评论</button>"
                    }
                }
            }
        });

    })

    //获取 url 后的参数值
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
</script>
</html>
<!-- 采购页面跳转使用 -->
<%
    request.getSession().setAttribute("cgtz", 1);
%>