<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>创建优惠券</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link id="skin" rel="stylesheet" href="/cbtconsole/js/warehousejs/jBox/Skins2/Green/jbox.css" />
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/i18n/jquery.jBox-zh-CN.js"></script>
<!-- <link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css"> -->
<script type="text/javascript">
var searchReport = "/cbtconsole/StatisticalReport/searchCoupusManagement"; //报表查询
</script>
<style type="text/css">
.displaynone{display:none;}
.item_box{display:inline-block;margin-right:52px;}
.item_box select{width:150px;}
.mod_pay3 { width: 600px; position: fixed;
	top: 100px; left: 15%;      
	z-index: 1011; background: gray;
	padding: 5px; padding-bottom: 20px;
	z-index: 1011; border: 15px solid #33CCFF; }
.w-group{margin-bottom: 10px;width: 60%;text-align: center;}
.w-label{float:left;}
.w-div{margin-left:120px;}
.w-remark{width:100%;}
table.imagetable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #999999;
	border-collapse: collapse;
}
table.imagetable th {
	background:#b5cfd2 url('cell-blue.jpg');
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
}
table.imagetable td {
/* 	background:#dcddc0 url('cell-grey.jpg'); */
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
	word-break: break-all;
}
.displaynone{display:none;}
</style>
<%
  String all_amounts=request.getParameter("all_amount");
%>
<script type="text/javascript">
	$(document).ready(function(){
		all_amount=<%=all_amounts%>;
		document.getElementById("all_amount").innerHTML=all_amount;
		document.getElementById("remaining_amount").innerHTML=all_amount;
		$.ajax({
			type:"post", 
			url:"/cbtconsole/StatisticalReport/createBatch",
			data:{}, 
			//dataType:"text",
			success : function(data){  
				document.getElementById("batch").innerHTML=data.batch;
			}
		});
		//类别查询
		$.ajax({
			type:"post", 
			url:"/cbtconsole/StatisticalReport/queryUsingRange",
			data:{}, 
			//dataType:"text",
			success : function(data){  
				$('#range').html("");
				if(data){
					var reportDetailList=data.data.aliCategoryList;
					htm_='';
					for(var i=0;i<reportDetailList.length;i++){
						htm_+="<input type='checkbox' name='using_range' value='"+reportDetailList[i].id+"'>"+reportDetailList[i].category+"";
					}
					$('#range').append(htm_);
				}
			}
		});
	});
	
	function selectType(value){
		if(value=="2"){
			var rfddd = document.getElementById("div1");
			rfddd.style.display = "none";
			var rfddd = document.getElementById("div2");
			rfddd.style.display = "block";
			var rfddd = document.getElementById("div3");
			rfddd.style.display = "block";
		}else{
			var rfddd = document.getElementById("div1");
			rfddd.style.display = "block";
			var rfddd = document.getElementById("div2");
			rfddd.style.display = "none";
			var rfddd = document.getElementById("div3");
			rfddd.style.display = "none";
		}
	}
	
</script>

