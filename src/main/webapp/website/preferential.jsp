<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<style type="text/css">
.email{
    width: 140px;
    word-break: break-all;
 }
 .lazyImg{
 	height: 50px;
 	width: 50px;
	cursor: pointer;
 }
</style>
<title>批量优惠申请</title>
<%String sessionId = request.getSession().getId();
       String userJson = Redis.hget(sessionId, "admuser");
      Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
      int uid = user.getId();
      String userName = user.getAdmName();
      %>
</head>
<script type="text/javascript">
	$(function(){
		 $("img.lazyImg").lazyload({
			 threshold : 200,
			 effect : "fadeIn"
		 });
	})

	//处理返回结果
	  var urls = window.location.href;
	  if(urls.indexOf("?res=")>0){
		var res=urls.split("=")[1];
		if(res==0){
			alert("Failed");
		}else{
			alert("操作成功");
		}
		
	} 

    var admins = "";      //用于保存后台管理员列表的select 的 option值
	//查询并初始化  后台管理员列表
	function selectAdmin(){
	jQuery.ajax({
        url:"/cbtconsole/messages/selectAdminUser",
        data:{"type" : "sell"},
        type:"post",
        async:false,
        success:function(data, status){
        	if(data.ok){
        		var admUserList = data.data;
        		var admUser =new Object();
        		admins='<option value="0">未分配</option>';
       			var admid= $(this).find('#adminid').val();
       			var isDelete= $(this).find('#isDelete').val();
       			for(var i=0;i<admUserList.length;i++){
       				admUser = admUserList[i];
       				admins +='<option value="'+admUser.id+'">'+admUser.admname+'</option>';
       			}
            }else{
				 alert(data.message);
			}
        },
    	error:function(e){
    		//alert("客户列表查询失败！");
    	}
    });
    }
	
	var page = 1;
	function fn(val){
		selectAdmin();
		var type = $("#type").val();
		var userid = $("#userid").val();
		var cdate = $("#cdate").val();
		var hdate = $("#hdate").val();
		var adminid =<%=uid%>;
		var adminName ='<%=userName%>';
		var param_useremail = null;
		 if(val==2){
			  page = page+1;
			  if(count<page){
				  return;
			  }
		  }else if(val == 3){
			  page = page-1;
			  if(0>=page){
				  page = 1;
				  return;
			  }
		  }else if(val ==1){
			  $("#type").val(0);			  
			  type=0;
		  }
		
		if(document.getElementById("param_useremail")){
			 param_useremail = document.getElementById("param_useremail").value;
		}
		$.post("/cbtconsole/WebsiteServlet",
  			{action:'getPreferentials',className:'PreferentialwServlet',
			userid:userid,type:type,page:page,cdate:cdate,hdate:hdate,email:param_useremail,
			adminid:adminid},
  			function(res){
  				var json = eval(res)[0];
  				var pre = json.pre;
  				$("#rows").html(pre.length);
  				 $("#table tr:gt(0)").remove();
  				 var uid = "0";
  				 var j = 1;
  				 var type1 = $("#type").val();
  				 var index = 0;
  				 var itemID = "";
  				 for (var i = 0; i < pre.length; i++) {
  					 var preferential = pre[i].preferential;
  	  				 var userid = preferential.userid;
  	  				 if(userid == 0){userid = preferential.sessionid;}
  					 if(uid != userid || preferential.itemId != itemID){
  						 index = i;
  						 $("#table tr:eq("+(j-1)+")").after("<tr name='tr_' style='background-color: #F0FFFF'></tr>");
  		  				  $("#table tr:eq("+j+")").append("<td colspan='3'>"+ (preferential.userid==0?"sessionid":"userid")+":<em>"+userid+"</em></td>");
  		  				  $("#table tr:eq("+j+") td:eq(0)").after("<td colspan='5'>Email:<em>"+preferential.email+"</em></td>");
  		  				  //没有负责人
  		  				  var aa = $("#table tr:eq("+j+") td:eq(1)");
						  		  				  
  		  				  if(preferential.admname ==''){//<td class="fenpei"><select id="todo" class="todo'+i+'" onchange="distributionMessages('+i+')"></select>
  		  				    var html_='';
  		  					html_+='<input type="hidden" id="email'+i+'" value="'+preferential.email+'"/>';
			    			html_+='<input type="hidden" id="userid'+i+'" value="'+preferential.userid+'"/>';
  		  					$("#table tr:eq("+j+") td:eq(1)").after('<td colspan="3" class="fenpei">负责人：<select id="todo'+i+'" class="todo'+preferential.userid+'" onchange="distributionMessages(this,\''+preferential.userid+'\',\''+preferential.email+'\')">'+admins+'</select>'+html_+'</td>');
  		  				   }else if(adminName!='Ling' && adminName!='emmaxie' && adminName!='admin1' &&preferential.admname !=''){
  		  					  //有负责人，且当前用户不为Ling
  		  					$("#table tr:eq("+j+") td:eq(1)").after('<td colspan="3">负责人:<strong>'+preferential.admname+'</strong></td>');
  		  				  }else{
 		  					  //有负责人，当前用户为ling
  		  				    var html_='';
		  					var older = ">"+preferential.admname+"</option>";
		  					var checkedItem = "selected='selected'>"+preferential.admname+"</option>";
		  					var adm = admins.replace(older,checkedItem);
  		  					html_+='<input type="hidden" id="email'+i+'" value="'+preferential.email+'"/>';
			    			html_+='<input type="hidden" id="userid'+i+'" value="'+preferential.userid+'"/>';
  		  					$("#table tr:eq("+j+") td:eq(1)").after('<td colspan="3" class="fenpei">负责人：<select id="todo'+i+'" class="todo'+preferential.userid+'" onchange="distributionMessages(this,\''+preferential.userid+'\',\''+preferential.email+'\')">'+adm+'</select>'+html_+'</td>');
  		  				  }
  		  				//选中的值
  		  				if (preferential.admname != '') {
  		  					$("#todo" +i+ " option[flag='"+ preferential.admname + "']").attr('selected', 'selected');
  		  				}
  		  				  $("#table tr:eq("+j+") td:eq(2)").after("<td colspan='6'>country:"+preferential.country+"</td>");
  		  				  if(type1 == 0){
  		  					 $("#table tr:eq("+j+") td:eq(3)").append('<input type="button" onclick="fnSendemail(this,'+preferential.userid+',\''+preferential.sessionid+'\',\''+preferential.username+'\',\''+i+'\')" value="确认并发送邮件"/>');
  		  				//	$("#table tr:eq("+j+") td:eq(3)").append('<input type="button" onclick="openWinow()" value="确认并发送邮件"/>');
  		  				  }
  						 j += 1;
  					 }
  				  $("#table tr:eq("+(j-1)+")").after("<tr index='"+index+"'></tr>");
  				  $("#table tr:eq("+j+")").append("<td>"+preferential.id+"</td>");
  				  var goods_types_split = preferential.goods_types.split(",");
  				  var goods_types = "";
  				  for (var q = 0; q < goods_types_split.length; q++) {
  					goods_types += goods_types_split[q].split("@")[0]+"&nbsp;&nbsp;";
				  }
  				  $("#table tr:eq("+j+") td:eq(0)").after("<td><a target='_blank' href="+pre[i].url+" ><img src='"+pre[i].img+"' class='lazyImg' title='"+pre[i].name+"' data-original='"+pre[i].img+"' ></a><br>"+goods_types+"</td>");
  				  $("#table tr:eq("+j+") td:eq(1)").after("<td>"+pre[i].mOrder+"</td>");
  				  $("#table tr:eq("+j+") td:eq(2)").after("<td>"+preferential.number+"</td>");
  				  $("#table tr:eq("+j+") td:eq(3)").after("<td>"+preferential.sprice+"</td>");
  				  $("#table tr:eq("+j+") td:eq(4)").after("<td>"+(preferential.state==0?"未处理":"已处理")+"</td>");
  				  $("#table tr:eq("+j+") td:eq(5)").after("<td style=\"word-break:break-all\">"+preferential.note+"</td>");
  				  $("#table tr:eq("+j+") td:eq(6)").after("<td>"+preferential.createtime+"</td>");
  				  $("#table tr:eq("+j+") td:eq(7)").after("<td>"+preferential.handletime+"</td>");
  				  $("#table tr:eq("+j+") td:eq(8)").after("<td>"+preferential.effectivetime+"</td>");
  				  var eend = preferential.effectiveend;
  				  $("#table tr:eq("+j+") td:eq(9)").after("<td>"+(eend == 1 ? "失效":"有效")+"</td>");
  				  $("#table tr:eq("+j+") td:eq(10)").after("<td><div style='width:150px;max-height:130px;overflow-y:scroll;'></div></td>");
  				  var paInteracteds = pre[i].paInteracteds;
  				  var pai = paInteracteds.length;
  				  var sendemail = 1;
  				  var price_ = "";
  				  if(pai>0){
  					for(var k = 0; k < pai; k++){
  						if(preferential.id==paInteracteds[k].paid){
		  					sendemail = paInteracteds[0].type;
  	  					  var type = paInteracteds[k].type == 1 ? "客户":"系统";
  	  					  $("#table tr:eq("+j+") td:eq(11) div").append(type+":$"+paInteracteds[k].price+"&nbsp;"+paInteracteds[k].quantity+"&nbsp;"+paInteracteds[k].createtime+"</br>");
  						}
  	  					 /*if(k == 0){
  	  	  					  price_ = paInteracteds[k].price;
  	  					 }*/
  	  				}
  					price_ = paInteracteds[pai-1].price;
  				  }else{
  					price_ = preferential.price;
  				  }
  				  $("#table tr:eq("+j+") td:eq(11)").after("<td style='color:"+(sendemail == 1?"":"red")+"'>"+(sendemail == 1 ? "未回复":"已回复")+"</td>");
  				  /*$("#table tr:eq("+j+") td:eq(12)").after("<td class='email'>"+preferential.email+"</td>");*/
  				  $("#table tr:eq("+j+") td:eq(12)").after("<td>"+preferential.country+"</td>");
  				  if(preferential.state == 0 && eend == 0){
  					 $("#table tr:eq("+j+") td:eq(13)").after("<td><input type='hidden' value='"+preferential.id+"'/><input type='hidden' value='"+preferential.goodsid+"'/><input type='hidden' value='"+preferential.sprice+"'/>"+
  							 "<input type='hidden' value='"+pre[i].name+"'/>"+price_+"<input type='text'  id='"+("price_"+preferential.id)+"' style='width:80px;' onblur='updatePrice(this.value,"+preferential.id+","+preferential.itemId+","+preferential.number+","+price_+")'/><input type='hidden' value='"+eend+"'/></td>");
  				  }else{
  					 $("#table tr:eq("+j+") td:eq(13)").after("<td>"+price_+"</td>");
  				  }
  				  $("#table tr:eq("+j+") td:eq(14)").after("<td>"+preferential.currency+"</td>");
  				 	uid = userid;
  				 	itemID = preferential.itemId;
  					j += 1;
  				 }
  				  count = Math.ceil(json.count / 40);
  				  $("#count").html(count);
  				  $("#counto").html(json.count);
  				  page = parseInt(json.page, 0);
  				  $("#page").html(page);
  			} 
  			);
	}
	
	function updatePrice(price, id ,itemId,number,sPrice){
		if(isNaN(price)){
			alert("请输入正确的价格");
			return ;
		}
		$.ajax({
			url: "/cbtconsole/WebsiteServlet?className=PreferentialwServlet&action=updatePriceById&price="+price+"&id="+id+"&itemId="+itemId+"&number="+number+"&sPrice="+sPrice,
			type:"post",
			dataType:"text",
			success:function(data){
				if(data=0){
					alert("修改失败");
				}
			},
			error:function(){
				alert("Exception");
			}
		}); 
	}
	
	function fnSendemail(val,userid,sessionid,username, index){
		//保存价格，发送邮件
		//var tr_l = $("#table tr").length;
		//var this_l = $(val).parent().parent().index();
		var ids = "";
		var prices = "";
		var sprice = "";
		var name = "";
		var number = "";
		var gids = "";
		var eend = "";//是否有效
		var email = $(val).parent().parent().find(" td:eq(1) em:eq(0)").html();
		var type = 0;//是否存在已处理的申请
		
		var img = "";
		var currency = "";
		var question = "";
		$("#table tr[index='"+index+"']").each(function() {
			var tr_i = $(this);
			var td_ = tr_i.find("td").eq(14);
			var td_id = td_.find("input:eq(0)").val();
			var td_gid = td_.find("input:eq(1)").val();
			var td_sprice = td_.find("input:eq(2)").val();
			var td_name = td_.find("input:eq(3)").val();
			var td_eend = td_.find("input:eq(5)").val();
			var td_price = td_.find("input:eq(4)").val();
			var td_number = tr_i.find("td").eq(3).html();
			if(td_price != "" && typeof(td_price) != "undefined" ){
				ids += td_id + "@";
				gids += td_gid + "@";
				prices += td_price + "@";
				name += td_name + "@.@";
				sprice += td_sprice + "@";	
				number += td_number + "@";
				eend += td_eend + "@";
				
				img += tr_i.find("td:eq(1) img").attr("src") + "@";
				currency += tr_i.find("td:eq(15)").text() + "@";
				
				var q = tr_i.find("td:eq(6)").text();
				if(q != "" && typeof(q) != "undefined" ){
					question += q + "@";
				}
			}
		});
		/* for(var i = this_l; tr_l > i; i++){
			var tr_i = $("#table tr").eq(i);
			var name_tr = tr_i.attr("name");
			if(typeof(name_tr) != "undefined"){
				continue;
			}
			var td_ = tr_i.find("td").eq(15);
			var td_id = td_.find("input:eq(0)").val();
			var td_gid = td_.find("input:eq(1)").val();
			var td_sprice = td_.find("input:eq(2)").val();
			var td_name = td_.find("input:eq(3)").val();
			var td_eend = td_.find("input:eq(5)").val();
			var td_price = td_.find("input:eq(4)").val();
			var td_number = tr_i.find("td").eq(3).html();
			if(td_price != "" && typeof(td_price) != "undefined" ){
					ids += td_id + "@";
					gids += td_gid + "@";
					prices += td_price + "@";
					name += td_name + "@.@";
					sprice += td_sprice + "@";	
					number += td_number + "@";
					eend += td_eend + "@";
					
					img += tr_i.find("td:eq(1) img").attr("src") + "@";
					currency += tr_i.find("td:eq(16)").text() + "@";
					
					var q = tr_i.find("td:eq(6)").text();
					if(q != "" && typeof(q) != "undefined" ){
						question += q + "@";
					}
			}
		} */
		
		if(ids == ""){
			alert("请输入对应按钮所需的价格，再点击");
			return;
		}
		
		$("#myModal").modal('show');
		
		//在弹出的窗体中设置值
		$("#email_").val(email);
		$("#ids_").val(ids);
		$("#gids_").val(gids);
		$("#prices_").val(prices);
		$("#sprices_").val(sprice);
		$("#title_").val(name);
		$("#number_").val(number);
		$("#userId_").val(userid);
		$("#eend_").val(eend);
		$("#sessionid_").val(sessionid);
		$("#username_").val(username);
		
		$("#img_").val(img);
		$("#currency_").val(currency);
		$("#question_").val(question);
	}
	
	
	function realSend(){
// 		form1.submit();
// 		      		<input type="hidden" id="email_" name="email"/>
// 		      		<input type="hidden" id="ids_" name="ids"/>
// 		      		<input type="hidden" id="gids_" name="gids"/>
// 		      		<input type="hidden" id="prices_" name="prices"/>
// 		      		<input type="hidden" id="sprices_" name="sprices"/>
// 		      		<input type="hidden" id="title_" name="title"/>
// 		      		<input type="hidden" id="number_" name="number"/>
// 		      		<input type="hidden" id="userId_" name="userid"/>
// 		      		<input type="hidden" id="confirm_" name="confirm" value="0"/>
// 		      		<input type="hidden" id="eend_" name="eend"/>
// 		      		<input type="hidden" id="sessionid_" name="sessionid"/>
// 		      		<input type="hidden" id="username_" name="username"/>
		      		
// 		      		<input type="hidden" id="img_" name="img"/>
// 		      		<input type="hidden" id="currency_" name="currency"/>
// 		      		<input type="hidden" id="question_" name="question"/>
		      				      		
// 		      		<textarea id="emailContent" name="content" rows="10" cols="75"></textarea>
        var email=$("#email_").val();
        var ids=$("#ids_").val();
        var gids=$("#gids_").val();
        var prices=$("#prices_").val();
        var sprices=$("#sprices_").val();
        var title=$("#title_").val();
        var number=$("#number_").val();
        var userId=$("#userId_").val();
        var confirm=$("#confirm_").val();
        var eend=$("#eend_").val();
        var sessionid=$("#sessionid_").val();
        var username=$("#username_").val();
        var img=$("#img_").val();
        var currency=$("#currency_").val();
        var question=$("#question_").val();
        var emailContent=$("#emailContent").val();
		$.ajax({
			url : "/cbtconsole/WebsiteServlet",
			data:{
	        	  "action":"savePainteracted",
	        	  "className":"PreferentialwServlet",
	        		"email":email,
		        	  "ids":ids,
		        		  "gids":gids,
			        	  "prices":prices,
			        		  "sprices":sprices,
				        	  "title":title,
				        		  "number":number,
					        	  "userid":userId,
					        		  "confirm":confirm,
						        	  "eend":eend,
						        		  "sessionid":sessionid,
							        	  "username":username,
							        		  "img":img,
								        	  "currency":currency,
								        		  "question":question,
									        	  "emailContent":emailContent
	        	  },
			type : "post",
			success :function(data){
				if(data>0){
					alert("操作成功");
					location.reload();
				}else{
					alert("Failed");
				}
			}
		});
	}
	 
	function fnCancel(val,uid){
		var cancel_reason = $(val).next("textarea").html();
		$.post("/cbtconsole/WebsiteServlet",
	  			{action:'cancelPainteracted',className:'PreferentialwServlet',pid:uid,cancel_reason:cancel_reason},
	  			function(res){
	  				if(res>0){
	  					alert("取消成功");
	  				}else{
	  					alert("取消失败");
	  				}
		  	});
	}
	
	//商业询盘按钮点击时 跳转到所有商业询盘
	function gotoBuiess(flag){
		var admuserid =<%=uid%>;
		var url='';
		if(flag == 2){
			url='/cbtconsole/messages/preferential?adminid='+admuserid+'&type=4';
		}else if(flag == 4){
			url='/cbtconsole/messages/getBusiess?adminid='+admuserid+'&status=4';
		}
		window.open(url);
	}
	
	//客服分配
	function distributionMessages(val,userid,email){
		var adminid = $(val).find("option:selected").val();
		var adminName = $(val).find("option:selected").text();
		if(userid==''){userid=0;}
		var url= "/cbtconsole/messages/distributionMessages";
		jQuery.ajax({
	        url:url,
	        data:{"userid":userid,
	        	"adminid":adminid,
	        	"hrefName":email,
	        	"adminName":adminName},
	        type:"post",
	        success:function(data, status){
	        	if(data.ok){
	        		$(val).css("border-color","green");
	        		$(val).after("<span style='color:green'>更新成功</span>");//alert($('.todo'+i).next('span').eq(0).text());
	        		$(".todo" + userid).val(adminid);
	        		//$('.todo'+i).find("span").eq(0).show().delay(3000).hide();
	            }else{
					 alert("任务分配失败！");
				}
	        },
	    	error:function(e){
	    		alert("任务分配失败！");
	    	}
		 });
	}
	
