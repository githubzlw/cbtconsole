<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>准备上线商品展示</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<style type="text/css">
.img_sty {
	max-width: 180px;
	max-height: 180px;
}

.div_sty {
	width: 370px;
}

.s_btn {
	display: inline-block;
	width: 200px;
	height: 40px;
	background: #169bd4;
	margin: 2px 20px;
	border-radius: 10px;
	line-height: 40px;
	text-align: center;
	color: #fff;
	cursor: pointer;
	font-size: 14px;
}

.s_btn_2 {
	display: inline-block;
	width: 80px;
	height: 40px;
	background: #169bd4;
	margin: 20px 20px;
	border-radius: 10px;
	line-height: 40px;
	text-align: center;
	color: #fff;
	cursor: pointer;
	font-size: 14px;
}

.del_btn {
	border-radius: 10px;
	height: 40px;
	width: 80px;
	margin-right: 40px;
	border-color: #2e6da4;
	color: white;
	background-color: red;
}

.check_sty {
	height: 24px;
	float: left;
	width: 24px;
}

.checkBg {
	background-color: #b6f5b6;
}
</style>
<script type="text/javascript">

	//数据清洗
	function doGoodsClear(sourceTbl,saveTbl) {
	
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/cbtconsole/CoreUrlC/doGoodsClear.do',
			data : {
				sourceTbl : sourceTbl,
				saveTbl : saveTbl
			},
			success : function(data) {
				if (data.ok) {
					$.messager.alert("提醒", "数据已经清洗，是异步执行，该画面可关闭", "info");
					/* setTimeout(function() {
						window.location.reload();
					}, 500); */
				} else {
					$.messager.alert("提醒", data.message, "error");
				}
			},
			error : function(XMLResponse) {
				$.messager.alert("提醒", "保存错误，请联系管理员", "error");
			}
		});
	}
	
	//产品发布上线
	function publishGoods(sourceTbl,saveTbl) {
		$.messager.confirm('系统提醒', '是否发布商品到线上？', function(r) {
			if (r) {
				$.messager.progress({
					title : '正在上传本地图片和数据，请等待...',
					msg : '可关闭当前界面或者等待一段时间后刷新页面'
				});
				setTimeout(function() {
					$.messager.progress('close');
				}, 60000);
				$.ajax({
					type : 'POST',
					dataType : 'json',
					url : '/cbtconsole/CoreUrlC/publishEditGoods',
					data : {
						sourceTbl : sourceTbl,
						saveTbl : saveTbl
					},
					success : function(data) {
						$.messager.progress('close');
						if (data.ok) {
							//$.messager.alert("提醒","已经发布，是异步执行，该画面可关闭。","info");
							/* if(data.total > 0){
								syncAndUpdateShopGoods(shopId,data.data);
							}else{
								$.messager.alert("提醒", "执行成功，即将刷新界面", "info");
								setTimeout(function() {
									window.location.reload();
								}, 1500);
							}		 */					
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
		});
	}
	
</script>
</head>
<body>
	<div>
		<h1 align="center">核心商品上线</h1>
		<div>
			<input class="s_btn" type="button" value="机器对标核心商品清洗"
					onclick="doGoodsClear('ali_core_bench_details','new_core_goods_ready')" />
					
			<input class="s_btn" type="button" value="机器对标核心商品发布"
				onclick="publishGoods('ali_core_bench_details','new_core_goods_ready')" />
				
			<br>
			<input class="s_btn" type="button" value="亚马逊热卖商品清洗"
					onclick="doGoodsClear('ali_info_data_details','amazon_goods_ready')" />
					
			<input class="s_btn" type="button" value="亚马逊热卖商品发布"
				onclick="publishGoods('ali_info_data_details','amazon_goods_ready')" />
				
				
		</div>
	</div>
</body>
</html>