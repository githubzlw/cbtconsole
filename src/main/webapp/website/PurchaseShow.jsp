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
	<title>采购管理</title>
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
			font-family: "微软雅黑"
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
			top: 250px;
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
                document.getElementById("tip_"+od_id).innerHTML = "卖家还未发货";
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
                            var temp = "<a target='_blank' href='"+data[0].goods_url+"'>商品原链接</a>";
                            $("#"+Goodsdata_id+index+"_ylj").html(temp);
                        }
                        typeHtml += (data[i].goods_type+"<br/>");
                    }
                    $("#"+Goodsdata_id+index+"_ygg").html("<br/>"+typeHtml);
                } else{
                    $("#"+Goodsdata_id+index+"_ygg").html("<br/>无");
                }
            }});
    }
    //多货源
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
                    $("#dhy_hmsgid").html("添加成功");
                    window.setTimeout(function(){
                        $("#divdhy").hide();
                    },1500);

                }else{
                    $("#dhy_hmsgid").html("添加失败");
                }
            }
        });

    }

    //补货
    function insertOrderReplenishment(){
        var rep_type = $("input[name='bh_rep_type']:checked").val();
        if($("input[name='bh_rep_type']:checked").size()<1){
            $("#hmsgid").html("请选择补货类型");
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
            success : function(data){  //返回受影响的行数
                if(Number(data)>0){
                    $("#hmsgid").html("添加成功");
                    var dom=document.getElementById("is_replenishment");
                    dom.setAttribute("style","background-color:red;") ;
                }else{
                    $("#hmsgid").html("添加失败");
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
                    html += "<tr><td colspan='5' align='center'>暂无线下采购记录。</td></tr>";
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
            success : function(data){  //返回受影响的行数
                var objlist = eval("("+data+")");
                var html="";
                for(var i=0; i<objlist.length; i++){
                    html +="<tr><td width='10%'>" + objlist[i].acount + "</td><td width='10%'>" + objlist[i].price + "</td><td width='10%'>" + objlist[i].createtime + "</td><td width='10%'>" + objlist[i].admuserid + "</td><td width='50%'><a href='" + objlist[i].goods_p_url + "' target='block'>" + objlist[i].goods_p_url + "</a></td>" + "<td width='10%'>" + objlist[i].remark + "</td></tr>";
                }
                if(objlist.length<=0){
                    html += "<tr><td colspan='6' align='center'>暂无补货记录。</td></tr>";
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
    // 在本页面弹出采购供应商打分DIV
    function openSupplierDiv(shop_id){
        var rfddd = document.getElementById("supplierDiv");
        rfddd.style.display = "block";
        document.getElementById('su_shop_id').innerHTML="<a href='/cbtconsole/website/shopBuyLog.jsp?shopId="+shop_id+"' target='_blank'>"+shop_id+"</a>";// shop_id;
        $("#hidden_shopId").val(shop_id);
    }
    //在本页面弹出商品打分DIV
    function openSupplierGoodsDiv(goods_pid,shop_id){
        var rfddd = document.getElementById("supplierGoodsDiv");
        rfddd.style.display = "block";
        document.getElementById('su_goods_id').innerHTML= goods_pid;
        document.getElementById('su_goods_p_id').innerHTML= shop_id;
        $("#g_quality").val("0");
        $("#su_g_remark").val("");
    }
    //关闭采购供应商打分DIV
    function FncloseSupplierDiv(){
        var rfddd = document.getElementById("supplierDiv");
        rfddd.style.display = "none";
        document.getElementById('su_shop_id').innerHTML="";
        $("#hidden_shopId").val("");
        $("#quality").val("0");
        $("#su_data").val("");
        $("input[name=protocol]").attr("checked",false);
    }
    //关闭采样商品打分DIV
    function FncloseSupplierGoodsDiv(){
        var rfddd = document.getElementById("supplierGoodsDiv");
        rfddd.style.display = "none";
        document.getElementById('su_goods_id').innerHTML="";
        document.getElementById('su_goods_p_id').innerHTML="";
        $("#g_service option[value='0']").attr("selected","selected");
        $("#g_quality option[value='0']").attr("selected","selected");
        $("#su_g_remark").val("");
    }
    // 提交采购供应商打分数据
    function saveSupplier(){
        var shop_id=$("#hidden_shopId").val();
        if(shop_id == null || shop_id == "" || shop_id == "0000"){
            alert("店铺ID不符合打分规则");
            return;
        }
        var service="0";
        var quality=$("#quality").val();
        var su_data=$("#su_data").val();
        var  protocol=$('input[name="protocol"]:checked').val();
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/supplierscoring/saveproductscord',
            data:{quality:quality,rerundays:su_data,shopId:shop_id,inven:protocol},
            dataType:"json",
            success:function(data){
                if(data.flag == 'success'){
                    alert("采购供应商打分成功");
                    FncloseSupplierDiv();
                }else{
                    alert("采购供应商打分失败");
                }
            }
        });
    }
    //提交采样商品打分数据
    function saveGoodsSupplier(){
        var shop_id=document.getElementById("su_goods_p_id").innerHTML;
        var goods_pid=document.getElementById("su_goods_id").innerHTML;
        var quality=$("#g_quality").val();
        var remark=$("#su_g_remark").val();
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/supplierscoring/saveproductscord',
            data:{quality:quality,remarks:remark,shopId:shop_id,goodsPid:goods_pid},
            dataType:"json",
            success:function(data){
                if(data.flag == 'success'){
                    alert("采样商品打分成功");
                    FncloseSupplierGoodsDiv();
                }else{
                    alert("采样商品打分失败");
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
    }
    function apbhcle2(){
        $("#divdhy input[type='text']").val("");
        $("#divdhy textarea").val("");
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
    //获取 url 后的参数值
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
            alert("已到首页或尾页");
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
            alert("已到首页或尾页");
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
    //添加货源
    function goodsResource(exchange_rate,userid,orderNo,od_id,goodid,goodsdata_id,goods_url,googs_img,goods_price,googs_number,currency,shipping,goodssourcetype,cGoodstype,issure,in_idi,remainingi,lock_remainingi,new_remainingi,shop_ids,straight_address){
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
        $("#taobaospec").val(goodssourcetype);//规格
        $("#shop_id").val(shop_ids);
        $("#straight_address").val(straight_address);
        $("#resource").val(reso);
        document.getElementById('order_count').innerHTML= googs_numberr;
        if(in_idi!=null && in_idi!=""){
            document.getElementById('can_remaining').innerHTML= lock_remainingi;
            $("#in_id").val(in_idi);
            var buycount=document.getElementById("tity_"+orderNo+od_id).innerText;
            if(buycount != null && buycount != '' && buycount != '0'){
                document.getElementById("buycount").value=buycount;
            }else{
                document.getElementById("buycount").value=Number(googs_numberr)-Number(lock_remainingi);
            }
        }else{
            document.getElementById("buycount").value=googs_numberr;
            document.getElementById('can_remaining').innerHTML=0;
            document.getElementById('in_id').innerHTML=0;
        }

        var rmb = document.getElementById("usdprice");
        rmb.value = (goods_pricee * Number(exchange_rate)).toFixed(2) + "  RMB";//保留两位小数
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
                document.getElementById("url_info").innerHTML="新旧货源链接一致";
            }
        }
    }

    function AddResource(type){
        var admid = '${admid}';
        var reason = document.all("otherReason");
        var rereason = "";
        for(var rs=0;rs<reason.length;rs++){
            if(rs==0&&reason[0].value!=null&&$.trim(reason[0].value)!=""){
                rereason=rereason+"砍价情况:"+reason[0].value+"//,";
            } if(rs==1&&reason[1].value!=null&&$.trim(reason[1].value)!=""){
                rereason=rereason+"交期偏长:"+reason[1].value+"//,";
            } if(rs==2&&reason[2].value!=null&&$.trim(reason[2].value)!=""){
                rereason=rereason+"颜色替换:"+reason[2].value+"//,";
            } if(rs==3&&reason[3].value!=null&&$.trim(reason[3].value)!=""){
                rereason=rereason+"尺寸替换:"+reason[3].value+"//,";
            } if(rs==4&&reason[4].value!=null&&$.trim(reason[4].value)!=""){
                rereason=rereason+"订量问题:"+reason[4].value+"//,";
            } if(rs==5&&reason[5].value!=null&&$.trim(reason[5].value)!=""){
                rereason=rereason+"有疑问备注:"+reason[5].value+"//,";
            }if(rs==6&&reason[6].value!=null&&$.trim(reason[6].value)!=""){
                rereason=rereason+"无疑问备注:"+reason[6].value+"//,";
            }
        }

        if(admid==0){
            alert("请选择采购人员");
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
                alert("采购数量不能为空或小于0");
                return;
            }
            if(price==null || price==""){
                alert("采购价格不能为空");
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
                    if(!window.confirm('新旧货源链接一致,确定要重新录入货源吗?')){
                        return;
                    }else{
                        state_flag="1";
                    }
                }
            }
            if(rereason.indexOf("无货源")<=0&&(isNaN(buycount)||isNaN(price)||buycount==""||buycount==null||price==""||price==null)){
                alert("数量与价格必须为数字！货源链接必须以http开头！");
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
                            alert("请输入正确的供应商链接");
                        }else if(st==111){
                            alert("该 > 订单 < 已被取消；请自行刷新页面以确认！");
                            FncloseOut();
                        } else if(st==2){
                            alert("该 > 商品 < 已被取消；请自行刷新页面以确认！");
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
                                            $(this).children().eq(1).html("货源");
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
                                //价格
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
                                            if(ts=='无货源' || ts=='历史货源'){
                                                $(this).html("货源");
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
                                            $(this).children().eq(1).val("货源确认");
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
                                            $(this).children().eq(1).val("采购确认");
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
                                            $(this).children().eq(1).val("入库");
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
                                document.getElementById("rmk_"+orderNo+od_id).innerHTML="货源";
                                document.getElementById("rmk2_"+orderNo+od_id).innerHTML=rereason;
                                document.getElementById("hideDetails_"+orderNo+od_id).style.display="block";
                                $("input[id='hyqr"+orderNo+od_id+"']").val("货源确认");
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
    //计算利润
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
                    actual_freight="已出运，无上传运费";
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


    //全部取消货源
    function allQxQr(orderNo){
        var admid = '${admid}';
        $.ajax({
            type:"post",
            url:'/cbtconsole/purchase/allQxQrNew',
            dataType:"text",
            data:{"orderNo":orderNo,"admid":admid},
            success : function(data){
                if(data.length>0 && data.indexOf("&")>-1){
                    $.jBox.tip('操作成功', 'success');
                    var orders =data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        document.getElementById('hyqr'+orderno+odid).value="货源确认";
                        document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML="";
                        $("#rmk_"+orderno+odid).html("货源");
                        //采购按钮不可以点击
                        $("#"+orderno+odid).attr("disabled", true);
                        jslr(orderno);
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('操作成功', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    document.getElementById('hyqr'+orderno+odid).value="货源确认";
                    document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML="";
                    $("#rmk_"+orderno+odid).html("货源");
                    //采购按钮不可以点击
                    $("#"+orderno+odid).attr("disabled", true);
                    jslr(orderno);
                    window.location.reload();
                }else{
                    $.jBox.tip('操作失败', 'fail');
                }
            }
        });
    }


    //全部确认货源
    function allQr(orderNo){
        var admid = '${admid}';
        $.ajax({
            type:"post",
            url:'/cbtconsole/purchase/allQrNew',
            dataType:"text",
            data:{"orderNo":orderNo,"admid":admid},
            success : function(data){
                if(data.length>0 && data.indexOf("&")>-1){
                    $.jBox.tip('一键确认货源成功', 'success');
                    var orders=data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        var time=orders[i].split(";")[2];
                        document.getElementById('hyqr'+orderno+odid).value="取消货源";
                        document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML=time;
                        $("#rmk_"+orderno+odid).html("货源已确认");
                        $("#"+orderno+odid).removeAttr("disabled");
                        jslr(orderno);
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('一键确认货源成功', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    var time=data.split(";")[2];
                    document.getElementById('hyqr'+orderno+odid).value="取消货源";
                    document.getElementById("puechase_hyqr_time_"+orderno+odid).innerHTML=time;
                    $("#rmk_"+orderno+odid).html("货源已确认");
                    $("#"+orderno+odid).removeAttr("disabled");
                    window.location.reload();
                    jslr(orderno);
                }else{
                    $.jBox.tip('一键确认货源失败', 'fail');
                }
            }
        });
    }
    //************货源确认***************************
    function FnComfirmHyqr(siindex,userid,orderno,od_id,goodsid,goodsdata_id,goods_url,googs_img,goods_price,googs_number,purchaseCount,child_order_no,isDropshipOrder){
        var purchaseComfirmm = document.getElementById('hyqr'+orderno+od_id);
        var admid = '${admid}';
        var goods_title = document.getElementById("id_"+orderno+od_id).value;
        var issure = document.getElementById("rmk_"+orderno+od_id).innerHTML; //$("#rmk_"+orderno+goodsid)
        var newValue= document.getElementById("chk1_"+orderno+od_id).value;
        var oldValue = document.getElementById("chk2_"+orderno+od_id).value;
        if(admid==0){
            alert("请选择采购人员");
            return;
        }else if(oldValue==null || oldValue==""){
            alert("请输入采购价格");
            return;
        }else if(purchaseComfirmm.value=='货源确认'){
            $.jBox.tip("正在操作，清稍候", 'loading');
            document.getElementById('hyqr'+orderno+od_id).value="取消货源";
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
                        alert("该 > 订单 < 已被取消；请自行刷新页面以确认！");
                    } else if(st==2){
                        alert("该 > 商品 < 已被取消；请自行刷新页面以确认！");
                    } else if(st==100) {
                        var today = new Date();
                        //货源确认成功  设置 确认采购可以点击
                        $("#rmk_"+orderno+od_id).html("货源已确认");
                        $("#"+orderno+od_id).removeAttr("disabled");
                        var dd = today.getDate();
                        var mm = today.getMonth()+1; //一月是0，一定要注意
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
                        $.jBox.tip('成功', 'success');
                        jslr(orderno);
                    } else if(st==0){
                        $.jBox.tip('失败。', 'error');
                    }
                },complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('成功', 'success');
                    }
                }
            });
        } else {
            $.jBox.tip("正在操作，清稍候", 'loading');
            document.getElementById('hyqr'+orderno+od_id).value="货源确认";
            document.getElementById("puechase_hyqr_time_"+orderno+od_id).innerHTML="";
            var ajaxTimeOut =$.ajax({
                type:'POST',
                timeout : 4000,
                url:'/cbtconsole/PurchaseServlet?action=PurchaseComfirmOneQxhy&className=Purchase',
                data:{orderNo:orderno,odid:od_id,adminid:admid},
                success:function(i){
                    if(i!=0){
                        $("#rmk_"+orderno+od_id).html("货源");
                        //采购按钮不可以点击
                        $("#"+orderno+od_id).attr("disabled", true);
                        $.jBox.tip('成功', 'success');
                        document.getElementById("puechase_hyqr_time_"+orderno+od_id).innerHTML="";
                        jslr(orderno);
                    } else {
                        $.jBox.tip('失败。', 'error');
                        document.getElementById('hyqr'+orderno+od_id).value="取消货源";
                    }
                },complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('成功', 'success');
                    }
                }
            });
        }
    }


    var cgqrlistmap= new Array();
    var cgqrlistmapIndex = 0;

    //一键取消采购
    function allQxcgQr(orderNo){
        var admid = '${admid}';
        $.ajax({
            type:"post",
            url:'/cbtconsole/purchase/allQxcgQrNew',
            dataType:"text",
            data:{"orderNo":orderNo,"admid":admid},
            success : function(data){
                if(data.length>0 && data.indexOf("&")>-1){
                    $.jBox.tip('操作成功', 'success');
                    var orders=data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        document.getElementById(orderno+odid).value="采购确认";
                        document.getElementById("puechase_time_"+orderno+odid).innerHTML="";
                        $("#rmk_"+orderno+odid).html("货源已确认");
                        $("#hyqr"+orderno+odid).removeAttr("disabled"); //货源可以点击
                        $("#ruku_id_"+orderno+odid).attr("disabled", true);  //入库不可以点击
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('操作成功', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    document.getElementById(orderno+odid).value="采购确认";
                    document.getElementById("puechase_time_"+orderno+odid).innerHTML="";
                    $("#rmk_"+orderno+odid).html("货源已确认");
                    $("#hyqr"+orderno+odid).removeAttr("disabled"); //货源可以点击
                    $("#ruku_id_"+orderno+odid).attr("disabled", true);  //入库不可以点击
                }else{
                    $.jBox.tip('操作失败', 'fail');
                }
            }
        });
    }


    //一键确认采购
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
                    $.jBox.tip('操作成功', 'success');
                    var orders=data.split("&");
                    for(var i=0;i<orders.length;i++){
                        var orderno=orders[i].split(";")[0];
                        var odid=orders[i].split(";")[1];
                        var time=orders[i].split(";")[2];
                        document.getElementById(orderno+odid).value="取消采购";
                        document.getElementById("puechase_time_"+orderno+odid).innerHTML=time;
                        document.getElementById("inventory_"+orderno+odid).innerHTML="";
                        $("#rmk_"+orderno+odid).html("货源已采购");
                        $("#hyqr"+orderno+odid).attr("disabled", true); //货源确认不可点击
                    }
                    window.location.reload();
                }else if(data.length>0){
                    $.jBox.tip('操作成功', 'success');
                    var orderno=data.split(";")[0];
                    var odid=data.split(";")[1];
                    var time=data.split(";")[2];
                    document.getElementById(orderno+odid).value="取消采购";
                    document.getElementById("puechase_time_"+orderno+odid).innerHTML=time;
                    document.getElementById("inventory_"+orderno+odid).innerHTML="";
                    $("#rmk_"+orderno+odid).html("货源已采购");
                    $("#hyqr"+orderno+odid).attr("disabled", true); //货源确认不可点击
                    window.location.reload();
                }else{
                    $.jBox.tip('操作失败', 'fail');
                }
            }
        });
    }

    //****************采购确认****************
    function FnComfirm(userid,orderno,od_id,goodsid,goodsdata_id,goods_url,googs_img,goods_price,googs_number,purchaseCount,child_order_no,isDropshipOrder, that){
        $.jBox.tip("正在操作，請稍候", 'loading');
        var purchaseComfirmm = document.getElementById(orderno+od_id);
        var admid = '${admid}';
        var goods_title = document.getElementById("id_"+orderno+od_id).value;
        var issure = document.getElementById("rmk_"+orderno+od_id).innerHTML;
        var newValue= document.getElementById("chk1_"+orderno+od_id).value;
        var oldValue = document.getElementById("chk2_"+orderno+od_id).value;
        var goods_p_url=document.getElementById("chk_"+orderno+od_id+"").innerHTML;
        var websiteType = $(that).parent().find("select[name=websiteType]").val();

        if(admid==0){
            alert("请选择采购人员");
        } else if(purchaseComfirmm.value=='采购确认'){
            if(issure=='货源'||issure=='历史货源'||issure=='客户已同意替换'||issure=='货源已确认' ||issure=='货源已采购'){
                document.getElementById(orderno+od_id).value="取消采购";
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
                            $("#rmk_"+orderno+od_id).html("货源已采购");
                            $("#hyqr"+orderno+od_id).attr("disabled", true);
                            document.getElementById("inventory_"+orderno+od_id).innerHTML="";
                            $("#ruku_id_"+orderno+od_id).removeAttr("disabled");
                            $.jBox.tip('成功。', 'success');
                            var today = new Date();
                            var dd = today.getDate();
                            var mm = today.getMonth()+1; //一月是0，一定要注意
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
                            $.jBox.tip('失败。', 'error');
                            document.getElementById(orderno+od_id).value="采购确认";
                        } else if(st== -1 || st== "-1"){
                            $.jBox.tip('当前订单或者商品已取消，请刷新界面后操作', 'error');
                        }
                    },
                    complete : function(XMLHttpRequest,status){
                        if(status=='timeout'){
                            ajaxTimeOut.abort();
                            $.jBox.tip('成功。', 'success');
                        }
                    }
                });
            } else {
                $.jBox.tip('无货源,无法确认采购！', 'fail');
            }
        } else {
            document.getElementById(orderno+od_id).value="采购确认";
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
                        $("#rmk_"+orderno+od_id).html("货源已确认");
                        $("#hyqr"+orderno+od_id).removeAttr("disabled");
                        $("#ruku_id_"+orderno+od_id).attr("disabled", true);
                        $.jBox.tip('成功。', 'success');
                    } else {
                        $.jBox.tip('失败。', 'error');
                        document.getElementById(orderno+od_id).value="取消确认";
                    }
                },
                complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('成功。', 'success');
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
                    alert("该 > 订单/商品 < 已被取消；请刷新页面以确认！");
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
            alert("请选择采购人员");
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
            alert("请选择采购人员");
        } else {
            $.jBox.tip("正在操作，清稍候", 'loading');
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
                        $("#rmk_"+orderno+odid).html("货源已入库");
                        $.jBox.tip('成功。', 'success');
                        document.getElementById("ruku_id_"+orderno+odid).disabled=true;
                        window.setTimeout(function(){
                            document.getElementById("puechase_ruku_"+orderno+odid).innerHTML="";
                        },1500);
                    } else {
                        $.jBox.tip('失败。', 'error');
                    }
                },
                complete : function(XMLHttpRequest,status){
                    if(status=='timeout'){
                        ajaxTimeOut.abort();
                        $.jBox.tip('成功。', 'success');
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
            $.jBox.tip('获取登录ID错误', 'tip');
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
        $("input[name='radio']").removeAttr('checked');
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
            alert("请选择该笔线下采购是否有支出金额");
            return;
        }else if(type=="0"){
            taobaoPrice="0.00";
        }
        if (admName == 68) {
            admName = "cerongby4";
        } else if (admName == 57) {
            admName = "策融by2";
        } else if (admName == 58) {
            admName = "策融by3";
        } else if (admName == 53) {
            admName = "策融by1";
        } else if (admName == 59) {
            admName = "策融by5";
        }else if(admName == 61){
            admName="策融test";
        }
        $.ajax({
            type : 'POST',
            url : '/cbtconsole/purchase/insertSources',
            dataType : 'text',
            data : {
                shipno : shipno,
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
                    alert("录入成功");
                } else if (date == "3") {
                    alert("录入失败");
                } else if (date == "1") {
                    alert("该商品对于的线下采购记录已存在");
                } else {
                    alert("未知错误");
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
            alert("获取订单号失败，请关闭后重试");
            return;
        }
        if (od_id == null || od_id == "") {
            alert("获取订单详情id失败，请关闭后重试");
            return;
        }
        if (goodsid == null || goodsid == "") {
            alert("获取商品id失败，请关闭后重试");
            return;
        }
        if (remarkContent == null || remarkContent == "") {
            alert("请填写备注信息");
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

    function useInventory(od_id, state, orderid, goodsid) {
        $.ajax({
            type : "post",
            url : '/cbtconsole/purchase/useInventory',
            dataType : "text",
            data : {
                "od_id" : od_id,
                "isUse" : state
            },
            success : function(data) {
                document.getElementById("use_" + orderid + od_id).style.display = "none";
                if (state == 1) {
                    //使用库存
                    document.getElementById("inventory_" + orderid+ od_id).innerHTML = "仓库人员库存确认中";
                    document.getElementById("hyqr" + orderid + od_id).disabled = true;
                    document.getElementById("hyqr" + orderid + od_id).setAttribute("style","background-color:darkgray;");
                    document.getElementById("" + orderid + od_id).disabled = true;
                    document.getElementById("" + orderid + od_id).setAttribute("style","background-color:darkgray;");
                } else {
                    document.getElementById("inventory_" + orderid+ od_id).innerHTML = "";
                    document.getElementById("hyqr" + orderid + od_id).disabled = false;
                    document.getElementById("hyqr" + orderid + od_id).setAttribute("style", "background-color:");
                    document.getElementById("" + orderid + od_id).disabled = false;
                    document.getElementById("" + orderid + od_id).setAttribute("style", "background-color:");
                }
            }
        });
    }

    function getInventory(orderid, goodsid, odremark, purchase_state,odid) {
        if (odremark == "库存充足") {
            document.getElementById("inventory_" + orderid + odid).innerHTML = "库存充足,无需采购";
            return;
        } else if (odremark.indexOf("库存不足") > -1) {
            document.getElementById("inventory_" + orderid + odid).innerHTML = odremark;
            odremark = odremark.replace(/[^0-9]/ig, "");
            if (odremark == null || odremark == "null" || odremark == "") {
                odremark = 1;
            }
            document.getElementById("tity_" + orderid + odid).innerHTML = odremark;
            return;
        } else if (odremark.indexOf("请重新采购") > -1) {
            document.getElementById("inventory_" + orderid + odid).innerHTML = "库存为0,请重新采购";
            return;
        } else if (odremark.indexOf("疑似有库存") > -1) {
            document.getElementById("inventory_" + orderid + odid).innerHTML = odremark;
            return;
        } else if (odremark.indexOf("不使用库存") > -1) {
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
                            document.getElementById("inventory_" + orderid+ goodsid).innerHTML = "仓库人员库存确认中";
                            document.getElementById("hyqr" + orderid+ odid).disabled = true;
                            document.getElementById("hyqr" + orderid + odid) .setAttribute("style", "background-color:darkgray;");
                            document.getElementById("" + orderid + odid).disabled = true;
                            document.getElementById("" + orderid + odid).setAttribute("style", "background-color:darkgray;");
                        } else if (Number(can_remaining) > 0 && Number(is_use) == 0) {
                            document.getElementById("inventory_" + orderid+ odid).innerHTML = "";
                            document.getElementById("hyqr" + orderid+ odid).disabled = true;
                            document.getElementById("use_" + orderid+ odid).style.display = "block";
                            document.getElementById("hyqr" + orderid + odid) .setAttribute("style", "background-color:darkgray;");
                            document.getElementById("" + orderid + odid).disabled = true;
                            document.getElementById("" + orderid + odid).setAttribute("style","background-color:darkgray;");
                        }
                    }
                }
            });
    }

    // 广东确认直发
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
                    $.jBox.tip('操作成功', 'success');
                    window.location.reload();
                } else {
                    $.jBox.tip('操作失败', 'fail');
                }
            }
        });
    }


    function clearNoNum(obj) {
        //检查是否是非数字值
        if (isNaN(obj.value)) {
            obj.value = "";
        }
        if (obj != null) {
            //检查小数点后是否对于两位
            if (obj.value.toString().split(".").length > 1
                && obj.value.toString().split(".")[1].length > 2) {
                alert("小数点后多于两位！");
                obj.value = "";
            }
        }
    }

    //备注回复
    function doReplay1(orderid, goodsid,odid) {
        $("#remark_content_").val("");
        $("#rk_orderNo").val(orderid);
        $("#rk_goodsid").val(goodsid);
        $("#rk_odid").val(odid);
        var rfddd = document.getElementById("repalyDiv1");
        rfddd.style.display = "block";
    }
    //添加采样反馈
    function openSampleRemark(od_id) {
        $("#sample_remark").val("");
        $("#sample_od_id").val(od_id);
        console.log("od_id="+od_id);
        var rfddd = document.getElementById("repalyDiv2");
        rfddd.style.display = "block";
    }

    //增加商品沟通信息
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
            alert("请选择需要重新分配采购的商品");
            $("#buyuser1").val("");
        }else{
            $.ajax({
                url:"/cbtconsole/orderDetails/changeOrderBuyer.do",
                type:"post",
                dataType:"json",
                data : {"orderid":orderid,"admuserid":admuserid,"odids":check_val.toString()},
                success:function(data){
                    if(data.ok){
                        $("#buyuserinfo").text("执行成功");
                    }else{
                        $("#buyuserinfo").text("执行失败");
                    }
                    window.location.reload();
                },
                error : function(res){
                    $("#buyuserinfo").text("执行失败,请联系管理员");
                }

            });
        }
    }

    //手动调整采购人员
    function changeBuyer(odid,buyid){
        $.ajax({
            url:"/cbtconsole/orderDetails/changeBuyer.do",
            type:"post",
            dataType:"json",
            data : {"odid":odid,"admuserid":buyid},
            success:function(data){
                if(data.ok){
                    $("#info"+odid).text("执行成功");
                }else{
                    $("#info"+odid).text("执行失败");
                }
                window.location.reload();
            },
            error : function(res){
                $("#info"+odid).text("执行失败,请联系管理员");
            }
        });
    }

    function checkAll(order_noo) {
        if ($190("#fpall_" + order_noo + "").prop("checked") == true) {
            $190("input[name='fp_" + order_noo + "']").prop('checked', true);//全选
            $190("#tabId tr:not(:first)").each(
                function() {
                    if ($190(this).css("display") == "none") {
                        $190(this).find("input[name='fp_" + order_noo + "']")
                            .prop('checked', false);
                    }
                });
        } else {
            $190("input[name='fp_" + order_noo + "']").prop('checked', false);//反选
        }
    }
    //录入样品反馈信息doReplay1
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
                    alert("添加采样商品反馈成功");
                    var rfddd = document.getElementById("repalyDiv2");
                    rfddd.style.display = "none";
                    $("#sample_remark").val("");
                    $("#sample_od_id").val("");
                }else{
                    alert("添加采样商品反馈失败");
                }
            }
        });
    }


    /**
	 显示商品替换日志
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
                    html += "<tr><td colspan='6' align='center'>暂无替换记录。</td></tr>";
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
                    html +="<tr><td width='11%'><a target='_blank' href='"+objlist[i].car_url+"'>" + objlist[i].goodsname.substring(0,15) + "</a></td><td width='11%'>" + objlist[i].goods_p_price + "</td><td width='11%'>" + objlist[i].buycount + "</td><td width='10%'>" + objlist[i].admName + "</td><td width='11%'>" + objlist[i].createtime + "</td>" + "<td width='11%'>" + objlist[i].company + "</td><td width='11%'>" + objlist[i].level + "</td><td width='12%'><span id='"+objlist[i].od_id+"_remark'>" + objlist[i].remark + "</span></td><td width='11%'><input type='button' onclick='openSampleRemark("+objlist[i].od_id+");' value='录入样品反馈' /></td></tr>";
                }
                if(objlist.length<=0){
                    html += "<tr><td colspan='8' align='center'>暂无采样记录。</td></tr>";
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
            alert("请输入采购价格");
            return;
        }
        if(isNaN(price)){
            alert("采购价格只能为数字");
            return;
        }else if(Number(price)<=0){
            alert("采购价格必须大于0");
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
                    alert("更新成功");
                    $("#prc_"+orderid+""+odid+"").val(price);
                }else{
                    $("#prc_"+orderid+""+odid+"").val(old_price);
                    alert("更新采购价失败");
                }
            },
            error : function(e) {
                alert("更新采购价失败");
            }
        });
    }

    //商品授权操作
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
				    alert("操作失败，请联系管理员");
				}
            }
        });
	}
    //判断checkbox是否选中
    function isCheckbox(name){
        obj = document.getElementsByName(name);
        var check_val = "";
        for(k in obj){
            if(obj[k].checked)
                check_val= obj[k].value;
        }
        return check_val;
    }
    //发起退货
    function returnNum(odid,cusorder,pid,num) {

    	document.getElementById('cusorder').value=cusorder;
    	document.getElementById('num').value=num;
    	document.getElementById('odid').value=odid;
        document.getElementById('pid').value=pid;
        $.post("/cbtconsole/Look/getpid", {
            cusorder:cusorder,pid:pid
        }, function(res) {
            if(res.rows == 0){
                alert('该订单已全部发起退货');
                return;
            }else if(res.rows == 1){
                alert('该商品还未采购可直接取消采购');
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
        	alert('退货数量不能大于总数量');
        	return;
        }
		  $.post("/cbtconsole/Look/AddOrderByOdid", {
				number:number,cusorder:cusorder,returnNO:returnNO,odid:odid,num:num,pid:pid
			}, function(res) {
				if(res.rows == 0){
					alert('修改成功');
				}else if(res.rows == 1){
					alert('该订单已发起退货');
				}
			else if(res.rows == 2){
				alert('请填写数据');
				return;
			}
			else if(res.rows == 3){
				alert('该商品还未采购可直接取消采购');
				return;
			}else if(res.rows == 4){
				alert('退货数量不可大于总数');
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
                alert('退货成功');
                $("#th" + cusorder).html("");
                $("#th" + cusorder).append("最后退货时间" + res.footer);
            } else {
                alert('不可重复退单');
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
                    $("#tabl").append("<tr ><td style='width:20px'>选择</td><td>产品名</td><td>产品规格</td><td>可退数量</td><td>退货原因</td><td>退货数量</td></tr>");
                    $(msg.rows).each(function (index, item) {

                        $("#tabl").append("<tr ><td ><input type='checkbox' onclick='this.value=this.checked?1:0' style='width:20px' name='" + item.item + "' id='c1' /></td><td>" + item.item + "</td><td>" + item.sku + "</td><td>" + item.itemNumber + "</td><td>" + item.returnReason + "</td><td>" + item.changeShipno + "</td></tr>");

                    });
                } else {
                    alert("订单已全部退货")
                    $('#user_remark').window('close');

                }
            }
        });
    }
    function returnOr(uid,tbor) {
        $('#user_remark .remark_list').html('');
        $("#user_remark input[name='userid']").val(uid);
        $('#new_user_remark').val('');
        //查询历史备注信息
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
                    $("#tabl").append("<tr ><td>选择</td><td>产品pid</td><td>产品规格</td><td>可退数量</td><td>退货原因</td><td>退货数量</td></tr>");
                    $(msg.rows).each(function (index, item) {

                        $("#tabl").append("<tr ><td ><input type='checkbox' onclick='this.value=this.checked?1:0' name='" + item.item + "' id='c1' /></td><td><a href='https://www.importx.com/goodsinfo/122916001-121814002-1" + item.item + ".html' target='_blank' >" + item.item + "</a></td><td>" + item.sku + "</td><td>" + item.itemNumber + "</td><td>" + item.returnReason + "</td><td>" + item.changeShipno + "</td></tr>");

                        /* $("table").append("<tr ><td >1</td><td>"+item.item+"</td><td>产品规格</td><td>"+item.itemNumber+"</td><td></td><td></td></tr>"); */

                    });
                    $('#user_remark .remark_list').html(temHtml);
                    $(msg.rows1).each(function (index, item) {

                        $("#select_id").append("<option id='' value='" + item.a1688Order + "'>" + item.a1688Order + "</option>");
                        // $('#user_remark').window('open');
                        document.getElementById("user_remark").style.display = "";

                    })
                } else {
                    alert("订单已全部退货或者还未发货")

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
                alert('退货成功');
                $("#th" + cusOrder).html("");
                $("#th" + cusOrder).append("最后退货时间" + res.footer);
            } else if (res.rows == 0) {
                alert('不可重复退单');
            } else if (res.rows == 2) {
                alert('请勾选要退的商品');
            } else if (res.rows == 3) {
                alert('请填写数据');
            }
            else if (res.rows == 4) {
                alert('退货数量不能大于可退数量');
            } else if (res.rows == 5) {
                alert('该商品已全部退货');
                getItem();
            }
            getItem();
        });
    }

