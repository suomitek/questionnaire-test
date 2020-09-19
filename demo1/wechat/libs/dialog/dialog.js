// dialog.js

function warnModalBack(title, content, callback) {
  var self = this;
  wx.showModal({
    title: title,
    content: content,
    showCancel: false,
    confirmColor: '#007aff',
    success: function () {
      callback();
    }
  })
}

function warnModal(title, content) {
  var self = this;
  wx.showModal({
    title: title,
    content: content,
    showCancel: false,
    confirmColor: '#007aff',
    success: function () {
    }
  })
}
function warnModalError(content) {
  var self = this;
  wx.showModal({
    title: "错误提示",
    content: content,
    showCancel: false,
    confirmColor: '#007aff',
    success: function () {
    }
  })
}
function warnModalSuccess(content) {
  var self = this;
  wx.showModal({
    title: "成功提示",
    content: content,
    showCancel: false,
    confirmColor: '#007aff',
    success: function () {
    }
  })
}

function warnModalTrueBack(title, content, callback) {
  wx.showModal({
    title: title,
    content: content,
    confirmColor: '#007aff',
    cancelColor: '#007aff',
    confirmText: '是',
    cancelText: '否',
    success: function (res) {
      if (res.confirm) {
        callback(1)
      } else if (res.cancel) {
        callback(0)
      }
    }
  })
}

function showToastAutoError(title) {
  wx.showToast({
    title: title,
    image: '/libs/dialog/imgs/400.png',
    success: function (res) {
    }
  })
}
function showToastAutoSuccess(title) {
  wx.showToast({
    title: title,
    image: '/libs/dialog/imgs/200.png',
    success: function (res) {
    }
  })
}
function showToastAuto(title,icon) {
  wx.showToast({
    title: title,
    image: '/libs/dialog/imgs/' + icon +'.png',
    success: function (res) {
    }
  })
}
function showToastSuccess(title){
  wx.showToast({
    title: '成功',
    icon: 'success',
    success:function(res){
      
    }
  })
}
function showToastLoading(title){
  wx.showToast({
    title: title,
    icon: 'loading',
    success: function (res) {
      
    }
  })
}


module.exports = {
  warnModal: warnModal,
  warnModalError: warnModalError,
  warnModalSuccess: warnModalSuccess,
  
  warnModalBack: warnModalBack,
  warnModalTrueBack: warnModalTrueBack,

  showToastAuto: showToastAuto,
  showToastAutoError: showToastAutoError,
  showToastAutoSuccess: showToastAutoSuccess,

  showToastSuccess: showToastSuccess,
  showToastLoading: showToastLoading
}