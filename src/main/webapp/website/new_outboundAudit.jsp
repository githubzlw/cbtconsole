<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.cbt.util.AppConfig"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出库审核</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.imgbox.pack.js"></script>
<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/lrtk.css" type="text/css">
<style type="text/css">
/* 表格样式 */
table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #F4FFF4;           
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
	position: relative;
}
.oddrowcolor{
	background-color:#F4F5FF;
}
.evenrowcolor{
	background-color:#FFF4F7;            
}

/* 字体样式 */
body{font-family: arial,"Hiragino Sans GB","Microsoft Yahei",sans-serif;} 
p.thicker {font-weight: 900}

/* button样式 */
.className{
  line-height:30px;
  height:30px;
  width:70px;
  color:#ffffff;
  background-color:#3ba354;
  font-size:16px;
  font-weight:normal;
  font-family:Arial;
  border:0px solid #dcdcdc;
  -webkit-border-top-left-radius:3px;
  -moz-border-radius-topleft:3px;
  border-top-left-radius:3px;
  -webkit-border-top-right-radius:3px;
  -moz-border-radius-topright:3px;
  border-top-right-radius:3px;
  -webkit-border-bottom-left-radius:3px;
  -moz-border-radius-bottomleft:3px;
  border-bottom-left-radius:3px;
  -webkit-border-bottom-right-radius:3px;
  -moz-border-radius-bottomright:3px;
  border-bottom-right-radius:3px;
  -moz-box-shadow: inset 0px 0px 0px 0px #ffffff;
  -webkit-box-shadow: inset 0px 0px 0px 0px #ffffff;
  box-shadow: inset 0px 0px 0px 0px #ffffff;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.className:hover{
  background-color:#1c9439;
}

/* 订单号 */
.someclass {
	text-indent : 0em;
	word-spacing : 1px;
	letter-spacing : 2px;
	text-align : left;
	text-decoration : none;
	font-family : monospace;
	color : #007007;
	font-weight : bold;
	font-size : 14pt;        
	background-color : #F0F0FF;
	border-color : transparent;
}

/* 查询条件文本框 */
.querycss {
	color : #00B026;        
	font-size : 12pt;
	border-width : 1px;
	border-color : #AFFFA0;
	border-style : solid;
	height : 23px;
	width : 120px;
}


/* 一页显示select */


