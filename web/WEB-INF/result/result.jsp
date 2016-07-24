<%--
  Created by IntelliJ IDEA.
  User: oahnus
  Date: 2016/7/24
  Time: 18:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if IE 8 ]>
<html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9 ]>
<html lang="en" class="ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="en"> <!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>注册成功</title>
</head>
<body>
<h1 style="text-align: center">恭喜您注册成功</h1>
<h2 style="text-align: center">账号为<div style="color: red"><%=request.getAttribute("userID") %></div></h2>
<h2 style="text-align: center">请牢记密码:<div style="color: red"><%=request.getParameter("password") %></div></h2>
</body>
</html>

