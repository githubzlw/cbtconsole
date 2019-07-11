<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page import="com.cbt.website.userAuth.bean.Admuser"%>
<%@ page import="com.cbt.util.SerializeUtil"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>Customer Questions & Answers</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<!-- <link rel="stylesheet" href="script/style.css" type="text/css"> -->
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<style type="text/css">
.displaynone {
	display: none;
}
.item_box {
	display: inline-block;
	margin-right: 52px;
}
.item_box select {
	width: 150px;
}
.mod_pay3 {
	width: 600px;
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
.w-group {
	margin-bottom: 10px;
	width: 60%;
	text-align: center;
}
.w-label {
	float: left;
}
.w-div {
	margin-left: 120px;
}
.w-remark {
	width: 100%;
}
table.imagetable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #999999;
	border-collapse: collapse;
}
table.imagetable th {
	background: #b5cfd2 url('cell-blue.jpg');
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
}
.panel-title {
	text-align: center;
	height: 30px;
	font-size: 24px;
}
table.imagetable td {
	/* 	background:#dcddc0 url('cell-grey.jpg'); */
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
	word-break: break-all;
}
.displaynone {
	display: none;
}
.but_color {
	background: #44a823;
	width: 80px;
	height: 24px;
	border: 1px #aaa solid;
	color: #fff;
}
.list_table td{
word-wrap:break-word;
word-break:break-all;
}

#easyui_dialog_div{
	position:fixed;
	width:100%;
	height:100%;
	top:0;
	left:0;
	background:rgba(0,0,0,0.6);
	overflow:hidden;
	z-index:999;
	display:none;
}
.mask_unput{
	width:400px;
	height:120px;
	background:#fff;
	position:absolute;
	left:0;
	right:0;
	bottom:0;
	top:50px;
	margin:auto;
}
.mask_center{
	width:100%;
	height:100px;
	background:#fff;
	position:absolute;
	left:0;
	right:0;
	bottom:0;
	top:0;
	margin:auto;
}
input{
outline:none;
}
</style>
<%
	//取出当前登录用户
	String sessionId = request.getSession().getId();
	String userJson = Redis.hget(sessionId, "admuser");
	Admuser adm = (Admuser) SerializeUtil.JsonToObj(userJson,
			Admuser.class);
	int userid = adm.getId();
%>

<script type="text/javascript">
$('#dlg').dialog('close');
</script>
</head>
<body >
<!-- <div id="dlg" class="easyui-dialog" title="回复内容" data-options="modal:true" style="width:400px;height:200px;padding:10px;display:none;">
	<div style="margin-bottom:20px">
		<input class="easyui-textbox" style="width:100%;height:60px" name="replyContent1" id="replyContent1" data-options="multiline:true">
		<input type="hidden" id="gbookid"/>
	</div>
	<div style="text-align:center;padding:5px 0">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateReply()" style="width:80px">提交回复</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('close');" style="width:80px">关闭</a>
	</div>
</div> -->

<!-- 审核不合格的原因 -->
<div  id="easyui_dialog_div">
	
		<div  class="mask_unput">
			<div class="mask_center">
			   <div style="margin-left: 375px;"><a class="btn btn-light btn-xs" onclick="remarkclose()">X</a></div>
				原因:<input class="textbox_id" style="width:90%;height:50px" name="remark" id="replyContent1"  value="">
				<a  class="btn btn-light btn-xs" onclick="okRemark()" style="width:80px;float: right;">确认</a>
				<input type="hidden" value="" id="okqid" name="qid">
				<input type="hidden" value="" id="okisreview" name="remarkflag">
			</div>
		</div>
