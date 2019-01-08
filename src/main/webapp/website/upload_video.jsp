<%--
  Created by IntelliJ IDEA.
  User: 王宏杰
  Date: 2018/6/7
  Time: 19:40
  To change this template use File | Settings | File Templates.
--%>
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>产品单页橱窗视频编辑</title>
    <%
      String pid=request.getParameter("goods_pid");
    %>
    <script type="text/javascript">
        <%--var pid='<%=pid%>';--%>
        <%--if(pid !=null && pid != ""){--%>
            <%--$("#goods_pid").val('1264073673');--%>
            queryVideo();
        <%--}--%>
        video_id="";
        //查询产品视频信息
        function queryVideo(){
            var goods_pid=$("#goods_pid").val();
            var pid='${param.goods_pid}';
            if((goods_pid == null || goods_pid == "") && (pid == null || pid == "")){
                // alert("请输入产品pid");
                return;
            }
            if(pid != null && pid != ""){
                goods_pid=pid;
            }
            $.ajax({
                type: "GET",
                url: "/cbtconsole/warehouse/queryVideo?goods_pid="+goods_pid,
                dataType:"json",
                success:function(data){
                    var dzOrderno=data.dzOrderno;
                    if(dzOrderno == null || dzOrderno == ""){
                        return;
                    }
                    video_id=data.goods_url;
                    var html="<td>"+data.goods_pid+"</td>";
                    html+="<td><img src='"+data.car_url+"'></img></td>";
                    html+="<td><div id='embed'></div><input type='button' onclick='uploadInPic(\""+dzOrderno+"\")' value='视频上传'/>|<input type='button' onclick='deleteVideoPath(\""+dzOrderno+"\");' value='删除视频'/></td>";
                    //视频展示
                    if(video_id != null && video_id !=""){
                        videoUrl = 'http://www.vimeo.com/'+video_id;
                        endpoint = 'http://www.vimeo.com/api/oembed.json';
                        callback = 'embedVideo';
                        url = endpoint + '?url=' + encodeURIComponent(videoUrl) + '&callback=' + callback + '&width=400';
                        init();
                    }else{
                        $("#embed").html("无视频");
                    }
                    $("#show_info").html(html);
                },
                error:function(e){
                    alert("获取产品橱窗视频失败");
                }
            });
        }
        function embedVideo(video) {
            document.getElementById('embed').innerHTML = unescape(video.html);
        }
        function init() {
            var js = document.createElement('script');
            js.setAttribute('type', 'text/javascript');
            js.setAttribute('src', url);
            document.getElementsByTagName('head').item(0).appendChild(js);
        }

        function deleteVideoPath(goods_pid){
            $.messager.progress({
                title : '删除视频中',
                msg : '请等待...'
            });
            $.ajax({
                type: "GET",
                url: "/cbtconsole/warehouse/deleteVideoPath?goods_pid="+goods_pid,
                dataType:"json",
                success:function(data){
                    $.messager.progress('close');
                    if(data.msg == "0"){
                        topCenter("删除失败");
                    }else{
                        topCenter("删除成功");
                        queryVideo();
                    }
                }
            });
        }

        function uploadInPic(goods_pid){
            $('#pic_dlg').dialog('open');
            $("#pid").val(goods_pid);
        }
        //上传视频
        function uploadTypePicture() {
            $.messager.progress({
                title : '上传视频中',
                msg : '请等待...'
            });

            var formData = new FormData($("#upload_video")[0]);
            $.ajax({
                url: '/cbtconsole/warehouse/vimeoUpload',
                type: 'POST',
                data: formData,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (returndata) {
                    if(returndata.msg =="1"){
                        $.messager.progress('close');
                        topCenter("上传成功");
                        queryVideo();
                        closeUploadDialog();
                    }else{
                        topCenter("上传失败");
                    }
                },
                error: function (returndata) {
                    $.messager.progress('close');
                    topCenter("上传失败");
                }
            });
        }

        function topCenter(msg){
            $.messager.show({
                title:'消息',
                msg:msg,
                showType:'slide',
                style:{
                    right:'',
                    top:document.body.scrollTop+document.documentElement.scrollTop,
                    bottom:''
                }
            });
        }

        function closeUploadDialog() {
            $('#pic_dlg').dialog('close');
            $("#uploadFileForm")[0].reset();
        }
    </script>
</head>
<body onload="$('#pic_dlg').dialog('close');">
<div id="pic_dlg" class="easyui-dialog" title="产品单页视频上传">
    <form id="upload_video" enctype="multipart/form-data">
        <input type="file" name="file">
        <input type="hidden" name="pid" id="pid">
        <input type="hidden" name="token" value="cerong2018jack">
        <input type="button" value="提交" onclick="uploadTypePicture();">
    </form>
</div>
   产品Pid <input type="text" id="goods_pid"/>
   <input type="button" value="查询" onclick="queryVideo();">
    <table border="1">
        <tr>
            <div id="show_info"></div>
        </tr>
    </table>
</body>
</html>
