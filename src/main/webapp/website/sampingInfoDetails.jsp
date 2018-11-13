<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>样品采购界面</title>
<style>
.a1 {
	color: #00afff;
}
</style>
<script type="text/javascript">
function checkAll(checkall) {    
    arr = document.getElementsByName('createOrder' );   
    if (checkall.checked == true) {   
        for(i=0;i<arr.length;i++){
            arr[i].checked = true;   
        }  
        }else{  
            for(i=0;i<arr.length;i++){   
                if((arr[i]).checked==false){  
                	console.log("arr[i]=="+arr[i].value);
                    arr[i].checked = true;  
                }else{
                	arr[i].checked = false; 
                	console.log("arr[i]=="+arr[i].value);
                }  
            }  
        }  
}

function createBuyOrder(){
	    $('#create_order').attr("disabled",true); 
	  	obj = document.getElementsByName("createOrder");
	    var check_val = [];
	    var str="";
	    for(k in obj){
	        if(obj[k].checked){
	        	var vals="";
	        	check_val.push("'"+obj[k].value+"'");
	        	$(".css_"+obj[k].value+"").each(function(){
	        		vals+=$(this).val()+'|';
	        	});
	        	str+=obj[k].value+"$"+($("#num_"+obj[k].value+"").val())+"$"+vals+"&";
	        }
	    }
	    if(check_val==null || check_val==""){
	    	alert("请选择需要生成才样订单的商品");
	    }else{
	 		$.ajax({
				type : 'post',
				url : '/cbtconsole/warehouse/createBuyOrderForGoods',
				data : {
					'goods_pids' : check_val.toString(),"sku_val":str
				},
				dataType : 'text',
				success : function(res) {
					if(Number(res)>0){
						location.reload();
						alert("成功生成采样订单，请稍等几分钟等待数据同步");
					}else{
						alert("生成采样订单失败或者商品已经生成过采样订单请选择其他商品");
					}
					  $('#create_order').attr("disabled",false); 
				},
				error : function() {
					alert("生成采样订单失败");
				}
			});
	    }
}

function changeColor(index,id,h_id,type,value){
	$("#"+index+"_"+id+"").css("border","1px solid red").siblings().css('border','1px solid green');
	$("#"+index+""+type+"_"+h_id+"").val(value);
}

function ch_Color(isOk,goods_pid){
	$(isOk).parents('td').find(':checkbox').click();  
	var color=document.getElementById("c_"+goods_pid+"").style.background;
	if(color == "red"){
		$("#c_"+goods_pid+"").css("background","");
	}else{
		$("#c_"+goods_pid+"").css("background","red");
	}
	console.log("===");
}
</script>
</head>
<body style="">
	<div align="left">
		<span onmousemove="$(this).css('color','#ff6e02');"
			onmouseout="$(this).css('color','#7AB63F');"
			onclick="window.location.href=history.go(-1)"
			style="color: #7AB63F; cursor: pointer;"><em
			style="font-size: 19px;">&nbsp;</em></span>
	</div>
	<br>
	<div align="center">
		<input type="button" id="create_order" value="生成采样订单" onclick="createBuyOrder()" />
	</div>
	<div>
		<table id="table1" align="center" border="1px"
			style="font-size: 13px;" width="1200" bordercolor="#8064A2"
			cellpadding="0" cellspacing="0">
			<Tr>
				<th>工厂名</th>
				<th>ali链接</th>
				<th>ali名称</th>
				<th>1688图片</th>
				<th style="width:150px;">1688价格￥</th>
				<th>规格</th>
				<th>规格库存</th>
				<th>是否生成采样商品${gbb.list_type.indexOf('height')>=-1}<input
					type="checkbox" id="checkall" name="checkall"
					onclick="checkAll(checkall)" />全选
				</th>
			</Tr>

			<c:forEach items="${list }" var="gbb" varStatus="i">
				<Tr class="a">
					<td>${gbb.shop_id}</td>
					<td style="width:50px;"><a href="${gbb.goods_url}" title="1688产品链接" target="_blank" >ali链接</a></td>
					<td><a href="${gbb.url_1688}" title="1688产品链接" target="_blank">${gbb.goods_name}</a>
					</td>

					<td><a target='_blank' href="https://www.import-express.com/goodsinfo/a-1${gbb.goods_pid}.html"><img src="${gbb.img_1688}" title="1688图片,点击跳转网站产品单页" width="220px" height="220px"></img></a></td>
					<td>${gbb.price_1688}</td>
					<td>${gbb.type_msg}</td>
					<td style="width:200px">${gbb.sku_inventory}</td>
					<td style="width: 150px;" id="c_${gbb.goods_pid}"><input type="checkbox"
						name="createOrder" id="createOrder" value="${gbb.goods_pid}" />
						<br><input type="hidden" style="width:75px;" value="1" id="num_${gbb.goods_pid}"><div  style="width: 150px;height:220px;" onclick="ch_Color(this,'${gbb.goods_pid}');"></div></td>
				</Tr>
			</c:forEach>

		</table>
		<div align="center">${pager }</div>

	</div>
</body>
</html>