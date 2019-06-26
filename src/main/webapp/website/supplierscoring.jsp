<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.cbt.warehouse.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商列表</title>
</head>
<script src="/cbtconsole/js/jquery-2.1.0.min.js"></script>
<script src="/cbtconsole/js/bootstrap/bootstrap.min2.js"></script>
<link href="/cbtconsole/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
<link href="/cbtconsole/css/bootstrap.min2.css" rel="stylesheet">
<script src="/cbtconsole/js/bootstrap/moment-with-locales.js"></script>
<script src="/cbtconsole/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script src="/cbtconsole/js/bootstrap/bootstrap-datetimepicker.zh-CN.js"></script>


<body style="width: 98%; margin: 0 1%;">
 		<form action="/cbtconsole/supplierscoring/querySupplierScoringList" method="get">
 			<%--<input type="radio" name="flag" value="1" checked="checked">最近7天已经验货的,本人采购的,未评价的</input>--%>
    		<input type="hidden" name="flag" value="0" checked="checked"><%--所有的--%></input>
    		<div style="float: right;">【合作过的供应商（<span>${cooperatedCount}</span>）】【优质供应商（<span>${highCount}</span>）】【普通供应商（<span>${ordinaryCount}</span>）】【黑名单供应商（<span>${blacklistCount}</span>）】
 			</div><br />

                <div>
 			供应商ID:<input id="select_ship_id" type="text" name="shop_id" style="height: 20px;">
			供应商类别名称:<input id="categoryName" type="text" name="categoryName" style="height: 20px;">
 			供应商级别:<select id="select_level"  name="level" style="height: 20px;">
 							<option value=""  >---请选择---</option>
 							<option value="1">合作过的供应商</option>
 							<option value="2">优选供应商</option>
 							<option value="3" >黑名单</option>
 					  </select>
			质量评分:<select id="quality"  name="quality" style="height: 20px;">
						<option value=""  >---全部---</option>
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3" >3</option>
						<option value="4">4</option>
						<option value="5" >5</option>
						<option value="-1">暂未评分</option>
						</select>
			是否授权:<select id="authorizedFlag"  name="authorizedFlag" style="height: 20px;">
						<option value="" >---全部---</option>
						<option value="0">未授权</option>
						<option value="1">已授权</option>
		 			 </select>
                <br />
                </div>

                <div style="margin: 4px 0;">
                    <span style="float: left;">是否核心供应商:</span>
                    <select id="salesShop"  name="salesShop" style="float: left;">
                        <option value="0">---全部---</option>
                        <option value="1">是核心供应商</option>
                        <option value="2">不是核心供应商</option>
                    </select>
                    <span style="float: left;">店铺上线时间:</span>
                    <a class='input-group date' id='datetimepicker1' style="float: left;">
                        <input type='text' class="form-control" name="nowdate1" id='nowdate1' style="width: 100px; height: 20px;"/>
                        <span class="input-group-addon" style="float: left; width: 50px; height: 20px;">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                    </a>
                    <span style="float: left; margin: 0 6px">到</span>
                    <a class='input-group date' id='datetimepicker2' style="float: left;">
                        <input type='text' class="form-control" name="nowdate2" id='nowdate2' style="width: 100px; height: 20px;" />
                        <span class="input-group-addon" style="float: left; width: 50px; height: 20px;">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                    </a>
                    <input type="submit" style="margin-left:20px;height: 20px;line-height: 16px;" value="查询">
                    <input type="reset"  style="margin-left:20px;height: 20px;line-height: 16px;" value="重置">
                </div>
        </form>

    <table border="1px  #0094ff" style="border-collapse: collapse; width: 100%;">
 		<thead>
			<tr>
			<td>供应商</td>
			<td>供应商类别ID(类别名称)</td>
			<td>店铺商品总数|在线产品数量<br />|店铺产品在订单中出现次数</td>
			<td>是否精品店铺</td>
			<td>是否授权</td>
			<td>质量</td>
			<td>级别</td>
			<td>供应商地址</td>
			<td>产品打分</td>
			<td>售卖金额($)</td>
			<td>支持退换货天数</td>
			<td colspan="3">库存协议</td>
			</tr>
		</thead>
		<tbody>
		 <c:forEach items="${pageInfo.list }" var="supplierScoringBean" varStatus="status">
			<tr>
			<td><a target='_blank' href='https://www.import-express.com/shop?sid=${supplierScoringBean.shopId }'>${supplierScoringBean.shopId }</a>
			</td>
			<td>${supplierScoringBean.category}</td>
			<td>${supplierScoringBean.allcounts }|${supplierScoringBean.counts }|${supplierScoringBean.sell }</td>
			<td>${supplierScoringBean.type}</td>
			<td>${supplierScoringBean.authorizedFlag}</td>
			<td>
				<c:if test="${supplierScoringBean.qualityAvg == 0 && supplierScoringBean.serviceAvg == 0 }">
					暂未评分
				</c:if>
				<c:if test="${supplierScoringBean.qualityAvg != 0 || supplierScoringBean.serviceAvg != 0 }">
					${supplierScoringBean.qualityAvg }
				</c:if>
			</td> 			
			<td>
				<c:if test="${empty supplierScoringBean.level }">
							合作过的供应商
				</c:if>
				<c:if test="${not empty supplierScoringBean.level }">
						${supplierScoringBean.level }	
				</c:if>
			</td>
			<td>
				<c:if test="${empty supplierScoringBean.address}">
						<font color="red" size="2px">未知</font>
				</c:if>
				<c:if test="${not empty supplierScoringBean.address}">
							${supplierScoringBean.address}
				</c:if>
			</td>
			<td><a target="_blank" href="/cbtconsole/supplierscoring/supplierproducts?shop_id=${supplierScoringBean.shopId }&flag=1">进入店铺、商品评分</a></td>
			<td>${supplierScoringBean.shopPrice}</td>
			<td>
				<c:if test="${supplierScoringBean.returnDays!=0 }">${supplierScoringBean.returnDays }</c:if>
			</td>
			<td colspan="2" id="${status.index }inventoryAgreement">
				<c:if test="${supplierScoringBean.inventoryAgreement==2 }">
						<font color="green" size="3px">有库存协议</font>
						<c:if test="${supplierScoringBean.returnDays!=0 }">
								(${supplierScoringBean.returnDays}天可退换)
						</c:if>
				</c:if>
				<c:if test="${supplierScoringBean.inventoryAgreement==1 }">
						<font color="blue" size="3px">无库存协议</font>
				</c:if>
				<c:if test="${supplierScoringBean.inventoryAgreement==0 }">
						<font  color="red" size="3px">没有沟通过库存协议</font>
				</c:if>
			</td>
			<td id="${status.index }operation">
				<button onclick="updateOrAdd(${supplierScoringBean.id },${status.index},'${supplierScoringBean.shopId }')" ><a href="javascript:void(0)">添加/修改</a></button>
			</td>
			</tr>
		</c:forEach>
		</tbody>
    </table>
		 <div class="pageInfoInfo" id="pageInfoInfo">
				<span>&nbsp;&nbsp;总条数：<font>${pageInfo.countRecord }</font>&nbsp;&nbsp;总页数：<font>${pageInfo.countPage }</font></span>&nbsp;&nbsp;当前页:<font >${pageInfo.currentPage }</font>&nbsp;&nbsp;
				<button onclick="topageInfo('${pageInfo.currentPage-1}','${pageInfo.currentPage }')">上一页</button>
				&nbsp;
				<button onclick="topageInfo('${pageInfo.currentPage+1}','${pageInfo.currentPage }')">下一页</button>
				&nbsp;<input id="jump1" type="text" onkeyup="checkNum(this)">
				<button onclick="topageInfo('-1','${pageInfo.currentPage }')">转至</button>
			</div>
			<br> <br>

