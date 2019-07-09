<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="com.cbt.bean.ComplainChat"%>
<%@page import="com.cbt.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/fine-uploader-new.min.css">
<script type="text/javascript" src="/cbtconsole/js/fine-uploader/jquery.fine-uploader.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/keys.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
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
    width: 80%;
    margin-top: 30px;
} 
td{
vertical-align:middle;
}

#trRecords tr td{
	line-height:80px; height:80px;
}
#trRecords tr td button{
	height:30px;
	line-height:20px;
	margin-bottom:5px;
}
.showImg{
	width:100px;
	height:100px;
	margin: 10px 10px;
	display: inline;
}
.mid{
	margin: 0 auto;
    background:#fff;
    border:1px #e3e3e3 solid;
}
.rowll{
	margin-top: 10px;
}
label{display:inline;margin:0;font-weight:100;}
body{line-height:1em;}
.comwarnred,.conpwar{color:#f00;padding:5px 0;font-size:13px;}
.conpwar{margin-left:-160px;}
.form-group ul li{
	padding: 5px 0px;
}
.errMsg{color:#f00;padding:5px 0;font-size:13px;margin-left:15px;}
a:HOVER{
	cursor:pointer;
}
.urlinput{box-sizing:content-box;}
    #trigger-upload {
        color: white;
        background-color: #00ABC7;
        font-size: 14px;
        padding: 7px 20px;
        background-image: none;
    }

    #fine-uploader-manual-trigger .qq-upload-button {
        margin-right: 15px;
        border: medium none;
background:rgb(255, 108, 0) none repeat scroll 0% 0%;
    }
    #fine-uploader-manual-trigger .qq-upload-button:hover{
    background:rgb(255, 135, 0) ; 
    }

    #fine-uploader-manual-trigger .buttons {
        width: 36%;
    }

    #fine-uploader-manual-trigger .qq-uploader .qq-total-progress-bar-container {
        width: 60%;
    }
    #subBtn{background: rgb(0, 156, 255) none repeat scroll 0% 0%;width: 120px;}
    #subBtn:hover{background:rgb(81, 178, 240) none repeat scroll 0% 0%;}
ul#img_sf_1{ list-style:none;float:center; display:inline;}
ul#img_sf_1 li{float:left; width:60px; height:150px; display:inline; margin:2px 2px 2px 5px;}
ul#img_sf_11 li a{width:60px; height:150px; display:block;}
ul#img_sf_1 li a img{width:100%; border:1px #999 solid;}
 ul#img_sf_1 li a:hover{ z-index:100; margin:-16px 0px 0px -16px; position:absolute;}
ul#img_sf_1 li a img:hover{width: 100%; height: 100%; border:1px #999 solid;}
/**/
ul#img_sf_1 li {

   width: 100px;
   height: 100px;}
   .qq-uploader {
   position: relative;
   min-height: 200px;
   max-height: 490px;
   overflow-y: hidden;
   width: inherit;
   border-radius: 6px;
   background-color: #FDFDFD;
   border: 1px dashed #CCC;
   padding: 20px;
}
.trigger-upload {
   color: #fff;
   background-color: #00ABC7;
   font-size: 14px;
   padding: 7px 20px;
   background-image: none;
   border: 0;
   cursor: pointer;
}
.main .w-buttom{padding:0;margin-bottom:20px;}
.qq-upload-button {
   margin-right: 15px;
}
.qq-upload-button {
   display: inline;
   width: 105px;
   margin-bottom: 10px;
   padding: 7px 10px;
   text-align: center;
   float: left;
   border: medium none;
   background: rgb(255, 108, 0) none repeat scroll 0% 0%;
   color: #FFF;
   border-radius: 2px;
   box-shadow: 0 1px 1px rgba(255,255,255,.37) inset, 1px 0 1px rgba(255,255,255,.07) inset, 0 1px 0 rgba(0,0,0,.36), 0 -2px 12px rgba(0,0,0,.08) inset;
} 
</style>
<script type="text/template" id="qq-template-manual-trigger">
        <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="Please upload a file here">
            <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container">
                <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar"></div>
            </div>
            <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
                <span class="qq-upload-drop-area-text-selector"></span>
            </div>
            <div class="buttons">
                <div class="qq-upload-button-selector qq-upload-button">
                    <div>choosefile</div>
                </div>
                <button type="button" id="trigger-upload" class="btn btn-primary" style="display:none">
                    <i class="icon-upload icon-white"></i>upload
                </button>
            </div>
			<span id="upmessage" style="color:red"></span>
            <span class="qq-drop-processing-selector qq-drop-processing">
                <span>Processing dropped files...</span>
                <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
            </span>
            <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals">
                <li>
                    <div class="qq-progress-bar-container-selector">
                        <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>
                    </div>
                    <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
                    <img class="qq-thumbnail-selector" qq-max-size="100" qq-server-scale>
                    <span class="qq-upload-file-selector qq-upload-file"></span>
                    <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>
                    <input class="qq-edit-filename-selector qq-edit-filename" tabindex="0" type="text">
                    <span class="qq-upload-size-selector qq-upload-size"></span>
                    <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel">cancel</button>
                    <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry">reupload</button>
                    <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">remove</button>
                    <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
                </li>
            </ul>

            <dialog class="qq-alert-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">close</button>
                </div>
            </dialog>

            <dialog class="qq-confirm-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">No</button>
                    <button type="button" class="qq-ok-button-selector">Yes</button>
                </div>
            </dialog>

            <dialog class="qq-prompt-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <input type="text">
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">Cancel</button>
                    <button type="button" class="qq-ok-button-selector">Ok</button>
                </div>
            </dialog>
        </div>
    </script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtprogram/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<script type="text/javascript">
