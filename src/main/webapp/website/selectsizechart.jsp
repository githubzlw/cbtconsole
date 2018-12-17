<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
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
<script type="text/javascript"
    src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>替换尺码表</title>
<style>
*{padding:0;margin:0;list-style:none;}
.shopid_box{width:100%;overflow:hidden;margin-bottom:20px;}
.ul_list{width:100%;overflow:hidden;}
.li_list{float:left;width:40%;margin-right:10px;margin-bottom:10px;position:relative;overflow: hidden;height: 305px;border:1px solid #ccc;}   
.li_mask{width:100%;height:100%;position:absolute;left:0;right:0;bottom:0;top:0;margin:auto;z-index:10;background:rgba(0,0,0,0.6);color:greenyellow;font-size:16px;text-align:center}
.li_list .imgs{width:100%;height:270px;overflow:hidden;}
.li_list img{display:block;max-width:100%;max-height:100%;width:auto;height:auto;}
.li_list span{display:block;width:90%;overflow:hidden;font-size:14px;line-height:20px;color:#666;white-space:nowrap;text-overflow:ellipsis;
    position: absolute;bottom: 3px;left: 10px;}
    
    

.shopid{float:left;height:34px;line-height:34px;}
.shopid_p{float:left;margin-left:15px;height:34px;line-height:34px;}
.submitButton{display:inline-block;height:34px;line-height:34px;padding:0 20px;cursor:pointer;background:pink;border-radius:4px;font-size:14px;}
.submitButton:hover{background:blue;color:#fff;}
.submitButton:active{background:#999;color:red}
.shipid_txt{display:inline-block;margin-left:15px;line-height:34px;font-size:14px;}
.li_list input{}

.singleButton{display:block;overflow:hidden;font-size:14px;line-height:20px;color:#666;white-space:nowrap;text-overflow:ellipsis;
    position: absolute;bottom: 3px;left: 10px;display:inline-block;height:34px;line-height:34px;padding:0 20px;cursor:pointer;background:pink;border-radius:4px;font-size:14px;}
.singleButton:hover{background:blue;color:#fff;}
.singleButton:active{background:#999;color:red}
.sssd{width:40%;}
</style>
</head>
<body>
<div class="shopid_box">
<h2 class="shopid">一级类别:</h2>
<select class="catoption_lv1" value="">
<option class="311">童装</option>
<option class="312">内衣</option>
<option class="127380009">运动服饰</option>
<option class="10166">女装</option>
<option class="10165">男装</option>
</select>
<select class="catoption_minlv">
</select>
<p class="shopid_p">
<span class="submitButton" >一键替换成已上传的尺码表</span>
</p>
</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$(".catoption_lv1").change(function(){
		var opt = $(".catoption_lv1").val();
		if(opt=='童装'){loadCategoryName('311');}
		if(opt=='内衣'){loadCategoryName('312');}
		if(opt=='运动服饰'){loadCategoryName('127380009');}
		if(opt=='女装'){loadCategoryName('10166');}
		if(opt=='男装'){loadCategoryName('10165');}
	});
});

function loadCategoryName(catid){
	if(catid=='311'){$(".catoption_lv1").val("童装");}
	if(catid=='312'){$(".catoption_lv1").val("内衣");}
	if(catid=='127380009'){$(".catoption_lv1").val("运动服饰");}
	if(catid=='10166'){$(".catoption_lv1").val("女装");}
	if(catid=='10165'){$(".catoption_lv1").val("男装");}
	$.ajax({
        type:"post", 
        url:"/cbtconsole/order/loadCategoryName.do",
        data:{"catid":catid},
        dataType:"text",
        async:true,
        success : function(data){
        	data = JSON.parse(data);
            for(var i=0;i<data.length;i++){
            	var catName = data[i].name;
            	var catid = data[i].catid;
            	var optionstr = "<option class='"+catid+"'>"+catName+"</option>";
            	$(".catoption_minlv").append(optionstr);
            }
        }
    });
}

//加载 尺码表
function getSizeChart(catid){
	$.ajax({
        type:"post", 
        url:"/cbtconsole/order/getSizeChart.do",
        data:{},
        dataType:"text",
        async:true,
        success : function(data){
        	var dataJson = JSON.parse(data);
        	var shopHtml = "";
            if(dataJson.length>0){
            	var shopHtml = '<ul class="ul_list">';
            	for(var i=0;i<dataJson.length;i++){
            		var catimg = dataJson[i];
            		shopHtml = shopHtml + '<li class="li_list">';
                    shopHtml = shopHtml + '<div class="imgs"><img src="'+catimg.remotpath+'"></div>';
                    shopHtml = shopHtml + '<span calss="goods_name">产品id：<em>'+catimg.pid+'</em></span>';
                    shopHtml = shopHtml + '</li>';
                    shopHtml = shopHtml + '<li class="li_list"><input type="file"/>';
                    shopHtml = shopHtml + '<div class="imgs"><img src=""></div>';
                    shopHtml = shopHtml + '</li>';
            	}
            	shopHtml = shopHtml + '</ul>';
            }
            $(".shopid_box").append(shopHtml);
        }
    });
}
</script>
</html>