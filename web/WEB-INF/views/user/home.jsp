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
    <title>个人主页</title>
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
        </div>
    </div>
    <div class="row none-box">
        <c:choose>
            <c:when test="${empty(sessionScope.user.phone) }">
                <a class="btn btn-primary btn-lg col-xs-12" href="<%=path %>/user/phone">填写手机号，参与抽奖活动！</a>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${requestScope.game_status == 'gaming'}">
                        <a class="btn btn-primary btn-lg col-xs-12" href="<%=path %>/user/topay">开始抽奖！</a>
                    </c:when>
                    <c:when test="${requestScope.game_status == 'game_over'}">
                        <p class="text-warning col-xs-12">抽奖结束，已有${applicationScope.activity_max_user}人参与抽奖！</p>
                    </c:when>
                    <c:when test="${requestScope.game_status == 'not_start'}">
                        <p class="text-warning col-xs-12">抽奖活动将在${applicationScope.activity_begin_time}开始！</p>
                        <a class="btn btn-default btn-lg col-xs-12" href="<%=path %>/user/home">刷新</a>
                    </c:when>
                </c:choose>
            </c:otherwise>
        </c:choose>

    </div>
    <div class="row none-box">
        <div class="col-xs-12">
            <h4>活动说明</h4>
            <p>1、活动时间</p>
            <p>2、活动形式</p>
        </div>
    </div>
</body>
<script src="<%=path %>/plugins/jquery-3.2.1.min.js"></script>
<script src="<%=path %>/plugins/bootstrap/bootstrap.min.js"></script>
</html>
