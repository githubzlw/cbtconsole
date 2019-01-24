<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运单&运费列表</title>

<link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css">
<style type="text/css">
.check{
  display:none;
}
.repalyDiv{width: 500px;background: #34db51;text-align: center;position: fixed;left: 40%;top: 43%;}
.repalyDiv1{width: 500px;background: #34db51;text-align: center;position: fixed;left: 40%;top: 43%;}
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
</head>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String date = sdf.format(new Date());
%>
<script type="text/javascript">
$(function(){
	$("#hide_repalyDiv").click(function(){
		hideRepalyDiv();
	});
	$("#hide_repalyDiv1").click(function(){
		hideRepalyDiv1();
	});
})

function hideRepalyDiv(){
	$(".repalyDiv").hide();
	$("#flag_remark").val("");
	$("#sm_id").val("");
	$("#totalprice").val("");
}
function hideRepalyDiv1(){
	$(".repalyDiv1").hide();
	$("#flag_remark1").val("");
	$("#sm_id1").val("");
	$("#yugu_fright").val("");
}
var checkedList = new Array();
function selectAllOrInverse(){
	var obj=document.getElementsByName("sid"); 
	var count = obj.length;
	if (!$("#selAll").prop('checked')) {
		for(var i = 0; i < count; i++){
			obj[i].checked = false;
			checkedList.remove(i);
		}
	}else{
		for(var i = 0; i < count; i++){
			obj[i].checked = true;
			checkedList[i]=obj[i].value;
		}
	}
}
function checkedProOrCancel(id){
	var obj=document.getElementsByName("sid"); 
	var count = obj.length;
	var selectCount = 0;
	for (var j = 0; j < count; j++) {
		if(obj[j].checked == true) 
		{ 
		selectCount++;	
		} 
	}
	if(count == selectCount){	
		$("#selAll").prop("checked", true);
	}else{
		$("#selAll").prop("checked", false);
	}
}

Array.prototype.remove=function(dx) 
{ 
    if(isNaN(dx)||dx>this.length){return false;} 
    for(var i=0,n=0;i<this.length;i++) 
    { 
        if(this[i]!=this[dx]) 
        { 
            this[n++]=this[i] 
        } 
    } 
    this.length-=1;
}
var curPage;
	function searchShipment(page) {
		curPage = page;
		var company = $('#company').val();
		var senttimeBegin = $('#senttimeBegin').val();
		var senttimeEnd = $('#senttimeEnd').val();
		var type = $('#type').val();
		$('#exportBtn').css('display','none');
		$("#shipmentList tbody").html("");
		$("#shipmentList tfoot").html("");
		$(".check").css('display','none');
		$('#estimatefreight').css('display','none');
		$('#actual_freight').css('display','none');
		if(senttimeBegin==null || senttimeBegin==""){
			alert("请选择开始时间");
			return;
		}
		$.ajax({
			type:'post',
			url:'/cbtconsole/shipment/loadShipment',
			data:{'company':company,'senttimeBegin':senttimeBegin,'senttimeEnd':senttimeEnd,'choiseType':type,'page':page},
			dataType:'json',
			success:function(res){
				$('#exportBtn').css('display','inline-block');
				if (res!=undefined && res.totalCount > 0) {
					var tabStr = "";
					for (var i = 0; i < res.rows.length; i++) {
						var obj = res.rows[i];
						tabStr+=("<tr>");
						tabStr+=("<td align='center'><input type='checkbox' name='sid' value='"+obj.id+"' onclick='checkedProOrCancel("+obj.id+");'/></td>");
						tabStr+=("<td>"+obj.transportcompany+"</td>");
						tabStr+=("<td>"+obj.orderno+"</td>");
						tabStr+=("<td>"+obj.senttime+"</td>");
						tabStr+=("<td>"+obj.country+"</td>");
						tabStr+=("<td>"+obj.numbers+"</td>");
						tabStr+=("<td>"+obj.realweight+"</td>");
						tabStr+=("<td>"+obj.sweight+"</td>");
						tabStr+=("<td>"+obj.volumeweights+"</td>");
						tabStr+=("<td>"+obj.fuelsurcharge+"</td>");
						tabStr+=("<td>"+obj.securitycosts+"</td>");
						tabStr+=("<td>"+obj.taxs+"</td>");
						tabStr+=("<td>"+obj.totalprice+"</td>");
						tabStr+=("<td>"+obj.remark+"</td>");
						tabStr+=("<td>"+obj.flag_remarks+"</td>");
						if(type== 0){
							tabStr+=("<td>"+obj.estimatefreight+"</td>");
							tabStr+=("<td>"+obj.actual_freight+"</td>");
						}
						if (type == 2 || type == 3) {
							tabStr+=("<td>"+obj.sweight+"</td>");
							tabStr+=("<td>"+obj.svolume+"</td>");
							tabStr+=("<td>"+obj.tscompany+"</td>");
							tabStr+=("<td>"+obj.xtcountry+"</td>");
							tabStr+=("<td>"+obj.delivery+"</td>");
							tabStr+=("<td>"+obj.estimatefreight+"</td>");
							tabStr+=("<td>"+obj.actual_freight+"</td>");
							tabStr+=("<td><input type='button' value='可以支付' onclick='updateShipMentFlag("+obj.id+","+obj.totalprice+")'><input type='button' value='不可支付' onclick='updateNoPay("+obj.id+","+obj.estimatefreight+")'></td>");
						}
						tabStr+=("</tr>");
					}
					if (type == 0) {
						$("#shipmentList tfoot").append("<tr><td colspan='14'><span style='color:red;font-size:20px'>"+res.remark+"</span>&nbsp;总条数:"+res.totalCount+"&nbsp;当前页:<span>"+res.currePage+"</span>&nbsp;总页数:"+res.totalPage+"<div class='demo'><div id='demo1'></div></div></td></tr>");
					}
// 					else if (type == 2 || type == 3) {
// 						$("#shipmentList tfoot").append("<tr><td colspan='19'><span style='color:red;font-size:20px'>物流商账单总运费:"+res.totalPrice+res.remark+"</span>&nbsp;总条数:"+res.totalCount+"&nbsp;当前页:<span>"+res.currePage+"</span>&nbsp;总页数:"+res.totalPage+"<div class='demo'><div id='demo1'></div></div></td></tr>");
// 					}else{
// 						$("#shipmentList tfoot").append("<tr><td colspan='13'><span style='color:red;font-size:20px'>物流商账单总运费:"+res.totalPrice+res.remark+"</span>&nbsp;总条数:"+res.totalCount+"&nbsp;当前页:<span>"+res.currePage+"</span>&nbsp;总页数:"+res.totalPage+"<div class='demo'><div id='demo1'></div></div></td></tr>");
// 					}
					$("#demo1").paginate({
						count 		: res.totalPage,
						start 		: curPage,
						display     : 8,
						border					: true,
						border_color			: '#fff',
						text_color  			: '#fff',
						background_color    	: '#239ed7',	
						border_hover_color		: '#ccc',
						text_hover_color  		: '#239ed7',
						background_hover_color	: '#fff', 
						images					: false,
						mouse					: 'press'
					});
				}
				if (type == 2 || type == 3) {
					$('#estimatefreight').css('display','table-cell');
					$('#actual_freight').css('display','table-cell');
					$(".check").css('display','table-cell');
// 					$('#exportBtn1').css('display','inline-block');
					$('#exportBtn3').css('display','inline-block');
				}else{
// 					$('#exportBtn1').css('display','none');
					$('#exportBtn3').css('display','none');
				}
				if (type == 0) {
					$('#estimatefreight').css('display','table-cell');
					$('#actual_freight').css('display','table-cell');
				}
				$("#shipmentList tbody").append(tabStr);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("系统错误,请联系开发人员确认问题!");
			}
		});
	}
	function updateNoPay(sm_id,totalprice){
		$("#sm_id1").val("");
		$("#yugu_fright").val("");
		$(".repalyDiv1").show();
		$("#sm_id1").val(sm_id);
		$("#yugu_fright").val(totalprice);
	}
	function updateShipMentFlag(sm_id,totalprice){
		$("#sm_id").val("");
		$("#totalprice").val("");
		$(".repalyDiv").show();
		$("#sm_id").val(sm_id);
		$("#totalprice").val(totalprice);
	}
	function insertNoPay(){
		var fright=$("#yugu_fright").val();
		var rep_type = $("input[name='ro']:checked").val();
		if($("input[name='ro']:checked").size()<1){
			alert("请选择方式");
			return ;
		}
		if(rep_type=="1"){
			fright=$("#ac_fright").val();
			var reg=/^[0-9]+$/;
			var result= reg.test(fright);
			 if(!result){
				 alert("运费只能是数字！");
				 return;
			 }
		}
		var sm_id1=$("#sm_id1").val();
		var flag_remark1=$("#flag_remark1").val();
		if(flag_remark1==null || flag_remark1==""){
			alert("请填写原因!");
			return;
		}
		$.ajax({
			type:'post',
			url:'/cbtconsole/shipment/updateShipMentFlag',
			data:{'sm_id':sm_id1,"flag_remark":flag_remark1,"totalprice":fright},
			dataType:'json',
			success:function(res){
				alert(res.message);
				searchShipment(curPage);
				hideRepalyDiv1();
			},error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("系统错误,请联系开发人员确认问题!");
			}
		});
	}
	function insertShipMentFlag(){
		var sm_id=$("#sm_id").val();
		var totalprice=$("#totalprice").val();
		var flag_remark=$("#flag_remark").val();
		if(flag_remark==null || flag_remark==""){
			alert("请填写可以支付的原因!");
			return;
		}
		$.ajax({
			type:'post',
			url:'/cbtconsole/shipment/updateShipMentFlag',
			data:{'sm_id':sm_id,"flag_remark":flag_remark,"totalprice":totalprice},
			dataType:'json',
			success:function(res){
				alert(res.message);
				searchShipment(curPage);
				hideRepalyDiv();
			},error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("系统错误,请联系开发人员确认问题!");
			}
		});
	}
