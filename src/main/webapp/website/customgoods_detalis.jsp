<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/xheditor-1.2.2.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/xheditor_lang/zh-cn.js"></script>
<link rel="stylesheet" type="text/css"
	href="xheditor_skin/vista/iframe.css" />
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<style type="text/css">
* {
	padding: 0;
	margin: 0;
	outline: none;
	list-style: none;
}

html, body {
	width: 100%;
	height: 100%;
	overflow-y: auto;
	color: #333;
	font-size: 14px;
	background: #fff;
	font-family: "微软雅黑";
}

.all {
	width: 99%;
	height: 100%;
	margin: 0 auto;
}

.tit {
	width: 100%;
	height: 40px;
	line-height: 40px;
	font-weight: bold;
	font-size: 20px;
	text-align: center;
	color: #000;
	/* margin-bottom: 30px; */
}

.key_word_box {
	height: 30px;
	width: 80%;
}

.label_txt, .key_word {
	display: inline-block;
	height: 30px;
	line-height: 30px;
	font-weight: bold;
	letter-spacing: 2px;
}

.key_word {
	width: 80%;
	border: 1px solid #666;
	padding-left: 10px;
	margin-left: 1%;
}

.list_box {
	margin-top: 10px;
	width: 100%;
	height: auto;
}

.box {
	width: 100%;
	overflow: hidden;
}

.box li {
	display: -webkit-flex;
	display: -moz-flex;
	display: flex;
	width: 100%;
	overflow: hidden;
	background: #f7f7f7;
	box-sizing: border-box;
	border-bottom: 1px solid #666;
}

.box .flex {
	flex: 1;
	border-right: 1px solid #666;
	box-sizing: border-box;
}

.li_tit {
	width: 100%;
	text-align: center;
	font-size: 18px;
	height: 36px;
	line-height: 36px;
	font-weight: bold;
}

.li_box {
	width: 100%;
	overflow: hidden;
}

.li_name {
	float: left;
	width: 15%;
	padding: 5px 0.5%;
	font-size: 16px;
	display: block;
	font-weight: bold;
}

.li_desc {
	float: right;
	display: block;
	width: 80%;
	padding: 5px 2%;
	line-height: 20px;
}

.textarea {
	font-size: 16px;
	display: inline-block;
	width: 100%;
	min-height: 60px;
	border: 1px solid #666;
}

.textarea_ot {
	font-size: 16px;
	display: inline-block;
	width: 100%;
	height: 100%;
	min-height: 160px;
	border: 1px solid #666;
	resize: none;
}

.input {
	display: inline-block;
	width: 30%;
	min-height: 25px;
}

.p_txt {
	width: 80%;
	padding: 5px 5%;
	line-height: 20px;
}

.btn {
	color: #fff;
	background-color: #5db5dc;
	border-color: #2e6da4;
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
	border-radius: 3px;
	border: 1px solid transparent;
	cursor: pointer;
}
</style>
<script type="text/javascript">
	$(function() {
		$('#goods_content').xheditor({
			tools : "full",
			html5Upload:false,
			upBtnText: "上传", 
			upMultiple: 1,
			upImgUrl : "/cbtconsole/editc/uploads?savePath=http://192.168.1.220:8833",
			upImgExt : "jpg,jpeg,gif,png"
		});

	});

	function doSaveDetalis(pid, type) {
		var keyWord = $("#key_word").val();
		if (pid == "" || pid == "0" || pid == 0) {
			showMessage("pid为空");
			return;
		}
		var enname = $("#import_enname").val();
		if (enname == "") {
			showMessage("翻译名称为空");
			$("#import_enname").focus();
			return;
		}
		var weight = $("#import_weight").val();
		if (weight == "" || weight == "0.00" || weight == 0) {
			showMessage("重量为空");
			$("#import_weight").focus();
			return;
		}
		var feeprice = $("#import_feeprice").val();
		if (feeprice == "" || feeprice == "0.00" || feeprice == 0) {
			showMessage("运费价为空");
			$("#import_feeprice").focus();
			return;
		}
		var fprice = $("#import_fprice").val();
		if (fprice == "" || fprice == "0.00" || fprice == 0) {
			showMessage("非免邮商品价为空");
			$("#import_fprice").focus();
			return;
		}
		var price = $("#import_price").val();
		if (price == "" || price == "0.00" || price == 0) {
			showMessage("价格为空");
			$("#import_price").focus();
			return;
		}
		var endetail = $("#import_endetail").val();
		if (endetail == "") {
			showMessage("翻译详情为空");
			$("#import_endetail").focus();
			return;
		}
		var content = $("#goods_content").val();
		if (content == "") {
			showMessage("获取翻译图片为空");
			return;
		}
		var localpath = $("#goods_localpath").val();
		if (localpath == "") {
			showMessage("获取图片路径为空");
			return;
		}
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/cbtconsole/editc/saveEditDetalis',
			data : {
				"type" : type,
				"pid" : pid,
				"keyWord" : keyWord,
				"enname" : enname,
				"weight" : weight,
				"feeprice" : feeprice,
				"fprice" : fprice,
				"price" : price,
				"endetail" : endetail,
				"localpath" : localpath,
				"content" : content

			},
			success : function(data) {
				if (data.ok) {
					showMessage("执行成功");
					//window.location.reload();
				} else {
					$.messager.alert("提醒", data.message, "error");
				}
			},
			error : function(XMLResponse) {
				$.messager.alert("提醒", "保存错误，请联系管理员", "error");
			}
		});
	}

	function showMessage(msgStr) {
		$.messager.show({
			title : '提醒',
			msg : msgStr,
			timeout : 1500,
			showType : 'slide',
			style : {
				right : '',
				top : ($(window).height() * 0.35),
				bottom : ''
			}
		});
	}
