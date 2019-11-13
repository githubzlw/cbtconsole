<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>目录管理</title>
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
.table-catalog{margin-left: 1%;width: 98%;border: 3px solid #3c763d;}
.div-catalog table tbody td{border: 1px solid #3c763d;}
.div-catalog table thead th{border: 1px solid #3c763d;}
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
/* 产品弹窗 tc1 */
.tc1{
    position: absolute;
    z-index: 888;
    display: none;
    top: 100px;
    left: 50%;
    margin-left: -600px;
    background-color: #fff;
    padding: 20px;
    max-height: 800px;
    border-radius: 12px;
    overflow-y: auto;
}
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
.category-row-product {
    margin-left: 150px;
}
.h3-c{text-align: left;}
.close-x{position: absolute;top: 0;right: 3px;z-index: 999;}
.window-bd {width: 90%;}
.col-xs-3-product{height:200px;}
.h-tc{font-size: 23px;font-weight: bolder;}
.catalog_img{max-height: 200px;}
</style>
</head>
<body>
<div class="container-fluid report">
<div class=""></div>
		<h1 class="text-center">推荐目录列表</h1><span style="color: red;">注意:因线上与后台数据同步原因,新增或编辑目录后需等待3分钟刷新列表!!!</span>
		
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
		<label><button class="btn btn-success btn-query-list">搜索</button></label>
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
						<td class="catalog-name">
						<c:if test="${catalog.template==1}"><a href="https://www.import-express.com/apa/recatalog.html?id=${catalog.id }" target="_blank">${catalog.catalogName}</a></c:if>
						<c:if test="${catalog.template==2}"><a href="https://www.kidsproductwholesale.com/apa/recatalog.html?id=${catalog.id }" target="_blank">${catalog.catalogName}</a></c:if>
						<c:if test="${catalog.template==4}"><a href="https://www.petstoreinc.com/apa/recatalog.html?id=${catalog.id }" target="_blank">${catalog.catalogName}</a></c:if>
						
						</td>
						<td class="catalog-template">
						
						<c:if test="${catalog.template==1}">IMPORT</c:if>
						<c:if test="${catalog.template==2}">KIDS</c:if>
						<c:if test="${catalog.template==4}">PETS</c:if>
						
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
		<div class="row">
        <h3 class="text-center h-tc">推荐商品类目页预览</h3>
       <!--  <label>
        <input class="btn btn-info boutique_goods_create" type="button" value="生成目录页"  style="margin-right: 80px;">
        </label>
        <label>
        <input class="btn btn-info boutique_goods_clear" type="button" value="清空所有勾选的商品"  style="margin-right: 20px;">
        </label> -->
        </div>
        <br>
        
        <div class="row window-bd">
                <div class="row ain-list">
                    <div class="pre-row">
                        
                </div>
            </div>
    </div>
		
</div>
</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/catalog_list.js"></script>
</html>














