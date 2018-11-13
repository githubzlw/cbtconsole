<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>出货补录界面</title>
<style type="text/css">
table.altrowstable {                      
	font-family: verdana,arial,sans-serif;
	font-size:11px;                              
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
</style>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/kkpager/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
function add(){
	$("#alternatecolor").append(
			"<tr class='addinfo'>"+
			"<td><font  style='font-size : 15px;'><input   type='text'  name='orderid'></font></td>"+
			"<td><font  style='font-size : 15px;'><input   type='text'  name='remarks'></font></td> "+      
			"<td><font  style='font-size : 15px;'><input   type='text'  name='expressno'></font></td> "+        
			"<td>"+
			"<select  name='transport'>  "+
				"<option value='原飞航'  selected='selected'>原飞航</option>"+
				"<option value='JCEX'>JCEX</option>"+
				"<option value='emsinten'>emsinten</option>"+
				"<option value='shunfeng'>shunfeng</option>"+
				"<option value='ups'>ups</option>"+
				"<option value='DHL'>DHL</option> "+
				"<option value='TNT'>TNT</option> "+
				"<option value='Dpex'>Dpex</option> "+
				"<option value='Fedex'>Fedex</option> "+
				"<option value='yodel'>yodel</option> "+                 
		     "</select>                   "+                        
			"<td><font  style='font-size : 15px;'><input   type='text'  name='weight'>(kg)</font></td>"+
			"<td><font  style='font-size : 15px;'><input   type='text'  name='svolume'>(cm<sup>3</sup>)</font></td>"+
		    "<td><font  style='font-size : 15px;'><input   type='text'  name='estimatefreight'></font></td>"+
			"<td><font  style='font-size : 15px;'><input  class='form-control' type='date' name='createtime'  style='width: 155px'></font></td>"+
			"<td><button  onclick ='add()'>添加</button></td>"+
	       "</tr>");
}


function   submit(){
	var bgList= new Array(); 
	 var i = 0;
  	$(".addinfo").each(function(){
	      var orderid = $(this).find("input[name='orderid']").val();
	      var remarks = $(this).find("input[name='remarks']").val();
	      var expressno = $(this).find("input[name='expressno']").val();
	      var transport = $(this).find("select").val();
	      var weight = $(this).find("input[name='weight']").val();
	      var svolume = $(this).find("input[name='svolume']").val();
	      var estimatefreight = $(this).find("input[name='estimatefreight']").val();
	      var createtime = $(this).find("input[name='createtime']").val();
	      if(orderid.length==0||remarks.length==0||expressno.length==0||transport.length==0||weight.length==0||svolume.length==0||estimatefreight.length==0){
	    	  return true;
	      }
	      var orderMap={};                    
			orderMap['orderid']= orderid;
			orderMap['remarks']= remarks;
			orderMap['expressno']= expressno;
			orderMap['transport']= transport;                           
			orderMap['weight']= weight;                  
			orderMap['svolume']= svolume;          
			orderMap['estimatefreight']= estimatefreight;  
			orderMap['createtime']= createtime;  
			bgList[i] = orderMap; 
			i++;
	})
	if(bgList.length>0){
		var  mainMap = {};
		mainMap['bgList']=bgList;        
		console.log(mainMap); 
		 $.ajax({
			type:"post",
			url:"/cbtconsole/warehouse/makeup",
			contentType : 'application/json;charset=utf-8', 
			data:JSON.stringify(mainMap),
			dataType:"json",
			success:function(res){
				if(res>0){
					alert("后台插入成功!!");
				}else{
					alert("后台插入失败 !!")
				}
			}
		})
	}else{
		alert("数据都不能为空 !!");
	}
	
	
}

function reset(){
	$(".addinfo").find("input").val("");
}


</script>
</head>
<body>
<div align="center"><H1>出货补录界面</H1></div>
<div align="center">
 <table class="altrowstable" id="alternatecolor">
		<tr style="background-color:#FFECE4;">
			<td><font style="font-size : 15px;">订单编号(*)</font></td>
			<td><font style="font-size : 15px;">合并订单(*)</font></td>
			<td><font style="font-size : 15px;">快递跟踪号(*)</font></td>
			<td><font style="font-size : 15px;">出货方式</font></td>
			<td><font style="font-size : 15px;">打包重量(*)</font></td>
			<td><font style="font-size : 15px;">打包体积(*)</font></td>
			<td><font style="font-size : 15px;">预估运费(*)</font></td>
			<td><font style="font-size : 15px;">出货时间</font></td>
<!-- 			<td><font style="font-size : 15px;"></font></td> -->
		</tr>
		<tr  class ='addinfo'>
			<td><font  style="font-size : 15px;"><input   type="text"  name="orderid" ></font></td>
			<td><font  style="font-size : 15px;"><input   type="text"  name="remarks"></font></td>       
			<td><font  style="font-size : 15px;"><input   type="text"  name="expressno"></font></td>         
			<td>
			<select  name="transport">  
				<option value="原飞航"  selected="selected">原飞航</option>
				<option value="JCEX">JCEX</option>
				<option value="emsinten">emsinten</option>
				<option value="shunfeng">shunfeng</option>
				<option value="ups">ups</option>
				<option value="DHL">DHL</option> 
				<option value="TNT">TNT</option> 
				<option value="Dpex">Dpex</option> 
				<option value="Fedex">Fedex</option> 
				<option value="yodel">yodel</option>                  
		     </select>                                           
			</td> 
			<td><font  style="font-size : 15px;"><input   type="text"  name="weight">(kg)</font></td>
			<td><font  style="font-size : 15px;"><input   type="text"  name="svolume">(cm<sup>3</sup>)</font></td>
		    <td><font  style="font-size : 15px;"><input   type="text"  name="estimatefreight"></font></td>
			<td><font  style="font-size : 15px;"><input  class="form-control" type="date" name="createtime"  style="width: 155px"></font>
			</td>
<!-- 			<td><button  onclick ='add()'>添加</button></td> -->
	 </tr>
</table>
</div>
<div align="center"  style="margin-top:50px;">
<input type="button"  name="submit"  value="提交" onclick ="submit()"  style="width:50px;height:30px;">
<input type=button  name="reset"  value="重置"  onclick ="reset()"  style="width:50px;height:30px;">
</div>




<div  style="margin-top:150px;margin-left:160px;">

<h2>参考实例:</h2>
<table class="altrowstable" id="alternatecolor"  >
		<tr style="background-color:#FFECE4;">
			<td><font style="font-size : 15px;">订单编号(*)</font></td>
			<td><font style="font-size : 15px;">合并订单(*)</font></td>
			<td><font style="font-size : 15px;">快递跟踪号(*)</font></td>
			<td><font style="font-size : 15px;">出货方式</font></td>
			<td><font style="font-size : 15px;">打包重量(*)</font></td>
			<td><font style="font-size : 15px;">打包体积(*)</font></td>
			<td><font style="font-size : 15px;">预估运费(*)</font></td>
			<td><font style="font-size : 15px;">出货时间</font></td>
		</tr>
		<tr>
			<td><font  style="font-size : 15px;">P320888598290783</font></td>
			<td><font  style="font-size : 15px;">P320888598290783,</font></td>       
			<td><font  style="font-size : 15px;">867483890</font></td>         
			<td>
			<select  name="transport">  
				<option value="原飞航"  selected="selected">JCEX</option>
		     </select>                                           
			</td> 
			<td><font  style="font-size : 15px;">4.29(kg)</font></td>
			<td><font  style="font-size : 15px;">35*21*26(cm<sup>3</sup>)</font></td>
		    <td><font  style="font-size : 15px;">52</font></td>
			<td><font  style="font-size : 15px;">2017-03-30 14:01:42</font></td>
	 </tr>
</table>
</div>
</body>
</html>