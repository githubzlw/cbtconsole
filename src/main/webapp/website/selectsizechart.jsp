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
</body>
<script type="text/javascript">
$(document).ready(function(){
	getComfirmedSourceGoods();
});

//加载 已经经过货源确认的商品
function getComfirmedSourceGoods(){
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
            	var shopHtml = '<div class="shopid_box"><h2 class="shopid">类别名称：'+dataJson[0].name+'</h2>';
            	shopHtml = shopHtml +'<p class="shopid_p"><span class="submitButton" >一键替换成已上传的尺码表</span></p><ul class="ul_list">';
            	for(var i=0;i<dataJson.length;i++){
            		var catimg = dataJson[i];
            		shopHtml = shopHtml + '<li class="li_list">';
                    shopHtml = shopHtml + '<div class="imgs"><img src="'+catimg.remotpath+'"></div>';
                    shopHtml = shopHtml + '<span calss="goods_name">产品id：<em>'+catimg.pid+'</em></span>';
                    shopHtml = shopHtml + '</li>';
                    
                    shopHtml = shopHtml + '<li class="li_list"><input type="file"/>';
                    shopHtml = shopHtml + '<div class="imgs"><img src=""></div>';
                    shopHtml = shopHtml + '<p class="shopid_p"><span class="singleButton sssd" >替换成此尺码表</span></p>';
                    shopHtml = shopHtml + '</li>';
            	}
            	shopHtml = shopHtml + '</div>';
            }
            $(document.body).html(shopHtml);
        }
    });
}
</script>
</html>