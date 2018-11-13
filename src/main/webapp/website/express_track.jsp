<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>快递跟踪号查询页面</title>
<style type="text/css">
.statusbtn{width:120px;height:50px;float:left;outline: none;}
.goodinfo{color: red}
</style>
<script type="text/javascript">
var para=new Array();

$(function(){
	$("#search").focus();
	$("#search").focus(function(){
		$("#search").val('');
	});
	$("#search").bind("input propertychange",function(){
		document.onkeydown=function(event){
			var e=event||window.event;
			if(e&&e.keyCode==13){
				search();
			}
		}
	});
})

function caiji(){
	$.post("/cbtconsole/WebsiteServlet?action=runExeServlet&className=ExpressTrackServlet");
}

function submit(){
	var flag;
	for(var i=0;i<para.length;i++){
		var orderid=para[i].split(',')[0];
		var expresstrackid=para[i].split(',')[1];
		var goodsid=para[i].split(',')[2];
		var status=para[i].split(',')[3];
		if(status==0){
			//status=0;
			alert("有商品未选择状态！");
			$("#td"+goodsid).css("background-color","grey");
			return;
		}else{
			$("#td"+goodsid).css("background-color","");
		}
		$.post("/cbtconsole/WebsiteServlet?action=insertArrivalStatusServlet&className=ExpressTrackServlet",
				{expresstrackid:expresstrackid,orderid:orderid,goodsid:goodsid,status:status},
				function(res){
					if(res>0){
						  
					}
			});
			//res=updateGoodsStatus(para[i].split(',')[0],para[i].split(',')[1],para[i].split(',')[2],para[i].split(',')[3]);
	}

	search();
}

//更新商品的状态
function getStatus(orderid,trackid,goodsid,status){
	for(var i=0;i<para.length;i++){
		if(goodsid==para[i].split(',')[2]){
			para[i]=orderid+','+trackid+','+goodsid+','+status;
		}
	}
	//para.push(orderid+","+trackid+","+goodsid+","+status);
	
	if(status==1){
		$("#arrived"+goodsid).css("background-color","green");
		$("#notarrived"+goodsid).css("background-color","");
		$("#damaged"+goodsid).css("background-color","");
		$("#questioned"+goodsid).css("background-color","");
		$("#notenough"+goodsid).css("background-color","");
		document.getElementById("goodcount"+goodsid).style.visibility="hidden";
	}else if(status==2){
		$("#arrived"+goodsid).css("background-color","");
		$("#notarrived"+goodsid).css("background-color","green");
		$("#damaged"+goodsid).css("background-color","");
		$("#questioned"+goodsid).css("background-color","");
		$("#notenough"+goodsid).css("background-color","");
		document.getElementById("goodcount"+goodsid).style.visibility="hidden";
	}else if(status==3){
		$("#arrived"+goodsid).css("background-color","");
		$("#notarrived"+goodsid).css("background-color","");
		$("#damaged"+goodsid).css("background-color","green");
		$("#questioned"+goodsid).css("background-color","");
		$("#notenough"+goodsid).css("background-color","");
		document.getElementById("goodcount"+goodsid).style.visibility="hidden";
	}else if(status==4){
		$("#arrived"+goodsid).css("background-color","");
		$("#notarrived"+goodsid).css("background-color","");
		$("#damaged"+goodsid).css("background-color","");
		$("#questioned"+goodsid).css("background-color","green");
		$("#notenough"+goodsid).css("background-color","");
		document.getElementById("goodcount"+goodsid).style.visibility="hidden";
	}else if(status==5){
		$("#arrived"+goodsid).css("background-color","");
		$("#notarrived"+goodsid).css("background-color","");
		$("#damaged"+goodsid).css("background-color","");
		$("#questioned"+goodsid).css("background-color","");
		$("#notenough"+goodsid).css("background-color","green");
		//$("#goodcount"+goodsid).hide();
		document.getElementById("goodcount"+goodsid).style.visibility="visible";
	}
}
//更新数量不够的情况下到货数量
function updateArrvieCount(orderid,trackid,goodsid){
	var goodsarrivecount=$("#countvalue"+goodsid).val();
	if($("#countvalue"+goodsid).val()==''){
		alert("不能为空！");
	}else{
		$.post("/cbtconsole/WebsiteServlet?action=updateArriveCountServlet&className=ExpressTrackServlet",
				{expresstrackid:trackid,orderid:orderid,goodsid:goodsid,goodsarrivecount:goodsarrivecount},
				function(res){
					if(res>0){
						  alert('录入成功！');
						  $("#countvalue"+goodsid).attr("disabled","disabled");
					}
			});
	}
	
}
// function updateGoodsStatus(orderid,expresstrackid,goodsid,status){
// 	if((status+'')=='undefined'){
// 		status=0;
// 	}
// 	$.post("/cbtconsole/WebsiteServlet?action=insertArrivalStatusServlet&className=ExpressTrackServlet",
// 			{expresstrackid:expresstrackid,orderid:orderid,goodsid:goodsid,status:status},
// 			function(res){
// 				if(res>0){
				
