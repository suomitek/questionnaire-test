<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>后台登录${environment.name }</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="${basePath }/static/admin/css/bootstrap.min.css" rel="stylesheet">
    <link href="${basePath }/static/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${basePath }/static/admin/css/animate.css" rel="stylesheet">
    <link href="${basePath }/static/admin/css/style.css" rel="stylesheet">
    <link href="${basePath }/static/admin/css/login.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <script>
        if (window.top !== window.self) {
            window.top.location = window.location;
        }
    </script>

</head>

<body class="signin">
<div class="signinpanel">
    <div class="row">
        <div class="col-sm-12 text-center">
            <form method="post">
                <h1 class="no-margins">${constant.programName }</h1>
                <input type="text" id="name" class="form-control uname" placeholder="用户名" />
                <input type="password" id="password" class="form-control pword m-b" placeholder="密码" />
                <a class="btn btn-success btn-block" onclick="formSubmit()">登录</a>
            </form>
        </div>
    </div>
    <div class="signup-footer">
        <div class="row">
            <div class="col-sm-12 text-center">
                本系统由崔胜利设计和维护
            </div>
        </div>
    </div>
</div>
<script src="${basePath }/static/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${basePath }/static/admin/js/plugins/layer/layer.min.js"></script>
<script type="text/javascript">
    //form表单提交按钮
    function formSubmit(){
        var name = $("#name").val();
        var password = $("#password").val();
        if(name == "") {
            layer.msg("请输入用户名", {time:1000});
            return;
        }
        if(password == "") {
            layer.msg("请输入密码", {time:1000});
            return;
        }
        $.ajax({
            type:"post",
            url:"${basePath}/manage/admin/logindo",
            dataType:"json",
            data:{"name":name,"password":password},
            success:function(data){
                console.log(data);
                if(data.resultCode=="200") {
                    //验证码正确，进行提交操作
                    window.location.href="${basePath}/manage/admin/index";
                }else {
                    layer.msg(data.resultMsg, {time:1000});
                }
            },
            error:function(e){
                alert(e);
            }
        });
    }
</script>
</body>

</html>
