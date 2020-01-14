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
	width: 160px;
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
	function deleteShopReadyGoods(shopId) {	
		$.messager.confirm('系统提醒', '是否删除，删除保存后数据不可恢复', function(r) {
			if (r) {
				var pids = "";
				$(".isChoose").each(function() {
					var checkVal = $(this).val();
					pids += "," + checkVal;
				});
				if (pids == "") {
					$.messager.alert("提醒", "请选择需要删除的商品", "info");
				}else{
					$.ajax({
						type : 'POST',
						dataType : 'json',
						url : '/cbtconsole/ShopUrlC/deleteShopReadyGoods.do',
						data : {
							shopId : shopId,
							pids : pids.substring(1)
						},
						success : function(data) {
							if (data.ok) {
								$.messager.alert("提醒", "执行成功", "info");
								window.location.reload();
							} else {
								$.messager.alert("提醒", data.message, "error");
							}
						},
						error : function(XMLResponse) {
							$.messager.alert("提醒", "保存错误，请联系管理员", "error");
						}
					});
				}
				
			}
		});

	}

    function setShopGoodsNoSold(shopId) {
        var pids = "";
        $(".isChoose").each(function () {
            var checkVal = $(this).val();
            pids += "," + checkVal;
        });
        if (pids == "") {
            $.messager.alert("提醒", "请选择需要过滤销量的商品", "info");
        } else {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/ShopUrlC/setShopGoodsNoSold.do',
                data: {
                    shopId: shopId,
                    pids: pids.substring(1)
                },
                success: function (data) {
                    if (data.ok) {
                        $.messager.alert("提醒", "执行成功", "info");
                        window.location.reload();
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }
    }

	function publishGoods(shopId) {
		$.messager.confirm('系统提醒', '是否发布店铺商品到线上？', function(r) {
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
					url : '/cbtconsole/ShopUrlC/publishEditGoods',
					data : {
						shopId : shopId
					},
					success : function(data) {
						$.messager.progress('close');
						if (data.ok) {
							if(data.total > 0){
								syncAndUpdateShopGoods(shopId,data.data);
							}else{
								$.messager.alert("提醒", "执行成功，即将刷新界面", "info");
								setTimeout(function() {
									window.location.reload();
								}, 1500);
							}							
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
	
	function syncAndUpdateShopGoods(shopId,pids){
		$.messager.confirm('系统提醒', '当前商品:' + pids + '，已经存在且被编辑，是否发布店铺商品到线上？', function(r) {
			if (r) {
				$.ajax({
					type : 'POST',
					dataType : 'json',
					url : '/cbtconsole/ShopUrlC/publishCheckEditGoods',
					data : {
						shopId : shopId
					},
					success : function(data) {
						$.messager.progress('close');
						if (data.ok) {
							if(data.total > 0){
								
							}else{
								$.messager.alert("提醒", "执行成功，即将刷新界面", "info");
								setTimeout(function() {
									window.location.reload();
								}, 1500);
							}							
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
	
	function readyToOnline(shopId){
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/cbtconsole/ShopUrlC/readyToOnline',
			data : {
				shopId : shopId
			},
			success : function(data) {
				$.messager.progress('close');
				if (data.ok) {
					$.messager.alert("提醒", "执行成功，即将刷新界面", "info");
					setTimeout(function() {
						window.location.reload();
					}, 1500);
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

    function selectAll() {
	    var obj;
        $("#new_table").find(".div_sty").each(function () {
            obj = $(this).find(".check_sty");
            $(obj).prop("checked", true);
            $(obj).addClass("isChoose");
            $(obj).parent().parent().addClass("checkBg");
        });
    }

    function selectNotAll() {
	    var obj;
        $("#new_table").find(".div_sty").each(function () {
            obj = $(this).find(".check_sty");
            $(obj).prop("checked", false);
            $(obj).removeClass("isChoose");
			$(obj).parent().parent().removeClass("checkBg");
        });
    }
</script>
</head>
<body>

	<c:if test="${show == 0}">
		<h1 align="center">${msgStr}</h1>
	</c:if>
	<c:if test="${show > 0}">

		<div>
			<h1 align="center">准备上线商品展示<span
					style="color:${onlineState==2 ? 'green':'red'}">(状态：${stateDescribe}，商品总数：${goodsNum})</span></h1>
			<div>
				<%-- <a target="_blank"
					href="/cbtconsole/ShopUrlC/showShopPublicImg.do?shopId=${shopId}&useHm=0">查看公共图片</a>
				&nbsp;&nbsp;&nbsp; --%>
				<a target="_blank" style="color: #f50595;" title="打开较慢，请先点击“查看公共图片”，等待图片处理完成后再点击"
					href="/cbtconsole/ShopUrlC/showShopPublicImgTest.do?shopId=${shopId}&useHm=1">查看公共图片(含图片识别)</a>
				<input class="s_btn" type="button" value="发布/重新上线"
					onclick="publishGoods('${shopId}')" />
				<input class="s_btn" type="button" value="标识处理完成（待上线）"
					onclick="readyToOnline('${shopId}')" />
					&nbsp;&nbsp;&nbsp;
					<input class="del_btn" type="button" value="删除商品" onclick="deleteShopReadyGoods('${shopId}')">	
					<a target="_blank" href="http://192.168.1.27:9089/pap/translation.jsp">翻译词典管理</a>
					<input class="s_btn" type="button" value="标记上线不过滤销量" onclick="setShopGoodsNoSold('${shopId}')">
					<h3 style="color:red;" align="center">(请注意“已经在线上”的商品，如果不需要上线，请删除，否则“已经在线上”的商品点击“发布”按钮后，将进行数据更新)</h3>	
			</div>
			<br>
			
			<c:if test="${fn:length(isEditedList) > 0}">
			
			<table border="3" cellpadding="0" cellspacing="0" align="left">
			<thead> <tr> <td colspan="5"><b style="color:red;font-size:18px;">在线已编辑商品(总数${fn:length(isEditedList)})</b> </td>  </tr> </thead>
				<c:set var="total1" value="${fn:length(isEditedList)}" />
				<c:set var="count1" value="0" />
				<c:forEach items="${isEditedList}" var="goods" varStatus="index">
					<c:set var="count1" value="${count1 + 1 }" />
					<c:if test="${count1 == 1}">
						<tr>
					</c:if>
					<td><div class="div_sty">
							<input type="checkbox" class="check_sty" onclick="chooseBox(this)" value="${goods.pid}"/>
							<div>							 
								<a target="_blank" href="https://detail.1688.com/offer/${goods.pid}.html" ><img class="img_sty" src="${goods.imgUrl}" /></a>
								<br>价格:<span>${goods.showPrice}</span><span>$</span>
								<br>名称:<span>${goods.enName}</span>
							</div>
							<br>
							<div style="margin-bottom: 2px;">
								<span style="color:${goods.valid == 1 ? 'green':'red'};">数据清洗:${goods.valid == 1 ? '有效数据' : '无效数据'}</span>
								<br>
								<span style="color:${goods.syncFlag == 1 ? 'green':'red'};">商品状态:${goods.syncDescribe}</span>
								<br>
								<c:if test="${goods.onlineFlag > 0}">
									<b style="color:red;">已经在线上状态:[${goods.onlineValid > 0 ? '上架':'下架'}-${goods.onlineEdit > 0 ? '已编辑':'未编辑'}]</b>
								</c:if>
								
								<c:if test="${goods.syncFlag == 2}">
									<span style="color: red;">原因:${goods.syncRemark}</span>
									<br>
								</c:if>		
								<br>						
								<c:if test="${goods.enInfoNum == 0}">
									<b><a target="_blank" style="color:#f50ed8"
									href="/cbtconsole/ShopUrlC/editGoods.do?shopId=${goods.shopId}&pid=${goods.pid}">编辑详情(无详情图片)</a></b>
								</c:if>
								<c:if test="${goods.enInfoNum > 0}">
									<a target="_blank"
									href="/cbtconsole/ShopUrlC/editGoods.do?shopId=${goods.shopId}&pid=${goods.pid}">编辑详情</a>
								</c:if>								
								<c:if test="${goods.syncFlag == 1}">
									<!-- https://www.import-express.com http://192.168.1.29:8081 -->
									&nbsp;&nbsp;&nbsp;&nbsp;<a target="_blank"
										href="https://www.import-express.com/spider/detail?${goods.goodsUrl}">线上预览</a>
								</c:if>
							</div>
						</div></td>
					<c:if test="${count1 % 5 == 0}">
						</tr><tr>
					</c:if>
				</c:forEach>
				<c:if test="${total1 % 4 > 0}">
					</tr>
				</c:if>
			</table>
			<br>
			
			</c:if>
			<c:if test="${fn:length(unEditedList) > 0}">
			
			<table border="3" cellpadding="0" cellspacing="0" align="left">
			<thead> <tr> <td colspan="5"><b style="color:red;font-size:18px;">在线未编辑商品(总数${fn:length(unEditedList)})</b></td>  </tr> </thead>
				<c:set var="total2" value="${fn:length(unEditedList)}" />
				<c:set var="count2" value="0" />
				<c:forEach items="${unEditedList}" var="goods" varStatus="index">
					<c:set var="count2" value="${count2 + 1 }" />
					<c:if test="${count2 == 1}">
						<tr>
					</c:if>
					<td><div class="div_sty">
							<input type="checkbox" class="check_sty" onclick="chooseBox(this)" value="${goods.pid}"/>
							<div>							 
								<a target="_blank" href="https://detail.1688.com/offer/${goods.pid}.html" ><img class="img_sty" src="${goods.imgUrl}" /></a>
								<br>价格:<span>${goods.showPrice}</span><span>$</span>
								<br>名称:<span>${goods.enName}</span>
							</div>
							<br>
							<div style="margin-bottom: 2px;">
								<span style="color:${goods.valid == 1 ? 'green':'red'};">数据清洗:${goods.valid == 1 ? '有效数据' : '无效数据'}</span>
								<br>
								<span style="color:${goods.syncFlag == 1 ? 'green':'red'};">商品状态:${goods.syncDescribe}</span>
								<br>
								<c:if test="${goods.onlineFlag > 0}">
									<b style="color:red;">已经在线上状态:[${goods.onlineValid > 0 ? '上架':'下架'}-${goods.onlineEdit > 0 ? '已编辑':'未编辑'}]</b>
								</c:if>
								
								<c:if test="${goods.syncFlag == 2}">
									<span style="color: red;">原因:${goods.syncRemark}</span>
									<br>
								</c:if>		
								<br>						
								<c:if test="${goods.enInfoNum == 0}">
									<b><a target="_blank" style="color:#f50ed8"
									href="/cbtconsole/ShopUrlC/editGoods.do?shopId=${goods.shopId}&pid=${goods.pid}">编辑详情(无详情图片)</a></b>
								</c:if>
								<c:if test="${goods.enInfoNum > 0}">
									<a target="_blank"
									href="/cbtconsole/ShopUrlC/editGoods.do?shopId=${goods.shopId}&pid=${goods.pid}">编辑详情</a>
								</c:if>								
								<c:if test="${goods.syncFlag == 1}">
									<!-- https://www.import-express.com http://192.168.1.29:8081 -->
									&nbsp;&nbsp;&nbsp;&nbsp;<a target="_blank"
										href="https://www.import-express.com/spider/detail?${goods.goodsUrl}">线上预览</a>
								</c:if>
							</div>
						</div></td>
					<c:if test="${count2 % 5 == 0}">
						</tr><tr>
					</c:if>
				</c:forEach>
				<c:if test="${total2 % 4 > 0}">
					</tr>
				</c:if>
			</table>
			<br>
			</c:if>
			<c:if test="${fn:length(newList) > 0}">
			
			<table id="new_table" border="3" cellpadding="0" cellspacing="0" align="left">
			<thead>
                <tr>
                    <td colspan="5">
                        <b style="color:red;font-size:18px;">新的商品(总数${fn:length(newList)})</b>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input class="s_btn_2" type="button" value="全选" onclick="selectAll()"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input class="s_btn_2" type="button" value="清除选中" onclick="selectNotAll()"/>
                    </td>
                </tr>
            </thead>
				<c:set var="total3" value="${fn:length(newList)}" />
				<c:set var="count3" value="0" />
				<c:forEach items="${newList}" var="goods" varStatus="index">
					<c:set var="count3" value="${count3 + 1 }" />
					<c:if test="${count3 == 1}">
						<tr>
					</c:if>
					<td><div class="div_sty">
							<input type="checkbox" class="check_sty" onclick="chooseBox(this)" value="${goods.pid}"/>
							<div>							 
								<a target="_blank" href="https://detail.1688.com/offer/${goods.pid}.html" ><img class="img_sty" src="${goods.imgUrl}" /></a>
								<br>价格:<span>${goods.showPrice}</span><span>$</span>
								<br>名称:<span>${goods.enName}</span>
							</div>
							<br>
							<div style="margin-bottom: 2px;">
								<c:if test="${goods.valid == 1}">
									<span style="color:${goods.valid == 1 ? 'green':'red'};">数据清洗:有效数据</span>
								</c:if>
								<c:if test="${goods.valid == 2}">
									<span style="color:${goods.valid == 1 ? 'green':'red'};">数据清洗:图片处理异常</span>
								</c:if>
								<c:if test="${goods.valid == 0}">
									<span style="color:${goods.valid == 1 ? 'green':'red'};">数据清洗:无效数据</span>
								</c:if>

                                <c:if test="${goods.noSold > 0}">
                                    <em style="color: red;">(不过滤销量)</em>
                                </c:if>
								<br>
								<span style="color:${goods.syncFlag == 1 ? 'green':'red'};">商品状态:${goods.syncDescribe}</span>
								<br>
								<c:if test="${goods.onlineFlag > 0}">
									<b style="color:red;">已经在线上状态:[${goods.onlineValid > 0 ? '上架':'下架'}-${goods.onlineEdit > 0 ? '已编辑':'未编辑'}]</b>
								</c:if>
								
								<c:if test="${goods.syncFlag == 2}">
									<span style="color: red;">原因:${goods.syncRemark}</span>
									<br>
								</c:if>		
								<br>						
								<c:if test="${goods.enInfoNum == 0}">
									<b><a target="_blank" style="color:#f50ed8"
									href="/cbtconsole/ShopUrlC/editGoods.do?shopId=${goods.shopId}&pid=${goods.pid}">编辑详情(无详情图片)</a></b>
								</c:if>
								<c:if test="${goods.enInfoNum > 0}">
									<a target="_blank"
									href="/cbtconsole/ShopUrlC/editGoods.do?shopId=${goods.shopId}&pid=${goods.pid}">编辑详情</a>
								</c:if>								
								<c:if test="${goods.syncFlag == 1}">
									<!-- https://www.import-express.com http://192.168.1.29:8081 -->
									&nbsp;&nbsp;&nbsp;&nbsp;<a target="_blank"
										href="https://www.import-express.com/spider/detail?${goods.goodsUrl}">线上预览</a>
								</c:if>
							</div>
						</div></td>
					<c:if test="${count3 % 5 == 0}">
						</tr><tr>
					</c:if>
				</c:forEach>
				<c:if test="${total3 % 4 > 0}">
					</tr>
				</c:if>
			</table>
			
			</c:if>
			
		</div>

	</c:if>


</body>
</html>