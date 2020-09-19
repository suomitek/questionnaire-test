<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="${basePath }/static/HUI/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=uRhBKfzW36pGW8KGkyAhpwXDYwmjNcZP"></script> 
<script>


    // 百度地图API功能
    var map = new BMap.Map("container");
    var point = new BMap.Point(116.331398,39.897445);
    map.centerAndZoom(point,12);
    map.enableScrollWheelZoom(true);
    var geoc = new BMap.Geocoder();
    map.addEventListener("click", function(e){
        //通过点击百度地图，可以获取到对应的point, 由point的lng、lat属性就可以获取对应的经度纬度
        var pt = e.point;
        geoc.getLocation(pt, function(rs){
            //addressComponents对象可以获取到详细的地址信息
            var addComp = rs.addressComponents;
            var site = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
            //将对应的HTML元素设置值
            $("#address").val(site);
            $("#longitude").val(pt.lng);
            $("#latitude").val(pt.lat);
            addMap();
        });
    });
    function addMap(){
        var map = document.getElementById("container");
        if(map.style.display=='none'){
            map.style.display='block';
        }else{
            map.style.display='none';
        }
    }
</script>
