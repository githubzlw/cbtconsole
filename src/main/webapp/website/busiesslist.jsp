<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
	<script type="text/javascript"
			src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>Busiess 询盘</title>
<style>
body{
   width: 100%;
}
table{
   width: 1200px;
   border-collapse: collapse;
}
.manager{
  margin-top: 20px;
  display: none;
}
.page{
 padding-left: 673px;
}
.top{
 font-size: 20px;
}
.feedbackBkgd{
	position: fixed;
	background: rgb(220, 220, 220);
	width: 100%;
	height: 100%;
	opacity: 0.8; 
	display:none;
}
.feedbackStyle{
	border: 1px solid #000;
	width: 63%;
	display:none;
	position: fixed;
	background: #fff;
	left: 19%;
	top: 45%;
}
.feedbackClose{
	position: absolute;
    right: 1%;
    top: 5px;
	color: #000;
    font-size: 30px;
    text-decoration: none;
    font-weight: bold;
}
</style>
<script type="text/javascript">  
$(function(){  
	getAdminUser();	
});

 
 function getPage(total,page,pagesize){
	 var pagecount={};
	 if(total%pagesize>0){
		 pagecount=parseInt(total/pagesize)+1;
	 }else{
		 pagecount=total/pagesize;
	 } 
	 var up="";
	 var down="";
	 if(page<=1){
		 up="<span>【上一页】</span>"
	 }else{
		 up="<span><a href='javascript:void(0);' onclick='getBusiess("+(page-1)+")'>【上一页】</a></span>";
	 }
	 if(page>=pagecount){
		 down="<span>【下一页】</span>"
	 }else{
		 down="<span><a href='javascript:void(0);' onclick='getBusiess("+(page+1)+")'>【下一页】</a></span>";
	 }
	 $("#page").html("<span>总数:"+total+"</span>&nbsp;&nbsp;&nbsp;"+
	                 "<span>每页:"+pagesize+"</span>&nbsp;"+
	                 "<span>页次:"+page+"/"+pagecount+"</span>&nbsp;&nbsp;&nbsp;"+
	                 "<span>分页:<a href='javascript:void(0);' onclick='getBusiess(1)'>【首页】</a></span>"+up+down+
	                 "<span><a href='javascript:void(0);' onclick='getBusiess("+pagecount+")'>【尾页】</a></span>"+
	                 "<span>跳转到第<input type='text' size='4' name='page' value='"+page+"' id='redirct' >页</span>"+
	                 "<span><input type='button' onclick='getPageNum()' value='确定'></span>");
 }
 
 function genPage(total,page,pagesize,type){
	 var pagecount={};
	 if(total%pagesize>0){
		 pagecount=parseInt(total/pagesize)+1;
	 }else{
		 pagecount=total/pagesize;
	 } 
	 var up="";
	 var down="";
	 if(page<=1){
		 up="<span>【上一页】</span>"
	 }else{
		 up="<span><a href='javascript:void(0);' onclick='queryByChoice("+(page-1)+","+ type +")'>【上一页】</a></span>";
	 }
	 if(page>=pagecount){
		 down="<span>【下一页】</span>"
	 }else{
		 down="<span><a href='javascript:void(0);' onclick='queryByChoice("+(page+1)+","+ type +")'>【下一页】</a></span>";
	 }
	 $("#page").html("<span>总数:"+total+"</span>&nbsp;&nbsp;&nbsp;"+
	                 "<span>每页:"+pagesize+"</span>&nbsp;"+
	                 "<span>页次:"+page+"/"+pagecount+"</span>&nbsp;&nbsp;&nbsp;"+
	                 "<span>分页:<a href='javascript:void(0);' onclick='queryByChoice(1,"+ type +")'>【首页】</a></span>"+up+down+
	                 "<span><a href='javascript:void(0);' onclick='queryByChoice("+pagecount+","+ type +")'>【尾页】</a></span>"+
	                 "<span>跳转到第<input type='text' size='4' name='page' value='"+page+"' id='redirct' >页</span>"+
	                 "<span><input type='button' onclick='doPageJump(" + type + ")' value='确定'></span>");
 }
 
 function getPageNum(){
	 getBusiess($("#redirct").val());
 }
 
 function doPageJump(type){
	 queryByChoice($("#redirct").val(),type);
 }
 
 function getAdminUser(){
		var url= "/cbtconsole/messages/selectAdminUser";
		jQuery.ajax({
	        url:url,
	        data:{"type" : "sell"},
	        type:"post",
	        success:function(data, status){
	        	if(data.ok){
	        		var admUserList = data.data;
	        		var admUser =new Object();
	        		var htm_='';
	        		//fenpei"><input type="hidden" id="adminid" value="'+messages.adminid+'"/><select id="todo"></select></td>
	        		$('.fenpei').each(function(){
	        			htm_='<option value="0">未分配</option>';
	        			var admid= $(this).find('#adminid').val();
	        			var isDelete= $(this).find('#isDelete').val();
	        			for(var i=0;i<admUserList.length;i++){
	        				admUser = admUserList[i];
	        				htm_ +='<option value="'+admUser.id+'">'+admUser.admname+'</option>';
	        			}
	        			$(this).find(".todo").empty().append(htm_);

	        		});
	            }else{
					 alert(data.message);
				}
	        },
	    	error:function(e){
	    		alert("客户列表查询失败！");
	    	}
	    });
	}
 //@author zhulangui 改变商业询盘状态
