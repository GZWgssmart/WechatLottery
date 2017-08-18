<%--
  Created by IntelliJ IDEA.
  User: Wang Genshen
  Date: 2017-07-04
  Time: 19:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>中奖次数</title>
    <link href="<%=path %>/plugins/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="<%=path %>/plugins/sweet-alert/sweet-alert.css" rel="stylesheet"/>
    <link href="<%=path %>/css/main.css" rel="stylesheet" />
    <style>
        .btn_div button {
            margin-bottom:5px;
        }
    </style>
</head>
<body>
<div class="row none-box">
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 none-box">
        <img src="<%=path %>/${applicationScope.logo_img}" class="img-responsive"/>
    </div>
</div>
<div class="row none-box">
    <div class="col-xs-12 col-sm-12 com-md-12 col-lg-12">
        <h4>
            欢迎您：${sessionScope.user.wechatNickname}
        </h4>
    </div>
</div>
<div class="row none-box">
    <h4>请选择您想要的中奖次数，如选择5，则您可以最多中奖5次，每一次的中奖次数需要支付一小笔随机金额，随机金额在0.01-100元间不等</h4>
    <div class="col-xs-12 btn_div">
    <button class="btn btn-primary col-xs-3 col-xs-offset-2" onclick="chooseCount(1);">1</button>
    <button class="btn btn-primary col-xs-3 col-xs-offset-1" onclick="chooseCount(2);">2</button>
    <button class="btn btn-primary col-xs-3 col-xs-offset-2" onclick="chooseCount(3);">3</button>
    <button class="btn btn-primary col-xs-3 col-xs-offset-1" onclick="chooseCount(4);">4</button>
    <button class="btn btn-primary col-xs-3 col-xs-offset-2" onclick="chooseCount(5);">5</button>
    </div>

    <p style="margin-bottom: 5px;">中奖次数：<span id="chooseCount"></span></p>

    <button class="btn btn-primary col-xs-12" onclick="toPay();">确认抽奖！</button>

</div>
</body>
<script src="<%=path %>/plugins/jquery-3.2.1.min.js"></script>
<script src="<%=path %>/plugins/bootstrap/bootstrap.min.js"></script>
<script src="<%=path %>/plugins/sweet-alert/sweet-alert.min.js"></script>
<script>

    function chooseCount(count) {
        $("#chooseCount").text(count);
    }

    function toPay() {
        var count = $("#chooseCount").text();
        if (count == "") {
            swal("请选择中奖次数", "", "error")
        } else {
            window.location.href = "<%=path %>/user/topay?count=" + count;
        }
    }
</script>
</html>