<%String sessionId = request.getSession().getId();
String userJson = Redis.hget(sessionId, "admuser");
Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
String roletype=user.getRoletype();
int uid = user.getId();
String admName =user.getAdmName();
%> 

function submitForm(){
	 var complainText =$("#chatText").val();
	 var picture=$('.qq-editable').html();
	 if(complainText==''){
		 alert("请输入给用户的回复信息");
		 return false;
	 }
	 $("#urls_").html('');
	 $("#urls").val('');
	 if(complainText!='' && picture==undefined){
		 fn();
	 }else if(complainText!=''&& picture!=undefined){
		 document.getElementById("trigger-upload").click();
	 }
}


function fn(){
		var cid = $("#complainid").val();	
		var dealAdmin = $("#dealAdmin").val();	
		var dealAdminId = $("#dealAdminId").val(); 
		var presentAdmin = $("#presentAdmin").val();	
		var presentAdminId = $("#presentAdminid").val(); 
		var chatText = $("#chatText").val(); 
		var complainFileId = $("#complainFileId").val(); 
		var userEmail = $("#userEmail").html(); 
		var orderNo = $("#orders").html(); 
		var urls =$("#urls").val();
		var websiteType =$("#website_type").val();
		if(chatText.trim()!=""){
			
			$.post(
				"../complainChat/responseCustomer",
				{
					complainid : cid,
					chatAdmin:dealAdmin,
					chatAdminid:dealAdminId,
					chatText:chatText,
					complainFileId:complainFileId,
					dealAdmin:presentAdmin,
					dealAdminId:presentAdminId,
					userEmail:userEmail,
					orderNo:orderNo,
					urls:urls,
                    websiteType:websiteType
				}, function(res) {
					alert(res.message);
					$("#chatText").val("");
					location.reload();
				});
			
		}else{
			alert("请输入给用户的回复信息。");		
		}
	}
</script>

