// pages/wenjuanList/wenjuanList.js
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
    questionVos:[],
    moveheigth: 1,
    divheigth: 1,
    fengye: {
      draw: 1,
      length: 3,
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
    var banners = redis.get("wenjuanBanners");
    var bannerFlag = 1;
    var fengye = that.data.fengye
    if (banners!=null){
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
      moveheigth: parseInt(ev.scrollTop)*5
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
    var url = urls.urlobj.wenjuanList;
    var fengye = this.data.fengye;
    var questionVos = this.data.questionVos;
    var bannerFlag = fengye.banner;
    that.setData({ loading: true })
    util.getServerData(url, fengye, null).then(function (res) {
      
      if (bannerFlag == 1) {
        var banners = res.data.data.data.banners;
        redis.put("wenjuanBanners", banners, 60 * 1)//缓存一xiaoshi
        that.setData({
          banners: banners
        })
      }
     
      var tquestionVos = res.data.data.data.questionVos;
      for (var i = 0; i < tquestionVos.length;i++){
        tquestionVos[i].cover = JSON.parse(tquestionVos[i].cover);
      }
      if(tquestionVos.length>0){
        questionVos = questionVos.concat(tquestionVos)
      }
      var fengReturn = res.data.data
      fengye.draw = parseInt(fengye.draw) + 1
      that.setData({
        loading: false,
        fengye: fengye,
        questionVos: questionVos,
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
    var index = e.currentTarget.dataset.index;
    var question = this.data.questionVos[index];
    if (question.examineFlag==1){//
      dialog.warnModal("错误", "考试活动结束前不能复制");
      return
    }
    var addWenJuanData = {
      wentis: JSON.parse(question.problemsStr),
      setting: JSON.parse(question.questionDataStr),
    }
    wx.setStorageSync("addWenJuanData", addWenJuanData)
    dialog.warnModalTrueBack("提示", "已经存为草稿,是否前往添加?", function (flag) {
      if (flag == 1) {
        wx.navigateTo({
          url: '/pages/add/addwenjuan/addwenjuan'
        })
      }
    })
　}
})