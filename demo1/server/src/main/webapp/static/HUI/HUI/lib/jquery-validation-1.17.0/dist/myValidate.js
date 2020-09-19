jQuery.extend(jQuery.validator.messages, {
    required: "请输入必选字段",
    remote: "请修正该字段",
    email: "请输入正确格式的电子邮件",
    url: "请输入合法的网址",
    date: "请输入合法的日期",
    dateISO: "请输入合法的日期 (ISO).",
    number: "请输入合法的数字",
    digits: "请输入只能输入整数",
    creditcard: "请输入合法的信用卡号",
    equalTo: "请再次输入相同的值",
    accept: "拥有合法后缀名的字符串",
    maxlength: jQuery.validator.format("长度不能超过{0}"),
    minlength: jQuery.validator.format("长度不能小于{0}"),
    rangelength: jQuery.validator.format("长度介于{0}和{1}"),
    range: jQuery.validator.format("一个介于{0}和{1}之间的值"),
    max: jQuery.validator.format("最大不能超过{0}"),
    min: jQuery.validator.format("最小不能小于{0}")
});
// 密码匹配验证
jQuery.validator.addMethod("password", function(value, element) {
    var length = value.length;
    var mobile = /^[a-zA-Z]\w{6,12}$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "只能以字母开头包含字母、数字和下划线在6-12位之间");
// 手机号码验证
jQuery.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^1[34578]\d{9}$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写手机号码");
// 固定长度验证
jQuery.validator.addMethod("length", function(value, element) {
    var length = value.length;
    return this.optional(element) || (length == value);
}, "填写{0}位验证码");
// 后台属性是否存在的验证
jQuery.validator.addMethod("checkAttribute", function(value, element,message) {
    var flag = true;
    $.ajax({
        type:'post',
        url:'${basePath}/admin/user/loadCheckAttributeIsExistence',// 跳转到 action
        cache:false,
        async:false,
        data:{"checkVal":value,"attribute":message},
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