<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.cbt.website.userAuth.bean.*" %>
<%@page import="com.cbt.processes.servlet.Currency" %>
<%@page import="java.util.List" %>
<%@page import="com.cbt.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	//不允许浏览器端或缓存服务器缓存当前页面信息。
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	response.addHeader("Cache-Control", "no-cache");//浏览器和缓存服务器都不应该缓存页面信息
	response.addHeader("Cache-Control", "no-store");//请求和响应的信息都不应该被存储在对方的磁盘系统中；
	response.addHeader("Cache-Control", "must-revalidate");///于客户机的每次请求，代理服务器必须想服务器验证缓存是否过时；
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
	<link rel="shortcut icon" href="/cbtconsole/img/cbtconsole.png" type="image/x-icon"/>
	<link id="skin" rel="stylesheet" href="/cbtconsole/js/warehousejs/jBox/Skins2/Green/jbox.css"/>
	<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
	<link rel="stylesheet" href="/cbtconsole/css/warehousejs/refunddeal.css">
	<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/i18n/jquery.jBox-zh-CN.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
	<script type="text/javascript">
        <%	String sessionId = request.getSession().getId();
            String userJson = Redis.hget(sessionId, "admuser");
        %>
        var admJson = <%=userJson%>;
        var consoleName = '${admName}';
        var statue = '${stupresent}';
        var type = '${typeresent}';
	</script>
	<script type="text/javascript" src="/cbtconsole/js/refund/refund.js"></script>
	<title>退款管理</title>
</head>

<body class="bodym">
<input type="hidden" id="checked" value="0">

<c:if test="${roletype!='0' && roletype!='3' && roletype!='4'}">
	{"status":false,"message":"请重新登录进行操作"}
</c:if>