</script>
<body >
<input id="param_useremail" type="hidden" value="${param.useremail }">
<div align="center">
<div><a href="../order/getOrderInfo.do?showUnpaid=0">返回订单列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="#" onclick="gotoBuiess(2)">批量优惠申请</a>&nbsp;&nbsp;
<%-- <a href="goodpostage.jsp">邮费折扣申请</a>&nbsp;&nbsp; --%>
<a  href="#" onclick="gotoBuiess(4)">Busiess 询盘</a></div>
<h3>批量优惠申请</h3>
<div>类型：<select id="type" >
			<option value="0"  ${param.type == 0?'selected="selected"':'' } >未处理</option>
			<option value="1"  ${param.type == 1?'selected="selected"':'' }>已处理</option>
			<option value="2" ${param.type == 2?'selected="selected"':'' }>已取消</option>
			<option  ${param.type == -1?'selected="selected"':'' } value="-1">全部</option>
			<option  ${param.type == -2?'selected="selected"':'' } value="-2">未分配</option>
			</select>&nbsp;
	创建日期：<input id="cdate" name="cdate" onfocus="WdatePicker()" />&nbsp;
	处理日期：<input id="hdate" name="hdate" onfocus="WdatePicker()" />&nbsp;
	userid:<input id="userid" name="userid" value="${param.userid}"/>
	&nbsp;&nbsp;<input type="button" value="查询" onclick="fn()">