</div>
<div class="body_main">
<h3 align="center">Customer Questions & Answers</h3>
<form id="query_form" action="questionlist"   method="post"   align="center">
      <div id="top_toolbar" style="padding: 5px; height: auto" >
		 <div>
			商品ID：<input class="easyui-textbox" name="pid" value="${pid}" id="pid" style="width: 11%; margin-top: 10px;">
			商品名称：<input class="easyui-textbox" name="goodsName" value="${goodsName}" id="pname" style="width: 10%;">
			负责人：<select class="easyui-combobox"  name="adminid" id="adminid">
					<option value="0" <c:if test="${adminId==0}">selected</c:if>>全部</option>
					<c:forEach var="adm" items="${ admList}">
					<option value="${adm.id }" <c:if test="${adm.id==adminId }">selected</c:if>>${adm.confirmusername}</option>
					</c:forEach>
			     </select>
			回复状态：
				<select class="easyui-combobox" name="replayflag" id="replayflag" style="width: 10%;">
					<option value="0" <c:if test="${replyFlag == 0 }">selected</c:if> >全部</option> 
					<option value="1" <c:if test="${replyFlag == 1 }">selected</c:if> >未回复</option> 
					<option value="2" <c:if test="${replyFlag == 2 }">selected</c:if> >已回复</option>
				</select>
            产品单页是否显示：
             <select class="easyui-combobox" name="replyStatus" id="replyStatus" style="width: 10%;">
                 <option value="0" <c:if test="${replyStatus == 0 }">selected</c:if> >全部</option>
                 <option value="1" <c:if test="${replyStatus == 1 }">selected</c:if> >不同意显示</option>
                 <option value="2" <c:if test="${replyStatus == 2 }">selected</c:if> >同意显示</option>
             </select>
				提问日期:
				<input  type="text" name="startdate" id="startdate" style="width: 88px" 
				placeholder="开始日期" title="开始日期" onfocus="WdatePicker({isShowWeek:true})"<c:if test="${startdate != null}">value="${startdate}"</c:if> > 
				<input  type="text" name="enddate" id="enddate"style="width: 88px"
					 placeholder="结束日期" title="结束日期" onfocus="WdatePicker({isShowWeek:true})"<c:if test="${enddate != null}">value="${enddate}"</c:if>>
				<a class="btn btn-light btn-xs" onclick="tosearch();">检索</a>
				<a class="btn btn-light btn-xs" onclick="doReset();">清除条件</a>
	   </div>
	</div>

	<br>
	<br>
	<div align="center">	
		<table class="list_table" id="simple-table" border=1 >
			<thead>
				<tr>
<!-- 					<th style="text-align:center;"><input type="checkbox" class="check"  onclick="checkBox('ids',this.checked)"/></th> -->
					<th style="text-align:center;width:110px;">序号</th>
					<th style="text-align:center;">商品ID</th>
					<th style="text-align:center;width:200px;">商品名字</th>
<!-- 					<th style="text-align:center;">提问编号</th> -->
					<th style="text-align:center;width:60px;">提问者</th>
                    <th style="text-align:center;width:60px;">销售</th>
					<th style="text-align:center;width:500px">提问内容</th>
					<th style="text-align:center;width:170px;">提问时间</th>
					<th style="text-align:center;width:80px;">回复人</th>
					<th style="text-align:center;width:220px;">回复内容</th>
					<th style="text-align:center;width:170px">回复时间</th>
					<th style="text-align:center;width:100px">审核状态</th>
					<th style="text-align:center;">操作</th>
				</tr>
				</thead>
				<tbody>
				 <c:forEach items="${resultList}" var="qa" varStatus="status">
	       		 <tr>
<%-- 	       		 	<td style="text-align:center;"><input type='checkbox' name='ids' value="${qa.questionid}"class="check" /></td> --%>
					<td style="text-align:center;">${status.index+1}</td>
					<td style="text-align:center;">${qa.pid}</td>
					<td style="text-align:center;"><a href="${qa.purl}" target="_blank">${qa.pname}</a></td>
