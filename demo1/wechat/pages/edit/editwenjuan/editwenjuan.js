// pages/edit/editwenjuan/editwenjuan.js
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

  },

  onLoad: function (options) {
    var setting = redis.get("editWenJuanDatasetting");
    redis.remove("editWenJuanDatasetting");
    this.setData({
      setting: setting,
    })
  },
  fabu(){
    var that = this;
    if (this.check()){
      var url = urls.urlobj.editSave;
      util.getServerData(url, {
        settingStr: JSON.stringify(that.data.setting),
      }, null).then(function (res) {
        dialog.warnModalBack("成功", "修改成功",function(){
          wx.navigateBack({
            delta: 1,
          })
        });
      });
    }
  },

  check() {
    var setting = this.data.setting;
    var currentschedule = this.data.currentschedule
    var rules = {};
    var messages = {};


    var endTime = new Date(setting.endTimeStr).getTime();
    var time = new Date().getTime();
    var scour = endTime - time;
    if (scour < 60 * 5 * 1000) {
      dialog.warnModal("错误", "结束时间到现在至少为5分钟");
      return false;
    }

    messages.name = {
      required: '请输入问卷名称',
      maxlength: "问卷名称长度不多于15位"
    }
    rules.name = {
      required: true
    }
    messages.introduce = {
      required: '请输入问卷介绍',
    }
    rules.introduce = {
      required: true,
    }
    
    var myWxValidate = new WxValidate(rules, messages)

    var params = JSON.parse(JSON.stringify(this.data.setting));
    console.log(params)
    // 传入表单数据，调用验证方法
    if (!myWxValidate.checkForm(params)) {
      const error = myWxValidate.errorList[0]
      dialog.showToastAutoError(error.msg);
      return false
    }
    return true;
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
        dateTime1 = temp;
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
})