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
<title>订单确认和生成页面</title>
<style>
*{padding:0;margin:0;list-style:none;}
.shopid_box{width:100%;overflow:hidden;margin-bottom:20px;}
.ul_list{width:100%;overflow:hidden;}
.li_list{float:left;width:220px;margin-right:2%;margin-bottom:10px;position:relative;}
.li_mask{width:100%;height:100%;position:absolute;left:0;right:0;bottom:0;top:0;margin:auto;z-index:10;background:rgba(0,0,0,0.6);color:greenyellow;font-size:16px;text-align:center}
.li_list img{display:block;height:220px;}
.li_list span{display:block;width:90%;overflow:hidden;font-size:14px;line-height:20px;color:#666;white-space:nowrap;text-overflow:ellipsis;}
.shopid{float:left;height:34px;line-height:34px;}
.shopid_p{float:left;margin-left:15px;height:34px;line-height:34px;}
.submitButton{display:inline-block;height:34px;line-height:34px;padding:0 20px;cursor:pointer;background:pink;border-radius:4px;font-size:14px;}
.submitButton:hover{background:blue;color:#fff;}
.submitButton:active{background:#999;color:red}
.shipid_txt{display:inline-block;margin-left:15px;line-height:34px;font-size:14px;}
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
        url:"/cbtconsole/order/getComfirmedSourceGoods.do",
        data:{},
        dataType:"text",
        async:true,
        success : function(data){
        	var dataJson = JSON.parse(data);
        	var beanList = dataJson.beanList;
        	var shopidList = dataJson.shopidList;
        	var bodyHtml = "";
            if(shopidList.length>0){
            	for(var i=0;i<shopidList.length;i++){
            		var shopid = shopidList[i];
            		var shopHtml = '<div class="shopid_box"><h2 class="shopid">店铺名称：'+shopid+'</h2>';
            		shopHtml = shopHtml +'<p class="shopid_p"><span class="submitButton" onclick="generate1688Orders(this,\''+shopid+'\')">一键生成1688 '+shopid+' 店铺订单</span></p><ul class="ul_list">';
            		for(var j=0;j<beanList.length;j++){
            			var goodsBean = beanList[j];
            			if(goodsBean.shopid==shopid){
            				if(goodsBean.sampling_flag==2){
            					shopHtml = shopHtml + '<li class="li_list"><div class="li_mask">已下单，可支付</div><input type="hidden" value="'+goodsBean.id+'">';
            				}else{
            					shopHtml = shopHtml + '<li class="li_list"><input type="hidden" value="'+goodsBean.id+'">';
            				}
            				
            				shopHtml = shopHtml + '<img src="'+goodsBean.car_img.replace(".60x60.",".400x400.")+'">';
            				shopHtml = shopHtml + '<span calss="goods_name" title="'+goodsBean.goodsname+'">产品名称：<em>'+goodsBean.goodsname+'</em></span>';
            				var strArr = goodsBean.car_type.split(",");
            				var types = "";
            				for(var k=0;k<strArr.length;k++){
            					if(k==0){
            						types = strArr[k].split("@")[0];
            					}else{
            						types = types + "," + strArr[k].split("@")[0];
            					}
            				} 
            				shopHtml = shopHtml + '<span calss="goods_type" title="'+types+'">规格：<em>'+types+'</em></span>';
            				shopHtml = shopHtml + '<span calss="goods_price">价格：<em>'+goodsBean.goodsprice+'</em></span>';
            				shopHtml = shopHtml + '<span calss="goods_pid">产品id:<em>'+goodsBean.pid+'</em></span>';
            				shopHtml = shopHtml + '<span calss="goods_number">购买数量：<em>'+goodsBean.number+'</em></span>';
            				shopHtml = shopHtml + '<span calss="goods_number" title="'+goodsBean.orderid+'">所属订单：<em>'+goodsBean.orderid+'</em></span>';
            				shopHtml = shopHtml + '<span calss="goods_number">货源确认人：<em>'+goodsBean.adminid+'</em></span>';
            				shopHtml = shopHtml + '</li>';
            			}
            		}
            		shopHtml = shopHtml + '</ul></div>';
            		bodyHtml = bodyHtml + shopHtml;
            	}
            }
            $(document.body).html(bodyHtml+'<span class="submitButton" onclick="generate1688Orders_all(this)">一键生成1688所有店铺订单</span>');
        }
    });
}

function generate1688Orders(obj,shopid){
	$.ajax({
        type:"post", 
        url:"/cbtconsole/order/generate1688Orders.do",
        data:{"shopid":shopid},
        dataType:"text",
        async:true,
        success : function(data){
            var dataJson = JSON.parse(data);
            for(var i=0;i<dataJson.length;i++){
            	$(obj).after('<span class="shipid_txt">'+dataJson[i].message+'</span>');
            }
        }
    });
}

function generate1688Orders_all(obj){
    $.ajax({
        type:"post", 
        url:"/cbtconsole/order/generate1688Orders.do",
        data:{},
        dataType:"text",
        async:true,
        success : function(data){
            var dataJson = JSON.parse(data);
            for(var i=0;i<dataJson.length;i++){
                $(obj).after("<span style='display:block;' class='tipsContent'>"+dataJson[i].message+"</span>");
            }
        }
    });
}
</script>
</html>