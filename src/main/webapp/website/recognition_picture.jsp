<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>OCR图片管理</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<!-- <link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css"> -->
<style type="text/css">
.table {
	margin-left: 20px;
}
</style>
<script type="text/javascript"
	src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String date = sdf.format(new Date());
%>
<style type="text/css">
* {
	padding: 0;
	margin: 0;
	list-style: none;
}
#Img1{ width:150px; height:150px;}/*
#Img1:hover{ width:300px; height:300px;position: absolute;}*/

.div{
	float: left; padding: 30px;
	border: 2px solid aquamarine;
}

.div2{
	width: 280px;
	height: 50px;
	border: 2px solid aquamarine;
}
.div img{

	width: 100%;

	height: 100%;

	cursor: pointer;

	transition: all 0.6s;

	-ms-transition: all 0.8s;

}

.div img:hover{

	transform: scale(2.2);

	-ms-transform: scale(2.2);

}


html, body {
	height: 100%;
	font-family: "微软雅黑", Helvetica, Arial, sans-serif;
	color: #333;
	min-width: 1200px;
	font-size: 14px;
}


.main-top {
	overflow: hidden;
	padding-left: 50px;
}

.left {
	display: inline-block;
}
.main-center  .wenzi {
    margin-left: 55px;
}

.wenzi {
	display: inline-block;
	height: 32px;
	line-height: 32px;
	width: 100px;
	text-align: center;
	background: #629fd6;
	color: #fff;
	border-radius: 4px;
	letter-spacing: 2px;
	margin:5px;
}

.inputText, .selectText {
	display: inline-block;
	width: 200px;
	height: 30px;
	line-height: 30px;
	outline: none;
}

.left-margin {
	margin-left: 40px;
}

div.margin2 {
	margin-top: 20px;
}

.main-float {
	float: left;
	margin-left:50px;
	margin-top:50px;
}

.main-right {
	float: left;
	display: inline-block;
	margin-top: 90px;
	margin-left: 50px;
}

.main-right span {
	cursor: pointer;
}

.main-all {
	overflow: hidden;
}

.main-center {
    margin-left: 75px;
	margin-top: 50px;  
}

.main-table {
	margin: 0 55px;
	margin-bottom: 50px;
}

.table {
	width: 100%;
	border: 1px solid #ddd;
	border-collapse: separate;
	border-spacing: 0;
	border-collapse: collapse;
}

.table td, .table th {
	border: 1px solid #ddd;
	line-height: 30px;
	height: 30px;
	vertical-align: top;
	text-align: center;
}

.table th {
	background: #629fd6;
	color: #fff;
}

.a-link {
	display: inline-block;
	margin: 0 10px;
	color: #0000ff;
}
</style>
<script type="text/javascript">
    /*var myArray=new Array();
    var  maMap ={};*/
	$(function () {
        $.ajax({
            type: "GET",
            url: "${ctx}/Distinguish_Picture/FindCategory",
            dataType:"json",
            success: function(msg){
					var content="";
                    $("#imgtype").empty();
               		 content += '<option value="">请选择(全部)</option>';
                    for (var i = 0; i < msg.length; i++) {
                            content += '<option value="'+msg[i].categoryid+'">'+msg[i].name+'('+msg[i].id+')</option>';

                          }
                    $("#imgtype").append(content);
                    $("#imgtype option[value=${imgtype}]").attr("selected","selected");
            },
            error: function () {
                console.log("网络获取失败");
            }
        });
       /* var you = 0;
        $(".cbox:checked").each(function(){
            var elMap={};
            elMap['id'] =this.value;
            elMap['ocrneeddelete'] = $("#ocrneeddelete"+this.value).val();
            myArray[you] = elMap;
            you++;
        });*/
    });
function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
	}
	if(obj==-1){//前一页
		if(parseInt(page)<2){
			return ;
		}
		page = parseInt(page)-1;
	}else if(obj==1){//下一页
		if(parseInt(page)>parseInt($("#totalpage").val())-1){
			return ;
		}
		page = parseInt(page)+1;
	}else if(obj < -1){
		page=1;
	}
	
	$("#page").val(page);
    var pid = $("#pid").val();
    var imgtype = $("#imgtype").val();
    var state = $("#state").val();
    window.location.href="/cbtconsole/Distinguish_Picture/FindCustomGoodsInfo?page="+page+"&pid="+pid+"&imgtype="+imgtype+"&state="+state;}


