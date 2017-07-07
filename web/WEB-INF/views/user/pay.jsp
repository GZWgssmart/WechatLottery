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
</body>
<script>
    if (typeof WeixinJSBridge == "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    } else {
        onBridgeReady();
    }
    function onBridgeReady() {
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId": "${appId}",
                "timeStamp": "${timeStamp}",
                "nonceStr": "${nonceStr}",
                "package": "${packages}",
                "signType": "MD5",
                "paySign": "${paySign}"
            }, function (res) {
                if (res.err_msg == "get_brand_wcpay_request:ok") {
                    alert("微信支付成功!");
                } else if (res.err_msg == "get_brand_wcpay_request:cancel") {
                    alert("用户取消支付!");
                } else if (res.err_msg == "get_brand_wcpay_request:fail") {
                    alert("支付失败!");
                }
            });
    }
</script>
</html>
