
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.util.Redis"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4cbtconsole/WebsiteServlet?action=getOrderDetail/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<meta http-equiv=Content-Type content="text/html; charset=utf-8">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<script type="text/javascript" src="/cbtconsole/js/website/ordermgr.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
		src="/cbtconsole/js/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
<script type="text/javascript"
		src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>订单管理</title>
<script type="text/javascript">
var page = parseInt(<%=request.getAttribute("page")%>, 0);
var count = parseInt(<%=request.getAttribute("count")%>, 0);
<%--var buyuser = <%=request.getAttribute("buyuser")%>;--%>
var buyuser = <%=request.getAttribute("admuserid_str")%>;
var admuserid = <%=request.getAttribute("admuserid")%>;
var showUnpaid = <%=request.getAttribute("showUnpaid")%>;
var strm = <%=request.getAttribute("strm")%>;
var strname = <%=request.getAttribute("strname")%>;
<%
String sessionId = request.getSession().getId();
String authJson = Redis.hget(sessionId, "userauth");
List<Object> authlist = SerializeUtil.JsonToList(authJson, AuthInfo.class);
String userJson = Redis.hget(sessionId, "admuser");
Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
int uid = 0;
if(user !=null){
	uid = user.getId();
}%>

function fn(va) {
	
	 var show_changeState = admuserid == '0'?'display:block':'display:none';
	var orderlist = "<%=request.getAttribute("ordernolist") %>";
	var strRoletype =<%=request.getAttribute("strm")%>;
	 var strAdmid = <%=request.getAttribute("strname")%>;
	 var adminName = '<%=request.getAttribute("admName")%>';
	 getAllAdmuser(strm,strname,adminName);	
	 var admid=<%=uid%>;
	var json = <%=request.getAttribute("orderws")%>;
	var intadmid;
	var row = 1;
	$("#date").find('tr:gt(0)').remove();
	$("#table tr:gt(0)").remove();
	var myDate = new Date();
	var nowDate = myDate.getFullYear();
	nowDate = nowDate + "-" + (myDate.getMonth()+1);
	nowDate = nowDate + "-" + myDate.getDate(); 
	for (var i = json.length - 1; i >= 0; i--) {
		var orderColor ="";
		if(json[i].changenum==0){
			orderColor = "";
		}else if(json[i].changenum==1){
			orderColor = "#FF8484;color:white";
		}else if(json[i].changenum==2){
			orderColor = "";
		}
		$("#table tr:eq(" + (row - 1) + ")").after(
				"<tr id='tr_"+json[i].order_no+"'></tr>");
		//项目标识
		var paytype = json[i].paytype;
        //订单是否有申诉
        var ref_orderid = json[i].ref_orderid;
		var paystatus = json[i].paystatus;
		var isDropship = json[i].isDropshipOrder;
		var orderRemark =json[i].orderRemark;
		var addressFlag=json[i].addressFlag;
		var showImg = "";
		if(paytype == 1){
			showImg += "<span class='spiconbl'>Wire</span>";
		}
		if(paytype == 2 ){
			showImg += "<span class='spiconbl'>余额付</span>";
		}if(paytype == 3 ){
			showImg += "<span class='spiconbl'>补货订单</span>";
		}
		if(paystatus == 2){
			showImg += "<span class='spiconbl'>电汇</span>";
		}
        if(addressFlag == 1){
            showImg += "<span class='spiconbl' style='color:red'>付款异常(国家不一致)</span>";
        }
        if(addressFlag == 2){
            showImg += "<span class='spiconbl' style='color:red'>无付款信息</span>";
        }
        if(addressFlag == 3){
            showImg += "<span class='spiconbl' style='color:red'>B2B库存</span>";
        }
        if(json[i].ordertype == 3){
            showImg += "<span class='spiconbl' style='color:red'>dropship 国内库存订单</span>";
        }
        if(json[i].backList>0){
            showImg += "<span class='spiconbl' style='color:red'>用户黑名单</span>";
		}
        if(json[i].payBackList>0){
            showImg += "<span class='spiconbl' style='color:red'>支付账号黑名单</span>";
        }
        if(json[i].backAddressCount>0){
            showImg += "<span class='spiconbl' style='color:red'>订单城市黑名单</span>";
        }
		if(isDropship == 1){
			showImg += "<img style='width: 20px;' title='dropship' src='/cbtconsole/img/ds1.png'>";
		}
		if(json[i].product_cost > 200){
			showImg += "<span class='spiconbl'>大额</span>";
		}
		if(paytype == 5){
            showImg += "<span class='spiconbl'>支付失败</span>";
		}
        if(paytype == 4){
            showImg += "<span class='spiconbl'>支付pending</span>";
        }
		//Added <V1.0.1> Start： cjc 2018/10/13 15:41 TODO 如果有申诉确保可以连接到申诉页面
		////cbtconsole/complain/searchComplainByParam?userid=24940&creatTime=&complainState=-1&username=&toPage=1&currentPage=1
		//End：
        if(ref_orderid > 0){
            showImg += "<span class='spiconbl' style='color:red'><a href='/cbtconsole/complain/searchComplainByParam?userid="+json[i].user_id+"&creatTime=&complainState=-1&username=&toPage=1&currentPage=1' style='color:red' title='请点击啊！'>有申诉</a></span>";
        }
		if(json[i].state == -1 || json[i].state == 6){
			if(json[i].state == -1){
				showImg += "<span class='spiconbl'>已取消(内)</span>";
			}else{
				showImg += "<span class='spiconbl'>已取消(客)</span>";
			}
		}
		if(json[i].preferential_applications > 0){
			showImg += "<span class='spiconbl'>批量</span>";
		}
        //支付或者创建时间
		var createtime = json[i].createtime;
		var delivery_time = fnAddDate(createtime,(parseInt(json[i].delivery_time)));
		if(json[i].state == 5 && fnComparisonDate(fnAddDate(createtime,2),nowDate)){
            showImg += "<span class='spiconbl'>超期</span>";
		}else if(json[i].state == 2 && fnComparisonDate(fnAddDate(createtime,(parseInt(json[i].delivery_time)-2)),nowDate)){
            showImg += "<span class='spiconbl'>超期</span>";
		}else if(json[i].state == 3 && fnComparisonDate(delivery_time,nowDate)){
            showImg += "<span class='spiconbl'>超期</span>";
		}
		// if(json[i].purchase > 0 && json[i].state == 5){
		// 	showImg += "<span class='spiconbr'>货源问题</span>";
		// }
		if(json[i].deliver > 0){
			showImg += "<span class='spiconbr'>出货问题</span>";
		}
	    if(orderRemark!="" && orderRemark!=undefined){
			showImg += "<span class='spiconbl'>有留言</span>";
		} 
	    if(json[i].message_read>0){
			showImg += "<span class='spiconbl'>客户有新回复</span>";
		}
		//Added <V1.0.1> Start： cjc 2019/6/4 11:56:48 Description : 添加删除订单
		var checkType='${param.type}';
		var requestType = '<%=request.getAttribute("type")%>';
		var thisOrderNo = json[i].order_no;
		var delectImg = "<span class='spiconbl' onclick='delOrderinfo("+thisOrderNo+",this)'>删除</span>"
		if("order_pending" != checkType && "order_pending" != requestType){
			delectImg = "";
		}
		//End：
		$("#table tr:eq(" + row + ")").append("<td>" + (i + 1) + "</td>");
		$("#table tr:eq(" + row + ") td:eq(0)").after("<td id='"+json[i].order_no+"'>" + showImg + "</td>");
		//订单号
		$("#table tr:eq(" + row + ") td:eq(1)").after("<td   style='background-color:" + orderColor + ";'><a target='_blank' href='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="
				+ thisOrderNo + "&state=" + json[i].state + "&username=" + json[i].username
				+ "&paytime=" + json[i].createtime.split(':')[0] + "&allFreight=" + Number(json[i].allFreights).toFixed(2) + "'>" + json[i].order_no + "</a>" +
				delectImg +
				"</td>");
		//支付类型
        $("#table tr:eq(" + row + ") td:eq(2)").after("<td style='text-align:center;vertical-align:middle;'>"+json[i].paytypes+"</td>");
		//用户id
		$("#table tr:eq(" + row + ") td:eq(3)").after("<td><a target='_blank'  href='/cbtconsole/userinfo/getUserInfo.do?userId=" + json[i].user_id + "'>" + json[i].user_id + "<a></td>");
		//订单量
		$("#table tr:eq(" + row + ") td:eq(4)").after("<td style='text-align:center;vertical-align:middle;'>" + json[i].order_count + "</td>");
		//邮件
		$("#table tr:eq(" + row + ") td:eq(5)").after("<td><a target='_blank'  href='/cbtconsole/website/user.jsp?userid="+ json[i].user_id + "'>"+ json[i].email +"</a>" +
			"<a  target='_blank' href='http://192.168.1.27:8089/LookUseremail?email="+json[i].email+"' style='color: red'>("+json[i].emailcount+")</a></td>");
		//支付时间
		var paytime_ = (createtime.split(':')[0]).substring(5, 17);
		$("#table tr:eq(" + row + ") td:eq(6)").after("<td>"+ paytime_+ ":"+ createtime.split(':')[1]+ "</td>");
		//运输失效
		var delaytime = "";
		if(json[i].mode_transport != null){
			delaytime = json[i].mode_transport.split('@')[1];
		}
		$("#table tr:eq(" + row + ") td:eq(7)").after("<td style='text-align:center;vertical-align:middle;'>"+ (json[i].delivery_time == null || json[i].delivery_time == ''?"-":json[i].delivery_time) + "</td>");//国内准备时间
		$("#table tr:eq(" + row + ") td:eq(8)").after("<td style='text-align:center;vertical-align:middle;'>"+ (delaytime == null || delaytime == ''?"-":delaytime) + "</td>");
		//产品金额
		$("#table tr:eq(" + row + ") td:eq(9)").after("<td>" + (json[i].product_cost == null || json[i].product_cost ==''?"0":json[i].product_cost)+ "&nbsp;" + json[i].currency+ "</td>");
		//当前状态
		var state = json[i].state;
        //验货数量
		var checked = json[i].checked;
        //商品数量
		var countOd = json[i].countOd;
        //是否有问题验货
		var problems = json[i].problems;
        //是否有问题验货
		var no_checked = json[i].no_checked;
        //添加国家
		var  countrys  = json[i].countrys;
		var state_text = "";
		var color = "";
		var color1 ="";
		if (state == "0") {
			state_text = "等待付款";
		} else if (state == "1") {
			state_text = "购买中";
			color = "#93926C;color:white";
		} else if (state == "3") {
			state_text = "出运中";
			color = "#428484;color:white";
		} else if (state == "4") {
			state_text = "完结";
			color = "#008442;color:white";
		} else if (state == "5") {
			state_text = "订单审核";
			color = "#FF8484;color:white";
		} else if (state == "-1") {
			state_text = "后台取消";
			color = "";
		} else if (state == "2") {
			if(Number(checked)==Number(countOd)){
				state_text = "已到仓库,验货无误";
				color = "#97C730;color:white";
			}else if(Number(no_checked)>0){
				state_text = "已到仓库，未校验";
				color = "#cca4e3;color:white";
			}else if(problems!="0"){
				state_text = "已到仓库，校验有问题";
				color = "#FF8C00;color:white";
			}else{
				state_text = "已到仓库，状态错误";
				color = "#FF8C00;color:red";
			}
		} else if (state == "6") {
			state_text = "客户取消";
			color = "#00FFFF;";
		} 
		if (countrys == "INDIA"){
			color1 = "yellow;";
		}
		$("#table tr:eq(" + row + ") td:eq(10)").after("<td style='background-color:"+color+";'>"+ state_text + "</td>");
		//运单状态   undefined-未出货 -备货中；2-已发货；3-已签收；4-退回；5-异常；6-内部异常;7-手动标记为正常',
        var trackStateHtm = '';
        var trackStateColor = "";
        var trackState = json[i].track_state;
        if(trackState != undefined && trackState != '-1'){
            switch (trackState){
                case '7':
                    trackStateHtm = '手动标记为正常';
                    trackStateColor = '#428484;color:white';
                    break;
                case '6':
                    trackStateHtm = '内部异常';
                    trackStateColor = '#FF00FF;color:white';
                    break;
                case '5':
                    trackStateHtm = '异常';
                    trackStateColor = '#FF00FF;color:white';
                    break;
                case '4':
                    trackStateHtm = '退回';
                    trackStateColor = '#FF00FF;color:white';
                    break;
                case '3':
                    trackStateHtm = '已签收';
                    trackStateColor = '#008442;color:white';
                    break;
                default:
                    trackStateHtm = '已发货';
                    trackStateColor = '#428484;color:white';
                    break;
            }
        } /*else {
            trackStateHtm = '备货中';
        }*/
        $("#table tr:eq(" + row + ") td:eq(11)").after("<td style='background-color:"+trackStateColor+";'>"+ trackStateHtm + "</td>");
        //订货国家
		$("#table tr:eq(" + row + ") td:eq(12)").after("<td   id='custCountry"+json[i].order_no+"'  >" + (json[i].countrys == null || json[i].countrys == ''?"-":json[i].countrys)+ "</td>");
		//预估国际运费/实际称重预估运费
        $("#table tr:eq(" +row + ") td:eq(13)").after("<td>" + Number(json[i].allFreights).toFixed(2)+ "/"+Number(json[i].estimatefreight).toFixed(2)+"</td>");
		//显示订单总数量,采购数量,入库数量
        //入库数量
		var  NOWarehouses  = json[i].number_of_warehouses;
		//采购数量
		$("#table tr:eq(" + row + ") td:eq(14)").after("<td style='font-size:19px;text-align:center;vertical-align:middle;'>" + NOWarehouses + "/"+ problems + "/" + json[i].purchase_number + "/"+ json[i].details_number+ "</td>");
		if (Number(json[i].purchase_number) >= Number(json[i].details_number) && Number(json[i].details_number) != 0) {
			$("#table tr:eq(" + row + ") td:eq(15)").css("background-color", "yellow");
		}
		if (Number(NOWarehouses) >= Number(json[i].details_number) && Number(json[i].details_number) != 0) {
			$("#table tr:eq(" + row + ") td:eq(15)").css("background-color", "#FF00FF");
		}
		var client_update = json[i].client_update;
		var server_update = json[i].server_update;
		if (server_update > 0 && state == "5" && server_update == 1) {
            $("#table tr:eq(" + row + ")").css("color", "#0094C0");
		}else if (server_update > 0 && state == "5") {
            $("#table tr:eq(" + row + ")").css("color", "#0084FF");
		}
        //入库情况汇总
        // $("#table tr:eq(" + row + ") td:eq(15)").after("<td></td>");
		var user = json[i];
		$("#table tr:eq(" + row + ") td:eq(15)")
		.after("<td id='tduser"+json[i].user_id+"'><select id=\"select"+json[i].id+"\" class=\"select"+json[i].user_id+"\" onchange=\"gradeChange("
								+ json[i].id+","+ json[i].user_id+",'"+ json[i].email+"',"+i+ ",'"+json[i].username+"','"+json[i].order_no+"')\"  name='user"
								+ json[i].id+ "'>"+ str_personCharge
								+ "</select><span id='confirm"+i+"' style=\"display:none;\" value=\"更新成功\" \"/></td>");
		
		//选中的值
		if (json[i].adminid != '') {
			$("#select" +json[i].id+ " option[flag='"+ json[i].adminid + "']").attr('selected', 'selected');
			admName = user.adminname;
		}
		if(strRoletype !=0){
			$("#select" +json[i].id).attr('disabled', 'disabled');
		}
		uname = user.username;
		uemail = user.email;
        var emailFlag = json[i].emailFlag;
		if(adminName=="ling"){
		    var html_="<td style='"+show_changeState+"'><a target='_blank' href='javascript:void(0)' "
                +"onclick='window.open(\"/cbtconsole/website/updateorderstate.jsp?orderNo="
                + json[i].order_no+ "&state="+ state + "\",\"windows\",\"height=280,width=530,"
                +"top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no\")'>修改</a>";
		    if(checkType == "checkOrder"){
		        if(emailFlag>0){
                    html_+="<br><a target='_blank' style='color:red'  href='javascript:openCheckEmailForUser(\""+json[i].order_no+"\",\""+json[i].email+"\");'>提醒客户</a>";
				}else{
                    html_+="<br><a target='_blank'  href='javascript:openCheckEmailForUser(\""+json[i].order_no+"\",\""+json[i].email+"\");'>提醒客户</a>";
				}
			}
            html_+="</td>";
			$("#table tr:eq(" + row + ") td:eq(16)").after(html_);
		}else{
		    var html_="";
            if(checkType == "checkOrder"){
                if(emailFlag>0){
                    html_+="<br><a target='_blank' style='color:red'  href='javascript:openCheckEmailForUser(\""+json[i].order_no+"\",\""+json[i].email+"\");'>提醒客户</a>";
                }else{
                    html_+="<br><a target='_blank'  href='javascript:openCheckEmailForUser(\""+json[i].order_no+"\",\""+json[i].email+"\");'>提醒客户</a>";
                }
            }
			$("#table tr:eq(" + row + ") td:eq(16)").after("<td>"+html_+"</td>");
		}
		if(json[i].orderremarks==undefined || json[i].orderremarks=="null" || json[i].orderremarks==""){
			$("#table tr:eq(" + row + ") td:eq(17)").after("<td>无</td>");
		}else{
			$("#table tr:eq(" + row + ") td:eq(17)").after("<td><div style='width: 95%;margin:5px;height: 38px;overflow-y:scroll;overflow-x:hidden;'>"+json[i].orderremarks+"</div></td>");
		}
	}
	$("#counto").html(count);
	count = Math.ceil(count / 40);
	$("#count").html(count);
	$("#page").html(page);
    $("#adminusersc").val(buyuser);
	admName='<%=request.getAttribute("admName")%>';
	var roletype='<%=request.getAttribute("roletype")%>';
	if(roletype != "0"){
        $("#adminusersc").val(admuserid);
	}
	//如果只有一条数据，则打开该订单详情页面
	if(json.length==1){
		window.open("/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+ json[0].order_no+ "&state=5&paytime="+ json[0].createtime.split(':')[0]);
	}
  myfunction();
}
//改变销售负责人
function gradeChange(id,userid,email,ids,userName,orderNo) {
	$("#select" +id).attr('disabled', 'disabled');
	var stradmid_=$("#select"+id).find("option:selected").val();
	var stradmName_=$("#select"+id).find("option:selected").text();
	addUser(stradmName_,stradmid_,email,userid,ids,userName,orderNo);
	//改变销售人后将下拉框置灰5秒whj
	window.setTimeout(function(){ 
		$("#select" +id).removeAttr("disabled"); 
		},5000);

}

function   addUser(stradmName_,id,email, uid,ids,userName,orderNo) {
	var label1 = document.getElementById("confirm" + ids);
    $.ajax({
        url: "/cbtconsole/order/addUser",
        type:"POST",
        data : {"adminid":id,"userid":uid,"admName":stradmName_,"userName":userName,"email":email,"orderNo":orderNo},
        dataType:"json",
        success:function(data){
			if(data>0){
                $(".select" + uid +" ").val(id);
                label1.innerHTML ="更新成功";
                label1.style.display = "block";
			}else{
                label1.innerHTML ="更新失败";
                label1.style.display = "block";
			}
            window.setTimeout(function(){
                label1.style.display = "none";
            },3000);
        }
    });
	}
//获取负责人列表
var str_personCharge = '';
function getAllAdmuser(strRoletype,strAdmid,adminName) {
	var str = '';
	var buysrt = '';
	var sellAdm = eval('<%=request.getAttribute("sellAdm")%>');
	var purchaseAdm = eval('<%=request.getAttribute("purchaseAdm")%>');
	var buyuser_par = '<%=request.getAttribute("buyuser")%>';
	var admuserid_par = '<%=request.getAttribute("admuserid")%>';
	if(admuserid_par == "1"){
		$("#change_state_tr").show();
	}
	buysrt = buysrt + '<option value="0">全部</option>';
	for (var i = 0; i < sellAdm.length; i++) {
		if(strRoletype==0){
			if(str=='') {
				str = str + '<option value="0" ' + change + ' >全部</option>';
			}
			 str += '<option value="'+sellAdm[i].id+'" ' + change + ' flag="'+sellAdm[i].id+'">'
				+ sellAdm[i].confirmusername + '</option>';
		} else if(admuserid_par==sellAdm[i].id && strRoletype!=0 ){
			str += '<option value="'+sellAdm[i].id+'"">'
			+ sellAdm[i].confirmusername + '</option>';
		}
	}
	
	for (var i = 0; i < purchaseAdm.length; i++) {
		var change = '';
		if(buyuser_par == purchaseAdm[i].id){
			change = 'select="selected"';
		}
		buysrt += '<option value="'+purchaseAdm[i].id+'" ' + change + ' flag="'+purchaseAdm[i].confirmusername+'">'
				+ purchaseAdm[i].confirmusername + '</option>';
	}

		str_personCharge = str.replace("全部", "");
		$('#adminusersc').html(str);
		if(strRoletype !=0){
			$('#adminusersc').attr('disabled', 'disabled');
		}
		$('#buyuser').html(buysrt);
}
$(document).ready(function(){ 
	fn(1);
	fnGetStatistic();
	fnGetMessage(<%=  "0".equalsIgnoreCase(user.getRoletype())? 1 : uid%>); //获取各种消息数量
    uidTem = <%= "0".equalsIgnoreCase(user.getRoletype()) ? 1 : uid%>;
}); 

</script>
<style type="text/css">
.nomessleft p {height: 40px;text-align: left;}
.nomesname {font-size: 20px;background-color: #D2BBB5}
.spiconbl {display: inline-block;padding: 1px;border: 1px solid #00afff;border-radius: 3px;font-size: 12px;margin: 2px;}
.spiconbr {display: inline-block;padding: 1px;border: 1px solid #f00;border-radius: 3px;font-size: 12px;margin: 2px;}
.ordtoptable {/* width: 1250px; */}
.ordtoptable td {border: 1px solid #777;padding: 5px 0;text-align: center;}
.tabletoptoal td {text-align: center;}
#table tr td{height:38px;word-break: break-all;}
.otherpage a{background-color: #46f11e;font-size: 16px;font-weight: 500;color: black;}
.mod_pay3 {
	width: 720px;
	position: fixed;
	top: 250px;
	left: 15%;
	z-index: 1011;
	background: gray;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
	border: 15px solid #33CCFF;
}
</style> 
</head>
<body>
<div class="mod_pay3" style="display: none;" id="checkDiv">
	<div>
		<a href="javascript:void(0)" class="show_x"
		   onclick="closeCheckEmailForUser();">╳</a>
	</div>
	<input id="checkOrderid" type="hidden" value=""> <input
		id="checkEmail" type="hidden" value="">
	回复内容:
	<textarea name="checkRemark" rows="8" cols="50"
			  id="checkRemark"></textarea>
	<input type="button" id="repalyBtnId" onclick="sendCheckEmailForUser()"
		   value="发送邮件">
	<input type="button" onclick="closeCheckEmailForUser();" value="关闭">
</div>
		<div align="center">

			<div style="text-align: center;" class="otherpage">
				<a href="/cbtconsole/website/user.jsp"
					target="_blank">用户信息管理</a>
				<a href="/cbtconsole/website/customerinfo.jsp" target="_blank">客户信息综合查询</a>
				<a href="/cbtconsole/website/user_statistics.jsp" target="_blank">网站用户行为</a>
					<a
					href="/cbtconsole/refundss/getAllRefundApply" target="_blank">退款处理</a>
					<a
					href="/cbtconsole/website/pre_purchase.jsp"
					target="_blank">综合采购</a> <c:if test="${admuserid == 0}"><a href="/cbtconsole/website/shipmentcount.jsp"
					target="_blank">30天完成出货订单统计</a></c:if>
			</div>
			<%  if(!"0".equals(user.getRoletype())){%>
				<!-- 载入消息提醒jsp页面 -->
				<jsp:include page="message_notification.jsp"></jsp:include>
			<%}  %>

			<br />
			<div class="usetablediv">
				<a style="float: left;"
					href="/cbtconsole/order/getOrderInfo.do?showUnpaid=0">Back</a>
				<!-- <label>消息：<span id="assignment" class="bt btn-info">客户留言客服分配</span></label> -->
				<div class="row">
					<div class="nomessleft" style="width: 90%; margin: 0 auto;">
						<table class="ordtoptable">
							<tr>
								<td class="nomesname"><span style="font-size: 16px;">产品单页客户<br />折扣和定制需求<br />(Business inquiries)</span></td>
								<td class="nomesname"><span style="font-size: 16px;">购物车&支付<br />页面问题统计<br />(ask here)</span></td>
								<td class="nomesname"><span style="font-size: 16px;">产品单页用户<br />留言(Customer <br />Questions & Answers)</span></td>
                                <td class="nomesname"><span style="font-size: 16px;">订单需沟通<br />条数</span></td>
								<td class="nomesname">购物车营销(暂停用)</td>
								<td class="nomesname">商业询盘</td>
								<!-- <td class="nomesname">批量优惠申请(暂停用)</td> -->
								<td class="nomesname">投诉管理</td>
								<td class="nomesname">未确认进账订单</td>
<!-- 								<td class="nomesname">到账但未生成的订单</td> -->
								<td class="nomesname">注(颜色表示)</td>
								<td class="nomesname" ><span style="font-size: 16px;">清除表头session</br>信息并刷新数据</span></td>
							</tr>
							<tr>

                                <!-- 产品单页客户折扣和定制需求 -->
								<td>
                                    <span>
                                        <input type="hidden" id="style" value="noDelete">
                                        <span id="propagemessage" class="btn btn-success btnto"></span>
								    </span>
								    <input type="hidden" id="type" value="propagemessage">
								</td>
								
								<!-- 购物车&支付页面问题统计 -->
								<td>
									<span>
										<input type="hidden" id="style" value="noDelete"> 
										<span id="customerInfoCollection" class="btn btn-success btnto"></span>
									</span> 
									<input type="hidden" id="type" value="customerInfoCollection">
								</td>
								
								<!-- 产品单页用户留言(Customer Questions & Answers) -->
								<td>
									<span>
										<input type="hidden" id="style" value="noDelete"> 
										<span id="questionnum" class="btn btn-success btnto"></span>
									</span> 
									<input type="hidden" id="type" value="questionnum">
								</td>

                                <!-- 订单需沟通条数 -->
                                <td>
                                    <span>
                                        <input type="hidden" id="style" value="ordermessage">
                                        <span id="ordermessage" class="btn btn-success btnto"></span>
								    </span>
                                    <input type="hidden" id="type" value="ordermessage">
                                </td>

								<td><span> <input type="hidden" id="style"
										value="noDelete"> <span id="shopcarmarket"
										class="btn btn-success btnto"></span>
								</span> <span> <span style="font-size: 12px;">最近1周</span> <span
										id="shopcarmarket1" class="btn btn-warning btnto"></span>
								</span> <input type="hidden" id="type" value="shopcarmarket"></td>

                                <!-- 商业询盘 -->
								<td>
                                    <%--<span id="busquer"> <input type="hidden" id="style" value="noArrage">
                                        <span id="businquiries" class="btn btn-primary btnto"></span>
								    </span>--%>
                                    <span>
                                        <input type="hidden" id="style" value="noDelete">
										<span id="businquiries1" class="btn btn-success btnto"></span>
								    </span>
                                    <%--<span>
                                        <span id="businquiries2" class="btn btn-warning btnto"></span>
								    </span>--%>
                                    <input type="hidden" id="type" value="businquiries">
                                </td>
								<!-- <td><span id="bat"> <input type="hidden" id="style"
										value="noArrage"> <span id="batapply"
										class="btn btn-primary btnto"></span>
								</span> <span> <input type="hidden" id="style" value="noDelete">
										<span id="batapply1" class="btn btn-success btnto"></span>
								</span> <span> <span id="batapply2"
										class="btn btn-warning btnto"></span>
								</span> <input type="hidden" id="type" value="batapply"></td> -->
								<!-- 客户投诉-->
								<td><span>
									  <input type="hidden" id="style"  value="noDelete">
									  <span id="refundscom" class="btn btn-success btnto"></span>
								</span>
									<input type="hidden" id="type" value="refundscom">
									<!-- 未读-->
									<span>
										<input type="hidden" id="type" value="refundscom2">

								<span>
									<span id="refundscom2" class="btn btn-primary btnto"></span>
								</span>
										</span>
									<span> <span id="refundscom1" class="btn btn-warning btnto"></span>
								</span>

								</td>
								<!-- -->
								<td><span> <span id="ordermeg"
										class="btn btn-primary btnto"> </span>
								</span> <input type="hidden" id="type" value="ordermeg"></td>
<!-- 								<td><span> <span id="systemfailure" -->
<!-- 										class="btn btn-primary btnto"> </span> -->
<!-- 								</span> <input type="hidden" id="type" value="systemfailure"> -->
<!-- 								<input type="hidden" id="style"  value="systemfailure"></td> -->

								<td><span class="bt btn-success">未处理数量(待办)</span><br /> <span
									class="bt btn-primary">未布置数量(处理中)</span><br /> <span
									class="bt btn-warning">所有数量</span></td>
								<td><input type="button" value="刷新" onclick="reFreshoDate()"></td>
							</tr>
						</table>
						<span>[采购预警项目]:</span><span style="color:red;">付款超两天，但是订单状态仍然为“订单审核”，未进入“购买中”</span>
						<span style="margin-left:106px">[入库预警项目]:</span><span style="color:red;">国内交期只剩两天，但是订单状态仍然在“购买中”， 未到仓库</span><br>
						<span style="margin-left:-239px">[出货预警项目]:</span><span style="color:red;">订单已到库（不管验货状态），国内交期剩余两天的订单数量</span>
						<span  style="margin-left:111px">[超期未出货]:</span><span style="color:red;">超过国内交期的所有订单</span>
						<div class="toptoal" style="margin: 0 auto;">
							<table class="tabletoptoal" border="1px" style="font-size: 20px;"
								bordercolor="#8064A2" cellpadding="0" cellspacing="0">
								<tr style="background-color: #D2BBB5">
									<td style="width: 138px;">支付失败订单</td>
									<%--<td style="width: 138px;">未及时确认的订单</td>--%>
									<td style="width: 138px;">入库预警项目</td>
									<td style="width: 138px;">出货预警项目</td>
									<td style="width: 124px;">超期未出货</td>
									<td style="width: 124px;">订单取消</td>
									<td style="width: 124px;">建议替换/同意替换/取消替换</td>
									<!-- <td style="width: 124px;">同意替换</td> -->
									<%--<td style="width: 124px;">货源问题</td>--%>
									<!-- <td style="width: 124px;">货物到齐</td> -->
									<td style="width: 124px;">出货审核问题</td>
									<td style="width: 124px;">物流问题</td>
									<%--<td style="width: 124px;">出运运费预警</td>--%>
									<td style="width: 124px;">质检服务订单</td>
								</tr>
								<tr>
									<td id="order_pending">0</td>
									<%--<td id="purchasewarning">0</td>--%>
									<td id="storagewarning">0</td>
									<td id="shipmentwarning">0</td>
									<td id="notshipping">0</td>
									<td id="cacleorder">0</td>
									<td><span id="changes">0</span>/<span id="getchange">0</span>/<span id="noChange">0</span></td>
									<!-- <td id="getchange">0</td> -->
									<%--<td id="errorbuy">0</td>--%>
									<!-- <td id="allgoods">0</td> -->
									<td id="errorgoods">0</td>
									<td><%--<span id="onshipping">0</span>/--%><span id="onshippingw">0</span></td>
									<%--<td id="freightWaraing">0</td>--%>
									<td><span id="checkOrder">0</span></td>
								</tr>
							</table>

						</div>
					</div>

				</div>
				<!-- 顶部统计区域 start -->

				<!-- 顶部统计区域 end -->
				<br />


				<!-- 条件选择区域 start -->
				<table class="selecttable">
					<tr>
						<!-- <td style="width: 170px;"><input type="checkbox" id="isShowUnpaid" name="isShowUnpaid"/>是否显示未付款订单</td> -->
						<td style="width: 120px;">支付开始日期：<input id="startdate"
							name="startdate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /></td>
						<td style="width: 120px;">支付结束日期：<input id="enddate"
							name="enddate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})" value="${param.enddate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /></td>
						<td style="width: 80px;">state: <select id="state"
							name="state" style="width: 87px;">
								<option value="-2" ${param.state==-1?'selected="selected"':''}>全部订单</option>
								<option value="2" ${param.state==2?'selected="selected"':''}>到达仓库</option>
								<option value="6" ${param.state==6?'selected="selected"':''}>取消订单</option>
								<option value="0" ${param.state==0?'selected="selected"':''}>等待付款</option>
								<option value="1" ${param.state==1?'selected="selected"':''}>购买中</option>
								<option value="3" ${param.state==3?'selected="selected"':''}>出运中</option>
								<option value="4" ${param.state==4?'selected="selected"':''}>完结</option>
								<option value="5" ${param.state==5?'selected="selected"':''}>确认价格中</option>
								<option value="7" ${param.state==7?'selected="selected"':''}>非正常出库</option>
								<option value="8" ${param.state==8?'selected="selected"':''}>采样订单</option>
								<option value="9" ${param.state==9?'selected="selected"':''}>支付失败</option>
						</select>
						</td>
						<td style="width: 80px;">运单状态<select id="trackState"
							name="trackState" style="width: 87px;">
								<option value="0" ${param.trackState==0?'selected="selected"':''}>全部订单</option>
								<option value="2" ${param.trackState==2?'selected="selected"':''}>已发货</option>
								<option value="3" ${param.trackState==3?'selected="selected"':''}>已签收</option>
								<option value="4" ${param.trackState==4?'selected="selected"':''}>退回</option>
								<option value="5" ${param.trackState==5?'selected="selected"':''}>异常</option>
								<option value="6" ${param.trackState==6?'selected="selected"':''}>内部异常</option>
								<option value="7" ${param.trackState==7?'selected="selected"':''}>手动标记为正常</option>
						</select>
						</td>
						<td style="width: 115px;">销售负责人： <select id="adminusersc"
							name="adminuser" style="width: 87px;">
						</select>
						</td>
						<!-- <td style="width: 100px;">采购负责人： <select id="buyuser"
							name="buyuser" style="width: 87px;">
						</select>
						</td> -->
						<td style="width: 120px;">订单号:<input type="text" id="orderno"
							name="orderno" value="${param.orderno}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}'></td>
						<td style="width: 120px;">支付交易号:<input type="text" id="paymentid"
															 name="paymentid" value="${param.paymentid}"
															 onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}'></td>
						<td style="width: 120px;">用户ID:<input type="text" id="userid"
							name="userid" value="${param.userid}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}'></td>
						<td style="width: 120px;">用户邮箱：<input id="useremail"
							name="useremail" value="${param.email}" /></td>
						<td><button onclick="fnInquiry(1,'${param.type}');"
								style="margin-top: 18px;">查询</button></td>
						<td><button onclick="reset();" style="margin-top: 18px;">重置</button></td>
					</tr>
				</table>
				<!-- 条件选择区域 end -->
				<br />
			</div>

			<!-- 表格区域 start -->
			<div class="tablecontent">
				<table id="table" border="1px" style="font-size: 13px;width:97%;"
					bordercolor="#8064A2" cellpadding="0" cellspacing="0">
					<Tr style="background-color: #C6D2B5">
						<td style="width: 20px;">序号</td>
						<td style="width: 180px;">项目标识</td>
						<td style="width: 150px;">订单号</td>
						<td style="width: 150px;">支付类型</td>
						<td style="width: 60px;">用户ID</td>
						<td style="width: 40px;">订单量</td>
						<td style="width: 210px;">邮箱</td>
						<td style="width: 70px; text-align: left;">支付时间</td>
						<td style="width: 67px;">国内准备段</td>
						<td style="width: 67px;">国际运输段</td>
						<td style="width: 85px;">产品金额</td>
						<td style="width: 63px;">订单状态</td>
						<td style="width: 60px;">运单状态</td>
						<td style="width: 60px;">订货国家</td>
						<td style="width: 110px;">预估国际运费/实重预估运费</td>
						<td style="width: 110px;">入库/验货疑问/采购/总数</td>
						<%--<td style="width: 90px;">入库情况汇总</td>--%>
						<td style="width: 60px;">销售</td>
<!-- 						<td style="width: 65px;">采购</td> -->
						<td style="width: 50px;">操作</td>
						<td style="width: 100px;">备注</td>
					</Tr>
				</table>
			</div>
			<!-- 表格区域 end -->
			<div class="pages" id="pages">
				<span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font
					id="count"></font></span>&nbsp;&nbsp;当前页:<font id="page"></font>&nbsp;&nbsp;
				<button onclick="fnInquiry(3,'${param.type}')">上一页</button>
				&nbsp;
				<button onclick="fnInquiry(2,'${param.type}')">下一页</button>
				&nbsp;<input id="jump" type="text"
					onkeydown='if(event.keyCode==13){fnInquiry(4,"${param.type}");}'>
				<button onclick="fnInquiry(4,'${param.type}')">转至</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<button onclick="exportExcel()" style="margin-top: 18px;">导出本页</button>
			</div>
			<br> <br>
		</div>
		<!-- 时间 start -->
			<div style="font-size: 14px;text-align: center;" class="timediv">
				<span id="time1"></span>&nbsp;&nbsp;&nbsp;<span id="time2"></span>&nbsp;&nbsp;&nbsp;<span
					id="time3"></span>
			</div>
			<script type="text/javascript">showLeftTime();	</script>
</body>
</html>