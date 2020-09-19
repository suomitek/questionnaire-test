// pages/add/addwenjuan/question/duoxuan.js
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
    xuanze: {
      id: 0,
      questionId: 0,
      name: "",
      orderNum: -1,//题号排序号
      qType: "duoxuan",// 0单选 1多选 
      cover: [],//
      obtain: -1,
      coverId: [],
      must: 1,
      choicesNunber: 0,
      score: 1,///默认1分
      scoreIndex: 0,///默认0 为 1分
      examineFlag: 0,
      correctNumner: 0,
      choices: [{
        id: 0,
        cover: [],
        coverId: [],
        name: "",
        flag: false
      }, {
        id: 0,
        cover: [],
        coverId: [],
        name: "",
        flag: false
      }]
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var examineFlag = parseInt(options.examineFlag);
    var wenti = redis.get("wenti")
    if (wenti != null) {
      wenti.examineFlag = examineFlag;
      redis.remove("wenti")
      this.setData({
        examineFlag: examineFlag,
        xuanze: wenti
      })

    } else {
      wenti = this.data.xuanze;
      wenti.examineFlag = examineFlag;
      this.setData({
        xuanze: wenti,
        examineFlag: examineFlag
      })
    }
  },
  //每个分数的变化
  changeFen(e) {
    var wenti = this.data.xuanze;
    wenti.scoreIndex = e.detail.value;
    wenti.score = this.data.numList[e.detail.value];
    this.setData({
      xuanze: wenti
    })
  },
  timuSave() {
    var xuanze = this.data.xuanze;
    if (this.check()) {
      redis.put("wenti", this.data.xuanze, 600)
      console.log(xuanze)
      wx.navigateBack({
        delta: 1
      })
    }
  },
  check() {
    var examineFlag = this.data.examineFlag;
    //验证
    var xuanze = this.data.xuanze;
    var choices = this.data.xuanze.choices;
    if (xuanze.name == "") {
      dialog.warnModal("错误", "请输入题目");
      return false;
    }
    if (choices.length < 2) {
      dialog.warnModal("错误", "至少有两个选项");
      return false;
    }
    var number = 0
    for (var i = 0; i < choices.length; i++) {
      if (choices[i].name == "") {//选项内容为空
        dialog.warnModal("错误", "请输入题目第" + (i + 1) + "个选项内容");
        return false;
      }
      if (choices[i].flag) {//选为 正确
        number += 1;
      }
      if (examineFlag == 0) {
        choices[i].flag = false;
      }
    }
    if (number < 2 && examineFlag == 1) {//没有选中那一项为正确
      dialog.warnModal("错误", "考试问卷至少选择两项为答案");
      return false;
    }
    if (examineFlag == 1) {
      xuanze.correctNumner = number;
    }
    xuanze.choicesNunber = choices.length;
    this.setData({
      xuanze: xuanze
    })
    return true;
  },


  //所有为输 题目 的同步方法
  bindXuanZeKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var xuanze = this.data.xuanze;
    xuanze[`${attr}`] = e.detail.value
    this.setData({
      xuanze: xuanze
    })
  },
  //所有为 选项 文本框 的同步方法
  bindXuangXiangKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var index = parseInt(e.currentTarget.dataset.index)
    var xuanze = this.data.xuanze;
    var choices = this.data.xuanze.choices;
    choices[index][`${attr}`] = e.detail.value
    xuanze.choices = choices
    this.setData({
      xuanze: xuanze
    })
  },


  //图片上传 选项图片上传
  previewImage2(e) {
    var that = this;
    var xuanze = this.data.xuanze;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var choices = this.data.xuanze.choices;

    var ids = choices[index][`${attr}Id`]
    // that.addhuidiao2({}, attr,index)
    wx.chooseImage({
      count: 1,//暂时只写了单图上传
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success(res) {
        var tempFilePaths = res.tempFilePaths;
        // var uploadTask = util.singleImgUpload(tempFilePaths,that.huidian)
        wx.uploadFile({
          url: urls.urlobj.fileImgChangeSave,
          filePath: tempFilePaths[0],
          name: 'Filedata',
          header: {
            "Content-Type": "multipart/form-data",
            'accept': 'application/json',
          },
          formData: {
            id: ids.length>0?ids[0]:-1,
            imgFlag: 1
          },
          success: function (res) {
            var data = JSON.parse(res.data)
            console.log(data)
            console.log(data.resultCode)
            if (data.resultCode == 200) {
              choices[parseInt(index)][`${attr}`][0] = data.data.filePath;
              choices[parseInt(index)][`${attr}Id`][0] = data.data.id;
              xuanze.choices = choices
              that.setData({
                xuanze: xuanze
              })
            }
          },
        })
      }
    })
  },
  checkboxChange: function (e) {
    var indexs = e.detail.value;
    var xuanze = this.data.xuanze;
    var choices = this.data.xuanze.choices;
    for (var i = 0; i < choices.length; i++) {
      // choices[parseInt(indexs[i])].flag = true;
      if (indexs.indexOf("" + i) != -1) {
        choices[i].flag = true;
      } else {
        choices[i].flag = false;
      }
    }
    xuanze.choices = choices;
    this.setData({
      xuanze: xuanze
    })
  },
  shanchu(e) {
    var index = e.currentTarget.dataset.index;
    var xuanze = this.data.xuanze;
    var choices = this.data.xuanze.choices;
    choices.splice(index, 1)
    xuanze.choices = choices;
    this.setData({
      xuanze: xuanze
    })
  },
  tianjia() {//
    var xuanze = this.data.xuanze;
    var choices = this.data.xuanze.choices;
    choices.push({
      id: 0,
      cover: [],
      coverId: [],
      name: "",
      flag: false
    })
    xuanze.choices = choices;
    this.setData({
      xuanze: xuanze
    })
  },


  //图片上传
  previewImage(e) {
    var that = this;
    var xuanze = this.data.xuanze;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = xuanze[`${attr}Id`]

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
    var xuanze = this.data.xuanze;
    if (index == -1) {
      var filePath = data.data.filePath
      xuanze[`${attr}`].unshift(filePath)
      xuanze[`${attr}Id`].unshift(data.data.id);
    } else {
      xuanze[`${attr}`][index] = data.data.filePath;
      xuanze[`${attr}Id`][index] = data.data.id;
    }
    this.setData({
      xuanze: xuanze
    })
  },
  // 所有的switch
  changeSwitch(e) {
    var attr = e.currentTarget.dataset.attr
    var xuanze = this.data.xuanze;
    if (e.detail.value) {
      xuanze[`${attr}`] = 1
    } else {
      xuanze[`${attr}`] = 0
    }
    this.setData({ xuanze: xuanze })
  },
})