<input value="${roletype }" id="roletype" type="hidden">
<c:if test="${roletype=='0' || roletype=='3' || roletype=='4'}">
	<div class="col-xs-1">
		<c:if test="${admName=='Emma' || admName=='Ling'}">
				<span><input type="button" id="addSecValid"
							 onclick="showSecValid(0)" value="设置验证密码" class="btn"></span>
			<span><input type="button" id="updateSecValid"
						 onclick="showSecValid(1)" value="修改验证密码" class="btn"></span>
		</c:if>
		<a href="/cbtconsole/refundss/rlist" target="_blank">未匹配用户申诉记录</a>
	</div>
	<div class="main" align="center">
		<div class="tabletitle">退款处理</div>

		<div class="row" style="margin-bottom: 20px;">
			<div class="col-xs-2-1">
				<label>客户ID:</label><input id="userid" name="userid"
										   value="${userid }">
			</div>
			<div class="col-xs-2-1">
				<label>客户帐号:</label><input id="useraccount" name="useraccount"
										   value="${username}">
			</div>
			<div class="col-xs-2-1">
				<label>申请日期:</label> <input id="applyDate" class="Wdate"
											style="width: 110px; height: 26px" type="text" value="${appdate }"
											onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-12-12',maxDate:'2020-12-20'})"/>
			</div>
			<div class="col-xs-2-1">
				<label>完成日期:</label> <input id="agreeTime" class="Wdate"
											style="width: 110px; height: 26px" type="text"
											value="${agreeTime }"
											onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-12-12',maxDate:'2020-12-20'})"/>
			</div>
			<div class="col-xs-2-1">
				<label>状态:</label> <select id="_status" name="_status">
				<!-- 				<select id="_status" name="_status" onchange="FnsearchByState();subdate()"> -->

				<c:if test="${roletype=='0'}">
					<option value="-5">全部</option>
				</c:if>
				<c:if test="${admName!='Emma'}">
					<option value="0">新申请退款</option>
					<option value="1">销售已同意退款</option>
					<option value="-1">销售已驳回退款</option>
				</c:if>
				<c:if test="${roletype=='0'}">
					<option value="3">管理员已同意退款</option>
					<option value="-3">管理员已拒绝退款</option>
					<option value="-4">退款失败</option>
				</c:if>

				<option value="-2">客户已取消退款</option>
				<option value="2">退款已完结</option>

			</select>

				<label>来源:</label> <select id="from_status" name="from_status">
				<option value="-1">所有</option>
				<option value="0">余额提现</option>
				<option value="1">PayPal</option>
				<!-- <option value="2">用户投诉</option>
            <option value="3">邮件投诉</option> -->
			</select>
				<button type="button" class="btn btn-primary btn-sm "
						id="search_btn" onclick="search()">查询
				</button>
			</div>
			<div class="col-xs-1">
				<button type="reset" class="btn btn-primary btn-sm"
						onclick="resetPar()">重置
				</button>
			</div>
			<c:if test="${admName=='Ling'}">
				<div class="col-xs-1">
					<button type="reset" class="btn btn-primary btn-sm"
							style="background-color: #00a65a;"
							onclick="fnmsg(${messagecount})">${messagecount}</button>
				</div>
			</c:if>
			<div class="col-xs-1-1">
				<button type="button" class="btn btn-primary btn-sm"
						onclick="reportForm()">退款报表
				</button>
			</div>
			<div class="col-xs-1-1">
				<label>操作人:</label><span id="dealMan">${admName}</span>
			</div>
			<div class="col-xs-1-1">
				<input type="button" class="btn btn-primary btn-sm" value="添加退款记录"
					   onclick="$('.eiping').toggle()"/>
			</div>
		</div>

		<div class="eiping">
				<span>用户id:<input type="text" id="ruserid" class="ruserid"
								  name="ruserid" style="width: 100px;"></span> <span>&nbsp;&nbsp;&nbsp;&nbsp;金额:<input
				type="text" name="rappcount" class="rappcount" id="rappcount"
				style="width: 100px;"/> <span>&nbsp;&nbsp;&nbsp;&nbsp;PayPal账号:<input
				type="text" id="rpaypal" name="rpaypal" style="width: 300px;"/></span> <span>&nbsp;&nbsp;&nbsp;&nbsp;订单号:<input
				type="text" id="rorderid" name="rorderid" style="width: 300px;"/></span>
					<span>&nbsp;&nbsp;&nbsp;&nbsp;交易号:<input type="text"
															 id="rpayid" name="rpayid"
															 style="width: 300px;"/></span> <span>&nbsp;&nbsp;&nbsp;&nbsp;类型
						<select id="rtype" name="rtype">
							<option value="3" selected="selected">用户邮件投诉</option>
							<!-- <option value="1">用户paypal申诉</option> -->
					</select>
				</span>
				</span> <input type="submit" class="btn btn-primary btn-sm"
							   style="margin-left: 5px; display: inline-block; margin-left: 15px;"
							   value="提交" onclick="fnsubmint()"/>
			<!-- <form action="/cbtconsole/refundss/addRefund" method="post" onsubmit="return checkall()"  target="_self">
        </form> -->
		</div>
		<br>

		<!-- <div>Note：红色底纹Ling可批准;绿色底纹Emma可批准</div> -->
		<table class="table table-bordered  table-hover definewidth m10"
			   style="table-layout: fixed; word-wrap: break-word;">
			<thead>
			<tr>
				<td width="50px">#</td>
				<td width="80px">客户ID</td>
				<td width="200px">客户账号/PayPal帐号</td>
				<td width="40px">来源</td>
				<td width="300px">其他信息</td>
				<td width="100px">金额</td>
				<td width="100px">申请时间</td>
				<td width="130px">同意时间/<br>退款状态/<br>完结(时间)
				</td>
				<td width="320px">实际退款金额</td>
				<td width="300px">备注</td>
				<td width="260px">反馈</td>
			</tr>
			</thead>
			<tbody id="trRecords">
			<c:if test="${status==1}">
				<c:forEach items="${list}" var="refund" varStatus="steps">
					<tr style="max-height: 200px;"
						bgcolor="${steps.index%2==0?'#FFF7FB':'#ECFFFF' }">
						<td align="center" width="50px">${refund.id }<input
								type="hidden" id="iidd" value="${refund.id}"/> <c:if
								test="${refund.type==1 && refund.status==1 && refund.account>0.01}">
							<input type="checkbox" id="checkpid_${refund.id}"
								   class="checkpid" style="height: 20px; width: 20px;"
								   value="${refund.id}">
						</c:if>
						</td>
						<!--用户id  -->
						<td width="80px" align="center" class="uid"><a
								href="/cbtconsole/userinfo/getUserInfo.do?userId=${refund.userid }"
								target="_blank"> ${refund.userid }</a> <br>
							<br>
							<span style="font-weight: bold;"> ${refund.adminUid} </span></td>
						<td width="200px"><span><em>客户账号:</em><br>
									<a title="点击查看用户消费详情"
									   onclick="getRecordsDetail('${refund.userid }')">${refund.username }</a></span>
							<br/> <br/> <span><em>PayPal帐号:</em><br>
									<span
											style="font-size: 15px; font-weight: bold; color: #2196F3;">${refund.paypalname}</span></span>

							<input type="hidden" value="${refund.userEmail}"></td>

						<!--  来源 -->
						<td width="40px" align="center"><c:if
								test="${refund.type==0 }">
							<span style="color: black; font-weight: bold;">余额提现</span>
						</c:if> <c:if test="${refund.type==1 }">
							<span style="color: red; font-weight: bold;">PayPal</span>
						</c:if> <c:if test="${refund.type==2 }">
							<span style="color: #2196F3; font-weight: bold;">用户投诉</span>
						</c:if> <c:if test="${refund.type==3 }">
							<span style="color: #C210E0; font-weight: bold;">邮件投诉</span>
						</c:if></td>

						<td width="150px"><c:if test="${refund.type==1 }">
										<span><em>订单号:</em><a
												href="/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo=${refund.orderid}"
												target="_blank">${refund.orderid}</a></span>
							<br>
							<span><em>交易号:</em>${refund.payid}</span>
							<br>
							<span><em>type:</em>${refund.casetype}</span>
							<br>
							<span><em>code:</em>${refund.reasoncode}</span>
							<br>
							<span><em>note:</em>${refund.reasonnote}</span>
							<c:if test="${admName=='Ling'}">
								<br>
								<span><input type="button" class="bt-jz"
											 onclick="changeavailable(${refund.userid},'${refund.orderid}','');"
											 value="余额 补偿 / 校正"></span>
								<br>
							</c:if>
						</c:if> <c:if test="${refund.type==2 }">
							<c:if test="${admName!='Emma' || admName!='Ling'}">
								<c:if test="${refund.additionBanlance>0}">
												<span>该投诉已经余额补偿过:<em
														style="font-weight: bold; font-size: 16px; color: red;">${refund.additionBanlance}</em>&nbsp;USD
												</span>
									<c:if test="${refund.status==0}">
										<br>
										<span>若要退款至客户PayPal,请注意余额清除</span>
										<span><input type="button" class="bt-jz"
													 onclick="changeavailable(${refund.userid},'',${refund.complainId});"
													 value="余额 补偿 / 校正"></span>
									</c:if>
									<br>
									<br>
								</c:if>
								<span><a
										href="/cbtconsole/complain/getComplainByCid?cid=${refund.complainId}"
										target="_blank">投诉详情</a></span>
							</c:if>
							<c:if test="${admName=='Emma' || admName!='Ling'}">
								该用户有投诉
							</c:if>
						</c:if></td>

						<td width="100px" align="center"><span>当前余额:</span> <em
								style="color: #347936; font-weight: bold;">${refund.balance }&nbsp;${refund.balanceCurrency }</em>
							<br> <br> <span>退款金额:</span> <em
									style="color: red; font-weight: bold;">${refund.appcount }&nbsp;${refund.currency }</em>
						</td>
						<!-- 申请时间 -->
						<td width="100px" align="center">申请时间: <br>
								${refund.apptime } <c:if
									test="${refund.type==1&&refund.delaytime!=null}">
								<br>
								<br>
								<span>截止时间: <br> <em
										style="color: #2196F3; font-weight: bold;">${refund.delaytime}</em></span>
								<c:if test="${refund.isDelay!=0}">
									<br>
									<span style="color: red; font-weight: bold; font-size: 20px;">
											${refund.isDelay==1?'即将截止':'已经截止'}</span>
								</c:if>
							</c:if>
						</td>

						<td align="center" width="100px">
							<span style="color: red; font-weight: bold; font-size: 14px;">${refund.agree}</span>
							<br>${refund.agreetime}<br>
							<em style="color: #2196F3; font-weight: bold;">${refund.endtime}</em>
						</td>
						<!-- 实际退款 -->
						<td>
							<div style="position:relative;">
									<%--状态 0-申请退款
                                         1-销售同意退款
                                         2-退款完结
                                         3-管理员同意退款
                                         -1-销售驳回退款
                                         -2 -客户取消退款
                                         -3-管理员拒绝退款--%>
								<input type="hidden" value="${refund.status}" id="state_${refund.id}">
									<%--用户余额--%>
								<input type="hidden" value="${refund.balance}" id="balance_${refund.id}">
									<%--申请退款数额--%>
								<input type="hidden" value="${refund.appcount}" id="appcount_${refund.id}">
									<%--来源  0-提现
                                        1-paypal
                                        2-用户网上投诉
                                        3-邮件投诉--%>
								<input type="hidden" value="${refund.type}" id="type_${refund.id}">
									<%--用户id--%>
								<input type="hidden" value="${refund.userid}" id="user_${refund.id}">
									<%--订单id--%>
								<input type="hidden" value="${refund.orderid}" id="order_${refund.id}">

								<input type="hidden" value="${refund.payid}" id="payid_${refund.id}">
									<%--实际退款金额--%>
								<input type="hidden" value="${refund.additionid}" id="additionid_${refund.id}">
								<input type="hidden" value="${refund.complainId}" id="complainid_${refund.id}">

								<!-- 只有新申请的退款，并且是网上投诉或者邮件投诉的退款 才可以修改实际退款金额   只有Ling操作-->
								<input type="hidden" id="od_list" value="${refund.orderNoList}">
								<c:if test="${refund.status== 0 && admName!='Emma'}">

									<input type="text" value="${refund.account }" id="account_${refund.id}"
										   class="acrefund" style="width: 70px;">
									${refund.currency } &nbsp;&nbsp;&nbsp;
									<input type="button" value="查询可用订单" style="color: white;background-color: green"
										   onclick="findCanRefundOrderNo(${refund.userid},'account_${refund.id}','refundOrderId_${refund.id}','${refund.orderid}',${refund.type})"/>

									<%--$('#refundOrderId option:selected') .val();//选中的值--%>
									<select id="refundOrderId_${refund.id}" style="width: 270px;">
										<option value="">请选择订单号</option>
											<%--<c:forEach var="orderNoList" items="${refund.orderNoList }">
                                                <option value="${orderNoList.orderid}">${orderNoList.orderid}</option>
                                            </c:forEach>--%>
									</select>
									<br><br>
									<span>请选择退款原因</span>
									<select id="reson_${refund.id}">
										<option value="1">产品无货</option>
										<option value="2">无法发货</option>
										<option value="3">交期延误</option>
										<option value="4">客户申请</option>
										<option value="5">其他原因</option>
									</select>
								</c:if>
								<c:if test="${refund.status!= 0 || admName=='Emma'}">
									<input id="refundOrderId_${refund.id}" type="text" readonly="readonly" value="${refund.orderid}"/>
									<input type="hidden" value="${refund.account }" id="account_${refund.id}" />
									<span>${refund.account}</span>${refund.currency }
									<br>
									<input type="hidden" value="${refund.refundType}" id="reson_${refund.id}"
										   class="resonfund">
									<span>${refund.refundType}</span>
								</c:if>

								<c:if test="${(roletype=='3' || roletype=='4') && refund.status== 0 }">

									<textarea
											style="min-height: 72px; max-height: 80%;width: 300px;line-height: 15px; float: left;"
											id="remark_${refund.id}"></textarea>


									<div class="remark_sty">
										<input type="button" value="确认并备注" name="actionBtn"
											   class="btn btn_remark"
											   onclick="confirmAndRemark(${refund.id},${refund.status},1,${refund.type})">
										<input type="button" value="拒绝并备注" name="actionBtn"
											   class="btn btn_remark"
											   onclick="confirmAndRemark(${refund.id},${refund.status},0,${refund.type})">
									</div>
								</c:if>

								<c:if test="${roletype=='0'}">
									<c:if test="${(refund.status== 1 || refund.status== -1) && admName=='Ling'}">
											<textarea
													style="min-height: 72px;  max-height: 80%;width: 300px;line-height: 15px; float: left;"
													id="remark_${refund.id}"></textarea>
										<div class="remark_sty">
											<input type="button" value="确认并备注" name="actionBtn"
												   class="btn btn_remark"
												   onclick="confirmAndRemark(${refund.id},${refund.status},1,${refund.type})">
											<input type="button" value="拒绝并备注" name="actionBtn"
												   class="btn btn_remark"
												   onclick="confirmAndRemark(${refund.id},${refund.status},0,${refund.type})">
										</div>
									</c:if>
									<c:if test="${(refund.status== 3 || refund.status== -3) && admName=='Emma'}">

											<textarea
													style="min-height: 72px;  max-height: 80%;width: 300px;line-height: 15px; float: left;"
													id="remark_${refund.id}"></textarea>
										<div class="remark_sty">
											<input type="button" value="确认并备注" name="actionBtn"
												   class="btn btn_remark"
												   onclick="confirmAndRemark(${refund.id},${refund.status},1,${refund.type})">
											<input type="button" value="拒绝并备注" name="actionBtn"
												   class="btn btn_remark"
												   onclick="confirmAndRemark(${refund.id},${refund.status},0,${refund.type})">
										</div>
									</c:if>
								</c:if>
							</div>
						</td>

						<!-- 备注 -->
						<td style="position: relative; text-align: left; width: 320px;"
							align="center">
							<div
									style="min-height: 160px; width: 100%; word-wrap: break-word;">
								<!-- 新申请的退款   ling只有拒绝权利 以及添加备注-->

									<%-- <c:if test="${refund.status== 0 }">
                                        ${refund.remark}
                                    </c:if>

                                    <!-- 银行处理中 -->
                                    <c:if test="${refund.status== 1 || refund.status== 3 }">
                                        ${refund.remark}
                                    </c:if>

                                    <!-- 退款驳回 -->
                                    <c:if test="${refund.status== -1 }">
                                        ${refund.remark}
                                        <c:if test="${refund.refuse !=null && refund.refuse != ''}">
                                            <span>拒绝理由:</span>
                                        </c:if>
                                        ${refund.refuse}
                                    </c:if>
                                    <!-- 退款完结 -->
                                    <c:if test="${refund.status== 2  }">
                                        ${refund.remark}
                                    <br>
                                        <span>${refund.outcomeCode}</span>
                                    </c:if> --%>

									${refund.remark}
								<c:if test="${refund.refuse !=null && refund.refuse != ''}">
									<span>拒绝理由:</span>
									${refund.refuse}
								</c:if>
								<c:if test="${refund.status== 2  }">
									<br>
									<span>${refund.outcomeCode}</span>
								</c:if>

							</div>
						</td>

						<!-- 反馈 -->
						<td style="width: 320px;"><c:if
								test="${refund.status != -1 && refund.status != -2}">
							<div
									style="width: 100%; height: 160px; word-wrap: break-word;">
								<div style="height: 30px;"></div>
								<br>

								<c:if test="${admName!='Ling'}">
									${refund.feedback}
									<br>
								</c:if>

								<c:if test="${admName=='Ling'}">

									<c:if test="${refund.feedback != null}">
										<c:if test="${refund.status == 2}">
											${refund.feedback}
										</c:if>
										<c:if test="${refund.status != 2}">
														<textarea
																style="background-color: #DFF0D8; max-height: 60px; width: 220px; line-height: 15px; float: left;"
																id="feedback_${refund.id}" rows="10"
																cols="34">${refund.feedback}</textarea>
										</c:if>
									</c:if>
									<c:if test="${refund.feedback==null && refund.status != 2}">
													<textarea
															style="max-height: 80px; width: 220px; line-height: 15px; float: left;"
															id="feedback_${refund.id}" rows="10"
															cols="34">${refund.feedback}</textarea>
									</c:if>
									<c:if test="${refund.status != 2}">
										<input type="button" class="btn btn_remark"
											   onclick="addFeedBack(${refund.id },${refund.orderid})"
											   value="反馈">
									</c:if>
								</c:if>

							</div>
						</c:if></td>
					</tr>
				</c:forEach>
			</c:if>
			</tbody>
		</table>

		<div style="display: none">
			<form id="form"
				  action="/cbtconsole/payment/getRefundAblePaymentByUid"
				  method="post">
				<input type="text" id="fuid" name="uid" value=""> <input
					type="text" id="fbalance" name="balance" value=""> <input
					type="text" id="fcurrency" name="currency" value=""> <input
					type="text" id="fappcount" name="appcount" value=""> <input
					type="text" id="fappcurrency" name="appcurrency" value="">
			</form>
		</div>
		<div>
			<c:choose>
				<c:when test="${pagecount==0 }">
					<label>当前没有数据</label>
				</c:when>
				<c:otherwise>
					<span>共查到&nbsp;${count }&nbsp;条数据</span>&nbsp;&nbsp;
					<a id="prepage">上一页</a>&nbsp;
					第<span id="pagenow">${pagenow }</span>页/共<span id="pagecount">${pagecount }</span>页&nbsp;
					<a id="nextpage">下一页</a>&nbsp;
					<input type="text" id="topage" value="1" style="width: 40px"
						   onkeydown="if(event.keyCode==13){enterToJump()}"/>
					<input type="button" id="jumpToPage" value="Go"/>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