// 				}
// 		});
// }
function updateWarehouseId(orderid,ordercount,inwarehousecount){
	var warehouseid=$("#warehousetext_"+orderid).val();
	$.post("/cbtconsole/WebsiteServlet?action=updateWarehouseIdServlet&className=ExpressTrackServlet",
			{orderid:orderid,ordercount:ordercount,inwarehousecount:inwarehousecount,warehouseid:warehouseid},
			function(res){
				if(res>0){
					alert("入库成功！");
				}
			});
	search();
}
function search(){
	var expresstrackid=$("#search").val();
	$.post("/cbtconsole/WebsiteServlet?action=searchByExpressTrackid&className=ExpressTrackServlet",
			{expresstrackid:expresstrackid},
			function(res){
				$("#infodiv").html('');
				var json=eval(res);
				for(var i=0;i<json.length;i++){
					var str='';
					var urlsize=json[i].goodsImgurl.length;//图片链接数
					var size=4;
					var width;
					if(urlsize>size){
						width=size*600;
					}else{
						width=urlsize*600;
					}
					str+='<table border="1" style="width:'+width+'px;height:'+(600*Math.ceil(urlsize/size)+100)+'px;margin-top:20px">';
					str+='<tr><td style="width:600px;height:100px">订单号:'+json[i].expressTrackOrderid;
					str+='<br><br>到货数量：'+json[i].warehouseInfo.InWarehouseCount+'&nbsp;&nbsp;订单商品总数：'+json[i].warehouseInfo.OrderCount;
					str+='<br><br>库位号：<input id="warehousetext_'+json[i].expressTrackOrderid+'" type="text" disabled="disabled">';
					str+='<input id="warehousebtn_'+json[i].expressTrackOrderid+'" type="button" value="入库" disabled="disabled" style="margin-left:20px" onclick="updateWarehouseId(\''+json[i].expressTrackOrderid+'\',\''+json[i].warehouseInfo.OrderCount+'\',\''+json[i].warehouseInfo.InWarehouseCount+'\')">';
					str+='</td></tr>';
					//'<input type="button" value="提交货物状态" onclick="updateGoodsStatus(\''+json[i].expressTrackOrderid+'\',\''+json[i].expressTrackId+'\',\''+json[i].goodsId+'\')" style="margin-left:10px">'
					var imgurlArray=new Array();
					var goodsidArray = new Array();
					var arrivalstatusArray=new Array();
					var goodsnameArray = new Array();
					var priceArray = new Array();
					var typeArray=new Array();
					var quantityArray = new Array();
					if((json[i].goodsImgurl+'').indexOf(',')>-1){
						imgurlArray=(json[i].goodsImgurl+'').split(',');
						goodsidArray=(json[i].goodsId+'').split(',');
						arrivalstatusArray=(json[i].arrivalstatus+'').split(',');
						goodsnameArray=(json[i].goodsName+'').split(',');
						priceArray=(json[i].goodsPrice+'').split(',');
						typeArray=(json[i].goodsType+'').split(',');
						quantityArray=(json[i].goodsQuantity+'').split(',');
					}else{
						imgurlArray[0]=json[i].goodsImgurl;
						goodsidArray[0]=json[i].goodsId;
						arrivalstatusArray[0]=json[i].arrivalstatus;
						goodsnameArray[0]=json[i].goodsName;
						priceArray=json[i].goodsPrice;
						typeArray=json[i].goodsType;
						quantityArray=json[i].goodsQuantity;
					}
					for(var j=0,n=0;j<Math.ceil(urlsize/size);j++){
						str+="<tr>";
						for(var k=0;k<size&&n<urlsize;k++,n++){
							str+='<td id="td'+goodsidArray[n]+'">';
							str+='<div style="width:600px;height:80px;margin-left:20px;margin-top:20px;	word-wrap:break-word;word-break:break-all">'+goodsnameArray[n]+'</div>';
							str+='<div style="margin-top:30px;margin-left:20px;">';
							str+='<div style="width:400px;height:400px;"><img width="400px" height="400px" src="'+imgurlArray[n]+'"></div>';
							if((typeArray[n]+'')=='undefined'){
								str+='<div style="margin-top:-400px;margin-left:410px"><div style="padding:5px">price:<span class="goodinfo">'+priceArray[n]+'</span></div><div style="padding:5px">type:<span class="goodinfo"></span></div><div style="padding:5px">quantity:<span class="goodinfo">'+quantityArray[n]+'</span></div></div>';
							}else{
								str+='<div style="margin-top:-400px;margin-left:410px"><div style="padding:5px">price:<span class="goodinfo">'+priceArray[n]+'</span></div><div style="padding:5px">type:<span class="goodinfo">'+typeArray[n].replace('@',',')+'</span></div><div style="padding:5px">quantity:<span class="goodinfo">'+quantityArray[n]+'</span></div></div>';
							}
							str+='<div id="goodcount'+goodsidArray[n]+'" style="visibility:hidden;margin-left:430px;margin-top:290px;width:200px"><span style="font-size:12px">到货数量：</span><input id="countvalue'+goodsidArray[n]+'" type="text" style="width:70px"><input type="button" value="确认" onclick="updateArrvieCount(\''+json[i].expressTrackOrderid+'\',\''+json[i].expressTrackId+'\',\''+goodsidArray[n]+'\')"></div>';
							str+='</div>';
// 							str+='<div style="margin-top:30px;margin-left:20px"><input type="radio" name="status'+goodsidArray[k+size*j]+'" value=1>到货<input type="radio" name="status'+goodsidArray[k+size*j]+'" value=2>该到没到';
// 							str+='<input type="radio" name="status'+goodsidArray[k+size*j]+'" value=3>破损<input type="radio" name="status'+goodsidArray[k+size*j]+'" value=4>有疑问<input type="radio" name="status'+goodsidArray[k+size*j]+'" value=5>数量不够</div>';
 							str+='<div style="width:600px;height:100px;margin-top:20px;margin-left:20px">';
							str+='<input class="statusbtn" type="button" id="arrived'+goodsidArray[n]+'" value="到货" onclick="getStatus(\''+json[i].expressTrackOrderid+'\',\''+json[i].expressTrackId+'\',\''+goodsidArray[n]+'\',1)">';
							str+='<input class="statusbtn" type="button" id="notarrived'+goodsidArray[n]+'" value="该到没到" onclick="getStatus(\''+json[i].expressTrackOrderid+'\',\''+json[i].expressTrackId+'\',\''+goodsidArray[n]+'\',2)">';
							str+='<input class="statusbtn" type="button" id="damaged'+goodsidArray[n]+'" value="破损" onclick="getStatus(\''+json[i].expressTrackOrderid+'\',\''+json[i].expressTrackId+'\',\''+goodsidArray[n]+'\',3)">';
							str+='<input class="statusbtn" type="button" id="questioned'+goodsidArray[n]+'" value="有疑问" onclick="getStatus(\''+json[i].expressTrackOrderid+'\',\''+json[i].expressTrackId+'\',\''+goodsidArray[n]+'\',4)">';
							str+='<input class="statusbtn" type="button" id="notenough'+goodsidArray[n]+'" value="数量不够" onclick="getStatus(\''+json[i].expressTrackOrderid+'\',\''+json[i].expressTrackId+'\',\''+goodsidArray[n]+'\',5)">';
							str+='</div>';
							str+='</td>';
							para.push(json[i].expressTrackOrderid+","+json[i].expressTrackId+","+goodsidArray[n]+","+arrivalstatusArray[n]);
						}
						str+="</tr>";
						
					} 
					str+='</table>';
					
					$("#infodiv").append(str);
					for(var m=0;m<arrivalstatusArray.length;m++){
						if(arrivalstatusArray[m]==1){
							$("#arrived"+goodsidArray[m]).css("background-color","green");
						}else if(arrivalstatusArray[m]==2){
							$("#notarrived"+goodsidArray[m]).css("background-color","green");
						}else if(arrivalstatusArray[m]==3){
							$("#damaged"+goodsidArray[m]).css("background-color","green");
						}else if(arrivalstatusArray[m]==4){
							$("#questioned"+goodsidArray[m]).css("background-color","green");
						}else if(arrivalstatusArray[m]==5){
							$("#notenough"+goodsidArray[m]).css("background-color","green");
						}
					}
					
					if(json[i].warehouseInfo.InWarehouseCount>0){
						if(json[i].warehouseInfo.WarehouseId!=null){
							$("#warehousetext_"+json[i].expressTrackOrderid).val(json[i].warehouseInfo.WarehouseId);
							$("#warehousebtn_"+json[i].expressTrackOrderid).attr("disabled","disabled");
							$("#warehousetext_"+json[i].expressTrackOrderid).attr("disabled","disabled");
						}else{
							$("#warehousebtn_"+json[i].expressTrackOrderid).removeAttr("disabled");
							$("#warehousetext_"+json[i].expressTrackOrderid).removeAttr("disabled");
						}
						
					}
						
				}
			});
	$("#search").blur();
}
</script>
</head>
<body>
请输入快递跟踪号：<input type="text" id="search">
<input type="button" id="caijibtn" value="采集" onclick="caiji()">
<input style="position: fixed;top:400px;right:0px;width:200px;height:40px"type="button" id="submitbtn" value="提交货物状态" onclick="submit()">
<div id="infodiv">
</div>
<!-- <table border="1"> -->
<!-- 	<tr><td>type</td></tr> -->
<%-- 	<c:forEach items="${res}" var="list" varStatus="res"> --%>
<!-- 	<tr>		   -->
<%-- 		<td>${list.type}</td>	 --%>
<!-- 	</tr> -->
<%-- 	</c:forEach> --%>
<!-- </table>	 -->

</body>
</html>