<title>申诉详情</title>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<input type="hidden" value="${param.cid }" id="param-cid">
<input type="hidden" name="orderid"  id="orderid" value="${complain.refOrderId }"/>

	<div class="main">
	<div onclick="javascript:history.back(-1)" style="cursor: pointer;color:#00aff8;">« Back</div>
		<div class="tabletitle">申诉详情</div>
		<table class="table table-bordered  table-hover definewidth m10">
			<caption>申诉信息</caption>
			<tbody>
				<tr>
					<td class="tableleft">投诉订单编号：</td>
					<td id="orders">${complain.refOrderId }</td>
					<td class="tableleft" style="width: 15%">投诉时间：</td>
					<td style="width: 34%">${complain.creatTime }</td>
				</tr>
				<tr>
					<td class="tableleft" style="width: 15%">客户id：</td>
					<td style="width: 34%">
					<form id="form1" method="GET" action="../rechargeRecord/findRecordByUid" style="display: none">
						<input type="text" id="uid" name="uid" value="${complain.userid }">
					</form>
						${complain.userid }
						<c:if test="${complain.rid!=0 && complain.rid!=null }">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">客户要求退款：${complain.rcurrencyShow }${complain.rappcount }</font>&nbsp;&nbsp;&nbsp;<a onclick="goDetail()">详情</a>
						</c:if>
					</td>
					<td class="tableleft" style="width: 15%">客户邮箱：</td>
					<td style="width: 34%" id="userEmail">${complain.userEmail }</td>
				</tr>
				<tr>
					<td class="tableleft" style="width: 15%">投诉类型：</td>
					<td style="width: 34%">${complain.complainType }</td>
					<td class="tableleft" style="width: 15%">投诉状态：</td>
					<td style="width: 34%">
					<c:choose>
						<c:when test="${complain.complainState==0}">
							正在申诉
						</c:when>
						<c:when test="${complain.complainState==1 }">
							沟通中
						</c:when>
						<c:otherwise>
							已完结
						</c:otherwise>
					</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tableleft">投诉内容：</td>
					<td colspan="3" id="user_info"><div style="float: left;">${complain.complainText }</div></td>
				</tr>
				<tr>
					<td class="tableleft">退款补偿：</td>
					<td colspan="3"><em style="font-size: 18px;color: #2196F3;font-weight: bold;">${money}</em>&nbsp;&nbsp;USD</td>
				</tr>
				<c:if test="${not empty complain.disputeList }">
				<tr>
					<td class="tableleft">第三方申诉:</td>
					<td colspan="3">
					<c:forEach items="${complain.disputeList }" var="dispute">
					<a target="_blank" href="/cbtconsole/customer/dispute/info?disputeid=${ dispute}">${ dispute}</a>
					<br>
					</c:forEach>
					
					</td>
				</tr>
				</c:if>
<!-- 				<tr> -->
<!-- 					<td class="tableleft">图片：</td> -->
<!-- 					<td colspan="3" id="imgs"> -->
						
