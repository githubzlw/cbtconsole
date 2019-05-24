<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Check Goods Car Data</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>

    <script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/warehousejs/thelibrary.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.messager.js"></script>


    <link rel="stylesheet" href="/cbtconsole/css/warehousejs/measure.css" type="text/css">
    <!-- CSS goes in the document HEAD or added to your external stylesheet -->
    <!--  -->
    <script type="text/javascript"
            src="/cbtconsole/js/lhgdialog/lhgcore.lhgdialog.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
</head>
<body style="background-color : white;" onclick="">
<div align="center">
    <div><h1>Check Goods Car Data</h1></div>
    <div id="msginfo"></div>
    <br/><br/>
    <!-- 表格 -->
    <div>
        <table id="tabId" class="altrowstable" style="width: 800px">
            <tr>
                <td  colspan="3" align="center" valign="middle" > ${data}</td>
            </tr>
        </table>
        <div id="message">

        </div>
    </div>
</div>

</body>
<script type="text/javascript">
    function reorder() {
        var orderNo = $('#orderNo').val();
        var userId = $('#userId').val();
        $.ajax(
            {
                url:"../customerRelationshipManagement/reorderTrue",
                type:'POST',
                data:{
                    orderNo:orderNo,
                    userId:userId
                },
                success:function(result){
                    if(result != null){
                        var content = '<div></div><span style="color: orange">'+result+'</span><br>';
                        $.dialog({
                            title : '添加订单商品到指定用户结果!',
                            content : content,
                            max : false,
                            min : false,
                            lock : true,
                            drag : false,
                            fixed : true,
                            ok : function() {

                            },
                            cancel : function() {
                            }
                        });
                    }
                }
            }
        );
    }
</script>
<%--<script type="text/javascript" src="https://cdn.bootcss.com/canvas-nest.js/1.0.1/canvas-nest.min.js"></script>--%>
<script src="https://files.cnblogs.com/files/cl-blogs/backgroundSwitch.js" type="text/javascript"></script>
</html>