function changestatus(id){
	$.post("/cbtconsole/cbt/busiess/changeBusiess",{id:id},function(result){
	    if(result.code == 0){
	    	 window.location.reload();//刷新当前页面.
	    }else{
	    	alert("状态改变失败！");
	    }	    
	});
}
function distributionMessages(i){
    var adminid = $('#todo'+i).find("option:selected").val();
    var adminName = $('#todo'+i).find("option:selected").text();
    var hrefName=$('#email'+i).val();
    var userid=$('#userid'+i).val();
//    if(userid=='' || userid == "0"){
//        alert("用户ID错误");
//        return;
//    }
    $.ajax({
        url: "/cbtconsole/order/addUser",
        type: "POST",
        data: {
            "adminid": adminid,
            "userid": userid,
            "admName": adminName,
            "userName": hrefName,
            "email": hrefName,
            "orderNo": ''
        },
        dataType: "json",
        success: function (data) {
            if (data == 1) {
                var rdPage = $("#redirct").val();//跳转页
                var qrFilter = $("#queryFilter").val();//筛选项
                queryByChoice(rdPage, qrFilter);
            } else {
                alert("任务分配失败！");
            }
        }
    });

}
//商业询盘按钮点击时 跳转到所有商业询盘
function gotoBuiess(flag){
	var admuserid =$('#adminid').val();
	var url='';
	if(flag == 2){
		url='/cbtconsole/messages/preferential?adminid='+admuserid+'&status=4';
	}else if(flag == 4){
		url='/cbtconsole/messages/getBusiess?adminid='+admuserid+'&status=4';
	} else if(flag == 5){
		url='/cbtconsole/apa/questionnaire.html';
	}
	window.open(url);
}

</script>
</head>
<body>
<input id="param_useremail" type="hidden" value="${param.useremail}">
<%String sessionId = request.getSession().getId();
       String userJson = Redis.hget(sessionId, "admuser");
      Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
      int uid = user.getId();%>