</script>

<body onload="FnLoading();" id="bodyid" style="background-color: #F0FFFF;">

<div id="user_remark" class="qod_pay3" title="退货申请"
	 style="width:800px;height:auto;display: none;font-size: 16px;">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseOut()">╳</a>
	</div>
	<div id="sediv" style="margin-left:20px;">
		选择1688订单号： <select id="select_id" onchange="getItem()"></select>
		<div>公司订单：<span id="cuso"></span></div>
		<table id="tabl" border="1" cellspacing="0">
			<tr>
				<td style='width:20px'>选择</td>
				<td>产品pid</td>
				<td>产品规格</td>
				<td>可退数量</td>
				<td>退货原因</td>
				<td>退货数量</td>
			</tr>
		</table>
	</div>
	<div style="margin:20px 0 20px 40px;">
		<a href="javascript:void(0)" class="easyui-linkbutton"
		   onclick="addUserRemark()" style="width:80px">提交申请</a>部分退单选择此按钮，全单退可以使用下方按钮
	</div>
	<div style="margin:20px 0 20px 40px;">
		1688订单：<input class="but_color" type="button" value="整单提交" onclick="AddOll()">
		<input type='radio' size='5' name='radioname' value='客户退单' id='c'/>客户退单
		<input type='radio' size='5' name='radioname' value='质量问题' id='c'/>质量问题
		<input type='radio' size='5' name='radioname' value='客户要求' id='c'/>客户要求
	</div>
