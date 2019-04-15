<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <title>差货反馈</title>
    <style>
        *{word-wrap:break-word}html,body,h1,h2,h3,h4,h5,h6,hr,p,iframe,dl,dt,dd,ul,ol,li,pre,form,button,input,textarea,th,td,fieldset{margin:0;padding:0}ul,ol,dl{list-style-type:none}html,body{*position:static}html{font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}address,caption,cite,code,dfn,em,th,var{font-style:normal;font-weight:400}input,button,textarea,select,optgroup,option{font-family:inherit;font-size:inherit;font-style:inherit;font-weight:inherit}input,button{overflow:visible;vertical-align:top;outline:none}body,th,td,button,input,select,textarea{font-family:Helvetica,Verdana,sans-serif,microsoft yahei,hiragino sans gb,helvetica neue,Helvetica,tahoma,arial,Verdana,sans-serif,wenquanyi micro hei,\5b8b\4f53;font-size:12px;color:#333;-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing:grayscale}body{line-height:1.6}h1,h2,h3,h4,h5,h6{font-size:100%;font-weight:400}a,area{outline:none;blr:expression(this.onFocus=this.blur())}a{text-decoration:none;cursor:pointer}a:hover{text-decoration:none;outline:none}a.ie6:hover{zoom:1}a:focus{outline:none}a:hover,a:active{outline:none}:focus{outline:none}sub,sup{vertical-align:baseline}button,input[type=button],input[type=submit]{line-height:normal!important}img{border:0;vertical-align:middle}a img,img{-ms-interpolation-mode:bicubic}.img-responsive{max-width:100%;height:auto}*html{overflow:-moz-scrollbars-vertical;zoom:expression(function(ele){ele.style.zoom="1";document.execCommand("BackgroundImageCache",false,true)}(this))}header,footer,section,aside,details,menu,article,section,nav,address,hgroup,figure,figcaption,legend{display:block;margin:0;padding:0}time{display:inline}audio,canvas,video{display:inline-block;*display:inline;*zoom:1}audio:not([controls]){display:none}legend{width:100%;margin-bottom:20px;font-size:21px;line-height:40px;border:0;border-bottom:1px solid #e5e5e5}legend small{font-size:15px;color:#999}svg:not(:root){overflow:hidden}fieldset{border-width:0;padding:.35em .625em .75em;margin:0 2px;border:1px solid silver}input[type=number]::-webkit-inner-spin-button,input[type=number]::-webkit-outer-spin-button{height:auto}input[type=search]{-webkit-appearance:textfield;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;box-sizing:content-box}input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration{-webkit-appearance:none}.cl:after,.clearfix:after{content:".";display:block;height:0;clear:both;visibility:hidden}.cl,.clearfix{zoom:1}
        .a1{ color:#00afff;}
        #table1 th,#table1 td{max-width:600px;}

    </style>
    <script type="text/javascript">
        var check = function(){
            var uid = $("#selled").val();
            if(isNaN(uid)){
                $("#ts").html("请输入数字");
                return false;
            }else{return true;}
        };

        function getCodeId1(value){
            $("#categoryId1").val(value);
            document.getElementById("categoryId1").value=value;
        }

        function selectSimilarity(value){
            $("#similarityId").val(value);
        }

        function getCodeId(value){
            window.location = "/cbtconsole/customerServlet?action=findAllPicture&className=PictureComparisonServlet&cid="+value;
        }

        function infoSearch(){
            var userId = $("#userId").val();
            var timeFrom = $("#timeFrom").val();
            var timeTo = $("#timeTo").val();
            var cuName = $("#cuName").val();
            var email = $("#email").val();
            var admName = $("#admName option:selected").val();
            //state = $("#orderEnd").val();
            window.location = "getOldCustomShow?admName="+admName+"&email="+email+"&staTime="+timeFrom+"&enTime="+timeTo+"&id="+userId+"&cuName="+cuName+"&page=1";
            //window.location = "${ctx}/cbtconsole/customerServlet?action=getErrorInfo&className=PictureComparisonServlet&userId="+userId+"&timeFrom="+timeFrom+"&timeTo="+timeTo+"&valid="+valid;
        }
        function updatBateprice(pid,pid1){
            var Bad = $("#"+pid).val();
            var price = $("#"+pid1).val();
            $.ajax({
                type:"post",
                url:"/cbtconsole/purchase/AddBadOrder",
                data : {"pid":pid,"pid1":price},
                dataType:"json",
                success : function(data){
                   alert(data)
                }

            })

        }
        $(function(){
            $.ajax({
                type:"post",
                url:"/cbtconsole/OldCustomShow/getAlladm",
                dataType:"json",
                success : function(data){
                    $("#admName").append("<option value='-1'>销售选择</option>");
                    $(data).each(function (index, item) {
                        $("#admName").append("<option value="+item.admName+">"+item.admName+"</option>");
                    });
                }

            })

        });
        function FindAllOrder(value){
            window.open ("/cbtconsole/OldCustomShow/getCusOrder?usid="+value);
        }

    </script>
</head>
<body>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center" style="display: block;height: 30px;text-align: center;margin-bottom: 20px;">
    <select id="admName">
        <option value="${admName}">${admName=="-1"?"销售选择":admName}</option>
    </select>
    <span style="line-height:25px;">用户名:</span><input type="text" id="cuName" value="${cuName==null?"":cuName}" class="rlcj" style="margin-right: 20px;"/>
    <span style="line-height:25px;">用户邮箱:</span><input type="text" id="email" value="${email==null?"":email}" class="rlcj" style="margin-right: 20px;"/>
    <span style="line-height:25px;">用户id:</span><input type="text" id="userId" value="${id==null?"":id}" class="rlcj" style="margin-right: 20px;"/>
    <span style="line-height:25px;">时间:</span> <input id="timeFrom" class="Wdate rlcj" style="width:120px;" type="text" value="${timeFrom }" onclick="WdatePicker({skin:'whyGreen',lang:'en'})" /><span> ~</span>
    <input id="timeTo" class="Wdate rlcj" type="text" style="width:120px;" value="${timeTo }" onfocus="WdatePicker({skin:'whyGreen',lang:'en'})" />
    <input type="button" value="查找" class="rlcj" style="background: #7AB63F;padding: 0px 5px;color: #fff;margin-left: 10px;" onclick="infoSearch();" />
    <input type="button" value="重置" class="rlcj" style="background: #7AB63F;padding: 0px 5px;color: #fff;margin-left: 10px;" onclick="resetGoods()" />

</div>

<div style="width: 60%;min-width: 900px;max-width: 1400px;margin: 0 auto;">
    <table id="table1" align="center" border="1px" style="font-size: 13px;width:100%;" cellpadding="0" cellspacing="0" >
        <Tr>
            <th >商品pid</th>
            <th>商品类别</th>
            <th>商品名称</th>
            <th>商品价格</th>
            <th>质量差原因分析</th>
            <th>采购建议价格</th>
            <th>操作</th>
        </Tr>

        <c:forEach items="${goodsBeans }" var="gbb" varStatus="i">
            <Tr class="a" id="tr${gbb.pid}">
                <td>
                        ${gbb.pid}
                </td>
                <td>
                        ${gbb.keyword}
                </td>
                <td>
                        ${gbb.name}
                </td>
                <td>
                        ${gbb.price}
                </td>

                <td ><input type="text" id="${gbb.pid}" value=""></td>
                <td ><input type="text" id="${gbb.pid}d" ></td>
                <td ><button onclick="updatBateprice('${gbb.pid}','${gbb.pid}d')">确认提交</button></td>

            </Tr>
        </c:forEach>
    </table>
    </br>
    <div align="center">${pager }</div>

</div>
</div>
</body>
</html>