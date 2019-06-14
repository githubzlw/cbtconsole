<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2019/6/13
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>批量上传</title>
    <script type="text/javascript">
        //上传视频
        function uploadTypePicture() {
            $.messager.progress({
                title : '上传视频中',
                msg : '请等待...'
            });

            var formData = new FormData($("#upload_video")[0]);
            $.ajax({
                url: '/cbtconsole/warehouse/UploadAll',
                type: 'POST',
                data: formData,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (returndata) {
                    if(returndata[0] =="1"){
                        $.messager.progress('close');
                        alert("上传成功");
                        // closeUploadDialog();
                    }else if (returndata[0] =="0"){
                        alert("上传失败");
                        $.messager.progress('close');
                    }else {
                        alert(returndata+"上传失败")
                        $.messager.progress('close');
                    }
                },
                error: function (returndata) {
                    $.messager.progress('close');
                    topCenter("上传失败");
                }
            });
        }
        function closeUploadDialog() {
            $('#upload').dialog('close');
            // $("#uploadFileForm")[0].reset();
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
    </script>
</head>
<body >
<div id="upload" class="easyui-dialog" style="height: 300px;width: 500px" title="产品单页视频上传">
    <form id="upload_video" enctype="multipart/form-data">
        <input style="width: 400px;height: 200px" id="file" type="file" size="30" name="file" multiple=true />
        <input style="height: 100px" type="hidden" name="token" value="cerong2018jack">
        <input type="button" value="提交" onclick="uploadTypePicture();">
    </form>
</div>
</table>
</body>
</html>
