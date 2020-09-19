// pages/my/lishi/lishi.js
const app = getApp()
var urls = require('../../../utils/urls.js');
var util = require('../../../utils/util.js');
var dialog = require('../../../libs/dialog/dialog.js');
var redis = require('../../../libs/redis.js');
Page({
  data: {
    host: urls.host,
    swiperTitle: [{
      text: "投票足迹"
    }, {
      text: "问卷足迹"
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
    var url = urls.urlobj.footprints;
    util.getServerData(url, {
      subscriberId: wxUserInfo.id
    }, null).then(function (res) {
      that.setData({
        voteList: res.data.data.wjVoteVos
      })
      var questionList = res.data.data.wjQuestionVos
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