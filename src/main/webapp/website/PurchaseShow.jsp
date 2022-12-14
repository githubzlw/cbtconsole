<%@ page import="org.apache.shiro.web.session.mgt.WebSessionContext"%>
<%@ page import="com.cbt.website.bean.PurchasesBean"%>
<%@ page import="com.cbt.website.userAuth.bean.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<%@ page import="com.cbt.util.Util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%-- <% String admuserJson = Redis.hget(request.getSession().getId(), "admuser");  --%>
<%-- Admuser user = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);%> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.10.2.js"></script>
	<link rel="stylesheet" type="text/css"
		  href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		  href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
	<link rel="stylesheet" type="text/css"
		  href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
	<script type="text/javascript"
			src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
	<script type="text/javascript"
			src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
	<script type="text/javascript">
        var $190 = $;
	</script>
	<script type="text/javascript">
        jQuery.browser={};(function(){jQuery.browser.msie=false; jQuery.browser.version=0;if(navigator.userAgent.match(/MSIE ([0-9]+)./)){ jQuery.browser.msie=true;jQuery.browser.version=RegExp.$1;}})();
	</script>
	<link id="skin" rel="stylesheet"
		  href="${pageContext.request.contextPath}/js/warehousejs/jBox/Skins2/Green/jbox.css" />
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/warehousejs/jBox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/warehousejs/jBox/i18n/jquery.jBox-zh-CN.js"></script>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/warehousejs/thelibrary.js"></script>
	<link rel="stylesheet"
		  href="${pageContext.request.contextPath}/css/warehousejs/thelibrary.css"
		  type="text/css">
	<title>????????????</title>
	<style type="text/css">
		ul#img_sf_1 {
			list-style: none;
			float: left;
			display: inline;
		}

		ul#img_sf_1 li {
			float: left;
			width: 120px;
			height: 120px;
			display: inline;
			margin: 2px 2px 2px 5px;
		}

		ul#img_sf_11 li a {
			width: 120px;
			height: 120px;
			display: block;
		}

		ul#img_sf_1 li a img {
			width: 100%;
			height: 100%;
			border: 1px #999 solid;
		}

		ul#img_sf_1 li a:hover {
			z-index: 100;
			margin: -32px 0px 0px -32px;
			position: absolute;
		}

		ul#img_sf_1 li a img :hover {
			width: 600px;
			height: 600px;
			border: 1px #999 solid;
		}

		table {
			border-collapse: collapse;
		}

		.repalyBtn {
			height: 30px;
			width: 70px;
			background: #1c9439;
			border: 0px solid #dcdcdc;
			color: #ffffff;
			cursor: pointer;
		}

		.a {
			width: 100%;
		}

		.b {
			width: 98%;
			margin: 0 auto;
		}

		.c {
			font-size: 14px;
		}

		.cc {
			font-size: 20px;
			color: red;
			font-weight: bold;
		}

		.d {
			font-size: 17px;
			font-weight: bold;
			color: #708090;
		}

		.dd {
			font-size: 15px;
			font-weight: bold;
			word-wrap: break-word;
			word-break: break-all;
		}

		.e {
			font-size: 22px;
			font-weight: bold;
			font-family: "????????????"
		}

		.f {
			width: 171px;
			height: 24px;
			background: wheat;
			border: 2px solid #dfcaa4;
			margin-bottom: 3px;
			cursor: pointer;
		}

		.ffff {
			width: 120px;
			height: 24px;
			background: wheat;
			border: 2px solid #dfcaa4;
			margin-bottom: 3px;
			cursor: pointer;
		}

		.fff {
			width: 50px;
			height: 24px;
			background: wheat;
			border: 2px solid #dfcaa4;
			margin-bottom: 3px;
			cursor: pointer;
		}

		.ff {
			width: 80px;
			height: 24px;
			background: gray;
			border: 2px solid #dfcaa4;
			margin-bottom: 3px;
			cursor: pointer;
		}

		.g {
			width: 100px;
			height: 24px;
			background: wheat;
		}

		.gg {
			font-size: 17px;
			background: wheat;
		}

		.hh {
			font-size: 28px;
			text-decoration: none;
		}

		.h {
			width: 150px;
			height: 15px;
		}

		.i {
			width: 150px;
			height: 15px;
		}

		.style {
			margin: 0 auto;
			margin-top: 0;
			line-height: 50px;
			text-align: center;
			border-radius: 15px;
			border: 1px red;
		}

		.show_x {
			position: absolute;
			top: 5px;
			right: 15px;
			text-decoration: none;
		}

		.show_h3 {
			height: 20px;
			text-align: left;
		}

		.mod_pay1 {
			position: absolute;
			top: 0px;
			left: 0px;
			width: 100%;
			height: 200%;
			background-color: #666;
			opacity: .3;
			display: none;
		}

		.loading {
			position: fixed;
			top: 0px;
			left: 0px;
			width: 100%;
			height: 100%;
			color: #fff;
			z-index: 9999;
			background: #000 url(/cbtconsole/img/yuanfeihang/loaderTwo.gif)
			no-repeat 50% 300px;
			opacity: 0.4;
			display: none;
		}

		.mod_pay2 {
			width: 600px;
			height: 400px;
			position: fixed;
			background: snow;
			top: 200px;
			left: 5px;
			z-index: 1011;
			padding: 0;
			padding-bottom: 20px;
			z-index: 1011;
			display: none;
			overflow: auto;
			border: 15px solid #33CCFF;
		}

		.mod_pay3 {
			width: 720px;
			position: fixed;
			top: 120px;
			left: 15%;
			z-index: 1011;
			background: gray;
			padding: 5px;
			padding-bottom: 20px;
			z-index: 1011;
			border: 15px solid #33CCFF;
		}
		.qod_pay3 {
			/*width: 720px;*/
			/*height: 500px;*/
			/*top: 250px;*/
			/*left: 15%;*/
			/*overflow: auto;*/
			/*position: absolute;*/
			/*z-index: 1011;*/
			/*background: gray;*/
			/*padding: 5px;*/
			/*padding-bottom: 20px;*/
			/*z-index: 1011;*/
			/*border: 15px ;*/
			width: 720px;
			position: fixed;
			top: 250px;
			left: 15%;
			z-index: 1011;
			background: gray;
			padding: 5px;
			padding-bottom: 20px;
			z-index: 1011;
			border: 15px solid #33CCFF;

		}

		.mod_pay4 {
			width: 720px;
			position: fixed;
			top: 100px;
			left: 45%;
			z-index: 1011;
			background: gray;
			padding: 5px;
			padding-bottom: 20px;
			z-index: 1011;
			border: 15px solid #33CCFF;
		}

		.show_up {
			position: fixed;
			top: 90%;
			right: 35px;
		}

		.cai_remark {
			display: block;
			width: 100%;
			word-break: break-all;
		}

		.caitd01 {
			text-aligh: right;
		}

		.caitd02 {
			padding-left: 20px;
		}

		.remark {
			border-color: ccff66;
			width: 470px;
			height: 20px;
		}

		.remarktwo {
			border-color: ccff66;
			width: 170px;
			height: 60px;
		}

		.remarkBtn {
			width: 171px;
			height: 24px;
			background: #59f775;
			border: 2px solid #dfcaa4;
			margin-bottom: 3px;
			cursor: pointer;
		}

		.remarkAgainDiv {
			width: 500px;
			background: #34db51;
			text-align: center;
			position: fixed;
			left: 60%;
			top: 48%;
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
		.easyui-window{
			width: 100px;
			height: 300px;
			overflow:auto;
		}
		.model{
			position: absolute;
			width: 500px;
			height: 400px;
			margin-top: 10%;
			left: 50%;
			margin-left: -200px;
			background: #fff;
			border:1px solid #ddd;
		}
		.window, .window-shadow {
			position: fixed;}
	</style>
</head>
<script type="text/javascript">
    var odids="";
    $(document).ready(function(){
        odids = <%=request.getAttribute("odids")%>;
// 	getTipInfo(goodsids);
        genProcurement();
    });

    function getTipInfo(od_id){
        odids = <%=request.getAttribute("odids")%>;
        for(var i=0;i<odids.length;i++){
            var ops=odids[i];
            var odid=ops.odId;
            if(od_id==odid){
                document.getElementById("tip_"+od_id).innerHTML = "??????????????????";
            }
        }
    }
    function getGoodsCar(Goodsdata_id,userid,index,price,url,orderid){
        $.ajax({
            type:'post',
            url:'/cbtconsole/warehouse/getGoodsCar',
            dataType:'json',
            data:{Goodsdata_id:Goodsdata_id,userid:userid,index:index,price:price,url:url,orderid:orderid},
            success:function(data){
                if(data!='' && data!=null){
                    var typeHtml = "";
                    for(var i=0; i<data.length;i++){
                        if(i == 0){
                            var temp = "<a target='_blank' href='"+data[0].goods_url+"'>???????????????</a>";
                            $("#"+Goodsdata_id+index+"_ylj").html(temp);
                        }
                        typeHtml += (data[i].goods_type+"<br/>");
                    }
                    $("#"+Goodsdata_id+index+"_ygg").html("<br/>"+typeHtml);
                } else{
                    $("#"+Goodsdata_id+index+"_ygg").html("<br/>???");
                }
            }});
    }
    //?????????
    function dhyby(){
        var userid = $("#bh_userid").val();
        var odid = $("#bh_odid").val();
        var orderid = $("#bh_orderid").val();
        var goodsid = $("#bh_goodsid").val();
        var goods_url = $("#bh_goods_url").val();
        var goods_title = $("#bh_goods_title").val();

        var dhy_jg1 = $("#dhy_jg1").val();
        var dhy_hy1 = $("#dhy_hy1").val();
        var dhy_sl1 = $("#dhy_sl1").val();
        var dhy_id1 = $("#dhy_id1").val();

        var dhy_jg2 = $("#dhy_jg2").val();
        var dhy_hy2 = $("#dhy_hy2").val();
        var dhy_sl2 = $("#dhy_sl2").val();
        var dhy_id2 = $("#dhy_id2").val();

        $.ajax({
            type:"post",
            url:"/cbtconsole/warehouse/dhybh.do",//127
            dataType:"text",
            data:{userid:userid,
                orderid:orderid,goodsid:goodsid,
                goods_url:goods_url,goods_title:goods_title,
                dhy_jg1:dhy_jg1,dhy_hy1:dhy_hy1,
                dhy_jg2:dhy_jg2,dhy_hy2:dhy_hy2,
                dhy_sl1:dhy_sl1,dhy_sl2:dhy_sl2,
                dhy_id1:dhy_id1,dhy_id2:dhy_id2,odid:odid
            },
            success : function(data){
                if(Number(data)>0){
                    $("#dhy_hmsgid").html("????????????");
                    window.setTimeout(function(){
                        $("#divdhy").hide();
                    },1500);

                }else{
                    $("#dhy_hmsgid").html("????????????");
                }
            }
        });

    }

    //??????
    function insertOrderReplenishment(){
        var rep_type = $("input[name='bh_rep_type']:checked").val();
        if($("input[name='bh_rep_type']:checked").size()<1){
            // $("#hmsgid").html("?????????????????????");
            alert("?????????????????????");
            return ;
        }
        var buycount = $("#bh_buycount").val();
        var goods_price = $("#bh_goods_price").val();
        var bh_shop_id = $("#bh_shop_id").val();
        var goods_p_url = $("#bh_goods_p_url").val();
        var remark = $("#bh_remark").val();
        var userid = $("#bh_userid").val();
        var odid = $("#bh_odid").val();
        var orderid = $("#bh_orderid").val();
        var goodsid = $("#bh_goodsid").val();
        var goods_url = $("#bh_goods_url").val();
        var goods_title = document.getElementById("title_"+orderid+""+odid+"").innerHTML;
        $.ajax({
            type:"post",
            url:"/cbtconsole/purchase/insertOrderReplenishment.do",//127
            dataType:"text",
            data:{goods_title:goods_title,
                rep_type:rep_type,buycount:buycount,
                goods_price:goods_price,goods_p_url:goods_p_url,
                userid:userid,orderid:orderid,
                goods_url:goods_url,goodsid:goodsid,
                remark:remark,shop_id:bh_shop_id,odid:odid},
            success : function(data){  //????????????????????????
                if(Number(data)>0){
                	alert("????????????");
                    // $("#hmsgid").html("????????????");
                    $("#apbhdiv").hide();
                    // var dom=document.getElementById("is_replenishment");
                    // dom.setAttribute("style","background-color:red;") ;
                }else{
                	alert("????????????");
                    // $("#hmsgid").html("????????????");
                }
            }
        });
    }

    function getIsOfflinepurchase(orderid,goodsid){
        $.ajax({
            type:"post",
            url:"/cbtconsole/purchase/getIsOfflinepurchase.do",
            dataType:"text",
            data:{orderid:orderid,goodsid:goodsid},
            success : function(data){
                var objlist = eval("("+data+")");
                var html="";
                for(var i=0; i<objlist.length; i++){
                    html +="<tr><td width='10%'>" + objlist[i].shipno + "</td><td width='10%'>" + objlist[i].createtime + "</td><td width='10%'>" + objlist[i].admuserid + "</td><td width='50%'><a target='block' href='"+objlist[i].goods_p_url+"'>" + objlist[i].goods_p_url + "</a></td><td width='10%'>+ '"+objlist[i].tb_orderid + "'</td></tr>";
                }
                if(objlist.length<=0){
                    html += "<tr><td colspan='5' align='center'>???????????????????????????</td></tr>";
                }
                html +="</table>";
                var rfddd = document.getElementById("offlinepur_chase");
                rfddd.style.display = "block";
                $("#offlinepurchase").append(html);

            }
        });
    }

    function getIsReplenishment(orderid,goodsid){
        $.ajax({
            type:"post",
            url:"/cbtconsole/purchase/getIsReplenishment.do",
            dataType:"text",
            data:{orderid:orderid,goodsid:goodsid},
            success : function(data){  //????????????????????????
                var objlist = eval("("+data+")");
                var html="";
                for(var i=0; i<objlist.length; i++){
                    html +="<tr><td width='10%'>" + objlist[i].acount + "</td><td width='10%'>" + objlist[i].price + "</td><td width='10%'>" + objlist[i].createtime + "</td><td width='10%'>" + objlist[i].admuserid + "</td><td width='50%'><a href='" + objlist[i].goods_p_url + "' target='block'>" + objlist[i].goods_p_url + "</a></td>" + "<td width='10%'>" + objlist[i].remark + "</td></tr>";
                }
                if(objlist.length<=0){
                    html += "<tr><td colspan='6' align='center'>?????????????????????</td></tr>";
                }
                html +="</table>";
                var rfddd = document.getElementById("replenishment_record");
                rfddd.style.display = "block";
                $("#replenishment").append(html);

            }
        });
    }

    function Fncloserecord(){
        var rfddd = document.getElementById("replenishment_record");
        rfddd.style.display = "none";
        $("#replenishment tbody").html("");
    }
    function displayChangeLogInfo(){
        var rfddd = document.getElementById("displayChangeLog");
        rfddd.style.display = "none";
        $("#displayChangeLogs tbody").html("");
	}
    function FncloseBuyInfo(){
        var rfddd = document.getElementById("displayBuyInfo");
        rfddd.style.display = "none";
        $("#displayBuyInfos tbody").html("");
    }
    // ???????????????????????????????????????DIV
    function openSupplierDiv(shop_id){
        var rfddd = document.getElementById("supplierDiv");
        rfddd.style.display = "block";
        document.getElementById('su_shop_id').innerHTML="<a href='/cbtconsole/website/shopBuyLog.jsp?shopId="+shop_id+"' target='_blank'>"+shop_id+"</a>";// shop_id;
        $("#hidden_shopId").val(shop_id);
    }
    //??????????????????????????????DIV
    function openSupplierGoodsDiv(goods_pid,shop_id){
        var rfddd = document.getElementById("supplierGoodsDiv");
        rfddd.style.display = "block";
        document.getElementById('su_goods_id').innerHTML= goods_pid;
        document.getElementById('su_goods_p_id').innerHTML= shop_id;
        $("#g_quality").val("0");
        $("#su_g_remark").val("");
    }
    //???????????????????????????DIV
    function FncloseSupplierDiv(){
        var rfddd = document.getElementById("supplierDiv");
        rfddd.style.display = "none";
        document.getElementById('su_shop_id').innerHTML="";
        $("#hidden_shopId").val("");
        $("#quality").val("0");
        $("#su_data").val("");
        $("input[name=protocol]").attr("checked",false);
    }
    //????????????????????????DIV
    function FncloseSupplierGoodsDiv(){
        var rfddd = document.getElementById("supplierGoodsDiv");
        rfddd.style.display = "none";
        document.getElementById('su_goods_id').innerHTML="";
        document.getElementById('su_goods_p_id').innerHTML="";
        $("#g_service option[value='0']").attr("selected","selected");
        $("#g_quality option[value='0']").attr("selected","selected");
        $("#su_g_remark").val("");
    }
    // ?????????????????????????????????
    function saveSupplier(){
        var shop_id=$("#hidden_shopId").val();
        if(shop_id == null || shop_id == "" || shop_id == "0000"){
            alert("??????ID?????????????????????");
            return;
        }
        var service="0";
        var quality=$("#quality").val();
        var su_data=$("#su_data").val();
        var  protocol=$('input[name="protocol"]:checked').val();
        $.ajax({
            type: "POST",//????????????
            dataType:'json',
            url:'/cbtconsole/supplierscoring/saveproductscord',
            data:{quality:quality,rerundays:su_data,shopId:shop_id,inven:protocol},
            dataType:"json",
            success:function(data){
                if(data.flag == 'success'){
                    alert("???????????????????????????");
                    FncloseSupplierDiv();
                }else{
                    alert("???????????????????????????");
                }
            }
        });
    }
    //??????????????????????????????
    function saveGoodsSupplier(){
        var shop_id=document.getElementById("su_goods_p_id").innerHTML;
        var goods_pid=document.getElementById("su_goods_id").innerHTML;
        var quality=$("#g_quality").val();
        var remark=$("#su_g_remark").val();
        $.ajax({
            type: "POST",//????????????
            dataType:'json',
            url:'/cbtconsole/supplierscoring/saveproductscord',
            data:{quality:quality,remarks:remark,shopId:shop_id,goodsPid:goods_pid},
            dataType:"json",
            success:function(data){
                if(data.flag == 'success'){
                    alert("????????????????????????");
                    FncloseSupplierGoodsDiv();
                }else{
                    alert("????????????????????????");
                }
            }
        });
    }
    function Fncloserchase(){
        var rfddd = document.getElementById("offlinepur_chase");
        rfddd.style.display = "none";
        $("#offlinepurchase tbody").html("");
    }


    function showReplenishmen(orderid,goodsid){
        if($("#getIsReplenishmentId"+goodsid).is(":hidden")){
            $("#getIsReplenishmentId"+goodsid).show();
        }else{
            $("#getIsReplenishmentId"+goodsid).hide();
        }
    }
    function apbhcle(odid,orderNo,goodid){
        $("#bh_goods_title").val($("#dhy"+orderNo+odid).val());
        $("#apbhdiv input[type='text']").val("");
        $("#apbhdiv textarea").val("");
        $("#apbhdiv h1").html("");
        $("input[name='bh_rep_type']").attr("checked",false);
        $("#bh_submit").prop("disabled", false);
    }
    function apbhcle2(){
        $("#divdhy input[type='text']").val("");
        $("#divdhy textarea").val("");
    }

    function apbhdivHide(){
    	$('#apbhdiv').hide();
    	$("#idAddResourceOffline").prop("disabled", true);
	}

    $(function(){
        $("#hide_remarkDiv").click(function(){
            $(".remarkAgainDiv").hide();
            $("#remark_content").val("");
            $("#rk_orderNo").val("");
            $("#rk_od_id").val("");
            $("#rk_goodsid").val("");
            $("#isPush").prop("checked");
            $("#isPush").prop("disabled", false);
            $("#isPush").prop("checked", true);
        });
    })


    function FnClear(){
        $("#goodid").val("");
        $("#goodname").val("");
    }
    //?????? url ???????????????
    function getUrl(para){
        var paraArr = location.search.substring(1).split('&');
        for(var i = 0;i < paraArr.length;i++){
            if(para == paraArr[i].split('=')[0]){
                return paraArr[i].split('=')[1];
            }
        }
        return '';
    }
    function keepValue(){
        var catid=getUrl('catid');
        document.getElementById("goodid").value = getUrl('goodid');
        document.getElementById("search_state").value = getUrl('search_state');
        document.getElementById("page_size").value =getUrl('pagesize');
    }
    function FnSearch(orderarrs){
        var orderno = '${orderno}';
        var admid = '${admid}';
        var goodsName = $("#goodname").val();
        var search_state=$("#search_state").val();
        var goodsid=$("#goodid").val();
        var page_size=$("#page_size").val();
        window.location = "/cbtconsole/purchase/queryPurchaseInfo?pagenum="+orderarrs+"&orderid=0&admid="+admid+"&orderno="+orderno+"&days=365&unpaid=0&pagesize="+page_size+"&orderarrs=0&goodname="+goodsName+"&goodid="+goodsid+"&search_state="+search_state;
    }
    function submit(obj){
        var total_page=$("#total_page").html();
        if(Number(obj)>Number(total_page) || Number(obj)<=0){
            alert("?????????????????????");
            return;
        }
        var orderno = '${orderno}';
        var admid = '${admid}';
        var goodsName = $("#goodname").val();
        var search_state=$("#search_state").val();
        var goodsid=$("#goodid").val();
        var page_size=$("#page_size").val();
        window.location = "/cbtconsole/purchase/queryPurchaseInfo?pagenum="+obj+"&orderid=0&admid="+admid+"&orderno="+orderno+"&days=365&unpaid=0&pagesize="+page_size+"&orderarrs=0&goodname="+goodsName+"&goodid="+goodsid+"&search_state="+search_state;
    }
    function submitt(){
        var orderno = '${orderno}';
        var admid = '${admid}';
        var goodsName = $("#goodname").val();
        var search_state=$("#search_state").val();
        var goodsid=$("#goodid").val();
        var pagenum = document.getElementById("nummm").value;
        var page_size=$("#page_size").val();
        var total_page=$("#total_page").html();
        if(Number(pagenum)>Number(total_page) || Number(pagenum)<=0){
            alert("?????????????????????");
            return;
        }
        window.location = "/cbtconsole/purchase/queryPurchaseInfo?pagenum="+pagenum+"&orderid=0&admid="+admid+"&orderno="+orderno+"&days=365&unpaid=0&pagesize="+page_size+"&orderarrs=0&goodname="+goodsName+"&goodid="+goodsid+"&search_state="+search_state;
    }
    var useridd;
    var orderNoo;
    var goodidd;
    var od_idd;
    var goodsidd;
    var goodsdata_idd;
    var goods_urll;
    var googs_imgg;
    var goods_pricee;
    var goods_titlee;
    var googs_numberr;
    var currencyy;
    var goods_title;
    var cGoodstypee;
    var in_id;
    var remaining;
    var new_remaining;
    var lock_remaining;
    var in_id;
    //????????????
    function goodsResource(exchange_rate,userid,orderNo,od_id,goodid,goodsdata_id,goods_url,
    		googs_img,goods_price,googs_number,currency,shipping,goodssourcetype,
    		cGoodstype,issure,in_idi,remainingi,
    		lock_remainingi,new_remainingi,shop_ids,straight_address){
        document.getElementById("url_info").innerHTML="";
        useridd = userid;
        orderNoo = orderNo;
        goodidd = goodid;
        od_idd = od_id;
        goodsdata_idd = goodsdata_id;
        goods_urll = goods_url;
        googs_imgg = googs_img;
        goods_pricee = goods_price;
        goods_titlee = document.getElementById("id_"+orderNo+od_id).value;
        googs_numberr = googs_number;
        goodssourcetype=goodssourcetype;
        cGoodstypee=cGoodstype;
        currencyy = currency;
        issuree = issure;
        in_id=in_idi;
        new_remaining=new_remainingi;
        lock_remaining=lock_remainingi;
        remaining=remainingi;
        var rfddd = document.getElementById("rfddd");
        rfddd.style.display = "block";
        document.getElementById("shipping").value=shipping;
        var pri =  document.getElementById("chk2_"+orderNo+od_id).value;
        var reso =  document.getElementById("chk1_"+orderNo+od_id).value;
        $("#price").val(pri);
        $("#taobaospec").val(goodssourcetype);//??????
        $("#shop_id").val(shop_ids);
        $("#straight_address").val(straight_address);
        $("#resource").val(reso);
        $('#order_count').text(googs_numberr);
        if(in_idi!=null && in_idi!=""){
        	$("#can_remaining").text(lock_remainingi);
            $("#in_id").val(in_idi);
            var buycount=$("#tity_"+orderNo+od_id).text();
            if(lock_remainingi != null && lock_remainingi != '' && lock_remainingi != '0'){
            	$("#buycount").val(Number(googs_numberr)-Number(lock_remainingi));
            }else{
            	$("#buycount").val(Number(googs_numberr));
            }
        }else{
            $("#buycount").val(Number(googs_numberr));
            $("#can_remaining").text("0");
            $("#in_id").text("0");
        }

        var rmb = document.getElementById("usdprice");
        rmb.value = (goods_pricee * Number(exchange_rate)).toFixed(2) + "  RMB";//??????????????????
        $.ajax({
            type:'POST',
            url:'/cbtconsole/purchase/ShowRmark',
            data:{orderNo:orderNoo,goodsdataid:goodsdata_idd,goodid:goodidd,odid:od_idd},
            dataType:"json",
            success:function(remarks){
                if(remarks==null||remarks==""||remarks=="null"){
                } else {
                    var objs=eval(remarks);
                    var reason = document.all("otherReason");
                    if(objs.bargainRemark!=null){
                        reason[0].value=objs.bargainRemark;
                    }
                    if(objs.deliveryRemark!=null){
                        reason[1].value=objs.deliveryRemark;
                    }
                    if(objs.colorReplaceRemark!=null){
                        reason[2].value=objs.colorReplaceRemark;
                    }
                    if(objs.sizeReplaceRemark!=null){
                        reason[3].value=objs.sizeReplaceRemark;
                    }
                    if(objs.orderNumRemarks!=null){
                        reason[4].value=objs.orderNumRemarks;
                    }
                    if(objs.questionsRemarks!=null){
                        reason[5].value=objs.questionsRemarks;
                    }
                    if(objs.unquestionsRemarks!=null){
                        reason[6].value=objs.unquestionsRemarks;
                    }
                }
            }
        });
    }

    function check_url(){
        var orderNo = orderNoo;
        var goodid = goodidd;
        var old_goods_url = document.getElementById("url_"+orderNo+goodid).value;
        var resource = document.getElementById("resource").value;
        if(resource.indexOf("id=")>0){
            new_goods_itmeid=resource.split("id=")[1];
            if(new_goods_itmeid.indexOf("&")>0){
                new_goods_itmeid=new_goods_itmeid.split("&")[0];
            }
        }else if(resource.indexOf(".htm")>0){
            new_goods_itmeid=resource.split(".htm")[0].split("/")[resource.split(".htm")[0].split("/").length-1]
        }
        if(old_goods_url.length>5){
            if(old_goods_url==new_goods_itmeid){
                document.getElementById("url_info").innerHTML="????????????????????????";
            }
        }
    }

    function AddResource(type){
        var admid = '${admid}';
        var reason = document.all("otherReason");
        var rereason = "";
        for(var rs=0;rs<reason.length;rs++){
            if(rs==0&&reason[0].value!=null&&$.trim(reason[0].value)!=""){
                rereason=rereason+"????????????:"+reason[0].value+"//,";
            } if(rs==1&&reason[1].value!=null&&$.trim(reason[1].value)!=""){
                rereason=rereason+"????????????:"+reason[1].value+"//,";
            } if(rs==2&&reason[2].value!=null&&$.trim(reason[2].value)!=""){
                rereason=rereason+"????????????:"+reason[2].value+"//,";
            } if(rs==3&&reason[3].value!=null&&$.trim(reason[3].value)!=""){
                rereason=rereason+"????????????:"+reason[3].value+"//,";
            } if(rs==4&&reason[4].value!=null&&$.trim(reason[4].value)!=""){
                rereason=rereason+"????????????:"+reason[4].value+"//,";
            } if(rs==5&&reason[5].value!=null&&$.trim(reason[5].value)!=""){
                rereason=rereason+"???????????????:"+reason[5].value+"//,";
            }if(rs==6&&reason[6].value!=null&&$.trim(reason[6].value)!=""){
                rereason=rereason+"???????????????:"+reason[6].value+"//,";
            }
        }

        if(admid==0){
            alert("?????????????????????");
        } else {
            var userid = useridd;
            var orderNo = orderNoo;
            var goodid = goodidd;
            var od_id = od_idd;
            var goodsdata_id = goodsdata_idd;
            var goods_url = goods_urll;
            var googs_img = googs_imgg;
            var goods_price = goods_pricee;
            var goods_title = goods_titlee;
            var googs_number = googs_numberr;
            var currency = currencyy;
            var buycount = $.trim(document.getElementById("buycount").value);
            var price = $.trim(document.getElementById("price").value);
            if(buycount == null || buycount == "" || Number(buycount)<=0){
                alert("?????????????????????????????????0");
                return;
            }
            if(price==null || price==""){
                alert("????????????????????????");
                return;
            }
            var resource = document.getElementById("resource").value;
            var fdStart = resource.indexOf("http");
            var goodssourcetype = goodssourcetype;
            var cGoodstype=cGoodstypee;
            var issure = issuree;
            var new_goods_itmeid="";
            var state_flag="0";
            var old_goods_url = document.getElementById("url_"+orderNo+od_id).value;
            if(resource.indexOf("id=")>0){
                new_goods_itmeid=resource.split("id=")[1];
                if(new_goods_itmeid.indexOf("&")>0){
                    new_goods_itmeid=new_goods_itmeid.split("&")[0];
                }
            }else if(resource.indexOf(".htm")>0){
                new_goods_itmeid=resource.split(".htm")[0].split("/")[resource.split(".htm")[0].split("/").length-1]
            }
            if(old_goods_url.length>5){
                if(old_goods_url==new_goods_itmeid){
                    if(!window.confirm('????????????????????????,???????????????????????????????')){
                        return;
                    }else{
                        state_flag="1";
                    }
                }
            }
            if(rereason.indexOf("?????????")<=0&&(isNaN(buycount)||isNaN(price)||buycount==""||buycount==null||price==""||price==null)){
                alert("??????????????????????????????????????????????????????http?????????");
            } else {
                document.getElementById("rfddd").style.display = "none";
                $.ajax({
                    type:'POST',
                    url:'/cbtconsole/purchase/AddResource',
                    dataType : 'text',
                    data:{
                        type:type,
                        admid:admid,
                        userid:userid,
                        orderNo:orderNo,
                        goodid:goodid,
                        od_id:od_id,
                        goodsdata_id:goodsdata_id,
                        goods_url:goods_url,
                        googs_img:googs_img,
                        goods_price:goods_price,
                        goods_title:goods_title,
                        googs_number:googs_number,
                        price:price,
                        resource:resource,
                        buycount:buycount,
                        reason:rereason,
                        currency:currency,
                        goodssourcetype:goodssourcetype,
                        cGoodstype:cGoodstype,
                        issuree:issuree,
                        state_flag:state_flag
                    },
                    success:function(st){
                        $("#operatediv").css("display","none");
                        if(st==222){
                            alert("?????????????????????????????????");
                        }else if(st==111){
                            alert("??? > ?????? < ????????????????????????????????????????????????");
                            FncloseOut();
                        } else if(st==2){
                            alert("??? > ?????? < ????????????????????????????????????????????????");
                            FncloseOut();
                        } else {
                            document.getElementById("tity_" + orderNo + od_id).innerHTML=buycount;
                            $("input[id='url_"+orderNo+od_id+"']").val(new_goods_itmeid);
                            if(Number(type) == 2){
                                $("span[id^='issure"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            var t2 = "<input type='hidden' value='"+goods_title+"'/> " ;
                                            $(this).children().eq(1).html("??????");
                                        }
                                    }
                                });
                                $("span[id^='chk_"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            var t2 = "<input type='hidden' value='"+goods_title+"'/> " ;
                                            $(this).html(t2+resource);
                                        }
                                    }
                                });
                                //??????
                                $("input[id^='prc_"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            var t2 = "<input type='hidden' value='"+goods_title+"'/> " ;
                                            $(this).html(t2+price);
                                        }
                                    }
                                });
                                $("input[id^='chk1_"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            var t2 = "<input type='hidden' value='"+goods_title+"'/> " ;
                                            $(this).html(t2+resource);
                                        }
                                    }
                                });
                                $("span[id^='span_4"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                    }
                                });

                                $("span[id^='rmk2_"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            $(this).val(resource);
                                            var ts = $(this).html();
                                            if(ts=='?????????' || ts=='????????????'){
                                                $(this).html("??????");
                                            }
                                        }
                                    }
                                });
                                $("div[id^='click_hyqr_"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            $(this).children().eq(1).val("????????????");
                                            $(this).children().eq(1).attr("disabled",false);
                                        }
                                    }
                                });

                                $("div[id^='clickdiv_"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            $(this).children().eq(1).val("????????????");
                                            $(this).children().eq(1).attr("disabled",true);
                                        }
                                    }
                                });
                                $("div[id^='FnYiR_"+orderNo+"']").each(function(){
                                    var sBut = $(this).children("input:first").val();
                                    if (typeof(sBut) != "undefined"){
                                        sBut = sBut.substring(0,10);
                                        var t = goods_title.substring(0,10);
                                        if(t == sBut ){
                                            $(this).children().eq(1).val("??????");
                                            $(this).children().eq(1).attr("disabled",true);
                                        }
                                    }
                                });
                            }else{
                                document.getElementById("idAddResource").disabled=true;
                                document.getElementById("chk_"+orderNo+od_id).innerHTML=resource;
                                $("#prc_"+orderNo+""+od_id+"").val(price);
                                document.getElementById("chk1_"+orderNo+od_id).value=resource;
                                document.getElementById("chk2_"+orderNo+od_id).value=price;
                                document.getElementById("rmk_"+orderNo+od_id).innerHTML="??????";
                                document.getElementById("rmk2_"+orderNo+od_id).innerHTML=rereason;
                                document.getElementById("hideDetails_"+orderNo+od_id).style.display="block";
                                $("input[id='hyqr"+orderNo+od_id+"']").val("????????????");
                                $("input[id='hyqr"+orderNo+od_id+"']").attr("disabled",false);
                            }
                            FncloseOut();
                        }
                        location.reload();
                    }
                });
            }
        }
    }
    function FncloseOut(){
        document.getElementById("rfddd").style.display = "none";
        document.getElementById("user_remark").style.display = "none";
        document.getElementById("showdetail").style.display = 'none';
        document.getElementById("idAddResource").disabled=false;
        document.getElementById("price").value="";
        document.getElementById("resource").value='';
        document.getElementById("in_id").value="";
        document.getElementById("order_count").value='';
        document.getElementById("can_remaining").value='';
        var or = document.all("otherReason");
        for(var i=0;i<or.length;i++){
            or[i].value="";
        }
    }
    //????????????
    function jslr(orderNo){
        $.ajax({
            url:"/cbtconsole/orderInfo/queryOrderAmount",
            type : "post",
            dataType:"json",
            data :{"orderNo":orderNo},
            success:function(data){
                var json=data;
                $("#"+orderNo+"_pay_price").html(json.pay_price);
                $("#"+orderNo+"_buyAmount").html(json.acAmount);
                $("#"+orderNo+"_es_weight").html(json.weights);
                $("#"+orderNo+"_es_freight").html(json.allFreight);
                var actual_freight=json.actual_freight;
                var ac_profit="--";
                if(actual_freight == 654321){
                    actual_freight="--";
                }else if(actual_freight <= 0){
                    actual_freight="???????????????????????????";
                }
                if(json.awes_freight >0){
                    ac_profit=json.pay_price-json.acAmount-json.awes_freight;
                    ac_profit=ac_profit.toFixed(2);
                    var ac_lrl=((json.pay_price-json.acAmount-json.awes_freight)/json.pay_price*100).toFixed(2);
                    if(Number(ac_lrl)<=0){
                        ac_lrl="<span style='color:red;'>"+ac_lrl+"</span>";
                    }
                    ac_profit+="("+ac_lrl+"%)";
                }
                $("#"+orderNo+"_ac_freight").html(actual_freight);
                var es_profit=json.pay_price-json.es_price-json.allFreight;
                es_profit=es_profit.toFixed(2);
                var lrl=((json.pay_price-json.es_price-json.allFreight)/json.pay_price*100).toFixed(2);
                if(Number(lrl)<=0){
                    lrl="<span style='color:red;'>"+lrl+"</span>";
                }
                es_profit+="("+lrl+"%)";
                $("#"+orderNo+"_es_p").html(es_profit);
                $("#"+orderNo+"_ac_p").html(ac_profit);
            }
        });
    }
    var listmap= new Array();
    var listmapIndex = 0;


    //??????????????????
    function allQxQr(orderNo){
        var admid = '${admid}';
        $.ajax({
            type:"post",
            url:'/cbtconsole/purchase/allQxQrNew',
            dataType:"text",
            data:{"orderNo":orderNo,"admid":admid},
            success : function(data){
                if(data.length>0 && data.indexOf("&")>-1){
                    $.jBox.tip('????????????', 'success');
                    var orders =data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        document.getElementById('hyqr'+orderno+odid).value="????????????";
                        document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML="";
                        $("#rmk_"+orderno+odid).html("??????");
                        //???????????????????????????
                        $("#"+orderno+odid).attr("disabled", true);
                        jslr(orderno);
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('????????????', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    document.getElementById('hyqr'+orderno+odid).value="????????????";
                    document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML="";
                    $("#rmk_"+orderno+odid).html("??????");
                    //???????????????????????????
                    $("#"+orderno+odid).attr("disabled", true);
                    jslr(orderno);
                    window.location.reload();
                }else{
                    $.jBox.tip('????????????', 'fail');
                }
            }
        });
    }


    //??????????????????
    function allQr(orderNo){
        var admid = '${admid}';
        $.ajax({
            type:"post",
            url:'/cbtconsole/purchase/allQrNew',
            dataType:"text",
            data:{"orderNo":orderNo,"admid":admid},
            success : function(data){
                if(data.length>0 && data.indexOf("&")>-1){
                    $.jBox.tip('????????????????????????', 'success');
                    var orders=data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        var time=orders[i].split(";")[2];
                        document.getElementById('hyqr'+orderno+odid).value="????????????";
                        document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML=time;
                        $("#rmk_"+orderno+odid).html("???????????????");
                        $("#"+orderno+odid).removeAttr("disabled");
                        jslr(orderno);
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('????????????????????????', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    var time=data.split(";")[2];
                    document.getElementById('hyqr'+orderno+odid).value="????????????";
                    document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML=time;
                    $("#rmk_"+orderno+odid).html("???????????????");
                    $("#"+orderno+odid).removeAttr("disabled");
                    window.location.reload();
                    jslr(orderno);
                }else{
                    $.jBox.tip('????????????????????????', 'fail');
                }
            }
        });
    }
    //************????????????***************************
    function FnComfirmHyqr(siindex,userid,orderno,od_id,goodsid,goodsdata_id,goods_url,googs_img,goods_price,googs_number,purchaseCount,child_order_no,isDropshipOrder){
        var purchaseComfirmm = document.getElementById('hyqr'+orderno+od_id);
        var admid = '${admid}';
        var goods_title = document.getElementById("id_"+orderno+od_id).value;
        var issure = document.getElementById("rmk_"+orderno+od_id).innerHTML; //$("#rmk_"+orderno+goodsid)
        var newValue= document.getElementById("chk1_"+orderno+od_id).value;
        var oldValue = document.getElementById("chk2_"+orderno+od_id).value;
        if(admid==0){
            alert("?????????????????????");
            return;
        }else if(oldValue==null || oldValue==""){
            alert("?????????????????????");
            return;
        }else if(purchaseComfirmm.value=='????????????'){
            $.jBox.tip("????????????????????????", 'loading');
            document.getElementById('hyqr'+orderno+od_id).value="????????????";
            var ajaxTimeOut =$.ajax({
                type:'POST',
                timeout : 4000,
                url:'/cbtconsole/purchase/PurchaseComfirmTwoHyqr',
                data:{
                    adminid:admid,
                    userid:userid,
                    orderno:orderno,
                    od_id:od_id,
                    goodsid:goodsid,
                    goodsdataid:goodsdata_id,
                    goodsurl:goods_url,
                    googsimg:googs_img,
                    goodsprice:goods_price,
                    goodstitle:goods_title,
                    googsnumber:googs_number,
                    oldValue:oldValue,
                    newValue:newValue,
                    purchaseCount:purchaseCount,
                    child_order_no:child_order_no,
                    isDropshipOrder:isDropshipOrder
                },
                success:function(st){
                    if(st==111){
                        alert("??? > ?????? < ????????????????????????????????????????????????");
                    } else if(st==2){
                        alert("??? > ?????? < ????????????????????????????????????????????????");
                    } else if(st==100) {
                        var today = new Date();
                        //??????????????????  ?????? ????????????????????????
                        $("#rmk_"+orderno+od_id).html("???????????????");
                        $("#"+orderno+od_id).removeAttr("disabled");
                        var dd = today.getDate();
                        var mm = today.getMonth()+1; //?????????0??????????????????
                        var yyyy = today.getFullYear();
                        var hour = today.getHours();
                        var minutes = today.getMinutes() ;
                        var seconds = today.getSeconds();
                        if(dd<10) {
                            dd='0'+dd
                        }
                        if(mm<10) {
                            mm='0'+mm
                        }
                        today = yyyy+"-"+mm+"-"+dd+" "+hour+":"+minutes+":"+seconds;
                        document.getElementById("puechase_hyqr_time_"+orderno+od_id).innerHTML=today;
                        $.jBox.tip('??????', 'success');
                        jslr(orderno);
                    } else if(st==0){
                        $.jBox.tip('?????????', 'error');
                    }
                },complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('??????', 'success');
                    }
                }
            });
        } else {
            $.jBox.tip("????????????????????????", 'loading');
            document.getElementById('hyqr'+orderno+od_id).value="????????????";
            document.getElementById("puechase_hyqr_time_"+orderno+od_id).innerHTML="";
            var ajaxTimeOut =$.ajax({
                type:'POST',
                timeout : 4000,
                url:'/cbtconsole/PurchaseServlet?action=PurchaseComfirmOneQxhy&className=Purchase',
                data:{orderNo:orderno,odid:od_id,adminid:admid},
                success:function(i){
                    if(i!=0){
                        $("#rmk_"+orderno+od_id).html("??????");
                        //???????????????????????????
                        $("#"+orderno+od_id).attr("disabled", true);
                        $.jBox.tip('??????', 'success');
                        document.getElementById("puechase_hyqr_time_"+orderno+od_id).innerHTML="";
                        jslr(orderno);
                    } else {
                        $.jBox.tip('?????????', 'error');
                        document.getElementById('hyqr'+orderno+od_id).value="????????????";
                    }
                },complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('??????', 'success');
                    }
                }
            });
        }
    }


    var cgqrlistmap= new Array();
    var cgqrlistmapIndex = 0;

    //??????????????????
    function allQxcgQr(orderNo){
        var admid = '${admid}';
        $.ajax({
            type:"post",
            url:'/cbtconsole/purchase/allQxcgQrNew',
            dataType:"text",
            data:{"orderNo":orderNo,"admid":admid},
            success : function(data){
                if(data.length>0 && data.indexOf("&")>-1){
                    $.jBox.tip('????????????', 'success');
                    var orders=data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        document.getElementById(orderno+odid).value="????????????";
                        document.getElementById("puechase_time_"+orderno+odid).innerHTML="";
                        $("#rmk_"+orderno+odid).html("???????????????");
                        $("#hyqr"+orderno+odid).removeAttr("disabled"); //??????????????????
                        $("#ruku_id_"+orderno+odid).attr("disabled", true);  //?????????????????????
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('????????????', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    document.getElementById(orderno+odid).value="????????????";
                    document.getElementById("puechase_time_"+orderno+odid).innerHTML="";
                    $("#rmk_"+orderno+odid).html("???????????????");
                    $("#hyqr"+orderno+odid).removeAttr("disabled"); //??????????????????
                    $("#ruku_id_"+orderno+odid).attr("disabled", true);  //?????????????????????
                }else{
                    $.jBox.tip('????????????', 'fail');
                }
            }
        });
    }


    //??????????????????
    function allcgQr(orderNo, that){
        var admid = '${admid}';
        var websiteType = $(that).parent().find("select[name=websiteType]").val();
        $.ajax({
            type:"post",
            url:'/cbtconsole/purchase/allcgqrQrNew',
            dataType:"text",
            data:{"orderNo":orderNo,"admid":admid,"websiteType":websiteType},
            success : function(data){
                if(data.length>0 && data.indexOf("&")>-1){
                    $.jBox.tip('????????????', 'success');
                    var orders=data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        var time=orders[i].split(";")[2];
                        document.getElementById(orderno+odid).value="????????????";
                        document.getElementById("puechase_time_"+orderno+odid).innerHTML=time;
                        document.getElementById("inventory_"+orderno+odid).innerHTML="";
                        $("#rmk_"+orderno+odid).html("???????????????");
                        $("#hyqr"+orderno+odid).attr("disabled", true); //????????????????????????
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('????????????', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    var time=data.split(";")[2];
                    document.getElementById(orderno+odid).value="????????????";
                    document.getElementById("puechase_time_"+orderno+odid).innerHTML=time;
                    document.getElementById("inventory_"+orderno+odid).innerHTML="";
                    $("#rmk_"+orderno+odid).html("???????????????");
                    $("#hyqr"+orderno+odid).attr("disabled", true); //????????????????????????
                    window.location.reload();
                }else{
                    $.jBox.tip('????????????', 'fail');
                }
            }
        });
    }

    //****************????????????****************
    function FnComfirm(userid,orderno,od_id,goodsid,goodsdata_id,goods_url,googs_img,goods_price,googs_number,purchaseCount,child_order_no,isDropshipOrder, that){
        $.jBox.tip("????????????????????????", 'loading');
        var purchaseComfirmm = document.getElementById(orderno+od_id);
        var admid = '${admid}';
        var goods_title = document.getElementById("id_"+orderno+od_id).value;
        var issure = document.getElementById("rmk_"+orderno+od_id).innerHTML;
        var newValue= document.getElementById("chk1_"+orderno+od_id).value;
        var oldValue = document.getElementById("chk2_"+orderno+od_id).value;
        var goods_p_url=document.getElementById("chk_"+orderno+od_id+"").innerHTML;
        var websiteType = $(that).parent().find("select[name=websiteType]").val();

        if(admid==0){
            alert("?????????????????????");
        } else if(purchaseComfirmm.value=='????????????'){
            if(issure=='??????'||issure=='????????????'||issure=='?????????????????????'||issure=='???????????????' ||issure=='???????????????'){
                document.getElementById(orderno+od_id).value="????????????";
                var ajaxTimeOut =$.ajax({
                    type:'POST',
                    timeout : 4000,
                    url:'/cbtconsole/warehouse/purchaseConfirm',
                    data:{
                        orderno:orderno,
                        odid:od_id,
                        purchase_state:'3',
                        child_order_no:child_order_no,
                        isDropshipOrder:isDropshipOrder,
                        websiteType:websiteType
                    },
                    success:function(st){
                        if(st>0) {
                            $("#rmk_"+orderno+od_id).html("???????????????");
                            $("#hyqr"+orderno+od_id).attr("disabled", true);
                            document.getElementById("inventory_"+orderno+od_id).innerHTML="";
                            $("#ruku_id_"+orderno+od_id).removeAttr("disabled");
                            $.jBox.tip('?????????', 'success');
                            var today = new Date();
                            var dd = today.getDate();
                            var mm = today.getMonth()+1; //?????????0??????????????????
                            var yyyy = today.getFullYear();
                            var hour = today.getHours();
                            var minutes = today.getMinutes() ;
                            var seconds = today.getSeconds();
                            if(dd<10) {
                                dd='0'+dd
                            }
                            if(mm<10) {
                                mm='0'+mm
                            }
                            today = yyyy+"-"+mm+"-"+dd+" "+hour+":"+minutes+":"+seconds;
                            document.getElementById("puechase_time_"+orderno+od_id).innerHTML=today;
                        } else if(st==0){
                            $.jBox.tip('?????????', 'error');
                            document.getElementById(orderno+od_id).value="????????????";
                        } else if(st== -1 || st== "-1"){
                            $.jBox.tip('????????????????????????????????????????????????????????????', 'error');
                        }
                    },
                    complete : function(XMLHttpRequest,status){
                        if(status=='timeout'){
                            ajaxTimeOut.abort();
                            $.jBox.tip('?????????', 'success');
                        }
                    }
                });
            } else {
                $.jBox.tip('?????????,?????????????????????', 'fail');
            }
        } else {
            document.getElementById(orderno+od_id).value="????????????";
            document.getElementById("puechase_time_"+orderno+od_id).innerHTML="";
            var ajaxTimeOut =$.ajax({
                type:'POST',
                timeout : 4000,
                url:'/cbtconsole/warehouse/purchaseConfirm',
                data:{  orderno:orderno,
                    odid:od_id,
                    purchase_state:'1',
                    child_order_no:child_order_no,
                    isDropshipOrder:isDropshipOrder
                },
                success:function(i){
                    if(i!=0){
                        $("#rmk_"+orderno+od_id).html("???????????????");
                        $("#hyqr"+orderno+od_id).removeAttr("disabled");
                        $("#ruku_id_"+orderno+od_id).attr("disabled", true);
                        $.jBox.tip('?????????', 'success');
                    } else {
                        $.jBox.tip('?????????', 'error');
                        document.getElementById(orderno+od_id).value="????????????";
                    }
                },
                complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('?????????', 'success');
                    }
                }
            });
        }
    }
    function OtherSources(userid,orderNo,od_id,goodid,goodsdataid,goods_url,googs_img,goods_price,googs_number,currency){
        od_idd = od_id;
        useridd = userid;
        orderNoo = orderNo;
        goodidd = goodid;
        goodsdata_idd = goodsdataid;
        goods_urll = goods_url;
        googs_imgg = googs_img;
        goods_pricee = goods_price;
        goods_titlee = document.getElementById("id_"+orderNo+od_idd).value;
        googs_numberr = googs_number;
        currencyy = currency;
        $.ajax({
            type:'POST',
            url:'/cbtconsole/purchase/getOtherSources',
            data:{orderNo:orderNoo,goodid:goodidd,goods_url:goods_url,odid:od_id},
            success:function(otherUrl){
                if(otherUrl=="cancel"){
                    alert("??? > ??????/?????? < ??????????????????????????????????????????");
                } else {
                    var alertdiv = document.getElementById("alertdiv");
                    alertdiv.innerHTML = "<span style='color:red'>" + otherUrl + "</span>";
                    alertdiv.style.display = "block";
                    document.getElementById("bigestdiv").style.display = "block";
                    if(document.body.scrollHeight < window.screen.height){
                        $("#bigestdiv").css("height","100%");
                    } else if(document.body.scrollHeight > window.screen.height){
                        $("#bigestdiv").css("height",document.body.scrollHeight);
                    }
                }
            }
        });
    }
    function FnUseThis(pricee,i){
        var admid ='${admid}';
        var resource = document.getElementById("id_"+i).value;
        var buycount = $("#qquantity"+orderNoo+od_idd).val();
        if(admid == null || admid == "" || admid==0){
            alert("?????????????????????");
        } else {
            $("#operatediv").css("display","block");
            if(document.body.scrollHeight > window.screen.height){
                $("#operatediv").css("height",document.body.scrollHeight);
            }
            var userid = useridd;
            var orderNo = orderNoo;
            var goodid = goodidd;
            var goodsdata_id = goodsdata_idd;
            var goods_url = goods_urll;
            var googs_img = googs_imgg;
            var goods_price = goods_pricee;
            var goods_title = goods_titlee;
            var googs_number = googs_numberr;
            var currency = currencyy;
            var price = pricee;
            var rereason="";
            var fdStart = resource.indexOf("http");
            $.ajax({
                type:'POST',
                url:'/cbtconsole/purchase/AddResource',
                data:{
                    admid:admid,
                    od_id:od_idd,
                    userid:userid,
                    orderNo:orderNo,
                    goodid:goodid,
                    goodsdata_id:goodsdata_id,
                    goods_url:goods_url,
                    googs_img:googs_img,
                    goods_price:goods_price,
                    goods_title:goods_title,
                    googs_number:googs_number,
                    price:price,
                    resource:resource,
                    buycount:buycount,
                    reason:rereason,
                    currency:currency
                },
                success:function(admid){
                    document.getElementById("chk_"+orderNo+od_idd).innerHTML=resource;
                    $("#prc_"+orderNo+""+od_idd+"").val(pricee);
                    $("#chk2_"+orderNo+od_idd).val(pricee);
                    window.location.reload();
                }
            });
            var alertdiv = document.getElementById("alertdiv");
            alertdiv.style.display = "none";
            FnLoading();
            document.getElementById("bigestdiv").style.display = "none";
        }
    }

    function alertdivHide() {
        document.getElementById("alertdiv").style.display = "none";
        document.getElementById("bigestdiv").style.display = "none";
    }
    function FnShowUp() {
        document.getElementById("totopdiv1").style.display = "none";
        document.getElementById("totopdiv2").style.display = "block";
    }
    function FnHideUp() {
        document.getElementById("totopdiv1").style.display = "block";
        document.getElementById("totopdiv2").style.display = "none";
    }
    function hideTr(){
        var tab = document.getElementsByTagName("table");
        for(var j=0;j<tab.length;j++){
            var id = tab[j].getAttribute("id");
            var ttrds=document.all('ht'+id);
            if (ttrds == undefined) {
                continue;
			}
            for (var i = 2; i < ttrds.length;i++) {
                ttrds[i].style.display="none";
            }
        }
    }
    function judgePurchase(){
        var aa = parseFloat(document.getElementById("usdprice").value);
        var bb = parseFloat(document.getElementById("price").value);
        if(aa <= bb){
            document.getElementById("showdetail").style.display = 'block';
        } else {
            document.getElementById("showdetail").style.display = 'none';
        }
    }
    function FnYiRuKu(url,orderno,goodsid,odid){
        var admid = '${admid}';
        if(admid==0){
            alert("?????????????????????");
        } else {
            $.jBox.tip("????????????????????????", 'loading');
            admid = $("#admuser option:selected").text();
            var ajaxTimeOut =$.ajax({
                type:'POST',
                timeout : 4000,
                url:'/cbtconsole/warehouse/purchaseStorage',
                data:{orderno:orderno,goodid:goodsid,admid:admid,goodsurl:url,odid:odid},
                success:function(rk){
                    if(rk>0){
                        $("#"+orderno+odid).attr("disabled", true);
                        $("#hyqr"+orderno+odid).attr("disabled", true);
                        $("#rmk_"+orderno+odid).html("???????????????");
                        $.jBox.tip('?????????', 'success');
                        document.getElementById("ruku_id_"+orderno+odid).disabled=true;
                        window.setTimeout(function(){
                            document.getElementById("puechase_ruku_"+orderno+odid).innerHTML="";
                        },1500);
                    } else {
                        $.jBox.tip('?????????', 'error');
                    }
                },
                complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('?????????', 'success');
                    }
                }
            });
        }
    }
    function FnHideDetails(url,orderno,goodsid,odid){
        if(url.length==0){
            document.getElementById("hideDetails_"+orderno+odid).style.display='none';
        }
    }
    function FnLoading(){
        $("#operatediv").css("display","block");
        if(document.body.scrollHeight > window.screen.height){
            $("#operatediv").css("height",document.body.scrollHeight);
        } $("#operatediv").css("display","none");
    }



    function changImgOrUrl(location){
        var admId = '${admid}';
        if(admId == null ||admId == ""){
            $.jBox.tip('????????????ID??????', 'tip');
            return;
        }
        var parameters="";
        var page_size =  $("#page_size").val();
        var order1 =  document.getElementById("order_"+location).value;
        var goodid1 =  document.getElementById("goodsid_"+location).value;
        parameters+=order1+":"+goodid1;
        if(parseInt(location)+1<=page_size && document.getElementById("order_"+(parseInt(location)+1))!=null){
            var order2 =  document.getElementById("order_"+(parseInt(location)+1)).value;
            var goodid2 =  document.getElementById("goodsid_"+(parseInt(location)+1)).value;
            parameters+=","+order2+":"+goodid2;
        }
        if(parseInt(location)+2<=page_size && document.getElementById("order_"+(parseInt(location)+2))!=null){
            var order3 =  document.getElementById("order_"+(parseInt(location)+2)).value;
            var goodid3 =  document.getElementById("goodsid_"+(parseInt(location)+2)).value;
            parameters+=","+order3+":"+goodid3;
        }
        if(parseInt(location)+3<=page_size && document.getElementById("order_"+(parseInt(location)+3))!=null){
            var order4 =  document.getElementById("order_"+(parseInt(location)+3)).value;
            var goodid4 =  document.getElementById("goodsid_"+(parseInt(location)+3)).value;
            parameters+=","+order4+":"+goodid4;
        }
        if(parseInt(location)+4<=page_size && document.getElementById("order_"+(parseInt(location)+4))!=null){
            var order5 =  document.getElementById("order_"+(parseInt(location)+4)).value;
            var goodid5 =  document.getElementById("goodsid_"+(parseInt(location)+4)).value;
            parameters+=","+order5+":"+goodid5;
        }
        var url="/cbtconsole/purchase/addGoodNoForRedis";
        $.ajax({
            url:url,
            data:{admId:admId,parameters:parameters},
            type:"post",
            success:function(res){
            }
        });
    }

    function resetInsert(){
        document.getElementById("shipno").value="";
        document.getElementById("taobaoPrice").value="";
        document.getElementById("taobaoFeight").value="";
        document.getElementById("goodsQty").value="";
        document.getElementById("taobao_url").value="";
        document.getElementById("goods_sku").value="";
        document.getElementById("delivary_date").value="";
        document.getElementById("taobao_name").value="";
        document.getElementById("paydate").value="";
        document.getElementById("goods_imgs").value="";
        $("#taobao_id").val("");
        $("#shipno").val("");
        $("input[name='radio']").removeAttr('checked');
        fireDisable(false);
    }
    
    function fireDisable(isShow) {
		$("#idAddResourceOffline").prop("disabled", isShow);
		if(isShow){
			$("#on_notice").show();
		} else {
			$("#on_notice").hide();
		}
	}

    function AddinsertSources(goods_url,goods_num,goods_type,goods_img,price,orderid,goodsid,odid){
        var rfddd = document.getElementById("insertOrderInfo");
        rfddd.style.display = "block";
        document.getElementById("taobao_url").value=goods_url;
        document.getElementById("goods_sku").value=goods_type;
        document.getElementById("goodsQty").value=goods_num;
        document.getElementById("goods_imgs").value=goods_img;
        document.getElementById("taobaoPrice").value=price;
        document.getElementById("TbOrderid").value=orderid;
        document.getElementById("TbGoodsid").value=goodsid;
        document.getElementById("TbOdid").value=odid;
    }

    function checkTaoBaoOr1688() {
    	$("#on_notice").hide();
		var shipno = $.trim(document.getElementById("shipno").value);
		var taobao_id = $("#taobao_id").val();
		var selectType=$("#select_type").val();
		if(selectType == 1){
			if(!taobao_id){
				$("#on_notice").show().text("*??????????????????????????????");
			}
		} else if(shipno || taobao_id){
			// getTaobaoInfo(shipno, taobao_id);
			$("#on_notice").show().text("*???????????????1688???????????????????????????");
			return;
		} else {
			$("#on_notice").show().text("*???????????????1688???????????????????????????");
			return;
		}
	}

	function checkTaobaoInfo() {
		var taobao_id = $("#bh_1688_orderno").val();
		getTaobaoInfo('',taobao_id);
	}

	function getTaobaoInfo(shipno, taobao_id) {
    	fireDisable(true);
		$.ajax({
            type : 'POST',
            url : '/cbtconsole/purchase/getTaobaoInfo',
            dataType : 'text',
            data : {
                shipno : shipno,
				taobao_id : taobao_id
            },
            success : function(data) {
            	var json = eval("(" + data + ")");
            	if(json.ok){
            		if(json.rows){
            			fireDisable(false);
            			$("#taobao_id").val(json.rows.orderid);
            			$("#shipno").val(json.rows.shipno);
					} else{
            			$("#on_notice").show().text("*??????????????????1688??????????????????????????????");
					}
				}else{
            		$("#on_notice").show().text("*??????????????????1688??????????????????????????????");
				}
            }
        });
	}

    function insertSources(){
        var admName = '${admid}';
        var TbOrderid = $.trim(document.getElementById("TbOrderid").value);
        var TbGoodsid = $.trim(document.getElementById("TbGoodsid").value);
        var shipno = $.trim(document.getElementById("shipno").value);
        var odid = $.trim(document.getElementById("TbOdid").value);
        var taobaoPrice = $.trim(document.getElementById("taobaoPrice").value);
        var taobaoFeight = $.trim(document.getElementById("taobaoFeight").value);
        var goodsQty = $.trim(document.getElementById("goodsQty").value);
        var delivary_date = $.trim(document.getElementById("delivary_date").value);
        var taobao_url =$("#chk1_" + TbOrderid + odid+ "").val();
        var goods_sku = $.trim(document.getElementById("goods_sku").value);
        var paydate = $.trim(document.getElementById("paydate").value);
        var goods_imgs = $.trim(document.getElementById("goods_imgs").value);
        var taobao_name = $.trim(document.getElementById("title_" + TbOrderid+ odid + "").innerHTML);
        var temp=document.getElementsByName("radio");
        var type="";
        for (i=0;i<temp.length;i++){
            if(temp[i].checked){
                type=temp[i].value;
            }
        }
        if(type==null || type==""){
            alert("????????????????????????????????????????????????");
            return;
        }else if(type=="0"){
            taobaoPrice="0.00";
        }
        if (admName == 68) {
            admName = "cerongby4";
        } else if (admName == 57) {
            admName = "??????by2";
        } else if (admName == 58) {
            admName = "??????by3";
        } else if (admName == 53) {
            admName = "??????by1";
        } else if (admName == 59) {
            admName = "??????by5";
        }else if(admName == 61){
            admName="??????test";
        }
        var taobao_id = $("#taobao_id").val();
        var selectType=$("#select_type").val();
		if(selectType == 1){
			if(!taobao_id){
				alert("??????????????????????????????");
				return;
			}
		} else if(!shipno && !taobao_id){
			alert("???????????????1688???????????????????????????");
		}
		var orderno = "${param.orderno}";
        $.ajax({
            type : 'POST',
            url : '/cbtconsole/purchase/insertSources',
            dataType : 'text',
            data : {
            	orderno : orderno,
                shipno : shipno,
				taobao_id : taobao_id,
				selectType:selectType,
                taobaoPrice : taobaoPrice,
                taobaoFeight : taobaoFeight,
                goodsQty : goodsQty,
                taobao_url : taobao_url,
                odid : odid,
                goods_sku : goods_sku,
                taobao_name : taobao_name,
                paydate : paydate,
                goods_imgs : goods_imgs,
                delivary_date : delivary_date,
                admName : admName,
                TbOrderid : TbOrderid,
                TbGoodsid : TbGoodsid
            },
            success : function(date) {
                if (date == "2") {
                    var dom = document.getElementById("insert_"+ TbOrderid + odid);
                    dom.setAttribute("style", "background-color:red;");
                    alert("????????????");
                } else if (date == "3") {
                    alert("????????????");
                } else if (date == "1") {
                    alert("?????????????????????????????????????????????");
                } else {
                    alert("????????????");
                }
            }
        });
        FncloseInsert();
    }

    function FncloseInsert() {
        var rfddd = document.getElementById("insertOrderInfo");
        rfddd.style.display = "none";
        resetInsert();
    }

    function remarkAgain(orderNo, od_id, goodsid) {
        $("#rk_orderNo").val(orderNo);
        $("#rk_od_id").val(od_id);
        $("#rk_goodsid").val(goodsid);
        $("#isPush").prop("checked");
        $("#isPush").prop("disabled", false);
        $("#isPush").prop("checked", true);
        $(".remarkAgainDiv").show();
    }

    function saveRemarkAgain() {
        var orderNo = $("#rk_orderNo").val();
        var od_id = $("#rk_od_id").val();
        var goodsid = $("#rk_goodsid").val();
        var remarkContent = $("#remark_content").val();
        if (orderNo == null || orderNo == "") {
            alert("??????????????????????????????????????????");
            return;
        }
        if (od_id == null || od_id == "") {
            alert("??????????????????id???????????????????????????");
            return;
        }
        if (goodsid == null || goodsid == "") {
            alert("????????????id???????????????????????????");
            return;
        }
        if (remarkContent == null || remarkContent == "") {
            alert("?????????????????????");
            return;
        }
        var pushChecked = $("#isPush").is(':checked');
        if (pushChecked) {
            isPush = "Y";
        } else {
            isPush = "N";
        }
        $.ajax({
            type : "post",
            url : '/cbtconsole/PurchaseServlet?action=notePurchaseAgain&className=Purchase',
            dataType : "text",
            data : {
                "orderNo" : orderNo,
                "od_id" : od_id,
                "goodsid" : goodsid,
                "remarkContent" : remarkContent,
                "isPush" : isPush
            },
            success : function(data) {
                if (data == "ok") {
                    $(".remarkAgainDiv").hide();
                    $("#rk_orderNo").val("");
                    $("#rk_od_id").val("");
                    $("#rk_goodsid").val("");
                    $("#remark_content").val("");
                    //$("#isPush").attr("checked", true);
                    $("#isPush").prop("checked");
                    $("#isPush").prop("disabled", false);
                    $("#isPush").prop("checked", true);
                    FnSearch('1');
                } else {
                    alert(data);
                }
            }
        });

    }

    function useInventory(od_id, state, orderid, goodsid,inventory_count,googs_number,inventorySkuId,goodsUnit,seilUnit) {
        $.ajax({
            type : "post",
            url : '/cbtconsole/purchase/useInventory',
            dataType : "text",
            data : {
                "od_id" : od_id,
                "isUse" : state,
                "goodsid" : goodsid,
                "inventory_count" : inventory_count,
                "googs_number" : googs_number,
                "orderid" : orderid,
                "goodsUnit":goodsUnit,
                "seilUnit":seilUnit,
                "inventorySkuId" : inventorySkuId
            },
            success : function(data) {
            	$("#use_" + orderid + od_id).hide();
                if (state == 1) {
                    //????????????
                   // $("#inventory_" + orderid+ od_id).html("???????????????????????????");
                    $("#hyqr" + orderid + od_id).attr("disabled","true");
                    $("#hyqr" + orderid + od_id).attr("style","background-color:darkgray;");
                    $("#"+orderid + od_id).attr("style","background-color:darkgray;");
                    $("#" + orderid + od_id).attr("disabled","true");
                    location.reload() 
                } else {
                    $("#inventory_" + orderid+ od_id).html("????????????????????????");
                    $("#hyqr" + orderid + od_id).attr("disabled","false");
                    $("#hyqr" + orderid + od_id).attr("style","background-color:;");
                    $("#"+orderid + od_id).attr("style","background-color:;");
                    $("#" + orderid + od_id).attr("disabled","false");
                }
            }
        });
    }

    function getInventory(orderid, goodsid, odremark, purchase_state,odid) {
        if (odremark == "????????????") {
            document.getElementById("inventory_" + orderid + odid).innerHTML = "????????????,????????????";
            return;
        } else if (odremark.indexOf("????????????") > -1) {
            document.getElementById("inventory_" + orderid + odid).innerHTML = odremark;
            odremark = odremark.replace(/[^0-9]/ig, "");
            if (odremark == null || odremark == "null" || odremark == "") {
                odremark = 1;
            }
            document.getElementById("tity_" + orderid + odid).innerHTML = odremark;
            return;
        } else if (odremark.indexOf("???????????????") > -1) {
            document.getElementById("inventory_" + orderid + odid).innerHTML = "?????????0,???????????????";
            return;
        } else if (odremark.indexOf("???????????????") > -1) {
            document.getElementById("inventory_" + orderid + odid).innerHTML = odremark;
            return;
        } else if (odremark.indexOf("???????????????") > -1) {
            document.getElementById("inventory_" + orderid + odid).innerHTML = odremark;
            return;
        }
        $ .ajax({
                type : "post",
                url : '/cbtconsole/PurchaseServlet?action=getInventory&className=Purchase',
                dataType : "text",
                data : {
                    "orderNo" : orderid,
                    "goodsid" : goodsid,
                    "odid":odid
                },
                success : function(data) {
                    if (data.indexOf("&") > -1) {
                        var is_use = data.split("&")[1];
                        var can_remaining = data.split("&")[0];
                        if (Number(can_remaining) > 0
                            && Number(is_use) == 1) {
                           // $("#inventory_" + orderid+ goodsid).html("???????????????????????????");
                            $("#hyqr" + orderid+ odid).attr("disabled","true");
                            $("#hyqr" + orderid + odid).attr("style", "background-color:darkgray;");
                            $("#" + orderid + odid).attr("disabled","true");
                            $("#" + orderid + odid).attr("style", "background-color:darkgray;");
                        } else if (Number(can_remaining) > 0 && Number(is_use) == 0) {
                            $("#inventory_" + orderid+ odid).html( "");
                            $("#hyqr" + orderid+ odid).attr("disabled","true");
                            $("#use_" + orderid+ odid).show();
                            $("#hyqr" + orderid + odid) .attr("style", "background-color:darkgray;");
                            $("#" + orderid + odid).attr("disabled","true");
                            $("#" + orderid + odid).attr("style","background-color:darkgray;");
                        }
                    }
                }
            });
    }

    // ??????????????????
    function determineStraighthair(orderid,goodsid,odid){
        $.ajax({
            type : 'POST',
            async : false,
            url : '/cbtconsole/purchase/determineStraighthair',
            data : {
                'orderid' : orderid,
                'goodsid' : goodsid,
                "odid":odid
            },
            dataType : 'text',
            success : function(data) {
                if (data > 0) {
                    $.jBox.tip('????????????', 'success');
                    window.location.reload();
                } else {
                    $.jBox.tip('????????????', 'fail');
                }
            }
        });
    }


    function clearNoNum(obj) {
        //???????????????????????????
        if (isNaN(obj.value)) {
            obj.value = "";
        }
        if (obj != null) {
            //????????????????????????????????????
            if (obj.value.toString().split(".").length > 1
                && obj.value.toString().split(".")[1].length > 2) {
                alert("???????????????????????????");
                obj.value = "";
            }
        }
    }

    //????????????
    function doReplay1(orderid, goodsid,odid) {
        $("#remark_content_").val("");
        $("#rk_orderNo").val(orderid);
        $("#rk_goodsid").val(goodsid);
        $("#rk_odid").val(odid);
        var rfddd = document.getElementById("repalyDiv1");
        rfddd.style.display = "block";
    }
    //??????????????????
    function openSampleRemark(od_id) {
        $("#sample_remark").val("");
        $("#sample_od_id").val(od_id);
        console.log("od_id="+od_id);
        var rfddd = document.getElementById("repalyDiv2");
        rfddd.style.display = "block";
    }

    //????????????????????????
    function saveRepalyContent() {
        var orderid = $("#rk_orderNo").val();
        var goodsid = $("#rk_goodsid").val();
        var odid = $("#rk_odid").val();
        var text = $("#remark_content_").val();
        $ .ajax({
                type : 'POST',
                async : false,
                url : '/cbtconsole/PurchaseServlet?action=saveRepalyContent&className=Purchase',
                data : {'orderid' : orderid, 'goodsid' : goodsid,"type" : '1','text' : text, "odid":odid},
                dataType : 'text',
                success : function(data) {
                    if (data.length > 0) {
                        $("#rk_remark_" + orderid + odid + "").html(data);
                        $('#repalyDiv1').hide();
                    }
                }
            });
    }


    function genProcurement(){
        var sellLst = <%=request.getAttribute("aublist")%>;
        var sellsHtml='';
        sellsHtml += '<option value=""></option>';
        if(sellLst != null && sellLst.length >0){
            for(var i=0;i<sellLst.length;i++){
                sellsHtml += '<option value="'+sellLst[i].id+'">'+sellLst[i].admName+'</option>';
            }
        }
        $('select[id=buyuser1]').append(sellsHtml);
    }

    function changeOrderBuyer(orderid,admuserid){
        obj = document.getElementsByName("fp_"+orderid);
        var check_val = [];
        for(k in obj){
            if(obj[k].checked){
                check_val.push("'"+obj[k].value+"'");
            }
        }
        if(check_val==null || check_val==""){
            alert("??????????????????????????????????????????");
            $("#buyuser1").val("");
        }else{
            $.ajax({
                url:"/cbtconsole/orderDetails/changeOrderBuyer.do",
                type:"post",
                dataType:"json",
                data : {"orderid":orderid,"admuserid":admuserid,"odids":check_val.toString()},
                success:function(data){
                    if(data.ok){
                        $("#buyuserinfo").text("????????????");
                    }else{
                        $("#buyuserinfo").text("????????????");
                    }
                    window.location.reload();
                },
                error : function(res){
                    $("#buyuserinfo").text("????????????,??????????????????");
                }

            });
        }
    }

    //????????????????????????
    function changeBuyer(odid,buyid){
        $.ajax({
            url:"/cbtconsole/orderDetails/changeBuyer.do",
            type:"post",
            dataType:"json",
            data : {"odid":odid,"admuserid":buyid},
            success:function(data){
                if(data.ok){
                    $("#info"+odid).text("????????????");
                }else{
                    $("#info"+odid).text("????????????");
                }
                window.location.reload();
            },
            error : function(res){
                $("#info"+odid).text("????????????,??????????????????");
            }
        });
    }

    function checkAll(order_noo) {
        if ($190("#fpall_" + order_noo + "").prop("checked") == true) {
            $190("input[name='fp_" + order_noo + "']").prop('checked', true);//??????
            $190("#tabId tr:not(:first)").each(
                function() {
                    if ($190(this).css("display") == "none") {
                        $190(this).find("input[name='fp_" + order_noo + "']")
                            .prop('checked', false);
                    }
                });
        } else {
            $190("input[name='fp_" + order_noo + "']").prop('checked', false);//??????
        }
    }
    //????????????????????????doReplay1
    function addSampleRemark(){
        var remark=$("#sample_remark").val();
        var od_id=$("#sample_od_id").val();
        $.ajax({
            type:"post",
            url:"/cbtconsole/warehouse/addSampleRemark",
            dataType:"text",
            data:{od_id:od_id,remark:remark},
            success : function(data){
                if(data>0){
                    document.getElementById(od_id+"_remark").innerHTML = remark;
                    alert("??????????????????????????????");
                    var rfddd = document.getElementById("repalyDiv2");
                    rfddd.style.display = "none";
                    $("#sample_remark").val("");
                    $("#sample_od_id").val("");
                }else{
                    alert("??????????????????????????????");
                }
            }
        });
    }


    /**
	 ????????????????????????
     */
    function getDetailsChangeInfo(orderid,goodsid){
        $.ajax({
            type:"post",
            url:"/cbtconsole/purchase/getDetailsChangeInfo",
            dataType:"text",
            data:{"orderid":orderid,"goodsid":goodsid},
            success : function(data){
                var objlist = eval("("+data+")");
                var html="";
                for(var i=0; i<objlist.length; i++){
                    html +="<tr><td width='11%'>" + objlist[i].old_goodsPrice + "</td><td width='11%'><a target='_blank' href='"+objlist[i].old_url+"'>" + objlist[i].old_url + "</a></td><td width='10%'>" + objlist[i].new_goodsPrice + "</td><td width='11%'><a target='_blank' href='"+objlist[i].new_url+"'>" + objlist[i].new_url + "</a></td>" + "<td width='11%'>" + objlist[i].createtime + "</td><td width='11%'>" + objlist[i].admuserid + "</td></tr>";
                }
                if(objlist.length<=0){
                    html += "<tr><td colspan='6' align='center'>?????????????????????</td></tr>";
                }
                html +="</table>";
                var rfddd = document.getElementById("displayChangeLog");
                rfddd.style.display = "block";
                $("#displayChangeLogs").append(html);
            }
        });
	}

    function displayBuyLog(goods_pid,car_urlMD5){
        $.ajax({
            type:"post",
            url:"/cbtconsole/warehouse/displayBuyLog",
            dataType:"text",
            data:{"goods_pid":goods_pid,"car_urlMD5":car_urlMD5},
            success : function(data){
                var objlist = eval("("+data+")");
                var html="";
                for(var i=0; i<objlist.length; i++){
                    html +="<tr><td width='11%'><a target='_blank' href='"+objlist[i].car_url+"'>" + objlist[i].goodsname.substring(0,15) + "</a></td><td width='11%'>" + objlist[i].goods_p_price + "</td><td width='11%'>" + objlist[i].buycount + "</td><td width='10%'>" + objlist[i].admName + "</td><td width='11%'>" + objlist[i].createtime + "</td>" + "<td width='11%'>" + objlist[i].company + "</td><td width='11%'>" + objlist[i].level + "</td><td width='12%'><span id='"+objlist[i].od_id+"_remark'>" + objlist[i].remark + "</span></td><td width='11%'><input type='button' onclick='openSampleRemark("+objlist[i].od_id+");' value='??????????????????' /></td></tr>";
                }
                if(objlist.length<=0){
                    html += "<tr><td colspan='8' align='center'>?????????????????????</td></tr>";
                }
                html +="</table>";
                var rfddd = document.getElementById("displayBuyInfo");
                rfddd.style.display = "block";
                $("#displayBuyInfos").append(html);
            }
        });
    }

    function updatePrice(orderid,odid,old_price){
        var price=$("#prc_"+orderid+""+odid+"").val();
        if(price === "" || price ==null){
            alert("?????????????????????");
            return;
        }
        if(isNaN(price)){
            alert("???????????????????????????");
            return;
        }else if(Number(price)<=0){
            alert("????????????????????????0");
            return;
        }
        console.log(price);
        jQuery.ajax({
            url : "/cbtconsole/warehouse/updatePrice",
            data : {"orderid" : orderid,"odid" : odid,"price":price
            },
            type : "post",
            success : function(data) {
                if(data>0){
                    alert("????????????");
                    $("#prc_"+orderid+""+odid+"").val(price);
                }else{
                    $("#prc_"+orderid+""+odid+"").val(old_price);
                    alert("?????????????????????");
                }
            },
            error : function(e) {
                alert("?????????????????????");
            }
        });
    }

    //??????????????????
    function productAuthorization(goodsPid,odid,type){
        $.ajax({
            type:"post",
            url:"/cbtconsole/warehouse/productAuthorization",
            dataType:"text",
            data:{odid:odid,type:type,goodsPid:goodsPid},
            success : function(data){
				if(data>0){
                    window.location.reload();
				}else{
				    alert("?????????????????????????????????");
				}
            }
        });
	}
    //??????checkbox????????????
    function isCheckbox(name){
        obj = document.getElementsByName(name);
        var check_val = "";
        for(k in obj){
            if(obj[k].checked)
                check_val= obj[k].value;
        }
        return check_val;
    }
    //????????????
    function returnNum(odid,cusorder,pid,num) {

    	document.getElementById('cusorder').value=cusorder;
    	document.getElementById('num').value=num;
    	document.getElementById('odid').value=odid;
        document.getElementById('pid').value=pid;
        $.post("/cbtconsole/Look/getpid", {
            cusorder:cusorder,pid:pid
        }, function(res) {
            if(res.rows == 0){
                alert('??????????????????????????????');
                return;
            }else if(res.rows == 1){
                alert('??????????????????????????????????????????');
                return;
            }else {
                $('#user_remark').window('open');
			}
        });

    	        
    	   
    }
    function returnNu() {
        var cusorder =$(" #cusorder ").val()
        var number =$(" #number ").val()
        var returnNO =$(" #returnNO ").val()
        var num =$(" #num ").val()
        var odid =$(" #odid ").val()
        var pid =$(" #pid ").val()
        if(number>num){
        	alert('?????????????????????????????????');
        	return;
        }
		  $.post("/cbtconsole/Look/AddOrderByOdid", {
				number:number,cusorder:cusorder,returnNO:returnNO,odid:odid,num:num,pid:pid
			}, function(res) {
				if(res.rows == 0){
					alert('????????????');
				}else if(res.rows == 1){
					alert('????????????????????????');
				}
			else if(res.rows == 2){
				alert('???????????????');
				return;
			}
			else if(res.rows == 3){
				alert('??????????????????????????????????????????');
				return;
			}else if(res.rows == 4){
				alert('??????????????????????????????');
				return;
			}
				$('#user_remark').window('close');

});
}
    function AddOll() {
        var tbOrder = this.$("#select_id option:selected").val();
        var cusorder = $('#cuso').text();
        //alert(cusorder)
        var returnNO = $("input[name='radioname']checked").val();
        var isAutoSend = document.getElementsByName('radioname');
        for (var i = 0; i < isAutoSend.length; i++) {
            if (isAutoSend[i].checked == true) {
                returnNO = isAutoSend[i].value;
            }
        }
        //alert(returnNO)
        $.post("/cbtconsole/Look/AddRetAllOrder", {
            cusorder: cusorder, tbOrder: tbOrder, returnNO: returnNO
        }, function (res) {
            if (res.rows == 1) {
                alert('????????????');
                $("#th" + cusorder).html("");
                $("#th" + cusorder).append("??????????????????" + res.footer);
            } else {
                alert('??????????????????');
            }

            getItem();
        });
    }
    function getItem() {
        var tbOrder = this.$("#select_id option:selected").val();
        var cusOrder = $('#cuso').text();

        $.ajax({
            type: "POST",
            url: "/cbtconsole/Look/getAllOrder",
            data: {cusOrder: cusOrder, tbOrder: tbOrder, mid: 0},
            dataType: "json",
            success: function (msg) {
                if (msg.rows != null && msg.rows[0] != undefined) {
                    var temHtml = '';
                    document.getElementById("tabl").innerHTML = '';
                    $("#tabl").append("<tr ><td style='width:20px'>??????</td><td>?????????</td><td>????????????</td><td>????????????</td><td>????????????</td><td>????????????</td></tr>");
                    $(msg.rows).each(function (index, item) {

                        $("#tabl").append("<tr ><td ><input type='checkbox' onclick='this.value=this.checked?1:0' style='width:20px' name='" + item.item + "' id='c1' /></td><td>" + item.item + "</td><td>" + item.sku + "</td><td>" + item.itemNumber + "</td><td>" + item.returnReason + "</td><td>" + item.changeShipno + "</td></tr>");

                    });
                } else {
                    alert("?????????????????????")
                    $('#user_remark').window('close');

                }
            }
        });
    }
    function returnOr(uid,tbor) {
        $('#user_remark .remark_list').html('');
        $("#user_remark input[name='userid']").val(uid);
        $('#new_user_remark').val('');
        //????????????????????????
        $.ajax({
            type: "POST",
            url: "/cbtconsole/Look/getAllOrder",
            data: {cusOrder: uid, mid: 1,tbOrder:tbor},
            dataType: "json",
            success: function (msg) {
                if (msg.rows != null && msg.rows[0] != undefined) {
                    var temHtml = '';
                    document.getElementById("select_id").innerHTML = '';
                    $("#cuso").html("");
                    $("#cuso").append(msg.rows1[0].customerorder);
                    document.getElementById("tabl").innerHTML = '';
                    $("#tabl").append("<tr ><td>??????</td><td>??????pid</td><td>????????????</td><td>????????????</td><td>????????????</td><td>????????????</td></tr>");
                    $(msg.rows).each(function (index, item) {

                        $("#tabl").append("<tr ><td ><input type='checkbox' onclick='this.value=this.checked?1:0' name='" + item.item + "' id='c1' /></td><td><a href='https://www.importx.com/goodsinfo/122916001-121814002-1" + item.item + ".html' target='_blank' >" + item.item + "</a></td><td>" + item.sku + "</td><td>" + item.itemNumber + "</td><td>" + item.returnReason + "</td><td>" + item.changeShipno + "</td></tr>");

                        /* $("table").append("<tr ><td >1</td><td>"+item.item+"</td><td>????????????</td><td>"+item.itemNumber+"</td><td></td><td></td></tr>"); */

                    });
                    $('#user_remark .remark_list').html(temHtml);
                    $(msg.rows1).each(function (index, item) {

                        $("#select_id").append("<option id='' value='" + item.a1688Order + "'>" + item.a1688Order + "</option>");
                        // $('#user_remark').window('open');
                        document.getElementById("user_remark").style.display = "";

                    })
                } else {
                    alert("???????????????????????????????????????")

                }

            }

        });
    }
    function addUserRemark() {
        var tbOrder = this.$("#select_id option:selected").val();
        var cusOrder = $('#cuso').text();
        var der = $("#retu").val();
        var g = "";
        $("#tabl tr").each(function () {

            $(this).find('input').each(function () {
                var value = $(this).val();
                g += value + ",";

            });
        });
        g += tbOrder + ",";
        g += cusOrder;

        $.post("/cbtconsole/Look/AddAllOrder", {
            cusOrder: g
        }, function (res) {
            if (res.rows == 1) {
                alert('????????????');
                $("#th" + cusOrder).html("");
                $("#th" + cusOrder).append("??????????????????" + res.footer);
            } else if (res.rows == 0) {
                alert('??????????????????');
            } else if (res.rows == 2) {
                alert('????????????????????????');
            } else if (res.rows == 3) {
                alert('???????????????');
            }
            else if (res.rows == 4) {
                alert('????????????????????????????????????');
            } else if (res.rows == 5) {
                alert('????????????????????????');
                getItem();
            }
            getItem();
        });
    }

