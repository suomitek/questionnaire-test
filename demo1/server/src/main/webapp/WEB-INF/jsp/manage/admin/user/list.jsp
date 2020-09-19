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
	<title>用户列表</title>
</head>
<body>
<nav class="breadcrumb">
	<i class="Hui-iconfont">&#xe67f;</i> ${pathTitle}
	<a class="btn btn-success radius size-MINI r mr-20" id="refreshList" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a>
</nav>
<div class="pd-20">
	<div class="text-c <c:if test="${map.size()<1}">hide</c:if>">
		<span class="select-box inline">
			<select name="searchdeAttribute" id="searchdeAttribute" class="select">
				<c:forEach items="${map}" var="entry">
					<option value="${entry.key}" >${entry.value}</option>
				</c:forEach>
			</select>
		</span>
		<input type="text" id="searchdetail" class="input-text" style="width:250px" placeholder="请输入" name="">
		<button type="button" class="btn btn-success" id="searchBtn" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
	</div>
	<shiro:hasPermission name="/manage/admin/user/add">
		<div  class="cl pd-5 bg-1 bk-gray mt-20">
			<span class="l"><a href="javascript:;" onclick="object_full('新增用户','${basePath}/manage/admin/user/add')" class="btn btn-primary radius size-MINI"><i class="Hui-iconfont">&#xe600;</i> 新增用户</a></span>
		</div>
	</shiro:hasPermission>
	<table id="cckj_datatable" class="table table-border table-bordered table-bg">
		<thead>
			<tr>
				<th scope="col" colspan="7" class="text-c">用户列表</th>
			</tr>
			<tr class="text-c">
				<th width="30">序号</th>
				<th width="30">用户名</th>
				<th width="30">昵称</th>
				<th width="30">创建时间</th>
				<th width="30">电话</th>
				<th width="30">邮箱</th>
				<th width="120">操作</th>
			</tr>
		</thead>
	</table>
</div>
<%@include file="../footer.jsp"%>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${basePath }/static/HUI/lib/datatables/1.10.0/jquery.dataTables.js"></script>
<script type="text/javascript">
    function parentRefresh(){
        window.location.reload();
    }
    $("#searchdeAttribute").bind("change", function () { //按钮 更新提示语
        $("#searchdetail").attr("placeholder","请输入"+$("#searchdeAttribute").find("option:selected").text());
    });
    var table;
    inittable();
    $("#searchBtn").bind("click", function () { //按钮 触发table重新请求服务器
        table.fnDraw();
    });


    //初始页面
    function inittable(){
        table = $('#cckj_datatable').dataTable({
            "info":true,
            "searching":false,
            "lengthChange":true,
            "serverSide":true,
            "pageLength": 10,
            "oLanguage" : {
                "sLengthMenu": "每页显示 _MENU_ 条记录",
                "sZeroRecords": "抱歉， 没有找到",
                "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                "sInfoEmpty": "没有数据",
                "sInfoFiltered": "(从 _MAX_ 条数据中检索)",
                "sZeroRecords": "没有检索到数据",
                "sSearch": "名称:",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "前一页",
                    "sNext": "后一页",
                    "sLast": "尾页"
                }
            },
            "ajax":{
                "url":'${basePath}/manage/admin/user/listJson',
                "type":"GET",
                "data": function ( d ) {
                    return $.extend( {}, d, {
                        "searchdetail": $('#searchdetail').val(),
                        "searchdeAttribute": $('#searchdeAttribute').val()
                    } );
                }
            },
            "columns":[
                {"data":"id","orderable":false,"className":"td-manage text-c"},
                {"data":"name","orderable":false,"className":"td-manage text-c"},
                {"data":"nickName","orderable":false,"className":"td-manage text-c"},
                {"data":"createTimeStr","orderable":false,"className":"td-manage text-c"},
                {"data":"telNumber","orderable":false,"className":"td-manage text-c"},
                {"data":"mailbox","orderable":false,"className":"td-manage text-c"},
                {"data":"id",
                    "className":"td-manage text-c","orderable":false,
                    "render":function(data, type, full, meta){
                        var str = "";

                        <shiro:hasPermission name="/manage/admin/role/distribution">
                        str +="<a title='分配角色' href='javascript:;' onclick=\"object_show('分配角色','${basePath}/manage/admin/role/distribution?userId="+data+"',500,500)\" class='btn btn-primary radius size-MINI' style='margin-left:10px;'>";
                        str +="<i class='Hui-iconfont'>&#xe6df;</i>分配角色</a>";
                        </shiro:hasPermission>

                        <shiro:hasPermission name="/manage/admin/resource/listZtreeByUserId">
                        str +="<a title='查看权限' href='javascript:;' onclick=\"object_show('查看权限','${basePath}/manage/admin/resource/listZtreeByUserId?userId="+data+"',500,500)\" class='btn btn-primary radius size-MINI' style='margin-left:10px;'>";
                        str +="<i class='Hui-iconfont'>&#xe6df;</i>查看权限</a>";
                        </shiro:hasPermission>

                        <shiro:hasPermission name="/manage/admin/user/edit">
                        str +="<a title='编辑' href='javascript:;' onclick=\"object_full('用户编辑','${basePath}/manage/admin/user/edit?userId="+data+"')\" class='btn btn-primary radius size-MINI' style='margin-left:10px;'>";
                        str +="<i class='Hui-iconfont'>&#xe6df;</i>编辑</a>";
                        </shiro:hasPermission>

                        <shiro:hasPermission name="/manage/admin/user/delete">
                        str +="<a title='删除' href='javascript:;' onclick=\"object_del(this,'"+data+"')\" class='btn btn-danger radius size-MINI' style='margin-left:10px;' style='text-decoration:none'>";
                        str +="<i class='Hui-iconfont'>&#xe6e2;</i>&nbsp;删除</a>";
                        </shiro:hasPermission>

                        return str;
                    }
                },

            ]
        });
    }
    /*操作operation*/
    function object_full(name,url){
        var index = layer.open({
            type: 2,
            title:name,
            content: url
        });
        layer.full(index);
    }
    /*操作object_show*/
    function object_show(name,url,w,h){
        layer_show(name,url,w,h);
    }
    /*删除*/
    function object_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
            //此处请求后台程序，下方是成功后的前台处理
            $.ajax({
                type:'post',
                url:'${basePath}/manage/admin/user/delete',// 跳转到 action
                cache:false,
                data:{"userId":id},
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
