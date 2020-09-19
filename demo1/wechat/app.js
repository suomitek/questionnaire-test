//app.js
App({
  onLaunch: function (options) {
    var urls = require('utils/urls.js');
    var that = this;
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        wx.request({
          url: urls.urlobj.getOpenId,
        data: {
          code: res.code
        },
        header: {'content-type': 'application/json' },
        success: function (data) {
          that.globalData.openid = data.data.data.openid;
          wx.checkSession({
            success: function () {
              //session 未过期，并且在本生命周期一直有效
              let wxUserInfo = wx.getStorageSync("wxUserInfo");
              let authToken = wx.getStorageSync("authToken");
              sleep(1000)
              if ('' == wxUserInfo || undefined == wxUserInfo || '' == authToken || undefined == authToken) {
                wx.redirectTo({
                  url: '/pages/login/login'
                })
                return;
              } else {
                that.globalData.wxUserInfo = wxUserInfo;
                that.globalData.authToken = authToken;
              }
            },
            fail: function () {
              //登录态过期
              wx.redirectTo({
                url: '/pages/login/login'
              })
            }
          });
        }
      });
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }else{
          wx.redirectTo({
            url: '/pages/login/login'
          })
        }
      }
    })
    
    function sleep(numberMillis) {
      var now = new Date();
      var exitTime = now.getTime() + numberMillis;
      while (true) {
        now = new Date();
        if (now.getTime() > exitTime)
          return true;
      }
    }
  },
  onShow(options){
    // console.log("onShow--------------------------------")
    if (options.scene == 1007){//分享给好友进入
      var url = '/' + options.path;
      var canshu = "";
      var query = options.query;
      for (var key in query) { // 遍历js对象
        canshu = canshu + "&" + key + "=" + query[key];
      }

      canshu = canshu.substring(1, canshu.length);
      canshu = "?"+canshu
      wx.setStorageSync('loginUrl', url + canshu);
    }else{
      wx.setStorageSync('loginUrl', '/pages/wenjuanList/wenjuanList')
    }
  },
  
  //场景值判断

  globalData: {
    bmpAk : "RyLW3FVO1HFNe2CadiAyaqLqUjddMERq",
    userInfo: null
  }
})