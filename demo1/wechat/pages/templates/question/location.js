// pages/templates/question/location.js
var bmapLocation = require('../../../libs/bmapLocation.js');
var redis = require('../../../libs/redis.js');
var dialog = require('../../../libs/dialog/dialog.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    locationInfo:"",
    position:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },
  onShow(){
    var position = redis.get("position");
    if (position!=null){
      this.setData({
        position: position
      })
      redis.remove("position")
    }
  },
  position(){
    var position = this.data.position;
    var weizhi = "";
    if (position != null && position!=""){
      weizhi = position.address
    }
    wx.navigateTo({
      url: '/pages/position/position?weizhi=' + weizhi,
    })
  },
  location(){
    this.getLocation(this.locationCalldack)
  },
  locationCalldack(){
    var locationInfo = redis.get('locationInfo');
    if (locationInfo!=null){
      this.setData({
        locationInfo: locationInfo
      })
    }
  },
  getLocation(calldack) {
    var that = this;
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userLocation'] != true) {
          //用户没授权过
          wx.authorize({
            scope: 'scope.userLocation',
            success(res) {
              bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
                if (flag == 1) {
                  calldack()
                } else {
                  dialog.warnModal("错误", "定位失败1");
                }
              })
            },
            fail(res) {//拒绝后再次授权
              wx.openSetting({
                success(res) {
                  if (res.authSetting["scope.userLocation"]) {
                    // res.authSetting["scope.userLocation"]为trueb表示用户已同意获得定位信息，此时调用getlocation可以拿到信息
                    bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
                      if (flag == 1) {
                        calldack()
                      } else {
                        dialog.warnModal("错误", "定位失败2");
                      }
                    })
                  }
                }
              })
            }
          })
        } else {
          //用户授权过
          bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
            if (flag == 1) {
              calldack()
            } else {
              dialog.warnModal("错误", "定位失败3");
              calldack()
            }
          })
        }
      }
    })
  },
})