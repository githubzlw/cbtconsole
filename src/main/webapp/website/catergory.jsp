<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>类别页面商品数据管理</title>
<style type="text/css">
* {
	margin: 0;
	padding: 0
}

html, body {
	height: 100%;
	width: 100%;
	font-size: 12px
}

.white_content {
	display: none;
	position: absolute;
	top: 25%;
	left: 25%;
	width: 50%;
	padding: 6px 16px;
	border: 12px solid #D6E9F1;
	background-color: white;
	z-index: 1002;
	overflow: auto;
}

.black_overlay {
	display: none;
	position: absolute;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: #f5f5f5;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity = 80);
}

.close {
	float: right;
	clear: both;
	width: 100%;
	text-align: right;
	margin: 0 0 6px 0
}

.close a {
	color: #333;
	text-decoration: none;
	font-size: 14px;
	font-weight: 700
}

.con {
	text-indent: 1.5pc;
	line-height: 21px
}

#remaining_price em {
	color: red;
	font-style: normal;
}

.newsourceurl a {
	cursor: pointer;
	text-decoration: underline;
}

em {
	font-style: normal;
}

.con input {
	width: 360px;
	height: 28px;
}
</style>
</head>
<script type="text/javascript">
	var url = "";
	function fnValid(type) {
		var id = $.trim($("#productId").val());
		var cate = $("#catergory").val();
		$("#catergory_type").html("正在查询，请稍等...");
		$
				.post(
						"/cbtconsole/WebsiteServlet",
						{
							cate : cate,
							type : type,
							id : id,
							action : 'getCatergory',
							className : 'CatergorywServlet'
						},
						function(res) {
							$("#catergory_info tr:gt(0)").remove();
							var tab = $("#catergory_info");
							var json = eval(res);
							var len = json.length;
							$("#catergory_type").html("查询完毕：" + len + " 条。");
							for (var i = 0; i < len; i++) {
								var catergory = json[i];
								url = catergory.url.replace("&FreeShipping=1",
										"");
								tab
										.append("<tr><td style='display: none;'>"
												+ catergory.row
												+ "</td><td>"
												+ catergory.id
												+ "</td><td><a target='_Blank' href='"+url+"'><img width='80px' src='"+catergory.imgurl+"'></a></td><td>"
												+ catergory.productname
												+ "</td><td>"
												+ catergory.catergory
												+ "</td><td>"
												+ catergory.price
												+ "</td><td><em>"
												+ catergory.minorder
												+ "</em> <em>"
												+ catergory.unit
												+ "</em></td><td>"
												+ (catergory.valid == 1 ? "是"
														: "否")
												+ "</td><td><input type='button' value='修改商品信息' onclick='show(0,"
												+ catergory.id
												+ ",this)'>&nbsp;<input type='button' value='删除商品' onclick='deleteCatergory("+catergory.row+")'></td></tr>");
							}

						});
	}
	function show(type, id, val) {
		
		if (type == 0) {
			var tr = $(val).parent().parent();

			$("#row").val(tr.find("td:eq(0)").html());
			$("#price").val(tr.find("td:eq(5)").html());
			$("#name").val(tr.find("td:eq(3)").html());
			$("#unit").val(tr.find("td:eq(6) em:eq(1)").html());
			$("#mind").val(tr.find("td:eq(6) em:eq(0)").html());
			$("#url").val(tr.find("td:eq(2) a").attr("href"));
			$("#imgurl").val(tr.find("td:eq(2) img").attr("src"));
			$("#AddOrUpdateCate").val(tr.find("td:eq(4)").html());
			$("#productid").val(id);
			$("#comfirm").attr("onclick", "fnUpProduct('upCatergory')");
		} else {
			$("#productid").val("");
			$("#price").val("");
			$("#name").val("");
			$("#unit").val("");
			$("#mind").val("");
			$("#url").val("");
			$("#imgurl").val("");
			$("#AddOrUpdateCate").val($("#catergory").val());
			$("#comfirm").attr("onclick", "fnUpProduct('addCatergory')");
		}
		light.style.display = 'block';
		fade.style.display = 'block';
		$('#fade').css ('height','document.body.scrollHeight'); 
		
	}
	function hide(tag) {
		var light = document.getElementById(tag);
		var fade = document.getElementById('fade');
		light.style.display = 'none';
		fade.style.display = 'none';
	}

	function fnUpProduct(dz) {
		var row = $("#row").val();
		var id = $("#productid").val();
		var price = $("#price").val();
		var name = $("#name").val();
		var unit = $("#unit").val();
		var mind = $("#mind").val();
		var url = $("#url").val();
		var imgurl = $("#imgurl").val();
		var catergory = $("#AddOrUpdateCate").val();
		$.post("/cbtconsole/WebsiteServlet", {
			action : dz,
			className : 'CatergorywServlet',
			id : id,
			row : row,
			price : price,
			name : name,
			unit : unit,
			mind : mind,
			url : url,
			imgurl : imgurl,
			catergory : catergory
		}, function(res) {
			if (res > 0) {
				if (dz == 'upCatergory') {
					alert('修改成功');
					fnValid(1);
				}else{					
					alert("添加成功");
					fnValid(1);
				}
				hide('light');
			} else {
				if (dz == 'upCatergory') {
					alert('修改失败');
				}else{					
					alert("添加失败");
				}
			}
		});
	}
	
	
	function deleteCatergory(row) {
		$.post("/cbtconsole/WebsiteServlet", {
			action :"deleteCatergory",
			className : 'CatergorywServlet',
			row : row
		}, function(res) {
			if (res > 0) {
				alert("删除成功");
				hide('light');
				
			} else {
				alert("删除失败");
			}
		});
		fnValid(1);
	}
	
	function resetIn() {
		$("#productId").val("");
	}
	
	$(document).ready(function(){
		$.post("/cbtconsole/WebsiteServlet", {
			action : 'getCatergoryList',
			className : 'CatergorywServlet'
		},function(res){
			var json = eval(res);
			var str;
			for (var i=0;i<json.length;i++){
				str+='<option value="'+json[i]+'"">'+json[i]+'</option>';
			}
			$('#catergory').html(str);
			$('#AddOrUpdateCate').html(str);
		});
	});
	