<div class="feedbackBkgd"></div>  
<div align="center">
<br>
<div><a href="../order/getOrderInfo.do?showUnpaid=0">返回订单列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="#" onclick="gotoBuiess(2)">批量优惠申请</a>&nbsp;&nbsp;
<%-- <a href="/cbtconsole/website/goodpostage.jsp">邮费折扣申请</a>&nbsp;&nbsp; --%>
<a  href="#" onclick="gotoBuiess(4)">Busiess 询盘</a>&nbsp;&nbsp;
<a  href="#" onclick="gotoBuiess(5)">问卷调查</a></div>
<br>
    <div class="top" style="margin: 0 50px 0 -50px">
    	<%--<span>Busiess 询盘</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
    	<label for="queryFilter">筛选:</label> 
    	<select id="queryFilter" style="height: 24px;">
    		<%--<option value="0">全部</option>--%>
			<option value="1" selected="selected">Busiess 询盘</option>
			<%--<option value="2">服装定制</option>
			<option value="3">问卷调查</option>--%>
    	</select>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label for="state">状态:</label>
		<select id="state" style="height: 24px;">
			<option value="-1" ${state == -1?'selected="selected"':'' }>全部</option>
			<option value="0" ${state == 0?'selected="selected"':'' }>待办</option>
			<option value="1" ${state == 1?'selected="selected"':'' }>完成</option>
			<option value="2" ${state == 2?'selected="selected"':'' }>未布置</option>
		</select>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label for="admName">负责人:</label>
        <select class="easyui-combobox"  name="admName" id="admName">
            <option value="0" selected>全部</option>
            <c:forEach var="adm" items="${ admList}">
                <option value="${adm.id }" ${adminid == adm.id?'selected="selected"':'' } >${adm.admname }</option>
            </c:forEach>
        </select>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		创建开始时间：<input id="startdate" name="startdate" readonly="readonly"
												onfocus="WdatePicker({isShowWeek:true})"
												value="${param.startdate}"
												onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		创建结束时间：<input id="enddate"
												name="enddate" readonly="readonly"
												onfocus="WdatePicker({isShowWeek:true})" value="${param.enddate}"
												onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
		<input type="button" value="查询" onclick="queryByChoice(1,'')">
    </div>
    <div style="height: 10px"></div>
    <div id="feedbackDiv" class="feedbackStyle">
    	<h5>客户反馈</h5><a href="#" class="feedbackClose">X</a>
    	<table>
    		<tr>
    			 <td>用户id：<span id="feedbackUserid" style="width:40%;font-size: 16px;" ></span>
    			 &nbsp;&nbsp;邮箱：<span id="feedbackEmail" style="width:40%;font-size: 16px;" ></span></td>
    		</tr>
    		<tr>
    			 <td>类型：<span id="feedbackType" style="width:40%;font-size: 16px;" ></span></td>
    		</tr>
    		<tr>
    			 <td>内容：<span id="feedbackContent" style="width:80%;font-size: 16px;" ></span></td>
    		</tr>
    		<tr>
    			 <td>留言：<span id="feedbackComments" style="width:80%;font-size: 16px;" ></span></td>
    		</tr>
    		<tr>
    			 <td>搜索Url：<span id="feedbackSearchUrl" style="width:80%;font-size: 16px;" ></span></td>
    		</tr>
    	</table>
	</div>
    <div class="select" style="display: none">
              邮箱:<input type="text" id="selemail" value="${param.email}"/>
        <input type="button" id="selebutton" onclick="selectblacklist()" value="查询"/>
    </div>
    <div class="manager">
        <a href="/cbtconsole/BlackListServlet?action=addblacklist">添加黑名单</a>
        <a onclick="modifyblacklist()" href="javascript:void(0);" >修改黑名单</a>
        <a onclick="delblacklist()" href="javascript:void(0);" >删除黑名单</a>
    </div>
    <div style="height: 10px">
    </div>
	<div>
	     <div>
			<table border="1" class="imagetable">
			     <thead>
			        <tr> 
				        <th style="width:10%;" >公司名称</th> 
				        <th style="width:10%;">邮箱</th> 
				        <th style="width:10%;">订单价格</th> 
				        <th style="width:10%;">需求</th> 
				        <th style="width:5%;">用户id</th> 
				        <th style="width:10%;">电话号码</th> 
				        <th style="width:10%;">创建时间</th>
				        <th style="width:10%;">完结时间</th>
				        <th style="width:10%;">负责人</th>
				        <th style="width:5%;">状态</th>
				        <th style="width:10%;">更改状态</th>
				    </tr>
				 </thead> 
				 <tbody id="busiessbody">
			     </tbody>
			</table> 
		</div>
		<div align="center" id="page"></div>
		<div>
		<input type="hidden" id="adminid" value="${adminid}"/>
		<input type="hidden" id="status" value="${status}"/>
		</div>		
	</div>	
