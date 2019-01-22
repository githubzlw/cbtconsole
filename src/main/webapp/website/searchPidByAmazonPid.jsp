<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>

    <title>亚马逊产品开发</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">

    <style>
        .btn {
            display: inline-block;
            width: 120px;
            height: 34px;
            text-align: center;
            line-height: 34px;
            font-size: 14px;
            color: #fff;
            background: #2f2cf5;
            border-radius: 17px;
            cursor: pointer;
        }
    </style>

    <script>

        function addGoods(amazonPid) {
            //进行PID存在校检
            var url1688 = $("#url_1688").val();
            if (url1688 == null || url1688 == "") {
                alert("当前1688Url已经上线，禁止操作");
            } else {
                $.ajax({
                    type: 'POST',
                    url: '/cbtconsole/amazonProductCtr/checkIsExists',
                    data: {url1688: url1688, amazonPid: amazonPid},
                    success: function (data) {
                        var json = eval('(' + data + ')');
                        if (json.ok) {
                            if (json.data > 0) {
                                if (json.data == 1) {
                                    alert("当前amazonUrl已经存在对标，禁止操作");
                                } else {
                                    alert("当前1688Url已经上线，禁止操作");
                                }
                            } else {
                                $('#addForm').submit();
                            }
                        } else {
                            alert("error:" + json.message);
                        }
                    },
                    error: function (xmlContent) {
                        alert("连接失败，请重试");
                    }
                });
            }
        }

    </script>

</head>

<body>


<form id="addForm" action="/cbtconsole/amazonProductCtr/addToOnline" method="post">

    <div style="background-color: #0edc28;">
        <h3 style="text-align: center;">亚马逊对标</h3>
        <input name="amazonUrl" type="hidden" value="${param.amazonUrl}"/>
        <input name="amazonPid" type="hidden" value="${param.amazonPid}"/>
        <span>请输入1688URL:<input id="url_1688" name="url1688" type="text" style="width: 980px;height: 24px;"/></span>
    </div>
    <br>
    <button type="button" class="btn" onclick="addGoods('${param.amazonPid}')">添加对标商品</button>
</form>


<div>

    <iframe src="https://kj.1688.com/pdt_tongkuan.html?productUrl=${param.amazonUrl}"
            style="width: 100%;height: 900px;"></iframe>

</div>


<!-- 公共尾部 -->
<div id="div-foot">
    <script src="/js/common-foot.js"></script>
</div>
<script src='../js/jquery.min.js'></script>
<script src='../js/jquery.lazyload.min.js'></script>

<script src="../js/jquery.fly.min.js"></script>


</body>
</html>
