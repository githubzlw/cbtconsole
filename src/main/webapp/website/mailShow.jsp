<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <title>邮件</title>
    <style type="text/css">
        .divcss{width:100px;height:50px;border:1px solid #F00}
        #body_path{border-top:1px solid #000;width:1200px;height: 1500px}
        #sub{border-top:1px solid #000;width:100px;}
    </style>
    <script type="text/javascript">
        function lookemail(body ) {
                $.post("/cbtconsole/NewCustomersFollow/lookEmail", {
                    body:body,
                    dataType:"json",
                }, function(res) {

                    document.getElementById("body_path").innerHTML = '';


                    $("#body_path").append(res)
                });

        }
        $(window).load(function(){
            lookemail('${email[0].body_path }')
        });


    </script>
</head>
<body>
<div style="float:left; margin-right:50px;">邮件标题</div>
<div style="text-align: center">邮件正文</div>
<div id="sub" style="float:left; margin-right:50px;width: 300px">
<c:forEach items="${email }" var="e" varStatus="i">
 <td ><a href="javascript:void(0);" onclick="lookemail('${e.body_path }')">${e.theme}</a></td><br/>
</c:forEach>
</div>
  <div style="float:left; margin-right:50px;">
  <div id="body_path" ></div>
      </div>
  </div>

</body>
</html>