<%-- 					<td style="text-align:center;">${qa.questionid}</td> --%>
					<td style="text-align:center;">${qa.userid}</td>
                    <td style="text-align:center;">${qa.admName}</td>
					<td style="text-align:center;">${qa.question_content}</td>
					<td style="text-align:center;">${qa.create_time}</td>
					<td style="text-align:center;">${qa.reply_name}</td>
					<td style="">
						<div id="rcontent_${qa.questionid}">
							<textarea rows="2" cols="28" id="${qa.questionid}_replay">${qa.reply_content}</textarea>
							<br>
							<c:if test="${not empty qa.reply_content}">
								<a class="easyui-linkbutton"  onclick="replay('${qa.questionid}','${qa.c_shop_id}','${qa.pid}','${qa.purl}')">修改 </a>
								<a class="easyui-linkbutton"  onclick="sendEmail('${qa.questionid}','${qa.c_shop_id}','${qa.pid}','${qa.purl}')">邮件发送</a>
							</c:if>
							<c:if test="${empty qa.reply_content}">
								<a class="easyui-linkbutton"  onclick="replay('${qa.questionid}','${qa.c_shop_id}','${qa.pid}')"> 确认 </a>
							</c:if>
						</div>
					</td>
					<td style="width: 8%;text-align:center;">${qa.reply_time}</td>
					<td>
					<c:if test="${qa.reply_status ==1}"><span style="color:red;">前台未同意显示<span></span></c:if>
					<c:if test="${qa.reply_status ==2 and not empty qa.reply_content}"><span style="color:green;">前台已同意显示<span></span></c:if>
					<c:if test="${qa.isShow==1}"><span style="color:red;">未影响同店铺商品</span></c:if>
					<c:if test="${qa.isShow==2 and not empty qa.reply_content and not empty qa.shop_id}"><span style="color:green;">已影响同店铺商品</span></c:if>
					</td>
					<td>
						<c:if test="${qa.reply_status ==1}">
							<br><input type="button" style="margin-top:8px;color:green" onclick="influenceShop('${qa.questionid}','${qa.contextFlag}','${qa.c_shop_id}',1);" value="同意前台显示">
						</c:if>
						<c:if test="${qa.reply_status ==2 and not empty qa.reply_content}">
							<br><input type="button" style="margin-top:8px;color:red" onclick="influenceShop('${qa.questionid}','${qa.contextFlag}','${qa.c_shop_id}',2);" value="撤销前台显示">
						</c:if>
						<c:if test="${qa.isShow==1}">
							<br><input type="button" style="margin-top:8px;color:green" onclick="influenceShop('${qa.questionid}','${qa.contextFlag}','${qa.c_shop_id}',3);" value="影响同店铺商品问答">
						</c:if>
						<c:if test="${qa.isShow==2 and not empty qa.reply_content and not empty qa.shop_id}">
							<br><input type="button" style="margin-top:8px;color:red" onclick="influenceShop('${qa.questionid}','${qa.contextFlag}','${qa.c_shop_id}',4);" value="撤销影响同店铺商品问答">
						</c:if>
						<input type="button" style="margin-top:8px;color:blue" onclick="deleteQuestion('${qa.questionid}');" value="删除问答">
					</td>
					</tr>
			 </c:forEach> 
			</tbody>
		</table>
	</div>
	<div align="center">
	<input type="hidden" value="${totalPage}" id="totalPage">
	<input type="hidden" value="${page}" id="cpage">
    总条数:${total}&nbsp;&nbsp;&nbsp;
	总共:
	${page}/${totalPage}
	<a class="easyui-linkbutton" id="precheck" onclick="gotopage(1)">上一页</a>
	<a class="easyui-linkbutton" id="nextcheck" onclick="gotopage(2)">下一页</a>
	
	<input type="hidden" value="${page}" name="page"  id="page_box">
	<input type="text" value="${page}"  id="page" class="easyui-textbox page_box">
	<a class="easyui-linkbutton" id="pagecheck" onclick="gotopage(3)">Go</a>
	</div>
</form>
</div>
</body>
<script type="text/javascript">
/* function goEdit(questionid){
	window.open("/cbtconsole/question/goEdit?questionid="+questionid);
} */

function deleteQuestion(id){
    if(!confirm("确定删除该问答吗？")){
        return;
    }
    $.ajax({
        type:'POST',
        dataType:'text',
        url:'/cbtconsole/question/deleteQuestion',
        data:{qid:id},
        success:function(res){
            if(res>0){
                location.reload();
            }else{
                alert("删除失败");
            }
        },
        error:function(XMLResponse){
            alert('error');
        }
    });
}

//翻页
function gotopage(flag){
	var cpage = parseInt($("#cpage").val());
	var total = parseInt($("#totalPage").val());
	if(total < 1){
		return ;
	}
	if(flag==1){
		$("#page_box").val(cpage-1);
	}else if(flag==2){
		$("#page_box").val(cpage+1);
	}else if(flag==3){
		$("#page_box").val($("#page").val());
	}
	if(parseInt($("#page_box").val()) > total || parseInt($("#page_box").val()) < 1){
		return ;
	}
	$("#query_form").submit();
}

