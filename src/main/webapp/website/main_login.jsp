<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>电商后台管理系统登录</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
</head>
<body onload="init();">


<div style="margin-left: 700px; margin-top: 240px;">

<form action="/cbtconsole/userLogin/checkUserInfo.do" method="post">
    <div style="width: 500px; font-size: 28px; background: rgb(140, 228, 111);">
        <h3 style="text-align: center">电商后台管理系统登录</h3>
        <div style="padding: 10px 60px 20px 60px;">
            <table>
                <tr>
                    <td>用户名:</td>
                    <td><input
                            style="width: 182px; height: 30px; font-size: 24px;" type="text" id="username"
                            name="userName"/></td>
                </tr>
                <tr>
                    <td>密&nbsp;&nbsp;&nbsp;码:</td>
                    <td><input
                            style="width: 182px; height: 30px; font-size: 24px;" type="password" id="pwd" name="passWord"
                            /></td>
                            <!-- onkeypress="if (event.keyCode == 13){submitForm()}" -->
                </tr>
                <tr>
                    <td></td>
                    <td><br>
                        <input style="width: 166px;height: 40px;font-size: 26px;background-color: #e0d7ef;"
                               type="submit" 
                               value="登录"/>
                               <!--onclick="submitForm()"  -->
                        <div class="login_info" style="color: red;"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    </form>
</div>
<script>
    fnAutoLoginByCookie();

    function init() {
        $("#username").focus();
    }

    function submitForm() {
        var userName = $('#username').val();
        var passWord = $('#pwd').val();
        if (!userName) {
            $('.login_info').html("用户名不能为空！");
            $('.login_info').show();
        } else if (!passWord) {
            $('.login_info').html("密码不能为空！");
            $('.login_info').show();
        } else {

            $
                .ajax({
                    type: 'POST',
                    url: '/cbtconsole/userLogin/checkUserInfo.do',
                    data: {
                        'userName': userName,
                        'passWord': passWord
                    },
                    success: function (data) {
                        if (data.ok) {
                            window.location = "/cbtconsole/website/main_menu.jsp";
                        } else {
                            $('.login_info').html(data.message);
                            $('.login_info').show();
                        }
                    }
                });
        }
    }

    function fnAutoLoginByCookie() {
        $.ajax({
            type: 'POST',
            url: '/cbtconsole/userLogin/autoLoginByCookie.do',
            success: function (data) {
                if (data.ok) {
                    window.location = "/cbtconsole/website/main_menu.jsp";
                } else {
                    /*window.location = "/cbtconsole/userLogin/loginOut.do";*/
                    // $('.login_info').html(data.message);
                    // $('.login_info').show();
                }
            }
        });
    }
</script>
</body>
</html>