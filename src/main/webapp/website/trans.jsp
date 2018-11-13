<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>地址转换</title>
</head>
<body>
<div>
<h2>请输入商品的链接（原链接）</h2>
<form id="form2" name="form2" method="post" action="/cbtconsole/Trans">
 <textarea  name="url" id="textfield" cols="100" rows="5"> 
  ${url}
 </textarea>
 <br>
  <input type="submit" name="button" id="button" value="search" />
</form>
</div>
 <br>
<div>
<h2>商品短连接转换结果</h2>
 <textarea  name="result" id="result" cols="100" rows="5"> 
 ${data}
 </textarea>
</div>

</body>
</html>