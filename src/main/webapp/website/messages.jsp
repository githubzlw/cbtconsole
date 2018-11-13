<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<!DOCTYPE html>
<html>
<head>
<title>消息队列</title>
<meta http-equiv=Content-Type content="text/html; charset=utf-8">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">

<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
</head>
<body>
<div id="wrap">
	<div id="main" class="wide-bg-dark clearfix">
		<section class="page-top">
			<div class="container col-media-main">
				<div class="logo">系统消息</div>
				<nav class="menu">
				<div class="megAll" id="meg">
				<%String sessionId = request.getSession().getId();
	              String userJson = Redis.hget(sessionId, "admuser");
	             Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
	             int uid = user.getId();%>
				<p>客户留言 （未处理数量：<span id="propagemessage" class="btn btn-success btnto"> </span>，
				 最近1周所有留言：<span id="propagemessage1" class="btn btn-warning btnto"> </span>）
				 <input type="hidden" id="type" value="propagemessage"></p>
				<p>购物车营销  （未处理数量：<span id="shopcarmarket" class="btn btn-success btnto"> </span>，
				 最近1周值得营销的数量：<span id="shopcarmarket1" class="btn btn-warning btnto"> </span>）
				 <input type="hidden" id="type" value="shopcarmarket"></p>
				<p>商业询盘 （<span id="busquer">未布置数量：<span id="businquiries" class="btn btn-primary btnto"> </span>，</span> 
				未处理数量：<span id="businquiries1" class="btn btn-success btnto"> </span>，
				      所有商业询盘：<span id="businquiries2" class="btn btn-warning btnto"> </span>）
				      <input type="hidden" id="type" value="businquiries"></p>
				<p>批量优惠申请 （<span id="bat">未布置数量：<span id="batapply" class="btn btn-primary btnto"> </span>，
				</span> 未处理数量：<span id="batapply1" class="btn btn-success btnto"> </span>，
				 所有批量优惠申请：<span id="batapply2" class="btn btn-warning btnto"> </span>）
				 <input type="hidden" id="type" value="batapply"></p>
				<p>客户投诉 （未处理数量：<span id="refundscom" class="btn btn-success btnto"> </span>， 
				所有投诉：<span id="refundscom1" class="btn btn-warning btnto"> </span>）
				<input type="hidden" id="type" value="refundscom"></p>
				<p id="showorder" style="display:none;" >订单 （未分配数量:<span id="ordermeg" class="btn btn-primary btnto"> </span>）
				<input type="hidden" id="type" value="ordermeg"></p>
				</div>
				</nav>
				<form class="form-search">
					<center><label> userid:</label>
					  <input type="text" class="input-medium search-query " id="userId">
					  <button type="button" class="btn searchbtn">查询</button>
				  </center>
				</form>
				<form id="messageform"  action="/cbtconsole/messages/showMessages" method="post">
				<nav class="menu">
					 <div onselectstart="return false" style="padding:15px;">
                                <table id="settingall" class="imagetable">
                                    <thead>
                                        <tr>
                                            <th width="10%">序号</th>
                                            <th width="10%">消息类型</th>
                                            <th width="10%">用户id</th>
                                            <th width="10%">标题</th>
                                            <th width="10%">内容</th>
                                            <th width="10%">时间</th>
                                            <th width="10%">邮箱</th>
                                            <th width="10%">状态</th>
                                            <th >任务分配</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                	</tbody>
								</table>
							</div>
							<div style="margin:0px 300px;float;">
							<ul class="pagination" id="pageshow">
							</ul>
							<ul class="pagination navbar-right" id="gopage">
							</ul>
							</div>
							  <input type="hidden" name="currentPage" id="currentPage" value=""/>
				  </nav>
				  
				</form>
			</div>
	    </section>
<footer>
	<div class="wide-main col-media-main page-footer">
	</div>
