<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.2.min.js"></script>
<title>供应商/供应商产品打分</title>
</head>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
<body>
 <a href="/cbtconsole/supplierscoring/querySupplierScoringList?flag=1">返回供应商列表页</a>
 <a target="_blank" href="/cbtconsole/website/shopBuyLog.jsp?shopId=${param.shop_id}">查看该供应商采购历史</a>
 				<br /><br />
 				<input type="radio" name="flag" value="1" checked="checked">最近7天已经验货的,本人采购的,未评价的</input>
	    		<input type="radio" name="flag" value="0">所有的</input>
	 			<!-- 显示店铺的打分信息 -->
	 			<br><br><font style="text-align: center; padding-left: 200px">店铺打分信息展示 </font>
					<table border="1px  #0094ff ; border-collapse: collapse" style="width: 50%;height: 50%">
						<tr>
							<td>店铺</td>
							<td>质量</td>
							<td>编辑人</td>
							<td>编辑时间</td>
						</tr>
	 					<c:forEach items="${supplierproducts }" var="supplierBean">
	 							<tr>
									<td>
									${supplierBean.shopId }
									<td>
											${supplierBean.quality}
									</td> 			
					 				<td>${supplierBean.userName } </td>
									<td>${supplierBean.createtime } </td>
	 							</tr>	
	 					</c:forEach>
						<tr>
							<td><%=request.getParameter("shop_id")%></td>
							<td>
								<select style="text-align:center;margin-left: 30px" id="qualitys" name="qualitys">
									<option value="0">---请选择---</option>
									<c:forEach begin="1" end="5" step="1" var="i">
											<option value="${i }">${i }分</option>
									</c:forEach>
								</select>
							</td>
							<td><input type="button" onclick="toSubmit(1,'','<%=request.getParameter("shop_id")%>','<%=request.getParameter("shop_id")%>')" value="保存 "></td>
						</tr>
					</table>

	 			<br><br><font style="text-align: center; padding-left: 200px">产品打分信息展示 </font>
	 			</br></br>
					 	<font color="red">[质量]</font>:1:差 &nbsp;&nbsp;2:较差&nbsp;&nbsp;3: 一般&nbsp;&nbsp;4:无投诉&nbsp;&nbsp;5: 优质</br> 

	 			<table border="1px  #0094ff ; border-collapse: collapse" style="width: 50%;height: 50%">
	 			<tr>
					<td>产品</td> 			
					<td>质量</td> 
					<td>备注</td>
					 <td>编辑人</td>
					<td>操作</td>
				</tr>
	 			
	 			
	 			<!-- 别人的打分信息 -->
	 		<c:forEach items="${supplierProductsBeans }" var="supplierproduct" varStatus="status">
	 			<tr>
					<td>
							${supplierproduct.goodsImg }</td>
					<td>${supplierproduct.quality}
					</td>
					<td>
						<textarea id="111${status.index }remarks" name="remarks" rows="10" cols="15">${supplierproduct.remarks }</textarea>
							</td>
	 				<td>${supplierproduct.userName } </td>
					<td><input type="button" onclick="updateRemark('111${status.index }','${supplierproduct.id}')" value="保存评分">
						<input type="button" onclick="lookQuestion('${supplierproduct.goodsPid}')" value="查看问答">
						<%--<input type="button" onclick="lookCustom('${supplierproduct.goodsPid}')" value="查看收货评价">--%>
					</td>
	 			</tr>
	 		</c:forEach>
	 		<c:forEach items="${supplierProductsBeansList }" var="supplierProductsBean" varStatus="status">

	 			<form id="${status.index }form">
	 				<tr>
					<td>
							${supplierProductsBean.goodsImg }</td>
					<td>
						<select style="text-align:center;margin-left: 30px" id="${status.index }quality" name="quality">
							<option value="0">---请选择---</option>
							<c:forEach begin="1" end="5" step="1" var="i">
							<c:if test="${supplierProductsBean.quality == i}">
								<option selected="selected" value="${i }">${i }分</option>
							</c:if>
							<c:if test="${supplierProductsBean.quality != i}">
							<option value="${i }">${i }分</option>
							</c:if>
							</c:forEach>
						</select>
					</td>
					<td><textarea id="${status.index }remarks" name="remarks" rows="10" cols="15">${supplierProductsBean.remarks }</textarea></td>
	 				<td>${supplierProductsBean.userName } </td>
					<td>
						<input type="button" onclick="toSubmit(0,'${status.index }','${supplierProductsBean.shopId}','${supplierProductsBean.goodsPid}')" value="保存评分">
						<input type="button" onclick="lookQuestion('${supplierProductsBean.goodsPid}')" value="查看问答">
						<%--<input type="button" onclick="lookCustom('${supplierproduct.goodsPid}')" value="查看收货评价">--%>
					</td>
	 			</tr>
	 		</form>
	 		</c:forEach>
	 		</table>
 <div id="showQa" style="display: none;width: 580px;position: fixed;right: 16%;top: 12%;">

 </div>
	 <script type="text/javascript">

		 function updateRemark(index,id){
             var newRemark = $("#"+index+"remarks").val();
             if(newRemark == null || newRemark == ""){
                 alert("请输入更改的备注内容");
                 return;
			 }
             $.ajax({
                 type: "POST",
                 dataType:'json',
                 url:'/cbtconsole/supplierscoring/updateRemark',
                 data:{newRemark:newRemark,id:id},
                 dataType:"json",
                 success:function(data){
                     if(data.row >0){
                         alert("保存成功")
                         window.location.reload();
                     }else{
                         alert("保存失败")
                     }
                 }
             });
		 }
		 function closeDiv(){
             document.getElementById("showQa").style.display = "none";
             $("#showQa").html("");
		 }

		 function lookCustom(pid){

		 }

		 //查看该商品问答信息
		 function lookQuestion(pid){
             $("#showQa").html("");
             $.ajax({
                 type: "POST",
                 dataType:'json',
                 url:'/cbtconsole/supplierscoring/lookQuestion',
                 data:{pid:pid},
                 dataType:"json",
                 success:function(data){
                     document.getElementById("showQa").style.display = "block";
					var list=data.list;
					var html="<table border='1'><tr> <td>提问用户ID</td><td>提问内容</td><td>运营回答人员</td><td>回复内容</td></tr>";
					for(var i=0;i<list.length;i++){
					    console.log(list[i].question_content);
                        console.log(list[i].reply_content);
                        html+="<tr><td>"+list[i].userid+"</td><td>"+list[i].question_content+"</td>" +
							"<td>"+list[i].reply_name+"</td><td>"+list[i].reply_content+"</td></tr>";
					}
					if(list.length<=0){
                        html+="<tr><td>-</td><td>-</td><td>-</td><td>-</td></tr>";
					}
					html+="<tr><td colspan='4'><button style='margin-left:130px;' onclick='closeDiv();'>关闭</button></td></tr></table>";
					$("#showQa").html(html);
                 }
             });
		 }

	 		function toSubmit(type,index,shopId,goodsPid){
                var qvalue ="";
                var svalue = "";
                var rvalue ="";
	 		    if(type == 1){
                    qvalue = $("#qualitys").val();
				}else{
                    qvalue = $("#"+index+"quality").val();
                    rvalue = $("#"+index+"remarks").val();
				}
                $.ajax({
                    type: "POST",
                    dataType:'json',
                    url:'/cbtconsole/supplierscoring/saveproductscord',
                    data:{quality:qvalue,service:svalue,remarks:rvalue,shopId:shopId,goodsPid:goodsPid},
                    dataType:"json",
                    success:function(data){
                        if(data.flag == 'success'){
                            alert("保存成功")
                            window.location.reload();
                        }else{
                            alert("保存失败")
                        }
                    }
                });
	 		}
	 		
	 		//查询 最近7天已经验货的,本人采购的,未评价的  和 所有的 单选按钮
            $('input[type=radio][name=flag]').change(function() {
    		    window.location.href="/cbtconsole/supplierscoring/supplierproducts?shop_id=${param.shop_id}&flag="+$('input[type=radio][name=flag]:checked').val();
            });
	 		
            var flag='${map.flag}';
            if("true" == flag){
    			$('input[type=radio][name=flag][value=1]').get(0).checked = "checked";
    		} else{
    			$('input[type=radio][name=flag][value=0]').get(0).checked = "checked";
    		}

	 </script>
</body>
</html>