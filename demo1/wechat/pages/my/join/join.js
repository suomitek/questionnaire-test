// pages/my/join/join.js
const app = getApp()
var urls = require('../../../utils/urls.js');
var util = require('../../../utils/util.js');
var dialog = require('../../../libs/dialog/dialog.js');
var redis = require('../../../libs/redis.js');
Page({
  data: {
    host: urls.host,
    swiperTitle: [{
      text: "参与的投票"
    }, {
        text: "参与的问卷"
    }],
    currentPage: 0,
    showVideo: false,
    maskShow: false,
    maskName: "",
    maskId: 0,
    voteList: []
  },
  onLoad: function (options) {
    var wxUserInfo = wx.getStorageSync("wxUserInfo");
    var currentPage = options.currentPage;
    if (typeof (currentPage) != "undefined") {
      this.setData({
        currentPage: currentPage
      })
    }
    var that = this;
    var url = urls.urlobj.joinvotes;
    util.getServerData(url, {
      wxUserId: wxUserInfo.id
    }, null).then(function (res) {
      that.setData({
        voteList: res.data.data
      })

    });
    var url = urls.urlobj.joinwenjuans;
    util.getServerData(url, {
      wxUserId: wxUserInfo.id
    }, null).then(function (res) {
      var questionList = res.data.data
      for (var i = 0; i < questionList.length; i++) {
        var question = questionList[i];
        question.cover = JSON.parse(question.cover);
        questionList[i] = question
      }
      that.setData({
        questionList: questionList
      })
    });
  },
  turnPage: function (e) {
    this.setData({
      currentPage: e.currentTarget.dataset.index
    })
  },
  maskShow(e) {
    var id = e.currentTarget.dataset.id;
    var index = e.currentTarget.dataset.index;
    var type = e.currentTarget.dataset.type;
    if (type === "question") {
      wx.navigateTo({
        url: '/pages/wenjuan/detailed?id=' + id
      })
    } else {
      wx.navigateTo({
        url: '/pages/vote/detailed?id=' + id
      })
    }
  },
})