<!-- 					</td> -->
<!-- 				</tr> -->
			</tbody>
		</table>
		<div style="height: auto;overflow-y: auto;">
		<table class="table table-bordered  table-hover definewidth m10" style="margin-top: 20px">
			<caption>历史交流记录</caption>
			<tbody id="history">
			</tbody>
		</table>
		<span id="urls_" style="display: none">
			<c:forEach items="${imgsList }" var="cf">
				${cf.imgUrl }
			</c:forEach>
		</span>
		</div>
		<div class="mid" style="margin-bottom:20px;">
	<div>
		<div class="rowll">
			<form id="myForm" class="form-horizontal" action="" style="margin-top: 20px" method="post" enctype="multipart/form-data">
			  <div class="form-group">
			  <div class="col-sm-3 col-sm-offset-1" style="margin-left:20px;">
			    <label for="uploadPic" class="control-label" style="text-align: left;">Upload Pictures:</label>
			    <div style="margin-top:20px"><span style="color: red;">Note</span>:
				    <ul style="margin-left: 15px;margin-top: 10px;">
				    	<li>1:jpg、png、jpeg is allowed</li>
				    	<li>2:Size：lease than 10M</li>
				    </ul>
			    </div>
			  </div>
			    <div class="col-sm-7">
			    	 <div id="fine-uploader-manual-trigger"></div>
			    </div>
			  </div>
			  <input type="hidden" id="urls" name="urls"/>
			</form>
			  <span style="display: none" id="urls_"></span>
		</div>
	</div>
	</div>
	<div id="model-window">
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		      </div>
		    </div>
		  </div>
		</div>
	</div>
		<form onsubmit="return false;">
			  <input type="hidden" name="complainid" id="complainid" value="${complain.id }"/>
			  <input type="hidden" name="dealAdminid" id="dealAdminid" value="${complain.dealAdminid }"/>
			  <input type="hidden" name="dealAdmin" id="dealAdmin" value="${complain.dealAdmin }"/>
			  <input type="hidden" name="presentAdminid" id="presentAdminid" value="<%=uid %>"/>
			  <input type="hidden" name="presentAdmin" id="presentAdmin" value="<%=admName %>"/>
			  <input type="hidden" name="complainFileId" id="complainFileId" value="${complain.cfid }"/>
			  <div class="form-group">
			    <div class="col-sm-13">
			    	<textarea class="form-control" rows="8" id="chatText" name="chatText"></textarea>
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-offset-3 col-sm-10">
                  邮件回复客户网站名:
                  <select id="website_type" name="websiteType" style="height: 28px;width: 160px;">
                    <option value="1" <c:if test="${websiteType == 1}">selected="selected"</c:if>>import-express</option>
                    <option value="2" <c:if test="${websiteType == 2}">selected="selected"</c:if>>kidsproductwholesale</option>
                  </select>
			      <button type="button" id="submitForm1" onclick="submitForm();" class="btn btn-success">回复客户</button>
			      <button type="button" class="btn btn-success" onclick="closeCase()">关闭case</button>
			      <button type="button" class="btn btn-success" onclick="changeavailable()">余额补偿</button>
			    </div>
			  </div>
		  </form>
	</div>
			  <br><br>
			  <br><br>
			  <br><br>
			  <script>
	 var manualUploader = new qq.FineUploader({
         element: document.getElementById('fine-uploader-manual-trigger'),
         template: 'qq-template-manual-trigger',
         request: {
             endpoint: '../complainChat/uploads'
         },
         editFilename: {//编辑名字
             enable: true
         },
         thumbnails: {
             placeholders: {
                 waitingPath: '/cbtconsole/img/waiting-generic.png',
                 notAvailablePath: '/cbtconsole/img/not_available-generic.png'
             }
         },
         validation: {
             allowedExtensions: ['jpeg', 'jpg', 'png'],
             sizeLimit:100000000, //100*1000*1000 bytes
             accept:"image/jpg, image/jpeg , image/png"
         },
         callbacks: {
         	/* 开始上传 */
             onUpload: function (id, name) {
             },
             /* 选择文件后 */
             onSubmitted: function (id, name) {
             },
             /* 上传完成 */
             onComplete : function(id, name, responseJSON) {
            	 var str="";
            	 if(responseJSON.success){
           			var saveurl = responseJSON.saveLoaction;
            		var jsonArray= responseJSON.names;
           			str+=jsonArray+",";
           			$("#urls_").append(str);
             	 }
				},
				onAllComplete : function (succeeded, failed) {
					if(failed.length==0){
						var aa =$("#urls_").html();
	        			$("#urls").val(aa.substring(0,aa.length-1));
	        			$("#upmessage").show(3000).delay(2000).hide(2000);
	        			$("#upmessage").html("Upload successfully.");
	        			fn();	
					}else if(succeeded.length==0){
	        			$("#upmessage").show(3000).delay(2000).hide(2000);
	        			$("#upmessage").html("All failed.");
					}
				}
         },
         autoUpload: false,
         debug: true
     });

     qq(document.getElementById("trigger-upload")).attach("click", function() {
    	 manualUploader.uploadStoredFiles();
     });
     
    </script>
	<script type="text/javascript">
$(function(){
	 $(".modal-body table tbody").delegate("tr","click",function(){
		 var p = this;  
	    $(this).children().slice(1).click(function(){  
		        $($(p).children()[0]).children().each(function(){  
		            if(this.type=="checkbox"){  
		                if(!this.checked){  
		                    this.checked = true;  
		                }else{  
		                    this.checked = false;  
		                }  
		            }  
		        });  
	    });  
	});
	$("input[name='qqfile']").attr("accept","image/gif, image/jpeg , image/png");
})

