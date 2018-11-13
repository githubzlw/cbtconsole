<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtprogram/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
.tabletitle{
	font-family : 微软雅黑,宋体;
	font-size : 2em;
	text-align:center;
	color:red;
	margin-bottom: 20px;
}
a{
	cursor: pointer;
}
.main{
    margin: 0 auto;
    width: 95%;
    margin-top: 30px;
} 
body{
	text-align: center;
	width: 1800px;
}
td{
vertical-align:middle;
    padding: 0 0;
    margin: 0 0;
}
#trRecords tr td button{
	height:30px;}
	
.tab_line{border:1px #e2e2e2 solid}
.tab_line td{padding:0 0;margin:0 0;border:1px #000 solid;}
}
table { table-layout: fixed;word-wrap:break-word;}
div { word-wrap:break-word;}
</style>

<title>投诉管理</title>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
	<div class="main">
			<div class="tabletitle" >投诉管理</div>
		<div class="row"  style="margin-bottom: 20px;">
			<div class="col-xs-2"><label>客户ID:</label><input type="text" id="userid" name="userid" value="${userid }">
			<input id="complainState" name="complainState" value="${complainState }"  type="hidden" >
			</div>
			<div class="col-xs-2"><label>客户帐号:</label><input type="text" id="useraccount" name="useraccount" value="${username }"></div>
			<div class="col-xs-2"><label>申请日期:</label>
				<input id="applyDate" class="Wdate" style="width: 174px;height: 26px" type="text" value="${appdate }" onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-12-12',maxDate:'2020-12-20'})"/>
			</div>
			<div class="col-xs-2"><label>状态:</label>
				<select id="_status" name="_status">
					<option value="-1">请选择</option>
					<option value="0">未处理</option>
					<option value="1">交涉中</option>
					<option value="2">已完结</option>
				</select>
			</div>

			<div class="col-xs-2"><label>投诉类型:</label>
				<select id="type" name="type">
					<option value="">请选择</option>
					<option value="Track order status">Track order status</option>
					<option value="Request business cooperation">Request business cooperation</option>
					<option value="Report quailty issues">Report quality issues</option>
					<option value="Technical issues">Technical issues</option>
					<option value="Product questions">Product questions</option>
					<option value="Graphics and text does not match">Graphics and text does not match</option>
					<option value="Cancellation of order">Cancellation of order</option>
					<option value="Other questions">Other questions</option>
				</select>
			</div>
			<div class="col-xs-1">
				<button type="button" class="btn btn-primary btn-sm" id="search_btn" onclick="search()">查询</button>
				<button type="reset" class="btn btn-primary btn-sm" id="reset_btn" onclick="rset();">重置</button>
			</div>
			<div class="col-xs-1">
				<label>当前操作人:</label><span id="dealMan"></span>
			</div>
			<!-- <div class="col-xs-1">
				<button type="button" class="btn btn-primary btn-sm" onclick="reportForm()">投诉报表</button>
			</div> -->
		</div>
<table class="table table-bordered  table-hover definewidth m10">
			<thead>
				<tr>
				<td style="width:100px;">#</td>
				<td style="width:100px;">客户ID</td>
				<td style="width:200px;">客户邮箱</td>
				<td style="width:300px;">投诉订单</td>
				<td style="width:200px;">投诉类型</td>
				<td style="width:600px;" >投诉内容</td>
				<td style="width:100px;">投诉状态</td>
				<td style="width:100px;">投诉时间</td>
				<td style="width:50px;">销售负责人</td>
				<td style="width:50px;">后台处理人</td>
				<td style="width:80px;">操作</td>
				<td style="width:80px;"></td>
				<td style="width:200px;" title="客户消息数量+销售消息数量=总数量"><a href="#">消息数量</a>(客户消息数量+销售消息数量=总数量)</td>
				</tr>
			</thead>
			<tbody id="trRecords">
					<c:if test="${status==1}">
						<c:forEach items="${page.list}" var="complain">
						<c:if test="${complain.counts !=0}">
						   <tr style="max-height: 200px;" >
						</c:if>
						<c:if test="${complain.counts==0}">
						   <tr style="max-height: 200px;"  bgcolor="${complain.isRefund==1 ?'#ECFFFF':'#FFF7FB'}">
						</c:if>
								<td align="center" >${complain.id}</td>
								<td align="center" >${complain.userid }</td>
								<td align="center"  style="width:300px;word-break:break-all">${complain.userEmail}</td>
								<td align="center" style="width:300px;word-break: break-all;">
									<a target="_blank" href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${complain.refOrderId}">${complain.refOrderId}</a>
								</td>
								<td align="center" >${complain.complainType }</td>
								<td align="center"  style="max-width: 600px;">${complain.complainText }</td>
								<td align="center">
									<c:choose>
										<c:when test="${complain.complainState==0}">

											<c:choose>
												<c:when test="${complain.complainType == 'Report quality issues'
																|| complain.complainType == 'Graphics and text does not match'
																|| complain.complainType == 'Cancellation of order'}">
													正在提问中
												</c:when>
												<c:otherwise>
													正在申诉
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:when test="${complain.complainState==1 }">
											沟通中
										</c:when>
										<c:when test="${complain.complainState==2 }">
											已完结
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
								</td>
								<td align="center" > ${complain.creatTime }</td>
								<td align="center">${complain.saleAdmin}</td>
								<td align="center" >${complain.dealAdmin }  </td>
								<td align="center">
								<c:if test="${complain.counts>0}">
						  			 <button type="button" style="background-color:red" class="btn btn-primary btn-sm" onclick="watchDetail('${complain.id}');">投诉详细(${complain.counts})</button>
								</c:if>
								<c:if test="${complain.counts==0}">
						   			<button type="button" class="btn btn-primary btn-sm" onclick="watchDetail('${complain.id}');">投诉详情</button>
								</c:if>
								</td>
								<td align="center">
								<c:if test="${complain.isRefund==0  && adminid==1}">
									<c:if test="${complain.complainState!=2}">
											<button type="button" class="btn btn-primary btn-sm" onclick="changeavailable(${complain.userid},${complain.id},'${complain.refOrderId}');">余额补偿</button>
									</c:if>
								</c:if>
								<c:if test="${complain.isRefund==1}">
										<span style="font-weight: bold;color: red;font-size:12px;">该投诉有退款申诉,请找Ling确认</span>
								</c:if>
								</td>
								<td>
									<em id="customNum">${complain.customSum}</em>+
									<em id="salerNum">${complain.salerSum}</em>=
									<em id="SumNum">${complain.customSum+complain.salerSum}</em>
								</td>
							</tr>
						</c:forEach>
					</c:if>
			</tbody>
		</table>
		
		
		<%-- <div id="pageNum" style="text-align: center;margin: 0 auto">
			<ul class="pagination">
			    <c:forEach var="i" begin="1" end="${page.countPage }" step="1">
			      <li><a onclick="jumpTo(${i})"><c:out value="${i}" /></a></li>
			    </c:forEach>
			</ul>
		</div> --%>
		
		<input type="hidden" value="${param.toPage}" id="currentPage">
		<input type="hidden" value="${page.countPage}" id="totalpage">
		<div id="pageNum" style="text-align: center;margin: 0 auto;position:relative;">
		<c:if test="${page.countPage>0 }">
			<c:set var="dot" value="0"></c:set>
			<ul class="pagination" style="font-weight: bold;">
			     <li>
					<c:if test="${param.toPage-1>0}">
			          <a onclick="jumpTo(${param.toPage-1})">&lt;</a>
					</c:if>
					<c:if test="${param.toPage<2}">
			      	<a>&lt;</a>
					</c:if>
			      </li>
			    <c:forEach var="i" begin="1" end="${page.countPage }" step="1" >
				    <c:if test="${param.toPage==i}">
				        <li><span style="color:#8BC34A;">${i}</span></li>
				    </c:if>
				    <c:if test="${param.toPage!=i}">
					    <c:if test="${i<6}">
					      <li><a onclick="jumpTo(${i})"><c:out value="${i}" /></a></li>
					    </c:if>
					    
					    <c:if test="${i>5 && i<page.countPage-5 }">
						    <c:if test="${dot==0 }">
						     <li><a >&nbsp;&nbsp;......&nbsp;&nbsp;</a></li>
								<c:set var="dot" value="1"></c:set>
						    </c:if>
					    </c:if>
					    
					   <c:if test="${i>5 && i>page.countPage-5}">
					      <li><a onclick="jumpTo(${i})"><c:out value="${i}" /></a></li>
					    </c:if>
				    </c:if>
			    </c:forEach>
			    
			    <li>
				<c:if test="${param.toPage<page.countPage}">
			      		<a onclick="jumpTo(${param.toPage+1})">&gt;</a>
					</c:if>
					<c:if test="${param.toPage>page.countPage-1}">
			      			<a>&gt;</a>
					</c:if>
			      </li>
			</ul>
				<div style="position:absolute;right:0;top:20px;">
					Go to Page
					<input type="text" id="to-page" style="width:50px;height:30px;margin-left:5px;padding:2px;border:1px solid #ddd;border-radius:5px;" value="${param.toPage}"/>
					<input type="button" style="width:40px;height:30px;padding:2px;border:1px solid #ddd;border-radius:5px;background-color: #2e6da4;color: white;" onclick="goTo()" value="Go">
				</div>
			</c:if>
		</div>
		
	</div>
	
	<script type="text/javascript">
