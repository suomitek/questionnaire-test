// pages/templates/question/wenjian.js
var util = require('../../../utils/util.js');
var urls = require('../../../utils/urls.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host:urls.host,
    fileId:0,
    filePath:"",
    imgId:0,
    imgPath: "",
    files:[],
    id: 0,
    questionId: 0,
    name: "",
    orderNum: -1,
    must: 1,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },
  // uploadFile() {
  //   var that = this;
  //   var attr = 1//题号
  //   var fileId = that.data.fileId;
  //   wx.chooseMessageFile({
  //     count: 1,
  //     type: 'file',
  //     success(res) {
  //       // console.log(res)
  //       // tempFilePath可以作为img标签的src属性显示图片
  //       var tempFilePaths = res.tempFiles
  //       // console.log(tempFilePaths[0])

  //       wx.uploadFile({
  //         url: urls.urlobj.fileImgChangeSave,
  //         filePath: tempFilePaths[0].path,
  //         name: 'Filedata',
  //         header: {
  //           "Content-Type": "multipart/form-data",
  //           'accept': 'application/json',
  //         },
  //         formData: {
  //           id: fileId,
  //           imgFlag: 0//不是图片
  //         },
  //         success: function (res) {
  //           var data = JSON.parse(res.data)
  //           that.setData({
  //             fileId: data.data.id,
  //             filePath: data.data.filePath
  //           })
  //         },
  //         fail: function (res) {
  //           wx.showToast({
  //             title: "上传失败",
  //             icon: 'success',
  //             duration: 2000
  //           })
  //         },
  //       })
       
  //     }
  //   })
  // },
  // uploadImage(e) {
  //   var that = this; 
  //   var ids = this.data.imgIds;
  //   var attr = 1//题号
  //   var imgId = that.data.imgId;
  //   wx.chooseImage({
  //     count: 1,//暂时只写了单图上传
  //     sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
  //     sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
  //     success(res) {
  //       var tempFilePaths = res.tempFilePaths;
  //       wx.uploadFile({
  //         url: urls.urlobj.fileImgChangeSave,
  //         filePath: tempFilePaths[0],
  //         name: 'Filedata',
  //         header: {
  //           "Content-Type": "multipart/form-data",
  //           'accept': 'application/json',
  //         },
  //         formData: {
  //           id: imgId,
  //           imgFlag: 1
  //         },
  //         success: function (res) {
  //           var data = JSON.parse(res.data)
  //           that.setData({
  //             imgId: data.data.id,
  //             imgPath: data.data.filePath
  //           })
  //         },
  //         fail: function (res) {
  //           wx.showToast({
  //             title: "上传失败",
  //             icon: 'success',
  //             duration: 2000
  //           })
  //         },
  //       })
  //     }
  //   })
  // },








})