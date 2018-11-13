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
<title>必要、备用、建议采样页面</title>
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
    	var remarks_val="";
	    var pid_list="";
	    var str="";
	    for(k in obj){
	        if(obj[k].checked){
	        	var vals="";
	        	//客户下单的pid
	        	var old_pid=$("#oldPid_"+obj[k].value+"").val();
	        	//需要采样的货源链接
	        	check_val.push("'"+obj[k].value+"'");
                pid_list=pid_list+obj[k].value+",";
                var remarks=$("#remarks_"+obj[k].value+"").html();
                //规格信息
	        	$(".css_"+obj[k].value+"").each(function(){
	        		vals+=$(this).val()+'|';
	        	});
	        	// str+=obj[k].value+"$"+($("#num_"+obj[k].value+"").val())+"$"+vals+"&";
				//客户下单pid$采样pid$规格
				str+=old_pid+"$"+obj[k].value+"$"+vals+"&";
                remarks_val+=obj[k].value+"$"+remarks+"&";
	        }
	    }
	    if(check_val==null || check_val==""){
	    	alert("请选择需要生成才样订单的商品");
	    }else{
	 		$.ajax({
				type : 'post',
				url : '/cbtconsole/warehouse/createBuyOrder',
				data : {
					'goods_pids' : check_val.toString(),"sku_val":str,"pid_list":pid_list,"remarks_val":remarks_val
				},
				dataType : 'text',
                async: false,
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
		<br>
		<span style="color:red">必要采样数据不参与采用订单生成，走正常的采购流程</span>
	</div>
	<div>
		<table id="table1" align="center" border="1px"
			style="font-size: 13px;" width="1200" bordercolor="#8064A2"
			cellpadding="0" cellspacing="0">
			<Tr>
				<th>序号</th>
				<th>工厂名</th>
				<th>ali链接</th>
				<th>ali名称</th>
				<th>1688图片</th>
				<th style="width:150px;">1688价格￥</th>
				<th>规格</th>
				<th>规格库存</th>
				<th>采样链接</th>
				<th>采样属性</th>
				<th>是否生成采样商品<input
					type="checkbox" id="checkall" name="checkall"
					onclick="checkAll(checkall)" />全选
				</th>
			</Tr>

			<c:forEach items="${allList}" var="sgb" varStatus="i">
				<Tr class="a">
					<td>${i.index+1}</td>
					<td>${sgb.shop_id}</td>
					<td style="width:50px;"><a href="${sgb.goods_url}" title="1688产品链接" target="_blank" >ali链接</a></td>
					<td><a href="${sgb.url_1688}" title="1688产品链接" target="_blank">${sgb.goods_name}</a>
					</td>

					<td><a target='_blank' href="https://www.import-express.com/goodsinfo/a-1${sgb.goods_pid}.html"><img src="${sgb.img_1688}" title="1688图片,点击跳转网站产品单页" width="220px" height="220px"></img></a></td>
					<td>${sgb.price_1688}</td>
					<td>${sgb.type_msg}</td>
					<td style="width:200px">${sgb.sku_inventory}</td>
					<td style="width:200px"><a target="_blank" href="https://detail.1688.com/offer/${sgb.r_pid}.html">${sgb.r_shop_id}</a></td>
					<c:if test="${sgb.remarks == '必须采样'}">
						<td style="width:380px;color:red"><span id="remarks_${sgb.r_pid}">${sgb.remarks}</span></td>
					</c:if>
					<c:if test="${sgb.remarks == '备用采样'}">
						<td style="width:380px;color:blue"><span id="remarks_${sgb.r_pid}">${sgb.remarks}</span></td>
					</c:if>
					<c:if test="${sgb.remarks == '建议采样'}">
						<td style="width:380px;color:green"><span id="remarks_${sgb.r_pid}">${sgb.remarks}</span></td>
					</c:if>
					<td style="width: 150px;" id="c_${sgb.r_pid}">
						<c:if test="${sgb.remarks != '必须采样'}">
							<input type="checkbox"
								   name="createOrder" id="createOrder" value="${sgb.r_pid}" />
							<input type="hidden" id="oldPid_${sgb.r_pid}" value="${sgb.goods_pid}">
							<br><input type="hidden" style="width:75px;" value="1" id="num_${sgb.r_pid}">
							<div  style="width: 150px;height:220px;" onclick="ch_Color(this,'${sgb.r_pid}');"></div>
						</c:if>
						</td>
				</Tr>
			</c:forEach>

		</table>
		<%--<div align="center">${pager }</div>--%>

	</div>
</body>
</html>