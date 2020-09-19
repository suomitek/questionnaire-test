// pages/login/login.js
const app = getApp()
var urls = require('../../utils/urls.js');
var util = require('../../utils/util.js');
var redis = require('../../libs/redis.js');
Page({
  data: {
    //判断小程序的API，回调，参数，组件等是否在当前版本可用。
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  onLoad: function () {
    var that = this;
    // 查看是否授权
    // wx.getSetting({
    //   success: function (res) {
    //     if (res.authSetting['scope.userInfo']) {
    //       wx.getUserInfo({
    //         success: function (res) {
    //           //从数据库获取用户信息
    //           that.queryUsreInfo();
    //           //用户已经授权过
    //           wx.switchTab({
    //             url: '/pages/index/index'
    //           })
    //         }
    //       });
    //     }
    //   }
    // })
  },
  bindGetUserInfo: function (e) {
    if (e.detail.userInfo) {
      //用户按了允许授权按钮
      var that = this;
      //插入登录的用户的相关信息到数据库
      wx.request({
        url: urls.urlobj.register,
        data: {
          openid: getApp().globalData.openid,
          nickName: e.detail.userInfo.nickName,
          avatarUrl: e.detail.userInfo.avatarUrl,
          city: e.detail.userInfo.city,
          province: e.detail.userInfo.province,
          country: e.detail.userInfo.country,
          gender: e.detail.userInfo.gender
        },
        header: {
          'content-type': 'application/json'
        },
        success: function (res) {
          wx.setStorageSync('wxUserInfo', res.data.data)
          getApp().globalData.userInfo = res.data.data;
          that.getAuthToken();
        }
      });
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '警告',
        content: '您点击了拒绝授权，将无法进入小程序，请授权之后再进入!!!',
        showCancel: false,
        confirmText: '返回授权',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击了“返回授权”')
          }
        }
      })
    }
  },
  //获取用户信息接口
  queryUsreInfo: function () {
    var that = this
    wx.request({
      url: urls.urlobj.register,
      data: {
        openid: getApp().globalData.openid
      },
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        wx.setStorageSync('wxUserInfo', res.data.data)
        getApp().globalData.userInfo = res.data.data;
        that.getAuthToken();
      }
    });
  },
  getAuthToken: function(){
    let wxUserInfo = wx.getStorageSync("wxUserInfo")
    wx.request({
      url: urls.urlobj.wxLogin,
      data: {
        openid: wxUserInfo.openid
      },
      header: {'Content-Type': 'application/json'},
      success: function (res) {
        if (res.data.resultCode==200){
          redis.put("authToken", res.data.data, 6000)//默认十分钟
          getApp().globalData.authToken = res.data.data;
          var loginUrl = wx.getStorageSync('loginUrl');
          wx.redirectTo({
            url: loginUrl,
            fail: function(res){////////跳tar
              wx.switchTab({
                url: loginUrl,
              })
            }
          })
           
        }else{
          wx.showToast({
            title: '微信登录失败',
            icon: 'success',
            duration: 2000
          })
        }
      }
    })
  }
})