<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>推荐目录管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
/* 主页 */
.report .form-control {
	display: inline-block;
	width: 70%;
}

.report select.form-control {
	width: 48%;
}

.report {
	font-size: 16px;
	color: #333;
	background: #dff0d8;
	width: 80%;
}

.report label {
	margin-right: 10px;
}

.report .mt20 {
	margin-top: 20px;
	margin-left: 5px;
}

.report .row2 button {
	margin-right: 10px;
}

.report b {
	font-size: 18px;
}
.table-catalog{    margin-left: 1%;width: 98%;border: 4px solid #ccc;}
.table-catalog td{border: 4px solid #ccc;}
.div-catalog{min-height: 980px;}
.report .w200 {
	width: 200px;
}
.row-100{margin-left: 5px;}
.datagrid-cell-c2-goodsName{width:300px;}
.datagrid-cell-c2-goodsSku{width: 350px;}
.mt5 {
	margin-top: 5px;
}

.tc, .trnasparent {
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, .5);
	position: fixed;
	z-index: 1;
	display: none;
	text-align: center;
}

.tc {
	background-color: rgba(0, 0, 0, 0);
	top: 0;
	left: 0;
}
/* 产品弹窗 tc1 */
.tc,.tc *{font-size:16px;}
.tc1 .form-horizontal .control-label{text-align: left;}
.tc1 th{font-weight: normal;}
.tc1 th,.tc1 td{text-align: center;}
.tc1 label{font-weight: normal;}
.tc1 .remark label{margin-right:32px;}
.tc1 button{width: 300px;}
.tc1 .container{margin-top:50px;}
.tc1 table td:last-child{text-align: center;}
.tc1 table input[type="radio"]{width:20px;height:20px;}
.tc1{position:absolute;z-index:10;display:none;top:100px;left:50%;margin-left:-600px;background-color:#fff;
padding: 20px;max-height: 800px;}
.tc1_table{max-height:500px;height:auto;overflow-y:auto;}
.transparent,.transparent-bg{width:100%;height:100%;background-color:rgba(0,0,0,0);position: fixed;z-index:1;display: none;text-align: center;}
em,i{font-style: normal;}
*{margin:0;padding:0;box-sizing: border-box;}
.clearfix:before,.clearfix:after{content:"";display:table;}
.clearfix:after{clear:both;}
.clearfix{zoom:1;} 
.datagrid-cell-c2-remarkContext{width:400px;}
.li_more_s{display: none;}
.report .btn_page_in{width:100px;}
.btn-check-list{display: none;}
.query-control{    margin-bottom: 9px;}
.form-control-q{
    height: 34px;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
}
</style>
</head>
<body>
<div class="container-fluid report">
<div class=""></div>
		<h1 class="text-center">推荐目录列表</h1>
		<div class="row mt20 row2">
			<div class="col-xs-2">
				<button class="btn btn-success catalog-new" >新增推荐</button>
			</div>
		<div class="query-control col-xs-10">
		<label >模板: <select class="form-control-q" id="catalog-template" >
						<option value="0" ${template == 0? 'selected=selected':''}>全部</option>
						<option value="1" ${template == 1? 'selected=selected':''}>IMPORT</option>
						<option value="2" ${template == 2?'selected=selected':''}>KIDS</option>
						<option value="4" ${template == 4?'selected=selected':''}>PETS</option>
			</select>
			</label>
		<label>目录名称:<input class="form-control-q" name="" id="catalog-name" value="${catalogName}"></label>
		<label><button class="btn btn-success query_button">搜索</button></label>
		</div>
		</div>
		<div class="row mt20 div-catalog">
			<table class="table table-bordered table-catalog">
				<thead>
					<tr>
						<th>编号</th>
						<th>目录名称</th>
						<th>模板</th>
						<th>生成人</th>
						<th>生成时间</th>
						<th>包含产品数量</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${catalogList }" var="catalog" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}">
						<td class="catalog-id">${catalog.id}</td>
						<td class="catalog-name">${catalog.catalogName}</td>
						<td class="catalog-template">
						
						<c:if test="${catalog.template==1}">IMPORT</c:if>
						<c:if test="${catalog.template==2}">IMPORT</c:if>
						<c:if test="${catalog.template==4}">IMPORT</c:if>
						
						</td>
						<td align="left" class="catalog-create-admin">${catalog.createAdmin}</td>
						<td class="catalog-create-time">${catalog.createTime }</td>
						<td class="catalog-product-count">${catalog.productCount}</td>
						<td>
						<button class="btn btn-info catalog-preview" name="${catalog.id}">预览</button>
						<button class="btn btn-warning catalog-edit" name="${catalog.id}">编辑</button>
						<button class="btn btn-danger catalog-delete" name="${catalog.id}">删除</button>
						
						
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
				<div class="row page-ht row-100" style="${toltalPage > 0? '':'display:none;'}">
				<span>当前页 :${currentPage } / ${toltalPage},总共 ${catalogCount }条数据,跳转</span>
			<input type="text" class="form-control btn_page_in" id="current_page" value="${currentPage }">
				<button class="btn btn-success btn_page_qu" >查询</button>
				<button class="btn btn-success btn_page_up" >上一页</button>
				<button class="btn btn-success btn_page_down" >下一页</button>
				<input type="hidden" value="${toltalPage}" id="total_page">
				</div>
		</div>
		<br>
		<br>
		<br>
	</div>
	
	
	
	
	
<div class="tc">
	<div class="trnasparent"></div>
	<div class="container tc1">
		<div class="wrap row">
			<div class="col-xs-9">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-xs-2 control-label text-left">产品ID:</label>
						<div class="col-xs-10">
							<input type="text" class="form-control" id="lu_pid" onchange="getProduct()">
							<input type="hidden"  id="lu_catid" value="">
							<input type="hidden"  id="lu_price" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label text-left">产品名称:</label>
						<div class="col-xs-10">
							<span id="lu_name">产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称</span>
						</div>
					</div>
				</div>
				<div class="tc1_table">
				<table class="table table-bordered table-primary">
					<thead>
						<tr>
							<th>产品规格</th>
							<th>录入数量</th>
							<th>库位</th>
							<th>是否录入</th>
						</tr>
					</thead>
					<tbody id="lu_tr">
						<tr >
							<td><span class="lu_sku">xxx</span><br>
						<span class="lu_specid">ssss</span><br>
						<span class="lu_skuid">cccc</span></td>
							<td><input type="text" class="form-control lu_count" value="0"></td>
							<td class="lu_barcode">
							<input type='text' placeholder='请输入库位条形码' class='lu_barcode_a'>
							<!-- <a onclick="getbarcode()" class="lu_barcode_a">获取库位</a> --></td>
							<td><input type="checkbox" name="entry" class="lu_is"></td>
						</tr>
						
					</tbody>
				</table>
				</div>
			</div>
			<div class="col-xs-3">
				<img
					src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg"
					alt="" class="img-responsive" id="lu_img">
			</div>
		</div>
		
	</div>
	
	
</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/catalog_list.js"></script>
</html>