</div>
<script type="text/javascript">
//getBusiess(1);
queryByChoice(1,1);
function getBusiess(pagenum){
	var param_useremail = null;
	var status =$('#status').val();
	if(status==''){status=4;}
	var adminid = <%=uid%>;
	if(document.getElementById("param_useremail")){
		param_useremail = document.getElementById("param_useremail").value;
	}
	 $.post("/cbtconsole/cbt/busiess/getBusiess",{pagenum:pagenum,email:param_useremail,adminid:adminid,status:status},function(result){
		    $("#busiessbody").html("");
		    if(result.code == 0){
		    	var json=eval('('+result.data+')');
			    for(var i in json){   
			    	var userid="";
			    	if(json[i].userid != 0){
			    		userid=json[i].userid;
			    	}
			    	var htm_="<tr style='text-align: center'><td style=\"width:200px;word-break: break-all;\">"+json[i].company+"</td><td><a href=\"mailto:"+json[i].email+"?subject="+json[i].needs+"\">"+json[i].email+"</a></td><td>"+json[i].ordervalue+"</td>";
			    	if(undefined != json[i].customizedId && null != json[i].customizedId && 0 != json[i].customizedId){
			    		htm_ += "<td>"+json[i].needs+"<br/><a target='_blank' href='../website/customized.jsp?customizedId="+json[i].customizedId+"'>查看详情</a></td>";
			    	} else if(undefined != json[i].feedbackId && null != json[i].feedbackId && 0 != json[i].feedbackId){
			    		htm_ += "<td>"+json[i].needs+"<br/><a href='javascript:;' onclick='showFeedBackInfo("+json[i].feedbackId+",\""+ userid + "\",\""+ json[i].email +"\")'>查看详情</a></td>";
			    	}else{
			    		htm_ += "<td>"+json[i].needs+"</td>";
			    	}
			    	htm_ += "<td><a href='/cbtconsole/website/user.jsp?userid="+userid+"' target='_blank'>"+userid+"</a></td><td>"+json[i].userphone+"</td><td>"+json[i].createtime+"</td>";
			    	if(json[i].updatetime == null){
			    		htm_+='<td></td>';
			    	}else{
			    		htm_+='<td>'+timeStamp2String(json[i].updatetime.time)+'</td>';
			    	}
			    	if(json[i].admName !='' && json[i].admName != null){
			    		htm_+="<td>"+json[i].admName+"</td>";
			    	}else{
			    		htm_+='<td class="fenpei"><select id="todo'+i+'" class="todo" onchange="distributionMessages('+i+')"></select>';
			    		htm_+='<input type="hidden" id="email'+i+'" value="'+json[i].email+'"/>';
			    		htm_+='<input type="hidden" id="userid'+i+'" value="'+json[i].userid+'"/>';
			    		htm_+='</td>';
			    	}
			    	if(json[i].status == 0){
			    		htm_+="<td>待办</td>";
			    		htm_+='<td><a href="#" onclick="javascript:changestatus('+json[i].id+')">完结</a></td>';
			    	}else{
			    		htm_+="<td>完成</td>";
				    	htm_+='<td>已完结</td>';
			    	}
			    	htm_+="</tr>"
			    	$("#busiessbody").append(htm_);
			    }
			    getAdminUser();
			    var total=result.total;
			    var page=result.page;
			    var pagesize= result.pagesize;
			    getPage(total,page,pagesize);
		    }		    
	});
 }
 