</div>


 <%--<div id="user_remark" class="easyui-window" title="退货申请"--%>
         <%--data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"--%>
         <%--style="width:400px;height:auto;display: none;font-size: 16px;">--%>
	 <%--<div id="sediv" style="margin-left:20px;">--%>
		 <%--选择1688订单号： <select id="select_id" onchange="getItem()"></select>--%>
		 <%--<div>公司订单：<span id="cuso"></span></div>--%>
		 <%--<table id="tabl" border="1" cellspacing="0">--%>

			 <%--<tr>--%>
				 <%--<td style='width:20px'>选择</td>--%>
				 <%--<td>产品pid</td>--%>
				 <%--<td>产品规格</td>--%>
				 <%--<td>可退数量</td>--%>
				 <%--<td>退货原因</td>--%>
				 <%--<td>退货数量</td>--%>
			 <%--</tr>--%>
		 <%--</table>--%>
	 <%--</div>--%>
            <%--<div style="margin:20px 0 20px 40px;">--%>
                <%--<a href="javascript:void(0)" class="easyui-linkbutton"--%>
                   <%--onclick="returnNu()" style="width:80px" >提交申请</a>--%>
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
	<a href="#top" class='hh'>↑↑</a>
</div>
<div id="totopdiv2" class="show_up" onmouseout="FnHideUp()"
	 style="display: none">
	<a href="javascript:scroll(0,0)" class='gg'>返回顶部</a>
