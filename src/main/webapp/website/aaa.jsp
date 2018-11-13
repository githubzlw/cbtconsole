<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="Generator" content="EditPlus®">
  <meta name="Author" content="">
  <meta name="Keywords" content="">
  <meta name="Description" content="">
  <script type="text/javascript" src="/cbtconsole/js/JsBarcode.all.js"></script>
  <script type='text/javascript'
          src='http://cdn.staticfile.org/jquery/2.1.1/jquery.min.js'></script>
  <title>Document</title>
 </head>
 <body>
  <div>
<img id="barcode"/>
</div>
<script>
    var goodid=455478;
    var barcode = document.getElementById('barcode'),
        str = goodid,
        options = {
            format:"CODE128",
            displayValue:false,
            fontSize:18,
            height:20
        };
    JsBarcode(barcode, str, options);//原生
    $('#barcode').JsBarcode(str,options);//jQuery
        </script>
 </body>
</html>