function search(){
	var pid = $("#pid").val();
	var imgtype = $("#imgtype").val();
	window.location.href="/cbtconsole/Distinguish_Picture/FindCustomGoodsInfo?pid="+pid+"&imgtype="+imgtype;
}
function search2(){
        var pid = $("#pid").val();
        var imgtype = $("#imgtype").val();
        var state = $("#state").val();
        var Change_user = $("#Change_user").val();
        window.location.href="/cbtconsole/Distinguish_Picture/FindCustomGoodsInfo?pid="+pid+"&imgtype="+imgtype+"&state="+state+"&Change_user="+Change_user;
}


function  reset(){
	$(".selectText").val('');
	$("input[type='text']").val('');
}

    function fnselect(){
        if($("#checked").prop("checked") == true){
            $("input[class='cbox']").prop('checked',true );//全选
            $("#table:not(:first)").each(function(){
                if($(this).css("display")=="none"){
                    $(this).find("input[class='cbox']").prop('checked',false);
                }
            });
        }else{
            $("input[class='cbox']").prop('checked',false);//反选
        }
    }
function  updateSomes(type){
   	if(confirm("确定要删除选择的图片？")){
	var  mainMap ={};
	var erList= new Array();
    var id = "";
	var ocrneeddelete ="";
    var sbi = 0;
	$(".cbox:checked").each(function(){
		  var erMap={}; 
		  erMap['id'] =this.value;
		  erMap['ocrneeddelete'] = $("#ocrneeddelete"+this.value).val();
		  erList[sbi] = erMap;
		  sbi++;  		  
	});
	if(erList.length == 0){
		alert("请至少选择一个！");
	}else{
        var userName=$("#userName").val();
        for(var i=0;i<${customGoodsList }.length;i++){
			alert(${customGoodsList})
		}
       /* maMap['maList']=myArray;
        console.log(maMap);
        $.ajax({
            type:"post",
            url:"${ctx}/Distinguish_Picture/updateSomeis?userName="+userName,
            dataType:"json",
            contentType : 'application/json;charset=utf-8',
            data:JSON.stringify(maMap)
        });*/
        mainMap['bgList'] = erList;
		console.log(mainMap);
	 	$.ajax({
			type:"post",
			url:"${ctx}/Distinguish_Picture/updateSomeis_delete?userName="+userName,
			dataType:"json",
			contentType : 'application/json;charset=utf-8', 
		    data:JSON.stringify(mainMap),
			success:function(res){
				if(res==0){
                    alert("删除失败  !")
				}else{
					alert("删除成功 !");
					window.location.reload();
				}
			}
		})  
	}
 }
}
    function  updateSomes2(type){
        if(confirm("确定要下架选择的图片？")){
            var  mainMap ={};
            var erList= new Array();
            var id = "";
            var ocrneeddelete ="";
            var sbi = 0;
            $(".cbox:checked").each(function(){
                var erMap={};
                erMap['id'] =this.value;
                erMap['ocrneeddelete'] = $("#ocrneeddelete"+this.value).val();
                erList[sbi] = erMap;
                sbi++;
            });
            if(erList.length == 0){
                alert("请至少选择一个！");
            }else{
                var userName=$("#userName").val();
                /* maMap['maList']=myArray;
                 console.log(maMap);
                 $.ajax({
                     type:"post",
                     url:"${ctx}/Distinguish_Picture/updateSomeis?userName="+userName,
            dataType:"json",
            contentType : 'application/json;charset=utf-8',
            data:JSON.stringify(maMap)
        });*/
                mainMap['bgList'] = erList;
                console.log(mainMap);
                $.ajax({
                    type:"post",
                    url:"${ctx}",
                    dataType:"json",
                    contentType : 'application/json;charset=utf-8',
                    data:JSON.stringify(mainMap),
                    success:function(res){
                        if(res==0){
                            alert("线上下架失败  !")
                        }else{
                            alert("线上下架成功 !");
                            window.location.reload();
                        }
                    }
                })
            }
        }
    }
