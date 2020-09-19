function identityCodeValid(code) {
  var city = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江 ", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北 ", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏 ", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外 " };

  var tip = "";

  var pass = true;

  if (!code || !/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/i.test(code)) {

    tip = "身份证号格式错误";

    pass = false;

  }

  else if (!city[code.substr(0, 2)]) {

    tip = "地址编码错误";

    pass = false;

  }
  else {
    //18位身份证需要验证最后一位校验位
    if (code.length == 18) {
      code = code.split('');
      //∑(ai×Wi)(mod 11)
      //加权因子
      var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
      //校验位
      var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
      var sum = 0;
      var ai = 0;
      var wi = 0;
      for (var i = 0; i < 17; i++) {
        ai = code[i];
        wi = factor[i];
        sum += ai * wi;
      }
      var last = parity[sum % 11];
      if (parity[sum % 11] != code[17]) {
        tip = "校验位错误";
        pass = false;
      }
    }
  }
  return pass;
}
function qq(text){
  var reg = /^[1-9][0-9]{4,}$/;
  if (reg.test(text) === false) {
    return false;
  } else {
    return true;
  }
}
function shouji(text){
  var myreg = /^1[34578]\d{9}$/;
  if (!myreg.test(text)) {
    return false;
  } else {
    return true;
  }
}
function phone(text){
  var reg = /^\d{3}-\d{8}|\d{4}-\{7,8}$/;
  console.log(typeof ('/^\d{3}-\d{8}|\d{4}-\{7,8}$/'));
  console.log(typeof (JSON.parse('/^\d{3}-\d{8}|\d{4}-\{7,8}$/')))
  // reg = eval("/^\d{3}-\d{8}|\d{4}-\{7,8}$/");
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}
function zipCode(text){
  var reg = /^[1-9]\d{5}(?!\d)$/;
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}
//
function day(text) {
  var reg = /^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$/;
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}
function email(text) {
  var reg = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}
function intZero(text) {
  var reg = /^[1-9]\d*|0$/;
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}
function intNoZero(text) {
  var reg = /^[1-9]\d*$/;
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}
function name(text) {
  var reg = /^[\u4E00-\u9FA5]{2,10}$/;
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}
function feikong(text) {
  var reg = /^.+$/;
  if (!reg.test(text)) {
    return false;
  } else {
    return true;
  }
}

// function zipCode(text) {
//   var reg = /^[1-9]\d{5}(?!\d)$/;
//   if (!reg.test(text)) {
//     return false;
//   } else {
//     return true;
//   }
// }


function fit(type,text){
  if (type === "feikong") {
    return feikong(text);
  }
  if (type === "shouji") {
    return shouji(text);
  }
  if (type === "youxiang") {
    return email(text);
  }
  if (type === "xingming") {
    return name(text);
  }
  if (type === "idcard") {
    return identityCodeValid(text);
  }
  if (type === "riqi") {
    return day(text);
  }
  if (type === "zhengshu") {
    return intNoZero(text);
  }
  if (type === "zhengshu0") {
    return intZero(text);
  } 
  if (type === "qq") {
    return qq(text);
  }
 
}
module.exports = {
  fit: fit,
}