</body>
<script type="text/javascript">

	/* 校验输入框 */
	function checkNum(obj){
		  var re = /^[0-9]*[1-9][0-9]*$/ ;
		  var page = obj.value;
		  if(!re.test(page)){
			  obj.value="";
		  }
	}
	function topageInfo(topage,currpage){
            var salesShop = $("#salesShop").val();
            var nowdate1 = $("#nowdate1").val();
            var nowdate2 = $("#nowdate2").val();

			var countpage = "${pageInfo.countPage }";
			var level =	$("#select_level").val();
			var shop_id = $("#select_ship_id").val();
			var quality=$("#quality").val();
			var categoryName=$("#categoryName").val();
      		/* var services=$("#services").val(); */
      		var flag=$('input[type=radio][name=flag]:checked').val();
			topage =parseInt(topage)
			countpage =parseInt(countpage)
			if(topage == 0){
				return ;
			}
			if(topage>countpage){
				return ;
			}
			if(topage==-1){
				topage = $("#jump1").val()
			}
		    window.location.href="/cbtconsole/supplierscoring/querySupplierScoringList?currpage="+topage
                +"&shop_id="+shop_id+"&level="+level+"&quality="+quality
                +"&salesShop="+salesShop+"&nowdate1="+nowdate1+"&nowdate2="+nowdate2
                +"&flag="+flag+"&categoryName="+categoryName;

		}

	var text1;
	var text2;
	function updateOrAdd(id,index,shopid){
		//加载该店铺的信息
		  $.ajax({
               type: "POST",//方法类型
               dataType:'json',
               url: "/cbtconsole/supplierscoring/queryShopInfo" ,//url
               data: {"shop_id":shopid},
               success:function(data){
            	 text1 = $("#"+index+"inventoryAgreement").html()
           		 text2=$("#"+index+"operation").html();
           		var str = "<form id='"+index+"form'> <input type='hidden' name='id' value='"+id+"' /><input type='hidden' name='shopid' value='"+shopid+"' />   <input name='inventoryAgreement' type='radio' value='2'";
           			if(data.inventoryAgreement==2){
           				str+="checked='checked'";
           			}
           			str+="/>有库存协议<input name='inventoryAgreement' type='radio'";
           			if(data.inventoryAgreement==1){
           				str+="checked='checked'";
           			}
           			str+="value='1' />无库存协议 &nbsp;&nbsp;&nbsp;支持退货天数:<input size='4' type='text' name='returnDays' value="+data.returnDays+"></form>";

           		 $("#"+index+"inventoryAgreement").html(str);
           		$("#"+index+"operation").html("<button onclick='saveOrUp("+index+")' ><a href='javascript:void(0)'>保存</a></button> <button onclick='cancelSaveOrUp("+index+")' ><a href='javascript:void(0)'>取消</a></button>  ")
			    }
		 });



	}

	function cancelSaveOrUp(index) {
		$("#"+index+"inventoryAgreement").html(text1)
		$("#"+index+"operation").html(text2)
	}

	function saveOrUp(index){
			 var formData = $("#"+index+"form").serialize()
			//异步发送请求
			  $.ajax({
               type: "POST",//方法类型
               dataType:'json',
               url: "/cbtconsole/supplierscoring/saveorupdateInventoryAgreement" ,//url
               data:formData,
               success:function(data){
                   window.location.reload();

			    }
		 });

	}

	$(function(){
		var shop_id = '${shop_id}';
		var level = '${level}';
		var quality = '${quality}';
        var authorizedFlag='${authorizedFlag}';
        <%--var flag='${flag}';--%>
        var categoryName='${categoryName}';
        var salesShop='${salesShop}';
        var nowdate1 = '${nowdate1}';
        var nowdate2 = '${nowdate2}';
        $("#quality").val(quality);
        /* $("#services").val(services); */
		$("#select_ship_id").val(shop_id);
		$("#categoryName").val(categoryName);
		$("#authorizedFlag").val(authorizedFlag);
		$("#salesShop").val(salesShop);
		$("#nowdate1").val(nowdate1);
		$("#nowdate2").val(nowdate2);
		// if("true" == flag){
		// 	$('input[type=radio][name=flag][value=1]').get(0).checked = "checked";
		// } else{
		// 	$('input[type=radio][name=flag][value=0]').get(0).checked = "checked";
		// }
		if(level != ""){
			if(level==1){
				$("#select_level").val("1");
			}else if(level==2){
				$("#select_level").val("2");
			}else if(level==3){
				$("#select_level").val("3");
			}else{
                $("#select_level").val("");
			}

		}

	})

    //设置日期时间控件
    function Datetime(dateId) {
        $(dateId).datetimepicker({
            language: 'zh-CN', //显示中文
            format: 'yyyy-mm-dd', //显示格式
            minView: "month", //设置只显示到月份
            initialDate: new Date(),
            autoclose: true, //选中自动关闭
            todayBtn: true, //显示今日按钮
            locale: moment.locale('zh-cn')
        });
    }
    Datetime('#datetimepicker1');
    Datetime('#datetimepicker2');
    function iniDatetime() {
        var today = new Date();
        var nowdate = (today.getFullYear()) + "-" + (today.getMonth() + 1) + "-" + today.getDate();
        var date = new Date(nowdate);
        var mon = date.getMonth() + 1;
        var day = date.getDate();
        var mydate = date.getFullYear() + "-" + (mon < 10 ? "0" + mon : mon) + "-" + (day < 10 ? "0" + day : day);
        document.getElementById("nowdate2").value = '';
        today = new Date();
        today.setDate(today.getDate() - 90); //日期减90天
        nowdate = (today.getFullYear()) + "-" + (today.getMonth() + 1) + "-" + today.getDate();
        date = new Date(nowdate);
        mon = date.getMonth() + 1;
        day = date.getDate();
        mydate = date.getFullYear() + "-" + (mon < 10 ? "0" + mon : mon) + "-" + (day < 10 ? "0" + day : day);
        document.getElementById("nowdate1").value = '';
    }
    iniDatetime();


</script>

</html>