function queryByChoice(pagenum,type){
    type=$("#queryFilter").val();
	var param_useremail = null;
	var status =$('#status').val();
    var state =$('#state').val();
    var admName =$('#admName').val();
    var startdate =$('#startdate').val();
    var enddate =$('#enddate').val();
	if(status==''){status=4;}
	var adminid = <%=uid%>;
	if(document.getElementById("param_useremail")){
		param_useremail = document.getElementById("param_useremail").value;
	}
	var pam = {
			pagenum:pagenum,
			email:param_useremail,
			adminid:adminid,
			status:status,
			type:type,
			state:state,
			admName:admName,
			startdate:startdate,
			enddate:enddate
	};
	 $.post("/cbtconsole/cbt/busiess/queryByChoice",pam,function(result){
		    $("#busiessbody").html("");
		    if(result.code == 0){
		    	var json=eval('('+result.data+')');
			    for(var i in json){   
			    	var userid="";
			    	if(json[i].userid != 0){
			    		userid=json[i].userid;
			    	}
			    	var htm_="<tr style='text-align: center'><td style=\"width:200px;word-break: break-all;\">"+json[i].company+"</td><td><a href=\"mailto:"+json[i].email+"?subject="+json[i].needs+"\">"+json[i].email+"</a></td><td>"+json[i].ordervalue+"</td>";
			    	if(undefined != json[i].customizedId && null != json[i].customizedId && 0 != json[i].customizedId){
			    		htm_ += "<td>"+json[i].needs+"<br/><a target='_blank' href='../website/customized.jsp?customizedId="+json[i].customizedId+"'>查看详情</a></td>";
			    	} else if(undefined != json[i].feedbackId && null != json[i].feedbackId && 0 != json[i].feedbackId){
			    		htm_ += "<td>"+json[i].needs+"<br/><a href='javascript:;' onclick='showFeedBackInfo("+json[i].feedbackId+",\""+ userid + "\",\""+ json[i].email +"\")'>查看详情</a></td>";
			    	}else{
			    		htm_ += "<td>"+json[i].needs+"</td>";
			    	}
			    	htm_ += "<td><a href='/cbtconsole/website/user.jsp?userid="+userid+"' target='_blank'>"+userid+"</a></td><td>"+json[i].userphone+"</td><td>"+json[i].createtime+"</td>";
			    	if(json[i].updatetime == null){
			    		htm_+='<td></td>';
			    	}else{
			    		htm_+='<td>'+timeStamp2String(json[i].updatetime.time)+'</td>';
			    	}
			    	if(json[i].admName !='' && json[i].admName != null){
			    		htm_+="<td>"+json[i].admName+"</td>";
			    	}else{
			    		htm_+='<td class="fenpei"><select id="todo'+i+'" class="todo" onchange="distributionMessages('+i+')"></select>';
			    		htm_+='<input type="hidden" id="email'+i+'" value="'+json[i].email+'"/>';
			    		htm_+='<input type="hidden" id="userid'+i+'" value="'+json[i].userid+'"/>';
			    		htm_+='</td>';
			    	}
			    	if(json[i].status == 0){
			    		htm_+="<td>待办</td>";
			    		htm_+='<td><a href="#" onclick="javascript:changestatus('+json[i].id+')">完结</a></td>';
			    	}else{
			    		htm_+="<td>完成</td>";
				    	htm_+='<td>已完结</td>';
			    	}
			    	htm_+="</tr>"
			    	$("#busiessbody").append(htm_);
			    }
			    getAdminUser();
			    var total=result.total;
			    var page=result.page;
			    var pagesize= result.pagesize;
			    genPage(total,page,pagesize,type);
		    }		    
	});
 }
 
function timeStamp2String(time){ //时间格式转化
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

$(function(){
	$("#queryFilter").change(function(){
		var type = $(this).val();
		queryByChoice(1,type);
	});
	$(".feedbackClose").click(function(){
		closeFeedback();
		return false;
	});
});

function showFeedBackInfo(feedbackId,userid,email){
	$.ajax({
		type : "post",
		url : "/cbtconsole/cbt/feedback/queryFeedbackById.do",
		datatype : "json",
		data : {"id":feedbackId},
		success : function(data) {
			if (data.ok) {
				var resault = data.data;
				if(userid == null || userid == ""){
					$("#feedbackUserid").text("");
				} else{
					$("#feedbackUserid").text(userid);
				}
				if(email == null || email == ""){
					$("#feedbackEmail").text("");
				} else{
					$("#feedbackEmail").text(email);
				}
				if(resault.type == 1){
					$("#feedbackType").text("没有找到想要的");
				} else if(resault.type == 2){
					$("#feedbackType").text("价格太高");
				} else{
					$("#feedbackType").text("");
				}
				if(resault.content == null){
					$("#feedbackContent").text("");
				} else{
					$("#feedbackContent").text(decodeURI(resault.content,"UTF-8"));
				}	
				if(resault.otherComment == null){
					$("#feedbackComments").text("");
				} else{
					$("#feedbackComments").text(decodeURI(resault.otherComment,"UTF-8"));
				}
				var showUrl = "<a target ='_blank' href='"+resault.searchUrl+"'>"+resault.searchUrl+"</a>" ;
				$("#feedbackSearchUrl").empty();
				$("#feedbackSearchUrl").append(showUrl);
				$(".feedbackBkgd").show();
				$("#feedbackDiv").show();
			} else {
				alert(data.message);
			}
		},
		error : function(msg) {
			alert("连接失败，请重试！");
		}
	});
	return false;
}

function closeFeedback(){
	$(".feedbackBkgd").hide();
	$("#feedbackDiv").hide();
}

</script>
</body>
</html>