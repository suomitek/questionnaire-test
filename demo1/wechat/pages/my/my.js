// pages/my/my.js
Page({

  data: {
    worker: ""
  },
  onLoad: function () {
  },
  onShow: function () {
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    this.setData({
      wxUserInfo: wxUserInfo
    })
  },
  goto: function (e) {
    var url = e.currentTarget.dataset.url;
    wx.navigateTo({
      url: url,
    })
  },
  addvote: function () {
    wx.navigateTo({
      url: '/pages/add/addvote/selectStyle'
    })
  },
  addwenjuan: function () {
    wx.navigateTo({
      url: '/pages/add/addwenjuan/addwenjuan'
    })
  }
})