</script>

<body onload="FnLoading();" id="bodyid" style="background-color: #F0FFFF;">

<div id="user_remark" class="qod_pay3" title="????????????"
	 style="width:800px;height:auto;display: none;font-size: 16px;">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseOut()">???</a>
	</div>
	<div id="sediv" style="margin-left:20px;">
		??????1688???????????? <select id="select_id" onchange="getItem()"></select>
		<div>???????????????<span id="cuso"></span></div>
		<table id="tabl" border="1" cellspacing="0">
			<tr>
				<td style='width:20px'>??????</td>
				<td>??????pid</td>
				<td>????????????</td>
				<td>????????????</td>
				<td>????????????</td>
				<td>????????????</td>
			</tr>
		</table>
	</div>
	<div style="margin:20px 0 20px 40px;">
		<a href="javascript:void(0)" class="easyui-linkbutton"
		   onclick="addUserRemark()" style="width:80px">????????????</a>???????????????????????????????????????????????????????????????
	</div>
	<div style="margin:20px 0 20px 40px;">
		1688?????????<input class="but_color" type="button" value="????????????" onclick="AddOll()">
		<input type='radio' size='5' name='radioname' value='????????????' id='c'/>????????????
		<input type='radio' size='5' name='radioname' value='????????????' id='c'/>????????????
		<input type='radio' size='5' name='radioname' value='????????????' id='c'/>????????????
	</div>
