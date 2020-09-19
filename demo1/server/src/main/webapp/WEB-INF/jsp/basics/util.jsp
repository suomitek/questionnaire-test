<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">

    function parentReload(){
        window.location.reload();
    }
    function parentRefresh(){
        table.fnDraw();
    }
    $("#searchdeAttribute").bind("change", function () { //按钮 更新提示语
        $("#searchdetail").attr("placeholder","请输入"+$("#searchdeAttribute").find("option:selected").text());
    });
    $("#searchBtn").bind("click", function () { //按钮 触发table重新请求服务器
        table.fnDraw();
    });

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
                url:'${basePath}/${url}/delete',// 跳转到 action
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
