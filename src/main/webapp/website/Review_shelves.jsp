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
    <title>价格过低下架</title>
    <style>
        *{word-wrap:break-word}html,body,h1,h2,h3,h4,h5,h6,hr,p,iframe,dl,dt,dd,ul,ol,li,pre,form,button,input,textarea,th,td,fieldset{margin:0;padding:0}ul,ol,dl{list-style-type:none}html,body{*position:static}html{font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}address,caption,cite,code,dfn,em,th,var{font-style:normal;font-weight:400}input,button,textarea,select,optgroup,option{font-family:inherit;font-size:inherit;font-style:inherit;font-weight:inherit}input,button{overflow:visible;vertical-align:top;outline:none}body,th,td,button,input,select,textarea{font-family:Helvetica,Verdana,sans-serif,microsoft yahei,hiragino sans gb,helvetica neue,Helvetica,tahoma,arial,Verdana,sans-serif,wenquanyi micro hei,\5b8b\4f53;font-size:12px;color:#333;-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing:grayscale}body{line-height:1.6}h1,h2,h3,h4,h5,h6{font-size:100%;font-weight:400}a,area{outline:none;blr:expression(this.onFocus=this.blur())}a{text-decoration:none;cursor:pointer}a:hover{text-decoration:none;outline:none}a.ie6:hover{zoom:1}a:focus{outline:none}a:hover,a:active{outline:none}:focus{outline:none}sub,sup{vertical-align:baseline}button,input[type=button],input[type=submit]{line-height:normal!important}img{border:0;vertical-align:middle}a img,img{-ms-interpolation-mode:bicubic}.img-responsive{max-width:100%;height:auto}*html{overflow:-moz-scrollbars-vertical;zoom:expression(function(ele){ele.style.zoom="1";document.execCommand("BackgroundImageCache",false,true)}(this))}header,footer,section,aside,details,menu,article,section,nav,address,hgroup,figure,figcaption,legend{display:block;margin:0;padding:0}time{display:inline}audio,canvas,video{display:inline-block;*display:inline;*zoom:1}audio:not([controls]){display:none}legend{width:100%;margin-bottom:20px;font-size:21px;line-height:40px;border:0;border-bottom:1px solid #e5e5e5}legend small{font-size:15px;color:#999}svg:not(:root){overflow:hidden}fieldset{border-width:0;padding:.35em .625em .75em;margin:0 2px;border:1px solid silver}input[type=number]::-webkit-inner-spin-button,input[type=number]::-webkit-outer-spin-button{height:auto}input[type=search]{-webkit-appearance:textfield;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;box-sizing:content-box}input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration{-webkit-appearance:none}.cl:after,.clearfix:after{content:".";display:block;height:0;clear:both;visibility:hidden}.cl,.clearfix{zoom:1}
        .a1{ color:#00afff;}
        #table1 th,#table1 td{max-width:600px;}

    </style>
    <script type="text/javascript">
        function resetGoods(){
            $("#userId").val("");
            $("#timeFrom").val("");
            $("#timeTo").val("");
            $("#cuName").val("");
            $("#email").val("");
            $("#admName").val("-1");
        }
        function infoSearch(){

            var cupid = $("#cuName").val();
            window.location = "/cbtconsole/purchase/FindReviewShelves?pid=${pid}&price=${price}&cupid="+cupid+"&page=1";
        }
        function AddReviewGoods(pid,catid1,name,maxPrice){
            $.ajax({
                type:"post",
                url:"/cbtconsole/purchase/AddReviewGoods",
                data : {"catid1":catid1,"pid":pid,"name":name,"maxPrice":maxPrice},
                dataType:"json",
                success : function(data){
                    if (data.total==0){
                        alert("添加失败")
                    }else {
                        alert("添加下架列表成功")
                        location.reload()
                    }

                }

            })

        }
        function jrpurl(pid){
            window.open("https://www.importx.com/goodsinfo/winter-cashmere-woolen-overcoat-men-s-cap-in-the-long-10165-122194006-1"+pid+".html")
        }
    </script>
</head>
<body>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center" style="display: block;height: 30px;text-align: center;margin-bottom: 20px;">
    <span style="line-height:25px;">商品pid:</span><input type="text" id="cuName" value="${cupid==null?"":cupid}" class="rlcj" style="margin-right: 20px;"/>
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
            <th>下架原因</th>
            <th>操作</th>
        </Tr>

        <c:forEach items="${goodsBeans }" var="gbb" varStatus="i">
            <Tr class="a" id="tr${gbb.pid}">
                <td>
                    <a href="javascript:void(0);" onclick="jrpurl('${gbb.pid}')">${gbb.pid}</a>
                </td>
                <td>
                        ${gbb.catid1}
                </td>
                <td>
                        ${gbb.name}
                </td>
                <td>
                        ${gbb.maxPrice}
                </td>
                <td >价格过低下架</td>

                <td ><button onclick="AddReviewGoods('${gbb.pid}','${gbb.catid1}','${gbb.name}','${gbb.maxPrice}')">确认下架</button></td>

            </Tr>
        </c:forEach>
    </table>
    </br>
    <div align="center">${pager }</div>

</div>
</div>
</body>
</html>