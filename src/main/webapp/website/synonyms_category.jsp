<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<style type="text/css">
.bady-a{background-color: #f2dede;}
.report {font-size: 16px;color: #333;width: 1400px;}
.datagrid-cell-c2-synonyms{width: 750px;}	
.datagrid-cell-c2-category{width: 300px;}	
.datagrid-cell-c2-catid{width: 200px;}
.table-report{background-color: #dff0d8;}
.report .form-control {
	display: inline-block;
	width: 70%;
}
.report .btn_page_in{width:100px;}
.page-content{margin-bottom: 40px;}
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
.tc,.tc *{font-size:16px;}
.tc1 .form-horizontal .control-label{text-align: left;}
.tc1 .container{margin-top:50px;}
.tc1{position:absolute;z-index:10;display:none;top:100px;left:62%;margin-left:-600px;background-color:#fff;
padding: 20px;max-height: 800px;    width: 800px;}
.transparent,.transparent-bg{width:100%;height:100%;background-color:rgba(0,0,0,0);position: fixed;z-index:1;display: none;text-align: center;}
</style>
<title>类别同义词管理</title>
</head>

<body class="bady-a">
<div class="container-fluid report">
<h1 class="text-center">类别同义词管理</h1>

<div class="row param-content">
<div class="col-xs-1">
				<b>类别检索</b>
</div>
<div class="col-xs-11">
<label>Catid:<input type="text" id="catid-p" value="${catid }"></label>
<label><button class="btn btn-info btn-search">查询</button></label>
<label><button class="btn btn-info btn-add">新增</button></label>
</div>
</div>

<div class="row list-content">
<table class="table table-striped table-bordered table-responsive table-report">
				<thead>
					<tr>
						<th>类别ID</th>
						<th>类别名称</th>
						<th>同义词</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${categoryList }" var="tory" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}">
						<td class="datagrid-cell-c2-catid">${tory.catid}</td>
						<td class="datagrid-cell-c2-category" id="category-${tory.catid}">${tory.category}</td>
						<td class="datagrid-cell-c2-synonyms" id="synonyms-${tory.catid}">${tory.synonymsCategory}</td>
						<%-- <td class="datagrid-cell-c2-valid">
						<c:if test="${tory.valid==1}">启用</c:if>
						<c:if test="${tory.valid!=1}">禁用</c:if>
						</td> --%>
						<td>
						<button class="btn btn-info btn-delete"  onclick="deleteCatid('${tory.catid}')">删除</button>
						<button class="btn btn-info btn-update" onclick="updateCatid('${tory.catid}')">更改</button>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>


</div>



<div class="row page-content">
<div>
<span>当前页 :${currentPage} / ${totalPage},总共 ${totalCount }条数据,跳转</span>
<input type="text" class="form-control btn_page_in" id="current_page" value="${currentPage}">
<button class="btn btn-success btn_page_qu">查询</button>
<button class="btn btn-success btn_page_up">上一页</button>
<button class="btn btn-success btn_page_down">下一页</button>

<input type="hidden" value="${totalPage}" id="total_page">
</div>

</div>
</div>
<div class="tc">
	<div class="trnasparent"></div>
	<div class="container tc1">
		<div class="wrap row">
			<label>Catid:<input value="" id="update-catid" type="text" ></label>
			<label>Category:<input value="" id="update-category" type="text" ></label>
		</div>
		<div class="wrap row">
		<textarea rows="20" cols="70" id="update-sy"></textarea>
		</div>
		<div class="wrap row">
		<button class="btn btn-info btn-updeta-s">更新</button>
		<button class="btn btn-info btn-updeta-add">新增</button>
		</div>
	</div>
	
</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/synonyms_category.js"></script>
</html>