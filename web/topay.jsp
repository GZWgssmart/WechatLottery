<%--
  Created by IntelliJ IDEA.
  User: Wang Genshen
  Date: 2017-07-04
  Time: 22:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>活动支付</title>
</head>
<body>
请支付0.01元
<a href="<%=path %>/pay/pay">确认付款</a>
</body>
</html>