</div>


 <%--<div id="user_remark" class="easyui-window" title="????????????"--%>
         <%--data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"--%>
         <%--style="width:400px;height:auto;display: none;font-size: 16px;">--%>
	 <%--<div id="sediv" style="margin-left:20px;">--%>
		 <%--??????1688???????????? <select id="select_id" onchange="getItem()"></select>--%>
		 <%--<div>???????????????<span id="cuso"></span></div>--%>
		 <%--<table id="tabl" border="1" cellspacing="0">--%>

			 <%--<tr>--%>
				 <%--<td style='width:20px'>??????</td>--%>
				 <%--<td>??????pid</td>--%>
				 <%--<td>????????????</td>--%>
				 <%--<td>????????????</td>--%>
				 <%--<td>????????????</td>--%>
				 <%--<td>????????????</td>--%>
			 <%--</tr>--%>
		 <%--</table>--%>
	 <%--</div>--%>
            <%--<div style="margin:20px 0 20px 40px;">--%>
                <%--<a href="javascript:void(0)" class="easyui-linkbutton"--%>
                   <%--onclick="returnNu()" style="width:80px" >????????????</a>--%>
            <%--</div>--%>
    <%--</div>--%>
<%--<div >--%>

</div>
<br />
<input type="hidden" id="bh_userid" />
<input type="hidden" id="bh_odid" />
<input type="hidden" id="bh_orderid" />
<input type="hidden" id="bh_goodsid" />
<input type="hidden" id="bh_goods_url" />
<input type="hidden" id="bh_goods_title" />