</script>
<title>1688goods-edit</title>
</head>
<body>
	<h3 class="tit">产品详情</h3>
	<div class="all">
		<div class="key_word_box">
			<label for="key_word" class="label_txt">关键词 : </label><input
				type="text" id="key_word" class="key_word" value="${goods.keyword}" />
			<input type="button" onclick="doSaveDetalis('${goods.pid}',0)"
				value="保存" style="height: 30px; width: 60px;" class="btn" />
			&nbsp;&nbsp; <input type="button"
				onclick="doSaveDetalis('${goods.pid}',1)" value="保存并重新发布"
				style="height: 30px; width: 100px;" class="btn" />
		</div>
		<div class="list_box">
			<ul class="box">
				<li>
					<div class="li_tit flex">1688</div>
					<div class="li_tit flex">importEx</div>
					<div class="li_tit flex">Aliexpress</div>
				</li>
				<li>
					<div class="li_box flex">
						<span class="li_name">产品名称：</span> <a target="_blank"
							href="https://detail.1688.com/offer/${goods.pid}.html"> <span
							class="li_desc">${goods.name}</span>
						</a>
					</div>
					<div class="li_box flex">
						<textarea id="import_enname" class="textarea">${goods.enname}</textarea>
					</div>
					<div class="li_box flex">
						<a target="_blank" href="${goods.aliGoodsUrl}"> <span
							class="p_txt">${goods.aliGoodsName}</span>
						</a>
					</div>
				</li>
				<li>
					<div class="li_box flex">
						<span class="li_name">产品重量：</span> <span class="li_desc">${goods.weight}KG</span>
					</div>
					<div class="li_box flex">
						<input id="import_weight" type="number" step="0.01"
							value="${goods.weight}" class="input" /><span>KG</span>
					</div>
					<div class="li_box flex">
						<p class="p_txt">${goods.aliGoodsWeight}</p>
					</div>
				</li>
				<li>
					<div class="li_box flex">
						<span class="li_name">运费价：--</span>
						<%-- <span class="li_desc">${goods.feeprice}USD</span> --%>
					</div>
					<div class="li_box flex">
						<input id="import_feeprice" type="number" step="0.01"
							value="${goods.feeprice}" class="input" /><span>USD</span>
					</div>
					<div class="li_box flex">
						<p class="p_txt">${goods.aliGoodsFeeprice}USD</p>
					</div>
				</li>
				<li>
					<div class="li_box flex">
						<span class="li_name">非免邮价：--</span>
						<%-- <span class="li_desc">${goods.fprice}USD</span> --%>
					</div>
					<div class="li_box flex">
						<input id="import_fprice" type="number" step="0.01"
							value="${goods.fprice}" class="input" /><span>USD</span>
					</div>
					<div class="li_box flex">
						<p class="p_txt">${goods.aliGoodsFprice}USD</p>
					</div>
				</li>
				<li>
					<div class="li_box flex">
						<span class="li_name">产品价格：</span> <span class="li_desc">${goods.goods1688Price}</span>
					</div>
					<div class="li_box flex">
						<input id="import_price" type="number" step="0.01"
							value="${goods.price}" class="input" /><span>USD</span>
					</div>
					<div class="li_box flex">
						<p class="p_txt">${goods.aliGoodsPrice}USD</p>
					</div>
				</li>
				<li>
					<div class="li_box flex">
						<span class="li_name">产品明细：</span> <span class="li_desc">${goods.detail}</span>
					</div>
					<div class="li_box flex">
						<input type="hidden" id="goods_savePath" value="${savePath}"
							name="savePath"> <input type="hidden"
							id="goods_localpath" value="${localpath}" name="localpath">
						<textarea id="import_endetail" class="textarea_ot">${goods.endetail}</textarea>
					</div>
					<div class="li_box flex">
						<p class="p_txt">${goods.aliGoodsDetails}</p>
					</div>
				</li>
				<li style="height: 800px;">
					<div style="width: 100%; height: 100%; overflow: scroll;"
						class="li_box flex">
						<span class="li_name">产品详情：</span>
						<div class="li_desc">${goods.info}</div>
					</div>
					<div style="width: 100%; height: 100%; overflow-x: auto;"
						class="li_box flex">
						<textarea id="goods_content" style="width: 100%; height: 800px;">${goods.detailEx} ${text}</textarea>
						<%-- <textarea id="goods_content" name="elm2"
						class="xheditor {tools:'|,/,Cut,Copy,Paste,Pastetext,Blocktag,Fontface,FontSize,Bold,Italic,Underline,Strikethrough,FontColor,BackColor,SelectAll,Removeformat,Align,List,Outdent,Indent,Link,Unlink,Anchor,Img,Hr,Table,Source,Preview,Fullscreen,',html5Upload:false,skin:'vista',upImgUrl:'/cbtconsole/editc/uploads?savePath=${savePath}',upImgExt:'jpg,jpeg,png',onUpload:insertUpload}"
						style="width: 100%; height: 800px;">${goods.detailEx} ${text}</textarea> --%>
						<!--<textarea id="deleteimage" rows="30" cols="211" name="deleteimage" class="xheditor {tools:'|,',html5Upload:false,skin:'vista'}" style="display:none;">	</textarea>-->
					</div>
					<div style="width: 100%; height: 100%; overflow: scroll;"
						class="li_box flex">
						<div id="ali_goods_info" style="width: 100%;">
							${goods.aliGoodsInfo}</div>
					</div>
				</li>

			</ul>

		</div>
	</div>
</body>
<script type="text/javascript">
	/* 图片回调函数  根据实际情况调用*/
	function insertUpload(msg) {
		alert("成功");
	}
</script>
</html>