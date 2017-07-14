<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Wang Genshen
  Date: 2017-07-13
  Time: 22:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path= request.getContextPath();
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>所有付款用户</title>
    <link href="<%=path %>/plugins/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="<%=path %>/plugins/sweet-alert/sweet-alert.css" rel="stylesheet"/>
    <link href="<%=path %>/css/main.css" rel="stylesheet" />
</head>
<body>
    <div class="row none-box">
        <p class="col-lg-offset-3">已支付人数：${applicationScope.actual_pay }</p>
        <a class="col-lg-offset-3 col-lg-6 btn btn-primary" href="<%=path %>/pay/lottery">随机抽出中奖名额！</a>
        <div class="col-lg-12">
        <c:forEach items="${applicationScope.user_map}" var="map">
            <c:if test="${!empty(map.value.tranId)}">
                <div class="col-lg-2" style="margin-top: 20px;">
                        ${map.value.wechatNickname}&nbsp;${map.value.hidePhone}
                </div>
            </c:if>
        </c:forEach>
        </div>
    </div>
</body>
<script src="<%=path %>/plugins/jquery-3.2.1.min.js"></script>
<script src="<%=path %>/plugins/bootstrap/bootstrap.min.js"></script>
<script src="<%=path %>/plugins/sweet-alert/sweet-alert.min.js"></script>
<script src="<%=path %>/js/main.js"></script>
<script>
    $(function() {
       setInterval("toAllPayed()", 5000);
    });

    function toAllPayed() {
        window.location.href = "<%=path %>/pay/all_payed";
    }
</script>
</html>