.classSelect{
  line-height:30px;
  height:30px;
  width:157px;
  color:#ffffff;
  background-color:#3ba354;
  font-size:16px;
  font-weight:normal;
  font-family:Arial;
  border:0px solid #dcdcdc;
  -webkit-border-top-left-radius:3px;
  -moz-border-radius-topleft:3px;
  border-top-left-radius:3px;
  -webkit-border-top-right-radius:3px;
  -moz-border-radius-topright:3px;
  border-top-right-radius:3px;
  -webkit-border-bottom-left-radius:3px;
  -moz-border-radius-bottomleft:3px;
  border-bottom-left-radius:3px;
  -webkit-border-bottom-right-radius:3px;
  -moz-border-radius-bottomright:3px;
  border-bottom-right-radius:3px;
  -moz-box-shadow: inset 0px 0px 0px 0px #ffffff;
  -webkit-box-shadow: inset 0px 0px 0px 0px #ffffff;
  box-shadow: inset 0px 0px 0px 0px #ffffff;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.classSelect:hover{
  background-color:#FAFFF4;         
}
.repalyBtn{height: 30px;width: 70px;background: #1c9439;border: 0px solid #dcdcdc;color: #ffffff;cursor: pointer;position: absolute;right:3px;bottom:2px;}
.repalyDiv{width: 500px;background: #34db51;text-align: center;position: fixed;left: 40%;top: 43%;}
.repalyBtn:hover{opacity: 1;}
.w-margin-top{margin-top:40px;}

</style>
	<%
	  String orderid=request.getParameter("orderid");
	%>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>

<%--<link rel="stylesheet" href="css/orderinfo.css" type="text/css">--%>

<script type="text/javascript">
//表格样式
function altRows(id){
	if(document.getElementsByTagName){  
		
		var table = document.getElementById(id);  
		var rows = table.getElementsByTagName("tr"); 
		 
		for(i = 1; i < rows.length; i++){          
			if(i % 2 == 0){
				rows[i].className = "evenrowcolor";
			}else{
				rows[i].className = "oddrowcolor";
			}      
		}
	}
}


//清空查询条件
function cleText(){
//	document.getElementById("idname").value="";
	$('input[type=text]').val("");
}

//扫描条码审核商品
	function checkedGoods(){
    	var orderid='<%=orderid%>';
    	var od_count=$("#od_details").val();
		var goodsid=$("#goodsid").val();
        $.ajax({
            dataType:"json",
            type: "POST",//方法类型
            url:'/cbtconsole/warehouse/checkedGoods',
            data:{orderid:orderid,goodsid:goodsid},
            dataType:'json',
            success:function(data) {
                console.log(data);
                if (data>0) {
                    $("#div_"+orderid+goodsid).css("background-color","green");
                    var check=$("#check_details").val();
                    check=Number(check)+1;
                    $("#check_details").val(check);
                    if(Number(od_count) == Number(check)){
                        //启用确认出库按钮
                        $("#inspection").attr('disabled',false);
                    }
                    document.getElementById("tip_info").innerHTML=""
                }else{
                    document.getElementById("tip_info").innerHTML="扫描的商品不是在该订单列表中"
				}
                $("#goodsid").val("");
                $("#goodsid").focus;
            }
        });
	}

	function toPackList(){
        var userid = $("#userid").val();
        var orderid = '<%=orderid%>';
        var isDropshipOrder=$("#isDropshipOrder").val();
        $.ajax({
            type:"GET",
            url:"insertOrderfee.do",
            dataType:"text",
            data:{userid:userid ,orderid:orderid ,state:4,problem:"",isDropshipOrderFlag:isDropshipOrder},
            success : function(data){
                if(data > 0){
                    window.location.href="/cbtconsole/warehouse/getOrderInfoInspection.do?userid=&orderid="+orderid+"&day=&pageSize=50&orderstruts=2&orderPosition=0";
                }else{
                    //showMsg("msgid"+orderid,"<h1 style='color: red;'>审核失败请重新审核</h1>");
					alert("审核失败请重新审核");
				}
            }
        });
    }

    function toInspection(){
        var orderid='<%=orderid%>';
        window.location.href="/cbtconsole/warehouse/getOrderInfoInspection.do?userid=&orderid="+orderid+"&day=&pageSize=50&orderstruts=1&orderPosition=0";
    }


function celsearch() {
    $("#goodsid").val('');
}

function toquery() {
	var goodsid=$("#goodsid").val();
	if(goodsid.length >= 6){
        checkedGoods();
	}
}

</script>

</head>
<body style="background-color : #F4FFF4;">
	<div align="center" >
		
		<div><H1>出库审核</H1></div>
		<div>
            <input type="hidden" id="od_details" value="${oip_size}"/>
            <input type="hidden" id="check_details" value="0"/>
			商品号:<input class="querycss" style="width : 160px;" id="goodsid" name="goodsid" type="text" onInput="toquery();" onFocus="celsearch()"  onkeypress="if (event.keyCode == 13) checkedGoods()" />
			<span id="tip_info" style="color:red;font-size:25px;"></span>
		</div>
		<div>
			<c:forEach items="${oip.allOrders}" var="strOrder" varStatus="status">
				<div>
					  
					<div>
						<table width="1600px" class="altrowstable" id="alternatecolor${status.count}">
							<tr class="someclass" ><td colspan="8"><div style="display:inline">
							订单号:${strOrder }&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
							</div>
							<div style="display:inline"><span style="display: none" id="${strOrder }">订单已取消</span></div>
							</td></tr>
	   						<tr style="background-color:#FAFFF4;">
						  		<td width="210px"><font style="font-size : 15px;">订单编号</font></td>
				  				<td width="70px"><font style="font-size : 15px;">商品编号</font></td>
			  					<td width="220px"><font style="font-size : 15px;">入库时间</font></td>
								<td width="230px"><font style="font-size : 15px;">拍照图片</font></td>
								<td width="70px"><font style="font-size : 15px;">仓库位置</font></td>
								<td width="100px"><font style="font-size : 15px;">入库状态</font></td>
								<td><font style="font-size : 15px;">入库备注</font></td>
							</tr>
							<c:forEach items="${oip.pagelist}" var="storageLocation" varStatus="s">
								<c:if test="${storageLocation.orderid == strOrder}">
									<tr id="div_${storageLocation.orderid }${storageLocation.odid }">
										<c:if test="${storageLocation.state == '-1' || storageLocation.state == '6'}">
										<script type="text/javascript">
										    showType('${storageLocation.orderid }');
										    </script>
										</c:if>
										<td><input type="hidden" id="userid" value="${storageLocation.user_id }"/>
											<input type="hidden" id="isDropshipOrder" value="${storageLocation.isDropshipOrder}"/>
											<font style="font-size : 15px;">${storageLocation.orderid }</font></td>
										<td><font style="font-size : 15px;">${storageLocation.goodid }</font></td>
										<td><font style="font-size : 15px;">${storageLocation.createtime }</font></td>
										<td >
											<c:forEach var="pic" items="${storageLocation.picList}" varStatus="m">
												<a id="example-${m.index }"  href="${pic }">
													<img width="150px" height="150px" alt="" src="${pic }" /></a>
												<script type="text/javascript">
                                                    $('#example-${m.index }').imgbox({
                                                        'speedIn'		: 0,
                                                        'speedOut'		: 0,
                                                        'alignment'		: 'center',
                                                        'overlayShow'	: true,
                                                        'allowMultiple'	: false
                                                    });
												</script>
											</c:forEach>
										</td>
									   
										<td><font style="font-size : 15px;">${storageLocation.position }</font></td>
										<td><font style="font-size : 15px;">${storageLocation.goodstatus }</font></td>
										<td>
												 <div style="overflow-y:scroll;height:150px;">
													 <div class="w-font">
														 <font style="font-size : 15px;">${storageLocation.warehouse_remark }</font>
													 </div>
												 </div>

										</td>
									</tr>
								</c:if>
							</c:forEach>        
							<tr>
                                <td colspan="7">
                                    <input type="button" style="margin-left:700px;width:100px;height:40px" value="确认出库" id="inspection" disabled="disabled" onclick="toPackList();"/>
                                    <input type="button" style="width:100px;height:40px" value="取消出库" onclick="toInspection();"/>
                                </td>
                            </tr>
						</table>                                   
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
	
</body>
</html>