</script>
</head>
<body>
<h1 align="center"><b>删除有中文的图片</b></h1>
<h3 align="center" ><font color="red" id="tip"></font></h3>
	<div class="main">
		<div class="main-head"></div>
		<div class="main-all">
			<div class="main-float">
				<div class="main-top">
					<div class="left">
						<h3><span style="color: red">最近修改时间：(人工进行对图片的删除)</span></h3>
						<h3>单页显示数据：（35张）</h3>
					</div>
					<div class="left left-margin">
						<span style="color: red">备注：(人工进行对图片的删除)</span>
						<span style="color:blue">(当前处理人员：${username})<input type="hidden" id="userName" value="${username}"></span>
						状态位:<select   id="state" class="selectText"  onchange="search2()">
							<option value="" <c:if test="${state==''}"> selected </c:if>>未处理</option>
							<option value="1" <c:if test="${state==1}"> selected </c:if>>已处理(含中文)</option>
							<option value="2" <c:if test="${state==2}"> selected </c:if>>已处理(不含中文)</option>
						</select>
						处理人员:<select   id="Change_user" class="selectText"  onchange="search2()">
							<option value="">全部</option>
							<c:forEach items="${customGoodsList2}" var="ret">
								<option value="${ret.admname}" <c:if test="${ret.admname==Change_user}"> selected </c:if>>${ret.admname}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="main-top margin2">

					<div class="left">
						<span class="wenzi">产品分类：</span> <select   id="imgtype" class="selectText" onchange="search()">
						<option value="">请选择(全部)</option>
						<c:forEach items="${ret}" var="ret" >
								<option value="${ret.categoryid}" <c:if test="${ret.categoryid==imgtype}"> selected </c:if>>${ret.name}(${ret.id})</option>

						</c:forEach>
					</select>
					</div>
					<div class="left left-margin">
						<input type="hidden" value="${username}" id="user_">
						<span class="wenzi"  onclick="search();"><a href="#" style="text-decoration:none"><font color="white">查询</font></a></span>
						<span class="wenzi"  onclick="reset();"><a href="#" style="text-decoration:none"><font color="white">重置</font></a></span>
						<c:if test="${state==0}">
						<span class="wenzi"  onclick="updateSomes(1)" style="background-color: red"><a href="#" style="text-decoration:none"><font color="white">删除</font></a></span>
						<span style="color: blue">(勾选添加到（已处理含中文），未勾选则添加到（已处理不含中文）)</span>
						</c:if>
						<c:if test="${state==1}">
							<span class="wenzi"  onclick="updateSomes2()" style="background-color: red"><a href="#" style="text-decoration:none"><font color="white">线上下架</font></a></span>
						</c:if>
						<span style="color: red">(选择全部，点击查询可回到未处理状态位信息)</span>
						<%--<c:if test="${state==1}">
						<span class="wenzi"  onclick="updateSomes(3)" style="background-color: red"><a href="#" style="text-decoration:none"><font color="white">一键下架</font></a></span>
						<span style="color: red">(请谨慎操作，删除的图片为你已处理含中文的图片，线上下架)</span>
						</c:if>--%>
						</div>
				</div>
			</div>
		</div>
		<div class="left left-margin">
			<label><input type="checkbox" class="checkbox-all"   id="checked" onclick="fnselect()" style="width: 30px; height: 30px;">全选</label>
		</div>
		</div>
		<div class="main-table">
			<table class="table">
				<c:if test="${isdate==0}">
					<div class="div2">
						<span style="color: red;font-size: 40px">查询数据不存在</span>
					</div>
				</c:if>
				<c:forEach  var="customGoodsList"  items="${customGoodsList }"  varStatus="status">
					<div class="div">
						<img src="${customGoodsList.remotepath }" style="width:170px; height:170px;" alt="${customGoodsList.id }">
						<br/>
							<%--md5:<input type="text"  value="${customGoodsList.goodsmd5}"/>--%>

						<c:if test="${state==0}">
							<input type="checkbox"   class="cbox"  class="id"  value="${customGoodsList.id }" style="width: 30px; height: 30px;" />
						</c:if>
						<c:if test="${state==1}">
							<input type="checkbox"   class="cbox"  class="id"  value="${customGoodsList.goodsmd5 }" style="width: 30px; height: 30px;" />
						</c:if>
						</div>
				</c:forEach>
			</table>
		</div>
		
		<br>
		<div  style="margin-left:650px">
		<input type="hidden" id="totalpage" value="${totalpage}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal">${currentPage}<em>/</em> ${totalpage}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${currentPage}" style="height: 26px;">
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
	</div>
</body>
</html>