</c:if>
<div style="display: none">
	<form id="refund_form" name="refund_form">
		<input type="hidden" name="rid" value="" id="form_saleid"/>
		<!-- <input type="hidden" name="saleid" value="" id="form_saleid" /> <input -->
		type="hidden" name="money" value="" id="form_money" /> <input
			type="hidden" name="currency" value="" id="form_currency"/> <input
			type="submit" name="refund_submit" id="refund_submit"/>
	</form>
</div>


<div id="div_iframe" align="center">
	<div class="maitit">
		退款结果<span class="maiclo" onclick="mhide()">X</span>
	</div>
	<div>
		<iframe name="refund_iframe" class="fram_class"></iframe>
	</div>
</div>

<div id="div_secvlid">
	<h3>请输入验证密码</h3>
	<input type="hidden" id="refund_id" value=""/>
	<input type="hidden" id="refund_type" value=""/>
	<input type="hidden" id="refund_state" value=""/>
	<label for="select_userid">用户：</label>
	<select id="select_userid" disabled="disabled">
		<option value="1" selected="selected">Ling</option>
		<option value="8" selected="selected">Emma</option>
	</select>
	<br>
	<label for="secvlid_pwd">密码：</label>
	<input type="password" id="secvlid_pwd" value=""/>
	<br>
	<input type="button" value="确定" onclick="checkSecvlidPwd()" class="btn btn-primary btn-sm"/>
	<input type="button" value="取消" onclick="hideDivSecvlid()" class="btn btn-primary btn-sm"/>
