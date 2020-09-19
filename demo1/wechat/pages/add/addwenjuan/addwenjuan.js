// pages/add/addwenjuan/addwenjuan.js
var dateTimePicker = require('../../../libs/dateTimePicker.js'); 
var citesPicker = require('../../../libs/citesPicker.js');
var dialog = require('../../../libs/dialog/dialog.js');
import WxValidate from '../../../libs/WxValidate'
var util = require('../../../utils/util.js');
var urls = require('../../../utils/urls.js');
var redis = require('../../../libs/redis.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    click: false, //是否显示弹窗内容
    option: false, //显示弹窗或关闭弹窗的操作动画
    host:urls.host,
    thisWenTi:-1,
    model:"bianji",
    wentis: [],
    //进度
    currentschedule: 0,
    scheduleList: [{
      title: "活动内容"
      }, {
        title: "问卷设置"
      }, {
        title: "题目项"
    }],
    setting:{
      cover:[""],//
      coverId:[0],
      name:"",
      introduce:"",
      imgIntroduce:[],
      imgIntroduceId:[],

      themeColor:"#000",
      restrictFlag: 0,//地区限制
      overt: 1,//默认公开

      sendFlag:0,//默认开启 自咚发送
      sponsor: 0,//主办方
      comment: 1,//开启评论
      outside: 0,//不开放报名
      ipWxUserFrequency:1,
      examineFlag:0,//问卷类型 1考试卷

      //次数,同微信数
      numList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      numIndex: 0,
      ipIndex: 0, 
      //时间
      startYear: 2000,
      endYear: 2050,
      mailbox:"",//邮箱

      startTimeStr: util.timeToStr(new Date()),
      endTimeStr: util.timeToStr(new Date()),
    },
  },
  questionType(){
    var examineFlag = this.data.setting.examineFlag;
    wx.navigateTo({
      url: '/pages/add/addwenjuan/questionType?examineFlag=' + examineFlag
    })
  },
  onHide(){
    this.setDraft(1)//页面关闭保存草稿
  },
  setDraft(flag) { // 设置草稿
    if (flag == 1) {//保存
      var setting = this.data.setting;
      var wentis = this.data.wentis;
      var addWenJuanData={
        setting: setting,
        wentis: wentis
      }
      wx.setStorageSync("addWenJuanData", addWenJuanData)//只保存需要的数据
    } else { // 删除草稿
      var key = "addWenJuanData";
      wx.removeStorageSync(key)
    }
  },
  onShow(){
    var wenti = redis.get("wenti")
    redis.remove("wenti")
    var awentis = redis.get("awentis")
    var wentis = this.data.wentis
    if (wenti!=null){
      if (wenti.orderNum==-1){
        wentis.push(wenti);
      }else{
        wentis[wenti.orderNum] = wenti;
      }
      this.setDraft(1)//保存草稿
    } else if (awentis!=null){
      redis.remove("awentis")
      wentis = wentis.concat(awentis)
    }
    this.setData({
      wentis: wentis
    })
  },
  fabu(){

    if (!this.check()){
      return;
    }
    var that = this;
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    var url = urls.urlobj.questionAddSave;
    util.getServerData(url, {
      wxUserId: wxUserInfo.id,
      settingStr: JSON.stringify(that.data.setting),
      wentisStr: JSON.stringify(that.data.wentis),
    }, null).then(function (res) {
      console.log(res);/////////////////////////////////////////////////////原则添加成功 要删除草稿
      // that.setDraft(0)//上传成功 草稿 要 删除
      dialog.warnModalTrueBack("提示", "是否使前往问卷页面?", function (flag) {
        if (flag == 1) {
          wx.reLaunch({// 关闭所有页面 不在让他往 回跳了
            url: '/pages/wenjuan/detailed?id=' + res.data.data.id,
          })
        } else {
          wx.navigateBack({
            delta: 1,
          })
        }
      })
    });
  },
  yulan(){
    wx.navigateTo({// 预览
      url: '/pages/wenjuan/detailed?model=yulan',
    })
  },
  
  bianji(e){//编辑
    var wentis = this.data.wentis;
    var thisWenTi = this.data.thisWenTi;
    var examineFlag = this.data.setting.examineFlag;
    var wenti = wentis[thisWenTi]
    wenti.orderNum = thisWenTi//编辑的时候 放一个排序号
    redis.put("wenti", wenti,600)
    var qtype = e.currentTarget.dataset.qtype;
    wx.navigateTo({
      url: '/pages/add/addwenjuan/question/' + qtype + "?examineFlag=" + examineFlag
    })
  },
  fuzhi() {//复制
    var thisWenTi = this.data.thisWenTi;
    var wentis = this.data.wentis;
    wentis.splice(thisWenTi, 0, wentis[thisWenTi]);
    thisWenTi+=1;
    this.setData({
      wentis: wentis,
      thisWenTi: thisWenTi,
    });
    that.setDraft(1)//保存草稿
  },
  shangyi() {
    var thisWenTi = this.data.thisWenTi;
    if (thisWenTi<1){
      return;
    }
    var wentis = this.data.wentis;
    var temp = wentis[thisWenTi-1];
    wentis[thisWenTi-1] = wentis[thisWenTi];
    wentis[thisWenTi] = temp;
    thisWenTi-=1;
    this.setData({
      wentis: wentis,
      thisWenTi: thisWenTi,
    });
    this.setDraft(1)//保存草稿
  },
  xiayi() {
    var thisWenTi = this.data.thisWenTi;
    var wentis = this.data.wentis;
    if (thisWenTi >= wentis.length) {
      return;
    }
    var temp = wentis[thisWenTi + 1];
    wentis[thisWenTi+1] = wentis[thisWenTi];
    wentis[thisWenTi] = temp;
    thisWenTi += 1;
    this.setData({
      wentis: wentis,
      thisWenTi: thisWenTi,
    });
    this.setDraft(1)//保存草稿
  },
  shanchu() {
    var that = this
    dialog.warnModalTrueBack("提示", "是否删除题目?", function (flag) {
      if (flag == 1) {
        var thisWenTi = that.data.thisWenTi;
        var wentis = that.data.wentis;
        var newthisWenTi = JSON.parse(JSON.stringify(wentis));
        newthisWenTi.splice(thisWenTi, 1);
        if (thisWenTi + 1 == wentis.length) {
          thisWenTi -= 1
        }
        that.setData({
          wentis: newthisWenTi,
          thisWenTi: thisWenTi,
        })
        that.setDraft(1)//保存草稿
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var addWenJuanData = wx.getStorageSync("addWenJuanData")//取出的草稿
    if (addWenJuanData != null && addWenJuanData != "") {//有草稿 
      dialog.warnModalTrueBack("提示", "是否使用草稿?", function (flag) {
        if (flag == 1) {
          that.setData({
            wentis: addWenJuanData.wentis,
            setting: addWenJuanData.setting,
          })
          return;
        } else {
          that.setDraft(0)//不要草稿 要 删除
          that.initSetting()//出初始化
        }
      })
    }else{
      this.initSetting()//出初始化
    }

  },
  xuanti(e){
    var index = e.target.dataset.index;
    this.setData({
      thisWenTi: index
    })
  },
  //图片上传
  previewImage(e) {
    var that = this;
    var setting = this.data.setting;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = setting[`${attr}Id`]

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
    var setting = this.data.setting;
    console.log(index)
    if (index == -1) {
      var filePath = data.data.filePath
      setting[`${attr}`].unshift(filePath)
      setting[`${attr}Id`].unshift(data.data.id);
    } else {
      setting[`${attr}`][index] = data.data.filePath;
      setting[`${attr}Id`][index] = data.data.id;
    }
    this.setData({
      setting: setting
    })
  },
  //开始时间变化
  changeDateTime(e) {
    var setting = this.data.setting;
    setting.dateTime = e.detail.value;
    this.setData({ setting: setting });
    this.compare(0)
  },
  //开始时间变化
  changeDateTimeColumn(e) {
    var arr = this.data.setting.dateTime, dateArr = this.data.setting.dateTimeArray;
    arr[e.detail.column] = e.detail.value;
    dateArr[2] = dateTimePicker.getMonthDay(dateArr[0][arr[0]], dateArr[1][arr[1]]);
    var setting = this.data.setting
    setting.dateTimeArray = dateArr
    this.setData({ setting: setting });
  },
  //结束时间变化
  changeDateTime1(e) {
    var setting = this.data.setting;
    setting.dateTime1 = e.detail.value;
    this.setData({ setting: setting });
    this.compare(1)
  },
  //结束时间变化
  changeDateTimeColumn1(e) {
    var arr1 = this.data.setting.dateTime1, dateArr1 = this.data.setting.dateTimeArray1;
    arr1[e.detail.column] = e.detail.value;
    dateArr1[2] = dateTimePicker.getMonthDay(dateArr1[0][arr1[0]], dateArr1[1][arr1[1]]);
    var setting = this.data.setting
    setting.dateTimeArray1 = dateArr1
    this.setData({ setting: setting });
  },
  //核对开始，结束时间
  compare: function (flag) {//flag ==  0  变开始  flag ==  1  变结束
    var setting = this.data.setting
    var dateTime1 = setting.dateTime1
    var dateArr = setting.dateTimeArray
    var dateArr1 = setting.dateTimeArray1
    var dateTime = setting.dateTime;
    var time = util.timeStrTostamp(dateArr[0][dateTime[0]] + "-" + dateArr[1][dateTime[1]] + "-" + dateArr[2][dateTime[2]] + " "
      + dateArr[3][dateTime[3]] + ":" + dateArr[4][dateTime[4]] + ":" + dateArr[5][dateTime[5]]);

    var time1 = util.timeStrTostamp(dateArr1[0][dateTime1[0]] + "-" + dateArr1[1][dateTime1[1]] + "-" + dateArr1[2][dateTime1[2]] + " "
      + dateArr1[3][dateTime1[3]] + ":" + dateArr1[4][dateTime1[4]] + ":" + dateArr1[5][dateTime1[5]]);

    if (flag == 0) {
      
      if (parseInt(time) > parseInt(time1)) {//开始比结束更晚
        var temp = new Array(dateTime.length);
        for (var i = 0; i < dateTime.length; i++) {
          temp[i] = dateTime[i]
        }
        dateTime1=temp;
        time1 = time
      }
    } else {
      if (parseInt(time) > parseInt(time1)) {//结束比开始更早
        var temp = new Array(dateTime1.length);
        for (var i = 0; i < dateTime1.length; i++) {
          temp[i] = dateTime1[i]
        }
        dateTime = temp;
        time = time1
      }
    }
    setting.dateTime = dateTime
    setting.dateTime1 = dateTime1
    setting.startTimeStr = time;
    setting.endTimeStr = time1;
    this.setData({ setting: setting });
  },
  // 拾色器
  onChangeColor(e) {
    const index = e.target.dataset.id
    var setting = this.data.setting;
    setting[`colorData${index}`] = e.detail.colorData
    setting.themeColor = e.detail.colorData.pickerData.hex
    this.setData({ setting: setting })
  },
  toggleColorPicker(e) {
    var setting = this.data.setting;
    const index = e.currentTarget.dataset.id
    setting[`showColorPicker${index}`] = !setting[`showColorPicker${index}`]
    this.setData({ setting: setting })
  },
  closeColorPicker() {
    var setting = this.data.setting;
    setting.showColorPicker1 = false
    this.setData({ setting: setting })
  },
  // 选择省市区函数
  //子级别 地区变化
  changeCitesColumn(e) {
    var setting = this.data.setting;
    var arr = setting.thisCites
    var dateArr = setting.citesSelects;
    arr[e.detail.column] = e.detail.value;
    var data = citesPicker.liebiaogenxi(dateArr[0][arr[0]], dateArr[1][arr[1]], dateArr[2][arr[2]], e.detail.column);
    setting.citesSelects=data.citesSelects;
    setting.thisCites=data.thisCites;
    this.setData({
      setting: setting
    })
  },
  //保存地区变化
  changeCites(e) {
    var setting = this.data.setting;
    var citesSelects = this.data.setting.citesSelects
    setting.province = citesSelects[0][e.detail.value[0]];
    setting.city = citesSelects[1][e.detail.value[1]];
    setting.district = citesSelects[2][e.detail.value[2]];
    setting.thisCites = e.detail.value
    this.setData({ setting: setting });
  },
  //地区ip限制
  changeRrestrict(e) {
    var setting = this.data.setting;
    if (e.detail.value == false) {
      setting.restrictFlag = 0
    } else {
      setting.restrictFlag = 1
      //初始化不限地区选择器
      var data = citesPicker.liebiaogenxi("北京", "北京", "不限", 0);
      setting.province = "北京";
      setting.city = "北京";
      setting.district = "不限";
      setting.ipIndex = 0;
      setting.citesSelects = data.citesSelects,
      setting.thisCites = data.thisCites
      this.setData({
        setting: setting
      })
    }
    this.setData({ setting: setting })
  },
  // 所有的switch
  changeSwitch(e) {
    var attr = e.currentTarget.dataset.attr
    var setting = this.data.setting;
    if (e.detail.value) {
      setting[`${attr}`] = 1
    } else {
      setting[`${attr}`] = 0
    }
    this.setData({ setting: setting })
  },
  ////  数字按钮
  changeNum(e) {
    const flag = parseInt(e.target.dataset.flag);
    const attr = e.target.dataset.attr;
    var setting = this.data.setting;
    var min = parseInt(e.target.dataset.min);
    var max = parseInt(e.target.dataset.max);
    var thisStrr = parseInt(setting[`${attr}`]);
    thisStrr = thisStrr + flag
    if (thisStrr >= min && thisStrr<=max){
      setting[`${attr}`] = thisStrr
      this.setData({ setting: setting })
    }
  },
  initSetting(){
    var that = this;
    var setting = this.data.setting;
    //初始化两个时间选择器
    var obj = dateTimePicker.dateTimePicker(this.data.setting.startYear, this.data.setting.endYear);
    var obj1 = dateTimePicker.dateTimePicker(this.data.setting.startYear, this.data.setting.endYear);

    setting.dateTime = obj1.dateTime;
    setting.dateTimeArray = obj1.dateTimeArray;
    setting.dateTimeArray1 = obj1.dateTimeArray;
    setting.dateTime1 = obj1.dateTime;


    

    //初始化不限地区选择器
    var data = citesPicker.liebiaogenxi("北京", "北京", "不限", 0);

    setting.citesSelects = data.citesSelects;
    setting.thisCites = data.thisCites;

    //拾色器初始化
    wx.getSystemInfo({
      success(res) {
        setting.rpxRatio = res.screenWidth / 750
        that.setData({
          setting: setting
        })
      }
    })
    that.setData({
      setting: setting
    })
  },
  //所有为输入框的同步方法
  bindKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var setting = this.data.setting;
    setting[`${attr}`] = e.detail.value
    this.setData({
      setting: setting
    })
  },
  check() {
    var that = this;
    var setting = this.data.setting;
    var currentschedule = this.data.currentschedule
    var rules = {};
    var messages = {};
    switch (parseInt(currentschedule)) {
      case 2:
        var endTime = new Date(setting.endTimeStr).getTime();
        var time = new Date().getTime();
        var scour = endTime - time;
        if (scour < 60 * 5 * 1000) {
          dialog.warnModal("错误", "结束时间到现在至少为5分钟");
          return false;
        }
        var wentis = this.data.wentis;
        if (wentis.length<2){
          dialog.warnModal("错误", "问卷至少两道题");
          return false;
        }
        if(this.data.setting.examineFlag==1){//考试问卷要核对选择题是否有答案
          for(var i = 0;i<wentis.length;i++){
            wentis[i].orderNum = i;//初始化题号
            if (wentis[i].qType === 'danxuan') {
              if (wentis[i].correctNumner != 1) {
                dialog.warnModal("错误", "第" + (i + 1) + "题至少选择一个答案");
                return false;
              }
            } else if (wentis[i].qType === 'duoxuan') {
              if (wentis[i].correctNumner < 2) {
                dialog.warnModal("错误", "第" + (i + 1) + "题至少选择两个答案");
                return false;
              }
            }
          }
        }else{
          for (var i = 0; i < wentis.length; i++) {
            wentis[i].orderNum = i;//初始化题号
            if (wentis[i].qtype === 'danxuan' || wentis[i].qtype === 'duoxuan') {
              var correctNumner = 0;
              for (var x = 0; i < wentis[i].choices.length; x++) {
                wentis[i].choices[x].correctNumner = 0;
                wentis[i].choices[x].score = 0;
                wentis[i].choices[x].examineFlag = 0;
              }
            }
          }
        }
        that.setData({
          wentis: wentis
        })
        return true;
      break;
      case 1: /* 问卷设置 */
        // console.log("问卷设置")
        var endTime = new Date(setting.endTimeStr).getTime();
        var time = new Date().getTime();
        var scour = endTime - time;
        if (scour < 60 * 5 * 1000) {
          dialog.warnModal("错误", "结束时间到现在至少为5分钟");
          return false;
        }
        if (setting.sendFlag == 1) {//请输入接收邮箱
          messages.mailbox = {
            required: '请输入接收邮箱',
            email: '请输入正确邮箱',
          }
          rules.mailbox = {
            required: true,
            email:true,
          }
        }
        if (setting.sponsor == 1) {//显示主办方
          //验证主办方信息
          messages.sponsorName = {
            required: '请输入主办方名称'
          }
          rules.sponsorName = {
            required: true
          }
          messages.sponsorPhone = {
            required: '请输入主办方电话',
            tel: '主办方电话错误',
          }
          rules.sponsorPhone = {
            required: true,
            tel: true
          }
        } else if (setting.sendFlag == 0){
          return true;
        }
        break;
      case 0: /*活动内容*/
        messages = { name: { required: "请输入问卷名称", maxlength: "问卷名称长度不多于15位" }, introduce: { required: "请输入问卷介绍" }, cover: { required: "请输入问卷封面" } }; rules = { name: { required: true, maxlength: 20, }, introduce: { required: true }, cover: { required: true } };
        break;
    }
    var myWxValidate = new WxValidate(rules, messages)

    var params = JSON.parse(JSON.stringify(this.data.setting));
    params.cover = params.cover[0]
    // console.log(params)
    // 传入表单数据，调用验证方法
    if (!myWxValidate.checkForm(params)) {
      const error = myWxValidate.errorList[0]
      dialog.showToastAutoError(error.msg);
      return false
    }
    return true;
  },


  //上一页
  turnSchedule0: function () {
    var currentschedule = this.data.currentschedule - 1;
    this.setData({
      currentschedule: currentschedule
    })
  },
  //下一页
  turnSchedule1: function () {
    var currentschedule = this.data.currentschedule + 1;
    if (this.check()) {//验证没问题就保存
      this.setData({
        currentschedule: currentschedule
      })
      this.setDraft(1)//保存草稿
    }
  },

})