<div id="totopdiv1" class="show_up" onmouseover="FnShowUp()">
	<a href="#top" class='hh'>??????</a>
</div>
<div id="totopdiv2" class="show_up" onmouseout="FnHideUp()"
	 style="display: none">
	<a href="javascript:scroll(0,0)" class='gg'>????????????</a>
</div>
<div id="alertdiv" class="mod_pay2" align="center"></div>
<div id="bigestdiv" class="mod_pay1" onclick="alertdivHide();"></div>
<div id="operatediv" class="loading"></div>
<div class="mod_pay3" style="display: none;" id="rfddd">
	<center>
		<h3 class="show_h3">??????/????????????</h3>
		<div>
			<a href="javascript:void(0)" class="show_x" onclick="FncloseOut()">???</a>
		</div>
		<div>
			???????????????<span id="order_count" style="margin-right: 20px; color: red;">0</span>??????????????????:<span
				id="can_remaining">0</span>
		</div>
		<input type="hidden" id="in_id" />
		<div>
			???????????????<input type="text" name="buycount" id="buycount" class="remark" />
		</div>
		<div>
			???????????????<input type="text" name="taobaospec" id="taobaospec"
						class="remark" disabled="disabled" />
		</div>
		<div>
			???????????????<input type="text" name="shipping" id="shipping" class="remark"
						disabled="disabled" />
		</div>
		<div>
			???????????????<input type="text" name="usdprice" id="usdprice" class="remark"
						disabled="disabled" />
		</div>
		<div id="showdetail" style="display: none;">
			<font size="2px" color="wheat">???????????????</font>
		</div>
		<div>
			???????????????<input type="text" name="price" id="price" class="remark"
						onblur="judgePurchase();" />
		</div>
		<div>
			???????????????
			<textarea style="width: 470px" name="resource" id="resource" onBlur="check_url()" class="remarktwo"></textarea>
		</div>
		<div>
			<span id="url_info" style="color: red;"></span>
		</div>
		<h3 class="show_h3">???????????????</h3>
		<div>
			<span id="otherReason0">???????????????</span><input type="text"
													   name="otherReason" id="otherReason0" class="remark" /><br> <span
				id="otherReason1">???????????????</span><input type="text" name="otherReason"
													 id="otherReason1" class="remark" /><br> <span
				id="otherReason2">???????????????</span><input type="text" name="otherReason"
													 id="otherReason2" class="remark" /><br> <span
				id="otherReason3">???????????????</span><input type="text" name="otherReason"
													 id="otherReason3" class="remark" /><br> <span
				id="otherReason4">???????????????</span><input type="text" name="otherReason"
													 id="otherReason4" class="remark" /><br> <span
				id="otherReason5">???????????????:</span><input type="text"
													  name="otherReason" id="otherReason5" class="remark" /><br> <span
				id="otherReason6">???????????????:</span><input type="text"
													  name="otherReason" id="otherReason6" class="remark" /><br>
		</div>
		<input type="button" id="idAddResource" value="??????"
			   onclick="AddResource(1);"
			   style="width: 90px; height: 40px; margin-top: 20px;" /> <input
			type="button" value="??????" onclick="FncloseOut();"
			style="width: 90px; height: 40px;" /> <input type="button"
														 id="allidAddResource" value="????????????" onclick="AddResource(2);"
														 style="color: red; width: 90px; height: 40px; margin-top: 20px;" />
		<br />
		<p style="color: aqua;">
			???????????????<br /> 1.???????????? ????????? ???????????? ??????????????? ?????????????????? <br /> 2.????????? ???????????? ??????????????????
			??????,?????? ??????????????? <br /> 3.???????????? ?????? ??????????????? ??? ???????????? ?????? ??????????????? <br /> ????????????
			???????????? ???????????? ?????? ?????? ?????????????????????<br />
		</p>

	</center>
