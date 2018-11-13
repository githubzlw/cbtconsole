<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="com.cbt.processes.servlet.Currency"%>
<%@page import="java.util.List"%>
<%@page import="com.cbt.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/fine-uploader/jquery.fine-uploader.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/js/fine-uploader/fine-uploader-new.css">
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>订单手动生成与进账录入</title>
<style type="text/css">
body{width: 900px;margin: 0 auto;}
.body{width: 900px;height:auto;}
.table1 td{width: 300px;}
.table td{width: 300px;}
.error{background-color:#FF8484;color:white;}
.error2{background-color:#93926C;color:white;}
.error3{background-color: yellow;;color:red;}
.btn-primary{    color: #fff;
    background-color: #5db5dc;
    border-color: #2e6da4;    
    padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;}

.eiping{width:800px;padding:30px;background:#f3f3f3;border:2px dashed #F44336;}
.eipingr{width:800px;padding:30px;background:#f3f3f3;border:2px dashed #4CAF50;}
.eispan{
font-size: 25px;
}
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
</style>
<script type="text/template" id="qq-template-manual-trigger">
        <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="Please upload a invoice file here">
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

<script type="text/javascript">
<%String sessionId = request.getSession().getId();
String userJson = Redis.hget(sessionId, "admuser");
Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
String roletype=user.getRoletype();
int uid = user.getId();
String admName =user.getAdmName();
pageContext.setAttribute("admName", admName);
%>
$(function(){
	var roleTypejs = <%=roletype%>;
	var consoleName = '<%=admName%>';
	$("#dealMan").html(consoleName);
	$("input[name='qqfile']").attr("accept","image/gif, image/jpeg , image/png, application/pdf");
})

function fnonfocus(obj){
	if(obj == null){
		return ;
	}
	obj.style.color='black';
	obj.value="";
}


//检查是否是非数字值
function checkMO(obj) {
	setTimeout("", 1000);
	if(obj == null){
		return ;
	}
    if (isNaN(obj.value)) {
        obj.value = "";
    }
   	var vlue = obj.value.toString();
   	var psplit = vlue.split(".");
   	//小数点不能超过1个
    if(psplit.length>2){
        obj.value = "";
        return;
    }
    //检查小数点后是否对于两位
    if (psplit.length >1 && psplit[1].length>2) {
        obj.value = "";
        return;
    }
}
function fnup(){
	document.getElementById("trigger-upload").click();
}

/*  生成订单*/
function fnsubmint(){
	
	var userid = $("#userid").val();
	if(userid == ""&&userid =='必填'){
	    alert("用户id不可为空");
	    return false;
	}
	var price = $("#price").val();
	if(price == ""&&price =='必填'){
	    alert("金额不可为空");
	    return false;
	}
	$("#obt").attr("disabled", true); 
	var remark = $("#remark").val();
	if(remark=='只能填写英文'){
		remark = '';
	}
	var ostate = $("#ostate").val();
	var dealMan = $("#dealMan").html();
	var upfile = $("#upfile").val();
	 $.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/autoorder/add',
		data:{userid:userid,price:price,remark:remark,ostate:ostate,dealMan:dealMan,upfile:upfile},
		success:function(res){
			$(".qq-upload-list li").remove();
			if(res!=''){
				if(res=='0'){
					alert('请重新登录');
				}
				
				if(res=='-4'){
					alert('失败--state=-4');
				}
				if(res=='-3'){
					alert('失败--state=-3');
				}
				if(res=='-5'){
					alert('失败--用户无可用地址');
				}
				
				if(res.length>5){
					$("#redult").val("订单生成成功,订单号:"+res);
					$("#orderno").val(res);
					$("#payment_amount").val(price);
					$("#user_id").val(userid);
				}	
			}else{
				alert('添加失败，请重新添加');
			}
		},
		error:function(XMLResponse){
			alert('error');
		}
	}); 
	 $("#obt").attr("disabled", false); 
}
/*添加付款记录  */
function fnpay(){
	
	var userid = $("#user_id").val();
	if(userid == ""&&userid =='必填'){
	    alert("用户id不可为空");
	    return false;
	}
	var payment_amount = $("#payment_amount").val();
	if(payment_amount == ""&&payment_amount =='必填'){
	    alert("金额不可为空");
	    return false;
	}
	var paymentid = $("#paymentid").val();
	if(paymentid == ""&&paymentid =='必填'){
	    alert("交易号不可为空");
	    return false;
	}
	var ptype = $("#ptype").val();
	
	var orderno = $("#orderno").val();
	if(orderno == ""&&orderno =='必填'){
	    alert("订单号不可为空");
	    return false;
	}
	var orderdesc = $("#orderdesc").val();
	if(orderdesc == ""&&orderdesc =='必填'){
	    alert("paypal账号不可为空");
	    return false;
	}
	
	var otime = $("#otime").val();
	if(otime == ""&&otime =='必填'){
	    alert("进账时间不可为空");
	    return false;
	}
    var upfile = $("#upfile").val();
	var dealMan = $("#dealMan").html();
	$("#p-bt").attr("disabled", true); 
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/autoorder/payment',
		data:{userid:userid,paymentAmount:payment_amount,ptype:ptype,orderno:orderno,paymentid:paymentid,orderdesc:orderdesc,dealMan:dealMan,otime:otime,upfile:upfile},
		success:function(res){
			if(res!=''){
				if(res=='0'){
					alert('请重新登录');
				}
				if(res=='-1'){
					alert('paypal账号输入错误');
				}
				if(res=='-5'){
					alert('用户无可用收货地址');
				}
				if(res.length>5){
				    $("#predult").val("交易生成:"+res);
				}
			}else{
				alert('添加失败，请重新添加');
			}
		},
		error:function(XMLResponse){
			alert('error');
		}
	});
	$("#p-bt").attr("disabled", false); 
}
</script>