//发送邮件给客户
function sendEmail(qid,shop_id,url,purl){
    var content = $("#"+qid+"_replay").val();
    if(content == ''){
        return ;
    }
    $.ajax({
        type:'POST',
        dataType:'text',
        url:'/cbtconsole/question/sendEmail',
        data:{qid:qid,rcontent:content,url:url,purl:purl},
        success:function(res){
            if(res>0){
                alert("邮件发送成功");
            }else{
                alert("邮件发送失败");
            }
        },
        error:function(XMLResponse){
            alert('error');
        }
    });
}
//检索
function tosearch(){
	$("#query_form").submit();
}
//是否影响改商品同店铺商品问答信息
function influenceShop(qid,contextFlag,shop_id,state){
	var str="";
	if(state == "1"){
		str="确定同意前台显示";
		if(contextFlag == "1"){
		    alert("未回复不允许前台显示");
		    return;
		}
	}else if(state == "2"){
		str="撤销前台显示";
	}else if(state == "3"){
        str="影响同店铺商品";
        if(contextFlag == "1"){
            alert("未回复不允许影响同店铺商品");
            return;
        }
    }else if(state == "4"){
        str="撤销影响同店铺商品";
    }
	if(!confirm(str)){
		return;
	}
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/question/influenceShop',
		data:{qid:qid,reply_content:contextFlag,shop_id:shop_id,state:state},
		success:function(res){
			if(res>0){
				location.reload();
			}else{
				alert(str+"-操作失败");
			}
		},
		error:function(XMLResponse){
            alert(str+"-操作失败");
		}
	});
}
//回复
function replay(qid,shop_id,url,purl){
	var content = $("#"+qid+"_replay").val();
	if(content == ''){
		return ;
	}
	var isShow=2;
	var shop_flag="1";
    if(window.confirm('是否影响同店铺商品？')){
        shop_flag="2";
     }
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/question/edit',
		data:{qid:qid,rcontent:content,isShow:isShow,shop_flag:shop_flag,url:url,shop_id:shop_id,purl:purl},
		success:function(res){
			if(res>0){
				var ccontent = $("#"+qid+"_replay").val();
				$("#rcontent_"+qid).html(content);
				location.reload();
			}else{
				alert('添加失败，请重新添加');
			}
		},
		error:function(XMLResponse){
			alert('error');
		}
	});
}
//打开备注弹框
function openRemark(qid,remarkFlag){
	if(remarkFlag !=0){
		$("#easyui_dialog_div").show();
		
		if(remarkFlag==2 || remarkFlag==3){
			$("#replyContent1").val($("#review_remark_"+qid).val());
		}
		$("#okqid").val(qid);
		$("#okisreview").val(remarkFlag);
	}
}
//确认修改备注
function okRemark(){
	var qid= $("#okqid").val();
	var remarkFlag= $("#okisreview").val();
	remark(qid,remarkFlag);
}
function remarkclose(){
	$("#easyui_dialog_div").hide();
}

//审核偶作
function review(qid){
	var remark = '';
	if(remarkFlag!=0){
	 remark = $("#replyContent1").val();
		if(remark==''){
			return ;
		}
	}
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/question/remark',
		data:{qid:qid,remark:remark,remarkflag:remarkFlag},
		success:function(res){
			$("#easyui_dialog_div").hide();
			if(res>0){
				if(remarkFlag==0){
					$("#oremark_"+qid).html("审核通过");
				}else{
					$("#oremark_"+qid).html("审核未通过<div>(<a class=\"btn btn-light btn-xs\" onclick=\"openRemark('"+qid
							+"',2)\">原因</a>)<a class=\"btn btn-light btn-xs\" onclick=\"openRemark('"+qid
									+"',3)\">修改</a><input type=\"hidden\" value=\""+remark
									+"\" id=\"review_remark_"+qid+"\"></div>");
				}
			}else{
				alert('失败，请重新操作');
			}
		},
		error:function(XMLResponse){
			alert('error');
		}
	});
}

//清除条件 
function doReset(){
	$('#query_form').form('clear');
	$('#replyStatus').combobox('setValue','0');
	$('#adminid').combobox('setValue','0');
}

//复选框全选控制
function checkBox(name,checked){
	//checked="checked"
	$("input[name="+name+"]").attr("checked",checked);
}
//批量操作
function updateAll(){
			var str = $("input[name=ids]:checked").size();
			if(str==0){
				alert("您没有选择任何内容!");
				return;
			}
			if(!confirm("确定要修改选中的数据为显示吗?")){
				return;	
			}
			$("#query_form").attr("action","updateAll");
			$("#query_form").attr("method","post");
			$("#query_form").submit();
		}
	function deleteAll(){
			var str = $("input[name=ids]:checked").size();
			console.info(str);
			if(str==0){
				alert("您没有选择任何内容!");
				return;
			}
			if(!confirm("确定要修改选中的数据为不显示吗?")){
				return;	
			}
			$("#query_form").attr("action","deleteAll");
			$("#query_form").attr("method","post");
			$("#query_form").submit();
		}
	
function changeIsShow(id,type){
	var str="";
	if(type == 1){
		str="确定屏蔽其他用户查看该问答？";
	}else{
		str="确定将该问答显示给其他客户？";
	}
	if(!confirm(str)){
		return;	
	}
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/question/changeIsShow',
		data:{pid:id,type:type},
		success:function(res){
			if(res>0){
				alert("操作成功");
				location.reload();
			}else{
				alert("操作失败");
			}
		},
		error:function(XMLResponse){
			alert('error');
		}
	});
}
</script>

</html>