</footer>
</div>
<script  type="text/javascript">
$(function(){  
	//查询消息列表
	gotoPage(1);  //分页显示
	getMessageNum(); //获取各种消息数量
	$('.searchbtn').click(function(){
		gotoPage(1);
	});
	$('.btnto').click(function(){
		var type = $(this).parent().find('#type').val();
		var url="";
		if(type=="batapply"){
			url="/cbtconsole/website/preferential.jsp";
			window.open(url);
		}else if(type=="propagemessage"){
			url="/cbtconsole/customerServlet?action=findAll&className=GuestBookServlet";
			window.open(url);
		}else if(type=="businquiries"){
			url="/cbtconsole/website/busiesslist.jsp";
			window.open(url);
		}else if(type=="shopcarmarket"){
			url="/cbtconsole/website/shoppingCartManagement.jsp";
			window.open(url);
		}else if(type=="refundscom"){
			url="/cbtconsole/refundss/getAllRefundApply";
			window.open(url);
		}else{
			url="/cbtconsole/WebsiteServlet?action=getOrderws&className=OrderwsServlet&showUnpaid=0";
			window.open(url);
		}
	});
}); 
function gotoPage(currentPage){
	$("#settingall tbody").html('');
	$('#currentPage').val(currentPage);
	var userId = $('#userId').val();
	if(userId == ''){
		userId = -1;
	}
	var admuserid = <%=uid%>;
	jQuery.ajax({
		dataType:"json",
        url:"/cbtconsole/messages/showMessages",
        data:{"currentPage":currentPage,
        	"adminid":admuserid,
        	"userid":userId},
        type:"post",
        success:function(data){
        	if(data.ok){
                var page = data.data;
                var messagesList = page.list;
                var currentPage = page.currentPage;
                var countPage  = page.countPage;
                var countRecord =page.countRecord;
                var messages = new Object();
                for(var i=0;i<messagesList.length;i++){
                	messages = messagesList[i];
                	var time =timeStamp2String(messages.createTime);
                   var htm_ ='';
                   htm_+='<tr>';
                   htm_+='<td >'+(i+1)            +'</td> ';
                   htm_+='<td ><spqn class="btn btn-primary" style="text-decoration:underline;" onclick="gotourl('+i+')">'+messages.typeName+'</span>';
                   htm_+='<input type="hidden" class="type'+i+'" value="'+messages.type+'"/>';
                   htm_+='</td>';
                   htm_+='<td >'+messages.userid+'</td> ';
                   htm_+='<td >'+messages.title+'</td>';
                   htm_+='<td >'+messages.content+'</td> ';
                   htm_+='<td >'+time+'</td>';
                   htm_+='<td >'+messages.hrefName+'</td>';
                   if(messages.isDelete == 0){
                	   htm_ += '<td >新</td> ';
                   }else{
                	   htm_ += '<td >已完成</td> ';
                   }
                   htm_+='<td class="fenpei"><input type="hidden" id="adminid" value="'+messages.adminid+'"/>';
                   htm_+='<input type="hidden" id="isDelete'+i+'" value="'+messages.isDelete+'"/>';
                   htm_+='<input type="hidden" id="id'+i+'" value="'+messages.id+'"/>';
                   htm_+='<input type="hidden" id="hrefName'+i+'" value="'+messages.hrefName+'"/>';
                   htm_+='<input type="hidden" id="mytype'+i+'" value="'+messages.type+'"/>';
                   htm_+='<input type="hidden" id="userid'+i+'" value="'+messages.userid+'"/>';
                   htm_+='<select id="todo" class="todo'+i+'" onchange="distributionMessages('+i+')"></select></td> ';
                   htm_+='</tr> ';                           
                    $("#settingall").append(htm_);
                }//消息分页
                var html_='共'+countRecord+'条记录，当前第'+currentPage+'/'+countPage+'页，每页显示20行.';
                var countPageHtml ='';
                if(countPage == 1){
                	countPageHtml=' <li><a href="#">当前只有一页</a></li>';
                }else if(currentPage==1){
                	countPageHtml='<li><a href="#" onclick="javascript:gotoPage('+(currentPage+1)+')">下一页</a></li>';
                }else if(countPage==currentPage){
                	countPageHtml ='<li><a href="#" onclick="javascript:gotoPage('+(currentPage-1)+')">上一页</a></li>';
                }else{
                	countPageHtml ='<li><a href="#" onclick="javascript:gotoPage('+(currentPage-1)+')">上一页</a></li>';
                	countPageHtml +='<li><a href="#" onclick="javascript:gotoPage('+(currentPage+1)+')">下一页</a></li>';
                }
                $('#pageshow').html(html_);
                $('#gopage').html(countPageHtml);
                getAdminUser();
                getMessageNum();
            }else{
				 alert("消息列表查询失败！");
			}
        },
    	error:function(e){
    		alert("消息列表查询失败！");
    	}
    });
}
function deleteMessage(id){
	var url= "/cbtconsole/messages/deleteMessages";
	jQuery.ajax({
        url:url,
        data:{"id":id},
        type:"post",
        success:function(data, status){
        	if(data.ok){
        		alert(data.message);
        		var currentPage = $('#currentPage').val();
        		gotoPage(currentPage);
            }else{
				 alert(data.message);
			}
        },
    	error:function(e){
    		alert("状态更新失败！");
    	}
    });
}
function gotourl(i){
	var type =$('.type'+i).val();
	var url="";
	if(type=="batapply"){
		url="/cbtconsole/website/preferential.jsp";
		window.open(url);
	}else if(type=="propagemessage"){
		url="/cbtconsole/customerServlet?action=findAll&className=GuestBookServlet";
		window.open(url);
	}else if(type=="businquiries"){
		url="/cbtconsole/website/busiesslist.jsp";
		window.open(url);
	}else if(type=="shopcarmarket"){
		url="/cbtconsole/website/shoppingCartManagement.jsp";
		window.open(url);
	}else{
		alert("到其他页面！");
	}
	
}
function getMessageNum(){
	var url= "/cbtconsole/messages/selectBasicMessages";
	var mydate = new Date();
	var rYear =mydate.getFullYear(); 
	var rMonth = mydate.getMonth()+1;
	var rDay = mydate.getDate();
	var timeTo = rYear+'-'+ rMonth+'-'+rDay+' 23:59:59';
	mydate.setDate(mydate.getDate() -7);
	rDay = mydate.getDate();
	var timeFrom = rYear+'-'+ rMonth+'-'+rDay+' 00:00:00';
	var admuserid = <%=uid%>;
	jQuery.ajax({
        url:url,
        data:{"timeFrom":timeFrom,
        	"timeTo":timeTo,
        	"adminid":admuserid},
        type:"post",
        success:function(data, status){
        	if(data.ok){
        		var messagesCountList = data.data;
        		var messagesCountVo = new Object();
        		for(var i=0;i<messagesCountList.length;i++){
        			messagesCountVo = messagesCountList[i];
        			if(messagesCountVo.type=='propagemessage'){
        				$('#propagemessage').html(messagesCountVo.noDeleteCount);
        				$('#propagemessage1').html(messagesCountVo.countAll);
        			}else if(messagesCountVo.type=='shopcarmarket'){
        				$('#shopcarmarket').html(messagesCountVo.noDeleteCount);
        				$('#shopcarmarket1').html(messagesCountVo.countAll);
        			}else
        			if(messagesCountVo.type=='businquiries'){
        				if(admuserid ==1){
        					$('#businquiries').html(messagesCountVo.noArrgCount);
            				$('#businquiries1').html(messagesCountVo.noDeleteCount);
            				$('#businquiries2').html(messagesCountVo.countAll);
        				}else{
        					$('#busquer').css("display","none");
        					$('#businquiries1').html(messagesCountVo.noDeleteCount);
            				$('#businquiries2').html(messagesCountVo.countAll);
        				}
        			}else
        			if(messagesCountVo.type=='batapply'){
        				if(admuserid ==1){
        				$('#batapply').html(messagesCountVo.noArrgCount);
        				$('#batapply1').html(messagesCountVo.noDeleteCount);
        				$('#batapply2').html(messagesCountVo.countAll);
        				}else{
        					$('#bat').css("display","none");
            				$('#batapply1').html(messagesCountVo.noDeleteCount);
            				$('#batapply2').html(messagesCountVo.countAll);
        				}
        			}else if(messagesCountVo.type=='refundscom'){
        				$('#refundscom').html(messagesCountVo.noDeleteCount);
        				$('#refundscom1').html(messagesCountVo.countAll);
        			}
        			if(admuserid==1 && messagesCountVo.type=='ordermeg'){
        				$('#ordermeg').html(messagesCountVo.noArrgCount);
        				$('#showorder').css('display','block');
        			}
        		}
            }else{
				 alert(data.message);
			}
        },
    	error:function(e){
    		alert("消息数量信息查询失败！");
    	}
    });
}
function getAdminUser(){
	var url= "/cbtconsole/messages/selectAdminUser";
	var adminid = <%=uid%>;
	jQuery.ajax({
        url:url,
        data:{},
        type:"post",
        success:function(data, status){
        	if(data.ok){
        		var admUserList = data.data;
        		var admUser =new Object();
        		var htm_='';
        		//fenpei"><input type="hidden" id="adminid" value="'+messages.adminid+'"/><select id="todo"></select></td>
        		$('.fenpei').each(function(){
        			if(adminid != 1){
        				$(this).find('#todo').attr("disabled","disabled");
        			}
        			htm_='<option value="1">未分配</option>';
        			var admid= $(this).find('#adminid').val();
        			var isDelete= $(this).find('#isDelete').val();
        			for(var i=0;i<admUserList.length;i++){
        				admUser = admUserList[i];
        				if(admid == admUser.id){
        					htm_ +='<option value="'+admUser.id+'" selected>'+admUser.admname+'</option>';
        				}else{
        					htm_ +='<option value="'+admUser.id+'">'+admUser.admname+'</option>';
        				}
        			}
        			$(this).find("#todo").append(htm_);

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
function distributionMessages(i){
		var adminid = $('.todo'+i).find("option:selected").val();
		var adminName = $('.todo'+i).find("option:selected").text();
		var id=$('#id'+i).val();
		var hrefName =$('#hrefName'+i).val();
		var userid =$('#userid'+i).val();
		var isdelete = $('#isDelete'+i).val();
		var type=$('#mytype'+i).val();
		if(isdelete==1){
			alert("消息已处理，无需分配!");
			return false;
		}else{
			var url= "/cbtconsole/messages/distributionMessages";
			jQuery.ajax({
		        url:url,
		        data:{"id":id,
		        	"adminid":adminid,
		        	"hrefName":hrefName,
		        	"userid":userid,
		        	"type":type},
		        type:"post",
		        success:function(data, status){
		        	if(data.ok){
		        		var currentPage = $('#currentPage').val();
		        		gotoPage(currentPage);
		            }else{
						 alert(data.message);
					}
		        },
		    	error:function(e){
		    		alert("任务分配失败！");
		    	}
			 });
		}
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
</script>
</body>
</html>