</div>
<div id="alertdiv" class="mod_pay2" align="center"></div>
<div id="bigestdiv" class="mod_pay1" onclick="alertdivHide();"></div>
<div id="operatediv" class="loading"></div>
<div class="mod_pay3" style="display: none;" id="rfddd">
	<center>
		<h3 class="show_h3">录入/修改货源</h3>
		<div>
			<a href="javascript:void(0)" class="show_x" onclick="FncloseOut()">╳</a>
		</div>
		<div>
			订单数量：<span id="order_count" style="margin-right: 20px; color: red;">0</span>使用库存数量:<span
				id="can_remaining">0</span>
		</div>
		<input type="hidden" id="in_id" />
		<div>
			商品数量：<input type="text" name="buycount" id="buycount" class="remark" />
		</div>
		<div>
			商品规格：<input type="text" name="taobaospec" id="taobaospec"
						class="remark" disabled="disabled" />
		</div>
		<div>
			是否免邮：<input type="text" name="shipping" id="shipping" class="remark"
						disabled="disabled" />
		</div>
		<div>
			订单价格：<input type="text" name="usdprice" id="usdprice" class="remark"
						disabled="disabled" />
		</div>
		<div id="showdetail" style="display: none;">
			<font size="2px" color="wheat">采购价偏高</font>
		</div>
		<div>
			采购价格：<input type="text" name="price" id="price" class="remark"
						onblur="judgePurchase();" />
		</div>
		<div>
			采购货源：
			<textarea style="width: 470px" name="resource" id="resource" onBlur="check_url()" class="remarktwo"></textarea>
		</div>
		<div>
			<span id="url_info" style="color: red;"></span>
		</div>
		<h3 class="show_h3">采购备注：</h3>
		<div>
			<span id="otherReason0">砍价情况：</span><input type="text"
													   name="otherReason" id="otherReason" class="remark" /><br> <span
				id="otherReason1">交期偏长：</span><input type="text" name="otherReason"
													 id="otherReason" class="remark" /><br> <span
				id="otherReason2">颜色替换：</span><input type="text" name="otherReason"
													 id="otherReason" class="remark" /><br> <span
				id="otherReason3">尺寸替换：</span><input type="text" name="otherReason"
													 id="otherReason" class="remark" /><br> <span
				id="otherReason4">订量问题：</span><input type="text" name="otherReason"
													 id="otherReason" class="remark" /><br> <span
				id="otherReason5">有疑问备注:</span><input type="text"
													  name="otherReason" id="otherReason" class="remark" /><br> <span
				id="otherReason5">无疑问备注:</span><input type="text"
													  name="otherReason" id="otherReason" class="remark" /><br>
		</div>
		<input type="button" id="idAddResource" value="提交"
			   onclick="AddResource(1);"
			   style="width: 90px; height: 40px; margin-top: 20px;" /> <input
			type="button" value="取消" onclick="FncloseOut();"
			style="width: 90px; height: 40px;" /> <input type="button"
														 id="allidAddResource" value="全部替换" onclick="AddResource(2);"
														 style="color: red; width: 90px; height: 40px; margin-top: 20px;" />
		<br />
		<p style="color: aqua;">
			注意事项：<br /> 1.全部替换 是替换 该订单下 原链接一样 的所有商品。 <br /> 2.前提是 这些商品 以前就录入过
			货源,或者 确认过货源 <br /> 3.全部替换 会将 被替换商品 的 商品状态 改为 未确认货源 <br /> 以前做的
			确认货源 确认采购 入库 操作 需要重新开始。<br />
		</p>

	</center>