</div>


<div class="mod_pay3" style="display: none;" id="insertOrderInfo">
	<table>
		<caption><h3 class="show_h3">????????????</h3></caption>
		<tr>
			<td>???????????????:</td>
			<td><input type="text" name="taobao_id" id="taobao_id" class="remark" style="width: 250px;"
					   onblur="checkTaoBaoOr1688()"/></td>
		</tr>
		<tr>
			<td>????????????:</td>
			<td><input type="text" name="shipno" id="shipno" class="remark" style="width: 250px;"
					   onblur="checkTaoBaoOr1688()"/>
				<span id="on_notice" style="display: none;color: #ebef0f;font-size: 20px;">*???????????????????????????????????????</span></td>
		</tr>
		<tr>
			<td>????????????:</td>
			<td><select id="select_type" onchange="fireDisable(false)">
				<option value="0">1688??????</option>
				<option value="1">????????????</option>
			</select></td>
		</tr>
		<tr>
			<td>??????:</td>
			<td><label><input type="radio" name="radio" value="0"/>???????????????????????????????????????</label>
				<label><input type="radio" name="radio" value="1"/>??????????????????</label></td>
		</tr>
		<tr>
			<td colspan="2">
				<div>
					<input type="hidden" name="taobaoPrice" id="taobaoPrice"
						   class="remark"/>
					<input type="hidden" name="taobao_odid" id="taobao_odid"
						   class="remark"/>
				</div>
				<div>
					<input type="hidden" name="taobaoFeight" id="taobaoFeight"
						   class="remark"/>
				</div>
				<div>
					<input type="hidden" name="goodsQty" id="goodsQty" class="remark"/>
				</div>
				<div>
					<input type="hidden" name="paydate" id="paydate" class="remark"/>
				</div>
				<div>
					<input type="hidden" name="delivary_date" id="delivary_date"
						   class="remark"/>
				</div>
				<div>
					<input type="hidden" name="taobao_url" id="taobao_url"
						   class="remark"/>
				</div>
				<div>
					<input type="hidden" name="goods_sku" id="goods_sku" class="remark"/>
				</div>
				<div>
					<input type="hidden" name="taobao_name" id="taobao_name"
						   class="remark"/>
				</div>
				<input type="hidden" id="goods_imgs">
				<input type="hidden" id="TbOrderid">
				<input type="hidden" id="TbGoodsid">
				<input type="hidden" id="TbOdid">
			</td>
		</tr>
		<tr>
			<td colspan="2"><input type="button" id="idAddResourceOffline" value="??????"
					   onclick="insertSources();"
					   style="width: 90px; height: 40px; margin-top: 20px;"/>
				&nbsp;&nbsp;<input
					type="button" value="??????" onclick="FncloseInsert();"
					style="width: 90px; height: 40px;"/></td>
		</tr>
	</table>