</head>
<body text="#000000">
<div class="wrapper">
 <div class="content-wrapper">
   <form id="adduserForm" name="adduserForm" action="" method="post">
   创建优惠券 <a href="javascript:history.back(-1)">&laquo; back</a>
     <h3>本次创建总经费:$<span id="all_amount" style="color:red;"></span></h3>
    	 已创建优惠券:
     <div id="created_coupon">
     </div>
     剩余经费金额:<span id="remaining_amount"></span><br>
     创建批次:<span id="batch"></span><br>
     优惠券名称：<input type="text" id="coupons_name">*优惠券名称会显示在前台优惠券上面<br>
     发放方式:<select id="disbursement">
         <option value="1">新用户发放</option>
         <option value="2">用户自领</option>
         <option value="4">运营赠送</option>
         <option value="3">下单自动奖励</option>
     </select>
     *新用户发放、用户自领、下单自动奖励为用户触发，需运营设定规则，规则开发后使用，运营赠送，为运营触发，主动发给用户<br>
     优惠券类型:<select id="coupons_type" onchange="selectType(this.value);">
         <option value="1">限额券</option>
         <option value="2">折扣券</option>
         <option value="3">运费抵用券</option>
     </select><br>
   <div id="div1">面值金额:<input type="text" id="denomination"></div>
  <div id="div2" style="display: none">折扣:<input type="text" id="discount">%</div>
     总发行量：<input type="text" id="total_circulation"><br>
     最低消费：<input type="text" id="minimum_cons">美元
  <div id="div3" style="display: none">最多优惠：<input type="text" id="most_favorable" value="0.00">美元&nbsp;&nbsp;&nbsp;*消费达到此额度才能使用，0表示不限制</div>
  <span>*消费达到此额度才能使用，0表示不限制</span><br>
  每人限领：<select id="for_most">
         <option value="1">1</option>
         <option value="2">2</option>
         <option value="3">3</option>
         <option value="4">4</option>
         <option value="5">5</option>
     </select><br>
     有效日期:
     <input type="radio" name="validity_day" value="1"/>永久有效<br>
     <input type="radio" name="validity_day" value="2"/><input id="startdate"
							name="startdate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />-<input id="enddate"
							name="enddate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /><br>*活动时间为美国太平洋时间<br>
     <input type="radio" name="validity_day" value="3"/>有效天数 买家领取成功时开始的<input type="text" id="days">天内<br>
     使用范围：<br>
     <div id="range" style="width:700px;">
     </div>
     <input type="button" value="提交并新建" id="sub"  onclick="fn();"/>
   </form>
 </div>
 
