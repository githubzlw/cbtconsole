<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<title>新品云产品详情编辑</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/xheditor-1.2.2.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/xheditor_lang/zh-cn.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/js/xheditor_skin/vista/iframe.css" />
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/css/custom_goods_details.css" />
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript">
	var imgJson = {};
	var tempJson = {};

	$(document).ready(function() {
		initDialog();
		closeDialog();
		initXheditor();
		imageMagnifying();
		imageSwitchingDisplay();
		aliImgToExpress();
		closeSimilarGoodsDialog();
	});

	function relieveDisabled(obj) {
		$(obj).removeAttr("readonly");
		$(obj).css("background-color", "rgb(255, 255, 255)");
	}

	function addDisabled(obj) {
		$(obj).attr("readonly", true);
		$(obj).css("background-color", "#d8d8d8");
	}

	//初始化xheditor
	function initXheditor() {
		$('#goods_content').xheditor({
			tools : "full",
			html5Upload : false,
			upBtnText : "上传",
			upMultiple : 1,
			upImgUrl : "/cbtconsole/newcloudEdit/uploads?pid=${goods.pid}",
			upImgExt : "jpg,jpeg,gif,png"
		});
	}

	//放大镜
	function imageMagnifying() {

		var mengban = $('.mengban');
		var lvjing = $('.lvjing');
		var big_pic = $('.big_pic');
		var big_img = $('.big_img');
		mengban.hover(function() {
			lvjing.show();
			big_pic.show();
		}, function() {
			lvjing.hide();
			big_pic.hide();
		})
		mengban.on("mousemove", function(evt) {
			var e = evt || window.event;
			var x = e.offsetX - lvjing.width() / 2;
			var y = e.offsetY - lvjing.height() / 2;
			var cols = mengban.width() / lvjing.width();
			if (x < 0) {
				x = 0;
			} else if (x > mengban.width() - lvjing.width()) {
				x = mengban.width() - lvjing.width();
			}
			if (y < 0) {
				y = 0;
			} else if (y > mengban.height() - lvjing.height()) {
				y = mengban.height() - lvjing.height();
			}
			lvjing.css({
				left : x + 'px',
				top : y + 'px'
			});
			big_img.css({
				left : -cols * x + 'px',
				top : -cols * y + 'px'
			});
		})
	}

	function imageSwitchingDisplay() {
		roastingImg();
		//删除橱窗图
		$('#delete_pic').click(function() {
			$.messager.confirm('系统提醒', '是否删除，删除保存后数据不可恢复', function(r){
				if (r){
					$('.ul_pic li').each(function(i) {
						if ($('.ul_pic li').eq(i).hasClass('red_border')) {
							$(this).remove();
							$('.ul_pic li').eq(0).trigger('click');
						}
						if (!$('.ul_pic li').length) {
							var mainImg = $("#goods_main_img").val();
							$('.init_img').attr("src",mainImg);
						}
						roastingImg();
					});
				}
			});	
		});

		//点击尺码图
		$('.ul_color').on(
				'click',
				'li',
				function() {
					$(this).addClass('red_border').siblings().removeClass(
							'red_border');
					if ($(this).hasClass('red_border')) {
						var img_src = $(this).find('img').attr('src');
						img_src = img_src.replace("_50x50.jpg", "").replace(
								"._SS47_.jpg", ".jpg").replace("50x50",
								"200X200").replace("32x32", "400x400").replace(
								"60x60", "400x400");
						$('.init_img').attr('src', img_src);
						$('.big_img').attr('src', img_src);
					}
				});
		//点击尺码图
		$('.ul_size').on(
				'click',
				'li',
				function() {
					$(this).addClass('red_border').siblings().removeClass(
							'red_border');
				})
	}

	//轮播图片
	function roastingImg() {
		//点击小图对应切换图片控制
		$('.ul_pic li').each(
				function(i) {
					$(this).click(
							function() {
								var img_src = $(this).find('img').attr('src');
								$(this).addClass('red_border').siblings()
										.removeClass('red_border');
								img_src = img_src.replace("_50x50.jpg", "")
										.replace("._SS47_.jpg", ".jpg")
										.replace("50x50", "200X200").replace(
												"32x32", "400x400").replace(
												"60x60", "400x400");
								$('.init_img').attr('src', img_src);
								$('.big_img').attr('src', img_src);
							})
				});
		//小图点击切换运动
		var $l = 0;
		var $len = ($(".ul_pic>li").length - 1) * 90;
		$(".prev").click(function() {
			$l += 90;
			$(".ul_pic").stop().animate({
				left : ($l + 25) + "px"
			})
			setTimeout(function() {
				if ($l >= 0) {
					$l = 0;
					$(".ul_pic").stop().animate({
						left : "25px"
					})
				}
			}, 100)
		})
		$(".next").click(function() {
			$l -= 90;
			$(".ul_pic").stop().animate({
				left : ($l + 25) + "px"
			})
			setTimeout(function() {
				if ($l < -$len) {
					$l = -$len;
					$(".ul_pic").stop().animate({
						left : -$len + 25 + "px"
					})
				}
			}, 100)
		})
	}

	function aliImgToExpress() {
		var img_src = "";
		var index = null;
		$('#ali_goods_info').find('img').each(function(i) {
			$(this).click(function() {
				var height = $(this).height();
				if (height >= 100) {
					$(this).toggleClass('img_checked');
					if ($(this).hasClass('img_checked')) {
						img_src = $('.r_box img').eq(i).attr('src');
						tempJson["" + i] = img_src;
					} else {
						tempJson["" + i] = "";
					}
				} else {
					showMessage('请选择高度大于100的图片');
				}
			});
		});
		
		$(".inp_style").click(function(){
			$(this).removeAttr("readonly");
			$(this).css("background-color", "rgb(255, 255, 255)");
		});
		$(".inp_style").blur(function(){
			$(this).attr("readonly", true);
			$(this).css("background-color", "#d8d8d8");
		});
	}

	/* function addToImportE(pid) {
		if ($("#to_left_sp").prop("disabled") == true) {
			return;
		} else {
			var img_tag = "";
			var notChoose = true;
			$('#ali_goods_info').find('img').each(function(i) {
				if ($(this).hasClass('img_checked')) {
					notChoose = false;
				}
			});
			if (notChoose) {
				showMessage('请选择高度大于100的图片');
			} else {
				var imgs = "";
				for ( var key in tempJson) {
					if (!(key in imgJson)) {
						if (tempJson[key] != "") {
							imgJson[key] = tempJson[key];
							imgs += ";" + tempJson[key];
							//img_tag += '<img style="max-width:500px;height:auto;display:block;margin:10px auto;" src="'+tempJson[key]+'" />';
						}
					}
				}
				//添加Einfo图片到本地和线上图片服务器
				uploadEinfoNetImg(imgs.substring(1), pid);
			}
		}
	} */

	//添加Einfo图片到本地和线上图片服务器
	function uploadEinfoNetImg(imgs, pid) {
		$("#to_left_sp").attr("disabled", true);
		$('.mask').show().text("正在上传图片，请等待...");

		$
				.ajax({
					type : 'POST',
					dataType : 'json',
					url : '/cbtconsole/newcloudEdit/uploadEinfoNetImg',
					data : {
						"imgs" : imgs,
						"pid" : pid
					},
					success : function(data) {
						$("#to_left_sp").removeAttr("disabled");
						$('.mask').hide();
						if (data.ok) {
							//showMessage("执行成功，图片异步上传，请保存后刷新界面");
							$.messager.alert("提醒",
									"执行成功，如若图片未显示，请保存后刷新界面，否则请重新上传",
									"info");
							var newImgUrls = data.data;
							var fmBody = $("iframe").contents().find("body");
							var urls = newImgUrls.split(";");
							if (urls.length > 0) {
								var srcs = '';
								for (var i = 0; i < urls.length; i++) {
									if (urls[i] != "") {
										srcs += '<img style="width:78%;" src="'+urls[i]+'" />'
									}
								}
								if (srcs != "") {
									fmBody.append(srcs);
								}
							}
							$('#ali_goods_info').find('img').removeClass(
									"img_checked");
						} else {
							$.messager.alert("提醒", data.message, "error");
						}
					},
					error : function(XMLResponse) {
						$("#to_left_sp").removeAttr("disabled");
						$('.mask').hide();
						$.messager.alert("提醒", "执行错误，请联系管理员", "error");
					}
				});
	}

	function showMessage(msg) {
		$('.mask').show().text(msg);
		setTimeout(function() {
			$('.mask').hide();
		}, 1500);
	}

	function showEasyUIMessage(msg) {
		$.messager.show({
			title : '提醒',
			msg : msgStr,
			timeout : 2000,
			showType : 'slide',
			style : {
				right : '',
				top : ($(window).height() * 0.35),
				bottom : ''
			}
		});
	}

	function doSaveDetalis(pid, type) {
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
		var content = $("#goods_content").val();
		if (content == "") {
			showMessage("获取商品描述详情为空");
			return;
		}
		var remotepath = $("#goods_remotepath").val();
		if (remotepath == "") {
			showMessage("获取图片远程路径为空");
			return;
		}
		//不校检商品属性
		var endetail = getGoodsAttributeInfo();
		var imgInfo = getImgInfo();
		if (imgInfo == "") {
			showMessage("获取橱窗图为空");
			return;
		}
		var price =""
		var singSkus ="";
		var range_price = $("#range_price_id").val();
		//区间价格标识为空时，获取wprice表示的数据
		var reg = /^[-+]?[1-9]\d*(\.\d{1,2})?$/;	
		
		if($("#import_price").length>0){
			range_price="";
			var isErr =true;
			/* $(".wprice_inp").each(function(){
				var wprice_num = $.trim($("#wprice_num_" + i).text());
				var wprice_val = $.trim($("#wprice_val_" + i).val());
				i++;
				if(wprice_num ==null || wprice_num ==""){	
					showMessage("区间价格[商品数量]获取失败");
					return false;
				}else if(!reg.test(wprice_val)){
					showMessage("区间价格[" + wprice_num + "]的价格异常");
					return false;
				}else{
					isErr = false;
					wprice += "," + wprice_num + "@" + wprice_val;
				}
			});	 */
			
			price = $.trim($("#import_price").val());
			
			if(!reg.test(price)){
				showMessage("价格异常");
				return false;
			}else{
				isErr = false;
			}
			if(isErr){
				return;
			}		
		}else{//不为空时，获取单个sku的所有数据
			var isErr =true;
			$(".inp_price").each(function(){
				var ppid = $(this).attr("id");
				var price = $(this).val();
				if(ppid ==null || ppid ==""){	
					showMessage("单规格价ID获取失败");
					return false;
				}else if(!reg.test(price)){
					showMessage("单规格价的价格异常");
					return false;
				}else{
					isErr = false;
					singSkus += ";" + ppid + "@" + price;
				}	
			});
			if(isErr){
				return;
			}
		}
		$('.mask').show().text('正在执行，请等待...');
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/cbtconsole/newcloudEdit/saveEditDetalis',
			data : {
				"type" : type,
				"pid" : pid,
				"enname" : enname,
				"weight" : weight,
				"remotepath" : remotepath,
				"imgInfo" : imgInfo,
				"endetail" : endetail,
				"content" : content,
				"sku" : singSkus.substring(1),
				"price" : price
				//"rangePrice" : range_price
			},
			success : function(data) {
				$('.mask').hide();
				if (data.ok) {
					showMessage(data.message);
					setTimeout(function() {
						window.location.reload();
					}, 1500);
				} else {
					$.messager.alert("提醒", data.message, "error");
				}
			},
			error : function(XMLResponse) {
				$('.mask').hide();
				$.messager.alert("提醒", "保存错误，请联系管理员", "error");
			}
		});
	}

	function getGoodsAttributeInfo() {
		var info = "";
		$("#attribute_table").find("tbody").find(".inp_style").each(function() {
			info += ";" + $(this).val().trim();
		});
		return info = "" ? "" : info.substring(1);
	}

	function getImgInfo() {
		var info = "";
		$(".li_pic").find("img").each(function() {
			info += ";" + $(this).attr("src").trim();
		});
		return info.substring(1);
	}

	function setGoodsValid(pid, type) {
		if (pid == "" || pid == "0" || pid == 0) {
			showMessage("pid为空");
			return;
		}
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/cbtconsole/newcloudEdit/setGoodsValid',
			data : {
				"type" : type,
				"pid" : pid
			},
			success : function(data) {
				if (data.ok) {
					showMessage("执行成功");
				} else {
					$.messager.alert("提醒", data.message, "error");
				}
			},
			error : function(XMLResponse) {
				$.messager.alert("提醒", "保存错误，请联系管理员", "error");
			}
		});
	}

	function doQuery() {
		var pid = $("#query_pid").val();
		if (pid == null || pid == "") {
			showMessage("PID为空");
		} else {
			window.location.href = "/cbtconsole/newcloudEdit/detalisEdit?pid=" + pid;
		}
	}

	function initDialog() {
		$('#type_select').combobox({
			valueField : 'id',
			textField : 'text',
			data : [ {
				"id" : "0",
				"text" : "本地图片"
			}, {
				"id" : "1",
				"text" : "网络图片"
			} ],
			onChange : function(newValue, oldValue) {
				if (newValue == "0") {
					$('#local_picture').filebox('enable');
					$('#net_url').textbox('disable'); //设置输入框为禁用
				} else {
					$('#local_picture').filebox('disable');//设置文件上传框为禁用
					$('#net_url').textbox('enable');
				}
			}
		});
		$('#type_select').combobox('panel').height(60);
		$('#type_select').combobox('select', '0');
		$('#local_picture').filebox('enable');
		$('#net_url').textbox('disable'); //设置输入框为禁用	
		//closeDialog();
	}

	function uploadTypePicture(pid) {
		var selectVal = $('#type_select').combobox('getValue');
		//选择本地文件的使用form提交后台
		var formData = new FormData($("#uploadFileForm")[0]);
		if (selectVal == '0') {
			$.messager.progress({
				title : '上传本地图片',
				msg : '请等待...'
			});
			$
					.ajax({
						url : '/cbtconsole/newcloudEdit/uploadByJs',
						type : 'POST',
						data : formData,
						contentType : false,
						processData : false,
						success : function(data) {
							$.messager.progress('close');
							if (data.ok) {
								showMessage("执行成功");
								var srcs = '<li class="li_pic"><img src="'+data.data+'" /></li>';
								if (srcs != "") {
									$(".ul_pic").prepend(srcs);
								}
								var liLen = $('.ul_pic li').length;
								roastingImg();
								$('.ul_pic li').eq(0).trigger('click');
								closeDialog();
							} else {
								$.messager.alert("提醒", data.message, "info");
							}
						},
						error : function(XMLResponse) {
							$.messager.progress('close');
							$.messager.alert("提醒", "执行错误，请联系管理员", "error");
						}
					});
		} else if (selectVal == '1') {
			//选择网络图片的直接ajax传递后台
			var url = $('#net_url').textbox('getValue');
			$.messager.progress({
				title : '上传网络图片',
				msg : '请等待...'
			});
			$
					.ajax({
						type : 'POST',
						dataType : 'json',
						url : '/cbtconsole/newcloudEdit/uploadTypeNetImg',
						data : {
							"imgs" : url,
							"pid" : pid,
							"check" : 1
						},
						success : function(data) {
							closeDialog();
							$.messager.progress('close');
							if (data.ok) {
								//showMessage("执行成功，图片异步上传，请保存后刷新界面");
								$.messager.alert("提醒",
										"执行成功，如若图片未显示，请保存后刷新界面，否则请重新上传",
										"info");
								var newImgUrls = data.data;
								var urls = newImgUrls.split(";");
								if (urls.length > 0) {
									var srcs = '';
									for (var i = 0; i < urls.length; i++) {
										if (urls[i] != "") {
											srcs += '<li class="li_pic"><img src="'+urls[i]+'" /></li>';
										}
									}
									if (srcs != "") {
										$(".ul_pic").prepend(srcs);
									}
								}
								roastingImg();
								$('.ul_pic li').eq(0).trigger('click');
							} else {
								$.messager.alert("提醒", data.message, "error");
							}
						},
						error : function(XMLResponse) {
							$.messager.progress('close');
							$.messager.alert("提醒", "执行错误，请联系管理员", "error");
						}
					});
		} else {
			showMessage("请选择上传类型");
		}
	}

	function showDialog() {
		$('#type_select').combobox('select', '0');
		$('#pic_dlg').dialog('open');
	}

	function closeDialog() {
		$('#pic_dlg').dialog('close');
		$("#uploadFileForm")[0].reset();
	}
	
	function useAliGoodsDetails(pid){
		var ali_goods_info = $("#ali_goods_info").html();
		if(ali_goods_info == null || ali_goods_info == ""){
			showMessage("获取速卖通详情失败");
		}else{
			$.messager.confirm('提示', '是否使用速卖通详情?使用后编辑的详情数据将覆盖', function(rs) {
				if (rs) {
					$("#use_ali_goods").attr("disabled", true);
					$.messager.progress({
						title : '正在处理数据',
						msg : '请等待...'
					});
					$.ajax({
						type : 'POST',
						dataType : 'text',
						url : '/cbtconsole/newcloudEdit/useAliGoodsDetails',
						data : {
							"pid" : pid,
							"aliGoodsInfo" : ali_goods_info
						},
						success : function(data) {
							$.messager.progress('close');
							$("#use_ali_goods").removeAttr("disabled");
							var json = eval('(' + data + ')');
							if (json.ok) {
								showMessage("执行成功");
								$("#goods_content").val(json.data);							
							} else {
								showMessage(json.message);
							}
						},
						error : function() {
							$.messager.progress('close');
							$("#use_ali_goods").removeAttr("disabled");
							$.messager.alert("提醒", "error", "error");
						}
					});
				}
			});
			
		}
	}
	
	function openSimilarGoodsDialog(){		
		$('#similar_dlg').dialog('open');	
	}
	
	function closeSimilarGoodsDialog() {
		$('#similar_dlg').dialog('close');
		$("#similar_pids").val("");
	}
	
	function addSimilarGoods(){
		var mainPid = $("#main_pid").val();
		if(mainPid == null || mainPid == ""){
			showMessage("获取商品pid失败，请重试");
			return false;
		}		
		var similarPids = $("#similar_pids").val();
		if(similarPids == null || similarPids == ""){
			showMessage("获取相似商品pid失败，请重试");
			return false;
		}
		$.messager.progress({
			title : '正在保存数据',
			msg : '请等待...'
		});
		$.ajax({
			type : 'POST',
			dataType : 'text',
			url : '/cbtconsole/newcloudEdit/addSimilarGoods',
			data : {
				"mainPid" : mainPid,
				"similarPids" : similarPids
			},
			success : function(data) {
				$.messager.progress('close');
				var json = eval('(' + data + ')');
				closeSimilarGoodsDialog();
				if (json.ok) {										
					showMessage(json.message);
					querySimilarGoodsByMainPid();	
				} else {
					$.messager.alert("提醒", json.message, "error");
				}
			},
			error : function() {
				$.messager.progress('close');
				$.messager.alert("提醒", "error", "error");
			}
		});
	}
	
	function querySimilarGoodsByMainPid(){
		var mainPid = $("#main_pid").val();
		if(mainPid == null || mainPid == ""){
			return false;
		}	
		$.ajax({
			type : 'POST',
			dataType : 'text',
			url : '/cbtconsole/newcloudEdit/querySimilarGoodsByMainPid',
			data : {
				"mainPid" : mainPid
			},
			success : function(data) {
				var json = eval('(' + data + ')');
				if (json.ok) {				
					var data = json.data;
					if(data.length > 0){
						$("#similar_goods_ul").empty();
						var lis = '';
						for(var i=0;i<data.length;i++){
							lis += '<li class="li_pic_2"><a target="_blank" href="/cbtconsole/newcloudEdit/detalisEdit?pid=';
							lis += data[i].similarPid +'"><img class="img_sty" src="'+data[i].similarGoodsImg+'"></a></li>';
						}
						$("#similar_goods_ul").append(lis);
					}												
				} else {
					showMessage(json.message);
				}
			},
			error : function() {
				showMessage("查询相似商品失败");
			}
		});
	}