</script>
	<script type="text/javascript">
	
	function setImage(obj){    
		image.src = obj.value;
		}
	
	
	$(function(){
		Date.prototype.format = function(fmt){ 
			  var o = {   
			    "M+" : this.getMonth()+1,                 //月份   
			    "d+" : this.getDate(),                    //日   
			    "h+" : this.getHours(),                   //小时   
			    "m+" : this.getMinutes(),                 //分   
			    "s+" : this.getSeconds(),                 //秒   
			    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
			    "S"  : this.getMilliseconds()             //毫秒   
			  };
			  if(/(y+)/.test(fmt))   
			    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
			  for(var k in o)   
			    if(new RegExp("("+ k +")").test(fmt))   
			  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
			  return fmt;   
			}
		
		var  cid = $("#complainid").val();
		 $.post("../complain/getCommunicatingByCidBG",{complainId:cid},function(res){
				if(res.status){
					var jsonArray = res.complainList;
					var html ="";
					for(var i in jsonArray){
						var obj= jsonArray[i];
						var date = new Date(obj["ccchatTime"]);
						var sdate = date.format('yyyy-MM-dd hh:mm:ss')
						if(obj["ccflag"]==0){
							html+="<tr>";
							html+="<td colspan='4' style='text-align:left;border:none;'>"+sdate+"</td></tr>";
							html+="<tr><td colspan='2' style='text-align:left;border:none;padding-left:20px;word-break: break-word;'>Customer"+":"+obj["ccchatText"]+"</td>td style='border:none' colspan='2'>&nbsp;</td></tr>"
						}else if(obj["ccflag"]==1){
							html+="<tr>";
							html+="<td colspan='4' style='text-align:right;border:none'>"+sdate+"</td></tr>";
							html+="<tr><td style='border:none;' colspan='2'>&nbsp;</td><td colspan='2' style='text-align:right;border:none;padding-right:20px;word-break: break-word;'>"+obj["ccchatText"]+':'+obj["ccchatAdmin"]+""
							if(obj["imgUrl"] !=null && obj["imgUrl"] != "" && obj["imgUrl"]!=undefined){
                                html+="<br><img src='"+obj["imgUrl"]+"' style='max-width:840px;max-height:200px'></img>";
							}
							html+="</td></tr>";
						}	
					}
					$("#history").html(html);
				}else{
						
				}
			})		
			
			
		
		
		
// 	$("#submitForm").on("click",function(){
// 			var cid = $("#complainid").val();	
// 			var dealAdmin = $("#dealAdmin").val();	
// 			var dealAdminId = $("#dealAdminId").val(); 
// 			var presentAdmin = $("#presentAdmin").val();	
// 			var presentAdminId = $("#presentAdminid").val(); 
// 			var chatText = $("#chatText").val(); 
// 			var complainFileId = $("#complainFileId").val(); 
// 			var userEmail = $("#userEmail").html(); 
// 			var orderNo = $("#orders").html(); 
// 			if(chatText.trim()!=""){
				
// 				$.post(
// 					"../complainChat/responseCustomer",
// 					{
// 						complainid : cid,
// 						chatAdmin:dealAdmin,
// 						chatAdminid:dealAdminId,
// 						chatText:chatText,
// 						complainFileId:complainFileId,
// 						dealAdmin:presentAdmin,
// 						dealAdminId:presentAdminId,
// 						userEmail:userEmail,
// 						orderNo:orderNo
// 					}, function(res) {
// 						alert(res.message);
// 						$("#chatText").val("");
// 						location.reload();
// 					});
				
// 			}else{
// 				alert("请输入给用户的回复信息。");		
// 			}
// 		})	
			
 	 var imgs = $("#urls_").html().trim();
	 var imgArray = imgs.split(',');
	 for(var i in imgArray){
	     console.log("imgArray[i]==="+imgArray[i]);
			if(imgArray[i]){
				var html ="<ul id='img_sf_1'><li><a><img class='showImg' style='float:left;'  id='" + i + "' /></a></li></ul>";
				$("#user_info").append(html);
				$("#" + i).attr("src","http:///www.import-express.com/"+imgArray[i]+"");
			}
		 } 
// 	 for(var i in imgArray){
// 		if(imgArray[i]){
// 			var html ="<img class='showImg'   id='" + i + "' />";
// 			$("#imgs").append(html);
//  			$("#" + i).attr("src","http:///www.import-express.com/"+imgArray[i]+"");
// 		}
// 	 }  
	 
	 
})

function closeCase(){
	var complainid = $("#complainid").val();
	$.post("../complain/closeComplain",{complainId:complainid},function(res){
		alert(res.message);
	})
}

function goDetail(){
	form1.submit();
}

function changeavailable(){
	var userid=document.getElementById("uid").value;
	var complainid=document.getElementById("complainid").value;
	var orderid=document.getElementById("orderid").value;
	window.open("/cbtconsole/website/change_available.jsp?userid="+userid+"&orderid="+orderid+"&complainid="+complainid,"windows","height=500,width=900,top=300,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
}
</script>
</body>
</html>