</div>


<div class="mod_pay3" style="display: none;" id="insertOrderInfo">
	<center>
		<h3 class="show_h3">线下采购</h3>
		<div>
			快递单号：<input type="text" name="shipno" id="shipno" class="remark" style="width: 250px;"/>
			<label><input type="radio" name="radio" value="0"/>无支付宝支出（或公账支出）</label>
			<label><input type="radio" name="radio" value="1"/>有支付宝支出</label>
		</div>
		<div>
			<input type="hidden" name="taobaoPrice" id="taobaoPrice"
				   class="remark" />
			<input type="hidden" name="taobao_odid" id="taobao_odid"
				   class="remark" />
		</div>
		<div>
			<input type="hidden" name="taobaoFeight" id="taobaoFeight"
				   class="remark" />
		</div>
		<div>
			<input type="hidden" name="goodsQty" id="goodsQty" class="remark" />
		</div>
		<div>
			<input type="hidden" name="paydate" id="paydate" class="remark" />
		</div>
		<div>
			<input type="hidden" name="delivary_date" id="delivary_date"
				   class="remark" />
		</div>
		<div>
			<input type="hidden" name="taobao_url" id="taobao_url"
				   class="remark" />
		</div>
		<div>
			<input type="hidden" name="goods_sku" id="goods_sku" class="remark" />
		</div>
		<div>
			<input type="hidden" name="taobao_name" id="taobao_name"
				   class="remark" />
		</div>
		<input type="hidden" id="goods_imgs"> <input type="hidden"
													 id="TbOrderid"> <input type="hidden" id="TbGoodsid"><input type="hidden" id="TbOdid">
		<input type="button" id="idAddResource" value="提交"
			   onclick="insertSources();"
			   style="width: 90px; height: 40px; margin-top: 20px;" /> <input
			type="button" value="取消" onclick="FncloseInsert();"
			style="width: 90px; height: 40px;" />
	</center>
</div>


<div class="mod_pay3" style="display: none;" id="repalyDiv1">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv1').hide();">╳</a>
	</div>
	<input id="rk_orderNo" type="hidden" value=""> <input
		id="rk_odid" type="hidden" value=""><input
		id="rk_goodsid" type="hidden" value=""> 回复内容:
	<textarea name="remark_content" rows="8" cols="50"
			  id="remark_content_"></textarea>
	<input type="button" id="repalyBtnId" onclick="saveRepalyContent()"
		   value="提交回复">
</div>

<div class="mod_pay3" style="display: none;" id="repalyDiv4">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv4').hide();">╳</a>
	</div>
	质量:<input type="radio" name="view_quality" value="2">好<input type="radio" name="view_quality" value="1">一般<input type="radio" name="view_quality" value="0">差
	<br>
	交期:<input type="radio" name="view_delivery" value="2">好<input type="radio" name="view_delivery" value="1">一般<input type="radio" name="view_delivery" value="0">差
	<br>
	规格:<input type="radio" name="view_delivery" value="2">好<input type="radio" name="view_delivery" value="1">一般<input type="radio" name="view_delivery" value="0">差
	<br>
	<input id="review_pid" type="hidden" value=""> 评价内容:
	<textarea name="review_content" rows="8" cols="50"
			  id="review_content"></textarea>
	<input type="button"  onclick="saveRepalyContent()"
		   value="提交评价">
</div>

<div class="mod_pay4" style="display: none;" id="repalyDiv2">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv2').hide();">╳</a>
	</div>
	<input id="sample_od_id" type="hidden" value="">反馈内容:
	<textarea name="sample_remark" rows="8" cols="50"
			  id="sample_remark"></textarea>
	<input type="button" id="repalyBtnId" onclick="addSampleRemark()"
		   value="提交反馈">
</div>

<div class="mod_pay4" style="display: none;" id="repalyDiv3">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="$('#repalyDiv3').hide();">╳</a>
	</div>
	<input id="replace_pid" type="hidden" value="">录入替换货源链接:
	<input id="new_replace_url" style="width:500px" type="text" value=""><br>
	<input type="button" id="repalyBtnId" style="margin-left:250px" onclick="replaceUrl()"
		   value="替换1688对标货源">
</div>

<div class="mod_pay3" style="display: none;" id="apbhdiv">
	<center>
		<h3 class="show_h3">补货</h3>

		<h1 style="color: red;" id="hmsgid"></h1>
		<div>
			<a href="javascript:void(0)" class="show_x"
			   onclick="$('#apbhdiv').hide();">╳</a>
		</div>
		<div>
			采购补货<input type="radio" name="bh_rep_type" value="1" />替换弥补<input
				type="radio" name="bh_rep_type" value="2" />
		</div>
		<div>
			数量：<input type="text" id="bh_buycount" class="remark" />
		</div>
		<div>
			价格：<input type="text" id="bh_goods_price" class="remark"  onblur="judgePurchase();" />
		</div>
		<div>
			货源：
			<textarea style="width: 470px" id="bh_goods_p_url" class="remarktwo"></textarea>
		</div>
		<div>
			备注：
			<textarea style="width: 470px" id="bh_remark" class="remarktwo"></textarea>
		</div>
		<input type="button" value="提交" onclick="insertOrderReplenishment();"
			   style="width: 90px; height: 40px; margin-top: 20px;" /> <input
			type="button" value="取消" onclick="$('#apbhdiv').hide();"
			style="width: 90px; height: 40px;" />
	</center>
</div>
<!-- 评论end -->
<div class="mod_pay3" style="display: none; background-color: #FFFFFF;"
	 id="divdhy">
	<center>
		<h1 style="color: red;" id="dhy_hmsgid"></h1>
		<h3 class="show_h3">默认货源</h3>
		<div>
			<a href="javascript:void(0)" class="show_x"
			   onclick="$('#divdhy').hide();">╳</a>
		</div>
		<div>
			价格：<input disabled="disabled" type="text" id="divdhy_jg"
					  class="remark" /><br /> 货源：
			<textarea disabled="disabled" style="width: 470px" id="divdhy_text"
					  class="remarktwo"></textarea>
		</div>

		<h3 class="show_h3">货源1</h3>
		<div>
			价格：<input id="dhy_jg1" type="text" class="remark" /><br /> 数量：<input
				id="dhy_sl1" type="text" class="remark" /><br /> 货源：
			<textarea id="dhy_hy1" style="width: 470px" class="remarktwo"></textarea>
			<br /> <input id="dhy_id1" type="hidden" value="0" class="remark" /><br />
		</div>

		<h3 class="show_h3">货源2</h3>
		<div>
			<input id="dhy_id2" type="hidden" value="0" class="remark" /><br />
			价格：<input id="dhy_jg2" type="text" class="remark" /><br /> 数量：<input
				id="dhy_sl2" type="text" class="remark" /><br /> 货源：
			<textarea id="dhy_hy2" style="width: 470px" class="remarktwo"></textarea>
			<br />
		</div>

		<input type="button" value="提交" onclick="dhyby();"
			   style="width: 90px; height: 40px; margin-top: 20px;" /> <input
			type="button" value="取消" onclick="$('#divdhy').hide();"
			style="width: 90px; height: 40px;" />
	</center>
</div>

