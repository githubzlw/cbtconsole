<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.2.min.js"></script>
<title>评论管理</title>
<style>

/* 隐藏框样式 */
.box{
    width:400px; margin-top:10%; margin:auto; padding:28px;
    height:350px; border:1px #111 solid;
    display:none;            /* 默认对话框隐藏 */
}
.box.show{display:block;} 
.box .x{ font-size:18px; text-align:right; display:block;}
.box input{width:80%; font-size:18px; margin-top:18px;}

</style>


</head>
<script type="text/javascript">
//查看所有评论入口,使用动态菜单完成 yyl
function seeAllComments(goodsPid){
	var goods_img = $("#goods_img"+goodsPid).val();
	var oldUrl = $("#goods_url"+goodsPid).val();
	var goodsname = $("#goodsname"+goodsPid).val();
	var goodsprice = $("#goodsprice"+goodsPid).val();
	form = $("<form></form>")
     form.attr('action','/cbtconsole/goodsComment/selectcomments.do')
     form.attr('method','post')
     input1 = $("<input type='hidden' name='goods_img' />")
     input1.attr('value',goods_img)
     input2 = $("<input type='hidden' name='goods_url' />")
     input2.attr('value',oldUrl)
     input3 = $("<input type='hidden' name='goodsname' />")
     input3.attr('value',goodsname)
     input4 = $("<input type='hidden' name='goodsprice' />")
     input4.attr('value',goodsprice)
     input5 = $("<input type='hidden' name='goods_pid' />")
     input5.attr('value',goodsPid)
     
     form.append(input1)
     form.append(input2)
     form.append(input3)
     form.append(input4)
     form.append(input5)
     form.appendTo("body")
     form.css('display','none')
     
     form.submit()	 
	
}


</script>
<body>
<div>
			<form action="${pageContext.request.contextPath }/goodsComment/queryNewSaleAndCustomerComment.do" method="post">
			<table style="border-collapse:collapse;">
					<tr>
					<td>
					订单号：<input id="orderid" name="orderid" type="text" />
					产品id：<input id="goodsPid" name="goodsPid" type="text" />
					评论账号/ID：<input id="commentid" name="commentid" type="text" />
					时间：<input id="commenttime" name="commenttime" type="text" />
					销售：<select name="commentsale">
							<option value="">--请选择--</option>
							<option value="ling">ling
							<option value="ELISA">ELISA
						</select>
					评论者身份：<select id="commentIdentity" name="commentIdentity">
						<option value="">--请选择--</option>
						<option value="0">用户
						<option value="1">销售
					</select>
					</td>
						<td>
							<input id="search" type="submit"  value="查询" />
						</td>
					</tr>
				</table>
				</form>
	</div>

 <table border="1px  #0094ff ; border-collapse: collapse"  style="border-collapse:collapse;" >
	 		
	 			<tr>
					<td>id</td> 			
					<td>产品</td> 
					<td>订单详情编号</td>
					<td>规格</td>			
					<td>评论账号</td> 			
					<td>评论内容</td>
					<td>评论图片</td>
					<td>评论时间</td> 			
					<td>状态</td> 			
					<td>回复</td>
				</tr>
	 		
	 		<c:forEach items="${page.list }" var="customerGoodsComment">
	 			<tr>
					<td>${customerGoodsComment.id }</td> 	
					<td><a href="${customerGoodsComment.goods_url }"><img width="100px" height="150px" src="${customerGoodsComment.goods_img }"></a> <br/>
											<font size="2px">${customerGoodsComment.goodsname }</font><br/>
											<a href="javascript:void(0)" onclick="seeAllComments('${customerGoodsComment.goodsPid }')" >所有评论</a>
											<input type="hidden" id="goods_img${customerGoodsComment.goodsPid }" value="${customerGoodsComment.goods_img}"/>
											<input type="hidden" id="goods_url${customerGoodsComment.goodsPid }" value="${customerGoodsComment.goods_url }"/>
											<input type="hidden" id="goodsname${customerGoodsComment.goodsPid }" value="${customerGoodsComment.goodsname }"/>
											<input type="hidden" id="goodsprice${customerGoodsComment.goodsPid }" value="${customerGoodsComment.goodsprice }"/>
											 </td>		
					<td>${customerGoodsComment.orderDetailId }</td>
					<td>${customerGoodsComment.carType }</td>
				
					<td>用户评论:<br/>
						用户ID:${customerGoodsComment.userId }<br/>
						用户账号:${customerGoodsComment.userName }</td> 			
					<td>${customerGoodsComment.commentsContent != null || customerGoodsComment.commentsContent ==""?"":customerGoodsComment.commentsContent}</td>
					<td><img style="width:220px;height:220px" src="${customerGoodsComment.picPath}"></img></td>
					<td>${customerGoodsComment.commentsTime }</td> 			
					<td><div id="show_${customerGoodsComment.id }" ><c:if test="${customerGoodsComment.showFlag==1 }">前端显示<button onclick="setDisplay('${customerGoodsComment.id}','0','sale')">设置为不显示</button></c:if> <c:if test="${customerGoodsComment.showFlag==0 }">前端不显示<button onclick="setDisplay('${customerGoodsComment.id}','1','sale')">设置为显示</button></c:if></div></td>
					<td><button onclick="msgbox(1,'${customerGoodsComment.userId}')">邮件回复</button></td> 			
	 			</tr>
	 			<!-- 销售 -->
	 			 <c:set var="mygoodsid1" value="${customerGoodsComment.goodsPid }${'ID'}"></c:set>
	 			<tr>
					<td></td> 	
					<td> </td>		
					<td> </td>		
					<td> </td>		
					<td>销售:用户账号:${maps[mygoodsid1].userName }<br/>
					<td>${maps[mygoodsid1].commentsContent }</td> 			
					<td>${maps[mygoodsid1].commentsTime }</td> 			
					<td><c:if test="${maps[mygoodsid1].showFlag==1 }">前端显示</c:if> <c:if test="${maps[mygoodsid1].showFlag==0 }">前端不显示</c:if></td> 			
					<td></td> 			
	 			</tr>
	 		</c:forEach>
	 		
	 </table>	
	 <div class="pages" id="pages">
				<span>&nbsp;&nbsp;总条数：<font>${page.countRecord }</font>&nbsp;&nbsp;总页数：<font>${page.countPage }</font></span>&nbsp;&nbsp;当前页:<font >${page.currentPage }</font>&nbsp;&nbsp;
				<button onclick="toPage('${page.currentPage-1}','${page.countPage }')">上一页</button>
				&nbsp;
				<button onclick="toPage('${page.currentPage+1}','${page.countPage }')">下一页</button>
				&nbsp;<input id="jump1" type="text" onkeyup="checkNum(this)">
				<button onclick="toPage('-1','${page.countPage }')">转至</button>
			</div>
			<br> <br>
		</div>
		<!-- 隐藏的窗口 -->
		<div id='inputbox' class="box">
        <a class='x' href=''; onclick="msgbox(0); return false;">关闭</a>
        <textarea id="mailContent" rows="20" cols="40"></textarea>
        <input type="hidden" id="userMailId" value="">
        <input type="button" onclick="sendMail()" value="发送">
     </div>
     
     
  <script type="text/javascript">
