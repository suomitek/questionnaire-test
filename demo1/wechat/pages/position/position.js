var bmap = require('../../libs/bmap-wx.js');
var redis = require('../../libs/redis.js');
var wxMarkerData = [];  //定位成功回调对象  
var app = getApp();
Page({
  data: {
    weizhi:"",
    hidden: true,
    open:true,
    sugDataList:[],
    ak: app.globalData.bmpAk,
    longitude: '',   //经度  
    latitude: '',    //纬度  
    address: '',     //地址  
    cityInfo: {},     //城市信息
  },
  onLoad: function (options) {
    var weizhi = options.weizhi;
    var data = options.data;//从答题页面传过来就是index 索引
    if (typeof (data) =="undefined"){
      data = -1;
    }
    this.setData({
      weizhi: weizhi,
      data: data
    })
  },
  bindKeyInput: function (e) {
    this.setData({
      weizhi: e.detail.value,
      hidden: true
    })
  },
  search: function () {
    var text = this.data.weizhi;
    if (text == "" || text==null){
      return;
    }
    this.setData({
      hidden:false
    })
    var that = this;
    var BMap = new bmap.BMapWX({
      ak: that.data.ak
    });
    var fail = function (data) {
      console.log("fail")
      console.log(data)
    };
    var success = function (data) {
      console.log("success")
      console.log(data)
      var sugDataList = [];
      sugDataList.push(data.result);
      that.setData({
        sugDataList: sugDataList[0]
      });
    }
    BMap.suggestion({
      query: text,
      region: '全国',
      city_limit: false,
      fail: fail,
      success: success
    }); 
  },
  onTap: function (e) {
    var data = this.data.data;
    var sugData = this.data.sugDataList[e.currentTarget.dataset.index];
    var location = {
      markers: null,
      latitude: sugData.location.lat,
      longitude: sugData.location.lng,
      address: sugData.province + sugData.city + sugData.district + sugData.name
    };
    redis.put("position", location, 60);
    redis.put("data", data, 60);
    wx:wx.navigateBack({
      delta: 1,
    })
  },
  
}) 