$(function(){
	<%String sessionId = request.getSession().getId();
	String userJson = Redis.hget(sessionId, "admuser");
	Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
	String roletype=user.getRoletype();
	int uid = user.getId();
	String admName =user.getAdmName();%> 
	var roleTypejs = <%=roletype%>;
	var consoleName = '<%=admName%>';
	//根据权限处置按钮   用户为高级权限Ling   Emma   没有提交按钮
	$("#dealMan").html(consoleName);
	
})

function watchDetail(complainid){
	window.open('/cbtconsole/complain/getComplainByCid?cid='+complainid);
}


function rset(){
	$('input[type=text]').val("");
	//document.getElementById('applyDate').value = '';
	document.getElementById("_status").options[0].selected = true;
	$("#type").val("");
}

function search(){
	var uid = $("#userid").val();
	var useraccount = $("#useraccount").val();
	var applyDate = $("#applyDate").val();
	var statue = $("#_status").val();
	var type=$("#type").val();
	//var currentPage = ${page.currentPage};
	var currentPage=1;
	window.location.href = '/cbtconsole/complain/searchComplainByParam?userid='+uid+'&creatTime='+applyDate+'&complainState='+statue+'&username='+useraccount+'&toPage='+i+'&currentPage='+currentPage+"&type="+type;
}

