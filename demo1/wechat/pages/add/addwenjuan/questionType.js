// pages/add/addwenjuan/questionType.js
var dialog = require('../../../libs/dialog/dialog.js');
var util = require('../../../utils/util.js');
var urls = require('../../../utils/urls.js');
var redis = require('../../../libs/redis.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    yangshis:[
      {
        name: "单选",
        examineFlag:1,
        qtype: "danxuan",
        img: "../../../imgs/type/danxuan.png"
      }, {
        name: "多选",
        examineFlag: 1,
        qtype: "duoxuan",
        img: "../../../imgs/type/duoxuan.png"
      }, {
        name: "填空",
        qtype: "tiankong",
        examineFlag: 0,
        img: "../../../imgs/type/tiankong.png"
      }, {
        name: "简答",
        qtype: "jianda",
        img: "../../../imgs/type/jianda.png"
      }, {
        name: "评分",
        qtype: "pingfen",
        examineFlag: 0,
        img: "../../../imgs/type/pingfenti.png"
      }, {
        name: "文件",
        qtype: "wenjian",
        examineFlag: 0,
        img: "../../../imgs/type/wenjian.png"
      }, {
        name: "图片",
        qtype: "tupian",
        examineFlag: 0,
        img: "../../../imgs/type/tupian.png"
      }, {
        name: "定位",
        examineFlag: 0,
        qtype: "location",
        img: "../../../imgs/type/dingwei.png"
      },{
        name: "位置",
        examineFlag: 0,
        qtype: "position",
        img: "../../../imgs/type/weizhi.png"
      }, 
      //  {
      //   name: "日期",
      //   examineFlag: 0,
      //   img: "../../../imgs/type/riqi.png"
      // }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var examineFlag = options.examineFlag
    this.setData({
      examineFlag: examineFlag
    })
  },
  tiaozhuan(e) {
    var qtype = e.currentTarget.dataset.qtype;
    var examineFlag = this.data.examineFlag;
    //现在支持试卷全部题型
    // if (!(qtype == "danxuan" || qtype == "duoxuan")){
    //   if (examineFlag == 1){
    //     dialog.warnModal("错误", "考试类型问卷只能添加单选题多选题");
    //     return;
    //   }
    // }
    wx.redirectTo({
      url: '/pages/add/addwenjuan/question/' + qtype + "?examineFlag="+examineFlag
    })
  },
  piliang(e){
    var url = urls.urlobj.piliang;
    var examineFlag = this.data.examineFlag;
    //文件上传
      
    wx.chooseMessageFile({
      count: 2,
      type: 'file',
      success(res) {
        var yuexu = ["xls", "xlsx"]
        var tempFilePaths = res.tempFiles;
        var type = tempFilePaths[0].path;
        type = type.substring(type.lastIndexOf('.')+1, type.length);
        if (yuexu.indexOf(type)==-1) {
          dialog.warnModal("错误", "仅支持xls或xlsx格式的文件");
          return;
        }
        wx.uploadFile({
          url: urls.urlobj.fileImgChangeSave,
          filePath: tempFilePaths[0].path,
          name: 'Filedata',
          header: {
            "Content-Type": "multipart/form-data",
            'accept': 'application/json',
          },
          formData: {
            id: 0,
            imgFlag: 0//不是图片
          },
          success: function (res) {///////////////////文件长传完成去解析
            var data = JSON.parse(res.data)
            var filePath = data.data.filePath;
            util.getServerData(url, {
              examineFlag: examineFlag,
              filename: filePath
            }, null).then(function (res) {
              var wentis = JSON.parse(res.data.data.wentis);
              var errorNum = res.data.data.errorNum;
              var susNum = res.data.data.susNum;
              if (susNum>0){
                dialog.warnModalTrueBack("提示", "识别正确" + susNum + "道题,错误" + errorNum+"道题,是否添加?", function (flag) {
                  if (flag == 1) {
                    redis.put("awentis", wentis, 60)
                  }
                  wx.navigateBack({
                    delta: 1,
                  })
                })
              }else{
                dialog.warnModal("错误", "未正确识别");
              }
            });
          },
          fail: function (res) {
            dialog.warnModal("错误", "上传失败");
          },
        })
      }
    })









    
  }
})