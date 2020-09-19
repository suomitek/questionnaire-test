// pages/voteList/voteList.js
const app = getApp()
var urls = require('../../utils/urls.js');
var util = require('../../utils/util.js');
var dialog = require('../../libs/dialog/dialog.js');
var redis = require('../../libs/redis.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: urls.host,
    banners: [],
    loading: false,
    voteVos: [],
    moveheigth: 1,
    divheigth: 1,
    fengye: {
      draw: 1,
      length: 10,
      banner: 1
    },
    fengReturn: {
      draw: 1,
      length: 1,
      pageNum: 1,
    },
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var banners = redis.get("voteBanners");
    var bannerFlag = 1;
    var fengye = that.data.fengye
    if (banners != null) {
      fengye.banner = 0
      that.setData({
        fengye: fengye,
        banners: banners
      })
    }
    that.fetchCommentList(1);
    this.setData({
      divheigth: wx.getSystemInfoSync().windowWidth * 0.3
    })
  },
  onPageScroll: function (ev) {

    this.setData({
      moveheigth: parseInt(ev.scrollTop) * 5
    })
  },
  onReachBottom() {
    // 下拉触底，先判断是否有请求正在进行中
    // 以及检查当前请求页数是不是小于数据总页数，如符合条件，则发送请求
    if (!this.loading && this.data.fengReturn.pageNum >= this.data.fengye.draw) {
      this.fetchCommentList(this.data.fengye.draw + 1)
    }
  },
  fetchCommentList(pageNo) {
    var that = this;
    var url = urls.urlobj.votes;
    var fengye = this.data.fengye;
    var voteVos = this.data.voteVos;
    var bannerFlag = fengye.banner;
    that.setData({ loading: true })
    util.getServerData(url, fengye, null).then(function (res) {

      if (bannerFlag == 1) {
        var banners = res.data.data.data.banners;
        redis.put("voteBanners", banners, 60 * 60)//缓存一xiashi
        that.setData({
          banners: banners
        })
      }

      var tvoteVos = res.data.data.data.voteVos;
      if (tvoteVos.length > 0) {
        voteVos = voteVos.concat(tvoteVos)
      }
      var fengReturn = res.data.data
      fengye.draw = parseInt(fengye.draw) + 1
      that.setData({
        loading: false,
        fengye: fengye,
        voteVos: voteVos,
        fengReturn: fengReturn
      })
    });
  },
  goto: function (e) {
    var url = e.currentTarget.dataset.url;
    wx.navigateTo({
      url: url,
    })
  },
  //手指触摸开始赋值
　touchStart: function (e) {
　　 this.startTime = e.timeStamp;
　},
　　//手指触摸结束赋值
　touchEnd: function (e) {
　　this.endTime = e.timeStamp;
　},
　// nophonefull 不管点击还是长按都会触发的事件
　nophonefull: function (e) {
　　　　//通过判断手指触摸时间来判断是否是点击事件，当时间差小于350时，为点击事件
　  if (this.endTime - this.startTime < 350) {
      this.goto(e);
  　}
　},
  copy: function (e) {
    dialog.warnModal("错误", "投票活动不支持复制");
　}
})