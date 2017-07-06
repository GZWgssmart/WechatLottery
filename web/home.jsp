<%--
  Created by IntelliJ IDEA.
  User: Wang Genshen
  Date: 2017-07-04
  Time: 19:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>个人主页</title>
</head>
<body>
    ${sessionScope.user.wechatNickname}
    <a href="<%=path %>/user/topay">去付款</a>
</body>
</html>
