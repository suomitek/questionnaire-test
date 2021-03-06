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
	<title>菜单列表</title>
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
	<div  class="cl pd-5 bg-1 bk-gray mt-20">
		<shiro:hasPermission name="/manage/admin/resource/listAllZtree">
			<span class="l"><a href="javascript:;" onclick="object_show('菜单树','${basePath}/manage/admin/resource/listAllZtree',400,500)" class="btn btn-primary radius size-MINI">菜单树</a></span>
		</shiro:hasPermission>
		<shiro:hasPermission name="/manage/admin/resource/add">
		<span class="l ml-20"><a href="javascript:;" onclick="object_show('添加根菜单','${basePath}/manage/admin/resource/add?parentId=0',800,500)" class="btn btn-primary radius size-MINI"><i class="Hui-iconfont">&#xe600;</i>添加根菜单</a></span>
		</shiro:hasPermission>
	</div>

	<table id="cckj_datatable" class="table table-border table-bordered table-bg">
		<thead>
			<tr>
				<th scope="col" colspan="7" class="text-c">菜单列表</th>
			</tr>
			<tr class="text-c">
				<th width="20">序号</th>
				<th width="30">菜单名</th>
				<th width="30">编码</th>
				<th width="30">路径</th>
				<th width="10">级别</th>
				<th width="10">资源类型</th>
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
                "url":'${basePath}/manage/admin/resource/listJson',
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
                {"data":"resName","orderable":false,"className":"td-manage text-c"},
                {"data":"resCode","orderable":false,"className":"td-manage text-c"},
                {"data":"resUrl","orderable":false,"className":"td-manage text-c"},
                {"data":"resType","orderable":false,"className":"td-manage text-c"},
                {"data":"btnFlag",
                    "className":"td-manage text-c","orderable":false,
                    "render":function(data, type, full, meta){
                        var str = "";
                        if(parseInt(data)==1){
                            str = "页面资源";
                            <shiro:hasPermission name="/manage/admin/api/list">
                            str +="<a title='添加页面中的Api' href='javascript:;' onclick=\"object_show('添加Api','${basePath}/manage/admin/api/list?resourceId="+full.id+"',800,400)\" class='btn btn-primary radius size-MINI' style='margin-left:10px;'>";
                            str +="<i class=\"Hui-iconfont\">&#xe600;</i>管理Api</a>";
                            </shiro:hasPermission>
                        }else{
                            str = "按钮资源";
						}
                        return str;
					}
				},
				{"data":"id",
                    "className":"td-manage text-c","orderable":false,
                    "render":function(data, type, full, meta){
                        var str = "";

                        if(full.resType!=3){
							<shiro:hasPermission name="/manage/admin/resource/add">
                            str +="<a title='添加子资源' href='javascript:;' onclick=\"object_show('添加子菜单','${basePath}/manage/admin/resource/add?parentId="+data+"',800,500)\" class='btn btn-primary radius size-MINI' style='margin-left:10px;'>";
                            str +="<i class=\"Hui-iconfont\">&#xe600;</i>添加子菜单</a>";
							</shiro:hasPermission>
						}
						<shiro:hasPermission name="/manage/admin/resource/edit">
                        str +="<a title='编辑' href='javascript:;'onclick=\"object_show('编辑','${basePath}/manage/admin/resource/edit?id="+data+"',800,500)\" class='btn btn-primary radius size-MINI' style='margin-left:10px;'>";
                        str +="<i class='Hui-iconfont'>&#xe6df;</i>编辑</a>";
						</shiro:hasPermission>
						<shiro:hasPermission name="/manage/admin/resource/delete">
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
                url:'${basePath}/manage/admin/resource/delete',// 跳转到 action
                cache:false,
                data:{"id":id},
                dataType:'json',
                success:function(data) {
                    if(data.resultCode =="200" ){
                        parentRefresh();
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