<div class="a">
	<!-- 	<a href="javascript:history.back(-1)">返回采购管理</a> -->
	<div>
		<center>
			<c:if test="${admid==999}">
			<span>
			(<input type="checkbox" id="fpall_${orderno}" name="fpall_${orderno}" style="width: 15px; height: 15px;" onclick="checkAll('${orderno}')"><span style="color:red">全选</span>)
				分配此订单的采购人员：<select id="buyuser1" name="buyuser1"
								   style="width: 110px;"
								   onchange="changeOrderBuyer('${orderno}',this.value);">
					</select>
				</span><span id="buyuserinfo"></span>
			</c:if>
			<span>商品状态：</span> <span> <select id="search_state"
											  name="search_state" style="width: 100px;"
											  onkeypress="if (event.keyCode == 13) FnSearch('0');">
						<option value="0" selected="selected">全部</option>
						<option value="1">未采购</option>
						<option value="2">采购中</option>
			<!-- 						<option value="3">已到仓库验货无误</option> -->
				</select>
				</span>
			<span>商品编号/购物车id:<input
					type="text" id="goodid" class="h" value="${goodid}"
					onkeypress="if (event.keyCode == 13) FnSearch('1');" /></span>
			<span>产品名称:<input
					type="text" id="goodname" class="h" value="${goodname}"
					onkeypress="if (event.keyCode == 13) FnSearch('1');" />
					<span>每页大小：</span> <span> <select id="page_size" name="page_size" style="width: 100px;">
						<option value="50" selected="selected">50</option>
						<option value="100">100</option>
						<option value="150">150</option>
						<option value="200">200</option>
						<option value="200">500</option>
				</select>
					</span>
				 <span><input type="button" value="查询" class="ffff"
							  onclick="FnSearch('1');" /></span> <span><input type="button"
																			  value="重置" class="fff" onclick="FnClear();" /></span>
		</center>
	</div>
	<div>
		<div class="remarkAgainDiv" style="display: none;">
			<input id="rk_orderNo" type="hidden" value=""> <input
				id="rk_od_id" type="hidden" value=""> <input
				id="rk_goodsid" type="hidden" value=""> 备注内容: <a
				id="hide_remarkDiv"
				style="color: red; float: right; margin-right: 10px; font-size: 24px; text-decoration: none"
				href="javascript:void(0);">X</a><br>
			<textarea name="remark_content" rows="8" cols="50"
					  id="remark_content"></textarea>
			<font color="red" id="ts"></font><br> <input type="checkbox"
														 id="isPush"> <label for="isPush">是否推送(推送至消息中心)</label> &nbsp;&nbsp;
			<input type="button" id="remarkAgainBtn" onclick="saveRemarkAgain()"
				   value="提交备注">
		</div>
	</div>


	<div>&nbsp;</div>


	<c:if test="${empty pblist}">
		<div style="text-align: center">
			<font style="font-size: 24px;">当前采购人员没有相应数据！可切换到其他采购人员查看</font>
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
								<span class="d">订单号：</span><span class="c">${pb.orderNo}</span>
								<c:if test="${cgid == 52}">
									<a href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=1" target="_blank" style="text-decoration: none"></a>
								</c:if>
								<c:if test="${cgid != 52}">
									<a href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=${cgid}" target="_blank" style="text-decoration: none"></a>
								</c:if>
								<c:if test="${pb.invoice=='1'}">
									<input type="button" value="查看invoice" onclick="window.open('/cbtconsole/autoorder/show?orderid=${pb.orderNo}','_blank')">
								</c:if>
								<c:if test="${pb.invoice=='2'}">
									<input type="button" value="查看invoice" onclick="window.open('/cbtconsole/autoorder/shows?orderid=${pb.orderNo}','_blank')">
								</c:if>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="d">付款日期：</span><span class="c">${pb.paytime}</span>&nbsp;&nbsp;
								<span class="d">客户ID：</span><span class="c">${pb.userid}</span>&nbsp;&nbsp;
								<span class="d">销售负责人：</span><span class="c">${pb.saler}</span>&nbsp;&nbsp;
								<span class="d" id="gj${pb.orderNo}">国家：</span><span class="c">${pb.orderaddress}</span>

								<c:if test="${empty pb.orderremarkNew}">
									<span class="d">客户备注：</span><span class="c">${pb.orderremarkNew}</span>
								</c:if>
								<c:if test="${not empty pb.orderremarkNew}">
									<span class="d" style="color:red;">客户备注：</span><b style="font-size: 20px;background-color:red;color: white;">${pb.orderremarkNew}</b>
								</c:if>
								<br>
									<%--<span class="d">毛利：</span><span id="${pb.orderNo}_span" style="color: green;" class="c"></span> --%>
								<span class="d">客户实际支付金额(￥)：</span><span id="${pb.orderNo}_pay_price" style="color: green;" class="c">--</span>
									<%--<span class="d">预计采购金额(￥):</span><span id="${pb.orderNo}_es_price" style="color: green;" class="c">--</span>--%>
								<span class="d">实际采购金额(￥)：</span><span id="${pb.orderNo}_buyAmount" style="color: green;" class="c">--</span>
								<span class="d">预计重量(购物车重量)(kg):</span><span id="${pb.orderNo}_es_weight" style="color: green;" class="c">--</span>
								<span class="d">预估国际运费：</span><span id="${pb.orderNo}_es_freight" style="color: green;" class="c">--</span> RMB
								<span class="d">实际国际运费：</span><span id="${pb.orderNo}_ac_freight" style="color: green;" class="c">--</span> RMB <br>
								<span class="d">预估利润金额(￥)（预估利润率）:</span><span id="${pb.orderNo}_es_p" style="color: green;" class="c">--</span>
								<span class="d">实际利润金额(￥)（实际利润率）:</span><span id="${pb.orderNo}_ac_p" style="color: green;" class="c">--</span><br>
								<c:if test="${!(pb.orderProblem == null or pb.orderProblem == '')}">
									<span id="OrderProblem${pb.orderNo}" class="d">出库审核不通过的问题描述：${pb.orderProblem}</span><br>
								</c:if>
								<input type="hidden" id="ord_remark_${pb.orderNo}" value="${pb.orderremark}"> <input type="hidden" id="ord2_remark_${pb.orderNo}" value="${fn:substring(pb.orderremark, 0, 117)}"> <span id="ord_i_remark_${pb.orderNo}" style="background-color: lightgreen; color: blue; font-size: 20px;">${fn:substring(pb.orderremark, 0, 117)}</span>${pb.orderremark_btn}
								<span class="d">关联的支付宝订单、金额:</span><span style="width:1500px;display:block;word-wrap:break-word;word-wrap:break-word" id="tborderInfo">${tbInfo}</span>
							</div>
						</td>
					</tr>
					<tr id="ht${pb.orderNo}">
						<td width="25%" colspan="2">商品信息</td>
						<td width="8%">下单信息</td>
						<td width="22%">实际采购信息</td>
						<td width="5%">
                            一键确认采购发送邮件网站名:
                            <select name="websiteType" style="height: 28px;width: 160px;">
                                <option value="1" selected="selected">import-express</option>
                                <option value="2">kidsproductwholesale</option>
                            </select>
                            <br /><br />
                            操作/状态
							<br><input type="button" id="allQr1" style="color: green;" onclick="allQr('${pb.orderNo}')" value="一键确认货源" /><input type="button" id="allcgQr1" style="color: green;" onclick="allcgQr('${pb.orderNo}', this)" value="一键确认采购" /><br/><br/><input type="button" id="allQr2" style="color: red;" onclick="allQxQr('${pb.orderNo}')" value="一键取消货源" /><input type="button" id="allcgQr2" style="color: red;" onclick="allQxcgQr('${pb.orderNo}')" value="一键取消采购" /></td>
						<td width="6%">时间记录</td>
						<td width="4%">操作</td>
						<td width="4%">消息备注</td>
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
								<a href="${pb.importExUrl}" target="_blank">电商网站链接</a>
								<br>
							</c:if>
							<%--<input type="checkbox" name="${pb.orderNo}" value="${pb.orderNo},${pb.od_id}"><span style="color: red">线下申请付款</span>--%>
							<%--<input style="margin-left:55px;" type="checkbox" id="fp_${pb.orderNo}" name="fp_${pb.orderNo}" value="${pb.od_id}">--%>
							<br>
							<c:if test="${admid==999}">
								采购调整:<select
								id="buyer${pb.od_id}"
								onchange="changeBuyer(${pb.od_id},this.value);">
								<option value=""></option>
								<c:forEach var="aub" items="${aublist }">
									<option value="${aub.id }">${aub.admName}</option>
								</c:forEach>
								</select><span id="${orderd.od_id}_buyid">${pb.buyid}</span><span id="info${orderd.od_id}"></span>
							</c:if>
							<br>
							<span>商品号：</span><span>${pb.goodsid};购物车id:${pb.od_id}</span>
							<br>
							<input type="hidden" id="order_${pbsi.index+1}" value="${pb.orderNo}" />
							<input type="hidden" id="goodsid_${pbsi.index+1}" value="${pb.od_id}" />
							<input type="hidden" id="hdgd" value="${pb.goodsdata_id}">
							<div style="width: 100%; word-wrap: break-word;">
								<span>Type：</span><font class="dd">${pb.goods_type}</font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								<span>1688抓取规格：</span><font class="dd">${pb.type_name}</font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								<c:if test="${pb.purchase_state >3}">
								<span>扫描入库对应采购规格：</span>
									${pb.tbSku}
								</c:if>
							</div>
							<br/><br/>
							<span id='${pb.goodsdata_id}${pbsi.index }_ylj'></span>
							<br>
							<c:if test="${pb.od_state == 13}">
								<span><input type="button" value="显示原链接和规格" onclick="getGoodsCar('${pb.goodsdata_id}','${pb.userid}','${pbsi.index}','${pb.goods_price}','${pb.newValue}','${pb.orderNo}');" /></span>
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
								<span>Price：</span>
								<span>${pb.goods_price}&nbsp;${pb.currency}/piece</span>
							</div>
							<div>
								<span>Quantity：</span>
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
							<!-- 采样按钮 -->
							<div style="margin-top: 10px;">
							</div>
							<c:if test="${not empty pb.remarkpurchase}">
								<div style="width: 100%; word-wrap: break-word;">
									<span class="cai_remark">Remark：</span> <font class="cc">${pb.remarkpurchase}</font>
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
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">已到仓库，已校验该到没到</a>
									</h1>
								</c:if>
								<c:if test="${pb.rkgoodstatus == '3'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">已到仓库，已校验破损</a>
									</h1>
								</c:if>
								<c:if test="${pb.rkgoodstatus == '4'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">已到仓库，已校验有疑问</a>
									</h1>
								</c:if>
								<c:if test="${pb.rkgoodstatus == '5'}">
									<h1 style="color: red;">
										<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${pb.od_id}">已到仓库，已校验数量有误</a>
									</h1>
								</c:if>
							</div>
							<span style="color: red; font-size: 25px;" id="inventory_${pb.orderNo}${pb.od_id}"></span>
							<div id="use_${pb.orderNo}${pb.od_id}" style="display: none">
								<button onclick="useInventory('${pb.od_id}',1,'${pb.orderNo}','${pb.goodsid}')">使用库存</button>
								<button onclick="useInventory('${pb.od_id}',0,'${pb.orderNo}','${pb.goodsid}')">不使用库存</button>
							</div>
							<div id="hideDetails_${pb.orderNo}${pb.od_id}" style="display: block">
								<input type="hidden" id="${pb.orderNo}${pbsi.index}_e" value="${pb.oldValue}" /> <input type="hidden" id="${pb.orderNo}${pbsi.index}_eQuantity" value="${pb.purchaseCount}" />
								<h1 style="color: #F00">${pb.cginfo}</h1>
								<span>Price：</span>
									<%-- 									<span id="prc_${pb.orderNo}${pb.goodsid}"> --%>
								<input type="text" id="prc_${pb.orderNo}${pb.od_id}" style="width:50px" value="${pb.oldValue}"
									   onkeypress="if (event.keyCode == 13) updatePrice('${pb.orderNo}','${pb.od_id}','${pb.oldValue}');"/>
								<input type="hidden" value="${pb.goods_title }" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>Quantity：</span>
								<span id="tity_${pb.orderNo}${pb.od_id}">${pb.purchaseCount}</span><br />
								<c:if test="${pb.purchase_state<3}">
									<script type="text/javascript">
                                        getInventory('${pb.orderNo}','${pb.goodsid}','${pb.inventory_remark}','${pb.purchase_state}','${pb.od_id}');
									</script>
								</c:if>
								<div style="width: 100%; word-wrap: break-word; word-break: break-all">
									<c:if test="${pb.shopFlag==1}">
										采购链接/原始货源链接:
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
										原始货源链接:
										<a target="_blank" href="https://detail.1688.com/offer/${pb.goods_pid}.html">https://detail.1688.com/offer/${pb.goods_pid}.html</a><br>
										采购货源链接:
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
										<a style="color:red" href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=1" target="_blank" style="text-decoration: none">[选择其他1688货源]</a>
									</c:if>
									<c:if test="${cgid != 52}">
										<a style="color:red" href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${pb.orderNo}&purchaseId=${admid==999?1:cgid}" target="_blank" style="text-decoration: none">[选择其他1688货源]</a>
									</c:if>
									<%--<a style="color:red" href="/cbtconsole/website/goods_change.jsp?odid=${pb.od_id}" target="_blank" style="text-decoration: none">商品替换</a>--%>
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
									    <input type="button" name="btn_resources" id="btnSet_${pb.orderNo}${pb.od_id}" value="录入/修改货源" onclick="goodsResource(${pb.exchange_rate},'${pb.userid}','${pb.orderNo}','${pb.od_id}','${pb.goodsid}','${pb.goodsdata_id}','${pb.goods_url}','','${pb.goods_price}','${pb.googs_number}','${pb.currency}','${pb.mode_transport}','${pb.goodssourcetype}','${pb.cGoodstype}','${pb.issure }','${pb.in_id }','${pb.remaining }','${pb.lock_remaining }','${pb.new_remaining }','${pb.shop_ids}','${pb.straight_address}');"
											   class="f" onfocus="setStyle(this.id);" />
									</span>
								<span>
									    <c:if test="${pb.is_replenishment==0}">
											<input type="button" value="安排补货" class="f" id="is_replenishment" onclick='apbhcle("${pb.od_id}","${pb.orderNo}","${pb.goodsid}");$("#apbhdiv").show();$("#bh_userid").val("${pb.userid}");$("#bh_odid").val("${pb.od_id}");$("#bh_orderid").val("${pb.orderNo}");$("#bh_goodsid").val("${pb.goodsid}");$("#bh_goods_url").val("${pb.goods_url}");' />
										</c:if> <c:if test="${pb.is_replenishment==1}">
									<input type="button" value="安排补货" class="f" id="is_replenishment" style="background-color: red;" onclick='apbhcle("${pb.od_id}","${pb.orderNo}","${pb.goodsid}");$("#apbhdiv").show();$("#bh_userid").val("${pb.userid}");$("#bh_odid").val("${pb.od_id}");$("#bh_orderid").val("${pb.orderNo}");$("#bh_goodsid").val("${pb.goodsid}");$("#bh_goods_url").val("${pb.goods_url}");' />
								</c:if>
									</span>
								<span>
									    <input type="button" name="btn_resources" id="btnGet_${pb.orderNo}${pb.od_id}" value="其他用过的货源" onclick="OtherSources('${pb.userid}','${pb.orderNo}','${pb.od_id}','${pb.goodsid}','${pb.goodsdata_id}','${pb.importExUrl}','','${pb.goods_price}','${pb.googs_number}','${pb.currency}');" class="f" onfocus="setStyle(this.id);" />
									</span>
								<c:if test="${pb.offline_purchase==0}">
									<input type="button" id="insert_${pb.orderNo}${pb.od_id}" value="线下采购" onclick="AddinsertSources('${pb.newValue}','${pb.purchaseCount}','${pb.cGoodstype}','${pb.googs_img}','${pb.oldValue}','${pb.orderNo}','${pb.goodsid}','${pb.od_id}');" class="f" />
								</c:if>
								<c:if test="${pb.offline_purchase==1}">
									<input type="button" id="insert_${pb.orderNo}${pb.od_id}" style="background-color: red;" value="线下采购" onclick="AddinsertSources('${pb.newValue}','${pb.purchaseCount}','${pb.cGoodstype}','${pb.googs_img}','${pb.oldValue}','${pb.orderNo}','${pb.goodsid}','${pb.od_id}');" class="f" />
								</c:if>
								<c:if test="${pb.authorized_flag==1 || pb.authorized_flag==2}">
									<input type="button" style="background-color: green;"  value="授权该商品" onclick="productAuthorization('${pb.goods_pid}','${pb.od_id}',0);" class="f" />
								</c:if>
								<c:if test="${pb.authorized_flag==0}">
									<input type="button" id="insert_${pb.orderNo}${pb.od_id}" style="background-color: red;" value="取消商品授权" onclick="productAuthorization('${pb.goods_pid}','${pb.od_id}',1);" class="f" />
								</c:if>
								<span id="issure${pb.orderNo}${pb.od_id}">
									<input type="hidden" value="${pb.goods_title }" /><label id="rmk_${pb.orderNo}${pb.od_id}" style="color: red;">${pb.issure}</label></span>
								<span>${pb.originalGoodsUrl}</span> <input type="hidden" id="dhy${pb.orderNo}${pb.od_id}" value="${pb.goods_title}" />
								<br>${pb.noChnageRemark}
							</div>
								<%--<div style="width: 100%; word-wrap: break-word;">--%>
								<%--预估单品利润金额RMB（预估单品利润率%):${pb.profit}--%>
								<%--【预估单品利润金额RMB（预估单品利润率%）=客户实际支付单品金额-实际预估采购单品金额-预估国际单品运费--%>
								<%--</div>--%>

							<c:if test="${pb.replacementProduct!=null}">
								<div style="width: 100%; word-wrap: break-word;">
									<font color="red">客户录入替换商品： <span>${pb.replacementProduct}</span> </font><br>
								</div>
							</c:if>
							<div style="width: 100%; word-wrap: break-word;">
								供应商名字： <span>${pb.shopName}</span> <br>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								供应商评级： <span>${pb.shopGrade}</span> <br>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								同款链接： <a href="https://s.1688.com/collaboration/collaboration_search.htm?fromOfferId=${pb.goods_pid}&tab=sameDesign" target="_blank">链接</a>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								1688起订量:<span>${pb.morder}</span>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								原始货源重量(kg):<font color="green"><span id="cbrWeight_${pb.orderNo}${pb.od_id}">${pb.cbrWeight}</span></font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								预估重量(加入购物车重量)(kg):<font color="green"><span id="esWeight_${pb.orderNo}${pb.od_id}">${pb.carWeight}</span></font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								备注： <font class="cc"><span id="rmk2_${pb.orderNo}${pb.od_id}"> <input type="hidden" value="${pb.goods_title }" /> ${pb.remark}</span></font> <br> <font style="font-size: 24PX; color: midnightblue;">${pb.productState}</font>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								库存备注： <font class="cc"><span>${pb.inventoryRemark}</span></font> <br>
							</div>
							<c:if test="${pb.shopFlag==1}">
								<div style="width: 100%; word-wrap: break-word;">
									供应商地址： 【${pb.shopAddress}】<font class="cc"><span style="color:red">
									<c:if test="${pb.straight_flag==1}">
										【建议直发】
									</c:if>
									<c:if test="${pb.straight_flag==2}">
										【确认直发】【${pb.straight_time}】<a href="/cbtconsole/website/straight_hair_list.jsp?goods_pid=${pb.goods_pid}" target="_blank">直发列表</a>
									</c:if>
									</span></font>
									<c:if test="${pb.straight_flag==1}">
										<%-- <button onclick="determineStraighthair('${pb.orderNo}','${pb.od_id}','${pb.od_id}')">直发</button> --%>
										<button onclick="determineStraighthair('${pb.orderNo}','${pb.goodsid}','${pb.od_id}')">直发</button>
									</c:if>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									供应商授权信息：<span>${pb.authorizedFlag}</span> <br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									供应商级别： <span id="level">${pb.level}</span> <br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									供应商评分： <font class="cc"> <span><a target="_blank" href="/cbtconsole/supplierscoring/supplierproducts?shop_id=${pb.goodsShop}&goodsPid=${pb.goods_pid}&flag=0">${pb.quality}</a></span></font> <br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									供应商ID： <a target="_blank" style="color:red;" title="查看该供应商采购历史记录" href="/cbtconsole/website/shopBuyLog.jsp?shopId=${pb.goodsShop}">${pb.goodsShop}</a><br>
								</div>
								<div style="width: 100%; word-wrap: break-word;">
									供应商库存：${pb.shopInventory}
								</div>
								<div style="width: 170px; word-wrap: break-word;color: #59f775;background-color: black;">
									<b>店铺售卖金额($):${pb.goodsShopPrice}</b>
								</div>
							</c:if>
							<c:if test="${pb.inventory>0}">
								<div style="width: 100%; word-wrap: break-word;">
									可使用库存： <font class="cc"> <span id="rmk2_${pb.orderNo}${pb.od_id}"> <input type="hidden" value="${pb.inventory}" /><a target="_blank" href ="/cbtconsole/StatisticalReport/goodsInventoryReport?sku=${pb.cGoodstype}">${pb.inventory}</a></span></font> <br>
								</div>
							</c:if>
							<c:if test="${pb.pidInventory>0}">
								<div style="width: 100%; word-wrap: break-word;">
									产品有库存(规格不匹配)： <font class="cc"> <span id="rmk2_${pb.orderNo}${pb.od_id}"> <input type="hidden" value="${pb.pidInventory}" /><a target="_blank" href ="/cbtconsole/StatisticalReport/goodsInventoryReport?pid=${pb.goods_pid}">${pb.pidInventory}</a></span></font> <br>
								</div>
							</c:if>
							<div style="width: 100%; word-wrap: break-word;">
								物流信息： <font style="font-size:20px;font-weight:bold;color:blue;"> <span>${pb.shipstatus}</span></font> <br>
							</div>
                            <div style="width: 100%; word-wrap: break-word;">
								淘宝订单号： <font style="font-size:20px;font-weight:bold;color:blue;"> <span>${pb.shipno}</span></font> <br>
							</div>
							<div style="width: 100%; word-wrap: break-word;">
								产品编辑页面打分：<a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=${pb.goods_pid}">产品编辑</a> <br>
							</div>
							<script>
                                FnHideDetails('${pb.newValue}','${pb.orderNo}', '${pb.od_id}', '${pb.od_id}')
							</script>
							<c:if test="${pb.oistate==6}">
								<h1 style="color: red;">该订单已被客户取消</h1>
							</c:if> <c:if test="${pb.saler=='testAdm'}">
							<h1 style="color: red;">该订单为测试订单不要采购</h1>
						</c:if>
							<c:if test="${pb.odstate==2}">
								<h1 style="color: red;">该商品已被取消</h1>
							</c:if>
							<c:if test="${pb.refund_flag==1}">
								<h1 style="color: red;">该商品已退货</h1>
							</c:if>
							<input type="hidden" value="${pb.source_of_goods }">
							<c:if test="${pb.source_of_goods==1 }">
								<h3 style="color: purple;">该产品找不到货源--By:${pb.admin }</h3>
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
                                采购确认发送邮件网站名:
                                <select name="websiteType" style="height: 28px;width: 160px;">
                                    <option value="1" selected="selected">import-express</option>
                                    <option value="2">kidsproductwholesale</option>
                                </select>
                                <br /><br />
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
									<span> 仓库位置：
										 <c:if  test="${pb.position=='' || pb.position==null}">还未存放</c:if>
										 <c:if test="${pb.position != '' || pb.position!=null}">${pb.position}</c:if>
									</span>
							</div>
							<div>
								<span>录入货源：</span><br /> <span style="color: blue">${pb.addtime}</span>
							</div>
							<div>
								<span>货源确认：</span><br />
								<span style="color: blue" id="puechase_hyqr_time_${pb.orderNo}${pb.od_id}">${pb.puechaseTime}</span>
							</div>
							<div>
								<span style="color: red">采购确认：</span>
								<br/>
								<span style="color: red" id="puechase_time_${pb.orderNo}${pb.od_id}">${pb.purchasetime}</span>
							</div>
							<c:if test="${pb.tb_orderid==null}">
								<div>
									<span>库存入库时间：</span><br /> <span style="color: blue">${pb.rukuTime}</span>
								</div>
							</c:if>
							<c:if test="${pb.tb_orderid!=null}">
								<div>
									<span>入库时间：</span><br /> <span style="color: blue">${pb.rukuTime}</span>
								</div>
							</c:if>
							<div>
								<span>完成时间：</span><br /> <span style="color: blue"></span>
							</div>
						</td>
						<td width="4%">
							<input type="hidden" id="re_${pb.orderNo}${pb.od_id}" /> <input type="button" onclick="getIsReplenishment('${pb.orderNo}','${pb.goodsid}');" value="显示补货信息" />
							<input type="button" onclick="getIsOfflinepurchase('${pb.orderNo}','${pb.goodsid}');" value="显示线下采购信息" />
							<!-- 非采样订单不显示该按钮 -->
							<c:choose>
								<c:when test="${pb.shopFlag==1}">
									<input type="button" onclick="displayBuyLog('${pb.goods_pid}','${pb.car_urlMD5}');" value="采样历史日志" />
									<%-- <input type="button" onclick="openSupplierGoodsDiv('${pb.goods_pid}','${pb.goodsShop}');" value="商品打分" />
									<input type="button" onclick="openSupplierDiv('${pb.goodsShop}');" value="店铺打分" /> --%>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
							<input type="button" onclick="getDetailsChangeInfo('${pb.orderNo}','${pb.goodsid}');" value="显示采购替换信息" />
						</td>
						<td width="12%">
							<div style="overflow-y: scroll; height: 250px;">
								<div class="w-font">
									<font style="font-size: 15px;" id="rk_remark_${pb.orderNo}${pb.od_id}">${pb.goods_info}</font>
								</div>
							</div>
							<div class="w-margin-top">
								<input type="button" value="备注或回复" onclick="doReplay1('${pb.orderNo}','${pb.goodsid}','${pb.od_id}');" class="repalyBtn" />
								<input type="button" id="${pb.od_id}" stype="display:none" value="发起退货" onclick="returnOr('${pb.orderNo}','${pb.shipno}');" class="repalyBtn" />
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
					&nbsp;&nbsp;&nbsp; 当前第&nbsp;&nbsp;&nbsp;
					<a href="#">${pagenum}</a>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp; <span><input type="hidden" id="refresh" value="${pagenum}"></span>总共&nbsp;&nbsp;&nbsp;<a href="#"><span id="total_page">${totalpage }</span></a>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;总共&nbsp;&nbsp;&nbsp;<a href="#">${totalnum }</a>&nbsp;&nbsp;&nbsp;条商品记录
				</td>
				<td class="caitd02">
					<a href="javascript:void(0);" onclick="submit('1')">首页</a>&nbsp;&nbsp;&nbsp; <a href="javascript:void(0);" onclick="submit('${pagenum-1 }')">上一页</a>&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="submit('${pagenum+1 }')">下一页</a>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="submitt()">点击此处</a>
					&nbsp;&nbsp;跳转至&nbsp;&nbsp;
					<input style="width: 30px;" name="num" id="nummm" value="${num }" type="text" align="middle" />
					&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="submit('${totalpage }')">尾页</a></td>
			</tr>
		</table>
		<div id="divcss1">
			<table border="1">
				<tr>
					<td>淘宝运单号
					<td>
					<td>状态
					<td>
					<td>下单时间
					<td>
					<td>是否跟进
					<td>
				</tr>
				<tr>
					<td id="logistics_id">淘宝运单号
					<td>
					<td id="tb_state">状态
					<td>
					<td id="createtime">下单时间
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
		<a href="javascript:void(0)" class="show_x" onclick="Fncloserecord()">╳</a>
	</div>
	<center>
		<h3 class="show_h3">商品补货记录</h3>
		<table id="replenishment" class="imagetable">
			<thead>
			<tr>
				<td width='13%'>补货数量</td>
				<td width='13%'>补货价格</td>
				<td width='18%'>补货时间</td>
				<td width='10%'>补货人</td>
				<td width='18%'>补货链接</td>
				<td width=''>补货原因</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
