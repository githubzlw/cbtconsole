<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自动测量</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>

<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/thelibrary.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.messager.js"></script>


<link rel="stylesheet" href="/cbtconsole/css/warehousejs/measure.css" type="text/css">
<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<!--  -->
<script type="text/javascript">                             
var i=0; //td id
//包裹信息
function getPackageInfo(){
	var packageNo = $("#packageNo").val();
	
	//是否存在
	
	var si = $("input[id='"+packageNo+"']").size();
	//alert(si);                                                         
	if(si>0){                     
		return;        
	}                                           
	                            
	$.ajax({
		type:"post", 
		url:"/cbtconsole/warehouse/getPackageInfo",
		dataType:"json",                                                       
		data:{shipmentno : packageNo}, 
		success : function(data){    
			$("#packageNo").val("");            
			if(data != null){           
				var svolumeArray = new Array();
				svolumeArray[0] = '';          
				svolumeArray[1] = '';
				svolumeArray[2] = '';

				if(data.svolume!='' &&data.svolume!=null){
					svolumeArray = data.svolume.split("*");
				}
				                                  
				var tr =""+
				"<tr id='tr"+i+"'> "+
				"	<td><input type='text' id='packageNo"+i+"' value='"+packageNo+"' style='width: 50px' disabled='disabled'/><input type='hidden' id='"+packageNo+"' value='"+packageNo+"' /></td>"+            //包裹号
				"	<td><input type='text' id='weight"+i+"' value='"+data.sweight+"' /></td>"+											           //重量
				"	<td>"+
				"		<input type='text' id='volumeLength"+i+"' value='"+svolumeArray[0]+"'  style='width: 50px'/>*"+                                     //体积
				"		<input type='text' id='width"+i+"'  value='"+svolumeArray[1]+"' style='width: 50px'/>*"+
				"		<input type='text' id='height"+i+"'  value='"+svolumeArray[2]+"' style='width: 50px'/>"+
				"	</td>"+
				"	<td><input type='button' value='删除'  onclick='delRow("+i+")'/></td>"+
				"</tr>";
				
				$("#tabId").append(tr);
				i++;//id加1
			}
		}
	});              
}

//删除行
function delRow(id){
	$("#tr"+id).remove();
}

//保存所有
function saveAll(){
	var volumeLength = "";
  var width ="";
  var height = "";
  
  var listmap= new Array();
	var index=0;
  for(var j=0; j<i; j++){
  	
  	if ( typeof($("#tr"+j).html()) != "undefined" ){ 
  		volumeLength = $("#volumeLength"+j).val();
  		width = $("#width"+j).val();
  		height = $("#height"+j).val();
  		
  		var map = {}; 
  		map['shipmentno'] = $("#packageNo"+j).val();
  		map['sweight'] = $("#weight"+j).val();
  		map['svolume'] = volumeLength+"*"+width+"*"+height;
  	

  		map['volumeweight']  = ((volumeLength * width * height) / 5000).toFixed(3);
  		
  		listmap[index] = map;
  		index++;
  	}
  }                  
 // console.log(listmap);
  var mainMap={};  
	mainMap['listmap']=listmap;  
	$.ajax({
		type:"post", 
		url:"/cbtconsole/warehouse/saveALl",
		dataType:"json",  
	    contentType : 'application/json;charset=utf-8', 
	    data:JSON.stringify(mainMap),
		success : function(data){  
			if(Number(data)>0){
				$("#msginfo").html("<h1>保存成功！</h1>");
				window.setTimeout(function(){ 
					history.go(0);
				},1500);
			}else{
				$("#msginfo").html("<h1>失败！</h1>");
				window.setTimeout(function(){ 
					$("#msginfo").html(""); 
				},1500); 
			}
			
		}
	});
}
function asdf(){
	alert("132");             
}
</script>

</head>
<body style="background-color : #F4FFF4;" onclick="">
<div align="center">
	<div><h1>自动测量</h1></div>
	<div id="msginfo"></div>               
	<br/><br/>                      
	<!-- 扫描 -->
	<div style="width: 800px">
		出库号:<input id="packageNo" type='text' onkeypress="if (event.keyCode == 13) getPackageInfo()"/><input type="button" value="扫描" onclick="getPackageInfo()"/>
	                           
		<input type="button" value="保存所有" onclick="saveAll()" />
		                      <input type="hidden"/>
	</div>         
	
	<!-- 表格 -->
	<div>
		<table id="tabId" class="altrowstable" style="width: 800px">
			<tr>
				<td>出库号</td>
				<td>重量(KG)</td>
				<td>体积(CM<sup>3</sup>)</td>
				<td>操作</td>
			</tr>
			
		</table>
	</div>
</div>

</body>

</html>