var urls = require('urls.js');
var dialog = require('../libs/dialog/dialog.js');
var redis = require('../libs/redis.js');
const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}
function timeStrTostamp(date) {
  // var date = '2015-03-05 17:59:00';
  date = date.substring(0, 19);
  date = date.replace(/-/g, '/');
  return new Date(date).getTime();
}
function timeToStr(date) {
  //根据时间戳生成的时间对象
  var str = (date.getFullYear()) + "/" +
    (date.getMonth() + 1) + "/" +
    (date.getDate()) + " " +
    (date.getHours()) + ":" +
    (date.getMinutes()) + ":" +
    (date.getSeconds());
  return str
}
//去两端空白  
function trim(s) {
  return trimRight(trimLeft(s));
}
//去掉左边的空白  
function trimLeft(s) {
  if (s == null) {
    return "";
  }
  var whitespace = new String(" \t\n\r");
  var str = new String(s);
  if (whitespace.indexOf(str.charAt(0)) != -1) {
    var j = 0, i = str.length;
    while (j < i && whitespace.indexOf(str.charAt(j)) != -1) {
      j++;
    }
    str = str.substring(j, i);
  }
  return str;
}

//去掉右边的空白 
function trimRight(s) {
  if (s == null) return "";
  var whitespace = new String(" \t\n\r");
  var str = new String(s);
  if (whitespace.indexOf(str.charAt(str.length - 1)) != -1) {
    var i = str.length - 1;
    while (i >= 0 && whitespace.indexOf(str.charAt(i)) != -1) {
      i--;
    }
    str = str.substring(0, i + 1);
  }
  return str;
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}
function getAuthToken(){//////////////////////////////////////////登录保持最新
  var authToken = redis.get("authToken")

  if (authToken==null){//过期了
    let wxUserInfo = wx.getStorageSync("wxUserInfo")
    if (wxUserInfo == null || wxUserInfo==""){//还没有登录呢
      return ""
    }
    wx.request({
      url: urls.urlobj.wxLogin,
      data: {
        openid: wxUserInfo.openid
      },
      header: { 'Content-Type': 'application/json' },
      success: function (res) {
        if (res.data.resultCode == 200) {
          redis.put("authToken", res.data.data,6000)//默认十分钟
          getApp().globalData.authToken = res.data.data;
          return res.data.data;
        } else {
          //微信登录失败
        }
      }
    })
  }else{
    return authToken
  }
}
function sleep(numberMillis) {
  var now = new Date();
  var exitTime = now.getTime() + numberMillis;
  while (true) {
    now = new Date();
    if (now.getTime() > exitTime)
      return true;
  }
}
function getServerData(url, options, failMsg) {
  return new Promise(function (resolve, reject) {
    wx.request({
      url: url,
      data: options,
      method: "POST",
      header: {
        // 'Content-Type': 'application/json',
        "Content-Type": "application/x-www-form-urlencoded",//ssm框架可以正常获取值
        'authToken': getAuthToken()
      },
      success: function (res) {
        if (res.data.resultCode==401){
          wx.redirectTo({
            url: '/pages/unauthorized/unauthorized?msg=' + url
          })
        } if (res.data.resultCode == 400) {
          let wxUserInfo = wx.getStorageSync("wxUserInfo")
          if (!(wxUserInfo == null || wxUserInfo=="")){
            if (failMsg == null || failMsg == '') {
              dialog.warnModal("错误", res.data.resultMsg);
            } else {
              dialog.warnModal("错误", failMsg);
            }
          }
         
        }else{
          resolve(res)
        }
      },
      fail: function (res) {
        reject(res)
      }
    })
  })
}
function fileImgDelete(id, callback){
  wx.request({
    url: urls.urlobj.fileImgDelete,
    data: {
      id: id
    },
    header: { 'content-type': 'application/json' },
    success: function (res) {
      var data = res.data
      if (data.resultcode == 400) {
        wx.showToast({
          title: data.resultmsg,
          icon: 'success',
          duration: 2000
        })
      } else {
        callback()
      }
    }, fail: function (res) {
      wx.showToast({
        title: "删除失败",
        icon: 'success',
        duration: 2000
      })
    },
  })

}
function fileImgChangeSave(tempFilePaths, ids, imgFlag, attr, index, callback){
  wx.uploadFile({
    url: urls.urlobj.fileImgChangeSave,
    filePath: tempFilePaths[0],
    name: 'Filedata',
    header: {
      "Content-Type": "multipart/form-data",
      'accept': 'application/json',
    },
    formData: {
      id: index == -1 ? 0 : ids[index],
      imgFlag: imgFlag
    },
    success: function (res) {
      var data = JSON.parse(res.data)
      if (data.resultCode == 400) {
        wx.showToast({
          title: data.resultmsg,
          icon: 'success',
          duration: 2000
        })
      } else {
        callback(data, attr, index)
      }
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
function singleImgUpload(tempFilePaths, callback){
  var singleImgUpload = urls.urlobj.singleImgUpload
  const uploadTask = wx.uploadFile({
    url: singleImgUpload,      //此处换上你的接口地址
    filePath: tempFilePaths[0],
    name: 'Filedata',
    header: {
      "Content-Type": "multipart/form-data",
      'accept': 'application/json',
    },
    formData: {
      modelName: 'wenjuan',
      entityName: 'voteCover'
    },
    success: function (res) {
      var data = JSON.parse(res.data)
      if (data.resultcode == 400) {
        wx.showToast({
          title: data.resultmsg,
          icon: 'success',
          duration: 2000
        })
      }else{
        callback(data)
      }
    },
    fail: function (res) {
      wx.showToast({
        title: "上传失败",
        icon: 'success',
        duration: 2000
      })
    },
  })
  return uploadTask;
}

/**
 * 下载保存一个文件
 */
function downloadSaveFile(url) {
  console.info(url);
  wx.downloadFile({
    url: url,
    success: function (res) {
      wx.saveFile({
        tempFilePath: res.tempFilePath,
        success: function (result) {
          console.log(result)
          // result.id = id;
          // if (success) {
          //   success(result);
          // }
        },
        fail: function (e) {
          wx.showToast({
            title: "保存文件失败",
            icon: 'success',
            duration: 2000
          })
        }
      })
    },
    fail: function (e) {
      wx.showToast({
        title: "保存文件失败",
        icon: 'success',
        duration: 2000
      })
    }
  })
}


module.exports = {
  downloadSaveFile: downloadSaveFile,
  fileImgChangeSave: fileImgChangeSave,
  fileImgDelete: fileImgDelete,
  singleImgUpload: singleImgUpload,
  timeStrTostamp: timeStrTostamp,
  getServerData: getServerData,
  formatTime: formatTime,
  timeToStr: timeToStr,
  sleep: sleep,
  trim: trim
}