</div>


<div class="mod_pay3" style="display: none;" id="repalyDiv1">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv1').hide();">???</a>
	</div>
	<input id="rk_orderNo" type="hidden" value=""> <input
		id="rk_odid" type="hidden" value=""><input
		id="rk_goodsid" type="hidden" value=""> ????????????:
	<textarea name="remark_content" rows="8" cols="50"
			  id="remark_content_"></textarea>
	<input type="button" id="repalyBtnId" onclick="saveRepalyContent()"
		   value="????????????">
</div>

<div class="mod_pay3" style="display: none;" id="repalyDiv4">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv4').hide();">???</a>
	</div>
	??????:<input type="radio" name="view_quality" value="2">???<input type="radio" name="view_quality" value="1">??????<input type="radio" name="view_quality" value="0">???
	<br>
	??????:<input type="radio" name="view_delivery" value="2">???<input type="radio" name="view_delivery" value="1">??????<input type="radio" name="view_delivery" value="0">???
	<br>
	??????:<input type="radio" name="view_delivery" value="2">???<input type="radio" name="view_delivery" value="1">??????<input type="radio" name="view_delivery" value="0">???
	<br>
	<input id="review_pid" type="hidden" value=""> ????????????:
	<textarea name="review_content" rows="8" cols="50"
			  id="review_content"></textarea>
	<input type="button"  onclick="saveRepalyContent()"
		   value="????????????">
</div>

<div class="mod_pay4" style="display: none;" id="repalyDiv2">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv2').hide();">???</a>
	</div>
	<input id="sample_od_id" type="hidden" value="">????????????:
	<textarea name="sample_remark" rows="8" cols="50"
			  id="sample_remark"></textarea>
	<input type="button" id="repalyBtnId" onclick="addSampleRemark()"
		   value="????????????">
</div>

<div class="mod_pay4" style="display: none;" id="repalyDiv3">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv3').hide();">???</a>
	</div>
	<input id="replace_pid" type="hidden" value="">????????????????????????:
	<input id="new_replace_url" style="width:500px" type="text" value=""><br>
	<input type="button" id="repalyBtnId" style="margin-left:250px" onclick="replaceUrl()"
		   value="??????1688????????????">
</div>

<div class="mod_pay3" style="display: none;" id="apbhdiv">
	<table style="text-align: center;">
		<caption><h1 style="text-align: center">??????</h1></caption>
		<tr>
			<td colspan="2">????????????<input type="radio" name="bh_rep_type" value="1" />????????????<input
				type="radio" name="bh_rep_type" value="2" /></td>
		</tr>
		<tr>
			<td>??????:</td>
			<td><input type="text" id="bh_buycount" class="remark" /></td>
		</tr>
		<tr>
			<td>1688?????????:</td>
			<td><input type="text" id="bh_1688_orderno" class="remark" onblur="checkTaobaoInfo()"/></td>
		</tr>
		<tr>
			<td>??????:</td>
			<td><input type="text" id="bh_goods_price" class="remark"  onblur="judgePurchase();" /></td>
		</tr>
		<tr>
			<td>??????:</td>
			<td><textarea style="width: 470px" id="bh_goods_p_url" class="remarktwo"></textarea></td>
		</tr>
		<tr>
			<td>??????:</td>
			<td><textarea style="width: 470px" id="bh_remark" class="remarktwo"></textarea></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="button" value="??????" onclick="insertOrderReplenishment();" id="bh_submit"
			   style="width: 90px; height: 40px; margin-top: 20px;" /> <input
			type="button" value="??????" onclick="apbhdivHide();"
			style="width: 90px; height: 40px;" />
			</td>
		</tr>
	</table>
</div>
<!-- ??????end -->
<div class="mod_pay3" style="display: none; background-color: #FFFFFF;"
	 id="divdhy">
	<center>
		<h1 style="color: red;" id="dhy_hmsgid"></h1>
		<h3 class="show_h3">????????????</h3>
		<div>
			<a href="javascript:void(0)" class="show_x"
			   onclick="$('#divdhy').hide();">???</a>
		</div>
		<div>
			?????????<input disabled="disabled" type="text" id="divdhy_jg"
					  class="remark" /><br /> ?????????
			<textarea disabled="disabled" style="width: 470px" id="divdhy_text"
					  class="remarktwo"></textarea>
		</div>

		<h3 class="show_h3">??????1</h3>
		<div>
			?????????<input id="dhy_jg1" type="text" class="remark" /><br /> ?????????<input
				id="dhy_sl1" type="text" class="remark" /><br /> ?????????
			<textarea id="dhy_hy1" style="width: 470px" class="remarktwo"></textarea>
			<br /> <input id="dhy_id1" type="hidden" value="0" class="remark" /><br />
		</div>

		<h3 class="show_h3">??????2</h3>
		<div>
			<input id="dhy_id2" type="hidden" value="0" class="remark" /><br />
			?????????<input id="dhy_jg2" type="text" class="remark" /><br /> ?????????<input
				id="dhy_sl2" type="text" class="remark" /><br /> ?????????
			<textarea id="dhy_hy2" style="width: 470px" class="remarktwo"></textarea>
			<br />
		</div>

		<input type="button" value="??????" onclick="dhyby();"
			   style="width: 90px; height: 40px; margin-top: 20px;" /> <input
			type="button" value="??????" onclick="$('#divdhy').hide();"
			style="width: 90px; height: 40px;" />
	</center>
</div>