function toPage(page,totalpage){
	if(page==0){
		return;
	}
	if(page>totalpage){
		return;
	}
	if(page==-1){
		page = document.getElementById("jump1").value
		if(page>totalpage){
			return ;
		}
	}
	window.location.href="/cbtconsole/goodsComment/queryNewSaleAndCustomerComment.do?currpage="+page
}
/* 校验输入框 */
function checkNum(obj){
	  var re = /^[0-9]*[1-9][0-9]*$/ ; 
	  var page = obj.value;
	  if(!re.test(page)){
		  obj.value="";
	  }
}
/* 参数1:评论的id 参数2:是否显示 0/不显示  1/显示  参数3:标识操作的是销售还是客户评论*/
function setDisplay(id,showFlag,flag) {
    $.ajax({
        url:'/cbtconsole/goodsComment/updateComments.do',
        type:'POST',
        async:true,    //或false,是否异步
        data:{
            id:id,
            showFlag:showFlag
        },
        timeout:5000,    //超时时间
        success:function(data){
            if(data == 'success'){
                if(showFlag == 0){//设置为不显示
					$("#show_"+id).html('前端不显示 <button onclick=setDisplay("'+id+'","1","cust")>设置为显示</button>')
                }
                if(showFlag == 1){//设置为显示
					$("#show_"+id).html('前端显示 <button onclick=setDisplay("'+id+'","0","cust")>设置为不显示</button>')
                }

            }
        }
    })
}
/* 隐藏的输入框 */
function msgbox(n,id){
	if(n==1){
		document.getElementById("userMailId").value=id
	}
    document.getElementById('inputbox').style.display=n?'block':'none';     /* 点击按钮打开/关闭 对话框 */
}

function sendMail(){
	var content = document.getElementById("mailContent").value;
	var custId = document.getElementById("userMailId").value;
	if(content == ""){
		return;
	}
	 $.ajax({
		    url:'/cbtconsole/goodsComment/sendMailToCustomer.do',
		    type:'POST', 
		    async:true,    //或false,是否异步
		    data:{
		    	custId:custId,
		        content:content
		    },
		    success:function(data){
		    	if(data == 'success'){
		    		msgbox(0,null);
		    	}
		    }
	 });
}

</script>   
</body>
</html>