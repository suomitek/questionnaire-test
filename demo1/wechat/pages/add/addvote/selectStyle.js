// pages/add/addvote/selectStyle.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  selectStyle: function (e) {
    wx: wx.navigateTo({
      url: "/pages/add/addvote/addvote?voteType=" + e.currentTarget.dataset.index,
    })
  },
})