function FnsearchByState(){
	var state =$("#_status").val();
	window.location = "/cbtconsole/refundss/searchByStatus?status="+state;
}

function jumpTo(i){
	var uid = $("#userid").val();
	var useraccount = $("#useraccount").val();
	var applyDate = $("#applyDate").val();
	//var statue = $("#_status").val();
	var statue = $("#complainState").val()
	window.location = '/cbtconsole/complain/searchComplainByParam?userid='+uid+'&creatTime='+applyDate+'&complainState='+statue+'&username='+useraccount+'&toPage='+i+'&currentPage='+1;
}

function changeavailable(userid,complainid,orderid){
	window.open("/cbtconsole/website/change_available.jsp?userid="+userid+"&available=0&orderid="+orderid+"&complainid="+complainid,"windows","height=500,width=900,top=300,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
}

function goTo(){
	var  page =  $('#to-page').val();
	var  totalpage =  $('#totalpage').val();
	if(page == ''||parseInt(page)>parseInt(totalpage)||parseInt(page)<1){
		return ;
	}
	jumpTo(page);
}
$(document).ready(function(){
    var type= '${type}';
    var _status='${complainState}';
    $("#type").val(type);
    $("#_status").val(_status);
})
</script>
    <!-- HomTU start-->
    <div id="bigestdiv" onclick="FncloseOut();" class="bgd"></div>
	<!-- HomTU end-->
</body>
</html>