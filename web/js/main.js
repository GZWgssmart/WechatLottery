var contextPath = "";
function updatePhone() {
    if (checkPhone()) {
        $.post(contextPath + "/user/update_phone",
            $("#updatePhoneForm").serialize(),
            function (data) {
                if (data.result == "success") {
                    swal(data.message, "", "success");
                    setTimeout("toHomePage()", 3000);
                } else {
                    swal("绑定手机号失败", "", "error");
                }
            },'json');
    } else {
        swal("请输入正确的手机号", "", "error");
    }
}

function checkPhone(){
    var phone = $("#phone").val();
    if(!(/^1[34578]\d{9}$/.test(phone))){
        return false;
    } else {
        return true;
    }
}

function toHomePage() {
    window.location.href = contextPath + '/user/home';
}

function payFail() {
    swal("微信支付服务有问题，请稍候再试", "", "error");
    setTimeout("payFailResult()", 3000);
}

function cancelPay() {
    swal("取消支付", "", "warning");
    setTimeout("cancelPayResult()", 3000);
}

function paySuccess() {
    swal("支付成功", "", "success");
    setTimeout("toHomePage()", 3000);
}

function payTimeout() {
    swal("指定时间内未支付，您的支付被取消！", "", "warning");
}

function payFailResult() {
    $.get(contextPath + "/pay/pay_result?pay_result=fail",
        function (data) {
            toHomePage();
        }, "json"
    );
}

function cancelPayResult() {
    $.get(contextPath + "/pay/pay_result?pay_result=cancel",
        function (data) {
            toHomePage();
        }, "json"
    );
}