</div>

<div id="div_secvlid_addedit">
	<h3 id="div_secvlid_title">设置验证密码</h3>
	<br>
	<input type="hidden" id="edit_type" value=""/>
	<label for="userid_lst">用户：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
	<select id="userid_lst" disabled="disabled" style="width:180px">
		<option value="1" selected="selected">Ling</option>
		<option value="8" selected="selected">Emma</option>
	</select>

	<br>
	<label for="secvlid_old" id="check_old">原始密码：</label>
	<input type="password" id="secvlid_old" style="width:180px" value=""/>

	<br>
	<label for="secvlid_pwd_addedit">密码：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
	<input type="password" id="secvlid_pwd_addedit" style="width:180px" value=""/>

	<br>
	<label for="secvlid_pwd_addedit_two">确认密码：</label>
	<input type="password" id="secvlid_pwd_addedit_two" style="width:180px" value=""/>

	<br>
	<label for="secvlid_addedit_remark">备注：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
	<textarea id="secvlid_addedit_remark" rows="2" style="width:180px"></textarea>

	<br>
	<input type="button" value="确定" onclick="checkSecvlidPwdAddEdit()" id="add_update" class="btn btn-primary btn-sm"/>
	&nbsp;&nbsp;
	<input type="button" value="取消" onclick="hideDivSecvlidAddEdit()" class="btn btn-primary btn-sm"/>
</div>
<!-- HomTU start-->
<div id="bigestdiv" onclick="FncloseOut();" class="bgd"></div>
<!-- HomTU end-->
</body>
</html>