</div>
<br>
<div style="color: #008400">备注：同一个用户下的商品若有不同的邮箱地址，则会给用户表头中显示的邮箱发送。优惠价为空则不进行修改并不发送邮箱</div>
<div style="width: 96%">
	<table id="table" class="table table-bordered  table-hover definewidth m10" >
		<tbody>
			<tr>
				<td width="80px">商品ID</td>
				<td width="80px">商品信息</td>
				<td width="50px">MOQ</td>
				<td width="50px">客户定量</td>
				<td>单价</td>
				<td width="60px">状态</td>
				<td width="400px">客户note</td>
				<td style="min-width: 100px">创建日期</td>
				<td style="min-width: 100px">处理日期</td>
				<td style="min-width: 100px">有效期</td>
				<td style="min-width: 75px;">是否失效</td>
				<td>交互内容</td>
				<td width="60px">回复</td>
				<td>国家</td>
				<td>优惠价</td>
				<td>货币单位</td>
			</tr>
			 
		</tbody>
	</table>
</div>
<div id="model-window">
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">输入发给客户的邮件内容:</h4>
		      </div>
		      <div class="modal-body">
		      	<div>
		      	
<!-- $.post("/cbtconsole/WebsiteServlet",
	{action:'savePainteracted',className:'PreferentialwServlet',email:email,ids:ids,gids:gids,prices:prices,sprices:sprice,title:name,number:number,userid:userid,confirm:0,eend:eend,sessionid:sessionid,username:username},
  			function(res){
  				if(res>0){
  					alert("发送成功");
  				}else{
  					alert("发送失败");
  				}
 	});  -->
		      	<form id="form1" action="/cbtconsole/WebsiteServlet">
		      		<input type="hidden" value="savePainteracted" name="action"/>
		      		<input type="hidden" value="PreferentialwServlet" name="className"/>
		      		<input type="hidden" id="email_" name="email"/>
		      		<input type="hidden" id="ids_" name="ids"/>
		      		<input type="hidden" id="gids_" name="gids"/>
		      		<input type="hidden" id="prices_" name="prices"/>
		      		<input type="hidden" id="sprices_" name="sprices"/>
		      		<input type="hidden" id="title_" name="title"/>
		      		<input type="hidden" id="number_" name="number"/>
		      		<input type="hidden" id="userId_" name="userid"/>
		      		<input type="hidden" id="confirm_" name="confirm" value="0"/>
		      		<input type="hidden" id="eend_" name="eend"/>
		      		<input type="hidden" id="sessionid_" name="sessionid"/>
		      		<input type="hidden" id="username_" name="username"/>
		      		
		      		<input type="hidden" id="img_" name="img"/>
		      		<input type="hidden" id="currency_" name="currency"/>
		      		<input type="hidden" id="question_" name="question"/>
		      				      		
		      		<textarea id="emailContent" name="content" rows="10" cols="75"></textarea>
		      	</form>
		      	</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		        <button type="button" class="btn btn-primary" onclick="realSend()">发送</button>
		      </div>
		    </div>
		  </div>
		</div>
	</div>
<div class="pages" id="pages">
<span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font id="count"></font></span>&nbsp;&nbsp;当前页<font id="page"></font><button onclick="fn(3)">上一页</button>&nbsp;<button  onclick="fn(2)">下一页</button></div>
</div>
<div>
<input type="hidden" id="adminid" value="${param.adminid}"/>
</div>
	<script type="text/javascript">fn(${param.type});</script>
</body>
</html>