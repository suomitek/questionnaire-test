// pages/add/addwenjuan/question/tiankong.js
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
    checkTypes: ["不限", "整数", "小数", "日期", "手机", "邮箱", "身份证", "QQ", "省份城市"],
    checkType2s: ["buxian", "zhengshu", "xiaoshu", "riqi", "shouji", "youxiang", "shengfenzheng", "qq", "chengshi"],
    checkTypeIndex: 0,
    numList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25],
    tiankong: {
      id: 0,
      questionId: 0,
      name: "",
      qType: "tiankong",// 填空
      cover: [],//
      coverId: [],
      orderNum:-1,
      obtain: -1,
      must: 1,
      lineNumber:1,//行数
      checkType: "",
      reg: "",
      msg:"",
      score: 1,///默认1分
      scoreIndex: 0,///默认0 为 1分
      examineFlag: 0,
    }
  },
  onLoad: function (options) {
    var wenti = redis.get("wenti")
    var examineFlag = parseInt(options.examineFlag);
    if (wenti != null) {
      redis.remove("wenti")
      wenti.examineFlag = examineFlag;
      this.setData({
        tiankong: wenti
      })
    }else{
      wenti = this.data.tiankong;
      wenti.examineFlag = examineFlag;
      this.setData({
        tiankong: wenti
      })
    }
    
    var that = this;
    var checks = redis.get("checks")
    var tiankong = this.data.tiankong;
    if (checks==null){//没有缓存的
      var url = urls.urlobj.listCheck;
      util.getServerData(url, {}, null).then(function (res) {
        redis.put("checks", res.data.data, 600);
        checks = res.data.data;
        tiankong.msg = checks[0].msg
        tiankong.reg = checks[0].reg
        tiankong.checkId = checks[0].id
        tiankong.checkType = checks[0].icon
        var checkTypes = [];
        for (var i = 0; i < checks.length;i++){
          checkTypes.push(checks[i].name);
        }
        that.setData({
          tiankong: tiankong,
          checkTypes: checkTypes,
          checks: checks
        })
        return;
      });
    } 
    var checkTypes = [];
    for (var i = 0; i < checks.length; i++) {
      checkTypes.push(checks[i].name);
    }
    tiankong.msg = checks[0].msg
    tiankong.reg = checks[0].reg
    tiankong.checkId = checks[0].id
    tiankong.checkType = checks[0].icon
    that.setData({
      tiankong: tiankong,
      checkTypes: checkTypes,
      checks: checks
    })
    
  },
  //每个分数的变化
  changeFen(e) {
    var wenti = this.data.tiankong;
    wenti.scoreIndex = e.detail.value;
    wenti.score = this.data.numList[e.detail.value];
    this.setData({
      tiankong: wenti
    })
  },
  timuSave() {
    var wenti = this.data.tiankong;
    if (this.check()) {
      redis.put("wenti", wenti, 600)
      wx.navigateBack({
        delta: 1
      })
    }
  },
  check() {
    //验证
    var tiankong = this.data.tiankong;
    if (tiankong.name == "") {
      dialog.warnModal("错误", "请输入题目");
      return false;
    }    
    return true;
  },
  //图片上传
  previewImage(e) {
    var that = this;
    var tiankong = this.data.tiankong;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = tiankong[`${attr}Id`]

    wx.chooseImage({
      count: 1,//暂时只写了单图上传
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success(res) {
        var tempFilePaths = res.tempFilePaths;
        // var uploadTask = util.singleImgUpload(tempFilePaths,that.huidian)
        util.fileImgChangeSave(tempFilePaths, ids, 1, attr, index, that.addhuidiao)
      }
    })
  },
  //图片上传完成的回调'
  addhuidiao(data, attr, index) {
    var tiankong = this.data.tiankong;
    if (index == -1) {
      var filePath = data.data.filePath
      tiankong[`${attr}`].unshift(filePath)
      tiankong[`${attr}Id`].unshift(data.data.id);
    } else {
      tiankong[`${attr}`][index] = data.data.filePath;
      tiankong[`${attr}Id`][index] = data.data.id;
    }
    this.setData({
      tiankong: tiankong
    })
  },
  //
  changeCheckType(e) {
    var tiankong = this.data.tiankong;
    var checks = this.data.checks;
    tiankong.checkType = checks[e.detail.value].icon;
    tiankong.msg = checks[e.detail.value].msg;
    tiankong.reg = checks[e.detail.value].reg;
    tiankong.checkType = checks[e.detail.value].icon;
    tiankong.checkId = checks[e.detail.value].id;
    this.setData({
      tiankong: tiankong,
      checkTypeIndex: e.detail.value
    })
  },

  //所有为输 题目 的同步方法
  bindTianKongKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var tiankong = this.data.tiankong;
    tiankong[`${attr}`] = e.detail.value
    this.setData({
      tiankong: tiankong
    })
  },
  // 所有的switch
  changeSwitch(e) {
    var attr = e.currentTarget.dataset.attr
    var tiankong = this.data.tiankong;
    if (e.detail.value) {
      tiankong[`${attr}`] = 1
    } else {
      tiankong[`${attr}`] = 0
    }
    this.setData({ tiankong: tiankong })
  },
})