<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>马帮到货查询</title>
	<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var state = 0;
		var page = 1;
		fn(state,page, true);
		var date = new Date();
		var year = date.getFullYear();
		var mon = date.getMonth()+1;
		var startYear = document.getElementById("startYear");
		var endYear = document.getElementById("endYear");
		var startMon = document.getElementById("startMon");
		var endMon = document.getElementById("endMon");
		for(var i=2016;i<=year;i++){
			startYear.options.add(new Option(i, i));
			endYear.options.add(new Option(i, i));
		}
		for(var i=1;i<=12;i++){
			startMon.options.add(new Option(i, i));
			endMon.options.add(new Option(i, i));
		}
		var day = new Date(endYear,endMon,0).getDate();
		var end = endYear+"-"+endMon+"-"+day;
		cost("2016-05-01", end); //采购统计
	});

	function prePage(){
		var now = $("#nowPage").html();
		var state = $("#state").val();
		if(parseInt(now)==1){
			alert("已到达首页");
			return;
		}else{
			$("#nowPage").html(parseInt(now)-1);
			fn(state, parseInt(now)-1, false);
		}
	}
	
	function nextPage(){
		var now = $("#nowPage").html();
		var all = $("#AllPage").html();
		var state = $("#state").val();
		if(parseInt(now)==parseInt(all)){
			alert("已到达尾页");
			return;
		}else{
			$("#nowPage").html(parseInt(now)+1);
			fn(state, parseInt(now)+1, false);
		}
	}
	
	function goPage(){
		var all = $("#AllPage").html();
		var state = $("#state").val();
		var page = $("#input_page").val();
		if(isNaN(page)){
			alert("请输入正确的页码。");
		}else if(parseInt(page) <1 || (parseInt(page)>parseInt(all))){
			alert("页码超出范围。");
		}else{
			$("#nowPage").html(page);
			fn(state,page, false);
		}
	}
	
	function search(){
		var state = $("#state").val();
		fn(state,1,true);
	}
	
	function fn(state,page, fst){
		$.ajax({
			url:"/cbtconsole/warehouse/selectInstorage?state="+state+"&page="+page+"&rand="+Math.random(),
			type:"post",
			dataType:"json",
			success:function(data){
				$("#tab tbody").html("");
				if(data.length>0){
					for(var i =0;i<data.length;i++){
						$("#tab tbody").append("<tr height='35px' bgcolor='"+(i%2==0? '#F0F0F0':'#FDEDF8')+"'>"+
							"<td>"+((page-1)*20+i+1)+"</td>"+
							"<td>"+data[i].orderno+"</td>"+
							"<td>"+data[i].shipno+"</td>"+
							"<td>"+data[i].sku+"</td>"+
							"<td style='word-break:break-all'><a href='"+data[i].url+"' target='_blank'>"+data[i].url+"</td>"+
							"<td>"+data[i].productunitprice.toFixed(2)+"</td>"+
							"<td>"+data[i].productnumbers+"</td>"+
							"<td>"+data[i].unitCost.toFixed(2)+"</td>"+
							"<td>"+data[i].productnumbers+"</td>"+
							"<td>"+(data[i].state==0 ? '<font color="red">未到货</font>':'<font color="green">已到货</font>')+"</td>"+
							"</tr>");
					}
					$("#datacount").html(data[0].count);
					$("#AllPage").html(data[0].page);
				}else{
					$("#tab tbody").append("<tr height='30px' bgcolor='#FDEDF8'><td colspan='9' style='text-align:center;'>没有数据</td></tr>");
					$("#datacount").html(0);
					$("#AllPage").html(0);
				}
				if(fst){
					$("#nowPage").html(1);
				}
			}
		});
	}
	
	function cost(start_date, end_date){
		$.ajax({
			url:"/cbtconsole/warehouse/selectPurchaseCost?start_date="+start_date+"&end_date="+end_date,
			type:"post",
			dataType:"json",
			success:function(data){
				$("#cb tbody").html("");
				var total = 0.0;
				for(var i in data){
					$("#cb tbody").append("<tr height='30px' bgcolor='"+(i%2==0? '#F0F0F0':'#FDEDF8')+"'><td>"+data[i].ym+"</td>"+
					"<td>"+data[i].cost+"</td></tr>");
					total = total + data[i].cost;
				}
				$("#total").html(total.toFixed(2));
			},
			error:function(){
				alert("Error");
			}
		});
	}
	
	function searchCost(){
		var startYear = $("#startYear").val();
		var startMon = $("#startMon").val();
		var endYear = $("#endYear").val();
		var endMon = $("#endMon").val();
		var day = new Date(endYear,endMon,0).getDate();
		var start = startYear+"-"+startMon+"-01";
		var end = endYear+"-"+endMon+"-"+day;
		cost(start, end);
	}
	
	function show(f){
		if(f==1){
			$("#div1").css("display","block");
			$("#bt2").css("display","block");
			$("#div2").css("display","none");
			$("#bt1").css("display","none");
		}else{
			$("#div1").css("display","none");
			$("#bt2").css("display","none");
			$("#div2").css("display","block");
			$("#bt1").css("display","block");
		}
	}
	</script>
	</head>
  <body>
	<div align="center">
		<input type="button" value="马帮到货查询" onclick="show(1)" style="display: none;font-size: 26px;" id="bt1">
	  	<input type="button" value="采购成本查询" onclick="show(2)" style="display: block;font-size: 26px;" id="bt2">
	</div>
	<hr>
	<div id="div1" style="display: block;">
		<h2 style="text-align: center;">马帮到货查询</h2>
		<div style="text-align: center;">
		选择到货类型：
		<select id="state">
			<option value="-1">所有采购单</option>
			<option value="0" selected="selected">所有未到货</option>
			<option value="1">近3天到货</option>
			<option value="2">所有已到货</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" value="查询"  onclick="search()"/>
		</div>
		<br>
		<div style="text-align: center;">
			<table border="0"  id="tab" align="center">
				<thead>
					<tr bgcolor="#E1EBFA" height="45px" >
						<th width="50px">序号</th>
						<th width="200px">订单编号</th>
						<th width="200px">货运单号</th>
						<th width="180px">SKU</th>
						<th width="220px">商品链接</th>
						<th width="100px">销售单价</th>
						<th width="100px">销售数量</th>
						<th width="100px">采购单价</th>
						<th width="100px">采购数量</th>
						<th width="100px">到货状态</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div><br>
		<div style="text-align: center;">
			共有<span id="datacount">0</span>条记录&nbsp;
			<input type="button" onclick="prePage()"  value="上一页"/>
			第<span id="nowPage">1</span>页/共<span id="AllPage">0</span>页
			<input type="button"  onclick="nextPage()" value="下一页"/>&nbsp;
			<input type="text" value="1"  id="input_page"  style="width:40px" /><input  type="button"  onclick="goPage()" value="Go"/>
		</div>
	</div>
	<div id="div2" style="display: none">
		<div style="text-align: center;">
			<h2>采购成本</h2>
			开始时间：<select id="startYear" style="width:60px;"></select> 年
			<select id="startMon" style="width:40px;"></select> 月&nbsp;
			结束时间：<select id="endYear" style="width:60px;"></select> 年
			<select id="endMon" style="width:40px;"></select> 月&nbsp;
			<input  type="button"  onclick="searchCost()" value="查询"/><br/>
			<br>
			<table id="cb" border="0"   align="center">
				<thead>
					<tr height="45px" bgcolor="#E08E0B" style="color:white">
						<th width="200px">日期</th>
						<th width="200px">采购成本统计</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table><br/>
			查询时段内采购成本总计：<label style="color:red;" id="total">0</label>
		</div>
	</div>
  </body>
</html>