<div class="mod_pay4" style="display: none;" id="displayChangeLog">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="displayChangeLogInfo()">╳</a>
	</div>
	<center>
		<h3 class="show_h3">商品替换记录</h3>
		<table id="displayChangeLogs" class="imagetable">
			<thead>
			<tr>
				<td width='11%'>原始销售价格($)</td>
				<td width='11%'>原始货源链接</td>
				<td width='11%'>替换后销售价格(￥)</td>
				<td width='11%'>替换后货源链接</td>
				<td width='11%'>替换时间</td>
				<td width='11%'>替换负责人</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
<div class="mod_pay3" style="display: none;" id="displayBuyInfo">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseBuyInfo()">╳</a>
	</div>
	<center>
		<h3 class="show_h3">商品采样记录</h3>
		<table id="displayBuyInfos" class="imagetable">
			<thead>
			<tr>
				<td width='11%'>采样链接</td>
				<td width='11%'>采样价格</td>
				<td width='11%'>采样数量</td>
				<td width='11%'>采样人</td>
				<td width='11%'>采样时间</td>
				<td width='11%'>采样工厂</td>
				<td width='11%'>工厂状态</td>
				<td width='12%'>采样反馈</td>
				<td width='11%'>添加反馈</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
<div class="mod_pay3" style="display: none;" id="supplierDiv">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierDiv()">╳</a>
	</div>
	<center>
		<h3 class="show_h3">采购供应商打分</h3>
		<table id="supplierDivInfos" class="imagetable">
			<thead>
			<tr>
				<td rowspan="5"><span style="color:red">[质量]</span>:1:差   2:较差  3: 一般  4:无投诉  5: 优质</td>
			</tr>
			<%--<tr>--%>
			<%--<td rowspan="5"><span style="color:red">[服务]</span>:1:发货不及时  2:发货及时但是乱发  3:处理问题回复不及时  4:售后处理配合度一般  5:售后处理配合度好</td>--%>
			<%--</tr>--%>
			<tr>
				<td>店铺</td><td>质量</td><td>是否有库存协议</td><td>支持退货天数</td>
			</tr>
			<tr>
				<td>
					<span id="su_shop_id"></span>
					<input type="hidden" id="hidden_shopId">
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
					<input type="text" id="su_data" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
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
<div class="mod_pay3" style="display: none;" id="supplierGoodsDiv">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierGoodsDiv()">╳</a>
	</div>
	<center>
		<h3 class="show_h3">采样商品打分</h3>
		<table id="supplierGoodsDivInfos" class="imagetable">
			<thead>
			<tr>
				<td rowspan="5"><span style="color:red">[质量]</span>:1:差   2:较差  3: 一般  4:无投诉  5: 优质</td>
			</tr>
			<%--<tr>--%>
			<%--<td rowspan="5"><span style="color:red">[服务]</span>:1:发货不及时  2:发货及时但是乱发  3:处理问题回复不及时  4:售后处理配合度一般  5:售后处理配合度好</td>--%>
			<%--</tr>--%>
			<tr>
				<td>产品</td><td>质量</td><td>备注</td>
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
				<%--<td>--%>
				<%--<select id="g_service">--%>
				<%--<option value="0">---请选择---</option>--%>
				<%--<option value="1">1分</option>--%>
				<%--<option value="2">2分</option>--%>
				<%--<option value="3">3分</option>--%>
				<%--<option value="4">4分</option>--%>
				<%--<option value="5">5分</option>--%>
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
<div class="mod_pay3" style="display: none;" id="offlinepur_chase">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="Fncloserchase()">╳</a>
	</div>
	<center>
		<h3 class="show_h3">线下采购记录</h3>
		<table id="offlinepurchase" class="imagetable"
			   style="word-break: break-word;">
			<thead>
			<tr>
				<td width='10%'>采购运单号</td>
				<td width='10%'>采购时间</td>
				<td width='10%'>采购人</td>
				<td width='50%'>采购链接</td>
				<td width='10%'>淘宝订单号</td>
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
        var txt = (document.getElementById(order).innerHTML).indexOf("查看更多");
        if (txt == 0) {
            document.getElementById("ord_i_" + order).innerHTML = $(
                "#ord_" + order).val();
            document.getElementById(order).innerHTML = "<<收起";
        } else {
            document.getElementById("ord_i_" + order).innerHTML = $(
                "#ord2_" + order).val();
            document.getElementById(order).innerHTML = "查看更多>>";
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
                    alert("无法查看");
                } else {
                    window.open(url, "被替换产品")
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

<!-- 采购页面跳转使用 -->
<%
	request.getSession().removeAttribute("cgtz");
%>