</script>
<body>
	<br>
	<div align="center">
		<div>
			商品类别:<select id="catergory" name="catergory" style="width: 150px;">
				</select>&nbsp;&nbsp;商品ID:<input type="text" id="productId" onkeyup="this.value=this.value.replace(/\D/g,'')">&nbsp;&nbsp;<input
				onclick="fnValid(1)" type="button" value="查询商品">&nbsp;<input
				type="button" value="查询失效商品" onclick="fnValid(0)" style="display: none;">&nbsp;<input
				type="button" value="添加商品" onclick="show(1,1,1)">&nbsp;&nbsp;<input type="reset" value="重置" onclick="resetIn()"/>
		</div>
		<br> <em id="catergory_type"></em>
		<div>
			<table width="80%" id="catergory_info" border="1"
				bordercolor="#8064A2" cellpadding="0" cellspacing="0" style="text-align: center">
				<tr>
					<td style="display: none;">row</td>
					<td>id</td>
					<td>商品图片</td>
					<td>名称</td>
					<td>类别</td>
					<td>价格</td>
					<td>最小订量</td>
					<td>是否失效</td>
					<td>操作</td>
				</tr>
				<tbody id="catergory">
				</tbody>
			</table>
		</div>
		<div id="light" class="white_content">
			<div class="close">
				<a href="javascript:void(0)" onclick="hide('light')"> 关闭</a>
			</div>
			<div class="con">
				<table>
					<tr style="display: none;">
						<td>row：</td>
						<td><input type="text" disabled="disabled" name="row"
							id="row" /></td>
					</tr>
					<tr>
						<td>商品ID：</td>
						<td><input type="text" name="productid"
							id="productid" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
					</tr>
					<tr>
						<td>商品名称：</td>
						<td><input id="name" type="text"><em
							style="color: red">名称请勿输入"和\等特殊字符</em></td>
					</tr>
					<tr>
						<td>价格：</td>
						<td><input style="width: 50px;" id="price" type="text">$</td>
					</tr>
					<tr>
						<td>url：</td>
						<td><input id="url" type="text"></td>
					</tr>
					<tr>
						<td>图片路径：</td>
						<td><input id="imgurl" type="text"></td>
					</tr>
					
					<tr>
						<td>商品单位：</td>
						<td><input id="unit" type="text"></td>
					</tr>
					<tr>
						<td>商品类别：</td>
						<td><select id="AddOrUpdateCate" name="AddOrUpdateCate"></select></td>
					</tr>
					<tr id="fileupload">
						<td>最小订量：</td>
						<td><input style="width: 50px;" id="mind" type="text" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"></td>
					</tr>
					<tr align="center">
						<td colspan="2"><input style="width: 40px; height: 20px;"
							type="button" id="comfirm" onclick="fnUpProduct();" value="确认">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input style="width: 40px; height: 20px;" onclick="hide('light')"
							type="button" value="取消"></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>