// 	function batchUpdateShipMentFlag(){
// 		var checked = '';
// 		$('input[name="sid"]:checkbox:checked').each(function(i){
// 	        if(0==i){
// 	        	checked = $(this).val();
// 	        }else{
// 	        	checked += (","+$(this).val());
// 	        }
// 	    });
// 		if (checked == '') {
// 			alert("请选择可以支付的运单信息!");
// 			return;
// 		}
// 		$.ajax({
// 			type:'post',
// 			url:'/cbtconsole/shipment/batchUpdateShipMentFlag',
// 			data:{'idStr':checked},
// 			dataType:'json',
// 			success:function(res){
// 				alert(res.message);
// 				searchShipment(curPage);
// 			},error:function(XMLHttpRequest, textStatus, errorThrown){
// 				alert("系统错误,请联系开发人员确认问题!");
// 			}
// 		});
// 	}
	function delShipment(){
		var checked = '';
		$('input[name="sid"]:checkbox:checked').each(function(i){
	        if(0==i){
	        	checked = $(this).val();
	        }else{
	        	checked += (","+$(this).val());
	        }
	    });
		if (checked == '') {
			alert("请选择要删除的运单信息!");
			return;
		}
		$.ajax({
			type:'post',
			url:'/cbtconsole/shipment/delShipment',
			data:{'idStr':checked},
			dataType:'json',
			success:function(res){
				alert(res.message);
				searchShipment(curPage);
			},error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("系统错误,请联系开发人员确认问题!");
			}
		});
	}
	function resetCondition() {
		$('#company').val(0);
		$('#senttimeBegin').val("<%=date%>");
		$('#senttimeEnd').val("<%=date%>");
		$('#type').val(0);
	}
	function exportShipment(){
		var company = $('#company').val();
		var senttimeBegin = $('#senttimeBegin').val();
		var senttimeEnd = $('#senttimeEnd').val();
		var type = $('#type').val();
		window.open('/cbtconsole/shipment/exportshipment?company='+company+'&senttimeBegin='+senttimeBegin+'&senttimeEnd='+senttimeEnd+'&choiseType='+type+"&type=0");
	}
	function exportShipment1(){
		var company = $('#company').val();
		var senttimeBegin = $('#senttimeBegin').val();
		var senttimeEnd = $('#senttimeEnd').val();
		var type = $('#type').val();
		window.open('/cbtconsole/shipment/exportshipment?company='+company+'&senttimeBegin='+senttimeBegin+'&senttimeEnd='+senttimeEnd+'&choiseType='+type+"&type=1");
	}
	
	function gd() {  
		var rep_type = $("input[name='ro']:checked").val();
		if($("input[name='ro']:checked").size()<1){
			alert("请选择方式");
			return ;
		} 
		if(rep_type=="1"){
			document.getElementById("ac_fright").disabled = false;  
		}
	}
	
	// 录入某个月的物流商赔偿款 
	function showDiv(){
		var rfddd = document.getElementById("insertOrderInfo");
		rfddd.style.display = "block";
	}
	function FncloseInsert(){
		var rfddd = document.getElementById("insertOrderInfo");
		rfddd.style.display = "none";
		$("#amounts").val("0");
		$("#datas").val("0");
		$("#comps").val("0");
	}
	function insertSources(){
		var amounts=$("#amounts").val();
		var datas=$("#datas").val();
		var comps=$("#comps").val();
		if(datas=="0"){
			alert("请选择月份");
			return;
		}
		if(Number(amounts)<=0){
			alert("请输入大于0的正数");
			return;
		}
		if(comps=="0"){
			alert("请选择物流公司");
			return;
		}
		$.ajax({
			type:'post',
			url:'/cbtconsole/shipment/insertSources',
			data:{'amounts':amounts,"datas":datas,"comps":comps},
			dataType:'json',
			success:function(res){
				alert(res.message);
				if(res.message.indexOf("成功")>-1){
					FncloseInsert();
					searchShipment(curPage);
				}
			},error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("系统错误,请联系开发人员确认问题!");
			}
		});
	}
