<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>上传产品</title>
<style type="text/css">
body{width: 1500px;margin: 0 auto;}
.body{width: 1500px;height:auto;}
a{
	cursor: pointer;
	text-decoration:underline;
}
.btn{    color: #fff;
    background-color: #5db5dc;
    border-color: #2e6da4;    
    padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;}

</style>
<script type="text/javascript">
$(function(){
	var pids = '${param.pids}';
	if(pids == ''){
		return ;
	}
	var admin = '${param.admName}';
	var result = '';
	var pidslist = pids.split(',');
	for(var i=0;i<pidslist.length;i++){
		var pid = pidslist[i];
		if(pid == ''){
			continue;
		}
		result = result + "\n产品"+pid+"正在发布，请稍后...";
		$("#result").html(result);
		$.ajax({
			type:'POST',
			dataType:'text',
			url:'/cbtconsole/cutom/publish',
			data:{pid:pid,admin:admin},
			success:function(res){
					$("#result").html(result +"\n"+ res);
			},
			error:function(XMLResponse){
				result = result + "\n服务器异常!!!!";
				$("#result").html(result);
			}
		});
	}
});



</script>

</head>
<body class="body">
<textarea rows="20" cols="100" style="display:none;" id="pids">${param.pids}</textarea>
<input type="hidden"  id="admName" value="${param.admName}">

<textarea rows="50" cols="200" id="result"></textarea>
</body>
</html>