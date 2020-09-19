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
	<title>轮播列表</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>轮播列表 <a class="btn btn-success radius r mr-20" id="refreshList" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="text-c">
		<c:if test="${map.size()>0}">
			<span class="select-box inline">
				<select name="searchdeAttribute" id="searchdeAttribute" class="select">
					<c:forEach items="${map}" var="entry">
						<option value="${entry.key}" >${entry.value}</option>
					</c:forEach>
				</select>
			</span>
			<input type="text" id="searchdetail" class="input-text" style="width:250px" placeholder="请输入" name="">
			<button type="button" class="btn btn-success" id="searchBtn" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
		</c:if>
	</div>
	<div  class="cl pd-5 bg-1 bk-gray mt-20">
		<span class="l"><a href="javascript:;" onclick="object_show('新增轮播','${basePath }/${url}/add',800,500)" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 新增轮播</a></span>
	</div>
	<table id="cckj_datatable" class="table table-border table-bordered table-bg">
		<thead>
			<tr>
				<th scope="col" colspan="7" class="text-c">轮播列表</th>
			</tr>
			<tr class="text-c">
				<th width="30">序号</th>
				<th width="40">图片</th>
				<th width="30">标题</th>
				<th width="30">链接</th>
				<th width="30">排序号</th>
				<th width="30">创建时间</th>
				<th width="120">操作</th>
			</tr>
		</thead>
	</table>
</div>
<%@include file="../../../basics/footer.jsp"%>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${basePath }/static/HUI/js/myhuijs.js"></script>
<script type="text/javascript" src="${basePath }/static/HUI/lib/datatables/1.10.0/jquery.dataTables.js"></script>
<script type="text/javascript">
    var table;
    inittable();
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
                "url":'${basePath }/${url}/listJson',
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
                {"data":"imagePath",
					"orderable":false,"className":"td-manage text-c",
                    "render":function(data, type, full, meta){
                    var str = '<img style="width: 200px;" src="${basePath }'+data+'">';
                        return str;
                    }
				},
                {"data":"title","orderable":false,"className":"td-manage text-c"},
                {"data":"linkUrl","orderable":false,"className":"td-manage text-c"},
                {"data":"orderNum","orderable":false,"className":"td-manage text-c"},
                {"data":"createTime",
                    "className":"td-manage text-c","orderable":false,
                    "render":function(data, type, full, meta){
                        return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                    }
				},
                {"data":"id",
                    "className":"td-manage text-c","orderable":false,
                    "render":function(data, type, full, meta){
                        var str = "";

                        str +="<a title='编辑' href='javascript:;' onclick=\"object_show('轮播编辑','${basePath}/${url}/edit?id="+data+"',800,500)\" class='btn btn-primary radius size-MINI' style='margin-left:10px;'>";
                        str +="<i class='Hui-iconfont'>&#xe6df;</i>编辑</a>";

                        str +="<a title='删除' href='javascript:;' onclick=\"object_del(this,'${basePath}/${url}',"+data+")\" class='btn btn-danger radius size-MINI' style='margin-left:10px;' style='text-decoration:none'>";
                        str +="<i class='Hui-iconfont'>&#xe6e2;</i>&nbsp;删除</a>";
                        return str;
                    }
                }
            ]
        });
    }
</script>
</body>
</html>
