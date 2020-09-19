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
	<title>API列表</title>
</head>
<body>
<span class="l ml-20 mt-10 mb-10">
	<a href="javascript:;" onclick="add()" class="btn btn-primary radius size-MINI"><i class="Hui-iconfont">&#xe600;</i>添加API</a>
</span>
<table id="cckj_datatable" class="table table-border table-bordered table-bg">
	<thead>
	<tr>
		<th scope="col" colspan="7" class="text-c">API列表</th>
	</tr>
	<tr class="text-c">
		<th width="30">序号</th>
		<th width="30">链接地址</th>
		<th width="120">操作</th>
	</tr>
	<c:forEach items="${apis}" var="api">
		<tr class="text-c">
			<td width="30">${api.id}</td>
			<td width="120">
				<input value="${api.resUrl}"  class="input-text apiedit" type="text" apiid="${api.id}"/>
			</td>
			<td width="30">
				<a title='删除' href='javascript:;' onclick="delect(this,'${api.id}')" class='btn btn-danger radius size-MINI' style='margin-left:10px;' style='text-decoration:none'>
				<i class='Hui-iconfont'>&#xe6e2;</i>&nbsp;删除</a>
			</td>
		</tr>
	</c:forEach>

	</thead>
</table>
<%@include file="../../../basics/footer.jsp"%>
<script>
	$(".apiedit").change(function(){
        var id = $(this).attr("apiid")
		var resUrl = $(this).val();
        $.ajax({
            type:'post',
            url:'${basePath}/manage/admin/api/editSave',// 跳转到 action
            cache:false,
            data:{"id":id,"resUrl":resUrl},
            dataType:'json',
            success:function(data) {
                if(data.resultCode =="200" ){
                    layer.msg("修改 成功",{icon:1,time:1000});
                }else{
                    layer.msg(data.resultMsg,{icon:5,time:1000});
                }
            },
            error : function() {
                layer.msg(data.resultMsg,{icon:5,time:1000});
            }
        });
    });

	function add() {
        layer.prompt({title: '输入API地址，并确认', formType: 2}, function(pass, index){
            if(pass!=""||pass!=null){
                $.ajax({
                    type:'post',
                    url:'${basePath}/manage/admin/api/addSave',// 跳转到 action
                    cache:false,
                    data:{"resourceId":"${resourceId}","resUrl":pass,"id":0},
                    dataType:'json',
                    success:function(data) {
                        if(data.resultCode =="200" ){
                            layer.msg("添加成功",{icon:1,time:1000});
                            layer.close(index);
                            window.location.reload();
                        }else{
                            layer.msg(data.resultMsg,{icon:5,time:1000});
                        }
                    },
                    error : function() {
                        layer.msg(data.resultMsg,{icon:5,time:1000});
                    }
                });
			}else {
                layer.msg("地址不能为空",{icon:5,time:1000});
			}
        });
    }

	function delect(obj,id) {
        layer.confirm('确认要删除吗？',function(index){
            //此处请求后台程序，下方是成功后的前台处理
            $.ajax({
                type:'post',
                url:'${basePath}/manage/admin/api/delete',// 跳转到 action
                cache:false,
                data:{"id":id},
                dataType:'json',
                success:function(data) {
                    if(data.resultCode =="200" ){
                        $(obj).parents("tr")[0].remove();
                        layer.msg("删除成功",{icon:1,time:1000});
                    }else{
                        layer.msg(data.resultMsg,{icon:5,time:1000});
                    }
                },
                error : function() {
                    layer.msg(data.resultMsg,{icon:5,time:1000});
                }
            });
        });
    }
</script>
</body>
</html>
