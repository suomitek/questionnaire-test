// pages/wenjuan/detailed.js
var util = require('../../utils/util.js');
var urls = require('../../utils/urls.js');
var dialog = require('../../libs/dialog/dialog.js');
var myValidate = require('../../libs/MyValidate.js');
var bmapLocation = require('../../libs/bmapLocation.js');
var redis = require('../../libs/redis.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host:urls.host,
    wentis: [],
    setting:{},
    inputCode: true,
    userinfo: true,
  },
  

  tijiao(){

    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    if (wxUserInfo.phone == null || wxUserInfo.phone==""){
      dialog.warnModalBack("错误", "请完善个人信息", function () {
        wx.navigateTo({
          url: '/pages/my/xinxi/xinxi',
        })
      });
      
      // this.setData({ userinfo:false})
      return;
    }

    var num = this.check();
    if (num!=0){
      dialog.warnModal("错误", "还有" + num+"道题目存在错误");
      return;
    }

    var that=this;
    var setting = this.data.setting;
    var code = setting.code;
    var questionId = this.data.setting.id;
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    var data = {
      questionId: questionId,
      wxUserId: wxUserInfo.id,
    }
    if (setting.suspend != 1) {//不为1就是暂停
      dialog.warnModal("错误", "活动暂停");
      return;
    }
    if (setting.overt != 1 && (typeof (code) === "undefined" || code == "")) {//不公开且 密码码没有
      this.showInputCode()
      return;
    }
    data.code = code//设置投票码

    var locationInfo = redis.get('locationInfo');
    if (setting.restrictFlag == 1 && locationInfo == null) {//有地区限制
      // if (that.data.locationLogin == false) {
      this.getLocation(1, 1, function (qid, index){
        that.tijiao(); 
      })
    }
    if (setting.restrictFlag == 1) {//有地区限制  设置地址信息
      var i=0; 
      while (locationInfo==null){
        util.sleep(6)
        i++;
        if(i>5){
          dialog.warnModal("错误", "定位超时");
          break;
        }
      }
      data.province = locationInfo.cityInfo.province;
      data.city = locationInfo.cityInfo.city;
      data.district = locationInfo.cityInfo.district;
      data.street = locationInfo.cityInfo.street;
      data.streetNumber = locationInfo.cityInfo.streetNumber;
      data.latitude = locationInfo.latitude;
      data.longitude = locationInfo.longitude;
    }
      
    var wentis = this.data.wentis;
    data.wentisStr = JSON.stringify(wentis);



    var url = urls.urlobj.reply;
    console.log("可以了");
    console.log(data);
    setting['code'] = "";////////////清除投票码
    that.setData({
      setting: setting
    })
    util.getServerData(url, data, null).then(function (res) {
      if (res.data.resultCode == '200') {
        dialog.warnModal("成功", "提交成功");
        setting.answer = 1
        that.setData({
          setting: setting
        })
      }
    });
  },
  
  //用户信息
  input1: function (e) {
    var setting = this.data.setting;
    setting[e.currentTarget.dataset.attr] = e.detail.value; 
    this.setData({ setting: setting })
  },
  model1cancel: function (e) {
    this.setData({ userinfo: true })
  },
  model1confirm: function (e) {
    var that = this;
    if (this.data.setting.realName == "") {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      })
    } else if (this.data.setting.phone == "") {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      })
    }else{
      
      var url = urls.urlobj.editInfo;
      var wxUserInfo = wx.getStorageSync('wxUserInfo');
      var data = {
        id: wxUserInfo.id,
        phone: that.data.setting.phone,
        realName: that.data.setting.realName,
      }
      util.getServerData(url, data, null).then(function (res) {
        if (res.data.resultCode == '200') {
          wx.setStorageSync('wxUserInfo', res.data.data);
          that.setData({ userinfo: true })
          dialog.warnModal("成功", "修改成功请提交问卷");
        }
      });
    }
  },

  //投票码
  input: function (e) {
    var setting = this.data.setting;
    setting["code"] = e.detail.value;
    this.setData({ setting: setting })
  },
  showInputCode() {
    this.setData({ inputCode: false })
  },
  model2confirm: function (e) {
    if (this.data.code == "") {
      wx.showToast({
        title: '请输入问卷密码',
        icon: 'none'
      })
    } else {
      this.tijiao()
      this.setData({ inputCode: true })
    }
  },
  model2cancel: function (e) {
    this.setData({ inputCode: true })
  },
  check(){
    var wentis = this.data.wentis;
    var num = 0;
    for (var i = 0; i < wentis.length;i++){
      var errorData={};
      var wenti = wentis[i]
      if (wenti.qType === "location" || wenti.qType === "position") {//////////////定位或选位置
        errorData = this.dingweizhiCheck(wenti);
        wenti.errorFlag = errorData.errorFlag;
        if (wenti.errorFlag) {
          num++;
        }
        wenti.errorMsg = errorData.errorMsg;
        wentis[i] = wenti;
        continue;
      }
      if (wenti.qType === "tupian" || wenti.qType === "wenjian") {//////////////图片或文件
        errorData = this.fileImgCheck(wenti);
        wenti.errorFlag = errorData.errorFlag;
        if (wenti.errorFlag) {
          num++;
        }
        wenti.errorMsg = errorData.errorMsg;
        wentis[i] = wenti;
        continue;
      }
      if (wenti.qType === "danxuan") {//////////////单选
        errorData = this.danxuanCheck(wenti);
        wenti.errorFlag = errorData.errorFlag;
        if (wenti.errorFlag) {
          num++;
        }
        wenti.errorMsg = errorData.errorMsg;
        wentis[i] = wenti;
        continue;
      }
      if (wenti.qType === "duoxuan") {//////////////duo选
        errorData = this.duoxuanCheck(wenti);
        wenti.errorFlag = errorData.errorFlag;
        if (wenti.errorFlag) {
          num++;
        }
        wenti.errorMsg = errorData.errorMsg;
        wentis[i] = wenti;
        continue;
      }
      if (wenti.qType === "pingfen") {//////////////评分题
        errorData = this.pingfenCheck(wenti);
        wenti.errorFlag = errorData.errorFlag;
        if (wenti.errorFlag) {
          num++;
        }
        wenti.errorMsg = errorData.errorMsg;
        wentis[i] = wenti;
        continue;
      }
      if (wenti.qType === "jianda") {//////////////简答题
        errorData = this.jiandaCheck(wenti);
        wenti.errorFlag = errorData.errorFlag;
        if (wenti.errorFlag) {
          num++;
        }
        wenti.errorMsg = errorData.errorMsg;
        wentis[i] = wenti;
        continue;
      }
      if (wenti.qType === "tiankong") {//////////////填空题题
        errorData = this.tiankongCheck(wenti);
        wenti.errorFlag = errorData.errorFlag;
        if (wenti.errorFlag) {
          num++;
        }
        wenti.errorMsg = errorData.errorMsg;
        wentis[i] = wenti;
        continue;
      }
    }
    this.setData({
      wentis: wentis
    })
    return num;
  },
  dingweizhiCheck(wenti) {
    if (wenti.must == 1) {
      if (wenti.qType == "location" && wenti.location == "") {
        return {//错误
          errorFlag: true,
          errorMsg: "选择定位"
        }
      }
      if (wenti.qType == "position" && wenti.position == "") {
        return {//错误
          errorFlag: true,
          errorMsg: "选择地址"
        }
      }
    }
    return {//正确
      errorFlag: false,
      errorMsg: ""
    }
  },
  fileImgCheck(wenti) {
    if (wenti.must == 1) {
      if (wenti.qType == "wenjian" && wenti.filePath == "") {
        return {//错误
          errorFlag: true,
          errorMsg: "请上传文件"
        }
      }
      if (wenti.qType == "tupian" && wenti.imgPath == "") {
        return {//错误
          errorFlag: true,
          errorMsg: "请上传图片"
        }
      }
    }
    return {//正确
      errorFlag: false,
      errorMsg: ""
    }
  },
  tiankongCheck(wenti){//填空题验证 //填空题
    if (wenti.must == 1) {
      if (myValidate.fit(wenti.checkType, util.trim(wenti.answer))) {
        return {//正确
          errorFlag: false,
          errorMsg: ""
        }
      } else {
        return {//错误
          errorFlag: true,
          errorMsg: wenti.msg
        }
      }
    }
    return {//正确
      errorFlag: false,
      errorMsg: ""
    }
  },
  jiandaCheck(wenti){//简答题验证
    var msg = "必答题简答题不能为空";
    var answer = wenti["answer"] ;
    if (wenti.must == 1) {
      if (util.trim(answer).length < 1) {//简答题为空
        return {//错误
          errorFlag: true,
          errorMsg: msg
        }
      }
    }
    return {//正确
      errorFlag: false,
      errorMsg: ""
    }
  },
  pingfenCheck(wenti) {//评分题验证
    var msg = "必答题";
    var fen = wenti.fen;
    if (wenti.must == 1){
      if (typeof (fen) === "undefined") {
        fen = 0
      }
      if (fen < 1 || fen > wenti.menFen) {
        return {//错误
          errorFlag: true,
          errorMsg: msg
        }
      }
    }
    return {//正确
      errorFlag: false,
      errorMsg: ""
    }
  },
  duoxuanCheck(wenti) {//多选题验证
    var msg = "必答多择题至少选择两个答案";
    var choices = wenti.choices;
    if (wenti.must == 1) {//必选
      var num = 0;
      choices.forEach(function (choice) {
        if (choice.flag) {//选择了
          num++;
        }
      });
      if (num < 2) {
        return {//错误
          errorFlag: true,
          errorMsg: msg
        }
      }
    }
    return {//正确
      errorFlag: false,
      errorMsg: ""
    }
  },
  danxuanCheck(wenti) {//单选题验证
    var msg = "必答选择题选择一个答案";
    var choices = wenti.choices;
    if (wenti.must == 1) {//必选
      var num = 0;
      choices.forEach(function (choice) {
        if (choice.flag) {//选择了
          num++;
        }
      });
      if (num != 1) {
        return {//错误
          errorFlag: true,
          errorMsg: msg
        }
      } 
    }
    return {//正确
      errorFlag: false,
      errorMsg: ""
    }
  },
  

  //位置输入题目
  onShow() {//位置选择题 需要的
    var that = this;
    var position = redis.get("position");
    var data = redis.get("data");
    if (position != null) {
      var wentis = that.data.wentis;
      var wenti = wentis[parseInt(data)];
      wenti['position'] = position
      wentis[parseInt(data)] = wenti;
      wenti.errorFlag = false;
      wenti.errorMsg = "";
      that.setData({
        wentis: wentis,
      })
      redis.remove("position");
      redis.remove("data");
    }
  },
  position(e) {
    var qid = e.currentTarget.dataset.qid;//题目id
    var index = e.currentTarget.dataset.index;//题号
    var wentis = this.data.wentis;
    var wenti = wentis[index];
    var weizhi = "";
    var position = wenti.position;
    if (position != null && position != "") {
      weizhi = position.address
    }
    wx.navigateTo({
      url: '/pages/position/position?weizhi=' + weizhi + "&data=" + index,
    })
  },
  //位置输入题目 结束 结束 结束 结束

  //定位题
  location(e) {
    var qid = e.currentTarget.dataset.qid;//题目id
    var index = e.currentTarget.dataset.index;//题号
    this.getLocation(qid,index,this.locationCalldack)
  },
  locationCalldack(qid, index) {
    var wentis = this.data.wentis;
    var wenti = wentis[index];
    var location = redis.get('locationInfo');
    if (location != null) {
      wenti['location'] = location;
      wentis[index] = wenti;
      wentis[index].errorFlag = false;
      wentis[index].errorMsg = "";
      this.setData({
        wentis: wentis,
      })
    } 
  },
  getLocation(qid,index,calldack) {
    var that = this;
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userLocation'] != true) {
          //用户没授权过
          wx.authorize({
            scope: 'scope.userLocation',
            success(res) {
              bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
                if (flag == 1) {
                  calldack(qid, index)
                } else {
                  dialog.warnModal("错误", "定位失败1");
                }
              })
            },
            fail(res) {//拒绝后再次授权
              wx.openSetting({
                success(res) {
                  if (res.authSetting["scope.userLocation"]) {
                    //为true表示用户已同意获得定位信息，此时调用getlocation可以拿到信息
                    bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
                      if (flag == 1) {
                        calldack(qid, index)
                      } else {
                        dialog.warnModal("错误", "定位失败2");
                      }
                    })
                  }
                }
              })
            }
          })
        } else {
          //用户授权过
          bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
            if (flag == 1) {
              calldack(qid, index)
            } else {
              dialog.warnModal("错误", "定位失败3");//苹果这里老出现这个错误，我都懵逼了//但是她的定位信息是对的、、、、、、、
              calldack(qid, index)
            }
          })
        }
      }
    })
  },
  //定位题 结束




  //文件上传题
  uploadFile(e) {
    var that = this;
    var qid = e.target.dataset.qid;//题目id
    var index = e.target.dataset.index;//题号
    var wentis = this.data.wentis;
    var wenti = wentis[index];
    var fileId = wenti.fileId;
    if (typeof (fileId) == "undefined") {
      fileId = 0
    }
    wx.chooseMessageFile({
      count: 1,
      type: 'file',
      success(res) {
        // console.log(res)
        // tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFiles
        // console.log(tempFilePaths[0])

        wx.uploadFile({
          url: urls.urlobj.fileImgChangeSave,
          filePath: tempFilePaths[0].path,
          name: 'Filedata',
          header: {
            "Content-Type": "multipart/form-data",
            'accept': 'application/json',
          },
          formData: {
            id: fileId,
            imgFlag: 0//不是图片
          },
          success: function (res) {
            var data = JSON.parse(res.data)
            wenti.fileId = data.data.id;
            wenti.filePath = data.data.filePath;
            wentis[index] = wenti;
            wentis[index].errorFlag = false;
            wentis[index].errorMsg = "";
            that.setData({
              wentis: wentis,
            })
          },
          fail: function (res) {
            wx.showToast({
              title: "上传失败",
              icon: 'success',
              duration: 2000
            })
          },
        })
      }
    })
  },
  //图片上传题
  uploadImage(e) {
    var that = this;
    var qid = e.target.dataset.qid;//题目id
    var index = e.target.dataset.index;//题号
    var wentis = this.data.wentis;
    var wenti = wentis[index];
    var imgId = wenti.imgId;
    if (typeof (imgId) =="undefined"){
      imgId = 0
    }
    wx.chooseImage({
      count: 1,//暂时只写了单图上传
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success(res) {
        var tempFilePaths = res.tempFilePaths;
        wx.uploadFile({
          url: urls.urlobj.fileImgChangeSave,
          filePath: tempFilePaths[0],
          name: 'Filedata',
          header: {
            "Content-Type": "multipart/form-data",
            'accept': 'application/json',
          },
          formData: {
            id: imgId,
            imgFlag: 1
          },
          success: function (res) {
            var data = JSON.parse(res.data)
            wenti.imgId=data.data.id;
            wenti.imgPath = data.data.filePath;
            wentis[index] = wenti;
            wentis[index].errorFlag = false;
            wentis[index].errorMsg = "";
            that.setData({
              wentis: wentis,
            })
          },
          fail: function (res) {
            wx.showToast({
              title: "上传失败",
              icon: 'success',
              duration: 2000
            })
          },
        })
      }
    })
  },

  pingfen(e){///////////评分题
    var qid = e.target.dataset.qid;//题目id
    var index = e.target.dataset.index;//题号
    var fen = e.target.dataset.fen;//分数
    var wentis = this.data.wentis;
    var wenti = wentis[index];
    wenti.fen = fen
    wentis[index] = wenti;
    wentis[index].errorFlag = false;
    wentis[index].errorMsg = "";
    this.setData({
      wentis: wentis,
    })
  },
  bindKeyInput(e) {//////////文本题
    var val = e.detail.value;
    var qid = e.target.dataset.qid;//题目id
    var index = e.target.dataset.index;//题号
    var wentis = this.data.wentis;
    var wenti = wentis[index];
    wenti.answer = util.trim(val);//
    wenti.answer = JSON.stringify(wenti["answer"])
    if (val==""||val==null){
      wenti.answer="";
    }else{
      wenti.answer = wenti.answer.substring(1, wenti.answer.length-1)
    }
    //验证天空类型的数据是否正确
    if (wenti.qType ==="tiankong"){
      var errorData = this.tiankongCheck(wenti);
      wenti.errorFlag = errorData.errorFlag;
      wenti.errorMsg = errorData.errorMsg;
    }else{
      var errorData = this.jiandaCheck(wenti);
      wenti.errorFlag = errorData.errorFlag;
      wenti.errorMsg = errorData.errorMsg;
    }

    wentis[index] = wenti;
    this.setData({
      wentis: wentis,
    })
  },

  checkboxChange: function (e) {//////////////////多选题
    var indexs = e.detail.value;
    var qid = e.target.dataset.qid;//题目id
    var index = e.target.dataset.index;//题号
    var wentis = this.data.wentis;
    var choices = wentis[index].choices;
    for (var i = 0; i < choices.length; i++) {
      if (indexs.indexOf("" + i) != -1) {
        choices[i].flag = true;
      } else {
        choices[i].flag = false;
      }
    }
    wentis[index].choices = choices;
    this.setData({
      wentis: wentis,
    })
  },
  radioChange: function (e) {//单选框单选题
    var cindex = e.detail.value;//第几个选项
    var qid = e.target.dataset.qid;//题目id
    var index = e.target.dataset.index;//题号
    var wentis = this.data.wentis;
    var wenti = wentis[index];
    var choices = wentis[index].choices;
    for (var i = 0; i < choices.length;i++){
      if(i==cindex){
        choices[i].flag = true;
      }else{
        choices[i].flag = false;
      }
    }
    wentis[index].choices = choices;
    this.setData({
      wentis:wentis,
    })
  },



  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    // var addWenJuanData = wx.getStorageSync("addWenJuanData")//取出的草稿 
    
    var mod = options.model;
    if (typeof (mod) !='undefined' ){
      var addWenJuanData = wx.getStorageSync("addWenJuanData")//取出的草稿 
      this.setData({
        wentis: addWenJuanData.wentis,
        setting: addWenJuanData.setting,
        mod: mod
      })
      return;
    }
    var id = options.id; 
    // id = 6;///////////////////////////////////////////暂时用2
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    var wxUserId = options.wxUserId;
    if (typeof (wxUserId) ==="undefined"){
      wxUserId = wxUserInfo.id
    }
    var url = urls.urlobj.questionDetailed;
    util.getServerData(url, {
      id: id,
      wxUserId: wxUserId
    }, null).then(function (res) {
      if (res.data.resultCode == '200') {
        // console.log(res.data.data);
        var questionVo = res.data.data;
        var setting = JSON.parse(questionVo.questionDataStr);
        setting.startTimeStr = questionVo.startTimeStr.replace(/-/g, '/');
        setting.endTimeStr = questionVo.endTimeStr.replace(/-/g, '/');
        setting.overt = questionVo.overt;
        setting.answer = questionVo.answer;
        setting.fraction = questionVo.fraction;
        setting.examineFlag = questionVo.examineFlag;
        setting.sumScore = questionVo.sumScore;
        if (questionVo.name.length > 10) {
          questionVo.name2 = questionVo.name.substring(0, 10) + " . . .";
        }
        setting.suspend = questionVo.suspend;
        that.setData({
          mod:'zhengshi',
          wxUserInfo: wxUserInfo,
          setting: setting,
          wentis: JSON.parse(questionVo.problemsStr),
        })
        that.getColor();
        let endTimeList = [];
        that.setData({ actEndTimeList: endTimeList });
        that.countDown();
      }
    });
  },
  goxiangqin: function () {//查看详情
    var setting = this.data.setting;
    wx.setStorageSync("thisWenjuanSetting", setting)
    wx.navigateTo({
      url: '/pages/wenjuan/xiangqin'
    })
  },
  timeFormat(param) {//小于10的格式化函数
    return param < 10 ? '0' + param : param;
  },
  countDown() {//倒计时函数
    // 获取当前时间，同时得到活动结束时间数组
    let newTime = new Date().getTime();
    let endTimeList = [this.data.setting.startTimeStr, this.data.setting.endTimeStr];
    let countDownArr = [];

    // 对结束时间进行处理渲染到页面
    endTimeList.forEach(o => {
      let endTime = new Date(o).getTime();
      let obj = null;
      // 如果活动未结束，对时间进行处理
      if (endTime - newTime > 0) {
        let time = (endTime - newTime) / 1000;
        // 获取天、时、分、秒
        let day = parseInt(time / (60 * 60 * 24));
        let hou = parseInt(time % (60 * 60 * 24) / 3600);
        let min = parseInt(time % (60 * 60 * 24) % 3600 / 60);
        let sec = parseInt(time % (60 * 60 * 24) % 3600 % 60);
        obj = {
          day: this.timeFormat(day),
          hou: this.timeFormat(hou),
          min: this.timeFormat(min),
          sec: this.timeFormat(sec)
        }
      } else {//活动已结束，全部设置为'00'
        obj = {
          day: '00',
          hou: '00',
          min: '00',
          sec: '00'
        }
      }
      countDownArr.push(obj);
    })
    // 渲染，然后每隔一秒执行一次倒计时函数
    this.setData({ countDownList: countDownArr })
    //判断开没开始
    var setting = this.data.setting;
    if (countDownArr[0].day == "00" && countDownArr[0].hou == "00" && countDownArr[0].min == "00" && countDownArr[0].sec == "00") {
      //已将开始了
      if (countDownArr[1].day == "00" && countDownArr[1].hou == "00" && countDownArr[1].min == "00" && countDownArr[1].sec == "00") {
        //已将结束了
        setting.flag = 3
      }else{
        setting.flag = 2
      }
    } else {
      setting.flag = 1
    }
    if (setting.suspend==0){
      setting.flag = 0
    }
    this.setData({ setting: setting })
    setTimeout(this.countDown, 1000);
  },
  getColor() {
    var colorStyle = {
      timebgcolor: "#f1f1f1",
      weikaishi: "#9f10f1",
      jinxingzhong: "#38c100",
      yijishu: "#f11020",
      zanting: "#f19410"
    }
    // console.log(colorStyle)
    this.setData({
      colorStyle: colorStyle
    })
  },
})