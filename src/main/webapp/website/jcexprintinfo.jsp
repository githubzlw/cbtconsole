<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
     

    
                     
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.base64.js" charset="UTF-8"></script> 
<script type="text/javascript" src="/cbtconsole/js/warehousejs/tableExport.js" charset="UTF-8"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
       <link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">
<meta content="text/html;charset=UTF-8" http-equiv="Content-Type">
<title>佳成运单数据</title>                         
        
<script type="text/javascript">
        
function aSubmit(){        
	document.getElementById("idForm").submit();
}
function updateDeclareinfoByOrderid(){
	var str = "";
	for(var i=0; i<Number($("#jcexList").val()); i++){
		str += "'"+$("#orderid_"+i).val()+"',";
	}
	if(str.indexOf(",") != -1){
		str = str.substring(0,str.length-1);
	}
	$.ajax({        
		type:"get", 
		url:"updateDeclareinfoByOrderid.do",
		dataType:"text",
		data:{orderidArray:str},
		success : function(data){
			
		}
	});
}


//样品数据表格
function getHsCode(index){
	var productName = $("#productname"+index).html();
	$.ajax({   
		type:"post", 
		url:"getHsCode",
		data:{productName:productName}, 
		dataType:"text",
		success : function(data){  
			$("#hscode"+index).html(data);
		}
	});
}

</script>
</head>   
<body>      
  
		<button type="button" onclick="$('#alternatecolor').tableExport({ type: 'excel', separator: ';', escape: 'false' });"  class="btn btn-default">
            <i class="glyphicon glyphicon-search">导出Excel</i>
        </button>
   
        <form action="getJcexPrintInfo.do" id='idForm'>
        	开始日期:<input type="text" id="ckStartTime" name="ckStartTime"  value="${ckStartTime}"  onfocus="WdatePicker({isShowWeek:true})"  />(0点0分0秒)
			结束日期:<input type="text" id="ckEndTime" name="ckEndTime" value="${ckEndTime}"  onfocus="WdatePicker({isShowWeek:true})"  />(0点0分0秒)

			<a href='javascript:void(0);' onclick="aSubmit()" class='className'>查询</a>
		</form>
	        
	<table id="alternatecolor">
		<tr>                     
			<td>运单条码</td>   <td>快件类型</td>   <td>客户名称</td>
			<td>寄件人</td>     <td>英文公司名称</td><td>发件人邮编</td>
			<td>海关编码</td>   <td>收件公司</td>   <td>收件人</td>
			<td>收件地址</td>   <td>收件国家</td>   <td>收件城市</td>
			<td>州名</td>      <td>收件邮编</td>   <td>收件人电话</td>
			<td>结算方式</td>   <td>件数</td>      <td>包装</td>
			<td>中文品名</td>   <td>HS CODE</td>  <td>申报币种</td>
			<td>支付币种</td>   <td>统一编号</td>   <td>重量</td>
			<td>体积</td>      <td>英文品名</td>   <td>内件数</td>
			<td>单价金额</td>   <td>计量单位</td>   <td>材质</td>
			<td>规格型号</td>   <td>申报价值</td>   <td>备注</td>
			<td>参考单号</td>
			
		</tr>
		<c:forEach items="${jcexList }" var="jcexInfo" varStatus="s">
		<tr>                         
			<td>${jcexInfo.express_no}</td>   <td></td>   <td></td>
			<td>${jcexInfo.uod.adminname}</td>     <td>${jcexInfo.uod.admincompany}</td><td>${jcexInfo.uod.admincode}</td>
			<td></td>   <td></td>   <td>${jcexInfo.uod.recipients }</td>
			<td>${jcexInfo.uod.address}${jcexInfo.uod.userstreet}</td>   <td>${jcexInfo.uod.zone}</td>   <td>${jcexInfo.uod.address2}</td>
			<td>${jcexInfo.uod.statename}</td>      <td>${jcexInfo.uod.zipcode}</td>   <td>${jcexInfo.uod.phone}</td>
			<td>PP</td>   <td>1</td>      <td>${jcexInfo.cargo_type}</td>
			<td ><p id="productname${s.index }">${jcexInfo.productname}</p>
			
			
			</td> 
			
			  <td id="hscode${s.index }"></td>  <td>${jcexInfo.productcurreny}</td>
			<td>USD</td>   <td></td>   <td>${jcexInfo.weight}</td>
			<td>${jcexInfo.volume_lwh}</td>      <td>${jcexInfo.producenglishtname}</td>   <td>${jcexInfo.productnum}</td>
			<td></td>   <td></td>   <td></td>
			<td></td>   <td>${jcexInfo.productprice}</td>   <td>${jcexInfo.productremark}</td>
			<td>${jcexInfo.useridAndOrderid}           
			<input id="orderid_${s.index }" type="hidden" value="${jcexInfo.orderno}"/>
			               
			</td>
			    
		</tr>
		        <script type="text/javascript">
				                  
			getHsCode('${s.index }');
			</script> 
		</c:forEach>          
	</table>        
	<input id="jcexList" type="hidden" value="${fn:length(jcexList)}"/>
			
			
<script type="text/javascript">
// updateDeclareinfoByOrderid(); 
</script>			
</body>
</html>
    