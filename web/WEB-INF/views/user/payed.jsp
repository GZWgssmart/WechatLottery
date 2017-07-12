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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>已支付</title>
    <link href="<%=path %>/plugins/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="<%=path %>/css/main.css" rel="stylesheet" />
</head>
<body>
<div class="row none-box">
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 none-box">
        <img src="<%=path %>/images/sing.jpg" class="img-responsive"/>
    </div>
</div>
<div class="row none-box">
    <div class="col-xs-12 col-sm-12 com-md-12 col-lg-12">
        <h4>
            欢迎您：${sessionScope.user.wechatNickname}
        </h4>
        <p>您已成功支付${sessionScope.user.payedFee}元，无需再参与！</p>
    </div>
</div>
</body>
<script src="<%=path %>/plugins/jquery-3.2.1.min.js"></script>
<script src="<%=path %>/plugins/bootstrap/bootstrap.min.js"></script>
</html>
