// pages/add/addwenjuan/question/tupian.js
var util = require('../../../../utils/util.js');
var urls = require('../../../../utils/urls.js');
var dialog = require('../../../../libs/dialog/dialog.js');
var redis = require('../../../../libs/redis.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: urls.host,
    numList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25],
    image: {
      id: 0,
      questionId: 0,
      name: "",
      orderNum: -1,
      qType: "tupian",// 图片
      must: 1,
      imgPath: "",
      imgId: 0,
      score: 1,///默认1分
      scoreIndex: 0,///默认0 为 1分
      examineFlag: 0,
      obtain: -1,
    }
  },
  onLoad: function (options) {
    var wenti = redis.get("wenti")
    var examineFlag = parseInt(options.examineFlag);
    if (wenti != null) {
      redis.remove("wenti")
      wenti.examineFlag = examineFlag;
      this.setData({
        image: wenti
      })
    } else {
      wenti = this.data.image;
      wenti.examineFlag = examineFlag;
      this.setData({
        image: wenti
      })
    }
  },
  //每个分数的变化
  changeFen(e) {
    var wenti = this.data.image;
    wenti.scoreIndex = e.detail.value;
    wenti.score = this.data.numList[e.detail.value];
    this.setData({
      image: wenti
    })
  },
  timuSave() {
    var wenti = this.data.image;
    if (this.check()) {
      redis.put("wenti", wenti, 600)
      wx.navigateBack({
        delta: 1
      })
    }
  },
  check() {
    //验证
    var image = this.data.image;
    if (image.name == "") {
      dialog.warnModal("错误", "请输入题目");
      return false;
    }
    return true;
  },

  //所有为输 题目 的同步方法
  bindimageKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var image = this.data.image;
    image[`${attr}`] = e.detail.value
    this.setData({
      image: image
    })
  },
})