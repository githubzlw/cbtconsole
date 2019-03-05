<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	response.flushBuffer();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>同MD5店铺图片展示</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<style type="text/css">
.img_sty {
	max-width: 180px;
	max-height: 180px;
}

.checkBg {
	background-color: #b6f5b6;
}

.check_sty {
	height: 24px;
	float: left;
	width: 24px;
}
.select_ckb{
	height: 30px;
	width: 75px;
	background: #169bd4;
    color: #fff;
}

.div_sty {
	width: 185px;
	height: 215px;
}

.s_btn {
	display: inline-block;
	width: 120px;
	height: 35px;
	background: #169bd4;
	margin: 0px 0px 10px 10px;
	border-radius: 10px;
	text-align: center;
	color: #fff;
	cursor: pointer;
	font-size: 14px;
}

.div_sty_img {
	border: 5px solid #000;
	text-align: center;
	background-color: #FFF7FB;
	width: 750px;
	height: 750px;
	position: fixed;
	top: 90px;
	left: 15%;
	margin-left: 400px;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
}
</style>
<script type="text/javascript">

	$(document).ready(function() {
		$('.img_sty').lazyload({effect: "fadeIn"});
	});

	function chooseBox(obj) {
		var is = $(obj).is(':checked');
		if (is) {
			$(obj).addClass("isChoose");
			$(obj).parent().parent().addClass("checkBg");
		} else {
			$(obj).removeClass("isChoose");
			$(obj).parent().parent().removeClass("checkBg");
		}
	}

	function chooseImgBox(num) {
		if(num > 0){
			if(num == 1){
				$(".check_sty").each(function(){
					$(this).addClass("isChoose");
					$(this).parent().parent().addClass("checkBg");
					$(this).prop("checked",true);
				});
			}else{
				$(".check_sty").each(function(){
					$(this).removeClass("isChoose");
					$(this).parent().parent().removeClass("checkBg");
					$(this).removeAttr("checked");
				});
			}
		}else{
			$(".check_sty").each(function(){
				if ($(this).is(':checked')) {
					$(this).removeClass("isChoose");
					$(this).parent().parent().removeClass("checkBg");
					$(this).removeAttr("checked");
				}else {
					$(this).addClass("isChoose");
					$(this).parent().parent().addClass("checkBg");
					$(this).prop("checked",true);
				}
			});
		}
	}

	function deleteGoodsImgs(shopId,useHm) {
		$.messager.confirm('系统提醒', '是否删除，删除保存后数据不可恢复', function(r) {
			if (r) {
				var json = [];
				$(".isChoose").each(function() {
					var checkVal = $(this).val();
					var arrDt = checkVal.split("@");
					var pidPam = {};
					pidPam["pids"] = arrDt[0];
					pidPam["imgUrl"] = arrDt[1];
					json[json.length] = pidPam;
				});
				if (json.length == 0) {
					$.messager.alert("提醒", "请选择需要删除的图片", "info");
				} else {
					$.messager.progress({
						title : '正在删除图片',
						msg : '请等待...'
					});
					$.ajax({
						type : 'POST',
						dataType : 'json',
						url : '/cbtconsole/ShopUrlC/deleteGoodsImgs.do',
						data : {
							shopId : shopId,
							useHm : useHm,
							imgs : JSON.stringify(json)
						},
						success : function(data) {
							$.messager.progress('close');
							if (data.ok) {
								$.messager.alert("提醒", "执行成功，页面即将刷新", "info");
								window.location.reload();
							} else {
								$.messager.alert("提醒", data.message, "error");
							}
						},
						error : function(XMLResponse) {
							$.messager.progress('close');
							$.messager.alert("提醒", "保存错误，请联系管理员", "error");
						}
					});
				}
			}
		});

	}

	function bigImg(img) {
		$('#big_img').empty();
		htm_ = "<img style='max-width:700px;max-height:700px;' src="+img
			+"><br><input class='s_btn' type='button' value='关闭' onclick='closeBigImg()' />";
		$("#big_img").append(htm_);
		$("#big_img").css("display", "block");
	}

	function closeBigImg() {
		$("#big_img").css("display", "none");
		$('#big_img').empty();
	}
</script>
</head>
<body>

	<c:if test="${show == 0}">
		<h1 align="center">${message}</h1>
	</c:if>
	<c:if test="${show > 0}">

		<div class="div_sty_img" style="display: none;" id="big_img"></div>
		<div>
			<h1 align="center">同MD5店铺图片展示(<span style="color:green">总数:${showTotal}</span>)</h1>
			<div>

				<input class="s_btn" type="button" value="删除"
					onclick="deleteGoodsImgs('${shopId}',${useHm})" />&nbsp;&nbsp;&nbsp;&nbsp;<span><b
					style="color: red; font-size: 18px;">请点击图片查看放大的图片</b></span>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input class="select_ckb" type="button" onclick="chooseImgBox(1)" value="全选"/>
					&nbsp;&nbsp;&nbsp;
					<input class="select_ckb" type="button" onclick="chooseImgBox(0)" value="反选"/>
					&nbsp;&nbsp;&nbsp;
					<input class="select_ckb" type="button" onclick="chooseImgBox(2)" value="取消全选"/>
			</div>
			<table border="1" cellpadding="0" cellspacing="0" align="center">
				<c:set var="count" value="0" />
				<c:forEach items="${list}" var="imgGd">
					<c:set var="count" value="${count+1 }" />
					<c:if test="${count == 1}">
						<tr>
					</c:if>
					<td>
						<div class="div_sty">
							<label>
							input class="check_sty isChoose" type="checkbox" checked="checked"
									value="${imgGd.pid}" onclick="chooseBox(this)"/>
							</label> <br> <img class="img_sty" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif"
								data-original="${imgGd.imgShow}" onclick="bigImg('${imgGd.imgShow}')"/>
                            <br>
                            <span>PID:${imgGd.pid}</span>
						</div>
					</td>
					<c:if test="${count % 10 == 0}">
						</tr>
					</c:if>
				</c:forEach>
				<c:if test="${count % 10 > 0}">
					</tr>
				</c:if>
			</table>
		</div>

	</c:if>


</body>
</html>