</script>
    <script type="text/javascript" src="/cbtconsole/js/website/jquery.paginate.js"></script>
    <link rel="stylesheet" href="/cbtconsole/css/style.css">
<style type="text/css">
.demo{
                width:580px;
                padding:10px;
                margin:10px auto;
                border: 1px solid #fff;
                background-color:#f7f7f7;
            }
.mod_pay3 {
	width: 720px;
	position: fixed;
	top: 100px;
	left: 15%;
	z-index: 1011;
	background: gray;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
	border: 15px solid #33CCFF;
}
</style>
<body>
	<h2 align="center">运单&运费列表</h2>
	<div class="mod_pay3" style="display: none;" id="insertOrderInfo">
			<h3 class="show_h3">赔偿款录入</h3>
			<div style="margin-left:30px">
				月份：<select id="datas">
				 <option value="0">请选择</option>
				  <option value="2017-01">2017-01</option>
				  <option value="2017-02">2017-02</option>
				  <option value="2017-03">2017-03</option>
				  <option value="2017-04">2017-04</option>
				  <option value="2017-05">2017-05</option>
				  <option value="2017-06">2017-06</option>
				  <option value="2017-07">2017-07</option>
				  <option value="2017-08">2017-08</option>
				  <option value="2017-09">2017-09</option>
				  <option value="2017-10">2017-10</option>
				  <option value="2017-11">2017-11</option>
				  <option value="2017-12">2017-12</option>
				  <option value="2018-01">2018-01</option>
				  <option value="2018-02">2018-02</option>
				  <option value="2018-03">2018-03</option>
				  <option value="2018-04">2018-04</option>
				  <option value="2018-05">2018-05</option>
				  <option value="2018-06">2018-06</option>
				  <option value="2018-07">2018-07</option>
				  <option value="2018-08">2018-08</option>
				  <option value="2018-09">2018-09</option>
				  <option value="2018-10">2018-10</option>
				  <option value="2018-11">2018-11</option>
				</select>
				运输公司：<select id="comps">
				    <option value="0">请选择</option>
					<option value="JCEX">佳成JCEX</option>
					<option value="emsinten">邮政</option>
					<option value="原飞航">原飞航</option>
					<option value="SF">顺丰-睦鹏</option>
					<option value="深圳诚泰">航邮-深圳诚泰</option>
					<option value="TL">泰蓝-舜衡安</option>
					<option value="灿鑫">灿鑫</option>
					<option value="迅邮">迅邮</option>
					<option value="大誉">大誉</option>
					<option value="衡欣">衡欣</option>
					<option value="DHL">DHL</option>
				</select>
				金额：<input type="text" name="amounts" id="amounts" class="remark" style="width: 250px;"/>
			</div>
			<input type="hidden" id="goods_imgs"> <input type="hidden"
				id="TbOrderid"> <input type="hidden" id="TbGoodsid">
			<input type="button" id="idAddResource" value="提交"
				onclick="insertSources();"
				style="width: 90px; height: 40px; margin-top: 20px;margin-left:200px;" /> <input
				type="button" value="取消" onclick="FncloseInsert();"
				style="width: 90px; height: 40px;" />
	</div>
	<form class="form-inline" role="form">
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">运输公司</div>
				<select id="company" class="form-control">
					<option value="0">请选择</option>
					<option value="JCEX">佳成JCEX</option>
					<option value="emsinten">邮政</option>
					<option value="原飞航">原飞航</option>
					<option value="SF">顺丰-睦鹏</option>
					<option value="深圳诚泰">航邮-深圳诚泰</option>
					<option value="TL">泰蓝-舜衡安</option>
					<option value="灿鑫">灿鑫</option>
					<option value="迅邮">迅邮</option>
					<option value="大誉">大誉</option>
					<option value="衡欣">衡欣</option>
					<option value="DHL">DHL</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">发件日期</div>
						<input id="senttimeBegin"
							name="senttimeBegin" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /></td>
							~<input id="senttimeEnd"
							name="senttimeEnd" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})" value="${param.enddate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /></td>
<%-- 				<input id='senttimeBegin' class="form-control" type="date" value="<%=date%>" style="width: 155px"> --%>
<!-- 				<div class="input-group-addon">~</div> -->
<!-- 				<input id='senttimeEnd' class="form-control" type="date" value="" style="width: 155px"> -->
			</div>
		</div>
		
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">筛选</div>
				<select id='type' class="form-control">
					<option value="0">请选择</option>
					<option value="1">当前系统不存在的运单</option>
					<option value="2">运费超过5%的运单</option>
					<option value="3">运费超过5%/重量超过20%的运单</option>
				</select>
			</div>
		</div>
		<button type="button" class="btn btn-primary" onclick="searchShipment(1)">搜索</button>
		<button type="button" class="btn btn-warning" onclick="resetCondition()">重置</button>
		<button type="button" class="btn btn-danger" onclick="delShipment()">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" class="btn btn-primary" onclick="showDiv();">上传赔偿金额</button>
		<a href='/cbtconsole/website/shipment.jsp' target="_blank" class="btn btn-info">上传运单信息</a>
		<button type="button" id="exportBtn" class="btn btn-success" onclick="exportShipment()" style="display: none;">导出Excel</button>
		<a href='/cbtconsole/shipment/showCalculFreight'  target="_blank"  class="btn btn-success" >前三个月的运费综合</a>
<!-- 		<button type="button" id="exportBtn1" class="btn btn-success" onclick="batchUpdateShipMentFlag()" style="display: none;">批量判断为可以支付</button> -->
		<button type="button" id="exportBtn3" class="btn btn-success" onclick="exportShipment1()" style="display: none;">导出判断为不可以支付运单Excel</button>
	</form>
	<table id='shipmentList' class="table table-bordered table-condensed table-striped">
		<thead>
			<tr>
				<th width="55px"><input type="checkbox" id="selAll" onclick="selectAllOrInverse();"/>全选</th>
				<th>运输公司</th>
				<th>运单号</th>
				<th>发件时间</th>
				<th>目的地</th>
				<th>件数</th>
				<th>重量(运输公司)</th>
				<th>重量(我司)</th>
				<th>抛重(我司)</th>
<!-- 				<th>出货时间(我司)</th> -->
				<%--<th>COPY物流公司运费(RMB)</th>--%>
				<th>燃油附加费(RMB)</th>
				<th>安检费(RMB)</th>
				<th>税金(RMB)</th>
				<th>物流公司总运费(RMB)</th>
				<th style="width: 200px">备注</th>
				<th style="width: 200px">eric备注</th>
				<th class="check">实际重量</th>
				<th class="check">实际体积</th>
				<th class="check">运输方式</th>
				<th class="check">国家</th>
				<th class="check">交期</th>
				<th id='estimatefreight' style="display: none;">公式预估运费(RMB)</th>
				<th id='actual_freight' style="display: none;">人工改后运费(RMB)</th>
				<th class="check">人工判断</th>
			</tr>
		</thead>
		<tbody></tbody>
		<tfoot></tfoot>
	</table>
	<div>
    	<div class="repalyDiv" style="display:none;">
			<input id="sm_id" type="hidden" value="">
			<input id="totalprice" type="hidden" value="">
			回复内容: <a id="hide_repalyDiv" style="color: red;float: right;margin-right: 10px;font-size: 24px;text-decoration:none" href="javascript:void(0);">X</a><br>
			<textarea name="flag_remark" rows="8" cols="50" id="flag_remark"></textarea>
			<font color="red" id="ts"></font><br>
			<input type="button" id="repalyBtnId" onclick="insertShipMentFlag()" value="提交回复">
		</div>
		<div class="repalyDiv1" style="display:none;">
			<input id="sm_id1" type="hidden" value="">
			<input id="yugu_fright" type="hidden" value="">
			以预估运费<input type="radio" id="ro" name="ro" onclick="gd()" value="0">&nbsp;&nbsp;&nbsp;&nbsp;自定义运费<input type="radio" id="ro" onclick="gd()" name="ro" value="1">
			<br>实际运费:<input type="text" id="ac_fright" disabled="disabled"><br>
			回复内容: <a id="hide_repalyDiv1" style="color: red;float: right;margin-right: 10px;font-size: 24px;text-decoration:none" href="javascript:void(0);">X</a><br>
			<textarea name="flag_remark1" rows="8" cols="50" id="flag_remark1"></textarea>
			<font color="red" id="ts"></font><br>
			<input type="button" onclick="insertNoPay()" value="提交回复">
		</div>
    </div>
</body>
</html>