</script>
</head>

<body onload="querySimilarGoodsByMainPid()">

	<c:if test="${uid ==0}">
		{"status":false,"message":"请重新登录进行操作"}
	</c:if>
	
	<c:if test="${uid < 0}">
		<div>
			<p style="margin-left: 700px;font-size: 28px;color: red;margin-top: 200px;">获取产品失败</p>
		</div>
	</c:if>

	<c:if test="${uid > 0}">

		<div class="mask"></div>
		<div id="pic_dlg" class="easyui-dialog" title="上传橱窗图"
			data-options="modal:true"
			style="width: 460px; height: 330px; padding: 10px;">
			<form id="uploadFileForm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="pid" value="${goods.pid}" />
				<div style="margin-bottom: 20px">
					上传类型:<select id="type_select" name="type" class="easyui-combobox"
						style="width: 360px;" placeholder="请选择上传类型">
					</select>
				</div>
				<div style="margin-bottom: 20px">
					图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片:<input
						id="local_picture" name="uploadfile" class="easyui-filebox"
						data-options="prompt:'请选择一张分辨率大于400*200的图片...'"
						style="width: 360px">
				</div>
				<div style="margin-bottom: 20px">
					网络链接:<input id="net_url" name="neturl" class="easyui-textbox"
						style="width: 360px; height: 100px;"
						data-options="multiline:true,prompt:'请输入图片链接'"> <br>
					<span style="color: red; margin-left: 50px;">多个链接请用&nbsp;<b>英文分号(;)</b>&nbsp;分开,图片分辨率需大于400*200
					</span>
				</div>
			</form>

			<div style="text-align: center; padding: 5px 0">
				<a href="javascript:void(0)" data-options="iconCls:'icon-add'"
					class="easyui-linkbutton"
					onclick="uploadTypePicture('${goods.pid}')" style="width: 80px">确认上传</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
					class="easyui-linkbutton" onclick="closeDialog()"
					style="width: 80px">关闭</a>
			</div>
		</div>
		
		<div id="similar_dlg" class="easyui-dialog" title="添加相似商品"
			data-options="modal:true"
			style="width: 460px; height: 280px; padding: 10px;">
			<br>
			当前商品PID:<input type="text" id="main_pid" value="${goods.pid}" disabled="disabled"/>
			<br>
			<span><b style="font-size: 16px; color: red;">请输入相似商品PID，多个请用英文分号(;)隔开</b></span>
			<br>
			<!-- 相似商品PID:<input type="text" id="similar_pids" value="" placeholder="请输入相似商品PID" /> -->
			相似商品PID:<textarea id="similar_pids" style="width: 300px; height: 88px;"></textarea>
			<br><br>

			<div style="text-align: center; padding: 5px 0">
				<a href="javascript:void(0)" data-options="iconCls:'icon-add'"
					class="easyui-linkbutton"
					onclick="addSimilarGoods('${goods.pid}')" style="width: 80px">保存</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
					class="easyui-linkbutton" onclick="closeSimilarGoodsDialog()"
					style="width: 80px">关闭</a>
			</div>
		</div>
		
		<div class="allMain">
			<div class="s_top">
				<span class="s_last2"> <label for="query_pid"><b>新品云PID:</b></label>
					<input id="query_pid" value="${goods.pid}" placeholder="请输入PID" />
					<i class="s_btn" onclick="doQuery()">查询</i>
				</span>
					<c:if test="${goods.canEdit > 0}">
					<c:if test="${goods.canEdit != uid}">
						<span><b style="font-size: 18px; color: red;">*非当前商品的编辑人，不能进行保存操作</b></span>
					</c:if>
					<c:if test="${goods.canEdit == uid}">
						<span class="s_btn" onclick="doSaveDetalis('${goods.pid}',0)">保存</span>
						<span class="s_btn" onclick="doSaveDetalis('${goods.pid}',1)">保存并发布</span>
						<span class="s_btn" onclick="setGoodsValid('${goods.pid}',-1)">下架该商品</span>
						<%-- <span class="s_btn" onclick="setGoodsValid('${goods.pid}',1)">检查通过</span>
						<span class="s_last">*无需修改时点击检查通过</span> --%>
					</c:if>

                                        </c:if>	
							<c:if test="${goods.canEdit == 0}">
					<span class="s_btn" onclick="doSaveDetalis('${goods.pid}',0)">保存</span>
					<span class="s_btn" onclick="doSaveDetalis('${goods.pid}',1)">保存并发布</span>
					<span class="s_btn" onclick="setGoodsValid('${goods.pid}',-1)">下架该商品</span>
					<%-- <span class="s_btn" onclick="setGoodsValid('${goods.pid}',1)">检查通过</span>
					<span class="s_last">*无需修改时点击检查通过</span> --%>
				</c:if>


			</div>
			<div class="all_s">
				<div class="s_l">
					<p class="s_tit">
						<label class="label"> 产品名称：<input id="import_enname"
							type="text" class="s_txt" value="${goods.enname}" />
						</label>
					</p>
					<div class="s_box">
						<div class="detail_goods">
							<div class="goods_img">
								<div class="init_pic">
									<img src="${goods.showMainImage}" class="init_img">
									<div class="mengban"></div>
									<div class="lvjing"
										style="display: none; left: 141px; top: 30px;"></div>
								</div>
								<div class="big_pic" style="display: none;">
									<img src="/cbtconsole/img/init_pic.png" class="big_img"
										style="left: -282px; top: -60px;">
								</div>
								<div class="pic_list">
									<p class="prev">
										<span class="prev_arrow"></span>
									</p>
									<ul class="ul_pic">
										<c:forEach items="${showimgs}" var="imgBean"
											varStatus="imgIndex">
											<li class="li_pic"><img src="${imgBean}"></li>
										</c:forEach>
									</ul>
									<p class="next">
										<span class="next_arrow"></span>
									</p>
								</div>
								<div class="clear_box">
									<span class="clear_clo" onclick="showDialog()">添加</span> <span
										id="delete_pic" class="clear_clo">删除</span> <span
										class="clear_txt">*删除所选中的橱窗图</span>
								</div>
							</div>
							<div class="goods_detail">
								<div class="goods_top">
									<c:set value="" var="typeName"></c:set>
									<c:set value="${fn:length(showtypes)}" var="typeLength"></c:set>
									<c:forEach items="${showtypes}" var="typeBean"
										varStatus="typeIndex">
										<c:if test="${typeName != typeBean.lableType }">
											<c:if test="${typeName !=''}">
												</ul>
								</div>
	</c:if>
	<div class="goods_type_${typeBean.type}">
		<p class="goods_color">${typeBean.type}:</p>
		<c:if test="${typeBean.img!='null' && typeBean.img !=''}">
			<ul class="ul_color">
				<!-- </ul> -->
		</c:if>
		<c:if test="${typeBean.img=='null' || typeBean.img ==''}">
			<ul class="ul_size">
				<!-- </ul> -->
		</c:if>
		<c:set value="${typeBean.lableType}" var="typeName"></c:set>
		</c:if>

		<li
			class="${typeBean.img=='null'||typeBean.img==''?'li_size':'li_color'}">
			<c:if test="${typeBean.img!='null' && typeBean.img !=''}">
				<img class="img_limit" src="${typeBean.img}" alt="${typeBean.value}"
					title="${typeBean.value}" />
			</c:if> <c:if test="${typeBean.img=='null' || typeBean.img ==''}">
				<em title="${typeBean.value}">${typeBean.value}</em>
			</c:if>
		</li>
		<c:if test="${typeIndex.index== typeLength-1}">
			</ul>
	</div>
	</c:if>
	</c:forEach>


	<input id="range_price_id" value="${goods.rangePrice}" type="hidden" />
	<c:if test="${not empty showSku}">
		<div class="goods_p">
			<p class="goods_color">单规格价:</p>
			<table border="1" cellspacing="0" cellpadding="0" class="table_style">
				<tr>
					<c:forEach var="type_name" items="${typeNames}"
						varStatus="nameIndex">
						<td id="type_name_${type_name.key}">${type_name.value}</td>
					</c:forEach>
					<td id="type_name_choose">单价<br> <input type="checkbox"
						onclick="allSamePrice(this)" />全部相同
					</td>
				</tr>

				<c:forEach var="sku_bean" items="${showSku}" varStatus="skuIndex">
					<tr>
						<c:forEach var="tp_ar" items="${fn:split(sku_bean.skuAttrs,';')}">
							<td id="combine_id_${fn:split(tp_ar,'@')[0]}">${fn:split(tp_ar,'@')[2]}</td>
						</c:forEach>
						<td><input class="inp_style inp_price" title="单击可进行编辑"
							id="${sku_bean.ppIds}" value="${sku_bean.price}" /></td>
					</tr>
				</c:forEach>
			</table>
		</div>

	</c:if>
	<c:if test="${empty  showSku}">
		<div class="goods_p">
			<p class="goods_color">价格:</p>

			<table border="1" cellspacing="0" cellpadding="0" class="table_style">
				<tr>
					<td>价格($)</td>
				</tr>
				<tr>
					<td><input type="text" id="import_price"
						class="inp_style wprice_inp" title="单击可进行编辑"
						value="${goods.price}" /></td>
				</tr>
			</table>
		</div>
	</c:if>

	<div class="goods_p">
		<p class="goods_color">重量:</p>
		<p class="ul_size">
			<input id="import_weight" type="number" class="pr_txt"
				value="${goods.finalWeight}" step="0.01" /> <span class="goods_cur">
				KG</span>
		</p>
	</div>
	<div class="goods_p">
		<p class="goods_color">属性:</p>
		<div class="ul_size ul_size_p">
			<table border="1px" bordercolor="#ccc" cellspacing="0px"
				style="border-collapse: collapse" width="100%" id="attribute_table">
				<c:set value="${fn:length(showattribute)}" var="itemLength"></c:set>
				<tr>
					<c:forEach var="item" items="${showattribute}"
						varStatus="itemIndex">
						<td width="32%"><input type="text" class="inp_style"
							title="单击可进行编辑" id="sku_id_${item.key}" name="sku_key_val"
							value="${item.value}" /></td>
						<c:if
							test="${(itemIndex.index+1) % 3 == 0 || itemIndex.index == itemLength-1}">
				</tr>
				</c:if>
				</c:forEach>
			</table>
		</div>
	</div>
	</div>
	</div>
	</div>
	</div>
	</div>
	<div class="s_r">
		<div class="table_su">
			<span> <c:if
					test="${goods.isEdited == '1' || goods.isEdited == '2'}">
					<b style="font-size: 16px;"> 编辑状态：${goods.isEdited == '1' ? '仅标题已编辑':'标题和描述已编辑'}</b>
					<br>
					<b style="font-size: 16px;">编辑时间：${goods.updatetime}</b>
					<br>
					<b style="font-size: 16px;">编辑人：${goods.admin}</b>
					<br>
				</c:if> 
			</span>
		</div>

		
	</div>


	<div class="s_bot">
		<h1 style="text-align: center; color: red;">请不要全盘copy，请去掉所有有“品牌”、“店名”的图，请去掉所有有“aliexpress”字样的图</h1>
		<div class="bot_l">
			<div class="b_left">
				<h1 style="text-align: center">importE详情编辑框</h1>
				<input type="hidden" id="goods_savePath" value="${savePath}"
					name="savePath"> <input type="hidden" id="goods_localpath"
					value="${localpath}" name="localpath"> <input type="hidden"
					id="goods_remotepath" value="${goods.remotpath}" name="remotepath">
				<textarea id="goods_content" rows="100" style="width: 100%;">${text}</textarea>
			</div>
		</div>
	</div>
	</div>
	</c:if>
</body>
<script type="text/javascript">

function allSamePrice(obj){
	if($(obj).attr("checked")){
		$(obj).removeAttr("checked"); 
	}else{
		$.messager.prompt('提示', '请输入价格:', function(is){
			if (is){
				var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,2})?$)/;
				if (!reg.test(is)) {
					$(obj).removeAttr("checked"); 
					showMessage('价格必须为正数，最多两位小数！');
	            } else {
	            	$(".inp_price").val(is);
	            	$(obj).attr("checked","checked");
	            }				
			}else{
				showMessage('未输入价格或取消输入！');
				$(obj).removeAttr("checked"); 
			}
		});
	}	
}
</script>
</html>