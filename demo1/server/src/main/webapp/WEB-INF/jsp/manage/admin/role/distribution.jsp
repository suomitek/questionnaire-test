<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta charset="utf-8">
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta http-equiv="Cache-Control" content="no-siteapp" />
	<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui/css/H-ui.min.css" />
	<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui.admin/css/H-ui.admin.css" />
	<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/lib/Hui-iconfont/1.0.8/iconfont.css" />
	<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui.admin/skin/default/skin.css" id="skin" />
	<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui.admin/css/style.css" />
	<link rel="stylesheet" href="${basePath }/static/HUI/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<title>资源列表</title>
</head>
<body>
<nav class="breadcrumb">
	<i class="Hui-iconfont">&#xe67f;</i> ${pathTitle}
	<a class="btn btn-success radius size-MINI r mr-20" id="refreshList" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a>
</nav>
<div class="pd-20">
	<div class="row cl">
		<div class="col-xs-3 col-sm-2 col-xs-3">
			<input class="btn btn-primary radius size-MINI" type="submit" id="submit" value="&nbsp;&nbsp;保存&nbsp;&nbsp;">
		</div>
	</div>
</div>
	<input type="hidden" id="userId" value="${userId}"/>
	<div  class="cl pd-12 bg-1 bk-gray">
		<c:forEach items="${roleList}" var="role">
			<div class="check-box">
				<input type="checkbox" id="roles" name="roles" value="${role.id}" <c:if test="${role.flag ==true}">checked</c:if>>
				<label for="roles">${role.roleName}</label>
			</div>
		</c:forEach>
	</div>

<%@include file="../footer.jsp"%>
<script type="text/javascript">
    $("#submit").bind("click",function () {
        var roleObj=$("input[name='roles']:checked");

        var roles;
        for(var i=0;i<roleObj.length;i++){
            if(i==0){
                roles = $(roleObj[i]).val();
			}else{
                roles += ","+$(roleObj[i]).val();
			}
		}
        $.ajax( {
            url:'${basePath}/manage/admin/role/distributionSave',// 跳转到 action
            type:'post',
            cache:false,
            data:{"userId":${userId},"roles":roles},
            dataType:'json',
            success:function(data) {
                if(data.resultCode =="200" ){
                    layer.msg('修改成功',{icon:1,time:1000});
                }else{
                    layer.msg(data.resultMsg,{icon:5,time:1500});
                }
            },
            error : function(data) {
                layer.msg(data.resultMsg,{icon:5,time:1500});
            }
        });
    })
</script>
</body>
</html>
