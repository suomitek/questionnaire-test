function getip(callBack){
  var ip = "";
  wx.request({///////////////////////////上线的话这个端口要换掉 https 的收费 妈的
    url: 'http://ip-api.com/json',
    success: function (e) {
      ip = e.data.query;
      callBack(ip)
    }
  })
}


module.exports = {
  getip: getip
}