</head>
<body class="body">
<input type="hidden" id="upfile"  class="upfile" value="">
	<br>
	<br>
	<br>
	<div  style="color: black;font-weight: bold;font-size: 30px;margin-left: 10px;">操作人员:<span id="dealMan"></span>
	<input type="button" value="查看所有自生成订单" onclick="window.open('/cbtconsole/autoorder/alist','_blank')" style="font-size: 18px;" class="btn btn-primary btn-sm">
	</div>
	<br>
	
<div align="center">
	<div >
	<div style="font-size: 35px;">订单手动生成与进账录入</div>
		
	<div class="eiping">
	<div align="left">
	<span class="eispan">用户id:<input type="text" id="userid" class="userid" name="userid" style="width: 100px;height: 50px;color: gray;" onfocus="fnonfocus(this)" value="必填"></span>
	<!-- <span class="eispan">&nbsp;&nbsp;数量:<input type="text" name="quantity"  class="quantity"  id="quantity" style="width: 100px;height: 50px;" value="1"/></span> -->
	<span class="eispan">&nbsp;&nbsp;金额:<input type="text" name="price"  class="price"  id="price" style="width: 100px;height: 50px;color: gray;" onfocus="fnonfocus(this)" value="必填" onkeyup="checkMO(this)"/>USD</span>
	<span class="eispan">&nbsp;&nbsp;状态:
	<select class="eispan" style="height: 50px;" id="ostate">
	<option value="0" >未付款订单</option>
	<option value="5">已付款订单</option>
	</select></span>
	</div>
	<!-- <br>
	<div align="left">
	<span class="eispan">尺码:&nbsp;<input type="text" id="size" class="size" name="size" style="width: 100px;height: 50px;"></span>
	<span class="eispan">&nbsp;&nbsp;颜色:<input type="text" name="color"  class="color"  id="color" style="width: 100px;height: 50px;"/></span>
	<span class="eispan">&nbsp;&nbsp;其他:<input type="text" name="other"  class="other"  id="other" style="width: 100px;height: 50px;"/>&nbsp;</span>
	</div> -->
	<br>
	<!-- <div align="left">
	<span class="eispan">名称:<input type="text" id="name"  name="name" style="width: 700px;height: 50px;"/></span>
	</div>
	<br> -->
	<!-- <div align="left">
	<span class="eispan" >地址:<input type="text" id="address"  name="address" style="width: 700px;height: 50px;"/></span>
	</div>
	<br> -->
	<div align="left">
	<span class="eispan" ><span style="float: left;">前台客<br>户可见<br>备注</span><span style="float: left;margin-top: 15px;">:<input onfocus="fnonfocus(this)" type="text" id="remark"  name="remark"  style="width: 700px;height: 50px;color: gray;" value="只能填写英文"/>
	</span><span style="clear: both;"></span>
	</span>
	</div>
	<div style="clear: both;"></div>
	<br>
	<div align="left">
	<span class="eispan" >结果:&nbsp;</span><input type="text" value="不用填写" id="redult" class="redult" name="redult" style="width:700px;height: 50px;color: red;font-size: 25px;"  >
	</div>
	<br>
	<div align="left" style="color: #3f51b5;">
	<span>Note:&nbsp;
				    <span>&nbsp;1.文件名称不要带有中文</span></span>
				    <span>&nbsp;2.文件大小不要超过10M</span>
					<span>3.只支持jpg、png、jpeg、pdf</span>
	</div>
	<div align="left"><div id="fine-uploader-manual-trigger"></div>
	</div>
	<br>
	<input id="obt"  type="submit" class="btn btn-primary btn-sm" style="margin-left:5px;display:inline-block;width: 350px;height: 50px;font-size: 25px;" value="生成订单"  onclick="fnup()"/>
	</div>	
		</div>
		
	<br>
	<br>
	
	<!--  进账记录  -->
	
	<div  class="paymentc" id="paymentct">
	<div style="font-size: 35px;">进账记录</div>
		
	<div class="eipingr">
	<div align="left">
	<span class="eispan">用户id:<input type="text" id="user_id" class="userid" name="user_id" style="width: 100px;height: 50px;color: gray;" onfocus="fnonfocus(this)" value="必填"></span>
	<span class="eispan">&nbsp;金额:<input type="text" name="payment_amount"  class="price"  id="payment_amount" style="width: 100px;height: 50px;color: gray;"  onkeyup="checkMO(this)" onfocus="fnonfocus(this)" value="必填"/>USD</span>
	<span class="eispan">&nbsp;付款方式:
	<select class="eispan" style="height: 50px;" id="ptype">
	<option value="0" >Paypal</option>
	<option value="1">WireTransfer</option>
	</select></span>
	</div>
	<br>
	<div align="left">
	<span class="eispan" ><span style="float: left;">付款&nbsp;<br>时间&nbsp;</span><span style="float: left;">:<input type="text" id="otime"  name="otime"  value="必填" style="width: 100px;height: 50px;color: gray;" onfocus="WdatePicker({skin:'whyGreen',minDate:'2014-10-30',maxDate:'2120-12-20'})"/></span>
	</span>
	</div>
	<div style="clear: both;"></div>
	<br>
	<div align="left">
	<span class="eispan" >订单号:<input type="text" id="orderno"  name="orderno"  style="width: 700px;height: 50px;color: gray;" value="必填"/></span>
	</div>
	<br>
	<div align="left">
	<span class="eispan" >paypal<br>交易号:<input type="text" id="paymentid"  name="paymentid"  style="width: 700px;height: 50px;color: gray;" value="必填"/></span>
	</div>
	<br>
	<div align="left">
	<span class="eispan" >paypal<br>账号:&nbsp;<input type="text" id="orderdesc"  name="orderdesc"  style="width: 700px;height: 50px;color: gray;" value="必填"/></span>
	</div>
	<br>
	<div align="left">
	<span class="eispan" >结果:&nbsp;</span><input type="text" id="predult" class="predult" name="predult" style="width:700px;height: 50px;color: red;font-size: 25px;"  value="不用填写" >
	</div>
	<br>
	<input id="p-bt" type="submit" class="btn btn-primary btn-sm" style="margin-left:5px;display:inline-block;width: 350px;height: 50px;font-size: 25px;" value="生成进账记录"  onclick="fnpay()"/>
	</div>	
		</div>
</div>
<br>
<br>
<br>
<script>
	 var manualUploader = new qq.FineUploader({
         element: document.getElementById('fine-uploader-manual-trigger'),
         template: 'qq-template-manual-trigger',
         request: {
             endpoint: '/cbtconsole/server/uploads'
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
             allowedExtensions: ['jpeg', 'jpg', 'png','pdf'],
             sizeLimit:100000000, //100*1000*1000 bytes
             accept:"image/gif, image/jpeg , image/png,  application/pdf"
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
           			$("#upfile").val(saveurl+str);
             	 }
				},
				onAllComplete : function (succeeded, failed) {
					if(failed.length==0){
	        			$("#upmessage").show(3000).delay(2000).hide(2000);
	        			$("#upmessage").html("Upload successfully.");
	        			fnsubmint();	
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
</body>
</html>