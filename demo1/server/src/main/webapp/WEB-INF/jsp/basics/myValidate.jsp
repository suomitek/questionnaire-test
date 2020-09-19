<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="${basePath }/static/HUI/lib/jquery/1.9.1/jquery.min.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/jquery.validate.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/myValidate.js"></script>
<script src="${basePath }/static/js/inputToJson.js"></script>

<script type="text/javascript">

    $().ready(function() {
        // 后台属性是否存在的验证
        jQuery.validator.addMethod("checkAttribute", function(value, element,message) {
            var flag = true;
            $.ajax({
                type:'post',
                url:'${basePath}/${url}/loadCheckAttributeIsExistence',// 跳转到 action
                cache:false,
                async:false,
                data:{"checkVal":value,"attribute":message,"id":$("#id").val()},
                dataType:'json',
                success:function(data) {
                    if(data.resultCode =="200" ){
                        flag = true;
                    }else{
                        flag = false;
                    }
                }
            });
            return flag;
        }, "已被使用");
        jQuery.extend(jQuery.validator.messages, {
            required: "必选字段",
            remote: "请修正该字段",
            email: "正确格式的电子邮件",
            url: "合法的网址",
            date: "合法的日期",
            dateISO: "合法的日期 (ISO).",
            number: "合法的数字",
            digits: "只能输入整数",
            creditcard: "合法的信用卡号",
            equalTo: "请再次输入相同的值",
            accept: "拥有合法后缀名的字符串",
            maxlength: jQuery.validator.format("长度不能超过{0}"),
            minlength: jQuery.validator.format("长度不能小于{0}"),
            rangelength: jQuery.validator.format("长度介于{0}和{1}"),
            range: jQuery.validator.format("一个介于{0}和{1}之间的值"),
            max: jQuery.validator.format("最大不能超过{0}"),
            min: jQuery.validator.format("最小不能小于{0}")
        });
// 身份证验证
        jQuery.validator.addMethod("isIdCard", function(value, element) {
            var reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
            return this.optional(element) || !(reg.test(value));
        }, "请输入正确的身份证");
// 密码验证
        jQuery.validator.addMethod("isPassword", function(value, element) {
            var password = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/;
            return this.optional(element) || (password.test(value));
        }, "只能包含字母、数字在6-12位之间");
// 手机号码验证
        jQuery.validator.addMethod("isMobile", function(value, element) {
            var length = value.length;
            var mobile = /^1[34578]\d{9}$/;
            return this.optional(element) || (length == 11 && mobile.test(value));
        }, "请正确填写手机号码");
// 固定长度验证
        jQuery.validator.addMethod("strLenght", function(value, element,strLenght) {
            var length = value.length;
            return this.optional(element) || (length == strLenght);
        }, "填写{0}位验证码");
    })

</script>