<div class="a">
	<!-- 	<a href="javascript:history.back(-1)">??????????????????</a> -->
	<div>
		<center>
			<c:if test="${admid==999}">
			<span>
			(<input type="checkbox" id="fpall_${orderno}" name="fpall_${orderno}" style="width: 15px; height: 15px;" onclick="checkAll('${orderno}')"><span style="color:red">??????</span>)
				?????????????????????????????????<select id="buyuser1" name="buyuser1"
								   style="width: 110px;"
								   onchange="changeOrderBuyer('${orderno}',this.value);">
					</select>
				</span><span id="buyuserinfo"></span>
			</c:if>
			<span>???????????????</span> <span> <select id="search_state"
											  name="search_state" style="width: 100px;"
											  onkeypress="if (event.keyCode == 13) FnSearch('0');">
						<option value="0" selected="selected">??????</option>
						<option value="1">?????????</option>
						<option value="2">?????????</option>
			<!-- 						<option value="3">????????????????????????</option> -->
				</select>
				</span>
			<span>????????????/?????????id:<input
					type="text" id="goodid" class="h" value="${goodid}"
					onkeypress="if (event.keyCode == 13) FnSearch('1');" /></span>
			<span>????????????:<input
					type="text" id="goodname" class="h" value="${goodname}"
					onkeypress="if (event.keyCode == 13) FnSearch('1');" />
					<span>???????????????</span> <span> <select id="page_size" name="page_size" style="width: 100px;">
						<option value="50" selected="selected">50</option>
						<option value="100">100</option>
						<option value="150">150</option>
						<option value="200">200</option>
						<option value="200">500</option>
				</select>
					</span>
				 <span><input type="button" value="??????" class="ffff"
							  onclick="FnSearch('1');" /></span> <span><input type="button"
																			  value="??????" class="fff" onclick="FnClear();" /></span>
		</center>
	</div>
	<div>
		<div class="remarkAgainDiv" style="display: none;">
			<input id="rk_orderNo" type="hidden" value=""> <input
				id="rk_od_id" type="hidden" value=""> <input
				id="rk_goodsid" type="hidden" value=""> ????????????: <a
				id="hide_remarkDiv"
				style="color: red; float: right; margin-right: 10px; font-size: 24px; text-decoration: none"
				href="javascript:void(0);">X</a><br>
			<textarea name="remark_content" rows="8" cols="50"
					  id="remark_content"></textarea>
			<font color="red" id="ts"></font><br> <input type="checkbox"
														 id="isPush"> <label for="isPush">????????????(?????????????????????)</label> &nbsp;&nbsp;
			<input type="button" id="remarkAgainBtn" onclick="saveRemarkAgain()"
				   value="????????????">
		</div>
	</div>


	<div>&nbsp;</div>


	<c:if test="${empty pblist}">
		<div style="text-align: center">
			<font style="font-size: 24px;">???????????????????????????????????????????????????????????????????????????</font>
		</div>
	</c:if>

	<c:if test="${not empty pblist}">
		<c:forEach items="${pblist}" var="pb" varStatus="pbsi">
			<div class="b">
				<table id="${pb.orderNo}" align="center" border="1px"
					   style="font-size: 13px;" width="100%" bordercolor="gray">
					<tr id="ht${pb.orderNo}">
						<td colspan="8">
							<div style="background-color: #E4F2FF;">
								<input type='hidden' name='pagenum' value='${pb.orderid}'>
								<c:if test="${hasSampleOrder > 0}">
									<b style="color: red;font-size: 16px;">?????????????????????:<a href="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=999&orderno=${pb.orderNo}_SP&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid=${pb.userid}" target="_blank">${pb.orderNo}_SP</a></b>&nbsp;&nbsp;
								</c:if>
								<span class="d">????????????</span><span class="c">${pb.orderNo}</span>
								<c:if test="${cgid == 52}">
									<a href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=1" target="_blank" style="text-decoration: none"></a>
								</c:if>
								<c:if test="${cgid != 52}">
									<a href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=${cgid}" target="_blank" style="text-decoration: none"></a>
								</c:if>
								<c:if test="${pb.invoice=='1'}">
									<input type="button" value="??????invoice" onclick="window.open('/cbtconsole/autoorder/show?orderid=${pb.orderNo}','_blank')">
								</c:if>
								<c:if test="${pb.invoice=='2'}">
									<input type="button" value="??????invoice" onclick="window.open('/cbtconsole/autoorder/shows?orderid=${pb.orderNo}','_blank')">
								</c:if>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="d">???????????????</span><span class="c">${pb.paytime}</span>&nbsp;&nbsp;
								<span class="d">??????ID???</span><span class="c">${pb.userid}</span>&nbsp;&nbsp;
								<span class="d">??????????????????</span><span class="c">${pb.saler}</span>&nbsp;&nbsp;
								<span class="d" id="gj${pb.orderNo}">?????????</span><span class="c">${pb.orderaddress}</span>

								<c:if test="${empty pb.orderremarkNew}">
									<span class="d">???????????????</span><span class="c">${pb.orderremarkNew}</span>
								</c:if>
								<c:if test="${not empty pb.orderremarkNew}">
									<span class="d" style="color:red;">???????????????</span><b style="font-size: 20px;background-color:red;color: white;">${pb.orderremarkNew}</b>
								</c:if>
								<br>
									<%--<span class="d">?????????</span><span id="${pb.orderNo}_span" style="color: green;" class="c"></span> --%>
								<span class="d">????????????????????????(???)???</span><span id="${pb.orderNo}_pay_price" style="color: green;" class="c">--</span>
									<%--<span class="d">??????????????????(???):</span><span id="${pb.orderNo}_es_price" style="color: green;" class="c">--</span>--%>
								<span class="d">??????????????????(???)???</span><span id="${pb.orderNo}_buyAmount" style="color: green;" class="c">--</span>
								<span class="d">????????????(???????????????)(kg):</span><span id="${pb.orderNo}_es_weight" style="color: green;" class="c">--</span>
								<span class="d">?????????????????????</span><span id="${pb.orderNo}_es_freight" style="color: green;" class="c">--</span> RMB
								<span class="d">?????????????????????</span><span id="${pb.orderNo}_ac_freight" style="color: green;" class="c">--</span> RMB <br>
								<span class="d">??????????????????(???)?????????????????????:</span><span id="${pb.orderNo}_es_p" style="color: green;" class="c">--</span>
								<span class="d">??????????????????(???)?????????????????????:</span><span id="${pb.orderNo}_ac_p" style="color: green;" class="c">--</span><br>
								<c:if test="${!(pb.orderProblem == null or pb.orderProblem == '')}">
									<span id="OrderProblem${pb.orderNo}" class="d">???????????????????????????????????????${pb.orderProblem}</span><br>
								</c:if>
								<input type="hidden" id="ord_remark_${pb.orderNo}" value="${pb.orderremark}"> <input type="hidden" id="ord2_remark_${pb.orderNo}" value="${fn:substring(pb.orderremark, 0, 117)}"> <span id="ord_i_remark_${pb.orderNo}" style="background-color: lightgreen; color: blue; font-size: 20px;">${fn:substring(pb.orderremark, 0, 117)}</span>${pb.orderremark_btn}
								<span class="d">?????????????????????????????????:</span><span style="width:1500px;display:block;word-wrap:break-word;word-wrap:break-word" id="tborderInfo">${tbInfo}</span>
							</div>
						</td>
					</tr>
					<tr id="ht${pb.orderNo}">
						<td width="25%" colspan="2">????????????</td>
						<td width="8%">????????????</td>
						<td width="22%">??????????????????</td>
						<td width="5%">
                            <%--???????????????????????????????????????:--%>
                            <select name="websiteType" style="height: 28px;width: 160px;display: none;">
                                <option value="1" <c:if test="${websiteType == 1}">selected="selected"</c:if>>import-express</option>
                                <option value="2" <c:if test="${websiteType == 2}">selected="selected"</c:if>>kidscharming</option>
                                <option value="3" <c:if test="${websiteType == 3}">selected="selected"</c:if>>petstoreinc</option>
                            </select>
                            ??????/??????
							<br><input type="button" id="allQr1" style="color: green;" onclick="allQr('${pb.orderNo}')" value="??????????????????" /><input type="button" id="allcgQr1" style="color: green;" onclick="allcgQr('${pb.orderNo}', this)" value="??????????????????" /><br/><br/><input type="button" id="allQr2" style="color: red;" onclick="allQxQr('${pb.orderNo}')" value="??????????????????" /><input type="button" id="allcgQr2" style="color: red;" onclick="allQxcgQr('${pb.orderNo}')" value="??????????????????" /></td>
						<td width="6%">????????????</td>
						<td width="4%">??????</td>
						<td width="4%">????????????</td>
					</tr>
					<tr>
						<td width="13%" align="center">
							<c:set var="t_i" value="0"></c:set>
							<ul id="img_sf_1">
								<c:if test="${not empty pb.img_type}">
									<c:forEach items="${fn:split(pb.img_type,'@')}" var="img_type" varStatus="i">
										<c:if test="${fn:indexOf(img_type,'http') > -1}">
											<c:set var="t_i" value="1"></c:set>
											<c:if test="${pb.lastValue!=null}">
												<li><a id="example-${i.index }" target="_blank" href="${pb.lastValue}"> <img height="150" width="150" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif" data-original="${img_type}" onclick="changImgOrUrl('${pbsi.index+1}');">&nbsp;
												</a></li>
											</c:if>
											<c:if test="${pb.lastValue==null}">
												<li><a id="example-${i.index }" target="_blank" href="${pb.newValue}"> <img height="150" width="150" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif" data-original="${img_type}" onclick="changImgOrUrl('${pbsi.index+1}');">&nbsp;
												</a></li>
											</c:if>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${t_i == 0}">
								<li>
									<c:if test="${pb.lastValue!=null}">
									<a href="${pb.lastValue}" target="_block"><img class="lazy" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif" onclick="changImgOrUrl('${pbsi.index+1}');" data-original="${pb.fileimgname}" width="150px" height="150px"></a>
									</c:if>
									<c:if test="${pb.lastValue==null}">
									<a href="${pb.newValue}" target="_block"><img class="lazy" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif" onclick="changImgOrUrl('${pbsi.index+1}');" data-original="${pb.fileimgname}" width="150px" height="150px"></a>
									</c:if>
									</c:if>
									<c:if test="${t_i == 1}">
								<li>
									<c:if test="${pb.lastValue!=null}">
										<a href="${pb.lastValue}" target="_block"><img class="lazy" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif" onclick="changImgOrUrl('${pbsi.index+1}');" data-original="${pb.fileimgname}" width="50px" height="50px" /></a>
									</c:if>
									<c:if test="${pb.lastValue==null}">
										<a href="${pb.newValue}" target="_block"><img class="lazy" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif" onclick="changImgOrUrl('${pbsi.index+1}');" data-original="${pb.fileimgname}" width="50px" height="50px" /></a>
									</c:if>
								</li>
								</c:if>
							</ul>

						</td>
						<td width="12%"><span id="tip_${pb.od_id}" style="color: red;"> </span><br>
							<script type="text/javascript">
                                getTipInfo('${pb.od_id}');
							</script>
							<c:if test="${not empty  pb.importExUrl}">
								<a href="${pb.importExUrl}" target="_blank">??????????????????</a>
								<br>
							</c:if>
							<%--<input type="checkbox" name="${pb.orderNo}" value="${pb.orderNo},${pb.od_id}"><span style="color: red">??????????????????</span>--%>
							<%--<input style="margin-left:55px;" type="checkbox" id="fp_${pb.orderNo}" name="fp_${pb.orderNo}" value="${pb.od_id}">--%>
							<br>
							<c:if test="${admid==999}">
								????????????:<select
								id="buyer${pb.od_id}"
								onchange="changeBuyer(${pb.od_id},this.value);">
								<option value=""></option>
								<c:forEach var="aub" items="${aublist }">
									<option value="${aub.id }">${aub.admName}</option>
								</c:forEach>
								</select><span id="${orderd.od_id}_buyid">${pb.buyid}</span><span id="info${orderd.od_id}"></span>
							</c:if>
							<br>
							<span>????????????</span><span>${pb.goodsid};?????????id:${pb.od_id}</span>
							<br>
							<input type="hidden" id="order_${pbsi.index+1}" value="${pb.orderNo}" />
							<input type="hidden" id="goodsid_${pbsi.index+1}" value="${pb.od_id}" />
							<input type="hidden" id="hdgd" value="${pb.goodsdata_id}">
							<div style="width: 100%; word-wrap: break-word;">
								<span>Type???</span><font class="dd">${pb.goods_type}</font>
								&nbsp;&nbsp;(<em>${pb.specid} / ${pb.skuid}</em>)
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								<span>1688???????????????</span><font class="dd">${pb.type_name}</font>
								
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								<c:if test="${pb.purchase_state >3}">
								<span>?????????????????????????????????</span>
									${pb.tbSku}
								</c:if>
							</div>
							<br/><br/>
							<span id='${pb.goodsdata_id}${pbsi.index }_ylj'></span>
							<br>
							<c:if test="${pb.od_state == 13}">
								<span><input type="button" value="????????????????????????" onclick="getGoodsCar('${pb.goodsdata_id}','${pb.userid}','${pbsi.index}','${pb.goods_price}','${pb.newValue}','${pb.orderNo}');" /></span>
								<span id="${pb.goodsdata_id}${pbsi.index }_ygg"></span>
								<br>
							</c:if>
							<div style="width: 100%; word-wrap: break-word; word-break: break-all">
								<span id="title_${pb.orderNo}${pb.od_id}">${pb.goods_title }</span>
							</div>
						</td>
						<td width="8%">
							<input type="hidden" id="${pb.orderNo}${pbsi.index}_s" value="${pb.goods_price}" />
							<input type="hidden" id="${pb.orderNo}${pbsi.index}_sQuantity" value="${pb.googs_number}" /> <input type="hidden" id="${pb.orderNo}${pbsi.index }_jg" value="${pb.goods_price}" />
							<div>
								<span>Price???</span>
								<span>${pb.goods_price}&nbsp;${pb.currency}/piece</span>
							</div>
							<div>
								<span>Quantity???</span>
								<span style="font-size: 24px; color: red">${pb.googs_number}</span>
								<input type="hidden" id="qquantity${pb.orderNo}${pb.od_id}" value="${pb.purchaseCount}">
								<input type="hidden" value="${pb.goodsUnit},${pb.seilUnit}">
								<c:if test="${not empty pb.goodsUnit}">
									<c:if test="${pb.goodsUnit == pb.seilUnit}">
										${pb.goodsUnit}
									</c:if>
									<c:if test="${pb.goodsUnit != pb.seilUnit}">
										<span style="color: red">${pb.goodsUnit}&nbsp;${pb.seilUnit}</span>
									</c:if>
									<br>
								</c:if>
							</div>
							<!-- ???????????? -->
							<div style="margin-top: 10px;">
							</div>
							<c:if test="${not empty pb.remarkpurchase}">
								<div style="width: 100%; word-wrap: break-word;">
									<span class="cai_remark">Remark???</span> <font class="cc">${pb.remarkpurchase}</font>
								</div>
							</c:if>
							<c:if test="${pb.lastValue!=null}">
								<input type="hidden" value="${pb.last_tb_1688_itemid}" id="url_${pb.orderNo}${pb.od_id}" />
							</c:if>
							<c:if test="${pb.lastValue==null}">
								<input type="hidden" value="${pb.tb_1688_itemid}" id="url_${pb.orderNo}${pb.od_id}" />
							</c:if>
						</td>
						<td width="22%">
							<div>
								<c:if test="${pb.rkgoodstatus == '2'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">????????????????????????????????????</a>
									</h1>
								</c:if>
								<c:if test="${pb.rkgoodstatus == '3'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">??????????????????????????????</a>
									</h1>
								</c:if>
								<c:if test="${pb.rkgoodstatus == '4'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">?????????????????????????????????</a>
									</h1>
								</c:if>
								<c:if test="${pb.rkgoodstatus == '5'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">????????????????????????????????????</a>
									</h1>
								</c:if>
								<c:if test="${pb.rkgoodstatus == '6'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">???????????????????????????????????????</a>
									</h1>
								</c:if>
							</div>
							<span style="color: red; font-size: 25px;" id="inventory_${pb.orderNo}${pb.od_id}"></span>
							<div id="hideDetails_${pb.orderNo}${pb.od_id}" style="display: block">
								<input type="hidden" id="${pb.orderNo}${pbsi.index}_e" value="${pb.oldValue}" /> <input type="hidden" id="${pb.orderNo}${pbsi.index}_eQuantity" value="${pb.purchaseCount}" />
								<h1 style="color: #F00">${pb.cginfo}</h1>
								<span>Price???</span>
									<%-- 									<span id="prc_${pb.orderNo}${pb.goodsid}"> --%>
								<input type="text" id="prc_${pb.orderNo}${pb.od_id}" style="width:50px" value="${pb.oldValue}"
									   onkeypress="if (event.keyCode == 13) updatePrice('${pb.orderNo}','${pb.od_id}','${pb.oldValue}');"/>
								<input type="hidden" value="${pb.goods_title }" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>Quantity???</span>
								<span id="tity_${pb.orderNo}${pb.od_id}">${pb.purchaseCount}</span><br />
								<c:if test="${pb.purchase_state<3}">
									<script type="text/javascript">
                                        getInventory('${pb.orderNo}','${pb.goodsid}','${pb.inventory_remark}','${pb.purchase_state}','${pb.od_id}');
									</script>
								</c:if>
								<div style="width: 100%; word-wrap: break-word; word-break: break-all">
									<c:if test="${pb.shopFlag==1}">
										????????????/??????????????????:
										<c:if test="${pb.lastValue!=null}">
											<a href="${pb.lastValue}" target="_blank" onclick="changImgOrUrl('${pbsi.index+1}');"> <span id="chk_${pb.orderNo}${pb.od_id}"> ${pb.lastValue}<input type="hidden" value="${pb.goods_title }" /></span></a>
											<input type="hidden" id="chk1_${pb.orderNo}${pb.od_id}" value="${pb.lastValue}"></input>
										</c:if>
										<c:if test="${pb.lastValue==null}">
											<a href="${pb.newValue}" target="_blank" onclick="changImgOrUrl('${pbsi.index+1}');"> <span id="chk_${pb.orderNo}${pb.od_id}"> ${pb.newValue}<input type="hidden" value="${pb.goods_title }" /></span></a>
											<input type="hidden" id="chk1_${pb.orderNo}${pb.od_id}" value="${pb.newValue}">
										</c:if>
									</c:if>
									<c:if test="${pb.shopFlag!=1}">
										??????????????????:
										<a target="_blank" href="https://detail.1688.com/offer/${pb.goods_pid}.html">https://detail.1688.com/offer/${pb.goods_pid}.html</a><br>
										??????????????????:
										<c:if test="${pb.lastValue!=null}">
											<a href="${pb.lastValue}" target="_blank" onclick="changImgOrUrl('${pbsi.index+1}');"> <span id="chk_${pb.orderNo}${pb.od_id}"> ${pb.lastValue}<input type="hidden" value="${pb.goods_title }" /></span></a>
											<input type="hidden" id="chk1_${pb.orderNo}${pb.od_id}" value="${pb.lastValue}"></input>
										</c:if>
										<c:if test="${pb.lastValue==null}">
											<a href="${pb.newValue}" target="_blank" onclick="changImgOrUrl('${pbsi.index+1}');"> <span id="chk_${pb.orderNo}${pb.od_id}"> ${pb.newValue}<input type="hidden" value="${pb.goods_title }" /></span></a>
											<input type="hidden" id="chk1_${pb.orderNo}${pb.od_id}" value="${pb.newValue}">
										</c:if>
									</c:if>
									<br>
										${pb.support_info}
									<br>
									<c:if test="${cgid == 52}">
										<a style="color:red" href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=1" target="_blank" style="text-decoration: none">[????????????1688??????]</a>
									</c:if>
									<c:if test="${cgid != 52}">
										<a style="color:red" href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=${admid==999?1:cgid}" target="_blank" style="text-decoration: none">[????????????1688??????]</a>
									</c:if>
									<%--<a style="color:red" href="/cbtconsole/website/goods_change.jsp?odid=${pb.od_id}" target="_blank" style="text-decoration: none">????????????</a>--%>
								</div>
								<span> <input type="hidden" value="${pb.goods_title }" /></span>
								<span id="chk22_${pb.orderNo}${pb.od_id}">
										<input type="hidden" value="${pb.goods_title }" />
										<input type="hidden" id="chk2_${pb.orderNo}${pb.od_id}" value="${pb.oldValue}">
									</span>
							</div>
							<div>
								<span><input type="hidden" id="id_${pb.orderNo}${pb.od_id}" value="${pb.goods_title}"></span>
								<span id="span_4${pb.orderNo}${pb.od_id}">
									    <input type="hidden" value="${pb.goods_title }" />
									    <input type="button" name="btn_resources" id="btnSet_${pb.orderNo}${pb.od_id}" value="??????/????????????" onclick="goodsResource(${pb.exchange_rate},'${pb.userid}','${pb.orderNo}','${pb.od_id}','${pb.goodsid}','${pb.goodsdata_id}','${pb.goods_url}','','${pb.goods_price}','${pb.googs_number}','${pb.currency}','${pb.mode_transport}','${pb.goodssourcetype}','${pb.cGoodstype}','${pb.issure }','${pb.in_id }','${pb.remaining }','${pb.lock_remaining }','${pb.new_remaining }','${pb.shop_ids}','${pb.straight_address}');"
											   class="f" onfocus="setStyle(this.id);" />
									</span>
								<span>
									    <c:if test="${pb.is_replenishment==0}">
											<input type="button" value="????????????" class="f" id="is_replenishment" onclick='apbhcle("${pb.od_id}","${pb.orderNo}","${pb.goodsid}");$("#apbhdiv").show();$("#bh_userid").val("${pb.userid}");$("#bh_odid").val("${pb.od_id}");$("#bh_orderid").val("${pb.orderNo}");$("#bh_goodsid").val("${pb.goodsid}");$("#bh_goods_url").val("${pb.goods_url}");' />
										</c:if> <c:if test="${pb.is_replenishment==1}">
									<input type="button" value="????????????" class="f" id="is_replenishment" style="background-color: red;" onclick='apbhcle("${pb.od_id}","${pb.orderNo}","${pb.goodsid}");$("#apbhdiv").show();$("#bh_userid").val("${pb.userid}");$("#bh_odid").val("${pb.od_id}");$("#bh_orderid").val("${pb.orderNo}");$("#bh_goodsid").val("${pb.goodsid}");$("#bh_goods_url").val("${pb.goods_url}");' />
								</c:if>
									</span>
								<span>
									    <input type="button" name="btn_resources" id="btnGet_${pb.orderNo}${pb.od_id}" value="?????????????????????" onclick="OtherSources('${pb.userid}','${pb.orderNo}','${pb.od_id}','${pb.goodsid}','${pb.goodsdata_id}','${pb.importExUrl}','','${pb.goods_price}','${pb.googs_number}','${pb.currency}');" class="f" onfocus="setStyle(this.id);" />
									</span>
								<c:if test="${pb.offline_purchase==0}">
									<input type="button" id="insert_${pb.orderNo}${pb.od_id}" value="????????????" onclick="AddinsertSources('${pb.newValue}','${pb.purchaseCount}','${pb.cGoodstype}','${pb.googs_img}','${pb.oldValue}','${pb.orderNo}','${pb.goodsid}','${pb.od_id}');" class="f" />
								</c:if>
								<c:if test="${pb.offline_purchase==1}">
									<input type="button" id="insert_${pb.orderNo}${pb.od_id}" style="background-color: red;" value="????????????" onclick="AddinsertSources('${pb.newValue}','${pb.purchaseCount}','${pb.cGoodstype}','${pb.googs_img}','${pb.oldValue}','${pb.orderNo}','${pb.goodsid}','${pb.od_id}');" class="f" />
								</c:if>
								<c:if test="${pb.authorized_flag==1 || pb.authorized_flag==2}">
									<input type="button" style="background-color: green;"  value="???????????????" onclick="productAuthorization('${pb.goods_pid}','${pb.od_id}',0);" class="f" />
								</c:if>
								<c:if test="${pb.authorized_flag==0}">
									<input type="button" id="insert_${pb.orderNo}${pb.od_id}" style="background-color: red;" value="??????????????????" onclick="productAuthorization('${pb.goods_pid}','${pb.od_id}',1);" class="f" />
								</c:if>
								<span id="issure${pb.orderNo}${pb.od_id}">
									<input type="hidden" value="${pb.goods_title }" /><label id="rmk_${pb.orderNo}${pb.od_id}" style="color: red;">${pb.issure}</label></span>
								<span>${pb.originalGoodsUrl}</span> <input type="hidden" id="dhy${pb.orderNo}${pb.od_id}" value="${pb.goods_title}" />
								<br>${pb.noChnageRemark}
							</div>
								<%--<div style="width: 100%; word-wrap: break-word;">--%>
								<%--????????????????????????RMB????????????????????????%):${pb.profit}--%>
								<%--???????????????????????????RMB????????????????????????%???=??????????????????????????????-??????????????????????????????-????????????????????????--%>
								<%--</div>--%>

							<c:if test="${pb.replacementProduct!=null}">
								<div style="width: 100%; word-wrap: break-word;">
									<font color="red">??????????????????????????? <span>${pb.replacementProduct}</span> </font><br>
								</div>
							</c:if>
							<div style="width: 100%; word-wrap: break-word;">
								?????????????????? <span>${pb.shopName}</span> <br>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								?????????????????? <span>${pb.shopGrade}</span> <br>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								??????????????? <a href="https://s.1688.com/collaboration/collaboration_search.htm?fromOfferId=${pb.goods_pid}&tab=sameDesign" target="_blank">??????</a>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								1688?????????:<span>${pb.morder}</span>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								??????????????????(kg):<font color="green"><span id="cbrWeight_${pb.orderNo}${pb.od_id}">${pb.cbrWeight}</span></font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								????????????(?????????????????????)(kg):<font color="green"><span id="esWeight_${pb.orderNo}${pb.od_id}">${pb.carWeight}</span></font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								????????? <font class="cc"><span id="rmk2_${pb.orderNo}${pb.od_id}"> <input type="hidden" value="${pb.goods_title }" /> ${pb.remark}</span></font> <br> <font style="font-size: 24PX; color: midnightblue;">${pb.productState}</font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								??????????????? <font class="cc"><span>${pb.inventoryRemark}</span></font> <br>
							</div>
							<c:if test="${pb.shopFlag==1}">
								<div style="width: 100%; word-wrap: break-word;">
									?????????????????? ???${pb.shopAddress}???<font class="cc"><span style="color:red">
									<c:if test="${pb.straight_flag==1}">
										??????????????????
									</c:if>
									<c:if test="${pb.straight_flag==2}">
										?????????????????????${pb.straight_time}???<a href="/cbtconsole/website/straight_hair_list.jsp?goods_pid=${pb.goods_pid}" target="_blank">????????????</a>
									</c:if>
									</span></font>
									<c:if test="${pb.straight_flag==1}">
										<%-- <button onclick="determineStraighthair('${pb.orderNo}','${pb.od_id}','${pb.od_id}')">??????</button> --%>
										<button onclick="determineStraighthair('${pb.orderNo}','${pb.goodsid}','${pb.od_id}')">??????</button>
									</c:if>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									????????????????????????<span>${pb.authorizedFlag}</span> <br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									?????????????????? <span id="level">${pb.level}</span> <br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									?????????????????? <font class="cc"> <span><a target="_blank" href="/cbtconsole/supplierscoring/supplierproducts?shop_id=${pb.goodsShop}&goodsPid=${pb.goods_pid}&flag=0">${pb.quality}</a></span></font> <br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									?????????ID??? <a target="_blank" style="color:red;" title="????????????????????????????????????" href="/cbtconsole/website/shopBuyLog.jsp?shopId=${pb.goodsShop}">${pb.goodsShop}</a><br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									??????????????????${pb.shopInventory}
								</div>
								<div style="width: 170px; word-wrap: break-word;color: #59f775;background-color: black;">
									<b>??????????????????($):${pb.goodsShopPrice}</b>
								</div>
								<c:if test="${pb.goodsShopPrice>1000}">
									<div style="width: 100%; word-wrap: break-word;">
										???????????????????????????????????????
									</div>
								</c:if>
							</c:if>
							<c:if test="${pb.inventory>0}">
								<div style="width: 100%; word-wrap: break-word;">
									?????????????????? <font class="cc"> <span id="rmk2_${pb.orderNo}${pb.od_id}"> <input type="hidden" value="${pb.inventory}" />
									<a target="_blank" href ="/cbtconsole/inventory/list?inid=${pb.inventorySkuId}">${pb.inventory}</a>
									<button onclick="useInventory('${pb.od_id}',1,'${pb.orderNo}','${pb.goodsid}','${pb.inventory}','${pb.googs_number}','${pb.inventorySkuId}','${pb.goodsUnit}','${pb.seilUnit}')">????????????</button>
									</span></font> <br>
								
								</div>
							</c:if>
							<%-- <c:if test="${pb.pidInventory>0}">
								<div style="width: 100%; word-wrap: break-word;">
									???????????????(???????????????)??? <font class="cc"> <span id="rmk2_${pb.orderNo}${pb.od_id}"> <input type="hidden" value="${pb.pidInventory}" /><a target="_blank" href ="/cbtconsole/StatisticalReport/goodsInventoryReport?pid=${pb.goods_pid}">${pb.pidInventory}</a></span></font> <br>
								</div>
							</c:if> --%>
							<div style="width: 100%; word-wrap: break-word;">
								??????????????? <font style="font-size:20px;font-weight:bold;color:blue;"> <span>${pb.shipstatus}</span></font> <br>
							</div>
                            <div style="width: 100%; word-wrap: break-word;">
								1688???????????? <font style="font-size:20px;font-weight:bold;color:blue;"> <span>${pb.shipnoid}</span></font> <br>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								1688???????????? <font style="font-size:20px;font-weight:bold;color:blue;"> <span>${pb.tborderid}</span></font> <br>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								???????????????????????????<a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=${pb.goods_pid}">????????????</a> <br>
							</div>
							<script>
                                FnHideDetails('${pb.newValue}','${pb.orderNo}', '${pb.od_id}', '${pb.od_id}')
							</script>
							<c:if test="${pb.oistate==6}">
								<h1 style="color: red;">???????????????????????????</h1>
							</c:if> <c:if test="${pb.saler=='testAdm'}">
							<h1 style="color: red;">????????????????????????????????????</h1>
						</c:if>
							<c:if test="${pb.odstate==2}">
								<h1 style="color: red;">?????????????????????</h1>
							</c:if>
							<c:if test="${pb.refund_flag==1}">
								<h1 style="color: red;">??????????????????</h1>
							</c:if>
							<input type="hidden" value="${pb.source_of_goods }">
							<c:if test="${pb.source_of_goods==1 }">
								<h3 style="color: purple;">????????????????????????--By:${pb.admin }</h3>
							</c:if>
						</td>
						<td width="5%">
							<div id="${pb.orderNo}${pbsi.index}">
								<div id="click_hyqr_${pb.orderNo}" style="width: 100%;" onclick="FnComfirmHyqr('${pbsi.index}','${pb.userid}','${pb.orderNo}','${pb.od_id}','${pb.goodsid}','${pb.goodsdata_id}','${pb.goods_url}','','${pb.goods_price}','${pb.googs_number}','${pb.purchaseCount}','${pb.child_order_no}','${pb.isDropshipOrder}');">
									<input type="hidden" value="${pb.goods_title }" />${pb.querneGoods}
								</div>
							</div>
							<div>
								<span id="puechase_comfirm_hyqr${pb.orderNo}${pb.od_id}"></span>
							</div>
							<div>
								<br /><br />
							</div>
                            <div>
                                <%--?????????????????????????????????:--%>
                                <select name="websiteType" style="height: 28px;width: 160px;display: none;">
                                    <option value="1" <c:if test="${websiteType == 1}">selected="selected"</c:if>>import-express</option>
                                    <option value="2" <c:if test="${websiteType == 2}">selected="selected"</c:if>>kidscharming</option>
                                    <option value="3" <c:if test="${websiteType == 3}">selected="selected"</c:if>>petstoreinc</option>
                                </select>
                            </div>
							<div id="clickdiv_${pb.orderNo}" onclick="FnComfirm('${pb.userid}','${pb.orderNo}','${pb.od_id}','${pb.goodsid}','${pb.goodsdata_id}','${pb.goods_url}','','${pb.goods_price}','${pb.googs_number}','${pb.purchaseCount}','${pb.child_order_no}','${pb.isDropshipOrder}', this);" style="width: 100%;">
								<input type="hidden" value="${pb.od_state}" />${pb.purchaseSure}
							</div>
							<div>
								<span id="puechase_comfirm_${pb.orderNo}${pb.od_id}"></span>
							</div>
							<div>
								<br/>
							</div>
							<input type="hidden" value="${cgid}">
							<c:if test="${cgid == 40}">
								<div id="FnYiR_${pb.orderNo}" onclick="FnYiRuKu('${pb.goods_url}','${pb.orderNo}','${pb.goodsid}','${pb.od_id}');" style="width: 100%;">
									<input type="hidden" value="${pb.od_state}" />
									<input type="hidden" value="${pb.goods_title }" /> ${pb.yiruku}
								</div>
							</c:if>
							<div>
								<span id="puechase_ruku_${pb.orderNo}${pb.od_id}"></span>
							</div>
							<div>
								<br/>
							</div>
							<div>${pb.remarkAgainBtn}</div>
							<script>
                                jslr('${pb.orderNo}')
							</script>
						</td>
						<td width="10%">
							<div>
									<span> ???????????????
										 <c:if  test="${pb.position=='' || pb.position==null}">????????????</c:if>
										 <c:if test="${pb.position != '' || pb.position!=null}">${pb.position}</c:if>
									</span>
							</div>
							<div>
								<span>???????????????</span><br /> <span style="color: blue">${pb.addtime}</span>
							</div>
							<div>
								<span>???????????????</span><br />
								<span style="color: blue" id="puechase_hyqr_time_${pb.orderNo}${pb.od_id}">${pb.puechaseTime}</span>
							</div>
							<div>
								<span style="color: red">???????????????</span>
								<br/>
								<span style="color: red" id="puechase_time_${pb.orderNo}${pb.od_id}">${pb.purchasetime}</span>
							</div>
							<c:if test="${pb.tb_orderid==null}">
								<div>
									<span>?????????????????????</span><br /> <span style="color: blue">${pb.rukuTime}</span>
								</div>
							</c:if>
							<c:if test="${pb.tb_orderid!=null}">
								<div>
									<span>???????????????</span><br /> <span style="color: blue">${pb.rukuTime}</span>
								</div>
							</c:if>
							<div>
								<span>???????????????</span><br /> <span style="color: blue"></span>
							</div>
						</td>
						<td width="4%">
							<input type="hidden" id="re_${pb.orderNo}${pb.od_id}" /> <input type="button" onclick="getIsReplenishment('${pb.orderNo}','${pb.goodsid}');" value="??????????????????" />
							<input type="button" onclick="getIsOfflinepurchase('${pb.orderNo}','${pb.goodsid}');" value="????????????????????????" />
							<!-- ????????????????????????????????? -->
							<c:choose>
								<c:when test="${pb.shopFlag==1}">
									<input type="button" onclick="displayBuyLog('${pb.goods_pid}','${pb.car_urlMD5}');" value="??????????????????" />
									<%-- <input type="button" onclick="openSupplierGoodsDiv('${pb.goods_pid}','${pb.goodsShop}');" value="????????????" />
									<input type="button" onclick="openSupplierDiv('${pb.goodsShop}');" value="????????????" /> --%>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
							<input type="button" onclick="getDetailsChangeInfo('${pb.orderNo}','${pb.goodsid}');" value="????????????????????????" />
						</td>
						<td width="12%">
							<div style="overflow-y: scroll; height: 250px;">
								<div class="w-font">
									<font style="font-size: 15px;" id="rk_remark_${pb.orderNo}${pb.od_id}">${pb.goods_info}</font>
								</div>
							</div>
							<div class="w-margin-top">
								<input type="button" value="???????????????" onclick="doReplay1('${pb.orderNo}','${pb.goodsid}','${pb.od_id}');" class="repalyBtn" />
								<input type="button" id="${pb.od_id}" stype="display:none" value="????????????" onclick="returnOr('${pb.orderNo}','${pb.shipno}');" class="repalyBtn" />
								<P>${pb.returnTime}</P>
							</div>
						</td>
					</tr>
				</table>
			</div>
			${hideTr}
		</c:forEach>
	</c:if>
	<div>
		<br>
	</div>
	<div class="cai_page">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="caitd01">
					&nbsp;&nbsp;&nbsp; ?????????&nbsp;&nbsp;&nbsp;
					<a href="#">${pagenum}</a>&nbsp;&nbsp;&nbsp;???&nbsp;&nbsp;&nbsp; <span><input type="hidden" id="refresh" value="${pagenum}"></span>??????&nbsp;&nbsp;&nbsp;<a href="#"><span id="total_page">${totalpage }</span></a>&nbsp;&nbsp;&nbsp;???&nbsp;&nbsp;&nbsp;??????&nbsp;&nbsp;&nbsp;<a href="#">${totalnum }</a>&nbsp;&nbsp;&nbsp;???????????????
				</td>
				<td class="caitd02">
					<a href="javascript:void(0);" onclick="submit('1')">??????</a>&nbsp;&nbsp;&nbsp; <a href="javascript:void(0);" onclick="submit('${pagenum-1 }')">?????????</a>&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="submit('${pagenum+1 }')">?????????</a>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="submitt()">????????????</a>
					&nbsp;&nbsp;?????????&nbsp;&nbsp;
					<input style="width: 30px;" name="num" id="nummm" value="${num }" type="text" align="middle" />
					&nbsp;&nbsp;???&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="submit('${totalpage }')">??????</a></td>
			</tr>
		</table>
		<div id="divcss1">
			<table border="1">
				<tr>
					<td>???????????????
					<td>
					<td>??????
					<td>
					<td>????????????
					<td>
					<td>????????????
					<td>
				</tr>
				<tr>
					<td id="logistics_id">???????????????
					<td>
					<td id="tb_state">??????
					<td>
					<td id="createtime">????????????
					<td>
					<td id=""><img src='' />
					<td>
				</tr>
			</table>
		</div>
	</div>
