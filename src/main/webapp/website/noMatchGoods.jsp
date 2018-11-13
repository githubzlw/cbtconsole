<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="/cbtconsole/js/newtrack.js"></script>
<title>验货照片补拍</title>
<script type="text/javascript">

   $(function(){
	   var div1 = document.getElementById("paizhao");
	 div1.onmousedown = function(ev){
	var oevent = ev || event;

	 var distanceX = oevent.clientX - div1.offsetLeft;
	 var distanceY = oevent.clientY - div1.offsetTop;

	 document.onmousemove = function(ev){
	var oevent = ev || event;
	div1.style.left = oevent.clientX - distanceX + 'px';
	div1.style.top = oevent.clientY - distanceY + 'px'; 
	};document.onmouseup = function(){
		document.onmousemove = null;document.onmouseup = null;
	};
	}
   });
   
   function update(isok,itemid,sku,itemids,tborderid,state){
	   var orderid=document.getElementById("order_"+itemid).value;
	   var goodsid=document.getElementById("goodsid_"+itemid).value;
	   $.post("/cbtconsole/WebsiteServlet?action=updateMatch&className=ExpressTrackServlet",
				{
					orderid : orderid,
					goodsid : goodsid,
					sku : sku,
					itemids : itemids,
					state :state,
					tborderid : tborderid
				},
				function(res) {
					if (res==1) {
						$(isok).css("background","green");
						document.getElementById("span_" + itemid).innerHTML = "不能出货";
					} else if(res==999){
						$(isok).css("background","green");
						document.getElementById("span_" + itemid).innerHTML = "可以出货";
					}else{
						$(isok).css("background","red");
						document.getElementById("span_" + itemid).innerHTML = "关联入库失败";
					}
				});
   }
   
   function submitt(){
	   var val=$("#nummm").val();
	   if(val === "" || val ==null){
	        return false;
	    }
	    if(!isNaN(val) && Number(val)>0){
	    	 init(val);
	    }else{
	        return false;
	    }
   }

  function init(page){
	  var orderid=$("#orderid").val();
	  var goodsid=$("#goodsid").val();
	 $.post("/cbtconsole/WebsiteServlet?action=getNoMatchGoods&className=ExpressTrackServlet",{page :page,orderid:orderid,goodsid:goodsid},
			function(res){
		     var allJsonObj =JSON.parse(res);
			  $("#taobaoInfos").html("");
			  var str = "";
			  for(var j=0;j<allJsonObj.length;j++){
				  var jsonObj=allJsonObj[j];
					str += '<table border="1" width="310px;" style="float:left;margin-left:2px";"><tr>';
					str += '<td width="220px"><br><div style="height:50px">商品名称：'
							+ jsonObj.goodsid
							+ '</div></br><div>';
					str += '<div style="float:left;"><a href="'+jsonObj.car_url+'" target="_blank">';
					str += '<img style="width:60px;height:60px;" src="'+jsonObj.car_img+'"/></a></div>';
						
					str += '<div style="float:right;width:280px;height: 100px">';
					
					str += '<p style = "font-size:12px;">订单号：'
						+ jsonObj.orderid
						+ '</p>';
					str += '<p>商品号：'
						+ jsonObj.goodsid
						+ '</p>';
				    str+='<input type="button" id="bt_'+jsonObj.orderid+'_'+jsonObj.goodsid+'" value="重新拍照" onclick="btnCap(\''+jsonObj.orderid+'\',\''+jsonObj.goodsid+'\');"/>'
					str += '</div>';
					str += '<div style="clear:both;"></div>';
					str += '</div></td></tr></table>';
			  }
			  $("#taobaoInfos").html(str);
			  var _str='';
			  if(allJsonObj.length>0){
				  _str+='<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td class="caitd01">&nbsp;&nbsp;&nbsp; 当前第&nbsp;&nbsp;&nbsp;';
				  _str+='<a href="#">'+allJsonObj[0].buy_for_me+'</a>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp; <span><input type="hidden" id="refresh" value="'+allJsonObj[0].buy_for_me+'"></span>总共&nbsp;&nbsp;&nbsp;<a href="#"><span id="total_page">'+allJsonObj[0].checked+'</span></a>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;总共&nbsp;&nbsp;&nbsp;<a href="#">'+allJsonObj[0].buycount+'</a>&nbsp;&nbsp;&nbsp;条商品记录';
				  _str+='</td><td class="caitd02">';
				  _str+='<a href="javascript:void(0);" onclick="init(1)">首页</a>&nbsp;&nbsp;&nbsp; <a href="javascript:void(0);" onclick="submit('+(allJsonObj[0].buy_for_me-1)+')">上一页</a>&nbsp;&nbsp;&nbsp;';
				  _str+='<a href="javascript:void(0);" onclick="init('+(allJsonObj[0].buy_for_me+1)+')">下一页</a>&nbsp;&nbsp;&nbsp;';
				  _str+='<a href="javascript:void(0);" onclick="submitt()">点击此处</a>&nbsp;&nbsp;跳转至&nbsp;&nbsp;';
				  _str+='<input style="width: 30px;" name="num" id="nummm" value="" type="text" align="middle" /> &nbsp;&nbsp;页&nbsp;&nbsp;&nbsp; ';
				  _str+='<a href="javascript:void(0);" onclick="init('+allJsonObj[0].checked+')">尾页</a></td>';
				  _str+='</tr></table>';
			  }
			  $("#cai_page").html(_str);
			}
		);
  }
  
  function reset(){
	  $("#orderid").val("");
	  $("#goodsid").val("");
  }
</script>
<style>
	div,video{margin:0;padding:0;}
	#paizhao{position:absolute;width:400px;height:400px;right:0;top:0;z-index:99;cursor:move}
</style>
</head>
<body onload="init(1);">
    <div id="paizhao">
    <video id="video" width="400" height="400" autoplay></video><br>
    <canvas id="canvas" width="400"  height="400"></canvas>
 </div>
 <div style="margin-left:500px;">
  订单号:<input type="text" id="orderid"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  商品号:<input type="text" id="goodsid"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="查询" onclick="init(1)"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="重置" onclick="reset()"/>
 </div>
    <div id="taobaoInfos">
    </div>
    <div id="cai_page">
    </div>
</body>
</html>