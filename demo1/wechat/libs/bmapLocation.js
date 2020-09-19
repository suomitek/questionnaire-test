var bmap = require('bmap-wx.js');
var redis = require('redis.js');
function getLocation(ak,flag, callback) {
  // console.log(ak)
  var wxMarkerData = []
  var locationInfo = redis.get("locationInfo");
  // console.log(locationInfo)
  if (locationInfo == null || locationInfo==""){//看缓存有没有 有就直接过去了
    locationInfo={}
    /* 获取定位地理位置 */
    // 新建bmap对象   
    var BMap = new bmap.BMapWX({
      ak: ak
    });
    
    var fail = function (data) {
      console.log("fail")
      console.log(data)
      callback(0);
    };
    var success = function (data) {
      //返回数据内，已经包含经纬度  
      console.log("success")
      console.log(data);
      //使用wxMarkerData获取数据  
      wxMarkerData = data.wxMarkerData;
      //把所有数据放在初始化re内

      locationInfo.markers = wxMarkerData;
      locationInfo.latitude = wxMarkerData[0].latitude;
      locationInfo.longitude = wxMarkerData[0].longitude;
      locationInfo.address = wxMarkerData[0].address;
      locationInfo.cityInfo = data.originalData.result.addressComponent;

      var Address = data.wxMarkerData[0].address
      if (Address.search('省')) {
        Address = Address.slice(3)
      }
      // wx.setStorageSync('locationInfo', locationInfo);
      redis.put("locationInfo", locationInfo, 600)//缓存时间为600秒 10分
      callback(1);
    };
    // 发起regeocoding检索请求   
    BMap.regeocoding({
      fail: fail,
      success: success
    });
  }else{
    callback(1);
  }
}

module.exports = {    //数据暴露出去
  getLocation: getLocation
}