</div> 
</body>
<script type="text/javascript">

  function fn(){
	  var type= /^[1-9]+[0-9]*]*$/;
	  var re=new RegExp(type);
	  var remaining_amount=document.getElementById("remaining_amount").innerHTML;//剩余金额
	  var coupons_type=$("#coupons_type").val();
	  var denomination=$("#denomination").val();// 面值金额
	  var total_circulation=$("#total_circulation").val();// 总发行量
	  if(Number(coupons_type)!=2){
		  if(denomination.match(re)==null){
		  		alert("请输入正确的面值金额");
		  		return;
		  }
		  if(Number(denomination)*Number(total_circulation)>Number(remaining_amount)){
			  alert("创建的优惠券金额大于剩余金额");
			  return;
		  }
	  }
	  if(total_circulation.match(re)==null){
	  		alert("请输入正确的发行量");
	  		return;
	  	}
	  var minimum_cons=$("#minimum_cons").val();
	  if(minimum_cons.match(re)==null){
	  		alert("请输入正确的最低消费");
	  		return;
	  }
	  var almlev="";
	  var obj = document.getElementsByName("using_range");
	  for(var i=0;i<obj.length;i++){
	    if(obj[i].checked){
	     almlev += obj[i].value+",";
	    }
	   }

	  var for_most=$("#for_most").val();
	  var arr = document.getElementsByName("validity_day");
	  var batch=document.getElementById("batch").innerHTML;
	  var coupons_name=$("#coupons_name").val();
	  var type1=$("#disbursement").val();
	  var most_favorable=$("#most_favorable").val();
	  var discount=$("#discount").val();
	  var validity_day="";
	  var validity_type="";
	  for(var i=0;i<arr.length;i++){
          if(arr[i].checked){
                radioValue = arr[i].value;
                if(radioValue=="1"){
                	validity_type="1";
                }else if(radioValue=="2"){
                	var startdate=$("#startdate").val();
                	var enddate=$("#enddate").val();
                	if(startdate==null || startdate==""){
                		alert("请输入开始日期");
                		return;
                	}
                	var validity_day=startdate+"&"+enddate;
                	validity_type="2";
                }else if(radioValue=="3"){
                	var validity_day=$("#days").val();
                	validity_type="3";
                	if(validity_day.match(re)==null){
                		alert("请输入正整数");
                		return;
                	}
                }
          }
  	  }
	  $.post("/cbtconsole/StatisticalReport/createCoupons",
				{
				  "denomination":denomination,
		     	 "total_circulation":total_circulation,
		     	 "minimum_cons":minimum_cons,
		     	 "for_most":for_most,
		     	 "validity_day":validity_day,
		     	 "disbursement":type1,
		     	 "batch":batch,
		     	 "discount":discount,
		     	 "most_favorable":most_favorable,
		     	 "coupons_name":coupons_name,
		     	 "coupons_type":coupons_type,
		     	 "validity_type":validity_type,
		     	 "using_range":almlev
				}, function(res) {
					var i=res.data.tbAllCount;
					if(Number(i)>0){
						alert("新增成功");
						reset();
						var str="$"+denomination+"面额   "+total_circulation+"张"+"   共$"+Number(denomination)*Number(total_circulation)+"<input type='button' id="+i+" onclick='delCoupons("+i+","+(Number(denomination)*Number(total_circulation))+")' value='删除'><br>";
						$("#created_coupon").append(str);
						var remaining_amount=document.getElementById("remaining_amount").innerHTML;
						document.getElementById("remaining_amount").innerHTML=Number(remaining_amount)-(Number(denomination)*Number(total_circulation));
						if(Number(document.getElementById("remaining_amount").innerHTML)<=0){
							var stamp = document.getElementById("sub");
							stamp.style.display="none";
						}
					}else{
						alert("新增失败");	
					}
				});
  }
  
  function delCoupons(id,amount){
	  $.post("/cbtconsole/StatisticalReport/delCoupons",
				{
				  "id":id
				}, function(res) {
					var i=res.data.allCount;
					if(Number(i)>0){
						alert("删除成功");
						$("#"+id).attr("disabled", "true");
						$("#"+id).css("background","red");
						var remaining_amount=document.getElementById("remaining_amount").innerHTML;
						document.getElementById("remaining_amount").innerHTML=Number(remaining_amount)+Number(amount);
					}else{
						alert("删除失败");
					}
				});
  }
  
  function reset(){
	  $("#total_circulation").val("");
		$("#minimum_cons").val("");
		$("#for_most").val("");
		$("#startdate").val("");
		$("#enddate").val("");
		$("#days").val("");
		$("#coupons_name").val("");
		$("#denomination").val("");
		$("#coupons_type").attr("disabled","disabled"); 
		 var obj = document.getElementsByName("using_range");
		  for(var i=0;i<obj.length;i++){
			  obj[i].checked = false;
		   }
  }

  function BeginCreate(){
	  closeConpons();
	  window.location.href = "/cbtconsole/website/coupus_details.jsp";
  }
  
  function closeConpons(){
	   var rfddd = document.getElementById("insertInfo");
		rfddd.style.display = "none";
  }

   function addCoupons(){
	   var rfddd = document.getElementById("insertInfo");
		rfddd.style.display = "block";
   }

$("#prePage").click(function(){
	var nowPage = $("#nowPage").html();
	if(parseInt(nowPage)<=1 ){
		alert("已到达首页");
		return false;
	}else{
		$("#nowPage").html(parseInt(nowPage)-1)
		searchExport(parseInt(nowPage)-1);
	}
});
$("#nextPage").click(function(){
	var nowPage = $("#nowPage").html();
	var allPage = $("#allPage").html();
	if(parseInt(nowPage)==parseInt(allPage) ){
		alert("已到达尾页");
		return false;
	}else{
		$("#nowPage").html(parseInt(nowPage)+1)
		searchExport(parseInt(nowPage)+1);
	}
});
$("#jumpPage").click(function(){
	var allPage = $("#allPage").html();
	var topage = $("#toPage").val();
	if(isNaN(topage)){
		alert("请输入正确的页码");
		return false;
	}else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
		alert("页码超出范围");
		return false;
	}else{
		$("#nowPage").html(parseInt(topage))
		searchExport(parseInt(topage));
	}
});
//查询报表
$('#pgSearch').click(function(){
	searchExport(1)
});

function searchExport(page){
	$("#categroyReport tbody").html("");
	jQuery.ajax({
        url:searchReport,
        data:{"page":page
        	  },
        type:"post",
        success:function(data){
        	
        },
    	error:function(e){
    		alert("查询失败！");
    	}
    });
}




</script>
</html>