</div>
${keepValue}
<div class="mod_pay3" style="display: none;" id="replenishment_record">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="Fncloserecord()">???</a>
	</div>
	<center>
		<h3 class="show_h3">??????????????????</h3>
		<table id="replenishment" class="imagetable">
			<thead>
			<tr>
				<td width='13%'>????????????</td>
				<td width='13%'>????????????</td>
				<td width='18%'>????????????</td>
				<td width='10%'>?????????</td>
				<td width='18%'>????????????</td>
				<td width=''>????????????</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
<div class="mod_pay4" style="display: none;" id="displayChangeLog">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="displayChangeLogInfo()">???</a>
	</div>
	<center>
		<h3 class="show_h3">??????????????????</h3>
		<table id="displayChangeLogs" class="imagetable">
			<thead>
			<tr>
				<td width='11%'>??????????????????($)</td>
				<td width='11%'>??????????????????</td>
				<td width='11%'>?????????????????????(???)</td>
				<td width='11%'>?????????????????????</td>
				<td width='11%'>????????????</td>
				<td width='11%'>???????????????</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
<div class="mod_pay3" style="display: none;" id="displayBuyInfo">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseBuyInfo()">???</a>
	</div>
	<center>
		<h3 class="show_h3">??????????????????</h3>
		<table id="displayBuyInfos" class="imagetable">
			<thead>
			<tr>
				<td width='11%'>????????????</td>
				<td width='11%'>????????????</td>
				<td width='11%'>????????????</td>
				<td width='11%'>?????????</td>
				<td width='11%'>????????????</td>
				<td width='11%'>????????????</td>
				<td width='11%'>????????????</td>
				<td width='12%'>????????????</td>
				<td width='11%'>????????????</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
<div class="mod_pay3" style="display: none;" id="supplierDiv">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierDiv()">???</a>
	</div>
	<center>
		<h3 class="show_h3">?????????????????????</h3>
		<table id="supplierDivInfos" class="imagetable">
			<thead>
			<tr>
				<td rowspan="5"><span style="color:red">[??????]</span>:1:???   2:??????  3: ??????  4:?????????  5: ??????</td>
			</tr>
			<%--<tr>--%>
			<%--<td rowspan="5"><span style="color:red">[??????]</span>:1:???????????????  2:????????????????????????  3:???????????????????????????  4:???????????????????????????  5:????????????????????????</td>--%>
			<%--</tr>--%>
			<tr>
				<td>??????</td><td>??????</td><td>?????????????????????</td><td>??????????????????</td>
			</tr>
			<tr>
				<td>
					<span id="su_shop_id"></span>
					<input type="hidden" id="hidden_shopId">
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
					<input type="text" id="su_data" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
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
<div class="mod_pay3" style="display: none;" id="supplierGoodsDiv">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierGoodsDiv()">???</a>
	</div>
	<center>
		<h3 class="show_h3">??????????????????</h3>
		<table id="supplierGoodsDivInfos" class="imagetable">
			<thead>
			<tr>
				<td rowspan="5"><span style="color:red">[??????]</span>:1:???   2:??????  3: ??????  4:?????????  5: ??????</td>
			</tr>
			<%--<tr>--%>
			<%--<td rowspan="5"><span style="color:red">[??????]</span>:1:???????????????  2:????????????????????????  3:???????????????????????????  4:???????????????????????????  5:????????????????????????</td>--%>
			<%--</tr>--%>
			<tr>
				<td>??????</td><td>??????</td><td>??????</td>
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
				<%--<td>--%>
				<%--<select id="g_service">--%>
				<%--<option value="0">---?????????---</option>--%>
				<%--<option value="1">1???</option>--%>
				<%--<option value="2">2???</option>--%>
				<%--<option value="3">3???</option>--%>
				<%--<option value="4">4???</option>--%>
				<%--<option value="5">5???</option>--%>
				<%--</select>--%>
				<%--</td>--%>
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
<div class="mod_pay3" style="display: none;" id="offlinepur_chase">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="Fncloserchase()">???</a>
	</div>
	<center>
		<h3 class="show_h3">??????????????????</h3>
		<table id="offlinepurchase" class="imagetable"
			   style="word-break: break-word;">
			<thead>
			<tr>
				<td width='10%'>???????????????</td>
				<td width='10%'>????????????</td>
				<td width='10%'>?????????</td>
				<td width='50%'>????????????</td>
				<td width='10%'>???????????????</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
</body>
<script type="text/javascript">
    function setStyle(x) {
        var btn_res = document.all("btn_resources");
        for (var i = 0; i < btn_res.length; i++) {
            btn_res[i].style.background = "";
        }
    }
    function fnShow_od_remark(order) {
        var txt = (document.getElementById(order).innerHTML).indexOf("????????????");
        if (txt == 0) {
            document.getElementById("ord_i_" + order).innerHTML = $(
                "#ord_" + order).val();
            document.getElementById(order).innerHTML = "<<??????";
        } else {
            document.getElementById("ord_i_" + order).innerHTML = $(
                "#ord2_" + order).val();
            document.getElementById(order).innerHTML = "????????????>>";
        }
    }
    function FnOriginalUrl(id) {
        var idd = id.split("_");
        var order_no = "";
        var goods_id = "";
        if (idd.length == 3) {
            order_no = idd[1];
            goods_id = idd[2];
        } else if (idd.length == 4) {
            order_no = idd[1];
            goods_id = idd[3];
        }
        $.ajax({
            type : 'POST',
            url : '/cbtconsole/PurchaseServlet?action=getOriginalUrl&className=Purchase',
            data : {
                order_no : order_no,
                goods_id : goods_id
            },
            success : function(url) {
                if (url == null || url == '') {
                    alert("????????????");
                } else {
                    window.open(url, "???????????????")
                }
            }
        });
    }
</script>
<script type="text/javascript">
    var od_state = 0;
    function order_mind() {
        if (od_state == 0) {
            $('.order_remark').css('background', 'wheat');
            od_state = 1;
        } else if (od_state == 1) {
            $('.order_remark').css('background', 'red');
            od_state = 2;
        } else if (od_state == 2) {
            $('.order_remark').css('background', 'pink');
            od_state = 3;
        } else {
            $('.order_remark').css('background', 'gray');
            od_state = 0;
        }
        setTimeout('order_mind()', 500);
    }
    order_mind();
</script>
<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery.lazyload.js"></script>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        $("img").lazyload({
            effect : 'fadeIn',
            threshold : 200
        });
    })
</script>
</html>

<!-- ???????????????????????? -->
<%
	request.getSession().removeAttribute("cgtz");
%>